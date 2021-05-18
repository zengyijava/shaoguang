package com.montnets.emp.employee.vo;

import java.sql.Timestamp; 
 
/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-10 上午11:14:09
 * @description 
 */
public class LfEmployeeVo implements java.io.Serializable
{
	/**
	 * 员工表vo
	 */
	private static final long serialVersionUID = 4858484003154896689L;
	//员工id
	private Integer employeeId;
	private Integer pid;
	//部门id
	private Long depId;
	//人员编码
	private String employeeNo;
	//状态
	private Integer estate;
	//备注
	private String commnets;
	private Integer recState;
	private Integer hidephState;
	//email
	private String email;
	//msn
	private String msn;
	//qq
	private String qq;
	//名称
	private String name;
	//性别
	private Integer sex;
	//手机号
	private String mobile;
	//生日
	private Timestamp birthday;
	//座机
	private String oph;
	//guid
	private Long guId;
	//机构名称
	private String depName;	
	//private String depCode;
	//员工编码
	private String employeeCode;
	
	private String hrStatus;
	//修改时间
	private Timestamp lastUpddttm;
	
	private Integer isOperator;
	
	private Integer udgId;
	//机构IDs查询条件 
	private String depIds;
	
	
	
	/**
	 * ~~~~~~~~~~~~~~~~~~~只做传值 ，未做查询
	 */
	//职位名称
	private String dutyName;
	//生日开始时间
	private String beginbir;
	//生日结束时间
	private String endbir;
	//登录账号
	private String userName;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDutyName() {
		return dutyName;
	}

	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}

	public String getBeginbir() {
		return beginbir;
	}

	public void setBeginbir(String beginbir) {
		this.beginbir = beginbir;
	}

	public String getEndbir() {
		return endbir;
	}

	public void setEndbir(String endbir) {
		this.endbir = endbir;
	}
	
	/**
	 * ~~~~~~~~~~~~~~~~~~~
	 */
	
	
	public LfEmployeeVo(){}   

	public String getDepName()
	{
		return depName;
	}
	
	public void setDepName(String depName)
	{
		this.depName = depName;
	}

	public Integer getEmployeeId()
	{
		return employeeId;
	}
 
	public void setEmployeeId(Integer employeeId)
	{
		this.employeeId = employeeId;
	}
 
	public Long getDepId()
	{
		return depId;
	}

	public void setDepId(Long depId)
	{
		this.depId = depId;
	}

	public String getEmployeeNo()
	{
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo)
	{
		this.employeeNo = employeeNo;
	}

	public Integer getPid()
	{
		return pid;
	}

	public void setPid(Integer pid)
	{
		this.pid = pid;
	}

	public Integer getEstate()
	{
		return estate;
	}

	public void setEstate(Integer estate)
	{
		this.estate = estate;
	}

	public String getCommnets()
	{
		return commnets;
	}

	public void setCommnets(String commnets)
	{
		this.commnets = commnets;
	}

	public Integer getRecState()
	{
		return recState;
	}

	public void setRecState(Integer recState)
	{
		this.recState = recState;
	}

	public Integer getHidephState()
	{
		return hidephState;
	}

	public void setHidephState(Integer hidephState)
	{
		this.hidephState = hidephState;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getMsn()
	{
		return msn;
	}

	public void setMsn(String msn)
	{
		this.msn = msn;
	}

	public String getQq()
	{
		return qq;
	}

	public void setQq(String qq)
	{
		this.qq = qq;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getSex()
	{
		return sex;
	}

	public void setSex(Integer sex)
	{
		this.sex = sex;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	 
	public Timestamp getBirthday()
	{
		return birthday;
	}

	public void setBirthday(Timestamp birthday)
	{
		this.birthday = birthday;
	}

	public String getOph()
	{
		return oph;
	}

	public void setOph(String oph)
	{
		this.oph = oph;
	}

	/*public String getDepCode()
	{
		return depCode;
	}

	public void setDepCode(String depCode)
	{
		this.depCode = depCode;
	}*/

	public String getEmployeeCode()
	{
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode)
	{
		this.employeeCode = employeeCode;
	}

	public String getHrStatus()
	{
		return hrStatus;
	}

	public void setHrStatus(String hrStatus)
	{
		this.hrStatus = hrStatus;
	}

	public Timestamp getLastUpddttm()
	{
		return lastUpddttm;
	}

	public void setLastUpddttm(Timestamp lastUpddttm)
	{
		this.lastUpddttm = lastUpddttm;
	}

	public Integer getIsOperator()
	{
		return isOperator;
	}

	public void setIsOperator(Integer isOperator)
	{
		this.isOperator = isOperator;
	}

	public Long getGuId()
	{
		return guId;
	}

	public void setGuId(Long guId)
	{
		this.guId = guId;
	}

	public Integer getUdgId()
	{
		return udgId;
	}

	public void setUdgId(Integer udgId)
	{
		this.udgId = udgId;
	}

	public String getDepIds() {
		return depIds;
	}

	public void setDepIds(String depIds) {
		this.depIds = depIds;
	}
}
