package com.montnets.emp.notice.vo.view;

import com.montnets.emp.table.notice.TableLfNotice;
import com.montnets.emp.table.sysuser.TableLfSysuser;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author tanglili <jack860127@126.com>
 * @project montnets_entity
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-9-27 上午11:19:44
 * @description
 */
public class ViewLfNoticeVo {
    protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    /**
     * 加载字段
     */
    static {
        columns.put("noticeID", TableLfNotice.NOTICE_ID);
        columns.put("userID", TableLfNotice.USER_ID);
        columns.put("title", TableLfNotice.TITLE);
        columns.put("context", TableLfNotice.CONTEXT);
        columns.put("publishTime", TableLfNotice.PUBLISH_TIME);
        columns.put("name", TableLfSysuser.NAME);
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
