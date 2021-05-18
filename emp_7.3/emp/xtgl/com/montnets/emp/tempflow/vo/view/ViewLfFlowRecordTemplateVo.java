package com.montnets.emp.tempflow.vo.view;

import com.montnets.emp.table.approveflow.TableLfFlowRecord;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.template.TableLfTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author tanglili <jack860127@126.com>
 * @project montnets_entity
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-16 下午01:56:35
 * @description
 */
public class ViewLfFlowRecordTemplateVo {
    protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    /**
     * 加载字段
     */
    static {
        columns.put("frId", TableLfFlowRecord.FR_ID);
        columns.put("mtId", TableLfFlowRecord.MT_ID);
        columns.put("FId", TableLfFlowRecord.F_ID);
        columns.put("RTime", TableLfFlowRecord.R_TIME);
        columns.put("RLevel", TableLfFlowRecord.R_LEVEL);
        columns.put("reviewType", TableLfFlowRecord.REVIEW_TYPE);
        columns.put("RLevelAmount", TableLfFlowRecord.R_LEVELAMOUNT);
        columns.put("RContent", TableLfFlowRecord.R_CONTENT);
        columns.put("RState", TableLfFlowRecord.R_STATE);
        columns.put("comments", TableLfFlowRecord.COMMENTS);
        columns.put("preReviName", "preReviName");
        columns.put("reviName", "reviName");
        columns.put("name", TableLfSysuser.NAME);
        columns.put("userId", TableLfSysuser.USER_ID);
        columns.put("userName", TableLfSysuser.USER_NAME);
        columns.put("depName", TableLfDep.DEP_NAME);
        columns.put("tmName", TableLfTemplate.TM_NAME);
        columns.put("dsflag", TableLfTemplate.DS_FLAG);
        columns.put("addtime", TableLfTemplate.ADD_TIME);
        columns.put("tmMsg", TableLfTemplate.TM_MSG);
        columns.put("tmState", TableLfTemplate.TM_STATE);
        columns.put("paramcnt", TableLfTemplate.PARAMCNT);
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
