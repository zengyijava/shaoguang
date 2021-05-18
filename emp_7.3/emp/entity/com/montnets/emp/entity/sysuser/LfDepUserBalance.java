package com.montnets.emp.entity.sysuser;

import java.io.Serializable;

public class LfDepUserBalance implements Serializable {

	/**
	 * 机构(操作员)余额表
	 */
	private static final long serialVersionUID = 8115979724333637909L;
	// 充值/回收表序列id，标识机构（操作员）余额记录
	private Long blId;
	// 所属机构id
	private Long targetId;
	// 短信余额：短信可用余额
	private Long smsBalance;
	// 短信已发短信数量
	private Long smsCount;
	// 彩信余额：彩信可用余额
	private Long mmsBalance;
	// 彩信已发 数量
	private Long mmsCount;
	// 企业编号
	private String corpCode;
	//短信阀值
	private Integer smsAlarm;
	//彩信阀值
	private Integer mmsAlarm;
	//通知人姓名
	private String alarmName;
	//通知人手机号
	private String alarmPhone;
	//通知次数
	private Integer alarmCount;
	//短信已通知次数
	private Integer alarmedCount;
	//是否每天通知 0否 1是
	private Integer hasDayAlarm;
	//彩信已通知次数
	private Integer mmsAlarmedCount;

	// 短信可分配余额
	// private Long smsAllotBln;
	// 彩信可分配余额
	// private Long mmsAllotBln;

	public LfDepUserBalance() {

	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public Long getBlId() {
		return blId;
	}

	public void setBlId(Long blId) {
		this.blId = blId;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public Long getSmsBalance() {
		return smsBalance;
	}

	public void setSmsBalance(Long smsBalance) {
		this.smsBalance = smsBalance;
	}

	public Long getSmsCount() {
		return smsCount;
	}

	public void setSmsCount(Long smsCount) {
		this.smsCount = smsCount;
	}

	public Long getMmsBalance() {
		return mmsBalance;
	}

	public void setMmsBalance(Long mmsBalance) {
		this.mmsBalance = mmsBalance;
	}

	public Long getMmsCount() {
		return mmsCount;
	}

	public void setMmsCount(Long mmsCount) {
		this.mmsCount = mmsCount;
	}

	public Integer getSmsAlarm()
	{
		return smsAlarm;
	}

	public void setSmsAlarm(Integer smsAlarm)
	{
		this.smsAlarm = smsAlarm;
	}

	public Integer getMmsAlarm()
	{
		return mmsAlarm;
	}

	public void setMmsAlarm(Integer mmsAlarm)
	{
		this.mmsAlarm = mmsAlarm;
	}

	public String getAlarmName()
	{
		return alarmName;
	}

	public void setAlarmName(String alarmName)
	{
		this.alarmName = alarmName;
	}

	public String getAlarmPhone()
	{
		return alarmPhone;
	}

	public void setAlarmPhone(String alarmPhone)
	{
		this.alarmPhone = alarmPhone;
	}

	public Integer getAlarmCount()
	{
		return alarmCount;
	}

	public void setAlarmCount(Integer alarmCount)
	{
		this.alarmCount = alarmCount;
	}

	public Integer getAlarmedCount()
	{
		return alarmedCount;
	}

	public void setAlarmedCount(Integer alarmedCount)
	{
		this.alarmedCount = alarmedCount;
	}

	public Integer getHasDayAlarm()
	{
		return hasDayAlarm;
	}

	public void setHasDayAlarm(Integer hasDayAlarm)
	{
		this.hasDayAlarm = hasDayAlarm;
	}

	public Integer getMmsAlarmedCount()
	{
		return mmsAlarmedCount;
	}

	public void setMmsAlarmedCount(Integer mmsAlarmedCount)
	{
		this.mmsAlarmedCount = mmsAlarmedCount;
	}
	
}
