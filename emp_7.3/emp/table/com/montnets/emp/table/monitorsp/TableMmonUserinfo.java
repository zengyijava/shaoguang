package com.montnets.emp.table.monitorsp;

import java.util.HashMap;
import java.util.Map;

public class TableMmonUserinfo {

	public static final String TABLE_NAME = "M_MON_USERINFO";

	public static final String PTCODE = "PTCODE";
	
	public static final String USERID = "USERID";

	public static final String USERUID = "USERUID";

	public static final String USERNAME = "USERNAME";

	public static final String USERPRIVILEGE = "USERPRIVILEGE";
	
	public static final String JTYPE = "JTYPE";

	public static final String LINKNUM = "LINKNUM";

	public static final String LOGINIP = "LOGINIP";

	public static final String ONLINESTATUS = "ONLINESTATUS";

	public static final String USERFEE = "USERFEE";
	
	public static final String FIXFAILURERATE = "FIXFAILURERATE";
	
	public static final String FAILURENUM = "FAILURENUM";
	
	public static final String FAILURERATE = "FAILURERATE";	
	
	public static final String BINDINFO = "BINDINFO";	
	
	public static final String MTTOTALSND = "MTTOTALSND";
	
	public static final String MTHAVESND ="MTHAVESND";
	
	public static final String MTREMAINED = "MTREMAINED";
	
	public static final String MTSNDINFO = "MTSNDINFO";
	
	public static final String MTSNDSPD = "MTSNDSPD";
	
	public static final String MOTOTALRECV = "MOTOTALRECV";
	
	public static final String MOREMAINED = "MOREMAINED";
	
	public static final String MORPTRECVSPD = "MORPTRECVSPD";
	
	public static final String MOTMOUTCNT = "MOTMOUTCNT";
	
	public static final String RPTTOTALRECV = "RPTTOTALRECV";
	
	public static final String RPTREMAINED = "RPTREMAINED";
	
	public static final String RPTTMOUTCNT = "RPTTMOUTCNT";
	
	public static final String LOGININTM = "LOGININTM";
	
	public static final String LOGINOUTTM = "LOGINOUTTM";
	
	public static final String UPDATETIME = "UPDATETIME"; 
	
	protected final static Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("MmonUserinfo", TABLE_NAME);
		/*columns.put("sequence", SEQUENCE);*/
		columns.put("ptcode", PTCODE);
		columns.put("userId", USERID);
		columns.put("userUid", USERUID);
		columns.put("userName", USERNAME);
		columns.put("", USERPRIVILEGE);
		columns.put("jtype", JTYPE);
		columns.put("linkNum", LINKNUM);
		columns.put("loginIp", LOGINIP);
		columns.put("onLineStatus", ONLINESTATUS);
		columns.put("userFee",USERFEE);
		columns.put("fixFailureRate",FIXFAILURERATE);
		columns.put("failureNum",FAILURENUM);
		columns.put("failureRate",FAILURERATE);
		columns.put("bindInfo", BINDINFO);
		columns.put("mtTotalSnd", MTTOTALSND);
		columns.put("mtHaveSnd", MTHAVESND);
		columns.put("mtRemained", MTREMAINED);
		columns.put("mtSndInfo", MTSNDINFO);
		columns.put("mtSndSpd", MTSNDSPD);
		columns.put("moTotalRec", MOTOTALRECV);
		columns.put("moRemained", MOREMAINED);
		columns.put("moRptRecvSpd", MORPTRECVSPD);
		columns.put("moTmoutCnt", MOTMOUTCNT);
		columns.put("rptTotalRecv", RPTTOTALRECV);
		columns.put("rptRemained", RPTREMAINED);
		columns.put("rptTmoutCnt", RPTTMOUTCNT);
		columns.put("loginInTm", LOGININTM);
		columns.put("loginOutTm", LOGINOUTTM);
		columns.put("updateTime", UPDATETIME);	 
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
