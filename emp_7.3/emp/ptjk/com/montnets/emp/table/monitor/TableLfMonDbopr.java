/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2016-4-25 下午02:49:13
 */
package com.montnets.emp.table.monitor;

import java.sql.Timestamp;

import java.util.HashMap;

import java.util.Map;
/**
 * @功能概要：
 * @项目名称： emp
 * @初创作者： chentingsheng <cts314@163.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-4-25 下午02:49:13
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */

public class TableLfMonDbopr
{

	public static final String TABLE_NAME	= "LF_MON_DBOPR";

	public static final String PROCENODE = "PROCENODE";

	public static final String ID = "ID";

	public static final String CREATETIME = "CREATETIME";

	public static final String SEQUENCE	= "S_LF_MON_DBOPR";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfMonDbopr", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("procenode", PROCENODE);
		columns.put("id", ID);
		columns.put("createtime", CREATETIME);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}

