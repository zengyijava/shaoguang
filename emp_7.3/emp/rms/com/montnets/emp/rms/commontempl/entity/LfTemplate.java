/**
 * 
 */
package com.montnets.emp.rms.commontempl.entity;

import java.sql.Timestamp;

/**
 * 
 * @ClassName: LfTemplateVo
 * @Description: TODO
 * @author xuty
 * @date 2018-3-20 下午6:15:30
 * 
 */
public class LfTemplate implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2541531387111354453L;
	private Long tmid;
	private Long userid;
	private String tmname;
	private String tmmsg;
	private Integer dsflag;
	private Integer tmstate;
	private Timestamp addtime;
	private Integer ispass;
	private Integer tmptype;
	private String bizcode;
	private String corpcode;
	private Long sptemplid;
	private Integer auditstatus;
	private Long mmstemplid;
	private Integer tmplstatus;
	private Integer paramcnt;
	private Integer submitstatus;
	private String emptemplid;
	private Integer errcode;
	private String tmcode;
	private Integer degree;
	private Long degreesize;
	private Integer industryid;
	private Integer useid;
	
	//是否是公共模板 0为不是 1为是
	private Integer isPublic;
	//模板使用次数
	private Long usecount;
	
	//参数json串
	private String exljson;
	//模板版本
	private String ver;
	
	public Long getTmid() {
		return tmid;
	}
	public void setTmid(Long tmid) {
		this.tmid = tmid;
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public String getTmname() {
		return tmname;
	}
	public void setTmname(String tmname) {
		this.tmname = tmname;
	}
	public String getTmmsg() {
		return tmmsg;
	}
	public void setTmmsg(String tmmsg) {
		this.tmmsg = tmmsg;
	}
	public Integer getDsflag() {
		return dsflag;
	}
	public void setDsflag(Integer dsflag) {
		this.dsflag = dsflag;
	}
	public Integer getTmstate() {
		return tmstate;
	}
	public void setTmstate(Integer tmstate) {
		this.tmstate = tmstate;
	}
	public Timestamp getAddtime() {
		return addtime;
	}
	public void setAddtime(Timestamp addtime) {
		this.addtime = addtime;
	}
	public Integer getIspass() {
		return ispass;
	}
	public void setIspass(Integer ispass) {
		this.ispass = ispass;
	}
	public Integer getTmptype() {
		return tmptype;
	}
	public void setTmptype(Integer tmptype) {
		this.tmptype = tmptype;
	}
	public String getBizcode() {
		return bizcode;
	}
	public void setBizcode(String bizcode) {
		this.bizcode = bizcode;
	}
	public String getCorpcode() {
		return corpcode;
	}
	public void setCorpcode(String corpcode) {
		this.corpcode = corpcode;
	}
	public Long getSptemplid() {
		return sptemplid;
	}
	public void setSptemplid(Long sptemplid) {
		this.sptemplid = sptemplid;
	}
	public Integer getAuditstatus() {
		return auditstatus;
	}
	public void setAuditstatus(Integer auditstatus) {
		this.auditstatus = auditstatus;
	}
	public Long getMmstemplid() {
		return mmstemplid;
	}
	public void setMmstemplid(Long mmstemplid) {
		this.mmstemplid = mmstemplid;
	}
	public Integer getTmplstatus() {
		return tmplstatus;
	}
	public void setTmplstatus(Integer tmplstatus) {
		this.tmplstatus = tmplstatus;
	}
	public Integer getParamcnt() {
		return paramcnt;
	}
	public void setParamcnt(Integer paramcnt) {
		this.paramcnt = paramcnt;
	}
	public Integer getSubmitstatus() {
		return submitstatus;
	}
	public void setSubmitstatus(Integer submitstatus) {
		this.submitstatus = submitstatus;
	}
	public String getEmptemplid() {
		return emptemplid;
	}
	public void setEmptemplid(String emptemplid) {
		this.emptemplid = emptemplid;
	}
	public Integer getErrcode() {
		return errcode;
	}
	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}
	public String getTmcode() {
		return tmcode;
	}
	public void setTmcode(String tmcode) {
		this.tmcode = tmcode;
	}
	public Integer getDegree() {
		return degree;
	}
	public void setDegree(Integer degree) {
		this.degree = degree;
	}
	public Long getDegreesize() {
		return degreesize;
	}
	public void setDegreesize(Long degreesize) {
		this.degreesize = degreesize;
	}
	public Integer getIndustryid() {
		return industryid;
	}
	public void setIndustryid(Integer industryid) {
		this.industryid = industryid;
	}
	public Integer getUseid() {
		return useid;
	}
	public void setUseid(Integer useid) {
		this.useid = useid;
	}
 

	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}
	public void setUsecount(Long usecount) {
		this.usecount = usecount;
	}
	public Long getUsecount() {
		return usecount;
	}
	
	public void setExljson(String exljson) {
		this.exljson = exljson;
	}
	public String getExljson() {
		return exljson;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}
}
