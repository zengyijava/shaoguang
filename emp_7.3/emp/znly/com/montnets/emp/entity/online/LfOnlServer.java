package com.montnets.emp.entity.online;

import java.sql.Timestamp;

public class LfOnlServer implements java.io.Serializable
{

    /**
     * @description  
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-25 上午11:45:55
     */
    private static final long serialVersionUID = -6701756275918393163L;
    // 标识列id自增
    private Long serId;
    // 服务号
    private String serNum;
    // 客服人员id
    private Long customeId;
    // 公众号Id.若值为0时，则为APP对应的服务号
    private Long AId;
    // 创建时间
    private Timestamp createTime;
    // 手机用户的openid
    private String fromUser;
    // 评分
    private Integer score;
    // 评价信息
    private String evaluate;
    // 服务时长，单位（分）
    private Integer duration;
    // 结束时间
    private Timestamp endTime;
    // 服务类型，1-微信，6-app
    private Integer serType; 

    public LfOnlServer()
    {
        this.createTime = new Timestamp(System.currentTimeMillis());
    }
    
    public Long getSerId()
    {
        return serId;
    }

    public void setSerId(Long serId)
    {
        this.serId = serId;
    }

    public String getSerNum()
    {
        return serNum;
    }

    public void setSerNum(String serNum)
    {
        this.serNum = serNum;
    }

    public Long getCustomeId()
    {
        return customeId;
    }

    public void setCustomeId(Long customeId)
    {
        this.customeId = customeId;
    }

    public Long getAId()
    {
        return AId;
    }

    public void setAId(Long aId)
    {
        AId = aId;
    }

    public Timestamp getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime)
    {
        this.createTime = createTime;
    }

    public String getFromUser()
    {
        return fromUser;
    }

    public void setFromUser(String fromUser)
    {
        this.fromUser = fromUser;
    }

    public Integer getScore()
    {
        return score;
    }

    public void setScore(Integer score)
    {
        this.score = score;
    }

    public String getEvaluate()
    {
        return evaluate;
    }

    public void setEvaluate(String evaluate)
    {
        this.evaluate = evaluate;
    }

    public Integer getDuration()
    {
        return duration;
    }

    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }

    public Timestamp getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Timestamp endTime)
    {
        this.endTime = endTime;
    }

	public Integer getSerType() {
		return serType;
	}

	public void setSerType(Integer serType) {
		this.serType = serType;
	}
}
