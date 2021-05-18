/**
 * Program  : DateFormatter.java
 * Author   : chensj
 * Create   : 2013-6-13 下午05:10:52
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.client.dao;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 
 * @author   chensj <510061684@qq.com>
 * @version  1.0.0
 * @2013-6-13 下午05:10:52
 */
public class DateFormatter {
	public static String swicthDate(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
	    Date date=new Date();   
	    return sdf.format(date);
	    
	}
	public static Date swicthString(String s){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		if(!GlobalMethods.isInvalidString(s)){
			try {
				return sdf.parse(s);
			} catch (ParseException e) {
				EmpExecutionContext.error(e, "时间转化失败");
			}
		}
		return null;
	}
	public static Timestamp swicthSqltring(String s){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		if(!GlobalMethods.isInvalidString(s)){
			try {
				return new Timestamp(sdf.parse(s).getTime());
			} catch (ParseException e) {
				EmpExecutionContext.error(e, "时间转化失败");
			}
		}
		return null;
	}
	public static Timestamp sqlDate(){
		Timestamp tt=new Timestamp(new Date().getTime());
		return tt;
	}
}

