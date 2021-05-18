package com.montnets.emp.table.system;

import java.util.HashMap;
import java.util.Map;

public class TableMoWaitA {
	
	public static final String TABLE_NAME = "MO_WAIT_A";

	public static final String ID = "ID";
	
	public static final String ECID = "ECID";

	public static final String USERUID = "USERUID";
	
	public static final String LOGINUID = "LOGINUID";

	public static final String USERID = "USERID";

	public static final String PTMSGID = "PTMSGID";

	public static final String SPNUMBER = "SPNUMBER";

	public static final String PHONE = "PHONE";

	public static final String DELIVERTIME = "DELIVERTIME";

	public static final String MESSAGE = "MESSAGE";

	public static final String UNICOM = "UNICOM";

	public static final String TP_UDHI = "TP_UDHI";

	public static final String TP_PID = "TP_PID";

	public static final String LONGMSGSEQ = "LONGMSGSEQ";

	public static final String SERVICEID = "SERVICEID";

	public static final String SPGATE = "SPGATE";
	
	public static final String MSGFMT = "MSGFMT";

	public static final String PKNUMBER = "PKNUMBER";

	public static final String PKTOTAL = "PKTOTAL";

	public static final String PTNOTICE = "PTNOTICE";
	
	public static final String CPNO = "CPNO";
	
	protected final static Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("MoWaitA", TABLE_NAME);
		/*columns.put("sequence", SEQUENCE);*/
		columns.put("tableId", ID);
		columns.put("id", ID);
		columns.put("ecid", ECID);
		columns.put("userUid", USERUID);
		columns.put("loginUid", LOGINUID);
		columns.put("userId", USERID);
		columns.put("ptMsgId", PTMSGID);
		columns.put("spNumber", SPNUMBER);
		columns.put("phone", PHONE);
		columns.put("deliverTime", DELIVERTIME);
		columns.put("message", MESSAGE);
		columns.put("unicom", UNICOM);
		columns.put("tpUdhi", TP_UDHI);
		columns.put("tpPid", TP_PID);
		columns.put("longMsgSeq", LONGMSGSEQ);
		columns.put("serviceId", SERVICEID);
		columns.put("spgate", SPGATE);
		columns.put("msgFmt", MSGFMT);
		columns.put("pkNumber", PKNUMBER);
		columns.put("pkTotal", PKTOTAL);
		columns.put("ptNotice", PTNOTICE);
		columns.put("cpno", CPNO);	 
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
