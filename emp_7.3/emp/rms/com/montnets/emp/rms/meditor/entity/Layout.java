package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;


/**
 * @author xuty
 * @version V1.0
 * @ClassName: Layout
 * @Description:卡片布局类
 * @date 2018-7-26 上午11:45:47
 */
public class Layout implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1393870147421843872L;
    //组件的x坐标
    private String x;
    //组件的y坐标
    private String y;
    //组件的宽
    private String w;
    //组件的高
    private String h;
    //组件的层级，数值越大层级越高
    private String z;
    //组件内对齐：l 左对齐(默认)、c 居中、r 右对齐、b 底部、ch 垂直居中、cv 水平居中
    private String align;

    private String maxLine;

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getMaxLine() {
        return maxLine;
    }

    public void setMaxLine(String maxLine) {
        this.maxLine = maxLine;
    }
}
