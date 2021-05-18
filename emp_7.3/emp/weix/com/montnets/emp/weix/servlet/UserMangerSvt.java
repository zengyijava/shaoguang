package com.montnets.emp.weix.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.weix.LfWcAccount;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.weix.biz.UserMgrBiz;
import com.montnets.emp.weix.biz.WeixBiz;

/**
 * @author chensj
 */
public class UserMangerSvt extends BaseServlet
{
	// 资源路径
	private static final String		PATH		= "/weix/user";
	// 微信用户逻辑层
	private final UserMgrBiz userMgrBiz	= new UserMgrBiz();

	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 分页信息
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter = pageSet(pageInfo, request);
		try
		{
			// 企业编号
			String lgcorpcode = request.getParameter("lgcorpcode");
			
			// 查询所有的公众帐号
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lgcorpcode);
			List<LfWcAccount> lfWcAccList = new ArrayList<LfWcAccount>();
			lfWcAccList = new BaseBiz().getByCondition(LfWcAccount.class, conditionMap, null);
			
			if(!isFirstEnter)
			{
				// 微信编号
				String wcid = request.getParameter("wcid");
				// 手机号
				String phone = request.getParameter("phone");
				// 客户名称
				String uname = request.getParameter("uname");
				// 认证开始时间
				String beginTime = request.getParameter("beginTime");
				// 认证结束时间
				String endTime = request.getParameter("endTime");
				if(phone != null && !"".equals(phone.trim()))
				{
					conditionMap.put("phone", phone.trim());
				}
				if(uname != null && !"".equals(uname.trim()))
				{
					conditionMap.put("uname", uname.trim());
				}
				if(wcid != null && !"".equals(wcid.trim()))
				{
					conditionMap.put("wcid", wcid);
				}
				if(beginTime != null && !"".equals(beginTime.trim()))
				{
					conditionMap.put("beginTime", beginTime);
				}
				if(endTime != null && !"".equals(endTime.trim()))
				{
					conditionMap.put("endTime", endTime);
				}
			}

			List<DynaBean> beans = userMgrBiz.getUserInfoList(lgcorpcode, conditionMap, pageInfo);
			request.setAttribute("userlist", beans);
			request.setAttribute("acctList", lfWcAccList);
		}
		catch (ServletException e)
		{
			EmpExecutionContext.error(e, "用户关注列表Servlet异常！");
		}
		catch (IOException e)
		{
			EmpExecutionContext.error(e, "用户关注列表IO异常！");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取用户关注列表失败！");
		}finally{
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher(PATH + "/userManger.jsp").forward(request, response);
		}
	}
}
