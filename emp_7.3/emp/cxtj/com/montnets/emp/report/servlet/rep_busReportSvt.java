package com.montnets.emp.report.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobConfigBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.system.LfPageField;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.query.biz.QueryBiz;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.report.biz.BusReportBiz;
import com.montnets.emp.report.vo.BusNationtVo;
import com.montnets.emp.report.vo.BusReportVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;

/**
 * 业务类型统计报表
 *
 * @功能概要：
 * @项目名称： p_cxtj
 * @初创作者： liuxiangheng <xiaokanrensheng1012@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2015-12-23 下午05:27:25
 * <p>修改记录1：</p>
 * <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */

@SuppressWarnings("serial")
public class rep_busReportSvt extends BaseServlet {
    /**
     * 模块名称
     */
    private final String empRoot = "cxtj";
    /**
     * 功能文件夹名
     */
    private final String base = "/report";

    /**
     * 时分秒格式化
     */
    private final SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");
    private final QueryBiz qBiz = new QueryBiz();
    /**
     * 业务类型报表biz
     */
    private final BusReportBiz busReportBiz = new BusReportBiz();

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

    /**
     * 业务类型统计报表
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @description
     * @author liuxiangheng <xiaokanrensheng1012@126.com>
     * @datetime 2015-12-23 下午05:27:37
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LfSysuser user = qBiz.getCurrentUser(request);
        //没有机构权限
/*		if(user == null || user.getPermissionType()!=2){
			request.getRequestDispatcher("/common/no_dep_permission.jsp").forward(
					request, response);
		}*/
        //起始ms数
        long startl = System.currentTimeMillis();
        //开始时间
        String starthms = hms.format(startl);
        // 获取系统session
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        // 短彩类型
        String msType = request.getParameter("msType");
        //数据源
        String datasourcetype = request.getParameter("datasourcetype");
        // 企业编码
        String lgcorpcode = qBiz.getCorpCode(request);
        //年报表/月报表
        String reportTypeStr = request.getParameter("reportType");
        //年份
        String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
        //时间
        String countTime = request.getParameter("countTime");
        //是否返回
        boolean isBack = request.getParameter("isback") != null;
        //月份
        String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
        List<BusReportVo> busreportList = null;
        boolean isFirstEnter = false;
        PageInfo pageInfo = new PageInfo();
        //业务类型
        String bustype = request.getParameter("bustype");
        //年报表月报表 默认为日报表
        int reportType = 2;
        //运营商类型（国内  100（实际对应，0,1,21），国外 5）
        String spisuncm = request.getParameter("spisuncm");
        //开始时间
        String begintime = null == request.getParameter("begintime") ? "" : request.getParameter("begintime").toString().trim();// 开始时间
        //结束时间
        String endtime = null == request.getParameter("endtime") ? "" : request.getParameter("endtime").toString().trim();// 结束时间

        try {
            //默认为短信
            if (msType == null) {
                msType = "0";
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
                // 查询条件对象
                BusReportVo busreportvo = new BusReportVo();
                // 开始时间
                busreportvo.setBegintime(begintime);
                // 结束时间
                busreportvo.setEndtime(endtime);
                // 短信类型
                busreportvo.setMstype(Integer.parseInt(msType.trim()));
                //if(StaticValue.CORPTYPE==1){
                //企业编码
                busreportvo.setCorpCode(lgcorpcode);
                //}
                //月份
                if (timeMonth.length() > 2) {
                    timeMonth = timeMonth.substring(1);
                }

                //运营商类型
                busreportvo.setSpisuncm(spisuncm);
                request.setAttribute("spisuncm", spisuncm);
                if (!"".equals(countTime) && countTime != null) {
                    timeYear = countTime.substring(0, 4);
                    timeMonth = countTime.length() == 7 ? countTime.substring(5) : timeMonth;
                }

                if (reportTypeStr != null) {
                    reportType = Integer.parseInt(reportTypeStr);
                }
                busreportvo.setImonth(timeMonth);
                //设置年份
                busreportvo.setY(timeYear);
                //业务类型
                busreportvo.setBusCode(bustype);

                //数据源
                busreportvo.setDatasourcetype(Integer.parseInt(datasourcetype.trim()));
                // 通过拼接方法获取逗号隔开的 操作员id字符串
                long[] sum = null; // 合计

                if (isBack) {
                    if (session.getAttribute("busReportPage") != null && session.getAttribute("bus_reportvo_session") != null) {
                        pageInfo = (PageInfo) session.getAttribute("busReportPage");
                        busreportvo = (BusReportVo) session.getAttribute("bus_reportvo_session");
                        reportType = Integer.parseInt(busreportvo.getReportType());
                    }
                }
                busreportvo.setReportType(reportType + "");
                busreportList = busReportBiz.getBusReportByVo(busreportvo, pageInfo);
                sum = busReportBiz.findSumCount(busreportvo);

                request.setAttribute("reportType", reportType);
                session.setAttribute("bus_sumArray", sum);
                request.setAttribute("resultList", busreportList);
                //用于明细后退之后，保持页面页码等情况
                session.setAttribute("busReportPage", pageInfo);
                session.setAttribute("bus_reportvo_session", busreportvo);
                request.setAttribute("pageInfo", pageInfo);
            }
            BaseBiz basebiz = new BaseBiz();
            LinkedHashMap<String, String> conditionbusMap = new LinkedHashMap<String, String>();
            conditionbusMap.put("corpCode&in", "0," + lgcorpcode);
            //根据企业编码查询业务类型
            List<LfBusManager> busList = basebiz.getByCondition(
                    LfBusManager.class, conditionbusMap, null);
            request.setAttribute("busList", busList);
            request.setAttribute("countTime", timeYear + "-" + timeMonth);
            // 获取数据库短彩类型数据
            GlobConfigBiz gcBiz = new GlobConfigBiz();


            List<LfPageField> pagefileds = gcBiz.getPageFieldById("100001");
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("pagefiledusers", pagefileds);
            request.setAttribute("isFirstEnter", isFirstEnter);
            long count = 0l;
            //从pageinfo中获取查询总条数
            if (pageInfo != null) {
                count = pageInfo.getTotalRec();
            }
            // 写日志
            String opContent = "业务类型统计报表：" + count + "条 成功开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            new QueryBiz().setLog(request, "业务类型统计报表", opContent, StaticValue.GET);
            //点击返回时候，返回原来的状态
            if (isBack) {
                if (request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("bus_reportvo_backURL") != null) {
                    String back_url = (String) request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("bus_reportvo_backURL");
                    request.getRequestDispatcher(empRoot + base + "/rep_busReport.jsp?" + back_url).forward(request, response);
                } else {
                    request.getRequestDispatcher(empRoot + base + "/rep_busReport.jsp?lgcorpcode=" + lgcorpcode).forward(request, response);
                }

            } else {
                String url = "bustype=" + bustype + "&msType=" + msType + "&datasourcetype=" + datasourcetype + "&lgcorpcode=" + lgcorpcode + "&reportType=" + reportType + "&countTime=" + countTime + "&begintime=" + begintime + "&endtime=" + endtime + "&spisuncm=" + spisuncm;
                request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("bus_reportvo_backURL", url);
                request.getRequestDispatcher(empRoot + base + "/rep_busReport.jsp?lgcorpcode=" + lgcorpcode).forward(request, response);
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "业务类型统计报表sevlet查询异常");
        }
    }

    /**
     * 业务类型统计报表(详细信息)
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @description
     * @author liuxiangheng <xiaokanrensheng1012@126.com>
     * @datetime 2015-12-23 下午05:28:41
     */
    public void detailInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //起始ms数
        long startl = System.currentTimeMillis();
        //开始时间
        String starthms = hms.format(startl);
        // 获取系统session
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        // 短彩类型
        String msType = request.getParameter("msType");
        //数据源
        String datasourcetype = request.getParameter("datasourcetype");
        // 企业编码
        String lgcorpcode = qBiz.getCorpCode(request);
        //年报表/月报表
        String reportTypeStr = request.getParameter("reportType");
        //年份
        String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
        //时间
        String countTime = request.getParameter("countTime");
        //月份
        String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);

        //业务类型
        String bustype = request.getParameter("bustype");

        List<BusReportVo> busreportList = null;
        PageInfo pageInfo = new PageInfo();
        String url = "";
        try {
            //默认为短信
            if (msType == null) {
                msType = "0";
            }
            boolean isError = request.getParameter("pageIndex") == null && request.getParameter("pageSize") == null;
            if (!isError) {
                // 设置分页
                pageInfo.setPageSize(getIntParameter("pageSize", pageInfo.getPageSize(), request));
                pageInfo.setPageIndex(getIntParameter("pageIndex", 1, request));
            }
            //开始时间
            String begintime = null == request.getParameter("begintime") ? "" : request.getParameter("begintime").toString().trim();// 开始时间
            //结束时间
            String endtime = null == request.getParameter("endtime") ? "" : request.getParameter("endtime").toString().trim();// 结束时间
            // 查询条件对象
            BusReportVo busreportvo = new BusReportVo();
            // 开始时间
            busreportvo.setBegintime(begintime);
            // 结束时间
            busreportvo.setEndtime(endtime);

            //是否是详细里列表的标志
            busreportvo.setDetailFlag("detail");

            // 短信类型
            busreportvo.setMstype(Integer.parseInt(msType.trim()));
            //if(StaticValue.CORPTYPE==1){
            //企业编码
            busreportvo.setCorpCode(lgcorpcode);
            //}
            //月份
            if (timeMonth.length() > 2) {
                timeMonth = timeMonth.substring(1);
            }
            if (!"".equals(countTime) && countTime != null) {
                timeYear = countTime.substring(0, 4);
                timeMonth = countTime.length() == 7 ? countTime.substring(5) : timeMonth;
            }
            //年报表月报表 默认为日报表
            int reportType = 2;
            if (reportTypeStr != null) {
                reportType = Integer.parseInt(reportTypeStr);
            }
            //运营商类型（国内  100（实际对应，0,1,21），国外 5）
            String spisuncm = request.getParameter("spisuncm");
            if (spisuncm == null) {
                spisuncm = "100";//默认为国内
            }
            //运营商类型
            busreportvo.setSpisuncm(spisuncm);
            busreportvo.setImonth(timeMonth);
            //设置年份
            busreportvo.setY(timeYear);
            //业务类型
            busreportvo.setBusCode(bustype);
            busreportvo.setReportType(reportType + "");
            //数据源
            busreportvo.setDatasourcetype(Integer.parseInt(datasourcetype.trim()));
            // 通过拼接方法获取逗号隔开的 操作员id字符串
            long[] sum = null; // 合计
            busreportList = busReportBiz.getBusReportByVo(busreportvo, pageInfo);
            sum = busReportBiz.findSumCount(busreportvo);

            request.setAttribute("reportType", reportType);
            session.setAttribute("bus_sumArray", sum);
            request.setAttribute("resultList", busreportList);

            request.setAttribute("pageInfo", pageInfo);

            BaseBiz basebiz = new BaseBiz();
            LinkedHashMap<String, String> conditionbusMap = new LinkedHashMap<String, String>();
            conditionbusMap.put("corpCode&in", "0," + lgcorpcode);
            //根据企业编码查询业务类型
            List<LfBusManager> busList = basebiz.getByCondition(
                    LfBusManager.class, conditionbusMap, null);
            request.setAttribute("busList", busList);
            request.setAttribute("countTime", timeYear + "-" + timeMonth);
            // 获取数据库短彩类型数据
            GlobConfigBiz gcBiz = new GlobConfigBiz();
            List<LfPageField> pagefileds = gcBiz.getPageFieldById("100001");
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("pagefiledusers", pagefileds);
            //这个几个参数用于后退时候，记录原来查询的条件
//			String back_bustype=request.getParameter("back_bustype")==null?"":request.getParameter("back_bustype");
//			if(back_bustype==null||"null".equals(back_bustype)){
//				back_bustype="";
//			}
//			String back_datasourcetype=request.getParameter("back_datasourcetype")==null?"":request.getParameter("back_datasourcetype");
//			String back_spisuncm=request.getParameter("back_spisuncm")==null?"":request.getParameter("back_spisuncm");

            url = "bustype=" + bustype + "&msType=" + msType + "&datasourcetype=" + datasourcetype + "&lgcorpcode=" + lgcorpcode + "&reportType=" + reportType + "&countTime=" + countTime + "&begintime=" + begintime + "&endtime=" + endtime + "&spisuncm=" + spisuncm;
            request.setAttribute("excelURL", url);
            long count = 0l;
            //从pageinfo中获取查询总条数
            if (pageInfo != null) {
                count = pageInfo.getTotalRec();
            }
            // 写日志
            String opContent = "业务类型统计报表详情：" + count + "条 成功开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            new QueryBiz().setLog(request, "业务类型统计报表", opContent, StaticValue.GET);
//			url=url+"&back_bustype="+back_bustype+"&back_datasourcetype="+back_datasourcetype+"&back_spisuncm="+back_spisuncm;
            request.getRequestDispatcher(empRoot + base + "/rep_busDetailReport.jsp?" + url).forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "业务类型统计报表sevlet查询异常");
        }
    }

    /**
     * 业务类型统计报表(国家类型)
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @description
     * @author liuxiangheng <xiaokanrensheng1012@126.com>
     * @datetime 2015-12-23 下午05:28:59
     */
    public void busNation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //起始ms数
        long startl = System.currentTimeMillis();
        //开始时间
        String starthms = hms.format(startl);
        // 获取系统session
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        // 短彩类型
        String msType = request.getParameter("msType");
        //数据源
        String datasourcetype = request.getParameter("datasourcetype");
        // 企业编码
        String lgcorpcode = qBiz.getCorpCode(request);
        //年报表/月报表
        String reportTypeStr = request.getParameter("reportType");
        //年份
        String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
        //时间
        String countTime = request.getParameter("countTime");
        //月份
        String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
        List<BusNationtVo> busreportList = null;
        PageInfo pageInfo = new PageInfo();
        String url = "";
        try {
            //默认为短信
            if (msType == null) {
                msType = "0";
            }

            boolean isError = request.getParameter("pageIndex") == null && request.getParameter("pageSize") == null;
            if (!isError) {
                // 设置分页
                pageInfo.setPageSize(getIntParameter("pageSize", pageInfo.getPageSize(), request));
                pageInfo.setPageIndex(getIntParameter("pageIndex", 1, request));
            }
            //业务类型
            String bustype = request.getParameter("bustype");

            //开始时间
            String begintime = null == request.getParameter("begintime") ? "" : request.getParameter("begintime").toString().trim();// 开始时间
            //结束时间
            String endtime = null == request.getParameter("endtime") ? "" : request.getParameter("endtime").toString().trim();// 结束时间
            // 查询条件对象
            BusReportVo busreportvo = new BusReportVo();
            // 开始时间
            busreportvo.setBegintime(begintime);

            // 结束时间
            busreportvo.setEndtime(endtime);
            //运营商类型（国内  100（实际对应，0,1,21），国外 5）
            String spisuncm = request.getParameter("spisuncm");
            if (spisuncm == null) {
                spisuncm = "100";//默认为国内
            }
            //运营商类型
            busreportvo.setSpisuncm(spisuncm);
            //国家代码
            busreportvo.setNationcode(request.getParameter("nationCode"));
            //国家名称
            String nationName = request.getParameter("nationName");
            if (nationName != null && !"".equals(nationName)) {
                nationName = URLDecoder.decode(nationName, "UTF-8");
                nationName = nationName.trim();
            }
            busreportvo.setNationname(nationName);
            busreportvo.setNationFlag("nation");
            // 短信类型
            busreportvo.setMstype(Integer.parseInt(msType.trim()));
            //if(StaticValue.CORPTYPE==1){
            //企业编码
            busreportvo.setCorpCode(lgcorpcode);
            //}
            //月份
            if (timeMonth.length() > 2) {
                timeMonth = timeMonth.substring(1);
            }
            if (!"".equals(countTime) && countTime != null) {
                timeYear = countTime.substring(0, 4);
                timeMonth = countTime.length() == 7 ? countTime.substring(5) : timeMonth;
            }
            //年报表月报表 默认为日报表
            int reportType = 2;
            if (reportTypeStr != null) {
                reportType = Integer.parseInt(reportTypeStr);
            }
            busreportvo.setImonth(timeMonth);
            //设置年份
            busreportvo.setY(timeYear);
            //业务类型
            busreportvo.setBusCode(bustype);
            busreportvo.setReportType(reportType + "");
            //数据源
            busreportvo.setDatasourcetype(Integer.parseInt(datasourcetype.trim()));
            // 通过拼接方法获取逗号隔开的 操作员id字符串
            long[] sum = null; // 合计
            busreportList = busReportBiz.getBusNationReportByVo(busreportvo, pageInfo);
            sum = busReportBiz.findSumCount(busreportvo);

            request.setAttribute("reportType", reportType);

            session.setAttribute("bus_nation_sumArray", sum);

            request.setAttribute("resultList", busreportList);

            request.setAttribute("pageInfo", pageInfo);

            BaseBiz basebiz = new BaseBiz();
            LinkedHashMap<String, String> conditionbusMap = new LinkedHashMap<String, String>();
            conditionbusMap.put("corpCode&in", "0," + lgcorpcode);
            //根据企业编码查询业务类型
            List<LfBusManager> busList = basebiz.getByCondition(
                    LfBusManager.class, conditionbusMap, null);
            request.setAttribute("busList", busList);
            request.setAttribute("countTime", timeYear + "-" + timeMonth);
            url = "bustype=" + bustype + "&msType=" + msType + "&datasourcetype=" + datasourcetype + "&lgcorpcode=" + lgcorpcode + "&reportType=" + reportType + "&countTime=" + countTime + "&begintime=" + begintime + "&endtime=" + endtime + "&spisuncm=" + spisuncm;
            request.setAttribute("excelURL", url);
//			String back_bustype=request.getParameter("back_bustype")==null?"":request.getParameter("back_bustype");
//			if(back_bustype==null||"null".equals(back_bustype)){
//				back_bustype="";
//			}
//			String back_datasourcetype=request.getParameter("back_datasourcetype")==null?"":request.getParameter("back_datasourcetype");
//			String back_spisuncm=request.getParameter("back_spisuncm")==null?"":request.getParameter("back_spisuncm");
//			url=url+"&back_bustype="+back_bustype+"&back_datasourcetype="+back_datasourcetype+"&back_spisuncm="+back_spisuncm;
            // 获取数据库短彩类型数据
            GlobConfigBiz gcBiz = new GlobConfigBiz();
            List<LfPageField> pagefileds = gcBiz.getPageFieldById("100001");
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("pagefiledusers", pagefileds);
            long count = 0l;
            //从pageinfo中获取查询总条数
            if (pageInfo != null) {
                count = pageInfo.getTotalRec();
            }
            // 写日志
            String opContent = "业务类型统计报表国家详情：" + count + "条 成功开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            new QueryBiz().setLog(request, "业务类型统计报表国家详情", opContent, StaticValue.GET);
            request.getRequestDispatcher(empRoot + base + "/rep_busReportNation.jsp?" + url).forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "业务类型统计报表sevlet查询异常");
        }
    }


    /**
     * 业务类型报表导出
     *
     * @param request
     * @param response
     * @throws Exception
     * @description
     * @author liuxiangheng <xiaokanrensheng1012@126.com>
     * @datetime 2015-12-23 下午05:30:05
     */
    public void r_busRptExportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //起始ms数
        long startl = System.currentTimeMillis();
        //开始时间
        String starthms = hms.format(startl);
        // 获取session对象
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        List<BusReportVo> buslist = null;
        // 短彩类型
        String msType = request.getParameter("msType");
        //数据源
        String datasourcetype = request.getParameter("datasourcetype");
        // 企业编码
        String lgcorpcode = qBiz.getCorpCode(request);
        //年报表/月报表
        String reportTypeStr = request.getParameter("reportType");
        try {
            //默认为短信
            if (msType == null || "".equals(msType)) {
                msType = "0";
            }
            //默认为全部
            if (datasourcetype == null || "".equals(datasourcetype)) {
                datasourcetype = "0";
            }

            //业务类型
            String bustype = request.getParameter("bustype");
            //年份
            String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
            //时间
            String countTime = request.getParameter("countTime");
            //月份
            String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
            //开始时间
            String begintime = null == request.getParameter("begintime") ? "" : request.getParameter("begintime").toString().trim();// 开始时间
            //结束时间
            String endtime = null == request.getParameter("endtime") ? "" : request.getParameter("endtime").toString().trim();// 结束时间
            // 查询条件对象
            BusReportVo busreportvo = new BusReportVo();
            // 开始时间
            busreportvo.setBegintime(begintime);
            // 结束时间
            busreportvo.setEndtime(endtime);
            //运营商类型（国内  100（实际对应，0,1,21），国外 5）
            String spisuncm = request.getParameter("spisuncm");
            if (spisuncm == null) {
                spisuncm = "100";//默认为国内
            }
            //运营商类型
            busreportvo.setSpisuncm(spisuncm);
            // 短信类型
            busreportvo.setMstype(Integer.parseInt(msType.trim()));
            //if(StaticValue.CORPTYPE==1){
            //企业编码
            busreportvo.setCorpCode(lgcorpcode);
            //}
            //年报表月报表 默认为日报表
            int reportType = 2;
            if (reportTypeStr != null) {
                reportType = Integer.parseInt(reportTypeStr);
            }
            //如果是新的页面首页界面过来的
            String isdeail = request.getParameter("isdeail");
            if (!"no".equals(isdeail)) {
                //是否是详细里列表的标志
                busreportvo.setDetailFlag("detail");
            }
            busreportvo.setReportType(reportType + "");
            //业务类型
            busreportvo.setBusCode(bustype);
            if (!"".equals(countTime) && countTime != null) {
                timeYear = countTime.substring(0, 4);
                timeMonth = countTime.length() == 7 ? countTime.substring(5) : timeMonth;
            }
            busreportvo.setImonth(timeMonth);
            //设置年份
            busreportvo.setY(timeYear);
            //数据源
            busreportvo.setDatasourcetype(Integer.parseInt(datasourcetype.trim()));

            //数据源
            String datasourcename = "";
            if (busreportvo.getDatasourcetype() != null && busreportvo.getDatasourcetype() == 0) {
                datasourcename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qb", request);
            } else if (busreportvo.getDatasourcetype() != null && busreportvo.getDatasourcetype() == 1) {
                datasourcename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_empfs", request);
            } else if (busreportvo.getDatasourcetype() != null && busreportvo.getDatasourcetype() == 2) {
                datasourcename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_httpjr", request);
            } else if (busreportvo.getDatasourcetype() != null && busreportvo.getDatasourcetype() == 3) {
                datasourcename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_dbjr", request);
            } else if (busreportvo.getDatasourcetype() != null && busreportvo.getDatasourcetype() == 4) {
                datasourcename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_zljr", request);
            } else {
                datasourcename = "--";
            }

            buslist = busReportBiz.getBusReportByVo(busreportvo, null);

            // 获取session存储的统计数
            long[] sumArray = (long[]) session.getAttribute("bus_sumArray");

            //时间段字符串
            String showTime = session.getAttribute("bus_showTime").toString();
            //输出
            response.setContentType("html/text");
            PrintWriter out = response.getWriter();
            if (buslist != null && buslist.size() > 0) {
                Map<String, String> resultMap = null;
                String isdestr = "";
                if (!"no".equals(isdeail)) {
                    resultMap = busReportBiz.createBusReportExcel(buslist, reportType, sumArray, datasourcename, request);
                    session.setAttribute("busdetail_export_map", resultMap);
                    isdestr = "详情";
                } else {
                    resultMap = busReportBiz.createBusExcel_V1(buslist, spisuncm, begintime, endtime, countTime, reportType, sumArray, datasourcename, request);
                    session.setAttribute("bus_export_map", resultMap);
                    isdestr = "";
                }
                String fileName = (String) resultMap.get("FILE_NAME");
                // 写日志
                String opContent = "导出业务类型统计报表" + isdestr + "：" + buslist.size() + "条  ,文件名：" + fileName + ",开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
                new QueryBiz().setLog(request, "业务类型统计报表" + isdestr, opContent, StaticValue.GET);
                buslist.clear();
                buslist = null;
                out.print("true");
//				String fileName = (String) resultMap.get("FILE_NAME");
//				String filePath = (String) resultMap.get("FILE_PATH");
//				DownloadFile df = new DownloadFile();
//				df.downFile(request, response, filePath, fileName);
            } else {
                out.print("false");
            }
        } catch (Exception e) {
            // 异常处理
            EmpExecutionContext.error(e, "业务类型报表导出异常");
        }
    }

    /**
     * 下载导出文件 业务类型报表导出下载
     *
     * @param request
     * @param response
     * @description
     * @author liuxiangheng <xiaokanrensheng1012@126.com>
     * @datetime 2015-12-23 下午05:30:20
     */
    public void bus_downloadFile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("bus_export_map");
        session.removeAttribute("bus_export_map");
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
     * 下载导出文件 业务类型报表详情导出下载
     *
     * @param request
     * @param response
     * @description
     * @author liuxiangheng <xiaokanrensheng1012@126.com>
     * @datetime 2015-12-23 下午05:30:28
     */
    public void busdetail_downloadFile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("busdetail_export_map");
        session.removeAttribute("busdetail_export_map");
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
     * 业务类型报表导出
     *
     * @param request
     * @param response
     * @throws Exception
     * @description
     * @author liuxiangheng <xiaokanrensheng1012@126.com>
     * @datetime 2015-12-23 下午05:30:36
     */
    public void r_busNationExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //起始ms数
        long startl = System.currentTimeMillis();
        //开始时间
        String starthms = hms.format(startl);
        // 获取session对象
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        List<BusNationtVo> buslist = null;
        // 短彩类型
        String msType = request.getParameter("msType");
        //数据源
        String datasourcetype = request.getParameter("datasourcetype");
        // 企业编码
        String lgcorpcode = qBiz.getCorpCode(request);
        //年报表/月报表
        String reportTypeStr = request.getParameter("reportType");
        try {
            //默认为短信
            if (msType == null || "".equals(msType)) {
                msType = "0";
            }
            //默认为全部
            if (datasourcetype == null || "".equals(datasourcetype)) {
                datasourcetype = "0";
            }
            //业务类型
            String bustype = request.getParameter("bustype");
            //年份
            String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
            //时间
            String countTime = request.getParameter("countTime");
            //月份
            String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
            //开始时间
            String begintime = null == request.getParameter("begintime") ? "" : request.getParameter("begintime").toString().trim();// 开始时间
            //结束时间
            String endtime = null == request.getParameter("endtime") ? "" : request.getParameter("endtime").toString().trim();// 结束时间
            // 查询条件对象
            BusReportVo busreportvo = new BusReportVo();
            // 开始时间
            busreportvo.setBegintime(begintime);
            // 结束时间
            busreportvo.setEndtime(endtime);
            //运营商类型（国内  100（实际对应，0,1,21），国外 5）
            String spisuncm = request.getParameter("spisuncm");
            if (spisuncm == null) {
                spisuncm = "100";//默认为国内
            }

            //国家代码
            busreportvo.setNationcode(request.getParameter("nationCode"));
            //国家名称
            String nationName = request.getParameter("nationName");
//			if(nationName!=null&&!"".equals(nationName)){
//				nationName = URLDecoder.decode(nationName, "UTF-8");
//			}
            busreportvo.setNationname(nationName);
            //运营商类型
            busreportvo.setSpisuncm(spisuncm);
            // 短信类型
            busreportvo.setMstype(Integer.parseInt(msType.trim()));
            //if(StaticValue.CORPTYPE==1){
            //企业编码
            busreportvo.setCorpCode(lgcorpcode);
            //}
            //年报表月报表 默认为日报表
            int reportType = 2;
            if (reportTypeStr != null) {
                reportType = Integer.parseInt(reportTypeStr);
            }
            //是否是国际详细里列表的标志
            busreportvo.setNationFlag("nation");
            busreportvo.setReportType(reportType + "");
            //业务类型
            busreportvo.setBusCode(bustype);
            if (!"".equals(countTime) && countTime != null) {
                timeYear = countTime.substring(0, 4);
                timeMonth = countTime.length() == 7 ? countTime.substring(5) : timeMonth;
            }
            busreportvo.setImonth(timeMonth);
            //设置年份
            busreportvo.setY(timeYear);
            //数据源
            busreportvo.setDatasourcetype(Integer.parseInt(datasourcetype.trim()));

            //数据源
            String datasourcename = "";
            if (busreportvo.getDatasourcetype() != null && busreportvo.getDatasourcetype() == 0) {
                datasourcename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qb", request);
            } else if (busreportvo.getDatasourcetype() != null && busreportvo.getDatasourcetype() == 1) {
                datasourcename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_empfs", request);
            } else if (busreportvo.getDatasourcetype() != null && busreportvo.getDatasourcetype() == 2) {
                datasourcename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_httpjr", request);
            } else if (busreportvo.getDatasourcetype() != null && busreportvo.getDatasourcetype() == 3) {
                datasourcename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_dbjr", request);
            } else if (busreportvo.getDatasourcetype() != null && busreportvo.getDatasourcetype() == 4) {
                datasourcename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_zljr", request);
            } else {
                datasourcename = "--";
            }

            buslist = busReportBiz.getBusNationReportByVo(busreportvo, null);

            // 获取session存储的统计数
            long[] sumArray = (long[]) session.getAttribute("bus_nation_sumArray");
            //时间段字符串
            String showTime = session.getAttribute("bus_showTime").toString();

            response.setContentType("html/text");
            PrintWriter out = response.getWriter();
            if (buslist != null && buslist.size() > 0) {
                Map<String, String> resultMap = busReportBiz.createBusNationExcel(buslist, begintime, endtime, countTime, reportType, sumArray, showTime, datasourcename, request);
                String fileName = (String) resultMap.get("FILE_NAME");
                // 写日志
                String opContent = "导出业务类型统计报表国家详情：" + buslist.size() + "条  ,文件名：" + fileName + ",开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
                new QueryBiz().setLog(request, "业务类型统计报表国家详情", opContent, StaticValue.GET);
                buslist.clear();
                buslist = null;
                session.setAttribute("busnation_export_map", resultMap);
                out.print("true");
//				String filePath = (String) resultMap.get("FILE_PATH");
//				DownloadFile df = new DownloadFile();
//				df.downFile(request, response, filePath, fileName);
            } else {
                out.print("false");
            }
        } catch (Exception e) {
            // 异常处理
            EmpExecutionContext.error(e, "业务类型报表导出异常");
        }
    }


    /**
     * 下载导出文件 业务类型报表国家详情导出下载
     *
     * @param request
     * @param response
     * @description
     * @author liuxiangheng <xiaokanrensheng1012@126.com>
     * @datetime 2015-12-23 下午05:30:46
     */
    public void busnation_downloadFile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("busnation_export_map");
        session.removeAttribute("busnation_export_map");
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
