/**
 * 
 */
package com.montnets.emp.table.mmsmate;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 彩信素材分类映射类
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-15 下午02:44:20
 * @description 
 */

public class TableLfMaterialSort
{
	//表名_彩信素材分类
	public static final String TABLE_NAME = "LF_MATERIAL_SORT";
	//素材分类ID（自增） 
	public static final String SORT_ID = "SORT_ID";
	//分类名称
	public static final String SORT_NAME = "SORT_NAME";
	//上一级素材分类编码
	public static final String PARENT_CODE = "PARENT_CODE";
	//素材分类编码
	public static final String CHILD_CODE = "CHILD_CODE";
	//分类级别
	public static final String SORT_LEVEL = "SORT_LEVEL";
	//企业编码
	public static final String CORP_CODE = "CORP_CODE";
	//序列
 	public static final String SEQUENCE = "S_LF_MATERIAL_SORT";

	protected static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfMaterialSort", TABLE_NAME);
		columns.put("tableId", SORT_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("sortId",SORT_ID );
		columns.put("sortName", SORT_NAME);
		columns.put("parentCode",PARENT_CODE );
		columns.put("childCode",CHILD_CODE);
		columns.put("sortLevel",SORT_LEVEL);
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
