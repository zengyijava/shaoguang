package com.montnets.emp.shorturl.report.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewSendDetailMttaskVo {
    protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    static {
        columns.put("unicom", "UNICOM");
        columns.put("phone", "PHONE");
        columns.put("message", "MESSAGE");
        columns.put("errorcode", "ERRORCODE");
        columns.put("pknumber", "PKNUMBER");
        columns.put("pktotal", "PKTOTAL");
    }

    public ViewSendDetailMttaskVo() {
    }

    public static Map<String, String> getORM() {
        return columns;
    }
}
