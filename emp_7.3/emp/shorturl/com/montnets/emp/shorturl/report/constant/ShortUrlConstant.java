package com.montnets.emp.shorturl.report.constant;

import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;

public class ShortUrlConstant {

	public static final String DB_IP = "montnets.emp.shorturl.databaseIp";
	public static final String DB_PORT = "montnets.emp.shorturl.databasePort";
	public static final String DB_NAME = "montnets.emp.shorturl.databaseName";
	public static final String DB_USER = "montnets.emp.shorturl.user";
	public static final String DB_PASS = "montnets.emp.shorturl.password";
	public static final String DB_TRENSFER_TIME = "montnets.emp.shorturl.transfer.time";
	public static final String DB_TRENSFER_PER_DATA = "montnets.emp.shorturl.transfer.perData";
	protected static Long dbTransferTime;
	protected static Long dbStopTime;
	protected static Integer dbTransferPerData;

	static {
		try {
			String dbTransferPerDataStr = SystemGlobals.getValue(DB_TRENSFER_PER_DATA);
			dbTransferPerDataStr = (null != dbTransferPerDataStr && !"".equals(dbTransferPerDataStr.trim())) ? dbTransferPerDataStr : "1000";
			dbTransferPerData = Integer.valueOf(dbTransferPerDataStr);

			String time = SystemGlobals.getValue(ShortUrlConstant.DB_TRENSFER_TIME);
			time = (null != time && !"".equals(time.trim())) ? time : "1000";
			dbTransferTime = Long.valueOf(time) * 60 * 1000;
			dbStopTime = Long.valueOf(time) * 40 * 1000;
		} catch (NumberFormatException e) {
			EmpExecutionContext.error("企业短链报表同步线程配置每次同步的数据量异常！");
		}
	}

	public static Long getDbTransferTime() {
		return dbTransferTime;
	}

	public static void setDbTransferTime(Long dbTransferTime) {
		ShortUrlConstant.dbTransferTime = dbTransferTime;
	}

	public static Long getDbStopTime() {
		return dbStopTime;
	}

	public static void setDbStopTime(Long dbStopTime) {
		ShortUrlConstant.dbStopTime = dbStopTime;
	}

	public static Integer getDbTransferPerData() {
		return dbTransferPerData;
	}

	public static void setDbTransferPerData(Integer dbTransferPerData) {
		ShortUrlConstant.dbTransferPerData = dbTransferPerData;
	}
	
}
