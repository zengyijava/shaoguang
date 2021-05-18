package com.montnets.emp.query.dao;

import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.inbox.dao.ReciveBoxDao;
import com.montnets.emp.query.vo.SystemMtTaskVo;
import com.montnets.emp.table.sms.TableMtTask;
import com.montnets.emp.table.sms.TableMtTask01_12;
/**
 * 系统下行实时记录查询
 * @project sinolife
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-14 下午04:20:59
 * @description
 */
public class GenericSystemMtTaskVoSQL {
	// SystemMtTaskVoDAO
	/**
	 * 系统下行实时记录查询列名
	 * SystemMtTaskVoDAO
	 */
	public static String getFieldSql() {
		StringBuffer sql = new StringBuffer("select mttask.").append(TableMtTask.ID)
						.append(",mttask.").append(TableMtTask.USER_ID).append(
						",mttask.").append(TableMtTask.SPGATE).append(
						",mttask.").append(TableMtTask.CPNO).append(",mttask.")
						.append(TableMtTask.PHONE).append(",mttask.").append(
						TableMtTask.TASK_ID).append(",mttask.").append(
						TableMtTask.SEND_STATUS).append(",mttask.").append(
						TableMtTask.ERROR_CODE).append(",mttask.").append(
						TableMtTask.SEND_TIME).append(",mttask.").append(
						TableMtTask.MESSAGE).append(",mttask.").append(
						TableMtTask.UNICOM).append(",mttask.").append(
						TableMtTask.RECV_TIME);/*.append(",bus.").append(
						TableLfBusManager.BUS_NAME)*/
		//if(StaticValue.DBTYPE != StaticValue.DB2_DBTYPE)
		//{
			sql.append(",mttask.").append(
						TableMtTask.SVRTYPE).append(",mttask.").append(
						TableMtTask.PK_NUMBER).append(",mttask.").append(
						TableMtTask.PK_TOTAL).append(",mttask.").append(
						TableMtTask.MSGFMT);
		//}
		return sql.toString();
	}

	/**
	 * 系统下行实时记录查询
	 * @return
	 */
	public static String getTableSql() {
		//李杨浩修改过的地方
		// String sql = new
		// StringBuffer(" from ").append(TableMtTask.TABLE_NAME)
		// .append(" mttask where").append(" mttask.").append(
		// TableMtTask.ERROR_CODE).append(" not like 'E%'")
		// .toString();
		String sql = new StringBuffer(" from ").append(TableMtTask.TABLE_NAME)
				.append(" mttask ").append(StaticValue.getWITHNOLOCK())/*.append(" left join ").append(TableLfBusManager.TABLE_NAME).append(" bus ")
				.append(StaticValue.getWITHNOLOCK()).append(" on bus.").append(TableLfBusManager.BUS_CODE)
				.append("=mttask.").append(TableMtTask.SVRTYPE)*/.append(" ").toString();
		return sql;
	}

	/**
	 * SystemMtTaskVoDAO
	 * 系统下行实时记录查询
	 * @param systemMtTaskVo
	 * @return
	 */
	public static String getConditionSql(SystemMtTaskVo systemMtTaskVo) 
	{
		StringBuffer conditionSql = new StringBuffer();
		//是否有where，默认为false即没where
		boolean hasWhere = false;
		//如果未绑定sp账号，且是多企业则查询结果为空
		if(systemMtTaskVo.getSpusers()==null && StaticValue.getCORPTYPE() == 1)
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" 1=2");
		}
        //sp账号
		if (systemMtTaskVo.getUserid() != null && !"".equals(systemMtTaskVo.getUserid())) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.").append(TableMtTask.USER_ID)
			.append("='").append(systemMtTaskVo.getUserid().trim())
			.append("'");
		}			
        //如果绑定了sp账号，则需要查询此范围内的记录
		if (systemMtTaskVo.getSpusers() != null && !"".equals(systemMtTaskVo.getSpusers())) 
		{
			String spusers=systemMtTaskVo.getSpusers();
			//sp账号个数
			int length=0;
			if(spusers.contains(",")){
					String[] useriday=spusers.split(",");
					length=useriday.length;
			}else{
				length=1;
			}
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			//如果个数小于50则用in
			if(length<50)
			{
				String insqlstr=new ReciveBoxDao().getSqlStr(spusers, "mttask."+TableMtTask.USER_ID);
				conditionSql.append(" (" + insqlstr + ") ");
			}
			else
			{
				conditionSql.append(" exists (select SPUSER from lf_sp_dep_bind "+StaticValue.getWITHNOLOCK()+" where SPUSER=mttask.USERID ");
				if(systemMtTaskVo.getCorpcode()!=null&&!"".equals(systemMtTaskVo.getCorpcode())){
					conditionSql.append(" and CORP_CODE= '").append(systemMtTaskVo.getCorpcode().trim()).append("' ");
				}
				conditionSql.append(") ");
			}
		}
        //通道号
		if (systemMtTaskVo.getSpgate() != null && !"".equals(systemMtTaskVo.getSpgate())) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.").append(TableMtTask.SPGATE)
			.append("='").append(systemMtTaskVo.getSpgate().trim())
			.append("'");
		}
		//运营商
		if (systemMtTaskVo.getUnicom() != null) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.").append(TableMtTask.UNICOM)
			.append("=").append(systemMtTaskVo.getUnicom());
		}
		//业务类型
		if (systemMtTaskVo.getSvrtype() != null && !"".equals(systemMtTaskVo.getSvrtype())) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			if("-1".equals(systemMtTaskVo.getSvrtype()))
			{
				conditionSql.append(" not exists (select BUS_CODE from LF_BUSMANAGER "+StaticValue.getWITHNOLOCK()+"  where BUS_CODE=mttask.SVRTYPE) ");
			}
			else
			{
				conditionSql.append(" mttask.").append(TableMtTask01_12.SVRTYPE)
					.append("='").append(systemMtTaskVo.getSvrtype().trim()).append("'");
			}
		}
        //发送状态
		if (systemMtTaskVo.getSendstatus() != null) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.").append(TableMtTask.SEND_STATUS)
			.append("=").append(systemMtTaskVo.getSendstatus());
		}
        //发送错误编码
		if (systemMtTaskVo.getErrorcode() != null && !"".equals(systemMtTaskVo.getErrorcode()))
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			if ("成功".equals(systemMtTaskVo.getErrorcode().trim())) 
			{
				conditionSql.append(" mttask.").append(
						TableMtTask.ERROR_CODE).append(" in ('DELIVRD','0')");
			} 
			else if ("未返".equals(systemMtTaskVo.getErrorcode().trim())) 
			{
				conditionSql.append(" mttask.").append(
						TableMtTask.ERROR_CODE).append(" = '       '");
			}
			else if ("失败".equals(systemMtTaskVo.getErrorcode().trim())) 
			{
				conditionSql.append(" mttask.").append(
						TableMtTask.ERROR_CODE).append(
						" not in ('DELIVRD','0','       ')");
			} 
			else
			{
				conditionSql.append(" mttask.").append(
						TableMtTask.ERROR_CODE).append(" like '%").append(
						systemMtTaskVo.getErrorcode().toUpperCase()).append(
						"%'");
			}
		}
        //手机号码
		if (systemMtTaskVo.getPhone() != null && !"".equals(systemMtTaskVo.getPhone())) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.").append(TableMtTask.PHONE)
			.append(" = '").append(systemMtTaskVo.getPhone().trim())
			.append("'");
		}
		
        //任务批次
		if (systemMtTaskVo.getTaskid() != null)
		{
			Long taskid =0l;
			if(systemMtTaskVo.getTaskid()==0l){
				taskid=-1l;
			}else{
				taskid=systemMtTaskVo.getTaskid();
			}
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.TASKID").append("=").append(taskid).append(" ");
		}
		
		//短信内容
		if(systemMtTaskVo.getMessage() != null && !"".equals(systemMtTaskVo.getMessage()))
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.MESSAGE")
			.append(" like '%").append(systemMtTaskVo.getMessage().trim()).append("%'");
		}
		//操作员
		if(systemMtTaskVo.getP1() != null && !"".equals(systemMtTaskVo.getP1()))
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			//conditionSql.append(" and mttask.p1 in (").append(systemMtTaskVo.getP1()).append(")");
			conditionSql.append(" exists (select USER_CODE from lf_sysuser "+StaticValue.getWITHNOLOCK()+" where USER_CODE=mttask.p1 and (USER_NAME like '%")
			.append(systemMtTaskVo.getP1()).append("%' or NAME like '%").append(systemMtTaskVo.getP1()).append("%') ");

			if(systemMtTaskVo.getCorpcode()!=null&&!"".equals(systemMtTaskVo.getCorpcode())){
				conditionSql.append(" and CORP_CODE= '").append(systemMtTaskVo.getCorpcode()).append("' ");
			}
			conditionSql.append(") ");
		}
        //指定时间段，开始时间
		if (systemMtTaskVo.getStartTime() != null && !"".equals(systemMtTaskVo.getStartTime())) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.SENDTIME >=? ");
		}
		//指定时间段，结束时间
		if (systemMtTaskVo.getEndTime() != null && !"".equals(systemMtTaskVo.getEndTime())) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.SENDTIME <=? ");
		}

		if (systemMtTaskVo.getCpno() != null && !"".equals(systemMtTaskVo.getCpno()))
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttask.CPNO like '%").append(systemMtTaskVo.getCpno().trim()).append("%'");
		}
		String sql = conditionSql.toString();
		return sql;
	}

	/**
	 * 系统下行实时记录查询
	 * @return
	 */
	public static String getOrderBySql() {
		String sql = new StringBuffer(" order by mttask.").append(
				TableMtTask.ID).append(" desc").toString();
		return sql;
	}

	/**
	 * 系统下行实时记录查询
	 * @param systemMtTaskVo
	 * @return
	 */
	public static List<String> getTimeCondition(SystemMtTaskVo systemMtTaskVo) {
		List<String> timeList = new ArrayList<String>();
		if (systemMtTaskVo.getStartTime() != null
				&& !"".equals(systemMtTaskVo.getStartTime())) {
			timeList.add(systemMtTaskVo.getStartTime());
		}
		if (systemMtTaskVo.getEndTime() != null
				&& !"".equals(systemMtTaskVo.getEndTime())) {
			timeList.add(systemMtTaskVo.getEndTime());
		}
		return timeList;
	}
	
	/**
	 * 按情况拼接where或and
	 * @param sql
	 * @param hasWhere false为没where关键词
	 * @return 第一次拼接后将会有where，直接返回true
	 */
	private static boolean addWhereOrAnd(StringBuffer sql, boolean hasWhere)
	{
		if(!hasWhere)
		{
			sql.append(" where ");
		}
		else
		{
			sql.append(" and ");
		}
		return true;
	}
	
}
