package com.montnets.emp.entity.corp;

import java.io.Serializable;

/**
 * @author hsh <lensener@foxmail.com>
 * @project montnets_entity
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2018-10-19 下午02:53:36
 * @description 前端配置表
 */
public class LfWebconfig implements Serializable {


    private static final long serialVersionUID = 7245155906763633868L;
    /**
     * 标识ID
     */
    private Long id;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置
     */
    private String jsonConfig;

    /**
     * 企业编码
     */
    private String corpCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getJsonConfig() {
        return jsonConfig;
    }

    public void setJsonConfig(String jsonConfig) {
        this.jsonConfig = jsonConfig;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    @Override
    public String toString() {
        return "LfWebconfig{" +
                "id=" + id +
                ", configName='" + configName + '\'' +
                ", jsonConfig='" + jsonConfig + '\'' +
                ", corpCode='" + corpCode + '\'' +
                '}';
    }
}
