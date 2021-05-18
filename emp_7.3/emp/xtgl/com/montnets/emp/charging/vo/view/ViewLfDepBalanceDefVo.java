package com.montnets.emp.charging.vo.view;

import com.montnets.emp.table.sysuser.TableLfBalanceDef;
import com.montnets.emp.table.sysuser.TableLfDep;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewLfDepBalanceDefVo {
    protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    /**
     * 加载字段
     */
    static {
        columns.put("depId", TableLfDep.DEP_ID);
        columns.put("depName", TableLfDep.DEP_NAME);
        columns.put("corpCode", TableLfDep.CORP_CODE);
        columns.put("smsCount", TableLfBalanceDef.SMS_COUNT);
        columns.put("mmsCount", TableLfBalanceDef.MMS_COUNT);
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
