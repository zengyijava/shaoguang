package com.montnets.emp.shorturl.report.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.shorturl.report.biz.SurlReportBiz;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Surl_sendDetailReportSvt extends BaseServlet{
    private final SurlReportBiz surlReportBiz = new SurlReportBiz();
    private static final String MODNAME = "企业短链";
    private static final String OPNAME = "发送明细查询";
    private static final String empRoot = "shorturl";
    private static final String base = "/report";

    /**
     * 短链 发送明细(系统下行记录)查询
     * @param request
     * @param response
     * @throws Exception
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String requestPath = request.getRequestURI();
        String actionPath=requestPath.substring(requestPath.lastIndexOf("/")+1, requestPath.length());
        //记录日志 起始ms数
        long startTime=System.currentTimeMillis();

        // 记录类型
        String recordType = request.getParameter("recordType");
        //任务批次
        String taskid = request.getParameter("taskid");
        //手机号码
        String phone=request.getParameter("phone");
        //业务类型
        String buscode=request.getParameter("busCode");
        //SP账号
        String userid=request.getParameter("userid");
        //状态码
        String mterrorcode = request.getParameter("mterrorcode");
        //通道号
        String spgate=request.getParameter("spgate");
        //自定义流水号
        String usermsgid = request.getParameter("usermsgid");
        //开始时间
        String sendtime=request.getParameter("sendtime");
        //结束时间
        String recvtime=request.getParameter("recvtime");
        // 企业编码
        String lgcorpcode = "";
        // 当前登录操作员id
        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);

        // 当前登录操作员
        String lgusername = request.getParameter("lgusername");
        //运营应商
        String spisuncm=request.getParameter("spisuncm");

        //分页对象
        PageInfo pageInfo = new PageInfo();
        // 是否第一次访问
        boolean isFirstEnter = pageSet(pageInfo, request);

        long count = 0L;
        // 查询条件
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        try {
            //获取登录sysuser
            LfSysuser loginSysuser = surlReportBiz.getCurrenUser(request);
            if(loginSysuser == null){
                EmpExecutionContext.error(MODNAME + "," + OPNAME + ",从session中获取当前登录对象出现异常");
                return;
            }
            lgcorpcode= loginSysuser.getCorpCode();
            //判断企业编码获取
            if(lgcorpcode==null||"".equals(lgcorpcode)){
                EmpExecutionContext.error(MODNAME + "," + OPNAME + ",session中获取企业编码出现异常");
                return;
            }
            //判断SP账号是否是属于本企业的
            if(userid != null && !"".equals(userid.trim())) {
                //多企业才处理
                if(StaticValue.getCORPTYPE() ==1 && !"100000".equals(lgcorpcode)){
                    boolean checkFlag=new CheckUtil().checkSysuserInCorp(loginSysuser, lgcorpcode, userid, null);
                    if(!checkFlag){
                        EmpExecutionContext.error(MODNAME + "," + OPNAME + ",检查操作员，企业编码，发送账号不通过，spuserid："+userid+",corpcode="+lgcorpcode);
                        return;
                    }
                }
            }

            //设置业务类型，页面条件查询下拉框用
            if(!getAndSetBus(lgcorpcode, request)){
                return;
            }

            //设置通道号和发送账号，页面条件查询下拉框用
            if(!getAndSetSp(lgcorpcode, request)){
                return;
            }

            //第一次进入
            if(isFirstEnter) {
                request.getSession(false).removeAttribute("sysMtTaskCon");
                return;
            }

            //企业编码
            conditionMap.put("lgcorpcode", lgcorpcode.trim());
            //查询类型   实时 或 历史
            if(recordType == null || recordType.length() < 1) {
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
            //业务类型
            conditionMap.put("buscode", buscode);
            //开始时间
            conditionMap.put("sendtime", sendtime);
            //结束时间
            conditionMap.put("recvtime", recvtime);
            //状态码
            conditionMap.put("mterrorcode", mterrorcode);
            //自定义流水号
            conditionMap.put("usermsgid", usermsgid);

            //session中获取当前登录操作员对象
            LfSysuser curUser = surlReportBiz.getCurrenUser(request);
            //设置有权限看的操作员编码。不需要考虑权限则是空字符串，目前只有admin和管辖范围是顶级机构不需要考虑权限
            String usercode = surlReportBiz.getPermissionUserCode(curUser);
            if(usercode == null) {
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

            String spuserpri =surlReportBiz.getPermissionSpuserMtpri(curUser);

            if(spuserpri==null){
                //当前操作员编码
                conditionMap.put("spcurcorpcode", "'" + curUser.getCorpCode() + "'");
                //当前操作员id
                conditionMap.put("spcurUserId", curUser.getUserId().toString());
            }else{
                //有权限看的操作员编码
                conditionMap.put("spuserpri", spuserpri);
            }

            //查询下行记录
            List<DynaBean> mtTaskList = getMtTasks(conditionMap, pageInfo, request);

            //没记录就不需要查询分页
            if(mtTaskList == null || mtTaskList.size() == 0) {
                pageInfo.setNeedNewData(2);
                pageInfo.setTotalRec(0);
                pageInfo.setTotalPage(1);
            }
            //错误码说明map，key序号，value为错误码说明
            Map<String,String> errCodeDesMap = surlReportBiz.getErrCodeDisMap(mtTaskList, lgcorpcode);
            request.setAttribute("errCodeDesMap", errCodeDesMap);
            request.setAttribute("sysMtTaskList", mtTaskList);

            //设置查询条件到session，这个要放在查询方法之后，因为查询方法里还会设置一些查询条件
            request.getSession(false).setAttribute("sysMtTaskCon", conditionMap);

            //获取总数，用于记录日志
            count = mtTaskList==null? 0: mtTaskList.size();
        }
        catch (Exception e) {
            // 异常处理
            EmpExecutionContext.error(e, "系统下行记录查询，异常。");
            request.setAttribute("findresult", "-1");
        } finally {
            // 是否第一次进入标示
            request.setAttribute("isFirstEnter", isFirstEnter);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("actionPath", actionPath);
            String conditionstr = "userid=" + conditionMap.get("userid") + ",SP账号=" + conditionMap.get("spUsers")
                    + ",通道号码=" + conditionMap.get("spgate") + ",业务类型=" + conditionMap.get("buscode")
                    + ",运营商=" + conditionMap.get("spisuncm") + ",手机号码=" + conditionMap.get("phone")
                    + ",任务Id=" + conditionMap.get("taskid") + ",发送时间=" + conditionMap.get("sendtime")
                    + ",结束时间=" + conditionMap.get("recvtime") + ",pageindex=" + pageInfo.getPageIndex();
            SimpleDateFormat sdf_hms = new SimpleDateFormat("HH:mm:ss");
            //开始时间
            String starthms= sdf_hms.format(startTime);
            //菜单初次点入判断
            if(recordType == null){
                recordType = "(菜单初次点入)";
            }
            String opContent = OPNAME + "," + recordType + " totalcount:" + count + "条 ,开始："+starthms+",耗时:"+(System.currentTimeMillis()-startTime)+"ms,条件:"+conditionstr;
            EmpExecutionContext.info(MODNAME, lgcorpcode, lguserid, lgusername, opContent, StaticValue.GET);
            request.getRequestDispatcher(empRoot + base + "/surl_sendDetailReport.jsp").forward(request, response);
        }
    }
    /**
     * 获取并设置业务类型到request里
     * @param lgcorpcode 企业编码
     * @param request 请求对象
     * @return 成功返回true
     */
    private boolean getAndSetBus(String lgcorpcode, HttpServletRequest request) {
        try {
            // 查询条件
            LinkedHashMap<String, String> conditionMMap = new LinkedHashMap<String, String>();
            // 获取业务类型
            if(!"100000".equals(lgcorpcode)) {
                // 只显示自定义业务
                conditionMMap.put("corpCode&in", "0," + lgcorpcode);
            }
            else {
                conditionMMap.put("corpCode&not in", "1,2");
            }

            BaseBiz baseBiz	= new BaseBiz();
            List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, null, conditionMMap, null);
            LinkedHashMap<String, String> busMap = new LinkedHashMap<String, String>();
            if(busList != null && busList.size() > 0)
            {
                for (LfBusManager bus : busList)
                {
                    busMap.put(bus.getBusCode(), bus.getBusName());
                }
            }
            request.setAttribute("busMap", busMap);
            return true;
        }
        catch (Exception e) {
            EmpExecutionContext.error(e, "系统下行记录，获取并设置业务类型，异常。");
            return false;
        }
    }
    /**
     * 获取并设置通道和发送账号
     * @param lgcorpcode 企业编码
     * @param request 请求对象
     * @return 成功返回true
     */
    private boolean getAndSetSp(String lgcorpcode, HttpServletRequest request) {
        try {
            SurlReportBiz surlReportBiz = new SurlReportBiz();
            List<DynaBean> spList = surlReportBiz.getSpgateList(lgcorpcode);
            request.setAttribute("spList", spList);

            // 页面SP账号查询条件
            List<String> lfsp = surlReportBiz.getSpUserList(lgcorpcode);
            request.setAttribute("mrUserList", lfsp);
            return true;
        }
        catch (Exception e) {
            EmpExecutionContext.error(e, MODNAME + "," + OPNAME + "，获取并设置通道和发送账号，异常。");
            return false;
        }
    }
    /**
     * 查询下行记录
     * @param conditionMap 查询条件
     * @param pageInfo 分页对象
     * @param request 请求对象
     * @return 返回下行记录对象集合
     */
    private List<DynaBean> getMtTasks(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, HttpServletRequest request) {
        List<DynaBean> mtTaskList;

        if(pageInfo.getNeedNewData() == 1) {
            //查备份表
            conditionMap.put("realTableName", "gw_mt_task_bak");
            mtTaskList = surlReportBiz.getMtRecords(conditionMap, pageInfo);
            return mtTaskList;
        }
        //页面点击分页，则用上次的表查 从session中取上次使用的表名
        LinkedHashMap<String, String> conditionMapPre = (LinkedHashMap<String, String>)request.getSession(false).getAttribute("sysMtTaskCon");
        //拿不到，则使用默认值
        if(conditionMapPre == null) {
            conditionMapPre = new LinkedHashMap<String, String>();
            conditionMapPre.put("realTableName", "gw_mt_task_bak");
        }
        //设置上次使用的表名
        conditionMap.put("realTableName", conditionMapPre.get("realTableName"));
        //查询
        mtTaskList = surlReportBiz.getMtRecords(conditionMap, pageInfo);
        return mtTaskList;
    }
}
