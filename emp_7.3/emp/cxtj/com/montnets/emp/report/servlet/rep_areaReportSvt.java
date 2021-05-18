package com.montnets.emp.report.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.biz.GlobConfigBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.report.AprovinceCity;
import com.montnets.emp.entity.system.LfPageField;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.query.biz.QueryBiz;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.report.biz.AreaReportBiz;
import com.montnets.emp.report.vo.AreaReportVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;

/**
 * 区域统计报表
 *
 * @功能概要：
 * @项目名称： p_cxtj
 * @初创作者： liuxiangheng <xiaokanrensheng1012@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2015-12-23 下午04:54:33
 * <p>
 * 修改记录1：
 * </p>
 *
 * <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
@SuppressWarnings("serial")
public class rep_areaReportSvt extends BaseServlet {

    // 模块名称
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
     * @param param
     * @param defaultValue
     * @param request
     * @return
     * @description
     * @author liuxiangheng <xiaokanrensheng1012@126.com>
     * @datetime 2015-12-23 下午04:55:01
     */
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
     * 区域统计报表查询方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @description
     * @author liuxiangheng <xiaokanrensheng1012@126.com>
     * @datetime 2015-12-23 下午04:55:11
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LfSysuser user = qBiz.getCurrentUser(request);
/*		//没有机构权限
		if(user == null || user.getPermissionType()!=2){
			request.getRequestDispatcher("/common/no_dep_permission.jsp").forward(
					request, response);
		}*/
        // 起始ms数
        long startl = System.currentTimeMillis();
        // 开始时间
        String starthms = hms.format(startl);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // // 登录企业编码信息
        // String corpcode = request.getParameter("lgcorpcode");
        // // 短彩类型
        // String msType = request.getParameter("msType");
        // // 报表类型
        // String reporttypestr = request.getParameter("reportType");
        // //年份或月份查询条件 日报表的起始时间
        // String countTime = request.getParameter("countTime");
        // //日报表 结束时间
        // String endTime = request.getParameter("endTime");
        // 时间
        String timeYear = (calendar.get(Calendar.YEAR)) + "";
        // 时间月份
        String timeMonth = "0" + (calendar.get(Calendar.MONTH) + 1);
        // 区域统计查询条件对象
        AreaReportVo areaReportVo = new AreaReportVo();
        // 分页对象
        PageInfo pageInfo = new PageInfo();
        // 报表类型
        int reportType = 0;
        // 是否第一次执行
        boolean isFirstEnter = false;
        boolean isError = true;
        // 区域统计报表结果集定义
        List<AreaReportVo> reportList = null;
        // 区域统计报表biz
        AreaReportBiz arbiz = new AreaReportBiz();
        List<AprovinceCity> acitys = new ArrayList<AprovinceCity>();
        // 是否详情查看
        boolean isDes = "1".equals(request.getParameter("isDes"));
        // 是否返回
        boolean isBack = request.getParameter("isback") != null;
        try {
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
            // 判断是否第一次访问
            if (!isFirstEnter) {
                HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
                if (!isBack) {
                    // 登录企业编码信息
                    String corpcode = qBiz.getCorpCode(request);
                    // 短彩类型
                    String msType = request.getParameter("msType");
                    // 报表类型
                    String reporttypestr = request.getParameter("reportType");
                    // 年份或月份查询条件 日报表的起始时间
                    String countTime = request.getParameter("countTime");
                    // 日报表 结束时间
                    String endTime = request.getParameter("endTime");
                    // 区域
                    String province = request.getParameter("provinces");
                    // 判断短彩类型是否为空 如果为空赋予默认值
                    if (msType == null) {
                        msType = "0";
                    }

                    areaReportVo.setMstype(Integer.parseInt(msType));
                    // 如果月份超过两位
                    if (timeMonth.length() > 2) {
                        timeMonth = timeMonth.substring(1);
                    }
                    // 判断报表类型不为空则类型转换
                    if (reporttypestr != null) {
                        reportType = Integer.parseInt(reporttypestr);
                    }
                    // 年份或月份查询条件不为空则提取年份 和月份
                    if (!"".equals(countTime) && countTime != null) {
                        timeYear = countTime.substring(0, 4);
                        timeMonth = countTime.length() >= 7 ? countTime.substring(5, 7) : timeMonth;
                    } else {
                        countTime = "";
                    }
                    // 年
                    areaReportVo.setY(timeYear);
                    // 月
                    areaReportVo.setImonth(timeMonth);
                    // 报表类型
                    areaReportVo.setReporttype(reportType);
                    // if(StaticValue.CORPTYPE==1){
                    // 企业编码
                    areaReportVo.setCorpcode(corpcode);
                    // }
                    // 区域
                    areaReportVo.setProvince(province.equals(MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request)) ? "未知" : province);
                    areaReportVo.setStartdate(countTime);
                    areaReportVo.setEnddate(endTime);
                    areaReportVo.setDes(isDes);
                } else {
                    areaReportVo = (AreaReportVo) session.getAttribute("rep_areaReportVo");
                    pageInfo = (PageInfo) session.getAttribute("rep_areaPageinfo");
                }

                // 存储 areaReportVo对象及pageinfo对象 处理二级返回
                if (!isDes) {
                    session.setAttribute("rep_areaReportVo", areaReportVo);
                    session.setAttribute("rep_areaPageinfo", pageInfo);
                }
                // 获取区域统计报表结果集
                reportList = arbiz.getAreaReportsByVO(areaReportVo, pageInfo);
                // 总数
                request.setAttribute("sumCount", arbiz.findSumCount(areaReportVo));
                long[] sum = arbiz.findSumCount(areaReportVo);
                //提交总数
                request.setAttribute("icount", sum[0]);
                //接收成功数
                request.setAttribute("rsucc", sum[1]);
                //发送失败数
                request.setAttribute("rfail1", sum[2]);
                //接收失败数
                request.setAttribute("rfail2", sum[3]);
                //未返数
                request.setAttribute("rnret", sum[4]);
                // 存储对象
                request.setAttribute("areaReportVo", areaReportVo);
                // 分页对象
                request.setAttribute("pageInfo", pageInfo);
                // 存储查询结果集
                request.setAttribute("reportList", reportList);
            }
            request.setAttribute("countTime", sdf.format(calendar.getTime()));
            // 获取系统定义的短彩类型值
            GlobConfigBiz gcBiz = new GlobConfigBiz();
            List<LfPageField> pagefileds = gcBiz.getPageFieldById("100001");
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("pagefileds", pagefileds);
            request.setAttribute("isFirstEnter", isFirstEnter);
            // 区域list
            acitys = arbiz.getProvinceList();
            long count = 0l;
            // 从pageinfo中获取查询总条数
            if (pageInfo != null) {
                count = pageInfo.getTotalRec();
            }
            // 写日志
            String opContent = "区域统计报表查询：" + count + "条开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            new QueryBiz().setLog(request, "区域统计报表", opContent, StaticValue.GET);
        } catch (Exception e) {
            // 异常时存储部分数据
            request.setAttribute("countTime", sdf.format(calendar.getTime()));
            long[] returnLong = new long[5];
            returnLong[0] = 0;
            returnLong[1] = 0;
            returnLong[2] = 0;
            returnLong[3] = 0;
            returnLong[4] = 0;
            request.setAttribute("findresult", "-1");
            request.setAttribute("pageInfo", pageInfo);
            EmpExecutionContext.error(e, "查询异常");
        } finally {
            request.setAttribute("acitys", acitys);
            // 跳转
            request.getRequestDispatcher(empRoot + base + "/rep_areaReport.jsp").forward(request, response);
        }
    }

    /**
     * 区域统计统计报表
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void r_areaRptExportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 起始ms数
        long startl = System.currentTimeMillis();
        // 开始时间
        String starthms = hms.format(startl);
        // 获取页面上的时间段
        String countTime = request.getParameter("countTime");
        // 区域
        String province = request.getParameter("province");
        // 报表类型
        String reporttypestr = request.getParameter("reportType");
        // 短彩类型
        String mstype = request.getParameter("mstype");
        // 登录企业编码信息
        String corpcode = qBiz.getCorpCode(request);

        // 区域统计报表查询条件对象
        AreaReportVo areareportvo = new AreaReportVo();
        // 区域统计报表BIZ
        AreaReportBiz omrbiz = new AreaReportBiz();
        // 是否为详情界面
        boolean isDes = "1".equals(request.getParameter("isDes"));
        // 日报表结束时间
        String endTime = request.getParameter("endTime");
        // 导出结果集
        List<AreaReportVo> reportList = null;
        try {
            // 时间格式
            String timeYear = (Calendar.getInstance().get(Calendar.YEAR)) + "";
            String timeMonth = "0" + (Calendar.getInstance().get(Calendar.MONTH) + 1);
            if (timeMonth.length() > 2) {
                timeMonth = timeMonth.substring(1);
            }
            // 获得报表的类型（0月报表 1年报表）
            int reportType = 0;
            if (reporttypestr != null) {
                reportType = Integer.parseInt(reporttypestr);
            }
            // 时间字符串处理
            if (!"".equals(countTime) && countTime != null) {
                timeYear = countTime.substring(0, 4);
                timeMonth = countTime.length() == 7 ? countTime.substring(5) : timeMonth;
            } else {
                countTime = "";
            }
            // 短彩类型
            if (mstype == null || "".equals(mstype)) {
                // 默认是短信
                mstype = "0";
            }

            areareportvo.setMstype(Integer.parseInt(mstype));
            // 设置区域统计账户ID
            // 设置区域统计账户时间
            areareportvo.setY(timeYear);
            // 月
            areareportvo.setImonth(timeMonth);
            // 报表类型
            areareportvo.setReporttype(reportType);
            // 省份
            areareportvo.setProvince(province);
            // 判断是否多企业
            // if(StaticValue.CORPTYPE==1){
            areareportvo.setCorpcode(corpcode);
            // }
            areareportvo.setDes(isDes);
            areareportvo.setStartdate(countTime);
            areareportvo.setEnddate(endTime);
            long[] sum = omrbiz.findSumCount(areareportvo);
            reportList = omrbiz.getAreaReportsByVO(areareportvo, null);
            response.setContentType("html/text");
            PrintWriter out = response.getWriter();
            // 判断结果集是否有数据有则导出
            if (reportList != null && reportList.size() > 0) {
                // 保存失败成功总数到对象
                areareportvo.setIcount(sum[0]);
                areareportvo.setRsucc(sum[1]);
                areareportvo.setRfail1(sum[2]);
                areareportvo.setRfail2(sum[3]);
                areareportvo.setRnret(sum[4]);
                Map<String, String> resultMap = omrbiz.createareaReportExcel(reportList, areareportvo, request);
                String fileName = (String) resultMap.get("FILE_NAME");
                // 写日志
                String opContent = "导出区域统计报表查询：" + reportList.size() + "条 文件名：" + fileName + ",开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
                new QueryBiz().setLog(request, "区域统计报表", opContent, StaticValue.GET);
                reportList.clear();
                reportList = null;

                HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
                session.setAttribute("area_export_map", resultMap);
                out.print("true");
                // String fileName = (String) resultMap.get("FILE_NAME");
                // String filePath = (String) resultMap.get("FILE_PATH");
                // DownloadFile df = new DownloadFile();
                // df.downFile(request, response, filePath, fileName);
            } else {
                out.print("false");
            }
        } catch (Exception e) {
            // 异常处理
            EmpExecutionContext.error(e, "导出异常");
        }
    }

    /**
     * 下载导出文件
     *
     * @param request
     * @param response
     */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("area_export_map");
        session.removeAttribute("area_export_map");
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
