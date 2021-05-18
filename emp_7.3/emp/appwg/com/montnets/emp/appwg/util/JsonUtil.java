package com.montnets.emp.appwg.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.montnets.emp.common.context.EmpExecutionContext;

public class JsonUtil
{
	/**
	 * 解析json字符串并封装成json对象
	 * @param jsonStr
	 * @return
	 */
	public static JSONObject parsJsonObj(String jsonStr)
    {
        try
        {
			jsonStr = string2Json(jsonStr);
        	if(jsonStr == null){
        		jsonStr = "";
			}
            InputStream inputStream = new ByteArrayInputStream(jsonStr.getBytes("UTF-8"));
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            Object obj = new JSONParser().parse(bf);
            JSONObject jsonObject = (JSONObject) obj;
            return jsonObject;
        }
        catch (Exception e)
        {
        	EmpExecutionContext.error(e, "json字符串并封装成json对象异常。json:"+jsonStr);
            return null;
        }
        
    }
	
	/**
	 * 把json格式字符串转换为合法的json
	 * @param s
	 * @return
	 */
	public static String string2Json(String s) {
		try
		{
			if(s == null || s.length() == 0){
				return "";
			}
			StringBuffer sb = new StringBuffer ();     
			for (int i=0; i<s.length(); i++) {    
   
			    char c = s.charAt(i);     
			    switch (c) {     
			    /*case '\"':     
			        sb.append("\\\"");     
			        break;*/     
			    /*case '\\':     
			        sb.append("\\\\");
			        break; */    
			    /*case '/':     
			        sb.append("\\/");
			        break; */    
			    case '\b':     
			        sb.append("\\b");
			        break;     
			    case '\f':     
			        sb.append("\\f");
			        break;     
			    case '\n':     
			        sb.append("\\n");
			        break;     
			    case '\r':     
			        sb.append("\\r");
			        break;     
			    case '\t':     
			        sb.append("\\t");
			        break;  
			    default:     
			        sb.append(c);     
			    }
			}
			return sb.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "把json格式字符串转换为合法的json异常。json:"+s);
			return null;
		}     
	 }
	
	/**
	 * 处理字符串，符合json格式
	 * @param s
	 * @return
	 */
	public static String stringForJson(String s) {
		try
		{
			if(s == null || s.length() == 0){
				return "";
			}
			StringBuffer sb = new StringBuffer ();     
			for (int i=0; i<s.length(); i++) {    
   
			    char c = s.charAt(i);     
			    switch (c) {     
			    case '\"':     
			        sb.append("\\\"");     
			        break;   
			    case '\\':     
			        sb.append("\\\\");
			        break;     
			    /*case '/':     
			        sb.append("\\/");
			        break;*/     
			    case '\b':     
			        sb.append("\\b");
			        break;     
			    case '\f':     
			        sb.append("\\f");
			        break;     
			    case '\n':     
			        sb.append("\\n");
			        break;     
			    case '\r':     
			        sb.append("\\r");
			        break;     
			    case '\t':     
			        sb.append("\\t");
			        break;  
			    default:     
			        sb.append(c);     
			    }
			}
			return sb.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理字符串为符合json格式异常。json:"+s);
			return null;
		}     
	 }

}
