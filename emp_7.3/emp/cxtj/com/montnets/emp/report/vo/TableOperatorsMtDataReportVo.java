package com.montnets.emp.report.vo;

import java.util.HashMap;
import java.util.Map;

public class TableOperatorsMtDataReportVo {	
	
	public static final String SPID="SPID";

	protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("spID", SPID);
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
