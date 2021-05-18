package com.montnets.emp.table.weix;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信关键字与回复模板关联表（LF_WC_TLINK）
 *
 * @author fangyt <foyoto@gmail.com>
 * @project p_weix
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWcTlink {

    /**
     * 关键字编号
     */
    public static final String K_ID = "K_ID";
    /**
     * 模板编号
     */
    public static final String T_ID = "T_ID";
    /**
     * 序列
     */
    public static final String SEQUENCE = "S_LF_WC_TLINK";
    public static final String TABLE_NAME = "LF_WC_TLINK";
    /**
     * 映射集合
     */
    protected static final Map<String, String> columns = new HashMap<String, String>();

    static {
        columns.put("LfWcTlink", TABLE_NAME);
        columns.put("sequence", SEQUENCE);
        columns.put("KId", K_ID);
        columns.put("TId", T_ID);

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
