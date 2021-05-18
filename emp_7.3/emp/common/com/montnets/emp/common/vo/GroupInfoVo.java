package com.montnets.emp.common.vo;

/**
 * 
 * @project emp
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-4 上午10:39:24
 * @description 通讯录分组信息vo
 */
public class GroupInfoVo implements java.io.Serializable
{
	private static final long serialVersionUID = -6769264862038843880L;
    //主键
	private Long udgId;
    //类型
	private Integer l2gType;
    //手机号
	private String mobile;
    //名称
	private String name;
    //关联id
	private Long l2gId;
    //用户名称
	private String udgName;
	//分组类型
	private Integer groupType;
	//分组属性
	private Integer gpAttribute;
	//用户guid
	private Long guId;
	//用户userid
	private Long userId;

	public Long getUdgId()
	{
		return udgId;
	}

	public void setUdgId(Long udgId)
	{
		this.udgId = udgId;
	}

	public Integer getL2gType()
	{
		return l2gType;
	}

	public void setL2gType(Integer l2gType)
	{
		this.l2gType = l2gType;
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

	public Long getL2gId()
	{
		return l2gId;
	}

	public void setL2gId(Long l2gId)
	{
		this.l2gId = l2gId;
	}

	public String getUdgName()
	{
		return udgName;
	}

	public void setUdgName(String udgName)
	{
		this.udgName = udgName;
	}

	public Long getGuId()
	{
		return guId;
	}

	public void setGuId(Long guId)
	{
		this.guId = guId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public Integer getGroupType() {
		return groupType;
	}

	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}

	public Integer getGpAttribute() {
		return gpAttribute;
	}

	public void setGpAttribute(Integer gpAttribute) {
		this.gpAttribute = gpAttribute;
	}
	
	
	
}
