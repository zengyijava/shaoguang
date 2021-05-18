package com.montnets.emp.table.group;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义通讯录_ 分组
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 上午11:16:17
 * @description
 */
public class TableLfList2gro
{
	//表名_自定义通讯录_ 分组
	public static final String TABLE_NAME = "LF_LIST2GRO";
	//标识列
	public static final String L2G_ID = "L2G_ID";
	// 添加类型(员工0,客户1,手工2) 
	public static final String L2G_TYPE = "L2G_TYPE";
	//分组ID
	public static final String UDG_ID = "UDG_ID";
	//唯一标识  L2G_TYPE
	//为0时，插入的是员工通讯录的GUID；
	//为1时,插入的是客户通讯录的GUID；
	//为2时，插入的是自定义通讯录的GUID;
	//为3时，插入的是操作员的GUID)
	public static final String GUID = "GUID";
	//共享类型
	public static final String SHARE_TYPE = "SHARE_TYPE";
	//序列ID
	public static final String SEQUENCE = "S_LF_LIST2GRO";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfList2gro", TABLE_NAME);
		columns.put("tableId", L2G_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("l2gId", L2G_ID);
 		columns.put("l2gType", L2G_TYPE);
		columns.put("udgId", UDG_ID);
		columns.put("sharetype",SHARE_TYPE);
		columns.put("guId",GUID);
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
