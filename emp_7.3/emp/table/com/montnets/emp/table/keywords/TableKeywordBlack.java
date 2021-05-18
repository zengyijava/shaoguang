/**
 * 
 */
package com.montnets.emp.table.keywords;

import java.util.HashMap;
import java.util.Map;

/**
 * 关键字
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 上午10:53:46
 * @description
 */

public class TableKeywordBlack
{
	//表名_关键字
	public static final String TABLE_NAME = "KEYWORD_BLACK";
	//主键
	public static final String ID = "ID";
	//关键字名称
	public static final String KEY_WORD = "KEYWORD";
	public static final String KEY_TYPE = "KEYTYPE";
	//关键字级别(一级:1，二级:2，三级:3)，暂时没用
	public static final String KEY_LEVEL = "KEYLEVEL";
	public static final String OPT_TYPE = "OPTTYPE";
	public static final String CREATE_TIME = "CREATETIME";
	//备注
	public static final String COMMENTS = "COMMENTS";
	//序列
	public static final String SEQUENCE = "S_KEYWORD_BLACK";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("KeywordBlack", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("keyWord", KEY_WORD);
		columns.put("keyType", KEY_TYPE);
		columns.put("keyLevel", KEY_LEVEL);
		columns.put("opType", OPT_TYPE);
		//columns.put("createTime", CREATE_TIME);
		//columns.put("comments", COMMENTS);
		
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
