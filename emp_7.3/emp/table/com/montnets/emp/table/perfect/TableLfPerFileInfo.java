/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-10-29 上午10:07:21
 */
package com.montnets.emp.table.perfect;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 
 * @project p_dxzs
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-10-29 上午10:07:21
 */

public class TableLfPerFileInfo
{
	//完美通知发送信息表
	public static final String TABLE_NAME = "LF_PER_FILEINFO";
	//任务ID
	public static final String TASKID = "TASKID";
	//发送文件路径
	public static final String  SENDFILENAME = "SENDFILENAME";
	
	protected static final Map<String, String> columns = new HashMap<String, String>();
	static
	{
		columns.put("LfPerFileInfo", TABLE_NAME);
		columns.put("taskId", TASKID);
		columns.put("sendFileName", SENDFILENAME);
	}
	
	
	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-10-29 上午10:11:22
	 */
	public static Map<String, String> getORM()
	{
		return columns;

	}
}
