package com.montnets.emp.table.wxsysuser;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-21 下午01:35:36
 * @description
 */
public class TableLfSysPrivilege
{
	//菜单表
	public static final String TABLE_NAME = "LF_SYS_PRIVILEGE";

	//主键id
	public static final String PRIVILEGE_ID = "PRIVILEGE_ID";

	//资源id对应lf_third_control
	public static final String RESOURCE_ID = "RESOURCE_ID";

	//操作类型
	public static final String OPERATE_ID = "OPERATE_ID";
	//功能名称
	public static final String COMMENTS = "COMMENTS";
	//每个模块功能对应的权限code
	public static final String PRIV_CODE = "PRIV_CODE";
	//菜单名
	public static final String MENU_NAME = "MENUNAME";
	//模块名
	public static final String MOD_NAME = "MODNAME";
	//菜单编码
	public static final String MENU_CODE = "MENUCODE";
	//菜单跳转链接
	public static final String MENU_SITE = "MENUSITE";
 	//序列
	public static final String SEQUENCE = "S_LF_PRIVILEGE";

	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfSysPrivilege", TABLE_NAME);
		columns.put("tableId", PRIVILEGE_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("privilegeId", PRIVILEGE_ID);
		columns.put("resourceId", RESOURCE_ID);
		columns.put("operateId", OPERATE_ID);
		columns.put("comments", COMMENTS);
		columns.put("privCode", PRIV_CODE);
		columns.put("menuName", MENU_NAME);
		columns.put("modName", MOD_NAME);
		columns.put("menuCode", MENU_CODE);
		columns.put("menuSite", MENU_SITE);
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
