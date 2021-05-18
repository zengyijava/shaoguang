package com.montnets.emp.table.weix;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信公众帐号关系表（LF_WC_ALINK）
 *
 * @author fangyt <foyoto@gmail.com>
 * @project p_weix
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWcAlink {
    /**
     * EMP微信公众帐号ID
     */
    public static final String A_ID = "A_ID";
    /**
     * EMP微信用户ID
     */
    public static final String WC_ID = "WC_ID";
    /**
     * EMP微信用户openid
     */
    public static final String OPEN_ID = "OPEN_ID";
    /**
     * 序列
     */
    public static final String SEQUENCE = "S_LF_WC_ALINK";
    /**
     * 映射集合
     */
    protected final static Map<String, String> columns = new HashMap<String, String>();
    /**
     * 表名：微信关键字与回复模板关联表
     */
    public static final String TABLE_NAME = "LF_WC_ALINK";

    static {
        columns.put("LfWcAlink", TABLE_NAME);
        columns.put("sequence", SEQUENCE);
        columns.put("wcId", WC_ID);
        columns.put("AId", A_ID);
        columns.put("openId", OPEN_ID);
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
