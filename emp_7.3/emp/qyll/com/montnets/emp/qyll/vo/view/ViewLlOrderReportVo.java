package com.montnets.emp.qyll.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewLlOrderReportVo {
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("reportDate", "REPORTDATE");
		columns.put("productName", "PRODUCTNAME");
		columns.put("productId", "PRODUCTID");
		columns.put("isp", "ISP");
		columns.put("sunMitNum", "SUNMITNUM");
		columns.put("succNum", "SUCCNUM");
		columns.put("faildNum", "FAILDNUM");
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
