package com.montnets.emp.util;

/**
 * 加载DLL动态链接库
 * 
 * @author Jinny.Ding (djhsun@sina.com)
 * @version 1.0
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011/10/25
 * @description 加载eatool.dll
 * 
 */
public class FinancialSMSEncrypt {
	// 加载eatool.dll
	static {
		//System.out.println(System.getProperty("java.library.path"));
		//System.out.println(new TxtFileUtil().getWebRoot()+"eatool.dll");
		//System.loadLibrary("eatool");
		System.load(new TxtFileUtil().getWebRoot()+"eatool.dll");
	}

	/**
	 * 
	 * 生成鉴权码 <br/> >0:成功,代表鉴权码的长度 其他:失败, 鉴权码:定义32字节
	 * 
	 * @param String
	 *            szUserId:SP帐号明文
	 * @param String
	 *            szPwd:密码明文
	 * @param String
	 *            szVerifyCode:随机验证码 定长8字节
	 * 
	 * @return String[]
	 */

	public native String[] GenAuthenCode(String strUserId, String strPwd,
			String strVerifyCode);

	/**
	 * 加密短信内容 <br/> >0:成功,代表加密后短信的长度 其他:失败
	 * 
	 * @param String
	 *            szUserId:SP帐号明文
	 * @param String
	 *            szVerifyCode:随机验证码 定长8字节
	 * @param String
	 *            szInMsg:短信内容明文 最好不要超过320个汉字 640个字节
	 * @param int
	 *            szOutMsg:短信内容密文 至少要预留1024个字节
	 * @return String[]
	 */
	public native String[] EncryptMsg(String strUserId, String strVerifyCode,
			String strInMsg, int InMsgLen);

	/**
	 * //解密短信内容 <br/> >0:成功,代表解密后短信的长度 其他:失败
	 * 
	 * @param String
	 *            szUserId:SP帐号明文
	 * @param String
	 *            szVerifyCode:随机验证码 定长8字节
	 * @param String
	 *            szInMsg:短信内容密文 最长不超过1024个字节
	 * @param String
	 *            szOutMsg:短信内容明文 至少要预留1024个字节
	 */
	public native String[] DecryptMsg(String strUserId, String strVerifyCode,
			String strInMsg, int nInMsgLen);
}
