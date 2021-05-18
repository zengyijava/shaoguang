package com.montnets.emp.rms.meditor.dto;

import com.montnets.emp.util.PageInfo;

public class PageListDto {

    private PageInfo pageInfo;

    private Object ob;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public Object getOb() {
        return ob;
    }

    public void setOb(Object ob) {
        this.ob = ob;
    }

    @Override
    public String toString() {
        return "PageListDto{" +
                "pageInfo=" + pageInfo +
                ", ob=" + ob +
                '}';
    }
}
