package com.montnets.emp.sysuser.vo.view;

import com.montnets.emp.table.sysuser.TableLfRoles;
import com.montnets.emp.table.sysuser.TableLfSysuser;

import java.util.LinkedHashMap;
import java.util.Map;



public class ViewLfRolesVo {
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("roleId", TableLfRoles.ROLE_ID);
		columns.put("roleName", TableLfRoles.ROLE_NAME);
		columns.put("comments", TableLfRoles.COMMENTS);
		columns.put("guId", TableLfRoles.GUID);
		columns.put("userName", TableLfSysuser.USER_NAME);	
	}

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
