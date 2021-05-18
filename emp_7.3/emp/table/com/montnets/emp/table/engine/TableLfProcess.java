package com.montnets.emp.table.engine;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 下午02:03:54
 * @description
 */
public class TableLfProcess
{
	public static final String TABLE_NAME = "LF_PROCESS";

	public static final String PR_ID = "PR_ID";
	// 库连接ID
	public static final String DB_ID = "DB_ID";
	// 服务业务id
	public static final String SER_ID = "SER_ID";

	public static final String SG_ID = "SG_ID";
	// 步骤说明
	public static final String PR_NAME = "PR_NAME";
	// 描述
	public static final String COMMENTS = "COMMENTS";
	// 1增,2删,3改，4是查询5是回复
	public static final String PR_TYPE = "PR_TYPE";
	// 是否（0否，1是）为最后处理步骤
	public static final String FINAL_STATE = "FINAL_STATE";

	public static final String GR_STATE = "GR_STATE";
	// 处理结果（成功/失败）
	public static final String PR_RESULT = "PR_RESULT";

	public static final String PRO_TABLE_NAME = "TABLE_NAME";
	// SQL语句
	public static final String SQL = "SQL_STR";
	// 下一步ID（OR步骤序号）
	public static final String NEXTPR_ID = "NEXTPR_ID";

	public static final String SYS_ERROR = "SYS_ERROR";

	public static final String DB_ERROR = "DB_ERROR";
	// 步骤序号
	public static final String PR_NO = "PR_NO";
	// 上一步ID
	public static final String PREPR_ID = "PREPR_ID";
	//指令代码
	public static final String PRO_CODE = "PRO_CODE";
	//消息分隔符
	public static final String MSG_SEPARATED = "MSG_SEPARATED";
	//短信类型 0为下行短信，1为上行短信
	public static final String MSG_TYPE = "MSG_TYPE";
	//短信内容
	public static final String MSG_CONTENT = "MSG_CONTENT";
	//模板ID
	public static final String TEMPLATE_ID = "TEMPLATE_ID";
	
	//序列
	public static final String SEQUENCE = "S_LF_PROCESS";

	//映射集合
	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfProcess", TABLE_NAME);
		columns.put("tableId", PR_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("prId", PR_ID);
		columns.put("dbId", DB_ID);
		columns.put("serId", SER_ID);
		columns.put("sgId", SG_ID);
		columns.put("prName", PR_NAME);
		columns.put("comments", COMMENTS);
		columns.put("prType", PR_TYPE);
		columns.put("finalState", FINAL_STATE);
		columns.put("grState", GR_STATE);
		columns.put("prResult", PR_RESULT);
		columns.put("tableName", PRO_TABLE_NAME);
		columns.put("sql", SQL);
		columns.put("nextPrId", NEXTPR_ID);
		columns.put("sysError", SYS_ERROR);
		columns.put("dbError", DB_ERROR);
		columns.put("prNo", PR_NO);
		columns.put("prePrId", PREPR_ID);
		columns.put("proCode", PRO_CODE);
		columns.put("msgSeparated", MSG_SEPARATED);
		columns.put("msgType", MSG_TYPE);
		columns.put("msgContent", MSG_CONTENT);
		columns.put("templateId", TEMPLATE_ID);
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
