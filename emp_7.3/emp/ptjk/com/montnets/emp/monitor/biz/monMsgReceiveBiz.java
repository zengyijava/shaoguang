/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-11-21 下午03:30:48
 */
package com.montnets.emp.monitor.biz;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.monitor.constant.MonitorStaticValue;

/**
 * @description
 * @project emp_std_189
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-11-21 下午03:30:48
 */

public class monMsgReceiveBiz
{
	/**
	 * 处理消息
	 * 
	 * @description
	 * @param monitorMsg
	 * @return 0:成功；-1：消息流转字符串异常；-2：消息内容为空；-3：解密异常；-4：消息入列失败；-99：未知异常
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-11-21 下午04:16:23
	 */
	public int processMonitorMsg(InputStream monitorMsgStream)
	{
		//解密前的消息内容
		String monotorMsg = "";
		// 解密后的消息内容
		String msg = "";
		try
		{
			//消息流转字符串
			monotorMsg = convertStreamToString(monitorMsgStream);
			if(monotorMsg == null)
			{
				EmpExecutionContext.error("监控消息流转字符串异常！");
				return -1;
			}
			if("".equals(monotorMsg))
			{
				EmpExecutionContext.error("监控消息内容为空！");
				return -2;
			}
			if(monotorMsg.indexOf("5800monmsg") > -1)
			{
				monotorMsg = monotorMsg.substring(monotorMsg.indexOf("5800monmsg") + 11);
				msg = MsgEncryptOrDecrypt.decryptStringUTF8(monotorMsg, MonitorStaticValue.getFileServerMonMsgKey());
			}
			else
			{
				// 解密
				msg = Decryption(monotorMsg);
			}
			if(msg == null)
			{
				EmpExecutionContext.error("监控消息解密异常！消息内容:" + monotorMsg);
				return -3;
			}

			// 消息入列
			try
			{
				//队列大于9000,移除最老的数据
				if(MonitorStaticValue.getMonitorMsgQueue().size() > 9000)
				{
					MonitorStaticValue.getMonitorMsgQueue().poll();
				}
				MonitorStaticValue.getMonitorMsgQueue().put(msg);
			}
			catch (InterruptedException e)
			{
				EmpExecutionContext.error(e, "监控消息入列异常，消息内容：" + msg);
				Thread.currentThread().interrupt();
				return -4;
			}

			// 启动监控线程
			monitorThreadTask();

			// 处理成功
			return 0;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "监控消息处理失败！");
			return -99;
		}
	}

	/**
	 * 消息流转字符串
	 * @description    
	 * @param in
	 * @return
	 * @throws IOException       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-11-25 下午12:20:12
	 */
	public String convertStreamToString(InputStream in) throws IOException
	{
		try
		{
			StringBuffer out = new StringBuffer();
			byte[] b = new byte[1024];
			for (int n; (n = in.read(b)) != -1;)
			{
				out.append(new String(b, 0, n));
			}
			return out.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "监控消息流转字符串异常！");
			return null;
		}
	}
	
	/**
	 * 启动监控线程任务
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-11-21 下午03:50:15
	 */
	public void monitorThreadTask()
	{
		try
		{
			// 如果数据解析线程未启动
			if(!MonitorStaticValue.isMonDataResolve())
			{
				new Thread(new MonDataResolveBiz()).start();
				MonitorStaticValue.setMonDataResolve(true);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "启动监控线程异常！通道账号费用信息:" + MonitorStaticValue.isGateAccountFee() + "数据解析线程:" + MonitorStaticValue.isGateAccountFee());
		}
	}

	/**
	 * 消息解密
	 * 
	 * @description
	 * @param monitorMsg
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-11-21 下午04:07:55
	 */
	private String Decryption(String monitorMsg)
	{
		// BASE64解密后内容
		byte[] msgByte = null;
		// DES解密后的消息内容
		String msg = "";
		
		try
		{
			//BASE64解密
			msgByte = base64Decryption(monitorMsg);
			if(msgByte == null)
			{
				EmpExecutionContext.error("监控消息BASE64解密异常！");
				return null;
			}
			
			//获取私钥
			byte[]privateKey = getPrivatekey(msgByte);
			if(privateKey == null)
			{
				EmpExecutionContext.error("获取私钥异常！");
				return null;
			}
			
			//监控消息内容
			byte[] monitorMsgByte = getMonitorMsgByte(msgByte);
			
			// DES解密
			msg = desDecryption(monitorMsgByte, privateKey);
			if(msg == null)
			{
				EmpExecutionContext.error("监控消息DES解密异常！");
				return null;
			}
			return msg;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "消息解密异常！");
			return null;
		}
	}

	/**
	 * BASE64解密
	 * 
	 * @description
	 * @param msg
	 *        返回解密后的字节型密码
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-11-21 下午04:38:44
	 */
	private byte[] base64Decryption(String msg)
	{
		try
		{
			BASE64Decoder base64De = new BASE64Decoder();
			return base64De.decodeBuffer(msg);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "监控消息BASE64解密异常！");
			return null;
		}
	}

	/**
	 * 获取私钥
	 * @description    
	 * @param msgByte
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-11-25 上午08:49:57
	 */
	private byte[] getPrivatekey(byte[] msgByte)
	{
		try
		{
			//私钥
			byte[] privateKey = new byte[8];
			//转为字符串
			String msgStr = new String(msgByte, "UTF-8");
			//前8位为公钥密码
			String publicKey = msgStr.substring(0, 8);
			//MD5摘要
			byte[] key = getMD5(publicKey);
			//获取前8位字节
			for(int i=0; i<8;i++)
			{
				privateKey[i] = key[i];
			}
			return privateKey;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取私钥异常！");
			return null;
		}
	}
	
	/**
	 * 监控消息内容，取BASE64解密后8位以后的数据
	 * @description    
	 * @param msgByte
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-11-25 上午09:00:10
	 */
	private byte[] getMonitorMsgByte(byte[] msgByte)
	{
		try
		{
			int len = msgByte.length;
			byte[] monitorMsgByte = new byte[len-8];
			for(int i=8;i<len;i++)
			{
				
				monitorMsgByte[i-8] = msgByte[i];
			}
			return monitorMsgByte;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取监控消息内容异常！");
			return null;
		}
	}
	
	/**
	 * 消息EDS解密
	 * 
	 * @description
	 * @param msgByte
	 * @param key
	 * @return
	 * @throws Exception
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-11-21 下午04:40:18
	 */
	public String desDecryption(byte[] msgByte, byte[] key)
	{
		try
		{
			// 创建一个DESKeySpec对象
			DESKeySpec desKey = new DESKeySpec(key);
			// 创建一个密匙工厂
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// 将DESKeySpec对象转换成SecretKey对象
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, securekey);
			// 真正开始解密操作
			return new String(cipher.doFinal(msgByte));
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "监控消息DES解密异常！");
			return null;
		}
	}
	
	/**
	 * MD5摘要
	 * @description    
	 * @param password
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-11-25 下午12:13:14
	 */
	public byte[] getMD5(String password)
	{
		MessageDigest md;
		byte b[] = null;
		try
		{
			String message = password + "(!*montnets@#)" + password;
			md = MessageDigest.getInstance("MD5");
			md.update(message.getBytes());
			b = md.digest();
		}
		catch (NoSuchAlgorithmException e)
		{
			EmpExecutionContext.error(e, "监控消息私钥MD5摘要异常！");
		}
		return b;
	}
}
