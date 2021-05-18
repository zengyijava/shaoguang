package com.montnets.emp.rms.meditor.entity;

import java.util.List;

/**
 * @Description: H5 每一页 实体类
 * @Auther:xuty
 * @Date: 2018/9/21 15:05
 */
public class H5Page {
    //背景图片
    private String bgSrc;
    //每一页中的每一个元素
    private List<H5Element> elements;

    private String cardHtml;

    private int degree;

    private int degreeSize;

    public String getBgSrc() {
        return bgSrc;
    }

    public void setBgSrc(String bgSrc) {
        this.bgSrc = bgSrc;
    }

    public List<H5Element> getElements() {
        return elements;
    }

    public void setElements(List<H5Element> elements) {
        this.elements = elements;
    }

    public String getCardHtml() {
        return cardHtml;
    }

    public void setCardHtml(String cardHtml) {
        this.cardHtml = cardHtml;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getDegreeSize() {
        return degreeSize;
    }

    public void setDegreeSize(int degreeSize) {
        this.degreeSize = degreeSize;
    }
}
