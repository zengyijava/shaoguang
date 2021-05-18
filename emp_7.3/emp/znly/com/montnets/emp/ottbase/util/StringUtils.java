package com.montnets.emp.ottbase.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
	private static Random random=new Random();
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
		if(mobile.length() == 12 && "0".equals(mobile.substring(0, 1))){
			mobile = mobile.substring(1,mobile.length());
		}else if(mobile.length() == 14 && (mobile.startsWith("+86") ||
				mobile.startsWith("086"))){
			mobile = mobile.substring(3,14);
		}else if(mobile.length() == 15 && mobile.startsWith("0086")){
			mobile = mobile.substring(4,15);
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
     * 时间字符串转换
     * @description 返回格式化后的时间字符串   
     * @param dt 需格式化的时间
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-11-29 下午03:15:51
     */
    public static String timeFormat(Timestamp dt)
    {
    	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(dt);
    }
    
    public static Timestamp parseTime(String timeStr)
    {
    	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
           return new Timestamp(df.parse(timeStr).getTime());
        }
        catch (ParseException e)
        {
            return null;
        }
    }
    /**
     * 将时间格式字符串转化为长整形数据
     * @description    
     * @param timeStr
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-23 上午08:56:16
     */
    public static long parseTimeToLong(String timeStr)
    {
    	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
           return df.parse(timeStr).getTime();
        }
        catch (ParseException e)
        {
            return 0l;
        }
    }
    
    /**
     * 时间字符串转换
     * @description 返回格式化后的时间字符串   
     * @param dt 需格式化的时间
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-11-29 下午03:15:51
     */
    public static String timeFormat(Date dt)
    {
    	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(dt);
    }
    
    /**
     * 解析json数组
     * @description 将json数组的字符串转换为json对象    
     * @param jsonArrStr
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-11-29 下午03:16:58
     */
    public static JSONArray parsJsonArrStr(String jsonArrStr)
    {
        JSONArray jsonObject;
        try
        {
            InputStream inputStream = new ByteArrayInputStream(jsonArrStr.getBytes("UTF-8"));
            jsonObject = (JSONArray) new JSONParser().parse(new BufferedReader(new InputStreamReader(inputStream, "UTF-8")));
        }
        catch (Exception e)
        {
            return null;
        }
        return jsonObject;
    }
    
    /**
     * 解析json数据
     * @description  将json格式的字符串转化为json对象  
     * @param jsonStr
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-11-29 下午03:17:38
     */
    public static JSONObject parsJsonObj(String jsonStr)
    {
        JSONObject jsonObject;
        try
        {
        	if(jsonStr.indexOf("\r\n")>-1 || jsonStr.indexOf("\n") > -1 || jsonStr.indexOf("\\n") > -1)
        	{
        		jsonStr = jsonStr.replace("\r\n", "/huanhang/").replace("\n", "/huanhang/").replace("\\n", "/huanhang/");
        	}
            InputStream inputStream = new ByteArrayInputStream(jsonStr.getBytes("UTF-8"));
            jsonObject = (JSONObject) new JSONParser().parse(new BufferedReader(new InputStreamReader(inputStream, "UTF-8")));
        }
        catch (Exception e)
        {
           return null;
        }
        return jsonObject;
    }
    
    /**
     * 解析json数据
     * @description 将key:value,key2:value2形式的字符串转换为json格式数据    
     * @param params key:value,key2:value2形式的字符串
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-16 上午09:34:16
     */
    public static JSONObject parsJsonParam(String params)
    {
        JSONObject jsonObject = new JSONObject();
        if(params != null && params.length() > 0)
        {
            String[] paramArr = params.split(",");
            for(int i = 0;i < paramArr.length; i ++ )
            {
                String[] vaa = paramArr[i].split(":");
                jsonObject.put(vaa[0], vaa[1]);
            }
        }
        return jsonObject;
    }
    
    /**
     * 字符串转化为时间格式
     * @description    
     * @param time
     * @return
     * @throws ParseException       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-2-19 下午05:27:31
     */
    public static Date parsToDate(String time) throws ParseException
    {
    	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.parse(time);
    }
}
