package com.montnets.emp.rms.report.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.rms.report.table.TableLfRmsReport;


/**
 * 档位统计报表
 * @project p_ydcx
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2018-1-13
 * @description
 */
public class ViewLfRmsReportVo
{
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("icount", TableLfRmsReport.ICOUNT);
		columns.put("rfail", TableLfRmsReport.RFAIL);
		columns.put("rsucc", TableLfRmsReport.RSUCC);
		columns.put("iymd", TableLfRmsReport.IYMD);
		columns.put("degree", TableLfRmsReport.DEGREE);
		columns.put("spisuncm", TableLfRmsReport.SPISUNCM);
		columns.put("spID", TableLfRmsReport.USER_ID);
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
