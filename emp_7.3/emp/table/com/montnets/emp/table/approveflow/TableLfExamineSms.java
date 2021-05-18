package com.montnets.emp.table.approveflow;

import java.util.HashMap;
import java.util.Map;

public class TableLfExamineSms implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8299975374447856125L;
	//表名_审批提醒短信表
	public static final String TABLE_NAME = "LF_EXAMINE_SMS";
	//标识列
	public static final String ES_ID = "ES_ID";
	//审核流程ID
	public static final String FR_ID = "FR_ID";
	//手机号码 
	public static final String PHONE  = "PHONE";
	//短信内容
	public static final String MSGCONTENT = "MSGCONTENT";
	//同意批次号
	public static final String BATCHNUMBER = "BATCHNUMBER";
	//不同意批次号
	public static final String DISAGREENUMBER = "DISAGREENUMBER";
	//回复内容
	public static final String RECIVECONTENT = "RECIVECONTENT";
	//回复时间
	public static final String RECIVETIME = "RECIVETIME";
	//通道号
	public static final String SPNUMBER = "SPNUMBER";
	//发送账号
	public static final String SPUSER = "SPUSER";
	//1代表短信提醒 2代表是否成功回复
	public static final String ES_TYPE = "ES_TYPE";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("tableId", ES_ID);
		columns.put("LfExamineSms", TABLE_NAME);
		columns.put("esId", ES_ID);
		columns.put("frId", FR_ID);
		columns.put("phone", PHONE);
		columns.put("msgContent", MSGCONTENT);
		columns.put("batchNumber", BATCHNUMBER);
		columns.put("disagreeNumber", DISAGREENUMBER);
		columns.put("reciveContent", RECIVECONTENT);
		columns.put("reciveTime", RECIVETIME);
		columns.put("spNumber", SPNUMBER);
		columns.put("spUser", SPUSER);
		columns.put("esType", ES_TYPE);
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
