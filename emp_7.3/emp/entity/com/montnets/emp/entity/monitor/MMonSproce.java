package com.montnets.emp.entity.monitor;


import java.sql.Timestamp;

/**
 * 监控程序基本信息
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-26 下午03:49:02
 */
public class MMonSproce
{

	//程序类型
	private Integer procetype;

	//程序ID
	private Integer proceid;

	//监控状态
	private Integer monstatus;

	//平台编号
	private String ptcode;

	//程序名称
	private String procename;

	//修改时间
	private Timestamp modifytime;

	//程序文件路径
	private String filepath;

	//程序升级时间
	private Timestamp upgradetime;

	//程序对应备份主机
	private Integer bakhostid;

	//备份文件路径
	private String bakfilepath;

	//主机ID
	private Integer hostid;

	//记录创建时间
	private Timestamp createtime;

	//数据采集频率
	private Integer monfreq;

	//备注
	private String descr;

	//程序版本号
	private String version;

	public MMonSproce(){
	} 

	public Integer getProcetype(){

		return procetype;
	}

	public void setProcetype(Integer procetype){

		this.procetype= procetype;

	}

	public Integer getProceid(){

		return proceid;
	}

	public void setProceid(Integer proceid){

		this.proceid= proceid;

	}

	public Integer getMonstatus(){

		return monstatus;
	}

	public void setMonstatus(Integer monstatus){

		this.monstatus= monstatus;

	}

	public String getPtcode(){

		return ptcode;
	}

	public void setPtcode(String ptcode){

		this.ptcode= ptcode;

	}

	public String getProcename(){

		return procename;
	}

	public void setProcename(String procename){

		this.procename= procename;

	}

	public Timestamp getModifytime(){

		return modifytime;
	}

	public void setModifytime(Timestamp modifytime){

		this.modifytime= modifytime;

	}

	public String getFilepath(){

		return filepath;
	}

	public void setFilepath(String filepath){

		this.filepath= filepath;

	}

	public Timestamp getUpgradetime(){

		return upgradetime;
	}

	public void setUpgradetime(Timestamp upgradetime){

		this.upgradetime= upgradetime;

	}

	public Integer getBakhostid(){

		return bakhostid;
	}

	public void setBakhostid(Integer bakhostid){

		this.bakhostid= bakhostid;

	}

	public String getBakfilepath(){

		return bakfilepath;
	}

	public void setBakfilepath(String bakfilepath){

		this.bakfilepath= bakfilepath;

	}

	public Integer getHostid(){

		return hostid;
	}

	public void setHostid(Integer hostid){

		this.hostid= hostid;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public Integer getMonfreq(){

		return monfreq;
	}

	public void setMonfreq(Integer monfreq){

		this.monfreq= monfreq;

	}

	public String getDescr(){

		return descr;
	}

	public void setDescr(String descr){

		this.descr= descr;

	}

	public String getVersion(){

		return version;
	}

	public void setVersion(String version){

		this.version= version;

	}

}

					