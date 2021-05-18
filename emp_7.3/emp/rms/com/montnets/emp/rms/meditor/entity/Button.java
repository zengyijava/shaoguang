package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;

public class Button implements Serializable {

    private static final long serialVersionUID = 974580276229286551L;

    /**
     *
     */
    //组件tag
    private String tag;
    //组件的内容
    private String content;
    //点击操作的行为动作：1.打开大图、2.打开链接、3.复制、4.打开APP、5.打开地图、6.拨打电话、7.打开快应用
    private String action;
    //action点击操作的目标内容，可以表示为：大图url地址、网页url地址、电话号码、需要复制的内容
    private String target;
    //打开地图的参数
    private ButtonMap map;
    //打开APP或快应用的参数
    private App app;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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

    public ButtonMap getMap() {
        return map;
    }

    public void setMap(ButtonMap map) {
        this.map = map;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }


}
