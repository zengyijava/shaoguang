package com.montnets.emp.report.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.report.TableMtDatareport;

/**
 * 下行记录
 * @project sinolife
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-17 下午02:10:07
 * @description
 */
public class ViewOperatorsMtDataReportVo
{
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		//columns.put("spgate", TableMtDatareport.SPGATE);
		columns.put("spisuncm", TableMtDatareport.SPISUNCM);
		//columns.put("iymd", TableSpMtDatareport.IYMD);
		columns.put("rfail1", TableMtDatareport.RFAIL1);
		columns.put("rfail2", TableMtDatareport.RFAIL2);
		columns.put("rnret", TableMtDatareport.RNRET);
		columns.put("rsucc", TableMtDatareport.RSUCC);
		columns.put("icount", TableMtDatareport.ICOUNT);
		columns.put("y", TableMtDatareport.Y);
		columns.put("imonth", TableMtDatareport.IMONTH);
		columns.put("spID", TableMtDatareport.SPID);
		//columns.put("stratTime", TableMtDatareport.IYMD);
		//columns.put("endTime", TableMtDatareport.IYMD);
		columns.put("iymd", TableMtDatareport.IYMD);
		//columns.put("userid", TableSpMtDatareport.USER_ID);
		//columns.put("staffName", TableUserdata.STAFF_NAME);
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
