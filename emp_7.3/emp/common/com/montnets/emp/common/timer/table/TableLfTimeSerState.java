package com.montnets.emp.common.timer.table;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @功能概要：定时服务状态表
 * @项目名称： emp_std_192.169.1.81_2new
 * @初创作者： huangzb <huangzb@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-3-23 下午04:38:56
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class TableLfTimeSerState
{
	//表名
	protected static final String TABLE_NAME = "LF_TimeSerCtrl";
	public static final String TimeServerID = "TimeServerID";
	public static final String UpdateTime = "UpdateTime";
	public static final String DealState = "DealState";
	public static final String NodeId = "NodeId";
	public static final String ServerIP = "ServerIP";
	public static final String ServerPort = "ServerPort";
	public static final String ServerURL = "ServerURL";
	public static final String dbCurrentTime = "dbCurrentTime";
	public static final String ctrlTimeSerID = "ctrlTimeSerID";
	public static final String ctrlTime = "ctrlTime";

	public static final String SEQUENCE = "S_LF_TimeSerCtrl";

	
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfTimeSerCtrl", TABLE_NAME);
		columns.put("tableId", TimeServerID);
		columns.put("sequence", SEQUENCE);
		columns.put("timeServerID", TimeServerID);
		columns.put("updateTime", UpdateTime);
		columns.put("dealState", DealState);
		columns.put("nodeId", NodeId);
		columns.put("serverIP", ServerIP);
		columns.put("serverPort", ServerPort);
		columns.put("serverURL", ServerURL);
		columns.put("dbCurrentTime", dbCurrentTime);
		columns.put("ctrlTimeSerID", ctrlTimeSerID);
		columns.put("ctrlTime", ctrlTime);
		
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
