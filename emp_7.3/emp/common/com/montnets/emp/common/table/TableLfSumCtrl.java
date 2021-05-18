package com.montnets.emp.common.table;

import java.util.HashMap;
import java.util.Map;




/**
 * 汇总控制记录表
 * @功能概要：
 * @项目名称： p_xtgl
 * @初创作者： liuxiangheng <xiaokanrensheng1012@126.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-3-30 上午11:52:02
 * <p>修改记录1：</p>
 * <pre>    
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class TableLfSumCtrl
{
	//表名
	protected static final String TABLE_NAME = "LF_SUMCTRL";
	public static final String ID = "ID";
	public static final String NODEID = "NODEID";
	public static final String ISMAIN = "ISMAIN";
	public static final String SUMTYPE = "SUMTYPE";
	public static final String UPDATETIME = "UPDATETIME";

	public static final String SEQUENCE = "S_LF_SUMCTRL";

	
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfSumCtrl", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("nodeId", NODEID);
		columns.put("isMain", ISMAIN);
		columns.put("sumType", SUMTYPE);
		columns.put("updateTime", UPDATETIME);
		
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
