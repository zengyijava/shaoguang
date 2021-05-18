package com.montnets.emp.shorturl.surlmanage.table;

import java.util.HashMap;
import java.util.Map;

/**
 * Lf_Neturl表对应map
 * @ClassName:TableLfNeturl.java
 * @Description:TODO
 * @author Administrator
 * @date:2018-1-11下午03:07:57
 */

public class TableLfNeturl {

	public static final String TABLE_NAME = "LF_NETURL";
	public static final String ID = "ID";
	public static final String SRC_URL = "SRC_URL";
	public static final String URL_NAME = "URL_NAME";
	public static final String URL_MSG = "URL_MSG";
	public static final String CORP_CODE = "CORP_CODE";
	public static final String ISPASS = "ISPASS";
	public static final String URLSTATE = "URLSTATE";
	public static final String CREATE_UID = "CREATE_UID";
	public static final String CREATE_TM = "CREATE_TM";
	public static final String UPDATE_UID = "UPDATE_UID";
	public static final String UPDATE_TM = "UPDATE_TM";
	public static final String AUDIT_UID = "AUDIT_UID";
	public static final String AUDIT_TM = "AUDIT_TM";
	public static final String STOP_UID = "STOP_UID";
	public static final String STOP_TM = "STOP_TM";
	public static final String REMARKS = "REMARKS";
	public static final String REMARKS1 = "REMARKS1";
	public static final String REMARKS2 = "REMARKS2";
	public static final String SEQUENCE	= "LF_NETURL_S";
	protected static final Map<String , String> columns = new HashMap<String, String>();
	
	static
	{
		columns.put("LfNeturl",TABLE_NAME);
		columns.put("tableId",ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id",ID);
		columns.put("srcurl",SRC_URL);
		columns.put("urlname",URL_NAME);
		columns.put("urlmsg",URL_MSG);
		columns.put("corpcode",CORP_CODE);
		columns.put("ispass",ISPASS);
		columns.put("urlstate",URLSTATE);
		columns.put("createuid",CREATE_UID);
		columns.put("createtm",CREATE_TM);
		columns.put("updateuid",UPDATE_UID);
		columns.put("updatetm",UPDATE_TM);
		columns.put("audituid",AUDIT_UID);
		columns.put("audittm",AUDIT_TM);
		columns.put("stopuid",STOP_UID);
		columns.put("stoptm",STOP_TM);
		columns.put("remarks",REMARKS);
		columns.put("remarks1",REMARKS1);
		columns.put("remarks2",REMARKS2);
	}
	
	public static Map<String , String> getORM(){
		return columns;
	}
	
}
