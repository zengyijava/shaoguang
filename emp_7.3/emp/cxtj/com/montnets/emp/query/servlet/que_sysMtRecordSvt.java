package com.montnets.emp.query.servlet;

import com.montnets.emp.common.atom.UserdataAtom;
import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SmsSendBiz;
import com.montnets.emp.common.constant.*;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.blacklist.PbListBlack;
import com.montnets.emp.entity.dxzs.LfDfadvanced;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.perfect.LfPerfectNotic;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.query.biz.QueryBiz;
import com.montnets.emp.query.biz.ResendMtRecordBiz;
import com.montnets.emp.query.biz.SysMtRecordBiz;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.smstask.SmsUtil;
import com.montnets.emp.smstask.WGParams;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;

import org.apache.commons.beanutils.DynaBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime
 * @description
 */
@SuppressWarnings("serial")
public class que_sysMtRecordSvt extends BaseServlet {
    private final String empRoot = "cxtj";

    private final String base = "/query";
    private final String SDBF = "手动重发";

    /**
     * 查询日期格式化：HH:mm:ss
     */
    private final SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");

    private final SysMtRecordBiz mtRecordBiz = new SysMtRecordBiz();

    private final BaseBiz baseBiz = new BaseBiz();

    private final ResendMtRecordBiz resendMtRecordBiz = new ResendMtRecordBiz();
    
    private final SmsSendBiz smsSendBiz=new SmsSendBiz();

    /**
     * 系统下行记录查询
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //起始ms数
        long startl = System.currentTimeMillis();
        //获取用户信息
        LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY);
        // 记录类型
        String recordType = request.getParameter("recordType");
        // 企业编码
        String lgcorpcode = "";
        // 当前登录操作员id
        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);

        // 当前登录操作员
        String lgusername = request.getParameter("lgusername");
        //账户
        String userid = request.getParameter("userid");
        //通道号
        String spgate = request.getParameter("spgate");
        //开始时间
        String sendtime = request.getParameter("sendtime");
        //结束时间
        String recvtime = request.getParameter("recvtime");
        //电话号码
        String phone = request.getParameter("phone");
        //业务类型
        String buscode = request.getParameter("busCode");
        //运营应商
        String spisuncm = request.getParameter("spisuncm");
        // 操作员编码
        //String usercode = request.getParameter("usercode");
        //任务批次
        String taskid = request.getParameter("taskid");
        //错误码
        String mterrorcode = request.getParameter("mterrorcode");
        //自定义流水号
        String usermsgid = request.getParameter("usermsgid");

        //分页对象
        PageInfo pageInfo = new PageInfo();
        // 是否第一次访问
        boolean isFirstEnter = pageSet(pageInfo, request);
        long count = 0l;
        // 查询条件
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        try {
            QueryBiz qbiz = new QueryBiz();
            //获取登录sysuser
            LfSysuser sysuser = qbiz.getCurrentUser(request);
            if (sysuser == null) {
                EmpExecutionContext.error("下行记录查询,session中获取当前登录对象出现异常");
                return;
            }
            lgcorpcode = sysuser.getCorpCode();
            //判断企业编码获取
            if (lgcorpcode == null || "".equals(lgcorpcode)) {
                EmpExecutionContext.error("下行记录查询,session中获取企业编码出现异常");
                return;
            }
            //判断SP账号是否是属于本企业的
            if (userid != null && !"".equals(userid.trim()) && !"100000".equals(lgcorpcode)) {
                //多企业才处理
                if (StaticValue.getCORPTYPE() == 1) {
                    boolean checkFlag = new CheckUtil().checkSysuserInCorp(sysuser, lgcorpcode, userid, null);
                    if (!checkFlag) {
                        EmpExecutionContext.error("下行记录查询,检查操作员，企业编码，发送账号不通过，spuserid：" + userid + ",corpcode=" + lgcorpcode);
                        return;
                    }
                }
            }

            //设置业务类型，页面条件查询下拉框用
            getAndSetBus(lgcorpcode, request);

            //设置通道号和发送账号，页面条件查询下拉框用
            getAndSetSp(lgcorpcode, request);

            //第一次进入
            if (isFirstEnter) {
                request.getSession(RptStaticValue.GET_SESSION_FALSE).removeAttribute("sysMtTaskCon");
                return;
            }

            //企业编码
            conditionMap.put("lgcorpcode", lgcorpcode.trim());
            //查询类型   实时 或 历史
            if (recordType == null || recordType.length() < 1) {
                //实时
                recordType = "realTime";
            }
            //查询类型
            conditionMap.put("recordType", recordType);
            //运营商
            conditionMap.put("spisuncm", spisuncm);
            //taskid
            conditionMap.put("taskid", taskid);
            //sp账户
            conditionMap.put("userid", userid);
            //通道号
            conditionMap.put("spgate", spgate);
            //手机号
            conditionMap.put("phone", phone);
            ////业务类型
            conditionMap.put("buscode", buscode);
            //开始时间
            conditionMap.put("sendtime", sendtime);
            //结束时间
            conditionMap.put("recvtime", recvtime);
            //错误码条件
            conditionMap.put("mterrorcode", mterrorcode);
            //自定义流水号
            conditionMap.put("usermsgid", usermsgid);

            //session中获取当前登录操作员对象
            LfSysuser curUser = getCurUserInSession(request);
            //设置有权限看的操作员编码。不需要考虑权限则是空字符串，目前只有admin和管辖范围是顶级机构不需要考虑权限
            String usercode = mtRecordBiz.getPermissionUserCode(curUser);
            if (usercode == null) {
                //当前操作员编码
                conditionMap.put("curUsercode", "'" + curUser.getUserCode() + "'");
                //当前操作员id
                conditionMap.put("curUserId", curUser.getUserId().toString());
            } else {
                //当前操作员编码
                conditionMap.put("curUsercode", "'" + curUser.getUserCode() + "'");
                //当前操作员id
                conditionMap.put("curUserId", curUser.getUserId().toString());
                //有权限看的操作员编码
                conditionMap.put("domUsercode", usercode);
            }
            String spuserpri = mtRecordBiz.getPermissionSpuserMtpri(curUser);
            if (spuserpri == null) {
                //当前操作员编码
                conditionMap.put("spcurcorpcode", "'" + curUser.getCorpCode() + "'");
                //当前操作员id
                conditionMap.put("spcurUserId", curUser.getUserId().toString());
            } else {
                //有权限看的操作员编码
                conditionMap.put("spuserpri", spuserpri);
            }

            //实时库分页对象
            Object realDbpageInfoObj = request.getSession(false).getAttribute("mtRealDbpageInfo");
            PageInfo realDbpageInfo;
            //从session获取
            if (realDbpageInfoObj != null) {
                realDbpageInfo = (PageInfo) realDbpageInfoObj;
            }
            //从session获取为空
            else {
                //声明一个对象
                realDbpageInfo = new PageInfo();
            }
            //查询下行记录，如果实时记录查询，则先查备份表，如果没记录，则再查实时表
            List<DynaBean> mtTaskList = getMtTasks(conditionMap, pageInfo, request, realDbpageInfo);

            //拿到下行记录信息,根据登录信息判断是否可以显示下发信息内容中的数字
            mtRecordBiz.setMtTaskList(lfSysuser, mtTaskList);


            //没记录就不需要查询分页
            if (mtTaskList == null || mtTaskList.size() == 0) {
                pageInfo.setNeedNewData(2);
                pageInfo.setTotalRec(0);
                pageInfo.setTotalPage(1);
            }

            //错误码说明map，key序号，value为错误码说明
            Map<String, String> errCodeDesMap = mtRecordBiz.getErrCodeDisMap(mtTaskList, lgcorpcode);
            request.setAttribute("errCodeDesMap", errCodeDesMap);

            //modify by tanglili 20181217-------------开始
            //此修改用于补发
            //将长短信拆分后的短信流水号，都设置为长短信拆分后第一条短信的流水号。以便补发的处理
            //用于存放处理后的平台流水号
            List<String> ptmsgidList = new ArrayList<String>();
            long ssid = 17179869184L;
            Long firstPtmsgid = 0L;
            int pknumber = 0;
            if ("realTime".equals(recordType)) {
                if (mtTaskList != null && mtTaskList.size() > 0) {
                    for (int i = 0; i < mtTaskList.size(); i++) {
                        //如果不是第一条短信,则需要重置流水号，计算出此长短信第一条短信的流水号
                        pknumber = Integer.parseInt(String.valueOf(mtTaskList.get(i).get("pknumber")));
                        if (pknumber > 1) {
                            firstPtmsgid = Long.parseLong(String.valueOf(mtTaskList.get(i).get("ptmsgid"))) - (pknumber - 1) * ssid;
                            //mtTaskList.get(i).set("ptmsgid", firstPtmsgid);
                            ptmsgidList.add(String.valueOf(firstPtmsgid));
                        } else {
                            ptmsgidList.add(String.valueOf(mtTaskList.get(i).get("ptmsgid")));
                        }
                    }
                }
            }
            request.setAttribute("ptmsgidList", ptmsgidList);
            //modify by tanglili 20181217-------------结束

            request.setAttribute("sysMtTaskList", mtTaskList);
            //设置查询条件到session，这个要放在查询方法之后，因为查询方法里还会设置一些查询条件
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("sysMtTaskCon", conditionMap);


            //获取sp账号列表
            List<Userdata> spUserList = smsBiz.getSpUserList(sysuser);
            request.setAttribute("sendUserList", spUserList);

            //获取高级设置默认信息
            LinkedHashMap<String, String> spConditionMap = new LinkedHashMap<String, String>();
            spConditionMap.put("userid", String.valueOf(lguserid));
            //1:相同内容群发
            spConditionMap.put("flag", "1");
            LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
            orderMap.put("id", StaticValue.DESC);
            List<LfDfadvanced> lfDfadvancedList = baseBiz.getByCondition(LfDfadvanced.class, spConditionMap, orderMap);
            LfDfadvanced lfDfadvanced = null;
            if (lfDfadvancedList != null && lfDfadvancedList.size() > 0) {
                lfDfadvanced = lfDfadvancedList.get(0);
            }
            request.setAttribute("lfDfadvanced", lfDfadvanced);

            //获取总数，用于记录日志
            count = mtTaskList == null ? 0 : mtTaskList.size();
        } catch (Exception e) {
            // 异常处理
            EmpExecutionContext.error(e, "系统下行记录查询，异常。");
            request.setAttribute("findresult", "-1");
        } finally {
            // 是否第一次进入标示
            request.setAttribute("isFirstEnter", isFirstEnter);
            request.setAttribute("pageInfo", pageInfo);

            String conditionstr = "userid=" + conditionMap.get("userid") + ",bindspUserid=" + conditionMap.get("spUsers")
                    + ",spgate=" + conditionMap.get("spgate") + ",buscode=" + conditionMap.get("buscode")
                    + ",spisuncm=" + conditionMap.get("spisuncm") + ",phone=" + conditionMap.get("phone")
                    + ",taskid=" + conditionMap.get("taskid") + ",sendtime=" + conditionMap.get("sendtime")
                    + ",recvtime=" + conditionMap.get("recvtime") + ",pageindex=" + pageInfo.getPageIndex();
            //开始时间
            String starthms = hms.format(startl);
            //菜单初次点入判断
            if (recordType == null) {
                recordType = "(菜单初次点入)";
            }
            String opContent = "系统下行记录" + recordType + " totalcount:" + count + "条 ,开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms,条件:" + conditionstr;
            EmpExecutionContext.info("查询统计", lgcorpcode, lguserid, lgusername, opContent, StaticValue.GET);

            request.getRequestDispatcher(empRoot + base + "/que_sysMtRecord.jsp").forward(request, response);
        }
    }

    /**
     * @param conditionMap 查询条件
     * @param pageInfo     分页对象
     * @param request      请求对象
     * @return 返回下行记录对象集合
     * @description 查询下行记录，如果是实时查询，则点击查询时，先查备份表，没记录，则查实时表
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-26 上午10:55:24
     */
    private List<DynaBean> getMtTasks(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, HttpServletRequest request) {
        List<DynaBean> mtTaskList;
        //页面点击查询，则先用备份表查
        if (pageInfo.getNeedNewData() == 1) {
            //查备份表
            conditionMap.put("realTableName", "gw_mt_task_bak");
            mtTaskList = mtRecordBiz.getMtRecords(conditionMap, pageInfo);

            //下行记录查询，之前是备份表没有记录则查询实时表，现在改成备份表和实时表联合查询。查询实时表的代码注释。
//			//如果是实时查询，且上面查备份表没记录，则查实时表
//			if((mtTaskList == null || mtTaskList.size() < 1) && "realTime".equals(conditionMap.get("recordType")))
//			{
//				//查实时表
//				conditionMap.put("realTableName", "mt_task");
//				mtTaskList = mtRecordBiz.getMtRecords(conditionMap, pageInfo);
//			}
            return mtTaskList;
        }
        //页面点击分页，则用上次的表查

        //取session中，上次使用的表名
        LinkedHashMap<String, String> conditionMapPre = (LinkedHashMap<String, String>) request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("sysMtTaskCon");
        //拿不到，则使用默认值
        if (conditionMapPre == null) {
            conditionMapPre = new LinkedHashMap<String, String>();
            conditionMapPre.put("realTableName", "gw_mt_task_bak");
        }
        //设置上次使用的表名
        conditionMap.put("realTableName", conditionMapPre.get("realTableName"));
        //查询
        mtTaskList = mtRecordBiz.getMtRecords(conditionMap, pageInfo);
        return mtTaskList;
    }

    /**
     * @param conditionMap 查询条件
     * @param pageInfo     分页对象
     * @param request      请求对象
     * @return 返回下行记录对象集合
     * @description 查询下行记录，如果是实时查询，则点击查询时，先查备份表，没记录，则查实时表
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-26 上午10:55:24
     */
    private List<DynaBean> getMtTasks(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, HttpServletRequest request, PageInfo realDbpageInfo) {
        List<DynaBean> mtTaskList;
        //页面点击查询，则先用备份表查
        if (pageInfo.getNeedNewData() == 1) {
            //查备份表
            conditionMap.put("realTableName", "gw_mt_task_bak");
            mtTaskList = mtRecordBiz.getMtRecords(conditionMap, pageInfo, realDbpageInfo);

            //下行记录查询，之前是备份表没有记录则查询实时表，现在改成备份表和实时表联合查询。查询实时表的代码注释。
//			//如果是实时查询，且上面查备份表没记录，则查实时表
//			if((mtTaskList == null || mtTaskList.size() < 1) && "realTime".equals(conditionMap.get("recordType")))
//			{
//				//查实时表
//				conditionMap.put("realTableName", "mt_task");
//				mtTaskList = mtRecordBiz.getMtRecords(conditionMap, pageInfo);
//			}
            return mtTaskList;
        }
        //页面点击分页，则用上次的表查

        //取session中，上次使用的表名
        LinkedHashMap<String, String> conditionMapPre = (LinkedHashMap<String, String>) request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("sysMtTaskCon");
        //拿不到，则使用默认值
        if (conditionMapPre == null) {
            conditionMapPre = new LinkedHashMap<String, String>();
            conditionMapPre.put("realTableName", "gw_mt_task_bak");
        }
        //设置上次使用的表名
        conditionMap.put("realTableName", conditionMapPre.get("realTableName"));
        //查询
        mtTaskList = mtRecordBiz.getMtRecords(conditionMap, pageInfo, realDbpageInfo);
        return mtTaskList;
    }

    /**
     * 查询下行记录分页信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getMtPageInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //起始ms数
        long startl = System.currentTimeMillis();
        String conditionstr = "";
        //分页对象
        PageInfo pageInfo = new PageInfo();
        try {
        	LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY);
        	if(null == lfSysuser){
        		return;
        	}
        	
            // 查询条件
            LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>) request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("sysMtTaskCon");
            if (conditionMap == null || conditionMap.size() < 1) {
                EmpExecutionContext.error("svt查询下行记录分页信息，获取查询条件对象集合为空。");
                return;
            }

            pageSet(pageInfo, request);
            //实时库分页对象
            Object realDbpageInfoObj = request.getSession(false).getAttribute("mtRealDbpageInfo");
            PageInfo realDbpageInfo;
            //从session获取
            if (realDbpageInfoObj != null) {
                realDbpageInfo = (PageInfo) realDbpageInfoObj;
            }
            //从session获取为空
            else {
                //声明一个对象
                realDbpageInfo = new PageInfo();
            }

            SysMtRecordBiz mtRecordBiz = new SysMtRecordBiz();
            //设置分页对象和实时库分页对象
            mtRecordBiz.getMtRecordsPageInfo(conditionMap, pageInfo, realDbpageInfo);
            //设置到session中
            request.getSession(false).setAttribute("mtRealDbpageInfo", realDbpageInfo);

            conditionstr = "userid=" + conditionMap.get("userid") + ",bindspUserid=" + conditionMap.get("spUsers")
                    + ",spgate=" + conditionMap.get("spgate") + ",buscode=" + conditionMap.get("buscode")
                    + ",spisuncm=" + conditionMap.get("spisuncm") + ",phone=" + conditionMap.get("phone")
                    + ",taskid=" + conditionMap.get("taskid") + ",sendtime=" + conditionMap.get("sendtime")
                    + ",recvtime=" + conditionMap.get("recvtime") + ",totalRec=" + pageInfo.getTotalRec();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "svt查询下行记录分页信息，异常。");
        } finally {
            response.getWriter().print("{totalRec:" + pageInfo.getTotalRec() + ",totalPage:" + pageInfo.getTotalPage() + "}");
            //开始时间
            String starthms = hms.format(startl);
            String opContent = "系统下行记录。分页：" + pageInfo.getTotalRec() + "条 ,开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms,条件：" + conditionstr;
            setLog(request, "查询统计", opContent, StaticValue.GET);
        }
    }

    /**
     * 获取并设置业务类型到request里
     *
     * @param lgcorpcode 企业编码
     * @param request    请求对象
     * @return 成功返回true
     */
    private boolean getAndSetBus(String lgcorpcode, HttpServletRequest request) {
        try {
            // 查询条件
            LinkedHashMap<String, String> conditionMMap = new LinkedHashMap<String, String>();
            // 获取业务类型
            if (!"100000".equals(lgcorpcode)) {
                // 只显示自定义业务
                conditionMMap.put("corpCode&in", "0," + lgcorpcode);
            } else {
                conditionMMap.put("corpCode&not in", "1,2");
            }

            BaseBiz baseBiz = new BaseBiz();
            List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, null, conditionMMap, null);
            LinkedHashMap<String, String> busMap = new LinkedHashMap<String, String>();
            if (busList != null && busList.size() > 0) {
                for (LfBusManager bus : busList) {
                    busMap.put(bus.getBusCode(), bus.getBusName());
                }
            }
            request.setAttribute("busMap", busMap);
            return true;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "系统下行记录，获取并设置业务类型，异常。");
            return false;
        }
    }

    /**
     * 获取并设置通道和发送账号
     *
     * @param lgcorpcode 企业编码
     * @param request    请求对象
     * @return 成功返回true
     */
    private boolean getAndSetSp(String lgcorpcode, HttpServletRequest request) {
        try {
            SysMtRecordBiz mtRecordBiz = new SysMtRecordBiz();
            List<DynaBean> spList = mtRecordBiz.getSpgateList(lgcorpcode);
            request.setAttribute("spList", spList);

            // 页面SP账号查询条件
            List<String> lfsp = mtRecordBiz.getSpUserList(lgcorpcode);
            request.setAttribute("mrUserList", lfsp);
            return true;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "系统下行记录，获取并设置通道和发送账号，异常。");
            return false;
        }
    }

    /**
     * 写日志
     *
     * @param request   请求对象
     * @param opModule  菜单名称
     * @param opContent 操作内容
     * @param opType
     */
    private void setLog(HttpServletRequest request, String opModule, String opContent, String opType) {
        try {
            Object loginSysuserObj = request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
            if (loginSysuserObj == null) {
                EmpExecutionContext.error("系统下行记录，记录日志，获取不到session当前登录操作员对象。");
                return;
            }

            LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
            EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId() + "", loginSysuser.getUserName(), opContent, opType);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "系统下行记录，记录日志，异常。");
        }
    }

    /**
     * 获取session当前登录操作员的企业编码
     *
     * @param request
     * @return 返回企业编码
     */
    private String getCurrenCorpCode(HttpServletRequest request) {
        try {
            Object loginSysuserObj = request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
            if (loginSysuserObj == null) {
                return null;
            }
            LfSysuser sysUser = (LfSysuser) loginSysuserObj;
            return sysUser.getCorpCode();

        } catch (Exception e) {
            EmpExecutionContext.error("SESSION获取企业编码失败。");
            return null;
        }
    }

    /**
     * 从session获取当前登录操作员对象
     *
     * @param request 请求对象
     * @return 返回当前登录操作员对象，为空则返回null
     */
    private LfSysuser getCurUserInSession(HttpServletRequest request) {
        Object loginSysuserObj = request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
        if (loginSysuserObj == null) {
            EmpExecutionContext.error("系统下行记录，从session获取当前登录操作员对象为空。");
            return null;
        }
        return (LfSysuser) loginSysuserObj;
    }

    /**
     * 手动批量重发
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author wengkb
     */
    public void reSendSMSAllMt(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SuperOpLog spLog = new SuperOpLog();//操作日志
        LfSysuser lfSysuser = (LfSysuser) request.getSession().getAttribute("loginSysuser");
        String result = ""; //重发状态

//        // 查询条件
//        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

        try {
//            //分页对象
//            PageInfo pageInfo = new PageInfo();
//            pageSet(pageInfo, request);
            String ptmsgIds = request.getParameter("ptmsgIds");

            //modify by tanglili 20181217-------------开始
            //过滤掉重复的平台流水号
            //平台流水号为空，则返回
            String oldptmsgIds = ptmsgIds;
            //处理平台流水号
            ptmsgIds = resendMtRecordBiz.handlePtmsgIds(ptmsgIds);
            if ("error".equals(ptmsgIds)) {
                result = ptmsgIds;
                EmpExecutionContext.error("处理平台流水号失败！平台流水号为:"+oldptmsgIds);
                return;
            }
            EmpExecutionContext.info("接口短信补发，传入的ptmsgid集合为:" + oldptmsgIds + ",过滤重复的ptmsgid后的集合为:" + ptmsgIds);
            //modify by tanglili 20181217-------------结束

//            String pageSize = request.getParameter("pageSize");
//
//            pageInfo.setPageSize(Integer.parseInt(pageSize));
//
//            //设置有权限看的操作员编码。不需要考虑权限则是空字符串，目前只有admin和管辖范围是顶级机构不需要考虑权限
//            String usercode = mtRecordBiz.getPermissionUserCode(lfSysuser);
//            if (usercode == null) {
//                //当前操作员编码
//                conditionMap.put("curUsercode", "'" + lfSysuser.getUserCode() + "'");
//                //当前操作员id
//                conditionMap.put("curUserId", lfSysuser.getUserId().toString());
//            } else {
//                //当前操作员编码
//                conditionMap.put("curUsercode", "'" + lfSysuser.getUserCode() + "'");
//                //当前操作员id
//                conditionMap.put("curUserId", lfSysuser.getUserId().toString());
//                //有权限看的操作员编码
//                conditionMap.put("domUsercode", usercode);
//            }
//            String spuserpri = mtRecordBiz.getPermissionSpuserMtpri(lfSysuser);
//            if (spuserpri == null) {
//                //当前操作员编码
//                conditionMap.put("spcurcorpcode", "'" + lfSysuser.getCorpCode() + "'");
//                //当前操作员id
//                conditionMap.put("spcurUserId", lfSysuser.getUserId().toString());
//            } else {
//                //有权限看的操作员编码
//                conditionMap.put("spuserpri", spuserpri);
//            }
//
//
//            //// 分解批量短信id
//            //conditionMap.put("ids", msgIds);
//            //modify by tanglili 20181217-------------开始
//            //修改后，需要用平台流水号来处理
//            conditionMap.put("ptmsgids", ptmsgIds);
//            //modify by tanglili 20181217-------------结束
//            conditionMap.put("recordType", "realTime");
//            conditionMap.put("lguserid", lfSysuser.getUserId().toString());
//            conditionMap.put("permissionType", lfSysuser.getPermissionType().toString());
//            //查询下行记录，如果实时记录查询，则先查备份表，如果没记录，则再查实时表
//            List<DynaBean> mtTaskList = getResendMtTasks(conditionMap, pageInfo, request);
//            查询 批量发送短信第一条
            
            //查询拆分后的第一条短信
            List<DynaBean> mtTaskList = resendMtRecordBiz.findMtTaskByPtmsgids(ptmsgIds);
            if (mtTaskList == null || mtTaskList.size() == 0) {
                result = "error";
                EmpExecutionContext.error("查询拆分后的第一条短信失败！");
                return;
            }
            //如果有一条发送成功，则认为成功
            boolean isSuccess=false;
            String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
            for (int i = 0; i < mtTaskList.size(); i++) {
                    DynaBean mtTask = mtTaskList.get(i);
                    result = reSendOne(mtTask, lfSysuser.getUserCode(), lfSysuser.getUserName(), lfSysuser.getCorpCode(), langName);
                    //有一条发送成功，则认为成功
                    if("createSuccess".equals(result))
                    {
                    	isSuccess=true;
                    	EmpExecutionContext.info("第"+(i+1)+"次补发成功！");
                    }else
                    {
                    	EmpExecutionContext.error("第"+(i+1)+"次补发失败！错误码为:"+result);
                    }
            }
                if(isSuccess)
                {
                	result = "createSuccess" + "count:" + mtTaskList.size();
                }else
                {
                	EmpExecutionContext.error("批量补发失败！平台流水号为:"+oldptmsgIds);
                	result = "error";
                }

        } catch (Exception e1) {
            spLog.logFailureString(lfSysuser.getUserName(), StaticValue.STATISTICS, SDBF, "操作员：" + lfSysuser.getUserName() + "手动批量补发短信失败。", e1, lfSysuser.getCorpCode());
            EmpExecutionContext.error(e1, "系统下行短信重发异常");
        } finally {
            response.getWriter().print(result);
        }
    }

    /**
     * @param conditionMap 查询条件
     * @param pageInfo     分页对象
     * @param request      请求对象
     * @return 返回下行记录对象集合
     * @description 批量重发查询下行记录，如果是实时查询，则点击查询时，先查备份表，没记录，则查实时表
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-26 上午10:55:24
     */
    private List<DynaBean> getResendMtTasks(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, HttpServletRequest request) {
        List<DynaBean> mtTaskList;
        //页面点击查询，则先用备份表查
        if (pageInfo.getNeedNewData() == 1) {
            //查备份表
            conditionMap.put("realTableName", "gw_mt_task_bak");
            mtTaskList = mtRecordBiz.getMtRecords(conditionMap, pageInfo);

            //下行记录查询，之前是备份表没有记录则查询实时表，现在改成备份表和实时表联合查询。查询实时表的代码注释。
//			//如果是实时查询，且上面查备份表没记录，则查实时表
//			if((mtTaskList == null || mtTaskList.size() < 1) && "realTime".equals(conditionMap.get("recordType")))
//			{
//				//查实时表
//				conditionMap.put("realTableName", "mt_task");
//				mtTaskList = mtRecordBiz.getMtRecords(conditionMap, pageInfo);
//			}
            return mtTaskList;
        }
        //页面点击分页，则用上次的表查

        //取session中，上次使用的表名
        LinkedHashMap<String, String> conditionMapPre = (LinkedHashMap<String, String>) request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("sysMtTaskCon");
        //拿不到，则使用默认值
        if (conditionMapPre == null) {
            conditionMapPre = new LinkedHashMap<String, String>();
            conditionMapPre.put("realTableName", "gw_mt_task_bak");
        }
        //设置上次使用的表名
        conditionMap.put("realTableName", conditionMapPre.get("realTableName"));
        //查询
        mtTaskList = mtRecordBiz.getMtRecords(conditionMap, pageInfo);
        return mtTaskList;
    }

    /**
     * 单条发送
     *
     * @param mtTask
     * @param userCode
     * @param userName
     * @return
     * @throws Exception
     */
    public String reSendOne(DynaBean mtTask, String userCode, String userName, String lgcorpcode, String langName) throws Exception {
        SuperOpLog spLog = new SuperOpLog();//操作日志
        String result = "";
        String passwd = "";
        String wgRespond = "";  //请求网关返回的响应信息
        //StringBuffer message = new StringBuffer(); //短信内容
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        try {
            String phone = mtTask.get("phone").toString();
            String svrtype = mtTask.get("svrtype").toString();
            String ptmsgid = mtTask.get("ptmsgid").toString();
            String pktotal = mtTask.get("pktotal").toString();
            String spid = mtTask.get("userid").toString();
            String spgate = mtTask.get("spgate").toString();

            conditionMap.put("phone", phone);
            conditionMap.put("svrType", svrtype);
            conditionMap.put("optype", "1");
            conditionMap.put("blType", "1");

            BaseBiz baseBiz = new BaseBiz();
            List<PbListBlack> blackList = baseBiz.getByCondition(PbListBlack.class, conditionMap, null);
            if (blackList.size() > 0) {
                result = "blackList";
                EmpExecutionContext.error("下行记录补发，手机号码为黑名单！phone=" + phone+ ",svrtype="+svrtype);
                return result;
            }

            //modify by tanglili 20181225-------------开始
            //通过长短信第一条，查询出此长短信拆分后的短信
            String message = resendMtRecordBiz.getMessageByFirst(Long.parseLong(ptmsgid), Integer.parseInt(pktotal));
            if (message == null) {
                EmpExecutionContext.error("下行记录补发，根据第一条短信查找并且拼接长短信的所有拆分短信失败！ptmsgid=" + ptmsgid + ",pktotal=" + pktotal);
                return "error";
            }

            //如果存在签名，则去掉签名
            String spisuncm = mtTask.get("unicom").toString();
            GtPortUsed gtPortUsed = resendMtRecordBiz.getGtPortUsed(spid, spisuncm);
            if (gtPortUsed == null) {
            	EmpExecutionContext.error("下行记录补发，查询签名信息失败！"+"spid="+spid+",spisuncm="+spisuncm);
                return "error";
            }
            String finalMessage = null;
            //获取中文签名
            String signStr = gtPortUsed.getSignstr();
            //不是国际通道
            if (!"5".equals(spisuncm)) {
                //去掉短信的签名
                finalMessage = resendMtRecordBiz.handleMessage(message, signStr);
            } else   //国际通道
            {
                //英文签名
                String enSignStr = gtPortUsed.getEnsignstr();
                //去掉国际短信的签名
                finalMessage = resendMtRecordBiz.handleGJMessage(message, signStr, enSignStr);
            }
            if (finalMessage == null) {
            	EmpExecutionContext.error("下行记录补发，获取短信内容失败！"+"spid="+spid+",spisuncm="+spisuncm);
                return "error";
            }
            
            //查询贴尾
            String tailContent=smsSendBiz.getTailContents(svrtype, spid, lgcorpcode);
            //存在贴尾，则去掉贴尾
            if(tailContent!=null&&!"".equals(tailContent))
            {
            	if(finalMessage.endsWith(tailContent))
            	{
            		int messageLen=finalMessage.length()-tailContent.length();
					finalMessage = finalMessage.substring(0, messageLen);
            	}
//            	else
//            	{
//            		EmpExecutionContext.error("接口补发，去掉贴尾失败！短信内容:"+finalMessage+",贴尾:"+tailContent);
//            		return "error";
//            	}
            }

            //过滤关键字
            // 包含的关键字，为""时内容无关键字
            String words = keyWordAtom.checkText(finalMessage, lgcorpcode);
            // 包含关键字
            if (!"".equals(words)) {
                EmpExecutionContext.error("接口补发，关键字检查未通过，发送内容包含违禁词组:'"
                        + words
                        + "，corpCode:"
                        + lgcorpcode
                        + "，errCode:"
                        + IErrorCode.V10016
                        + ",短信内容为:" + finalMessage);
                ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
                String desc = info.getErrorInfo(IErrorCode.V10016);
                result = desc + words;
                return result;
            }

            //modify by tanglili 20181225-------------结束

            WGParams wgParams = new WGParams(); //创建发送对象
            UserdataAtom userdataAtom = new UserdataAtom();
            passwd = userdataAtom.getUserPassWord(spid);
            wgParams.setCommand("MT_REQUEST");
            wgParams.setSpid(spid);
            wgParams.setSppassword(passwd);
            wgParams.setDa(phone);
            wgParams.setSm(finalMessage);
            wgParams.setParam1(userCode);
            wgParams.setSvrtype(svrtype);
            wgParams.setDas("15");  //15表示的是GBK编码
            wgParams.setBgtyhn("2"); //2表示是http接口发送
            Map<String, String[]> prop = WebgatePropInfo.getProp(); //读取配置文件
            SmsUtil smsutil = new SmsUtil();
            wgRespond = smsutil.execute(wgParams, prop.get("webgateProp")[1]); //调用发送接口  得到网关返回的状态信息
            if (wgRespond.indexOf("mtstat=ACCEPTD") > -1 && wgRespond.indexOf("mterrcode=000") > -1) {
                spLog.logSuccessString(userName, StaticValue.STATISTICS, SDBF, "操作员：" + userName + "手动补发手机号为(" + phone + ")短信成功。", userCode);
                result = "createSuccess";
            } else {
                spLog.logFailureString(userName, StaticValue.STATISTICS, SDBF, "操作员：" + userName + "手动补发手机号为(" + phone + ")短信失败。", null, userCode);
                EmpExecutionContext.error("操作员：" + userName + "手动补发手机号为" + phone + "失败，网关返回的状态码：" + wgRespond);
                result = "error";
            }

        } catch (Exception e) {
            result = "error";
            EmpExecutionContext.error(e, "系统下行短信重发异常");
        }

        return result;
    }


    //批量重发需要用到
    private final WgMsgConfigBiz msgConfigBiz = new WgMsgConfigBiz();

    private final TxtFileUtil txtFileUtil = new TxtFileUtil();

    private final KeyWordAtom keyWordAtom = new KeyWordAtom();

    private final BalanceLogBiz balancebiz = new BalanceLogBiz();

    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    private final CommonBiz commonBiz = new CommonBiz();

    private final SmsBiz smsBiz = new SmsBiz();

    private final String DESC = "(系统下行记录补发)";

    /**
     * 补发前的验证
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void sendValidate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result = "";
        String oldtaskId = "";
        try {
            oldtaskId = request.getParameter("taskid");
            LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
            conMap.put("taskId", oldtaskId);
            //根据mtid获取任务信息
            List<LfMttask> lfmttaskList = baseBiz.findListByCondition(LfMttask.class, conMap, null);
            if (lfmttaskList == null) {
                result = "error";
                EmpExecutionContext.error("无法确定任务类型，不进行补发。taskid=" + oldtaskId);
                return;
            }
            if (lfmttaskList.size() == 0) {
                List<LfPerfectNotic> perfectNoticeList = baseBiz.findListByCondition(LfPerfectNotic.class, conMap, null);
                if (perfectNoticeList != null && perfectNoticeList.size() > 0) {
                    result = "wmtznotsupport";
                    EmpExecutionContext.info("完美通知信息不可进行补发！taskid=" + oldtaskId);
                    return;
                } else {
                    result = "error";
                    EmpExecutionContext.error("无法确定任务类型，不进行补发。taskid=" + oldtaskId);
                    return;
                }
            }

            LfMttask lfmttask = lfmttaskList.get(0);
            //移动财务不支持补发
            //信息类型 1-短信， 2-彩信，3为短信模板，4为彩信模板，5-移动财务 6-网讯 7-智能引擎下行业务 8-APP短信 9-员工生日祝福  10-客户生日祝福 21富信发送
            if (lfmttask.getMsType().intValue() == 5) {
                result = "ydcwnotsupport";
                EmpExecutionContext.info("移动财务信息不可进行补发！taskid=" + oldtaskId);
                return;
            }
            //企业富信不可进行补发
            if (lfmttask.getMsType().intValue() == 21) {
                result = "qyfxnotsupport";
                EmpExecutionContext.info("企业富信不可进行补发！taskid=" + oldtaskId);
                return;
            }
            result = "success";
        } catch (Exception e) {
            result = "error";
            EmpExecutionContext.error(e, "下行记录补发前的验证失败！taskid=" + oldtaskId);
        } finally {
            response.getWriter().print(result);
        }
    }

    /**
     * 手工补发，适用于EMP WEB界面发送的短信批次。接口发送的，此方法不适用。
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void resendBatchSms(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result = "";
        String oldtaskId = "";
        try {
            oldtaskId = request.getParameter("taskid");
            LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
            conMap.put("taskId", oldtaskId);
            //根据mtid获取任务信息
            LfMttask lfmttask = new BaseBiz().findListByCondition(LfMttask.class, conMap, null).get(0);
            if (lfmttask.getMsgType().intValue() == 1) {
                //调用相同内容批量
                result = ssm_sendBatch(request, response);
            } else if (lfmttask.getMsgType().intValue() == 2 || lfmttask.getMsgType().intValue() == 3) {
                //调用不同内容批量
                result = dsm_sendBatch(request, response);
            } else {
                //批量类型不正确
                result = "error";
                EmpExecutionContext.error("下行记录补发，批量短信msgtype不正确，msgtype:" + lfmttask.getMsgType() + ",taskid:" + oldtaskId);
            }
        } catch (Exception e) {
            result = "error";
            EmpExecutionContext.error(e, "系统下行短信补发失败！");
        } finally {
            response.getWriter().print(result);
        }
    }

    /**
     * 相同内容发送处理方法
     *
     * @param request
     * @param response
     * @return
     */
    public String ssm_sendBatch(HttpServletRequest request, HttpServletResponse response) {
        SuperOpLog spLog = new SuperOpLog();//操作日志
        LfSysuser lfSysuser = null;
        String result = ""; //重发状态
        long startTime = System.currentTimeMillis();
        //发送账号
        String spUser = "";
        // 内容
        String msg = "";
        //操作员id
        String strlguserid = null;
        //操作员id
        Long lguserid = null;
        //企业编码
        String lgcorpcode = "";
        //业务类型
        String busCode = "";
        // 获取当前登录操作员id
        //漏洞修复 session里获取操作员信息
        strlguserid = SysuserUtil.strLguserid(request);
        //获取页面传过来的lf_mttask表中的taskid
        String oldtaskId = request.getParameter("taskid");

        //产生taskId
        String taskId = String.valueOf(commonBiz.getAvailableTaskId());
        // 预览参数传递变量类
        PreviewParams preParams = new PreviewParams();
        String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
        try {
            if (strlguserid == null || strlguserid.trim().length() == 0
                    || "null".equals(strlguserid.trim()) || "undefined".equals(strlguserid.trim())) {
                EmpExecutionContext.error(DESC + "相同内容，获取操作员id异常。userid:" + strlguserid + "，errCode：" + IErrorCode.V10001);
                EmpExecutionContext.logRequestUrl(request, "后台请求");
                ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
                String desc = info.getErrorInfo(IErrorCode.V10001);
                result = desc;
                return result;
            }
            lguserid = Long.valueOf(strlguserid);

            Map<String, String> btnMap = (Map<String, String>) request.getSession(false)
                    .getAttribute("btnMap");
            // 是否有预览号码权限.
            if (btnMap.get(StaticValue.PHONE_LOOK_CODE) != null) {
                //号码是否可见，0：不可见，1：可见
                preParams.setIshidephone(1);
            }

            // 获取运营商号码段
            String[] haoduan = msgConfigBiz.getHaoduan();
            if (haoduan == null) {
                EmpExecutionContext.error(DESC + "相同内容，获取运营商号码段异常。userId：" + strlguserid + "，errCode:" + IErrorCode.V10002);
                throw new EMPException(IErrorCode.V10002);
            }
            //设置文件名参数
            String[] fileNameparam = {taskId};
            // 获取号码文件url
            String[] phoneFilePath = txtFileUtil.getSaveFileUrl(lguserid, fileNameparam);
            // 数组长度是确定的，还判断个啥
//				if (phoneFilePath == null || phoneFilePath.length < 5)
            if (phoneFilePath == null) {
                EmpExecutionContext.error(DESC + "相同内容，获取发送文件路径失败。userId：" + strlguserid + "，errCode:" + IErrorCode.V10013);
                throw new EMPException(IErrorCode.V10013);
            } else {
                //判断文件是否存在，存在则返回
                if (new File(phoneFilePath[0]).exists()) {
                    EmpExecutionContext.error(DESC + "相同内容，获取发送文件路径失败，文件路径已存在，文件路径：" + phoneFilePath[0] + "，userid:" + strlguserid + "，errCode：" + IErrorCode.V10013);
                    throw new EMPException(IErrorCode.V10013);
                }
                preParams.setPhoneFilePath(phoneFilePath);
            }

            LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
            conMap.put("taskId", oldtaskId);
            //根据mtid获取任务信息
            LfMttask lfmttask = new BaseBiz().findListByCondition(LfMttask.class, conMap, null).get(0);

            // 获取发送账号
            spUser = request.getParameter("spUser");
            // 获取发送短信内容
            msg = lfmttask.getMsg();
            // 获取业务编码
            busCode = lfmttask.getBusCode();
            //登录操作员信息
            lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //登录操作员信息为空
            if (lfSysuser == null) {
                EmpExecutionContext.error(DESC + "相同内容，从session获取登录操作员信息异常。lfSysuser为null，errCode：" + IErrorCode.V10001);
                ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
                String desc = info.getErrorInfo(IErrorCode.V10001);
                result = desc;
                return result;
            }

            // 获取企业编码
            lgcorpcode = lfSysuser.getCorpCode();

            //操作员、企业编码、SP账号检查
            boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfSysuser, lgcorpcode, spUser, null);
            if (!checkFlag) {
                EmpExecutionContext.error(DESC + "相同内容，检查操作员、企业编码、发送账号不通过，taskid:" + taskId
                        + "，corpCode:" + lgcorpcode
                        + "，userid：" + strlguserid
                        + "，spUser：" + spUser
                        + "，errCode:" + IErrorCode.B20007);
                ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
                String desc = info.getErrorInfo(IErrorCode.B20007);
                result = desc;
                return result;
            }
            //关键字检查
            if (msg != null && msg.trim().length() > 0) {
                if (lgcorpcode != null && lgcorpcode.trim().length() > 0) {
                    //包含的关键字，为""时内容无关键字
                    String words = keyWordAtom.checkText(msg, lgcorpcode);
                    //包含关键字
                    if (!"".equals(words)) {
                        EmpExecutionContext.error(DESC + "相同内容，关键字检查未通过，发送内容包含违禁词组:'" + words + "'，taskid:" + taskId
                                + "，corpCode:" + lgcorpcode
                                + "，userid：" + strlguserid
                                + "，errCode:" + IErrorCode.V10016);
                        ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
                        String desc = info.getErrorInfo(IErrorCode.V10016);
                        result = desc + words;
                        return result;
                    }
                } else {
                    EmpExecutionContext.error(DESC + "相同内容，获取企业编码异常，lgcorpcode:" + lgcorpcode + "，taskid:" + taskId + "，userid：" + strlguserid + "，errCode:" + IErrorCode.V10001);
                    ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
                    String desc = info.getErrorInfo(IErrorCode.V10001);
                    result = desc;
                    return result;
                }
            } else {
                EmpExecutionContext.error(DESC + "相同内容，获取短信内容异常，短信内容为空。taskid:" + taskId
                        + "，corpCode:" + lgcorpcode
                        + "，userid：" + strlguserid
                        + "，errCode:" + IErrorCode.V10001);
                EmpExecutionContext.logRequestUrl(request, "后台请求");
                ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
                String desc = info.getErrorInfo(IErrorCode.V10001);
                result = desc;
                return result;
            }


            HashSet<String> sendPhoneSet = new HashSet<String>();
            //获取发送的手机号码
            String phones = request.getParameter("phones");
            String[] phoneArr = phones.split(",");
            //手机号码过滤重复。用Set集合的属性。
            for (int i = 0; i < phoneArr.length; i++) {
                sendPhoneSet.add(phoneArr[i]);
            }

            // 解析文本文件流，验证号码合法性、过滤黑名单、过滤重号、生成有效号码文件和无效号码文件
            resendMtRecordBiz.parsePhone(sendPhoneSet, preParams, haoduan,
                    lguserid.toString(), lgcorpcode, busCode, request);
            //TODO：考虑在前面就处理好范围超出判断
            //判断有效号码数是否超出范围
            if (preParams.getEffCount() > StaticValue.MAX_PHONE_NUM) {
                //删除前面生成的有效号码和无效号码文件
                for (int i = 0; i < phoneFilePath.length; i++) {
                    txtFileUtil.deleteFile(phoneFilePath[i]);
                }
                //文件内有效号码大于500万
                result = "overstep";
                EmpExecutionContext.error(DESC + "相同内容，有效号码数超出过最大限制:" + StaticValue.MAX_PHONE_NUM + "，无法进行发送，taskid:" + taskId + "，corpCode:" + lgcorpcode + "，userid：" + strlguserid);
                return result;
            }

            //处理预览号码内容，有值则去掉最后一个分号
            String previewPhone = preParams.getPreviewPhone();
            if (previewPhone.lastIndexOf(";") > 0) {
                previewPhone = previewPhone.substring(0, previewPhone
                        .lastIndexOf(";"));
                preParams.setPreviewPhone(previewPhone);
            }

            //没有提交号码，返回页面提示
            if (preParams.getSubCount() == 0) {
                result = "noPhone";
                EmpExecutionContext.error(DESC + "相同内容，没有提交号码，无法进行发送。taskid:" + taskId + "，corpCode:" + lgcorpcode + "，userid：" + strlguserid);
                return result;
            }

            //有效号码数为0
            if(preParams.getEffCount()==0)
            {
            	 //补发失败，补发信息触发关键字或为黑名单号码！
            	 result = "noEffPhone";
            	 EmpExecutionContext.error(DESC + "相同内容，没有有效号码，无法进行发送。taskid:" + taskId + "，corpCode:" + lgcorpcode + "，userid：" + strlguserid);
                 return result;
            }
            
            // 机构余额
            Long depFeeCount = -1L;
            //预发送条数
            int preSendCount = -1;
            //替换短信内的特殊字符
            msg = smsBiz.smsContentFilter(msg);
            //获取预发送条数
            preSendCount = smsBiz.getCountSmsSend(spUser, msg, preParams.getOprValidPhone());
            if (preSendCount < 0) {
                EmpExecutionContext.error(DESC + "相同内容，获取预发送条数异常。errCode：" + IErrorCode.B20006 + "，taskid:" + taskId + "，corpCode:" + lgcorpcode + "，userid：" + strlguserid);
                ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
                String desc = info.getErrorInfo(IErrorCode.B20006);
                result = desc;
                return result;
            }

            if (preSendCount == 0) {
                result = "noPhone";
                EmpExecutionContext.error(DESC + "相同内容，没有可发送的有效号码，无法进行发送。taskid:"
                        + taskId
                        + "，corpCode:"
                        + lgcorpcode
                        + "，userid："
                        + strlguserid);
                return result;
            }

            //判断是否需要机构计费
            Map<String, String> infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
            // 如果启用了计费则为true;未启用则为false;
            boolean isCharge = false;
            if (infoMap == null) {
                new CommonBiz().setSendInfo(request, response, lgcorpcode, lguserid);
                infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
            }
            if ("true".equals(infoMap.get("feeFlag"))) {
                isCharge = true;
            }

            //机构、SP账号检查余额标识，只要有其一个余额不足，则为false，之后的扣费将不再执行
            boolean isAllCharge = true;
            //启用计费，查询机构余额
            if (isCharge) {
                //获取机构余额
                depFeeCount = balancebiz.getAllowSmsAmount(lfSysuser);
                if (depFeeCount == null || depFeeCount < 0) {
                    EmpExecutionContext.error("相同内容，获取机构余额异常。errCode：" + IErrorCode.B20024
                            + "，taskid:" + taskId
                            + "，corpCode:" + lgcorpcode
                            + "，depFeeCount:" + depFeeCount
                            + "，userid：" + strlguserid);
                    throw new EMPException(IErrorCode.B20024);
                }
                //机构余额小于发送条数
                if (depFeeCount - preSendCount < 0) {
                    //设置检查标识为false余额不足
                    isAllCharge = false;
                    EmpExecutionContext.error(DESC + "相同内容，机构余额小于发送条数，taskid:" + taskId
                            + "，corpCode:" + lgcorpcode
                            + "，userid：" + strlguserid
                            + "，depFeeCount:" + depFeeCount
                            + "，preSendCount" + preSendCount);
                    //机构余额不足
                    result = "depfee:-2";
                    return result;
                }

            }
            //SP账号类型,1:预付费;2:后付费
            Long feeFlag = 2L;
            // SP账号余额
            Long spUserFeeCount = -1L;
            //机构余额检查正常或无机构扣费
            if (isAllCharge) {
                //获取SP账号类型
                feeFlag = balancebiz.getSpUserFeeFlag(spUser, 1);
                if (feeFlag == null || feeFlag < 0) {
                    EmpExecutionContext.error(DESC + "相同内容，获取SP账号计费类型异常。errCode：" + IErrorCode.B20044
                            + "，taskid:" + taskId
                            + "，corpCode:" + lgcorpcode
                            + "，userid:" + strlguserid
                            + "，spUser:" + spUser
                            + "，feeFlag:" + feeFlag);
                    throw new EMPException(IErrorCode.B20044);
                }
                //预付费账号，查询SP账号余额
                if (feeFlag == 1) {
                    //获取SP账号余额
                    spUserFeeCount = balancebiz.getSpUserAmount(spUser);
                    if (spUserFeeCount == null || spUserFeeCount < 0) {
                        EmpExecutionContext.error(DESC + "相同内容，获取SP账号余额异常。errCode：" + IErrorCode.B20045
                                + "，taskid:" + taskId
                                + "，corpCode:" + lgcorpcode
                                + "，userid：" + strlguserid
                                + "，spUser:" + spUser
                                + "，spUserFeeCount:" + spUserFeeCount);
                        throw new EMPException(IErrorCode.B20045);
                    }
                    //SP账号余额小于发送条数
                    if (spUserFeeCount - preSendCount < 0) {
                        //设置检查标识为false余额不足
                        isAllCharge = false;
                        EmpExecutionContext.error(DESC + "相同内容，SP账号余额小于发送条数。，taskid:" + taskId
                                + "，corpCode:" + lgcorpcode
                                + "，userid：" + strlguserid
                                + "，spUserFeeCount:" + spUserFeeCount
                                + "，preSendCount" + preSendCount
                                + "，spUser:" + spUser);
                        //SP账号余额不足
                        result = "spuserfee:-2";
                        return result;
                    }
                }
            }

            String spFeeResult = "koufeiSuccess";
            //机构和SP账号余额检查都通过
            if (isAllCharge) {
                //检查运营商余额
                spFeeResult = balancebiz.checkGwFee(spUser, preSendCount, lgcorpcode, 1);
            }

            //运营商余额检查不通过，提示页面
            if ("nogwfee".equals(spFeeResult) || "lessgwfee".equals(spFeeResult) || "feefail".equals(spFeeResult)) {
                result = spFeeResult;
                EmpExecutionContext.info(DESC + "相同内容补发，运营商余额检查不通过！");
                return result;
            }

            //直接发送
            result = resendMtRecordBiz.send(request, response, lfmttask, taskId, preParams, preSendCount);

            //操作日志信息
            StringBuffer opContent = new StringBuffer().append(DESC).append(sdf.format(startTime)).append("，耗时：")
                    .append((System.currentTimeMillis() - startTime)).append("ms");

            opContent.append("，提交总数：").append(preParams.getSubCount())
                    .append("，有效数：").append(preParams.getEffCount())
                    .append("，短信条数：").append(preSendCount)
                    .append("，taskId：").append(taskId)
                    .append("，spUser:").append(spUser);
            //操作员名称
            String userName = " ";
            if (lfSysuser != null) {
                userName = lfSysuser.getUserName();
            }
            EmpExecutionContext.info(DESC + "相同内容群发", lgcorpcode, strlguserid, userName, opContent.toString(), "OTHER");
        } catch (EMPException empex) {
            ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
            //获取自定义异常编码
            String message = empex.getMessage();
            //获取自定义异常提示信息
            String desc = "";
            //文件不存在
            if (message.indexOf("B20042") == 0) {
                desc = String.format(info.getErrorInfo(IErrorCode.B20042), message.substring(6));
            } else {
                desc = info.getErrorInfo(message);
            }
            //拼接前台自定义异常标识
            result = desc;
            EmpExecutionContext.error(empex, "系统下行记录短信补发失败！lguserid:" + lguserid + ",lgcorpcode:" + lgcorpcode);
            EmpExecutionContext.logRequestUrl(request, "后台请求");
        } catch (Exception e) {
            result = "error";
            String userName = "";
            String corpCode = "";
            if(null != lfSysuser){
            	userName = lfSysuser.getUserName();
            	corpCode = lfSysuser.getCorpCode();
            }
            spLog.logFailureString(userName, StaticValue.STATISTICS, SDBF, "操作员：" + userName + "手动批量补发短信失败。", e, corpCode);
            EmpExecutionContext.error(e, "系统下行记录短信补发失败！lguserid:" + lguserid + ",lgcorpcode:" + lgcorpcode);
        }
        return result;
    }


    /**
     * 不同内容发送处理方法
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public String dsm_sendBatch(HttpServletRequest request, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        // 返回结果
        String result = "";
        // 发送账号
        String spUser = "";
        // 内容
        String msg = "";
        // 操作员id
        String strlguserid = null;
        // 操作员id
        Long lguserid = null;
        // 企业编码
        String lgcorpcode = "";
        // 短信模板参数个数
        int tempParamCount = 0;
        // 发送类型（不同内容发送，动态模板发送）
        String sendType = "";
        // 业务类型
        String busCode = "";
        // 获取当前登录操作员id
        //strlguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        strlguserid = SysuserUtil.strLguserid(request);
        //获取页面传过来的lf_mttask表中的taskid
        String oldtaskId = request.getParameter("taskid");

        //漏洞修复 session里获取操作员信息
        if (request.getSession(false) == null || request.getSession(false).getAttribute("loginSysuser") == null) {
            strlguserid = request.getParameter("lguserid");
        } else {
            LfSysuser sysUser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            strlguserid = String.valueOf(sysUser.getUserId());
        }

        //任务ID
        String taskId = String.valueOf(commonBiz.getAvailableTaskId());
        // 预览参数传递变量类
        PreviewParams preParams = new PreviewParams();
        String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
        try {
            if (strlguserid == null || strlguserid.trim().length() == 0) {
                EmpExecutionContext.error(DESC +"不同内容预览，获取操作员id为null。errCode：" + IErrorCode.V10001 + "，userid:" + strlguserid);
                ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
                String desc = info.getErrorInfo(IErrorCode.V10001);
                result = desc;
                return result;
            }
            lguserid = Long.valueOf(strlguserid);

            Map<String, String> btnMap = (Map<String, String>) request.getSession(false).getAttribute("btnMap");
            // 是否有预览号码权限.
            if (btnMap.get(StaticValue.PHONE_LOOK_CODE) != null) {
                // 号码是否可见，0：不可见，1：可见
                preParams.setIshidephone(1);
            }

            // 获取运营商号码段
            String[] haoduan = msgConfigBiz.getHaoduan();
            if (haoduan == null) {
                EmpExecutionContext.error(DESC +"不同内容预览，获取运营商号码段异常。userId：" + strlguserid + "，errCode:" + IErrorCode.V10002);
                throw new EMPException(IErrorCode.V10002);
            }

            //设置文件名参数
            String[] fileNameparam = {taskId};
            // 获取号码文件url
            String[] phoneFilePath = txtFileUtil.getSaveFileUrl(lguserid, fileNameparam);
//			if(phoneFilePath == null || phoneFilePath.length < 5)
            if (phoneFilePath == null) {
                EmpExecutionContext.error(DESC +"不同内容预览，获取发送文件路径失败。userId：" + strlguserid + "，errCode:" + IErrorCode.V10013);
                throw new EMPException(IErrorCode.V10013);
            } else {
                //号码文件是否已存在
                File file = new File(phoneFilePath[0]);
                //判断文件是否存在，存在则返回
                if (file.exists()) {
                    EmpExecutionContext.error(DESC +"不同内容预览，获取发送文件路径失败，文件路径已存在，文件路径：" + phoneFilePath[0] + "，userid:" + strlguserid + "，errCode：" + IErrorCode.V10013);
                    throw new EMPException(IErrorCode.V10013);
                }
                preParams.setPhoneFilePath(phoneFilePath);
            }


            LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
            conMap.put("taskId", oldtaskId);
            //根据mtid获取任务信息
            LfMttask lfmttask = new BaseBiz().findListByCondition(LfMttask.class, conMap, null).get(0);

            // 获取发送账号
            spUser = request.getParameter("spUser");
            String cs = com.montnets.emp.i18n.util.MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_223", request);
            if (lfmttask.getMsg() != null) {
                // 获取发送短信内容
                msg = lfmttask.getMsg().replaceAll("\\{#" + cs + "([1-9][0-9]*)#\\}", "#p_$1#");
            } else {
                msg = "";
            }
            // 发送类型（短信模板发送2，文件内容发送3）
            sendType = String.valueOf(lfmttask.getMsgType());
            
            //modify by tanglili 2019-03-15
            //如果是网讯，则发送类型统一是3
            if(lfmttask.getMsType().intValue()==6)
            {
            	sendType="3";
            }
            
            // 获取业务编码
            busCode = lfmttask.getBusCode();
            //登录操作员信息
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //登录操作员信息为空
            if (lfSysuser == null) {
                EmpExecutionContext.error(DESC +"不同内容预览，从session获取登录操作员信息异常。lfSysuser为null，errCode：" + IErrorCode.V10001);
                ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
                String desc = info.getErrorInfo(IErrorCode.V10001);
                result = desc;
                return result;
            }

            // 获取企业编码
            lgcorpcode = lfSysuser.getCorpCode();

            //操作员、企业编码、SP账号检查
            boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfSysuser, lgcorpcode, spUser, null);
            if (!checkFlag) {
                EmpExecutionContext.error(DESC +"不同内容预览，检查操作员、企业编码、发送账号不通过，taskid:" + taskId
                        + "，corpCode:" + lgcorpcode
                        + "，userid：" + strlguserid
                        + "，spUser：" + spUser
                        + "，errCode:" + IErrorCode.B20007);
                ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
                String desc = info.getErrorInfo(IErrorCode.B20007);
                result = desc;
                return result;
            }
            // 动态模板发送时
            if ("2".equals(sendType)) {
                //关键字检查
                if (msg != null && msg.trim().length() > 0) {
                    if (lgcorpcode != null && lgcorpcode.trim().length() > 0) {
                        //包含的关键字，为""时内容无关键字
                        String words = keyWordAtom.checkText(msg, lgcorpcode);
                        //包含关键字
                        if (!"".equals(words)) {
                            EmpExecutionContext.error(DESC +"不同内容预览，关键字检查未通过，发送内容包含违禁词组:'" + words + "'，taskid:" + taskId
                                    + "，corpCode:" + lgcorpcode
                                    + "，userid：" + strlguserid
                                    + "，errCode:" + IErrorCode.V10016);
                            ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
                            String desc = info.getErrorInfo(IErrorCode.V10016);
                            result = desc + words;
                            return result;
                        }
                    } else {
                        EmpExecutionContext.error(DESC +"不同内容预览，获取企业编码异常，lgcorpcode:" + lgcorpcode + "，taskid:" + taskId + "，userid：" + strlguserid + "，errCode:" + IErrorCode.V10001);
                        ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
                        String desc = info.getErrorInfo(IErrorCode.V10001);
                        result = desc;
                        return result;
                    }
                } else {
                    EmpExecutionContext.error(DESC +"不同内容预览，获取短信内容异常，获取短信内容为空。taskid:" + taskId
                            + "，corpCode:" + lgcorpcode
                            + "，userid：" + strlguserid
                            + "，errCode:" + IErrorCode.V10001);
                    EmpExecutionContext.logRequestUrl(request, "后台请求");
                    ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
                    String desc = info.getErrorInfo(IErrorCode.V10001);
                    result = desc;
                    return result;
                }
            }

            preParams.setSendType(Integer.parseInt(sendType));

            //获取平台流水号
            String ptmsgIds = request.getParameter("ptmsgIds");
            String oldptmsgIds = ptmsgIds;
            ptmsgIds = resendMtRecordBiz.handlePtmsgIds(ptmsgIds);
            if ("error".equals(ptmsgIds)) {
                result = ptmsgIds;
                return result;
            }
            EmpExecutionContext.info(DESC +"不同内容短信补发，传入的ptmsgid集合为:" + oldptmsgIds + ",过滤重复的ptmsgid后的集合为:" + ptmsgIds);

            //查询拆分后的第一条短信
            List<DynaBean> mttaskList = resendMtRecordBiz.findMtTaskByPtmsgids(ptmsgIds);
            if (mttaskList == null || mttaskList.size() == 0) {
                result = "error";
                EmpExecutionContext.error(DESC +"查询拆分后的第一条短信失败！taskid=" + oldtaskId);
                return result;
            }

            //根据长短信第一条查询长短信所有短信拼接在一起
            List<String> phoneAndContentList = resendMtRecordBiz.findPhoneAndContentByFirstContent(mttaskList);
            if (phoneAndContentList == null || phoneAndContentList.size() == 0) {
                result = "error";
                return result;
            }

            //解析手机号和短信内容集合，验证号码合法性、过滤黑名单、过滤重号、过滤关键字、拼接动态内容、生成有效号码文件和无效号码文件
            resendMtRecordBiz.parsePhoneAndContent(phoneAndContentList, preParams, haoduan, lguserid.toString(), lgcorpcode, busCode, msg, spUser, request);

            // TODO：考虑在前面就处理好范围超出判断
            // 判断有效号码数是否超出范围
            if (preParams.getEffCount() > StaticValue.MAX_PHONE_NUM) {
                // 删除前面生成的有效号码和无效号码文件
                for (int i = 0; i < phoneFilePath.length; i++) {
                    txtFileUtil.deleteFile(phoneFilePath[i]);
                }
                // 文件内有效号码大于500万
                result = "overstep";
                EmpExecutionContext.error(DESC +"不同内容预览，有效号码数超过" + StaticValue.MAX_PHONE_NUM + "，无法进行发送，taskid:" + taskId + "，corpCode:" + lgcorpcode + "，userid：" + strlguserid);
                return result;
            }

            // 没有发送号码，返回页面提示
            if (preParams.getSubCount() == 0) {
                result = "noPhone";
                EmpExecutionContext.error(DESC +"不同内容预览，没有提交号码，无法进行发送。taskid:" + taskId + "，corpCode:" + lgcorpcode + "，userid：" + strlguserid);
                return result;
            }

            //有效号码数为0
            if(preParams.getEffCount()==0)
            {
            	 //补发失败，补发信息触发关键字或为黑名单号码！
            	 result = "noEffPhone";
            	 EmpExecutionContext.error(DESC +"不同内容预览，没有有效号码，无法进行发送。taskid:" + taskId + "，corpCode:" + lgcorpcode + "，userid：" + strlguserid);
                 return result;
            }
            
            // 机构余额
            Long depFeeCount = 0L;
            // 预发送条数
            int preSendCount = 0;

            // 获取预发送条数
            preSendCount = smsBiz.countAllOprSmsNumByDsm(spUser, msg, Integer.parseInt(sendType), phoneFilePath[1], null);

            //判断是否需要机构计费
            Map<String, String> infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
            // 如果启用了计费则为true;未启用则为false;
            boolean isCharge = false;
            if (infoMap == null) {
                new CommonBiz().setSendInfo(request, response, lgcorpcode, lguserid);
                infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
            }
            if ("true".equals(infoMap.get("feeFlag"))) {
                isCharge = true;
            }

            //机构、SP账号检查余额标识，只要有其一个余额不足，则为false，之后的扣费将不再执行
            boolean isAllCharge = true;
            //启用计费，查询机构余额
            if (isCharge) {
                //获取机构余额
                depFeeCount = balancebiz.getAllowSmsAmount(lfSysuser);
                if (depFeeCount == null) {
                    EmpExecutionContext.error(DESC +"不同内容预览，获取机构余额异常。errCode：" + IErrorCode.B20024
                            + "，taskid:" + taskId
                            + "，corpCode:" + lgcorpcode
                            + "，userid：" + strlguserid
                            + "，depFeeCount:" + depFeeCount);
                    throw new EMPException(IErrorCode.B20024);
                }
                //机构余额小于发送条数
                if (depFeeCount - preSendCount < 0) {
                    //设置检查标识为false余额不足
                    isAllCharge = false;
                    EmpExecutionContext.error(DESC +"不同内容预览，机构余额小于发送条数，taskid:" + taskId
                            + "，corpCode:" + lgcorpcode
                            + "，userid：" + strlguserid
                            + "，depFeeCount:" + depFeeCount
                            + "，preSendCount" + preSendCount);
                    //机构余额不足
                    result = "depfee:-2";
                    return result;
                }
            }
            //SP账号类型,1:预付费;2:后付费
            Long feeFlag = 2L;
            // SP账号余额
            Long spUserFeeCount = -1L;
            //机构余额检查正常或无机构扣费
            if (isAllCharge) {
                //获取SP账号类型
                feeFlag = balancebiz.getSpUserFeeFlag(spUser, 1);
                if (feeFlag == null || feeFlag < 0) {
                    EmpExecutionContext.error(DESC +"不同内容预览，获取SP账号计费类型异常。errCode：" + IErrorCode.B20044
                            + "，taskid:" + taskId
                            + "，corpCode:" + lgcorpcode
                            + "，userid：" + strlguserid
                            + "，spUser:" + spUser
                            + "，feeFlag:" + feeFlag);
                    throw new EMPException(IErrorCode.B20044);
                }
                //预付费账号，查询SP账号余额
                if (feeFlag == 1) {
                    //获取SP账号余额
                    spUserFeeCount = balancebiz.getSpUserAmount(spUser);
                    if (spUserFeeCount == null || spUserFeeCount < 0) {
                        EmpExecutionContext.error(DESC +"不同内容预览，获取SP账号余额异常。errCode：" + IErrorCode.B20045
                                + "，taskid:" + taskId
                                + "，corpCode:" + lgcorpcode
                                + "，userid：" + strlguserid
                                + "，spUser:" + spUser
                                + "，spUserFeeCount:" + spUserFeeCount);
                        throw new EMPException(IErrorCode.B20045);
                    }
                    //SP账号余额小于发送条数
                    if (spUserFeeCount - preSendCount < 0) {
                        //设置检查标识为false余额不足
                        isAllCharge = false;
                        EmpExecutionContext.error(DESC +"相同内容预览，SP账号余额小于发送条数。，taskid:" + taskId
                                + "，corpCode:" + lgcorpcode
                                + "，userid：" + strlguserid
                                + "，spUserFeeCount:" + spUserFeeCount
                                + "，preSendCount" + preSendCount
                                + "，spUser:" + spUser);
                        //SP账号余额不足
                        result = "spuserfee:-2";
                        return result;
                    }
                }

            }
            String spFeeResult = "koufeiSuccess";
            //机构和SP账号余额检查都通过
            if (isAllCharge) {
                // 检查运营商余额
                spFeeResult = balancebiz.checkGwFee(spUser, preSendCount, lgcorpcode, 1);
            }

            //运营商余额检查不通过，提示页面
            if ("nogwfee".equals(spFeeResult) || "lessgwfee".equals(spFeeResult) || "feefail".equals(spFeeResult)) {
                result = spFeeResult;
                EmpExecutionContext.info(DESC + "不同内容补发，运营商余额检查不通过！");
                return result;
            }

            //直接发送
            result = resendMtRecordBiz.send(request, response, lfmttask, taskId, preParams, preSendCount);

            //操作日志信息
            StringBuffer opContent = new StringBuffer().append("预览S：").append(sdf.format(startTime)).append("，耗时：")
                    .append((System.currentTimeMillis() - startTime)).append("ms");
            opContent.append("，提交总数：").append(preParams.getSubCount())
                    .append("，有效数：").append(preParams.getEffCount())
                    .append("，发送条数：").append(preSendCount)
                    .append("，taskId：").append(taskId)
                    .append("，spUser:").append(spUser);
            //操作员名称
            String userName = " ";
            if (lfSysuser != null) {
                userName = lfSysuser.getUserName();
            }
            EmpExecutionContext.info(DESC +"不同内容群发", lgcorpcode, strlguserid, userName, opContent.toString(), "OTHER");
        } catch (EMPException empex) {
            ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
            //获取自定义异常编码
            String message = empex.getMessage();
            //获取自定义异常提示信息
            String desc = "";
            //excel文件加密异常
            if (message.indexOf("B20038") == 0) {
                desc = String.format(info.getErrorInfo(IErrorCode.B20038), message.substring(6));
            } else {
                desc = info.getErrorInfo(message);
            }
            // 拼接前台自定义异常标识
            result = desc;
            EmpExecutionContext.error(empex, lguserid, lgcorpcode);
            EmpExecutionContext.logRequestUrl(request, "后台请求");
        } catch (Exception ex) {
            result = "error";
            EmpExecutionContext.error(ex, lguserid, lgcorpcode);
            EmpExecutionContext.logRequestUrl(request, "后台请求");
        }
        return result;
    }

}
