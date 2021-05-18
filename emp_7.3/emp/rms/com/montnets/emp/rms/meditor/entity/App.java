package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;

public class App implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3852048065337950L;
    //应用包名，打开指定应用的唯一标识
    private String pkg;
    //指定路径，如：快应用的path(如：user/info)、打开APP(如：com.montnets.messaging.MainActivity)
    private String path;
    //指定参数,仅用在打开快应用，格式如：
    //param1=value1&param2=value2
    private String param;

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

}
