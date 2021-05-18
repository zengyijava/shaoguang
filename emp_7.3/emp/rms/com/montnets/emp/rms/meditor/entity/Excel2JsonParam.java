package com.montnets.emp.rms.meditor.entity;

import com.montnets.emp.rms.tools.Excel2JsonDto;

import java.util.List;

public class Excel2JsonParam {
    private String fmcts;

    private List<Excel2JsonDto> fminfos;

    public String getFmcts() {
        return fmcts;
    }

    public void setFmcts(String fmcts) {
        this.fmcts = fmcts;
    }

    public List<Excel2JsonDto> getFminfos() {
        return fminfos;
    }

    public void setFminfos(List<Excel2JsonDto> fminfos) {
        this.fminfos = fminfos;
    }
}
