package com.montnets.emp.query.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;
import com.montnets.emp.table.sms.TableMtTask01_12;

/**
 * 下行历史记录
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-5 下午03:30:58
 * @description
 */
public class ViewSystemMtTask01_12Vo
{
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("id", TableMtTask01_12.ID);
		columns.put("userid",TableMtTask01_12.USER_ID);
		columns.put("spgate",TableMtTask01_12.SPGATE);
		columns.put("cpno", TableMtTask01_12.CPNO);
		columns.put("phone",TableMtTask01_12.PHONE);
		columns.put("sendstatus",TableMtTask01_12.SEND_STATUS);
		columns.put("errorcode", TableMtTask01_12.ERRO_RCODE);
		columns.put("sendtime", TableMtTask01_12.SEND_TIME);
		columns.put("message", TableMtTask01_12.MESSAGE);
		columns.put("unicom", TableMtTask01_12.UNICOM);
		columns.put("recvTime", TableMtTask01_12.RECV_TIME);
		columns.put("svrtype", TableMtTask01_12.SVRTYPE);
//		columns.put("svrtype", TableLfBusManager.BUS_NAME);
		columns.put("pknumber", TableMtTask01_12.PK_NUMBER);
		columns.put("pktotal", TableMtTask01_12.PK_TOTAL);
		columns.put("taskid", TableMtTask01_12.TASK_ID);
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
