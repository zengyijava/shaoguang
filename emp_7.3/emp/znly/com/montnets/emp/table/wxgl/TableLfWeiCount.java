package com.montnets.emp.table.wxgl;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfWeiCount
 *
 * @project p_wei
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWeiCount
{
	// 微信用户统计
	public static final String				TABLE_NAME	= "LF_WEI_COUNT";

    // 编号
    public static final String			COUNT_ID	= "COUNT_ID";

    // 新关注人数
    public static final String          FOLLOW_COUNT    = "FOLLOW_COUNT";
    
    // 取消关注人数
    public static final String			UNFOLLOW_COUNT	= "UNFOLLOW_COUNT";
    
    // 净关注人数
    public static final String			INCOME_COUNT	= "INCOME_COUNT";

    // 累计关注人数
    public static final String			AMOUNT_COUNT	= "AMOUNT_COUNT";

    // 当前统计日期
    public static final String			DAYTIME	= "DAYTIME";

    // 公众帐号ID
    public static final String			A_ID	= "A_ID";

    // 企业编号
    public static final String			CORP_CODE	= "CORP_CODE";

    // 修改时间
    public static final String			MODIFYTIME	= "MODIFYTIME";

	// 序列
	public static final String			SEQUENCE	= "S_LF_WEI_COUNT";

	// 映射集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfWeiCount", TABLE_NAME);
		columns.put("tableId", COUNT_ID);
		columns.put("sequence", SEQUENCE);
        columns.put("countId", COUNT_ID);
        columns.put("unfollowCount", UNFOLLOW_COUNT);
        columns.put("followCount", FOLLOW_COUNT);
        columns.put("incomeCount", INCOME_COUNT);
        columns.put("amountCount", AMOUNT_COUNT);
        columns.put("dayTime", DAYTIME);
        columns.put("aId", A_ID);
        columns.put("corpCode", CORP_CODE);
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