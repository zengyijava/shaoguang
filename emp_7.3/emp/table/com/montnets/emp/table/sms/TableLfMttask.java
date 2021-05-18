package com.montnets.emp.table.sms;

import java.util.HashMap;
import java.util.Map;

/**
 * 下行短信任务表
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:16
 * @description
 */
public class TableLfMttask
{
	//表名_下行短信任务
	public static final String TABLE_NAME = "LF_MTTASK";
	//标识列
	public static final String MT_ID = "MT_ID";
	//操作员ID
	public static final String USER_ID = "USER_ID";
	//主题
	public static final String TITLE = "TITLE";
	//短信内容
	public static final String MSG = "MSG";
	//相同内容是1，不同内容是2，动态模板是3
	public static final String MSG_TYPE = "MSG_TYPE";
	//发送开始时间
	public static final String BIGIN_TIME = "BIGINTIME";
	//发送结束时间
	public static final String END_TIME = "ENDTIME";
	//优先级(0表示优先处理，1-9表示按优先及别处理，1是最优，9是最后)
	public static final String SEND_LEVEL = "SENDLEVEL";
	//提交时间
	public static final String SUBMIT_TIME = "SUBMITTIME";
	//任务说明
	public static final String TASK_NAME = "TASKNAME";
	//提交状态(创建中1，提交2，撤销3,冻结4)
	public static final String SUB_STATE = "SUB_STATE";
	//审批状态(无需审批:0，未审批:-1，同意:1，拒绝:2)
	public static final String RE_STATE = "RE_STATE";
	//0是未发送，1是已发送,2发送失败,3(未使用),  4发送中,5超时未发送
	public static final String SEND_STATE = "SENDSTATE";
	//已达到审批级数(从1级开始，一共5级，1级：已审批了1级，2级：已审批了1级和2级，如此类推，一直到5级)
	public static final String RE_LEVEL = "RE_LEVEL";
	//提交数
	public static final String SUB_COUNT = "SUB_COUNT";
	//有效号码总数
	public static final String EFF_COUNT = "EFF_COUNT";
	//成功发送总数
	public static final String SUC_COUNT = "SUC_COUNT";
	//失败总数
	public static final String FAI_COUNT = "FAI_COUNT";
	//群发类型（相同1，不同2，动模3）
	public static final String BMT_TYPE = "BMTTYPE";
	//内容
	public static final String CONTENT = "CONTENT";
	//号码文件地址
	public static final String MOBILE_URL = "MOBILE_URL";
	//（文件上传1或手工输入0）
	public static final String MOBILE_TYPE = "MOBILE_TYPE";
	//内容类型-0直接输入，1用txt上传
	public static final String TXT_TYPE = "TXT_TYPE";
	//备注
	public static final String COMMENTS = "COMMENTS";
	//SP账号
	public static final String SP_USER = "SP_USER";
	
	public static final String STAFF_NAME = "STAFFNAME";
	//SP账号密码
	public static final String SP_PASS = "SP_PWD";
	//发送短信总数(网关发送总数)
	public static final String ICOUNT = "ICOUNT";
	//信息类型 1-短信， 2-彩信，5-移动财务 6-网讯
	public static final String MS_TYPE = "MS_TYPE";
	//短信定时发送时间
	public static final String TIMER_TIME = "TIMER_TIME";
	//是否定时发送  1-是 0-否
	public static final String TIMER_STATUS = "TIMER_STATUS";
	//业务编码
	public static final String BUS_CODE = "BUS_CODE";
	//用来存放参数
	public static final String PARAMS = "PARAMS";
	//企业编码
	public static final String CORP_CODE = "CORP_CODE";
	//错误编码
	public static final String ERROR_CODES = "ERROR_CODES";
	
	public static final String SEQUENCE = "S_LF_MTTASK";
	//是否回复
	public static final String ISREPLY  = "ISREPLY";
	//通道号
	public static final String SPNUMBER = "SPNUMBER";
	//尾号
	public static final String SUBNO = "SUBNO";
	//是否重发  1-已重发 0-未重发
	public static final String ISRETRY = "ISRETRY";
	//真正失败数
	public static final String RFAIL2 = "RFAIL2";
	//未反数
	public static final String RNRET = "RNRET";
	 //提交总数
	public static final String ICOUNT2 = "ICOUNT2";
	//任务Id
	public static final String TASKID = "TASKID";
	//模板ID
	public static final String TEMP_ID = "TEMP_ID";
	//模板参数个数
	public static final String PARAM_COUNT = "PARAM_COUNT";
	
	//模板路径
	public static final String TMPL_PATH="TMPL_PATH";
	
	//批量任务ID
	public static final String BATCHID="BATCHID";
	
	//发送类型
	public static final String TASKTYPE="TASKTYPE";
	
	public static final String WYSENDINFO = "WYSENDINFO";
	//发送文件URL
	public static final String FILEURI = "FILEURI";
	
	public static final String MSGEDCODETYPE = "MSGEDCODETYPE";
	
	public static final String VALIDTM = "VALIDTM";

	public static final String TEMPTYPE = "TEMP_TYPE";

	public static final String PHONENUUM = "PHONENUM";

	public static final String SENDNUM = "SENDNUM";

	public static final String FINISHTIME = "FINISHTIME";

	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfMttask", TABLE_NAME);
		columns.put("tableId", MT_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("mtId", MT_ID);
		columns.put("userId", USER_ID);
		columns.put("title", TITLE);
		columns.put("msg", MSG);
		columns.put("msgType", MSG_TYPE);
		columns.put("biginTime", BIGIN_TIME);
		columns.put("endTime", END_TIME);
		columns.put("sendLevel", SEND_LEVEL);
		columns.put("submitTime", SUBMIT_TIME);
		columns.put("taskName", TASK_NAME);
		columns.put("subState", SUB_STATE);
		columns.put("reState", RE_STATE);
		columns.put("sendstate", SEND_STATE);
		columns.put("reLevel", RE_LEVEL);
		columns.put("subCount", SUB_COUNT);
		columns.put("effCount", EFF_COUNT);
		columns.put("sucCount", SUC_COUNT);
		columns.put("faiCount", FAI_COUNT);
		columns.put("bmtType", BMT_TYPE);
		columns.put("content", CONTENT);
		columns.put("mobileUrl", MOBILE_URL);
		columns.put("mobileType", MOBILE_TYPE);
		columns.put("txtType", TXT_TYPE);
		columns.put("comments", COMMENTS);
		columns.put("spUser", SP_USER);
		columns.put("spPwd", SP_PASS);
		columns.put("icount", ICOUNT);
		columns.put("msType", MS_TYPE);
		columns.put("timerTime", TIMER_TIME);
		columns.put("timerStatus", TIMER_STATUS);
		columns.put("busCode", BUS_CODE);
		columns.put("params", PARAMS);
		columns.put("corpCode", CORP_CODE);
		columns.put("errorCodes", ERROR_CODES);
		columns.put("isReply", ISREPLY);
		columns.put("spNumber", SPNUMBER);
		columns.put("subNo", SUBNO);
		columns.put("isRetry", ISRETRY);
		columns.put("rfail2", RFAIL2);
		columns.put("rnret", RNRET);
		columns.put("icount2", ICOUNT2);
		columns.put("taskId", TASKID);
		columns.put("tempid", TEMP_ID);
		columns.put("paramcount", PARAM_COUNT);
		columns.put("tmplPath", TMPL_PATH);
		columns.put("batchID", BATCHID);
		columns.put("taskType", TASKTYPE);
		columns.put("wySendInfo", WYSENDINFO);
		columns.put("fileuri", FILEURI);
		columns.put("msgedcodetype", MSGEDCODETYPE);
		columns.put("validtm", VALIDTM);
		columns.put("tempType", TEMPTYPE);
		columns.put("phoneNum",PHONENUUM);
		columns.put("sendNum",SENDNUM);
		columns.put("finishTime",FINISHTIME);
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM()
	{
		return columns;
	}

}
