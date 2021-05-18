package com.montnets.emp.entity.birthwish;


public class LfBirthdayMember implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8138123577685994685L;
	/**
	 * 生日祝福成员实体类
	 */
	private Long id;
	//引用生日祝福设置表id
	private Long bsId;
	//祝福类型(1员工生日祝福；2客户生日祝福)
	private Integer type;
	//成员标示，用guid
	private Long memberId;
	//操作员id
	private Long userId;
	//祝福语内容(暂不适用)
	private String msg;
	//尊称(暂作为成员name用)
	private String addName;
	//企业编码
	private String corpCode;
	//成员类型，1为人员，2为单机构，3为包含子机构
	private Integer membertype;
	
	public Integer getMembertype() {
		return membertype;
	}
	public void setMembertype(Integer membertype) {
		this.membertype = membertype;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getBsId() {
		return bsId;
	}
	public void setBsId(Long bsId) {
		this.bsId = bsId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getAddName() {
		return addName;
	}
	public void setAddName(String addName) {
		this.addName = addName;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	
	
	
	
}