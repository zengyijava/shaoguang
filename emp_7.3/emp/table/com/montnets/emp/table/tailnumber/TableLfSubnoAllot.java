/**
 * 
 */
package com.montnets.emp.table.tailnumber;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-2 下午06:19:50
 * @description
 */

public class TableLfSubnoAllot
{
	//表名：子号分配规则
	public static final String TABLE_NAME = "LF_SUBNOALLOT";

	//自增ID
	public static final String SUID = "SUID";

	//模块编号
	public static final String MENU_CODE = "MENUCODE";

	//操作员GUID
	public static final String LOGINID = "LOGINID";

	//任务ID
	public static final String SP_USER = "SP_USER";

	//子号
	public static final String SUBNO = "SUBNO";

	//分配类型(0-固定 1-自动)
	public static final String ALLOT_TYPE = "ALLOTTYPE";

	//起始扩展子号位
	public static final String EXTEND_SUBNO_BEGIN = "EXTENDSUBNOBEGIN";

	//结束扩展子号位
	public static final String EXTEMD_SUBNO_END = "EXTEMDSUBNOEND";

	//当前正在使用的扩展子号
	public static final String USEDEXTEND_SUBNO = "USEDEXTENDSUBNO";

	//运营商
	public static final String SP_NUMBER = "SPNUMBER";

	//更新时间
	public static final String UPDATE_TIME = "UPDATETIME";

	//创建时间
	public static final String CREATE_TIME = "CREATETIME";

	//路由ID
	public static final String ROUTE_ID = "ROUTEID";

	//子号
	public static final String BUS_CODE = "BUS_CODE";

	//序列
	public static final String SEQUENCE = "S_LF_SUBNOALLOT";

	//机构编码
	public static final String DEP_ID = "DEP_ID";
	
	//分配类型（1-为机构分配子号，2-为操作员分配子号）
	public static final String SHARE_TYPE = "SHARE_TYPE";
	
	//编码
	public static final String CODES = "CODES";
	
	//编码类型（0-模块编码；1-业务编码；2-产品编码;3-机构编码；4-操作员GUID；5-发送任务）
	public static final String CODE_TYPE = "CODE_TYPE";
	
	//企业编码
	public static final String CORP_CODE = "CORP_CODE";
	
	//任务ID
	public static final String TASK_ID = "TASK_ID";
	
	//有效期，单位小时，默认24*7
	public static final String VALIDITY = "VALIDITY";
	
	//1有效，2无效(尾号有效性标示字段)
	public static final String ISVALID = "ISVALID";
	
	//映射集合
    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfSubnoAllot", TABLE_NAME);
		columns.put("tableId", SUID);
		columns.put("sequence", SEQUENCE);
		columns.put("suId", SUID);
		columns.put("menuCode", MENU_CODE);
		columns.put("loginId", LOGINID);
		columns.put("spUser", SP_USER);
		columns.put("subno", SUBNO);
		columns.put("allotType", ALLOT_TYPE);
		columns.put("extendSubnoBegin", EXTEND_SUBNO_BEGIN);
		columns.put("extendSubnoEnd", EXTEMD_SUBNO_END);
		columns.put("usedExtendSubno", USEDEXTEND_SUBNO);
		columns.put("spNumber", SP_NUMBER);
		columns.put("updateTime", UPDATE_TIME);
		columns.put("createTime", CREATE_TIME);
		columns.put("routeId", ROUTE_ID);
		columns.put("busCode", BUS_CODE);
		columns.put("depId", DEP_ID);
		columns.put("shareType", SHARE_TYPE);
		columns.put("codes", CODES);
		columns.put("codeType", CODE_TYPE);
		columns.put("corpCode", CORP_CODE);
		columns.put("taskId", TASK_ID);
		columns.put("validity", VALIDITY);
		columns.put("isValid", ISVALID);
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
