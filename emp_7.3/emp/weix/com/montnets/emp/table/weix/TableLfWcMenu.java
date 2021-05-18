package com.montnets.emp.table.weix;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信菜单（LF_WC_MENU）
 *
 * @author fangyt <foyoto@gmail.com>
 * @project p_weix
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWcMenu {
    /**
     * 菜单ID
     */
    public static final String M_ID = "M_ID";
    /**
     * 菜单名
     */
    public static final String M_NAME = "M_NAME";
    /**
     * 类型(1:发送消息click；2：转到连接view)
     */
    public static final String M_TYPE = "M_TYPE";
    /**
     * 菜单标示 ，自定义菜单唯一标识
     */
    public static final String M_KEY = "M_KEY";
    /**
     * 自动回复的URL
     */
    public static final String M_URL = "M_URL";
    /**
     * 是否隐藏
     */
    public static final String M_HIDDEN = "M_HIDDEN";
    /**
     * 菜单的顺序编号
     */
    public static final String M_ORDER = "M_ORDER";
    /**
     * 父ID
     */
    public static final String P_ID = "P_ID";
    /**
     * 模板Id
     */
    public static final String T_ID = "T_ID";
    /**
     * 内容摘要，界面显示用
     */
    public static final String MSG_TEXT = "MSG_TEXT";
    /**
     * 返回数据需要生成的XML格式
     */
    public static final String MSG_XML = "MSG_XML";
    /**
     * 公众帐号的ID
     */
    public static final String A_ID = "A_ID";
    /**
     * 企业编号
     */
    public static final String CORP_CODE = "CORP_CODE";
    /**
     * 创建时间
     */
    public static final String CREATETIME = "CREATETIME";
    /**
     * 序列
     */
    public static final String SEQUENCE = "520";
    /**
     * 表名：微信自定义菜单表
     */
    public static final String TABLE_NAME = "LF_WC_MENU";
    /**
     * 映射集合
     */
    protected static final Map<String , String> columns = new HashMap<String, String>();

    static {
        columns.put("LfWcMenu", TABLE_NAME);
        columns.put("tableId", M_ID);
        columns.put("MId", M_ID);
        columns.put("mname", M_NAME);
        columns.put("mtype", M_TYPE);
        columns.put("mkey", M_KEY);
        columns.put("murl", M_URL);
        columns.put("mhidden", M_HIDDEN);
        columns.put("morder", M_ORDER);
        columns.put("PId", P_ID);
        columns.put("TId", T_ID);
        columns.put("msgText", MSG_TEXT);
        columns.put("msgXml", MSG_XML);
        columns.put("AId", A_ID);
        columns.put("corpCode", CORP_CODE);
        columns.put("createTime", CREATETIME);
        columns.put("sequence", SEQUENCE);
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
