package com.montnets.emp.table.segnumber;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:30:48
 * @description
 */
public class TablePbServicetype {

	public static final String TABLE_NAME = "PB_SERVICETYPE";
	public static final String SPISUNCM = "SPISUNCM";
	public static final String SERVICE_NO = "SERVICENO";
	public static final String SERVICE_INFO = "SERVICEINFO";

	protected final static Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("PbServicetype", TABLE_NAME);
		columns.put("spisuncm", SPISUNCM);
		columns.put("serviceno", SERVICE_NO);
		columns.put("serviceinfo",SERVICE_INFO);
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM() {
		return columns;
	}

}
