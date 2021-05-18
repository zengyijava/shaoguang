package com.montnets.emp.rms.meditor.entity;

/**
 * @Description: 模板每一页中的每一个元素对象
 * @Auther:xuty
 * @Date: 2018/9/21 15:23
 */
public class H5Element {
    private boolean active;
    private String type;
    private String tag;
    private int x;
    private int y;
    private int z;
    private int w;
    private H5Style style;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public H5Style getStyle() {
        return style;
    }

    public void setStyle(H5Style style) {
        this.style = style;
    }
}
