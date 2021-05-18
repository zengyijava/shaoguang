package com.montnets.emp.corpmanage.vo.view;

import com.montnets.emp.table.corp.TableLfCorp;
import com.montnets.emp.table.pasroute.TableLfSpDepBind;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wuxiaotao <819475589@qq.com>
 * @project montnets_entity
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-10-31 下午04:04:24
 * @description
 */

public class ViewLfSpCorpBindVo {
    protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    /**
     * 加载字段
     */
    static {
        columns.put("spUser", TableLfSpDepBind.SP_USER);
        columns.put("corpCode", TableLfSpDepBind.CORP_CODE);
        columns.put("platFormType", TableLfSpDepBind.PLATFORM_TYPE);
        columns.put("corpName", TableLfCorp.CORP_NAME);
        columns.put("corpId", TableLfCorp.CORP_ID);
        columns.put("isValidate", TableLfSpDepBind.IS_VALIDATE);
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
