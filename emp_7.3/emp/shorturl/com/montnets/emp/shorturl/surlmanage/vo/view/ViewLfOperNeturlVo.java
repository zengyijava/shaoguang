package com.montnets.emp.shorturl.surlmanage.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.shorturl.surlmanage.table.TableLfNeturl;
import com.montnets.emp.table.corp.TableLfCorp;
import com.montnets.emp.table.sysuser.TableLfSysuser;

public class ViewLfOperNeturlVo {
protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
	
	/**
     * 加载字段
     */
	static
	{
		columns.put("id", TableLfNeturl.ID);
		columns.put("srcurl", TableLfNeturl.SRC_URL);
		columns.put("urlname",TableLfNeturl.URL_NAME);
		columns.put("urlmsg", TableLfNeturl.URL_MSG);
		columns.put("createuid", TableLfNeturl.CREATE_UID);
		columns.put("createuser",TableLfSysuser.NAME);
		columns.put("corpcode", TableLfNeturl.CORP_CODE);
		columns.put("corpname", TableLfCorp.CORP_NAME);
		columns.put("createtm", TableLfNeturl.CREATE_TM);
		columns.put("ispass",TableLfNeturl.ISPASS);
		columns.put("audituid",TableLfNeturl.AUDIT_UID );
		columns.put("remarks",TableLfNeturl.REMARKS );
		columns.put("remarks1",TableLfNeturl.REMARKS1 );
		columns.put("urlstate",TableLfNeturl.URLSTATE );
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
