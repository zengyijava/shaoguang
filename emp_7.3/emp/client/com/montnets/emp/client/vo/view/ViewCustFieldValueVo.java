package com.montnets.emp.client.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;
import com.montnets.emp.table.client.TableLfCustFieldValue;

/**
 * @project emp
 * @author wuqw <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2012-1-11
 * @description
 */

public class ViewCustFieldValueVo
{
	//实体类字段与数据库字段实体类映射的map集合
	protected  static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		//id
		columns.put("id", TableLfCustFieldValue.ID);
		//属性id
		columns.put("field_ID", TableLfCustFieldValue.FIELD_ID);
		//属性值
		columns.put("field_Value", TableLfCustFieldValue.FIELD_VALUE);
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
