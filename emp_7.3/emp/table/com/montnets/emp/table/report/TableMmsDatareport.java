package com.montnets.emp.table.report;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:44
 * @description
 */
public class TableMmsDatareport
{

	public static final String TABLE_NAME = "MMS_DATAREPORT";
	//用户账号
	public static final String USER_ID = "USERID";
	//任务ID
	public static final String TASK_ID = "TASKID";
	//主端口
	public static final String SPGATE = "SPGATE";
	//日期(年月日)
	public static final String IYMD = "IYMD";
	//小时
	public static final String IHOUR = "IHOUR";
	//平台编码
	public static final String PT_CODE = "PTCODE";
	//月份
	public static final String IMONTH = "IMONTH";
	//发送总数
	public static final String ICOUNT = "ICOUNT";
	//成功数
	public static final String SUCC = "SUCC";
	//失败1数量
	public static final String FAIL1 = "FAIL1";
	//失败2数量
	public static final String FAIL2 = "FAIL2";
	//失败3数量
	public static final String FAIL3 = "FAIL3";
	//未反
	public static final String NRET = "NRET";
	//成功数
	public static final String RSUCC = "RSUCC";
	//R失败1数量
	public static final String RFAIL1 = "RFAIL1";
	//R失败2数量
	public static final String RFAIL2 = "RFAIL2";
	//R未返回数量
	public static final String RNRET = "RNRET";
	//
	public static final String RELEASE_FLAG = "RELEASEFLAG";
	//开始时间
	public static final String START_TIME = "STARTTIME";
	//结束时间
	public static final String END_TIME = "ENDTIME";
	//ID自增
	public static final String ID = "ID";
	//年
	public static final String Y = "Y";
	//运营商:(0: 移动,1: 联通,21: 电信)
	public static final String SPISUNCM = "SPISUNCM";

	public static final String SEQUENCE = "MMS_DATAREPORT_ID";
	//SPID
	public static final String SPID="SPID";
	//服务类型
	public static final String SVRTYPE = "SVRTYPE";
	//自定义参数1
	public static final String P1 = "P1";
	//自定义参数2
	public static final String P2 = "P2";
	//自定义参数3
	public static final String P3 = "P3";
	//自定义参数4
	public static final String P4 = "P4";

    protected static final Map<String, String> columns = new HashMap<String, String>();
    /**
     * 加载字段
     */ 
	static
	{
		columns.put("MtDatareport", TABLE_NAME);
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
		columns.put("spID", SPID);
		columns.put("svrType", SVRTYPE);
		columns.put("p1", P1);
		columns.put("p2", P2);
		columns.put("p3", P3);
		columns.put("p4", P4);
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
