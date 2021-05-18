package com.montnets.emp.shorturl.surlmanage.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.lang.StringUtils;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.shorturl.surlmanage.biz.NoticeStopBiz;
import com.montnets.emp.shorturl.surlmanage.biz.UrlManagerBiz;
import com.montnets.emp.shorturl.surlmanage.entity.LfNeturl;
import com.montnets.emp.shorturl.surlmanage.vo.LfNeturlVo;
import com.montnets.emp.util.PageInfo;

/**
 * 管理源地址servlet  查询 添加  删除
 * @ClassName:Surl_managerSvt.java
 * @Description:TODO
 * @author Administrator
 * @date:2018-1-11下午03:18:14
 */
@SuppressWarnings("serial")
public class  Surl_managerSvt extends BaseServlet{
	
	//公共biz类实例化
	final BaseBiz baseBiz=new BaseBiz();
	
	final UrlManagerBiz urlbiz = new UrlManagerBiz();
	
	final NoticeStopBiz noticebiz = new NoticeStopBiz();
	
	
	//页面模块文件名称
	private final String empRoot = "shorturl";
	//页面功能文件名称
	private final String basePath ="/urlmanage";
	
	/**
	 * 查看
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {
		
		//查询结果模板volist
		List<LfNeturlVo> urlList = null;
		PageInfo pageInfo = new PageInfo();
		//机构树
//		String departmentTree = "";
		
		//条件map 
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			//初始化分页信息
			pageSet(pageInfo, request);
			//当前机构编码
			String lgcorpcode = request.getParameter("lgcorpcode");
			//连接名称
			String urlname = request.getParameter("urlname");
			//连接地址
			String srcurl = request.getParameter("srcurl");
			//emp启用状态
			String urlstate = request.getParameter("urlstate");
			//运营商审批状态
			String  ispass = request.getParameter("ispass");
			//创建人
			String creatuser = request.getParameter("creatuser");
			//机构
			String depId = request.getParameter("depid");
			//机构名称
			String depNam = request.getParameter("depNam");
			
			//创建开始时间
			String startTime = request.getParameter("startTime");
			//创建结束时间
			String recvtime = request.getParameter("recvtime");
			
			//是否包含子机构
            String isContainsSun=request.getParameter("isContainsSun");
            
            //是否包含子机构
			if(isContainsSun==null||"".equals(isContainsSun)){
				isContainsSun = "0";
			}else{
				isContainsSun = "1";
			}
			LfSysuser sysuser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
            //当前登录操作员id
			Long lguserid = sysuser.getUserId();
			lgcorpcode = sysuser.getCorpCode();
			
			conditionMap.put("urlname", urlname);
			conditionMap.put("srcurl", srcurl);
			//设置 连接状态 条件   1：禁用 0：启用 
			conditionMap.put("urlstate", urlstate);
			//设置 运营商审批状态 条件 审批状态 -1:删除   0：未审批, 1：无需审批 ,2：审批通过 ,3：审批未通过 
			conditionMap.put("ispass", ispass);
			//创建人
			conditionMap.put("creatuser",creatuser);
			//机构id 被选中的机构
			conditionMap.put("depId", depId);
			conditionMap.put("depNam", depNam);
			conditionMap.put("startTime", startTime);
			conditionMap.put("recvtime", recvtime);
			//当前登录人id
			conditionMap.put("lguserid", lguserid.toString());
			conditionMap.put("isContainsSun", isContainsSun);
			
			urlList = urlbiz.getNeturlVos(conditionMap, pageInfo);
			
			//加载机构树
//			departmentTree = busTypeBiz.getDepartmentJosnData(lguserid,lgcorpcode);
//			request.setAttribute("departmentTree", departmentTree);
			
			//查询创建人名map
			//Map<String, String> usersMap =  urlbiz.getAllUser(urlList);
			//request.setAttribute("usersMap", usersMap);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("urlList", urlList);
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("lgcorpcode",lgcorpcode);
			request.getRequestDispatcher(this.empRoot  + this.basePath+"/url_manage.jsp")
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
				request.getRequestDispatcher(this.empRoot + this.basePath + "/url_manage.jsp").forward(request, response);
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
	 * 新建源地址
	 * @param request
	 * @param response
	 */
	public void add(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//链接名
		String urlname = request.getParameter("urlname");
		//链接描述
		String urlmsg = request.getParameter("urlmsg");
		//谅解地址
		String srcurl= request.getParameter("srcurl");
		if (!StringUtils.isBlank(srcurl)) {
			srcurl = srcurl.replace("|", "&");
		}
		try {
			//获取登录sysuser
			LfSysuser sysuser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			if(sysuser==null){
				EmpExecutionContext.error("添加链接地址,session中获取当前登录对象出现异常");
				response.getWriter().print(false);
				return ;
			}
			LfNeturl lfNeturl = new LfNeturl();
			lfNeturl.setUrlname(urlname);
			lfNeturl.setSrcurl(srcurl);
			lfNeturl.setUrlmsg(urlmsg);
			//创建人
			lfNeturl.setCreateuid(Integer.valueOf(sysuser.getUserId().toString()));
			//创建时间
			lfNeturl.setCreatetm(new Timestamp(System.currentTimeMillis()));
			//默认最后一次修改时间
			lfNeturl.setUpdateuid(Integer.valueOf(sysuser.getUserId().toString()));
			lfNeturl.setUpdatetm(new Timestamp(System.currentTimeMillis()));
			//默认审核时间
			lfNeturl.setAudittm(new Timestamp(System.currentTimeMillis()));
			lfNeturl.setStoptm(new Timestamp(System.currentTimeMillis()));
			lfNeturl.setRemarks("");
			lfNeturl.setRemarks1("");
			lfNeturl.setRemarks2("");
			
			//根据是否企业版判断是否要运营商审批 //0：单企业；1：多企业
			if (StaticValue.getCORPTYPE() ==0) {
				//单企业无需审批
				lfNeturl.setIspass(1);
			}else {
				//多企业需要运营商审批   
				//设置为 未审批
				lfNeturl.setIspass(0);
			}
			//企业编码
			lfNeturl.setCorpcode(sysuser.getCorpCode());
			//创建人
			lfNeturl.setCreateuid(Integer.valueOf(sysuser.getUserId().toString()));
			//启用
			lfNeturl.setUrlstate(0);
			boolean result  = baseBiz.addObj(lfNeturl);
			if (result) {
				response.getWriter().print(true);
			}else {
				response.getWriter().print(false);
			}
		} catch (Exception e) {
			response.getWriter().print(false);
			EmpExecutionContext.error(e,"添加链接地址数据异常！");
		}
		
		
	}
	
	/**
	 * 删除
	 * @param request
	 * @param response
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response)throws IOException{
		try {
			//id
			String urlid = request.getParameter("ids");
			
			//审批状态 -1:删除   0：未审批, 1：无需审批 ,2：审批通过 ,3：审批未通过 
			String ispass = request.getParameter("ispass");
			//条件map
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("id&in", urlid);
			
			//更改属性map
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("ispass", ispass);
			
			boolean result=urlbiz.update(urlid, ispass);
			response.getWriter().print(result);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"删除短链地址异常（逻辑删除）！");
			response.getWriter().print("false");
		}
	}
	
	/**
	 * 修改url地址状态
	 * @param request
	 * @param response
	 */
	public void changeState(HttpServletRequest request, HttpServletResponse response)throws IOException{
		try {
			//获取登录sysuser
			LfSysuser sysuser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			if(sysuser==null){
				EmpExecutionContext.error("添加链接地址,session中获取当前登录对象出现异常");
				response.getWriter().print(false);
				return ;
			}
			
			//id
			String urlid = request.getParameter("urlid");
			
			//0.启用，-1.禁用   状态
			String urlState = request.getParameter("urlState");
			//条件map
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("id", urlid);
			
			//更改属性map
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("urlstate", urlState);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMap.put("updateuid", sysuser.getUserId().toString());
			objectMap.put("updatetm", sdf.format(new Date()));
			
			boolean result=baseBiz.update(LfNeturl.class, objectMap, conditionMap);
			
			//通知短地址中心禁用长地址
			//noticebiz.setUrlStop(urlid,sysuser, result);
			
			response.getWriter().print(result);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"修改短链地址状态异常！(客户侧禁用)");
			response.getWriter().print("false");
		}
	}
	
	/**
	 * 加载机构树
	 * @Title: createDeptTree
	 * @Description: TODO
	 * @param request
	 * @param response
	 * @throws Exception 
	 * @return void
	 */
	public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception{
		try
		{
			Long depId = null;
			Long userid=null;
			//部门iD
			String depStr = request.getParameter("depId");
			//操作员账号
			//String userStr = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userStr = SysuserUtil.strLguserid(request);

			if(depStr != null && !"".equals(depStr.trim())){
				depId = Long.parseLong(depStr);
			}
			if(userStr != null && !"".equals(userStr.trim())){
				userid = Long.parseLong(userStr);
			}
			//从session获取企业编码
			LfSysuser lfSysuser = getLoginUser(request);
			String corpCode = lfSysuser.getCorpCode();
			String departmentTree = urlbiz.getDepartmentJosnData2(depId, userid,corpCode);			
			response.getWriter().print(departmentTree);		
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"群发历史或群发任务查询条件中的机构树加载方法异常");
		}
	}
	
	
	public void getNetUrl(HttpServletRequest request, HttpServletResponse response)throws IOException{
		
		//查询结果模板volist
		List<LfNeturlVo> urlList = null;
		PageInfo pageInfo = new PageInfo();
		//机构树
		//String departmentTree = "";
		
		//条件map 
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			//初始化分页信息
			pageSet(pageInfo, request);
			//当前机构编码
			String lgcorpcode = request.getParameter("lgcorpcode");
			//连接名称
			String urlname = request.getParameter("urlname");
			//连接地址
			String srcurl = request.getParameter("srcurl");
			
			LfSysuser sysuser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
            //当前登录操作员id
			Long lguserid = sysuser.getUserId();
			lgcorpcode = sysuser.getCorpCode();
			
			if (!StringUtils.isBlank(urlname)) {
				conditionMap.put("urlname", urlname);
			}
			if (!StringUtils.isBlank(srcurl)) {
				conditionMap.put("srcurl", srcurl);
			}
			conditionMap.put("ispass", "2");
			conditionMap.put("urlstate", "0");
			conditionMap.put("lguserid", lguserid.toString());
			
			urlList = urlbiz.getNeturlVos(conditionMap, pageInfo);
			
			//加载机构树
			//departmentTree = busTypeBiz.getDepartmentJosnData(lguserid,lgcorpcode);
			//request.setAttribute("departmentTree", departmentTree);
			
			//查询创建人名map
			//Map<String, String> usersMap =  urlbiz.getAllUser(urlList);
			//request.setAttribute("usersMap", usersMap);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("urlList", urlList);
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("lgcorpcode",lgcorpcode);
			request.getRequestDispatcher(this.empRoot  + this.basePath+"/url_smsmanage.jsp")
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
				request.getRequestDispatcher(this.empRoot + this.basePath + "/url_smsmanage.jsp").forward(request, response);
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
}
