package com.montnets.emp.entity.lbs;

import java.sql.Timestamp;

/**
 * 
 * @description     用户地理位置信息
 * @project ott     
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-12-17 下午03:55:14
 */
public class LfLbsUserPios implements java.io.Serializable
{

    /**
     * @description  
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-17 下午03:55:06
     */
    private static final long serialVersionUID = -245035913121833234L;
    
    // 自增ID
    private Long              upid;

    // 纬度
    private String            lat;

    // 经度
    private String            lng;
    
    // 公众帐号ID
    private Long              AId;
    
    // 用户openid
    private String              openid;

    // 企业编码
    private String            corpCode;

    // 更新时间
    private Timestamp         modifytime;
    
    public Long getUpid()
    {
        return upid;
    }

    public void setUpid(Long upid)
    {
        this.upid = upid;
    }

    public String getLat()
    {
        return lat;
    }

    public void setLat(String lat)
    {
        this.lat = lat;
    }

    public String getLng()
    {
        return lng;
    }

    public void setLng(String lng)
    {
        this.lng = lng;
    }

    public Long getAId()
    {
        return AId;
    }

    public void setAId(Long aId)
    {
        AId = aId;
    }

    public String getOpenid()
    {
        return openid;
    }

    public void setOpenid(String openid)
    {
        this.openid = openid;
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
