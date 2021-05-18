package com.montnets.emp.entity.lottery;

import java.sql.Timestamp;


/**
 * 实体类： LfMarSweepstakes
 *    抽奖活动信息
 * @project p_mar
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfMarSweepstakes implements java.io.Serializable
{
   
    /**
     * @description  
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2014-1-3 上午11:52:34
     */
    private static final long serialVersionUID = 7291215508570314061L;

    // 自增ID
    private Long	sid;
   
    // 活动名称
    private String	title;
   
    // 关键字
    private String	keyword;
   
    // 描述
    private String	note;
   
    // 活动开始时间
    private Timestamp	begintime;
   
    // 活动结束时间
    private Timestamp	endtime;
   
    // 活动兑奖时间 
    private Timestamp	expiryTime;
   
    // 参与类型   123N
    private Integer	partakeType;
    
    //参与次数
    private Integer parTimes;
   
    // 参与人数
    private Integer	partakeCount;
   
    // 启用状态   1启用  2禁用
    private Integer	usingAttr;
   
    // 最大随机范围
    private Integer	probabilityNum;
    
    //中奖概率
    private Integer prizeWinning;
   
    // 企业编码 
    private String	corpCode;
   
    //创建时间
    private Timestamp	createtime;
   
    // 更新时间 
    private Timestamp	moditytime;
   
    

	public Long getSid()
    {
        return sid;
    }

    public void setSid(Long sid)
    {
        this.sid = sid;
    }

    public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}
    
	public String getKeyword()
	{
		return keyword;
	}

	public void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}
    
	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}
    
	public Timestamp getBegintime()
	{
		return begintime;
	}

	public void setBegintime(Timestamp begintime)
	{
		this.begintime = begintime;
	}
    
	public Timestamp getEndtime()
	{
		return endtime;
	}

	public void setEndtime(Timestamp endtime)
	{
		this.endtime = endtime;
	}
    
	public Timestamp getExpiryTime()
	{
		return expiryTime;
	}

	public void setExpiryTime(Timestamp expiryTime)
	{
		this.expiryTime = expiryTime;
	}
    
	public Integer getPartakeType()
	{
		return partakeType;
	}

	public void setPartakeType(Integer partakeType)
	{
		this.partakeType = partakeType;
	}
    
	public Integer getPartakeCount()
	{
		return partakeCount;
	}

	public void setPartakeCount(Integer partakeCount)
	{
		this.partakeCount = partakeCount;
	}
    
	public Integer getUsingAttr()
	{
		return usingAttr;
	}

	public void setUsingAttr(Integer usingAttr)
	{
		this.usingAttr = usingAttr;
	}
    
	public Integer getProbabilityNum()
	{
		return probabilityNum;
	}

	public void setProbabilityNum(Integer probabilityNum)
	{
		this.probabilityNum = probabilityNum;
	}
    
	public String getCorpCode()
	{
		return corpCode;
	}

	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
    
	public Timestamp getCreatetime()
	{
		return createtime;
	}

	public void setCreatetime(Timestamp createtime)
	{
		this.createtime = createtime;
	}
    
	public Timestamp getModitytime()
	{
		return moditytime;
	}

	public void setModitytime(Timestamp moditytime)
	{
		this.moditytime = moditytime;
	}
	
    public Integer getParTimes()
    {
        return parTimes;
    }

    public void setParTimes(Integer parTimes)
    {
        this.parTimes = parTimes;
    }

    public Integer getPrizeWinning()
    {
        return prizeWinning;
    }

    public void setPrizeWinning(Integer prizeWinning)
    {
        this.prizeWinning = prizeWinning;
    }
}