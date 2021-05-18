package com.montnets.emp.mondetails.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.monitor.LfMonErr;
import com.montnets.emp.entity.monitor.LfMonShost;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.mondetails.biz.HostMonBiz;
import com.montnets.emp.monitor.constant.MonDhostParams;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.util.PageInfo;

/**
 * 主机监控
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-30 下午07:26:17
 */
@SuppressWarnings("serial")
public class mon_hostMonSvt extends BaseServlet
{

	final String empRoot="ptjk";
	final String base="/mondetails";
	/**
	 * 主机监控详情查询
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @datetime 2013-11-30 下午07:27:23
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		try {
			request.getRequestDispatcher(this.empRoot+base+"/mon_hostIndex.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"主机监控详情查询异常！");
			request.setAttribute("findresult", "-1");
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_hostIndex.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"主机监控详情查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"主机监控详情查询异常！");
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
			Map<Long, LfMonShost> hostBaseMap = MonitorStaticValue.getHostBaseMap();
			if(hostBaseMap == null || hostBaseMap.size() < 1)
			{
				hostBaseMap = MonitorStaticValue.getHostBaseMapTemp();
			}
			while(keys.hasNext()){
				Long key = keys.next();
				MonDhostParams host = hostMap.get(key);
				LfMonShost shost = hostBaseMap.get(key);
				//过滤不监控数据
				if(shost!=null&&0==shost.getMonstatus())
				{
					continue;
				}
				//过滤删除数据
				if(shost==null || 1==shost.getHostusestatus())
				{
					continue;
				}
				//主机名称条件过滤
				if(host.getHostName()!=null &&hostname!=null &&!"".equals(hostname) && host.getHostName().indexOf(hostname)<0)
				{
					continue;
				}
				//主机编号条件过滤
				if(host.getHostid()!=null && hostid!=null &&!"".equals(hostid) && !host.getHostid().toString().equals(hostid))
				{
					continue;
				}
				//告警级别条件过滤
				if(host.getEvtType()!=null && evttype!=null &&!"".equals(evttype) && !host.getEvtType().toString().equals(evttype))
				{
					continue;
				}
				//在网状态条件过滤
				if(host.getHoststatus()!=null && hoststatus!=null &&!"".equals(hoststatus) && !host.getHoststatus().toString().equals(hoststatus))
				{
					continue;
				}
				hostList.add(host);
			}
			//分页设置
			pageSetList(pageInfo,hostList.size());
			for(int i=(pageInfo.getPageIndex()-1)*pageInfo.getPageSize();i<pageInfo.getPageIndex()*pageInfo.getPageSize()&&i<hostList.size();i++)
			{
				pageList.add(hostList.get(i));
			}
			//分页
			
			request.setAttribute("pageList", pageList);
			request.setAttribute("pageInfo", pageInfo);
			
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
			
			EmpExecutionContext.info("主机监控详情", corpCode, userId, userName, opContent, "GET");
			
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
	 * 主机监控查询(查库)
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @datetime 2013-11-30 下午07:27:23
	 */
	public void findOld(HttpServletRequest request, HttpServletResponse response)
	{
		PageInfo pageInfo = new PageInfo();
		try {
			List<DynaBean> monitorList = null;
			pageSet(pageInfo,request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			setConditionMap(request,conditionMap);
			HostMonBiz hostMonBiz = new HostMonBiz();
			monitorList = hostMonBiz.getHostMon(pageInfo, conditionMap);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("monitorList", monitorList);
			request.getRequestDispatcher(this.empRoot+base+"/mon_hostMon.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"主机监控管理查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_hostMon.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"主机监控管理查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"主机监控管理查询异常！");
			}
		}
	}

	/**
	 * 设置查询条件
	 * @description    
	 * @param request
	 * @param conditionMap       			 
	 * @author zhangmin
	 * @datetime 2013-11-18 
	 */
	public void setConditionMap(HttpServletRequest request,LinkedHashMap<String, String> conditionMap )
	{
		//主机编号
		String hostcode = request.getParameter("hostcode");
		//主机名称
		String hostname = request.getParameter("hostname");
		//内网ip1
		String adapter1 = request.getParameter("adapter1");
		//告警级别
		String evttype = request.getParameter("evttype");
		//在网状态
		String hoststatus = request.getParameter("hoststatus");
		conditionMap.put("hostcode", hostcode);
		conditionMap.put("hostname", hostname);
		conditionMap.put("adapter1", adapter1);
		conditionMap.put("evttype", evttype);
		conditionMap.put("hoststatus", hoststatus);
		
	}
}
