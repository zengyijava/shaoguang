package com.montnets.emp.rms.meditor.entity;

/**
 * @Description:
 * @Auther:xuty
 * @Date: 2018/9/25 11:28
 */
public class H5DataElement { //类型
    private String type;
    //标题
    private String text;
    //封面图片src
    private String src;
    //样式
    private H5Style style;

    //组建ID
    private String tag;

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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public H5Style getStyle() {
        return style;
    }

    public void setStyle(H5Style style) {
        this.style = style;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
