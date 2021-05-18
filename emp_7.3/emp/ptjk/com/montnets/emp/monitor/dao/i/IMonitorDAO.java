/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-30 上午11:51:40
 */
package com.montnets.emp.monitor.dao.i;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

/**监控DAO接口
 * @description
 * @project p_comm
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-30 上午11:51:40
 */

public interface IMonitorDAO
{
	/**
	 * 获取消息中心服务器配置信息
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-16 上午10:06:50
	 */
	public String[] getMqServerInfo();

	/**
	 * 设置通道账号余额至缓存
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-17 下午03:43:31
	 */
	public void setGateAccoutFee();

	/**
	 * 获取通道账号网关编号
	 * 
	 * @description
	 * @param gateAccount
	 *        通道账号
	 * @return 网关编号
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-2-18 下午04:52:41
	 */
	public Long getGatewayId(String gateAccount);

	/**
	 * 获取网优通道及SIM卡信息
	 * 
	 * @description
	 * @return 0:通道名称;1:IPCOM IP; 2:IPCOM 端口; 3:SIM卡号码
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-2 下午08:52:57
	 */
	public String[] getIpcomAndSimInfo();

	/**
	 * 告警短信发送记录是否小于3分钟未收到状态报告
	 * 
	 * @description
	 * @param msgId
	 * @return true 是; false 否
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-9 下午06:09:35
	 */
	public String getAlarmSmsRecord(String msgId);
	
	/**
	 * 告警短信发送记录发送失败,未收到状态报告并且记录大于等于3分钟小于6分钟
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-9 下午07:21:27
	 */
	public List<DynaBean> getAlarmSmsRecord();
	
	/**
	 * 获取监控配置信息，包括页面刷新时间 、数据分析间隔时间、网络异常时间间隔
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-11 上午10:55:38
	 */
	public List<DynaBean> getMonConfig();
	
	/**
	 * 查询告警短信滞留记录
	 * @description    
	 * @param mtMsgid 消息流水号
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-17 上午11:19:26
	 */
	public List<DynaBean> getAlarmSmsRemained(String mtMsgid);
}
