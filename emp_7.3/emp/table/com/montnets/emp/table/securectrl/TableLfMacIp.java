package com.montnets.emp.table.securectrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Mac与IP地址绑定表
 * @author Administrator
 *
 */
public class TableLfMacIp implements java.io.Serializable{
	
	private static final long serialVersionUID = -5043703922358023317L;
	//表名_Mac与IP地址绑定表
	public static final String TABLE_NAME = "LF_MACIP";
	//主键  标识列
	public static final String LMIID = "LMIID";
	//操作员ID
	public static final String GUID = "GUID";
	//mac地址
	public static final String MAC_ADDR = "MAC_ADDR";
	//IP地址
	public static final String IP_ADDR = "IP_ADDR";
	//绑定类型(无效)
	public static final String TYPE = "TYPE";
	//绑定人(创建人)
	public static final String CREATOR_NAME = "CREATORNAME";
	//绑定时间(创建时间)
	public static final String CREAT_TIME = "CREATTIME";
	//是否启用动态口令(0是不启用,1是启用)
	public static final String DT_PASS = "DT_PWD";
	//序列
	public static final String SEQUENCE = "S_LF_MACIP";
	
	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfMacIp", TABLE_NAME);
		columns.put("tableId", LMIID);
		columns.put("sequence", SEQUENCE);
		columns.put("lmiid", LMIID);
		columns.put("guid", GUID);
		columns.put("macaddr", MAC_ADDR);
		columns.put("ipaddr", IP_ADDR);
		columns.put("creatorName", CREATOR_NAME);
		columns.put("creatTime", CREAT_TIME);
		columns.put("type", TYPE);
		columns.put("dtpwd", DT_PASS);
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
