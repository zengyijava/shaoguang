/**
 * Program  : TableAMnp.java
 * Author   : zousy
 * Create   : 2014-3-24 下午02:25:59
 * company ShenZhen Montnets Technology CO.,LTD.
 */

package com.montnets.emp.table.wy;

import java.util.HashMap;
import java.util.Map;

/**
 * 携号转网表
 *
 * @author zousy <zousy999@qq.com>
 * @version 1.0.0
 * @2014-3-24 下午02:25:59
 */
public class TableAMnp {

    public static final String TABLE_NAME = "A_MNP";
    public static final String ID = "ID";
    /**
     * 手机号
     */
    public static final String PHONE = "PHONE";
    /**
     * 转网前的运营商类型
     */
    public static final String UNICOM = "UNICOM";
    /**
     * 转网后的运营商类型
     */
    public static final String PHONETYPE = "PHONETYPE";
    /**
     * 录入类型(0 手动，1 系统自动)
     */
    public static final String ADDTYPE = "ADDTYPE";
    /**
     * 0 增加，1 删除
     */
    public static final String OPTTYPE = "OPTTYPE";
    /**
     * 信息更新时间
     */
    public static final String CREATETIME = "CREATETIME";
    /**
     * 序列
     */
    public static final String SEQUENCE = "SEQ_A_MNP";
    /**
     * 映射集合
     */
    protected static final Map<String, String> columns = new HashMap<String, String>();

    static {
        columns.put("AMnp", TABLE_NAME);
        columns.put("sequence", SEQUENCE);
        columns.put("tableId", ID);
        columns.put("id", ID);
        columns.put("phone", PHONE);
        columns.put("unicom", UNICOM);
        columns.put("phoneType", PHONETYPE);
        columns.put("addType", ADDTYPE);
        columns.put("optType", OPTTYPE);
        columns.put("createTime", CREATETIME);
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

