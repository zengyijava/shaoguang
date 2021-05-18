package com.montnets.emp.appmage.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.LfMttaskVo;
import com.montnets.emp.table.report.TableMtDatareport;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sms.TableMtTask;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;


public class SmstaskSql {
	
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
				TableLfMttask.ISRETRY).append(",mttask.").append(
				TableLfMttask.TIMER_TIME).append(",mttask.").append(
				TableLfMttask.BUS_CODE).append(",mttask.").append(
				TableLfMttask.TIMER_STATUS).append(",mttask.").append(
				TableLfMttask.MS_TYPE).append(",mttask.").append(
				TableLfMttask.TASKID).append(",mttask.").append(
				TableLfMttask.TASKTYPE).append(",mttask.").append(
				TableLfMttask.BATCHID).toString();
		return sql;
	}
	/**
	 * 组装查询语句 
	 * @return
	 */
	public static String getmttaskFieldSql()
	{
		String sql = new StringBuffer("select mt.*").toString();
	   return sql;
	}
	/**
	 * 组装查询语句
	 * @return
	 */
	public static String getPartmttaskFieldSql()
	{
		   String sql =new StringBuffer("select mt.").append(TableMtTask.UNICOM).append(",mt.").append(TableMtTask.PHONE)
		   .append(",mt.").append(TableMtTask.MESSAGE).append(",mt.").append(TableMtTask.ERROR_CODE)
		   .append(",mt.").append(TableMtTask.PK_NUMBER).append(",mt.").append(TableMtTask.PK_TOTAL)
		   .toString();
		   return sql;
	}
	
	/**
	 * 详情查询时假如查两个表的sql拼接
	 * @return
	 */
	public static String getmttaskTableSqlTwo(String tableName)
	{
		//拼接sql
		   String sql =new StringBuffer("select ms.").append(TableMtTask.PHONE).append(",ms.").append(TableMtTask.TASK_ID)
		   .append(",ms.").append(TableMtTask.MESSAGE).append(",ms.").append(TableMtTask.ERROR_CODE)
		   .append(",ms.").append(TableMtTask.PK_NUMBER).append(",ms.").append(TableMtTask.PK_TOTAL)
		   .append(" from ").append(tableName).append(" ms ").append(StaticValue.getWITHNOLOCK()).append(" union all ")
		   .append("select ml.").append(TableMtTask.PHONE).append(",ml.").append(TableMtTask.TASK_ID)
		   .append(",ml.").append(TableMtTask.MESSAGE).append(",ml.").append(TableMtTask.ERROR_CODE)
		   .append(",ml.").append(TableMtTask.PK_NUMBER).append(",ml.").append(TableMtTask.PK_TOTAL)
		   .append(" from ").append("gw_mt_task_bak").append(" ml ").append(StaticValue.getWITHNOLOCK()).toString();
			return sql;
	}
	
	
	
	/**
	 * 详情查询时假如查两个表的sql拼接
	 * @return
	 */
	public static String getmttaskTableSqlTwo(String tableName,LinkedHashMap<String, String> conditionMap)
	{
		   StringBuffer sql =new StringBuffer("select ms.").append(TableMtTask.UNICOM).append(",ms.").append(TableMtTask.PHONE).append(",ms.").append(TableMtTask.TASK_ID)
		   .append(",ms.").append(TableMtTask.MESSAGE).append(",ms.").append(TableMtTask.ERROR_CODE)
		   .append(",ms.").append(TableMtTask.PK_NUMBER).append(",ms.").append(TableMtTask.PK_TOTAL)
		   .append(" from ").append(tableName).append(" ms ").append(StaticValue.getWITHNOLOCK());
		    sql.append(" where "+getmttaksConditionSql(conditionMap,"ms"));
		    sql.append(" union all ")
		   .append("select ml.").append(TableMtTask.UNICOM).append(",ml.").append(TableMtTask.PHONE).append(",ml.").append(TableMtTask.TASK_ID)
		   .append(",ml.").append(TableMtTask.MESSAGE).append(",ml.").append(TableMtTask.ERROR_CODE)
		   .append(",ml.").append(TableMtTask.PK_NUMBER).append(",ml.").append(TableMtTask.PK_TOTAL)
		   .append(" from ").append("gw_mt_task_bak").append(" ml ").append(StaticValue.getWITHNOLOCK()).toString();
		   
		   sql.append(" where "+getmttaksConditionSql(conditionMap,"ml"));
			return sql.toString();
	}
	/**
	 * 查询sql拼接
	 * @return
	 */
	public static String getSumIcountMtdatareportFieldSql()
	{
		String sql =new StringBuffer("select sum( mt.").append(TableMtDatareport.ICOUNT).append(") totalcount").toString();
		return sql;
	}
	
	/**
	 * 查询sql拼接
	 * @return
	 */
	public static String getSumIcountMtdatareportTableSql()
	{
		String sql = new StringBuffer(" from ").append(TableMtDatareport.TABLE_NAME).append(" mt ").append(StaticValue.getWITHNOLOCK()).toString();
		return sql;
	}

	/**
	 * 查询sql的where条件拼接
	 * @return
	 */
	public static String getSumIcountMtdatareportcConditionSql(LinkedHashMap<String, String> conditionMap)
	{
		StringBuffer conditionSql = new StringBuffer();
		if(conditionMap.containsKey("taskid"))
		{
			conditionSql.append(" where mt.").append(TableMtDatareport.TASK_ID)
			.append("=").append(conditionMap.get("taskid"));
		}else if(conditionMap.containsKey("batchid")){
			conditionSql.append(" where mt.").append(TableMtDatareport.BATCHID)
			.append("=").append(conditionMap.get("batchid"));
		}		
		if(conditionMap.containsKey("iymd"))
		{
			conditionSql.append(" and mt.").append(TableMtDatareport.IYMD).append("<=").append(conditionMap.get("iymd"));
			
		}
		
		return conditionSql.toString();
	}
	
	/**
	 * LfMttaskVoDAO
	 * 组装查询语句
	 * @return
	 */
	public static String getTableSql(String corpCode)
	{
//		String sql = new StringBuffer(" from ")
//				.append(TableLfMttask.TABLE_NAME).append(" mttask ").append(" inner join lf_sysuser sysuser on mttask.user_id=sysuser.user_id").toString();
		String sql = new StringBuffer(" from ")
		.append(TableLfMttask.TABLE_NAME).append(" mttask ").append(StaticValue.getWITHNOLOCK()).append(" where mttask.CORP_CODE = '").append(corpCode).append("' ").toString();
		return sql;
	}
	/**
	 * 组装查询语句
	 * @param tableName
	 * @return
	 */
	public static String getmttaskTableSql(String tableName)
	{
		String sql = new StringBuffer(" from ").append(tableName).append(" mt ").append(StaticValue.getWITHNOLOCK()).toString();
		return sql;
	}
	/**
	 * LfMttaskVoDAO
	 * 组装查询语句
	 * @param GL_UserID
	 * @return
	 */
	public static String getDominationSql2(String GL_UserID)
	{
//		StringBuffer dominationSql = new StringBuffer("select ").append(
//				TableLfDomination.DEP_ID).append(" from ").append(
//				TableLfDomination.TABLE_NAME).append(StaticValue.WITHNOLOCK)
//				.append(" where ").append(TableLfDomination.USER_ID)
//				.append("=").append(GL_UserID);
//		String sql = new StringBuffer(" where (sysuser.").append(
//				TableLfSysuser.USER_ID).append("=").append(GL_UserID).append(
//				" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (")
//				.append(dominationSql).append("))").toString();
		
		String sql = " and mttask.user_id="+GL_UserID;
		return sql;
	}
	
	public static String getDominationSql(String GL_UserID)
	{
		String sql = " and  mttask.user_id in " +
		"(select user_id from lf_sysuser where dep_id in (select dep_id from lf_domination where user_id="+GL_UserID+"))";
		return sql;
	}
	/**
	 * 组装过滤条件
	 * @param conditionMap
	 * @return
	 */
	public static String getmttaksConditionSql(LinkedHashMap<String, String> conditionMap)
	{
		StringBuffer conditionSql = new StringBuffer();
		if(conditionMap.containsKey("taskid"))
		{
			conditionSql.append(" where mt.").append(TableMtTask.TASK_ID)
			.append("=").append(conditionMap.get("taskid"));
		}else if(conditionMap.containsKey("batchid")){
			conditionSql.append(" where mt.").append(TableMtTask.BATCHID)
			.append("=").append(conditionMap.get("batchid"));
		}	
		else
		{
			conditionSql.append(" where 1=2");
			
		}
		if(conditionMap.containsKey("errorcode&like"))
		{
			conditionSql.append(" and (mt.").append(TableMtTask.ERROR_CODE).append(" like 'E1:%' or mt." )
				.append(TableMtTask.ERROR_CODE).append(" like 'E2:%' )");
			
		}
		if(conditionMap.containsKey("errorcode&not like"))
		{
			conditionSql.append(" and mt.").append(TableMtTask.ERROR_CODE).append(" not like 'E1:%' and mt.")
			.append(TableMtTask.ERROR_CODE).append(" not like 'E2:%' and mt.")
			.append(TableMtTask.ERROR_CODE).append(" not in ('DELIVRD' ,'0      ','       ')");
		}
		if(conditionMap.containsKey("errorcode&in"))
		{
			//发送成功
			conditionSql.append(" and mt.").append(TableMtTask.ERROR_CODE).append(" in ('DELIVRD' ,'0      ') ");
		}
		if(conditionMap.containsKey("errorcode"))
		{
			//状态未返
			conditionSql.append(" and mt.").append(TableMtTask.ERROR_CODE).append(" = '       ' ");
		}
		if(conditionMap.containsKey("phone&like"))
		{
			conditionSql.append(" and mt.").append(TableMtTask.PHONE).append(" like '%").append(conditionMap.get("phone&like")).append("%'");
		}
		if(conditionMap.containsKey("unicom"))
		{
			conditionSql.append(" and mt.").append(TableMtTask.UNICOM).append("=").append(conditionMap.get("unicom"));
		}
		
		return conditionSql.toString();
	}
	/**
	 * 组装过滤条件
	 * @param conditionMap
	 * @param tablename
	 * @return
	 */
	public static String getmttaksConditionSql(LinkedHashMap<String, String> conditionMap,String tablename)
	{
		StringBuffer conditionSql = new StringBuffer();
		if(conditionMap.containsKey("taskid"))
		{
			conditionSql.append(tablename+".").append(TableMtTask.TASK_ID)
			.append("=").append(conditionMap.get("taskid"));
		}else if(conditionMap.containsKey("batchid")){
			conditionSql.append(tablename+".").append(TableMtTask.BATCHID)
			.append("=").append(conditionMap.get("batchid"));
		}
		else
		{
			conditionSql.append(" 1=2");
			
		}
		if(conditionMap.containsKey("errorcode&like"))
		{
			conditionSql.append(" and (").append(tablename+".").append(TableMtTask.ERROR_CODE).append(" like 'E1:%' or " )
			.append(tablename+".").append(TableMtTask.ERROR_CODE).append(" like 'E2:%' )");
			
		}
		if(conditionMap.containsKey("errorcode&not like"))
		{
			conditionSql.append(" and ").append(tablename+".").append(TableMtTask.ERROR_CODE).append(" not like 'E1:%' and ")
			.append(tablename+".").append(TableMtTask.ERROR_CODE).append(" not like 'E2:%' and ")
			.append(tablename+".").append(TableMtTask.ERROR_CODE).append(" not in ('DELIVRD' ,'0      ','       ')");
		}
		
		if(conditionMap.containsKey("phone&like"))
		{
			conditionSql.append(" and ").append(tablename+".").append(TableMtTask.PHONE).append(" like '%").append(conditionMap.get("phone&like")).append("%'");
		}
		if(conditionMap.containsKey("unicom"))
		{
			conditionSql.append(" and ").append(tablename).append(".").append(TableMtTask.UNICOM).append("=").append(conditionMap.get("unicom"));
		}
		
		return conditionSql.toString();
	}

	/**
	 * LfMttaskVoDAO
	 * 组装过滤条件
	 * @param lfMttaskVo
	 * @return
	 */
	public static String getConditionSql(LfMttaskVo lfMttaskVo)
	{
		StringBuffer conditionSql = new StringBuffer();
		//只需查询短信账号的发送内容
		//conditionSql.append(" and userdata.").append(TableUserdata.ACCOUNTTYPE).append("=1");
		//sp账号
		if (lfMttaskVo.getSpUser() != null
				&& !"".equals(lfMttaskVo.getSpUser()))
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.SP_USER)
					.append(" = '").append(lfMttaskVo.getSpUser()).append("'");
		}
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
		//if (lfMttaskVo.getBmtType() != null && !"".equals(lfMttaskVo.getBmtType()))  //findbugs
		if (lfMttaskVo.getBmtType() != null)
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.BMT_TYPE)
					.append("=").append(lfMttaskVo.getBmtType());
		}
		//提交状态
		//if (lfMttaskVo.getSubState() != null && !"".equals(lfMttaskVo.getSubState()))  //findbugs
		if (lfMttaskVo.getSubState() != null)
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.SUB_STATE)
					.append("=").append(lfMttaskVo.getSubState());
		}
		//审批状态
		if (lfMttaskVo.getReStates() != null
				&& !"".equals(lfMttaskVo.getReStates()))
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.RE_STATE)
					.append(" in (").append(lfMttaskVo.getReStates()).append(
							")");
		}
		//主题
		if (lfMttaskVo.getTitle() != null && !"".equals(lfMttaskVo.getTitle()))
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.TITLE)
					.append(" like '%").append(lfMttaskVo.getTitle()).append(
							"%'");
		}
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		//指定时间段，开始时间
		if (lfMttaskVo.getStartSendTime() != null
				&& !"".equals(lfMttaskVo.getStartSendTime()))
		{
			conditionSql.append(" and mttask.")
					.append(TableLfMttask.TIMER_TIME).append(">=").append(genericDao.getTimeCondition(lfMttaskVo.getStartSendTime()));
		}
		//指定时间段，结束时间
		if (lfMttaskVo.getEndSendTime() != null
				&& !"".equals(lfMttaskVo.getEndSendTime()))
		{
			conditionSql.append(" and mttask.")
					.append(TableLfMttask.TIMER_TIME).append("<=").append(genericDao.getTimeCondition(lfMttaskVo.getEndSendTime()));
		}
		if (lfMttaskVo.getStartSubmitTime() != null
				&& !"".equals(lfMttaskVo.getStartSubmitTime()))
		{
			conditionSql.append(" and mttask.").append(
					TableLfMttask.SUBMIT_TIME).append(">=").append(genericDao.getTimeCondition(lfMttaskVo.getStartSubmitTime()));
		}
		if (lfMttaskVo.getEndSubmitTime() != null
				&& !"".equals(lfMttaskVo.getEndSubmitTime()))
		{
			conditionSql.append(" and mttask.").append(
					TableLfMttask.SUBMIT_TIME).append("<=").append(genericDao.getTimeCondition(lfMttaskVo.getEndSubmitTime()));
		}
		//业务类型
		if (lfMttaskVo.getBusCode() != null
				&& !"".equals(lfMttaskVo.getBusCode()))
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.BUS_CODE)
					.append("='").append(lfMttaskVo.getBusCode()).append("'");
		}
		if (lfMttaskVo.getMsTypes() != null
				&& !"".equals(lfMttaskVo.getMsTypes()))
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.MS_TYPE)
					.append(" in (").append(lfMttaskVo.getMsTypes())
					.append(")");
		}
		//机构
		if (lfMttaskVo.getDepIds() != null
				&& !"".equals(lfMttaskVo.getDepIds()))
		{
			conditionSql.append(" and mttask.user_id in (select user_id from lf_sysuser where ").append(lfMttaskVo.getDepIds()).append(")");
		}
		//操作员
		if (lfMttaskVo.getUserIds() != null
				&& !"".equals(lfMttaskVo.getUserIds()))
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.USER_ID)
					.append(" in (").append(lfMttaskVo.getUserIds())
					.append(")");
		}
		
		//操作员
		//if (lfMttaskVo.getUserId() != null && !"".equals(lfMttaskVo.getUserId()))  //findbugs
		if (lfMttaskVo.getUserId() != null) 
		{
			conditionSql.append(" and mttask.").append(TableLfMttask.USER_ID)
					.append("<>").append(lfMttaskVo.getUserId());
		}
		
		if (lfMttaskVo.getOverSendstate() != null
				&& !"".equals(lfMttaskVo.getOverSendstate()))
		{
			conditionSql.append(" and mttask.")
					.append(TableLfMttask.SEND_STATE).append(" in (").append(
							lfMttaskVo.getOverSendstate()).append(")");
		}
		
		if (lfMttaskVo.getCorpCode() != null
				&& !"".equals(lfMttaskVo.getCorpCode()))
		{
			conditionSql.append(" and mttask.")
					.append(TableLfMttask.CORP_CODE).append(" ='").append(
							lfMttaskVo.getCorpCode()).append("'");
		}
		
		if(lfMttaskVo.getTaskState() != null
				&& !"".equals(lfMttaskVo.getTaskState()))
		{
			//任务状态
			String taskState = lfMttaskVo.getTaskState();
			//待审批
			if("1".equals(taskState))
			{
				conditionSql.append(" and mttask.")
				.append(TableLfMttask.RE_STATE).append("=-1")
				.append(" and mttask.").append(TableLfMttask.SUB_STATE).append("=2");
			}
			//审批不通过
			else if("2".equals(taskState))
			{
				conditionSql.append(" and mttask.")
				.append(TableLfMttask.RE_STATE).append("=2")
				.append(" and mttask.").append(TableLfMttask.SUB_STATE).append("=2");
			}
			//待发送
			else if("3".equals(taskState))
			{
				conditionSql.append(" and mttask.")
				.append(TableLfMttask.RE_STATE).append(" in(0,1)")
				.append(" and mttask.").append(TableLfMttask.SEND_STATE).append("=0")
				.append(" and mttask.").append(TableLfMttask.SUB_STATE).append("=2");
			}
			//已撤销
			else if("4".equals(taskState))
			{
				conditionSql.append(" and mttask.")
				.append(TableLfMttask.SUB_STATE).append("=3");
			}
			//已发送
			else if("5".equals(taskState))
			{
				conditionSql.append(" and mttask.")
				.append(TableLfMttask.SEND_STATE).append(" in(1,2,3,4) ");
			}
			//已冻结
			else if("6".equals(taskState))
			{
				conditionSql.append(" and mttask.")
				.append(TableLfMttask.SUB_STATE).append("=4 ");
			}
			//超时未发送
			else if("7".equals(taskState))
			{
				conditionSql.append(" and mttask.")
				.append(TableLfMttask.SEND_STATE).append("=5 ");
			}
		}
		if(lfMttaskVo.getTaskType() != null
				&& !"".equals(String.valueOf(lfMttaskVo.getTaskType()))){
			if(lfMttaskVo.getTaskType()==1){
				conditionSql.append(" and mttask.")
				.append(TableLfMttask.TASKTYPE).append("=1 ");
			}else if(lfMttaskVo.getTaskType()==2){
				conditionSql.append(" and mttask.")
				.append(TableLfMttask.TASKTYPE).append("=2 ");
			}
		}
		//任务批次查询条件
		//if(lfMttaskVo.getTaskId()!=null&&!"".equals(lfMttaskVo.getTaskId())){  //findbugs
		if(lfMttaskVo.getTaskId()!=null){ 
			Long taskid=-2L;
			if(lfMttaskVo.getTaskId()==0L){
				taskid=-1L;
			}else{
				taskid=lfMttaskVo.getTaskId();
			}
			conditionSql.append(" and mttask.").append(TableLfMttask.TASKID)
			.append("=").append(taskid);
		}
		String sql = conditionSql.toString();
		return sql;
	}

	/**
	 * 组装过滤条件
	 * @param lfFlowRecordVo
	 * @return
	 */
	public static List<String> getTimeCondition(LfMttaskVo lfMttaskVo)
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
   /**
    * 组装查询语句
    * @param orderbyMap
    * @return
    */
	public static String getmttaskOrderSql(LinkedHashMap<String,String> orderbyMap)
	{
		StringBuffer sql = new StringBuffer("");
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			sql.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			Map.Entry<String, String> e = null;
			while (iter.hasNext())
			{
				e = iter.next();
				sql.append("mt.").append(e.getKey()).append(" ").append(
						e.getValue()).append(",");
			}
			
			sql.deleteCharAt(sql.lastIndexOf(","));
		}
		return sql.toString();
	}

}
