package com.montnets.emp.global.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.system.LfPageField;
import com.montnets.emp.util.PageInfo;



public class glo_operPageFieldSvt extends BaseServlet{	
	
	
	
	final BaseBiz baseBiz = new BaseBiz();
	
	private static final long serialVersionUID = 8860328134397197778L;
	
	/**
	 * 页面初始化，查询数据库
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		//数据库返回列表
		List<LfPageField> pageFieldList = new ArrayList<LfPageField>();
		//分页
		PageInfo pageInfo=new PageInfo();
		//查询数据库
		try {
			pageSet(pageInfo, request);
			pageFieldList = baseBiz.getByCondition(LfPageField.class, null, null,null,pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"servlet查询方法异常！");
		}
		
		//放入请求中
		request.setAttribute("pageFieldList", pageFieldList);
		request.setAttribute("pageInfo", pageInfo);

		//重定向
		try {
			request.getRequestDispatcher("/common/glo_operPageField.jsp").forward(request, response);
		} catch (ServletException e) {
			EmpExecutionContext.error(e,"servlet异常！");
		} catch (IOException e) {
			EmpExecutionContext.error(e,"servlet跳转异常！");
		}

	}
	
	/**
	 * 新增页面控件
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void addPageField(HttpServletRequest request, HttpServletResponse response)
	{
		String opModule = "页面控件维护";
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "ADD";	
		
		//返回信息
		String result = "failed";
		
		//接收参数
		//模块编号
		String modleId = request.getParameter("modleId");
		//界面编号
		String pageId = request.getParameter("pageId");
		//控件名称
		String fieldName = request.getParameter("fieldName");
		//控件词汇
		String field = request.getParameter("field");
		//控件类型
		String fieldTypeStr= request.getParameter("fieldType");
		//控件子项值
		String subFieldValue = request.getParameter("subFieldValue");
		//控件子项名称
		String subFieldName = request.getParameter("subFieldName");
		//控件是否显示
		String filedShowStr = request.getParameter("filedShow");
		//是否有子项
		String subFieldStr = request.getParameter("subField");
		//控件默认值
		String defaultValue = request.getParameter("defaultValue");
		//子项排序值 
		String sortValueStr =request.getParameter("sortValue");
		int sortValue = (sortValueStr!=null && !"".equals(sortValueStr)?Integer.parseInt(sortValueStr):0);
		//是否子项
		String isFieldStr = request.getParameter("isField");
		
		//控件编号
		String fieldId = request.getParameter("fieldId");
		if(isFieldStr != null && isFieldStr.length() > 0)
		{
			//如果不是子项，或是子项且控件编号为空时，从数据库中取fieldId最大值+1
			if("1".equals(isFieldStr) || ("0".equals(isFieldStr) && (fieldId == null || fieldId.length() < 1)))
				{
					operPageFieldDAO dao = new operPageFieldDAO();
					fieldId = dao.getMaxFieldId(fieldTypeStr);
				}
		}
		//判断页面参数是否为空
		if(modleId == null || pageId == null || fieldId ==null || fieldName == null 
			|| fieldTypeStr == null || filedShowStr == null || subFieldStr == null 
			|| isFieldStr == null)
		{
			EmpExecutionContext.error("新增页面控件获取参数异常，" +
					"modleId:" + modleId+
					";pageId:" + pageId+
					";fieldName:" + fieldName +
					";fieldTypeStr:" + fieldTypeStr +
					";filedShowStr:" + filedShowStr +
					";subFieldStr:" + subFieldStr +
					";isFieldStr:" + isFieldStr +
					";fieldId:" + fieldId
					);
			result = "failed";
		}	
		else
		{
			try {
				//字符转换抛异常
				//控件类型
				int fieldType = Integer.valueOf(fieldTypeStr);
				//控件是否显示
				int filedShow = Integer.valueOf(filedShowStr);
				//是否有子项
				int subField = Integer.valueOf(subFieldStr);
				//是否子项
				int isField = Integer.valueOf(isFieldStr);
				//初始化对象
				LfPageField LfPageField = new LfPageField();
				//控件是子项时，允许维护“控件子项值”、“控件子项名称”、“子项排序值”
				if(isField == 0)
				{
					LfPageField.setSubFieldValue(subFieldValue);
					LfPageField.setSubFieldName(subFieldName);
				}
				LfPageField.setSortValue(sortValue);
				LfPageField.setModleId(modleId);
				LfPageField.setPageId(pageId);
				LfPageField.setFieldName(fieldName);
				LfPageField.setField(field);
				LfPageField.setFieldType(fieldType);
				LfPageField.setFiledShow(filedShow);
				LfPageField.setSubField(subField);
				LfPageField.setDefaultValue(defaultValue);
				LfPageField.setIsField(isField);
				LfPageField.setFieldId(fieldId);
				//新增页面控件
				boolean isFlag = baseBiz.addObj(LfPageField);
				//返回状态
				result = isFlag?"success":"failed";
			}catch (Exception e) {
				EmpExecutionContext.error("增加页面控件信息失败！");
				result = "failed";
			}
		}
		try {
			String OpStatus = result.equals("success")?"成功":"失败"; 
			//添加操作日志
			opContent = "新增页面控件"+OpStatus+"。（模块编号："+modleId+"，界面编号："+pageId+"，控件名称："+fieldName+"，控件类型："+fieldTypeStr+"，控件是否显示："+filedShowStr+"，是否有子项："+subFieldStr+"，控件默认值："+defaultValue+"，子项排序值 :"+sortValueStr+"，是否子项："+isFieldStr+"）";
			EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
			response.getWriter().print(result);
		} catch (IOException e) {
			EmpExecutionContext.error("返回增加页面控件操作失败！");
		}
	}
	
	/**
	 * 修改页面控件信息
	 * @param request
	 * @param response
	 */
	public void modiPageField(HttpServletRequest request, HttpServletResponse response)
	{
		String opModule = "页面控件维护";
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "UPDATE";	
		//返回状态
		String result = "failed";
		//接收参数
		//模块编号
		String modleId = request.getParameter("modleId");
		//界面编号
		String pageId = request.getParameter("pageId");
		//控件名称
		String fieldName = request.getParameter("fieldName");
		//控件词汇
		String field = request.getParameter("field");
		//控件类型
		String fieldType= request.getParameter("fieldType");
		//控件子项值
		String subFieldValue = request.getParameter("subFieldValue");
		//控件子项名称
		String subFieldName = request.getParameter("subFieldName");
		//控件是否显示
		String filedShow = request.getParameter("filedShow");
		//是否有子项
		String subField = request.getParameter("subField");
		//控件默认值
		String defaultValue = request.getParameter("defaultValue");
		//子项排序值 
		String sortValue =request.getParameter("sortValue");
		//是否子项
		String isField = request.getParameter("isField");
		//控件编号
		String fieldId = request.getParameter("fieldId");
		
		if(modleId == null || pageId == null || fieldId ==null || fieldName == null 
			|| fieldType == null || filedShow == null || subField == null 
			|| isField == null)
		{
			EmpExecutionContext.error("修改页面控件获取参数异常，" +
				"modleId:" + modleId+
				";pageId:" + pageId+
				";fieldName:" + fieldName +
				";fieldTypeStr:" + fieldType +
				";filedShowStr:" + filedShow +
				";subFieldStr:" + subField +
				";isFieldStr:" + isField +
				";fieldId:" + fieldId
					);
			result = "failed";
		}
		else
		{	
			try {
				//更新参数值
				LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
				objectMap.put("modleId", modleId);
				objectMap.put("pageId", pageId);
				objectMap.put("fieldName", fieldName);
				objectMap.put("field", field);
				objectMap.put("subFieldValue", subFieldValue);
				objectMap.put("subFieldName", subFieldName);
				objectMap.put("filedShow", filedShow);
				objectMap.put("subField", subField);
				objectMap.put("defaultValue", defaultValue);
				objectMap.put("sortValue", sortValue);
				objectMap.put("isField", isField);
				if("0".equals(isField))
				{
					objectMap.put("subFieldValue", sortValue);
				}
				//更新条件
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("fieldId", fieldId);
				if("0".equals(isField))
				{	
					//是子项，以增加子项值、子项排序值做为条件
					conditionMap.put("subFieldValue", subFieldValue);
					conditionMap.put("sortValue", sortValue);
				}
				else
				{
					//不是子项，是否为子项做为条件
					conditionMap.put("isField", isField);
				}
				//数据库操作返回标识
				boolean isFlag = true;
				List<LfPageField> lfPageFieldList = baseBiz.findListByCondition(LfPageField.class, conditionMap, null);
				LfPageField lfPageField = lfPageFieldList.get(0);
				//修改页面控件信息
				isFlag = baseBiz.update(LfPageField.class, objectMap, conditionMap);
				//返回状态
				result = isFlag?"success":"failed";
				//查询出修改前信息
				String oldModleId = lfPageField.getModleId();
				String oldPageId = lfPageField.getPageId();
				String oldFieldName = lfPageField.getFieldName();
				String oldFieldTypeStr = lfPageField.getFieldType().toString();
				String oldFiledShow = lfPageField.getFiledShow().toString();
				String oldSubField = lfPageField.getSubField().toString();
				String oldDefaultValue = lfPageField.getDefaultValue();
				String oldSortValue = lfPageField.getSortValue().toString();
				String oldIsField = lfPageField.getIsField().toString();
				String OpStatus = result.equals("success")?"成功":"失败"; 
				opContent = "修改页面控件"+OpStatus+"。（模块编号："+oldModleId+"，界面编号："+oldPageId+"，控件名称："+oldFieldName+"，控件类型："+oldFieldTypeStr+"，控件是否显示："+oldFiledShow+"，是否有子项："+oldSubField+"，控件默认值："+oldDefaultValue+"，子项排序值 :"+oldSortValue+"，是否子项："+oldIsField+"）-->（模块编号："+modleId+"，界面编号："+pageId+"，控件名称："+fieldName+"，控件类型："+oldFieldTypeStr+"，控件是否显示："+filedShow+"，是否有子项："+subField+"，控件默认值："+defaultValue+"，子项排序值 :"+sortValue+"，是否子项："+isField+"）";
			} catch (Exception e) {
				EmpExecutionContext.error("修改页面控件信息失败！");
				result = "failed";
			}
		}
		try {
			//添加操作日志
			EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
			response.getWriter().print(result);
		} catch (IOException e) {
			EmpExecutionContext.error("返回修改页面控件操作失败！");
		}
	}
	
	/**
	 * 删除页面控件信息
	 * @param request
	 * @param response
	 */
	public void delPageField(HttpServletRequest request, HttpServletResponse response)
	{
		String opModule = "页面控件维护";
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "DELETE";
		//接收参数
		//模块编号
		String modleId = request.getParameter("modleId");
		//界面编号
		String pageId = request.getParameter("pageId");
		//控件名称
		String fieldName = request.getParameter("fieldName");
		//控件词汇
		String field = request.getParameter("field");
		//控件类型
		String fieldType= request.getParameter("fieldType");
		//控件子项值
		String subFieldValue = request.getParameter("subFieldValue");
		//控件子项名称
		String subFieldName = request.getParameter("subFieldName");
		//控件是否显示
		String filedShow = request.getParameter("filedShow");
		//是否有子项
		String subField = request.getParameter("subField");
		//控件默认值
		String defaultValue = request.getParameter("defaultValue");
		//子项排序值 
		String sortValue =request.getParameter("sortValue");
		//是否子项
		String isField = request.getParameter("isField");
		//控件编号
		String fieldId = request.getParameter("fieldId");

		//删除条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		if(modleId != null && !"".equals(modleId) && !"null".equals(modleId))
		{
			conditionMap.put("modleId", modleId );
		}
		if(pageId != null &&  !"".equals(pageId))
		{
			conditionMap.put("pageId", pageId);
		}		
		if(fieldId != null && !"".equals(fieldId))
		{
			conditionMap.put("fieldId", fieldId);
		}		
		if(fieldName != null && !"".equals(fieldName))
		{
			conditionMap.put("fieldName", fieldName);
		}		
		if(field != null && !"".equals(field))
		{
			conditionMap.put("field", field);
		}		
		if(fieldType != null && !"".equals(fieldType))
		{
			conditionMap.put("fieldType", fieldType);
		}		
		if(subFieldValue != null && !"".equals(subFieldValue) && !"null".equals(subFieldValue))
		{
			conditionMap.put("subFieldValue", subFieldValue);
		}		
		if(subFieldName != null && !"".equals(subFieldName) && !"null".equals(subFieldValue))
		{
			conditionMap.put("subFieldName", subFieldName);
		}		
		if(filedShow != null && !"".equals(filedShow))
		{
			conditionMap.put("filedShow", filedShow);
		}		
		if(subField != null && !"".equals(subField))
		{
			conditionMap.put("subField", subField);
		}		
		if(defaultValue != null && !"".equals(defaultValue))
		{
			conditionMap.put("defaultValue", defaultValue);
		}
		if(sortValue != null && !"".equals(sortValue) && !"null".equals(sortValue))
		{
			conditionMap.put("sortValue", sortValue);
		}		
		if(isField != null && !"".endsWith(isField))
		{
			conditionMap.put("isField", isField);
		}
		
		//数据库操作标识
		Integer resFlag = 0;
		
		//返回状态
		String result;
		
		try {
			resFlag = baseBiz.deleteByCondition(LfPageField.class, conditionMap);
		} catch (Exception e) {
			resFlag = -1;
			EmpExecutionContext.error("删除页面控件操作失败！");
		}
		
		if(resFlag > 0){
			result = "success";
		}
		else{
			result = "failed";
		}
		String OpStatus = result.equals("success")?"成功":"失败"; 
		opContent = "删除页面控件"+OpStatus+"。（模块编号："+modleId+"，界面编号："+pageId+"，控件名称："+fieldName+"，控件类型："+fieldType+"，控件是否显示："+filedShow+"，是否有子项："+subField+"，控件默认值："+defaultValue+"，子项排序值 :"+sortValue+"，是否子项："+isField+"）";	
		//添加操作日志
		EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
		try {
			response.getWriter().print(result);
		} catch (IOException e) {
			EmpExecutionContext.error("返回删除页面控件操作失败！");
		}
		
	}

}
