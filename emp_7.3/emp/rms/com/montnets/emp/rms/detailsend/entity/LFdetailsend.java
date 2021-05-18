package com.montnets.emp.rms.detailsend.entity;

public class LFdetailsend implements java.io.Serializable {

	/**
	 * 发送明细sp账号查询查询实体类
	 * 
	 * @author lvxin
	 */
	private static final long serialVersionUID = 6557264982551812482L;
	// sp账号
	private String userid;
	// 提交总数
	private Long icount;
	// 接收成功数
	private Long rsucc;
	// 企业编码
	private String corpCode;
	// 档位
	private Integer degree;
	// 失败数
	private Long rfail;
	// 容量套餐类型
	private String spisuncm;
	// 时间
	private Integer iymd;
	// 创建时间
	private String createTime;
	// 同步时间
	private String synTime;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Long getIcount() {
		return icount;
	}

	public void setIcount(Long icount) {
		this.icount = icount;
	}

	public Long getRsucc() {
		return rsucc;
	}

	public void setRsucc(Long rsucc) {
		this.rsucc = rsucc;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public Integer getDegree() {
		return degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
	}

	public Long getRfail() {
		return rfail;
	}

	public void setRfail(Long rfail) {
		this.rfail = rfail;
	}

	public String getSpisuncm() {
		return spisuncm;
	}

	public void setSpisuncm(String spisuncm) {
		this.spisuncm = spisuncm;
	}

	public Integer getIymd() {
		return iymd;
	}

	public void setIymd(Integer iymd) {
		this.iymd = iymd;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getSynTime() {
		return synTime;
	}

	public void setSynTime(String synTime) {
		this.synTime = synTime;
	}

}
