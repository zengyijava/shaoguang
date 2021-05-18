package com.montnets.emp.table.sms;

import com.montnets.emp.common.constant.StaticValue;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:46
 * @description
 */
public class TableMtTask
{

	public static final String TABLE_NAME = "MT_TASK";

	public static final String ID = "ID";

	public static final String PT_MSG_ID = "PTMSGID";

	public static final String UID = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE?"UID":"\"UID\"";

	public static final String ECID = "ECID";

	public static final String USER_ID = "USERID";

	public static final String SPGATE = "SPGATE";

	public static final String CPNO = "CPNO";

	public static final String PHONE = "PHONE";

	public static final String SP_MSG_ID = "SPMSGID";

	public static final String RET_FLAG = "RETFLAG";

	public static final String FEE_FLAG = "FEEFLAG";

	public static final String PK_NUMBER = "PKNUMBER";

	public static final String PK_TOTAL = "PKTOTAL";

	public static final String SEND_STATUS = "SENDSTATUS";

	public static final String SEND_FLAG = "SENDFLAG";

	public static final String RECV_FLAG = "RECVFLAG";

	public static final String DONE_DATE = "DONEDATE";

	public static final String ERROR_CODE = "ERRORCODE";

	public static final String SEND_LEVEL = "SENDLEVEL";

	public static final String SEND_TYPE = "SENDTYPE";

	public static final String UNICOM = "UNICOM";

	public static final String SEND_TIME = "SENDTIME";

	public static final String RECV_TIME = "RECVTIME";

	public static final String MESSAGE = "MESSAGE";

	public static final String RE_SEND_CNT = "RESENDCNT";

	public static final String TASK_ID = "TASKID";

	public static final String MSGFMT = "MSGFMT";
	
	public static final String SVRTYPE = "SVRTYPE";
	
	public static final String BATCHID = "BATCHID";
	
	public static final String SEQUENCE = "MT_TASK_ID";

    protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("MtTask", TABLE_NAME);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("ptMsgId", PT_MSG_ID);
		columns.put("uid", UID);
		columns.put("ecid", ECID);
		columns.put("userId", USER_ID);
		columns.put("spgate", SPGATE);
		columns.put("cpno", CPNO);
		columns.put("phone", PHONE);
		columns.put("spMsgId", SP_MSG_ID);
		columns.put("retFlag", RET_FLAG);
		columns.put("feeFlag", FEE_FLAG);
		columns.put("pknumber", PK_NUMBER);
		columns.put("pktotal", PK_TOTAL);
		columns.put("sendStatus", SEND_STATUS);
		columns.put("sendFlag", SEND_FLAG);
		columns.put("recvFlag", RECV_FLAG);
		columns.put("doneDate", DONE_DATE);
		columns.put("errorCode", ERROR_CODE);
		columns.put("sendLevel", SEND_LEVEL);
		columns.put("sendType", SEND_TYPE);
		columns.put("unicom", UNICOM);
		columns.put("sendTime", SEND_TIME);
		columns.put("recvTime", RECV_TIME);
		columns.put("message", MESSAGE);
		columns.put("resendcnt", RE_SEND_CNT);
		columns.put("taskId", TASK_ID);
		columns.put("msgfmt", MSGFMT);
		columns.put("svrtype", SVRTYPE);
		columns.put("batchID", BATCHID);
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
