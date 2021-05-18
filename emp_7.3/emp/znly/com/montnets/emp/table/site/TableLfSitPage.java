package com.montnets.emp.table.site;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfSitPage
 *
 * @project p_sit
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfSitPage
{
	// 表名
	public static final String				TABLE_NAME	= "lF_SIT_PAGE";


    // 程序自增ID
    public static final String			PAGE_ID	= "PAGE_ID";

    // 微站编号
    public static final String			S_ID	= "S_ID";

    // 唯一标识（访问用）
    public static final String			PAGE_TYPE	= "PAGE_TYPE";

    // 页面的名称
    public static final String			NAME	= "NAME";

    // 访问地址
    public static final String			URL	= "URL";

    // 排序
    public static final String			SEQ_NUM	= "SEQ_NUM";

    // 是否属于系统（1是，0否）
    public static final String			IS_SYSTEM	= "IS_SYSTEM";

    // 企业编码（0表示系统默认分类）
    public static final String			CORP_CODE	= "CORP_CODE";

    // 创建时间
    public static final String			CREATETIME	= "CREATETIME";

    // 更新时间
    public static final String			MODITYTIME	= "MODITYTIME";

	// 序列
	public static final String			SEQUENCE	= "533";

	// 映射集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfSitPage", TABLE_NAME);
		columns.put("tableId", PAGE_ID);
		columns.put("sequence", SEQUENCE);
        columns.put("pageId", PAGE_ID);
        columns.put("sId", S_ID);
        columns.put("pageType", PAGE_TYPE);
        columns.put("name", NAME);
        columns.put("url", URL);
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