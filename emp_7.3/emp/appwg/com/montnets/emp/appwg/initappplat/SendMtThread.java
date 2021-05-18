package com.montnets.emp.appwg.initappplat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.montnets.app.EMessage;
import com.montnets.app.EMessageModel;
import com.montnets.app.PMessage;
import com.montnets.app.PMessageModel;
import com.montnets.app.style.EMessageStyle;
import com.montnets.app.style.EMessageStyle1;
import com.montnets.app.style.EMessageStyle10;
import com.montnets.app.style.EMessageStyle2;
import com.montnets.app.style.EMessageStyle8;
import com.montnets.app.style.EMessageStyle9;
import com.montnets.app.style.PMessageStyle;
import com.montnets.app.style.PMessageStyle1;
import com.montnets.app.style.PMessageStyle2;
import com.montnets.app.style.PMessageStyle3;
import com.montnets.app.style.PMessageStyle4;
import com.montnets.emp.appwg.bean.AppErrCode;
import com.montnets.emp.appwg.bean.AppStaticValue;
import com.montnets.emp.appwg.bean.WgMessage;
import com.montnets.emp.appwg.cache.RecMtCacheStorage;
import com.montnets.emp.appwg.cache.WinCacheStorage;
import com.montnets.emp.appwg.dao.WgDAO;
import com.montnets.emp.appwg.util.JsonUtil;
import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 
 * @project emp_std_5.0
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-8-28 下午12:21:53
 * @description 发送线程
 */
public class SendMtThread extends Thread
{
	public SendMtThread(){
		this.setName("Mt发送线程");
		//IDataAccessDriver dataAccessDriver = new DataAccessDriver();
		//empDao = dataAccessDriver.getEmpDAO();
		wgDao = new WgDAO();
		respBiz = new HandleRespMsgBIz();
		mtQue = RecMtCacheStorage.getInstance();
		//handleBiz = new HandleMsgBiz();
	}
	
	//private IEmpDAO	empDao;
	private WgDAO wgDao;
	private HandleRespMsgBIz respBiz;
	//private HandleMsgBiz handleBiz;
	
	private boolean isSendMtThreadExit = true;
	private RecMtCacheStorage mtQue;
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


	public void stopSendMtThread(){
		isSendMtThreadExit = false;
	}
	
	public final void run()
	{
		dealSend();
		
		//线程跳出了循环，则设置为未启动
		isThreadStart = false;
	}
	
	private void dealSend(){
		while(isSendMtThreadExit){
			try
			{
				//缓冲没信息
				if(mtQue.getSize() < 1){
					Thread.sleep(500);
					continue;
				}
				
				//网络不正常
				if(AppSdkPackage.getInstance().getXmppConnState() != 1){
					Thread.sleep(2000);
					continue;
				}
				
				//从缓冲中获取消息
				WgMessage wgMsg = mtQue.consumeRecMt();
				if(wgMsg == null){
					Thread.sleep(1000);
					continue;
				}
				
				if(wgMsg.getSendedCount() >= AppStaticValue.RE_SEND_COUNT){
					
					boolean delRes = respBiz.DealReSendFail(wgMsg);
					
					//重发3次都失败
					wgMsg.setErrCode(AppErrCode.E10003);
					//仍然失败，发送失败状态报告
					boolean sendRpt = respBiz.sendRpt(wgMsg);
					EmpExecutionContext.error("mt发送线程处理发送，消息重发次数超过"+AppStaticValue.RE_SEND_COUNT+"次，不能发送。更新数据库记录结果为"+delRes+"发送状态报告结果为"+sendRpt+"。serial="+wgMsg.getSerial());
					continue;
				}
				
				//公众消息
				if(wgMsg.getMsgType() == 1){
					dealSendE(wgMsg);
				}
				//个人消息
				else if(wgMsg.getMsgType() == 2){
					dealSendP(wgMsg);
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "发送线程处理异常。");
			}
		}
	}
	
	private void dealSendE(WgMessage wgMsg){
		
		try
		{
			EMessage emsg = getEMessage(wgMsg);
			if(emsg == null){
				//更新为发送失败
				boolean uRes = updateSendFail(wgMsg);
				//获取EMessage对象失败
				wgMsg.setErrCode(AppErrCode.E10005);
				//仍然失败，发送失败状态报告
				boolean sRpt = respBiz.sendRpt(wgMsg);
				EmpExecutionContext.error("发送公众消息，获取EMessage为null。更新记录状态结果为"+uRes+"，发送状态报告结果为"+sRpt);
				return;
			}
			
			//生成序列id
			String packetID = EMessage.nextID();
			wgMsg.setPacketId(packetID);
			emsg.setPacketID(packetID);
			
			//发送前更新数据库
			boolean updateRes = dealUpdateSending(wgMsg);
			if(!updateRes){
				/*//发送前更新数据库失败
				wgMsg.setErrCode(AppErrCode.E10004);
				//仍然失败，发送失败状态报告
				respBiz.sendRpt(wgMsg);*/
				
				EmpExecutionContext.error("发送公众消息，发送前更新失败。serial="+wgMsg.getSerial());
				Thread.sleep(2000);
				return;
			}
			
			//放入窗口
			boolean addWin = addWin(wgMsg);
			if(!addWin){
				EmpExecutionContext.error("发送公众消息，发送前放窗口失败。serial="+wgMsg.getSerial());
				return;
			}
			//网络不正常
			if(AppSdkPackage.getInstance().getXmppConnState() != 1){
				Thread.sleep(1000);
				return;
			}
			//发送公众消息
			int sendRes = sendE(emsg);
			//发送次数+1
			wgMsg.setSendedCount(wgMsg.getSendedCount()+1);
			if(sendRes != 0){
				
				EmpExecutionContext.error("发送公众消息，失败。serial="+wgMsg.getSerial());
				//发3次
				boolean sendAgainRes = sendFailAgain(wgMsg, emsg, null);
				
				//发送失败，且超过重发的次数了
				if(!sendAgainRes){
					//提交失败则移除窗口
					WinCacheStorage.getInstance().removeWindow(wgMsg);
					boolean uRes = updateSendFail(wgMsg);
					//提交个人消息接口失败
					wgMsg.setErrCode(AppErrCode.E10002);
					//仍然失败，发送失败状态报告
					boolean sendRpt = respBiz.sendRpt(wgMsg);
					EmpExecutionContext.error("发送公众消息，发送3次仍然失败。更新记录状态结果为"+uRes+"，发送状态报告结果为"+sendRpt+"。serial="+wgMsg.getSerial());
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理发送公众消息异常。serial="+wgMsg.getSerial());
		}
	}
	
	/**
	 * 更新发送中，失败重试3次
	 * @param wgMsg
	 * @return
	 */
	private boolean dealUpdateSending(WgMessage wgMsg){
		try
		{
				//失败尝试3次，失败重连数据库
				int tryCount=0;
				
				do{
					boolean updateRes = updateSending(wgMsg);
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
			EmpExecutionContext.error(e, "mt发送更新发送中异常。serial="+wgMsg.getSerial());
			return false;
			
		}
	}
	
	private void dealSendP(WgMessage wgMsg){
		
		try
		{
			PMessage pmsg = getPMessage(wgMsg);
			if(pmsg == null){
				//更新为发送失败
				boolean uRes = updateSendFail(wgMsg);
				//获取PMessage对象失败
				wgMsg.setErrCode(AppErrCode.E10006);
				//仍然失败，发送失败状态报告
				boolean sRpt = respBiz.sendRpt(wgMsg);
				EmpExecutionContext.error("发送个人消息，获取PMessage为null。更新记录状态结果为"+uRes+"，发送状态报告结果为"+sRpt+"。serial="+wgMsg.getSerial());
				return;
			}
			
			//生成序列id
			String packetID = PMessage.nextID();
			wgMsg.setPacketId(packetID);
			pmsg.setPacketID(packetID);
			
			//发送前更新数据库
			boolean updateRes = dealUpdateSending(wgMsg);
			if(!updateRes){
				/*//发送前更新数据库失败
				wgMsg.setErrCode(AppErrCode.E10004);
				//仍然失败，发送失败状态报告
				respBiz.sendRpt(wgMsg);*/
				
				EmpExecutionContext.error("发送个人消息，发送前更新失败。serial="+wgMsg.getSerial());
				Thread.sleep(2000);
				return;
			}
			
			//放入窗口
			boolean addWin = addWin(wgMsg);
			if(!addWin){
				EmpExecutionContext.error("发送个人消息，发送前放窗口失败。serial="+wgMsg.getSerial());
				return;
			}
			//网络不正常
			if(AppSdkPackage.getInstance().getXmppConnState() != 1){
				Thread.sleep(1000);
				return;
			}
			//发送消息
			int sendRes = sendP(pmsg);
			//发送次数+1
			wgMsg.setSendedCount(wgMsg.getSendedCount()+1);
			if(sendRes != 0){
				
				EmpExecutionContext.error("发送个人消息，失败。serial="+wgMsg.getSerial());
				//发3次
				boolean sendAgainRes = sendFailAgain(wgMsg, null, pmsg);
				if(!sendAgainRes){
					//提交失败则移除窗口
					WinCacheStorage.getInstance().removeWindow(wgMsg);
					boolean uRes = updateSendFail(wgMsg);
					//提交公众消息接口失败
					wgMsg.setErrCode(AppErrCode.E10001);
					//仍然失败，发送失败状态报告
					boolean sendRpt = respBiz.sendRpt(wgMsg);
					EmpExecutionContext.error("发送个人消息，发送3次仍然失败。更新记录状态结果为"+uRes+"，发送状态报告结果为"+sendRpt+"。serial="+wgMsg.getSerial());
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理发送个人消息异常。serial="+wgMsg.getSerial());
		}
	}

	
	private boolean sendFailAgain(WgMessage wgMsg, EMessage emsg, PMessage pmsg){
		
		try
		{
			//根据错误代码适当延时
			//发3次
			int count = 0;
			int sendRes = 0;
			do{
				Thread.sleep(1000);
				count++;
				if(emsg != null){
					
					sendRes = sendE(emsg);
				}
				else if(pmsg != null){
					sendRes = sendP(pmsg);
				}
				if(sendRes == 0){
					return true;
				}
			}while(count < 3);
			
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "mt提交失败后马上重试提交异常。serial="+wgMsg.getSerial());
			return false;
		}
	}
	
	
	private boolean updateSending(WgMessage wgMsg){
		try
		{
			//用于放缓存失败更新
			Map<String,String> objectMap = new HashMap<String,String>();
			objectMap.put("sendState", String.valueOf(AppStaticValue.SEND_STATE_SENDING));
			objectMap.put("sendedcount", wgMsg.getSendedCount().toString());
			
			Map<String,String> conditionMap = new HashMap<String,String>();
			
			conditionMap.put("id", wgMsg.getId().toString());
			boolean updateRes = wgDao.updateMtDB(objectMap, conditionMap);
			return updateRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新mt消息发送状态为发送中异常。serial="+wgMsg.getSerial());
			return false;
		}
	}
	
	private boolean updateSendFail(WgMessage wgMsg){
		try
		{
			//用于放缓存失败更新
			Map<String,String> objectMap = new HashMap<String,String>();
			objectMap.put("sendState", String.valueOf(AppStaticValue.SEND_STATE_FAIL));
			objectMap.put("sendedcount", wgMsg.getSendedCount().toString());
			
			Map<String,String> conditionMap = new HashMap<String,String>();
			
			conditionMap.put("id", wgMsg.getId().toString());
			boolean updateRes = wgDao.updateMtDB(objectMap, conditionMap);
			return updateRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新mt消息发送状态为发送失败异常。serial="+wgMsg.getSerial());
			return false;
		}
	}
	
	
	private boolean addWin(WgMessage wgMsg){
		try
		{
			int addWin = -1;
			do{
				addWin = WinCacheStorage.getInstance().addWindow(wgMsg);
				if(addWin == 0){
					return true;
				}
			}while(isSendMtThreadExit && addWin < 0);
			
			EmpExecutionContext.error("放入发送窗口失败。serial="+wgMsg.getSerial());
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "放入发送窗口异常。serial="+wgMsg.getSerial());
			return false;
		}
	}
	
	private int sendE(EMessage emsg){
		try
		{
			//发送消息到app平台
			AppSdkPackage.getInstance().getXMPPServer().pushEMessage(emsg);
			EmpExecutionContext.info("send mt e:"+emsg.toXML());
			EmpExecutionContext.appRequestInfoLog("send mt e:"+emsg.toXML());
			return 0;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送App消息，提交消息到APP平台异常。xml:"+emsg.toXML());
			return -99;
		}
	}
	
	private int sendP(PMessage pmsg){
		try
		{
			//发送消息到app平台
			AppSdkPackage.getInstance().getXMPPServer().pushPMessage(pmsg);
			EmpExecutionContext.info("send mt p:"+pmsg.toXML());
			EmpExecutionContext.appRequestInfoLog("send mt p:"+pmsg.toXML());
			return 0;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送App消息，提交消息到APP平台异常。xml:"+pmsg.toXML());
			return -99;
		}
	}
	
	/**
	 * 获取发送接口的参数对象
	 * 
	 * @return 返回参数集合
	 */
	private EMessage getEMessage(WgMessage wgMsg){
		
		try
		{
			EMessage msg = new EMessage();
			
			msg.setIcode(AppStaticValue.ICODE_SENDEMSG);
			
			msg.setSerial(wgMsg.getSerial());
			
			msg.setEcode(wgMsg.getEcCode());
			
			//消息对象
			EMessageModel model = new EMessageModel();
			
			model.setId(wgMsg.getAppId());
			
			model.setFrom(wgMsg.getFrom());
			
			//公众消息类型。 1：首页推送数据；2：通知、提醒消息
			model.setEmtype(wgMsg.getEmtype());
			model.setTtype(wgMsg.getToType());
			model.setValidity(wgMsg.getValidity());
			//消息发送方法。1：即时发送
			model.setStype(1);
			
			List<EMessageStyle> styleList = this.parsPublicMsgBodyJson(wgMsg);
			if(styleList == null){
				EmpExecutionContext.error("获取发送公众消息参数集合对象异常。serial="+wgMsg.getSerial());
				return null;
			}
			
			model.setBody(styleList);
			
			
			model.setTo(wgMsg.getTo());
			msg.addEModel(model);
			
			EmpExecutionContext.info("发送公众消息。xml:"+msg.toXML());
				
			
			return msg;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("mt处理，获取EMessage对象异常。serial="+wgMsg.getSerial());
			return null;
		}
		
	}
	
	/**
	 * 获取发送接口的参数对象
	 * 
	 * @return 返回参数集合
	 */
	private PMessage getPMessage(WgMessage wgMsg){
		
		try
		{
			PMessage msg = new PMessage();
			
			msg.setIcode(AppStaticValue.ICODE_SENDPMSG);
			
			msg.setSerial(wgMsg.getSerial());
			
			msg.setEcode(wgMsg.getEcCode());
			
			//消息对象
			PMessageModel model = new PMessageModel();
			
			model.setId(0L);
			model.setFrom(wgMsg.getFrom());
			
			if(wgMsg.getValidity() != null){
				model.setValidity(wgMsg.getValidity());
			}
			
			List<PMessageStyle> styleList = parsPersonalMsgBodyJson(wgMsg.getBody());
			if(styleList == null){
				EmpExecutionContext.error("获取发送个人消息参数集合对象异常。serial="+wgMsg.getSerial());
				return null;
			}
			
			model.setBody(styleList);
			
			model.setTo(wgMsg.getTo());
			msg.addPModel(model);
			
			EmpExecutionContext.info("发送个人消息。xml:"+msg.toXML());
				
			
			return msg;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("mt处理，获取PMessage对象异常。serial="+wgMsg.getSerial());
			return null;
		}
		
	}
	
	/**
	 * 处理json数据
	 * @param json json格式字符串
	 * @return 返回封装了json数据的AppMessage，异常返回null
	 */
	private List<EMessageStyle> parsPublicMsgBodyJson(WgMessage wgMsg){
		try
		{
			String json = getWgContent(wgMsg);
			//把json格式字符串转为json对象
			JSONObject jsonObj = JsonUtil.parsJsonObj(json);
			if(jsonObj == null){
				EmpExecutionContext.error("处理公众消息内容json数据失败，获取json对象为null。serial="+wgMsg.getSerial());
				return null;
			}
			
			//获取style的数量
			int size = 1;
			if(jsonObj.get("stylecount") != null){
				//获取style的数量
				size = Integer.parseInt(jsonObj.get("stylecount").toString());
			}
			
			List<EMessageStyle> msgStylesList = new ArrayList<EMessageStyle>();
			
			EMessageStyle[] styleArray = new EMessageStyle[size];
			
			Integer sortIndex = null;
			JSONObject styleJsonObj = null;
			for(Object key : jsonObj.keySet()){
				
				if("EMessageStyle1".equals(key.toString())){
					//获取对象，要在这里获取，因为要转型
					styleJsonObj = (JSONObject)jsonObj.get(key.toString());
					//获取消息内容样式
					EMessageStyle1 style1 = new EMessageStyle1();
					style1.setTitle(styleJsonObj.get("title")==null?"":styleJsonObj.get("title").toString());
					style1.setContent(styleJsonObj.get("content")==null?"":styleJsonObj.get("content").toString());
					
					if(styleJsonObj.get("sortindex") != null){
						sortIndex = Integer.valueOf(styleJsonObj.get("sortindex").toString());
					}
					else{
						sortIndex = 0;
					}
					
					styleArray[sortIndex] = style1;
					//msgStylesList.add(style1);
				}
				else if("EMessageStyle2".equals(key.toString())){
					//获取对象，要在这里获取，因为要转型
					styleJsonObj = (JSONObject)jsonObj.get(key.toString());
					
					//获取消息内容样式
					EMessageStyle2 style2 = new EMessageStyle2();
					style2.setTitle(styleJsonObj.get("title")==null?"":styleJsonObj.get("title").toString());
					style2.setContent(styleJsonObj.get("content")==null?"":styleJsonObj.get("content").toString());
					style2.setPic(styleJsonObj.get("pic")==null?"":styleJsonObj.get("pic").toString());
					style2.setUrl(styleJsonObj.get("url")==null?"":styleJsonObj.get("url").toString());
					
					if(styleJsonObj.get("sortindex") != null){
						sortIndex = Integer.valueOf(styleJsonObj.get("sortindex").toString());
					}
					else{
						sortIndex = 0;
					}
					
					styleArray[sortIndex] = style2;
					//msgStylesList.add(style2);
				}
				else if("EMessageStyle8".equals(key.toString())){
					//获取对象，要在这里获取，因为要转型
					styleJsonObj = (JSONObject)jsonObj.get(key.toString());
					
					EMessageStyle8 style8 = new EMessageStyle8();
					style8.setTitle1(styleJsonObj.get("title1")==null?"":styleJsonObj.get("title1").toString());
					style8.setContent1(styleJsonObj.get("content1")==null?"":styleJsonObj.get("content1").toString());
					style8.setPic1(styleJsonObj.get("pic1")==null?"":styleJsonObj.get("pic1").toString());
					style8.setUrl1(styleJsonObj.get("url1")==null?"":styleJsonObj.get("url1").toString());
					
					style8.setTitle2(styleJsonObj.get("title2")==null?"":styleJsonObj.get("title2").toString());
					style8.setContent2(styleJsonObj.get("content2")==null?"":styleJsonObj.get("content2").toString());
					style8.setPic2(styleJsonObj.get("pic2")==null?"":styleJsonObj.get("pic2").toString());
					style8.setUrl2(styleJsonObj.get("url2")==null?"":styleJsonObj.get("url2").toString());
					
					style8.setTitle3(styleJsonObj.get("title3")==null?"":styleJsonObj.get("title3").toString());
					style8.setContent3(styleJsonObj.get("content3")==null?"":styleJsonObj.get("content3").toString());
					style8.setPic3(styleJsonObj.get("pic3")==null?"":styleJsonObj.get("pic3").toString());
					style8.setUrl3(styleJsonObj.get("url3")==null?"":styleJsonObj.get("url3").toString());
					
					style8.setTitle4(styleJsonObj.get("title4")==null?"":styleJsonObj.get("title4").toString());
					style8.setContent4(styleJsonObj.get("content4")==null?"":styleJsonObj.get("content4").toString());
					style8.setPic4(styleJsonObj.get("pic4")==null?"":styleJsonObj.get("pic4").toString());
					style8.setUrl4(styleJsonObj.get("url4")==null?"":styleJsonObj.get("url4").toString());
					
					style8.setTitle5(styleJsonObj.get("title5")==null?"":styleJsonObj.get("title5").toString());
					style8.setContent5(styleJsonObj.get("content5")==null?"":styleJsonObj.get("content5").toString());
					style8.setPic5(styleJsonObj.get("pic5")==null?"":styleJsonObj.get("pic5").toString());
					style8.setUrl5(styleJsonObj.get("url5")==null?"":styleJsonObj.get("url5").toString());
					
					if(styleJsonObj.get("sortindex") != null){
						sortIndex = Integer.valueOf(styleJsonObj.get("sortindex").toString());
					}
					else{
						sortIndex = 0;
					}
					
					styleArray[sortIndex] = style8;
					//msgStylesList.add(sortIndex, style8);
				}
				else if("EMessageStyle9".equals(key.toString())){
					//获取对象，要在这里获取，因为要转型
					styleJsonObj = (JSONObject)jsonObj.get(key.toString());
					
					EMessageStyle9 style9 = new EMessageStyle9();
					style9.setUrl(styleJsonObj.get("url")==null?"":styleJsonObj.get("url").toString());
					if(styleJsonObj.get("time") != null)
					{
						style9.setTime(Long.parseLong(styleJsonObj.get("time").toString()));
					}
					
					if(styleJsonObj.get("sortindex") != null){
						sortIndex = Integer.valueOf(styleJsonObj.get("sortindex").toString());
					}
					else{
						sortIndex = 0;
					}
					
					styleArray[sortIndex] = style9;
					//msgStylesList.add(style9);
				}
				else if("EMessageStyle10".equals(key.toString())){
					//获取对象，要在这里获取，因为要转型
					styleJsonObj = (JSONObject)jsonObj.get(key.toString());
					
					EMessageStyle10 style10 = new EMessageStyle10();
					style10.setTitle(styleJsonObj.get("title")==null?"":styleJsonObj.get("title").toString());
					style10.setUrl(styleJsonObj.get("url")==null?"":styleJsonObj.get("url").toString());
					if(styleJsonObj.get("time") != null)
					{
						style10.setTime(Long.parseLong(styleJsonObj.get("time").toString()));
					}
					style10.setPic(styleJsonObj.get("pic")==null?"":styleJsonObj.get("pic").toString());
					
					if(styleJsonObj.get("sortindex") != null){
						sortIndex = Integer.valueOf(styleJsonObj.get("sortindex").toString());
					}
					else{
						sortIndex = 0;
					}
					
					styleArray[sortIndex] = style10;
					//msgStylesList.add(style10);
				}
				else if("stylecount".equals(key.toString())){
					//这个在前面取来用了，这里就什么都不做
				}
				//首页公众消息会有多个相同样式，json用的key不能重复，所以首页的要截掉前面部分
				else if("EMessageStyle2".equals(key.toString().substring(key.toString().length()-14))){
					//获取对象，要在这里获取，因为要转型
					styleJsonObj = (JSONObject)jsonObj.get(key.toString());
					
					//获取消息内容样式
					EMessageStyle2 style2 = new EMessageStyle2();
					style2.setTitle(styleJsonObj.get("title")==null?"":styleJsonObj.get("title").toString());
					style2.setContent(styleJsonObj.get("content")==null?"":styleJsonObj.get("content").toString());
					style2.setPic(styleJsonObj.get("pic")==null?"":styleJsonObj.get("pic").toString());
					style2.setUrl(styleJsonObj.get("url")==null?"":styleJsonObj.get("url").toString());
					
					if(styleJsonObj.get("sortindex") == null){
						return null;
					}
					sortIndex = Integer.valueOf(styleJsonObj.get("sortindex").toString());
					
					styleArray[sortIndex] = style2;
					//msgStylesList.add(sortIndex, style2);
					
				}
			}
			
			for(int i =0; i< styleArray.length; i++){
				msgStylesList.add(styleArray[i]);
			}
			
			return msgStylesList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理公众消息内容json数据异常。serial="+wgMsg.getSerial());
			return null;
		}
	}
	
	private String getWgContent(WgMessage wgMsg){
		try
		{
			StringBuffer sb = new StringBuffer();
			
			if(wgMsg.getBody().length() > 0){
				sb.append(wgMsg.getBody());
			}
			if(wgMsg.getContent1().length() > 0){
				sb.append(wgMsg.getContent1());
			}
			if(wgMsg.getContent2().length() > 0){
				sb.append(wgMsg.getContent2());
			}
			if(wgMsg.getContent3().length() > 0){
				sb.append(wgMsg.getContent3());
			}
			if(wgMsg.getContent4().length() > 0){
				sb.append(wgMsg.getContent4());
			}
			if(wgMsg.getContent5().length() > 0){
				sb.append(wgMsg.getContent5());
			}
			if(wgMsg.getContent6().length() > 0){
				sb.append(wgMsg.getContent6());
			}
			if(wgMsg.getContent7().length() > 0){
				sb.append(wgMsg.getContent7());
			}
			if(wgMsg.getContent8().length() > 0){
				sb.append(wgMsg.getContent8());
			}
			if(wgMsg.getContent9().length() > 0){
				sb.append(wgMsg.getContent9());
			}
			if(wgMsg.getContent10().length() > 0){
				sb.append(wgMsg.getContent10());
			}
			return sb.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取消息体内容异常。serial="+wgMsg.getSerial());
			return null;
		}
	}
	
	/**
	 * 处理json数据
	 * @param json json格式字符串
	 * @return 返回封装了json数据的AppMessage，异常返回null
	 */
	private List<PMessageStyle> parsPersonalMsgBodyJson(String json){
		try
		{
			//把json格式字符串转为json对象
			JSONObject jsonObj = JsonUtil.parsJsonObj(json);
			if(jsonObj == null){
				EmpExecutionContext.error("处理个人消息内容json数据失败，获取json对象为null。json:"+json);
				return null;
			}
			
			List<PMessageStyle> msgStylesList = new ArrayList<PMessageStyle>();
			//获取消息内容样式
			JSONObject styleJsonObj = null;
			
			for(Object key : jsonObj.keySet()){
				styleJsonObj = (JSONObject)jsonObj.get(key.toString());
				if("PMessageStyle1".equals(key.toString())){
					//获取消息内容样式
					PMessageStyle1 style1 = new PMessageStyle1();
					style1.setContent(styleJsonObj.get("content")==null?"":styleJsonObj.get("content").toString());
					
					msgStylesList.add(style1);
				}
				else if("PMessageStyle2".equals(key.toString())){
					//获取消息内容样式
					PMessageStyle2 style2 = new PMessageStyle2();
					style2.setPic(styleJsonObj.get("pic")==null?"":styleJsonObj.get("pic").toString());
					
					msgStylesList.add(style2);
				}
				
				else if("PMessageStyle3".equals(key.toString())){
					PMessageStyle3 style3 = new PMessageStyle3();
					style3.setUrl(styleJsonObj.get("url")==null?"":styleJsonObj.get("url").toString());
					if(styleJsonObj.get("time") != null)
					{
						style3.setTime(Long.parseLong(styleJsonObj.get("time").toString()));
					}
					
					msgStylesList.add(style3);
				}
				else if("PMessageStyle4".equals(key.toString())){
					PMessageStyle4 style4 = new PMessageStyle4();
					style4.setTitle(styleJsonObj.get("title")==null?"":styleJsonObj.get("title").toString());
					style4.setContent(styleJsonObj.get("content")==null?"":styleJsonObj.get("content").toString());
					style4.setUrl(styleJsonObj.get("url")==null?"":styleJsonObj.get("url").toString());
					if(styleJsonObj.get("time") != null)
					{
						style4.setTime(Long.parseLong(styleJsonObj.get("time").toString()));
					}
					style4.setPic(styleJsonObj.get("pic")==null?"":styleJsonObj.get("pic").toString());
					
					msgStylesList.add(style4);
				}
			}
			
			return msgStylesList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "个人消息内容json封装为App接口参数对象数据异常。json="+json);
			return null;
		}
	}
	
	
}
