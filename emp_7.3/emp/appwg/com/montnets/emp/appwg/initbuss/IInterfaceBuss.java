package com.montnets.emp.appwg.initbuss;

import com.montnets.emp.appwg.bean.AppMessage;
import com.montnets.emp.appwg.wginterface.IProcPMessage;
import com.montnets.emp.appwg.wginterface.IProcRptMessage;

public interface IInterfaceBuss
{
	
	
	/**
	 * 发送APP消息
	 * @param appMsg
	 * @return 
	 * 1:提交成功。
	 * 0：未登录。
	 * -1:提交失败。
	 * -2:appMsg对象为null。
	 * -3:Ecode为null。
	 * -4:FromUserName为null。
	 * -5:ToUserNameSet为null。
	 * -6:发送参数集合对象为null。
	 * -7:下行消息集合对象为null。
	 * -8：保存消息记录异常。
	 * -9：获取并保存消息记录异常。
	 * 
	 */
	public int SendAppPublicMsg(AppMessage appMsg);
	
	/**
	 * 发送客服消息
	 * @param pMessageModel 参数。
	 * @return 
	 * 1:提交成功
	 * 0：未登录
	 * -1:提交失败；
	 * -2:PMessageModel对象为null；
	 * -3:Ecode为null；
	 * -4:From为null
	 * -5:To为null
	 * -6:MessageStyles为null
	 * -100:获取消息参数为null
	 */
	public int SendAppPersionMsg(String json);
	
	/**
	 * 设置个人消息接收处理接口实例
	 * @param procPMessageImpl 个人消息接收处理接口实例
	 * @return 成功返回true
	 */
	public boolean SetIProcPMessage(IProcPMessage procPMessageImpl);
	
	/**
	 * 设置状态报告消息接收处理接口实例
	 * @param procRptMessageImpl 个人消息接收处理接口实例
	 * @return 成功返回true
	 */
	public boolean SetIProcRptMessage(IProcRptMessage procRptMessageImpl);
	
	/**
	 * 发送APP设置首页消息
	 * @param appMsg 消息对象，必填Sid、MainPageTopList和MainPageTitleList，选填validity
	 * @return 
	 * 1:提交成功。
	 * 0：未登录。
	 * -1:提交失败。
	 * -2:appMsg对象为null。
	 * -3:Sid为null。
	 * -4:更新app首页信息失败。
	 * -5:app首页信息对象为空。
	 * -6:获取并更新app首页信息对象异常。
	 * -8:主页内容为空。
	 */
	public int SetAppMainPage(AppMessage appMsg);
	
	/**
	 * 发送取消APP设置首页消息
	 * @param appMsg 消息对象，必填Sid
	 * @return 
	 * 1:提交成功。
	 * 0：未登录。
	 * -1:提交失败。
	 * -2:appMsg对象为null。
	 * -3:Sid为null。
	 * -5:app首页信息对象为空。
	 * -30:流水号为空。
	 * -31:获取和构建消息对象异常。
	 */
	public int CancelSetMainPage(AppMessage appMsg);
	
	/**
	 * 获取App平台企业编码
	 * @return 返回当前登录的企业编码
	 */
	public String getAppECode();
	
	
	
}
