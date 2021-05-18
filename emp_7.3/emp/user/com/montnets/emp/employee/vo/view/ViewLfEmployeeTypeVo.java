package com.montnets.emp.employee.vo.view;

import com.montnets.emp.table.employee.TableLfEmployeeType;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-7 下午04:47:49
 * @description
 */

public class ViewLfEmployeeTypeVo
{
	
	protected static final Map<String,String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("id", TableLfEmployeeType.ID);
		columns.put("user_id", TableLfEmployeeType.USER_ID);
		columns.put("name", TableLfEmployeeType.NAME);
		columns.put("corpCode", TableLfEmployeeType.CORP_CODE);
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
