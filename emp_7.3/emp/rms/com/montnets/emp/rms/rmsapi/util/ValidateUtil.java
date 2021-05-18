package com.montnets.emp.rms.rmsapi.util;


public class ValidateUtil {

	public static boolean accountIsNull(String userid,String pwd)
	{
		if ((null == userid || "".equals(userid))|| (null == pwd || "".equals(pwd))) {
			return true;
		}
	    return false;
	}
}
