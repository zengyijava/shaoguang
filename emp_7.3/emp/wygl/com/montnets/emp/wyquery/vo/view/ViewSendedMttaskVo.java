package com.montnets.emp.wyquery.vo.view;

import com.montnets.emp.table.sms.TableMtTask;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewSendedMttaskVo {

    protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    /**
     * 加载字段
     */
    static {
        columns.put("unicom", TableMtTask.UNICOM);
        columns.put("phone", TableMtTask.PHONE);
        columns.put("message", TableMtTask.MESSAGE);
        columns.put("errorcode", TableMtTask.ERROR_CODE);
        columns.put("pknumber", TableMtTask.PK_NUMBER);
        columns.put("pktotal", TableMtTask.PK_TOTAL);
        columns.put("sendtime", TableMtTask.SEND_TIME);
        columns.put("recvtime", TableMtTask.RECV_TIME);
        columns.put("spgate", TableMtTask.SPGATE);
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
