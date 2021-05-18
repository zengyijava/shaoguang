package com.montnets.emp.table.system;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
/**
 *   提交密码接收人VO
 * @author Administrator
 *
 */
public class TableLfdepPassUser {
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("userId",TableLfSysuser.USER_ID);
		columns.put("name", TableLfSysuser.NAME);
		columns.put("mobile",TableLfSysuser.MOBILE);
		columns.put("worknumber",TableLfSysuser.WORKNUMBER);
		columns.put("depId", TableLfDep.DEP_ID);
		columns.put("depName",TableLfDep.DEP_NAME);
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
