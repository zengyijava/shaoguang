package com.montnets.emp.common.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.sms.TableMtTask;

public class ViewSendedMttaskVo {
	
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("unicom", TableMtTask.UNICOM);
		columns.put("phone", TableMtTask.PHONE);
		columns.put("message", TableMtTask.MESSAGE);
		columns.put("errorcode", TableMtTask.ERROR_CODE);
		columns.put("pknumber", TableMtTask.PK_NUMBER);
		columns.put("pktotal", TableMtTask.PK_TOTAL);
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
