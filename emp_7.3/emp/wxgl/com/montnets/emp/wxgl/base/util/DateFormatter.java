/**
 * Program  : DateFormatter.java
 * Author   : chensj
 * Create   : 2013-6-13 下午05:10:52
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.wxgl.base.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.ottbase.util.GlobalMethods;

/**
 * 
 * @author   chensj <510061684@qq.com>
 * @version  1.0.0
 * @2013-6-13 下午05:10:52
 */
public class DateFormatter {
	/**
	 * 将日期对象转化成字符串格式yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String DateToString(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
	    return sdf.format(date);
	    
	}
	
	/**
	 * 将字符串格式化为日期对象
	 * @param s
	 * @return
	 */
	public static Date StringToDate(String s){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		if(!GlobalMethods.isInvalidString(s)){
			try {
				return sdf.parse(s);
			} catch (ParseException e) {
				EmpExecutionContext.error(e,"日期转换失败！");
			}
		}
		return null;
	}
	
	/**
	 * 创建当前时间对象
	 * @return
	 */
	public static Timestamp createDate(){
		Timestamp tt=new Timestamp(new Date().getTime());
		return tt;
	}
}

