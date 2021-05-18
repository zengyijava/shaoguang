package com.montnets.emp.entity.weix;

import java.sql.Timestamp;

/**
 * 微信公众帐号实体类
 *
 * @author fangyt <foyoto@gmail.com>
 * @project p_weix
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfWcAccount {
    private static final long serialVersionUID = -4923255341568942888L;
    /**
     * 腾讯给出的微信公众号的唯一标识
     */
    private String fakeId;
    /**
     * 公众帐号的名称
     */
    private String name;
    /**
     * 公众帐号
     */
    private String code;
    /**
     * 微信公众帐号openid
     */
    private String openId;
    /**
     * 微信图像的url地址
     */
    private String img;
    /**
     * 公众帐号类型
     */
    private Integer type;
    /**
     * 是否认证
     */
    private Integer isApprove;
    /**
     * 二维码
     */
    private String qrcode;
    /**
     * 功能介绍
     */
    private String info;
    /**
     * API接入地址
     */
    private String url;
    /**
     * 接入TOKEN
     */
    private String token;
    /**
     * 接入时间
     */
    private Timestamp bindTime;
    /**
     * 企业编号
     */
    private String corpCode;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 第三方用户唯一凭证
     */
    private String appId;
    /**
     * 修改时间
     */
    private Timestamp modifyTime;
    /**
     * 获取access_token填写client_credential
     */
    private String grantType;
    /**
     * 第三方用户唯一凭证密钥，既appsecret
     */
    private String secret;
    /**
     * 口令凭证
     */
    private String accessToken;
    /**
     * 获取token的时间
     */
    private Timestamp accessTime;
    /**
     * 菜单发布时间
     */
    private Timestamp releaseTime;
    /**
     * 公众帐号程序自增编号
     */
    private Long AId;

    public Long getAId() {
        return AId;
    }

    public void setAId(Long AId) {
        this.AId = AId;
    }

    public String getFakeId() {
        return fakeId;
    }

    public void setFakeId(String fakeId) {
        this.fakeId = fakeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsApprove() {
        return isApprove;
    }

    public void setIsApprove(Integer isApprove) {
        this.isApprove = isApprove;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getBindTime() {
        return bindTime;
    }

    public void setBindTime(Timestamp bindTime) {
        this.bindTime = bindTime;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Timestamp getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Timestamp accessTime) {
        this.accessTime = accessTime;
    }

    public Timestamp getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Timestamp releaseTime) {
        this.releaseTime = releaseTime;
    }

}
