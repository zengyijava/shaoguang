package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 前端模板父类
 *
 * @author dell
 */
public class TempData implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3066457291691870852L;

    private Long tmid;

    //模板集合
    private List<SubTempData> tempArr;
    //模板类型
    private Integer tmpType;
    //模板名称
    private String tmName;
    //行业ID
    private Integer industryId;
    //用途ID
    private Integer useId;
    //模板状态 1.启用，0.禁用
    private Integer tmState;
    //操作类型 0保存 1提交审核
    private Integer subType;
    //参数
    private List<TempDataParam> paramArr;

    private Integer editorWidth;

    //是否公共场景
    private Integer isPublic;

    //H5 封面
    private H5App app;

    public List<SubTempData> getTempArr() {
        return tempArr;
    }

    public Long getTmid() {
        return tmid;
    }

    public void setTmid(Long tmid) {
        this.tmid = tmid;
    }

    public void setTempArr(List<SubTempData> tempArr) {
        this.tempArr = tempArr;
    }

    public Integer getTmpType() {
        return tmpType;
    }

    public void setTmpType(Integer tmpType) {
        this.tmpType = tmpType;
    }

    public String getTmName() {
        return tmName;
    }

    public void setTmName(String tmName) {
        this.tmName = tmName;
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

    public Integer getTmState() {
        return tmState;
    }

    public void setTmState(Integer tmState) {
        this.tmState = tmState;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public List<TempDataParam> getParamArr() {
        return paramArr;
    }

    public void setParamArr(List<TempDataParam> paramArr) {
        this.paramArr = paramArr;
    }

    public Integer getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getEditorWidth() {
        return editorWidth;
    }

    public void setEditorWidth(Integer editorWidth) {
        this.editorWidth = editorWidth;
    }

    public H5App getApp() {
        return app;
    }

    public void setApp(H5App app) {
        this.app = app;
    }
}
