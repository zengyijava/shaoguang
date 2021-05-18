package com.montnets.emp.appwg.wginterface;

import com.montnets.emp.entity.appmage.LfAppMwClient;

public interface IWgMwCommuteBiz
{
	/**
	 * 初始化梦网app平台接口
	 */
	public void InitMwApp();
	
	/**
	 * 停止app网关
	 */
	public void StopAppWg();
	
	/**
	 * 登录App平台
	 * @return 
	 		 1	登录成功。
	 		-1	未配置企业账户。
	 		-2	host或port为空。
	 		-3	用户名或密码为空。
	 		-4	初始化登录xmpp参数异常。
	 		-5	连接并登录xmpp异常。
	 		-8	未定义异常。
			401	用户名密码无效。
			408	用户请求超时。
			409	冲突错误。
			501	服务器不提供此功能。
			502	服务器内部错误。
			504	连接服务器超时。
			其他	不可恢复错误。
	 * 
	 */
	public int Login();
	
	
	/**
	 * 获取App平台企业编码
	 * @return 返回当前登录的企业编码
	 */
	public String getAppECode();
	
	/**
	 * 获取并同步用户
	 * @param uName 用户账号
	 * @param ecode 企业编码
	 * @return 成功则返回用户对象，异常返回null
	 */
	public LfAppMwClient SynUserInfo(String uName, String ecode);
	
	/**
	 * 服务器启动时登录
	 * @return
			 1	登录成功。
	 		-1	未配置企业账户。
	 		-2	host或port为空。
	 		-3	用户名或密码为空。
	 		-4	初始化登录xmpp参数异常。
	 		-5	连接并登录xmpp异常。
	 		-8	未定义异常。
			401	用户名密码无效。
			408	用户请求超时。
			409	冲突错误。
			501	服务器不提供此功能。
			502	服务器内部错误。
			504	连接服务器超时。
			其他	不可恢复错误。
	 */
	public int LoginForServiceStart();
}
