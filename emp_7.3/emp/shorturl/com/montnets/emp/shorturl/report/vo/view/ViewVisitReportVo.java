package com.montnets.emp.shorturl.report.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sms.TableMtTask;


public class ViewVisitReportVo {
	
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
	
	static{
		columns.put("phone", TableMtTask.PHONE);
		columns.put("taskId", TableMtTask.TASK_ID);
		columns.put("unicom", TableMtTask.UNICOM);
		columns.put("errorCode", TableMtTask.ERROR_CODE);
		columns.put("message", TableMtTask.MESSAGE);
		columns.put("title", TableLfMttask.TITLE);
		columns.put("sendTime", "SENDTIME");
		columns.put("visitCount", "VISITCOUNT");
//		columns.put("visitNum", "VISITNUM");
		columns.put("lastIP", "lastIP");
		columns.put("lastVisitTime", "lastVisitTime");
	}
	
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
