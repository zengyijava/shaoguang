package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;

/**
 * @Author:yangdl
 * @Data:Created in 14:02 2018.8.8 008
 */
public class SendParam implements Serializable {

    private static final long serialVersionUID = 4319462952177860745L;

    //参数类型：1-运营商富信参数,2-表示h5页面参数,3-表示OTT富信(卡片)参数&富文本参数，summary-短信
    private String type;
    //参数内容
    private Object content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
