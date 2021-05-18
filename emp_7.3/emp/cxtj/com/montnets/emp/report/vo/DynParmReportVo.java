package com.montnets.emp.report.vo;

/**
 * 动态参数统计报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:49:13
 * @description
 */
public class DynParmReportVo implements java.io.Serializable{

	private static final long serialVersionUID = -1677890568391649915L;

	public DynParmReportVo(){}
 
	
	private String paramName;//参数含义
	
	private String pa;
	
    //提交总数
    private Long icount;
    //接收成功数
    private Long rsucc;
    //发送失败数
    private Long rfail1;
    //接收失败数
    private Long rfail2;
    //未返数
    private Long rnret;
	
	private String iymd;//发送时间
	
	private String sendTime;//开始时间
	
	private String endTime;//结束时间 

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public Long getRfail2() {
		return rfail2;
	}

	public void setRfail2(Long rfail2) {
		this.rfail2 = rfail2;
	}

	public Long getIcount() {
		return icount;
	}

	public void setIcount(Long icount) {
		this.icount = icount;
	}

	public String getIymd() {
		return iymd;
	}

	public void setIymd(String iymd) {
		this.iymd = iymd;
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

	public String getPa() {
		return pa;
	}

	public void setPa(String pa) {
		this.pa = pa;
	}

    public Long getRsucc() {
        return rsucc;
    }

    public void setRsucc(Long rsucc) {
        this.rsucc = rsucc;
    }

    public Long getRfail1() {
        return rfail1;
    }

    public void setRfail1(Long rfail1) {
        this.rfail1 = rfail1;
    }

    public Long getRnret() {
        return rnret;
    }

    public void setRnret(Long rnret) {
        this.rnret = rnret;
    }
}
