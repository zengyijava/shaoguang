package com.montnets.emp.shorturl.surlmanage.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfDomainCorp {


	public static final String TABLE_NAME = "LF_DOMAIN_CORP";
	public static final String DOMAIN_ID = "DOMAIN_ID";
	public static final String CORP_CODE = "CORP_CODE";
	public static final String CREATE_UID = "CREATE_UID";
	public static final String CREATE_TM = "CREATE_TM";
	public static final String UPDATE_UID = "UPDATE_UID";
	public static final String UPDATE_TM = "UPDATE_TM";
	public static final String FLAG = "FLAG";
	public static final String CREATE_USER = "CREATE_USER";
    public static final String TABLE_ID = "DOMAIN_CORP_ID";
	
	protected static final Map<String , String> columns = new HashMap<String, String>();
	
	static
	{
		columns.put("LfDomainCorp",TABLE_NAME);
		columns.put("tableId",TABLE_ID);
		columns.put("domainId",DOMAIN_ID);
		columns.put("corpCode",CORP_CODE);
		columns.put("createUid",CREATE_UID);
		columns.put("createTm",CREATE_TM);
		columns.put("updateUid",UPDATE_UID);
		columns.put("updateTm",UPDATE_TM);
		columns.put("flag",FLAG);
		columns.put("createUser",CREATE_USER);
	}
	
	public static Map<String , String> getORM(){
		return columns;
	}
	

}
