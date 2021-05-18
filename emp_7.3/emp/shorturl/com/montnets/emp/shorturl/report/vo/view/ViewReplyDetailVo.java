package com.montnets.emp.shorturl.report.vo.view;

import com.montnets.emp.shorturl.surlmanage.table.TableLfUrlTask;
import com.montnets.emp.table.engine.TableLfMotask;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewReplyDetailVo {
    protected final static Map<String, String> columns = new LinkedHashMap<String, String>();

    static{
        columns.put("phone", TableLfMotask.PHONE);
        columns.put("msgContent", TableLfMotask.MSGCONTENT);
        columns.put("deliverTime", TableLfMotask.DELIVERTIME);
        columns.put("replyName", "REPLYNAME");
    }

    /**
     * 返回实体类字段与数据库字段实体类映射的map集合
     *
     * @return
     */
    public static Map<String, String> getORM() {
        return columns;
    }
}
