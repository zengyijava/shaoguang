package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;
/**
 * 主机信息表
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-30 上午09:31:56
 */
public class LfMonShost
{

	//虚拟内存占用率告警阀值，显示格式为XX%
	private Integer vmemusage;

	//监控状态：0：未监控1：监控
	private Integer monstatus;

	//最后一次更新时间
	private Timestamp modifytime;

	//CPU占用时间告警阀值
	private Integer cpusj;

	//主机备注信息
	private String descr;

	//电脑主机名称
	private String hostname;

	//告警短信发送手机号
	private String monphone;

	//告警邮件发送邮箱
	private String monemail;
	
	//磁盘空间占用率告警阀值，显示格式为XX%
	private Integer diskfreespace;

	//物理内存占用率告警阀值，显示格式为XX%
	private Integer memusage;

	//CPU使用量告警阀值，显示格式为XX% 
	private Integer cpuusage;

	//主键，自增ID
	private Long hostid;

	//记录创建时间
	private Timestamp createtime;

	//进程总数告警阀值
	private Integer processcnt;

	//1：应用服务器2：数据库服务器
	private Integer hosttype;

	//逗号分隔：Ip，子网掩码，网关，MAC地址
	private String adapter2;

	//逗号分隔：Ip，子网掩码，网关，MAC地址
	private String adapter1;

	//CPU占用比率告警阀值
	private Integer cpubl;

	//外网IP(可输入多个以英文逗号分隔)
	private String oupip;

	//数据采集频率  采集被监控程序的频率，单位秒 
	private Integer monfreq;
	
	//内存
	private Integer hostmem;
	
	//硬盘
	private Integer hosthd;
	
	//CPU信息
	private String hostcpu;
	
	//操作系统
	private String opersys;
	
	// 主机使用状态，0:使用 ；1:删除
	private Integer hostusestatus = 0;
	
	public String getHostcpu()
	{
		return hostcpu;
	}

	public void setHostcpu(String hostcpu)
	{
		this.hostcpu = hostcpu;
	}

	public String getOpersys()
	{
		return opersys;
	}

	public void setOpersys(String opersys)
	{
		this.opersys = opersys;
	}

	public Integer getHostmem()
	{
		return hostmem;
	}

	public void setHostmem(Integer hostmem)
	{
		this.hostmem = hostmem;
	}

	public Integer getHosthd()
	{
		return hosthd;
	}

	public void setHosthd(Integer hosthd)
	{
		this.hosthd = hosthd;
	}

	public Integer getMonfreq()
	{
		return monfreq;
	}

	public void setMonfreq(Integer monfreq)
	{
		this.monfreq = monfreq;
	}

	public LfMonShost(){
	} 

	public Integer getVmemusage(){

		return vmemusage;
	}

	public void setVmemusage(Integer vmemusage){

		this.vmemusage= vmemusage;

	}

	public Integer getMonstatus(){

		return monstatus;
	}

	public void setMonstatus(Integer monstatus){

		this.monstatus= monstatus;

	}

	public Timestamp getModifytime(){

		return modifytime;
	}

	public void setModifytime(Timestamp modifytime){

		this.modifytime= modifytime;

	}

	public Integer getCpusj(){

		return cpusj;
	}

	public void setCpusj(Integer cpusj){

		this.cpusj= cpusj;

	}

	public String getDescr(){

		return descr;
	}

	public void setDescr(String descr){

		this.descr= descr;

	}

	public String getHostname(){

		return hostname;
	}

	public void setHostname(String hostname){

		this.hostname= hostname;

	}

	public String getMonphone(){

		return monphone;
	}

	public void setMonphone(String monphone){

		this.monphone= monphone;

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

	public Integer getCpuusage(){

		return cpuusage;
	}

	public void setCpuusage(Integer cpuusage){

		this.cpuusage= cpuusage;

	}

	public Long getHostid(){

		return hostid;
	}

	public void setHostid(Long hostid){

		this.hostid= hostid;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public Integer getProcesscnt(){

		return processcnt;
	}

	public void setProcesscnt(Integer processcnt){

		this.processcnt= processcnt;

	}

	public Integer getHosttype(){

		return hosttype;
	}

	public void setHosttype(Integer hosttype){

		this.hosttype= hosttype;

	}

	public String getAdapter2(){

		return adapter2;
	}

	public void setAdapter2(String adapter2){

		this.adapter2= adapter2;

	}

	public String getAdapter1(){

		return adapter1;
	}

	public void setAdapter1(String adapter1){

		this.adapter1= adapter1;

	}

	public Integer getCpubl(){

		return cpubl;
	}

	public void setCpubl(Integer cpubl){

		this.cpubl= cpubl;

	}

	public String getOupip(){

		return oupip;
	}

	public void setOupip(String oupip){

		this.oupip= oupip;

	}

	public Integer getHostusestatus()
	{
		return hostusestatus;
	}

	public void setHostusestatus(Integer hostusestatus)
	{
		this.hostusestatus = hostusestatus;
	}

	public String getMonemail() {
		return monemail;
	}

	public void setMonemail(String monemail) {
		this.monemail = monemail;
	}

}
