/**
 * 
 */
package com.montnets.emp.table.perfect;

import java.util.HashMap;
import java.util.Map;

/**
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-9-24 上午09:32:06
 * @description 
 */

public class TableLfPerfectNotic
{
	public static final String TABLE_NAME = "LF_PERFECT_NOTIC";

	public static final String P_NOTIC_ID = "P_NOTIC_ID";

	public static final String SUBMITTIME = "SUBMITTIME";
	
	public static final String SEND_INTERAL = "SEND_INTERAL";

	public static final String MAX_SENDCOUNT = "MAX_SENDCOUNT";

	public static final String ARY_SENDCOUNT = "ARY_SENDCOUNT";

	public static final String SENDER_GUID = "SENDER_GUID";

	public static final String CONTENT = "CONTENT";
	
	public static final String RECEIVER_TYPE = "RECEIVER_TYPE";
	
	public static final String DIALOGID = "DIALOGID";
	
	public static final String TASKID = "TASKID";
	

	public static final String  RECEVIERID = "RECEVIERID";
	
	public static final String NOTIC_COUNT = "NOTIC_COUNT";
	
	public static final String CORP_CODE = "CORP_CODE";
	
	public static final String SEQUENCE = "S_LF_PERFECT_NOTIC";

	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfPerfectNotic", TABLE_NAME);
		columns.put("tableId", P_NOTIC_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("noticId",P_NOTIC_ID );
		columns.put("submitTime",SUBMITTIME);
		columns.put("sendInterval",SEND_INTERAL );
		columns.put("maxSendCount", MAX_SENDCOUNT);
		columns.put("arySendCount", ARY_SENDCOUNT);
		columns.put("senderGuid", SENDER_GUID);
		columns.put("content",CONTENT );
		columns.put("recevierType", RECEIVER_TYPE);
		columns.put("recevierId",RECEVIERID );
		columns.put("dialogId",DIALOGID );
		columns.put("taskId", TASKID);
		columns.put("noticCount", NOTIC_COUNT);
		columns.put("corpCode", CORP_CODE);
	 
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
