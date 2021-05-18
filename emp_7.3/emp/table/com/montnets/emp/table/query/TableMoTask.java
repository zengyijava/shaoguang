package com.montnets.emp.table.query;

import com.montnets.emp.common.constant.StaticValue;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:39
 * @description
 */
public class TableMoTask
{

	public static final String TABLE_NAME = "MO_TASK";

	public static final String ID = "ID";

	public static final String PT_MSG_ID = "PTMSGID";

	public static final String UID = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE?"UID":"\"UID\"";

	public static final String ORG_UID = "ORGUID";

	public static final String ECID = "ECID";

	public static final String USER_ID = "USERID";

	public static final String SPNUMBER = "SPNUMBER";

	public static final String SERVICE_ID = "SERVICEID";

	public static final String SEND_STATUS = "SENDSTATUS";

	public static final String MSG_FMT = "MSGFMT";

	public static final String TP_PID = "TP_PID";

	public static final String TP_UDHI = "TP_UDHI";

	public static final String DELIVER_TIME = "DELIVERTIME";

	public static final String PHONE = "PHONE";

	public static final String MSG_CONTENT = "MSGCONTENT";

	public static final String DONE_TIME = "DONETIME";

	public static final String SEQUENCE = "MO_TASK_ID";
	//-------------------GUODW
	public static final String UNICOM = "UNICOM";
	
	public static final String PKNUMBER="PKNUMBER";
	
	public static final String PKTOTAL="PKTOTAL";
    //-------------------------------------
    protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("MoTask", TABLE_NAME);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("ptMsgId", PT_MSG_ID);
		columns.put("uid", UID);
		columns.put("orgUid", ORG_UID);
		columns.put("ecid", ECID);
		columns.put("userId", USER_ID);
		columns.put("spnumber", SPNUMBER);
		columns.put("serviceId", SERVICE_ID);
		columns.put("sendStatus", SEND_STATUS);
		columns.put("msgFmt", MSG_FMT);
		columns.put("tpPid", TP_PID);
		columns.put("tpUdhi", TP_UDHI);
		columns.put("deliverTime", DELIVER_TIME);
		columns.put("phone", PHONE);
		columns.put("msgContent", MSG_CONTENT);
		columns.put("doneTime", DONE_TIME);
		columns.put("pknumber", PKNUMBER);
		columns.put("pktotal", PKTOTAL);
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
