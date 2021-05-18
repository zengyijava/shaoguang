package com.montnets.emp.entity.ydyw;

import java.io.Serializable;
import java.sql.Timestamp;

public class LfDeductionsList implements Serializable
{

	private static final long serialVersionUID = -1158774267250082967L;

	private Long id;//自增ID
	
	private Long contractid;//签约ID 
	
	private String mobile;//用户手机号码
	
	private String customname;//客户姓名
	
	private String acctno;//签约账号
	
	private String debitaccount;//扣款账号
	
	private String taocancode;//套餐编号
	
	private String taocanname;//套餐名称
	
	private Integer taocantype;//套餐类型（1：VIP免费；2：包月；3：包季；4：包年；）
	
	private Integer taocanmoney;//套餐资费金额
	
	private Integer iyear;//扣费-年
	
	private Integer imonth;//扣费-月
	
	private Integer bupsummoney;//当月退费金额总数
	
	private Integer buptimer;//当月退费次数
	
	private Integer contractstate;//签约状态（0:：已签约；1：已取消签约；2：已冻结）
	
	private Integer contracttype;//签约方式（0：柜台录入；1：主动上行）
	
	private Integer deductionstype;//扣费状态（0：等待扣费；1：扣费成功；2：扣费失败；3：退费成功；4：退费失败；5：退费申请中）
	
	private Integer buckuptimer;//欠费补扣次数
	
	private Timestamp udpatetime;//更新时间
	
	private Timestamp bucklefeetime;//扣费时间
	
	private Timestamp buckleupfeetime;//退费时间
	
	private Long contractdep;//签约机构ID

	private String depname;//签约机构名称
	
	private Long contractuser;//签约操作员ID

	private Long depid;//操作员机构ID
	
	private Long userid;//操作员ID
	
	private String corpcode;//企业编码
	
	private String msgid;//流水号

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public LfDeductionsList(){
	} 

	public Long getContractuser(){

		return contractuser;
	}

	public void setContractuser(Long contractuser){

		this.contractuser= contractuser;

	}

	public Integer getContractstate(){

		return contractstate;
	}

	public void setContractstate(Integer contractstate){

		this.contractstate= contractstate;

	}

	public String getDebitaccount(){

		return debitaccount;
	}

	public void setDebitaccount(String debitaccount){

		this.debitaccount= debitaccount;

	}

	public Integer getTaocanmoney(){

		return taocanmoney;
	}

	public void setTaocanmoney(Integer taocanmoney){

		this.taocanmoney= taocanmoney;

	}

	public Integer getContracttype(){

		return contracttype;
	}

	public void setContracttype(Integer contracttype){

		this.contracttype= contracttype;

	}

	public String getCustomname(){

		return customname;
	}

	public void setCustomname(String customname){

		this.customname= customname;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public Long getContractdep(){

		return contractdep;
	}

	public void setContractdep(Long contractdep){

		this.contractdep= contractdep;

	}

	public Integer getTaocantype(){

		return taocantype;
	}

	public void setTaocantype(Integer taocantype){

		this.taocantype= taocantype;

	}

	public Timestamp getBucklefeetime(){

		return bucklefeetime;
	}

	public void setBucklefeetime(Timestamp bucklefeetime){

		this.bucklefeetime= bucklefeetime;

	}

	public Integer getImonth(){

		return imonth;
	}

	public void setImonth(Integer imonth){

		this.imonth= imonth;

	}

	public Long getContractid(){

		return contractid;
	}

	public void setContractid(Long contractid){

		this.contractid= contractid;

	}

	public Integer getIyear(){

		return iyear;
	}

	public void setIyear(Integer iyear){

		this.iyear= iyear;

	}

	public Integer getBuckuptimer(){

		return buckuptimer;
	}

	public void setBuckuptimer(Integer buckuptimer){

		this.buckuptimer= buckuptimer;

	}

	public String getAcctno(){

		return acctno;
	}

	public void setAcctno(String acctno){

		this.acctno= acctno;

	}

	public String getCorpcode(){

		return corpcode;
	}

	public void setCorpcode(String corpcode){

		this.corpcode= corpcode;

	}

	public Integer getBuptimer(){

		return buptimer;
	}

	public void setBuptimer(Integer buptimer){

		this.buptimer= buptimer;

	}

	public Timestamp getUdpatetime(){

		return udpatetime;
	}

	public void setUdpatetime(Timestamp udpatetime){

		this.udpatetime= udpatetime;

	}

	public String getTaocanname(){

		return taocanname;
	}

	public void setTaocanname(String taocanname){

		this.taocanname= taocanname;

	}

	public Integer getBupsummoney(){

		return bupsummoney;
	}

	public void setBupsummoney(Integer bupsummoney){

		this.bupsummoney= bupsummoney;

	}

	public Long getDepid(){

		return depid;
	}

	public void setDepid(Long depid){

		this.depid= depid;

	}

	public String getDepname(){

		return depname;
	}

	public void setDepname(String depname){

		this.depname= depname;

	}

	public String getTaocancode(){

		return taocancode;
	}

	public void setTaocancode(String taocancode){

		this.taocancode= taocancode;

	}

	public String getMobile(){

		return mobile;
	}

	public void setMobile(String mobile){

		this.mobile= mobile;

	}

	public Integer getDeductionstype(){

		return deductionstype;
	}

	public void setDeductionstype(Integer deductionstype){

		this.deductionstype= deductionstype;

	}

	public Timestamp getBuckleupfeetime(){

		return buckleupfeetime;
	}

	public void setBuckleupfeetime(Timestamp buckleupfeetime){

		this.buckleupfeetime= buckleupfeetime;

	}

	public String getMsgid()
	{
		return msgid;
	}

	public void setMsgid(String msgid)
	{
		this.msgid = msgid;
	}

}

							
