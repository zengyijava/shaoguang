/**
 * 
 */
package com.montnets.emp.table.mms;

import java.util.HashMap;
import java.util.Map;

/**
 * 彩信下行记录
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-18 下午06:53:14
 * @description 
 */

public class TableLfMmsMttask
{
	//表名_彩信下行记录
	public static final String TABLE_NAME = "LF_MMSMTTASK";
	//自增
	public static final String MMS_ID = "MMS_ID";
	//操作员ID
	public static final String USER_ID = "USER_ID";
	//手机号码
	public static final String PHONE = "PHONE";
	//彩信内容
	public static final String MMS_MSG="MMS_MSG";
	//发送状态
	public static final String SEND_STATUS="SEND_STATUS";
	//发送时间
	public static final String SEND_TIME="SEND_TIME";
	//序列
	public static final String SEQUENCE = "S_LF_MMSMTTASK";
	
	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfMmsMttask", TABLE_NAME);
		columns.put("tableId", MMS_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("mmsId",MMS_ID);
		columns.put("userId", USER_ID);
		columns.put("phone", PHONE);
		columns.put("mmsMsg", MMS_MSG);
		columns.put("sendStatus", SEND_STATUS);
		columns.put("sendTime", SEND_TIME);
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
