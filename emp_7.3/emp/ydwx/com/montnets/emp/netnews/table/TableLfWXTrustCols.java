package com.montnets.emp.netnews.table;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vincent
 * @version 1.0
 * describe 业务数据结构表（LF_WX_TRUSTDATA_COLS）
 * date 2011.12.1
 * **/

public class TableLfWXTrustCols implements Serializable{
	
	public static final String TABLE_NAME ="LF_WX_TRUSTCOLS";
	public static final String ID = "ID";					//自动编号
	public static final String TRUSTID = "TRUSTID";			//业务数据编号
	public static final String NAME = "NAME";			//显示名称
	public static final String COLNAME = "COLNAME";		//字段列名
	public static final String COLTYPE = "COLTYPE";			//数据类型。0：字符串；1：数字；2：日期。
	public static final String COLSIZE = "COLSIZE";			//数据长度
	public static final String OTHERSIZE = "OTHERSIZE";			//其他长度，如：小数位。
	public static final String ISPARAM = "ISPARAM";			//设为参数。0：否；1：是
	//private static final String PH ="PH";
	
	//映射集合
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfWXTrustCols", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("id", ID);
		columns.put("trustId", TRUSTID);
		columns.put("name", NAME);
		columns.put("colName", COLNAME);
		columns.put("colType", COLTYPE);
		columns.put("colSize", COLSIZE);
		columns.put("otherSize", OTHERSIZE);
		columns.put("isParam", ISPARAM);
		//columns.put("PH", PH);
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
