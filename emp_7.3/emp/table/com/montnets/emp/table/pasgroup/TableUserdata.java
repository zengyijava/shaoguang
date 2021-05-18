package com.montnets.emp.table.pasgroup;

import com.montnets.emp.common.constant.StaticValue;

import java.util.HashMap;
import java.util.Map;


public class TableUserdata
{

	public static final String TABLE_NAME = "USERDATA";

	public static final String TABLE_ID = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE?"UID":"\"UID\"";

	public static final String UID = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE?"UID":"\"UID\"";

	public static final String USER_ID = "USERID";

	public static final String LOGIN_ID = "LOGINID";

	public static final String STAFF_NAME = "STAFFNAME";

	public static final String CORP_ACCOUNT = "CORPACCOUNT";

	public static final String USER_TYPE = "USERTYPE";

	public static final String STATUS = "STATUS";

	public static final String USER_PASSW = "USERPASSWORD";

	public static final String FEE_FLAG = "FEEFLAG";

	//public static final String KHDATE = "KHDATE";

	public static final String ORDERTIME = "ORDERTIME";
	
	//public static final String LXR = "LXR";

	//public static final String LXRPH = "LXRPH";

	public static final String RISE_LEVEL = "RISELEVEL";

	public static final String MO_URL = "MOURL";

	public static final String RPTURL = "RPTURL";
	//SP账号类型	(1短信SP账号，2彩信SP账号)
	public static final String ACCOUNTTYPE = "ACCOUNTTYPE";
	
	public static final String SEQUENCE = "SEQ_USERDATA";
	
	public static final String SPTYPE = "SPTYPE";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("Userdata", TABLE_NAME);
		columns.put("tableId", TABLE_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("uid", UID);
		columns.put("userId", USER_ID);
		columns.put("loginId", LOGIN_ID);
		columns.put("staffName", STAFF_NAME);
		columns.put("corpAccount", CORP_ACCOUNT);
		columns.put("userType", USER_TYPE);
		columns.put("status", STATUS);
		columns.put("userPassword", USER_PASSW);
		columns.put("feeFlag", FEE_FLAG);
		//columns.put("khdate", KHDATE);
		columns.put("orderTime",ORDERTIME);
		//columns.put("lxr", LXR);
		//columns.put("lxrph", LXRPH);
		columns.put("riseLevel", RISE_LEVEL);
		columns.put("moUrl", MO_URL);
		columns.put("rptUrl", RPTURL);
		columns.put("accouttype", ACCOUNTTYPE);
		columns.put("sptype", SPTYPE);
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
