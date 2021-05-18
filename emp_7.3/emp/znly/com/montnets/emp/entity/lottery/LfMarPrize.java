package com.montnets.emp.entity.lottery;

import java.sql.Timestamp;

import oracle.sql.TIMESTAMP;

/**
 * 实体类： LfMarPrize
 * 抽奖活动奖项信息
 * 
 * @project p_mar
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfMarPrize implements java.io.Serializable
{

    /**
     * @description
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2014-1-3 上午11:50:12
     */
    private static final long serialVersionUID = 2698263582584501005L;

    // 自增ID
    private Long              pid;

    // 活动ID
    private Long              sid;

    // 奖品名称
    private String            title;

    // 限制奖品个数
    private Integer           limitCount;

    // 启用状态 1开启 2禁用
    private Integer           usingAttr;

    // 随机数头区间
    private Integer           headRange;

    // 随机数尾区间
    private Integer           endRange;

    // 手工输入中奖数字
    private String            rangePart;

    // 奖品下发开始时间
    private Timestamp         beginPrizeTime;

    // 奖品结束下发时间
    private Timestamp         endPrizeTime;

    public Long getPid()
    {
        return pid;
    }

    public void setPid(Long pid)
    {
        this.pid = pid;
    }

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

    public Integer getLimitCount()
    {
        return limitCount;
    }

    public void setLimitCount(Integer limitCount)
    {
        this.limitCount = limitCount;
    }

    public Integer getUsingAttr()
    {
        return usingAttr;
    }

    public void setUsingAttr(Integer usingAttr)
    {
        this.usingAttr = usingAttr;
    }

    public Integer getHeadRange()
    {
        return headRange;
    }

    public void setHeadRange(Integer headRange)
    {
        this.headRange = headRange;
    }

    public Integer getEndRange()
    {
        return endRange;
    }

    public void setEndRange(Integer endRange)
    {
        this.endRange = endRange;
    }

    public String getRangePart()
    {
        return rangePart;
    }

    public void setRangePart(String rangePart)
    {
        this.rangePart = rangePart;
    }

    public Timestamp getBeginPrizeTime()
    {
        return beginPrizeTime;
    }

    public void setBeginPrizeTime(Timestamp beginPrizeTime)
    {
        this.beginPrizeTime = beginPrizeTime;
    }

    public Timestamp getEndPrizeTime()
    {
        return endPrizeTime;
    }

    public void setEndPrizeTime(Timestamp endPrizeTime)
    {
        this.endPrizeTime = endPrizeTime;
    }

}
