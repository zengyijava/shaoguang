/**
 * 
 */
package com.montnets.emp.entity.system;

import java.sql.Timestamp;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-29 下午06:37:37
 * @description
 */

public class LfTimerHistory implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2495083222132930312L;
	
	// 标识ID
	private Long taId;
	
	// 任务ID
	private Long timerTaskId;
	
	//运行时间
	private Timestamp runTime;

	//运行时间（年）
	private Integer runYear;

	//运行时间（月）
	private Integer runMonth;

	//运行时间（天）
	private Integer runDay;
	
	//执行结果（0-失败 1-成功 2-等待执行结果4-过期未执行）
	//运行结果
	private Integer runResult;
	
	//任务表达式
	private String taskExpression;
	//错误代码
	private String taskErrorCode;
	
	//任务名
	private String timerTaskName;
	//开始时间
	private Timestamp startTime;
	//上次执行时间
	private Timestamp preTime;
	//执行间隔（单位由intervalUnit字段指定，默认为天）
	private Integer runInterval;
	//失败重试时间
	private Timestamp failRetryTime;
	//失败重试次数
	private Integer failRetryCount;
	//运行次数(每天定时任务允许运行总次数)
	private Integer runCount;	
	//运行次数（每天定时任务允许运行的次数)
	private Integer runPerCount;
	//类名（用于区分类，加载类）
	private String className;
	//自定义时间差(有效范围)
	private Long customInterval;
	//异常情况是否需要重发1-需要；0,null-不需要
	private Integer isRerun;
	//重发次数
	private Integer rerunCount;
	//执行间隔单位。1-天；2-月 ;3-小时
	private Integer intervalUnit;

	public LfTimerHistory()
	{
	}

	public String getTimerTaskName()
	{
		return timerTaskName;
	}

	public void setTimerTaskName(String timerTaskName)
	{
		this.timerTaskName = timerTaskName;
	}

	public Timestamp getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Timestamp startTime)
	{
		this.startTime = startTime;
	}

	public Timestamp getPreTime()
	{
		return preTime;
	}

	public void setPreTime(Timestamp preTime)
	{
		this.preTime = preTime;
	}

	public Integer getRunInterval()
	{
		return runInterval;
	}

	public void setRunInterval(Integer runInterval)
	{
		this.runInterval = runInterval;
	}

	public Timestamp getFailRetryTime()
	{
		return failRetryTime;
	}

	public void setFailRetryTime(Timestamp failRetryTime)
	{
		this.failRetryTime = failRetryTime;
	}

	public Integer getFailRetryCount()
	{
		return failRetryCount;
	}

	public void setFailRetryCount(Integer failRetryCount)
	{
		this.failRetryCount = failRetryCount;
	}

	public Integer getRunCount()
	{
		return runCount;
	}

	public void setRunCount(Integer runCount)
	{
		this.runCount = runCount;
	}

	public Integer getRunPerCount()
	{
		return runPerCount;
	}

	public void setRunPerCount(Integer runPerCount)
	{
		this.runPerCount = runPerCount;
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public Long getCustomInterval()
	{
		return customInterval;
	}

	public void setCustomInterval(Long customInterval)
	{
		this.customInterval = customInterval;
	}

	public Integer getIsRerun()
	{
		return isRerun;
	}

	public void setIsRerun(Integer isRerun)
	{
		this.isRerun = isRerun;
	}

	public Integer getRerunCount()
	{
		return rerunCount;
	}

	public void setRerunCount(Integer rerunCount)
	{
		this.rerunCount = rerunCount;
	}

	public Integer getIntervalUnit()
	{
		return intervalUnit;
	}

	public void setIntervalUnit(Integer intervalUnit)
	{
		this.intervalUnit = intervalUnit;
	}

	// 任务ID
	public Long getTimerTaskId()
	{
		return timerTaskId;
	}

	public Long getTaId()
	{
		return taId;
	}

	// 标识ID
	public void setTaId(Long taId)
	{
		this.taId = taId;
	}

	public void setTimerTaskId(Long timerTaskId)
	{
		this.timerTaskId = timerTaskId;
	}

	//运行时间
	public Timestamp getRunTime()
	{
		return runTime;
	}

	public void setRunTime(Timestamp runTime)
	{
		this.runTime = runTime;
	}

	//运行时间（年）
	public Integer getRunYear()
	{
		return runYear;
	}

	public void setRunYear(Integer runYear)
	{
		this.runYear = runYear;
	}

	//运行时间（月）
	public Integer getRunMonth()
	{
		return runMonth;
	}

	public void setRunMonth(Integer runMonth)
	{
		this.runMonth = runMonth;
	}

	//运行时间（天）
	public Integer getRunDay()
	{
		return runDay;
	}

	public void setRunDay(Integer runDay)
	{
		this.runDay = runDay;
	}

	
	//执行结果（0-失败 1-成功 2-等待执行结果4-过期未执行）
	//运行结果
	public Integer getRunResult()
	{
		return runResult;
	}

	public void setRunResult(Integer runResult)
	{
		this.runResult = runResult;
	}
	
	//任务表达式
	public String getTaskExpression()
	{
		return taskExpression;
	}

	public void setTaskExpression(String taskExpression)
	{
		this.taskExpression = taskExpression;
	}

	//错误代码
	public String getTaskErrorCode()
	{
		return taskErrorCode;
	}

	public void setTaskErrorCode(String taskErrorCode)
	{
		this.taskErrorCode = taskErrorCode;
	}

}
