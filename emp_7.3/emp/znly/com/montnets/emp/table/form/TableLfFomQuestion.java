package com.montnets.emp.table.form;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfFomQuestion
 *
 * @project p_fom
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfFomQuestion
{
	// 表名
	public static final String				TABLE_NAME	= "LF_FOM_QUESTION";


    public static final String			Q_ID	= "Q_ID";

    public static final String			F_ID	= "F_ID";

    public static final String			FILED_TYPE	= "FILED_TYPE";

    public static final String			TITLE	= "TITLE";

    public static final String			FORM_DATA	= "FORM_DATA";

    public static final String			SEQ_NUM	= "SEQ_NUM";

    public static final String			CORP_CODE	= "CORP_CODE";

    public static final String			CREATETIME	= "CREATETIME";

    public static final String			MODITYTIME	= "MODITYTIME";

	// 序列
	public static final String			SEQUENCE	= "31";

	// 映射集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfFomQuestion", TABLE_NAME);
		columns.put("tableId", Q_ID);
		columns.put("sequence", SEQUENCE);
        columns.put("qId", Q_ID);
        columns.put("fId", F_ID);
        columns.put("filedType", FILED_TYPE);
        columns.put("title", TITLE);
        columns.put("formData", FORM_DATA);
        columns.put("seqNum", SEQ_NUM);
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