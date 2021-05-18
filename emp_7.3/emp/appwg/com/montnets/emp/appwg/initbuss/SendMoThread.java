package com.montnets.emp.appwg.initbuss;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.montnets.app.EMessage;
import com.montnets.app.PMessage;
import com.montnets.app.PMessageModel;
import com.montnets.app.style.PMessageStyle;
import com.montnets.app.style.PMessageStyle1;
import com.montnets.app.style.PMessageStyle2;
import com.montnets.app.style.PMessageStyle3;
import com.montnets.app.style.PMessageStyle4;
import com.montnets.emp.appwg.bean.AppStaticValue;
import com.montnets.emp.appwg.bean.WgMessage;
import com.montnets.emp.appwg.biz.UserDealStorage;
import com.montnets.emp.appwg.cache.RecMoCacheStorage;
import com.montnets.emp.appwg.dao.WgDAO;
import com.montnets.emp.appwg.util.JsonUtil;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IDataAccessDriver;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.appmage.LfAppTcMomsg;

/**
 * 
 * @project emp_std_5.0
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-8-28 下午12:21:53
 * @description 发送线程
 */
public class SendMoThread extends Thread
{
	public SendMoThread(){
		this.setName("Mo发送线程");
		IDataAccessDriver dataAccessDriver = new DataAccessDriver();
		empDao = dataAccessDriver.getEmpDAO();
		wgDao = new WgDAO();
		msgBiz = new HandleMsgBiz();
		moQue = RecMoCacheStorage.getInstance();
	}
	
	private IEmpDAO	empDao;
	private WgDAO wgDao;
	private HandleMsgBiz msgBiz;
	
	private boolean isSendMoThreadExit = true;
	private RecMoCacheStorage moQue;
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
	
	public void stopSendMoThread(){
		isSendMoThreadExit = false;
	}
	
	public final void run()
	{
		dealSend();
		
		//线程跳出了循环，则设置为未启动
		isThreadStart = false;
	}
	
	private void dealSend(){
		while(isSendMoThreadExit){
			try
			{
				
				//缓冲没信息
				if(moQue.getSize() < 1){
					Thread.sleep(500);
					continue;
				}
				//从缓冲中获取消息
				WgMessage wgMsg = moQue.consumeRecMo();
				if(wgMsg == null){
					Thread.sleep(1000);
					continue;
				}
				
				if(wgMsg.getSendedCount() >= AppStaticValue.RE_SEND_COUNT){
					boolean delRes = msgBiz.DealReSendFail(wgMsg);
					EmpExecutionContext.error("mo发送线程处理发送，消息重发次数超过"+AppStaticValue.RE_SEND_COUNT+"次，不能发送。更新数据库记录结果为"+delRes+"。serial="+wgMsg.getSerial());
					
					continue;
				}
				
				dealSendP(wgMsg);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "Mo发送线程处理异常。");
			}
		}
	}
	
	private void dealSendP(WgMessage wgMsg){
		
		try
		{
			PMessage pmsg = getPMessage(wgMsg);
			if(pmsg == null){
				//更新为发送失败
				boolean uRes = updateMoSendState(wgMsg.getId(), AppStaticValue.SEND_STATE_FAIL);
				
				EmpExecutionContext.error("发送mo消息，获取PMessage为null。更新记录状态结果为"+uRes+",serial="+wgMsg.getSerial());
				return;
			}
			
			//生成序列id
			String packetID = EMessage.nextID();
			wgMsg.setPacketId(packetID);
			pmsg.setPacketID(packetID);
			
			//发送前更新数据库
			boolean updateRes = updateSendSucc(wgMsg);
			if(!updateRes){
				
				EmpExecutionContext.error("发送mo消息，发送前更新失败。serial="+wgMsg.getSerial());
				Thread.sleep(2000);
				return;
			}
			
			//发送消息
			int sendRes = moSend(pmsg, wgMsg);
			//发送次数+1
			wgMsg.setSendedCount(wgMsg.getSendedCount()+1);
			if(sendRes != 0){
				
				EmpExecutionContext.error("发送mo消息，推送失败。serial="+wgMsg.getSerial());
				//发3次
				boolean sendAgainRes = sendFailAgain(wgMsg, pmsg);
				//仍然失败
				if(!sendAgainRes){
					//更新为未读
					boolean uRes = updateMoSendState(wgMsg.getId(), AppStaticValue.SEND_STATE_FAIL);
					EmpExecutionContext.error("发送mo消息，推送3次仍然失败。更新记录状态结果为"+uRes+",serial="+wgMsg.getSerial());
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送mo消息，线程处理异常。serial="+wgMsg.getSerial());
		}
	}
	
	private int moSend(PMessage pmessage, WgMessage wgMsg){
		
		try
		{
			LfAppTcMomsg moMsg = new LfAppTcMomsg();
			
			String json = getJson(moMsg, pmessage, wgMsg);
			if(json == null){
				EmpExecutionContext.error("发送mo消息，获取json为null。流水号："+pmessage.getSerial());
				return -1;
			}
			EmpExecutionContext.info("发送mo消息，json="+json);
			
			//保存数据
			boolean saveRes = saveMoMsg(moMsg, pmessage, json);
			if(!saveRes){
				EmpExecutionContext.error("发送mo消息，保存消息记录失败。流水号："+pmessage.getSerial());
				return -2;
			}
			
			//用户账号放到处理队列，用于判断同步信息
			UserDealStorage.produce(moMsg.getFromUser(), moMsg.getCorpcode());
			
			if(HandleMsgBiz.getProcPmsgImpl() == null){
				EmpExecutionContext.error("发送mo消息，个人消息接收处理接口实例为null。流水号："+pmessage.getSerial());
				return -3;
			}
			
			EmpExecutionContext.info("推送给客服上行json："+json+"    。流水号："+pmessage.getSerial());
			EmpExecutionContext.appRequestInfoLog("send mo:"+json);
			boolean res = HandleMsgBiz.getProcPmsgImpl().processPMessage(json);
			EmpExecutionContext.appRequestInfoLog("resp send mo:"+res+",serial="+pmessage.getSerial());
			if(res){
				EmpExecutionContext.info("推送个人消息成功。流水号："+pmessage.getSerial());
				return 0;
			}
			else{
				EmpExecutionContext.info("推送个人消息失败。流水号："+pmessage.getSerial());
				return -4;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送mo消息，异常。xml:"+pmessage.toXML());
			return -99;
		}
	}
	
	/**
	 * 获取json格式字符串
	 * @param pMsgModel 构造参数
	 * @return 返回json格式字符串，异常返回null
	 */
	public String getJson(LfAppTcMomsg moMsg, PMessage pmessage, WgMessage wgMsg){
		if(pmessage.getPModel() == null){
			EmpExecutionContext.error("获取json格式字符串失败，PMessageModel对象为null。xml:"+pmessage.toXML());
			return null;
		}
		
		try
		{
			PMessageModel pMsgModel = pmessage.getPModel();
			
			String fname = pMsgModel.getFname();
			if(fname == null){
				fname = "";
			}
			//转义特殊字符
			fname = JsonUtil.stringForJson(fname);
			
			StringBuffer sb = new StringBuffer()
				.append("{")
				.append("\"ECODE\":").append("\"").append(pmessage.getEcode()).append("\"").append(",")
				.append("\"FROM\":").append("\"").append(pMsgModel.getFrom()).append("\"").append(",")
				.append("\"FROMNAME\":").append("\"").append(fname).append("\"").append(",")
				.append("\"TO\":").append("\"").append(pMsgModel.getTo()).append("\"").append(",")
				.append("\"VALIDITY\":").append("\"").append(pMsgModel.getValidity()).append("\"").append(",")
				.append("\"OUTLINEMSG\":").append("\"").append(wgMsg.getOutlinemsg()).append("\"").append(",");
				
			sb.append("\"pMessageStyles\":").append("{");
			
			if(pMsgModel.getBody() == null || pMsgModel.getBody().size() == 0){
				EmpExecutionContext.error("pMessageStyles为空。");
				return null;
			}
			int size = pMsgModel.getBody().size();
			for(int i = 0; i < size; i++){	
				
				//样式1
				if(pMsgModel.getBody().get(i).getStyle() == 1){
					
					PMessageStyle1 style1 = (PMessageStyle1)pMsgModel.getBody().get(i);
					String content = style1.getContent();
					if(content == null){
						content = "";
					}
					//转义特殊字符
					content = JsonUtil.stringForJson(content);
					sb
					.append("\"PMessageStyle1\":")
					.append("{")
					.append("\"content\":").append("\"").append(content).append("\"")
					.append("}");
					
					moMsg.setMsgtype(0);
					moMsg.setMsgtext(style1.getContent());
				}
				//样式2
				else if(pMsgModel.getBody().get(i).getStyle() == 2){
					
					PMessageStyle2 style2 = (PMessageStyle2)pMsgModel.getBody().get(i);
					sb
					.append("\"PMessageStyle2\":")
					.append("{")
					.append("\"pic\":").append("\"").append(style2.getPic()).append("\"")
					.append("}");
					
					moMsg.setMsgtype(1);
					moMsg.setMsgtext(style2.getPic());
				}
				//样式3
				else if(pMsgModel.getBody().get(i).getStyle() == 3){
					
					PMessageStyle3 style3 = (PMessageStyle3)pMsgModel.getBody().get(i);
					sb
					.append("\"PMessageStyle3\":")
					.append("{")
					.append("\"url\":").append("\"").append(style3.getUrl()).append("\"").append(",")
					.append("\"time\":").append("\"").append(style3.getTime()).append("\"")
					.append("}");
					
					moMsg.setMsgtype(3);
					moMsg.setMsgtext(style3.getUrl());
				}
				//样式4
				else if(pMsgModel.getBody().get(i).getStyle() == 4){
					
					PMessageStyle4 style4 = (PMessageStyle4)pMsgModel.getBody().get(i);
					sb
					.append("\"PMessageStyle4\":")
					.append("{")
					.append("\"pic\":").append("\"").append(style4.getPic()).append("\"").append(",")
					.append("\"url\":").append("\"").append(style4.getUrl()).append("\"").append(",")
					.append("\"time\":").append("\"").append(style4.getTime()).append("\"")
					.append("}");
					
					moMsg.setMsgtype(2);
					moMsg.setMsgtext(style4.getUrl());
				}
				
				if(i < size - 1){
					sb.append(",");
				}
			}
			sb.append("}").append("}");
			
			return sb.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取mo消息json格式字符串异常。serial="+wgMsg.getSerial());
			return null;
		}
	}
	
	/**
	 * 保存APP上行消息记录
	 * @param pmessage 上行消息对象
	 * @param json 上行消息json格式字符串
	 * @return 成功返回true
	 */
	private boolean saveMoMsg(LfAppTcMomsg moMsg, PMessage pmessage, String json){
		
		try
		{
			//流水号
			moMsg.setSerial(pmessage.getSerial());
			//企业编码
			moMsg.setCorpcode(pmessage.getEcode());
			//发送这用户名
			moMsg.setFromUser(pmessage.getPModel().getFrom());
			//发送这姓名
			moMsg.setFromName(pmessage.getPModel().getFname());
			//接收者用户名
			moMsg.setToUser(pmessage.getPModel().getTo());
			//接收者姓名
			moMsg.setToName(pmessage.getPModel().getTname());
			//消息来源。 1－企业后台管理； 2－企业EMP； 3－企业客服； 11－安卓客户端； 12－IOS客户端；
			moMsg.setMsgSrc(pmessage.getPModel().getMsg_src());
			//消息json格式字符串
			moMsg.setMsgJson(json);
			//状态
			moMsg.setStatus(pmessage.getPModel().getStatus());
			moMsg.setCreatetime(new Timestamp(System.currentTimeMillis()));
			
			boolean saveRes = empDao.save(moMsg);
			
			return saveRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "保存APP上行消息异常。serial="+pmessage.getSerial());
			return false;
		}
		
	}

	
	private boolean sendFailAgain(WgMessage wgMsg, PMessage pmsg){
		
		try
		{
			//根据错误代码适当延时
			//发3次
			int count = 0;
			int sendRes = 0;
			do{
				Thread.sleep(1000);
				count++;
				sendRes = moSend(pmsg, wgMsg);
				if(sendRes == 0){
					return true;
				}
			}while(count < 3);
			
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送mo消息，失败后马上重试提交异常。serial="+wgMsg.getSerial());
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
			boolean updateRes = wgDao.updateMoDB(objectMap, conditionMap);
			return updateRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新mo消息发送状态为成功异常。id="+wgMsg.getId()+",serial="+wgMsg.getSerial());
			return false;
		}
	}
	
	/**
	 * 更新mo发送状态
	 * @param id
	 * @param state
	 * @return
	 */
	private boolean updateMoSendState(Long id, int state){
		try
		{
			//用于放缓存失败更新
			Map<String,String> objectMap = new HashMap<String,String>();
			objectMap.put("sendState", String.valueOf(state));
			
			Map<String,String> conditionMap = new HashMap<String,String>();
			
			conditionMap.put("id", id.toString());
			boolean updateRes = wgDao.updateMoDB(objectMap, conditionMap);
			return updateRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新mo消息发送状态异常。id="+id+",sendState="+state);
			return false;
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
			model.setFname(wgMsg.getFromName());
			
			if(wgMsg.getValidity() != null){
				model.setValidity(wgMsg.getValidity());
			}
			
			List<PMessageStyle> styleList = parsPersonalMsgBodyJson(wgMsg.getBody());
			
			model.setBody(styleList);
			
			model.setTo(wgMsg.getTo());
			model.setTname(wgMsg.getToName());
			msg.addPModel(model);
			
			return msg;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("mo处理，获取PMessage对象异常。serial="+wgMsg.getSerial());
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
				EmpExecutionContext.error("处理个人消息内容json数据失败，获取json对象为null。json="+json);
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
			EmpExecutionContext.error(e, "处理个人消息内容json数据异常。json="+json);
			return null;
		}
	}
	
	
}
