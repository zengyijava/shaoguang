package com.montnets.emp.rms.meditor.entity;

import java.util.ArrayList;

/**
 * V3内部类
 */
public class RmsV3Element {
    private int w;
    private int h;
    private int size;
    private String width;
    private String height;
    private String src="";
    private ArrayList textEditable;

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public ArrayList getTextEditable() {
        return textEditable;
    }

    public void setTextEditable(ArrayList textEditable) {
        this.textEditable = textEditable;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
