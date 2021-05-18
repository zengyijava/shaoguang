package com.montnets.emp.qyll.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * 字符编码工具类
 * @author xy201
 *
 */
public class CharacterEncodeUtil {
	
	public static String getEncode(String str){
		String enCode = "UTF-8";
		if(Charset.forName("GB2312").newEncoder().canEncode(str)){
			enCode = "GB2312";
		}
		return enCode;
	}
	 
	/** 
	 * 获取字符串编码
	 * 
	 * @param str 
	 * @return 
	 */  
	public static String getEncoding(String str) {  
	    String encode[] = new String[]{  
	            "UTF-8",  
	            "ISO-8859-1",  
	            "GB2312",  
	            "GBK",  
	            "GB18030",  
	            "Big5",  
	            "Unicode",  
	            "ASCII"  
	    };  
	    for (int i = 0; i < encode.length; i++){  
	        try {  
	            if (str.equals(new String(str.getBytes(encode[i]), encode[i]))) {  
	                return encode[i];  
	            }  
	        } catch (Exception ex) {  
	        }  
	    }  
	      
	    return "";  
	}  
	
//	public static void main(String[] args) throws UnsupportedEncodingException {
//		  BufferedReader br = new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
//		  String read = null;
//		  System.out.print("输入数据：");
//		  try {
//		   read = br.readLine();
//		  } catch (IOException e) {
//		   e.printStackTrace();
//		  }
//		  System.out.println("输入数据："+read); 
//	}

}
