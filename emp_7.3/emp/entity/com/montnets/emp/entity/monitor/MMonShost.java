package com.montnets.emp.entity.monitor;


import java.sql.Timestamp;

/**
 * 主机基本信息
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-26 上午11:50:45
 */
public class MMonShost
{

	//机柜编号,外键关联机柜表（M_MON_CABINET）
	private Integer cabinetid;

	//主机编号,唯一索引
	private String hostcode;

	//监控客户端登录验证密码
	private String pwd;

	//硬盘大小
	private Integer hosthd;

	//主机内存.唯一索引
	private Integer hostmem;

	//主机ID 主键，自增ID
	private Integer hostid;

	//主机类型1:应用服务器2：数据库服务器
	private Integer hosttype;

	//采集频率
	private Integer monfreq;

	//外网IP(可输入多个以英文逗号分隔)
	private String oupip;

	//主机尺寸,固定几种取值：1U、2U、3U、4U
	private Integer hostsize;

	//快速服务代码
	private String svrcode;

	//主机位置:放在机柜第几U的位置，如主机尺寸为3U,主机位置为2U,
	//则2至5U的位置不能再放置主机
	private Integer hostpos;

	//监控程序个数
	private Integer monnum;

	//使用状态:电脑监控状态：0：未监控 1：监控
	private Integer monstatus;

	//修改时间
	private Timestamp modifytime;

	//备注
	private String descr;

	//主机名称
	private String hostname;

	//Cpu信息
	private String hostcpu;

	//备用电脑
	private Integer bakhostid;

	//服务编号
	private String srvbh;

	//记录创建时间
	private Timestamp createtime;

	//内网第一个ip.逗号分隔：Ip，子网掩码，网关，MAC地址
	private String adapter2;

	//内网第一个ip.逗号分隔：Ip，子网掩码，网关，MAC地址
	private String adapter1;

	//操作系统
	private String opersys;

	//内网第四个ip.逗号分隔：Ip，子网掩码，网关，MAC地址
	private String adapter4;

	//内网第三个ip.逗号分隔：Ip，子网掩码，网关，MAC地址
	private String adapter3;

	public MMonShost(){
	} 

	public Integer getCabinetid(){

		return cabinetid;
	}

	public void setCabinetid(Integer cabinetid){

		this.cabinetid= cabinetid;

	}

	public String getHostcode(){

		return hostcode;
	}

	public void setHostcode(String hostcode){

		this.hostcode= hostcode;

	}

	public String getPwd(){

		return pwd;
	}

	public void setPwd(String pwd){

		this.pwd= pwd;

	}

	public Integer getHosthd(){

		return hosthd;
	}

	public void setHosthd(Integer hosthd){

		this.hosthd= hosthd;

	}

	public Integer getHostmem(){

		return hostmem;
	}

	public void setHostmem(Integer hostmem){

		this.hostmem= hostmem;

	}

	public Integer getHostid(){

		return hostid;
	}

	public void setHostid(Integer hostid){

		this.hostid= hostid;

	}

	public Integer getHosttype(){

		return hosttype;
	}

	public void setHosttype(Integer hosttype){

		this.hosttype= hosttype;

	}

	public Integer getMonfreq(){

		return monfreq;
	}

	public void setMonfreq(Integer monfreq){

		this.monfreq= monfreq;

	}

	public String getOupip(){

		return oupip;
	}

	public void setOupip(String oupip){

		this.oupip= oupip;

	}

	public Integer getHostsize(){

		return hostsize;
	}

	public void setHostsize(Integer hostsize){

		this.hostsize= hostsize;

	}

	public String getSvrcode(){

		return svrcode;
	}

	public void setSvrcode(String svrcode){

		this.svrcode= svrcode;

	}

	public Integer getHostpos(){

		return hostpos;
	}

	public void setHostpos(Integer hostpos){

		this.hostpos= hostpos;

	}

	public Integer getMonnum(){

		return monnum;
	}

	public void setMonnum(Integer monnum){

		this.monnum= monnum;

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

	public String getHostcpu(){

		return hostcpu;
	}

	public void setHostcpu(String hostcpu){

		this.hostcpu= hostcpu;

	}

	public Integer getBakhostid(){

		return bakhostid;
	}

	public void setBakhostid(Integer bakhostid){

		this.bakhostid= bakhostid;

	}

	public String getSrvbh(){

		return srvbh;
	}

	public void setSrvbh(String srvbh){

		this.srvbh= srvbh;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

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

	public String getOpersys(){

		return opersys;
	}

	public void setOpersys(String opersys){

		this.opersys= opersys;

	}

	public String getAdapter4(){

		return adapter4;
	}

	public void setAdapter4(String adapter4){

		this.adapter4= adapter4;

	}

	public String getAdapter3(){

		return adapter3;
	}

	public void setAdapter3(String adapter3){

		this.adapter3= adapter3;

	}

}

					