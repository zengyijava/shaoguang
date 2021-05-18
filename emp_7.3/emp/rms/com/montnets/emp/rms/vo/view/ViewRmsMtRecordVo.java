package com.montnets.emp.rms.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewRmsMtRecordVo {

	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    static {
        columns.put("spUser", "USERID");
        columns.put("spGate", "SPGATE");
        columns.put("sendSubject", "TITLE");
        columns.put("svrtype", "SVRTYPE");
        columns.put("rmsSubject", "TM_NAME");
        columns.put("tmplId", "TMPLID");
        columns.put("degree", "CHGRADE");
        columns.put("unicom", "UNICOM");
        columns.put("phone", "PHONE");
        columns.put("taskId", "TASKID");
        columns.put("sendTime", "SENDTIME");
        columns.put("recvTime", "RECVTIME");
//        columns.put("downTime", "DOWNTM");  由于MTTASK中DOWNTM字段默认初始值为0000-00-00 00:00:00在转换成TimeStamp类型时报错，该字段暂未用到
        columns.put("userMsgId", "USERMSGID");
        columns.put("errorCode2", "ERRORCODE2");
        columns.put("custId", "CUSTID");
        columns.put("errorCode", "ERRORCODE");
        columns.put("ptmsgid", "PTMSGID");
        columns.put("tmId", "TM_ID");
    }

    public static Map<String, String> getORM() {
        return columns;
    }
}
