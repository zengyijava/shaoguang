package com.montnets.emp.reportform.dto;

import java.util.List;

/**
 * 机构树(包含操作员)
 * @author chenguang
 * @date 20128-12-12 10:43:00
 */
public class DepOrUserTreeDto {
    /**
     * 机构或操作员的Id 为了区别 机构为org_1 操作员为mem_1
     */
    private String id;
    /**
     * 机构或操作员的名字
     */
    private String label;
    /**
     * 所包含的子机构或操作员
     */
    private List<DepOrUserTreeDto> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<DepOrUserTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<DepOrUserTreeDto> children) {
        this.children = children;
    }
}
