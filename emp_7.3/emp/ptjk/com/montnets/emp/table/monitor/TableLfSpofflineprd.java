
package com.montnets.emp.table.monitor;



import java.util.HashMap;

import java.util.Map;
/**
 * 
 * @功能概要：SP离线时间段告警阀值管理表Table类
 * @项目名称： emp_std_201508
 * @初创作者： tanglili <jack860127@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-1-23 下午02:10:12
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class TableLfSpofflineprd
{

	public static final String TABLE_NAME	= "LF_SPOFFLINEPRD";
	//结束时间段(小时)
	public static final String END_HOUR = "END_HOUR";
	//最后更新的操作员ID
	public static final String USER_ID = "USER_ID";
	//创建时间
	public static final String CREATE_TIME = "CREATE_TIME";
	//自增ID
	public static final String ID = "ID";
	//更新时间
	public static final String UPDATE_TIME = "UPDATE_TIME";
	//企业编码
	public static final String CORP_CODE = "CORP_CODE";
	//时长
	public static final String DURATION = "DURATION";
	//开始时间段(小时)
	public static final String BEGIN_HOUR = "BEGIN_HOUR";
	//SP账号
	public static final String SPACCOUNTID = "SPACCOUNTID";
    //序列
	public static final String SEQUENCE	= "LF_SPOFFLINEPRD_S";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfSpofflineprd", TABLE_NAME);
		columns.put("tableId",ID);
		columns.put("sequence", SEQUENCE);
		columns.put("endhour", END_HOUR);
		columns.put("userid", USER_ID);
		columns.put("createtime", CREATE_TIME);
		columns.put("id", ID);
		columns.put("updatetime", UPDATE_TIME);
		columns.put("corpcode", CORP_CODE);
		columns.put("duration", DURATION);
		columns.put("beginhour", BEGIN_HOUR);
		columns.put("spaccountid", SPACCOUNTID);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
