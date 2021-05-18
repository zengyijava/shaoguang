package com.montnets.emp.rms.wbs.util;

import java.util.Calendar;

public class StringUtils {
	/**
	 * 获取时间
	* @Title: getLongTime 
	* @Description: TODO
	* @param @return    设定文件 
	* @return Long    返回类型 
	* @throws
	 */
	public static Long getLongTime() {
		Calendar c = Calendar.getInstance();
		return c.getTimeInMillis();
	}


}
