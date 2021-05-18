package com.montnets.emp.util;

import java.util.*;

import org.json.simple.JSONObject;

public class StringUtils
{
	public static final String EMPTY = "";
	
    public static final int INDEX_NOT_FOUND = -1;
    
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }
    
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isNotBlank(String str) {
        return !StringUtils.isBlank(str);
    }
    
    public static String getFirstCharStr(String methodStr){
    	return Character.toUpperCase(methodStr.charAt(0))+ methodStr.substring(1);
    }
    
	public static boolean IsNullOrEmpty(String str){
		return (str == null || " ".equals(str) || str.length() == 0);
	}
	
	private static Random random = new Random();
	
	/**
	 * 获取随机数
	 * @return
	 */
	public static String getRandom(){
		return String.valueOf((double)new Date().getTime()*random.nextFloat());
	}
	
	/**
	 * 手机号码解析
	 * @param mobile
	 * @return
	 */
	public static String parseMobile(String mobile){
		if(mobile == null){
			return "";
		}
		if(mobile.length() == 12 && "0".equals(mobile.substring(0, 1)) && !"00".equals(mobile.substring(0, 2))){
			mobile = mobile.substring(1,mobile.length());
		}else if(mobile.length() == 14 && (mobile.startsWith("+86") ||
				mobile.startsWith("086"))){
			mobile = mobile.substring(3,14);
		}else if(mobile.length() == 15 && mobile.startsWith("0086")){
			mobile = mobile.substring(4,15);
		}else if(mobile.length() == 13 && mobile.startsWith("86")){
			mobile = mobile.substring(2,13);
		}
		return mobile;
	}
	
	/**
	 * 传入map转换成json格式
	 * @param objMap
	 * @return
	 */
	public String getJsonStr(Map<String,Object> objMap)
	{
		JSONObject resultJson = new JSONObject();
		
		if(objMap == null){
			return "";
		}
		Iterator<String> it = objMap.keySet().iterator();
		
		while(it.hasNext()){
			String key = it.next();
			resultJson.put(key, objMap.get(key));
		}
		
		return resultJson.toString();
	}
	
	/**
	 * 转义html标签
	 * @param str
	 * @return
	 */
	public static String escapeString(String str)
	{
		str = str.replace("&", "&amp;");
		str = str.replace("<", "&lt;");
		str = str.replace(">", "&gt;");
		
		return str;
	}

	/**
	 * 字符串等分为list
	 * @param str
	 * @return
	 */
	public static List<String> strToList(String str){
		//截取长度
		int spLength = 1000;
		if (isBlank(str)){
			return null;
		}
		List<String> list = new ArrayList<String>();
		//长度
		int strLength = str.length();
		//截取数量
		int subNum = strLength/spLength;

		for (int i = 0;i<subNum;i++){
			int beg = i*spLength;
			int end = (i+1)*spLength;
			list.add(str.substring(beg,end));
		}
		String lastStr = str.substring(spLength*subNum,str.length());
		if (StringUtils.isNotBlank(lastStr)){
			list.add(lastStr);
		}
		return list;
	}

	/**
	 * list拼接为string
	 * @param list
	 * @return
	 */
	public static String listToStr(List<String> list){
		if (null == list || list.size()<1){
			return "";
		}
		StringBuffer strResult = new StringBuffer();
		for (String str : list){
			strResult.append(str);
		}
		return String.valueOf(strResult);
	}
}
