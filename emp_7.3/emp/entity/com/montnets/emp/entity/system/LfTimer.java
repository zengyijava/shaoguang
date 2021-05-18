/**
 * 
 */
package com.montnets.emp.entity.system;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-23 下午03:33:26
 * @description
 */

public class LfTimer implements java.io.Serializable
{
	/**
	 * 定时任务
	 */
	private static final long serialVersionUID = -7780706935535006548L;
	//任务ID
	private Long timerTaskId;
	//任务名
	private String timerTaskName;
	//运行状态（0-停止,1-运行,2-暂停)
	private Integer runState;
	//开始时间
	private Timestamp startTime;
	//上次执行时间
	private Timestamp preTime;
	//下次执行时间
	private Timestamp nextTime;
	//执行结果（0-失败 1-成功 2-等待执行结果）
	private Integer result;
	//执行间隔（单位由intervalUnit字段指定，默认为天）
	private Integer runInterval;
	//参数表达式,每个参数用 | 该字符隔开
	private String taskExpression;
	//错误代码
	private String taskErrorCode;
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
	
	
	
	public Integer getIntervalUnit()
	{
		return intervalUnit;
	}

	public void setIntervalUnit(Integer intervalUnit)
	{
		this.intervalUnit = intervalUnit;
	}

	//异常情况是否需要重发1-需要；0,null-不需要
	public Integer getIsRerun() {
		return isRerun;
	}

	public void setIsRerun(Integer isRerun) {
		this.isRerun = isRerun;
	}

	
	//重发次数
	public Integer getRerunCount() {
		return rerunCount;
	}

	public void setRerunCount(Integer rerunCount) {
		this.rerunCount = rerunCount;
	}

	//自定义时间差(有效范围)
	public Long getCustomInterval() {
		return customInterval;
	}
	
	public void setCustomInterval(Long customInterval) {
		this.customInterval = customInterval;
	}



	public LfTimer()
	{
		 
	}
	
	//任务ID
	public Long getTimerTaskId()
	{
		return timerTaskId;
	}

	public void setTimerTaskId(Long timerTaskId)
	{
		this.timerTaskId = timerTaskId;
	}

	//任务名
	public String getTimerTaskName()
	{
		return timerTaskName;
	}



	public void setTimerTaskName(String timerTaskName)
	{
		this.timerTaskName = timerTaskName;
	}

	//运行状态（0-停止,1-运行,2-暂停)
	public Integer getRunState()
	{
		return runState;
	}
	public void setRunState(Integer runState)
	{
		this.runState = runState;
	}

	//开始时间
	public Timestamp getStartTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(startTime != null){
			return Timestamp.valueOf(sdf.format(startTime.getTime()));
		}
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(startTime != null){
			this.startTime = Timestamp.valueOf(sdf.format(startTime.getTime()));
		}else{
			this.startTime = startTime;
		}
	}

	//上次执行时间
	public Timestamp getPreTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(preTime != null){
			return Timestamp.valueOf(sdf.format(preTime.getTime()));
		}
		return preTime;
	}

	public void setPreTime(Timestamp preTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(preTime != null){
			this.preTime = Timestamp.valueOf(sdf.format(preTime.getTime()));
		}else{
			this.preTime = preTime;
		}
	}

	//下次执行时间
	public Timestamp getNextTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(nextTime != null){
			return Timestamp.valueOf(sdf.format(nextTime.getTime()));
		}
		return nextTime;
	}

	public void setNextTime(Timestamp nextTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(nextTime != null){
			this.nextTime = Timestamp.valueOf(sdf.format(nextTime.getTime()));
		}else{
			this.nextTime = nextTime;
		}
	}

	//执行结果（0-失败 1-成功 2-等待执行结果）
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}

	//执行间隔（单位为：天）
	public Integer getRunInterval() {
		return runInterval;
	}

	public void setRunInterval(Integer runInterval) {
		this.runInterval = runInterval;
	}

	
	//参数表达式,每个参数用 | 该字符隔开
	public String getTaskExpression() {
		return taskExpression;
	}

	public void setTaskExpression(String taskExpression) {
		this.taskExpression = taskExpression;
	}

	
	//错误代码
	public String getTaskErrorCode() {
		return taskErrorCode;
	}

	public void setTaskErrorCode(String taskErrorCode) {
		this.taskErrorCode = taskErrorCode;
	}

	//失败重试时间
	public Timestamp getFailRetryTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(failRetryTime != null){
			return Timestamp.valueOf(sdf.format(failRetryTime.getTime()));
		}
		return failRetryTime;
	}

	public void setFailRetryTime(Timestamp failRetryTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(failRetryTime != null){
			this.failRetryTime = Timestamp.valueOf(sdf.format(failRetryTime.getTime()));
		}else{
			this.failRetryTime = failRetryTime;
		}
	}

	//失败重试次数
	public Integer getFailRetryCount() {
		return failRetryCount;
	}

	public void setFailRetryCount(Integer failRetryCount) {
		this.failRetryCount = failRetryCount;
	}


	//运行次数(每天定时任务允许运行总次数)
	public Integer getRunCount()
	{
		return runCount;
	}
	public void setRunCount(Integer runCount)
	{
		this.runCount = runCount;
	}


	//运行次数（每天定时任务允许运行的次数)
	public Integer getRunPerCount()
	{
		return runPerCount;
	}
	public void setRunPerCount(Integer runPerCount)
	{
		this.runPerCount = runPerCount;
	}


	//类名（用于区分类，加载类）
	public String getClassName()
	{
		return className;
	}
	public void setClassName(String className)
	{
		this.className = className;
	}

}
