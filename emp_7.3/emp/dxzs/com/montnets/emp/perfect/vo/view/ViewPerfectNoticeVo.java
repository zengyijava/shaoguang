package com.montnets.emp.perfect.vo.view;

import java.util.HashMap;
import java.util.Map;

import com.montnets.emp.table.perfect.TableLfPerfectNotic;
import com.montnets.emp.table.sysuser.TableLfSysuser;


public class ViewPerfectNoticeVo
{
	protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("noticId", TableLfPerfectNotic.P_NOTIC_ID);
		columns.put("submitTime", TableLfPerfectNotic.SUBMITTIME);
		columns.put("senderGuid", TableLfPerfectNotic.SENDER_GUID);
		columns.put("content", TableLfPerfectNotic.CONTENT);
		columns.put("recevierType", TableLfPerfectNotic.RECEIVER_TYPE);
		columns.put("recevierId", TableLfPerfectNotic.RECEVIERID);
		columns.put("noticCount", TableLfPerfectNotic.NOTIC_COUNT);
		columns.put("arySendCount",TableLfPerfectNotic.ARY_SENDCOUNT);
		columns.put("taskId", TableLfPerfectNotic.TASKID);	
		columns.put("senderName", TableLfSysuser.NAME);	
		
		//columns.put("receiverName", "RECEIVENAME");		
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
