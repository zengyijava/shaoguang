package com.montnets.emp.table.wxgl;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信图文回复表（LF_WEI_RIMG）
 * 
 * @project p_weix
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWeiRimg
{

	public static final String				TABLE_NAME	= "LF_WEI_RIMG";

	// 图文编号ID
	public static final String			RIMG_ID		= "RIMG_ID";

	// 图文标题
	public static final String			TITLE		= "TITLE";

	public static final String			DESCRIPTION	= "DESCRIPTION";

	// 封面地址
	public static final String			PICURL		= "PICURL";

	// 外网链接地址
	public static final String			LINK		= "LINK";

	// 公众帐号的ID
	public static final String			A_ID		= "A_ID";

	// 企业标号
	public static final String			CORP_CODE	= "CORP_CODE";

	// 创建时间
	public static final String			CREATETIME	= "CREATETIME";
	
	// 更新时间
	public static final String			MODIFYTIME	= "MODIFYTIME";

	// 更新时间
	public static final String			SUMMARY	= "SUMMARY";
	
	// 原文连接
	public static final String          SOURCE_URL = "SOURCE_URL";

	// 序列
	public static final String			SEQUENCE	= "512";

	// 映射集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfWeiRimg", TABLE_NAME);
		columns.put("tableId", RIMG_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("rimgId", RIMG_ID);
		columns.put("title", TITLE);
		columns.put("description", DESCRIPTION);
		columns.put("picurl", PICURL);
		columns.put("link", LINK);
		columns.put("AId", A_ID);
		columns.put("corpCode", CORP_CODE);
		columns.put("createtime", CREATETIME);
		columns.put("modifytime", MODIFYTIME);
		columns.put("summary", SUMMARY);
		columns.put("sourceUrl", SOURCE_URL);

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
