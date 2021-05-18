package com.montnets.emp.common.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.client.TableLfCustField;
import com.montnets.emp.table.client.TableLfCustFieldValue;

/**
 * @project emp
 * @author wuqw <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2012-1-11
 * @description
 */

public class ViewLfCustFieldValueVo
{
	//实体类字段与数据库字段实体类映射的map集合
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
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
		//属性关系
		columns.put("field_Ref", TableLfCustField.FIELD_REF);
		//属性名
		columns.put("field_Name", TableLfCustField.FIELD_NAME);
		//类型
		columns.put("v_type", TableLfCustField.V_TYPE);
		//企业编码
		columns.put("corp_code", TableLfCustField.CORP_CODE);
		//操作员id
		columns.put("userid", TableLfCustField.USERID);
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
