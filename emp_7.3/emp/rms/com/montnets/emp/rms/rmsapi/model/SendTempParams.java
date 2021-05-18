package com.montnets.emp.rms.rmsapi.model;

import java.io.Serializable;

/**
 * 发送模板参数实体类
 * @author chenly
 *
 */
public class SendTempParams implements Serializable
{
   	private static final long serialVersionUID = -2165287432154478789L;
    
    //用户账号
  	private String userid;
  	//TaskId
  	private Long taskid;
  	//p1字段 存放操作员编码
	private String param1;
  	//用户密码
  	private String pwd;
	//时间戳
  	private String timestamp;
    //用户唯一标识
  	private String apikey;
    //短信接收的手机号
	private String mobile;
	//模板id
	private String tmplid;
	//富信主题，最长20个字，用于发送明文短链方式发送使用，必填
	private String title;
	//变量名和变量值
	private String content;
	//业务类型
	private String svrtype;
	//有效时长
	private Integer validtm;
	//扩展号
	private String exno;
	//用户自定义流水号
	private String custid;
	//自定义扩展数据
	private String exdata;
	//手机展现方式,1 卡片(需要手机支持),2 彩信方式,3 明文短链(手动点击链接H5页面),0-1-2-3,3-0-0-0
	private String showay;
	//下载方式,1 使用数据网络下载,2 使用wifi网络下载,3 数据网络、wifi都可以，wifi优先
	private Integer dldway;
	//下载使用的网络(当dldway支持数据网络下载时有效),1 本网下载,2 本卡下载
	private Integer dldnet;
	//是否免流量发送,0 ,1 免流,2 不免流
	private Integer isfree;
	//控制信息内容在手机上显示的时间，时间格式yyyyMMddHH24MISS
	private String showtime;
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getMobile()
	{
		return mobile;
	}
	
	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
	
	public String getTmplid()
	{
		return tmplid;
	}
	
	public void setTmplid(String tmplid)
	{
		this.tmplid = tmplid;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public void setContent(String content)
	{
		this.content=content;
	}
			
	public String getSvrtype()
	{
		return svrtype;
	}
	
	public void setSvrtype(String svrtype)
	{
		this.svrtype = svrtype;
	}
	
	public String getExno()
	{
		return exno;
	}
	
	public void setExno(String exno)
	{
		this.exno = exno;
	}
	
	public String getCustid()
	{
		return custid;
	}
	
	public void setCustid(String custid)
	{
		this.custid = custid;
	}
	
	public String getExdata()
	{
		return exdata;
	}
	
	public void setExdata(String exdata)
	{
		this.exdata = exdata;
	}

	public Integer getValidtm() {
		return validtm;
	}

	public void setValidtm(Integer validtm) {
		this.validtm = validtm;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getShoway() {
		return showay;
	}

	public void setShoway(String showay) {
		this.showay = showay;
	}

	public Integer getDldway() {
		return dldway;
	}

	public void setDldway(Integer dldway) {
		this.dldway = dldway;
	}

	public Integer getDldnet() {
		return dldnet;
	}

	public void setDldnet(Integer dldnet) {
		this.dldnet = dldnet;
	}

	public Integer getIsfree() {
		return isfree;
	}

	public void setIsfree(Integer isfree) {
		this.isfree = isfree;
	}

	public String getShowtime() {
		return showtime;
	}

	public void setShowtime(String showtime) {
		this.showtime = showtime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getTaskid() {
		return taskid;
	}

	public void setTaskid(Long taskid) {
		this.taskid = taskid;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}
}
