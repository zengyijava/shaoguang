/**
 * Program : TableLfAppTcMomsg.java
 * Author : zousy
 * Create : 2014-6-13 下午01:59:27
 * company ShenZhen Montnets Technology CO.,LTD.
 */

package com.montnets.emp.table.appmage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zousy <zousy999@qq.com>
 * @version 1.0.0
 * @2014-6-13 下午01:59:27
 */
public class TableLfAppTcMomsg
{
	public static final String				TABLE_NAME	= "LF_APP_TC_MOMSG";

	public static final String			MSG_ID		= "MSG_ID";

	public static final String			A_ID		= "A_ID";

	public static final String			WC_ID		= "WC_ID";

	public static final String			MSG_XML		= "MSG_XML";

	public static final String			MSG_TYPE	= "MSG_TYPE";

	public static final String			CREATETIME	= "CREATETIME";

	public static final String			CORP_CODE	= "CORP_CODE";

	public static final String			PARENT_ID	= "PARENT_ID";

	public static final String			TYPE		= "TYPE";

	public static final String			MSG_TEXT	= "MSG_TEXT";

	public static final String			MSG_JSON	= "MSG_JSON";

	public static final String			FROMUSER	= "FROMUSER";

	public static final String			TONAME		= "TONAME";

	public static final String			TOUSER		= "TOUSER";

	public static final String			MSG_SRC		= "MSG_SRC";

	public static final String			STATUS		= "STATUS";
	
	public static final String			SERIAL		= "SERIAL";
	
	public static final String			FROMNAME		= "FROMNAME";

	public static final String			SEQUENCE	= "S_LF_APP_TC_MOMSG";

	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfAppTcMomsg", TABLE_NAME);
		columns.put("tableId", MSG_ID);
		columns.put("sequence", SEQUENCE);

		columns.put("msgid", MSG_ID);

		columns.put("aid", A_ID);

		columns.put("wcid", WC_ID);

		columns.put("msgxml", MSG_XML);

		columns.put("msgtype", MSG_TYPE);

		columns.put("createtime", CREATETIME);

		columns.put("corpcode", CORP_CODE);

		columns.put("parentid", PARENT_ID);

		columns.put("type", TYPE);

		columns.put("msgtext", MSG_TEXT);

		columns.put("msgJson", MSG_JSON);
		columns.put("fromUser", FROMUSER);
		columns.put("toName", TONAME);
		columns.put("toUser", TOUSER);
		columns.put("msgSrc", MSG_SRC);
		columns.put("status", STATUS);
		columns.put("serial", SERIAL);
		columns.put("fromName", FROMNAME);

	};

	public static Map<String, String> getORM()
	{
		return columns;
	}
}
