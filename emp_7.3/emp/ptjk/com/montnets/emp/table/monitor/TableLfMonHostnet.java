/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2016-4-27 下午06:19:54
 */
package com.montnets.emp.table.monitor;

import java.util.HashMap;
import java.util.Map;
/**
 * @功能概要：
 * @项目名称： emp
 * @初创作者： chentingsheng <cts314@163.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-4-27 下午06:19:54
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */


public class TableLfMonHostnet
{

	public static final String TABLE_NAME	= "LF_MON_HOSTNET";

	public static final String WEBNAME = "WEBNAME";

	public static final String NETSTATE = "NETSTATE";

	public static final String PROCENODE = "PROCENODE";

	public static final String MONSTATUS = "MONSTATUS";

	public static final String MONTYPE = "MONTYPE";

	public static final String CREATETIME = "CREATETIME";

	public static final String IPADDR = "IPADDR";

	public static final String SMSALFLAG1 = "SMSALFLAG1";

	public static final String HOSTNAME = "HOSTNAME";

	public static final String ID = "ID";

	public static final String SERVERNUM = "SERVERNUM";

	public static final String WEBNODE = "WEBNODE";

	public static final String MAILALFLAG1 = "MAILALFLAG1";

	public static final String UPDATETIME = "UPDATETIME";

	public static final String EVTTYPE = "EVTTYPE";
	
	public static final String PROCETYPE = "PROCETYPE";

    public static final String DBSERVTIME = "DBSERVTIME";
    
	public static final String SEQUENCE	= "S_LF_M_HOSTNET";
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("LfMonHostnet", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("webname", WEBNAME);
		columns.put("netstate", NETSTATE);
		columns.put("procenode", PROCENODE);
		columns.put("monstatus", MONSTATUS);
		columns.put("montype", MONTYPE);
		columns.put("createtime", CREATETIME);
		columns.put("ipaddr", IPADDR);
		columns.put("smsalflag1", SMSALFLAG1);
		columns.put("hostname", HOSTNAME);
		columns.put("id", ID);
		columns.put("servernum", SERVERNUM);
		columns.put("webnode", WEBNODE);
		columns.put("mailalflag1", MAILALFLAG1);
		columns.put("updatetime", UPDATETIME);
		columns.put("evttype", EVTTYPE);
		columns.put("procetype", PROCETYPE);
	    columns.put("dbservtime", DBSERVTIME);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}

					
