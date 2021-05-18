package com.montnets.emp.table.client;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-21 下午01:48:09
 * @description
 */
public class TableLfClient5Pro {

	//表名 (客户表)
	public final static String TABLE_NAME = "LF_CLIENT";
	//客户ID
	public static final String CLIENT_ID = "CLIENT_ID";
	//客户机构
	public static final String DEP_ID = "DEP_ID";
	//分类ID
	public static final String CC_ID = "CC_ID";
	//客户姓名
	public static final String NAME = "NAME";
	//性别
	public static final String SEX = "SEX";
	//生日
	public static final String BIRTHDAY = "BIRTHDAY";
	//手机
	public static final String MOBILE = "MOBILE";
	//办公电话
	public static final String OPH = "OPH";
	//QQ号
	public static final String QQ = "QQ";
	//Email地址
	public static final String E_MAIL = "E_MAIL";
	//msn账号
	public static final String MSN = "MSN";
	//职位
	public static final String JOB = "JOB";
	//行业
	public static final String PROFESSION = "PROFESSION";
	//所属区域
	public static final String AREA = "AREA";
	//使用状态（0无效，1有效）
	public static final String C_STATE = "C_STATE";
	//客户信息是否（0否，1是）共享
	public static final String SHARE_STATE = "SHARE_STATE";
	//描述
	public static final String COMMENTS = "COMMENTS";
	//是否（0否，1是）接收短信
	public static final String REC_STATE = "REC_STATE";
	//隐藏手机号（0否，1是）
	public static final String HIDE_STATE = "HIDE_STATE";
	//添加操作员
	public static final String USER_ID = "USER_ID";
	//客户经理姓名
	public static final String ENAME = "ENAME";
	//业务类型（业务类型编码）
    public static final String BIZ_ID="BIZ_ID";
    //客户编号（唯一标识）
    public static final String CLIENT_CODE="CLIENT_CODE";
	//机构编码
    public static final String DEP_CODE = "DEP_CODE";
    //手机号码, 可能为空，多值时格式为phone1;phone2
 	public static final String PHONE="PHONE";
 	//员工批次号（yyyymmddxxxxx)
	public static final String BATCH_NO="BATCH_NO";
	//通讯录唯一标识
	public static final String GUID = "GUID";
	//企业编码
	public static final String CORP_CODE="CORP_CODE";
	//客户自定义属性 field 01~50
	public static final String FIELD01 = "FIELD01";
	
	public static final String FIELD02 = "FIELD02";

	public static final String FIELD03 = "FIELD03";

	public static final String FIELD04 = "FIELD04";

	public static final String FIELD05 = "FIELD05";
	
	public static final String SEQUENCE="2";
	
	protected final static Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfClient5Pro", TABLE_NAME);
		//columns.put("tableId", CLIENT_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("clientId", CLIENT_ID);
		columns.put("depId", DEP_ID);
		columns.put("ccId", CC_ID);
		columns.put("name", NAME);
		columns.put("sex", SEX);
		columns.put("birthday", BIRTHDAY);
		columns.put("mobile", MOBILE);
		columns.put("oph", OPH);
		columns.put("qq", QQ);
		columns.put("EMail", E_MAIL);
		columns.put("msn", MSN);
		columns.put("job", JOB);
		columns.put("profession", PROFESSION);
		columns.put("area", AREA);
		columns.put("cstate", C_STATE);
		columns.put("shareState", SHARE_STATE);
		columns.put("comments", COMMENTS);
		columns.put("recState", REC_STATE);
		columns.put("hideState", HIDE_STATE);
		columns.put("userId", USER_ID);
		columns.put("ename", ENAME);
		columns.put("bizId", BIZ_ID);
		columns.put("clientCode", CLIENT_CODE);
		columns.put("depCode", DEP_CODE);
 		columns.put("phone", PHONE);
		columns.put("batchNo", BATCH_NO);
		columns.put("guId", GUID);
		columns.put("corpCode",CORP_CODE);
		columns.put("field01", FIELD01);
		columns.put("field02", FIELD02);
		columns.put("field03", FIELD03);
		columns.put("field04", FIELD04);
		columns.put("field05", FIELD05);
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
