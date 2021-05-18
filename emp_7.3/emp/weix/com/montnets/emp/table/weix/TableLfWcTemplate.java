package com.montnets.emp.table.weix;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信模版表（LF_WC_TEMPLATE）
 *
 * @author fangyt <foyoto@gmail.com>
 * @project p_weix
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWcTemplate {

    /**
     * 模板ID
     */
    public static final String T_ID = "T_ID";
    /**
     * 模板名称
     */
    public static final String NAME = "T_NAME";
    /**
     * 消息的类型（0：文本回复；1：单图文回复；2：多图文回复；3：语音回复；）
     */
    public static final String MSG_TYPE = "MSG_TYPE";
    /**
     * 返回数据需要生成的XML格式
     */
    public static final String MSG_XML = "MSG_XML";
    /**
     * 内容摘要，界面显示用
     */
    public static final String MSG_TEXT = "MSG_TEXT";
    /**
     * 是否共用
     */
    public static final String IS_PUBLIC = "IS_PUBLIC";
    /**
     * 是否草稿
     */
    public static final String IS_DRAFT = "IS_DRAFT";
    /**
     * 0：静态；1：动态
     */
    public static final String IS_DYNAMIC = "IS_DYNAMIC";
    /**
     * 公众帐号的ID（如果为空，为该企业所有公众账号共有）
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
     * 更新时间
     */
    public static final String MODIFYTIME = "MODIFYTIME";
    /**
     * 关联图文id（多个id以“，”隔开）
     */
    public static final String RIMGIDS = "RIMG_IDS";
    /**
     * 关联关键字
     */
    public static final String KEY_WORDSVO = "KEY_WORDSVO";
    /**
     * 序列
     */
    public static final String SEQUENCE = "510";
    public static final String TABLE_NAME = "LF_WC_TEMPLATE";
    /**
     * 映射集合
     */
    protected static final Map<String , String> columns = new HashMap<String, String>();

    static {
        columns.put("LfWcTemplate", TABLE_NAME);
        columns.put("tableId", T_ID);
        columns.put("sequence", SEQUENCE);
        columns.put("TId", T_ID);
        columns.put("name", NAME);
        columns.put("msgType", MSG_TYPE);
        columns.put("msgXml", MSG_XML);
        columns.put("msgText", MSG_TEXT);
        columns.put("isPublic", IS_PUBLIC);
        columns.put("isDraft", IS_DRAFT);
        columns.put("isDynamic", IS_DYNAMIC);
        columns.put("AId", A_ID);
        columns.put("corpCode", CORP_CODE);
        columns.put("createtime", CREATETIME);
        columns.put("modifytime", MODIFYTIME);
        columns.put("rimgids", RIMGIDS);
        columns.put("keywordsvo", KEY_WORDSVO);

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
