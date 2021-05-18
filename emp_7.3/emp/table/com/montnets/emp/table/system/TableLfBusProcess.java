/**
 * 
 */
package com.montnets.emp.table.system;

import java.util.HashMap;
import java.util.Map;


/**
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-16 上午08:56:22
 * @description  业务流程映射类
 */

public class TableLfBusProcess
{
	//表名 (业务流程)
	public static final String TABLE_NAME = "LF_BUS_PROCESS";
	//业务流程ID（自增） 
	public static final String BUSPRO_ID = "BUSPRO_ID";
	//业务编码
	public static final String BUS_CODE = "BUS_CODE";
	//菜单编码
	public static final String MENU_CODE = "MENU_CODE";
	//类名
	public static final String CLASS_NAME = "CLASS_NAME";
	//注册类型（0-mo上行；1-rpt报告）
	public static final String REG_TYPE = "REG_TYPE";
	//编码
	public static final String CODES = "CODES";
	//编码类型（0-模块编码；1-业务编码；2-产品编码;3-机构编码；4-操作员编码）
	public static final String CODE_TYPE = "CODE_TYPE";
	
	public static final String HTTP_URL="HTTP_URL";
	//发送类型
	public static final String SEND_TYPE="SEND_TYPE";
	//序列名
	public static final String SEQUENCE="S_LF_BUS_PROCESS";
	
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfBusProcess", TABLE_NAME);
		columns.put("tableId", BUSPRO_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("busProId", BUSPRO_ID);
		columns.put("busCode", BUS_CODE);
		columns.put("menuCode", MENU_CODE);
		columns.put("className", CLASS_NAME);
		columns.put("regType", REG_TYPE);
		columns.put("codes", CODES);
		columns.put("codeType", CODE_TYPE);
		columns.put("httpUrl", HTTP_URL);
		columns.put("sendType", SEND_TYPE);
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
