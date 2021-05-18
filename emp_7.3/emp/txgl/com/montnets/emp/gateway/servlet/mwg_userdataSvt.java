package com.montnets.emp.gateway.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.gateway.AgwParamConf;
import com.montnets.emp.entity.gateway.AgwParamValue;
import com.montnets.emp.entity.gateway.AprotocolTmpl;
import com.montnets.emp.entity.system.LfGlobalVariable;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.gateway.biz.MwGatewayBiz;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.biz.PageFieldConfigBiz;
import com.montnets.emp.servmodule.txgl.entity.*;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * mwadmin 登录操作servlet
 * 
 * @功能概要：
 * @项目名称： p_txgl
 * @初创作者： liuxiangheng <xiaokanrensheng1012@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2017-4-21 下午04:47:49
 *        <p>
 *        修改记录1：
 *        </p>
 * 
 *        <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
@SuppressWarnings("serial")
public class mwg_userdataSvt extends BaseServlet
{
	final ErrorLoger				errorLoger	= new ErrorLoger();

	// 操作模块
	final String					opModule	= StaticValue.GATE_CONFIG;

	final String					opSper		= StaticValue.OPSPER;

	private final String			empRoot		= "txgl";

	private final String			basePath	= "/mwgateway";

	private final MwGatewayBiz	gatewayBiz	= new MwGatewayBiz();

	/**
	 * mwadmin通道账户管理查询
	 * 
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		long stime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		BaseBiz baseBiz = new BaseBiz();
		// 获取系统定义的短彩类型值
		PageFieldConfigBiz gcBiz = new PageFieldConfigBiz();
		List<LfPageField> pagefileds = gcBiz.getPageFieldById("100004");
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// conditionMap.put("uid&>", "100001");
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
			// 通道账号
			String gt_userid = null;
			// 通道账户名称
			String staffName = null;
			// 账户类型
			String accouttype = null;
			// 运营商账户id
			String spaccid = null;
			// 合作商 0 梦网科技  1 其他 
			String sptype = null;
			//连接类型 1 所有链路实时连接 0 单链路单连
			String keepconn=null;
			

			boolean isBack = request.getParameter("isback") == null ? false : true;// 是否返回操作
			HttpSession session = request.getSession(false);
			if(isBack)
			{
				pageInfo = (PageInfo) session.getAttribute("mwg_pageinfo");
				gt_userid = session.getAttribute("mwg_gt_userid") == null ? "" : String.valueOf(session.getAttribute("mwg_gt_userid"));
				accouttype = session.getAttribute("mwg_accouttype") == null ? "" : String.valueOf(session.getAttribute("mwg_accouttype"));
				staffName = session.getAttribute("mwg_staffName") == null ? "" : String.valueOf(session.getAttribute("mwg_staffName"));
				spaccid = session.getAttribute("mwg_spaccid") == null ? "" : String.valueOf(session.getAttribute("mwg_spaccid"));
				sptype = session.getAttribute("mwg_sptype") == null ? "" : String.valueOf(session.getAttribute("mwg_sptype"));
				keepconn = session.getAttribute("mwg_keepconn") == null ? "" : String.valueOf(session.getAttribute("mwg_keepconn"));
				request.setAttribute("gt_userid", gt_userid);
				request.setAttribute("accouttype", accouttype);
				request.setAttribute("staffName", staffName);
				request.setAttribute("spaccid", spaccid);
				request.setAttribute("sptype", sptype);
				request.setAttribute("keepconn", keepconn);

			}
			else
			{
				pageSet(pageInfo, request);
				gt_userid = request.getParameter("gt_userid");
				accouttype = request.getParameter("accouttype");
				staffName = request.getParameter("staffName");
				spaccid = request.getParameter("spaccid");
				sptype = request.getParameter("sptype");
				keepconn = request.getParameter("keepconn");
			}
			session.setAttribute("mwg_pageinfo", pageInfo);
			session.setAttribute("mwg_gt_userid", gt_userid);
			session.setAttribute("mwg_accouttype", accouttype);
			session.setAttribute("mwg_spaccid", spaccid);
			session.setAttribute("mwg_sptype", sptype);
			session.setAttribute("mwg_keepconn", keepconn);
			if(gt_userid != null && !"".equals(gt_userid))
			{
				conditionMap.put("userId", gt_userid);
			}
			if(accouttype != null && !"".equals(accouttype))
			{
				conditionMap.put("accouttype", accouttype);
			}
			if(staffName != null && !"".equals(staffName))
			{
				conditionMap.put("staffName", staffName);
			}
			if(spaccid != null && !"".equals(spaccid))
			{
				conditionMap.put("spaccid", spaccid);
			}
			if(sptype != null && !"".equals(sptype))
			{
				conditionMap.put("sptype", sptype);
			}
			if(keepconn != null && !"".equals(keepconn))
			{
				conditionMap.put("keepconn", keepconn);
			}

			List<DynaBean> userList = gatewayBiz.getAccountList(conditionMap, pageInfo);
			orderMap.clear();
			conditionMap.clear();
			orderMap.put("userId", StaticValue.ASC);
			conditionMap.put("userType", "1");
			List<Userdata> userListCon = baseBiz.getByCondition(Userdata.class, conditionMap, orderMap);
			List<AgwAccount> accountList = baseBiz.getEntityList(AgwAccount.class);
			Map<String, AgwAccount> accountMap = new LinkedHashMap<String, AgwAccount>();
			for (AgwAccount agwAccount : accountList)
			{
				accountMap.put(agwAccount.getPtAccUid().toString(), agwAccount);
			}
			// 加密后的集合
			Map<String, String> keyIdMap = new HashMap<String, String>();
			// ID加密
			if(userList != null && userList.size() > 0)
			{
				// 加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				// 加密对象不为空
				if(encryptOrDecrypt != null)
				{
					boolean result = encryptOrDecrypt.batchEncryptByDynaBeanToMap(userList, "uid", keyIdMap);
					if(!result)
					{
						EmpExecutionContext.error("查询通道账户管理列表，参数加密失败。");
						throw new Exception("查询通道账户管理列表，参数加密失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("查询通道账户管理列表，从session中获取加密对象为空！");
					userList.clear();
					throw new Exception("查询通道账户管理列表，获取加密对象失败。");
				}
			}
			request.setAttribute("keyIdMap", keyIdMap);
			request.setAttribute("userList", userList);
			request.setAttribute("userListCon", userListCon);
			request.setAttribute("accountMap", accountMap);
			request.getSession(false).setAttribute("mwg_pagefileds", pagefileds);
			request.setAttribute("pageInfo", pageInfo);
			if(StaticValue.getCORPTYPE() ==1){
				request.getRequestDispatcher(this.empRoot + this.basePath + "/mwg_userdata1.jsp").forward(request, response);
			}else{
				request.getRequestDispatcher(this.empRoot + this.basePath + "/mwg_userdata.jsp").forward(request, response);
			}
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request, "通道账户管理", "(" + sDate + ")查询", StaticValue.GET);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "通道账户管理查询异常"));
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try
			{
				request.getSession(false).setAttribute("mwg_pagefileds", pagefileds);
				request.getRequestDispatcher(this.empRoot + this.basePath + "/mwg_userdata.jsp").forward(request, response);
			}
			catch (ServletException e1)
			{
				EmpExecutionContext.error(errorLoger.getErrorLog(e1, "通道账户管理查询servlet异常！"));
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(errorLoger.getErrorLog(e1, "通道账户管理查询servlet跳转异常！"));
			}
		}
	}

	/**
	 * 获取通讯协议
	 * 
	 * @param request
	 * @param response
	 */
	public void getPrTmplOption(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			String accouttype = request.getParameter("accouttype");
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("protocol", "asc");
			List<AprotocolTmpl> tmplList = new BaseBiz().getByCondition(AprotocolTmpl.class, null, orderbyMap);
			String empLangName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
			String pleaseSelect = "zh_HK".equals(empLangName)?"Please select":"zh_TW".equals(empLangName)?"請選擇":"请选择";
			String html = "<option value=''>==="+pleaseSelect+"===</option>";
			String otherStr = "";
			for (int i = 0; i < tmplList.size(); i++)
			{
				int proCode = tmplList.get(i).getProtocolCode();
				if(accouttype != null && "2".equals(accouttype))
				{
					if(proCode == 50)
					{
						otherStr += "<option  value='" + tmplList.get(i).getProtocolCode() + "'>" + tmplList.get(i).getProtocol() + "</option>";
					}
					if(proCode > 100)
					{
						html += "<option  value='" + tmplList.get(i).getProtocolCode() + "'>" + tmplList.get(i).getProtocol() + "</option>";
					}
				}
				else
				{
					if(proCode < 100)
					{
						if(proCode == 50)
						{
							otherStr += "<option  value='" + tmplList.get(i).getProtocolCode() + "'>" + tmplList.get(i).getProtocol() + "</option>";
						}
						else
						{
							html += "<option  value='" + tmplList.get(i).getProtocolCode() + "'>" + tmplList.get(i).getProtocol() + "</option>";
						}
					}
				}
			}
			writer.print(html + otherStr);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "获取通讯协议异常！"));
			writer.print("");
		}
	}

	/**
	 * 获取通讯协议配置属性
	 * 
	 * @param request
	 * @param response
	 */
	public void getPrTmplContent(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			String protocolCode = request.getParameter("protocolCode");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("protocolCode", protocolCode);
			List<AprotocolTmpl> tmplList = new BaseBiz().getByCondition(AprotocolTmpl.class, conditionMap, null);

			if(tmplList != null && tmplList.size() > 0)
			{
				writer.print(tmplList.get(0).getProtocolParam() == null ? "" : tmplList.get(0).getProtocolParam());
			}
			else
			{
				writer.print("");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "通讯协议配置属性异常！"));
			if(writer!=null){
				writer.print("");
			}
		}
	}

	/**
	 * 通道账户管理修改跳转页面
	 * 
	 * @param request
	 * @param response
	 */
	public void toEdit(HttpServletRequest request, HttpServletResponse response)
	{
		// String uid = request.getParameter("uid");
		String uid;
		String accouTtype = request.getParameter("accouTtype");
		BaseBiz baseBiz = new BaseBiz();
		try
		{
			String keyId = request.getParameter("keyId");
			// 加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			// 加密对象不为空
			if(encryptOrDecrypt != null)
			{
				// 解密
				uid = encryptOrDecrypt.decrypt(keyId);
				if(uid == null)
				{
					EmpExecutionContext.error("修改短彩sp账户，参数解密码失败，keyId:" + keyId);
					throw new Exception("修改短彩sp账户，参数解密码失败。");
				}
			}
			else
			{
				EmpExecutionContext.error("修改短彩sp账户，从session中获取加密对象为空！");
				throw new Exception("修改短彩sp账户，获取加密对象失败。");
			}
			Userdata userdata = baseBiz.getById(Userdata.class, uid);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("ptAccUid", uid);
			List<AgwAccount> accountList = baseBiz.getByCondition(AgwAccount.class, conditionMap, null);
			AgwAccount account = null;
			if(accountList != null && accountList.size() > 0)
			{
				account = accountList.get(0);
			}
			LfSpFee spFee = null;
			if(account != null)
			{
				conditionMap.clear();
				conditionMap.put("spUser", account.getSpAccid());
				conditionMap.put("accountType", accouTtype);
				List<LfSpFee> spFeeList = baseBiz.getByCondition(LfSpFee.class, conditionMap, null);
				if(spFeeList != null && spFeeList.size() > 0)
				{
					spFee = spFeeList.get(0);
				}
			}
			request.setAttribute("spFee", spFee);

			request.setAttribute("userdata", userdata);
			request.setAttribute("account", account);
			// 短信sp账号则查出主备 多连路连接
			if("1".equals(accouTtype) && account != null)
			{
				conditionMap.clear();
				conditionMap.put("ptaccid", account.getPtAccId());
				List<GwGateconninfo> gwgateconinfos = baseBiz.getByCondition(GwGateconninfo.class, conditionMap, null);
				request.setAttribute("gwgcinfos", gwgateconinfos);
			}

			// 获取运营商备用URL
			List<String> bakUrlList = new ArrayList<String>();
			conditionMap.clear();
			conditionMap.put("globalKey", "BALANCEBAKURL");
			List<LfGlobalVariable> globalVariableList = new BaseBiz().getByCondition(LfGlobalVariable.class, conditionMap, null);
			if(globalVariableList != null && globalVariableList.size() > 0)
			{
				String balanceBakRrl = globalVariableList.get(0).getGlobalStrValue();
				if(balanceBakRrl != null && balanceBakRrl.trim().length() > 0)
				{
					EmpExecutionContext.info("跳转通道账户管理修改页面，获取查询运营商备用URL为:" + balanceBakRrl);
					// 有多个查询运营商余额备用地址
					if(balanceBakRrl.indexOf("@") >= 0)
					{
						// 获取查询运营商余额备用地址
						String[] url = balanceBakRrl.split("@");
						// 设置查询运营商余额备用地址
						for (int i = 0; i < url.length; i++)
						{
							bakUrlList.add(url[i]);
						}
					}
					// 一个查询运营商余额备用地址
					else
					{
						bakUrlList.add(balanceBakRrl);
					}
				}
				else
				{
					EmpExecutionContext.info("跳转通道账户管理修改页面，获取查询运营商备用URL为空。");
				}
			}
			request.setAttribute("bakUrlList", bakUrlList);
			if(StaticValue.getCORPTYPE() ==1){
				request.getRequestDispatcher(this.empRoot + this.basePath + "/mwg_editUserdata1.jsp").forward(request, response);
			}else{
				request.getRequestDispatcher(this.empRoot + this.basePath + "/mwg_editUserdata.jsp").forward(request, response);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "通道账户管理修改跳转异常！"));
		}
	}


	/**
	 * 修改通道账户
	 * 
	 * @param request
	 * @param response
	 */
	public void edit(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			writer.print(gatewayBiz.editUserdata(request));
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "修改通道账户异常！"));
			if(writer!=null){
				writer.print(ERROR);
			}
		}

	}

	
	
	/**
	 * 修改通道账户高级配置
	 * 
	 * @param request
	 * @param response
	 */
	public void editAdvanceConf(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			writer.print(gatewayBiz.editAdvanceConf(request));
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "修改通道账户异常！"));
			if(writer!=null){
				writer.print(ERROR);
			}
		}

	}
	
	
	/**
	 * 通过网关编号获取通道配置信息
	 * 
	 * @param gwNo
	 * @param conditionMap
	 */
	private void paramConfToParamValue(Integer gwNo, LinkedHashMap<String, String> conditionMap)
	{
		List<AgwParamValue> paramList = new ArrayList<AgwParamValue>();
		BaseBiz baseBiz = new BaseBiz();
		try
		{
			List<AgwParamConf> confList = baseBiz.getByCondition(AgwParamConf.class, conditionMap, null);
			if(confList != null && confList.size() > 0)
			{
				for (AgwParamConf conf : confList)
				{
					AgwParamValue agwParam = new AgwParamValue();
					agwParam.setGwNo(gwNo);
					agwParam.setGwType(conf.getGwType());
					agwParam.setParamItem(conf.getParamItem());
					agwParam.setParamValue(conf.getDefaultValue());
					paramList.add(agwParam);
				}
			}
			baseBiz.addList(AgwParamValue.class, paramList);
		}
		catch (Exception e)
		{
			// writer.print("error");
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "通过网关编号获取通道配置信息异常！"));
		}
	}

	private void setLog(HttpServletRequest request, String opModule, String opContent, String opType)
	{
		try
		{
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj != null)
			{
				LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId() + "", loginSysuser.getUserName(), opContent, opType);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, opModule + opType + opContent + "日志写入异常"));
		}
	}

	/**
	 * 检查备用后端账号网关数
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void checkBakNum(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		int count = -1;
		try
		{
			String uid = request.getParameter("uid");
			LinkedHashMap<String, String> condMap = new LinkedHashMap<String, String>();
			condMap.put("ptaccuid", uid);
			condMap.put("gweight&<>", "0");
			List<GWCluSpBind> gwCluSpBinds = new BaseBiz().getByCondition(GWCluSpBind.class, condMap, null);
			count = gwCluSpBinds.size();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询对应主后端账号的备用网关数目异常！");
		}
		out.print(count);
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
	public ParamsEncryptOrDecrypt getParamsEncryptOrDecrypt(HttpServletRequest request)
	{
		try
		{
			ParamsEncryptOrDecrypt encryptOrDecrypt = null;
			// 加密对象
			Object encrypOrDecrypttobject = request.getSession(false).getAttribute("decryptObj");
			// 加密对象不为空
			if(encrypOrDecrypttobject != null)
			{
				// 强转类型
				encryptOrDecrypt = (ParamsEncryptOrDecrypt) encrypOrDecrypttobject;
			}
			else
			{
				EmpExecutionContext.error("通道账户管理,从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "通道账户管理,从session获取加密对象异常。");
			return null;
		}
	}

	/**
	 * 获取运营商余额查询备用URL
	 * 
	 * @description
	 * @param request
	 * @param response
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2017-1-10 下午03:04:41
	 */
	public void getBalanceBakUrl(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter out = null;
		String balanceBakRrl = "";
		try
		{
			response.setContentType("text/html");
			out = response.getWriter();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("globalKey", "BALANCEBAKURL");
			List<LfGlobalVariable> globalVariableList = new BaseBiz().getByCondition(LfGlobalVariable.class, conditionMap, null);
			if(globalVariableList != null && globalVariableList.size() > 0)
			{
				balanceBakRrl = globalVariableList.get(0).getGlobalStrValue();
			}
			out.print(balanceBakRrl);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "通道账户管理,获取运营商余额查询备用URL失败。");
			if(out!=null){
				out.print("");
			}
		}
	}
}
