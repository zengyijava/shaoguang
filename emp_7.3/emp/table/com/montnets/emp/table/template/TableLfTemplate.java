package com.montnets.emp.table.template;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-21 下午02:19:08
 * @description
 */
public class TableLfTemplate
{
    //表名：短信模板
	public static final String TABLE_NAME = "LF_TEMPLATE";

	//模板ID
	public static final String TM_ID = "TM_ID";

	//操作员ID
	public static final String USER_ID = "USER_ID";

	//模板说明
	public static final String TM_NAME = "TM_NAME";

	//模板内容
	public static final String TM_MSG = "TM_MSG";

	//动态1/静态0
	public static final String DS_FLAG = "DSFLAG";

	//模板状态（0无效，1有效）
	public static final String TM_STATE = "TM_STATE";

	//添加时间
	public static final String ADD_TIME = "ADDTIME";

	//是否审核(无需审核-0，未审核-1，同意1，拒绝2)）
	public static final String ISPASS = "ISPASS";
	
	//模板（3-短信模板;4-彩信模板）
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
	
	//模板参数个数
	public static final String PARAMCNT = "PARAMCNT";
	
	//模板编码
	public static final String TM_CODE = "TM_CODE";
	//	提交状态 
	//	0：未提交
	//	1：提交成功
	//	2：提交失败
	//	3；提交中
	public static final String SUBMITSTATUS = "SUBMITSTATUS";
	//EMP系统模块ID
	public static final String EMP_TEMPLID = "EMP_TEMPLID";
	
	//错误编码
	public static final String ERROR_CODE = "ERROR_CODE";
	
	
	//序列
	public static final String SEQUENCE = "S_LF_TEMPLATE";

	//映射集合
	protected final static Map<String, String> columns = new HashMap<String, String>();


	//档位
	public static final  String DEGREE= "DEGREE";
	
	//容量
	public static final  String DEGREESIZE = "DEGREE_SIZE" ;
    
	//行业ID 关联 LF_INDUSTRY_USE 表中的ID
	public static final String INDUSTRYID = "INDUSTRYID";
	
	//用途ID 关联 LF_INDUSTRY_USE 表中的ID
	public static final String USEID = "USEID";
	
	//是否是公共模板 0为不是 1为是
	public static final String ISPUBLIC = "ISPUBLIC";
	
	//模板使用次数
	public static final String USECOUNT = "USECOUNT";
	
	//动态模板生成excel的表头json
	public static final String EXLJSON= "EXLJSON";

	// 富信模板版本
	public static final String VER= "VER";

	//富信发送接口中参数个数
	public static final String PARAMSNUM = "PARAMSNUM";

	//是否是快捷场景
	public static final String ISSHORTTEMP = "ISSHORTTEMP";

	//模板来源
	public static final String SOURCE = "SOURCE";

	//RCOS 同步来的模板是否公共素材
	public static final String ISMATERIAL = "ISMATERIAL";

	//RCOS 平台同步来的模板状态
	public static final String RCOSTMPSTATE = "RCOS_TMPSTATE";

	static
	{
		columns.put("LfTemplate", TABLE_NAME);
		columns.put("tableId", TM_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("tmid", TM_ID);
		columns.put("userId", USER_ID);
		columns.put("tmName", TM_NAME);
		columns.put("tmMsg", TM_MSG);
		columns.put("dsflag", DS_FLAG);
		columns.put("tmState", TM_STATE);
		columns.put("addtime", ADD_TIME);
		columns.put("isPass", ISPASS);
		columns.put("tmpType", TMP_TYPE);
		columns.put("bizCode", BIZ_CODE);
		columns.put("corpCode", CORP_CODE);
		columns.put("sptemplid", SP_TEMPLID);
		columns.put("auditstatus", AUDITSTATUS);
		columns.put("mmstmplid", MMS_TMPLID);
		columns.put("tmplstatus", TMPLSTATUS);
		columns.put("paramcnt", PARAMCNT);
		columns.put("submitstatus", SUBMITSTATUS);
		columns.put("emptemplid", EMP_TEMPLID);
		columns.put("errorcode", ERROR_CODE);
		columns.put("tmCode", TM_CODE);
		columns.put("degree", DEGREE);
		columns.put("degreeSize", DEGREESIZE);
		columns.put("industryid",INDUSTRYID );
		columns.put("useid",USEID);
		columns.put("isPublic",ISPUBLIC);
		columns.put("usecount", USECOUNT);
		columns.put("exlJson", EXLJSON); 
		columns.put("ver", VER);
		columns.put("paramsnum", PARAMSNUM);
		columns.put("isShortTemp", ISSHORTTEMP);
		columns.put("source", SOURCE);
		columns.put("isMaterial", ISMATERIAL);
		columns.put("rcosTmpState", RCOSTMPSTATE);
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
