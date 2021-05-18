package com.montnets.emp.email;

import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.montnets.emp.common.context.EmpExecutionContext;

public class Aes {

	private static final String KEY_ALGORITHM = "AES";

	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
	private static final String SECRETKEY = "AESKEYFFFFFFFFFF";

	public static byte[] initSecretKey() {

		// 返回生成指定算法的秘密密钥的 KeyGenerator 对象

		KeyGenerator kg = null;

		try {

			kg = KeyGenerator.getInstance(KEY_ALGORITHM);

		} catch (NoSuchAlgorithmException e) {

			EmpExecutionContext.error(e, "生成指定算法的秘密对象异常");

			return new byte[0];

		}

		// 初始化此密钥生成器，使其具有确定的密钥大小

		// AES 要求密钥长度为 128

		kg.init(128);

		// 生成一个密钥

		SecretKey secretKey = kg.generateKey();

		return secretKey.getEncoded();

	}

	private static Key toKey(byte[] key) {

		// 生成密钥

		return new SecretKeySpec(key, KEY_ALGORITHM);

	}

	public static byte[] encrypt(byte[] data, Key key) throws Exception {

		return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);

	}

	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {

		return encrypt(data, key, DEFAULT_CIPHER_ALGORITHM);

	}

	public static byte[] encrypt(byte[] data, byte[] key, String cipherAlgorithm)
			throws Exception {

		// 还原密钥

		Key k = toKey(key);

		return encrypt(data, k, cipherAlgorithm);

	}

	public static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm)
			throws Exception {

		// 实例化

		Cipher cipher = Cipher.getInstance(cipherAlgorithm);

		// 使用密钥初始化，设置为加密模式

		cipher.init(Cipher.ENCRYPT_MODE, key);

		// 执行操作

		return cipher.doFinal(data);

	}

	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {

		return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);

	}

	public static byte[] decrypt(byte[] data, Key key) throws Exception {

		return decrypt(data, key, DEFAULT_CIPHER_ALGORITHM);

	}

	public static byte[] decrypt(byte[] data, byte[] key, String cipherAlgorithm)
			throws Exception {

		// 还原密钥

		Key k = toKey(key);

		return decrypt(data, k, cipherAlgorithm);

	}

	public static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm)
			throws Exception {

		// 实例化

		Cipher cipher = Cipher.getInstance(cipherAlgorithm);

		// 使用密钥初始化，设置为解密模式

		cipher.init(Cipher.DECRYPT_MODE, key);

		// 执行操作

		return cipher.doFinal(data);

	}
	
	/**
	 * 加密
	 */
	public static String enString(String password){
		
		//获取密钥
		byte[] key = Aes.SECRETKEY.getBytes();
		//加密
		String hexStr="";
		try {
			byte[] encryptData = encrypt(password.getBytes(), key);
			hexStr = parseByte2HexStr(encryptData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			EmpExecutionContext.error(e, "邮箱密码加密异常");
		}
		
		return hexStr;
	}
	
	/**
	 * 解密
	 */
	public static String deString(String hexStr){
		
		//获取密钥
		byte[] key = Aes.SECRETKEY.getBytes();
		byte[] decryptData;
		try {
			byte[] result = parseHexStr2Byte(hexStr);
			decryptData = decrypt(result, key);
			return new String(decryptData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			EmpExecutionContext.error(e, "邮箱密码解密密异常");
			return "";
		}
	}
	
	/**
	 * 将二进制数组转换成16进制字符串
	 * @param buf
	 * @return
	 */
	private static String parseByte2HexStr(byte buf[]){
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

	/**
	 * 将16进制字符串转化成2进制数组
	 * @param hexStr
	 * @return
	 */
	private static byte[] parseHexStr2Byte(String hexStr) { 
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
}
