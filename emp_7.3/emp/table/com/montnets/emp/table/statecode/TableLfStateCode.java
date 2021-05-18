/**
 * 
 */
package com.montnets.emp.table.statecode;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author pengj
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2016-1-15 上午09:05:00
 * @description
 */

public class TableLfStateCode
{
	//表名(业务管理表)
	public static final String TABLE_NAME = "LF_STATECODE";
	//编号ID
	public static final String STATE_ID = "STATE_ID";
	//状态码
	public static final String STATE_CODE = "STATE_CODE";
	//映射码
	public static final String MAPPING_CODE = "MAPPING_CODE";
	//状态码描述
	public static final String STATE_DES = "STATE_DES";
	//企业编码
	public static final String CORP_CODE="CORP_CODE";
	//创建时间
	public static final String CREATE_TIME="CREATE_TIME";
	//更新时间
	public static final String UPDATE_TIME="UPDATE_TIME";
	//最后更新的操作员ID
	public static final String USER_ID="USER_ID";
	//序列名
	public static final String SEQUENCE = "LF_STATECODE_S";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfStateCode", TABLE_NAME);
		
		columns.put("tableId", STATE_ID);
		
		columns.put("sequence", SEQUENCE);
		
		columns.put("stateId", STATE_ID);
		
		columns.put("stateCode", STATE_CODE);

		columns.put("mappingCode", MAPPING_CODE);
		
		columns.put("stateDes", STATE_DES);
		
		columns.put("corpCode",CORP_CODE);
		
		columns.put("createTime",CREATE_TIME );
		
		columns.put("updateTime",UPDATE_TIME );
		
		columns.put("userId",USER_ID );
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
