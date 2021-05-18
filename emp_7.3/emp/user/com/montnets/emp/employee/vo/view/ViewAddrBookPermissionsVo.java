package com.montnets.emp.employee.vo.view;

import com.montnets.emp.table.client.TableLfCliDepConn;
import com.montnets.emp.table.client.TableLfClientDep;
import com.montnets.emp.table.employee.TableLfEmpDepConn;
import com.montnets.emp.table.sysuser.TableLfSysuser;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @project sinolife
 * @author liujianjun <646654831@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-6-9 上午09:49:29
 * @description 
 */

public class ViewAddrBookPermissionsVo
{
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		//columns.put("depCode", TableLfEmpDepConn.DEP_CODE_THIRD);
		//机构名称
		columns.put("depName", TableLfClientDep.DEP_NAME);
		columns.put("connId",TableLfCliDepConn.CONN_ID);
		//机构id
		columns.put("depId",TableLfCliDepConn.DEP_ID);
		//操作员id
		columns.put("userId", TableLfEmpDepConn.USER_ID);
		//操作员名称
		columns.put("userName", TableLfSysuser.USER_NAME);
		//操作员姓名
		columns.put("name", TableLfSysuser.NAME);
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
