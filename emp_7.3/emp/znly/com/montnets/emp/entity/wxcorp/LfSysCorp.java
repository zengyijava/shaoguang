package com.montnets.emp.entity.wxcorp;

/**
 * 
 * @project montnets_entity
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-10-25 下午03:44:36
 * @description
 */
public class LfSysCorp implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1100135406595679746L;
	//标识ID
	private Long corpID;
	//企业编码
	private String corpCode;
	//企业名称
	private String corpName;
	//手机号码
	private String mobile;
	//联系电话
	private String phone;
	//企业地址
	private String address;
	//联系人
	private String linkman;
	//电子邮箱
	private String emails;
	//
	private String userName;

	private String lgoUrl;
	//尾号位数（默认4位）
	private Integer subnoDigit;
	//当前分配尾号（默认0）
	private String curSubno;

	public Integer getSubnoDigit() {
		return subnoDigit;
	}

	public void setSubnoDigit(Integer subnoDigit) {
		this.subnoDigit = subnoDigit;
	}

	public String getCurSubno() {
		return curSubno;
	}

	public void setCurSubno(String curSubno) {
		this.curSubno = curSubno;
	}

	private Integer isBalance;// 默认0不计费，1为计费。

	public Integer getIsBalance() {
		return isBalance;
	}

	public void setIsBalance(Integer isBalance) {
		this.isBalance = isBalance;
	}

	public Long getCorpID() {
		return corpID;
	}

	public void setCorpID(Long corpID) {
		this.corpID = corpID;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getEmails() {
		return emails;
	}

	public void setEmails(String emails) {
		this.emails = emails;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLgoUrl() {
		return lgoUrl;
	}

	public void setLgoUrl(String lgoUrl) {
		this.lgoUrl = lgoUrl;
	}

}
