package com.montnets.emp.rms.meditor.entity;

/**
 * @Description:
 * @Auther:xuty
 * @Date: 2018/9/21 14:38
 */
public class H5Title {
    //类型
    private String type;
    //标题
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
