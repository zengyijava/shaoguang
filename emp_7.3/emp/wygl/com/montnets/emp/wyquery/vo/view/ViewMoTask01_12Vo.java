package com.montnets.emp.wyquery.vo.view;

import com.montnets.emp.table.query.TableMoTask01_12;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-1 上午09:45:27
 * @description
 */
public class ViewMoTask01_12Vo {
    protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    /**
     * 加载字段
     */
    static {
        columns.put("spnumber", TableMoTask01_12.SPNUMBER);
        columns.put("deliverTime", TableMoTask01_12.DELIVERTIME);
        columns.put("phone", TableMoTask01_12.PHONE);
        //columns.put("name", TableLfEmployee.NAME);
        columns.put("msgContent", TableMoTask01_12.MSGCONTENT);
        columns.put("msgFmt", TableMoTask01_12.MSGFMT);
        columns.put("userId", TableMoTask01_12.USERID);
        columns.put("unicom", TableMoTask01_12.UNICOM);
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
