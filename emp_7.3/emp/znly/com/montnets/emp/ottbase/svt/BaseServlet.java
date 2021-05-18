package com.montnets.emp.ottbase.svt;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.ottbase.constant.WXStaticValue;
import com.montnets.emp.util.PageInfo;

/**
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-9 下午04:29:58
 */

public class BaseServlet extends HttpServlet {

	private static final long serialVersionUID = -4906192663471241287L;

	protected static final String ERROR = "error";

	/**
	 * doget方法
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * dopost方法
	 */
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		request.setCharacterEncoding(WXStaticValue.ENCODING);
		response.setCharacterEncoding(WXStaticValue.ENCODING);
		// 请求路径
		String requestUrl = request.getServletPath()
				+ (request.getPathInfo() == null ? "" : request.getPathInfo());
		try {
			if (requestUrl.indexOf("_") > 0 && requestUrl.indexOf(".htm") > 0) {
				request.setAttribute("rTitle", requestUrl.substring(requestUrl
						.indexOf("_") + 1, requestUrl.indexOf(".htm")));
			}
		} catch (Exception e) {
		}
		String method = "find";
		try {
			if (request.getParameter("method") != null) {
				method = request.getParameter("method");
			} else {
				method = "find";
			}

			// 登录匹配验证
			/*String tkn = request.getParameter("tkn");
			int ISCLUSTER = StaticValue.ISCLUSTER;
			if (request.getSession(false) != null) {
				if (tkn != null && ISCLUSTER == 0) {

					// 根据token获取登录对象，存在时判断session，集群时不做判断
					if (StaticValue.loginInfoMap.get(tkn) != null) {
						// 去除loginInfoMap中的sessionid跟当前的sessionId比较
						if (!StaticValue.loginInfoMap.get(tkn).checkSessionId(
								request.getSession(false).getId())) {
							response.sendRedirect(request.getContextPath()
									+ "/quit");
							return;
						}
					}
					// 不存在则退出登录
					else {
						response.sendRedirect(request.getContextPath()
								+ "/quit");
						return;
					}
				}
			} else {
				// 获取不到session对象时，退出登录
				response.sendRedirect(request.getContextPath()
						+ "/quit");
				return;
			}*/

			Class<? extends BaseServlet> c = this.getClass();
			Class[] parameterTypes = { HttpServletRequest.class,
					HttpServletResponse.class };
			Method clazzMethod = c.getMethod(method, parameterTypes);
			Object[] Objparams = { request, response };
			clazzMethod.invoke(this, Objparams);
		} catch (SecurityException e) {
			EmpExecutionContext.error(e, "baseSvt方法转发异常。");
		} catch (NoSuchMethodException e) {
			EmpExecutionContext.error(e, "baseSvt方法转发，未找到对应方法异常。");
		} catch (IllegalArgumentException e) {
			EmpExecutionContext.error(e, "baseSvt方法转发异常。");
		} catch (IllegalAccessException e) {
			EmpExecutionContext.error(e, "baseSvt方法转发异常。");
		} catch (InvocationTargetException e) {
			EmpExecutionContext.error(e, "baseSvt方法转发异常。");
		}
	}

	/**
	 * 设置分页对象
	 * @param pageInfo 分页对象
	 * @param request 请求对象
	 * @return 是否第一次登录 
	 */
	public boolean pageSet(PageInfo pageInfo ,HttpServletRequest request ) {
		boolean isFirstEnter;
		String pageIndex = request.getParameter("pageIndex");
		String pageSize = request.getParameter("pageSize");
		//新增
		String totalPage = request.getParameter("totalPage");
		String totalRec = request.getParameter("totalRec");
		//-----------
		//分页信息类
		isFirstEnter = pageIndex== null && pageSize == null;
		if(pageIndex != null && checkNumber(pageIndex))
		{
			pageInfo.setPageIndex(Integer.valueOf(pageIndex));
		}
		if(pageSize != null && checkNumber(pageSize))
		{
			pageInfo.setPageSize(Integer.valueOf(pageSize));
		}
		//新增
		if(totalPage != null && checkNumber(totalPage))
		{
			pageInfo.setTotalPage(Integer.valueOf(totalPage));
		}
		if(totalRec != null && checkNumber(totalRec))
		{
			pageInfo.setTotalRec(Integer.valueOf(totalRec));
		}
		//------------
		return isFirstEnter;
	}
	
	/**
	 * 验证数字
	 * @param str 传入字符串
	 * @return
	 */
	private boolean checkNumber(String str)
	{
		for (int k = str.length(); --k >= 0;)
		{
			if (!Character.isDigit(str.charAt(k)))
			{
				return false;
			}
		}
		return true;
	}
}
