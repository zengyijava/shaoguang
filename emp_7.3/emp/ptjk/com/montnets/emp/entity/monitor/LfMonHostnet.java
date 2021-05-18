/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2016-4-27 下午06:19:07
 */
package com.montnets.emp.entity.monitor;

import java.sql.Timestamp;
/**
 * @功能概要：
 * @项目名称： emp
 * @初创作者： chentingsheng <cts314@163.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-4-27 下午06:19:07
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */



public class LfMonHostnet
{

	private String webname;

	private Integer netstate;

	private Long procenode;

	private Integer monstatus;

	private Integer montype;

	private Timestamp createtime;

	private String ipaddr;

	private Long smsalflag1;

	private String hostname;

	private Long id;

	private String servernum;

	private Long webnode;

	private Long mailalflag1;

	private Timestamp updatetime;

	private Integer evttype;
	
	private Integer procetype;

    //数据库当前入库时间
    private Timestamp dbservtime;
	public LfMonHostnet(){
	} 

	public String getWebname(){

		return webname;
	}

	public void setWebname(String webname){

		this.webname= webname;

	}

	public Integer getNetstate(){

		return netstate;
	}

	public void setNetstate(Integer netstate){

		this.netstate= netstate;

	}

	public Long getProcenode(){

		return procenode;
	}

	public void setProcenode(Long procenode){

		this.procenode= procenode;

	}

	public Integer getMonstatus(){

		return monstatus;
	}

	public void setMonstatus(Integer monstatus){

		this.monstatus= monstatus;

	}

	public Integer getMontype(){

		return montype;
	}

	public void setMontype(Integer montype){

		this.montype= montype;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public String getIpaddr(){

		return ipaddr;
	}

	public void setIpaddr(String ipaddr){

		this.ipaddr= ipaddr;

	}

	public Long getSmsalflag1(){

		return smsalflag1;
	}

	public void setSmsalflag1(Long smsalflag1){

		this.smsalflag1= smsalflag1;

	}

	public String getHostname(){

		return hostname;
	}

	public void setHostname(String hostname){

		this.hostname= hostname;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public String getServernum(){

		return servernum;
	}

	public void setServernum(String servernum){

		this.servernum= servernum;

	}

	public Long getWebnode(){

		return webnode;
	}

	public void setWebnode(Long webnode){

		this.webnode= webnode;

	}

	public Long getMailalflag1()
	{
		return mailalflag1;
	}

	public void setMailalflag1(Long mailalflag1)
	{
		this.mailalflag1 = mailalflag1;
	}

	public Timestamp getUpdatetime(){

		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime){

		this.updatetime= updatetime;

	}

	public Integer getEvttype(){

		return evttype;
	}

	public void setEvttype(Integer evttype){

		this.evttype= evttype;

	}

	public Integer getProcetype()
	{
		return procetype;
	}

	public void setProcetype(Integer procetype)
	{
		this.procetype = procetype;
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

