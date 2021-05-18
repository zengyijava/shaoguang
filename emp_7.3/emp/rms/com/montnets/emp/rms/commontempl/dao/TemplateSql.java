package com.montnets.emp.rms.commontempl.dao;

import com.montnets.emp.rms.commontempl.table.TableLfTemplate;
import com.montnets.emp.rms.vo.LfTemplateVo;
import com.montnets.emp.util.StringUtils;

public class TemplateSql {
	//查询字段sql
	public static String getFiledSql(){ 
		StringBuffer filedSql = new StringBuffer("SELECT ");
		filedSql
		.append("NULL AS ID").append(",")
		.append("NULL AS SHARE_TYPE").append(",")
		.append("B.NAME").append(",")
		.append("B.USER_NAME").append(",")
		.append("NULL AS USER_STATE").append(",")
		.append("C.DEP_NAME").append(",")
		.append("A.").append(TableLfTemplate.TM_ID).append(",")
		.append("A.").append(TableLfTemplate.USER_ID).append(",")
		.append("A.").append(TableLfTemplate.BIZ_CODE).append(",")
		.append("A.").append(TableLfTemplate.CORP_CODE).append(",")
		.append("A.").append(TableLfTemplate.DEGREE).append(",")
		.append("A.").append(TableLfTemplate.DEGREE_SIZE).append(",")
		.append("A.").append(TableLfTemplate.DSFLAG).append(",")
		.append("A.").append(TableLfTemplate.EMP_TEMPLID).append(",")
		.append("A.").append(TableLfTemplate.ERROR_CODE).append(",")
		.append("A.").append(TableLfTemplate.INDUSTRYID).append(",")
		.append("A.").append(TableLfTemplate.USEID).append(",")
		.append("A.").append(TableLfTemplate.ISPUBLIC).append(",")
		.append("A.").append(TableLfTemplate.ISPASS).append(",")
		.append("A.").append(TableLfTemplate.MMS_TMPLID).append(",")
		.append("A.").append(TableLfTemplate.PARAMCNT).append(",")
		.append("A.").append(TableLfTemplate.ADDTIME).append(",")
		.append("A.").append(TableLfTemplate.AUDITSTATUS).append(",")
		.append("A.").append(TableLfTemplate.SP_TEMPLID).append(",")
		.append("A.").append(TableLfTemplate.SUBMITSTATUS).append(",")
		.append("A.").append(TableLfTemplate.TM_CODE).append(",")
		.append("A.").append(TableLfTemplate.TM_MSG).append(",")
		.append("A.").append(TableLfTemplate.TM_NAME).append(",")
		.append("A.").append(TableLfTemplate.TM_STATE).append(",")
		.append("A.").append(TableLfTemplate.TMP_TYPE).append(",")
		.append("A.").append(TableLfTemplate.USECOUNT).append(",")
		.append("A.").append(TableLfTemplate.ISSHORTTEMP).append(",")
		/*.append("A.").append(TableLfTemplate.TMPLSTATUS)*/
		.append("A.").append(TableLfTemplate.VER);
		return filedSql.toString();
	}

	//表名sql
	public static String getTableSql(){
		StringBuffer tableSql = new StringBuffer(" FROM ");
		tableSql.append(TableLfTemplate.TABLE_NAME).append(" A ")
		.append("LEFT JOIN LF_SYSUSER  B ON A.USER_ID = B.USER_ID ")
		.append("LEFT JOIN LF_DEP C ON B.DEP_ID = C.DEP_ID ");
		return tableSql.toString();
		
	}
	
	//查询条件sql
	
	public static String getConditionSql(LfTemplateVo lfTemplateVo,String key){
		StringBuilder conditionSql = new StringBuilder(" WHERE 1=1 ");
		//富信发送页面查询的时候需要加上模板状态(启用还是停用)的筛选条件
		if(lfTemplateVo.getTmState() != null){
			conditionSql
					.append(" AND ")
					.append(TableLfTemplate.TM_STATE)
					.append(" = ").append(lfTemplateVo.getTmState());
		}
		//富信发送页面查询的时候需要加上模板类型(静态还是动态)的筛选条件
		if(lfTemplateVo.getDsflag() != null){
			conditionSql
					.append(" AND ")
					.append(TableLfTemplate.DSFLAG)
					.append(" = ").append(lfTemplateVo.getDsflag());
		}
		//审核状态
		if(lfTemplateVo.getAuditstatus() != null){
			conditionSql
			.append(" AND ")
			.append(TableLfTemplate.AUDITSTATUS)
			.append(" = ").append(lfTemplateVo.getAuditstatus());
			
		}
		
		//是否公共模板
		if(lfTemplateVo.getIsPublic()!= null){
			conditionSql
			.append(" AND ")
			.append(TableLfTemplate.ISPUBLIC)
			.append(" = ").append(lfTemplateVo.getIsPublic());
			
		}
		//模板类型-短信、彩信、富信
		if(!StringUtils.IsNullOrEmpty(String.valueOf(lfTemplateVo.getTmpType()))){
			conditionSql
			.append(" AND ")
			.append(TableLfTemplate.TMP_TYPE)
			.append(" = ").append(lfTemplateVo.getTmpType());
			
		}
		
		//行业ID
		if(lfTemplateVo.getIndustryid()!= null && -1!= lfTemplateVo.getIndustryid()){//-1为全部
				conditionSql
				.append(" AND ")
				.append(TableLfTemplate.INDUSTRYID)
				.append(" = ")
				.append(lfTemplateVo.getIndustryid());
		}
		//用途ID
		if(lfTemplateVo.getUseid()!= null && -2 != lfTemplateVo.getUseid()){//-2为全部
				conditionSql
				.append(" AND ")
				.append(TableLfTemplate.USEID)
				.append(" = ")
				.append(lfTemplateVo.getUseid());
			
		}

		//模板名字
		if(lfTemplateVo.getTmName() != null){
			conditionSql
					.append(" AND ")
					.append(TableLfTemplate.TM_NAME)
					.append(" like '%")
					.append(lfTemplateVo.getTmName().trim()).append("%'");
		}
		//模板ID
		if(lfTemplateVo.getSptemplid() != null){
			conditionSql
					.append(" AND ")
					.append(TableLfTemplate.SP_TEMPLID)
					.append(" like '%")
					.append(lfTemplateVo.getSptemplid()).append("%'");
		}

		if(!StringUtils.IsNullOrEmpty(key)){
			//模板名称 /模板ID
			conditionSql
			.append(" AND (")
			.append(TableLfTemplate.SP_TEMPLID)
			.append(" LIKE '%")
			.append(key.trim()).append("%'")
			.append(" OR ")
			.append(TableLfTemplate.TM_NAME)
			.append(" LIKE '%").append(key.trim()).append("%')");
		}
		
		
		return conditionSql.toString();
		
	}
	/**
	 * 排序sql
	 * @return
	 */
	public static String getOrderSql(){
		StringBuffer orderSql = new StringBuffer(" ORDER BY ");
		orderSql.append("USECOUNT DESC, ADDTIME DESC ");
		return orderSql.toString();
	}
}
