package com.montnets.emp.inbox.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.engine.TableLfMotask;
import com.montnets.emp.table.sysuser.TableLfSysuser;


public class ViewLfMotaskVo1 {
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("moId", TableLfMotask.MO_ID);
		columns.put("spUser", TableLfMotask.SP_USER);
		columns.put("spnumber", TableLfMotask.SPNUMBER);
		columns.put("msgFmt", TableLfMotask.MSGFMT);
		columns.put("deliverTime", TableLfMotask.DELIVERTIME);
		columns.put("phone", TableLfMotask.PHONE);
		columns.put("msgContent", TableLfMotask.MSGCONTENT);
		columns.put("corpCode", TableLfMotask.CORPCODE);
		columns.put("spisuncm", TableLfMotask.SPISUNCM);
		columns.put("userguid", TableLfMotask.USER_GUID);
		
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
