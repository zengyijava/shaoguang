package com.montnets.emp.common.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.ViewParams;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;

/**
 * 查询运营商余额
 * @author 林志焊
 *
 */
@SuppressWarnings("serial")
public class CheckFeeServlet extends BaseServlet{

	private final String empRoot = "";
	
	/**
	 * find方法，frame3.0框架调用的获取运营商余额的方法
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
		
	{
		try
		{
		//获取当前登录的操作员
		BaseBiz baseBiz = new BaseBiz();
		//String lguserid=request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


			LfSysuser sysuser=baseBiz.getById(LfSysuser.class, Long.parseLong(lguserid));
		
		//企业编码
		String corpCode = sysuser.getCorpCode();
		BalanceLogBiz balanceBiz = new BalanceLogBiz();
		if(!balanceBiz.IsChargings(sysuser.getUserId()))
		{
			request.setAttribute("nodepfee", "1");
		}
		//是否需要重载
		if("1".equals(request.getParameter("isreload")) )
		{
			request.setAttribute("feeList", balanceBiz.getPtFee(corpCode));
		}else
		{
			request.setAttribute("feeList", balanceBiz.getSpFeeByCorpCode(corpCode));
		}
		request.getRequestDispatcher("/frame/"+request.getSession(false).getAttribute(StaticValue.EMP_WEB_FRAME)+"/spaccFee.jsp")
			.forward(request, response);
		}catch (Exception e) {
			EmpExecutionContext.error(e, "查询运营商余额失败！");
			try{
				request.getRequestDispatcher("/frame/"+request.getSession(false).getAttribute(StaticValue.EMP_WEB_FRAME)+"/spaccFee.jsp");
			}catch (Exception ex) {
				EmpExecutionContext.error(ex, "查询运营商余额跳转页面失败！");
			}
		}
	}

	/**
	 * frame2.5框架调用的获取运营商余额的方法
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findSpfee(HttpServletRequest request, HttpServletResponse response)
		
	{
		try
		{
		BaseBiz baseBiz = new BaseBiz();
		Long maxcount = 0l;
		Long maxMmsConunt = 0l;
		//String lguserid=request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


			LfSysuser sysuser=baseBiz.getById(LfSysuser.class, Long.parseLong(lguserid));
		
		BalanceLogBiz balanceBiz = new BalanceLogBiz();
		//判断是否开启机构计费
		if(!balanceBiz.IsChargings(sysuser.getUserId()))
		{
			request.setAttribute("nodepfee", "1");
		}else {
			//短信余额
			maxcount=balanceBiz.getAllowSmsAmount(sysuser);
			//彩信余额
			maxMmsConunt=balanceBiz.getAllowMmsAmount(sysuser);
			//获取当前操作员所在机构
			LfDep dep = baseBiz.getById(LfDep.class, sysuser.getDepId());
			//机构名称传到前台
			request.setAttribute("depName", dep.getDepName());
		}
		request.setAttribute("maxcount",maxcount);
		request.setAttribute("maxMmsConunt",maxMmsConunt);
		
		//企业编码
		String corpCode = sysuser.getCorpCode();
		Map<String, String> btnMap = (Map<String, String>) request.getSession(false).getAttribute("btnMap");
		//是否显示运营商余额的权限
		String isShowYe = btnMap.get(ViewParams.YECODE+"-0") == null ? "1" : "0";
		request.setAttribute("isShowYe", isShowYe);
		
		//具有显示运营商余额权限且需要重载运营商余额时，调用重载方法
		if("1".equals(request.getParameter("isreload")) && "0".equals(isShowYe))
		{
			
			request.setAttribute("feeList", balanceBiz.getPtFee(corpCode));
		}else
		{
			request.setAttribute("feeList", balanceBiz.getSpFeeByCorpCode(corpCode));
		}
		request.getRequestDispatcher("/frame/"+request.getSession(false).getAttribute(StaticValue.EMP_WEB_FRAME)+"/spaccFee.jsp")
			.forward(request, response);
		}catch (Exception e) {
			EmpExecutionContext.error(e, "查询运营商余额失败！");
			try
			{
				request.getRequestDispatcher("/frame/"+request.getSession(false).getAttribute(StaticValue.EMP_WEB_FRAME)+"/spaccFee.jsp")
				.forward(request, response);
			}
			catch (Exception ex)
			{
				EmpExecutionContext.error(ex, "查询运营商余额跳转页面失败！");
			}
		}
	}
}
