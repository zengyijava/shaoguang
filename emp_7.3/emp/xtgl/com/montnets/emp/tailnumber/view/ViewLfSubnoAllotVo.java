package com.montnets.emp.tailnumber.view;

import com.montnets.emp.table.biztype.TableLfBusManager;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfPrivilege;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.tailnumber.TableLfSubnoAllot;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author liujianjun <646654831@qq.com>
 * @project sinolife
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-26 上午10:24:03
 * @description
 */

public class ViewLfSubnoAllotVo {
    //实体类字段与数据库字段实体类映射的map集合
    protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    /**
     * 加载字段
     */
    static {
        //id
        columns.put("suId", TableLfSubnoAllot.SUID);
//		columns.put("menuCode", TableLfSubnoAllot.MENU_CODE);
//		columns.put("loginId", TableLfSubnoAllot.LOGINID);
//		columns.put("depId", TableLfSubnoAllot.DEP_ID);
        //发送账号
        columns.put("spUser", TableLfSubnoAllot.SP_USER);
//		columns.put("subno", TableLfSubnoAllot.SUBNO);
        //分配类型
        columns.put("allotType", TableLfSubnoAllot.ALLOT_TYPE);
        //开始尾号
        columns.put("extendSubnoBegin", TableLfSubnoAllot.EXTEND_SUBNO_BEGIN);
        //结束尾号
        columns.put("extendSubnoEnd", TableLfSubnoAllot.EXTEMD_SUBNO_END);
        //使用中尾号
        columns.put("usedExtendSubno", TableLfSubnoAllot.USEDEXTEND_SUBNO);
        //全通道好
        columns.put("spNumber", TableLfSubnoAllot.SP_NUMBER);
//		columns.put("updateTime", TableLfSubnoAllot.UPDATE_TIME);
//		columns.put("createTime", TableLfSubnoAllot.CREATE_TIME);
//		columns.put("routeId", TableLfSubnoAllot.ROUTE_ID);
//		columns.put("shareType", TableLfSubnoAllot.SHARE_TYPE);
//		columns.put("userName", TableLfSysuser.USER_NAME);
//		columns.put("depName", TableLfDep.DEP_NAME);
        //编码
        columns.put("codes", TableLfSubnoAllot.CODES);
        //编码类型
        columns.put("codeType", TableLfSubnoAllot.CODE_TYPE);
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


    protected static final Map<String, String> columnsMenu = new LinkedHashMap<String, String>();

    public static Map<String, String> getColumnsMenu() {
        return columnsMenu;
    }

    static {
        columnsMenu.putAll(columns);
        columnsMenu.put("name", TableLfPrivilege.MENU_NAME);

    }

    ;

    /**
     * 返回实体类字段与数据库字段实体类映射的map集合
     *
     * @return
     */
    public static Map<String, String> getMenuORM() {
        return columnsMenu;
    }


    protected static final Map<String, String> columnsBus = new LinkedHashMap<String, String>();

    static {
        columnsBus.putAll(columns);
        columnsBus.put("name", TableLfBusManager.BUS_NAME);

    }

    ;

    /**
     * 返回实体类字段与数据库字段实体类映射的map集合
     *
     * @return
     */
    public static Map<String, String> getBusORM() {
        return columnsBus;
    }


    protected static final Map<String, String> columnsPro = new LinkedHashMap<String, String>();

    static {
        columnsPro.putAll(columns);

    }

    ;

    /**
     * 返回实体类字段与数据库字段实体类映射的map集合
     *
     * @return
     */
    public static Map<String, String> getProORM() {
        return columnsPro;
    }


    protected static final Map<String, String> columnsDep = new LinkedHashMap<String, String>();

    static {
        columnsDep.putAll(columns);
        columnsDep.put("name", TableLfDep.DEP_NAME);

    }

    ;

    /**
     * 返回实体类字段与数据库字段实体类映射的map集合
     *
     * @return
     */
    public static Map<String, String> getDepORM() {
        return columnsDep;
    }


    protected static final Map<String, String> columnsUser = new LinkedHashMap<String, String>();

    static {
        columnsUser.putAll(columns);
        columnsUser.put("name", TableLfSysuser.NAME);

    }

    ;

    /**
     * 返回实体类字段与数据库字段实体类映射的map集合
     *
     * @return
     */
    public static Map<String, String> getUserORM() {
        return columnsUser;
    }

    public static Map<String, String> getColumns() {
        return columns;
    }

    public static Map<String, String> getColumnsBus() {
        return columnsBus;
    }

    public static Map<String, String> getColumnsPro() {
        return columnsPro;
    }

    public static Map<String, String> getColumnsDep() {
        return columnsDep;
    }

    public static Map<String, String> getColumnsUser() {
        return columnsUser;
    }
}
