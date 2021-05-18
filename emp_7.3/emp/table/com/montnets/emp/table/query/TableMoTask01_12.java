package com.montnets.emp.table.query;

import com.montnets.emp.common.constant.StaticValue;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 上午09:26:07
 * @description
 */
public class TableMoTask01_12 {

	public static final String TABLE_NAME_01 = "MOTASK01";
	public static final String TABLE_NAME_02 = "MOTASK02";
	public static final String TABLE_NAME_03 = "MOTASK03";
	public static final String TABLE_NAME_04 = "MOTASK04";
	public static final String TABLE_NAME_05 = "MOTASK05";
	public static final String TABLE_NAME_06 = "MOTASK06";
	public static final String TABLE_NAME_07 = "MOTASK07";
	public static final String TABLE_NAME_08 = "MOTASK08";
	public static final String TABLE_NAME_09 = "MOTASK09";
	public static final String TABLE_NAME_10 = "MOTASK10";
	public static final String TABLE_NAME_11 = "MOTASK11";
	public static final String TABLE_NAME_12 = "MOTASK12";
	public static final String TABLE_NAME_HIS = "MO_TASK_HIS";
	public static final String ID = "ID";
	public static final String UID = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE?"UID":"\"UID\"";
	public static final String USERID = "USERID";
	public static final String SPNUMBER = "SPNUMBER";
	public static final String SERVICEID = "SERVICEID";
	public static final String SENDSTATUS = "SENDSTATUS";
	public static final String DELIVERTIME = "DELIVERTIME";
	public static final String PHONE = "PHONE";
	public static final String MSGCONTENT = "MSGCONTENT";
	public static final String ECID = "ECID";
	public static final String ORGUID = "ORGUID";
	public static final String PTMSGID = "PTMSGID";
	public static final String TP_PID = "TP_PID";
	public static final String TP_UDHI = "TP_UDHI";
	public static final String MSGFMT = "MSGFMT";
	public static final String UNICOM = "UNICOM";

    protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
	static {
		columns.put("MoTask01", TABLE_NAME_01);
		columns.put("MoTask02", TABLE_NAME_02);
		columns.put("MoTask03", TABLE_NAME_03);
		columns.put("MoTask04", TABLE_NAME_04);
		columns.put("MoTask05", TABLE_NAME_05);
		columns.put("MoTask06", TABLE_NAME_06);
		columns.put("MoTask07", TABLE_NAME_07);
		columns.put("MoTask08", TABLE_NAME_08);
		columns.put("MoTask09", TABLE_NAME_09);
		columns.put("MoTask10", TABLE_NAME_10);
		columns.put("MoTask11", TABLE_NAME_11);
		columns.put("MoTask12", TABLE_NAME_12);
		columns.put("MoTaskHis",TABLE_NAME_HIS);
		columns.put("id", ID);
		columns.put("uid",UID);
		columns.put("userId", USERID);
		columns.put("spnumber", SPNUMBER);
		columns.put("serviceId", SERVICEID);
		columns.put("sendStatus", SENDSTATUS);
		columns.put("deliverTime",DELIVERTIME);
		columns.put("phone", PHONE);
		columns.put("msgContent",MSGCONTENT);
		columns.put("ecid", ECID);
		columns.put("orgUid",ORGUID);
		columns.put("ptmsgId", PTMSGID);
		columns.put("tpPid", TP_PID);
		columns.put("tpUdhi", TP_UDHI);
		columns.put("msgFmt", MSGFMT);
		columns.put("unicom", UNICOM);
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM() {
		return columns;
	}
}
