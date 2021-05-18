package com.montnets.emp.table.blacklist;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:49
 * @description
 */
public class TablePbListBlack
{

	public static final String TABLE_NAME = "PB_LIST_BLACK";

	public static final String ID = "ID";

	public static final String USER_ID = "USERID";

	public static final String SPGATE = "SPGATE";

	public static final String SPNUMBER = "SPNUMBER";

	public static final String PHONE = "PHONE";
	public static final String SVRTYPE = "SVRTYPE";
	public static final String CORPCODE = "CORPCODE";

	public static final String OP_TYPE = "OPTYPE";

	public static final String OP_TTIME = "OPTTIME";


	public static final String SEQUENCE = "PB_LIST_BLACK_ID";
	public static final String MSG = "MSG";
	//黑名单类型。1：短信；2：彩信
	public static final String BLTYPE = "BLTYPE";

    protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("PbListBlack", TABLE_NAME);
		columns.put("sequence", SEQUENCE);
		columns.put("tableId", ID);
		columns.put("id", ID);
		columns.put("userId", USER_ID);
		columns.put("spgate", SPGATE);
		columns.put("spnumber", SPNUMBER);
		columns.put("phone", PHONE);
		columns.put("svrType", SVRTYPE);
		columns.put("corpCode", CORPCODE);
		columns.put("optype", OP_TYPE);
		columns.put("optTime", OP_TTIME);
		columns.put("msg", MSG);
		columns.put("blType", BLTYPE);
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
