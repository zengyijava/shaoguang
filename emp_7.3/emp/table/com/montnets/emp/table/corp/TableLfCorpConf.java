package com.montnets.emp.table.corp;

import java.util.HashMap;
import java.util.Map;

/**
 * @project sinolife
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-16 下午03:18:12
 * @description 
 */
public class TableLfCorpConf
{
	//表名  (企业信息表)
	public static final String  TABLE_NAME = "LF_CORP_CONF";
	
	public static final String  CC_ID = "CC_ID";
	//企业编码
	public static final String  CORP_CODE = "CORP_CODE";
	
	public static final String  PARAM_KEY = "PARAM_KEY";
	
	public static final String  PARAM_VALUE = "PARAM_VALUE";
	//序列名
    public static final String  SEQUENCE="LF_CORP_CONF_S";


    protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfCorpConf", TABLE_NAME);
		columns.put("tableId", CC_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("ccId", CC_ID);
		columns.put("corpCode", CORP_CODE);
		columns.put("paramKey", PARAM_KEY);
		columns.put("paramValue", PARAM_VALUE);

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
