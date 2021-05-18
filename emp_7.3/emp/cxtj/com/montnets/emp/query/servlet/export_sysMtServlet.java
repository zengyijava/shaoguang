package com.montnets.emp.query.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.query.biz.ReportMtBiz;
import com.montnets.emp.query.thread.MtReportThread;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.StringUtils;

public class export_sysMtServlet extends BaseServlet {

	private final ReportMtBiz iReportMtBiz = new ReportMtBiz();

	private static final String MT_REPORT_LIMIT = "1000000";

	/**
	 * 根据条件查询导出记录数量，用于校验
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void queryEptList (HttpServletRequest request, HttpServletResponse response) {
		// 账户
		String userid = "";
		// 当前登录操作员
		String lgusername = "";
		// 企业编码
		String lgcorpcode = "";
		// 获取用户信息
		LfSysuser curUser = null;

		PrintWriter print = null;
		try {
			print = response.getWriter();

			curUser = getLoginUser(request);
			// 记录类型
			String recordType = request.getParameter("recordType");

			lgusername = request.getParameter("lgusername");

			userid = request.getParameter("userid");
			// 通道号
			String spgate = request.getParameter("spgate");
			// 开始时间
			String sendtime = request.getParameter("sendtime");
			// 结束时间
			String recvtime = request.getParameter("recvtime");
			// 电话号码
			String phone = request.getParameter("phone");
			// 业务类型
			String buscode = request.getParameter("busCode");
			// 运营应商
			String spisuncm = request.getParameter("spisuncm");
			// 任务批次
			String taskid = request.getParameter("taskid");
			// 错误码
			String mterrorcode = request.getParameter("mterrorcode");
			// 自定义流水号
			String usermsgid = request.getParameter("usermsgid");
			// 文件标题
			String fileName = request.getParameter("sfileName");

			if (curUser == null) {
				EmpExecutionContext.error("下行记录查询,session中获取当前登录对象出现异常");
				print.print("{\"rtCode\":-1}");
			}
			if (curUser != null) {
				lgcorpcode = curUser.getCorpCode();
			}
			// 判断企业编码获取
			if (lgcorpcode == null || "".equals(lgcorpcode)) {
				EmpExecutionContext.error("下行记录查询,session中获取企业编码出现异常");
				print.print("{\"rtCode\":-1}");
			}
			// 判断SP账号是否是属于本企业的
			if (userid != null && !"".equals(userid.trim())
					&& !"100000".equals(lgcorpcode)) {
				// 多企业才处理
				if (StaticValue.getCORPTYPE() == 1) {
					boolean checkFlag = new CheckUtil().checkSysuserInCorp(curUser,
							lgcorpcode, userid, null);
					if (!checkFlag) {
						EmpExecutionContext.error("下行记录查询,检查操作员，企业编码，发送账号不通过，spuserid："
								+ userid + ",corpcode=" + lgcorpcode);
						print.print("{\"rtCode\":-1}");
					}
				}
			}

			// 查询条件的参数
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 企业编码
			conditionMap.put("lgcorpcode", lgcorpcode == null ? "" : lgcorpcode.trim());
			// 查询类型 实时 或 历史
			if (recordType == null || recordType.length() < 1) {
				// 实时
				recordType = "realTime";
			}

			conditionMap.put("recordType", recordType);
			conditionMap.put("spisuncm", spisuncm);
			conditionMap.put("taskid", taskid);
			conditionMap.put("userid", userid);
			conditionMap.put("spgate", spgate);
			conditionMap.put("phone", phone);
			conditionMap.put("buscode", buscode);
			conditionMap.put("sendtime", sendtime);
			conditionMap.put("recvtime", recvtime);
			conditionMap.put("mterrorcode", mterrorcode);
			conditionMap.put("usermsgid", usermsgid);
			conditionMap.put("fileName", fileName);

			if (recordType.equals("history")) {
				iReportMtBiz.setHisCondition(conditionMap);
			}

			// 设置有权限看的操作员编码。不需要考虑权限则是空字符串，目前只有admin和管辖范围是顶级机构不需要考虑权限
			String usercode = iReportMtBiz.getPermissionUserCode(curUser);
			if (curUser == null) {
				curUser = new LfSysuser();
			}
			if (usercode == null) {
				// 当前操作员编码
				conditionMap.put("curUsercode", "'" + curUser.getUserCode() + "'");
				// 当前操作员id
				conditionMap.put("curUserId", curUser.getUserId().toString());
			}
			else {
				// 当前操作员编码
				conditionMap.put("curUsercode", "'" + curUser.getUserCode() + "'");
				// 当前操作员id
				conditionMap.put("curUserId", curUser.getUserId().toString());
				// 有权限看的操作员编码
				conditionMap.put("domUsercode", usercode);
			}
			String spuserpri = iReportMtBiz.getPermissionSpuserMtpri(curUser);
			if (spuserpri == null) {
				// 当前操作员编码
				conditionMap.put("spcurcorpcode", "'" + curUser.getCorpCode() + "'");
				// 当前操作员id
				conditionMap.put("spcurUserId", curUser.getUserId().toString());
			}
			else {
				// 有权限看的操作员编码
				conditionMap.put("spuserpri", spuserpri);
			}
			conditionMap.put("realTableName", "gw_mt_task_bak");

			// 从配置文件读取导出记录的限制数量
			//String limit = SystemGlobals.getValue("montnets.cxtj.mt.report.limit");
			//从缓存中读取导出记录的限制数量 modify by tanglili 20210201
			String limit=SystemGlobals.getSysParam("cxtjMtExportLimit");
			int maxSize = Integer.parseInt(StringUtils.isEmpty(limit) ? MT_REPORT_LIMIT
					: limit);

			// 查询需要导出的下行记录
			int tatolCount = iReportMtBiz.countMtTasks(conditionMap);

			if (tatolCount == 0) {// 导出记录为空
				print.print("{\"rtCode\":1,\"sendtime\":\""
						+ conditionMap.get("sendtime") + "\",\"recvtime\":\""
						+ conditionMap.get("recvtime") + "\"}");
			}
			else if (tatolCount > maxSize) {// 导出记录超出最大限制
				print.print("{\"rtCode\":2,\"sendtime\":\""
						+ conditionMap.get("sendtime") + "\",\"recvtime\":\""
						+ conditionMap.get("recvtime") + "\"}");
			}
			else {
				print.print("{\"rtCode\":0}");// 可正常导出
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "校验导出记录时查询记录失败，异常。");
			print.print("{\"rtCode\":-1}");
		}
		finally {
			print.close();
		}
	}
	/**
	 * 导出下行列表
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void export (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 账户
		String userid = "";
		// 当前登录操作员
		String lgusername = "";
		// 企业编码
		String lgcorpcode = "";
		// 获取用户信息
		LfSysuser curUser = null;
		try {
			curUser = getLoginUser(request);
			// 记录类型
			String recordType = request.getParameter("recordType");

			lgusername = request.getParameter("lgusername");

			userid = request.getParameter("userid");
			// 通道号
			String spgate = request.getParameter("spgate");
			// 开始时间
			String sendtime = request.getParameter("sendtime");
			// 结束时间
			String recvtime = request.getParameter("recvtime");
			// 电话号码
			String phone = request.getParameter("phone");
			// 业务类型
			String buscode = request.getParameter("busCode");
			// 运营应商
			String spisuncm = request.getParameter("spisuncm");
			// 任务批次
			String taskid = request.getParameter("taskid");
			// 错误码
			String mterrorcode = request.getParameter("mterrorcode");
			// 自定义流水号
			String usermsgid = request.getParameter("usermsgid");
			// 文件标题
			String fileName = request.getParameter("sfileName");

			if (curUser == null) {
				EmpExecutionContext.error("下行记录查询,session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode = curUser.getCorpCode();
			// 判断企业编码获取
			if (lgcorpcode == null || "".equals(lgcorpcode)) {
				EmpExecutionContext.error("下行记录查询,session中获取企业编码出现异常");
				return;
			}
			// 判断SP账号是否是属于本企业的
			if (userid != null && !"".equals(userid.trim())
					&& !"100000".equals(lgcorpcode)) {
				// 多企业才处理
				if (StaticValue.getCORPTYPE() == 1) {
					boolean checkFlag = new CheckUtil().checkSysuserInCorp(curUser,
							lgcorpcode, userid, null);
					if (!checkFlag) {
						EmpExecutionContext.error("下行记录查询,检查操作员，企业编码，发送账号不通过，spuserid："
								+ userid + ",corpcode=" + lgcorpcode);
						return;
					}
				}
			}
			// 查询条件的参数
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 企业编码
			conditionMap.put("lgcorpcode", lgcorpcode.trim());
			// 查询类型 实时 或 历史
			if (recordType == null || recordType.length() < 1) {
				// 实时
				recordType = "realTime";
			}
			conditionMap.put("recordType", recordType);
			conditionMap.put("spisuncm", spisuncm);
			conditionMap.put("taskid", taskid);
			conditionMap.put("userid", userid);
			conditionMap.put("spgate", spgate);
			conditionMap.put("phone", phone);
			conditionMap.put("buscode", buscode);
			conditionMap.put("sendtime", sendtime);
			conditionMap.put("recvtime", recvtime);
			conditionMap.put("mterrorcode", mterrorcode);
			conditionMap.put("usermsgid", usermsgid);
			conditionMap.put("fileName", fileName);
			// 设置有权限看的操作员编码。不需要考虑权限则是空字符串，目前只有admin和管辖范围是顶级机构不需要考虑权限
			String usercode = iReportMtBiz.getPermissionUserCode(curUser);
			if (usercode == null) {
				// 当前操作员编码
				conditionMap.put("curUsercode", "'" + curUser.getUserCode() + "'");
				// 当前操作员id
				conditionMap.put("curUserId", curUser.getUserId().toString());
			}
			else {
				// 当前操作员编码
				conditionMap.put("curUsercode", "'" + curUser.getUserCode() + "'");
				// 当前操作员id
				conditionMap.put("curUserId", curUser.getUserId().toString());
				// 有权限看的操作员编码
				conditionMap.put("domUsercode", usercode);
			}
			String spuserpri = iReportMtBiz.getPermissionSpuserMtpri(curUser);
			if (spuserpri == null) {
				// 当前操作员编码
				conditionMap.put("spcurcorpcode", "'" + curUser.getCorpCode() + "'");
				// 当前操作员id
				conditionMap.put("spcurUserId", curUser.getUserId().toString());
			}
			else {
				// 有权限看的操作员编码
				conditionMap.put("spuserpri", spuserpri);
			}
			conditionMap.put("realTableName", "gw_mt_task_bak");

			if (!StringUtils.isNotEmpty(fileName)) {
				EmpExecutionContext.error("文件名为空。");
				request.setAttribute("exportErr", "-1");
				return;
			}
			// 下行下载的记录表添加记录,返回文件id
			Long fileid = iReportMtBiz.addRptRecord(fileName, curUser.getUserId()
					.toString());
			if (fileid == null) {
				request.setAttribute("exportErr", "-1");
			}
			else {
				// 封装异步请求参数
				Map<String, Object> paramterMap = getRptParamMap(request, curUser);
				// 文件id传过去
				paramterMap.put("fileid", fileid);
				// 调用线程异步查询下行历史记录并且生成CSV报表
				new Thread(new MtReportThread(conditionMap, paramterMap)).start();
			}

		} catch (Exception e) {
			// 异常处理
			EmpExecutionContext.error(e, "系统下行导出报表，异常。");
			request.setAttribute("exportErr", "-1");
		}
		finally {
			request.getRequestDispatcher("/que_mtRptRecord.htm?method=findPageList")
					.forward(request, response);
			EmpExecutionContext.info("查询统计", lgcorpcode, userid, lgusername,
					"开始异步导出下行记录", StaticValue.GET);
		}
	}

	/**
	 * 封装异步请求参数
	 * 
	 * @param request
	 * @param curUser
	 * @return
	 */
	private Map<String, Object> getRptParamMap (HttpServletRequest request,
			LfSysuser curUser) {
		// 国际化参数
		Map<String, Object> paramterMap = new HashMap<String, Object>();
		paramterMap.put("cxtj_sjcx_xtxxdc_cg",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxdc_cg", request));
		paramterMap.put("cxtj_sjcx_dxzljl_yd",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_yd", request));
		paramterMap.put("cxtj_sjcx_dxzljl_lt",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_lt", request));
		paramterMap.put("cxtj_sjcx_dxzljl_dx",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_dxzljl_dx", request));
		paramterMap.put("cxtj_sjcx_report_gw",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_report_gw", request));
		paramterMap.put("cxtj_sjcx_xtxxdc_spzh",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxdc_spzh", request));
		paramterMap.put("cxtj_sjcx_xtxxdc_tdhm",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxdc_tdhm", request));
		paramterMap.put("cxtj_sjcx_xtxxdc_yys",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxdc_yys", request));
		paramterMap.put("cxtj_sjcx_xtxxdc_sjhm",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxdc_sjhm", request));
		paramterMap.put("cxtj_sjcx_xtxxdc_rwpc",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxdc_rwpc", request));
		paramterMap.put("cxtj_sjcx_xtxxdc_ztbg",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxdc_ztbg", request));
		paramterMap.put("cxtj_sjcx_xtxxdc_fssj",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxdc_fssj", request));
		paramterMap.put("cxtj_sjcx_xtxxdc_jssj",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxdc_jssj", request));
		paramterMap.put("cxtj_sjcx_xtxxdc_ywlx",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxdc_ywlx", request));
		paramterMap.put("cxtj_sjcx_xtxxdc_ft",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxdc_ft", request));
		paramterMap.put("cxtj_sjcx_xtxxdc_dxnr",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxdc_dxnr", request));
		paramterMap.put("cxtj_sjcx_xtxxdc_zdylsh",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxdc_zdylsh", request));
		paramterMap.put("cxtj_sjcx_xtxxdc_bm",
				MessageUtils.extractMessage("cxtj", "cxtj_sjcx_xtxxdc_bm", request));

		paramterMap.put("user", curUser);
		// 用户选择的表头
		paramterMap.put("columns", request.getParameterValues("columns"));
		// 查询当前用户权限
		Map<String, String> btnMap = (Map<String, String>) request.getSession()
				.getAttribute("btnMap");
		if (btnMap == null) {
			btnMap = new HashMap<String, String>();
		}
		paramterMap.put("btnMap", btnMap);
		return paramterMap;
	}

}
