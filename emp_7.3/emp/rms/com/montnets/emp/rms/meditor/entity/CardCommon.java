package com.montnets.emp.rms.meditor.entity;


import java.io.Serializable;

/**
 * @author xuty
 * @version V1.0
 * @ClassName: CardCommon
 * @Description:卡片JSON转换公共类
 * @date 2018-7-26 上午11:44:40
 */
public class CardCommon implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6950943063359239686L;
    //模板ID
    private String id;
    //结构ID
    private String sid;
    //模板类型 1.普通卡片 2.html格式富文本
    private Integer temptype;

    //模板结构
    private Template template;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Integer getTemptype() {
        return temptype;
    }

    public void setTemptype(Integer temptype) {
        this.temptype = temptype;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }
}
