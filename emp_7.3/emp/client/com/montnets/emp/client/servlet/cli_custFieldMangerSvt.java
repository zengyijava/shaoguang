package com.montnets.emp.client.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.client.biz.CustFieldBiz;
import com.montnets.emp.client.vo.CustFieldValueVo;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.client.LfCustField;
import com.montnets.emp.entity.client.LfCustFieldValue;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;


/**
 * 
 * 
 * @project montnets_emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-25 上午08:51:50
 * @description 
 */
@SuppressWarnings("serial")
public class cli_custFieldMangerSvt extends BaseServlet
{
 
	//日志模块
	static String  opModule=StaticValue.ADDBR_MANAGER;
    //操作员
	static String opSper = StaticValue.OPSPER;
    private static CustFieldBiz biz = new CustFieldBiz();
    private static String empRoot ="client";

	static BaseBiz baseBiz=new BaseBiz();
	static SuperOpLog spLog=new SuperOpLog();
     /**
 	 * 
 	 * @description 获取企业客户属性列表
 	 * @param request
 	 * @param response
 	 */
 	public void find(HttpServletRequest request, HttpServletResponse response) {
 		try {
 			//当前登录企业
 			String lgcorpcode = request.getParameter("lgcorpcode");
 			//自定义属性列表
 			List<LfCustField> custFieldList = this.getCustFieldList(lgcorpcode);
			
 			String fieldTemp = "";
 			String numSeq = "";
 			int num = 1;
 			//如果自定义属性列表不为空
 			if(custFieldList != null && custFieldList.size() > 0){
 				boolean numFlag = false;
 				for(int i = 0; i < custFieldList.size(); i++){
 						LfCustField lfCustField = (LfCustField)custFieldList.get(i);
 						fieldTemp = lfCustField.getField_Ref();
 						int fieldNum = Integer.parseInt(fieldTemp.substring(5)) - 1 ;
 						if(fieldNum > i){
 							num = i + 1;
 							numFlag = true;
 							break;
 						}
 				}
 				if(numFlag == false){
 					num = custFieldList.size() + 1;
 				}
 			}
 			if(num < 10){
 				numSeq += "0";
 			}
 			numSeq += String.valueOf(num);
 			String field_ref = "FIELD" + numSeq;
 			request.setAttribute("field_ref", field_ref);
			request.setAttribute("rsList", custFieldList);
 			//跳转页面
 			request.getRequestDispatcher(this.empRoot + "/climan/cli_custFieldManage.jsp").forward(
 					request, response);
 		} catch (Exception e) {
 			//打印异常信息
 			EmpExecutionContext.error(e,"获取企业客户属性列表出现异常！");
 		}
 	}
 	
 	/**
 	 * @description  获取    属性值    列表
	 * @param request
	 * @param response
	 */
	public void getTable(HttpServletRequest request, HttpServletResponse response) {
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		try {
			PageInfo pageInfo=new PageInfo();
			pageSet(pageInfo, request);
			
			List<CustFieldValueVo> dataVoList = new ArrayList<CustFieldValueVo>();
			//查询条件
			CustFieldValueVo dataVo = new CustFieldValueVo();
			//当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			
				dataVo.setCorp_code(lgcorpcode);
				//属性值id
 				String fieldID = request.getParameter("depId");
 				//属性值
 				String fieldValue = request.getParameter("fieldValue");
 				
 				if(null != fieldID && !"".equals(fieldID) && !"undefined".endsWith(fieldID))
 				{
 					dataVo.setField_ID(Long.valueOf(fieldID));
 				}
 				if(null != fieldValue && !"".equals(fieldValue))
 				{
 					dataVo.setField_Value(fieldValue);
 				}
 				//查询操作
 				dataVoList = biz.getCustVos(dataVo, pageInfo);
	 		
	 			request.setAttribute("rsList", dataVoList);
	 			request.setAttribute("pageInfo", pageInfo);
	 			request.setAttribute("fieldID", fieldID);
	 			request.setAttribute("fieldName", fieldValue);
	 			//增加查询日志
	 			if(pageInfo!=null){
	 				long end_time=System.currentTimeMillis();
	 				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"毫秒，数量："+pageInfo.getTotalRec();
	 				opSucLog(request, "客户属性管理", opContent, "GET");
	 			}
	 			//跳转
 				request.getRequestDispatcher(this.empRoot + "/climan/cli_custFieldManageTable.jsp").forward(
					request, response);
 				
	 			} catch (Exception e) {
	 				//打印异常信息
	 				EmpExecutionContext.error(e,"获取属性值列表出现异常！");
		}
	}
	/**
	 * @description 获取企业客户属List
	 * @return List
	 * @throws Exception
	 */
	private List<LfCustField> getCustFieldList(String corpCode)throws Exception
	{
		//自定义属性列表
		List<LfCustField> dataList = new ArrayList<LfCustField>();
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
		try
		{
			//企业编码
			conditionMap.put("corp_code", corpCode);
			orderbyMap.put("field_Ref","asc" );
			//查询属性记录
			dataList = baseBiz.getByCondition(LfCustField.class, conditionMap, orderbyMap);
			
		} catch (Exception e)
		{
			//记录和打印异常信息
			EmpExecutionContext.error(e,"获取企业客户属性出现异常！");
 		}
		//返回结果集合
		return dataList;
	}
	/**
	 * @description 添加、修改属性值
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void EditCustFieldValue(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//操作结果
		boolean op = false;
		//操作类型
		String opType = "";
		//操作内容
	    String opContent ="";
		//当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");

		String opUser = "";
		try
		{
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			opUser = sysuser.getUserName();
			String ID = request.getParameter("ID");
			//属性值id
			String fieldID = request.getParameter("fieldID");
			//属性值
			String fieldValue = request.getParameter("fieldValue");
			
			if(ID == null){
				//操作类型
				opType = StaticValue.ADD;
				//操作内容
				opContent = "新建属性值";
				
				LfCustFieldValue lfCustFieldValue = new LfCustFieldValue();
				
				lfCustFieldValue.setField_ID(Long.parseLong(fieldID));
				lfCustFieldValue.setField_Value(fieldValue);
				//添加属性值
				op = baseBiz.addObj(lfCustFieldValue);
				String ret="失败";
				if(op){
					ret="成功";
				}
				//增加操作日志
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("客户属性管理", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "新建属性值"+ret+"。[属性值]（"+fieldValue+"）", "ADD");
				}
				
				//EmpExecutionContext.info("模块名称：客户属性管理，企业："+lgcorpcode+"，操作员："+opUser+"，新建属性值（属性值："+fieldValue+"），新建成功");
			}else{
				//通过id找到自定义属性记录
				LfCustFieldValue lfCustField = baseBiz.getById(LfCustFieldValue.class, ID);
				String before="";
				if(lfCustField!=null){
					before=lfCustField.getField_Value();
				}
				opType = StaticValue.UPDATE;
				opContent = "修改属性值";
				//修改属性值
				op = biz.updateCustFieldValue(ID, fieldValue);
				String ret="失败";
				if(op){
					ret="成功";
				}
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					EmpExecutionContext.info("客户属性管理", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "修改属性值"+ret+"。[id，属性值]（"+ID+"，"+before+"）->（"+ID+"，"+fieldValue+"）", "UPDATE");
				}
				//EmpExecutionContext.info("模块名称：客户属性管理，企业："+lgcorpcode+"，操作员："+opUser+"，修改属性值（属性值id:"+ID+"，属性值："+fieldValue+"），修改成功");
			}
			
		} catch (Exception e)
		{
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, e,lgcorpcode);
			EmpExecutionContext.error(e,"模块名称：客户属性管理，企业："+lgcorpcode+"，操作员："+opUser+"，添加或修改属性值出现异常！");
		}
		//异步打印添加或修改结果
		response.getWriter().print(op);
		//成功日志
		spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);

	}
	
	
	/**
	 * @description 判断属性值是否存在
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void checkFieldValue(HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		//判断属性值是否存在
		List<LfCustFieldValue> dataList = new ArrayList<LfCustFieldValue>();
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
		
		boolean op = false;
		
		try
		{
			String ID = request.getParameter("ID");
			//属性值id
			String fieldID = request.getParameter("fieldID");
			//属性值
			String fieldValue = request.getParameter("fieldValue");

			//TODO
			conditionMap.put("field_ID", fieldID);
			conditionMap.put("field_Value", fieldValue);
			orderbyMap.put("field_ID","asc" );

			//查找该属性条件下是否存在
			dataList = baseBiz.getByCondition(LfCustFieldValue.class, conditionMap, orderbyMap);
			//如果不为空则返回true
			if(dataList != null && dataList.size() > 0){
				op = true;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"判断属性值是否存在出现异常！");
 		}
		//异步调用返回结果
		response.getWriter().print(op);
	}
	
	/**
	 * 删除单个属性值
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		//获取属性值id
		String ID = request.getParameter("ID");
		int delnum = 0;
		//当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		//删除操作
		String opType = StaticValue.DELETE;
		//操作内容
		String opContent = "删除属性值";
		String opUser = "";
		try
		{
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			opUser = sysuser.getUserName();
			//通过id找到自定义属性记录
			String before="";
			if(ID!=null&&!"".equals(ID)){
				String[] ids=ID.split(",");
				for(int k=0;k<ids.length;k++){
					LfCustFieldValue lfCustField = baseBiz.getById(LfCustFieldValue.class, ids[k]);
					if(lfCustField!=null){
						before=lfCustField.getField_Value()+","+before;
					}
				}

			}
			delnum = biz.delCustFieldValue(ID);
			
			if(delnum!=0){
				EmpExecutionContext.info("客户属性管理", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "删除属性值成功。[id|属性值]（"+ID+"|"+before+"）", "DELETE");
			}else{
				EmpExecutionContext.info("客户属性管理", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "删除属性值失败。[id|属性值]（"+ID+"|"+before+"）", "DELETE");
			}
		} 
		catch (Exception e)
		{
			//失败日志
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper,null,lgcorpcode);
			EmpExecutionContext.error(e,"删除单个属性值出现异常！");
		}
		//异步输入结果
		response.getWriter().print(delnum);
		spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
	}
	
	/**
	 * @description 删除客户属性
	 * @param response   
	 */
	public void delGroup(HttpServletRequest request, HttpServletResponse response)
	{
		//属性id
		String udgId = request.getParameter("udgId");
		Integer delnum = 0;
		//当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		//删除操作
		String opType = StaticValue.DELETE;
		String opContent = "删除属性";
		String opUser = "";
		try
		{
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			opUser = sysuser.getUserName();
			//通过id删除群组
			LfCustField lfCustField = baseBiz.getById(LfCustField.class, udgId);
			if(lfCustField==null){
				opSucLog(request, "客户属性管理", "删除属性时，客户属性ID不存在(可能已经被删除)，udgId："+udgId, StaticValue.OTHER);
				//该用户可能已经删除
				response.getWriter().print("-1");
				return;
			}
			String before="";
			if(lfCustField!=null){
				before=lfCustField.getField_Name();
			}
			delnum = biz.delGroupsAllInfo(udgId);
			if(delnum!=0){
				EmpExecutionContext.info("客户属性管理", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "删除客户属性成功。[id，属性值]（"+udgId+"，"+before+"）", "DELETE");
			}else{
				EmpExecutionContext.info("客户属性管理", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "删除客户属性失败。[id，属性值]（"+udgId+"，"+before+"）", "DELETE");
			}
			
			//异步返回执行结果
			response.getWriter().print(delnum);
		} catch (Exception e)
		{
			//异常捕捉日志
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, e,lgcorpcode);
			EmpExecutionContext.error(e,"模块名称：客户属性管理，企业："+lgcorpcode+"，操作员："+opUser+"，属性id："+udgId+"，删除客户属性出现异常！");
		}
		spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
	}
	/**
	 * @description 判断LfCustFieldValue实体类
	 * 对应的数据库中的LF_CUSTFIELD_VALUE表中的属性值是否存在，即客户属性所绑定了哪些属性值。
	 * @param request
	 * @param response
	 */
	public void checkCustField(HttpServletRequest request, HttpServletResponse response)
	{
		//自定义属性id
		String field_ID = request.getParameter("udgId");
		try
		{
			boolean exists = false;
			List<LfCustFieldValue> lfList2groList = new ArrayList<LfCustFieldValue>();
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			
				conditionMap.put("field_ID",field_ID);
				//通过id获取属性值
				lfList2groList = baseBiz.findListByCondition(LfCustFieldValue.class, conditionMap,null);
				
				if(lfList2groList.size() > 0)
				{
					//如果属性值存在就返回true
					exists = true;
				}
			
			//异步返回接结果
			response.getWriter().print(exists);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取客户属性所绑定属性值出现异常！");
		}
		
	}
	/**
	 * @description 添加属性
	 * @param request
	 * @param response
	 */
	public void addCustField(HttpServletRequest request, HttpServletResponse response){
		//属性名
		String udgName = request.getParameter("udgName");  
		//引用字段
		String field_ref = request.getParameter("field_ref");
		//属性类型
		String v_type = request.getParameter("groupType");
		//当前登录操作员id
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		//当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		
		LfCustField lfCustField = new LfCustField();
		//添加成功标志
		boolean addok = false;
		String opType = StaticValue.ADD;
		String opContent = "添加属性";
		String opUser = "";
		try
		{
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			opUser = sysuser.getUserName();
			if(null != udgName && 0 != udgName.length())
			{
				lfCustField.setCorp_code(lgcorpcode);
				lfCustField.setField_Name(udgName);
				lfCustField.setField_Ref(field_ref);
				lfCustField.setUserid(lguserid);
				lfCustField.setV_type(v_type);
				//保存对象
				addok = baseBiz.addObj(lfCustField);
				if(addok){
					EmpExecutionContext.info("客户属性管理", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "添加属性成功。[属性名，引用字段，属性类型]（"+udgName+"，"+field_ref+"，"+v_type+"）", "ADD");
				}else{
					EmpExecutionContext.info("客户属性管理", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "添加属性失败。[属性名，引用字段，属性类型]（"+udgName+"，"+field_ref+"，"+v_type+"）", "ADD");
				}
				response.getWriter().print(addok);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"添加属性出现异常！");
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, e,lgcorpcode);
		}
		spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);

	}
	
	/**
	 * @param request
	 * 
	 * @param response
	 * 
	 */
	public void updateGroupName(HttpServletRequest request, HttpServletResponse response){
		//群组名称
		String udgName = request.getParameter("udgName");
		//群组id
		String udgId = request.getParameter("udgId");
		//属性类型
		String vtype = request.getParameter("vtype");
		//当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		
		LinkedHashMap<String,String> objectMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		boolean updateok = false;
		//日志操作类型
		String opType = StaticValue.UPDATE;
		//操作内容
		String opContent = "修改属性名称";
		String opUser = "";
		try
		{
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			opUser = sysuser.getUserName();
			//属性名称
			objectMap.put("field_Name", udgName);  
			//属性类型
			objectMap.put("v_type", vtype);
			objectMap.put("corp_code", lgcorpcode);
			
			if(null != udgId && 0 != udgId.length())
			{
				conditionMap.put("id", udgId);
			}
			//更新操作
			LfCustField lfCustField = baseBiz.getById(LfCustField.class, udgId);
			String before="";
			if(lfCustField!=null){
				before=lfCustField.getField_Name();
			}else{
				opSucLog(request, "客户属性管理", "修改属性时，该属性不存在，可能已被删除，udgId："+udgId, StaticValue.OTHER);
				response.getWriter().print("notexit");
				return;
			}
			updateok = baseBiz.update(LfCustField.class, objectMap, conditionMap);
			if(updateok){
				EmpExecutionContext.info("客户属性管理", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "修改属性成功。[属性ID，属性名]（"+udgId+"，"+before+"）->（"+udgId+"，"+udgName+"）", "UPDATE");
				//EmpExecutionContext.info("模块名称：客户属性管理，企业："+lgcorpcode+"，操作员："+opUser+"，修改属性（属性名:"+udgName+"）成功");	
			}else{
				EmpExecutionContext.info("客户属性管理", sysuser.getCorpCode(), sysuser.getUserId()+"", sysuser.getUserName(), "修改属性失败。[属性ID，属性名]（"+udgId+"，"+before+"）->（"+udgId+"，"+udgName+"）", "UPDATE");
				//EmpExecutionContext.error("模块名称：客户属性管理，企业："+lgcorpcode+"，操作员："+opUser+"，修改属性（属性名:"+udgName+"）失败");	
			}
			
			response.getWriter().print(updateok);
		} catch (Exception e)
		{
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, e,lgcorpcode);
			EmpExecutionContext.error(e,"修改属性名称出现异常 ！");
		}
		spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);

	}
	
	/**
	 * @param request
	 * 
	 * @param response
	 * @throws IOException 
	 * 
	 */
	public void getCustFileByid(HttpServletRequest request, HttpServletResponse response) throws IOException{
		//群组id
		String id = request.getParameter("udgId");
		LfCustField lfCustField = new LfCustField();
		
		String op = "";
		try
		{
			//通过id找到自定义属性记录
			lfCustField = baseBiz.getById(LfCustField.class, id);
			op = lfCustField.getField_Name() + ";" + lfCustField.getV_type();
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取自定义属性记录出现异常！");
		}
		//异步输入结果
		response.getWriter().print(op);

	}
	/**
	 * @description  验证是否是相同属性
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void checkGpName(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String udgName = request.getParameter("udgName");
 		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
 		LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
 		List<LfCustField> lfCustFileList = new ArrayList<LfCustField>();
 		//当前登录企业
 		String lgcorpcode = request.getParameter("lgcorpcode");
 		//企业代码
 		conditionMap.put("corp_code", lgcorpcode);
 		//entity class LfCustField 中的String field_Name显示名称
		conditionMap.put("field_Name", udgName);
		try
		{
			PageInfo pageInfoGroup = new PageInfo();
			//每次查找1w条记录
			pageInfoGroup.setPageSize(10000);
			//设为第一页
			pageInfoGroup.setPageIndex(1);
			
			orderbyMap.put("field_Name","asc" );
			//根据条件查询自定义属性列表
			lfCustFileList = baseBiz.getByCondition(LfCustField.class, conditionMap, orderbyMap);
			if(lfCustFileList.size()> 0)
			{
				//成功打印到页面
				response.getWriter().print("true");
			}else
			{
				response.getWriter().print("false");
			}
		} catch (Exception e)
		{
			response.getWriter().print("false");
			EmpExecutionContext.error(e," 验证是否是相同属性出现异常！");
		}
	}
	
	/**
	 * @description  记录操作成功日志  
	 * @param request
	 * @param modName 模块名称
	 * @param opContent 操作详情    	
	 * @param opType 操作类型 ADD UPDATE DELETE GET OTHER		 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-3-3 上午11:29:50
	 */
	public void opSucLog(HttpServletRequest request,String modName,String opContent,String opType){
		try{
			LfSysuser lfSysuser = null;
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if(obj == null) return;
			lfSysuser = (LfSysuser)obj;
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}catch (Exception e) {
			EmpExecutionContext.error(e," 记录日志出现异常！");
		}
	}
}
