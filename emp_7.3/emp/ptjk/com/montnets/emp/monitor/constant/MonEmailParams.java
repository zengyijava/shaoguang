package com.montnets.emp.monitor.constant;

/**
 * 
 * @功能概要：邮件发送实体类
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-4-6 下午04:03:56
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class MonEmailParams
{
	//邮件内容
	protected String content;
	//公司编码
	protected String corpCode;
	//邮件地址
	protected String email;
	
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public String getCorpCode()
	{
		return corpCode;
	}
	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	
}
