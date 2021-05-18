package com.montnets.emp.appwg.wginterface;

public interface IProcPMessage
{
	/**
	 * 个人消息接收处理接口实例
	 * @param json json格式字符串
	 * @return 处理成功返回true
	 */
	public boolean processPMessage(String json);
}
