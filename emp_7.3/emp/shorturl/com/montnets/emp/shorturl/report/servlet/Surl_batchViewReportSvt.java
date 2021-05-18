package com.montnets.emp.shorturl.report.servlet;

import com.montnets.EMPException;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.shorturl.report.biz.SurlReportBiz;
import com.montnets.emp.shorturl.report.biz.SurlTaskReportExcelTool;
import com.montnets.emp.shorturl.report.vo.BatchVisitVo;
import com.montnets.emp.shorturl.report.vo.SendDetailMttaskVo;
import com.montnets.emp.shorturl.report.vo.VstDetailVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Surl_batchViewReportSvt extends BaseServlet{
    private static final Integer SURL_MS_TYPE = 31;
    private final SurlReportBiz surlReportBiz = new SurlReportBiz();
    /**
     * 模板路径
     */
    private final String  excelPath = new TxtFileUtil().getWebRoot() + "shorturl/report/file";
    /**
     * 生成excel的工具类
     */
    final SurlTaskReportExcelTool et = new SurlTaskReportExcelTool(excelPath);

    public void find(HttpServletRequest request, HttpServletResponse response) {
        String requestPath = request.getRequestURI();
        String actionPath = requestPath.substring(requestPath.lastIndexOf("/")+1, requestPath.length());
        long startTime = System.currentTimeMillis();
        //当前登录用户的企业编码
        String corpCode = "";
        String  userId = "";
        PageInfo pageInfo = new PageInfo();
        List<BatchVisitVo> viewBatchVisits = null;
        BatchVisitVo visitVo = new BatchVisitVo();
        try{
            //登录操作员信息
            LfSysuser loginSysuser = surlReportBiz.getCurrenUser(request);
            //当前登录用户的企业编码
            corpCode =loginSysuser.getCorpCode();
            userId =String.valueOf(loginSysuser.getUserId());
            //隶属机构
            String depid = request.getParameter("depid");
            String depNam = request.getParameter("depNam");
            //操作员
            String userid = request.getParameter("userid");
            String origUserid = userid;
            String userName = request.getParameter("userName");
            //发送主题
            String title = request.getParameter("title");
            //任务批次
            String taskID = request.getParameter("taskID");
            //长地址
            String longUrl = request.getParameter("longUrl");
            //创建时间
            String startSubmitTime = request.getParameter("sendtime");
            //结束时间
            String endSubmitTime = request.getParameter("recvtime");
            //是否包含子机构
            String isContainsSun=request.getParameter("isContainsSun");

            boolean isFirstEnter = pageSet(pageInfo,request);

            //获取过滤条件
            userid = (userid != null && userid.trim().length() > 0 && !"请选择".equals(userName) && userid.contains(",")) ? userid.substring(0,userid.lastIndexOf(",")):"";
            depid = (depid != null && depid.length() > 0 && !"请选择".equals(depNam)) ? depid : "";

            if(taskID != null && !"".equals(taskID.trim())){
                try{
                    visitVo.setTaskId(Long.parseLong(taskID.trim()));
                }catch (Exception e) {
                    EmpExecutionContext.error("任务ID转换异常，taskID:" + taskID);
                }
            }
            if(isContainsSun != null && !"".equals(isContainsSun)){
                visitVo.setIsContainsSun(isContainsSun);
            }
            //设置Ms_Type
            visitVo.setMsType(SURL_MS_TYPE);
            visitVo.setDepIds(depid);
            visitVo.setUserIds(userid);
            visitVo.setNetUrl(longUrl);
            visitVo.setCurrCorpCode(loginSysuser.getCorpCode());
            visitVo.setCurrUserId(userId);
            visitVo.setCurrUserName(loginSysuser.getUserName());
            visitVo.setCurrUserDataPri(loginSysuser.getPermissionType());

            //发送主题
            if(title != null && title.length() > 0) {
                visitVo.setTitle(title);
            }

            if (!"".equals(endSubmitTime))
                visitVo.setEndSendTime(endSubmitTime);
            if (!"".equals(startSubmitTime))
                visitVo.setStartSendTime(startSubmitTime);

            if(!isFirstEnter) {
                viewBatchVisits = surlReportBiz.getBatchVisitVo(visitVo, pageInfo);
            }

            if(isContainsSun == null|| "".equals(isContainsSun)){
                request.setAttribute("isContainsSun", "0");
            }else{
                request.setAttribute("isContainsSun", "1");
            }
            request.setAttribute("viewBatchVisits", viewBatchVisits);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("isFirstEnter", isFirstEnter);
            request.setAttribute("actionPath", actionPath);
            request.setAttribute("BatchVisitVo", visitVo);

            request.setAttribute("depid", depid);
            request.setAttribute("depNam", depNam);
            request.setAttribute("userid", origUserid);
            request.setAttribute("userName", userName);
            request.setAttribute("isContainsSun", isContainsSun);

            request.setAttribute("currUserid", loginSysuser.getUserId().toString());



            if(!isFirstEnter) {
                String conditionstr = "操作员=" + userName +
                        ",机构=" + depNam + ",源地址=" + longUrl
                        + ",发送主题=" + title
                        + ",任务批次=" + taskID + ",发送时间=" + startSubmitTime
                        + ",结束时间=" + endSubmitTime + ",pageindex=" + pageInfo.getPageIndex() + ",总数：" + pageInfo.getTotalRec();
                //格式化时间
                SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
                //操作日志信息
                String opContent = "批次访问统计查询："+ sdformat.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，条件：" + conditionstr;
                LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
                EmpExecutionContext.info("企业短链", corpCode, userId, lfSysuser.getUserName(), opContent, "GET");
            }
            request.getRequestDispatcher("shorturl/report/surl_batchViewReport.jsp").forward(request,response);
        }catch (Exception e){
            EmpExecutionContext.error("批次访问统计查询方法请求URL:" + request.getRequestURI()+ "，请求参数，corpCode：" + corpCode + "，userId :" + userId);
            EmpExecutionContext.error(e,"批次访问统计查询的方法异常！");
            request.setAttribute("findresult", "-1");
            request.setAttribute("isFirstEnter", true);//回到页面第一次加载时的状态
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("actionPath", actionPath);
            try {
                request.getRequestDispatcher("shorturl/report/surl_batchViewReport.jsp").forward(request,response);
            } catch (ServletException e1) {
                EmpExecutionContext.error(e1,"批次发送统计查询serlvet异常！");
            } catch (IOException e1) {
                EmpExecutionContext.error(e1,"批次发送统计查询serlvet跳转异常！");
            }
        }
    }

    public void getPhoneNumDetail(HttpServletRequest request, HttpServletResponse response){
        String corpCode = "";
        String userId = "";
        String requestPath = request.getRequestURI();
        String actionPath = requestPath.substring(requestPath.lastIndexOf("/")+1, requestPath.length());
        try{
            //登录操作员信息
            LfSysuser loginSysuser = surlReportBiz.getCurrenUser(request);
            corpCode = loginSysuser.getCorpCode();
            userId = String.valueOf(loginSysuser.getUserId());

            String taskId = request.getParameter("taskId");
            if(taskId == null){
                EmpExecutionContext.error("批次访问详情，查看号码详情异常，传入参数有误，taskId为null");
                throw new EMPException();
            }
            //根据TaskId获取相关信息
            LfMttask lfMttask =  surlReportBiz.getLfMttaskByTaskId(Long.parseLong(taskId));
            if(lfMttask == null){
                EmpExecutionContext.error("批次访问详情，查看号码详情异常，根据TaskId查询LfMttask结果为null");
                throw new EMPException();
            }
            PageInfo pageInfo = new PageInfo();

            //获取访问人数与访问次数
            HashMap<String, Integer> map = surlReportBiz.getVisitStatistByTaskId(Long.parseLong(taskId),lfMttask.getTimerTime());

            //记录上一个页面的跳转信息，用于返回操作
            PageInfo prePageInfo = new PageInfo();
            pageSet(prePageInfo, request);

            request.getSession(false).setAttribute("prePageInfo",prePageInfo);

            VstDetailVo detailVo = new VstDetailVo();

            detailVo.setTaskId(taskId);
            detailVo.setSendTime(lfMttask.getTimerTime());

            //获取该批次所有号码的手机相关信息
            List<VstDetailVo> vstDetailVoList =  surlReportBiz.getAllVstDetail(detailVo,pageInfo);

            request.setAttribute("actionPath", actionPath);
            request.setAttribute("lfMttask", lfMttask);
            request.setAttribute("vstDetailVoList", vstDetailVoList);
            request.setAttribute("visitorCount", map.size() == 0 ? 0 : map.get("visitorCount"));
            request.setAttribute("visitCount", map.size() == 0 ? 0 : map.get("visitCount"));
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("corpCode", corpCode);
            request.setAttribute("userId", userId);

            request.getRequestDispatcher("shorturl/report/surl_phoneNumDetail.jsp").forward(request,response);
        }catch (Exception e){
            EmpExecutionContext.error(e,"企业短链报表查询，批次访问统计-号码详情查看异常");
            try {
                request.setAttribute("actionPath", actionPath);
                request.setAttribute("lfMttask", new LfMttask());
                request.setAttribute("pageInfo", new PageInfo());
                request.getRequestDispatcher("shorturl/report/surl_phoneNumDetail.jsp").forward(request,response);
            } catch (Exception e1) {
                EmpExecutionContext.error(e,"企业短链报表查询，批次访问统计-号码详情查看异常,跳转页面异常");
            }
        }
    }

    /**
     * 根据条件查询号码详情
     * @param request
     * @param response
     */
    public void findPhoneNumDetailByCondition(HttpServletRequest request, HttpServletResponse response){
        String requestPath = request.getRequestURI();
        String corpCode = "";
        String userId = "";
        String actionPath = requestPath.substring(requestPath.lastIndexOf("/")+1, requestPath.length());
        List<VstDetailVo> vstDetailVoList = new ArrayList<VstDetailVo>();
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        PageInfo pageInfo = new PageInfo();
        LfMttask lfMttask = new LfMttask();
        try {
            //任务批次
            String taskId = request.getParameter("taskId");

            if(taskId == null || "".equals(taskId)){
                EmpExecutionContext.error("企业短链，批次访问查询-号码详情条件查询，传入关键参数有误，TaskId为null");
                throw new EMPException();
            }
            //根据TaskId获取相关信息
            lfMttask =  surlReportBiz.getLfMttaskByTaskId(Long.parseLong(taskId));
            if(lfMttask == null){
                EmpExecutionContext.error("批次访问详情，查看号码详情异常，根据TaskId查询LfMttask结果为null");
                throw new EMPException();
            }

            //获取访问人数与访问次数
            map = surlReportBiz.getVisitStatistByTaskId(Long.parseLong(taskId),lfMttask.getTimerTime());

            pageSet(pageInfo,request);

            //手机号码
            String phone = request.getParameter("phone");
            //访问状态
            String visitStatus = request.getParameter("visitStatus");
            //访问区域
            String visitArea = request.getParameter("visitArea");
            //访问IP
            String visitIP = request.getParameter("visitIP");
            //访问时间
            String visitTime = request.getParameter("sendtime");
            //结束时间
            String recvTime = request.getParameter("recvtime");
            //企业编码
            corpCode = request.getParameter("corpCode");

            if(corpCode == null || "".equals(corpCode)){
                //登录操作员信息
                LfSysuser loginSysuser = surlReportBiz.getCurrenUser(request);
                corpCode = loginSysuser.getCorpCode();
            }

            VstDetailVo detailVo = new VstDetailVo();

            detailVo.setCropCode(corpCode);
            detailVo.setPhone(phone);
            detailVo.setTaskId(taskId);
            detailVo.setVisitIP(visitIP);
            detailVo.setVsttm(visitTime);
            detailVo.setEndTime(recvTime);
            detailVo.setSendTime(lfMttask.getTimerTime());
            detailVo.setVisitStatus(visitStatus);
            detailVo.setMobileArea(visitArea);

            vstDetailVoList = surlReportBiz.findPhoneNumDetailByCondition(detailVo, pageInfo);

            //数据回显
            request.setAttribute("phone", phone);
            request.setAttribute("visitStatus",visitStatus);
            request.setAttribute("visitIP",visitIP);
            request.setAttribute("visitArea",visitArea);
            request.setAttribute("sendtime",visitTime);
            request.setAttribute("recvtime",recvTime);

            request.setAttribute("corpCode", corpCode);
            request.setAttribute("vstDetailVoList", vstDetailVoList);
            request.setAttribute("visitorCount", map.size() == 0 ? 0 : map.get("visitorCount"));
            request.setAttribute("visitCount", map.size() == 0 ? 0 : map.get("visitCount"));
            request.setAttribute("lfMttask", lfMttask);
            request.setAttribute("pageInfo",pageInfo);
            request.setAttribute("actionPath", actionPath);

            request.getRequestDispatcher("shorturl/report/surl_phoneNumDetail.jsp").forward(request,response);
        }catch (Exception e){
            request.setAttribute("corpCode", corpCode);
            request.setAttribute("vstDetailList", vstDetailVoList);
            request.setAttribute("lfMttask", lfMttask);
            request.setAttribute("pageInfo",new PageInfo());
            request.setAttribute("actionPath", actionPath);
            request.setAttribute("visitorCount", map.size() == 0 ? 0 : map.get("visitorCount"));
            request.setAttribute("visitCount", map.size() == 0 ? 0 : map.get("visitCount"));
            try {
                request.getRequestDispatcher("shorturl/report/surl_phoneNumDetail.jsp").forward(request,response);
            } catch (ServletException e1) {
                EmpExecutionContext.error(e1,"批次发送统计查询serlvet异常！");
            } catch (IOException e1) {
                EmpExecutionContext.error(e1,"批次发送统计查询serlvet跳转异常！");
            }
        }
    }

    /**
     *批次访问统计报表查询
     * @param request
     * @param response
     * @throws Exception
     */
    public void batchVisitReportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception{
        long startTime = System.currentTimeMillis();
        List<BatchVisitVo> batchVisitVos = new ArrayList<BatchVisitVo>();
        String userid;
        String deptid;
        String title;
        String taskId;
        String longUrl;
        String sendtime;
        String recvtime;
        String lgcorpcode;
        String lguserid;
        String isContainsSun;
        StringBuffer logContent = new StringBuffer();
        BatchVisitVo visitVo = new BatchVisitVo();
        try {
            userid = request.getParameter("userid");
            deptid = request.getParameter("deptid");
            title = request.getParameter("title");
            taskId = request.getParameter("taskId");
            longUrl = request.getParameter("longUrl");
            sendtime = request.getParameter("sendtime");
            recvtime = request.getParameter("recvtime");
            isContainsSun = request.getParameter("isContainsSun");

            //登录操作员信息
            LfSysuser loginSysuser = surlReportBiz.getCurrenUser(request);
            //当前登录用户的企业编码
            lgcorpcode = loginSysuser.getCorpCode();
            lguserid = String.valueOf(loginSysuser.getUserId());

            visitVo.setUserIds(userid);
            visitVo.setDepIds(deptid);
            visitVo.setTitle(title);
            visitVo.setTaskId(taskId == null || "".equals(taskId) ? null:Long.parseLong(taskId));
            visitVo.setNetUrl(longUrl);
            visitVo.setStartSendTime(sendtime);
            visitVo.setEndSendTime(recvtime);
            visitVo.setMsType(SURL_MS_TYPE);
            visitVo.setIsContainsSun(isContainsSun);
            visitVo.setCurrCorpCode(loginSysuser.getCorpCode());
            visitVo.setCurrUserId(loginSysuser.getUserId().toString());
            visitVo.setCurrUserName(loginSysuser.getUserName());
            visitVo.setCurrUserDataPri(loginSysuser.getPermissionType());


            //拼接组装ViewBatchVisit对象返回至jsp页面
            batchVisitVos = surlReportBiz.getBatchVisitVo(visitVo, null);

            //返回状态
            String result = "false";
            //操作日志信息
            String opContent = "";
            //格式化时间
            SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
            if (batchVisitVos != null && batchVisitVos.size()>0 ) {
                Map<String, String> resultMap = et.createBatchVisitExcel(batchVisitVos,request);
                if(resultMap != null && resultMap.size() == 2){
                    request.getSession(false).setAttribute("BatchVisit",resultMap);
                    //操作日志信息
                    opContent = "企业短链，批次访问统计报表导出，导出成功。";
                    result = "true";
                }else {
                    opContent = "企业短链，批次访问统计报表导出，导出失败。";
                }
                opContent += "开始："+sdformat.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + batchVisitVos.size();
            } else {
                opContent = "企业短链，批次访问统计报表导出，导出失败，没有有效可导出的数据。";
            }
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            EmpExecutionContext.info("企业短链，批次发送统计报表导出", lfSysuser.getCorpCode(), lguserid, lfSysuser.getUserName(), opContent, "GET");
            response.getWriter().print(result);
        }catch (Exception e){
            //异常打印
            EmpExecutionContext.error(e,"企业短链-批次访问统计报表，excel导出异常！");
            response.getWriter().print("false");
        }
    }

    /**
     * 批次访问统计-号码详情查看报表查询
     * @param request
     * @param response
     * @throws Exception
     */
    public void batchVisitDetailReportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception{
        long startTime = System.currentTimeMillis();
        List<VstDetailVo> vstDetailVoList = new ArrayList<VstDetailVo>();
        String phone;
        String visitStatus;
        String visitArea;
        String visitIP;
        String sendtime;
        String recvtime;
        String timerTime;
        String isExportAll;
        String taskId;
        String lgcorpcode;
        String lguserid;
        try {
            //登录操作员信息
            LfSysuser loginSysuser = surlReportBiz.getCurrenUser(request);
            //当前登录用户的企业编码
            lgcorpcode = loginSysuser.getCorpCode();
            lguserid = String.valueOf(loginSysuser.getUserId());

            taskId = request.getParameter("taskId");
            phone = request.getParameter("phone");
            visitStatus = request.getParameter("visitStatus");
            visitArea = request.getParameter("visitArea");
            visitIP = request.getParameter("visitIP");
            sendtime = request.getParameter("sendtime");
            recvtime = request.getParameter("recvtime");
            timerTime = request.getParameter("timerTime");
            isExportAll = request.getParameter("isExportAll");

            VstDetailVo detailVo = new VstDetailVo();

            detailVo.setCropCode(lgcorpcode);
            detailVo.setTaskId(taskId);
            detailVo.setSendTime(Timestamp.valueOf(timerTime));
            detailVo.setAreaName(visitArea);
            detailVo.setVisitStatus(visitStatus);
            detailVo.setVisitIP(visitIP);
            detailVo.setVsttm(sendtime);
            detailVo.setEndTime(recvtime);
            detailVo.setPhone(phone);

            //获取该批次所有号码的手机相关信息
            vstDetailVoList =  surlReportBiz.findPhoneNumDetailByCondition(detailVo,null);

            //返回状态
            String result = "false";
            //操作日志信息
            String opContent = "";
            //格式化时间
            SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
            if (vstDetailVoList != null && vstDetailVoList.size()>0 ) {
                Map<String, String> resultMap = et.createBatchVisitDetailExcel(vstDetailVoList,isExportAll,request);
                if(resultMap != null && resultMap.size() == 2){
                    request.getSession(false).setAttribute("BatchVisitDetail",resultMap);
                    //操作日志信息
                    opContent = "企业短链-批次访问统计-号码详情查看报表，导出成功。";
                    result = "true";
                }else {
                    opContent = "企业短链-批次访问统计-号码详情查看报表，导出失败。";
                }
                opContent += "开始："+sdformat.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + vstDetailVoList.size();
            } else {
                opContent = "企业短链-批次访问统计-号码详情查看报表导出，导出失败，没有有效可导出的数据。";
            }
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            EmpExecutionContext.info("企业短链-批次访问统计-号码详情查看报表导出", lfSysuser.getCorpCode(), lguserid, lfSysuser.getUserName(), opContent, "GET");
            response.getWriter().print(result);
        }catch (Exception e){
            //异常打印
            EmpExecutionContext.error(e,"企业短链-批次访问统计报表-号码详情查看，excel导出异常！");
            response.getWriter().print("false");
        }
    }
}
