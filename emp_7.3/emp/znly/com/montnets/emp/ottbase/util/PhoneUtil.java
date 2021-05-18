package com.montnets.emp.ottbase.util;

import java.util.HashSet;

import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.ottbase.constant.OttException;

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
	/**
	 * 过滤重复号码
	 * @param phoneSet 合法号码集合
	 * @param phone 传入需要判断的号码
	 * @return 返回false：代表是重号，返回true：代表不是重号。
	 * @throws OttException 抛出异常，即过滤重号失败。
	 */
	public boolean checkRepeat(HashSet<Long> phoneSet,String phone) throws OttException
	{
		try
		{
			// 添加号码之前，有效号码的数目。
			int beforeAddSize = phoneSet.size();
			// 添加号码，利用HashSet特性，添加的对象与集合中不存在就添加，存在就覆盖。
			phoneSet.add(Long.parseLong(phone));
			//如果添加之前集合的大小和添加之后集合的大小一致，则代表这个号码是重号。不一致，则代表这个号码不是重号。
			if(beforeAddSize == phoneSet.size())
			{
				//号码是重号
				return false;
			}
			else
			{
				//号码不是重号
				return true;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"过滤重号失败！");
			throw new OttException(IErrorCode.V10007, e);
		}
	}
	/**
	 * 过滤重复号码，如果不在合法号码集合内，不添加到集合中,区别于checkRepeat
	 * @param phoneSet 合法号码集合
	 * @param phone 传入需要判断的号码
	 * @return
	 * @throws OttException
	 */
	public boolean checkRepeatWithOutAdd(HashSet<Long> phoneSet,String phone) throws OttException
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
			throw new OttException(IErrorCode.V10007,e);
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
			
			String number = haoduan[0] + "," + haoduan[1] + "," + haoduan[2];
			//如果不存在该号段，返回0
			if (number.replace(mobile.substring(0, 3), "").length() == number
					.length())
			{
				return 0;
			}
			return 1;
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"验证号码的号段是否合法异常！");
			throw new OttException(IErrorCode.B20016,e);
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
			// 移动号码段
			String yd = haoduan[0];
			// 联通号码段
			String lt = haoduan[1];
			// 电信号码段
			String dx = haoduan[2];
			//发送号码前三位
			String mobilePart = mobile.substring(0, 3);
			//移动号码段,返回0
			if(yd.indexOf(mobilePart) > -1)
			{
				return 0;
			}
			//联通号码段,返回1
			else if(lt.indexOf(mobilePart) > -1)
			{
				return 1;
			}
			//电信号码段,返回2
			else if(dx.indexOf(mobilePart) > -1)
			{
				return 2;
			}
			//不在号段范围内
			else
			{
				return -1;
			}
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
		//号段
		String number = "";
		//属于移动时
		if (spiscumu - 0 == 0)
		{
			number = haoduan[0];
		}
		//属于联通时
		else if (spiscumu - 1 == 0)
		{
			number = haoduan[1];
		}
		//属于电信时
		else if (spiscumu - 21 == 0)
		{
			number = haoduan[2];
		}
		//检测是否存在于对应运营商的号段
		if (number.replace(mobile.substring(0, 3), "").length() == number
				.length())
		{
			return 0;
		}
		return 1;
	}
	
}
