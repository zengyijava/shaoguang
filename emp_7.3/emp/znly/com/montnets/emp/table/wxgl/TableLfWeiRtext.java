package com.montnets.emp.table.wxgl;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信默认回复消息（LF_WEI_RTEXT）
 * 
 * @project p_weix
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWeiRtext
{

	public static final String				TABLE_NAME	= "LF_WEI_RTEXT";

	// 默认回复ID
	public static final String			TET_ID		= "TET_ID";

	// 消息的类型（0：文本回复；1：单图文回复；2：多图文回复；3：语音回复；）
	public static final String			TET_TYPE	= "TET_TYPE";

	// 模板Id
	public static final String			T_ID		= "T_ID";

	// 公众帐号的ID
	public static final String			A_ID		= "A_ID";

	// 企业编号
	public static final String			CORP_CODE	= "CORP_CODE";

	// 创建时间
	public static final String			CREATETIME	= "CREATETIME";

	// 更新时间
	public static final String			MODIFYTIME	= "MODIFYTIME";

	// 序列
	public static final String			SEQUENCE	= "S_LF_WEI_RTEXT";

	// 内容摘要，界面显示用
	public static final String			MSG_TEXT	= "MSG_TEXT";

	// 返回数据需要生成的XML格式
	public static final String			MSG_XML		= "MSG_XML";

	// 标题
	public static final String			TITLE		= "TITLE";

	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfWeiRtext", TABLE_NAME);
		columns.put("tableId", TET_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("TetId", TET_ID);
		columns.put("tetType", TET_TYPE);
		columns.put("msgText", MSG_TEXT);
		columns.put("msgXML", MSG_XML);
		columns.put("TId", T_ID);
		columns.put("AId", A_ID);
		columns.put("corpCode", CORP_CODE);
		columns.put("createtime", CREATETIME);
		columns.put("modifytime", MODIFYTIME);
		columns.put("title", TITLE);

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
