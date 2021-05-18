package com.montnets.emp.servmodule.txgl.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfSpFeeAlarm
{
	public static final String TABLE_NAME = "LF_SPFEEALARM";
	public final static String ID ="ID";// 自增ID
	public final static String SPUSER ="SPUSER";//SP账号
	public final static String NOTICENAME ="NOTICENAME";//通知人姓名
	public final static String ALARMPHONE ="ALARMPHONE";//告警手机号码
	public final static String MODI_USERID ="MODI_USERID"; //最后更新记录的操作员ID
	public final static String CORP_CODE ="CORP_CODE";//企业编码
	public final static String CREATE_TIME ="CREATE_TIME";//创建时间
	public final static String UPDATE_TIME ="UPDATE_TIME";//更新时间
	public final static String ALARMEDCOUNT ="ALARMEDCOUNT";//告警次数
	public final static String SEQUENCE ="S_LF_SPFEEALARM";//序列

    protected static final Map<String, String> columns = new HashMap<String, String>();
	static
	{
		columns.put("LfSpFeeAlarm", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("id", ID);
		columns.put("spuser", SPUSER);
		columns.put("noticename", NOTICENAME);
		columns.put("alarmphone", ALARMPHONE);
		columns.put("modiuserid", MODI_USERID);
 		columns.put("corpcode", CORP_CODE);
 		columns.put("createtime", CREATE_TIME);
 		columns.put("updatetime", UPDATE_TIME);
 		columns.put("alarmedcount", ALARMEDCOUNT);
 		columns.put("sequence", SEQUENCE);
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
