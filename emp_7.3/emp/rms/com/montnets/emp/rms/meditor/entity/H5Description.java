package com.montnets.emp.rms.meditor.entity;

/**
 * @Description:H5 描述实体类
 * @Auther:xuty
 * @Date: 2018/9/21 14:39
 */
public class H5Description {
    //类型
    private String type;
    //描述
    private String text;

    //样式
    private H5Style style;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public H5Style getStyle() {
        return style;
    }

    public void setStyle(H5Style style) {
        this.style = style;
    }
}
