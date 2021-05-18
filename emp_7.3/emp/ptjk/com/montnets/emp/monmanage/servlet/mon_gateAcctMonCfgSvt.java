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


import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.monitor.LfMonSgtacinfo;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.monitor.biz.AccoutBaseInfoBiz;
import com.montnets.emp.monmanage.biz.GateAcctMonCfgBiz;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.SuperOpLog;
/**
 * 通道账号监控管理servlet
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-03 上午10:34:41
 */
@SuppressWarnings("serial")
public class mon_gateAcctMonCfgSvt extends BaseServlet {

	final String empRoot="ptjk";
	final String base="/monmanage";
	protected final SuperOpLog spLog = new SuperOpLog();
	private final BaseBiz baseBiz = new BaseBiz();
	public static final String OPMODULE = "通道账号监控设置";
	
	/**
	 * 通道账号监控管理查询
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
			//查询所有通道账号列表
			List<DynaBean> gateList = new ArrayList<DynaBean>();
			pageSet(pageInfo,request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			setConditionMap(request,conditionMap);
			GateAcctMonCfgBiz geteMonCfgBiz = new GateAcctMonCfgBiz();
			monitorList = geteMonCfgBiz.getGateAcctMonCfg(pageInfo, conditionMap);
			gateList = geteMonCfgBiz.getGateAcctMonCfg(null, null);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("monitorList", monitorList);
			request.setAttribute("gateList", gateList);
			
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
			String opContent = "开始时间："+sdf.format(stratTime)+ " 耗时：" 
			+(System.currentTimeMillis()-stratTime) + " 数量："+totalCount;
			
			EmpExecutionContext.info("通道账号监控设置", corpCode, userId, userName, opContent, "GET");
			
			request.getRequestDispatcher(this.empRoot+base+"/mon_gateAcctMonCfg.jsp").forward(request,
					response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"通道账号监控管理查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot+base+"/mon_gateAcctMonCfg.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"通道账号监控管理查询异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"通道账号监控管理查询！");
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
		String gateaccountType = request.getParameter("gateaccountType");
		//用户账号 0则进行全匹配  1 模糊匹配
		String gateaccount = request.getParameter("gateaccount");
		//账号名称
		String gatename = request.getParameter("gatename");
		
		
		conditionMap.put("gatename", gatename);
		conditionMap.put("monstatus", monstatus);
		conditionMap.put("gateaccount", gateaccount);
		conditionMap.put("gateaccountType", gateaccountType);
		
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
			String gateaccount = request.getParameter("gateaccount");
			if(gateaccount==null || "".equals(gateaccount))
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
			conditionMap.put("gateaccount", gateaccount);
			List<DynaBean> dhostList = new GateAcctMonCfgBiz().getGateAcctMonCfg(null, conditionMap);
			if(dhostList!=null && dhostList.size()>0)
			{
				DynaBean dhostBean = dhostList.get(0);
				if(dhostBean.get("monphone")==null){
					dhostBean.set("monphone", "");
				};
				if(dhostBean.get("monemail")==null){
					dhostBean.set("monemail", "");
				};
				result = dhostBean.get("rptsndratio") + "|" + dhostBean.get("mosndratio")+"|"
						+dhostBean.get("mtremained") + "|" + dhostBean.get("moremained")+"|"
						+dhostBean.get("rptremained") + "|"+dhostBean.get("linknum")+"|"
						+dhostBean.get("userfee")+"|"+dhostBean.get("monphone")+"|"
						+dhostBean.get("monstatus")+"|"+dhostBean.get("isarrearage")+"|"
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
	 * 设置阀值
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
		String opContent = "通道账号监控设置";
		String opUser = "";
		//字段
		String field = "[通道账号,MT待发告警阀值，MO滞留告警阀值，MO最低转发率，RPT滞留告警阀值，RPT最低转发率，余额告警阀值，告警手机号，监控状态，欠费告警]";
		String result="error";
		LfSysuser sysuser = null;
		try
		{
			sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(sysuser!=null)
			{
				opUser=sysuser.getUserName();
			}
			String gateaccount = request.getParameter("gateaccount");
			if(gateaccount==null || "".equals(gateaccount))
			{
				EmpExecutionContext.error("根据gateaccount查询sp账号监控阀值信息异常：spaccountid为空！");
				return;
			}
			//获取编辑前的字段
			//根据通道号查询
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			map.put("gateaccount", gateaccount);
			List<LfMonSgtacinfo> list = baseBiz.getByCondition(LfMonSgtacinfo.class, map, null);
			LfMonSgtacinfo lfMonSgtacinfo = new LfMonSgtacinfo();
			//修改前字段数据
			String editBefore = "(";
			if(list!=null&&list.size()>0)
			{
				lfMonSgtacinfo = list.get(0);
				editBefore += lfMonSgtacinfo.getGateaccount()+"，";
				editBefore += lfMonSgtacinfo.getMtremained()+"，";
				editBefore += lfMonSgtacinfo.getMoremained()+"，";
				editBefore += lfMonSgtacinfo.getMosndratio()+"，";
				editBefore += lfMonSgtacinfo.getRptremained()+"，";
				editBefore += lfMonSgtacinfo.getRptsndratio()+"，";
				editBefore += lfMonSgtacinfo.getUserfee()+"，";
				editBefore += lfMonSgtacinfo.getMonphone()+"，";
				editBefore += lfMonSgtacinfo.getMonstatus()+"，";
				editBefore += lfMonSgtacinfo.getIsarrearage()+")";
				editBefore += lfMonSgtacinfo.getMonemail()+")";
			}
			
			opContent = "通道账号监控设置（通道账号："+gateaccount+"）";
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			//查询未被选择过的程序
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			String lgcorpcode = request.getParameter("lgcorpcode");
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
			
			if(new GateAcctMonCfgBiz().editGateAcct(objectMap, gateaccount)){
				result="success";
				spLog.logSuccessString(opUser, opModule, opType, opContent,sysuser.getCorpCode());
			}
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null)
			{
				String monphone = request.getParameter("monphone");
				String userfee = request.getParameter("userfee");
				String mtremained = request.getParameter("mtremained");
				String moremained = request.getParameter("moremained");
				String rptremained = request.getParameter("rptremained");
				String monstatus = request.getParameter("monstatus");
				String mosndratio = request.getParameter("mosndratio");
				String rptsndratio = request.getParameter("rptsndratio");
				String isarrearage = request.getParameter("isarrearage");
				String monemail = request.getParameter("monemail");
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String flag = "success".equals(result)?"成功":"失败";
				//编辑后字段
				String editAfter = "(";
				editAfter += gateaccount + "，"+ mtremained + "，" + moremained + "，" + mosndratio  + "，" + rptremained
							 + "，" + rptsndratio + "，" + userfee + "，" + monphone + "，" + monstatus + "，" +isarrearage +"，" +monemail + ")";
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
		
		
		String monphone = request.getParameter("monphone");
		String userfee = request.getParameter("userfee");
		String mtremained = request.getParameter("mtremained");
		String moremained = request.getParameter("moremained");
		String rptremained = request.getParameter("rptremained");
		String monstatus = request.getParameter("monstatus");
		String linknum = request.getParameter("linknum");
		String mosndratio = request.getParameter("mosndratio");
		String rptsndratio = request.getParameter("rptsndratio");
		String isarrearage = request.getParameter("isarrearage");
		String monemail = request.getParameter("monemail").trim();
		objectMap.put("monphone", monphone);
		objectMap.put("userfee", StringUtils.defaultIfEmpty(userfee,"0"));
		objectMap.put("mtremained", StringUtils.defaultIfEmpty(mtremained,"0"));
		objectMap.put("moremained", StringUtils.defaultIfEmpty(moremained,"0"));
		objectMap.put("rptremained", StringUtils.defaultIfEmpty(rptremained,"0"));
		objectMap.put("linknum", StringUtils.defaultIfEmpty(linknum,"0"));
		objectMap.put("isarrearage", isarrearage);
		objectMap.put("monstatus", monstatus);
		objectMap.put("mosndratio", StringUtils.defaultIfEmpty(mosndratio,"0"));
		objectMap.put("rptsndratio", StringUtils.defaultIfEmpty(rptsndratio,"0"));
		objectMap.put("monemail", StringUtils.defaultIfEmpty(monemail," "));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		objectMap.put("modifytime", sdf.format(new Date()));
	}
	
	/**
	 * 检查告警手机号码是否合法
	 * @description
	 * @return       			 
	 * @author zhangmin
	 * @throws IOException 
	 * @datetime 2014-3-31 下午05:13:07
	 */
	public void checkPhone(HttpServletRequest request, HttpServletResponse response ) throws IOException
	{
		String result="error";
		try
		{
			String monphone = request.getParameter("monphone");
			if(monphone==null||monphone.length()==0)
			{
				result = "success";
				return;
			}
			String [] phone = monphone.split(",");
			if(phone.length==0)
			{
				result = "phoneError";
				return;
			}
			//获取运营商号码段
			String[] haoduan = new WgMsgConfigBiz().getHaoduan();
			PhoneUtil phoneUtil = new PhoneUtil();
			for(int i=0;i<phone.length;i++)
			{
				if(phoneUtil.getPhoneType(phone[i], haoduan)==-1)
				{
					result = "phoneError";
					return;
				}
			}
			result="success";
		}
		catch (Exception e) {
			result="error";
			EmpExecutionContext.error(e,"验证告警手机号码异常！");
		}
		finally
		{
			response.getWriter().write(result) ;
		}
	}
	
}
