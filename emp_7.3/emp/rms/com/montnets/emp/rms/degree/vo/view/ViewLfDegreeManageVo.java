package com.montnets.emp.rms.degree.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewLfDegreeManageVo {
	protected final static Map<String, String> columns = new LinkedHashMap<String, String>();

	static {
//		columns.put("id", TableLfDegree.ID);
		columns.put("tableId", "id");
		columns.put("id", "id");
		columns.put("LfDegreeManage", "LF_DEGREE");
		columns.put("degree", "degree");
		columns.put("degreeBegin", "degreeBegin");
		columns.put("degreeEnd", "degreeEnd");
		columns.put("userId", "userId");
		columns.put("validDateBegin", "validDateBegin");
		columns.put("validDateEnd", "validDateEnd");
		columns.put("status", "status");
		columns.put("createTime", "createTime");
		columns.put("userName", "userName");
	}

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM() {
		return columns;
	}
}
