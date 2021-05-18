package com.montnets.emp.client.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.client.biz.AddrBookBiz;
import com.montnets.emp.client.biz.ClientAddrBookBiz;
import com.montnets.emp.client.biz.EnterpriseBiz;
import com.montnets.emp.client.vo.LfEnterpriseVo;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;

import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.birthwish.LfBirthdayMember;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.client.LfClientDepSp;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.SuperOpLog;


@SuppressWarnings("serial")
public class cli_addrbookDepTreeSvt extends BaseServlet
{
	static EnterpriseBiz enterBiz=new EnterpriseBiz();
	static ClientAddrBookBiz clientBiz = new ClientAddrBookBiz();

	/**
	 * Constructor of the object.
	 */
	public cli_addrbookDepTreeSvt()
	{ 
		super();
	}

	/***
	 * 查询客户记录
	* @Description: TODO
	* @param @param request
	* @param @param response
	* @return void
	 */
	public void isHandAdd(HttpServletRequest request, HttpServletResponse response)
	{
		String dType = request.getParameter("m");
		try
		{
			if ("getEmpSecondDepJson".equals(dType))
			{
				boolean r = clientBiz.isEmployeeDepCustomAdd();
				if (r)
				{
					response.getWriter().print("true");
				}
			}
			else if("getClientDepTreeJson".equals(dType))
			{
				boolean r = clientBiz.isClientDepCustomAdd();
				if (r)
				{
					response.getWriter().print("true");
				}
			}
				
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询客户记录出现异常！");
		}
	}	
	
	/**
	 * 获取客户机构树
	 * @param request
	 * @param response
	 */
	public void getEntDepTreeJson(HttpServletRequest request, HttpServletResponse response)
	{
		String treeJson = "";
		try
		{
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			List<LfEnterpriseVo> enterprisesList = enterBiz.getEnterpriseVos(1, lfSysuser.getUserId(),lfSysuser.getCorpCode());
			treeJson = this.getAddrBookJson(enterprisesList);
			response.getWriter().print(treeJson);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取客户机构树出现异常！");
		}
	}
	
	/***
	 * 获取客户机构树
	* @Description: TODO
	* @param @param request
	* @param @param response
	* @return void
	 */
	public void getClientDepTreeJson(HttpServletRequest request, HttpServletResponse response)
	{
		EnterpriseBiz enterBiz=new EnterpriseBiz();
		String treeJson = "";
		try
		{
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			List<LfEnterpriseVo> enterprisesList = enterBiz.getEnterpriseVos(0, lfSysuser.getUserId(),lfSysuser.getCorpCode());
			treeJson = this.getAddrBookJson(enterprisesList);
			response.getWriter().print(treeJson);
 		} catch (Exception e)
		{
 			EmpExecutionContext.error(e,"获取客户机构树出现异常！");
		}
	}
	
	/***
	 * 获取操作员机构树
	* @Description: TODO 获取操作员机构树
	* @param @param request
	* @param @param response
	* @return void
	 */
	public void getCustromDepTree(HttpServletRequest request, HttpServletResponse response){
		AddrBookBiz addrBookBiz = new AddrBookBiz();
		String treeJson = "";
		try
		{
			LfSysuser lfSysuser = getLoginUser(request);
			List<LfDep> domDepsList = addrBookBiz.getAllDeps(lfSysuser==null?0l:lfSysuser.getUserId(),lfSysuser==null?"":lfSysuser.getCorpCode());
			//List<LfSysuser> usersList = addrBookBiz.getAllUserByCurUserId(lfSysuser.getUserId());
			List<LfSysuser> usersList = addrBookBiz.getAllUserByCurUserId(lfSysuser==null?0l:lfSysuser.getUserId());
			treeJson = this.getDepAndSysuserJosn(domDepsList, usersList);

			response.getWriter().print(treeJson);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取客户机构树出现异常！");
		}
	}
	
	/****
	 * 获取客户机构树
	* @Description: TODO 获取客户机构树
	* @param @param request
	* @param @param response
	* @return void
	 */
	public void getCustData(HttpServletRequest request, HttpServletResponse response){
		AddrBookBiz addrBookBiz = new AddrBookBiz();
		List<LfSysuser> lfSysusers = null;
		StringBuffer tree = null;
		try {
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			tree = new StringBuffer("[");
			BaseBiz baseBiz = new BaseBiz();
			LfDep dep = baseBiz.getById(LfDep.class, sysuser.getDepId());
			if(dep != null){
				tree.append("{");
				tree.append("id:").append(dep.getDepId());
				tree.append(",name:'").append(dep.getDepName()).append("'");
				tree.append(",pId:").append(dep.getSuperiorId());
				tree.append(",isParent:").append(true);
				tree.append("}");
			}
		
			
			lfSysusers = addrBookBiz.getAllSysusers(sysuser.getUserId());
			LfSysuser lfSysuser = null;
			if(!lfSysusers.isEmpty()){
				tree.append(",");
			}
	
			LfSysuser nowlfSysuser =(LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			for (int i = 0; i < lfSysusers.size(); i++) 
			{
				lfSysuser = lfSysusers.get(i);
				if(nowlfSysuser.getUserName().equals(lfSysuser.getUserName()))
				{
					tree.append("{");
					tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
					tree.append(",name:'").append(lfSysuser.getName()).append("'");
					tree.append(",pId:").append(lfSysuser.getDepId());
					tree.append(",isParent:").append(false);
					tree.append("}");
				}
			}
			tree.append("]");
			response.getWriter().print(tree);
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"获取客户机构树出现异常！");
		}
	}
	
	/**
	 * 获得第三方机构树信息
	* @Description: TODO
	* @param @param enterprisesList
	* @param @return
	* @param @throws Exception
	* @return String
	 */
	private String getAddrBookJson(List<LfEnterpriseVo> enterprisesList) throws Exception {
		if(enterprisesList == null || enterprisesList.size() == 0){
			return null;
		}
		LfEnterpriseVo enterpriseVo;
		StringBuffer tree = new StringBuffer("[");
		for (int i = 0; i < enterprisesList.size(); i++) 
		{
			enterpriseVo = enterprisesList.get(i);
			tree.append("{");
			tree.append("id:'").append(enterpriseVo.getDepCode()+"'");
			tree.append(",name:'").append(enterpriseVo.getDepName()).append("'");
			
			if(enterpriseVo.getDepCode().equals(enterpriseVo.getParentCode())){
				tree.append(",pId:'").append(0+"'");
			}else{
				tree.append(",pId:'").append(enterpriseVo.getParentCode()+"'");
			}
			tree.append(",dlevel:").append(enterpriseVo.getDepLevel());
			tree.append(",depCode:'").append(enterpriseVo.getDepCode()+"'");
			tree.append(",isParent:").append(true);
			tree.append("}");
			if(i != enterprisesList.size()-1){
				tree.append(",");
			}
		}
		tree.append("]");
		
		return tree.toString();
	}
	
	/****
	 * 获取操作员机构树
	* @Description: TODO
	* @param @param lfDeps
	* @param @param lfSysusers
	* @param @return
	* @return String
	 */
	private String getDepAndSysuserJosn(List<LfDep> lfDeps, List<LfSysuser> lfSysusers){

		StringBuffer tree = null;
		try {
			LfDep lfDep = null;
			tree = new StringBuffer("[");
			for (int i = 0; i < lfDeps.size(); i++) {
				lfDep = lfDeps.get(i);
				tree.append("{");
				tree.append("id:").append(lfDep.getDepId());
				tree.append(",name:'").append(lfDep.getDepName()).append("'");
				tree.append(",pId:").append(lfDep.getSuperiorId());
				tree.append(",depId:'").append(lfDep.getDepId()+"'");
				tree.append(",isParent:").append(true);
				tree.append("}");
				if(i != lfDeps.size()-1){
					tree.append(",");
				}
			}

			LfSysuser lfSysuser = null;
			if(!lfSysusers.isEmpty()){
				tree.append(",");
			}
			for (int i = 0; i < lfSysusers.size(); i++) {
				lfSysuser = lfSysusers.get(i);
				tree.append("{");
				tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
				tree.append(",name:'").append(lfSysuser.getName()).append("'");
				tree.append(",pId:").append(lfSysuser.getDepId());
				tree.append(",depId:'").append(lfSysuser.getUserId()+"'");
				tree.append(",isParent:").append(false);
				tree.append("}");
				if(i != lfSysusers.size()-1){
					tree.append(",");
				}
			}
			
			tree.append("]");
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取操作员机构树出现异常！");
		}
		return tree.toString();
	}
	
	
	
	
	/**
	 *   获取机构的 数，只 限查子级
	 * @param request
	 * @param response
	 */
	public void getEmpSecondDepJson(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			LfSysuser lfSysuser = getLoginUser(request);
			String depId = request.getParameter("depId");
			List<LfEmployeeDep> empDepList = enterBiz.getEmpSecondDepTreeByUserIdorDepId(lfSysuser==null?"":lfSysuser.getUserId().toString(),depId,lfSysuser==null?"":lfSysuser.getCorpCode());
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
			try
			{
				response.getWriter().print("");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1,"JSON传值到页面异常！");
			}
			EmpExecutionContext.error(e,"获取员工机构信息出现异常！");
		}
	}
	
	
	/**
	 *   获取客户机构的 数，只 限查子级
	 * @param request
	 * @param response
	 */
	public void getClientSecondDepJson(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			String depId = request.getParameter("depId");
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			LfSysuser lfSysuser = getLoginUser(request);
			String corpCode="";
			if(lfSysuser!=null)
			{
			 corpCode=lfSysuser.getCorpCode();
			}
			//此方法只查询两级机构
			List<LfClientDep> clientDepList = enterBiz.getCliSecondDepTreeByUserIdorDepId(lguserid, depId,corpCode);
			LfClientDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(clientDepList != null && clientDepList.size()>0){
				tree.append("[");
				for (int i = 0; i < clientDepList.size(); i++) {
					dep = clientDepList.get(i);
					tree.append("{");
					tree.append("id:").append(dep.getDepId()+"");
					tree.append(",name:'").append(dep.getDepName()).append("'");
					tree.append(",depcodethird:'").append(dep.getDepcodethird()).append("'");
					//树数据中加入父机构id
					if(dep.getParentId()-0==0){
						tree.append(",pId:").append(0);
					}else{
						tree.append(",pId:").append(dep.getParentId());
					}
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != clientDepList.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		} catch (Exception e)
		{
			try
			{
				response.getWriter().print("");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1,"JSON传值到页面异常！");
			}
			EmpExecutionContext.error(e,"获取客户机构信息出现异常！");
		}
	}
	
	/**
	 *   获取客户机构的 数(除未知机构)，只 限查子级
	 * @param request
	 * @param response
	 */
	public void getSecondDepNoUnknow(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			String depId = request.getParameter("depId");
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			LfSysuser lfSysuser = getLoginUser(request);
			String corpCode="";
			if(lfSysuser!=null)
			{
			 corpCode=lfSysuser.getCorpCode();
			}
			//此方法只查询两级机构
			List<LfClientDep> clientDepList = enterBiz.getCliSecondDepTreeByUserIdorDepId(lguserid, depId,corpCode);
			LfClientDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(clientDepList != null && clientDepList.size()>0){
				tree.append("[");
				for (int i = 0; i < clientDepList.size(); i++) {
					dep = clientDepList.get(i);
					//未知机构不显示
					if("-10".equals(dep.getDepId().toString()))
					{
						continue;
					}
					tree.append("{");
					tree.append("id:").append(dep.getDepId()+"");
					tree.append(",name:'").append(dep.getDepName()).append("'");
					tree.append(",depcodethird:'").append(dep.getDepcodethird()).append("'");
					//树数据中加入父机构id
					if(dep.getParentId()-0==0){
						tree.append(",pId:").append(0);
					}else{
						tree.append(",pId:").append(dep.getParentId());
					}
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != clientDepList.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		} catch (Exception e)
		{
			try
			{
				response.getWriter().print("");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1,"JSON传值到页面异常！");
			}
			EmpExecutionContext.error(e,"获取客户机构信息出现异常！");
		}
	}
	//修改客户资料时 树的结构有些是被默认选中的
	public void getClientSecondDepJsonedit(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			String depId = request.getParameter("depId");
			String cleid = request.getParameter("cleid");
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			LfSysuser lfSysuser = getLoginUser(request);
			String corpCode="";
			if(lfSysuser!=null)
			{
			 corpCode=lfSysuser.getCorpCode();
			}
			HashSet<String> depIdSet = new HashSet<String>();
			if(cleid != null && !"".equals(cleid)){
				LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
				conditionMap.put("clientId", cleid);
				List<LfClientDepSp> depSpList = new BaseBiz().getByCondition(LfClientDepSp.class, conditionMap, null);
				if(depSpList != null && depSpList.size()>0){
					for(LfClientDepSp temp:depSpList){
						depIdSet.add(String.valueOf(temp.getDepId()));
					}
				}
			}
			//此方法只查询两级机构
			List<LfClientDep> clientDepList = enterBiz.getCliSecondDepTreeByUserIdorDepId(lguserid, depId,corpCode);
			LfClientDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(clientDepList != null && clientDepList.size()>0){
				tree.append("[");
				for (int i = 0; i < clientDepList.size(); i++) {
					dep = clientDepList.get(i);
					//未知机构不显示
					if("-10".equals(dep.getDepId().toString()))
					{
						continue;
					}
					tree.append("{");
					tree.append("id:").append(dep.getDepId()+"");
					tree.append(",name:'").append(dep.getDepName()).append("'");
					if(depIdSet.contains(dep.getDepId().toString())){
						tree.append(",checked:").append(true);
					}else{
						tree.append(",checked:").append(false);
					}
					tree.append(",depcodethird:'").append(dep.getDepcodethird()).append("'");
					//树数据中加入父机构id
					if(dep.getParentId()-0==0){
						tree.append(",pId:").append(0);
					}else{
						tree.append(",pId:").append(dep.getParentId());
					}
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != clientDepList.size()-1){
						tree.append(",");
					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		} catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"获取客户机构信息出现异常！");
		}
	}
	private boolean isesxit(String ss,Long id){
		String[] aa = ss.split(",");
		for(String a:aa){
			if(a.equals(id.toString())){
				return true;
			}
		}
		return false;
	}
	//带颜色的树生日祝福选择员工通讯录用
	public void getEmpSecondDepJson1(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			String depId = request.getParameter("depId");
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			String lgcorpcode = request.getParameter("lgcorpcode");
			//此方法只查询两级机构
			List<LfEmployeeDep> empDepList = enterBiz.getEmpSecondDepTreeByUserIdorDepId(lguserid,depId,lgcorpcode);
			LfEmployeeDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(empDepList != null && empDepList.size()>0){
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				StringBuffer guids = new StringBuffer();
				List<LfBirthdayMember> memberList=null;
				for(LfEmployeeDep empDep:empDepList)
				{
					guids.append(empDep.getDepId()).append(",");
				}
				if(guids.length()>0)
				{
					conditionMap.put("corpCode", lgcorpcode);
					conditionMap.put("memberId&in", guids.deleteCharAt(guids.lastIndexOf(",")).toString());
					conditionMap.put("type", "1");
					conditionMap.put("membertype", "2");
					memberList = new BaseBiz().getByCondition(LfBirthdayMember.class, conditionMap, null);
				}
				List<Long> memberGuidList = new ArrayList<Long>();
				if(memberList!=null && memberList.size()>0)
				{
					for(LfBirthdayMember member : memberList)
					{
						memberGuidList.add(member.getMemberId());
					}
				}
				tree.append("[");
				if (empDepList != null && empDepList.size() > 0) {
					for (int i = 0; i < empDepList.size(); i++) {
						dep = empDepList.get(i);
						tree.append("{");
						tree.append("id:").append(dep.getDepId() + "");
						tree.append(",name:'").append(dep.getDepName()).append(
								"'");
						tree.append(",depcodethird:'").append(
								dep.getDepcodethird()).append("'");
						// 树数据中加入父机构id
						if (dep.getParentId() - 0 == 0) {
							tree.append(",pId:").append(0);
						} else {
							tree.append(",pId:").append(dep.getParentId());
						}
						tree.append(",isParent:").append(true);

						if (memberGuidList.contains(dep.getDepId())) {
							tree.append(",isBind:").append(true);
						} else {
							tree.append(",isBind:").append(false);
						}

						tree.append("}");
						if (i != empDepList.size() - 1) {
							tree.append(",");
						}

					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		} catch (Exception e)
		{
			try
			{
				response.getWriter().print("");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1,"JSON传值到页面异常！");
			}
			EmpExecutionContext.error(e,"获取员工机构信息出现异常！");
		}
	}
	//带颜色的树生日祝福选择客户通讯录用
	public void getClientSecondDepJson1(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			String depId = request.getParameter("depId");
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			String lgcorpcode = request.getParameter("lgcorpcode");
			//此方法只查询两级机构
			List<LfClientDep> clientDepList = enterBiz.getCliSecondDepTreeByUserIdorDepId(lguserid, depId,lgcorpcode);
			LfClientDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(clientDepList != null && clientDepList.size()>0){
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				StringBuffer guids = new StringBuffer();
				List<LfBirthdayMember> memberList=null;
				for(LfClientDep clientDep:clientDepList)
				{
					guids.append(clientDep.getDepId()).append(",");
				}
				if(guids.length()>0)
				{
					conditionMap.put("corpCode", lgcorpcode);
					conditionMap.put("memberId&in", guids.deleteCharAt(guids.lastIndexOf(",")).toString());
					conditionMap.put("membertype", "2");
					conditionMap.put("type", "2");
					memberList = new BaseBiz().getByCondition(LfBirthdayMember.class, conditionMap, null);
				}
				List<Long> memberGuidList = new ArrayList<Long>();
				if(memberList!=null && memberList.size()>0)
				{
					for(LfBirthdayMember member : memberList)
					{
						memberGuidList.add(member.getMemberId());
					}
				}
				tree.append("[");
				if (clientDepList != null && clientDepList.size() > 0) {
					for (int i = 0; i < clientDepList.size(); i++) {
						dep = clientDepList.get(i);

						tree.append("{");
						tree.append("id:").append(dep.getDepId() + "");
						tree.append(",name:'").append(dep.getDepName()).append(
								"'");
						tree.append(",depcodethird:'").append(
								dep.getDepcodethird()).append("'");
						// 树数据中加入父机构id
						if (dep.getParentId() - 0 == 0) {
							tree.append(",pId:").append(0);
						} else {
							tree.append(",pId:").append(dep.getParentId());
						}
						tree.append(",isParent:").append(true);
						
						if (memberGuidList.contains(dep.getDepId())) {
							tree.append(",isBind:").append(true);
						} else {
							tree.append(",isBind:").append(false);
						}
						
						tree.append("}");
						if (i != clientDepList.size() - 1) {
							tree.append(",");
						}
					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		} catch (Exception e)
		{
			try
			{
				response.getWriter().print("");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1,"JSON传值到页面异常！");
			}
			EmpExecutionContext.error(e,"获取客户机构信息出现异常！");
		}
	}
	/**
	 *   修改机构的名称 
	 * @param request
	 * @param response
	 */
	public void updateDepName(HttpServletRequest request, HttpServletResponse response)
	{
		LfSysuser lfSysuser = null;
		SuperOpLog spLog = new SuperOpLog();
		String corpCode="";
		String depId="";
		String depName ="";
		String isResult = "0";
		String updateBefore="";
		try
		{
			lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			BaseBiz baseBiz = new BaseBiz();
			depName = request.getParameter("depName");	//需要修改的名称
			depId = request.getParameter("depId");	//机构id
			String type = request.getParameter("type");			//点的是否是树的顶级机构  1不是   2是的
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			corpCode =lfSysuser.getCorpCode();			//企业编码
			conditionMap.put("depId", depId);
			conditionMap.put("corpCode", corpCode);
			List<LfClientDep> deps = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
			if(deps != null && deps.size()>0){
				LfClientDep dep = deps.get(0);
				 updateBefore="("+dep.getDepName()+")";
				//if("2".equals(type)){	//修改顶级机构名称
				//同级机构下是否存在相同的机构名
				conditionMap.clear();
				conditionMap.put("parentId", dep.getParentId().toString());
				conditionMap.put("corpCode", corpCode);
				conditionMap.put("depName", depName);
				List<LfClientDep> lfClientDeps = baseBiz.getByCondition(LfClientDep.class, conditionMap, null);
				if(lfClientDeps != null && lfClientDeps.size()>0){
					isResult = "3";		//同级存在相同的机构
				}
				//}
				if(!"3".equals(isResult)){
					dep.setDepName(depName);
					boolean returnFlag = baseBiz.updateObj(dep);
					if(returnFlag){
						isResult = "1";		//修改成功
						EmpExecutionContext.info("客户通讯录", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "修改成功。[部门名称，部门ID]（"+updateBefore+","+depId+"）->（"+depName+","+depId+"）", "UPDATE");
						spLog.logSuccessString(lfSysuser.getUserName(), StaticValue.ADDBR_MANAGER, StaticValue.UPDATE, "将客户机构(id："+depId+")改名为："+depName, corpCode);
					}else{
						EmpExecutionContext.info("客户通讯录", lfSysuser.getCorpCode(), lfSysuser.getUserId()+"", lfSysuser.getUserName(), "修改失败。[部门名称，部门ID]（"+updateBefore+","+depId+"）->（"+depName+","+depId+"）", "UPDATE");
						isResult = "2";		//修改失败
					}
				}
			}else{
				isResult = "4";	//数据库之类的出错 没有查询结果 
			}
			response.getWriter().print(isResult);
		}
		catch (Exception e)
		{
			if(lfSysuser == null){
				lfSysuser = new LfSysuser();
			}
			EmpExecutionContext.error(e,"模块名称：客户通讯录，企业："+lfSysuser.getCorpCode()+"，操作员："+lfSysuser.getUserId()+"["+lfSysuser.getUserName()+"]，修改失败。[部门名称，部门ID]（"+updateBefore+","+depId+"）->（"+depName+","+depId+"）");
			spLog.logFailureString(lfSysuser.getUserName(), StaticValue.ADDBR_MANAGER, StaticValue.UPDATE, "将客户机构(id："+depId+")改名为："+depName, e, corpCode);
			try
			{
				response.getWriter().print("");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1,"JSON传值到页面异常！");
			}
			EmpExecutionContext.error(e,"修改机构名称 出现异常！");
		}
	}

}
