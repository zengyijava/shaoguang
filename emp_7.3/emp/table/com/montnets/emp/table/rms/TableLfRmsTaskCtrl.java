package com.montnets.emp.table.rms;

import java.util.HashMap;
import java.util.Map;

public class TableLfRmsTaskCtrl {
	
	public static final String TABLE_NAME = "LF_RMSTASK_CTRL";
	
	public static final String ID = "ID";
	
	public static final String TASKID = "TASKID";
	
	public static final String CURRENT_COUNT = "CURRENT_COUNT";
	
	public static final String UPDATE_TIME = "UPDATE_TIME";
	
	public static final String SEQUENCE = "S_LF_RMSTASK_CTRL";

    protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */ 
	static{
		columns.put("LfRmsTaskCtrl",TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("id", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("taskId", TASKID);
		columns.put("currentCount", CURRENT_COUNT);
		columns.put("updateTime", UPDATE_TIME);
	}
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
