package com.montnets.emp.table.template;

import java.util.HashMap;
import java.util.Map;

public class TableLfTmplRela {

 	public static final String TABLE_NAME = "LF_TMPLRELA";
 	//自增ID
 	public static final String id = "ID";
	//模板ID
	public static final String templId = "TEMPL_ID";
	//模板共享类型，1-机构，2--操作员
	public static final String toUserType = "TOUSER_TYPE";
	//操作员或机构唯一编码，TYPE为2时，此字段为操作员ID，1时为机构ID
	public static final String toUser = "TOUSER";
	//新增时间
	public static final String createTime = "CREATETIME";
	//模板类型 1：短信模板；2：彩信模板；3:网讯模板
	public static final String templType = "TEMPL_TYPE";
	//是否为共享 0不共享 1 共享
	public static final String shareType = "SHARE_TYPE";
	//模板创建者Id
	public static final String createrId = "CREATER_ID";
	//企业编号
	public static final String corpCode = "CORP_CODE";
	
	// 序列
	public static final String	SEQUENCE = "S_LF_TMPLRELA";


    protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("sequence", SEQUENCE);
		columns.put("LfTmplRela", TABLE_NAME);
		columns.put("id", id);
		columns.put("tableId", id);
		columns.put("templId", templId);
		columns.put("toUserType", toUserType);
		columns.put("toUser", toUser);
		columns.put("createTime", createTime);
		columns.put("templType", templType);
		columns.put("shareType", shareType);
		columns.put("createrId", createrId);
		columns.put("corpCode", corpCode);
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM() {
		return columns;
	}

}
