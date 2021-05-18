package com.montnets.emp.netnews.table;
import java.util.HashMap;
import java.util.Map;

/***
 * 页面存储表
 * @author Administrator
 *
 */
public class TableLfWXPAGE{
	public static final String TABLE_NAME ="LF_WX_PAGE";
	public static final String ID = "ID";					//自动编号
	public static final String NAME = "NAME";			//业务数据编号
	public static final String NETID = "NETID";			//显示名称
	public static final String PARENTID = "PARENTID";		//字段列名
	public static final String CHILDID = "CHILDID";			//数据类型。0：字符串；1：数字；2：日期。
	public static final String CONTENT = "CONTENT";			//数据长度
	public static final String PAGESIZE = "PAGESIZE";			//其他长度，如：小数位。
	public static final String MODIFYID = "MODIFYID";			//设为参数。0：否；1：是
	public static final String MODIFYDATE = "MODIFYDATE";			//素材管理文件类型
	public static final String CREATID = "CREATID";			
	public static final String CREATDATE = "CREATDATE";			
	public static final String LINK = "LINK";		
	
	//序列
	public static final String SEQUENCE = "S_LF_WX_PAGE";
	
	protected static final Map<String, String> columns = new HashMap<String, String>();
	static
	{
		columns.put("LfWXPAGE", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("ID", ID);
		columns.put("NAME", NAME);
		columns.put("NETID", NETID);
		columns.put("PARENTID", PARENTID);
		columns.put("CHILDID", CHILDID);
		columns.put("CONTENT", CONTENT);
		columns.put("PAGESIZE", PAGESIZE);
		columns.put("MODIFYID", MODIFYID);
		columns.put("MODIFYDATE", MODIFYDATE);
		columns.put("CREATID", CREATID);
		columns.put("CREATDATE", CREATDATE);
		columns.put("link", LINK);
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
