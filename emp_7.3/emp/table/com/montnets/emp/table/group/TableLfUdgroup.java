/**
 * 
 */
package com.montnets.emp.table.group;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-28 上午09:32:32
 * @description
 */

public class TableLfUdgroup
{
	//表格：自定义通讯录群组
	public static final String TABLE_NAME = "LF_UDGROUP";

	//分组ID
	public static final String UDG_ID = "UDG_ID";

	//分组名称
	public static final String UDG_NAME = "UDG_NAME";

	//操作员ID
	public static final String USER_ID = "USER_ID";

	//群类型（1-互动群；2-广播群；3-超级群）
	public static final String GROUP_TYPE = "GROUP_TYPE";
	
	//群组所属（0-员工和自定义群组 ；1-客户和自定义群组）
	public static final String GP_ATTRIBUTE = "GP_ATTRIBUTE";
	
	public static final String SEND_MODE = "SEND_MODE";
	
	//共享状态
	public static final String SHARE_TYPE = "SHARE_TYPE";
	
	//群组Id
	public static final String GROUP_ID = "GROUP_ID";
	
	//群组拥有者
	public static final String RECEIVER = "RECEIVER";
	//群组拥有者
	public static final String SHARE_STATUS = "SHARE_STATUS";
	//群组拥有者
	public static final String CREATETIME = "CREATETIME";
	
	//序列
	public static final String SEQUENCE = "S_LF_UDGROUP";

	//映射集合
    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfUdgroup", TABLE_NAME);
		columns.put("tableId", UDG_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("udgId", UDG_ID);
		columns.put("udgName", UDG_NAME);
		columns.put("userId", USER_ID);
		columns.put("groupType", GROUP_TYPE);
		columns.put("gpAttribute", GP_ATTRIBUTE);
		columns.put("sendmode", SEND_MODE);
		columns.put("sharetype", SHARE_TYPE);
		columns.put("groupid", GROUP_ID);
		columns.put("receiver", RECEIVER);
		columns.put("shareStatus", SHARE_STATUS);
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
