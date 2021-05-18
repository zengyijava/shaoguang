/**
 * 
 */
package com.montnets.emp.table.wxsysuser;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-2-10 下午01:40:39
 * @description
 */

public class TableLfSysRoles
{

	//表名:角色表
	public static final String TABLE_NAME = "LF_SYS_ROLES";

	//角色id
	public static final String ROLE_ID = "ROLE_ID";

	//角色名称
	public static final String ROLE_NAME = "ROLE_NAME";

	//内容
	public static final String COMMENTS = "COMMENTS";

	//企业编码
	public static final String CORP_CODE = "CORP_CODE";

	//创建该角色者的guid
	public static final String GUID = "CREATE_GUID";

	//序列
	public static final String SEQUENCE = "S_LF_ROLES";

	//集合
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfSysRoles", TABLE_NAME);
		columns.put("tableId", ROLE_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("roleId", ROLE_ID);
		columns.put("roleName", ROLE_NAME);
		columns.put("comments", COMMENTS);
		columns.put("corpCode", CORP_CODE);
		columns.put("guId", GUID);
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
