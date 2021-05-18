package com.montnets.emp.ottbase.service;

import java.util.List;

/**
 * 
 * @author Administrator
 *
 */

public interface IMmsSend
{
	/**
	 * 
	 * @param userid
	 * @param password
	 * @param mobiles
	 * @param base64Content
	 * @param subject
	 * @return
	 */
	public String sendMms(String userid, String password, String mobiles,
			String base64Content, String subject);
	
	/**
	 * 调用.net的webservice查询彩信余额
	 * @param userid
	 * @param password
	 * @return
	 */
	public String getMmsBalance(String userid, String password);
	
}
