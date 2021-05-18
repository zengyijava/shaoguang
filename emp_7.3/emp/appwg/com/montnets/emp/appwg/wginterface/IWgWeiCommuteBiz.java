package com.montnets.emp.appwg.wginterface;

import com.montnets.emp.appwg.bean.AppMessage;

public interface IWgWeiCommuteBiz
{
	/**
	 * 发送微信消息
	 * @param appMsg 消息对象
	 * @return
	 * 		  000：	提交成功。
	 * 		   -2：	获取发送json格式字符串为空
	 * 		   -3：	发送微信消息异常
	 * 		-9999：	提交失败。
	 */
	public String SendWeixMsg(AppMessage appMsg);
	
}
