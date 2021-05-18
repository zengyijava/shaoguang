package com.montnets.emp.rms.wbs.util;

public class Validate {
	/**
	 * @Title: isNumber 
	 * @Description:
	 * 判断是否是数字,主要判断传递过来的号码提交数量、发送成功数量、接收失败数量是否是数字,是数字则返回true，否则返回false
	 * @param str  
	 * @return boolean 返回类型 @throws
	 */
	public static  boolean isNumber(String str) {
		if (str == null) {
			return false;
		}
		return str.matches("\\d+");
	}
    /**
     * 
    * @Title: isFlag 
    * @Description: 判断传入的flag是否有效，0|1为有效，返回true;其余字符串为非法,返回false
    * @param str  传入字符串
    * @param @return    设定文件 
    * @return boolean    返回类型 
    * @throws
     */
	public static  boolean isFlag(String str) {
		if (str == null) {
			return false;
		}
		return str.matches("[0]|[1]");
	}

	/**
	 * 
	 *  @Title: isNull 
	 *  @Description:判断传入的字符串是否为空，如果为空则返回true,否则返回false
	 *  @param  str 
	 *  @return boolean 返回类型 @throws
	 */
	public static  boolean isNull(String str) {
		if (str == null || str.length() <= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *  
	 * @Title: isYMD 
	 * @Description: 验证传递过来的年月日是否yyyyMMdd格式，日期格式正确则返回true,否则返回false
	 * @param str 
	 * @return boolean 返回类型 @throws
	 */
	public static  boolean isYMD(String str) {
		if (str == null) {
			return false;
		}
		if (isNumber(str)) {
			// 是数字了，要判断是否合法
			return str.matches("\\d+");
		}
		return false;
	}
}