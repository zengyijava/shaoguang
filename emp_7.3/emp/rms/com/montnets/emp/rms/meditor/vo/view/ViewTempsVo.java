package com.montnets.emp.rms.meditor.vo.view;

import com.montnets.emp.rms.meditor.table.TableLfSubTemplate;
import com.montnets.emp.table.template.TableLfTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewTempsVo {

	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();

    /**
     * 加载字段
     */
    static
    {

        columns.put("tmId", TableLfTemplate.TM_ID);
        columns.put("tmpType", TableLfTemplate.TMP_TYPE);
        columns.put("tmName", TableLfTemplate.TM_NAME);
        columns.put("auditStatus", TableLfTemplate.AUDITSTATUS);
        columns.put("sptemplid", TableLfTemplate.SP_TEMPLID);
        columns.put("addTime", TableLfTemplate.ADD_TIME);
        columns.put("content", TableLfSubTemplate.CONTENT);
        columns.put("useId", TableLfTemplate.USEID);
        columns.put("industryId", TableLfTemplate.INDUSTRYID);
        columns.put("isShortTemp", TableLfTemplate.ISSHORTTEMP);
        columns.put("dsFlag", TableLfTemplate.DS_FLAG);
        columns.put("tmState", TableLfTemplate.TM_STATE);
        columns.put("usecount", TableLfTemplate.USECOUNT);
        columns.put("degreeSize", TableLfSubTemplate.DEGREE_SIZE);
        columns.put("degree", TableLfSubTemplate.DEGREE);
        columns.put("userId", TableLfTemplate.USER_ID);
        columns.put("cardHtml", TableLfSubTemplate.CARD_HTML);
        columns.put("fileUrl", TableLfSubTemplate.FILEURL);
        columns.put("tmMsg", TableLfTemplate.TM_MSG);
        columns.put("ver", TableLfTemplate.VER);
        columns.put("h5Type", TableLfSubTemplate.H5TYPE);
        columns.put("h5Url", TableLfSubTemplate.H5URL);
        columns.put("source", TableLfTemplate.SOURCE);
        columns.put("isMaterial", TableLfTemplate.ISMATERIAL);

    };

    /**
     * 返回实体类字段与数据库字段实体类映射的map集合
     *
     * @return
     */
    public static Map<String, String> getORM()
    {
        return columns;
    }
}
