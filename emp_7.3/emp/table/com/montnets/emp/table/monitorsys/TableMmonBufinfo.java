package com.montnets.emp.table.monitorsys;

import java.util.HashMap;
import java.util.Map;

public class TableMmonBufinfo {

	public static final String TABLE_NAME = "M_MON_BUFINFO";

	public static final String PTCODE = "PTCODE";
	
	public static final String ENDCNT = "ENDCNT";

	public static final String MOTOTALRECV = "MOTOTALRECV";
	
	public static final String MTTOTALSND = "MTTOTALSND";

	public static final String WRMOBUF = "WRMOBUF";

	public static final String UPDMOBUF = "UPDMOBUF";

	public static final String UPDRPTBUF = "UPDRPTBUF";

	public static final String WRRPTBUF = "WRRPTBUF";

	public static final String ENDRSPBUF = "ENDRSPBUF";

	public static final String SMTSNDBUF = "SMTSNDBUF";

	public static final String NMTSNDBUF = "NMTSNDBUF";

	public static final String MTWAITBUF = "MTWAITBUF";

	public static final String PRECNT = "PRECNT";

	public static final String MTTOTALRECV = "MTTOTALRECV";

	public static final String MOTOTALSND = "MOTOTALSND";

	public static final String WRMTTASKBUF = "WRMTTASKBUF";
	
	public static final String WRMTTMBUF = "WRMTTMBUF";

	public static final String WRMTVFYBUF = "WRMTVFYBUF";

	public static final String WRMTLVLBUF = "WRMTLVLBUF";

	public static final String PRERSPBUF = "PRERSPBUF";

	public static final String PRERSPTMPBUF = "PRERSPTMPBUF";

	public static final String MOSNDBUF = "MOSNDBUF";

	public static final String RPTSNDBUF = "RPTSNDBUF";

	public static final String MORPTWAITBUF = "MORPTWAITBUF";

	public static final String LOGFILENUM = "LOGFILENUM";

	public static final String LOGBUF = "LOGBUF";

	public static final String RECVBUF = "RECVBUF";
	
	public static final String RESNDBUF = "RESNDBUF";

	public static final String SUPPSNDBUF = "SUPPSNDBUF";

	public static final String MONLOGBUF = "MONLOGBUF";
 
	public static final String UPDATETIME = "UPDATETIME";

    protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("MmonBufinfo", TABLE_NAME);
		/*columns.put("sequence", SEQUENCE);*/
		columns.put("ptcode", PTCODE);
		columns.put("endCnt", ENDCNT);
		columns.put("moTotalRecv", MOTOTALRECV);
		columns.put("mtTotalSnd", MTTOTALSND);
		columns.put("wrMoBuf", WRMOBUF);
		columns.put("upDmoBuf", UPDMOBUF);
		columns.put("updRptBuf", UPDRPTBUF);
		columns.put("wrRptBuf", WRRPTBUF);
		columns.put("endRspBuf", ENDRSPBUF);
		columns.put("smTsndBuf", SMTSNDBUF);
		columns.put("nmTsndBuf", NMTSNDBUF);
		columns.put("mtWaitBuf", MTWAITBUF);
		columns.put("preCnt", PRECNT);
		columns.put("mtTotalRecv", MTTOTALRECV);
		columns.put("moTotalSnd", MOTOTALSND);
		columns.put("wrMttaskBuf", WRMTTASKBUF);
		columns.put("wrMtTmBuf", WRMTTMBUF);
		columns.put("wrMtvfyBuf", WRMTVFYBUF);
		columns.put("wrMtLvlBuf", WRMTLVLBUF);
		columns.put("preRspBuf", PRERSPBUF);
		columns.put("preRspTmpBuf", PRERSPTMPBUF);
		columns.put("moSndBuf", MOSNDBUF);
		columns.put("rptSndBuf", RPTSNDBUF);
		columns.put("moRptWaitBuf", MORPTWAITBUF);
		columns.put("logFileNum", LOGFILENUM);
		columns.put("logBuf", LOGBUF);
		columns.put("recvBuf", RECVBUF);
		columns.put("reSndBuf", RESNDBUF);
		columns.put("suppSndBuf", SUPPSNDBUF);
		columns.put("monLogBuf", MONLOGBUF);
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
