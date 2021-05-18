package com.montnets.emp.entity.lbs;

import java.sql.Timestamp;


/**
 * 实体类： LfLbsPushset
 *
 * @project p_lbs
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfLbsPushset implements java.io.Serializable
{
   
    /**
     * @description  
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-9 下午02:06:16
     */
    private static final long serialVersionUID = 110258588869588703L;

    // 自增ID 
    private Long	pushId;
   
    //  推送模式类型   1多图文   2页面交互
    private Integer	pushtype;
   
    // 图片地址
    private String	imgurl;
   
    // 简介
    private String	note;
   
    // 服务半径
    private String	radius;
   
    // 是否自动 扩大搜索半径
    private Integer	autoradius;
   
    // 多图文推送条目 
    private Integer	pushcount;
   
    // 是否启用更多
    private Integer	automore;
   
    // 企业编码
    private String	corpCode;
   
    // 创建时间
    private Timestamp	createtime;
   
    // 更新时间 
    private Timestamp	moditytime;

    public Long getPushId()
    {
        return pushId;
    }

    public void setPushId(Long pushId)
    {
        this.pushId = pushId;
    }

    public Integer getPushtype()
    {
        return pushtype;
    }

    public void setPushtype(Integer pushtype)
    {
        this.pushtype = pushtype;
    }

    public String getImgurl()
    {
        return imgurl;
    }

    public void setImgurl(String imgurl)
    {
        this.imgurl = imgurl;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getRadius()
    {
        return radius;
    }

    public void setRadius(String radius)
    {
        this.radius = radius;
    }

    public Integer getAutoradius()
    {
        return autoradius;
    }

    public void setAutoradius(Integer autoradius)
    {
        this.autoradius = autoradius;
    }

    public Integer getPushcount()
    {
        return pushcount;
    }

    public void setPushcount(Integer pushcount)
    {
        this.pushcount = pushcount;
    }

    public Integer getAutomore()
    {
        return automore;
    }

    public void setAutomore(Integer automore)
    {
        this.automore = automore;
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

}