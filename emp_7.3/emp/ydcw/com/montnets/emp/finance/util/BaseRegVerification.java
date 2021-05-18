package com.montnets.emp.finance.util;

/**
 * 公共验证类
 * 
 * @author Jinny.Ding (djhsun@sina.com)
 * @version 1.0
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @date Mar 31, 2012
 */
public interface BaseRegVerification {
	/**
	 * 验证用户输入的手机号码是否合法
	 * 
	 * @param <b>String
	 *            </b> content
	 * @return boolean <br/> <b>false</b> 手机号码不合法
	 */
	public boolean isMobileNO(String mobiles) throws Exception;

	/**
	 * 判断财务短信内容的长度,不能大于640个字节
	 * 
	 * @param financialMsg
	 *            短信内容
	 * @return boolean
	 */
	public boolean isByteLengthWithFinancialMsg(String financialMsg)
			throws Exception;
}
