package com.montnets.emp.ottbase.constant;
/**
 * 
 * @description     百度请求url
 * @project ott 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-12-19 上午08:38:45
 */
public class BaiduHttpUrl
{
    /**
     * 驾车—起/终点模糊检索
     * 请求事例
     * http://api.map.baidu.com/direction/v1?mode=driving&origin=上地五街&destination=北京大学&origin_region
        =北京&destination_region=北京&output=json&ak=E4805d16520de693a3fe707cdc962045
     */
    public final static String DIRECTION_BUS_URL  = "http://api.map.baidu.com/direction/v1?";
    
    /**
     *  地理位置解析URL
     */
    public final static String GEOCODER_LOCATION_URL  = "http://api.map.baidu.com/geocoder/v2/?";

}
