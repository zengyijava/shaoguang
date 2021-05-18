package com.montnets.emp.table.lbs;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yejiangmin <282905282@qq.com>
 * @description 地理位置采集表
 * @project ott
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-28 下午02:46:08
 */
public class TableLfLbsPios {

    // 地理位置采集表
    public static final String TABLE_NAME = "LF_LBS_PIOS";

    // 自增ID
    public static final String P_ID = "P_ID";

    // 城市ID
    public static final String CITY_ID = "CITY_ID";

    // 纬度
    public static final String LAT = "LAT";

    // 经度
    public static final String LNG = "LNG";

    // 标题
    public static final String TITLE = "TITLE";

    // 关键字
    public static final String KEYWORD = "KEYWORD";

    // 座机电话
    public static final String TELEPHONE = "TELEPHONE";

    // 地址
    public static final String ADDRESS = "ADDRESS";

    // 描述
    public static final String NOTE = "NOTE";

    // 公众账号
    public static final String A_ID = "A_ID";

    // 企业编码
    public static final String CORP_CODE = "CORP_CODE";

    // 创建时间
    public static final String CREATETIME = "CREATETIME";

    // 最近更新时间
    public static final String MODITYTIME = "MODITYTIME";

    public static final String DISTANCE = "DISTANCE";
    // 序列
    public static final String SEQUENCE = "S_LF_LBS_PIOS";

    protected static final Map<String, String> columns = new HashMap<String, String>();

    static {
        columns.put("LfLbsPios", TABLE_NAME);
        columns.put("tableId", P_ID);
        columns.put("sequence", SEQUENCE);
        columns.put("pid", P_ID);
        columns.put("cityid", CITY_ID);
        columns.put("lat", LAT);
        columns.put("lng", LNG);
        columns.put("title", TITLE);
        columns.put("keyword", KEYWORD);
        columns.put("telephone", TELEPHONE);
        columns.put("address", ADDRESS);
        columns.put("note", NOTE);
        columns.put("AId", A_ID);
        columns.put("corpCode", CORP_CODE);
        columns.put("createtime", CREATETIME);
        columns.put("modifytime", MODITYTIME);
        columns.put("distance", DISTANCE);


    }

    ;

    /**
     * 返回实体类字段与数据库字段实体类映射的map集合
     *
     * @return
     */
    public static Map<String, String> getORM() {
        return columns;
    }

}
