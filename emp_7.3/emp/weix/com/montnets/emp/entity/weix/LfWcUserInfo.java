package com.montnets.emp.entity.weix;

import java.sql.Timestamp;

/**
 * 微信用户基本信息表
 * 
 * @project p_weix
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfWcUserInfo implements java.io.Serializable
{
	private static final long	serialVersionUID	= -4923255341568942858L;

	// 微信帐号ID
	private Long				wcId;

	// 微信号的唯一标识
	private String				fakeId;

	// 微信名称
	private String				name;

	// 微信名
	private String				code;

	// 微信图像的url地址
	private String				img;

	// 类型 1.客户 2.员工 3.操作员
	private Integer				type;

	// 个人签名
	private String				signature;

	// 企业编号
	private String				corpCode;

	// 创建时间
	private Timestamp			createTime;

	// 修改时间
	private Timestamp			modifyTime;

	// 手机号
	private String				phone;

	// emp的姓名（用户，员工，客户其之一的姓名）
	private String				uname;

	// 其他
	private String				descr;

	// 提交基本信息时间
	private Timestamp			verifyTime;

	// EMP用户ID
	private Long				UId;

	public LfWcUserInfo()
	{
	}

	public static long getSerialVersionUID()
	{
		return serialVersionUID;
	}

	public Long getWcId()
	{
		return wcId;
	}

	public void setWcId(Long wcId)
	{
		this.wcId = wcId;
	}

	public String getFakeId()
	{
		return fakeId;
	}

	public void setFakeId(String fakeId)
	{
		this.fakeId = fakeId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getImg()
	{
		return img;
	}

	public void setImg(String img)
	{
		this.img = img;
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
	}

	public String getSignature()
	{
		return signature;
	}

	public void setSignature(String signature)
	{
		this.signature = signature;
	}

	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}

	public Timestamp getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}

	public Timestamp getModifyTime()
	{
		return modifyTime;
	}

	public void setModifyTime(Timestamp modifyTime)
	{
		this.modifyTime = modifyTime;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getUname()
	{
		return uname;
	}

	public void setUname(String uname)
	{
		this.uname = uname;
	}

	public String getDescr()
	{
		return descr;
	}

	public void setDescr(String descr)
	{
		this.descr = descr;
	}

	public Timestamp getVerifyTime()
	{
		return verifyTime;
	}

	public void setVerifyTime(Timestamp verifyTime)
	{
		this.verifyTime = verifyTime;
	}

	public Long getUId()
	{
		return UId;
	}

	public void setUId(Long uId)
	{
		UId = uId;
	}

}
