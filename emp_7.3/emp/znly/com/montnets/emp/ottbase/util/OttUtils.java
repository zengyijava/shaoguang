package com.montnets.emp.ottbase.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import sun.misc.BASE64Encoder;

public class OttUtils {
	
	/**
	 * 根据机构编码得到父机构编码
	 * @param depCode
	 * @return
	 */
	public static String getPartDepCode(String depCode) {

		String partDepCode = "";
		try {
			//partDepCode = depCode.replaceAll("0+$", "");
			partDepCode = depCode.replaceAll("0+$", "");
			Integer length = partDepCode.length();
			Integer defaultLength = depCode.length();
			if("".equals(partDepCode) || length ==0){
				partDepCode = "";
			}else{
				 if(length%2 == 0){
					 partDepCode = depCode.substring(0, length);
				 }else{
					 if(defaultLength > length+1){
						 partDepCode = depCode.substring(0, length+1);
					 }else{
						 partDepCode = depCode.substring(0, length);
						 partDepCode = partDepCode+"0";
					 }
				 }
			}
		} catch (NullPointerException e) {
			partDepCode = depCode;
		}
		return partDepCode;
	}
	/**
	 * 根据机构编码得到父机构编码
	 * @param depCode
	 * @param defaultValue
	 * @return
	 */
	public static String getPartDepCode(String depCode,String defaultValue) {

		String partDepCode = "";
		try {
			//partDepCode = depCode.replaceAll("0+$", "");
			partDepCode = depCode.replaceAll("0+$", "");
			Integer length = partDepCode.length();
			Integer defaultLength = depCode.length();
			if("".equals(partDepCode) || length ==0){
				partDepCode = "";
			}else{
				 if(length%2 == 0){
					 partDepCode = depCode.substring(0, length);
				 }else{
					 if(defaultLength > length+1){
						 partDepCode = depCode.substring(0, length+1);
					 }else{
						 partDepCode = depCode.substring(0, length);
						 partDepCode = partDepCode+"0";
					 }
				 }
			}
		} catch (NullPointerException e) {
			partDepCode = depCode;
		}
		/*if("".equals(partDepCode)){
			partDepCode = defaultValue;
		}*/
		return partDepCode;
	}

	public static String getRandomCharacter(int length) {
		String val = "";

		Random random = new Random();
		for (int i = 0; i < length; i++) {
			int choice = 97;
			val += (char) (choice + random.nextInt(26));
		}
		return val;
	}

	public static String getToken() {
		String val = "";

		for (int i = 0; i < 5; i++) {
			if ("".equals(val)) {
				val += getRandomCharacter(6);
			} else {
				val += "-" + getRandomCharacter(6);
			}
		}
		return val;
	}

	public static String getSerialNum(long guid){
		StringBuffer serialNum = new StringBuffer();
		Date currentTime = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = format.format(currentTime);
		serialNum.append(dateString).append("_").append(guid).append("_");
		Random r = new Random();
		for(int i=0;i<4;i++){
			serialNum.append(r.nextInt(10));
		}
		return serialNum.toString();
	}
	/**
	 * 将以英文逗号隔开的参数字符串，转成BASE64编码的字符串，其中的逗号不进行BASE64编码。
	 * @param str
	 * @return
	 */
	public static String StringToBase64(String str){
		StringBuffer strBuffer=new StringBuffer();
		BASE64Encoder base64=new BASE64Encoder();
		String[] params=str.split(",");
		for (int i = 0; i < params.length; i++)
		{
			strBuffer.append(base64.encode(params[i].getBytes()).replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "")).append(",");
		}
		strBuffer.deleteCharAt(strBuffer.lastIndexOf(","));
		return strBuffer.toString();
	}
}
