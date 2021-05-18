package com.montnets.emp.rms.wbs.util.impl;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.wbs.util.IEncodeAndDecode;

/**
 * 此类主要完成对相关文件的加密和解密
 * 加密方法；
 *（1）首先对要加密的字符串MD5加密；
 *（2）用DES的ECB模式加密（步骤1的密文+要加密的字符串）；
 *	（a）获得加密的Key；
 *	（b）根据key进行DES.ECB模式的加密；
 *（3）将加密的密文进行Base64编码；
 *解密算法：
 *（1）将得到的密文进行Base64的解密；
 *（2）用DES.ECB模式将步骤（1）的密文解密；
 *（3）将（2）的字符串subString();
 * @author asus
 *
 */
public  class Base64MD5Coding implements IEncodeAndDecode {
	/**
	 * 加密
	 * @param 需要加密的密文
	 */
	public String encoding(String code,String key) {
		if(null == code){
			return null;
		}
		String text = null;
		//MD5生成密文 
		try {
			String ciphertext = MD5(code);
			String md5Text = ciphertext+ code;
			//DES加密MD5字段+明文
			byte[] key_ = getKey(key);
			SecretKey desKey = getDesKey(key_);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE,desKey);
			byte[] desText = cipher.doFinal(md5Text.getBytes("utf-8"));
			//BASE64编码
			BASE64Encoder b64 = new BASE64Encoder();
			text = b64.encode(desText);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"base64加密失败:"+e.getMessage());
			return null;
		}
		return text;
	}
	/**
	 * 解密需要解密的密文
	 * @param code 密文
	 * @key 密钥
	 */
	public String decode(String code,String key) {
		if(null == code || null == key){
			return null;
		}
		String plainText = null;
		try{
			//BASE64 DECODE
			BASE64Decoder b64 = new BASE64Decoder();
			byte[] inputStream = b64.decodeBuffer(code);
			//DESj解密
			byte[] key_ = getKey(key);
			SecretKey desKey = getDesKey(key_);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE,desKey);
			byte[] desText = cipher.doFinal(inputStream);
			plainText = new String(desText,"UTF-8");
			//因为MD5是固定的32byte的长度，这里截取32byte之后的内容即为明文内容
			plainText = plainText.substring(32, plainText.length());
		}catch(Exception e){
			EmpExecutionContext.error(e,"base64解密失败:"+e.getMessage());
			return null;
			
		}
		return plainText;
	}
    
	 /**
	  * 密钥是一个长度16、由16进制字符组成的字符串，如：1234567890ABCDEF
	  * 使用时，相临的两位理解为一个16进制数的明文，然后转换为实际使用的8位密钥。
	  * @param key 加密或解密密钥,如：1234567890ABCDEF
	  * @return 返回8bytes类型数组一共是64位
     * */
	public byte[] getKey(String key){
		byte[] key_ = new byte[8];
		//没两位代表一个16进制明文，转换成一个byte
		for(int i = 0;i < 8; i++){
			int t =  Integer.parseInt(key.substring(i*2,i*2+2),16);
			if(t > Byte.MAX_VALUE){
				t = t- 256;
			}
			key_[i] = (byte)t;
		}
		return key_;
	}
	/**
	 * 得到DES算法的64位密钥
	 * @param key 8个byte数组
	 * @return 对应的DES密钥
	 */
	private SecretKey getDesKey(byte[] key){
		DESKeySpec dks;
		SecretKeyFactory keyFactory;
		SecretKey secretKey = null;
		try {
			dks = new DESKeySpec(key);
			keyFactory = SecretKeyFactory.getInstance("DES");
			secretKey = keyFactory.generateSecret(dks);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取des算法密钥失败:"+e.getMessage());
		}   
	  
        return secretKey;
	}
	
	private String MD5(String plainText){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			String result = buf.toString();
			return result;
		} catch (NoSuchAlgorithmException e) {
			EmpExecutionContext.error(e,"md5加密失败:"+e.getMessage());
			return "";
		} 
	}
	
	public static void main(String[] args)
	{
		String code="98f9dblBh+NGD45YyKEY+VgAFqDz77PBLHt9RKDDLw8TrH/2zQ5lEhF1ieuHR035E1o56IeY2FLt4xWonf5U0rbtu+efGhoKgFaoDoNFQkCUHpvH2HIKmS9LUki+/p4FIAfmLK8Z8dAoPwjDTymc7Y12MQfl4Y7OhAMeBWrLJ70ma7gXzmfIQGQ8GJivIOgkfuwGkghzljwMqph3aKphGMxrfdgD80qlBDwqe3yyayHsQ2WSTjDx5cPdV4pXm+Tc+/TsQHJUEW4=";
	}
}
