package com.montnets.emp.entity.lbs;

import java.sql.Timestamp;

/**
 * @description 地理位置采集表
 * @project ott
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-11-28 下午02:26:09
 */
public class LfLbsPios implements java.io.Serializable
{

    /**
     * @description
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-28 下午02:25:55
     */
    private static final long serialVersionUID = -4514781904393364075L;

    // 自增ID
    private Long              pid;

    // 城市ID
    private Long              cityid;

    // 纬度
    private String            lat;

    // 经度
    private String            lng;

    // 标题
    private String            title;

    // 关键字
    private String            keyword;

    // 电话
    private String            telephone;

    // 地址
    private String            address;

    // 描述
    private String            note;

    // 公众帐号ID
    private String             AId;

    // 企业编码
    private String            corpCode;

    // 创建时间
    private Timestamp         createtime;

    // 更新时间
    private Timestamp         modifytime;
    
   // 距离，做传值 比较
    private Double distance;

    public Long getPid()
    {
        return pid;
    }

    public void setPid(Long pid)
    {
        this.pid = pid;
    }

    public Long getCityid()
    {
        return cityid;
    }

    public void setCityid(Long cityid)
    {
        this.cityid = cityid;
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

    public String getTelephone()
    {
        return telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getAId()
    {
        return AId;
    }

    public void setAId(String aId)
    {
        AId = aId;
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

    public Timestamp getModifytime()
    {
        return modifytime;
    }

    public void setModifytime(Timestamp modifytime)
    {
        this.modifytime = modifytime;
    }

    public Double getDistance()
    {
        return distance;
    }

    public void setDistance(Double distance)
    {
        this.distance = distance;
    }
}
