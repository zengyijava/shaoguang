package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;

/**
 * @Description:
 * @Auther:xuty
 * @Date: 2018/7/31 17:28
 */
public class Event implements Serializable {
    //点击操作的行为动作：1.打开大图、2.打开链接、3.复制、4.打开APP、5.打开地图、6.拨打电话、7.打开快应用，详情转3.5
    private String action;
    //ction点击操作的目标内容，可以表示为：大图url地址、网页url地址、电话号码、需要复制的内容
    private String target;
    //打开地图的参数
    private Map map;
    //打开APP或快应用的参数
    private App app;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }
}
