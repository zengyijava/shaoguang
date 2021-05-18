/**
 * 
 */
package com.montnets.emp.table.pasroute;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-30 下午05:00:19
 * @description
 */

public class TableLfSpDepBind
{
	//表名： 部门-账户-通道-操作员绑定关系
	public static final String TABLE_NAME = "LF_SP_DEP_BIND";

	//标识列ID
	public static final String DSG_ID = "DSG_ID";

	//机构ID
	public static final String DEP_ID = "DEP_ID";

	//网关账户标识列ID 
	public static final String SP_USER = "SPUSER";

	//网关通道标识列ID
	public static final String SP_GATEID = "SPGATEID";

	//操作员ID
	public static final String USER_ID = "USER_ID";

	//绑定类型(1-机构跟账户绑定   2-操作员跟账户绑定 ) 
	public static final String BIIND_TYPE = "BIIND_TYPE";

	//0-共享 1-独占
	public static final String SHARE_TYPE = "SHARE_TYPE";

	//
	public static final String DEP_CODE_THIRD = "DEP_CODE_THIRD";

	public static final String SEQUENCE = "S_LF_SP_DEP_BIND";
	
	public static final String MENU_CODE = "MENUCODE";
	
	public static final String BUS_CODE = "BUS_CODE";

	public static final String SELBIN_TYPE = "SELBIN_TYPE";
	
	//业务账户绑定类型 （1-emp 2-DBServer）
	public static final String PLATFORM_TYPE = "PLATFORM_TYPE";
	
	//业务ID
	public static final String BUS_ID = "BUS_ID";
	
	//企业编码
	public static final String CORP_CODE="CORP_CODE";
	
	//是否启用
	public static final String IS_VALIDATE = "IS_VALIDATE";
	
	//集合
	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfSpDepBind", TABLE_NAME);
		columns.put("tableId", DSG_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("dsgId", DSG_ID);
		columns.put("depId", DEP_ID);
		columns.put("spUser", SP_USER);
		columns.put("spgateId", SP_GATEID);
		columns.put("userId", USER_ID);
		columns.put("bindType", BIIND_TYPE);
		columns.put("shareType", SHARE_TYPE);
		columns.put("depCodeThird", DEP_CODE_THIRD);
		columns.put("menuCode", MENU_CODE);
		columns.put("busCode", BUS_CODE);
		columns.put("selBinType", SELBIN_TYPE);
		columns.put("platFormType", PLATFORM_TYPE);
		columns.put("busId", BUS_ID);
		columns.put("corpCode",CORP_CODE);
		columns.put("isValidate", IS_VALIDATE);
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
