package com.montnets.emp.common.vo;

import java.sql.Timestamp;
import java.util.List;

public class LfSysuserVo implements java.io.Serializable
{
	/**
	 * 操作员
	 */
	private static final long serialVersionUID = -4943327546486340200L;
    //操作员userid
	private Long userId;
	//操作员名称
	private String name;
	
	private Integer userType;
    //用户状态
	private Integer userState;

	private String holder;

	private Timestamp regTime;
    //用户手机号
	private String mobile;

	private String oph;
    //用户QQ号
	private String qq;
    //用户 email
	private String EMail;
    //用户性别
	private Integer sex;
    //用户密码
	private String password;
    //用户名称
	private String userName;
	//机构id
	private String depId;
	//机构名称
	private String depName;
	
	private Long postId;
    //管辖类型
	private Integer permissionType;
	//用户code
	private String userCode;
	
	@SuppressWarnings("unchecked")
	private List domDepList;

	@SuppressWarnings("unchecked")
	private List roleList;
	
	private Integer pid;
	
	private Long guId;
	
	
	private Integer isExistSubno;
	
	private String usedSubno;
	
	private String fax;
	private String msn;
	private Timestamp birthday;
	private String duties;
	private Integer isReviewer;
	private String comments;
	//查询操作员是否包含子机构
	private Integer isAll;
	/**
	 *   
	 * ~~~~~~~~~~~~~~~查询的时候传值
	 */
	//创建开始时间
	private String submitSartTime;
	//至
	private String submitEndTime;
	//加密后的ID
	private String keyId;
	//0显示数字,1不显示数字
	private Integer showNum;

	public Integer getShowNum() {
		return showNum;
	}

	public void setShowNum(Integer showNum) {
		this.showNum = showNum;
	}

	public String getSubmitSartTime() {
		return submitSartTime;
	}

	public void setSubmitSartTime(String submitSartTime) {
		this.submitSartTime = submitSartTime;
	}

	public String getSubmitEndTime() {
		return submitEndTime;
	}

	public void setSubmitEndTime(String submitEndTime) {
		this.submitEndTime = submitEndTime;
	}
	
	/**
	 *   
	 * ~~~~~~~~~~~~~~~END
	 */

	public String getUsedSubno() {
		return usedSubno;
	}

	public void setUsedSubno(String usedSubno) {
		this.usedSubno = usedSubno;
	}

	public Integer getIsExistSubno() {
		return isExistSubno;
	}

	public void setIsExistSubno(Integer isExistSubno) {
		this.isExistSubno = isExistSubno;
	}

	public Long getGuId() {
		return guId;
	}

	public void setGuId(Long guId) {
		this.guId = guId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getUserState()
	{
		return userState;
	}

	public void setUserState(Integer userState)
	{
		this.userState = userState;
	}

	public String getHolder()
	{
		return holder;
	}

	public void setHolder(String holder)
	{
		this.holder = holder;
	}

	public Timestamp getRegTime()
	{
		return regTime;
	}

	public void setRegTime(Timestamp regTime)
	{
		this.regTime = regTime;
	}

	public String getDepName()
	{
		return depName;
	}

	public void setDepName(String depName)
	{
		this.depName = depName;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getOph()
	{
		return oph;
	}

	public void setOph(String oph)
	{
		this.oph = oph;
	}

	public String getQq()
	{
		return qq;
	}

	public void setQq(String qq)
	{
		this.qq = qq;
	}

	public String getEMail()
	{
		return EMail;
	}

	public void setEMail(String eMail)
	{
		EMail = eMail;
	}

	public Integer getSex()
	{
		return sex;
	}

	public void setSex(Integer sex)
	{
		this.sex = sex;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	@SuppressWarnings("unchecked")
	public List getDomDepList()
	{
		return domDepList;
	}

	@SuppressWarnings("unchecked")
	public void setDomDepList(List domDepList)
	{
		this.domDepList = domDepList;
	}

	@SuppressWarnings("unchecked")
	public List getRoleList()
	{
		return roleList;
	}

	@SuppressWarnings("unchecked")
	public void setRoleList(List roleList)
	{
		this.roleList = roleList;
	}

	public String getDepId()
	{
		return depId;
	}

	public void setDepId(String depId)
	{
		this.depId = depId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

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

	public Integer getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(Integer permissionType) {
		this.permissionType = permissionType;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public Timestamp getBirthday() {
		return birthday;
	}

	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}

	public String getDuties() {
		return duties;
	}

	public void setDuties(String duties) {
		this.duties = duties;
	}

	public Integer getIsReviewer() {
		return isReviewer;
	}

	public void setIsReviewer(Integer isReviewer) {
		this.isReviewer = isReviewer;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getIsAll()
	{
		return isAll;
	}

	public void setIsAll(Integer isAll)
	{
		this.isAll = isAll;
	}

	public String getKeyId()
	{
		return keyId;
	}

	public void setKeyId(String keyId)
	{
		this.keyId = keyId;
	}

	
}
