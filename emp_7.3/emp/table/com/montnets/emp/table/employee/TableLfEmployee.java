/**
 * 
 */
package com.montnets.emp.table.employee;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-26 上午10:12:02
 * @description
 */

public class TableLfEmployee
{
	//表名  员工信息表
	public static final String TABLE_NAME = "LF_EMPLOYEE";
	//员工ID
	public static final String EMPLOYEE_ID = "EMPLOYEEID";
	//职位ID
	public static final String P_ID = "P_ID";
	//机构ID
	public static final String DEP_ID = "DEP_ID";
	//工号
	public static final String EMPLOYEE_NO = "EMPLOYEE_NO";
	//员工状态（0无效，1有效）
	public static final String E_STATE = "E_STATE";
	//备注
	public static final String COMMENTS = "COMMENTS";
	//是否（0否，1是）接收短信
	public static final String REC_STATE = "REC_STATE";
	//隐藏手机号
	public static final String HIDEPH_STATE = "HIDEPH_STATE";
	//E-MAIL地址
	public static final String E_MAIL = "E_MAIL";
	//MSN号码
	public static final String MSN = "MSN";
	//QQ号码
	public static final String QQ = "QQ";
	//姓名
	public static final String NAME = "NAME";
	//性别
	public static final String SEX = "SEX";
	//手机
	public static final String MOBILE = "MOBILE";
	//生日
	public static final String BIRTHDAY = "BIRTHDAY";
	//办公电话
	public static final String OPH = "OPH";
	//通讯录唯一性标识
	public static final String GUID = "GUID";
	//业务类型（业务类型编码）
	public static final String BIZ_ID = "BIZ_ID";
	//员工编码
	public static final String EMPLOYEE_CODE = "EMPLOYEE_CODE";
	//部门编码
	//public static final String DEP_CODE = "DEP_CODE";
	// 更新时间
	public static final String LASTUPDDTTM = "LASTUPDDTTM";
	// 在职状态
	public static final String HR_STATUS = "HR_STATUS";
	// 1-表示操作员；0-表示非操作员
	public static final String IS_OPERATOR = "IS_OPERATOR";
	// 
	public static final String ADD_TYPE = "ADD_TYPE";
	//企业编码
	public static final String CORP_CODE="CORP_CODE";
	//职务
	public static final String DUTIES = "DUTIES";
	//传真号码
	public static final String FAX = "FAX";
	//序列号
	public static final String SEQUENCE = "S_LF_EMPLOYEE";
	//统一平台操作员标识ID
	public static final String UP_GUID = "UP_GUID";
	
	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfEmployee", TABLE_NAME);
		columns.put("tableId", EMPLOYEE_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("employeeId", EMPLOYEE_ID);
		columns.put("pid", P_ID);
		columns.put("depId", DEP_ID);
		columns.put("employeeNo", EMPLOYEE_NO);
		columns.put("estate", E_STATE);
		columns.put("commnets", COMMENTS);
		columns.put("recState", REC_STATE);
		columns.put("hidephState", HIDEPH_STATE);
		columns.put("email", E_MAIL);
		columns.put("msn", MSN);
		columns.put("qq", QQ);
		columns.put("name", NAME);
		columns.put("sex", SEX);
		columns.put("mobile", MOBILE);
		columns.put("birthday", BIRTHDAY);
		columns.put("oph", OPH);
		columns.put("guId", GUID);
		columns.put("bizId", BIZ_ID);
		columns.put("employeeCode", EMPLOYEE_CODE);
		//columns.put("depCode", DEP_CODE);
		columns.put("lastUpddttm", LASTUPDDTTM);
		columns.put("hrStatus", HR_STATUS);
		columns.put("isOperator", IS_OPERATOR);
		columns.put("addType",ADD_TYPE);
		columns.put("corpCode",CORP_CODE);
		columns.put("duties", DUTIES);
		columns.put("fax", FAX);
		columns.put("upGuId", UP_GUID);
		
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
