package com.montnets.emp.rms.meditor.entity;

import java.util.List;

/**
 * @Description: H5 模板内容实体类
 * @Auther:xuty
 * @Date: 2018/9/21 15:00
 */
public class H5Content {
    //H5模板每一页对象
    private List<H5Page> pages;
    //H5 翻页动画
    private H5Animation animation;

    public List<H5Page> getPages() {
        return pages;
    }

    public void setPages(List<H5Page> pages) {
        this.pages = pages;
    }

    public H5Animation getAnimation() {
        return animation;
    }

    public void setAnimation(H5Animation animation) {
        this.animation = animation;
    }
}
