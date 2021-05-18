package com.montnets.emp.netnews.table;

import java.util.HashMap;
import java.util.Map;

/***
 * 网讯类型存储表
 * @author Administrator
 *
 */
public class TableLfWXSORT{
	
	public static final String TABLE_NAME ="LF_WX_SORT";
	public static final String ID = "ID";					//自动编号
	public static final String NAME = "NAME";			//业务数据编号
	public static final String DESCRIPTION = "DESCRIPTION";			//显示名称
	public static final String TYPE = "TYPE";		//字段列名
	public static final String MODIFYID = "MODIFYID";			//数据类型。0：字符串；1：数字；2：日期。
	public static final String MODIFYDATE = "MODIFYDATE";			//数据长度
	public static final String CREATID = "CREATID";			//其他长度，如：小数位。
	public static final String CREATDATE = "CREATDATE";			//设为参数。0：否；1：是
	public static final String FILETYPE = "FILETYPE";			//素材管理文件类型
	public static final String CORPCODE = "CORP_CODE";			//企业编码
	public static final String PARENTID = "PARENTID";
	public static final String sort_path = "sort_path";
	//序列
	public static final String SEQUENCE = "18";
	
	//映射集合
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfWXSORT", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("ID", ID);
		columns.put("NAME", NAME);
		columns.put("DESCRIPTION", DESCRIPTION);
		columns.put("TYPE", TYPE);
		columns.put("MODIFYID", MODIFYID);
		columns.put("MODIFYDATE", MODIFYDATE);
		columns.put("CREATID", CREATID);
		columns.put("CREATDATE", CREATDATE);
		columns.put("FILETYPE", FILETYPE);
		columns.put("corpCode", CORPCODE);
		columns.put("sort_path", sort_path);
		columns.put("parentId", PARENTID);
		columns.put("sequence", SEQUENCE);
		
		
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
