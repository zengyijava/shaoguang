package com.montnets.emp.entity.wxgl;

import java.sql.Timestamp;


/**
 * 实体类： LfWeiCount
 *
 * @project p_wei
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfWeiCount implements java.io.Serializable
{
   
    // 编号
    private Long	countId;
   
    // 新关注人数 
    private Integer	unfollowCount;
   
    // 取消关注人数
    private Integer	followCount;
   
    // 净关注人数
    private Integer	incomeCount;
   
    // 累计关注人数
    private Integer	amountCount;
   
    // 当前统计日期
    private Timestamp	dayTime;
   
    // 公众帐号ID
    private Long	aId;
   
    // 企业编号
    private String	corpCode;
   
    // 修改时间
    private Timestamp	modifytime;
   
    
	public Long getCountId()
	{
		return countId;
	}

	public void setCountId(Long countId)
	{
		this.countId = countId;
	}
    
	public Timestamp getDayTime()
    {
        return dayTime;
    }

    public void setDayTime(Timestamp dayTime)
    {
        this.dayTime = dayTime;
    }
    
    public Long getAId()
	{
		return aId;
	}

	public Integer getUnfollowCount()
    {
        return unfollowCount;
    }

    public void setUnfollowCount(Integer unfollowCount)
    {
        this.unfollowCount = unfollowCount;
    }

    public Integer getFollowCount()
    {
        return followCount;
    }

    public void setFollowCount(Integer followCount)
    {
        this.followCount = followCount;
    }

    public Integer getIncomeCount()
    {
        return incomeCount;
    }

    public void setIncomeCount(Integer incomeCount)
    {
        this.incomeCount = incomeCount;
    }

    public Integer getAmountCount()
    {
        return amountCount;
    }

    public void setAmountCount(Integer amountCount)
    {
        this.amountCount = amountCount;
    }

    public void setAId(Long aId)
	{
		this.aId = aId;
	}
    
	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
    
	public Timestamp getModifytime()
	{
		return modifytime;
	}

	public void setModifytime(Timestamp modifytime)
	{
		this.modifytime = modifytime;
	}

}