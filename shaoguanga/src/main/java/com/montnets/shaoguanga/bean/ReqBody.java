package com.montnets.shaoguanga.bean;


import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * @Author WJH
 * @Description 请求体
 * @date 2021/4/7 14:54
 * @Email ibytecode2020@gmail.com
 */
public class ReqBody {
    @NotBlank(message = "企业名称不能为空")
    private String ecName;
    @NotBlank(message = "接口账号用户名不能为空")
    private String apId;
    @NotBlank(message = "手机号码不能为空")
    private String mobiles;
    @NotBlank(message = "短信内容不能为空")
    private String content;
    private String sign;
    @Length(max = 20, message = "扩展码不能超过20位")
    private String addSerial;
    @NotBlank(message = "参数校验序列不能为空")
    private String mac;
    @NotBlank(message = "系统类别不能为空")
    private String systemType;
    @NotBlank(message = "接口账户密码不能为空")
    private String appSecret;

    public Set<String> getMobilesList() {
        String[] mobileArr = this.mobiles.split(",");
        return new HashSet<>(Arrays.asList(mobileArr));
    }

    public String getEcName() {
        return ecName;
    }

    public void setEcName(String ecName) {
        this.ecName = ecName;
    }

    public String getApId() {
        return apId;
    }

    public void setApId(String apId) {
        this.apId = apId;
    }

    public String getMobiles() {
        return mobiles;
    }

    public void setMobiles(String mobiles) {
        this.mobiles = mobiles;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAddSerial() {
        return addSerial;
    }

    public void setAddSerial(String addSerial) {
        this.addSerial = addSerial;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getAppSecret() {
        return appSecret;
    }

    @Override
    public String toString() {
        return "ReqBody{" +
                "ecName='" + ecName + '\'' +
                ", apId='" + apId + '\'' +
                ", mobiles='" + mobiles + '\'' +
                ", content='" + content + '\'' +
                ", sign='" + sign + '\'' +
                ", addSerial='" + addSerial + '\'' +
                ", mac='" + mac + '\'' +
                ", systemType='" + systemType + '\'' +
                ", appSecret='" + appSecret + '\'' +
                '}';
    }

    public void setAppSecret(String appSecret) {

        this.appSecret = appSecret;
    }
}
