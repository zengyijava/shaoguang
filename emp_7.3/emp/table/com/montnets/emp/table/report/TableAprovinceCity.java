/**
 * 
 */
package com.montnets.emp.table.report;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Administrator
 *
 */
public class TableAprovinceCity {

	//表名
	public static final String TABLE_NAME = "A_PROVINCECITY";
	//标识列
	public static final String ID = "ID";
	//网关编号
	public static final String PROVINCE = "PROVINCE";
	//类型
	public static final String CITY = "CITY";
	
	public static final String AREACODE = "AREACODE";

	public static final String PROVINCECODE = "PROVINCECODE";


    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("AprovinceCity", TABLE_NAME);
  		columns.put("tableId", ID);
		columns.put("id", ID);
		columns.put("province",PROVINCE);
		columns.put("city",CITY);
		columns.put("areaCode",AREACODE);
		columns.put("provinceCode",PROVINCECODE);
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
