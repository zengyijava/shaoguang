package com.montnets.emp.jfcx.vo;

import java.sql.Timestamp;

/**
 * 客户计费查询
 * @project p_ydyw
 * @author zhangl 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-1-15 下午04:35:46
 * @description
 */
public class CrmBillQueryVo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3201282192917120629L;
	
	private Long contractid;//签约id
	
	private String mobile; //手机号码
	
	private String customname;//客户 姓名
	
	private String identno;//证件号码
	
	private String debitaccount;//扣费账号
	
	private String acctno;//签约账号
	
	private String taocanname;//套餐名字
	
	private Integer taocantype;//套餐类型（1：VIP免费；2：包月；3：包季；4：包年；）
	
	private Integer taocanmoney;//套餐资费金额
	
	private String taocancode;//套餐编码
	
	private Integer contractstate;//签约状态（0:：已签约；1：已取消签约；2：已冻结）
	
	private Integer deductionsmoney;//扣费金额
	
	private Timestamp oprtime;//操作时间

	private Integer oprstate;//扣费状态（0：等待扣费；1：扣费成功；2：扣费失败；3：退费成功；4：退费失败；5：退费申请中）
	
	private String depname;//签约机构名称
	
	private String sendTime;
	
	private String endTime;
	
	private String isContainsSun;//是否包含子机构

	private String depids;
	
	private String corpcode;
	
	
	public String getTaocancode() {
		return taocancode;
	}

	public void setTaocancode(String taocancode) {
		this.taocancode = taocancode;
	}

	public String getCorpcode() {
		return corpcode;
	}

	public void setCorpcode(String corpcode) {
		this.corpcode = corpcode;
	}

	public String getDepids() {
		return depids;
	}

	public void setDepids(String depids) {
		this.depids = depids;
	}

	public String getIsContainsSun() {
		return isContainsSun;
	}

	public void setIsContainsSun(String isContainsSun) {
		this.isContainsSun = isContainsSun;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getOprstate() {
		return oprstate;
	}

	public void setOprstate(Integer oprstate) {
		this.oprstate = oprstate;
	}

	public String getDepname() {
		return depname;
	}

	public void setDepname(String depname) {
		this.depname = depname;
	}

	public Timestamp getOprtime() {
		return oprtime;
	}

	public void setOprtime(Timestamp oprtime) {
		this.oprtime = oprtime;
	}

	public Integer getTaocanmoney() {
		return taocanmoney;
	}

	public void setTaocanmoney(Integer taocanmoney) {
		this.taocanmoney = taocanmoney;
	}

	public Integer getContractstate() {
		return contractstate;
	}

	public void setContractstate(Integer contractstate) {
		this.contractstate = contractstate;
	}

	public Integer getDeductionsmoney() {
		return deductionsmoney;
	}

	public void setDeductionsmoney(Integer deductionsmoney) {
		this.deductionsmoney = deductionsmoney;
	}

	public String getTaocanname() {
		return taocanname;
	}

	public void setTaocanname(String taocanname) {
		this.taocanname = taocanname;
	}

	public Integer getTaocantype() {
		return taocantype;
	}

	public void setTaocantype(Integer taocantype) {
		this.taocantype = taocantype;
	}

	public String getDebitaccount() {
		return debitaccount;
	}

	public void setDebitaccount(String debitaccount) {
		this.debitaccount = debitaccount;
	}

	public String getAcctno() {
		return acctno;
	}


	public void setAcctno(String acctno) {
		this.acctno = acctno;
	}



	
	
	
	public String getIdentno() {
		return identno;
	}


	public void setIdentno(String identno) {
		this.identno = identno;
	}


	public Long getContractid() {
		return contractid;
	}


	public void setContractid(Long contractid) {
		this.contractid = contractid;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getCustomname() {
		return customname;
	}


	public void setCustomname(String customname) {
		this.customname = customname;
	}



	public CrmBillQueryVo(){}

	
	
	
	
	
}
