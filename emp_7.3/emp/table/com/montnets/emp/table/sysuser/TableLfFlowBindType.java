package com.montnets.emp.table.sysuser;

import java.util.HashMap;
import java.util.Map;

/**
 * 审核流程绑定类型
 * @project p_tabl
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-7-19 上午11:22:53
 * @description
 */
public class TableLfFlowBindType {

	//审核开关表
	public static final String TABLE_NAME = "LF_FLOWBINDTYPE";
	//自增ID
	public static final String ID = "ID";
	//模块编码。
	public static final String MENUCODE = "MENUCODE";
	//审核流ID
	public static final String F_ID = "F_ID";
	//信息类型。1：短信发送；2：彩信发送；3：短信模板；4：彩信模板；
	public static final String INFO_TYPE = "INFO_TYPE";
	//修改时间
	public static final String UPDATE_TIME = "UPDATE_TIME";
	//新增时间
	public static final String CREATE_TIME = "CREATE_TIME";
	
	//序列名
	public static final String SEQUENCE = "S_LF_FLOWBINDTYPE";
	
	
	
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static {
		columns.put("LfFlowBindType", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("FId", F_ID);
		columns.put("menuCode", MENUCODE);
		columns.put("infoType", INFO_TYPE);
		columns.put("updateTime", UPDATE_TIME);
		columns.put("createTime", CREATE_TIME);
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
