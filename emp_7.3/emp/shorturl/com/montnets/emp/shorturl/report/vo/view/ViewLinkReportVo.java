package com.montnets.emp.shorturl.report.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.shorturl.surlmanage.table.TableLfUrlTask;


public class ViewLinkReportVo {
	
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
	
	static{
		columns.put("url", TableLfUrlTask.NETURL);
		columns.put("totalNum", TableLfUrlTask.SUB_COUNT);
		columns.put("visitCount", "visitCount");
		columns.put("visitNum", "visitNum");
		columns.put("effCount", "EFF_COUNT");
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
