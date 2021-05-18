package com.montnets.emp.table.sysuser;

import java.util.HashMap;
import java.util.Map;

public class TableLfDepUserBalance {

	//表名    机构(操作员)余额表
	public static final String TABLE_NAME = "LF_DEP_USER_BALANCE";
	//充值/回收表序列id，标识机构（操作员）余额记录
	public static final String BL_ID = "BL_ID";
	// 所属机构id
	public static final String TARGET_ID = "TARGET_ID";
	// 短信余额：短信可用余额
	public static final String SMS_BALANCE = "SMS_BALANCE";
	// 短信已发短信数量
	public static final String SMS_COUNT = "SMS_COUNT";
	// 彩信余额：彩信可用余额
	public static final String MMS_BALANCE = "MMS_BALANCE";
	// 彩信已发 数量
	public static final String MMS_COUNT = "MMS_COUNT";
	// 企业编号
	public static final String CORP_CODE  = "CORP_CODE";
	//短信阀值
	public static final String SMS_ALARM_VALUE  = "SMS_ALARM_VALUE";
	//彩信阀值
	public static final String MMS_ALARM_VALUE  = "MMS_ALARM_VALUE";
	//通知人手机号
	public static final String ALARM_PHONE  = "ALARM_PHONE";
	//通知人姓名
	public static final String ALARM_NAME  = "ALARM_NAME";
	//通知次数
	public static final String ALARM_COUNT  = "ALARM_COUNT";
	//已通知次数
	public static final String ALARMED_COUNT  = "ALARMED_COUNT";
	//是否每天通知 0否 1是
	public static final String HAS_DAYALARM  = "HAS_DAYALARM";
	//彩信已通知次数
	public static final String MMSALARMEDCOUNT  = "MMSALARMEDCOUNT";
	//序列名
	public static final String SEQUENCE="S_LF_DEP_USER_BALANCE";
	
//	public static final String SMS_ALLOT_BLN = "SMS_ALLOT_BLN";
//	public static final String MMS_ALLOT_BLN = "MMS_ALLOT_BLN";

	protected static final Map<String , String> columns = new HashMap<String, String>();

	static {
		columns.put("LfDepUserBalance", TABLE_NAME);
		columns.put("tableId", BL_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("blId", BL_ID);
		columns.put("targetId", TARGET_ID);
		columns.put("smsBalance", SMS_BALANCE);
		columns.put("smsCount", SMS_COUNT);
		columns.put("mmsBalance", MMS_BALANCE);
		columns.put("mmsCount", MMS_COUNT);
		columns.put("corpCode", CORP_CODE);
		columns.put("smsAlarm", SMS_ALARM_VALUE);
		columns.put("mmsAlarm", MMS_ALARM_VALUE);
		columns.put("alarmName", ALARM_NAME);
		columns.put("alarmPhone", ALARM_PHONE);
		columns.put("alarmCount", ALARM_COUNT);
		columns.put("alarmedCount", ALARMED_COUNT);
		columns.put("hasDayAlarm", HAS_DAYALARM);
		columns.put("mmsAlarmedCount", MMSALARMEDCOUNT);
		
//		columns.put("smsAllotBln", SMS_ALLOT_BLN);
//		columns.put("mmsAllotBln", MMS_ALLOT_BLN);
	}

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM() {
		return columns;
	}
}
