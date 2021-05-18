package com.montnets.emp.appwg.initappplat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.app.EMessage;
import com.montnets.app.EMessageModel;
import com.montnets.app.PMessage;
import com.montnets.app.PMessageModel;
import com.montnets.emp.appwg.bean.AppStaticValue;
import com.montnets.emp.appwg.bean.WgMessage;
import com.montnets.emp.appwg.cache.RecMtCacheStorage;
import com.montnets.emp.appwg.cache.WinCacheStorage;
import com.montnets.emp.appwg.cache.Window;
import com.montnets.emp.appwg.dao.WgDAO;
import com.montnets.emp.appwg.initbuss.HandleMsgBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.appmage.LfAppMtmsg;
import com.montnets.emp.entity.appmage.LfAppSitInfo;

public class HandleRespMsgBIz extends SuperBiz
{
	
	private SimpleDateFormat sdfT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 公众消息响应处理
	 * @return
	 */
	public boolean RespEMRecHandle(EMessage eMsg){
		
		try
		{
			if(eMsg == null){
				EmpExecutionContext.error("处理公众消息响应失败，EMessage消息对象为null。");
				return false;
			}
			
			EmpExecutionContext.info("resp mt e:"+eMsg.toXML());
			EmpExecutionContext.appRequestInfoLog("resp mt e:"+eMsg.toXML());
			
			EMessageModel eMsgModel = eMsg.getEModel();
			if(eMsgModel == null){
				EmpExecutionContext.error("处理公众消息响应失败，EMessageModel消息对象为null。xml:"+eMsg.toXML());
				return false;
			}
			Boolean res = null;
			//消息中心响应app网关，且是通知消息
			if(eMsg.getIcode() == 2010 && eMsgModel.getEmtype() == 2){
				res = updateMtMsg(String.valueOf(eMsg.getRcode()), eMsg.getSerial(), eMsgModel.getTo());
				MtRespRecHandle(eMsg);
			}
			//消息中心响应app网关，且是设置首页消息 
			else if(eMsg.getIcode() == 2010 && eMsgModel.getEmtype() == 1){
				res = updateAppSitInfoReceipt(String.valueOf(eMsg.getRcode()), eMsg.getSerial(), eMsgModel.getId());
				MtRespRecHandle(eMsg);
			}
			EmpExecutionContext.info("更新公众消息响应记录结果："+res+";serial="+eMsg.getSerial());
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理公众消息响应异常。xml:"+(eMsg!=null? eMsg.toXML():""));
			return false;
		}
	}
	
	/**
	 * 个人状态报告消息处理，接收到状态报告消息会调用这方法
	 * @return
	 */
	public boolean RespPMRecHandle(PMessage pMsg){
		
		try
		{
			if(pMsg == null){
				EmpExecutionContext.error("接收并更新个人消息响应，PMessage对象为null。");
				return false;
			}
			EmpExecutionContext.info("resp mt p:"+pMsg.toXML());
			EmpExecutionContext.appRequestInfoLog("resp mt p:"+pMsg.toXML());
			
			PMessageModel pMsgModel = pMsg.getPModel();
			if(pMsgModel == null){
				EmpExecutionContext.error("接收并更新个人消息响应，PMessageModel对象为null。xml:"+pMsg.toXML());
				return false;
			}
			
			String state = "-1";
			boolean res = false;
			//回应
			if(pMsg.getIcode() == 2050){
				state = pMsg.getRcode().toString();
				res = updateMtMsg(state, pMsg.getSerial(), pMsgModel.getTo());
				MtRespRecHandle(pMsg);
			}
			
			EmpExecutionContext.info("更新个人消息响应到下行记录："+res+",serial="+pMsg.getSerial());
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "接收并更新个人消息响应异常。xml:"+(pMsg!=null? pMsg.toXML():""));
			return false;
		}
	}
	
	/**
	 * 更新app首页信息消息回执
	 * @param appMsg 消息参数
	 * @return 成功返回true
	 */
	private boolean updateAppSitInfoReceipt(String sendState, String serial, Long appId){
		try
		{
			if(sendState == null || sendState.trim().length() ==0 
			|| serial == null || serial.trim().length() == 0
			|| appId == null) 
			{
				EmpExecutionContext.error("更新app首页信息消息回执失败，参数为空。" +
						"sendState="+sendState+";serial="+serial+";appId="+appId);
				return false;
			}
			
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("sendState", sendState);
			
			//app平台传过来的id
			objectMap.put("msgId", appId.toString());
			
			String recTime = sdfT.format(new Date());
			objectMap.put("recTime", recTime);
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("serial", serial);
			
			Integer res = empDao.updateBySymbolsCondition(LfAppSitInfo.class, objectMap, conditionMap);
			if(res!=null && res > 0){
				return true;
			}
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新app首页信息消息回执异常。");
			return false;
		}
	}

	
	/**
	 * 更新app消息下行记录回执状态
	 * @param sendstate 更新的状态
	 * @param appMsgId 消息流水号
	 * @return 成功返回true
	 */
	private boolean updateMtMsg(String sendstate, String appMsgId, String tousername){
		try
		{
			if(sendstate == null || sendstate.trim().length() ==0) {
				EmpExecutionContext.error("更新App消息下行记录状态失败，状态为空。");
				return false;
			}
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("rptState", sendstate);
			
			String recTime = sdfT.format(new Date());
			objectMap.put("recRPTTime", recTime);
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("appMsgId", appMsgId);
			
			if(tousername != null && tousername.trim().length() > 0){
				//把分号换成逗号
				tousername = tousername.replace(";", ",");
				
				conditionMap.put("tousername&in", tousername);
			}
			
			Integer res = empDao.updateBySymbolsCondition(LfAppMtmsg.class, objectMap, conditionMap);
			if(res!=null && res > 0){
				return true;
			}
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新App消息下行记录状态异常。");
			return false;
		}
	}

	
	/**
	 * 个人消息响应
	 * @param pMsg
	 * @return
	 */
	private boolean MtRespRecHandle(PMessage pMsg){
		try
		{
			//从窗口取出对应记录
			Window win = WinCacheStorage.getInstance().getWindow(pMsg.getPacketID());
			if(win == null){
				return false;
			}
			
			boolean dealRes = false;
			//响应成功
			if(pMsg.getRcode() != null && pMsg.getRcode() == 0){
				//响应成功，则移除窗口
				WinCacheStorage.getInstance().removeWindow(pMsg.getPacketID());
				//更新数据库记录为成功
				dealRes = updateMtRespSucc(win.getMessage());
				
				return dealRes;
			}
			//响应失败
			else{
				//移除窗口
				WinCacheStorage.getInstance().removeWindow(pMsg.getPacketID());
				
				dealRes = mtRespFailDeal(win.getMessage());
				return dealRes;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理个人mt消息响应异常。");
			return false;
		}
	}
	
	/**
	 * 个人消息响应
	 * @param pMsg
	 * @return
	 */
	private boolean MtRespRecHandle(EMessage eMsg){
		try
		{
			//从窗口取出对应记录
			Window win = WinCacheStorage.getInstance().getWindow(eMsg.getPacketID());
			if(win == null){
				return false;
			}
			
			boolean dealRes = false;
			//响应成功
			if(eMsg.getRcode() != null && eMsg.getRcode() == 0){
				//响应成功，则移除窗口
				WinCacheStorage.getInstance().removeWindow(eMsg.getPacketID());
				//更新数据库记录为成功
				dealRes = updateMtRespSucc(win.getMessage());
				
				return dealRes;
			}
			//响应失败
			else{
				//移除窗口
				WinCacheStorage.getInstance().removeWindow(eMsg.getPacketID());
				
				dealRes = mtRespFailDeal(win.getMessage());
				return dealRes;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理个人mt消息响应异常。");
			return false;
		}
	}
	
	
	private boolean updateMtRespSucc(WgMessage wgMsg){
		try
		{
			//用于放缓存失败更新
			Map<String,String> objectMap = new HashMap<String,String>();
			objectMap.put("sendState", String.valueOf(AppStaticValue.SEND_STATE_SUCC));
			
			Map<String,String> conditionMap = new HashMap<String,String>();
			
			conditionMap.put("id", wgMsg.getId().toString());
			boolean updateRes = new WgDAO().updateMtDB(objectMap, conditionMap);
			return updateRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新mt响应为成功异常。");
			return false;
		}
	}
	
	public boolean mtRespFailDeal(WgMessage wgMsg){
		try
		{
			boolean res = false;
			
			//已达到重发次数
			if(wgMsg.getSendedCount() > AppStaticValue.RE_SEND_COUNT){
				EmpExecutionContext.error("mt消息重发次数超过"+AppStaticValue.RE_SEND_COUNT+"次，不能发送。serial="+wgMsg.getSerial());
				
				res = DealReSendFail(wgMsg);
				
				//重发3次都失败
				wgMsg.setErrCode("E1:0003");
				//仍然失败，发送失败状态报告
				sendRpt(wgMsg);
					
				return res;
			}
			
			//缓冲已满
			if(AppStaticValue.REC_MT_QUEUE_SIZE - RecMtCacheStorage.getInstance().getSize() == 0){
				//更新数据库记录为未读
				res = updateNoRead(wgMsg);
				return res;
			}
			
			//缓存未满
			//更新为已读待发送
			res = updateReadWaitSend(wgMsg);
			//更新成功，放缓存
			if(res){
				//再把消息放到发送队列，等下次发送
				boolean putRes = RecMtCacheStorage.getInstance().produceRecMt(wgMsg);
				//放缓存失败
				if(!putRes){
					res = updateNoRead(wgMsg);
				}
			}
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理失败的mt响应异常。");
			return false;
			
		}
	}
	
	public boolean sendRpt(WgMessage wgMsg){
		try
		{
			//公众消息
			if(wgMsg.getMsgType() == 1){
				wgMsg.setiCode(203);
			}
			//个人消息
			else if(wgMsg.getMsgType() == 2){
				wgMsg.setiCode(207);
			}
			wgMsg.setSendResult(1);
			wgMsg.setUserName(wgMsg.getTo());
			List<WgMessage> wgMsgList = new ArrayList<WgMessage>();
			wgMsgList.add(wgMsg);
			int res = new HandleMsgBiz().PutInRecRptCache(wgMsgList);
			if(res == 0){
				return true;
			}
			else{
				return false;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送失败提交状态报告异常。");
			return false;
		}
	}
	
	/**
	 * 处理重发失败
	 * @param wgMsg
	 * @return
	 */
	public boolean DealReSendFail(WgMessage wgMsg){
		try
		{
				//失败尝试3次，失败重连数据库
				int tryCount=0;
				
				do{
					boolean updateRes = updateSendState(wgMsg.getId(), AppStaticValue.SEND_STATE_FAIL);
					if(updateRes){
						return true;
					}else{
						Thread.sleep(2000);
					}
					tryCount++;
				}while(tryCount < 3);
				
				return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理重发失败异常。");
			return false;
			
		}
	}
	
	private boolean updateSendState(Long id, int state){
		try
		{
			//用于放缓存失败更新
			Map<String,String> objectMap = new HashMap<String,String>();
			objectMap.put("sendState", String.valueOf(state));
			
			Map<String,String> conditionMap = new HashMap<String,String>();
			
			conditionMap.put("id", id.toString());
			boolean updateRes = new WgDAO().updateMtDB(objectMap, conditionMap);
			return updateRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新mt消息发送状态异常。id="+id+",sendState="+state);
			return false;
		}
	}
	
	private boolean updateNoRead(WgMessage wgMsg){
		try
		{
			//用于放缓存失败更新
			Map<String,String> objectMap = new HashMap<String,String>();
			objectMap.put("sendState", String.valueOf(AppStaticValue.SEND_STATE_NOREAD));
			
			Map<String,String> conditionMap = new HashMap<String,String>();
			
			conditionMap.put("id", wgMsg.getId().toString());
			boolean updateRes = new WgDAO().updateMtDB(objectMap, conditionMap);
			return updateRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新发送消息发送异常。");
			return false;
		}
	}
	
	private boolean updateReadWaitSend(WgMessage wgMsg){
		try
		{
			//用于放缓存失败更新
			Map<String,String> objectMap = new HashMap<String,String>();
			objectMap.put("sendState", String.valueOf(AppStaticValue.SEND_STATE_WAIT_SEND));
			
			Map<String,String> conditionMap = new HashMap<String,String>();
			
			conditionMap.put("id", wgMsg.getId().toString());
			boolean updateRes = new WgDAO().updateMtDB(objectMap, conditionMap);
			return updateRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新发送消息发送异常。");
			return false;
		}
	}
	
}
