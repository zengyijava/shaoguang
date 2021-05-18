package com.montnets.emp.shorturl.surlmanage.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.shorturl.surlmanage.biz.UrlDomianBiz;
import com.montnets.emp.shorturl.surlmanage.entity.LfDomain;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;






/**
 * 管理源地址servlet  查询 添加  修改
 * @ClassName:Surl_managerSvt.java
 * @Description:TODO
 * @author ouyangyu
 * @date:2018-3-5下午03:18:14
 */
@SuppressWarnings("serial")
public class Surl_domainSvt extends BaseServlet{
	
	//操作模块
	final String opModule="短域名管理";
	//操作用户
	final String opSper = StaticValue.OPSPER;
	
	//公共biz类实例化
	final BaseBiz baseBiz=new BaseBiz();
	final UrlDomianBiz urlbiz = new UrlDomianBiz();

	final ErrorLoger errorLoger = new ErrorLoger();
	
	//页面模块文件名称
	private final String empRoot = "shorturl";
	//页面功能文件名称
	private final String basePath ="/urldomian";
	
	
	/**
	 * 查看
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {

		//查询结果模板volist
		List<LfDomain> urlList = null;
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageSize(10);
		
		//条件map 
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			//初始化分页信息
			pageSet(pageInfo, request);
			//当前机构编码
			String lgcorpcode = request.getParameter("lgcorpcode");
			//短域名
			String domain = request.getParameter("domain");
			//应用类型
			String dtype = request.getParameter("dtype");
			//状态
			String flag = request.getParameter("flag");
			//创建人
			String createUser = request.getParameter("createUser");
			//创建开始时间
			String startTime = request.getParameter("startTime");
			//创建结束时间
			String recvtime = request.getParameter("recvtime");
			
			LfSysuser sysuser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
            //当前登录操作员id
			Long lguserid = sysuser.getUserId();
			lgcorpcode = sysuser.getCorpCode();
			//企业名称
			String lgcorpName = sysuser.getName();
			
			conditionMap.put("domain", domain);
			conditionMap.put("dtype", dtype);
			conditionMap.put("flag", flag);
			conditionMap.put("createUser", createUser);
			conditionMap.put("startTime", startTime);
			conditionMap.put("recvtime", recvtime);
			
			//当前登录人id
			conditionMap.put("lguserid", lguserid.toString());
			conditionMap.put("lgcorpcode", lgcorpcode.toString());
			conditionMap.put("lgcorpName", lgcorpName.toString());
			
			urlList = urlbiz.getDomains(conditionMap, pageInfo);
			
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("urlList", urlList);
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("lgcorpcode",lgcorpcode);
			request.getRequestDispatcher(this.empRoot  + this.basePath+"/url_domian.jsp")
			.forward(request, response);
			
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"短链报备查询异常！");
			// 异常错误
			request.getSession(false).setAttribute("error", e);
			request.setAttribute("findresult", "-1");
			// 分页信息
			request.setAttribute("pageInfo", pageInfo);
			try
			{
				request.getRequestDispatcher(this.empRoot + this.basePath + "/url_domian.jsp").forward(request, response);
			}
			catch (ServletException e1)
			{
				EmpExecutionContext.error(e1, "短链报备查询后跳转异常");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1, "短链报备查询后跳转IO异常");
			}
		}
	}
	
	
	/**
	 * 进入新建短域名界面
	 * @param request
	 * @param response
	 */
	public void toAdd(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			request.getRequestDispatcher(empRoot  + basePath +"/url_addDomian.jsp")
				.forward(request, response);
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,"新建短域名页面跳转异常！");
		}
	}
	
	/**
	 * 新建短域名
	 * @param request
	 * @param response
	 */
	public void add(HttpServletRequest request, HttpServletResponse response)
	{	
		String opUser = "";
		String corpcode = "";
		//日志内容
		String opContent = null;
		//修改前数据
		StringBuffer oldStr=new StringBuffer("");
		//修改后数据
		StringBuffer newStr=new StringBuffer("");
		//修改字段字符串
		String updateContent="";
		SuperOpLog opLog = new SuperOpLog();
		
		try{
			//短域名
			String domain = request.getParameter("domain");
			//总长度
			String lenAll = "";
			//全局扩展位数
			String lenExten = request.getParameter("lenExten");
			//域名类别  0 公用  1专用
			String dtype = request.getParameter("dtype");
			//域名状态  0  有效    -1 无效
			String flag = "0";
			//有效时间，单位天 , 30  ,值不能为0 
			String validDays = request.getParameter("validDays");
			//创建人员
			String createUid = "";
			String createUser = "";
			//创建时间
			Timestamp createTm = new Timestamp(System.currentTimeMillis());
			//最后修改人员ID 
			String updateUid = "";
			//最后修改时间
			Timestamp updateTm = new Timestamp(System.currentTimeMillis());
			String remark = "";
			//添加成功操作日志
			LfSysuser sysuser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			
			
			LfDomain lfDomain=new LfDomain();

			// 去除空格
            if(domain != null) {
                domain = domain.trim();
            }

			lfDomain.setDomain(domain);

			

			
			if(lenExten!=null&&!"".equals(lenExten)){
				if(lenExten.indexOf(".")!=-1){
					response.getWriter().print(false);
					return;
				}
				lfDomain.setLenExten(Long.valueOf(lenExten));
				lfDomain.setLenAll(domain.length()+Integer.valueOf(lenExten)+1);
			}else{
				lfDomain.setLenExten(0l);
				lfDomain.setLenAll(domain.length());
			}
			
			if(dtype!=null&&!"".equals(dtype)){
				lfDomain.setDtype(Integer.valueOf(dtype));
			}else{
				lfDomain.setDtype(0);
			}
			
			if(flag!=null&&!"".equals(flag)){
				lfDomain.setFlag(Integer.valueOf(flag));
			}else{
				lfDomain.setFlag(0);
			}
			
			if(validDays!=null&&!"".equals(validDays)){
				if(validDays.indexOf(".")!=-1){
					response.getWriter().print(false);
					return;
				}	
				lfDomain.setValidDays(Long.valueOf(validDays));
			}else{
				lfDomain.setValidDays(30l);
			}
			
			lfDomain.setCreateUid(sysuser.getUserId());

			lfDomain.setCreateTm(createTm);
			
			lfDomain.setUpdateUid(sysuser.getUserId());
			lfDomain.setCreateUser(sysuser.getUserName());
			
			lfDomain.setUpdateTm(updateTm);
			
			lfDomain.setRemark(remark);

			boolean result = false;
			result = urlbiz.addLfDomain(lfDomain);
			
			
            //当前登录操作员id
			Long lguserid = sysuser.getUserId();
			corpcode = sysuser.getCorpCode();
			opUser = sysuser.getUserName();
			
			
			newStr.append(lfDomain.getDomain()).append(",").append(lfDomain.getLenExten())
			.append(",").append(lfDomain.getDtype()).append(",").append(lfDomain.getValidDays())
			.append(",").append(lfDomain.getCreateTm());
			
			
			
			if (result)
			{
				opContent = "短域名，全局扩展位数，访问时效上限，应用类型"+newStr.toString();
				setLog(request, "短域名管理", opContent, StaticValue.ADD);
				opLog.logSuccessString(opUser, opModule, StaticValue.ADD, opContent,corpcode);
				
				response.getWriter().print(true);
			}
			else
			{
				opContent = "短域名，全局扩展位数，访问时效上限，应用类型"+newStr.toString();
				setLog(request, "短域名管理", opContent, StaticValue.ADD);
				opLog.logFailureString(opUser, opModule, StaticValue.ADD, opContent,null,corpcode);
				
				response.getWriter().print(false);
			}
			
			
		}catch(Exception e){
			//添加失败操作日志
			EmpExecutionContext.error(e,"新建短域名异常");
			opContent = "短域名，全局扩展位数，访问时效上限，应用类型"+newStr.toString();
			setLog(request, "短域名管理", opContent, StaticValue.ADD);
			opLog.logFailureString(opUser, opModule, StaticValue.ADD, opContent,null,corpcode);
			
		}
	}
	
	/**
	 * 跳转到修改短域名管理页面
	 * @param request
	 * @param response
	 */
	public void toEdit(HttpServletRequest request, HttpServletResponse response)
	{

		//查询结果模板volist
		LfDomain lfDomain = null;
		PageInfo pageInfo = new PageInfo();
		pageInfo.setPageSize(10);
		//条件map 
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			
			String  id = request.getParameter("id");
			lfDomain = baseBiz.getById(LfDomain.class, id);
			LfSysuser sysuser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
            //当前登录操作员id
			Long lguserid = sysuser.getUserId();
			String lgcorpcode = sysuser.getCorpCode();
			//企业名称
			String lgcorpName = sysuser.getName();

			
			//当前登录人id
			conditionMap.put("lguserid", lguserid.toString());
			conditionMap.put("lgcorpcode", lgcorpcode.toString());
			conditionMap.put("lgcorpName", lgcorpName.toString());

			
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lfDomain", lfDomain);
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("lgcorpcode",lgcorpcode);
			request.getRequestDispatcher(this.empRoot  + this.basePath+"/url_editDomian.jsp")
			.forward(request, response);
			
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"短链报备查询异常！");
			// 异常错误
			request.getSession(false).setAttribute("error", e);
			request.setAttribute("findresult", "-1");
			// 分页信息
			request.setAttribute("pageInfo", pageInfo);
			try
			{
				request.getRequestDispatcher(this.empRoot + this.basePath + "/url_editDomian.jsp").forward(request, response);
			}
			catch (ServletException e1)
			{
				EmpExecutionContext.error(e1, "短链报备查询后跳转异常");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1, "短链报备查询后跳转IO异常");
			}
		}
	}
	
	/**
	 * 修改短域名管理界面
	 * @param request
	 * @param response
	 */
	public void update(HttpServletRequest request, HttpServletResponse response)
	{	
		String opUser = "";
		String corpcode = "";
		//日志内容
		String opContent = null;
		//修改前数据
		StringBuffer oldStr=new StringBuffer("");
		//修改后数据
		StringBuffer newStr=new StringBuffer("");
		//修改字段字符串
		String updateContent="";
		SuperOpLog opLog = new SuperOpLog();		
		
		try{
			String id = request.getParameter("id");
			
			//短域名
			String domain = request.getParameter("domain");
			String lenExten = request.getParameter("lenExten");
			//域名类别  0 公用  1专用
			String dtype = request.getParameter("dtype");
			//有效时间，单位天 , 30  ,值不能为0 
			String validDays = request.getParameter("validDays");
			//最后修改人员ID 
			String updateUid = "";
			//最后修改时间
			Timestamp updateTm = new Timestamp(System.currentTimeMillis());;

			LfDomain lfDomain = baseBiz.getById(LfDomain.class, id);
			
			lfDomain.setDomain(domain);
			
			if(lenExten!=null&&!"".equals(lenExten)){
				lfDomain.setLenExten(Long.valueOf(lenExten));
				
				lfDomain.setLenAll(lfDomain.getDomain().length()+1+Integer.valueOf(lenExten));
			}else{
				lfDomain.setLenExten(0l);
			}
			
			
			
			if(dtype!=null&&!"".equals(dtype)){
				lfDomain.setDtype(Integer.valueOf(dtype));
			}else{
				lfDomain.setDtype(0);
			}
			
			if(validDays!=null&&!"".equals(validDays)){
				lfDomain.setValidDays(Long.valueOf(validDays));
			}else{
				lfDomain.setValidDays(0l);
			}
			
			
			if(updateUid!=null&&!"".equals(updateUid)){
				lfDomain.setUpdateUid(Long.valueOf(updateUid));
			}else{
				lfDomain.setUpdateUid(0l);
			}
			
			lfDomain.setUpdateTm(updateTm);

			boolean result = false;
			result = urlbiz.updateLfDomain(lfDomain);
			
			//添加成功操作日志
			LfSysuser sysuser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
            //当前登录操作员id
			Long lguserid = sysuser.getUserId();
			corpcode = sysuser.getCorpCode();
			opUser = sysuser.getUserName();
			
			
			oldStr.append(domain).append(",").append(lenExten)
			.append(",").append(dtype).append(",").append(validDays)
			.append(",").append(lfDomain.getUpdateTm());
			
			newStr.append(lfDomain.getDomain()).append(",").append(lfDomain.getLenExten())
			.append(",").append(lfDomain.getDtype()).append(",").append(lfDomain.getValidDays())
			.append(",").append(lfDomain.getCreateTm());

			
			
			
			if (result)
			{	
				opContent = "短域名，全局扩展位数，访问时效上限，应用类型"+"（"+oldStr.toString()+"-->"+newStr.toString()+"）";
				setLog(request, "短域名管理", opContent, StaticValue.UPDATE);
				opLog.logSuccessString(opUser, opModule, StaticValue.UPDATE, opContent,corpcode);

				response.getWriter().print(true);
			}
			else
			{
				opContent = "短域名，全局扩展位数，访问时效上限，应用类型"+"（"+oldStr.toString()+"-->"+newStr.toString()+"）";
				setLog(request, "短域名管理", opContent, StaticValue.UPDATE);
				opLog.logFailureString(opUser, opModule, StaticValue.UPDATE, opContent,null,corpcode);
				
				response.getWriter().print(false);
			}

		}catch(Exception e){
			//添加失败操作日志
			EmpExecutionContext.error(e,"新建短域名异常");
			opContent = "短域名，全局扩展位数，访问时效上限，应用类型"+"（"+oldStr.toString()+"-->"+newStr.toString()+"）";
			setLog(request, "短域名管理", opContent, StaticValue.UPDATE);
			opLog.logFailureString(opUser, opModule, StaticValue.UPDATE, opContent,null,corpcode);
	
		}
	}
	
	/**
	 * 修改通道状
	 * @param request
	 * @param response
	 */
	public void changeState(HttpServletRequest request, HttpServletResponse response)
	{
		String opUser = "";
		String corpcode = "";
		//日志内容
		String opContent = null;
		//修改前数据
		StringBuffer oldStr=new StringBuffer("");
		//修改后数据
		StringBuffer newStr=new StringBuffer("");
		//修改字段字符串
		String updateContent="";
		SuperOpLog opLog = new SuperOpLog();
		
		
		
		
		boolean statuFlag;
		PrintWriter writer = null;
		//条件map 
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LfDomain domain = null;
		try {
			writer = response.getWriter();
			String id = request.getParameter("id");
			//用于记录日志
			conditionMap.put("id", String.valueOf(id));
			domain = baseBiz.getById(LfDomain.class, id);

			statuFlag = urlbiz.changeState(Long.parseLong(id));
			
			LfSysuser sysuser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
            //当前登录操作员id
			Long lguserid = sysuser.getUserId();
			corpcode = sysuser.getCorpCode();
			opUser = sysuser.getUserName();
			
			
			oldStr.append(domain.getDomain()).append(",").append(domain.getLenExten())
			.append(",").append(domain.getDtype()).append(",").append(domain.getValidDays())
			.append(",").append(domain.getFlag()).append(",").append(domain.getCreateTm());
			
			domain = baseBiz.getById(LfDomain.class, id);
			newStr.append(domain.getDomain()).append(",").append(domain.getLenExten())
			.append(",").append(domain.getDtype()).append(",").append(domain.getValidDays())
			.append(",").append(domain.getFlag()).append(",").append(domain.getCreateTm());
			
			if(statuFlag){
				opContent = "短域名，全局扩展位数，访问时效上限，应用类型,状态"+"（"+oldStr.toString()+"-->"+newStr.toString()+"）";
				setLog(request, "短域名管理", opContent, StaticValue.UPDATE);
				opLog.logSuccessString(opUser, opModule, StaticValue.UPDATE, opContent,corpcode);
				
			}else{
				opContent = "短域名，全局扩展位数，访问时效上限，应用类型,状态"+"（"+oldStr.toString()+"-->"+newStr.toString()+"）";
				setLog(request, "短域名管理", opContent, StaticValue.UPDATE);
				opLog.logFailureString(opUser, opModule, StaticValue.UPDATE, opContent,null,corpcode);
				
			}

			writer.print(statuFlag);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"修改状态异常！");
			opContent = "短域名，全局扩展位数，访问时效上限，应用类型,状态"+"（"+oldStr.toString()+"-->"+newStr.toString()+"）";
			setLog(request, "短域名管理", opContent, StaticValue.UPDATE);
			opLog.logFailureString(opUser, opModule, StaticValue.UPDATE, opContent,null,corpcode);

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
	private void setLog(HttpServletRequest request,String opModule,String opContent,String opType){
		try
		{
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, opType);
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,opModule+opType+opContent+"日志写入异常"));
		}
	}
	
}
