package com.montnets.emp.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * MD5加密
 * @author Administrator
 *
 */
public class MD5 {
	
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			//System.exit(-1);
			EmpExecutionContext.error(e,"MD5加密异常！");
		} catch (UnsupportedEncodingException e) {
			EmpExecutionContext.error(e,"MD5加密异常！");
		}

		byte[] byteArray = messageDigest==null?null:messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; byteArray!=null && i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}
	public static void main(String[] args){
		//EmpExecutionContext.debug(MD5.getMD5Str("0000"));
	}
}
