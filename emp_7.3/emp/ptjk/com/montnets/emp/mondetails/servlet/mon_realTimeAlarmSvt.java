package com.montnets.emp.mondetails.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.mondetails.biz.ErrMonBiz;
import com.montnets.emp.mondetails.biz.HisMonBiz;
import com.montnets.emp.monitor.constant.MonDhostParams;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.util.PageInfo;

@SuppressWarnings("serial")
public class mon_realTimeAlarmSvt extends BaseServlet
{

	final String empRoot="ptjk";
	final String base="/mondetails";
	/**
	 * 实时告警详情查询
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @datetime 2013-11-30 下午07:27:23
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		//查询开始时间
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		long stratTime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		try {
			//标示从哪个页面跳转过来的，便于返回 1.从监控跳转过来
			String herfType =  request.getParameter("herfType");
			String id = request.getParameter("id");
			BaseBiz baseBiz = new BaseBiz();
			List<DynaBean> monitorList = null;
			pageSet(pageInfo,request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			setConditionMap(request,conditionMap);
			if(id!=null&&!"".equals(id.trim())){
				conditionMap.put("id", id);
			}
			ErrMonBiz errMonBiz = new ErrMonBiz();
			monitorList = errMonBiz.getErrMon(pageInfo, conditionMap);
			
			//usermap key:username value name
			LinkedHashMap<String, String> userMap = new LinkedHashMap<String, String>();
			List<LfSysuser> userList = baseBiz.getEntityList(LfSysuser.class);
			LfSysuser user = new LfSysuser();
			if(userList!=null && userList.size()>0)
			{
				for(int i=0;i<userList.size();i++)
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
			int totalCount =  pageInfo.getTotalRec();
			//日志信息
			String opContent = "开始时间："+sdf.format(stratTime)+" 数量："+totalCount;
			
			EmpExecutionContext.info("实时告警详情", corpCode, userId, userName, opContent, "GET");
			request.getRequestDispatcher(this.empRoot+base+"/mon_realTimeMon.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"实时告警详情查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_realTimeMon.jsp")
				.forward(request, response);
			} catch (ServletException e1) {
				EmpExecutionContext.error(e1,"实时告警详情查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"实时告警详情查询异常！");
			}
		}
	}
	
	/**
	 * 历史告警详情查询(实时告警信息的历史告警信息)
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws UnsupportedEncodingException 
	 * @datetime 2013-11-30 下午07:27:23
	 */
	public void toHisMon(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//查询开始时间
		long stratTime = System.currentTimeMillis();
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PageInfo pageInfo = new PageInfo();
		try {
			List<DynaBean> monitorList = null;
			pageSet(pageInfo,request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//监控类型
			String apptype = request.getParameter("apptype");
			String monname = request.getParameter("monname");
			monname=monname!=null?( java.net.URLDecoder.decode(monname,"UTF-8")):monname;
			conditionMap.put("apptype", apptype);
			conditionMap.put("monname", monname);
			HisMonBiz hisMonBiz = new HisMonBiz();
			BaseBiz baseBiz = new BaseBiz();
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
			String lgCorpCode = null;
			//当前操作员ID
			String lgUserId = null;
			//当前操作员登录名
			String lgUserName = null;
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				if(loginSysuser!=null && !"".equals(loginSysuser.getCorpCode()))
				{
					lgCorpCode = loginSysuser.getCorpCode();
				}
				if(loginSysuser!=null && loginSysuser.getUserId() != null)
				{
					lgUserId = String.valueOf(loginSysuser.getUserId());
				}
				if(loginSysuser!=null && !loginSysuser.getUserName().equals("") && loginSysuser.getUserName()!= null)
				{
					lgUserName = loginSysuser.getUserName();
				}
			}
			//查询出的数据的总数量
			int totalCount =  pageInfo.getTotalRec();
			//日志信息
			String opContent = "开始时间："+sdf.format(stratTime)+" 数量："+totalCount;
			
			EmpExecutionContext.info("实时告警详情跳转历史告警详情", lgCorpCode, lgUserId, lgUserName, opContent, "GET");
			//标示从实时页面跳转过去
			request.setAttribute("type", "1");
			request.getRequestDispatcher(this.empRoot+base+"/mon_hisMon.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"历史告警详情查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("type", "1");
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_hisMon.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"历史告警详情查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"历史告警详情查询异常！");
			}
		}
	}
	
	/**
	 * 主机监控详情查询（从缓存中获取）
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @datetime 2013-11-30 下午07:27:23
	 */
	public void getInfo(HttpServletRequest request, HttpServletResponse response)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//查询开始时间
		long stratTime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		try {
			pageSet(pageInfo,request);
			String hostname = request.getParameter("hostname");
			String hostid = request.getParameter("hostid");
			String evttype = request.getParameter("evttype");
			String hoststatus = request.getParameter("hoststatus");
			List<MonDhostParams> hostList = new ArrayList<MonDhostParams>();
			List<MonDhostParams> pageList = new ArrayList<MonDhostParams>();
			// 程序监控详情信息
			Map<Long, MonDhostParams> hostMap = MonitorStaticValue.getHostMap();
			if(hostMap == null || hostMap.size() < 1)
			{
				hostMap = MonitorStaticValue.getHostMapTemp();
			}
			//遍历程序动态信息map
			Iterator<Long> keys = hostMap.keySet().iterator();
			while(keys.hasNext()){
				Long key = keys.next();
				MonDhostParams host = hostMap.get(key);
				//主机名称条件过滤
				if(host.getHostName()!=null &&hostname!=null &&!"".equals(hostname) && host.getHostName().indexOf(hostname)<0)
				{
					continue;
				}
				//主机编号条件过滤
				if(host.getHostid()!=null && hostid!=null &&!"".equals(hostid) && !String.valueOf(host.getHostid()).equals(hostid.toString()))
				{
					continue;
				}
				//告警级别条件过滤
				if(host.getEvtType()!=null && evttype!=null &&!"".equals(evttype) && !String.valueOf(host.getEvtType()).equals(evttype.toString()))
				{
					continue;
				}
				//在网状态条件过滤
				if(host.getHoststatus()!=null && hoststatus!=null &&!"".equals(hoststatus) && !String.valueOf(host.getHoststatus()).equals(hoststatus.toString()))
				{
					continue;
				}
				hostList.add(host);
			}
			//分页设置
			pageSetList(pageInfo,hostList.size());
			for(int i=(pageInfo.getPageIndex()-1)*pageInfo.getPageSize();i<pageInfo.getPageIndex()*pageInfo.getPageSize()&&i<=hostList.size();i++)
			{
				pageList.add(hostList.get(i));
			}
			
			request.setAttribute("pageList", pageList);
			request.setAttribute("pageInfo", pageInfo);
			
			//企业编码
			String lgCorpCode = null;
			//当前操作员ID
			String lgUserId = null;
			//当前操作员登录名
			String lgUserName = null;
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				if(loginSysuser!=null && !"".equals(loginSysuser.getCorpCode()))
				{
					lgCorpCode = loginSysuser.getCorpCode();
				}
				if(loginSysuser!=null && loginSysuser.getUserId() != null)
				{
					lgUserId = String.valueOf(loginSysuser.getUserId());
				}
				if(loginSysuser!=null && !loginSysuser.getUserName().equals("") && loginSysuser.getUserName()!= null)
				{
					lgUserName = loginSysuser.getUserName();
				}
			}
			//查询出的数据的总数量
			int totalCount =  pageInfo.getTotalRec();
			//日志信息
			String opContent = "开始时间："+sdf.format(stratTime)+" 数量："+totalCount;
			
			EmpExecutionContext.info("实时告警详情中的主机监控详情", lgCorpCode, lgUserId, lgUserName, opContent, "GET");
			
			request.getRequestDispatcher(this.empRoot+base+"/mon_hostMon.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"主机监控详情查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_hostMon.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"主机监控详情查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"主机监控详情查询异常！");
			}
		}
	}
	
	/**
	 *  * 设置分页信息
	 * @description    
	 * @param pageInfo
	 * @param totalCount       			 
	 * @author zhangmin
	 * @datetime 2014-1-8 上午11:49:09
	 */
	
	public void pageSetList(PageInfo pageInfo,int totalCount ) {
		//当前页数
		int pageSize = pageInfo.getPageSize();
		//总页数
		int totalPage = totalCount % pageSize == 0 ? totalCount
				/ pageSize : totalCount / pageSize + 1;
		pageInfo.setTotalRec(totalCount);
		pageInfo.setTotalPage(totalPage);
		//如果当前页数大于最大页数，则跳转到第一页
		if (pageInfo.getPageIndex() > totalPage)
		{
			pageInfo.setPageIndex(1);
		}
	}
	

	/**
	 * 设置查询条件
	 * @description    
	 * @param request
	 * @param conditionMap       			 
	 * @author zhangmin
	 * @throws UnsupportedEncodingException 
	 * @datetime 2013-11-18 
	 */
	public void setConditionMap(HttpServletRequest request,LinkedHashMap<String, String> conditionMap )
	{
		//事件内容
		String msg = request.getParameter("msg");
		//告警级别
		String evttype = request.getParameter("evttype");
		String recvtime = request.getParameter("recvtime");
		String sendtime = request.getParameter("sendtime");
		//监控类型
		String apptype = request.getParameter("apptype");
		//处理状态
		String dealflag = request.getParameter("dealflag");
		String monname = request.getParameter("monname");
		conditionMap.put("evttype", evttype);
		conditionMap.put("recvtime", recvtime);
		conditionMap.put("sendtime", sendtime);
		conditionMap.put("apptype", apptype);
		conditionMap.put("dealflag", dealflag);
		conditionMap.put("monname", monname);
		
	}
	
	/**
	 * 实时告警处理
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @datetime 2013-11-30 下午07:27:23
	 */
	public void dealMon(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String result ="error";
		try 
		{
			//告警信息id
			String ids = request.getParameter("ids");
			//描述
			String dealdesc = request.getParameter("dealdesc");
			//处理状态
			String dealflag = request.getParameter("dealflag");
			//用户id(处理人)
			String lgusername = request.getParameter("lgusername");
			boolean issuccess = new ErrMonBiz().dealMon(lgusername, ids, dealflag, dealdesc);
			result = issuccess?"sucess":"fail";
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String flag = issuccess?"成功":"失败";
				String field = "[id，描述，处理状态]";
				String opContent = "处理"+flag+"。"+field+"("+ids+"，"+dealdesc+"，"+dealflag+")";
				EmpExecutionContext.info("实时告警详情", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, "update");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"实时告警处理异常！");
		}
		finally
		{
			response.getWriter().write(result);
		}
	}
}
