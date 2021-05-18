package com.montnets.emp.rms.meditor.vo;

import com.montnets.emp.rms.meditor.entity.LfFodder;

import java.util.List;

public class LfFodderVo {
    private List<LfFodder> list;
    private int totalPage;
    private int totalRecord;

    public List<LfFodder> getList() {
        return list;
    }

    public void setList(List<LfFodder> list) {
        this.list = list;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }
}
