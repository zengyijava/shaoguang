package com.montnets.emp.common.biz;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.montnets.emp.common.context.EmpExecutionContext;

public class PwdEncryptOrDecrypt implements java.io.Serializable{

	//用于判断是否是新密码加密的标识
	private final String newPwFlag="YYY999";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private byte[] key = new byte[8];
	
	private static BufferedReader breader = null;
	
	public PwdEncryptOrDecrypt()
	{
		setKey();
	}

	/**
	 * 用于用户加密使用
	 * @param arg
	 * @throws IOException
	 */
	public static void main(String arg[]) throws IOException {		
		String flag="";
        try {
        	breader = new BufferedReader(new InputStreamReader(System.in,"GBK"));
		} catch (Exception e) {
        	EmpExecutionContext.error(e, "获取流失败");
		}
		System.out.print("请输入数字0(表示加密)或者1(表示解密)：");
		//IP地址
		flag = breader.readLine();
		if("0".equals(flag)){
		    System.out.print("请输入EMP数据库需要加密的密码：");
		   String psw = breader.readLine();
		    if("".equals(psw.trim())){
		    	System.out.print("您输入需要加密的密码为空");
		    }else{
		    	PwdEncryptOrDecrypt ed=new PwdEncryptOrDecrypt();
		    	System.out.print("EMP数据库加密之后的密码为："+ed.encrypt(psw));
		    }
		}else if("1".equals(flag)){
		    System.out.println("请输入EMP数据库需要解密的密码：");
			   String psw = breader.readLine();
			    if("".equals(psw.trim())){
			    	System.out.print("您输入解密的密码为空");
			    }else{
			    	PwdEncryptOrDecrypt ed=new PwdEncryptOrDecrypt();
			    	System.out.print("EMP数据库解密之后的密码为："+ed.decrypt(psw));
			    }
		}else{
			System.out.println("您输入的数字不正确！");
		}
	}

	/**
	 * 为了便于使用使用固定秘钥
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-27 上午10:17:43
	 */
	private void setKey()
	{
		String keyStr = "";
		try
		{
			
			//获取随机数+当前时间这毫秒数
			keyStr = "MONTENTSEMP100001";
			this.key = keyStr.getBytes();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "生成请求参数加密解密密钥异常。！keyStr:"+keyStr);
		}
	}
	
	/**
	 * 加密
	 * @description    
	 * @param params 需要加密的值
	 * @return      加密后的值,异常返回null 			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-29 下午02:52:44
	 */
	public String encrypt(String params)
	{
		try
		{
			if(params == null || params.trim().length() < 1)
			{
				EmpExecutionContext.error("请求参数加密，加密参数不合法，params:"+params);
				return null;
			}
			
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			//DES加密
			byte[] encryptbyte = cipher.doFinal(params.getBytes("UTF-8"));
			//转16进制字符器
			String hexStr = parseByte2HexStr(encryptbyte);
			//为了区别新旧密码，后面加了固定部分
			hexStr=hexStr+newPwFlag;
			return hexStr;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "请求参数加密失败！params:" + params + "，key:" + key);
			return null;
		}
	}
	
	/**
	 * 将二进制数组转换成16进制字符串
	 * @description    
	 * @param buf
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-29 下午02:53:56
	 */
	private static String parseByte2HexStr(byte buf[])
	{
		try
		{
			StringBuffer sb = new StringBuffer(); 
			for (int i = 0; i < buf.length; i++) { 
			String hex = Integer.toHexString(buf[i] & 0xFF); 
			if (hex.length() == 1) { 
			hex = '0' + hex; 
			} 
			sb.append(hex.toUpperCase()); 
			} 
			return sb.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "请求参数加密失败，将二进制数组转换成16进制字符串异常。buf:"+buf);
			return null;
		} 
	}
	
	/**
	 * 解密
	 * @description    
	 * @param params 需要解密的值
	 * @return    解密后的值，异常返回null   			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-29 下午02:53:17
	 */
	public String decrypt(String params)
	{
		try
		{
			if(params == null || params.trim().length() < 1)
			{
				EmpExecutionContext.error("请求参数解密，解密参数不合法，params:"+params);
				return null;
			}
			//16进制字符串转字节
			params=params.substring(0,params.length()-6);
			byte[] result = parseHexStr2Byte(params);
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] retByte = cipher.doFinal(result);
			return new String(retByte, "UTF-8");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "请求参数解密失败！params:" + params + "，key:" + key);
			return null;
		}
	}
	/**
	 * 将16进制字符串转化成2进制数组
	 * @description    
	 * @param hexStr
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-29 下午02:54:06
	 */
	private static byte[] parseHexStr2Byte(String hexStr) 
	{ 
		try
		{
			if (hexStr.length() < 1) 
			return null; 
			byte[] result = new byte[hexStr.length() / 2]; 
			for (int i = 0; i < hexStr.length() / 2; i++) { 
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16); 
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 
			16); 
			result[i] = (byte) (high * 16 + low); 
			} 
			return result;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "请求参数解密失败，将16进制字符串转化成2进制数组异常。hexStr:"+hexStr);
			return null;
		} 
	}
	
	

}
