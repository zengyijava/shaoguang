package com.montnets.emp.tczl.entity.table;

import java.util.HashMap;
import java.util.Map;

public class TableLfTaocanCmd
{
	//表名(套餐指令关系表)
	public static final String TABLE_NAME = "LF_TAOCAN_CMD";
	//自增ID
	public static final String ID = "ID";
	//指令代码
	public static final String STRUCTCODE = "STRUCTCODE";
	//套餐编号
	public static final String TAOCAN_CODE = "TAOCAN_CODE";
	//套餐类型 （1：VIP免费；2：包月；3：包季；4：包年；）
	public static final String TAOCAN_TYPE = "TAOCAN_TYPE";
	//套餐金额
	public static final String TAOCAN_MONEY = "TAOCAN_MONEY";
	//指令类型  0：订购；1：退订；2：全局查看；3：全局退订，4：签约；5：取消签约
	public static final String STRUCT_TYPE="STRUCT_TYPE";
	//创建时间
	public static final String CREATE_TIME="CREATE_TIME";
	//更新时间
	public static final String UPDATE_TIME="UPDATE_TIME";
	//操作员机构ID
	public static final String DEP_ID="DEP_ID";
	//操作员ID
	public static final String USER_ID="USER_ID";
	//企业编码
	public static final String CORP_CODE="CORP_CODE";
	//序列名
	public static final String SEQUENCE = "S_LF_BUSMANAGER";
	
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfTaocanCmd", TABLE_NAME);
		
		columns.put("tableId", ID);
		
		columns.put("sequence", SEQUENCE);
		
		columns.put("id", ID);
		
		columns.put("structcode", STRUCTCODE);
		
		columns.put("taocanCode", TAOCAN_CODE);
		
		columns.put("taocanType", TAOCAN_TYPE);
		
		columns.put("taocanMoney", TAOCAN_MONEY);
		
		columns.put("structType", STRUCT_TYPE);
		
		columns.put("createTime", CREATE_TIME);
		
		columns.put("updateTime", UPDATE_TIME);
		
		columns.put("depId", DEP_ID);
		
		columns.put("userId", USER_ID);
		
		columns.put("corpCode",CORP_CODE);

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
