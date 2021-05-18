package com.montnets.emp.engine.biz;

import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.common.timer.dao.GenericLfTimerVoDAO;
import com.montnets.emp.engine.dao.FetchSendMsgDAO;
import com.montnets.emp.entity.datasource.LfDBConnect;
import com.montnets.emp.entity.engine.LfProcess;
import com.montnets.emp.entity.engine.LfReply;
import com.montnets.emp.entity.engine.LfService;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.system.LfTimerHistory;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.smstask.WGParams;
import com.montnets.emp.util.*;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FetchSendMsgBiz extends SuperBiz {

    //日志biz
    private final AppLogBiz serLogBiz = new AppLogBiz();
    //计费biz
    private final BalanceLogBiz balanceBiz = new BalanceLogBiz();

    private final FetchSendMsgDAO fetchDao = new FetchSendMsgDAO();

    private final KeyWordAtom keyWordAtom = new KeyWordAtom();

    /**
     * 开始下行业务
     *
     * @param serId 业务id
     * @return
     * @throws Exception
     */
    public boolean startService(Long serId) {
        try {
            //服务对象
            LfService service = empDao.findObjectByID(LfService.class, serId);
            if (service == null) {
                EmpExecutionContext.error("下行业务执行前，失败，业务对象为null。"
                        + "serId=" + serId
                );
                return false;
            }

            EmpExecutionContext.info("执行下行业务，业务信息。"
                    + "serId=" + service.getSerId()
                    + ",userId=" + service.getUserId()
                    + ",serName=" + service.getSerName()
                    + ",commnets=" + service.getCommnets()
                    + ",subNo=" + service.getSubNo()
                    + ",spUser=" + service.getSpUser()
                    + ",runState=" + service.getRunState()
                    + ",serType=" + service.getSerType()
                    + ",ownerId=" + service.getOwnerId()
                    + ",createTime=" + service.getCreateTime()
                    + ",corpCode=" + service.getCorpCode()
            );

            //保存返回结果
            boolean result = false;
            //判断业务状态，1是启用，0是停止
            if (service.getRunState() - 1 == 0) {
                //初始化定时biz
                //TaskManagerBiz timerBiz = new TaskManagerBiz();
                // 检查任务是否未成功执行过,返回true表示未执行过
                boolean ckResult = checkIsRun("ser|" + serId);

                //如果已执行
                if (!ckResult) {
                    return false;
                }

                //执行任务并返回结果
                result = runMtServ(service);
            }
            //返回结果
            return result;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "下行业务执行前，执行异常。");
            return false;
        }

    }

    /**
     * 检查任务是否未成功执行过
     *
     * @param taskExpression 任务表达式
     * @return 返回true表示未执行过
     */
    public boolean checkIsRun(String taskExpression) {
        try {
            //当前日期
            Calendar curTime = Calendar.getInstance();
            //查询条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("taskExpression", taskExpression);
            List<LfTimer> timersList = empDao.findListBySymbolsCondition(LfTimer.class, conditionMap, null);
            Long timerTaskId = timersList.get(0).getTimerTaskId();

            conditionMap.put("timerTaskId", timerTaskId.toString());
            conditionMap.put("runYear", String.valueOf(curTime.get(Calendar.YEAR)));
            conditionMap.put("runMonth", String.valueOf(curTime.get(Calendar.MONTH) + 1));
            conditionMap.put("runDay", String.valueOf(curTime.get(Calendar.DAY_OF_MONTH)));

            //获取状态为"成功"的发送历史记录
            conditionMap.put("runResult&in", "1");

            //按条件查询
            List<LfTimerHistory> timerHissList = empDao.findListBySymbolsCondition(LfTimerHistory.class, conditionMap, null);

            //无记录，未执行过
            if (timerHissList != null && timerHissList.size() == 0) {
                //返回true
                return true;
            } else if (timerHissList == null) {
                //异常查不到记录，则返回否
                EmpExecutionContext.error("任务id:" + taskExpression + "，查询历史记录为null，该次执行取消!");
                return false;
            } else {
                //有记录，则返回否
                EmpExecutionContext.error("任务id:" + taskExpression + "，在当天已执行过，该次执行取消!");
                return false;
            }
        } catch (Exception e) {
            //异常处理
            EmpExecutionContext.error(e, "检查任务是否已成功执行过失败。");
            return false;
        }

    }

    /**
     * 运行下行业务总流程方法
     *
     * @param service 下行业务对象
     * @return 成功返回true
     */
    private boolean runMtServ(LfService service) {
        //创建新的操作日志，6为正在运行中
        Long slId = serLogBiz.addServiceLogReturnId(6, service.getSerId(), "");
        try {
            // 获取创建者
            LfSysuser sysuser = empDao.findObjectByID(LfSysuser.class, service.getUserId());
            if (sysuser == null) {
                //没获取到创建者则报异常返回否
                EmpExecutionContext.error("下行业务执行中，获取操作员对象为空。"
                        + "serId=" + service.getSerId()
                        + ",userId=" + service.getUserId()
                        + ",serName=" + service.getSerName()
                        + ",commnets=" + service.getCommnets()
                        + ",subNo=" + service.getSubNo()
                        + ",spUser=" + service.getSpUser()
                        + ",runState=" + service.getRunState()
                        + ",serType=" + service.getSerType()
                        + ",ownerId=" + service.getOwnerId()
                        + ",createTime=" + service.getCreateTime()
                        + ",corpCode=" + service.getCorpCode()
                );
                return false;
            }

            //运行前检查是否可运行
            boolean chkRes = checkBeforeRun(service, sysuser, slId);
            //检查不通过，不能运行
            if (!chkRes) {
                EmpExecutionContext.error("下行业务执行中，运行前检查，不可运行。"
                        + "serId=" + service.getSerId()
                        + ",userId=" + service.getUserId()
                        + ",serName=" + service.getSerName()
                        + ",commnets=" + service.getCommnets()
                        + ",subNo=" + service.getSubNo()
                        + ",spUser=" + service.getSpUser()
                        + ",runState=" + service.getRunState()
                        + ",serType=" + service.getSerType()
                        + ",ownerId=" + service.getOwnerId()
                        + ",createTime=" + service.getCreateTime()
                        + ",corpCode=" + service.getCorpCode()
                );
                return false;
            }

            //文件处理biz
            TxtFileUtil txtfileutil = new TxtFileUtil();
            String[] urlValuesArray = txtfileutil.createUrlAndDir(2, service.getSerId().toString());
            //相对路径
            String mobileUrl = urlValuesArray[1];

            //发送任务对象
            LfMttask mttask = new LfMttask();
            mttask.setSpUser(service.getSpUser());
            //执行查询步骤并生成发送号码文件，mttask将会包含提交总数和有效号码数
            boolean selResult = exeSelect(service, urlValuesArray, slId, mttask);
            if (!selResult) {
                EmpExecutionContext.error("下行业务执行中，执行查询步骤并生成发送号码文件失败。"
                        + "serId=" + service.getSerId()
                        + ",userId=" + service.getUserId()
                        + ",serName=" + service.getSerName()
                        + ",commnets=" + service.getCommnets()
                        + ",subNo=" + service.getSubNo()
                        + ",spUser=" + service.getSpUser()
                        + ",runState=" + service.getRunState()
                        + ",serType=" + service.getSerType()
                        + ",ownerId=" + service.getOwnerId()
                        + ",createTime=" + service.getCreateTime()
                        + ",corpCode=" + service.getCorpCode()
                );
                return false;
            }

            //发送类型  1=相同内容，2=不同内容，变量值从上面的方法中带出来
            String strBmttype = service.getCommnets();
            //拿出暂时存放的短信内容。
            String msg = service.getCpno();
            //相同内容方式发送，短信内容为空，则不发送
            if ("1".equals(strBmttype) && (msg == null || msg.length() < 1)) {
                //更新日志
                serLogBiz.updateServiceLog(slId, 13, mobileUrl);
                EmpExecutionContext.info("下行业务执行中，相同内容发送，发送内容为空。"
                        + "serId=" + service.getSerId()
                        + ",userId=" + service.getUserId()
                        + ",serName=" + service.getSerName()
                        + ",commnets=" + service.getCommnets()
                        + ",subNo=" + service.getSubNo()
                        + ",spUser=" + service.getSpUser()
                        + ",runState=" + service.getRunState()
                        + ",serType=" + service.getSerType()
                        + ",ownerId=" + service.getOwnerId()
                        + ",createTime=" + service.getCreateTime()
                        + ",corpCode=" + service.getCorpCode()
                );
                return false;
            }

            //中文•不能识别,转为英文字符
            msg = service.getCpno().replaceAll("•", "·").replace("¥", "￥");
            //相同内容则过滤关键字，不同内容的在生成文件时已做过滤
            if ("1".equals(strBmttype) && keyWordAtom.filterKeyWord(msg.toUpperCase(), service.getCorpCode()) != 0) {
                //更新日志
                serLogBiz.updateServiceLog(slId, 12, mobileUrl);
                EmpExecutionContext.info("下行业务执行中，发送内容包含关键字。"
                        + "serId=" + service.getSerId()
                        + ",userId=" + service.getUserId()
                        + ",serName=" + service.getSerName()
                        + ",commnets=" + service.getCommnets()
                        + ",subNo=" + service.getSubNo()
                        + ",spUser=" + service.getSpUser()
                        + ",runState=" + service.getRunState()
                        + ",serType=" + service.getSerType()
                        + ",ownerId=" + service.getOwnerId()
                        + ",createTime=" + service.getCreateTime()
                        + ",corpCode=" + service.getCorpCode()
                );
                return false;
            }

            SmsSendBiz smsSendBiz = new SmsSendBiz();
            String tailcontents = smsSendBiz.getTailContents(service.getBusCode(), service.getSpUser(), service.getCorpCode());
            if (tailcontents != null) {
                msg += tailcontents;
            }

            //初始化任务对象
            mttask = initMttask(mttask, mobileUrl, msg, service, Integer.valueOf(strBmttype));
            if (mttask == null) {
                serLogBiz.updateServiceLog(slId, 5, mobileUrl);
                EmpExecutionContext.error("下行业务执行中，初始化任务对象失败。"
                        + "serId=" + service.getSerId()
                        + ",userId=" + service.getUserId()
                        + ",serName=" + service.getSerName()
                        + ",commnets=" + service.getCommnets()
                        + ",subNo=" + service.getSubNo()
                        + ",spUser=" + service.getSpUser()
                        + ",runState=" + service.getRunState()
                        + ",serType=" + service.getSerType()
                        + ",ownerId=" + service.getOwnerId()
                        + ",createTime=" + service.getCreateTime()
                        + ",corpCode=" + service.getCorpCode()
                        + ",Bmttype=" + strBmttype
                        + ",msg=" + msg
                        + ",mobileUrl=" + mobileUrl
                );
                return false;
            }

            //计算预发送条数、扣费、mttask设置预发送条数
            boolean debRes = doDebits(mttask, slId);
            //处理失败
            if (!debRes) {
                EmpExecutionContext.error("下行业务执行中，计算预发送条数、扣费失败。"
                        + "serId=" + service.getSerId()
                        + ",userId=" + service.getUserId()
                        + ",serName=" + service.getSerName()
                        + ",commnets=" + service.getCommnets()
                        + ",subNo=" + service.getSubNo()
                        + ",spUser=" + service.getSpUser()
                        + ",runState=" + service.getRunState()
                        + ",serType=" + service.getSerType()
                        + ",ownerId=" + service.getOwnerId()
                        + ",createTime=" + service.getCreateTime()
                        + ",corpCode=" + service.getCorpCode()
                        + ",taskId=" + mttask.getTaskId()
                );
                return false;
            }

            boolean mtRes = empDao.save(mttask);
            if (!mtRes) {
                //有日志id则更新记录，5为其他异常
                serLogBiz.updateServiceLog(slId, 5, "");
                EmpExecutionContext.error("下行业务执行中，保存任务对象失败。"
                        + "serId=" + service.getSerId()
                        + ",userId=" + service.getUserId()
                        + ",serName=" + service.getSerName()
                        + ",commnets=" + service.getCommnets()
                        + ",subNo=" + service.getSubNo()
                        + ",spUser=" + service.getSpUser()
                        + ",runState=" + service.getRunState()
                        + ",serType=" + service.getSerType()
                        + ",ownerId=" + service.getOwnerId()
                        + ",createTime=" + service.getCreateTime()
                        + ",corpCode=" + service.getCorpCode()
                        + ",taskId=" + mttask.getTaskId()
                );
            }

            //发送短信
            boolean sendRes = sendMsg(mttask, sysuser.getUserCode(), slId);
            //提交网关失败
            if (!sendRes) {
                //运营商补费
                balanceBiz.huishouFee(Integer.valueOf(mttask.getIcount()), mttask.getSpUser(), 1);

                //0是未发送，1是已发送到网关,2发送失败,3网关处理完成,  4发送中,5超时未发送
                mttask.setSendstate(2);
                EmpExecutionContext.error("下行业务执行中，提交网关失败。"
                        + "serId=" + service.getSerId()
                        + ",userId=" + service.getUserId()
                        + ",serName=" + service.getSerName()
                        + ",commnets=" + service.getCommnets()
                        + ",subNo=" + service.getSubNo()
                        + ",spUser=" + service.getSpUser()
                        + ",runState=" + service.getRunState()
                        + ",serType=" + service.getSerType()
                        + ",ownerId=" + service.getOwnerId()
                        + ",createTime=" + service.getCreateTime()
                        + ",corpCode=" + service.getCorpCode()
                        + ",taskId=" + mttask.getTaskId()
                );
            } else {
                //0是未发送，1是已发送到网关,2发送失败,3网关处理完成,  4发送中,5超时未发送
                mttask.setSendstate(1);
                EmpExecutionContext.info("下行业务执行中，提交网关成功。"
                        + "serId=" + service.getSerId()
                        + ",userId=" + service.getUserId()
                        + ",serName=" + service.getSerName()
                        + ",commnets=" + service.getCommnets()
                        + ",subNo=" + service.getSubNo()
                        + ",spUser=" + service.getSpUser()
                        + ",runState=" + service.getRunState()
                        + ",serType=" + service.getSerType()
                        + ",ownerId=" + service.getOwnerId()
                        + ",createTime=" + service.getCreateTime()
                        + ",corpCode=" + service.getCorpCode()
                        + ",taskId=" + mttask.getTaskId()
                );
            }

            //发送后更新任务状态
            updateMttaskByTaskid(mttask);

            //返回结果
            return sendRes;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "下行业务执行中，异常。"
                    + "serId=" + service.getSerId()
                    + ",userId=" + service.getUserId()
                    + ",serName=" + service.getSerName()
                    + ",commnets=" + service.getCommnets()
                    + ",subNo=" + service.getSubNo()
                    + ",spUser=" + service.getSpUser()
                    + ",runState=" + service.getRunState()
                    + ",serType=" + service.getSerType()
                    + ",ownerId=" + service.getOwnerId()
                    + ",createTime=" + service.getCreateTime()
                    + ",corpCode=" + service.getCorpCode()
            );
            //有日志id则更新记录，5为其他异常
            serLogBiz.updateServiceLog(slId, 5, "");

            return false;
        }
    }

    /**
     * @param mt 任务对象
     * @return true-修改成功,false-修改失败
     * @throws Exception
     * @description 发送完成后更新任务表
     */
    private boolean updateMttaskByTaskid(LfMttask mttask) {
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();

            conditionMap.put("taskId", mttask.getTaskId().toString());
            objectMap.put("sendstate", mttask.getSendstate().toString());
            objectMap.put("errorCodes", mttask.getErrorCodes());

            return empDao.update(LfMttask.class, objectMap, conditionMap);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "下行业务执行中，下发后更新异常。taskid:" + mttask.getTaskId());
            return false;
        }
    }

    /**
     * 运行任务前检查是否可运行
     *
     * @param service 下行任务对象
     * @param sysuser 操作员
     * @param slId    日志id
     * @return 可运行则返回true
     */
    private boolean checkBeforeRun(LfService service, LfSysuser sysuser, Long slId) {
        try {
            //企业状态. 0禁用; 1启用
            int corpState = fetchDao.getCorpState(service.getCorpCode());
            if (corpState < 1) {
                EmpExecutionContext.info("下行业务，运行前检查，企业状态为禁用"
                        + "。corpState=" + corpState
                        + ",serId=" + service.getSerId()
                        + ",corpCode=" + service.getCorpCode());
                return false;
            }

            if (sysuser == null) {
                //没获取到创建者则报异常返回否
                EmpExecutionContext.error("下行业务，运行前检查，操作员对象不存在"
                        + "。serId=" + service.getSerId()
                        + ",corpCode=" + service.getCorpCode()
                        + ",userId=" + service.getUserId());
                return false;
            } else if (sysuser.getUserState() != 1) {
                //TODO 日志状态应用方式
                // 禁用的状态
                EmpExecutionContext.info("下行业务，运行前检查，操作员状态为禁用/注销，不允许执行"
                        + "。serId=" + service.getSerId()
                        + ",corpCode=" + service.getCorpCode()
                        + ",userId=" + service.getUserId());
                //建一个新的操作记录，8为操作员状态为禁用/注销，不允许执行
                serLogBiz.addNewServiceLog(8, service.getSerId(), "");
                return false;
            }

            CheckUtil checkUtil = new CheckUtil();
            //验证操作员企业发送账户是否一致性
            boolean checkSendResult = checkUtil.checkSysuserInCorp(sysuser, service.getCorpCode(), service.getSpUser(), null);
            if (!checkSendResult) {
                EmpExecutionContext.error("下行业务，运行前检查，操作员企业发送账户是否一致性不通过，当次执行取消。"
                        + "下行业务id：" + service.getSerId()
                        + "，下行业务配置的发送账户：" + service.getSpUser()
                        + "，下行业务所属企业：" + service.getCorpCode()
                        + "，操作员的所属企业：" + sysuser.getCorpCode());
                return false;
            }
            return true;
        } catch (Exception e) {
            //有日志id则更新记录，5为其他异常
            serLogBiz.updateServiceLog(slId, 5, "");

            EmpExecutionContext.error(e, "下行业务，运行前检查，异常"
                    + "。serId=" + service.getSerId()
                    + ",corpCode=" + service.getCorpCode()
                    + ",userId=" + service.getUserId());
            return false;
        }
    }

    /**
     * 计算预发送条数、扣费
     *
     * @param mttask 发送任务对象
     * @param slId   日志id，用于更新日志
     * @return 成功返回true
     */
    private boolean doDebits(LfMttask mttask, Long slId) {
        try {
            SmsBiz smsBiz = new SmsBiz();
            //统计预发送条数
            Integer icount = smsBiz.countAllOprSmsNumber(mttask.getSpUser(), mttask.getMsg(), mttask.getBmtType(), mttask.getMobileUrl(), null);
            if (icount == null || icount == 0) {
                //更新日志，5为其他异常
                serLogBiz.updateServiceLog(slId, 5, mttask.getMobileUrl());
                EmpExecutionContext.error("下行业务执行中，统计预发送条数为null或0。"
                        + "serId=" + mttask.getTempid()
                        + ",userId=" + mttask.getUserId()
                        + ",spUser=" + mttask.getSpUser()
                        + ",corpCode=" + mttask.getCorpCode()
                        + ",Bmttype=" + mttask.getBmtType()
                        + ",msg=" + mttask.getMsg()
                        + ",taskId=" + mttask.getTaskId()
                        + ",mobileUrl=" + mttask.getMobileUrl()
                );
                return false;
            }

            // 预发送条数
            mttask.setIcount(String.valueOf(icount));

            //上传文件到文件服务器
            if (StaticValue.getISCLUSTER() == 1) {
                CommonBiz commBiz = new CommonBiz();
                //上传发送文件到文件服务器，并使用文件服务器地址
                mttask.setFileuri(commBiz.uploadFileToFileServer(mttask.getMobileUrl()));
                EmpExecutionContext.info("下行业务执行中，发送文件上传到文件服务器，上传后返回路径：" + mttask.getFileuri());
            } else {
                //使用本节点地址
                mttask.setFileuri(StaticValue.BASEURL);
                EmpExecutionContext.info("下行业务执行中，发送文件使用本地路径：" + mttask.getFileuri());
            }

            //SP账号余额检查
            int spResult = balanceBiz.checkSpUserFee(mttask.getSpUser(), icount, 1);
            //SP账号余额不足，或者其他错误情况，则流程不能继续
            if (spResult < 0) {
                EmpExecutionContext.error("下行业务执行中，SP账号余额不足。"
                        + "serId=" + mttask.getTempid()
                        + ",userId=" + mttask.getUserId()
                        + ",spUser=" + mttask.getSpUser()
                        + ",corpCode=" + mttask.getCorpCode()
                        + ",Bmttype=" + mttask.getBmtType()
                        + ",msg=" + mttask.getMsg()
                        + ",taskId=" + mttask.getTaskId()
                        + ",mobileUrl=" + mttask.getMobileUrl()
                        + ",icount=" + icount
                        + ",SP账号余额检查返回=" + spResult
                );
                //14为发送账号余额不足
                serLogBiz.updateServiceLog(slId, 14, mttask.getMobileUrl());
                return false;
            }

            //扣费并接收返回状态
            String wgresult = balanceBiz.checkGwFee(mttask.getSpUser(), icount, mttask.getCorpCode(), true, 1);
            if ("nogwfee".equals(wgresult) || "feefail".equals(wgresult) || "lessgwfee".equals(wgresult.substring(0, 9))) {
                EmpExecutionContext.error("下行业务执行中，运营商余额不足/获取运营商余额失败。"
                        + "serId=" + mttask.getTempid()
                        + ",userId=" + mttask.getUserId()
                        + ",spUser=" + mttask.getSpUser()
                        + ",corpCode=" + mttask.getCorpCode()
                        + ",Bmttype=" + mttask.getBmtType()
                        + ",msg=" + mttask.getMsg()
                        + ",mobileUrl=" + mttask.getMobileUrl()
                        + ",icount=" + icount
                        + ",taskId=" + mttask.getTaskId()
                        + ",wgresult=" + wgresult
                );
                //2为运营商余额不足/获取运营商余额失败
                serLogBiz.updateServiceLog(slId, 2, mttask.getMobileUrl());
                return false;
            }

            return true;
        } catch (Exception e) {
            //更新日志，5为其他异常
            serLogBiz.updateServiceLog(slId, 5, mttask.getMobileUrl());
            EmpExecutionContext.error(e, "下行业务执行中，扣费异常。"
                    + "serId=" + mttask.getTempid()
                    + ",userId=" + mttask.getUserId()
                    + ",spUser=" + mttask.getSpUser()
                    + ",corpCode=" + mttask.getCorpCode()
                    + ",Bmttype=" + mttask.getBmtType()
                    + ",msg=" + mttask.getMsg()
                    + ",taskId=" + mttask.getTaskId()
                    + ",mobileUrl=" + mttask.getMobileUrl()
            );
            return false;
        }
    }

    /**
     * 初始化任务对象
     *
     * @param icount    预发送条数
     * @param mobileUrl 发送号码文件相对路径
     * @param msg       发送内容
     * @param service   下行业务对象
     * @param bmttype   发送类型  1=相同内容，2=不同内容
     * @return 成功则返回任务对象实例
     */
    private LfMttask initMttask(LfMttask mttask, String mobileUrl, String msg, LfService service, Integer bmttype) {
        try {

            mttask.setMobileUrl(mobileUrl);
            // 任务主题
            mttask.setTitle(service.getSerName());
            // sp账号
            mttask.setSpUser(service.getSpUser());
            // 提交类型
            mttask.setBmtType(bmttype);
            // 定时时间
            mttask.setTimerStatus(0);
            // 根据发送类型判断 短信类型
            mttask.setMsgType(bmttype);
            //信息类型 1-短信， 2-彩信，5-移动财务 6-网讯 7-智能引擎下行业务
            mttask.setMsType(7);
            // 提交类型
            mttask.setSubState(2);
            // 业务类型
            mttask.setBusCode(service.getBusCode());

            mttask.setMsg(msg);
            // 发送状态
            mttask.setSendstate(0);
            // 企业编码
            mttask.setCorpCode(service.getCorpCode());
            // 发送优先级
            mttask.setSendLevel(1);
            // 是否需要回复
            mttask.setIsReply(0);
            // 尾号
            mttask.setSubNo(service.getSubNo());
            // 操作员id
            mttask.setUserId(service.getUserId());
            //全局变量biz
            GlobalVariableBiz globalBiz = GlobalVariableBiz.getInstance();
            //获取任务id
            Long taskId = globalBiz.getValueByKey("taskId", 1L);
            mttask.setTaskId(taskId);

            mttask.setTimerTime(mttask.getSubmitTime());
            mttask.setTempid(service.getSerId());

            //识别模式。空和1-使用尾号；
            if (service.getIdentifyMode() == 1) {
                // 获取上行尾号，此方法的规则是，如果能找到这个模块绑定的子号就用，找不到就重新生成一个尾号返回
                String subno = GlobalVariableBiz.getInstance().getValidSubno(
                        StaticValue.MOBUSCODE, 0, service.getCorpCode(),
                        new ErrorCodeParam());
                mttask.setSubNo(subno);
            }

            return mttask;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "下行业务执行中，初始化任务对象异常。"
                    + "serId=" + service.getSerId()
                    + ",userId=" + service.getUserId()
                    + ",serName=" + service.getSerName()
                    + ",commnets=" + service.getCommnets()
                    + ",subNo=" + service.getSubNo()
                    + ",spUser=" + service.getSpUser()
                    + ",runState=" + service.getRunState()
                    + ",serType=" + service.getSerType()
                    + ",ownerId=" + service.getOwnerId()
                    + ",createTime=" + service.getCreateTime()
                    + ",corpCode=" + service.getCorpCode()
                    + ",mobileUrl=" + mobileUrl
                    + ",msg=" + msg
                    + ",bmttype=" + bmttype
                    + ",taskId=" + mttask.getTaskId()
            );
            return null;
        }
    }

    private boolean exeSelect(LfService service, String[] urlValuesArray, Long slId, LfMttask mttask) {
        //reply回复步骤对象
        LfReply reply = null;
        //select步骤对象
        LfProcess select = null;

        //发送文件地址
        String newUrl;
        String mobileUrl;
        //发送类型  1=相同内容，2=不同内容
        String strBmttype = null;
        try {
            //步骤处理biz
            ProcessConfigBiz processBiz = new ProcessConfigBiz();
            // 获取智能引擎步骤对象集合
            List<LfProcess> processList = processBiz.getProcescBySerId(service.getSerId());
            //没步骤
            if (processList == null || processList.size() == 0) {
                EmpExecutionContext.error("下行业务执行中，获取不到业务步骤。"
                        + "serId=" + service.getSerId()
                        + ",userId=" + service.getUserId()
                        + ",serName=" + service.getSerName()
                        + ",commnets=" + service.getCommnets()
                        + ",subNo=" + service.getSubNo()
                        + ",spUser=" + service.getSpUser()
                        + ",runState=" + service.getRunState()
                        + ",serType=" + service.getSerType()
                        + ",ownerId=" + service.getOwnerId()
                        + ",createTime=" + service.getCreateTime()
                        + ",corpCode=" + service.getCorpCode()
                );
                //更新日志，4为服务配置不正确
                serLogBiz.updateServiceLog(slId, 4, "");
                return false;
            }

            //有处理步骤则循环处理
            for (int h = 0; h < processList.size(); h++) {
                //reply和select步骤都找到了则退出循环
                if (reply != null && select != null) {
                    break;
                }
                //找到select步骤
                if (processList.get(h).getPrType() == 4) {
                    select = processList.get(h);
                    continue;
                }
                //找到reply步骤
                if (processList.get(h).getPrType() == 5) {
                    //获取回复步骤
                    reply = processBiz.getReplyByPrId(processList.get(h).getPrId());
                    continue;
                }
            }

            if (select == null || reply == null || reply.getPrId() == null || StringUtils.isEmpty(reply.getMsgMain())) {
                EmpExecutionContext.error("下行业务执行中，业务配置不正确。"
                        + "serId=" + service.getSerId()
                        + ",userId=" + service.getUserId()
                        + ",serName=" + service.getSerName()
                        + ",commnets=" + service.getCommnets()
                        + ",subNo=" + service.getSubNo()
                        + ",spUser=" + service.getSpUser()
                        + ",runState=" + service.getRunState()
                        + ",serType=" + service.getSerType()
                        + ",ownerId=" + service.getOwnerId()
                        + ",createTime=" + service.getCreateTime()
                        + ",corpCode=" + service.getCorpCode()
                );
                //无步骤则记录日志并返回，4为服务配置不正确
                serLogBiz.updateServiceLog(slId, 4, "");
                return false;
            }

            //短信文件地址
            //绝对路径
            newUrl = urlValuesArray[0];
            //相对路径
            mobileUrl = urlValuesArray[1];

            //使用replay的msgHeader作为文件相对路径的传输
            reply.setMsgHeader(mobileUrl);

            //发送类型  1=相同内容，2=不同内容
            strBmttype = isDynameicParam(reply.getMsgMain());
            if (strBmttype == null) {
                EmpExecutionContext.error("下行业务执行中，获取发送类型为空。"
                        + "serId=" + service.getSerId()
                        + ",userId=" + service.getUserId()
                        + ",serName=" + service.getSerName()
                        + ",commnets=" + service.getCommnets()
                        + ",subNo=" + service.getSubNo()
                        + ",spUser=" + service.getSpUser()
                        + ",runState=" + service.getRunState()
                        + ",serType=" + service.getSerType()
                        + ",ownerId=" + service.getOwnerId()
                        + ",createTime=" + service.getCreateTime()
                        + ",corpCode=" + service.getCorpCode()
                );
                //更新日志,5为其他异常
                serLogBiz.updateServiceLog(slId, 5, "");
                return false;
            }
            //暂时放这里，为了传到外面使用
            service.setCommnets(strBmttype);
            //把短信模板内容暂放这里，，为了传到外面使用
            service.setCpno(reply.getMsgMain());

            //执行select步骤
            boolean bathQueryResult = queryProcessSql(newUrl,
                    select, reply, service.getCorpCode(), service.getBusCode(), strBmttype, mttask);

            if (!bathQueryResult) {
                EmpExecutionContext.error("下行业务执行中，执行select步骤失败。"
                        + "serId=" + service.getSerId()
                        + ",userId=" + service.getUserId()
                        + ",serName=" + service.getSerName()
                        + ",commnets=" + service.getCommnets()
                        + ",subNo=" + service.getSubNo()
                        + ",spUser=" + service.getSpUser()
                        + ",runState=" + service.getRunState()
                        + ",serType=" + service.getSerType()
                        + ",ownerId=" + service.getOwnerId()
                        + ",createTime=" + service.getCreateTime()
                        + ",corpCode=" + service.getCorpCode()
                );
                //更新日志,3为数据库操作异常
                serLogBiz.updateServiceLog(slId, 3, "");
                return false;
            }

            //判断生成文件是否有内容
            if (!isMobileFileNotNull(newUrl)) {
                EmpExecutionContext.error("下行业务执行中，抓取的内容为空。"
                        + "serId=" + service.getSerId()
                        + ",userId=" + service.getUserId()
                        + ",serName=" + service.getSerName()
                        + ",commnets=" + service.getCommnets()
                        + ",subNo=" + service.getSubNo()
                        + ",spUser=" + service.getSpUser()
                        + ",runState=" + service.getRunState()
                        + ",serType=" + service.getSerType()
                        + ",ownerId=" + service.getOwnerId()
                        + ",createTime=" + service.getCreateTime()
                        + ",corpCode=" + service.getCorpCode()
                );
                //更新日志，7为抓取的内容为空
                serLogBiz.updateServiceLog(slId, 7, "");
                return false;
            }
            return true;

        } catch (Exception e) {
            EmpExecutionContext.error(e, "下行业务执行中，执行select步骤异常。"
                    + "serId=" + service.getSerId()
                    + ",userId=" + service.getUserId()
                    + ",serName=" + service.getSerName()
                    + ",commnets=" + service.getCommnets()
                    + ",subNo=" + service.getSubNo()
                    + ",spUser=" + service.getSpUser()
                    + ",runState=" + service.getRunState()
                    + ",serType=" + service.getSerType()
                    + ",ownerId=" + service.getOwnerId()
                    + ",createTime=" + service.getCreateTime()
                    + ",corpCode=" + service.getCorpCode()
            );
            //有日志id则更新记录，5为其他异常
            serLogBiz.updateServiceLog(slId, 5, "");
            return false;
        }
    }

    private boolean sendMsg(LfMttask mttask, String userCode, Long slId) {

        //提交网关结果状态码
        String resultReceive = null;
        //返回结果
        boolean returnRes = false;
        try {
            EmpExecutionContext.info("下行业务执行中，发送信息。"
                    + "serId=" + mttask.getTempid()
                    + ",userId=" + mttask.getUserId()
                    + ",spUser=" + mttask.getSpUser()
                    + ",corpCode=" + mttask.getCorpCode()
                    + ",Bmttype=" + mttask.getBmtType()
                    + ",msg=" + mttask.getMsg()
                    + ",mobileUrl=" + mttask.getMobileUrl()
                    + ",userCode=" + userCode
                    + ",TaskId=" + mttask.getTaskId()
                    + ",Title=" + mttask.getTitle()
                    + ",SendLevel=" + mttask.getSendLevel()
                    + ",BusCode=" + mttask.getBusCode()
                    + ",SubNo=" + mttask.getSubNo()
                    + ",Icount=" + mttask.getIcount()
                    + ",SubCount=" + mttask.getSubCount()
                    + ",EffCount=" + mttask.getEffCount()
            );
            WGParams wg = new WGParams();
            // 发送账号
            wg.setSpid(mttask.getSpUser());
            // sp密码
            wg.setSppassword(new CommonBiz().getSpPwdBySpUserId(mttask
                    .getSpUser()));
            // 发送类型  1=相同内容，2=不同内容
            wg.setBmttype(mttask.getBmtType().toString());
            wg.setTaskid(mttask.getTaskId().toString());
            wg.setTitle(mttask.getTitle());
            wg.setPriority(mttask.getSendLevel().toString());
            // 发送文件url
            wg.setUrl(mttask.getFileuri() + mttask.getMobileUrl());
            // 用户编码
            wg.setParam1(userCode);
            // 业务类型
            wg.setSvrtype(mttask.getBusCode());
            // 模块编码
            wg.setModuleid(StaticValue.MTBUSCODE);
            wg.setRptflag("0");
            wg.setContent(mttask.getMsg());
            //有尾号则带上尾号
            if (mttask.getSubNo() != null && mttask.getSubNo().trim().length() > 0) {
                wg.setSa(mttask.getSubNo());
            }

            //短信发送biz
            HttpSmsSend smsSend = new HttpSmsSend();
            String returnResult = smsSend.createbatchMtRequest(wg);
            //如果没有返回结果
            if (returnResult == null) {
                EmpExecutionContext.error("下行业务执行中，发送失败，返回结果为null。"
                        + "serId=" + mttask.getTempid()
                        + ",userId=" + mttask.getUserId()
                        + ",spUser=" + mttask.getSpUser()
                        + ",corpCode=" + mttask.getCorpCode()
                        + ",Bmttype=" + mttask.getBmtType()
                        + ",msg=" + mttask.getMsg()
                        + ",mobileUrl=" + mttask.getMobileUrl()
                        + ",userCode=" + userCode
                        + ",TaskId=" + mttask.getTaskId()
                        + ",Title=" + mttask.getTitle()
                        + ",SendLevel=" + mttask.getSendLevel()
                        + ",BusCode=" + mttask.getBusCode()
                        + ",SubNo=" + mttask.getSubNo()
                        + ",Icount=" + mttask.getIcount()
                        + ",SubCount=" + mttask.getSubCount()
                        + ",EffCount=" + mttask.getEffCount()
                );
                //记录日志，并返回，1为向网关发送请求失败
                //serLogBiz.updateServiceLog(slId, 1, mttask.getMobileUrl());
                return false;
            }

            EmpExecutionContext.info("下行业务执行中，提交发送返回信息。"
                    + "网关返回：" + returnResult
                    + ",serId=" + mttask.getTempid()
                    + ",userId=" + mttask.getUserId()
                    + ",spUser=" + mttask.getSpUser()
                    + ",corpCode=" + mttask.getCorpCode()
                    + ",Bmttype=" + mttask.getBmtType()
                    + ",msg=" + mttask.getMsg()
                    + ",mobileUrl=" + mttask.getMobileUrl()
                    + ",userCode=" + userCode
                    + ",TaskId=" + mttask.getTaskId()
                    + ",Title=" + mttask.getTitle()
                    + ",SendLevel=" + mttask.getSendLevel()
                    + ",BusCode=" + mttask.getBusCode()
                    + ",SubNo=" + mttask.getSubNo()
                    + ",Icount=" + mttask.getIcount()
                    + ",SubCount=" + mttask.getSubCount()
                    + ",EffCount=" + mttask.getEffCount()
            );

            //获取网关返回错误代码信息
            int index = returnResult.indexOf("mterrcode");
            resultReceive = returnResult.substring(index + 10, index + 13);
            //设置网关返回代码
            mttask.setErrorCodes(resultReceive);
        } catch (Exception e) {
            //记录日志，1为向网关发送请求失败
            serLogBiz.updateServiceLog(slId, 1, mttask.getMobileUrl());
            EmpExecutionContext.error(e, "下行业务执行中，发送异常。"
                    + "serId=" + mttask.getTempid()
                    + ",userId=" + mttask.getUserId()
                    + ",spUser=" + mttask.getSpUser()
                    + ",corpCode=" + mttask.getCorpCode()
                    + ",Bmttype=" + mttask.getBmtType()
                    + ",msg=" + mttask.getMsg()
                    + ",mobileUrl=" + mttask.getMobileUrl()
                    + ",userCode=" + userCode
                    + ",TaskId=" + mttask.getTaskId()
                    + ",Title=" + mttask.getTitle()
                    + ",SendLevel=" + mttask.getSendLevel()
                    + ",BusCode=" + mttask.getBusCode()
                    + ",SubNo=" + mttask.getSubNo()
                    + ",Icount=" + mttask.getIcount()
                    + ",SubCount=" + mttask.getSubCount()
                    + ",EffCount=" + mttask.getEffCount()
            );
            //return false;
        } finally {
            //网关发送成功
            if (resultReceive != null && "000".equals(resultReceive)) {
                EmpExecutionContext.info("下行业务执行中，发送成功。"
                        + "serId=" + mttask.getTempid()
                        + ",userId=" + mttask.getUserId()
                        + ",spUser=" + mttask.getSpUser()
                        + ",corpCode=" + mttask.getCorpCode()
                        + ",Bmttype=" + mttask.getBmtType()
                        + ",msg=" + mttask.getMsg()
                        + ",mobileUrl=" + mttask.getMobileUrl()
                        + ",userCode=" + userCode
                        + ",TaskId=" + mttask.getTaskId()
                        + ",Title=" + mttask.getTitle()
                        + ",SendLevel=" + mttask.getSendLevel()
                        + ",BusCode=" + mttask.getBusCode()
                        + ",SubNo=" + mttask.getSubNo()
                        + ",Icount=" + mttask.getIcount()
                        + ",SubCount=" + mttask.getSubCount()
                        + ",EffCount=" + mttask.getEffCount()
                );
                //记录成功日志，0为成功向网关发送请求
                serLogBiz.updateServiceLog(slId, 0, mttask.getMobileUrl());
                returnRes = true;
            } else {
                EmpExecutionContext.error("下行业务执行中，发送失败。"
                        + "网关错误码：" + resultReceive
                        + ",serId=" + mttask.getTempid()
                        + ",userId=" + mttask.getUserId()
                        + ",spUser=" + mttask.getSpUser()
                        + ",corpCode=" + mttask.getCorpCode()
                        + ",Bmttype=" + mttask.getBmtType()
                        + ",msg=" + mttask.getMsg()
                        + ",mobileUrl=" + mttask.getMobileUrl()
                        + ",userCode=" + userCode
                        + ",TaskId=" + mttask.getTaskId()
                        + ",Title=" + mttask.getTitle()
                        + ",SendLevel=" + mttask.getSendLevel()
                        + ",BusCode=" + mttask.getBusCode()
                        + ",SubNo=" + mttask.getSubNo()
                        + ",Icount=" + mttask.getIcount()
                        + ",SubCount=" + mttask.getSubCount()
                        + ",EffCount=" + mttask.getEffCount()
                );
                //记录成功日志，1为向网关发送请求失败
                serLogBiz.updateServiceLog(slId, 1, mttask.getMobileUrl());
                returnRes = false;
            }
        }
        return returnRes;
    }

    /**
     * 执行select步骤，抓取结果
     *
     * @param url
     * @param process
     * @param reply
     * @return
     * @throws Exception
     */
    private boolean queryProcessSql(String url, LfProcess process,
                                    LfReply reply, String corpCode, String busCode, String strBmttype, LfMttask mttask) {
        JDBCUtil jdbcUtil = new JDBCUtil();
        Connection conn = null;
        FileOutputStream fos = null;
        ByteArrayInputStream baInput = null;
        OutputStreamWriter write = null;
        BufferedWriter writer = null;
        PreparedStatement pState = null;
        ResultSet resultSet = null;

        try {
            EmpExecutionContext.info("下行业务执行中，执行select步骤，步骤信息。"
                    + "prId=" + process.getPrId()
                    + ",dbId=" + process.getDbId()
                    + ",serId=" + process.getSerId()
                    + ",prName=" + process.getPrName()
                    + ",prType=" + process.getPrType()
                    + ",finalState=" + process.getFinalState()
                    + ",sql=" + process.getSql()
                    + ",nextPrId=" + process.getNextPrId()
                    + ",prNo=" + process.getPrNo()
                    + ",prePrId=" + process.getPrePrId()
                    + ",corpCode=" + corpCode
                    + ",busCode=" + busCode
                    + ",Bmttype=" + strBmttype
                    + ",replyMsg=" + reply.getMsgMain()
                    + ",url=" + url
            );

            //获取数据库连接对象
            LfDBConnect dbConnect = empDao.findObjectByID(LfDBConnect.class, process.getDbId());
            if (dbConnect == null) {
                EmpExecutionContext.error("下行业务执行中，执行select步骤，获取数据库连接对象为空。"
                        + "prId=" + process.getPrId()
                        + ",dbId=" + process.getDbId()
                        + ",serId=" + process.getSerId()
                        + ",prName=" + process.getPrName()
                        + ",prType=" + process.getPrType()
                        + ",finalState=" + process.getFinalState()
                        + ",sql=" + process.getSql()
                        + ",nextPrId=" + process.getNextPrId()
                        + ",prNo=" + process.getPrNo()
                        + ",prePrId=" + process.getPrePrId()
                        + ",corpCode=" + corpCode
                );
                return false;
            }

            EmpExecutionContext.info("下行业务执行中，执行select步骤，步骤数据库信息。"
                    + "prId=" + process.getPrId()
                    + ",dbId=" + process.getDbId()
                    + ",serId=" + process.getSerId()
                    + ",prName=" + process.getPrName()
                    + ",prType=" + process.getPrType()
                    + ",sql=" + process.getSql()
                    + ",DbType=" + dbConnect.getDbType()
                    + ",ConStr=" + dbConnect.getConStr()
                    + ",DbUser=" + dbConnect.getDbUser()
            );

            //获取数据库驱动
            String driver = jdbcUtil.getClassNameByDBType(dbConnect.getDbType());

            Class.forName(driver);
            String dbUrl=dbConnect.getConStr();
            if(driver.indexOf("mysql")>-1){
                dbUrl+="?useUnicode=true&characterEncoding=UTF-8";
            }
            //创建数据库连接
            conn = DriverManager.getConnection(dbUrl,
                    dbConnect.getDbUser(), dbConnect.getDbPwd());
            //取出要执行的sql语句
            String sql = process.getSql();
            EmpExecutionContext.sql("execute sql : " + sql);
            //执行sql语句
            pState = conn.prepareStatement(sql);
            pState.setFetchSize(256);
            resultSet = pState.executeQuery();
            resultSet.setFetchSize(256);
            String newline = System.getProperties().getProperty("line.separator");
            File file;
            int buffer = 20480;

            file = new File(url);
            if (!file.exists() != false) {
                boolean state = file.createNewFile();
                if (!state) {
                    EmpExecutionContext.error("创建文件失败");
                }
            }

            fos = new FileOutputStream(url, true);
            write = new OutputStreamWriter(fos, "GBK");
            writer = new BufferedWriter(write, buffer);
            String content = null;
            StringBuilder sbBatchContent = new StringBuilder();
            String phone = null;
            String param = null;
            int ibatchRow = 0;
            WgMsgConfigBiz wgMsgBiz = new WgMsgConfigBiz();
            //手机号段数组
            String[] haoduan = wgMsgBiz.getHaoduan();
            // REPLY发送内容消息
            String strReplyMsg = reply.getMsgMain();

            HttpSmsSend jobBiz = new HttpSmsSend();
            BlackListAtom blackBiz = new BlackListAtom();
            KeyWordAtom keyWordAtom = new KeyWordAtom();
            PhoneUtil phoneUtil = new PhoneUtil();
            SmsSendBiz smsSendBiz = new SmsSendBiz();

            String tailcontents = smsSendBiz.getTailContents(busCode, mttask.getSpUser(), corpCode);
            if (tailcontents == null) {
                tailcontents = "";
            }

            //提交总数
            Long subCount = 0l;
            //有效号码数
            Long effCount = 0l;

            //有记录则循环读取并处理
            while (resultSet.next()) {
                //获取数据库记录的手机号码
                phone = resultSet.getString("phone");
                //1. 校验手机号码有效性
                if (StringUtils.isEmpty(phone) || "".equals(phone.trim())) {
                    continue;
                }
                //提交总数加1
                subCount++;
                //2.验证号码的号段是否合法,以及过滤黑名单号码
                if (phoneUtil.getPhoneType(phone, haoduan) == -1
                        || blackBiz.checkBlackList(corpCode, phone, busCode)) {
                    continue;
                }

                //获取发送内容
                param = resultSet.getString("content");

                if (null == param) {
                    param = "";
                }

                // 校验发送内容如果包含动态参数即组装内容 ,否则就认定为相同内容发送
                if ("2".equals(strBmttype)) {
                    //拼装带占位符的内容
                    content = jobBiz.combineContent(param, strReplyMsg);
                    content = content.replaceAll("•", "·").replace("¥", "￥");
                    //过滤关键字
                    if (StringUtils.isEmpty(content)
                            || keyWordAtom.filterKeyWord(content.toUpperCase(), corpCode) != 0) {

                        continue;
                    }
                    //拼装手机号、短信内容为新一行
                    sbBatchContent.append(phone).append(",").append(content).append(tailcontents).append(newline);
                } else {
                    //拼装手机号为新一行
                    sbBatchContent.append(phone).append(newline);
                }

                //有效号码加1
                effCount++;

                ++ibatchRow;
                /*
                 * 批量写文件, 每1000行执行一次写入
                 */
                if (ibatchRow != 0 && (ibatchRow % 1000) == 0) {
                    writer.write(sbBatchContent.toString());
                    sbBatchContent.setLength(0);
                    ibatchRow = 0;
                }
            }

            //判断如果还存在批发送内容未保存到文件，则写入文件
            if (sbBatchContent.length() > 0) {
                writer.write(sbBatchContent.toString());
                sbBatchContent.setLength(0);
            }

            write.flush();
            writer.flush();

            mttask.setSubCount(subCount);
            mttask.setEffCount(effCount);

            EmpExecutionContext.info("下行业务执行中，执行select步骤，号码数量信息。"
                    + "subCount=" + subCount
                    + ",effCount=" + effCount
                    + ",prId=" + process.getPrId()
                    + ",dbId=" + process.getDbId()
                    + ",serId=" + process.getSerId()
                    + ",prName=" + process.getPrName()
                    + ",prType=" + process.getPrType()
                    + ",finalState=" + process.getFinalState()
                    + ",sql=" + process.getSql()
                    + ",nextPrId=" + process.getNextPrId()
                    + ",prNo=" + process.getPrNo()
                    + ",prePrId=" + process.getPrePrId()
                    + ",corpCode=" + corpCode
                    + ",busCode=" + busCode
                    + ",Bmttype=" + strBmttype
                    + ",replyMsg=" + reply.getMsgMain()
                    + ",url=" + url
            );
            return true;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "下行业务执行中，执行select步骤，异常。"
                    + "prId=" + process.getPrId()
                    + ",dbId=" + process.getDbId()
                    + ",serId=" + process.getSerId()
                    + ",prName=" + process.getPrName()
                    + ",prType=" + process.getPrType()
                    + ",finalState=" + process.getFinalState()
                    + ",sql=" + process.getSql()
                    + ",nextPrId=" + process.getNextPrId()
                    + ",prNo=" + process.getPrNo()
                    + ",prePrId=" + process.getPrePrId()
                    + ",corpCode=" + corpCode
                    + ",busCode=" + busCode
                    + ",Bmttype=" + strBmttype
                    + ",replyMsg=" + reply.getMsgMain()
                    + ",url=" + url
            );
            return false;
        } finally {
            try {
                //关闭数据库操作对象
                jdbcUtil.closeAll(conn, resultSet, pState);

                //关闭文件操作对象
                if (writer != null) {
                    writer.close();
                }
                if (write != null) {
                    write.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (baInput != null) {
                    baInput.close();
                }

            } catch (IOException e) {
                EmpExecutionContext.error(e, "下行业务执行中，执行select步骤，关闭资源异常。"
                        + "prId=" + process.getPrId()
                        + ",dbId=" + process.getDbId()
                        + ",serId=" + process.getSerId()
                        + ",prName=" + process.getPrName()
                        + ",prType=" + process.getPrType()
                        + ",finalState=" + process.getFinalState()
                        + ",sql=" + process.getSql()
                        + ",nextPrId=" + process.getNextPrId()
                        + ",prNo=" + process.getPrNo()
                        + ",prePrId=" + process.getPrePrId()
                        + ",corpCode=" + corpCode
                        + ",busCode=" + busCode
                        + ",Bmttype=" + strBmttype
                        + ",replyMsg=" + reply.getMsgMain()
                        + ",url=" + url
                );
            }
        }
    }

    /**
     * @param url
     * @return
     * @throws Exception
     */
    private boolean isMobileFileNotNull(String url) throws Exception {
        //保存返回结果
        boolean result = false;
        BufferedReader reader = null;
        try {

            //读取文件
            File dist_File = new File(url);
            if (dist_File.exists() == false) {
                return true;
            }
            //设置文件编码
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(url), "GBK"));
            String tmp = null;
            //读取文件行数并做处理
            while ((tmp = reader.readLine()) != null && !"".equals(tmp.trim())) {
                result = true;
                break;
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "isMobileFileNotNull");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        //关闭文件流

        //返回结果
        return result;
    }

    /**
     * @param prId
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> findProcessResult(Long prId) {
        try {
            //id不允许空
            if (prId == null) {
                return null;
            }

            //获取对应的步骤
            LfProcess process = empDao.findObjectByID(LfProcess.class, prId);
            //获取数据库连接对象
            LfDBConnect dbConnect = empDao.findObjectByID(LfDBConnect.class, process.getDbId());
            //获取数据库驱动
            String driver = new JDBCUtil().getClassNameByDBType(dbConnect.getDbType());
            String dbUrl=dbConnect.getConStr();
            if(driver.indexOf("mysql")>-1){
                dbUrl+="?useUnicode=true&characterEncoding=UTF-8";
            }
            //执行sql语句
            List<Map<String, String>> resultList = executeQuery(driver,dbUrl, dbConnect.getDbUser(), dbConnect.getDbPwd(), process.getSql(), null);
            //返回结果
            return resultList;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "下行业务预览，异常。");
            return null;
        }
    }

    public List<Map<String, String>> executeQuery(String driverName,String url, String user, String password, String sql, Object[] params) {
        Connection conn = null;
        PreparedStatement pState = null;
        ResultSet resultSet = null;
        try {
            //设置驱动名
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, user, password);
            EmpExecutionContext.sql("execute sql : " + sql);
            pState = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pState.setObject(i + 1, params[i]);
                }
            }
            resultSet = pState.executeQuery();

            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            List<Map<String, String>> rsList = new ArrayList<Map<String, String>>();
            while (resultSet.next()) {
                Map<String, String> rsMap = new HashMap<String, String>(columnCount);
                for (int i = 1; i < columnCount + 1; i++) {
                    rsMap.put(rsmd.getColumnLabel(i)/*.getColumnName(i)*/.toUpperCase(), resultSet.getString(i) == null ? "" : resultSet.getString(i));
                }
                if (rsMap.get("PHONE") != null && !"".equals(rsMap.get("PHONE").trim())) {
                    rsList.add(rsMap);
                    break;
                }

            }
            return rsList;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "执行查询语句失败，sql:" + sql + "，params：" + params);
            return null;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (pState != null) {
                    pState.close();
                }
                if (conn != null && conn.isClosed() == false) {
                    conn.close();
                }
            } catch (SQLException e) {
                EmpExecutionContext.error(e, "下行业务预览，数据库资源回收异常。");
            }
        }
    }

    /**
     * @param LfTimerVo
     * @param userId
     * @param pageInfo
     * @return
     * @throws Exception
     */
    public List<LfTimer> getTimerVos(LfTimer lfTimer, Long userId,
                                     PageInfo pageInfo) throws Exception {
        //获取查询结果
        List<LfTimer> timerVosList = new GenericLfTimerVoDAO().findLfTimerVo(lfTimer,
                userId, pageInfo);
        //返回查询结果
        return timerVosList;
    }

    /**
     * @param serId
     * @return
     * @throws Exception
     */
    public LfTimer getTaskByExpression(String serId) throws Exception {
        //任务管理对象biz
        TaskManagerBiz tmBiz = new TaskManagerBiz();
        //定时任务类
        LfTimer task = null;
        //获取定时任务对象集合
        List<LfTimer> tasksList = tmBiz.getTaskByExpression(serId);
        //获取任务对象
        if (tasksList != null && tasksList.size() == 1) {
            task = tasksList.get(0);
        }
        //返回获取的对象
        return task;
    }

    /**
     * @param taskId
     * @return
     * @throws Exception
     */
    public boolean startTaskByTaskId(Long taskId) throws Exception {
        //任务管理biz
        TaskManagerBiz tmBiz = new TaskManagerBiz();
        //不允许id为空
        if (taskId == null) {
            //返回否
            return false;
        }
        //开始任务并返回结果
        boolean result = tmBiz.startTask(taskId);
        //返回结果
        return result;
    }

    /**
     * @param timerTaskId
     * @return
     * @throws Exception
     */
    public boolean stopTaskByTaskId(Long timerTaskId) throws Exception {
        //任务管理biz
        TaskManagerBiz tmBiz = new TaskManagerBiz();
        //不允许id为空
        if (timerTaskId == null) {
            //返回否
            return false;
        }
        //停止任务并返回结果
        boolean result = tmBiz.stopTask(timerTaskId);
        //返回结果
        return result;
    }

    /**
     * @param timerTaskId
     * @return
     * @throws Exception
     */
    public boolean pauseTaskByTaskId(Long timerTaskId) throws Exception {
        //任务管理biz
        TaskManagerBiz tmBiz = new TaskManagerBiz();
        //不允许id为空
        if (timerTaskId == null) {
            //返回否
            return false;
        }
        //暂停任务并返回结果
        boolean result = tmBiz.pauseTask(timerTaskId);
        //返回结果
        return result;
    }

    /**
     * DESC: 校验发送内容是否为动态参数
     *
     * @param strDtMsg 发送内容
     * @return 返回发送类型  1=相同内容，2=不同内容
     */
    public String isDynameicParam(String strDtMsg) {

        String strExp = "#[pP]_[1-9][0-9]*#";

        Matcher objDynMatcher = Pattern.compile(strExp,
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(strDtMsg);
        //包含则返回
        if (objDynMatcher.find()) {
            return "2";
        }

        return "1";
    }

}