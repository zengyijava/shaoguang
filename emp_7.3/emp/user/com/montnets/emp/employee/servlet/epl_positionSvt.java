package com.montnets.emp.employee.servlet;


import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.employee.biz.EmployeeBookBiz;
import com.montnets.emp.employee.vo.LfEmployeeTypeVo;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeType;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;




/**
 * 职位管理
 * type_custormAddrBookServlet
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class epl_positionSvt extends BaseServlet {

	/*private EmployeeAddrBookBiz employeeBiz = new EmployeeAddrBookBiz();*/
	private final EmployeeBookBiz employeeBiz = new EmployeeBookBiz();
	//private EnterpriseBiz eb = new EnterpriseBiz();
	//private AddrBookBiz addrBookBiz = new AddrBookBiz();
	private final String empRoot = "user";
	private final String basePath = "/employee";
	public static final String opModule = "职位管理";
	final BaseBiz baseBiz=new BaseBiz();
	
	/**进入FIND方法，
	 * @param request
	 * @param response
	 */
   public void find(HttpServletRequest request, HttpServletResponse response) {
  		try {
  			request.getRequestDispatcher(empRoot + basePath +  "/epl_position.jsp").forward(
  					request, response);
  		} catch (Exception e) {
  			EmpExecutionContext.error(e, "跳转页面出现异常！");
  			try {
				request.getSession(false).setAttribute("error", e);
			} catch (Exception e1) {
				EmpExecutionContext.error(e1,"session为null");
			}
  		}
  	}
	     

	/**
	 * @param request
	 * @param response
	 */
	public void getTable(HttpServletRequest request, HttpServletResponse response)
	{
			long startTime = System.currentTimeMillis();
			//登录者的GUID
			String guid = request.getParameter("lgguid");
			Long lgguid = null;
			LfSysuser sysUser = null;
			String corpCode = "";
			Long userId = null;
			try{
				lgguid = Long.valueOf(guid);
				//登录者对象
				sysUser = baseBiz.getByGuId(LfSysuser.class, lgguid);
				//登录着的用户ID
				userId = sysUser.getUserId();
				//企业编码
				corpCode = sysUser.getCorpCode();
			}catch (Exception e) {
				EmpExecutionContext.error(e,"职位管理获取当前操作员对象失败！");
			}
		
		try {
			
			//是否第一次打开
			PageInfo pageInfo=new PageInfo();
			pageSet(pageInfo, request);
			//获取自定义人员的列表
			List<LfEmployeeTypeVo> bookInfoList = employeeBiz.getEmployeeTypeVo( userId,corpCode,pageInfo);
			HttpSession session=request.getSession(false);
			session.setAttribute("bookInfoList", bookInfoList);
			request.setAttribute("bookType", "employee");
			request.setAttribute("pageInfo", pageInfo);

			//格式化时间
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			//记录操作日志
			String opContent = "开始："+sdf.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + pageInfo.getTotalRec();
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			EmpExecutionContext.info("职位管理", lfSysuser.getCorpCode(), lfSysuser.getUserId().toString(), lfSysuser.getUserName(), opContent, "GET");
			
			request.getRequestDispatcher(empRoot + basePath + "/epl_positionTable.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"跳转职位管理页面失败！");
		}
	}
	
	public void getAllType(HttpServletRequest request, HttpServletResponse response)
	{
		try {
			response.setContentType("text/html;charset=UTF-8");
			//是否第一次打开
			boolean isFirstEnter = false;
			PageInfo pageInfo=new PageInfo();
			isFirstEnter=pageSet(pageInfo, request);
			LfSysuser lfSysuser=(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			List<LfEmployeeTypeVo> bookInfoList = employeeBiz.getAllEmployeeTypeVo( lfSysuser.getUserId(),lfSysuser.getCorpCode());
			
			String str = "[";
			for(int i=0;i<bookInfoList.size();i++){
				LfEmployeeTypeVo lv = bookInfoList.get(i);
				String temp = "{id:"+lv.getId()+",name:'"+lv.getName()+"'}";
				str = str+temp;
				if(i==bookInfoList.size()-1){
					str = str+"]";
				}
				else{
					str = str+",";
				}
				
			}
			if(bookInfoList.size()==0){
				str = str +"]";
			}
			response.getWriter().print(str);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取类型信息异常！");
			try {
				request.getSession(false).setAttribute("error", e);
			} catch (Exception e1) {
				EmpExecutionContext.error(e1,"session为null");
			}
		}
	}
	
	/**新增职位
	 * @param request
	 * @param response
	 */
	public void addType(HttpServletRequest request, HttpServletResponse response)
	{
		
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		LfSysuser sysUser = null;
		String corpCode = "";
		Long userId = null;
		try{
			userId = Long.valueOf(lguserid);
			sysUser = baseBiz.getById(LfSysuser.class, userId);
			userId = sysUser.getUserId();
			corpCode = sysUser.getCorpCode();
		}catch (Exception e) {
			EmpExecutionContext.error(e,"职位管理获取当前操作员对象失败！");
		}
		try
		{
			String zhiweiType = request.getParameter("zhiweiType").trim();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("name",zhiweiType);
			conditionMap.put("corpcode",corpCode);
			List<LfEmployeeType> typeList = baseBiz.getByCondition(LfEmployeeType.class, conditionMap, null);
			if(typeList != null && typeList.size()>0){
				response.getWriter().print("mid");
				return;
			}
			LfEmployeeType type = new LfEmployeeType();
			type.setCorpcode(corpCode);
			type.setName(zhiweiType);
			type.setUserId(userId);
			boolean b =false;
			b =  baseBiz.addObj(type);
		//	boolean b = employeeBiz.addEmployeeTypeVo( userId,zhiweiType,corpCode);
			if(b){
				opSucLog(request, opModule, "新增职位（名称："+zhiweiType+"）成功。","ADD");
				response.getWriter().print("ok");
			}else{
				response.getWriter().print("fail");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"新增职位失败！");
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
			//手机号码
			String mobile =  request.getParameter("mobile"); 
			String eNo =  request.getParameter("eNo"); 
			String hidOpType =  request.getParameter("hidOpType");
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
			if (!"edit".equals(hidOpType))
			{
				LinkedHashMap<String,String> cond = new LinkedHashMap<String,String>();
				cond.put("employeeNo", eNo);
				List<LfEmployee> le = baseBiz.getByCondition(LfEmployee.class, cond, null);
				if(le != null && le.size() != 0)
				{
					response.getWriter().print("已存在该工号的员工记录，无须重复添加！");
				}
				else
				{
					response.getWriter().print("true");
				}
			}
			else
			{
				response.getWriter().print("true");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"验证员工是否存在失败！");
		}
	}
	
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String corpCode = request.getParameter("corpCode");
		
		try
		{
			String bookIds = request.getParameter("bookIds");
			LinkedHashMap<String, String> condMap = new LinkedHashMap<String, String>();
			condMap.put("id&in",bookIds);
			List<LfEmployeeType> types = baseBiz.getByCondition(LfEmployeeType.class,condMap , null);
			String des = "(";
			for (LfEmployeeType type:types)
			{
				des += "["+type.getId()+","+type.getName()+"],";
			}
			des += ")";
			String id[] = bookIds.split(",");
			for (int i = 0; i<id.length;i++)
			{
				employeeBiz.delEmployeeTypeById(corpCode,Integer.parseInt(id[i]));
			}
			opSucLog(request, opModule, "删除职位（总数："+id.length+"）[职位ID,职位名称]"+des+"成功。","DELETE");
			 response.getWriter().print("suceess");
		}catch (Exception e)
		{
			response.getWriter().print("errer");
			EmpExecutionContext.error(e,"批量删除职位失败 ！");
		}
	}
	
	
	/**
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	//修改职位
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//登录者的用户ID
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		LfSysuser sysUser = null;
		String corpCode = "";
		Long userId = null;
		try{
				userId = Long.valueOf(lguserid);
			sysUser = baseBiz.getById(LfSysuser.class, userId);
			userId = sysUser.getUserId();
			corpCode = sysUser.getCorpCode();
		}catch (Exception e) {
			EmpExecutionContext.error(e,"职位管理获取当前操作员对象失败！");
		}
		try
		{
			//对职位的修改
			String id = request.getParameter("id");
			LfEmployeeType type = baseBiz.getById(LfEmployeeType.class, id);
			if(type != null){
				 String typename = request.getParameter("zhiweiType").trim();
				 String _typename = type.getName();
				// type.setName(typename);
				// boolean b = employeeBiz.updEmployeeTypeById(userId,corpCode,Integer.parseInt(id),typename);
				 LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				 conditionMap.put("name", typename);
				 conditionMap.put("corpcode",corpCode);
				 List<LfEmployeeType> typeList = baseBiz.getByCondition(LfEmployeeType.class, conditionMap, null) ;
				 if(typeList != null && typeList.size()>0){
					 response.getWriter().print("mid");
				 }else{
					 if(employeeBiz.updateEmployeePosition(type, typename)){
						 opSucLog(request, opModule, "修改职位（名称："+_typename+" --> "+typename+"）成功。","UPDATE");
						 response.getWriter().print("ok");
					 }else {
						 response.getWriter().print("fail");
					 }
				 }
				
			}
		}
		catch (Exception e)
		{
			response.getWriter().print("errer");
			EmpExecutionContext.error(e,"修改职位失败！");
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
		EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
	}	
}
