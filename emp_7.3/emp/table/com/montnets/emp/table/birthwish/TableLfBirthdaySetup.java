package com.montnets.emp.table.birthwish;

import java.util.HashMap;
import java.util.Map;

public class TableLfBirthdaySetup {

	//表名(生日祝福启动)
	public static final String TABLE_NAME = "LF_BIRTHDAYSETUP";
	//标识列
	public static final String ID = "ID";
	//操作员Id
	public static final String USER_ID = "USER_ID";
	//祝福语内容
	public static final String MSG = "MSG";
	//发送时间
	public static final String SEND_TIME = "SEND_TIME";
	//是否加尊称(1是；2否)
	public static final String IS_ADDNAME = "IS_ADDNAME";
	//尊称
	public static final String ADDNAME = "ADDNAME";
	//发送账号
	public static final String SP_USER="SP_USER";
	//是否启用(1是；2否)
	public static final String IS_USE="IS_USE";
	//祝福类型(1员工生日祝福；2客户生日祝福)
	public static final String TYPE="TYPE";
	//企业编码 
	public static final String CORPCODE="CORPCODE";
	//业务编码
	public static final String BUS_CODE="BUS_CODE";
	//标题
	public static final String TITLE="TITLE";
	//是否签名，1是，2否
	public static final String ISSIGNNAME="ISSIGNNAME";
	//签名
	public static final String SIGNNAME="SIGNNAME";
	//序列
	public static final String SEQUENCE = "S_LF_BIRTHDAYSETUP";
	
	protected final static Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfBirthdaySetup", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("id", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("userId", USER_ID);
		columns.put("msg", MSG);
		columns.put("sendTime", SEND_TIME);
		columns.put("isAddName", IS_ADDNAME);
		columns.put("addName", ADDNAME);
		columns.put("spUser", SP_USER);
		columns.put("isUse", IS_USE);
		columns.put("type",TYPE);
		columns.put("corpCode", CORPCODE);
		columns.put("buscode", BUS_CODE);
		columns.put("title", TITLE);
		columns.put("isSignName", ISSIGNNAME);
		columns.put("signName", SIGNNAME);
		
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
