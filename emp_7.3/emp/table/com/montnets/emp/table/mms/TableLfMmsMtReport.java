/**
 * 
 */
package com.montnets.emp.table.mms;

import java.util.HashMap;
import java.util.Map;

/**
 * 彩信下行汇总实体类
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-10-14 上午09:38:44
 * @description 
 */

public class TableLfMmsMtReport
{
	//表名_彩信下行汇总
	public static final String TABLE_NAME = "LF_MMS_MTREPORT";
	//用户账号
	public static final String USER_ID = "USERID";
	//任务ID
	public static final String TASK_ID = "TASKID";
	//主端口
	public static final String SPGATE = "SPGATE";
	//年月日
	public static final String IYMD = "IYMD";
	//小时
	public static final String IHOUR = "IHOUR";
	
	public static final String PT_CODE = "PTCODE";
	//月份
	public static final String IMONTH = "IMONTH";
	//发送总数
	public static final String ICOUNT = "ICOUNT";

	public static final String SUCC = "SUCC";

	public static final String FAIL1 = "FAIL1";

	public static final String FAIL2 = "FAIL2";
	//
	public static final String FAIL3 = "FAIL3";
	//
	public static final String NRET = "NRET";
	//成功数包括未返
	public static final String RSUCC = "RSUCC";
	//条件失败
	public static final String RFAIL1 = "RFAIL1";
	//失败2数量(真正的失败数)
	public static final String RFAIL2 = "RFAIL2";
	//未返数
	public static final String RNRET = "RNRET";
	//...
	public static final String RELEASE_FLAG = "RELEASEFLAG";
	//开始时间
	public static final String START_TIME = "STARTTIME";
	//结束时间
	public static final String END_TIME = "ENDTIME";
	//标识列
	public static final String ID = "ID";
	//年
	public static final String Y = "Y";

	//public static final String OP_CONTENT = "OP_CONTENT";
	//运营商:(0: 移动,1: 联通,21: 电信)
	public static final String SPISUNCM = "SPISUNCM";
	//序列
	public static final String SEQUENCE = "S_LF_MMS_MTREPORT";

	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfMmsMtReport", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("userId", USER_ID);
		columns.put("taskId", TASK_ID);
		columns.put("spgate", SPGATE);
		columns.put("iymd", IYMD);
		columns.put("ihour", IHOUR);
		columns.put("ptCode", PT_CODE);
		columns.put("imonth", IMONTH);
		columns.put("icount", ICOUNT);
		columns.put("succ", SUCC);
		columns.put("fail1", FAIL1);
		columns.put("fail2", FAIL2);
		columns.put("fail3", FAIL3);
		columns.put("nret", NRET);
		columns.put("rsucc", RSUCC);
		columns.put("rfail1", RFAIL1);
		columns.put("rfail2", RFAIL2);
		columns.put("rnret", RNRET);
		columns.put("releaseFlag", RELEASE_FLAG);
		columns.put("startTime", START_TIME);
		columns.put("endTime", END_TIME);
		columns.put("id", ID);
		columns.put("y", Y);
		columns.put("spisuncm", SPISUNCM);
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
