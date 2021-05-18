/**
 * 
 */
package com.montnets.emp.table.engine;

import java.util.HashMap;
import java.util.Map;


/**
 * 上行短信任务
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-4 上午11:34:08
 * @description
 */

public class TableLfMotask
{
	//表名——上行短信任务表
	public static final String TABLE_NAME = "LF_MOTASK";
	//标识
	public static final String MO_ID = "MO_ID";
	//梦网短信平台自编的流水号
	public static final String PTMSGID = "PTMSGID";
	//该上行所属用户的UID
	public static final String UIDS = "UIDS";
	//递送该条上行信息的通道的UID
	public static final String ORGUID = "ORGUID";
	//该上行所属用户的企业的ID
	public static final String ECID = "ECID";
	// 该上行所属用户的账号
	public static final String SP_USER = "SP_USER";
	// 上行通道
	public static final String SPNUMBER = "SPNUMBER";
	// 服务类型
	public static final String SERVICEID = "SERVICEID";
	// 服务代码
	public static final String SPSC = "SPSC";
	//上行的发送状态(0：MO送往SP成功，1：网关接收MO成功)
	public static final String SENDSTATUS = "SENDSTATUS";
	// 消息编码格式
	public static final String MSGFMT = "MSGFMT";

	public static final String TP_PID = "TP_PID";

	public static final String TP_UDHI = "TP_UDHI";
	// 接收该上行的时间
	public static final String DELIVERTIME = "DELIVERTIME";
	// 上行手机号
	public static final String PHONE = "PHONE";
	// 上行内容
	public static final String MSGCONTENT = "MSGCONTENT";
	//上行发至应用系统的时间
	public static final String DONETIME = "DONETIME";
	//0移动，1联通，21电信
	public static final String SPISUNCM = "SPISUNCM";

	public static final String SEQUENCE = "S_LF_MOTASK";
	//操作员GUID
	public static final String USER_GUID = "USER_GUID";
	//模块编号
	public static final String MENUCODE = "MENUCODE";
	//机构ID
	public static final String DEP_ID = "DEP_ID";
	//任务ID
	public static final String TASK_ID = "TASK_ID";
	//业务编码
	public static final String BUS_CODE = "BUS_CODE";
	//所属企业编码
	public static final String CORPCODE = "CORPCODE";
	//尾号
	public static final String SUBNO = "SUBNO";
	//指令
	public static final String MOORDER = "MOORDER";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfMotask", TABLE_NAME);
		columns.put("tableId", MO_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("moId", MO_ID);
		columns.put("ptMsgId", PTMSGID);
		columns.put("uids", UIDS);
		columns.put("orgUid", ORGUID);
		columns.put("ecid", ECID);
		columns.put("spUser", SP_USER);
		columns.put("spnumber", SPNUMBER);
		columns.put("serviceId", SERVICEID);
		columns.put("spsc", SPSC);
		columns.put("sendStatus", SENDSTATUS);
		columns.put("msgFmt", MSGFMT);
		columns.put("tpPid", TP_PID);
		columns.put("tpUdhi", TP_UDHI);
		columns.put("deliverTime", DELIVERTIME);
		columns.put("phone", PHONE);
		columns.put("msgContent", MSGCONTENT);
		columns.put("doneTime", DONETIME);
		columns.put("spisuncm", SPISUNCM);
		
		columns.put("userGuid", USER_GUID);
		columns.put("menuCode", MENUCODE);
		columns.put("depId", DEP_ID);
		columns.put("taskId", TASK_ID);
		columns.put("busCode", BUS_CODE);
		columns.put("corpCode", CORPCODE);
		columns.put("subno", SUBNO);
		columns.put("moOrder", MOORDER);
		
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
