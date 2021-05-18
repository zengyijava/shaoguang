package com.montnets.emp.reportform.dto;

import java.util.List;

/**
 * 将查询的省市信息转化为对应的Json
 * @date 2018-12-11 16:07:13
 * @author Chenguang
 */
public class AprovinceCityDto {
    private String value = "";
    private String label;
    private List<AprovinceCityDto> children;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<AprovinceCityDto> getChildren() {
        return children;
    }

    public void setChildren(List<AprovinceCityDto> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AprovinceCityDto) {
            AprovinceCityDto aprovinceCityDto= (AprovinceCityDto) obj;
            return value.equalsIgnoreCase(aprovinceCityDto.getValue()) &&
                    label.equals(aprovinceCityDto.getLabel());
        }
        return false;
    }
}
