package com.montnets.emp.table.pasgroup;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:42
 * @description
 */
public class TableMrSpgateWatch {

	public static final String TABLE_NAME = "MR_SPGATE_WATCH";
	public static final String GATE_ID = "GATEID";
	public static final String SPGATE = "SPGATE";
	public static final String PT_CODE = "PTCODE";
	public static final String STATE = "STATE";
	public static final String LS_SEND_TIME = "LSSENDTIME";
	public static final String LS_RECV_TIME = "LSRECVTIME";
	public static final String DELAYSS = "DELAYSS";
	public static final String WATCH_NUM = "WATCHNUM";
	public static final String WATCH_SUCC = "WATCHSUCC";
	public static final String WATCH_DELAY = "WATCHDELAY";
	public static final String NUM_MT = "NUMMT";
	public static final String NUM_RPT = "NUMRPT";
	public static final String NUM_MO = "NUMMO";
	public static final String SPEED_MT = "SPEEDMT";
	public static final String SPEED_MO = "SPEEDMO";
	public static final String SPEED_RPT = "SPEEDRPT";
	public static final String UPDATE_TIME = "UPDATETIME";
	public static final String DEAL_FLAG = "DEALFLAG";
	public static final String ERROR_INFO = "ERRORINFO";
	public static final String USR_ID = "USRID";
	public static final String ONLINE_STATUS = "ONLINESTATUS";
	public static final String TOTAL_RPT_SEND = "TOTALRPTSEND";
	public static final String TOTAL_MO_SEND = "TOTALMOSEND";
	public static final String HAVE_SEND_MO = "HAVESENDMO";
	public static final String HAVES_END_RPT = "HAVESENDRPT";
	public static final String RECV_MT = "RECVMT";
	public static final String REMAINED_MT = "REMAINEDMT";
	public static final String TIME_RSEND_MT = "TIMERSENDMT";
	public static final String REMAINED_RPT = "REMAINEDRPT";
	public static final String REMAINED_MO = "REMAINEDMO";
	public static final String RECV_SPEED = "RECVSPEED";
	public static final String PARAM1 = "PARAM1";
	public static final String PARAM2 = "PARAM2";
	public static final String PARAM3 = "PARAM3";
	public static final String PARAM4 = "PARAM4";
	public static final String PARAM5 = "PARAM5";
	public static final String PARAM6 = "PARAM6";
	public static final String PARAM7 = "PARAM7";

    protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
	static {
		columns.put("MrSpgateWatch",TABLE_NAME);
		columns.put("gateId",GATE_ID);
		columns.put("spgate", SPGATE);
		columns.put("ptcode", PT_CODE);
		columns.put("state", STATE);
		columns.put("lssendTime",LS_SEND_TIME);
		columns.put("lsrecvTime", LS_RECV_TIME);
		columns.put("delayss",DELAYSS);
		columns.put("watchNum",WATCH_NUM);
		columns.put("watchSucc",WATCH_SUCC);
		columns.put("watchDelay", WATCH_DELAY);
		columns.put("numMt",NUM_MT);
		columns.put("numRpt", NUM_RPT);
		columns.put("numMo", NUM_MO);
		columns.put("speedMt", SPEED_MT);
		columns.put("speedMo",SPEED_MO);
		columns.put("speedRpt",SPEED_RPT);
		columns.put("updateTime",UPDATE_TIME);
		columns.put("dealFlag",DEAL_FLAG);
		columns.put("errorInfo", ERROR_INFO);
		columns.put("usrId", USR_ID);
		columns.put("onlineStatus", ONLINE_STATUS);
		columns.put("totalRptSend",TOTAL_RPT_SEND);
		columns.put("totalMoSend",TOTAL_MO_SEND);
		columns.put("haveSendMo", HAVE_SEND_MO);
		columns.put("haveSendRpt",HAVES_END_RPT);
		columns.put("recvMt", RECV_MT);
		columns.put("remainedMt", REMAINED_MT);
		columns.put("timeRsendMt", TIME_RSEND_MT);
		columns.put("remainedRpt",REMAINED_RPT);
		columns.put("remainedMo", REMAINED_MO);
		columns.put("recvSpeed", RECV_SPEED);
		columns.put("param1", PARAM1);
		columns.put("param2", PARAM2);
		columns.put("param3",PARAM3);
		columns.put("param4", PARAM4);
		columns.put("param5",PARAM5);
		columns.put("param6", PARAM6);
		columns.put("param7",PARAM7);
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM() {
		return columns;
	}

}
