/**
 * 
 */
package com.montnets.emp.entity.biztype;

import java.sql.Timestamp;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-20 下午03:11:15
 * @description
 */

public class LfBusManager implements java.io.Serializable
{
	/**
	 * 
	 * */
	private static final long serialVersionUID = 3202364415064310922L;
	//业务ID
	private Integer busId;
	//业务编码(B+业务类型+"-"+子模块类型)
	private String busCode;
	//业务名称
	private String busName;
	//业务描述
	private String busDescription;
	//类名
	private String className;
	//企业编码
	private String corpCode;
	
	private Long userId;
	
	//业务状态 0启用  1禁用
	private Integer state;
	//业务类型（0：手动；1：触发；2：手动+触发）
	private Integer busType;
	//优先级，-99表示无优先级
	private Integer riseLevel;
	//创建时间
	private Timestamp createTime;
	//修改时间
	private Timestamp updateTime;
	//机构ID
	private Long depId;
	
	
	public Long getDepId()
	{
		return depId;
	}

	public void setDepId(Long depId)
	{
		this.depId = depId;
	}

	public Integer getState()
	{
		return state;
	}

	public void setState(Integer state)
	{
		this.state = state;
	}

	public Integer getBusType()
	{
		return busType;
	}

	public void setBusType(Integer busType)
	{
		this.busType = busType;
	}

	public Integer getRiseLevel()
	{
		return riseLevel;
	}

	public void setRiseLevel(Integer riseLevel)
	{
		this.riseLevel = riseLevel;
	}

	public Timestamp getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public LfBusManager()
	{
	}

	public Integer getBusId()
	{
		return busId;
	}

	public void setBusId(Integer busId)
	{
		this.busId = busId;
	}

	public String getBusCode()
	{
		return busCode;
	}

	public void setBusCode(String busCode)
	{
		this.busCode = busCode;
	}

	public String getBusName()
	{
		return busName;
	}

	public void setBusName(String busName)
	{
		this.busName = busName;
	}

	public String getBusDescription()
	{
		return busDescription;
	}

	public void setBusDescription(String busDescription)
	{
		this.busDescription = busDescription;
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
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
