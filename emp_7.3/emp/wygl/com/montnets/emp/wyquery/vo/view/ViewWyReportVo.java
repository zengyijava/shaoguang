package com.montnets.emp.wyquery.vo.view;

import com.montnets.emp.table.passage.TableXtGateQueue;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewWyReportVo {
    protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    /**
     * 加载字段
     */
    static {

        columns.put("rfail2", "RFAIL2");
        columns.put("icount", "ICOUNT");
        columns.put("rsucc", "RSUCC");
        columns.put("rfail1", "RFAIL1");
        columns.put("rnret", "RNRET");
        columns.put("gateName", TableXtGateQueue.GATE_NAME);
        columns.put("spgate", TableXtGateQueue.SPGATE);

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
