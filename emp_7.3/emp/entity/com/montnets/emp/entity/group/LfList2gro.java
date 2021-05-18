package com.montnets.emp.entity.group;

/**
 * TableLfList2gro对应的实体类
 * 自定义通讯录_ 分组
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 上午11:14:50
 * @description
 */
public class LfList2gro implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4390753035556723974L;

	/**
	 * 
	 */
	// private static final long serialVersionUID = 3702846134112955457L;
	//标识列
	private Long l2gId;
	// 添加类型(员工0,客户1,手工2)
 	private Integer l2gType;
	// 分组ID
	private Integer udgId;
	//唯一标识  L2G_TYPE
	//为0时，插入的是员工通讯录的GUID；
	//为1时,插入的是客户通讯录的GUID；
	//为2时，插入的是自定义通讯录的GUID;
	//为3时，插入的是操作员的GUID)
	private Long guId;
	
	//共享类型
	private Integer sharetype;
	
	public Long getL2gId()
	{
		return l2gId;
	}

	public void setL2gId(Long l2gId)
	{
		this.l2gId = l2gId;
	}

	public Integer getL2gType()
	{
		return l2gType;
	}

	public void setL2gType(Integer l2gType)
	{
		this.l2gType = l2gType;
	}

	public Integer getUdgId()
	{
		return udgId;
	}

	public void setUdgId(Integer udgId)
	{
		this.udgId = udgId;
	}

	public Long getGuId()
	{
		return guId;
	}

	public void setGuId(Long guId)
	{
		this.guId = guId;
	}

	public Integer getSharetype() {
		return sharetype;
	}

	public void setSharetype(Integer sharetype) {
		this.sharetype = sharetype;
	}

	 

}