package com.montnets.emp.clientsms.vo;

/**
 * 客户
 * @project emp
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-15 下午03:46:13
 * @description
 */
public class LfClientVo implements java.io.Serializable
{
	private static final long serialVersionUID = -6215757813263883134L;
    //主键
	private Long clientId;
    //机构id
	private Long depId;
    //手机号
	private String mobile;
    //名称
	private String name;
    //msn
	private String msn;
    //qq
	private String qq;
    //性别
	private String sex;
    //生日
	private String birthDay;
    //email
	private String email;
    //座机
	private String oph;
	//机构code
	private String depCode;
	//客户code
	private String clientCode;
    //客户guid
	private Long guId;
	//机构名称
	private String depName;
	//关联id
	private Integer udgId;
    //职务
    private String job;
    //职业
    private String profession;
    //客户经理
    private String ename;
	//获取主键方法
	public Long getClientId()
	{
		return clientId;
	}
	//设置主键方法
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

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDepName()
	{
		return depName;
	}

	public void setDepName(String depName)
	{
		this.depName = depName;
	}

	public String getMsn()
	{
		return msn;
	}

	public void setMsn(String msn)
	{
		this.msn = msn;
	}

	public String getQq()
	{
		return qq;
	}

	public void setQq(String qq)
	{
		this.qq = qq;
	}

	public String getSex()
	{
		return sex;
	}

	public void setSex(String sex)
	{
		this.sex = sex;
	}

	public String getBirthDay()
	{
		return birthDay;
	}

	public void setBirthDay(String birthDay)
	{
		this.birthDay = birthDay;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getOph()
	{
		return oph;
	}

	public void setOph(String oph)
	{
		this.oph = oph;
	}

	public String getDepCode()
	{
		return depCode;
	}

	public void setDepCode(String depCode)
	{
		this.depCode = depCode;
	}

	public Long getGuId()
	{
		return guId;
	}

	public void setGuId(Long guId)
	{
		this.guId = guId;
	}

	public Integer getUdgId()
	{
		return udgId;
	}

	public void setUdgId(Integer udgId)
	{
		this.udgId = udgId;
	}

	public String getClientCode()
	{
		return clientCode;
	}

	public void setClientCode(String clientCode)
	{
		this.clientCode = clientCode;
	}

    public String getJob()
    {
        return job;
    }

    public void setJob(String job)
    {
        this.job = job;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }
}
