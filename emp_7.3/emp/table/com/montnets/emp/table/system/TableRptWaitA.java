package com.montnets.emp.table.system;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-27 下午02:43:40
 * @description
 */

public class TableRptWaitA
{
	public static final String TABLE_NAME = "RPT_WAIT_A";

	public static final String ID = "ID";

	public static final String ECID = "ECID";

	public static final String USER_UID = "USERUID";

	public static final String LOGIN_UID = "LOGINUID";

	public static final String USERID = "USERID";

	public static final String PT_MSGID = "PTMSGID";

	public static final String SPNUMBER = "SPNUMBER";

	public static final String PHONE = "PHONE";

	public static final String SUBMIT_TIME = "SUBMITTIME";

	public static final String DONE_TIME = "DONETIME";
	
	public static final String USERMSGID = "USERMSGID";
	
	public static final String MODULEID = "MODULEID";

	public static final String ERRORCODE = "ERRORCODE";

	public static final String SEQUENCE = "RPTWAITA_ID";

	protected static final Map<String , String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("RptWaitA", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("ecId", ECID);
		columns.put("userUid", USER_UID);
		columns.put("LoginUid", LOGIN_UID);
		columns.put("userId", USERID);
		columns.put("ptMsgId", PT_MSGID);
		columns.put("spnumber", SPNUMBER);
		columns.put("phone", PHONE);
		columns.put("submitTime", SUBMIT_TIME);
		columns.put("userMsgId", USERMSGID);
		columns.put("moduleId", MODULEID);
		columns.put("doneTime", DONE_TIME);
		columns.put("errorCode", ERRORCODE);
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
