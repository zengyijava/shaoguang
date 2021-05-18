package com.montnets.emp.table.sysuser;

import java.util.HashMap;
import java.util.Map;


public class TableLfDeppwdReceiver implements java.io.Serializable{

	private static final long serialVersionUID = -5043703922358023317L;
	//表名(部门密码接收人)
	public static final String TABLE_NAME = "LF_DEPPWDRECEIVER";
	//标识ID
	public static final String DPR_ID = "DPR_ID";
	//部门id
	public static final String DEP_ID = "DEP_ID";
	//用户USER_ID
	public static final String USER_ID = "USER_ID";
	//创建时间
	public static final String CREATETIME = "CREATETIME";
	//序列
	public static final String SEQUENCE = "S_LF_DEPPWDRECEIVER";

	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfDeppwdReceiver", TABLE_NAME);
		columns.put("tableId", DPR_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("dprid", DPR_ID);
		columns.put("depid", DEP_ID);
		columns.put("userid", USER_ID);
		columns.put("createtime", CREATETIME);
		
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
