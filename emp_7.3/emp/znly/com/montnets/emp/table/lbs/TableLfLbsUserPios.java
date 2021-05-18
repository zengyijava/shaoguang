package com.montnets.emp.table.lbs;

import java.util.HashMap;
import java.util.Map;

public class TableLfLbsUserPios
{

    // 地理位置采集表
    public static final String              TABLE_NAME = "LF_LBS_USERPIOS";

    // 自增ID
    public static final String        UP_ID       = "UP_ID";


    // 纬度
    public static final String        LAT        = "LAT";

    // 经度
    public static final String        LNG        = "LNG";

    // 公众账号
    public static final String        A_ID       = "A_ID";
    
    public static final String        OPENID       = "OPENID";

    // 企业编码
    public static final String        CORP_CODE  = "CORP_CODE";

    // 最近更新时间
    public static final String        MODITYTIME = "MODITYTIME";
    
    // 序列
    public static final String        SEQUENCE   = "S_LF_LBS_USERPIOS";

    protected static final Map<String, String> columns    = new HashMap<String, String>();

    static
    {
        columns.put("LfLbsUserPios", TABLE_NAME);
        columns.put("tableId",UP_ID);
        columns.put("sequence", SEQUENCE);
        columns.put("upid", UP_ID);
        columns.put("lat", LAT);
        columns.put("lng", LNG);
        columns.put("AId", A_ID);
        columns.put("openid", OPENID);
        columns.put("corpCode", CORP_CODE);
        columns.put("modifytime", MODITYTIME);
        
    };

    /**
     * 返回实体类字段与数据库字段实体类映射的map集合
     * 
     * @return
     */
    public static Map<String, String> getORM()
    {
        return columns;
    }

}
