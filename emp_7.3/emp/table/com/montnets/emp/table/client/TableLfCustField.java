package com.montnets.emp.table.client;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuqw <158030621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2012-1-10
 * @description 
 */
public class TableLfCustField {

	//表名 ()
	public static final String TABLE_NAME = "LF_CUSTFIELD";
	//自增ID
	public static final String ID = "ID";
	//引用字段
	public static final String FIELD_REF = "FIELD_REF";
	//显示名称
	public static final String FIELD_NAME = "FIELD_NAME";
	//值类型。0：单选；1：多选
	public static final String V_TYPE = "V_TYPE";
	//企业代码
	public static final String CORP_CODE = "CORP_CODE";
	//用户ID
	public static final String USERID = "USERID";
	//序列名
	public static final String SEQUENCE="S_LF_CUSTFIELD";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfCustField", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("field_Ref", FIELD_REF);
		columns.put("field_Name", FIELD_NAME);
		columns.put("v_type", V_TYPE);
		columns.put("corp_code", CORP_CODE);
		columns.put("userid", USERID);
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
