package com.montnets.emp.client.servlet;

import com.montnets.emp.client.biz.AddrBookBiz;
import com.montnets.emp.client.biz.ClientAddrBookBiz;
import com.montnets.emp.client.biz.EnterpriseBiz;
import com.montnets.emp.client.biz.ExcelToolClient;
import com.montnets.emp.client.dao.DateFormatter;
import com.montnets.emp.client.dao.GenericLfClientVoDAO;
import com.montnets.emp.client.vo.LfClientVo;
import com.montnets.emp.client.vo.LfEnterpriseVo;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.birthwish.LfBirthdayMember;
import com.montnets.emp.entity.client.*;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.*;
import jxl.*;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 *
 * @project emp
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime
 * @description
 */
@SuppressWarnings("serial")
public class cli_addrBookSvt extends BaseServlet
{

	private final ClientAddrBookBiz clientBiz = new ClientAddrBookBiz();

	private final EnterpriseBiz eb = new EnterpriseBiz();

	private final GlobalVariableBiz globalBiz = GlobalVariableBiz.getInstance();

	private final AddrBookBiz addrBookBiz = new AddrBookBiz();

	private final String line = StaticValue.systemProperty
			.getProperty(StaticValue.LINE_SEPARATOR);

	// 模块
	private final String opModule = StaticValue.ADDBR_MANAGER;

	// 操作员姓名
	private final String opSper = StaticValue.OPSPER;

	private final  String empRoot = "client";

	private final  String excelPath = "client/climan/file/";

	private final  BaseBiz baseBiz = new BaseBiz();

	private final  SuperOpLog spLog = new SuperOpLog();

	private final  PhoneUtil phoneUtil = new PhoneUtil();

	private final  SmsBiz biz=new SmsBiz();
	//判断是否加载APP模块
	private final  boolean ismodule=biz.isWyModule("21");
//	private boolean ismodule=true;
	/**
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{

			PageInfo pageInfo=new PageInfo();

			String skip=request.getParameter("skip");
			pageSet(pageInfo, request);

			// 当前登录操作员id
//			Long lguserid = Long.valueOf(request.getParameter("lguserid"));

			//String lguserObj=request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserObj = SysuserUtil.strLguserid(request);

			Long lguserid = 0l;
//			if(lguserObj!=null&&!"undefined".equals(lguserObj)&&!"".equals(lguserObj)){
//				lguserid = Long.valueOf(request.getParameter("lguserid"));
//			}else{
//				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
//				if(loginSysuserObj!=null){
//					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
//					lguserid=loginSysuser.getUserId();
//				}
//			}
			//漏洞修复 session里获取操作员信息
			lguserid = SysuserUtil.longLguserid(request);


			// 当前登录企业
//			String lgcorpcode = request.getParameter("lgcorpcode");
			// 防止请求中修改用户信息达到修改用户的目的
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String lgcorpcode = sysuser.getCorpCode();
			// 获取当前操作员有权限的机构
			List<LfEnterpriseVo> enterprisesList = eb.getEnterpriseVos(0,
					lguserid, lgcorpcode);

			// 判断当前登录操作员是否有客户通讯录权限
			if (enterprisesList == null || enterprisesList.size() == 0)
			{
				request.getRequestDispatcher(
						this.empRoot + "/climan/cli_noPermission.jsp")
						.forward(request, response);
				return;
			}
			//增加是否加载模块的判断 仅限于客户通讯录
			request.setAttribute("bookType", "client");
			request.setAttribute("ismodule", ismodule);
			request.getSession(false).setAttribute("skip", skip);

			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lguserid", lguserid);
			LfClientVo bookInfo = new LfClientVo();
			if("ture".equals(skip)){
				bookInfo=(LfClientVo)request.getSession(false).getAttribute("clientInfo");
			}
		    request.getSession(false).setAttribute("clientInfo", bookInfo);

			request.getRequestDispatcher(
					this.empRoot + "/climan/cli_clientAddrBook.jsp")
					.forward(request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"客户通讯录页面跳转出现异常！");
		}
	}

	/**
	 * @param request
	 * @param response
	 */
	public void getTable(HttpServletRequest request,
			HttpServletResponse response)
	{
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		try
		{
			LfClientVo bookInfo = new LfClientVo();
			PageInfo pageInfo=new PageInfo();
			boolean isFirstEnter = pageSet(pageInfo, request);
			//****加密对象*****
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			String depId = request.getParameter("depId");
			String skip=(String)request.getSession(false).getAttribute("skip");
			if("ture".equals(skip)){
				bookInfo=(LfClientVo)request.getSession(false).getAttribute("clientInfo");

				pageInfo=(PageInfo)request.getSession(false).getAttribute("pageIn");
				request.getSession(false).setAttribute("skip","false");
			}

			// 查询条件
			if (!isFirstEnter)
			{
				// 姓名
				bookInfo.setName(request.getParameter("name"));
				// 手机
				bookInfo.setMobile(request.getParameter("mobile"));
				//签约用户
				String iscontract = request.getParameter("iscontract");
				if(null!=iscontract && !"".equals(iscontract)){
					bookInfo.setIsContract(Integer.valueOf(iscontract));
				}
			}

			if (depId != null && depId.length() != 0)
			{
				bookInfo.setDepId(Long.parseLong(depId));
			}
			String appacount = request.getParameter("appacount");
			String appstatue = request.getParameter("appstatue");
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("appacount", appacount);
			conditionMap.put("appstatue", appstatue);

			// 当前登录操作员id
			Long lguserid =0l;
			// 当前登录企业 防止请求中修改用户信息达到修改用户的目的
//			String lgcorpcode = request.getParameter("lgcorpcode");
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String lgcorpcode = sysuser.getCorpCode();
			lguserid=sysuser.getUserId();

//			if(request.getParameter("lguserid")!=null){
//				 lguserid = Long.valueOf(request.getParameter("lguserid"));
//			}else{
//				//如果界面上无法获得就从缓存中取出
//				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
//				if(loginSysuserObj!=null){
//					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
//					lguserid=loginSysuser.getUserId();
//				}
//			}

			// 查询客户记录
			List<DynaBean> bookInfoList=null;
			//如果加载了APP模块，则使用APP查询方式
			if(ismodule){
			 bookInfoList = clientBiz.getClientVocd(lguserid,
					bookInfo, lgcorpcode, pageInfo,conditionMap);
			}else {
				 bookInfoList = clientBiz.getClientVocd(lguserid,
							bookInfo, lgcorpcode, pageInfo);
			}
			HttpSession session = request.getSession(false);
			session.setAttribute("bookInfoList", bookInfoList);
			// 查询条件，存到session，导出excel时用
			session.setAttribute("clientInfo", bookInfo);
			session.setAttribute("pageIn", pageInfo);
			request.setAttribute("ismodule", ismodule);

			request.setAttribute("bookType", "client");
			request.setAttribute("pageInfo", pageInfo);
			HashMap<String,String> encryptmap =new HashMap<String,String>();
			//未知机构客户设置标识
			if(bookInfoList != null && bookInfoList.size()>0)
			{
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					//加密处理
					String client_id="";
						for(DynaBean book:bookInfoList)
						{
							client_id = encryptOrDecrypt.encrypt(String.valueOf(book.get("client_id")));
							//通过实际值，加密值
							encryptmap.put(book.get("client_id").toString(), client_id);
						}
					}
				else
				{
					EmpExecutionContext.error("查询操作员列表，从session中获取加密对象为空！");
				}
				//未知机构客户编码
				StringBuffer unknowDepClient = new StringBuffer(",");
				if(ismodule)
				{
					for(DynaBean book:bookInfoList)
					{
						String dep_id = book.get("dep_id")==null?"":book.get("dep_id").toString();
						if(dep_id.indexOf("-10") > -1)
						{
							unknowDepClient.append(book.get("client_id").toString()).append(",");
						}
					}
				}
				request.setAttribute("encryptmap", encryptmap);
				request.setAttribute("unknowDepClient", unknowDepClient.toString());
			}
			//增加查询日志
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"毫秒，数量："+pageInfo.getTotalRec();
				opSucLog(request, "客户通讯录查询", opContent, "GET");
			}
			request.getRequestDispatcher(this.empRoot + "/climan/cli_bookTable.jsp").forward(request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取客户列表出现异常！");
		}
	}

	/**
	 * @description 同步
	 * @param request
	 * @param response
	 */
	public void sync(HttpServletRequest request, HttpServletResponse response)
	{

		PrintWriter out = null;

		String opType = request.getParameter("opType");

		try
		{
			out = response.getWriter();
			if ("1".equals(opType))
			{
				Integer result=clientBiz.addEmployeeByProcedure(null, null, null);
				out.print(result);
				String ret=result>-1?"成功":"失败";
				//增加操作日志
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("模块名称：客户通讯录，企业："+loginSysuser.getCorpCode()+"，"
						+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
						+"同步员工信息"+ret+"。");
					}
			} else if ("2".equals(opType))
			{
				ClientAddrBookBiz clientBiz = new ClientAddrBookBiz();
				Integer result=clientBiz.addClientByProcedure(null, null, null);
				out.print(result);
				String ret=result>-1?"成功":"失败";
					//增加操作日志
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("模块名称：客户通讯录，企业："+loginSysuser.getCorpCode()+"，"
						+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
						+"同步客户机构信息"+ret+"。");
					}
			} else if ("3".equals(opType))
			{
				AddrBookBiz addrBookBiz = new AddrBookBiz();
				Integer result=addrBookBiz.addEnterpriseProc(null, null, 2);
				out.print(result);
				String ret=result>-1?"成功":"失败";
					//增加操作日志
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("模块名称：客户通讯录，企业："+loginSysuser.getCorpCode()+"，"
						+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
						+"同步机构相关信息"+ret+"。");
					}
			} else if ("4".equals(opType))
			{
				AddrBookBiz addrBookBiz = new AddrBookBiz();
				Integer result=addrBookBiz.addEnterpriseProc(null, null, 3);
				out.print(result);
				String ret=result>-1?"成功":"失败";
					//增加操作日志
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("模块名称：客户通讯录，企业："+loginSysuser.getCorpCode()+"，"
						+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
						+"同步机构相关信息"+ret+"。");
					}
			}

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"同步客户列表出现异常！");
			if(out != null){
				out.print(-1);
			}
		}
	}


	/**
	 *   单个新增客户
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@ SuppressWarnings("unchecked")
	public void addBookcd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

		try {
//			String lgcorpcode = request.getParameter("lgcorpcode");
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String lgcorpcode = sysuser.getCorpCode();
			List<LfCustField> list1 = getdatalist(lgcorpcode);
			Map<LfCustField, List<LfCustFieldValue>> map1 = new LinkedHashMap<LfCustField, List<LfCustFieldValue>>();
			Map<LfCustField, List<LfCustFieldValue>> map2 = new LinkedHashMap<LfCustField, List<LfCustFieldValue>>();
			for (int i = 0; i < list1.size(); i++)
			{
				LfCustField lf = list1.get(i);
				// 判断是单选属性
				if (lf.getV_type().equals("0"))
				{
					List<LfCustFieldValue> list2 = getValueVo(lf.getId());
					map1.put(lf, list2);
				}
				// 判断是多选属性
				else if (lf.getV_type().equals("1"))
				{
					List<LfCustFieldValue> list3 = getValueVo(lf.getId());
					map2.put(lf, list3);
				}
			}
			// 将单选多选 发送到页面显示
			request.setAttribute("map1", map1);
			request.setAttribute("map2", map2);
			LfClientMultiPro client = new LfClientMultiPro();
			String cName = request.getParameter("cName");//客户姓名
			String mobile = request.getParameter("mobile");
			String depId = request.getParameter("depId");//机构编号
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("name",cName);
			conditionMap.put("mobile",mobile);
			conditionMap.put("corpCode",lgcorpcode);
			List<LfClientMultiPro> clientList = baseBiz.getByCondition(LfClientMultiPro.class, conditionMap, null);
			boolean isFlag = false;  //是新增还是修改
			HashSet<String> depidset = new HashSet<String>();
			String[] deps = depId.substring(0, depId.length()-1).split(",");
			//需要新增加的机构
			List<String> addDep = new ArrayList<String>();
			if(clientList != null && clientList.size()>0){
				client = clientList.get(0);
				isFlag = true;
				conditionMap.clear();
				conditionMap.put("clientId", String.valueOf(client.getClientId()));
				List<LfClientDepSp> depSp = baseBiz.getByCondition(LfClientDepSp.class, conditionMap, null);
				if(depSp != null && depSp.size()>0){
					for(LfClientDepSp sp:depSp){
						depidset.add(String.valueOf(sp.getDepId()));
					}
				}
			}
			//APP客户号
			String appClientId = "-1";
			//新增客户手机号码在未知机构中存在
			if(!isFlag)
			{
				appClientId = clientBiz.getUnknowDepAppClientId(lgcorpcode, mobile);
				if(appClientId != null && !"-1".equals(appClientId))
				{
					isFlag = true;
					//未知机构编码
					depidset.add("-10");
					//APP用户clientid
					client.setClientId(Long.parseLong(appClientId));
				}
			}

			if(deps != null && deps.length>0){
				for(String a:deps){
					if(isEffectValue(a)){
						if(isFlag){
							if(!depidset.contains(a)){
								addDep.add(a);
							}
						}else{
							addDep.add(a);
						}
					}
				}
			}
			if(addDep != null && addDep.size()>0){
					if (list1.size() > 0)
					{
						for (int m = 0; m < list1.size(); m++)
						{
							LfCustField lf = list1.get(m);
							String s = "F"+ lf.getField_Ref().substring(1).toLowerCase();
							// 利用反射将request中获得的值添加到客户lc中
							Class clazz = Class.forName("com.montnets.emp.entity.client.LfClientMultiPro");
							Method mf = clazz.getDeclaredMethod("set" + s,String.class);
							if (lf.getV_type()!= null && "0".equals(lf.getV_type()))
							{
								if (request.getParameter(lf.getField_Name()) != null && !"".equals(request.getParameter(lf.getField_Name()))){
									mf.invoke(client, request.getParameter(lf.getField_Name()));
								}else if(!isFlag){
									//如果是新增没值，则设空
									mf.invoke(client,"");
								}
							} else if (lf.getV_type() != null && "1".equals(lf.getV_type()))
							{
								if (request.getParameterValues(lf.getField_Name()) != null && !"".equals(request.getParameter(lf.getField_Name())))
								{
									String[] aa = request.getParameterValues(lf.getField_Name());
									StringBuffer buf = new StringBuffer();
									for (int f = 0; f < aa.length; f++){
										buf.append(aa[f]);
										buf.append(";");
									}
									mf.invoke(client, buf.toString().substring(0,buf.toString().lastIndexOf(";")));
								} else if(!isFlag){
									mf.invoke(client, "");
								}
							}
						}
					}
					client.setCorpCode(lgcorpcode);
					client.setName(cName);
					// 手机
					client.setMobile(mobile);
					//设置客户信息
					this.setClientInfo(request, client, isFlag);
					String returnmsg = clientBiz.isAddClient(isFlag,addDep, client);
					if("samePerson".equals(returnmsg)){
						String str =cName+" ["+mobile+"] 加入到所选机构成功！";
						//增加操作日志
						Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
						if(loginSysuserObj!=null){
							LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
							EmpExecutionContext.info("客户通讯录", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "新增客户已经存在。[客户名称，手机号码]（"+cName+"，"+mobile+"）", "ADD");
						}
						request.setAttribute("result", "isexist");
						request.setAttribute("isexiststr",str);
						//如果从未知机构修改,则删除用户与未知机构的对应关系
						if(appClientId != null && !"-1".equals(appClientId))
						{
							//删除用户与未知机构的对应关系
							conditionMap.clear();
							conditionMap.put("clientId", appClientId);
							conditionMap.put("depId", "-10");
							baseBiz.deleteByCondition(LfClientDepSp.class, conditionMap);
						}
					}else if("success".equals(returnmsg)){
						//增加操作日志
						Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
						if(loginSysuserObj!=null){
							LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
							EmpExecutionContext.info("客户通讯录", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "新增成功。[客户名称，手机号码]（"+cName+"，"+mobile+"）", "ADD");
						}
						request.setAttribute("result", "true");
					}else{
						//增加操作日志
						Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
						if(loginSysuserObj!=null){
							LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
							EmpExecutionContext.info("客户通讯录", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "没有新增。[客户名称，手机号码]（"+cName+"，"+mobile+"）", "ADD");
						}
						request.setAttribute("result", "false");
					}
			}else{
				request.setAttribute("result", "isexist");
				String str = cName+" ["+mobile+"] 已存在所选机构中，不允许重复添加！";
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("客户通讯录", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "已存在所选机构中，不允许重复添加。[客户名称，手机号码]（"+cName+"，"+mobile+"）", "ADD");
				}
				request.setAttribute("isexiststr",str);
			}
		} catch (Exception e) {
			request.setAttribute("result", "false");
			EmpExecutionContext.error(e, "新增客户失败！");
		}finally{
			request.getRequestDispatcher(this.empRoot + "/climan/cli_addClient.jsp").forward(request,
					response);
		}
	}

	/**
	 *  判断是否为空
	 * @param temp	值
	 * @return
	 */
	public boolean isEffectValue(String temp){
		if(temp == null || "".equals(temp)){
			return false;
		}
		return true;
	}


	/**
	 *   修改客户
	 * @param request
	 * @param response
	 */
	@ SuppressWarnings("unchecked")
	public void editBookcd(HttpServletRequest request,HttpServletResponse response){
		try {
//			String lgcorpcode = request.getParameter("lgcorpcode");
			// 当前登录企业 防止请求中修改用户信息达到修改用户的目的
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String lgcorpcode = sysuser.getCorpCode();
			List<LfCustField> list1 = getdatalist(lgcorpcode);
			Map<LfCustField, List<LfCustFieldValue>> map1 = new LinkedHashMap<LfCustField, List<LfCustFieldValue>>();
			Map<LfCustField, List<LfCustFieldValue>> map2 = new LinkedHashMap<LfCustField, List<LfCustFieldValue>>();
			for (int i = 0; i < list1.size(); i++)
			{
				LfCustField lf = list1.get(i);
				// 判断是否是单选属性
				if (lf.getV_type() != null && "0".equals(lf.getV_type())){
					List<LfCustFieldValue> list2 = getValueVo(lf.getId());
					map1.put(lf, list2);
				}else if (lf.getV_type() != null  && "1".equals(lf.getV_type())){
					// 判断是否多选属性
					List<LfCustFieldValue> list3 = getValueVo(lf.getId());
					map2.put(lf, list3);
				}
			}
			String bookId = request.getParameter("bookId");
			LfClient client = new LfClient();
			//StringBuffer aas = new StringBuffer();
			String depId = request.getParameter("depId");//机构编号
			String cName = request.getParameter("cName");//客户姓名
			String mobile = request.getParameter("mobile");
			String clientCode = request.getParameter("clientCode");//客户号
			// 当前登录操作员id
			//Long lguserid = Long.valueOf(request.getParameter("lguserid"));
			//漏洞修复 session里获取操作员信息
			Long  lguserid = SysuserUtil.longLguserid(request);

			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("name",cName);
			conditionMap.put("mobile",mobile);
			conditionMap.put("corpCode",lgcorpcode);
			conditionMap.put("clientId&<>",String.valueOf(bookId));
			List<LfClient> clientList = baseBiz.getByCondition(LfClient.class, conditionMap, null);
			boolean isFlag = false;  //修改客户 还是覆盖客户记录
			HashSet<String> depidset = new HashSet<String>();
			String[] deps = depId.substring(0, depId.length()-1).split(",");
			//需要新增加的机构
			List<String> addDep = new ArrayList<String>();
			LfClient updateClient = baseBiz.getById(LfClient.class, bookId);

			String prePhone="";
			String preName="";
			if(updateClient!=null){
				prePhone=updateClient.getMobile();
				preName=updateClient.getName();
			}
			//该操作员所拥有的客户机构权限
			String userclientconns = clientBiz.getUserClientConnDepId(lguserid);
			conditionMap.clear();
			conditionMap.put("depId&not in",userclientconns);
			conditionMap.put("clientId",String.valueOf(bookId));
			List<LfClientDepSp> clientDepsp = baseBiz.getByCondition(LfClientDepSp.class, conditionMap, null);
			if(clientDepsp != null && clientDepsp.size()>0){
				for(LfClientDepSp sp:clientDepsp){
					addDep.add(String.valueOf(sp.getDepId()));
				}
			}
			if(clientList != null && clientList.size()>0){
				//在数据库上做修改
				client = clientList.get(0);
				isFlag = true;
				conditionMap.clear();
				conditionMap.put("clientId", String.valueOf(client.getClientId()));
				List<LfClientDepSp> depSp = baseBiz.getByCondition(LfClientDepSp.class, conditionMap, null);
				if(depSp != null && depSp.size()>0){
					for(LfClientDepSp sp:depSp){
						depidset.add(String.valueOf(sp.getDepId()));
					}
				}
			}else{
				client = updateClient;
			}
			//这里判断机构的ID情况
			if(deps != null && deps.length>0){
				for(String a:deps){
					if(isEffectValue(a)){
						if(isFlag){
							if(!depidset.contains(a)){
								addDep.add(a);
							}
						}else{
							addDep.add(a);
						}
					}
				}
			}
			if (list1.size() > 0)
			{
				for (int m = 0; m < list1.size(); m++)
				{
					LfCustField lf = list1.get(m);
					String s = "F"+ lf.getField_Ref().substring(1).toLowerCase();
					Class clazz = Class.forName("com.montnets.emp.entity.client.LfClient");
					Method mf = clazz.getDeclaredMethod("set" + s,String.class);
					if (lf.getV_type() != null && "0".equals(lf.getV_type()))
					{
						// 如果单选自定义属性不为空
						if (request.getParameter(lf.getField_Name()) != null
								&& !"".equals(request.getParameter(lf.getField_Name())))
						{
							// 通过映射将自定义属性赋值
							mf.invoke(client, request.getParameter(lf.getField_Name()));
							//aas.append(request.getParameter(lf.getField_Name()));
						}else if(!isFlag){
							mf.invoke(client, "");
							//aas.append(request.getParameter(lf.getField_Name()));
						}
					}else if (lf.getV_type() != null && "1".equals(lf.getV_type())){
						 // 多选
						if (request.getParameterValues(lf.getField_Name()) != null && !"".equals(request.getParameter(lf.getField_Name())))
						{
							String[] aa = request.getParameterValues(lf.getField_Name());
							StringBuffer buf = new StringBuffer();
							for (int f = 0; f < aa.length; f++){
								buf.append(aa[f]);
								buf.append(";");
							}
							mf.invoke(client, buf.toString().substring(0,buf.toString().lastIndexOf(";")));
						}else if(!isFlag){
							mf.invoke(client, "");
						}
					}
				}
			}
			String sex = request.getParameter("sex");	//性别
			String birth = request.getParameter("birth");//生日
			String job = request.getParameter("job");
			String pro = request.getParameter("pro");
			String eName = request.getParameter("eName");
			String qq = request.getParameter("qq");
			String area = request.getParameter("area");
			String msn = request.getParameter("msn");
			String comm = request.getParameter("comm");//客户描述
			String EMail = request.getParameter("EMail");
			String oph = request.getParameter("oph");

			client.setCorpCode(lgcorpcode);
			client.setName(cName);
			// 手机
			client.setMobile(mobile);
			// 性别
			client.setSex(Integer.parseInt(sex));
			//客户号

			if(isFlag){
				//覆盖
				if(isEffectValue(clientCode)){
					client.setClientCode(clientCode.toUpperCase());
				}
				//如果操作员没有设值 客户号 ，则 将客户的guid设值到客户号
				if(client.getClientCode() == null || "".equals(client.getClientCode())){
					client.setClientCode(String.valueOf(client.getGuId()));
				}
				// 职务
				if(isEffectValue(job)){
					client.setJob(job);
				}
				//行业
				if(isEffectValue(pro)){
					client.setProfession(pro);
				}
				//经理
				if(isEffectValue(eName)){
					client.setEname(eName);
				}
				// qq
				if(isEffectValue(qq)){
					client.setQq(qq);
				}
				// 地区
				if(isEffectValue(area)){
					client.setArea(area);
				}
				// msn
				if(isEffectValue(msn)){
					client.setMsn(msn);
				}
				// 描述
				if(isEffectValue(comm)){
					client.setComments(comm);
				}
				if(isEffectValue(EMail)){
					client.setEMail(EMail);
				}
				if(isEffectValue(oph)){
					client.setOph(oph);
				}
				if(isEffectValue(birth)){
					client.setBirthday(DateFormatter.swicthSqltring(birth +" 00:00:00"));
				}
			}else{
				//修改
				if(isEffectValue(clientCode)){
					client.setClientCode(clientCode.toUpperCase());
				}else{
					client.setClientCode(String.valueOf(client.getGuId()));
				}
				client.setJob(job);
				client.setProfession(pro);
				client.setEname(eName);
				client.setQq(qq);
				client.setArea(area);
				client.setMsn(msn);
				client.setComments(comm);
				client.setEMail(EMail);
				client.setOph(oph);
				if(isEffectValue(birth)){
					client.setBirthday(DateFormatter.swicthSqltring(birth +" 00:00:00"));
				}
			}


			client.setUserId(lguserid);
			String returnmgs ="";
			returnmgs = clientBiz.isEditClient(updateClient,isFlag,addDep, client,lguserid);
			if("success".equals(returnmgs)){
				request.setAttribute("addresult", "true");
				if(depId.length() > 4 && depId.indexOf("-10") > -1)
				{
					//删除用户与未知机构的对应关系
					conditionMap.clear();
					conditionMap.put("clientId", bookId);
					conditionMap.put("depId", "-10");
					baseBiz.deleteByCondition(LfClientDepSp.class, conditionMap);
				}
				try{
					if(!isFlag && !isEffectValue(birth)){
						conditionMap.clear();
						conditionMap.put("clientId",String.valueOf(client.getClientId()));
						conditionMap.put("corpCode",client.getCorpCode());
						LinkedHashMap<String,String> objectMap = new LinkedHashMap<String, String>();
						objectMap.put("birthday",null);
						baseBiz.update(LfClient.class, objectMap, conditionMap);
					}
				}catch (Exception e) {
					EmpExecutionContext.error(e,"更新客户生日出现异常！");
				}
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("客户通讯录", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "修改客户成功。[客户名称，手机号码，客户ID]（"+preName+"，"+prePhone+","+bookId+"）->（"+cName+"，"+mobile+","+bookId+"）", "UPDATE");
				}
			}else{
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("客户通讯录", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "修改客户失败。[客户名称，手机号码，客户ID]（"+preName+"，"+prePhone+","+bookId+"）->（"+cName+"，"+mobile+","+bookId+"）", "UPDATE");
				}
				request.setAttribute("addresult", "false");
			}
			String[] depname = clientBiz.getDepName(String.valueOf(client.getClientId()),lguserid);
			depname[1] = depname[1]+",";
			request.setAttribute("map1", map1);
			request.setAttribute("map2", map2);
			request.setAttribute("cleid", String.valueOf(client.getClientId()));
			request.setAttribute("client", client);
			request.setAttribute("depname", depname);
			request.setAttribute("lguserid", lguserid);
			request.getRequestDispatcher(this.empRoot + "/climan/cli_editClient.jsp").forward(request, response);
		} catch (Exception e) {
			request.setAttribute("addresult", "false");
			EmpExecutionContext.error(e, "修改客户失败！");
		}
	}



	public String getWebRoot()
	{
		String realUrl = getClass().getProtectionDomain().getCodeSource()
				.getLocation().getPath();
		String newUrl = "";
		if (realUrl.contains("/WEB-INF/"))
		{
			newUrl = realUrl.substring(0, realUrl.lastIndexOf("WEB-INF/"));
		}
		realUrl = newUrl.replace("%20", " ");// //此路径不兼容jboss
		return realUrl;
	}

	public String[] getClientUploadUrl(int id, Date time)
	{
		//String uploadPath = StaticValue.CLIENT_UPLOAD;
		String uploadPath = excelPath + "upload/";
		//String uploadPath ="yhgl/client/file/upload/";
		// 存放路径的数组
		String[] url = new String[5];
		String saveName = "1_" + id + "_"
				+ (new SimpleDateFormat("yyyyMMddHHmmssS")).format(time)
				+ ".txt";
		String logicUrl;
		String physicsUrl = this.getWebRoot();
		physicsUrl = physicsUrl + uploadPath + saveName;
		logicUrl = uploadPath + saveName;
		url[0] = physicsUrl;
		url[1] = logicUrl;
		return url;
	}

	/**
	 * @description 批量导入客户
	 * @param request
	 * @param response
	 */
	@ SuppressWarnings("unchecked")
	public void uploadBook(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		String depId = "";
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> fileList = null;
		// 上传文件
		try
		{
			fileList = upload.parseRequest(request);
		} catch (FileUploadException e){
			EmpExecutionContext.error(e,"批量导入客户解析表单出现异常！");
		}
		LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		Iterator<FileItem> it = fileList.iterator();
		// 循环遍历request中的每一个fileItem
		FileItem fileItem = null;
		// 临时的，用以保存xls的fileItem
		FileItem fileItem2 = null;
		while (it.hasNext())
		{
			fileItem = (FileItem) it.next();
			String fileName = fileItem.getFieldName();
			// 获取机构id
			if (fileName.equals("depId"))
			{
				try
				{
					depId = fileItem.getString("UTF-8").toString();
					// 通过机构id获取机构下面的客户
				} catch (UnsupportedEncodingException e)
				{
					EmpExecutionContext.error(e,"获取客户机构ID失败！");
					request.setAttribute("result", "error");
					break;
				}
			} else if (!fileItem.isFormField() && fileItem.getName().length() > 0){
				fileItem2 = fileItem;
			}
		}
		if(depId != null && !"".equals(depId)){
			//增加操作日志
			String[] deps = depId.substring(0, depId.length()-1).split(",");
			String fileCurName = fileItem2 == null? "" : fileItem2.getName();
			String fileType = fileCurName.substring(fileCurName.lastIndexOf("."));
			EmpExecutionContext.info("客户通讯录导入，导入文件名："+fileCurName+"，userId:"+sysuser.getUserId()+"，corpCode:"+sysuser.getCorpCode());
			//处理excel2003
			if (fileType.equals(".xls")){
				this.parseClientXls(request, fileItem2, deps);
			}else if(fileType.equals(".xlsx")){
			//处理excel2007
				this.parseXSSFXls(request, fileItem2, deps);
			}else{
			//如果都不是则页面提示异常
				request.setAttribute("result", "false");
			}

		}
		
		request.getRequestDispatcher(this.empRoot + "/climan/cli_addClient.jsp").forward(request, response);
	}

	/**
	 *  处理客户2007版本的xlsx上传文件
	 *  6.1版本新增功能
	 * @param request	请求
	 * @param fileItem	页面对象
	 * @param deps	所选机构ID数组
	 */
	@ SuppressWarnings("unchecked")
	private void parseXSSFXls(HttpServletRequest request, FileItem fileItem, String[] deps) {
		long start = System.currentTimeMillis();
		String oppType = StaticValue.ADD;
		String opContent = "批量导入客户";
		GenericLfClientVoDAO clientVoDAO = new GenericLfClientVoDAO();
		SuperOpLog spLog = new SuperOpLog();
		TxtFileUtil txtFileUtil = new TxtFileUtil();
		HttpSession session = request.getSession();
		String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
		// 导入返回错误 内容显示资源
		String di = MessageUtils.extractMessage("client", "client_khtxlgl_txl_upload_1", request);
		String hang = MessageUtils.extractMessage("client", "client_khtxlgl_txl_upload_2", request);
		// 姓名为空或超过32位字符
		String xmc32 = MessageUtils.extractMessage("client", "client_khtxlgl_txl_upload_3", request);
		String sjhwk = MessageUtils.extractMessage("client", "client_khtxlgl_txl_upload_4", request);
		String sjhff = MessageUtils.extractMessage("client", "client_khtxlgl_txl_upload_5", request);
		//姓名和手机号码存在重复
		String xmsjcf = MessageUtils.extractMessage("client", "client_khtxlgl_txl_upload_6", request);

		String man = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_man", request);
		String woman = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_woman", request);

		List<LfCustField> lfcs = null;
		// 插入条数
		int insertCount = 0;
		// 插入结果
		String insertResult;
		String lgcorpcode = request.getParameter("lgcorpcode");

		String opUser = "";
		Set<String> addSet = new HashSet<String>();
		Set<String> dbSet = new HashSet<String>();
		//app客户未知机构手机号 clientid集合
		Map<Long,Long> unMap;
		//从未知机构转入的手机号集合
		Set<String> upSet = new HashSet<String>();
		//转入机构的未知机构客户集合
		List<Long> unlist = new ArrayList<Long>();
		String[] cells;
		try {
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			LinkedHashMap<String, String> dbMap = clientBiz.getClientMsg(lgcorpcode, dbSet);
			unMap = clientBiz.getUndepClientMsg(lgcorpcode);
			opUser = sysuser.getUserName();
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			request.setAttribute("lguserid",lguserid);
			request.setAttribute("company", lgcorpcode);
			// 生成文件路径用到
			Date time = Calendar.getInstance().getTime();
			// 包含文件的物理路径和相对路径
			String[] url = this.getClientUploadUrl(Integer.parseInt(lguserid), time);
			lfcs = getdatalist(lgcorpcode);
			// 单选项和值的map
			Map<LfCustField, Map<String, String>> map1 = new LinkedHashMap<LfCustField, Map<String, String>>();
			// 多选项和值的map
			// 屬性name和屬性對象的map
			Map<String, LfCustField> map3 = new LinkedHashMap<String, LfCustField>();
			LfCustField lf = null;
			Map<String, String> valueIdMap;
			for (int i = 0; i < lfcs.size(); i++) {
				LfCustField lfc = lfcs.get(i);
				lf = lfc;
				valueIdMap = clientVoDAO.findCustFieldValueList(lf.getId());
				map1.put(lf, valueIdMap);
				map3.put(lf.getField_Name(), lf);
			}
			String[] haoduan = new WgMsgConfigBiz().getHaoduan();
			// 获得excel工作簿对象
            //获取一个存放临时文件的目录
        	String uploadPath = StaticValue.FILEDIRNAME;
			String temp = new TxtFileUtil().getWebRoot() + uploadPath;

			Excel2007VO excel = new Excel2007Reader().fileParset(temp, fileItem.getInputStream());

			//等于1表示标题
			if(excel==null||excel.getFirstline()==null||"".equals(excel.getFirstline())){
				request.setAttribute("result", "emptype");
				return;
			}

			String firstline=excel.getFirstline();
			String[] titleList=firstline.split(",");
			//循环获取第一行的标题列表,第一个是空的
			int cellCount = titleList.length-1;
			// 判断模板第一行长度是否正确
			if (cellCount != (map1.size() + 14)){
				String realUrl = txtFileUtil.getWebRoot();
				String path = realUrl + "/";
				path += StaticValue.FILE_UPLOAD_PATH;
				String filepath = "/" + lgcorpcode + "clientexam_"+langName+".xlsx";
				String fullFilePath = path + filepath;
				writeExcel(lfcs, fullFilePath,request);
				request.setAttribute("result", "emptype");
				return;
			}
			//由于2007版本如果第一行是空的，取的是第二行的数据
			if(iscorr(titleList,request)){
				request.setAttribute("result", "emptype");
				return;
			}
			//客户属性个数,5为0-5个;6为大于5个
			int proCount = 6;

			//客户属性数量为0-5个
			if(cellCount < 20 ) {
				proCount = 5;
			}
			/**********客户属性数量为0-5个***********/
			//新增客户信息
			List<LfClient5Pro> addClient5ProList = new ArrayList<LfClient5Pro>();
			//修改客户信息
			List<LfClient5Pro> modiClient5ProList = new ArrayList<LfClient5Pro>();
			LfClient5Pro client5Pro = null;
			// 利用反射将request中获得的值添加到客户中
			Class clazz5Pro = Class.forName("com.montnets.emp.entity.client.LfClient5Pro");

			/**********客户属性数量大于5个***********/
			//新增客户信息
			List<LfClientMultiPro> addClientList = new ArrayList<LfClientMultiPro>();
			//修改客户信息
			List<LfClientMultiPro> modiClientList = new ArrayList<LfClientMultiPro>();
			LfClientMultiPro client = null;
			// 利用反射将request中获得的值添加到客户中
			Class clazz = Class.forName("com.montnets.emp.entity.client.LfClientMultiPro");

			// 减去标题
			int lastrow = excel.getCurRow();
			Long rowLength = (long) (lastrow - 1);
			Long curValue = globalBiz.getValueByKey("guid", rowLength);
			int repeatNum = 0;
			StringBuilder contentSb = new StringBuilder();
			// ---------这些变量都是在循环里面的解析第十四个单元格以后使用的，现在拿到外面避免重复声明
			Map<String, String> lfvalus = null;
			LfCustField llf = null;
			String colname = null;
			Method mf = null;
			// setter方法名
			String s = "";
			// 多选用户填的值
			String[] aas = null;
			// 多选的值
			StringBuffer buas = null;
			int resultCount = 0;
			int maxNum = 200000;
			if(lastrow>=maxNum){//里面多了一个标题
				request.setAttribute("result", "overMax");//超过最大行200000
				return;
			}

			// -------------------------------------
			List<String> lineContent = excel.getLineContent();

			//超过最大列总行数
			int outMaxColumCount = 0;
			//最大列
			int maxColum = 70;
			//更新次数
			int updateTimer = 0;
			//计算行数
			int k = -1;
			for(String tmp : lineContent){
				if(k > maxNum){
					break;
				}
				//需要每次都要初始化，不然如果下个没有值，就会为上个字段的值
				Long unclientId = null;
				String namemobile = "";
				String phoneStr = "";
				String nameStr = "";
				String clientCode = "";
				int sex = 2;
				Timestamp birthday = null;
				String eMail = "";
				String msn = "";
				String oph = "";
				String qq = "";
				String comments = "";
				String job = "";
				String profession = "";
				String ename = "";
				String area = "";

				boolean isAdd = true;
				// 循环获得单元行
				if("".equals(tmp)){
					continue;
				}

				k++;

				//标题跳过
				if(k==0){
					continue;
				}

				cells = tmp.split(",");
				int size = cells.length;

				if (size >1){
					nameStr =cells[1];
					if (nameStr.trim().length() < 1 || nameStr.trim().length() > 33)
					{
						contentSb.append(di).append(k + 1).append(hang).append("      ").append("     ").append(xmc32).append(line);
						repeatNum++;
						continue;
					}
				}
				if (size >2){
					// 第一个单元格内容
					clientCode =cells[2];
					if (clientCode != null && !"".equals(clientCode) && clientCode.length() < 32)
					{
						// 转换成大写判断
						clientCode = clientCode.toUpperCase();
					}
				}
				// 手机
				if (size >3){
					phoneStr =cells[3];
					if (phoneStr.length() == 0){
						contentSb.append(di).append(k + 1).append(hang).append("    ").append("     ").append(sjhwk).append(line);
						repeatNum++;
						continue;
					}else if(phoneUtil.getPhoneType(phoneStr, haoduan)==-1){
						contentSb.append(di).append(k + 1).append(hang).append("    ").append(phoneStr).append("     ").append(sjhff).append(line);
						repeatNum++;
						continue;
					}
					//增加电话号码为空的判断
					if("".equals(phoneStr)){
						contentSb.append(di).append(k + 1).append(hang).append("   ").append("     ").append(sjhwk).append(line);
						repeatNum++;
						continue;
					}

					namemobile = nameStr.trim()+phoneStr;

					if(addSet.add(namemobile))
					{
						if(dbSet.contains(namemobile))
						{
							isAdd = false;
						}else if((unclientId = unMap.remove(Long.valueOf(phoneStr)))!=null){//需要从未知转入的所选机构
							upSet.add(phoneStr);
							isAdd = false;
						}else if(upSet.contains(phoneStr)){//该手机号码已经处理转入
							continue;
						}
					}else
					{
						contentSb.append(di).append(k + 1).append(hang).append("    ").append(nameStr).append(" ").append(phoneStr).append("    ").append(xmsjcf).append(line);
						repeatNum++;
						continue;
					}
				}


				// 性别
				if (size >4){
					String sexStr=cells[4];
					if (sexStr.length() < 32){
						if (sexStr != null && sexStr.equals(man)){
							sex = 1;
						} else if (sexStr != null && sexStr.equals(woman)){
							sex = 0;
						} else{
							sex = 2;
						}
					}
				}else{
					sex = 2;
				}
				// 生日
				if (size>5)
				{
					String birthdayStr=cells[5];
					try{
						if (birthdayStr != null && !birthdayStr.trim().equals("")&&birthdayStr.trim().length()>=10){
							Timestamp BIRTHDAY = new Timestamp(
									Integer.parseInt(birthdayStr.substring(0, 4)) - 1900,
									Integer.parseInt(birthdayStr.substring(5, 7)) - 1,
									Integer.parseInt(birthdayStr.substring(8, 10)),
									0, 0, 0, 0);
							birthday = BIRTHDAY;
						}
					} catch (Exception eee){
						EmpExecutionContext.error(eee,"批量上传客户处理生日出现异常！");
					}
				}
				// 电子邮箱
				if (size >6){
					if (cells[6].length() < 32){
						eMail = cells[6];
					}
				}
				// MSN
				if (size >7){
					if (cells[7].length() < 32){
						msn =cells[7];
					}
				}
				if (size >8){
					if (cells[8].length() < 32){
						oph = cells[8];
					}
				}
				if (size >9){
					if (cells[9].length() < 32){
						qq =cells[9];
					}
				}
				// 描述
				if (size >10){
					if (cells[10].length() < 32){
						comments = cells[10];
					}
				}
				// 职务
				if (size >11){
					if (cells[11].length() < 32){
						job = cells[11];
					}
				}
				// 行业
				if (size >12){
					if (cells[12].length() < 32){
						profession = cells[12];
					}
				}
				if (size >13){
					if (cells[13].length() < 32){
						ename = cells[13];
					}
				}
				// 区域
				if (size >14){
					if (cells[14].length() < 32){
						area = cells[14];
					}
				}

				//0-5个客户属性
				client5Pro = new LfClient5Pro();
				//多个客户属性
				client = new LfClientMultiPro();

				// 处理自定义属性
				if (size > 15)
				{
					//列大于列最大限制
					if(size > maxColum)
					{
						size = maxColum;
						outMaxColumCount++;
					}
					for (int celns = 15; celns < size; celns++)
					{
						if (cells[celns] == null || "".equals(cells[celns].trim())){
							continue;
						}
						// colname 表示自定义列名
						if(titleList!=null&&titleList.length>=celns){
							colname = titleList[celns].substring(titleList[celns].lastIndexOf(")") + 1);
						}
						// 遍历自定义属性记录列表
						llf = map3.get(colname);
						// 如果没有与列名对应的属性对象就跳过
						if (llf == null){
							continue;
						}
						// 单选
						if (llf.getV_type().equals("0")){
							// 获取属性值列表记录
							lfvalus = map1.get(llf);
							// 遍历属性对应的值记录，如果跟所填值相等
							if (lfvalus.containsKey(cells[celns])){
								s = "F" + llf.getField_Ref().substring(1).toLowerCase();
								if(proCount == 5)
								{
									mf = clazz5Pro.getDeclaredMethod("set" + s,String.class);
									if (cells[celns].length() < 32){
										// 将属性值id放在lfclient对象的filed属性里面去
										mf.invoke(client5Pro, lfvalus.get(cells[celns]));
									}
								}
								else
								{
									mf = clazz.getDeclaredMethod("set" + s,String.class);
									if (cells[celns].length() < 32){
										// 将属性值id放在lfclient对象的filed属性里面去
										mf.invoke(client, lfvalus.get(cells[celns]));
									}
								}
							}
						} else if (llf.getV_type().equals("1")){
							// 多选 获取属性值列表记录
							lfvalus = map1.get(llf);
							aas = cells[celns].replaceAll("；",";").split(";");
							buas = new StringBuffer();
							for (int las = 0; las < aas.length; las++){
								if (lfvalus.containsKey(aas[las])){
									buas.append(lfvalus.get(aas[las]));
									buas.append(";");
								}
								if (buas.toString() != null && !"".equals(buas.toString())){
									s = "F"+ llf.getField_Ref().substring(1).toLowerCase();
									if(proCount == 5)
									{
										mf = clazz5Pro.getDeclaredMethod("set" + s,String.class);
										mf.invoke(client5Pro, buas.toString().substring(0,buas.toString().lastIndexOf(";")));
									}
									else
									{
										mf = clazz.getDeclaredMethod("set" + s,String.class);
										mf.invoke(client, buas.toString().substring(0,buas.toString().lastIndexOf(";")));
									}
								}
							}
						}
					}

				}

				//0-5个客户属性
				if(proCount == 5)
				{
					client5Pro.setName(nameStr);
					client5Pro.setMobile(phoneStr);
					client5Pro.setSex(sex);
					client5Pro.setBirthday(birthday);
					client5Pro.setEMail(eMail);
					client5Pro.setMsn(msn);
					client5Pro.setOph(oph);
					client5Pro.setQq(qq);
					client5Pro.setComments(comments);
					client5Pro.setJob(job);
					client5Pro.setProfession(profession);
					client5Pro.setEname(ename);
					client5Pro.setArea(area);
					// 机构id
					client5Pro.setCstate(1);
					// 企业编码
					client5Pro.setCorpCode(lgcorpcode);
					client5Pro.setDepId(0L);
					if(isAdd){
						// 全局id
						client5Pro.setGuId(curValue - rowLength + k);
						client5Pro.setClientId(curValue - rowLength + k);
						client5Pro.setClientCode(clientCode == null || "".equals(clientCode)?String.valueOf(curValue - rowLength + k):clientCode);
						addClient5ProList.add(client5Pro);
					}else{
						//修改存在特殊情况 dbMap.get(namemobile)不存在 但是存在手机号码相同的未知机构客户信息
						if(dbMap.get(namemobile)!=null){
							client5Pro.setClientId(Long.parseLong(dbMap.get(namemobile)));
						}else{
							client5Pro.setClientId(unclientId);
							unlist.add(unclientId);//便于删除未知机构客户之前的关联机构信息
						}
						modiClient5ProList.add(client5Pro);
					}
				}
				//大于5个客户属性
				else
				{
					client.setName(nameStr);
					client.setMobile(phoneStr);
					client.setSex(sex);
					client.setBirthday(birthday);
					client.setEMail(eMail);
					client.setMsn(msn);
					client.setOph(oph);
					client.setQq(qq);
					client.setComments(comments);
					client.setJob(job);
					client.setProfession(profession);
					client.setEname(ename);
					client.setArea(area);
					// 机构id
					client.setCstate(1);
					// 企业编码
					client.setCorpCode(lgcorpcode);
					// 全局id
					client.setDepId(0L);
					if(isAdd){
						// 全局id
						client.setGuId(curValue - rowLength + k);
						client.setClientId(curValue - rowLength + k);
						client.setClientCode(clientCode == null || "".equals(clientCode)?String.valueOf(curValue - rowLength + k):clientCode);
						addClientList.add(client);
					}else{
						client.setClientId(Long.parseLong(dbMap.get(namemobile)));
						if(dbMap.get(namemobile)!=null){
							client.setClientId(Long.parseLong(dbMap.get(namemobile)));
						}else{
							client.setClientId(unclientId);
							unlist.add(unclientId);//便于删除未知机构客户之前的关联机构信息
						}
						modiClientList.add(client);
					}
				}

				if((k+1)%5000==0){
					if(updateTimer > (maxNum/5000))
					{
						EmpExecutionContext.error("客户通讯录导入，更新数量超过最大限制，updateTimer："+updateTimer+"，maxNum:"+maxNum);
						break;
					}
					updateTimer++;
					String returnmsg = "";
					//0-5个客户属性
					if(proCount == 5)
					{
						returnmsg = clientBiz.updateClient5ProList(addClient5ProList, modiClient5ProList, deps,unlist);
					}
					//大于5个客户属性
					else
					{
						returnmsg = clientBiz.updateClientList(addClientList, modiClientList, deps,unlist);
					}
					if("success".equals(returnmsg)){
						resultCount =resultCount+ addClient5ProList.size() + modiClient5ProList.size() + addClientList.size() + modiClientList.size();
						if (resultCount > 0){
							insertResult = "upload" + (resultCount + insertCount);
							request.setAttribute("result", insertResult);
						}

					}else{
						request.setAttribute("result", "false");
					}
					addClient5ProList.clear();
					modiClient5ProList.clear();
					addClientList.clear();
					modiClientList.clear();
				}
			}
			if(addClient5ProList.size()>0 || modiClient5ProList.size()>0 ||addClientList.size() > 0 || modiClientList.size() > 0)
			{
				String returnmsg = "";
				//0-5个客户属性
				if(proCount == 5)
				{
					returnmsg = clientBiz.updateClient5ProList(addClient5ProList, modiClient5ProList, deps,unlist);
				}
				//大于5个客户属性
				else
				{
					returnmsg = clientBiz.updateClientList(addClientList, modiClientList, deps,unlist);
				}
				if("success".equals(returnmsg)){
					resultCount = resultCount+addClient5ProList.size() + modiClient5ProList.size() + addClientList.size() + modiClientList.size();
					if (resultCount > 0){
						insertResult = "upload" + (resultCount + insertCount);
						request.setAttribute("result", insertResult);
					}
				}else{
					request.setAttribute("result", "false");
				}
			}
			else
			{
				// for循环外面的clientList为空，并且for循环里面也没有数据，那么就证明excel中没有记录
				request.setAttribute("result", "noRecord");
			}
			if (resultCount + insertCount > 0){
				spLog.logSuccessString(opUser, opModule, oppType, opContent,lgcorpcode);
			}
			// 写文件
			String FileStr = url[0];
			// 临时文件路径
			String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt"))+ "_client" + ".txt";

			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String dep_tempid="";
				if(deps!=null){
					for(int i=0;i<deps.length;i++){
						if(i==deps.length-1){
							dep_tempid=dep_tempid+deps[i];
						}else{
							dep_tempid=deps[i]+","+dep_tempid;
						}
					}
				}
				String content = "批量导入客户[机构ID|数量]（"+dep_tempid+"|"+(resultCount + insertCount)+"条），超过70最大列总次数:"
				+outMaxColumCount+"，耗时:"+(System.currentTimeMillis() - start) + "ms";
				EmpExecutionContext.info("客户通讯录", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), content, "OTHER");			}
			// 写文件
			txtFileUtil.writeToTxtFile(FileStrTemp, new StringBuffer(StringUtils.defaultIfEmpty(MessageUtils.extractMessage("client","client_upload_success",request),"成功上传了"))
					.append(resultCount + insertCount).append(StringUtils.defaultIfEmpty(MessageUtils.extractMessage("client","client_record_number",request),"条记录")).append(line)
					.append(StringUtils.defaultIfEmpty(MessageUtils.extractMessage("client","client_grep_number",request),"过滤号码：")).append(repeatNum).append(StringUtils.defaultIfEmpty(MessageUtils.extractMessage("client","client_record_count",request),"条")).append(line).append(contentSb).toString());
			String pathTemp = url[1].substring(0, url[1].indexOf(".txt"))+"_client"+".txt";
			request.setAttribute("path",pathTemp);
		} catch (Exception e){
			EmpExecutionContext.error(e,"批量上传客户出现异常！");
			// 判断是否因为上传了错误格式的xls文件
			if (e.getMessage().equals("Unable to recognize OLE stream")){
				request.setAttribute("result", "errorXls");
			}else{
				request.setAttribute("result", "false");
			}
			spLog.logFailureString(opUser, opModule, oppType, opContent, e,lgcorpcode);
		}finally{
//			fileItem.delete();
			fileItem = null;
		}


	}


	/**
	 *  处理客户2003版本的xls上传文件
	 * @param request	请求
	 * @param fileItem	页面对象
	 * @param deps	所选机构ID数组
	 */
	@ SuppressWarnings("unchecked")
	public void parseClientXls(HttpServletRequest request, FileItem fileItem,String[] deps)
	{
		long start = System.currentTimeMillis();
		String oppType = StaticValue.ADD;
		String opContent = "批量导入客户";
		GenericLfClientVoDAO clientVoDAO = new GenericLfClientVoDAO();
		SuperOpLog spLog = new SuperOpLog();
		TxtFileUtil txtFileUtil = new TxtFileUtil();

		HttpSession session = request.getSession();
		String langName = (String)session.getAttribute(StaticValue.LANG_KEY);

		// 导入返回错误 内容显示资源
		String di = MessageUtils.extractMessage("client", "client_khtxlgl_txl_upload_1", request);
		String hang = MessageUtils.extractMessage("client", "client_khtxlgl_txl_upload_2", request);
		// 姓名为空或超过32位字符
		String xmc32 = MessageUtils.extractMessage("client", "client_khtxlgl_txl_upload_3", request);
		String sjhwk = MessageUtils.extractMessage("client", "client_khtxlgl_txl_upload_4", request);
		String sjhff = MessageUtils.extractMessage("client", "client_khtxlgl_txl_upload_5", request);
		//姓名和手机号码存在重复
		String xmsjcf = MessageUtils.extractMessage("client", "client_khtxlgl_txl_upload_6", request);

		String man = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_man", request);
		String woman = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_woman", request);

		List<LfCustField> lfcs = null;
		// 插入条数
		Integer insertCount = 0;
		// 插入结果
		String insertResult = new String();
		String lgcorpcode = request.getParameter("lgcorpcode");

		String opUser = "";
		Set<String> addSet = new HashSet<String>();
		Set<String> dbSet = new HashSet<String>();
		//app客户未知机构手机号 clientid集合
		Map<Long,Long> unMap = new HashMap<Long, Long>();
		//从未知机构转入的手机号集合
		Set<String> upSet = new HashSet<String>();
		//转入机构的未知机构客户集合
		List<Long> unlist = new ArrayList<Long>();
		Sheet sh = null;
		Workbook workBook = null;
		Calendar cal =null;
		try
		{
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			LinkedHashMap<String, String> dbMap = clientBiz.getClientMsg(lgcorpcode, dbSet);
			unMap = clientBiz.getUndepClientMsg(lgcorpcode);
			//Set<String> dbSet = dbMap.keySet();
			opUser = sysuser.getUserName();
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			request.setAttribute("lguserid",lguserid);
			request.setAttribute("company", lgcorpcode);
			// 生成文件路径用到
			Date time = Calendar.getInstance().getTime();
			// 包含文件的物理路径和相对路径
			String[] url = this.getClientUploadUrl((int) (Long.parseLong(lguserid) - 0), time);
			String fileCurName = fileItem.getName();
			String fileType = fileCurName.substring(fileCurName.lastIndexOf("."));
			if (!fileType.equals(".xls")){
				return;
			}
			lfcs = getdatalist(lgcorpcode);
			// 单选项和值的map
			Map<LfCustField, Map<String, String>> map1 = new LinkedHashMap<LfCustField, Map<String, String>>();
			// 多选项和值的map
			// 屬性name和屬性對象的map
			Map<String, LfCustField> map3 = new LinkedHashMap<String, LfCustField>();
			LfCustField lf = null;
			Map<String, String> valueIdMap = null;
			for (int i = 0; i < lfcs.size(); i++){
				lf = lfcs.get(i);
				valueIdMap = clientVoDAO.findCustFieldValueList(lf.getId());
				map1.put(lf, valueIdMap);
				map3.put(lf.getField_Name(), lf);
			}
			String[] haoduan = new WgMsgConfigBiz().getHaoduan();
			// 获得excel工作簿对象
			workBook = Workbook.getWorkbook(fileItem.getInputStream());
			sh = workBook.getSheet(0);
			Cell[] cellcol = sh.getRow(0);
			int cellCount = cellcol.length;
			// 判断模板第一行长度是否正确
    			if (cellcol.length != (map1.size() + 14)){
				String realUrl = txtFileUtil.getWebRoot();
				String path = realUrl + "/";
				path += StaticValue.FILE_UPLOAD_PATH;
				String filepath = "/" + lgcorpcode + "clientexam_"+langName+".xls";
				String fullFilePath = path + filepath;
				writeExcel(lfcs, fullFilePath,request);
				request.setAttribute("result", "emptype");
				return;
			}
			//客户属性个数,5为0-5个;6为大于5个
			int proCount = 6;

			//客户属性数量为0-5个
			if(cellCount < 20 )
			{
				proCount = 5;
			}
			/**********客户属性数量为0-5个***********/
			//新增客户信息
			List<LfClient5Pro> addClient5ProList = new ArrayList<LfClient5Pro>();
			//修改客户信息
			List<LfClient5Pro> modiClient5ProList = new ArrayList<LfClient5Pro>();
			LfClient5Pro client5Pro = null;
			// 利用反射将request中获得的值添加到客户中
			Class clazz5Pro = Class.forName("com.montnets.emp.entity.client.LfClient5Pro");

			/**********客户属性数量大于5个***********/
			//新增客户信息
			List<LfClientMultiPro> addClientList = new ArrayList<LfClientMultiPro>();
			//修改客户信息
			List<LfClientMultiPro> modiClientList = new ArrayList<LfClientMultiPro>();
			LfClientMultiPro client = null;
			// 利用反射将request中获得的值添加到客户中
			Class clazz = Class.forName("com.montnets.emp.entity.client.LfClientMultiPro");

			// 减去标题
			int rows = sh.getRows();
			Long rowLength = new Long(rows - 1);
			Long curValue = globalBiz.getValueByKey("guid", rowLength);
			EmpExecutionContext.info("客户通讯录导入，获取文件总行数:"+rows+"，去除标题后行数:"+rowLength+"，获取GUID:"+curValue+"，文件名:"+fileItem.getName());

			Cell[] cells;
			int repeatNum = 0;
			StringBuffer contentSb = new StringBuffer();
			// ---------这些变量都是在循环里面的解析第十四个单元格以后使用的，现在拿到外面避免重复声明
			Map<String, String> lfvalus = null;
			LfCustField llf = null;
			String colname = null;
			Method mf = null;
			// setter方法名
			String s = "";
			// 多选用户填的值
			String[] aas = null;
			// 多选的值
			StringBuffer buas = null;
			int resultCount = 0;
			//最大列数限制
			int maxColum = 70;
			//超过最大列次数
			int outMaxColumCount = 0;
			//更新次数
			int updateTimer = 0;
			//最大行数限制
			int maxNum = 200000;
			if(rows > maxNum)
			{
				rows = maxNum;
			}
			// -------------------------------------
			for (int k = 1; k < rows; k++)
			{
				//需要每次都要初始化，不然如果下个没有值，就会为上个字段的值
				Long unclientId = null;
				String namemobile = "";
				String phoneStr = "";
				String nameStr = "";
				String clientCode = "";
				int sex = 2;
				Timestamp birthday = null;
				String eMail = "";
				String msn = "";
				String oph = "";
				String qq = "";
				String comments = "";
				String job = "";
				String profession = "";
				String ename = "";
				String area = "";

				// 循环获得单元行
				cells = sh.getRow(k);
				int size = cells.length;
				if (size > 0){
					nameStr = cells[0].getContents();
					//此处对值进行正则判断
					Integer nameLength = clientBiz.getStrLength(nameStr);
					if (nameLength < 1 || nameLength > 60)
					{
						contentSb.append(di + (k + 1) + hang+"    ").append("     "+xmc32).append(line);
						repeatNum++;
						continue;
					}
				}
				if (size > 1){
					// 第一个单元格内容
					clientCode = cells[1].getContents();
					if (clientCode != null && !"".equals(clientCode) && clientCode.length() < 32)
					{
						// 转换成大写判断
						clientCode = clientCode.toUpperCase();
					}
				}
				// 手机
				if (size > 2){
					phoneStr = cells[2].getContents().trim();
					if (phoneStr.length() == 0){
						contentSb.append(di + (k + 1) + hang+"    ").append("     "+sjhwk).append(line);
						repeatNum++;
						continue;
					}else if(phoneUtil.getPhoneType(phoneStr, haoduan)==-1){
						contentSb.append(di + (k + 1) + hang+"    ").append(phoneStr).append("     "+sjhff).append(line);
						repeatNum++;
						continue;
					}
				}
				//增加电话号码为空的判断
				if("".equals(phoneStr)){
					contentSb.append(di + (k + 1) + hang+"    ").append("     "+sjhwk).append(line);
					repeatNum++;
					continue;
				}

				namemobile = nameStr.trim()+phoneStr;
				boolean isAdd = true;
				if(addSet.add(namemobile))
				{
					if(dbSet.contains(namemobile))
					{
						isAdd = false;
					}else if((unclientId = unMap.remove(Long.valueOf(phoneStr)))!=null){//需要从未知转入的所选机构
						upSet.add(phoneStr);
						isAdd = false;
					}else if(upSet.contains(phoneStr)){//该手机号码已经处理转入
						continue;
					}
				}else
				{
					contentSb.append(di + (k + 1) + hang+"    ").append(nameStr +" "+phoneStr).append("    "+xmsjcf).append(line);
					repeatNum++;
					continue;
				}

				// 性别
				if (size > 3){
					if (cells[3].getContents().length() < 32){
						if (cells[3].getContents() != null && cells[3].getContents().equals(man)){
							sex = 1;
						} else if (cells[3].getContents() != null && cells[3].getContents().equals(woman)){
							sex = 0;
						} else{
							sex = 2;
						}
					}
				}else{
					sex = 2;
				}
				// 生日
				if (size > 4)
				{
					try{
						if (cells[4].getContents() != null && !cells[4].getContents().trim().equals("")){
							Cell c00 = cells[4];
							String birth = "";
							if (c00.getType() == CellType.LABEL){
								birth = c00.getContents();
								if(birth != null && birth.trim().length() >= 10)
								{
									cal = Calendar.getInstance();
									cal.set(Calendar.YEAR, Integer.parseInt(birth.substring(0, 4)));
									cal.set(Calendar.MONTH, Integer.parseInt(birth.substring(5, 7))-1);
									cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(birth.substring(8, 10)));
									birthday= new Timestamp(cal.getTimeInMillis());
								}
							}else if (c00.getType() == CellType.DATE){
								DateCell addsss = (DateCell) cells[4];
								Date date = (Date) addsss.getDate();
								if (date != null){
									birthday = new Timestamp(date.getTime());
								}
							}
						}
					} catch (Exception eee){
						EmpExecutionContext.error(eee,"批量上传客户处理生日出现异常！");
					}
				}
				// 电子邮箱
				if (size > 5){
					if (cells[5].getContents().length() < 32){
						eMail = cells[5].getContents();
					}
				}
				// MSN
				if (size > 6){
					if (cells[6].getContents().length() < 32){
						msn = cells[6].getContents();
					}
				}
				if (size > 7){
					if (cells[7].getContents().length() < 32){
						oph = cells[7].getContents();
					}
				}
				if (size > 8){
					if (cells[8].getContents().length() < 32){
						qq = cells[8].getContents();
					}
				}
				// 描述
				if (size > 9){
					if (cells[9].getContents().length() < 32){
						comments = cells[9].getContents();
					}
				}
				// 职务
				if (size > 10){
					if (cells[10].getContents().length() < 32){
						job = cells[10].getContents();
					}
				}
				// 行业
				if (size > 11){
					if (cells[11].getContents().length() < 32){
						profession = cells[11].getContents();
					}
				}
				if (size > 12){
					if (cells[12].getContents().length() < 32){
						ename = cells[12].getContents();
					}
				}
				// 区域
				if (size > 13){
					if (cells[13].getContents().length() < 32){
						area = cells[13].getContents();
					}
				}

				//0-5个客户属性
				client5Pro = new LfClient5Pro();
				//多个客户属性
				client = new LfClientMultiPro();

				// 处理自定义属性
				if (size > 14)
				{
					//列大于列最大限制
					if(size > maxColum)
					{
						size = maxColum;
						outMaxColumCount++;
					}
					for (int celns = 14; celns < size; celns++)
					{
						if (cells[celns].getContents() == null || "".equals(cells[celns].getContents().trim())){
							continue;
						}
						// colname 表示自定义列名
						colname = cellcol[celns].getContents().substring(cellcol[celns].getContents().lastIndexOf(")") + 1);
						// 遍历自定义属性记录列表
						llf = map3.get(colname);
						// 如果没有与列名对应的属性对象就跳过
						if (llf == null){
							continue;
						}
						// 单选
						if (llf.getV_type().equals("0")){
							// 获取属性值列表记录
							lfvalus = map1.get(llf);
							// 遍历属性对应的值记录，如果跟所填值相等
							if (lfvalus.containsKey(cells[celns].getContents())){
								s = "F" + llf.getField_Ref().substring(1).toLowerCase();
								if(proCount == 5)
								{
									mf = clazz5Pro.getDeclaredMethod("set" + s,String.class);
									if (cells[celns].getContents().length() < 32){
										// 将属性值id放在lfclient对象的filed属性里面去
										mf.invoke(client5Pro, lfvalus.get(cells[celns].getContents()));
									}
								}
								else
								{
									mf = clazz.getDeclaredMethod("set" + s,String.class);
									if (cells[celns].getContents().length() < 32){
										// 将属性值id放在lfclient对象的filed属性里面去
										mf.invoke(client, lfvalus.get(cells[celns].getContents()));
									}
								}
							}
						} else if (llf.getV_type().equals("1")){
							// 多选 获取属性值列表记录
							lfvalus = map1.get(llf);
							aas = cells[celns].getContents().replaceAll("；",";").split(";");
							buas = new StringBuffer();
							for (int las = 0; las < aas.length; las++){
								if (lfvalus.containsKey(aas[las])){
									buas.append(lfvalus.get(aas[las]));
									buas.append(";");
								}
								if (buas.toString() != null && !"".equals(buas.toString())){
									s = "F"+ llf.getField_Ref().substring(1).toLowerCase();
									if(proCount == 5)
									{
										mf = clazz5Pro.getDeclaredMethod("set" + s,String.class);
										mf.invoke(client5Pro, buas.toString().substring(0,buas.toString().lastIndexOf(";")));
									}
									else
									{
										mf = clazz.getDeclaredMethod("set" + s,String.class);
										mf.invoke(client, buas.toString().substring(0,buas.toString().lastIndexOf(";")));
									}
								}
							}
						}
					}
				}

				//0-5个客户属性
				if(proCount == 5)
				{
					client5Pro.setName(nameStr);
					client5Pro.setMobile(phoneStr);
					client5Pro.setSex(sex);
					client5Pro.setBirthday(birthday);
					client5Pro.setEMail(eMail);
					client5Pro.setMsn(msn);
					client5Pro.setOph(oph);
					client5Pro.setQq(qq);
					client5Pro.setComments(comments);
					client5Pro.setJob(job);
					client5Pro.setProfession(profession);
					client5Pro.setEname(ename);
					client5Pro.setArea(area);
					// 机构id
					client5Pro.setCstate(1);
					// 企业编码
					client5Pro.setCorpCode(lgcorpcode);
					client5Pro.setDepId(0L);
					if(isAdd){
						// 全局id
						client5Pro.setGuId(curValue - rowLength + k);
						client5Pro.setClientId(curValue - rowLength + k);
						client5Pro.setClientCode(clientCode == null || "".equals(clientCode)?String.valueOf(curValue - rowLength + k):clientCode);
						addClient5ProList.add(client5Pro);
					}else{
						//修改存在特殊情况 dbMap.get(namemobile)不存在 但是存在手机号码相同的未知机构客户信息
						if(dbMap.get(namemobile)!=null){
							client5Pro.setClientId(Long.parseLong(dbMap.get(namemobile)));
						}else{
							client5Pro.setClientId(unclientId);
							unlist.add(unclientId);//便于删除未知机构客户之前的关联机构信息
						}
						modiClient5ProList.add(client5Pro);
					}
				}
				//大于5个客户属性
				else
				{
					client.setName(nameStr);
					client.setMobile(phoneStr);
					client.setSex(sex);
					client.setBirthday(birthday);
					client.setEMail(eMail);
					client.setMsn(msn);
					client.setOph(oph);
					client.setQq(qq);
					client.setComments(comments);
					client.setJob(job);
					client.setProfession(profession);
					client.setEname(ename);
					client.setArea(area);
					// 机构id
					client.setCstate(1);
					// 企业编码
					client.setCorpCode(lgcorpcode);
					// 全局id
					client.setDepId(0L);
					if(isAdd){
						// 全局id
						client.setGuId(curValue - rowLength + k);
						client.setClientId(curValue - rowLength + k);
						client.setClientCode(clientCode == null || "".equals(clientCode)?String.valueOf(curValue - rowLength + k):clientCode);
						addClientList.add(client);
					}else{
						client.setClientId(Long.parseLong(dbMap.get(namemobile)));
						if(dbMap.get(namemobile)!=null){
							client.setClientId(Long.parseLong(dbMap.get(namemobile)));
						}else{
							client.setClientId(unclientId);
							unlist.add(unclientId);//便于删除未知机构客户之前的关联机构信息
						}
						modiClientList.add(client);
					}
				}

				cells = null;
				if((k+1)%5000==0){
					if(updateTimer > (maxNum/5000))
					{
						EmpExecutionContext.error("客户通讯录导入，更新数量超过最大限制，updateTimer："+updateTimer+"，maxNum:"+maxNum);
						break;
					}
					String returnmsg = "";
					//0-5个客户属性
					if(proCount == 5)
					{
						returnmsg = clientBiz.updateClient5ProList(addClient5ProList, modiClient5ProList, deps,unlist);
					}
					//大于5个客户属性
					else
					{
						returnmsg = clientBiz.updateClientList(addClientList, modiClientList, deps,unlist);
					}
					if("success".equals(returnmsg)){
						resultCount =resultCount+ addClient5ProList.size() + modiClient5ProList.size() + addClientList.size() + modiClientList.size();
						if (resultCount > 0){
							insertResult = "upload" + (resultCount + insertCount);
							request.setAttribute("result", insertResult);
						}

					}else{
						request.setAttribute("result", "false");
					}
					addClient5ProList.clear();
					modiClient5ProList.clear();
					addClientList.clear();
					modiClientList.clear();
				}

			}


			if(addClient5ProList.size()>0 || modiClient5ProList.size()>0 ||addClientList.size() > 0 || modiClientList.size() > 0)
			{
				String returnmsg = "";
				//0-5个客户属性
				if(proCount == 5)
				{
					returnmsg = clientBiz.updateClient5ProList(addClient5ProList, modiClient5ProList, deps,unlist);
				}
				//大于5个客户属性
				else
				{
					returnmsg = clientBiz.updateClientList(addClientList, modiClientList, deps,unlist);
				}
				if("success".equals(returnmsg)){
					resultCount = resultCount+addClient5ProList.size() + modiClient5ProList.size() + addClientList.size() + modiClientList.size();
					if (resultCount > 0){
						insertResult = "upload" + (resultCount + insertCount);
						request.setAttribute("result", insertResult);
					}
				}else{
					request.setAttribute("result", "false");
				}
			}
			else
			{
				// for循环外面的clientList为空，并且for循环里面也没有数据，那么就证明excel中没有记录
				request.setAttribute("result", "noRecord");
			}
			if (resultCount + insertCount > 0){
				spLog.logSuccessString(opUser, opModule, oppType, opContent,lgcorpcode);
			}
			// 写文件
			String FileStr = url[0];
			// 临时文件路径
			String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt"))+ "_client" + ".txt";

			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String dep_tempid="";
				if(deps!=null){
					for(int i=0;i<deps.length;i++){
						if(i==deps.length-1){
							dep_tempid=dep_tempid+deps[i];
						}else{
							dep_tempid=deps[i]+","+dep_tempid;
						}
					}
				}
				String content = "批量导入客户[机构ID|数量]（"+dep_tempid+"|"+(resultCount + insertCount)+"条），超过70最大列总次数:"
				+outMaxColumCount+"，耗时:"+(System.currentTimeMillis() - start) + "ms";
				EmpExecutionContext.info("客户通讯录", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), content, "OTHER");			}
			// 写文件
			txtFileUtil.writeToTxtFile(FileStrTemp, new StringBuffer("成功上传了")
					.append(resultCount + insertCount).append("条记录").append(line)
					.append("过滤号码：").append(repeatNum).append("条").append(line).append(contentSb).toString());
			String pathTemp = url[1].substring(0, url[1].indexOf(".txt"))+"_client"+".txt";
			request.setAttribute("path",pathTemp);
		} catch (Exception e){
			EmpExecutionContext.error(e,"批量上传客户出现异常！");
			// 判断是否因为上传了错误格式的xls文件
			if (e.getMessage().equals("Unable to recognize OLE stream")){
				request.setAttribute("result", "errorXls");
			}else{
				request.setAttribute("result", "false");
			}
			spLog.logFailureString(opUser, opModule, oppType, opContent, e,lgcorpcode);
		}finally{
			// 清空工作表
			sh = null;
			if(workBook != null)
			{
				// 关闭工作簿
				workBook.close();
				workBook = null;
			}
//			fileItem.delete();
			fileItem = null;
		}

	}

	/**
	 *  处理客户手机号码
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void checkClient(HttpServletRequest request,HttpServletResponse response) throws IOException{
		try{
			String[] haoduan = new WgMsgConfigBiz().getHaoduan();
			// 检查号段是否合法
			String tmp = request.getParameter("mobile");
			if(tmp == null || tmp.length() == 0){
				response.getWriter().print("numfalse");
				EmpExecutionContext.error("处理客户手机号码失败，手机号码为空。");
				return;
			}
			if (phoneUtil.getPhoneType(tmp, haoduan) == -1) {
				response.getWriter().print("numfalse");
				return;
			}else{
				String name = request.getParameter("name");
				String lgcorpcode = request.getParameter("lgcorpcode");
				//1是新增   2是修改
				String actionType = request.getParameter("actionType");
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("corpCode",lgcorpcode);
				if("edit".equals(actionType)){
					String clientid = request.getParameter("clientid");
					conditionMap.put("clientId&<>",String.valueOf(clientid));
				}
				conditionMap.put("name&<>",name);
				conditionMap.put("mobile",tmp);

				List<LfClient> clientList = baseBiz.getByCondition(LfClient.class, conditionMap, null);
				if("edit".equals(actionType)){
					String depId = request.getParameter("depId");
					LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
					condition.put("name",name);
					condition.put("mobile",tmp);
					condition.put("corpCode",lgcorpcode);
					String clientid = request.getParameter("clientid");
					condition.put("clientid",String.valueOf(clientid));
					condition.put("depid",depId);
					int number=clientBiz.hasClient(condition);
					if(number>0){
						response.getWriter().print("updateExist");
						return;
					}
				}

				if(clientList != null && clientList.size()>0){
					response.getWriter().print("isexistnum");
				}else{
					response.getWriter().print("numtrue");
				}
				return;
			}
		}catch (Exception e) {
			response.getWriter().print("numfalse");
			EmpExecutionContext.error(e,"处理客户手机号码出现异常！");
		}
	}

	/**
	 *
	 * 2012-12-10
	 *
	 * @param phone 要验证的号码
	 * @param name 要求验证的姓名
	 * @param phoneNameSet 已经存在的号码合集
	 * @param checkFlag 页面选择时候要过滤重号 1：验证；2：不验证
	 * @return boolean true表示重复，false表示不重复
	 */
	public boolean checkRepeat(String phone, String name, Set<String> phoneNameSet, Set<String> phoneSet,int checkFlag) {
		// 先过滤电话姓名同时重复的记录
		if(phoneNameSet==null)
		{
			return false;
		}
		if (phoneNameSet.contains(name+phone))
		{
			return true;
		}
		// 再过滤电话重复的
		// 过滤
		if (checkFlag == 1)
		{
			if (phoneSet.contains(phone))
			{
				return true;
			}
			phoneSet.add(phone);
		}
		//不过滤
		phoneNameSet.add(name+phone);
		return false;
	}


	public void checkBook(HttpServletRequest request,
			HttpServletResponse response)
	{
		try
		{
			// 验证客户是否已存在
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String mobile = request.getParameter("mobile").trim();
			String name = request.getParameter("name").trim();
			String depId = request.getParameter("depCode") !=null? request.getParameter("depCode").trim():"";
			String changeFlag = request.getParameter("changeFlag").trim();
			if (mobile != null && !"".equals(mobile))
			{
				String[] haoduan = new WgMsgConfigBiz().getHaoduan();
				// 检查号段是否合法
				int re = addrBookBiz.checkMobile(mobile, haoduan);
				if (re != 1)
				{
					// 合法异步输出结果
					response.getWriter().print("numfalse");
					return;
				} else
				{
					if ("true".equals(changeFlag))
					{
						conditionMap.put("mobile", mobile);
						if(depId == null){
							depId = "";
						}
						if(depId!=null && !"".equals(depId)){
							depId = depId.substring(0, depId.length()-1);
						}
						String[] deps = depId.split(",");
						if(deps.length>1){
							for(int i=0;i<deps.length;i++){
								conditionMap.put("depId", deps[i]);
								List<LfClient> clientList = baseBiz
										.getByCondition(LfClient.class, conditionMap,
												null);
								if (clientList == null || clientList.size() == 0)
								{
									response.getWriter().print("noRepeat");
									return;
								}
								for (LfClient client : clientList)
								{
									if (name.equals(client.getName()))
									{
										response.getWriter().print("phoneNameRepeat");
										return;
									}
								}
							}
						}
						response.getWriter().print("true");
						return;
					} else
					{
						response.getWriter().print("true");
					}
				}
			} else
			{
				response.getWriter().print("true");
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"验证客户是否已存在出现异常！");
		}
	}

	/**
	 * @param request
	 * @description 增加部门,验证机构级数和同机构下子机构总数是否合理
	 * @param response
	 * @throws IOException
	 */
	public void addDep(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// 增加部门,验证机构级数和同机构下子机构总数是否合理
		String oppType = StaticValue.ADD;
		String opContent = "";
		String name = "";
		// 当前登录操作员id
		//Long lguserid = Long.valueOf(request.getParameter("lguserid"));
		//漏洞修复 session里获取操作员信息
		Long lguserid = SysuserUtil.longLguserid(request);


		// 当前登录企业
//		String lgcorpcode = request.getParameter("lgcorpcode");
		LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		String lgcorpcode = sysuser.getCorpCode();

		String opUser = "";
		try
		{
//			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			opUser = sysuser.getUserName();
			SuperOpLog spLog = new SuperOpLog();
			EnterpriseBiz enterBiz = new EnterpriseBiz();
			// 机构名称
			name = request.getParameter("name").trim();
			// 父id
			String superiorId = request.getParameter("superiorId").trim();
			// 第三方机构编码
			String depcodethird = request.getParameter("depcodethird").trim()
					.toUpperCase();

			//****判断父机构是否存在(多个用户交叉操作时候)
			LfClientDep parentDep = baseBiz.getById(LfClientDep.class,
					Long.valueOf(superiorId));
			if(parentDep==null){
				setLog(request, "客户通讯录", "新增子机构，父机构不存在，可能已被删除！", StaticValue.OTHER);
				response.getWriter().print("parentnotexit");
				return;
			}

			// 检查自定义机构编码是否重复
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

			//判断自定义编码和机构名称是否重复
			List<DynaBean> employeeList = clientBiz.sameDepNameOrDepCode(name, depcodethird, superiorId, lgcorpcode);
			if(employeeList!=null && employeeList.size() > 0)
			{
				String output = "";
				if(depcodethird.equals(employeeList.get(0).get("dep_code_third").toString()))
				{
					output="codethirdRepeat";
				}
				else
				{
					output="existdepname";
				}
				response.getWriter().print(output);
				return;
			}

			// 获取系统配置的机构信息的map
			LinkedHashMap<String, String> map = SystemGlobals
					.getSysParamLfcorpConf(lgcorpcode);
			// 机构级别(系统配置)
			String maxLevel = map.get("dep.maxlevel");
			// 单个机构子机构数(系统配置)
			String maxchild = map.get("dep.maxchild");
			// 机构总数(系统配置)
			String maxdep = map.get("dep.maxdep");

			long depCount = enterBiz.getClientDepCount(lgcorpcode);
			long levCount = enterBiz.getClientDepLevByDepId(Long
					.parseLong(superiorId));

			// 获取当前机构下的直接子机构的数量
			conditionMap.clear();
			conditionMap.put("parentId", superiorId);
			conditionMap.put("corpCode", lgcorpcode);
			List<LfClientDep> leList = baseBiz.getByCondition(
					LfClientDep.class, conditionMap, null);
			long childCount = leList == null ? 0 : leList.size();
			// 如果机构总数超过了配置的总数，则返回
			if (depCount >= Long.parseLong(maxdep))
			{
				response.getWriter().print("maxDep");
				return;
			}
			// 如果机构层数超过了配置的总数，则返回
			else if (levCount >= Long.parseLong(maxLevel))
			{
				response.getWriter().print("maxLevel");
				return;
			}
			// 如果子机构数超过了配置的总数，则返回
			else if (childCount >= Long.parseLong(maxchild))
			{
				response.getWriter().print("maxChild");
				return;
			}

			// 构造一个客户机构对象，作为存储对象
			LfClientDep dep = new LfClientDep();
			dep.setParentId(Long.valueOf(superiorId));
			dep.setDepName(name);
			dep.setCorpCode(lgcorpcode);
			dep.setDepcodethird(depcodethird);
			boolean result = false;


			dep.setDeppath(parentDep.getDeppath());
			dep.setDepLevel(parentDep.getDepLevel() + 1);
			// 经过所有验证后
			result = eb.addCliDepConnInAddCliDep(lguserid, dep);
			if (result)
			{
				opContent = "新增客户通讯录机构（机构名称:" + name + ")";
				spLog.logSuccessString(opUser, opModule, oppType, opContent,
						lgcorpcode);
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("客户通讯录", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "新增客户通讯录机构成功。[部门名称，第三方机构编码，部门路径，部门级别]（"+name+","+depcodethird+"，"+parentDep.getDeppath()+"，"+(parentDep.getDepLevel()+1)+"）", "ADD");
				}

				response.getWriter().print(result + "");
			} else
			{
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("客户通讯录", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "新增客户通讯录机构失败。[部门名称，第三方机构编码，部门路径，部门级别]（"+name+","+depcodethird+"，"+parentDep.getDeppath()+"，"+(parentDep.getDepLevel()+1)+"）", "ADD");
				}
				response.getWriter().print("length");
			}
		} catch (Exception e)
		{
			opContent = "新增客户通讯录机构（机构名称:" + name + ")";
			spLog.logFailureString(opUser, opModule, oppType, opContent, e,
					lgcorpcode);
			EmpExecutionContext.error(e,"模块名称：客户通讯录，企业："+lgcorpcode+"，操作员："+opUser+"，"+opContent+"，新增异常");
			response.getWriter().print("error");
			EmpExecutionContext.error(e,"新增客户通讯录机构出现异常！");
		}
	}


	/**
	 *   批量删除客户
	 * @param request 请求
	 * @param response	回应
	 * @throws IOException
	 */
	public void deletecd(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// 当前登录企业
		String lgcorpcode = "";

		String opUser = "";
		String oppType=StaticValue.DELETE;
		String opContent = "批量删除客户";
		try
		{
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			lgcorpcode = sysuser.getCorpCode();
			opUser = sysuser.getUserName();
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			//-----增加解密处理----
			String clientSelect="";
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				String bookIds = request.getParameter("bookIds");
				String userIdStr = "";

				String[] clientArray=null;
				if(bookIds!=null&&bookIds.length()>0){
					clientArray= bookIds.split(",");
				}
				if(clientArray!=null){
					for(int k=0;k<clientArray.length;k++){

						//解密
						userIdStr = encryptOrDecrypt.decrypt(clientArray[k]);
						if(userIdStr == null)
						{
							EmpExecutionContext.error("客户通讯录参数解密码失败，keyId:"+clientArray[k]);
							response.getWriter().print("error");
							return;
						}
						if(k==clientArray.length-1){
							clientSelect=clientSelect+userIdStr;
						}else{
							clientSelect=clientSelect+userIdStr+",";
						}

					}
				}

			}
			else
			{
				EmpExecutionContext.error("客户通讯录从session中获取加密对象为空！");
				response.getWriter().print("error");
				return;
			}
			//----id解密结束-----

			String retrunmsg= "";
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("clientId&in", clientSelect);
			conditionMap.put("corpCode", sysuser.getCorpCode());
			List<LfClient> clientList = baseBiz.getByCondition(LfClient.class, conditionMap, null);
			String  phones="";
			for(int i=0;i<clientList.size();i++){
				LfClient client=	clientList.get(i);
				phones=client.getMobile()+","+phones;
			}
			if(clientList != null && clientList.size()>0){

				//这里表示的是批量删除的客户CLIENTID 对应的客户机构关联表
				LinkedHashMap<Long,List<Long>> clientmsg = clientBiz.getExistClientDeps(clientSelect);
				//获取当前操作员的客户管辖机构范围
				String userclientconn = clientBiz.getUserClientConnDepId(Long.valueOf(lguserid));
				HashSet<Long> clientConnSet = new HashSet<Long>();
				if(userclientconn != null && userclientconn.length()>0){
					String[] conn = userclientconn.split(",");
					if(conn != null){
						for(String temp:conn){
							if(temp != null && !"".equals(temp)){
								clientConnSet.add(Long.valueOf(temp));
							}
						}
					}
				}
				List<StringBuffer> bufferList = clientBiz.filterDelClient(clientList, clientmsg, clientConnSet);
				StringBuffer clientidbuffer = bufferList.get(0);
				StringBuffer guidbuffer = bufferList.get(1);
				retrunmsg = clientBiz.isDeleteClient(clientSelect,guidbuffer.toString(),clientidbuffer.toString(),userclientconn,lgcorpcode);
			}
			if("success".equals(retrunmsg)){
				response.getWriter().print(1);

				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("客户通讯录", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "批量删除客户成功。[客户ID|手机号]（"+clientSelect+"|"+phones+"）", "DELETE");
				}
				spLog.logSuccessString(opUser, opModule, oppType, opContent,lgcorpcode);
			}else{
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("客户通讯录", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "批量删除客户失败。[客户ID|手机号]（"+clientSelect+"|"+phones+"）", "DELETE");
				}

				response.getWriter().print(0);
			}

		}catch(Exception e){
			spLog.logFailureString(opUser, opModule, oppType, opContent, e,
					lgcorpcode);

			EmpExecutionContext.error(e,"模块名称：客户通讯录，企业："+lgcorpcode+"，操作员："+opUser+"，批量删除客户出现异常！");
			response.getWriter().print(0);
		}
	}

	/**
	 *  处理删除客户机构操作
	 * @param request
	 * @param response
	 */
	public void delDep(HttpServletRequest request, HttpServletResponse response)
	{
		String oppType = StaticValue.DELETE;
		String lgcorpcode ="";
		String opUser = "";
		String depid = request.getParameter("id");
		String opContent = "删除客户机构(id:" + depid + ")";
		try{
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			opUser = sysuser.getUserName();
			lgcorpcode = sysuser.getCorpCode();
			// 当前登录操作员id
			//Long lguserid = Long.valueOf(request.getParameter("lguserid"));
			//漏洞修复 session里获取操作员信息
			Long lguserid = SysuserUtil.longLguserid(request);

			int result=-1;
			//未知机构且已加载APP企业模块,不允许删除
			if("-10".equals(depid) && ismodule)
			{
				//APP用户未知机构不允许删除
				result = 4;
			}
			else
			{
				LfClientDep lfClientDep = baseBiz.getById(LfClientDep.class, depid);
				// 当多个用户删除同一个机构，可能导致通过depid查询不到机构
				if(lfClientDep==null){
					EmpExecutionContext.error("删除客户机构操作出现异常，部门ID不存在(可能已经被删除)，depid："+depid);
					//该用户可能已经删除
					response.getWriter().print("5");
					return;
				}

				//判断是不是该企业的顶级机构
				if(lfClientDep.getDepLevel()!=null && lfClientDep.getDepLevel().intValue() == 1)
				{
					result = 2;
				}
				else
				{
					LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("userId",String.valueOf(lguserid));
					conditionMap.put("depId",depid);
					List<LfCliDepConn> depConnList = baseBiz.getByCondition(LfCliDepConn.class, conditionMap, null);
					if(depConnList != null && depConnList.size()>0){
						response.getWriter().print(3);
						return;
					}
					// 不能删除有设置生日祝福的机构
					// 查询当前机构在生日祝福中是否使用
					conditionMap.clear();
					conditionMap.put("memberId",depid);
					conditionMap.put("type", "2");
					conditionMap.put("corpCode",lgcorpcode);
					conditionMap.put("membertype", "2");
					List<LfBirthdayMember> birthdayMembers = baseBiz.getByCondition(LfBirthdayMember.class,conditionMap,null);
					if (birthdayMembers != null && birthdayMembers.size() > 0 ){
						response.getWriter().print("haveBirthdayMembers");
						EmpExecutionContext.info("客户通讯录-该机构有被客户生日祝福使用，不能删除。机构id：" + depid + "企业编码：" + lgcorpcode);
						return;
					}

					// 查询当前机构的上级机构在生日祝福中是否使用
					String deppath = lfClientDep.getDeppath();
					// 获取父机构id
					String supIds = StringUtils.join(deppath.split("/"),",");
					conditionMap.clear();
					conditionMap.put("memberId&in",supIds);
					conditionMap.put("type", "2");
					conditionMap.put("corpCode",lgcorpcode);
					// 设置成员类型为包含子机构
					conditionMap.put("membertype", "3");
					List<LfBirthdayMember> supBirthdayMembers = baseBiz.getByCondition(LfBirthdayMember.class,conditionMap,null);
					if (supBirthdayMembers != null && supBirthdayMembers.size() > 0){
						response.getWriter().print("haveBirthdayMembers");
						EmpExecutionContext.info("客户通讯录-该机构的上级机构有被客户生日祝福使用，不能删除。机构id：" + depid + "企业编码：" + lgcorpcode);
						return;
					}
					
					conditionMap.clear();
					conditionMap.put("deppath&like2", lfClientDep.getDeppath());
					List<LfClientDep> clientDepList = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
					if(clientDepList != null && clientDepList.size()>1){
						//有子机构不允许删除
						response.getWriter().print(0);
						return;
					}
					String depName= lfClientDep.getDepName();
					result = clientBiz.delClientDep(depid, lgcorpcode,lguserid);
					//增加日志
					String res="失败";
					if(result>0){
						res="成功";
					}
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("客户通讯录", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "删除客户机构"+res+"。[客户机构ID，机构名称]（"+depid+"，"+depName+"）", "DELETE");
					}

					if (result > 0){
						spLog.logSuccessString(opUser, opModule, oppType, opContent,lgcorpcode);
					}
				}

			}
			response.getWriter().print(result);
		} catch (Exception e){
			spLog.logFailureString(opUser, opModule, oppType, opContent, e,lgcorpcode);
			EmpExecutionContext.error(e,"删除客户机构操作出现异常！");
		}
	}

	/**
	 *   修改客户
	 * @param request
	 * @param response
	 */
	public void doEditcd(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
//			String lgcorpcode = request.getParameter("lgcorpcode");
			//公司编码从session中取值，防止页面攻击
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String lgcorpcode = sysuser.getCorpCode();
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			List<LfCustField> list1 = getdatalist(lgcorpcode);
			Map<LfCustField, List<LfCustFieldValue>> map1 = new LinkedHashMap<LfCustField, List<LfCustFieldValue>>();
			Map<LfCustField, List<LfCustFieldValue>> map2 = new LinkedHashMap<LfCustField, List<LfCustFieldValue>>();
			for (int i = 0; i < list1.size(); i++)
			{
				LfCustField lf = list1.get(i);
				// 判断是否是单选属性
				if (lf.getV_type() != null && "0".equals(lf.getV_type()))
				{
					List<LfCustFieldValue> list2 = getValueVo(lf.getId());
					map1.put(lf, list2);
				}
				// 判断是否多选属性
				else if (lf.getV_type() != null && "1".equals(lf.getV_type()))
				{

					List<LfCustFieldValue> list3 = getValueVo(lf.getId());
					map2.put(lf, list3);
				}
			}
			// 当前登录企业
			String bookId = request.getParameter("bookId");
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				bookId = encryptOrDecrypt.decrypt(bookId);
				if(bookId == null)
				{
					EmpExecutionContext.error("修改客户信息，参数解密码失败，bookId:"+bookId);
					return;
				}
			}
			else
			{
				EmpExecutionContext.error("修改客户信息，从session中获取加密对象为空！");
				return;
			}

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("clientId", bookId);
			conditionMap.put("corpCode", sysuser.getCorpCode());
			List<LfClient> clientList = baseBiz.getByCondition(LfClient.class, conditionMap, null);
//			 LfClient lc = baseBiz.getById(LfClient.class, bookId);

			if(clientList == null || clientList.size() <1){
				EmpExecutionContext.error("跳转 修改客户页面出现异常。获取不到lfclient对象。bookId="+bookId+"。");
				return;
			}

			String[] depname = clientBiz.getDepName(bookId,Long.valueOf(lguserid));
			depname[1] = depname[1]+",";
			request.setAttribute("map1", map1);
			request.setAttribute("map2", map2);
			request.setAttribute("cleid", bookId);
			request.setAttribute("client", clientList.get(0));
			request.setAttribute("depname", depname);
			request.setAttribute("lguserid", lguserid);
			request.setAttribute("lgcorpcode", lgcorpcode);
			request.getRequestDispatcher(this.empRoot + "/climan/cli_editClient.jsp").forward(request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"跳转 修改客户页面出现异常！");
		}
	}
	/**
	 *   进入添加客户界面
	 * @param request
	 * @param response
	 */
	public void getClientType(HttpServletRequest request,
			HttpServletResponse response)
	{
		try
		{
			// 当前登录企业
//			String lgcorpcode = request.getParameter("lgcorpcode");
			//公司编码从session中取值，防止页面攻击
			HttpSession session = request.getSession();
			String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String lgcorpcode = sysuser.getCorpCode();
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			String pageIndex=request.getParameter("pageIndex");
			String pagesize=request.getParameter("pagesize");
			// 获取自定义属性
			List<LfCustField> list1 = getdatalist(lgcorpcode);
			Map<LfCustField, List<LfCustFieldValue>> map1 = new LinkedHashMap<LfCustField, List<LfCustFieldValue>>();
			Map<LfCustField, List<LfCustFieldValue>> map2 = new LinkedHashMap<LfCustField, List<LfCustFieldValue>>();
			// 把单选和多选分开
			for (int i = 0; i < list1.size(); i++)
			{
				LfCustField lf = list1.get(i);
				// 单选
				if (lf.getV_type() != null && "0".equals(lf.getV_type()))
				{
					List<LfCustFieldValue> list2 = getValueVo(lf.getId());
					map1.put(lf, list2);
				}
				// 多选
				else if (lf.getV_type() != null && "1".equals(lf.getV_type()))
				{
					List<LfCustFieldValue> list3 = getValueVo(lf.getId());
					map2.put(lf, list3);
				}
			}
			String realUrl = new TxtFileUtil().getWebRoot();

			String path = realUrl + "/";
			String filepath = "client/climan/file/temp/" + lgcorpcode + "clientexam_"+langName+".xls";
			String fullFilePath = path + filepath;
			writeExcel(list1, fullFilePath,request);
			//增加EXCEL2007模板
			filepath="client/climan/file/temp/" + lgcorpcode + "clientTemplate_"+langName+".xlsx";
			fullFilePath = path + filepath;
			creat2007Excel(list1, fullFilePath,request);

			request.setAttribute("company", lgcorpcode);
			request.setAttribute("filepath", filepath);
			request.setAttribute("map1", map1);
			request.setAttribute("map2", map2);
			request.getSession(false).setAttribute("pageI", pageIndex);
			request.getSession(false).setAttribute("pageS", pagesize);
			request.setAttribute("lguserid", lguserid);
			request.getRequestDispatcher(
					this.empRoot + "/climan/cli_addClient.jsp").forward(
					request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"进入添加客户界面出现异常！");
		}
	}

	/**
	 * @description 得到扩展的客户属性list
	 * @return
	 * @throws Exception
	 */
	public List<LfCustField> getdatalist(String corpCode) throws Exception
	{
		// 得到扩展的客户属性list
		List<LfCustField> dataList = new ArrayList<LfCustField>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("corp_code", corpCode);
			orderbyMap.put("field_Name", "asc");
			// 找出所有的自定义属性列表
			dataList = baseBiz.getByCondition(LfCustField.class,
					conditionMap, orderbyMap);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取 客户属性列表出现异常！");
		}
		return dataList;
	}

	// 得到扩展的客户属性的具体值的list
	public List<LfCustFieldValue> getValueVo(Long fieldID)
	{

		List<LfCustFieldValue> dataVoList = new ArrayList<LfCustFieldValue>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("field_ID", fieldID.toString());
			// 根据id获取属性值列表
			dataVoList = baseBiz.getByCondition(LfCustFieldValue.class,
					conditionMap, orderbyMap);

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取 客户属性列表出现异常！");
		}
		return dataVoList;

	}

	public void exportClientExcel(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		// 下载客户模板
		//String context = request.getSession(false).getServletContext().getRealPath(
			//	"/fileUpload/excelDownload");
		String excelPath = new TxtFileUtil().getWebRoot()+"client/climan/file";
		PrintWriter out = response.getWriter();
		try
		{
			LfClientVo bookInfo = request.getSession(false).getAttribute(
					"clientInfo") != null ? (LfClientVo) request.getSession(false)
					.getAttribute("clientInfo") : new LfClientVo();
			PageInfo pageInfo=new PageInfo();
			pageSet(pageInfo, request);

			ExcelToolClient et = new ExcelToolClient(excelPath);
			// 当前登录操作员id
			//Long lguserid = Long.valueOf(request.getParameter("lguserid"));
			//漏洞修复 session里获取操作员信息
			Long lguserid = SysuserUtil.longLguserid(request);



			// 当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

			conditionMap.put("appacount", request.getParameter("appacount"));
			conditionMap.put("appstatue", request.getParameter("appstatue"));
			Map<String, String> resultMap=null;
			if(ismodule){
				 resultMap = et.createClientExcelRar(lguserid,
							bookInfo, lgcorpcode, pageInfo,conditionMap,request);
			}else {
				 resultMap = et.createClientExcelRar(lguserid,
							bookInfo, lgcorpcode, pageInfo,request);
			}


			if (resultMap != null && resultMap.size() > 0)
			{
				//增加查询日志
				long end_time=System.currentTimeMillis();
		        String opContent = "导出开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"毫秒，" + resultMap.get("totalCount") + "条成功。 ";
				setLog(request, "客户通讯录", opContent, StaticValue.OTHER);

				request.getSession(false).setAttribute("cliaddrbook_export_map",resultMap);
	            out.print("true");
				// 生成下载文件
				//DownloadFile dfs = new DownloadFile();
				//dfs.downFile(request, response, filePath, fileName);
			} else
			{
				out.print("false");
				//this.find(request, response);
			}
		} catch (Exception e)
		{

			EmpExecutionContext.error(e,"下载客户模板出现异常！");
			//this.find(request, response);
		}
	}

	/**
	 * 下载导出文件
	 * @param request
	 * @param response
	 */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response)   {
    	try{
	        HttpSession session = request.getSession(false);
	        Object obj = session.getAttribute("cliaddrbook_export_map");
	        session.removeAttribute("cliaddrbook_export_map");
	        if(obj != null){
	            // 弹出下载页面。
	            DownloadFile dfs = new DownloadFile();
	            Map<String, String> resultMap = (Map<String, String>) obj;
	            String fileName = (String) resultMap.get("FILE_NAME");
	            String filePath = (String) resultMap.get("FILE_PATH");
	            dfs.downFile(request, response, filePath, fileName);
	        }

    	}catch (Exception e) {
    		EmpExecutionContext.error(e,"下载导出文件异常！");
		}
    }

    /***
     * 用于校验2007版本的EXCEL导入的第一行是否是正确（2003版本不存在这样的）
     * @description
     * @param title
     * @return
     * @author zhangsan <zhangsan@126.com>
     * @datetime 2016-1-19 下午04:27:37
     */
    public boolean iscorr(String[] title, HttpServletRequest request){
    	if(title.length<15){
    		return true;
    	}
    	//for(int j=1;j<title.length;j++){
    		if(!MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_name", request).equals(title[1])){
				return true;
    		}
    		if(!MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_clientnumber", request).equals(title[2])){
    			return true;
    		}
    		if(!MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_phone", request).equals(title[3])){
    			return true;
    		}
    		if(!MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_sex", request).equals(title[4])){
    			return true;
    		}
    		if(!MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_birthday", request).equals(title[5])){
    			return true;
    		}
    		if(!"E-mail".equals(title[6])){
    			return true;
    		}
    		if(!"MSN".equals(title[7])){
    			return true;
    		}
    		if(!MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_landline", request).equals(title[8]) ){
    			return true;
    		}
    		if(!"QQ".equals(title[9])){
    			return true;
    		}
    		if(!MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_clientdes", request).equals(title[10])){
    			return true;
    		}
    		if(!MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_duties", request).equals(title[11])){
    			return true;
    		}
    		if(!MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_industry", request).equals(title[12])){
    			return true;
    		}
    		if(!MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_accountmanager", request).equals(title[13])){
    			return true;
    		}
    		if(!MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_belongarea", request).equals(title[14])){
    			return true;
    		}
    	//}
    	return false;
    }

	/**
	 * 创建2007版Excel文件
	 *
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void creat2007Excel(List<LfCustField> list, String path,HttpServletRequest request){
		OutputStream os = null;
		try
		{
			String emplangName = MessageUtils.extractMessage("common","common_empLangName",request);
			String khscmb = MessageUtils.extractMessage("client", "client_khtxlgl_txl_4", request);
			String name = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_name", request);
			String khh = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_clientnumber", request);
			String phone = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_phone", request);
			String sex = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_sex", request);
			String birth = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_birthday", request);
			String tele = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_landline", request);
			String khms = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_clientdes", request);
			String zhiwu = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_duties", request);
			String hangye = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_industry", request);
			String khjl = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_accountmanager", request);
			String ssqy = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_belongarea", request);

			String danx = MessageUtils.extractMessage("client", "client_khtxlgl_txl_1", request);
			String duox = MessageUtils.extractMessage("client", "client_khtxlgl_txl_2", request);
			String xxy = MessageUtils.extractMessage("client", "client_khtxlgl_txl_3", request);

			os = new FileOutputStream(path);
			//工作区
			XSSFWorkbook wb = new XSSFWorkbook();
			//创建第一个sheet
			XSSFSheet sheet= wb.createSheet(khscmb);
			// 设置表格第5列为文本格式
			XSSFCellStyle cellStyle = wb.createCellStyle();
			XSSFDataFormat format=wb.createDataFormat();
			cellStyle.setDataFormat(format.getFormat("@"));
			sheet.setDefaultColumnStyle(4, cellStyle);
			
			//生成第一行
			XSSFRow row = sheet.createRow(0);
			//给这一行的第一列赋值
			row.createCell(0).setCellValue(name);
			row.createCell(1).setCellValue(khh);
			row.createCell(2).setCellValue(phone);
			row.createCell(3).setCellValue(sex);
			row.createCell(4).setCellValue(birth);
			row.createCell(5).setCellValue("E-mail");
			row.createCell(6).setCellValue("MSN");
			row.createCell(7).setCellValue(tele);
			row.createCell(8).setCellValue("QQ");
			row.createCell(9).setCellValue(khms);
			row.createCell(10).setCellValue(zhiwu);
			row.createCell(11).setCellValue(hangye);
			row.createCell(12).setCellValue(khjl);
			row.createCell(13).setCellValue(ssqy);
			
			//生成第二行
			XSSFRow row2 = sheet.createRow(1);
			row2.createCell(0).setCellValue("zh_HK".equals(emplangName)?"Jay Zhou":"张三");
			row2.createCell(1).setCellValue("10001");
			row2.createCell(2).setCellValue("13247081590");
			row2.createCell(3).setCellValue("zh_HK".equals(emplangName)?"Male":"男");
			// 单独再设置生日格式值为文本格式（之前设置这一列的因为给了默认值会不生效）
			XSSFCell cell=row2.createCell(4);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("1985-10-11");
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			row2.createCell(5).setCellValue("12345@163.com");
			row2.createCell(6).setCellValue("12345");
			row2.createCell(7).setCellValue("0755-88888888");
			row2.createCell(8).setCellValue("123456");
			row2.createCell(9).setCellValue("zh_HK".equals(emplangName)?"Faker":"客户描述");
			row2.createCell(10).setCellValue("zh_HK".equals(emplangName)?"Manager":"经理");
			row2.createCell(11).setCellValue("zh_HK".equals(emplangName)?"Financial":"金融");
			row2.createCell(12).setCellValue("zh_HK".equals(emplangName)?"Jolin":"李四");
			row2.createCell(13).setCellValue("zh_HK".equals(emplangName)?"Shen Zhen":"深圳");

			if (list.size() > 0)
			{
				for (int h = 0; h < list.size(); h++)
				{
					LfCustField lfc = list.get(h);
					// 单选
					if (lfc.getV_type().equals("0"))
					{
						row.createCell(14 + h).setCellValue("("+danx+")"
								+ lfc.getField_Name());
						row2.createCell(14 + h).setCellValue(lfc.getField_Name()
								+ xxy);
					}
					// 多选
					else if (lfc.getV_type().equals("1"))
					{
						row.createCell(14 + h).setCellValue("("+duox+")"
								+ lfc.getField_Name());
						row2.createCell(14 + h).setCellValue(lfc.getField_Name()
								+ xxy+";" + lfc.getField_Name() + xxy);
					}
				}

			}
			//写文件
			wb.write(os);
			//关闭输出流
			os.close();

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"生成excel模板出现异常！");
		}finally {
			SysuserUtil.closeStream(os);
		}


	}


	// 根据客户属性生成excel模板
	public static void writeExcel(List<LfCustField> list, String path,HttpServletRequest request)
	{

		WritableWorkbook outBook;
		try
		{
			String emplangName = MessageUtils.extractMessage("common","common_empLangName",request);
			String khscmb = MessageUtils.extractMessage("client", "client_khtxlgl_txl_4", request);
			String name = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_name", request);
			String khh = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_clientnumber", request);
			String phone = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_phone", request);
			String sex = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_sex", request);
			String birth = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_birthday", request);
			String tele = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_landline", request);
			String khms = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_clientdes", request);
			String zhiwu = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_duties", request);
			String hangye = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_industry", request);
			String khjl = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_accountmanager", request);
			String ssqy = MessageUtils.extractMessage("client", "client_khtxlgl_kftxl_text_belongarea", request);

			String danx = MessageUtils.extractMessage("client", "client_khtxlgl_txl_1", request);
			String duox = MessageUtils.extractMessage("client", "client_khtxlgl_txl_2", request);
			String xxy = MessageUtils.extractMessage("client", "client_khtxlgl_txl_3", request);
			
			// 写excel模板
			outBook = Workbook.createWorkbook(new File(path));
			// 表头行
			WritableSheet ws = outBook.createSheet(khscmb, 0);
			jxl.write.Label labelB = new jxl.write.Label(0, 0, name);
			jxl.write.Label labelA = new jxl.write.Label(1, 0, khh);
			jxl.write.Label labelC = new jxl.write.Label(2, 0, phone);
			jxl.write.Label labelD = new jxl.write.Label(3, 0, sex);
			jxl.write.Label labelE = new jxl.write.Label(4, 0, birth);
			jxl.write.Label labelF = new jxl.write.Label(5, 0, "E-mail");
			jxl.write.Label labelG = new jxl.write.Label(6, 0, "MSN");
			jxl.write.Label labelH = new jxl.write.Label(7, 0, tele);
			jxl.write.Label labelI = new jxl.write.Label(8, 0, "QQ");
			jxl.write.Label labelJ = new jxl.write.Label(9, 0, khms);
			jxl.write.Label labelK = new jxl.write.Label(10, 0, zhiwu);
			jxl.write.Label labelL = new jxl.write.Label(11, 0, hangye);
			jxl.write.Label labelM = new jxl.write.Label(12, 0, khjl);
			jxl.write.Label labelN = new jxl.write.Label(13, 0, ssqy);
			ws.addCell(labelA);
			ws.addCell(labelB);
			ws.addCell(labelC);
			ws.addCell(labelD);
			ws.addCell(labelE);
			ws.addCell(labelF);
			ws.addCell(labelG);
			ws.addCell(labelH);
			ws.addCell(labelI);
			ws.addCell(labelJ);
			ws.addCell(labelK);
			ws.addCell(labelL);
			ws.addCell(labelM);
			ws.addCell(labelN);
			// 实例行
			labelB = new jxl.write.Label(0, 1, "zh_HK".equals(emplangName)?"Jay Zhou":"张三");
			labelA = new jxl.write.Label(1, 1, "10001");
			labelC = new jxl.write.Label(2, 1, "13247081590");
			labelD = new jxl.write.Label(3, 1, "zh_HK".equals(emplangName)?"Male":"男");
			labelE = new jxl.write.Label(4, 1, "1985-10-11");
			labelF = new jxl.write.Label(5, 1, "12345@163.com");
			labelG = new jxl.write.Label(6, 1, "12345");
			labelH = new jxl.write.Label(7, 1, "0755-88888888");
			labelI = new jxl.write.Label(8, 1, "123456");
			labelJ = new jxl.write.Label(9, 1, "zh_HK".equals(emplangName)?"Faker":"客户描述");
			labelK = new jxl.write.Label(10, 1, "zh_HK".equals(emplangName)?"Manager":"经理");
			labelL = new jxl.write.Label(11, 1, "zh_HK".equals(emplangName)?"Financial":"金融");
			labelM = new jxl.write.Label(12, 1, "zh_HK".equals(emplangName)?"Jolin":"李四");
			labelN = new jxl.write.Label(13, 1, "zh_HK".equals(emplangName)?"Shen Zhen":"深圳");
			ws.addCell(labelA);
			ws.addCell(labelB);
			ws.addCell(labelC);
			ws.addCell(labelD);
			ws.addCell(labelE);
			ws.addCell(labelF);
			ws.addCell(labelG);
			ws.addCell(labelH);
			ws.addCell(labelI);
			ws.addCell(labelJ);
			ws.addCell(labelK);
			ws.addCell(labelL);
			ws.addCell(labelM);
			ws.addCell(labelN);
			jxl.write.Label mm = null;
			if (list.size() > 0)
			{
				for (int h = 0; h < list.size(); h++)
				{
					LfCustField lfc = list.get(h);
					// 单选
					if (lfc.getV_type().equals("0"))
					{
						mm = new jxl.write.Label(14 + h, 0, "("+danx+")"
								+ lfc.getField_Name());
						ws.addCell(mm);
						mm = new jxl.write.Label(14 + h, 1, lfc.getField_Name()
								+ xxy);
						ws.addCell(mm);
					}
					// 多选
					else if (lfc.getV_type().equals("1"))
					{
						mm = new jxl.write.Label(14 + h, 0, "("+duox+")"
								+ lfc.getField_Name());
						ws.addCell(mm);
						mm = new jxl.write.Label(14 + h, 1, lfc.getField_Name()
								+ xxy+";" + lfc.getField_Name() + xxy);
						ws.addCell(mm);
					}
				}

			}
			// 写文件操作
			outBook.write();
			// 关闭流
			outBook.close();

		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"生成excel模板出现异常！");
		}
	}
	
	public void getMWClient(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String appcode=request.getParameter("appcode");
		List<DynaBean> client=clientBiz.getMWClient(appcode);
		String result="";
		if(client!=null&&client.size()>0){
			String app_code=client.get(0).get("app_code")==null?"":client.get(0).get("app_code")+"";
			String uname=client.get(0).get("nick_name")==null?"":client.get(0).get("nick_name")+"";
			String phone=client.get(0).get("phone")==null?"":client.get(0).get("phone")+"";
			String sex="";
			if(client.get(0).get("sex")!=null){
			 String s=client.get(0).get("sex")+"";
			 if("1".equals(s)){
			 sex="男";
			 }else if("2".equals(s)){
			 sex="女";
			 }else if("0".equals(s)){
			 sex="未知";
			 }
			}
			String age=client.get(0).get("age")==null?"":client.get(0).get("age")+"";
			String verifytime=client.get(0).get("verifytime")==null?"":client.get(0).get("verifytime")+"" ;
			if(!"".equals(verifytime)){
				if(verifytime.indexOf(".")>-1){
					verifytime=verifytime.substring(0, verifytime.lastIndexOf("."));
				}
			}
			result=app_code+"@@"+uname+"@@"+phone+"@@"+sex+"@@"+age+"@@"+verifytime;
		}
		response.getWriter().print(result);
	}
	
	/**
	 * 设置客户信息
	 * @description    
	 * @param request
	 * @param client
	 * @param isFlag       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-8-21 下午06:18:22
	 */
	public void setClientInfo(HttpServletRequest request, LfClientMultiPro client, boolean isFlag)
	{
		String sex = request.getParameter("sex");	//性别
		String birth = request.getParameter("birth");//生日
		String job = request.getParameter("job");
		String pro = request.getParameter("pro");
		String eName = request.getParameter("eName");
		String qq = request.getParameter("qq");
		String area = request.getParameter("area");
		String msn = request.getParameter("msn");
		String comm = request.getParameter("comm");//客户描述
		String EMail = request.getParameter("EMail");
		String oph = request.getParameter("oph");
		String clientCode = request.getParameter("clientCode");//客户号
		// 当前登录操作员id
		//Long lguserid = Long.valueOf(request.getParameter("lguserid"));
		//漏洞修复 session里获取操作员信息
		Long lguserid = SysuserUtil.longLguserid(request);


		// 性别
		client.setSex(Integer.parseInt(sex));
		
		if(!isFlag){
			//这里是新增
			Long guid = globalBiz.getValueByKey("guid", 1L);
			client.setGuId(guid);
			client.setClientId(guid);
			// 转换成大写插入数据库
			client.setRecState(null);
			client.setShareState(null);
			client.setHideState(null);
			client.setDepId(0L);
			if(isEffectValue(clientCode)){
				client.setClientCode(clientCode.toUpperCase());
			}else{
				client.setClientCode(String.valueOf(guid));
			}
		}else{
			//这里是修改
			if(isEffectValue(clientCode)){
				client.setClientCode(clientCode.toUpperCase());
			}
			if(client.getClientCode() == null || "".equals(client.getClientCode())){
				if(client.getGuId() != null){
					client.setClientCode(String.valueOf(client.getGuId()));
				}
			}
		}
		
		// 职务
		if(isFlag){
			if(isEffectValue(job)){
				client.setJob(job);
			}
		}else{
			client.setJob(job);
		}
		//行业
		if(isFlag){
			if(isEffectValue(pro)){
				client.setProfession(pro);
			}
		}else{
			client.setProfession(pro);
		}
		//经理
		if(isFlag){
			if(isEffectValue(eName)){
				client.setEname(eName);
			}
		}else{
			client.setEname(eName);
		}
		// qq
		if(isFlag){
			if(isEffectValue(qq)){
				client.setQq(qq);
			}
		}else{
			client.setQq(qq);
		}
		// 地区
		if(isFlag){
			if(isEffectValue(area)){
				client.setArea(area);
			}
		}else{
			client.setArea(area);
		}
		// msn
		if(isFlag){
			if(isEffectValue(msn)){
				client.setMsn(msn);
			}
		}else{
			client.setMsn(msn);
		}
		// 描述
		if(isFlag){
			if(isEffectValue(comm)){
				client.setComments(comm);
			}
		}else{
			client.setComments(comm);
		}
		if(isFlag){
			if(isEffectValue(EMail)){
				client.setEMail(EMail);
			}
		}else{
			client.setEMail(EMail);
		}
		if(isFlag){
			if(isEffectValue(oph)){
				client.setOph(oph);
			}
		}else{
			client.setOph(oph);
		}
		if(isEffectValue(birth)){
			client.setBirthday(DateFormatter.swicthSqltring(birth +" 00:00:00"));
		}
		client.setUserId(lguserid);
	}
	
	/**
	 * 根据guid获取客户签约信息
	 * @author WANGRUBIN
	 * @datatime 2015-1-19 下午05:05:20
	 * @description TODO 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getContractDetail (HttpServletRequest request, HttpServletResponse response) throws Exception{
		//企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		//操作员的用户ID
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		String guid = request.getParameter("guid");
		
		//签约客户信息列表
		List<DynaBean> contractList = null;
		//签约套餐map集合
		Map taocanMap = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		
		try {
			//是否第一次打开
			boolean isFirstEnter = false;
			PageInfo pageInfo=new PageInfo();
			isFirstEnter=pageSet(pageInfo, request);
			
			//查询功能
			if(!isFirstEnter){
				
			}
			//返回查询结果
			contractList = addrBookBiz.getContractList(guid,conditionMap, pageInfo);
			
			taocanMap = new HashMap<String, String>();
			
			//查询套餐列表记录并添加到map集合
			if(contractList!=null&&contractList.size()>0){
				 
				String contractIds = "";
				 
				for(int i=0;i<contractList.size();i++){
					DynaBean bean=contractList.get(i);
					contractIds += ""+bean.get("contract_id").toString()+",";
				}
				if(contractIds.indexOf(",")>-1){
					contractIds = contractIds.substring(0,contractIds.length()-1);
				}
				List<DynaBean> contractRaocanList = addrBookBiz.getContractRaocanList(contractIds);
				for(DynaBean bean : contractRaocanList){
					String key = bean.get("contract_id").toString();
					if(key!=null)
					{
						String val = taocanMap.get(key)==null?bean.get("taocan_code").toString():taocanMap.get(key)+","+bean.get("taocan_code").toString();
						taocanMap.put(key, val);
					}
				}
			}
			
			//返回分页信息
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lguserid", lguserid);
			request.setAttribute("lgcorpcode", lgcorpcode);
			request.setAttribute("guid", guid);
			request.setAttribute("contractList", contractList);
			request.setAttribute("taocanMap", taocanMap);
			
			request.getRequestDispatcher(this.empRoot + "/climan/cli_contractDetails.jsp").forward(request, response);
		} catch (Exception e) {
			//记录和打印异常信息
			EmpExecutionContext.error(e,"获取操作员列表的信息出现异常！");
		}		
		
	}
	
	/**
	 * @description  记录操作成功日志  
	 * @param request
	 * @param modName 模块名称
	 * @param opContent 操作详情    	
	 * @param opType 操作类型 ADD UPDATE DELETE GET OTHER		 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-3-3 上午11:29:50
	 */
	public void opSucLog(HttpServletRequest request,String modName,String opContent,String opType){
		try{
			LfSysuser lfSysuser = null;
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if(obj == null) return;
			lfSysuser = (LfSysuser)obj;
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"记录操作日志出现异常！");
		}
	}
	
	/**
	 * 写日志
	 * 
	 * @param request
	 *        请求对象
	 * @param opModule
	 *        菜单名称
	 * @param opContent
	 *        操作内容
	 * @param opType
	 */
	public void setLog(HttpServletRequest request,String opModule,String opContent,String opType){
		try
		{
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, opType);
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,opModule+opType+opContent+"日志写入异常");
		}
	}
	/**
	 * 获取加密对象
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
			//加密对象
			Object encrypOrDecrypttobject = request.getSession(false).getAttribute("decryptObj");
			//加密对象不为空
			if(encrypOrDecrypttobject != null)
			{
				//强转类型
				encryptOrDecrypt=(ParamsEncryptOrDecrypt)encrypOrDecrypttobject;
			}
			else
			{
				EmpExecutionContext.error("从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "从session获取加密对象异常。");
			return null;
		}
	}

}
