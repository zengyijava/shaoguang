/**
 * 
 */
package com.montnets.emp.table.tailnumber;

import java.util.HashMap;
import java.util.Map;

//import com.montnets.emp.common.StaticValue;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-4-2 下午06:30:03
 * @description
 */

public class TableLfSubnoAllotDetail
{
	//表名：子号分配详细的基本信息
	public static final String TABLE_NAME = "LF_SUBNOALLOT_DETAIL";

	//自增ID
	public static final String SUDID = "SUDID";

	//模块编号
	public static final String MENU_CODE = "MENUCODE";

	//操作员GUID
	public static final String LOGINID = "LOGINID";

	//发送账号ID
	public static final String SP_USER = "SP_USER";

	//子号
	public static final String SUBNO = "SUBNO";

	//主通道号
	public static final String SPGATE = "SPGATE";

	//当前正在使用的扩展子号
	public static final String USEDEXTEND_SUBNO = "USEDEXTENDSUBNO";

	//全通道号
	public static final String SP_NUMBER = "SPNUMBER";

	//分配类型(0-固定 1-自动)
	public static final String ALLOT_TYPE = "ALLOTTYPE";

	//更新时间
	public static final String UPDATE_TIME = "UPDATETIME";

	//创建时间
	public static final String CREATE_TIME = "CREATETIME";

	//所属企业编码
	public static final String BUS_CODE = "BUS_CODE";

	//编码
	public static final String CODES = "CODES";
	
	//编码类型（0-模块编码；1-业务编码；2-产品编码;3-机构编码；4-操作员GUID；5-发送任务）
	public static final String CODE_TYPE = "CODE_TYPE";
	
	//对应子号分配概要表中的标识ID
	public static final String SUID = "SUID";
	
	//所属企业编码
	public static final String CORP_CODE = "CORP_CODE";
	
	//序列
	public static final String SEQUENCE = "S_LF_SUBNOALLOT_DETAIL";
	
	//任务id
	public static final String TASK_ID = "TASK_ID";
	
	//有效期，单位小时，默认24*7
	public static final String VALIDITY = "VALIDITY";
	
	//机构编码
	public static final String DEP_ID = "DEP_ID";
	
	//1有效，2无效(尾号有效性标示字段)
	public static final String ISVALID = "ISVALID";

	//映射集合
	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfSubnoAllotDetail", TABLE_NAME);
		columns.put("tableId", SUDID);
		columns.put("sequence", SEQUENCE);
		columns.put("sudId", SUDID);
		columns.put("menuCode", MENU_CODE);
		columns.put("loginId", LOGINID);
		columns.put("spUser", SP_USER);
		columns.put("spgate", SPGATE);
		columns.put("subno", SUBNO);
		columns.put("usedExtendSubno", USEDEXTEND_SUBNO);
		columns.put("spNumber", SP_NUMBER);
		columns.put("allotType", ALLOT_TYPE);
		columns.put("updateTime", UPDATE_TIME);
		columns.put("createTime", CREATE_TIME);
		columns.put("busCode", BUS_CODE);
		columns.put("codes", CODES);
		columns.put("codeType", CODE_TYPE);
		columns.put("suId", SUID);
		columns.put("corpCode", CORP_CODE);
		columns.put("taskId", TASK_ID);
		columns.put("validity", VALIDITY);
		columns.put("depId", DEP_ID);
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
