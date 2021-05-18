package com.montnets.emp.txgl.corpmanage.dto;

import java.util.List;

public class CorpMultimediaDto {
    private Integer multimedia;
    private List modulePer;

    public Integer getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(Integer multimedia) {
        this.multimedia = multimedia;
    }

    public List getModulePer() {
        return modulePer;
    }

    public void setModulePer(List modulePer) {
        this.modulePer = modulePer;
    }

    @Override
    public String toString() {
        return "CorpMultimediaDto{" +
                "multimedia=" + multimedia +
                ", modulePer=" + modulePer +
                '}';
    }
}
