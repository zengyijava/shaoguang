package com.montnets.emp.shorturl.report.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;


public class ViewVisitDetailVo {
	
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
	
	static{
		columns.put("phone", "PHONE");
		columns.put("visitTime", "VSTTM");
		columns.put("lastIP", "SRCADDRESS");
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
