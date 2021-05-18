package com.montnets.emp.wyquery.dao;

import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.wyquery.vo.SystemMtTaskVo;
import com.montnets.emp.table.sms.TableMtTask;
import com.montnets.emp.table.sms.TableMtTask01_12;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
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
						TableMtTask.RECV_TIME)
						.append(",gate.GateName");
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
		//增加了子号判断
		String sql = new StringBuffer(" from ")
//                .append(TableMtTask.TABLE_NAME)
                .append("gw_mt_task_bak")
				.append(" mttask left join lf_sysuser c on mttask.p1=c.USER_CODE   left join xt_gate_queue gate on  mttask.spgate = gate.spgate  ")/*.append(" left join ").append(TableLfBusManager.TABLE_NAME).append(" bus ")
				.append(StaticValue.WITHNOLOCK).append(" on bus.").append(TableLfBusManager.BUS_CODE)
				.append("=mttask.").append(TableMtTask.SVRTYPE)*/.toString();
		return sql;
	}

	/**
	 * SystemMtTaskVoDAO
	 * 系统下行实时记录查询
	 * @param systemMtTaskVo
	 * @return
	 */
	public static String getConditionSql(SystemMtTaskVo systemMtTaskVo,String inter) {
		StringBuffer conditionSql = new StringBuffer();
		int corpType = StaticValue.getCORPTYPE();
		//如果未绑定sp账号，且是多企业则查询结果为空
		if(systemMtTaskVo.getSpusers()==null && corpType == 1)
		{
			conditionSql.append("and 1=2 ");
		}
		//需要指定 是网优模块的
		conditionSql.append( " and mttask.spgate like '200%'" );
		//增加权限
		conditionSql.append(getDominationSql(systemMtTaskVo.getLguserid()));
        //sp账号
		if (systemMtTaskVo.getUserid() != null
				&& !"".equals(systemMtTaskVo.getUserid())) {
			conditionSql.append(" and mttask.").append(TableMtTask.USER_ID)
					.append("='").append(systemMtTaskVo.getUserid().trim())
					.append("'");

		}			
        //如果绑定了sp账号，则需要查询此范围内的记录
		if (systemMtTaskVo.getSpusers() != null 
				&& !"".equals(systemMtTaskVo.getSpusers())) {
//			conditionSql.append(" and mttask.").append(TableMtTask.USER_ID) //去掉了
//					.append(" in (").append(systemMtTaskVo.getSpusers())
//					.append(")");
			
			conditionSql.append(" and exists (select SPUSER from lf_sp_dep_bind with(nolock) where SPUSER=mttask.USERID ");
			if(systemMtTaskVo.getCorpcode()!=null&&!"".equals(systemMtTaskVo.getCorpcode())){
				conditionSql.append(" and CORP_CODE= '").append(systemMtTaskVo.getCorpcode().trim()).append("' ");
			}
			conditionSql.append(") ");
			
			
		}
        //通道号
		if (systemMtTaskVo.getSpgate() != null
				&& !"".equals(systemMtTaskVo.getSpgate())) {
			conditionSql.append(" and mttask.").append(TableMtTask.SPGATE)
					.append("='").append(systemMtTaskVo.getSpgate().trim())
					.append("'");
		}
		//运营商
		if (systemMtTaskVo.getUnicom() != null) {
			conditionSql.append(" and mttask.").append(TableMtTask.UNICOM)
					.append("=").append(systemMtTaskVo.getUnicom());					
		}
		//业务类型
		if (systemMtTaskVo.getSvrtype() != null
				&& !"".equals(systemMtTaskVo.getSvrtype())) {
			if("-1".equals(systemMtTaskVo.getSvrtype())){
				conditionSql.append(" and not exists (select BUS_CODE from LF_BUSMANAGER where BUS_CODE=mttask.SVRTYPE) ");
			}else{
				conditionSql.append(" and mttask.").append(TableMtTask01_12.SVRTYPE)
					.append("='").append(systemMtTaskVo.getSvrtype().trim()).append("'");
			}
		}
        //发送状态
		if (systemMtTaskVo.getSendstatus() != null) {
			conditionSql.append(" and mttask.").append(TableMtTask.SEND_STATUS)
					.append("=").append(systemMtTaskVo.getSendstatus());
		}
        //发送错误编码
		if (systemMtTaskVo.getErrorcode() != null
				&& !"".equals(systemMtTaskVo.getErrorcode())) {
			if ("success".equals(systemMtTaskVo.getErrorcode().trim())) {
				conditionSql.append(" and mttask.").append(
						TableMtTask.ERROR_CODE).append(" in ('DELIVRD','0','       ')");
			} else if ("fail".equals(systemMtTaskVo.getErrorcode().trim())) {
				conditionSql.append(" and mttask.").append(
						TableMtTask.ERROR_CODE).append(
						" not in ('DELIVRD','0','       ')");
			} else {
				conditionSql.append(" and mttask.").append(
						TableMtTask.ERROR_CODE).append(" like '%").append(
						systemMtTaskVo.getErrorcode().toUpperCase()).append(
						"%'");
			}
		}
        //手机号码
		if (systemMtTaskVo.getPhone() != null
				&& !"".equals(systemMtTaskVo.getPhone())) {
			if(systemMtTaskVo.getPhone().length()==11){
				conditionSql.append(" and mttask.").append(TableMtTask.PHONE)
				.append(" = '").append(systemMtTaskVo.getPhone().trim())
				.append("' ");
			}else{
				conditionSql.append(" and mttask.").append(TableMtTask.PHONE)
				.append(" like '%").append(systemMtTaskVo.getPhone().trim())
				.append("%'");
			}
			
		}
		
        //任务批次
		if (systemMtTaskVo.getTaskid() != null) {
			Long taskid =0l;
			if(systemMtTaskVo.getTaskid()==0l){
				taskid=-1l;
			}else{
				taskid=systemMtTaskVo.getTaskid();
			}
			conditionSql.append(" and mttask.").append(TableMtTask.TASK_ID)
					.append("=").append(taskid).append(" ");
		}
		
		//短信内容
		if(systemMtTaskVo.getMessage() != null && !"".equals(systemMtTaskVo.getMessage()))
		{
			conditionSql.append(" and mttask.").append(TableMtTask.MESSAGE)
			.append(" like '%").append(systemMtTaskVo.getMessage().trim())
			.append("%'");
		}
		//操作员
//		if(systemMtTaskVo.getP1() != null && !"".equals(systemMtTaskVo.getP1()))
//		{
//			//个人只能查询自己发送的记录
//			conditionSql.append(" and mttask.p1 = '").append(systemMtTaskVo.getP1()).append("'");
//		}else{
//			//机构权限查询本机构节点权限范围内发送的记录
//			StringBuffer dominationSql = new StringBuffer("select ").append(
//					TableLfDomination.DEP_ID).append(" from ").append(
//					TableLfDomination.TABLE_NAME).append(" where ").append(
//					TableLfDomination.USER_ID).append("=").append(systemMtTaskVo.getLguserid());
//			conditionSql.append(" and (c.").append(TableLfSysuser.DEP_ID).append(" in (")
//					.append(dominationSql).append(")")
//					.append(")");
//		}
        //指定时间段，开始时间
		if (systemMtTaskVo.getStartTime() != null
				&& !"".equals(systemMtTaskVo.getStartTime())) {
			conditionSql.append(" and mttask.").append(TableMtTask.SEND_TIME)
					.append(">='").append(systemMtTaskVo.getStartTime()).append("'");
		}
		//指定时间段，结束时间
		if (systemMtTaskVo.getEndTime() != null
				&& !"".equals(systemMtTaskVo.getEndTime())) {
			conditionSql.append(" and mttask.").append(TableMtTask.SEND_TIME)
					.append("<='").append(systemMtTaskVo.getEndTime()).append("'");
		}

		if (systemMtTaskVo.getCpno() != null
				&& !"".equals(systemMtTaskVo.getCpno())) {
			conditionSql.append(" and mttask.").append(TableMtTask.CPNO)
					.append(" like '%").append(systemMtTaskVo.getCpno().trim())
					.append("%'");
		}
		//通道名称
		if (systemMtTaskVo.getGateName() != null
				&& !"".equals(systemMtTaskVo.getGateName())) {
			conditionSql.append(" and gate.GateName")
			.append(" like '%").append(systemMtTaskVo.getGateName().trim())
			.append("%'");
		}
		
		if (systemMtTaskVo.getReportStatus() != null
				&& !"".equals(systemMtTaskVo.getReportStatus())&&"success".equals(systemMtTaskVo.getReportStatus())) {
			conditionSql.append(" and( mttask.").append(TableMtTask.ERROR_CODE)
			.append(" ='DELIVRD' or ").append(TableMtTask.ERROR_CODE) .append(" ='0')");
		}else if(systemMtTaskVo.getReportStatus() != null&&"fail".equals(systemMtTaskVo.getReportStatus())){
			conditionSql.append(" and( mttask.").append(TableMtTask.ERROR_CODE)
			.append(" !='DELIVRD' and ").append(TableMtTask.ERROR_CODE) .append("!='0')");
		}
		
		String sql = conditionSql.toString();
		return sql;
	}

	//获取权限
	public  static String getDominationSql(String userId)
	{
		StringBuffer dominationSql = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(userId);
		String sql = new StringBuffer(" and (c.").append(
				TableLfSysuser.USER_ID).append("=").append(userId).append(
				" or c.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(dominationSql).append(")")
				.append(")").toString();
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
}
