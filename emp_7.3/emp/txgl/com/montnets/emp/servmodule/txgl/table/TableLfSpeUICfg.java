/**
 * Program  : TableLfSpeUICfg.java
 * Author   : zousy
 * Create   : 2013-11-20 下午04:08:13
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.servmodule.txgl.table;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author   Administrator <510061684@qq.com>
 * @version  1.0.0
 * @2013-11-20 下午04:08:13
 */
public class TableLfSpeUICfg
{
	public static final String				TABLE_NAME	= "LF_SPEUICFG";
	//内页公司名
	private static final String			COMPANYNAME		= "COMPANYNAME";
	//内页公司logo
	private static final String			COMPANYLOGO		= "COMPANYLOGO";
	//登录页背景图
	private static final String			BGIMG		= "BGIMG";
	//登录页logo
	private static final String			LOGINLOGO		= "LOGINLOGO";
	//个性化显示内容
	private static final String			DISPCONTENT	= "DISPCONTENT";
	//显示类别  1 默认 0个性化
	private static final String			DISPLAYTYPE	= "DISPLAYTYPE";
	//机构编码
	private static final String			CORPCODE	= "CORPCODE";

	// 集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{

		columns.put("LfSpeUICfg", TABLE_NAME);
		columns.put("companyName", COMPANYNAME);
		columns.put("companyLogo", COMPANYLOGO);
		columns.put("bgImg", BGIMG);
		columns.put("loginLogo", LOGINLOGO);
		columns.put("dispContent", DISPCONTENT);
		columns.put("displayType", DISPLAYTYPE);
		columns.put("corpCode", CORPCODE);
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM()
	{
		return columns;
	}
}

