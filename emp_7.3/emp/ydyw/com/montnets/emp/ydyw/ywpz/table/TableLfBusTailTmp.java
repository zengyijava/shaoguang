package com.montnets.emp.ydyw.ywpz.table;

import java.util.LinkedHashMap;
import java.util.Map;

public class TableLfBusTailTmp {
	
	//表名
	public static final String TABLE_NAME = "LF_BUS_TAILTMP";

	//主键ID
	public static final String ID = "ID";
	
	//业务ID
	public static final String BUS_ID = "BUS_ID";
	
	//短信模板ID
	public static final String TM_ID = "TM_ID";
	
	//关联类型：1、短信模板对业务    	0、业务对贴尾
	public static final String ASSOCIATE_TYPE = "ASSOCIATE_TYPE";
	
	//企业编码
	public static final String CORP_CODE = "CORP_CODE";
	
	//创建时间
	public static final String CREATE_TIME = "CREATE_TIME";
	
	//修改时间
	public static final String UPDATE_TIME = "UPDATE_TIME";
	
	//机构id
	public static final String DEP_ID = "DEP_ID";
	
	//创建人id
	public static final String USER_ID = "USER_ID";
	
	public static final String sequence = "";
	
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
	
	static {
		columns.put("LfBusTailTmp", TABLE_NAME);
		columns.put("id", ID);
		columns.put("tableId", ID);
		columns.put("sequence", sequence);
		columns.put("busId", BUS_ID);
		columns.put("tmId", TM_ID);
		columns.put("associateType", ASSOCIATE_TYPE);
		columns.put("corpCode", CORP_CODE);
		columns.put("createTime", CREATE_TIME);
		columns.put("updateTime", UPDATE_TIME);
		columns.put("depId", DEP_ID);
		columns.put("userId", USER_ID);
	}
	
	public static Map<String, String> getORM() {
		return columns;
	}
	
}
