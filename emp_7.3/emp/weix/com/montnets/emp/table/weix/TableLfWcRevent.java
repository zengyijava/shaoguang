package com.montnets.emp.table.weix;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信事件回复消息（LF_WC_REVENT）
 *
 * @author fangyt <foyoto@gmail.com>
 * @project p_weix
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWcRevent {
    /**
     * 自增id
     */
    public static final String EVT_ID = "EVT_ID";
    /**
     * 消息的类型（0：无关键字默认回复；1：关注事件；2：点阅；3：取消点阅；4：CLICK(自定义菜单点击事件)；）
     */
    public static final String EVT_TYPE = "EVT_TYPE";
    /**
     * 模板Id
     */
    public static final String T_ID = "T_ID";
    /**
     * 公众帐号的ID（如果为空，为该企业所有公众帐号共有）
     */
    public static final String A_ID = "A_ID";
    /**
     * 企业编码
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
    public static final String SEQUENCE = "S_LF_WC_REVENT";
    /**
     * 返回数据需要生成的XML格式
     */
    public static final String MSG_XML = "MSG_XML";
    /**
     * 内容摘要，界面显示用
     */
    public static final String MSG_TEXT = "MSG_TEXT";
    /**
     * 标题
     */
    public static final String TITLE = "TITLE";
    public static final String TABLE_NAME = "LF_WC_REVENT";
    /**
     * 映射集合
     */
    protected static final Map<String , String> columns = new HashMap<String, String>();

    static {

        columns.put("LfWcRevent", TABLE_NAME);
        columns.put("tableId", EVT_ID);
        columns.put("EvtId", EVT_ID);
        columns.put("evtType", EVT_TYPE);
        columns.put("TId", T_ID);
        columns.put("AId", A_ID);
        columns.put("corpCode", CORP_CODE);
        columns.put("createtime", CREATETIME);
        columns.put("modifytime", MODIFYTIME);
        columns.put("msgText", MSG_TEXT);
        columns.put("msgXml", MSG_XML);
        columns.put("title", TITLE);
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
