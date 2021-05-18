/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-4-8 下午06:59:35
 */
package com.montnets.emp.monitor.biz.i;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;


/**告警短信BIZ接口
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-4-8 下午06:59:35
 */

public interface IAlarmSmsBiz
{
	
	/**
	 * 以mtmsgid为条件查询MT_TASK表对应记录并更新至网优告警短信发送记录表中，默认状态为0：失败
	 * @description    
	 * @param msgidStr 消息号
	 * @param phoneList   发送号码列表    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-8 下午05:06:59
	 */
	public void batchAddAlarmSmsRecord(String msgidStr, String[]phoneList, String msg);
	
	/**
	 * 新增告警短信发送记录
	 * @description    
	 * @param phoneList	发送号码
	 * @return       	自增ID		 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-8 下午05:10:18
	 */
	public String addAlSmsRecordReturnId(String[]phoneList, String msg, String[] haoduans);
	
	/**
	 * 更新告警短信发送记录为1:成功
	 * @description    
	 * @param id   自增ID或MSGID  
	 * @param condition   更新条件 0:ID;1;MSGID  	
	 * @param sendFlag 发送标志(0:未接收到状态报告；1：已接收到状态报告；2：网优发送)
	 * @param sendStatus 发送状态(0:失败；1：成功)
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-8 下午05:20:58
	 */
	public void setAlarmSmsRecord(String id, int condition, String sendFlag, String sendStatus);
	
	/**
	 * 获取小于等于3分钟告警短信发送记录短信内容
	 * @description    
	 * @param msgId
	 * @return   true 是; false 否    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-9 下午06:09:35
	 */
	public String getAlarmSmsRecord(String msgId);
	
	/**
	 * 告警短信发送记录发送失败,未收到状态报告并且大于等于3分钟小于6分钟未收到状态报告
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-9 下午07:21:27
	 */
	public List<DynaBean> getAlarmSmsRecord();
}
