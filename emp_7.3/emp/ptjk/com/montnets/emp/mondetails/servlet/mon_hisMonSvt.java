package com.montnets.emp.mondetails.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.gateway.AgwParamConf;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.mondetails.biz.ErrMonBiz;
import com.montnets.emp.mondetails.biz.HisMonBiz;
import com.montnets.emp.util.PageInfo;

/**
 * 告警历史svt
 * 
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-30 下午07:26:17
 */
@SuppressWarnings("serial")
public class mon_hisMonSvt extends BaseServlet
{

	final String	empRoot	= "ptjk";

	final String	base	= "/mondetails";

	/**
	 * 历史告警详情查询
	 * 
	 * @description
	 * @param request
	 * @param response
	 * @author zhangmin
	 * @datetime 2013-11-30 下午07:27:23
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//查询开始时间
		long stratTime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		try
		{
			BaseBiz baseBiz = new BaseBiz();
			List<DynaBean> monitorList = null;
			pageSet(pageInfo, request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			setConditionMap(request, conditionMap);
			HisMonBiz hisMonBiz = new HisMonBiz();
			monitorList = hisMonBiz.getErrMon(pageInfo, conditionMap);

			// usermap key:username value name
			LinkedHashMap<String, String> userMap = new LinkedHashMap<String, String>();
			List<LfSysuser> userList = baseBiz.getEntityList(LfSysuser.class);
			LfSysuser user = new LfSysuser();
			if(userList != null && userList.size() > 0)
			{
				for (int i = 0; i < userList.size(); i++)
				{

					user = userList.get(i);
					userMap.put(user.getUserName(), user.getName());
				}

			}
			request.setAttribute("userMap", userMap);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("monitorList", monitorList);
		
			//企业编码
			String corpCode = null;
			//当前操作员ID
			String userId = null;
			//当前操作员登录名
			String userName = null;
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				if(loginSysuser!=null && !"".equals(loginSysuser.getCorpCode()))
				{
					corpCode = loginSysuser.getCorpCode();
				}
				if(loginSysuser!=null && loginSysuser.getUserId() != null)
				{
					userId = String.valueOf(loginSysuser.getUserId());
				}
				if(loginSysuser!=null && !loginSysuser.getUserName().equals("") && loginSysuser.getUserName()!= null)
				{
					userName = loginSysuser.getUserName();
				}
			}

			//查询出的数据的总数量
			int totalCount = pageInfo.getTotalRec();
			//日志信息
			String opContent = "开始时间："+sdf.format(stratTime) + "耗时：" +
			 (System.currentTimeMillis() - stratTime) + "ms  数量："+totalCount;
			
			EmpExecutionContext.info("历史告警详情", corpCode, userId, userName, opContent, "GET");
			
			
			request.getRequestDispatcher(this.empRoot + base + "/mon_hisMon.jsp").forward(request, response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "历史告警详情查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try
			{
				request.getRequestDispatcher(this.empRoot + base + "/mon_hisMon.jsp").forward(request, response);
			}
			catch (ServletException e1)
			{
				EmpExecutionContext.error(e1, "历史告警详情查询异常！");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1, "历史告警详情查询异常！");
			}
		}
	}

	/**
	 * 设置查询条件
	 * 
	 * @description
	 * @param request
	 * @param conditionMap
	 * @author zhangmin
	 * @datetime 2013-11-18
	 */
	public void setConditionMap(HttpServletRequest request, LinkedHashMap<String, String> conditionMap)
	{
		// 事件内容
		String msg = request.getParameter("msg");
		// 告警级别
		String evttype = request.getParameter("evttype");
		String recvtime = request.getParameter("recvtime");
		String sendtime = request.getParameter("sendtime");
		// 监控类型
		String apptype = request.getParameter("apptype");
		// 处理状态
		String dealflag = request.getParameter("dealflag");
		// 处理这
		String dealpeople = request.getParameter("dealpeople");
		String monname = request.getParameter("monname");
		conditionMap.put("evttype", evttype);
		conditionMap.put("recvtime", recvtime);
		conditionMap.put("sendtime", sendtime);
		conditionMap.put("apptype", apptype);
		conditionMap.put("dealflag", dealflag);
		conditionMap.put("dealpeople", dealpeople);
		conditionMap.put("monname", monname);


	}
}
