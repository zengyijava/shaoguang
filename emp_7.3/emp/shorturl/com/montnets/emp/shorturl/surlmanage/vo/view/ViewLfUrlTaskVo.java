package com.montnets.emp.shorturl.surlmanage.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.shorturl.surlmanage.table.TableLfUrlTask;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;

public class ViewLfUrlTaskVo {

	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
	
	/**
     * 加载字段
     */
	static
	{
		columns.put("id", TableLfUrlTask.ID);
		columns.put("createUid", TableLfUrlTask.CREATE_UID);
		columns.put("name", TableLfSysuser.NAME);
		columns.put("depname", TableLfDep.DEP_NAME);
		columns.put("depid", TableLfDep.DEP_ID);
		columns.put("spUser", TableLfUrlTask.SPUSER);
		columns.put("utype", TableLfUrlTask.UTYPE);
		columns.put("title", TableLfUrlTask.TITLE);
		columns.put("taskId", TableLfUrlTask.TASKID);
		columns.put("sendtm", TableLfUrlTask.SEND_TM);
		columns.put("createtm", TableLfUrlTask.CREATE_TM);
		columns.put("subCount", TableLfUrlTask.SUB_COUNT);
		columns.put("msg", TableLfUrlTask.MSG);
		columns.put("netUrl", TableLfUrlTask.NETURL);
		columns.put("netUrlId", TableLfUrlTask.NETURL_ID);
		columns.put("srcfile", TableLfUrlTask.SRC_FILE);
		columns.put("urlfile", TableLfUrlTask.URL_FILE);
		columns.put("status", TableLfUrlTask.STATUS);
		
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
