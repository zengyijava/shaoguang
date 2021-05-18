package com.montnets.emp.entity.sysuser;

import java.sql.Timestamp;


/**
 * TableLfSysuser
 * 
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 09:25:01
 * @description
 */
public class LfSysuser implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4923255341568942848L;
	/**
	 * 
	 */
	// private static final long serialVersionUID = -7849806703810437897L;
	//操作员userid
	private Long userId;
	//机构id
	private Long depId;
	//操作员id
	private String userName;
	//操作员名称
	private String name;
	//性别
	private Integer sex;
	//生日
	private Timestamp birthday;
	//手机号码
	private String mobile;
	//座机
	private String oph;
	//QQ号码
	private String qq;
	//Email
	private String EMail;
	//MSN
	private String msn;
	//密码
	private String password;
	//操作员状态（0为禁用，1为启用，2为注销）
	private Integer userState;
	//注册时间
	private Timestamp regTime;
	//开户人
	private String holder;
	//描述
	private String comments;
	//工作状态（0无效，1有效）
	private Integer workState;
	//手工输入权限
	private Integer manualInput;
	//用户类型（1-管理员 ；2-用户）
	private Integer userType;
	//自建群组id
	private Integer udgId;
	//是否（0否，1是）能查看部门客户
	private Integer clientState;
	//员工ID(对应员工表中的员工ID)
	private Long employeeId;
	//唯一标识（通讯录用）
	private Long guId;
	//判断是否在线（0-不在线；1-在线）
	private Integer onLine;
	//企业编码
	private String corpCode;
	//职位ID（对应LF_POSITION表中的P-ID)
	private Integer pid;
	private Long postId;
	
	//在职状态（A表示在职，其他为离职）	默认为A
	private String hrStatus;
	//权限类型 1：个人权限  2：机构权限
	private Integer permissionType;
	//用户编码
	private String userCode;
	//是否存在尾号1为有尾号，0、2为没有尾号，默认为0
	private Integer isExistSubNo;
	//统一平台操作员标识ID
	private Long upGuId;
	
	private String fax;
	
	//是否是审核人，1：是，2：否
	private Integer isReviewer;
	//职务
	private String duties;
	//是否需要被审核，1：要，2：不需要
	private Integer isAudited;
	//功能控制标志位:第一位：是否支持审核短信提醒0：否 1：是
	private String controlFlag;
	//密码输错次数
	private Integer pwderrortimes; 
	//工号
	private String worknumber;

	//密码最后修改时间
	private Timestamp pwdupdatetime; 
	
	//找回密码的次数
	private Integer findPwdtimes; 
	//发送找回密码的日期
	private String findPwddate; 
	//0，非客服，1属于客服人员
	private Integer isCustome;
	//0显示数字,1不显示数字
	private Integer showNum = 0;
	
	//语言设置
	private String languageCode;
	public LfSysuser() {

		regTime = new Timestamp(System.currentTimeMillis());
		permissionType = new Integer(2);
		this.hrStatus ="A";
		this.languageCode="zh_TW";
	}

	public Integer getShowNum() {
		return showNum;
	}

	public void setShowNum(Integer showNum) {
		this.showNum = showNum;
	}

	public Integer getIsCustome()
	{
		return isCustome;
	}



	public void setIsCustome(Integer isCustome)
	{
		this.isCustome = isCustome;
	}



	//统一平台操作员标识ID
	public Long getUpGuId() {
		return upGuId;
	}

	public void setUpGuId(Long upGuId) {
		this.upGuId = upGuId;
	}

	
	
	//找回密码的次数
	public Integer getFindPwdtimes()
	{
		return findPwdtimes;
	}



	public void setFindPwdtimes(Integer findPwdtimes)
	{
		this.findPwdtimes = findPwdtimes;
	}


	//发送找回密码的日期
	public String getFindPwddate()
	{
		return findPwddate;
	}



	public void setFindPwddate(String findPwddate)
	{
		this.findPwddate = findPwddate;
	}



	//在职状态（A表示在职，其他为离职）	默认为A
	public String getHrStatus() {
		return hrStatus;
	}

	public void setHrStatus(String hrStatus) {
		this.hrStatus = hrStatus;
	}

	//密码输错次数
	public Integer getPwderrortimes()
	{
		return pwderrortimes;
	}


	//密码最后修改时间
	public void setPwderrortimes(Integer pwderrortimes)
	{
		this.pwderrortimes = pwderrortimes;
	}



	public Timestamp getPwdupdatetime()
	{
		return pwdupdatetime;
	}



	public void setPwdupdatetime(Timestamp pwdupdatetime)
	{
		this.pwdupdatetime = pwdupdatetime;
	}

	//工号
	public String getWorknumber() {
		return worknumber;
	}

	public void setWorknumber(String worknumber) {
		this.worknumber = worknumber;
	}

	//是否存在尾号1为有尾号，0、2为没有尾号，默认为0
	public Integer getIsExistSubNo() {
		return isExistSubNo;
	}

	public void setIsExistSubNo(Integer isExistSubNo) {
		this.isExistSubNo = isExistSubNo;
	}
	
	
	//功能控制标志位
	public String getControlFlag()
	{
		return controlFlag;
	}



	public void setControlFlag(String controlFlag)
	{
		this.controlFlag = controlFlag;
	}



	//操作员userid
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	//操作员id
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	//操作员名称
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	//性别
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	//生日
	public Timestamp getBirthday() {
		return birthday;
	}

	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}

	//手机号码
	public String getMobile() {
		if(mobile == null){
			return "";
		}
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	//座机
	public String getOph() {
		return oph;
	}

	public void setOph(String oph) {
		this.oph = oph;
	}

	//QQ号码
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	//Email
	public String getEMail() {
		return EMail;
	}

	public void setEMail(String eMail) {
		EMail = eMail;
	}

	//MSN
	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	//密码
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	//操作员状态（0为禁用，1为启用，2为注销）
	public Integer getUserState() {
		return userState;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
	}

	//注册时间
	public Timestamp getRegTime() {
		return regTime;
	}

	public void setRegTime(Timestamp regTime) {
		this.regTime = regTime;
	}

	//开户人
	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	//描述
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	//工作状态（0无效，1有效）
	public Integer getWorkState() {
		return workState;
	}

	public void setWorkState(Integer workState) {
		this.workState = workState;
	}

	//手工输入权限
	public Integer getManualInput() {
		return manualInput;
	}

	public void setManualInput(Integer manualInput) {
		this.manualInput = manualInput;
	}

	//用户类型（1-管理员 ；2-用户）
	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	//自建群组id
	public Integer getUdgId() {
		return udgId;
	}

	public void setUdgId(Integer udgId) {
		this.udgId = udgId;
	}

	//是否（0否，1是）能查看部门客户
	public Integer getClientState() {
		return clientState;
	}

	public void setClientState(Integer clientState) {
		this.clientState = clientState;
	}

	//机构id
	public Long getDepId() {
		return depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}

	//员工ID(对应员工表中的员工ID)
	public Long getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(Long employeeId)
	{
		this.employeeId = employeeId;
	}

	//唯一标识（通讯录用）
	public Long getGuId()
	{
		return guId;
	}

	public void setGuId(Long guId)
	{
		this.guId = guId;
	}

	//判断是否在线（0-不在线；1-在线）
	public Integer getOnLine()
	{
		return onLine;
	}

	public void setOnLine(Integer onLine)
	{
		this.onLine = onLine;
	}
	
	//企业编码
	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}

	//职位ID（对应LF_POSITION表中的P-ID)
	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	
	//权限类型 1：个人权限  2：机构权限
	public Integer getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(Integer permissionType) {
		this.permissionType = permissionType;
	}

	//用户编码
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}



	public String getFax()
	{
		return fax;
	}



	public void setFax(String fax)
	{
		this.fax = fax;
	}



	public Integer getIsReviewer()
	{
		return isReviewer;
	}



	public void setIsReviewer(Integer isReviewer)
	{
		this.isReviewer = isReviewer;
	}



	public String getDuties()
	{
		return duties;
	}



	public void setDuties(String duties)
	{
		this.duties = duties;
	}



	public Integer getIsAudited()
	{
		return isAudited;
	}



	public void setIsAudited(Integer isAudited)
	{
		this.isAudited = isAudited;
	}



	public String getLanguageCode() {
		return languageCode;
	}



	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	
	
}