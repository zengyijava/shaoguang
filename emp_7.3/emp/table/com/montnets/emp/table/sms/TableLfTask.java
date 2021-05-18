package com.montnets.emp.table.sms;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:25
 * @description
 */
public class TableLfTask
{

	//表名：任务的基本信息
	public static final String TABLE_NAME = "LF_TASK";

    //自增ID
	public static final String TAID = "TAID";

	//任务ID
	public static final String TASK_ID = "TASKID";

	//服务任务ID
	public static final String SL_ID = "SL_ID";

	//发送任务ID
	public static final String TASK_TYPE = "TASKTYPE";

	//业务类型（服务0，手工1，接入2,IM 3）
	public static final String MT_ID = "MT_ID";

	//SP账号
	public static final String SPUSER = "SPUSER";

	//操作员登录ID(指提交任务的操作员帐号)
	public static final String USER_NAME = "USERNAME";

	//操作员所属机构ID（指提交任务的操作员所在部门）
	public static final String DEP_ID = "DEP_ID";

	//操作员ID
	public static final String USER_ID = "USER_ID";
	
	//提交时间
	public static final String SUBMIT_TIME = "SUBMIT_TIME";
	
	//企业编码
	public static final String CORP_CODE = "CORP_CODE";
	
	//序列
	public static final String SEQUENCE = "S_LF_TASK";

    //映射集合
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfTask", TABLE_NAME);
		columns.put("tableId", TAID);
		columns.put("sequence", SEQUENCE);
		columns.put("taId", TAID);
		columns.put("taskId", TASK_ID);
		columns.put("slId", SL_ID);
		columns.put("taskType", TASK_TYPE);
		columns.put("mtId", MT_ID);
		columns.put("spUser", SPUSER);
		columns.put("userName", USER_NAME);
		columns.put("depId", DEP_ID);
		columns.put("userId", USER_ID);
		columns.put("submitTime", SUBMIT_TIME);
		columns.put("corpCode", CORP_CODE);

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
