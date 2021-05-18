package com.montnets.emp.table.pasroute;

import java.util.HashMap;
import java.util.Map;

/**
 * 彩信企业账户绑定表
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:22
 * @description
 */
public class TableLfMmsAccbind
{
	//表名_彩信企业账户绑定表
	public static final String TABLE_NAME = "LF_MMSACCBIND";
	//标识列ID
	public static final String ID = "ID";
	//彩信账号 
	public static final String MMS_USER = "MMS_USER";
	//操作员ID
	public static final String USER_ID = "USER_ID";
	//企业编码
	public static final String CORP_CODE = "CORP_CODE";
	//是否有效(0失效，1起效)
	public static final String IS_VALIDATE = "IS_VALIDATE";
	//序列
	public static final String SEQUENCE = "S_LF_MMSACCBIND";
	//创建时间
	public static final String CREATETIME = "CREATETIME";
	//备注
	public static final String DESCRIPTION = "DESCRIPTION";
	
	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfMmsAccbind", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("mmsUser", MMS_USER);
		columns.put("userId", USER_ID);
		columns.put("corpCode", CORP_CODE);
		columns.put("isValidate", IS_VALIDATE);
		columns.put("createTime", CREATETIME);
		columns.put("description", DESCRIPTION);
		
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
