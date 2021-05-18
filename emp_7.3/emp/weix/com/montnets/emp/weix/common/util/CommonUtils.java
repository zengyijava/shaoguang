/**
 * Program : CommonUtils.java
 * Author : Administrator
 * Create : 2013-9-10 下午12:17:34
 * company ShenZhen Montnets Technology CO.,LTD.
 */

package com.montnets.emp.weix.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

/**
 * @author Administrator <510061684@qq.com>
 * @version 1.0.0
 * @2013-9-10 下午12:17:34
 */
public class CommonUtils
{
	public static final String	NULLTIP		= "不能为空！";

	public static final String	OVERSIZR	= "长度不得超过{size}个字符！";

	// public static final String FALSERULE="格式不正确！";
	/**
	 * object转为string
	 */
	public static String defaultString(Object obj)
	{
		if(obj == null)
			return "";
		return obj.toString();
	}

	/**
	 * 判断字符串是否包含中文字符
	 */
	public static boolean isContainsChinese(String str)
	{
		Pattern pat = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher matcher = pat.matcher(str);
		boolean flg = false;
		if(matcher.find())
		{
			flg = true;
		}
		return flg;
	}

	/**
	 * 判断字符串长度是否超出限制
	 * 
	 * @author zousy
	 * @create 2013-9-26 上午08:37:54
	 * @since
	 * @param str
	 * @param maxSize
	 * @return
	 */
	public static String judgeLength(String str, int maxSize)
	{
		if(StringUtils.isNotBlank(str))
		{
			str = str.trim();
			int size = 0;
			for (int i = 0; i < str.length(); i++)
			{
				String s = String.valueOf(str.charAt(i));
				if(s.matches("[\u4e00-\u9fa5]"))
				{
					size += 2;
				}
				else
				{
					size += 1;
				}
			}
			if(maxSize < size)
			{
				return OVERSIZR.replace("{size}", String.valueOf(maxSize));
			}
		}
		else
		{
			return NULLTIP;
		}
		return null;
	}

	/**
	 * 判断是否满足url格式
	 * 
	 * @author zousy
	 * @create 2013-9-26 上午08:38:47
	 * @since
	 * @param str
	 * @return
	 */
	public static boolean isUrl(String str)
	{
		boolean flag = false;
		String regex = "^http(s)?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-]*)?$";
		if(StringUtils.isNotBlank(str))
		{
			flag = str.matches(regex);
		}
		return flag;
	}

	/**
	 * 综合判断 包括非空、格式、长度 等
	 * 
	 * @author zousy
	 * @create 2013-9-26 上午08:39:25
	 * @since
	 * @param name
	 * @param str
	 * @param type
	 * @param maxSize
	 * @return
	 */
	public static String judgeRuleAndLength(String name, String str, int type, int maxSize)
	{
		String result = name;
		boolean flag = false;
		if(StringUtils.isNotBlank(str))
		{
			str = str.trim();
			// 英文数组组成
			if(type == 1)
			{
				flag = StringUtils.isAlphanumeric(str);
			}
			// url
			if(type == 2)
			{
				flag = isUrl(str);
			}
			// 满足字符组成格式
			if(flag)
			{
				String ju = judgeLength(str, maxSize);
				if(ju != null)
				{
					result += ju;
				}
				else
				{
					return null;
				}
			}
			else
			{
				if(type == 1)
				{
					result += "只能由数字、字母组成！";
				}
				if(type == 2)
				{
					result += "格式不合法！";
				}
			}

		}
		else
		{
			result += NULLTIP;
		}
		return result;
	}

}
