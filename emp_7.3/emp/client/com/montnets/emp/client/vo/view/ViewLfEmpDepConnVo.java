package com.montnets.emp.client.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.employee.TableLfEmpDepConn;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-7 下午04:47:49
 * @description
 */

public class ViewLfEmpDepConnVo
{
	
	protected static final Map<String,String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("user_id", TableLfEmpDepConn.USER_ID);
		//columns.put("dep_code_third", TableLfEmpDepConn.DEP_CODE_THIRD);
		columns.put("dep_id", TableLfEmpDepConn.DEP_ID);
		columns.put("conn_id", TableLfEmpDepConn.CONN_ID);
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
