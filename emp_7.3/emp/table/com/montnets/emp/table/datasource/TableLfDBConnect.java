/**
/**
 *
 */
package com.montnets.emp.table.datasource;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-21 下午02:40:19
 * @description
 */

public class TableLfDBConnect
{
	//表名  数据库连接表
	public static final String TABLE_NAME = "LF_DBCONNECT";
	//标识ID
	public static final String DB_ID = "DB_ID";
	//数据连接说明
	public static final String DBCON_NAME = "DBCON_NAME";
	//服务器IP
	public static final String DBCON_IP = "DBCON_IP";
	//数据库类型
	public static final String DB_TYPE = "DB_TYPE";
	//实例名
	public static final String SERVICE_NAME = "SERVICE_NAME";
	//数据库名
	public static final String DB_NAME = "DB_NAME";
	//端口号
	public static final String PORT = "PORT";
	//用户
	public static final String DB_USER = "DB_USER";
	//密码
	public static final String DB_PASS = "DB_PWD";
	//连接字符串
	public static final String CON_STR = "CON_STR";
	//描述
	public static final String COMMENTS = "COMMENTS";
	//ORACLEL连接名称类型(0 service_name，1 SID)
	public static final String DBCONN_TYPE = "DBCONN_TYPE";
	//企业编码
	public static final String CORP_CODE = "CORP_CODE";
	//序列名
	public static final String SEQUENCE = "S_LF_DBCONNECT";

	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfDBConnect", TABLE_NAME);
		columns.put("tableId", DB_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("dbId", DB_ID);
		columns.put("dbconName", DBCON_NAME);
		columns.put("dbconIP", DBCON_IP);
		columns.put("dbType", DB_TYPE);
		columns.put("serviceName", SERVICE_NAME);
		columns.put("dbName", DB_NAME);
		columns.put("port", PORT);
		columns.put("dbUser", DB_USER);
		columns.put("dbPwd", DB_PASS);
		columns.put("conStr", CON_STR);
		columns.put("comments", COMMENTS);
		columns.put("dbconnType", DBCONN_TYPE);
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
