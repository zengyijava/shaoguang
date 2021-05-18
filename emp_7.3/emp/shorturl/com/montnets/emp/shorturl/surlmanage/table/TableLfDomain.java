package com.montnets.emp.shorturl.surlmanage.table;

import java.util.HashMap;
import java.util.Map;

/**
 * 短域名维护映射
 * @ClassName:LfDomain.java
 * @Description:TODO
 * @author ouyangyu
 * @date:2018-3-5下午02:46:54
 */
public class TableLfDomain {

	public static final String TABLE_NAME = "LF_DOMAIN";
	public static final String ID = "ID";
	public static final String DOMAIN = "DOMAIN";
	public static final String LEN_ALL = "LEN_ALL";
	public static final String LEN_EXTEN = "LEN_EXTEN";
	public static final String DTYPE = "DTYPE";
	public static final String FLAG = "FLAG";
	public static final String VALID_DAYS = "VALID_DAYS";
	public static final String CREATE_UID = "CREATE_UID";
	public static final String CREATE_USER = "CREATE_USER";
	public static final String CREATE_TM = "CREATE_TM";
	public static final String UPDATE_UID = "UPDATE_UID";
	public static final String UPDATE_TM = "UPDATE_TM";
	public static final String REMARK = "REMARK";
	public static final String SEQUENCE	= "LF_DOMAIN_S";
	
	protected static final Map<String , String> columns = new HashMap<String, String>();
	
	static
	{
		columns.put("LfDomain",TABLE_NAME);
		columns.put("tableId",ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id",ID);
		columns.put("domain",DOMAIN);
		columns.put("lenAll",LEN_ALL);
		columns.put("lenExten",LEN_EXTEN);
		columns.put("dtype",DTYPE);
		columns.put("flag",FLAG);
		columns.put("validDays",VALID_DAYS);
		columns.put("createUid",CREATE_UID);
		columns.put("createUser",CREATE_USER);
		columns.put("createTm",CREATE_TM);
		columns.put("updateUid",UPDATE_UID);
		columns.put("updateTm",UPDATE_TM);
		columns.put("remark",REMARK);

	}
	
	public static Map<String , String> getORM(){
		return columns;
	}
	
}
