/**
 *
 */
package com.montnets.emp.table.weix;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信关键字表（LF_WC_KEYWORD）
 *
 * @author fangyt <foyoto@gmail.com>
 * @project p_weix
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWcKeyword {

    public static final String K_ID = "K_ID";
    /**
     * 角色名称
     */
    public static final String NAME = "NAME";
    /**
     * 内容
     */
    public static final String TYPE = "TYPE";
    /**
     * 企业编码
     */
    public static final String A_ID = "A_ID";
    /**
     * 创建该角色者的guid
     */
    public static final String CORP_CODE = "CORP_CODE";
    /**
     * 更新时间
     */
    public static final String CREATETIME = "CREATETIME";
    /**
     * 更新时间
     */
    public static final String MODIFYTIME = "MODIFYTIME";
    /**
     * 序列
     */
    public static final String SEQUENCE = "511";
    /**
     * 集合
     */
    protected static final Map<String, String> columns = new HashMap<String, String>();
    public static final String TABLE_NAME = "LF_WC_KEYWORD";

    static {
        columns.put("LfWcKeyword", TABLE_NAME);
        columns.put("tableId", K_ID);
        columns.put("sequence", SEQUENCE);
        columns.put("KId", K_ID);
        columns.put("name", NAME);
        columns.put("type", TYPE);
        columns.put("AId", A_ID);
        columns.put("corpCode", CORP_CODE);
        columns.put("createtime", CREATETIME);
        columns.put("modifytime", MODIFYTIME);

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
