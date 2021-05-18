package com.montnets.emp.netnews.table;

import com.montnets.emp.common.constant.StaticValue;

import java.util.HashMap;
import java.util.Map;

public class TableLfWxVisitLog
{

	public static final String TABLE_NAME = "LF_WX_VISITLOG";

	public static final String id = "id";

	// 关联编号
	public static final String refid = "refid";

	// 类型。0：网讯；1：投票
	public static final String type = "type";

	public static final String phone = "phone";

	// 访问时间
	public static final String visitdate = "visitdate";

	// 访问状态。0：允许；1：拒绝
	public static final String visitstatus = "visitstatus";

	// 拒绝为拒绝的原因
	public static final String memo = "memo";

	//public static final String uid = "uid";
	
	public static final String uid = (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE||StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)?"uid":"\"uid\"";

	// 访问浏览器
	public static final String userAgent = "userAgent";

	// 访问ip地址
	public static final String ipAddr = "ipAddr";
	
	// 企业编码
	public static final String corpCode = "corp_code";

	// 任务ID
	public static final String TASKID = "TASKID";
	
	// 序列
	public static final String SEQUENCE = "S_LF_WX_VISITLOG";

	protected static final Map<String, String> columns = new HashMap<String, String>();
	static
	{
		columns.put("LfWxVisitLog", TABLE_NAME);
		columns.put("tableId", id);

		columns.put("id", id);
		columns.put("refid", refid);
		columns.put("type", type);
		columns.put("phone", phone);
		columns.put("visitdate", visitdate);
		// 访问状态。0：允许；1：拒绝
		columns.put("visitstatus", visitstatus);
		// 拒绝为拒绝的原因
		columns.put("memo", memo);
		columns.put("uid", uid);
		// 访问浏览器
		columns.put("userAgent", userAgent);
		// 访问ip地址
		columns.put("ipAddr", ipAddr);
		columns.put("corpCode", corpCode);
		columns.put("taskid", TASKID);
		columns.put("sequence", SEQUENCE);

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
