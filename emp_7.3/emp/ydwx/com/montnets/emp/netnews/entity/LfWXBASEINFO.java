package com.montnets.emp.netnews.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Vincent
 * @version 1.0
 * describe 网讯基本信息表（LF_WX_BASEINFO）
 * date 2011.11.5
 * **/

public class LfWXBASEINFO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long ID = 0L;					//自动编号
	private Long NETID = 0L;				//网讯编号
	private String NAME = "";			//网讯名称
	private Long SORT ;				//网讯类型
	private Integer TIMETYPE = 0;			//有效时间。0：永久；1：其他
	private Timestamp TIMEOUT;			//其他：过期时间
	private Integer wxRIGHT = 0;				//访问权限。0：不限；1：所有企业通讯录人员；2：限接受者
	private Timestamp TIMEEND;			//有效截止日期
	private String URL = "";			//网讯地址 
	private Integer STATUS = 0;				//状态。0：起草；1：待审核2、已审核3审核未通过4无需审核
	private String AUDITUSERID = "";	//审核人ID
	private Timestamp AUDITDATE;		//审核日期
	private Integer wxTYPE = 0;				//选择模板。0：模板；1：自定义模板；2：问卷模板
	private String IMAGE = "";			//缩略图地址
	private Long MODIFYID = 0L;			//最后修改用户ID
	private Timestamp MODIFYDATE;		//最后修改时间
	private Long CREATID = 0L;			//创建用户ID
	private Timestamp CREATDATE;		//创建时间
	private String SMS ;
	private Integer wxSHARE = 0 ; 		//是否可以分享网讯
	private String CORPCODE ="";		//企业编号 
	private String dataTableName="";		//互动数据 表名
	private String dynTableName="";		//互动数据 表名
	//模板类型。1：静态模板，生成静态html页面；2：动态模板，可使用业务数据和上行交互，生成jsp动态页面；
	private Integer tempType;
	//动态网讯使用的参数，格式为1,2
	private String params;
	//用户添加的参数，格式为1,2
	private String hasParams;
	
	//运营商审批状态 默认值为0 字段类型为int类型  1.表示审核通过，2表示审核未通过  
	//StaticValue.IS_WX_OPERATOR_REVIEW (网讯是否运营商商审核 0表示运营商不审核，1表示运营商审核)
	private Integer operAppStatus;
	//运营商审批意见
	private String  operAppNote;
	
	
	public Integer getOperAppStatus()
	{
		return operAppStatus;
	}
	public void setOperAppStatus(Integer operAppStatus)
	{
		this.operAppStatus = operAppStatus;
	}
	
	
	public String getOperAppNote()
	{
		return operAppNote;
	}
	public void setOperAppNote(String operAppNote)
	{
		this.operAppNote = operAppNote;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public Long getSORT() {
		return SORT;
	}
	public void setSORT(Long sORT) {
		SORT = sORT;
	}
	public Integer getTIMETYPE() {
		return TIMETYPE;
	}
	public void setTIMETYPE(Integer tIMETYPE) {
		TIMETYPE = tIMETYPE;
	}
	public Timestamp getTIMEOUT() {
		return TIMEOUT;
	}
	public void setTIMEOUT(Timestamp tIMEOUT) {
		TIMEOUT = tIMEOUT;
	}
	public Integer getWxRIGHT() {
		return wxRIGHT;
	}
	public void setWxRIGHT(Integer wxRIGHT) {
		this.wxRIGHT = wxRIGHT;
	}
	public Timestamp getTIMEEND() {
		return TIMEEND;
	}
	public void setTIMEEND(Timestamp tIMEEND) {
		TIMEEND = tIMEEND;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public Integer getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(Integer sTATUS) {
		STATUS = sTATUS;
	}
	public String getAUDITUSERID() {
		return AUDITUSERID;
	}
	public void setAUDITUSERID(String aUDITUSERID) {
		AUDITUSERID = aUDITUSERID;
	}
	public Timestamp getAUDITDATE() {
		return AUDITDATE;
	}
	public void setAUDITDATE(Timestamp aUDITDATE) {
		AUDITDATE = aUDITDATE;
	}
	public Integer getWxTYPE() {
		return wxTYPE;
	}
	public void setWxTYPE(Integer wxTYPE) {
		this.wxTYPE = wxTYPE;
	}
	public String getIMAGE() {
		return IMAGE;
	}
	public void setIMAGE(String iMAGE) {
		IMAGE = iMAGE;
	}
	public Timestamp getMODIFYDATE() {
		return MODIFYDATE;
	}
	public void setMODIFYDATE(Timestamp mODIFYDATE) {
		MODIFYDATE = mODIFYDATE;
	}
	public Timestamp getCREATDATE() {
		return CREATDATE;
	}
	public void setCREATDATE(Timestamp cREATDATE) {
		CREATDATE = cREATDATE;
	}
	public String getSMS() {
		return SMS;
	}
	public void setSMS(String sMS) {
		SMS = sMS;
	}
	public String getCORPCODE() {
		return CORPCODE;
	}
	public void setCORPCODE(String cORPCODE) {
		CORPCODE = cORPCODE;
	}
	public Integer getWxSHARE() {
		return wxSHARE;
	}
	public void setWxSHARE(Integer wxSHARE) {
		this.wxSHARE = wxSHARE;
	}
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public Long getNETID() {
		return NETID;
	}
	public void setNETID(Long nETID) {
		NETID = nETID;
	}
	public Long getMODIFYID() {
		return MODIFYID;
	}
	public void setMODIFYID(Long mODIFYID) {
		MODIFYID = mODIFYID;
	}
	public Long getCREATID() {
		return CREATID;
	}
	public void setCREATID(Long cREATID) {
		CREATID = cREATID;
	}
	public String getDataTableName()
	{
		return dataTableName;
	}
	public void setDataTableName(String dataTableName)
	{
		this.dataTableName = dataTableName;
	}
	public String getDynTableName()
	{
		return dynTableName;
	}
	public void setDynTableName(String dynTableName)
	{
		this.dynTableName = dynTableName;
	}
	public Integer getTempType()
	{
		return tempType;
	}
	public void setTempType(Integer tempType)
	{
		this.tempType = tempType;
	}
	public String getParams()
	{
		return params;
	}
	public void setParams(String params)
	{
		this.params = params;
	}
	public String getHasParams()
	{
		return hasParams;
	}
	public void setHasParams(String hasParams)
	{
		this.hasParams = hasParams;
	}
	
	
}
