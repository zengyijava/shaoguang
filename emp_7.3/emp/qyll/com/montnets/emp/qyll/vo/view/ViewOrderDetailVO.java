package com.montnets.emp.qyll.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jason Huang
 * @date 2017年10月31日 上午10:12:52
 */

public class ViewOrderDetailVO {
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

	static
	{
		
		columns.put("errcode", "ERRCODE");
		columns.put("mobile", "MOBILE");
		columns.put("theme", "PRODUCTNAME");
		columns.put("isp", "ISP");
		columns.put("state", "LLRPT");
		columns.put("orderno", "ORDERNO");
		columns.put("organization", "DEP_NAME");
		columns.put("operator", "NAME");
		columns.put("ordertm", "ORDERTM");
		columns.put("rpttm", "RPTTM");
	};

	public static Map<String, String> getORM()
	{
		return columns;
	}

}
