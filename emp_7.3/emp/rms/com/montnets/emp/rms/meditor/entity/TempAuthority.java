package com.montnets.emp.rms.meditor.entity;

/**
 * 富信权限相关类
 */
public class TempAuthority {
    //公共场景/我的场景
    private String type;
    //添加
    private String add = "0";
    //更新
    private String update = "0";
    //删除
    private String del = "0";
    //查询
    private String query  = "0";
    //是否显示 审核状态
    private String auth  = "0";
    //立即发送
    private String send  = "0";
    // 预览
    private String preview  = "0";
    // 复制
    private String copy  = "0";
    // 导出
    private String export  = "0";
    // 详情
    private String detail  = "0";
    // 复制链接[H5才有]
    private String link  = "0";
    //启用禁用
    private String state  = "0";
    //是否快捷场景
    private String shortcut  = "0";
    //是否显示行业用途
    private String industryAndUse  = "0";
    //是否展示模板共享按钮
    private String share = "0";

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getCopy() {
        return copy;
    }

    public void setCopy(String copy) {
        this.copy = copy;
    }

    public String getExport() {
        return export;
    }

    public void setExport(String export) {
        this.export = export;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public String getIndustryAndUse() {
        return industryAndUse;
    }

    public void setIndustryAndUse(String industryAndUse) {
        this.industryAndUse = industryAndUse;
    }
}
