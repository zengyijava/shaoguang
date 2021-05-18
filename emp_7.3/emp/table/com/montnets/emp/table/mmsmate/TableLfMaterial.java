/**
 * 
 */
package com.montnets.emp.table.mmsmate;

import java.util.HashMap;
import java.util.Map;

/**
 * 彩信材料映射类
 * 
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-15 下午02:32:02
 * @description 
 */

public class TableLfMaterial
{
	//表名_彩信材料
	public static final String TABLE_NAME = "LF_MATERIAL";
	//素材ID（自增）
	public static final String MTAL_ID = "MTAL_ID";
	//素材名称
	public static final String MTAL_NAME = "MTAL_NAME";
	//素材类型（比如jpg，gif等）
	public static final String MTAL_TYPE = "MTAL_TYPE";
	//素材大小（单位kb)
	public static final String MTAL_SIZE = "MTAL_SIZE";
	//素材添加者
	public static final String USER_ID = "USER_ID";
	//素材添加时间
	public static final String MTAL_UPTIME = "MTAL_UPTIME";
	//备注
	public static final String COMMENTS = "COMMENTS";
	//素材分类ID
	public static final String SORT_ID = "SORT_ID";
	//素材存放地址
	public static final String MTAL_ADDRESS = "MTAL_ADDRESS";
	//素材图片宽度
	public static final String MTAL_WIDTH = "MTAL_WIDTH";
	//素材图片高度
	public static final String MTAL_HEIGHT = "MTAL_HEIGHT";
	//序列
	public static final String SEQUENCE = "S_LF_MATERIAL";
	//企业编码
	public static final String CORP_CODE = "CORP_CODE";

	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfMaterial", TABLE_NAME);
		columns.put("tableId", MTAL_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("mtalId",MTAL_ID );
		columns.put("mtalName",MTAL_NAME );
		columns.put("mtalType",MTAL_TYPE );
		columns.put("mtalSize",MTAL_SIZE );
		columns.put("userId",USER_ID );
		columns.put("mtalUptime",MTAL_UPTIME);
		columns.put("comments",COMMENTS );
		columns.put("sortId",SORT_ID );
		columns.put("mtalAddress", MTAL_ADDRESS);
		columns.put("mtalWidth", MTAL_WIDTH);
		columns.put("mtalHeight", MTAL_HEIGHT);
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
