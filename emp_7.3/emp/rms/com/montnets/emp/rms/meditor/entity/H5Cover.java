package com.montnets.emp.rms.meditor.entity;

/**
 * @Description: H5 封面实体类
 * @Auther:xuty
 * @Date: 2018/9/21 14:39
 */
public class H5Cover {
    //类型
    private String type;
    //封面图片路径
    private String src;

    //样式
    private H5Style style;

    public String getType() {

        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
