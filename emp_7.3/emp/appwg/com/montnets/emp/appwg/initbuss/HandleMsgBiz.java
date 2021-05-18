package com.montnets.emp.appwg.initbuss;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.packet.PacketExtension;
import org.json.simple.JSONObject;

import com.montnets.app.EMessage;
import com.montnets.app.EMessageModel;
import com.montnets.app.PMessage;
import com.montnets.app.PMessageModel;
import com.montnets.app.StateReportMessage;
import com.montnets.app.StateReportModel;
import com.montnets.app.style.PMessageStyle1;
import com.montnets.app.style.PMessageStyle2;
import com.montnets.app.style.PMessageStyle3;
import com.montnets.app.style.PMessageStyle4;
import com.montnets.emp.appwg.bean.AppMessage;
import com.montnets.emp.appwg.bean.AppStaticValue;
import com.montnets.emp.appwg.bean.WgMessage;
import com.montnets.emp.appwg.cache.RecMoCacheStorage;
import com.montnets.emp.appwg.cache.RecMtCacheStorage;
import com.montnets.emp.appwg.cache.RecRptCacheStorage;
import com.montnets.emp.appwg.cache.WinCacheStorage;
import com.montnets.emp.appwg.cache.Window;
import com.montnets.emp.appwg.dao.WgDAO;
import com.montnets.emp.appwg.entity.LfAppMoCache;
import com.montnets.emp.appwg.entity.LfAppMsgContent;
import com.montnets.emp.appwg.entity.LfAppMsgcache;
import com.montnets.emp.appwg.entity.LfAppRptCache;
import com.montnets.emp.appwg.initappplat.AppSdkPackage;
import com.montnets.emp.appwg.util.JsonUtil;
import com.montnets.emp.appwg.wginterface.IProcPMessage;
import com.montnets.emp.appwg.wginterface.IProcRptMessage;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.appmage.LfAppMtmsg;
import com.montnets.emp.entity.appmage.LfAppSitInfo;
import com.montnets.emp.entity.appmage.LfAppTcMomsg;

import static com.sun.mail.imap.protocol.INTERNALDATE.format;

/**
 * 
 * @project p_appwg
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-6-11 下午04:28:41
 * @description app网关与App梦网平台通信逻辑类
 */
public class HandleMsgBiz extends SuperBiz implements IInterfaceBuss
{
	
	private static IProcPMessage procPmsgImpl;
	
	private static IProcRptMessage procRptmsgImpl;
	
	private SimpleDateFormat sdfT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private WgDAO wgDao = new WgDAO();

	public static IProcPMessage getProcPmsgImpl() {
		return procPmsgImpl;
	}

	public static IProcRptMessage getProcRptmsgImpl() {
		return procRptmsgImpl;
	}

	/**
	 * 消息放进接收下行消息缓冲队列，同时要存库
	 * @return 返回0为成功。
	 */
	public int PutInRecMtCache(List<WgMessage> msgList){
		
		try
		{
			if(msgList == null || msgList.size() == 0){
				EmpExecutionContext.error("mt消息放缓存处理，消息集合为空。");
				return -1;
			}
			boolean putRes = false;
			
			//缓存中容量不充足，只存库，不放缓存
			if((AppStaticValue.REC_MT_QUEUE_SIZE - RecMtCacheStorage.getInstance().getSize()) - msgList.size() < 1){
				List<LfAppMsgcache> appMsgList = new ArrayList<LfAppMsgcache>();
				List<LfAppMsgContent> contentList = new ArrayList<LfAppMsgContent>();
				//获取保存对象，状态设置为未读
				putRes = msg2AppMsgcacheNoRead(appMsgList, contentList, msgList);
				if(!putRes){
					return -5;
				}
				//保存到数据库
				putRes = saveAppMsgDB(appMsgList, contentList);
				if(putRes){
					return 0;
				}
				else{
					EmpExecutionContext.error("mt消息放缓存处理，不放缓存，保存数据库记录失败。");
					return -2;
				}
			}
			
			//缓存中容量充足
			List<LfAppMsgcache> appMsgList = new ArrayList<LfAppMsgcache>();
			List<LfAppMsgContent> contentList = new ArrayList<LfAppMsgContent>();
			//获取保存对象
			putRes = msg2AppMsgcacheForReaded(appMsgList, contentList, msgList);
			if(!putRes){
				return -6;
			}
			//保存到数据库
			boolean saveRes = saveAppMsgDB(appMsgList, contentList);
			if(!saveRes){
				EmpExecutionContext.error("mt消息放缓存处理，放缓存，保存数据库记录失败。");
				return -3;
			}
			//放入缓存中
			putRes = putMtCache(msgList);

			if(putRes){
				return 0;
			}
			EmpExecutionContext.error("mt消息放缓存处理，放缓存失败。");
			return -4;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "mt消息放缓存处理，异常。");
			return -99;
		}
	}
	
	/**
	 * 消息放进接收下行消息缓冲队列，同时要存库
	 * @return 返回0为成功。
	 */
	public int PutInRecMoCache(List<WgMessage> msgList){
		
		try
		{
			if(msgList == null || msgList.size() == 0){
				EmpExecutionContext.error("mo消息放缓存处理，消息集合为null。");
				return -1;
			}
			boolean putRes = false;
			
			//缓存中容量不充足，只存库，不放缓存
			if((AppStaticValue.REC_MO_QUEUE_SIZE - RecMoCacheStorage.getInstance().getSize()) - msgList.size() < 1){
				//获取保存对象，状态设置为未读
				List<LfAppMoCache> appMsgList = msg2AppMoCacheNoRead(msgList);
				//保存到数据库
				putRes = saveAppMoCache(appMsgList);
				if(putRes){
					return 0;
				}
				else{
					EmpExecutionContext.error("mo消息放缓存处理，不放缓存，保存数据库记录失败。");
					return -2;
				}
			}
			
			//缓存中容量充足
			//获取保存对象
			List<LfAppMoCache> appMsgList = msg2AppMoCacheForReaded(msgList);
			//保存到数据库
			boolean saveRes = saveAppMoCache(appMsgList);
			if(!saveRes){
				EmpExecutionContext.error("mo消息放缓存处理，放缓存，保存数据库记录失败。");
				return -3;
			}
			//放入缓存中
			putRes = putMoCache(msgList);

			if(putRes){
				return 0;
			}
			EmpExecutionContext.error("mo消息放缓存处理，放缓存失败。");
			return -4;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "mo消息放缓存处理，异常。");
			return -99;
		}
	}
	
	/**
	 * 消息放进接收下行消息缓冲队列，同时要存库
	 * @return 返回0为成功。
	 */
	public int PutInRecRptCache(List<WgMessage> msgList){
		
		try
		{
			if(msgList == null || msgList.size() == 0){
				EmpExecutionContext.error("rpt消息放缓存处理，消息集合为null。");
				return -1;
			}
			boolean putRes = false;
			
			//缓存中容量不充足，只存库，不放缓存
			if((AppStaticValue.REC_RPT_QUEUE_SIZE - RecRptCacheStorage.getInstance().getSize()) - msgList.size() < 1){
				//获取保存对象，状态设置为未读
				List<LfAppRptCache> appMsgList = msg2AppRptCacheNoRead(msgList);
				//保存到数据库
				putRes = saveAppRptCache(appMsgList);
				if(putRes){
					return 0;
				}
				else{
					EmpExecutionContext.error("rpt消息放缓存处理，不放缓存，保存数据库记录失败。");
					return -2;
				}
			}
			
			//缓存中容量充足
			//获取保存对象
			List<LfAppRptCache> appMsgList = msg2AppRptCacheForReaded(msgList);
			//保存到数据库
			boolean saveRes = saveAppRptCache(appMsgList);
			if(!saveRes){
				EmpExecutionContext.error("rpt消息放缓存处理，放缓存，保存数据库记录失败。");
				return -3;
			}
			//放入缓存中
			putRes = putRptCache(msgList);

			if(putRes){
				return 0;
			}
			EmpExecutionContext.error("rpt消息放缓存处理，放缓存失败。");
			return -4;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "rpt消息放缓存处理，异常。");
			return -99;
		}
	}
	
	private boolean putMtCache(List<WgMessage> msgList){
		try
		{
			StringBuffer sb = new StringBuffer();
			boolean putRes = false;
			int count = 0;
			
			//用于放缓存失败更新
			Map<String,String> objectMap = new HashMap<String,String>();
			objectMap.put("sendState", String.valueOf(AppStaticValue.SEND_STATE_NOREAD));
			
			Map<String,String> conditionMap = new HashMap<String,String>();
			
			for(WgMessage wgMsg : msgList){
				putRes = RecMtCacheStorage.getInstance().produceRecMt(wgMsg);
				//把放不成功的id保存下来，后面把状态更新为未读
				if(!putRes){
					sb.append(wgMsg.getId()).append(",");
					count++;
				}
				//1千个id更新一次库
				if(count > 0 && count % 1000 == 0 && sb.length() > 0){
					//截掉最后一个逗号
					sb.delete(sb.length()-1, sb.length());
					conditionMap.put("id", sb.toString());
					wgDao.updateMtDB(objectMap, conditionMap);
					conditionMap.clear();
					sb.setLength(0);
				}
			}
			
			if(sb.length() > 0){
				//截掉最后一个逗号
				sb.delete(sb.length()-1, sb.length());
				conditionMap.put("id", sb.toString());
				wgDao.updateMtDB(objectMap, conditionMap);
				conditionMap.clear();
				sb.setLength(0);
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "mt消息保存后，放入缓存异常。");
			return false;
		}
	}
	
	private boolean putMoCache(List<WgMessage> msgList){
		try
		{
			StringBuffer sb = new StringBuffer();
			boolean putRes = false;
			int count = 0;
			
			//用于放缓存失败更新
			Map<String,String> objectMap = new HashMap<String,String>();
			//更新为未读
			objectMap.put("sendState", String.valueOf(AppStaticValue.SEND_STATE_NOREAD));
			
			Map<String,String> conditionMap = new HashMap<String,String>();
			
			for(WgMessage wgMsg : msgList){
				putRes = RecMoCacheStorage.getInstance().produceRecMo(wgMsg);
				//把放不成功的id保存下来，后面把状态更新为未读
				if(!putRes){
					sb.append(wgMsg.getId()).append(",");
					count++;
				}
				//1千个id更新一次库
				if(count > 0 && count % 1000 == 0 && sb.length() > 0){
					//截掉最后一个逗号
					sb.delete(sb.length()-1, sb.length());
					conditionMap.put("id", sb.toString());
					wgDao.updateMoDB(objectMap, conditionMap);
					conditionMap.clear();
					sb.setLength(0);
				}
			}
			
			if(sb.length() > 0){
				//截掉最后一个逗号
				sb.delete(sb.length()-1, sb.length());
				conditionMap.put("id", sb.toString());
				wgDao.updateMoDB(objectMap, conditionMap);
				conditionMap.clear();
				sb.setLength(0);
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "mo消息保存后，放入缓存异常。");
			return false;
		}
	}
	
	private boolean putRptCache(List<WgMessage> msgList){
		try
		{
			StringBuffer sb = new StringBuffer();
			boolean putRes = false;
			int count = 0;
			
			//用于放缓存失败更新
			Map<String,String> objectMap = new HashMap<String,String>();
			//更新为未读
			objectMap.put("sendState", String.valueOf(AppStaticValue.SEND_STATE_NOREAD));
			
			Map<String,String> conditionMap = new HashMap<String,String>();
			
			for(WgMessage wgMsg : msgList){
				putRes = RecRptCacheStorage.getInstance().produceRecRpt(wgMsg);
				//把放不成功的id保存下来，后面把状态更新为未读
				if(!putRes){
					sb.append(wgMsg.getId()).append(",");
					count++;
				}
				//1千个id更新一次库
				if(count > 0 && count % 1000 == 0 && sb.length() > 0){
					//截掉最后一个逗号
					sb.delete(sb.length()-1, sb.length());
					conditionMap.put("id", sb.toString());
					wgDao.updateRptDB(objectMap, conditionMap);
					conditionMap.clear();
					sb.setLength(0);
				}
			}
			
			if(sb.length() > 0){
				//截掉最后一个逗号
				sb.delete(sb.length()-1, sb.length());
				conditionMap.put("id", sb.toString());
				wgDao.updateRptDB(objectMap, conditionMap);
				conditionMap.clear();
				sb.setLength(0);
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "rpt消息保存后，放入缓存异常。");
			return false;
		}
	}
	
	private boolean msg2AppMsgcacheNoRead(List<LfAppMsgcache> appmsgList, List<LfAppMsgContent> contentList, List<WgMessage> msgList){
		try
		{
			LfAppMsgcache appmsg = null;
			
			long id = empDao.getIdByPro(27, msgList.size());
			boolean getRes = false;
			for(WgMessage wgMsg : msgList){
				wgMsg.setId(id);
				//设置未读状态
				wgMsg.setSendState(AppStaticValue.SEND_STATE_NOREAD);
				appmsg = new LfAppMsgcache();
				getRes = getLfAppMsgcache(appmsg, contentList, wgMsg);
				if(!getRes){
					EmpExecutionContext.error("mt已读消息转换为存库实体集合失败。");
					return false;
				}
				
				appmsgList.add(appmsg);
				id = id - 1;
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "已读消息转换为存库实体集合异常。");
			return false;
		}
	}
	
	private List<LfAppMoCache> msg2AppMoCacheNoRead(List<WgMessage> msgList){
		try
		{
			List<LfAppMoCache> appmsgList = new ArrayList<LfAppMoCache>();
			
			LfAppMoCache appmsg = null;
			
			long id = empDao.getIdByPro(28, msgList.size());
			
			for(WgMessage wgMsg : msgList){
				wgMsg.setId(id);
				//设置未读状态
				wgMsg.setSendState(AppStaticValue.SEND_STATE_NOREAD);
				appmsg = getLfAppMoCache(wgMsg);
				appmsgList.add(appmsg);
				id = id - 1;
			}
			return appmsgList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "Mo已读消息转换为存库实体集合异常。");
			return null;
		}
	}
	
	private List<LfAppRptCache> msg2AppRptCacheNoRead(List<WgMessage> msgList){
		try
		{
			List<LfAppRptCache> appmsgList = new ArrayList<LfAppRptCache>();
			
			LfAppRptCache appmsg = null;
			
			long id = empDao.getIdByPro(29, msgList.size());
			
			for(WgMessage wgMsg : msgList){
				wgMsg.setId(id);
				//设置未读状态
				wgMsg.setSendState(AppStaticValue.SEND_STATE_NOREAD);
				appmsg = getLfAppRptCache(wgMsg);
				appmsgList.add(appmsg);
				id = id - 1;
			}
			return appmsgList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "Rpt已读消息转换为存库实体集合异常。");
			return null;
		}
	}
	
	private boolean msg2AppMsgcacheForReaded(List<LfAppMsgcache> appmsgList, List<LfAppMsgContent> contentList, List<WgMessage> msgList){
		try
		{
			LfAppMsgcache appmsg = null;
			
			long id = empDao.getIdByPro(27, msgList.size());
			boolean getRes = false;
			
			for(WgMessage wgMsg : msgList){
				
				wgMsg.setId(id);
				//发送状态。已读待发送
				wgMsg.setSendState(AppStaticValue.SEND_STATE_WAIT_SEND);
				
				appmsg = new LfAppMsgcache();
				getRes = getLfAppMsgcache(appmsg, contentList, wgMsg);
				if(!getRes){
					EmpExecutionContext.error("mt已读消息转换为存库实体集合失败。");
					return false;
				}
				
				appmsgList.add(appmsg);
				id = id - 1;
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "mt已读消息转换为存库实体集合异常。");
			return false;
		}
	}
	
	private List<LfAppMoCache> msg2AppMoCacheForReaded(List<WgMessage> msgList){
		try
		{
			List<LfAppMoCache> appmsgList = new ArrayList<LfAppMoCache>();
			
			LfAppMoCache appmsg = null;
			
			long id = empDao.getIdByPro(28, msgList.size());
			
			for(WgMessage wgMsg : msgList){
				
				wgMsg.setId(id);
				//发送状态。已读待发送
				wgMsg.setSendState(AppStaticValue.SEND_STATE_WAIT_SEND);
				appmsg = getLfAppMoCache(wgMsg);
				appmsgList.add(appmsg);
				id = id - 1;
			}
			return appmsgList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "mo已读消息转换为存库实体集合异常。");
			return null;
		}
	}
	
	private List<LfAppRptCache> msg2AppRptCacheForReaded(List<WgMessage> msgList){
		try
		{
			List<LfAppRptCache> appmsgList = new ArrayList<LfAppRptCache>();
			
			LfAppRptCache appmsg = null;
			
			long id = empDao.getIdByPro(29, msgList.size());
			
			for(WgMessage wgMsg : msgList){
				
				wgMsg.setId(id);
				//发送状态。已读待发送
				wgMsg.setSendState(AppStaticValue.SEND_STATE_WAIT_SEND);
				appmsg = getLfAppRptCache(wgMsg);
				appmsgList.add(appmsg);
				id = id - 1;
			}
			return appmsgList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "rpt已读消息转换为存库实体集合异常。");
			return null;
		}
	}
	
	private boolean saveAppMsgDB(List<LfAppMsgcache> appmsgList, List<LfAppMsgContent> contentList){
		if(appmsgList == null || appmsgList.size() == 0){
			return false;
		}
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			int res = empTransDao.save(conn, appmsgList, LfAppMsgcache.class);
			empTransDao.save(conn, contentList, LfAppMsgContent.class);
			
			if(res > 0){
				empTransDao.commitTransaction(conn);
				return true;
			}
			else{
				empTransDao.rollBackTransaction(conn);
				return false;
			}
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "保存LfAppMsgcache到数据库集合异常。");
			return false;
		}
		finally{
			empTransDao.closeConnection(conn);
		}
	}
	
	private boolean saveAppMoCache(List<LfAppMoCache> appmsgList){
		try
		{
			if(appmsgList == null || appmsgList.size() == 0){
				return false;
			}
			int res = empDao.save(appmsgList, LfAppMoCache.class);
			if(res > 0){
				return true;
			}
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "保存LfAppMoCache到数据库集合异常。");
			return false;
		}
	}
	
	private boolean saveAppRptCache(List<LfAppRptCache> appmsgList){
		try
		{
			if(appmsgList == null || appmsgList.size() == 0){
				return false;
			}
			int res = empDao.save(appmsgList, LfAppRptCache.class);
			if(res > 0){
				return true;
			}
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "保存LfAppRptCache到数据库集合异常。");
			return false;
		}
	}
	
	private boolean getLfAppMsgcache(LfAppMsgcache appmsg, List<LfAppMsgContent> contentList, WgMessage wgMsg){
		try
		{
			appmsg.setId(wgMsg.getId());
			appmsg.setAppid(wgMsg.getAppId());
			appmsg.setBody(wgMsg.getBody());
			appmsg.setCreatetime(new Timestamp(System.currentTimeMillis()));
			appmsg.setEcode(wgMsg.getEcCode());
			appmsg.setEmptype(wgMsg.getEmtype());
			appmsg.setFname(wgMsg.getFromName());
			appmsg.setFromjid(wgMsg.getFrom());
			appmsg.setMsgid(wgMsg.getMsgId());
			appmsg.setMsgsrc(wgMsg.getMsgSrc());
			appmsg.setMsgtype(wgMsg.getMsgType());
			appmsg.setReadstate(wgMsg.getReadState());
			appmsg.setSendedcount(wgMsg.getSendedCount());
			appmsg.setSendstate(wgMsg.getSendState());
			appmsg.setSerial(wgMsg.getSerial());
			appmsg.setTname(wgMsg.getToName());
			appmsg.setTojid(wgMsg.getTo());
			appmsg.setTotype(wgMsg.getToType());
			appmsg.setValidity(wgMsg.getValidity());
			
			if(wgMsg.getContent1() != null && wgMsg.getContent1().length() > 0){
				//设置内容
				LfAppMsgContent content1 = new LfAppMsgContent();
				content1.setCacheId(appmsg.getId());
				content1.setContent(wgMsg.getContent1());
				content1.setMsgType(1);
				content1.setSortNum(1);
				contentList.add(content1);
				//设置为有内容在另一个表
				appmsg.setHasContent(2);
			}
			
			if(wgMsg.getContent2() != null && wgMsg.getContent2().length() > 0){
				LfAppMsgContent content2 = new LfAppMsgContent();
				content2.setCacheId(appmsg.getId());
				content2.setContent(wgMsg.getContent2());
				content2.setMsgType(1);
				content2.setSortNum(2);
				contentList.add(content2);
				//设置为有内容在另一个表
				appmsg.setHasContent(2);
			}
			
			if(wgMsg.getContent3() != null && wgMsg.getContent3().length() > 0){
				LfAppMsgContent content3 = new LfAppMsgContent();
				content3.setCacheId(appmsg.getId());
				content3.setContent(wgMsg.getContent3());
				content3.setMsgType(1);
				content3.setSortNum(3);
				contentList.add(content3);
				//设置为有内容在另一个表
				appmsg.setHasContent(2);
			}
			
			if(wgMsg.getContent4() != null && wgMsg.getContent4().length() > 0){
				LfAppMsgContent content4 = new LfAppMsgContent();
				content4.setCacheId(appmsg.getId());
				content4.setContent(wgMsg.getContent4());
				content4.setMsgType(1);
				content4.setSortNum(4);
				contentList.add(content4);
				//设置为有内容在另一个表
				appmsg.setHasContent(2);
			}
			
			if(wgMsg.getContent5() != null && wgMsg.getContent5().length() > 0){
				LfAppMsgContent content5 = new LfAppMsgContent();
				content5.setCacheId(appmsg.getId());
				content5.setContent(wgMsg.getContent5());
				content5.setMsgType(1);
				content5.setSortNum(5);
				contentList.add(content5);
				//设置为有内容在另一个表
				appmsg.setHasContent(2);
			}
			
			if(wgMsg.getContent6() != null && wgMsg.getContent6().length() > 0){
				LfAppMsgContent content6 = new LfAppMsgContent();
				content6.setCacheId(appmsg.getId());
				content6.setContent(wgMsg.getContent6());
				content6.setMsgType(1);
				content6.setSortNum(6);
				contentList.add(content6);
				//设置为有内容在另一个表
				appmsg.setHasContent(2);
			}
			
			if(wgMsg.getContent7() != null && wgMsg.getContent7().length() > 0){
				LfAppMsgContent content7 = new LfAppMsgContent();
				content7.setCacheId(appmsg.getId());
				content7.setContent(wgMsg.getContent7());
				content7.setMsgType(1);
				content7.setSortNum(7);
				contentList.add(content7);
				//设置为有内容在另一个表
				appmsg.setHasContent(2);
			}
			
			if(wgMsg.getContent8() != null && wgMsg.getContent8().length() > 0){
				LfAppMsgContent content8 = new LfAppMsgContent();
				content8.setCacheId(appmsg.getId());
				content8.setContent(wgMsg.getContent8());
				content8.setMsgType(1);
				content8.setSortNum(8);
				contentList.add(content8);
				//设置为有内容在另一个表
				appmsg.setHasContent(2);
			}
			
			if(wgMsg.getContent9() != null && wgMsg.getContent9().length() > 0){
				LfAppMsgContent content9 = new LfAppMsgContent();
				content9.setCacheId(appmsg.getId());
				content9.setContent(wgMsg.getContent9());
				content9.setMsgType(1);
				content9.setSortNum(9);
				contentList.add(content9);
				//设置为有内容在另一个表
				appmsg.setHasContent(2);
			}
			
			if(wgMsg.getContent10() != null && wgMsg.getContent10().length() > 0){
				LfAppMsgContent content10 = new LfAppMsgContent();
				content10.setCacheId(appmsg.getId());
				content10.setContent(wgMsg.getContent10());
				content10.setMsgType(1);
				content10.setSortNum(10);
				contentList.add(content10);
				//设置为有内容在另一个表
				appmsg.setHasContent(2);
			}
			
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取LfAppMsgcache对象异常。");
			return false;
		}
	}
	
	private LfAppMoCache getLfAppMoCache(WgMessage wgMsg){
		try
		{
			LfAppMoCache appmsg = new LfAppMoCache();
			appmsg.setId(wgMsg.getId());
			appmsg.setAppid(wgMsg.getAppId());
			appmsg.setBody(wgMsg.getBody());
			appmsg.setCreatetime(new Timestamp(System.currentTimeMillis()));
			appmsg.setEcode(wgMsg.getEcCode());
			appmsg.setEmptype(wgMsg.getEmtype());
			appmsg.setFname(wgMsg.getFromName());
			appmsg.setFromjid(wgMsg.getFrom());
			appmsg.setMsgid(wgMsg.getMsgId());
			appmsg.setMsgsrc(wgMsg.getMsgSrc());
			appmsg.setMsgtype(wgMsg.getMsgType());
			appmsg.setReadstate(wgMsg.getReadState());
			appmsg.setSendedcount(wgMsg.getSendedCount());
			appmsg.setSendstate(wgMsg.getSendState());
			appmsg.setSerial(wgMsg.getSerial());
			appmsg.setTname(wgMsg.getToName());
			appmsg.setTojid(wgMsg.getTo());
			appmsg.setTotype(wgMsg.getToType());
			appmsg.setValidity(wgMsg.getValidity());
			appmsg.setOutlinemsg(wgMsg.getOutlinemsg());
			
			return appmsg;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取LfAppMoCache对象异常。");
			return null;
		}
	}
	
	private LfAppRptCache getLfAppRptCache(WgMessage wgMsg){
		try
		{
			LfAppRptCache appmsg = new LfAppRptCache();
			appmsg.setId(wgMsg.getId());
			appmsg.setAppid(wgMsg.getAppId());
			appmsg.setBody(wgMsg.getBody());
			appmsg.setCreatetime(new Timestamp(System.currentTimeMillis()));
			appmsg.setEcode(wgMsg.getEcCode());
			appmsg.setEmptype(wgMsg.getEmtype());
			appmsg.setMsgid(wgMsg.getMsgId());
			appmsg.setMsgtype(wgMsg.getMsgType());
			appmsg.setReadstate(wgMsg.getReadState());
			appmsg.setSendedcount(wgMsg.getSendedCount());
			appmsg.setSendstate(wgMsg.getSendState());
			appmsg.setSerial(wgMsg.getSerial());
			appmsg.setValidity(wgMsg.getValidity());
			
			appmsg.setIcode(wgMsg.getiCode());
			appmsg.setUserName(wgMsg.getUserName());
			appmsg.setSendResult(wgMsg.getSendResult());
			appmsg.setErrCode(wgMsg.getErrCode());
			
			return appmsg;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取LfAppRptCache对象异常。");
			return null;
		}
	}
	
	/**
	 * 下行响应消息处理，接收到下行响应消息会调用这方法
	 * @return
	 */
	public boolean MtRespRecHandle(PMessage pMsg){
		//解析消息
		//处理发送窗口
		//响应成功
		try
		{
			Window win = WinCacheStorage.getInstance().getWindow(pMsg.getPacketID());
			if(win == null){
				return false;
			}
			
			if(pMsg.getRcode() != null && pMsg.getRcode() == 0){
				//响应成功，则移除窗口
				WinCacheStorage.getInstance().removeWindow(pMsg.getPacketID());
				//更新数据库记录为成功
				wgDao.updateSendResult(win.getMessage().getId().toString(), "2", "2");
				return true;
			}
			//响应失败
			
			//移除窗口
			WinCacheStorage.getInstance().removeWindow(pMsg.getPacketID());
			
			boolean res = mtRespFailDeal(win.getMessage());
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理个人消息响应异常。");
			return false;
		}
	}
	
	private boolean mtRespFailDeal(WgMessage wgMsg){
		try
		{
			boolean res = false;
			//缓冲已满
			if(AppStaticValue.REC_MT_QUEUE_SIZE - RecMtCacheStorage.getInstance().getSize() == 0){
				//更新数据库记录为失败
				res = wgDao.updateSendResult(wgMsg.getId().toString(), "3", "1");
			}else{
				//再把消息放到发送队列，等下次发送
				RecMtCacheStorage.getInstance().produceRecMt(wgMsg);
				//更新数据库记录为失败
				res = wgDao.updateSendResult(wgMsg.getId().toString(), "3", "2");
			}
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理失败的mt响应异常。");
			return false;
			
		}
	}
	
	public boolean MtRespRecHandle(EMessage eMsg){
		
		Window win = WinCacheStorage.getInstance().getWindow(eMsg.getPacketID());
		if(win == null){
			return false;
		}
		
		//响应成功
		if(eMsg.getRcode() != null && eMsg.getRcode() == 0){
			//响应成功，则移除窗口
			WinCacheStorage.getInstance().removeWindow(eMsg.getPacketID());
			//更新数据库记录为成功
			wgDao.updateSendResult(win.getMessage().getId().toString(), "2", "2");
			return true;
		}
		//响应失败
		
		
		//移除窗口
		WinCacheStorage.getInstance().removeWindow(eMsg.getPacketID());
		
		boolean res = mtRespFailDeal(win.getMessage());
		
		return res;
	}
	
	/**
	 * 上行消息处理，接收到上行消息会调用这方法
	 * @return
	 */
	public boolean MoRecHandle(PMessage pmessage){
		if(pmessage == null){
			EmpExecutionContext.error("接收个人消上行息，pmessage为null。");
			return false;
		}
		try
		{
			EmpExecutionContext.info("rece mo:"+pmessage.toXML());
			EmpExecutionContext.appRequestInfoLog("rece mo:"+pmessage.toXML());
			
			if(pmessage.getPModel() == null){
				sendRecPMsgReceipt(pmessage, false);
				EmpExecutionContext.error("接收个人上行消息，PModel为null。xml:"+pmessage.toXML());
				return false;
			}
			
			//是否重号
			boolean isRepeat = isRepeatSerial(HandleStaticValue.moReStoreCache, pmessage.getSerial());
			if(isRepeat)
			{
				sendRecPMsgReceipt(pmessage, true);
				EmpExecutionContext.info("个人上行消息流水号重复，流水号："+pmessage.getSerial());
				return true;
			}
			
			WgMessage wgMsg = getWgMessage(pmessage);
			
			List<WgMessage> msgList = new ArrayList<WgMessage>();
			msgList.add(wgMsg);
			
			int putRes = PutInRecMoCache(msgList);
			if(putRes == 0){
				sendRecPMsgReceipt(pmessage, true);
				return true;
			}
			sendRecPMsgReceipt(pmessage, false);
			EmpExecutionContext.info("接收个人上行消息，放缓存失败。流水号："+pmessage.getSerial());
			return false;
		}
		catch (Exception e)
		{
			sendRecPMsgReceipt(pmessage, false);
			EmpExecutionContext.error(e, "接受处理个人上行信息异常。xml:"+pmessage.toXML());
			return false;
		}
	}
	
	private WgMessage getWgMessage(PMessage appMsg){
		try
		{
			WgMessage wgMsg = new WgMessage();
			
			wgMsg.setFrom(appMsg.getPModel().getFrom());
			wgMsg.setFromName(appMsg.getPModel().getFname());
			wgMsg.setTo(appMsg.getPModel().getTo());
			wgMsg.setMsgSrc(appMsg.getPModel().getMsg_src());
			
			String bodyJson = getMoBodyJson(appMsg);
			wgMsg.setBody(bodyJson);
			
			wgMsg.setSerial(appMsg.getSerial());
			wgMsg.setEcCode(appMsg.getEcode());
			//个人消息
			wgMsg.setMsgType(2);
			
			//标记为离线消息。0在线消息；1离线消息
			Integer outlinemsg = 0;
			if(appMsg.getExtension("x", "jabber:x:delay") != null){
				outlinemsg = 1;
			}
			//设置离线标识
			wgMsg.setOutlinemsg(outlinemsg);
			
			return wgMsg;
		}
		catch (Exception e) {
			EmpExecutionContext.error(e, "获取WgMessage异常。");
			return null;
		}
	}
	
	private WgMessage getWgMessage(StateReportMessage appMsg){
		try
		{
			WgMessage wgMsg = new WgMessage();
			
			wgMsg.setSerial(appMsg.getSerial());
			wgMsg.setEcCode(appMsg.getEcode());
			wgMsg.setiCode(appMsg.getIcode());
			wgMsg.setSendResult(appMsg.getStateReportModel().getState());
			wgMsg.setErrCode(appMsg.getStateReportModel().getErrcode());
			wgMsg.setUserName(appMsg.getStateReportModel().getUsername());
			
			return wgMsg;
		}
		catch (Exception e) {
			EmpExecutionContext.error(e, "获取WgMessage异常。");
			return null;
		}
	}
	
	/**
	 * 获取json格式字符串
	 * @param /pMsgModel 构造参数
	 * @return 返回json格式字符串，异常返回null
	 */
	public String getMoBodyJson(PMessage pmessage){
		if(pmessage.getPModel() == null){
			EmpExecutionContext.error("获取MoBodyJson格式字符串，PMessageModel对象为null。");
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
				.append("{");
			
			if(pMsgModel.getBody() == null || pMsgModel.getBody().size() == 0){
				EmpExecutionContext.error("获取MoBodyJson格式字符串，pMessageStyles为空。");
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
					
				}
				//样式2
				else if(pMsgModel.getBody().get(i).getStyle() == 2){
					
					PMessageStyle2 style2 = (PMessageStyle2)pMsgModel.getBody().get(i);
					sb
					.append("\"PMessageStyle2\":")
					.append("{")
					.append("\"pic\":").append("\"").append(style2.getPic()).append("\"")
					.append("}");
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
				}
				
				if(i < size - 1){
					sb.append(",");
				}
			}
			sb.append("}");
			
			return sb.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取MoBodyJson格式字符串异常。");
			return null;
		}
	}
	
	/**
	 * 个人状态报告消息处理，接收到状态报告消息会调用这方法
	 * @return
	 */
	public boolean RptPMRecHandle(PMessage pMsg){
		
		try
		{
			if(pMsg == null){
				EmpExecutionContext.error("接收并更新个人消息响应，PMessage对象为null。");
				return false;
			}
			EmpExecutionContext.info("接收个人消息响应消息xml:"+pMsg.toXML());
			
			PMessageModel pMsgModel = pMsg.getPModel();
			if(pMsgModel == null){
				EmpExecutionContext.error("接收并更新个人消息响应，PMessageModel对象为null。");
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
			else
			{
				//是否重号
				boolean isRepeat = isRepeatSerial(HandleStaticValue.pmRptReStoreCache, pMsg.getSerial());
				if(isRepeat)
				{
					sendRecPMsgReceipt(pMsg, true);
					EmpExecutionContext.info("个人消息响应流水号重复，流水号："+pMsg.getSerial());
					return true;
				}
				
				if(pMsgModel.getStatus() != null){
					state = pMsgModel.getStatus()+"0";
				}
				//状态报告
				if(pMsg.getIcode() == 207){
					res = updateMtMsg(state, pMsg.getSerial(), pMsgModel.getTo());
					if(!res){
						EmpExecutionContext.error("个人消息响应，保存消息记录失败。流水号："+pMsg.getSerial());
						//发送失败的回执
						sendRecPMsgReceipt(pMsg, false);
					}
					else
					{
						//发送成功的回执
						sendRecPMsgReceipt(pMsg, true);
					}
				}
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
	 * 状态报告消息处理，接收到状态报告消息会调用这方法
	 * @return
	 */
	public boolean RptMsgRecHandle(StateReportMessage rptMsg){
		
		try
		{
			if(rptMsg == null){
				EmpExecutionContext.error("接收并更新消息状态报告，StateReportMessage对象为null。");
				return false;
			}
			EmpExecutionContext.info("rece rpt:"+rptMsg.toXML());
			EmpExecutionContext.appRequestInfoLog("rece rpt:"+rptMsg.toXML());
			
			StateReportModel pMsgModel = rptMsg.getStateReportModel();
			if(pMsgModel == null){
				sendRptMsgReceipt(rptMsg, false);
				EmpExecutionContext.error("接收并更新消息状态报告，StateReportModel对象为null。xml:"+rptMsg.toXML());
				return false;
			}
			
			boolean res = false;
			
//			//是否重号
//			boolean isRepeat = isRepeatSerial(HandleStaticValue.pmRptReStoreCache, rptMsg.getSerial());
//			if(isRepeat)
//			{
//				sendRptMsgReceipt(rptMsg, true);
//				EmpExecutionContext.info("消息状态报告流水号重复，流水号："+rptMsg.getSerial());
//				return true;
//			}
			
			WgMessage wgMsg = getWgMessage(rptMsg);
			
			List<WgMessage> msgList = new ArrayList<WgMessage>();
			msgList.add(wgMsg);
			
			int putRes = PutInRecRptCache(msgList);
			if(putRes == 0){
				sendRptMsgReceipt(rptMsg, true);
				return true;
			}
			
			sendRptMsgReceipt(rptMsg, false);
			EmpExecutionContext.info("更新消息状态报告到下行记录："+res+",serial="+rptMsg.getSerial());
			return false;
		}
		catch (Exception e)
		{
			sendRptMsgReceipt(rptMsg, false);
			EmpExecutionContext.error(e, "接收并更新消息状态报告异常。xml:"+(rptMsg!= null?rptMsg.toXML():""));
			return false;
		}
	}

	/**
	 * 公共状态报告消息处理，接收到状态报告消息会调用这方法
	 * @return
	 */
	public boolean RptEMRecHandle(EMessage eMsg){
		
		try
		{
			if(eMsg == null){
				EmpExecutionContext.error("处理公众消息响应失败，EMessage消息对象为null。");
				return false;
			}
			
			EmpExecutionContext.info("接收公众消息响应消息xml:"+eMsg.toXML());
			
			EMessageModel eMsgModel = eMsg.getEModel();
			if(eMsgModel == null){
				EmpExecutionContext.error("处理公众消息响应失败，EMessageModel消息对象为null。");
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
			else
			{
				//是否重号
//				boolean isRepeat = isRepeatSerial(HandleStaticValue.emRptReStoreCache, eMsg.getSerial());
//				if(isRepeat)
//				{
//					sendRecEMsgReceipt(eMsg, true);
//					EmpExecutionContext.info("公众消息响应流水号重复，流水号："+eMsg.getSerial());
//					return true;
//				}
				
				//消息中心推送app网关
				if(eMsg.getIcode() == 203 && eMsgModel.getEmtype() == 2){
					res = updateMtMsg(String.valueOf(eMsg.getRcode())+"0", eMsg.getSerial(), eMsgModel.getTo());
					sendRecEMsgReceipt(eMsg, res);
				}
				//消息中心推送app网关，，且是设置首页消息 
				else if(eMsg.getIcode() == 203 && eMsgModel.getEmtype() == 1){
					res = updateAppSitInfoReceipt(String.valueOf(eMsg.getRcode())+"0", eMsg.getSerial(), eMsgModel.getId());
					sendRecEMsgReceipt(eMsg, res);
				}
			}
			EmpExecutionContext.info("更新公众消息响应记录结果："+res+";serial="+eMsg.getSerial());
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理公众消息响应异常。xml:"+(eMsg!=null? eMsg.toXML():""));
			return false;
		}
		//处理，验证，过滤重复
		//存库
		//回应
		//推送
	}
	
	
	
	
	/**
	 * 发送APP消息
	 * @param appMsg
	 * @return 
	 * 1:提交成功。
	 * 0：未登录。
	 * -1:提交失败。
	 * -2:appMsg对象为null。
	 * -3:Ecode为null。
	 * -4:FromUserName为null。
	 * -5:ToUserNameSet为null。
	 * -6:发送参数集合对象为null。
	 * -7:下行消息集合对象为null。
	 * -8：保存消息记录异常。
	 * -9：获取并保存消息记录异常。
	 * 
	 */
	public int SendAppPublicMsg(AppMessage appMsg){
		
		if(appMsg == null){
			EmpExecutionContext.error("发送App消息失败，appMsg对象为null。");
			return -2;
		}
		if(appMsg.getEcode() == null || appMsg.getEcode().length() == 0){
			EmpExecutionContext.error("发送App消息失败，Ecode为null。");
			return -3;
		}
		if(appMsg.getFromUserName() == null || appMsg.getFromUserName().length() == 0){
			EmpExecutionContext.error("发送App消息失败，FromUserName为null。");
			return -4;
		}
		if(appMsg.getToUserNameSet() == null || appMsg.getToUserNameSet().size() == 0){
			EmpExecutionContext.error("发送App消息失败，ToUserNameSet为null。");
			return -5;
		}
		if(AppSdkPackage.getInstance().getXmppConnState() != 1){
			//再登录
			/*int loginRes = login(host, port, ecode, corpUser, password);
			if(loginRes != 1){
				EmpExecutionContext.error("发送App消息失败，未登录。");
				return 0;
			}*/
			EmpExecutionContext.error("发送App消息失败，未登录。");
			return 0;
			
		}
		
		EmpExecutionContext.info("发送App消息接口被调用：" +
				"taskid="+appMsg.getTaskId()+
				";MsgType="+appMsg.getMsgType()+
				";MsgContent="+appMsg.getMsgContent()+
				";Url="+appMsg.getUrl()
				);
		
		List<WgMessage> msgList = null;
		//流水号集合，一个元素存一个流水号
		List<String> serialList = null;
		try
		{
			List<LfAppMtmsg> allMtMsgList = new ArrayList<LfAppMtmsg>();
			
			//保存发送的流水号集合，用户失败后更新发送记录状态
			serialList = new ArrayList<String>();
			
			//发送公众消息直接传企业编码给FROM
			appMsg.setFromUserName(appMsg.getEcode());
			
			msgList = getWgMsgList(appMsg, allMtMsgList, serialList);
			if(msgList == null || msgList.size() == 0){
				EmpExecutionContext.error("发送App消息失败，发送参数集合对象为null。");
				return -6;
			}
			if(allMtMsgList == null || allMtMsgList.size() == 0 || serialList == null || serialList.size() == 0){
				EmpExecutionContext.error("发送App消息失败，下行消息集合对象为null。");
				return -7;
			}
			
			//保存每条消息记录
			int saveRes = empDao.save(allMtMsgList, LfAppMtmsg.class);
			//保存失败
			if(saveRes == 0){
				EmpExecutionContext.error("发送APP消息，保存消息记录失败。");
				return -8;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送APP消息，获取并保存消息记录异常。");
			return -9;
		}
		
		try
		{
			//发送消息到app平台
			PutInRecMtCache(msgList);
			
			EmpExecutionContext.info("发送App消息接口被调用发送成功：" +
					"taskid="+appMsg.getTaskId()+
					";MsgType="+appMsg.getMsgType()+
					";MsgContent="+appMsg.getMsgContent()+
					";Url="+appMsg.getUrl()
					);
			
			return 1;
		}
		catch (Exception e)
		{
			//提交失败，需要更新状态
			boolean udapteRes = updateMtMsgForFail(serialList);
			EmpExecutionContext.error("发送App消息，提交失败，更新发送记录提交状态结果："+udapteRes+"；taskid="+appMsg.getTaskId());
			
			EmpExecutionContext.error(e, "发送App消息，提交消息到APP平台异常。taskid="+appMsg.getTaskId());
			return -1;
		}
	}
	
	/**
	 * 发送失败则更新发送记录的提交状态为失败
	 * @param serialList 流水号集合
	 * @return 成功返回true
	 */
	private boolean updateMtMsgForFail(List<String> serialList){
		
		//流水号字符串，格式为id1,id2,id3，每个元素最多1000个流水号
		List<String> serialList1k = getSerialList(serialList);
		if(serialList1k == null || serialList1k.size() == 0){
			EmpExecutionContext.error("发送失败则更新发送记录的提交状态为失败，获取流水号为空。");
			return false;
		}
		
		Connection conn = empTransDao.getConnection();
		try
		{
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			//1.提交成功；2.提交失败；
			objectMap.put("sendstate", "2");
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			
			empTransDao.beginTransaction(conn);
			
			//循环更新，每次更新1000个流水号的记录状态
			for(String serials : serialList1k){
				conditionMap.clear();
				conditionMap.put("appMsgId&in", serials);
				Integer res = empTransDao.updateBySymbolsCondition(conn, LfAppMtmsg.class, objectMap, conditionMap);
				//没更新到，则返回
				if(res == null || res == 0){
					empTransDao.rollBackTransaction(conn);
					EmpExecutionContext.error("循环更新发送状态失败。serials="+serials);
					return false;
				}
			}
			
			empTransDao.commitTransaction(conn);
			
			return true;
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "发送失败则更新发送记录的提交状态为失败异常。");
			return false;
		}
		finally{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 发送设置首页消息失败则更新发送记录的提交状态为失败
	 * @param //serialList 流水号集合
	 * @return 成功返回true
	 */
	private boolean updateSetMainPageForFail(LfAppSitInfo appSitInfo){
		
		try
		{
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			//空：未返；0:APP网关推送消息中心成功；1:APP网关推送消息中心失败；00:客户端接收成功；10:客户端接收失败；110:已接收,未显示；120:成功显示
			objectMap.put("sendState", "1");
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("sid", String.valueOf(appSitInfo.getSid()));
			boolean updateRes = empDao.update(LfAppSitInfo.class, objectMap, conditionMap);
			
			return updateRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送设置首页消息失败则更新发送记录的提交状态为失败异常。");
			return false;
		}
	}
	
	/**
	 * 获取流水号字符串集合，格式为id1,id2,id3，每个元素最多1000个流水号
	 * @param serialList 流水号集合
	 * @return 成功返回流水号字符串集合，格式为id1,id2,id3，每个元素最多1000个流水号。异常返回null
	 */
	private List<String> getSerialList(List<String> serialList){
		try
		{
			//流水号字符串，格式为id1,id2,id3，每个元素最多1000个流水号
			List<String> serialList1k = new ArrayList<String>();
			int count = 0;
			//构造流水号，格式为id1,id2,id3
			String serialStr = "";
			for(int i = 0; i < serialList.size(); i++){
				count ++;
				serialStr += serialList.get(i);
				if(i < serialList.size() - 1){
					serialStr += ",";
				}
				
				if(count == 1000){
					serialList1k.add(serialStr);
					count = 0;
					serialStr = "";
				}
			}
			
			//少于1千的，也要保存
			if(serialStr != null && serialStr.length() > 0){
				serialList1k.add(serialStr);
			}
			
			return serialList1k;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取流水号字符串集合异常。");
			return null;
		}
	}
	
	/**
	 * 发送APP设置首页消息
	 * @param appMsg 消息对象，必填Sid、MainPageTopList和MainPageTitleList，选填validity
	 * @return 
	 * 1:提交成功。
	 * 0：未登录。
	 * -1:提交失败。
	 * -2:appMsg对象为null。
	 * -3:Sid为null。
	 * -4:更新app首页信息失败。
	 * -5:app首页信息对象为空。
	 * -6:获取并更新app首页信息对象异常。
	 * -8:主页内容为空。
	 * -9:设置内容失败。
	 */
	public int SetAppMainPage(AppMessage appMsg){
		
		if(appMsg == null){
			EmpExecutionContext.error("发送App主页消息失败，appMsg对象为null。");
			return -2;
		}
		
		if(appMsg.getSid() == null){
			EmpExecutionContext.error("发送App主页消息失败，Sid为null。");
			return -3;
		}
		
		if((appMsg.getMainPageTopList() == null || appMsg.getMainPageTopList().size() == 0) 
		&& (appMsg.getMainPageTitleList() == null || appMsg.getMainPageTitleList().size() == 0) )
		{
			EmpExecutionContext.error("发送App主页消息失败，主页内容为空。");
			return -8;
		}
		if(AppSdkPackage.getInstance().getXmppConnState() != 1){
			//再登录
			/*int loginRes = login(host, port, ecode, corpUser, password);
			if(loginRes != 1){
				EmpExecutionContext.error("发送App主页消息失败，未登录。");
				return 0;
			}*/
			EmpExecutionContext.error("发送App主页消息失败，未登录。");
			return 0;
		}
		
		//企业编码
		appMsg.setEcode(AppSdkPackage.getInstance().getEcode());
		//发送者用户名
		appMsg.setFromUserName(AppSdkPackage.getInstance().getEcode());
		//接收者用户名
		appMsg.setToUserName(AppSdkPackage.getInstance().getEcode());
		//消息接收者类型：1、发送所有企业用户，取企业编号；2、发送到某些人，TO为这些人的用户账号，用;号隔开
		appMsg.setToType(1);
		//公众消息类型。 1：首页推送数据；2：通知、提醒消息
		appMsg.setEmpType(1);
		//消息的类型。 	0：文本消息；1：图片消息；2：视频；3：语音；4：事件推送； 
		appMsg.setMsgType(4);
		appMsg.setTitle("设置首页");
		
		WgMessage wgMsg = null;
		LfAppSitInfo appSitInfo = null;
		try
		{
			
			appSitInfo = getAppSitInfoById(appMsg.getSid().toString());
			if(appSitInfo == null){
				EmpExecutionContext.error("发送App主页消息失败，app首页信息对象为空。");
				return -5;
			}
			
			wgMsg = new WgMessage();
			//消息类型为公众消息
			wgMsg.setMsgType(1);
			wgMsg.setFrom(appMsg.getFromUserName());
			wgMsg.setTo(appMsg.getToUserName());
			//公众消息类型。 1：首页推送数据；2：通知、提醒消息
			wgMsg.setEmtype(1);
			
			wgMsg.setToType(appMsg.getToType());
			wgMsg.setValidity(appMsg.getValidity());
			
			//获取内容样式对象集合
			boolean setRes = setMainPageBody(wgMsg, appMsg);
			//设置消息内容失败
			if(!setRes){
				return -9;
			}
			
			//流水号
			String serial = null;
			//已经存在，则使用已存在的
			if(appSitInfo.getSerial() != null && appSitInfo.getSerial().trim().length() > 0){
				serial = appSitInfo.getSerial();
				//如果拿不到id，则填-1，则app平台使用流水号
				Long id = -1L;
				if(appSitInfo.getMsgId() != null && appSitInfo.getMsgId().trim().length() > 0){
					id = Long.valueOf(appSitInfo.getMsgId());
				}
				wgMsg.setAppId(id);
			}
			//不存在，则获取新的
			else{
				serial = AppSdkPackage.getInstance().getSerial(appMsg.getFromUserName());
				appSitInfo.setSerial(serial);
				//新增填0
				wgMsg.setAppId(0L);
			}
			
			wgMsg.setSerial(serial);
			wgMsg.setEcCode(appMsg.getEcode());
			
			//更新首页信息对象
			boolean updateRes = updateAppSitInfo(appMsg, appSitInfo);
			if(!updateRes){
				EmpExecutionContext.error("发送App主页消息，更新app首页信息失败。");
				return -4;
			}
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("发送App主页消息，获取并更新app首页信息对象异常。");
			return -6;
		}
		
		try
		{
			List<WgMessage> msgList = new ArrayList<WgMessage>();
			msgList.add(wgMsg);
			PutInRecMtCache(msgList);
			
			EmpExecutionContext.info("发送App主页消息成功。流水号："+wgMsg.getSerial());
			
			return 1;
		}
		catch (Exception e)
		{
			boolean failUpdateRes = updateSetMainPageForFail(appSitInfo);
			EmpExecutionContext.error("发送App主页消息，提交失败，更新提交状态结果："+failUpdateRes+"。流水号："+wgMsg.getSerial());
			
			EmpExecutionContext.error("发送App主页消息，提交到App平台异常。流水号："+wgMsg.getSerial());
			return -1;
		}
	}
	
	/**
	 * 发送取消APP设置首页消息
	 * @param appMsg 消息对象，必填Sid
	 * @return 
	 * 1:提交成功。
	 * 0：未登录。
	 * -1:提交失败。
	 * -2:appMsg对象为null。
	 * -3:Sid为null。
	 * -5:app首页信息对象为空。
	 * -30:流水号为空。
	 * -31:获取和构建消息对象异常。
	 */
	public int CancelSetMainPage(AppMessage appMsg){
		
		EMessage emessage = null;
		try
		{
			if(appMsg == null){
				EmpExecutionContext.error("发送取消APP设置首页消息失败，appMsg对象为null。");
				return -2;
			}
			
			if(appMsg.getSid() == null){
				EmpExecutionContext.error("发送取消APP设置首页消息失败，Sid为null。");
				return -3;
			}
			if(AppSdkPackage.getInstance().getXmppConnState() != 1){
				/*//再登录
				int loginRes = login(host, port, ecode, corpUser, password);
				if(loginRes != 1){
					EmpExecutionContext.error("发送取消APP设置首页消息失败，未登录。");
					return 0;
				}*/
				EmpExecutionContext.error("发送取消APP设置首页消息失败，未登录。");
				return 0;
			}
			
			LfAppSitInfo appSitInfo = getAppSitInfoById(appMsg.getSid().toString());
			if(appSitInfo == null){
				EmpExecutionContext.error("发送取消APP设置首页消息失败，app首页信息对象为空。");
				return -5;
			}
			
			//企业编码
			appMsg.setEcode(AppSdkPackage.getInstance().getEcode());
			//发送者用户名
			appMsg.setFromUserName(AppSdkPackage.getInstance().getEcode());
			//接收者用户名
			appMsg.setToUserName(AppSdkPackage.getInstance().getEcode());
			//消息接收者类型：1、发送所有企业用户，取企业编号；2、发送到某些人，TO为这些人的用户账号，用;号隔开
			appMsg.setToType(1);
			//公众消息类型。 1：首页推送数据；2：通知、提醒消息
			appMsg.setEmpType(1);
			//消息的类型。 	0：文本消息；1：图片消息；2：视频；3：语音；4：事件推送； 
			appMsg.setMsgType(4);
			appMsg.setTitle("设置首页");
			
			EMessageModel model = new EMessageModel();
			model.setFrom(appMsg.getFromUserName());
			model.setTo(appMsg.getToUserName());
			//公众消息类型。 1：首页推送数据；2：通知、提醒消息
			model.setEmtype(1);
			model.setStype(1);
			model.setTtype(appMsg.getToType());
			
			//设置为当前时间的毫秒数，即表示马上过期
			model.setValidity(System.currentTimeMillis());
			
			//有msgid则传过去
			if(appSitInfo.getMsgId() != null && appSitInfo.getMsgId().trim().length() > 0){
				model.setId(Long.valueOf(appSitInfo.getMsgId()));
			}
			//没有msgid，则填-1
			else{
				model.setId(-1L);
			}
			
			/*//获取内容样式对象集合
			List<EMessageStyle> msgStylesList = this.getAppMainPageStyle(appMsg);
			model.setBody(msgStylesList);*/
			
			emessage = new EMessage();
			emessage.setIcode(AppStaticValue.ICODE_SENDEMSG);
			
			//流水号不存在
			if(appSitInfo.getSerial() == null || appSitInfo.getSerial().trim().length() == 0){
				EmpExecutionContext.error("发送取消APP设置首页消息失败，流水号为空。");
				return -30;
			}
			
			emessage.setSerial(appSitInfo.getSerial());
			emessage.setEcode(appMsg.getEcode());
			emessage.addEModel(model);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送取消APP设置首页消息，获取和构建消息对象异常。");
			return -31;
		}
		
		try
		{
			EmpExecutionContext.info("发送取消APP设置首页消息xml:"+emessage.toXML());
			
			AppSdkPackage.getInstance().getXMPPServer().pushEMessage(emessage);
			
			EmpExecutionContext.info("发送取消APP设置首页消息成功。流水号："+emessage.getSerial());
			
			return 1;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("发送取消APP设置首页消息，提交到App平台异常。");
			return -1;
		}
	}
	
	/**
	 * 发送个人消息
	 * @param //pMessageModel
	 * @return 
	 * 1:提交成功
	 * 0：未登录
	 * -1:提交失败；
	 * -2:PMessageModel对象为null；
	 * -3:Ecode为null；
	 * -4:From为null
	 * -5:To为null
	 * -6:MessageStyles为null
	 * -7:下行消息集合对象为null
	 * -8:保存消息记录失败。
	 * -9:获取并保存消息记录异常。
	 */
	public int SendPersonalMsg(AppMessage appMsg){
		
		if(appMsg == null){
			EmpExecutionContext.error("发送个人消息失败，pMessageModel对象为null。");
			return -2;
		}
		if(appMsg.getEcode() == null || appMsg.getEcode().length() == 0){
			EmpExecutionContext.error("发送个人消息失败，Ecode为null。");
			return -3;
		}
		if(appMsg.getFromUserName() == null || appMsg.getFromUserName().length() == 0){
			EmpExecutionContext.error("发送个人消息失败，FromUserName为null。");
			return -4;
		}
		if(appMsg.getToUserName() == null || appMsg.getToUserName().length() == 0){
			EmpExecutionContext.error("发送个人消息失败，ToUserName为null。");
			return -5;
		}
		if(appMsg.getBody() == null || appMsg.getBody().length() == 0){
			EmpExecutionContext.error("发送个人消息失败，MsgStylesList为null。");
			return -6;
		}
		if(AppSdkPackage.getInstance().getXmppConnState() != 1){
			//再登录
			/*int loginRes = login(host, port, ecode, corpUser, password);
			if(loginRes != 1){
				EmpExecutionContext.error("发送个人消息失败，未登录。");
				return 0;
			}*/
			EmpExecutionContext.error("发送个人消息失败，未登录。");
			return 0;
		}
		
		//流水号集合，一个元素存一个流水号
		List<String> serialList = new ArrayList<String>();
		WgMessage message = new WgMessage();
		try
		{
			String serial = AppSdkPackage.getInstance().getSerial(appMsg.getFromUserName());
			serialList.add(serial);
			
			message.setFrom(appMsg.getFromUserName());
			message.setFromName(appMsg.getFromName());
			message.setTo(appMsg.getToUserName());
			message.setMsgSrc(appMsg.getMsgSrc());
			message.setBody(appMsg.getBody());
			
			message.setSerial(serial);
			message.setEcCode(appMsg.getEcode());
			message.setMsgType(2);
			
			//获取msgid
			long msgId = getMsgId(1);
			
			appMsg.setMsgId(msgId-1);
			
			List<LfAppMtmsg> mtMsgList = this.getAppMtmsgList(appMsg.getToUserName(), appMsg, message.getSerial(), 2);
			if(mtMsgList == null || mtMsgList.size() == 0){
				EmpExecutionContext.error("发送个人消息，获取app下行消息对象集合失败，消息对象集合为空。");
				return -7;
			}
			
			//保存每条消息记录
			int saveRes = empDao.save(mtMsgList, LfAppMtmsg.class);
			//保存失败
			if(saveRes == 0){
				EmpExecutionContext.error("发送个人消息，保存消息记录失败。");
				return -8;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送个人消息，获取并保存消息记录异常。");
			return -9;
		}
		
		try
		{
			List<WgMessage> msgList = new ArrayList<WgMessage>();
			msgList.add(message);
			PutInRecMtCache(msgList);
			
			//AppSdkPackage.getInstance().getXMPPServer().pushPMessage(message);
			
			EmpExecutionContext.info("发送个人消息成功。流水号:"+message.getSerial());
			
			return 1;
		}
		catch (Exception e)
		{
			//提交失败，需要更新状态
			boolean udapteRes = updateMtMsgForFail(serialList);
			EmpExecutionContext.error("发送个人消息，提交失败，更新发送记录提交状态结果："+udapteRes+"；流水号："+message.getSerial());
			
			EmpExecutionContext.error(e, "发送个人消息，提交到app平台异常。流水号："+message.getSerial());
			return -1;
		}
		
	}
	
	/**
	 * 发送客服消息
	 * @param //pMessageModel 参数。
	 * @return 
	 * 1:提交成功
	 * 0：未登录
	 * -1:提交失败；
	 * -2:PMessageModel对象为null；
	 * -3:Ecode为null；
	 * -4:From为null
	 * -5:To为null
	 * -6:MessageStyles为null
	 * -100:获取消息参数为null
	 */
	public int SendAppPersionMsg(String json){
		
		try
		{
			EmpExecutionContext.info("客服发送的json："+json);
			
			AppMessage appMsg = this.parsPMsgJson(json);
			if(appMsg == null){
				EmpExecutionContext.error("发送客服消息失败，获取消息参数为null。");
				return -100;
			}
			appMsg.setTitle("客服消息");
			//来源为3－企业客服
			appMsg.setMsgSrc(3);
			int res = SendPersonalMsg(appMsg);
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送客服消息异常。");
			return -1;
		}
	}
	
	/**
	 * 设置个人消息接收处理接口实例
	 * @param procPMessageImpl 个人消息接收处理接口实例
	 * @return 成功返回true
	 */
	public boolean SetIProcPMessage(IProcPMessage procPMessageImpl){
		
		if(procPMessageImpl == null){
			EmpExecutionContext.error("设置个人消息接收处理接口实例失败，实例为null。");
			return false;
		}
		procPmsgImpl = procPMessageImpl;
		return true;
	}
	
	/**
	 * 设置状态报告消息接收处理接口实例
	 * @param procRptMessageImpl 个人消息接收处理接口实例
	 * @return 成功返回true
	 */
	public boolean SetIProcRptMessage(IProcRptMessage procRptMessageImpl){
		
		if(procRptMessageImpl == null){
			EmpExecutionContext.error("设置状态报告消息接收处理接口实例失败，实例为null。");
			return false;
		}
		procRptmsgImpl = procRptMessageImpl;
		return true;
	}
	
	/**
	 * 获取App平台企业编码
	 * @return 返回当前登录的企业编码
	 */
	public String getAppECode(){
		if(AppSdkPackage.getInstance().getEcode() == null){
			int res = AppSdkPackage.getInstance().initAppLoginInfo();
			EmpExecutionContext.info("获取App平台企业编码为空，重新初始化结果："+res);
		}
		return AppSdkPackage.getInstance().getEcode();
	}
	
	/**
	 * 根据id获取app首页消息对象
	 * @param sid app首页消息id
	 * @return 成功返回app首页消息对象
	 */
	private LfAppSitInfo getAppSitInfoById(String sid){
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("sid", sid);
			List<LfAppSitInfo> appSitInfoList = empDao.findListByCondition(LfAppSitInfo.class, conditionMap, null);
			if(appSitInfoList == null || appSitInfoList.size() == 0){
				return null;
			}
			return appSitInfoList.get(0);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据id获取app首页消息对象异常。");
			return null;
		}
	}
	
	/**
	 * 更新app首页信息
	 * @param appMsg 消息参数
	 * @return 成功返回true
	 */
	private boolean updateAppSitInfo(AppMessage appMsg, LfAppSitInfo appSitInfo){
		try
		{
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			
			objectMap.put("serial", appSitInfo.getSerial());
			objectMap.put("validity", String.valueOf(appMsg.getValidity()));

			SimpleDateFormat sdfT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			String sendTime = sdfT.format(new Date());
			objectMap.put("sendTime", sendTime);
			
			objectMap.put("fromUser", appMsg.getFromUserName());
			objectMap.put("toUser", appMsg.getToUserName());
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("sid", String.valueOf(appMsg.getSid()));
			
			int res = empDao.updateBySymbolsCondition(LfAppSitInfo.class, objectMap, conditionMap);
			if(res > 0){
				return true;
			}
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新app首页信息异常。");
			return false;
		}
	}
	

	/**
	 * 获取App首页内容样式对象集合
	 * @param appMsg 消息参数对象
	 * @return 返回App首页内容样式对象集合，异常返回null
	 */
	private boolean setMainPageBody(WgMessage wgMsg, AppMessage appMsg){
		try
		{
			int size = 0;
			boolean setRes = false;
			
			StringBuffer sbBigPic = new StringBuffer();
			sbBigPic.append("{");
			
			String title = null;
			String content = null;
			
			//构造轮询大图
			if(appMsg.getMainPageTopList() != null && appMsg.getMainPageTopList().size() > 0){
				size = appMsg.getMainPageTopList().size();
				
				sbBigPic.append("\"EMessageStyle8\":")
				.append("{")
				.append("\"sortindex\"").append(":\"").append(0).append("\"").append(",");
				for(int i = 0; i < size; i++){
					if(i == 0){
						title = JsonUtil.stringForJson(appMsg.getMainPageTopList().get(i).getTitle());
						content = JsonUtil.stringForJson(appMsg.getMainPageTopList().get(i).getContent());
						
						sbBigPic
						.append("\"title1\"").append(":\"").append(title).append("\"").append(",")
						.append("\"content1\"").append(":\"").append(content).append("\"").append(",")
						.append("\"pic1\"").append(":\"").append(appMsg.getMainPageTopList().get(i).getPic()).append("\"").append(",")
						.append("\"url1\"").append(":\"").append(appMsg.getMainPageTopList().get(i).getUrl()).append("\"");
						if(i < size - 1){
							sbBigPic.append(",");
						}
						
					}
					else if(i == 1){
						title = JsonUtil.stringForJson(appMsg.getMainPageTopList().get(i).getTitle());
						content = JsonUtil.stringForJson(appMsg.getMainPageTopList().get(i).getContent());
						
						sbBigPic
						.append("\"title2\"").append(":\"").append(title).append("\"").append(",")
						.append("\"content2\"").append(":\"").append(content).append("\"").append(",")
						.append("\"pic2\"").append(":\"").append(appMsg.getMainPageTopList().get(i).getPic()).append("\"").append(",")
						.append("\"url2\"").append(":\"").append(appMsg.getMainPageTopList().get(i).getUrl()).append("\"");
						if(i < size - 1){
							sbBigPic.append(",");
						}
						
					}
					else if(i == 2){
						title = JsonUtil.stringForJson(appMsg.getMainPageTopList().get(i).getTitle());
						content = JsonUtil.stringForJson(appMsg.getMainPageTopList().get(i).getContent());
						
						sbBigPic
						.append("\"title3\"").append(":\"").append(title).append("\"").append(",")
						.append("\"content3\"").append(":\"").append(content).append("\"").append(",")
						.append("\"pic3\"").append(":\"").append(appMsg.getMainPageTopList().get(i).getPic()).append("\"").append(",")
						.append("\"url3\"").append(":\"").append(appMsg.getMainPageTopList().get(i).getUrl()).append("\"");
						if(i < size - 1){
							sbBigPic.append(",");
						}
					}
					else if(i == 3){
						title = JsonUtil.stringForJson(appMsg.getMainPageTopList().get(i).getTitle());
						content = JsonUtil.stringForJson(appMsg.getMainPageTopList().get(i).getContent());
						
						sbBigPic
						.append("\"title4\"").append(":\"").append(title).append("\"").append(",")
						.append("\"content4\"").append(":\"").append(content).append("\"").append(",")
						.append("\"pic4\"").append(":\"").append(appMsg.getMainPageTopList().get(i).getPic()).append("\"").append(",")
						.append("\"url4\"").append(":\"").append(appMsg.getMainPageTopList().get(i).getUrl()).append("\"");
						if(i < size - 1){
							sbBigPic.append(",");
						}
						
					}
					else if(i == 4){
						title = JsonUtil.stringForJson(appMsg.getMainPageTopList().get(i).getTitle());
						content = JsonUtil.stringForJson(appMsg.getMainPageTopList().get(i).getContent());
						
						sbBigPic
						.append("\"title5\"").append(":\"").append(title).append("\"").append(",")
						.append("\"content5\"").append(":\"").append(content).append("\"").append(",")
						.append("\"pic5\"").append(":\"").append(appMsg.getMainPageTopList().get(i).getPic()).append("\"").append(",")
						.append("\"url5\"").append(":\"").append(appMsg.getMainPageTopList().get(i).getUrl()).append("\"");
						if(i < size - 1){
							sbBigPic.append(",");
						}
						
					}
					else{
						//只支持5个
						break;
					}
				}
				sbBigPic.append("}");
			}
			
			//设置内容
			setRes = setWgContent(wgMsg, sbBigPic);
			if(!setRes){
				return false;
			}
			sbBigPic.setLength(0);
			
			//构造左图右文列表
			if(appMsg.getMainPageTitleList() != null && appMsg.getMainPageTitleList().size() > 0){
				//EMessageStyle2 style2 = null;
				size = appMsg.getMainPageTitleList().size();
				
				StringBuffer sbPicContent = null;
				
				for(int i = 0; i < size; i++){
					sbPicContent = new StringBuffer();
					if(i==0){
						sbPicContent.append(",");
					}
					
					title = JsonUtil.stringForJson(appMsg.getMainPageTitleList().get(i).getTitle());
					content = JsonUtil.stringForJson(appMsg.getMainPageTitleList().get(i).getContent());
					
					sbPicContent.append("\"")
					//首页公众消息会有多个，导致key重复，拼这个唯一标识进去
					.append("pm").append(i)
					.append("EMessageStyle2\":")
					.append("{")
					.append("\"sortindex\"").append(":\"").append(i+1).append("\"").append(",")
					.append("\"title\"").append(":\"").append(title).append("\"").append(",")
					.append("\"content\"").append(":\"").append(content).append("\"").append(",")
					.append("\"pic\"").append(":\"").append(appMsg.getMainPageTitleList().get(i).getPic()).append("\"").append(",")
					.append("\"url\"").append(":\"").append(appMsg.getMainPageTitleList().get(i).getUrl()).append("\"")
					.append("}");
					
					if(i < size - 1){
						sbPicContent.append(",");
					}
					
					//设置内容
					setRes = setWgContent(wgMsg, sbPicContent);
					if(!setRes){
						return false;
					}
					sbPicContent.setLength(0);
				}
			}
			
			StringBuffer sbEnd = new StringBuffer();
			sbEnd.append(",\"stylecount\"").append(":\"").append(1+size).append("\"");
			sbEnd.append("}");
			
			//设置内容
			setRes = setWgContent(wgMsg, sbEnd);
			if(!setRes){
				return false;
			}
			
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取App首页内容样式对象集合异常。");
			return false;
		}
	}
	
	private boolean setWgContent(WgMessage wgMsg, StringBuffer sbContent){
		try
		{
			if(wgMsg.getBody().length() == 0){
				wgMsg.setBody(sbContent.toString());
			}
			else if(wgMsg.getContent1().length() == 0){
				wgMsg.setContent1(sbContent.toString());
			}
			else if(wgMsg.getContent2().length() == 0){
				wgMsg.setContent2(sbContent.toString());
			}
			else if(wgMsg.getContent3().length() == 0){
				wgMsg.setContent3(sbContent.toString());
			}
			else if(wgMsg.getContent4().length() == 0){
				wgMsg.setContent4(sbContent.toString());
			}
			else if(wgMsg.getContent5().length() == 0){
				wgMsg.setContent5(sbContent.toString());
			}
			else if(wgMsg.getContent6().length() == 0){
				wgMsg.setContent6(sbContent.toString());
			}
			else if(wgMsg.getContent7().length() == 0){
				wgMsg.setContent7(sbContent.toString());
			}
			else if(wgMsg.getContent8().length() == 0){
				wgMsg.setContent8(sbContent.toString());
			}
			else if(wgMsg.getContent9().length() == 0){
				wgMsg.setContent9(sbContent.toString());
			}
			else if(wgMsg.getContent10().length() == 0){
				wgMsg.setContent10(sbContent.toString());
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置消息体内容异常。");
			return false;
		}
	}
	
	/**
	 * 处理json数据
	 * @param json json格式字符串
	 * @return 返回封装了json数据的AppMessage，异常返回null
	 */
	private AppMessage parsPMsgJson(String json){
		try
		{
			//把json格式字符串转为json对象
			JSONObject jsonObj = JsonUtil.parsJsonObj(json);
			if(jsonObj == null){
				EmpExecutionContext.error("处理json数据失败，获取json对象为null。");
				return null;
			}
			AppMessage appMsg = new AppMessage();
			
			if(jsonObj.get("ECODE") == null){
				EmpExecutionContext.error("处理json数据失败，ECODE为null。");
				return null;
			}
			appMsg.setEcode(jsonObj.get("ECODE").toString());
			
			if(jsonObj.get("FROM") == null){
				EmpExecutionContext.error("处理json数据失败，FROM为null。");
				return null;
			}
			appMsg.setFromUserName(jsonObj.get("FROM").toString());
			appMsg.setUserId(Long.valueOf(jsonObj.get("FROM").toString()));
			
			if(jsonObj.get("FROMNAME") == null){
				EmpExecutionContext.error("处理json数据失败，FROMNAME为null。");
				return null;
			}
			appMsg.setFromName(jsonObj.get("FROMNAME").toString());
			
			if(jsonObj.get("TO") == null){
				EmpExecutionContext.error("处理json数据失败，TO为null。");
				return null;
			}
			appMsg.setToUserName(jsonObj.get("TO").toString());
			
			if(jsonObj.get("VALIDITY") != null){
				appMsg.setValidity(Long.parseLong(jsonObj.get("VALIDITY").toString()));
			}
			
			if(jsonObj.get("MSGTYPE") != null){
				appMsg.setMsgType(Integer.parseInt(jsonObj.get("MSGTYPE").toString()));
			}
			
			if(jsonObj.get("pMessageStyles") == null){
				EmpExecutionContext.error("处理json数据失败，pMessageStyles为null。");
				return null;
			}
			
			//获取消息内容样式
			JSONObject pMsgStylesJsonObj = (JSONObject)jsonObj.get("pMessageStyles");
			
			if(pMsgStylesJsonObj.size() == 0){
				EmpExecutionContext.error("处理json数据失败，pMessageStyles无内容。");
				return null;
			}
			
			appMsg.setBody(pMsgStylesJsonObj.toString());
			
			JSONObject styleJsonObj = null;
			//获取PMessageStyle
			for(Object key : pMsgStylesJsonObj.keySet()){
				styleJsonObj = (JSONObject)pMsgStylesJsonObj.get(key.toString());
				if("PMessageStyle1".equals(key.toString())){
					
					appMsg.setMsgContent(styleJsonObj.get("content")==null?"":styleJsonObj.get("content").toString());
				}
				else if("PMessageStyle2".equals(key.toString())){
					if(styleJsonObj.get("pic") == null){
						EmpExecutionContext.error("处理json数据失败，存在PMessageStyle2但pic为null。");
						return null;
					}
					
					appMsg.setUrl(styleJsonObj.get("pic").toString());
				}
				else if("PMessageStyle3".equals(key.toString())){
					if(styleJsonObj.get("url") == null){
						EmpExecutionContext.error("处理json数据失败，存在PMessageStyle3但url为null。");
						return null;
					}
					
					appMsg.setUrl(styleJsonObj.get("url").toString());
				}
				else if("PMessageStyle4".equals(key.toString())){
					if(styleJsonObj.get("url") == null || styleJsonObj.get("time") == null){
						EmpExecutionContext.error("处理json数据失败，存在PMessageStyle4但url为null。");
						return null;
					}
					
					appMsg.setUrl(styleJsonObj.get("url").toString());
				}
			}
			
			return appMsg;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理json数据异常。");
			return null;
		}
	}
	
	/**
	 * 获取消息id
	 * @return 返回消息id字符串
	 */
	private long getMsgId(int count){
		//批量获取msgid
		long msgId = empDao.getIdByPro(514, count);
		
		return msgId;
	}
	
		
	/**
	 * 发送接收个人消息回执
	 * @param pmessage 消息对象
	 * @param res 结果，true表示成功
	 * @return 发送成功返回true
	 */
	private boolean sendRecPMsgReceipt(PMessage pmessage, boolean res){
		
		try
		{
			PMessage receiptMsg = getPReceiptMsg(pmessage, res?0:1);
			
			if(receiptMsg == null){
				EmpExecutionContext.error("发送接收个人消息回执失败，个人消息回执对象为空。");
				return false;
			}
			
			EmpExecutionContext.info("发送接收个人消息回执xml:"+receiptMsg.toXML());
			
			//返回回执给app平台
			AppSdkPackage.getInstance().getXMPPServer().pushPMessage(receiptMsg);
			
			EmpExecutionContext.appRequestInfoLog("resp rece mo："+receiptMsg.toXML());
			
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送接收个人消息回执异常");
			return false;
		}
		
	}
	
	private boolean sendRptMsgReceipt(StateReportMessage rptMsg, boolean res){
		
		try
		{
			StateReportMessage receiptMsg = getRptReceiptMsg(rptMsg, res?0:1);
			
			if(receiptMsg == null){
				EmpExecutionContext.error("发送接收状态报告消息回执失败，消息回执对象为空。");
				return false;
			}
			
			EmpExecutionContext.info("发送接收状态报告消息回执xml:"+receiptMsg.toXML());
			
			//返回回执给app平台
			//AppSdkPackage.getInstance().getXMPPServer().pushPMessage(receiptMsg);
			AppSdkPackage.getInstance().pushMessage(receiptMsg);
			
			EmpExecutionContext.appRequestInfoLog("resp rece rpt:"+receiptMsg.toXML());
			EmpExecutionContext.info("发送接收状态报告消息回执成功。流水号："+receiptMsg.getSerial());
			
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送接收个人消息回执异常");
			return false;
		}
		
	}
	
	/**
	 * 发送公众消息回执
	 * @param emessage 消息对象
	 * @param res 结果，true表示成功
	 * @return 发送成功返回true
	 */
	private boolean sendRecEMsgReceipt(EMessage emessage, boolean res){
		
		try
		{
			EMessage receiptMsg = getEReceiptMsg(emessage, res?0:1);
			
			if(receiptMsg == null){
				EmpExecutionContext.error("发送公众消息回执回执失败，公众消息回执对象为空。");
				return false;
			}
			
			EmpExecutionContext.info("发送公众消息回执xml:"+receiptMsg.toXML());
			
			//返回回执给app平台
			AppSdkPackage.getInstance().getXMPPServer().pushEMessage(receiptMsg);
			
			EmpExecutionContext.info("发送公众消息回执成功。流水号："+receiptMsg.getSerial());
			
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送公众消息回执异常");
			return false;
		}
		
	}
	/**
	 * 获取个人消息回执消息对象
	 * @param pmessage 接收到的个人消息对象
	 * @param rcode 状态，0 成功   1 失败
	 * @return 返回个人消息回执消息对象
	 */
	private PMessage getPReceiptMsg(PMessage pmessage, Integer rcode){
		
		try
		{
			PMessage r = new PMessage(pmessage.getSerial(), pmessage.getEcode(), pmessage.getIcode()*10, rcode, "");
			//r.setIcode(pmessage.getIcode()*10);
			r.setPacketID(pmessage.getPacketID());
			r.setType(pmessage.getType());
			r.setFrom(pmessage.getTo());
			r.setTo(pmessage.getFrom());
			PacketExtension extension = pmessage.getExtension("x", "jabber:x:delay");
			if (extension != null) {
				r.addExtension(extension);
			}
			/*String subject = pmessage.getSubject();
			if (subject != null) {
				r.setSubject(subject);
			}
			String body = pmessage.getBody();
			if (body != null) {
				r.setBody(body);
			}
			Collection<PacketExtension> extensions = pmessage.getExtensions();
			if (extensions != null) {
				r.addExtensions(extensions);
			}
			XMPPError error = pmessage.getError();
			if (error != null) {
				r.setError(error);
			}*/
			r.addPModel(pmessage.getPModel());
			return r;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取个人消息回执消息对象异常。");
			return null;
		}
	}
	
	private StateReportMessage getRptReceiptMsg(StateReportMessage rptMsg, Integer rcode){
		
		try
		{
			StateReportMessage r = new StateReportMessage(rptMsg.getSerial(), rptMsg.getEcode(), rptMsg.getIcode()*10, rcode, "");
			//r.setIcode(pmessage.getIcode()*10);
			r.setPacketID(rptMsg.getPacketID());
			r.setType(rptMsg.getType());
			r.setFrom(rptMsg.getTo());
			r.setTo(rptMsg.getFrom());
			PacketExtension extension = rptMsg.getExtension("x", "jabber:x:delay");
			if (extension != null) {
				r.addExtension(extension);
			}
			/*String subject = pmessage.getSubject();
			if (subject != null) {
				r.setSubject(subject);
			}
			String body = pmessage.getBody();
			if (body != null) {
				r.setBody(body);
			}
			Collection<PacketExtension> extensions = pmessage.getExtensions();
			if (extensions != null) {
				r.addExtensions(extensions);
			}
			XMPPError error = pmessage.getError();
			if (error != null) {
				r.setError(error);
			}*/
			r.addStateReportModel(rptMsg.getStateReportModel());
			return r;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取状态报告消息对象异常。");
			return null;
		}
	}
	
	/**
	 * 获取公众消息回执消息对象
	 * @param emessage 接收到的公众消息对象
	 * @param rcode 状态，0 成功   1 失败
	 * @return 返回公众消息回执消息对象
	 */
	private EMessage getEReceiptMsg(EMessage emessage, Integer rcode){
		
		try
		{
			EMessage r = new EMessage(emessage.getSerial(), emessage.getEcode(), emessage.getIcode()*10, rcode, "");
			r.setPacketID(emessage.getPacketID());
			r.setType(emessage.getType());
			r.setFrom(emessage.getTo());
			r.setTo(emessage.getFrom());
			
			PacketExtension extension = emessage.getExtension("x", "jabber:x:delay");
			if (extension != null) {
				r.addExtension(extension);
			}
			/*String subject = emessage.getSubject();
			if (subject != null) {
				r.setSubject(subject);
			}
			String body = emessage.getBody();
			if (body != null) {
				r.setBody(body);
			}
			Collection<PacketExtension> extensions = emessage.getExtensions();
			if (extensions != null) {
				r.addExtensions(extensions);
			}
			XMPPError error = emessage.getError();
			if (error != null) {
				r.setError(error);
			}*/
			r.addEModel(emessage.getEModel());
			return r;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取公众消息回执消息对象异常。");
			return null;
		}
	}
	
	/**
	 * 更新app消息下行记录回执状态
	 * @param sendstate 更新的状态
	 * @param appMsgId 消息流水号
	 * @return 成功返回true
	 */
	public boolean updateMtMsg(String sendstate, String appMsgId, String tousername){
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
			if(res != null){
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
	 * 更新app首页信息消息回执
	 * @param //appMsg 消息参数
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
	 * 获取json格式字符串
	 * @param //pMsgModel 构造参数
	 * @return 返回json格式字符串，异常返回null
	 */
	public String getJson(LfAppTcMomsg moMsg, PMessage pmessage, String ecode){
		if(pmessage.getPModel() == null){
			EmpExecutionContext.error("获取json格式字符串失败，PMessageModel对象为null。");
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
			
			//标记为离线消息。0在线消息；1离线消息
			String outlinemsg = "0";
			if(pmessage.getExtension("x", "jabber:x:delay") != null){
				outlinemsg = "1";
			}
			
			StringBuffer sb = new StringBuffer()
				.append("{")
				.append("\"ECODE\":").append("\"").append(ecode).append("\"").append(",")
				.append("\"FROM\":").append("\"").append(pMsgModel.getFrom()).append("\"").append(",")
				.append("\"FROMNAME\":").append("\"").append(fname).append("\"").append(",")
				.append("\"TO\":").append("\"").append(pMsgModel.getTo()).append("\"").append(",")
				.append("\"VALIDITY\":").append("\"").append(pMsgModel.getValidity()).append("\"").append(",")
				.append("\"OUTLINEMSG\":").append("\"").append(outlinemsg).append("\"").append(",");
				
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
			EmpExecutionContext.error(e, "获取json格式字符串异常。");
			return null;
		}
	}
	
	/**
	 * 获取发送接口的参数对象
	 * @param appMsg 消息参数对象
	 * @param allMtMsgList 发送记录对象，方法会填充这个集合
	 * @param serialList 流水号集合，用户失败后更新发送记录状态
	 * @return 返回参数集合
	 */
	private List<WgMessage> getWgMsgList(AppMessage appMsg, List<LfAppMtmsg> allMtMsgList, List<String> serialList){
		
		try
		{
			//返回的结果
			List<WgMessage> msgList = new ArrayList<WgMessage>();
			WgMessage msg = null;
			//EMessageModel model = null;
			
			//100个用户名存一个位置。存放用户名，以;分隔
			List<String> userNameList = this.splitUserName(appMsg);
			if(userNameList == null){
				EmpExecutionContext.error("获取发送接口的参数对象失败，获取用户名字符串集合为空。");
				return null;
			}
			
			List<LfAppMtmsg> mtMsgList = null;
			
			int listSize = userNameList.size();
			
			//批量获取msgid
			long msgId = getMsgId(appMsg.getSendCount()+1);
			
			appMsg.setMsgId(msgId-appMsg.getSendCount());
			//封装消息对象
			
			for(int i = 0; i<listSize;i++){
				
				msg = new WgMessage();
				//消息类型为公众消息
				msg.setMsgType(1);
				//msg.setIcode(AppStaticValue.ICODE_SENDEMSG);
				
				String serial = AppSdkPackage.getInstance().getSerial(appMsg.getFromUserName());
				msg.setSerial(serial);
				serialList.add(serial);
				
				msg.setEcCode(appMsg.getEcode());
				
				msg.setEcCode(appMsg.getEcode());
				
				msg.setFrom(appMsg.getFromUserName());
				//公众消息类型。 1：首页推送数据；2：通知、提醒消息
				msg.setEmtype(2);
				
				//消息接收者类型：1:发送所有企业用户，取企业编号；2:发送到某些人，TO为这些人的用户账号，用;号隔开，最多100
				msg.setToType(appMsg.getToType());
				
				msg.setValidity(appMsg.getValidity());
				
				msg.setTo(userNameList.get(i));
				
				//获取消息发送内容json
				String body = getBodyJson(appMsg);
				if(body == null){
					EmpExecutionContext.error("获取app下行消息对象集合失败，消息内容为空。");
					return null;
				}
				
				msg.setBody(body);
				
				msgList.add(msg);
				
				//获取下行消息，用于保存到数据库
				mtMsgList = this.getAppMtmsgList(userNameList.get(i), appMsg, msg.getSerial(), 1);
				if(mtMsgList == null || mtMsgList.size() == 0){
					EmpExecutionContext.error("获取app下行消息对象集合失败，消息对象集合为空。");
					return null;
				}
				
				allMtMsgList.addAll(mtMsgList);
			}
			
			return msgList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("获取发送参数集合对象异常。");
			return null;
		}
		
	}
	
	/**
	 * 获取app下行消息对象集合
	 * @param toUserNames 用户登录名
	 * @param appMsg 任务参数对象
	 * @param appMsgId app消息id
	 * @param sendMsgType 发送消息类型。1：公众消息，2：个人消息；
	 * @return 返回app下行消息对象集合，异常返回null
	 */
	private List<LfAppMtmsg> getAppMtmsgList(String toUserNames, AppMessage appMsg, String appMsgId, int sendMsgType){
		try
		{
			List<LfAppMtmsg> mtMsgList = new ArrayList<LfAppMtmsg>();
			LfAppMtmsg mtMsg = null;
			String[] userArray = toUserNames.split(AppSdkPackage.splitSymbol);
			int size = userArray.length;
			for(int i = 0; i < size; i++){
				mtMsg = this.getAppMtmsg(userArray[i], appMsg, appMsgId, sendMsgType);
				
				mtMsgList.add(mtMsg);
			}
			
			return mtMsgList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取app下行消息对象集合异常。");
			return null;
		}
	}
	
	/**
	 * 获取app下行消息对象
	 * @param toUserName 用户登录名
	 * @param appMsg 任务参数对象
	 * @param appMsgId app消息id
	 * @param sendMsgType 发送消息类型。1：公众消息，2：个人消息；
	 * @return 返回app下行消息对象集合，异常返回null
	 */
	private LfAppMtmsg getAppMtmsg(String toUserName, AppMessage appMsg, String appMsgId, int sendMsgType){
		try
		{
			LfAppMtmsg mtMsg = new LfAppMtmsg();
			
			long msgId = appMsg.getMsgId()+1;
			appMsg.setMsgId(msgId);
			mtMsg.setMsgid(String.valueOf(msgId));
			
			mtMsg.setAppMsgId(appMsgId);
			mtMsg.setTaskid(appMsg.getTaskId());
			//不是公众消息则需要设置发送者者
			if(sendMsgType != 1){
				mtMsg.setAppUserAccount(appMsg.getFromUserName());
			}
			mtMsg.setSendMsgType(sendMsgType);
			mtMsg.setMsgtype(appMsg.getMsgType());
			mtMsg.setTousername(toUserName);
			mtMsg.setUserid(appMsg.getUserId());
			//1.提交成功；2.提交失败；
			mtMsg.setSendstate(1);
			mtMsg.setTitle(appMsg.getTitle());
			if(appMsg.getMsgType() == 0){
				mtMsg.setContent(appMsg.getMsgContent());
			}else{
				mtMsg.setContent(appMsg.getUrl());
			}
			mtMsg.setCorpcode(appMsg.getEcode());
				
			return mtMsg;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取app下行消息对象异常。");
			return null;
		}
	}
	
	/**
	 * 拆分用户名字符串为100个为一组
	 * @param //toUserNameArray 用户名字符串
	 * @return 返回用户名字符串集合，一个元素包含100个用户名字符串，用户名以分号分隔
	 */
	private List<String> splitUserName(AppMessage appMsg){
		try
		{
			LinkedHashSet<String> toUserNameSet = appMsg.getToUserNameSet();
			//100个用户名存一个位置。存放用户名，以;分隔
			List<String> userNameList = new ArrayList<String>();
			String userName = "";
			//计数变量
			int count = 0;
			
			//循环拼接username
			for(String key : toUserNameSet){
				userName += key;
				count++;
				//100条存一次
				if(count == AppStaticValue.TOUSERSIZE){
					userNameList.add(userName);
					//计算发送总数
					appMsg.setSendCount(appMsg.getSendCount() + count);
					count = 0;
					userName = "";
					continue;
				}
				//加分隔符
				userName += AppSdkPackage.splitSymbol;
			}
			
			//剩余的也要保存
			if(userName.length() > 0){
				//计算发送总数
				appMsg.setSendCount(appMsg.getSendCount() + count);
				//把最后的一个分隔符截取掉
				userName = userName.substring(0, userName.length()-1);
				userNameList.add(userName);
			}
			return userNameList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "拆分用户名字符串异常。");
			return null;
		}
		
	}

	/**
	 * 获取对应样式对象
	 * @param appMsg 发送任务参数对象
	 * @return 返回样式对象，找不到类型或异常返回null
	 */
	private String getBodyJson(AppMessage appMsg){
		
		try
		{
			String title = null;
			String content = null;
			
			int i = 0;
			int size = 1;
			
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			
			switch(appMsg.getMsgType()){
				//文字
				case 0:
					title = JsonUtil.stringForJson(appMsg.getTitle());
					content = JsonUtil.stringForJson(appMsg.getMsgContent());
					
					sb.append("\"EMessageStyle1\":")
					.append("{")
					.append("\"sortindex\"").append(":\"").append(i).append("\"").append(",")
					.append("\"title\"").append(":\"").append(title).append("\"").append(",")
					.append("\"content\"").append(":\"").append(content).append("\"")
					.append("}");
					
					break;
				
				//图片
				case 1:
					title = JsonUtil.stringForJson(appMsg.getTitle());
					
					sb.append("\"EMessageStyle8\":")
					.append("{")
					.append("\"sortindex\"").append(":\"").append(i).append("\"").append(",")
					.append("\"title1\"").append(":\"").append(title).append("\"").append(",")
					.append("\"pic1\"").append(":\"").append(appMsg.getUrl()).append("\"")
					.append("}");
					
					break;
					
				//视频
				case 2:
					title = JsonUtil.stringForJson(appMsg.getTitle());
					
					sb.append("\"EMessageStyle10\":")
					.append("{")
					.append("\"sortindex\"").append(":\"").append(i).append("\"").append(",")
					.append("\"title\"").append(":\"").append(title).append("\"").append(",")
					.append("\"url\"").append(":\"").append(appMsg.getUrl()).append("\"").append(",")
					.append("\"pic\"").append(":\"").append(appMsg.getPicUrl()==null?"":appMsg.getPicUrl()).append("\"").append(",")
					.append("\"time\"").append(":\"").append(appMsg.getTime()).append("\"")
					.append("}");
					
					break;
					
				//语音	
				case 3:
					
					sb.append("\"EMessageStyle9\":")
					.append("{")
					.append("\"sortindex\"").append(":\"").append(i).append("\"").append(",")
					.append("\"url\"").append(":\"").append(appMsg.getUrl()).append("\"").append(",")
					.append("\"time\"").append(":\"").append(appMsg.getTime()).append("\"")
					.append("}");
					
					break;
					
				default:
					EmpExecutionContext.error("获取样式对象失败，未支持该种类型："+appMsg.getMsgType());
					return null;
			}
			
			sb.append(",\"stylecount\"").append(":\"").append(size).append("\"");
			
			sb.append("}");
			
			return sb.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取消息内容json异常。");
			return null;
		}
		
	}
	
	/**
	 * 过滤重号
	 * @description    
	 * @param Serial
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-8-28 下午09:54:53
	 */
	private boolean isRepeatSerial(StoreCache cache, String Serial)
	{
		boolean isRepeat =false;
			try
			{
				//开始时间，第一次进入为0
				if(cache.getStart() == 0)
				{
					cache.setStart(System.currentTimeMillis());
				}
				//当前存放数据的索引，下标0为开始记录时间
				cache.setCurIndex((int)(System.currentTimeMillis()-cache.getStart())/(60*1000)%10);
				//当前索引不等于最后一次比较索引
				if(cache.getCurIndex() != cache.getLastIndex())
				{
					//从新开始
					if(cache.getCurIndex() ==0)
					{
						cache.setStart(System.currentTimeMillis());
					}
					//创建新下标
					cache.cacheList.set(cache.getCurIndex(), new HashSet<String>());
				}
				//当前集合中存在
				if(!cache.cacheList.get(cache.getCurIndex()).add(Serial))
				{
					//重号
					isRepeat = true;
				}
				//当前列表不存，遍历其他集合
				else
				{
					int index =cache.getCurIndex();
					for(int i=0; i<9; i++)
					{
						index = ((cache.getCurIndex()-i)+9)%10;
						if(cache.cacheList.get(index).contains(Serial))
						{
							//重号
							isRepeat = true;
							break;
						}
					}
				}
				//记录存在数据下标
				cache.setLastIndex(cache.getCurIndex());
				return isRepeat;
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "过滤重号异常,流水号:"+Serial);
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
			EmpExecutionContext.error(e, "处理Mo重发失败异常。");
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
			boolean updateRes = new WgDAO().updateMoDB(objectMap, conditionMap);
			return updateRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新Mo发送消息发送异常。");
			return false;
		}
	}

}
