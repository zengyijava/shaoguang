package com.montnets.emp.table.notice;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @project montnets_entity
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-30 下午03:14:08
 * @description
 */
public class TableLfNotice
{
	public static final String TABLE_NAME = "LF_NOTICE";

	public static final String NOTICE_ID = "NOTICE_ID";

	public static final String USER_ID = "USER_ID";

	public static final String TITLE = "TITLE";

	public static final String CONTEXT = "CONTEXT";

	public static final String PUBLISH_TIME = "PUBLISH_TIME";
	
	public static final String CORP_CODE = "CORP_CODE";
	
	public static final String NOTE_TAIL = "NOTE_TAIL";
	public static final String NOTE_STATE = "NOTE_STATE";
	public static final String NOTE_VALID = "NOTE_VALID";

	public static final String SEQUENCE = "S_LF_NOTICE";

	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfNotice", TABLE_NAME);
		columns.put("tableId", NOTICE_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("noticeID", NOTICE_ID);
		columns.put("userID", USER_ID);
		columns.put("title", TITLE);
		columns.put("context", CONTEXT);
		columns.put("publishTime", PUBLISH_TIME);
		columns.put("corpcode", CORP_CODE);
		
		columns.put("noteTail", NOTE_TAIL);
		columns.put("noteState", NOTE_STATE);
		columns.put("noteValid", NOTE_VALID);
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
