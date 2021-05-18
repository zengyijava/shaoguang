package com.montnets.emp.table.wxsysuser;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:22
 * @description
 */
public class TableLfSysUser
{
	//表名：操作员的基本信息
	public static final String TABLE_NAME = "LF_SYS_USER";

	//操作员ID
	public static final String USER_ID = "USER_ID";

	//机构ID
	public static final String DEP_ID = "DEP_ID";

	//登录名
	public static final String USER_NAME = "USER_NAME";

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

	//QQ
	public static final String QQ = "QQ";

	//E-MAIL
	public static final String E_MAIL = "E_MAIL";

	//MSN
	public static final String MSN = "MSN";
	
	//fax
	public static final String FAX = "FAX";

	//登录密码
	public static final String PAZZWORD = "PASSWORD";

	//操作员状态（0无效，1有效）
	public static final String USER_STATE = "USER_STATE";

	//开户日期
	public static final String REG_TIME = "REG_TIME";

	//开户人
	public static final String HOLDER = "HOLDER";

	//描述
	public static final String COMMENTS = "COMMENTS";

	//工作状态（0无效，1有效）
	public static final String WORK_STATE = "WORK_STATE";

	//手工输入权限
	public static final String MANUAL_INPUT = "MANUAL_INPUT";

	//用户类型（1-管理员 ；2-用户）
	public static final String USER_TYPE = "USER_TYPE";

	//
	public static final String UDG_ID = "UDG_ID";

	//是否（0否，1是）能查看部门客户
	public static final String CLIENT_STATE = "CLIENT_STATE";

	//员工ID(对应员工表中的员工ID)
	public static final String EMPLOYEE_ID = "EMPLOYEEID";

	//唯一标识（通讯录用）
	public static final String GUID = "GUID";
	
	//判断是否在线（0-不在线；1-在线）
	public static final String ON_LINE = "ON_LINE";
	
	//职位ID（对应LF_POSITION表中的P-ID)
	public static final String P_ID="P_ID";
	
	//企业编码
	public static final String CORP_CODE="CORP_CODE";
	
	//
	public static final String POST_ID = "POST_ID";
	
	//在职状态（A表示在职，其他为离职）	默认为A
	public static final String HRSTATUS = "HR_STATUS";

	// 权限类型：1系统管理员， 2：客服管理员，3：客服 人员 
	public static final String PERMISSION_TYPE = "PERMISSION_TYPE";
	
	//用户编码
	public static final String USER_CODE = "USER_CODE";
	
	//是否分配固定尾号
	public static final String ISEXISTSUBNO = "ISEXISTSUBNO";
	
	//序列
	public static final String SEQUENCE = "19";
	
	//统一平台操作员标识ID
	public static final String UP_GUID = "UP_GUID";
	
	//是否是审核人，1：是，2：否
	public static final String IS_REVIEWER = "IS_REVIEWER";
	//职务
	public static final String DUTIES = "DUTIES";
	//是否需要被审核，1：要，2：不需要
	public static final String IS_AUDITED = "IS_AUDITED";
	
	//所属公众账号
    public static final String A_ID = "A_ID";

	//映射集合
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfSysUser", TABLE_NAME);
		columns.put("tableId", USER_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("userId", USER_ID);
		columns.put("depId", DEP_ID);
		columns.put("userName", USER_NAME);
		columns.put("name", NAME);
		columns.put("sex", SEX);
		columns.put("birthday", BIRTHDAY);
		columns.put("mobile", MOBILE);
		columns.put("oph", OPH);
		columns.put("qq", QQ);
		columns.put("EMail", E_MAIL);
		columns.put("msn", MSN);
		columns.put("fax", FAX);
		columns.put("password", PAZZWORD);
		columns.put("userState", USER_STATE);
		columns.put("regTime", REG_TIME);
		columns.put("holder", HOLDER);
		columns.put("comments", COMMENTS);
		columns.put("workState", WORK_STATE);
		columns.put("manualInput", MANUAL_INPUT);
		columns.put("userType", USER_TYPE);
		columns.put("udgId", UDG_ID);
		columns.put("clientState", CLIENT_STATE);
		columns.put("employeeId", EMPLOYEE_ID);
		columns.put("guId",GUID);
		columns.put("onLine", ON_LINE);
		columns.put("corpCode",CORP_CODE);
		columns.put("pid",P_ID);
		columns.put("postId", POST_ID);
		columns.put("hrStatus", HRSTATUS);
		columns.put("permissionType", PERMISSION_TYPE);
		columns.put("userCode", USER_CODE);
		columns.put("isExistSubNo", ISEXISTSUBNO);
		columns.put("upGuId", UP_GUID);
		columns.put("isReviewer", IS_REVIEWER);
		columns.put("duties", DUTIES);
		columns.put("AId", A_ID);
		columns.put("isAudited", IS_AUDITED);
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
