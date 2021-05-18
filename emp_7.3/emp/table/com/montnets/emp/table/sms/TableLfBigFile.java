package com.montnets.emp.table.sms;

import java.util.HashMap;
import java.util.Map;

public class TableLfBigFile {
	
	public static String TABLE_NAME = "LF_BIGFILE";

	public static final String ID             ="ID";
	public static final String FILE_NAME      ="FILE_NAME";
	public static final String SUB_COUNT      ="SUB_COUNT";
	public static final String EFF_COUNT      ="EFF_COUNT";
	public static final String BLA_COUNT      ="BLA_COUNT";
	public static final String REP_COUNT      ="REP_COUNT";
	public static final String ERR_COUNT      ="ERR_COUNT";
	public static final String OPR_NUM      ="OPR_NUM";
	public static final String HANDLE_STATUS  ="HANDLE_STATUS";
	public static final String FILE_STATUS    ="FILE_STATUS";
	public static final String TASKID         ="TASKID";
	public static final String UPLOAD_NUM        ="UPLOAD_NUM";
	public static final String EFF_NUM        ="EFF_NUM";
	public static final String HANDLE_NODE    ="HANDLE_NODE";
	public static final String USERID         ="USERID";
	public static final String USER_NAME      ="USER_NAME";
	public static final String BUS_ID    = "BUS_ID";
	public static final String BUS_NAME  = "BUS_NAME";
	public static final String BUS_CODE  = "BUS_CODE";
	public static final String DEP_ID         ="DEP_ID";
	public static final String CREATE_TIME    ="CREATE_TIME";
	public static final String UPDATE_TIME    ="UPDATE_TIME";
	public static final String FILE_URL       ="FILE_URL";
	public static final String FILE_URL2      ="FILE_URL2";
	public static final String FILE_URL3      ="FILE_URL3";
	public static final String FILE_URL4      ="FILE_URL4";
	public static final String FILE_URL5      ="FILE_URL5";
	public static final String FILE_URL6      ="FILE_URL6";
	public static final String FILE_URL7      ="FILE_URL7";
	public static final String FILE_URL8      ="FILE_URL8";
	public static final String FILE_URL9      ="FILE_URL9";
	public static final String VIEW_URL       ="VIEW_URL";
	public static final String BAD_URL        ="BAD_URL";
	public static final String CORP_CODE      ="CORP_CODE";
	public static final String REMARK         ="REMARK";
	public static final String SEQUENCE	= "LF_BIGFILE_S";
	
	public static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfBigFile", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id",ID);
		columns.put("fileName",FILE_NAME     );
		columns.put("subCount",SUB_COUNT     );
		columns.put("effCount",EFF_COUNT     );
		columns.put("blaCount",BLA_COUNT     );
		columns.put("repCount",REP_COUNT     );
		columns.put("errCount",ERR_COUNT     );
		columns.put("handleStatus",HANDLE_STATUS );
		columns.put("fileStatus",FILE_STATUS   );
		columns.put("taskId",TASKID        );
		columns.put("uploadNum",UPLOAD_NUM       );
		columns.put("oprNum",OPR_NUM       );
		columns.put("effNum",EFF_NUM       );
		columns.put("handleNode",HANDLE_NODE   );
		columns.put("userId",USERID        );
		columns.put("userName",USER_NAME     );
		columns.put("depId",DEP_ID        );
		columns.put("createTime",CREATE_TIME   );
		columns.put("updateTime",UPDATE_TIME   );
		columns.put("fileUrl",FILE_URL      );
		columns.put("fileUrl2",FILE_URL2     );
		columns.put("fileUrl3",FILE_URL3     );
		columns.put("fileUrl4",FILE_URL4     );
		columns.put("fileUrl5",FILE_URL5     );
		columns.put("fileUrl6",FILE_URL6     );
		columns.put("fileUrl7",FILE_URL7     );
		columns.put("fileUrl8",FILE_URL8     );
		columns.put("fileUrl9",FILE_URL9     );
		columns.put("viewUrl",VIEW_URL      );
		columns.put("badUrl",BAD_URL       );
		columns.put("corpCode",CORP_CODE     );
		columns.put("remark",REMARK        );
		columns.put("busId",BUS_ID        );
		columns.put("busName",BUS_NAME        );
		columns.put("busCode",BUS_CODE        );
		
	  } 
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
