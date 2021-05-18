package com.montnets.emp.charging.vo.view;

import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDepUserBalance;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wuxiaotao <819475589@qq.com>
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-2-25 上午09:58:23
 * @description
 */

public class ViewLfDepBalanceVo {
    protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    /**
     * 加载字段
     */
    static {
        columns.put("depId", TableLfDep.DEP_ID);
        columns.put("depName", TableLfDep.DEP_NAME);
        columns.put("depResp", TableLfDep.DEP_RESP);
        columns.put("smsBalance", TableLfDepUserBalance.SMS_BALANCE);
        columns.put("mmsBalance", TableLfDepUserBalance.MMS_BALANCE);
        columns.put("smsCount", TableLfDepUserBalance.SMS_COUNT);
        columns.put("mmsCount", TableLfDepUserBalance.MMS_COUNT);
        columns.put("smsAlarm", TableLfDepUserBalance.SMS_ALARM_VALUE);
        columns.put("mmsAlarm", TableLfDepUserBalance.MMS_ALARM_VALUE);
        columns.put("alarmName", TableLfDepUserBalance.ALARM_NAME);
        columns.put("alarmPhone", TableLfDepUserBalance.ALARM_PHONE);
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
