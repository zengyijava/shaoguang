package com.montnets.emp.netnews.bean.view;

import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.employee.TableLfEmployeeDep;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-7 下午04:47:49
 * @description
 */

public class ViewLfEmployeeVo
{
	
	protected static final Map<String,String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("employeeId", TableLfEmployee.EMPLOYEE_ID);
		columns.put("pid", TableLfEmployee.P_ID);
		columns.put("depId", TableLfEmployee.DEP_ID);
		columns.put("employeeNo", TableLfEmployee.EMPLOYEE_NO);
		columns.put("estate",TableLfEmployee.E_STATE);
		columns.put("commnets",TableLfEmployee.COMMENTS);
		columns.put("recState",TableLfEmployee.REC_STATE);
		columns.put("hidephState",TableLfEmployee.HIDEPH_STATE);
		columns.put("email",TableLfEmployee.E_MAIL);
		columns.put("msn",TableLfEmployee.MSN);
		columns.put("qq",TableLfEmployee.QQ);
		columns.put("name", TableLfEmployee.NAME);
		columns.put("sex",TableLfEmployee.SEX);
		columns.put("mobile",TableLfEmployee.MOBILE);
		columns.put("birthday", TableLfEmployee.BIRTHDAY);
		columns.put("oph", TableLfEmployee.OPH);
		columns.put("guId", TableLfEmployee.GUID);
		columns.put("depName",TableLfEmployeeDep.DEP_NAME);
		//columns.put("depCode", TableLfEmployee.DEP_CODE);
 		columns.put("lastUpddttm", TableLfEmployee.LASTUPDDTTM);
		columns.put("hrStatus",TableLfEmployee.HR_STATUS);
		columns.put("isOperator",TableLfEmployee.IS_OPERATOR);
		columns.put("employeeCode", TableLfEmployee.EMPLOYEE_CODE);
		columns.put("birthday", TableLfEmployee.BIRTHDAY);
		columns.put("dutyName", TableLfEmployee.DUTIES);
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
