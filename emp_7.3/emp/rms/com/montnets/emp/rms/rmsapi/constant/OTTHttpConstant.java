package com.montnets.emp.rms.rmsapi.constant;

import com.montnets.emp.common.constant.SystemGlobals;

/**
 * @Author:yangdl
 * @Data:Created in 19:11 2018.8.7 007
 */
public class OTTHttpConstant {

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

    /************** 富信平台删除模板rest路径 **************/
    public static final String RMS_DELETE_TEMPLATE_URL = "/rms/v3/std/del_template";

    /************** 富信平台查询模板rest路径 **************/
    public static final String RMS_QUERY_TEMPLATE_URL = "/rms/v3/std/query_template";

    /************** 富信平台发送模板rest路径 **************/
    public static final String RMS_SEND_TEMPLATE_URL = "/rms/v2/std/send_template";

    /************** 富信平台获取状态报告模板rest路径 **************/
    public static final String RMS_GET_RPT_TEMPLATE_URL = "/rms/v2/std/get_rpt";

    /************** 富信平台下载模板rest路径 **************/
    public static final String RMS_GET_TEMPLATE_URL = "/rms/v3/std/get_tmplfile";

    /************** 富信平台查询历史记录rest路径 **************/
    public static final String MBOSS_QUERY_HISRECORD_URL = "/rms_his_interf";

    /************** 富信平台账号 **************/
    public static final String RMS_USERID = "SZ8541";

    /************** 富信平台密码 **************/
    public static final String RMS_PASS = "SZ8541";

    /************** 富信平台apikey **************/
    public static final String RMS_API_KEY = "BZGRkZGRkZGTO0rCuxOPW0Ln6PC9ib2R5P";

    /************** 富信平台ip **************/
    protected static String RMS_IP = "10.10.203.3"; //192.169.0.63

    /************** 富信平台端口 **************/
    protected static int RMS_PORT = 8080;//60002

    /************** 富信发送平台端口 **************/
    protected static int RMS_SEND_PORT = 8082;

    /************** 富信发送平台ip **************/
    protected static String RMS_SEND_IP = "192.169.0.120";

    /************** 富信平台账号 **************/
    protected static String RMS_SEND_USERID = "hwmc01";

    /************** 富信平台密码 **************/
    protected static String RMS_SEND_PASS = "123456";

    /************** 汇总数据同步秘钥**************/
    protected static String SYSN_DATA_ENCRY_KEY = "91B940A64C61F88E";

    /************** 汇总数据发布地址**************/
    protected static String RMS_SYN_DATA_PUSH_SERVICE_URL = "http://192.169.1.247:8088/rmsReport/IRmsService";

    /************** 默认档位**************/
    public static final String DEFAULT_DEGREE = "1";

    /************** 固定字符串**************/
    public static final String FIXATION_SEQUENCE = "00000000";

    public static final Integer DEFAULT_VALIDTM = 48;

    /************** json报文格式**************/
    public static final String APPLICATION_JSON = "application/json";

    /************** mboss数据查询ip**************/
    protected static String RMS_MOSS_QUERY_IP = "10.10.203.3";

    /************** mboss数据查询port**************/
    protected static int RMS_MOSS_QUERY_PORT = 8080;

    /************** mboss数据查询加密密钥**************/
    protected static String RMS_MOSS_QUERY_ENCRY_KEY = "6158ACD7414A7E5F";
    /**************富信编辑器版本******/
    protected static String RMS_EDITOR_VERSION = "2.0";

    public static String getRMS_IP() {
		return RMS_IP;
	}

	public static void setRMS_IP(String rMS_IP) {
		RMS_IP = rMS_IP;
	}

	public static int getRMS_PORT() {
		return RMS_PORT;
	}

	public static void setRMS_PORT(int rMS_PORT) {
		RMS_PORT = rMS_PORT;
	}

	public static int getRMS_SEND_PORT() {
		return RMS_SEND_PORT;
	}

	public static void setRMS_SEND_PORT(int rMS_SEND_PORT) {
		RMS_SEND_PORT = rMS_SEND_PORT;
	}

	public static String getRMS_SEND_IP() {
		return RMS_SEND_IP;
	}

	public static void setRMS_SEND_IP(String rMS_SEND_IP) {
		RMS_SEND_IP = rMS_SEND_IP;
	}

	public static String getRMS_SEND_USERID() {
		return RMS_SEND_USERID;
	}

	public static void setRMS_SEND_USERID(String rMS_SEND_USERID) {
		RMS_SEND_USERID = rMS_SEND_USERID;
	}

	public static String getRMS_SEND_PASS() {
		return RMS_SEND_PASS;
	}

	public static void setRMS_SEND_PASS(String rMS_SEND_PASS) {
		RMS_SEND_PASS = rMS_SEND_PASS;
	}

	public static String getSYSN_DATA_ENCRY_KEY() {
		return SYSN_DATA_ENCRY_KEY;
	}

	public static void setSYSN_DATA_ENCRY_KEY(String sYSN_DATA_ENCRY_KEY) {
		SYSN_DATA_ENCRY_KEY = sYSN_DATA_ENCRY_KEY;
	}

	public static String getRMS_SYN_DATA_PUSH_SERVICE_URL() {
		return RMS_SYN_DATA_PUSH_SERVICE_URL;
	}

	public static void setRMS_SYN_DATA_PUSH_SERVICE_URL(
			String rMS_SYN_DATA_PUSH_SERVICE_URL) {
		RMS_SYN_DATA_PUSH_SERVICE_URL = rMS_SYN_DATA_PUSH_SERVICE_URL;
	}

	public static String getRMS_EDITOR_VERSION() {
		return RMS_EDITOR_VERSION;
	}

	public static void setRMS_EDITOR_VERSION(String rMS_EDITOR_VERSION) {
		RMS_EDITOR_VERSION = rMS_EDITOR_VERSION;
	}

	static {
        RMS_EDITOR_VERSION = SystemGlobals.getValue("montnets.rms.editor.version");
        RMS_IP = SystemGlobals.getValue("montnets.rms.template.http.ip");
        RMS_PORT = SystemGlobals.getIntValue("montnets.rms.template.http.port", 8080);
        RMS_SEND_IP = SystemGlobals.getValue("montnets.rms.send.http.ip");
        RMS_SEND_PORT = SystemGlobals.getIntValue("montnets.rms.send.http.port", 8080);
        RMS_SYN_DATA_PUSH_SERVICE_URL = SystemGlobals.getValue("montnets.rms.mboss.push.url");
        SYSN_DATA_ENCRY_KEY = SystemGlobals.getValue("montnets.rms.mboss.encry.key");
        RMS_MOSS_QUERY_IP = SystemGlobals.getValue("montnets.rms.mboss.query.http.ip");
        RMS_MOSS_QUERY_PORT = SystemGlobals.getIntValue("montnets.rms.mboss.query.http.port", 8080);
        RMS_MOSS_QUERY_ENCRY_KEY = SystemGlobals.getValue("montnets.rms.mboss.query.encry.key");

    }
    
    
}
