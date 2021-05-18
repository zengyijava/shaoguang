package com.montnets.emp.shorturl.surlmanage.util;

public class HttpClientUtil {
	public static final String UTF_8="utf-8";
	//扫描地址禁用线程运行标识
    protected static boolean SYSTEM_THREAD_RUN_FLAG = true;
	
	 /**
     * @param str
     * @return    
     *
     * @Description: 字符串是否为""或者null
    */
   public static boolean isBlankStr(String str){
       boolean result=false;
       if(null==str||"".equals(str)){
           result=true;
       }
       return result;
   }

	public static boolean isSYSTEM_THREAD_RUN_FLAG() {
		return SYSTEM_THREAD_RUN_FLAG;
	}

	public static void setSYSTEM_THREAD_RUN_FLAG(boolean sYSTEM_THREAD_RUN_FLAG) {
		SYSTEM_THREAD_RUN_FLAG = sYSTEM_THREAD_RUN_FLAG;
	}
   
}
