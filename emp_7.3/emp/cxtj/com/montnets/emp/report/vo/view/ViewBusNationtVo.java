package com.montnets.emp.report.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.report.TableMtDatareport;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
/**
 * @author Administrator
 *
 */
public class ViewBusNationtVo {

	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("y", "Y");
		columns.put("imonth", "IMONTH");
		columns.put("iymd", "IYMD");
		columns.put("busCode", "SVRTYPE");
		columns.put("busName", "BUS_NAME");
        columns.put("icount","ICOUNT");
        columns.put("rsucc", "RSUCC");
        columns.put("rfail1","RFAIL1");
        columns.put("rfail2", "RFAIL2");
        columns.put("rnret", "RNRET");
		columns.put("spisuncm", "SPISUNCM");
		columns.put("nationcode", "NATIONCODE");
		columns.put("nationname", "NATIONNAME");
		columns.put("spgatecode", "SPGATECODE");
		columns.put("spgatename", "SPGATENAME");
		columns.put("sptype", "SPTYPE");
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
