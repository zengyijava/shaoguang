package com.montnets.emp.employee.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.birthwish.LfBirthdayMember;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.biz.SendBirthDateBiz;
import com.montnets.emp.common.biz.SubnoManagerBiz;
import com.montnets.emp.common.biz.SysuserBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.vo.LfSysuserVo;
import com.montnets.emp.employee.biz.EmployeeBookBiz;
import com.montnets.emp.employee.biz.EmployeeExcel;
import com.montnets.emp.employee.vo.LfEmployeeTypeVo;
import com.montnets.emp.employee.vo.LfEmployeeVo;
import com.montnets.emp.entity.client.LfCliDepConn;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sysuser.LfRoles;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllot;
import com.montnets.emp.sysuser.biz.DepPriBiz;
import com.montnets.emp.sysuser.biz.RoleBiz;
import com.montnets.emp.sysuser.biz.SysuserPriBiz;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.MD5;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.lang.StringUtils;


/**
 * 员工通讯录
 * eqa_employeeAddrBookServlet
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class epl_employeeBookSvt extends BaseServlet {

	//private EmployeeAddrBookBiz employeeBiz = new EmployeeAddrBookBiz();
	private final EmployeeBookBiz employeeBiz = new EmployeeBookBiz();
	private final DepPriBiz depPriBiz = new DepPriBiz();
	//private EnterpriseBiz eb = new EnterpriseBiz();
	private final GlobalVariableBiz globalBiz = GlobalVariableBiz.getInstance();
	//private AddrBookBiz addrBookBiz = new AddrBookBiz();
	/*private AddrBookDomBiz domBiz = new AddrBookDomBiz();*/
	final String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);
	//操作模块
	final String opModule = StaticValue.ADDBR_MANAGER;
	//操作者
	final String opSper = StaticValue.OPSPER;
	
	private final String empRoot = "user";
	
	private final String excelPath = "user/employee/file/";
	
	private final BaseBiz baseBiz=new BaseBiz();
	final SuperOpLog spLog=new SuperOpLog();
	final EmployeeBookBiz bookBiz = new EmployeeBookBiz();
	final Pattern pattern = Pattern.compile("[*_#:?<>|&]");
	
	
	/**
	 * 解密处理
	 * @param request
	 * @param udgId
	 * @return
	 */
	public String getDecryptValue(HttpServletRequest request,String udgId){
				//-----增加解密处理----
			//加密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			String uid="";
			//加密对象不为空
			if(encryptOrDecrypt != null)
			{
				//解密
				uid = encryptOrDecrypt.decrypt(udgId);
				if(uid == null)
				{
					EmpExecutionContext.error("员工通讯录参数解密码失败，keyId:"+uid);
					return "";
				}
			}
			else
			{
				EmpExecutionContext.error("员工通讯录从session中获取加密对象为空！");
				return "";
			}
			return uid;
	}
	
	
	/**
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		//登录者GUID
		String lgusername = "";
		Long lgguid = null;
		//操作员对象
		LfSysuser sysuser = null;
		//BaseBiz baseBiz = new BaseBiz();
		//分页
		PageInfo pageInfo=new PageInfo();
		
		
		try {
			//获取登录sysuser
			sysuser =depPriBiz.getCurrenUser(request);
			if(sysuser==null){
				EmpExecutionContext.error("员工通讯录,find方法session中获取当前登录对象出现异常");
				return;
			}
			String lgcorpcode=sysuser.getCorpCode();
			//判断企业编码获取
			if(lgcorpcode==null||"".equals(lgcorpcode.trim())){
				EmpExecutionContext.error("员工通讯录,find方法session中获取企业编码出现异常");
				return;
			}
			Long userId =sysuser.getUserId();
			//判断当前登录ID
			if(userId==null){
				EmpExecutionContext.error("员工通讯录,find方法session中获取userId出现异常");
				return;
			}
			lgguid=sysuser.getGuId();
			//判断当前登录ID
			if(lgguid==null){
				EmpExecutionContext.error("员工通讯录,find方法session中获取lgguid出现异常");
				return;
			}
			
			boolean isDes = "1".equals(request.getParameter("isDes"));
			if(!isDes){
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("userId", String.valueOf(userId));
				//获取所有机构关联
				List<LfEmpDepConn> conn = baseBiz.getByCondition(LfEmpDepConn.class, conditionMap, null);
				if(conn != null && conn.size() > 0)
				{
					request.setAttribute("connDepId", conn.get(0).getDepId().toString());
				}else			
				{
					//跳转至没有权限页面
					request.getRequestDispatcher(empRoot  + "/employee/epl_nopermission.jsp").forward(request,
							response);
					return ;
				}
			}else{
				HttpSession session = request.getSession(false);
				LfEmployeeVo bookInfo = (LfEmployeeVo) session.getAttribute("employeeInfo");
				request.setAttribute("connDepId", bookInfo.getDepIds());
			}
			//查询出职位列表
			List<LfEmployeeTypeVo> optionList = employeeBiz.getAllEmployeeTypeVo(userId,lgcorpcode);
			request.setAttribute("optionList", optionList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lgguid", lgguid);
			request.setAttribute("lgusername", lgusername);
			request.getRequestDispatcher(empRoot + "/employee/epl_employeeBook.jsp").forward(request,
					response);
		} catch (Exception e) {
			try {
				request.getSession(false).setAttribute("error", e);
			} catch (Exception e1) {
				EmpExecutionContext.error(e1,"session为null");
			}
			EmpExecutionContext.error(e, "员工通讯录跳转页面失败！");
		}
	}

	/**
	 * @param request
	 * @param response
	 */
	//获取机构下人员信息
	public void getTable(HttpServletRequest request, HttpServletResponse response)
	{
		long startTime = System.currentTimeMillis();
		String userName = request.getParameter("userName");
		Long lgguid = null;
		LfSysuser sysUser = null;
		String corpCode = "";
		Long userId = null;
		try{
			
			// TODO 添加日志
			//添加日志，用于观察url请求参数获取的情况 p
			EmpExecutionContext.logRequestUrl(request, null);

			//获取登录sysuser
			sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工通讯录,find方法session中获取当前登录对象出现异常");
				return;
			}
			corpCode=sysUser.getCorpCode();
			//判断企业编码获取
			if(corpCode==null||"".equals(corpCode.trim())){
				EmpExecutionContext.error("员工通讯录,find方法session中获取企业编码出现异常");
				return;
			}
			userId =sysUser.getUserId();
			//判断当前登录ID
			if(userId==null){
				EmpExecutionContext.error("员工通讯录,find方法session中获取userId出现异常");
				return;
			}
			lgguid=sysUser.getGuId();
			//判断当前登录ID
			if(lgguid==null){
				EmpExecutionContext.error("员工通讯录,find方法session中获取lgguid出现异常");
				return;
			}
	
			/*去掉这个try catch，合为一个总的 
			}catch (Exception e) {
				//进入异常
				EmpExecutionContext.error(e, "员工通讯录获取当前操作员对象失败！");
			}
			try {
			*/
			
			HttpSession session=request.getSession(false);
			LfEmployeeVo bookInfo=new LfEmployeeVo();
			PageInfo pageInfo=new PageInfo();
			boolean isDes = "1".equals(request.getParameter("isDes"));
			String depId="";
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			if(!isDes){
				//是否第一次打开
				boolean isFirstEnter = false;
				isFirstEnter=pageSet(pageInfo, request);
				//机构ID
				depId=request.getParameter("depId");
				//****加密对象*****
				if(!isFirstEnter){
					bookInfo.setName(request.getParameter("name"));
					bookInfo.setMobile(request.getParameter("mobile"));
					
					//性别
					String sexy = request.getParameter("sexy");
					if(sexy != null && !"".equals(sexy)){
						bookInfo.setSex(Integer.valueOf(sexy));
					}
					//职位  名称
					String option = request.getParameter("option");
					if(option != null && !"".equals(option)){
						bookInfo.setDutyName(option);
					}
					
					//生日起始时间
					String submitSartTime = request.getParameter("submitSartTime");
					if(submitSartTime != null && !"".equals(submitSartTime)){
						submitSartTime = submitSartTime + " 00:00:00";
						bookInfo.setBeginbir(submitSartTime);
					}
					//生日结束时间
					String submitEndTime = request.getParameter("submitEndTime");
					if(submitEndTime != null && !"".equals(submitEndTime)){
						submitEndTime = submitEndTime + " 00:00:00";
						bookInfo.setEndbir(submitEndTime);
					}
					if(userName != null && !"".equals(userName)){
						bookInfo.setUserName(userName);
					}
				}
				bookInfo.setDepIds(depId);
				
			}else{
				bookInfo = (LfEmployeeVo) session.getAttribute("employeeInfo");
				pageInfo = (PageInfo) session.getAttribute("employeeBook_pageInfo");
			}
			//该机构下的人员信息
			/*List<LfEmployeeVo> bookInfoList = employeeBiz.findEmployeeVoByDepIds( userId,corpCode,bookInfo, pageInfo);*/
			List<LfEmployeeVo> bookInfoList = employeeBiz.findEmployeeVoByDepIds( userId,corpCode,bookInfo, pageInfo);
			session.setAttribute("bookInfoList", bookInfoList);
			
			HashMap<String,String> encryptmap =new HashMap<String,String>();
			//未知机构客户设置标识
			if(bookInfoList != null && bookInfoList.size()>0)
			{
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					//加密处理
					String employee_id="";
					for(LfEmployeeVo book:bookInfoList) 
					{
						employee_id = encryptOrDecrypt.encrypt(String.valueOf(book.getEmployeeId()));
							//通过实际值，加密值
						encryptmap.put(book.getEmployeeId()+"", employee_id);
					}
				}
				else
				{
					EmpExecutionContext.error("查询员工通讯录列表，从session中获取加密对象为空！");
				}
			}
			session.setAttribute("employeeInfo", bookInfo);
			//查询条件，存到session，导出excel时用
			request.setAttribute("bookType", "employee");
			request.setAttribute("pageInfo", pageInfo);
			session.setAttribute("employeeBook_pageInfo", pageInfo);
			request.setAttribute("encryptmap", encryptmap);

			//格式化时间
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			//操作日志信息
			String opContent = "查询："+sdf.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，机构ID:"+depId+"，总数：" + pageInfo.getTotalRec();
			EmpExecutionContext.info("员工通讯录", corpCode, String.valueOf(userId), sysUser.getUserName(), opContent, "GET");
			request.getRequestDispatcher(empRoot  + "/employee/epl_employeeTable.jsp").forward(request,
					response);
		} catch (Exception e) {
			try {
				request.getSession(false).setAttribute("error", e);
			} catch (Exception e1) {
				EmpExecutionContext.error(e1,"session为null");
			}
			EmpExecutionContext.error(e, "员工通讯录获取员工列表失败！");
		}
	}
	

	//新增修改员工信息
	public void add(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String bookId = request.getParameter("bookId");
		String oppType =null, opContent =null;
		String opUser = getOpUser(request);
		//机构ID
		String depId = request.getParameter("depId");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>() ;
		try{
			//新增时判断是否达到机构员工上限
			if (bookId == null || "".equals(bookId)){	
				int count = bookBiz.getEmployeesCount(depId);
				if(count >= StaticValue.MAX_PEOPLE_COUNT){
					request.setAttribute("result", "maxemp");
					toAddEmployeePage(request, response);
					return ;
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "处理员工机构下最大员工数失败！");
		}
		//名称
		String cName = request.getParameter("cName");
		//性别 
		String sex = request.getParameter("sex");
		//生日
		String birth = request.getParameter("birth");
		//QQ
		String qq = request.getParameter("qq");
		//MSN
		String msn = request.getParameter("msn");
		//EMAIL
		String EMail = request.getParameter("EMail");
		String oph = request.getParameter("oph");
		//职位
		String job = request.getParameter("job");
		//手机号码
		String mobile = request.getParameter("mobile");
		//传真
		String fax = request.getParameter("fax");
		String comments = request.getParameter("comments");
		String employeeNo = request.getParameter("employeeNo");
		String lgcorpcode="";
		//获取登录sysuser
		LfSysuser sysUser =depPriBiz.getCurrenUser(request);
		if(sysUser==null){
			EmpExecutionContext.error("员工通讯录,find方法session中获取当前登录对象出现异常");
			return;
		}
		lgcorpcode=sysUser.getCorpCode();
		//判断企业编码获取
		if(lgcorpcode==null||"".equals(lgcorpcode.trim())){
			EmpExecutionContext.error("员工通讯录,find方法session中获取企业编码出现异常");
			return;
		}
		
		LfEmployee lfEmployee = null;
		LfEmployee _lfEmployee = null;
		if (bookId != null && !"".equals(bookId)){
			bookId=getDecryptValue(request, bookId);
			lfEmployee = baseBiz.getById(LfEmployee.class, bookId);
			_lfEmployee = (LfEmployee) BeanUtils.cloneBean(lfEmployee);
		}else{
			lfEmployee = new LfEmployee();
			Long guid = Long.parseLong(request.getParameter("eguid"));
			lfEmployee.setGuId(guid);
			lfEmployee.setCorpCode(lgcorpcode);
		}
		lfEmployee.setDepId(Long.parseLong(depId));
		lfEmployee.setName(cName);
		if(employeeNo!=null){
			lfEmployee.setEmployeeNo(employeeNo.toUpperCase());
		}
		lfEmployee.setSex(Integer.parseInt(sex));
		lfEmployee.setMobile(mobile);
		lfEmployee.setQq(qq);
		lfEmployee.setMsn(msn);
		lfEmployee.setEmail(EMail);
		lfEmployee.setOph(oph);
		lfEmployee.setFax(fax);
		lfEmployee.setDuties(job);
		lfEmployee.setCommnets(comments);
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 

		if (birth != null && !"".equals(birth)) {
			//lfEmployee.setBirthday(Timestamp.valueOf(birth + " 00:00:00"));
			try {
				lfEmployee.setBirthday(new Timestamp(sdf.parse(birth + " 00:00:00").getTime()));
			} catch (ParseException e) {
				EmpExecutionContext.error(e, "员工生日格式转化失败！");
			}
		}
		
		String haveSubno = request.getParameter("haveSubno");
		String userDepId = request.getParameter("userDepId");
		String isUser = request.getParameter("isUser");
		EmpExecutionContext.info("员工通讯录,添加员工,传入参数，haveSubno="+haveSubno+",userDepId="+userDepId+",isUser="+isUser);
		LfSysuser lfSysuser = baseBiz.getByGuId(LfSysuser.class, lfEmployee.getGuId());
		//操作员状态
		int userState = 1;
		//控制标识位
		String controlFlag = "0000";
		if(lfSysuser !=null)
		{
			userState = Integer.parseInt(lfSysuser.getUserState().toString());
			controlFlag = lfSysuser.getControlFlag().toString();
		}
		Long[] roleIds = null;
		Long domDepId = null;
		String[] employDepId = null;
		String[] clientDepId = null ;
		//判断是否有权限新增操作员或者修改操作员
		String isFlagPermission = request.getParameter("isFlagPermission");
		if("1".equals(isUser)){
			lfEmployee.setIsOperator(1);
			String username = request.getParameter("userName");
			if(lfSysuser == null){
				lfSysuser = new LfSysuser();
				lfSysuser.setGuId(lfEmployee.getGuId());
				lfSysuser.setUserName(username);
				lfSysuser.setUserCode(request.getParameter("userCode"));
				lfSysuser.setPassword(MD5.getMD5Str(username + username.toLowerCase()));
			}
			lfSysuser.setUserType(2);
			lfSysuser.setUserState(userState);
			if(!"1".equals(haveSubno)){
				lfSysuser.setIsExistSubNo(2);
			}
			if (birth != null && !"".equals(birth)) {
				//lfSysuser.setBirthday(Timestamp.valueOf(birth + " 00:00:00"));
				try {
					lfSysuser.setBirthday(new Timestamp(sdf.parse(birth + " 00:00:00").getTime()));
				} catch (ParseException e) {
					EmpExecutionContext.error(e, "操作员生日格式转化失败！");
				}
			}
			String isReviewer = request.getParameter("isReviewer");
			String isaudited = request.getParameter("isaudited");
			lfSysuser.setDepId(Long.parseLong(userDepId));
			lfSysuser.setName(cName);
			lfSysuser.setSex(Integer.parseInt(sex));
			lfSysuser.setMobile(mobile);
			lfSysuser.setQq(qq);
			lfSysuser.setMsn(msn);
			lfSysuser.setEMail(EMail);
			lfSysuser.setOph(oph);
			lfSysuser.setFax(fax);
			lfSysuser.setDuties(job);
			lfSysuser.setComments(comments);
			lfSysuser.setCorpCode(lgcorpcode);
			lfSysuser.setIsReviewer(Integer.parseInt(isReviewer));
			lfSysuser.setIsAudited(Integer.valueOf(isaudited));
			lfSysuser.setControlFlag(controlFlag);
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, request.getParameter("lguserid"));
			lfSysuser.setHolder(sysuser.getName());
			lfSysuser.setPermissionType(Integer.parseInt(request.getParameter("userPerType")));
			
			//有登录系统权限，转操作员
			String[] roleId = request.getParameter("cheRoles").split(",");
			
			roleIds=new Long[roleId.length];
			for (int r = 0; r < roleIds.length; r++) {
				roleIds[r] = Long.valueOf(roleId[r]);
			}
			
			//机构权限
			if("2".equals(request.getParameter("userPerType"))){
				domDepId = Long.valueOf(request.getParameter("domDepId"));
			}
			if("2".equals(request.getParameter("employPerType"))){
				String domDepId_employ = request.getParameter("domDepId_employ");
				//employDepId = domDepId_employ.split(",");
				employDepId = getEmployeeDepIds(domDepId_employ);
			}
			if("2".equals(request.getParameter("clientPerType"))){
				String domDepId_client = request.getParameter("domDepId_client");
				//clientDepId = domDepId_client.split(",");
				clientDepId = getClientDepIds(domDepId_client);
			}
			
		}else if("0".equals(isUser)){
			//表示非操作员
			lfEmployee.setIsOperator(0);
			//当有这个 权限设置操作员的时候
			if("2".equals(isFlagPermission) && lfSysuser != null){
				lfSysuser.setUserType(0);
				lfSysuser.setUserState(2);
				baseBiz.updateObj(lfSysuser);
				lfSysuser = null;
			}
		
		}else{
			
		}
		
		boolean result = false;
		oppType= StaticValue.ADD;
		opContent = "新建员工（员工名称："+cName+"）";
		try{
			//新建员工
			if(bookId == null || "".equals(bookId)){
				if("0".equals(isUser)){
					result = baseBiz.addObj(lfEmployee);
				}else if("1".equals(isUser)){
					result = new SysuserPriBiz().add(domDepId, roleIds, lfSysuser,lfEmployee,employDepId,clientDepId);
					if(result){	
						if("1".equals(haveSubno)){
							//修改的尾号
							String addSubno = request.getParameter("addSubno");  
							//新增时赋予的尾号
							String subno2 = request.getParameter("subno2");		
							lfSysuser.setIsExistSubNo(1);
							String isRepeatSubno = "1";
							if(!subno2.equals(addSubno)){
								isRepeatSubno = new SysuserPriBiz().updateUserSubno(lfEmployee.getGuId(),subno2,addSubno, lgcorpcode);
								if("2".equals(isRepeatSubno)){
//									lfSysuser.setIsExistSubNo(2);
								}
							}else{
								new SubnoManagerBiz().updateSubnoStat(addSubno,lgcorpcode, null);
							}
							baseBiz.updateObj(lfSysuser);
						}
					}
				}else{
					result=false;
				}
			}else{
				//修改员工 
				//当没有修改操作员的权限的话，那么不允许修改
				if("1".equals(isFlagPermission)){
					lfSysuser = null;
				}
				result = new SysuserPriBiz().update(domDepId, roleIds, lfSysuser, lfEmployee, employDepId, clientDepId);
				if(result){
					try{
						if(birth == null || "".equals(birth)){
							conditionMap.clear();
							conditionMap.put("employeeId",String.valueOf(bookId));
							conditionMap.put("corpCode",lgcorpcode);
							LinkedHashMap<String,String> objectMap = new LinkedHashMap<String, String>();
							objectMap.put("birthday",null);
							baseBiz.update(LfEmployee.class, objectMap, conditionMap);
						}
						if("1".equals(haveSubno)){
							//修改的尾号
							String updateSubno = request.getParameter("addSubno");  
							//新增时赋予的尾号
							String subno = request.getParameter("subno2");		
							lfSysuser.setIsExistSubNo(1);
							String isRepeatSubno = "1";
							if(!subno.equals(updateSubno)){
								isRepeatSubno = new SysuserPriBiz().updateUserSubno(lfEmployee.getGuId(),subno,updateSubno, lgcorpcode);
								if("2".equals(isRepeatSubno)){
//									lfSysuser.setIsExistSubNo(2);
								}
							}else{
								new SubnoManagerBiz().updateSubnoStat(updateSubno,lgcorpcode, null);
							}
							baseBiz.updateObj(lfSysuser);
						}
					}catch (Exception e) {
						EmpExecutionContext.error(e, "员工通讯录更新员工生日出现异常 ！");
					}
				}
			}
			if (result) {
				spLog.logSuccessString(opUser, opModule, oppType, opContent,lgcorpcode);
				if(bookId == null || "".equals(bookId)){//新增
					opSucLog(request, opModule, "新增员工（姓名："+lfEmployee.getName()+"，手机号码："+lfEmployee.getMobile()+"，机构："+lfEmployee.getDepId()+"）成功。", "ADD");
				}else{//修改
					opSucLog(request, opModule, "修改员工[姓名，生日，性别，手机号码，机构]"+"("+_lfEmployee.getName()+","+_lfEmployee.getBirthday()+","+_lfEmployee.getSex()+","+_lfEmployee.getMobile()+","+_lfEmployee.getDepId()+")"+
							"-->("+lfEmployee.getName()+","+lfEmployee.getBirthday()+","+lfEmployee.getSex()+","+lfEmployee.getMobile()+","+lfEmployee.getDepId()+")成功。"
							, "UPDATE");
				}
				request.setAttribute("result", "true");
			}else {	
				//失败 
				spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, null,lgcorpcode);
			}
			request.setAttribute("employee", lfEmployee);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "员工通讯录操作员工出现异常！");
			request.setAttribute("result", "false");
		}finally{
			if(bookId == null || "".equals(bookId)){
				toAddEmployeePage(request, response);
			}else{
				doEdit(request, response);
			}
			
		}
	}
	
	/**
	 * 
	 * 是否是没有被调用的方法？
	 * @param request
	 * @param response
	 * @throws java.io.IOException
	 * @throws javax.servlet.ServletException
	 */
	//新增与修改员工信息
	public void addBook(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String basePath = "/employee";
		String oppType =null, opContent =null;
		String opUser = getOpUser(request);
		String url="";
		try
		{
			//手机号码
			String mobile = request.getParameter("mobile");
			LfEmployee lc = new LfEmployee();
			//类别
			String bookId = request.getParameter("bookId");
			//用户ID
			String userId = request.getParameter("userId");
			//GUID
			String userGuId = request.getParameter("guId");
			String lgcorpcode=request.getParameter("lgcorpcode");
			//String lguserid=request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String lguserid = SysuserUtil.strLguserid(request);


            if (bookId != null && !"".equals(bookId))
			{
				lc = baseBiz.getById(LfEmployee.class, bookId);
			}
			else if (userId != null && !"".equals(userId))
			{
				lc.setGuId(Long.parseLong(userGuId));
			}
			else
			{
				Long guid= globalBiz.getValueByKey("guid", 1L);
				lc.setGuId(guid);
			}
			String employeeNo = request.getParameter("employeeNo");
			//机构ID
			String depId = request.getParameter("depId");
			
			if(depId != null && !"".equals(depId)){
				//BaseBiz baseBiz = new BaseBiz();
				//LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
				//map.put("corpCode", getCorpCode());
				//map.put("depCode", depId);
				//List<LfEmployeeDep> LfEmployeeDeps = baseBiz.getByCondition(LfEmployeeDep.class, map, null);
				//if(LfEmployeeDeps != null && LfEmployeeDeps.size()>0){
				//	LfEmployeeDep dep = LfEmployeeDeps.get(0);
				//	lc.setDepId(dep.getDepId());
				//}
				lc.setDepId(Long.parseLong(depId));
			}else{
				lc.setDepId(new Long("1"));
			}
			
			
			//名称
			String cName = request.getParameter("cName");
			//性别 
			String sex = request.getParameter("sex");
			//生日
			String birth = request.getParameter("birth");
			//QQ
			String qq = request.getParameter("qq");
			//MSN
			String msn = request.getParameter("msn");
			//EMAIL
			String EMail = request.getParameter("EMail");
			String oph = request.getParameter("oph");
			//职位
			String job = request.getParameter("job");
			//传真
			String chuanz = request.getParameter("chuanz");
			String comm = request.getParameter("comments");
			if (!"".equals(birth))
			{
				lc.setBirthday(Timestamp.valueOf(birth+" 00:00:00"));
			}else{
				lc.setBirthday(null);
			}
			lc.setEmployeeNo(employeeNo.toUpperCase());
			//将员工号转换成大写
			//lc.setDepId(new Long("1"));
			//lc.setDepCode(depId);
			if ("".equals(cName))
			{
				lc.setName(employeeNo);
			}
			else
			{
				lc.setName(cName);
			}
			lc.setSex(Integer.parseInt(sex));
			lc.setMobile(mobile);
			lc.setQq(qq);
			lc.setMsn(msn);
			lc.setEmail(EMail);
			lc.setOph(oph);
			lc.setFax(chuanz);
			lc.setCommnets(comm);
			lc.setCorpCode(lgcorpcode);
			//增加企业标号
			
			boolean result = false;
			//修改员工
			if (bookId != null && !"".equals(bookId))
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>() ;
				conditionMap.put("depId", depId);
				
				List<LfEmployee> employeeDeps = new BaseBiz().getByCondition(LfEmployee.class, conditionMap, null);
				List<LfEmployeeTypeVo> bookInfoList = employeeBiz.getAllEmployeeTypeVo( Long.parseLong(lguserid),lgcorpcode);
				request.setAttribute("zwList", bookInfoList);
				basePath = "/employee";
				if(employeeDeps.size() >= StaticValue.MAX_PEOPLE_COUNT){
					url=  "/epl_editEmployee.jsp";
					request.setAttribute("result", "maxemp");
				}else{
					lc.setDuties(job);
					url=  "/epl_editEmployee.jsp";
					result = employeeBiz.updateEmployee(lc);
					if(result){
						try{
							if(lc.getBirthday() == null || "".equals(birth)){
								conditionMap.clear();
								LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>() ;
								objectMap.put("birthday","");
								conditionMap.put("employeeId", lc.getEmployeeId().toString());
								new BaseBiz().update(LfEmployee.class, objectMap, conditionMap);
							}
						}catch (Exception e) {
							EmpExecutionContext.error(e, "更新员工日期出现异常！");
						}
					}
					
				}
				oppType= StaticValue.UPDATE;
				opContent="修改员工（员工名称:"+cName+"）";
			}
			else
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>() ;
				conditionMap.put("depId", depId);
				
				List<LfEmployee> employeeDeps = new BaseBiz().getByCondition(LfEmployee.class, conditionMap, null);
				
				//System.out.println(employeeDeps.size());
				List<LfEmployeeTypeVo> bookInfoList = employeeBiz.getAllEmployeeTypeVo( Long.parseLong(lguserid),lgcorpcode);
				request.setAttribute("zwList", bookInfoList);
				if (userId != null && !"".equals(userId)){
					//判断是从操作员转员工
					lc.setIsOperator(1);
					url=  "/opt_editEmployee.jsp";
					basePath = "/operator";
				}else{
					url=  "/epl_addEmployee.jsp";
					basePath = "/employee";
				}
				if(employeeDeps.size() >= StaticValue.MAX_PEOPLE_COUNT){
					request.setAttribute("result", "maxemp");
				}else{
					lc.setDuties(job);
					result = baseBiz.addObj(lc);
				}
				oppType= StaticValue.ADD;
				opContent = "新建员工（员工名称："+cName+"）";
			}
			if (result)
			{   
				if (userId != null && !"".equals(userId))
				{
					LfSysuser user = baseBiz.getById(LfSysuser.class, userId);
					user.setUserType(2);
					user.setUserState(1);
					baseBiz.updateObj(user);
					request.setAttribute("user", user);
					
					LfEmployeeDep empDep = baseBiz.getById(LfEmployeeDep.class, lc.getDepId());
					request.setAttribute("depName", empDep == null?"":empDep.getDepName());
				}
				spLog.logSuccessString(opUser, opModule, oppType, opContent,lgcorpcode);
				request.setAttribute("result", "true");
			}
			request.setAttribute("employee", lc);
			request.getRequestDispatcher(empRoot + basePath + url).forward(request,
					response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "修改员工失败！");
			request.setAttribute("result", "false");
			request.getRequestDispatcher(empRoot + basePath + url).forward(request,
					response);
		}
	}
	//员工上传的检测是否有号码重复
	public void checkUploadBook(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String depId = "";
		LfSysuser lfSysuser = null;
		try {
			lfSysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
		} catch (Exception e1) {
			EmpExecutionContext.error(e1,"session为null");
		}
		boolean phoneRepeat = false;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> fileList = null;
		try
		{
			fileList = upload.parseRequest(request);
		} catch (FileUploadException e)
		{
			EmpExecutionContext.error(e, "员工通讯录上传员工文件出现异常！");
		}
		Iterator<FileItem> it = fileList.iterator();
		boolean result = false;
		while (it.hasNext())
		{
			FileItem fileItem = it.next();
			String fileName = fileItem.getFieldName();
			if (fileName.equals("depId"))
			{
				try {
					depId = fileItem.getString("UTF-8").toString();
				} catch (UnsupportedEncodingException e) {
					EmpExecutionContext.error(e, "获取员工机构ID失败！");
					request.setAttribute("result", "error");
					break;
				}
			}
			else if (!fileItem.isFormField()
					&& fileItem.getName().length() > 0)
			{
				BufferedReader reader = null;
				String tmp;

				try
				{
					String fileCurName = fileItem.getName();
					String fileType = fileCurName.substring(fileCurName.lastIndexOf("."));
					if(fileType.equals(".xls")){
						LinkedHashMap<String , String > conMap=new LinkedHashMap<String, String>();
						conMap.put("corpCode", lfSysuser.getCorpCode());
						List<LfEmployee> empList = baseBiz.getByCondition(LfEmployee.class, conMap, null);
						HashSet<String> repeatList = new HashSet<String>();
						if(empList!=null && empList.size()>0)
						{
							for(LfEmployee employee : empList)
							{
								repeatList.add(employee.getMobile());								
							}
						}
						
						Workbook workBook = Workbook.getWorkbook(fileItem.getInputStream());
						Sheet sh = workBook.getSheet(0);
						
						for (int k = 1; k < sh.getRows(); k++)
						{
							Cell[] cells = sh.getRow(k);
							int size = cells.length;
							if(size>2) 
							{
							  String phone = cells[2].getContents().trim();
							  if(phone !=null && !"".equals(phone))
							  {
								  if(repeatList.contains(phone))
								  {
									  phoneRepeat = true;
									  response.getWriter().print("<script>parent.UploadEmployee('phoneExist');</script>");
	                                  return;
								  }else
								  {
									  repeatList.add(phone);
								  }
							  }
							}
							
						}
					}
				}catch (Exception e) {
					EmpExecutionContext.error(e, "员工号码检测异常！");
				}
			}
		}
		response.getWriter().print("<script>parent.UploadEmployee('S');</script>");		
		
	}
	public String getWebRoot()
	{
		String realUrl = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String newUrl = "";
		if (realUrl.contains("/WEB-INF/"))
		{
			newUrl = realUrl.substring(0, realUrl.lastIndexOf("WEB-INF/"));
		}
		realUrl = newUrl.replace("%20", " ");////此路径不兼容jboss
		return realUrl;
	}
	public String[] getEmpUploadUrl(int id, Date time)
	{
		
		/*String uploadPath = StaticValue.EMP_UPLOAD;*/
		String uploadPath = excelPath + "upload/";
		//存放路径的数组
		String[] url = new String[5];
		String saveName = "1_" + id + "_"+ (new SimpleDateFormat("yyyyMMddHHmmssS")).format(time)+ ".txt";
		String logicUrl;
		String physicsUrl = this.getWebRoot();
		physicsUrl = physicsUrl + uploadPath + saveName;
		logicUrl = uploadPath + saveName;
		url[0] = physicsUrl;
		url[1] = logicUrl;
		return url;
	}
	/**
	 * @param request
	 * @param response
	 */
	//上传员工文件
	public void uploadBook(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long start = System.currentTimeMillis();
		
		//添加与日志相关
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		long startTimeByLog = System.currentTimeMillis();  //解析上传文件开始时间
		
		//excel有效行数
		int rows = 0;
		//导入前机构员工总数
		int employeeSize = 0;
		//导入成功条数
		int resultCount=0;
		String oppType =null, opContent =null;
		//获取操作员名称
		String opUser = getOpUser(request);
		//"新建"
		oppType= StaticValue.ADD;
		String depId = "";
		Pattern pa = Pattern.compile(" {2,}");
		HttpSession session = request.getSession();
		
		//创建DiskFileItemFactory
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//创建文件上传解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		List<FileItem> fileList = null;
		try
		{
			//解析上传数据
			fileList = upload.parseRequest(request);
			
			//结束时间
			long end = System.currentTimeMillis();
			
			//添加日志，上传文件有时会出现“FileUploadException:Read timed out”文件上传超时的异常，因此添加日志记录解析上传文件的耗时，看看耗时多久会出现超时的情况，从而进一步排查问题
			long endTimeByLog = System.currentTimeMillis();  //解析上传文件结束时间
			long consumeTimeByLog = endTimeByLog - startTimeByLog;  //解析上传文件耗时
			
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent1 = "上传员工文件，上传文件解析开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms";
				
				EmpExecutionContext.info("员工通讯录", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "GET");
			}
			
			
			
		} catch (FileUploadException e)
		{
			//添加日志，上传文件有时会出现“FileUploadException:Read timed out”文件上传超时的异常，因此添加日志记录解析上传文件的耗时，看看耗时多久会出现超时的情况，从而进一步排查问题
			long endTimeByLog = System.currentTimeMillis();  //解析上传文件结束时间
			long consumeTimeByLog = endTimeByLog - startTimeByLog;  //解析上传文件耗时
			String opContent1 = "上传员工文件出现异常 ！，上传文件解析开始时间："+sdf.format(startTimeByLog)+"，耗时："+consumeTimeByLog+"ms";
			
			EmpExecutionContext.error(e, opContent1);
		}
		Iterator<FileItem> it = fileList.iterator();
		boolean result = false;
		String lgcorpcode = "";
		String lguserid = "";
		String lgguid = "";
		
		//获取登录sysuser
		LfSysuser sysUser =depPriBiz.getCurrenUser(request);
		if(sysUser==null){
			EmpExecutionContext.error("员工通讯录,uploadBook方法session中获取当前登录对象出现异常");
			return;
		}
		lgcorpcode=sysUser.getCorpCode();
		//判断企业编码获取
		if(lgcorpcode==null||"".equals(lgcorpcode.trim())){
			EmpExecutionContext.error("员工通讯录,uploadBook方法session中获取企业编码出现异常");
			return;
		}
		lguserid=sysUser.getUserId()+"";
		//判断操作员获取
		if(lguserid==null||"".equals(lguserid.trim())){
			EmpExecutionContext.error("员工通讯录,uploadBook方法session中获取登录操作员ID出现异常");
			return;
		}
		lgguid=sysUser.getGuId()+"";
		//判断企业编码获取
		if(lgguid==null||"".equals(lgguid.trim())){
			EmpExecutionContext.error("员工通讯录,uploadBook方法session中获取登录lgguid出现异常");
			return;
		}
		
		request.setAttribute("lgguid",lgguid);
		int checkFlag = Integer.parseInt(request.getParameter("checkFlag"));
		Date time = Calendar.getInstance().getTime();
		String[] url = this.getEmpUploadUrl((int)(Long.parseLong(lguserid)-0), time);
		//获取所有的 号段
		String[] haoduan = employeeBiz.getHaoduan();
		
		//获取该机构下的职位
	/*	LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
		condition.put("corpcode",lgcorpcode);
		List<LfEmployeeType> typeList = baseBiz.getByCondition(LfEmployeeType.class, condition, null);
		LinkedHashSet<String> jobSet = new LinkedHashSet<String>();
		if(typeList != null && typeList.size()>0){
			for(LfEmployeeType type:typeList){
				jobSet.add(type.getName().trim());
			}
		}*/
		// 国际化
		String di = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_99", request);
		String hang = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_100", request);
		String yghcf = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_101", request);
		String xmwk = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_102", request);
		String xmff = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_103", request);
		String hmcf = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_104", request);
		String hmwk = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_105", request);
		String cgscl = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_106", request);
		String tjl = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_107", request);
		String glhm = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_108", request);
		String tiao = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_109", request);
		
		String nan = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_11", request);
		String nv = com.montnets.emp.i18n.util.MessageUtils.extractMessage("employee", "employee_dxzs_title_12", request);
		
		while (it.hasNext())
		{
			FileItem fileItem =  it.next();
			//获得表单标签的name属性的值
			String fileName = fileItem.getFieldName();
			//如果name是所属机构
			if (fileName.equals("depId"))
			{
				try {
					//解决数据中文乱码问题
					depId = fileItem.getString("UTF-8").toString();
				} catch (UnsupportedEncodingException e) {
					EmpExecutionContext.error(e, "获取员工机构出现异常！");
					request.setAttribute("result", "error");
					break;
				}
			}
			//对上传文件进行处理
			else if (!fileItem.isFormField()
					&& fileItem.getName().length() > 0)
			{
				BufferedReader reader = null;
				String tmp;
				Workbook workBook = null;
				Connection conn = null;
				try
				{
					//上传文件名称
					String fileCurName = fileItem.getName();
					//文件类型
					String fileType = fileCurName.substring(fileCurName.lastIndexOf("."));
					//如果是.xls文件
					if(fileType.equals(".xls")){
						//重复数
						int repeatNum=0;
						//通过公司编号获得员工信息
						List<LfEmployee> employees = bookBiz.getAllEmployees(lgcorpcode);
						Set<String> noList = new HashSet<String>();
						Set<String> phones = new HashSet<String>();
						Set<String> phonenames = new HashSet<String>();
						Iterator<LfEmployee> its = employees.iterator();
						while(its.hasNext()){
							LfEmployee obj = its.next();
							if(depId.equals(obj.getDepId().toString())){
								employeeSize++;
								if(checkFlag==1){
									phones.add(obj.getMobile());
								}
								phonenames.add(obj.getMobile()+obj.getName());
							}
								noList.add(obj.getEmployeeNo());
						}
						its = null;
						//获得工作薄（Workbook）
						workBook = Workbook.getWorkbook(fileItem.getInputStream());
						//获得工作薄（Workbook）中工作表（Sheet）
						Sheet sh = workBook.getSheet(0);
						StringBuffer contentSb = new StringBuffer();
						for(int i=1;i<sh.getRows();i++){
							//获取某一行的所有单元格
							if(sh.getRow(i).length<3){
								break;
							}
							rows++;
						}
						//一个机构下员工或者操作员的最大数目，50000
						if(employeeSize+rows> StaticValue.MAX_PEOPLE_COUNT){
							request.setAttribute("result", "maxemp");
						}else{
							long maxguid = globalBiz.getValueByKey("guid", rows);
							List<LfEmployee> employeeList = null;
								for (int k = 1; k <= rows; k++)
								{
									if(employeeList == null){
										employeeList = new  ArrayList<LfEmployee>();
									}
									//获取某一行的所有单元格
									Cell[] cells = sh.getRow(k);
									//单元格的长度
									int size = cells.length;
									LfEmployee employee= new LfEmployee();
									if(size<3){
										break;
									}
									
									//员工工号
									String employeeNo = cells[0].getContents();
									//关键字过滤
									if(employeeNo!=null){
										employeeNo=employeeNo.replaceAll("['\"<>]", "");
									}
									//重复处理
									if(employeeNo!=null && !"".equals(employeeNo))
									{
										//确保nolist里面的员工工号不重复
										if(noList.add(employeeNo.toUpperCase()))
										{
											employee.setEmployeeNo(employeeNo.toUpperCase());//将员工号转换成大写
										}else {
											contentSb.append(di+(k+1)+hang+"   ").append("     "+yghcf).append(line);
											//重复数
											repeatNum++;
											continue;
										}
									}else
									{
										employee.setEmployeeNo("");
									}
									//员工姓名
									if(cells[1].getContents() == null || "".equals(cells[1].getContents()))
									{
										contentSb.append(di+(k+1)+hang+"   ").append("     "+xmwk).append(line);
										repeatNum++;
										continue;
									}
									//判断是否存在非法字符
									else if(pattern.matcher(cells[1].getContents()).find())
									{
										contentSb.append(di+(k+1)+hang+"   ").append("     "+xmff).append(line);
										repeatNum++;
										continue;
									}
									else
									{
										employee.setName(cells[1].getContents());
									}
									//手机号码
									String phoneStr = cells[2].getContents().trim();
									if (phoneStr.length() < 32 && phoneStr.length()>0) 
									{
										//判断手机号+姓名是否重复
										if(checkRepeat(phoneStr, employee.getName(), phones,phonenames, checkFlag))
										{
											contentSb.append(di+(k+1)+hang+"   ").append(phoneStr).append("     "+hmcf).append(line);
											repeatNum++;
											continue;
										}
										else 
										{
											employee.setMobile(phoneStr);
											//支持国际化号码 PhoneUtil
											PhoneUtil util=new PhoneUtil();
											//验证号码是否合法，是否存在于运营商号段，获取运营商标识
											int re = util.getPhoneType(employee.getMobile(),haoduan);	
											
//											int re = employeeBiz.checkMobile(employee.getMobile(), haoduan);
											//-1: 非法号码
											if (re == -1) 
											{
												continue;
											}
										}
									} else {
										contentSb.append(di+(k+1)+hang+"   ").append("     "+hmwk).append(line);
										repeatNum++;
										continue;
									}
									//如果已发现存在重复的手机号码。则无需继续验证
									/*if(!phoneRepeat)
									{
										if(repeatList.contains(employee.getMobile())){
											phoneRepeat = true;
											//continue;
										}else{
											repeatList.add(employee.getMobile());
										}
									}*/
									
									/*if(size>3) {
										String duty = cells[3].getContents();
										if(duty != null && !"".equals(duty)){
											if(jobSet.size()>0 && jobSet != null && jobSet.contains(duty.trim())){
												employee.setDuties(duty);
											}else{
												contentSb.append("第"+(k+1)+"行      ").append("     职位不存在").append(line);
												repeatNum++;
												continue;
											}
										}else{
											employee.setDuties(duty);
										}
									}*/
									//职位
									if(size>3) {
										employee.setDuties(cells[3].getContents());
									}
									//性别
									if(size>4) employee.setSex((cells[4].getContents()!=null &&
											cells[4].getContents().equals(nan))?1:
												(cells[4].getContents()!=null && cells[4].getContents().equals(nv))?0:2);
									else {
										 employee.setSex(2);
									}
									//出生日期
									if(size>5)
									{
										try
										{
											//if (cells[5].getContents() != null && !cells[5].getContents().trim().equals(""))  //备份
											if (cells[5].getContents() != null && !("".equals(cells[5].getContents().trim())))  //修改
											{

												Cell c00 = cells[5];
												String birth = "";
												if (c00.getType() == CellType.LABEL)
												{
													birth = c00.getContents();
													/*
													//新增 -------------------------------------------------------------------------------
													1999年09月09
													1999/09/09
													1999-09-9
													1999-09-09
													1999-09-09
													1999-09-09
													--------------------
													1999-09-09
													*/ 
													if(birth.length()>=8){
														StringBuffer sbBirth = new StringBuffer(birth.trim());
//														if(sbBirth.charAt(sbBirth.length()-1)=='日'){
//															sbBirth.deleteCharAt(sbBirth.length()-1);  
//														}
														
														if(sbBirth.charAt(6)=='-'||sbBirth.charAt(6)=='/'){
															sbBirth.insert(5, '0');
															 if(sbBirth.length()==9){
																 sbBirth.insert(8, '0');
															 }
														}
														if((sbBirth.charAt(7)=='-'||sbBirth.charAt(7)=='/')&&sbBirth.length()==9){
															sbBirth.insert(8, '0');
														}
														sbBirth.replace(4, 5, "-");
														sbBirth.replace(7, 8, "-");
														birth = sbBirth.toString();
													}
													//-------------------------------------------------------------------------------------
													
												} else if (c00.getType() == CellType.DATE)
												{
													DateCell addsss = (DateCell) cells[5];
													Date date = addsss.getDate();
													if (date != null)
													{
														birth = new SimpleDateFormat("yyyy-MM-dd ")
																.format(date);
													}
												}
												
												
												
												/*备份
												if(birth != null && birth.length() >= 10 ) {
													Timestamp BIRTHDAY = new Timestamp(
															Integer.parseInt(birth.substring(0, 4)) - 1900,
															Integer.parseInt(birth.substring(5, 7)) - 1,
															Integer.parseInt(birth.substring(8, 10)),
															0, 0, 0, 0);
													employee.setBirthday(BIRTHDAY);
												}
												*/
												/*
												//新增   正则表达式对日期进行判断-----------------------------------------------------------
												*/
												/*该日期正则表达式较复杂，但对日期匹配较标准，但是性能方面可能有影响，所以使用了下面简单形式的日期正则表达式
												Pattern p = Pattern
												.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\-\\s]?((((0?"
														+ "[13578])|(1[02]))[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))"
														+ "|(((0?[469])|(11))[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])|(30)))|"
														+ "(0?2[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12"
														+ "35679])|([13579][01345789]))[\\-\\-\\s]?((((0?[13578])|(1[02]))"
														+ "[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))"
														+ "[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\-\\s]?((0?["
														+ "1-9])|(1[0-9])|(2[0-8]))))))");
												*/
												/*日期正则表达式*/
												Pattern p = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
												//如果为true，生日匹配正确
												boolean birthRight = p.matcher(birth).matches();
												//----------------------------------------------------------------------------------------
												
												//修改 -----------------------------------------------------------------------------------
												if(birth != null && !("".equals(birth.trim())) && birth.trim().length() == 10 && birthRight) {	
													Timestamp BIRTHDAY = new Timestamp(
															Integer.parseInt(birth.trim().substring(0, 4)) - 1900,
															Integer.parseInt(birth.trim().substring(5, 7)) - 1,
															Integer.parseInt(birth.trim().substring(8, 10)),
															0, 0, 0, 0);
													employee.setBirthday(BIRTHDAY);
													
												}
												//----------------------------------------------------------------------------------------
												
											}
										} catch (Exception eee)
										{
											EmpExecutionContext.error(eee, "处理上传员工文件中的生日出现异常 ！");
//											continue;
										}
									}
									//E-mail
									if(size>6) employee.setEmail(cells[6].getContents());
									//MSN
									if(size>7) employee.setMsn(cells[7].getContents());
									//座机
									if(size>8) employee.setOph(cells[8].getContents());
									//QQ
									if(size>9) employee.setQq(cells[9].getContents());
									//员工描述
									if(size>10) employee.setCommnets(cells[10].getContents());
									//传真
									if(size>11) employee.setFax(cells[11].getContents());
									if(depId != null){
										employee.setDepId(Long.parseLong(depId));
									}else{
										employee.setDepId(new Long(1));
									}
									//employee.setDepCode(depId);
									//员工状态（0无效，1有效）
									employee.setEstate(1);
									//通讯录唯一性标识
									employee.setGuId(maxguid-1);
									maxguid--;
									//企业编码
									employee.setCorpCode(lgcorpcode);
									
									employeeList.add(employee);
									if(employeeList.size()==5000){
										if(conn == null){
											conn = baseBiz.getConnection();
											baseBiz.beginTransaction(conn);
										}
										//保存一个list集合中的所有对象
										resultCount += baseBiz.addList(conn,LfEmployee.class, employeeList);
										employeeList = null;
									}
								}
								if(employeeList!=null&&employeeList.size()>0)
								{
									if(conn == null){
										conn = baseBiz.getConnection();
										baseBiz.beginTransaction(conn);
									}
									resultCount +=baseBiz.addList(conn,LfEmployee.class, employeeList);
								}
								//全部数据添加到数据库
								if(resultCount == 0){
									request.setAttribute("result", "noRecord");
								}else{
									baseBiz.commitTransaction(conn);
									request.setAttribute("result", "upload"+resultCount);
									opContent = "批量导入员工（共导入员工："+resultCount+"条）";
									spLog.logSuccessString(opUser, opModule, oppType, opContent,lgcorpcode);
									opSucLog(request, opModule, opContent+"成功。","ADD");
									//写文件
									String FileStr = url[0];
									//临时文件路径
									String FileStrTemp = FileStr.substring(0, FileStr.indexOf(".txt"))+"_emp"+".txt";
									new TxtFileUtil().writeToTxtFile(FileStrTemp,new StringBuffer(cgscl).append(resultCount).append(tjl).append(line).append(glhm).append(repeatNum).append(tiao).append(line).append(contentSb).toString());
									/*request.setAttribute("path",FileStrTemp.substring(FileStrTemp
											.indexOf("yhgl")));*/
									String pathTemp = url[1].substring(0, url[1].indexOf(".txt"))+"_emp"+".txt";
									request.setAttribute("path",pathTemp);
								}
						}
					}
					else
					{
						//不支持的文件格式
					}
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "上传员工文件出现异常！");
					request.setAttribute("result", "false");
					if(conn != null)
						baseBiz.rollBackTransaction(conn);
				} finally
				{
					if(conn!=null&&!conn.isClosed()){
						baseBiz.closeConnection(conn);
					}
					if(workBook != null)
					workBook.close();
					try
					{
						if(reader!=null) reader.close();
					} catch (IOException e)
					{
						EmpExecutionContext.error(e, "上传员工文件关闭文件流出现异常！");
					}
					fileItem.delete();
				}
			}
		}
//		System.out.println("机构已有员工数："+employeeSize+" 文件上传数据条数："+(rows)+"成功添加员工数："+resultCount+" 整个过程用时："+(System.currentTimeMillis()-start));
		request.getRequestDispatcher(empRoot + "/employee/epl_addEmployee.jsp").forward(request,
				response);
	}
	
	
	/**
	 * 
	 * 2012-12-10
	 * @param phone 要验证的号码
	 * @param name 要求验证的姓名
	 * @param phoneNameMap 已经存在的号码合集
	 * @param checkFlag 页面选择时候要过滤重号 1：验证；2：不验证
	 * @return 
	 * boolean true表示重复，false表示不重复
	 */
	public boolean checkRepeat(String phone,String name,Map<String, String> phoneNameMap,int checkFlag)
	{
		//先过滤电话姓名同时重复的记录
		if(phoneNameMap.containsKey(phone+name))
		{
			return true;
		}
		//再过滤电话重复的
		else 
		{
			if(phoneNameMap.containsValue(phone))
			{
				//过滤
				if(checkFlag==1)
				{
					return true;
				}
				//不过滤
				else 
				{
					//phoneName.add(phone+name);
					//phones.add(phone);
					phoneNameMap.put(phone+name, phone);
					return false;
				}
			}
			else 
			{
				//phoneName.add(phone+name);
				//phones.add(phone);
				phoneNameMap.put(phone+name, phone);
				return false;
			}
		}
	}
	/**
	 * @param request
	 * @param response
	 */
	public void checkBook(HttpServletRequest request, HttpServletResponse response)
	{
		
		try
		{
			LfSysuser lfSysuser=(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			//手机号码
			String mobile =  request.getParameter("mobile"); 
			String eNo =  request.getParameter("eNo"); 
			String hidOpType =  request.getParameter("hidOpType");
			String name=request.getParameter("name");
			String depId=request.getParameter("depId");
			//String depCode = request.getParameter("depCode");
			if(mobile!=null && !"".equals(mobile))
			{
				String[] haoduan = employeeBiz.getHaoduan();
				PhoneUtil util =new PhoneUtil();
				int re = util.checkMobile(mobile,haoduan);	
				if (re == -1)
//				int re = employeeBiz.checkMobile(mobile,haoduan);	
//				if (re != 1)
				{
					response.getWriter().print("numfalse");
					return;
				}
			}
			//对员工编号进行空处理
			if(eNo!=null&&"".equals(eNo.trim())){
				eNo=null;
			}
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			String corpCode = lfSysuser.getCorpCode();

			if (!"edit".equals(hidOpType))
			{
				//当员工编号有值时进行重复判断
				if(eNo!=null){
					
					conditionMap.put("employeeNo", eNo.toUpperCase());
					//将员工号转换成大写
					conditionMap.put("corpCode", corpCode);
					//将员工号转换成大写
					List<LfEmployee> le = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
					if(le != null && le.size()>0){
						response.getWriter().print("已存在该工号的员工记录，无须重复添加！");
						return;
					}
				}
				conditionMap.clear();
				conditionMap.put("mobile", mobile);
				conditionMap.put("corpCode", corpCode);
				conditionMap.put("depId", depId);
				List<LfEmployee> mobileList = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
				if(mobileList != null && mobileList.size()>0){
					String responseStr="phoneExist";
					for (int i = 0; i < mobileList.size(); i++)
					{
						if(mobileList.get(i).getName().equals(name)){
							responseStr="phoneAndNameExist";
							break;
						}
					}
					response.getWriter().print(responseStr);
				}else{
					response.getWriter().print("true");
				}
			}else{
				String bookId =request.getParameter("bookId");
				if(bookId==null||"".equals(bookId.trim())){
					return;
				}
				bookId=getDecryptValue(request, bookId);
				LfEmployee employee = baseBiz.getById(LfEmployee.class, bookId.trim());
				//修改时对于员工空号为空的记录工号可以进行修改
				//当获取到员工编号有值传入且员工编号变化时进行重复判断
				if(eNo!=null&&!eNo.toUpperCase().equals(employee.getEmployeeNo())){
					conditionMap.put("employeeNo", eNo.toUpperCase());
					conditionMap.put("corpCode", corpCode);
					List<LfEmployee> le = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
					if(le != null && le.size()>0){
						response.getWriter().print("已存在该工号的员工记录！");
						return;
					}
				}
				//进行重号判断
				if(employee!=null){
					
					String oldmoblie = employee.getMobile();
					String olddep = String.valueOf(employee.getDepId());
					if(!oldmoblie.equals(mobile)||!olddep.equals(depId)){
						conditionMap.clear();
						conditionMap.put("mobile", mobile);
						conditionMap.put("corpCode", corpCode);
						conditionMap.put("depId", depId);
						List<LfEmployee> mobileList = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
						if(mobileList != null && mobileList.size()>0){
							String responseStr="phoneExist";
							for (int i = 0; i < mobileList.size(); i++)
							{
								if(mobileList.get(i).getName().equals(name)){
									responseStr="phoneAndNameExist";
									break;
								}
							}
							response.getWriter().print(responseStr);
							return;
						}
					}
					response.getWriter().print("true");
				}
				
			}
		}
		catch (Exception e)
		{
				EmpExecutionContext.error(e, "处理员工号码重复异常！");
		}
	}
	
	/**添加员工机构
	 * @param request
	 * @param response
	 * @throws java.io.IOException
	 */
	public void addDep(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String oppType =null, opContent =null;
		String opUser = getOpUser(request);
		//操作远的用户ID
		String lguserid = "";
		//企业编码
		String lgcorpcode = "";
		Long userId = null;
		try{
			
			// TODO 添加日志
			//添加日志，用于观察url请求参数获取的情况
			EmpExecutionContext.logRequestUrl(request, null);
			//获取登录sysuser
			LfSysuser sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工通讯录,uploadBook方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysUser.getCorpCode();
			//判断企业编码获取
			if(lgcorpcode==null||"".equals(lgcorpcode.trim())){
				EmpExecutionContext.error("员工通讯录,uploadBook方法session中获取企业编码出现异常");
				return;
			}
			lguserid=sysUser.getUserId()+"";
			//判断操作员获取
			if(lguserid==null||"".equals(lguserid.trim())){
				EmpExecutionContext.error("员工通讯录,uploadBook方法session中获取登录操作员ID出现异常");
				return;
			}
			
			/*此处去掉一个try catch
			}catch (Exception e) {
				EmpExecutionContext.error(e, "员工通讯录当前操作员lguserid 转化失败！");
			}
		
			try
			{
			*/
		
			//部门名称
			String name = request.getParameter("name");
			//部门id
			String scode = request.getParameter("scode");
			//自定义编码
			String depcodethird=request.getParameter("depcodethird");
			String output = "";
	    	oppType = StaticValue.ADD;
			opContent = "新增员工机构（机构名称:"+name+",机构编码:"+depcodethird+")";
			//过滤机构id为空的情况
			if(scode==null||"".equals(scode))
			{
				output="";
			}
			else
			{
				if(depcodethird!=null && name!=null){
					LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
/*					conditionMap.put("depcodethird", depcodethird.toUpperCase());
					conditionMap.put("corpCode", lgcorpcode);
					List<LfEmployeeDep> employeeDep=baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
					//判断自定义编码是否重复
					if(employeeDep!=null&&employeeDep.size()>0)
					{
						output="机构编码重复";
					}*/
					//判断自定义编码和机构名称是否重复
					List<DynaBean> employeeList = employeeBiz.sameDepNameOrDepCode(name, depcodethird.toUpperCase(), scode, lgcorpcode);
					if(employeeList!=null && employeeList.size() > 0)
					{
						if(depcodethird.toUpperCase().equals(employeeList.get(0).get("dep_code_third").toString()))
						{
							output="existdepcode";
						}
						else
						{
							output="existdepname";
						}
					}
					else{
						LfEmployeeDep le = employeeBiz.getEmployeeNewDep(scode,lgcorpcode);
						
						// 新增p 查询不到机构，有可能是用户在已删除机构中新增机构-------
						if (le == null) {
							output = "notfounddep";
						} else {

						// ----------------------------------------------------------
						
							le.setDepName(name);
							le.setDepEffStatus("A");
							le.setDepcodethird(depcodethird.toUpperCase());
							le.setCorpCode(lgcorpcode);
							//增加部门时的企业编号
							
							//EnterpriseBiz emterBiz = new EnterpriseBiz();
							boolean result = false;
							//获取系统配置的机构信息的map
							LinkedHashMap<String, String> map= SystemGlobals.getSysParamLfcorpConf(lgcorpcode);
							// 机构级别(系统配置)
							String maxLevel=map.get("dep.maxlevel");
							//单个机构子机构数(系统配置)
							String maxchild=map.get("dep.maxchild");
							// 机构总数(系统配置)
							String maxdep=map.get("dep.maxdep");
							
							long depCount=employeeBiz.getEmployeeDepCount(lgcorpcode);
							long levCount=employeeBiz.getEmployeeDepLevByDepId(Long.parseLong(scode));
							conditionMap.clear();
							conditionMap.put("parentId",scode);
							conditionMap.put("corpCode", lgcorpcode);
							List<LfEmployeeDep> leList=baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
							long childCount=leList==null?0:leList.size();
							//如果机构总数超过了配置的总数
							if(depCount>=Long.parseLong(maxdep))
							{
								output="DepAbove,"+Long.parseLong(maxdep);
							}
							//如果机构层数超过了配置的总数
							else if(levCount>=Long.parseLong(maxLevel))
							{
								output="levAbove,"+Long.parseLong(maxLevel);
							}
							//如果子机构数超过了配置的总数
							else if(childCount>=Long.parseLong(maxchild))
							{
								output="childAbove,"+Long.parseLong(maxchild);
							}
							else
							{
								LfEmployeeDep parentDep = baseBiz.getById(LfEmployeeDep.class, le.getParentId());
								le.setDeppath(parentDep.getDeppath());
								le.setDepLevel(parentDep.getDepLevel()+1);
								result = employeeBiz.addEmpDepConnInAddEmpDep(userId, le);
								if(result)
								{
									spLog.logSuccessString(opUser, opModule, oppType, opContent,lgcorpcode);
									opSucLog(request, opModule, opContent+"成功。","ADD");
								}
								output = result+"";
							}
						}
						
					}
				}
			}
			
			response.getWriter().print(output);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "新增员工机构失败！");
			response.getWriter().print("");
			spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, e,lgcorpcode);
		}
	}
	
	/**
	 * @param request
	 * @param response
	 */
	//删除员工
	public void delete(HttpServletRequest request, HttpServletResponse response)
	{
		String oppType =null, opContent =null;
		String opUser = getOpUser(request);
		//企业编码
		String lgcorpcode = "";
		try
		{
			
			//获取登录sysuser
			LfSysuser sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工通讯录,delete方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysUser.getCorpCode();
			//判断企业编码获取
			if(lgcorpcode==null||"".equals(lgcorpcode.trim())){
				EmpExecutionContext.error("员工通讯录,delete方法session中获取企业编码出现异常");
				return;
			}
			
			//员工IDSTR
			String bookIds = request.getParameter("bookIds");
			
			String id[] = bookIds.split(",");
			String guIds = "";
			String emps = "";//删除员工的重要信息拼接字符串
			LfEmployee lfEmployee = new LfEmployee();
			for (int i = 0; i<id.length;i++)
			{
				id[i]=getDecryptValue(request, id[i]);
				lfEmployee = baseBiz.getById(LfEmployee.class, id[i]);
				guIds+=lfEmployee.getGuId()+",";
				emps += "["+lfEmployee.getEmployeeId()+","+lfEmployee.getName()+","+lfEmployee.getMobile()+"],";
			}
			if(null != guIds && guIds.contains(","))
			{
				guIds = guIds.substring(0,guIds.lastIndexOf(","));
			}

			int result = -1;
			//删除信息
			result = employeeBiz.delEmployeeByGuid(guIds);
			new SendBirthDateBiz().deleteAddrBirthMemberByGuIds(guIds,lgcorpcode, "1");
			if(result >0)
			{
				oppType = StaticValue.DELETE;
				opContent = "批量删除员工（共删除:"+result+"条)";	
		    	spLog.logSuccessString(opUser, opModule, oppType, opContent,lgcorpcode);
		    	emps = "[员工ID,姓名，手机号码]（"+emps+"）";
		    	opSucLog(request, opModule, opContent+emps+"成功。", "DELETE");
			}
			
			response.getWriter().print(result);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "删除员工失败！");
		}
	}
	
	/**
	 * 删除员工机构  并且会删除员工
	 * @param request
	 * @param response
	 */
	public void delDep(HttpServletRequest request, HttpServletResponse response)
	{
		String oppType = null, opContent = null;
		String opUser = getOpUser(request);
		// 企业编码
		String lgcorpcode = "";
		Long userId = null;
		try {
			
			// TODO 添加日志
			//添加日志，用于观察url请求参数获取的情况 p
			EmpExecutionContext.logRequestUrl(request, null);
			//获取登录sysuser
			LfSysuser sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工通讯录,delete方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysUser.getCorpCode();
			//判断企业编码获取
			if(lgcorpcode==null||"".equals(lgcorpcode.trim())){
				EmpExecutionContext.error("员工通讯录,delete方法session中获取企业编码出现异常");
				return;
			}
			userId=sysUser.getUserId();
			//判断USERID
			if(userId==null){
				EmpExecutionContext.error("员工通讯录,delete方法session中获取企业编码出现异常");
				return;
			}
			
		/*	去掉这个try catch，合为一个总的 p
		}catch (Exception e) {
			//进入异常
			EmpExecutionContext.error(e, "员工通讯录当前操作员lguserid 转化失败！");
		}
		
		try
		{
		*/

			// 需要删除 ID
			String bookIds = request.getParameter("id");
			// 机构名称
			String name = request.getParameter("name");
			int result = -1;
			LfEmployeeDep lfEmployeeDep = baseBiz.getById(LfEmployeeDep.class,
					bookIds);
			

			// 新增  对机构树中已删除的机构进行删除操作的处理 如果为机构为null，可能该机构已被其它用户删除
			if (lfEmployeeDep == null) {
				result = -2;

				EmpExecutionContext.error("机构查询失败，该机构已被其它用户删除");

				response.getWriter().print(result);

				// --------------------------------------------------------------------------------------
			} else {
				// 根机构不能删除
				if (lfEmployeeDep.getDepLevel() != null
						&& lfEmployeeDep.getDepLevel().intValue() == 1) {
					// 2代表根机构
					result = 2;
				} else {
					// 不能删除有设置生日祝福的机构
					// 查询当前机构在生日祝福中是否使用
					LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.clear();
					conditionMap.put("memberId",bookIds);
					conditionMap.put("type", "1");
					conditionMap.put("corpCode",lgcorpcode);
					conditionMap.put("membertype", "2");
					List<LfBirthdayMember> birthdayMembers = baseBiz.getByCondition(LfBirthdayMember.class,conditionMap,null);
					if (birthdayMembers != null && birthdayMembers.size() > 0 ){
						response.getWriter().print("haveBirthdayMembers");
						EmpExecutionContext.info("员工通讯录-该机构有被员工生日祝福使用，不能删除。机构id：" + bookIds + "企业编码：" + lgcorpcode);
						return;
					}

					// 查询当前机构的上级机构在生日祝福中是否使用
					String deppath = lfEmployeeDep.getDeppath();
					// 获取父机构id
					String supIds = StringUtils.join(deppath.split("/"),",");
					conditionMap.clear();
					conditionMap.put("memberId&in",supIds);
					conditionMap.put("type", "1");
					conditionMap.put("corpCode",lgcorpcode);
					// 设置成员类型为包含子机构
					conditionMap.put("membertype", "3");
					List<LfBirthdayMember> supBirthdayMembers = baseBiz.getByCondition(LfBirthdayMember.class,conditionMap,null);
					if (supBirthdayMembers != null && supBirthdayMembers.size() > 0){
						response.getWriter().print("haveBirthdayMembers");
						EmpExecutionContext.info("员工通讯录-该机构的上级机构有被员工生日祝福使用，不能删除。机构id：" + bookIds + "企业编码：" + lgcorpcode);
						return;
					}
					result = employeeBiz.delEmployeeAddrDep(bookIds,
							lgcorpcode, userId);
					if (result > 0) {
						oppType = StaticValue.DELETE;
						opContent = "删除员工机构（机构ID:" + bookIds + "，机构名称:" + name
								+ "）";
						spLog.logSuccessString(opUser, opModule, oppType,
								opContent, lgcorpcode);
						opSucLog(request, opModule, opContent + "成功。", "DELETE");
					}
				}
				response.getWriter().print(result);
			}

		} catch (Exception e) {
			EmpExecutionContext.error(e, "删除员工机构失败！");
		}
	}

	/**
	 * @param request
	 * @param response
	 */
	//编码
	public void doEdit(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			//修改编码的 员工ID
			String bookId = request.getParameter("bookId");
			String lguserid="";
			String lgcorpcode="";
			
			//获取登录sysuser
			LfSysuser sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工通讯录,doEdit方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysUser.getCorpCode();
			//判断企业编码获取
			if(lgcorpcode==null||"".equals(lgcorpcode.trim())){
				EmpExecutionContext.error("员工通讯录,doEdit方法session中获取企业编码出现异常");
				return;
			}
			lguserid=sysUser.getUserId()+"";
			//判断USERID
			if(lguserid==null){
				EmpExecutionContext.error("员工通讯录,doEdit方法session中获取企业编码出现异常");
				return;
			}
			
			LfEmployee lc = null;
			if (bookId != null && !"".equals(bookId)) {
				bookId=getDecryptValue(request, bookId);
				lc = baseBiz.getById(LfEmployee.class, bookId);
			}
			if (lc == null) {
				lc = new LfEmployee();
			} else {
				LfEmployeeDep empDep = baseBiz.getById(LfEmployeeDep.class, lc
						.getDepId());
				request.setAttribute("depName", empDep == null ? "" : empDep
						.getDepName());
			}
			LfSysuser sysuser = baseBiz.getByGuId(LfSysuser.class, lc.getGuId());
			request.setAttribute("lfsysuser", sysuser);
			if(sysuser != null ){
				SysuserBiz userBiz = new SysuserBiz();
				LfSysuserVo sysuserVo = userBiz.getSysuserVoByUserId(sysuser.getUserId());
				request.setAttribute("sysuser", sysuserVo);
				
				request.setAttribute("isExistSubno", sysuser.getIsExistSubNo());
				//判断是否分配操作员固定尾�?  分配了就进去 尾号规则表去查询该操作员的尾�?
				if(sysuser.getIsExistSubNo() == 1){
					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("corpCode", lgcorpcode);
					conditionMap.put("loginId", String.valueOf(sysuser.getGuId()));
					conditionMap.put("menuCode&is null", "isnull");
					conditionMap.put("busCode&is null", "isnull");
					List<LfSubnoAllot> subnoAllotList = baseBiz.getByCondition(LfSubnoAllot.class, conditionMap, null);
					if(subnoAllotList != null && subnoAllotList.size()>0){
						LfSubnoAllot subnoAllot = subnoAllotList.get(0);
						request.setAttribute("usedSubno", subnoAllot.getUsedExtendSubno());
					}
				}else{
					//删除数据库中在操作员没有分配尾号的状态时候， 去数据库删除�?��
					new SysuserPriBiz().delUserSubno(sysuser);
				}
				
				Integer subnoDigit = userBiz.getSubnoDidit(lgcorpcode);
				//获取该企业的默认子号长度
	   			request.setAttribute("subnoDigit",subnoDigit);
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("userId",sysuserVo.getUserId().toString());
				List<LfEmpDepConn> empDepConn = baseBiz.getByCondition(LfEmpDepConn.class, conditionMap, null);
				List<LfCliDepConn> clientDepConn = baseBiz.getByCondition(LfCliDepConn.class, conditionMap, null);
				if(empDepConn != null && empDepConn.size()>0){
					String depIds = "";
					for (LfEmpDepConn lfEmpDepConn : empDepConn) {
						depIds +=lfEmpDepConn.getDepId()+",";
					}
					depIds = depIds.substring(0,depIds.lastIndexOf(","));
					conditionMap.clear();
					conditionMap.put("depId&in", depIds);
					List<LfEmployeeDep> employeeDeps = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
					String depNames = "";
					for (LfEmployeeDep lfEmployeeDep : employeeDeps) {
						depNames += lfEmployeeDep.getDepName()+";";
					}
					request.setAttribute("employeeIds", depIds);
					request.setAttribute("employeeNames", depNames);
				}
				
				if(clientDepConn != null && clientDepConn.size()>0){
					String depIds = "";
					for (LfCliDepConn lfCliDepConn : clientDepConn) {
						depIds +=lfCliDepConn.getDepId()+",";
					}
					depIds = depIds.substring(0,depIds.lastIndexOf(","));
					conditionMap.clear();
					conditionMap.put("depId&in", depIds);
					List<LfClientDep> clientDeps = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
					String depNames = "";
					for (LfClientDep lfClientDep : clientDeps) {
						depNames += lfClientDep.getDepName()+";";
					}
					request.setAttribute("clientIds", depIds);
					request.setAttribute("clientNames", depNames);
				}
			}
			
			LfSysuser cusysuser = baseBiz.getById(LfSysuser.class, lguserid);
			//默认机构权限
			Integer permission = 2;
			if(sysuser != null){
				permission = cusysuser.getPermissionType();
			}
			request.setAttribute("permission", permission);
			
			List<LfEmployeeTypeVo> bookInfoList = employeeBiz.getAllEmployeeTypeVo( Long.parseLong(lguserid),lgcorpcode);
			request.setAttribute("zwList", bookInfoList);
			RoleBiz roleBiz=new RoleBiz();
			List<LfRoles> roleList=roleBiz.getAllRoles(Long.parseLong(lguserid));
			request.setAttribute("roleList", roleList);
			request.setAttribute("employee", lc);
			request.getRequestDispatcher(empRoot + "/employee/epl_editEmployee.jsp").forward(request,
					response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "修改员工失败！");
		}
	}
	
	
	/**
	 *   修改机构的名称 
	 * @param request
	 * @param response
	 * @throws java.io.IOException
	 */
	public void updateDepName(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String oppType = null, opContent = null;
		String opUser = getOpUser(request);
		String isResult = "0";
		String lgcorpcode = "";
		try {
			
			//获取登录sysuser
			LfSysuser sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工通讯录,doEdit方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysUser.getCorpCode();
			//判断企业编码获取
			if(lgcorpcode==null||"".equals(lgcorpcode.trim())){
				EmpExecutionContext.error("员工通讯录,doEdit方法session中获取企业编码出现异常");
				return;
			}
			
			
			BaseBiz baseBiz = new BaseBiz();
			String depName = request.getParameter("depName");
			// 需要修改的名称
			String depId = request.getParameter("depCode");
			// 机构编码
			// String type = request.getParameter("type");
			// 点的是否是树的顶级机构 1不是 2是的
			oppType = StaticValue.UPDATE;
			String _depName = "";
			opContent = "修改员工机构（机构名称:" + depName + ")";
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// String corpCode = getCorpCode();
			// 企业编码
			conditionMap.put("depId", depId);
			List<LfEmployeeDep> deps = baseBiz.getByCondition(
					LfEmployeeDep.class, conditionMap, null);

			if (deps != null && deps.size() > 0) {
				LfEmployeeDep dep = deps.get(0);
				// if("2".equals(type)){
				// 同级机构下是否存在相同的机构名
				conditionMap.clear();
				conditionMap.put("parentId", dep.getParentId().toString());
				conditionMap.put("corpCode", lgcorpcode);
				conditionMap.put("depName", depName);
				List<LfEmployeeDep> employeeDeps = baseBiz.getByCondition(
						LfEmployeeDep.class, conditionMap, null);
				if (employeeDeps != null && employeeDeps.size() > 0) {
					isResult = "3";
					// 同级存在相同的机构
				}
				// }
				if (!"3".equals(isResult)) {
					_depName = dep.getDepName();
					dep.setDepName(depName);
					boolean returnFlag = baseBiz.updateObj(dep);
					if (returnFlag) {
						isResult = "1";
						// 修改成功
						spLog.logSuccessString(opUser, opModule, oppType,
								opContent, lgcorpcode);
						opSucLog(request, opModule, "修改员工机构（机构ID:"
								+ dep.getDepId() + ",机构名称：" + _depName + "-->"
								+ depName + "）成功。", "UPDATE");
					} else {
						isResult = "2";
						// 修改失败
					}
				}
			} else {
				isResult = "4";
				// 数据库之类的出错 没有查询结果
			}

			response.getWriter().print(isResult);
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e, "修改员工机构名称失败！");
			spLog.logFailureString(opUser, opModule, oppType, opContent
					+ opSper, e, lgcorpcode);
		}
	}
	
	/**
	 *   进入新增员工页面前查询 职务LIST
	 * @param request
	 * @param response
	 */
	public void toAddEmployeePage(HttpServletRequest request, HttpServletResponse response)
	{
		List<LfEmployeeTypeVo> bookInfoList = null;
		try
		{
			//企业编码
			String corpCode = request.getParameter("lgcorpcode");
			//用户ID
			//String id = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String id = SysuserUtil.strLguserid(request);

			Long userId = Long.valueOf(id);
			bookInfoList = employeeBiz.getAllEmployeeTypeVo( userId,corpCode);
			request.setAttribute("zwList", bookInfoList);
			RoleBiz roleBiz=new RoleBiz();
			List<LfRoles> roleList=roleBiz.getAllRoles(userId);
			request.setAttribute("roleList", roleList);
			Long guid = globalBiz.getValueByKey("guid", 1L);
			request.setAttribute("guid", guid);
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, userId);
			//默认机构权限
			Integer permission = 2;
			if(sysuser != null){
				permission = sysuser.getPermissionType();
			}
			request.setAttribute("permission", permission);
			request.getRequestDispatcher(empRoot  + "/employee/epl_addEmployee.jsp").forward(request,
					response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "进入新增员工失败！");
		}
	}
	/**
	 * @param request
	 * @param response
	 * @throws java.io.IOException
	 */
	//修改员工机构的
	public void changeDep(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String oppType =null, opContent =null;
		String opUser = getOpUser(request);
		try
		{
			LfSysuser lfSysuser=(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			oppType = StaticValue.UPDATE;
			//机构ID
			String depId = request.getParameter("depId");
			//修改修改的员工ID
			String bookIds = request.getParameter("bookIds");
			
			String id[] = bookIds.split(",");
			String guIds = "";
			LfEmployee lfEmployee = new LfEmployee();
			for (int i = 0; i<id.length;i++)
			{
				id[i]=getDecryptValue(request, id[i]);
				lfEmployee = baseBiz.getById(LfEmployee.class, id[i]);
				guIds+=lfEmployee.getGuId()+",";
			}
			if(null != guIds && guIds.contains(","))
			{
				guIds = guIds.substring(0,guIds.lastIndexOf(","));
			}
			//修改员工机构ID
			boolean result = employeeBiz.changeEmployeeDep(guIds,depId);
			if(result)
			{
		    	opContent = "批量修改员工机构（共修改:"+id.length+"条)";	
		    	spLog.logSuccessString(opUser, opModule, oppType, opContent,lfSysuser.getCorpCode());
		    	opSucLog(request, opModule, "批量修改员工机构（总条数:"+id.length+",机构ID:"+depId+",员工ID:"+bookIds+")成功。","UPDATE");
			}
			
			response.getWriter().print(result);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "批量修改员工机构失败！");
			response.getWriter().print("false");
		}
	}

	
	
	
	/**
	 *   获取机构的 数，只 限查子级
	 * @param request
	 * @param response
	 * @throws java.io.IOException
	 */
	public void getEmpSecondDepJson(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			//登录用户
			LfSysuser lfSysuser=(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			String depId = request.getParameter("depId");
			//通过员工机构id查找树
			List<LfEmployeeDep> empDepList = employeeBiz.getEmpSecondDepTreeByUserorDepId(lfSysuser,depId);
			LfEmployeeDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(empDepList != null && empDepList.size()>0){
				tree.append("[");
				for (int i = 0; i < empDepList.size(); i++) {
					dep = empDepList.get(i);
					tree.append("{");
					tree.append("id:'").append(dep.getDepId()+"'");
					tree.append(",name:'").append(dep.getDepName()).append("'");
					tree.append(",pId:'").append(dep.getParentId()+"'");
					tree.append(",depcodethird:'").append(dep.getDepcodethird()+"'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != empDepList.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		} catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e, "获取员工机构树出现异常！");
		}
	}
	
	/**
	 *   获取机构的 数，只 限查子级
	 * @param request
	 * @param response
	 * @throws java.io.IOException
	 */
	public void getEmpDepCheck(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String checkedIds = request.getParameter("checkedIds");
		List<String> ids = null;
		if(checkedIds != null && !"".equals(checkedIds)){
			ids = new ArrayList<String>();
			String[] chId = checkedIds.split(",");
			for (String str : chId) {
				ids.add(str);
			}
		}
		try
		{
			LfSysuser lfSysuser=(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			String depId = request.getParameter("depId");
			List<LfEmployeeDep> empDepList = employeeBiz.getEmpSecondDepTreeByUserorDepId(lfSysuser,depId);
			LfEmployeeDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(empDepList != null && empDepList.size()>0){
				tree.append("[");
				for (int i = 0; i < empDepList.size(); i++) {
					dep = empDepList.get(i);
					tree.append("{");
					tree.append("id:'").append(dep.getDepId()+"'");
					tree.append(",name:'").append(dep.getDepName()).append("'");
					tree.append(",pId:'").append(dep.getParentId()+"'");
					tree.append(",depcodethird:'").append(dep.getDepcodethird()+"'");
					tree.append(",depId:'").append(dep.getDepId()+"'");
					tree.append(",isParent:").append(true);
					if(ids != null && ids.contains(dep.getDepId().toString())){
						tree.append(",checked:").append(true);
					}
					tree.append("}");
					if(i != empDepList.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		} catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e, "处理员工机构出现异常！");
		}
	}
	
	
	// 批量导出员工
	public void exportEmployeeExcel(HttpServletRequest request,HttpServletResponse response) {
		
		try {
			//pageSet(request);
			long startTime = System.currentTimeMillis();
			String context = request.getSession(false).getServletContext().getRealPath(excelPath);

			//获取当前企业编码与用户ID
			String lgUserId = "";
			String corpCode = "";
			String lgGuId = "";
			//获取登录sysuser
			LfSysuser sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工通讯录,exportEmployeeExcel方法session中获取当前登录对象出现异常");
				return;
			}
			corpCode=sysUser.getCorpCode();
			//判断企业编码获取
			if(corpCode==null||"".equals(corpCode.trim())){
				EmpExecutionContext.error("员工通讯录,exportEmployeeExcel方法session中获取企业编码出现异常");
				return;
			}
			lgGuId=sysUser.getGuId()+"";
			//判断企业编码获取
			if(lgGuId==null||"".equals(lgGuId.trim())){
				EmpExecutionContext.error("员工通讯录,exportEmployeeExcel方法session中获取lgGuId出现异常");
				return;
			}
			lgUserId=sysUser.getUserId()+"";
			//判断企业编码获取
			if(lgUserId==null||"".equals(lgUserId.trim())){
				EmpExecutionContext.error("员工通讯录,exportEmployeeExcel方法session中获取lgUserId出现异常");
				return;
			}
			PageInfo pageInfo=new PageInfo();
			//返回状态
			String result = "false";
			//操作日志信息
			String opContent = "";
			Long userId = null;
			//如果从页面获取用户Id不为空
			if (null != lgUserId && !"".equals(lgUserId)) {
				//变量转类型
				userId = Long.parseLong(lgUserId);
				if (null != corpCode && !"".equals(corpCode)) {
					corpCode = corpCode.toString();

					LfEmployeeVo mv = request.getSession(false).getAttribute("employeeInfo") != null ? (LfEmployeeVo) request
							.getSession(false).getAttribute("employeeInfo")
							: new LfEmployeeVo();
							
					//request.getSession().removeAttribute("employeeInfo");
					EmployeeExcel et = new EmployeeExcel(context);
					
					Map<String, String> resultMap = et.createEqaEmployeeAddrBookRar(userId, mv, corpCode,pageInfo,request);
					//如果导出内容不为空
					if (resultMap != null && resultMap.size() > 0) {
//						//获取导出的文件名
//						String fileName = resultMap.get("FILE_NAME");
//						//获取导出的路径
//						String filePath =  resultMap.get("FILE_PATH");
//						//下载文件
//						DownloadFile dfs = new DownloadFile();
//						dfs.downFile(request, response, filePath, fileName);
//						opSucLog(request, opModule, "批量导出员工成功。", "OTHER");
			            HttpSession session = request.getSession(false);
			            session.setAttribute("employee_export",resultMap);

			        	//格式化时间
			        	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
						//操作日志信息
						opContent = "导出成功，开始："+sdf.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + pageInfo.getTotalRec();
						result = "true";
						
					//如果导出内容为空
					} else {
						//跳转到find页面
						//response.sendRedirect(request.getContextPath()+"/epl_employeeBook.htm?lgguid="+lgGuId);
						//操作日志信息
						opContent = "导出失败，无导出数据";
					}
				} else {
					//response.sendRedirect(request.getContextPath()+"/epl_employeeBook.htm?lgguid="+lgGuId);
					//操作日志信息
					opContent = "导出失败，corpCode："+corpCode;
				}
			} else {
				//response.sendRedirect(request.getContextPath()+"/epl_employeeBook.htm?lgguid="+lgGuId);
				//操作日志信息
				opContent = "导出失败，lgUserId："+lgUserId;
			}
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			EmpExecutionContext.info("员工通讯录", corpCode, lgUserId, lfSysuser.getUserName(), opContent, "GET");
			response.getWriter().print(result);
			
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e, "批量导出员工机构出现异常！");
			try {
				request.getSession(false).setAttribute("error", e);
			} catch (Exception e1) {
				EmpExecutionContext.error(e1,"session为null");
			}
		}
	}
	
	/**
	 * 员工通讯录EXCEL文件下载
	 * @description    
	 * @param request
	 * @param response
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-8-13 下午12:26:22
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//文件名
		String fileName = "";
		//文件路径
		String filePath = "";
        try
		{
			HttpSession session = request.getSession(false);
			Object obj = session.getAttribute("employee_export");
			if(obj != null){
			    Map<String, String> resultMap = (Map<String, String>) obj;
			    fileName = (String) resultMap.get("FILE_NAME");
			    filePath = (String) resultMap.get("FILE_PATH");
			    //弹出下载页面
			    new DownloadFile().downFile(request, response, filePath, fileName);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "员工通讯录EXCEL文件下载失败，fileName:"+fileName+"，filePath:"+filePath);
		}
	}
	
	
	
	public void toAdd(HttpServletRequest request,HttpServletResponse response)
	{
		String lgguid = request.getParameter("lgguid");
		HttpSession session = request.getSession(false);
		try
		{
			LfSysuser sysuser = baseBiz.getByGuId(LfSysuser.class, Long.parseLong(lgguid));
			int permissionType = sysuser.getPermissionType();
			
			if(permissionType != 2){
				request.getRequestDispatcher(empRoot  +  "/employee/epl_nopermission.jsp").forward(
						request, response);
				return;
			}
			
			RoleBiz roleBiz=new RoleBiz();
			List<LfRoles> roleList=roleBiz.getAllRoles(sysuser.getUserId());
			session.setAttribute("roleList", roleList);
		
			/*WgMsgConfigBiz roBiz = new WgMsgConfigBiz();
   			List<Userdata> userList=roBiz.getAllRoutUserdata();*/
			List<Userdata> userList = null;
			session.setAttribute("routUserdatas", userList);
			
			//员工的id，用于员工转为操作员
			String eid = request.getParameter("eid");
			
			Long guid = null;
			if (eid != null && !"".equals(eid))
			{
				LfEmployee le =  baseBiz.getById(LfEmployee.class, eid);
				request.setAttribute("le", le);
				guid = le.getGuId();
			}else{
				guid = globalBiz.getValueByKey("guid", 1L);
			}
			SysuserBiz userBiz = new SysuserBiz();
			Integer subnoDigit = userBiz.getSubnoDidit(sysuser.getCorpCode());
			//获取该企业的默认子号长度
   			request.setAttribute("subnoDigit",subnoDigit);
   			request.setAttribute("guid", guid);
   			request.setAttribute("corpCode", sysuser.getCorpCode());
			request.getRequestDispatcher(empRoot +"/operator/opt_addSysuser.jsp")
				.forward(request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "跳转新增操作员页面出现异常！");
		}
	}
	
	/**
	 * 检查重复
	 * @param aa
	 * @param ee
	 * @return
	 */
	protected boolean checkRepeat(List<String> aa,String ee)
	{
		int begin=0;
		int end=aa.size();
		int center=0;
		int result=0;
		center=(begin+end)/2;
		while(end>begin)
		{
			result=aa.get(center).compareTo(ee);
			if(result>0)
			{
				end=center;
			}else if(result<0)
			{
				begin=(begin==center?center+1:center);
			}else
			{
				return false;
			}
			center=(begin+end)/2;
		}
		aa.add(center,ee);
		return true;
	}
	
	//过滤员工机构
	private String[] getEmployeeDepIds(String depIds)
	{
		String[] ids = depIds.split(",");
		LfEmployeeDep employeeDep = null;
		List<LfEmployeeDep> employeeDeps = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			for (int i=0;i<ids.length-1;i++) {
				if(!"".equals(ids[i])){
					employeeDep = baseBiz.getById(LfEmployeeDep.class, ids[i]);
					conditionMap.put("deppath&like2",employeeDep.getDeppath());
					employeeDeps = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
					if(employeeDeps!=null && employeeDeps.size()>0){
						List<String> list = new ArrayList<String>();
						for (LfEmployeeDep lfEmployeeDep : employeeDeps) {
							list.add(lfEmployeeDep.getDepId().toString());
						}
						for (int j=i+1;j<ids.length;j++) {
							if(list.contains(ids[j])){
								ids[j] = "";
							}
						}
					}
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "过滤员工机构异常！");
		}
		return ids;
	}
	/**
	 * 过滤客户机构
	 * @param depIds
	 * @return
	 */
	private String[] getClientDepIds(String depIds)
	{
		String[] ids = depIds.split(",");
		LfClientDep clientDep = null;
		List<LfClientDep> clientDeps = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			for (int i=0;i<ids.length-1;i++) {
				if(!"".equals(ids[i])){
					clientDep = baseBiz.getById(LfClientDep.class, ids[i]);
					conditionMap.put("deppath&like2",clientDep.getDeppath());
					clientDeps = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
					if(clientDeps!=null && clientDeps.size()>0){
						List<String> list = new ArrayList<String>();
						for (LfClientDep lfclientDep : clientDeps) {
							list.add(lfclientDep.getDepId().toString());
						}
						for (int j=i+1;j<ids.length;j++) {
							if(list.contains(ids[j])){
								ids[j] = "";
							}
						}
					}
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "过滤客户机构异常！");
		}
		return ids;
	}
	
	
	/**
	 * 获取操作员名称
	 * @description    
	 * @param request
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-4-2 上午09:17:49
	 */
	public String getOpUser(HttpServletRequest request){
		String opUser = "";
		LfSysuser sysuser = null;
		try {
			sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		} catch (Exception e) {
			EmpExecutionContext.error(e,"session为null");
		}
		if(sysuser != null){
			opUser = sysuser.getUserName();
		}
		return opUser;
	}
	
	/**
	 * 手机号+姓名是否重复
	 * @description    
	 * @param phone
	 * @param name
	 * @param phones
	 * @param checkFlag 1 只检查phone 
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-5-28 下午04:53:09
	 */
	public boolean checkRepeat(String phone,String name,Set<String> phones,Set<String> phonenames,int checkFlag){
		if(!phonenames.add(phone+name)){
			return true;
		}else if(checkFlag==1){
			return !phones.add(phone);
		}else{
			return false;
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
		LfSysuser lfSysuser = null;
		Object obj = null;
		try {
			obj = request.getSession(false).getAttribute("loginSysuser");
		} catch (Exception e) {
			EmpExecutionContext.error(e,"session为null");
		}
		if(obj == null) return;
		lfSysuser = (LfSysuser)obj;
		EmpExecutionContext.info(modName, lfSysuser.getCorpCode(), String.valueOf(lfSysuser.getUserId()), lfSysuser.getUserName(), opContent, opType);
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
			Object encrypOrDecrypttobject = (ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
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
