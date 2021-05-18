package com.montnets.emp.table.wxcorp;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @project montnets_entity
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-10-25 下午04:01:04
 * @description
 */
public class TableLfSysCorp
{
	//表名 (企业表)
	public static final String TABLE_NAME = "LF_SYS_CORP";
	//标识ID
	public static final String CORP_ID = "CORP_ID";
	//企业编码
	public static final String CORP_CODE = "CORP_CODE";
	//企业名称
	public static final String CORP_NAME = "CORP_NAME";
	//手机号码
	public static final String MOBILE = "MOBILE";
	//联系电话
	public static final String PHONE = "PHONE";
	//企业地址
	public static final String ADDRESS = "ADDRESS";
	//联系人
	public static final String LINKMAN = "LINKMAN";
	//电子邮箱
	public static final String E_MAILS = "E_MAILS";

	public static final String SEQUENCE = "S_LF_CORP";

	public static final String USER_NAME = "USER_NAME";
	
	public static final String LGO_URL = "LGO_URL";
	//是否计费
	public static final String ISBALANCE = "ISBALANCE";
	//尾号位数（默认4位）
	public static final String SUBNO_DIGIT = "SUBNO_DIGIT";
	
	public static final String CUR_SUBNO = "CUR_SUBNO";
	//当前分配尾号（默认0）
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfSysCorp", TABLE_NAME);
		columns.put("tableId", CORP_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("corpID", CORP_ID);
		columns.put("corpCode", CORP_CODE);
		columns.put("corpName", CORP_NAME);
		columns.put("mobile", MOBILE);
		columns.put("phone", PHONE);
		columns.put("address", ADDRESS);
		columns.put("linkman", LINKMAN);
		columns.put("emails", E_MAILS);
		columns.put("userName", USER_NAME);
		columns.put("lgoUrl", LGO_URL);
		columns.put("isBalance", ISBALANCE);
		columns.put("subnoDigit", SUBNO_DIGIT);
		columns.put("curSubno", CUR_SUBNO);
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
