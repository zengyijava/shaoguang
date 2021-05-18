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
public class TableLfWXTrustData
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
		columns.put("LfWXTrustData", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("id", ID);
		columns.put("name", NAME);
		columns.put("tableName", TABLENAME);
		columns.put("type", TYPE);
		columns.put("name", NAME);
		columns.put("colnum", COLNUM);
		columns.put("status", STATUS);
		columns.put("modifyId", MODIFYID);
		columns.put("modifyDate", MODIFYDATE);
		columns.put("creatId", CREATID);
		columns.put("creatDate", CREATDATE);
		columns.put("url", URL);
		columns.put("auditUserId", AUDITUSERID);
		columns.put("auditDate", AUDITDATE);
		columns.put("corpCode", CORPCODE);
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
