package com.montnets.emp.common.timer.dao;

import java.sql.Timestamp;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-23 下午03:33:26
 * @description
 */

public class LfTimerVo implements java.io.Serializable
{
	/**
	 * 定时任务vo
	 */
	private static final long serialVersionUID = 4122760260028623114L;
    //主键
	private Long timerTaskId;
    //定时任务名称
	private String timerTaskName;
    //状态
	private Integer runState;
    //开始时间
	private Timestamp startTime;
	//上次执行时间
	private Timestamp preTime;
    //下次执行时间
	private Timestamp nextTime;
	//执行结果
	private Integer result;
	//执行间隔
	private Integer runInterval;
	//任务表达式
	private String taskExpression;
	//错误编码
	private String taskErrorCode;
	//失败重试时间
	private Timestamp failRetryTime;
	//失败重试次数
	private Integer failRetryCount;
	//运行次数
	private Integer runCount;
	//上次运行次数
	private Integer runPerCount;
	//业务类路径名
	private String className;
	//用户userid
	private Long userId;	
	
	public LfTimerVo()
	{	 
	}	
	//获取操作员id
	public Long getUserId() {
		return userId;
	}
	//设置操作员id
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getTimerTaskId()
	{
		return timerTaskId;
	}

	public void setTimerTaskId(Long timerTaskId)
	{
		this.timerTaskId = timerTaskId;
	}

	public String getTimerTaskName()
	{
		return timerTaskName;
	}

	public void setTimerTaskName(String timerTaskName)
	{
		this.timerTaskName = timerTaskName;
	}

	public Integer getRunState()
	{
		return runState;
	}

	public void setRunState(Integer runState)
	{
		this.runState = runState;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getPreTime() {
		return preTime;
	}

	public void setPreTime(Timestamp preTime) {
		this.preTime = preTime;
	}

	public Timestamp getNextTime() {
		return nextTime;
	}

	public void setNextTime(Timestamp nextTime) {
		this.nextTime = nextTime;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public Integer getRunInterval() {
		return runInterval;
	}

	public void setRunInterval(Integer runInterval) {
		this.runInterval = runInterval;
	}

	public String getTaskExpression() {
		return taskExpression;
	}

	public void setTaskExpression(String taskExpression) {
		this.taskExpression = taskExpression;
	}

	public String getTaskErrorCode() {
		return taskErrorCode;
	}

	public void setTaskErrorCode(String taskErrorCode) {
		this.taskErrorCode = taskErrorCode;
	}

	public Timestamp getFailRetryTime() {
		return failRetryTime;
	}

	public void setFailRetryTime(Timestamp failRetryTime) {
		this.failRetryTime = failRetryTime;
	}

	public Integer getFailRetryCount() {
		return failRetryCount;
	}

	public void setFailRetryCount(Integer failRetryCount) {
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
}
