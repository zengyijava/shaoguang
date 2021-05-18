package com.montnets.emp.table.sysuser;

import java.util.HashMap;
import java.util.Map;

/**
 * 审核开关表
 * @project p_tabl
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-7-19 上午11:22:53
 * @description
 */
public class TableLfReviewSwitch {

	//审核开关表
	public static final String TABLE_NAME = "LF_REVIEWSWITCH";
	//自增ID
	public static final String ID = "ID";
	//模块编码。
	public static final String MENUCODE = "MENUCODE";
	//开关状态。1：开；2：关
	public static final String SWITCH_TYPE = "SWITCH_TYPE";
	//审批阀值，默认值0，即没阀值，针对发送模块
	public static final String MSG_COUNT = "MSG_COUNT";
	//信息类型。1：短信模板；2：彩信模板；3：短信发送；4：彩信发送；
	public static final String INFO_TYPE = "INFO_TYPE";
	//修改时间
	public static final String UPDATE_TIME = "UPDATE_TIME";
	//企业编号
	public static final String CORP_CODE = "CORP_CODE";
	
	//序列名
	public static final String SEQUENCE = "S_LF_REVIEWSWITCH";
	
	
	
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfReviewSwitch", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("menuCode", MENUCODE);
		columns.put("switchType", SWITCH_TYPE);
		columns.put("msgCount", MSG_COUNT);
		columns.put("infoType", INFO_TYPE);
		columns.put("corpCode", CORP_CODE);
		columns.put("updateTime", UPDATE_TIME);
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
