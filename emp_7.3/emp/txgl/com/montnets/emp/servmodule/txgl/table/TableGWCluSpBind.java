package com.montnets.emp.servmodule.txgl.table;

import java.util.HashMap;
import java.util.Map;

/**
 * @功能概要：
 * @项目名称： emp_std_192.169.1.81
 * @初创作者： Administrator
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016/4/20 16:04
 * <p>修改记录1：</p>
 * <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class TableGWCluSpBind {
    public static final String TABLE_NAME	= "GW_CLUSPBIND";

    private static final String PTACCUID	= "PTACCUID";

    private static final String GWNO = "GWNO";

    private static final String GWEIGHT = "GWEIGHT";

    private static final String UPDTIME = "UPDTIME";

    private static final String ID = "ID";

    private static final String SEQUENCE	= "GWCLUSPBIND_S";

    protected static final Map<String , String> columns = new HashMap<String, String>();

    static
    {
        columns.put("GWCluSpBind", TABLE_NAME);
        columns.put("tableId", ID);
        columns.put("sequence", SEQUENCE);
        columns.put("ptaccuid", PTACCUID);
        columns.put("gwno", GWNO);
        columns.put("gweight", GWEIGHT);
        columns.put("updtime", UPDTIME);
        columns.put("id", ID);
    };


    public static Map<String , String> getORM(){

        return columns;

    }
}
