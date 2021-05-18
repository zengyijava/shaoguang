package com.montnets.emp.jfcx.vo;

import java.sql.Timestamp;

/**
 * 客户退费管理
 * @project p_ydyw
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-12-29 上午09:52:01
 * @description
 */
public class CrmRfMgrVo implements java.io.Serializable{

	private static final long serialVersionUID = -3201282192917120629L;
	
	private Long id;//自增主键ID
	
	private Long contractid;//签约id
	
	private String mobile; //手机号码
	
	private String customname;//客户 姓名
	
	private String acctno;//签约账号
	
	private String debitaccount; //扣费账号
	
	private String taocanname;//套餐名称
	
	private String taocancode;//套餐编码
	
	private Integer taocantype;//套餐类型（1：VIP免费；2：包月；3：包季；4：包年；）
	
	private Integer taocanmoney;//套餐资费金额
	
	private Integer deductionstype; //扣费状态（0：等待扣费；1：扣费成功；2：扣费失败；3：退费成功；4：退费失败；5：退费申请中）
	
	private Long depId;//隶属机构id
	
	private String depids;//
	
	private String depname;//机构名称
	
	private Integer contractstate;//签约状态
	
	private Timestamp updatetime;//更新时间

	private Timestamp bucklefeetime;//扣费时间
	
	private Timestamp buckleupfeetime;//退费时间
	
	private String sendTime;//扣费开始时间
	
	private String endTime;//扣费结束时间 
	
	private String isContainsSun;//是否包含子机构
	
	private String corpcode;
	
	private String username;//操作员账号
	
	private String name;//操作员名称
	
	private Integer bupsummoney;//当月退费金额
	
	public CrmRfMgrVo(){}
	
	
	public Integer getBupsummoney() {
		return bupsummoney;
	}

	public void setBupsummoney(Integer bupsummoney) {
		this.bupsummoney = bupsummoney;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCorpcode() {
		return corpcode;
	}

	public void setCorpcode(String corpcode) {
		this.corpcode = corpcode;
	}


	public String getIsContainsSun() {
		return isContainsSun;
	}

	public void setIsContainsSun(String isContainsSun) {
		this.isContainsSun = isContainsSun;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getBucklefeetime() {
		return bucklefeetime;
	}

	public void setBucklefeetime(Timestamp bucklefeetime) {
		this.bucklefeetime = bucklefeetime;
	}

	public Timestamp getBuckleupfeetime() {
		return buckleupfeetime;
	}

	public void setBuckleupfeetime(Timestamp buckleupfeetime) {
		this.buckleupfeetime = buckleupfeetime;
	}

	public Integer getContractstate() {
		return contractstate;
	}

	public void setContractstate(Integer contractstate) {
		this.contractstate = contractstate;
	}

	public String getDepids() {
		return depids;
	}

	public void setDepids(String depids) {
		this.depids = depids;
	}

	public Long getContractid()
	{
		return contractid;
	}

	public void setContractid(Long contractid)
	{
		this.contractid = contractid;
	}

	public Timestamp getUpdatetime()
	{
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime)
	{
		this.updatetime = updatetime;
	}
	
	public String getTaocanname() {
		return taocanname;
	}

	public void setTaocanname(String taocanname) {
		this.taocanname = taocanname;
	}

	public String getTaocancode() {
		return taocancode;
	}

	public void setTaocancode(String taocancode) {
		this.taocancode = taocancode;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}


	public String getCustomname() {
		return customname;
	}

	public void setCustomname(String customname) {
		this.customname = customname;
	}

	public String getAcctno() {
		return acctno;
	}

	public void setAcctno(String acctno) {
		this.acctno = acctno;
	}

	public String getDebitaccount()
	{
		return debitaccount;
	}

	public void setDebitaccount(String debitaccount)
	{
		this.debitaccount = debitaccount;
	}

	public Integer getTaocantype() {
		return taocantype;
	}

	public void setTaocantype(Integer taocantype) {
		this.taocantype = taocantype;
	}

	public Integer getTaocanmoney() {
		return taocanmoney;
	}

	public void setTaocanmoney(Integer taocanmoney) {
		this.taocanmoney = taocanmoney;
	}

	public Integer getDeductionstype()
	{
		return deductionstype;
	}

	public void setDeductionstype(Integer deductionstype)
	{
		this.deductionstype = deductionstype;
	}

	public Long getDepId()
	{
		return depId;
	}

	public void setDepId(Long depId)
	{
		this.depId = depId;
	}

	public String getDepname()
	{
		return depname;
	}

	public void setDepname(String depname)
	{
		this.depname = depname;
	}

	public String getSendTime()
	{
		return sendTime;
	}

	public void setSendTime(String sendTime)
	{
		this.sendTime = sendTime;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	
}
