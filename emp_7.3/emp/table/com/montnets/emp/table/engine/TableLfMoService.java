/**
 * 
 */
package com.montnets.emp.table.engine;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-20 下午02:26:03
 * @description
 */

public class TableLfMoService
{

	public static final String TABLE_NAME = "LF_MO_SERVICE";
	//自增ID
	public static final String MS_ID = "MS_ID";
	//该上行用户的账号
	public static final String SP_USER = "SP_USER";
	//上行通道号
	public static final String SPNUMBER = "SPNUMBER";
	//上行内容
	public static final String MSGCONTENT = "MSGCONTENT";
	//接收时间
	public static final String DELIVERTIME = "DELIVERTIME";
	//上行手机
	public static final String PHONE = "PHONE";
	//步骤ID
	public static final String PR_ID = "PR_ID";
	//业务ID
	public static final String SER_ID = "SER_ID";
	//序列
	public static final String SEQUENCE = "S_LF_MO_SERVICE";
	
	//回复状态，1-成功，2和空-未回复
	public static final String REPLY_STATE = "REPLY_STATE";
	//回复号码内容url
	public static final String REPLY_URL = "REPLY_URL";
	//企业编码
	public static final String CORP_CODE = "CORP_CODE";

	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfMoService", TABLE_NAME);
		columns.put("tableId", MS_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("msId", MS_ID);
		columns.put("spUser", SP_USER);
		columns.put("spnumber", SPNUMBER);
		columns.put("msgContent", MSGCONTENT);
		columns.put("deliverTime", DELIVERTIME);
		columns.put("phone", PHONE);
		columns.put("prId", PR_ID);
		columns.put("serId", SER_ID);
		columns.put("replyState", REPLY_STATE);
		columns.put("replyUrl", REPLY_URL);
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
