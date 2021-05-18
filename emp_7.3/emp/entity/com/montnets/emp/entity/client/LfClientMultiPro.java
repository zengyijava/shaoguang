package com.montnets.emp.entity.client;

import java.sql.Timestamp;
 
 
/**
 * TableLfClient对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-21 下午01:39:29
 * @description 
 */
public class LfClientMultiPro implements java.io.Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1404468256278571926L;

	/**
	 * 
	 */
	//private static final long serialVersionUID = 5873535411176160320L;
	//客户ID
	private Long clientId;
	//客户机构
	private Long depId;
	//分类ID
	private Long ccId;
	//客户姓名
	private String name;
	//性别
	private Integer sex;
	//生日
	private Timestamp birthday;
	//手机
	private String mobile;
	//办公电话
	private String oph;
	//QQ号
	private String qq;
	//Email地址
	private String EMail;
	//msn账号
	private String msn;
	//职位
	private String job;
	//行业
	private String profession;
	//所属区域
	private String area;
	//使用状态（0无效，1有效）
	private Integer cstate;
	//客户信息是否（0否，1是）共享
	private Integer shareState;
	//描述
	private String comments;
	//是否（0否，1是）接收短信
	private Integer recState;
	//隐藏手机号（0否，1是）
	private Integer hideState;
	//添加操作员
	private Long userId;
	//客户经理姓名
	private String ename;
	//业务类型（业务类型编码）
	private String bizId;
	//客户编号（唯一标识）
	private String clientCode;
	//机构编码
 	private String depCode;
 	//手机号码, 可能为空，多值时格式为phone1;phone2
 	private String phone;
 	//员工批次号（yyyymmddxxxxx)
	private String batchNo;
	//通讯录唯一标识
	private Long guId;
	//企业编码
	private String corpCode;
	//客户自定义属性 field 01~50
	private String field01 = "";
	
	private String field02 = "";
	
	private String field03 = "";

	private String field04 = "";

	private String field05 = "";

	private String field06 = "";

	private String field07 = "";

	private String field08 = "";

	private String field09 = "";

	private String field10 = "";

	private String field11 = "";

	private String field12 = "";

	private String field13 = "";

	private String field14 = "";

	private String field15 = "";
	
	private String field16 = "";

	private String field17 = "";

	private String field18 = "";

	private String field19 = "";

	private String field20 = "";
	
	private String field21 = "";
	
	private String field22 = "";
	
	private String field23 = "";
	
	private String field24 = "";
	
	private String field25 = "";
	
	private String field26 = "";
	
	private String field27 = "";
	
	private String field28 = "";
	
	private String field29 = "";
	
	private String field30 = "";
	
	private String field31 = "";
	
	private String field32 = "";
	
	private String field33 = "";
	
	private String field34 = "";
	
	private String field35 = "";
	
	private String field36 = "";
	
	private String field37 = "";
	
	private String field38 = "";
	
	private String field39 = "";
	
	private String field40 = "";
	
	private String field41 = "";
	
	private String field42 = "";
	
	private String field43 = "";
	
	private String field44 = "";
	
	private String field45 = "";
	
	private String field46 = "";
	
	private String field47 = "";

	private String field48 = "";
	
	private String field49 = "";
	
	private String field50 = ""; 
	
	public LfClientMultiPro() 
	{
		this.ccId = new Long(1);
		
	}

	public Long getClientId()
	{
		return clientId;
	}

	public void setClientId(Long clientId)
	{
		this.clientId = clientId;
	}

	public Long getDepId()
	{
		return depId;
	}

	public void setDepId(Long depId)
	{
		this.depId = depId;
	}

	public Long getCcId()
	{
		return ccId;
	}

	public void setCcId(Long ccId)
	{
		this.ccId = ccId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getSex()
	{
		return sex;
	}

	public void setSex(Integer sex)
	{
		this.sex = sex;
	}

	public Timestamp getBirthday()
	{
		return birthday;
	}

	public void setBirthday(Timestamp birthday)
	{
		this.birthday = birthday;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getOph()
	{
		return oph;
	}

	public void setOph(String oph)
	{
		this.oph = oph;
	}

	public String getQq()
	{
		return qq;
	}

	public void setQq(String qq)
	{
		this.qq = qq;
	}

	public String getEMail()
	{
		return EMail;
	}

	public void setEMail(String eMail)
	{
		EMail = eMail;
	}

	public String getMsn()
	{
		return msn;
	}

	public void setMsn(String msn)
	{
		this.msn = msn;
	}

	public String getJob()
	{
		return job;
	}

	public void setJob(String job)
	{
		this.job = job;
	}

	public String getProfession()
	{
		return profession;
	}

	public void setProfession(String profession)
	{
		this.profession = profession;
	}

	public String getArea()
	{
		return area;
	}

	public void setArea(String area)
	{
		this.area = area;
	}

	public Integer getCstate()
	{
		return cstate;
	}

	public void setCstate(Integer cstate)
	{
		this.cstate = cstate;
	}

 
	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	 

	public Integer getRecState()
	{
		return recState;
	}

	public void setRecState(Integer recState)
	{
		this.recState = recState;
	}

 
	public Integer getShareState()
	{
		return shareState;
	}

	public void setShareState(Integer shareState)
	{
		this.shareState = shareState;
	}

	public Integer getHideState()
	{
		return hideState;
	}

	public void setHideState(Integer hideState)
	{
		this.hideState = hideState;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getEname()
	{
		return ename;
	}

	public void setEname(String ename)
	{
		this.ename = ename;
	}

	public String getBizId()
	{
		return bizId;
	}

	public void setBizId(String bizId)
	{
		this.bizId = bizId;
	}

	public String getClientCode()
	{
		return clientCode;
	}

	public void setClientCode(String clientCode)
	{
		this.clientCode = clientCode;
	}

	public String getDepCode()
	{
		return depCode;
	}

	public void setDepCode(String depCode)
	{
		this.depCode = depCode;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getBatchNo()
	{
		return batchNo;
	}

	public void setBatchNo(String batchNo)
	{
		this.batchNo = batchNo;
	}

	public Long getGuId()
	{
		return guId;
	}

	public void setGuId(Long guId)
	{
		this.guId = guId;
	}

	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
	
	public String getField01() {
		return field01;
	}

	public void setField01(String field01) {
		this.field01 = field01;
	}

	public String getField02() {
		return field02;
	}

	public void setField02(String field02) {
		this.field02 = field02;
	}

	public String getField03() {
		return field03;
	}

	public void setField03(String field03) {
		this.field03 = field03;
	}

	public String getField04() {
		return field04;
	}

	public void setField04(String field04) {
		this.field04 = field04;
	}

	public String getField05() {
		return field05;
	}

	public void setField05(String field05) {
		this.field05 = field05;
	}

	public String getField06() {
		return field06;
	}

	public void setField06(String field06) {
		this.field06 = field06;
	}

	public String getField07() {
		return field07;
	}

	public void setField07(String field07) {
		this.field07 = field07;
	}

	public String getField08() {
		return field08;
	}

	public void setField08(String field08) {
		this.field08 = field08;
	}

	public String getField09() {
		return field09;
	}

	public void setField09(String field09) {
		this.field09 = field09;
	}

	public String getField10()
	{
		return field10;
	}

	public void setField10(String field10)
	{
		this.field10 = field10;
	}

	public String getField11()
	{
		return field11;
	}

	public void setField11(String field11)
	{
		this.field11 = field11;
	}

	public String getField12()
	{
		return field12;
	}

	public void setField12(String field12)
	{
		this.field12 = field12;
	}

	public String getField13()
	{
		return field13;
	}

	public void setField13(String field13)
	{
		this.field13 = field13;
	}

	public String getField14()
	{
		return field14;
	}

	public void setField14(String field14)
	{
		this.field14 = field14;
	}

	public String getField15() {
		return field15;
	}

	public void setField15(String field15) {
		this.field15 = field15;
	}

	public String getField16()
	{
		return field16;
	}

	public void setField16(String field16)
	{
		this.field16 = field16;
	}

	public String getField17()
	{
		return field17;
	}

	public void setField17(String field17)
	{
		this.field17 = field17;
	}

	public String getField18()
	{
		return field18;
	}

	public void setField18(String field18)
	{
		this.field18 = field18;
	}

	public String getField19()
	{
		return field19;
	}

	public void setField19(String field19)
	{
		this.field19 = field19;
	}

	public String getField20()
	{
		return field20;
	}

	public void setField20(String field20)
	{
		this.field20 = field20;
	}

	public String getField21()
	{
		return field21;
	}

	public void setField21(String field21)
	{
		this.field21 = field21;
	}

	public String getField22()
	{
		return field22;
	}

	public void setField22(String field22)
	{
		this.field22 = field22;
	}

	public String getField23()
	{
		return field23;
	}

	public void setField23(String field23)
	{
		this.field23 = field23;
	}

	public String getField24()
	{
		return field24;
	}

	public void setField24(String field24)
	{
		this.field24 = field24;
	}

	public String getField25()
	{
		return field25;
	}

	public void setField25(String field25)
	{
		this.field25 = field25;
	}

	public String getField26()
	{
		return field26;
	}

	public void setField26(String field26)
	{
		this.field26 = field26;
	}

	public String getField27()
	{
		return field27;
	}

	public void setField27(String field27)
	{
		this.field27 = field27;
	}

	public String getField28()
	{
		return field28;
	}

	public void setField28(String field28)
	{
		this.field28 = field28;
	}

	public String getField29()
	{
		return field29;
	}

	public void setField29(String field29)
	{
		this.field29 = field29;
	}

	public String getField30()
	{
		return field30;
	}

	public void setField30(String field30)
	{
		this.field30 = field30;
	}

	public String getField31()
	{
		return field31;
	}

	public void setField31(String field31)
	{
		this.field31 = field31;
	}

	public String getField32()
	{
		return field32;
	}

	public void setField32(String field32)
	{
		this.field32 = field32;
	}

	public String getField33()
	{
		return field33;
	}

	public void setField33(String field33)
	{
		this.field33 = field33;
	}

	public String getField34()
	{
		return field34;
	}

	public void setField34(String field34)
	{
		this.field34 = field34;
	}

	public String getField35()
	{
		return field35;
	}

	public void setField35(String field35)
	{
		this.field35 = field35;
	}

	public String getField36()
	{
		return field36;
	}

	public void setField36(String field36)
	{
		this.field36 = field36;
	}

	public String getField37()
	{
		return field37;
	}

	public void setField37(String field37)
	{
		this.field37 = field37;
	}

	public String getField38()
	{
		return field38;
	}

	public void setField38(String field38)
	{
		this.field38 = field38;
	}

	public String getField39()
	{
		return field39;
	}

	public void setField39(String field39)
	{
		this.field39 = field39;
	}

	public String getField40()
	{
		return field40;
	}

	public void setField40(String field40)
	{
		this.field40 = field40;
	}

	public String getField41()
	{
		return field41;
	}

	public void setField41(String field41)
	{
		this.field41 = field41;
	}

	public String getField42()
	{
		return field42;
	}

	public void setField42(String field42)
	{
		this.field42 = field42;
	}

	public String getField43()
	{
		return field43;
	}

	public void setField43(String field43)
	{
		this.field43 = field43;
	}

	public String getField44()
	{
		return field44;
	}

	public void setField44(String field44)
	{
		this.field44 = field44;
	}

	public String getField45()
	{
		return field45;
	}

	public void setField45(String field45)
	{
		this.field45 = field45;
	}

	public String getField46()
	{
		return field46;
	}

	public void setField46(String field46)
	{
		this.field46 = field46;
	}

	public String getField47()
	{
		return field47;
	}

	public void setField47(String field47)
	{
		this.field47 = field47;
	}

	public String getField48()
	{
		return field48;
	}

	public void setField48(String field48)
	{
		this.field48 = field48;
	}

	public String getField49()
	{
		return field49;
	}

	public void setField49(String field49)
	{
		this.field49 = field49;
	}

	public String getField50()
	{
		return field50;
	}

	public void setField50(String field50)
	{
		this.field50 = field50;
	}

 
 

 
	 
	 
}