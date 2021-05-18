package com.montnets.emp.appwg.initbuss;

import java.util.HashMap;
import java.util.Map;

import com.montnets.emp.appwg.bean.AppStaticValue;
import com.montnets.emp.appwg.bean.WgMessage;
import com.montnets.emp.appwg.cache.RecRptCacheStorage;
import com.montnets.emp.appwg.dao.WgDAO;
import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 
 * @project emp_std_5.0
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-8-28 下午12:21:53
 * @description 发送线程
 */
public class SendRptThread extends Thread
{
	public SendRptThread(){
		this.setName("Rpt发送线程");
		//IDataAccessDriver dataAccessDriver = new DataAccessDriver();
		//empDao = dataAccessDriver.getEmpDAO();
		wgDao = new WgDAO();
		msgBiz = new HandleMsgBiz();
		rptQue = RecRptCacheStorage.getInstance();
	}
	
	//private IEmpDAO	empDao;
	private WgDAO wgDao;
	private HandleMsgBiz msgBiz;
	
	private boolean isSendRptThreadExit = true;
	private RecRptCacheStorage rptQue;
	//线程是否已启动，true为已启动
	private boolean isThreadStart = false;
	
	/**
	 * 线程是否已启动
	 * @return true为已启动
	 */
	public boolean isThreadStart()
	{
		return isThreadStart;
	}
	
	/**
	 * 线程是否已启动
	 * @return true为已启动
	 */
	public void setThreadStart(boolean isThreadStart)
	{
		this.isThreadStart = isThreadStart;
	}
	
	public void stopSendRptThread(){
		isSendRptThreadExit = false;
	}
	
	public final void run()
	{
		dealSend();
		
		//线程跳出了循环，则设置为未启动
		isThreadStart = false;
	}
	
	private void dealSend(){
		while(isSendRptThreadExit){
			try
			{
				//缓冲没信息
				if(rptQue.getSize() < 1){
					Thread.sleep(500);
					continue;
				}
				//从缓冲中获取消息
				WgMessage wgMsg = rptQue.consumeRecRpt();
				if(wgMsg == null){
					Thread.sleep(1000);
					continue;
				}
				
				if(wgMsg.getSendedCount() >= AppStaticValue.RE_SEND_COUNT){
					boolean delRes = dealReSendFail(wgMsg);
					EmpExecutionContext.error("rpt发送线程处理发送，消息重发次数超过"+AppStaticValue.RE_SEND_COUNT+"次，不能发送。更新数据库记录结果为"+delRes+"。serial="+wgMsg.getSerial());
					continue;
				}
				
				dealSendRpt(wgMsg);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "Rpt发送线程处理异常。");
			}
		}
	}
	
	/**
	 * 处理重发失败
	 * @param wgMsg
	 * @return
	 */
	private boolean dealReSendFail(WgMessage wgMsg){
		try
		{
				//失败尝试3次，失败重连数据库
				int tryCount=0;
				
				do{
					boolean updateRes = updateFail(wgMsg);
					if(updateRes){
						return true;
					}else{
						Thread.sleep(2000);
					}
					tryCount++;
				}while(tryCount < 3);
				
				//发状态报告
				return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理Rpt重发失败异常。");
			return true;
			
		}
	}
	
	private boolean updateFail(WgMessage wgMsg){
		try
		{
			//用于放缓存失败更新
			Map<String,String> objectMap = new HashMap<String,String>();
			objectMap.put("sendState", String.valueOf(AppStaticValue.SEND_STATE_FAIL));
			
			Map<String,String> conditionMap = new HashMap<String,String>();
			
			conditionMap.put("id", wgMsg.getId().toString());
			boolean updateRes = wgDao.updateRptDB(objectMap, conditionMap);
			return updateRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新rpt发送消息发送异常。");
			return false;
		}
	}
	
	private void dealSendRpt(WgMessage wgMsg){
		try
		{
			//发送前更新数据库
			boolean updateRes = updateSendSucc(wgMsg);
			if(!updateRes){
				
				EmpExecutionContext.error("发送rpt消息，发送前更新失败。");
				Thread.sleep(2000);
				return;
			}
			
			//发送消息
			int sendRes = rptSend(wgMsg);
			//发送次数+1
			wgMsg.setSendedCount(wgMsg.getSendedCount()+1);
			if(sendRes != 0){
				
				EmpExecutionContext.error("发送rpt消息，推送失败。serial="+wgMsg.getSerial());
				//发3次
				boolean sendAgainRes = sendFailAgain(wgMsg);
				//仍然失败
				if(!sendAgainRes){
					//更新为未读
					boolean uRes = updateRptSendState(wgMsg.getId(), AppStaticValue.SEND_STATE_NOREAD);
					EmpExecutionContext.error("发送mo消息，推送3次仍然失败。更新记录状态结果为"+uRes+",serial="+wgMsg.getSerial());
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送mo消息，线程处理异常。");
		}
	}
	
	/**
	 * 
	 * @param wgMsg
	 * @return 0为成功
	 */
	private int rptSend(WgMessage wgMsg){
		try
		{
			boolean res = false;
			//状态报告，个人消息
			if(wgMsg.getiCode() == 207){
				res = msgBiz.updateMtMsg(String.valueOf(wgMsg.getSendResult()),wgMsg.getSerial(), wgMsg.getUserName());
				if(!res){
					EmpExecutionContext.error("发送rpt消息。个人消息rpt更新失败。流水号："+wgMsg.getSerial());
					//return -1;
				}
				
				if(HandleMsgBiz.getProcRptmsgImpl() == null){
					EmpExecutionContext.error("发送rpt消息。状态报告接收处理接口实例为null。流水号："+wgMsg.getSerial());
					return -2;
				}
				//获取推送接口参数字符串
				String json = getRptJson(wgMsg);
				if(json == null || json.length() == 0){
					EmpExecutionContext.error("发送rpt消息。获取json为空。serial="+wgMsg.getSerial());
					return -6;
				}
				EmpExecutionContext.info("send rpt:"+json);
				EmpExecutionContext.appRequestInfoLog("send rpt:"+json);
				//推送状态报告
				res = HandleMsgBiz.getProcRptmsgImpl().processRptMessage(json);
				EmpExecutionContext.info("resp send rpt:"+res+",serial="+wgMsg.getSerial());
				EmpExecutionContext.appRequestInfoLog("resp send rpt:"+res+",serial="+wgMsg.getSerial());
				//推送成功
				if(res){
					return 0;
				}
				EmpExecutionContext.error("发送rpt消息。推送失败。serial="+wgMsg.getSerial()+",json="+json);
				//推送失败
				return -3;
			}
			//消息中心推送app网关，公众消息
			else if(wgMsg.getiCode() == 203){
				res = msgBiz.updateMtMsg(String.valueOf(wgMsg.getSendResult()), wgMsg.getSerial(), wgMsg.getUserName());
				//更新成功
				if(res){
					return 0;
				}
				EmpExecutionContext.error("发送rpt消息。公众消息rpt更新失败。serial="+wgMsg.getSerial());
				//更新失败
				return -4;
			}
			EmpExecutionContext.error("发送rpt消息。未知类型的状态报告，不处理。serial="+wgMsg.getSerial());
			//无法识别类型的状态报告
			return -5;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送rpt消息，异常。serial="+wgMsg.getSerial());
			return -99;
		}
	}
	
	/**
	 * 获取json格式字符串
	 * @param wgMsg 构造参数
	 * @return 返回json格式字符串，异常返回null
	 */
	private String getRptJson(WgMessage wgMsg){
		try
		{
			StringBuffer sb = new StringBuffer()
				.append("{")
				.append("\"msgid\":").append("\"").append(wgMsg.getSerial()).append("\"").append(",")
				.append("\"appacount\":").append("\"").append(wgMsg.getUserName()).append("\"").append(",")
				.append("\"state\":").append("\"").append(wgMsg.getSendResult()).append("\"").append(",")
				.append("\"errcode\":").append("\"").append(wgMsg.getErrCode()).append("\"")
				.append("}");
			
			return sb.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "状态报告获取json格式字符串异常。serial="+wgMsg.getSerial());
			return null;
		}
	}
	
	private boolean sendFailAgain(WgMessage wgMsg){
		
		try
		{
			//根据错误代码适当延时
			//发3次
			int count = 0;
			int sendRes = 0;
			do{
				Thread.sleep(1000);
				count++;
				sendRes = rptSend(wgMsg);
				if(sendRes == 0){
					return true;
				}
			}while(count < 3);
			
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送rpt消息，失败后马上重试提交异常。serial="+wgMsg.getSerial());
			return false;
		}
	}
	
	private boolean updateSendSucc(WgMessage wgMsg){
		try
		{
			//用于放缓存失败更新
			Map<String,String> objectMap = new HashMap<String,String>();
			objectMap.put("sendState", String.valueOf(AppStaticValue.SEND_STATE_SUCC));
			objectMap.put("sendedcount", wgMsg.getSendedCount().toString());
			
			Map<String,String> conditionMap = new HashMap<String,String>();
			
			conditionMap.put("id", wgMsg.getId().toString());
			boolean updateRes = wgDao.updateRptDB(objectMap, conditionMap);
			return updateRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新rpt消息发送状态为成功异常。id="+wgMsg.getId()+",serial="+wgMsg.getSerial());
			return false;
		}
	}
	
	/**
	 * 更新rpt发送状态
	 * @param id
	 * @param state
	 * @return
	 */
	private boolean updateRptSendState(Long id, int state){
		try
		{
			//用于放缓存失败更新
			Map<String,String> objectMap = new HashMap<String,String>();
			objectMap.put("sendState", String.valueOf(state));
			
			Map<String,String> conditionMap = new HashMap<String,String>();
			
			conditionMap.put("id", id.toString());
			boolean updateRes = wgDao.updateRptDB(objectMap, conditionMap);
			return updateRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新rpt消息发送状态异常。id="+id+",sendState="+state);
			return false;
		}
	}
	
	
	
}
