/**
 * 
 */
package com.montnets.emp.entity.group;

import java.sql.Timestamp;

 
/**
 * TableLfMalist
 * 手工添加通讯名单
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 ����11:20:21
 * @description 
 */

public class LfMalist implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3318253653957660688L;
	//private static final long serialVersionUID = -8831932217076244576L;
	//手工添加ID
	private Long maId;
	//姓名
	private String name;
	//性别
	private Integer sex;
	//生日
	private Timestamp birthday;
	//手机
	private String mobile;
	//办公电话
	private String oph;
	//QQ号码
	private String qq;
	//Email地址
	private String email;
	//Msn号码
	private String msn;
	//机构ID
	private Long depId;
	//员工ID
	private Long userId;
	//操作员ID
	private Long guId;
	//企业编码
	private String corpCode;
	
	public LfMalist(){}

	public Long getMaId()
	{
		return maId;
	}

	public void setMaId(Long maId)
	{
		this.maId = maId;
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

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getMsn()
	{
		return msn; 
	}

	public void setMsn(String msn)
	{
		this.msn = msn;
	}

	public Long getDepId()
	{
		return depId;
	}

	public void setDepId(Long depId)
	{
		this.depId = depId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
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
	 
	
	
}
