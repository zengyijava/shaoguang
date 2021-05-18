/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-6-25 上午10:30:12
 */
package com.montnets.emp.appmage.biz;

import java.util.HashMap;
import java.util.Map;

/**  APP发送接口返回状态
 * @description 
 * @project emp_std_183
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-6-25 上午10:30:12
 */

public class appImpResultStatus
{
	public final Map<Integer,String> resultInfoMap=new HashMap<Integer,String>();
	
	/**
	 *  APP发送接口返回状态
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-25 上午10:44:07
	 */
	public appImpResultStatus()
	{
		resultInfoMap.put(0,"企业账号未登录！");
		resultInfoMap.put(-1,"请求提交异常！");
		resultInfoMap.put(-2,"发送参数异常【MW：-2】！");//PMessageModel对象为null
		resultInfoMap.put(-3,"发送参数异常【MW：-3】！");//Ecode为null
		resultInfoMap.put(-4,"发送参数异常【MW：-4】！");//From为null
		resultInfoMap.put(-5,"发送参数异常【MW：-5】！");//To为null
		resultInfoMap.put(-6,"发送参数异常【MW：-6】！");//MessageStyles为null
		resultInfoMap.put(-100,"发送参数异常【MW：-100】！");//获取消息参数为null
	}
}
