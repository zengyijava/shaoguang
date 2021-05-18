/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2016-10-20 下午07:40:32
 */
package com.montnets.emp.common.biz;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.beanutils.DynaBean;
import org.apache.poi.ss.formula.functions.T;

import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * @功能概要：
 * @项目名称： emp
 * @初创作者： chentingsheng <cts314@163.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-10-20 下午07:40:32
 *        <p>
 *        修改记录1：
 *        </p>
 * 
 *        <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */

public class ParamsEncryptOrDecrypt implements java.io.Serializable
{
	/**
	 * @description  
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-26 下午06:08:51
	 */
	private static final long	serialVersionUID	= 1L;

	private byte[] key = new byte[8];
	
	public ParamsEncryptOrDecrypt()
	{
		setKey();
	}

	/**
	 * 生成密钥，32位的随机字母和数字加当前时间毫秒数
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-27 上午10:17:43
	 */
	private void setKey()
	{
		String keyStr = "";
		try
		{
			//生成随机数的字母和数字
			String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
			Random random = new Random();
			StringBuffer sb = new StringBuffer();
			int number = 0;
			for (int i = 0; i < 32; i++)
			{
				number = random.nextInt(base.length());
				sb.append(base.charAt(number));
			}
			//获取随机数+当前时间这毫秒数
			keyStr = sb.toString()+System.currentTimeMillis();
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
			return hexStr;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "请求参数加密失败！params:" + params + "，key:" + key);
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
	
	/**
     * 批量对动态BEAN集合对象指定加密字段和赋值 
     * @description    
     * @param <T>
     * @param entityList 对象列表
     * @param encryptValue  要加密的字段，全部小写字母
     * @param keyId    加密后赋值的对象，全部小写字母
     * @return     true:成功;false:失败  			 
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-10-28 下午04:05:37
     */
    public <T>boolean batchEncryptByDynaBeanToMap(List<DynaBean> entityList, String encryptValue, Map<String, String> keyIdMap)
    {
    	try
		{
    		//对象列表为空，返回
			if(entityList == null || entityList.size() < 1)
			{
				EmpExecutionContext.error("批量动态BEAN参数加密，对象列表为空。");
				return false;
			}
			String vaule = "";
			//遍历列表
			for(int i=0; i<entityList.size(); i++)
			{
				vaule = entityList.get(i).get(encryptValue).toString();
				keyIdMap.put(vaule, this.encrypt(vaule));
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "批量动态BEAN参数加密，对象指定加密字段和赋值失败。");
			return false;
		}
		
    }
   
	/**
     * 批理集合对象指定加密字段和赋值 
     * @description    
     * @param <T>
     * @param entityList 对象列表
     * @param encryptValue  要加密的字段
     * @param keyIdMap    加密后赋值的集合对象
     * @return     true:成功;false:失败  			 
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-10-28 下午04:05:37
     */
    public <T>boolean batchEncryptToMap(List<T> entityList, String encryptValue, Map<Long, String> keyIdMap)
    {
    	try
		{
    		//对象列表为空，返回
			if(entityList == null || entityList.size() < 1)
			{
				EmpExecutionContext.error("批量参数加密，对象列表为空。");
				return false;
			}
			Object obj;
			//遍历列表
			for(int i=0; i<entityList.size(); i++)
			{
				//获取对象
				obj = entityList.get(i);
				this.setValueToMap(obj, encryptValue, keyIdMap);
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "批量参数加密，对象指定加密字段和赋值失败。");
			return false;
		}
		
    }
    
	/**
     * 批理集合对象指定加密字段和赋值 
     * @description    
     * @param <T>
     * @param entityList 对象列表
     * @param encryptValue  要加密的字段
     * @param keyIdMap    加密后赋值的集合对象
     * @return     true:成功;false:失败  			 
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-10-28 下午04:05:37
     */
    public <T>boolean batchEncryptToMapKeyStr(List<T> entityList, String encryptValue, Map<String, String> keyIdMap)
    {
    	try
		{
    		//对象列表为空，返回
			if(entityList == null || entityList.size() < 1)
			{
				EmpExecutionContext.error("批量参数加密，对象列表为空。");
				return false;
			}
			Object obj;
			//遍历列表
			for(int i=0; i<entityList.size(); i++)
			{
				//获取对象
				obj = entityList.get(i);
				this.setValueToMapKeyStr(obj, encryptValue, keyIdMap);
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "批量参数加密，对象指定加密字段和赋值失败。");
			return false;
		}
		
    }
    
	/**
     * 批理集合对象指定加密字段和赋值，只支持VO
     * @description    
     * @param <T>
     * @param entityList 对象列表
     * @param encryptValue  要加密的字段
     * @param keyId    加密后赋值的对象
     * @return     true:成功;false:失败  			 
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-10-28 下午04:05:37
     */
    public <T>boolean batchEncrypt(List<T> entityList, String encryptValue, String keyId)
    {
    	try
		{
    		//对象列表为空，返回
			if(entityList == null || entityList.size() < 1)
			{
				EmpExecutionContext.error("批量参数加密，对象列表为空。");
				return false;
			}
			Object obj;
			//遍历列表
			for(int i=0; i<entityList.size(); i++)
			{
				//获取对象
				obj = entityList.get(i);
				//将指定的字段加密后赋给别一个字段
				setValue(obj, encryptValue, keyId);
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "批量参数加密，对象指定加密字段和赋值失败。");
			return false;
		}
		
    }
    
    /**
	 * 获取指定字段加密和重新赋值给另一个字段
	 * @description    
	 * @param obj
     * @param encryptValue  要加密的字段
     * @param keyId    加密后赋值的对象  			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-28 下午04:07:00
	 */
    public void setValue(Object obj, String encryptValue, String keyId)
	{
		try
		{
			Class<T> cls = (Class<T>) obj.getClass();
			//获取加密字段的值
			Method getMethod = cls.getMethod("get" + encryptValue);
			String value = String.valueOf(getMethod.invoke(obj));
			//将加密后的值账于指定的字段
			Method setMethod = cls.getMethod("set"+ keyId, String.class);
			setMethod.invoke(obj, this.encrypt(value));
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "参数加密，获取指定字段加密和重新赋值给另一个字段失败");
		}
	}
    
    /**
	 * 获取指定字段加密和重新赋值给另一个字段
	 * @description    
	 * @param obj
     * @param encryptValue  要加密的字段
     * @param keyIdMap    加密集合			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-28 下午04:07:00
	 */
    public void setValueToMap(Object obj, String encryptValue, Map<Long, String> keyIdMap)
	{
		try
		{
			Class<T> cls = (Class<T>) obj.getClass();
			//获取加密字段的值
			Method getMethod = cls.getMethod("get" + encryptValue);
			String value = String.valueOf(getMethod.invoke(obj));
			//将加密后的值账于指定的字段
			keyIdMap.put(Long.parseLong(value), this.encrypt(value));
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "参数加密，获取指定加密字段和重新赋值字段设置加密集合中失败.");
		}
	}
    
    /**
	 * 获取指定字段加密和重新赋值给另一个字段
	 * @description    
	 * @param obj
     * @param encryptValue  要加密的字段
     * @param keyIdMap    加密集合			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-28 下午04:07:00
	 */
    public void setValueToMapKeyStr(Object obj, String encryptValue, Map<String, String> keyIdMap)
	{
		try
		{
			Class<T> cls = (Class<T>) obj.getClass();
			//获取加密字段的值
			Method getMethod = cls.getMethod("get" + encryptValue);
			String value = String.valueOf(getMethod.invoke(obj));
			//将加密后的值账于指定的字段
			keyIdMap.put(value, this.encrypt(value));
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "参数加密，获取指定加密字段和重新赋值字段设置加密集合中失败.");
		}
	}
}
