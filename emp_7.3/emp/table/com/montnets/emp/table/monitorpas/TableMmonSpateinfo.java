package com.montnets.emp.table.monitorpas;

import java.util.HashMap;
import java.util.Map;

public class TableMmonSpateinfo {

	public static final String TABLE_NAME = "M_MON_SPGATEINFO";

	public static final String PTCODE = "PTCODE";
	
	public static final String USERID = "USERID";

	public static final String USERUID = "USERUID";

	public static final String USERNAME = "USERNAME";

	public static final String JTYPE = "JTYPE";

	public static final String LINKNUM = "LINKNUM";

	public static final String LOGINIP = "LOGINIP";

	public static final String ONLINESTATUS = "ONLINESTATUS";

	public static final String MTHAVESND = "MTHAVESND";

	public static final String MTREMAINED = "MTREMAINED";

	public static final String MTRECVSPD = "MTRECVSPD";

	public static final String MOTOTALRECV = "MOTOTALRECV";

	public static final String MOHAVESND = "MOHAVESND";

	public static final String MOREMAINED = "MOREMAINED";

	public static final String MOSNDSPD = "MOSNDSPD";

	public static final String RPTTOTALRECV = "RPTTOTALRECV";

	public static final String RPTHAVESND = "RPTHAVESND";

	public static final String RPTREMAINED = "RPTREMAINED";

	public static final String RPTSNDSPD = "RPTSNDSPD";

	public static final String SNDERCNT = "SNDERCNT";

	public static final String LASTSNDERTM = "LASTSNDERTM";

	public static final String LOGININTM = "LOGININTM";

	public static final String LOGINOUTTM = "LOGINOUTTM";

	public static final String UPDATETIME = "UPDATETIME";

	
	protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("MmonSpateinfo", TABLE_NAME);
		/*columns.put("sequence", SEQUENCE);*/
		columns.put("ptcode", PTCODE);
		columns.put("userId", USERID);
		columns.put("userUid", USERUID);
		columns.put("userName", USERNAME);
		columns.put("jtype", JTYPE);
		columns.put("linkNum", LINKNUM);
		columns.put("loginIp", LOGINIP);
		columns.put("onLineStatus", ONLINESTATUS);
		columns.put("mtHaveSnd", MTHAVESND);
		columns.put("mtRemained", MTREMAINED);
		columns.put("mtRecvSpd", MTRECVSPD);
		columns.put("moTotalRecv", MOTOTALRECV);
		columns.put("moHaveSnd", MOHAVESND);
		columns.put("moRemained", MOREMAINED);
		columns.put("moSndSpd", MOSNDSPD);
		columns.put("rptTotalRecv", RPTTOTALRECV);
		columns.put("rptHaveSnd", RPTHAVESND);
		columns.put("rptRemained", RPTREMAINED);
		columns.put("rptSndSpd", RPTSNDSPD);
		columns.put("snderCnt", SNDERCNT);
		columns.put("lastSnderRtm", LASTSNDERTM);
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
