package com.montnets.emp.appwg.initappplat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.appwg.bean.AppStaticValue;
import com.montnets.emp.appwg.bean.WgMessage;
import com.montnets.emp.appwg.cache.RecMtCacheStorage;
import com.montnets.emp.appwg.dao.WgDAO;
import com.montnets.emp.appwg.entity.LfAppMsgContent;
import com.montnets.emp.appwg.entity.LfAppMsgcache;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IDataAccessDriver;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.util.PageInfo;

/**
 * @project emp_std_5.0new
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-8-30 上午10:20:32
 * @description 处理数据库mt消息线程
 */
public class ReadMtQueDBThread extends Thread
{
	public ReadMtQueDBThread()
	{
		this.setName("处理数据库mt消息线程");
		
		IDataAccessDriver dataAccessDriver = new DataAccessDriver();
		empDao = dataAccessDriver.getEmpDAO();
		wgDao = new WgDAO();
		mtQue = RecMtCacheStorage.getInstance();
	}

	private IEmpDAO	empDao;
	private WgDAO wgDao;
	
	private boolean isReadMtQueDBThreadExit = true;
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
	
	public void stopReadMtQueDBThread(){
		isReadMtQueDBThreadExit = false;
	}
	

	public final void run()
	{
		dealMt();
		
		//线程跳出了循环，则设置为未启动
		isThreadStart = false;
	}

	private void dealMt()
	{
		while(isReadMtQueDBThreadExit)
		{
			try
			{
				int mtSize =  mtQue.getSize();
				int dbSize =0;
				if(mtSize >= AppStaticValue.REC_MT_QUEUE_SIZE * 2 / 3){
					Thread.sleep(1000);
					continue;
				}
				else{
					dbSize = AppStaticValue.REC_MT_QUEUE_SIZE - mtSize;
				}
				
				//先判断真实缓冲大小
				List<WgMessage> wgMsgList = getDBList(dbSize);
				if(wgMsgList == null || wgMsgList.size() < 1){
					Thread.sleep(2000);
					continue;
				}
				
				List<WgMessage> wgMsgTempList = new ArrayList<WgMessage>();
				
				Iterator<WgMessage> wgMsgIt = wgMsgList.iterator();
				int count=0;
				WgMessage wgMsg = null;
				StringBuffer idSb = new StringBuffer();
				while(wgMsgIt.hasNext()){
					wgMsg = wgMsgIt.next();
					//读取状态。1：未读；2：已读；
					wgMsg.setReadState(2);
					
					idSb.append(wgMsg.getId()).append(",");
					wgMsgTempList.add(wgMsg);
					count++;
					//满200处理一次
					if(count > 0 && count % 200 == 0){
						//截掉最后一个逗号
						idSb.delete(idSb.length()-1, idSb.length());
						boolean updateRes = updateReadWaitSend(idSb.toString());
						if(updateRes){
							mtQue.produceRecMt(wgMsgTempList);
							wgMsgTempList.clear();
							idSb.setLength(0);
							if(mtQue.getSize() >= AppStaticValue.REC_MT_QUEUE_SIZE ){
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
						mtQue.produceRecMt(wgMsgTempList);
						wgMsgTempList.clear();
						idSb.setLength(0);
					}
					else{
						Thread.sleep(2000);
					}
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "数据库Mt缓存线程处理异常。");
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
			boolean updateRes = wgDao.updateMtDB(objectMap, conditionMap);
			return updateRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新发送消息发送异常。");
			return false;
		}
	}
	
	private List<WgMessage> getDBList(int dbSize){
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
			List<LfAppMsgcache> appMsgList = empDao.findPageListBySymbolsCondition(null, LfAppMsgcache.class, conditionMap, orderbyMap, pageInfo);
			if(appMsgList == null || appMsgList.size() == 0){
				return new ArrayList<WgMessage>();
			}
			
			List<WgMessage> wgMsgList = new ArrayList<WgMessage>();
			
			List<LfAppMsgContent> contentsList = null;
			WgMessage wgMsg = null;
			
			for(LfAppMsgcache appMsg : appMsgList){
				//在另一个表中有内容
				if(appMsg.getHasContent() != null && appMsg.getHasContent() == 2){
					contentsList = this.getMsgContent(appMsg.getId());
					//不存在，则有异常，跳过这条
					if(contentsList == null || contentsList.size() == 0){
						continue;
					}
					wgMsg = getWgMessage(appMsg, contentsList);
					
				}
				else{
					//在另一个表没内容
					wgMsg = getWgMessage(appMsg, null);
				}
				wgMsgList.add(wgMsg);
			}
			
			return wgMsgList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取数据库mt记录异常。");
			return null;
		}
	}
	
	private List<LfAppMsgContent> getMsgContent(Long id){
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("cacheId", id.toString());
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("sortNum", StaticValue.ASC);
			List<LfAppMsgContent> contentsList = empDao.findListByCondition(LfAppMsgContent.class, conditionMap, orderbyMap);
			return contentsList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取LfAppMsgContent集合异常。");
			return null;
		}
	}
	
	private WgMessage getWgMessage(LfAppMsgcache appMsg, List<LfAppMsgContent> contentsList){
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
			
			if(contentsList == null || contentsList.size() == 0){
				return wgMsg;
			}
			
			int size = contentsList.size();
			
			if(size - 1 > -1 && contentsList.get(0) != null){
				wgMsg.setContent1(contentsList.get(0).getContent());
			}
			if(size - 2 > -1 && contentsList.get(1) != null){
				wgMsg.setContent2(contentsList.get(1).getContent());
			}
			if(size - 3 > -1 &&contentsList.get(2) != null){
				wgMsg.setContent3(contentsList.get(2).getContent());
			}
			if(size - 4 > -1 &&contentsList.get(3) != null){
				wgMsg.setContent4(contentsList.get(3).getContent());
			}
			if(size - 5 > -1 &&contentsList.get(4) != null){
				wgMsg.setContent5(contentsList.get(4).getContent());
			}
			if(size - 6 > -1 &&contentsList.get(5) != null){
				wgMsg.setContent6(contentsList.get(5).getContent());
			}
			if(size - 7 > -1 &&contentsList.get(6) != null){
				wgMsg.setContent7(contentsList.get(6).getContent());
			}
			if(size - 8 > -1 &&contentsList.get(7) != null){
				wgMsg.setContent8(contentsList.get(7).getContent());
			}
			if(size - 9 > -1 &&contentsList.get(8) != null){
				wgMsg.setContent9(contentsList.get(8).getContent());
			}
			if(size - 10 > -1 &&contentsList.get(9) != null){
				wgMsg.setContent10(contentsList.get(9).getContent());
			}
			
			return wgMsg;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取WgMessage对象异常。");
			return null;
		}
	}
	

}
