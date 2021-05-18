package com.montnets.emp.rms.degree.table;

import java.util.LinkedHashMap;
import java.util.Map;

public class TableLfDegree {
	//表名
	public static final String TABLE_NAME = "LF_DEGREE";

	//主键
	public static final String ID = "ID";

	//档位 1-5档
	public static final String DEGREE = "DEGREE";

	//档位容量开始
	public static final String DEGREE_BEGIN ="DEGREE_BEGIN";

	//档位容量结束
	public static final String DEGREE_END = "DEGREE_END";

	//有效开始时间
	public static final String VALID_DATE_BEGIN = "VALID_DATE_BEGIN";

	//有效截止时间
	public static final String VALID_DATE_END = "VALID_DATE_END";

	//状态，0-启用，1-禁用，2-过期，默认为0
	public static final String STATUS = "STATUS";

	//操作员ID
	public static final String USER_ID = "USER_ID";
	
	//记录创建时间
	public static final String CREATE_TIME = "CREATE_TIME";

	/**
	 * 序列名字 用于ORacle主键生成
	 */
	private static final String SEQUENCE_NAME = "S_LF_DEGREE";

	protected final static Map<String, String> columns = new LinkedHashMap<String, String>();
	
	static {
		// 一定要有的
		columns.put("LfDegree", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("id", ID);
		columns.put("degree", DEGREE);
		columns.put("degreeBegin", DEGREE_BEGIN);
		columns.put("degreeEnd", DEGREE_END);
		columns.put("validDateBegin", VALID_DATE_BEGIN);
		columns.put("validDateEnd", VALID_DATE_END);
		columns.put("status", STATUS);
		columns.put("userId", USER_ID);
		columns.put("createTime", CREATE_TIME);
		columns.put("sequence", SEQUENCE_NAME);
	}
	
	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM() {
		return columns;
	}
	
}
