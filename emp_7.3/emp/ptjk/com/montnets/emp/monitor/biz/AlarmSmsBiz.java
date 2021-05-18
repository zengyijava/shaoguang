/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-4-8 下午06:59:35
 */
package com.montnets.emp.monitor.biz;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.MonAlarmDsmParams;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.monitor.LfAlsmsrecord;
import com.montnets.emp.entity.sms.MtTask;
import com.montnets.emp.monitor.biz.i.IAlarmSmsBiz;
import com.montnets.emp.monitor.dao.MonitorDAO;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.PhoneUtil;


/**
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-4-8 下午06:59:35
 */

public class AlarmSmsBiz extends SuperBiz implements IAlarmSmsBiz
{
	BaseBiz	baseBiz	= new BaseBiz();
	
	MonitorDAO monitorDAO = new MonitorDAO();
	
	PhoneUtil phoneUtil = new PhoneUtil();
	
	/**
	 * 以mtmsgid为条件查询MT_TASK表对应记录并更新至网优告警短信发送记录表中，默认状态为0：失败
	 * @description    
	 * @param msgidStr 消息号
	 * @param phoneList   发送号码列表    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-8 下午05:06:59
	 */
	public void batchAddAlarmSmsRecord(String msgidStr, String[]phoneList, String msg)
	{
		Long msgid = 0L;
		if(msgidStr != null && !"".equals(msgidStr))
		{
			msgid = Long.parseLong(msgidStr);
		}
		else
		{
			EmpExecutionContext.error("提交网关成功，获取网关返回消息msgid失败，msgid：" + msgidStr);
			return;
		}
		if(msgid != 0)
		{
			StringBuffer mtMsgId = new StringBuffer();
			mtMsgId.append(msgid).append(",");
			int count = phoneList.length;
			//根据号码个数,获取所有msgid
			for(int i=1; i<count; i++)
			{
				msgid += 1;
				mtMsgId.append(msgid).append(",");
			}
			//去掉最后一个逗号
			String mtMsgidStr = mtMsgId.substring(0, mtMsgId.lastIndexOf(",")).toString();
			try
			{
				// 查询MT_TASK表记录
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("ptMsgId&in", mtMsgidStr);
				List<MtTask> mtTaskList = baseBiz.getByCondition(MtTask.class, conditionMap, null);
				Iterator<MtTask> itr = mtTaskList.iterator();
				
				MtTask mtTaskInfo = null;
				List<LfAlsmsrecord>  sendrecordList = new ArrayList<LfAlsmsrecord>();
				while(itr.hasNext())
				{
					mtTaskInfo = itr.next();
					LfAlsmsrecord sendrecord = new LfAlsmsrecord();
					sendrecord.setMsgid((mtTaskInfo.getPtMsgId()!=null? mtTaskInfo.getPtMsgId():0L));
					sendrecord.setUserid(mtTaskInfo.getUserId());
					sendrecord.setSpgate(mtTaskInfo.getSpgate());
					sendrecord.setPhone(mtTaskInfo.getPhone());
					//发送状态（0：失败；1：成功），默认失败
					sendrecord.setSendstatus(0);
					//发送标志(0:未接收到状态报告；1：已接收到状态报告；2：网优发送)
					sendrecord.setSendflag(0);
					sendrecord.setErrorcode(mtTaskInfo.getErrorCode());
					sendrecord.setUnicom(mtTaskInfo.getUnicom().intValue());
					sendrecord.setSendtime(new Timestamp(System.currentTimeMillis()));
					sendrecord.setRecvtime(new Timestamp(System.currentTimeMillis()));
					sendrecord.setMessage(msg);
					sendrecordList.add(sendrecord);
				}
				//MT_TASK表中查询记录总数不等于号码个数，则在滞留表中再查询一次
				if(count != sendrecordList.size())
				{
					List<DynaBean> alarmSmsRemainedList = monitorDAO.getAlarmSmsRemained(mtMsgidStr);
					if(alarmSmsRemainedList != null && alarmSmsRemainedList.size()>0)
					{
						String[] haoduans = new WgMsgConfigBiz().getHaoduan();
						for(DynaBean alarmSmsRemained:alarmSmsRemainedList)
						{
							String phone = alarmSmsRemained.get("phone").toString();
							String ptMsgIdStr = alarmSmsRemained.get("ptmsgid").toString();
							Long ptMsgId = ptMsgIdStr!=null&&!"".equals(ptMsgIdStr)?Long.parseLong(ptMsgIdStr):0L;
							String userId = alarmSmsRemained.get("userid").toString();
							String spGate = alarmSmsRemained.get("spgate").toString();
							
							//一条记录多个号码
							if(phone.indexOf(",") > -1)
							{
								String[] rePhoneList = phone.split(",");
								for(int i=0; i<rePhoneList.length; i++)
								{
									//运营商标识
									int unicom = phoneUtil.getPhoneType(rePhoneList[i], haoduans);
									LfAlsmsrecord sendrecords = new LfAlsmsrecord();
									if(unicom == 2)
									{
										unicom = 21;
									}
									else if(unicom == 3)
									{
										unicom = 5;
									}
									else if(unicom == -1)
									{
										unicom = 0;
									}
									if(i > 0)
									{
										ptMsgId += 1;
									}
									sendrecords.setUserid(userId);
									sendrecords.setSpgate(spGate);
									//发送状态（0：失败；1：成功），默认失败
									sendrecords.setSendstatus(0);
									//发送标志(0:未接收到状态报告；1：已接收到状态报告；2：网优发送)
									sendrecords.setSendflag(0);
									sendrecords.setErrorcode("0");
									sendrecords.setSendtime(new Timestamp(System.currentTimeMillis()));
									sendrecords.setMessage(alarmSmsRemained.get("message").toString());
									sendrecords.setMsgid(ptMsgId);
									sendrecords.setUnicom(unicom);
									sendrecords.setPhone(rePhoneList[i]);
									sendrecordList.add(sendrecords);
								}
							}
							else
							{
								LfAlsmsrecord sendrecords = new LfAlsmsrecord();
								//运营商标识
								int unicom = phoneUtil.getPhoneType(phone, haoduans);
								if(unicom == 2)
								{
									unicom = 21;
								}
								else if(unicom == 3)
								{
									unicom = 5;
								}
								else if(unicom == -1)
								{
									unicom = 0;
								}
								sendrecords.setUserid(userId);
								sendrecords.setSpgate(spGate);
								//发送状态（0：失败；1：成功），默认失败
								sendrecords.setSendstatus(0);
								//发送标志(0:未接收到状态报告；1：已接收到状态报告；2：网优发送)
								sendrecords.setSendflag(0);
								sendrecords.setErrorcode("0");
								sendrecords.setSendtime(new Timestamp(System.currentTimeMillis()));
								sendrecords.setMessage(alarmSmsRemained.get("message").toString());
								sendrecords.setMsgid(ptMsgId);
								sendrecords.setUnicom(unicom);
								sendrecords.setPhone(phone);
								sendrecordList.add(sendrecords);
							}
						}
					}
				}
				
				if(sendrecordList != null && sendrecordList.size() > 0)
				{
					//更新告警短信发送记录
					baseBiz.addList(LfAlsmsrecord.class, sendrecordList);
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "告警短信提交网关成功，更新记录表异常！");
			}
		}
	}
	
	/**
	 * 以mtmsgid为条件查询MT_TASK表对应记录并更新至网优告警短信发送记录表中，默认状态为0：失败
	 * @description    
	 * @param msgidStr
	 * @param batchAlarmDsmList       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-5 下午03:52:14
	 */
	public void batchAddAlarmDsmRecord(String msgidStr, List<MonAlarmDsmParams> batchAlarmDsmList)
	{
		Long msgid = 0L;
		if(msgidStr != null && !"".equals(msgidStr))
		{
			msgid = Long.parseLong(msgidStr);
		}
		else
		{
			EmpExecutionContext.error("提交网关成功，获取网关返回消息msgid失败，msgid：" + msgidStr);
			return;
		}
		if(msgid != 0)
		{
			StringBuffer mtMsgId = new StringBuffer();
			mtMsgId.append(msgid).append(",");
			int count = batchAlarmDsmList.size();
			//根据号码个数,获取所有msgid
			for(int i=1; i<count; i++)
			{
				msgid += 1;
				mtMsgId.append(msgid).append(",");
			}
			//去掉最后一个逗号
			String mtMsgidStr = mtMsgId.substring(0, mtMsgId.lastIndexOf(",")).toString();
			try
			{
				// 查询MT_TASK表记录
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("ptMsgId&in", mtMsgidStr);
				List<MtTask> mtTaskList = baseBiz.getByCondition(MtTask.class, conditionMap, null);
				Iterator<MtTask> itr = mtTaskList.iterator();
				
				MtTask mtTaskInfo = null;
				List<LfAlsmsrecord>  sendrecordList = new ArrayList<LfAlsmsrecord>();
				while(itr.hasNext())
				{
					mtTaskInfo = itr.next();
					LfAlsmsrecord sendrecord = new LfAlsmsrecord();
					sendrecord.setMsgid((mtTaskInfo.getPtMsgId()!=null? mtTaskInfo.getPtMsgId():0L));
					sendrecord.setUserid(mtTaskInfo.getUserId());
					sendrecord.setSpgate(mtTaskInfo.getSpgate());
					sendrecord.setPhone(mtTaskInfo.getPhone());
					//发送状态（0：失败；1：成功），默认失败
					sendrecord.setSendstatus(0);
					//发送标志(0:未接收到状态报告；1：已接收到状态报告；2：网优发送)
					sendrecord.setSendflag(0);
					sendrecord.setErrorcode(mtTaskInfo.getErrorCode());
					sendrecord.setUnicom(mtTaskInfo.getUnicom().intValue());
					sendrecord.setSendtime(new Timestamp(System.currentTimeMillis()));
					sendrecord.setRecvtime(new Timestamp(System.currentTimeMillis()));
					sendrecord.setMessage(mtTaskInfo.getMessage());
					sendrecordList.add(sendrecord);
				}
				//MT_TASK表中查询记录总数不等于号码个数，则在滞留表中再查询一次
				if(count != sendrecordList.size())
				{
					List<DynaBean> alarmSmsRemainedList = monitorDAO.getAlarmSmsRemained(mtMsgidStr);
					if(alarmSmsRemainedList != null && alarmSmsRemainedList.size()>0)
					{
						String[] haoduans = new WgMsgConfigBiz().getHaoduan();
						for(DynaBean alarmSmsRemained:alarmSmsRemainedList)
						{
							String phone = alarmSmsRemained.get("phone").toString();
							String ptMsgIdStr = alarmSmsRemained.get("ptmsgid").toString();
							Long ptMsgId = ptMsgIdStr!=null&&!"".equals(ptMsgIdStr)?Long.parseLong(ptMsgIdStr):0L;
							String userId = alarmSmsRemained.get("userid").toString();
							String spGate = alarmSmsRemained.get("spgate").toString();
							
							//一条记录多个号码
							if(phone.indexOf(",") > -1)
							{
								String[] rePhoneList = phone.split(",");
								for(int i=0; i<rePhoneList.length; i++)
								{
									//运营商标识
									int unicom = phoneUtil.getPhoneType(rePhoneList[i], haoduans);
									LfAlsmsrecord sendrecords = new LfAlsmsrecord();
									if(unicom == 2)
									{
										unicom = 21;
									}
									else if(unicom == 3)
									{
										unicom = 5;
									}
									else if(unicom == -1)
									{
										unicom = 0;
									}
									if(i > 0)
									{
										ptMsgId += 1;
									}
									sendrecords.setUserid(userId);
									sendrecords.setSpgate(spGate);
									//发送状态（0：失败；1：成功），默认失败
									sendrecords.setSendstatus(0);
									//发送标志(0:未接收到状态报告；1：已接收到状态报告；2：网优发送)
									sendrecords.setSendflag(0);
									sendrecords.setErrorcode("0");
									sendrecords.setSendtime(new Timestamp(System.currentTimeMillis()));
									sendrecords.setMessage(alarmSmsRemained.get("message").toString());
									sendrecords.setMsgid(ptMsgId);
									sendrecords.setUnicom(unicom);
									sendrecords.setPhone(rePhoneList[i]);
									sendrecordList.add(sendrecords);
								}
							}
							else
							{
								LfAlsmsrecord sendrecords = new LfAlsmsrecord();
								//运营商标识
								int unicom = phoneUtil.getPhoneType(phone, haoduans);
								if(unicom == 2)
								{
									unicom = 21;
								}
								else if(unicom == 3)
								{
									unicom = 5;
								}
								else if(unicom == -1)
								{
									unicom = 0;
								}
								sendrecords.setUserid(userId);
								sendrecords.setSpgate(spGate);
								//发送状态（0：失败；1：成功），默认失败
								sendrecords.setSendstatus(0);
								//发送标志(0:未接收到状态报告；1：已接收到状态报告；2：网优发送)
								sendrecords.setSendflag(0);
								sendrecords.setErrorcode("0");
								sendrecords.setSendtime(new Timestamp(System.currentTimeMillis()));
								sendrecords.setMessage(alarmSmsRemained.get("message").toString());
								sendrecords.setMsgid(ptMsgId);
								sendrecords.setUnicom(unicom);
								sendrecords.setPhone(phone);
								sendrecordList.add(sendrecords);
							}
						}
					}
				}
				
				if(sendrecordList != null && sendrecordList.size() > 0)
				{
					//更新告警短信发送记录
					baseBiz.addList(LfAlsmsrecord.class, sendrecordList);
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "告警短信提交网关成功，更新记录表异常！");
			}
		}
	}
	
	
	/**
	 * 新增告警短信发送记录
	 * @description    
	 * @param phoneList	发送号码
	 * @return       	自增ID		 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-8 下午05:10:18
	 */
	public String addAlSmsRecordReturnId(String[]phoneList, String msg, String[] haoduans)
	{
		//自增ID
		StringBuffer id = new StringBuffer("");
		
		// 向LF_WYSendRecord表插入数据
		for(int i=0; i < phoneList.length; i++)
		{
			LfAlsmsrecord sendrecord = new LfAlsmsrecord();
			sendrecord.setMsgid(0L);
			sendrecord.setUserid("0");
			sendrecord.setSpgate("0");
			sendrecord.setPhone(phoneList[i]);
			//发送状态（0：失败；1：成功），默认失败
			sendrecord.setSendstatus(0);
			//发送标志(0:未接收到状态报告；1：已接收到状态报告；2：网优发送)
			sendrecord.setSendflag(2);
			sendrecord.setErrorcode("WYSEND");
			//运营商标识
			int unicom = phoneUtil.getPhoneType(phoneList[i], haoduans);
			if(unicom == 2)
			{
				unicom = 21;
			}
			else if(unicom == 3)
			{
				unicom = 5;
			}
			else if(unicom == -1)
			{
				unicom = 0;
			}
			sendrecord.setUnicom(unicom);
			sendrecord.setSendtime(new Timestamp(System.currentTimeMillis()));
			sendrecord.setMessage(msg);
			try
			{
				//返回自增ID
				id.append(baseBiz.addObjReturnId(sendrecord)).append(",");
			}
			catch (Exception e)
			{
				id.append(-1L);
				EmpExecutionContext.error(e, "告警短信发送记录更新失败！告警手机号码：" + phoneList[i]);
			}
		}
		return id.toString();
	}
	
	/**
	 * 新增告警短信发送记录
	 * @description    
	 * @param phoneList
	 * @param msg
	 * @param haoduans
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-5 下午03:54:50
	 */
	public String addDsmAlSmsRecordReturnId(List<MonAlarmDsmParams> batchAlarmDsmList, String[] haoduans)
	{
		//自增ID
		StringBuffer id = new StringBuffer("");
		
		// 向LF_WYSendRecord表插入数据
		for(int i=0; i < batchAlarmDsmList.size(); i++)
		{
			LfAlsmsrecord sendrecord = new LfAlsmsrecord();
			sendrecord.setMsgid(0L);
			sendrecord.setUserid("0");
			sendrecord.setSpgate("0");
			sendrecord.setPhone(batchAlarmDsmList.get(i).getPhone());
			//发送状态（0：失败；1：成功），默认失败
			sendrecord.setSendstatus(0);
			//发送标志(0:未接收到状态报告；1：已接收到状态报告；2：网优发送)
			sendrecord.setSendflag(2);
			sendrecord.setErrorcode("WYSEND");
			//运营商标识
			int unicom = phoneUtil.getPhoneType(batchAlarmDsmList.get(i).getPhone(), haoduans);
			if(unicom == 2)
			{
				unicom = 21;
			}
			else if(unicom == 3)
			{
				unicom = 5;
			}
			else if(unicom == -1)
			{
				unicom = 0;
			}
			sendrecord.setUnicom(unicom);
			sendrecord.setSendtime(new Timestamp(System.currentTimeMillis()));
			sendrecord.setMessage(batchAlarmDsmList.get(i).getMsg());
			try
			{
				//返回自增ID
				id.append(baseBiz.addObjReturnId(sendrecord)).append(",");
			}
			catch (Exception e)
			{
				id.append(-1L);
				EmpExecutionContext.error(e, "告警短信发送记录更新失败！phone:" + batchAlarmDsmList.get(i).getPhone()+"，msg:"+batchAlarmDsmList.get(i).getMsg());
			}
		}
		return id.toString();
	}
	
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
	public void setAlarmSmsRecord(String id, int condition, String sendFlag, String sendStatus)
	{
		try
		{
			// 更新短信告警发送记录表，修改状态为1：成功
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			String key = "id";
			//条件为1时，以msgid为条件，为0时，以ID为条件
			if(condition == 1)
			{
				key = "msgid";
			}
			conditionMap.put(key, id);
			//objectMap.put("errorcode", "DELIVRD");
			objectMap.put("sendflag", sendFlag);
			objectMap.put("sendstatus", sendStatus);
			baseBiz.update(MonDbConnection.getInstance().getConnection(), LfAlsmsrecord.class, objectMap, conditionMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新告警短信发送记录状态失败！");
		}
	}
	
	/**
	 * 获取小于等于3分钟告警短信发送记录短信内容
	 * @description    
	 * @param msgId
	 * @return   true 是; false 否    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-9 下午06:09:35
	 */
	public String getAlarmSmsRecord(String msgId)
	{
		return monitorDAO.getAlarmSmsRecord(msgId);
	}
	
	/**
	 * 告警短信发送记录发送失败,未收到状态报告并且大于等于3分钟小于6分钟未收到状态报告
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-9 下午07:21:27
	 */
	public List<DynaBean> getAlarmSmsRecord()
	{
		return monitorDAO.getAlarmSmsRecord();
	}
}
