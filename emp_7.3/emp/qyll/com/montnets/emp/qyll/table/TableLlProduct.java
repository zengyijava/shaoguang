package com.montnets.emp.qyll.table;

import java.util.HashMap;
import java.util.Map;

public class TableLlProduct {
	//表名：
	public static final String TABLE_NAME = "LL_PRODUCT";
	
	public static final String ID = "ID";
	
	public static final String ECID = "ECID"; 
	
	public static final String PRODUCTID = "PRODUCTID"; 
	
	public static final String PRODUCTNAME = "PRODUCTNAME"; 
	
	public static final String ISP = "ISP"; 
	
	public static final String VOLUME = "VOLUME"; 
	
	public static final String PRICE = "PRICE"; 
	
	public static final String DISCPRICE = "DISCPRICE"; 
	
	public static final String AREA = "AREA"; 
	
	public static final String PTYPE = "PTYPE"; 
	
	public static final String PMOLD = "PMOLD"; 
	
	public static final String RTYPE = "RTYPE"; 
	
	public static final String STATUS = "STATUS"; 
	
	public static final String UPDATETM = "UPDATETM"; 
	
	public static final String CREATETM = "CREATETM"; 
	
	public static final String OPERATORID = "OPERATORID"; 
	
	//映射集合
	protected static final Map<String, String> columns = new HashMap<String, String>();
	
	static
	{
		columns.put("LlProduct", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("id", ID);
		columns.put("ecid", ECID);
		columns.put("productid", PRODUCTID);
		columns.put("productname", PRODUCTNAME);
		columns.put("isp", ISP);
		columns.put("volume", VOLUME);
		columns.put("price", PRICE);
		columns.put("discprice", DISCPRICE);
		columns.put("area", AREA);
		columns.put("ptype", PTYPE);
		columns.put("pmold", PMOLD);
		columns.put("rtype", RTYPE);
		columns.put("status", STATUS);
		columns.put("updatetm", UPDATETM);
		columns.put("createtm", CREATETM);
		columns.put("operatorid", OPERATORID);
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
