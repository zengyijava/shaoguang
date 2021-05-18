package com.montnets.emp.qyll.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.montnets.emp.common.context.EmpExecutionContext;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncryptOrDecrypt {
	
	// 算法名称    
    public static final String KEY_ALGORITHM = "DES";    
    // 算法名称/加密模式/填充方式
    public static final String CIPHER_MODE = "DES/CBC/PKCS5Padding";
	
    /**
	 * 加密
	 * 对消息的加密算法：Base64(DES(MD5(消息体)+消息体))
	 * @param message 消息明文
	 * @param key 密钥，系统生成的密钥为24位的Base64字符，由两部分构成：12位Key(密钥密码)＋12位的IV(初始化向量)。
	 * @return String 消息密文
	 * @throws Exception
	 */
	public static String encryptString(String message, String key) throws Exception {
		
		if(message == null || message.length() == 0){
			throw new IllegalArgumentException("传入message参数不正确。");
		}
		else if(key == null || key.length() == 0 || key.length() != 24){
			throw new IllegalArgumentException("传入key参数不正确。");
		}
		
		//获取key前12位字节数组
		byte[] keyArray = getKey(key);
		//MD5加密+消息明文
		String s = md5(message) + message;
		
		Cipher cipher = Cipher.getInstance(CIPHER_MODE);
		DESKeySpec desKeySpec = new DESKeySpec(keyArray);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		
		//获取key后12位字节数组
		byte[] ivArray = getIV(key);
		IvParameterSpec iv = new IvParameterSpec(ivArray);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		
		//DES加密后的字节数组
		byte[] encryptbyte = cipher.doFinal(s.getBytes("GBK"));
		BASE64Encoder base64Encoder = new BASE64Encoder();
		//转为Base64返回
		return base64Encoder.encode(encryptbyte);
	}

	/**
	 * 解密
	 * 对消息的加密算法：Base64(DES(MD5(消息体)+消息体))
	 * @param message 消息密文
	 * @param key 密钥，系统生成的密钥为24位的Base64字符，由两部分构成：12位Key(密钥密码)＋12位的IV(初始化向量)。
	 * @return String 消息明文
	 * @throws Exception
	 */
	public static String decryptString(String message, String key) throws Exception {
		
		if(message == null || message.length() == 0){
			throw new IllegalArgumentException("传入message参数不正确。");
		}
		else if(key == null || key.length() == 0 || key.length() != 24){
			throw new IllegalArgumentException("传入key参数不正确。");
		}
		
		//获取key前12位字节数组
		byte[] keyArray = getKey(key);
		
		BASE64Decoder base64Decoder = new BASE64Decoder();
		//转为DES加密的字节数组
		byte[] bytesrc = base64Decoder.decodeBuffer(message);
		Cipher cipher = Cipher.getInstance(CIPHER_MODE);
		DESKeySpec desKeySpec = new DESKeySpec(keyArray);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		
		byte[] ivArray = getIV(key);
		IvParameterSpec iv = new IvParameterSpec(ivArray);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		
		//获取MD5加密+消息明文的字节数组
		byte[] retByte = cipher.doFinal(bytesrc);
		String r = new String(retByte,"GBK");
		EmpExecutionContext.info("========获取解析的信息：r.substring(32)"+r.substring(32));
		//截取消息明文并返回
		return r.substring(32);
	}
    
	/**
	 * 获取key中的key(密钥密码)
	 * @param key 系统生成的密钥为24位的Base64字符，由两部分构成：12位Key(密钥密码)＋12位的IV(初始化向量)。
	 * @return 返回8位无符号字节数组
	 * @throws IOException
	 */
	private static byte[] getKey(String key) throws IOException{
		//截取key的后12位初始化向量字符串
		String keyStr = key.substring(0, 12);
		BASE64Decoder base64Decoder = new BASE64Decoder();
		byte[] keyArray = base64Decoder.decodeBuffer(keyStr);
		return keyArray;
	}
	
	/**
	 * 获取key中的IV(初始化向量)
	 * @param key 系统生成的密钥为24位的Base64字符，由两部分构成：12位Key(密钥密码)＋12位的IV(初始化向量)。
	 * @return 返回8位无符号字节数组
	 * @throws IOException
	 */
	private static byte[] getIV(String key) throws IOException{
		//截取key的后12位初始化向量字符串
		String ivStr = key.substring(12);
		BASE64Decoder base64Decoder = new BASE64Decoder();
		byte[] ivArray = base64Decoder.decodeBuffer(ivStr);
		return ivArray;
	}

	/**
	 * MD5加密
	 * @param message 明文
	 * @return String 密文
	 * @throws NoSuchAlgorithmException
	 */
	private static String md5(String message) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("md5");
		md.update(message.getBytes());
		byte b[] = md.digest();
		int i;
		StringBuffer sb = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				sb.append("0");
			sb.append(Integer.toHexString(i));
		}
		return sb.toString();

	}
	
	public static String base64Decoder(String msg) throws IOException{
		BASE64Decoder decoder = new BASE64Decoder();
		return new String(decoder.decodeBuffer(msg),"UTF-8");
		
	}
	
	public static void main(String[] args) throws IOException {
		BASE64Decoder decoder = new BASE64Decoder();
		BASE64Encoder en = new BASE64Encoder();
		String str = en.encodeBuffer("中国,,,".getBytes("UTF-8"));
		System.out.println(str);
		System.out.print(new String(decoder.decodeBuffer(str),"UTF-8"));
	}
	
}
