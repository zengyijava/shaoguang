/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-27 下午06:59:44
 */
package com.montnets.emp.monitor.biz;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONObject;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.GenericEmpDAO;
import com.montnets.emp.common.dao.impl.GenericEmpTransactionDAO;
import com.montnets.emp.common.entity.LfNodeBaseInfo;
import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.database.IConnectionManager;
import com.montnets.emp.entity.monitor.LfBusareasend;
import com.montnets.emp.entity.monitor.LfMonDGateBuf;
import com.montnets.emp.entity.monitor.LfMonDbstate;
import com.montnets.emp.entity.monitor.LfMonDgtacinfo;
import com.montnets.emp.entity.monitor.LfMonDhost;
import com.montnets.emp.entity.monitor.LfMonDproce;
import com.montnets.emp.entity.monitor.LfMonDspacinfo;
import com.montnets.emp.entity.monitor.LfMonHostnet;
import com.montnets.emp.entity.monitor.LfMonSgtacinfo;
import com.montnets.emp.entity.monitor.LfMonShost;
import com.montnets.emp.entity.monitor.LfMonSproce;
import com.montnets.emp.entity.monitor.LfMonSspacinfo;
import com.montnets.emp.monitor.constant.MonDspAccountParams;
import com.montnets.emp.monitor.constant.MonDwbsBufParams;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.monitor.dao.MonitorDAO;

/**
 * @description
 * @project p_comm
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-27 下午06:59:44
 */

public class ParsetMqXmlBiz extends SuperBiz
{
	BaseBiz	baseBiz	= new BaseBiz();

	MonitorBaseInfoBiz monitorBaseInfoBiz = new MonitorBaseInfoBiz();
	
	// UTF8格式消息
	private String monitorMsgUtf8 = "";
	
	// 日志信息
	private StringBuffer monMsgLogInfo = new StringBuffer();
	
	private IConnectionManager connectionManager = new ConnectionManagerImp();
	
	//数据库连接
	private Connection connection = null;
	
    Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 解析XML格式字符串并启动数据分析线程
	 *
	 * @description
	 * @param xml
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-30 上午10:33:02
	 */
	@SuppressWarnings("unchecked")
	public void paresetStringXml(String xml)
	{
		try
		{
			//重置日志信息
			monMsgLogInfo.setLength(0);
			// 重置消息
			monitorMsgUtf8 = "";
			Document doc = null;
			// 应用编号(网关编号)
			String appIdStr = "";
			// 应用编号(网关编号)
			Long appId;
			// 应用类型
			String appType = "";
			// 将字符串转为XML
			try
			{
				monitorMsgUtf8 = URLDecoder.decode(xml, "UTF-8");
				doc = DocumentHelper.parseText(monitorMsgUtf8.substring(0, monitorMsgUtf8.lastIndexOf(">")+1));
			}
			catch (Exception e)
			{
				EmpExecutionContext.error("监控消息编码格式转换异常，xml:" + xml + "，monitorMsg:" + monitorMsgUtf8);
				return;
			}
			if(doc == null)
			{
				EmpExecutionContext.error("监控消息编码格式转换失败， doc为NULL，monitorMsg:" + monitorMsgUtf8);
				return;
			}
			// 获取根节点
			Element rootElt = null;
			rootElt = doc.getRootElement();
			if(rootElt == null)
			{
				EmpExecutionContext.error("解析监控消息获取节点异常， rootElt为NULL，monitorMsg:" + monitorMsgUtf8);
				return;
			}
			// 获取根节点下的子节点APPINFO
			Iterator iteratorAppInfo = rootElt.elementIterator("APPINFO");
			// EVENT节点
			Iterator iteratorEvent = null;
			// 遍历APPINFO节点
			while(iteratorAppInfo.hasNext())
			{
				Element recordEle = (Element) iteratorAppInfo.next();
				// 拿到APPINFO节点的ID值
				appIdStr = recordEle.attributeValue("ID").trim();
				// 拿到APPINFO节点的TYPE值
				appType = recordEle.attributeValue("TYPE").trim();
				if("".equals(appIdStr) || appIdStr == null || "".equals(appType) || appType == null)
				{
					EmpExecutionContext.error("解析监控消息参数信息异常，appId:" + appIdStr + "，appType:" + appType +"，monitorMsg:" + monitorMsgUtf8);
					return;
				}
				appId = Long.valueOf(appIdStr);
				// 获取子节点APPINFO下的子节点EVENT
				iteratorEvent = recordEle.elementIterator("EVENT");
				if(iteratorEvent == null)
				{
					EmpExecutionContext.error("解析监控消息信息EVENT节点异常，iteratorEvent为NULL，monitorMsg:" + monitorMsgUtf8);
					return;
				}
				// 设置解析后的消息信息到缓存
				setParsetXmlInfo(iteratorEvent, appId, appType);
			}
			//需要打印监控日志信息
			if(MonitorStaticValue.isMonMsgLog())
			{
				EmpExecutionContext.monMsgLog(monMsgLogInfo.toString());
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "解析监控消息异常，monitorMsg:" + monitorMsgUtf8);
		}
	}

	/**
	 * 设置解析后的消息信息到缓存
	 *
	 * @description
	 * @param iteratorEvent
	 * @param appId
	 * @param appType
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-30 上午10:33:33
	 */
	@SuppressWarnings("unchecked")
	private void setParsetXmlInfo(Iterator iteratorEvent, Long appId, String appType)
	{
		// 事件ID
		String eventId = "";
		Element eventEle = null;
		Element conEle = null;
		Iterator iteratorContent = null;
		Iterator iteratorInfo = null;
		// 消息发送时间
		String messageTimeStr = "";
		// 消息发送时间(时间格式)
		Timestamp messageTime = null;
		// 主机编号
		Long hostId = -1L;
		// 程序编号
		Long proceId = -1L;
		// 是否主机信息
		boolean isHost = false;
		try
		{
			// 存放从消息解析出来的主机数据
			LfMonDhost monDhost = new LfMonDhost();
			// 根据网关编号从程序基础信息缓存中获取对应的主机编号
			Map<Long, LfMonSproce> monSproceBase = MonitorStaticValue.getProceBaseMap();
			if(monSproceBase == null || monSproceBase.size() < 1)
			{
				monSproceBase = MonitorStaticValue.getProceBaseMapTemp();
			}
			for (Long sproceId : monSproceBase.keySet())
			{
				//文件服务器类型
				if("5800".equals(appType))
				{
					if(String.valueOf(appId).equals(monSproceBase.get(sproceId).getServernum()))
					{
						hostId = monSproceBase.get(sproceId).getHostid();
						proceId = sproceId;
						break;
					}
				}
				else
				{
					if(appId - monSproceBase.get(sproceId).getGatewayid() == 0)
					{
						hostId = monSproceBase.get(sproceId).getHostid();
						proceId = sproceId;
						break;
					}
				}
			}
			// 遍历EVENT节点
			while(iteratorEvent.hasNext())
			{
				// 获取节点EVENT
				eventEle = (Element) iteratorEvent.next();
				if(eventEle != null)
				{
					// 获取子节点CONTENT
					iteratorContent = eventEle.elementIterator("CONTENT");
					if(iteratorContent == null)
					{
						EmpExecutionContext.error("监控消息获取子节点CONTENT失败，monitorMsg:" + monitorMsgUtf8);
						continue;
					}
				}
				else
				{
					EmpExecutionContext.error("监控消息EVENT节点下无节点CONTENT。");
					return;
				}
				// 拿到EVENT节点的ID值
				eventId = eventEle.attributeValue("ID").trim();
				// 拿到EVENT节点的TM值
				messageTimeStr = eventEle.attributeValue("TM").trim();
				// 设置监控日志
				monMsgLogInfo.append("messageTime:").append(messageTimeStr).append("，appId:").append(appId)
				.append("，appType:").append(appType).append("，eventId:"+eventId).append("。");
				if(!"".equals(messageTimeStr) && messageTimeStr != null)
				{
					// 转为时间格式
					messageTime = timeChange(messageTimeStr);
					if(messageTime == null)
					{
						EmpExecutionContext.error("解析监控消息参数信息异常，messageTime:" + messageTime + "，monitorMsg:" + monitorMsgUtf8);
						return;
					}
				}
				else
				{
					EmpExecutionContext.error("解析监控消息参数信息异常，messageTimeStr:" + messageTimeStr + "，monitorMsg:" + monitorMsgUtf8);
					return;
				}
				if("".equals(eventId) || eventId == null)
				{
					EmpExecutionContext.error("解析监控消息参数信息异常，eventId:" + eventId + "，monitorMsg:" + monitorMsgUtf8);
					return;
				}
				// 遍历CONTENT节点
				while(iteratorContent.hasNext())
				{
					conEle = (Element) iteratorContent.next();
					iteratorInfo = conEle.elementIterator("INFO");
					// 4000：短信网关EMP_GATEWAY消息
					if("4000".equals(appType))
					{
						// 主机信息:1000-主机网络信息;1001-主机资源占用信息;1002-主机硬盘信息
						if("1000".equals(eventId) || "1001".equals(eventId) || "1002".equals(eventId))
						{
//							if(hostId + 1 == 0)
//							{
//								continue;
//							}
							isHost = true;
							// 解析主机信息
							setHostInfo(iteratorInfo, messageTime, eventId, hostId, monDhost, appId);
						}
						else if("1010".equals(eventId))
						{
							// 解析、设置EMP网关WBS程序信息
							setProcedureInfo(iteratorInfo, proceId, messageTime, appId, hostId, appType);
						}
						else if("1011".equals(eventId))
						{
							// 解析、设置SP账号信息信息
							setSpAccoutInfo(iteratorInfo, messageTime, hostId, appId);
						}
						else if("1012".equals(eventId))
						{
							// 解析、设置通道账号信息信息
							setGateAccoutInfo(iteratorInfo, messageTime, appId);
						}
						else if("1013".equals(eventId))
						{
							// 解析、设置WBS缓冲信息
							//setWbsBufInfo(iteratorInfo, messageTime, proceId, appId);
						}
						else if("1014".equals(eventId))
						{
							//解析、设置业务区域监控数据
							setBusAreaInfo(iteratorInfo, messageTime, appId);
						}
					}
					// 3000:短信网关SPGATE消息
					else if("3000".equals(appType))
					{
						if("1100".equals(eventId) || "1101".equals(eventId) || "1102".equals(eventId))
						{
							isHost = true;
							// 解析主机信息
							setHostInfo(iteratorInfo, messageTime, eventId, hostId, monDhost, appId);
						}
						else if("1110".equals(eventId))
						{
							// 解析、设置EMP网关SPGATE程序信息
							setProcedureInfo(iteratorInfo, proceId, messageTime, appId, hostId, appType);
						}
						else if("1111".equals(eventId))
						{
							// 解析、设置SPGATE缓冲信息
							setSpGateBufInfo(iteratorInfo, messageTime, proceId, appId);
						}
					}
					// 5800:文件服务器消息
					else if("5800".equals(appType))
					{
						//主机
						if("1500".equals(eventId))
						{
							isHost = true;
							// 解析文件服务器主机信息
							setFileServerHostInfo(iteratorInfo, messageTime, eventId, hostId, monDhost, appId);
						}
						else if("1510".equals(eventId))
						{
							// 解析文件服务器程序信息
							setProcedureInfo(iteratorInfo, proceId, messageTime, appId, hostId, appType);
						}
					}
					else
					{
						EmpExecutionContext.error("解析网关监控消息异常，appType:"+appType + "，monitorMsg:" + monitorMsgUtf8);
						return;
					}
				}
			}
			// 设置主机信息
			if(hostId + 1 != 0)
			{
                monDhost.setHostid(hostId);
                Map<Long, LfMonShost> hostBaseMap = MonitorStaticValue.getHostBaseMap();
                if(hostBaseMap == null || hostBaseMap.size() < 1)
                {
                	hostBaseMap = MonitorStaticValue.getHostBaseMapTemp();
                }
				monDhost.setHostName(hostBaseMap.get(hostId).getHostname());
                monDhost.setAdapter1(hostBaseMap.get(hostId).getAdapter1());
                monDhost.setServerNum(StaticValue.getServerNumber());
                saveOrUpdate(monDhost,"hostid");
			}
			if(isHost)
			{
				//设置主机监控
				String ipAddr = monDhost.getIpAddr();
				if(ipAddr != null && !"".equals(ipAddr.trim()))
				{
					setHostNetInfo(appId, appType, ipAddr, messageTime);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置解析后的消息信息异常 ，monitorMsg:" + monitorMsgUtf8);
		}
	}

	/**
	 * 设置主机网络监控信息
	 * @description    
	 * @param appId
	 * @param appType
	 * @param ipAddr
	 * @param messageTime       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-27 下午07:13:20
	 */
	private void setHostNetInfo(Long appId, String appType, String ipAddr, Timestamp messageTime)
	{
        try
		{
			//主机网络监控信息
        	Map<String, LfMonHostnet> hostNetMap = this.getHostNetMonInfo();
			LfMonHostnet monHostnet = null;
			String key = "";
			boolean isUpdateIp = false;
			//主机名
			String hostName = "";
			//程序类型
			Integer proceType = 0;
	        if("4000".equals(appType))
			{
	        	hostName = "EMP网关"+appId+"(EMP_GW)主机";
	        	proceType = 5200;
			}
	        else if("3000".equals(appType))
	        {
	        	hostName = "运营商接口"+appId+"(SPGATE)主机";
	        	proceType = 5300;
	        }
	        else if("5800".equals(appType))
	        {
	        	hostName = "文件服务器"+appId+"主机";
	        	proceType = 5800;
	        }
	        else
	        {
	        	EmpExecutionContext.error("设置主机网络监控信息异常，appType:"+appType+"，monitorMsg:" + monitorMsgUtf8);
	        	return;
	        }
			//遍历WEB节点服务器
			for(LfNodeBaseInfo nodeBaseInfo:MonitorStaticValue.getNodeBaseInfoList())
			{
				key = nodeBaseInfo.getNodeId()+appId+proceType;
				if(hostNetMap.containsKey(key))
				{
					if(!ipAddr.equals(hostNetMap.get(key).getIpaddr()))
					{
						//更新IP
						isUpdateIp = true;
					}
				}
				else
				{
					//新增记录
					monHostnet = new LfMonHostnet();
			        monHostnet.setWebnode(Long.parseLong(nodeBaseInfo.getNodeId()));
			        monHostnet.setWebname("WEB服务器"+nodeBaseInfo.getNodeId()+"主机");
			        monHostnet.setProcenode(appId);
			        monHostnet.setProcetype(proceType);
			        monHostnet.setHostname(hostName);
			        monHostnet.setMontype(0);
			        monHostnet.setIpaddr(ipAddr);
			        monHostnet.setCreatetime(new Timestamp(System.currentTimeMillis()));
			        monHostnet.setUpdatetime(messageTime);
			        monHostnet.setMonstatus(1);
			        monHostnet.setEvttype(0);
			        monHostnet.setNetstate(0);
			        monHostnet.setSmsalflag1(0L);
			        monHostnet.setMailalflag1(0L);
			        monHostnet.setServernum(StaticValue.getServerNumber());
			        try
					{
						//数据更新
	                    Map<String, String> kyeMap = new HashMap<String, String>();
	                    kyeMap.put("WEBNODE", "");
	                    kyeMap.put("PROCENODE", "");
	                    kyeMap.put("PROCETYPE", "");
	                    saveOrUpdateMultiKey(monHostnet,kyeMap);
					}
					catch (Exception e)
					{
						EmpExecutionContext.error(e, "新增主机网络监控信息失败，monitorMsg:" + monitorMsgUtf8);
					}
					continue;
				}
			}
			//更新主机网络监控信息
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
            objectMap.put("servernum", StaticValue.getServerNumber());
            objectMap.put("updatetime", format.format(messageTime));
            //更新主机IP
			if(isUpdateIp)
			{
				objectMap.put("ipaddr", String.valueOf(ipAddr));
			}
			//更新条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("procenode", String.valueOf(appId));
			conditionMap.put("procetype", String.valueOf(proceType));
			baseBiz.updateForMonitor(getConnection(), true, LfMonHostnet.class, objectMap, conditionMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置设置主机网络监控信息异常 ，monitorMsg:" + monitorMsgUtf8);
		}
	}
	
	/**
	 * 解析主机信息
	 *
	 * @description
	 * @param iteratorInfo
	 * @param messageTime
	 * @param eventId
	 * @param hostId
	 * @param monDhost
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-4 下午01:43:57
	 */
	@SuppressWarnings("unchecked")
	private void setHostInfo(Iterator iteratorInfo, Timestamp messageTime, String eventId, Long hostId, LfMonDhost monDhost, Long appId)
	{
		// 存在主机监控信息,进行消息解析
//		Map<Long, LfMonShost> hostBaseMap = MonitorStaticValue.hostBaseMap;
//		if(hostBaseMap == null || hostBaseMap.size() < 1)
//		{
//			hostBaseMap = MonitorStaticValue.hostBaseMapTemp;
//		}
		Element infoEle = null;
		// 物理内存
		String memUse = "";
		// 虚拟内存
		String vMemUse = "";
		// 磁盘空间
		String diskSpace = "";
		// CPU占用量
		String cpuUsage = "";
		// 物理内存使用量
		String memUsage = "";
		// 虚拟内存使用量
		String vMemUsage = "";
		// 磁盘剩余量
		String diskFreeSpace = "";
		// 进程总数
		String processCnt = "";
		// IP
		String ipAddr = "";

		// 根据程序中的主机编号从缓存中获取对应的主机对象
//		Map<Long, MonDhostParams> hostMap = MonitorStaticValue.hostMap;
//		if(hostMap == null || hostMap.size() < 1)
//		{
//			hostMap = MonitorStaticValue.hostMapTemp;
//		}
//		MonDhostParams monDhostParams = hostMap.get(hostId);
		try
		{
			// 遍历INFO节点
			while(iteratorInfo.hasNext())
			{
				infoEle = (Element) iteratorInfo.next();
				// EVENT节点ID为1001或1101，主机资源占用信息
				if("1001".equals(eventId) || "1101".equals(eventId))
				{
					// CPU占用量
					cpuUsage = infoEle.attributeValue("CpuUsage");
					monDhost.setCpuusage(cpuUsage != null && !"".equals(cpuUsage) ? Integer.valueOf(cpuUsage.trim()) : 0);
					// 物理内存
					memUse = infoEle.attributeValue("PhyMemTol");
					monDhost.setMemuse(memUse != null && !"".equals(memUse) ? Integer.valueOf(memUse.trim()) : 0);
					// 物理内存使用量
					memUsage = infoEle.attributeValue("PhyMemUse");
					monDhost.setMemusage(memUsage != null && !"".equals(memUsage) ? Integer.valueOf(memUsage.trim()) : 0);
					// 虚拟内存
					vMemUse = infoEle.attributeValue("VMemTol");
					monDhost.setVmemuse(vMemUse != null && !"".equals(vMemUse) ? Integer.valueOf(vMemUse.trim()) : 0);
					// 虚拟内存使用量
					vMemUsage = infoEle.attributeValue("VMemUse");
					monDhost.setVmemusage(vMemUsage != null && !"".equals(vMemUsage) ? Integer.valueOf(vMemUsage.trim()) : 0);
					// 进程总数
					processCnt = infoEle.attributeValue("ProcessCnt");
					monDhost.setProcesscnt(processCnt != null && !"".equals(processCnt) ? Integer.valueOf(processCnt.trim()) : 0);
				}
				// EVENT节点ID为1002或1102，主机硬盘信息
				else if("1002".equals(eventId) || "1102".equals(eventId))
				{
					// 磁盘空间
					diskSpace = infoEle.attributeValue("HardTol");
					monDhost.setDiskspace(diskSpace != null && !"".equals(diskSpace) ? Integer.valueOf(diskSpace.trim()) : 0);
					// 磁盘剩余量
					diskFreeSpace = infoEle.attributeValue("HardCanUse");
					monDhost.setDiskfreespace(diskFreeSpace != null && !"".equals(diskFreeSpace) ? Integer.valueOf(diskFreeSpace.trim()) : 0);
				}
				// EVENT节点ID为1000或1100，网络信息
				else if("1000".equals(eventId) || "1100".equals(eventId))
				{ 
					//IP
					ipAddr = infoEle.attributeValue("IpAddr");
					if(ipAddr != null && !"".equals(ipAddr.trim()) )
					{
						ipAddr = monitorBaseInfoBiz.getValidIp(ipAddr, monDhost.getIpAddr());
						if(ipAddr.trim().length() > 0)
						{
							if(monDhost.getIpAddr() != null && !"".equals(monDhost.getIpAddr().trim()))
							{
								ipAddr += ","+monDhost.getIpAddr();
							}
							//长度超过256，需要截取
							if(ipAddr.length() > 240)
							{
								ipAddr = ipAddr.substring(0, 240);
							}
							monDhost.setIpAddr(ipAddr);
						}
						
					}
				}
				// 更新时间
				monDhost.setUpdatetime(messageTime);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "解析主机监控消息参数格式异常！主机编号：" + hostId + "，monitorMsg:" + monitorMsgUtf8);
		}
	}

	/**
	 * 解析文件服务器主机信息
	 * @description    
	 * @param iteratorInfo
	 * @param messageTime
	 * @param eventId
	 * @param hostId
	 * @param monDhost
	 * @param appId       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-23 下午01:16:51
	 */
	private void setFileServerHostInfo(Iterator iteratorInfo, Timestamp messageTime, String eventId, Long hostId, LfMonDhost monDhost, Long appId)
	{
		Element infoEle = null;
		// 物理内存
		String memUse = "";
		// 虚拟内存
		String vMemUse = "";
		// 磁盘空间
		String diskSpace = "";
		// CPU占用量
		String cpuUsage = "";
		// 物理内存使用量
		String memUsage = "";
		// 虚拟内存使用量
		String vMemUsage = "";
		// 磁盘剩余量
		String diskFreeSpace = "";
		// 进程总数
		String processCnt = "";
		// IP
		String ipAddr = "";
		try
		{
			// 遍历INFO节点
			while(iteratorInfo.hasNext())
			{
				infoEle = (Element) iteratorInfo.next();
				// CPU占用量
				cpuUsage = infoEle.attributeValue("CpuUsage");
				monDhost.setCpuusage(cpuUsage != null && !"".equals(cpuUsage) ? Integer.valueOf(cpuUsage.trim()) : 0);
				// 物理内存
				memUse = infoEle.attributeValue("PhyMemTol");
				monDhost.setMemuse(memUse != null && !"".equals(memUse) ? Integer.valueOf(memUse.trim()) : 0);
				// 物理内存使用量
				memUsage = infoEle.attributeValue("PhyMemUse");
				monDhost.setMemusage(memUsage != null && !"".equals(memUsage) ? Integer.valueOf(memUsage.trim()) : 0);
				// 虚拟内存
				vMemUse = infoEle.attributeValue("VMemTol");
				monDhost.setVmemuse(vMemUse != null && !"".equals(vMemUse) ? Integer.valueOf(vMemUse.trim()) : 0);
				// 虚拟内存使用量
				vMemUsage = infoEle.attributeValue("VMemUse");
				monDhost.setVmemusage(vMemUsage != null && !"".equals(vMemUsage) ? Integer.valueOf(vMemUsage.trim()) : 0);
				// 进程总数
				processCnt = infoEle.attributeValue("ProcessCnt");
				monDhost.setProcesscnt(processCnt != null && !"".equals(processCnt) ? Integer.valueOf(processCnt.trim()) : 0);
				// 磁盘空间
				diskSpace = infoEle.attributeValue("HardTol");
				monDhost.setDiskspace(diskSpace != null && !"".equals(diskSpace) ? Integer.valueOf(diskSpace.trim()) : 0);
				// 磁盘剩余量
				diskFreeSpace = infoEle.attributeValue("HardCanUse");
				monDhost.setDiskfreespace(diskFreeSpace != null && !"".equals(diskFreeSpace) ? Integer.valueOf(diskFreeSpace.trim()) : 0);
				
				//IP
				ipAddr = infoEle.attributeValue("IpAddr");
				if(ipAddr != null && !"".equals(ipAddr.trim()) )
				{
					ipAddr = monitorBaseInfoBiz.getValidIp(ipAddr, monDhost.getIpAddr());
					if(ipAddr.trim().length() > 0)
					{
						if(monDhost.getIpAddr() != null && !"".equals(monDhost.getIpAddr().trim()))
						{
							ipAddr += ","+monDhost.getIpAddr();
						}
						//长度超过256，需要截取
						if(ipAddr.length() > 240)
						{
							ipAddr = ipAddr.substring(0, 240);
						}
						monDhost.setIpAddr(ipAddr);
					}
				}
				// 更新时间
				monDhost.setUpdatetime(messageTime);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "解析主机监控消息参数格式异常！主机编号：" + hostId + "，monitorMsg:" + monitorMsgUtf8);
			// 主机更新状态
		}
	}
	
	/**
	 * 解析设置EMP网关WBS、SPGATE、文件件服务器程序信息
	 *
	 * @description
     * @param iteratorInfo
     * @param proceId
     * @param messageTime
     * @param appId
     * @param hostId
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-4 下午01:44:14
	 */
	@SuppressWarnings("unchecked")
	private void setProcedureInfo(Iterator iteratorInfo, Long proceId, Timestamp messageTime, Long appId, Long hostId)
	{
		// 存在程序监控信息,进行消息解析
		Map<Long, LfMonSproce> proceBaseMap = MonitorStaticValue.getProceBaseMap();
		if(proceBaseMap == null || proceBaseMap.size() < 1)
		{
			proceBaseMap = MonitorStaticValue.getProceBaseMapTemp();
		}
		// 通过程序编号从缓存中获取对象
		LfMonDproce monDproce = new LfMonDproce();
		// 数据库监控信息
		LfMonDbstate monDbstate = new LfMonDbstate();
		Element infoEle = null;
		// CPU占用量
		String cpuUsage = "";
		// 物理内存使用量
		String memUsage = "";
		// 虚拟内存使用量
		String vMEMUsage = "";
		// 最低磁盘剩余量
		String dispFree = "";
		// 数据库连接状态
		String DBConnectState = "";
		//新增操作状态
		String dbaddoprState = "";
		//新增操作描述
		String dbaddoprDes = "";
		//删除操作状态
		String dbdeloprState = "";
		//删除操作描述
		String dbdeloprDes = "";
		//修改操作状态
		String dbmodioprState = "";
		//修改操作描述
		String dbmodioprDes = "";
		//查询操作状态
		String dbdispoprState = "";
		//查询操作描述
		String dbdispoprDes = "";
		try
		{
			// 遍历INFO节点
			while(iteratorInfo.hasNext())
			{
				infoEle = (Element) iteratorInfo.next();
				// CPU占用量
				cpuUsage = infoEle.attributeValue("CpuUsage");
				monDproce.setCpuusage(cpuUsage != null && !"".equals(cpuUsage) ? Integer.valueOf(cpuUsage.trim()) : 0);
				// 物理内存使用量
				memUsage = infoEle.attributeValue("MemUsage");
				monDproce.setMemusage(memUsage != null && !"".equals(memUsage) ? Integer.valueOf(memUsage.trim()) : 0);
				// 虚拟内存使用量
				vMEMUsage = infoEle.attributeValue("VMEMUsage");
				monDproce.setVmemusage(vMEMUsage != null && !"".equals(vMEMUsage) ? Integer.valueOf(vMEMUsage.trim()) : 0);
				// 最低磁盘剩余量
				dispFree = infoEle.attributeValue("DISKFREE");
                monDproce.setDiskFree(dispFree != null && !"".equals(dispFree) ? Integer.valueOf(dispFree.trim()) : 0);
				// 数据库连接状态
				DBConnectState = infoEle.attributeValue("DBConnectState");
                monDproce.setDbconnectstate(DBConnectState != null && !"".equals(DBConnectState) ? Integer.valueOf(DBConnectState.trim()) : 0);
                monDbstate.setDbconnectstate(DBConnectState != null && !"".equals(DBConnectState) ? Integer.valueOf(DBConnectState.trim()) : 0);
				// 更新时间
				monDproce.setUpdatetime(messageTime);
				monDproce.setStarttime(new Timestamp(System.currentTimeMillis()));
                monDproce.setServerNum(StaticValue.getServerNumber());
                
                //新增操作状态
                dbaddoprState = infoEle.attributeValue("DBAddOprState");
                monDbstate.setAddopr(dbaddoprState != null && !"".equals(dbaddoprState) ? Integer.valueOf(dbaddoprState.trim()) : 0);
             	//新增操作描述
                dbaddoprDes = infoEle.attributeValue("DBAddOprDes");
                monDbstate.setDbaddoprdes(dbaddoprDes != null && !"".equals(dbaddoprDes) ? dbaddoprDes.trim() : " ");
                //删除操作状态
                dbdeloprState = infoEle.attributeValue("DBDelOprState");
                monDbstate.setDelopr(dbdeloprState != null && !"".equals(dbdeloprState) ? Integer.valueOf(dbdeloprState.trim()) : 0);
                //删除操作描述
                dbdeloprDes = infoEle.attributeValue("DBDelOprDes");
                monDbstate.setDbdeloprdes(dbdeloprDes != null && !"".equals(dbdeloprDes) ? dbdeloprDes.trim() : " ");
                //修改操作状态
                dbmodioprState = infoEle.attributeValue("DBModiOprState");
                monDbstate.setModiopr(dbmodioprState != null && !"".equals(dbmodioprState) ? Integer.valueOf(dbmodioprState.trim()) : 0);
                //修改操作描述
                dbmodioprDes = infoEle.attributeValue("DBModiOprDes");
                monDbstate.setDbmodioprdes(dbmodioprDes != null && !"".equals(dbmodioprDes) ? dbmodioprDes.trim() : " ");
                //查询操作状态
                dbdispoprState = infoEle.attributeValue("DBDispOprState");
                monDbstate.setDispopr(dbdispoprState != null && !"".equals(dbdispoprState) ? Integer.valueOf(dbdispoprState.trim()) : 0);
                //查询操作描述
                dbdispoprDes = infoEle.attributeValue("DBDispOprDes");
                monDbstate.setDbdispoprdes(dbdispoprDes != null && !"".equals(dbdispoprDes) ? dbdispoprDes.trim() : " ");
                //更新数据库监控信息
                
                
                //存在程序监控
        		if(proceBaseMap.containsKey(proceId))
        		{
        	        // 程序编号
        	        monDproce.setProceid(proceId);
        	        // 网关编号
        	        monDproce.setGatewayId(appId);
        	        // 主机名称
        	        Map<Long, LfMonShost> hostBaseMap = MonitorStaticValue.getHostBaseMap();
        	        if(hostBaseMap == null || hostBaseMap.size() < 1)
        	        {
        	        	hostBaseMap = MonitorStaticValue.getHostBaseMapTemp();
        	        }
        			monDproce.setHostname(hostId + 1 == 0 ? "-" : hostBaseMap.get(hostId).getHostname());
        	        // 程序名称
        	        monDproce.setProcename(proceBaseMap.get(proceId).getProcename());
        	        // 程序类型
        	        monDproce.setProcetype(proceBaseMap.get(proceId).getProcetype());
        	        // 主机ID
        	        monDproce.setHostid(hostId);
        	        // 版本号
        	        monDproce.setVersion(proceBaseMap.get(proceId).getVersion());
        	        //更新程序监控数据
        			saveOrUpdate(monDproce,"proceid");
        		}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "解析程序监控消息参数格式异常，程序编号： " + proceId + "，程序名称:" + monDproce.getProcename() + "，monitorMsg:" + monitorMsgUtf8);
		}
	}

	/**
	 * 解析设置EMP网关WBS、SPGATE、文件件服务器程序信息
	 *
	 * @description
     * @param iteratorInfo
     * @param proceId
     * @param messageTime
     * @param appId
     * @param hostId
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-4 下午01:44:14
	 */
	@SuppressWarnings("unchecked")
	private void setProcedureInfo(Iterator iteratorInfo, Long proceId, Timestamp messageTime, Long appId, Long hostId, String appType)
	{
		// 存在程序监控信息,进行消息解析
		Map<Long, LfMonSproce> proceBaseMap = MonitorStaticValue.getProceBaseMap();
		if(proceBaseMap == null || proceBaseMap.size() < 1)
		{
			proceBaseMap = MonitorStaticValue.getProceBaseMapTemp();
		}
		// 通过程序编号从缓存中获取对象
		LfMonDproce monDproce = new LfMonDproce();
		// 数据库监控信息
		LfMonDbstate monDbstate = new LfMonDbstate();
		Element infoEle = null;
		// CPU占用量
		String cpuUsage = "";
		// 物理内存使用量
		String memUsage = "";
		// 虚拟内存使用量
		String vMEMUsage = "";
		// 最低磁盘剩余量
		String dispFree = "";
		// 数据库连接状态
		String DBConnectState = "";
		//新增操作状态
		String dbaddoprState = "";
		//新增操作描述
		String dbaddoprDes = "";
		//删除操作状态
		String dbdeloprState = "";
		//删除操作描述
		String dbdeloprDes = "";
		//修改操作状态
		String dbmodioprState = "";
		//修改操作描述
		String dbmodioprDes = "";
		//查询操作状态
		String dbdispoprState = "";
		//查询操作描述
		String dbdispoprDes = "";
		try
		{
			// 遍历INFO节点
			while(iteratorInfo.hasNext())
			{
				infoEle = (Element) iteratorInfo.next();
				// CPU占用量
				cpuUsage = infoEle.attributeValue("CpuUsage");
				monDproce.setCpuusage(cpuUsage != null && !"".equals(cpuUsage) ? Integer.valueOf(cpuUsage.trim()) : 0);
				// 物理内存使用量
				memUsage = infoEle.attributeValue("MemUsage");
				monDproce.setMemusage(memUsage != null && !"".equals(memUsage) ? Integer.valueOf(memUsage.trim()) : 0);
				// 虚拟内存使用量
				vMEMUsage = infoEle.attributeValue("VMEMUsage");
				monDproce.setVmemusage(vMEMUsage != null && !"".equals(vMEMUsage) ? Integer.valueOf(vMEMUsage.trim()) : 0);
				// 最低磁盘剩余量
				dispFree = infoEle.attributeValue("DISKFREE");
                monDproce.setDiskFree(dispFree != null && !"".equals(dispFree) ? Integer.valueOf(dispFree.trim()) : 0);
				// 数据库连接状态
				DBConnectState = infoEle.attributeValue("DBConnectState");
                monDproce.setDbconnectstate(DBConnectState != null && !"".equals(DBConnectState) ? Integer.valueOf(DBConnectState.trim()) : 0);
				// 更新时间
				monDproce.setUpdatetime(messageTime);
				monDproce.setStarttime(new Timestamp(System.currentTimeMillis()));
                monDproce.setServerNum(StaticValue.getServerNumber());
                
                //不是文件服务器，设置数据库监控消息
                if(!"5800".equals(appType))
                {
                	String proceType = "";
                	if("4000".equals(appType))
        			{
        	        	proceType = "5200";
        			}
        	        else if("3000".equals(appType))
        	        {
        	        	proceType = "5300";
        	        }
        	        else if("5800".equals(appType))
        	        {
        	        	proceType = "5800";
        	        }
	        		Map<String, LfMonDbstate> monDbstateMap = MonitorStaticValue.getDbMonMap();
	        		if(monDbstateMap == null || monDbstateMap.size() < 1)
	        		{
	        			monDbstateMap = MonitorStaticValue.getDbMonMapTemp();
	        		}
	        		//程序在数据库监控消息缓存中存在
	        		if(monDbstateMap.containsKey(proceType+appId))
	        		{
	        			monDbstate.setId(monDbstateMap.get(proceType+appId).getId());
	        			//程序编号
	        			monDbstate.setProcenode(appId);
	        			//程序类型
	        			monDbstate.setProcetype(Integer.parseInt(proceType));
	        			//数据库连接状态
	        			monDbstate.setDbconnectstate(DBConnectState != null && !"".equals(DBConnectState) ? Integer.valueOf(DBConnectState.trim()) : 0);
	        			//新增操作状态
	        			dbaddoprState = infoEle.attributeValue("DBAddOprState");
	        			monDbstate.setAddopr(dbaddoprState != null && !"".equals(dbaddoprState) ? Integer.valueOf(dbaddoprState.trim()) : 0);
	        			//新增操作描述
	        			dbaddoprDes = infoEle.attributeValue("DBAddOprDes");
	        			monDbstate.setDbaddoprdes(dbaddoprDes != null && !"".equals(dbaddoprDes) ? dbaddoprDes.trim() : " ");
	        			if(monDbstate.getDbaddoprdes().length() > 128)
	        			{
	        				monDbstate.setDbaddoprdes(monDbstate.getDbaddoprdes().substring(0, 128));
	        			}
	        			//删除操作状态
	        			dbdeloprState = infoEle.attributeValue("DBDelOprState");
	        			monDbstate.setDelopr(dbdeloprState != null && !"".equals(dbdeloprState) ? Integer.valueOf(dbdeloprState.trim()) : 0);
	        			//删除操作描述
	        			dbdeloprDes = infoEle.attributeValue("DBDelOprDes");
	        			monDbstate.setDbdeloprdes(dbdeloprDes != null && !"".equals(dbdeloprDes) ? dbdeloprDes.trim() : " ");
	        			if(monDbstate.getDbdeloprdes().length() > 128)
	        			{
	        				monDbstate.setDbdeloprdes(monDbstate.getDbdeloprdes().substring(0, 128));
	        			}
	        			//修改操作状态
	        			dbmodioprState = infoEle.attributeValue("DBModiOprState");
	        			monDbstate.setModiopr(dbmodioprState != null && !"".equals(dbmodioprState) ? Integer.valueOf(dbmodioprState.trim()) : 0);
	        			//修改操作描述
	        			dbmodioprDes = infoEle.attributeValue("DBModiOprDes");
	        			monDbstate.setDbmodioprdes(dbmodioprDes != null && !"".equals(dbmodioprDes) ? dbmodioprDes.trim() : " ");
	        			if(monDbstate.getDbmodioprdes().length() > 128)
	        			{
	        				monDbstate.setDbmodioprdes(monDbstate.getDbmodioprdes().substring(0, 128));
	        			}
	        			//查询操作状态
	        			dbdispoprState = infoEle.attributeValue("DBDispOprState");
	        			monDbstate.setDispopr(dbdispoprState != null && !"".equals(dbdispoprState) ? Integer.valueOf(dbdispoprState.trim()) : 0);
	        			//查询操作描述
	        			dbdispoprDes = infoEle.attributeValue("DBDispOprDes");
	        			monDbstate.setDbdispoprdes(dbdispoprDes != null && !"".equals(dbdispoprDes) ? dbdispoprDes.trim() : " ");
	        			if(monDbstate.getDbdispoprdes().length() > 128)
	        			{
	        				monDbstate.setDbdispoprdes(monDbstate.getDbdispoprdes().substring(0, 128));
	        			}
	        			//更新时间
	        			monDbstate.setUpdatetime(messageTime);
	        			//WEB服务器节点
	        			monDbstate.setServerNum(StaticValue.getServerNumber());
	        			//更新数据库监控信息
	        			baseBiz.updateObj(getConnection(), monDbstate);
	        		}
                }
                //存在程序监控
        		if(proceBaseMap.containsKey(proceId))
        		{
        	        // 程序编号
        	        monDproce.setProceid(proceId);
        	        // 网关编号
        	        monDproce.setGatewayId(appId);
        	        // 主机名称
        	        Map<Long, LfMonShost> hostBaseMap = MonitorStaticValue.getHostBaseMap();
        	        if(hostBaseMap == null || hostBaseMap.size() < 1)
        	        {
        	        	hostBaseMap = MonitorStaticValue.getHostBaseMapTemp();
        	        }
        			monDproce.setHostname(hostId + 1 == 0 ? "-" : hostBaseMap.get(hostId).getHostname());
        	        // 程序名称
        	        monDproce.setProcename(proceBaseMap.get(proceId).getProcename());
        	        // 程序类型
        	        monDproce.setProcetype(proceBaseMap.get(proceId).getProcetype());
        	        // 主机ID
        	        monDproce.setHostid(hostId);
        	        // 版本号
        	        monDproce.setVersion(proceBaseMap.get(proceId).getVersion());
        	        //更新程序监控数据
        			saveOrUpdate(monDproce,"proceid");
        		}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "解析程序监控消息参数格式异常，程序编号： " + proceId + "，程序名称:" + monDproce.getProcename() + "，monitorMsg:" + monitorMsgUtf8);
		}
	}
	
	/**
	 * 解析SP账号信息
	 *
	 * @description
	 * @param iteratorInfo
	 * @param messageTime
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-31 下午03:22:41
	 */
	@SuppressWarnings("unchecked")
	private void setSpAccoutInfo(Iterator iteratorInfo, Timestamp messageTime, Long hostId)
	{
		LfMonDspacinfo dspacinfo = new LfMonDspacinfo();
        Element infoEle = null;
		// 用户帐号
		String spAccountId = "";
		// SP账号相关信息
		String[] spAccountInfo = {"-","1","1"};
		// 登录类型
		String loginType = "0";
		// 遍历INFO节点
		try
		{
			while(iteratorInfo.hasNext())
			{
				infoEle = (Element) iteratorInfo.next();
				// 用户帐号
				spAccountId = infoEle.attributeValue("SpAccountId");
				if("".equals(spAccountId) || spAccountId == null)
				{
					EmpExecutionContext.error("解析监控消息SP账号信息失败，spAccountId: " + spAccountId + "，monitorMsg:" + monitorMsgUtf8);
					continue;
				}
				if(!MonitorStaticValue.getSPACCOUTN_INFO().containsKey(spAccountId))
				{
					EmpExecutionContext.error("解析监控消息SP账号信息异常，用户数据表中无 " + spAccountId + "相关数据。");
					continue;
				}

                // 主机编号
                dspacinfo.setHostId(hostId);


				try
				{
					// SP账号
					dspacinfo.setSpaccountid(spAccountId.trim());
					// 帐号连接数
					String linkNum = infoEle.attributeValue("LinkNum");
					dspacinfo.setLinknum(linkNum != null && !"".equals(linkNum) ? Integer.valueOf(linkNum.trim()) : 0);
					// 帐号登录IP
					String loginIp = infoEle.attributeValue("LoginIp");
					if(loginIp.trim().indexOf(":") != -1)
					{
						String ip = loginIp.trim().split(":")[0];
						if("".equals(ip) || ip == null)
						{
							dspacinfo.setLoginip("0.0.0.0");
						}
						else
						{
							dspacinfo.setLoginip(ip);
						}
					}
					else
					{
						dspacinfo.setLoginip("0.0.0.0");
					}
					// 在线状态
					String onlineStatus = infoEle.attributeValue("OnlineStatus");
					dspacinfo.setOnlinestatus(onlineStatus != null && !"".equals(onlineStatus) ? Integer.valueOf(onlineStatus.trim()) : 0);
					// MT提交总量
					String mtTotalSnd = infoEle.attributeValue("MtTotalSnd");
					dspacinfo.setMtTotalSnd(mtTotalSnd != null && !"".equals(mtTotalSnd) ? Integer.valueOf(mtTotalSnd.trim()) : 0);
					// MT已转发量
					String mtHaveSnd = infoEle.attributeValue("MtHaveSnd");
					dspacinfo.setMthavesnd(mtHaveSnd != null && !"".equals(mtHaveSnd) ? Integer.valueOf(mtHaveSnd.trim()) : 0);
                    // 登录类型
                    loginType = infoEle.attributeValue("LoginType");
                    if(loginType == null || "".equals(loginType))
                    {
                        loginType = "0";
                    }
                    dspacinfo.setLoginType(Integer.valueOf(loginType.trim()));

                    Map<String, MonDspAccountParams> spAccountMap = MonitorStaticValue.getSpAccountMap();
                    if(spAccountMap == null || spAccountMap.size() < 1)
                    {
                    	spAccountMap = MonitorStaticValue.getSpAccountMapTemp();
                    }
					MonDspAccountParams monDspAcParams = spAccountMap.get(spAccountId);
                    // 登录类型为直连账号
                    if("2".equals(loginType))
                    {
                        // 在线状态为离线
                        if("1".equals(onlineStatus))
                        {
                            //如果未记录离线时间
                            if(monDspAcParams != null && (monDspAcParams.getOfflineDuration() == null || monDspAcParams.getOfflineDuration() == 0))
                            {
                                // 记录离线时间
                                dspacinfo.setOfflineDuration(messageTime.getTime());
                            }
                        }
                        else
                        {
                            // 设置为0
                            dspacinfo.setOfflineDuration(0L);
                        }
                    }
                    // 已转发量为0，或和上次的数据相等
                    if(monDspAcParams != null && (dspacinfo.getMtTotalSnd() == 0 || dspacinfo.getMtTotalSnd() - monDspAcParams.getMtTotalSnd() == 0) )
                    {
                        //如果未记录未提交数据时间
                        if(monDspAcParams.getNoMtHaveSnd() == null || monDspAcParams.getNoMtHaveSnd() == 0)
                        {
                            // 设置未提交数据时间
                            dspacinfo.setNoMtHaveSnd(messageTime.getTime());
                        }
                    }
                    else
                    {
                        // 设置为0
                        dspacinfo.setNoMtHaveSnd(0L);
                    }
                    // MT滞留量
					String mtRemained = infoEle.attributeValue("MtRemained");
					dspacinfo.setMtremained(mtRemained != null && !"".equals(mtRemained) ? Integer.valueOf(mtRemained.trim()) : 0);
					// MT提交速度
					String mtSndSpd = infoEle.attributeValue("MtSndSpd");
					dspacinfo.setMtsndspd(mtSndSpd != null && !"".equals(mtSndSpd) ? Integer.valueOf(mtSndSpd.trim()) : 0);
					// MT下发速度
					String mTIssuedSpd = infoEle.attributeValue("MTIssuedSpd");
					dspacinfo.setMtissuedspd(mTIssuedSpd != null && !"".equals(mTIssuedSpd) ? Integer.valueOf(mTIssuedSpd.trim()) : 0);
					// MO接收总量
					String moTotalRecv = infoEle.attributeValue("MoTotalRecv");
					dspacinfo.setMototalrecv(moTotalRecv != null && !"".equals(moTotalRecv) ? Integer.valueOf(moTotalRecv.trim()) : 0);
					// MO转发量
					String moHaveSnd = infoEle.attributeValue("MoHaveSnd");
					dspacinfo.setMohavesnd(moHaveSnd != null && !"".equals(moHaveSnd) ? Integer.valueOf(moHaveSnd.trim()) : 0);
					// MO滞留量
					String moRemained = infoEle.attributeValue("MoRemained");
					dspacinfo.setMoremained(moRemained != null && !"".equals(moRemained) ? Integer.valueOf(moRemained.trim()) : 0);
					// MO转发速度
					String moSndSpd = infoEle.attributeValue("MoSndSpd");
					dspacinfo.setMosndspd(moSndSpd != null && !"".equals(moSndSpd) ? Integer.valueOf(moSndSpd.trim()) : 0);
					// Rpt接收总量
					String rptTotalRecv = infoEle.attributeValue("RptTotalRecv");
					dspacinfo.setRpttotalrecv(rptTotalRecv != null && !"".equals(rptTotalRecv) ? Integer.valueOf(rptTotalRecv.trim()) : 0);
					// Rpt转发量
					String rptHaveSnd = infoEle.attributeValue("RptHaveSnd");
					dspacinfo.setRptHaveSnd(rptHaveSnd != null && !"".equals(rptHaveSnd) ? Integer.valueOf(rptHaveSnd.trim()) : 0);
					// Rpt滞留量
					String rptRemained = infoEle.attributeValue("RptRemained");
					dspacinfo.setRptremained(rptRemained != null && !"".equals(rptRemained) ? Integer.valueOf(rptRemained.trim()) : 0);
					// RPT接收速度
					String rptSndSpd = infoEle.attributeValue("RptSndSpd");
					dspacinfo.setRptsndspd(rptSndSpd != null && !"".equals(rptSndSpd) ? Integer.valueOf(rptSndSpd.trim()) : 0);
					// 最后一次登录时间
					String loginInTm = infoEle.attributeValue("LoginInTm");
					dspacinfo.setLoginintm(timeChange(loginInTm.trim()));
					// 最后一次离线时间
					String loginOutTm = infoEle.attributeValue("LoginOutTm");
					dspacinfo.setLoginouttm(timeChange(loginOutTm.trim()));
					// 从缓存获取SP账号相关信息
					if(MonitorStaticValue.getSPACCOUTN_INFO().containsKey(spAccountId))
					{
						spAccountInfo = MonitorStaticValue.getSPACCOUTN_INFO().get(spAccountId);
					}
					// SP账号名称
					dspacinfo.setAccountName(spAccountInfo[0]);
					// SP账号类型
					dspacinfo.setSpAccountType(Integer.valueOf(spAccountInfo[1]));
					// 发送级别
					dspacinfo.setSendlevel(Integer.valueOf(spAccountInfo[2]));
					// 更新时间
					dspacinfo.setUpdatetime(messageTime);
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "解析SP账号监控消息参数格式异常！SP账号: " + spAccountId + "，monitorMsg:" + monitorMsgUtf8);
					continue;
				}
                try {
                    dspacinfo.setServerNum("0");
                    saveOrUpdate(dspacinfo,"spaccountid");
                } catch (Exception e) {
                    EmpExecutionContext.error(e,"处理sp账号监控缓存信息入库异常 ，monitorMsg:" + monitorMsgUtf8);
                }
                // 缓存无对应SP账号基础信息，向表中插入SP账号基础信息初始化数据并写入缓存
				Map<String, LfMonSspacinfo> spAccountBaseMap = MonitorStaticValue.getSpAccountBaseMap();
				if(spAccountBaseMap == null || spAccountBaseMap.size() < 1)
				{
					spAccountBaseMap = MonitorStaticValue.getSpAccountBaseMapTemp();
				}
				if(!spAccountBaseMap.containsKey(spAccountId))
				{
					LfMonSspacinfo lfMonSspacinfo = new LfMonSspacinfo();
					// 主机编号
					lfMonSspacinfo.setHostid(hostId);
					// SP账号
					lfMonSspacinfo.setSpaccountid(spAccountId);
					// SP账号名称
					lfMonSspacinfo.setAccountname(spAccountInfo[0]);
					// SP账号类型
					lfMonSspacinfo.setSpaccounttype(Integer.valueOf(spAccountInfo[1]));
					// 发送级别
					lfMonSspacinfo.setSendlevel(Integer.valueOf(spAccountInfo[2]));
					// 更新时间
					lfMonSspacinfo.setModifytime(new Timestamp(System.currentTimeMillis()));
					// 创建时间
					lfMonSspacinfo.setCreatetime(new Timestamp(System.currentTimeMillis()));
                    // 登录类型
                    lfMonSspacinfo.setLoginType(Integer.parseInt(loginType));
                    // 创建SP账号配置信息成功
					addSpAccoutCfgInfo(lfMonSspacinfo);
				}
				else
				{
					// 缓存获取基础信息
					LfMonSspacinfo lfMonSspacinfo = spAccountBaseMap.get(spAccountId);

                    // 基础信息表中的名称、类型、发送级别、登录类型和相关信息缓存不一致，更新基础信息缓存及表数据
					if(!loginType.equals(lfMonSspacinfo.getLoginType().toString()) || !spAccountInfo[0].equals(lfMonSspacinfo.getAccountname()) || !(Integer.valueOf(spAccountInfo[1])).equals(lfMonSspacinfo.getSpaccounttype()) || !(Integer.valueOf(spAccountInfo[2])).equals(lfMonSspacinfo.getSendlevel()))
					{
						try
						{
							// 更新数据库
							LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
                            objectMap.put("loginType", loginType);
							objectMap.put("accountname", spAccountInfo[0]);
							objectMap.put("spaccounttype", spAccountInfo[1]);
							objectMap.put("sendlevel", spAccountInfo[2]);
							LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
							conditionMap.put("spaccountid", spAccountId);
							baseBiz.update(getConnection(), LfMonSspacinfo.class, objectMap, conditionMap);
						}
						catch (Exception e)
						{
							EmpExecutionContext.error(e, "更新SP账号基础信息表失败!SP账号:" + spAccountId + "，monitorMsg:" + monitorMsgUtf8);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "解析SP账号监控信息异常，SP账号: " + spAccountId + "，monitorMsg:" + monitorMsgUtf8);
		}
	}
	
	/**
	 * 解析SP账号信息
	 *
	 * @description
	 * @param iteratorInfo
	 * @param messageTime
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-31 下午03:22:41
	 */
	@SuppressWarnings("unchecked")
	private void setSpAccoutInfo(Iterator iteratorInfo, Timestamp messageTime, Long hostId, Long appId)
	{
		LfMonDspacinfo dspacinfo = new LfMonDspacinfo();
        Element infoEle = null;
		// 用户帐号
		String spAccountId = "";
		// SP账号相关信息
		String[] spAccountInfo = {"-","1","1"};
		// 登录类型
		String loginType = "0";
		// 遍历INFO节点
		try
		{
			while(iteratorInfo.hasNext())
			{
				infoEle = (Element) iteratorInfo.next();
				// 用户帐号
				spAccountId = infoEle.attributeValue("SpAccountId");
				if("".equals(spAccountId) || spAccountId == null)
				{
					EmpExecutionContext.error("解析监控消息SP账号信息失败，spAccountId: " + spAccountId + "，monitorMsg:" + monitorMsgUtf8);
					continue;
				}
				if(!MonitorStaticValue.getSPACCOUTN_INFO().containsKey(spAccountId))
				{
					EmpExecutionContext.error("解析监控消息SP账号信息异常，用户数据表中无 " + spAccountId + "相关数据。");
					continue;
				}

                // 主机编号
                dspacinfo.setHostId(hostId);


				try
				{
					// SP账号
					dspacinfo.setSpaccountid(spAccountId.trim());
					// 帐号连接数
					String linkNum = infoEle.attributeValue("LinkNum");
					dspacinfo.setLinknum(linkNum != null && !"".equals(linkNum) ? Integer.valueOf(linkNum.trim()) : 0);
					// 帐号登录IP
					String loginIp = infoEle.attributeValue("LoginIp");
					if(loginIp.trim().indexOf(":") != -1)
					{
						String ip = loginIp.trim().split(":")[0];
						if("".equals(ip) || ip == null)
						{
							dspacinfo.setLoginip("0.0.0.0");
						}
						else
						{
							dspacinfo.setLoginip(ip);
						}
					}
					else
					{
						dspacinfo.setLoginip("0.0.0.0");
					}
					// 在线状态
					String onlineStatus = infoEle.attributeValue("OnlineStatus");
					dspacinfo.setOnlinestatus(onlineStatus != null && !"".equals(onlineStatus) ? Integer.valueOf(onlineStatus.trim()) : 0);
					// MT提交总量
					String mtTotalSnd = infoEle.attributeValue("MtTotalSnd");
					dspacinfo.setMtTotalSnd(mtTotalSnd != null && !"".equals(mtTotalSnd) ? Integer.valueOf(mtTotalSnd.trim()) : 0);
					// MT已转发量
					String mtHaveSnd = infoEle.attributeValue("MtHaveSnd");
					dspacinfo.setMthavesnd(mtHaveSnd != null && !"".equals(mtHaveSnd) ? Integer.valueOf(mtHaveSnd.trim()) : 0);
                    // 登录类型
                    loginType = infoEle.attributeValue("LoginType");
                    if(loginType == null || "".equals(loginType))
                    {
                        loginType = "0";
                    }
                    dspacinfo.setLoginType(Integer.valueOf(loginType.trim()));

                    Map<String, MonDspAccountParams> spAccountMap = MonitorStaticValue.getSpAccountMap();
                    if(spAccountMap == null || spAccountMap.size() < 1)
                    {
                    	spAccountMap = MonitorStaticValue.getSpAccountMapTemp();
                    }
					MonDspAccountParams monDspAcParams = spAccountMap.get(spAccountId);
                    // 登录类型为直连账号
                    if("2".equals(loginType))
                    {
                        // 在线状态为离线
                        if("1".equals(onlineStatus))
                        {
                            //如果未记录离线时间
                            if(monDspAcParams != null && (monDspAcParams.getOfflineDuration() == null || monDspAcParams.getOfflineDuration() == 0))
                            {
                            	Timestamp dbServerTime = monitorBaseInfoBiz.getDbServerTime(getConnection());
                                // 记录离线时间
                                dspacinfo.setOfflineDuration(dbServerTime.getTime());
                            }
                        }
                        else
                        {
                            // 设置为0
                            dspacinfo.setOfflineDuration(0L);
                        }
                    }
                    if(monDspAcParams != null)
                    {
                    	// 已提交总量为0，或和上次的数据相等
                    	boolean isNoMtHaveSndState = isNoMtHaveSndState(dspacinfo.getMtTotalSnd(), monDspAcParams.getSpNodeMthavesndMap(), spAccountId+appId);
                    	//未提交
                    	if(isNoMtHaveSndState)
                    	{
                    		//如果未记录未提交数据时间
                    		if(monDspAcParams.getNoMtHaveSnd() == null || monDspAcParams.getNoMtHaveSnd() == 0)
                    		{
                    			Timestamp dbServerTime = monitorBaseInfoBiz.getDbServerTime(getConnection());
                    			// 设置未提交数据时间
                    			dspacinfo.setNoMtHaveSnd(dbServerTime.getTime());
                    		}
                    	}
                    	else
                    	{
                    		// 设置为0
                            dspacinfo.setNoMtHaveSnd(0L);
                    	}
                    }
                    else
                    {
                        // 设置为0
                        dspacinfo.setNoMtHaveSnd(0L);
                    }
                    // MT滞留量
					String mtRemained = infoEle.attributeValue("MtRemained");
					dspacinfo.setMtremained(mtRemained != null && !"".equals(mtRemained) ? Integer.valueOf(mtRemained.trim()) : 0);
					// MT提交速度
					String mtSndSpd = infoEle.attributeValue("MtSndSpd");
					dspacinfo.setMtsndspd(mtSndSpd != null && !"".equals(mtSndSpd) ? Integer.valueOf(mtSndSpd.trim()) : 0);
					// MT下发速度
					String mTIssuedSpd = infoEle.attributeValue("MTIssuedSpd");
					dspacinfo.setMtissuedspd(mTIssuedSpd != null && !"".equals(mTIssuedSpd) ? Integer.valueOf(mTIssuedSpd.trim()) : 0);
					// MO接收总量
					String moTotalRecv = infoEle.attributeValue("MoTotalRecv");
					dspacinfo.setMototalrecv(moTotalRecv != null && !"".equals(moTotalRecv) ? Integer.valueOf(moTotalRecv.trim()) : 0);
					// MO转发量
					String moHaveSnd = infoEle.attributeValue("MoHaveSnd");
					dspacinfo.setMohavesnd(moHaveSnd != null && !"".equals(moHaveSnd) ? Integer.valueOf(moHaveSnd.trim()) : 0);
					// MO滞留量
					String moRemained = infoEle.attributeValue("MoRemained");
					dspacinfo.setMoremained(moRemained != null && !"".equals(moRemained) ? Integer.valueOf(moRemained.trim()) : 0);
					// MO转发速度
					String moSndSpd = infoEle.attributeValue("MoSndSpd");
					dspacinfo.setMosndspd(moSndSpd != null && !"".equals(moSndSpd) ? Integer.valueOf(moSndSpd.trim()) : 0);
					// Rpt接收总量
					String rptTotalRecv = infoEle.attributeValue("RptTotalRecv");
					dspacinfo.setRpttotalrecv(rptTotalRecv != null && !"".equals(rptTotalRecv) ? Integer.valueOf(rptTotalRecv.trim()) : 0);
					// Rpt转发量
					String rptHaveSnd = infoEle.attributeValue("RptHaveSnd");
					dspacinfo.setRptHaveSnd(rptHaveSnd != null && !"".equals(rptHaveSnd) ? Integer.valueOf(rptHaveSnd.trim()) : 0);
					// Rpt滞留量
					String rptRemained = infoEle.attributeValue("RptRemained");
					dspacinfo.setRptremained(rptRemained != null && !"".equals(rptRemained) ? Integer.valueOf(rptRemained.trim()) : 0);
					// RPT接收速度
					String rptSndSpd = infoEle.attributeValue("RptSndSpd");
					dspacinfo.setRptsndspd(rptSndSpd != null && !"".equals(rptSndSpd) ? Integer.valueOf(rptSndSpd.trim()) : 0);
					// 最后一次登录时间
					String loginInTm = infoEle.attributeValue("LoginInTm");
					dspacinfo.setLoginintm(timeChange(loginInTm.trim()));
					// 最后一次离线时间
					String loginOutTm = infoEle.attributeValue("LoginOutTm");
					dspacinfo.setLoginouttm(timeChange(loginOutTm.trim()));
					// 从缓存获取SP账号相关信息
					if(MonitorStaticValue.getSPACCOUTN_INFO().containsKey(spAccountId))
					{
						spAccountInfo = MonitorStaticValue.getSPACCOUTN_INFO().get(spAccountId);
					}
					// SP账号名称
					dspacinfo.setAccountName(spAccountInfo[0]);
					// SP账号类型
					dspacinfo.setSpAccountType(Integer.valueOf(spAccountInfo[1]));
					// 发送级别
					dspacinfo.setSendlevel(Integer.valueOf(spAccountInfo[2]));
					// 更新时间
					dspacinfo.setUpdatetime(messageTime);
					//网关编号
					dspacinfo.setGatewayid(appId);
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "解析SP账号监控消息参数格式异常！SP账号: " + spAccountId + "，monitorMsg:" + monitorMsgUtf8);
					continue;
				}
                try {
                    dspacinfo.setServerNum(StaticValue.getServerNumber());
                    Map<String, String> kyeMap = new HashMap<String, String>();
                    kyeMap.put("SPACCOUNTID", "");
                    kyeMap.put("GATEWAYID", "");
                    saveOrUpdateMultiKey(dspacinfo,kyeMap);
                } catch (Exception e) {
                    EmpExecutionContext.error(e,"处理sp账号监控缓存信息入库异常 ，monitorMsg:" + monitorMsgUtf8);
                }
                // 缓存无对应SP账号基础信息，向表中插入SP账号基础信息初始化数据并写入缓存
				Map<String, LfMonSspacinfo> spAccountBaseMap = MonitorStaticValue.getSpAccountBaseMap();
				if(spAccountBaseMap == null || spAccountBaseMap.size() < 1)
				{
					spAccountBaseMap = MonitorStaticValue.getSpAccountBaseMapTemp();
				}
				if(!spAccountBaseMap.containsKey(spAccountId))
				{
					LfMonSspacinfo lfMonSspacinfo = new LfMonSspacinfo();
					// 主机编号
					lfMonSspacinfo.setHostid(hostId);
					// SP账号
					lfMonSspacinfo.setSpaccountid(spAccountId);
					// SP账号名称
					lfMonSspacinfo.setAccountname(spAccountInfo[0]);
					// SP账号类型
					lfMonSspacinfo.setSpaccounttype(Integer.valueOf(spAccountInfo[1]));
					// 发送级别
					lfMonSspacinfo.setSendlevel(Integer.valueOf(spAccountInfo[2]));
					// 更新时间
					lfMonSspacinfo.setModifytime(new Timestamp(System.currentTimeMillis()));
					// 创建时间
					lfMonSspacinfo.setCreatetime(new Timestamp(System.currentTimeMillis()));
                    // 登录类型
                    lfMonSspacinfo.setLoginType(Integer.parseInt(loginType));
                    // 创建SP账号配置信息成功
					addSpAccoutCfgInfo(lfMonSspacinfo);
				}
				else
				{
					// 缓存获取基础信息
					LfMonSspacinfo lfMonSspacinfo = spAccountBaseMap.get(spAccountId);

                    // 基础信息表中的名称、类型、发送级别、登录类型和相关信息缓存不一致，更新基础信息缓存及表数据
					if(!loginType.equals(lfMonSspacinfo.getLoginType().toString()) || !spAccountInfo[0].equals(lfMonSspacinfo.getAccountname()) || !(Integer.valueOf(spAccountInfo[1])).equals(lfMonSspacinfo.getSpaccounttype()) || !(Integer.valueOf(spAccountInfo[2])).equals(lfMonSspacinfo.getSendlevel()))
					{
						try
						{
							// 更新数据库
							LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
                            objectMap.put("loginType", loginType);
							objectMap.put("accountname", spAccountInfo[0]);
							objectMap.put("spaccounttype", spAccountInfo[1]);
							objectMap.put("sendlevel", spAccountInfo[2]);
							LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
							conditionMap.put("spaccountid", spAccountId);
							baseBiz.update(getConnection(), LfMonSspacinfo.class, objectMap, conditionMap);
						}
						catch (Exception e)
						{
							EmpExecutionContext.error(e, "更新SP账号基础信息表失败!SP账号:" + spAccountId + "，monitorMsg:" + monitorMsgUtf8);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "解析SP账号监控信息异常，SP账号: " + spAccountId + "，monitorMsg:" + monitorMsgUtf8);
		}
	}

	/**
	 * 解析通道账号信息
	 *
	 * @description
	 * @param iteratorInfo
	 * @param messageTime
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-31 下午03:22:20
	 */
	@SuppressWarnings("unchecked")
	private void setGateAccoutInfo(Iterator iteratorInfo, Timestamp messageTime)
	{
        LfMonDgtacinfo dgtacinfo = new LfMonDgtacinfo();
		Element infoEle = null;
		// 通道账号
		String gateAccount = "";
		// 通道账号相关信息
		String[] gateAccountInfo = {"-","2"};
		// 账号状态
		int status = -1;
		// 网关编号
		Long gatewayId = -1L;
		try
		{
			// 遍历INFO节点
			while(iteratorInfo.hasNext())
			{
				infoEle = (Element) iteratorInfo.next();
				// 通道账号
				gateAccount = infoEle.attributeValue("GateAccount");
				if("".equals(gateAccount) || gateAccount == null)
				{
					EmpExecutionContext.error("解析监控消息通道账号信息失败，gateAccount: " + gateAccount + "，monitorMsg:" + monitorMsgUtf8);
					continue;
				}
				if(!MonitorStaticValue.getGATEACCOUTN_INFO().containsKey(gateAccount))
				{
					EmpExecutionContext.error("解析监控消息通道账号信息异常，用户数据表中无 " + gateAccount + "相关数据。");
					continue;
				}


                // 获取网关编号
                gatewayId = new MonitorDAO().getGatewayId(gateAccount);
                // 网关编号
                dgtacinfo.setGatewayid(gatewayId);

				Map<String, LfMonSgtacinfo> gateAccountBaseMap = MonitorStaticValue.getGateAccountBaseMap();
				if(gateAccountBaseMap == null || gateAccountBaseMap.size() < 1)
				{
					gateAccountBaseMap = MonitorStaticValue.getGateAccountBaseMapTemp();
				}
				try
				{
					// 通道账号
					dgtacinfo.setGateaccount(gateAccount);
					// 帐号连接数
					String linkNum = infoEle.attributeValue("LinkNum");
					dgtacinfo.setLinknum(linkNum != null && !"".equals(linkNum) ? Integer.valueOf(linkNum.trim()) : 0);
					// 帐号登录IP
					String loginIp = infoEle.attributeValue("LoginIp");
					if(loginIp.trim().indexOf(":") != -1)
					{
						String ip = loginIp.trim().split(":")[0];
						if("".equals(ip) || ip == null)
						{
							dgtacinfo.setLoginip("0.0.0.0");
						}
						else
						{
							dgtacinfo.setLoginip(ip);
						}
					}
					else
					{
						dgtacinfo.setLoginip("0.0.0.0");
					}
					// 在线状态
					String onlineStatus = infoEle.attributeValue("OnlineStatus");
					if(onlineStatus != null && !"".equals(onlineStatus))
					{
						if("1".equals(onlineStatus))
						{
                            dgtacinfo.setOnlinestatus(1);
							// 告警别级为2:严重
//							dgtacinfo.setEvtType(2);
						}
						else if(status == 2)
						{
							dgtacinfo.setOnlinestatus(status);
						}
						else
						{
							dgtacinfo.setOnlinestatus(0);
						}
					}
					else
					{
						dgtacinfo.setOnlinestatus(1);
						// 告警别级为2:严重
//						dgtacinfo.setEvtType(2);
					}
					// MT提交总量
					String mtTotalSnd = infoEle.attributeValue("MtTotalSnd");
					dgtacinfo.setMttotalsnd(mtTotalSnd != null && !"".equals(mtTotalSnd) ? Integer.valueOf(mtTotalSnd.trim()) : 0);
					// MT已转发量
					String mtHaveSnd = infoEle.attributeValue("MtHaveSnd");
					dgtacinfo.setMthavesnd(mtHaveSnd != null && !"".equals(mtHaveSnd) ? Integer.valueOf(mtHaveSnd.trim()) : 0);
					// MT滞留量
					String mtRemained = infoEle.attributeValue("MtRemained");
					dgtacinfo.setMtremained(mtRemained != null && !"".equals(mtRemained) ? Integer.valueOf(mtRemained.trim()) : 0);
					// MT接收速度
					String mtRecvSpd = infoEle.attributeValue("MtRecvSpd");
					dgtacinfo.setMtrecvspd(mtRecvSpd != null && !"".equals(mtRecvSpd) ? Integer.valueOf(mtRecvSpd.trim()) : 0);
					// MO接收总量
					String moTotalRecv = infoEle.attributeValue("MoTotalRecv");
					dgtacinfo.setMototalrecv(moTotalRecv != null && !"".equals(moTotalRecv) ? Integer.valueOf(moTotalRecv.trim()) : 0);
					// MO转发量
					String moHaveSnd = infoEle.attributeValue("MoHaveSnd");
					dgtacinfo.setMohavesnd(moHaveSnd != null && !"".equals(moHaveSnd) ? Integer.valueOf(moHaveSnd.trim()) : 0);
					// MO滞留量
					String moRemained = infoEle.attributeValue("MoRemained");
					dgtacinfo.setMoremained(moRemained != null && !"".equals(moRemained) ? Integer.valueOf(moRemained.trim()) : 0);
					// MO转发速度
					String moSndSpd = infoEle.attributeValue("MoSndSpd");
					dgtacinfo.setMosndspd(moSndSpd != null && !"".equals(moSndSpd) ? Integer.valueOf(moSndSpd.trim()) : 0);
					// Rpt转发量
					String rptHaveSnd = infoEle.attributeValue("RptHaveSnd");
					dgtacinfo.setRpthavesnd(rptHaveSnd != null && !"".equals(rptHaveSnd) ? Integer.valueOf(rptHaveSnd.trim()) : 0);
					// Rpt滞留量
					String rptRemained = infoEle.attributeValue("RptRemained");
					dgtacinfo.setRptremained(rptRemained != null && !"".equals(rptRemained) ? Integer.valueOf(rptRemained.trim()) : 0);
					// RPT转发速度
					String rptSndSpd = infoEle.attributeValue("RptSndSpd");
					dgtacinfo.setRptsndspd(rptSndSpd != null && !"".equals(rptSndSpd) ? Integer.valueOf(rptSndSpd.trim()) : 0);
					// Rpt接收总量
					String rptTotalRecv = infoEle.attributeValue("RptTotalRecv");
					dgtacinfo.setRpttotalrecv(rptTotalRecv != null && !"".equals(rptTotalRecv) ? Integer.valueOf(rptTotalRecv.trim()) : 0);
					// 最后一次登录时间
					String loginInTm = infoEle.attributeValue("LoginInTm");
					dgtacinfo.setLoginintm(timeChange(loginInTm.trim()));
					// 最后一次离线时间
					String loginOutTm = infoEle.attributeValue("LoginOutTm");
					dgtacinfo.setLoginouttm(timeChange(loginOutTm.trim()));

					// 从缓存获取通道账号相关信息
					if(MonitorStaticValue.getGATEACCOUTN_INFO().containsKey(gateAccount))
					{
						gateAccountInfo = MonitorStaticValue.getGATEACCOUTN_INFO().get(gateAccount);
					}
					// 通道名称
					if("END000".equals(gateAccount))
					{
						dgtacinfo.setGateName("压力测试");
					}
					else
					{
						dgtacinfo.setGateName(gateAccountInfo[0]);
					}
					// 付费类型
					dgtacinfo.setFeeflag(Integer.valueOf(gateAccountInfo[1]));
					// 帐号费用
					if(MonitorStaticValue.getGateAccoutFee().containsKey(gateAccount))
					{
						dgtacinfo.setUserfee(MonitorStaticValue.getGateAccoutFee().get(gateAccount));
					}
					else
					{
						dgtacinfo.setUserfee(0);
					}
                    //设置关联主机
                    if(gateAccountBaseMap.containsKey(gateAccount))
                    {
                        dgtacinfo.setHostId(gateAccountBaseMap.get(gateAccount).getHostid());
                    }else
                    {
                        dgtacinfo.setHostId(-1L);
                    }
					// 更新时间
					dgtacinfo.setModifytime(messageTime);
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "解析通道账号监控消息参数格式异常！通道账号: " + gateAccount + "，monitorMsg:" + monitorMsgUtf8);
					continue;
				}


                try {
                    dgtacinfo.setServerNum(StaticValue.getServerNumber());
                    Map<String, String> kyeMap = new HashMap<String, String>();
                    kyeMap.put("GATEACCOUNT", "");
                    kyeMap.put("GATEWAYID", "");
                    saveOrUpdateMultiKey(dgtacinfo,kyeMap);
                } catch (Exception e) {
                    EmpExecutionContext.error(e,"处理通道账号监控缓存信息入库异常 ，monitorMsg:" + monitorMsgUtf8);
                }

				// 缓存无对应通道账号基础信息，向表中插入通道账号基础信息初始化数据并写入缓存
				if(!gateAccountBaseMap.containsKey(gateAccount))
				{
					LfMonSgtacinfo lfMonSgtacinfo = new LfMonSgtacinfo();
					// 主机编号
					lfMonSgtacinfo.setHostid(-1L);
					// 通道账号
					lfMonSgtacinfo.setGateaccount(gateAccount);
					// 通道账号名称
					if("END000".equals(gateAccount))
					{
						lfMonSgtacinfo.setGatename("压力测试");
					}
					else
					{
						lfMonSgtacinfo.setGatename(gateAccountInfo[0]);
					}
					// 网关编号
					lfMonSgtacinfo.setGatewayid(gatewayId);
					// 更新时间
					lfMonSgtacinfo.setModifytime(new Timestamp(System.currentTimeMillis()));
					// 创建时间
					lfMonSgtacinfo.setCreatetime(new Timestamp(System.currentTimeMillis()));
					// 创建通道账号配置信息成功,写入缓存
					addGateAccoutCfgInfo(lfMonSgtacinfo);
				}
				else
				{
					if(!"END000".equals(gateAccount))
					{
						// 缓存获取基础信息
						LfMonSgtacinfo lfMonSgtacinfo = gateAccountBaseMap.get(gateAccount);
						// 基础信息表中的名称和相关信息缓存不一致，更新基础信息缓存及表数据
						if(!gateAccountInfo[0].equals(lfMonSgtacinfo.getGatename()))
						{
							try
							{
								// 更新数据库
								LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
								objectMap.put("gatename", gateAccountInfo[0]);
								LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
								conditionMap.put("gateaccount", gateAccount);
								baseBiz.update(getConnection(), LfMonSgtacinfo.class, objectMap, conditionMap);
							}
							catch (Exception e)
							{
								EmpExecutionContext.error(e, "更新通道账号基础信息表失败!通道账号:" + gateAccount + "，monitorMsg:" + monitorMsgUtf8);
							}

						}
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "解析通道账号监控消息参数格式异常！通道账号: " + gateAccount + "，monitorMsg:" + monitorMsgUtf8);
		}
	}
	
	/**
	 * 解析通道账号信息
	 *
	 * @description
	 * @param iteratorInfo
	 * @param messageTime
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-31 下午03:22:20
	 */
	@SuppressWarnings("unchecked")
	private void setGateAccoutInfo(Iterator iteratorInfo, Timestamp messageTime, Long appId)
	{
        LfMonDgtacinfo dgtacinfo = new LfMonDgtacinfo();
		Element infoEle = null;
		// 通道账号
		String gateAccount = "";
		// 通道账号相关信息
		String[] gateAccountInfo = {"-","2"};
		// 账号状态
		int status = -1;
		// 网关编号
//		Long gatewayId = -1L;
		try
		{
			// 遍历INFO节点
			while(iteratorInfo.hasNext())
			{
				infoEle = (Element) iteratorInfo.next();
				// 通道账号
				gateAccount = infoEle.attributeValue("GateAccount");
				if("".equals(gateAccount) || gateAccount == null)
				{
					EmpExecutionContext.error("解析监控消息通道账号信息失败，gateAccount: " + gateAccount + "，monitorMsg:" + monitorMsgUtf8);
					continue;
				}
				if(!MonitorStaticValue.getGATEACCOUTN_INFO().containsKey(gateAccount))
				{
					EmpExecutionContext.error("解析监控消息通道账号信息异常，用户数据表中无 " + gateAccount + "相关数据。");
					continue;
				}

                // 网关编号
                dgtacinfo.setGatewayid(appId);
                dgtacinfo.setGwno(appId);

				Map<String, LfMonSgtacinfo> gateAccountBaseMap = MonitorStaticValue.getGateAccountBaseMap();
				if(gateAccountBaseMap == null || gateAccountBaseMap.size() < 1)
				{
					gateAccountBaseMap = MonitorStaticValue.getGateAccountBaseMapTemp();
				}
				try
				{
					// 通道账号
					dgtacinfo.setGateaccount(gateAccount);
					// 帐号连接数
					String linkNum = infoEle.attributeValue("LinkNum");
					dgtacinfo.setLinknum(linkNum != null && !"".equals(linkNum) ? Integer.valueOf(linkNum.trim()) : 0);
					// 帐号登录IP
					String loginIp = infoEle.attributeValue("LoginIp");
					if(loginIp.trim().indexOf(":") != -1)
					{
						String ip = loginIp.trim().split(":")[0];
						if("".equals(ip) || ip == null)
						{
							dgtacinfo.setLoginip("0.0.0.0");
						}
						else
						{
							dgtacinfo.setLoginip(ip);
						}
					}
					else
					{
						dgtacinfo.setLoginip("0.0.0.0");
					}
					// 在线状态
					String onlineStatus = infoEle.attributeValue("OnlineStatus");
					if(onlineStatus != null && !"".equals(onlineStatus))
					{
						if("1".equals(onlineStatus))
						{
                            dgtacinfo.setOnlinestatus(1);
							// 告警别级为2:严重
//							dgtacinfo.setEvtType(2);
						}
						else if(status == 2)
						{
							dgtacinfo.setOnlinestatus(status);
						}
						else
						{
							dgtacinfo.setOnlinestatus(0);
						}
					}
					else
					{
						dgtacinfo.setOnlinestatus(1);
						// 告警别级为2:严重
//						dgtacinfo.setEvtType(2);
					}
					// MT提交总量
					String mtTotalSnd = infoEle.attributeValue("MtTotalSnd");
					dgtacinfo.setMttotalsnd(mtTotalSnd != null && !"".equals(mtTotalSnd) ? Integer.valueOf(mtTotalSnd.trim()) : 0);
					// MT已转发量
					String mtHaveSnd = infoEle.attributeValue("MtHaveSnd");
					dgtacinfo.setMthavesnd(mtHaveSnd != null && !"".equals(mtHaveSnd) ? Integer.valueOf(mtHaveSnd.trim()) : 0);
					// MT滞留量
					String mtRemained = infoEle.attributeValue("MtRemained");
					dgtacinfo.setMtremained(mtRemained != null && !"".equals(mtRemained) ? Integer.valueOf(mtRemained.trim()) : 0);
					// MT接收速度
					String mtRecvSpd = infoEle.attributeValue("MtRecvSpd");
					dgtacinfo.setMtrecvspd(mtRecvSpd != null && !"".equals(mtRecvSpd) ? Integer.valueOf(mtRecvSpd.trim()) : 0);
					// MO接收总量
					String moTotalRecv = infoEle.attributeValue("MoTotalRecv");
					dgtacinfo.setMototalrecv(moTotalRecv != null && !"".equals(moTotalRecv) ? Integer.valueOf(moTotalRecv.trim()) : 0);
					// MO转发量
					String moHaveSnd = infoEle.attributeValue("MoHaveSnd");
					dgtacinfo.setMohavesnd(moHaveSnd != null && !"".equals(moHaveSnd) ? Integer.valueOf(moHaveSnd.trim()) : 0);
					// MO滞留量
					String moRemained = infoEle.attributeValue("MoRemained");
					dgtacinfo.setMoremained(moRemained != null && !"".equals(moRemained) ? Integer.valueOf(moRemained.trim()) : 0);
					// MO转发速度
					String moSndSpd = infoEle.attributeValue("MoSndSpd");
					dgtacinfo.setMosndspd(moSndSpd != null && !"".equals(moSndSpd) ? Integer.valueOf(moSndSpd.trim()) : 0);
					// Rpt转发量
					String rptHaveSnd = infoEle.attributeValue("RptHaveSnd");
					dgtacinfo.setRpthavesnd(rptHaveSnd != null && !"".equals(rptHaveSnd) ? Integer.valueOf(rptHaveSnd.trim()) : 0);
					// Rpt滞留量
					String rptRemained = infoEle.attributeValue("RptRemained");
					dgtacinfo.setRptremained(rptRemained != null && !"".equals(rptRemained) ? Integer.valueOf(rptRemained.trim()) : 0);
					// RPT转发速度
					String rptSndSpd = infoEle.attributeValue("RptSndSpd");
					dgtacinfo.setRptsndspd(rptSndSpd != null && !"".equals(rptSndSpd) ? Integer.valueOf(rptSndSpd.trim()) : 0);
					// Rpt接收总量
					String rptTotalRecv = infoEle.attributeValue("RptTotalRecv");
					dgtacinfo.setRpttotalrecv(rptTotalRecv != null && !"".equals(rptTotalRecv) ? Integer.valueOf(rptTotalRecv.trim()) : 0);
					// 最后一次登录时间
					String loginInTm = infoEle.attributeValue("LoginInTm");
					dgtacinfo.setLoginintm(timeChange(loginInTm.trim()));
					// 最后一次离线时间
					String loginOutTm = infoEle.attributeValue("LoginOutTm");
					dgtacinfo.setLoginouttm(timeChange(loginOutTm.trim()));

					// 从缓存获取通道账号相关信息
					if(MonitorStaticValue.getGATEACCOUTN_INFO().containsKey(gateAccount))
					{
						gateAccountInfo = MonitorStaticValue.getGATEACCOUTN_INFO().get(gateAccount);
					}
					// 通道名称
					if("END000".equals(gateAccount))
					{
						dgtacinfo.setGateName("压力测试");
					}
					else
					{
						dgtacinfo.setGateName(gateAccountInfo[0]);
					}
					// 付费类型
					dgtacinfo.setFeeflag(Integer.valueOf(gateAccountInfo[1]));
					// 帐号费用
					if(MonitorStaticValue.getGateAccoutFee().containsKey(gateAccount))
					{
						dgtacinfo.setUserfee(MonitorStaticValue.getGateAccoutFee().get(gateAccount));
					}
					else
					{
						dgtacinfo.setUserfee(0);
					}
                    //设置关联主机
                    if(gateAccountBaseMap.containsKey(gateAccount))
                    {
                        dgtacinfo.setHostId(gateAccountBaseMap.get(gateAccount).getHostid());
                    }else
                    {
                        dgtacinfo.setHostId(-1L);
                    }
					// 更新时间
					dgtacinfo.setModifytime(messageTime);
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "解析通道账号监控消息参数格式异常！通道账号: " + gateAccount + "，monitorMsg:" + monitorMsgUtf8);
					continue;
				}


                try {
                    dgtacinfo.setServerNum(StaticValue.getServerNumber());
                    Map<String, String> kyeMap = new HashMap<String, String>();
                    kyeMap.put("GATEACCOUNT", "");
                    kyeMap.put("GWNO", "");
                    saveOrUpdateMultiKey(dgtacinfo,kyeMap);
                } catch (Exception e) {
                    EmpExecutionContext.error(e,"处理通道账号监控缓存信息入库异常 ，monitorMsg:" + monitorMsgUtf8);
                }

				// 缓存无对应通道账号基础信息，向表中插入通道账号基础信息初始化数据并写入缓存
				if(!gateAccountBaseMap.containsKey(gateAccount))
				{
					LfMonSgtacinfo lfMonSgtacinfo = new LfMonSgtacinfo();
					// 主机编号
					lfMonSgtacinfo.setHostid(-1L);
					// 通道账号
					lfMonSgtacinfo.setGateaccount(gateAccount);
					// 通道账号名称
					if("END000".equals(gateAccount))
					{
						lfMonSgtacinfo.setGatename("压力测试");
					}
					else
					{
						lfMonSgtacinfo.setGatename(gateAccountInfo[0]);
					}
					// 网关编号
					lfMonSgtacinfo.setGatewayid(appId);
					// 更新时间
					lfMonSgtacinfo.setModifytime(new Timestamp(System.currentTimeMillis()));
					// 创建时间
					lfMonSgtacinfo.setCreatetime(new Timestamp(System.currentTimeMillis()));
					// 创建通道账号配置信息成功,写入缓存
					addGateAccoutCfgInfo(lfMonSgtacinfo);
				}
				else
				{
					if(!"END000".equals(gateAccount))
					{
						// 缓存获取基础信息
						LfMonSgtacinfo lfMonSgtacinfo = gateAccountBaseMap.get(gateAccount);
						// 基础信息表中的名称和相关信息缓存不一致，更新基础信息缓存及表数据
						if(!gateAccountInfo[0].equals(lfMonSgtacinfo.getGatename()))
						{
							try
							{
								// 更新数据库
								LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
								objectMap.put("gatename", gateAccountInfo[0]);
								LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
								conditionMap.put("gateaccount", gateAccount);
								baseBiz.update(getConnection(), LfMonSgtacinfo.class, objectMap, conditionMap);
							}
							catch (Exception e)
							{
								EmpExecutionContext.error(e, "更新通道账号基础信息表失败!通道账号:" + gateAccount + "，monitorMsg:" + monitorMsgUtf8);
							}

						}
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "解析通道账号监控消息参数格式异常！通道账号: " + gateAccount + "，monitorMsg:" + monitorMsgUtf8);
		}
	}

	/**
	 * 解析WBS缓冲信息
	 *
	 * @description
	 * @param iteratorInfo
	 * @param messageTime
	 * @param proceId
	 * @param appId
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-6 下午04:58:55
	 */
	@SuppressWarnings("unchecked")
	private void setWbsBufInfo(Iterator iteratorInfo, Timestamp messageTime, Long proceId, Long appId)
	{
		// 从缓存中获取对应的WBS缓冲对象
		MonDwbsBufParams monDwbsBufParams = MonitorStaticValue.getWbsBufInfoMap().get(appId);
		// 缓存存在对应的主机对象,判断消息时间是否大于缓存对象时间
		if(monDwbsBufParams != null)
		{
			if(monDwbsBufParams.getUpdatetime() != null && monDwbsBufParams.getUpdatetime().compareTo(messageTime) >= 0)
			{
				EmpExecutionContext.info("appId:"+appId+"，WBS缓冲信息消息时间小于缓存对象更新时间。缓存时间："+monDwbsBufParams.getUpdatetime()
											+"，消息时间："+messageTime + "，monitorMsg:" + monitorMsgUtf8);
				return;
			}
		}
		else
		{
			monDwbsBufParams = new MonDwbsBufParams();
			// 程序编号
			monDwbsBufParams.setProceid(proceId);
			// 网关编号
			monDwbsBufParams.setGatewayid(appId);
		}
		Element infoEle = null;
		try
		{
			// 遍历INFO节点
			while(iteratorInfo.hasNext())
			{
				infoEle = (Element) iteratorInfo.next();
				// MT滞留量
				String mtNosd = infoEle.attributeValue("MTNOSD");
				monDwbsBufParams.setMtnosd(mtNosd != null && !"".equals(mtNosd) ? Integer.valueOf(mtNosd.trim()) : 0);
				// MO滞留量
				String moNosd = infoEle.attributeValue("MONOSD");
				monDwbsBufParams.setMonosd(moNosd != null && !"".equals(moNosd) ? Integer.valueOf(moNosd.trim()) : 0);
				// RPT滞留量
				String rptNosd = infoEle.attributeValue("RPTNOSD");
				monDwbsBufParams.setRptnosd(rptNosd != null && !"".equals(rptNosd) ? Integer.valueOf(rptNosd.trim()) : 0);
				// 后端连接数
				String endCnt = infoEle.attributeValue("ENDCNT");
				monDwbsBufParams.setMonosd(endCnt != null && !"".equals(endCnt) ? Integer.valueOf(endCnt.trim()) : 0);
				// MO接收总量
				String moRvcnt = infoEle.attributeValue("MORVCNT");
				monDwbsBufParams.setMorvcnt(moRvcnt != null && !"".equals(moRvcnt) ? Integer.valueOf(moRvcnt.trim()) : 0);
				// MT转发总量
				String mtSdcnt = infoEle.attributeValue("MTSDCNT");
				monDwbsBufParams.setMtsdcnt(mtSdcnt != null && !"".equals(mtSdcnt) ? Integer.valueOf(mtSdcnt.trim()) : 0);
				// MO写库缓冲
				String wrMoBuf = infoEle.attributeValue("WRMOBUF");
				monDwbsBufParams.setWrmobuf(wrMoBuf != null && !"".equals(wrMoBuf) ? Integer.valueOf(wrMoBuf.trim()) : 0);
				// MO更新缓冲
				String updMoBuf = infoEle.attributeValue("UPDMOBUF");
				monDwbsBufParams.setUpdmobuf(updMoBuf != null && !"".equals(updMoBuf) ? Integer.valueOf(updMoBuf.trim()) : 0);
				// Rpt写库缓冲
				String wrRptBuf = infoEle.attributeValue("WRRPTBUF");
				monDwbsBufParams.setWrrptbuf(wrRptBuf != null && !"".equals(wrRptBuf) ? Integer.valueOf(wrRptBuf.trim()) : 0);
				// 后端回应缓冲
				String endRspBuf = infoEle.attributeValue("ENDRSPBUF");
				monDwbsBufParams.setEndrspbuf(endRspBuf != null && !"".equals(endRspBuf) ? Integer.valueOf(endRspBuf.trim()) : 0);
				// MT发送队列
				String mtSdBuf = infoEle.attributeValue("MTSDBUF");
				monDwbsBufParams.setMtsdbuf(mtSdBuf != null && !"".equals(mtSdBuf) ? Integer.valueOf(mtSdBuf.trim()) : 0);
				// MT发送等待缓冲
				String mtWaitBuf = infoEle.attributeValue("MTWAITBUF");
				monDwbsBufParams.setMtwaitbuf(mtWaitBuf != null && !"".equals(mtWaitBuf) ? Integer.valueOf(mtWaitBuf.trim()) : 0);
				// 前端连接数
				String preCnt = infoEle.attributeValue("PRECNT");
				monDwbsBufParams.setPrecnt(preCnt != null && !"".equals(preCnt) ? Integer.valueOf(preCnt.trim()) : 0);
				// MT接收总量
				String mtRvCnt = infoEle.attributeValue("MTRVCNT");
				monDwbsBufParams.setMtrvcnt(mtRvCnt != null && !"".equals(mtRvCnt) ? Integer.valueOf(mtRvCnt.trim()) : 0);
				// MO转发总量
				String moSdCnt = infoEle.attributeValue("MOSDCNT");
				monDwbsBufParams.setMosdcnt(moSdCnt != null && !"".equals(moSdCnt) ? Integer.valueOf(moSdCnt.trim()) : 0);
				// 写MTTASK缓冲
				String wrMtBUF = infoEle.attributeValue("WRMTBUF");
				monDwbsBufParams.setWrmtbuf(wrMtBUF != null && !"".equals(wrMtBUF) ? Integer.valueOf(wrMtBUF.trim()) : 0);
				// 写MTVFY缓冲
				String wrMtVfyBuf = infoEle.attributeValue("WRMTVFYBUF");
				monDwbsBufParams.setWrmtvfybuf(wrMtVfyBuf != null && !"".equals(wrMtVfyBuf) ? Integer.valueOf(wrMtVfyBuf.trim()) : 0);
				// 写MTLVL缓冲
				String wrMtLvlBuf = infoEle.attributeValue("WRMTLVLBUF");
				monDwbsBufParams.setWrmtlvlbuf(wrMtLvlBuf != null && !"".equals(wrMtLvlBuf) ? Integer.valueOf(wrMtLvlBuf.trim()) : 0);
				// 前端回应缓冲
				String preRspBuf = infoEle.attributeValue("PRERSPBUF");
				monDwbsBufParams.setPrerspbuf(preRspBuf != null && !"".equals(preRspBuf) ? Integer.valueOf(preRspBuf.trim()) : 0);
				// 前端回应临时缓冲
				String preRspTmpBuf = infoEle.attributeValue("PRERSPTMPBUF");
				monDwbsBufParams.setPrersptmpbuf(preRspTmpBuf != null && !"".equals(preRspTmpBuf) ? Integer.valueOf(preRspTmpBuf.trim()) : 0);
				// MO/Rpt发送缓冲
				String moRptSdBuf = infoEle.attributeValue("MORPTSDBUF");
				monDwbsBufParams.setRptsdbuf(moRptSdBuf != null && !"".equals(moRptSdBuf) ? Integer.valueOf(moRptSdBuf.trim()) : 0);
				// Rpt发送缓冲
				String rptSdBuf = infoEle.attributeValue("RPTSDBUF");
				monDwbsBufParams.setRptsdbuf(rptSdBuf != null && !"".equals(rptSdBuf) ? Integer.valueOf(rptSdBuf.trim()) : 0);
				// MO/RPT发送等待缓冲
				String moRptWaitBuf = infoEle.attributeValue("MORPTWAITBUF");
				monDwbsBufParams.setMorptwaitbuf(moRptWaitBuf != null && !"".equals(moRptWaitBuf) ? Integer.valueOf(moRptWaitBuf.trim()) : 0);
				// 日志文件数量
				String logFileNum = infoEle.attributeValue("LOGFILENUM");
				monDwbsBufParams.setLogfilenum(logFileNum != null && !"".equals(logFileNum) ? Integer.valueOf(logFileNum.trim()) : 0);
				// 日志缓冲
				String logBuf = infoEle.attributeValue("LOGBUF");
				monDwbsBufParams.setLogbuf(logBuf != null && !"".equals(logBuf) ? Integer.valueOf(logBuf.trim()) : 0);
				// 接收缓冲
				String reCvBuf = infoEle.attributeValue("RECVBUF");
				monDwbsBufParams.setRecvbuf(reCvBuf != null && !"".equals(reCvBuf) ? Integer.valueOf(reCvBuf.trim()) : 0);
				// 重发缓冲
				String reSndBuf = infoEle.attributeValue("RESNDBUF");
				monDwbsBufParams.setResndbuf(reSndBuf != null && !"".equals(reSndBuf) ? Integer.valueOf(reSndBuf.trim()) : 0);
				// 补发缓冲
				String suppSdBuf = infoEle.attributeValue("SUPPSDBUF");
				monDwbsBufParams.setSuppsdbuf(suppSdBuf != null && !"".equals(suppSdBuf) ? Integer.valueOf(suppSdBuf.trim()) : 0);
				// MT瞬时接收速度
				String mtRvSpd1 = infoEle.attributeValue("MTRVSPD1");
				monDwbsBufParams.setMtrvspd1(mtRvSpd1 != null && !"".equals(mtRvSpd1) ? Integer.valueOf(mtRvSpd1.trim()) : 0);
				// MT瞬时转发速度
				String mtSdSpd1 = infoEle.attributeValue("MTSDSPD1");
				monDwbsBufParams.setMtsdspd1(mtSdSpd1 != null && !"".equals(mtSdSpd1) ? Integer.valueOf(mtSdSpd1.trim()) : 0);
				// MT一分钟的平均接收速度
				String mtRvSpd2 = infoEle.attributeValue("MTRVSPD2");
				monDwbsBufParams.setMtrvspd2(mtRvSpd2 != null && !"".equals(mtRvSpd2) ? Integer.valueOf(mtRvSpd2.trim()) : 0);
				// MT一分钟的平均转发速度
				String mtSdSpd2 = infoEle.attributeValue("MTSDSPD2");
				monDwbsBufParams.setMtsdspd2(mtSdSpd2 != null && !"".equals(mtSdSpd2) ? Integer.valueOf(mtSdSpd2.trim()) : 0);
				// MO/RPT瞬时接收速度
				String moRptRvSpd1 = infoEle.attributeValue("MORPTRVSPD1");
				monDwbsBufParams.setMorptrvspd1(moRptRvSpd1 != null && !"".equals(moRptRvSpd1) ? Integer.valueOf(moRptRvSpd1.trim()) : 0);
				// MO/RPT瞬时转发速度
				String moRptSdSpd1 = infoEle.attributeValue("MORPTSDSPD1");
				monDwbsBufParams.setMorptsdspd1(moRptSdSpd1 != null && !"".equals(moRptSdSpd1) ? Integer.valueOf(moRptSdSpd1.trim()) : 0);
				// MO/RPT一分钟的平均接收速度
				String moRptRvSpd2 = infoEle.attributeValue("MORPTRVSPD2");
				monDwbsBufParams.setMorptrvspd2(moRptRvSpd2 != null && !"".equals(moRptRvSpd2) ? Integer.valueOf(moRptRvSpd2.trim()) : 0);
				// MO/RPT一分钟的平均转发速度
				String moRptSdSpd2 = infoEle.attributeValue("MORPTSDSPD2");
				monDwbsBufParams.setMorptsdspd2(moRptSdSpd2 != null && !"".equals(moRptSdSpd2) ? Integer.valueOf(moRptSdSpd2.trim()) : 0);
				// 更新时间
				monDwbsBufParams.setUpdatetime(messageTime);
				// 数据写入缓存
				synchronized (MonitorStaticValue.getWbsBufInfoMap())
				{
					MonitorStaticValue.getWbsBufInfoMap().put(appId, monDwbsBufParams);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "解析WBS缓冲监控消息参数格式异常！appId:"+appId + "，monitorMsg:" + monitorMsgUtf8);
		}
	}

	/**
	 * 解析SPGATE缓冲信息
	 *
	 * @description
	 * @param iteratorInfo
	 * @param messageTime
	 * @param proceId
	 * @param appId
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-6 下午05:46:43
	 */
	@SuppressWarnings("unchecked")
	private void setSpGateBufInfo(Iterator iteratorInfo, Timestamp messageTime, Long proceId, Long appId)
	{
        try
        {
        	LfMonDGateBuf dGateBuf = new LfMonDGateBuf();
	        // 程序编号
	        dGateBuf.setProceid(proceId);
	        // 网关编号
	        dGateBuf.setGatewayid(appId);
			Element infoEle = null;
			//通道账号
			String gateAccount = "";
			//通道账号名
			String gateName = "";
			
			//获取通道账户信息
			String[] gateAccountInfo = monitorBaseInfoBiz.getGateAccountInfoByGwno(appId);
			if(gateAccountInfo != null)
			{
				gateAccount = gateAccountInfo[0];
				gateName = gateAccountInfo[1];
				if(gateAccount != null && !"".equals(gateAccount))
				{
					// 遍历INFO节点
					while(iteratorInfo.hasNext())
					{
						infoEle = (Element) iteratorInfo.next();
						// MT接收量
						String mtRvCnt = infoEle.attributeValue("MTRVCNT");
						dGateBuf.setMtrvcnt(mtRvCnt != null && !"".equals(mtRvCnt) ? Integer.valueOf(mtRvCnt.trim()) : 0);
						// MT发送量
						String mtSdCnt = infoEle.attributeValue("MTSDCNT");
						dGateBuf.setMtsdcnt(mtSdCnt != null && !"".equals(mtSdCnt) ? Integer.valueOf(mtSdCnt.trim()) : 0);
						// MT发送缓冲
						String mtSdBuf = infoEle.attributeValue("MTSDBUF");
						dGateBuf.setMtsdbuf(mtSdBuf != null && !"".equals(mtSdBuf) ? Integer.valueOf(mtSdBuf.trim()) : 0);
						// MT数据库更新缓冲
						String mtUpdBuf = infoEle.attributeValue("MTUPDBUF");
						dGateBuf.setMtupdbuf(mtUpdBuf != null && !"".equals(mtUpdBuf) ? Integer.valueOf(mtUpdBuf.trim()) : 0);
						// MT发送等待缓冲
						String mtSdWaitBuf = infoEle.attributeValue("MTSDWAITBUF");
						dGateBuf.setMtsdwaitbuf(mtSdWaitBuf != null && !"".equals(mtSdWaitBuf) ? Integer.valueOf(mtSdWaitBuf.trim()) : 0);
						// MT发送速度(3秒)
						String mtSpd1 = infoEle.attributeValue("MTSPD1");
						dGateBuf.setMtspd1(mtSpd1 != null && !"".equals(mtSpd1) ? Integer.valueOf(mtSpd1.trim()) : 0);
						// MT发送 (1分钟)
						String mtSpd2 = infoEle.attributeValue("MTSPD2");
						dGateBuf.setMtspd2(mtSpd2 != null && !"".equals(mtSpd2) ? Integer.valueOf(mtSpd2.trim()) : 0);
						// RPT接收量
						String rptRvCnt = infoEle.attributeValue("RPTRVCNT");
						dGateBuf.setRptrvcnt(rptRvCnt != null && !"".equals(rptRvCnt) ? Integer.valueOf(rptRvCnt.trim()) : 0);
						// MO接收缓冲
						String moRvBuf = infoEle.attributeValue("MORVBUF");
						dGateBuf.setMorvbuf(moRvBuf != null && !"".equals(moRvBuf) ? Integer.valueOf(moRvBuf.trim()) : 0);
						// RPT接收缓冲
						String rptRvBuf = infoEle.attributeValue("RPTRVBUF");
						dGateBuf.setRptrvbuf(rptRvBuf != null && !"".equals(rptRvBuf) ? Integer.valueOf(rptRvBuf.trim()) : 0);
						// MO发送缓冲
						String moSdBuf = infoEle.attributeValue("MOSDBUF");
						dGateBuf.setMosdbuf(moSdBuf != null && !"".equals(moSdBuf) ? Integer.valueOf(moSdBuf.trim()) : 0);
						// RPT发送缓冲
						String rptSdBuf = infoEle.attributeValue("RPTSDBUF");
						dGateBuf.setRptsdbuf(rptSdBuf != null && !"".equals(rptSdBuf) ? Integer.valueOf(rptSdBuf.trim()) : 0);
						// MO发送等待缓冲
						String moSdWaitBuf = infoEle.attributeValue("MOSDWAITBUF");
						dGateBuf.setMosdwaitbuf(moSdWaitBuf != null && !"".equals(moSdWaitBuf) ? Integer.valueOf(moSdWaitBuf.trim()) : 0);
						// RPT发送等待缓冲
						String rptSdWaitBuf = infoEle.attributeValue("RPTSDWAITBUF");
						dGateBuf.setRptsdwaitbuf(rptSdWaitBuf != null && !"".equals(rptSdWaitBuf) ? Integer.valueOf(rptSdWaitBuf.trim()) : 0);
						// 上行状态报告接收速度
						String moRptSpd = infoEle.attributeValue("MORPTSPD");
						dGateBuf.setMorptspd(moRptSpd != null && !"".equals(moRptSpd) ? Integer.valueOf(moRptSpd.trim()) : 0);
						// 更新时间
						dGateBuf.setUpdatetime(messageTime);
						//通道账号
						dGateBuf.setGateaccount(gateAccount);
						//通道账号名
						dGateBuf.setGatename(gateName);
						//数据更新
	                    Map<String, String> kyeMap = new HashMap<String, String>();
	                    kyeMap.put("GATEACCOUNT", "");
	                    kyeMap.put("GATEWAYID", "");
	                    saveOrUpdateMultiKey(dGateBuf,kyeMap);
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "解析SPGATE缓冲监控消息参数格式异常！appId:"+appId + "，monitorMsg:" + monitorMsgUtf8);
		}
	}

	/**
	 * 设置业务区域发送数据
	 * @description
	 * @param iteratorInfo
	 * @param messageTime
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-27 下午04:56:49
	 */
	@SuppressWarnings("unchecked")
	private void setBusAreaInfo(Iterator iteratorInfo, Timestamp messageTime, Long appId)
	{
		try
		{
			//业务编码
			String busCode = "";
			//数据时间
			int dataHour = 0;
			//区域码码
			String areaCode = "";
			//MT已发
			String mtHaveSnd = "";
			//INFO节点
			Element infoEle = null;
			//A节点迭代器
			Iterator aiteratorInfo = null;
			//A节点
			Element aEle = null;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(messageTime);
			//消息小时
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			if(hour == 0)
			{
				hour = 24;
				calendar.add(Calendar.DAY_OF_MONTH, -1);
			}
			int messageDate = Integer.parseInt(format.format(calendar.getTimeInMillis()));
			// 业务区域发送对象KEY值，格式：时间&业务&区域
			//String busAreaSendKey = "";
			// 业务区域发送对象VALUE值
			String busAreaSendValue = "";
			// 业务区域发送对象VALUE值的json格式
			JSONObject busAreaSendValueJson = null;
			// 业务区域发送对象
			LfBusareasend busareasend = null;
			// 业务区域发送记录
			List<LfBusareasend> busareasendList = null;
			// 遍历INFO节点
			while(iteratorInfo.hasNext())
			{
				infoEle = (Element) iteratorInfo.next();
				if(infoEle != null)
				{
					aiteratorInfo = infoEle.elementIterator("A");
					if(aiteratorInfo == null)
					{
						EmpExecutionContext.error("业务区域监控消息获取子节点A失败 ，monitorMsg:" + monitorMsgUtf8);
						continue;
					}
				}
				else
				{
					EmpExecutionContext.error("业务区域监控消息INFO节点下无节点A，monitorMsg:" + monitorMsgUtf8);
					continue;
				}
				// 业务编码
				busCode = infoEle.attributeValue("BusCode").trim();
				// 数据时间
				dataHour = Integer.parseInt(infoEle.attributeValue("Hour").trim());
				if(dataHour == 0)
				{
					dataHour = 24;
				}
				//数据时间和消息时间一致
				if(hour - dataHour == 0)
				{
					while(aiteratorInfo.hasNext())
					{
						aEle = (Element) aiteratorInfo.next();
						if(aEle == null)
						{
							EmpExecutionContext.error("业务区域监控消息获取节点A数据失败。aEle为null" + "，monitorMsg:" + monitorMsgUtf8);
							continue;
						}
						//区域编码
						areaCode = aEle.attributeValue("AreaCode");
						if(areaCode == null || "".equals(areaCode))
						{
							EmpExecutionContext.error("业务区域监控消息获取节点A数据异常。areaCode:"+areaCode + "，monitorMsg:" + monitorMsgUtf8);
							continue;
						}
						//MT总量
						mtHaveSnd = aEle.attributeValue("MtHaveSnd");
						if(mtHaveSnd == null || "".equals(mtHaveSnd))
						{
							mtHaveSnd = "0";
						}
						//设置业务区域发送对象KEY值，格式：时间&业务&区域
						//busAreaSendKey = messageDate + "&"+ busCode + "&" + areaCode;
						
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						conditionMap.put("buscode", busCode);
						conditionMap.put("areacode", areaCode);
						conditionMap.put("datadate", String.valueOf(messageDate));
						conditionMap.put("gatewayid", String.valueOf(appId));
						busareasendList = empDao.findListByCondition(getConnection(), true, LfBusareasend.class, conditionMap, null);
						//缓存数据已存在
						if(busareasendList != null && busareasendList.size() > 0)
						{
							// 业务区域发送发送总数
							busAreaSendValue = busareasendList.get(0).getMtsendcount();
							// 业务区域发送对象VALUE值转为JSON格式
							busAreaSendValueJson = new JSONObject(busAreaSendValue);
							// 时间段数据不存在或存在数据但值不一致，更新记录
							if(!busAreaSendValueJson.has(String.valueOf(hour)) 
									|| !mtHaveSnd.equals(busAreaSendValueJson.get(String.valueOf(hour))))
							{
								//设置发送信息
								busAreaSendValueJson.put(String.valueOf(hour), mtHaveSnd);
								// 更新数据库
								monitorBaseInfoBiz.updateBusAreaSend(getConnection(), messageDate, busCode, areaCode, busAreaSendValueJson.toString(), appId);
								// 更新缓存
								//MonitorStaticValue.mon_BusAreaMap.put(busAreaSendKey, busAreaSendValueJson.toString());
							}
						}
						//缓存数据不存在
						else
						{
							//设置业务区域发送对象
							busareasend = new LfBusareasend();
							busareasend.setBusname(" ");
							busareasend.setBuscode(busCode);
							busareasend.setAreacode(areaCode);
							busAreaSendValueJson = new JSONObject();
							busAreaSendValueJson.put(String.valueOf(hour), mtHaveSnd);
							busareasend.setMtsendcount(busAreaSendValueJson.toString());
							busareasend.setCorpcode(" ");
							busareasend.setUpdatetime(new Timestamp(System.currentTimeMillis()));
							busareasend.setDatadate(messageDate);
							busareasend.setGatewayid(appId);
							//新增记录
							baseBiz.addObj(getConnection(), busareasend);
							//更新缓存
							//MonitorStaticValue.mon_BusAreaMap.put(busAreaSendKey, busAreaSendValueJson.toString());
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "解析业务区域发送数据监控消息参数格式异常 ，monitorMsg:" + monitorMsgUtf8);
		}
	}

	/**
	 * 字符串转换时间格式
	 *
	 * @description
	 * @param time
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-12-31 上午11:37:49
	 */
	public Timestamp timeChange(String time)
	{
		Timestamp ts = null;
		try
		{
			Format format;
			if(time.indexOf(".") == -1)
			{
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
			else
			{
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			}
			Date d = (Date) format.parseObject(time);
			ts = new Timestamp(d.getTime());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "监控消息解析，解析XML格式字符串转换时间格式异常。");
			ts = null;
		}
		return ts;
	}

	/**
	 * 创建SP账号配置信息
	 *
	 * @description
	 * @param lfMonSspacinfo
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-7 下午01:51:38
	 */
	public boolean addSpAccoutCfgInfo(LfMonSspacinfo lfMonSspacinfo)
	{
		boolean isResult = false;
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("spaccountid", lfMonSspacinfo.getSpaccountid());
			List<LfMonSspacinfo> monSspacinfoList = empDao.findListByCondition(getConnection(), true, LfMonSspacinfo.class, conditionMap, null);
			//如果该SP账号记录已存在,直接返回
			if(monSspacinfoList !=null && monSspacinfoList.size() > 0)
			{
				EmpExecutionContext.info("监控消息解析，创建SP账号配置时，已存在相同的SP账号，spaccountid:"+lfMonSspacinfo.getSpaccountid());
				return true;
			}
			isResult = empDao.save(getConnection(), true, lfMonSspacinfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "监控消息解析，创建SP账号配置信息失败!spaccountid:"+lfMonSspacinfo.getSpaccountid());
			isResult = false;
		}
		return isResult;
	}

	/**
	 * 创建通道账号配置信息
	 *
	 * @description
	 * @param lfMonSgtacinfo
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-6 下午07:22:13
	 */
	public boolean addGateAccoutCfgInfo(LfMonSgtacinfo lfMonSgtacinfo)
	{
		boolean isResult = false;
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("gateaccount", lfMonSgtacinfo.getGateaccount());
			List<LfMonSgtacinfo> monSgtacinfoList = empDao.findListByCondition(getConnection(), true, LfMonSgtacinfo.class, conditionMap, null);
			//如果该通道账号记录已存在,直接返回
			if(monSgtacinfoList != null && monSgtacinfoList.size() > 0)
			{
				EmpExecutionContext.info("监控消息解析，创建通道账号配置时，已存在相同的通道账号，gateaccount:"+lfMonSgtacinfo.getGateaccount());
				return true;
			}
			isResult = empDao.save(getConnection(), true, lfMonSgtacinfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "监控消息解析，创建通道账号配置信息失败!gateaccount:"+lfMonSgtacinfo.getGateaccount());
			isResult = false;
		}
		return isResult;
	}
    
    /**
     * 更新监控信息到数据库
     * @param obj 实体对象
     * @param key update条件中key值
     */
    public void saveOrUpdateMultiKey(Object obj,Map<String, String> keyMap){
        GenericEmpTransactionDAO genericEmpTransactionDAO = new GenericEmpTransactionDAO();
/*        if(isExpireMultiKey(obj,keyMap)){
            return;
        }*/
        try {
			connection = getConnection();
            empTransDao.beginTransaction(connection);
            boolean up = genericEmpTransactionDAO.updateConMultiKey(connection,obj,keyMap);
            if(!up)
            {
                genericEmpTransactionDAO.saveCon(connection,obj);
            }
            empTransDao.commitTransaction(connection);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"监控消息解析，更新监控信息异常！");
            empTransDao.rollBackTransaction(connection);
        }finally
        {
        	try
			{
				connection.setAutoCommit(true);
			}
			catch (SQLException e)
			{
				EmpExecutionContext.error(e, "监控消息解析，设置连接状态为自动提交异常！");
			}
        }
    }

    /**
     * 更新监控信息到数据库
     * @param obj 实体对象
     * @param key update条件中key值
     */
    public void saveOrUpdate(Object obj,String key){
        GenericEmpTransactionDAO genericEmpTransactionDAO = new GenericEmpTransactionDAO();
/*        if(isExpire(obj,key)){
            return;
        }*/
        try {
			connection = getConnection();
            empTransDao.beginTransaction(connection);
            boolean up = genericEmpTransactionDAO.updateCon(connection,obj,key);
            if(!up)
            {
                genericEmpTransactionDAO.saveCon(connection,obj);
            }
            empTransDao.commitTransaction(connection);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"监控消息解析，更新监控信息异常！key:"+key);
            empTransDao.rollBackTransaction(connection);
        }finally
        {
        	try
			{
				connection.setAutoCommit(true);
			}
			catch (SQLException e)
			{
				EmpExecutionContext.error(e, "监控消息解析，设置连接状态为自动提交异常！");
			}
        }
    }
    
    /**
     * 判断数据是否过期（小于数据库时间则不进行更新）
     */
    public boolean isExpire(Object obj,String key) {
        boolean isExpire = true;
        try {
            Class entityClass = obj.getClass();
            Map<String, String> columns = new GenericEmpDAO().getORMMap(entityClass);
            String tableName = columns.get(entityClass.getSimpleName());
            String updatetime = "updatetime";
            if(obj instanceof LfMonDgtacinfo)
            {
                updatetime = "modifytime";
            }
            List list = new ArrayList();
            String[] params = {updatetime,key};
            for (String param : params) {
                Object val = entityClass.getMethod("get"+ Character.toUpperCase(param.charAt(0))+ param.substring(1)).invoke(obj);
                if(val == null)
                {
                    return false;
                }
                list.add(val);
            }

            String sql = "select count(*) totalcount from "+tableName+" where "+updatetime+" >= ? " + "and " + key + "= ?";

            isExpire = new SuperDAO().findCountBySQLCon(getConnection(), true,sql,list) > 0;

        } catch (Exception e) {
            EmpExecutionContext.error(e,"监控消息解析，判断监控数据时间有效性失败！");
        }
        return isExpire;
    }

    /**
     * 判断数据是否过期（小于数据库时间则不进行更新）
     */
    public boolean isExpireMultiKey(Object obj,Map<String, String> keyMap) {
        boolean isExpire = true;
        try {
            Class entityClass = obj.getClass();
            Map<String, String> columns = new GenericEmpDAO().getORMMap(entityClass);
            String tableName = columns.get(entityClass.getSimpleName());
            String updatetime = "updatetime";
            if(obj instanceof LfMonDgtacinfo)
            {
                updatetime = "modifytime";
            }
            List list = new ArrayList();
            StringBuffer sql = new StringBuffer();
            sql.append("select count(*) totalcount from ").append(tableName).append(" where ").append(updatetime).append(" >= ?");
            String[] params = new String[keyMap.size()+1];
            params[0] = updatetime;
            int i = 1;
            for(String key : keyMap.keySet())
            {
            	params[i] = key.toLowerCase();
            	sql.append(" and ").append(key).append(" = ?");
            	i++;
            }
            for (String param : params) {
                Object val = entityClass.getMethod("get"+ Character.toUpperCase(param.charAt(0))+ param.substring(1)).invoke(obj);
                if(val == null)
                {
                    return false;
                }
                list.add(val);
            }
            isExpire = new SuperDAO().findCountBySQLCon(getConnection(), true,sql.toString(),list) > 0;

        } catch (Exception e) {
            EmpExecutionContext.error(e,"监控消息解析，判断监控数据时间有效性失败！");
        }
        return isExpire;
    }
    
    /**
     * 获取连接
     * @description    
     * @return       			 
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-4-6 下午05:58:38
     */
	public Connection getConnection()
	{
		try
		{
			if(connection == null || connection.isClosed())
			{
				connection = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
			return connection;
		}
		catch (SQLException e)
		{
			EmpExecutionContext.error(e, "监控消息解析，监控分析线程获取数据库连接异常！");
			return null;
		}
	}

	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}
    
	/**
	 * 关闭连接
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-6 下午02:13:39
	 */
	public void closeConnection()
	{
		if (connection != null)
		{
			try
			{
				connection.close();
			}
			catch (SQLException e)
			{
				EmpExecutionContext.error(e, "监控消息解析，关闭监控消息解析数据库连接异常！");
			}
		}
	}
    
	/**
	 * 获取主机网络监控信息
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-5-7 下午06:06:09
	 */
	public Map<String, LfMonHostnet> getHostNetMonInfo()
	{
		try
		{
			// 查询数据库
			List<LfMonHostnet> lfMonHostnetList = baseBiz.getByCondition(getConnection(), true, LfMonHostnet.class, null, null);
			
			Iterator<LfMonHostnet> itr = lfMonHostnetList.iterator();
			LfMonHostnet monHostnet;
			Map<String, LfMonHostnet> hostNetInfoMap = new HashMap<String, LfMonHostnet>();
			while(itr.hasNext())
			{
				monHostnet = itr.next();
				// 设置主机网络信息
				hostNetInfoMap.put(String.valueOf(monHostnet.getWebnode())+monHostnet.getProcenode()+monHostnet.getProcetype(), monHostnet);
			}
			return hostNetInfoMap;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "监控消息解析，获取主机网络监控信息异常！");
			return null;
		}
	}
	
	/**
	 * 是否未提交
	 * @description    
	 * @param NoMtHaveSnd
	 * @param spNodeMthavesndMap
	 * @param key
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-5-19 上午11:48:07
	 */
	public boolean isNoMtHaveSndState(Integer NoMtHaveSnd, Map<String, Integer> spNodeMthavesndMap, String key)
	{
		boolean isNoMtHaveSndState = false;
		try
		{
			//单除当前节点的提交总量
			Integer otherSpSnd = 0;
			//单个SP账号缓存中所有节点的提交总量
			Integer oldAllSpSnd = 0;
			//单个SP账号当前所有节点的提交总量
			Integer newAllSpsnd = 0;
			if(spNodeMthavesndMap != null && spNodeMthavesndMap.size() > 0)
			{
				for(String mapKey : spNodeMthavesndMap.keySet())
				{
					//缓存中所有节点提交总量累加
					oldAllSpSnd += spNodeMthavesndMap.get(mapKey);
					if(!mapKey.equals(key))
					{
						//除当前节点的提交总量累加
						otherSpSnd += spNodeMthavesndMap.get(mapKey);
					}
				}
			}
			//单个SP账号当前所有节点的提交总量 = 缓存中除当前节点的提交总量累加+单个SP账号当前节点的提交总量
			newAllSpsnd = otherSpSnd + NoMtHaveSnd;
			if(newAllSpsnd == 0)
			{
				return true;
			}
			if(newAllSpsnd - oldAllSpSnd == 0)
			{
				return true;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "监控消息解析，SP账号未提交状态获取失败！");
		}
		
		return isNoMtHaveSndState;
	}
}
