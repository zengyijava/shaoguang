/**
 * 
 */
package com.montnets.emp.table.system;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-29 下午06:46:26
 * @description
 */

public class TableLfTimerHistory
{
	//表名： 定时任务历史记录 
	public static final String TABLE_NAME = "LF_TIMER_HISTORY";

	//标识ID
	public static final String TA_ID = "TA_ID";

	//任务ID
	public static final String TIMER_TASK_ID = "TIMER_TASK_ID";

	//运行时间
	public static final String RUN_TIME = "RUN_TIME";

	//运行时间（年）
	public static final String RUN_YEAR = "RUN_YEAR";

	//运行时间（月）
	public static final String RUN_MONTH = "RUN_MONTH";

	//运行时间（天）
	public static final String RUN_DAY = "RUN_DAY";

	//运行结果
	public static final String RUN_RESULT = "RUN_RESULT";

	//任务表达式
	public static final String TASK_EXPRESSION = "TASK_EXPRESSION";

	//错误代码
	public static final String TASK_ERRORCODE = "TASK_ERRORCODE";

	//序列
	public static final String SEQUENCE = "S_LF_TIMER_HISTORY";
	
	//任务名
	public static final String TIMER_TASK_NAME = "TIMER_TASK_NAME";
	
	//开始时间
	public static final String START_TIME = "START_TIME";

	//上次执行时间
	public static final String PRE_TIME = "PRE_TIME";

	//执行间隔（单位为：天）
	public static final String RUN_INTERVAL = "RUN_INTERVAL";

	//失败重试时间
	public static final String FAIL_RETRY_TIME = "FAIL_RETRY_TIME";

	//失败重试次数
	public static final String FAIL_RETRY_COUNT = "FAIL_RETRY_COUNT";

	//运行次数(每天定时任务允许运行总次数)
	public static final String RUN_COUNT = "RUNCOUNT";

	//运行次数（每天定时任务允许运行的次数)
	public static final String RUN_PER_COUNT = "RUNPERCOUNT";

	//类名（用于区分类，加载类）
	public static final String CLASS_NAME = "CLASS_NAME";

	//异常情况是否需要重发1-需要；0,null-不需要
	public static final String CUSTOMINTERVAL = "CUSTOMINTERVAL";

	//重发次数
	public static final String ISRERUN = "ISRERUN";

	//自定义时间差
	public static final String RERUNCOUNT = "RERUNCOUNT";
	
	//执行间隔单位。1-天；2-月 
	public static final String INTERVAL_UNIT = "INTERVAL_UNIT";

	//映射集合
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfTimerHistory", TABLE_NAME);
		columns.put("tableId", TA_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("taId", TA_ID);
		columns.put("timerTaskId", TIMER_TASK_ID);
		columns.put("runTime", RUN_TIME);
		columns.put("runYear", RUN_YEAR);
		columns.put("runMonth", RUN_MONTH);
		columns.put("runDay", RUN_DAY);
		columns.put("runResult", RUN_RESULT);
		columns.put("taskExpression", TASK_EXPRESSION);
		columns.put("taskErrorCode", TASK_ERRORCODE);
		
		columns.put("timerTaskName", TIMER_TASK_NAME);
		columns.put("startTime", START_TIME);
		columns.put("preTime", PRE_TIME);
		columns.put("runInterval", RUN_INTERVAL);
		columns.put("failRetryTime", FAIL_RETRY_TIME);
		columns.put("failRetryCount", FAIL_RETRY_COUNT);
		columns.put("runCount", RUN_COUNT);
		columns.put("runPerCount", RUN_PER_COUNT);
		columns.put("className", CLASS_NAME);
		columns.put("customInterval", CUSTOMINTERVAL);
		columns.put("isRerun", ISRERUN);
		columns.put("rerunCount", RERUNCOUNT);
		columns.put("intervalUnit", INTERVAL_UNIT);

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
