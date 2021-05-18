package com.montnets.emp.selfparam.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.selfparam.LfWgParamConfig;
import com.montnets.emp.entity.selfparam.LfWgParmDefinition;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
/**
 * 自定义参数管理
 */
@SuppressWarnings("serial")
public class par_selParamSvt extends BaseServlet {

	private final String empRoot = "xtgl";
	private final String basePath = "/selfparam";
	
	//操作模块
	private final String opModule="参数维护";
	//操作用户
	private final String opSper = StaticValue.OPSPER;
	private final BaseBiz baseBiz=new BaseBiz();
	private final SuperOpLog spLog=new SuperOpLog();

	
	/**
	 * 自定义参数查询
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		PageInfo pageInfo=new PageInfo();
		try {
			//是否第一次打开
			pageSet(pageInfo, request);
			
			String param = request.getParameter("params");
            //加载过滤条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		    //加载参数项 
			if(param != null && !"".equals(param))
			{
				String params="";
				if(Long.valueOf(param)==2)
				{
					params="Param2";
				}
				else if(Long.valueOf(param)==3)
				{
					params="Param3";
				}
				else if(Long.valueOf(param)==4)
				{
					params="Param4";
				}
			   conditionMap.put("param", params);
			}
			//从session获取企业编码
			LfSysuser sysuser = getLoginUser(request);
			String corpCode = sysuser != null?sysuser.getCorpCode():"";
			conditionMap.put("corpCode", corpCode);
			//加载排序列
			orderbyMap.put("param", StaticValue.ASC);
			orderbyMap.put("paramSubNum", StaticValue.ASC);
			//调用查询方法
			List<LfWgParmDefinition> LfWgParamDefinitionList = baseBiz.getByConditionNoCount(LfWgParmDefinition.class,null, conditionMap, orderbyMap,pageInfo);
			//返回结果到前台
			request.setAttribute( "LfWgParamDefinitionList",LfWgParamDefinitionList);
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher(this.empRoot  + this.basePath+"/par_selParamDefinition.jsp")
				.forward(request, response);
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"自定义参数查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot  + this.basePath+"/par_selParamDefinition.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"自定义参数查询cservlet异常");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"自定义参数查询servlet跳转异常");
			}
		}
	}
	/**
	 * 查询参数值信息
	 * @param request
	 * @param response
	 */	
	public void findConfig(HttpServletRequest request, HttpServletResponse response)
	{
		response.setContentType("text/html;charset=UTF-8");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		PageInfo pageInfo=new PageInfo();
		try {
			pageSet(pageInfo, request);
			//获取页面传过来的值
            String paramValue =request.getParameter("paramValue");
            //参数值含义
            String paramName = request.getParameter("paramName");
            //参数名称
            String pid = request.getParameter("dpid");			
		    //加载过滤条件
			if(paramValue !=null && !"".equals(paramValue))
			{
				conditionMap.put("paramValue&like", paramValue);				
			}
			if(paramName !=null && !"".equals(paramName))
			{
				conditionMap.put("paramName&like", paramName);			
			}	
		    LfWgParmDefinition lfdefinition = baseBiz.getById(LfWgParmDefinition.class, pid);
		    if(lfdefinition !=null)
		    {
		    	conditionMap.put("param", lfdefinition.getParam());
		    	conditionMap.put("paramSubNum", lfdefinition.getParamSubNum().toString());
		    }
		    //查询加企业编码
		    LfSysuser sysuser = getLoginUser(request);
		    String corpCode = sysuser!=null?sysuser.getCorpCode():"";
		    conditionMap.put("corpCode", corpCode);
			//加载排序条件
			orderbyMap.put("param", StaticValue.ASC);
			orderbyMap.put("paramSubNum", StaticValue.ASC);
			//调用查询方法
			List<LfWgParamConfig> LfWgParamConfigList = baseBiz.getByConditionNoCount(LfWgParamConfig.class,null, conditionMap, orderbyMap,pageInfo);
			//返回结果到前台
			request.setAttribute( "LfWgParamConfigList",LfWgParamConfigList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("conditionMap",conditionMap);
			request.setAttribute("lfdefinition", lfdefinition);
			request.getRequestDispatcher(this.empRoot  + this.basePath+"/par_selParamConfig.jsp")
				.forward(request, response);
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"自定义参数值查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("conditionMap",conditionMap);
			try {
				request.getRequestDispatcher(this.empRoot  + this.basePath+"/par_selParamConfig.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"参数值查询servlet异常");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"参数值查询servlet跳转异常");
			}
		}
	}
	/**
	 * 新增网关动态参数值 add by chenghong
	 * @param request
	 * @param response
	 */
	public void add(HttpServletRequest request, HttpServletResponse response)
	{
		//操作类型
		String oppType = null;
		//操作内容
		String opContent = null;
		String opUser="";
		oppType=StaticValue.ADD;
		opContent = "新建网关动态参数值";
		String corpcode = null;
		try {
			LfWgParamConfig paramconfig = new LfWgParamConfig();
			//参数定义id.
			String dpid = request.getParameter("dpid");
			//参数值
			String paramvalues = request.getParameter("paramValue");
			//参数含义
			String paramname = request.getParameter("paramName");
			//备注
			String memo= request.getParameter("memo");
			//企业编码
			corpcode = request.getParameter("lgcorpcode");
			opUser=request.getParameter("lguserid");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//根据自己定义参数id获取
			LfWgParmDefinition lfdefinition = baseBiz.getById(LfWgParmDefinition.class, dpid);
		    if(lfdefinition !=null)
		    {
		    	conditionMap.put("param", lfdefinition.getParam());
				conditionMap.put("paramSubNum", lfdefinition.getParamSubNum().toString());
		    }		
			
			conditionMap.put("paramValue", paramvalues);			
			List<LfWgParamConfig> LfWgParamConfigListbyvalue = baseBiz.getByCondition(LfWgParamConfig.class, conditionMap, null);
			conditionMap.remove("paramValue");
			conditionMap.put("paramName",paramname);
			List<LfWgParamConfig> LfWgParamConfigListbyname = baseBiz.getByCondition(LfWgParamConfig.class, conditionMap, null);
			//用于判断是否参数值或参数含义定义重复
			if(LfWgParamConfigListbyvalue !=null && LfWgParamConfigListbyvalue.size()>0)
			{
				response.getWriter().print("valueExsist");
			}
			else if(LfWgParamConfigListbyname != null && LfWgParamConfigListbyname.size()>0)
			{
				response.getWriter().print("nameExsist");
			}
			else
			{	
				 if(lfdefinition !=null){
				//设置对象属性
				paramconfig.setParam(lfdefinition.getParam());
				paramconfig.setParamSubNum(lfdefinition.getParamSubNum());
				 }
				paramconfig.setParamValue(paramvalues);
				paramconfig.setParamName(paramname);
				paramconfig.setCorpCode(corpcode);
				paramconfig.setMemo(memo);
				//保存对象
				boolean result = baseBiz.addObj(paramconfig);
				response.getWriter().print(result);
				
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					String opContent1 = "新建网关动态参数值"+(result==true?"成功":"失败")+"。[参数名称，参数值，参数值含义]" +
							"("+(lfdefinition!=null?lfdefinition.getParamSubName():"")+"，"+paramvalues+"，"+paramname+")";
					
						EmpExecutionContext.info("自定义参数管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
							loginSysuser.getUserName(), opContent1, "ADD");
					
				}				
				
				//添加日志
				spLog.logSuccessString(opUser, opModule, oppType, opContent,corpcode);
			}
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"新增网关动态参数值异常！");
			spLog.logFailureString(opUser, opModule, oppType, opContent+opSper, e,corpcode);
		}
	}
	/**
	 * 修改网关动态参数值
	 * @param request
	 * @param response
	 */
	public void editConfig (HttpServletRequest request, HttpServletResponse response)
	{
		//操作类型
		String oppType = null;
		//操作内容
		String opContent = null;
		oppType=StaticValue.UPDATE;
		opContent="修改网关动态参数值";
		String lgusername = null;
		String lgcorpcode = null;
		try {
			//参数名称
			String paramname = request.getParameter("paramName");
			//备注
			String memo= request.getParameter("memo");
			String pid = request.getParameter("pid");
			lgusername = request.getParameter("lgusername");
			lgcorpcode = request.getParameter("lgcorpcode");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			
			//判断一下参数值含义的重复问题
			LfWgParamConfig lfc= baseBiz.getById(LfWgParamConfig.class, pid);
			conditionMap.put("param", lfc.getParam());
			conditionMap.put("paramSubNum", lfc.getParamSubNum().toString());
			conditionMap.put("paramName", paramname);
			conditionMap.put("pid&<>", pid);
			conditionMap.put("corpCode", lgcorpcode);
			List<LfWgParamConfig> LfWgParamConfigList = baseBiz.getByCondition(LfWgParamConfig.class,null, conditionMap, null);
			if(LfWgParamConfigList.size()>0)
			{
				response.getWriter().print("nameExsist");
				return;
			}
			
			//查询操作之前记录
			LfWgParamConfig befchgEntity = baseBiz.getById(LfWgParamConfig.class, pid);
			String befchgCont = befchgEntity.getParam()+"，"+befchgEntity.getParamSubNum()+"，"+befchgEntity.getParamValue()+"，"+befchgEntity.getParamName();
			
			conditionMap.clear();
			//加载修改属性
			objectMap.put("memo", memo);
			objectMap.put("paramName", paramname);
            //加载条件属性
			conditionMap.put("pid", pid);
			conditionMap.put("corpCode", lgcorpcode);
			//修改方法
			boolean result = baseBiz.update(LfWgParamConfig.class, objectMap, conditionMap);
			response.getWriter().print(result);
			//添加日志
			spLog.logSuccessString(lgusername, opModule, oppType, opContent,lgcorpcode);
			
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent1 = "修改网关动态参数值"+(result==true?"成功":"失败")+"。[参数项，分段数，参数值，参数值含义]" +
						"("+befchgCont+")->("+befchgEntity.getParam()+"，"+befchgEntity.getParamSubNum()+"，"+befchgEntity.getParamValue()+"，"+paramname+")";
				EmpExecutionContext.info("自定义参数管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "UPDATE");
			}				
			
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"修改网关动态参数值异常！");
			spLog.logFailureString(lgusername, opModule, oppType, opContent+opSper, e,lgcorpcode);
		}
		
	}
	/**
	 * 删除网关动态参数值的内容
	 * @param request
	 * @param response
	 */
	public void delConfig(HttpServletRequest request, HttpServletResponse response)
	{
		//操作类型
		String oppType = null;
		//操作内容
		String opContent = null;
		oppType=StaticValue.DELETE;
		opContent = "删除网关动态参数值";
		String pid = request.getParameter("pid");
		String lgusername = request.getParameter("lgusername");
		String lgcorpcode = request.getParameter("lgcorpcode");
		try {
			
			//查询操作之前记录
			LfWgParamConfig befchgEntity = baseBiz.getById(LfWgParamConfig.class, pid);
			String befchgCont = befchgEntity.getParam()+"，"+befchgEntity.getParamSubNum()+"，"+befchgEntity.getParamValue()+"，"+befchgEntity.getParamName();
			
			//删除操作
			int count = baseBiz.deleteByIds(LfWgParamConfig.class, pid);
			if(count>0)
			{
				response.getWriter().print("true");
				//添加日志
				spLog.logSuccessString(lgusername, opModule, oppType, opContent,lgcorpcode);
			}
			
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent1 = "删除网关动态参数值"+(count>0?"成功":"失败")+"。[参数项，分段数，参数值，参数值含义]" +
						"("+befchgCont+")";
				EmpExecutionContext.info("自定义参数管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "DELETE");
			}
			
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"删除网关动态参数值的内容异常！");
			spLog.logFailureString(lgusername, opModule, oppType, opContent+opSper, e,lgcorpcode);
		}
		
	}
	/**
	 * 新增网关动态参数定义
	 * @param request
	 * @param response
	 */
    public void adddefinition(HttpServletRequest request, HttpServletResponse response)
    {
    	//操作类型
    	String oppType = null;
    	//操作内容
    	String opContent = null;
    	oppType=StaticValue.ADD;
		opContent = "新建网关动态参数定义";
		String corpcode =null;
		String lgusername = null;
		try {
			LfWgParmDefinition paramDefinition = new LfWgParmDefinition();
			//参数项
			String param = request.getParameter("param");
			//分段
			String paramSubNum = request.getParameter("ParamSubNum");
			//分隔符
			String paramSubSign = request.getParameter("ParamSubSign");
			//参数名称
			String paramSubName= request.getParameter("ParamSubName");
			//备注
			String memo= request.getParameter("memo");
			//企业编码
			corpcode = request.getParameter("lgcorpcode");
			lgusername = request.getParameter("lgusername");
			if(Long.valueOf(param)==2)
			{
				param="Param2";
			}
			else if(Long.valueOf(param)==3)
			{
				param="Param3";
			}
			else if(Long.valueOf(param)==4)
			{
				param="Param4";
			}
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //判断重复参数名称
			conditionMap.put("paramSubName",paramSubName);
			List<LfWgParmDefinition> LfWgParamDefinitionList = baseBiz.getByCondition(LfWgParmDefinition.class, conditionMap, null);
			if(LfWgParamDefinitionList !=null && LfWgParamDefinitionList.size()>0)
			{
				response.getWriter().print("nameExsist");
			}
			else
			{
				//加载参数属性
				paramDefinition.setParam(param);
				paramDefinition.setParamSubName(paramSubName);
				paramDefinition.setParamSubNum(Integer.valueOf(paramSubNum));
				paramDefinition.setParamSubSign(paramSubSign);
				paramDefinition.setMemo(memo);
				paramDefinition.setCorpCode(corpcode);
				//保存方法
				boolean result = baseBiz.addObj(paramDefinition);
				response.getWriter().print(result);
				//添加日志
				spLog.logSuccessString(lgusername, opModule, oppType, opContent,corpcode);
				
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					String opContent1 = "新建网关动态参数定义"+(result==true?"成功":"失败")+"。[参数项，分段数,分段符，参数名称]" +
							"("+param+"，"+paramSubNum+"，"+paramSubSign+"，"+paramSubName+")";
					EmpExecutionContext.info("自定义参数管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
							loginSysuser.getUserName(), opContent1, "ADD");
				}					
			}
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"新增网关动态参数定义异常！");
			spLog.logFailureString(lgusername, opModule, oppType, opContent+opSper, e,corpcode);
		}
    	
    }
	/**
	 * 修改网关动态参数定义
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
    public void editdefinition(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
    	//操作类型
    	String oppType = null;
    	//操作内容
    	String opContent = null;
    	oppType=StaticValue.UPDATE;
    	opContent="修改网关动态参数定义";
		String corpcode =null;
		String lgusername = null;
    	try {
    		//参数名称
			String curParamSubName = request.getParameter("curParamSubName");
			String ParamSubName = request.getParameter("ParamSubName");
			//参数项
			String param = request.getParameter("param");
			//分段
			String paramSubNum = request.getParameter("ParamSubNum");
			//分隔符
			String paramSubSign = request.getParameter("ParamSubSign");
			//备注
			String memo= request.getParameter("memo");
			//企业编码
			corpcode = request.getParameter("lgcorpcode");
			lgusername = request.getParameter("lgusername");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//判断重复
			if(!curParamSubName.equals(ParamSubName))
			{
				conditionMap.put("paramSubName", ParamSubName);
				List<LfWgParmDefinition> LfWgParmDefinitionList = baseBiz.getByCondition(LfWgParmDefinition.class, conditionMap, null);
				if(LfWgParmDefinitionList != null && LfWgParmDefinitionList.size()>0)
				{
					response.getWriter().print("nameExsist");
					return;
				}
			}
			if(Long.valueOf(param)==2)
			{
				param="Param2";
			}
			else if(Long.valueOf(param)==3)
			{
				param="Param3";
			}
			else if(Long.valueOf(param)==4)
			{
				param="Param4";
			}
			
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			
			//查询操作之前记录
			LfWgParmDefinition befchgEntity = baseBiz.getById(LfWgParmDefinition.class, request.getParameter("pid"));
			String befchgCont = befchgEntity.getParam()+"，"+befchgEntity.getParamSubNum()+"，"+befchgEntity.getParamSubSign()+"，"+befchgEntity.getParamSubName();
			
			//加载修改属性
			conditionMap.clear();
			objectMap.put("memo", memo);
			objectMap.put("param", param);
			objectMap.put("paramSubNum", paramSubNum);
			objectMap.put("paramSubSign", paramSubSign);
			objectMap.put("paramSubName", ParamSubName);
			//加载过滤条件
			conditionMap.put("pid", request.getParameter("pid"));
			conditionMap.put("corpCode",corpcode);
			//调用修改方法
			boolean result = baseBiz.update(LfWgParmDefinition.class, objectMap, conditionMap);
			response.getWriter().print(result);
			//添加日志
			spLog.logSuccessString(lgusername, opModule, oppType, opContent,corpcode);
			
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent1 = "修改网关动态参数定义"+(result==true?"成功":"失败")+"。[参数项，分段数,分段符，参数名称]" +
						"("+befchgCont+")->("+param+"，"+paramSubNum+"，"+paramSubSign+"，"+ParamSubName+")";
				EmpExecutionContext.info("自定义参数管理", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "UPDATE");
			}			
			
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"修改网关动态参数定义异常！ ");
			response.getWriter().print(this.ERROR);
			spLog.logFailureString(lgusername, opModule, oppType, opContent+opSper, e,corpcode);
		}    	
    }
    
    /**
     * 判断此参数项目是否已添加了2个分段值
     * @param request
     * @param response
     */
    public void changeparamsign(HttpServletRequest request, HttpServletResponse response)
    {
    	try {
    		//获取参数项
    		String param = request.getParameter("param");
    		if(Long.valueOf(param)==2)
			{
				param="Param2";
			}
			else if(Long.valueOf(param)==3)
			{
				param="Param3";
			}
			else if(Long.valueOf(param)==4)
			{
				param="Param4";
			}
    		//加载过滤条件
    		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
    		conditionMap.put("param", param);
    		//调用查询方法
    		List<LfWgParmDefinition> LfWgParamDefinitionList = baseBiz.getByCondition(LfWgParmDefinition.class, conditionMap, null);
    		//判断结果值是否为空
    		if(LfWgParamDefinitionList!=null && LfWgParamDefinitionList.size()>0)
    		{
    			if(LfWgParamDefinitionList.size()==2)
    			{
    				response.getWriter().print("noadd");    				
    			}
    			else
    			{
    				response.getWriter().print("2");
    			}
    		}
    		else 
    		{
    			response.getWriter().print("1");    			
    		}
    		
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"判断参数项目是否已添加了2个分段值异常！");
		}
    }
}
