package com.montnets.emp.table.wxgl;

import java.util.HashMap;
import java.util.Map;

/**
 * 手机号对应验证码表（LF_WEI_VERIFY）
 * 
 * @project p_weix
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWeiVerify
{

	// 表名：用户微信基本信息表
	public static final String				TABLE_NAME		= "LF_WEI_VERIFY";

	public static final String				VF_ID			= "VF_ID";

	// 微信ID
	public static final String			WC_ID			= "WC_ID";

	// 手机号
	public static final String			PHONE			= "PHONE";

	// 验证码
	public static final String			VERIFYCODE		= "VERIFYCODE";

	// 最后一次获取验证码成功的有效时间
	public static final String			CODETIME		= "CODETIME";

	// verifyTime 验证码的获取时间
	public static final String			LIMTTIME		= "LIMTTIME";

	// 企业编码
	public static final String			CORP_CODE		= "CORP_CODE";

	// 验证次数
	public static final String			VERIFY_COUNT	= "VERIFY_COUNT";

	// 序列
	public static final String			SEQUENCE		= "S_LF_WEI_VERIFY";

	// 映射集合
    protected static final Map<String, String>	columns			= new HashMap<String, String>();

	static
	{
		columns.put("LfWeiVerify", TABLE_NAME);
		columns.put("tableId", VF_ID);
		columns.put("vfId", VF_ID);
		columns.put("wcId", WC_ID);
		columns.put("phone", PHONE);
		columns.put("verifyCode", VERIFYCODE);
		columns.put("codeTime", CODETIME);
		columns.put("limitTime", LIMTTIME);
		columns.put("verifyCount", VERIFY_COUNT);
		columns.put("corpCode", CORP_CODE);

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
