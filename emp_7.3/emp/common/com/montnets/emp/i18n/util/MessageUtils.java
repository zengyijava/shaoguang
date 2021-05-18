package com.montnets.emp.i18n.util;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.montnets.emp.common.constant.StaticValue;

public class MessageUtils {

	public static String getWord(String fileName, Locale locale, String key) {
		String message = getMessage(fileName, locale, key);
		return message;
	}

	public static Locale getLocale(HttpServletRequest request) {
		String language = extractLanguage(request);
		Locale locale = resolveLocale(language);
		return locale;
	}
	/**
	 * @param fileName 模块名(比如cxtj)
	 * @param key 关键字
	 * @param request 请求
	 * @return
	 */
	public static String extractMessage(String fileName, String key, HttpServletRequest request) {
		// 提取用户设置语言
		String language = extractLanguage(request);
		// 将用户设置语言转换为国际化语言情景
		Locale locale = resolveLocale(language);
		// 获取对应语言的值
		return getMessage(fileName, locale, key);
	}
	
	/**
	 * @param fileName 模块名(比如cxtj)
	 * @param key 关键字
	 * @param request 请求
	 * @return
	 */
	public static String extractMessage(String fileName, String key, String language) {
		// 提取用户设置语言
		// 将用户设置语言转换为国际化语言情景
		Locale locale = resolveLocale(language);
		// 获取对应语言的值
		return getMessage(fileName, locale, key);
	}

	/**
	 * 提取设置的语言， 优先获取请求中传过来的语言值， 如果没有则使用登录用户session中的语言值
	 * 
	 * @param pageContext
	 * @return
	 */
	private static String extractLanguage(HttpServletRequest request) {
		String language  = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
		return language;
	}

	/**
	 * 根据设置的语言获取国际化对象
	 * 
	 * @param language
	 * @return
	 */
	private static Locale resolveLocale(String language) {
		Locale locale = null;
		if (language != null) {
			if (StaticValue.ZH_CN.equals(language)) {
				locale = new Locale("zh", "CN");
			} else if (StaticValue.ZH_TW.equals(language)) {
				locale = new Locale("zh", "TW");
			} else if (StaticValue.ZH_HK.equals(language) || StaticValue.EN_US.equals(language)) {
				locale = new Locale("zh", "HK");
			}
		}
		if (null == locale) {
			locale = Locale.getDefault();
		}
		return locale;
	}

	/**
	 * 根据国际化语言和键获取对应语言的值
	 * 
	 * @param locale
	 * @param key
	 * @return
	 */
	private static String getMessage(String fileName, Locale locale, String key) {
		// 根据国际化语言获取对应语言键值对
		Map<String, String> loc = getLanguage(fileName, locale);
		// 过滤程序员代码，过进行其他操作
		key = filterKey(key);
		String value = loc.get(key);
		return value;
	}

	/**
	 * TODO 过滤代码
	 * 
	 * @param key
	 * @return
	 */
	private static String filterKey(String key) {
		return key;
	}

	/**
	 * 根据国际化语言获取对应语言键值对
	 * @param locale
	 * @param key
	 * @return
	 */
	private static Map<String, String> getLanguage(String fileName, Locale locale) {
		String language = locale.getLanguage();
		String country = locale.getCountry();
		String lc = language+"_"+country;
		Map<String,String> loc = MessageResource.getInstance().getMap(fileName, lc);
		return loc;
	}

	/**
	 * 根据国际化语言获取对应语言键值对
	 * 
	 * @param locale
	 * @param key
	 * @return
	 */
	private static Map<String, String> getLanguage(String fileName, HttpServletRequest request) {
		String language = extractLanguage(request);
		Locale locale = resolveLocale(language);
		return getLanguage(fileName, locale);
	}

}
