package com.montnets.emp.common.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.group.TableLfUdgroup;

/**
 * 
 * @project emp
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-4 下午02:58:48
 * @description
 */
public class ViewGroupInfoVo
{
	//实体类字段与数据库字段实体类映射的map集合
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		//群组id
		columns.put("udgId", TableLfList2gro.UDG_ID);
		//群组类型
		columns.put("l2gType", TableLfList2gro.L2G_TYPE);
		//手机号码
		columns.put("mobile", TableLfEmployee.MOBILE);
		//姓名
		columns.put("name", TableLfEmployee.NAME);
		//id
		columns.put("l2gId", TableLfList2gro.L2G_ID);
		//群组名称
		columns.put("udgName", TableLfUdgroup.UDG_NAME);
		//guid
		columns.put("guId",TableLfList2gro.GUID);
		//操作员id
		columns.put("userId", TableLfUdgroup.USER_ID);
		columns.put("gpAttribute", TableLfUdgroup.GP_ATTRIBUTE);
		//群组类型
		columns.put("groupType", TableLfUdgroup.GROUP_TYPE);
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
