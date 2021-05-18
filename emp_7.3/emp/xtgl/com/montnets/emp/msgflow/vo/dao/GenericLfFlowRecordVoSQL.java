package com.montnets.emp.msgflow.vo.dao;

import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.vo.LfFlowRecordVo;
import com.montnets.emp.table.approveflow.TableLfFlowRecord;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;

/**
 * 
 * @project sinolife
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-13 上午09:56:15
 * @description
 */
public class GenericLfFlowRecordVoSQL
{
	/**
	 * 组装查询语句
	 * @return
	 */
	public static String getFieldSql()
	{
		//拼接sql
		String sql = new StringBuffer("select flowrecord.").append(TableLfFlowRecord.FR_ID)
				.append(",flowrecord.").append(TableLfFlowRecord.MT_ID)
				.append(",flowrecord.").append(TableLfFlowRecord.F_ID)
				.append(",flowrecord.").append(TableLfFlowRecord.REVIEW_TYPE)
				.append(",flowrecord.").append(TableLfFlowRecord.R_TIME)
				.append(",flowrecord.").append(TableLfFlowRecord.R_LEVEL)
				.append(",flowrecord.").append(TableLfFlowRecord.R_LEVELAMOUNT)
				.append(",flowrecord.").append(TableLfFlowRecord.R_CONTENT)
				.append(",flowrecord.").append(TableLfFlowRecord.R_STATE)
				.append(",flowrecord.").append(TableLfFlowRecord.COMMENTS)
				.append(",prerevisysuser.").append(TableLfSysuser.NAME)
				.append(" preReviName,revisysuser.").append(TableLfSysuser.NAME)
				.append(" reviName,sysuser.").append(TableLfSysuser.NAME)
				.append(",sysuser.").append(TableLfSysuser.USER_NAME)
				.append(",dep.").append(TableLfDep.DEP_NAME)
				.append(",mttask.").append(TableLfMttask.TIMER_STATUS)
				.append(",mttask.").append(TableLfMttask.TIMER_TIME)
				.append(",mttask.").append(TableLfMttask.TITLE)
				.append(",mttask.").append(TableLfMttask.TASK_NAME)
				.append(",mttask.").append(TableLfMttask.BMT_TYPE)
				.append(",mttask.").append(TableLfMttask.SP_USER)
				.append(",mttask.").append(TableLfMttask.SUBMIT_TIME)
				.append(",mttask.").append(TableLfMttask.MOBILE_URL)
				.append(",mttask.").append(TableLfMttask.MSG_TYPE)
				.append(",mttask.").append(TableLfMttask.EFF_COUNT)
				.append(",mttask.").append(TableLfMttask.MSG)
				.append(",mttask.").append(TableLfMttask.TMPL_PATH)
				.append(",userdata.").append(TableUserdata.STAFF_NAME)
				.append(",sysuser.").append(TableLfSysuser.USER_STATE).toString();
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
				TableLfMttask.TABLE_NAME).append(" mttask ").append(
				StaticValue.getWITHNOLOCK()).append(" on mttask.").append(
				TableLfMttask.MT_ID).append("=flowrecord.").append(
				TableLfFlowRecord.MT_ID).append(" inner join ").append(
				TableLfSysuser.TABLE_NAME).append(" sysuser ").append(
				StaticValue.getWITHNOLOCK()).append(" on sysuser.").append(
				TableLfSysuser.USER_ID).append("=mttask.").append(
				TableLfMttask.USER_ID).append(" inner join ").append(
				TableLfDep.TABLE_NAME).append(" dep ").append(
				StaticValue.getWITHNOLOCK()).append(" on dep.").append(
				TableLfDep.DEP_ID).append("=sysuser.").append(
				TableLfSysuser.DEP_ID).append(" left join ").append(
				TableUserdata.TABLE_NAME).append(" userdata ").append(
				StaticValue.getWITHNOLOCK()).append(" on mttask.").append(
				TableLfMttask.SP_USER).append("=userdata.").append(
				TableUserdata.USER_ID).toString();
		return sql;
	}

	/**
	 *  组装查询语句
	 * @param GL_UserID
	 * @return
	 */
	public static String getDominationSql(String GL_UserID)
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
						GL_UserID).append(" and mttask.").append(
						TableLfMttask.SUB_STATE).append(
						"=2 and (preflowrecord.").append(
						TableLfFlowRecord.MT_ID).append(
						" is null or (flowrecord.").append(
						TableLfFlowRecord.MT_ID).append("=preflowrecord.")
				.append(TableLfFlowRecord.MT_ID).append(" and preflowrecord.")
				.append(TableLfFlowRecord.R_STATE).append("=1))").toString();
		return sql;
	}

	/**
	 *  组装查询语句
	 * @param LfFlowRecordVo
	 * @return
	 */
	public static String getConditionSql(LfFlowRecordVo lfFlowRecordVo)
	{
		StringBuffer conditionSql = new StringBuffer();
		//查询条件---发送账号
		if (lfFlowRecordVo.getSpUser() != null
				&& !"".equals(lfFlowRecordVo.getSpUser()))
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.SP_USER)
					.append(" like '%").append(lfFlowRecordVo.getSpUser())
					.append("%'");
		}
		//查询条件---标题
		if (lfFlowRecordVo.getTitle() != null
				&& !"".equals(lfFlowRecordVo.getTitle()))
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.TITLE)
					.append(" like '%").append(lfFlowRecordVo.getTitle())
					.append("%'");
		}
		//查询条件----任务名称
		if (lfFlowRecordVo.getTaskName() != null
				&& !"".equals(lfFlowRecordVo.getTaskName()))
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.TASK_NAME)
					.append(" like '%").append(lfFlowRecordVo.getTaskName())
					.append("%'");
		}

		if (lfFlowRecordVo.getBmtType() != null)
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.BMT_TYPE)
					.append("=").append(lfFlowRecordVo.getBmtType());
		}
		//查询条件----用户名称
		if (lfFlowRecordVo.getName() != null
				&& !"".equals(lfFlowRecordVo.getName()))
		{
			conditionSql.append(" and sysuser.").append(TableLfSysuser.NAME)
					.append("='").append(lfFlowRecordVo.getName()).append("'");
		}
		//查询条件---username
		if (lfFlowRecordVo.getUserName() != null
				&& !"".equals(lfFlowRecordVo.getUserName()))
		{
			conditionSql.append(" and sysuser.").append(
					TableLfSysuser.USER_NAME).append("='").append(
					lfFlowRecordVo.getUserName()).append("'");
		}
		//查询条件----审批状态
		if (lfFlowRecordVo.getRState() != null)
		{
			conditionSql.append(" and flowrecord.").append(
					TableLfFlowRecord.R_STATE).append("=").append(
					lfFlowRecordVo.getRState());
		}

		if (lfFlowRecordVo.getReviName() != null
				&& !"".equals(lfFlowRecordVo.getReviName()))
		{
			conditionSql.append(" and flowrecord.").append(
					TableLfFlowRecord.R_CONTENT).append(" like '%").append(
					lfFlowRecordVo.getReviName()).append("%'");
		}

		//做数据库兼容
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			//ORACLE
			if (lfFlowRecordVo.getStartSubmitTime() != null
					&& !"".equals(lfFlowRecordVo.getStartSubmitTime()))
			{
				conditionSql.append(" and mttask.").append(
						TableLfMttask.SUBMIT_TIME).append(">=to_date('").append(lfFlowRecordVo.getStartSubmitTime()+"','yyyy-MM-dd HH24:mi:ss')");
			}
			if (lfFlowRecordVo.getEndSubmitTime() != null
					&& !"".equals(lfFlowRecordVo.getEndSubmitTime()))
			{
				conditionSql.append(" and mttask.").append(
						TableLfMttask.SUBMIT_TIME).append("<=to_date('").append(lfFlowRecordVo.getEndSubmitTime()+"','yyyy-MM-dd HH24:mi:ss')");
			}
		}
		else
		{
			if (lfFlowRecordVo.getStartSubmitTime() != null
					&& !"".equals(lfFlowRecordVo.getStartSubmitTime()))
			{
				conditionSql.append(" and mttask.").append(
						TableLfMttask.SUBMIT_TIME).append(">='").append(lfFlowRecordVo.getStartSubmitTime()).append("'");
			}
			if (lfFlowRecordVo.getEndSubmitTime() != null
					&& !"".equals(lfFlowRecordVo.getEndSubmitTime()))
			{
				conditionSql.append(" and mttask.").append(
						TableLfMttask.SUBMIT_TIME).append("<='").append(lfFlowRecordVo.getEndSubmitTime()).append("'");
			}
		}

		if (lfFlowRecordVo.getFrId() != null)
		{
			conditionSql.append(" and flowrecord.").append(
					TableLfFlowRecord.FR_ID).append("=").append(
					lfFlowRecordVo.getFrId());
		}

		if (lfFlowRecordVo.getReviewType() != null)
		{
			//查短信与彩信模板
			if (lfFlowRecordVo.getReviewType() == -1){
					conditionSql.append(" and flowrecord.").append(
							TableLfFlowRecord.REVIEW_TYPE).append(" in (3,4) ");
			//查短信与彩信、移动财务
			} 
			if (lfFlowRecordVo.getReviewType() == 0) {
					conditionSql.append(" and flowrecord.").append(
							TableLfFlowRecord.REVIEW_TYPE).append(" in (1,2,5) ");
			}
			//根据条件查询
			if(lfFlowRecordVo.getReviewType() != -1 && lfFlowRecordVo.getReviewType() != 0){
				conditionSql.append(" and flowrecord.").append(
						TableLfFlowRecord.REVIEW_TYPE).append("=").append(
						lfFlowRecordVo.getReviewType());
			}

		}

		String sql = conditionSql.toString();
		return sql;
	}

	/**
	 *  组装查询语句
	 * @return
	 */
	public static String getOrderBySql()
	{
		String sql = new StringBuffer(" order by mttask.").append(
				TableLfMttask.MT_ID).append(" desc").toString();
		return sql;
	}

	/**
	 *  组装查询语句
	 * @param lfFlowRecordVo
	 * @return
	 */
	public static List<String> getTimeCondition(LfFlowRecordVo lfFlowRecordVo)
	{
		List<String> timeList = new ArrayList<String>();
		if (lfFlowRecordVo.getStartSubmitTime() != null
				&& !"".equals(lfFlowRecordVo.getStartSubmitTime()))
		{
			timeList.add(lfFlowRecordVo.getStartSubmitTime());
		}
		if (lfFlowRecordVo.getEndSubmitTime() != null
				&& !"".equals(lfFlowRecordVo.getEndSubmitTime()))
		{
			timeList.add(lfFlowRecordVo.getEndSubmitTime());
		}
		return timeList;
	}
}
