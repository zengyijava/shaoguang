package com.montnets.emp.table.group;

import java.util.HashMap;
import java.util.Map;

/**
 * 手工添加通讯名单
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 上午11:29:04
 * @description
 */
public class TableLfMalist
{
	//表名_手工添加通讯名单
	public static final String TABLE_NAME = "LF_MALIST";
	//手工添加ID
	public static final String MA_ID = "MA_ID";
	//姓名
	public static final String NAME = "NAME";
	//性别
	public static final String SEX = "SEX";
	//生日
	public static final String BIRTHDAY = "BIRTHDAY";
	//手机
	public static final String MOBILE = "MOBILE";
	//办公电话
	public static final String OPH = "OPH";
	//QQ号码
	public static final String QQ = "QQ";
	//Email地址
	public static final String E_MAIL = "E_MAIL";
	//Msn号码
	public static final String MSN = "MSN";
	//机构ID
	public static final String DEP_ID = "DEP_ID";
	//员工ID
	public static final String USER_ID = "USER_ID";
	//操作员ID
	public static final String GUID = "GUID";
	//企业编码
	public static final String CORP_CODE="CORP_CODE";
	//序列号
	public static final String SEQUENCE = "S_LF_MALIST";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfMalist", TABLE_NAME);
		columns.put("tableId", MA_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("maId", MA_ID);
		columns.put("name", NAME);
		columns.put("sex", SEX);
		columns.put("birthday", BIRTHDAY);
		columns.put("mobile", MOBILE);
		columns.put("oph", OPH);
		columns.put("qq", QQ);
		columns.put("email", E_MAIL);
		columns.put("msn", MSN);
		columns.put("depId", DEP_ID);
		columns.put("userId", USER_ID);
		columns.put("guId",GUID);
		columns.put("corpCode",CORP_CODE);
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
