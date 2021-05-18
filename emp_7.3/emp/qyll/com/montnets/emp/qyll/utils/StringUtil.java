package com.montnets.emp.qyll.utils;

import java.util.Calendar;

import com.montnets.emp.common.context.EmpExecutionContext;

public class StringUtil {
	
	/**
	 * 检测字符串是否为空
	 * @param s
	 * @return
	 */
	public static boolean isNullOrEmpty(CharSequence s) {
		return (s == null || s.length() <= 0);
	} 
	/**
	 * 截取订单编号中的年份
	 * 订单编号格式[EMP]+YYYYMMDD+序列号
	 * @param orderNo
	 * @return
	 */
	public static String subOrderNoYear(String orderNo){
		String year = "";
		int nowYear = Calendar.getInstance().get(Calendar.YEAR);
		
		try {
			if(orderNo.startsWith("EMP")){//以EMP开头
				year = orderNo.substring(3,7);//EMP2017xxxxxxx
			}else{
				year = orderNo.substring(0,4);//2017xxxxxxx
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"根据订单编号截取年份失败！采用当前年份："+nowYear);
			year = String.valueOf(nowYear);
			return year;
		}
		
		//判断所截取的年份是否为当前年份或者前一年，若不是，设置为当前年份
		if(!String.valueOf(nowYear).equals(year) && !String.valueOf( nowYear - 1).equals(year)){
			year = String.valueOf(nowYear);
		}
		
		return year;
	}
	
	/**
	 * 检测字符串是否为空或者为空格
	 * @param s
	 * @return
	 */
	public static boolean isNullOrBlank(String s) {
		return (s == null || s.trim().length() <= 0);
	}
	
}
