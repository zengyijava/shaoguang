package com.montnets.emp.table.wxgl;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfWeiGroup 微信用户分组
 *
 * @project p_wei
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWeiGroup
{
	// 表名
	public static final String				TABLE_NAME	= "LF_WEI_GROUP";


    // 主键
    public static final String			G_ID	= "G_ID";

    //微信同步过来的群组ID
    public static final String          WG_ID   = "WG_ID";
    
    // 分组名称
    public static final String			NAME	= "NAME";

    // 分组内用户数量
    public static final String			COUNT	= "COUNT";

    // 公众帐号的ID
    public static final String			A_ID	= "A_ID";

    // 企业编号
    public static final String			CORP_CODE	= "CORP_CODE";

    // 创建时间
    public static final String			CREATETIME	= "CREATETIME";

    // 修改时间
    public static final String			MODIFYTIME	= "MODIFYTIME";

	// 序列
	public static final String			SEQUENCE	= "521";

	// 映射集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfWeiGroup", TABLE_NAME);
		columns.put("tableId", G_ID);
		columns.put("sequence", SEQUENCE);
        columns.put("GId", G_ID);
        columns.put("WGId", WG_ID);
        columns.put("name", NAME);
        columns.put("count", COUNT);
        columns.put("AId", A_ID);
        columns.put("corpCode", CORP_CODE);
        columns.put("createtime", CREATETIME);
        columns.put("modifytime", MODIFYTIME);
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