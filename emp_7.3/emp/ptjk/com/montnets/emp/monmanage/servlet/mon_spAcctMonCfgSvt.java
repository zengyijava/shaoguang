package com.montnets.emp.monmanage.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;


import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.monitor.LfMonSspacinfo;
import com.montnets.emp.entity.monitor.LfSpofflineprd;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.monitor.biz.AccoutBaseInfoBiz;
import com.montnets.emp.monmanage.biz.GateAcctMonCfgBiz;
import com.montnets.emp.monmanage.biz.SpAcctMonCfgBiz;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
/**
 * sp账号监控管理servlet
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-03 上午10:34:41
 */
@SuppressWarnings("serial")
public class mon_spAcctMonCfgSvt extends BaseServlet {

	final String empRoot="ptjk";
	final String base="/monmanage";
	protected final SuperOpLog spLog = new SuperOpLog();
	private final BaseBiz baseBiz = new BaseBiz();
	public static final String OPMODULE = "SP账号监控设置";
	/**
	 * sp账号监控管理查询
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @datetime 2014-1-2 下午04:59:57
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//查询开始时间
		long stratTime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		try {
			// 设置SP账号和通道账号信息
			new AccoutBaseInfoBiz().getSpAndGateInfo();
			List<DynaBean> monitorList = null;
			List<DynaBean> spList = new ArrayList<DynaBean>();
			pageSet(pageInfo,request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			setConditionMap(request,conditionMap);
			SpAcctMonCfgBiz hostMonCfgBiz = new SpAcctMonCfgBiz();
			monitorList = hostMonCfgBiz.getspAcctMonCfg(pageInfo, conditionMap);
			spList = hostMonCfgBiz.getspAcctMonCfg(null, null);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("monitorList", monitorList);
			request.setAttribute("spList", spList);
			
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
			
			EmpExecutionContext.info("sp账号监控设置", corpCode, userId, userName, opContent, "GET");
			
			request.getRequestDispatcher(this.empRoot+base+"/mon_spAcctMonCfg.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"sp账号监控管理查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_spAcctMonCfg.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"sp账号监控管理查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"sp账号监控管理查询异常！");
			}
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
		//用户账号传入类型   0：下拉框  1 文本输入
		String spaccountType = request.getParameter("spaccountType");
		//用户账号 0则进行全匹配  1 模糊匹配
		String spaccountid = request.getParameter("spaccountid");
		//账号名称
		String accountname = request.getParameter("accountname");
		
		//登录类型
		String logintype=request.getParameter("logintype");
		conditionMap.put("logintype", logintype);
		
		conditionMap.put("accountname", accountname);
		conditionMap.put("monstatus", monstatus);
		conditionMap.put("spaccountid", spaccountid);
		conditionMap.put("spaccountType", spaccountType);
		
	}
	

	/**
	 * 跳转到sp账号设置阀值页面
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
		String result="error";
		try
		{
			String spaccountid = request.getParameter("spaccountid");
			if(spaccountid==null || "".equals(spaccountid))
			{
				EmpExecutionContext.error("根据spaccountid查询sp账号监控阀值信息异常：spaccountid为空！");
				return;
			}
			//查询未被选择过的程序
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			String lgcorpcode = request.getParameter("lgcorpcode");
			conditionMap.put("spaccountid", spaccountid);
			List<DynaBean> dhostList = new SpAcctMonCfgBiz().getspAcctMonCfg(null, conditionMap);
			//查询时间段
			conditionMap.clear();
			//设置SP账号查询条件
			conditionMap.put("spaccountid", spaccountid);
			//设置企业编码查询条件
			conditionMap.put("corpcode", lgcorpcode);
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("id", StaticValue.ASC);
			//查询监控时间段
			List<LfSpofflineprd> lfSpofflineprds=baseBiz.getByCondition(LfSpofflineprd.class, conditionMap, orderbyMap);
			if(dhostList!=null && dhostList.size()>0)
			{
				DynaBean dhostBean = dhostList.get(0);
				if(dhostBean.get("monphone")==null){
					dhostBean.set("monphone", "");
				};
				if(dhostBean.get("monemail")==null){
					dhostBean.set("monemail", "");
				};
				//初始化数组
				//定义开始时间数组
				String[] selectfromArr=new String[4];
				//定义结束时间数组
				String[] selecttoArr=new String[4];
				//定义时长数组
				String[] durationArr=new String[4];
				//初始化开始时间数组
				for (int i = 0; i < selectfromArr.length; i++)
				{
					selectfromArr[i]="0";
				}
				//初始化结束时间数组
				for (int i = 0; i < selecttoArr.length; i++)
				{
					selecttoArr[i]="0";
				}
				//初始化时长数组
				for (int i = 0; i < durationArr.length; i++)
				{
					durationArr[i]="0";
				}
				//将设置的时间段放置到数组中
				if(lfSpofflineprds!=null&&lfSpofflineprds.size()>0)
				{
					LfSpofflineprd lfSpofflineprd=null;
					for (int i = 0; i < lfSpofflineprds.size(); i++)
					{
						lfSpofflineprd=lfSpofflineprds.get(i);
						//设置开始时间
						selectfromArr[i]=String.valueOf(lfSpofflineprd.getBeginhour());
						//设置结束时间
						selecttoArr[i]=String.valueOf(lfSpofflineprd.getEndhour());
						//设置时长
						durationArr[i]=String.valueOf(lfSpofflineprd.getDuration());
					}
				}
				result = dhostBean.get("rptrecvratio") + "|" + dhostBean.get("morecvratio")+"|"
						+dhostBean.get("mtremained") + "|" + dhostBean.get("moremained")+"|"
						+dhostBean.get("rptremained") + "|" + dhostBean.get("monstatus")+"|"
						+dhostBean.get("monphone")+"|"+ dhostBean.get("offlinethreshd")+"|"
						+selectfromArr[0]+"|"+selecttoArr[0]+"|"+durationArr[0]+"|"
						+selectfromArr[1]+"|"+selecttoArr[1]+"|"+durationArr[1]+"|"
						+selectfromArr[2]+"|"+selecttoArr[2]+"|"+durationArr[2]+"|"
						+selectfromArr[3]+"|"+selecttoArr[3]+"|"+durationArr[3]+"|"
						+dhostBean.get("monemail").toString().trim();
			}
		}
		catch (Exception e) {
			result="error";
			EmpExecutionContext.error(e,"跳转sp账号监控设置页面异常！");
		}
		finally
		{
			response.getWriter().print(result);
		}
	}
	
	/**
	 * 跳转到sp账号设置阀值页面
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @throws ServletException 
	 * @datetime 2013-11-27 上午11:11:42
	 */
	public void edit(HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{

		//操作模块
		String opModule = "系统监控";
		String opSper = StaticValue.OPSPER;
		String opType = StaticValue.UPDATE;
		String opContent = "SP账号监控设置";
		String opUser = "";
		String field = "[SP账号，MT待发告警阀值，MO滞留告警阀值，MO最低接收率，RPT滞留告警阀值，RPT最低接收率，告警手机号，监控状态]";
		String result="error";
		LfSysuser sysuser = null;
		try
		{
			sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(sysuser!=null)
			{
				opUser=sysuser.getUserName();
			}
			String spaccountid = request.getParameter("spaccountid");
			if(spaccountid==null || "".equals(spaccountid))
			{
				EmpExecutionContext.error("根据spaccountid查询sp账号监控阀值信息异常：spaccountid为空！");
				return;
			}
			//获取编辑前的字段
			//根据sp账号号查询
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			map.put("spaccountid", spaccountid);
			List<LfMonSspacinfo> list = baseBiz.getByCondition(LfMonSspacinfo.class, map, null);
			LfMonSspacinfo lfMonSspacinfo = new LfMonSspacinfo();
			//修改前字段数据
			String editBefore = "(";
			if(list!=null&&list.size()>0)
			{
				lfMonSspacinfo = list.get(0);
				editBefore += lfMonSspacinfo.getSpaccountid()+"，";
				editBefore += lfMonSspacinfo.getMtremained()+"，";
				editBefore += lfMonSspacinfo.getMoremained()+"，";
				editBefore += lfMonSspacinfo.getMorecvratio()+"，";
				editBefore += lfMonSspacinfo.getRptremained()+"，";
				editBefore += lfMonSspacinfo.getRptrecvratio()+"，";
				editBefore += lfMonSspacinfo.getMonphone()+"，";
				editBefore += lfMonSspacinfo.getMonemail()+"，";
				editBefore += lfMonSspacinfo.getMonstatus()+")";
			}
			opContent = "SP账号监控设置（SP账号："+spaccountid+"）";
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			String lgcorpcode = request.getParameter("lgcorpcode");
			//查询未被选择过的程序
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			setUpdatePra(request,objectMap);
			if(objectMap.get("monphone")!=null&&objectMap.get("monphone").length()>0)
			{
				String res = new GateAcctMonCfgBiz().checkPhone(objectMap.get("monphone"));
				if(!"success".equals(res))
				{
					result = res;
					return ;
				}
			}
			//验证邮箱账号
			String email = objectMap.get("monemail").trim();
			if(email!=null&&email.length()>0)
			{
				String res = new GateAcctMonCfgBiz().checkEmail(email);
				if(!"success".equals(res))
				{
					result = res;
					return ;
				}
			}
			List<LfSpofflineprd> lfSpofflineprds=getLfSpofflineprds(request, lgcorpcode, lguserid, spaccountid);
			if(new SpAcctMonCfgBiz().editSpAcctNew(objectMap, spaccountid,lfSpofflineprds,lgcorpcode)){
				result="success";
				spLog.logSuccessString(opUser, opModule, opType, opContent,sysuser.getCorpCode());
			}
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null)
			{
				String rptrecvratio = request.getParameter("rptrecvratio");
				String morecvratio = request.getParameter("morecvratio");
				String mtremained = request.getParameter("mtremained");
				String moremained = request.getParameter("moremained");
				String rptremained = request.getParameter("rptremained");
				String monstatus = request.getParameter("monstatus");
				String monphone = request.getParameter("monphone");
				String monemail = request.getParameter("monemail");
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String flag = "success".equals(result)?"成功":"失败";
				//编辑后字段
				String editAfter = "(";
				editAfter += spaccountid + "，"+ mtremained + "，" + moremained + "，" + morecvratio  + "，" + rptremained
							+ "，" + rptrecvratio + "，" + monphone  + "，" +monemail  + "，" +monstatus + ")";
				String opContent2 = "设置" + flag + "。" + field + editBefore +"->" + editAfter;
				//记录日志
				EmpExecutionContext.info(OPMODULE, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent2, "update");
			}
		}
		catch (Exception e) {
			result="error";
			EmpExecutionContext.error(e,"跳转sp账号监控设置页面异常！");
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, null,sysuser.getCorpCode());
		}
		finally
		{
			response.getWriter().print(result);
		}
	}
	
	/**
	 * 设置阀值参数
	 * @description    
	 * @param request
	 * @param objectMap
	 * @author zhangmin
	 * @datetime 2014-1-2 下午04:59:49
	 */
	public void setUpdatePra(HttpServletRequest request,LinkedHashMap<String, String> objectMap )
	{
		String rptrecvratio = request.getParameter("rptrecvratio");
		String morecvratio = request.getParameter("morecvratio");
		String mtremained = request.getParameter("mtremained");
		String moremained = request.getParameter("moremained");
		String rptremained = request.getParameter("rptremained");
		String monstatus = request.getParameter("monstatus");
		String monphone = request.getParameter("monphone");
		String monemail = request.getParameter("monemail").trim();
		//账号离线阀值
		String offlinethreshd=request.getParameter("offlinethreshd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		objectMap.put("rptrecvratio", StringUtils.defaultIfEmpty(rptrecvratio, "0"));
		objectMap.put("morecvratio", StringUtils.defaultIfEmpty(morecvratio,"0"));
		objectMap.put("mtremained", StringUtils.defaultIfEmpty(mtremained,"0"));
		objectMap.put("moremained", StringUtils.defaultIfEmpty(moremained,"0"));
		objectMap.put("rptremained", StringUtils.defaultIfEmpty(rptremained,"0"));
		objectMap.put("monstatus", monstatus);
		objectMap.put("monphone", StringUtils.defaultIfEmpty(monphone,""));
		objectMap.put("monemail", StringUtils.defaultIfEmpty(monemail," "));
		//数据库兼容
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
		{
			objectMap.put("modifytime", "to_date('"+sdf.format(new Date())+"','yyyy-MM-dd HH24:mi:ss')");
		}else
		{
			objectMap.put("modifytime", "'"+sdf.format(new Date())+"'");
		}
		//设置账号离线阀值
		objectMap.put("offlineThreshd", StringUtils.defaultIfEmpty(offlinethreshd,"0"));
	}
	
	/**
	 * 获取时间段
	 * @description    
	 * @param request
	 * @return       			 
	 * @author tanglili <jack860127@126.com>
	 * @datetime 2016-1-27 下午08:28:14
	 */
	public List<LfSpofflineprd> getLfSpofflineprds(HttpServletRequest request,String corpCode,String lguserid,String spaccountid)
	{
		List<LfSpofflineprd> lfSpofflineprds=new ArrayList<LfSpofflineprd>();
		try{
			 for(int i=1;i<=4;i++)
			 {
				 //时间段(开始)
				 String from=request.getParameter("selectfrom"+i);
				 //时间段(结束)
				 String to=request.getParameter("selectto"+i);
				 if(from!=null&&!"".equals(from)&&to!=null&&!"".equals(to))
				 {
					 //时长
					 String duration=request.getParameter("duration"+i);
					 LfSpofflineprd lfSpofflineprd=new LfSpofflineprd();
					 lfSpofflineprd.setBeginhour(Integer.parseInt(from));
					 lfSpofflineprd.setEndhour(Integer.parseInt(to));
					 lfSpofflineprd.setDuration(Integer.parseInt(duration));
					 lfSpofflineprd.setCorpcode(corpCode);
					 lfSpofflineprd.setUserid(Long.parseLong(lguserid));
					 lfSpofflineprd.setSpaccountid(spaccountid);
					 lfSpofflineprds.add(lfSpofflineprd);
				 }
			 }
		}catch (Exception e) {
			lfSpofflineprds=null;
			EmpExecutionContext.error(e, "获取时间段异常！");
		}
		 return lfSpofflineprds;
	}
}
