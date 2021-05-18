package com.montnets.emp.query.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.query.TableMoTask;

/**
 * 
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-1 上午11:16:25
 * @description
 */
public class ViewMoTaskVo
{
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("spnumber", TableMoTask.SPNUMBER);
		columns.put("deliverTime", TableMoTask.DELIVER_TIME);
		columns.put("phone", TableMoTask.PHONE);
		columns.put("msgContent", TableMoTask.MSG_CONTENT);
		//-------------GUODW
		columns.put("pknumber", TableMoTask.PKNUMBER);
		columns.put("pktotal", TableMoTask.PKTOTAL);
		columns.put("msgFmt", TableMoTask.MSG_FMT);
		columns.put("userId", TableMoTask.USER_ID);
		columns.put("unicom", TableMoTask.UNICOM);
		//columns.put("name", TableLfEmployee.NAME);
		//------------
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
