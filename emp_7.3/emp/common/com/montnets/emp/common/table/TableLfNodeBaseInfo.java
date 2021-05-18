package com.montnets.emp.common.table;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @功能概要：节点基础信息表
 * @项目名称： emp_std_192.169.1.81_2new
 * @初创作者： huangzb <huangzb@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-3-29 上午09:35:02
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class TableLfNodeBaseInfo
{
	//表名
	protected static final String TABLE_NAME = "LF_NodeBaseInfo";
	public static final String NodeId = "NodeId";
	public static final String ServerIP = "ServerIP";
	public static final String ServerPort = "ServerPort";
	public static final String SerLocalURL = "SerLocalURL";
	public static final String SerInternetURL = "SerInternetURL";
	public static final String UpdateTime = "UpdateTime";

	public static final String SEQUENCE = "S_LF_NodeBaseInfo";

	
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfNodeBaseInfo", TABLE_NAME);
		//columns.put("tableId", NodeId);
		columns.put("sequence", SEQUENCE);
		columns.put("nodeId", NodeId);
		columns.put("serverIP", ServerIP);
		columns.put("serverPort", ServerPort);
		columns.put("serLocalURL", SerLocalURL);
		columns.put("serInternetURL", SerInternetURL);
		columns.put("updateTime", UpdateTime);
		
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
