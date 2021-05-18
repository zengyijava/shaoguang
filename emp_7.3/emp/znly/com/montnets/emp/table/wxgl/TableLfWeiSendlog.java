package com.montnets.emp.table.wxgl;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfWeiSendlog
 * 
 * @project p_wei
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWeiSendlog
{
    // 群发消息历史表
    public static final String              TABLE_NAME = "LF_WEI_SENDLOG";

    // 消息ID
    public static final String        SEND_ID    = "SEND_ID";

    // 发送类型(0：分组群发，1：按地区发送)
    public static final String        TP         = "TP";

    // 消息的类型(文本：text,图文：mpnews)
    public static final String        MSG_TYPE   = "MSG_TYPE";

    // 地区的名称(目前存的中文，以后可能需要改进)
    public static final String        AREA_VALUE = "AREA_VALUE";

    // 提交的数据
    public static final String        POST_MSG   = "POST_MSG";

    // 图文模板或关联资源的ID
    public static final String        T_ID       = "T_ID";

    // 公众帐号
    public static final String        A_ID       = "A_ID";

    // 企业编号
    public static final String        CORP_CODE  = "CORP_CODE";

    // 创建时间
    public static final String        CREATETIME = "CREATETIME";

    // 消息的状态（0：发送失败，1：已提交成功，2：发送成功）
    public static final String        STATUS     = "STATUS";

    // 微信服务器推送过来的反馈信息
    public static final String        EVENTDATA  = "EVENTDATA";
    // 提交返回的信息
    public static final String        RESPONSE_MSG = "RESPONSE_MSG";
    
    // 返回消息的id
    public static final String        MSG_ID = "MSG_ID";
    
    // 文本发送时，为文本内容，图文发送时，为图文标题
    public static final String        SEND_CONTENT   = "SEND_CONTENT";
    
    // 序列
    public static final String        SEQUENCE   = "S_LF_WEI_SENDLOG";

    // 映射集合
    protected static final Map<String, String> columns    = new HashMap<String, String>();

    static
    {
        columns.put("LfWeiSendlog", TABLE_NAME);
        columns.put("tableId", SEND_ID);
        columns.put("sequence", SEQUENCE);
        columns.put("sendId", SEND_ID);
        columns.put("tp", TP);
        columns.put("msgType", MSG_TYPE);
        columns.put("areaValue", AREA_VALUE);
        columns.put("postMsg", POST_MSG);
        columns.put("tId", T_ID);
        columns.put("aId", A_ID);
        columns.put("status", STATUS);
        columns.put("eventData", EVENTDATA);
        columns.put("responseMsg", RESPONSE_MSG);
        columns.put("msgId", MSG_ID);
        columns.put("corpCode", CORP_CODE);
        columns.put("createtime", CREATETIME);
        columns.put("sendContent", SEND_CONTENT);
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
