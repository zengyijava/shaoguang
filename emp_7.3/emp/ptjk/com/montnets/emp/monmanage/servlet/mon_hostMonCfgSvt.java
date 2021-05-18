package com.montnets.emp.monmanage.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.monitor.LfMonShost;
import com.montnets.emp.entity.monitor.LfMonSproce;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.monmanage.biz.HostMonCfgBiz;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
/**
 * 主机监控管理和配置servlet
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-26 上午10:34:41
 */
@SuppressWarnings("serial")
public class mon_hostMonCfgSvt extends BaseServlet {

	final String empRoot="ptjk";
	final String base="/monmanage";
	final HostMonCfgBiz hostCfg = new HostMonCfgBiz();
	protected final SuperOpLog spLog = new SuperOpLog();
	private final BaseBiz baseBiz = new BaseBiz();
	public static final String OPMODULE = "主机监控设置";
	/**
	 * 主机信息查询
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @datetime 2013-11-26 下午04:08:35
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//查询开始时间
		long stratTime = System.currentTimeMillis();
		
		PageInfo pageInfo = new PageInfo();
		try {
			List<DynaBean> monitorList = null;
			pageSet(pageInfo,request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			setConditionMap(request,conditionMap);
			monitorList = new HostMonCfgBiz().getHost(pageInfo, conditionMap);
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
			String opContent = "开始时间："+sdf.format(stratTime)+ " 耗时：" 
			+(System.currentTimeMillis()-stratTime)+"ms  数量："+totalCount;
			
			EmpExecutionContext.info("主机监控设置", corpCode, userId, userName, opContent, "GET");
			
			
			request.getRequestDispatcher(this.empRoot+base+"/mon_hostMonCfg.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"主机监控管理查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_hostMonCfg.jsp")
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
		String hostid = request.getParameter("hostid");
		//主机名称
		String hostname = request.getParameter("hostname");
		//内网ip1
		String adapter1 = request.getParameter("adapter1");
		if(hostid!=null && !"".equals(hostid))
		{
			conditionMap.put("hostid", hostid);
		}
		if(hostname!=null && !"".equals(hostname))
		{
			conditionMap.put("hostname", hostname);
		}
		if(adapter1!=null && !"".equals(adapter1))
		{
			conditionMap.put("adapter1", adapter1);
		}
		
	}
	
	/**
	 * 跳转到新建主机信息页面
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @throws ServletException 
	 * @datetime 2013-11-27 上午11:11:42
	 */
	public void toAdd(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("hostid","-1");
			conditionMap.put("proceusestatus","0");
			List<LfMonSproce> sprcList = new BaseBiz().getByCondition(LfMonSproce.class, conditionMap, null);
			request.setAttribute("sprcList", sprcList);
			//程序ID,如果从新建程序跳过来，会有值
			String prcId = request.getParameter("prcId");
			request.setAttribute("prcId", prcId);
			//表示从新建程序跳转过来的
//			String hrefType = request.getParameter("hrefType");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("跳转到添加主机信息页面异常！");
		}
		request.getRequestDispatcher(this.empRoot+base+"/mon_addHost.jsp").forward(request,
				response);
	}
	/**
	 * 检查该条记录是否存在
	 * @description    
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException       			 
	 * @author zhangsan <zhangsan@126.com>
	 * @datetime 2016-6-8 下午03:26:25
	 */
	public void checkHost(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		try
		{
			String ret="no";
			String hostid=request.getParameter("hostid");
			if(hostid!=null&&!"".equals(hostid)){
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("hostid",hostid);
				conditionMap.put("hostusestatus", "0");
				List<LfMonShost> sprcList = new BaseBiz().getByCondition(LfMonShost.class, conditionMap, null);
				if(sprcList!=null&&sprcList.size()>0){
					ret="yes";
				}
			}

			response.getWriter().print(ret);

		}
		catch (Exception e)
		{
			EmpExecutionContext.error("检查该条记录是否存在异常！");
		}
	}
	
	/**
	 * 跳转到主机信息设置
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @throws ServletException 
	 * @datetime 2013-11-27 上午11:11:42
	 */
	public void toEdit(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{
		try
		{
			String hostid = request.getParameter("hostid");
			if(hostid==null || "".equals(hostid))
			{
				EmpExecutionContext.error("根据主机id查询主机监控设置信息异常：hostid为空！");
				return;
			}
			//查询未被选择过的程序
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
//			String lguserid = request.getParameter("lguserid");
//			String lgcorpcode = request.getParameter("lgcorpcode");
			conditionMap.put("hostid", hostid);
			List<DynaBean> dhostList = new HostMonCfgBiz().getHost(null, conditionMap);
			if(dhostList!=null && dhostList.size()>0)
			{
				DynaBean dhostBean = dhostList.get(0);
				request.setAttribute("dhostBean", dhostBean);
			}
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"跳转主机监控设置页面异常！");
		}
		finally
		{
			request.getRequestDispatcher(this.empRoot+base+"/mon_editHost.jsp").forward(request,
					response);
		}
	}
	
	/**
	 * 添加主机配置信息
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @throws ServletException 
	 * @datetime 2013-11-27 下午02:19:28
	 */
	public void addHost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//操作模块
		String opModule = "系统监控";
		String opSper = StaticValue.OPSPER;
		String opType = StaticValue.ADD;
		String opContent = "新建主机";
		String opUser = "";
		String field = "[监控程序，主机名称，操作系统，主机内存，磁盘大小，CPU信息，监控状态，外网IP，内网IP，告警手机号，" +
						"CPU占用比率告警阀值，物理内存使用量告警阀值，虚拟内存使用量告警阀值，磁盘剩余空间告警阀值，进程数告警阀值]";
		//结果
		boolean result = false;
		LfSysuser sysuser = null;
		try
		{
			sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(sysuser!=null)
			{
				opUser=sysuser.getUserName();
			}
			//BaseBiz baseBiz = new BaseBiz();
			LfMonShost shost = new LfMonShost();
			//获取表单参数值
			this.getAddParValue(shost,request);
			opContent="新建主机（主机名称："+shost.getHostname()+"）";
			String proceid = request.getParameter("proceid");
			//查询未被选择过的程序
//			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
//			conditionMap.put("proceid&not in", "select proceid from lf_mon_sproce where hostid<>-1");
//			List<LfMonSproce> sprcList = baseBiz.getByCondition(LfMonSproce.class, conditionMap, null);
//			request.setAttribute("sprcList", sprcList);
//			//程序id
//			String proceid =request.getParameter("proceid");
			result = new HostMonCfgBiz().addHost(shost,proceid);
			spLog.logSuccessString(opUser, opModule, opType, opContent,sysuser==null?"":sysuser.getCorpCode());
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null)
			{
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String flag = result?"成功":"失败";
				String addvalueStr = this.getEditAfterValueStr(request, proceid);
				String opContent2 = "新建"+flag+"。"+field+addvalueStr;
				EmpExecutionContext.info(OPMODULE, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent2, "add");
			}
		}
		catch (Exception e) {
			EmpExecutionContext.error(e, "新建主机信息servlet层异常！");
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, null,sysuser==null?"":sysuser.getCorpCode());
		}
		finally
		{
			request.setAttribute("result", result);
			request.getRequestDispatcher(this.empRoot+base+"/mon_addHost.jsp").forward(request,
					response);
		}
		
	}
	
	/**
	 * 修改主机配置信息
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @throws ServletException 
	 * @datetime 2013-11-27 下午02:19:28
	 */
	public void editHost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	
		//操作模块
		String opModule = "系统监控";
		String opSper = StaticValue.OPSPER;
		String opType = StaticValue.UPDATE;
		String opContent = "修改主机";
		String opUser = "";
		//字段
		String field = "[主机编号，主机名称，操作系统，主机内存，磁盘大小，CPU信息，监控状态，外网IP，内网IP，告警手机号，" +
						"CPU占用比率告警阀值，物理内存使用量告警阀值，虚拟内存使用量告警阀值，磁盘剩余空间告警阀值，进程数告警阀值]";
		//修改结果
		boolean result = false;
		BaseBiz baseBiz = new BaseBiz();
		
		LfSysuser sysuser = null;
		try
		{
			sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(sysuser!=null)
			{
				opUser=sysuser.getUserName();
			}
			String hostid = request.getParameter("hostid");
			LfMonShost shost = baseBiz.getById(LfMonShost.class, hostid);
			//修改前字段
			String editBeforeValueStr = "(";
			editBeforeValueStr += shost.getHostid()+"，"+shost.getHostname()+"，"+shost.getOpersys()+"，"+shost.getHostmem()
								  +"，"+shost.getHosthd()+"，"+shost.getHostcpu()+"，"+shost.getMonstatus()+"，"+shost.getOupip()
								  +"，"+shost.getAdapter1()+"，"+shost.getMonphone()+"，"+shost.getMonemail()+"，"+shost.getCpuusage()+"，"+shost.getMemusage()
								  +"，"+shost.getVmemusage()+"，"+shost.getDiskfreespace()+"，"+shost.getProcesscnt();
			editBeforeValueStr += ")";
			//获取表单参数值
			this.getEditParValue(shost,request);
			opContent="修改主机（主机编号:"+hostid+"）";
			//暂没用到
			//result = new HostMonCfgBiz().editHost(shost);
			result = new BaseBiz().updateObj(shost);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("hostid", request.getParameter("hostid"));
			List<DynaBean> dhostList = new HostMonCfgBiz().getHost(null, conditionMap);
			if(dhostList!=null && dhostList.size()>0)
			{
				DynaBean dhostBean = dhostList.get(0);
				request.setAttribute("dhostBean", dhostBean);
			}
			spLog.logSuccessString(opUser, opModule, opType, opContent,sysuser==null?"":sysuser.getCorpCode());
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null)
			{
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String flag = result?"成功":"失败";
				String editAfterValueStr = this.getEditAfterValueStr(request, hostid);
				String opContent2 = "设置"+flag+"。"+field+editBeforeValueStr+"->"+editAfterValueStr;
				EmpExecutionContext.info(OPMODULE, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent2, "update");
			}
			
		}
		catch (Exception e) {
			EmpExecutionContext.error(e, "修改主机信息servlet层异常！");
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, null,sysuser==null?"":sysuser.getCorpCode());
		}
		finally
		{
			request.setAttribute("result", result);
			request.getRequestDispatcher(this.empRoot+base+"/mon_editHost.jsp").forward(request,
					response);
		}
		
	}
	
	/**
	 * 新建主机配置信息时获取表单参数值
	 * @description    
	 * @param shost 主机基本配置信息
	 * @param dhost 主机动态信息
	 * @param request       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 下午02:29:18
	 */
	public void getAddParValue(LfMonShost shost,HttpServletRequest request)
	{
		//-------------------主机基本信息-------------------
		//主机名称
		String hostname = request.getParameter("hostname");
		//监控状态
		String monstatus = request.getParameter("monstatus");
		//外网ip
		String oupip = request.getParameter("oupip");
		//内网第一个ip
		String adapter1 = request.getParameter("adapter1");
		//数据采集频率
		String monfreq = request.getParameter("monfreq");
		//告警手机号
		String monphone = request.getParameter("monphone");
		//告警邮箱
		String monemail = request.getParameter("monemail");
		//操作系统
		String opersys = request.getParameter("opersys");
		//主机内存
		String hostmem = request.getParameter("hostmem");
		//硬盘大小
		String hosthd =request.getParameter("hosthd");
		//CPU信息
		String hostcpu = request.getParameter("hostcpu");
		
		shost.setHostname(hostname);
		shost.setMonstatus(Integer.parseInt(monstatus!=null?monstatus:"0"));
		shost.setOupip(oupip!=null&&oupip.length()>0&&!"...".equals(oupip)?oupip:" ");
		shost.setAdapter1(adapter1!=null&&adapter1.length()>0&&!"...".equals(adapter1)?adapter1:" ");
		shost.setAdapter2(" ");
		shost.setMonfreq(Integer.parseInt(monfreq!=null&&monfreq.length()>0?monfreq:"0"));
		shost.setCreatetime(new Timestamp(System.currentTimeMillis()));
		shost.setModifytime(new Timestamp(System.currentTimeMillis()));
		//主机类型
		shost.setHosttype(1);
		shost.setMonphone(monphone);
		if(monemail==null || monemail.equals("")){monemail=" ";}
		shost.setMonemail(monemail);
		shost.setOpersys(opersys);
		shost.setHostmem(Integer.parseInt(hostmem!=null&&hostmem.length()>0?hostmem:"0"));
		shost.setHosthd(Integer.parseInt(hosthd!=null&&hosthd.length()>0?hosthd:"0"));
		shost.setHostcpu(hostcpu);
		//备注
		
		//---------主机告警阀值设置----------------------------
		//CPU占用比率告警阀值
		String cpubl = request.getParameter("cpubl");
		//CPU占用时间告警阀值
		String cpusj = request.getParameter("cpusj");
		//CPU使用量告警阀值，显示格式为XX% 
		String cpuusage = request.getParameter("cpuusage");
		//物理内存告警阀值
		String memusage = request.getParameter("memusage");
		//虚拟内存告警阀值
		String vmemusage = request.getParameter("vmemusage");
		//磁盘剩余空间告警阀值
		String diskfreespace = request.getParameter("diskfreespace");
		//线程总数告警阀值
		String processcnt =request.getParameter("processcnt");
		
		//CPU占用量  CPU使用量告警阀值，显示格式为XX%
		shost.setCpuusage(cpuusage!=null&&!"".equals(cpuusage)?Integer.parseInt(cpuusage):0);
		//物理内存告警阀值
		shost.setMemusage(memusage!=null&&!"".equals(memusage)?Integer.parseInt(memusage):0);
		//虚拟内存告警阀值
		shost.setVmemusage(vmemusage!=null&&!"".equals(vmemusage)?Integer.parseInt(vmemusage):0);
		//磁盘剩余空间告警阀值
		shost.setDiskfreespace(diskfreespace!=null&&!"".equals(diskfreespace)?Integer.parseInt(diskfreespace):0);
		//CPU占用时间告警阀值
		shost.setCpusj(cpusj!=null&&!"".equals(cpusj)?Integer.parseInt(cpusj):0);
		//CPU占用比率告警阀值
		shost.setCpubl(cpubl!=null&&!"".equals(cpubl)?Integer.parseInt(cpubl):0);
		//线程总数告警阀值
		shost.setProcesscnt(processcnt!=null&&!"".equals(processcnt)?Integer.parseInt(processcnt):0);
	}
	
	
	/**
	 * 保存成功操作日志
	 * @param request
	 * @param result
	 * @param loginSysuserObj
	 * @param operate
	 * @param hostid
	 */
	public String getEditAfterValueStr(HttpServletRequest request,String id)
	{
		String editAfterValueStr = "(";
		//主机名称
		String hostname = request.getParameter("hostname");
		//监控状态
		String monstatus = request.getParameter("monstatus");
		//外网ip
		String oupip = request.getParameter("oupip");
		//内网第一个ip
		String adapter1 = request.getParameter("adapter1");
		//数据采集频率
//		String monfreq = request.getParameter("monfreq");
		//告警手机号
		String monphone = request.getParameter("monphone");
		//告警邮箱
		String monemail = request.getParameter("monemail");
		//操作系统
		String opersys = request.getParameter("opersys");
		//主机内存
		String hostmem = request.getParameter("hostmem");
		//硬盘大小
		String hosthd =request.getParameter("hosthd");
		//CPU信息
		String hostcpu = request.getParameter("hostcpu");
		//---------主机告警阀值设置----------------------------
		//CPU占用比率告警阀值
//		String cpubl = request.getParameter("cpubl");
//		//CPU占用时间告警阀值
//		String cpusj = request.getParameter("cpusj");
		//CPU使用量告警阀值，显示格式为XX% 
		String cpuusage = request.getParameter("cpuusage");
		//物理内存告警阀值
		String memusage = request.getParameter("memusage");
		//虚拟内存告警阀值
		String vmemusage = request.getParameter("vmemusage");
		//磁盘剩余空间告警阀值
		String diskfreespace = request.getParameter("diskfreespace");
		//线程总数告警阀值
		String processcnt =request.getParameter("processcnt");
		//设置才有主机编号属性
		editAfterValueStr += id+"，" + hostname +"，" +opersys+"，"+hostmem+"，"+hosthd+"，"+hostcpu+"，"+monstatus+"，"+oupip+"，"+
							 adapter1+"，" +monphone+"，" +monemail+"，"+cpuusage+"，"+memusage+"，"+vmemusage+"，"+diskfreespace+"，"+processcnt+")";
		return editAfterValueStr;
	}
	
	
	/**
	 * 修改主机配置信息时获取表单参数值
	 * @description    
	 * @param shost
	 * @param dhost
	 * @param request       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 下午02:29:18
	 */
	public void getEditParValue(LfMonShost shost,HttpServletRequest request)
	{
		
		//-------------------主机基本信息修改-------------------
		//-------------------主机基本信息-------------------
		//主机名称
		String hostname = request.getParameter("hostname");
		//监控状态
		String monstatus = request.getParameter("monstatus");
		//外网ip
		String oupip = request.getParameter("oupip");
		//内网第一个ip
		String adapter1 = request.getParameter("adapter1");
		//数据采集频率
		String monfreq = request.getParameter("monfreq");
		//告警手机号
		String monphone = request.getParameter("monphone");
		//告警邮箱
		String monemail = request.getParameter("monemail");
		//操作系统
		String opersys = request.getParameter("opersys");
		//主机内存
		String hostmem = request.getParameter("hostmem");
		//硬盘大小
		String hosthd =request.getParameter("hosthd");
		//CPU信息
		String hostcpu = request.getParameter("hostcpu");
		shost.setHostname(hostname);
		shost.setMonstatus(Integer.parseInt(monstatus!=null?monstatus:"0"));
		shost.setOupip(oupip!=null&&oupip.length()>0&&!"...".equals(oupip)?oupip:" ");
		shost.setAdapter1(adapter1!=null&&adapter1.length()>0&&!"...".equals(adapter1)?adapter1:" ");
		shost.setAdapter2(" ");
		shost.setOupip(oupip);
		shost.setMonfreq(Integer.parseInt(monfreq!=null&&monfreq.length()>0?monfreq:"0"));
		shost.setModifytime(new Timestamp(System.currentTimeMillis()));
		shost.setMonphone(monphone);
		if(monemail==null || monemail.equals("")){monemail=" ";}
		shost.setMonemail(monemail);
		shost.setOpersys(opersys);
		shost.setHostmem(Integer.parseInt(hostmem!=null&&hostmem.length()>0?hostmem:"0"));
		shost.setHosthd(Integer.parseInt(hosthd!=null&&hosthd.length()>0?hosthd:"0"));
		shost.setHostcpu(hostcpu);
		//备注
		
		//---------主机告警阀值设置----------------------------
		//CPU占用比率告警阀值
		String cpubl = request.getParameter("cpubl");
		//CPU占用时间告警阀值
		String cpusj = request.getParameter("cpusj");
		//CPU使用量告警阀值，显示格式为XX% 
		String cpuusage = request.getParameter("cpuusage");
		//物理内存告警阀值
		String memusage = request.getParameter("memusage");
		//虚拟内存告警阀值
		String vmemusage = request.getParameter("vmemusage");
		//磁盘剩余空间告警阀值
		String diskfreespace = request.getParameter("diskfreespace");
		//线程总数告警阀值
		String processcnt =request.getParameter("processcnt");
		
		//CPU占用量  CPU使用量告警阀值，显示格式为XX%
		shost.setCpuusage(cpuusage!=null&&!"".equals(cpuusage)?Integer.parseInt(cpuusage):0);
		//物理内存告警阀值
		shost.setMemusage(memusage!=null&&!"".equals(memusage)?Integer.parseInt(memusage):0);
		//虚拟内存告警阀值
		shost.setVmemusage(vmemusage!=null&&!"".equals(vmemusage)?Integer.parseInt(vmemusage):0);
		//磁盘剩余空间告警阀值
		shost.setDiskfreespace(diskfreespace!=null&&!"".equals(diskfreespace)?Integer.parseInt(diskfreespace):0);
		//CPU占用时间告警阀值
		shost.setCpusj(cpusj!=null&&!"".equals(cpusj)?Integer.parseInt(cpusj):0);
		//CPU占用比率告警阀值
		shost.setCpubl(cpubl!=null&&!"".equals(cpubl)?Integer.parseInt(cpubl):0);
		//线程总数告警阀值
		shost.setProcesscnt(processcnt!=null&&!"".equals(processcnt)?Integer.parseInt(processcnt):0);
		
	}
	public void delete (HttpServletRequest request,HttpServletResponse response) throws IOException{
		//操作模块
		String opModule = "系统监控";
		String opSper = StaticValue.OPSPER;
		String opType = StaticValue.DELETE;
		String opContent = "删除主机";
		String opUser = "";
		String json = "error";
		PrintWriter out = null;
		response.setContentType("text/html");
		out = response.getWriter();
		String id = request.getParameter("id");
		LfSysuser sysuser = null;
		try {
			sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			opContent="删除主机（主机编号："+id+"）";
			if(sysuser!=null)
			{
				opUser=sysuser.getUserName();
			}
			if(id==null||"".equals(id)){
				json = "error";
				return;
			}
			long hostid = Long.parseLong(id);
			//获取删除前信息
			LfMonShost lfMonShost = baseBiz.getById(LfMonShost.class, hostid);
			String valueStr = "(";
			if(lfMonShost!=null)
			{
				valueStr += lfMonShost.getHostid()+"，"+lfMonShost.getHostname()+"，"+lfMonShost.getMonstatus();
			}
			valueStr+=")";
			if(hostCfg.delete(hostid)){
				json = "success";
				spLog.logSuccessString(opUser, opModule, opType, opContent,sysuser==null?"":sysuser.getCorpCode());
			}
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null)
			{
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String flag = "失败";
				if("success".equals(json))
				{
					flag = "成功";
				}
				String filed = "[主机编号，主机名称，监控状态]";
				String opContent2 = "删除主机监控"+flag+"。"+filed+valueStr;
				EmpExecutionContext.info(OPMODULE, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent2, "delete");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取通道账号详情异常！");
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, null,sysuser==null?"":sysuser.getCorpCode());
			json = "error";
		}finally{
			out.print(json);
		}
	}
}
