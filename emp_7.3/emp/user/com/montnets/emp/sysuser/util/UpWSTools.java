package com.montnets.emp.sysuser.util;

import java.rmi.RemoteException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.xml.rpc.ServiceException;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 统一用户平台工具类
 * @author Administrator
 *
 */
public class UpWSTools {
	/**
	 * 此方法用于对消息体作Base64加密
	 */
	public static String toEncode(String msgContext) {
			try {
				return new String(encode(encrypt(msgContext.getBytes("UTF-8"), "11111111".getBytes())));
			} catch (Exception e) {
				EmpExecutionContext.error(e,"Base64加密失败！");
			}
		return "";
	}

	/**
	 * 此方法用于对消息体作Base64解密
	 */
	public static String toDecode(String msgContext) throws Exception {
		if(msgContext != null && !"".equals(msgContext)){
			try {
				return new String(decrypt(decode(msgContext), "11111111".getBytes()), "UTF-8");
			} catch (Exception e) {
				EmpExecutionContext.error(e,"对消息体进行加密 出现异常！");
			}
		}
		return "";
	}

	private static String encode(byte[] bstr) {
		return org.apache.axis.encoding.Base64.encode(bstr);
	}

	private static byte[] encrypt(byte[] data, byte[] key) {
		try {
			SecureRandom sr = new SecureRandom();
			DESKeySpec dks = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
			byte encryptedData[] = cipher.doFinal(data);
			return encryptedData;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"执行加密失败！");
		}
		return null;
	}

	private static byte[] decode(String str) throws Exception {
		byte[] bt = null;
		try {
			bt = org.apache.axis.encoding.Base64.decode(str);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"执行64位加密失败！");
			throw e;
		}
		return bt;
	}

	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		try {
			SecureRandom sr = new SecureRandom();
			DESKeySpec dks = new DESKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
			byte decryptedData[] = cipher.doFinal(data);
			return decryptedData;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"执行加密失败！");
			throw e;
		}
	}
	
	/**
	 *  调用WEBSERVICE
	 * @param xmlString
	 * @param address
	 * @return
	 */
	public static String SYNWS(String xmlString,String address)throws Exception{
		UpWSService imws = new UpWSServiceLocator(address);
		UpWS im = null;
		String xmlReulst = "";
		try {
			im = imws.getESAI();
		} catch (ServiceException e) {
			EmpExecutionContext.error(e,"获取UpWS对象出现异常！");
			xmlReulst = "";
		}
		try {
			xmlReulst = im==null?"":im.ESAI(xmlString);
		} catch (RemoteException e) {
			EmpExecutionContext.error(e,"获取调用WEBSERVICE接口出现异常！");
			xmlReulst = "";
		}
		return xmlReulst;
	}
	
	
	/** 判断是否非空 */
	public static boolean isNotNull(Object obj1) {
		if (null == obj1 || "".equals(obj1.toString())||"null".equals(obj1.toString())) {
			return false;
		} else {
			return true;
		}
	}
	
	
	/**
	 *   判断是否为NULL或者为空      则返回""   
	 * @param str	传入字符
	 * @return
	 */
	public static String isNotKong(String str) {
		try{
			if (null == str || "".equals(str.trim()) || "null".equals(str.trim()) || str.length() == 0) {
				return "";
			} else {
				return str;
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"判断是否为空为null出现异常！");
			str = "";
		}
		return str;
	}
	
	
	/**
	 *   处理返回回执报文 信息
	 * @param returnReq	回执报文对象
	 * @param isFlag	是否有流水号
	 * @return
	 */
	public String returnReqStr(ReturnReq returnReq){
		StringBuffer inputXMLStr = new StringBuffer();
		try{
			inputXMLStr.append("<?xml version='1.0' encoding='UTF-8'?>"); 
			inputXMLStr.append("<SYNRQ><BCode>").append(returnReq.getBcode()).append("</BCode>");
			inputXMLStr.append("<ApId>").append(returnReq.getApid()).append("</ApId>");
			inputXMLStr.append("<Cert>").append(returnReq.getCert()).append("</Cert>");
			inputXMLStr.append("<RESULTCODE>").append(returnReq.getResultCode()).append("</RESULTCODE>");
			inputXMLStr.append("<RESULTMSG>").append(returnReq.getResultMsg()).append("</RESULTMSG>");
			inputXMLStr.append("<Cnxt>");
			if(returnReq.isHaveSer()){
				StringBuffer str = new StringBuffer();
				str.append("<ReReq><BODY>");
				str.append("<STA>").append(returnReq.getSta()).append("</STA>");
				str.append("<ERR>").append(returnReq.getErr()).append("</ERR>");
				str.append("<SER>").append(returnReq.getSer()).append("</SER>");
				str.append("</BODY></ReReq>");
				inputXMLStr.append(UpWSTools.toEncode(str.toString()));
			}
			inputXMLStr.append("</Cnxt></SYNRQ>");
		}catch (Exception e) {
			inputXMLStr = new StringBuffer();
			EmpExecutionContext.error(e,"处理返回回执报文 信息出现异常！");
		}
		return inputXMLStr.toString();
	}
	
	
	/**
	 *   webservice向服务端发送请求报文
	 * @param requestStr	请求报文对象
	 * @return
	 */
	public String returnRequestStr(RequestStr requestStr){
		StringBuffer inputXMLStr = new StringBuffer();
		StringBuffer str = new StringBuffer();
		try{
			inputXMLStr.append("<?xml version='1.0' encoding='UTF-8'?>");
			inputXMLStr.append("<SYNRQ><BCode>").append(requestStr.getBcode()).append("</BCode>");
			inputXMLStr.append("<ApId>").append(requestStr.getApid()).append("</ApId>");
			inputXMLStr.append("<Cert>").append(requestStr.getCert()).append("</Cert><Cnxt>");
			
			str.append("<OptReq><BODY>");
			str.append("<TPE>").append(requestStr.getTpe()).append("</TPE>");
			str.append("</BODY></OptReq>");
			inputXMLStr.append(UpWSTools.toEncode(str.toString()));
			
			inputXMLStr.append("</Cnxt></SYNRQ>");
		}catch (Exception e) {
			//如果报错的返回空
			inputXMLStr = new StringBuffer();
			EmpExecutionContext.error(e,"webservice向服务端发送请求报文出现异常！");
		}
		return inputXMLStr.toString();
	}
	
	/**
	 *  处理返回对象的添值
	 * @param returnReq	返回对象
	 * @param returnMsg	处理操作的返回值
	 * @param serialNum	流水号、
	 * @return
	 */
	public boolean returnResponStr(ReturnReq returnReq,String returnMsg,String serialNum){
		boolean isFlag = false;
		try{
			if("success".equals(returnMsg)){
				returnReq.setResultCode("0");
				returnReq.setResultMsg("成功");
				returnReq.setSta("1");
				returnReq.setErr("成功");
				returnReq.setSer(serialNum);
				returnReq.setHaveSer(true);
			}else{
				returnReq.setSta("2");
				if("apidError".equals(returnMsg)){
					returnReq.setResultCode("101");
					returnReq.setResultMsg("应用编码（APID）不对");
					returnReq.setErr("应用编码（APID）不对");
					returnReq.setSer("");
					returnReq.setHaveSer(false);
				}else if("certError".equals(returnMsg)){
					returnReq.setResultCode("102");
					returnReq.setResultMsg("应用密钥（Cert）不对");
					returnReq.setErr("应用密钥（Cert）不对");
					returnReq.setSer("");
					returnReq.setHaveSer(false);
				}else{
					//是否有流水号、
					returnReq.setResultCode("103");
					if("noContent".equals(returnMsg)){
						returnReq.setResultMsg("报文格式没数据");
						returnReq.setErr("报文格式没数据");
					}else{
						returnReq.setResultMsg("报文格式不对");
						returnReq.setErr("报文格式不对");
					}
					if(UpWSTools.isNotNull(serialNum)){
						returnReq.setSer(serialNum);
						returnReq.setHaveSer(true);
					}else{
						returnReq.setSer("");
						returnReq.setHaveSer(false);
					}
				}
			}
			isFlag = true;
		}catch (Exception e) {
			EmpExecutionContext.error(e,"处理返回对象的添值出现异常！");
			isFlag = false;
		}
		return isFlag;
	}
	
	/**
	 *   判断是否配置文件中是否存值
	 * @param webserviceIp
	 * @param empCert
	 * @param empApId
	 * @return
	 */
	public boolean isExistSystemValue(){
		boolean isFlag = false;
		try{
			//服务URL
			String webserviceIp = SystemGlobals.getValue(StaticValue.UPWS_WEBSERVICE_IP);
			//密钥
			String empCert =  SystemGlobals.getValue(StaticValue.UPWS_WEBSERVICE_CERT);
			//系统标识
			String empApId =  SystemGlobals.getValue(StaticValue.UPWS_WEBSERVICE_APID);
			if(UpWSTools.isNotNull(webserviceIp) && UpWSTools.isNotNull(empCert) && UpWSTools.isNotNull(empApId)){
				isFlag = true;
			}
		}catch (Exception e) {
			isFlag =false;
			EmpExecutionContext.error(e,"判断是否配置文件中是否存值出现异常！");
		}
		return isFlag;
	}
	
	/**
	 *   判断报文是否正确
	 * @param empCert	EMP密钥
	 * @param empApId	EMP应用系统
	 * @param cert		报文密钥
	 * @param apid		报文应用系统
	 * @param resultCode	报文编码
	 * @return
	 */
	public String isRightValue(String empCert,String empApId,String cert,String apid,String resultCode,ReturnReq returnReq){
		String returnMsg = "1";
		try{
			//应用系统错误
			if(!empApId.equals(apid)){
				returnReq.setResultCode("101");
				returnReq.setResultMsg("应用编码（APID）不对");
				returnReq.setErr("应用编码（APID）不对");
				returnReq.setSer("");
				return "apidError";
			}
			//密钥错误
			if(!empCert.equals(cert)){
				returnReq.setResultCode("102");
				returnReq.setResultMsg("应用密钥（Cert）不对");
				returnReq.setErr("应用密钥（Cert）不对");
				returnReq.setSer("");
				return "certError";
			}
			//返回结果错误
			if(!"0".equals(resultCode)){
				returnReq.setResultCode("103");
				returnReq.setResultMsg("报文格式不对");
				returnReq.setErr("报文格式不对");
				returnReq.setSer("");
				returnReq.setHaveSer(false);
				return "fail"; 
			}
			
		}catch (Exception e) {
			EmpExecutionContext.error(e,"判断报文是否正确出现异常！");
		}
		return returnMsg;
	}
	
	/**
	 *   判断字符串是否相同
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean equals(String s1, String s2) {
		if (UpWSTools.isNotNull(s1) && UpWSTools.isNotNull(s2) && s1.equals(s2)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	
	

}
