package com.montnets.emp.entity.wxgl;

import java.sql.Timestamp;

/**
 * 实体类： OtWeiUserinfo 用户微信资料
 * 
 * @project p_wei
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfWeiUserInfo implements java.io.Serializable
{

    /**
     * @description
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-29 下午04:34:01
     */
    private static final long serialVersionUID = 2021303186844909190L;

    // 微信ID
    private Long               wcId;

    // 微信号的唯一标识
    private String             fakeId;

    // 用户组ID
    private Long               GId;

    // 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息
    private String             subscribe;

    // 用户的昵称
    private String             nickName;

    // 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    private String             sex;

    // 用户所在城市
    private String             city;

    // 用户所在省份
    private String             province;

    // 用户所在国家
    private String             country;

    // 用户的语言，简体中文为zh_CN
    private String             language;

    // 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
    private String             headImgUrl;

    // 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
    private Timestamp          subscribeTime;

    // 姓名（用户，员工，客户其之一的姓名）
    private String             uname;

    // 手机号码
    private String             phone;

    // 其他
    private String             descr;

    // 提交基本信息时间
    private Timestamp          verifytime;

    // 用户ID
    private Long               UId;

    // 企业编号
    private String             corpCode;

    // 创建时间
    private Timestamp          createtime;

    // 最后修改时间
    private Timestamp          modifytime;

    // 微信用户openId
    private String            openId;

    // 公众帐号ID
    private Long              AId;

    // 用户头像本地头像地址，用户没有头像时该项为空
    private String             localImgUrl;

    public Long getWcId()
    {
        return wcId;
    }

    public void setWcId(Long wcId)
    {
        this.wcId = wcId;
    }

    public String getFakeId()
    {
        return fakeId;
    }

    public void setFakeId(String fakeId)
    {
        this.fakeId = fakeId;
    }

    public Long getGId()
    {
        return GId;
    }

    public void setGId(Long gId)
    {
        GId = gId;
    }

    public String getSubscribe()
    {
        return subscribe;
    }

    public void setSubscribe(String subscribe)
    {
        this.subscribe = subscribe;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getHeadImgUrl()
    {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl)
    {
        this.headImgUrl = headImgUrl;
    }

    public Timestamp getSubscribeTime()
    {
        return subscribeTime;
    }

    public void setSubscribeTime(Timestamp subscribeTime)
    {
        this.subscribeTime = subscribeTime;
    }

    public String getUname()
    {
        return uname;
    }

    public void setUname(String uname)
    {
        this.uname = uname;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getDescr()
    {
        return descr;
    }

    public void setDescr(String descr)
    {
        this.descr = descr;
    }

    public Timestamp getVerifytime()
    {
        return verifytime;
    }

    public void setVerifytime(Timestamp verifytime)
    {
        this.verifytime = verifytime;
    }

    public Long getUId()
    {
        return UId;
    }

    public void setUId(Long UId)
    {
        this.UId = UId;
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

    public String getOpenId()
    {
        return openId;
    }

    public void setOpenId(String openId)
    {
        this.openId = openId;
    }

    public Long getAId()
    {
        return AId;
    }

    public void setAId(Long aId)
    {
        AId = aId;
    }

    public String getLocalImgUrl()
    {
        return localImgUrl;
    }

    public void setLocalImgUrl(String localImgUrl)
    {
        this.localImgUrl = localImgUrl;
    }

}
