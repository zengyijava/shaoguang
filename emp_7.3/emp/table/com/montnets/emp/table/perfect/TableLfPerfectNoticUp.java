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
 * @datetime 2011-9-24 上午09:38:46
 * @description 
 */

public class TableLfPerfectNoticUp
{
	//表名 完美通知
	public static final String TABLE_NAME = "LF_P_N_UP";
	//通知回复标识ID
	public static final String PNUP_ID = "PNUP_ID";
	//完美通知ID
	public static final String P_NOTIC = "P_NOTIC";
	//发送者GUID	
	public static final String SENDER_GUID = "SENDER_GUID";
	//回复内容
	public static final String CONTENT = "CONTENT";
	//发送时间
	public static final String SEND_TIME = "SEND_TIME";
	//接收者guid 
	public static final String RECEIVERID = "RECEIVERID";
	//接收者类型
	public static final String USERTYPE = "USERTYPE";
	//名字
	public static final String NAME = "NAME";
	//是否回复
	public static final String ISREPLY = "ISREPLY";
	//是否接收
	public static final String ISRECEIVER = "ISRECEIVER";
	//会话ID
	public static final String DIALOGIN = "DIALOGIN";
	//任务ID
	public static final String TASKID = "TASKID";
	//联系电话	
	public static final String MOBILE = "MOBILE";
	//接收次数
	public static final String RECEIVE_COUNT = "RECEIVE_COUNT";
	//发送账号
	public static final String SPUSER = "SPUSER";
	//全通道号
	public static final String SPNUMBER = "SPNUMBER";
	//创建时间
	public static final String CREATE_TIME = "CREATE_TIME";
	//标志没有接收到状态报告的完美通知 '1':已经接收  ‘2’：接收中   ‘3’：没有状态报告返回
	public static final String ISATRRED  = "ISATRRED";
	//序列
	public static final String SEQUENCE = "S_LF_P_N_UP";

	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfPerfectNoticUp", TABLE_NAME);
		columns.put("tableId", PNUP_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("pnupId",PNUP_ID );
		columns.put("pnotic",P_NOTIC );
		columns.put("senderGuid", SENDER_GUID);
		columns.put("content", CONTENT);
		columns.put("sendTime", SEND_TIME);
		columns.put("receiverId", RECEIVERID);
		columns.put("userType", USERTYPE);
		columns.put("name",NAME );
		columns.put("isReply",ISREPLY);
		columns.put("isReceive",ISRECEIVER );
		columns.put("dialogId", DIALOGIN);
		columns.put("taskId", TASKID);
		columns.put("mobile", MOBILE);
		columns.put("receiveCount", RECEIVE_COUNT);
		columns.put("spUser", SPUSER);
		columns.put("spNumber", SPNUMBER);
		columns.put("createTime", CREATE_TIME);
		columns.put("isAtrred", ISATRRED);
 
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
