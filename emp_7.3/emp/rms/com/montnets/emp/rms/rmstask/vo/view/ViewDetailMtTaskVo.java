package com.montnets.emp.rms.rmstask.vo.view;

import com.montnets.emp.table.sms.TableMtTask01_12;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewDetailMtTaskVo {

    protected final static Map<String, String> columns = new LinkedHashMap<String, String>();

    static {
        columns.put("phone", TableMtTask01_12.PHONE);
        columns.put("unicom", TableMtTask01_12.UNICOM);
        columns.put("errorCode2", TableMtTask01_12.ERROR_CODE2);
        columns.put("errorCode", TableMtTask01_12.ERRO_RCODE);
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
