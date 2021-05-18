					
package com.montnets.emp.table.system;

import java.util.HashMap;
import java.util.Map;

public class TableLfSysmttask
{
	public static final String TABLE_NAME	= "LF_SYSMTTASK";

   public static final String MOBILE_URL= "MOBILE_URL";

   public static final String BMTTYPE= "BMTTYPE";

   public static final String USER_ID= "USER_ID";

   public static final String SUBMITTIME= "SUBMITTIME";

   public static final String SEND_TYPE= "SEND_TYPE";

   public static final String SP_USER= "SP_USER";

   public static final String CORP_CODE= "CORP_CODE";

   public static final String FAI_COUNT= "FAI_COUNT";

   public static final String SUB_COUNT= "SUB_COUNT";

   public static final String PHONE= "PHONE";

   public static final String ICOUNT= "ICOUNT";

   public static final String MSG= "MSG";

   public static final String MS_TYPE= "MS_TYPE";

   public static final String ID= "ID";

   public static final String SUC_COUNT= "SUC_COUNT";

   public static final String TITLE= "TITLE";
   
	//发送状态。0是未提交网关，1提交网关成功,2提交网关失败,3网关处理完成
   public static final String SENDSTATE = "SENDSTATE";
	//任务Id
   public static final String TASKID = "TASKID";
	//业务编码
   public static final String BUS_CODE = "BUS_CODE";
	//错误编码
   public static final String ERROR_CODES = "ERROR_CODES";
	//传给网关的用户编码
   public static final String USER_CODE = "USER_CODE";

   public static final String SEQUENCE	= "S_LF_SYSMTTASK";
   protected final static Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfSysmttask", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
       
			columns.put("mobileurl", MOBILE_URL);
        
			columns.put("bmttype", BMTTYPE);
        
			columns.put("userid", USER_ID);
        
			columns.put("submittime", SUBMITTIME);
        
			columns.put("sendtype", SEND_TYPE);
        
			columns.put("spuser", SP_USER);
        
			columns.put("corpcode", CORP_CODE);
        
			columns.put("faicount", FAI_COUNT);
        
			columns.put("subcount", SUB_COUNT);
        
			columns.put("phone", PHONE);
        
			columns.put("icount", ICOUNT);
        
			columns.put("msg", MSG);
        
			columns.put("mstype", MS_TYPE);
        
			columns.put("id", ID);
        
			columns.put("succount", SUC_COUNT);
        
			columns.put("title", TITLE);
			
			//发送状态。0是未提交网关，1提交网关成功,2提交网关失败,3网关处理完成
			columns.put("sendState", SENDSTATE);
			//任务Id
			columns.put("taskId", TASKID);
			//业务编码
			columns.put("busCode", BUS_CODE);
			//错误编码
			columns.put("errorCodes", ERROR_CODES);
			//传给网关的用户编码
			columns.put("userCode", USER_CODE);
        
	};


	public static Map<String , String> getORM()
	{
		return columns;
	}
}

					