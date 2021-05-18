package com.montnets.emp.table.site;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfSitType
 *
 * @project p_sit
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfSitType
{
	// 表名
	public static final String				TABLE_NAME	= "LF_SIT_TYPE";


    // 程序自增ID
    public static final String			TYPE_ID	= "TYPE_ID";

    // 名称
    public static final String			NAME	= "NAME";

    // 是否默认选择
    public static final String			IS_DEFAULT	= "IS_DEFAULT";

    // 模式（0标准，1高级）
    public static final String			PATTERN	= "PATTERN";

    // 封面地址
    public static final String			IMG_URL	= "IMG_URL";

    // 图片预览0
    public static final String			IMG_URL0	= "IMG_URL0";

    // 图片预览1
    public static final String			IMG_URL1	= "IMG_URL1";

    // 图片预览2
    public static final String			IMG_URL2	= "IMG_URL2";

    // 图片预览3
    public static final String			IMG_URL3	= "IMG_URL3";

    // 是否属于系统（1是，0否）
    public static final String			IS_SYSTEM	= "IS_SYSTEM";

    // 排序
    public static final String			SEQ_NUM	= "SEQ_NUM";

    // 企业编码（0表示系统默认分类）
    public static final String			CORP_CODE	= "CORP_CODE";

    // 创建时间
    public static final String			CREATETIME	= "CREATETIME";

    // 更新时间
    public static final String			MODITYTIME	= "MODITYTIME";

	// 序列
	public static final String			SEQUENCE	= "S_LF_SIT_TYPE";

	// 映射集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfSitType", TABLE_NAME);
		columns.put("tableId", TYPE_ID);
		columns.put("sequence", SEQUENCE);
        columns.put("typeId", TYPE_ID);
        columns.put("name", NAME);
        columns.put("isDefault", IS_DEFAULT);
        columns.put("pattern", PATTERN);
        columns.put("imgUrl", IMG_URL);
        columns.put("imgUrl0", IMG_URL0);
        columns.put("imgUrl1", IMG_URL1);
        columns.put("imgUrl2", IMG_URL2);
        columns.put("imgUrl3", IMG_URL3);
        columns.put("isSystem", IS_SYSTEM);
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