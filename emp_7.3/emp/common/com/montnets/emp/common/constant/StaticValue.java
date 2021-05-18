package com.montnets.emp.common.constant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.montnets.emp.common.bean.BalanceAlarmBean;
import com.montnets.emp.entity.monitoronline.LfMonOnlcfg;
import com.montnets.emp.entity.monitoronline.LfMonOnluser;

public class StaticValue
{
	public static final String			SMS_BOX								= "短信信箱";

	public static final String			MMS_BOX								= "彩信信箱";

	public static final String			BIZUSED_CENTER						= "业务应用中心";

	public static final String			TEMP_MANAGE							= "模板管理";

	public static final String			PARAM_PRESERVE						= "参数维护";

	public static final String			STATISTICS							= "查询统计";

	public static final String			USERS_MANAGER						= "用户管理";

	public static final String			ADDBR_MANAGER						= "通讯录管理";

	public static final String			OPER_MONITOR						= "运营监控";

	public static final String			GATE_CONFIG							= "网关配置";

	public static final String			ADD									= "新建";

	public static final String			DELETE								= "删除";

	public static final String			UPDATE								= "修改";

	public static final String			GET									= "查询";

	public static final String			OTHER								= "其他";

	public static final String			TRUE_RESULT							= "1";

	public static final String			FALSE_RESULT						= "0";

	public static final String			ALLOUT								= "allout";

	public static final String			CURDOUT								= "curdout";

	public static final String			NOSEARCH							= "nosearch";

	public static final String			NOLOG								= "nolog";

	public static final Properties			systemProperty						= System.getProperties();

	public static final String			LINE_SEPARATOR						= "line.separator";

	public static final String			FILEDIRNAME							= "file/smstxt/";

	public static final String			IMSMSTXT							= "imsmstxt/";

	public static final String			MANUALSMSTXT						= "manualsmstxt/";

	public static final String			SERVICESMSTXT						= "servicesmstxt/";

	public static final String			MOBILEBUSINESSTXT					= "mobilebussinesstxt/";

	public static final String			OASMSTXT							= "oasmstxt/";

	public static final String			OPSPER								= "#LG#";

	public static final String			EMP_POOLNAME						= "EMP";

	public static final String			SMSACC_POOLNAME						= "EMP";

	public static final String			SMSSVR_POOLNAME						= "EMP";

	public static final String			DB_CACHE_SWITCH_OPEN				= "1";

	public static final String			ASC									= "ASC";

	public static final String			DESC								= "DESC";

	public static final String			IN									= "in";

	public static final String			NOT_IN								= "not in";

	public static final String			LIKE								= "like";

	public static final String			LIKE1								= "like1";

	public static final String			LIKE2								= "like2";

	public static final String			BETWEEN								= "between";

	public static final String			MENUCODE_SGSMS						= "3-1";

	public static final String			MENUCODE_MOSMS						= "3-4";

	public static final String			MENUCODE_MTSMS						= "3-5";

	public static final String			MENUCODE_BGIM						= "8-1";

	public static final String			MENUCODE_KFIM						= "8-2";

	/*
	 * public static final String
	 * EMP_TABLE_PATH="com.montnets.emp.database.emp.table"; public static final
	 * String SMSACC_TABLE_PATH="com.montnets.emp.database.acc.table"; public
	 * static final String
	 * SMSSVR_TABLE_PATH="com.montnets.emp.database.svr.table";
	 * public static final String VIEW_PATH="com.montnets.emp.vo.view";
	 */
	// public static String DATABASE_TYPE="ORACLE";
	// public static final String ORACLE_DATABASE="ORACLE";

	private static Map<String, String>	titleMap;

	public static Map<String, String> getTitleMap() {
		return titleMap;
	}

	public static void setTitleMap(Map<String, String> titleMap) {
		StaticValue.titleMap = titleMap;
	}

	public static final String			DB_ACCESS_DRIVER					= "database.access.driver.implementation";

	public static final String			DB_CACHE_SWITCH						= "";

	public static final String			DB_CACHE_REFRESH_TIME_HOUR			= "";

	public static final String			DB_CACHE_REFRESH_TIME_MINUTE		= "";

	public static final String			DB_JOB_TIME							= "database.job.time";

	public static final String			MON_EMP_P1234							= "montnets.emp.p1234";


	public static final String			EMP_WEB_ROOT						= "emp.web.root";

	public static final String			EMP_WEB_DEFAULT						= "";

	public static final String			EMP_PHONE_UNICOM					= "emp.phone.unicom";

	public static final String			EMP_PHONE_TELCOM					= "emp.phone.telcom";

	public static final String			EMP_PHONE_CTLCOM					= "emp.phone.ctlcom";

	public static final String			EMP_WEB_VERSION						= "emp.web.version";

	public static final String			EMP_LOGIN_YANZHENGMA				= "emp.login.yanzhengma";

	public static final String			THIRD_PARTY_TIME					= "";

	public static final String			EMP_LOG_SAVEFILTERLEVEL				= "emp.log.saveFilterLevel";

	public static final String			EMP_LOG_PRINTFILTERLEVEL			= "";

	public static final String			EMP_LOG_BUFFERSIZE					= "emp.log.bufferSize";

	public static final String			EMP_LOG_DEVMODE						= "emp.log.devMode";

	public static final String			EMP_LOG_SHOW_SQL					= "emp.log.show_sql";

	// 数据源连接池的种类。
	public static final String			POOL_TYPE							= "poolType";

	public static final String			EMP_PAGEINFO_DEFAULTPAGESIZE		= "emp.pageInfo.defaultPageSize";

	public static final String			MMS_WEBSERVICE_URL					= "";

	public static final String			MMS_WEBSERVICE_TYPE					= "";

	public static final String			MMS_USERID							= "";

	private static final String			MMS_PASSW_NOUSE						= "";

	public static final String			SESSION_USER_KEY					= "loginSysuser";

	// 系统计费开关key
	public static final String			EMP_BILLING							= "emp.balance.billing";

	// 系统计费开关key
	public static final String			EMP_MAXLEVEL						= "emp.balance.dep.maxLevel";

	// 企业快快发送账号配置
//	public static final String			EMP_SPUSER_KK						= "emp.spUser.kk";

	// 彩信模板支持帧大小
	public static final String			EMP_MMS_FRAMESIZE					= "emp.mms.frameSize";

	// 是否支持企业快快key
//	public static final String			KK_SUPPORT							= "emp.kk.support";

	public static final String			IM_USER								= "E";

	public static final String			IM_GROUP							= "G";

	public static final String			IM_DEPT								= "D";

	public static final String			ICON_FEMALE							= "user_female";

	public static final String			ICON_MALE							= "user_suit";

	public static final String			ICON_STATUS_ONLINE					= "status_online";

	public static final String			ICON_STATUS_OFFLINE					= "status_offline";

	public static final String			ICON_STATUS_BUSY					= "status_busy";

	public static final String			ICON_STATUS_AWAY					= "status_away";

	public static final String			MSG_STATE_UR						= "0";

	public static final String			MSG_STATE_R							= "1";

	public static final String			MSG_STATE_PR						= "1";

	public static final String			IM_MENU_CODE_OFFICE					= "8-1";

	public static final int				SEND_TYPE_PRIVATE_TALK				= 1;

	public static final int				SEND_TYPE_GROUP_CHAT				= 2;

	public static final String			ENCODING							= "UTF-8";

	public static final String			CONTENT_TYPE_TEXT_HTML				= "text/html;charset=UTF-8";

	public static final String			DATE_FORMAT							= "yyyy-MM-dd  HH:mm:ss";

	public static final String			ORACLR_DATE_FORMAT					= "yyyy-MM-dd HH24:mi:ss";

	public static final String			IM_DATE_FORMAT						= "yyyy-MM-dd HH:mm:ss";

	public static final int				DEFAULT_PAGE_SIZE					= 10;

	/* public static final String FILE_UPLOAD_PATH = "file/fileDownload"; */
	public static final String			FILE_UPLOAD_PATH					= "file/excel/temp";

	public static final String			CLIENT_UPLOAD						= "file/clientUpload/";

	public static final String			EMP_UPLOAD							= "file/empUpload/";

	// public static final String WX = "file/wx/";
	public static final String			WX_MATER							= "file/wx/mater";

	public static final String			WX_PAGE								= "file/wx/PAGE";

	// public static final String WX_VIDEO = "file/wx/video";

	// public static final String WC = "file/weix/";

	public static final String			WC_RIMG								= "file/weix/rimg";

	public static final String			WC_ACCOUNT							= "file/weix/account";

	public static final String			SESSION_KEY_UPDATE_MTTASK			= "updateMattaskPercentage";

	public static final String			FLOW_TYPE							= "1";

	public static final String			BUS_UPSERVICE						= "B1-1";

	public static final String			BUS_IMOFFIC							= "B2-1";

	public static final String			BUS_IMCLIENTSEAT					= "B2-2";

	public static final String			TIMESTAMP							= "class java.sql.Timestamp";

	public static final String			DATE_SQL							= "class java.sql.Date";

	public static final String			DATE_UTIL							= "class java.util.Date";

	public static final String[]				DepMtReportBiz_title_month			= {"机构下行月统计报表","DepDownReportMonth"};

	public static final String[]				DepMtReportBiz_title_year			= {"机构下行年统计报表","DepDownReportYear"};

	public static final String[]				OperatorsMtReportBiz_title_month	= {"运营商下行月统计报表","operatorsMtReportMonth"};

	public static final String[]				OperatorsMtReportBiz_title_year		= {"运营商下行年统计报表","operatorsMtReportYear"};

	public static final String[]				SpisuncmMtReportBiz_title_month		= {"运营商下行月统计报表","spisuncmMtReportMonth"};

	public static final String[]				SpisuncmMtReportBiz_title_year		= {"运营商下行年统计报表","spisuncmMtReportYear"};

	public static final String[]				SysMoReportBiz_title_month			= {"系统上行月统计报表","sysMoReportMonth"};

	public static final String[]				SysMoReportBiz_title_year			= {"系统上行年统计报表","sysMoReportYear"};

	public static final String[]				SysMtReportBiz_title_month			= {"系统下行月统计报表","sysMtReportMonth"};

	public static final String[]				SysMtReportBiz_title_year			= {"系统下行年统计报表","sysMtReportYear"};

	public static final String[]				SysMoRealTimeRecordBiz				= {"系统上行实时记录","sysMoRtRecord"};

	public static final String[]				SysMoHistoryRecordBiz				= {"系统上行历史记录","sysMoHRBiz"};

	// excel模板目录
	public static final String			EXCEL_FILE							= "file/excel/";

	public static final String			MMS_MATERIAL						= "file/mms/material/";

	public static final String			MMS_MTTASKS							= "file/mms/mttasks/";

	public static final String			PNOTICE_MTTASKS						= "file/notice/";

	public static final String			MMS_TEMPLATES						= "file/mms/templates/";

	public static final String			MMS_PREVIEW							= "file/mms/preview/";

	public static final String			MMS_TEMPRESOURCE					= "file/mms/tempResource/";

	public static final String			MMS_SENDRESOURCE					= "file/mms/sendResource/";

	public static final String			EMP_WEB_FRAME						= "emp.web.frame";

	public static final String			MANUAL_SMSTXT						= "file/smstxt/manualsmstxt";

	public static final String			JSPXML_FILE							= "file/jspxmlfile/";

	public static final String			YDCW								= "file/ydcw/";

	public static final String          APP_SENDFILE                        = "file/app/sendfile";

	// public static final String SUV_MENUCODE = "1200-1200";
	public static final String			SUV_MENUCODE						= "1200-1300";

	// public static final String IM_MENUCODE = "1205-1000";
	public static final String			IM_MENUCODE							= "1000-1400";

	// 上行业务管理模块编码
	public static final String			SERMO_MENUCODE						= "1203-1000";

	public static final String			ELECPOWER_MENUCODE					= "1050-1100";

	public static final String			OA_MENUCODE							= "2100-1000";

	// 员工相同内容发送
	public static final String			EMPSAMEMSGSEND_MENUCODE				= "1050-1060";

	// 员工不同内容发送
	public static final String			EMPDIFMSGSEND_MENUCODE				= "1050-1070";

	// 客户相同内容发送
	public static final String			CLISAMEMSGSEND_MENUCODE				= "1050-1080";

	// 客户不同内容发送
	public static final String			CLIDIFMSGSEND_MENUCODE				= "1050-1090";

	// 电子工资单
	public static final String			ELCPAYROLL_MENUCODE					= "1260-1000";

	// 报销提醒
	public static final String			REMIND_MENUCODE						= "1260-1100";

	// 回款通知
	public static final String			BACKNNOTICE_MENUCODE				= "1260-1200";

	// 下行业务管理
	public static final String			MTSERVICE_MENUCODE					= "1203-1200";

	// 审批提醒
	public static final String			VERIFYREMIND_MENUCODE				= "10680";

	public static final String			WXREMIND_MENUCODE					= "10910";

	// 告警短信
	public static final String			MONITORSMS_MENUCODE					= "10920";

	// 登录发送动态口令
	public static final String			LOGINSENDMESSAGE_MENUCODE			= "12002";

	// 手工群发
	public static final String			SGQF_MENUCODE						= "1050-1055";

	// 操作员尾号管理
	public static final String			IM_CZYWHGL_MENUCODE					= "1600-1450";

	public static final int				MODULE_IM_OFFICE					= 0;

	public static final int				MODULE_IM_CLIENT					= 1;

	public static final int				MODULE_IM_SEAT						= 2;

	public static final int				MO_TYPE								= 0;

	public static final int				MT_TYPE								= 1;

	public static final String			IM_MENUCODE_OFFICE					= "1000-1400";

	public static final String			IM_MENUCODE_CLIENT					= "2300-0900";

	public static final String			IM_MENUCODE_SEAT					= "2400-1000";

	public static final String			PERFECT_NOTIC						= "1050-1300";

	public static final String			IM_MENUCODE_WS						= "8888-8888";

	public static final String			SMSBOX_MENUCODE						= "1000-1000";

	public static final String			MMSBOX_MENUCODE						= "1100-1000";

	public static final String			MOBILE_PRUDUCT_CLASSNAME			= "com.montnets.emp.biz.mobilebusiness.MobileBusinessStart";

	public static final int				MOBILE_TYPE							= 0;

	public static final int				PC_TYPE								= 1;

	public static final int				IM_MESSGAE_SOURCE_EMP				= 1;

	public static final int				IM_MESSGAE_SOURCE_IM				= 2;

	public static final int				IM_MESSGAE_SOURCE_WAP				= 3;

	public static final int				IM_MESSGAE_SOURCE_MOBILE			= 4;

	public static final String			EMP_LOGIN_ISMULTI					= "emp.login.isMulti";

	public static final String			CONTRACT_CREATE						= "001";

	public static final String			CONTRACT_CANCEL						= "002";

	public static final String			ORDER_CREATE						= "003";

	public static final String			ORDER_CANCEL						= "004";

	public static final String			APPOINTMENT							= "005";

	public static final String			SGCC_SERIAL_NUMBER					= "";

	public static final String			CONTRACT_CREATE_VALUE				= "";

	public static final String			ORDER_CREATE_VALUE					= "";

	public static final String			OA_ATTACHMENT						= "file/oa/attachment/";

	//0：单企业；1：多企业
	private static int					CORPTYPE							= 0;
	

	public static final String			CODE1								= "F7675CB5CA896F8BE68EBD9E73383895";

	public static final String			CODE2								= "9E635674C8700758A0335A6C755BDF45";

	static
	{
		String enterprise = SystemGlobals.getValue("montnets.enterprise");
		if(CODE1.equals(enterprise))
		{
			setCORPTYPE(0);
		}
		else if(CODE2.equals(enterprise))
		{
			setCORPTYPE(1);
		}
	}

	public static final int				DBTYPE								= Integer.parseInt(SystemGlobals.getValue("DBType"));

	public static final int				ORACLE_DBTYPE						= 1;

	public static final int				SQLSERVER_DBTYPE					= 2;

	public static final int				MYSQL_DBTYPE						= 3;

	public static final int				DB2_DBTYPE							= 4;

	// 报表模块文件目录
	public static final String			BASEPATH_REPORT						= "/report";

	// 基础模块文件目录
	public static final String			BASEPATH							= "/base";

	public static final String			LOGOPATH							= "/images/logo/";

	// 下行业务编码
	public static final String			MTBUSCODE							= "10420";

	// 上行业务编码
	public static final String			MOBUSCODE							= "10410";

	// 企业怪怪模块ID
	public static final String			QYKKCODE							= "10040";

	// 完美通知模块ID
	public static final String			WMNOTICECODE						= "10030";

	// 备用服务器用的数据库连接池
	public static final String			EMP_BACKUP							= "EMP_BACKUP";

	// taskId取值数量
	public static final int				MAXTASKIDCOUNT						= 100;

	// 是否使用备用服务器 1-使用 ；0-不使用
	public static final String			USE_BACKUP_SERVER					= "montnets.emp.use_backup_server";

	private static String				WITHNOLOCK							= "";

	static
	{
		if(DBTYPE == SQLSERVER_DBTYPE)
		{
			setWITHNOLOCK(" with(nolock) ");
		}
	}

	public static final String			SMS_FEE_WEBSERVICE_URL				= "sms.fee.webservice.url";

	// 电子工资单业务对应的模块编码
	public static final String			MF0001								= "1260-1000";

	// 报销提醒业务对应的模块编码
	public static final String			MF0002								= "1260-1100";

	// 回款通知业务对应的模块编码
	public static final String			MF0003								= "1260-1200";

	// 企业快快（群发）业务对应的模块编码
	public static final String			MF0004								= "1000-1400";

	// 手工群发业务对应的模块编码
	public static final String			MF0005								= "1050-1055";

	// 操作员尾号管理(企业快快单发)业务对应的模块编码
	public static final String			MF0006								= "1600-1450";

	// 审批提醒业务对应的模块编码
	public static final String			MF0007								= VERIFYREMIND_MENUCODE;

	// 完美通知业务对应的模块编码
	public static final String			MF0008								= "1050-1300";

	// 上行业务管理业务对应的模块编码
	public static final String			MF0009								= "1203-1000";

	public static final String getMenuCode(String busCode)
	{
		if(busCode.equals("MF0001"))
		{
			return MF0001;
		}
		else if(busCode.equals("MF0002"))
		{
			return MF0002;
		}
		else if(busCode.equals("MF0003"))
		{
			return MF0003;
		}
		else if(busCode.equals("MF0004"))
		{
			return MF0004;
		}
		else if(busCode.equals("MF0005"))
		{
			return MF0005;
		}
		else if(busCode.equals("MF0006"))
		{
			return MF0006;
		}
		else if(busCode.equals("MF0007"))
		{
			return MF0007;
		}
		else if(busCode.equals("MF0008"))
		{
			return MF0008;
		}
		else if(busCode.equals("MF0009"))
		{
			return MF0009;
		}
		else
		{
			return "";
		}
	}

	// 完美通知的业务编码
	public static final String								WMTZ_BUSCODE			= "MF0008";

	// 自动尾号的有效期，默认为7天单位为ms
	public static final Long								VALIDITY				= 7 * 24 * 3600 * 1000L;

	// 操作员固定有效期 1HOUR
	public static final Long								TEMPVALIDITY			= 3600 * 1000L;

	// 短信账号
	public static final String								SMSACCOUNT				= "SP账号";

	// 是否包含企业快快绑定的SP账号，如果为0，则排除企业快快绑定的SP账号，如果为1，则包含企业快快绑定的SP账号,默认值为1。
	//public static int										isContainKKSp			= Integer.parseInt(SystemGlobals.getValue("emp.spUser.kk"));
	private static int										isContainKKSp			= 0;

    public static int getIsContainKKSp() {
        return isContainKKSp;
    }

    public static void setIsContainKKSp(int isContainKKSp) {
        StaticValue.isContainKKSp = isContainKKSp;
    }


	// 控制sql语句in查询的最大值
	private static int										inConditionMax			= 1000;

    public static int getInConditionMax() {
        return inConditionMax;
    }

    public static void setInConditionMax(int inConditionMax) {
        StaticValue.inConditionMax = inConditionMax;
    }

	// 机构最大级数
	public static final String									DEP_MAXLEVEL			= "dep.maxlevel";

	// 同级最大机构数
	public static final String									DEP_MAXCHILD			= "dep.maxchild";

	// 最大机构数量
	public static final String									DEP_MAXDEP				= "dep.maxdep";

	public static final String									MAX_CHARGE_DEP_LEVEL	= "dep.maxchargelevel";

	// 机构最大级数
	public static final Integer									DEP_MAXLEVEL_COUNT		= 10;

	// 同级最大机构数
	public static final Integer									DEP_MAXCHILD_COUNT		= 100;

	// 最大机构数量
	public static final Integer									DEP_MAXDEP_COUNT		= 10000;

	// 机构最大充值级数
	public static final Integer									MAX_CHARGE_DEP			= 20;

	// 密码位数
	private static final String									PASS_COUNT			= "6";

	// 密码组合形式
	private static final String									PASS_COMBTYPE			= "1,2,";

	// 修改周期
	private static final String									PASS_UPCYCLE			= "90";

	// 过期提醒
	private static final String									PASS_PASTALARM			= "7";

	// 错误上线
	private static final String									PASS_ERRLIMIT			= "6";

	//动态口令
	private static final String									PASS_DYNPWD			= "您正在登录企业移动信息平台，手机动态口令为：#P_1#";

	// 短信贴尾内容(默认值：回复TD退订)
	public static final String									SMS_TAILCONTENTS			= "回复TD退订";

	//短信贴尾标识(0：关闭；1：开启 ，默认值0)
	private static String									SMS_TAILFLAG			= "0";

	// 登录安全初始设置
	public static final Integer									SECURITY_VAL			= 0;

	// 一个机构下员工或者操作员的最大数目
	public final static int									MAX_PEOPLE_COUNT		= 50000;

	// 充值支持的最大级数
	// public static final Integer CHARGE_MAX_DEP_LEVEL = 5;

	// rpt最大接收数
	public final static int									MAX_RPT_COUNT			= 150;

	// 完美通知发送人数最大值
	public final static Integer								MAX_PERNOTIC_COUNT		= 1000;

	// 服务器登录信息变量
	private static final Map<String, LoginInfo>					loginInfoMap			= new ConcurrentHashMap<String, LoginInfo>();

	// 是否支持企业快快
//	public static final int									KKSUPPORT				= Integer.parseInt(SystemGlobals.getValue(KK_SUPPORT));

	// 同步用户统一平台WEBSERVICEIP地址
	public static final String								UPWS_WEBSERVICE_IP		= "";

	// 同步用户统一平台系统
	public static final String								UPWS_WEBSERVICE_APID	= "";

	// 同步用户统一平台系统密钥
	public static final String								UPWS_WEBSERVICE_CERT	= "";

	// 是否开启同步请求消息中心功能1开启 2是关闭
	public static final String								UPWS_WEBSERVICE_ISOPEN	= "";

	// 同步的时间间隔 单位是分钟
	public static final String								UP_TIME_INTERVAL		= "";

	// 单企业企业编码
	public static final String								UPWS_CORPCODE			= "100001";

	// 号码是否可见权限编码
	public static final String								PHONE_LOOK_CODE			= "1600-2000-0";

	// 投诉建议反馈地址
	public static final String								SUGGEST_REPORT_URL		= "suggest.report.url";

	private static String									DAO_PACKAGE				= "";

	private static String									DAO_CLASS				= "";

	// 存放要加载的模块map
	private static Map<String, String>						inniMenuMap				= new HashMap<String, String>();

	// 要存放的大模块id
	private static StringBuffer								menu_num				= new StringBuffer("");
	static
	{
		if(DBTYPE == SQLSERVER_DBTYPE)
		{
			setDaoPackage("sqlserver");
			setDaoClass("SQLServer");
		}
		else if(DBTYPE == ORACLE_DBTYPE)
		{
			setDaoPackage("oracle");
			setDaoClass("Oracle");
		}
		else if(DBTYPE == DB2_DBTYPE)
		{
			setDaoPackage("db2");
			setDaoClass("DB2");
		}
		else if(DBTYPE == MYSQL_DBTYPE)
		{
			setDaoPackage("mysql");
			setDaoClass("MySQL");
		}
	}

	// 服务器编号 为数字1001,1002,1003,1004...
	private static String									SERVER_NUMBER			= SystemGlobals.getValue("cluster.server.number");

	// public static String
	// SERVER_NUMBER=SystemGlobals.getValue("ServerNumber");

	// 是否集群 1代表集群 0代表不集群 默认为0
	private static int										ISCLUSTER				= Integer.parseInt(SystemGlobals.getValue("cluster.iscluster"));

	// public static int ISCLUSTER=SystemGlobals.getValue("IsCluster");

	// 文件服务器地址
	private static String									FILE_SERVER_URL;

	// 文件服务器外网地址
	private static String									FILE_SERVER_VIEWURL;

	// 文件服务器外网地址
	private static String									FILE_SERVER_OUTER_URL;

    // 文件服务器内网地址列表
    private static List<String> FILE_SERVER_QUEUE = new ArrayList<String>();

    // 文件服务器外网地址列表
    private static List<String> FILE_SERVER_OUTER_QUEUE = new ArrayList<String>();

    // 主文件服务器索引
    private static int FILE_SERVER_INDEX =  0;

    // 主文件服务器（外网）索引
    private static int FILE_SERVER_OUTER_INDEX =  0;

    // 主文件服务器总数
    private static int FILE_SERVER_SIZE =  1;

    //文件服务器检测连接 更新周期 防止频繁检测 10s
    public static final long FILE_SERVER_CHECK_ALIVE_PERIOD = (long)10*1000;

    //上次文件服务器检测时间
    private static long FILE_SERVER_CHECK_ALIVE_TIME = 0;

    //上次文件服务器(外网)检测时间
    private static long FILE_SERVER_OUTER_CHECK_ALIVE_TIME = 0;

    //上次文件服务器检测结果
    private static String[] FILE_SERVER_CHECK_ALIVE_RESULT ;

    //上次文件服务器(外网)检测结果
    private static String FILE_SERVER_OUTER_CHECK_ALIVE_RESULT ;

	public static String[] getFileServerCheckAliveResult() {
		return FILE_SERVER_CHECK_ALIVE_RESULT;
	}

	public static void setFileServerCheckAliveResult(String[] fileServerCheckAliveResult) {
		FILE_SERVER_CHECK_ALIVE_RESULT = fileServerCheckAliveResult;
	}

	public static String getFileServerOuterCheckAliveResult() {
		return FILE_SERVER_OUTER_CHECK_ALIVE_RESULT;
	}

	public static void setFileServerOuterCheckAliveResult(String fileServerOuterCheckAliveResult) {
		FILE_SERVER_OUTER_CHECK_ALIVE_RESULT = fileServerOuterCheckAliveResult;
	}


    //当前节点地址(内网地址)
    public static final String BASEURL = SystemGlobals.getValue("montnets.thisUrl");
    //当前节点地址(外网地址)
    public static final String OUTERURL = SystemGlobals.getValue("montnets.outerUrl");

	// 是否运行定时任务，1代表运行，0代表不运行，默认运行。
	public static final int										ISRUNTIMEJOB			= Integer.parseInt(SystemGlobals.getValue("cluster.isruntimejob"));

	private static String									EMP_VERSION				= "1.0.0.0";

	/**
	 * 压缩文件大小
	 */
	public static final long										ZIP_SIZE				= (long)500 * 1024 * 1024;

	/**
	 * 最大大小
	 */
	public static final long										MAX_SIZE				= (long)500 * 1024 * 1024;

	/**
	 * 最大号码数
	 */
	public static final long										MAX_PHONE_NUM			= 15000000;

	/**
	 * 每次写号码数
	 */
	public static final long										PER_PHONE_NUM			= 10000;

	/**
	 * 返回异常值
	 */
	public static final int										EXP_RETURN				= -9999;

	/**
	 * 重新同步内存值的时间间隔(单位:分钟)
	 */
	public static final int										SYNC_TIMER_INTERVAL		= 30 * 60 * 1000;

	/**
	 * 号码文件参数分隔符
	 */
	public static final String									MSG_SPLIT_CHAR			= ",";

	/**
	 * 最大短信内容字数
	 */
	public static final int										MAX_MSG_LEN				= 990;

	private static int										IS_WX_OPERATOR_REVIEW	= 0;

	//增加单企业，多企业的判断
	static{
		if(getCORPTYPE() ==1){
			setIsWxOperatorReview(1);
		}else{
			setIsWxOperatorReview(0);
		}
	}

	/**
	 * 下行记录过期时间，单位小时
	 */
	public static final int										MTTASKC_OVERDUE	= 24;

	/**
	 * 默认阀值提醒次数
	 */
	public static final int										ALARM_COUNT				= 1;

	// 消息中心服务器地址
	private static  String									MQ_SERVER_URL			= "127.0.0.1:61616";

	// MQ队列名
	private static  String									MQ_QUEUE_NAME			= "MEPMonitorQueue";

	// 在线用户信息
	private static  LfMonOnlcfg								MON_ONLINECFG			= new LfMonOnlcfg();

	// 在线用户告警标识
	public static final Integer									onleneMonFlag			= 0;

	// 在线用户详情
	public static final Map<String, LfMonOnluser>	mon_OnlineUserMap		= new ConcurrentHashMap<String, LfMonOnluser>();

	// 在线用户详情临时缓存
	public static final Map<String, LfMonOnluser>	mon_OnlineUserMapTemp		= new ConcurrentHashMap<String, LfMonOnluser>();

	// 生日祝福姓名签名，左边符号
	private static String	BIRTHWISH_NAME_SIGN_LEFT	= "(";

	// 生日祝福姓名签名，右边符号
	private static String	BIRTHWISH_NAME_SIGN_RIGHT	= ")";

	//英文短信特殊字符字符串,一个字符按2个计算
	private static String    SMSCONTENT_SPECIALCHAR_STR = "";

    public static String getSmscontentSpecialcharStr() {
        return SMSCONTENT_SPECIALCHAR_STR;
    }

    public static void setSmscontentSpecialcharStr(String smscontentSpecialcharStr) {
        SMSCONTENT_SPECIALCHAR_STR = smscontentSpecialcharStr;
    }

	//英文短信特殊字符,一个字符按2个计算
	public static final Map<Integer, String>    SMSCONTENT_SPECIALCHAR = new HashMap<Integer, String>();

	//短信异常字符,需要替换为空格
	public static final Map<Integer, String>    SMSCONTENT_REPLACECHAR = new HashMap<Integer, String>();

	//HTTP请求超时时间(毫秒)
	public static int HTTP_REQUEST_TIMEOUT = 2 * 60 * 1000;

	//HTTP响应超时时间(毫秒)
	public static int HTTP_RESPONSE_TIMEOUT = 2 * 60 * 1000;

	//查询运营商余额MBOSS接口加密密钥
	public static String MBOSS_KEY = "775047B2F2925858";

	//运营商余额请求间隔（毫秒）
	private static  long BALANCE_REQUEST_INTERVAL = (long)5 * 60 * 1000;

	//消息体加密标志，0 不加密，1 加密
	public static int BALANCE_MSG_ENCRYFLAG = 1;

	//MBOSS平台WEBSERVICES地址
	private static String MBOSS_WEBSERVICES_URL = "";

	//SP账号类型开关0:所有1:EMP应用
	private static int SPTYPEFLAG = 1;

	//日志打印间隔（毫秒）
	private static long LOG_PRINT_INTERVAL = (long)20 * 1000;

	//监控报文打印间隔（毫秒）
	private static long MONLOG_PRINT_INTERVAL = (long)10 * 1000;

	//企业黑名单最大数，默认500万
	private static long BLACK_MAXCOUNT = 5000000;

	/*****日志标识*****/
	//告警
	public static final String LOGFLAG_WARN = "[W]";
	//错误
	public static final String LOGFLAG_ERROR = "[E]";
	//信息
	public static final String LOGFLAG_INFO = "[I]";
	//调试
	public static final String LOGFLAG_DEBUG = "[D]";
	//模板可编辑标识，0：不可编辑；1：可编辑
	private static long TMPEDITORFLAG = 0;
	//日志保存路径
	public static final String LOG_SAVE_PATH = SystemGlobals.getValue("montnets.emp.LogSavePath");
	//是否生成客户端监控文件 0:否；1:是
	private static int ISCLIENTMONFILE = 0;
	//服务器操作系统 0:WINDOWS,1:LINUX
	private static int SERVER_SYSTEM_TYPE = 0;

    static {
        //主文件服务器
        StaticValue.getFileServerQueue().add(SystemGlobals.getValue("montnets.fileserver.innerUrl"));
        StaticValue.getFileServerOuterQueue().add(SystemGlobals.getValue("montnets.fileserver.outerUrl"));
        //备文件服务器
        String bakInnerUrl = SystemGlobals.getValue("montnets.fileserver.bak.innerUrl");
        String bakOuterUrl = SystemGlobals.getValue("montnets.fileserver.bak.outerUrl");
        if(bakInnerUrl != null && bakInnerUrl.trim().length()>0)
        {
            String[] httpUrls = bakInnerUrl.split("([,，]\\s*)+");
            for (String httpUrl : httpUrls) {
                if(httpUrl.trim().length() == 0 || StaticValue.getFileServerQueue().contains(httpUrl))
                {
                    continue;
                }
                StaticValue.getFileServerQueue().add(httpUrl);
            }
        }

        if(bakOuterUrl != null && bakOuterUrl.trim().length()>0)
        {
            String[] httpOutUrls = bakOuterUrl.split("([,，]\\s*)+");
            for (String httpoutUrl : httpOutUrls) {
                if(httpoutUrl.trim().length() == 0 || StaticValue.getFileServerOuterQueue().contains(httpoutUrl))
                {
                    continue;
                }
                StaticValue.getFileServerOuterQueue().add(httpoutUrl);
            }
        }

        StaticValue.setFileServerSize(StaticValue.getFileServerQueue().size());
        if(StaticValue.getFileServerSize() > 0)
        {
            StaticValue.FILE_SERVER_URL = StaticValue.getFileServerQueue().get(0);
            StaticValue.FILE_SERVER_VIEWURL = StaticValue.getFileServerOuterQueue().get(0);
            StaticValue.FILE_SERVER_OUTER_URL = StaticValue.getFileServerOuterQueue().get(0);
        }
    }

    //内存信息，下标0:总内存；1：首次超过总内存指定百分比的时间
	private static long[] MEMORY_INFO= new long[2];

	//超过内存总量百分比时间
	private static long EXCEED_MEMORY_TIME = (long)5 * 60 * 1000;

	//总内存百分比阀值
	private static double MEMORY_PERCENTAGE = 0.8;

	private static double MEMORY_PERCENTAGE_VALUE = 0.05;

	//查询运营商余额备用URL地址
	private static List<String> BALANCE_BAK_URL = new ArrayList<String>();

	//SP账号余额阀值告警线程状态 true:运行；false:停止
	private static boolean spUserBalanceAlarmState = true;

	//实时数据保留实时库时间,-1:关，其他为实时库保留实时数据的月
	private static int readDataSaveTime = -1;

	//使用历史库时间
	private static String useHistoryDBTime = "201610";

	//上行黑名单查询单次总量
	private static int moTdCount = 500;

	//使用EMP服务器URL标识，0：不使用，1：使用
	private static int useServerUrlFlag = 0;

	//EMP服务器URL
	private static String ServerUrl = "";

	//JSP文件引入版本号
	private static String JSP_IMP_VERSION = String.valueOf(System.currentTimeMillis());

	//文件服务器HTTP请求超时时间(毫秒)
	private static int FILE_HTTP_REQUEST_TIMEOUT = 10 * 1000;
	//用户的密码
	private static String DB_PASSWD=SystemGlobals.getValue("montnets.emp.password");
	//备用的用户的密码
	private static String BACK_DB_PASSWD=SystemGlobals.getValue("montnets.emp.password2");

	//查询余额失败账号
	private static Map<String, BalanceAlarmBean>	BALANCE_FAIL_ACCOUNT = new ConcurrentHashMap<String, BalanceAlarmBean>();

	//EMP线程运行状态
	private static boolean EMP_THREADRUN_STATE = true;

	//简体中文
	public static final String ZH_CN="zh_CN";
	//繁体中文
	public static final String ZH_TW="zh_TW";
	//英文
	public static final String ZH_HK="zh_HK";
	//英文
	public static final String EN_US="en_US";
	//语言设置
	public static final String LANG_KEY="emp_lang";
	//分隔符
	public static final String EXECL_SPLID = "@P9l&";


    public static String getSmsTailflag() {
        return SMS_TAILFLAG;
    }

    public static void setSmsTailflag(String smsTailflag) {
        SMS_TAILFLAG = smsTailflag;
    }

    public static String getDaoPackage() {
        return DAO_PACKAGE;
    }

    public static void setDaoPackage(String daoPackage) {
        DAO_PACKAGE = daoPackage;
    }

    public static String getDaoClass() {
        return DAO_CLASS;
    }

    public static void setDaoClass(String daoClass) {
        DAO_CLASS = daoClass;
    }

    public static StringBuffer getMenu_num() {
        return menu_num;
    }

    public static void setMenu_num(StringBuffer menu_num) {
        StaticValue.menu_num = menu_num;
    }

    public static String getFileServerUrl() {
        return FILE_SERVER_URL;
    }

    public static void setFileServerUrl(String fileServerUrl) {
        FILE_SERVER_URL = fileServerUrl;
    }

    public static String getFileServerViewurl() {
        return FILE_SERVER_VIEWURL;
    }

    public static void setFileServerViewurl(String fileServerViewurl) {
        FILE_SERVER_VIEWURL = fileServerViewurl;
    }

    public static String getFileServerOuterUrl() {
        return FILE_SERVER_OUTER_URL;
    }

    public static void setFileServerOuterUrl(String fileServerOuterUrl) {
        FILE_SERVER_OUTER_URL = fileServerOuterUrl;
    }

    public static int getFileServerIndex() {
        return FILE_SERVER_INDEX;
    }

    public static void setFileServerIndex(int fileServerIndex) {
        FILE_SERVER_INDEX = fileServerIndex;
    }

    public static int getFileServerOuterIndex() {
        return FILE_SERVER_OUTER_INDEX;
    }

    public static void setFileServerOuterIndex(int fileServerOuterIndex) {
        FILE_SERVER_OUTER_INDEX = fileServerOuterIndex;
    }

	public static String getWITHNOLOCK() {
		return WITHNOLOCK;
	}

	public static void setWITHNOLOCK(String WITHNOLOCK) {
		StaticValue.WITHNOLOCK = WITHNOLOCK;
	}

	public static int getCORPTYPE() {
		return CORPTYPE;
	}

	public static void setCORPTYPE(int CORPTYPE) {
		StaticValue.CORPTYPE = CORPTYPE;
	}

	public static String getPassCount() {
		return PASS_COUNT;
	}

	public static String getPassCombtype() {
		return PASS_COMBTYPE;
	}

	public static String getPassUpcycle() {
		return PASS_UPCYCLE;
	}

	public static String getPassPastalarm() {
		return PASS_PASTALARM;
	}

	public static String getPassErrlimit() {
		return PASS_ERRLIMIT;
	}

	public static String getPassDynpwd() {
		return PASS_DYNPWD;
	}

	public static Map<String, LoginInfo> getLoginInfoMap() {
		return loginInfoMap;
	}

	public static Map<String, String> getInniMenuMap() {
		return inniMenuMap;
	}

	public static void setInniMenuMap(Map<String, String> inniMenuMap) {
		StaticValue.inniMenuMap = inniMenuMap;
	}

	public static String getServerNumber() {
		return SERVER_NUMBER;
	}

	public static void setServerNumber(String serverNumber) {
		SERVER_NUMBER = serverNumber;
	}

	public static int getISCLUSTER() {
		return ISCLUSTER;
	}

	public static void setISCLUSTER(int ISCLUSTER) {
		StaticValue.ISCLUSTER = ISCLUSTER;
	}

	public static List<String> getFileServerQueue() {
		return FILE_SERVER_QUEUE;
	}

	public static void setFileServerQueue(List<String> fileServerQueue) {
		FILE_SERVER_QUEUE = fileServerQueue;
	}

	public static List<String> getFileServerOuterQueue() {
		return FILE_SERVER_OUTER_QUEUE;
	}

	public static void setFileServerOuterQueue(List<String> fileServerOuterQueue) {
		FILE_SERVER_OUTER_QUEUE = fileServerOuterQueue;
	}

	public static int getFileServerSize() {
		return FILE_SERVER_SIZE;
	}

	public static void setFileServerSize(int fileServerSize) {
		FILE_SERVER_SIZE = fileServerSize;
	}

	public static long getFileServerCheckAliveTime() {
		return FILE_SERVER_CHECK_ALIVE_TIME;
	}

	public static void setFileServerCheckAliveTime(long fileServerCheckAliveTime) {
		FILE_SERVER_CHECK_ALIVE_TIME = fileServerCheckAliveTime;
	}

	public static long getFileServerOuterCheckAliveTime() {
		return FILE_SERVER_OUTER_CHECK_ALIVE_TIME;
	}

	public static void setFileServerOuterCheckAliveTime(long fileServerOuterCheckAliveTime) {
		FILE_SERVER_OUTER_CHECK_ALIVE_TIME = fileServerOuterCheckAliveTime;
	}

	public static String getEmpVersion() {
		return EMP_VERSION;
	}

	public static void setEmpVersion(String empVersion) {
		EMP_VERSION = empVersion;
	}

	/**
	 * 网讯是否运营商审核,0表示运营商不审核，1表示运营商审核。
	 */
	public static int getIsWxOperatorReview() {
		return IS_WX_OPERATOR_REVIEW;
	}

	public static void setIsWxOperatorReview(int isWxOperatorReview) {
		IS_WX_OPERATOR_REVIEW = isWxOperatorReview;
	}

	public static String getMqServerUrl() {
		return MQ_SERVER_URL;
	}

	public static void setMqServerUrl(String mqServerUrl) {
		MQ_SERVER_URL = mqServerUrl;
	}

	public static String getMqQueueName() {
		return MQ_QUEUE_NAME;
	}

	public static void setMqQueueName(String mqQueueName) {
		MQ_QUEUE_NAME = mqQueueName;
	}

	public static LfMonOnlcfg getMonOnlinecfg() {
		return MON_ONLINECFG;
	}

	public static void setMonOnlinecfg(LfMonOnlcfg monOnlinecfg) {
		MON_ONLINECFG = monOnlinecfg;
	}

	public static String getBirthwishNameSignLeft() {
		return BIRTHWISH_NAME_SIGN_LEFT;
	}

	public static void setBirthwishNameSignLeft(String birthwishNameSignLeft) {
		BIRTHWISH_NAME_SIGN_LEFT = birthwishNameSignLeft;
	}

	public static String getBirthwishNameSignRight() {
		return BIRTHWISH_NAME_SIGN_RIGHT;
	}

	public static void setBirthwishNameSignRight(String birthwishNameSignRight) {
		BIRTHWISH_NAME_SIGN_RIGHT = birthwishNameSignRight;
	}

//	public static int getHttpRequestTimeout() {
//		return HTTP_REQUEST_TIMEOUT;
//	}
//
//	public static void setHttpRequestTimeout(int httpRequestTimeout) {
//		HTTP_REQUEST_TIMEOUT = httpRequestTimeout;
//	}

//	public static int getHttpResponseTimeout() {
//		return HTTP_RESPONSE_TIMEOUT;
//	}
//
//	public static void setHttpResponseTimeout(int httpResponseTimeout) {
//		HTTP_RESPONSE_TIMEOUT = httpResponseTimeout;
//	}

//	public static String getMbossKey() {
//		return MBOSS_KEY;
//	}
//
//	public static void setMbossKey(String mbossKey) {
//		MBOSS_KEY = mbossKey;
//	}

	public static long getBalanceRequestInterval() {
		return BALANCE_REQUEST_INTERVAL;
	}

	public static void setBalanceRequestInterval(long balanceRequestInterval) {
		BALANCE_REQUEST_INTERVAL = balanceRequestInterval;
	}

//	public static int getBalanceMsgEncryflag() {
//		return BALANCE_MSG_ENCRYFLAG;
//	}
//
//	public static void setBalanceMsgEncryflag(int balanceMsgEncryflag) {
//		BALANCE_MSG_ENCRYFLAG = balanceMsgEncryflag;
//	}

	public static String getMbossWebservicesUrl() {
		return MBOSS_WEBSERVICES_URL;
	}

	public static void setMbossWebservicesUrl(String mbossWebservicesUrl) {
		MBOSS_WEBSERVICES_URL = mbossWebservicesUrl;
	}

	public static int getSPTYPEFLAG() {
		return SPTYPEFLAG;
	}

	public static void setSPTYPEFLAG(int SPTYPEFLAG) {
		StaticValue.SPTYPEFLAG = SPTYPEFLAG;
	}

	public static long getLogPrintInterval() {
		return LOG_PRINT_INTERVAL;
	}

	public static void setLogPrintInterval(long logPrintInterval) {
		LOG_PRINT_INTERVAL = logPrintInterval;
	}

	public static long getMonlogPrintInterval() {
		return MONLOG_PRINT_INTERVAL;
	}

	public static void setMonlogPrintInterval(long monlogPrintInterval) {
		MONLOG_PRINT_INTERVAL = monlogPrintInterval;
	}

	public static long getBlackMaxcount() {
		return BLACK_MAXCOUNT;
	}

	public static void setBlackMaxcount(long blackMaxcount) {
		BLACK_MAXCOUNT = blackMaxcount;
	}

	/*****************/
	public static long getTMPEDITORFLAG() {
		return TMPEDITORFLAG;
	}

	public static void setTMPEDITORFLAG(long TMPEDITORFLAG) {
		StaticValue.TMPEDITORFLAG = TMPEDITORFLAG;
	}

	public static int getISCLIENTMONFILE() {
		return ISCLIENTMONFILE;
	}

	public static void setISCLIENTMONFILE(int ISCLIENTMONFILE) {
		StaticValue.ISCLIENTMONFILE = ISCLIENTMONFILE;
	}

	public static int getServerSystemType() {
		return SERVER_SYSTEM_TYPE;
	}

	public static void setServerSystemType(int serverSystemType) {
		SERVER_SYSTEM_TYPE = serverSystemType;
	}

	public static long[] getMemoryInfo() {
		return MEMORY_INFO;
	}

	public static void setMemoryInfo(long[] memoryInfo) {
		MEMORY_INFO = memoryInfo;
	}

	public static long getExceedMemoryTime() {
		return EXCEED_MEMORY_TIME;
	}

	public static void setExceedMemoryTime(long exceedMemoryTime) {
		EXCEED_MEMORY_TIME = exceedMemoryTime;
	}

	public static double getMemoryPercentage() {
		return MEMORY_PERCENTAGE;
	}

	public static void setMemoryPercentage(double memoryPercentage) {
		MEMORY_PERCENTAGE = memoryPercentage;
	}

	public static double getMemoryPercentageValue() {
		return MEMORY_PERCENTAGE_VALUE;
	}

	public static void setMemoryPercentageValue(double memoryPercentageValue) {
		MEMORY_PERCENTAGE_VALUE = memoryPercentageValue;
	}

	public static List<String> getBalanceBakUrl() {
		return BALANCE_BAK_URL;
	}

	public static void setBalanceBakUrl(List<String> balanceBakUrl) {
		BALANCE_BAK_URL = balanceBakUrl;
	}

	public static boolean isSpUserBalanceAlarmState() {
		return spUserBalanceAlarmState;
	}

	public static void setSpUserBalanceAlarmState(boolean spUserBalanceAlarmState) {
		StaticValue.spUserBalanceAlarmState = spUserBalanceAlarmState;
	}

	public static int getReadDataSaveTime() {
		return readDataSaveTime;
	}

	public static void setReadDataSaveTime(int readDataSaveTime) {
		StaticValue.readDataSaveTime = readDataSaveTime;
	}

	public static String getUseHistoryDBTime() {
		return useHistoryDBTime;
	}

	public static void setUseHistoryDBTime(String useHistoryDBTime) {
		StaticValue.useHistoryDBTime = useHistoryDBTime;
	}

	public static int getMoTdCount() {
		return moTdCount;
	}

	public static void setMoTdCount(int moTdCount) {
		StaticValue.moTdCount = moTdCount;
	}

	public static int getUseServerUrlFlag() {
		return useServerUrlFlag;
	}

	public static void setUseServerUrlFlag(int useServerUrlFlag) {
		StaticValue.useServerUrlFlag = useServerUrlFlag;
	}

	public static String getServerUrl() {
		return ServerUrl;
	}

	public static void setServerUrl(String serverUrl) {
		ServerUrl = serverUrl;
	}

	public static String getJspImpVersion() {
		return JSP_IMP_VERSION;
	}

	public static void setJspImpVersion(String jspImpVersion) {
		JSP_IMP_VERSION = jspImpVersion;
	}

	public static int getFileHttpRequestTimeout() {
		return FILE_HTTP_REQUEST_TIMEOUT;
	}

	public static void setFileHttpRequestTimeout(int fileHttpRequestTimeout) {
		FILE_HTTP_REQUEST_TIMEOUT = fileHttpRequestTimeout;
	}

	public static Map<String, BalanceAlarmBean> getBalanceFailAccount() {
		return BALANCE_FAIL_ACCOUNT;
	}

	public static void setBalanceFailAccount(Map<String, BalanceAlarmBean> balanceFailAccount) {
		BALANCE_FAIL_ACCOUNT = balanceFailAccount;
	}

	public static boolean isEmpThreadrunState() {
		return EMP_THREADRUN_STATE;
	}

	public static void setEmpThreadrunState(boolean empThreadrunState) {
		EMP_THREADRUN_STATE = empThreadrunState;
	}

	public static String getDbPasswd() {
		return DB_PASSWD;
	}

	public static void setDbPasswd(String dbPasswd) {
		DB_PASSWD = dbPasswd;
	}

	public static String getBackDbPasswd() {
		return BACK_DB_PASSWD;
	}

	public static void setBackDbPasswd(String backDbPasswd) {
		BACK_DB_PASSWD = backDbPasswd;
	}
}
