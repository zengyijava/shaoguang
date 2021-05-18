package com.montnets.emp.ottbase.constant;

import com.montnets.emp.common.constant.LoginInfo;
import com.montnets.emp.common.constant.SystemGlobals;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class WXStaticValue {

    protected static final Properties systemProperty = System.getProperties();

    public static Properties getSystemProperty() {
        return systemProperty;
    }

    public static final String LINE_SEPARATOR = "line.separator";

    public static final String FILEDIRNAME = "file/smstxt/";
    //excel模板目录
    public static final String EXCEL_FILE = "file/excel/";
    public static final String MANUAL_SMSTXT = "file/smstxt/manualsmstxt/";

    public static final String JSPXML_FILE = "file/jspxmlfile/";
    //excel文件模板
    public static final String FILE_UPLOAD_PATH = "file/excel/temp/";

    //微信图文
    public static final String WEIX_RIMG = "file/weix/rimg/";

    //微信账户
    public static final String WEIX_ACCOUNT = "file/weix/account/";
    //微信多媒体图片
    public static final String WEIX_MEDIA_IMG = "file/weix/media/img/";
    //微信多媒体声音
    public static final String WEIX_MEDIA_SOUND = "file/weix/media/sound/";
    //微信多媒体视频
    public static final String WEIX_MEDIA_VIDEO = "file/weix/media/video/";
    //微信二维码
    public static final String WEIX_QRCODE = "file/weix/qrcode/";
    //存放LBS的图片文件
    public static final String FILE_LBS_IMG = "file/weix/lbs/";
    //微信关注者头像图片本地存放地址
    public static final String WEIX_USER = "file/weix/user/img/";
    //微信请求文件信息
    public static final String WEIX_REQUEST = "file/weix/request/";
    //微信返回信息
    public static final String WEIX_RESPONSE = "file/weix/response/";
    //微信请求HTTP请求地址
    public static final String WEIX_HTTPREQUEST = "file/weix/httprequest/";
    //微站表单存放地址
    public static final String WZGL_FORMHTML_URL = "file/wzgl/formhtml/";
    //群发的日志存放地址
    public static final String QUNFA_REQUEST_URL = "file/weix/qunfa/request/";

    //微信多媒体图片
    public static final String WZGL_IMG = "file/wzgl/img/";

    public static final String MANUALSMSTXT = "manualsmstxt/";

    public static final String SERVICESMSTXT = "servicesmstxt/";

    public static final String MOBILEBUSINESSTXT = "mobilebussinesstxt/";

    public static final String MMS_WEBSERVICE_URL = "mms.webservice.url";

    public static final String EMP_POOLNAME = "emp";

    public static final String SMSACC_POOLNAME = "ott";

    public static final String SMSSVR_POOLNAME = "ott";

    public static final String DB_CACHE_SWITCH_OPEN = "1";

    public static final String ASC = "ASC";

    public static final String DESC = "DESC";

    public static final String IN = "in";

    public static final String NOT_IN = "not in";

    public static final String LIKE = "like";

    public static final String LIKE1 = "like1";

    public static final String LIKE2 = "like2";

    public static final String BETWEEN = "between";

    protected static Map<String, String> titleMap;

    public static final String DB_ACCESS_DRIVER = "database.access.driver.implementation";

    public static final String DB_CACHE_SWITCH = "database.cache.switch";

    public static final String DB_CACHE_REFRESH_TIME_HOUR = "database.cache.refresh.time.hour";

    public static final String DB_CACHE_REFRESH_TIME_MINUTE = "database.cache.refesh.time.minute";

    public static final String DB_JOB_TIME = "database.job.time";

    public static final String OTT_WEB_ROOT = "ott.web.root";

    public static final String OTT_WEB_DEFAULT = "ott.web.default";

    public static final String OTT_WEB_VERSION = "ott.web.version";

    public static final String OTT_LOGIN_YANZHENGMA = "ott.login.yanzhengma";

    public static final String THIRD_PARTY_TIME = "thridparty.syncinfo.addrbook";

    public static final String OTT_LOG_SAVEFILTERLEVEL = "ott.log.saveFilterLevel";

    public static final String OTT_LOG_PRINTFILTERLEVEL = "ott.log.printFilterLevel";

    public static final String OTT_LOG_BUFFERSIZE = "ott.log.bufferSize";

    public static final String OTT_LOG_DEVMODE = "ott.log.devMode";

    public static final String OTT_LOG_SHOW_SQL = "ott.log.show_sql";
    //数据源连接池的种类。
    public static final String POOL_TYPE = "poolType";

    public static final String OTT_PAGEINFO_DEFAULTPAGESIZE = "ott.pageInfo.defaultPageSize";

    public static final String SESSION_USER_KEY = "loginSysuser";


    public static final String ENCODING = "UTF-8";

    public static final String CONTENT_TYPE_TEXT_HTML = "text/html;charset=UTF-8";

    public static final String DATE_FORMAT = "yyyy-MM-dd  HH:mm:ss";

    public static final String ORACLR_DATE_FORMAT = "yyyy-MM-dd HH24:mi:ss";

    public static final String IM_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final int DEFAULT_PAGE_SIZE = 10;


    public static final String SESSION_KEY_UPDATE_MTTASK = "updateMattaskPercentage";

    public static final String FLOW_TYPE = "1";

    public static final String TIMESTAMP = "class java.sql.Timestamp";

    public static final String DATE_SQL = "class java.sql.Date";

    public static final String DATE_UTIL = "class java.util.Date";

    public static final String WEB_FRAME = "ott.web.frame";

    public static final int MOBILE_TYPE = 0;

    public static final int PC_TYPE = 1;

    public static final String OTT_LOGIN_ISMULTI = "ott.login.isMulti";

    public static final String SGCC_SERIAL_NUMBER = "sgcc.serial.number";

    public static final String CONTRACT_CREATE_VALUE = "sgcc.contract.create.value";

    public static final String ORDER_CREATE_VALUE = "sgcc.order.create.value";

    private static int CORPTYPE = 0;

    public static final String CODE1 = "F7675CB5CA896F8BE68EBD9E73383895";

    public static final String CODE2 = "9E635674C8700758A0335A6C755BDF45";

    public static int getCORPTYPE() {
        return CORPTYPE;
    }

    static {
        String enterprise = SystemGlobals.getValue("montnets.enterprise");
        if (CODE1.equals(enterprise)) {
            CORPTYPE = 0;
        } else if (CODE2.equals(enterprise)) {
            CORPTYPE = 1;
        }
    }

    public static final int DBTYPE = Integer.parseInt(SystemGlobals
            .getValue("DBType"));

    public static final int ORACLE_DBTYPE = 1;

    public static final int SQLSERVER_DBTYPE = 2;

    public static final int MYSQL_DBTYPE = 3;

    public static final int DB2_DBTYPE = 4;

    public static final String LOGOPATH = "/images/logo/";


    //备用服务器用的数据库连接池
    public static final String OTT_BACKUP = "OTT_BACKUP";
    //taskId取值数量
    public static final int MAXTASKIDCOUNT = 100;

    //是否使用备用服务器 1-使用 ；0-不使用
    public static final String USE_BACKUP_SERVER = "montnets.ott.use_backup_server";

    private static String WITHNOLOCK = "";

    public static String getWITHNOLOCK() {
        return WITHNOLOCK;
    }

    static {
        if (DBTYPE == SQLSERVER_DBTYPE) {
            WITHNOLOCK = " with(nolock) ";
        }
    }

    public static final String SMS_FEE_WEBSERVICE_URL = "sms.fee.webservice.url";

    //控制sql语句in查询的最大值
    public static final int inConditionMax = 1000;
    // 机构最大级数
    public static final String DEP_MAXLEVEL = "dep.maxlevel";
    //同级最大机构数
    public static final String DEP_MAXCHILD = "dep.maxchild";
    //最大机构数量
    public static final String DEP_MAXDEP = "dep.maxdep";
    public static final String MAX_CHARGE_DEP_LEVEL = "dep.maxchargelevel";
    // 机构最大级数
    public static final Integer DEP_MAXLEVEL_COUNT = 10;
    //同级最大机构数
    public static final Integer DEP_MAXCHILD_COUNT = 100;
    //最大机构数量
    public static final Integer DEP_MAXDEP_COUNT = 10000;
    //机构最大充值级数
    public static final Integer MAX_CHARGE_DEP = 5;
    //登录安全初始设置
    public static final Integer SECURITY_VAL = 0;


    //一个机构下员工或者操作员的最大数目
    public static final int MAX_PEOPLE_COUNT = 50000;

    //服务器登录信息变量
    protected static final Map<String, LoginInfo> loginInfoMap = new HashMap<String, LoginInfo>();


    //单企业企业编码
    public static final String UPWS_CORPCODE = "100001";

    //号码是否可见权限编码
    public static final String PHONE_LOOK_CODE = "1600-2000-0";

    //投诉建议反馈地址
    public static final String SUGGEST_REPORT_URL = "suggest.report.url";

    private static String DAO_PACKAGE = "";

    private static String DAO_CLASS = "";

    public static String getDaoPackage() {
        return DAO_PACKAGE;
    }

    public static String getDaoClass() {
        return DAO_CLASS;
    }

    //存放要加载的模块map
    protected static final Map<String, String> inniMenuMap = new HashMap<String, String>();

    //要存放的大模块id
    public final static StringBuffer menu_num = new StringBuffer("");

    static {
        if (DBTYPE == SQLSERVER_DBTYPE) {
            DAO_PACKAGE = "sqlserver";
            DAO_CLASS = "SQLServer";
        } else if (DBTYPE == ORACLE_DBTYPE) {
            DAO_PACKAGE = "oracle";
            DAO_CLASS = "Oracle";
        } else if (DBTYPE == DB2_DBTYPE) {
            DAO_PACKAGE = "db2";
            DAO_CLASS = "DB2";
        } else if (DBTYPE == MYSQL_DBTYPE) {
            DAO_PACKAGE = "mysql";
            DAO_CLASS = "MySQL";
        }
    }

    //服务器编号 为数字1001,1002,1003,1004...
    public static final String SERVER_NUMBER = SystemGlobals.getValue("cluster.server.number");

    //是否集群  1代表集群  0代表不集群 默认为0
    public static final int ISCLUSTER = Integer.parseInt(SystemGlobals.getValue("cluster.iscluster"));

    //文件服务器地址
    public static final String FILE_SERVER_URL = SystemGlobals.getValue("montnets.fileserver.innerUrl");
    //文件服务器外网地址
    public static final String FILE_SERVER_VIEWURL = SystemGlobals.getValue("montnets.fileserver.outerUrl");
    //网关请求地址
    public static final String WEBGATE_URL = SystemGlobals.getValue("montnets.webgate");

    //是否运行定时任务，1代表运行，0代表不运行，默认运行。
    public static final int ISRUNTIMEJOB = Integer.parseInt(SystemGlobals.getValue("cluster.isruntimejob"));

    public static final String OTT_VERSION = SystemGlobals.getValue(OTT_WEB_VERSION);//"1.0.0.0";

    /**
     * 压缩文件大小
     */
    public static final long ZIP_SIZE = 10 * 1024L * 1024L;
    /**
     * 最大大小
     */
    public static final long MAX_SIZE = 100 * 1024L * 1024L;
    /**
     * 最大号码数
     */
    public static final long MAX_PHONE_NUM = 5000000;
    /**
     * 每次写号码数
     */
    public static final long PER_PHONE_NUM = 10000;

    /**
     * 返回异常值
     */
    public static final int EXP_RETURN = -9999;

    /**
     * 重新同步内存值的时间间隔
     */
    public static final int SYNC_TIMER_INTERVAL = 10 * 60 * 1000;

    /**
     * 号码文件参数分隔符
     */
    public static final String MSG_SPLIT_CHAR = ",";

    /**
     * 最大短信内容字数
     */
    public static final int MAX_MSG_LEN = 990;

    /**
     * 默认框架
     */
    public static final String DEFAULT_FRAME = SystemGlobals.getValue(WXStaticValue.WEB_FRAME);


    /**
     *   微信静态变量
     */
    /**
     * 返回JSON格式
     */
    public static final String RETURNHTTP_JSON = "JSON";
    /**
     * 返回XML格式
     */
    public static final String WX_RETURNHTTP_XML = "XML";
    /**
     * FILE格式  返回字符串
     */
    public static final String WX_RETURNHTTP_FILE = "FILE";
    /**
     * POST请求JSON格式
     */
    public static final String WX_HTTPPOSTTYPE_MSG = "MSG";
    /**
     * POST请求文件
     */
    public static final String WX_HTTPPOSTTYPE_FILE = "FILE";
    /**
     * 二维码类型，QR_SCENE为临时
     */
    public static final String WX_SCENE_ACTION_NAME = "QR_SCENE";
    /**
     * QR_LIMIT_SCENE为永久
     */
    public static final String WX_LIMIT_SCENE_ACTION_NAME = "QR_LIMIT_SCENE";
    /**
     * 百度地图AK密钥
     */
    public static final String BAIDU_MAP_AK = SystemGlobals.getValue("montnets.baidumap.ak");

    /**
     * 未分组名字
     */
    public static final String NO_GROUP_NAME = "未分组";
}
