/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2016-4-28 上午10:21:30
 */
package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;
/**
 * @功能概要：
 * @项目名称： emp
 * @初创作者： chentingsheng <cts314@163.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-4-28 上午10:21:30
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */

public class TableLfMonHnetwarn
{

	public static final String TABLE_NAME	= "LF_MON_HNETWARN";

	public static final String MONEMAIL = "MONEMAIL";

	public static final String ID = "ID";

	public static final String CREATETIME = "CREATETIME";

	public static final String UPDATETIME = "UPDATETIME";

	public static final String MONPHONE = "MONPHONE";

	public static final String SEQUENCE	= "S_LF_M_HNETWARN";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfMonHnetwarn", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("monemail", MONEMAIL);
		columns.put("id", ID);
		columns.put("createtime", CREATETIME);
		columns.put("updatetime", UPDATETIME);
		columns.put("monphone", MONPHONE);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}

