package com.montnets.emp.monview.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.monitor.LfMonSgtacinfo;
import com.montnets.emp.entity.monitor.LfMonShost;
import com.montnets.emp.entity.monitor.LfMonSproce;
import com.montnets.emp.entity.monitor.LfMonSspacinfo;
import com.montnets.emp.entity.system.LfUser2skin;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.monitor.biz.AccoutBaseInfoBiz;
import com.montnets.emp.monitor.constant.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("serial")
public class mon_mainMonSvt extends BaseServlet
{

	final String empRoot="ptjk";
	final String base="/monview";
	
	/**
	 * 监控视图主界面
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @datetime 2013-12-30 下午04:08:35
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		try {
			// 设置SP账号和通道账号信息
			new AccoutBaseInfoBiz().getSpAndGateInfo();
			//表示main.jsp过来来
			String herfType = request.getParameter("herfType");
			if("1".equals(herfType))
			{
				request.getRequestDispatcher(this.empRoot+base+"/mon_index.jsp").forward(request,
						response);
			}
			else
			{
				request.getRequestDispatcher(this.empRoot+base+"/mon_btngo.jsp").forward(request,
						response);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"监控视图主界面查询异常！");
			request.setAttribute("findresult", "-1");
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_index.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"监控视图主界面查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"监控视图主界面查询异常！");
			}
		}
	}
	
	/**
	 * 获取监控视图主界面信息方法
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @throws ServletException 
	 * @datetime 2013-12-30 下午04:08:35
	 */
	public void getMonInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
			request.getRequestDispatcher(this.empRoot+base+"/mon_mainMon.jsp").forward(request,
					response);
	}
	
	@SuppressWarnings("unchecked")
	public void getJson(HttpServletRequest request, HttpServletResponse response)
	{
		//提交总量
		Long spSubCount = 0L;
		//运营商已发总量
		Long bufSendCount = 0L;
		//sp待发总条数
		Long spWaitSendCount = 0L;
		//通道账号待发总条数
		Long gtWaitSendCount = 0L;
		//待发总量(柱子)=提交总量-已发总量-SPGATE待发总量
		Long waitSendCount = 0L;
		//标示运营商接口告警状态 0.正常  1.警告  2.严重
		String gtErrType = "0";
		//标示EMP网关告警状态 0.正常  1.警告  2.严重
		String wbsErrType = "0";
		//EMP网关程序状态
		String wbsPrcState = "0";
		int ismonvoice = 0;
		PrintWriter out = null;
		JSONObject json = new JSONObject();
		try {
			response.setCharacterEncoding("UTF-8");
			out = response.getWriter();
			//查询该操作员是否开启声音告警
			String monvoice = (String)request.getSession(false).getAttribute("monvoice");
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			if(monvoice==null)
			{
				LinkedHashMap<String, String> condMap = new LinkedHashMap<String, String>();
				condMap.put("userid", lguserid);
				List<LfUser2skin> userSkin = new BaseBiz().getByCondition(LfUser2skin.class, condMap, null);
				ismonvoice = userSkin!=null&&userSkin.size()>0?userSkin.get(0).getMonvoice():0;
				request.getSession(false).setAttribute("monvoice", monvoice);
				
			}
			else
			{
				
				ismonvoice=Integer.parseInt(monvoice);
			}
			//sp账号搜索
			String keyword = request.getParameter("keyword");
			//是否有sp账号条件查询
			boolean isHaveKeyWord = this.isHaveCondtion(keyword);
			List<MonDspAccountParams> spList = new ArrayList<MonDspAccountParams>();
			//正常
			List<MonDspAccountParams> zcList = new ArrayList<MonDspAccountParams>();
			//告警
			List<MonDspAccountParams> gzList = new ArrayList<MonDspAccountParams>();
			//严重sp账号
			List<MonDspAccountParams> yzList = new ArrayList<MonDspAccountParams>();
			List<MonDgateacParams> gtList = new ArrayList<MonDgateacParams>();
			List<MonDgateacParams> gtZcList = new ArrayList<MonDgateacParams>();
			List<MonDgateacParams> gtYzList = new ArrayList<MonDgateacParams>();
			List<MonDgateacParams> gtGjList = new ArrayList<MonDgateacParams>();
			
			List<MonDhostParams> dhostList = new ArrayList<MonDhostParams>();
			List<MonDgateBufParams> bufList = new ArrayList<MonDgateBufParams>();
			List<MonErrorParams> errList = new ArrayList<MonErrorParams>();
			// SP账号监控动态信息
			Map<String, MonDspAccountParams> spAccountMap = MonitorStaticValue.getSpAccountMap();
			if(spAccountMap == null || spAccountMap.size() < 1)
			{
				spAccountMap = MonitorStaticValue.getSpAccountMapTemp();
			}
			
			// 通道账号监控动态信息
			Map<String, MonDgateacParams> gateAccountMap = MonitorStaticValue.getGateAccountMap();
			if(gateAccountMap == null || gateAccountMap.size() < 1)
			{
				gateAccountMap = MonitorStaticValue.getGateAccountMapTemp();
			}
			
			// 主机监控动态信息
			Map<Long, MonDhostParams> hostMap = MonitorStaticValue.getHostMap();
			if(hostMap == null || hostMap.size() < 1)
			{
				hostMap = MonitorStaticValue.getHostMapTemp();
			}
			
			//spgate缓冲信息
			Map<String, MonDgateBufParams> bufMap =  MonitorStaticValue.getSpgateBufInfoMap();
			if(bufMap == null || bufMap.size() < 1)
			{
				bufMap =  MonitorStaticValue.getSpgateBufInfoMapTemp();
			}
			
			// 主机监控动态信息
			Map<Long, MonDproceParams> prcMap =  MonitorStaticValue.getProceMap();
			if(prcMap == null || prcMap.size() < 1)
			{
				prcMap =  MonitorStaticValue.getProceMapTemp();
			}
			// 主机监控动态信息
			TreeMap<Long, MonErrorParams> errMap =  MonitorStaticValue.getMonError();
			if(errMap == null || errMap.size() < 1)
			{
				errMap =  MonitorStaticValue.getMonErrorTemp();
			}
			
			//遍历sp账号动态信息map
			Iterator<String> spkey = spAccountMap.keySet().iterator();
			Map<String, LfMonSspacinfo> spAccountBaseMap = MonitorStaticValue.getSpAccountBaseMap();
			if(spAccountBaseMap == null || spAccountBaseMap.size() < 1)
			{
				spAccountBaseMap = MonitorStaticValue.getSpAccountBaseMapTemp();
			}
			while(spkey.hasNext()){
				String key = spkey.next();//key
				MonDspAccountParams sp = spAccountMap.get(key);
				LfMonSspacinfo basesp = spAccountBaseMap.get(key);
				//过滤掉未监控的
				if(basesp!=null&&basesp.getMonstatus()==0){
					continue;
				}
				//sp条件查询过滤
				if(isHaveKeyWord && !keyword.equals(sp.getSpaccountid()))
				{
					continue;
				}
				if(sp.getEvtType()==2)
				{
					wbsErrType = "2";
				}
				else if (!"2".equals(wbsErrType)&&sp.getEvtType()==1)
				{
					wbsErrType = "1";
				}
				spSubCount = sp.getMtTotalSnd() + spSubCount;
				spWaitSendCount = sp.getMtremained() + spWaitSendCount;
				//根据告警类型排序
				if(sp.getEvtType()==1)
				{
					gzList.add(sp);
				}
				else if(sp.getEvtType()==2)
				{
					yzList.add(sp);
				}
				else
				{
					zcList.add(sp);
				}
				
			}
			spList.addAll(yzList);
			spList.addAll(gzList);
			spList.addAll(zcList);
			yzList=null;
			gzList=null;
			zcList=null;
			
			//遍历通道账号动态信息map
			Iterator<String> gtkey = gateAccountMap.keySet().iterator();
			Map<String, LfMonSgtacinfo> gateAccountBaseMap = MonitorStaticValue.getGateAccountBaseMap();
			if(gateAccountBaseMap == null || gateAccountBaseMap.size() < 1)
			{
				gateAccountBaseMap = MonitorStaticValue.getGateAccountBaseMapTemp();
			}
			while(gtkey.hasNext()){
				String key = gtkey.next();//key
				MonDgateacParams gt = gateAccountMap.get(key);
				LfMonSgtacinfo basegt = gateAccountBaseMap.get(key);
				//MonDgateBufParams gtBuf = MonitorStaticValue.spgateBufInfoMap.get(gt.getGatewayId());
				MonDgateBufParams gtBuf = bufMap.get(gt.getGateaccount());
				//过滤掉未监控的
				if(basegt!=null&&basegt.getMonstatus()==0){
					continue;
				}
				if(gt.getEvtType()==2)
				{
					gtErrType = "2";
				}
				else if (!"2".equals(gtErrType)&&gt.getEvtType()==1)
				{
					gtErrType = "1";
				}
				//gtList.add(gt);
				//根据告警类型排序
				if(gt.getEvtType()==1)
				{
					gtGjList.add(gt);
				}
				else if(gt.getEvtType()==2)
				{
					gtYzList.add(gt);
				}
				else
				{
					gtZcList.add(gt);
				}
				bufSendCount = bufSendCount+(gtBuf!=null?gtBuf.getMtsdcnt():0);
				gtWaitSendCount = gt.getMtremained() + gtWaitSendCount;
			}
			gtList.addAll(gtYzList);
			gtList.addAll(gtGjList);
			gtList.addAll(gtZcList);
			gtZcList=null;
			gtGjList=null;
			gtYzList=null;
			
			//遍历spgate的buffer动态信息map
			Iterator<String> spBufKey = bufMap.keySet().iterator();
			while(spBufKey.hasNext()){
				String key = spBufKey.next();//key
				MonDgateBufParams buf = bufMap.get(key);
				//bufSendCount = bufSendCount+buf.getMtsdcnt();
				bufList.add(buf);
			}
			
			//遍历主机动态信息map
			Iterator<Long> hostkey = hostMap.keySet().iterator();
			Map<Long, LfMonShost> hostBaseMap = MonitorStaticValue.getHostBaseMap();
			if(hostBaseMap == null || hostBaseMap.size() < 1)
			{
				hostBaseMap = MonitorStaticValue.getHostBaseMapTemp();
			}
			while(hostkey.hasNext()){
				Long key = hostkey.next();//key
				MonDhostParams host = hostMap.get(key);
				if(hostBaseMap.get(key)!=null&&hostBaseMap.get(key).getMonstatus()==1)
				{
					dhostList.add(host);
				}
			}
			//是否有告警信息（处理状态未新建的告警信息），1有 2无；
			int isHaveMon=2;
			//告警信息
			Iterator<Long> errkey = errMap.keySet().iterator();
			while(errkey.hasNext()){
				Long key = errkey.next();//key
				MonErrorParams mon = errMap.get(key);
				errList.add(mon);
				if(1==mon.getDealflag())
				{
					isHaveMon=1;
				}
			}
			//按时间排序
			orderErrList(errList);
			//遍历程序动态信息map
			Iterator<Long> prckey = prcMap.keySet().iterator();
			Map<Long, LfMonSproce> proceBaseMap = MonitorStaticValue.getProceBaseMap();
			if(proceBaseMap == null || proceBaseMap.size() < 1)
			{
				proceBaseMap = MonitorStaticValue.getProceBaseMapTemp();
			}
			while(prckey.hasNext()){
				Long key = prckey.next();//key
				MonDproceParams mon = prcMap.get(key);
				if(proceBaseMap.get(key)!=null&&proceBaseMap.get(key).getGatewayid()==99)
				{
					wbsPrcState = mon.getEvttype().toString();
				}
			}
			JSONArray right = getright(gtList);
			//待发总量(柱子)=提交总量-已发总量-SPGATE待发总量
			//waitSendCount=spSubCount - bufSendCount - gtWaitSendCount;
			//待发总量（柱子），直接使用SP待发总量
			waitSendCount = spWaitSendCount;
			long actualheight = (bufSendCount+waitSendCount)!=0?Math.abs(waitSendCount)*100/(bufSendCount+Math.abs(waitSendCount)):0;
			if(actualheight==0&&waitSendCount!=0)
			{
				actualheight=1;
			}
			json.put("ismonvoice", ismonvoice);
			json.put("isHaveMon",isHaveMon);
			//sp待发总量
			json.put("spwaitsendcount",spWaitSendCount);
			//待发总量
			json.put("waitsendcount",waitSendCount);
			//json.put("waitsendcount",(spSubCount - bufSendCount)>0?(spSubCount - bufSendCount):0 );
			//json.put("waitsendcount",waitSendCount );
			json.put("bufsendcount",bufSendCount );
			//提交总量
			json.put("volume", spSubCount);
			//json.put("volume", bufSendCount+waitSendCount);
			json.put("wbsprcstyle", "1".equals(wbsPrcState)?"abnormal":("2".equals(wbsPrcState)?"malfunction":"normal"));
			json.put("gterrstyle", "1".equals(gtErrType)?"abnormal":("2".equals(gtErrType)?"malfunction":"normal"));
			json.put("actualheight",actualheight );
			json.put("voicestyle", 0==ismonvoice?"":"closeVoice");
			json.put("online", StaticValue.getMonOnlinecfg() !=null? StaticValue.getMonOnlinecfg().getOnlinenum():"0" );
			json.put("left", getleft(spList,wbsPrcState));
			json.put("main", getmain(gtList));
			json.put("right", right);
			json.put("host", gethost(dhostList,request));
			json.put("warn", getwarn(errList,20,request));
			json.put("showvoice",0==ismonvoice?"":"closeVoice");
		} catch (Exception e) {
			EmpExecutionContext.error(e, "监控主界面获取数据异常！");
		}finally{
			out.print(json.toString());
		}
	}
	
	
	/**
	 * 判断是否含有条件查询
	 * @description    
	 * @param keyword sp账号
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-1-6 上午10:27:44
	 */
	public boolean isHaveCondtion(String keyword)
	{
		if(keyword!=null && "undefined".equals(keyword) && "".equals(keyword) )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * 设置查询条件
	 * @description    
	 * @param request
	 * @param conditionMap       			 
	 * @author zhangmin
	 * @datetime 2014-1-2 下午04:59:49
	 */
	public void setConditionMap(HttpServletRequest request,LinkedHashMap<String, String> conditionMap )
	{
		//监控状态
		String monstatus = request.getParameter("monstatus");
		//用户账号
		String spaccountid = request.getParameter("spaccountid");
		//账号名称
		String accountname = request.getParameter("accountname");
		conditionMap.put("accountname", accountname);
		conditionMap.put("monstatus", monstatus);
		conditionMap.put("spaccountid", spaccountid);
		
	}
	
	/**
	 * 切换声音告警
	 * @description    
	 * @param request
	 * @author zhangmin
	 * @throws IOException 
	 * @datetime 2014-1-2 下午04:59:49
	 */
	public void changeVoice(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String rst = "success";
		try
		{
			
			//声音告警0.告警  1.不告警
			String state = request.getParameter("state");
			//操作员id
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			BaseBiz basebiz = new BaseBiz();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objiectMap = new LinkedHashMap<String, String>();
			conditionMap.put("userid", lguserid);
			objiectMap.put("monvoice", state);
			boolean result = basebiz.update(LfUser2skin.class, objiectMap, conditionMap);
			if(result)
			{
				request.getSession(false).setAttribute("monvoice",state);
			}
			else
			{
				rst="fail";
			}
		}
		catch (Exception e) {
			rst="error";
			EmpExecutionContext.error(e,"监控声音告警切换异常！");
		}
		finally
		{
			response.getWriter().print(rst);
		}
		
	}

	/**
	 * 右下角弹出框解析
	 * @description    
	 * @param request
	 * @author zhangmin
	 * @throws IOException 
	 * @datetime 2014-1-2 下午04:59:49
	 */
	public void getErrMon(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		JSONArray json = new JSONArray();
		String result = "";
		List<MonErrorParams> errList = new ArrayList<MonErrorParams>();
		try
		{
			// 主机监控动态信息
			TreeMap<Long, MonErrorParams> errMap =  MonitorStaticValue.getMonError();
			//告警信息
			Iterator<Long> errkey = errMap.keySet().iterator();
			while(errkey.hasNext()){
				Long key = errkey.next();//key
				MonErrorParams mon = errMap.get(key);
				errList.add(mon);
			}
			//按时间排序
			orderErrList(errList);
			json =getwarn(errList,3,request);
			result = json.toString();
		}
		catch (Exception e) {
			result = "error";
			EmpExecutionContext.error(e,"右下角弹出框获取告警信息异常！");
		}
		finally
		{
			response.getWriter().print(result.toString());
		}
		
	}
	@SuppressWarnings("unchecked")
	public JSONArray getleft(List<MonDspAccountParams> spList,String wbsPrcState){
		JSONArray array = new JSONArray();
		for(int i=0;i<spList.size()&&i<8;i++)
		{
			JSONObject json = new JSONObject();
			MonDspAccountParams sp = spList.get(i);
			json.put("spaccountid", sp.getSpaccountid());
			json.put("mtremained", sp.getMtremained());
			json.put("mtsndspd", sp.getMtsndspd());
			if(sp.getEvtType()!=null&&sp.getEvtType()==1){
				json.put("style", "abnormal");
			}else{
				json.put("style", "normal");
			}
			if("2".equals(wbsPrcState)){
				json.put("ballstyle", "err");
				json.put("linestyle", "errline");
			}else{
				json.put("linestyle", "normalline");
				json.put("ballstyle", "");
			}
			array.add(json);
		}
		return array;
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getright(List<MonDgateacParams> gtList){
		JSONArray array = new JSONArray();
		Map<String, MonDgateBufParams> spgateBufInfoMap = MonitorStaticValue.getSpgateBufInfoMap();
		if(spgateBufInfoMap == null || spgateBufInfoMap.size() < 1)
		{
			spgateBufInfoMap = MonitorStaticValue.getSpgateBufInfoMapTemp();
		}
		for(int i=0;i<gtList.size()&&i<9;i++)
		{
			JSONObject json = new JSONObject();
			MonDgateacParams gt = gtList.get(i);
			MonDgateBufParams gtBuf = spgateBufInfoMap.get(gt.getGateaccount());
			json.put("gatename", gt.getGateName());
//			json.put("acctname", MonViewParams.getGtAcctName(gt.getGateName()));
			int mtsdcnt = gtBuf!=null?gtBuf.getMtsdcnt():0;
			json.put("mtsdcnt", mtsdcnt);
			json.put("mtspd1", gtBuf!=null?gtBuf.getMtspd1():0);
			if(gt.getEvtType()==null||(gt.getEvtType()!=1&&gt.getEvtType()!=2)){
				json.put("style", "normal");
				json.put("linestyle", "normalline");
				json.put("ballstyle", "");
			}else if(gt.getEvtType()==1){
				json.put("style", "abnormal");
				json.put("linestyle", "normalline");
				json.put("ballstyle", "");
			}if(gt.getEvtType()==2){
				json.put("style", "malfunction");
				json.put("linestyle", "errline");
				json.put("ballstyle", "err");
			}
			array.add(json);
		}
		return array;
	}
	
	
	@SuppressWarnings("unchecked")
	public JSONArray getmain(List<MonDgateacParams> gtList){
		JSONArray array = new JSONArray();
		for(int i=0;i<gtList.size()&&i<9;i++)
		{
			JSONObject json = new JSONObject();
			MonDgateacParams gt = gtList.get(i);
			json.put("gatename", gt.getGateName());
//			json.put("_gatename", MonViewParams.getAcctName(gt.getGateName()));
			json.put("gateaccount", gt.getGateaccount());
			json.put("mtremained",gt.getMtremained());
			if(gt.getEvtType()==null||(gt.getEvtType()!=1&&gt.getEvtType()!=2)){
				json.put("style", "normal");
			}else if(gt.getEvtType()==1){
				json.put("style", "abnormal");
			}if(gt.getEvtType()==2){
				json.put("style", "malfunction");
			}
			array.add(json);
		}
		return array;
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray gethost(List<MonDhostParams> dhostList,HttpServletRequest request){
		JSONArray array = new JSONArray();
		Map<Long, LfMonShost> hostBaseMap = MonitorStaticValue.getHostBaseMap();
		if(hostBaseMap == null || hostBaseMap.size() < 1)
		{
			hostBaseMap = MonitorStaticValue.getHostBaseMapTemp();
		}
		for(int i=0;i<dhostList.size();i++)
		{
			JSONObject json = new JSONObject();
			MonDhostParams dhost = dhostList.get(i);
			Map<Integer, Long> monThresholdFlag = dhost.getMonThresholdFlag();
			LfMonShost shost=hostBaseMap.get(dhost.getHostid()); 
			//物理内存占用量
			int memUsage = dhost.getMemusage();
			//物理内存
			int MemUse = dhost.getMemuse();
			//物理内存%比
			int mem = (int)(MemUse==0?0:((memUsage/1024.0)*100/(MemUse/1024.0)));
			
			//磁盘空间
			int diskspace=dhost.getDiskspace();
			//磁盘剩余量
			int diskfreespace=dhost.getDiskfreespace();
			int disk =(int) (diskspace!=0?((diskspace-diskfreespace)*100.0/diskspace):0);
			boolean isCpuAlarm = false;
			if(monThresholdFlag.get(2) == -1 || monThresholdFlag.get(2) == 1 
				|| (monThresholdFlag.get(2) != 0 && System.currentTimeMillis() - monThresholdFlag.get(2) >= 3*60*1000))
			{
				isCpuAlarm = true;
			}
			boolean isMemAlarm = false;
			if(monThresholdFlag.get(3) == -1 || monThresholdFlag.get(3) == 1 
					|| (monThresholdFlag.get(3) != 0 && System.currentTimeMillis() - monThresholdFlag.get(3) >= 3*60*1000))
			{
				isMemAlarm = true;
			}
			json.put("hostid", shost.getHostid());
			json.put("hostname", shost.getHostname());
			json.put("hostusestatus", dhost.getHoststatus()==2?"<font color='red'>  （"+MessageUtils.extractMessage("ptjk","ptjk_common_ych",request)+"）</font>":dhost.getHoststatus()==3?"<font color='red'>  （"+MessageUtils.extractMessage("ptjk","ptjk_common_ych",request)+"）</font>":"");
			json.put("cpustyle", (shost.getCpuusage()!=0&&dhost.getCpuusage()>=shost.getCpuusage()&&isCpuAlarm)?"abnormal":"");
			json.put("dcpuusage", dhost.getCpuusage());
			json.put("memustyle", shost.getMemusage()!=0&&MemUse>=shost.getMemusage()&&isMemAlarm?"abnormal":"" );
			json.put("dmemusage", String.format("%.1f",memUsage/1024.0)+"/"+String.format("%.1f",MemUse/1024.0));
			json.put("memuwidth", mem);
			json.put("diskstyle", shost.getDiskfreespace()!=0&&diskfreespace<=shost.getDiskfreespace()?"abnormal":"");
			json.put("ddiskfreespace", String.format("%.1f",(diskspace-diskfreespace)/1024.0)+"/"+String.format("%.1f",diskspace/1024.0));
			json.put("diskwidth", disk);
			array.add(json);
		}
		return array;
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getwarn(List<MonErrorParams> errList,int size,HttpServletRequest request){
		if(size<=0){size=20;}
		JSONArray array = new JSONArray();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<Long, LfMonShost> hostBaseMap = MonitorStaticValue.getHostBaseMap();
		if(hostBaseMap == null || hostBaseMap.size() < 1)
		{
			hostBaseMap = MonitorStaticValue.getHostBaseMapTemp();
		}
		Map<Long, LfMonSproce> proceBaseMap = MonitorStaticValue.getProceBaseMap();
		if(proceBaseMap == null || proceBaseMap.size() < 1)
		{
			proceBaseMap = MonitorStaticValue.getProceBaseMapTemp();
		}
		Map<String, LfMonSspacinfo> spAccountBaseMap = MonitorStaticValue.getSpAccountBaseMap();
		if(spAccountBaseMap == null || spAccountBaseMap.size() < 1)
		{
			spAccountBaseMap = MonitorStaticValue.getSpAccountBaseMapTemp();
		}
		Map<String, LfMonSgtacinfo> gateAccountBaseMap = MonitorStaticValue.getGateAccountBaseMap();
		if(gateAccountBaseMap == null || gateAccountBaseMap.size() < 1)
		{
			gateAccountBaseMap = MonitorStaticValue.getGateAccountBaseMapTemp();
		}
		for(int i=0;i<errList.size()&&i<size;i++)
		{
			MonErrorParams mon = errList.get(i);
			String monName = "";
			if(3000==mon.getApptype())
			{
				if(hostBaseMap.get(mon.getHostid())!=null)
				{
					monName ="["+MessageUtils.extractMessage("ptjk","ptjk_common_zj",request)+"]"+ hostBaseMap.get(mon.getHostid()).getHostname();
				}
			}
			else
			{
				if(5000==mon.getApptype())
				{
					if( proceBaseMap.get(mon.getProceid())!=null)
					{
						monName ="["+MessageUtils.extractMessage("ptjk","ptjk_common_webcx",request)+"]"+proceBaseMap.get(mon.getProceid()).getProcename();
					}
				}
				else if(5200==mon.getApptype())
				{
					if(proceBaseMap.get(mon.getProceid())!=null)
					{
						monName ="["+MessageUtils.extractMessage("ptjk","ptjk_common_empwg",request)+"]"+ proceBaseMap.get(mon.getProceid()).getProcename();
					}
				}
				else if(5300==mon.getApptype())
				{
					if(proceBaseMap.get(mon.getProceid())!=null)
					{
						
						monName ="["+MessageUtils.extractMessage("ptjk","ptjk_common_yysjk",request)+"]"+ proceBaseMap.get(mon.getProceid()).getProcename();
					}
				}
				else if(5400==mon.getApptype())
				{
					if(spAccountBaseMap.get(mon.getSpaccountid())!=null)
					{
						monName ="["+MessageUtils.extractMessage("ptjk","ptjk_common_spzh",request)+"]"+spAccountBaseMap.get(mon.getSpaccountid()).getAccountname();
					}
				}
				else if(5500==mon.getApptype())
				{
					if(gateAccountBaseMap.get(mon.getGateaccount())!=null)
					{
						monName ="["+MessageUtils.extractMessage("ptjk","ptjk_common_tdzh",request)+"]"+ gateAccountBaseMap.get(mon.getGateaccount()).getGatename();
					}
				}
				else if(5600==mon.getApptype())
				{
					monName ="["+MessageUtils.extractMessage("ptjk","ptjk_jkxq_ssgj_5",request)+"]";
				}
				else if(5700==mon.getApptype())
				{
					if( proceBaseMap.get(mon.getProceid())!=null)
					{
						monName ="["+MessageUtils.extractMessage("ptjk","ptjk_common_webcx",request)+"]"+proceBaseMap.get(mon.getProceid()).getProcename();
					}
				}
			}
			JSONObject json = new JSONObject();
			json.put("evttime", sdf.format(mon.getRcvtime()));
			json.put("id", mon.getId());
			json.put("monname", monName);
			json.put("msg", mon.getMsg());
			array.add(json);	
		}
		return array;
	}
	
	public void orderErrList(List<MonErrorParams> errList){
		Comparator<MonErrorParams> c = new Comparator<MonErrorParams>()
		{
			public int compare(MonErrorParams o1, MonErrorParams o2)
			{
				if(o1.getRcvtime().after(o2.getRcvtime())){
					return -1;
				}else{
					return 1;
				}
			}
		};
		Collections.sort(errList,c);
	}
}
