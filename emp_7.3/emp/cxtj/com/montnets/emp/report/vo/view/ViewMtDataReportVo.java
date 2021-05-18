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
public class ViewMtDataReportVo {

	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("userId", TableLfSysuser.USER_ID);
		columns.put("depId", TableLfDep.DEP_ID);
		columns.put("userName",TableLfSysuser.NAME);
		columns.put("depName", TableLfDep.DEP_NAME);
		columns.put("icount", TableMtDatareport.ICOUNT);
		columns.put("rsucc", TableMtDatareport.RSUCC);
		columns.put("rfail1",TableMtDatareport.RFAIL1);
		columns.put("rfail2",TableMtDatareport.RFAIL2);
		columns.put("rnret",TableMtDatareport.RNRET);
		columns.put("userState",TableLfSysuser.USER_STATE);
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
