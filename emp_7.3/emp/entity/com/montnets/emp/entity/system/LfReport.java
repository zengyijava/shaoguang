package com.montnets.emp.entity.system;

/**
 * 
 * @project montnets_entity
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-24 下午02:31:18
 * @description 状态报告类
 */
public class LfReport implements java.io.Serializable
{
	/**
	 * 状态报告类
	 */
	private static final long serialVersionUID = -2618713516027466699L;
	//发送账号
	private String spid;
	//发送账号密码
	private String sppassword;
	private String mtmsgid;
	private String mtstat;
	private String mterrorcode;
	//任务id
	private Long taskId;
	//手机号
	private String phone;
	private Long userMsgId;
	//模块id
	private Integer moduleId;
	
	public Long getUserMsgId() {
		return userMsgId;
	}
	public void setUserMsgId(Long userMsgId) {
		this.userMsgId = userMsgId;
	}
	public Integer getModuleId() {
		return moduleId;
	}
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	public String getSpid()
	{
		return spid;
	}
	public void setSpid(String spid)
	{
		this.spid = spid;
	}
	public String getSppassword()
	{
		return sppassword;
	}
	public void setSppassword(String sppassword)
	{
		this.sppassword = sppassword;
	}
	public String getMtmsgid()
	{
		return mtmsgid;
	}
	public void setMtmsgid(String mtmsgid)
	{
		this.mtmsgid = mtmsgid;
	}
	public String getMtstat()
	{
		return mtstat;
	}
	public void setMtstat(String mtstat)
	{
		this.mtstat = mtstat;
	}
	public String getMterrorcode()
	{
		return mterrorcode;
	}
	public void setMterrorcode(String mterrorcode)
	{
		this.mterrorcode = mterrorcode;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}	
}
