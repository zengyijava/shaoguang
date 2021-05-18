/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-2 下午01:43:08
 */
package com.montnets.emp.monitor.biz.i;


/**设置监控基本信息BIZ接口
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-2 下午01:43:08
 */

public interface IMonitorBaseInfoBiz
{
	/**
	 * 在线用户告警设置表信息写入缓存
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-30 下午06:55:42
	 */
	public void setOnlineCfgInfo();

	/**
	 * 设置主机基本信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-2 下午01:55:38
	 */
	public void setHostBaseInfo();

	/**
	 * 设置程序基础信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-2 下午02:14:57
	 */
	public void setProceBaseInfo();

	/**
	 * 设置SP账号基础信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-2 下午02:19:06
	 */
	public void setspAcBaseInfo();

	/**
	 * 设置通道账号基础信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-2 下午02:22:00
	 */
	public void setGateAcBaseInfo();

	/**
	 * 设置实时告警信息
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-15 下午04:37:34
	 */
	public void setMonErrorInfo();
	
	/**
	 * 设置消息中心服务器信息至缓存
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-16 上午10:11:23
	 */
	public void setMqServerBaseInfo();
	
	/**
	 * 设置监控配置信息，包括页面刷新时间 、数据分析间隔时间、网络异常时间间隔
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-11 上午10:58:09
	 */
	public void setMonConfig();
	
	/**
	 * 设置通道账号余额
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-11-7 下午03:05:44
	 */
	public void setGateAccountFee();
	
	
	/**
	 * 获取WEB服务器名称
	 * @description    
	 * @param ServerInfo       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-11-13 下午03:18:00
	 */
	public void getServerName(String ServerInfo);

	/**
	 * 执行监控定时任务
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-28 上午10:04:54
	 */
	public void executeMonTimeJob();
	
	/**
	 * 删除半年前告警短信记录定时任务
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-13 上午11:23:04
	 */
	public void executedelAlarmSmsTask();
	
}
