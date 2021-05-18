package com.montnets.emp.rms.report.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfRmsReport {
	//表名
	public static final String TABLE_NAME = "LF_RMS_REPORT";
	//主键
	public static final String ID="ID";
	//运营商
	public static final String SPISUNCM="SPISUNCM";
	//sp账号
	public static final String USER_ID="USERID";
	//年月日
	public static final String IYMD="IYMD";
	//提交总数
	public static final String ICOUNT="ICOUNT";
	//发送成功数
	public static final String RSUCC="RSUCC";
	//接收失败数
	public static final String RFAIL="RFAIL";
	//create时间
	public static final String CREATE_TIME="CREATE_TIME";
	//企业编码
	public static final String CORP_CODE="CORP_CODE";
	//同步时间
	public static final String SYN_TIME="SYN_TIME";
	//档位
	public static final String DEGREE="DEGREE";
	//扩展字段1
	public static final String PARAM1 = "PARAM1";
	//扩展字段2
	public static final String PARAM2 = "PARAM2";
	//扩展字段3
	public static final String PARAM3 = "PARAM3";
	//扩展字段4
	public static final String PARAM4 = "PARAM4";
	//序列
	public static final String SEQUENCE = "LF_RMS_REPORT_ID";
	
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfRmsReport", TABLE_NAME);
		columns.put("id", "ID");
		columns.put("spisuncm", "SPISUNCM");
		columns.put("spID", "USERID");
		columns.put("iymd", "IYMD");
		columns.put("icount", "ICOUNT");
		columns.put("rsucc", "RSUCC");
		columns.put("rfail", "RFAIL");
		columns.put("createTime", "CREATE_TIME");
		columns.put("corpCode", "CORP_CODE");
		columns.put("synTime", "SYN_TIME");
		columns.put("degree", "DEGREE");
		columns.put("param1", "PARAM1");
		columns.put("param2", "PARAM2");
		columns.put("param3", "PARAM3");
		columns.put("param4", "PARAM4");
		columns.put("sequence", "LF_RMS_REPORT_ID");
	}
	
	public static Map<String, String> getORM()
	{
		return columns;
	}
}
