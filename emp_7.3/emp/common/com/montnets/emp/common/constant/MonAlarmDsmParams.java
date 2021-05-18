/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2016-4-5 下午02:12:58
 */
package com.montnets.emp.common.constant;

/**
 * @功能概要：
 * @项目名称： emp
 * @初创作者： chentingsheng <cts314@163.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-4-5 下午02:12:58
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */

public class MonAlarmDsmParams
{
	//手机号码
	private String phone;
	//短信内容
	private String msg;
	
	public MonAlarmDsmParams()
	{
		
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}
	
}
