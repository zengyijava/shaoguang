package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;

public class ButtonMap implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1371982980643146189L;
    //应用包名,指定使用那个地图
    private String pkg;
    //地图纬度
    private String lat;
    //地图经度
    private String lon;
    //地图显示的title
    private String title;
    //位置坐标的地址
    private String addr;

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

}
