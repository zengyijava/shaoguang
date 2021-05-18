package com.montnets.emp.rms.rmsapi.constant;

import com.montnets.emp.common.constant.SystemGlobals;

/**
 * 
 * 文件名称:RMSHttpConstant.java
 * 文件描述:常量类
 * 内容摘要:
 * 修改日期       修改人员   版本	   修改内容 
 * 2018-1-10    qiyin     0.1    0.1 新建
 * 版权:版权所有(C)2018
 * 公司:深圳市梦网科技有限发展公司
 * @author:   qiyin<15112605627@163.com>
 * @version:  0.1  
 * @Date:     2018-1-10 上午11:26:25
 */
public class RMSHttpConstant
{


	public static final String UTF8_ENCODE = "UTF-8";

	/****************** http 请求成功状态码 **************/
	public static final int HTTP_SUCCESS_CODE = 200;

	/************** 重发完毕 **************/
	public static final int RESEND_DONE = 0;

	/************** 默认重发次数 **************/
	public static final int DEFAULT_RESEND = 2;

	/************** 是否为长连接，默认为长连接 **************/
	public static final boolean IS_KEEP_ALIVE = true;

	/************** 长连接 **************/
	public static final String KEEP_ALIVE = "Keep-Alive";

	/************** 请求超时时间(毫秒) 5秒 **************/
	public static final int HTTP_REQUEST_TIMEOUT = 5 * 1000;

	
	/************** 默认http连接池大小 **************/
	public static final int POOL_NUMBER = 2;

	/************** 响应超时时间(毫秒) 30秒 **************/
	public static final int HTTP_RESPONSE_TIMEOUT = 30 * 1000;

	
	/************** 富信平台上传模板rest路径 **************/
	public static final String RMS_UPLOAD_TEMPLATE_URL = "/rms/v3/std/upload_tmplfile";

	/************** 富信平台上传模板rest路径 V2.0**************/
	public static final String RMS_UPLOAD_TEMPLATE_URL_V2 = "/rms/v2/std/upload_tmplfile";

	/************** 富信平台删除模板rest路径 **************/
	public static final String RMS_DELETE_TEMPLATE_URL = "/rms/v3/std/del_template";
	
	/************** 富信平台查询模板rest路径 **************/
	public static final String RMS_QUERY_TEMPLATE_URL = "/rms/v3/std/query_template";
	public static final String RMS_QUERY_TEMPLATE_URL_V2 = "/rms/v2/std/query_template";

	/************** 富信平台发送模板rest路径 **************/
	public static final String RMS_SEND_TEMPLATE_URL = "/rms/v3/std/send_template";
	
	/************** 富信平台下载模板rest路径 **************/
	public static final String RMS_GET_TEMPLATE_URL = "/rms/v3/std/get_tmplfile";
	
	/************** 富信平台下载同步RCOS模板路径 **************/
	public static final String RCOS_GET_EC_EMPLATE_URL = "/rms/v2/std/sync_template";

	/************** RCOS平台下载模板rest路径 **************/
	public static final String RCOS_GET_TEMPLATE_URL = "/emp/v1/std/get_tmplfile";

	/************** RCOS平台同步公共模板状态地址 **************/
	public static final String RCOS_SYN_TEMPLATE_URL = "/emp/v1/std/status_tmplfile";

	/************** 富信平台查询历史记录rest路径 **************/
	public static final String MBOSS_QUERY_HISRECORD_URL = "/rms_his_interf";
	
	/************** 富信平台账号 **************/
	public static final String RMS_USERID = "SZ8541";
	
	/************** 富信平台密码 **************/
	public static final String RMS_PASS = "SZ8541";
	
	/************** 富信平台apikey **************/
	public static final String RMS_API_KEY = "BZGRkZGRkZGTO0rCuxOPW0Ln6PC9ib2R5P";
	
	/************** 富信平台ip **************/
	public static  final String RMS_IP; //192.169.0.63

	/************** 富信平台端口 **************/
	public static  final int RMS_PORT;//60002

	/************** 富信发送平台端口 **************/
	public static  final int RMS_SEND_PORT ;
	
	/************** 富信发送平台ip **************/
	public static  final String RMS_SEND_IP;
	
	/************** 富信平台账号 **************/
	public static final String RMS_SEND_USERID = "hwmc01";
	
	/************** 富信平台密码 **************/
	public static final String RMS_SEND_PASS = "123456";
	

	/************** 汇总数据同步秘钥**************/
	public static  final String SYSN_DATA_ENCRY_KEY ;
	
	/************** 汇总数据发布地址**************/
	public static  final String RMS_SYN_DATA_PUSH_SERVICE_URL ;
	
	/************** 默认档位**************/
	public static final String DEFAULT_DEGREE = "1";
	
	
	/************** 固定字符串**************/
	public static final String FIXATION_SEQUENCE = "00000000";

	public static final Integer DEFAULT_VALIDTM = 48;

	/************** json报文格式**************/
	public static final String APPLICATION_JSON = "application/json";

	/************** mboss数据查询ip**************/
	public static  final String RMS_MOSS_QUERY_IP;

	/************** mboss数据查询port**************/
	public static final int RMS_MOSS_QUERY_PORT;
	
	/************** mboss数据查询加密密钥**************/
	public static final String RMS_MOSS_QUERY_ENCRY_KEY;

	/************** 富信平台同步RCOS公共模板 ip **************/
	public static  final String RCOS_IP; //192.169.0.63

	/************** 富信平台同步RCOS公共模板端口 **************/
	public static  final int RCOS_PORT;//60002

	
	static{
		RCOS_IP=SystemGlobals.getValue("montnets.rcos.template.http.ip");
		RCOS_PORT=SystemGlobals.getIntValue("montnets.rcos.template.http.port",8080);
		RMS_IP=SystemGlobals.getValue("montnets.rms.template.http.ip");
		RMS_PORT=SystemGlobals.getIntValue("montnets.rms.template.http.port",8080);
		RMS_SEND_IP=SystemGlobals.getValue("montnets.rms.send.http.ip");
		RMS_SEND_PORT=SystemGlobals.getIntValue("montnets.rms.send.http.port",8080);
		RMS_SYN_DATA_PUSH_SERVICE_URL=SystemGlobals.getValue("montnets.rms.mboss.push.url");
		SYSN_DATA_ENCRY_KEY=SystemGlobals.getValue("montnets.rms.mboss.encry.key");
		RMS_MOSS_QUERY_IP=SystemGlobals.getValue("montnets.rms.mboss.query.http.ip");
		RMS_MOSS_QUERY_PORT=SystemGlobals.getIntValue("montnets.rms.mboss.query.http.port",8080);
		RMS_MOSS_QUERY_ENCRY_KEY=SystemGlobals.getValue("montnets.rms.mboss.query.encry.key");
		
	}
}
