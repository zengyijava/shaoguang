package com.montnets.emp.tempflow.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.table.approveflow.TableLfFlowRecord;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.template.TableLfTemplate;
import com.montnets.emp.tempflow.vo.LfFlowRecordTemplateVo;

/**
 * 
 * @project montnets_dao
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-16 下午02:13:35
 * @description
 */
public class GenericLfFlowRecordTemplateVoSQL
{
	/**
	 * 组装查询语句
	 * @return
	 */
	public static String getFieldSql()
	{
		String sql = new StringBuffer("select flowrecord.").append(
				TableLfFlowRecord.FR_ID).append(",flowrecord.").append(
				TableLfFlowRecord.MT_ID).append(",flowrecord.").append(
				TableLfFlowRecord.F_ID).append(",flowrecord.").append(
				TableLfFlowRecord.R_TIME).append(",flowrecord.").append(
				TableLfFlowRecord.R_LEVEL).append(",flowrecord.").append(
				TableLfFlowRecord.REVIEW_TYPE).append(",flowrecord.").append(
				TableLfFlowRecord.R_LEVELAMOUNT).append(",flowrecord.").append(
				TableLfFlowRecord.R_CONTENT).append(",flowrecord.").append(
				TableLfFlowRecord.R_STATE).append(",flowrecord.").append(
				TableLfFlowRecord.COMMENTS).append(",prerevisysuser.").append(
				TableLfSysuser.NAME).append(" preReviName,revisysuser.")
				.append(TableLfSysuser.NAME).append(" reviName,sysuser.")
				.append(TableLfSysuser.NAME).append(",sysuser.").append(
						TableLfSysuser.USER_ID).append(",sysuser.").append(
						TableLfSysuser.USER_NAME).append(",dep.").append(
						TableLfDep.DEP_NAME).append(",lftemplate.").append(
						TableLfTemplate.TM_NAME).append(",lftemplate.").append(
						TableLfTemplate.DS_FLAG).append(",lftemplate.").append(
						TableLfTemplate.ADD_TIME).append(",lftemplate.")
				.append(TableLfTemplate.TM_MSG).append(",lftemplate.").append(
						TableLfTemplate.TM_STATE).append(",lftemplate.").append(TableLfTemplate.PARAMCNT).toString();
		//返回字符串
		return sql;
	}

	/**
	 * 组装查询语句
	 * @return
	 */
	public static String getTableSql()
	{
		//拼接sql
		String sql = new StringBuffer(" from ").append(
				TableLfFlowRecord.TABLE_NAME).append(" flowrecord ").append(
				StaticValue.getWITHNOLOCK()).append(" left join ").append(
				TableLfSysuser.TABLE_NAME).append(" prerevisysuser ").append(
				StaticValue.getWITHNOLOCK()).append(" on flowrecord.").append(
				TableLfFlowRecord.PRE_RV).append("=prerevisysuser.").append(
				TableLfSysuser.USER_ID).append(" inner join ").append(
				TableLfSysuser.TABLE_NAME).append(" revisysuser ").append(
				StaticValue.getWITHNOLOCK()).append(" on flowrecord.").append(
				TableLfFlowRecord.REVIEWER).append("=revisysuser.").append(
				TableLfSysuser.USER_ID).append(" inner join ").append(
				TableLfTemplate.TABLE_NAME).append(" lftemplate ").append(
				StaticValue.getWITHNOLOCK()).append(" on lftemplate.").append(
				TableLfTemplate.TM_ID).append("=flowrecord.").append(
				TableLfFlowRecord.MT_ID).append(" inner join ").append(
				TableLfSysuser.TABLE_NAME).append(" sysuser ").append(
				StaticValue.getWITHNOLOCK()).append(" on sysuser.").append(
				TableLfSysuser.USER_ID).append("=lftemplate.").append(
				TableLfTemplate.USER_ID).append(" inner join ").append(
				TableLfDep.TABLE_NAME).append(" dep ").append(
				StaticValue.getWITHNOLOCK()).append(" on dep.").append(
				TableLfDep.DEP_ID).append("=sysuser.").append(
				TableLfSysuser.DEP_ID).toString();
		//返回字符串
		return sql;
	}

	/**
	 * 组装查询语句
	 * @param loginUserID
	 * @return
	 */
	public static String getDominationSql(Long loginUserID)
	{
		//拼接sql
		String sql = new StringBuffer(" left join ").append(
				TableLfFlowRecord.TABLE_NAME).append(
				" preflowrecord ").append(StaticValue.getWITHNOLOCK()).append(" on preflowrecord.").append(
				TableLfFlowRecord.REVIEWER).append("=flowrecord.").append(
				TableLfFlowRecord.PRE_RV).append(" and preflowrecord.").append(
				TableLfFlowRecord.REVIEW_TYPE).append("=flowrecord.").append(
				TableLfFlowRecord.REVIEW_TYPE).append(" where flowrecord.")
				.append(TableLfFlowRecord.REVIEWER).append("=").append(
						loginUserID).append(" and (preflowrecord.").append(
						TableLfFlowRecord.MT_ID).append(
						" is null or (flowrecord.").append(
						TableLfFlowRecord.MT_ID).append("=preflowrecord.")
				.append(TableLfFlowRecord.MT_ID).append(" and preflowrecord.")
				.append(TableLfFlowRecord.R_STATE).append("=1))").toString();
		//返回字符串
		return sql;
	}

	/**
	 * 组装过滤条件
	 * @param// LfFlowRecordVo
	 * @return
	 */
	public static String getConditionSql(
			LfFlowRecordTemplateVo lfFlowRecordTemplateVo)
	{
		StringBuffer conditionSql = new StringBuffer();

		//创建人id
		if (lfFlowRecordTemplateVo.getUserId() != null)
		{
			conditionSql.append(" and sysuser.").append(TableLfSysuser.USER_ID)
					.append(" = ").append(lfFlowRecordTemplateVo.getUserId())
					.append("");
		}

		//添加时间（起始）
		if (lfFlowRecordTemplateVo.getStartAddTime() != null
				&& !"".equals(lfFlowRecordTemplateVo.getStartAddTime()))
		{
			//数据库兼容处理
			if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
			{
				//oracle数据库
				conditionSql.append(" and lftemplate.").append(
						TableLfTemplate.ADD_TIME).append(">=").append(
						"to_date('").append(
						lfFlowRecordTemplateVo.getStartAddTime()).append(
						"','yyyy-MM-dd HH24:mi:ss')");
			} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				//sqlserver数据库
				conditionSql.append(" and lftemplate.").append(
						TableLfTemplate.ADD_TIME).append(">=").append("'")
						.append(lfFlowRecordTemplateVo.getStartAddTime())
						.append("'");
			}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
				//db2数据库
				conditionSql.append(" and lftemplate.").append(
						TableLfTemplate.ADD_TIME).append(">=").append("'")
						.append(lfFlowRecordTemplateVo.getStartAddTime())
						.append("'");
			}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
				//mysql数据库
				conditionSql.append(" and lftemplate.").append(
						TableLfTemplate.ADD_TIME).append(">=").append("'")
						.append(lfFlowRecordTemplateVo.getStartAddTime())
						.append("'");
			}

		}

		//添加时间（结束）
		if (lfFlowRecordTemplateVo.getEndAddTime() != null
				&& !"".equals(lfFlowRecordTemplateVo.getEndAddTime()))
		{
			//数据库兼容处理
			if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
			{
				//oracle数据库
				conditionSql.append(" and lftemplate.").append(
						TableLfTemplate.ADD_TIME).append("<=").append(
						"to_date('").append(
						lfFlowRecordTemplateVo.getEndAddTime()).append(
						"','yyyy-MM-dd HH24:mi:ss')");
			} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				//sqlserver数据库
				conditionSql.append(" and lftemplate.").append(
						TableLfTemplate.ADD_TIME).append("<=").append("'")
						.append(lfFlowRecordTemplateVo.getEndAddTime()).append(
								"'");
			}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
				//db2数据库
				conditionSql.append(" and lftemplate.").append(
						TableLfTemplate.ADD_TIME).append("<=").append("'")
						.append(lfFlowRecordTemplateVo.getEndAddTime()).append(
								"'");
			}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
				//db2数据库
				conditionSql.append(" and lftemplate.").append(
						TableLfTemplate.ADD_TIME).append("<=").append("'")
						.append(lfFlowRecordTemplateVo.getEndAddTime()).append(
								"'");
			}

		}
		//查询条件----用户名称
		if (lfFlowRecordTemplateVo.getName() != null
				&& !"".equals(lfFlowRecordTemplateVo.getName()))
		{
			conditionSql.append(" and sysuser.").append(TableLfSysuser.NAME)
					.append("='").append(lfFlowRecordTemplateVo.getName())
					.append("'");
		}

		//审批状态
		if (lfFlowRecordTemplateVo.getRState() != null)
		{
			conditionSql.append(" and flowrecord.").append(
					TableLfFlowRecord.R_STATE).append("=").append(
					lfFlowRecordTemplateVo.getRState());
		}

		if (lfFlowRecordTemplateVo.getReviName() != null
				&& !"".equals(lfFlowRecordTemplateVo.getReviName()))
		{
			conditionSql.append(" and flowrecord.").append(
					TableLfFlowRecord.R_CONTENT).append(" like '%").append(
					lfFlowRecordTemplateVo.getReviName()).append("%'");
		}

		//流程id
		if (lfFlowRecordTemplateVo.getFrId() != null)
		{
			conditionSql.append(" and flowrecord.").append(
					TableLfFlowRecord.FR_ID).append("=").append(
					lfFlowRecordTemplateVo.getFrId());
		}

		//类型
		if (lfFlowRecordTemplateVo.getReviewType() != null)
		{
			//（短信与彩信的所有模板）
			if(lfFlowRecordTemplateVo.getReviewType()+1 == 0){
				conditionSql.append(" and flowrecord.").append(
						TableLfFlowRecord.REVIEW_TYPE).append(" in (3,4)");
			//短信或彩信模板。
			}else if(lfFlowRecordTemplateVo.getReviewType()-3 == 0 || lfFlowRecordTemplateVo.getReviewType()-4 == 0){
				conditionSql.append(" and flowrecord.").append(
					TableLfFlowRecord.REVIEW_TYPE).append("=").append(
					lfFlowRecordTemplateVo.getReviewType());
			}
		}

		//模板名称
		if (lfFlowRecordTemplateVo.getTmName() != null
				&& !"".equals(lfFlowRecordTemplateVo.getTmName()))
		{
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TM_NAME).append(" like '%").append(
					lfFlowRecordTemplateVo.getTmName()).append("%'");
		}

		if (lfFlowRecordTemplateVo.getTmMsg() != null
				&& !"".equals(lfFlowRecordTemplateVo.getTmMsg()))
		{
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TM_MSG).append(" like '%").append(
					lfFlowRecordTemplateVo.getTmMsg()).append("%'");
		}

		if (lfFlowRecordTemplateVo.getDsflag() != null)
		{
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.DS_FLAG).append("=").append(
					lfFlowRecordTemplateVo.getDsflag());
		}

		//转换字符串
		String sql = conditionSql.toString();
		//返回字符串
		return sql;
	}

	/**
	 * 获取排序sql
	 * @return
	 */
	public static String getOrderBySql()
	{
		//排序
		String sql = new StringBuffer(" order by lftemplate.").append(
				TableLfTemplate.ADD_TIME).append(" desc").toString();
		return sql;
	}
}
