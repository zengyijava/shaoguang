package com.montnets.emp.table.pasgrpbind;

import java.util.HashMap;
import java.util.Map;


public class TableLfAccountBind implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5043703922358023317L;
	//表名(账号绑定表)
	public static final String TABLE_NAME = "LF_ACCOUNT_BIND";
	//标识ID
	public static final String ID = "ID";
	//发送账号
	public static final String SPUSERID = "SPUSERID";
	//业务编码
	public static final String BUS_CODE = "BUS_CODE";
	//操作员GUID
	public static final String SYS_GUID = "SYS_GUID";
	//机构编码
	public static final String DEP_CODE = "DEP_CODE";
	//模块编码
	public static final String MENU_CODE = "MENU_CODE";
	//绑定类型（0-机构；1-操作员；2-业务类型）
	public static final String BIND_TYPE = "BIND_TYPE";
	//创建时间
	public static final String CREATETIME = "CREATETIME";
	//创建者GUID
	public static final String CREATER_GUID = "CREATER_GUID";
	//备注
	public static final String DESCRIPTION = "DESCRIPTION";
	//序列
	public static final String SEQUENCE = "S_LF_ACCOUNT_BIND";
	//企业编码
	public static final String CORP_CODE = "CORP_CODE";

	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfAccountBind", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("spuserId", SPUSERID);
		columns.put("busCode", BUS_CODE);
		columns.put("sysGuid", SYS_GUID);
		columns.put("depCode", DEP_CODE);
		columns.put("menuCode", MENU_CODE);
		columns.put("bindType", BIND_TYPE);
		columns.put("createtime", CREATETIME);
		columns.put("createrGuid", CREATER_GUID);
		columns.put("description", DESCRIPTION);
		columns.put("corpCode", CORP_CODE);
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
