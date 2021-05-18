package com.montnets.emp.shorturl.report.entity;

import java.sql.Timestamp;

public class VstDetail {
    //自增id
    private Long id ;
    //平台流水号
    private Long ptmsgid;
    //访问流水号
    private Long vstmsgid;
    //用户名 SP账号
    private String userid;
    //企业编号
    private Integer ecid;
    //手机号
    private String phone;
    //手机号归属地
    private Integer mobilearea;
    //创建时间
    private Timestamp cttm;
    //访问时间
    private Timestamp vsttm;
    //平台编号
    private String ptcode;
    //网关编号
    private Integer wgno;
    //访问源
    private String srcaddress;
    //手机终端信息
    private String xwapprof;
    //浏览器类型
    private String xbrotype;
    //分辨率
    private String drs;
    //访问次数
    private Long times;
    //任务批次号
    private String cutid;
    //短地址
    private String surl;
    //长地址
    private String lurl;
    //http请求头
    private String httpHeader;
    //扩展数据
    private String exdata;
    //源平台信息
    private String srcpt;
    //原访问id
    private Long sid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPtmsgid() {
        return ptmsgid;
    }

    public void setPtmsgid(Long ptmsgid) {
        this.ptmsgid = ptmsgid;
    }

    public Long getVstmsgid() {
        return vstmsgid;
    }

    public void setVstmsgid(Long vstmsgid) {
        this.vstmsgid = vstmsgid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getEcid() {
        return ecid;
    }

    public void setEcid(Integer ecid) {
        this.ecid = ecid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getMobilearea() {
        return mobilearea;
    }

    public void setMobilearea(Integer mobilearea) {
        this.mobilearea = mobilearea;
    }

    public Timestamp getCttm() {
        return cttm;
    }

    public void setCttm(Timestamp cttm) {
        this.cttm = cttm;
    }

    public Timestamp getVsttm() {
        return vsttm;
    }

    public void setVsttm(Timestamp vsttm) {
        this.vsttm = vsttm;
    }

    public String getPtcode() {
        return ptcode;
    }

    public void setPtcode(String ptcode) {
        this.ptcode = ptcode;
    }

    public Integer getWgno() {
        return wgno;
    }

    public void setWgno(Integer wgno) {
        this.wgno = wgno;
    }

    public String getSrcaddress() {
        return srcaddress;
    }

    public void setSrcaddress(String srcaddress) {
        this.srcaddress = srcaddress;
    }

    public String getXwapprof() {
        return xwapprof;
    }

    public void setXwapprof(String xwapprof) {
        this.xwapprof = xwapprof;
    }

    public String getXbrotype() {
        return xbrotype;
    }

    public void setXbrotype(String xbrotype) {
        this.xbrotype = xbrotype;
    }

    public String getDrs() {
        return drs;
    }

    public void setDrs(String drs) {
        this.drs = drs;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public String getCutid() {
        return cutid;
    }

    public void setCutid(String cutid) {
        this.cutid = cutid;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    public String getLurl() {
        return lurl;
    }

    public void setLurl(String lurl) {
        this.lurl = lurl;
    }

    public String getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(String httpHeader) {
        this.httpHeader = httpHeader;
    }

    public String getExdata() {
        return exdata;
    }

    public void setExdata(String exdata) {
        this.exdata = exdata;
    }

    public String getSrcpt() {
        return srcpt;
    }

    public void setSrcpt(String srcpt) {
        this.srcpt = srcpt;
    }

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}
    
    
}
