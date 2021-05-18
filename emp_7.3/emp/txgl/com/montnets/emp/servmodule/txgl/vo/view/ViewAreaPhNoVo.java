package com.montnets.emp.servmodule.txgl.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewAreaPhNoVo {
	protected final static Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	private String moblie;
	private String areaCode;
	private String province;
	private String servePro;
	private String city;
	private String id;
	static
	{
		columns.put("moblie", "MOBILE");
		columns.put("areaCode", "AREACODE");
		columns.put("province", "PROVINCE");
		columns.put("servePro", "SERVEPRO");
		columns.put("city", "CITY");
		columns.put("id", "ID");
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
