package com.montnets.emp.report.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 下行记录
 * @project sinolife
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-17 下午02:10:07
 * @description
 */
public class ViewAreaReportVo
{
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("y", "Y");
		columns.put("imonth", "IMONTH");
		columns.put("iymd", "IYMD");
		columns.put("province", "PROVINCE");
		columns.put("rfail2", "RFAIL2");
		columns.put("icount", "ICOUNT");
		columns.put("rsucc", "RSUCC");
		columns.put("rfail1", "RFAIL1");
		columns.put("rnret", "RNRET");
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
