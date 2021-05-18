package com.montnets.emp.qyll.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jason Huang
 * @date 2017年11月1日 下午2:53:53
 */

public class ViewProductVO {
	protected final static Map<String, String> columns = new LinkedHashMap<String, String>();

	static {
		columns.put("productId", "PRODUCTID");
		columns.put("productName", "PRODUCTNAME");
		columns.put("isp", "ISP");
		columns.put("count", "COUNT");
	};

	public static Map<String, String> getORM() {
		return columns;
	}
}
