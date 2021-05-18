package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;

/**
 * @Description: 模板JSON 结构字段存储实体类，将模板结构 按照 每 1000字 存储一条记录
 * @Auther:xuty
 * @Date: 2018/9/10 14:49
 */
public class LfTempContent implements Serializable {
    //自增ID
    private Long id;
    //模板表的自增ID
    private Long tmId;
    //JSON 类型， 1-前端JSON，2-终端JSON
    private Integer contType;
    //模板类型 11-富媒体，12-卡片，13-富文本
    private Integer tmpType;
    //模板结构JSON 按 每1000字 存储的内容
    private String tmpContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTmId() {
        return tmId;
    }

    public void setTmId(Long tmId) {
        this.tmId = tmId;
    }

    public Integer getContType() {
        return contType;
    }

    public void setContType(Integer contType) {
        this.contType = contType;
    }

    public Integer getTmpType() {
        return tmpType;
    }

    public void setTmpType(Integer tmpType) {
        this.tmpType = tmpType;
    }

    public String getTmpContent() {
        return tmpContent;
    }

    public void setTmpContent(String tmpContent) {
        this.tmpContent = tmpContent;
    }
}
