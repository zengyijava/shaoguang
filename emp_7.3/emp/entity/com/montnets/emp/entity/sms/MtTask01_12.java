package com.montnets.emp.entity.sms;

import java.sql.Timestamp;

 
public class MtTask01_12 implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1355321740907052075L;
 	private Long id;
	private Long mday;
	private String userid;
	private String spgate;
	private String cpno;
	private String phone;
	private Long spmsgid;
	private Long retflag;
	private Long feeflag;
	private Long pknumber;
	private Long pktotal;
	private Long sendstatus;
	private Long sendflag;
	private Long recvflag;
	private String donedate;
	private String errorCode;
	private Long sendlevel;
	private Long sendtype;
	private Long unicom;
	private Timestamp sendtime;
	private Timestamp recvtime;
	private String message;
	private Long taskid;
	private Long ecid;
	private Long ptmsgid;

    private String usermsgid;
    private String custid;
    private String error_code2;
    private String tmplid;
    private String downtm;
    private String chgrade;


	//任务批次号
    private Long batchID;
    
	public Long getBatchID()
	{
		return batchID;
	}


	public void setBatchID(Long batchID)
	{
		this.batchID = batchID;
	}
	
	public MtTask01_12(){}
	
	 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMday() {
		return mday;
	}

	public void setMday(Long mday) {
		this.mday = mday;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getSpgate() {
		return spgate;
	}

	public void setSpgate(String spgate) {
		this.spgate = spgate;
	}

	public String getCpno() {
		return cpno;
	}

	public void setCpno(String cpno) {
		this.cpno = cpno;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getSpmsgid() {
		return spmsgid;
	}

	public void setSpmsgid(Long spmsgid) {
		this.spmsgid = spmsgid;
	}

	public Long getRetflag() {
		return retflag;
	}

	public void setRetflag(Long retflag) {
		this.retflag = retflag;
	}

	public Long getFeeflag() {
		return feeflag;
	}

	public void setFeeflag(Long feeflag) {
		this.feeflag = feeflag;
	}

	public Long getPknumber() {
		return pknumber;
	}

	public void setPknumber(Long pknumber) {
		this.pknumber = pknumber;
	}

	public Long getPktotal() {
		return pktotal;
	}

	public void setPktotal(Long pktotal) {
		this.pktotal = pktotal;
	}

	public Long getSendstatus() {
		return sendstatus;
	}

	public void setSendstatus(Long sendstatus) {
		this.sendstatus = sendstatus;
	}

	public Long getSendflag() {
		return sendflag;
	}

	public void setSendflag(Long sendflag) {
		this.sendflag = sendflag;
	}

	public Long getRecvflag() {
		return recvflag;
	}

	public void setRecvflag(Long recvflag) {
		this.recvflag = recvflag;
	}

	public String getDonedate() {
		return donedate;
	}

	public void setDonedate(String donedate) {
		this.donedate = donedate;
	}

 
	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	public Long getSendlevel() {
		return sendlevel;
	}

	public void setSendlevel(Long sendlevel) {
		this.sendlevel = sendlevel;
	}

	public Long getSendtype() {
		return sendtype;
	}

	public void setSendtype(Long sendtype) {
		this.sendtype = sendtype;
	}

	public Long getUnicom() {
		return unicom;
	}

	public void setUnicom(Long unicom) {
		this.unicom = unicom;
	}

	public Timestamp getSendtime() {
		return sendtime;
	}

	public void setSendtime(Timestamp sendtime) {
		this.sendtime = sendtime;
	}

	public Timestamp getRecvtime() {
		return recvtime;
	}

	public void setRecvtime(Timestamp recvtime) {
		this.recvtime = recvtime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getTaskid() {
		return taskid;
	}

	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}

	public Long getEcid() {
		return ecid;
	}

	public void setEcid(Long ecid) {
		this.ecid = ecid;
	}

	public Long getPtmsgid() {
		return ptmsgid;
	}

	public void setPtmsgid(Long ptmsgid) {
		this.ptmsgid = ptmsgid;
	}

    public String getUsermsgid() {
        return usermsgid;
    }

    public void setUsermsgid(String usermsgid) {
        this.usermsgid = usermsgid;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getError_code2() {
        return error_code2;
    }

    public void setError_code2(String error_code2) {
        this.error_code2 = error_code2;
    }

    public String getTmplid() {
        return tmplid;
    }

    public void setTmplid(String tmplid) {
        this.tmplid = tmplid;
    }

    public String getDowntm() {
        return downtm;
    }

    public void setDowntm(String downtm) {
        this.downtm = downtm;
    }

    public String getChgrade() {
        return chgrade;
    }

    public void setChgrade(String chgrade) {
        this.chgrade = chgrade;
    }
}
