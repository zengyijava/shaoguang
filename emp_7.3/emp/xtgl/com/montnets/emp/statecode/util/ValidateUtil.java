package com.montnets.emp.statecode.util;

public class ValidateUtil {
	
	/**
	 * 验证字符串是否由数字加字母组成
	 * @param value  要验证的字符串
	 * @return     满足条件返回true，否则返回false
	 */
	public static boolean validate(String value) {
		if(value==null||"".equals(value)) {
			return false;
		}
		return value.matches("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{0,20}$")||value.matches("[a-zA-Z]{0,20}")||value.matches("[0-9]{0,20}");
	}
	public static boolean validateChar(String value) {
		if(value==null||"".equals(value)) {
			return false;
		}
		return value.matches("[a-zA-Z]{0,20}");
	}
	public static boolean validateNum(String value) {
		if(value==null||"".equals(value)) {
			return false;
		}
		return value.matches("[0-9]{0,20}");
	}
	/**
	 * 验证字符串是否为数字组成
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			int chr = str.charAt(i);
			if (chr < 48 || chr > 57)
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		
		String mappingCode1="11111aaaaa11111aaaaa";
		String mappingCode2="111111111111111111111";
		String mappingCode3="aaaaaaaaaaaaaaaaaaaaa";
		String mappingCode4="a@aaaaaaaaaaaaaaaaaa";
		String mappingCode5="1#111111111111111111";
		System.out.println(validate(mappingCode1));
		System.out.println(validate(mappingCode2));
		System.out.println(validate(mappingCode3));
		System.out.println(validate(mappingCode4));
		System.out.println(validate(mappingCode5));
		System.out.println("==========");
		if (mappingCode1 != null && !"".equals(mappingCode1) && (mappingCode1.length() > 20||!ValidateUtil.validate(mappingCode1)))
		{
			System.out.println("mappingCode:"+validate("映射码"));
		}
		
		/*String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{0,20}$";		

		String value = "aaa";  // 长度不够
		System.out.println(value+"："+value.matches(regex));

		value = "1111aaaa1111aaaaa";  // 太长
		System.out.println(value+"："+value.matches(regex));

		value = "111111111"; // 纯数字
		System.out.println(value+"："+value.matches(regex));

		value = "aaaaaaaaa"; // 纯字母
		System.out.println(value+"："+value.matches(regex));

		value = "####@@@@#"; // 特殊字符
		System.out.println(value+"："+value.matches(regex));

		value = "1111aaaa";  // 数字字母组合
		System.out.println(value+"："+value.matches(regex));

		value = "aaaa1111"; // 数字字母组合
		System.out.println(value+"："+value.matches(regex));

		value = "aa1111aa";	// 数字字母组合
		System.out.println(value+"："+value.matches(regex));

		value = "11aaaa11";	// 数字字母组合
		System.out.println(value+"："+value.matches(regex));

		value = "aa11aa11"; // 数字字母组合
		System.out.println(value+"："+value.matches(regex));*/
	}
}
