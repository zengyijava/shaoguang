package com.montnets.emp.table.birthwish;

import java.util.HashMap;
import java.util.Map;

public class TableLfBirthdayMember {

	//生日祝福成员
	public static final String TABLE_NAME = "LF_BIRTHDAYMEMBER";
	//自增ID
	public static final String ID = "ID";
	//引用生日祝福设置表id
	public static final String BS_ID = "BS_ID";
	//祝福类型(1员工生日祝福；2客户生日祝福)
	public static final String TYPE = "TYPE";
	//成员标示，用guid
	public static final String MEMBER_ID = "MEMBER_ID";
	//操作员id
	public static final String USER_ID = "USER_ID";
	//祝福语内容(暂不适用)
	public static final String MSG = "MSG";
	//尊称(暂作为成员name用)
	public static final String ADDNAME = "ADDNAME";
	//企业编码
	public static final String CORPCODE = "CORPCODE";
	//成员类型，1为人员，2为单机构，3为包含子机构
	public static final String MEMBER_TYPE="MEMBER_TYPE";
	//序列
	public static final String SEQUENCE="S_LF_BIRTHDAYMEMBER";
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfBirthdayMember", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("id", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("bsId", BS_ID);
		columns.put("type", TYPE);
		columns.put("memberId", MEMBER_ID);
		columns.put("userId", USER_ID);
		columns.put("msg", MSG);
		columns.put("addName", ADDNAME);
		columns.put("corpCode", CORPCODE);
		columns.put("membertype", MEMBER_TYPE);
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
