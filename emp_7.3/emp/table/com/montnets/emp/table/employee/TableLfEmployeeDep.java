/**
 * 
 */
package com.montnets.emp.table.employee;

import java.util.HashMap;
import java.util.Map;

/**
 * @project sinolife
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-16 下午04:02:31
 * @description
 */

public class TableLfEmployeeDep
{
	//表名_员工机构表
	public static final String TABLE_NAME = "LF_EMPLOYEE_DEP";
	//机构ID
	public static final String DEP_ID = "DEP_ID";
	//机构编码
	//public static final String DEP_CODE = "DEP_CODE";
	////机构名称
	public static final String DEP_NAME = "DEP_NAME";

	//public static final String DEP_PCODE = "DEP_PCODE";

	public static final String DEP_EFF_STATUS = "DEP_EFF_STATUS";
	//机构级别
	public static final String DEP_LEVEL = "DEP_LEVEL";
	
	public static final String ADD_TYPE = "ADD_TYPE";
	//企业编码
	public static final String CORP_CODE="CORP_CODE";
	//父级ID
	public static final String PARENT_ID = "PARENT_ID";
	
	public static final String DEP_CODE_THIRD = "DEP_CODE_THIRD";
	//序列名
	public static final String SEQUENCE = "S_LF_EMPLOYEE_DEP";
	//机构级别1/2/3/...
	public static final String DEP_PATH="DEP_PATH";
	//up_depId VARCHAR (64) COLLATE 统一平台机构标识ID
	public static final String UP_DEPID = "UP_DEPID";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfEmployeeDep", TABLE_NAME);
		columns.put("tableId", DEP_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("depId", DEP_ID);
		//columns.put("depCode", DEP_CODE);
		columns.put("depName", DEP_NAME);
		//columns.put("depPcode", DEP_PCODE);
		columns.put("depEffStatus", DEP_EFF_STATUS);
		columns.put("depLevel", DEP_LEVEL);
		columns.put("addType",ADD_TYPE);
		columns.put("corpCode",CORP_CODE);
		columns.put("parentId", PARENT_ID);
		columns.put("depcodethird", DEP_CODE_THIRD);
		columns.put("deppath", DEP_PATH);
		columns.put("updepid", UP_DEPID);
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
