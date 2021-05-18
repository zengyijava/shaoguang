package com.montnets.emp.rms.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jason Huang
 * @date 2018年1月19日 下午4:50:25
 */

public class ViewUserDataVO {
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
	static {
		columns.put("userId", "USERID");
		columns.put("passWord", "USERPASSWORD");
	}

	public static Map<String, String> getORM() {
		return columns;
	}
}
