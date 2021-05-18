package com.montnets.emp.shorturl.surlmanage.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfUrlTask {
	public static final String TABLE_NAME	= "LF_URLTASK";
	
	public static final String ID = "ID";
	public static final String TASKID = "TASKID";
	public static final String SPUSER = "SPUSER";
	public static final String CORP_CODE = "CORP_CODE";
	public static final String TITLE = "TITLE";
	public static final String MSG = "MSG";
	public static final String UTYPE = "UTYPE";
	public static final String PLAN_TIME = "PLAN_TIME";
	public static final String SUBMITTIME = "SUBMIT_TM";
	public static final String SEND_TM = "SEND_TM";
	public static final String HANDLE_NODE = "HANDLE_NODE";
	public static final String HANDLE_LINE = "HANDLE_LINE";
	public static final String SUB_COUNT = "SUB_COUNT";
	public static final String SRC_FILE = "SRC_FILE";
	public static final String URL_FILE = "URL_FILE";
	public static final String STATUS = "STATUS";
	public static final String CREATE_UID = "CREATE_UID";
	public static final String CREATE_TM = "CREATE_TM";
	public static final String UPDATE_UID = "UPDATE_UID";
	public static final String UPDATE_TM = "UPDATE_TM";
	public static final String NETURL = "NETURL";
	public static final String NETURL_ID = "NETURL_ID";
	public static final String DOMAIN_ID = "DOMAIN_ID";
	public static final String DOMAIN_URL = "DOMAIN_URL";
	public static final String DOMAIN_EXTEN = "DOMAIN_EXTEN";
	public static final String DOMAIN_LEN = "DOMAIN_LEN";
	public static final String VALID_DAYS = "VALID_DAYS";
	public static final String REMARK = "REMARK";
	public static final String REMARK1 = "REMARK1";
	public static final String STR_TASKID = "STR_TASKID";
	
	public static final String SEQUENCE	= "LF_URLTASK_S";
	protected final static Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfUrlTask", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("taskId", TASKID);
		columns.put("spUser", SPUSER);
		columns.put("corpCode", CORP_CODE);
		columns.put("title", TITLE);
		columns.put("msg", MSG);
		columns.put("utype", UTYPE);
		columns.put("plantime",PLAN_TIME );
		columns.put("submittm", SUBMITTIME);
		columns.put("sendtm",SEND_TM );
		columns.put("handleNode",HANDLE_NODE );
		columns.put("handleLine",HANDLE_LINE );
		columns.put("subCount", SUB_COUNT);
		columns.put("srcfile", SRC_FILE);
		columns.put("urlfile",URL_FILE );
		columns.put("status", STATUS);
		columns.put("createUid", CREATE_UID);
		columns.put("createtm", CREATE_TM);
		columns.put("updateUid", UPDATE_UID);
		columns.put("updatetm", UPDATE_TM);
		columns.put("netUrl", NETURL);
		columns.put("netUrlId", NETURL_ID);
		columns.put("domainId", DOMAIN_ID);
		columns.put("domainUrl", DOMAIN_URL);
		columns.put("domainExten", DOMAIN_EXTEN);
		columns.put("domainLen", DOMAIN_LEN);
		columns.put("validDays", VALID_DAYS);
		columns.put("remark", REMARK);
		columns.put("remark1", REMARK1);
		columns.put("strTaskId", STR_TASKID);
	};


	public static Map<String , String> getORM(){
		return columns;
	}
}
