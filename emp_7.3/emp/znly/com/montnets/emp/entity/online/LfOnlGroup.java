package com.montnets.emp.entity.online;

import java.sql.Timestamp;


/**
 * 实体类： LfOnlGroup
 *
 * @project p_onl
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfOnlGroup implements java.io.Serializable
{
   
    /**
     * @description  
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-11 下午02:40:48
     */
    private static final long serialVersionUID = -2844776155038148112L;

    //  标识列
    private Long	gpId;
   
    // 群组名称
    private String	gpName;
   
    // 创建者
    private Long	createUser;
   
    // 创建时间
    private Timestamp	createTime;
    
    // 人数
    private Integer memCount;
   
	public Long getGpId()
	{
		return gpId;
	}

	public void setGpId(Long gpId)
	{
		this.gpId = gpId;
	}
    
	public String getGpName()
	{
		return gpName;
	}

	public void setGpName(String gpName)
	{
		this.gpName = gpName;
	}
    
	public Long getCreateUser()
	{
		return createUser;
	}

	public void setCreateUser(Long createUser)
	{
		this.createUser = createUser;
	}
    
	public Timestamp getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}

    public Integer getMemCount()
    {
        return memCount;
    }

    public void setMemCount(Integer memCount)
    {
        this.memCount = memCount;
    }
	
	

}