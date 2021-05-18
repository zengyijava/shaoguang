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

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.gateway.AgwAccount;
import com.montnets.emp.entity.monitor.LfMonShost;
import com.montnets.emp.entity.monitor.LfMonSproce;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.monmanage.biz.PrcMonCfgBiz;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
/**
 * 程序监控管理设置
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-28 下午02:24:07
 */
@SuppressWarnings("serial")
public class mon_prcMonCfgSvt extends BaseServlet
{

	final String empRoot="ptjk";
	final String base="/monmanage";
	final PrcMonCfgBiz prcCfg = new PrcMonCfgBiz();
	protected final SuperOpLog spLog = new SuperOpLog();
	private final BaseBiz baseBiz = new BaseBiz();
	public static final String OPMODULE = "程序监控设置";
	/**
	 * 程序监控管理查询
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
			PrcMonCfgBiz prcMonCfgBiz = new PrcMonCfgBiz();
			monitorList = prcMonCfgBiz.getSprc(pageInfo, conditionMap);
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
			+(System.currentTimeMillis()-stratTime) + "ms  数量："+totalCount;

			EmpExecutionContext.info("程序监控设置", corpCode, userId, userName, opContent, "GET");

			request.getRequestDispatcher(this.empRoot+base+"/mon_prcMonCfg.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"程序监控管理查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_prcMonCfg.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"程序监控管理查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"程序监控管理查询异常！");
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
		//程序名称
		String procename = request.getParameter("procename");
		//主机名称
		String hostname = request.getParameter("hostname");
		//程序编号
		String proceid = request.getParameter("proceid");
		conditionMap.put("procename", procename);
		conditionMap.put("hostname", hostname);
		conditionMap.put("proceid", proceid);
	}
	
	/**
	 * 跳转到新建程序信息页面
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
			LinkedHashMap<String, String> cond = new LinkedHashMap<String, String>();
			cond.put("hostusestatus", "0");
			List<LfMonShost> shostList = new BaseBiz().getByCondition(LfMonShost.class, cond, null);
			request.setAttribute("shostList", shostList);
			cond.clear();
			//uid字段在有些数据库中是关键字需要特殊处理
			String uid = (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE?"UID":"\"UID\"");
			cond.put("ptAccUid&in", "select "+uid+" from "+TableUserdata.TABLE_NAME+" where "+TableUserdata.ACCOUNTTYPE+"=1");
			List<AgwAccount> accountList = new BaseBiz().getByCondition(AgwAccount.class, cond, null);
			request.setAttribute("accountList", accountList);
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"跳转到新建程序信息页面异常！");
		}
		request.getRequestDispatcher(this.empRoot+base+"/mon_addPrc.jsp").forward(request,
				response);
	}
	
	/**
	 * 获得EMP网关相关信息
	 * @description    
	 * @param request
	 * @param response
	 * @throws Exception       			 
	 * @author zhangsan <zhangsan@126.com>
	 * @datetime 2016-5-6 下午03:11:39
	 */
	public void getgw(HttpServletRequest request, HttpServletResponse response) throws Exception{
		PrcMonCfgBiz prcBiz=new PrcMonCfgBiz();
		List<DynaBean> dhostList = prcBiz.getgw();
 		JSONArray members = new JSONArray(); 
 		if(dhostList!=null&&dhostList.size()>0){
 			for(int k=0;k<dhostList.size();k++){
 				DynaBean status=dhostList.get(k);
	            JSONObject member = new JSONObject();
	            member.put("gwno", status.get("gwno"));
	            members.add(member);
 			}
 		}
 		
 		response.getWriter().print(members);
	}
	
	/***
	 * 获得Spgate主与备用的，但是不包括彩信的
	 * @description    
	 * @param request
	 * @param response
	 * @throws Exception       			 
	 * @author zhangsan <zhangsan@126.com>
	 * @datetime 2016-5-6 下午03:33:30
	 */
	public void getShost(HttpServletRequest request, HttpServletResponse response) throws Exception{
		PrcMonCfgBiz prcBiz=new PrcMonCfgBiz();
		List<DynaBean> dhostList = prcBiz.getSPGate();
 		JSONArray members = new JSONArray(); 
 		if(dhostList!=null&&dhostList.size()>0){
 			for(int k=0;k<dhostList.size();k++){
 				DynaBean shost=dhostList.get(k);
	            JSONObject member = new JSONObject();
	            member.put("gwno", shost.get("gwno"));
	            members.add(member);
 			}
 		}
 		response.getWriter().print(members);
	}
	
	/**
	 * 检查程序类型是否已经存在
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @throws ServletException 
	 * @datetime 2013-11-27 上午11:11:42
	 */
	public void checkPrc(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{
		String result = "have";
		try
		{
			String procetype = request.getParameter("procetype");
			String servernum = request.getParameter("servernum");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			if("5000".equals(procetype) || "5800".equals(procetype))
            {
                //使用中
                conditionMap.put("proceusestatus","0");
                conditionMap.put("procetype",procetype);
                conditionMap.put("servernum",servernum);
                List<LfMonSproce> prcList = new BaseBiz().getByCondition(LfMonSproce.class, conditionMap, null);
                if(prcList!=null && prcList.size()>0)
                {
                    result = "have";
                }
                else
                {
                    result = "no";
                }
            }//不是网关spgate，判断程序类型的唯一
//            else if(!"5300".equals(procetype))
//			{
//				//使用中
//                conditionMap.clear();
//				conditionMap.put("proceusestatus","0");
//				conditionMap.put("procetype",procetype);
//				List<LfMonSproce> prcList = new BaseBiz().getByCondition(LfMonSproce.class, conditionMap, null);
//				if(prcList!=null && prcList.size()>0)
//				{
//					result = "have";
//				}
//				else
//				{
//					result = "no";
//				}
//			}
			//网关spgate判断所选择网关编号唯一
			else
			{
				String gatewayid = request.getParameter("gatewayid");
				conditionMap.clear();
				//使用中
				conditionMap.put("proceusestatus","0");
				conditionMap.put("procetype",procetype);
				conditionMap.put("gatewayid",gatewayid);
				List<LfMonSproce> prcList = new BaseBiz().getByCondition(LfMonSproce.class, conditionMap, null);
				if(prcList!=null && prcList.size()>0)
				{
					result = "haveGtId";
				}
				else
				{
					result = "no";
				}
			}
			
		}
		catch (Exception e)
		{
			result="error";
			EmpExecutionContext.error(e,"验证程序类型是否存在异常！");
		}
		finally
		{
			response.getWriter().print(result);
		}
	}
	

	/**
	 * 跳转到修改程序信息
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
			PrcMonCfgBiz prcBiz=new PrcMonCfgBiz();
			LinkedHashMap<String, String> cond = new LinkedHashMap<String, String>();
			cond.put("hostusestatus", "0");
			List<LfMonShost> shostList = new BaseBiz().getByCondition(LfMonShost.class, cond, null);
			request.setAttribute("shostList", shostList);
			String proceid = request.getParameter("proceid");
			if(proceid==null || "".equals(proceid))
			{
				EmpExecutionContext.error("根据程序id查询程序信息异常：proceid为空！");
				return;
			}
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			String lgcorpcode = request.getParameter("lgcorpcode");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("proceid", proceid);
			List<DynaBean> dhostList = prcBiz.getSprc(null, conditionMap);
			if(dhostList!=null && dhostList.size()>0)
			{
				DynaBean sprcBean = dhostList.get(0);
				request.setAttribute("sprcBean", sprcBean);
			}
			List<DynaBean> mqlist = prcBiz.getMqMsg();
			String mqurl="";
			String mqname="";
			if(mqlist!=null && mqlist.size()>0)
			{
				for(int i=0;i<mqlist.size();i++)
				{
					DynaBean bean = mqlist.get(i);
					if("MONITORMQURL".equals(bean.get("paramitem").toString()))
					{
						mqurl = bean.get("defaultvalue").toString();
					}
					else if("MONITORQUENAME".equals(bean.get("paramitem").toString()))
					{
						mqname = bean.get("defaultvalue").toString();
					}
				}
			}
			request.setAttribute("mqurl", mqurl);
			request.setAttribute("mqname", mqname);
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"转到修改程序信息页面异常！");
		}
		finally
		{
			request.getRequestDispatcher(this.empRoot+base+"/mon_editPrc.jsp").forward(request,
					response);
		}
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
	public void checkDEL(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		try
		{
			String ret="no";
			String proceid = request.getParameter("proceid");
			if(proceid!=null&&!"".equals(proceid)){
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("proceid",proceid);
                conditionMap.put("proceusestatus","0");
				List<LfMonSproce> sprcList = new BaseBiz().getByCondition(LfMonSproce.class, conditionMap, null);
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
	 * 添加程序信息
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @throws ServletException 
	 * @datetime 2013-11-27 下午02:19:28
	 */
	public void addPrc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	
		//操作模块
		String opModule = "系统监控";
		String opSper = StaticValue.OPSPER;
		String opType = StaticValue.ADD;
		String opContent = "新建程序";
		String opUser = "";
		String field = "[程序类型，程序名称，所属主机，监控状态，版本号，告警手机号，CPU占用比率告警阀值，物理内存使用量告警阀值，虚拟内存使用量告警阀值，磁盘剩余空间告警阀值]";
		boolean result = false;
		BaseBiz baseBiz = new BaseBiz();
		LfSysuser sysuser = null;
		Long prcId = null;
		try
		{
			sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(sysuser!=null)
			{
				opUser=sysuser.getUserName();
			}
			LinkedHashMap<String, String> cond = new LinkedHashMap<String, String>();
			cond.put("hostusestatus", "0");
			List<LfMonShost> shostList = new BaseBiz().getByCondition(LfMonShost.class, cond, null);
			request.setAttribute("shostList", shostList);
			List<AgwAccount> accountList = baseBiz.getEntityList(AgwAccount.class);
			request.setAttribute("accountList", accountList);
			LfMonSproce sprc = new LfMonSproce();
			//获取表单参数值
			this.getAddParValue(sprc,request);
			opContent="新建程序（程序名称："+sprc.getProcename()+"）";
			prcId = new BaseBiz().addObjReturnId(sprc);
			if(prcId!=null)
			{
				// 程序监控基础信息
//				sprc.setProceid(prcId);
//				MonitorStaticValue.proceBaseMap.put(prcId, sprc);
//				//初始化动态缓存告警标识
//				Map<Integer, Integer> monThresholdFlag = new HashMap<Integer, Integer>();
//				monThresholdFlag.put(1, 1);
//				monThresholdFlag.put(2, 0);
//				monThresholdFlag.put(3, 0);
//				monThresholdFlag.put(4, 0);
//				monThresholdFlag.put(5, 0);
//				monThresholdFlag.put(6, 0);
//				monThresholdFlag.put(100, 0);
//				MonDproceParams dproce = new PrcMonCfgBiz().copyBy(sprc);
//				dproce.setMonThresholdFlag(monThresholdFlag);
//				MonitorStaticValue.proceMap.put(prcId, dproce);
				result = true;
				spLog.logSuccessString(opUser, opModule, opType, opContent,sysuser==null?"":sysuser.getCorpCode());
			}
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null)
			{	
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String flag = result?"成功":"失败";
				String addvalueStr = this.getEditAfterValueStr(request, "add",null);
				String opContent2 = "新建"+flag+"。"+field+addvalueStr;
				EmpExecutionContext.info(OPMODULE, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent2, "add");
			}
		}
		catch (Exception e) {
			result = false;
			EmpExecutionContext.error(e, "新建程序信息servlet层异常！");
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, null,sysuser==null?"":sysuser.getCorpCode());
		}
		finally
		{
			request.setAttribute("result", result);
			request.setAttribute("prcId", prcId);
			request.getRequestDispatcher(this.empRoot+base+"/mon_addPrc.jsp").forward(request,
					response);
		}
		
	}
	
	/**
	 * 修改程序信息
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @throws ServletException 
	 * @datetime 2013-11-27 下午02:19:28
	 */
	public void editPrc(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	
		//操作模块
		String opModule = "系统监控";
		String opSper = StaticValue.OPSPER;
		String opType = StaticValue.UPDATE;
		String opContent = "修改程序";
		String opUser = "";
		String field = "[程序id，所属主机，监控状态，版本号，告警手机号，消息接收服务器地址，CPU占用比率告警阀值，物理内存使用量告警阀值，虚拟内存使用量告警阀值，磁盘剩余空间告警阀值]";
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
			String proceid = request.getParameter("proceid");
			String mqurl = request.getParameter("mqurl");
			request.setAttribute("mqurl", mqurl);
			LfMonSproce sprc = baseBiz.getById(LfMonSproce.class, proceid);
			//修改前字段
			String editBeforeValueStr = "(";
			//消息接受服务器地址不好获取暂时用空格
			editBeforeValueStr += sprc.getProceid()+"，"+sprc.getHostid()+"，"+sprc.getMonstatus()+"，"+sprc.getVersion()+"，"+sprc.getMonphone()+"，"+sprc.getMonemail()
								  +"， ，"+sprc.getCpubl()+"，"+sprc.getMemuse()+"，"+sprc.getVmemuse()+"，"+sprc.getHarddiskspace();
			editBeforeValueStr += ")";
			PrcMonCfgBiz prcMonCfgBiz = new PrcMonCfgBiz();
			//获取表单参数值
			this.getEditParValue(sprc,request);
			opContent = "修改程序（程序编号:"+sprc.getProceid()+"）";
			result = prcMonCfgBiz.editPrc(sprc, mqurl);
			LinkedHashMap<String, String> cond = new LinkedHashMap<String, String>();
			cond.put("hostusestatus", "0");
			List<LfMonShost> shostList = new BaseBiz().getByCondition(LfMonShost.class, cond, null);
			request.setAttribute("shostList", shostList);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("proceid", request.getParameter("proceid"));
			List<DynaBean> dhostList = prcMonCfgBiz.getSprc(null, conditionMap);
			if(dhostList!=null && dhostList.size()>0)
			{
				DynaBean sprcBean = dhostList.get(0);
				request.setAttribute("sprcBean", sprcBean);
			}
//			if(result)
//			{
//
//				//同步修改缓存
//				// 程序监控基础信息
//				MonitorStaticValue.proceBaseMap.put(Long.parseLong(proceid), sprc);
//				new PrcMonCfgBiz().updateBy(MonitorStaticValue.proceMap.get(Long.parseLong(proceid)),sprc);
				spLog.logSuccessString(opUser, opModule, opType, opContent,sysuser==null?"":sysuser.getCorpCode());
//			}
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null)
			{
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String flag = result?"成功":"失败";
				String editAfterValueStr = this.getEditAfterValueStr(request,"edit",sprc.getProceid());
				String opContent2 = "设置"+flag+"。"+field+editBeforeValueStr+"->"+editAfterValueStr;
				EmpExecutionContext.info(OPMODULE, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent2, "update");
			}
		}
		catch (Exception e) {
			EmpExecutionContext.error(e, "修改程序信息servlet层异常！");
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, null,sysuser==null?"":sysuser.getCorpCode());
		}
		finally
		{
			request.setAttribute("result", result);
			request.getRequestDispatcher(this.empRoot+base+"/mon_editPrc.jsp").forward(request,
					response);
		}
		
	}

	public String getEditAfterValueStr(HttpServletRequest request,String  operate,Long id)
	{
		String editAfterValueStr = "";
		//-------------------主机基本信息修改-------------------
		//主机id
		String hostid = request.getParameter("hostid");
		//程序名称
		String procename = request.getParameter("procename");
		//程序类型
		String procetype = request.getParameter("procetype");
		//监控状态
		String monstatus = request.getParameter("monstatus");
		//版本号
		String version = request.getParameter("version");
		//报警手机号
		String monphone = request.getParameter("monphone");
		//报警邮箱
		String monemail = request.getParameter("monemail");
		//网关编号
		String gatewayid = request.getParameter("gatewayid");
		//消息接收服务器地址
		String mqurl = request.getParameter("mqurl");
		//--------------告警阀值-------------------
		//CPU最大占用比率告警阀值，格式成XX%的字符串后显示
		String cpubl = request.getParameter("cpubl");
		//物理内存最大使用量告警阀值
		String memuse = request.getParameter("memuse");
		//虚拟内存最大使用量告警阀值
		String vmeuse = request.getParameter("vmemuse");
		//硬盘剩余量告警阀值
		String harddiskspace = request.getParameter("harddiskspace");
		//当前线程数告警阀值，程序类型为EMP，字段不为空
		//新增是程序类型为名称，设置时为ID
		if("edit".equals(operate))
		{
			editAfterValueStr += "("+id+"，"+hostid+"，"+monstatus+"，"+version+"，"+monphone+"，"+monemail+"，"+mqurl+"，"+cpubl+"，"+memuse+"，"+vmeuse+"，"+harddiskspace+")";
		}
		else if("add".equals(operate))
		{
			editAfterValueStr += "("+procetype+"，"+procename+"，"+hostid+"，"+monstatus+"，"+version+"，"+monphone+"，"+monemail+"，"+cpubl+"，"+memuse+"，"+vmeuse+"，"+harddiskspace+")";
		}	
		return editAfterValueStr;
	}
	
	
	/**
	 * 新建程序信息时获取表单参数值
	 * @description    
	 * @param sprc 程序基本配置信息
	 * @param request       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 下午02:29:18
	 */
	public void getAddParValue(LfMonSproce sprc,HttpServletRequest request)
	{
		//-------------------主机基本信息修改-------------------
		//主机id
		String hostid = request.getParameter("hostid");
		//程序名称
		String procename = request.getParameter("procename");
		//程序名称
		String procetype = request.getParameter("procetype");
		//监控状态
		String monstatus = request.getParameter("monstatus");
		//数据采集频率
		String monfreq = request.getParameter("monfreq");
		//版本号
		String version = request.getParameter("version");
		//报警手机号
		String monphone = request.getParameter("monphone");		
		//报警邮箱
		String monemail = request.getParameter("monemail");
		//网关编号
		String gatewayid = request.getParameter("gatewayid");
        //服务器节点
		String servernum = request.getParameter("servernum");
		
		//主机id
		sprc.setHostid(hostid!=null&&!"".equals(hostid)?Long.parseLong(hostid):-1L);
		sprc.setCreatetime(new Timestamp(System.currentTimeMillis()));
		sprc.setModifytime(new Timestamp(System.currentTimeMillis()));
		//程序名称
		sprc.setProcename(procename);
		//程序版本
		sprc.setVersion(version);
		//数据采集频率
		sprc.setMonfreq(Integer.parseInt(monfreq!=null?monfreq:"30"));
		//监控状态
		sprc.setMonstatus(Integer.parseInt(monstatus));
		//程序类型
		sprc.setProcetype(Integer.parseInt(procetype));
		sprc.setMonphone(monphone);
		sprc.setMonemail(StringUtils.defaultIfEmpty(monemail, " "));
//		//wbs网关编号默认99
//		if("5200".equals(procetype))
//		{
//			gatewayid ="99";
//		}else if(("5000".equals(procetype) || "5800".equals(procetype)) && StaticValue.ISCLUSTER == 1)
//        {
//            sprc.setServernum(servernum);
//        }
		sprc.setServernum(servernum != null && !"".equals(servernum)?servernum:" ");
		sprc.setGatewayid(gatewayid!=null&&!"".equals(gatewayid)?Long.parseLong(gatewayid):0L);
		//备注
		sprc.setDescr(" ");
		//--------------告警阀值-------------------
		//CPU最大占用比率告警阀值，格式成XX%的字符串后显示
		String cpubl = request.getParameter("cpubl");
		//物理内存最大使用量告警阀值
		String memuse = request.getParameter("memuse");
		//虚拟内存最大使用量告警阀值
		String vmeuse = request.getParameter("vmemuse");
		//硬盘剩余量告警阀值
		String harddiskspace = request.getParameter("harddiskspace");
		//当前线程数告警阀值，程序类型为EMP，字段不为空
		String curthreaduse = request.getParameter("curthreaduse");
		//当前会话数告警阀值，程序类型为EMP，字段不为空
		String curseesionuse = request.getParameter("curseesionuse");
		//当前数据库连接数告警阀值，程序类型为EMP，字段不为空
		String curconuse = request.getParameter("curconuse");
		//堆内存使用率告警阀值，程序类型为EMP，字段不为空，百分比格式
		String heapuse = request.getParameter("heapuse");
		//非堆内存使用率告警阀值，程序类型为EMP，字段不为空，百分比格式
		String nonheapuse = request.getParameter("nonheapuse");
		//数据库存储空间使用率告警阀值，程序类型为数据库，字段不为空，百分比格式
		String dbspaceuse = request.getParameter("dbspaceuse");
		//数据库存储空间使用率告警阀值，程序类型为数据库，字段不为空，百分比格式
		String dbconnum = request.getParameter("dbconnum");
		//数据库网络连接监控，0：开启;1：关闭
		String isdbconnect=request.getParameter("isdbconnect");

		sprc.setCpubl(cpubl!=null&&!"".equals(cpubl)?Integer.parseInt(cpubl):0);
		sprc.setMemuse(memuse!=null&&!"".equals(memuse)?Integer.parseInt(memuse):0);
		sprc.setVmemuse(vmeuse!=null&&!"".equals(vmeuse)?Integer.parseInt(vmeuse):0);
		sprc.setHarddiskspace(harddiskspace!=null&&!"".equals(harddiskspace)?Integer.parseInt(harddiskspace):0);
		sprc.setCurthreaduse(curthreaduse!=null&&!"".equals(curthreaduse)?Integer.parseInt(curthreaduse):0);
		sprc.setCurseesionuse(curseesionuse!=null&&!"".equals(curseesionuse)?Integer.parseInt(curseesionuse):0);
		sprc.setCurconuse(curconuse!=null&&!"".equals(curconuse)?Integer.parseInt(curconuse):0);
		sprc.setHeapuse(heapuse!=null&&!"".equals(heapuse)?Integer.parseInt(heapuse):0);
		sprc.setNonheapuse(nonheapuse!=null&&!"".equals(nonheapuse)?Integer.parseInt(nonheapuse):0);
		sprc.setIsDbconnect(isdbconnect!=null&&!"".equals(isdbconnect)?Integer.parseInt(isdbconnect):0);
	}
	
	/**
	 * 修改程序信息时获取表单参数值
	 * @description    
	 * @param sprc 程序基本信息
	 * @param request       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 下午02:29:18
	 */
	public void getEditParValue(LfMonSproce sprc,HttpServletRequest request)
	{
		//-------------------主机基本信息修改-------------------
		//-------------------主机基本信息修改-------------------
		//主机id
		String hostid = request.getParameter("hostid");
		//程序名称
		String procename = request.getParameter("procename");
		//程序名称
//		String procetype = request.getParameter("procetype");
		//监控状态
		String monstatus = request.getParameter("monstatus");
		//数据采集频率
		String monfreq = request.getParameter("monfreq");
		//版本号
		String version = request.getParameter("version");
		//报警手机号
		String monphone = request.getParameter("monphone");
		//报警邮箱
		String monemail = request.getParameter("monemail");
		//数据库网络连接监控，0：开启;1：关闭
		String isdbconnect=request.getParameter("isdbconnect");
		//主机id
		sprc.setHostid(Long.parseLong(hostid));
		sprc.setCreatetime(new Timestamp(System.currentTimeMillis()));
		sprc.setModifytime(new Timestamp(System.currentTimeMillis()));
		//程序名称
		sprc.setProcename(procename);
		//程序版本
		sprc.setVersion(version);
		//数据采集频率
//		sprc.setMonfreq(Integer.parseInt(monfreq!=null?monfreq:"30"));
		//监控状态
		sprc.setMonstatus(Integer.parseInt(monstatus));
		//程序类型
//		sprc.setProcetype(Integer.parseInt(procetype));
		sprc.setMonphone(monphone);
		sprc.setMonemail(StringUtils.defaultIfEmpty(monemail, " "));
		//备注
		sprc.setDescr(" ");
		//--------------告警阀值-------------------
		//CPU最大占用比率告警阀值，格式成XX%的字符串后显示
		String cpubl = StringUtils.defaultIfEmpty(request.getParameter("cpubl"), "0");
		//物理内存最大使用量告警阀值
		String memuse = StringUtils.defaultIfEmpty(request.getParameter("memuse"), "0");
		//虚拟内存最大使用量告警阀值
		String vmeuse = StringUtils.defaultIfEmpty(request.getParameter("vmemuse"), "0");
		//硬盘剩余量告警阀值
		String harddiskspace = StringUtils.defaultIfEmpty(request.getParameter("harddiskspace"), "0");
		//当前线程数告警阀值，程序类型为EMP，字段不为空
//		String curthreaduse = request.getParameter("curthreaduse");
		//当前会话数告警阀值，程序类型为EMP，字段不为空
//		String curseesionuse = request.getParameter("curseesionuse");
		//当前数据库连接数告警阀值，程序类型为EMP，字段不为空
//		String curconuse = request.getParameter("curconuse");
		//堆内存使用率告警阀值，程序类型为EMP，字段不为空，百分比格式
//		String heapuse = request.getParameter("heapuse");
		//非堆内存使用率告警阀值，程序类型为EMP，字段不为空，百分比格式
//		String nonheapuse = request.getParameter("nonheapuse");
		//数据库存储空间使用率告警阀值，程序类型为数据库，字段不为空，百分比格式
//		String dbspaceuse = request.getParameter("dbspaceuse");
		//数据库存储空间使用率告警阀值，程序类型为数据库，字段不为空，百分比格式
//		String dbconnum = request.getParameter("dbconnum");
		sprc.setIsDbconnect(isdbconnect!=null&&!"".equals(isdbconnect)?Integer.parseInt(isdbconnect):0);
		sprc.setCpubl(Integer.parseInt(cpubl));
		sprc.setMemuse(Integer.parseInt(memuse));
		sprc.setVmemuse(Integer.parseInt(vmeuse));
		sprc.setHarddiskspace(Integer.parseInt(harddiskspace));
	}
	
	public void delete (HttpServletRequest request,HttpServletResponse response) throws IOException{

		//操作模块
		String opModule = "系统监控";
		String opSper = StaticValue.OPSPER;
		String opType = StaticValue.DELETE;
		String opContent = "删除程序";
		String opUser = "";
		String json = "error";
		PrintWriter out = null;
		String id = request.getParameter("id");
		opContent="删除程序（程序编号："+id+"）";
		LfSysuser sysuser = null;
		response.setContentType("text/html");
		out = response.getWriter();
		try {
			sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(sysuser!=null)
			{
				opUser=sysuser.getUserName();
			}
			if(id==null||"".equals(id)){
				json = "error";
				return;
			}
			Long proceid = Long.parseLong(id);
			LfMonSproce sproce = baseBiz.getById(LfMonSproce.class, proceid);
			//获取删除前信息
			String valueStr = "(";
			if(sproce!=null)
			{
				valueStr += sproce.getProceid()+"，"+sproce.getProcename()+"，"+sproce.getMonstatus()+"，"+sproce.getVersion()+"，"+sproce.getHostid();
			}
			valueStr+=")";
			if(prcCfg.delete(proceid)){
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
				String filed = "[程序编号，程序名称，监控状态，版本号，主机编号]";
				String opContent2 = "删除程序监控"+flag+"。"+filed+valueStr;
				EmpExecutionContext.info(OPMODULE, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent2, "delete");
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "删除程序异常！");
			json = "error";
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, null,sysuser==null?"":sysuser.getCorpCode());
		}finally{
			out.print(json);
		}
	}
}
