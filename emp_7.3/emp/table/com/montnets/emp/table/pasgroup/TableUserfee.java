package com.montnets.emp.table.pasgroup;

import com.montnets.emp.common.constant.StaticValue;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:30:56
 * @description
 */
public class TableUserfee {

	public static final String TABLE_NAME = "USERFEE";
	public static final String UID = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE?"UID":"\"UID\"";
	public static final String ECID = "ECID";
	public static final String USER_ID = "USERID";
	public static final String SEND_NUM = "SENDNUM";
	public static final String SENDED_NUM = "SENDEDNUM";
	public static final String POST_PAY_USED = "POSTPAYUSED";
	public static final String THRES_HOLD = "THRESHOLD";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("Userfee",TABLE_NAME);
		columns.put("tableId", UID);
		columns.put("uid", UID);
		columns.put("ecid", ECID);
		columns.put("userId", USER_ID);
		columns.put("sendNum", SEND_NUM);
		columns.put("sendedNum", SENDED_NUM);
		columns.put("postPayUsed",POST_PAY_USED);
		columns.put("thresHold", THRES_HOLD);
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM() {
		return columns;
	}
}
