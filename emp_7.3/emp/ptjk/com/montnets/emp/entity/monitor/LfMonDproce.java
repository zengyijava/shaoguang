package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

public class LfMonDproce
{
	// EMP服务器启动时间，程序类型为EMP，字段不为空
	private Timestamp	starttime;

	// 当前线程数 是 程序类型为EMP，字段不为空
	private Integer		curthreadnum;

	// 当前会话数 程序类型为EMP，字段不为空
	private Integer		curseesionnum;

	// 程序编号
	private Long		proceid;

	// 最后一次更时间
	private Timestamp	updatetime;

	// 数据库连接池最大连接数 程序类型为EMP，字段不为空
	private Integer		maxactivenum;

	// 当前数据库连接数 程序类型为EMP，字段不为空
	private Integer		curconnum;

	// 程序状态 0：正常 1：未启动
	private Integer		procestatus;

	// 最大线程数 程序类型为EMP，字段不为空
	private Integer		maxthreadnum;

	// 虚拟内存最大使用量
	private Integer		vmemusage;

	// 堆内存使用率 程序类型为EMP，字段不为空，百分比格式
	private Integer		heapuse;

	// 物理内存使用量，以M为单位
	private Integer		memusage;

	// CPU使用量，有必要是格式成XX%的字符串后显示
	private Integer		cpuusage;

	private Long		id;

	// 非堆内存使用率
	private Integer		nonheapuse;

	// 最低磁盘剩余量
	private Integer		diskFree;

	// 繁忙线程数 程序类型为EMP，字段不为空
	private Integer		busythreadnum;

	// EMP系统工作时长
	//private String		EmpWorkTime;
	
	//网关编号
	private Long gatewayId;

	//主机名称
	private String hostname;
	
	//程序名称
	private String procename;
	
	//主机id
	private Long hostid;
	
	//告警级别
	private Integer evttype;
	
	//版本号
	private String version;
	
	private Long thresholdflag1;
	
	private Long thresholdflag2;
	
	private Long thresholdflag3;
	
	private Long thresholdflag4;
	
	private Long thresholdflag5;
	
	private Long thresholdflag6;

    private String serverNum;
    
    private Long sendmailflag1; 
    
    private Long sendmailflag2; 
    
    private Long sendmailflag3; 
    
    private Long sendmailflag4; 
    
    private Long sendmailflag5;
    
    private Long sendmailflag6; 
    
	// 数据库连接状态 0：正常；1：断开
	private Integer dbconnectstate;
	
	//程序类型
	private Integer procetype;
	
	//数据库当前入库时间
	private Timestamp dbservtime;

	public Timestamp getStarttime() {
		return starttime;
	}

	public void setStarttime(Timestamp starttime) {
		this.starttime = starttime;
	}

	public Integer getCurthreadnum() {
		return curthreadnum;
	}

	public void setCurthreadnum(Integer curthreadnum) {
		this.curthreadnum = curthreadnum;
	}

	public Integer getCurseesionnum() {
		return curseesionnum;
	}

	public void setCurseesionnum(Integer curseesionnum) {
		this.curseesionnum = curseesionnum;
	}

	public Long getProceid() {
		return proceid;
	}

	public void setProceid(Long proceid) {
		this.proceid = proceid;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public Integer getMaxactivenum() {
		return maxactivenum;
	}

	public void setMaxactivenum(Integer maxactivenum) {
		this.maxactivenum = maxactivenum;
	}

	public Integer getCurconnum() {
		return curconnum;
	}

	public void setCurconnum(Integer curconnum) {
		this.curconnum = curconnum;
	}

	public Integer getProcestatus() {
		return procestatus;
	}

	public void setProcestatus(Integer procestatus) {
		this.procestatus = procestatus;
	}

	public Integer getMaxthreadnum() {
		return maxthreadnum;
	}

	public void setMaxthreadnum(Integer maxthreadnum) {
		this.maxthreadnum = maxthreadnum;
	}

	public Integer getVmemusage() {
		return vmemusage;
	}

	public void setVmemusage(Integer vmemusage) {
		this.vmemusage = vmemusage;
	}

	public Integer getHeapuse() {
		return heapuse;
	}

	public void setHeapuse(Integer heapuse) {
		this.heapuse = heapuse;
	}

	public Integer getMemusage() {
		return memusage;
	}

	public void setMemusage(Integer memusage) {
		this.memusage = memusage;
	}

	public Integer getCpuusage() {
		return cpuusage;
	}

	public void setCpuusage(Integer cpuusage) {
		this.cpuusage = cpuusage;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNonheapuse() {
		return nonheapuse;
	}

	public void setNonheapuse(Integer nonheapuse) {
		this.nonheapuse = nonheapuse;
	}

	public Integer getDiskFree() {
		return diskFree;
	}

	public void setDiskFree(Integer diskFree) {
		this.diskFree = diskFree;
	}

	public Integer getBusythreadnum() {
		return busythreadnum;
	}

	public void setBusythreadnum(Integer busythreadnum) {
		this.busythreadnum = busythreadnum;
	}

	public Long getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(Long gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getProcename() {
		return procename;
	}

	public void setProcename(String procename) {
		this.procename = procename;
	}

	public Long getHostid() {
		return hostid;
	}

	public void setHostid(Long hostid) {
		this.hostid = hostid;
	}

	public Integer getEvttype() {
		return evttype;
	}

	public void setEvttype(Integer evttype) {
		this.evttype = evttype;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	

	public Long getThresholdflag1()
	{
		return thresholdflag1;
	}

	public void setThresholdflag1(Long thresholdflag1)
	{
		this.thresholdflag1 = thresholdflag1;
	}

	public Long getThresholdflag2()
	{
		return thresholdflag2;
	}

	public void setThresholdflag2(Long thresholdflag2)
	{
		this.thresholdflag2 = thresholdflag2;
	}

	public Long getThresholdflag3()
	{
		return thresholdflag3;
	}

	public void setThresholdflag3(Long thresholdflag3)
	{
		this.thresholdflag3 = thresholdflag3;
	}

	public Long getThresholdflag4()
	{
		return thresholdflag4;
	}

	public void setThresholdflag4(Long thresholdflag4)
	{
		this.thresholdflag4 = thresholdflag4;
	}

	public Long getThresholdflag5()
	{
		return thresholdflag5;
	}

	public void setThresholdflag5(Long thresholdflag5)
	{
		this.thresholdflag5 = thresholdflag5;
	}

	public String getServerNum()
	{
		return serverNum;
	}

	public void setServerNum(String serverNum)
	{
		this.serverNum = serverNum;
	}

	public Integer getDbconnectstate()
	{
		return dbconnectstate;
	}

	public void setDbconnectstate(Integer dbconnectstate)
	{
		this.dbconnectstate = dbconnectstate;
	}

	public Integer getProcetype()
	{
		return procetype;
	}

	public void setProcetype(Integer procetype)
	{
		this.procetype = procetype;
	}

	public Long getThresholdflag6()
	{
		return thresholdflag6;
	}

	public void setThresholdflag6(Long thresholdflag6)
	{
		this.thresholdflag6 = thresholdflag6;
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

	public Timestamp getDbservtime()
	{
		return dbservtime;
	}

	public void setDbservtime(Timestamp dbservtime)
	{
		this.dbservtime = dbservtime;
	}

	
	
}
