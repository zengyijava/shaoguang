package com.montnets.emp.rms.meditor.entity;

/**
 * @Description: H5 封面实体类
 * @Auther:xuty
 * @Date: 2018/9/21 14:35
 */
public class H5App {
    //标题
    private H5DataElement title;
    //描述
    private H5DataElement description;
    //封面
    private  H5DataElement cover;
    //宽
    private int width;
    //高
    private int height;
    private int maxLine;


    public H5DataElement getTitle() {
        return title;
    }

    public void setTitle(H5DataElement title) {
        this.title = title;
    }

    public H5DataElement getDescription() {
        return description;
    }

    public void setDescription(H5DataElement description) {
        this.description = description;
    }

    public H5DataElement getCover() {
        return cover;
    }

    public void setCover(H5DataElement cover) {
        this.cover = cover;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMaxLine() {
        return maxLine;
    }

    public void setMaxLine(int maxLine) {
        this.maxLine = maxLine;
    }
}
