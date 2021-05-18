package com.montnets.emp.rms.report.vo.view;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.rms.table.TableMtDataReport;
import com.montnets.emp.table.corp.TableLfCorp;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MtDataReport的VO对象的映射类
 *
 * @date 2018-6-13 14:59:01
 * @author Cheng
 */
public class ViewMtDataReportVo {
    /**
     * 实体类字段与数据库字段实体类映射的map集合
     */
    protected final static Map<String, String> columns = new LinkedHashMap<String, String>();

    static {
        columns.put("icount", TableMtDataReport.ICOUNT);
        columns.put("rfail1", TableMtDataReport.RFAIL1);
        columns.put("rnret", TableMtDataReport.RNRET);
        columns.put("recfail", TableMtDataReport.RECFAIL);
        columns.put("rsucc", TableMtDataReport.RSUCC);
        //columns.put("dwsucc", TableMtDataReport.DWSUCC);
        columns.put("iymd", TableMtDataReport.IYMD);
        columns.put("chgrade", TableMtDataReport.CHGRADE);
        columns.put("spisuncm", TableMtDataReport.SPISUNCM);
        columns.put("userId", TableMtDataReport.USERID);
        if(StaticValue.getCORPTYPE() == 1){
            columns.put("corpCode", TableLfCorp.CORP_CODE);
            columns.put("corpName", TableLfCorp.CORP_NAME);
        }
    }

    /**
     * 返回实体类字段与数据库字段实体类映射的map集合
     *
     * @return Map
     */
    public static Map<String, String> getORM() {
        return columns;
    }
}
