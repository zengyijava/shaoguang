package com.montnets.emp.qyll.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jason Huang
 * @date 2017年10月31日 下午2:11:02
 */

public class ViewOrderTaskVO {
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

	static {
		columns.put("id", "ID");
		columns.put("operator", "NAME");
		columns.put("organization", "DEP_NAME");
		columns.put("topic", "TOPIC");
		columns.put("orderNo", "ORDERNO");
		columns.put("msg", "MSG");
		columns.put("orderState", "ORDERSTATUS");
		columns.put("smsState", "SMSSTATUS");
		columns.put("createtm", "CREATETM");
		columns.put("ordertm", "ORDERTM");
		columns.put("subCount", "SUBCOUNT");
		//columns.put("effCount", "EFFCOUNT");
		columns.put("sucCount", "SUCCOUNT");
		//columns.put("faiCount", "FAICOUNT");
		columns.put("productIds", "PRO_IDS");
	};

	public static Map<String, String> getORM() { 
		return columns;
	}
}
