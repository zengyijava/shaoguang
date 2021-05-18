package com.montnets.emp.table.form;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfFomInfo
 *
 * @project p_fom
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfFomInfo
{
	// 表名
	public static final String				TABLE_NAME	= "LF_FOM_INFO";


    public static final String			F_ID	= "F_ID";

    public static final String			TYPE_ID	= "TYPE_ID";
    
    public static final String          PARENT_ID = "PARENT_ID";

    public static final String			TITLE	= "TITLE";

    public static final String			NOTE	= "NOTE";

    public static final String			PUBLISH_STATE	= "PUBLISH_STATE";

    public static final String			URL	= "URL";

    public static final String			SUBMIT_COUNT	= "SUBMIT_COUNT";

    public static final String			SEQ_NUM	= "SEQ_NUM";

    public static final String			IS_SYSTEM	= "IS_SYSTEM";

    public static final String			CORP_CODE	= "CORP_CODE";

    public static final String			CREATETIME	= "CREATETIME";

    public static final String			MODITYTIME	= "MODITYTIME";

	// 序列
	public static final String			SEQUENCE	= "30";

	// 映射集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfFomInfo", TABLE_NAME);
		columns.put("tableId", F_ID);
		columns.put("sequence", SEQUENCE);
        columns.put("fId", F_ID);
        columns.put("typeId", TYPE_ID);
        columns.put("parentId", PARENT_ID);
        columns.put("title", TITLE);
        columns.put("note", NOTE);
        columns.put("publishState", PUBLISH_STATE);
        columns.put("url", URL);
        columns.put("submitCount", SUBMIT_COUNT);
        columns.put("seqNum", SEQ_NUM);
        columns.put("isSystem", IS_SYSTEM);
        columns.put("corpCode", CORP_CODE);
        columns.put("createtime", CREATETIME);
        columns.put("moditytime", MODITYTIME);
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