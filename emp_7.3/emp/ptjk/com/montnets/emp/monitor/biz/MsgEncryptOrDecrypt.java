/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2016-4-22 下午03:09:25
 */
package com.montnets.emp.monitor.biz;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * @功能概要：
 * @项目名称： emp_ftp
 * @初创作者： chentingsheng <cts314@163.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-4-22 下午03:09:25
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */

public class MsgEncryptOrDecrypt
{
	/**
	 * 密钥是一个长度16、由16进制字符组成的字符串，如：1234567890ABCDEF
	 * 使用时，相临的两位理解为一个16进制数的明文，然后转换为实际使用的8位密钥。
	 * 
	 * @param key
	 *        ：加密或解密密钥
	 * @return：返回byte类型数组
	 */
	private static byte[] getKey(String key)
	{
		try
		{
			byte[] mArray = new byte[8];
			for (int i = 0; i < 8; i++)
			{
				int t = Integer.parseInt(key.substring(i * 2, i * 2 + 2), 16);
				mArray[i] = (byte) ((t > 127) ? (t - 256) : t);
			}
			return mArray;
		}
		catch (NumberFormatException e)
		{
			EmpExecutionContext.error(e, "获取加密密钥失败！key:"+key);
			return null;
		}
	}

	/**
	 * MD5
	 * 
	 * @param message
	 *        ：明文
	 * @return String 密文
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5(String message) throws NoSuchAlgorithmException
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("md5");
			md.update(message.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer sb = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++)
			{
				i = b[offset];
				if(i < 0) i += 256;
				if(i < 16) sb.append("0");
				sb.append(Integer.toHexString(i));
			}
			return sb.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "消息MD5加密失败！message:" + message);
			return null;
		}
	}

	/**
	 * 解密
	 * 
	 * @param message
	 *        :密文
	 * @param key
	 *        ：密钥,密钥是一个长度16、由16进制字符组成的字符串
	 * @return String ：明文
	 * @throws Exception
	 */
	public static String decryptString(String message, String key) throws Exception
	{
		try
		{
			// base64 + des 解密.net 加密传来串
			byte[] k = getKey(key);
			if(k == null)
			{
				EmpExecutionContext.error("消息解密失败，加密密钥异常！key:"+key);
				return null;
			}
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] bytesrc = base64Decoder.decodeBuffer(message);
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(k);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] retByte = cipher.doFinal(bytesrc);
			String r = new String(retByte);
			return r.substring(32);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "消息解密失败！message:" + message + "，key:" + key);
			return null;
		}
	}

	/**
	 * 解密
	 * 
	 * @param message
	 *        :密文
	 * @param key
	 *        ：密钥,密钥是一个长度16、由16进制字符组成的字符串
	 * @return String ：明文
	 * @throws Exception
	 */
	public static String decryptStringUTF8(String message, String key) throws Exception
	{
		try
		{
			// base64 + des 解密.net 加密传来串
			byte[] k = getKey(key);
			if(k == null)
			{
				EmpExecutionContext.error("消息UTF-8编码解密失败，加密密钥异常！key:"+key);
				return null;
			}
			BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] bytesrc = base64Decoder.decodeBuffer(message);
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(k);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] retByte = cipher.doFinal(bytesrc);
			String r = new String(retByte, "UTF-8");
			return r.substring(32);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "消息UTF-8编码解密失败！message:" + message + "，key:" + key);
			return null;
		}
	}
	
	/**
	 * 加密
	 * 
	 * @param message
	 *        :明文
	 * @param key
	 *        :密钥,密钥是一个长度16、由16进制字符组成的字符串
	 * @return String 密文
	 * @throws Exception
	 */
	public static String encryptString(String message, String key) throws Exception
	{
		try
		{
			// 产生与.net 对应的加密des + base64 加密串
			byte[] k = getKey(key);
			if(k == null)
			{
				EmpExecutionContext.error("消息加密失败，加密密钥异常！message:" + message + "，key:" + key);
				return null;
			}
			if(message == null ){
				return null;
			}
			String md5s = md5(message);
//			String s = md5(message) + message;
			if(md5s == null)
			{
				EmpExecutionContext.error("消息加密失败，MD5加密异常！message:" + message + "，key:" + key);
				return null;
			}
			String s = md5s + message;
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(k);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptbyte = cipher.doFinal(s.getBytes());
			BASE64Encoder base64Encoder = new BASE64Encoder();
			return base64Encoder.encode(encryptbyte);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "消息加密失败！message:" + message + "，key:" + key);
			return null;
		}
	}

}
