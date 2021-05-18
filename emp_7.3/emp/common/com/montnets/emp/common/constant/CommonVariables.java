package com.montnets.emp.common.constant;

import java.util.Map;

/**
 * 参数传递类
 * @author LinZhiHan
 *
 */
public class CommonVariables{
	//错误编码
	private Integer errorCode;
	//字符串类型的错误编码
	private String strErrorCode;
	//机构Id字符串
	private String depIdStrs;
	
	//手机号码屏蔽的替换字符
	private String phoneReplaceChar = "*****";
	//手机号码屏蔽的起始位
	private int beginRe = 3;
	//手机号码屏蔽的结束位
	private int endRe = 8;
	
	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getStrErrorCode()
	{
		return strErrorCode;
	}

	public void setStrErrorCode(String strErrorCode)
	{
		this.strErrorCode = strErrorCode;
	}

	public String getDepIdStrs() {
		return depIdStrs;
	}

	public void setDepIdStrs(String depIdStrs) {
		this.depIdStrs = depIdStrs;
	}
	
	/**
	 * 屏蔽手机号码
	 * @param btnMap 权限
	 * @param phone 手机号码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String replacePhoneNumber(Map btnMap,String phone)
	{
		//判断权限是否存在
		if(btnMap.get(StaticValue.PHONE_LOOK_CODE) == null )
		{
			phone = replacePhoneNumber(phone);
		}
		//返回结果
		return phone; 
	}
	
	/**
	 * 屏蔽手机号码
	 * @param phone 手机号码
	 * @return
	 */
	public String replacePhoneNumber(String phone)
	{
		//传入号码未空时，返回空串
		if(phone == null)
		{
			return "";
		}
		phone = phone.trim();
		//判断号码长度
		int len = phone.length();
		if(len >= endRe)
		{
			//替换手机号码
			phone = phone.substring(0,beginRe)+phoneReplaceChar+phone.substring(endRe);
		}else if(len<=phoneReplaceChar.length()){
			phone = phoneReplaceChar;
		}else{
			int prefix = (len-phoneReplaceChar.length())/2;
			phone = phone.substring(0,prefix)+phoneReplaceChar+phone.substring(prefix+phoneReplaceChar.length());
		}
		return phone; 
	}
	
/*	public static void main(String[] args)
	{
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		CommonVariables c = new CommonVariables();
		for(int i=0;i<15;i++){
			sb.append(r.nextInt(10));
			String str = sb.toString();
			System.out.println(str+">>>"+c.replacePhoneNumber(str));
		}
	}*/
}
