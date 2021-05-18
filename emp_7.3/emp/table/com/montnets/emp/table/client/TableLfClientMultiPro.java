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
public class TableLfClientMultiPro {

	//表名 (客户表)
	public static final String TABLE_NAME = "LF_CLIENT";
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

	public static final String FIELD06 = "FIELD06";

	public static final String FIELD07 = "FIELD07";

	public static final String FIELD08 = "FIELD08";

	public static final String FIELD09 = "FIELD09";

	public static final String FIELD10 = "FIELD10";

	public static final String FIELD11 = "FIELD11";
	
	public static final String FIELD12 = "FIELD12";
	
	public static final String FIELD13 = "FIELD13";
	
	public static final String FIELD14 = "FIELD14";
	
	public static final String FIELD15 = "FIELD15";
	
	public static final String FIELD16 = "FIELD16";
	
	public static final String FIELD17 = "FIELD17";
	
	public static final String FIELD18 = "FIELD18";
	
	public static final String FIELD19 = "FIELD19";
	
	public static final String FIELD20 = "FIELD20";
	
	public static final String FIELD21 = "FIELD21";
	
	public static final String FIELD22 = "FIELD22";
	
	public static final String FIELD23 = "FIELD23";
	
	public static final String FIELD24 = "FIELD24";
	
	public static final String FIELD25 = "FIELD25";
	
	public static final String FIELD26 = "FIELD26";
	
	public static final String FIELD27 = "FIELD27";
	
	public static final String FIELD28 = "FIELD28";
	
	public static final String FIELD29 = "FIELD29";
	
	public static final String FIELD30 = "FIELD30";
	
	public static final String FIELD31 = "FIELD31";
	
	public static final String FIELD32 = "FIELD32";
	
	public static final String FIELD33 = "FIELD33";
	
	public static final String FIELD34 = "FIELD34";

	public static final String FIELD35 = "FIELD35";

	public static final String FIELD36 = "FIELD36";

	public static final String FIELD37 = "FIELD37";

	public static final String FIELD38 = "FIELD38";

	public static final String FIELD39 = "FIELD39";

	public static final String FIELD40 = "FIELD40";

	public static final String FIELD41 = "FIELD41";
	
	public static final String FIELD42 = "FIELD42";
	
	public static final String FIELD43 = "FIELD43";
	
	public static final String FIELD44 = "FIELD44";
	
	public static final String FIELD45 = "FIELD45";
	
	public static final String FIELD46 = "FIELD46";
	
	public static final String FIELD47 = "FIELD47";
	
	public static final String FIELD48 = "FIELD48";
	
	public static final String FIELD49 = "FIELD49";
	
	public static final String FIELD50 = "FIELD50";
	
	public static final String SEQUENCE="2";
	
	protected final static Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfClientMultiPro", TABLE_NAME);
		//columns.put("tableId", "");
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
		columns.put("field06", FIELD06);
		columns.put("field07", FIELD07);
		columns.put("field08", FIELD08);
		columns.put("field09", FIELD09);
		columns.put("field10", FIELD10);
		columns.put("field11", FIELD11);
		columns.put("field12", FIELD12);
		columns.put("field13", FIELD13);
		columns.put("field14", FIELD14);
		columns.put("field15", FIELD15);
		
		columns.put("field16", FIELD16);
		columns.put("field17", FIELD17);
		columns.put("field18", FIELD18);
		columns.put("field19", FIELD19);
		columns.put("field20", FIELD20);
		
		columns.put("field21", FIELD21);
		columns.put("field22", FIELD22);
		columns.put("field23", FIELD23);
		columns.put("field24", FIELD24);
		columns.put("field25", FIELD25);
		columns.put("field26", FIELD26);
		columns.put("field27", FIELD27);
		columns.put("field28", FIELD28);
		columns.put("field29", FIELD29);
		
		columns.put("field30", FIELD30);
		columns.put("field31", FIELD31);
		columns.put("field32", FIELD32);
		columns.put("field33", FIELD33);
		columns.put("field34", FIELD34);
		columns.put("field35", FIELD35);
		columns.put("field36", FIELD36);
		
		columns.put("field37", FIELD37);
		columns.put("field38", FIELD38);
		columns.put("field39", FIELD39);
		columns.put("field40", FIELD40);
		columns.put("field41", FIELD41);
		columns.put("field42", FIELD42);
		columns.put("field43", FIELD43);
		columns.put("field44", FIELD44);
		columns.put("field45", FIELD45);
		columns.put("field46", FIELD46);
		columns.put("field47", FIELD47);
		
		columns.put("field48", FIELD48);
		columns.put("field49", FIELD49);
		columns.put("field50", FIELD50);
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
