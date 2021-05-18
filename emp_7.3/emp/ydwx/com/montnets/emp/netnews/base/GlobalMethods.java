/**
 * Program  : GlobalMethods.java
 * Author   : chensj
 * Create   : 2013-6-9 上午08:52:39
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.netnews.base;
import java.util.logging.Logger;

import com.montnets.emp.util.TxtFileUtil;

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
	
	/***
	 * 转换long型
	 * @param s
	 * @return
	 */
	public static Long swicthLong(String s) {
		if(!isInvalidString(s)){
			return Long.parseLong(s);
		}
		return null;
	}
	
	/***
	 * 删除文件
	 * @param url
	 * @return
	 */
	public static boolean deleteFile(String url){
		boolean b =false;
		if(!isInvalidString(url)){
			String basePath = new TxtFileUtil().getWebRoot();
			java.io.File file  = new java.io.File(basePath + url);
			logger.info(basePath+url);
			if(file.exists()){
				b = file.delete();
			}else{
				logger.info(basePath + url + "该路径下的文件不存在！");
			}
		}
		return b;
	}
	
}

