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
 * @datetime 2011-1-28 上午10:24:18
 * @description
 */

public class TableLfReviewer2level
{

	public static final String TABLE_NAME = "LF_REVIEWER2LEVEL";
	//主键
	public static final String FRL_ID = "FRL_ID";
	//外键(LF_FLOW.Fid)  审批流程表
	public static final String F_ID = "F_ID";
	//外键(lf_sysuser.user_id)  用户表
	public static final String USER_ID = "USER_ID";
	//等级 
	public static final String R_LEVEL = "R_LEVEL";
	//该级审核人是否需要审核提醒)  1 ：是 ，2：否
	public static final String IS_REV_REMIND = "IS_REV_REMIND";
	//审核类型，1-操作员，2-群组，3-上级，4-机构审核员,5-逐级审核
	private static final String  R_TYPE = "R_TYPE";
	//唯一编码，R_TYPE类型决定值类型
	private static final String  R_CODE = "R_CODE";
	//1全部通过生效;2第一人审核生效
	private static final String  R_CONDITION = "R_CONDITION";
	
	
	
	//序列
	public static final String SEQUENCE = "S_LF_REVIEWER2LEVEL";

	//映射集合
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfReviewer2level", TABLE_NAME);
		columns.put("tableId", FRL_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("frlId", FRL_ID);
		columns.put("FId", F_ID);
		columns.put("userId", USER_ID);
		columns.put("RLevel", R_LEVEL);
		columns.put("isRevRemind", IS_REV_REMIND);
		columns.put("rType", R_TYPE);
		columns.put("rCode", R_CODE);
		columns.put("rCondition", R_CONDITION);
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
