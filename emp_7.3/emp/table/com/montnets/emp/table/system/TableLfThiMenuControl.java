/**
 * 
 */
package com.montnets.emp.table.system;

import java.util.HashMap;
import java.util.Map;

/**
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-9-7 下午07:01:11
 * @description 
 */

public class TableLfThiMenuControl
{
	//表名：三级菜单控制表
	public static final String TABLE_NAME = "LF_THIR_MENUCONTROL";

	//标识列
	public static final String TID = "TID";	 
    
	//菜单顺序
    public static final String MENU_NUM = "MENU_NUM";	
    
    // 菜单标题
    public static final String TITLE = "TITLE";	
    
    //权限菜单
    public static final String PRI_MENU = "PRI_MENU";
    
    //权限菜单顺序
    public static final String PRI_ORDER = "PRI_ORDER";	
     
    //序列
	public static final String SEQUENCE = "S_LF_THIR_MENUCONTROL";

	// 繁体菜单标题
	public static final String ZH_TW_TITLE = "ZH_TW_TITLE";

	// 英文菜单标题
	public static final String ZH_HK_TITLE = "ZH_HK_TITLE";
	
	//映射集合
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfThiMenuControl", TABLE_NAME);
		columns.put("tableId",TID  );
		columns.put("sequence", SEQUENCE);
	    columns.put("tid",TID);
	    columns.put("menuNum",MENU_NUM);
	    columns.put("title",TITLE);
	    columns.put("priMenu",PRI_MENU);
	    columns.put("priOrder",PRI_ORDER);
	    columns.put("zhTwTitle",ZH_TW_TITLE);
	    columns.put("zhHkTitle",ZH_HK_TITLE);
	   
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
