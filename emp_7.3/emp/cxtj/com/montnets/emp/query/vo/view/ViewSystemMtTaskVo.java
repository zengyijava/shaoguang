package com.montnets.emp.query.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.sms.TableMtTask;

/**
 * 下行记录vo
 * @project emp
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-5 下午03:37:54
 * @description
 */
public class ViewSystemMtTaskVo
{
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("id", TableMtTask.ID);
		columns.put("userid", TableMtTask.USER_ID);
		columns.put("spgate", TableMtTask.SPGATE);
		columns.put("cpno", TableMtTask.CPNO);
		columns.put("phone", TableMtTask.PHONE);
		columns.put("sendstatus", TableMtTask.SEND_STATUS);
		columns.put("errorcode", TableMtTask.ERROR_CODE);
		columns.put("sendtime", TableMtTask.SEND_TIME);
		columns.put("message", TableMtTask.MESSAGE);
		columns.put("unicom", TableMtTask.UNICOM);
		columns.put("recvTime", TableMtTask.RECV_TIME);
		//if(StaticValue.DBTYPE != StaticValue.DB2_DBTYPE)
		//{
			columns.put("svrtype", TableMtTask.SVRTYPE);
			//columns.put("svrtype", TableLfBusManager.BUS_NAME);
			columns.put("pknumber", TableMtTask.PK_NUMBER);
			columns.put("pktotal", TableMtTask.PK_TOTAL);
			columns.put("msgfmt", TableMtTask.MSGFMT);
		//}
			columns.put("taskid", TableMtTask.TASK_ID);
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
