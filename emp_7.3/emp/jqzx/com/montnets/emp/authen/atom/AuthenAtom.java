/**
 * @description  
 * @author liaojirong <ljr0300@163.com>
 * @datetime 2013-11-15 上午11:26:40
 */
package com.montnets.emp.authen.atom;

import com.montnets.emp.common.context.EmpExecutionContext;


/**
 * @description  用于解析 加密后数据
 * @project emp_std_192.169.1.81
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author liaojirong <ljr0300@163.com>
 * @datetime 2013-11-15 上午11:26:40
 */

public class AuthenAtom
{

	/**
	 * @description  构造方法        			 
	 * @author liaojirong <ljr0300@163.com>
	 * @datetime 2013-11-15 上午11:26:40
	 */
	public AuthenAtom()
	{
		
	}
	/***
	 * 解密
	 * @param code
	 * @return
	 */
	public String parseCode(String code){
		String comm="";
		for(int i=0;i<code.length();i++){
			if((i+1)%2==0){
				comm=comm+code.charAt(i);
			}
		}
		return comm;
	}
	
	/**
	 * 解码
	 * 
	 * @param str
	 * @return string
	 */
	public byte[] decode(String str) {
		byte[] bt = null;
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			bt = decoder.decodeBuffer(str);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"base64解码异常，str："+str);
		}

		return bt;
	}
    
	
	/**
	 * 加密处理
	 * @param password
	 * @return
	 */
	public String encrypt(String password){
	       String tmpname= _getRandomString(password.length()*2);
	       String accountvalue="";
	       for (int i = 0; i < password.length(); i++) {
	    	   accountvalue=accountvalue+tmpname.charAt(i*2)+password.charAt(i);

	       }
		return accountvalue;
	}
	
	/***
	 * 具体的用户的算法
	 * @param len
	 * @return
	 */
	public String _getRandomString(int len) {
	    String  $chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678"; // 默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1
	    int maxPos = $chars.length();
	    String pass = "";
	    for (int i = 0; i < len; i++) {
	    	Double number=Math.floor(Math.random() * maxPos);
	    	pass += $chars.charAt(number.intValue());
	    }
	    return pass;
	}
	
	/**
	 * 验证请求中是否存在漏洞
	 * @param request
	 * @return 是否可以继续请求
	 */
	public boolean holesProcessing(String request){
		boolean isContinue=true;
		if(request==null||"".equals(request)){
			return isContinue;
		}
		String signal=";,',\",--,(,),";
		String[] singalArr=signal.split(",");
		for(int i=0;i<singalArr.length;i++){
			if(request.indexOf(singalArr[i])>-1){
				isContinue=false;
				return isContinue;
			}
		}
		return isContinue;
	}

}
