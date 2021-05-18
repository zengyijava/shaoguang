/**
 * Program  : GlobalMethods.java
 * Author   : chensj
 * Create   : 2013-6-9 上午08:52:39
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.client.dao;

import java.util.logging.Logger;


/**
 * 
 * @author   chensj <510061684@qq.com>
 * @version  1.0.0
 * @2013-6-9 上午08:52:39
 * 全局共用的方法
 */
public class GlobalMethods {
	
	
	private static final Logger logger = Logger.getLogger("GlobalMethods");
	
	private GlobalMethods(){}
    /**
     * 判断是否为无效的字符串
     * @author chensj
     * @create 2013-6-9 上午08:54:56
     * @since  undefineds null "" -->true
     * @param arg
     * @return
     */
	public static boolean isInvalidString(String arg){
		
		return (arg==null || "".equals(arg)
				|| "undefined".equals(arg))?true:false;
	}
	public static boolean isNullObject(Object o){
		return (o==null)?true:false;
	}
	public static Long swicthLong(String s) {
		if(!isInvalidString(s)){
			return Long.parseLong(s);
		}
		return null;
	}
	
}

