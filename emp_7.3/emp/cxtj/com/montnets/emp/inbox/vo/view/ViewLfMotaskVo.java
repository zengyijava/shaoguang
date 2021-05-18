package com.montnets.emp.inbox.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.engine.TableLfMotask;
import com.montnets.emp.table.sysuser.TableLfSysuser;


public class ViewLfMotaskVo {
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("moId", TableLfMotask.MO_ID);
		columns.put("ptMsgId", TableLfMotask.PTMSGID);
		columns.put("uids", TableLfMotask.UIDS);
		columns.put("orgUid", TableLfMotask.ORGUID);
		columns.put("ecid", TableLfMotask.ECID);
		columns.put("spUser", TableLfMotask.SP_USER);
		columns.put("spnumber", TableLfMotask.SPNUMBER);
		columns.put("serviceId", TableLfMotask.SERVICEID);
		columns.put("spsc", TableLfMotask.SPSC);
		columns.put("sendStatus", TableLfMotask.SENDSTATUS);
		columns.put("msgFmt", TableLfMotask.MSGFMT);
		columns.put("tpPid", TableLfMotask.TP_PID);
		columns.put("tpUdhi", TableLfMotask.TP_UDHI);
		columns.put("deliverTime", TableLfMotask.DELIVERTIME);
		columns.put("phone", TableLfMotask.PHONE);
		columns.put("msgContent", TableLfMotask.MSGCONTENT);
		columns.put("doneTime", TableLfMotask.DONETIME);
		columns.put("userGuid", TableLfMotask.USER_GUID);
		columns.put("menuCode", TableLfMotask.MENUCODE);
		columns.put("depId", TableLfMotask.DEP_ID);
		columns.put("taskId", TableLfMotask.TASK_ID);
		columns.put("busCode", TableLfMotask.BUS_CODE);
		columns.put("corpCode", TableLfMotask.CORPCODE);
		columns.put("spisuncm", TableLfMotask.SPISUNCM);
		columns.put("subno", TableLfMotask.SUBNO);
		columns.put("userId", TableLfSysuser.USER_ID);
		columns.put("name", TableLfSysuser.NAME);
		columns.put("userName", TableLfSysuser.USER_NAME);
		columns.put("userState", TableLfSysuser.USER_STATE);	
		columns.put("sysdepId", "SYSDEPID");
		columns.put("employeeName", "employeeName");
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
