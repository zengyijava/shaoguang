package com.montnets.emp.common.vo;

import java.sql.Timestamp;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-2-25 上午09:44:01
 * @description 账户监控Vo
 */

public class LfBirthdaySetupVo implements java.io.Serializable
{
	//主键
	private Long id;
	//操作员id
	private Long userId;
	//标题
	private String title;
	//祝福语内容
	private String msg;
	//发送时间
	private Timestamp sendTime;
	//是否加尊称(1是；2否)
	private Integer isAddName;
	//尊称
	private String addName;
	//发送账号
	private String spUser;
	//是否启用(1是；2否)
	private Integer isUse;
	//祝福类型(1员工生日祝福；2客户生日祝福)
	private Integer type;
	private String corpCode;
	//业务类型
	private String buscode;
	//用户名称
	private String username;
	//机构id
	private Long depid;
	//机构名称
	private String depname;	
	//是否签名，1是，2否
	private Integer isSignName;
	//签名
	private String signName;
	
	//ID加密字符串
	private String idCipher;
	
	//获取id
	public Long getId() {
		return id;
	}
	//设置id
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Timestamp getSendTime() {
		return sendTime;
	}
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	public Integer getIsAddName() {
		return isAddName;
	}
	public void setIsAddName(Integer isAddName) {
		this.isAddName = isAddName;
	}
	public String getAddName() {
		return addName;
	}
	public void setAddName(String addName) {
		this.addName = addName;
	}
	public String getSpUser() {
		return spUser;
	}
	public void setSpUser(String spUser) {
		this.spUser = spUser;
	}
	public Integer getIsUse() {
		return isUse;
	}
	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getBuscode() {
		return buscode;
	}
	public void setBuscode(String buscode) {
		this.buscode = buscode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getDepid() {
		return depid;
	}
	public void setDepid(Long depid) {
		this.depid = depid;
	}
	public String getDepname() {
		return depname;
	}
	public void setDepname(String depname) {
		this.depname = depname;
	}
	public Integer getIsSignName() {
		return isSignName;
	}
	public void setIsSignName(Integer isSignName) {
		this.isSignName = isSignName;
	}
	public String getSignName() {
		return signName;
	}
	public void setSignName(String signName) {
		this.signName = signName;
	}
	public String getIdCipher() {
		return idCipher;
	}
	public void setIdCipher(String idCipher) {
		this.idCipher = idCipher;
	}	
	
	
}
