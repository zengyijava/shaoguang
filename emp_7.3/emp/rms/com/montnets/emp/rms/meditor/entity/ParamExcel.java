package com.montnets.emp.rms.meditor.entity;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @Author:yangdl
 * @Data:Created in 11:34 2018.8.22 022
 */
public class ParamExcel {

    private String type;

    private String order;

    private LinkedHashMap<String,String> map;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public LinkedHashMap<String, String> getMap() {
        return map;
    }

    public void setMap(LinkedHashMap<String, String> map) {
        this.map = map;
    }


}
