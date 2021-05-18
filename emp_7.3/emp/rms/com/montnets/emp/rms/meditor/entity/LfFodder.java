package com.montnets.emp.rms.meditor.entity;

public class LfFodder {
    //自增ID
    private Long id;
    //操作员userid
    private Long userId;
    //资源服务器相对路径
    private String url;
    //2:图片 3:视频 4：音频
    private Long foType;
    //文件大小
    private Long foSize;
    //宽
    private Long width = 0L;
    //高
    private Long height = 0L;
    //宽高比例
    private String radio = "";
    //时长
    private Long duration = 0L;
    //名称
    private String original;
    //启用/禁用,1表示启用
    private Integer status = 1;

    //视频首帧图路径
    private String fistFramePath = "";


    public String getFistFramePath() {
        return fistFramePath;
    }

    public void setFistFramePath(String fistFramePath) {
        this.fistFramePath = fistFramePath;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getFoType() {
        return foType;
    }

    public void setFoType(Long foType) {
        this.foType = foType;
    }

    public Long getFoSize() {
        return foSize;
    }

    public void setFoSize(Long foSize) {
        this.foSize = foSize;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public String getRadio() {
        return radio;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}
