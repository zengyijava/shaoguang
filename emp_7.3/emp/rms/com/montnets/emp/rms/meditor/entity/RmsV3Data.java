package com.montnets.emp.rms.meditor.entity;

import java.util.ArrayList;

/**
 * V3Data
 */
public class RmsV3Data {

    private String  tag;
    private String  type;
    private String  text;
    private String   src;
    private String   paramText;
    private RmsV3Element image;
    private RmsV3Element chart;
    private boolean active;
    private Integer borderRadius;
    private ArrayList textEditable;
    private String width;
    private Integer h;
    private Integer w;
    private String height;
    private String isShowImgText;
    private Double duration;
    private String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getBorderRadius() {
        return borderRadius;
    }

    public void setBorderRadius(Integer borderRadius) {
        this.borderRadius = borderRadius;
    }

    public ArrayList getTextEditable() {
        return textEditable;
    }

    public void setTextEditable(ArrayList textEditable) {
        this.textEditable = textEditable;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public Integer getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getIsShowImgText() {
        return isShowImgText;
    }

    public void setIsShowImgText(String isShowImgText) {
        this.isShowImgText = isShowImgText;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public RmsV3Element getChart() {
        return chart;
    }

    public void setChart(RmsV3Element chart) {
        this.chart = chart;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public RmsV3Element getImage() {
        return image;
    }

    public void setImage(RmsV3Element image) {
        this.image = image;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

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

    public String getParamText() {
        return paramText;
    }

    public void setParamText(String paramText) {
        this.paramText = paramText;
    }
}

