package com.montnets.emp.table.sms;

import java.util.HashMap;

import java.util.Map;

/**
 * @功能概要：草稿箱管理表
 * @项目名称： emp_std_192.169.1.81
 * @初创作者： Administrator
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016/1/19 13:40
 * <p>修改记录1：</p>
 * <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class TableLfDrafts
{

    public static final String TABLE_NAME	= "LF_DRAFTS";

    public static final String MOBILE_URL = "MOBILE_URL";

    public static final String DRAFTS_TYPE = "DRAFTS_TYPE";

    public static final String MSG = "MSG";

    public static final String USER_ID = "USER_ID";

    public static final String CREATE_TIME = "CREATE_TIME";

    public static final String ID = "ID";

    public static final String UPDATE_TIME = "UPDATE_TIME";

    public static final String CORP_CODE = "CORP_CODE";

    public static final String TITLE = "TITLE";

    public static final String SEQUENCE	= "LF_DRAFTS_S";
    protected static final Map<String , String> columns = new HashMap<String, String>();

    static
    {

        columns.put("LfDrafts", TABLE_NAME);
        columns.put("tableId", ID);
        columns.put("sequence", SEQUENCE);
        columns.put("mobileurl", MOBILE_URL);
        columns.put("draftstype", DRAFTS_TYPE);
        columns.put("msg", MSG);
        columns.put("userid", USER_ID);
        columns.put("createtime", CREATE_TIME);
        columns.put("id", ID);
        columns.put("updatetime", UPDATE_TIME);
        columns.put("corpcode", CORP_CODE);
        columns.put("title", TITLE);
    };


    public static Map<String , String> getORM(){

        return columns;

    }
}
