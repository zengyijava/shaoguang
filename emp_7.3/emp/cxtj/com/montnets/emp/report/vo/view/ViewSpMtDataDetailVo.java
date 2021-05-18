package com.montnets.emp.report.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.report.TableMtDatareport;

public class ViewSpMtDataDetailVo {
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		//columns.put("iymd", TableMtDatareport.IYMD);
		columns.put("y", TableMtDatareport.Y);
		columns.put("imonth", TableMtDatareport.IMONTH);
		columns.put("icount", TableMtDatareport.ICOUNT);
		columns.put("rsucc", TableMtDatareport.RSUCC);
		columns.put("rfail1",TableMtDatareport.RFAIL1);
		columns.put("rfail2",TableMtDatareport.RFAIL2);
		columns.put("rnret",TableMtDatareport.RNRET);
		columns.put("userid", TableMtDatareport.USER_ID);
		columns.put("staffname", TableUserdata.STAFF_NAME);
		columns.put("sptype", "SPTYPE");
		columns.put("sendtype", "SENDTYPE");
		columns.put("iymd", TableMtDatareport.IYMD);
		
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
