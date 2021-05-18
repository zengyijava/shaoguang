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

public class TableLfKeywords
{
	//表名_关键字
	public static final String TABLE_NAME = "LF_KEYWORDS";
	//主键
	public static final String KW_ID = "KW_ID";
	//关键字名称
	public static final String KEY_WOED = "KEYWOED";
	//关键字级别(一级:1，二级:2，三级:3)，暂时没用
	public static final String KW_LEVEL = "KW_LEVEL";
	//状态（1.启用;0.停用）
	public static final String KW_STATE = "KW_STATE";
	//备注
	public static final String COMMENTS = "COMMENTS";
	//企业编码
	public static final String CORP_CODE = "CORP_CODE";
	//序列
	public static final String SEQUENCE = "S_LF_KEYWORDS";

	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfKeywords", TABLE_NAME);
		columns.put("tableId", KW_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("kwId", KW_ID);
		columns.put("keyWord", KEY_WOED);
		columns.put("kwLevel", KW_LEVEL);
		columns.put("kwState", KW_STATE);
		columns.put("comments", COMMENTS);
		columns.put("corpCode", CORP_CODE);
		
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
