package com.montnets.emp.report.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobConfigBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.system.LfPageField;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.query.biz.QueryBiz;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.report.biz.DepReportBiz;
import com.montnets.emp.report.biz.ReportBiz;
import com.montnets.emp.report.vo.CountReportVo;
import com.montnets.emp.report.vo.DepAreaRptVo;
import com.montnets.emp.report.vo.DepRptVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;

/**
 * 机构统计报表servlet
 *
 * @author liaojirong <ljr0300@163.com>
 * @project p_cxtj
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:48:43
 * @description
 */
public class rep_sysDepReportSvt extends BaseServlet {
    /**
     * 机构统计报表biz
     */
    private final DepReportBiz depReportBiz = new DepReportBiz();

    /**
     * 报表biz
     */
    private final ReportBiz reportBiz = new ReportBiz();

    private final QueryBiz queryBiz = new QueryBiz();

    /**
     * 查询日期格式化（年月）：yyyy-MM。用于格式化页面来的日期字符串
     */
    //private SimpleDateFormat sdfSeachTime = new SimpleDateFormat("yyyy-MM");

    /**
     * 查询日期格式化（年月日）：yyyyMMdd
     */
    private final SimpleDateFormat sdfSeachDayTime = new SimpleDateFormat("yyyyMMdd");

    /**
     * 查询日期格式化（年-月-日）：yyyy-MM-dd
     */
    private final SimpleDateFormat sdfSeachDay = new SimpleDateFormat("yyyy-MM-dd");

    //session变量名称标记
    //private static String strName = "rd_";

    // 模块名称
    private final String empRoot = "cxtj";

    // 基路径
    private final String base = "/report";
    //时分秒格式化
    private final SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");

    /**
     * 分页使用方法
     *
     * @param param
     * @param defaultValue
     * @param request
     * @return
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
     * 机构统计报表查询方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //起始毫秒数
        long startl = System.currentTimeMillis();

        PageInfo pageInfo = new PageInfo();
        // 是否第一次访问
        boolean isFirstEnter = pageSet(pageInfo, request);

        // 类型
        String msType = request.getParameter("msType");
        // 企业编码
        String corpCode = "";
        // 登录操作员id
        String userid = "";
        String lgusername = request.getParameter("lgusername");
        //报表类型
        String reportType = request.getParameter("reportType");
        //运营商
        String spnumtype = request.getParameter("spnumtype");
        // 部门名称组合字符串
        String dName = request.getParameter("depNam");
        // 部门ID组合字符串
        String depids = request.getParameter("deptString");
        //查询开始时间
        String begintime = request.getParameter("begintime");
        //查询结束时间
        String endtime = request.getParameter("endtime");
        //数据源类型
        String datasourcetype = request.getParameter("datasourcetype");

        //进入标记，空为从左侧菜单进入；1为从详情进入
        String inFlag = request.getParameter("inFlag");

        // 获取当前登录操作员对象
        LfSysuser user = queryBiz.getCurrentUser(request);
        if (user == null) {
            EmpExecutionContext.error("机构统计报表查询，企业编码为空，获取不到登录操作员对象。");
            return;
        }
        //没有机构权限
        if (user.getPermissionType() != 2) {
            request.getRequestDispatcher("/common/no_dep_permission.jsp").forward(
                    request, response);
        }
        //当传入企业编码为空或空串则从session中取

        corpCode = user.getCorpCode();
        userid = user.getUserId().toString();

        try {
            request.setAttribute("isFirstEnter", isFirstEnter);

            GlobConfigBiz gcBiz = new GlobConfigBiz();
            //短彩类型数据获取
            List<LfPageField> pagefileds = gcBiz.getPageFieldById("100001");
            //短彩类型数据
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("pagefileddeps", pagefileds);

            //第一次进入
            if (isFirstEnter) {
                //第一次进入，清空session查询条件
                clearSearchCondition(request);
                return;
            }
            if (msType == null || msType.trim().length() == 0 || "null".equals(msType)) {
                //默认短信
                msType = "0";
            }
            if (reportType == null || reportType.trim().length() == 0 || "null".equals(reportType)) {
                //默认月报表
                reportType = "0";
            }
            if (spnumtype == null || spnumtype.trim().length() == 0 || "null".equals(spnumtype)) {
                //默认国内
                spnumtype = "0";
            }
            if (begintime == null || begintime.trim().length() == 0 || "null".equals(begintime)) {
                //默认当前日期
                begintime = sdfSeachDay.format(new Date());
            }
            if (endtime == null || endtime.trim().length() == 0 || "null".equals(endtime)) {
                //默认当前日期月份的最后一天
                Date dateEndTime = sdfSeachDay.parse(begintime);
                Calendar ca = Calendar.getInstance();
                ca.setTime(dateEndTime);
                ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
                endtime = sdfSeachDay.format(ca.getTime());
            }


            HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
            //从菜单进入，则设置查询条件session
            if (inFlag == null || inFlag.trim().length() == 0) {
                //机构
                session.setAttribute(RptStaticValue.PREFIX_RPT_DEP + "depNam", dName);
                //发送类型
                session.setAttribute(RptStaticValue.PREFIX_RPT_DEP + "datasourcetype", datasourcetype);
                // 信息类型
                session.setAttribute(RptStaticValue.PREFIX_RPT_DEP + "msType", msType);
                //运营商
                session.setAttribute(RptStaticValue.PREFIX_RPT_DEP + "spnumtype", spnumtype);
                //报表类型
                session.setAttribute(RptStaticValue.PREFIX_RPT_DEP + "reportType", reportType);
                //查询开始时间
                session.setAttribute(RptStaticValue.PREFIX_RPT_DEP + "begintime", begintime);
                //查询结束时间
                session.setAttribute(RptStaticValue.PREFIX_RPT_DEP + "endtime", endtime);
                // 部门ID组合字符串
                session.setAttribute(RptStaticValue.PREFIX_RPT_DEP + "deptString", depids);
                //用于返回
                session.setAttribute(RptStaticValue.PREFIX_RPT_DEP + "deprptdname", dName);
                session.setAttribute(RptStaticValue.PREFIX_RPT_DEP + "pageIndex", pageInfo.getPageIndex());
                session.setAttribute(RptStaticValue.PREFIX_RPT_DEP + "pageSize", pageInfo.getPageSize());

                session.setAttribute(RptStaticValue.PREFIX_RPT_DEP + "totalPage", pageInfo.getTotalPage());
                session.setAttribute(RptStaticValue.PREFIX_RPT_DEP + "totalRec", pageInfo.getTotalRec());
            }
            //从其他地方进入，则通过session获取查询条件
            else {
                // 类型
                msType = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_DEP + "msType");
                //报表类型
                reportType = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_DEP + "reportType");
                //运营商
                spnumtype = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_DEP + "spnumtype");
                // 部门名称组合字符串
                //dName = (String)session.getAttribute(RptStaticValue.PREFIX_RPT_DEP + "depNam");
                // 部门ID组合字符串
                depids = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_DEP + "deptString");
                //查询开始时间
                begintime = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_DEP + "begintime");
                //查询结束时间
                endtime = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_DEP + "endtime");
                //数据源类型
                datasourcetype = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_DEP + "datasourcetype");

                Integer pageIndex = (Integer) session.getAttribute(RptStaticValue.PREFIX_RPT_DEP + "pageIndex");
                pageInfo.setPageIndex(pageIndex);
                Integer pageSize = (Integer) session.getAttribute(RptStaticValue.PREFIX_RPT_DEP + "pageSize");
                pageInfo.setPageSize(pageSize);

                Integer totalPage = (Integer) session.getAttribute(RptStaticValue.PREFIX_RPT_DEP + "totalPage");
                pageInfo.setTotalPage(totalPage);

                Integer totalRec = (Integer) session.getAttribute(RptStaticValue.PREFIX_RPT_DEP + "totalRec");
                pageInfo.setTotalRec(totalRec);
            }

            // 获得session
            //如果选了彩信又选了接入发送
            if ("1".equals(msType) && "2".equals(datasourcetype)) {
                //提交总数
                request.setAttribute("icount", 0);
                //接收成功数
                request.setAttribute("rsucc", 0);
                //发送失败数
                request.setAttribute("rfail1", 0);
                //接收失败数
                request.setAttribute("rfail2", 0);
                //未返数
                request.setAttribute("rnret", 0);
                return;
            }

            //权限类型 1：个人权限  2：机构权限
            if (user.getPermissionType() == 1) {
                //提交总数
                request.setAttribute("icount", 0);
                //接收成功数
                request.setAttribute("rsucc", 0);
                //发送失败数
                request.setAttribute("rfail1", 0);
                //接收失败数
                request.setAttribute("rfail2", 0);
                //未返数
                request.setAttribute("rnret", 0);
                return;
            }

            // 查询对象vo
            DepRptVo deprptVo = new DepRptVo();
            // 短信类型
            deprptVo.setMstype(Integer.parseInt(msType));
            if (datasourcetype != null && datasourcetype.trim().length() > 0 && !"null".equals(datasourcetype)) {
                //发送类型
                deprptVo.setDatasourcetype(Integer.parseInt(datasourcetype));
            }
            //运营商
            deprptVo.setSpnumtype(Integer.parseInt(spnumtype));

            // 设置开始和结束时间
            //月报表
            if ("1".equals(reportType)) {
                //数据表中为int类型，这里去掉字符，变为纯数字
                begintime = begintime.replaceAll("-", "");
                begintime += "01";//加上日
                Date dateEndTime = sdfSeachDayTime.parse(begintime);
                Calendar ca = Calendar.getInstance();
                ca.setTime(dateEndTime);
                ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
                endtime = sdfSeachDayTime.format(ca.getTime());
            }
            //年报表
            else if ("2".equals(reportType)) {
                //用开始时间的年份来
                endtime = begintime + "1231";
                begintime += "0101";
            }
            //日报表
            else {
                //数据表中为int类型，这里去掉字符，变为纯数字
                begintime = begintime.replaceAll("-", "").trim();
                endtime = endtime.replaceAll("-", "").trim();
            }
            deprptVo.setSendTime(begintime);
            deprptVo.setEndTime(endtime);

            List<DepRptVo> deprptList = depReportBiz.getDepReportList(depids, deprptVo, user.getUserId(), corpCode, pageInfo);
            replaceString(deprptList, request);
            // 合计
            long[] sum;
            // 判断数据库类型如果是sqlserver则合计数据取集合中最后一条
            if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE && deprptVo.getMstype() != null && deprptVo.getMstype() == 0) {
                DepRptVo vo = deprptList.get(deprptList.size() - 1);
                sum = new long[5];
                sum[0] = vo.getIcount();
                sum[1] = vo.getRsucc();
                sum[2] = vo.getRfail1();
                sum[3] = vo.getRfail2();
                sum[4] = vo.getRnret();
                deprptList.remove(deprptList.size() - 1);
            } else {
                sum = depReportBiz.findSumCount(user.getUserId(), depids, deprptVo, corpCode);
            }
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
            //成功数和失败数都为0
            if (sum == null || sum[0] + sum[1] + sum[2] + sum[3] + sum[4] == 0) {
                deprptList = new ArrayList<DepRptVo>();
                pageInfo.setTotalPage(1);
                pageInfo.setTotalRec(0);
            }

            //获取加密解密对象
            ParamsEncryptOrDecrypt cryptor = (ParamsEncryptOrDecrypt) request.getSession(false).getAttribute("decryptObj");
            if (cryptor == null) {
                EmpExecutionContext.error("机构统计报表获取加密对象失败。");
                return;
            }
            //加密id
            cryptor.batchEncrypt(deprptList, "DepId", "SecretId");
            //分页对象
            request.setAttribute("pageInfo", pageInfo);

            /**
             * 对返回数据处理,当发送成功数为0和接受成功数为0 不显示
             * lianghuageng
             */
            if (null != deprptList) {
                Iterator<DepRptVo> iterator = deprptList.iterator();
                while (iterator.hasNext()) {
                    DepRptVo tmp = iterator.next();
                    if (tmp.getRnret() == 0 && tmp.getRfail2() == 0 && tmp.getIcount() == 0) {
                        iterator.remove();
                    }
                }
            }

            //机构结果
            request.setAttribute("resultList", deprptList);

        } catch (Exception e) {
            request.setAttribute("findresult", "-1");
            request.setAttribute("pageInfo", pageInfo);
            EmpExecutionContext.error(e, "机构统计报表查询异常。");
        } finally {
            long count = 0l;
            //从pageinfo中获取查询总条数
            if (pageInfo != null) {
                count = pageInfo.getTotalRec();
            }
            //开始时间
            String starthms = hms.format(startl);
            // 写日志
            String opContent = "机构统计报表查询：" + count + "条 开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "毫秒";
            EmpExecutionContext.info("机构统计报表", corpCode, userid, lgusername, opContent, StaticValue.GET);
            request.getRequestDispatcher(empRoot + base + "/rep_sysDepReport.jsp?lgcorpcode=" + corpCode + "&lguserid=" + userid).forward(request, response);
        }
    }

    private <cxtj_sjcx_report_jg> void replaceString(List<DepRptVo> deprptList, HttpServletRequest request) {
        // TODO Auto-generated method stub
        if (null == deprptList || deprptList.isEmpty()) {
            return;
        }
        String temp = null;
        for (DepRptVo vo : deprptList) {
            temp = vo.getDepName();
            temp = temp.replaceAll("未知机构", MessageUtils.extractMessage("cxtj", "cxtj_rpt_dep_wzjg", request));
            temp = temp.replaceAll("机构", MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_jg", request));
            temp = temp.replaceAll("未知操作员", MessageUtils.extractMessage("cxtj", "cxtj_rpt_dep_wzczy", request));
            temp = temp.replaceAll("操作员", MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_czy", request));
            vo.setDepName(temp);
        }
    }

    /**
     * 清空session里的查询条件
     *
     * @param request
     */
    private void clearSearchCondition(HttpServletRequest request) {
        // 获得session
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        //机构
        session.removeAttribute(RptStaticValue.PREFIX_RPT_DEP + "depNam");
        //发送类型
        session.removeAttribute(RptStaticValue.PREFIX_RPT_DEP + "datasourcetype");
        // 信息类型
        session.removeAttribute(RptStaticValue.PREFIX_RPT_DEP + "msType");
        //运营商
        session.removeAttribute(RptStaticValue.PREFIX_RPT_DEP + "spnumtype");
        //报表类型
        session.removeAttribute(RptStaticValue.PREFIX_RPT_DEP + "reportType");
        //查询开始时间
        session.removeAttribute(RptStaticValue.PREFIX_RPT_DEP + "begintime");
        //查询结束时间
        session.removeAttribute(RptStaticValue.PREFIX_RPT_DEP + "endtime");

        // 部门ID组合字符串
        session.removeAttribute(RptStaticValue.PREFIX_RPT_DEP + "deptString");
        //用于返回
        session.removeAttribute(RptStaticValue.PREFIX_RPT_DEP + "deprptdname");
        session.removeAttribute(RptStaticValue.PREFIX_RPT_DEP + "pageIndex");
        session.removeAttribute(RptStaticValue.PREFIX_RPT_DEP + "pageSize");
    }

    /**
     * 生成机构树字符串
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 机构iD
        String depId = request.getParameter("depId");
        try {
            //获取当前登录操作员对象
            LfSysuser curUser = getCurUserInSession(request);
            String depJson = reportBiz.getDepJosn(depId, curUser);
            if (depJson == null) {
                response.getWriter().print("");
            } else {
                response.getWriter().print(depJson);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询统计，生成机构树字符串，异常。depId=" + depId);
            response.getWriter().print("");
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
            return null;
        }
        return (LfSysuser) loginSysuserObj;
    }

    // 输出人员代码数据
    public void createUserTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // 企业编码
            String corpcode = queryBiz.getCorpCode(request);
            // 部门操作员树
            String deptUserTree = getDeptUserJosnData(corpcode);
            response.getWriter().print(deptUserTree);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "输出人员代码数据异常");
        }
    }

    // 获取人员代码数据
    public String getDeptUserJosnData(String corpCode) {
        List<LfDep> lfDeps;
        List<LfSysuser> lfSysusers;
        StringBuffer tree = null;
        try {
            // 查询出所有的机构
            lfDeps = reportBiz.getAllDepsByCorpCode(corpCode);
            LfDep lfDep = null;
            tree = new StringBuffer("[");
            for (int i = 0; i < lfDeps.size(); i++) {
                lfDep = lfDeps.get(i);
                tree.append("{");
                tree.append("id:").append(lfDep.getDepId());
                tree.append(",name:'").append(lfDep.getDepName()).append("'");
                tree.append(",pId:").append(lfDep.getSuperiorId());
                tree.append(",isParent:").append(true);
                tree.append(",nocheck:").append(true);
                tree.append("}");
                if (i != lfDeps.size() - 1) {
                    tree.append(",");
                }
            }

            lfSysusers = reportBiz.getAllSysusersByCorpCode(corpCode);
            LfSysuser lfSysuser = null;
            if (!lfSysusers.isEmpty()) {
                tree.append(",");
            }
            for (int i = 0; i < lfSysusers.size(); i++) {
                lfSysuser = lfSysusers.get(i);
                tree.append("{");
                tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
                tree.append(",name:'").append(lfSysuser.getName()).append("'");
                tree.append(",pId:").append(lfSysuser.getDepId());
                tree.append(",isParent:").append(false);
                tree.append("}");

                if (i != lfSysusers.size() - 1) {
                    tree.append(",");
                }
            }
            tree.append(",{");
            tree.append("id:'u").append("-1'");
            tree.append(",name:'").append("未知操作员'");
            tree.append(",pId:").append("1");
            tree.append(",isParent:").append(false);
            tree.append("}");

            tree.append("]");
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取人员数据异常");
        }
        return tree.toString();
    }

    /**
     * 将传递过来的机构，进行格式转换
     *
     * @param deptName
     * @return
     */
    public String getSpiltDepName(String deptName) {

        String returndepIds = "";

        // 选择机构名称，则按机构查询
        if (!"".equals(deptName) && null != deptName) {
            String[] deptIds = null;

            // 拼接机构ID字符串
            StringBuffer sbDep = new StringBuffer();

            deptIds = deptName.split(",");
            // 存储机构ID
            List<String> depIds = new ArrayList<String>();

            for (int i = 0; i < deptIds.length; i++) {

                depIds.add(deptIds[i].substring(deptIds[i].lastIndexOf(":") + 1));

            }
            for (int j = 0; j < depIds.size(); j++) {

                if (j == depIds.size() - 1) {
                    sbDep = sbDep.append(depIds.get(j).toString());

                } else {

                    sbDep = sbDep.append(depIds.get(j).toString() + ",");
                }

            }

            returndepIds = sbDep.toString();

        }
        return returndepIds;

    }

    /**
     * 将传递过来的用户数据，进行格式转换
     *
     * @param userName
     * @return
     */
    public String getSplitUserIds(String userName) {
        String usersIds = "";

        if (!"".equals(userName) && null != userName) {

            String[] userIds = new String[0];
            // 拼接操作员ID字符串
            StringBuffer sb = new StringBuffer();
            if (userName != null && !userName.equals("")) {

                userIds = userName.split(",");

            }
            // 存储操作员ID
            List<String> sysIds = new ArrayList<String>();
            if (userName != null && !userName.equals("")) {
                for (int i = 0; i < userIds.length; i++) {
                    sysIds.add(userIds[i].substring(1));

                }
            }
            for (int j = 0; j < sysIds.size(); j++) {
                if (j == sysIds.size() - 1) {
                    sb = sb.append(sysIds.get(j).toString());
                } else {
                    sb = sb.append(sysIds.get(j).toString() + ",");
                }
            }
            usersIds = sb.toString();

        }

        return usersIds;
    }

    /**
     * 机构报表导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void r_sdRptExportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //起始ms数
        long startl = System.currentTimeMillis();

        try {
            String queryTime = request.getParameter("queryTime");
            // 获取当前用户的企业编码
            String corpCode = queryBiz.getCorpCode(request);
            String userid = request.getParameter("lguserid");
            // 查询条件中的机构IDs
            String depids = request.getParameter("depNam");
            //短彩类型
            String mstype = request.getParameter("mstype");
            //数据源类型
            String datasourcetype = request.getParameter("datasourcetype");
            // 开始时间
            String begintime = request.getParameter("begintime");
            // 结束时间
            String endtime = request.getParameter("endtime");
            //运营商
            String spnumtype = request.getParameter("spnumtype");
            //报表类型
            String reportType = request.getParameter("reportType");

            if (mstype == null || mstype.trim().length() == 0 || "null".equals(mstype)) {
                //默认短信
                mstype = "0";
            }
            if (reportType == null || reportType.trim().length() == 0 || "null".equals(reportType)) {
                //默认月报表
                reportType = "0";
            }
            if (spnumtype == null || spnumtype.trim().length() == 0 || "null".equals(spnumtype)) {
                //默认国内
                spnumtype = "0";
            }
            if (begintime == null || begintime.trim().length() == 0 || "null".equals(begintime)) {
                //默认当前日期
                begintime = sdfSeachDay.format(new Date());
            }
            if (endtime == null || endtime.trim().length() == 0 || "null".equals(endtime)) {
                //默认当前日期月份的最后一天
                Date dateEndTime = sdfSeachDay.parse(begintime);
                Calendar ca = Calendar.getInstance();
                ca.setTime(dateEndTime);
                ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
                endtime = sdfSeachDay.format(ca.getTime());
            }

            // 获取当前登录操作员对象
            LfSysuser user = queryBiz.getCurrentUser(request);
            if (user == null) {
                EmpExecutionContext.error("机构统计报表查询导出，企业编码为空，获取不到登录操作员对象。");
                response.getWriter().print("false");
                return;
            }
            //当传入企业编码为空或空串则从session中取
            if (corpCode == null || userid == null || corpCode.trim().length() == 0 || "null".equals(corpCode)) {
                corpCode = user.getCorpCode();
                userid = user.getUserId().toString();
            }

            // 设置开始和结束时间
            //月报表
            if ("1".equals(reportType)) {
                //数据表中为int类型，这里去掉字符，变为纯数字
                begintime = begintime.replaceAll("-", "");
                begintime += "01";//加上日
                Date dateEndTime = sdfSeachDayTime.parse(begintime);
                Calendar ca = Calendar.getInstance();
                ca.setTime(dateEndTime);
                ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
                endtime = sdfSeachDayTime.format(ca.getTime());
            }
            //年报表
            else if ("2".equals(reportType)) {
                //用开始时间的年份来
                endtime = begintime + "1231";
                begintime += "0101";
            }
            //日报表
            else {
                //数据表中为int类型，这里去掉字符，变为纯数字
                begintime = begintime.replaceAll("-", "");
                endtime = endtime.replaceAll("-", "");
            }
            DepRptVo deprptvo = new DepRptVo();
            deprptvo.setSendTime(begintime);
            deprptvo.setEndTime(endtime);
            deprptvo.setMstype(Integer.parseInt(mstype));
            if (datasourcetype != null && datasourcetype.trim().length() > 0 && !"null".equals(datasourcetype)) {
                //发送类型
                deprptvo.setDatasourcetype(Integer.parseInt(datasourcetype));
            }
            deprptvo.setSpnumtype(Integer.parseInt(spnumtype));
            DepReportBiz depReportBiz = new DepReportBiz();

            List<DepRptVo> mdlist = depReportBiz.getDepReportList(depids, deprptvo, user.getUserId(), corpCode, null);
            //mdlist = depReportBiz.getDepRptVoListUnPage(permissionType, depids, deprptvo, user.getUserId(), corpCode);
            // 合计
            long[] sum;
            // 判断数据库类型如果是sqlserver则合计数据取集合中最后一条
            if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE && deprptvo.getMstype() != null && deprptvo.getMstype() == 0) {
                DepRptVo vo = mdlist.get(mdlist.size() - 1);
                sum = new long[5];
                sum[0] = vo.getIcount();
                sum[1] = vo.getRsucc();
                sum[2] = vo.getRfail1();
                sum[3] = vo.getRfail2();
                sum[4] = vo.getRnret();

                mdlist.remove(mdlist.size() - 1);
            } else {
                sum = depReportBiz.findSumCount(user.getUserId(), depids, deprptvo, corpCode);
            }

            // 获取session存储的 成功数失败数和时间字符串
            CountReportVo crptvo = new CountReportVo();
            crptvo.setIcount(sum[0]);
            crptvo.setRsucc(sum[1]);
            crptvo.setRfail1(sum[2]);
            crptvo.setRfail2(sum[3]);
            crptvo.setRnret(sum[4]);

            HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
            String showTime = session.getAttribute("showTime").toString();
            String datasourcename;
            if (deprptvo.getDatasourcetype() == 0) {
                datasourcename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qb", request);
            } else if (deprptvo.getDatasourcetype() == 1) {
                datasourcename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_empfs", request);
            } else if (deprptvo.getDatasourcetype() == 2) {
                datasourcename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_httpjr", request);
            } else if (deprptvo.getDatasourcetype() == 3) {
                datasourcename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_dbjr", request);
            } else if (deprptvo.getDatasourcetype() == 4) {
                datasourcename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_zljr", request);
            } else {
                datasourcename = "--";
            }
            String spnumtypename;
            if (deprptvo.getSpnumtype() == -1) {
                spnumtypename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qb", request);
            } else if (deprptvo.getSpnumtype() == 0) {
                spnumtypename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gn", request);
            } else if (deprptvo.getSpnumtype() == 1) {
                spnumtypename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gw", request);
            } else {
                spnumtypename = "--";
            }

            if (mdlist == null || mdlist.size() == 0) {
                response.getWriter().print("false");
                return;
            }
            response.setContentType("html/text");
            /**
             * 对返回数据处理,当发送成功数为0和接受成功数为0 不导出
             * moll
             */
            if (null != mdlist) {
                Iterator<DepRptVo> iterator = mdlist.iterator();
                while (iterator.hasNext()) {
                    DepRptVo tmp = iterator.next();
                    if (tmp.getRnret() == 0 && tmp.getRfail2() == 0 && tmp.getIcount() == 0) {
                        iterator.remove();
                    }
                }
            }
            Map<String, String> resultMap = depReportBiz.createSysDepReportExcel(mdlist, queryTime, crptvo, showTime, datasourcename, spnumtypename, request);
            String fileName = (String) resultMap.get("FILE_NAME");
            //开始时间
            String starthms = hms.format(startl);
            // 写日志
            String opContent = "导出机构统计报表：" + mdlist.size() + "条  ,文件名：" + fileName + ",开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            new QueryBiz().setLog(request, "机构统计报表", opContent, StaticValue.GET);
            mdlist.clear();
            mdlist = null;
            session.setAttribute("dep_export_map", resultMap);
            response.getWriter().print("true");
        } catch (Exception e) {
            // 异常处理
            EmpExecutionContext.error(e, "机构报表导出异常。");
            response.getWriter().print("false");
        }
    }

    /**
     * 机构统计报表下载导出文件
     *
     * @param request
     * @param response
     */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("dep_export_map");
        session.removeAttribute("dep_export_map");
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
     * 查看详细信息导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    // excel导出全部数据
    public void r_sdRptDetailExportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //起始ms数
        long startl = System.currentTimeMillis();

        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        try {
            // 类型
            String mstype = request.getParameter("mstype");
            // 企业编码
            String corpCode = queryBiz.getCorpCode(request);
            //报表类型
            String reportType = request.getParameter("reportType");
            //idtype  1 机构  2操作员  3未知
            String idtype = request.getParameter("idtype");
            // ID  机构id  操作员id  或者 null（未知）
            String id = request.getParameter("id");
            // 开始时间
            String begintime = request.getParameter("begintime");
            // 结束时间
            String endtime = request.getParameter("endtime");
            //数据源类型
            String datasourcetype = request.getParameter("datasourcetype");
            //运营商
            String spnumtype = request.getParameter("spnumtype");

            if (mstype == null || mstype.trim().length() == 0 || "null".equals(mstype)) {
                //默认短信
                mstype = "0";
            }
            if (reportType == null || reportType.trim().length() == 0 || "null".equals(reportType)) {
                //默认月报表
                reportType = "0";
            }
            if (spnumtype == null || spnumtype.trim().length() == 0 || "null".equals(spnumtype)) {
                //默认国内
                spnumtype = "0";
            }
            if (begintime == null || begintime.trim().length() == 0 || "null".equals(begintime)) {
                //默认当前日期
                begintime = sdfSeachDay.format(new Date());
            }
            if (endtime == null || endtime.trim().length() == 0 || "null".equals(endtime)) {
                //默认当前日期月份的最后一天
                Date dateEndTime = sdfSeachDay.parse(begintime);
                Calendar ca = Calendar.getInstance();
                ca.setTime(dateEndTime);
                ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
                endtime = sdfSeachDay.format(ca.getTime());
            }
            // 获取当前登录操作员对象
            LfSysuser user = queryBiz.getCurrentUser(request);
            if (user == null) {
                EmpExecutionContext.error("机构统计报表查询，企业编码为空，获取不到登录操作员对象。");
                return;
            }
            // 查询对象vo
            DepRptVo deprptvo = new DepRptVo();
            // 短信类型
            deprptvo.setMstype(Integer.parseInt(mstype));
            deprptvo.setReporttype(Integer.parseInt(reportType));
            if (datasourcetype != null && datasourcetype.trim().length() > 0 && !"null".equals(datasourcetype)) {
                //发送类型
                deprptvo.setDatasourcetype(Integer.parseInt(datasourcetype));
            }
            deprptvo.setSpnumtype(Integer.parseInt(spnumtype));
            //1 机构  2操作员  3未知
            deprptvo.setIdtype(idtype);
            deprptvo.setSendTime(begintime);
            deprptvo.setEndTime(endtime);

            //获取加密解密对象
            ParamsEncryptOrDecrypt cryptor = (ParamsEncryptOrDecrypt) request.getSession(false).getAttribute("decryptObj");
            if (cryptor == null) {
                EmpExecutionContext.error("机构统计报表获取解密对象失败。");
                return;
            }
            //获取是否开启p2参数配置
            String depcodethird2p2 = SystemGlobals.getValue("depcodethird2p2");
            //获取不到配置文件值
            if (depcodethird2p2 == null || depcodethird2p2.trim().length() == 0) {
                depcodethird2p2 = "false";
            }
            if ("true".equals(depcodethird2p2)) {
                // 部门ID组合字符串
                String depids = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_DEP + "deptString");
                // 如果没有选择任何机构赋值当前登录人的部门id
                if (depids != null && !"".equals(depids.trim())) {
                    deprptvo.setDepName(depids);
                } else {
                    List<LfDomination> lfdom = new SpecialDAO().findDomDepIdByUserID(user.getUserId() + "");
                    if (lfdom != null && lfdom.size() > 0) {
                        LfDomination lfd = lfdom.get(0);
                        deprptvo.setDepName(lfd.getDepId().toString());
                    } else {
                        EmpExecutionContext.error("机构统计报表，合计，获取不到管辖机构。curUserId=" + user.getUserId());
                        deprptvo.setDepName("0");
                    }
                }
            } else {

            }

            //解密
            id = cryptor.decrypt(id);
            List<DynaBean> deprptlist = depReportBiz.getDetailListByIdtype(id, deprptvo, corpCode, null);

            // 合计
            long[] sum = depReportBiz.findDetailSumCount(id, deprptvo, corpCode);
            // 获取session存储的 成功数失败数和时间字符串
            CountReportVo crptvo = new CountReportVo();
            crptvo.setIcount(sum[0]);
            crptvo.setRsucc(sum[1]);
            crptvo.setRfail1(sum[2]);
            crptvo.setRfail2(sum[3]);
            crptvo.setRnret(sum[4]);

            //操作员名称
            String userdepname = session.getAttribute("userdepname").toString();
            //发送方式
            String datasourcenamedetail;
            if (deprptvo.getDatasourcetype() == 0) {
                //datasourcenamedetail="全部";
                datasourcenamedetail = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qb", request);
            } else if (deprptvo.getDatasourcetype() == 1) {
                datasourcenamedetail = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_empfs", request);
            } else if (deprptvo.getDatasourcetype() == 2) {
                datasourcenamedetail = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_httpjr", request);
            } else if (deprptvo.getDatasourcetype() == 3) {
                datasourcenamedetail = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_dbjr", request);
            } else if (deprptvo.getDatasourcetype() == 4) {
                datasourcenamedetail = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_zljr", request);
            } else {
                datasourcenamedetail = "--";
            }

            //运营商
            String spnumtypename;
            if (deprptvo.getSpnumtype() == 0) {
                spnumtypename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qb", request);
            } else if (deprptvo.getSpnumtype() == 1) {
                spnumtypename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gn", request);
            } else if (deprptvo.getSpnumtype() == -1) {
                spnumtypename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gw", request);
            } else {
                spnumtypename = "--";
            }

            response.setContentType("html/text");
            if (deprptlist == null || deprptlist.size() == 0) {
                response.getWriter().print("false");
                return;
            }

            Map<String, String> resultMap = depReportBiz.createSysDepDetailReportExcel(deprptlist, deprptvo.getReporttype(), crptvo, datasourcenamedetail, userdepname, spnumtypename, request);
            String fileName = (String) resultMap.get("FILE_NAME");
            //开始时间
            String starthms = hms.format(startl);
            // 写日志
            String opContent = "导出机构统计报表详情：" + deprptlist.size() + "条  ,文件名：" + fileName + ",开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            new QueryBiz().setLog(request, "机构统计报表详情", opContent, StaticValue.GET);
            deprptlist.clear();
            deprptlist = null;
            session.setAttribute("depdetail_export_map", resultMap);
            response.getWriter().print("true");
        } catch (Exception e) {
            // 异常处理
            EmpExecutionContext.error(e, "机构报表详情导出异常。");
            response.getWriter().print("false");
        }
    }


    /**
     * 机构统计报表详情下载导出文件
     *
     * @param request
     * @param response
     */
    public void depdetaildownloadFile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("depdetail_export_map");
        session.removeAttribute("depdetail_export_map");
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
     * 查看详细信息
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void findInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //起始ms数
        long startl = System.currentTimeMillis();

        // 类型
        String mstype = request.getParameter("mstype");
        // 企业编码
        String corpCode = queryBiz.getCorpCode(request);
        // 登录操作员id
        String userid = request.getParameter("lguserid");
        //报表类型
        String reportType = request.getParameter("reportType");
        //idtype  1 机构  2操作员  3未知
        String idtype = request.getParameter("idtype");
        // ID  机构id  操作员id  或者 null（未知）
        String id = request.getParameter("id");
        //数据源类型
        String datasourcetype = request.getParameter("datasourcetype");
        //运营商
        String spnumtype = request.getParameter("spnumtype");
        // 开始时间
        String begintime = request.getParameter("begintime");
        // 结束时间
        String endtime = request.getParameter("endtime");

        PageInfo pageInfo = new PageInfo();
        try {
            pageSet(pageInfo, request);
            if (mstype == null || mstype.trim().length() == 0 || "null".equals(mstype)) {
                //默认短信
                mstype = "0";
            }
            if (reportType == null || reportType.trim().length() == 0 || "null".equals(reportType)) {
                //默认月报表
                reportType = "0";
            }
            if (spnumtype == null || spnumtype.trim().length() == 0 || "null".equals(spnumtype)) {
                //默认国内
                spnumtype = "0";
            }
            if (begintime == null || begintime.trim().length() == 0 || "null".equals(begintime)) {
                //默认当前日期
                begintime = sdfSeachDay.format(new Date());
            }
            if (endtime == null || endtime.trim().length() == 0 || "null".equals(endtime)) {
                //默认当前日期月份的最后一天
                Date dateEndTime = sdfSeachDay.parse(begintime);
                Calendar ca = Calendar.getInstance();
                ca.setTime(dateEndTime);
                ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
                endtime = sdfSeachDay.format(ca.getTime());
            }

            if (idtype == null || idtype.trim().length() == 0) {
                request.setAttribute("detailicount", 0);
                request.setAttribute("detailrsucc", 0);
                request.setAttribute("detailrfail1", 0);
                request.setAttribute("detailrfail2", 0);
                request.setAttribute("detailrnret", 0);
                return;
            }
            // 获取当前登录操作员对象
            LfSysuser user = queryBiz.getCurrentUser(request);
            if (user == null) {
                EmpExecutionContext.error("机构统计报表查询，企业编码为空，获取不到登录操作员对象。");
                return;
            }
            // 查询对象vo
            DepRptVo deprptvo = new DepRptVo();
            // 短信类型
            deprptvo.setMstype(Integer.parseInt(mstype));
            deprptvo.setReporttype(Integer.parseInt(reportType));
            if (datasourcetype != null && datasourcetype.trim().length() > 0 && !"null".equals(datasourcetype)) {
                //发送类型
                deprptvo.setDatasourcetype(Integer.parseInt(datasourcetype));
            }

            deprptvo.setSpnumtype(Integer.parseInt(spnumtype));
            //1 机构  2操作员  3未知
            deprptvo.setIdtype(idtype);

            // 设置开始和结束时间
            //月报表
            if ("1".equals(reportType)) {
                //数据表中为int类型，这里去掉字符，变为纯数字
                begintime = begintime.replaceAll("-", "");
                begintime += "01";//加上日
                Date dateEndTime = sdfSeachDayTime.parse(begintime);
                Calendar ca = Calendar.getInstance();
                ca.setTime(dateEndTime);
                ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
                endtime = sdfSeachDayTime.format(ca.getTime());
            }
            //年报表
            else if ("2".equals(reportType)) {
                //用开始时间的年份来
                endtime = begintime + "1231";
                begintime += "0101";
            }
            //日报表
            else {
                //数据表中为int类型，这里去掉字符，变为纯数字
                begintime = begintime.replaceAll("-", "");
                endtime = endtime.replaceAll("-", "");
            }
            deprptvo.setSendTime(begintime);
            deprptvo.setEndTime(endtime);
            //获取是否开启p2参数配置
            String depcodethird2p2 = SystemGlobals.getValue("depcodethird2p2");
            //获取不到配置文件值
            if (depcodethird2p2 == null || depcodethird2p2.trim().length() == 0) {
                depcodethird2p2 = "false";
            }
            if ("true".equals(depcodethird2p2)) {
                HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
                // 部门ID组合字符串
                String depids = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_DEP + "deptString");
                // 如果没有选择任何机构赋值当前登录人的部门id
                if (depids != null && !"".equals(depids.trim())) {
                    deprptvo.setDepName(depids);
                } else {
                    List<LfDomination> lfdom = new SpecialDAO().findDomDepIdByUserID(user.getUserId() + "");
                    if (lfdom != null && lfdom.size() > 0) {
                        LfDomination lfd = lfdom.get(0);
                        deprptvo.setDepName(lfd.getDepId().toString());
                    } else {
                        EmpExecutionContext.error("机构统计报表，合计，获取不到管辖机构。curUserId=" + user.getUserId());
                        deprptvo.setDepName("0");
                    }
                }
            } else {

            }
            //获取加密解密对象
            ParamsEncryptOrDecrypt cryptor = (ParamsEncryptOrDecrypt) request.getSession(false).getAttribute("decryptObj");
            if (cryptor == null) {
                EmpExecutionContext.error("机构统计报表获取解密对象失败。");
                return;
            }
            //解密
            id = cryptor.decrypt(id);

            List<DynaBean> deprptlist = depReportBiz.getDetailListByIdtype(id, deprptvo, corpCode, pageInfo);
            // 合计
            long[] sum = depReportBiz.findDetailSumCount(id, deprptvo, corpCode);
            //提交总数
            request.setAttribute("detailicount", sum[0]);
            //接收成功数
            request.setAttribute("detailrsucc", sum[1]);
            //发送失败数
            request.setAttribute("detailrfail1", sum[2]);
            //接收失败数
            request.setAttribute("detailrfail2", sum[3]);
            //未返数
            request.setAttribute("detailrnret", sum[4]);
//			if(sum == null || sum[0] + sum[1] + sum[2] + sum[3] + sum[4] == 0)
//			{
//				deprptlist=new ArrayList<DynaBean>();
//				pageInfo.setTotalPage(1);
//				pageInfo.setTotalRec(0);
//			}
//			
            String userdepname;
            if ("1".equals(idtype)) {
                LfDep lfdep = new BaseBiz().getById(LfDep.class, id);
                if (lfdep != null) {
                    userdepname = "[" + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_jg", request) + "]" + (lfdep.getDepName() == null ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request) : lfdep.getDepName().toString().replace("<", "&lt;").replace(">", "&gt;"));
                } else {
                    userdepname = "[" + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_jg", request) + "]" + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request);
                }
            } else if ("2".equals(idtype)) {
                LfSysuser lfsysuser = new BaseBiz().getById(LfSysuser.class, id);
                if (lfsysuser != null) {
                    userdepname = "[" + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_czy", request) + "]" + (lfsysuser.getUserName() == null ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request) : lfsysuser.getUserName().toString().replace("<", "&lt;").replace(">", "&gt;"));
                    if (lfsysuser.getUserName() != null && lfsysuser.getUserState() != null && lfsysuser.getUserState() == 2) {
                        userdepname = userdepname + "<font color='red'>(" + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_jgtjbb_yzx", request) + ")</font>";
                    }
                } else {
                    userdepname = "[" + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_czy", request) + "]" + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request);
                }
            } else if ("3".equals(idtype)) {
                userdepname = MessageUtils.extractMessage("cxtj", "cxtj_rpt_dep_wzjg", request);
            } else {
                userdepname = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request);
            }
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("userdepname", userdepname);

            //分页对象
            request.setAttribute("pageInfo", pageInfo);
            //机构结果
            request.setAttribute("detailList", deprptlist);
            request.setAttribute("deprptvodetail", deprptvo);

            long count = 0l;
            //从pageinfo中获取查询总条数
            if (pageInfo != null) {
                count = pageInfo.getTotalRec();
            }
            //开始时间
            String starthms = hms.format(startl);
            // 写日志
            String opContent = "机构统计报表详情查询：" + count + "条 开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            new QueryBiz().setLog(request, "机构统计报表详情", opContent, StaticValue.GET);
        } catch (Exception e) {
            request.setAttribute("findresult", "-1");
            request.setAttribute("pageInfo", pageInfo);
            EmpExecutionContext.error(e, "机构报表查询，详情，异常。");
        } finally {
            request.getRequestDispatcher(empRoot + base + "/rep_sysDepDetail.jsp?lgcorpcode=" + corpCode + "&lguserid=" + userid).forward(request, response);
        }
    }

    /**
     * 各国详情
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void findAreaInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //起始ms数
        long startl = System.currentTimeMillis();

        // 获得session
        //HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        // 类型
        String mstype = request.getParameter("mstype");
        // 企业编码
        String corpCode = queryBiz.getCorpCode(request);
        // 登录操作员id
        String userid = request.getParameter("lguserid");
        //报表类型
        String reportType = request.getParameter("reportType");
        //idtype  1 机构  2操作员  3未知
        String idtype = request.getParameter("idtype");
        // ID  机构id  操作员id  或者 null（未知）
        String id = request.getParameter("id");
        // 开始时间
        String begintime = request.getParameter("begintime");
        // 结束时间
        String endtime = request.getParameter("endtime");
        //数据源类型
        String datasourcetype = request.getParameter("datasourcetype");
        //运营商
        String spnumtype = request.getParameter("spnumtype");
        //国家代码
        String areacode = request.getParameter("areacode");
        //国家名称
        String areaname = request.getParameter("areaname");

        PageInfo pageInfo = new PageInfo();
        try {
            pageSet(pageInfo, request);
            if (mstype == null || mstype.trim().length() == 0 || "null".equals(mstype)) {
                //默认短信
                mstype = "0";
            }
            if (reportType == null || reportType.trim().length() == 0 || "null".equals(reportType)) {
                //默认月报表
                reportType = "0";
            }
            if (spnumtype == null || spnumtype.trim().length() == 0 || "null".equals(spnumtype)) {
                //默认国内
                spnumtype = "0";
            }
            if (begintime == null || begintime.trim().length() == 0 || "null".equals(begintime)) {
                //默认当前日期
                begintime = sdfSeachDay.format(new Date());
            }
            if (endtime == null || endtime.trim().length() == 0 || "null".equals(endtime)) {
                //默认当前日期月份的最后一天
                Date dateEndTime = sdfSeachDay.parse(begintime);
                Calendar ca = Calendar.getInstance();
                ca.setTime(dateEndTime);
                ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
                endtime = sdfSeachDay.format(ca.getTime());
            }

            if (idtype == null || idtype.trim().length() == 0) {
                // 提交总数
                request.setAttribute("detailareaicount", 0);
                // 接收成功数
                request.setAttribute("detailarearsucc", 0);
                // 发送失败数
                request.setAttribute("detailarearfail1", 0);
                // 接收失败数
                request.setAttribute("detailarearfail2", 0);
                // 未返数
                request.setAttribute("detailarearnret", 0);
                return;
            }

            // 查询对象vo
            DepAreaRptVo deparearptvo = new DepAreaRptVo();
            // 短信类型
            deparearptvo.setMstype(Integer.parseInt(mstype));
            deparearptvo.setReporttype(Integer.parseInt(reportType));
            if (datasourcetype != null && datasourcetype.trim().length() > 0 && !"null".equals(datasourcetype)) {
                //发送类型
                deparearptvo.setDatasourcetype(Integer.parseInt(datasourcetype));
            }
            deparearptvo.setSpnumtype(Integer.parseInt(spnumtype));
            deparearptvo.setAreacode(areacode);
            deparearptvo.setAreaname(areaname);
            //1 机构  2操作员  3未知
            deparearptvo.setIdtype(idtype);

            // 设置开始和结束时间
            //月报表
            if ("1".equals(reportType)) {
                //数据表中为int类型，这里去掉字符，变为纯数字
                begintime = begintime.replaceAll("-", "");
                begintime += "01";//加上日
                Date dateEndTime = sdfSeachDayTime.parse(begintime);
                Calendar ca = Calendar.getInstance();
                ca.setTime(dateEndTime);
                ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
                endtime = sdfSeachDayTime.format(ca.getTime());
            }
            //年报表
            else if ("2".equals(reportType)) {
                //用开始时间的年份来
                endtime = begintime + "1231";
                begintime += "0101";
            }
            //日报表
            else {
                //数据表中为int类型，这里去掉字符，变为纯数字
                begintime = begintime.replaceAll("-", "");
                endtime = endtime.replaceAll("-", "");
            }
            deparearptvo.setSendtime(begintime);
            deparearptvo.setEndtime(endtime);

            //获取加密解密对象
            ParamsEncryptOrDecrypt cryptor = (ParamsEncryptOrDecrypt) request.getSession(false).getAttribute("decryptObj");
            if (cryptor == null) {
                EmpExecutionContext.error("机构统计报表获取解密对象失败。");
                return;
            }
            //解密
            id = cryptor.decrypt(id);

            List<DynaBean> deprptarealist = depReportBiz.getAreaDetailListByIdtype(id, deparearptvo, corpCode, pageInfo);
            // 合计
            long[] sum = depReportBiz.findAreaDetailSumCount(id, deparearptvo, corpCode);
            // 提交总数
            request.setAttribute("detailareaicount", sum[0]);
            // 接收成功数
            request.setAttribute("detailarearsucc", sum[1]);
            // 发送失败数
            request.setAttribute("detailarearfail1", sum[2]);
            // 接收失败数
            request.setAttribute("detailarearfail2", sum[3]);
            // 未返数
            request.setAttribute("detailarearnret", sum[4]);
//            if(sum == null || sum[0] + sum[1] + sum[2] + sum[3] + sum[4] == 0)
//			{
//				deprptarealist=new ArrayList<DynaBean>();
//				pageInfo.setTotalPage(1);
//				pageInfo.setTotalRec(0);
//			}
//			
            String userdepareaname;
            //机构
            if ("1".equals(idtype)) {
                LfDep lfdep = new BaseBiz().getById(LfDep.class, id);
                if (lfdep != null) {
                    userdepareaname = "[" + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_jg", request) + "]" + (lfdep.getDepName() == null ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request) : lfdep.getDepName().toString().replace("<", "&lt;").replace(">", "&gt;"));
                } else {
                    userdepareaname = "[" + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_jg", request) + "]" + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request);
                }
            }
            //操作员
            else if ("2".equals(idtype)) {
                LfSysuser lfsysuser = new BaseBiz().getById(LfSysuser.class, id);
                if (lfsysuser != null) {
                    userdepareaname = "[" + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_czy", request) + "]" + (lfsysuser.getUserName() == null ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request) : lfsysuser.getUserName().toString().replace("<", "&lt;").replace(">", "&gt;"));
                    if (lfsysuser.getUserName() != null && lfsysuser.getUserState() != null && lfsysuser.getUserState() == 2) {
                        userdepareaname = userdepareaname + "<font color='red'>(" + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_jgtjbb_yzx", request) + ")</font>";
                    }
                } else {
                    userdepareaname = "[" + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_czy", request) + "]" + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request);
                }
            }
            //未知
            else if ("3".equals(idtype)) {
                userdepareaname = MessageUtils.extractMessage("cxtj", "cxtj_rpt_dep_wzjg", request);
            } else {
                userdepareaname = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_wz", request);
            }
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("userareadepname", userdepareaname);

            //分页对象
            request.setAttribute("pageInfo", pageInfo);
            //机构结果
            request.setAttribute("detailareaList", deprptarealist);
            request.setAttribute("deparearptvodetail", deparearptvo);

            long count = 0l;
            //从pageinfo中获取查询总条数
            if (pageInfo != null) {
                count = pageInfo.getTotalRec();
            }

            //开始时间
            String starthms = hms.format(startl);
            // 写日志
            String opContent = "机构统计报表各国详情：" + count + "条 开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            new QueryBiz().setLog(request, "机构统计报表各国详情", opContent, StaticValue.GET);
        } catch (Exception e) {
            request.setAttribute("findresult", "-1");
            request.setAttribute("pageInfo", pageInfo);
            EmpExecutionContext.error(e, "机构统计报表，各国详情查询，异常。");
        } finally {
            request.getRequestDispatcher(empRoot + base + "/rep_sysDepAreaDetail.jsp?lgcorpcode=" + corpCode + "&lguserid=" + userid).forward(request, response);
        }
    }

    /**
     * 查看详细信息导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    // excel导出全部数据
    public void r_sdRptAreaDetailExportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //起始ms数
        long startl = System.currentTimeMillis();
        try {
            // 类型
            String mstype = request.getParameter("mstype");
            // 企业编码
            String corpCode = queryBiz.getCorpCode(request);
            // 登录操作员id
            //String userid = request.getParameter("lguserid");
            //报表类型
            String reportType = request.getParameter("reportType");
            //idtype  1 机构  2操作员  3未知
            String idtype = request.getParameter("idtype");
            // ID  机构id  操作员id  或者 null（未知）
            String id = request.getParameter("id");
            // 开始时间
            String begintime = request.getParameter("begintime");
            // 结束时间
            String endtime = request.getParameter("endtime");
            //数据源类型
            String datasourcetype = request.getParameter("datasourcetype");
            //运营商
            String spnumtype = request.getParameter("spnumtype");
            //国家代码
            String areacode = request.getParameter("areacode");
            //国家名称
            String areaname = request.getParameter("areaname");

            if (mstype == null || mstype.trim().length() == 0 || "null".equals(mstype)) {
                //默认短信
                mstype = "0";
            }
            if (reportType == null || reportType.trim().length() == 0 || "null".equals(reportType)) {
                //默认月报表
                reportType = "0";
            }
            if (spnumtype == null || spnumtype.trim().length() == 0 || "null".equals(spnumtype)) {
                //默认国内
                spnumtype = "0";
            }
            if (begintime == null || begintime.trim().length() == 0 || "null".equals(begintime)) {
                //默认当前日期
                begintime = sdfSeachDay.format(new Date());
            }
            if (endtime == null || endtime.trim().length() == 0 || "null".equals(endtime)) {
                //默认当前日期月份的最后一天
                Date dateEndTime = sdfSeachDay.parse(begintime);
                Calendar ca = Calendar.getInstance();
                ca.setTime(dateEndTime);
                ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
                endtime = sdfSeachDay.format(ca.getTime());
            }

            // 查询对象vo
            DepAreaRptVo deparearptvo = new DepAreaRptVo();
            // 短信类型
            deparearptvo.setMstype(Integer.parseInt(mstype));
            deparearptvo.setReporttype(Integer.parseInt(reportType));
            if (datasourcetype != null && datasourcetype.trim().length() > 0 && !"null".equals(datasourcetype)) {
                //发送类型
                deparearptvo.setDatasourcetype(Integer.parseInt(datasourcetype));
            }
            deparearptvo.setSpnumtype(Integer.parseInt(spnumtype));
            deparearptvo.setAreacode(areacode);
            deparearptvo.setAreaname(areaname);
            //1 机构  2操作员  3未知
            deparearptvo.setIdtype(idtype);
            //日报表不做处理
            deparearptvo.setSendtime(begintime);
            deparearptvo.setEndtime(endtime);

            //获取加密解密对象
            ParamsEncryptOrDecrypt cryptor = (ParamsEncryptOrDecrypt) request.getSession(false).getAttribute("decryptObj");
            if (cryptor == null) {
                EmpExecutionContext.error("机构统计报表获取解密对象失败。");
                return;
            }
            //解密
            id = cryptor.decrypt(id);

            List<DynaBean> deprptarealist = depReportBiz.getAreaDetailListByIdtype(id, deparearptvo, corpCode, null);

            // 合计
            long[] sum = depReportBiz.findAreaDetailSumCount(id, deparearptvo, corpCode);

            // 获取session存储的 成功数失败数和时间字符串
            CountReportVo crptvo = new CountReportVo();
            crptvo.setIcount(sum[0]);
            crptvo.setRsucc(sum[1]);
            crptvo.setRfail1(sum[2]);
            crptvo.setRfail2(sum[3]);
            crptvo.setRnret(sum[4]);

            HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
            //操作员名称
            String userareadepname = session.getAttribute("userareadepname") != null ? session.getAttribute("userareadepname").toString() : "";
            //发送方式
            String datasourcenameareadetail;
            if (deparearptvo.getDatasourcetype() == 0) {
                //datasourcenameareadetail="全部";
                datasourcenameareadetail = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qb", request);
            } else if (deparearptvo.getDatasourcetype() == 1) {
                datasourcenameareadetail = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_empfs", request);
            } else if (deparearptvo.getDatasourcetype() == 2) {
                datasourcenameareadetail = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_httpjr", request);
            } else if (deparearptvo.getDatasourcetype() == 3) {
                datasourcenameareadetail = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_jbjr", request);
            } else if (deparearptvo.getDatasourcetype() == 4) {
                datasourcenameareadetail = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_zljr", request);
            } else {
                datasourcenameareadetail = "--";
            }

            String showareatime = session.getAttribute("showareatime").toString();
            response.setContentType("html/text");
            if (deprptarealist == null || deprptarealist.size() == 0) {
                response.getWriter().print("false");
                return;
            }
            Map<String, String> resultMap = depReportBiz.createSysDepAreaDetailReportExcel(deprptarealist, deparearptvo.getReporttype(), crptvo, datasourcenameareadetail, userareadepname, showareatime, request);
            String fileName = (String) resultMap.get("FILE_NAME");
            //开始时间
            String starthms = hms.format(startl);
            // 写日志
            String opContent = "导出机构统计报表国外详情：" + deprptarealist.size() + "条  ,文件名：" + fileName + ",开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            new QueryBiz().setLog(request, "机构统计报表国外详情", opContent, StaticValue.GET);
            deprptarealist.clear();
            deprptarealist = null;
            session.setAttribute("depnation_export_map", resultMap);
            response.getWriter().print("true");
        } catch (Exception e) {
            // 异常处理
            EmpExecutionContext.error(e, "机构报表各国详情导出异常。");
            response.getWriter().print("false");
        }
    }

    /**
     * 机构报表国外详情下载导出文件
     *
     * @param request
     * @param response
     */
    public void depNationdownloadFile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("depnation_export_map");
        session.removeAttribute("depnation_export_map");
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
