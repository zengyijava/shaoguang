package com.montnets.emp.mondetails.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.monitor.LfMonSspacinfo;
import com.montnets.emp.entity.monitorsp.MmonUserinfo;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.monitor.constant.MonDspAccountParams;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-18 下午08:17:51
 */
@SuppressWarnings("serial")
public class mon_spAcctMonSvt extends BaseServlet {

	final String empRoot="ptjk";
	final String base="/mondetails";
	/**
	 * sp账号监控详情查询
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @datetime 2013-11-30 下午07:27:23
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		try {
			//标示从哪个页面跳转过来的，便于返回 1.从监控跳转过来
			String herfType =  (String)request.getParameter("herfType");
			request.getRequestDispatcher(this.empRoot+base+"/mon_spAcctIndex.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"sp账号监控详情查询异常！");
			request.setAttribute("findresult", "-1");
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_spAcctIndex.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"sp账号监控详情查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"sp账号监控详情查询异常！");
			}
		}
	}
	/**
	 * sp账号监控详情查询（从缓存中获取）
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
			String spaccountid = request.getParameter("spaccountid");
			String accountname = request.getParameter("accountname");
			//账号传入类型 
			String spaccountType = request.getParameter("spaccountType");
			String evttype = request.getParameter("evttype");
			//mt滞留
			String mtremained = request.getParameter("mtremained");
			//mo滞留
			String moremained = request.getParameter("moremained");
			//rpt滞留
			String rptremained = request.getParameter("rptremained");
			//账号类型
			String accounttype = request.getParameter("accounttype");
			//登录类型
			String logintype=request.getParameter("logintype");
			//账号状态
			String onlinestatus=request.getParameter("onlinestatus");
			List<MonDspAccountParams> hostList = new ArrayList<MonDspAccountParams>();
			List<MonDspAccountParams> pageList = new ArrayList<MonDspAccountParams>();
			// SP账号监控详情信息
			Map<String, MonDspAccountParams> spAccountMap = MonitorStaticValue.getSpAccountMap();
			if(spAccountMap == null || spAccountMap.size() < 1)
			{
				spAccountMap = MonitorStaticValue.getSpAccountMapTemp();
			}
			//  SP账号基础信息
			Map<String, LfMonSspacinfo> spAccountBaseMap = MonitorStaticValue.getSpAccountBaseMap();
			if(spAccountBaseMap == null || spAccountBaseMap.size() < 1)
			{
				spAccountBaseMap = MonitorStaticValue.getSpAccountBaseMapTemp();
			}
			//遍历程序动态信息map
			Iterator<String> keys = spAccountMap.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();
				MonDspAccountParams spacct = spAccountMap.get(key);
				LfMonSspacinfo baseSpacct = spAccountBaseMap.get(key);
				//过滤不监控的状态信息 状态不为1全部过滤
				if(!(baseSpacct!=null&&baseSpacct.getMonstatus()==1)){
					continue;
				}
				//主机名称条件过滤
				if(accountname!=null &&!"".equals(accountname))
				{
					if(spacct.getAccountName()==null||"".equals(spacct.getAccountName())){
						continue;
					}else if(spacct.getAccountName().toLowerCase().indexOf(accountname.toLowerCase())<0){
						continue;
					}
				}
				//主机编号条件过滤
				if(spaccountid!=null &&!"".equals(spaccountid))
				{
					//如果为空则不匹配 跳出
					if(spacct.getSpaccountid()==null||"".equals(spacct.getSpaccountid())){
						continue;
					}else if("1".equals(spaccountType)){//手动输入 模糊匹配
						if(spacct.getSpaccountid().toLowerCase().indexOf(spaccountid.toLowerCase())<0){
							continue;
						}
					}else{//完全匹配
						if(!spacct.getSpaccountid().equals(spaccountid)){
							continue;
						}
					}
				}
				//告警级别条件过滤
				if(evttype!=null &&!"".equals(evttype))
				{
					Integer type= spacct.getEvtType();
					if(type==null){type = -1;}
					if(evttype.equals("0")&&(type==1||type==2)){
						continue;
					}
					if(!evttype.equals("0")&&!evttype.equals(String.valueOf(type))){
						continue;
					}
				}
				//账号类型条件过滤
				if(accounttype!=null &&!"".equals(accounttype))
				{
					Integer type= spacct.getSpAccountType();
					if(type==null||(type!=1&&type!=2)){type = 3;}
					if(Integer.valueOf(accounttype)-type!=0){
						continue;
					}
				}
				//mt滞留条件过滤
				if(!isMatch(mtremained, spacct.getMtremained())){
					continue;
				}
				//mo滞留条件过滤
				if(!isMatch(moremained, spacct.getMoremained())){
					continue;
				}
				//rpt滞留条件过滤
				if(!isMatch(rptremained, spacct.getRptremained())){
					continue;
				}
				//登录类型条件过滤
				if(logintype!=null&&!"".equals(logintype))
				{
					Integer type=spacct.getLoginType();
					if(type==null)
					{
						type=0;
					}
					//不符号条件的过滤掉
					if(Integer.valueOf(logintype)-type!=0)
					{
						continue;
					}
					
				}
				//账号状态条件过滤
				if(onlinestatus!=null&&!"".equals(onlinestatus))
				{
					Integer status=spacct.getOnlinestatus();
					if(status==null)
					{
						status=-1;
					}
					//不符号条件的过滤掉
					if(Integer.valueOf(onlinestatus)-status!=0)
					{
						continue;
					}
					
				}
				hostList.add(spacct);
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
			
			EmpExecutionContext.info("sp账号监控详情", corpCode, userId, userName, opContent, "GET");
			
			request.getRequestDispatcher(this.empRoot+base+"/mon_spAcctMon.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"sp账号监控详情查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_spAcctMon.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"sp账号监控详情查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"sp账号监控详情查询异常！");
			}
		}
	}
	/**
	 * 获取sp账号
	 * @Title: getDes
	 * @Description: TODO
	 * @param  
	 * @return void    返回类型
	 * @throws
	 */
	public void getDes(HttpServletRequest request, HttpServletResponse response)
	{
		String json = "";
		PrintWriter out = null;
		try {
			String spaccount = request.getParameter("spaccount");
			if(spaccount==null||"".equals(spaccount.trim())){
				return;
			}
			response.setContentType("text/html");
			out = response.getWriter();
			MonDspAccountParams mon = new MonDspAccountParams();
			// 程序监控详情信息
			Map<String, MonDspAccountParams> spAccountMap = MonitorStaticValue.getSpAccountMap();
			if(spAccountMap == null || spAccountMap.size() < 1)
			{
				spAccountMap = MonitorStaticValue.getSpAccountMapTemp();
			}
			//遍历程序动态信息map
			Iterator<String> keys = spAccountMap.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();
				MonDspAccountParams spAcct = spAccountMap.get(key);
				//账号条件过滤
				if(spaccount.equals(spAcct.getSpaccountid()))
				{
					mon = spAcct;
					break;
				}
			}
			StringWriter writer = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            
            //在线状态
            String onlinestatusStr="未知";
			Integer onlinestatus=mon.getOnlinestatus();
			if(onlinestatus!=null&&onlinestatus==0)
			{
			    onlinestatusStr="在线";
			}else if(onlinestatus!=null&&onlinestatus==1)
			{
			    onlinestatusStr="离线";
			}else
			{
			    onlinestatusStr="未知";
			}
			mon.setOnlinestatusStr(onlinestatusStr);
			
			//提交状态
			String submitStatusStr="未知";
			//spid为空，则未知
			if(mon.getSpaccountid()==null||"".equals(mon.getSpaccountid().trim())){
			   submitStatusStr="未知";
			}else if(spAccountMap.get(mon.getSpaccountid())==null){
			   submitStatusStr="未知";
			}else{
				 //获取提交状态
				 Long submitStatus=spAccountMap.get(mon.getSpaccountid()).getMonThresholdFlag().get(13);
				 //提交状态为0，正常
				 if(submitStatus==0)
				 {
				    submitStatusStr="正常";
				 }
				 //提交状态为1，不正常
				 else if(submitStatus==1)
				 {
				 	//计算未提交数据时间
				 	long min=(Calendar.getInstance().getTime().getTime()- mon.getNoMtHaveSnd())/(1000*60);
				 	submitStatusStr=min+"(分钟)未提交";
				 }else{
				    submitStatusStr="未知";
				 }
			}
			mon.setSubmitStatusStr(submitStatusStr);
			
            mapper.writeValue(writer, mon);
            json=writer.toString();
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取SP账号详情异常！");
		}
		out.print(json);
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
	 * @datetime 2013-11-18 
	 */
	public void setConditionMap(HttpServletRequest request,LinkedHashMap<String, String> conditionMap )
	{
		// 告警级别
		String warnJb = "";
		if(request.getParameter("warnJb") != null){
			warnJb = request.getParameter("warnJb");
		}
		// 处理状态
		String dealStatus = "";
		if(request.getParameter("dealStatus") != null){
			dealStatus = request.getParameter("dealStatus");
		}
		// 账号ID
		String userId = "";
		if(request.getParameter("userId") != null){
			userId = request.getParameter("userId");
		}
		//告警级别
		int evtType = -1; 
		if(request.getParameter("evtType") != null){
			evtType = Integer.parseInt(request.getParameter("evtType"));
		}
		
		// 费用
		int userFee = 5;
		if (request.getParameter("userFee") != null) {
			userFee = Integer.parseInt(request.getParameter("userFee"));
		}
		// 在线状态
		short onlineStatus = -1;
		if (request.getParameter("onlineStatus") != null) {
			onlineStatus = Short.parseShort(request
					.getParameter("onlineStatus"));
		}
		// MT滞留
		String mtRemained = "";
		if(request.getParameter("mtRemained") != null){
			mtRemained = request.getParameter("mtRemained");
		}
		// MO滞留
		String moRemained = "";
		if(request.getParameter("moRemained") != null){
			moRemained = request.getParameter("moRemained");
		}
		// RPT滞留
		String rptRemained = "";
		if(request.getParameter("rptRemained") != null){
			rptRemained = request.getParameter("rptRemained");
		}
	}
	
	/**
	 * 获取通道账号详情
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @throws ServletException 
	 * @datetime 2013-11-18 
	 */
	public void getSpAccountDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String userId = request.getParameter("userId");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", userId);
			List<MmonUserinfo> monitorList = new BaseBiz().getByCondition(MmonUserinfo.class, conditionMap, null);
			
			if(monitorList!=null && monitorList.size()>0)
			{
				MmonUserinfo mmonUserinfo = monitorList.get(0);
				request.setAttribute("mmonUserinfo",mmonUserinfo);
			}
			else
			{
				EmpExecutionContext.error("查询通道账户:"+userId+"监控详细信息无记录异常！");
			}
			
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"查询通道账户监控详细信息异常！");
		}
		finally
		{
			request.getRequestDispatcher(this.empRoot+base+"/mon_oneSpAccount.jsp").forward(request,
					response);
		}
	}
	/**
	 * 下拉框获取通道账号数据
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-1-16 下午06:33:07
	 */
	public void getSpAccts(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter out = null;
		JSONArray array = new JSONArray();
		response.setContentType("text/html");
		try
		{
			out = response.getWriter();
			// SP账号动态信息
			Map<String, MonDspAccountParams> spAccountMap = MonitorStaticValue.getSpAccountMap();
			if(spAccountMap == null || spAccountMap.size() < 1)
			{
				spAccountMap = MonitorStaticValue.getSpAccountMapTemp();
			}
			// SP账号基础信息
			Map<String, LfMonSspacinfo> spAccountBaseMap = MonitorStaticValue.getSpAccountBaseMap();
			if(spAccountBaseMap == null || spAccountBaseMap.size() < 1)
			{
				spAccountBaseMap = MonitorStaticValue.getSpAccountBaseMapTemp();
			}
			//遍历程序动态信息map
			Iterator<String> keys = spAccountMap.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();
				MonDspAccountParams spAcct = spAccountMap.get(key);
				LfMonSspacinfo baseSpAcct = spAccountBaseMap.get(key);
				//过滤不监控的状态信息 状态不为1全部过滤
				if(!(baseSpAcct!=null&&baseSpAcct.getMonstatus()==1)){
					continue;
				}
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("spAcct", spAcct.getSpaccountid());
				array.add(jsonObject);
			}
		}
		catch (IOException e)
		{
			EmpExecutionContext.error(e,"监控详情获取SP账号失败！");
		}
		out.print(array.toString());
	}
	
	/**
	 * 判断 remained 是否在 mtremained 区间
	 * @description    
	 * @param mtremained 形如0-99
	 * @param remained
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-1-18 下午03:30:38
	 */
	public boolean isMatch(String mtremained,Integer remained){
		boolean isok = false;
		if(mtremained == null ||"".equals(mtremained.trim())){
			return true;
		}
		String[] strs = mtremained.split("-");
		String[] result = new String[2];
		int j=0;
		for(int i=0;i<strs.length;i++){
			if(!"".equals(strs[i])){
				result[j]=strs[i];
				j++;
			}
			if(j==2){
				break;
			}
		}
		if(j==0){
			isok = true;
		}else{
			if(remained == null){
				return false;
			}
			if(j==1){
				try
				{
					isok=remained>=Integer.parseInt(result[0]);
				}
				catch (Exception e) {
					EmpExecutionContext.error(e, "参数转换异常!");
					return false;
				}
			}else if(j==2){
				try
				{
					isok=(remained>=Integer.parseInt(result[0]))&&(remained<=Integer.parseInt(result[1]));
				}
				catch (Exception e) {
					EmpExecutionContext.error(e, "参数转换异常!");
					return false;
				}
			}
		}
		return isok;
	}
}
