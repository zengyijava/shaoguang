package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;

/**
 * @author xuty
 * @version V1.0
 * @ClassName: Attr
 * @Description: 卡片属性类
 * @date 2018-7-26 上午11:45:19
 */
public class Attr implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8926406214022996544L;
    //卡片背景或音频背景
    private String bgsrc;
    ////字体大小，像素值
    private String size;
    //字体顔色,16进制格式
    private String color;
    //组件按下时的背景顔色，16进制格式
    private String press;
    //默认显示的文本
    private String text;
    //字体是否加粗：n 正常(默认)、b 加粗、i 斜体、u 下划线，多个样式组合用或“|”连接
    private String tstyle;
    //图片数据源,可以是url地址、base64
    private String src;
    //图片圆角,像素值px
    private String corner;
    //媒体数据源，目前仅支持文件名，文件名必须唯一
    private String msrc;
    //音视频数据源类型：1-文件名；2-url
    private String msrctype;
    //媒体的时长，单位为秒s
    private String duration;
    //图片缩略图数据源类型：1-文件名；2-url
    private String srctype;

    private String maxLine;
    //音频控件标题
    private String title;


    public String getMsrctype() {
        return msrctype;
    }

    public void setMsrctype(String msrctype) {
        this.msrctype = msrctype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMaxLine() {
        return maxLine;
    }

    public void setMaxLine(String maxLine) {
        this.maxLine = maxLine;
    }

    public String getSrctype() {
        return srctype;
    }

    public void setSrctype(String srctype) {
        this.srctype = srctype;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getCorner() {
        return corner;
    }

    public void setCorner(String corner) {
        this.corner = corner;
    }

    public String getMsrc() {
        return msrc;
    }

    public void setMsrc(String msrc) {
        this.msrc = msrc;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBgsrc() {
        return bgsrc;
    }

    public void setBgsrc(String bgsrc) {
        this.bgsrc = bgsrc;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTstyle() {
        return tstyle;
    }

    public void setTstyle(String tstyle) {
        this.tstyle = tstyle;
    }


}
