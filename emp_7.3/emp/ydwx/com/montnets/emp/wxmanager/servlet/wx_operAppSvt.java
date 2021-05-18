package com.montnets.emp.wxmanager.servlet;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.netnews.common.AllUtil;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wxmanger.dao.WxManagerDao;

public class wx_operAppSvt extends BaseServlet{
	/**
	 * 查询方法
	 * @param request
	 * @param response
	 */
	
	private final WxManagerDao managerDao = new WxManagerDao();
	private final BaseBiz baseBiz = new BaseBiz();
	
	/**
	 *  跳转到列表页面
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) 
	{
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		String corp=AllUtil.toStringValue(request.getParameter("corp"),"");
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter;
		try {
			request.getSession(false).setAttribute("topMonu","12");
			LfSysuser curUser = baseBiz.getById(LfSysuser.class, lguserid);
			isFirstEnter = pageSet(pageInfo, request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			if(!isFirstEnter)
			{	
				//获得页面参数
				String wxid=request.getParameter("wxid");
				String wxname=request.getParameter("wxname");
				String temptype =request.getParameter("temptype");
				String zhuangtai=request.getParameter("operState");
				String startdate=request.getParameter("startdate");
				String enddate=request.getParameter("enddate");
				String corpname=request.getParameter("corpname");
				
				if(wxid!=null && !"".equals(wxid.trim()))
				{
					conditionMap.put("wxid", wxid.trim());
					request.setAttribute("wxid",wxid);
				}
				if(corpname!=null && !"".equals(corpname.trim()))
				{
					conditionMap.put("corpname", corpname.trim());
					request.setAttribute("corpname",corpname);
				}
				
				if(wxname!=null && !"".equals(wxname.trim()))
				{
					conditionMap.put("wxname", wxname.trim());
					request.setAttribute("wxname",wxname);
				}
				if(temptype!=null && !"".equals(temptype.trim()))
				{
					conditionMap.put("temptype", temptype.trim());
					request.setAttribute("temptype",temptype);
				}
				if(zhuangtai!=null && !"".equals(zhuangtai))
				{
					conditionMap.put("operState", zhuangtai);
					request.setAttribute("operState",zhuangtai);
				}
				if(startdate!=null && !"".equals(startdate))
				{
					request.setAttribute("startdate",startdate);
					conditionMap.put("startdate", startdate+" 00:00:00");
				}
				if(enddate!=null && !"".equals(enddate))
				{
					request.setAttribute("enddate",enddate);
					conditionMap.put("enddate", enddate+" 23:59:59");
				}

				
			}else{
				//默认查询出未审核
				request.setAttribute("operState","0");
				conditionMap.put("operState","0");
			}
			if("100000".equals(curUser.getCorpCode())){
				lguserid=null;
			}
			List<DynaBean> beans = managerDao.getBaseInfos(conditionMap, pageInfo, lguserid);
			request.setAttribute("pagebaseinfo",beans);
			request.setAttribute("pageInfo",pageInfo);
			request.setAttribute("userId", curUser.getUserId().toString());
			request.setAttribute("corp", corp);
			request.getRequestDispatcher("ydwx/manageWX/wx_operApp.jsp").forward(request, response);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询网讯信息异常!");
		}
	}
	
	/**
	 * 网讯运营商审批
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	public void updateState(HttpServletRequest request, HttpServletResponse response) {
		//页面上获得的参数
		String id=request.getParameter("frId");
		String state=request.getParameter("state");
		String content=request.getParameter("cont");
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("operAppStatus", state);
			objectMap.put("operAppNote", content);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("ID", id);
			try
			{
				boolean b = baseBiz.update(LfWXBASEINFO.class, objectMap, conditionMap);
				if(b){
					//增加操作日志
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser sysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("网讯审核", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "网讯运营商审批成功。[模板id]（"+id+"）", "OTHER");
					}
					response.getWriter().print("true");
				}else{
					//增加操作日志
					Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser sysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("网讯审核", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "网讯运营商审批失败。[模板id]（"+id+"）", "OTHER");
					}
					response.getWriter().print("false");
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "审核网讯信息异常!");
			}
		

	}
}
