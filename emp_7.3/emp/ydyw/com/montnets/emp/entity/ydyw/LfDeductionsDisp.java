package com.montnets.emp.entity.ydyw;

import java.io.Serializable;
import java.sql.Timestamp;

public class LfDeductionsDisp implements Serializable
{
	
	private static final long serialVersionUID = 3078861464512997369L;

	//id主键
	private Long id;
	
	//签约ID 
	private Long contractid;
	
	//用户手机号码
	private String mobile;
	
	//客户姓名
	private String customname;

	//签约账号
	private String acctno;
	
	//扣款账号
	private String debitaccount;

	//套餐编码
	private String taocancode;
	
	//套餐资费金额
	private Integer taocanmoney;

	//套餐名称
	private String taocanname;

	//套餐类型（1：VIP免费；2：包月；3：包季；4：包年；）
	private Integer taocantype;
	
	//套餐类型（1：VIP免费；2：包月；3：包季；4：包年；）
	private Integer contractstate;

	//操作类型（0：扣费；1：退费）
	private Integer deductionstype;
	
	//扣费金额
	private Integer deductionsmoney;
	
	//操作结果（0：成功；1：失败；2：失败，余额不足）
	private Integer oprstate;

	//操作日期
	private Timestamp oprtime;

	//签约机构Id
	private Long contractdep;
	
	//签约机构名称
	private String depname;
	
	//签约操作员id
	private Long contractuser; 
	
	//签约操作员名称
	private String username;

	//签约机构ID
	private Long depid;
	
	//操作员id
	private Long userid;
	
	//企业编码
	private String corpcode;
	
	//流水号
	private String msgid;
	
	//更新时间
	private Timestamp updatetime;
	

	public LfDeductionsDisp(){
	} 

	public Long getContractid(){

		return contractid;
	}

	public void setContractid(Long contractid){

		this.contractid= contractid;

	}


	public String getAcctno(){

		return acctno;
	}

	public void setAcctno(String acctno){

		this.acctno= acctno;

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

	public Integer getOprstate(){

		return oprstate;
	}

	public void setOprstate(Integer oprstate){

		this.oprstate= oprstate;

	}

	public String getCorpcode(){

		return corpcode;
	}

	public void setCorpcode(String corpcode){

		this.corpcode= corpcode;

	}

	public Integer getTaocanmoney(){

		return taocanmoney;
	}

	public void setTaocanmoney(Integer taocanmoney){

		this.taocanmoney= taocanmoney;

	}

	public String getTaocanname(){

		return taocanname;
	}

	public void setTaocanname(String taocanname){

		this.taocanname= taocanname;

	}

	public String getCustomname(){

		return customname;
	}

	public void setCustomname(String customname){

		this.customname= customname;

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

	public Integer getDeductionsmoney(){

		return deductionsmoney;
	}

	public void setDeductionsmoney(Integer deductionsmoney){

		this.deductionsmoney= deductionsmoney;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public Timestamp getOprtime(){

		return oprtime;
	}

	public void setOprtime(Timestamp oprtime){

		this.oprtime= oprtime;

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

	public Integer getDeductionstype(){

		return deductionstype;
	}

	public void setDeductionstype(Integer deductionstype){

		this.deductionstype= deductionstype;

	}

	public Long getContractuser() {
		return contractuser;
	}

	public void setContractuser(Long contractuser) {
		this.contractuser = contractuser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getMsgid()
	{
		return msgid;
	}

	public void setMsgid(String msgid)
	{
		this.msgid = msgid;
	}

	public Timestamp getUpdatetime()
	{
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime)
	{
		this.updatetime = updatetime;
	}

	
}

							