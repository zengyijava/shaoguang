package com.montnets.emp.appmage.svt;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.appmage.biz.app_climanagerBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.appmage.LfAppMwGroup;
import com.montnets.emp.entity.sysuser.LfSysuser;


public class app_cligroupmanagerSvt  extends BaseServlet
{

	private static final long	serialVersionUID	= 1L;
	private static final String PATH = "/appmage/contact";
	private static final BaseBiz baseBiz = new BaseBiz();
	private static final app_climanagerBiz biz =new app_climanagerBiz();
	
	/**
	 * 查询用户群组
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) 
	{
        // 当前登录企业
        String lgcorpcode = request.getParameter("lgcorpcode");
        //String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		String groupid=request.getParameter("groupid");
       try {
    	   
    	   List<DynaBean>  listGroup=biz.querygroup();
    	request.setAttribute("notAtGroup", biz.queryNotAtGroup(lgcorpcode)+"");
		request.setAttribute("listGroup", listGroup);
		request.setAttribute("lguserid", lguserid);
		request.setAttribute("groupid", groupid);
		request.setAttribute("lgcorpcode", lgcorpcode);
		
		request.getRequestDispatcher(PATH + "/app_webexlist.jsp" ).forward(request, response);
	} catch (Exception e) {
		EmpExecutionContext.error(e, "APP用户群组页面加载出错！");
	}

		
	}
	/**
	 * 添加用户群组
	 * @param request
	 * @param response
	 */
	public void addWeblist(HttpServletRequest request, HttpServletResponse response) 
	{
		String name = request.getParameter("name");
		String corpcode = request.getParameter("lgcorpcode");
		//新增时候查询一下是否重复名称
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("name", name);
		try {
			List<LfAppMwGroup> list=baseBiz.getByCondition(LfAppMwGroup.class, conditionMap, null);
			if(list.size()>0){
				response.getWriter().print("repeat");
				return;
			}
		} catch (Exception e1) {
			EmpExecutionContext.error(e1, "通过群组名称查询异常！");
		}
		try {
			LfAppMwGroup group =new LfAppMwGroup();
			group.setCount("0");
			group.setCreatetime(new Timestamp(System.currentTimeMillis()));
			if(corpcode!=null&&!"".equals(corpcode)){
				group.setCorpcode(corpcode);
			}
			group.setName(name);
			 Long addTemp = baseBiz.addObjProReturnId(group);
	            if(addTemp != null && addTemp > 0)
	            {
	                // 新增成功
	    			response.getWriter().print("true");
	    			//增加操作日志
	    			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
	    			if(loginSysuserObj!=null){
	    				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
	    				String opContent = "新增群组成功， 群组名称：" + name;
	    				EmpExecutionContext.info("APP用户管理", loginSysuser.getCorpCode(),
	    						loginSysuser.getUserId().toString(), loginSysuser
	    								.getUserName(), opContent, "ADD");
	    			}
	            }
	            else
	            {
	            	//增加操作日志
	    			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
	    			if(loginSysuserObj!=null){
	    				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
	    				String opContent = "新增群组失败， 群组名称：" + name;
	    				EmpExecutionContext.info("APP用户管理", loginSysuser.getCorpCode(),
	    						loginSysuser.getUserId().toString(), loginSysuser
	    								.getUserName(), opContent, "ADD");
	    			}
	            	
	                // 新增失败
	                response.getWriter().print("false");
	            }
		} catch (Exception e) {
			EmpExecutionContext.error(e, "添加用户群组异常！");
		}
	}
	
	/**
	 * 修改用户群组
	 * @param request
	 * @param response
	 */
	public void updateDepName(HttpServletRequest request, HttpServletResponse response) 
	{
		String newName = request.getParameter("depName");
		String groupid = request.getParameter("groupid");
		 LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		 conditionMap.put("gid", groupid);
		 LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		 objectMap.put("name", newName);
		 
		 try {
			List<LfAppMwGroup>	list=baseBiz.findListByCondition(LfAppMwGroup.class, objectMap, null);
			if(list.size()>0){
				response.getWriter().print("reapt");
				return;
			}
		} catch (Exception e1) {
			EmpExecutionContext.error(e1, "按照名称查询群组异常！");
		}
		 
		try {
			//查询获得修改之前名字
			 LinkedHashMap<String, String> objectMap2 = new LinkedHashMap<String, String>();
			 objectMap2.put("gid", groupid);
			 List<LfAppMwGroup>	list2= baseBiz.findListByCondition(LfAppMwGroup.class, objectMap2, null);
			 String oldName= null;
			 if(list2.size()>0){
				oldName=list2.get(0).getName();
			 }
			 //修改群组名
			 boolean addTemp = baseBiz.update(LfAppMwGroup.class, objectMap, conditionMap);
	            if(addTemp)
	            {
	            	//增加操作日志
	    			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
	    			if(loginSysuserObj!=null){
	    				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
	    				String opContent = "修改群组名称成功。" +
	    						"[ 群组ID,群组名称](" + groupid+","+oldName+")-->("+ groupid+","+newName+")";
	    				EmpExecutionContext.info("APP用户管理", loginSysuser.getCorpCode(),
	    						loginSysuser.getUserId().toString(), loginSysuser
	    								.getUserName(), opContent, "ADD");
	    			}
	                // 修改用户群组成功
	    			response.getWriter().print("true");
	            }
	            else
	            {
	            	//增加操作日志
	    			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
	    			if(loginSysuserObj!=null){
	    				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
	    				String opContent = "修改群组名称失败。" +
	    						"[ 群组ID,群组名称](" + groupid+","+oldName+")-->("+ groupid+","+newName+")";
	    				EmpExecutionContext.info("APP用户管理", loginSysuser.getCorpCode(),
	    						loginSysuser.getUserId().toString(), loginSysuser
	    								.getUserName(), opContent, "ADD");
	    			}
	                // 修改用户群组失败
	                response.getWriter().print("false");
	            }
		} catch (Exception e) {
			EmpExecutionContext.error(e, "修改用户群组异常！");
		}
	
	}
	
	/**
	 * 删除用户群组
	 * @param request
	 * @param response
	 */
	public void delDep(HttpServletRequest request, HttpServletResponse response) 
	{
		String groupid = request.getParameter("groupid");
		 LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		 conditionMap.put("gid", groupid);
		try {

//			 int addTemp = baseBiz.deleteByCondition(LfAppOlGroup.class, conditionMap);
			//查询获得修改之前名字
			 LinkedHashMap<String, String> objectMap2 = new LinkedHashMap<String, String>();
			 objectMap2.put("gid", groupid);
			 List<LfAppMwGroup>	list2= baseBiz.findListByCondition(LfAppMwGroup.class, objectMap2, null);
			 String oldName= null;
			 if(list2.size()>0){
				oldName=list2.get(0).getName();
			 }
			 boolean addTemp = biz.delDep(conditionMap);
	            if(addTemp)
	            {
	            	//增加操作日志
	    			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
	    			if(loginSysuserObj!=null){
	    				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
	    				String opContent = "删除群组名称成功。" +
	    						"[ 群组ID,群组名称](" + groupid+","+oldName+")";
	    				EmpExecutionContext.info("APP用户管理", loginSysuser.getCorpCode(),
	    						loginSysuser.getUserId().toString(), loginSysuser
	    								.getUserName(), opContent, "DELETE");
	    			}
	                // 新增成功
	    			response.getWriter().print("true");
	            }
	            else
	            {
	            	//增加操作日志
	    			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
	    			if(loginSysuserObj!=null){
	    				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
	    				String opContent = "删除群组名称失败。" +
	    						"[ 群组ID,群组名称](" + groupid+","+oldName+")";
	    				EmpExecutionContext.info("APP用户管理", loginSysuser.getCorpCode(),
	    						loginSysuser.getUserId().toString(), loginSysuser
	    								.getUserName(), opContent, "DELETE");
	    			}

	                // 新增失败
	                response.getWriter().print("false");
	            }
		} catch (Exception e) {
			EmpExecutionContext.error(e, "删除用户群组异常！");
		}
	
	}
	
	/**
	 * 添加群组
	 * @param request
	 * @param response
	 */
	
	public void changeGroup(HttpServletRequest request, HttpServletResponse response) 
	{
		String selected = request.getParameter("selected");
		String value=request.getParameter("value");
		String lgcorpcode=request.getParameter("lgcorpcode");
		 LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		 conditionMap.put("lgcorpcode", lgcorpcode);
		 conditionMap.put("selected",selected);
		 LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		 objectMap.put("value", value);
		 
		 
		try {

			 boolean addTemp = biz.changeGroup(conditionMap, objectMap);
	            if(addTemp)
	            {
	            	//增加操作日志
	    			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
	    			if(loginSysuserObj!=null){
	    				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
	    				String opContent = "添加群组名称成功。" +
						"[ 群组ID,客户](" + selected+","+value+")";
	    				EmpExecutionContext.info("APP用户管理", loginSysuser.getCorpCode(),
	    						loginSysuser.getUserId().toString(), loginSysuser
	    								.getUserName(), opContent, "ADD");
	    			}
	                // 新增成功
	    			response.getWriter().print("true");
	            }
	            else
	            {
	            	//增加操作日志
	    			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
	    			if(loginSysuserObj!=null){
	    				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
	    				String opContent = "添加群组名称失败。" +
						"[ 群组ID,客户](" + selected+","+value+")";
	    				EmpExecutionContext.info("APP用户管理", loginSysuser.getCorpCode(),
	    						loginSysuser.getUserId().toString(), loginSysuser
	    								.getUserName(), opContent, "ADD");
	    			}

	                // 新增失败
	                response.getWriter().print("false");
	            }
		} catch (Exception e) {
			EmpExecutionContext.error(e, "变更用户群组异常！");
		}
	
	}
	
	/**
	 * 退出群组
	 * @param request
	 * @param response
	 */
	
	public void delLink(HttpServletRequest request, HttpServletResponse response) 
	{
		String guid = request.getParameter("guid");
		String gid=request.getParameter("gid");
		 LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		 conditionMap.put("guid", guid);
		 conditionMap.put("gid",gid);
		 
		 
		try {

			 boolean addTemp = biz.delLink(conditionMap);
	            if(addTemp)
	            {
	            	//增加操作日志
	    			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
	    			if(loginSysuserObj!=null){
	    				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
	    				String opContent = "删除群组名称成功。" +
						"[ 群组ID,客户ID](" + gid+","+guid+")";
				        EmpExecutionContext.info("APP用户管理", loginSysuser.getCorpCode(),
						loginSysuser.getUserId().toString(), loginSysuser
								.getUserName(), opContent, "DELETE");
	    		
	    			}
	                // 退出群组成功
	    			response.getWriter().print("true");
	            }
	            else
	            {
	            	//增加操作日志
	    			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
	    			if(loginSysuserObj!=null){
	    				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
	    				String opContent = "删除群组名称失败。" +
						"[ 群组ID,客户ID](" + gid+","+guid+")";
				        EmpExecutionContext.info("APP用户管理", loginSysuser.getCorpCode(),
						loginSysuser.getUserId().toString(), loginSysuser
								.getUserName(), opContent, "DELETE");
	    		
	    			}
	                // 退出群组失败
	                response.getWriter().print("false");
	            }
		} catch (Exception e) {
			EmpExecutionContext.error(e, "退出群组异常！");
		}
	
	}
	
	/****
	 * 批量退出群组
	 * @param request
	 * @param response
	 */
	public void delAllLink(HttpServletRequest request, HttpServletResponse response) 
	{
		String hiddenValue = request.getParameter("hiddenValue");
		String groupValue=request.getParameter("groupValue");
		 LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		 conditionMap.put("hiddenValue", hiddenValue);
		 conditionMap.put("groupValue",groupValue);
		 
		 
		try {

			 boolean addTemp = biz.delAllLink(conditionMap);
	            if(addTemp)
	            {
	            	//增加操作日志
	    			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
	    			if(loginSysuserObj!=null){
	    				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
	    				String opContent = "删除群组名称成功。" +
						"[ 群组ID,客户ID](" + groupValue+","+hiddenValue+")";
	    				EmpExecutionContext.info("APP用户管理", loginSysuser.getCorpCode(),
	    						loginSysuser.getUserId().toString(), loginSysuser
	    								.getUserName(), opContent, "DELETE");
	    			}
	                // 退出群组成功
	    			response.getWriter().print("true");
	            }
	            else
	            {
	            	//增加操作日志
	    			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
	    			if(loginSysuserObj!=null){
	    				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
	    				String opContent = "删除群组名称失败。" +
						"[ 群组ID,客户ID](" + groupValue+","+hiddenValue+")";
	    				EmpExecutionContext.info("APP用户管理", loginSysuser.getCorpCode(),
	    						loginSysuser.getUserId().toString(), loginSysuser
	    								.getUserName(), opContent, "DELETE");
	    			}

	                // 退出群组失败
	                response.getWriter().print("false");
	            }
		} catch (Exception e) {
			EmpExecutionContext.error(e, "退出群组异常！");
		}
	
	}
	
}
