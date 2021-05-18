package com.montnets.emp.report.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.GlobConfigBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.system.LfPageField;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.query.biz.QueryBiz;
import com.montnets.emp.report.bean.RptStaticValue;
import com.montnets.emp.report.biz.OperatorReportBiz;
import com.montnets.emp.report.biz.ReportBiz;
import com.montnets.emp.report.vo.CountReportVo;
import com.montnets.emp.report.vo.MtDataReportVo;
import com.montnets.emp.report.vo.UserAreaRptVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 操作员统计报表
 *
 * @author liaojirong <ljr0300@163.com>
 * @project p_cxtj
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:48:52
 * @description
 */
@SuppressWarnings("serial")
public class rep_sysUserReportSvt extends BaseServlet {
    // 模块名称
    private final String empRoot = "cxtj";

    // 功能文件夹名
    private final String base = "/report";

    /**
     * 查询日期格式化（年月日）：yyyyMMdd
     */
    private final SimpleDateFormat sdfSeachDayTime = new SimpleDateFormat("yyyyMMdd");

    /**
     * 查询日期格式化（年-月-日）：yyyy-MM-dd
     */
    private final SimpleDateFormat sdfSeachDay = new SimpleDateFormat("yyyy-MM-dd");

    // 操作员报表biz
    private final OperatorReportBiz opReportBiz = new OperatorReportBiz();

    // 报表biz
    private final ReportBiz reportBiz = new ReportBiz();
    private final QueryBiz queryBiz = new QueryBiz();

    //时分秒格式化
    private final SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");

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
     * 操作员统计报表
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //起始ms数
        long startl = System.currentTimeMillis();

        // 短彩类型
        String msType = request.getParameter("msType");
        // 企业编码
        String lgcorpcode = "";
        // 登录操作员id
        String lguserid = "";
        String lgusername = request.getParameter("lgusername");
        //报表类型
        String reportType = request.getParameter("reportType");
        // 部门ID组合字符串
        String depids = request.getParameter("deptString");
        // 操作员ID组合字符串
        String userName = request.getParameter("userString");
        String begintime = request.getParameter("begintime");
        String endtime = request.getParameter("endtime");
        //运营商
        String spnumtype = request.getParameter("spnumtype");
        String depNam = request.getParameter("depNam");
        String uName = request.getParameter("userName");

        // 是否第一次访问
        PageInfo pageInfo = new PageInfo();
        boolean isFirstEnter = pageSet(pageInfo, request);
        //进入标记，空为从左侧菜单进入；1为从详情进入
        String inFlag = request.getParameter("inFlag");
        // 获取当前登录操作员对象

        LfSysuser user = queryBiz.getCurrentUser(request);
        if (user == null) {
            EmpExecutionContext.error("操作员统计报表查询，获取不到登录操作员对象。lgcorpcode=" + lgcorpcode + ",lguserid=" + lguserid);
            return;
        }
        //没有机构权限
/*		if(user.getPermissionType()!=2){
			request.getRequestDispatcher("/common/no_dep_permission.jsp").forward(
					request, response);
		}*/
        // 直接从session中取企业编码和操作员id
        lgcorpcode = user.getCorpCode();
        lguserid = user.getUserId().toString();

        try {
            request.setAttribute("isFirstEnter", isFirstEnter);

            // 获取数据库短彩类型数据
            GlobConfigBiz gcBiz = new GlobConfigBiz();
            List<LfPageField> pagefileds = gcBiz.getPageFieldById("100001");
            request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("pagefiledusers", pagefileds);

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
                //默认日报表
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
            if (session != null) {
                //从菜单进入，则设置查询条件session
                if (inFlag == null || inFlag.trim().length() == 0) {
                    //发送类型
                    session.setAttribute(RptStaticValue.PREFIX_RPT_USER + "msType", msType);
                    //运营商
                    session.setAttribute(RptStaticValue.PREFIX_RPT_USER + "spnumtype", spnumtype);
                    //报表类型
                    session.setAttribute(RptStaticValue.PREFIX_RPT_USER + "reportType", reportType);
                    //查询开始时间
                    session.setAttribute(RptStaticValue.PREFIX_RPT_USER + "begintime", begintime);
                    //查询结束时间
                    session.setAttribute(RptStaticValue.PREFIX_RPT_USER + "endtime", endtime);
                    // 部门ID组合字符串
                    session.setAttribute(RptStaticValue.PREFIX_RPT_USER + "deptString", depids);
                    // 操作员ID组合字符串
                    session.setAttribute(RptStaticValue.PREFIX_RPT_USER + "userString", userName);
                    session.setAttribute(RptStaticValue.PREFIX_RPT_USER + "pageIndex", pageInfo.getPageIndex());
                    session.setAttribute(RptStaticValue.PREFIX_RPT_USER + "pageSize", pageInfo.getPageSize());

                    session.setAttribute(RptStaticValue.PREFIX_RPT_USER + "totalPage", pageInfo.getTotalPage());
                    session.setAttribute(RptStaticValue.PREFIX_RPT_USER + "totalRec", pageInfo.getTotalRec());

                    session.setAttribute("userrptuname", uName);
                    session.setAttribute("userrptdname", depNam);
                }
                //从其他地方进入，则通过session获取查询条件
                else {
                    // 短彩类型
                    msType = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_USER + "msType");
                    //报表类型
                    reportType = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_USER + "reportType");
                    // 部门ID组合字符串
                    depids = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_USER + "deptString");
                    // 操作员ID组合字符串
                    userName = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_USER + "userString");
                    begintime = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_USER + "begintime");
                    endtime = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_USER + "endtime");
                    //运营商
                    spnumtype = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_USER + "spnumtype");

                    Integer pageIndex = (Integer) session.getAttribute(RptStaticValue.PREFIX_RPT_USER + "pageIndex");
                    pageInfo.setPageIndex(pageIndex);
                    Integer pageSize = (Integer) session.getAttribute(RptStaticValue.PREFIX_RPT_USER + "pageSize");
                    pageInfo.setPageSize(pageSize);

                    Integer totalPage = (Integer) session.getAttribute(RptStaticValue.PREFIX_RPT_USER + "totalPage");
                    pageInfo.setTotalPage(totalPage);

                    Integer totalRec = (Integer) session.getAttribute(RptStaticValue.PREFIX_RPT_USER + "totalRec");
                    pageInfo.setTotalRec(totalRec);
                }
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
                endtime = begintime + "1231";
                begintime += "0101";
            }
            //日报表
            else {
                //数据表中为int类型，这里去掉字符，变为纯数字
                begintime = begintime.replaceAll("-", "");
                endtime = endtime.replaceAll("-", "");
            }
            // 查询条件对象
            MtDataReportVo mtDataReportVo = new MtDataReportVo();
            // 开始时间
            mtDataReportVo.setSendTime(begintime);
            mtDataReportVo.setEndTime(endtime);
            // 短信类型
            mtDataReportVo.setMstype(Integer.parseInt(msType));
            //运营商
            mtDataReportVo.setSpnumtype(Integer.parseInt(spnumtype));
            // 查询条件的操作员IDs，通过拼接方法获取逗号隔开的 操作员id字符串
            String usersIds = getSplitUserIds(userName);
            // 当前操作员所拥有的权限
            List<MtDataReportVo> mtreportList = opReportBiz.getMtDataReportList(user.getPermissionType(), depids, usersIds, mtDataReportVo, user.getUserId(), lgcorpcode, pageInfo);
            for (MtDataReportVo vo : mtreportList) {
                vo.setDepName(vo.getDepName().replaceAll("未知机构", MessageUtils.extractMessage("cxtj", "cxtj_rpt_dep_wzjg", request)));
                vo.setUserName(vo.getUserName().replaceAll("未知操作员", MessageUtils.extractMessage("cxtj", "cxtj_rpt_dep_wzczy", request)));
            }
            // 合计
            long[] sum = opReportBiz.findSumCount(user.getPermissionType(), user.getUserId(), depids, usersIds, mtDataReportVo, lgcorpcode, null);
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
//			if(sum == null || sum[0] + sum[1] + sum[2] + sum[3] + sum[4] == 0){
//				mtreportList=new ArrayList<MtDataReportVo>();
//				pageInfo=new PageInfo();
//			}

            //获取加密解密对象
            ParamsEncryptOrDecrypt cryptor = (ParamsEncryptOrDecrypt) request.getSession(false).getAttribute("decryptObj");
            if (cryptor == null) {
                EmpExecutionContext.error("操作员统计报表获取加密对象失败。");
                return;
            }
            //加密id
            cryptor.batchEncrypt(mtreportList, "UserId", "SecretId");
            request.setAttribute("resultList", mtreportList);
            request.setAttribute("pageInfo", pageInfo);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "操作员统计报表sevlet查询异常。");
        } finally {
            long count = 0l;
            //从pageinfo中获取查询总条数
            if (pageInfo != null) {
                count = pageInfo.getTotalRec();
            }
            //开始时间
            String starthms = hms.format(startl);
            // 写日志
            String opContent = "操作员统计报表查询：" + count + "条 开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms。"
                    + "depids=" + depids + ",userName=" + userName + ",mstype=" + msType + ",reportType=" + reportType
                    + ",spnumtype=" + spnumtype + ",begintime=" + begintime + ",endtime=" + endtime;

            EmpExecutionContext.info("操作员统计报表", lgcorpcode, lguserid, lgusername, opContent, StaticValue.GET);
            request.getRequestDispatcher(empRoot + base + "/rep_sysUserReport.jsp?lgcorpcode=" + lgcorpcode + "&lguserid=" + lguserid).forward(request, response);
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
        //发送类型
        session.removeAttribute(RptStaticValue.PREFIX_RPT_USER + "msType");
        //运营商
        session.removeAttribute(RptStaticValue.PREFIX_RPT_USER + "spnumtype");
        //报表类型
        session.removeAttribute(RptStaticValue.PREFIX_RPT_USER + "reportType");
        //查询开始时间
        session.removeAttribute(RptStaticValue.PREFIX_RPT_USER + "begintime");
        //查询结束时间
        session.removeAttribute(RptStaticValue.PREFIX_RPT_USER + "endtime");
        // 部门ID组合字符串
        session.removeAttribute(RptStaticValue.PREFIX_RPT_USER + "deptString");
        // 操作员ID组合字符串
        session.removeAttribute(RptStaticValue.PREFIX_RPT_USER + "userString");
        session.removeAttribute(RptStaticValue.PREFIX_RPT_USER + "pageIndex");
        session.removeAttribute(RptStaticValue.PREFIX_RPT_USER + "pageSize");

        session.removeAttribute("userrptuname");
        session.removeAttribute("userrptdname");
    }

    //查看操作员报表详情
    public void findInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //起始ms数
        long startl = System.currentTimeMillis();

        // 短彩类型
        String mstype = request.getParameter("mstype");
        // 登录操作员id
        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);

        // 企业编码
        String lgcorpcode = queryBiz.getCorpCode(request);
        //报表类型
        String reporttype = request.getParameter("reporttype");
        //ID
        String id = request.getParameter("userid");
        //开始时间
        String begintime = request.getParameter("sendtime");
        //结束时间
        String endtime = request.getParameter("endtime");
        //运营商
        String spnumtype = request.getParameter("spnumtype");

        PageInfo pageInfo = new PageInfo();
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        //获取条件查询
        try {
            pageSet(pageInfo, request);

            if (mstype == null || mstype.trim().length() == 0 || "null".equals(mstype)) {
                //默认短信
                mstype = "0";
            }
            if (reporttype == null || reporttype.trim().length() == 0 || "null".equals(reporttype)) {
                //默认日报表
                reporttype = "0";
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

            //当传入企业编码为空或空串则从session中取
            if (lgcorpcode == null || lgcorpcode.trim().length() == 0 || "null".equals(lgcorpcode) || "undefined".equals(lgcorpcode)) {
                // 获取当前登录操作员对象
                LfSysuser user = queryBiz.getCurrentUser(request);
                //如果session的user对象为空且有当前登录操作员id，则查库拿
                if (user == null && lguserid != null && lguserid.trim().length() > 0 && !"null".equals(lguserid) && !"undefined".equals(lguserid)) {
                    BaseBiz baseBiz = new BaseBiz();
                    user = baseBiz.getLfSysuserByUserId(lguserid);
                }
                if (user == null) {
                    EmpExecutionContext.error("操作员统计报表详情查询，获取不到登录操作员对象。");
                    return;
                }
                lgcorpcode = user.getCorpCode();
            }
            // 获取当前登录操作员对象
            LfSysuser user = queryBiz.getCurrentUser(request);
            if (user == null) {
                EmpExecutionContext.error("机构统计报表查询，企业编码为空，获取不到登录操作员对象。");
                return;
            }
            //查询条件对象
            MtDataReportVo mtDataReportVo = new MtDataReportVo();
            // 短信类型
            mtDataReportVo.setMstype(Integer.parseInt(mstype));
            //运营商
            mtDataReportVo.setSpnumtype(Integer.parseInt(spnumtype));

            //获取加密解密对象
            ParamsEncryptOrDecrypt cryptor = (ParamsEncryptOrDecrypt) request.getSession(false).getAttribute("decryptObj");
            if (cryptor == null) {
                EmpExecutionContext.error("操作员统计报表获取解密对象失败。");
                return;
            }
            //解密
            id = cryptor.decrypt(id);
            //界面传入操作员ID
            mtDataReportVo.setUserId(Long.valueOf(id));
            // 设置开始和结束时间
            //月报表
            if ("1".equals(reporttype)) {
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
            else if ("2".equals(reporttype)) {
                endtime = begintime + "1231";
                begintime += "0101";
            }
            //日报表
            else {
                //数据表中为int类型，这里去掉字符，变为纯数字
                begintime = begintime.replaceAll("-", "");
                endtime = endtime.replaceAll("-", "");
            }
            mtDataReportVo.setSendTime(begintime);
            mtDataReportVo.setEndTime(endtime);
            //获取是否开启p2参数配置
            String depcodethird2p2 = SystemGlobals.getValue("depcodethird2p2");
            //获取不到配置文件值
            if (depcodethird2p2 == null || depcodethird2p2.trim().length() == 0) {
                depcodethird2p2 = "false";
            }
            if ("true".equals(depcodethird2p2) && user.getPermissionType() != 1) {
                // 部门ID组合字符串
                String depids = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_USER + "deptString");
                // ownUserIds = null;
                if (null == depids || 0 == depids.length()) {
                    depids = opReportBiz.getDepIds(user.getUserId());
                    // 2012-5-14 14:45根据领导指示，操作员报表不做机构权限，
                    // 只要拥有改模块的访问权限，都可以查看当前企业下的所有操作员相关报表信息
                    // 查找当前登录人员的管辖范围机构。
                    List<LfDomination> lfdom = new SpecialDAO().findDomDepIdByUserID(user.getUserId().toString());
                    if (lfdom != null && lfdom.size() > 0) {
                        LfDomination lfd = lfdom.get(0);
                        mtDataReportVo.setDepName(lfd.getDepId().toString());
                    } else {
                        EmpExecutionContext.error("操作元报表，获取不到管辖机构。curUserId=" + user.getUserId());
                        mtDataReportVo.setDepName("0");
                    }
                } else {
                    mtDataReportVo.setDepName(depids);
                }
                //传递外层查询的机构条件
                mtDataReportVo.setUserName(depids);
            } else {

            }

            List<DynaBean> userdetaillist = opReportBiz.getMtDataReportInfoDetailList(id, mtDataReportVo, lgcorpcode, pageInfo, reporttype);
            // 合计
            long[] sum = opReportBiz.findSumDetailCount(id, mtDataReportVo, lgcorpcode, reporttype);
            //提交总数
            request.setAttribute("userdetailicount", sum[0]);
            //接收成功数
            request.setAttribute("userdetailrsucc", sum[1]);
            //发送失败数
            request.setAttribute("userdetailrfail1", sum[2]);
            //接收失败数
            request.setAttribute("userdetailrfail2", sum[3]);
            //未返数
            request.setAttribute("userdetailrnret", sum[4]);
            //分页对象
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("userdetaillist", userdetaillist);

            String username;
            String depname;
            if (id == null) {
                username = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
                depname = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
                session.setAttribute("username", username);
                session.setAttribute("depname", depname);
                return;
            }

            LfSysuser lfsysuser = new BaseBiz().getById(LfSysuser.class, id);
            if (lfsysuser == null) {
                username = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
                depname = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
                session.setAttribute("username", username);
                session.setAttribute("depname", depname);
                return;
            }

            username = (lfsysuser.getUserName() == null ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request) : lfsysuser.getUserName().toString().replace("<", "&lt;").replace(">", "&gt;"));
            if (lfsysuser.getUserName() != null && lfsysuser.getUserState() != null && lfsysuser.getUserState() == 2) {
                username = username + "<font color='red'>(" + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_jgtjbb_yzx", request) + ")</font>";
            }
            if (lfsysuser.getDepId() != null) {
                LfDep lfdep = new BaseBiz().getById(LfDep.class, lfsysuser.getDepId());
                if (lfdep != null) {
                    depname = lfdep.getDepName() == null ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request) : lfdep.getDepName().replace("<", "&lt;").replace(">", "&gt;");
                } else {
                    depname = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
                }
            } else {
                depname = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
            }
            session.setAttribute("username", username);
            session.setAttribute("depname", depname);

            long count = 0l;
            //从pageinfo中获取查询总条数
            if (pageInfo != null) {
                count = pageInfo.getTotalRec();
            }
            //开始时间
            String starthms = hms.format(startl);
            // 写日志
            String opContent = "操作员统计报表详情查询：" + count + "条 开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            queryBiz.setLog(request, "操作员统计报表详情", opContent, StaticValue.GET);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "操作员统计报表sevlet详细查询异常");
        } finally {
            request.getRequestDispatcher(empRoot + base + "/rep_sysDetailsReport.jsp").forward(request, response);
        }
    }


    /**
     * 详细操作员报表详情导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void r_suRptDataExportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //起始ms数
        long startl = System.currentTimeMillis();
        // 短彩类型
        String mstype = request.getParameter("mstype");
        // 登录操作员id
        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);

        // 企业编码
        String lgcorpcode = queryBiz.getCorpCode(request);
        //报表类型
        String reporttype = request.getParameter("reporttype");
        //ID
        String id = request.getParameter("userid");
        //开始时间
        String begintime = request.getParameter("sendtime");
        //结束时间
        String endtime = request.getParameter("endtime");
        //运营商
        String spnumtype = request.getParameter("spnumtype");
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        //获取条件查询
        try {
            if (mstype == null || mstype.trim().length() == 0 || "null".equals(mstype)) {
                //默认短信
                mstype = "0";
            }
            if (reporttype == null || reporttype.trim().length() == 0 || "null".equals(reporttype)) {
                //默认日报表
                reporttype = "0";
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

            //当传入企业编码为空或空串则从session中取
            if (lgcorpcode == null || lgcorpcode.trim().length() == 0 || "null".equals(lgcorpcode) || "undefined".equals(lgcorpcode)) {
                // 获取当前登录操作员对象
                LfSysuser user = queryBiz.getCurrentUser(request);
                //如果session的user对象为空且有当前登录操作员id，则查库拿
                if (user == null && lguserid != null && lguserid.trim().length() > 0 && !"null".equals(lguserid) && !"undefined".equals(lguserid)) {
                    BaseBiz baseBiz = new BaseBiz();
                    user = baseBiz.getLfSysuserByUserId(lguserid);
                }
                if (user == null) {
                    EmpExecutionContext.error("操作员统计报表详情导出，获取不到登录操作员对象。");
                    response.getWriter().print("false");
                    return;
                }
                lgcorpcode = user.getCorpCode();
            }
            // 获取当前登录操作员对象
            LfSysuser user = queryBiz.getCurrentUser(request);
            if (user == null) {
                EmpExecutionContext.error("机构统计报表查询，企业编码为空，获取不到登录操作员对象。");
                return;
            }
            //查询条件对象
            MtDataReportVo mtDataReportVo = new MtDataReportVo();
            // 短信类型
            mtDataReportVo.setMstype(Integer.parseInt(mstype));
            //运营商
            mtDataReportVo.setSpnumtype(Integer.parseInt(spnumtype));

            //获取加密解密对象
            ParamsEncryptOrDecrypt cryptor = (ParamsEncryptOrDecrypt) request.getSession(false).getAttribute("decryptObj");
            if (cryptor == null) {
                EmpExecutionContext.error("操作员统计报表获取解密对象失败。");
                return;
            }
            //解密
            id = cryptor.decrypt(id);

            //界面传入操作员ID
            mtDataReportVo.setUserId(Long.valueOf(id));
            // 设置开始和结束时间
            //月报表
            if ("1".equals(reporttype)) {
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
            else if ("2".equals(reporttype)) {
                endtime = begintime + "1231";
                begintime += "0101";
            }
            //日报表
            else {
                //数据表中为int类型，这里去掉字符，变为纯数字
                begintime = begintime.replaceAll("-", "");
                endtime = endtime.replaceAll("-", "");
            }
            mtDataReportVo.setSendTime(begintime);
            mtDataReportVo.setEndTime(endtime);
            //获取是否开启p2参数配置
            String depcodethird2p2 = SystemGlobals.getValue("depcodethird2p2");
            //获取不到配置文件值
            if (depcodethird2p2 == null || depcodethird2p2.trim().length() == 0) {
                depcodethird2p2 = "false";
            }
            if ("true".equals(depcodethird2p2) && user.getPermissionType() != 1) {
                // 部门ID组合字符串
                String depids = (String) session.getAttribute(RptStaticValue.PREFIX_RPT_USER + "deptString");
                // ownUserIds = null;
                if (null == depids || 0 == depids.length()) {
                    depids = opReportBiz.getDepIds(user.getUserId());
                    // 2012-5-14 14:45根据领导指示，操作员报表不做机构权限，
                    // 只要拥有改模块的访问权限，都可以查看当前企业下的所有操作员相关报表信息
                    // 查找当前登录人员的管辖范围机构。
                    List<LfDomination> lfdom = new SpecialDAO().findDomDepIdByUserID(user.getUserId().toString());
                    if (lfdom != null && lfdom.size() > 0) {
                        LfDomination lfd = lfdom.get(0);
                        mtDataReportVo.setDepName(lfd.getDepId().toString());
                    } else {
                        EmpExecutionContext.error("操作元报表，获取不到管辖机构。curUserId=" + user.getUserId());
                        mtDataReportVo.setDepName("0");
                    }
                } else {
                    mtDataReportVo.setDepName(depids);
                }
            } else {

            }
            List<DynaBean> userdetaillist = opReportBiz.getMtDataReportInfoDetailList(id, mtDataReportVo, lgcorpcode, null, reporttype);
            // 合计
            long[] sum = opReportBiz.findSumDetailCount(id, mtDataReportVo, lgcorpcode, reporttype);
            // 获取session存储的 成功数失败数和时间字符串
            CountReportVo crptvo = new CountReportVo();
            crptvo.setIcount(sum[0]);
            crptvo.setRsucc(sum[1]);
            crptvo.setRfail1(sum[2]);
            crptvo.setRfail2(sum[3]);
            crptvo.setRnret(sum[4]);

            //操作员名称
            String username = (String) session.getAttribute("username");
            //机构名称
            String depname = (String) session.getAttribute("depname");
            //运营商
            String spnumtypename;
            if (mtDataReportVo.getSpnumtype() == 0) {
                spnumtypename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gn", request);
            } else if (mtDataReportVo.getSpnumtype() == 1) {
                spnumtypename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gw", request);
            } else if (mtDataReportVo.getSpnumtype() == -1) {
                spnumtypename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qb", request);
            } else {
                spnumtypename = "--";
            }

            response.setContentType("html/text");
            if (userdetaillist == null || userdetaillist.size() == 0) {
                response.getWriter().print("false");
                return;
            }

            Map<String, String> resultMap = opReportBiz.createdetailSysUserReportExcelV1(userdetaillist, crptvo, reporttype, username, depname, spnumtypename, request);
            String fileName = (String) resultMap.get("FILE_NAME");
            //开始时间
            String starthms = hms.format(startl);
            // 写日志
            String opContent = "导出操作员详情统计报表：" + userdetaillist.size() + "条  ,文件名：" + fileName + ",开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            queryBiz.setLog(request, "操作员统计详情报表", opContent, StaticValue.GET);
            userdetaillist.clear();
            userdetaillist = null;
            session.setAttribute("userdetail_export_map", resultMap);
            response.getWriter().print("true");
        } catch (Exception e) {
            // 异常处理
            EmpExecutionContext.error(e, "操作员报表详情导出异常");
            response.getWriter().print("false");
        }
    }

    /**
     * 操作员统计报表详情下载导出文件
     *
     * @param request
     * @param response
     */
    public void detaildownloadFile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("userdetail_export_map");
        session.removeAttribute("userdetail_export_map");
        if (obj != null) {
            // 弹出下载页面。
            DownloadFile dfs = new DownloadFile();
            Map<String, String> resultMap = (Map<String, String>) obj;
            String fileName = (String) resultMap.get("FILE_NAME");
            String filePath = (String) resultMap.get("FILE_PATH");
            dfs.downFile(request, response, filePath, fileName);
        }
    }


    //查看操作员报表详情
    public void findAreaInfoById(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //起始ms数
        long startl = System.currentTimeMillis();

        // 短彩类型
        String mstype = request.getParameter("mstype");
        // 登录操作员id
        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);

        // 企业编码
        String lgcorpcode = queryBiz.getCorpCode(request);
        //报表类型
        String reporttype = request.getParameter("reporttype");
        //ID
        String id = request.getParameter("userid");
        //开始时间
        String begintime = request.getParameter("sendtime");
        //结束时间
        String endtime = request.getParameter("endtime");
        //运营商
        String spnumtype = request.getParameter("spnumtype");
        //国家/区号代码
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
            if (reporttype == null || reporttype.trim().length() == 0 || "null".equals(reporttype)) {
                //默认日报表
                reporttype = "0";
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

            //当传入企业编码为空或空串则从session中取
            if (lgcorpcode == null || lgcorpcode.trim().length() == 0 || "null".equals(lgcorpcode)) {
                // 获取当前登录操作员对象
                LfSysuser user = queryBiz.getCurrentUser(request);
                //如果session的user对象为空且有当前登录操作员id，则查库拿
                if (user == null && lguserid != null && lguserid.trim().length() > 0 && !"null".equals(lguserid) && !"undefined".equals(lguserid)) {
                    BaseBiz baseBiz = new BaseBiz();
                    user = baseBiz.getLfSysuserByUserId(lguserid);
                }
                if (user == null) {
                    EmpExecutionContext.error("操作员统计报表各国详情查询，企业编码为空，获取不到登录操作员对象。");
                    return;
                }
                lgcorpcode = user.getCorpCode();
            }

            //查询条件对象
            UserAreaRptVo userareavo = new UserAreaRptVo();
            // 短信类型
            userareavo.setMstype(Integer.parseInt(mstype));
            //运营商
            userareavo.setSpnumtype(Integer.parseInt(spnumtype));
            //国家区域代码
            userareavo.setAreacode(areacode);
            //国家名称
            userareavo.setAreaname(areaname);

            //获取加密解密对象
            ParamsEncryptOrDecrypt cryptor = (ParamsEncryptOrDecrypt) request.getSession(false).getAttribute("decryptObj");
            if (cryptor == null) {
                EmpExecutionContext.error("操作员统计报表获取解密对象失败。");
                return;
            }
            //解密
            id = cryptor.decrypt(id);
            //界面传入操作员ID
            userareavo.setUserid(Long.valueOf(id));
            // 设置开始和结束时间
            //月报表
            if ("1".equals(reporttype)) {
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
            else if ("2".equals(reporttype)) {
                endtime = begintime + "1231";
                begintime += "0101";
            }
            //日报表
            else {
                //数据表中为int类型，这里去掉字符，变为纯数字
                begintime = begintime.replaceAll("-", "");
                endtime = endtime.replaceAll("-", "");
            }
            userareavo.setSendtime(begintime);
            userareavo.setEndtime(endtime);

            List<DynaBean> userareadetaillist = opReportBiz.getAreaMtDataReportInfoDetailList(id, userareavo, lgcorpcode, pageInfo, reporttype);
            // 合计
            long[] sum = opReportBiz.findAreaSumDetailCount(id, userareavo, lgcorpcode, reporttype);
            //提交总数
            request.setAttribute("userareadetailicount", sum[0]);
            //接收成功数
            request.setAttribute("userareadetailrsucc", sum[1]);
            //发送失败数
            request.setAttribute("userareadetailrfail1", sum[2]);
            //接收失败数
            request.setAttribute("userareadetailrfail2", sum[3]);
            //未返数
            request.setAttribute("userareadetailrnret", sum[4]);

            //分页对象
            request.setAttribute("pageInfo", pageInfo);
            request.setAttribute("userareadetaillist", userareadetaillist);
            HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
            String userareaname;
            String depareaname;
            if (id == null) {
                userareaname = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
                depareaname = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
                session.setAttribute("userareaname", userareaname);
                session.setAttribute("userareadepname", depareaname);
                return;
            }

            LfSysuser lfsysuser = new BaseBiz().getById(LfSysuser.class, id);
            if (lfsysuser == null) {
                userareaname = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
                depareaname = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
                session.setAttribute("userareaname", userareaname);
                session.setAttribute("userareadepname", depareaname);
                return;
            }

            userareaname = (lfsysuser.getUserName() == null ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request) : lfsysuser.getUserName().toString().replace("<", "&lt;").replace(">", "&gt;"));
            if (lfsysuser.getUserName() != null && lfsysuser.getUserState() != null && lfsysuser.getUserState() == 2) {
                userareaname = userareaname + "<font color='red'>(" + MessageUtils.extractMessage("cxtj", "cxtj_sjcx_jgtjbb_yzx", request) + ")</font>";
            }
            if (lfsysuser.getDepId() != null) {
                LfDep lfdep = new BaseBiz().getById(LfDep.class, lfsysuser.getDepId());
                if (lfdep != null) {
                    depareaname = lfdep.getDepName() == null ? MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request) : lfdep.getDepName().replace("<", "&lt;").replace(">", "&gt;");
                } else {
                    depareaname = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
                }
            } else {
                depareaname = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_zhtjbb_wz", request);
            }
            session.setAttribute("userareaname", userareaname);
            session.setAttribute("userareadepname", depareaname);

            long count = 0l;
            //从pageinfo中获取查询总条数
            if (pageInfo != null) {
                count = pageInfo.getTotalRec();
            }
            //开始时间
            String starthms = hms.format(startl);
            // 写日志
            String opContent = "操作员统计报表国家详情查询：" + count + "条 开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            queryBiz.setLog(request, "操作员统计报表国家详情", opContent, StaticValue.GET);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "操作员统计报表sevlet各国详细查询异常");
        } finally {
            request.getRequestDispatcher(empRoot + base + "/rep_sysUserAreaDetail.jsp").forward(request, response);
        }
    }


    /**
     * 详细操作员报表国家详情导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void r_suRptDataAreaExportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //起始ms数
        long startl = System.currentTimeMillis();

        // 短彩类型
        String mstype = request.getParameter("mstype");
        // 登录操作员id
        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);

        // 企业编码
        String lgcorpcode = queryBiz.getCorpCode(request);
        //报表类型
        String reporttype = request.getParameter("reporttype");
        //ID
        String id = request.getParameter("userid");
        //开始时间
        String begintime = request.getParameter("sendtime");
        //结束时间
        String endtime = request.getParameter("endtime");
        //运营商
        String spnumtype = request.getParameter("spnumtype");
        //国家代码
        String areacode = request.getParameter("areacode");
        //国家名称
        String areaname = request.getParameter("areaname");

        //获取条件查询
        try {
            if (mstype == null || mstype.trim().length() == 0 || "null".equals(mstype)) {
                //默认短信
                mstype = "0";
            }
            if (reporttype == null || reporttype.trim().length() == 0 || "null".equals(reporttype)) {
                //默认日报表
                reporttype = "0";
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

            //当传入企业编码为空或空串则从session中取
            if (lgcorpcode == null || lgcorpcode.trim().length() == 0 || "null".equals(lgcorpcode)) {
                // 获取当前登录操作员对象
                LfSysuser user = queryBiz.getCurrentUser(request);
                //如果session的user对象为空且有当前登录操作员id，则查库拿
                if (user == null && lguserid != null && lguserid.trim().length() > 0 && !"null".equals(lguserid) && !"undefined".equals(lguserid)) {
                    BaseBiz baseBiz = new BaseBiz();
                    user = baseBiz.getLfSysuserByUserId(lguserid);
                }
                if (user == null) {
                    EmpExecutionContext.error("操作员统计报表各国详情查询导出，企业编码为空，获取不到登录操作员对象。");
                    response.getWriter().print("false");
                    return;
                }
                lgcorpcode = user.getCorpCode();
            }

            //查询条件对象
            UserAreaRptVo userareavo = new UserAreaRptVo();
            // 短信类型
            userareavo.setMstype(Integer.parseInt(mstype));
            //运营商
            userareavo.setSpnumtype(Integer.parseInt(spnumtype));
            //国家区域代码
            userareavo.setAreacode(areacode);
            //国家名称
            userareavo.setAreaname(areaname);

            //获取加密解密对象
            ParamsEncryptOrDecrypt cryptor = (ParamsEncryptOrDecrypt) request.getSession(false).getAttribute("decryptObj");
            if (cryptor == null) {
                EmpExecutionContext.error("操作员统计报表获取解密对象失败。");
                return;
            }
            //解密
            id = cryptor.decrypt(id);
            //界面传入操作员ID
            userareavo.setUserid(Long.valueOf(id));
            // 设置开始和结束时间
            //月报表
            if ("1".equals(reporttype)) {
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
            else if ("2".equals(reporttype)) {
                endtime = begintime + "1231";
                begintime += "0101";
            }
            //日报表
            else {
                //数据表中为int类型，这里去掉字符，变为纯数字
                begintime = begintime.replaceAll("-", "");
                endtime = endtime.replaceAll("-", "");
            }
            userareavo.setSendtime(begintime);
            userareavo.setEndtime(endtime);

            List<DynaBean> userareadetaillist = opReportBiz.getAreaMtDataReportInfoDetailList(id, userareavo, lgcorpcode, null, reporttype);

            // 合计
            long[] sum = opReportBiz.findAreaSumDetailCount(id, userareavo, lgcorpcode, reporttype);
            // 获取session存储的 成功数失败数和时间字符串
            CountReportVo crptvo = new CountReportVo();
            crptvo.setIcount(sum[0]);
            crptvo.setRsucc(sum[1]);
            crptvo.setRfail1(sum[2]);
            crptvo.setRfail2(sum[3]);
            crptvo.setRnret(sum[4]);
            HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
            //操作员名称
            String username = (String) session.getAttribute("userareaname");
            //机构名称
            String depname = (String) session.getAttribute("userareadepname");
            //运营商
            String spnumtypename;
            if (userareavo.getSpnumtype() == 0) {
                spnumtypename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gn", request);
            } else if (userareavo.getSpnumtype() == 1) {
                spnumtypename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gw", request);
            } else if (userareavo.getSpnumtype() == -1) {
                spnumtypename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qb", request);
            } else {
                spnumtypename = "--";
            }
            String showuserareatime = (String) session.getAttribute("showuserareatime");
            response.setContentType("html/text");
            if (userareadetaillist == null || userareadetaillist.size() == 0) {
                response.getWriter().print("false");
                return;
            }

            Map<String, String> resultMap = opReportBiz.createAreadetailSysUserReportExcelV1(userareadetaillist, crptvo, reporttype, username, depname, spnumtypename, showuserareatime, request);
            String fileName = (String) resultMap.get("FILE_NAME");
            //开始时间
            String starthms = hms.format(startl);
            // 写日志
            String opContent = "导出操作员国家详情统计报表：" + userareadetaillist.size() + "条  ,文件名：" + fileName + ",开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
            queryBiz.setLog(request, "操作员统计国家详情报表", opContent, StaticValue.GET);
            userareadetaillist.clear();
            userareadetaillist = null;
            session.setAttribute("userareadetail_export_map", resultMap);
            response.getWriter().print("true");
        } catch (Exception e) {
            // 异常处理
            EmpExecutionContext.error(e, "操作员报表国家详情导出异常。");
            response.getWriter().print("false");
        }
    }


    /**
     * 操作员统计报表国家详情下载导出文件
     *
     * @param request
     * @param response
     */
    public void areadownloadFile(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
        Object obj = session.getAttribute("userareadetail_export_map");
        session.removeAttribute("userareadetail_export_map");
        if (obj != null) {
            // 弹出下载页面。
            DownloadFile dfs = new DownloadFile();
            Map<String, String> resultMap = (Map<String, String>) obj;
            String fileName = (String) resultMap.get("FILE_NAME");
            String filePath = (String) resultMap.get("FILE_PATH");
            dfs.downFile(request, response, filePath, fileName);
        }
    }


    // 输出机构代码数据
    public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // 父级机构ID
            Long superiorDepId = null;
            // 操作员id
            Long userid = null;
            // 部门ID
            String depStr = request.getParameter("depId");
            // 操作员ID
            String userStr = request.getParameter("lguserid");
            if (depStr != null && !"".equals(depStr.trim())) {
                superiorDepId = Long.parseLong(depStr);
            }
            if (userStr != null && !"".equals(userStr.trim())) {
                userid = Long.parseLong(userStr);
            }
            // 通过机构id和操作员ID获得机构树字符串
            String departmentTree = getDepUserJosnDataNew(superiorDepId, userid);
            response.getWriter().print(departmentTree);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取机构树异常");
        }

    }

    // 获取机构代码数据
    private String getDepUserJosnDataNew(Long superiorDepId, Long userid) {
        StringBuffer tree = new StringBuffer();
        LfSysuser user = null;
        BaseBiz baseBiz = new BaseBiz();
        try {
            user = baseBiz.getById(LfSysuser.class, userid);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            EmpExecutionContext.error(e1, "获取操作员失败");
        }
        if (user != null && user.getPermissionType() == 1) {
            tree = new StringBuffer("[]");
        } else {
            DepBiz depBiz = new DepBiz();
            List<LfDep> lfDeps;

            try {
                lfDeps = null;

                if (superiorDepId == null) {
                    lfDeps = new ArrayList<LfDep>();
                    LfDep lfDep = depBiz.getAllDeps(userid).get(0);// 这里需要优化
                    lfDeps.add(lfDep);
                } else {
                    lfDeps = new DepBiz().getDepsByDepSuperId(superiorDepId);
                }
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
                tree.append("]");
            } catch (Exception e) {
                EmpExecutionContext.error(e, "获取操作员失败");
            }
        }
        return tree.toString();
    }

    /**
     * 生成机构操作员树字符串
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void createUserTree(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 设置语言
        String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
        String depId = request.getParameter("depId");
        try {
            //获取当前登录操作员对象
            LfSysuser curUser = getCurUserInSession(request);
            String deptUserTree = reportBiz.getDepUserJosn(langName, depId, curUser);
            if (deptUserTree == null) {
                response.getWriter().print("");
            } else {
                response.getWriter().print(deptUserTree);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询统计，生成机构操作员树字符串，异常。depId=" + depId);
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

            StringBuffer sbDep = new StringBuffer(); // 拼接机构ID字符串

            deptIds = deptName.split(",");

            List<String> depIds = new ArrayList<String>(); // 存储机构ID

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

            StringBuffer sb = new StringBuffer(); // 拼接操作员ID字符串
            if (userName != null && !userName.equals("")) {

                userIds = userName.split(",");

            }
            List<String> sysIds = new ArrayList<String>(); // 存储操作员ID
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
     * 操作员报表导出
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void r_suRptExportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //起始ms数
        long startl = System.currentTimeMillis();

        List<MtDataReportVo> mdlist = null;
        // 时间段字符串
        String queryTime = request.getParameter("queryTime");
        // 企业编码
        String lgcorpcode = queryBiz.getCorpCode(request);
        // 登录操作员id
        //String lguserid = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String lguserid = SysuserUtil.strLguserid(request);

        // 部门id
        String depids = request.getParameter("depNam");
        // 操作员ID字符串
        String userName = request.getParameter("userName");
        // 短彩类型
        String mstype = request.getParameter("mstype");
        //报表类型
        String reportType = request.getParameter("reportType");
        //运营商
        String spnumtype = request.getParameter("spnumtype");
        // 开始时间
        String begintime = request.getParameter("begintime");
        // 结束时间
        String endtime = request.getParameter("endtime");
        try {
            if (mstype == null || mstype.trim().length() == 0 || "null".equals(mstype)) {
                //默认短信
                mstype = "0";
            }
            if (reportType == null || reportType.trim().length() == 0 || "null".equals(reportType)) {
                //默认日报表
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
            //如果session的user对象为空且有当前登录操作员id，则查库拿
            if (user == null && lguserid != null && lguserid.trim().length() > 0 && !"null".equals(lguserid) && !"undefined".equals(lguserid)) {
                BaseBiz baseBiz = new BaseBiz();
                user = baseBiz.getLfSysuserByUserId(lguserid);
            }
            if (user == null) {
                EmpExecutionContext.error("操作员统计报表导出，获取不到登录操作员对象。lgcorpcode=" + lgcorpcode + ",lguserid=" + lguserid);
                response.getWriter().print("false");
                return;
            }
            //当传入企业编码为空或空串则从session中取
            if (lgcorpcode == null || lguserid == null || lgcorpcode.trim().length() == 0 || "null".equals(lgcorpcode) || lguserid.trim().length() == 0 || "null".equals(lguserid)) {
                lgcorpcode = user.getCorpCode();
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
                endtime = begintime + "1231";
                begintime += "0101";
            }
            //日报表
            else {
                //数据表中为int类型，这里去掉字符，变为纯数字
                begintime = begintime.replaceAll("-", "");
                endtime = endtime.replaceAll("-", "");
            }
            // 操作员统计报表查询条件对象
            MtDataReportVo mtDataReportVo = new MtDataReportVo();
            // 开始时间
            mtDataReportVo.setSendTime(begintime);
            mtDataReportVo.setEndTime(endtime);

            mtDataReportVo.setMstype(Integer.parseInt(mstype));

            mtDataReportVo.setSpnumtype(Integer.parseInt(spnumtype));

            rep_sysUserReportSvt rsurs = new rep_sysUserReportSvt();
            // 获取用逗号隔开的操作员id
            String usersIds = rsurs.getSplitUserIds(userName);
            // 当前操作员所拥有的权限
            mdlist = opReportBiz.getMtDataReportList(user.getPermissionType(), depids, usersIds, mtDataReportVo, user.getUserId(), lgcorpcode, null);
            for (MtDataReportVo vo : mdlist) {
                vo.setDepName(vo.getDepName().replaceAll("未知机构", MessageUtils.extractMessage("cxtj", "cxtj_rpt_dep_wzjg", request)));
                vo.setUserName(vo.getUserName().replaceAll("未知操作员", MessageUtils.extractMessage("cxtj", "cxtj_rpt_dep_wzczy", request)));
            }
            // 合计
            long[] sum = opReportBiz.findSumCount(user.getPermissionType(), user.getUserId(), depids, usersIds, mtDataReportVo, lgcorpcode, null);

            // 获取session对象
            HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
            // 获取session存储的 成功数失败数和时间字符串
            CountReportVo crptvo = new CountReportVo();
            crptvo.setIcount(sum[0]);
            crptvo.setRsucc(sum[1]);
            crptvo.setRfail1(sum[2]);
            crptvo.setRfail2(sum[3]);
            crptvo.setRnret(sum[4]);
            String showTime = (String) session.getAttribute("showTime");
            String spnumtypename;
            if (mtDataReportVo.getSpnumtype() == -1) {
                spnumtypename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_qb", request);
            } else if (mtDataReportVo.getSpnumtype() == 0) {
                spnumtypename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gn", request);
            } else if (mtDataReportVo.getSpnumtype() == 1) {
                spnumtypename = MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gw", request);
            } else {
                spnumtypename = "--";
            }
            response.setContentType("html/text");
            if (mdlist == null || mdlist.size() == 0) {
                response.getWriter().print("false");
                return;
            }

            Map<String, String> resultMap = opReportBiz.createSysUserReportExcelV1(mdlist, queryTime, crptvo, showTime, spnumtypename, request);
            String fileName = (String) resultMap.get("FILE_NAME");
            //开始时间
            String starthms = hms.format(startl);
            // 写日志
            String opContent = "导出操作员统计报表：" + mdlist.size() + "条  ,文件名：" + fileName + ",开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms。"
                    + "depids=" + depids + ",userName=" + userName + ",mstype=" + mstype + ",reportType=" + reportType
                    + ",spnumtype=" + spnumtype + ",begintime=" + begintime + ",endtime=" + endtime;

            queryBiz.setLog(request, "操作员统计报表", opContent, StaticValue.GET);
            mdlist.clear();
            mdlist = null;
            session.setAttribute("user_export_map", resultMap);
            response.getWriter().print("true");
        } catch (Exception e) {
            // 异常处理
            EmpExecutionContext.error(e, "操作员报表导出异常");
            response.getWriter().print("false");
        }
    }


    /**
     * 操作员统计报表下载导出文件
     *
     * @param request
     * @param response
     */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(RptStaticValue.GET_SESSION_FALSE);
            Object obj = session.getAttribute("user_export_map");
            session.removeAttribute("user_export_map");
            if (obj != null) {
                // 弹出下载页面。
                DownloadFile dfs = new DownloadFile();
                Map<String, String> resultMap = (Map<String, String>) obj;
                String fileName = (String) resultMap.get("FILE_NAME");
                String filePath = (String) resultMap.get("FILE_PATH");
                dfs.downFile(request, response, filePath, fileName);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "操作员统计报表，下载，异常。");
        }
    }


}
