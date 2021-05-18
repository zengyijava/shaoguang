package com.montnets.emp.netnews.table;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:22
 * @description
 */
public class TableLfWXUploadFile
{
	//表名：
	public static final String TABLE_NAME = "LF_WX_UPLOADFILE";

	public static final String ID = "ID";

	public static final String NETID = "NETID";

	public static final String FILENAME = "FILENAME";

	public static final String FILEVER = "FILEVER";

	public static final String FILEDESC = "FILEDESC";

	public static final String UPLOADDATE = "UPLOADDATE";

	public static final String UPLOADUSERID = "UPLOADUSERID";

	public static final String WEBURL = "WEBURL";

	public static final String STATUS = "STATUS";

	public static final String MODIFYID = "MODIFYID";

	public static final String MODIFYDATE = "MODIFYDATE";

	public static final String CREATID = "CREATID";

	public static final String CREATDATE = "CREATDATE";

	public static final String NAMETEMP = "NAMETEMP";
	
	public static final String SORTID = "SORTID";
	public static final String DATE = "DATE";
	public static final String TYPENAME = "TYPENAME";
	public static final String FILETYPE = "FILETYPE";
	public static final String USERTYPE = "USERTYPE";
	public static final String TYPE = "TYPE";
	public static final String FILESIZE = "FILESIZE"; 
	public static final String CORP_CODE = "CORP_CODE"; 
	//序列
	public static final String SEQUENCE = "S_LF_WX_UPLOADFILE";


	//映射集合
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfWXUploadFile", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("ID", ID);
		columns.put("NETID", NETID);
		columns.put("FILENAME", FILENAME);
		columns.put("FILEVER", FILEVER);
		columns.put("FILEDESC", FILEDESC);
		columns.put("UPLOADDATE", UPLOADDATE);
		columns.put("UPLOADUSERID", UPLOADUSERID);
		columns.put("WEBURL", WEBURL);
		columns.put("STATUS", STATUS);
		columns.put("MODIFYID", MODIFYID);
		columns.put("MODIFYDATE", MODIFYDATE);
		columns.put("CREATID", CREATID);
		columns.put("CREATDATE", CREATDATE);
		columns.put("NAMETEMP", NAMETEMP);
		columns.put("SORTID", SORTID);
		columns.put("type", TYPE);
		columns.put("FILESIZE", FILESIZE);
		columns.put("CORP_CODE", CORP_CODE);
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
