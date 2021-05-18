package com.montnets.emp.entity.ydyw;

import java.io.Serializable;
import java.sql.Timestamp;

/***
 * 套餐计费规则表
 * @author Administrator
 *
 */
public class LfProCharges implements Serializable{
	//计费规则ID
	private Long ruleid;
	//免费试用开始日期
	private Timestamp trystartdate;
	//免费试用结束日期
	private Timestamp tryenddate;
	//免费试用天数
	private Integer trydays;
	//免费试用类型，0：订阅之日起；1：自定义时间
	private Integer trytype;
	//扣费类型，1：订购生效次月；2：订购生效当天;3: 订购生效当月
	private Integer buckletype;
	//扣费时间
	private Integer buckledate;
	//欠费补扣最大次数  给个默认值0，避免其他地方计算时候出错
	private Integer buckupmaxtimer=0;
	//欠费补扣间隔天数
	private Integer buckupintervalday;
	//套餐编码，对应套餐表中的套餐编码
	private String  taocancode;
	//备注
    private String comments;
	//企业编码
	private Long corpcode;
	  //创建时间
	private Timestamp createtime;
	  //修改时间
	private Timestamp updatetime; 
	//操作员机构ID
	private Long depid;
	//操作员ID
	 private Long userid;

	 
	public Long getRuleid() {
		return ruleid;
	}
	public void setRuleid(Long ruleid) {
		this.ruleid = ruleid;
	}
	public Timestamp getTrystartdate() {
		return trystartdate;
	}
	public void setTrystartdate(Timestamp trystartdate) {
		this.trystartdate = trystartdate;
	}
	public Timestamp getTryenddate() {
		return tryenddate;
	}
	public void setTryenddate(Timestamp tryenddate) {
		this.tryenddate = tryenddate;
	}
	public Integer getTrydays() {
		return trydays;
	}
	public void setTrydays(Integer trydays) {
		this.trydays = trydays;
	}

	
	public Integer getTrytype() {
		return trytype;
	}
	public void setTrytype(Integer trytype) {
		this.trytype = trytype;
	}
	public Integer getBuckletype() {
		return buckletype;
	}
	public void setBuckletype(Integer buckletype) {
		this.buckletype = buckletype;
	}
	public Integer getBuckledate() {
		return buckledate;
	}
	public void setBuckledate(Integer buckledate) {
		this.buckledate = buckledate;
	}
	public Integer getBuckupmaxtimer() {
		return buckupmaxtimer;
	}
	public void setBuckupmaxtimer(Integer buckupmaxtimer) {
		this.buckupmaxtimer = buckupmaxtimer;
	}
	public Integer getBuckupintervalday() {
		return buckupintervalday;
	}
	public void setBuckupintervalday(Integer buckupintervalday) {
		this.buckupintervalday = buckupintervalday;
	}
	
	public String getTaocancode() {
		return taocancode;
	}
	public void setTaocancode(String taocancode) {
		this.taocancode = taocancode;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Long getCorpcode() {
		return corpcode;
	}
	public void setCorpcode(Long corpcode) {
		this.corpcode = corpcode;
	}
	public Timestamp getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	public Timestamp getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}
	public Long getDepid() {
		return depid;
	}
	public void setDepid(Long depid) {
		this.depid = depid;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	 
	
	 
	 
}
