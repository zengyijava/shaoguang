package com.montnets.emp.mmstask.dao;

import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.vo.LfMttaskVo2;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;


/**
 * 群发历史群发任务查询
 * @project sinolife
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-13 上午10:14:31
 * @description
 */
public class LfMttaskVoSQL
{
	/**
	 * LfMttaskVoDAO
	 * 组装查询语句
	 * @return
	 */
	public static String getFieldSql()
	{
		//拼接sql
		String sql = new StringBuffer("select mttask.").append(
				TableLfMttask.MT_ID).append(",mttask.").append(
				TableLfMttask.USER_ID).append(",mttask.").append(
				TableLfMttask.TITLE).append(",mttask.").append(
				TableLfMttask.MSG_TYPE).append(",mttask.").append(
				TableLfMttask.SUBMIT_TIME).append(",mttask.").append(
				TableLfMttask.SUB_STATE).append(",mttask.").append(
				TableLfMttask.RE_STATE).append(",mttask.").append(
				TableLfMttask.SEND_STATE).append(",mttask.").append(
				TableLfMttask.SEND_LEVEL).append(",mttask.").append(
				TableLfMttask.BMT_TYPE).append(",mttask.").append(
				TableLfMttask.ERROR_CODES).append(",mttask.").append(
				TableLfMttask.SP_USER).append(",mttask.").append(
				TableLfMttask.MOBILE_URL).append(",mttask.").append(
				TableLfMttask.SUB_COUNT).append(",mttask.").append(
				TableLfMttask.EFF_COUNT).append(",mttask.").append(
				TableLfMttask.SUC_COUNT).append(",mttask.").append(
				TableLfMttask.FAI_COUNT).append(",mttask.").append(
				TableLfMttask.COMMENTS).append(",mttask.").append(
				TableLfMttask.MSG).append(",mttask.").append(
				TableLfMttask.SP_PASS).append(",mttask.").append(
				TableLfMttask.ISREPLY).append(",mttask.").append(
				TableLfMttask.SPNUMBER).append(",mttask.").append(
				TableLfMttask.SUBNO).append(",mttask.").append(
				TableLfMttask.ICOUNT).append(",mttask.").append(
				TableLfMttask.ICOUNT2).append(",mttask.").append(
				TableLfMttask.RFAIL2).append(",mttask.").append(
				TableLfMttask.TMPL_PATH).append(",mttask.").append(
				TableLfMttask.TASK_NAME).append(",mttask.").append(
				TableLfMttask.ISRETRY).append(",sysuser.").append(
				TableLfSysuser.USER_STATE).append(",sysuser.").append(
				TableLfSysuser.NAME).append(",sysuser.").append(
				TableLfSysuser.USER_NAME).append(",dep.").append(
				TableLfDep.DEP_NAME).append(",dep.").append(TableLfDep.DEP_ID)
				.append(",userdata.").append(TableUserdata.STAFF_NAME).append(
				",mttask.").append(TableLfMttask.TIMER_TIME).append(
				",mttask.").append(TableLfMttask.BUS_CODE).append(
				",mttask.").append(TableLfMttask.TIMER_STATUS).append(
				",mttask.").append(TableLfMttask.TASKID).append(
				",mttask.").append(TableLfMttask.MS_TYPE).toString();
		return sql;
	}
	
	

	
	/**
	 * LfMttaskVoDAO
	 * 组装查询语句
	 * @return
	 */
	public static String getTableSql()
	{
		String sql = new StringBuffer(" from ")
				.append(TableLfMttask.TABLE_NAME).append(" mttask ").append(
						StaticValue.getWITHNOLOCK()).append(" inner join ").append(
						TableLfSysuser.TABLE_NAME).append(" sysuser ").append(
						StaticValue.getWITHNOLOCK()).append(" on mttask.").append(
						TableLfMttask.USER_ID).append("=sysuser.").append(
						TableLfSysuser.USER_ID).append(" inner join ").append(
						TableLfDep.TABLE_NAME).append(" dep ").append(
						StaticValue.getWITHNOLOCK()).append(" on sysuser.").append(
						TableLfSysuser.DEP_ID).append("=dep.").append(
						TableLfDep.DEP_ID).append(" left join ").append(
						TableUserdata.TABLE_NAME).append(" userdata ").append(
						StaticValue.getWITHNOLOCK()).append(" on mttask.").append(
						TableLfMttask.SP_USER).append("=userdata.").append(
						TableUserdata.USER_ID).append(" and userdata.").append(TableUserdata.ACCOUNTTYPE).append("=2")/*.append(" inner join ").append(
						TableLfBusManager.TABLE_NAME).append(" busmanager ")
						.append(StaticValue.WITHNOLOCK).append(" on mttask.").append(
						TableLfMttask.BUS_CODE).append("=busmanager.").append(
						TableLfBusManager.BUS_CODE).append(" and (busmanager.").append(
						TableLfBusManager.CORP_CODE).append("=mttask.").append(TableLfMttask.CORP_CODE)
						.append(" or busmanager.").append(TableLfBusManager.CORP_CODE).append(" in ('0','1','2')").append(")")*/.toString();
		return sql;
	}
	/**
	 * LfMttaskVoDAO
	 * 组装查询语句
	 * @param GL_UserID
	 * @return
	 */
	public static String getDominationSql(String GL_UserID)
	{
		StringBuffer dominationSql = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
				.append(" where ").append(TableLfDomination.USER_ID)
				.append("=").append(GL_UserID);
		String sql = new StringBuffer(" where (sysuser.").append(
				TableLfSysuser.USER_ID).append("=").append(GL_UserID).append(
				" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(dominationSql).append("))").toString();
		return sql;
	}

	/**
	 * LfMttaskVoDAO
	 * 组装过滤条件
	 * @param lfMttaskVo
	 * @return
	 */
	public static String getConditionSql(LfMttaskVo2 lfMttaskVo)
	{
		StringBuffer conditionSql = new StringBuffer();
		//机构
		if (lfMttaskVo.getDepName() != null
				&& !"".equals(lfMttaskVo.getDepName()))
		{
			conditionSql.append(" and dep.").append(TableLfDep.DEP_NAME)
					.append("='").append(lfMttaskVo.getDepName()).append("'");
		}
		//操作员
		if (lfMttaskVo.getName() != null && !"".equals(lfMttaskVo.getName()))
		{
			conditionSql.append(" and sysuser.").append(TableLfSysuser.NAME)
					.append("='").append(lfMttaskVo.getName()).append("'");
		}
		if (lfMttaskVo.getBmtType() != null)
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.BMT_TYPE)
					.append("=").append(lfMttaskVo.getBmtType());
		}
		//提交状态
		if (lfMttaskVo.getSubState() != null)
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.SUB_STATE)
					.append("=").append(lfMttaskVo.getSubState());
		}
		//审批状态
		if (lfMttaskVo.getReState() != null)
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.RE_STATE)
					.append(" =").append(lfMttaskVo.getReState());
		}
		//主题
		if (lfMttaskVo.getTaskName() != null && !"".equals(lfMttaskVo.getTaskName()))
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.TASK_NAME)
					.append(" like '%").append(lfMttaskVo.getTaskName()).append(
							"%'");
		}
		//彩信主题
		if (lfMttaskVo.getTitle() != null && !"".equals(lfMttaskVo.getTitle()))
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.TITLE)
					.append(" like '%").append(lfMttaskVo.getTitle()).append(
							"%'");
		}
		//指定时间段，开始时间
		if (lfMttaskVo.getStartSubmitTime() != null
				&& !"".equals(lfMttaskVo.getStartSubmitTime()))
		{
			conditionSql.append(" and mttask.")
					.append(TableLfMttask.SUBMIT_TIME).append(">=?");
		}
		//指定时间段，结束时间
		if (lfMttaskVo.getEndSubmitTime() != null
				&& !"".equals(lfMttaskVo.getEndSubmitTime()))
		{
			conditionSql.append(" and mttask.")
					.append(TableLfMttask.SUBMIT_TIME).append("<=?");
		}
		
		//业务类型
		/*if (lfMttaskVo.getBusCode() != null
				&& !"".equals(lfMttaskVo.getBusCode()))
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.BUS_CODE)
					.append("='").append(lfMttaskVo.getBusCode()).append("'");
		}*/
		if (lfMttaskVo.getMsType() != null)
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.MS_TYPE)
					.append(" =").append(lfMttaskVo.getMsType());
		}
		//机构
		if (lfMttaskVo.getDepIds() != null
				&& !"".equals(lfMttaskVo.getDepIds()))
		{
			conditionSql.append(" and ").append(lfMttaskVo.getDepIds());
		}
		
		//操作员
		if (lfMttaskVo.getUserIds() != null
				&& !"".equals(lfMttaskVo.getUserIds()))
		{
			conditionSql.append(" and sysuser.").append(TableLfSysuser.USER_ID)
					.append(" in (").append(lfMttaskVo.getUserIds())
					.append(")");
		}
		if (lfMttaskVo.getSendstate() != null)
		{
			conditionSql.append(" and mttask.")
					.append(TableLfMttask.SEND_STATE).append(" in (").append(
							lfMttaskVo.getSendstate()).append(")");
		}
		if(lfMttaskVo.getTimerStatus() != null){
			conditionSql.append(" and mttask.")
			.append(TableLfMttask.TIMER_STATUS).append(" =").append(
					lfMttaskVo.getTimerStatus());
		}
        if(lfMttaskVo.getFlowID() != null){
            conditionSql.append(" and mttask.taskid")
                    .append(" in ( SELECT DISTINCT MT_ID FROM LF_FLOWRECORD WHERE F_ID = ").append(lfMttaskVo.getFlowID())
                    .append(" and INFO_TYPE = 2 ) ");
            //过滤掉已撤销的彩信任务
            conditionSql.append(" and mttask.sub_state <> 3 ");
        }
		String sql = conditionSql.toString();
		return sql;
	}

	/**
	 * 组装过滤条件
	 * @param lfMttaskVo
	 * @return
	 */
	public static List<String> getTimeCondition(LfMttaskVo2 lfMttaskVo)
	{
		List<String> timeList = new ArrayList<String>();
		if (lfMttaskVo.getStartSendTime() != null
				&& !"".equals(lfMttaskVo.getStartSendTime()))
		{
			timeList.add(lfMttaskVo.getStartSendTime());
		}
		if (lfMttaskVo.getEndSendTime() != null
				&& !"".equals(lfMttaskVo.getEndSendTime()))
		{
			timeList.add(lfMttaskVo.getEndSendTime());
		}
		if (lfMttaskVo.getStartSubmitTime() != null
				&& !"".equals(lfMttaskVo.getStartSubmitTime()))
		{
			timeList.add(lfMttaskVo.getStartSubmitTime());
		}
		if (lfMttaskVo.getEndSubmitTime() != null
				&& !"".equals(lfMttaskVo.getEndSubmitTime()))
		{
			timeList.add(lfMttaskVo.getEndSubmitTime());
		}
		return timeList;
	}

	/**
	 * 组装排序条件
	 * @return
	 */
	public static String getOrderBySql()
	{
		String sql = new StringBuffer(" order by mttask.").append(
				TableLfMttask.MT_ID).append(" desc").toString();
		return sql;
	}

}
