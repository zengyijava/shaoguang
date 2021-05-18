/**
 * 
 */
package com.montnets.emp.table.biztype;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-20 下午03:13:32
 * @description
 */

public class TableLfBusManager
{
	//表名(业务管理表)
	public static final String TABLE_NAME = "LF_BUSMANAGER";
	//业务ID
	public static final String BUS_ID = "BUS_ID";
	//业务编码(B+业务类型+"-"+子模块类型)
	public static final String BUS_CODE = "BUS_CODE";
	//业务名称
	public static final String BUS_NAME = "BUS_NAME";
	//业务描述
	public static final String BUS_DESCRIPTION = "BUS_DESCRIPTION";
	//类名
	public static final String CLASS_NAME = "CLASS_NAME";
	//企业编码
	public static final String CORP_CODE="CORP_CODE";
	
	public static final String USER_ID="USER_ID";
	
	public static final String BUS_TYPE="BUS_TYPE";
	
	public static final String RISELEVEL="RISELEVEL";
	
	public static final String STATE="STATE";
	
	public static final String CREATE_TIME="CREATE_TIME";
	
	public static final String UPDATE_TIME="UPDATE_TIME";
	
	public static final String DEP_ID="DEP_ID";
	
	//序列名
	public static final String SEQUENCE = "S_LF_BUSMANAGER";
	
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfBusManager", TABLE_NAME);
		
		columns.put("tableId", BUS_ID);
		
		columns.put("sequence", SEQUENCE);
		
		columns.put("busId", BUS_ID);
		
		columns.put("busCode", BUS_CODE);
		
		columns.put("busName", BUS_NAME);
		
		columns.put("busDescription", BUS_DESCRIPTION);
		
		columns.put("className", CLASS_NAME);
		
		columns.put("corpCode",CORP_CODE);
		
		columns.put("userId", USER_ID);
		
		columns.put("state", STATE);
		
		columns.put("busType",BUS_TYPE );
		
		columns.put("riseLevel",RISELEVEL );
		
		columns.put("createTime",CREATE_TIME );
		
		columns.put("updateTime",UPDATE_TIME );
		
		columns.put("depId", DEP_ID);
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
