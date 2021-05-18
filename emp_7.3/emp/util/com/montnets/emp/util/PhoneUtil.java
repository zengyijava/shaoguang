package com.montnets.emp.util;

import java.util.HashSet;

import com.montnets.emp.security.numsegment.OprNumSegmentBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.context.EmpExecutionContext;
/**
 * 号码处理工具类
 * @project emp_new
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-8-26 下午07:18:17
 * @description
 */
public class PhoneUtil
{
	OprNumSegmentBiz oprNumSegmentBiz = new OprNumSegmentBiz();
	/**
	 * 过滤重复号码
	 * @param phoneSet 合法号码集合
	 * @param phone 传入需要判断的号码
	 * @return 返回false：代表是重号，返回true：代表不是重号。
	 * @throws EMPException 抛出异常，即过滤重号失败。
	 */
	public boolean checkRepeat(HashSet<Long> phoneSet,String phone) throws EMPException
	{
		try
		{
			//国际号码转为负数(00开头转为LONG型会自动删除00)
			if("+".equals(phone.substring(0, 1)))
			{
				phone = "-" + phone.substring(1);
			}
			else if("00".equals(phone.substring(0, 2)))
			{
				phone = "-" + phone.substring(2);
			}
			// 添加号码，利用HashSet特性，添加的对象与集合中不存在就添加，存在就覆盖。
			if(phoneSet.add(Long.parseLong(phone.trim())))
			{
				//号码不是重号
				return true;
			}
			else
			{
				//号码是重号
				return false;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"过滤重号失败！");
			throw new EMPException(IErrorCode.V10007, e);
		}
	}

	/**
	 * 过滤重复号码(重载checkRepeat方法,21位字符串转为LONG型时超界)
	 * @param phoneSet 合法号码集合
	 * @param phone 传入需要判断的号码
	 * @return 返回1：代表是重号，返回0：代表不是重号，返回-1：表示非法号码。
	 * @throws EMPException 抛出异常，即过滤重号失败。
	 */
	public int checkRepeat(String phone, HashSet<Long> phoneSet) throws EMPException
	{
		try
		{
			//国际号码转为负数(00开头转为LONG型会自动删除00)
			if("+".equals(phone.substring(0, 1)))
			{
				phone = "-" + phone.substring(1);
			}
			else if("00".equals(phone.substring(0, 2)))
			{
				phone = "-" + phone.substring(2);
			}

			try
			{
				// 添加号码，利用HashSet特性，添加的对象与集合中不存在就添加，存在就覆盖。
				if(phoneSet.add(Long.parseLong(phone.trim())))
				{
					//号码不是重号
					return 0;
				}
				else
				{
					//号码是重号
					return 1;
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error("过滤重号时转换类型异常！phone:" + phone);
				//号码格式错误,
				return -1;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"过滤重号异常！");
			throw new EMPException(IErrorCode.V10007, e);
		}
	}
	
	/**
	 * 过滤重复号码，如果不在合法号码集合内，不添加到集合中,区别于checkRepeat
	 * @param phoneSet 合法号码集合
	 * @param phone 传入需要判断的号码
	 * @return
	 * @throws EMPException
	 */
	public boolean checkRepeatWithOutAdd(HashSet<Long> phoneSet,String phone) throws EMPException
	{
		try
		{
			if(phoneSet.contains(Long.parseLong(phone))){
				return false;
			}
			return true;
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"过滤重号失败！");
			throw new EMPException(IErrorCode.V10007,e);
		}
	}
	
	/**
	 * 验证号码的号段是否合法
	 * @param mobile
	 * @param haoduan
	 * @return
	 * @throws Exception
	 */
	public int checkMobile(String mobile, String[] haoduan) throws Exception
	{
		try
		{
			//如果手机号不是11位，就返回
			if (mobile.length() != 11)
			{
				return 0;
			}
			for (int k = mobile.length(); --k >= 0;)
			{
				char single = mobile.charAt(k);
				//增加根据Unicode编码判断是否为0-9的数字
				if (!Character.isDigit(single) || (int)single > 57 || (int)single < 48)
				{
					return 0;
				}
			}
			//获取号码归属运营商
			int phoneType = oprNumSegmentBiz.getphoneType(mobile);
			if(phoneType == -1)
			{
				return 0;
			}
			return 1;
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"验证号码的号段是否合法异常！");
			throw new EMPException(IErrorCode.B20016,e);
		}	
	}	
	
	/**
	 * 验证号码是否合法，是否存在于运营商号段，获取运营商标识
	 * @param mobile 发送号码
	 * @param haoduan 号码段
	 * @return -1: 非法号码  0:移动号码  1:联通号码  2:电信号码
	 * @throws Exception
	 */
	public int getOprAndcheckValid(String mobile, String[] haoduan)
	{
		try
		{
			//如果手机号不是11位，就返回
			if (mobile.length() != 11)
			{
				return -1;
			}
			for (int k = mobile.length(); --k >= 0;)
			{
				char single = mobile.charAt(k);
				//增加根据Unicode编码判断是否为0-9的数字
				if (!Character.isDigit(single) || (int)single > 57 || (int)single < 48)
				{
					return -1;
				}
			}
			//获取号码归属运营商
			int phoneType = oprNumSegmentBiz.getphoneType(mobile);
			if(phoneType == 21)
			{
				phoneType = 2;
			}
			return phoneType;
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"验证号码的号段是否合法异常！错误码:" + IErrorCode.B20016);
			return -1;
		}
	}	
	
	/**
	 * 检测手机号
	 * @param mobile 手机号
	 * @param spiscumu 运营商
	 * @param haoduan 号段
	 * @return 0-非法，1-合法
	 * @throws Exception
	 */
	public int checkMobile(String mobile, Integer spiscumu, String[] haoduan)
			throws Exception
	{
		//检测长度
		if (mobile.length() != 11)
		{
			return 0;
		}
		for (int k = mobile.length(); --k >= 0;)
		{
			char single = mobile.charAt(k);
			//增加根据Unicode编码判断是否为0-9的数字
			if (!Character.isDigit(single) || (int)single > 57 || (int)single < 48)
			{
				return 0;
			}
		}
		//获取号码归属运营商
		int phoneType = oprNumSegmentBiz.getphoneType(mobile);
		if(phoneType == -1)
		{
			return 0;
		}
		return 1;
	}
	
	/**
	 * 	验证号码是否合法，是否存在于运营商号段，获取运营商标识
	 * @description    
	 * @param phone 手机号码
	 * @param haoduan 号码段
	 * @return   -1: 非法号码  0:移动号码  1:联通号码  21:电信号码 5:国际号码    			 
	 */
	public int getPhoneType(String phone)
	{
		if(phone != null && !"".equals(phone))
		{
			//号码类型,默认为0移动号码
			int phoneType = 0;
			String phoneTemp = phone.trim();
			int phoneLen = phoneTemp.length();
			//号码为7-21位
			if(phoneLen >= 7 && phoneLen <= 21)
			{
				//国际号码以"00"或"+"开头
				if("+".equals(phoneTemp.substring(0, 1)))
				{
					phoneTemp = phoneTemp.substring(1);
					phoneLen = phoneTemp.length();
					phoneType = 5;
				}
				else if("00".equals(phoneTemp.substring(0, 2)))
				{
					phoneType = 5;
				}
				else if("86".equals(phoneTemp.substring(0, 2)))
				{
					phoneTemp = phoneTemp.substring(2);
					phoneLen = phoneTemp.length();
				}
				else if("086".equals(phoneTemp.substring(0, 3)))
				{
					phoneTemp = phoneTemp.substring(3);
					phoneLen = phoneTemp.length();
				}
				//不是国际号码并且手机号不是11位
				if(phoneType == 0 && phoneLen != 11)
				{
					return -1;
				}
				for (int k = phoneLen; --k >= 0;)
				{
					char single = phoneTemp.charAt(k);
					if (!Character.isDigit(single) || (int)single > 57 || (int)single < 48)
					{
						return -1;
					}
				}
				//不是国际号码
				if(phoneType -5 != 0)
				{
					//获取号码归属运营商
					phoneType = oprNumSegmentBiz.getphoneType(phoneTemp);
				}
				return phoneType;
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return -1;
		}
	}
	/**
	 * 	验证号码是否合法，是否存在于运营商号段，获取运营商标识
	 * @description    
	 * @param phone 手机号码
	 * @param haoduan 号码段
	 * @return   -1: 非法号码  0:移动号码  1:联通号码  2:电信号码 3:国际号码    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-3-26 下午07:44:56
	 */
	public int getPhoneType(String phone, String[] haoduan)
	{
		if(phone != null && !"".equals(phone))
		{
			//号码类型,默认为0移动号码
			int phoneType = 0;
			String phoneTemp = phone.trim();
			int phoneLen = phoneTemp.length();
			//号码为7-21位
			if(phoneLen >= 7 && phoneLen <= 21)
			{
				//国际号码以"00"或"+"开头
				if("+".equals(phoneTemp.substring(0, 1)))
				{
					phoneTemp = phoneTemp.substring(1);
					phoneLen = phoneTemp.length();
					phoneType = 3;
				}
				else if("00".equals(phoneTemp.substring(0, 2)))
				{
					phoneType = 3;
				}
				else if("86".equals(phoneTemp.substring(0, 2)))
				{
					phoneTemp = phoneTemp.substring(2);
					phoneLen = phoneTemp.length();
				}
				else if("086".equals(phoneTemp.substring(0, 3)))
				{
					phoneTemp = phoneTemp.substring(3);
					phoneLen = phoneTemp.length();
				}
				//不是国际号码并且手机号不是11位
				if(phoneType == 0 && phoneLen != 11)
				{
					return -1;
				}
				for (int k = phoneLen; --k >= 0;)
				{
					char single = phoneTemp.charAt(k);
					if (!Character.isDigit(single) || (int)single > 57 || (int)single < 48)
					{
						return -1;
					}
				}
				//不是国际号码
				if(phoneType -3 != 0)
				{
					//获取号码归属运营商
					phoneType = oprNumSegmentBiz.getphoneType(phoneTemp);
					if(phoneType == 21)
					{
						phoneType = 2;
					}
				}
				return phoneType;
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return -1;
		}
	}
	/**
	 * 是否为国际号码
	 * @description    
	 * @param phone	手机号码
	 * @return  false 否;true 是     			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-3-28 上午09:09:22
	 */
	public boolean isAreaCode(String phone)
	{
		if(phone != null && !"".equals(phone))
		{
			String phoneTemp = phone.trim();
			if(phoneTemp.length() >= 7 && phoneTemp.length() <= 21)
			{
				//国际号码以"00"或"+"开头
				if(!"+".equals(phoneTemp.substring(0, 1)) && !"00".equals(phoneTemp.substring(0, 2)))
				{
					return false;
				}
				if("+".equals(phoneTemp.substring(0, 1)))
				{
					phoneTemp = phoneTemp.substring(1);
				}
				//是否为数字字符串
				for (int k = phoneTemp.length(); --k >= 0;)
				{
					char single = phoneTemp.charAt(k);
					if (!Character.isDigit(single) || (int)single > 57 || (int)single < 48)
					{
						return false;
					}
				}
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
}
