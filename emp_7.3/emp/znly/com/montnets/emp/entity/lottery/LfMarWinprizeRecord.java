package com.montnets.emp.entity.lottery;

import java.sql.Timestamp;



/**
 * 实体类： LfMarWinprizeRecord
 *中奖记录信息
 * @project p_mar
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfMarWinprizeRecord implements java.io.Serializable
{
   
    /**
     * @description  
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2014-1-3 上午11:58:10
     */
    private static final long serialVersionUID = -5214794997795135472L;

    // 自增ID 
    private Long	wid;
   
    // 活动ID
    private Long	sid;
   
    // 公众帐号ID
    private Long	aid;
   
    // 历史记录ID
    private Long	rid;
    
    //奖品 ID
    private Long pid;

    // 普通用户OPENID
    private String	openId;
   
    // 流水号
    private String	serialNum;
   
    // 手机号码
    private String	phone;
   
    // 企业编码
    private String	corpCode;
   
    // 中奖时间
    private Timestamp	createtime;
   

	public Long getWid()
    {
        return wid;
    }

    public void setWid(Long wid)
    {
        this.wid = wid;
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

    public Long getRid()
    {
        return rid;
    }

    public void setRid(Long rid)
    {
        this.rid = rid;
    }

    public String getOpenId()
	{
		return openId;
	}

	public void setOpenId(String openId)
	{
		this.openId = openId;
	}
    
	public String getSerialNum()
	{
		return serialNum;
	}

	public void setSerialNum(String serialNum)
	{
		this.serialNum = serialNum;
	}
    
	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
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
	
	   
    public Long getPid()
    {
        return pid;
    }

    public void setPid(Long pid)
    {
        this.pid = pid;
    }

}