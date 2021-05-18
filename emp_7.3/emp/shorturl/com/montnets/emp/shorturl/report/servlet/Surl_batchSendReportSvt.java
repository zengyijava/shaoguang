package com.montnets.emp.shorturl.report.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.shorturl.report.biz.SurlTaskReportExcelTool;
import com.montnets.emp.shorturl.report.biz.SurlReportBiz;
import com.montnets.emp.shorturl.report.vo.LfMttaskVo;
import com.montnets.emp.shorturl.report.vo.ReplyDetailVo;
import com.montnets.emp.shorturl.report.vo.SendDetailMttaskVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Surl_batchSendReportSvt extends BaseServlet{
    private final BaseBiz baseBiz = new BaseBiz();
    private final SurlReportBiz surlReportBiz = new SurlReportBiz();
    private final String path=new TxtFileUtil().getWebRoot();
    //模板路径
    private final String  excelPath = path + "shorturl/report/file";
    //生成excel的工具类
    private final SurlTaskReportExcelTool et = new SurlTaskReportExcelTool(excelPath);

    public void find(HttpServletRequest request, HttpServletResponse response) {
        String requestPath = request.getRequestURI();
        String actionPath=requestPath.substring(requestPath.lastIndexOf("/")+1, requestPath.length());
        String titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));
        //短信biz
        List<LfMttaskVo> mtVoList = null;
        LfMttaskVo mtVo = new LfMttaskVo();
        long startTime = System.currentTimeMillis();
        PageInfo pageInfo = new PageInfo();
        //当前登录用户的企业编码
        String corpCode = "";
        String  userId = "";
        try {
            //登录操作员信息
            LfSysuser loginSysuser = surlReportBiz.getCurrenUser(request);
            //当前登录用户的企业编码
            corpCode =loginSysuser.getCorpCode();
            userId =String.valueOf(loginSysuser.getUserId());
            if(corpCode==null||"".equals(corpCode.trim())||"undefined".equals(corpCode.trim())||userId==null||"".equals(userId.trim())||"undefined".equals(userId.trim())){
                EmpExecutionContext.error("企业短链，批次发送统计查询获取登录操作员ID和登录操作员企业编码异常！lguserid="+userId+",lgcorpcode="+corpCode+"。改成从Session获取。");
                loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
                userId=String.valueOf(loginSysuser.getUserId());
                corpCode=loginSysuser.getCorpCode();
            }
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //查找绑定的sp账号
            //如果不是10000用户登录，则需要带上企业编码查询
            if(corpCode != null && !corpCode.equals("100000")) {
                conditionMap.put("corpCode", corpCode);
            }
            conditionMap.put("isValidate", "1");
            List<LfSpDepBind> userList = baseBiz.getByCondition(LfSpDepBind.class, conditionMap, null);
            //隶属机构
            String depid = request.getParameter("depid");
            String depNam = request.getParameter("depNam");
            //操作员
            String userid = request.getParameter("userid");
            String userName = request.getParameter("userName");
            //sp账号
            String spUser = request.getParameter("spUser");
            //发送类型
            String taskType= request.getParameter("taskType");
            //任务批次
            String taskID=request.getParameter("taskID");
            //发送主题
            String title = request.getParameter("title");
            //发送状态
            String sendstate=request.getParameter("sendstate");
            //创建时间
            String startSubmitTime = request.getParameter("sendtime");
            //结束时间
            String endSubmitTime = request.getParameter("recvtime");
            //是否包含子机构
            String isContainsSun=request.getParameter("isContainsSun");
            //是否返回操作
            boolean isBack = request.getParameter("isback") != null;

            if(isBack){
                request.setAttribute("spUser", spUser);
                request.setAttribute("userid", userid);
                request.setAttribute("depid", depid);
                request.setAttribute("sendtime", startSubmitTime);
                request.setAttribute("recvtime", endSubmitTime);
                request.setAttribute("taskType", taskType);
                request.setAttribute("sendstate", sendstate);
                request.setAttribute("taskID", taskID);
                request.setAttribute("depNam", depNam);
                request.setAttribute("userName", userName);
            }

            if(taskID!=null && !"".equals(taskID.trim())){
                try{
                    mtVo.setTaskId(Long.parseLong(taskID.trim()));
                }catch (Exception e) {
                    EmpExecutionContext.error("任务ID转换异常，taskID:" + taskID);
                }
            }
            //获取过滤条件
            userid = (userid != null && userid.trim().length() > 0 && !"请选择".equals(userName) && userid.contains(",")) ? userid.substring(0,userid.lastIndexOf(",")):"";
            depid = (depid != null && depid.length() > 0 && !"请选择".equals(depNam)) ? depid : "";

            if(isContainsSun != null && !"".equals(isContainsSun)){
                mtVo.setIsContainsSun(isContainsSun);
            }

            //设置MsType
            mtVo.setMsType("31");

            if(taskType != null && !"".equals(taskType)){
                mtVo.setTaskType(Integer.parseInt(taskType));
            }
            boolean isFirstEnter = !isBack && pageSet(pageInfo,request);

            if(!isFirstEnter){
                mtVo.setSpUser(spUser);
                mtVo.setDepIds(depid);
                mtVo.setUserIds(userid);
                if(title != null && title.length() > 0) {
                    mtVo.setTitle(title);
                }

                if(sendstate!=null&&!"".equals(sendstate)){
                    if(!"0".equals(sendstate)){
                        mtVo.setOverSendstate(sendstate);
                    }
                }

                if (!"".equals(endSubmitTime))
                    mtVo.setEndSendTime(endSubmitTime);
                if (!"".equals(startSubmitTime))
                    mtVo.setStartSendTime(startSubmitTime);

                //查询lfmttask信息
                if(corpCode != null && corpCode.equals("100000")) {
                    mtVoList = surlReportBiz.getLfMttaskVoWithoutDomination(mtVo, pageInfo);
                }else {
                    mtVoList = surlReportBiz.getLfMttaskVo(Long.parseLong(userId), mtVo, pageInfo);
                }

                if(isContainsSun==null||"".equals(isContainsSun)){
                    request.setAttribute("isContainsSun", "0");
                }else{
                    request.setAttribute("isContainsSun", "1");
                }
            }
            request.setAttribute("title", title);
            request.setAttribute("isFirstEnter", isFirstEnter);
            request.setAttribute("sendUserList", userList);
            request.setAttribute("mtList", mtVoList);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("lguserid", userId);
            request.setAttribute("actionPath", actionPath);
            request.setAttribute("titlePath", titlePath.substring(titlePath.lastIndexOf("/"), titlePath.length()));

            //滞留数
            StringBuilder taskids = new StringBuilder();
            if(mtVoList != null && mtVoList.size()>0){
                for(LfMttaskVo mttaskVo:mtVoList){
                    taskids.append(",").append(mttaskVo.getTaskId());
                }
            }
            Map<Long, Long> taskRemains = new HashMap<Long, Long>();
            if(taskids.length()>0){
                taskids.delete(0, 1);
                taskRemains = surlReportBiz.getTaskRemains(taskids.toString());
            }
            request.setAttribute("taskRemains", taskRemains);
            if(!isFirstEnter) {
                String conditionstr = "操作员=" + userName + ",SP账号=" + spUser
                        + ",机构=" + depNam + ",发送状态=" + sendstate
                        + ",发送类型=" + taskType + ",发送主题=" + title
                        + ",任务批次=" + taskID + ",创建时间=" + startSubmitTime
                        + ",结束时间=" + endSubmitTime + ",pageindex=" + pageInfo.getPageIndex() + ",总数：" + pageInfo.getTotalRec();
                SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
                //操作日志信息
                String opContent = "批次发送统计查询："+sdformat.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，条件：" + conditionstr;
                LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
                EmpExecutionContext.info("企业短链", corpCode, userId, lfSysuser.getUserName(), opContent, "GET");
            }
            request.getRequestDispatcher("shorturl/report/surl_batchSendReport.jsp").forward(request,response);
        } catch (Exception e) {
            EmpExecutionContext.error("批次发送统计查询方法请求URL:" + request.getRequestURI()+ "，请求参数，corpCode：" + corpCode + "，userId :" + userId);
            EmpExecutionContext.error(e,"批次发送统计查询的方法异常！");
            request.setAttribute("findresult", "-1");
            request.setAttribute("isFirstEnter", true);//回到页面第一次加载时的状态
            request.setAttribute("titlePath", titlePath);
            request.setAttribute("actionPath", actionPath);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("lguserid", userId);
            try {
                request.getRequestDispatcher("shorturl/report/surl_batchSendReport.jsp").forward(request, response);
            } catch (ServletException e1) {
                EmpExecutionContext.error(e1,"批次发送统计查询serlvet异常！");
            } catch (IOException e1) {
                EmpExecutionContext.error(e1,"批次发送统计查询serlvet跳转异常！");
            }
        }
    }
    /**
     * 群发历史页面获取操作员树的方法
     * @param request
     * @param response
     * @throws Exception
     */
    public void createUserTree2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //加请求日志
            EmpExecutionContext.logRequestUrl(request, null);
            Long depId = null;

            String depStr = request.getParameter("depId");
            //String userid = request.getParameter("lguserid");
            if(depStr != null && !"".equals(depStr.trim())){
                depId = Long.parseLong(depStr);
            }
            String requestPath = request.getRequestURI();
            String titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));//   比如/p_base/smt_smsTaskRecord.jsp->smsTaskRecord

            //当前登录操作员ID
            LfSysuser currentSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //当前登录操作员ID
            Long userid=currentSysuser.getUserId();
            //调用公用创建树的方法
            String departmentTree = getDeptUserJosnData(titlePath,depId,userid,currentSysuser,request);
            response.getWriter().print(departmentTree);
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e,"群发历史页面获取操作员树的方法 异常！");
        }
    }
    /**
     * 操作员树的加载方法(重载方法)
     * @param titlePath
     * @param depId
     * @param userid
     * @return
     * @throws Exception
     */
    public  String getDeptUserJosnData(String titlePath,Long depId,Long userid,LfSysuser loginSysuser,HttpServletRequest request) throws Exception{
        StringBuffer tree = new StringBuffer();
        //已注销
        String yzx = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_139", request);
        //根据userid获取当前操作员信息
        LfSysuser currUser = loginSysuser;
        if(currUser.getPermissionType()==1)
        {
            tree=new StringBuffer("[]");
        }else
        {
            List<LfDep> lfDeps;
            List<LfSysuser> lfSysusers = null;

            DepBiz depBiz = new DepBiz();
            try {
                //如果企业编码是10000的用户登录
                if(currUser.getCorpCode().equals("100000"))
                {
                    if(depId == null)
                    {
                        LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
                        LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
                        //只查询顶级机构
                        conditionMap.put("superiorId", "0");
                        //查询未删除的机构
                        conditionMap.put("depState", "1");
                        //排序
                        orderbyMap.put("depId", StaticValue.ASC);
                        orderbyMap.put("deppath", StaticValue.ASC);
                        lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);
                    }
                    else
                    {
                        lfDeps = new DepBiz().getDepsByDepSuperId(depId);
                    }


                }else
                {
                    if(depId == null){
                        lfDeps = new ArrayList<LfDep>();
                        LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(userid,currUser.getCorpCode()).get(0);
                        lfDeps.add(lfDep);
                    }else{
                        lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,currUser.getCorpCode());
                    }
                }
                //拼结机构树
                LfDep lfDep = null;
                tree = new StringBuffer("[");
                for (int i = 0; i < lfDeps.size(); i++) {
                    lfDep = lfDeps.get(i);
                    tree.append("{");
                    tree.append("id:").append(lfDep.getDepId());
                    tree.append(",name:'").append(lfDep.getDepName()).append("'");
                    tree.append(",pId:").append(lfDep.getSuperiorId());
                    tree.append(",depId:'").append(lfDep.getDepId()+"'");
                    tree.append(",isParent:").append(true);
                    tree.append(",nocheck:").append(true);
                    tree.append("}");
                    if(i != lfDeps.size()-1){
                        tree.append(",");
                    }
                }

                //	SysuserBiz sysBiz = new SysuserBiz();
                if(depId != null)
                {
                    //如果当前登录用户的企业编码是10000
                    if(currUser.getCorpCode().equals("100000"))
                    {
                        LinkedHashMap<String,String> conMap = new LinkedHashMap<String,String>();
                        conMap.put("userId&<>","1" );
                        conMap.put("depId", depId.toString());
                        lfSysusers = baseBiz.getByCondition(LfSysuser.class, conMap, null);
                    }else {
                        //获取所有状态操作员信息
                        /*lfSysusers = sysBiz.getAllSysusersOfSmsTaskRecordByDep(userid,depId);*/
                        lfSysusers = surlReportBiz.getAllSysusersOfSmsTaskRecordByDep(userid,depId);
                    }
                }
                //拼结操作员信息
                LfSysuser lfSysuser = null;
                if(lfSysusers != null && !lfSysusers.isEmpty()){
                    if(lfDeps !=null && lfDeps.size()>0)
                    {
                        tree.append(",");
                    }

                    for (int i = 0; i < lfSysusers.size(); i++) {
                        //操作员信息
                        lfSysuser = lfSysusers.get(i);
                        tree.append("{");
                        tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
                        tree.append(",name:'").append(lfSysuser.getName()).append("'");
                        if(lfSysuser.getUserState()==2)
                        {
                            tree.append(",name:'").append(lfSysuser.getName()).append("("+yzx+")'");
                        }else
                        {
                            tree.append(",name:'").append(lfSysuser.getName()).append("'");
                        }
                        tree.append(",pId:").append(lfSysuser.getDepId());
                        tree.append(",depId:'").append(lfSysuser.getDepId()+"'");
                        tree.append(",isParent:").append(false);
                        tree.append("}");
                        if(i != lfSysusers.size()-1){
                            tree.append(",");
                        }
                    }
                }

                tree.append("]");
            } catch (Exception e) {
                EmpExecutionContext.error(e,"群发历史或群发任务操作员树的加载方法异常！");
            }
        }
        return tree.toString();
    }
    /**
     * 查询条件中的机构树加载方法
     * @param request
     * @param response
     * @throws Exception
     */
    //输出机构代码数据
    public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //加请求日志
            EmpExecutionContext.logRequestUrl(request, null);
            Long depId = null;
            Long userid=null;
            //部门iD
            String depStr = request.getParameter("depId");
            if(depStr != null && !"".equals(depStr.trim())){
                depId = Long.parseLong(depStr);
            }
            //登录操作员ID
            LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //当前登录操作员ID
            userid=loginSysuser.getUserId();
            String departmentTree = surlReportBiz.getDepartmentJosnData2(depId, userid,loginSysuser);
            response.getWriter().print(departmentTree);
        }
        catch (Exception e) {
            EmpExecutionContext.error(e,"群发历史或群发任务查询条件中的机构树加载方法异常");
        }
    }
    /**
     * 批次发送统计报表excel导出方法
     * @param request
     * @param response
     * @throws Exception
     */
    public void exportAllReport(HttpServletRequest request,HttpServletResponse response)throws Exception{

        long startTime = System.currentTimeMillis();
        //下行短信list
        List<LfMttaskVo> mtVoList = null;

        LfMttaskVo mtVo = new LfMttaskVo();
        //企业编码
        String corpCode = null;
        //用户id
        String userId = null;
        try
        {
            //登录操作员信息
            LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //当前登录用户的企业编码
            corpCode =loginSysuser.getCorpCode();
            userId =String.valueOf(loginSysuser.getUserId());

            String spUser = request.getParameter("spuser");
            String taskType= request.getParameter("taskType");
            String mTitle= request.getParameter("mTitle");
            String taskID= request.getParameter("taskID");
            String isContainsSun= request.getParameter("isContainsSun");
            String sendstate= request.getParameter("sendstate");
            String depId= request.getParameter("depId");
            String userIds= request.getParameter("userIds");
            String sendtime= request.getParameter("sendtime");
            String recvtime= request.getParameter("recvtime");

            if(taskType!=null&&!"".equals(taskType)){
                mtVo.setTaskType(Integer.parseInt(taskType));
            }

            //增加批次号
            if(taskID!=null&&!"".equals(taskID.trim())){
                mtVo.setTaskId(Long.parseLong(taskID.trim()));
            }

            //是否包含子机构
            if(isContainsSun!=null&&!"".equals(isContainsSun)){
                mtVo.setIsContainsSun(isContainsSun);
            }

            mtVo.setMsType("31");
            mtVo.setSpUser(spUser);
            mtVo.setDepIds(depId);
            mtVo.setUserIds(userIds);
            if(mTitle != null && mTitle.length() > 0) {
                mtVo.setTitle(mTitle);
            }

            //查询发送时间
            if (!"".equals(recvtime)&& null != recvtime)
                mtVo.setEndSendTime(recvtime);
            if (!"".equals(sendtime)&& null != sendtime)
                mtVo.setStartSendTime(sendtime);

            if(sendstate!=null&&!"".equals(sendstate)){
                if(!"0".equals(sendstate)){
                    mtVo.setOverSendstate(sendstate);
                }
            }

            if(corpCode != null && corpCode.equals("100000")) {
                mtVoList = surlReportBiz.getLfMttaskVoWithoutDomination( mtVo, null);
            }else {
                mtVoList = surlReportBiz.getLfMttaskVo(Long.parseLong(userId), mtVo, null);
            }

            //返回状态
            String result = "false";
            //操作日志信息
            String opContent = "";

            SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
            if (mtVoList != null && mtVoList.size()>0 ) {
                Map<String, String> resultMap = et.createMtReportExcel(mtVoList,request);
                if(resultMap != null && resultMap.size() == 2){
                    request.getSession(false).setAttribute("BatchSend",resultMap);
                    //操作日志信息
                    opContent = "企业短链，批次访问统计报表导出，导出成功。";
                    result = "true";
                }else {
                    opContent = "企业短链，批次访问统计报表导出，导出失败。";
                }
                opContent += "开始："+sdformat.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + mtVoList.size();
            } else {
                opContent = "企业短链，批次发送统计报表导出，导出失败，没有有效可导出的数据。";
            }
            opContent += "开始："+sdformat.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + (mtVoList==null?0:mtVoList.size());
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            EmpExecutionContext.info("企业短链，批次发送统计报表导出", lfSysuser.getCorpCode(), userId, lfSysuser.getUserName(), opContent, "GET");
            response.getWriter().print(result);
        } catch (Exception e) {
            //异常打印
            EmpExecutionContext.error(e,"群发历史查询的excel导出异常！");
            response.getWriter().print("false");
        }
    }

    public void findSendInfoByCondition(HttpServletRequest request, HttpServletResponse response){
        PageInfo pageInfo = new PageInfo();
        List<SendDetailMttaskVo> mttaskVoList = new ArrayList<SendDetailMttaskVo>();
        try {
            pageSet(pageInfo, request);
            String taskId = request.getParameter("taskId");
            //运营商
            String spisuncm = request.getParameter("spisuncm");
            //手机号码
            String phone = request.getParameter("phone");
            //状态
            String type = request.getParameter("type");

            LfMttask lfMttask = surlReportBiz.getLfMttaskByTaskId(Long.parseLong(taskId));

            SendDetailMttaskVo detailMttaskVo = new SendDetailMttaskVo();
            detailMttaskVo.setTaskId(taskId);
            detailMttaskVo.setSendTime(lfMttask.getTimerTime());
            detailMttaskVo.setPhone(phone);
            if(!"".equals(spisuncm)){
                detailMttaskVo.setUnicom(Long.parseLong(spisuncm));
            }

            //获取页面传过来的类型（1：查询全部，2：查询接收错误，3:查询提交错误,4:带页面查询条件的）
            if(type != null) {
                if (type.equals("2")) {
                    //接收错误
                    detailMttaskVo.setErrorcode(" not like 'E1:%' and ERRORCODE not like 'E2:%' and ERRORCODE not like 'DELIVRD' and ERRORCODE <> ''");
                } else if (type.equals("3")) {
                    //提交错误
                    detailMttaskVo.setErrorcode(" like 'E1:%' or ERRORCODE like 'E2:%'");
                } else if (type.equals("5")) {
                    //发送成功
                    detailMttaskVo.setErrorcode("= 'DELIVRD'");
                } else if (type.equals("6")) {
                    //状态未返
                    detailMttaskVo.setErrorcode(" = ''");
                }
            }
            mttaskVoList =  surlReportBiz.findAllSendMttask(detailMttaskVo,pageInfo);

            request.setAttribute("mttaskVoList", mttaskVoList);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("spisuncm",spisuncm);
            request.setAttribute("phone",phone);
            request.setAttribute("type",type);

            request.getRequestDispatcher("shorturl/report/surl_batchSendDetail.jsp").forward(request,response);
        }catch (Exception e){
            try {
                request.setAttribute("mttaskVoList", mttaskVoList);
                request.setAttribute("pageInfo", pageInfo);
                request.getRequestDispatcher("shorturl/report/surl_batchSendDetail.jsp").forward(request,response);
            } catch (Exception e1) {
                EmpExecutionContext.error(e,"企业短链报表查询-批次访问统计-发送详情查询(条件查询)异常");
            }
        }
    }

    public void findAllSendDetail(HttpServletRequest request, HttpServletResponse response) {
        PageInfo pageInfo = new PageInfo();
        List<SendDetailMttaskVo> mttaskVoList = new ArrayList<SendDetailMttaskVo>();
        try {
            pageSet(pageInfo,request);
            String taskId  = request.getParameter("taskId");

            LfMttask lfMttask = surlReportBiz.getLfMttaskByTaskId(Long.parseLong(taskId));

            //记录上一个页面的跳转信息，用于返回操作
            PageInfo prePageInfo = new PageInfo();
            pageSet(prePageInfo, request);

            request.getSession(false).setAttribute("prePageInfo",prePageInfo);

            //主题
            String title = lfMttask.getTitle();
            //内容
            String message = lfMttask.getMsg();
            //发送时间
            String sendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lfMttask.getTimerTime().getTime());
            //发送情况
            String sendInfo = "待汇总";
            //发送总数
            String icount = lfMttask.getIcount2();
            if(icount != null){
                //提交失败总数
                String fail_count=(lfMttask.getFaiCount() == null?"0":lfMttask.getFaiCount());
                //发送成功数
                long suc= Long.parseLong(icount) - Long.parseLong(fail_count);
                //接收失败总数
                String r_count=String.valueOf((lfMttask.getRfail2()==null?"0":lfMttask.getRfail2()));
                //发送总数为-条，其中发送成功数为-条，提交失败数为-条，接收失败数为-条
                sendInfo = "发送总数为"+icount+"条，其中发送成功数为"+suc+"条，提交失败数为"+fail_count+"条，接收失败数为"+r_count+"条";
            }

            HashMap<String,String> map = new HashMap<String, String>();
            map.put("title",title);
            map.put("message",message);
            map.put("sendtime",sendTime);
            map.put("sendInfo",sendInfo);
            map.put("taskId",taskId);
            request.getSession(false).setAttribute("batchSendDetailMap",map);


            //获取该批次所有的号码信息
            SendDetailMttaskVo detailMttaskVo = new SendDetailMttaskVo();
            detailMttaskVo.setTaskId(taskId);
            detailMttaskVo.setSendTime(lfMttask.getTimerTime());

            mttaskVoList =  surlReportBiz.findAllSendMttask(detailMttaskVo,pageInfo);

            request.setAttribute("mttaskVoList", mttaskVoList);
            request.setAttribute("pageInfo", pageInfo);

            request.getRequestDispatcher("shorturl/report/surl_batchSendDetail.jsp").forward(request,response);
        } catch (Exception e) {
            try {
                request.setAttribute("mttaskVoList", mttaskVoList);
                request.setAttribute("pageInfo", pageInfo);
                request.getRequestDispatcher("shorturl/report/surl_batchSendDetail.jsp").forward(request,response);
            } catch (Exception e1) {
                EmpExecutionContext.error(e,"企业短链报表查询-批次访问统计-发送详情查询异常");
            }
        }
    }

    public void getReplyDetail(HttpServletRequest request, HttpServletResponse response){
        PageInfo pageInfo = new PageInfo();
        List<ReplyDetailVo> replyDetailVos = new ArrayList<ReplyDetailVo>();
        try {
            pageSet(pageInfo,request);
            String taskId  = request.getParameter("taskId");

            LfMttask lfMttask = surlReportBiz.getLfMttaskByTaskId(Long.parseLong(taskId));

            //记录上一个页面的跳转信息，用于返回操作
            PageInfo prePageInfo = new PageInfo();
            pageSet(prePageInfo, request);

            ReplyDetailVo replyDetailVo = new ReplyDetailVo();
            replyDetailVo.setTaskId(taskId);
            replyDetailVo.setSendTime(lfMttask.getTimerTime());

            replyDetailVos =  surlReportBiz.findAllReplyDetail(replyDetailVo,pageInfo);

            request.getSession(false).setAttribute("prePageInfo",prePageInfo);
            request.setAttribute("replyDetailVos", replyDetailVos);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("taskId", taskId);

            //获取该批次所有的回复信息
            request.getRequestDispatcher("shorturl/report/surl_batchReplyDetail.jsp").forward(request,response);
        }catch (Exception e){
            try {
                request.setAttribute("replyDetailVos", replyDetailVos);
                request.setAttribute("pageInfo", pageInfo);
                request.getRequestDispatcher("shorturl/report/surl_batchReplyDetail.jsp").forward(request,response);
            } catch (Exception e1) {
                EmpExecutionContext.error(e,"企业短链报表查询-批次访问统计-回复详情查询异常");
            }
        }
    }

    public void getReplyDetailByCondition(HttpServletRequest request, HttpServletResponse response){
        PageInfo pageInfo = new PageInfo();
        List<ReplyDetailVo> replyDetailVos = new ArrayList<ReplyDetailVo>();
        try {
            pageSet(pageInfo,request);

            String taskId  = request.getParameter("taskId");

            LfMttask lfMttask = surlReportBiz.getLfMttaskByTaskId(Long.parseLong(taskId));

            ReplyDetailVo replyDetailVo = new ReplyDetailVo();
            replyDetailVo.setTaskId(taskId);
            replyDetailVo.setSendTime(lfMttask.getTimerTime());

            String replyPhone =request.getParameter("replyPhone");
            String replyName = request.getParameter("replyName");
            String replyContent = request.getParameter("replyContent");

            replyDetailVo.setReplyName(replyName);
            replyDetailVo.setMsgContent(replyContent);
            replyDetailVo.setPhone(replyPhone);

            replyDetailVos =  surlReportBiz.findAllReplyDetail(replyDetailVo,pageInfo);

            /*数据回显*/
            request.setAttribute("replyPhone", replyPhone);
            request.setAttribute("replyName", replyName);
            request.setAttribute("replyContent", replyContent);

            request.setAttribute("replyDetailVos", replyDetailVos);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("taskId", taskId);
            //获取该批次所有的回复信息
            request.getRequestDispatcher("shorturl/report/surl_batchReplyDetail.jsp").forward(request,response);
        }catch (Exception e){
            try {
                request.setAttribute("replyDetailVos", replyDetailVos);
                request.setAttribute("pageInfo", pageInfo);
                request.getRequestDispatcher("shorturl/report/surl_batchReplyDetail.jsp").forward(request,response);
            } catch (Exception e1) {
                EmpExecutionContext.error(e,"企业短链报表查询-批次访问统计-回复详情查询(条件)异常");
            }
        }
    }

    /**
     * EXCEL文件下载
     * @param request
     * @param response
     * @throws Exception
     */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //文件名
        String fileName = "";
        //文件路径
        String filePath = "";
        //导出类型
        String type = "";
        try {
            type = request.getParameter("type");
            HttpSession session = request.getSession(false);
            Object obj = session.getAttribute(type);
            if(obj != null){
                Map<String, String> resultMap = (Map<String, String>) obj;
                fileName = resultMap.get("FILE_NAME");
                filePath = resultMap.get("FILE_PATH");
                //弹出下载页面
                new DownloadFile().downFile(request, response, filePath, fileName);
            }
        }
        catch (Exception e) {
            EmpExecutionContext.error(e, "群发EXCEL文件下载失败，fileName:"+fileName+"，filePath:"+filePath+"，exportType:"+type);
        }
    }

    /**
     * 批次访问统计-号码详情查看的导出功能实现
     * @param request
     * @param response
     * @throws Exception
     */
    public void sendDetailReportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception{
        long startTime = System.currentTimeMillis();
        List<SendDetailMttaskVo> mttaskVoList = new ArrayList<SendDetailMttaskVo>();
        String type;
        String spisuncm;
        String phone;
        String userId;
        String corpCode;
        String taskId;
        StringBuffer logContent = new StringBuffer();
        try{
            //企业编码
            corpCode = request.getParameter("lgcorpcode");
            //用户id
           // userId = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            userId = SysuserUtil.strLguserid(request);
            //批次号
            taskId  = request.getParameter("taskId");
            //是否只导出手机号
            String IsexportAll = request.getParameter("IsexportAll");
            //获取页面传过来的查询条件
            type = request.getParameter("type");//发送状态
            spisuncm = request.getParameter("spisuncm");//运营商
            phone = request.getParameter("phone");//手机号

            //记录日志
            logContent.append("参数内容：").append("taskId:").append(taskId).
                        append(",userId:").append(userId).append(",corpCode:").
                        append(corpCode).append(",运营商：").append(spisuncm).
                        append("，手机号：").append(phone).append(",发送状态：").append(type);

            LfMttask lfMttask = surlReportBiz.getLfMttaskByTaskId(Long.parseLong(taskId));
            SendDetailMttaskVo detailMttaskVo = new SendDetailMttaskVo();
            detailMttaskVo.setTaskId(taskId);
            detailMttaskVo.setSendTime(lfMttask.getTimerTime());
            detailMttaskVo.setPhone(phone);
            if(!"".equals(spisuncm)){
                detailMttaskVo.setUnicom(Long.parseLong(spisuncm));
            }

            //获取页面传过来的类型（1：查询全部，2：查询接收错误，3:查询提交错误,5:发送成功，6：状态未返）
            if(type != null) {
                if (type.equals("2")) {
                    //接收错误
                    detailMttaskVo.setErrorcode(" not like 'E1:%' and ERRORCODE not like 'E2:%' and ERRORCODE not like 'DELIVRD' and ERRORCODE <> ''");
                } else if (type.equals("3")) {
                    //提交错误
                    detailMttaskVo.setErrorcode(" like 'E1:%' or ERRORCODE like 'E2:%'");
                } else if (type.equals("5")) {
                    //发送成功
                    detailMttaskVo.setErrorcode("= 'DELIVRD'");
                } else if (type.equals("6")) {
                    //状态未返
                    detailMttaskVo.setErrorcode(" = ''");
                }
            }
            mttaskVoList =  surlReportBiz.findAllSendMttask(detailMttaskVo,null);
            String opContent = "";
            //返回状态
            String result = "false";
            if (mttaskVoList != null && mttaskVoList.size()>0 ) {
                Map<String,String> btnMap=(Map<String,String>)request.getSession(false).getAttribute("btnMap");
                int ishidephome=0;
                if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null){
                    ishidephome=1;
                }
                Map<String, String> resultMap = et.createSendDetailExcel(mttaskVoList,IsexportAll,ishidephome,request);
                HttpSession session = request.getSession(false);
                session.setAttribute("BatchSendDetail",resultMap);

                SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
                //操作日志信息
                opContent = "详情导出成功。开始："+sdformat.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + mttaskVoList.size() + "。" + logContent;
                result = "true";
            }else {
                opContent = "详情导出失败。" + logContent;
            }
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            EmpExecutionContext.info("企业短链-批次发送统计-发送详情报表", lfSysuser.getCorpCode(), lfSysuser.getUserId().toString(), lfSysuser.getUserName(), opContent, "GET");
            response.getWriter().print(result);
        }catch (Exception e){
            EmpExecutionContext.error(e,"企业短链-批次发送统计-发送详情报表，excel导出异常！" + logContent);
            response.getWriter().print("false");
        }
    }

    /**
     * 批次访问统计-回复详情查看的导出功能实现
     * @param request
     * @param response
     * @throws Exception
     */
    public void sendReplyDetailExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<ReplyDetailVo> replyDetailVos = new ArrayList<ReplyDetailVo>();
        long startTime = System.currentTimeMillis();
        String replyName;
        String replyPhone;
        String replyContent;
        String userId;
        String corpCode;
        String taskId;
        StringBuffer logContent = new StringBuffer();
        try {
            //企业编码
            corpCode = request.getParameter("lgcorpcode");
            //用户id
            //userId = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            userId = SysuserUtil.strLguserid(request);
            //批次号
            taskId = request.getParameter("taskId");
            //回复手机号
            replyPhone = request.getParameter("replyPhone");
            //回复内容
            replyContent = request.getParameter("replyContent");
           //回复姓名
            replyName = request.getParameter("replyName");

            //记录日志
            logContent.append("参数内容：").append("taskId:").append(taskId).
                    append(",userId:").append(userId).append(",corpCode:").
                    append(corpCode).append(",回复姓名:").append(replyName).
                    append(",回复手机号:").append(replyPhone).append(",回复内容：").append(replyContent);

            LfMttask lfMttask = surlReportBiz.getLfMttaskByTaskId(Long.parseLong(taskId));

            ReplyDetailVo replyDetailVo = new ReplyDetailVo();
            replyDetailVo.setTaskId(taskId);
            replyDetailVo.setSendTime(lfMttask.getTimerTime());

            replyDetailVo.setReplyName(replyName);
            replyDetailVo.setMsgContent(replyContent);
            replyDetailVo.setPhone(replyPhone);

            replyDetailVos =  surlReportBiz.findAllReplyDetail(replyDetailVo,null);

            String opContent = "";
            //返回状态
            String result = "false";
            if (replyDetailVos != null && replyDetailVos.size()>0 ) {

                Map<String, String> resultMap = et.createSendReplyDetailExcel(replyDetailVos,request);

                HttpSession session = request.getSession(false);
                session.setAttribute("SendReplyDetail",resultMap);
                SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
                //操作日志信息
                opContent = "详情导出成功。开始："+sdformat.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + replyDetailVos.size() + "。" + logContent;
                result = "true";
            }else {
                opContent = "详情导出失败。" + logContent;
            }
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            EmpExecutionContext.info("企业短链-批次发送统计-回复详情报表", lfSysuser.getCorpCode(), lfSysuser.getUserId().toString(), lfSysuser.getUserName(), opContent, "GET");
            response.getWriter().print(result);
        }catch (Exception e){
            EmpExecutionContext.error(e,"企业短链-批次发送统计-发送详情报表，excel导出异常！" + logContent);
            response.getWriter().print("false");
        }
    }
}
