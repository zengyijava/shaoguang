package com.montnets.emp.common.constant;

import com.montnets.emp.common.context.EmpExecutionContext;




public class ErrorCodeParam
{
	//错误代码
	private String errorCode;
	//错误代码描述
	private String description;

	
	
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
		EmpExecutionContext.error("设置错误代码："+errorCode);
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
}
