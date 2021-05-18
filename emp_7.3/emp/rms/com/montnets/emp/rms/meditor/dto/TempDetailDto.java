package com.montnets.emp.rms.meditor.dto;

import java.util.List;

public class TempDetailDto {
    private Integer industryId;
    private Integer useId;
    private Integer tmpType;
    private List list;
    private List paramArr;
    private Long sptemplid;
    private String tmName;
    private Long dsflag;
    private String ver;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public Long getDsflag() {
        return dsflag;
    }

    public void setDsflag(Long dsflag) {
        this.dsflag = dsflag;
    }

    public String getTmName() {
        return tmName;
    }

    public void setTmName(String tmName) {
        this.tmName = tmName;
    }

    public Long getSptemplid() {
        return sptemplid;
    }

    public void setSptemplid(Long sptemplid) {
        this.sptemplid = sptemplid;
    }

    public List getParamArr() {
        return paramArr;
    }

    public void setParamArr(List paramArr) {
        this.paramArr = paramArr;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }

    public Integer getUseId() {
        return useId;
    }

    public void setUseId(Integer useId) {
        this.useId = useId;
    }

    public Integer getTmpType() {
        return tmpType;
    }

    public void setTmpType(Integer tmpType) {
        this.tmpType = tmpType;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "TempDetailDto{" +
                "industryId=" + industryId +
                ", useId=" + useId +
                ", tmpType=" + tmpType +
                ", list=" + list +
                ", paramArr=" + paramArr +
                ", sptemplid=" + sptemplid +
                ", tmName='" + tmName + '\'' +
                ", dsflag=" + dsflag +
                ", ver='" + ver + '\'' +
                '}';
    }
}
