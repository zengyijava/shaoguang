package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;

/**
 * @Description: 地图相关实体类
 * @Auther:xuty
 * @Date: 2018/7/31 17:31
 */
public class Map implements Serializable {
    
    //应用包名，这里可以指定使用那个地图
    private String pkg;
    //地图纬度，double类型
    private double lat;
    //地图经度，double类型
    private double lon;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
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
