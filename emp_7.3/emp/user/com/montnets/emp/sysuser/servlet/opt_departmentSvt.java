package com.montnets.emp.sysuser.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.system.LfdepPassUser;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDepUserBalance;
import com.montnets.emp.entity.sysuser.LfDeppwdReceiver;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.sysuser.bean.OptUpload;
import com.montnets.emp.sysuser.biz.DepExport;
import com.montnets.emp.sysuser.biz.DepPriBiz;
import com.montnets.emp.sysuser.biz.DepUploadXls;
import com.montnets.emp.sysuser.dao.DepExportDao;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 组织机构管理
 * 
 * @project emp
 * @author Sam Wang
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-08-02
 * @description
 */
@SuppressWarnings("serial")
public class opt_departmentSvt extends BaseServlet {

	private final DepBiz depBiz = new DepBiz();
	private final DepPriBiz depPriBiz = new DepPriBiz();
	private final DepExportDao depExportDao = new DepExportDao();
	private final DepUploadXls depUp = new DepUploadXls();

	private final String excelPath = "user/employee/file/";
	private final String depexcelPath = "user/operator/file/";

	// 操作模块
	final String opModule = StaticValue.USERS_MANAGER;
	// 操作用户
	final String opSper = StaticValue.OPSPER;
	private static final String PATH = "/user/operator";
	final BaseBiz baseBiz = new BaseBiz();
	final SuperOpLog spLog = new SuperOpLog();

	/**
	 * 解密处理
	 * 
	 * @param request
	 * @param udgId
	 * @return
	 */
	public String getDecryptValue (HttpServletRequest request, String udgId) {
		// -----增加解密处理----
		// 加密对象
		ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
		String uid = "";
		// 加密对象不为空
		if (encryptOrDecrypt != null) {
			// 解密
			uid = encryptOrDecrypt.decrypt(udgId);
			if (uid == null) {
				EmpExecutionContext.error("员工通讯录参数解密码失败，keyId:" + uid);
				return "";
			}
		}
		else {
			EmpExecutionContext.error("员工通讯录从session中获取加密对象为空！");
			return "";
		}
		return uid;
	}

	public void find (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LfSysuser sysuser = null;
		Long guid = null;
		try {
			// 请求日志
			EmpExecutionContext.logRequestUrl(request, "后台请求");
			// 获取登录sysuser
			sysuser = depPriBiz.getCurrenUser(request);
			if (sysuser == null) {
				EmpExecutionContext.error("机构管理,find方法session中获取当前登录对象出现异常");
				return;
			}
			guid = sysuser.getGuId();
			// 判断企业编码获取
			if (guid == null) {
				EmpExecutionContext.error("机构管理,find方法session中获取企业编码出现异常");
				return;
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e, "操作员机构管理跳转失败！guid为：" + guid + ",sysuser为："
					+ sysuser);
		}
		finally {
			// 判断该用户是否有操作员机构权限
			if (sysuser != null && sysuser.getPermissionType() == 2) {
				PageInfo pageInfo = new PageInfo();
				pageSet(pageInfo, request);
				request.setAttribute("pageInfo", pageInfo);
				request.setAttribute("lguserid", String.valueOf(sysuser.getUserId()));
				request.getRequestDispatcher(PATH + "/opt_department.jsp").forward(
						request, response);
			}
			else {
				request.getRequestDispatcher("/common/no_dep_permission.jsp").forward(
						request, response);
			}
		}
	}

	/**
	 * 获取操作员机构管理列表
	 * 
	 * @Title: getTable
	 * @Description: TODO
	 * @param request
	 * @param response
	 * @return void
	 */
	public void getTable (HttpServletRequest request, HttpServletResponse response) {
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		Long lguserid = 0l;
		String lgcorpcode = "";
		// 日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		try {
			String depId = request.getParameter("depId");
			// 是否第一次打开
			pageSet(pageInfo, request);

			// 获取登录sysuser
			LfSysuser sysuser = depPriBiz.getCurrenUser(request);
			if (sysuser == null) {
				EmpExecutionContext.error("机构管理,gettable方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode = sysuser.getCorpCode();
			// 判断企业编码获取
			if (lgcorpcode == null || "".equals(lgcorpcode.trim())) {
				EmpExecutionContext.error("机构管理,getTable方法session中获取企业编码出现异常");
				return;
			}

			lguserid = sysuser.getUserId();
			// 判断企业编码获取
			if (lguserid == null) {
				EmpExecutionContext.error("机构管理,getTable方法session中获取操作员ID出现异常");
				return;
			}

			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("depId", StaticValue.ASC);
			// 当删除机构时，depId传入的为[object Object]
			if (depId == null || "".equals(depId) || "[object Object]".equals(depId)) {
				LfDep dep = depBiz.getAllDepByUserIdAndCorpCode(lguserid, lgcorpcode)
						.get(0);
				depId = dep.getDepId().toString();
			}
			else {
				depId = getDecryptValue(request, depId);
			}
			conditionMap.put("corpCode", lgcorpcode);
			conditionMap.put("depId", depId);
			// 只是显示正常机构
			conditionMap.put("depState", "1");
			// 判断机构id 是否在本企业
			List<LfDep> depListtest = baseBiz.getByCondition(LfDep.class, conditionMap,
					null);
			if (depListtest == null || depListtest.size() == 0) {
				EmpExecutionContext.error("机构管理,getTable方法传入的机构id 不在本机构内");
				return;
			}
			conditionMap.remove("depId");
			// List<LfDep> depList = baseBiz.getByCondition(LfDep.class, null,
			// conditionMap, orderbyMap, pageInfo);
			List<LfDep> depList = depBiz.getDepListByDepIdCon(Long.valueOf(depId),
					conditionMap, orderbyMap, pageInfo);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("depList", depList);
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			HashMap<String, String> encryptmap = new HashMap<String, String>();
			// 未知机构客户设置标识
			if (depList != null && depList.size() > 0) {
				// 加密对象不为空
				if (encryptOrDecrypt != null) {
					// 加密处理
					String dep_id = "";
					for (LfDep depobj : depList) {
						dep_id = encryptOrDecrypt.encrypt(String.valueOf(depobj
								.getDepId()));
						// 通过实际值，加密值
						encryptmap.put(depobj.getDepId() + "", dep_id);
					}
				}
				else {
					EmpExecutionContext.error("查询机构列表，从session中获取加密对象为空！");
				}
			}
			request.setAttribute("encryptmap", encryptmap);

			// 增加查询日志
			if (pageInfo != null) {
				long end_time = System.currentTimeMillis();
				String opContent = "查询开始时间：" + format.format(begin_time) + ",耗时:"
						+ (end_time - begin_time) + "ms，数量：" + pageInfo.getTotalRec();
				opSucLog(request, "机构管理", opContent, "GET");
			}
			request.getRequestDispatcher(PATH + "/opt_depTable.jsp").forward(request,
					response);
		} catch (Exception e) {

			EmpExecutionContext.error(e, "获取操作员机构管理列表失败！");
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("conditionMap", conditionMap);
			try {
				request.getRequestDispatcher(PATH + "/opt_depTable.jsp").forward(request,
						response);
			} catch (ServletException e1) {
				// 原来写的不对
				EmpExecutionContext.error(e1, "跳转页面异常！");
			} catch (IOException e1) {

				EmpExecutionContext.error(e1, "执行获取操作员机构管理列表IO异常！");
			}
		}
	}

	/**
	 * 获取机构的密码接收人列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getPassUser (HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo, request);
			String depId = request.getParameter("depId");
			/*
			 * List<LfSysuser> passUserList = null; passUserList=
			 * depBiz.getSysuserbyDepId(depId,pageInfo);
			 */
			List<LfdepPassUser> depPassUserVos = null;
			depPassUserVos = depBiz.getSysuserbyDepId(depId, pageInfo);
			request.setAttribute("depId", depId);
			LfDep lfdep = null;
			String depName = "";
			try {
				lfdep = baseBiz.getById(LfDep.class, depId);
				depName = lfdep.getDepName();
			} catch (Exception e) {
				depName = "未知机构";
				EmpExecutionContext.error(e, "获取未知机构失败！");
			}
			request.setAttribute("depName", depName);
			request.setAttribute("pageInfo", pageInfo);
			// request.setAttribute("passUserList", passUserList);
			request.setAttribute("passUserList", depPassUserVos);
			request.getRequestDispatcher(PATH + "/opt_depPassUser.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取机构的密码接收人列表失败！");
		}
	}

	/**
	 * 处理新增密码接收人
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void addPassUser (HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String oppType = null , opContent = null;
		String opUser = getOpUser(request);
		try {
			String str = "fail";
			String depId = request.getParameter("depId");
			String addUserId = request.getParameter("addUserId");
			LfSysuser sysuser = null;
			LfDep lfdep = null;
			try {
				sysuser = baseBiz.getById(LfSysuser.class, addUserId);
				lfdep = baseBiz.getById(LfDep.class, depId);
				opContent = "机构[" + lfdep.getDepName() + "]下新建密码接收人（姓名："
						+ sysuser.getName() + "）";
			} catch (Exception e) {
				EmpExecutionContext.error(e, "新建密码接收人失败！");
			}
			oppType = StaticValue.ADD;

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("depid", depId);
			conditionMap.put("userid", addUserId);
			List<LfDeppwdReceiver> receiverList = baseBiz.getByCondition(
					LfDeppwdReceiver.class, conditionMap, null);
			if (receiverList != null && receiverList.size() > 0) {
				str = "repeat";
				response.getWriter().print(str);
				return;
			}
			LfDeppwdReceiver addReceiver = new LfDeppwdReceiver();
			addReceiver.setDepid(Long.valueOf(depId));
			addReceiver.setCreatetime(new Timestamp(System.currentTimeMillis()));
			addReceiver.setUserid(Long.valueOf(addUserId));
			if (baseBiz.addObj(addReceiver)) {
				str = "success";
				EmpExecutionContext.info("企业："
						+ (sysuser == null ? "" : sysuser.getCorpCode()) + ",操作员："
						+ opUser + "," + opContent + "成功");
				spLog.logSuccessString(opUser, opModule, oppType, opContent,
						(sysuser == null ? "" : sysuser.getCorpCode()));
			}
			else {
				EmpExecutionContext.error("企业："
						+ (sysuser == null ? "" : sysuser.getCorpCode()) + ",操作员："
						+ opUser + "," + opContent + "失败");
				spLog.logFailureString(opUser, opModule, oppType, opContent + opSper,
						null, (sysuser == null ? "" : sysuser.getCorpCode()));
			}
			response.getWriter().print(str);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "处理新增密码接收人失败！");
		}
	}

	/**
	 * 删除密码接收者
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void delPassUser (HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String oppType = null , opContent = null;
		String opUser = getOpUser(request);
		try {
			String str = "fail";
			String depId = request.getParameter("depId");
			String items = request.getParameter("items");
			opContent = "删除密码接收人（批量删除）";
			oppType = StaticValue.DELETE;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("depid", depId);
			conditionMap.put("userid&in", items);
			Integer delCount = baseBiz.deleteByCondition(LfDeppwdReceiver.class,
					conditionMap);
			if (delCount != null && delCount > 0) {
				str = "success";
				EmpExecutionContext.info("企业：100001,操作员：" + opUser + "," + opContent
						+ "成功");
				spLog.logSuccessString(opUser, opModule, oppType, opContent, "100001");
			}
			else {
				EmpExecutionContext.error("企业：100001,操作员：" + opUser + "," + opContent
						+ "失败");
				spLog.logFailureString(opUser, opModule, oppType, opContent + opSper,
						null, "100001");
			}
			response.getWriter().print(str);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "删除密码接收者失败！");
		}
	}

	/**
	 * 操作员机构的 新增和修改
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void add (HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String lgcorpcode = "";
		String oppType = null , opContent = null;
		String opUser = "";
		try {
			// 获取登录sysuser
			LfSysuser sysuser = depPriBiz.getCurrenUser(request);
			if (sysuser == null) {
				EmpExecutionContext.error("机构管理,ADD方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode = sysuser.getCorpCode();
			// 判断企业编码获取
			if (lgcorpcode == null || "".equals(lgcorpcode.trim())) {
				EmpExecutionContext.error("机构管理,ADD方法session中获取企业编码出现异常");
				return;
			}
			opUser = sysuser.getUserName();

			SuperOpLog spLog = new SuperOpLog();
			// 机构ID(如果是新增则此Id为新机构的父机构Id，若为修改则此Id为当前正在修改的机构的Id)
			String id = request.getParameter("id");
			// 机构联系人
			String depContact = request.getParameter("depContact");
			// 机构描述
			String depResp = request.getParameter("depResp");
			// 新机构名称
			String depName = request.getParameter("depName");
			// 第三方机构编码
			String depCodeThird = request.getParameter("depCodeThird").toUpperCase();
			// String oldcode = request.getParameter("oldcode"); //不解
			// 旧的机构名称
			String oldDepName = request.getParameter("oldDepName");
			// 判断是新增还是修改 1是新增 2 是修改
			String type = request.getParameter("type");
			// 父级机构ID(若为新增则superiorId等于id，若为修改则superiorId等于当前正在修改的机构的父机构Id)
			String superiorIdstr = request.getParameter("superiorId");
			// 解密
			if (superiorIdstr != null && !"0".equals(superiorIdstr)) {
				superiorIdstr = getDecryptValue(request, superiorIdstr);
			}
			Long superiorId = Long.valueOf(superiorIdstr);
			boolean result = false;
			Integer retrunMsg = 0; // 默认
			LfDep dep = new LfDep();
			/*---strat------增加处理删除操作数据不同步导致的数据缺失报错问题--*/
			LfDep lfDep = null;
			if (id != null && !"".equals(id.trim())) {
				id = getDecryptValue(request, id.trim());
				lfDep = baseBiz.getById(LfDep.class, id);
			}
			else {
				retrunMsg = 5;
				response.getWriter().print(retrunMsg);
				opContent = "操作员机构 新增或修改失败！机构id为空！depId：" + id;
				EmpExecutionContext.error("模块名称：机构管理，企业：" + lgcorpcode + ",操作员：" + opUser
						+ "," + opContent);
				return;
			}
			if (lfDep == null || lfDep.getDepState() == 2) {
				retrunMsg = 4;
				response.getWriter().print(retrunMsg);
				opContent = "该机构不存在，可能已被删除！depId：" + id;
				EmpExecutionContext.error("模块名称：机构管理，企业：" + lgcorpcode + ",操作员：" + opUser
						+ "," + opContent);
				return;
			}

			// 如果机构对象中的企业编码和当前登录的企业编码不一致则回退
			if (!lgcorpcode.equals(lfDep.getCorpCode())) {
				retrunMsg = 4;
				response.getWriter().print(retrunMsg);
				opContent = "该机构不在本企业内，可能已被删除！depId：" + id;
				EmpExecutionContext.error("模块名称：机构管理，企业：" + lgcorpcode + ",操作员：" + opUser
						+ "," + opContent);
				return;
			}

			/*---------end---------*/
			if ("1".equals(type)) {
				oppType = StaticValue.ADD;
				opContent = "新增机构（机构名：" + depName + ")";
				// 自定义机构编码是否重复
				LinkedHashMap<String, String> codeCondition = new LinkedHashMap<String, String>();
				codeCondition.put("corpCode", lgcorpcode);
				codeCondition.put("depCodeThird", depCodeThird);
				codeCondition.put("depState", "1");
				List<LfDep> depCodes = baseBiz.getByCondition(LfDep.class, codeCondition,
						null);
				// 如果重复则返回
				if (depCodes != null && depCodes.size() > 0) {
					response.getWriter().print("codethirdRepeat");
					return;
				}
				String resultData = depPriBiz.checkDepsCount(lgcorpcode, id);
				if (!"true".equals(resultData)) {
					response.getWriter().print(resultData);
					return;
				}
			}
			else if ("2".equals(type)) {
				if (id == null || "".equals(id.trim())) {
					response.getWriter().print("2");// 操作失败
					return;
				}
				oppType = StaticValue.UPDATE;
				opContent = "修改机构（机构名：" + depName + ")";
				dep = baseBiz.getById(LfDep.class, id);
				if (!oldDepName.equals(depName)) {
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("corpCode", lgcorpcode);
					conditionMap.put("superiorId", String.valueOf(dep.getSuperiorId()));
					conditionMap.put("depName", depName);
					conditionMap.put("depState", "1");
					List<LfDep> deps = baseBiz.getByCondition(LfDep.class, conditionMap,
							null);
					if (deps != null && deps.size() > 0) {
						retrunMsg = 3;
					}
				}
			}
			dep.setDepName(depName.trim());
			dep.setDepCodeThird(depCodeThird.trim());
			dep.setDepContact(depContact);
			dep.setDepResp(depResp);
			// 新增
			if ("1".equals(type)) {
				dep.setCorpCode(lgcorpcode);
				dep.setSuperiorId(superiorId);
				LfDep parentDep = baseBiz.getById(LfDep.class, superiorId);
				dep.setDeppath(parentDep.getDeppath());
				if (parentDep.getDepLevel() != null) {
					dep.setDepLevel(parentDep.getDepLevel() + 1);
				}
				// 默认为显
				dep.setDepState(1);
				result = depPriBiz.addDep(dep);
			}
			else if ("2".equals(type) && retrunMsg != 3) {
				// 修改
				result = baseBiz.updateObj(dep);
				// 如果是顶级机构，则更新头部公司名称显示
				if (result) {
					if (dep.getSuperiorId() - 0 == 0) {
						request.getSession(false).setAttribute("depName",
								dep.getDepName());
					}
				}

			}

			if (retrunMsg != 3) {
				if (result) {
					// 修改成功
					retrunMsg = 1;
					if (oppType == StaticValue.ADD) {
						opSucLog(request, opModule, "新增操作员机构（机构名称：" + depName + "，机构编码："
								+ depCodeThird + "）成功。", "ADD");
					}
					else {
						opSucLog(request, opModule, "修改操作员机构（机构名称：" + oldDepName + "-->"
								+ depName + "）成功。", "UPDATE");
					}
					spLog.logSuccessString(opUser, opModule, oppType, opContent,
							lgcorpcode);
				}
				else {
					// 修改失败
					EmpExecutionContext.error("企业：" + lgcorpcode + ",操作员：" + opUser + ","
							+ opContent + "失败");
					retrunMsg = 2;
				}
			}
			response.getWriter().print(retrunMsg);
			/*
			 * Long superiorId
			 * =Long.valueOf(request.getParameter("superiorId")); LfDep dep =
			 * new LfDep(); if (id != null && !"".equals(id)) { opContent =
			 * "修改机构（机构名�?"+depName+")"; dep = baseBiz.getById(LfDep.class, id);
			 * }
			 * 
			 * dep.setDepName(depName.trim());
			 * dep.setDepCodeThird(depCodeThird.trim());
			 * dep.setDepContact(depContact); dep.setDepResp(depResp);
			 * dep.setSuperiorId(superiorId); if (id != null && !"".equals(id))
			 * { dep.setSuperiorId(null); result = baseBiz.updateObj(dep); }
			 * else { dep.setCorpCode(getCorpCode()); result =
			 * depBiz.addDep(dep); } if (result) {
			 * spLog.logSuccessString(opUser, opModule, oppType,
			 * opContent,getCorpCode()); } writer.print(result+"");
			 */
		} catch (Exception e) {
			response.getWriter().print("error");
			EmpExecutionContext.error(e, "操作操作员机构新增或修改出现异常！");
			spLog.logFailureString(opUser, opModule, oppType, opContent + opSper, e,
					lgcorpcode);
		}
	}

	public void checkDepCode (HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		response.getWriter().print(false);
	}

	/**
	 * 删除操作员机构树
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void delete (HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String corpCode = "";
		String oppType = null , opContent = null;
		String opUser = "";
		try {

			// 获取登录sysuser
			LfSysuser sysuser = depPriBiz.getCurrenUser(request);
			if (sysuser == null) {
				EmpExecutionContext.error("机构管理,delete方法session中获取当前登录对象出现异常");
				return;
			}
			corpCode = sysuser.getCorpCode();
			// 判断企业编码获取
			if (corpCode == null || "".equals(corpCode.trim())) {
				EmpExecutionContext.error("机构管理,delete方法session中获取企业编码出现异常");
				return;
			}
			opUser = sysuser.getUserName();

			// String depName2 = request.getParameter("depName");
			oppType = StaticValue.DELETE;
			// String corpCode = getCorpCode();
			// 父级机构ID
			String pareDepId = request.getParameter("pareDepId");
			String depIdstr = request.getParameter("depId");
			/*---strat------增加处理删除操作数据不同步导致的数据缺失报错问题--*/
			Long depId = null;
			LfDep dep = null;
			if (depIdstr != null && !"".equals(depIdstr.trim())) {
				// 解密
				depIdstr = getDecryptValue(request, depIdstr);
				depId = Long.valueOf(depIdstr);
				dep = baseBiz.getById(LfDep.class, depId);
				// 判断机构是否已被删除
				if (dep == null || dep.getDepState() == 2) {
					response.getWriter().print("3");
					opContent = "该机构不存在，可能已被删除！depId：" + depId + ",depName："
							+ (dep == null ? "" : dep.getDepName());
					EmpExecutionContext.error("模块名称：机构管理，企业：" + corpCode + ",操作员："
							+ opUser + "," + opContent);
					return;
				}

				// 判断机构是否在企业内
				if (!corpCode.equals(dep.getCorpCode())) {
					response.getWriter().print("3");
					opContent = "该机构不再本企业内，可能已被删除！depId：" + depId + ",depName："
							+ dep.getDepName();
					EmpExecutionContext.error("模块名称：机构管理，企业：" + corpCode + ",操作员："
							+ opUser + "," + opContent);
					return;
				}

			}
			else {
				response.getWriter().print("2");
				opContent = "删除机构失败！获取请求参数机构id为空！depId:" + depIdstr;
				EmpExecutionContext.error("模块名称：机构管理，企业：" + corpCode + ",操作员：" + opUser
						+ "," + opContent);
				return;
			}
			/*---------------end-----------------*/
			// 获取该机构信息
			opContent = "删除机构（机构名：" + dep.getDepName() + "）";
			CommonVariables commonVar = new CommonVariables();
			// int count=depBiz.deleteDep(depId, commonVar);
			// 解密
			if (pareDepId != null && !"".equals(pareDepId)) {
				pareDepId = getDecryptValue(request, pareDepId);
			}
			Integer count = depPriBiz.deleteDepAndBalance(depId, commonVar, corpCode,
					pareDepId);
			if (commonVar.getErrorCode() == null || count == 1) {
				// response.getWriter().flush();
				response.getWriter().print("1");
				opSucLog(request, opModule, opContent + "成功。", "DELETE");
				spLog.logSuccessString(opUser, opModule, oppType, opContent, corpCode);
			}
			else if (count == 0) {
				// response.getWriter().flush();
				response.getWriter().print("0");
			}
			else {
				// response.getWriter().flush();
				EmpExecutionContext.error("企业：" + corpCode + ",操作员：" + opUser + ","
						+ opContent + "失败");
				response.getWriter().print("2");
			}
		} catch (Exception e) {
			response.getWriter().print("errer");
			EmpExecutionContext.error(e, "删除操作员机构失败！");
		}
	}

	// 获取机构ID 加密树机构树
	public void createTreejm (HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			/*
			 * java.util.Enumeration<String> a = request.getParameterNames();
			 * 
			 * while (a.hasMoreElements()) { String string = (String)
			 * a.nextElement(); }
			 */
			// 机构id
			Long depId = null;
			String depStr = request.getParameter("depId");
			Long userId = null;
			// 获取登录sysuser
			LfSysuser sysuser = depPriBiz.getCurrenUser(request);
			if (sysuser == null) {
				EmpExecutionContext.error("机构管理,createTreejm方法session中获取当前登录对象出现异常");
				return;
			}
			String corpCode = sysuser.getCorpCode();
			// 判断企业编码获取
			if (corpCode == null || "".equals(corpCode.trim())) {
				EmpExecutionContext.error("机构管理,createTreejm方法session中获取企业编码出现异常");
				return;
			}
			userId = sysuser.getUserId();

			if (depStr != null && !"".equals(depStr.trim())) {
				// 解密
				depStr = getDecryptValue(request, depStr);
				depId = Long.parseLong(depStr);
				LfDep dep = baseBiz.getById(LfDep.class, depId);
				String opContent = "";
				// 判断机构是否已被删除
				if (dep == null || dep.getDepState() == 2) {
					opContent = "该机构不存在，可能已被删除！depId：" + depId + ",depName："
							+ (dep == null ? "" : dep.getDepName());
					EmpExecutionContext.error("模块名称：机构管理，加载机构树，企业：" + corpCode + ","
							+ opContent);
					return;
				}

				// 判断机构是否在企业内
				if (!corpCode.equals(dep.getCorpCode())) {
					opContent = "该机构不再本企业内，可能已被删除！depId：" + depId + ",depName："
							+ dep.getDepName();
					EmpExecutionContext.error("模块名称：机构管理，加载机构树，企业：" + corpCode + ","
							+ opContent);
					return;
				}
			}
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			// 获取机构树字符串
			String departmentTree = getDepartmentJosnData3(encryptOrDecrypt, depId,
					userId, corpCode);
			response.getWriter().print(departmentTree);
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e, "获取操作员机构树出现异常！");
		}
	}

	// 获取机构树
	public void createTree2 (HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			/*
			 * java.util.Enumeration<String> a = request.getParameterNames();
			 * 
			 * while (a.hasMoreElements()) { String string = (String)
			 * a.nextElement(); }
			 */
			// 机构id
			Long depId = null;
			String depStr = request.getParameter("depId");
			Long userId = null;
			// 获取登录sysuser
			LfSysuser sysuser = depPriBiz.getCurrenUser(request);
			if (sysuser == null) {
				EmpExecutionContext.error("机构管理,createTree2方法session中获取当前登录对象出现异常");
				return;
			}
			String corpCode = sysuser.getCorpCode();
			// 判断企业编码获取
			if (corpCode == null || "".equals(corpCode.trim())) {
				EmpExecutionContext.error("机构管理,createTree2方法session中获取企业编码出现异常");
				return;
			}
			userId = sysuser.getUserId();

			if (depStr != null && !"".equals(depStr.trim())) {
				depId = Long.parseLong(depStr);
				LfDep dep = baseBiz.getById(LfDep.class, depId);
				String opContent = "";
				// 判断机构是否已被删除
				if (dep == null || dep.getDepState() == 2) {
					opContent = "该机构不存在，可能已被删除！depId：" + depId + ",depName："
							+ (dep == null ? "" : dep.getDepName());
					EmpExecutionContext.error("模块名称：机构管理，加载机构树，企业：" + corpCode + ","
							+ opContent);
					return;
				}

				// 判断机构是否在企业内
				if (!corpCode.equals(dep.getCorpCode())) {
					opContent = "该机构不再本企业内，可能已被删除！depId：" + depId + ",depName："
							+ dep.getDepName();
					EmpExecutionContext.error("模块名称：机构管理，加载机构树，企业：" + corpCode + ","
							+ opContent);
					return;
				}
			}
			// 获取机构树字符串
			String departmentTree = getDepartmentJosnData2(depId, userId, corpCode);
			response.getWriter().print(departmentTree);
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e, "获取操作员机构树出现异常！");
		}
	}

	// 获取机构树
	public void createTree1 (HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute(
					"loginSysuser");
			String departmentTree = "";
			if (lfSysuser != null) {
				departmentTree = getDepartmentJosnData1(lfSysuser);
			}
			response.getWriter().print(departmentTree);
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e, "获取操作员机构树出现异常！");
		}
	}

	// 获取机构树
	public void createTree (HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long userId = null;
		// 获取当前登录账号的userid
		try {
			// 获取登录sysuser
			LfSysuser sysuser = depPriBiz.getCurrenUser(request);
			if (sysuser == null) {
				EmpExecutionContext.error("机构管理,createTree方法session中获取当前登录对象出现异常");
				return;
			}
			userId = sysuser.getUserId();
			// 判断操作员ID获取
			if (userId == null) {
				EmpExecutionContext.error("机构管理,createTree方法session中获取企业编码出现异常");
				return;
			}

			// 机构树字符串
			String departmentTree = getDepartmentJosnData(userId);
			response.getWriter().print(departmentTree);
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e, "获取操作员机构树出现异常！");
		}
	}

	// 根据机构id获取机构详细信息
	@SuppressWarnings("unchecked")
	public void getDep (HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			// 机构id
			String depId = request.getParameter("depId");

			// 获取登录sysuser
			LfSysuser sysuser = depPriBiz.getCurrenUser(request);
			if (sysuser == null) {
				EmpExecutionContext.error("机构管理,getDep方法session中获取当前登录对象出现异常");
				return;
			}
			String corpcode = sysuser.getCorpCode();
			// 判断企业编码获取
			if (corpcode == null || "".equals(corpcode)) {
				EmpExecutionContext.error("机构管理,getDep方法session中获取企业编码出现异常");
				return;
			}
			if (depId == null || "".equals(depId)) {
				EmpExecutionContext.error("机构管理,getDep方法机构ID没传，depId：" + depId);
				return;
			}
			// 解密
			depId = getDecryptValue(request, depId);
			LfDep superDep = baseBiz.getById(LfDep.class, depId);
			if (superDep != null) {
				// 判断机构是否在本企业内
				if (!corpcode.equals(superDep.getCorpCode())) {
					EmpExecutionContext.error("机构管理,getDep方法传入的机构ID不再本企业内，depid：" + depId
							+ ",corpcode:" + corpcode);
					return;
				}
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);

				JSONObject jsonObject = new JSONObject();
				// 加密对象不为空
				if (encryptOrDecrypt != null) {
					jsonObject
							.put("depId", encryptOrDecrypt.encrypt(String
									.valueOf(superDep.getDepId())));
					jsonObject.put("superiorId", encryptOrDecrypt.encrypt(String
							.valueOf(superDep.getSuperiorId())));
				}
				else {
					jsonObject.put("depId", superDep.getDepId());
					jsonObject.put("superiorId", superDep.getSuperiorId());
				}
				jsonObject.put("depName", superDep.getDepName());
				jsonObject.put("depCodeThird", superDep.getDepCodeThird());
				jsonObject.put("depResp",
						superDep.getDepResp() != null ? superDep.getDepResp() : "");
				// 通过json数据的形式写到页�?
				response.getWriter().print(jsonObject.toString());
			}
			else {
				response.getWriter().print("");
			}
		} catch (Exception e) {
			response.getWriter().print("error");
			EmpExecutionContext.error(e, "获取操作员机构树出现异常！");
		}
	}

	// 验证机构编码是否已经存在
	public void check (HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			String depCodeThird = request.getParameter("depCodeThird");
			boolean isExsit = depPriBiz.isDepCodeThirdExists(depCodeThird);
			response.getWriter().print(isExsit + "");
			response.getWriter().flush();
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e, "验证操作员机构编码是否存在出现异常！");
		}
	}

	// 验证机构数目（机构层级，同级机构数，机构总数�?
	public void checkDepsCount (HttpServletRequest request, HttpServletResponse response) {
		// 机构id
		String depId = request.getParameter("depId");
		// 企业编码
		String corpCode = "";
		PrintWriter out = null;
		String result = "";
		try {
			// 获取登录sysuser
			LfSysuser sysuser = depPriBiz.getCurrenUser(request);
			if (sysuser == null) {
				EmpExecutionContext.error("机构管理,checkDepsCount方法session中获取当前登录对象出现异常");
				return;
			}
			corpCode = sysuser.getCorpCode();
			// 判断企业编码获取
			if (corpCode == null || "".equals(corpCode)) {
				EmpExecutionContext.error("机构管理,checkDepsCount方法session中获取企业编码出现异常");
				return;
			}

			LfDep superDep = baseBiz.getById(LfDep.class, depId);
			// 判断机构是否在本企业内
			if (!corpCode.equals(superDep.getCorpCode())) {
				EmpExecutionContext.error("机构管理,checkDepsCount方法传入的机构ID不再本企业内，depid："
						+ depId + ",corpcode:" + corpCode);
				return;
			}

			out = response.getWriter();
			// 验证机构层级，同级机构数，机构�?�?
			result = depPriBiz.checkDepsCount(corpCode, depId);
		} catch (Exception e) {
			result = "error";
			EmpExecutionContext.error(e, "验证操作员机构层数出现异常！");
		}
		finally {
			if (out != null) {
				out.print(result);
				out.flush();
				out.close();
			}
		}
	}

	// 改成OA样式点击机构只列出该机构下的员工，不管该机构的子机构的员工
	public void getDepAndUserTree (HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String epname = request.getParameter("epname");
			String chooseType = request.getParameter("chooseType");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String depId = request.getParameter("depId");
			StringBuffer sb = new StringBuffer();
			String lgcorpcode = "";// 当前登录企业
			// 获取登录sysuser
			LfSysuser sysuser = depPriBiz.getCurrenUser(request);
			if (sysuser == null) {
				EmpExecutionContext.error("机构管理,getDepAndUserTree方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode = sysuser.getCorpCode();
			// 判断企业编码获取
			if (lgcorpcode == null || "".equals(lgcorpcode)) {
				EmpExecutionContext.error("机构管理,getDepAndUserTree方法session中获取企业编码出现异常");
				return;
			}

			LfDep superDep = baseBiz.getById(LfDep.class, depId);
			// 判断机构是否在本企业内
			if (!lgcorpcode.equals(superDep.getCorpCode())) {
				EmpExecutionContext.error("机构管理,getDepAndUserTree方法传入的机构ID不再本企业内，depid："
						+ depId + ",corpcode:" + lgcorpcode);
				return;
			}

			conditionMap.put("depId", depId);
			conditionMap.put("corpCode", lgcorpcode);
			if ("0".equals(chooseType)) {
				conditionMap.put("name&like", epname);
			}
			else if ("1".equals(chooseType)) {
				conditionMap.put("mobile&like", epname);
			}

			List<LfSysuser> sysuserList = baseBiz.getByCondition(LfSysuser.class,
					conditionMap, null);

			if (sysuserList != null && sysuserList.size() > 0) {
				for (LfSysuser user : sysuserList) {
					sb.append("<option value='").append(user.getUserId())
							.append("' mobile='").append(user.getMobile()).append("'>");
					sb.append(user.getName().trim()).append("</option>");
				}
			}
			response.getWriter().print(sb.toString());

		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取操作员机构下操作员出现异常！");
		}
	}

	// 点击选择机构按钮的时候如果包含子机构则获取子机构集合
	public void checkDepIsExist (HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			String depId = request.getParameter("depId");
			// String depName= request.getParameter("depName");
			String depIdsExist = request.getParameter("depIdsExist");

			String lgcorpcode = "";// 当前登录企业

			// 获取登录sysuser
			LfSysuser sysuser = depPriBiz.getCurrenUser(request);
			if (sysuser == null) {
				EmpExecutionContext.error("机构管理,checkDepIsExist方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode = sysuser.getCorpCode();
			// 判断企业编码获取
			if (lgcorpcode == null || "".equals(lgcorpcode)) {
				EmpExecutionContext.error("机构管理,checkDepIsExist方法session中获取企业编码出现异常");
				return;
			}

			String[] depIds = depIdsExist.split(",");
			StringBuffer depIdsTemp = new StringBuffer();
			for (int i = 0; i < depIds.length; i++) {
				if (depIds[i].indexOf("e") > -1) {
					depIdsTemp.append(depIds[i].substring(1) + ",");
				}
				else if (depIds[i].equals(depId)) {
					response.getWriter().print("depExist");
					return;
				}
			}
			// 判断新添加的机构是不是已经添加的机构的子机构
			boolean result = isDepAcontainsDepB(depIdsTemp.toString(), depId, lgcorpcode);
			if (result) {
				response.getWriter().print("depExist");
				return;
			}
			else {
				response.getWriter().print("notExist");
				return;
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e, "验证操作员机构是否包含出现异常！");
		}
	}

	// 判断当前机构是否被包含在其它机构
	private boolean isDepAcontainsDepB (String depIdAs, String depIdB, String corpCode) {
		boolean result = false;
		LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
		String[] depIdAsTemp = depIdAs.split(",");
		try {
			for (int a = 0; a < depIdAsTemp.length; a++) {
				if (depIdAsTemp[a] != null && !"".equals(depIdAsTemp[a])) {
					String deps = new DepDAO().getChildUserDepByParentID(null,
							Long.valueOf(depIdAsTemp[a]));
					String depArray[] = deps.split(",");
					for (int i = 0; i < depArray.length; i++) {
						depIdSet.add(Long.valueOf(depArray[i]));
					}
				}
			}
			result = depIdSet.contains(Long.valueOf(depIdB));
		} catch (Exception e) {
			EmpExecutionContext.error(e, "验证操作员机构是否包含出现异常！");
		}
		return result;
	}

	public void isDepsContainedByDepB (HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String ismut = request.getParameter("ismut");
		String depId = request.getParameter("depId");
		if ("0".equals(ismut)) {
			String countttt = depBiz.getDepCountByDepId(depId);
			response.getWriter().print(countttt);
			return;
		}
		LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
		String depIdsExist = request.getParameter("depIdsExist");
		String[] depIds = depIdsExist.split(",");
		// 将已经存在的机构id放在�?��list里面(如果前缀有e就去掉e放在depIdExistList里面)
		List<Long> depIdExistList = new ArrayList<Long>();
		for (int j = 0; j < depIds.length; j++) {
			if (depIds[j] != null && !"".equals(depIds[j])) {
				if (depIds[j].indexOf("e") > -1) {
					if (!"".equals(depIds[j].substring(1))) {
						depIdExistList.add(Long.valueOf(depIds[j].substring(1)));
					}
				}
				else {
					depIdExistList.add(Long.valueOf(depIds[j]));
				}

			}
		}

		// 查找出要添加的机构的�?��子机构，放在�?��set里面
		/*
		 * lfEmployeeDepList = new
		 * GenericLfEmployeeVoDAO().findEmployeeDepsByDepId(lgcorpcode,depId);
		 * List<Long> depIdListTemp = new ArrayList<Long>(); for(int
		 * i=0;i<lfEmployeeDepList.size();i++) {
		 * depIdSet.add(lfEmployeeDepList.get(i).getDepId()); }
		 */
		List<Long> depIdListTemp = new ArrayList<Long>();
		String deps = new DepDAO().getChildUserDepByParentID(null, Long.valueOf(depId));
		String depArray[] = deps.split(",");
		for (String str : depArray) {
			depIdSet.add(Long.valueOf(str));
		}

		// 遍历这个set，看看已经存在的机构是否包含在这个机构的子机构里面，如果包含的话，就重新生成�?��option列表的字符串给select控件
		for (int a = 0; a < depIdExistList.size(); a++) {
			if (depIdSet.contains(depIdExistList.get(a))) {
				depIdListTemp.add(depIdExistList.get(a));
			}
		}
		// 如果确实有几个已经存在的机构是要添加机构的子机构，那么就从新生成select的html
		String depids = depIdSet.toString();
		depids = depids.substring(1, depids.length() - 1);
		// 计算机构人数
		String countttt = depBiz.getDepCountByDepId(depids);
		if (depIdListTemp.size() > 0) {
			// depIdExistList.removeAll(depIdListTemp);
			String tempDeps = depIdListTemp.toString();
			tempDeps = tempDeps.substring(1, tempDeps.length() - 1);
			response.getWriter().print(countttt + "," + tempDeps);
			return;
		}
		// 如果没有包含关系
		else {
			response.getWriter().print("notContains" + "&" + countttt);
			return;
		}
		/*
		 * //重新生成select option时将要添加的机构也算进来
		 * depIdExistList.add(Long.valueOf(depId));
		 * //通过机构id字符串获取机构列表�?与机构id�?��对应，这些机构信心将显示在页面上�? List<LfEmployeeDep>
		 * deps = getEmpDepByDepIds(depIdExistList);
		 * 
		 * StringBuilder sb = new StringBuilder();
		 * //如果以前已经存在的机构是包含子机构，那么生成包含子机构的option，并且当前所选机构也会生�?包含子机�?的option if
		 * (deps != null && deps.size() > 0) { for (LfEmployeeDep user : deps) {
		 * if (set.contains("e"+user.getDepId()) ||
		 * user.getDepId()-Long.valueOf(depId)==0) {
		 * sb.append("<option value='")
		 * .append(user.getDepId()).append("' et='3' mobile=''>"
		 * ).append(" [ 机构] "
		 * ).append(user.getDepName().trim()).append("(包含子机�?</option>"); }else
		 * { sb.append("<option value='").append(user.getDepId()).append(
		 * "' et='2' mobile=''>"
		 * ).append(" [ 机构] ").append(user.getDepName().trim
		 * ()).append("</option>"); } } }
		 * response.getWriter().print(sb.toString());
		 */

	}
	/**
	 * 树
	 * 
	 * @return
	 */
	protected String getDepartmentJosnData2 (Long depId, Long userId) {
		StringBuffer tree = null;
		LfSysuser sysuser = null;
		try {
			sysuser = baseBiz.getById(LfSysuser.class, userId);
			if (sysuser != null && sysuser.getPermissionType() != 1) {
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps = new ArrayList<LfDep>();
				// 第一次进入depId(机构Id)为空则获取当前操作员所在机构下的所有子机构
				if (depId == null) {
					List<LfDep> list = depBiz.getAllDeps(userId);
					if (list != null && list.size() > 0) {
						lfDeps.add(list.get(0));
					}
					// LfDep lfDep = depBiz.getAllDeps(userId).get(0);//这里需要优化
					// lfDeps.add(lfDep);
					// lfDeps.addAll(new
					// DepBiz().getDepsByDepSuperId(lfDep.getDepId()));
					// 非首次进入，根据depId(机构Id)获取本机构下的所有子机构
				}
				else {
					lfDeps = depBiz.getDepsByDepSuperId(depId);
				}

				LfDep lfDep = null;
				tree = new StringBuffer("[");
				if (lfDeps != null) {
					for (int i = 0; i < lfDeps.size(); i++) {
						lfDep = lfDeps.get(i);
						String path = lfDep.getDeppath();
						if (path == null) {
							path = "";
						}
						if (!path.startsWith("/")) {
							path = "/" + path;
						}
						tree.append("{");
						tree.append("id:").append(lfDep.getDepId());
						tree.append(",name:'").append(lfDep.getDepName()).append("'");
						tree.append(",pId:").append(lfDep.getSuperiorId());
						tree.append(",depId:'").append(lfDep.getDepId() + "'");
						tree.append(",dlevel:'").append(lfDep.getDepLevel() + "'");
						tree.append(",dpath:'").append(path + "'");
						tree.append(",isParent:").append(true);
						tree.append("}");
						tree.append(",");
					}
					if (tree.length() > 1) {
						tree = tree.deleteCharAt(tree.length() - 1);
					}
				}
				tree.append("]");

			}
			else {
				tree = new StringBuffer("[]");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "操作员机构获取机构出现异常！userId:" + userId + " depId:"
					+ depId);
			tree = new StringBuffer("[]");
		}
		return tree.toString();
	}

	/**
	 * 树
	 * 
	 * @return
	 */
	protected String getDepartmentJosnData3 (ParamsEncryptOrDecrypt encryptOrDecrypt,
			Long depId, Long userId, String corpCode) {
		StringBuffer tree = null;
		LfSysuser sysuser = null;
		try {
			sysuser = baseBiz.getById(LfSysuser.class, userId);
			if (sysuser != null && sysuser.getPermissionType() != 1) {
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps = new ArrayList<LfDep>();
				// 第一次进入depId(机构Id)为空则获取当前操作员所在机构下的所有子机构
				if (depId == null) {
					// List<LfDep> list = depBiz.getAllDeps(userId);
					List<LfDep> list = depBiz.getAllDepByUserIdAndCorpCode(userId,
							corpCode);
					if (list != null && list.size() > 0) {
						lfDeps.add(list.get(0));
					}
					// LfDep lfDep = depBiz.getAllDeps(userId).get(0);//这里需要优化
					// lfDeps.add(lfDep);
					// lfDeps.addAll(new
					// DepBiz().getDepsByDepSuperId(lfDep.getDepId()));
					// 非首次进入，根据depId(机构Id)获取本机构下的所有子机构
				}
				else {
					// lfDeps = depBiz.getDepsByDepSuperId(depId);
					lfDeps = depBiz.getDepsByDepSuperIdAndCorpCode(depId, corpCode);
				}

				LfDep lfDep = null;
				tree = new StringBuffer("[");
				if (lfDeps != null) {
					for (int i = 0; i < lfDeps.size(); i++) {
						lfDep = lfDeps.get(i);

						String superiorId = String.valueOf(lfDep.getSuperiorId());
						String depidstr = String.valueOf(lfDep.getDepId());
						if (encryptOrDecrypt != null) {
							superiorId = encryptOrDecrypt.encrypt(superiorId);
							depidstr = encryptOrDecrypt.encrypt(depidstr);
						}
						String path = lfDep.getDeppath();
						if (path == null) {
							path = "";
						}
						if (!path.startsWith("/")) {
							path = "/" + path;
						}
						tree.append("{");
						tree.append("id:").append("'" + depidstr + "'");
						tree.append(",name:'").append(lfDep.getDepName()).append("'");
						tree.append(",pId:'").append(superiorId + "'");
						tree.append(",depId:'").append(depidstr + "'");
						tree.append(",dlevel:'").append(lfDep.getDepLevel() + "'");
						tree.append(",dpath:'").append(path + "'");
						tree.append(",isParent:").append(true);
						tree.append("}");
						tree.append(",");
					}
					if (tree.length() > 1) {
						tree = tree.deleteCharAt(tree.length() - 1);
					}
				}
				tree.append("]");

			}
			else {
				tree = new StringBuffer("[]");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "操作员机构获取机构出现异常！userId:" + userId + " depId:"
					+ depId);
			tree = new StringBuffer("[]");
		}
		return tree.toString();
	}

	/**
	 * 树
	 * 
	 * @return
	 */
	protected String getDepartmentJosnData2 (Long depId, Long userId, String corpCode) {
		StringBuffer tree = null;
		LfSysuser sysuser = null;
		try {
			sysuser = baseBiz.getById(LfSysuser.class, userId);
			if (sysuser != null && sysuser.getPermissionType() != 1) {
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps = new ArrayList<LfDep>();
				// 第一次进入depId(机构Id)为空则获取当前操作员所在机构下的所有子机构
				if (depId == null) {
					// List<LfDep> list = depBiz.getAllDeps(userId);
					List<LfDep> list = depBiz.getAllDepByUserIdAndCorpCode(userId,
							corpCode);
					if (list != null && list.size() > 0) {
						lfDeps.add(list.get(0));
					}
					// LfDep lfDep = depBiz.getAllDeps(userId).get(0);//这里需要优化
					// lfDeps.add(lfDep);
					// lfDeps.addAll(new
					// DepBiz().getDepsByDepSuperId(lfDep.getDepId()));
					// 非首次进入，根据depId(机构Id)获取本机构下的所有子机构
				}
				else {
					// lfDeps = depBiz.getDepsByDepSuperId(depId);
					lfDeps = depBiz.getDepsByDepSuperIdAndCorpCode(depId, corpCode);
				}

				LfDep lfDep = null;
				tree = new StringBuffer("[");
				if (lfDeps != null) {
					for (int i = 0; i < lfDeps.size(); i++) {
						lfDep = lfDeps.get(i);
						String path = lfDep.getDeppath();
						if (path == null) {
							path = "";
						}
						if (!path.startsWith("/")) {
							path = "/" + path;
						}
						tree.append("{");
						tree.append("id:").append(lfDep.getDepId());
						tree.append(",name:'").append(lfDep.getDepName()).append("'");
						tree.append(",pId:").append(lfDep.getSuperiorId());
						tree.append(",depId:'").append(lfDep.getDepId() + "'");
						tree.append(",dlevel:'").append(lfDep.getDepLevel() + "'");
						tree.append(",dpath:'").append(path + "'");
						tree.append(",isParent:").append(true);
						tree.append("}");
						tree.append(",");
					}
					if (tree.length() > 1) {
						tree = tree.deleteCharAt(tree.length() - 1);
					}
				}
				tree.append("]");

			}
			else {
				tree = new StringBuffer("[]");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "操作员机构获取机构出现异常！userId:" + userId + " depId:"
					+ depId);
			tree = new StringBuffer("[]");
		}
		return tree.toString();
	}

	/**
	 * 机构树
	 * 
	 * @return
	 */
	protected String getDepartmentJosnData1 (LfSysuser lfSysuser) {

		// 树json数据
		StringBuffer tree = null;
		// 个人权限
		if (lfSysuser.getPermissionType() == 1) {
			tree = new StringBuffer("[]");
		}
		// 机构权限
		else {
			DepBiz depBiz = new DepBiz();
			List<LfDep> lfDeps;

			try {
				// 获取管辖范围内的所有机构
				// lfDeps = depBiz.getAllDeps(lfSysuser.getUserId());
				lfDeps = depBiz.getAllDepByUserIdAndCorpCode(lfSysuser.getUserId(),
						lfSysuser.getCorpCode());
				LfDep lfDep = null;
				// json数据拼写
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId() + "'");
					// tree.append(",dlevel:").append(lfDep.getDepLevel());
					tree.append(",isParent:").append(true);
					tree.append("}");
					if (i != lfDeps.size() - 1) {
						tree.append(",");
					}
				}
				tree.append("]");

			} catch (Exception e) {
				// 异常处理
				EmpExecutionContext.error(e, "操作员机构树获取失败！");
			}
		}
		return tree == null ? "" : tree.toString();
	}
	/**
	 * 机构树
	 * 
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	protected String getDepartmentJosnData (Long userid) throws Exception {
		StringBuffer tree = null;
		// 根据用户id获取用户信息
		LfSysuser currUser = baseBiz.getById(LfSysuser.class, userid);
		if (currUser.getPermissionType() == 1) {
			tree = new StringBuffer("[]");
		}
		else {
			// 机构biz
			DepBiz depBiz = new DepBiz();
			List<LfDep> lfDeps;

			try {
				// 如果是100000的企业
				if (currUser.getCorpCode().equals("100000")) {
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
					conditionMap.put("depState", "1");
					orderbyMap.put("depId", StaticValue.ASC);

					lfDeps = baseBiz
							.getByCondition(LfDep.class, conditionMap, orderbyMap);
				}
				else {
					// 获取管辖范围内的所有机构
					lfDeps = depBiz.getAllDeps(userid);
				}
				LfDep lfDep = null;
				// 机构树的json数据拼写
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId() + "'");
					// tree.append(",level:").append(lfDep.getDepLevel());
					// tree.append(",dlevel:").append(lfDep.getDepLevel());
					tree.append(",isParent:").append(true);
					tree.append("}");
					if (i != lfDeps.size() - 1) {
						tree.append(",");
					}
				}
				tree.append("]");

			} catch (Exception e) {
				// 异常处理
				EmpExecutionContext.error(e, "操作员机构树获取失败！");
			}
		}
		return tree == null ? "" : tree.toString();
	}

	/**
	 * 获取操作员名称
	 * 
	 * @description
	 * @param request
	 * @return
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-4-2 上午09:17:49
	 */
	public String getOpUser (HttpServletRequest request) {
		String opUser = "";
		try {
			Object sysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if (sysuserObj != null) {
				LfSysuser sysuser = (LfSysuser) sysuserObj;
				opUser = sysuser.getUserName();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "session获取操作员名称异常，session为空!");
		}
		return opUser;
	}
	/**
	 * @description 记录操作成功日志
	 * @param request
	 * @param modName
	 *            模块名称
	 * @param opContent
	 *            操作详情
	 * @param opType
	 *            操作类型 ADD UPDATE DELETE GET OTHER
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-3-3 上午11:29:50
	 */
	public void opSucLog (HttpServletRequest request, String modName, String opContent,
			String opType) {
		LfSysuser lfSysuser = null;
		try {
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if (obj == null)
				return;
			lfSysuser = (LfSysuser) obj;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "记录操作日志异常，session为空！");
		}
		if (lfSysuser != null) {
			EmpExecutionContext.info(modName, lfSysuser.getCorpCode(),
					String.valueOf(lfSysuser.getUserId()), lfSysuser.getUserName(),
					opContent, opType);
		}
	}

	/**
	 * 判断当前机构是否有彩信与短信余额
	 * 
	 * @param request
	 * @param response
	 */
	public void checkMsg (HttpServletRequest request, HttpServletResponse response) {
		String depId = request.getParameter("depId");
		String corpCode = "";
		try {
			// 获取登录sysuser
			LfSysuser sysuser = depPriBiz.getCurrenUser(request);
			if (sysuser == null) {
				EmpExecutionContext.error("机构管理,checkMsg方法session中获取当前登录对象出现异常");
				return;
			}
			corpCode = sysuser.getCorpCode();
			// 判断企业编码获取
			if (corpCode == null || "".equals(corpCode)) {
				EmpExecutionContext.error("机构管理,checkMsg方法session中获取企业编码出现异常");
				return;
			}

			// 解密
			if (depId != null && !"".equals(depId)) {
				depId = getDecryptValue(request, depId);
			}

			LfDep superDep = baseBiz.getById(LfDep.class, depId);
			// 判断机构是否在本企业内
			if (!corpCode.equals(superDep.getCorpCode())) {
				EmpExecutionContext.error("机构管理,checkMsg方法传入的机构ID不再本企业内，depid：" + depId
						+ ",corpcode:" + corpCode);
				return;
			}

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("targetId", depId);
			List<LfDepUserBalance> pareBalances = baseBiz.getByCondition(
					LfDepUserBalance.class, conditionMap, null);
			if (pareBalances != null && pareBalances.size() > 0) {
				LfDepUserBalance dep = pareBalances.get(0);
				if (dep.getSmsBalance() > 0) {
					response.getWriter().print("true");
				}
				else if (dep.getMmsBalance() > 0) {
					response.getWriter().print("true");
				}
				else {
					response.getWriter().print("false");
				}
			}
		} catch (Exception e) {
			// 异常处理
			EmpExecutionContext.error(e, "彩信与短信余额获取失败！");
		}
	}
	/**
	 * session获取当前登录用户Id
	 * 
	 * @Title: getUserId
	 * @Description: TODO
	 * @param request
	 * @return long
	 * @author duanjl <duanjialin28827@163.com>
	 * @date 2015-9-2 下午01:59:31
	 */
	public long getUserId (HttpServletRequest request) {
		try {
			Object loginSysuserObj = request.getSession(false).getAttribute(
					"loginSysuser");
			if (loginSysuserObj != null) {
				LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
				if (loginSysuser != null && loginSysuser.getUserId() != null) {
					return loginSysuser.getUserId();
				}
				else {
					return -1;
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error("SESSION获取当前登录用户Id失败!");
			return -1;
		}
		return -1;
	}

	/**
	 * 获取加密对象
	 * 
	 * @description
	 * @param request
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-26 下午07:29:28
	 */
	public ParamsEncryptOrDecrypt getParamsEncryptOrDecrypt (HttpServletRequest request) {
		try {
			ParamsEncryptOrDecrypt encryptOrDecrypt = null;
			// 加密对象
			Object encrypOrDecrypttobject = (ParamsEncryptOrDecrypt) request.getSession(
					false).getAttribute("decryptObj");
			// 加密对象不为空
			if (encrypOrDecrypttobject != null) {
				// 强转类型
				encryptOrDecrypt = (ParamsEncryptOrDecrypt) encrypOrDecrypttobject;
			}
			else {
				EmpExecutionContext.error("从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "从session获取加密对象异常。");
			return null;
		}
	}
	/**
	 * 上传机构管理的文件
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void organizUplode (HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<LfDep> listDep = null;
		LfSysuser loginSysuser = super.getLoginUser(request);

		// 获取登录user所在的部门
		// Long dept = loginSysuser.getDepId();
		// 获取当前机构，机构编码，深度，和上一级机构编码
		listDep = depExportDao.findByUserId(loginSysuser.getUserId());
		if (listDep == null || listDep.size() <= 0) {
			EmpExecutionContext.error("机构批量导入，查询数据库内容为空");
			return;
		}
		String deppath = listDep.get(0).getDeppath();
		OptUpload opt = new OptUpload();

		LinkedHashMap<Integer, String> erroMap = new LinkedHashMap<Integer, String>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 记录文件解析开始时间
		Long startTime = System.currentTimeMillis();

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload uplode = new ServletFileUpload(factory);

		List<FileItem> fileList = null;
		fileList = uplode.parseRequest(request);
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		String contentLog = "文件开始解析时间：" + sdf.format(startTime) + "文件解析耗时：" + time + "ms";
		EmpExecutionContext.info("机构管理：", loginSysuser.getCorpCode().toString(),
				loginSysuser.getUserId().toString(), loginSysuser.getUserName(),
				contentLog, "GET");

		Iterator<FileItem> it = fileList.listIterator();
		while (it.hasNext()) {
			FileItem fileItem = it.next();
			if (!fileItem.isFormField() && fileItem.getFieldName().length() > 0) {
				// 上传文件名称
				String fileCurName = fileItem.getName();
				// 文件类型
				String fileType = fileCurName.substring(fileCurName.lastIndexOf("."));
				if (fileType.equals(".xls")) {
					depUp.depUploadXls(request, deppath, opt, erroMap, loginSysuser,
							fileItem);
				}
				else if (fileType.equals(".xlsx")) {
					depUp.depUploadXlsx(request, deppath, opt, erroMap, loginSysuser,
							fileItem);
				}
				else {
					EmpExecutionContext.error("只支持xls和xlsx文件的上传");
				}

			}
		}
		request.setAttribute("logData", opt);
		request.setAttribute("errMap", erroMap);
		request.getRequestDispatcher(PATH + "/opt_upload.jsp").forward(request, response);

	}

	public void goUplode (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher(PATH + "/opt_upload.jsp").forward(request, response);
	}

	public void exportDep (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取模块路径
		String excelPath = new TxtFileUtil().getWebRoot() + "user/operator/file";
		// 日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time = System.currentTimeMillis();
		// 获取当前选中的机构的id和名称
		String id = request.getParameter("id");
		try {
			PrintWriter out = response.getWriter();
			// 是否第一次打开
			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo, request);
			// 漏洞修复 session里获取操作员信息
			Long userId = SysuserUtil.longLguserid(request);
			if (userId == null) {
				EmpExecutionContext.error("机构管理,exportDep方法session中获取当前登录对象出现异常");
				return;

			}
			// 获取当前登录用户信息
			LfSysuser lfUser = baseBiz.getById(LfSysuser.class, userId);
			// 获取当前企业编码
			String corpCode = request.getParameter("lgcorpcode");

			// 获取导出数据的查询条件，判断是否有选中的机构，如果有，则导入选中机构其下的数据，如果没有则导入所有数据
			Long depId;
			if (id != null && !"".equals(id)) {
				depId = Long.parseLong(getDecryptValue(request, id.trim()));
			}
			else {
				depId = lfUser.getDepId();
				;
			}

			// 获取导出数据的查询条件。
			Map<String, String> resultMap = new DepExport().createDepExcel(excelPath,
					corpCode, depId, pageInfo, request);

			if (resultMap != null && resultMap.size() > 0) {
				String fileName = resultMap.get("FILE_NAME");
				String filePath = resultMap.get("FILE_PATH");

				// 写日志
				if (pageInfo != null) {
					long end_time = System.currentTimeMillis();
					String opContent = "导出机构开始时间："
							+ format.format(System.currentTimeMillis()) + ",耗时:"
							+ (end_time - begin_time) + "ms,导出总数："
							+ pageInfo.getTotalRec() + "条 ";
					opSucLog(request, "机构管理", opContent, "OTHER");
				}

				request.getSession(false).setAttribute("exportToExcel",
						fileName + "@@" + filePath);
				out.print("true");

			}
			else {
				out.print("false");
			}
		} catch (Exception e) {
			// 异常处理
			EmpExecutionContext.error(e, "机构管理导出异常！");
		}

	}
	/**
	 * excel文件导出
	 * 
	 * @param request
	 * @param response
	 */
	public void downloadFile (HttpServletRequest request, HttpServletResponse response) {
		String down_session = request.getParameter("down_session");
		try {
			HttpSession session = request.getSession(false);
			Object obj = session.getAttribute(down_session);
			if (obj != null) {
				String result = (String) obj;
				if (result.indexOf("@@") > -1) {
					String[] file = result.split("@@");
					// 弹出下载页面。
					DownloadFile dfs = new DownloadFile();
					dfs.downFile(request, response, file[1], file[0]);
					session.removeAttribute(down_session);
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "excel文件导出异常！");
		}
	}

}
