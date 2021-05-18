package com.montnets.emp.table.sms;

import java.util.HashMap;
import java.util.Map;

public class TableMtTask01_12 {

	public static final String TABLE_NAME_01 = "MTTASK01";
	public static final String TABLE_NAME_02 = "MTTASK02";
	public static final String TABLE_NAME_03 = "MTTASK03";
	public static final String TABLE_NAME_04 = "MTTASK04";
	public static final String TABLE_NAME_05 = "MTTASK05";
	public static final String TABLE_NAME_06 = "MTTASK06";
	public static final String TABLE_NAME_07 = "MTTASK07";
	public static final String TABLE_NAME_08 = "MTTASK08";
	public static final String TABLE_NAME_09 = "MTTASK09";
	public static final String TABLE_NAME_10 = "MTTASK10";
	public static final String TABLE_NAME_11 = "MTTASK11";
	public static final String TABLE_NAME_12 = "MTTASK12";
	public static final String ID = "ID";
	public static final String MDAY = "MDAY";
	public static final String USER_ID = "USERID";
	public static final String SPGATE = "SPGATE";
	public static final String CPNO = "CPNO";
	public static final String PHONE = "PHONE";
	public static final String SP_MSG_ID = "SPMSGID";
	public static final String RET_FLAG = "RETFLAG";
	public static final String FEE_FLAG = "FEEFLAG";
	public static final String PK_NUMBER = "PKNUMBER";
	public static final String PK_TOTAL = "PKTOTAL";
	public static final String SEND_STATUS = "SENDSTATUS";
	public static final String SEND_FLAG = "SENDFLAG";
	public static final String RECV_FLAG = "RECVFLAG";
	public static final String DONE_DATE = "DONEDATE";
	public static final String ERRO_RCODE = "ERRORCODE";
	public static final String SEND_LEVEL = "SENDLEVEL";
	public static final String SEND_TYPE = "SENDTYPE";
	public static final String UNICOM = "UNICOM";
	public static final String SEND_TIME = "SENDTIME";
	public static final String RECV_TIME = "RECVTIME";
	public static final String MESSAGE = "MESSAGE";
	public static final String TASK_ID = "TASKID";
	public static final String ECID = "ECID";
	public static final String PT_MSG_ID = "PTMSGID";
	public static final String SVRTYPE = "SVRTYPE";

	public static final String USERMSGID = "USERMSGID";
    public static final String CUSTID = "CUSTID";
    public static final String ERROR_CODE2 = "ERRORCODE2";
    public static final String TMPLID = "TMPLID";
    public static final String DOWNTM = "DOWNTM";
    public static final String CHGRADE = "CHGRADE";

	public static final String BATCHID = "BATCHID";


	protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
	static {
		columns.put("MtTask01", TABLE_NAME_01);
		columns.put("MtTask02", TABLE_NAME_02);
		columns.put("MtTask03", TABLE_NAME_03);
		columns.put("MtTask04", TABLE_NAME_04);
		columns.put("MtTask05", TABLE_NAME_05);
		columns.put("MtTask06", TABLE_NAME_06);
		columns.put("MtTask07", TABLE_NAME_07);
		columns.put("MtTask08", TABLE_NAME_08);
		columns.put("MtTask09", TABLE_NAME_09);
		columns.put("MtTask10", TABLE_NAME_10);
		columns.put("MtTask11", TABLE_NAME_11);
		columns.put("MtTask12", TABLE_NAME_12);
		columns.put("id",ID);
		columns.put("mday", MDAY);
		columns.put("userid",USER_ID);
		columns.put("spgate",SPGATE);
		columns.put("cpno", CPNO);
		columns.put("phone", PHONE);
		columns.put("spmsgid",SP_MSG_ID);
		columns.put("retflag",RET_FLAG);
		columns.put("feeflag",FEE_FLAG);
		columns.put("pknumber",PK_NUMBER);
		columns.put("pktotal",PK_TOTAL);
		columns.put("sendstatus",SEND_STATUS);
		columns.put("sendflag",SEND_FLAG);
		columns.put("recvflag",RECV_FLAG);
		columns.put("donedate",DONE_DATE);
		columns.put("errorcode",ERRO_RCODE);
		columns.put("sendlevel",SEND_LEVEL);
		columns.put("sendtype",SEND_TYPE);
		columns.put("unicom", UNICOM);
		columns.put("sendtime",SEND_TIME);
		columns.put("recvtime", RECV_TIME);
		columns.put("message", MESSAGE);
		columns.put("taskid", TASK_ID);
		columns.put("ecid",ECID);
		columns.put("ptmsgid",PT_MSG_ID);
		columns.put("svrtype",SVRTYPE);

        columns.put("usermsgid", USERMSGID);
        columns.put("custid", CUSTID);
        columns.put("error_code2", ERROR_CODE2);
        columns.put("tmplid", TMPLID);
        columns.put("downtm", DOWNTM);
        columns.put("chgrade", CHGRADE);

		columns.put("batchID", BATCHID);
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
