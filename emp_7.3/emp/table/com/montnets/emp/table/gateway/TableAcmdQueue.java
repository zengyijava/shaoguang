/**
 * 
 */
package com.montnets.emp.table.gateway;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 *
 */
public class TableAcmdQueue {
	
	public static final String TABLE_NAME = "A_CMDQUE";

	public static final String ID = "ID";
 
	public static final String GWNO = "GWNO";

	public static final String GWTYPE = "GWTYPE";
	
	public static final String CMDTYPE = "CMDTYPE";
	
	public static final String CMDINFO  = "CMDINFO";
	
	public static final String CMDPARAM  = "CMDPARAM";
	
	public static final String DEALSTATUS  = "DEALSTATUS";
	
	public static final String RESULTCODE  = "RESULTCODE";
	
	public static final String REQTIME  = "REQTIME";
	
	public static final String DONETIME  = "DONETIME";
	
	public static final String SEQUENCE = "SEQ_A_CMDQUE";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("AcmdQueue", TABLE_NAME);
  		columns.put("tableId", ID);
  		columns.put("sequence",SEQUENCE);
		columns.put("id", ID);
		columns.put("gwNo",GWNO);
		columns.put("gwType",GWTYPE);
		columns.put("cmdType",CMDTYPE);
		columns.put("cmdInfo",CMDINFO);
		columns.put("cmdParam",CMDPARAM);
		columns.put("dealStatus",DEALSTATUS);
		columns.put("resultCode",RESULTCODE);
		columns.put("reqTime",REQTIME);
		columns.put("doneTime", DONETIME);
		
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
