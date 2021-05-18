package com.montnets.emp.apimanage.servlet;

import com.montnets.emp.apimanage.biz.PushRsProtocolBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
//import com.montnets.emp.ottbase.constant.StaticValue;
import com.montnets.emp.servmodule.txgl.entity.GwBaseprotocol;
import com.montnets.emp.servmodule.txgl.entity.GwMultiEnterp;
import com.montnets.emp.servmodule.txgl.entity.GwPushRsProtocol;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * API推送用户回应管理
 * @author Administrator
 *
 */
public class wg_pushRsProtocolSvt extends BaseServlet{
	
	private final BaseBiz baseBiz = new BaseBiz();
	
	private final String empRoot = "txgl";
	
	private final String basePath = "/apimanage";
	
	/**
	 * API推送用户回应管理查询
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) 
	{
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter;
		String corpInfo="";
		try {
			isFirstEnter = pageSet(pageInfo, request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//回应格式
			String crspfmt =request.getParameter("crspfmt");
				
			//回应状态
			String rspStatus =request.getParameter("rspStatus");
				
			//返回命令
			String rspCmd =request.getParameter("rspCmd");
				
			//客户参数名
			String cargName =request.getParameter("cargName");
				
			//客户字段值
			String cargValue =request.getParameter("cargValue");
			
			//企业名称  只有托管版有
			String corpName="";
				
			//设置查询条件
			//回应格式  查询条件
			if(crspfmt!=null && !"".equals(crspfmt.trim()))
			{
				conditionMap.put("crspfmt", crspfmt.trim());

			}
			//回应状态 查询条件
			if(rspStatus!=null && !"".equals(rspStatus))
			{
				conditionMap.put("rspStatus", rspStatus);
			}
			//返回命令 查询条件	
			if(rspCmd!=null && !"".equals(rspCmd))
			{
				conditionMap.put("rspCmd", rspCmd);
			}
			
			if(StaticValue.getCORPTYPE() == 0){
			
			//客户参数名 查询条件
			if(cargName!=null && !"".equals(cargName))
			{
				conditionMap.put("cargName&like", cargName);
			}
			////客户字段值  查询条件
			if(cargValue!=null && !"".equals(cargValue))
			{
				conditionMap.put("cargValue&like", cargValue);
			}
			
			}else
			{
				//客户参数名 查询条件
				if(cargName!=null && !"".equals(cargName))
				{
					conditionMap.put("cargName", cargName);
				}
				////客户字段值  查询条件
				if(cargValue!=null && !"".equals(cargValue))
				{
					conditionMap.put("cargValue", cargValue);
				}
				
				//企业名称
				corpName=request.getParameter("corpName");
				if(corpName!=null && !"".equals(corpName))
				{
					conditionMap.put("corpName", corpName);
				}
			}
				
			
			//设置排序条件
			LinkedHashMap<String, String> orderbyMap =new LinkedHashMap<String, String>();
			orderbyMap.put("id", StaticValue.ASC);
			
			if(StaticValue.getCORPTYPE() == 0)
			{
				//获取企业信息
				LinkedHashMap<String, String> corpConditionMap = new LinkedHashMap<String, String>();
				corpConditionMap.put("corpCode", "100001");
				LfCorp lfCorp=new BaseBiz().getByCondition(LfCorp.class, corpConditionMap, null).get(0);
				corpInfo="【"+lfCorp.getCorpCode()+"】"+lfCorp.getCorpName();
				
				//查询数据
				List<GwPushRsProtocol> gwPushRsProtocolList=new PushRsProtocolBiz().getGwPushRsProtocol(conditionMap, orderbyMap,pageInfo);
				//设置request参数
				request.setAttribute("gwPushRsProtocolList",gwPushRsProtocolList);
			}else
			{
				//查询数据
				List<DynaBean> gwPushRsProtocolList=new PushRsProtocolBiz().getGwPushRsProtocolList(conditionMap,pageInfo);
				//设置request参数
				request.setAttribute("gwPushRsProtocolList",gwPushRsProtocolList);
			}
			
			request.setAttribute("pageInfo",pageInfo);	
			conditionMap.put("cargName", cargName);
			conditionMap.put("cargValue", cargValue);
			request.setAttribute("conditionMap",conditionMap);
			request.setAttribute("corpInfo", corpInfo);
			
			int corptype = StaticValue.getCORPTYPE();
			//单企业
			if(corptype==0)
			{
			//页面跳转
			request.getRequestDispatcher(
					empRoot + basePath + "/wg_pushRsProtocol.jsp").forward(
					request, response);
			}else
			{
				//页面跳转
				request.getRequestDispatcher(
						empRoot + basePath + "/wg_pushRsProtocolMulit.jsp").forward(
						request, response);
			}
			
		} catch (Exception e) {
			String str="";
			EmpExecutionContext.error(e, "查询API推送用户回应管理"+str+"时，产生异常!");
		}
	}
	
	/**
	 * 新增API推送用户回应
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void add(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//返回命令
		String rspCmd = request.getParameter("rspCmd");
		//客户参数名
		String cargName = request.getParameter("cargName");
		//回应状态
		String rspStatus = request.getParameter("rspStatus");
		//客户字段值
		String cargValue = request.getParameter("cargValue");
		
		//企业编码
		String corpCode="";
		
		try
		{
			//单企业
			if(StaticValue.getCORPTYPE() == 0)
			{
				corpCode="100001";
			}else
			{
				corpCode=request.getParameter("corpCode");
			}
			
			if(MessageUtils.extractMessage("mwadmin", "txgl_apimanage_text_113", request).equals(cargValue))
			{
				response.getWriter().print("noCargValue");
				return;
			}
			
			//验证是否重复
			LinkedHashMap<String, String> repeatConditionMap = new LinkedHashMap<String, String>();
			repeatConditionMap.put("ecid", corpCode);
			repeatConditionMap.put("rspCmd", rspCmd);
			repeatConditionMap.put("cargName", cargName);
			repeatConditionMap.put("rspStatus", rspStatus);
			List<GwPushRsProtocol> gwPushRsProtocolList=baseBiz.findListByCondition(GwPushRsProtocol.class, repeatConditionMap, null);
			if(gwPushRsProtocolList!=null&&gwPushRsProtocolList.size()>0)
			{
				response.getWriter().print("repeat");
				return;
			}
			
		
			//设置API推送回应相关信息
			GwPushRsProtocol gwPushRsProtocol = new GwPushRsProtocol();
			
			gwPushRsProtocol.setCargName(cargName);
			gwPushRsProtocol.setCargValue(cargValue);
			gwPushRsProtocol.setRspStatus(Integer.parseInt(rspStatus));
			gwPushRsProtocol.setRspCmd(Integer.parseInt(rspCmd));
			gwPushRsProtocol.setEcid(Integer.parseInt(corpCode));
			//预留字段 填'000000'
			gwPushRsProtocol.setUserid("000000");
			
			//查询个性化接口企业信息
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("ecid", corpCode);
			List<GwMultiEnterp> gwMultiEnterpList=baseBiz.findListByCondition(GwMultiEnterp.class, conditionMap, null);
			//没有获取到相应的企业基本协议信息
			if(gwMultiEnterpList==null||gwMultiEnterpList.size()<1)
			{
				response.getWriter().print("noBaseInfo");
				return;
			}
			GwMultiEnterp gwMultiEnterp=gwMultiEnterpList.get(0);
			
			
			//设置回应格式
			conditionMap.clear();
			//设置命令
			if("1".equals(rspCmd))
			{
				conditionMap.put("funname", "MO");
			}else if("2".equals(rspCmd))
			{
				conditionMap.put("funname", "RPT");
			}
			//设置请求类型
			conditionMap.put("cmdtype", rspStatus);
			//设置企业编码
			conditionMap.put("ecid", corpCode);
			//设置方法类型
			conditionMap.put("funtype", gwMultiEnterp.getFuntype());
			
			List<GwBaseprotocol> gwBaseprotocolList=baseBiz.findListByCondition(GwBaseprotocol.class, conditionMap, null);
			//没有获取到相应的企业基本协议信息
			if(gwBaseprotocolList==null||gwBaseprotocolList.size()<1)
			{
				response.getWriter().print("noBaseInfo");
				return;
			}
			
			GwBaseprotocol gwBaseprotocol=gwBaseprotocolList.get(0);
			gwPushRsProtocol.setCrspfmt(gwBaseprotocol.getRettype());
			
			//新增API推送回应
			boolean result = baseBiz.addObj(gwPushRsProtocol);
			response.getWriter().print(result);
		} catch (Exception e)
		{
			response.getWriter().print(ERROR);
			EmpExecutionContext.error(e,"新增API推送用户回应管理失败！");
		}
	}
	
	/**
	 * 修改API推送用户回应
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//客户字段值
		String cargValue = request.getParameter("cargValue");
		
		//自增ID
		String id=request.getParameter("id");
		//返回命令
		//String rspCmd = request.getParameter("rspCmd");
		//客户参数名
		//String cargName = request.getParameter("cargName");
		//回应状态
		//String rspStatus = request.getParameter("rspStatus");
		try
		{
			//更新的条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("id", id);
			//更新的值
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("cargValue", cargValue);
			//更新操作
			boolean result=new BaseBiz().update(GwPushRsProtocol.class, objectMap, conditionMap);
			//返回值
			response.getWriter().print(result);
		} catch (Exception e)
		{
			response.getWriter().print(ERROR);
			EmpExecutionContext.error(e,"修改API推送用户回应管理失败！");
		}
	}
	
	/**
	 * 删除API推送用户回应
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		
		try
		{
			//返回命令
			//String rspCmd = request.getParameter("rspCmd");
			//客户参数名
			//String cargName = request.getParameter("cargName");
			//if(cargName!=null&&cargName.length()>0){
				//cargName= new String(cargName.getBytes("iso8859-1"),"UTF-8");
			//}
			//回应状态
			//String rspStatus = request.getParameter("rspStatus");
			//ECID
			//String ecid = request.getParameter("ecid");
			
			//ID参数
			String id=request.getParameter("id");
			
//			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
//			conditionMap.put("rspCmd", rspCmd);
//			conditionMap.put("cargName", cargName);
//			conditionMap.put("rspStatus", rspStatus);
//			conditionMap.put("ecid", "100001");
			
			//根据ID删除数据
			int count = new BaseBiz().deleteByIds(GwPushRsProtocol.class, id);
			//异步返回删除条数
			response.getWriter().print(count);
			
		} catch (Exception e)
		{
			response.getWriter().print(0);

			EmpExecutionContext.error(e,"API推送用户回应删除异常！");
		}
	}
	
	
	/**
	 * 获取公司信息
	 * @param request
	 * @param response
	 */
	public void corpList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{

		PageInfo pageInfo = new PageInfo();
		//登录操作员信息
		LfSysuser loginSysuser = (LfSysuser)request.getSession(false).getAttribute("loginSysuser");
		//加载排序条件 
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//操作员企业编码
		String corpCode = loginSysuser.getCorpCode();
		 List<LfCorp> corpList = null;
		//不为100000的企业编码不允许查询
//		if(corpCode != null && "100000".equals(corpCode))
//		{
			//非第一次登录时加载查询条件
			String code = request.getParameter("code");
			if(code != null && !"".equals(code))
			{
				code = URLDecoder.decode(code, "UTF-8");
				conditionMap.put("corpCode&like", code);
			}
			String name = request.getParameter("condcorpname");
			if(name != null && !"".equals(name))
			{
				name = URLDecoder.decode(name, "UTF-8");
				conditionMap.put("corpName&like", name);
			}
			//非10万企业
			conditionMap.put("corpCode&<>", "100000");
			orderbyMap.put("corpCode", "desc");
		try {
			pageSet(pageInfo, request);
			corpList = baseBiz.getByConditionNoCount(LfCorp.class, null, conditionMap,
					orderbyMap, pageInfo);
			
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取公司信息产生异常!");
		}
		
//		}else
//		{
//			EmpExecutionContext.error("查询企业信息异常，操作员企业编码非管理级企业，操作员企业编码："+corpCode);
//		}
		request.setAttribute("conditionMap", conditionMap);
		request.setAttribute("corpList", corpList);
		request.setAttribute("pageInfo",pageInfo);
//		if(StaticValue.CORPTYPE==0){
//			request.getRequestDispatcher(empRoot + basePath + "/wg_customersList.jsp").forward(request, response);
//		}else{
			request.getRequestDispatcher(empRoot + basePath + "/wg_corpListMulit.jsp").forward(request, response);
//		}
	}
}
