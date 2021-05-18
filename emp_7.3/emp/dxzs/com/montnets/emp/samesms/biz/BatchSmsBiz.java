package com.montnets.emp.samesms.biz;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.atom.UserdataAtom;
import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SmsSendBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.SMParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.WGStatus;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SmsSpecialDAO;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.sms.LfBigFile;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.TxtFileUtil;

public class BatchSmsBiz extends SmsSendBiz {

    private SmsSpecialDAO smsSpecialDAO = new SmsSpecialDAO();
    TxtFileUtil txtfileutil = new TxtFileUtil();
    // 格式化时间
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
    String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);

    public String batchSend(HttpServletRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        String result = "";
        LfBigFile lfbf = null;
        try {
            String bigfileid = request.getParameter("bigfileid");
            lfbf = new BaseBiz().getById(LfBigFile.class, bigfileid);
        }
        catch (Exception e) {
            EmpExecutionContext.error(e, "repair");
        }
        // 检查参数
        int num = lfbf.getEffNum();
        String bigtaiskids = request.getParameter("bigtaiskids");
        if (bigtaiskids == null || "".equals(bigtaiskids) || num != bigtaiskids.split(",").length) {

            result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
            return result;
        } else {

            lfbf.setTaskId(lfbf.getTaskId() + bigtaiskids);
            lfbf.setHandleStatus(4);
            lfbf.setUpdateTime(new Timestamp(new Date().getTime()));
            try {
                new BaseBiz().updateObj(lfbf);
            }
            catch (Exception e) {
                EmpExecutionContext.error(e, "更新bigtaiskids失败");
            }
        }

        Long lguserid = null;

        LfMttask mttask = new LfMttask();

        // 操作内容
        String oprInfo = "";
        // 发送类型：1-相同内容短信 2－动态模板短信；3－文件内容短信；4-客户群组短信
        String sendType = request.getParameter("sendType");

        // 日志内容
        if (sendType != null && sendType.trim().length() > 0) {
            if ("1".equals(sendType)) {
                oprInfo = "相同内容群发";
            } else if ("4".equals(sendType)) {
                oprInfo = "客户群组群发";
                // 创建客户群组短信任务时,sendType都按1来处理
                sendType = "1";
            } else {
                oprInfo = "不同内容群发";
            }
        } else {
            EmpExecutionContext.error("短信任务发送获取参数异常，sendType:"
                                      + sendType
                                      + "，errCode："
                                      + IErrorCode.V10001);
            result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
            EmpExecutionContext.logRequestUrl(request, "后台请求");
            return result;
        }

        // 当前登录操作员id
        String strlguserid = request.getParameter("lguserid");
        // 当前登录企业
        String lgcorpcode = request.getParameter("lgcorpcode");
        // 提交类型
        String bmtType = request.getParameter("bmtType");
        // 发送账号
        String spUser = request.getParameter("spUser");
        // 任务主题
        String title = request.getParameter("taskname");
        // 信息内容
        String msg = request.getParameter("msg");
        // 业务编码
        String busCode = request.getParameter("busCode");
        // 是否定时
        String timerStatus = request.getParameter("timerStatus");
        // 定时时间
        String timerTime = request.getParameter("timerTime");
        // 提交状态(已提交 2，已撤销3)
        Integer subState = 2;
        // 是否需要回复
        String isReply = request.getParameter("isReply");
        // 优先级
        String priority = request.getParameter("priority");
        // 尾号
        String subno = request.getParameter("subNo");
        // 预览结果
        // 提交总数
        String hidSubCount = request.getParameter("hidSubCount");
        // 有效总数
        String hidEffCount = request.getParameter("hidEffCount");
        // 文件绝对路径
        String hidMobileUrl = request.getParameter("hidMobileUrl");
        // 预发送条数
        String hidPreSendCount = request.getParameter("hidPreSendCount");
        // 贴尾内容
        String tailcontents = request.getParameter("tailcontents");
        // 判断页面参数是否为空
        if (strlguserid == null
            || lgcorpcode == null
            || spUser == null
            || msg == null
            || timerStatus == null
            || hidSubCount == null
            || hidEffCount == null
            || hidMobileUrl == null
            || hidPreSendCount == null) {
            EmpExecutionContext.error(oprInfo
                                      + "，发送获取参数异常，"
                                      + "strlguserid:"
                                      + strlguserid
                                      + ";lgcorpcode:"
                                      + lgcorpcode
                                      + ";spUser:"
                                      + spUser
                                      + ";msg:"
                                      + msg
                                      + ";timerStatus:"
                                      + timerStatus
                                      + ";hidSubCount:"
                                      + hidSubCount
                                      + ";hidEffCount:"
                                      + hidEffCount
                                      + ";hidMobileUrl:"
                                      + hidMobileUrl
                                      + ";hidPreSendCount:"
                                      + hidPreSendCount
                                      + "，errCode："
                                      + IErrorCode.V10001);
            result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
            EmpExecutionContext.error(result);
            return result;
        }

        // 登录操作员信息
        LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
        // 登录操作员信息为空
        if (lfSysuser == null) {
            EmpExecutionContext.error(oprInfo
                                      + "，从session获取登录操作员信息异常。lfSysuser为null，errCode："
                                      + IErrorCode.V10001);
            result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
            EmpExecutionContext.error(result);
            return result;
        }
        // 任务id
        String taskId = request.getParameter("taskId");
        try {
            // 操作员、企业编码、SP账号检查
            boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfSysuser,
                                                                   lgcorpcode,
                                                                   spUser,
                                                                   null);
            if (!checkFlag) {
                EmpExecutionContext.error(oprInfo
                                          + "，检查操作员、企业编码、发送账号不通过，，taskid:"
                                          + taskId
                                          + "，corpCode:"
                                          + lgcorpcode
                                          + "，userid："
                                          + strlguserid
                                          + "，spUser："
                                          + spUser
                                          + "，errCode:"
                                          + IErrorCode.V10001);
                result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
                EmpExecutionContext.error(result);
                return result;
            }
            // 判断任务ID是否合法
            if (!BalanceLogBiz.checkNumber(taskId) || "".equals(taskId)) {
                EmpExecutionContext.error(oprInfo
                                          + "，发送获取参数异常，taskId格式非法，taskId:"
                                          + taskId
                                          + "strlguserid:"
                                          + strlguserid
                                          + "，lgcorpcode:"
                                          + lgcorpcode
                                          + "，errCode："
                                          + IErrorCode.V10001);
                result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
                return result;
            } else {
                // 查询任务ID在是否在lf_mttask表已使用,false:存在；true:不存在
                if (!smsSpecialDAO.checkTaskIdNotUse(Long.parseLong(taskId.trim()))) {
                    EmpExecutionContext.error(oprInfo
                                              + "，发送获取参数异常，taskId已使用:"
                                              + taskId
                                              + "strlguserid:"
                                              + strlguserid
                                              + "，lgcorpcode:"
                                              + lgcorpcode
                                              + "，errCode："
                                              + IErrorCode.V10001);
                    result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
                    return result;
                }
                // 任务ID是否已在网关任务表存在,true:存在；false:不存在
                if (smsSpecialDAO.isExistTask(taskId)) {
                    EmpExecutionContext.error(oprInfo
                                              + "，发送获取参数异常，taskId在网关任务表已存在，taskId:"
                                              + taskId
                                              + "，strlguserid:"
                                              + strlguserid
                                              + "，lgcorpcode:"
                                              + lgcorpcode
                                              + "，errCode："
                                              + IErrorCode.V10001);
                    result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
                    return result;
                }
            }
        }
        catch (Exception e) {
            EmpExecutionContext.error(e, oprInfo
                                         + "taskId合法性校验异常 ，taskId:"
                                         + taskId
                                         + "strlguserid:"
                                         + strlguserid
                                         + ";lgcorpcode:"
                                         + lgcorpcode
                                         + "，errCode："
                                         + IErrorCode.V10012);
            result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10012);
            EmpExecutionContext.error(result);
            return result;
        }

        try {
            try {
                lguserid = Long.valueOf(strlguserid);

                // 非不同内容文件发送
                if (!"3".equals(sendType)) {
                    if (tailcontents != null) {
                        msg += tailcontents;
                    }
                }
                // 初始化任务对象
                // 提交总数
                mttask.setSubCount(Long.valueOf(hidSubCount));
                // 有效总数
                mttask.setEffCount(Long.valueOf(hidEffCount));
                // 文件绝对路径
                mttask.setMobileUrl(hidMobileUrl);
                // 预发送条数
                mttask.setIcount(hidPreSendCount);
                // 任务主题
                mttask.setTitle(title);
                // sp账号
                mttask.setSpUser(spUser);
                // 提交类型
                mttask.setBmtType(Integer.valueOf(bmtType));
                // 是否定时
                Integer timerStatus1 = (timerStatus == null || "".equals(timerStatus)) ? 0
                                                                                      : Integer.valueOf(timerStatus);
                // 定时时间
                mttask.setTimerStatus(timerStatus1);
                // 根据发送类型判断 短信类型
                mttask.setMsgType(Integer.valueOf(sendType));
                // 不同内容动态模块，设置消息编码为35
                if ("2".equals(sendType)) {
                    mttask.setMsgedcodetype(35);
                }
                // 信息类型：1－短信 2－彩信
                mttask.setMsType(1);
                // 提交类型
                mttask.setSubState(subState);
                // 业务类型
                mttask.setBusCode(busCode);
                // 中文•不能识别,转为英文字符
                msg = msg.replaceAll("•", "·").replace("¥", "￥");
                mttask.setMsg(msg);
                // 发送状态
                mttask.setSendstate(0);
                // 企业编码
                mttask.setCorpCode(lgcorpcode);
                // 发送优先级
                mttask.setSendLevel(Integer.valueOf(priority));
                // 是否需要回复
                mttask.setIsReply(Integer.valueOf(isReply));
                // 尾号
                mttask.setSubNo(subno);
                // 操作员id
                mttask.setUserId(lguserid);

                mttask.setTaskId(Long.valueOf(taskId));

                // 定时任务
                if (timerStatus1 == 1) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    timerTime = timerTime + ":00";
                    mttask.setTimerTime(new Timestamp(sdf.parse(timerTime).getTime()));
                } else {
                    // 非定时任务
                    mttask.setTimerTime(mttask.getSubmitTime());
                }

                UserdataAtom userdataAtom = new UserdataAtom();
                // 设置发送账户密码
                mttask.setSpPwd(userdataAtom.getUserPassWord(spUser));
            }
            catch (Exception e) {
                EmpExecutionContext.error(e,
                                          strlguserid,
                                          lgcorpcode,
                                          "初始化任务对象失败",
                                          IErrorCode.V10011);
                throw new EMPException(IErrorCode.V10011, e);
            }

            try {
                // 获取发送信息等缓存数据（是否计费、是否审核、用户编码）
                Map<String, String> infoMap = null;
                infoMap = new CommonBiz().checkMapNull(infoMap, lguserid, lgcorpcode);
                // 创建短信任务、判断审核流、扣费、判断创建定时任务、调用网关发送
                result = addBatchSmsLfMttask(mttask, infoMap, lfbf, bigtaiskids, lfSysuser);
                // 日志信息
                StringBuffer opContent = new StringBuffer("开始：").append(sdf.format(startTime))
                                                                .append("，耗时：")
                                                                .append((System.currentTimeMillis() - startTime))
                                                                .append("ms，title：")
                                                                .append(title)
                                                                .append("，taskid：")
                                                                .append(taskId)
                                                                .append("，spUser:")
                                                                .append(spUser)
                                                                .append("，提交总数：")
                                                                .append(hidSubCount)
                                                                .append("，有效数：")
                                                                .append(hidEffCount)
                                                                .append("，状态：")
                                                                .append(result);

                // 操作员名称
                String userName = lfSysuser.getUserName();
                EmpExecutionContext.info(oprInfo,
                                         lgcorpcode,
                                         strlguserid,
                                         userName,
                                         opContent.toString(),
                                         "OTHER");
                // 结果
                String reultClong = result;
                // 根据错误编码从网关定义查找错误信息
                String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
                // 根据错误编码从网关定义查找错误信息
                result = new WGStatus(langName).infoMap.get(result);
                // 如果返回状态网关中未定义，则重置为之前状态
                if (result == null) {
                    result = reultClong;
                }
            }
            catch (Exception e) {
                EmpExecutionContext.error(e, strlguserid, lgcorpcode, "创建短信任务失败", IErrorCode.V10012);
                throw new EMPException(IErrorCode.V10012, e);
            }
        }
        catch (Exception e) {
            ErrorCodeInfo errInfo = ErrorCodeInfo.getInstance();
            result = errInfo.getErrorInfo(e.getMessage());
            // 未知异常
            if (result == null) {
                result = errInfo.getErrorInfo(IErrorCode.V10015);
            }
            // 失败日志
            EmpExecutionContext.error(e, strlguserid, lgcorpcode, result, "");
        }
        return result;
    }

    private String addBatchSmsLfMttask(LfMttask mttask,
                                       Map<String, String> infoMap,
                                       LfBigFile lfbf,
                                       String bigtaiskids, LfSysuser lfSysuser) {
        if (infoMap == null) {
            infoMap = new HashMap<String, String>();
        }
        //页面生成的子号
        String subno=mttask.getSubNo();
        // 大文件发送标识
        infoMap.put("is_big_file_mw_kj", "1");
        List<LfMttask> taskList = createBatchFileP1(mttask, lfbf, bigtaiskids);
        String result = sendBatchSmsLfMttask(taskList, infoMap,  lfSysuser,subno);
        return result;
    }

    /**
     * 发送分配任务
     * 
     * @param taskList
     * @param infoMap
     * @return
     */
    private String sendBatchSmsLfMttask(List<LfMttask> taskList, Map<String, String> infoMap, LfSysuser lfSysuser,String jspsubNo) {
        String result = "";
        if (taskList == null || taskList.isEmpty()) {
            return "batchFail";
        }
        int total = 0;
        boolean flag = false;
        int i=0;
        for (LfMttask lfMttask : taskList) {
            i++;
            if (lfMttask.getIsReply() == 1) {
                
                String subNo = "";
                //第一个尾号使用页面传过来的尾号
                if(i==1){
                   subNo=jspsubNo; 
                }else{
                   subNo = generateSnbo(lfSysuser, lfMttask);  
                }

                if (subNo == null || "".equals(subNo)) {
                    continue;
                }
                String foo = checkSubnoIsVaild(lfMttask, subNo);
                if (!"success".equals(foo)) {

                    continue;
                }
                lfMttask.setSubNo(subNo);
            }

            
            result = addSmsLfMttask(lfMttask, infoMap);
            if ("000".equals(result)) {
                flag = true;
            }
            total++;
            EmpExecutionContext.info("超大批量短信发送，第"
                                     + total
                                     + "批，发送主题："
                                     + lfMttask.getTitle()
                                     + " ,任务ID："
                                     + lfMttask.getTaskId()
                                     + " ,发送结果："
                                     + result);
        }

        return flag ? "000" : result;
    }
    /**
     * 
     * 
     * @param lfSysuser
     * @param lfMttask
     * @return
     * 
     * @Description:
     */
    private String generateSnbo(LfSysuser lfSysuser, LfMttask lfMttask) {
        String subNo = "";
        String errorcode = "";
        SMParams smParams = new SMParams();
        // 编码（0模块编码1业务编码2产品编码3机构id4操作员guid,5任务id）
        smParams.setCodes(lfMttask.getTaskId().toString());
        // 编码类别
        smParams.setCodeType(5);
        smParams.setCorpCode(lfMttask.getCorpCode());
        // (分配类型0固定1自动有效期7天，null表是不设有效期)
        smParams.setAllotType(1);
        // 尾号是否确定插入表
        smParams.setSubnoVali(false);
        smParams.setTaskId(lfMttask.getTaskId());
        smParams.setLoginId(lfSysuser.getGuId().toString());
        ErrorCodeParam errorCodeParam = new ErrorCodeParam();
        try {
            LfSubnoAllotDetail subnoAllotDetail = GlobalVariableBiz.getInstance()
                                                                   .getSubnoDetail(smParams,
                                                                                   errorCodeParam);

            if (errorCodeParam.getErrorCode() != null
                && "EZHB237".equals(errorCodeParam.getErrorCode())) {
                // 没有可用的尾号（尾号已经用完）
                errorcode = "noUsedSubNo";
            } else if (errorCodeParam.getErrorCode() != null
                       && "EZHB238".equals(errorCodeParam.getErrorCode())) {
                // 获取尾号失败
                errorcode = "noSubNo";
            }
            subNo = subnoAllotDetail != null ? subnoAllotDetail.getUsedExtendSubno() : null;
            if (subNo == null || "".equals(subNo)) {
                // 获取尾号失败
                errorcode = "noSubNo";
            }
        }
        catch (Exception e) {
            errorcode = "error";
            EmpExecutionContext.error(e, lfMttask.getTaskId() + "对应子号获取失败" + errorcode);
        }
        return subNo;
    }

    /**
     * 
     * 
     * @param lfMttask
     * @param subNo
     * @return
     * 
     * @Description:
     */
    private String checkSubnoIsVaild(LfMttask lfMttask, String subNo) {
        String foo = "success";
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("userId", lfMttask.getSpUser());
        LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
        orderByMap.put("spisuncm", StaticValue.ASC);
        int strLen = 0;
        try {
            List<GtPortUsed> gtPortUseds = new BaseBiz().getByCondition(GtPortUsed.class,
                                                                        conditionMap,
                                                                        orderByMap);
            GtPortUsed gtPortUsed = null;
            for (int i = 0; i < gtPortUseds.size(); i++) {
                gtPortUsed = gtPortUseds.get(i);
                // 拓展尾号长度
                int cpnoLen = gtPortUsed.getCpno() != null ? gtPortUsed.getCpno().trim().length()
                                                          : 0;
                // 通道号+拓展尾号+尾号 的长度
                strLen = gtPortUsed.getSpgate().length() + cpnoLen + subNo.length();
                // 判断各运营商的通道号+拓展尾号+尾号是否大于20，如果大于20
                // 则前台需提示XX运营商通道号+尾号长度大于20，不允许发送
                if (gtPortUsed.getSpisuncm() == 0 && strLen > 20) {
                    foo = "ydfail";
                } else if (gtPortUsed.getSpisuncm() == 1 && strLen > 20) {
                    foo = "ltfail";
                } else if (gtPortUsed.getSpisuncm() == 21 && strLen > 20) {
                    foo = "dxfail";
                }
            }

        }
        catch (Exception e) {
            foo = "error";
            EmpExecutionContext.error(e, "营商通道号+尾号长度大于20");
        }
        return foo;
    }
    /**
     * 创建分批任务对象列表
     * 
     * @param mttask
     * @param lfbf
     * @param bigtaiskids
     * @return
     */
    private List<LfMttask> createBatchFileP1(LfMttask mttask, LfBigFile lfbf, String bigtaiskids) {
        List<LfMttask> taskList = new ArrayList<LfMttask>();

        try {
            int num = lfbf.getEffNum();
            String str[] = bigtaiskids.split(",");
            LfMttask task;
            for (int i = 0; i < num; i++) {

                task = new LfMttask();
                task.setBatchID(mttask.getBatchID());
                task.setBiginTime(mttask.getBiginTime());
                task.setBmtType(mttask.getBmtType());
                task.setBusCode(mttask.getBusCode());
                task.setComments(mttask.getComments());
                task.setContent(mttask.getContent());
                task.setEffCount(mttask.getEffCount());
                task.setEndTime(mttask.getEndTime());
                task.setErrorCodes(mttask.getErrorCodes());
                task.setFaiCount(mttask.getFaiCount());

                task.setFileuri(mttask.getFileuri());
                task.setIcount(mttask.getIcount());
                task.setIcount2(mttask.getIcount2());
                task.setIsReply(mttask.getIsReply());
                task.setIsRetry(mttask.getIsRetry());
                task.setMobileType(mttask.getMobileType());
                task.setMobileUrl(mttask.getMobileUrl());
                task.setMsg(mttask.getMsg());
                task.setMsgedcodetype(mttask.getMsgedcodetype());
                task.setMsgType(mttask.getMsgType());

                task.setMsType(mttask.getMsType());
                task.setMtId(mttask.getMtId());
                task.setParamcount(mttask.getParamcount());
                task.setParams(mttask.getParams());
                task.setReLevel(mttask.getReLevel());
                task.setReState(mttask.getReState());
                task.setRfail2(mttask.getRfail2());
                task.setRnret(mttask.getRnret());
                task.setSendLevel(mttask.getSendLevel());
                task.setSendstate(mttask.getSendstate());

                task.setSpNumber(mttask.getSpNumber());
                task.setSpPwd(mttask.getSpPwd());
                task.setSpUser(mttask.getSpUser());
                task.setSubCount(mttask.getSubCount());
                task.setSubmitTime(mttask.getSubmitTime());
                task.setSubNo(mttask.getSubNo());
                task.setSubState(mttask.getSubState());
                task.setSucCount(mttask.getSucCount());
                task.setTaskId(mttask.getTaskId());
                task.setTaskName(mttask.getTaskName());

                task.setTaskType(mttask.getTaskType());
                task.setTempid(mttask.getTempid());
                task.setTimerStatus(mttask.getTimerStatus());
                task.setTimerTime(mttask.getTimerTime());
                task.setTitle(num > 1 ? ((mttask.getTitle() == null ? "" : mttask.getTitle()) + "_" + (i + 1))
                                     : mttask.getTitle());
                task.setTmplPath(mttask.getTmplPath());
                task.setTxtType(mttask.getTxtType());
                task.setUserId(mttask.getUserId());
                task.setValidtm(mttask.getValidtm());
                task.setWySendInfo(mttask.getWySendInfo());

                task.setCorpCode(mttask.getCorpCode());

                String url = null;
                if (i == 0) {
                    url = lfbf.getFileUrl();
                } else if (i == 1) {
                    url = lfbf.getFileUrl2();
                } else if (i == 2) {
                    url = lfbf.getFileUrl3();
                } else if (i == 3) {
                    url = lfbf.getFileUrl4();
                } else if (i == 4) {
                    url = lfbf.getFileUrl5();
                } else if (i == 5) {
                    url = lfbf.getFileUrl6();
                } else if (i == 6) {
                    url = lfbf.getFileUrl7();
                } else if (i == 7) {
                    url = lfbf.getFileUrl8();
                } else if (i == 8) {
                    url = lfbf.getFileUrl9();
                }
                task.setTaskId(Long.valueOf(str[i]));

                if (i != num - 1) {
                    task.setEffCount(15000000L);
                    task.setIcount("15000000");

                } else {
                    long count = lfbf.getEffCount() - (num - 1) * 15000000;
                    task.setEffCount(count);
                    task.setIcount(String.valueOf(count));
                }
                task.setBusCode(lfbf.getBusCode());
                task.setFileuri(getUrl(url)[0]);
                task.setMobileUrl(getUrl(url)[1]);
                taskList.add(task);

            }

        }
        catch (Exception e) {
            EmpExecutionContext.error(e, "创建分配任务列表异常");
            return taskList;
        }

        return taskList;
    }

    private String[] getUrl(String url) {

        String[] str = new String[2];

        if (url.indexOf("file/smstxt") > -1) {

            str[0] = url.split("file/smstxt")[0];

            str[1] = "file/smstxt" + url.split("file/smstxt")[1];
        }

        return str;

    }

}
