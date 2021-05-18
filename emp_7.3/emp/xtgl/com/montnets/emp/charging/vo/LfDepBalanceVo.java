package com.montnets.emp.charging.vo;

public class LfDepBalanceVo implements java.io.Serializable{

	private static final long serialVersionUID = -5256430003755263425L;
	//机构id
	private Long depId;
	//机构名称
	private String depName;
	//机构职责
	private String depResp;
	//短信余额
	private Long smsBalance;
	//短信可分配
	//private Long smsAllotBln;
	private Long smsCount ;
	//彩信余额
	private Long mmsBalance;
	//彩信可分配
	//private Long mmsAllotBln;
	private Long mmsCount;
	//短信阀值
	private Long smsAlarm;
	//彩信阀值
	private Long mmsAlarm;
	//通知人姓名
	private String alarmName;
	//通知人手机号码
	private String alarmPhone;
	public Long getDepId() {
		return depId;
	}
	public void setDepId(Long depId) {
		this.depId = depId;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public String getDepResp() {
		return depResp;
	}
	public void setDepResp(String depResp) {
		this.depResp = depResp;
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
	public Long getSmsAlarm()
	{
		return smsAlarm;
	}
	public void setSmsAlarm(Long smsAlarm)
	{
		this.smsAlarm = smsAlarm;
	}
	public Long getMmsAlarm()
	{
		return mmsAlarm;
	}
	public void setMmsAlarm(Long mmsAlarm)
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
	
}
