package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;

public class LfMonDbstate
{
	//修改操作状态 0：正常；1：失败
	private Integer modiopr;
	//监控状态 0：未监控1：监控
	private Integer monstatus;
	//程序类型 5000:EMP系统  5200：EMP网关WBS 5300：EMP网关SPGATE
	private Integer procetype;
	// 记录创建时间
	private Timestamp createtime;
	//删除操作状态 0：正常；1：失败
	private Integer delopr;
	//程序与连接状态 0：正常；1：断开
	private Integer dbconnectstate;
	//数据库ID
	private Long dbid;
	//程序名称
	private String procename;
	//邮件告警标识
	private Long mailalflag1;
	
	private Long mailalflag2;
	
	private Long mailalflag3;
	
	private Long mailalflag4;
	
	private Long mailalflag5;

	private Long id;
	//查询操作状态0：正常；1：失败
	private Integer dispopr;
	//告警级别
	private Integer evttype;
	//程序节点
	private Long procenode;
	//短信告警标识
	private Long smsalflag4;

	private Long smsalflag5;

	private Long smsalflag1;

	private Long smsalflag2;

	private Long smsalflag3;

	//新增操作状态0：正常；1：失败
	private Integer addopr;
	// 更新时间
	private Timestamp updatetime;
	//数据库名称
	private String dbname;
	//数据分析服务器节点
	private String serverNum;
	//新增操作描述
	private String dbaddoprdes;
	//删除操作描述
	private String dbdeloprdes;
	//修改操作描述
	private String dbmodioprdes;
	//查询操作描述
	private String dbdispoprdes;
    //数据库当前入库时间
    private Timestamp dbservtime;
	public LfMonDbstate(){
	} 

	public Integer getModiopr(){

		return modiopr;
	}

	public void setModiopr(Integer modiopr){

		this.modiopr= modiopr;

	}

	public Integer getMonstatus(){

		return monstatus;
	}

	public void setMonstatus(Integer monstatus){

		this.monstatus= monstatus;

	}

	public Integer getProcetype(){

		return procetype;
	}

	public void setProcetype(Integer procetype){

		this.procetype= procetype;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public Integer getDelopr(){

		return delopr;
	}

	public void setDelopr(Integer delopr){

		this.delopr= delopr;

	}

	public Integer getDbconnectstate(){

		return dbconnectstate;
	}

	public void setDbconnectstate(Integer dbconnectstate){

		this.dbconnectstate= dbconnectstate;

	}

	public Long getDbid(){

		return dbid;
	}

	public void setDbid(Long dbid){

		this.dbid= dbid;

	}

	public Long getMailalflag3(){

		return mailalflag3;
	}

	public void setMailalflag3(Long mailalflag3){

		this.mailalflag3= mailalflag3;

	}

	public Long getMailalflag2(){

		return mailalflag2;
	}

	public void setMailalflag2(Long mailalflag2){

		this.mailalflag2= mailalflag2;

	}

	public String getProcename(){

		return procename;
	}

	public void setProcename(String procename){

		this.procename= procename;

	}

	public Long getMailalflag5(){

		return mailalflag5;
	}

	public void setMailalflag5(Long mailalflag5){

		this.mailalflag5= mailalflag5;

	}

	public Long getMailalflag4(){

		return mailalflag4;
	}

	public void setMailalflag4(Long mailalflag4){

		this.mailalflag4= mailalflag4;

	}

	public Long getMailalflag1(){

		return mailalflag1;
	}

	public void setMailalflag1(Long mailalflag1){

		this.mailalflag1= mailalflag1;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public Integer getDispopr(){

		return dispopr;
	}

	public void setDispopr(Integer dispopr){

		this.dispopr= dispopr;

	}

	public Integer getEvttype(){

		return evttype;
	}

	public void setEvttype(Integer evttype){

		this.evttype= evttype;

	}

	public Long getProcenode(){

		return procenode;
	}

	public void setProcenode(Long procenode){

		this.procenode= procenode;

	}

	public Long getSmsalflag4(){

		return smsalflag4;
	}

	public void setSmsalflag4(Long smsalflag4){

		this.smsalflag4= smsalflag4;

	}

	public Long getSmsalflag5(){

		return smsalflag5;
	}

	public void setSmsalflag5(Long smsalflag5){

		this.smsalflag5= smsalflag5;

	}

	public Long getSmsalflag1(){

		return smsalflag1;
	}

	public void setSmsalflag1(Long smsalflag1){

		this.smsalflag1= smsalflag1;

	}

	public Long getSmsalflag2(){

		return smsalflag2;
	}

	public void setSmsalflag2(Long smsalflag2){

		this.smsalflag2= smsalflag2;

	}

	public Long getSmsalflag3(){

		return smsalflag3;
	}

	public void setSmsalflag3(Long smsalflag3){

		this.smsalflag3= smsalflag3;

	}

	public Integer getAddopr(){

		return addopr;
	}

	public void setAddopr(Integer addopr){

		this.addopr= addopr;

	}

	public Timestamp getUpdatetime(){

		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime){

		this.updatetime= updatetime;

	}

	public String getDbname(){

		return dbname;
	}

	public void setDbname(String dbname){

		this.dbname= dbname;

	}

	public String getServerNum()
	{
		return serverNum;
	}

	public void setServerNum(String serverNum)
	{
		this.serverNum = serverNum;
	}

	public String getDbaddoprdes()
	{
		return dbaddoprdes;
	}

	public void setDbaddoprdes(String dbaddoprdes)
	{
		this.dbaddoprdes = dbaddoprdes;
	}

	public String getDbdeloprdes()
	{
		return dbdeloprdes;
	}

	public void setDbdeloprdes(String dbdeloprdes)
	{
		this.dbdeloprdes = dbdeloprdes;
	}

	public String getDbmodioprdes()
	{
		return dbmodioprdes;
	}

	public void setDbmodioprdes(String dbmodioprdes)
	{
		this.dbmodioprdes = dbmodioprdes;
	}

	public String getDbdispoprdes()
	{
		return dbdispoprdes;
	}

	public void setDbdispoprdes(String dbdispoprdes)
	{
		this.dbdispoprdes = dbdispoprdes;
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
