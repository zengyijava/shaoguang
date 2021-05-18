/**
 * 
 */
package com.montnets.emp.table.approveflow;

import java.util.HashMap;
import java.util.Map;

/**
 * 审核流程记录
 * 
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-2-19 下午01:42:29
 * @description
 */

public class TableLfFlowRecord {

	// 表名_审核流程记录
	public static final String TABLE_NAME = "LF_FLOWRECORD";
	// 标识列
	public static final String FR_ID = "FR_ID";
	// 对应的审批任务ID
	public static final String MT_ID = "MT_ID";
	// 上级审批人的操作员ID
	public static final String PRE_RV = "PRE_RV";
	// 当前审批人ID
	public static final String REVIEWER = "REVIEWER";
	// 当前审批级别
	public static final String R_LEVEL = "R_LEVEL";
	// 审批总级别
	public static final String R_LEVELAMOUNT = "R_LEVELAMOUNT";
	// 审批意见
	public static final String R_CONTENT = "R_CONTENT";
	// 审批状态
	public static final String R_STATE = "R_STATE";
	// 备注
	public static final String COMMENTS = "COMMENTS";
	// 审批时间
	public static final String R_TIME = "R_TIME";
	// 审核流程ID
	public static final String F_ID = "F_ID";
	// 审核类型(1-代表短信审核;2-代表彩信审核;3-代表短信模板审核;4-代表彩信模板审核)
	public static final String REVIEW_TYPE = "REVIEW_TYPE";
	// 序列名
	public static final String SEQUENCE = "S_LF_FLOWRECORD";
	// 审批指令(同意)
	public static final String BATCHNUMBER = "BATCHNUMBER";
	// 审批指令(不同意)
	public static final String DISAGREENUMBER = "DISAGREENUMBER";
	//信息类型。1：短信模板；2：彩信模板；3：短信发送；4：彩信发送；
	public static final String INFO_TYPE = "INFO_TYPE";
	//发起流程的操作员唯一编码
	public static final String PROUSERCODE = "PROUSERCODE";
	//审核类型，1-操作员，2-群组，3-上级，4-机构审核员,5-逐级审核
	public static final String R_TYPE = "R_TYPE";
	//审核人操作员编码
	public static final String USER_CODE = "USER_CODE";
	//1全部通过生效;2第一人审核生效
	public static final String R_CONDITION = "R_CONDITION";
	//1：流程结束。2：流程未结束
	public static final String IS_COMPLETE = "IS_COMPLETE";
	//任务提交时间
	public static final String SUBMITTIME = "SUBMITTIME";
	//是否已催办
	public static final String ISREMIND = "ISREMIND";
	//催办时间
	public static final String REMINDTIME = "REMINDTIME";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfFlowRecord", TABLE_NAME);
		columns.put("tableId", FR_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("frId", FR_ID);
		columns.put("mtId", MT_ID);
		columns.put("preRv", PRE_RV);
		columns.put("reviewer", REVIEWER);
		columns.put("RLevel", R_LEVEL);
		columns.put("RLevelAmount", R_LEVELAMOUNT);
		columns.put("RContent", R_CONTENT);
		columns.put("RState", R_STATE);
		columns.put("comments", COMMENTS);
		columns.put("RTime", R_TIME);
		columns.put("FId", F_ID);
		columns.put("reviewType", REVIEW_TYPE);
		columns.put("batchNumber", BATCHNUMBER);
		columns.put("disagreeNumber", DISAGREENUMBER);
		columns.put("infoType", INFO_TYPE);
		columns.put("ProUserCode", PROUSERCODE);
		columns.put("rType", R_TYPE);
		columns.put("userCode", USER_CODE);
		columns.put("rCondition", R_CONDITION);
		columns.put("isComplete", IS_COMPLETE);
		columns.put("submitTime", SUBMITTIME);
		columns.put("isremind", ISREMIND);
		columns.put("remindTime", REMINDTIME);
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
