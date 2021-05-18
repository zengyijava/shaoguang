package com.montnets.emp.biztype.vo.view;

import com.montnets.emp.table.biztype.TableLfBusManager;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>project name p_xtgl</p>
 * <p>Title: ViewLfBusManagerVo</p>
 * <p>Description: </p>
 * <p>Company: Montnets Technology CO.,LTD.</p>
 *
 * @author dingzx
 * @date 2015-1-15下午02:24:06
 */
public class ViewLfBusManagerVo {


    protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    /**
     * 加载字段
     */
    static {
        columns.put("busId", TableLfBusManager.BUS_ID);
        columns.put("busCode", TableLfBusManager.BUS_CODE);
        columns.put("busName", TableLfBusManager.BUS_NAME);
        columns.put("busDescription", TableLfBusManager.BUS_DESCRIPTION);
        columns.put("corpCode", TableLfBusManager.CORP_CODE);
        columns.put("createTime", TableLfBusManager.CREATE_TIME);
        columns.put("updateTime", TableLfBusManager.UPDATE_TIME);
        columns.put("userId", TableLfBusManager.USER_ID);
        columns.put("state", TableLfBusManager.STATE);
        columns.put("busType", TableLfBusManager.BUS_TYPE);
        columns.put("riseLevel", TableLfBusManager.RISELEVEL);
        columns.put("name", TableLfSysuser.NAME);
        columns.put("userName", TableLfSysuser.USER_NAME);
        columns.put("depName", TableLfDep.DEP_NAME);
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
