package com.montnets.emp.netnews.common;

import java.util.Map;
import java.util.Properties;

public class StaticValue {
	
	//原EMP保存值
	public static final String EMP_LOG_BUFFERSIZE = "emp.log.bufferSize";
	
	public static final String EMP_LOG_PRINTFILTERLEVEL = "emp.log.printFilterLevel";
	
	public static final String EMP_LOG_SAVEFILTERLEVEL = "emp.log.saveFilterLevel";
	
	public static final String EMP_LOG_SHOW_SQL = "emp.log.show_sql";
	
	public static final String ENCODING = "UTF-8";
	
	public static final String FILEDIRNAME = "fileUpload/smstxtfile/";
	
	public static final String IMSMSTXT = "imsmstxt/";
	
	public static final String MANUALSMSTXT = "manualsmstxt/";
	
	public static final String SERVICESMSTXT = "servicesmstxt/";
	
	public static final String MOBILEBUSINESSTXT = "mobilebussinesstxt/";
	
	public static final java.lang.String ASC = "ASC";
	 
	public static final java.lang.String DESC = "DESC";
	
	/**
	 * 网讯保存值保存的key值
	 */
	public static final String RESOURCE_PROPERTY = "resourceBundle";
	
	public static final String VISION = "montnets.wx.version";
	
	public static final String DBTYPE = "montnets.wx.dbtype";
	
	public static final String DES_KEY = "montnets.wx.webservice.deskey";
	
	public static final String BEAN_PACKAGE = "com.montnets.wx.bean";
	
	public static final String UPLOAD_PATH = "ueditor/server/uploadfiles/";
	
	public static final String UPLOAD_TEMP = "uploadTemp/";
	
	public static final String UPLOAD_JSP = "newjsp/";
	
	public static final String TEMP_PATH = "temp/";
	//设置模板时上传图片保存的路径
	public static final String WX_TEMP_PATH = "/file/wxTempImage/";
	
	public static final String SHOW_VERIFY_CODE = "com.montnets.wx.showVerifyCode";
	
	/**
	 * memcached保存的key值
	 */
	public static final String SMS_TIMING_DATE = "SMS_TIMING_DATE";		//定时保存网讯访问记录用，（五分钟）
	
	public static final String SMS_DATA_MAX__COUNT = "SMS_DATA_MAX__COUNT";		//一天最多访问网讯次数用，（一天时间）
		
	public static final String SMS_NETMSG_NET_PGEE = "SMS_NETMSG_NET_PGEE";	//页面ID集合
	
	public static final String SMS_PHONE_VISIT_LIST = "SMS_PHONE_VISIT_LIST" ;		//手机一天访问次数集合
	
	public static final String SMS_NETMSG_BALANCE_LIST = "SMS_NETMSG_BALANCE_LIST";		//网讯帐户余额集合
	
	public static final String SMS_NETMSG_PAGE_LIST = "MSM_NETMSG_PAGE_LIST";		//网讯页面集合
	
	public static final String 	SMS_NETMSG_VISIT_LOG = "SMS_NETMSG_VISIT_LOG";	//网讯访问日志记录
	
	public static final String OPSPER = "#LG#";
	
	public static final String SURVEY_MANAGER = "问卷管理";
	
}
