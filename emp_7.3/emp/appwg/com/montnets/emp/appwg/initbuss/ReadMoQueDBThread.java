package com.montnets.emp.appwg.initbuss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.appwg.bean.AppStaticValue;
import com.montnets.emp.appwg.bean.WgMessage;
import com.montnets.emp.appwg.cache.RecMoCacheStorage;
import com.montnets.emp.appwg.dao.WgDAO;
import com.montnets.emp.appwg.entity.LfAppMoCache;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IDataAccessDriver;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @project emp_std_5.0new
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-9-2 下午12:02:54
 * @description 处理数据库mo消息线程
 */
public class ReadMoQueDBThread extends Thread
{
	public ReadMoQueDBThread()
	{
		this.setName("处理数据库mo消息线程");
		
		IDataAccessDriver dataAccessDriver = new DataAccessDriver();
		empDao = dataAccessDriver.getEmpDAO();
		wgDao = new WgDAO();
		moQue = RecMoCacheStorage.getInstance();
	}

	private IEmpDAO	empDao;
	private WgDAO wgDao;
	
	
	private boolean isReadMoQueDBThreadExit = true;
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
	
	public void stopReadMoQueDBThread(){
		isReadMoQueDBThreadExit = false;
	}
	

	public final void run()
	{
		dealMo();
		
		//线程跳出了循环，则设置为未启动
		isThreadStart = false;
	}

	private void dealMo()
	{
		while(isReadMoQueDBThreadExit)
		{
			try
			{
				int moSize =  moQue.getSize();
				int dbSize =0;
				if(moSize >= AppStaticValue.REC_MO_QUEUE_SIZE * 2 / 3){
					Thread.sleep(1000);
					continue;
				}
				else{
					dbSize = AppStaticValue.REC_MO_QUEUE_SIZE - moSize;
				}
				//先判断真实缓冲大小
				
				List<LfAppMoCache> appMsgList = this.getDBList(dbSize);
				if(appMsgList == null || appMsgList.size() < 1){
					Thread.sleep(2000);
					continue;
				}
				
				List<LfAppMoCache> appMsgListCache = new ArrayList<LfAppMoCache>();
				Iterator<LfAppMoCache> appMsgIt = appMsgList.iterator();
				int count=0;
				LfAppMoCache appMsg = null;
				StringBuffer idSb = new StringBuffer();
				while(appMsgIt.hasNext()){
					appMsg = appMsgIt.next();
					idSb.append(appMsg.getId()).append(",");
					appMsgListCache.add(appMsg);
					count++;
					//满200处理一次
					if(count > 0 && count % 200 == 0){
						//截掉最后一个逗号
						idSb.delete(idSb.length()-1, idSb.length());
						boolean updateRes = updateReadWaitSend(idSb.toString());
						if(updateRes){
							moQue.produceRecMo(appMsg2WgMsgForReaded(appMsgListCache));
							appMsgListCache.clear();
							idSb.setLength(0);
							if(moQue.getSize() >= AppStaticValue.REC_MO_QUEUE_SIZE ){
								break;
							}
						}
						else{
							idSb.setLength(0);
							Thread.sleep(2000);
							break;
						}
					}
				}
				//处理不足200的信息
				if(idSb.length() > 0){
					//截掉最后一个逗号
					idSb.delete(idSb.length()-1, idSb.length());
					boolean updateRes = updateReadWaitSend(idSb.toString());
					if(updateRes){
						moQue.produceRecMo(appMsg2WgMsgForReaded(appMsgListCache));
						appMsgListCache.clear();
						idSb.setLength(0);
					}
					else{
						Thread.sleep(2000);
					}
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "数据库Mo缓存线程处理异常。");
			}
		}
	}
	
	private boolean updateReadWaitSend(String id){
		try
		{
			//用于放缓存失败更新
			Map<String,String> objectMap = new HashMap<String,String>();
			objectMap.put("sendState", String.valueOf(AppStaticValue.SEND_STATE_WAIT_SEND));
			
			Map<String,String> conditionMap = new HashMap<String,String>();
			
			conditionMap.put("id", id.toString());
			boolean updateRes = wgDao.updateMoDB(objectMap, conditionMap);
			return updateRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新发送消息发送异常。");
			return false;
		}
	}
	
	private List<LfAppMoCache> getDBList(int dbSize){
		try
		{
			//先判断真实缓冲大小
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			PageInfo pageInfo = new PageInfo();

			//找未读状态
			conditionMap.put("sendstate", String.valueOf(AppStaticValue.SEND_STATE_NOREAD));
			// 时间最久
			orderbyMap.put("id", StaticValue.ASC);
			pageInfo.setPageSize(dbSize);
			List<LfAppMoCache> appMsgList = empDao.findPageListBySymbolsCondition(null, LfAppMoCache.class, conditionMap, orderbyMap, pageInfo);
			return appMsgList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取数据库mo记录异常。");
			return null;
		}
	}
	
	private List<WgMessage> appMsg2WgMsgForReaded(List<LfAppMoCache> msgList){
		try
		{
			List<WgMessage> wgMsgList = new ArrayList<WgMessage>();
			
			WgMessage wgMsg = null;
			
			for(LfAppMoCache appMsg : msgList){
				//读取状态。1：未读；2：已读；
				appMsg.setReadstate(2);
				wgMsg = getWgMessage(appMsg);
				wgMsgList.add(wgMsg);
			}
			return wgMsgList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "消息转换为缓存集合异常。");
			return null;
		}
	}
	
	
	private WgMessage getWgMessage(LfAppMoCache appMsg){
		try
		{
			WgMessage wgMsg = new WgMessage();
			wgMsg.setId(appMsg.getId());
			wgMsg.setAppId(appMsg.getAppid());
			wgMsg.setBody(appMsg.getBody());
			wgMsg.setCreatetime(appMsg.getCreatetime());
			wgMsg.setEcCode(appMsg.getEcode());
			wgMsg.setEmtype(appMsg.getEmptype());
			wgMsg.setFromName(appMsg.getFname());
			wgMsg.setFrom(appMsg.getFromjid());
			wgMsg.setMsgId(appMsg.getMsgid());
			wgMsg.setMsgSrc(appMsg.getMsgsrc());
			wgMsg.setMsgType(appMsg.getMsgtype());
			wgMsg.setReadState(appMsg.getReadstate());
			wgMsg.setSendedCount(appMsg.getSendedcount());
			wgMsg.setSendState(appMsg.getSendstate());
			wgMsg.setSerial(appMsg.getSerial());
			wgMsg.setToName(appMsg.getTname());
			wgMsg.setTo(appMsg.getTojid());
			wgMsg.setToType(appMsg.getTotype());
			wgMsg.setValidity(appMsg.getValidity());
			wgMsg.setOutlinemsg(appMsg.getOutlinemsg());
			
			return wgMsg;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "通过LfAppMoCache获取WgMessage对象异常。");
			return null;
		}
	}
	

}
