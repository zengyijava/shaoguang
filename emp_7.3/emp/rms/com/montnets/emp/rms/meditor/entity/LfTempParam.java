package com.montnets.emp.rms.meditor.entity;

/**
 * 模板参数表
 */
public class LfTempParam {
    /**
     * 主键
     */
    private Long id;
    /**
     * 模板id
     */
    private Long tmId;
    /**
     * 参数名称
     */
    private String name;
    /**
     * 最大长度
     */
    private Integer maxLength;
    /**
     * 最小长度
     */
    private Integer minLength;
    /**
     * 参数类型
     */
    private Integer type;
    /**
     * 长度约束 0可变 1固定
     */
    private Integer lengthRestrict;

    /**
     * 参数正则
     */
    private String regContent;

    /**
     * 固定长度
     */
    private Integer fixLength;
    /**
     *
     * 参数是否有长度
     */
    private Integer hasLength;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTmId() {
        return tmId;
    }

    public void setTmId(Long tmId) {
        this.tmId = tmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLengthRestrict() {
        return lengthRestrict;
    }

    public void setLengthRestrict(Integer lengthRestrict) {
        this.lengthRestrict = lengthRestrict;
    }

    public Integer getFixLength() {
        return fixLength;
    }

    public void setFixLength(Integer fixLength) {
        this.fixLength = fixLength;
    }

    public String getRegContent() {
        return regContent;
    }

    public void setRegContent(String regContent) {
        this.regContent = regContent;
    }

    public Integer getHasLength() {
        return hasLength;
    }

    public void setHasLength(Integer hasLength) {
        this.hasLength = hasLength;
    }

    @Override
    public String toString() {
        return "LfTempParam{" +
                "id=" + id +
                ", tmId=" + tmId +
                ", name='" + name + '\'' +
                ", maxLength=" + maxLength +
                ", minLength=" + minLength +
                ", type=" + type +
                ", lengthRestrict=" + lengthRestrict +
                ", regContent='" + regContent + '\'' +
                ", fixLength=" + fixLength +
                ", hasLength=" + hasLength +
                '}';
    }
}
