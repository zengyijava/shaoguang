/**
 * 
 */
package com.montnets.emp.table.sysuser;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-30 上午11:22:27
 * @description
 */

@SuppressWarnings("serial")
public class TableLfBalancePri implements java.io.Serializable
{
	//表名  (充值回收权限表)
	public static final String TABLE_NAME = "LF_BALANCE_PRI";
	//自增ID
	public static final String ID = "ID";
	//操作员ID
	public static final String USER_ID = "USER_ID";
	//机构ID
	public static final String DEP_ID = "DEP_ID";
	//充值权限类型
	public static final String TYPE = "TYPE";
	//创建记录的操作员ID
	public static final String CREATE_USERID = "CREATE_USERID";
	//企业编码
	public static final String CORP_CODE = "CORP_CODE";
	//创建时间
	public static final String CREATE_TIME = "CREATE_TIME";
	//序列名
	public static final String SEQUENCE="S_LF_BL_PRI";
	
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfBalancePri", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("userId", USER_ID);
		columns.put("depId", DEP_ID);
		columns.put("type", TYPE);
		columns.put("createUserId", CREATE_USERID);
		columns.put("corpCode", CORP_CODE);
		columns.put("createTime", CREATE_TIME);
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
