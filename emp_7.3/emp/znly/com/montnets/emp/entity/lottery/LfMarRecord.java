package com.montnets.emp.entity.lottery;

import java.sql.Timestamp;


/**
 * 实体类： LfMarRecord
 *抽奖记录信息
 * @project p_mar
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfMarRecord implements java.io.Serializable
{
   
    /**
     * @description  
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2014-1-3 上午11:59:13
     */
    private static final long serialVersionUID = 1164385792988900222L;

    // 自增ID
    private Long	rid;
   
    //  活动ID
    private Long	sid;
   
    //  公众账号
    private Long	aid;
   
    //普通用户OPENID
    private String	openId;
   
    //随机数
    private Long	randomNum;
   
    //企业编码
    private String	corpCode;
   
    //  创建时间
    private Timestamp	createtime;
   
    

    
	public Long getRid()
    {
        return rid;
    }

    public void setRid(Long rid)
    {
        this.rid = rid;
    }

    public Long getSid()
    {
        return sid;
    }

    public void setSid(Long sid)
    {
        this.sid = sid;
    }

    public Long getAid()
    {
        return aid;
    }

    public void setAid(Long aid)
    {
        this.aid = aid;
    }

    public String getOpenId()
	{
		return openId;
	}

	public void setOpenId(String openId)
	{
		this.openId = openId;
	}
    
	public Long getRandomNum()
	{
		return randomNum;
	}

	public void setRandomNum(Long randomNum)
	{
		this.randomNum = randomNum;
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

}