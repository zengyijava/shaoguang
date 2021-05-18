package com.montnets.emp.wyquery.vo.view;

import com.montnets.emp.table.sms.TableMtTask;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewSysMoMtSpgateVo {

    protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    /**
     * 加载字段
     */
    static {
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
