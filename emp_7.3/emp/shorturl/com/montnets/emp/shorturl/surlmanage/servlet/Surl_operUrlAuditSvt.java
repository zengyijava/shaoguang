package com.montnets.emp.shorturl.surlmanage.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.shorturl.surlmanage.biz.UrlOperUrlAuditBiz;
import com.montnets.emp.shorturl.surlmanage.vo.LfOperNeturlVo;
import com.montnets.emp.util.PageInfo;

/**
 * 运营商审核 servlet
 * @author Administrator
 *
 */
public class Surl_operUrlAuditSvt extends BaseServlet{
	
	private static final long serialVersionUID = 1454545478787878L;

	private final String empRoot = "shorturl";
	
	private final String basePath = "/operUrlAudit";
	
	final UrlOperUrlAuditBiz urlBiz = new UrlOperUrlAuditBiz();
	
	/**
	 * 查看
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {
		//查询结果模板volist
		List<LfOperNeturlVo> urlList = null;
		PageInfo pageInfo = new PageInfo();
		LinkedHashMap< String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			//初始化分页信息
			pageSet(pageInfo, request);
			//获取当前操作用户
			LfSysuser sysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			//当前操作员id
			//当前机构id
			
			//条件
			//连接地址名称
			String urlname = request.getParameter("urlname");
			//连接地址
			String srcurl = request.getParameter("srcurl");
			//审核状态
			String ispass = request.getParameter("ispass");
			//企业编号
			String corpnum = request.getParameter("corpnum");
			//企业名称
			String cropname = request.getParameter("cropname");
			//创建开始时间
			String startTime = request.getParameter("startTime");
			//创建结束时间
			String recvtime = request.getParameter("recvtime");
			conditionMap.put("urlname", urlname);
			conditionMap.put("srcurl", srcurl);
			//设置 运营商审批状态 条件 审批状态 -1:删除   0：未审批, 1：无需审批 ,2：审批通过 ,3：审批未通过 ,-2已禁用
			conditionMap.put("ispass", ispass);
			conditionMap.put("corpnum", corpnum);
			conditionMap.put("cropname", cropname);
			conditionMap.put("startTime", startTime);
			conditionMap.put("recvtime", recvtime);
			
			//审核人map集合
			Map<String, String> usersMap = urlBiz.getAlluser();
			//连接地址名称list
			List<String> urlNameList = urlBiz.getUrllist();
			urlList = urlBiz.findUrllist(conditionMap,pageInfo);
			
			//审核人
			request.setAttribute("usersMap", usersMap);
			//连接地址名称下拉框
			request.setAttribute("urlNameList", urlNameList);
			
			//详细信息
			request.setAttribute("urlList", urlList);
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher(this.empRoot  + this.basePath+"/url_operUrlAudit.jsp").forward(request, response);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"运营商审核URL链接地址查询异常！");
			try {
				request.getRequestDispatcher(this.empRoot+this.basePath+"/url_operUrlAudit.jsp").forward(request, response);
			} catch (ServletException e1) {
				EmpExecutionContext.error(e1,"运营商审核URL连接地址查询后跳转异常！");
			} catch (IOException e1) {
				EmpExecutionContext.error(e1,"运营商审核URL链接地址查询后跳转IO异常！");
			}
		}
	}
	
	
	/**
	 * 提交审核结果
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws IOException
	 */
	public void verifyUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			LfSysuser sysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			String userid = sysuser.getUserId().toString();
			//id
			String  id = request.getParameter("id");
			//审核意见
		 	String remarks =request.getParameter("remarks");
			//审核是否通过 2：审批通过 ,3：审批未通过 
			String ispass = request.getParameter("ispass");
			boolean result=urlBiz.update(id, remarks,ispass,userid);
			response.getWriter().print(result);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"运营商提交审核意见异常！");
			response.getWriter().print("false");
		}
	}
	/**
	 * 禁用长连接
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws IOException
	 */
	public void stopUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			LfSysuser sysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
			String userid = sysuser.getUserId().toString();
			//id
			String  id = request.getParameter("id");
			//禁用意见
		 	String remarks1 =request.getParameter("remarks1");
			//禁用  -3|-2       -3
			String ispass = request.getParameter("ispass");
			boolean result=urlBiz.stopUrl(id, remarks1,ispass,userid);
			response.getWriter().print(result);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"运营商提交审核意见异常！");
			response.getWriter().print("false");
		}
	}
	
	
}
