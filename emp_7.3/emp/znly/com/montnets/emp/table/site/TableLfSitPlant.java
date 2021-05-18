package com.montnets.emp.table.site;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfSitPlant
 * 
 * @project p_sit
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfSitPlant
{
    // 表名
    public static final String              TABLE_NAME   = "LF_SIT_PLANT";

    // 程序自增ID
    public static final String        PLANT_ID     = "PLANT_ID";

    // 板块的类型（1：滚动head，2联系方式link，3：内容列表List，4底部bottom）
    public static final String        PLANT_TYPE   = "PLANT_TYPE";

    // 所属页面
    public static final String        PAGE_ID      = "PAGE_ID";

    // 板块名称
    public static final String        NAME         = "NAME";

    // 板块表单标题IDS
    public static final String        FEILD_NAMES  = "FEILD_NAMES";

    // 模板表单值（格式待定）
    public static final String        FEILD_VALUES = "FEILD_VALUES";

    // 企业编码（0表示系统默认分类）
    public static final String        CORP_CODE    = "CORP_CODE";

    // 创建时间
    public static final String        CREATETIME   = "CREATETIME";

    // 更新时间
    public static final String        MODITYTIME   = "MODITYTIME";

    // 所属站点
    public static final String        S_ID         = "S_ID";

    // 序列
    public static final String        SEQUENCE     = "S_lF_SIT_PLANT";

    // 映射集合
    protected static final Map<String, String> columns      = new HashMap<String, String>();

    static
    {
        columns.put("LfSitPlant", TABLE_NAME);
        columns.put("tableId", PLANT_ID);
        columns.put("sequence", SEQUENCE);
        columns.put("plantId", PLANT_ID);
        columns.put("plantType", PLANT_TYPE);
        columns.put("pageId", PAGE_ID);
        columns.put("name", NAME);
        columns.put("feildNames", FEILD_NAMES);
        columns.put("feildValues", FEILD_VALUES);
        columns.put("corpCode", CORP_CODE);
        columns.put("createtime", CREATETIME);
        columns.put("moditytime", MODITYTIME);
        columns.put("sId", S_ID);
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
