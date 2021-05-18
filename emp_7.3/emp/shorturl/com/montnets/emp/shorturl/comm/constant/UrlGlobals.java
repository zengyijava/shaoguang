package com.montnets.emp.shorturl.comm.constant;

import java.util.Properties;

import com.montnets.emp.common.context.EmpExecutionContext;

public class UrlGlobals {
	/**
	 * 配置文件名称
	 */
	public static final String URL_SEND_CONFIG = "urlSendConfig";
//	/**
//	 * 短链接队列定时生成检查时间(秒)
//	 */
//	public static  Integer QUEUE_CHECK_TIME = 0;
//	/**
//	 * 短链接统计数据定时汇总时间(秒)
//	 */
//	public static  Integer SUM_DATA_TIME = 0;
//	/**
//	 * 短链接发送任务异常数据处理定时时间（秒）
//	 */
//	
//	/**
//	 * 短链接生成队列大小
//	 */
//	public static  Integer QUEUE_SIZE = 0;
//	/**
//	 * 短链接生成队列每次生成个数
//	 */
//	public static  Long QUEUE_CREATE_COUNT = 0L;
//	/**
//	 * 短链接生成队列最低值
//	 */
//	public static  Integer QUEUE_LIMIT_COUNT = 0;
//	/**
//	 * 短链接缓存并发数
//	 */
//	public static  Integer CONCURR_LEVEL = 0;
//	/**
//	 * 短链接缓存写入后过期天数
//	 */
//	public static  Integer EXPIRE_AFTER_ACCESS = 0;
//	/**
//	 * 短链接缓存初始化大小
//	 */
//	public static  Integer INIT_CAPACITY = 0;
//	/**
//	 * 短链接缓存最多容量
//	 */
//	public static  Integer MAX_MUM_SIZE = 0;
//	/**
//	 * 短链接缓存初始化写入数据大小
//	 */
//	public static  Integer INIT_LOAD_COUNT = 0;
	protected static Integer READ_TIME = 0;
	/**
	 * 短链接详细入库线程池核心线程数
	 */
	protected static  Integer CORE_POOL_SIZE = 0;
	/**
	 * 短链接详细入库线程池最大线程数
	 */
	protected static  Integer MAX_POOL_SIZE = 0;
	/**
	 * 短链接详细入库线程池空闲线程存活时间（秒）
	 */
	protected static  Integer KEEP_ALIVE_TIME = 0;
	/**
	 * 短链接前缀
	 */
	protected static String CHANGE_URL = "";
	
	/**
	 * 短地址中心URL 
	 * 调用获取短地址，禁用长地址接口时使用
	 * 例如：http://ip:port/sms/v2/std/
	 */
	protected static String SHORT_CENTER_URL="";
	/**
	 * 通知地址中心线程睡眠间隔时间
	 */
	protected static Long NOTICE_STOP_TIME = 360l;
	/**
	 * emp编号
	 */
	protected static String EMP_NUM=""; 
	/**
	 * 读取条数
	 */
	protected static Integer READ_COUNT = 0;



	/**
	 * exdata是否转码 0否 1是
	 */
	public static Integer IS_EXDATA_ENCODE = 0;
	protected static Properties defaults = new Properties();
	
	
	public static Integer getREAD_TIME() {
		return READ_TIME;
	}

	public static void setREAD_TIME(Integer rEAD_TIME) {
		READ_TIME = rEAD_TIME;
	}

	public static Integer getCORE_POOL_SIZE() {
		return CORE_POOL_SIZE;
	}

	public static void setCORE_POOL_SIZE(Integer cORE_POOL_SIZE) {
		CORE_POOL_SIZE = cORE_POOL_SIZE;
	}

	public static Integer getMAX_POOL_SIZE() {
		return MAX_POOL_SIZE;
	}

	public static void setMAX_POOL_SIZE(Integer mAX_POOL_SIZE) {
		MAX_POOL_SIZE = mAX_POOL_SIZE;
	}

	public static Integer getKEEP_ALIVE_TIME() {
		return KEEP_ALIVE_TIME;
	}

	public static void setKEEP_ALIVE_TIME(Integer kEEP_ALIVE_TIME) {
		KEEP_ALIVE_TIME = kEEP_ALIVE_TIME;
	}

	public static String getCHANGE_URL() {
		return CHANGE_URL;
	}

	public static void setCHANGE_URL(String cHANGE_URL) {
		CHANGE_URL = cHANGE_URL;
	}

	public static String getSHORT_CENTER_URL() {
		return SHORT_CENTER_URL;
	}

	public static void setSHORT_CENTER_URL(String sHORT_CENTER_URL) {
		SHORT_CENTER_URL = sHORT_CENTER_URL;
	}

	public static Long getNOTICE_STOP_TIME() {
		return NOTICE_STOP_TIME;
	}

	public static void setNOTICE_STOP_TIME(Long nOTICE_STOP_TIME) {
		NOTICE_STOP_TIME = nOTICE_STOP_TIME;
	}

	public static String getEMP_NUM() {
		return EMP_NUM;
	}

	public static void setEMP_NUM(String eMP_NUM) {
		EMP_NUM = eMP_NUM;
	}

	public static Integer getREAD_COUNT() {
		return READ_COUNT;
	}

	public static void setREAD_COUNT(Integer rEAD_COUNT) {
		READ_COUNT = rEAD_COUNT;
	}
	public static Integer getIsExdataEncode() {
		return IS_EXDATA_ENCODE;
	}

	public static void setIsExdataEncode(Integer isExdataEncode) {
		IS_EXDATA_ENCODE = isExdataEncode;
	}

	public static Properties getDefaults() {
		return defaults;
	}

	public static void setDefaults(Properties defaults) {
		UrlGlobals.defaults = defaults;
	}

	public static String getUrlSendConfig() {
		return URL_SEND_CONFIG;
	}

	public static void setProperties(Properties properties){
		defaults = properties;
	}
	
	public static Properties getProperties(){
		return defaults;
	}
	
	public static String getValue(String key){
		return defaults.getProperty(key);
	}
	public static String getValue(String key,String defaultValue){
		String value = defaults.getProperty(key);
		if(value==null || "".equals(value)){
			return defaultValue;
		}
		return value;
	}
	
	public static int getIntValue(String key,int defaultValue){
		if(defaults.getProperty(key)==null){
			return defaultValue;
		}
		try{
			return Integer.parseInt(defaults.getProperty(key).trim());
		}catch (Exception e) {
			EmpExecutionContext.error(e, "UrlGlobals读取配置文件信息异常。");
			return defaultValue;
		}
	}
	
	public static long getLongValue(String key,long defaultValue){
		if(defaults.getProperty(key)==null){
			return defaultValue;
		}
		try{
			return Long.parseLong(defaults.getProperty(key).trim());
		}catch (Exception e) {
			EmpExecutionContext.error(e, "UrlGlobals读取配置文件信息异常。");
			return defaultValue;
		}
	}
	
	
}
