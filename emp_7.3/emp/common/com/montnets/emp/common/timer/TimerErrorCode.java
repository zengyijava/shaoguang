/**
 * 
 */
package com.montnets.emp.common.timer;

/**
 * @author Administrator
 *
 */
public class TimerErrorCode {

	//错误代码
	private String errorCode;
	//错误代码描述
	private String description;

	
	
	//获取错误代码
	public String getErrorCode() {
		//返回错误代码
		return errorCode;
	}

	//设置错误代码
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	//获取描述
	public String getDescription()
	{
		//返回描述
		return description;
	}

	//设置描述
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	
	
}
