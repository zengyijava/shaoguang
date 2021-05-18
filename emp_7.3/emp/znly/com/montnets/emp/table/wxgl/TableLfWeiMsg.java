package com.montnets.emp.table.wxgl;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信上行/下行记录（LF_WEI_MSG）
 * 
 * @project p_weix
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWeiMsg
{
	// 表名：微信上行/下行记录
	public static final String				TABLE_NAME	= "LF_WEI_MSG";

	// 消息记录自动编号
	public static final String			MSG_ID		= "MSG_ID";

	// 消息的类型（0：文本消息；1：图片消息；2：地理位置消息；3：链接消息；4：事件推送；5：回复文本；6：回复图文；7：回复语音消息）
	public static final String			MSG_TYPE	= "MSG_TYPE";

	// 微信Id
	public static final String			WC_ID		= "WC_ID";

	// 公众帐号Id
	public static final String			A_ID		= "A_ID";

	// 上行/下行
	public static final String			TYPE		= "TYPE";

	// 消息的XML数据
	public static final String			MSG_XML		= "MSG_XML";

	// 消息的预览-在展示消息历史记录的时候不需要解析msgXML格式数据
	public static final String			MSG_TEXT	= "MSG_TEXT";

	// 上下行关联
	public static final String			PARENT_ID	= "PARENT_ID";

	// 企业编号
	public static final String			CORP_CODE	= "CORP_CODE";

	// 创建时间
	public static final String			CREATETIME	= "CREATETIME";

	// 序列
	public static final String			SEQUENCE	= "514";

	// 映射集合
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfWeiMsg", TABLE_NAME);
		columns.put("tableId", MSG_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("msgId", MSG_ID);
		columns.put("msgType", MSG_TYPE);
		columns.put("wcId", WC_ID);
		columns.put("AId", A_ID);
		columns.put("type", TYPE);
		columns.put("msgXml", MSG_XML);
		columns.put("msgText", MSG_TEXT);
		columns.put("parentId", PARENT_ID);
		columns.put("corpCode", CORP_CODE);
		columns.put("createTime", CREATETIME);

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
