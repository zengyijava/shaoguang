package com.montnets.emp.common.biz;

/*
	浏览器检测工具
 */
public class BrowseTool {
	public static String checkBrowse(String userAgent){
		if(userAgent.contains("360SE"))return "360SE";
		if(userAgent.contains("SE 2.X"))return "Sogou";
		if(userAgent.contains("Chrome"))return "Chrome";
		if(userAgent.contains("Firefox"))return "Firefox";
		if(userAgent.contains("Trident/7.0"))return "IE11";
		if(userAgent.contains("Trident/6.0"))return "IE10";
		if(userAgent.contains("Trident/5.0"))return "IE9";
		if(userAgent.contains("Trident/4.0"))return "IE8";
		if(userAgent.contains("MSIE 7.0"))return "IE7";
		if(userAgent.contains("MSIE 6.0"))return "IE6";
		if(userAgent.contains("MSIE")||userAgent.contains("Trident"))return "IE";
		return "other";
	}
}