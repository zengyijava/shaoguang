package com.montnets.emp.netnews.vo.view;

import com.montnets.emp.netnews.table.TableLfWXTrustData;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewLfWXTrustDataVo {

	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("LfWXTrustData", TableLfWXTrustData.TABLE_NAME);
		columns.put("tableId", TableLfWXTrustData.ID);
		columns.put("id", TableLfWXTrustData.ID);
		columns.put("name", TableLfWXTrustData.NAME);
		columns.put("tableName", TableLfWXTrustData.TABLENAME);
		columns.put("type", TableLfWXTrustData.TYPE);
		columns.put("name",TableLfWXTrustData. NAME);
		columns.put("colnum",TableLfWXTrustData. COLNUM);
		columns.put("status", TableLfWXTrustData.STATUS);
		columns.put("modifyId", TableLfWXTrustData.MODIFYID);
		columns.put("modifyDate",TableLfWXTrustData. MODIFYDATE);
		columns.put("creatId", TableLfWXTrustData.CREATID);
		columns.put("creatDate",TableLfWXTrustData. CREATDATE);
		columns.put("url",TableLfWXTrustData. URL);
		columns.put("auditUserId", TableLfWXTrustData.AUDITUSERID);
		columns.put("auditDate", TableLfWXTrustData.AUDITDATE);
		columns.put("corpCode", TableLfWXTrustData.CORPCODE);
		columns.put("createUser", "createUser");
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
