package com.montnets.emp.table.sysuser;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-21 下午03:28:07
 * @description
 */
public class TableLfFlow {

	//表名_审核流程表
	public static final String TABLE_NAME = "LF_FLOW";
	
	//审核流ID
	public static final String F_ID = "F_ID";
	//审核任务名
	public static final String F_TASK = "F_TASK";
	//审核类型（手工业务账户0、关键字过滤1）
	public static final String F_TYPE = "F_TYPE";
	//审批级数(1-5级)从一级开始审批，先审核一级才能审核二级，然后才能审核三级，如此类推，一直审核到五级
	public static final String R_LEVELAMOUNT = "R_LEVELAMOUNT";
	//发送账号(SP账号)
	public static final String USERID = "USERID";
	//条数
	public static final String MSG_ACCOUNT = "MSG_ACCOUNT";
	//备注
	public static final String COMMENTS = "COMMENTS";
	//企业编号
	public static final String CORP_CODE = "CORP_CODE";
	////审核流创建人id
	public static final String CREATE_USER_ID = "CREATE_USER_ID";
	//审批流程暂时用的字符串
	public static final String FLOWSHOW = "FLOWSHOW";
	//序列名
	public static final String SEQUENCE = "16";
	//是否启用审批提醒 1.启用  2.禁用
	public static final String REMIND_STATE = "REMIND_STATE";
	//修改时间
	public static final String UPDATE_TIME = "UPDATE_TIME";
	//新增时间
	public static final String CREATE_TIME = "CREATE_TIME";
	
	public static final String FLOWSTATE = "FLOWSTATE";
	
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static {
		columns.put("LfFlow", TABLE_NAME);
		columns.put("tableId", F_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("FId", F_ID);
		columns.put("FTask", F_TASK);
		columns.put("FType", F_TYPE);
		columns.put("RLevelAmount", R_LEVELAMOUNT);
		columns.put("userId", USERID);
		columns.put("msgAccount", MSG_ACCOUNT);
		columns.put("comments", COMMENTS);
		columns.put("corpCode", CORP_CODE);
		columns.put("flowshow", FLOWSHOW);
		columns.put("createUserId", CREATE_USER_ID);
		columns.put("remindState", REMIND_STATE);
		columns.put("updateTime", UPDATE_TIME);
		columns.put("createTime", CREATE_TIME);
		columns.put("flowState", FLOWSTATE);
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
