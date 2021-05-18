package com.montnets.emp.report.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobConfigBiz;
import com.montnets.emp.common.biz.SpUserBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.report.AprovinceCity;
import com.montnets.emp.entity.system.LfPageField;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.query.biz.QueryBiz;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.report.biz.SpMtReportBiz;
import com.montnets.emp.report.vo.*;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 发送账号下行统计报表
 *
 * @author liaojirong <ljr0300@163.com>
 * @project p_cxtj
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:48:30
 * @description
 */
@SuppressWarnings("serial")
public class rep_spMtReportSvt extends BaseServlet {

    // 模块名称
    private final String empRoot = "cxtj";
    private final BaseBiz baseBiz = new BaseBiz();
    // 功能文件夹
    private final String base = "/report";
    //时分秒格式化
    private final SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");
    private final QueryBiz qbiz = new QueryBiz();

    protected int getIntParameter(String param, int defaultValue, HttpServletRequest request) {
        try {
            if (request.getParameter(param) != null && !"".equals(request.getParameter(param))) {
                return Integer.parseInt(request.getParameter(param));
            } else {
                return defaultValue;
            }
        } catch (NumberFormatException e) {
            EmpExecutionContext.error(e, "获取分页信息异常");
            return defaultValue;
        }
    }

    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户信息
        LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY);
        //起始ms数
        long startl = System.currentTimeMillis();
        //开始时间
        String starthms = hms.format(startl);
        // 企业编码
        String corpcode = qbiz.getCorpCode(request);
        // 短彩类型  0是短信  1是彩信
        String msType = request.getParameter("msType");
        //年报表/月报表
        String reportTypeStr = request.getParameter("reportType");
        //账号类型
        String sptype = request.getParameter("sptype");
        //发送类型
        String sendtype = request.getParameter("sendtype");
        //账户名称
        String staffname = request.getParameter("staffname");
        //时间
        String countTime = request.getParameter("countTime");
        //年份
        String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
        //月份
        String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
        //统计时间开始
        String sendtime = request.getParameter("begintime");
        //是否返回
        boolean isBack = request.getParameter("isback") != null;
        //统计时间结束
        String endtime = request.getParameter("endtime");
        //运营商类型（国内  100（实际对应，0,1,21），国外 5）
        String spisuncm = request.getParameter("spisuncm");

        //区域
        String araCode = request.getParameter("araCode");
        //业务类型
        String svrType = request.getParameter("svrType");
        //操作员
        String userIdStr = request.getParameter("userIdStr");
        String userName = request.getParameter("userName");
        //机构
        String depIdStr = request.getParameter("depIdStr");
        String depName = request.getParameter("depName");
        String containSubDep = request.getParameter("containSubDep");
        //国内运营商
        String domesticOperator = request.getParameter("DomesticOperator");

        //短彩信帐号
        String spusers;
        String cTime = "";
        boolean isFirstEnter = false;
        //年报表月报表 默认为日报表
        int reportType = 2;
        PageInfo pageInfo = new PageInfo();
        SpMtReportBiz drp = new SpMtReportBiz();
        // 业务类型Map<业务编码，业务名称>
        HashMap<String, String> busTypeMap = new HashMap<String, String>(16);
        //业务类型
        List<LfBusManager> busTypeList = null;
        try {
            if (msType == null) {
                // 默认为短信
                msType = "0";
            }

            //短信彩帐号字符串
            spusers = qbiz.getSpUsers(msType, corpcode, StaticValue.getCORPTYPE());
            //查询条件对象
            SpMtDataReportVo spisuncmMtDataReportVo = new SpMtDataReportVo();
            //短彩类型
            spisuncmMtDataReportVo.setMstype(Integer.parseInt(msType));
            //企业编码
            spisuncmMtDataReportVo.setCorpCode(corpcode);
            //运营商类型
            spisuncmMtDataReportVo.setSpisuncm(spisuncm);

            spisuncmMtDataReportVo.setAraCode(araCode);
            spisuncmMtDataReportVo.setSvrType(svrType);
            spisuncmMtDataReportVo.setDepIdStr(depIdStr);
            spisuncmMtDataReportVo.setUserIdStr(userIdStr);
            spisuncmMtDataReportVo.setDomesticOperator(domesticOperator);
            spisuncmMtDataReportVo.setContainSubDep("1".equals(containSubDep));
            //获取当前操作员ID
            spisuncmMtDataReportVo.setCurrentUserId(lfSysuser.getUserId().toString());

            //将页面传来的操作员与机构信息转换为操作员编码
            //String userCode = drp.getPriUserCode(qbiz.getCurrenUser(request), depIdStr, userIdStr, spisuncmMtDataReportVo.getContainSubDep());
            spisuncmMtDataReportVo.setPriviligeUserCode(lfSysuser.getUserCode());

            if (sptype != null) {
                //账号类型
                if ("0".equals(sptype)) {
                    //spisuncmMtDataReportVo.setSptypes("1,2,3");
                } else if ("1".equals(sptype)) {
                    spisuncmMtDataReportVo.setSptypes("1");
                } else if ("2".equals(sptype)) {
                    spisuncmMtDataReportVo.setSptypes("2,3");
                }
            }
            //如果是彩信选择EMP发送即查所有不需要条件
            if ("1".equals(msType)) {
                if (sendtype != null && "1".equals(sendtype)) {
                    sendtype = null;
                }
            }
            //发送账户类型
            spisuncmMtDataReportVo.setSendtypes(sendtype);

            spisuncmMtDataReportVo.setStaffname(staffname);
            //账户名称


            //发送帐号
            if (!"".equals(spusers)) {
                spisuncmMtDataReportVo.setSpUsers(spusers);
            }
            //月份
            if (timeMonth.length() > 2) {
                timeMonth = timeMonth.substring(1);
            }

            if (reportTypeStr != null) {
                reportType = Integer.parseInt(reportTypeStr);
            }
            if (!"".equals(countTime) && countTime != null) {
                timeYear = countTime.substring(0, 4);
                timeMonth = countTime.length() == 7 ? countTime.substring(5) : timeMonth;
            } else {
                countTime = "";
            }

            boolean isError = true;
            // 操作员机构修改，多次点击出现org.apache.catalina.connector.Request.parseParameters异常
            isError = request.getParameter("pageIndex") == null && request.getParameter("pageSize") == null;
            if (!isBack && isError) {
                isFirstEnter = true;
            } else {
                // 设置分页
                pageInfo.setPageSize(getIntParameter("pageSize", pageInfo.getPageSize(), request));
                pageInfo.setPageIndex(getIntParameter("pageIndex", 1, request));
                isFirstEnter = false;
            }

            if (!isFirstEnter) {
                spisuncmMtDataReportVo.setUserid(request.getParameter("userId"));

            }
            spisuncmMtDataReportVo.setReportType(reportType + "");
            //设置年份
            spisuncmMtDataReportVo.setY(timeYear);
            HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
            if (isBack) {

                if (session.getAttribute("spmt_session") != null) {
                    pageInfo = (PageInfo) session.getAttribute("sessionPageInfo");
                    spisuncmMtDataReportVo = (SpMtDataReportVo) session.getAttribute("spmt_session");
                    reportType = Integer.parseInt(spisuncmMtDataReportVo.getReportType());
                }
            }


            List<SpMtDataReportVo> reportList = null;
            if (!isFirstEnter) {
                //获取当前操作员绑定的SP账号
                String currentSpUserId = drp.getlFmtTaskSpuser(lfSysuser.getUserId().toString());
                //获取需要查询的操作员绑定的SP账号
                String spUserId = new SpUserBiz().getAllSpUser(userIdStr);

                StringBuffer spUserIdSb = new StringBuffer();
                //遍历,查询SP账号
                if (StringUtils.isEmpty(userIdStr)) {
                    //如果条件为空,则查询所有的
                    spUserIdSb.append(currentSpUserId + ",");
                } else {
                    for (String tempCurrentSP : currentSpUserId.split(",")) {
                        for (String tempSP : spUserId.split(",")) {
                            if (tempCurrentSP.equalsIgnoreCase(tempSP)) {
                                spUserIdSb.append(tempCurrentSP);
                                spUserIdSb.append(",");
                            }
                        }
                    }
                }
                String realSpUser = spUserIdSb.toString();
                if (!"".equals(realSpUser)) {
                    realSpUser = realSpUser.substring(0, realSpUser.lastIndexOf(","));
                }
                spisuncmMtDataReportVo.setSpUserId(realSpUser);

                if (reportType == 0) {
                    //月报表
                    spisuncmMtDataReportVo.setImonth(timeMonth);
                    reportList = drp.getReportInfosByMonth(spisuncmMtDataReportVo, pageInfo);
                } else if (reportType == 1) {
                    // 年报表
                    spisuncmMtDataReportVo.setImonth(null);
                    reportList = drp.getReportInfosByYear(spisuncmMtDataReportVo, pageInfo);
                } else if (reportType == 2) {
                    //统计时间开始
                    spisuncmMtDataReportVo.setSendTime(sendtime);
                    //统计时间结束
                    spisuncmMtDataReportVo.setEndTime(endtime);
                    //日报表
                    spisuncmMtDataReportVo.setImonth(null);
                    reportList = drp.getReportInfosByDay(spisuncmMtDataReportVo, pageInfo);
                }
                long[] sumcount = drp.findSumCount(spisuncmMtDataReportVo);
                request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("sumCount", sumcount);
            }

            // 大写发送账号
            List<String> user_List = qbiz.getSpUserList("0", corpcode, StaticValue.getCORPTYPE());
            // 大写发送账号
            List<String> mmsuser_List = qbiz.getSpUserList("1", corpcode, StaticValue.getCORPTYPE());

            GlobConfigBiz gcBiz = new GlobConfigBiz();
            List<LfPageField> pagefileds = gcBiz.getPageFieldById("100001");

            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("pagefiledsps", pagefileds);
            request.setAttribute("corpcode", corpcode);
            cTime = timeYear + "-" + timeMonth;
            request.setAttribute("corpcode", corpcode);
            request.setAttribute("reportType", reportType);
            request.setAttribute("spisuncm", spisuncm);
            request.setAttribute("msType", msType);
            request.setAttribute("spUserList", user_List);
            request.setAttribute("mmsUserList", mmsuser_List);

            //业务类型
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            // 获取业务类型
            if (!"100000".equals(corpcode)) {
                // 只显示自定义业务
                conditionMap.put("corpCode&in", "0," + corpcode);
            } else {
                conditionMap.put("corpCode&not in", "1,2");
            }
            busTypeList = baseBiz.getByCondition(LfBusManager.class, conditionMap, null);
            for (LfBusManager businessType : busTypeList) {
                busTypeMap.put(businessType.getBusCode(), businessType.getBusName());
            }
            request.setAttribute("svrTypeMap", busTypeMap);
            //区域
            conditionMap.clear();
            List<AprovinceCity> areaCodeList = baseBiz.getByCondition(AprovinceCity.class, conditionMap, null);
            request.setAttribute("areaCodeList", areaCodeList);

            if (isFirstEnter) {
                pageInfo = new PageInfo();
            }
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("countTime", cTime);
            session.setAttribute("spmt_session", spisuncmMtDataReportVo);
            session.setAttribute("reportList", reportList);
            session.setAttribute("sessionPageInfo", pageInfo);
            request.setAttribute("isFirstEnter", isFirstEnter);

            request.setAttribute("userName", userName);
            request.setAttribute("depName", depName);
            request.setAttribute("depIdStr", depIdStr);
            request.setAttribute("userIdStr", userIdStr);
            request.setAttribute("containSubDep", containSubDep);
            request.setAttribute("araCode", araCode);
            request.setAttribute("svrType", svrType);
            request.setAttribute("domesticOperator", domesticOperator);
            long count = 0L;
            //从pageinfo中获取查询总条数
            if (pageInfo != null) {
                count = pageInfo.getTotalRec();
            }
            // 写日志
            String opContent = "SP账号统计报表查询：" + count + "条 开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            new QueryBiz().setLog(request, "SP账号统计报表", opContent, StaticValue.GET);

        } catch (Exception e) {
            request.setAttribute("countTime", timeYear + "-" + timeMonth);
            // System.out.println(request.getAttribute("countTime"));
            long[] returnLong = new long[2];
            returnLong[0] = 0;
            returnLong[1] = 0;
            request.setAttribute("sumCount", returnLong);
            request.setAttribute("findresult", "-1");
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("reportList", null);
            request.setAttribute("pageInfo", pageInfo);
            EmpExecutionContext.error(e, "SP账号统计报表servlet异常");

        } finally {
            //点击返回时候，返回原来的状态
            if (isBack) {
                String back_url = (String) request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("spmtreport_backURL");
                request.getRequestDispatcher(empRoot + base + "/rep_spMtReport.jsp?" + back_url).forward(request, response);
            } else {
                String url = "lgcorpcode=" + corpcode + "&spisuncm=" + spisuncm + "&begintime=" + sendtime + "&userId=" + request.getParameter("userId") + "&staffname=" + staffname + "&sptype=" + sptype + "&sendtype=" + sendtype + "&endtime=" + endtime + "&reportType=" + reportType + "&msType=" + msType + "&countTime=" + cTime;
                request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("spmtreport_backURL", url);
                request.getRequestDispatcher(empRoot + base + "/rep_spMtReport.jsp?lgcorpcode=" + corpcode).forward(request, response);
            }
        }
    }

    /**
     * SP账号报表(明细报表)
     *
     * @param request
     * @param response
     * @throws Exception
     */
    // excel导出全部数据
    public void r_smRptExportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //起始ms数
        long startl = System.currentTimeMillis();
        //开始时间
        String starthms = hms.format(startl);
        SpMtDataReportVo spisuncmMtDataReportVo = new SpMtDataReportVo();

        try {

            SpMtReportBiz drp = new SpMtReportBiz();
            // 企业编码
            String corpcode = qbiz.getCorpCode(request);
            // 短彩类型  0是短信  1是彩信
            String msType = request.getParameter("msType");
            //年报表/月报表
            String reportTypeStr = request.getParameter("reportType");
            //账号类型
            String sptype = request.getParameter("sptype");
            //发送类型
            String sendtype = request.getParameter("sendtype");
            //账户名称
            String staffname = request.getParameter("staffname");
            if (staffname != null && !"".equals(staffname)) {
                staffname = new String(staffname.getBytes("iso8859-1"), "UTF-8");
            }
            //时间
            String countTime = request.getParameter("countTime");
            //年份
            String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
            //月份
            String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
            //统计时间开始
            String sendtime = request.getParameter("begintime");
            //统计时间结束
            String endtime = request.getParameter("endtime");
            //运营商类型（国内  100（实际对应，0,1,21），国外 5）
            String spisuncm = request.getParameter("spisuncm");
            if (spisuncm == null) {
                spisuncm = "100";//默认为国内
            }
            //短彩信帐号
            String spusers;
            PageInfo pageInfo = new PageInfo();
            if (msType == null) {
                // 默认为短信
                msType = "0";
            }
            //短信彩帐号字符串
            spusers = qbiz.getSpUsers(msType, corpcode, StaticValue.getCORPTYPE());
            //短彩类型
            spisuncmMtDataReportVo.setMstype(Integer.parseInt(msType));
            //企业编码
            spisuncmMtDataReportVo.setCorpCode(corpcode);
            if (sptype != null) {
                //账号类型
                if ("0".equals(sptype)) {
                    //spisuncmMtDataReportVo.setSptypes("1,2,3");
                } else if ("1".equals(sptype)) {
                    spisuncmMtDataReportVo.setSptypes("1");
                } else if ("2".equals(sptype)) {
                    spisuncmMtDataReportVo.setSptypes("2,3");
                }
            }
            //如果是彩信选择EMP发送即查所有不需要条件
            if ("1".equals(msType)) {
                if (sendtype != null && "1".equals(sendtype)) {
                    sendtype = null;
                }
            }
            //发送账户类型
            spisuncmMtDataReportVo.setSendtypes(sendtype);
            //账户名称
            spisuncmMtDataReportVo.setStaffname(staffname);
            //统计时间开始
            spisuncmMtDataReportVo.setSendTime(sendtime);
            //统计时间结束
            spisuncmMtDataReportVo.setEndTime(endtime);

            //运营商类型
            spisuncmMtDataReportVo.setSpisuncm(spisuncm);

            //发送帐号
            if (!"".equals(spusers)) {
                spisuncmMtDataReportVo.setSpUsers(spusers);
            }
            //月份
            if (timeMonth.length() > 2) {
                timeMonth = timeMonth.substring(1);
            }
            //年报表月报表 默认为日报表
            int reportType = 2;
            if (reportTypeStr != null) {
                reportType = Integer.parseInt(reportTypeStr);
            }
            if (!"".equals(countTime) && countTime != null) {
                timeYear = countTime.substring(0, 4);
                timeMonth = countTime.length() == 7 ? countTime.substring(5) : timeMonth;
            } else {
                countTime = "";
            }
            // 设置分页
            pageInfo.setPageSize(getIntParameter("pageSize", pageInfo.getPageSize(), request));
            pageInfo.setPageIndex(getIntParameter("pageIndex", 1, request));
            spisuncmMtDataReportVo.setUserid((request.getParameter("userId")));

            //设置年份
            spisuncmMtDataReportVo.setY(timeYear);

            List<SpMtDataDetailVo> reportList = null;

            spisuncmMtDataReportVo.setReportType(reportType + "");

            if (reportType == 0) {
                spisuncmMtDataReportVo.setImonth(timeMonth);
                reportList = drp.getReportInfoDetail(spisuncmMtDataReportVo, null);
            } else if (reportType == 1) {
                // 年报表
                spisuncmMtDataReportVo.setImonth(null);
                spisuncmMtDataReportVo.setIsYearDetail("true");
                List<SpMtDataReportVo> list = drp.getReportInfosByMonth(spisuncmMtDataReportVo, pageInfo);
                List<SpMtDataDetailVo> vo = new ArrayList<SpMtDataDetailVo>();
                for (int i = 0; i < list.size(); i++) {
                    SpMtDataDetailVo tempvo = new SpMtDataDetailVo();
                    SpMtDataReportVo temp = list.get(i);
                    tempvo.setCorpCode(temp.getCorpCode());
                    tempvo.setEndTime(temp.getEndTime());
                    tempvo.setImonth(temp.getImonth());
                    tempvo.setMstype(tempvo.getMstype());
                    tempvo.setIcount(temp.getIcount());
                    tempvo.setRsucc(temp.getRsucc());
                    tempvo.setRfail1(temp.getRfail1());
                    tempvo.setRfail2(temp.getRfail2());
                    tempvo.setRnret(temp.getRnret());
                    tempvo.setSendTime(temp.getSendTime());
                    tempvo.setSendtype(temp.getSendtype());
                    tempvo.setSendtypes(temp.getSendtypes());
                    tempvo.setSpUsers(temp.getSpUsers());
                    tempvo.setSptype(temp.getSptype());
                    tempvo.setSptypes(temp.getSptypes());
                    tempvo.setStaffname(temp.getStaffname());
                    tempvo.setUserid(temp.getUserid());
                    tempvo.setY(temp.getY());
                    tempvo.setIymd(temp.getIymd());
                    vo.add(tempvo);
                }
                reportList = vo;
            } else if (reportType == 2) {
                //日报表
                spisuncmMtDataReportVo.setImonth(null);
                reportList = drp.getReportInfoDetail(spisuncmMtDataReportVo, null);
            }


            long[] sum = (long[]) request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("sumCount");
            // 获取session存储的 成功数失败数和时间字符串
            CountReportVo crptvo = new CountReportVo();
            crptvo.setIcount(sum[0]);
            crptvo.setRsucc(sum[1]);
            crptvo.setRfail1(sum[2]);
            crptvo.setRfail2(sum[3]);
            crptvo.setRnret(sum[4]);

            response.setContentType("html/text");
            PrintWriter out = response.getWriter();
            if (reportList != null && reportList.size() > 0) {

                Map<String, String> resultMap = drp.createSpMtReportExcelV1(reportList, reportType, crptvo, request);
                String fileName = (String) resultMap.get("FILE_NAME");
                // 写日志
                String opContent = "导出SP账号统计报表详情：" + reportList.size() + "条  ,文件名：" + fileName + ",开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
                new QueryBiz().setLog(request, "SP账号统计报表详情", opContent, StaticValue.GET);
                reportList.clear();
                reportList = null;
                HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
                session.setAttribute("repsp_export_map", resultMap);
                out.print("true");
//				String filePath = (String) resultMap.get("FILE_PATH");
//				DownloadFile df = new DownloadFile();
//				df.downFile(request, response, filePath, fileName);
            } else {
                out.print("false");
            }

        } catch (Exception e) {
            // 异常处理
            EmpExecutionContext.error(e, "SP账号统计报表导出异常");
        }
    }


    /**
     * SP帐号下载导出文件
     *
     * @param request
     * @param response
     */
    public void detaildownloadFile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("repsp_export_map");
        session.removeAttribute("repsp_export_map");
        if (obj != null) {
            // 弹出下载页面。
            DownloadFile dfs = new DownloadFile();
            Map<String, String> resultMap = (Map<String, String>) obj;
            String fileName = (String) resultMap.get("FILE_NAME");
            String filePath = (String) resultMap.get("FILE_PATH");
            dfs.downFile(request, response, filePath, fileName);
        }
    }


    /**
     * SP账号报表
     *
     * @param request
     * @param response
     * @throws Exception
     */
    // excel导出全部数据
    public void r_smRptSPExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //起始ms数
        long startl = System.currentTimeMillis();
        //开始时间
        String starthms = hms.format(startl);
        List<SpMtDataReportVo> mdlist = null;

        SpMtDataReportVo spisuncmMtDataReportVo = new SpMtDataReportVo();

        try {

            Integer reportType = null == request.getParameter("reportType") ? 0 : Integer.parseInt(request.getParameter("reportType").toString());

            spisuncmMtDataReportVo = (SpMtDataReportVo) request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("spmt_session");

            SpMtReportBiz drp = new SpMtReportBiz();
            switch (reportType) {
                case 0:// 月报表
                    mdlist = drp.getReportInfosByMonth(spisuncmMtDataReportVo, null);
                    break;
                case 1:// 年报表
                    mdlist = drp.getReportInfosByYear(spisuncmMtDataReportVo, null);
                    break;
                case 2:// 日报表
                    mdlist = drp.getReportInfosByDay(spisuncmMtDataReportVo, null);
                    break;
                default:
                    break;

            }

            long[] sum = (long[]) request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("sumCount");
            // 获取session存储的 成功数失败数和时间字符串
            CountReportVo crptvo = new CountReportVo();
            crptvo.setIcount(sum[0]);
            crptvo.setRsucc(sum[1]);
            crptvo.setRfail1(sum[2]);
            crptvo.setRfail2(sum[3]);
            crptvo.setRnret(sum[4]);
            response.setContentType("html/text");
            PrintWriter out = response.getWriter();
            if (mdlist != null && mdlist.size() > 0) {

                Map<String, String> resultMap = drp.createSpExcelV1(spisuncmMtDataReportVo, mdlist, crptvo, request);
                String fileName = (String) resultMap.get("FILE_NAME");
                // 写日志
                String opContent = "导出SP账号统计报表：" + mdlist.size() + "条  ,文件名：" + fileName + ",开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
                new QueryBiz().setLog(request, "SP账号统计报表", opContent, StaticValue.GET);
                mdlist.clear();
                mdlist = null;
                HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
                session.setAttribute("repsp_export_map", resultMap);
                out.print("true");
//				String filePath = (String) resultMap.get("FILE_PATH");
//				DownloadFile df = new DownloadFile();
//				df.downFile(request, response, filePath, fileName);
            } else {
                out.print("false");
            }

        } catch (Exception e) {
            // 异常处理
            EmpExecutionContext.error(e, "SP账号统计报表导出异常");
        }
    }

    /**
     * SP帐号下载导出文件
     *
     * @param request
     * @param response
     */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("repsp_export_map");
        session.removeAttribute("repsp_export_map");
        if (obj != null) {
            // 弹出下载页面。
            DownloadFile dfs = new DownloadFile();
            Map<String, String> resultMap = (Map<String, String>) obj;
            String fileName = (String) resultMap.get("FILE_NAME");
            String filePath = (String) resultMap.get("FILE_PATH");
            dfs.downFile(request, response, filePath, fileName);
        }
    }

    /**
     * 年，月，日的详细信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void detailInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户信息
        LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY);
        //起始ms数
        long startl = System.currentTimeMillis();
        //开始时间
        String starthms = hms.format(startl);
        // 企业编码
        String corpcode = qbiz.getCorpCode(request);
        // 短彩类型  0是短信  1是彩信
        String msType = request.getParameter("msType");
        //年报表/月报表
        String reportTypeStr = request.getParameter("reportType");
        //账号类型
        String sptype = request.getParameter("sptype");
        //发送类型
        String sendtype = request.getParameter("sendtype");
        //账户名称
        String staffname = request.getParameter("staffname");
        //时间
        String countTime = request.getParameter("countTime");
        //年份
        String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
        //月份
        String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
        //统计时间开始
        String sendtime = request.getParameter("begintime");
        //统计时间结束
        String endtime = request.getParameter("endtime");
        //运营商类型（国内  100（实际对应，0,1,21），国外 5）
        String spisuncm = request.getParameter("spisuncm");
        if (spisuncm == null) {
            spisuncm = "100";//默认为国内
        }
        //短彩信帐号
        String spusers;
        PageInfo pageInfo = new PageInfo();
        SpMtReportBiz drp = new SpMtReportBiz();
        if (staffname != null && !"".equals(staffname)) {
            staffname = URLDecoder.decode(staffname, "UTF-8");
        }

        String url = "";
        try {
            if (msType == null) {
                // 默认为短信
                msType = "0";
            }
            //短信彩帐号字符串
            spusers = qbiz.getSpUsers(msType, corpcode, StaticValue.getCORPTYPE());
            //查询条件对象
            SpMtDataReportVo spisuncmMtDataReportVo = new SpMtDataReportVo();
            //短彩类型
            spisuncmMtDataReportVo.setMstype(Integer.parseInt(msType));
            //企业编码
            spisuncmMtDataReportVo.setCorpCode(corpcode);

            //运营商类型
            spisuncmMtDataReportVo.setSpisuncm(spisuncm);
            if (sptype != null) {
                //账号类型
                if ("0".equals(sptype)) {
                    //spisuncmMtDataReportVo.setSptypes("1,2,3");
                } else if ("1".equals(sptype)) {
                    spisuncmMtDataReportVo.setSptypes("1");
                } else if ("2".equals(sptype)) {
                    spisuncmMtDataReportVo.setSptypes("2,3");
                }
            }
            //如果是彩信选择EMP发送即查所有不需要条件
            if ("1".equals(msType)) {
                if (sendtype != null && "1".equals(sendtype)) {
                    sendtype = null;
                }
            }
            //发送账户类型
            spisuncmMtDataReportVo.setSendtypes(sendtype);
            //账户名称 详情不需要用账户名称作为查询条件
            //spisuncmMtDataReportVo.setStaffname(staffname);
            //统计时间开始
            spisuncmMtDataReportVo.setSendTime(sendtime);
            //统计时间结束
            spisuncmMtDataReportVo.setEndTime(endtime);
            //获取当前操作员ID
            spisuncmMtDataReportVo.setCurrentUserId(lfSysuser.getUserId().toString());

            //将页面传来的操作员与机构信息转换为操作员编码
            spisuncmMtDataReportVo.setPriviligeUserCode(lfSysuser.getUserCode());

            //发送帐号
            if (!"".equals(spusers)) {
                spisuncmMtDataReportVo.setSpUsers(spusers);
            }
            //月份
            if (timeMonth.length() > 2) {
                timeMonth = timeMonth.substring(1);
            }
            //年报表月报表 默认为日报表
            int reportType = 2;
            if (reportTypeStr != null) {
                reportType = Integer.parseInt(reportTypeStr);
            }
            if (!"".equals(countTime) && countTime != null) {
                timeYear = countTime.substring(0, 4);
                timeMonth = countTime.length() == 7 ? countTime.substring(5) : timeMonth;
            } else {
                countTime = "";
            }
            boolean isError = request.getParameter("pageIndex") == null && request.getParameter("pageSize") == null;
            if (!isError) {
                // 设置分页
                pageInfo.setPageSize(getIntParameter("pageSize", pageInfo.getPageSize(), request));
                pageInfo.setPageIndex(getIntParameter("pageIndex", 1, request));
            }

            spisuncmMtDataReportVo.setUserid(request.getParameter("userId"));

            //设置年份
            spisuncmMtDataReportVo.setY(timeYear);

            List<SpMtDataDetailVo> reportList = null;

            spisuncmMtDataReportVo.setReportType(reportType + "");

            if (reportType == 0) {
                spisuncmMtDataReportVo.setImonth(timeMonth);
                reportList = drp.getReportInfoDetail(spisuncmMtDataReportVo, pageInfo);
            } else if (reportType == 1) {
                // 年报表
                spisuncmMtDataReportVo.setImonth(null);
                spisuncmMtDataReportVo.setIsYearDetail("true");
                List<SpMtDataReportVo> list = drp.getReportInfosByMonth(spisuncmMtDataReportVo, pageInfo);
                List<SpMtDataDetailVo> vo = new ArrayList<SpMtDataDetailVo>();
                for (int i = 0; i < list.size(); i++) {
                    SpMtDataDetailVo tempvo = new SpMtDataDetailVo();
                    SpMtDataReportVo temp = list.get(i);
                    tempvo.setCorpCode(temp.getCorpCode());
                    tempvo.setEndTime(temp.getEndTime());
                    tempvo.setImonth(temp.getImonth());
                    tempvo.setMstype(tempvo.getMstype());
                    tempvo.setIcount(temp.getIcount());
                    tempvo.setRsucc(temp.getRsucc());
                    tempvo.setRfail1(temp.getRfail1());
                    tempvo.setRfail2(temp.getRfail2());
                    tempvo.setRnret(temp.getRnret());
                    tempvo.setSendTime(temp.getSendTime());
                    tempvo.setSendtype(temp.getSendtype());
                    tempvo.setSendtypes(temp.getSendtypes());
                    tempvo.setSpUsers(temp.getSpUsers());
                    tempvo.setSptype(temp.getSptype());
                    tempvo.setSptypes(temp.getSptypes());
                    tempvo.setStaffname(temp.getStaffname());
                    tempvo.setUserid(temp.getUserid());
                    tempvo.setY(temp.getY());
                    tempvo.setIymd(temp.getIymd());
                    vo.add(tempvo);
                }
                reportList = vo;

            } else if (reportType == 2) {
                //日报表
                spisuncmMtDataReportVo.setImonth(null);
                reportList = drp.getReportInfoDetail(spisuncmMtDataReportVo, pageInfo);
            }

            // 大写发送账号
            List<String> user_List = qbiz.getSpUserList("0", corpcode, StaticValue.getCORPTYPE());
            // 大写发送账号
            List<String> mmsuser_List = qbiz.getSpUserList("1", corpcode, StaticValue.getCORPTYPE());

            GlobConfigBiz gcBiz = new GlobConfigBiz();
            List<LfPageField> pagefileds = gcBiz.getPageFieldById("100001");

            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("pagefiledsps", pagefileds);
            request.setAttribute("corpcode", corpcode);

            request.setAttribute("corpcode", corpcode);
            request.setAttribute("reportType", reportType);

            request.setAttribute("msType", msType);
            request.setAttribute("spUserList", user_List);
            request.setAttribute("mmsUserList", mmsuser_List);
            long[] sumcount = drp.findSumCount(spisuncmMtDataReportVo);
//            if(sumcount == null || sumcount[0] + sumcount[1] + sumcount[2] + sumcount[3] + sumcount[4] == 0)
//
//			{
//				reportList=new ArrayList<SpMtDataDetailVo>();
//				pageInfo=new PageInfo();
//			}
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("sumCount", sumcount);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("countTime", timeYear + "-" + timeMonth);
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("reportList", reportList);
            //**************用于后退时候，传入界面上的值****************
//			String back_staffname=request.getParameter("back_staffname")==null?"":request.getParameter("back_staffname");
//			String back_userId=request.getParameter("back_userId")==null?"":request.getParameter("back_userId");
//			String back_sptype=request.getParameter("back_sptype")==null?"":request.getParameter("back_sptype"); 
//			String back_sendtype=request.getParameter("back_sendtype")==null?"":request.getParameter("back_sendtype");
//			if(back_staffname!=null&&!"".equals(back_staffname)){
//				back_staffname = URLDecoder.decode(back_staffname, "UTF-8");
//			}
            url = "lgcorpcode=" + corpcode + "&spisuncm=" + spisuncm + "&begintime=" + sendtime + "&userId=" + request.getParameter("userId") + "&staffname=" + staffname + "&sptype=" + sptype + "&sendtype=" + sendtype + "&endtime=" + endtime + "&reportType=" + reportType + "&msType=" + msType + "&countTime=" + countTime;
//			url=url+"&back_staffname="+back_staffname+"&back_userId="+back_userId+"&back_sptype="+back_sptype+"&back_sendtype="+back_sendtype;
            request.setAttribute("excelURL", url);

            long count = 0l;
            //从pageinfo中获取查询总条数
            if (pageInfo != null) {
                count = pageInfo.getTotalRec();
            }
            // 写日志
            String opContent = "SP账号统计报表详情查询：" + count + "条 开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            new QueryBiz().setLog(request, "SP账号统计报表详情", opContent, StaticValue.GET);


        } catch (Exception e) {
            request.setAttribute("countTime", timeYear + "-" + timeMonth);
            // System.out.println(request.getAttribute("countTime"));
            long[] returnLong = new long[2];
            returnLong[0] = 0;
            returnLong[1] = 0;
            request.setAttribute("sumCount", returnLong);
            request.setAttribute("findresult", "-1");
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("reportList", null);
            request.setAttribute("pageInfo", pageInfo);
            EmpExecutionContext.error(e, "SP账号统计报表servlet异常");

        } finally {
            request.getRequestDispatcher(empRoot + base + "/rep_spMtDetal.jsp?" + url).forward(request, response);
        }
    }


    /**
     * SP账号统计报表--各国发送（详情处理）
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void nationSendInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //起始ms数
        long startl = System.currentTimeMillis();
        //开始时间
        String starthms = hms.format(startl);
        // 企业编码
        String corpcode = qbiz.getCorpCode(request);
        // 短彩类型  0是短信  1是彩信
        String msType = request.getParameter("msType");
        //年报表/月报表
        String reportTypeStr = request.getParameter("reportType");
        //账号类型
        String sptype = request.getParameter("sptype");
        //发送类型
        String sendtype = request.getParameter("sendtype");
        //账户名称
        String staffname = request.getParameter("staffname");
        //时间
        String countTime = request.getParameter("countTime");
        //年份
        String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
        //月份
        String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
        //统计时间开始
        String sendtime = request.getParameter("begintime");
        //统计时间结束
        String endtime = request.getParameter("endtime");
        //国家代码
        String nationCode = request.getParameter("nationCode");
        //国家名称
        String nationName = request.getParameter("nationName");
        //运营商类型（国内  100（实际对应，0,1,21），国外 5）
        String spisuncm = request.getParameter("spisuncm");
        if (spisuncm == null) {
            spisuncm = "100";//默认为国内
        }
        //由于汉字传递解码不一致
        if ("1".equals(request.getParameter("web"))) {
            if (staffname != null && !"".equals(staffname)) {
                staffname = new String(staffname.getBytes("iso8859-1"), "UTF-8");
            }
        } else {
            if (staffname != null && !"".equals(staffname)) {
                staffname = URLDecoder.decode(staffname, "UTF-8");
            }
        }


        if (nationName != null && !"".equals(nationName)) {
            nationName = URLDecoder.decode(nationName, "UTF-8");
            nationName = nationName.trim();
        }
        String spusers;
        PageInfo pageInfo = new PageInfo();
        SpMtReportBiz drp = new SpMtReportBiz();
        String url = "";
        try {
            if (msType == null) {
                // 默认为短信
                msType = "0";
            }
            //短信彩帐号字符串
            spusers = qbiz.getSpUsers(msType, corpcode, StaticValue.getCORPTYPE());
            //查询条件对象
            SpMtDataReportVo spisuncmMtDataReportVo = new SpMtDataReportVo();
            //短彩类型
            spisuncmMtDataReportVo.setMstype(Integer.parseInt(msType));
            //企业编码
            spisuncmMtDataReportVo.setCorpCode(corpcode);
            //运营商类型
            spisuncmMtDataReportVo.setSpisuncm(spisuncm);
            if (sptype != null) {
                //账号类型
                if ("0".equals(sptype)) {
                    //spisuncmMtDataReportVo.setSptypes("1,2,3");
                } else if ("1".equals(sptype)) {
                    spisuncmMtDataReportVo.setSptypes("1");
                } else if ("2".equals(sptype)) {
                    spisuncmMtDataReportVo.setSptypes("2,3");
                }
            }
            //如果是彩信选择EMP发送即查所有不需要条件
            if ("1".equals(msType)) {
                if (sendtype != null && "1".equals(sendtype)) {
                    sendtype = null;
                }
            }
            //
            spisuncmMtDataReportVo.setNationcode(nationCode);
            //国家名称
            spisuncmMtDataReportVo.setNationname(nationName);

            //发送账户类型
            spisuncmMtDataReportVo.setSendtypes(sendtype);
            //账户名称
            spisuncmMtDataReportVo.setStaffname(staffname);
            //统计时间开始
            spisuncmMtDataReportVo.setSendTime(sendtime);
            //统计时间结束
            spisuncmMtDataReportVo.setEndTime(endtime);

            //发送帐号
            if (!"".equals(spusers)) {
                spisuncmMtDataReportVo.setSpUsers(spusers);
            }
            //月份
            if (timeMonth.length() > 2) {
                timeMonth = timeMonth.substring(1);
            }
            //年报表月报表 默认为日报表
            int reportType = 2;
            if (reportTypeStr != null) {
                reportType = Integer.parseInt(reportTypeStr);
            }
            if (!"".equals(countTime) && countTime != null) {
                timeYear = countTime.substring(0, 4);
                timeMonth = countTime.length() == 7 ? countTime.substring(5) : timeMonth;
            } else {
                countTime = "";
            }
            boolean isError = request.getParameter("pageIndex") == null && request.getParameter("pageSize") == null;
            if (!isError) {
                // 设置分页
                pageInfo.setPageSize(getIntParameter("pageSize", pageInfo.getPageSize(), request));
                pageInfo.setPageIndex(getIntParameter("pageIndex", 1, request));
            }
            spisuncmMtDataReportVo.setUserid(request.getParameter("userId"));

            //设置年份
            spisuncmMtDataReportVo.setY(timeYear);

            List<SpMtDataNationVo> reportList = null;

            spisuncmMtDataReportVo.setReportType(reportType + "");

            if (reportType == 0) {
                spisuncmMtDataReportVo.setImonth(timeMonth);
                reportList = drp.getReportInfoNation(spisuncmMtDataReportVo, pageInfo);
            } else if (reportType == 1) {
                // 年报表
                spisuncmMtDataReportVo.setImonth(null);
                spisuncmMtDataReportVo.setIsYearDetail("true");
                List<SpMtNationVo> list = drp.getReportInfosByMonthNation(spisuncmMtDataReportVo, pageInfo);
                List<SpMtDataNationVo> vo = new ArrayList<SpMtDataNationVo>();
                for (int i = 0; i < list.size(); i++) {
                    SpMtDataNationVo tempvo = new SpMtDataNationVo();
                    SpMtNationVo temp = list.get(i);
                    tempvo.setCorpCode(temp.getCorpCode());
                    tempvo.setEndTime(temp.getEndTime());
                    tempvo.setImonth(temp.getImonth());
                    tempvo.setMstype(tempvo.getMstype());
                    tempvo.setIcount(temp.getIcount());
                    tempvo.setRsucc(temp.getRsucc());
                    tempvo.setRfail1(temp.getRfail1());
                    tempvo.setRfail2(temp.getRfail2());
                    tempvo.setRnret(temp.getRnret());
                    tempvo.setSendTime(temp.getSendTime());
                    tempvo.setSendtype(temp.getSendtype());
                    tempvo.setSendtypes(temp.getSendtypes());
                    tempvo.setSpUsers(temp.getSpUsers());
                    tempvo.setSptype(temp.getSptype());
                    tempvo.setSptypes(temp.getSptypes());
                    tempvo.setStaffname(temp.getStaffname());
                    tempvo.setUserid(temp.getUserid());
                    tempvo.setY(temp.getY());
                    //国家代码，做为传入的查询条件
                    tempvo.setNationcode(temp.getNationcode());
                    //国家名称
                    tempvo.setNationname(temp.getNationname());
                    //通道号码
                    tempvo.setSpgatecode(temp.getSpgatecode());
                    //通道名称
                    tempvo.setSpgatename(temp.getSpgatename());
                    vo.add(tempvo);
                }
                reportList = vo;

            } else if (reportType == 2) {
                //日报表
                spisuncmMtDataReportVo.setImonth(null);
                reportList = drp.getReportInfoNation(spisuncmMtDataReportVo, pageInfo);
            }

            // 大写发送账号
            List<String> user_List = qbiz.getSpUserList("0", corpcode, StaticValue.getCORPTYPE());
            // 大写发送账号
            List<String> mmsuser_List = qbiz.getSpUserList("1", corpcode, StaticValue.getCORPTYPE());

            GlobConfigBiz gcBiz = new GlobConfigBiz();
            List<LfPageField> pagefileds = gcBiz.getPageFieldById("100001");

            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("pagefiledsps", pagefileds);
            request.setAttribute("corpcode", corpcode);

            request.setAttribute("corpcode", corpcode);
            request.setAttribute("reportType", reportType);

            request.setAttribute("msType", msType);
            request.setAttribute("spUserList", user_List);
            request.setAttribute("mmsUserList", mmsuser_List);
            long[] sumcount = drp.findSumCountNation(spisuncmMtDataReportVo);
//            if(sumcount == null || sumcount[0] + sumcount[1] + sumcount[2] + sumcount[3] + sumcount[4] == 0)
//			{
//				reportList = new ArrayList<SpMtDataNationVo>();
//				pageInfo = new PageInfo();
//			}
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("sumCount", sumcount);
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("countTime", timeYear + "-" + timeMonth);
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("spisuncmMtDataReportVo", spisuncmMtDataReportVo);
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("reportList", reportList);
            //**************用于后退时候，传入界面上的值****************
//			String back_staffname=request.getParameter("back_staffname")==null?"":request.getParameter("back_staffname");
//			String back_userId=request.getParameter("back_userId")==null?"":request.getParameter("back_userId");
//			String back_sptype=request.getParameter("back_sptype")==null?"":request.getParameter("back_sptype"); 
//			String back_sendtype=request.getParameter("back_sendtype")==null?"":request.getParameter("back_sendtype");
//			
//			//由于汉字传递解码不一致
//			if("1".equals(request.getParameter("web"))){
//				if(back_staffname!=null&&!"".equals(back_staffname)){
//					back_staffname = new String(back_staffname.getBytes("iso8859-1"), "UTF-8");
//				}
//			}else{
//				if(back_staffname!=null&&!"".equals(back_staffname)){
//					back_staffname = URLDecoder.decode(back_staffname, "UTF-8");
//				}
//			}


            url = "lgcorpcode=" + corpcode + "&spisuncm=" + spisuncm + "&begintime=" + sendtime + "&userId=" + request.getParameter("userId") + "&staffname=" + staffname + "&sptype=" + sptype + "&sendtype=" + sendtype + "&endtime=" + endtime + "&reportType=" + reportType + "&msType=" + msType + "&countTime=" + countTime;
//			url=url+"&back_staffname="+back_staffname+"&back_userId="+back_userId+"&back_sptype="+back_sptype+"&back_sendtype="+back_sendtype;
            request.setAttribute("excelURL", url);
            long count = 0l;
            //从pageinfo中获取查询总条数
            if (pageInfo != null) {
                count = pageInfo.getTotalRec();
            }
            // 写日志
            String opContent = "SP账号统计报表国家详情查询：" + count + "条 开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            new QueryBiz().setLog(request, "SP账号统计报表国家详情", opContent, StaticValue.GET);


        } catch (Exception e) {
            request.setAttribute("countTime", timeYear + "-" + timeMonth);
            // System.out.println(request.getAttribute("countTime"));
            long[] returnLong = new long[2];
            returnLong[0] = 0;
            returnLong[1] = 0;
            request.setAttribute("sumCount", returnLong);
            request.setAttribute("findresult", "-1");
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("reportList", null);
            request.setAttribute("pageInfo", pageInfo);
            EmpExecutionContext.error(e, "SP账号统计报表servlet异常");

        } finally {
            request.getRequestDispatcher(empRoot + base + "/rep_spMtDetalNation.jsp?" + url).forward(request, response);
        }


    }


    /**
     * 各国发送导出EXCEL
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void nationExportExcel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //起始ms数
        long startl = System.currentTimeMillis();
        //开始时间
        String starthms = hms.format(startl);
        // 企业编码
        String corpcode = qbiz.getCorpCode(request);
        // 短彩类型  0是短信  1是彩信
        String msType = request.getParameter("msType");
        //年报表/月报表
        String reportTypeStr = request.getParameter("reportType");
        //账号类型
        String sptype = request.getParameter("sptype");
        //发送类型
        String sendtype = request.getParameter("sendtype");
        //账户名称
        String staffname = request.getParameter("staffname");
        if (staffname != null && !"".equals(staffname)) {
            staffname = new String(staffname.getBytes("iso8859-1"), "UTF-8");
        }
        //时间
        String countTime = request.getParameter("countTime");
        //年份
        String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
        //月份
        String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
        //统计时间开始
        String sendtime = request.getParameter("begintime");
        //统计时间结束
        String endtime = request.getParameter("endtime");
        //国家代码
        String nationCode = request.getParameter("nationCode");
        //国家名称
        String nationName = request.getParameter("nationName");
//		if(nationName!=null&&!"".equals(nationName)){
//			nationName = URLDecoder.decode(nationName, "UTF-8");
//		}
        //运营商类型（国内  100（实际对应，0,1,21），国外 5）
        String spisuncm = request.getParameter("spisuncm");
        if (spisuncm == null) {
            spisuncm = "100";//默认为国内
        }
        String spusers;
        SpMtReportBiz drp = new SpMtReportBiz();
        //年报表月报表 默认为日报表
        int reportType = 2;
        List<SpMtDataNationVo> reportList = null;
        try {
            if (msType == null) {
                // 默认为短信
                msType = "0";
            }
            //短信彩帐号字符串
            spusers = qbiz.getSpUsers(msType, corpcode, StaticValue.getCORPTYPE());
            //查询条件对象
            SpMtDataReportVo spisuncmMtDataReportVo = new SpMtDataReportVo();
            //短彩类型
            spisuncmMtDataReportVo.setMstype(Integer.parseInt(msType));
            //企业编码
            spisuncmMtDataReportVo.setCorpCode(corpcode);
            if (sptype != null) {
                //账号类型
                if ("0".equals(sptype)) {
                    //spisuncmMtDataReportVo.setSptypes("1,2,3");
                } else if ("1".equals(sptype)) {
                    spisuncmMtDataReportVo.setSptypes("1");
                } else if ("2".equals(sptype)) {
                    spisuncmMtDataReportVo.setSptypes("2,3");
                }
            }
            //如果是彩信选择EMP发送即查所有不需要条件
            if ("1".equals(msType)) {
                if (sendtype != null && "1".equals(sendtype)) {
                    sendtype = null;
                }
            }
            //
            spisuncmMtDataReportVo.setNationcode(nationCode);
            //国家名称
            spisuncmMtDataReportVo.setNationname(nationName);

            //发送账户类型
            spisuncmMtDataReportVo.setSendtypes(sendtype);
            //账户名称
            spisuncmMtDataReportVo.setStaffname(staffname);
            //统计时间开始
            spisuncmMtDataReportVo.setSendTime(sendtime);
            //统计时间结束
            spisuncmMtDataReportVo.setEndTime(endtime);
            //运营商类型
            spisuncmMtDataReportVo.setSpisuncm(spisuncm);

            //发送帐号
            if (!"".equals(spusers)) {
                spisuncmMtDataReportVo.setSpUsers(spusers);
            }
            //月份
            if (timeMonth.length() > 2) {
                timeMonth = timeMonth.substring(1);
            }

            if (reportTypeStr != null) {
                reportType = Integer.parseInt(reportTypeStr);
            }
            if (!"".equals(countTime) && countTime != null) {
                timeYear = countTime.substring(0, 4);
                timeMonth = countTime.length() == 7 ? countTime.substring(5) : timeMonth;
            } else {
                countTime = "";
            }

            spisuncmMtDataReportVo.setUserid((request.getParameter("userId")));

            //设置年份
            spisuncmMtDataReportVo.setY(timeYear);


            spisuncmMtDataReportVo.setReportType(reportType + "");

            if (reportType == 0) {
                spisuncmMtDataReportVo.setImonth(timeMonth);
                reportList = drp.getReportInfoNation(spisuncmMtDataReportVo, null);
            } else if (reportType == 1) {
                // 年报表
                spisuncmMtDataReportVo.setImonth(null);
                spisuncmMtDataReportVo.setIsYearDetail("true");
                List<SpMtNationVo> list = drp.getReportInfosByMonthNation(spisuncmMtDataReportVo, null);
                List<SpMtDataNationVo> vo = new ArrayList<SpMtDataNationVo>();
                for (int i = 0; i < list.size(); i++) {
                    SpMtDataNationVo tempvo = new SpMtDataNationVo();
                    SpMtNationVo temp = list.get(i);
                    tempvo.setCorpCode(temp.getCorpCode());
                    tempvo.setEndTime(temp.getEndTime());
                    tempvo.setImonth(temp.getImonth());
                    tempvo.setMstype(tempvo.getMstype());
                    tempvo.setIcount(temp.getIcount());
                    tempvo.setRsucc(temp.getRsucc());
                    tempvo.setRfail1(temp.getRfail1());
                    tempvo.setRfail2(temp.getRfail2());
                    tempvo.setRnret(temp.getRnret());
                    tempvo.setSendTime(temp.getSendTime());
                    tempvo.setSendtype(temp.getSendtype());
                    tempvo.setSendtypes(temp.getSendtypes());
                    tempvo.setSpUsers(temp.getSpUsers());
                    tempvo.setSptype(temp.getSptype());
                    tempvo.setSptypes(temp.getSptypes());
                    tempvo.setStaffname(temp.getStaffname());
                    tempvo.setUserid(temp.getUserid());
                    tempvo.setY(temp.getY());
                    //国家代码，做为传入的查询条件
                    tempvo.setNationcode(temp.getNationcode());
                    //国家名称
                    tempvo.setNationname(temp.getNationname());
                    //通道号码
                    tempvo.setSpgatecode(temp.getSpgatecode());
                    //通道名称
                    tempvo.setSpgatename(temp.getSpgatename());
                    vo.add(tempvo);
                }
                reportList = vo;

            } else if (reportType == 2) {
                //日报表
                spisuncmMtDataReportVo.setImonth(null);
                reportList = drp.getReportInfoNation(spisuncmMtDataReportVo, null);
            }
            long[] sum = (long[]) request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("sumCount");
            // 获取session存储的 成功数失败数和时间字符串
            CountReportVo crptvo = new CountReportVo();
            crptvo.setIcount(sum[0]);
            crptvo.setRsucc(sum[1]);
            crptvo.setRfail1(sum[2]);
            crptvo.setRfail2(sum[3]);
            crptvo.setRnret(sum[4]);

            response.setContentType("html/text");
            PrintWriter out = response.getWriter();
            if (reportList != null && reportList.size() > 0) {

                Map<String, String> resultMap = drp.createSpMtReportNationExcelV1(reportList, sendtime, endtime, reportType, crptvo, request);
                String fileName = (String) resultMap.get("FILE_NAME");
                // 写日志
                String opContent = "导出SP账号各国详情统计报表：" + reportList.size() + "条  ,文件名：" + fileName + ",开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
                new QueryBiz().setLog(request, "SP账号各国详情统计报表", opContent, StaticValue.GET);
                reportList.clear();
                reportList = null;
                HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
                session.setAttribute("repnation_export_map", resultMap);
                out.print("true");
//				String filePath = (String) resultMap.get("FILE_PATH");
//				DownloadFile df = new DownloadFile();
//				df.downFile(request, response, filePath, fileName);
            } else {
                out.print("false");
            }


        } catch (Exception e) {
            request.setAttribute("countTime", timeYear + "-" + timeMonth);
            // System.out.println(request.getAttribute("countTime"));
            long[] returnLong = new long[2];
            returnLong[0] = 0;
            returnLong[1] = 0;
            request.setAttribute("sumCount", returnLong);
            request.setAttribute("findresult", "-1");
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("reportList", null);
            EmpExecutionContext.error(e, "导出SP账号各国详情统计报表异常");

        }


    }


    /**
     * 国家详情下载导出文件
     *
     * @param request
     * @param response
     */
    public void nationdownloadFile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("repnation_export_map");
        session.removeAttribute("repnation_export_map");
        if (obj != null) {
            // 弹出下载页面。
            DownloadFile dfs = new DownloadFile();
            Map<String, String> resultMap = (Map<String, String>) obj;
            String fileName = (String) resultMap.get("FILE_NAME");
            String filePath = (String) resultMap.get("FILE_PATH");
            dfs.downFile(request, response, filePath, fileName);
        }
    }

}
