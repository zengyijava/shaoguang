package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

/**
 * 程序信息实体类
 * 
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 上午10:16:33
 */
public class LfMonSproce
{

	// 当前数据库连接数告警阀值，程序类型为EMP，字段不为空
	private Integer		curconuse;

	// 虚拟内存最大使用量告警阀值
	private Integer		vmemuse;

	// 5000:EMP系统5100：数据库 5200：EMP网关WBS 5300：EMP网关SPGATE 5400：SP账号信息 5500：通道账号信息
	// 5900:其他应用系统
	private Integer		procetype;

	// 自增ID,全局唯一
	private Long		proceid;

	// 监控状态：0：未监控1：监控
	private Integer		monstatus;

	// 程序中文名称
	private String		procename;

	// 当前会话数告警阀值，程序类型为EMP，字段不为空
	private Integer		curseesionuse;

	// 记录更新时间
	private Timestamp	modifytime;

	// 备注
	private String		descr;

	// 告警短信发送手机号
	private String		monphone;
	
	// 告警邮件发送邮箱
	private String		monemail;

	// 堆内存使用率告警阀值，程序类型为EMP，字段不为空，百分比格式
	private Integer		heapuse;

	// 与LF_MON_SHOST表外键关联
	private Long		hostid;

	// 创建时间
	private Timestamp	createtime;

	// 物理内存最大使用量告警阀值
	private Integer		memuse;

	// 硬盘剩余量告警阀值
	private Integer		harddiskspace;

	// 监控客户端采集被监控程序的频率，单位秒
	private Integer		monfreq;

	// CPU最大占用比率告警阀值，格式成XX%的字符串后显示
	private Integer		cpubl;

	// 非堆内存使用率告警阀值，程序类型为EMP，字段不为空，百分比格式
	private Integer		nonheapuse;

	// 程序版本号
	private String		version;

	// 当前线程数告警阀值，程序类型为EMP，字段不为空
	private Integer		curthreaduse;

	// 网关编号
	private Long		gatewayid;

	// 程序使用状态，0:使用 ；1:删除
	private Integer		proceusestatus = 0;
	
	//数据库网络连接监控，0：开启;1：关闭
	private Integer isDbconnect;
	
    // 服务器节点
    private String		servernum = " ";

	public LfMonSproce()
	{
	}

	public Integer getCurconuse()
	{

		return curconuse;
	}

	public void setCurconuse(Integer curconuse)
	{

		this.curconuse = curconuse;

	}

	public Integer getVmemuse()
	{

		return vmemuse;
	}

	public void setVmemuse(Integer vmemuse)
	{

		this.vmemuse = vmemuse;

	}

	public Integer getProcetype()
	{

		return procetype;
	}

	public void setProcetype(Integer procetype)
	{

		this.procetype = procetype;

	}

	public Long getProceid()
	{

		return proceid;
	}

	public void setProceid(Long proceid)
	{

		this.proceid = proceid;

	}

	public Integer getMonstatus()
	{

		return monstatus;
	}

	public void setMonstatus(Integer monstatus)
	{

		this.monstatus = monstatus;

	}

	public String getProcename()
	{

		return procename;
	}

	public void setProcename(String procename)
	{

		this.procename = procename;

	}

	public Integer getCurseesionuse()
	{

		return curseesionuse;
	}

	public void setCurseesionuse(Integer curseesionuse)
	{

		this.curseesionuse = curseesionuse;

	}

	public Timestamp getModifytime()
	{

		return modifytime;
	}

	public void setModifytime(Timestamp modifytime)
	{

		this.modifytime = modifytime;

	}

	public String getDescr()
	{

		return descr;
	}

	public void setDescr(String descr)
	{

		this.descr = descr;

	}

	public String getMonphone()
	{

		return monphone;
	}

	public void setMonphone(String monphone)
	{

		this.monphone = monphone;

	}

	public Integer getHeapuse()
	{

		return heapuse;
	}

	public void setHeapuse(Integer heapuse)
	{

		this.heapuse = heapuse;

	}

	public Long getHostid()
	{

		return hostid;
	}

	public void setHostid(Long hostid)
	{

		this.hostid = hostid;

	}

	public Timestamp getCreatetime()
	{

		return createtime;
	}

	public void setCreatetime(Timestamp createtime)
	{

		this.createtime = createtime;

	}

	public Integer getMemuse()
	{

		return memuse;
	}

	public void setMemuse(Integer memuse)
	{

		this.memuse = memuse;

	}

	public Integer getHarddiskspace()
	{

		return harddiskspace;
	}

	public void setHarddiskspace(Integer harddiskspace)
	{

		this.harddiskspace = harddiskspace;

	}

	public Integer getMonfreq()
	{

		return monfreq;
	}

	public void setMonfreq(Integer monfreq)
	{

		this.monfreq = monfreq;

	}

	public Integer getCpubl()
	{

		return cpubl;
	}

	public void setCpubl(Integer cpubl)
	{

		this.cpubl = cpubl;

	}

	public Integer getNonheapuse()
	{

		return nonheapuse;
	}

	public void setNonheapuse(Integer nonheapuse)
	{

		this.nonheapuse = nonheapuse;

	}

	public String getVersion()
	{

		return version;
	}

	public void setVersion(String version)
	{

		this.version = version;

	}

	public Integer getCurthreaduse()
	{

		return curthreaduse;
	}

	public void setCurthreaduse(Integer curthreaduse)
	{

		this.curthreaduse = curthreaduse;

	}

	public Long getGatewayid()
	{
		return gatewayid;
	}

	public void setGatewayid(Long gatewayid)
	{
		this.gatewayid = gatewayid;
	}

	public Integer getProceusestatus()
	{
		return proceusestatus;
	}

	public void setProceusestatus(Integer proceusestatus)
	{
		this.proceusestatus = proceusestatus;
	}

	public Integer getIsDbconnect()
	{
		return isDbconnect;
	}

	public void setIsDbconnect(Integer isDbconnect)
	{
		this.isDbconnect = isDbconnect;
	}

	public String getServernum()
	{
		return servernum;
	}

	public void setServernum(String servernum)
	{
		this.servernum = servernum;
	}

	public String getMonemail() {
		return monemail;
	}

	public void setMonemail(String monemail) {
		this.monemail = monemail;
	}

}
