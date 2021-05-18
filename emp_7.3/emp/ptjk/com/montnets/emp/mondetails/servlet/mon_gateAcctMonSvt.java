package com.montnets.emp.mondetails.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.monitor.LfMonSgtacinfo;
import com.montnets.emp.entity.monitorpas.MmonSpateinfo;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.monitor.constant.MonDgateacParams;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.util.PageInfo;

@SuppressWarnings("serial")
public class mon_gateAcctMonSvt extends BaseServlet
{


	final String empRoot="ptjk";
	final String base="/mondetails";
	/**
	 * 通道账号监控查询
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		try {
			//标示从哪个页面跳转过来的，便于返回 1.从监控跳转过来
			String herfType =  (String)request.getParameter("herfType");
			request.getRequestDispatcher(this.empRoot+base+"/mon_gateAcctIndex.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"通道账号监控详情查询异常！");
			request.setAttribute("findresult", "-1");
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_gateAcctIndex.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"通道账号监控详情查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"通道账号监控详情查询异常！");
			}
		}
	}
	
	/**
	 * 通道账号监控详情查询（从缓存中获取）
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
			//通道账号
			String gateaccount = request.getParameter("gateaccount");
			//账号传入类型 
			String gateaccountType = request.getParameter("gateaccountType");
			//账号名称
			String gatename = request.getParameter("gatename");
			//告警级别
			String evttype = request.getParameter("evttype");
			//在线状态
			String onlinestatus = request.getParameter("onlinestatus");
			//付费类型
			String feeflag = request.getParameter("feeflag");
			//mt滞留
			String mtremained = request.getParameter("mtremained");
			//mo滞留
			String moremained = request.getParameter("moremained");
			//rpt滞留
			String rptremained = request.getParameter("rptremained");
			List<MonDgateacParams> monList = new ArrayList<MonDgateacParams>();
			List<MonDgateacParams> pageList = new ArrayList<MonDgateacParams>();
			// 通道账号动态信息
			Map<String, MonDgateacParams> gateAccountMap = MonitorStaticValue.getGateAccountMap();
			if(gateAccountMap == null || gateAccountMap.size() < 1)
			{
				gateAccountMap = MonitorStaticValue.getGateAccountMapTemp();
			}
			// 通道账号基础信息
			Map<String, LfMonSgtacinfo> gateAccountBaseMap = MonitorStaticValue.getGateAccountBaseMap();
			if(gateAccountBaseMap == null || gateAccountBaseMap.size() < 1)
			{
				gateAccountBaseMap = MonitorStaticValue.getGateAccountBaseMapTemp();
			}
			//遍历程序动态信息map
			Iterator<String> keys = gateAccountMap.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();
				MonDgateacParams gateAcct = gateAccountMap.get(key);
				LfMonSgtacinfo baseGateAcct = gateAccountBaseMap.get(key);
				//过滤不监控的状态信息 状态不为1全部过滤
				if(!(baseGateAcct!=null&&baseGateAcct.getMonstatus()==1)){
					continue;
				}
				//账号条件过滤 如果为空说明没有限制 
				if(gateaccount!=null &&!"".equals(gateaccount))
				{	//如果为空则不匹配 跳出
					if(gateAcct.getGateaccount()==null||"".equals(gateAcct.getGateaccount())){
						continue;
					}else if("1".equals(gateaccountType)){//手动输入 模糊匹配
						if(gateAcct.getGateaccount().toLowerCase().indexOf(gateaccount.toLowerCase())<0){
							continue;
						}
					}else{//完全匹配
						if(!gateAcct.getGateaccount().equals(gateaccount)){
							continue;
						}
					}
				}
				//账号名称条件过滤
				if(gatename!=null &&!"".equals(gatename))
				{
					if(gateAcct.getGateName()==null||"".equals(gateAcct.getGateName())) {
						continue;
					}else if(gateAcct.getGateName().toLowerCase().indexOf(gatename.toLowerCase())<0){
						continue;
					}
				}
				//告警级别条件过滤
				if(evttype!=null &&!"".equals(evttype))
				{
					Integer type= gateAcct.getEvtType();
					if(type==null){type = -1;}
					if(evttype.equals("0")&&(type==1||type==2)){
						continue;
					}
					if(!evttype.equals("0")&&!evttype.equals(String.valueOf(type))){
						continue;
					}
				}
				//在线状态条件过滤
				if(onlinestatus!=null &&!"".equals(onlinestatus))
				{
					if(gateAcct.getOnlinestatus()==null) {
						continue;
					}else {
						String status = String.valueOf(gateAcct.getOnlinestatus());
						if(!"3".equals(onlinestatus) && !onlinestatus.equals(status))
						{
							continue;
						}
						if("3".equals(onlinestatus)&&("0".equals(status)||"1".equals(status)||"2".equals(status)))
						{
							continue;
						}
					}
				
				}
				//付费类型条件过滤
				if(feeflag!=null &&!"".equals(feeflag))
				{
					if(gateAcct.getFeeflag()==null) {
						continue;
					}else if(!feeflag.equals(String.valueOf(gateAcct.getFeeflag()))){
						continue;
					}
				}
				//mt滞留条件过滤
				if(!isMatch(mtremained, gateAcct.getMtremained())){
					continue;
				}
				//mo滞留条件过滤
				if(!isMatch(moremained, gateAcct.getMoremained())){
					continue;
				}
				//rpt滞留条件过滤
				if(!isMatch(rptremained, gateAcct.getRptremained())){
					continue;
				}
				monList.add(gateAcct);
			}
			//分页设置
			pageSetList(pageInfo,monList.size());
			for(int i=(pageInfo.getPageIndex()-1)*pageInfo.getPageSize();i<pageInfo.getPageIndex()*pageInfo.getPageSize()&&i<monList.size();i++)
			{
				pageList.add(monList.get(i));
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
			String opContent = "开始时间：" + sdf.format(stratTime) + " 数量：" + totalCount;
			
			EmpExecutionContext.info("通道账号监控详情", corpCode, userId, userName, opContent, "GET");
			
			request.getRequestDispatcher(this.empRoot+base+"/mon_gateAcctMon.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"通道账号监控详情查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_gateAcctMon.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"通道账号监控详情查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"通道账号监控详情查询异常！");
			}
		}
		
	}
	/**
	 * 通道账号监控详情查询--更多详情查看的异步请求
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author 
	 * @datetime 
	 */
	public void getDes(HttpServletRequest request, HttpServletResponse response)
	{
		String json = "";
		PrintWriter out = null;
		try {
			String gateaccount = request.getParameter("gateaccount");
			if(gateaccount==null||"".equals(gateaccount.trim())){
				return;
			}
			response.setContentType("text/html");
			out = response.getWriter();
			MonDgateacParams mon = new MonDgateacParams();
			// 程序监控详情信息
			Map<String, MonDgateacParams> gateAccountMap = MonitorStaticValue.getGateAccountMap();
			if(gateAccountMap == null || gateAccountMap.size() < 1)
			{
				gateAccountMap = MonitorStaticValue.getGateAccountMapTemp();
			}
			//遍历程序动态信息map
			Iterator<String> keys = gateAccountMap.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();
				MonDgateacParams gateAcct = gateAccountMap.get(key);
				//账号条件过滤
				if(gateaccount.equals(gateAcct.getGateaccount()))
				{
					mon = gateAcct;
					break;
				}
			}
			StringWriter writer = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(writer, mon);
            json=writer.toString();
            out.print(json);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取通道账号详情异常！");
		} finally{
			if(out!=null){
				out.close();
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
	 * @datetime 2013-11-18 下午07:01:01
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
		// 账号id
		String userId = "";
		if(request.getParameter("userId") != null){
			userId = request.getParameter("userId");
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
		//告警级别
		int evtType = -1;
		if (request.getParameter("evtType") != null) {
			evtType = Integer.parseInt(request.getParameter("evtType"));
		}
	}
	
	/**
	 * 获取通道账号详情
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhagmin
	 * @throws IOException 
	 * @throws ServletException 
	 * @datetime 2013-11-18 
	 */
	public void getGateAccountDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String userId = request.getParameter("userId");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", userId);
			List<MmonSpateinfo> monitorList = new BaseBiz().getByCondition(MmonSpateinfo.class, conditionMap, null);
			
			if(monitorList!=null && monitorList.size()>0)
			{
				MmonSpateinfo mmonSpateinfo = monitorList.get(0);
				request.setAttribute("mmonSpateinfo",mmonSpateinfo);
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
			request.getRequestDispatcher(this.empRoot+base+"/mon_oneGateAccount.jsp").forward(request,
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
	public void getGateAccts(HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter out = null;
		JSONArray array = new JSONArray();
		response.setContentType("text/html");
		try
		{
			out = response.getWriter();
			// 通道账号动态信息
			Map<String, MonDgateacParams> gateAccountMap = MonitorStaticValue.getGateAccountMap();
			if(gateAccountMap == null || gateAccountMap.size() < 1)
			{
				gateAccountMap = MonitorStaticValue.getGateAccountMapTemp();
			}
			// 通道账号基础信息
			Map<String, LfMonSgtacinfo> gateAccountBaseMap = MonitorStaticValue.getGateAccountBaseMap();
			if(gateAccountBaseMap == null || gateAccountBaseMap.size() < 1)
			{
				gateAccountBaseMap = MonitorStaticValue.getGateAccountBaseMapTemp();
			}
			//遍历程序动态信息map
			Iterator<String> keys = gateAccountMap.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();
				MonDgateacParams gateAcct = gateAccountMap.get(key);
				LfMonSgtacinfo baseGateAcct = gateAccountBaseMap.get(key);
				//过滤不监控的状态信息 状态不为1全部过滤
				if(!(baseGateAcct!=null&&baseGateAcct.getMonstatus()==1)){
					continue;
				}
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("gateAcct", gateAcct.getGateaccount());
				array.add(jsonObject);
				
			}
		}
		catch (IOException e)
		{
			EmpExecutionContext.error(e,"监控详情获取通道账号失败！");
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
				try{
					
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
