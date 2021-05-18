package com.montnets.emp.report.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;
import com.montnets.emp.table.report.TableMtDatareport;
import com.montnets.emp.table.selfparam.TableLfWgParamConfig;

public class ViewDynParmReportVo {

    protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{ 		 
		columns.put("paramName",TableLfWgParamConfig.PARAMNAME);
		columns.put("pa","PA");
        columns.put("icount","ICOUNT");
        columns.put("rsucc", "RSUCC");
        columns.put("rfail1","RFAIL1");
        columns.put("rfail2", "RFAIL2");
        columns.put("rnret", "RNRET");
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
