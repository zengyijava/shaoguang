/**
 * 
 */
package com.montnets.emp.ottbase.constant;

import java.util.Properties;

public class WeixMessage
{
	public static final String WEIX_MESSAGE = "WeixMessage";

	private static Properties defaults = new Properties();
	
	private WeixMessage(){}
	
	public static void setProperties(Properties properties){
		defaults = properties;
	}
	
	public static Properties getProperties(){
		return defaults;
	}
	
	
	public static String getValue(String key){
		return defaults.getProperty(key);
	}
	public static String getValue(String key,String defaultValue){
		String value = defaults.getProperty(key);
		if(value==null || "".equals(value)){
			return defaultValue;
		}
		return value;
	}
	/**
	 * 读取配置文件中的信息
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static int getIntValue(String key,int defaultValue){
		if(defaults.getProperty(key)==null){
			return defaultValue;
		}
		try{
			return Integer.parseInt(defaults.getProperty(key));
		}catch (Exception e) {
			return defaultValue;
		}
	}
	
}
