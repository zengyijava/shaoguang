package com.montnets.emp.employee.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.entity.birthwish.LfBirthdayMember;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SendBirthDateBiz;
import com.montnets.emp.common.biz.SysuserBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.employee.biz.EmployeeBookBiz;
import com.montnets.emp.employee.biz.EmployeeDoDepBiz;
import com.montnets.emp.employee.biz.EmployeeExcel;
import com.montnets.emp.employee.dao.EmpDepDao;
import com.montnets.emp.employee.vo.LfEmployeeTypeVo;
import com.montnets.emp.employee.vo.LfEmployeeVo;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sysuser.LfRoles;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.sysuser.biz.DepPriBiz;
import com.montnets.emp.sysuser.biz.RoleBiz;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import org.apache.commons.lang.*;


/**
 * 员工通讯录
 * eqa_employeeAddrBookServlet
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class epl_empDepSvt extends BaseServlet {

	private final EmployeeBookBiz employeeBiz = new EmployeeBookBiz();
	private final DepPriBiz depPriBiz = new DepPriBiz();
	private final GlobalVariableBiz globalBiz = GlobalVariableBiz.getInstance();
	private final String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);
	//操作模块
	private final String opModule =StaticValue.ADDBR_MANAGER;
	//操作者
	private final String opSper = StaticValue.OPSPER;
	
	private final String empRoot = "user";
	
	private final String basePath = "/employee";
	
	private final String excelPath = "user/employee/file/";
	
	private final BaseBiz baseBiz=new BaseBiz();
	private final SuperOpLog spLog=new SuperOpLog();
	private final EmployeeDoDepBiz depBiz = new EmployeeDoDepBiz();

	/**
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		//登录者GUID
		String lgusername = "";
		Long lgguid = null;
		LfSysuser sysUser = null;
		Long userId = null;
		
		try {
			
			//获取登录sysuser
			sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工机构,find方法session中获取当前登录对象出现异常");
				return;
			}
			lgguid=sysUser.getGuId();
			//判断企业编码获取
			if(lgguid==null){
				EmpExecutionContext.error("员工机构,find方法session中获取lgguid出现异常");
				return;
			}
			userId=sysUser.getUserId();
			//判断USERID获取
			if(userId==null){
				EmpExecutionContext.error("员工机构,find方法session中获取lgUserId出现异常");
				return;
			}
			lgusername=sysUser.getUserName();
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", String.valueOf(userId));
			//获取所有机构关联
			List<LfEmpDepConn> conn = baseBiz.getByCondition(LfEmpDepConn.class, conditionMap, null);
			if(conn != null && conn.size() > 0)
			{
				request.setAttribute("connDepId", conn.get(0).getDepId().toString());
			}else			
			{
				request.getRequestDispatcher(empRoot  + "/employee/epl_nopermission.jsp").forward(request,response);
				return ;
			}
			request.setAttribute("lgguid", lgguid);
			request.setAttribute("lgusername", lgusername);
			request.getRequestDispatcher(empRoot + "/employee/epl_empDep.jsp").forward(request,response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"跳转员工机构管理页面失败！");
		}
	}
	/**
	 *   获取机构的 数，只 限查子级
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getEmpSecondDepJson(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
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
			EmpExecutionContext.error(e,"查询员工机构树失败！");
		}
	}
	/**
	 * 获取机构下的机构
	 * @param request
	 * @param response
	 */
	public void getTable(HttpServletRequest request, HttpServletResponse response)
	{
		long startTime = System.currentTimeMillis();
		try{
			String lgcorpcode="";
			
			//获取登录sysuser
			LfSysuser sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工机构,getTable方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysUser.getCorpCode();
			//判断企业编码获取
			if(lgcorpcode==null||"".equals(lgcorpcode)){
				EmpExecutionContext.error("员工机构,getTable方法session中获取lgcorpcode出现异常");
				return;
			}
			
			String depId=request.getParameter("depId");
			PageInfo pageInfo = new PageInfo();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			pageSet(pageInfo, request);
			conditionMap.put("parentId", depId);
			conditionMap.put("corpCode", lgcorpcode);
			orderbyMap.put("addType",StaticValue.ASC);
			orderbyMap.put("depId",StaticValue.ASC);
			//获得list时，在分页查询中，只在第一页查询总数，后续翻页不查总数
			List<LfEmployeeDep> empDepList = baseBiz.getByConditionNoCount(LfEmployeeDep.class, null, conditionMap, orderbyMap, pageInfo);
			request.setAttribute("empDepList", empDepList);
			request.setAttribute("pageInfo", pageInfo);

			//格式化时间
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			//操作日志信息
			String opContent = "查询："+sdf.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，机构ID:"+depId+"，总数：" + pageInfo.getTotalRec();
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			EmpExecutionContext.info("员工机构管理", lgcorpcode, String.valueOf(lfSysuser.getUserId()), lfSysuser.getUserName(), opContent, "GET");
			request.getRequestDispatcher(empRoot  + "/employee/epl_depTable.jsp").forward(request,response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询员工机构失败！");
		}
	}
	/**
	 * 编辑排序功能
	 * 2013-7-17
	 * @param request
	 * @param response
	 * void
	 * @throws IOException 
	 */
	public void doEditSort(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String returnmsg = "fail";
		try {
			String ids = request.getParameter("ids");
			String sortids = request.getParameter("sortids");
			String lgcorpcode="";
			
			//获取登录sysuser
			LfSysuser sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工机构,doEditSort方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysUser.getCorpCode();
			//判断USERID获取
			if(lgcorpcode==null||"".equals(lgcorpcode)){
				EmpExecutionContext.error("员工机构,doEditSort方法session中获取lgcorpcode出现异常");
				return;
			}
			List<LfEmployeeDep> lists = new ArrayList<LfEmployeeDep>();
			String[] sortidsarray = new String[]{};
			if(sortids != null && !"".equals(sortids)){
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("corpCode",lgcorpcode);
				conditionMap.put("depId&in",ids);
				lists = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
				String[] idsarray = ids.substring(0,ids.length()-1).split(",");
				sortidsarray = sortids.substring(0,sortids.length()-1).split(",");
				returnmsg = depBiz.sortEmployeeDep(idsarray, sortidsarray, lgcorpcode);
			}
			if("true".equals(returnmsg)){
				String opContent = "员工机构排序[机构ID[机构名称]，排序] （";
				for (int i = 0; i < sortidsarray.length&&i<lists.size(); i++)
				{
					LfEmployeeDep eDep = lists.get(i);
					opContent += eDep.getDepId()+"["+eDep.getDepName()+"]，"+eDep.getAddType()+"-->"+sortidsarray[i]+"，";
				}
				opContent +="）成功。";
				opSucLog(request, opModule, opContent, "UPDATE");
			}
			response.getWriter().print(returnmsg);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"员工机构排序失败！");
			response.getWriter().print(returnmsg);
		}
	}
	/**
	 * 合并结构功能
	 * 2013-7-17
	 * @param request
	 * @param response
	 * void
	 */
	public void doAdddep(HttpServletRequest request, HttpServletResponse response) throws IOException{
		try {
			String lgcorpcode="";
			//需要被 合并的机构
			String srcid = request.getParameter("srcid");
			//目标机构 
			String tagid = request.getParameter("tagid");
			
			//获取登录sysuser
			LfSysuser sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工机构,doAdddep方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysUser.getCorpCode();
			//判断USERID获取
			if(lgcorpcode==null||"".equals(lgcorpcode)){
				EmpExecutionContext.error("员工机构,doAdddep方法session中获取lgcorpcode出现异常");
				return;
			}
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			List<LfEmployeeDep> employeeDepList = null;
			String returnmsg = "";
			//先判断
			LfEmployeeDep updateDep = baseBiz.getById(LfEmployeeDep.class, srcid);
			/*原来没有加企业编码 备份
			boolean b = new EmpDepDao().judgeiSson(updateDep.getDeppath(), tagid);
			*/
			//增加企业编码 新增一行 pengj
			boolean b = new EmpDepDao().judgeiSson(updateDep.getDeppath(), tagid, updateDep.getCorpCode());
			//---------------------------
			if(b){
				response.getWriter().print("exits");
			}else{
				//再修改
				LfEmployeeDep srcDep = baseBiz.getById(LfEmployeeDep.class, tagid);
				conditionMap.put("deppath&like2", updateDep.getDeppath());
				conditionMap.put("depId&<>", String.valueOf(updateDep.getDepId()));
				conditionMap.put("corpCode", lgcorpcode);
				objectMap.put("depLevel", StaticValue.ASC);
				employeeDepList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap,objectMap);
				returnmsg = depBiz.combineEmployeeDep(employeeDepList, updateDep, srcDep);
				if(returnmsg.equals("success")){
					response.getWriter().print("true");
					opSucLog(request, opModule, "合并机构（机构ID）（"+srcDep.getDepId()+"-->"+updateDep.getDepId()+"）成功。", "UPDATE");
				}else{
					response.getWriter().print("fail");
				}
			}
		} catch (Exception e) {
			response.getWriter().print("fail");
			EmpExecutionContext.error(e,"合并机构失败！");
				
		}
	}
	
	/**
	 * 调整员工机构
	 * 2013-7-17
	 * @param request
	 * @param response
	 * void
	 * @throws IOException 
	 */
	public void doEditdep(HttpServletRequest request, HttpServletResponse response) throws IOException{
		try {
			String lgcorpcode="";
			
			//获取登录sysuser
			LfSysuser sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工机构,doAdddep方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysUser.getCorpCode();
			//判断USERID获取
			if(lgcorpcode==null||"".equals(lgcorpcode)){
				EmpExecutionContext.error("员工机构,doAdddep方法session中获取lgcorpcode出现异常");
				return;
			}
			
			String srcid = request.getParameter("srcid");
			String tagid = request.getParameter("tagid");
			if(StringUtils.isBlank(lgcorpcode)||StringUtils.isBlank(srcid)||StringUtils.isBlank(tagid)){
				response.getWriter().print("fail");
				return;
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			List<LfEmployeeDep> list = null;
			LfEmployeeDep srcdep = baseBiz.getById(LfEmployeeDep.class, srcid);
			LfEmployeeDep tagdep = baseBiz.getById(LfEmployeeDep.class, tagid);
			boolean bo =  new EmpDepDao().judgeiSson(srcdep.getDeppath(), tagid);
			if(srcdep == null || tagdep == null){
				response.getWriter().print("fail");
				return;
			}
			if(tagid.equals(""+srcdep.getParentId()))
			{
				response.getWriter().print("isparent");
			}
			else if(bo)
			{
				response.getWriter().print("exits");
			}
			else
			{
				conditionMap.put("corpCode", lgcorpcode);
				conditionMap.put("deppath&like2", srcdep.getDeppath());
				list = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
				String returnmsg = "";
				returnmsg = depBiz.ImproveEmployeeDep(list,srcdep,tagdep);
				if(returnmsg.equals("success")){
					response.getWriter().print("true");
					opSucLog(request, opModule, "调整员工机构[员工数，机构ID] （"+list.size()+","+srcdep.getDepId()+"-->"+tagdep.getDepId()+"）成功。", "UPDATE");
				}else{
					response.getWriter().print("fail");
				}
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"调整员工机构异常！");
			response.getWriter().print("fail");
	
		}
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
	 * 
	 * 2012-12-10
	 * @param phone要验证的号码
	 * @param name要求验证的姓名
	 * @param phones已经存在的号码合集
	 * @param checkFlag页面选择时候要过滤重号 1：验证；2：不验证
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
				int re = employeeBiz.checkMobile(mobile,haoduan);	
				if (re != 1)
				{
					response.getWriter().print("numfalse");
					return;
				}
			}
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			String corpCode = lfSysuser.getCorpCode();

			if (!"edit".equals(hidOpType))
			{
				conditionMap.put("employeeNo", eNo.toUpperCase());
				//将员工号转换成大写
				conditionMap.put("corpCode", corpCode);
				//将员工号转换成大写
				List<LfEmployee> le = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
				if(le != null && le.size()>0){
					response.getWriter().print("已存在该工号的员工记录，无须重复添加！");
					return;
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
				conditionMap.put("employeeNo", eNo.toUpperCase());
				//将员工号转换成大写
				conditionMap.put("corpCode", corpCode);
				//将员工号转换成大写
				List<LfEmployee> employeeList = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
				if(employeeList != null && employeeList.size()>0){
					LfEmployee employee = employeeList.get(0);
					String oldmoblie = employee.getMobile();
					int samecount = 0;
					if(oldmoblie.equals(mobile)){
						samecount ++;
					}
					conditionMap.clear();
					conditionMap.put("mobile", mobile);
					conditionMap.put("corpCode", corpCode);
					List<LfEmployee> mobileList = baseBiz.getByCondition(LfEmployee.class, conditionMap, null);
					if(mobileList != null && mobileList.size()>samecount){
						response.getWriter().print("phoneExist");
					}else{
						response.getWriter().print("true");
					}
				}
				
			}
		}
		catch (Exception e)
		{
				EmpExecutionContext.error(e,"处理员工记录是否重复！");
		}
	}
	
	/**添加员工机构
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void addDep(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
			String oppType =null, opContent =null;
			String opUser = getOpUser(request);
			//操作远的用户ID
			String lguserid = "";
			//企业编码
			String lgcorpcode = "";
		
		try
		{
			//获取登录sysuser
			LfSysuser sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工机构,doAdddep方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysUser.getCorpCode();
			//判断企业编码获取
			if(lgcorpcode==null||"".equals(lgcorpcode)){
				EmpExecutionContext.error("员工机构,doAdddep方法session中获取lgcorpcode出现异常");
				return;
			}
			// 判断USERID获取
			lguserid = sysUser.getUserId() + "";
			Long userId=0l;
			if(lguserid == null || "".equals(lguserid))
			{
				EmpExecutionContext.error("员工机构,addDep方法session中获取lguserid出现异常");
				return;
			}else{
				userId = Long.parseLong(lguserid);
			}
			
			//部门名称
			String name = request.getParameter("name");
			//部门id
			String scode = request.getParameter("scode");
			//自定义编码
			String depcodethird=request.getParameter("depcodethird");
			String output = "";
	    	oppType = StaticValue.ADD;
			opContent = "新增员工机构（机构名称："+name+"，机构编码："+depcodethird+")";
			//过滤机构id为空的情况
			if(scode==null||"".equals(scode))
			{
				output="";
			}
			else
			{
				if(depcodethird!=null && name != null){
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
						le.setDepName(name);
						le.setDepEffStatus("A");
						le.setDepcodethird(depcodethird.toUpperCase());
						le.setCorpCode(lgcorpcode);
						//增加部门时的企业编号
						
						//EnterpriseBiz emterBiz = new EnterpriseBiz();
						boolean result = false;
						//获取系统配置的机构信息的map
						LinkedHashMap<String, String> map=SystemGlobals.getSysParamLfcorpConf(lgcorpcode);
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
								opSucLog(request, opModule, opContent+"成功。", "ADD");
							}
							output = result+"";
						}
					
						
					}
				}
			}
			
			response.getWriter().print(output);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"新增员工机构失败！");
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
				EmpExecutionContext.error("员工机构,delete方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysUser.getCorpCode();
			//判断企业编码获取
			if(lgcorpcode==null||"".equals(lgcorpcode)){
				EmpExecutionContext.error("员工机构,delete方法session中获取lgcorpcode出现异常");
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
			EmpExecutionContext.error(e,"批量删除员工失败！");
		}
	}
	
	/**
	 * 删除员工机构  并且会删除员工
	 * @param request
	 * @param response
	 */
	public void delDep(HttpServletRequest request, HttpServletResponse response)
	{
		String oppType =null, opContent =null;
		String opUser = getOpUser(request);
		//企业编码
		String lgcorpcode = "";
		Long userId = null;
		
		try
		{
			//获取登录sysuser
			LfSysuser sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工机构,delDep方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysUser.getCorpCode();
			//判断企业编码获取
			if(lgcorpcode==null||"".equals(lgcorpcode)){
				EmpExecutionContext.error("员工机构,delDep方法session中获取lgcorpcode出现异常");
				return;
			}
			userId=sysUser.getUserId();
			if(userId==null){
				EmpExecutionContext.error("员工机构,delDep方法session中获取userId出现异常");
				return;
			}
			
			//需要删除 ID
			String bookIds = request.getParameter("id");
			LfEmployeeDep employeeDep = baseBiz.getById(LfEmployeeDep.class, bookIds);
			//如果是顶级机构，不允许被删除
			if(employeeDep != null && employeeDep.getDepLevel() == 1){
				response.getWriter().print(2);
				return;
			}
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.clear();
			conditionMap.put("memberId",bookIds);
			conditionMap.put("type", "1");
			conditionMap.put("corpCode",lgcorpcode);
			conditionMap.put("membertype", "2");
			List<LfBirthdayMember> birthdayMembers = baseBiz.getByCondition(LfBirthdayMember.class,conditionMap,null);
			if (birthdayMembers != null && birthdayMembers.size() > 0 ){
				response.getWriter().print("haveBirthdayMembers");
				EmpExecutionContext.info("员工机构管理-该机构有被员工生日祝福使用，不能删除。机构id：" + bookIds + "企业编码：" + lgcorpcode);
				return;
			}

			// 查询当前机构的上级机构在生日祝福中是否使用
			String deppath = employeeDep.getDeppath();
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
				EmpExecutionContext.info("员工机构管理-该机构的上级机构有被员工生日祝福使用，不能删除。机构id：" + bookIds + "企业编码：" + lgcorpcode);
				return;
			}
			//机构名称
			String name = request.getParameter("name");
			int result = employeeBiz.delEmployeeAddrDep(bookIds,lgcorpcode,userId);
			if(result >0)
			{
				oppType = StaticValue.DELETE;
				opContent = "删除员工机构（机构名称:"+name+"，机构ID:"+bookIds+"）";
				try{
					spLog.logSuccessString(opUser, opModule, oppType, opContent,lgcorpcode);
					opSucLog(request, opModule, opContent+"成功。", "DELETE");
				}catch (Exception e) {
					EmpExecutionContext.error(e, "删除员工机构写日志出现异常！");
				}
				
			}
			response.getWriter().print(result);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"删除员工机构失败！");
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
				EmpExecutionContext.error("员工机构,doEdit方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysUser.getCorpCode();
			//判断企业编码获取
			if(lgcorpcode==null||"".equals(lgcorpcode)){
				EmpExecutionContext.error("员工机构,doEdit方法session中获取lgcorpcode出现异常");
				return;
			}
			lguserid=sysUser.getUserId()+"";
			if(lguserid==null||"".equals(lguserid)){
				EmpExecutionContext.error("员工机构,doEdit方法session中获取userId出现异常");
				return;
			}
			LfEmployee lc = null;
			if (bookId != null && !"".equals(bookId))
			{
				lc = baseBiz.getById(LfEmployee.class, bookId);
			}
			if (lc == null)
			{
				lc = new LfEmployee();
			}else
			{
				LfEmployeeDep empDep = baseBiz.getById(LfEmployeeDep.class, lc.getDepId());
				request.setAttribute("depName", empDep == null?"":empDep.getDepName());
			}
			
			List<LfEmployeeTypeVo> bookInfoList = employeeBiz.getAllEmployeeTypeVo( Long.parseLong(lguserid),lgcorpcode);
			request.setAttribute("zwList", bookInfoList);
			request.setAttribute("employee", lc);
			request.getRequestDispatcher(empRoot + "/employee/epl_editEmployee.jsp").forward(request,
					response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"修改员工信息失败！"); 
		}
	}
	
	
	/**
	 *   修改机构的名称 
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void updateDepName(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String oppType =null, opContent =null;
		String opUser = getOpUser(request);
		String isResult = "0";
		String lgcorpcode = "";
		try
		{
			//获取登录sysuser
			LfSysuser sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工机构,updateDepName方法session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysUser.getCorpCode();
			//判断企业编码获取
			if(lgcorpcode==null||"".equals(lgcorpcode)){
				EmpExecutionContext.error("员工机构,updateDepName方法session中获取lgcorpcode出现异常");
				return;
			}
			BaseBiz baseBiz = new BaseBiz();
			String depName = request.getParameter("depName");	
			//需要修改的名称
			String depId = request.getParameter("depCode");	
			//机构编码
			//String type = request.getParameter("type");			
			//点的是否是树的顶级机构  1不是   2是的
			oppType = StaticValue.UPDATE;
	    	opContent = "修改员工机构（机构名称:"+depName+")";
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
		//	String corpCode = getCorpCode();			
			//企业编码
			conditionMap.put("depId", depId);
			List<LfEmployeeDep> deps = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
			if(deps != null && deps.size()>0){
				LfEmployeeDep dep = deps.get(0);
				//if("2".equals(type)){	
				//同级机构下是否存在相同的机构名
				conditionMap.clear();
				conditionMap.put("parentId", dep.getParentId().toString());
				conditionMap.put("corpCode", lgcorpcode);
				conditionMap.put("depName", depName);
				List<LfEmployeeDep> employeeDeps = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
				if(employeeDeps != null && employeeDeps.size()>0){
					isResult = "3";		
					//同级存在相同的机构
				}
				//}
				if(!"3".equals(isResult)){
					String _depName = dep.getDepName();
					dep.setDepName(depName);
					boolean returnFlag = baseBiz.updateObj(dep);
					if(returnFlag){
						isResult = "1";		
						//修改成功
				    	spLog.logSuccessString(opUser, opModule, oppType, opContent,lgcorpcode);
				    	opSucLog(request, opModule, "修改员工机构[机构ID,机构名称] ("+dep.getDepId()+","+_depName+"-->"+depName+")成功。","UPDATE");
					}else{
						isResult = "2";		
						//修改失败
					}
				}
			}else{
				isResult = "4";	
				//数据库之类的出错 没有查询结果 
			}
			
			response.getWriter().print(isResult);
		}
		catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"修改员工机构名称失败！");
			spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, e,lgcorpcode);
		}
	}
	
	/**
	 *   进入新增员工页面前查询 职务LIST
	 * @param request
	 * @param response
	 */
	public void toAddEmployeePage(HttpServletRequest request, HttpServletResponse response)
	{
		Long userId = null;
		//企业编码
		String corpCode = "";
		
		List<LfEmployeeTypeVo> bookInfoList = null;
		//查询信息
		try
		{
			
			//获取登录sysuser
			LfSysuser sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工机构,toAddEmployeePage方法session中获取当前登录对象出现异常");
				return;
			}
			corpCode=sysUser.getCorpCode();
			//判断企业编码获取
			if(corpCode==null||"".equals(corpCode)){
				EmpExecutionContext.error("员工机构,toAddEmployeePage方法session中获取corpCode出现异常");
				return;
			}
			
			bookInfoList = employeeBiz.getAllEmployeeTypeVo( userId,corpCode);
			request.setAttribute("zwList", bookInfoList);
			request.getRequestDispatcher(empRoot  + "/employee/epl_addEmployee.jsp").forward(request,
					response);
		}
		catch (Exception e)
		{
			request.setAttribute("zwList", bookInfoList);
			EmpExecutionContext.error(e,"新增员工界面失败！");
		}
	}
	/**
	 * @param request
	 * @param response
	 * @throws IOException 
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
		    	opContent = "批量修改员工机构（调整后机构ID:"+depId+",共修改:"+id.length+"条)";	
		    	spLog.logSuccessString(opUser, opModule, oppType, opContent,lfSysuser.getCorpCode());
		    	opSucLog(request, opModule, opContent+"成功。","UPDATE");
			}
			
			response.getWriter().print(result);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"修改员工机构失败！");
			response.getWriter().print("false");
		}
	}

	
	
	
	
	
	
	// 批量导出员工
	public void exportEmployeeExcel(HttpServletRequest request,HttpServletResponse response) {
		
		try {
			//pageSet(request);
			
			String context = request.getSession(false).getServletContext().getRealPath(excelPath);

			//获取当前企业编码与用户ID
			String lgUserId = "";
			String corpCode = "";
			String lgGuId = "";
			
			//获取登录sysuser
			LfSysuser sysUser =depPriBiz.getCurrenUser(request);
			if(sysUser==null){
				EmpExecutionContext.error("员工机构,exportEmployeeExcel方法session中获取当前登录对象出现异常");
				return;
			}
			corpCode=sysUser.getCorpCode();
			//判断企业编码获取
			if(corpCode==null||"".equals(corpCode)){
				EmpExecutionContext.error("员工机构,exportEmployeeExcel方法session中获取corpCode出现异常");
				return;
			}
			
			lgUserId=sysUser.getUserId()+"";
			//判断企业编码获取
			if(lgUserId==null||"".equals(lgUserId)){
				EmpExecutionContext.error("员工机构,exportEmployeeExcel方法session中获取lgUserId出现异常");
				return;
			}
			
			lgGuId=sysUser.getGuId()+"";
			//判断lgGuId
			if(lgGuId==null||"".equals(lgGuId)){
				EmpExecutionContext.error("员工机构,exportEmployeeExcel方法session中获取lgGuId出现异常");
				return;
			}
			
			PageInfo pageInfo=new PageInfo();

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
						//获取导出的文件名
						String fileName = resultMap.get("FILE_NAME");
						//获取导出的路径
						String filePath = resultMap.get("FILE_PATH");
						//下载文件
						DownloadFile dfs = new DownloadFile();
						opSucLog(request, opModule, "批量导出员工记录（"+ resultMap.size()+"条）成功。", "OTHER");
						dfs.downFile(request, response, filePath, fileName);
					//如果导出内容为空
					} else {
						//跳转到find页面
						response.sendRedirect(request.getContextPath()+"/epl_employeeBook.htm?lgguid="+lgGuId);
					}
				} else {
					response.sendRedirect(request.getContextPath()+"/epl_employeeBook.htm?lgguid="+lgGuId);
				}
			} else {
				response.sendRedirect(request.getContextPath()+"/epl_employeeBook.htm?lgguid="+lgGuId);
			}
			
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"批量导出员工失败！");
			try {
				request.getSession(false).setAttribute("error", e);
			} catch (Exception e1) {
				EmpExecutionContext.error(e1,"session为null");
			}
		}
	}
	
	
	
	
	
	public void toAdd(HttpServletRequest request,HttpServletResponse response)
	{
		String lgguid = "";
		HttpSession session = request.getSession(false);
		try
		{
			//获取登录sysuser
			LfSysuser sysuser =depPriBiz.getCurrenUser(request);
			if(sysuser==null){
				EmpExecutionContext.error("员工机构,exportEmployeeExcel方法session中获取当前登录对象出现异常");
				return;
			}
			
			lgguid=sysuser.getGuId()+"";
			//判断lgGuId
			if(lgguid==null||"".equals(lgguid)){
				EmpExecutionContext.error("员工机构,exportEmployeeExcel方法session中获取lgguid出现异常");
				return;
			}
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
			EmpExecutionContext.error(e,"跳转新增操作员页面失败！");
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
		EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
	}
}
