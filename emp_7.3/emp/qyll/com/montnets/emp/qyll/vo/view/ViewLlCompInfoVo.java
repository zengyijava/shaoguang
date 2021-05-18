package com.montnets.emp.qyll.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewLlCompInfoVo {
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("password", "PASSWORD");
		columns.put("corpCode", "CORPCODE");
		columns.put("ecName", "ECNAME");
		columns.put("ip", "IP");
		columns.put("reMark", "REMARK");
		columns.put("pushAddr", "PUSHADDR");
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
