package com.montnets.emp.charging.vo.view;

import com.montnets.emp.table.sysuser.TableLfDepRechargeLog;
import com.montnets.emp.table.sysuser.TableLfSysuser;

import java.util.LinkedHashMap;
import java.util.Map;


public class ViewLfDepRechargeLogVo {
    protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    /**
     * 加载字段
     */
    static {
        columns.put("logId", TableLfDepRechargeLog.LOG_ID);
        columns.put("optType", TableLfDepRechargeLog.OPT_TYPE);
        columns.put("srcTargetId", TableLfDepRechargeLog.SRC_TARGETID);
        columns.put("dstTagertId", TableLfDepRechargeLog.DST_TARGETID);
        columns.put("msgType", TableLfDepRechargeLog.MSG_TYPE);
        columns.put("count", TableLfDepRechargeLog.COUNT);
        columns.put("optId", TableLfDepRechargeLog.OPT_ID);
        columns.put("optInfo", TableLfDepRechargeLog.OPT_INFO);
        columns.put("optDate", TableLfDepRechargeLog.OPT_DATE);
        columns.put("result", TableLfDepRechargeLog.RESULT);
        columns.put("memo", TableLfDepRechargeLog.MEMO);
        columns.put("srcName", "srcName");
        columns.put("dstName", "dstName");
        columns.put("userName", "userName");
        columns.put("userState", TableLfSysuser.USER_STATE);
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
