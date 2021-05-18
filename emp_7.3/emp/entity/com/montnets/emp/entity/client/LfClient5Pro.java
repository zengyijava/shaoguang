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
public class LfClient5Pro implements java.io.Serializable {
 
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
	
	public LfClient5Pro() 
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

	public String getField01()
	{
		return field01;
	}

	public void setField01(String field01)
	{
		this.field01 = field01;
	}

	public String getField02()
	{
		return field02;
	}

	public void setField02(String field02)
	{
		this.field02 = field02;
	}

	public String getField03()
	{
		return field03;
	}

	public void setField03(String field03)
	{
		this.field03 = field03;
	}

	public String getField04()
	{
		return field04;
	}

	public void setField04(String field04)
	{
		this.field04 = field04;
	}

	public String getField05()
	{
		return field05;
	}

	public void setField05(String field05)
	{
		this.field05 = field05;
	}
}