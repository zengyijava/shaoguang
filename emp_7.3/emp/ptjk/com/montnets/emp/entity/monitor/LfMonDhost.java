package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

/**
 * 主机动态信息实体类
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 上午10:02:01
 */
public class LfMonDhost
{
    private Integer id;
    private Integer memuse;

    private Integer cpuusage;

    private Long thresholdFlag1;

    private Long thresholdFlag2;

    private Long thresholdFlag3;

    private Long thresholdFlag4;

    private Long thresholdFlag5;

    private Long thresholdFlag6;

    private Integer vmemusage;

    private Integer vmemuse;

    private Integer diskfreespace;

    private String hostName;

    private Integer memusage;

    private Integer hoststatus;

    private Integer diskspace;

    private Long hostid;

    private Integer processcnt;

    private String adapter1;

    private Timestamp updatetime;

    private Integer evtType;

    private String serverNum;

    private Long sendmailflag1; 
    
    private Long sendmailflag2; 
    
    private Long sendmailflag3; 
    
    private Long sendmailflag4; 
    
    private Long sendmailflag5;
    
    private Long sendmailflag6; 
    
    private String ipAddr;
    //数据库当前入库时间
    private Timestamp dbservtime;
    
    public LfMonDhost(){
    }

    public Integer getMemuse(){

        return memuse;
    }

    public void setMemuse(Integer memuse){

        this.memuse= memuse;

    }

    public Integer getCpuusage(){

        return cpuusage;
    }

    public void setCpuusage(Integer cpuusage){

        this.cpuusage= cpuusage;

    }

    public Integer getVmemusage(){

        return vmemusage;
    }

    public void setVmemusage(Integer vmemusage){

        this.vmemusage= vmemusage;

    }

    public Integer getVmemuse(){

        return vmemuse;
    }

    public void setVmemuse(Integer vmemuse){

        this.vmemuse= vmemuse;

    }

    public Integer getDiskfreespace(){

        return diskfreespace;
    }

    public void setDiskfreespace(Integer diskfreespace){

        this.diskfreespace= diskfreespace;

    }

    public Integer getMemusage(){

        return memusage;
    }

    public void setMemusage(Integer memusage){

        this.memusage= memusage;

    }

    public Integer getHoststatus(){

        return hoststatus;
    }

    public void setHoststatus(Integer hoststatus){

        this.hoststatus= hoststatus;

    }

    public Integer getDiskspace(){

        return diskspace;
    }

    public void setDiskspace(Integer diskspace){

        this.diskspace= diskspace;

    }

    public Integer getProcesscnt(){

        return processcnt;
    }

    public void setProcesscnt(Integer processcnt){

        this.processcnt= processcnt;

    }

    public String getAdapter1(){

        return adapter1;
    }

    public void setAdapter1(String adapter1){

        this.adapter1= adapter1;

    }

    public Timestamp getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getEvtType() {
        return evtType;
    }

    public void setEvtType(Integer evtType) {
        this.evtType = evtType;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Long getHostid() {
        return hostid;
    }

    public void setHostid(Long hostid) {
        this.hostid = hostid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


	public Long getThresholdFlag1()
	{
		return thresholdFlag1;
	}

	public void setThresholdFlag1(Long thresholdFlag1)
	{
		this.thresholdFlag1 = thresholdFlag1;
	}

	public Long getThresholdFlag2()
	{
		return thresholdFlag2;
	}

	public void setThresholdFlag2(Long thresholdFlag2)
	{
		this.thresholdFlag2 = thresholdFlag2;
	}

	public Long getThresholdFlag3()
	{
		return thresholdFlag3;
	}

	public void setThresholdFlag3(Long thresholdFlag3)
	{
		this.thresholdFlag3 = thresholdFlag3;
	}

	public Long getThresholdFlag4()
	{
		return thresholdFlag4;
	}

	public void setThresholdFlag4(Long thresholdFlag4)
	{
		this.thresholdFlag4 = thresholdFlag4;
	}

	public Long getThresholdFlag5()
	{
		return thresholdFlag5;
	}

	public void setThresholdFlag5(Long thresholdFlag5)
	{
		this.thresholdFlag5 = thresholdFlag5;
	}

	public Long getThresholdFlag6()
	{
		return thresholdFlag6;
	}

	public void setThresholdFlag6(Long thresholdFlag6)
	{
		this.thresholdFlag6 = thresholdFlag6;
	}

	public String getServerNum()
	{
		return serverNum;
	}

	public void setServerNum(String serverNum)
	{
		this.serverNum = serverNum;
	}

	public Long getSendmailflag1() {
		return sendmailflag1;
	}

	public void setSendmailflag1(Long sendmailflag1) {
		this.sendmailflag1 = sendmailflag1;
	}

	public Long getSendmailflag2() {
		return sendmailflag2;
	}

	public void setSendmailflag2(Long sendmailflag2) {
		this.sendmailflag2 = sendmailflag2;
	}

	public Long getSendmailflag3() {
		return sendmailflag3;
	}

	public void setSendmailflag3(Long sendmailflag3) {
		this.sendmailflag3 = sendmailflag3;
	}

	public Long getSendmailflag4() {
		return sendmailflag4;
	}

	public void setSendmailflag4(Long sendmailflag4) {
		this.sendmailflag4 = sendmailflag4;
	}

	public Long getSendmailflag5() {
		return sendmailflag5;
	}

	public void setSendmailflag5(Long sendmailflag5) {
		this.sendmailflag5 = sendmailflag5;
	}

	public Long getSendmailflag6() {
		return sendmailflag6;
	}

	public void setSendmailflag6(Long sendmailflag6) {
		this.sendmailflag6 = sendmailflag6;
	}

	public String getIpAddr()
	{
		return ipAddr;
	}

	public void setIpAddr(String ipAddr)
	{
		this.ipAddr = ipAddr;
	}

	public Timestamp getDbservtime()
	{
		return dbservtime;
	}

	public void setDbservtime(Timestamp dbservtime)
	{
		this.dbservtime = dbservtime;
	}


}

