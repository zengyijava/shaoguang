package com.montnets.emp.table.report;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:44
 * @description
 */
public class TableMtDatareport
{

	public static final String TABLE_NAME = "MT_DATAREPORT";

	public static final String USER_ID = "USERID";

	public static final String TASK_ID = "TASKID";

	public static final String SPGATE = "SPGATE";

	public static final String IYMD = "IYMD";

	public static final String IHOUR = "IHOUR";

	public static final String PT_CODE = "PTCODE";

	public static final String IMONTH = "IMONTH";

	public static final String ICOUNT = "ICOUNT";

	public static final String SUCC = "SUCC";

	public static final String FAIL1 = "FAIL1";

	public static final String FAIL2 = "FAIL2";

	public static final String FAIL3 = "FAIL3";

	public static final String NRET = "NRET";

	public static final String RSUCC = "RSUCC";

	public static final String RFAIL1 = "RFAIL1";

	public static final String RFAIL2 = "RFAIL2";
	
	public static final String RECFAIL = "RECFAIL";

	public static final String RNRET = "RNRET";

	public static final String RELEASE_FLAG = "RELEASEFLAG";

	public static final String START_TIME = "STARTTIME";

	public static final String END_TIME = "ENDTIME";

	public static final String ID = "ID";

	public static final String Y = "Y";

	public static final String OP_CONTENT = "OP_CONTENT";

	public static final String SPISUNCM = "SPISUNCM";

	public static final String SEQUENCE = "MT_DATAREPORT_ID";
	
	public static final String SPID="SPID";

	public static final String SVRTYPE = "SVRTYPE";
	
	public static final String P1 = "P1";
	
	public static final String P2 = "P2";
	
	public static final String P3 = "P3";
	
	public static final String P4 = "P4";
	
	public static final String BATCHID = "BATCHID";

	public static final String MOBILEAREA = "MOBILEAREA";

	public static final String AREACODE = "AREACODE";

    protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */ 
	static
	{
		columns.put("MtDatareport", TABLE_NAME);
		columns.put("sequence", SEQUENCE);
		columns.put("userId", USER_ID);
		columns.put("taskId", TASK_ID);
		columns.put("spgate", SPGATE);
		columns.put("iymd", IYMD);
		columns.put("ihour", IHOUR);
		columns.put("ptCode", PT_CODE);
		columns.put("imonth", IMONTH);
		columns.put("icount", ICOUNT);
		columns.put("succ", SUCC);
		columns.put("fail1", FAIL1);
		columns.put("fail2", FAIL2);
		columns.put("fail3", FAIL3);
		columns.put("nret", NRET);
		columns.put("rsucc", RSUCC);
		columns.put("rfail1", RFAIL1);
		columns.put("rfail2", RFAIL2);
		columns.put("recfail", RECFAIL);
		columns.put("rnret", RNRET);
		columns.put("releaseFlag", RELEASE_FLAG);
		columns.put("startTime", START_TIME);
		columns.put("endTime", END_TIME);
		columns.put("id", ID);
		columns.put("y", Y);
		columns.put("spisuncm", SPISUNCM);
		columns.put("spID", SPID);
		columns.put("svrType", SVRTYPE);
		columns.put("p1", P1);
		columns.put("p2", P2);
		columns.put("p3", P3);
		columns.put("p4", P4);
		columns.put("batchID", BATCHID);
		columns.put("mobileArea", MOBILEAREA);
		columns.put("areaCode", AREACODE);
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
