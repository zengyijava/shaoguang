package com.montnets.emp.table.securectrl;

import java.util.HashMap;
import java.util.Map;

public class TableLfDynPhoneWord implements java.io.Serializable{
	private static final long serialVersionUID = -5043703922358023317L;
	//表名   手机动态口令
	public static final String TABLE_NAME = "LF_DYNPHONEWORD";
	//用户userid（外键）
	public static final String USERID = "USERID";
	//四位动态码
	public static final String PHONEWORD = "PHONEWORD";
	//创建时间
	public static final String CREATETIME = "CREATETIME";
	//当天是否是第一次登录(1：已登录,0：未登录)
	public static final String ISLOGIN ="ISLOGIN";
	//发送动态口令，网关返回msgid
	public static final String PTMSGID = "PTMSGID";
	//网关是否成功发送，即是否状态报告返回(000000:成功)
	public static final String ERRORCODE = "ERRORCODE";

	
	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfDynPhoneWord", TABLE_NAME);
		columns.put("userId", USERID);
		columns.put("phoneWord", PHONEWORD);
		columns.put("createTime", CREATETIME);
		columns.put("isLogin", ISLOGIN);
		columns.put("ptMsgId", PTMSGID);
		columns.put("errorCode", ERRORCODE);

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
