package com.montnets.emp.rms.meditor.dto;

import java.util.List;

public class ListDtoData {
    private Integer totalPage;
    private Integer totalRecord;
    private String rule;
    private List list;

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ListDtoData{" +
                "totalPage=" + totalPage +
                ", totalRecord=" + totalRecord +
                ", rule='" + rule + '\'' +
                ", list=" + list +
                '}';
    }
}
