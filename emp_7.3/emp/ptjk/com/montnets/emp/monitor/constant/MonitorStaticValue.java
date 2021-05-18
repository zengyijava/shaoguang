package com.montnets.emp.monitor.constant;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.montnets.emp.common.constant.MonAlarmDsmParams;
import com.montnets.emp.common.entity.LfNodeBaseInfo;
import com.montnets.emp.entity.monitor.LfMonDbstate;
import com.montnets.emp.entity.monitor.LfMonHostnet;
import com.montnets.emp.entity.monitor.LfMonSgtacinfo;
import com.montnets.emp.entity.monitor.LfMonShost;
import com.montnets.emp.entity.monitor.LfMonSproce;
import com.montnets.emp.entity.monitor.LfMonSspacinfo;

public class MonitorStaticValue
{
	// 主机监控基础信息
	protected static Map<Long, LfMonShost>			hostBaseMap					= new ConcurrentHashMap<Long, LfMonShost>();

	// 主机监控基础信息临时缓存
	protected static Map<Long, LfMonShost>			hostBaseMapTemp					= new ConcurrentHashMap<Long, LfMonShost>();

	// 主机监控动态信息
	protected static Map<Long, MonDhostParams>			hostMap					= new ConcurrentHashMap<Long, MonDhostParams>();

	// 主机监控动态信息临时缓存
	protected static Map<Long, MonDhostParams>			hostMapTemp				= new ConcurrentHashMap<Long, MonDhostParams>();

	// 程序监控基础信息
	protected static Map<Long, LfMonSproce>		proceBaseMap				= new ConcurrentHashMap<Long, LfMonSproce>();

	// 程序监控基础信息临时缓存
	protected static Map<Long, LfMonSproce>		proceBaseMapTemp				= new ConcurrentHashMap<Long, LfMonSproce>();

	// 程序监控动态信息
	protected static Map<Long, MonDproceParams>		proceMap				= new ConcurrentHashMap<Long, MonDproceParams>();

	// 程序监控动态信息临时缓存
	protected static Map<Long, MonDproceParams>		proceMapTemp				= new ConcurrentHashMap<Long, MonDproceParams>();

	// SP账号监控基础信息
	protected static Map<String, LfMonSspacinfo>	spAccountBaseMap			= new ConcurrentHashMap<String, LfMonSspacinfo>();

	// SP账号监控基础信息临时缓存
	protected static Map<String, LfMonSspacinfo>	spAccountBaseMapTemp			= new ConcurrentHashMap<String, LfMonSspacinfo>();

	// SP账号监控动态信息
	protected static Map<String, MonDspAccountParams>	spAccountMap			= new ConcurrentHashMap<String, MonDspAccountParams>();

	// SP账号监控动态信息临时缓存
	protected static Map<String, MonDspAccountParams>	spAccountMapTemp			= new ConcurrentHashMap<String, MonDspAccountParams>();

	// 通道账号监控基础信息
	protected static Map<String, LfMonSgtacinfo>	gateAccountBaseMap			= new ConcurrentHashMap<String, LfMonSgtacinfo>();

	// 通道账号监控基础信息临时缓存
	protected static Map<String, LfMonSgtacinfo>	gateAccountBaseMapTemp			= new ConcurrentHashMap<String, LfMonSgtacinfo>();

	// 通道账号监控动态信息
	protected static Map<String, MonDgateacParams>	gateAccountMap			= new ConcurrentHashMap<String, MonDgateacParams>();

	// 通道账号监控动态信息临时缓存
	protected static Map<String, MonDgateacParams>	gateAccountMapTemp			= new ConcurrentHashMap<String, MonDgateacParams>();

	//WBS缓冲信息
	protected static Map<Long, MonDwbsBufParams>	wbsBufInfoMap			= new ConcurrentHashMap<Long, MonDwbsBufParams>();

	//GATE缓冲信息
	protected static Map<String, MonDgateBufParams>	spgateBufInfoMap			= new ConcurrentHashMap<String, MonDgateBufParams>();

	//GATE缓冲信息临时缓存
	protected static Map<String, MonDgateBufParams>	spgateBufInfoMapTemp			= new ConcurrentHashMap<String, MonDgateBufParams>();

	// 数据库监控动态信息
	protected static Map<String, LfMonDbstate>	dbMonMap			= new ConcurrentHashMap<String, LfMonDbstate>();

	// 数据库监控动态临时缓存信息
	protected static Map<String, LfMonDbstate>	dbMonMapTemp			= new ConcurrentHashMap<String, LfMonDbstate>();

	// 主机网络监控信息
	protected static Map<String, LfMonHostnet>	hostNetMap			= new ConcurrentHashMap<String, LfMonHostnet>();

	// 主机网络监控缓存信息
	protected static Map<String, LfMonHostnet>	hostNetMapTemp			= new ConcurrentHashMap<String, LfMonHostnet>();

	//WEB节点基础信息
	protected static List<LfNodeBaseInfo> nodeBaseInfoList = new ArrayList<LfNodeBaseInfo>();

	//页面刷新时间 单位：秒,默认30秒
	protected static  int refreshTime =30*1000;

	//数据分析间隔时间 单位:毫秒
	protected static long timeInterval = 20*1000L;

	//网络异常时间间隔 单位:秒
	protected static int networkRrror = 180;

	//告警信息
	protected static TreeMap<Long ,MonErrorParams> monError = new TreeMap<Long ,MonErrorParams>();

	//告警信息临时缓存
	protected static TreeMap<Long ,MonErrorParams> monErrorTemp = new TreeMap<Long ,MonErrorParams>();

	//通道账号费用余额
	protected static Map<String, Integer> gateAccoutFee = new ConcurrentHashMap<String, Integer>();

	//EMP_GW程序状态，0：正常；1：异常
	protected static int EMP_WG_STATUS = 0;

	//MQ服务器告警标识
	protected static int MQServerFlag = 0;

	// 存放SP账号基础信息，KEY:SP账号，VALUE：String[0]账号名称；String[1]账号类型 string[2]发送级别
	protected static Map<String, String[]>						SPACCOUTN_INFO			= new ConcurrentHashMap<String, String[]>();

	// 存放通道账号基础信息，KEY：通道账事情；VALUE：String[0]账号名称；String[1]付费类型
	protected static Map<String, String[]>						GATEACCOUTN_INFO		= new ConcurrentHashMap<String, String[]>();

	//WEB服务器名称
	protected static String serverName = "";

	//监控消息队列
	protected static LinkedBlockingQueue<String> monitorMsgQueue = new LinkedBlockingQueue<String>(10000);

	//通道账号费用线程启动标识  false:未启动;true:启动
	protected static boolean isGateAccountFee = false;

	//数据解析线程启动标识  false:未启动;true:启动
	protected static boolean isMonDataResolve = false;

	//系统启动初始化加载状态,true:加载完成,false:未加载完成
	protected static boolean isLoadFinish = false;

	//连续达到告警阀值次数
	protected static int consecutiveTimes = 9;

	//连续达到告警阀值次数（动态数据，如CPU,内存等）
	protected static int DYN_CONSECUTIVETIMES = 9;

	//业务区域发送数据
	protected static Map<String, String>	mon_BusAreaMap		= new HashMap<String, String>();
	//业务区域发送数据初始化标识  false:未初始化;true:已初始化
	protected static boolean initBusAreaSend = false;
	//EMP服务器监控线程标识  false:程序退出;true:程序运行中
	protected static boolean MonEmpInfoThread = true;
	//监控消息线程标识  false:程序退出;true:程序运行中
	protected static boolean MonDataResolveThread = true;
	//监控数据分析线程标识  false:程序退出;true:程序运行中
	protected static boolean MonDataAnalysisThread = true;
	//监控线程标识  false:程序退出;true:程序运行中
	protected static boolean monThreadState = true;
	//是否打印监控消息日志，true:是；false:否
	protected static boolean isMonMsgLog = true;
	//分析线程启动时间
	protected static String threadStartDate = "";
	//发送短信号码及内容，号码之间以半角逗号“,”隔开，手机号码与下行内容之间以斜杠“/”分隔
	protected static List<MonAlarmDsmParams> monAlarmDsmList = new ArrayList<MonAlarmDsmParams>();
	//文件消息服务器密钥
	protected static String fileServerMonMsgKey = "1A2B3C4D5E6F7899";
	//数据库当前时间
	protected static Timestamp curDbServerTime ;
	public static Map<Long, LfMonShost> getHostBaseMap() {
		return hostBaseMap;
	}
	public static void setHostBaseMap(Map<Long, LfMonShost> hostBaseMap) {
		MonitorStaticValue.hostBaseMap = hostBaseMap;
	}
	public static Map<Long, LfMonShost> getHostBaseMapTemp() {
		return hostBaseMapTemp;
	}
	public static void setHostBaseMapTemp(Map<Long, LfMonShost> hostBaseMapTemp) {
		MonitorStaticValue.hostBaseMapTemp = hostBaseMapTemp;
	}
	public static Map<Long, MonDhostParams> getHostMap() {
		return hostMap;
	}
	public static void setHostMap(Map<Long, MonDhostParams> hostMap) {
		MonitorStaticValue.hostMap = hostMap;
	}
	public static Map<Long, MonDhostParams> getHostMapTemp() {
		return hostMapTemp;
	}
	public static void setHostMapTemp(Map<Long, MonDhostParams> hostMapTemp) {
		MonitorStaticValue.hostMapTemp = hostMapTemp;
	}
	public static Map<Long, LfMonSproce> getProceBaseMap() {
		return proceBaseMap;
	}
	public static void setProceBaseMap(Map<Long, LfMonSproce> proceBaseMap) {
		MonitorStaticValue.proceBaseMap = proceBaseMap;
	}
	public static Map<Long, LfMonSproce> getProceBaseMapTemp() {
		return proceBaseMapTemp;
	}
	public static void setProceBaseMapTemp(Map<Long, LfMonSproce> proceBaseMapTemp) {
		MonitorStaticValue.proceBaseMapTemp = proceBaseMapTemp;
	}
	public static Map<Long, MonDproceParams> getProceMap() {
		return proceMap;
	}
	public static void setProceMap(Map<Long, MonDproceParams> proceMap) {
		MonitorStaticValue.proceMap = proceMap;
	}
	public static Map<Long, MonDproceParams> getProceMapTemp() {
		return proceMapTemp;
	}
	public static void setProceMapTemp(Map<Long, MonDproceParams> proceMapTemp) {
		MonitorStaticValue.proceMapTemp = proceMapTemp;
	}
	public static Map<String, LfMonSspacinfo> getSpAccountBaseMap() {
		return spAccountBaseMap;
	}
	public static void setSpAccountBaseMap(
			Map<String, LfMonSspacinfo> spAccountBaseMap) {
		MonitorStaticValue.spAccountBaseMap = spAccountBaseMap;
	}
	public static Map<String, LfMonSspacinfo> getSpAccountBaseMapTemp() {
		return spAccountBaseMapTemp;
	}
	public static void setSpAccountBaseMapTemp(
			Map<String, LfMonSspacinfo> spAccountBaseMapTemp) {
		MonitorStaticValue.spAccountBaseMapTemp = spAccountBaseMapTemp;
	}
	public static Map<String, MonDspAccountParams> getSpAccountMap() {
		return spAccountMap;
	}
	public static void setSpAccountMap(Map<String, MonDspAccountParams> spAccountMap) {
		MonitorStaticValue.spAccountMap = spAccountMap;
	}
	public static Map<String, MonDspAccountParams> getSpAccountMapTemp() {
		return spAccountMapTemp;
	}
	public static void setSpAccountMapTemp(
			Map<String, MonDspAccountParams> spAccountMapTemp) {
		MonitorStaticValue.spAccountMapTemp = spAccountMapTemp;
	}
	public static Map<String, LfMonSgtacinfo> getGateAccountBaseMap() {
		return gateAccountBaseMap;
	}
	public static void setGateAccountBaseMap(
			Map<String, LfMonSgtacinfo> gateAccountBaseMap) {
		MonitorStaticValue.gateAccountBaseMap = gateAccountBaseMap;
	}
	public static Map<String, LfMonSgtacinfo> getGateAccountBaseMapTemp() {
		return gateAccountBaseMapTemp;
	}
	public static void setGateAccountBaseMapTemp(
			Map<String, LfMonSgtacinfo> gateAccountBaseMapTemp) {
		MonitorStaticValue.gateAccountBaseMapTemp = gateAccountBaseMapTemp;
	}
	public static Map<String, MonDgateacParams> getGateAccountMap() {
		return gateAccountMap;
	}
	public static void setGateAccountMap(
			Map<String, MonDgateacParams> gateAccountMap) {
		MonitorStaticValue.gateAccountMap = gateAccountMap;
	}
	public static Map<String, MonDgateacParams> getGateAccountMapTemp() {
		return gateAccountMapTemp;
	}
	public static void setGateAccountMapTemp(
			Map<String, MonDgateacParams> gateAccountMapTemp) {
		MonitorStaticValue.gateAccountMapTemp = gateAccountMapTemp;
	}
	public static Map<Long, MonDwbsBufParams> getWbsBufInfoMap() {
		return wbsBufInfoMap;
	}
	public static void setWbsBufInfoMap(Map<Long, MonDwbsBufParams> wbsBufInfoMap) {
		MonitorStaticValue.wbsBufInfoMap = wbsBufInfoMap;
	}
	public static Map<String, MonDgateBufParams> getSpgateBufInfoMap() {
		return spgateBufInfoMap;
	}
	public static void setSpgateBufInfoMap(
			Map<String, MonDgateBufParams> spgateBufInfoMap) {
		MonitorStaticValue.spgateBufInfoMap = spgateBufInfoMap;
	}
	public static Map<String, MonDgateBufParams> getSpgateBufInfoMapTemp() {
		return spgateBufInfoMapTemp;
	}
	public static void setSpgateBufInfoMapTemp(
			Map<String, MonDgateBufParams> spgateBufInfoMapTemp) {
		MonitorStaticValue.spgateBufInfoMapTemp = spgateBufInfoMapTemp;
	}
	public static Map<String, LfMonDbstate> getDbMonMap() {
		return dbMonMap;
	}
	public static void setDbMonMap(Map<String, LfMonDbstate> dbMonMap) {
		MonitorStaticValue.dbMonMap = dbMonMap;
	}
	public static Map<String, LfMonDbstate> getDbMonMapTemp() {
		return dbMonMapTemp;
	}
	public static void setDbMonMapTemp(Map<String, LfMonDbstate> dbMonMapTemp) {
		MonitorStaticValue.dbMonMapTemp = dbMonMapTemp;
	}
	public static Map<String, LfMonHostnet> getHostNetMap() {
		return hostNetMap;
	}
	public static void setHostNetMap(Map<String, LfMonHostnet> hostNetMap) {
		MonitorStaticValue.hostNetMap = hostNetMap;
	}
	public static Map<String, LfMonHostnet> getHostNetMapTemp() {
		return hostNetMapTemp;
	}
	public static void setHostNetMapTemp(Map<String, LfMonHostnet> hostNetMapTemp) {
		MonitorStaticValue.hostNetMapTemp = hostNetMapTemp;
	}
	public static List<LfNodeBaseInfo> getNodeBaseInfoList() {
		return nodeBaseInfoList;
	}
	public static void setNodeBaseInfoList(List<LfNodeBaseInfo> nodeBaseInfoList) {
		MonitorStaticValue.nodeBaseInfoList = nodeBaseInfoList;
	}
	public static int getRefreshTime() {
		return refreshTime;
	}
	public static void setRefreshTime(int refreshTime) {
		MonitorStaticValue.refreshTime = refreshTime;
	}
	public static long getTimeInterval() {
		return timeInterval;
	}
	public static void setTimeInterval(long timeInterval) {
		MonitorStaticValue.timeInterval = timeInterval;
	}
	public static int getNetworkRrror() {
		return networkRrror;
	}
	public static void setNetworkRrror(int networkRrror) {
		MonitorStaticValue.networkRrror = networkRrror;
	}
	public static TreeMap<Long, MonErrorParams> getMonError() {
		return monError;
	}
	public static void setMonError(TreeMap<Long, MonErrorParams> monError) {
		MonitorStaticValue.monError = monError;
	}
	public static TreeMap<Long, MonErrorParams> getMonErrorTemp() {
		return monErrorTemp;
	}
	public static void setMonErrorTemp(TreeMap<Long, MonErrorParams> monErrorTemp) {
		MonitorStaticValue.monErrorTemp = monErrorTemp;
	}
	public static Map<String, Integer> getGateAccoutFee() {
		return gateAccoutFee;
	}
	public static void setGateAccoutFee(Map<String, Integer> gateAccoutFee) {
		MonitorStaticValue.gateAccoutFee = gateAccoutFee;
	}
	public static int getEMP_WG_STATUS() {
		return EMP_WG_STATUS;
	}
	public static void setEMP_WG_STATUS(int eMP_WG_STATUS) {
		EMP_WG_STATUS = eMP_WG_STATUS;
	}
	public static int getMQServerFlag() {
		return MQServerFlag;
	}
	public static void setMQServerFlag(int mQServerFlag) {
		MQServerFlag = mQServerFlag;
	}
	public static Map<String, String[]> getSPACCOUTN_INFO() {
		return SPACCOUTN_INFO;
	}
	public static void setSPACCOUTN_INFO(Map<String, String[]> sPACCOUTN_INFO) {
		SPACCOUTN_INFO = sPACCOUTN_INFO;
	}
	public static Map<String, String[]> getGATEACCOUTN_INFO() {
		return GATEACCOUTN_INFO;
	}
	public static void setGATEACCOUTN_INFO(Map<String, String[]> gATEACCOUTN_INFO) {
		GATEACCOUTN_INFO = gATEACCOUTN_INFO;
	}
	public static String getServerName() {
		return serverName;
	}
	public static void setServerName(String serverName) {
		MonitorStaticValue.serverName = serverName;
	}
	public static LinkedBlockingQueue<String> getMonitorMsgQueue() {
		return monitorMsgQueue;
	}
	public static void setMonitorMsgQueue(
			LinkedBlockingQueue<String> monitorMsgQueue) {
		MonitorStaticValue.monitorMsgQueue = monitorMsgQueue;
	}
	public static boolean isGateAccountFee() {
		return isGateAccountFee;
	}
	public static void setGateAccountFee(boolean isGateAccountFee) {
		MonitorStaticValue.isGateAccountFee = isGateAccountFee;
	}
	public static boolean isMonDataResolve() {
		return isMonDataResolve;
	}
	public static void setMonDataResolve(boolean isMonDataResolve) {
		MonitorStaticValue.isMonDataResolve = isMonDataResolve;
	}
	public static boolean isLoadFinish() {
		return isLoadFinish;
	}
	public static void setLoadFinish(boolean isLoadFinish) {
		MonitorStaticValue.isLoadFinish = isLoadFinish;
	}
	public static int getConsecutiveTimes() {
		return consecutiveTimes;
	}
	public static void setConsecutiveTimes(int consecutiveTimes) {
		MonitorStaticValue.consecutiveTimes = consecutiveTimes;
	}
	public static int getDYN_CONSECUTIVETIMES() {
		return DYN_CONSECUTIVETIMES;
	}
	public static void setDYN_CONSECUTIVETIMES(int dYN_CONSECUTIVETIMES) {
		DYN_CONSECUTIVETIMES = dYN_CONSECUTIVETIMES;
	}
	public static Map<String, String> getMon_BusAreaMap() {
		return mon_BusAreaMap;
	}
	public static void setMon_BusAreaMap(Map<String, String> mon_BusAreaMap) {
		MonitorStaticValue.mon_BusAreaMap = mon_BusAreaMap;
	}
	public static boolean isInitBusAreaSend() {
		return initBusAreaSend;
	}
	public static void setInitBusAreaSend(boolean initBusAreaSend) {
		MonitorStaticValue.initBusAreaSend = initBusAreaSend;
	}
	public static boolean isMonEmpInfoThread() {
		return MonEmpInfoThread;
	}
	public static void setMonEmpInfoThread(boolean monEmpInfoThread) {
		MonEmpInfoThread = monEmpInfoThread;
	}
	public static boolean isMonDataResolveThread() {
		return MonDataResolveThread;
	}
	public static void setMonDataResolveThread(boolean monDataResolveThread) {
		MonDataResolveThread = monDataResolveThread;
	}
	public static boolean isMonDataAnalysisThread() {
		return MonDataAnalysisThread;
	}
	public static void setMonDataAnalysisThread(boolean monDataAnalysisThread) {
		MonDataAnalysisThread = monDataAnalysisThread;
	}
	public static boolean isMonThreadState() {
		return monThreadState;
	}
	public static void setMonThreadState(boolean monThreadState) {
		MonitorStaticValue.monThreadState = monThreadState;
	}
	public static boolean isMonMsgLog() {
		return isMonMsgLog;
	}
	public static void setMonMsgLog(boolean isMonMsgLog) {
		MonitorStaticValue.isMonMsgLog = isMonMsgLog;
	}
	public static String getThreadStartDate() {
		return threadStartDate;
	}
	public static void setThreadStartDate(String threadStartDate) {
		MonitorStaticValue.threadStartDate = threadStartDate;
	}
	public static List<MonAlarmDsmParams> getMonAlarmDsmList() {
		return monAlarmDsmList;
	}
	public static void setMonAlarmDsmList(List<MonAlarmDsmParams> monAlarmDsmList) {
		MonitorStaticValue.monAlarmDsmList = monAlarmDsmList;
	}
	public static String getFileServerMonMsgKey() {
		return fileServerMonMsgKey;
	}
	public static void setFileServerMonMsgKey(String fileServerMonMsgKey) {
		MonitorStaticValue.fileServerMonMsgKey = fileServerMonMsgKey;
	}
	public static Timestamp getCurDbServerTime() {
		return curDbServerTime;
	}
	public static void setCurDbServerTime(Timestamp curDbServerTime) {
		MonitorStaticValue.curDbServerTime = curDbServerTime;
	}
	
}
