package com.montnets.emp.query.dao;

import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.inbox.dao.ReciveBoxDao;
import com.montnets.emp.query.vo.SystemMtTask01_12Vo;
import com.montnets.emp.table.sms.TableMtTask01_12;

/**
 * 系统下历史记录查询 
 * @project sinolife
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-14 下午03:52:40
 * @description
 */
public class GenericSystemMtTask01_12VoSQL {
	/**
	 * 下行记录历史查询列名
	 */
	public static String getFieldSql() {
		String sql = new StringBuffer("select mttaskxx.").append(
				TableMtTask01_12.ID).append(",mttaskxx.").append(
				TableMtTask01_12.USER_ID).append(",mttaskxx.").append(
				TableMtTask01_12.SPGATE).append(",mttaskxx.").append(
				TableMtTask01_12.CPNO).append(",mttaskxx.").append(
				TableMtTask01_12.PHONE).append(",mttaskxx.").append(
				TableMtTask01_12.SEND_STATUS).append(",mttaskxx.").append(
				TableMtTask01_12.TASK_ID).append(",mttaskxx.").append(
				TableMtTask01_12.ERRO_RCODE).append(",mttaskxx.").append(
				TableMtTask01_12.SEND_TIME).append(",mttaskxx.").append(
				TableMtTask01_12.MESSAGE).append(",mttaskxx.").append(
				TableMtTask01_12.UNICOM).append(",mttaskxx.").append(
				TableMtTask01_12.PK_NUMBER).append(",mttaskxx.").append(
				TableMtTask01_12.PK_TOTAL).append(",mttaskxx.").append(
				TableMtTask01_12.SVRTYPE)/*.append(",bus.").append(
				TableLfBusManager.BUS_NAME)*/.append(",mttaskxx.").append(
				TableMtTask01_12.RECV_TIME)
				.toString();
		return sql;
	}

	/**
	 * SystemMtTask01_12VoDAO
	 * 系统下行记录历史查询
	 * @param tableName
	 * @return
	 */
	public static String getTableSql(String tableName) {
		// String sql = new StringBuffer(" from ").append(tableName).append(
		// " mttaskxx where").append(" mttaskxx.").append(
		// TableMtTask01_12.ERRO_RCODE).append(" not like 'E%'")
		// .toString();
		// 增加withnolock
		String withnolock ="";
		if(tableName != null && !"".equals(tableName) && tableName.indexOf("union") == -1)
		{
			withnolock = StaticValue.getWITHNOLOCK();
		}
		String sql = new StringBuffer(" from ").append(tableName).append(
		//" mttaskxx left join ").append(TableLfBusManager.TABLE_NAME).append(" bus on bus.").append(TableLfBusManager.BUS_CODE)
		//.append("=mttaskxx.").append(TableMtTask.SVRTYPE).append(" ").toString();
				" mttaskxx  ").append(" "+withnolock+" ").toString();
//		.append(" left join ").append(TableLfTask.TABLE_NAME)
//		.append(" task on task.").append(TableLfTask.TASK_ID).append("=mttaskxx.").append(TableMtTask.TASK_ID)
//		.append(" left join ").append(TableLfMttask.TABLE_NAME).append(" mttask on mttask.").append(TableLfMttask.MT_ID)
//		.append("=task.").append(TableLfTask.MT_ID).append(" left join ").append(TableLfBusManager.TABLE_NAME)
//		.append(" bus on bus.").append(TableLfBusManager.BUS_CODE).append("=mttask.").append(TableLfMttask.BUS_CODE)
		return sql;
	}

	/**
	 * SystemMtTask01_12VoDAO
	 * 系统下午记录历史查询
	 * @param systemMtTask01_12Vo
	 * @return
	 */
	public static String getConditionSql(SystemMtTask01_12Vo systemMtTask01_12Vo)
	{
		StringBuffer conditionSql = new StringBuffer();
		//是否有where，默认为false即没where
		boolean hasWhere = false;
        //如果未绑定sp账号，且时多企业，则查询结果为空
		if(systemMtTask01_12Vo.getSpusers()==null && StaticValue.getCORPTYPE() == 1)
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" 1=2");
		}
		// sp账号
		if (systemMtTask01_12Vo.getUserid() != null && !"".equals(systemMtTask01_12Vo.getUserid()))
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttaskxx.").append(
					TableMtTask01_12.USER_ID).append("='").append(
					systemMtTask01_12Vo.getUserid().trim()).append("'");
		}
        //如查绑定了sp账号，则需要查询此范围内的结果
		if (systemMtTask01_12Vo.getSpusers() != null && !"".equals(systemMtTask01_12Vo.getSpusers()))
		{
			String spusers=systemMtTask01_12Vo.getSpusers();
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
				String insqlstr=new ReciveBoxDao().getSqlStr(spusers, "mttaskxx."+TableMtTask01_12.USER_ID);
				conditionSql.append(" (" + insqlstr + ") ");
			}
			else
			{
				conditionSql.append(" exists (select SPUSER from lf_sp_dep_bind "+StaticValue.getWITHNOLOCK()+" where SPUSER=mttaskxx.USERID ");
				if(systemMtTask01_12Vo.getCorpcode()!=null&&!"".equals(systemMtTask01_12Vo.getCorpcode())){
					conditionSql.append(" and CORP_CODE= '").append(systemMtTask01_12Vo.getCorpcode().trim()).append("' ");
				}
				conditionSql.append(") ");
			}
		}
		//运营商
		if (systemMtTask01_12Vo.getUnicom() != null)
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttaskxx.").append(TableMtTask01_12.UNICOM)
			.append("=").append(systemMtTask01_12Vo.getUnicom());
		}
		//业务类型
		if (systemMtTask01_12Vo.getSvrtype() != null && !"".equals(systemMtTask01_12Vo.getSvrtype()))
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			if("-1".equals(systemMtTask01_12Vo.getSvrtype()))
			{
				conditionSql.append(" not exists (select BUS_CODE from LF_BUSMANAGER "+StaticValue.getWITHNOLOCK()+" where BUS_CODE=mttaskxx.SVRTYPE) ");
			}
			else
			{
				conditionSql.append(" mttaskxx.").append(TableMtTask01_12.SVRTYPE)
				.append("='").append(systemMtTask01_12Vo.getSvrtype().trim()).append("'");
			}
		}
		//短信内容
		if(systemMtTask01_12Vo.getMessage() != null && !"".equals(systemMtTask01_12Vo.getMessage()))
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttaskxx.").append(TableMtTask01_12.MESSAGE)
			.append(" like '%").append(systemMtTask01_12Vo.getMessage().trim())
			.append("%'");
		}
		//操作员
		if(systemMtTask01_12Vo.getP1() != null && !"".equals(systemMtTask01_12Vo.getP1()))
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
//			conditionSql.append(" and mttaskxx.p1 in (").append(systemMtTask01_12Vo.getP1()).append(")");
			conditionSql.append(" exists (select USER_CODE from lf_sysuser "+StaticValue.getWITHNOLOCK()+" where USER_CODE=mttaskxx.p1 and (USER_NAME like '%")
			.append(systemMtTask01_12Vo.getP1().trim()).append("%' or NAME like '%").append(systemMtTask01_12Vo.getP1().trim()).append("%') ");

			if(systemMtTask01_12Vo.getCorpcode()!=null&&!"".equals(systemMtTask01_12Vo.getCorpcode())){
				conditionSql.append(" and CORP_CODE= '").append(systemMtTask01_12Vo.getCorpcode().trim()).append("' ");
			}
			conditionSql.append(") ");
		}
        //通道号
		if (systemMtTask01_12Vo.getSpgate() != null && !"".equals(systemMtTask01_12Vo.getSpgate()))
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttaskxx.").append(
					TableMtTask01_12.SPGATE).append("='").append(systemMtTask01_12Vo.getSpgate().trim()).append("'");
		}
        //发送状态
		if (systemMtTask01_12Vo.getSendstatus() != null) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttaskxx.").append(
					TableMtTask01_12.SEND_STATUS).append("=").append(
					systemMtTask01_12Vo.getSendstatus());
		}
        // 发送错误编码
		if (systemMtTask01_12Vo.getErrorcode() != null && !"".equals(systemMtTask01_12Vo.getErrorcode()))
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			if ("成功".equals(systemMtTask01_12Vo.getErrorcode().trim())) 
			{
				conditionSql.append(" mttaskxx.").append(
						TableMtTask01_12.ERRO_RCODE).append(
						" in ('DELIVRD','0','       ')");
			}
			else if ("失败".equals(systemMtTask01_12Vo.getErrorcode().trim())) 
			{
				conditionSql.append(" mttaskxx.").append(
						TableMtTask01_12.ERRO_RCODE).append(
						" not in ('DELIVRD','0','       ')");
			}
			else 
			{
				conditionSql.append(" mttaskxx.").append(
						TableMtTask01_12.ERRO_RCODE).append(" like '%").append(
						systemMtTask01_12Vo.getErrorcode().toUpperCase())
						.append("%'");
			}
		}
        //手机号
		if (systemMtTask01_12Vo.getPhone() != null && !"".equals(systemMtTask01_12Vo.getPhone())) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttaskxx.")
			.append(TableMtTask01_12.PHONE).append(" = '").append(systemMtTask01_12Vo.getPhone().trim()).append("'");
		}
		
		  //任务批次
		if (systemMtTask01_12Vo.getTaskid() != null) 
		{
			Long taskid =0l;
			if(systemMtTask01_12Vo.getTaskid()==0l){
				taskid=-1l;
			}else{
				taskid=systemMtTask01_12Vo.getTaskid();
			}
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttaskxx.").append(TableMtTask01_12.TASK_ID)
			.append("=").append(taskid).append(" ");
		}
		
        //指定时间 段，开始时间 
		if (systemMtTask01_12Vo.getStartTime() != null && !"".equals(systemMtTask01_12Vo.getStartTime())) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttaskxx.").append(
					TableMtTask01_12.SEND_TIME).append(">=?");
		}
        //指定时间段，结束时间
		if (systemMtTask01_12Vo.getEndTime() != null && !"".equals(systemMtTask01_12Vo.getEndTime())) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttaskxx.").append(
					TableMtTask01_12.SEND_TIME).append("<=?");
		}

		if (systemMtTask01_12Vo.getCpno() != null && !"".equals(systemMtTask01_12Vo.getCpno())) 
		{
			hasWhere = addWhereOrAnd(conditionSql, hasWhere);
			conditionSql.append(" mttaskxx.").append(TableMtTask01_12.CPNO)
			.append(" like '%").append(systemMtTask01_12Vo.getCpno().trim())
			.append("%'");
		}
		String sql = conditionSql.toString();
		return sql;
	}

	/**
	 * SystemMtTask01_12VoDAO的
	 * 系统下行历史记录查询
	 * @return
	 */
	public static String getOrderBySql() {
		String sql = new StringBuffer(" order by mttaskxx.").append(
				TableMtTask01_12.ID).append(" desc").toString();
		return sql;
	}

	/**
	 * 系统下行历史记录查询
	 * @param systemMtTask01_12Vo
	 * @return
	 */
	public static List<String> getTimeCondition(
			SystemMtTask01_12Vo systemMtTask01_12Vo) {
		List<String> timeList = new ArrayList<String>();
		if (systemMtTask01_12Vo.getStartTime() != null
				&& !"".equals(systemMtTask01_12Vo.getStartTime())) {
			timeList.add(systemMtTask01_12Vo.getStartTime());
		}
		if (systemMtTask01_12Vo.getEndTime() != null
				&& !"".equals(systemMtTask01_12Vo.getEndTime())) {
			timeList.add(systemMtTask01_12Vo.getEndTime());
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
