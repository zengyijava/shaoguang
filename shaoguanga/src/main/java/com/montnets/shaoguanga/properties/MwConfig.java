package com.montnets.shaoguanga.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author WJH
 * @Description
 * @date 2021/4/7 17:16
 * @Email ibytecode2020@gmail.com
 */
@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "mw.conf")
public class MwConfig {
    private Map<String, String> spMap;
    private String wgUrl;
    private String sendSingle;
    private String sendBatch;
    private String getRpt;
    private String gatekeeper;
    private boolean needRpt;
    private Integer splitCount;
    private String username;
    private String passwd;

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    private Integer timeout;

    @Override
    public String toString() {
        return "MwConfig{" +
                "spMap=" + spMap +
                ", wgUrl='" + wgUrl + '\'' +
                ", sendSingle='" + sendSingle + '\'' +
                ", sendBatch='" + sendBatch + '\'' +
                ", getRpt='" + getRpt + '\'' +
                ", gatekeeper='" + gatekeeper + '\'' +
                ", needRpt=" + needRpt +
                ", splitCount=" + splitCount +
                ", username='" + username + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public Integer getSplitCount() {
        return splitCount;
    }

    public void setSplitCount(Integer splitCount) {
        this.splitCount = splitCount;
    }

    public boolean isNeedRpt() {
        return needRpt;
    }

    public void setNeedRpt(boolean needRpt) {
        this.needRpt = needRpt;
    }

    public String getGatekeeper() {
        return gatekeeper;
    }

    public void setGatekeeper(String gatekeeper) {
        this.gatekeeper = gatekeeper;
    }

    public Map<String, String> getSpMap() {
        return spMap;
    }

    public void setSpMap(Map<String, String> spMap) {
        this.spMap = spMap;
    }

    public String getWgUrl() {
        return wgUrl;
    }

    public void setWgUrl(String wgUrl) {
        this.wgUrl = wgUrl;
    }

    public String getSendSingle() {
        return sendSingle;
    }

    public void setSendSingle(String sendSingle) {
        this.sendSingle = sendSingle;
    }

    public String getSendBatch() {
        return sendBatch;
    }

    public void setSendBatch(String sendBatch) {
        this.sendBatch = sendBatch;
    }

    public String getGetRpt() { return getRpt; }

    public void setGetRpt(String getRpt) { this.getRpt = getRpt; }
}
