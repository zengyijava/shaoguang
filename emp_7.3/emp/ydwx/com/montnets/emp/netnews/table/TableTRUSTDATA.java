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
public class TableTRUSTDATA
{
	//表名：
	public static final String TABLE_NAME = "LF_WX_TRUSTDATA";

	public static final String ID = "ID";

	public static final String NAME = "NAME";

	public static final String TABLENAME = "TABLENAME";

	public static final String TYPE = "TYPE";

	public static final String COLNUM = "COLNUM";

	public static final String STATUS = "STATUS";

	public static final String MODIFYID = "MODIFYID";

	public static final String MODIFYDATE = "MODIFYDATE";

	public static final String CREATID = "CREATID";

	public static final String CREATDATE = "CREATDATE";

	public static final String URL = "URL";

	public static final String AUDITUSERID = "AUDITUSERID";

	public static final String AUDITDATE = "AUDITDATE";

	public static final String CORPCODE = "CORP_CODE";


	//映射集合
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("TRUSTDATA", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("NAME", NAME);
		columns.put("TABLENAME", TABLENAME);
		columns.put("TYPE", TYPE);
		columns.put("COLNUM", COLNUM);
		columns.put("STATUS", STATUS);
		columns.put("MODIFYID", MODIFYID);
		columns.put("MODIFYDATE", MODIFYDATE);
		columns.put("CREATID", CREATID);
		columns.put("CREATDATE", CREATDATE);
		columns.put("URL", URL);
		columns.put("AUDITUSERID", AUDITUSERID);
		columns.put("AUDITDATE", AUDITDATE);
		columns.put("CORP_CODE", CORPCODE);
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
