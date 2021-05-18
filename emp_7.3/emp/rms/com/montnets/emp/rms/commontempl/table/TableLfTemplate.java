package com.montnets.emp.rms.commontempl.table;

import java.util.LinkedHashMap;
import java.util.Map;

public class TableLfTemplate {

	// 表名
	public static final String TABLE_NAME = "LF_TEMPLATE";
	//模板ID-自增ID
	public static final String TM_ID = "TM_ID";
	//操作员ID
	public static final String USER_ID = "USER_ID";
	//模板名称
	public static final String TM_NAME  = "TM_NAME";
	//模板路径
	public static final String TM_MSG = "TM_MSG";
	//模板标识  1-动态，0-静态
	public static final String DSFLAG = "DSFLAG";
	//模板状态 0-无效，1-有效
	public static final String TM_STATE = "TM_STATE";
	//添加时间
	public static final String ADDTIME = "ADDTIME";
	//是否审核 0：无需审核，-1：未审核，1：同意，2：拒绝
	public static final String ISPASS = "ISPASS";
	//模板类型： 3:短信模板，4：彩信模板，5-富信模板
	public static final String TMP_TYPE = "TMP_TYPE";
	//业务编码
	public static final String BIZ_CODE = "BIZ_CODE";
	//企业编码
	public static final String CORP_CODE = "CORP_CODE";
	//彩信平台模板ID，值为0时表示未上传
	public static final String SP_TEMPLID = "SP_TEMPLID";
	//网关审核状态，（－1.未审批；0.无需审批；1.审批通过；2.审批未通过）
	public static final String AUDITSTATUS = "AUDITSTATUS";
	//关联彩信模板表MMS_TEMPLATE的Id字段
	public static final String MMS_TMPLID = "MMS_TMPLID";
	//网关彩信模板状态（0：正常，可用	1：锁定，暂时不可用	2：永久锁定，永远不可用）
	public static final String TMPLSTATUS = "TMPLSTATUS";
	//参数个数
	public static final String PARAMCNT = "PARAMCNT";
	//提交状态 	0：未提交	1：提交成功	2：提交失败	3；提交中
	public static final String SUBMITSTATUS = "SUBMITSTATUS";
	//EMP系统模块ID 
	public static final String EMP_TEMPLID = "EMP_TEMPLID";
	//错误编码
	public static final String ERROR_CODE = "ERROR_CODE";
	//自定义模板编码
	public static final String TM_CODE = "TM_CODE";
	//档位
	public static final String DEGREE = "DEGREE";
	//档位大小
	public static final String DEGREE_SIZE = "DEGREE_SIZE";
	//行业ID 关联 LF_INDUSTRY_USE 表中的ID
	public static final String INDUSTRYID = "INDUSTRYID";
	//用途ID 关联 LF_INDUSTRY_USE 表中的ID
	public static final String USEID = "USEID";
	
	//是否是公共模板 0为不是 1为是
	public static final String ISPUBLIC = "ISPUBLIC";
	
	//模板使用次数
	public static final String USECOUNT = "USECOUNT";
	
	//参数JSON串
	public static final String EXLJSON = "EXLJSON";
	
	//是否已设置为快捷模板(0:未设置，1:已设置)
	public static final String ISSHORTTEMP = "ISSHORTTEMP";

	//模板版本
	public static final String VER = "VER";
	
	public static final String SEQUENCE = "S_LF_TEMPLATE";
	protected final static Map<String, String> columns = new LinkedHashMap<String, String>();

	static {
		columns.put("LfTemplate", TABLE_NAME);
		columns.put("tableId", TM_ID);
		columns.put("tmid",TM_ID);
		columns.put("user_id", USER_ID);
		columns.put("tmname",TM_NAME );
		columns.put("tmmsg",TM_MSG );
		columns.put("dsflag",DSFLAG );
		columns.put("tmstate",TM_STATE);
		columns.put("addtime",ADDTIME );
		columns.put("ispass", ISPASS);
		columns.put("tmptype",TMP_TYPE );
		columns.put("bizcode", BIZ_CODE);
		columns.put("corpcode",CORP_CODE );
		columns.put("sptemplid",SP_TEMPLID );
		columns.put("auditstatus",AUDITSTATUS );
		columns.put("mmstmplid",MMS_TMPLID );
		columns.put("tmplstatus",TMPLSTATUS );
		columns.put("paramcnt",PARAMCNT );
		columns.put("submitstatus",SUBMITSTATUS );
		columns.put("emptemplid", EMP_TEMPLID);
		columns.put("errorcode",ERROR_CODE );
		columns.put("tmcode",TM_CODE );
		columns.put("degree",DEGREE );
		columns.put("degreesize",DEGREE_SIZE );
		columns.put("industryid",INDUSTRYID );
		columns.put("useid",USEID);
		columns.put("ispublic",ISPUBLIC);
		columns.put("usecount", USECOUNT);
		columns.put("exljson", EXLJSON);
		columns.put("isShortTemp", ISSHORTTEMP);
		columns.put("ver", VER);
	}

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM() {
		return columns;
	}

}
