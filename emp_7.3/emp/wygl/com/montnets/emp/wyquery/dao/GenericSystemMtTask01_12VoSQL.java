package com.montnets.emp.wyquery.dao;

import java.util.ArrayList;
import java.util.List;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.wyquery.vo.SystemMtTask01_12Vo;
import com.montnets.emp.table.sms.TableMtTask;
import com.montnets.emp.table.sms.TableMtTask01_12;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;

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
	public static String getFieldSql(String tableName) {
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
				.append(",gate.GateName")
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
		// 增加了子号判断
		String sql = new StringBuffer(" from ").append(tableName).append(
				" mttaskxx  ").append(" left join lf_sysuser c on mttaskxx.p1=c.USER_CODE  left join xt_gate_queue gate on  mttaskxx.spgate  = gate.spgate ").toString();

		return sql;
	}

	/**
	 * SystemMtTask01_12VoDAO
	 * 系统下午记录历史查询
	 * @param systemMtTask01_12Vo
	 * @return
	 */
	public static String getConditionSql(SystemMtTask01_12Vo systemMtTask01_12Vo,String inter) {
		StringBuffer conditionSql = new StringBuffer();
        //如果未绑定sp账号，且时多企业，则查询结果为空
		int corpType = StaticValue.getCORPTYPE();
		if(systemMtTask01_12Vo.getSpusers()==null && corpType == 1){
			conditionSql.append("and 1=2");
			
		}
		//需要指定 是网优模块的
		conditionSql.append( " and mttaskxx.spgate like '200%'" );
		//增加权限
		conditionSql.append(getDominationSql(systemMtTask01_12Vo.getLguserid()));
		// sp账号
		if (systemMtTask01_12Vo.getUserid() != null
				&& !"".equals(systemMtTask01_12Vo.getUserid())) {
			conditionSql.append(" and mttaskxx.").append(
					TableMtTask01_12.USER_ID).append("='").append(
					systemMtTask01_12Vo.getUserid().trim()).append("'");
		}
        //如查绑定了sp账号，则需要查询此范围内的结果
		if (systemMtTask01_12Vo.getSpusers() != null
				&& !"".equals(systemMtTask01_12Vo.getSpusers())) {
//			conditionSql.append(" and mttaskxx.").append(TableMtTask01_12.USER_ID)
//					.append(" in (").append(systemMtTask01_12Vo.getSpusers())
//					.append(")");
			
			conditionSql.append(" and exists (select SPUSER from lf_sp_dep_bind with(nolock) where SPUSER=mttaskxx.USERID ");
			if(systemMtTask01_12Vo.getCorpcode()!=null&&!"".equals(systemMtTask01_12Vo.getCorpcode())){
				conditionSql.append(" and CORP_CODE= '").append(systemMtTask01_12Vo.getCorpcode().trim()).append("' ");
			}
			conditionSql.append(") ");
		}
		//运营商
		if (systemMtTask01_12Vo.getUnicom() != null) {
			conditionSql.append(" and mttaskxx.").append(TableMtTask01_12.UNICOM)
					.append("=").append(systemMtTask01_12Vo.getUnicom());					
		}
		//业务类型
		if (systemMtTask01_12Vo.getSvrtype() != null
				&& !"".equals(systemMtTask01_12Vo.getSvrtype())) {
			if("-1".equals(systemMtTask01_12Vo.getSvrtype())){
				conditionSql.append(" and not exists (select BUS_CODE from LF_BUSMANAGER where BUS_CODE=mttaskxx.SVRTYPE) ");
			}else{
				conditionSql.append(" and mttaskxx.").append(TableMtTask01_12.SVRTYPE)
					.append("='").append(systemMtTask01_12Vo.getSvrtype().trim()).append("'");
			}
		}
		//短信内容
		if(systemMtTask01_12Vo.getMessage() != null && !"".equals(systemMtTask01_12Vo.getMessage()))
		{
			conditionSql.append(" and mttaskxx.").append(TableMtTask01_12.MESSAGE)
			.append(" like '%").append(systemMtTask01_12Vo.getMessage().trim())
			.append("%'");
		}
		//操作员
//		if(systemMtTask01_12Vo.getP1() != null && !"".equals(systemMtTask01_12Vo.getP1()))
//		{
//			conditionSql.append(" and mttaskxx.p1 = '").append(systemMtTask01_12Vo.getP1()).append("'");
			
//			conditionSql.append(" and exists (select USER_CODE from lf_sysuser with(nolock) where USER_CODE=mttaskxx.p1 and (USER_NAME like '%")
//			.append(systemMtTask01_12Vo.getP1().trim()).append("%' or NAME like '%").append(systemMtTask01_12Vo.getP1().trim()).append("%') ");
//
//			if(systemMtTask01_12Vo.getCorpcode()!=null&&!"".equals(systemMtTask01_12Vo.getCorpcode())){
//				conditionSql.append(" and CORP_CODE= '").append(systemMtTask01_12Vo.getCorpcode().trim()).append("' ");
//			}
//			conditionSql.append(") ");
			
//		}
        //通道号
		if (systemMtTask01_12Vo.getSpgate() != null
				&& !"".equals(systemMtTask01_12Vo.getSpgate())) {
			conditionSql.append(" and mttaskxx.").append(
					TableMtTask01_12.SPGATE).append("='").append(
					systemMtTask01_12Vo.getSpgate().trim()).append("'");
		}
        //发送状态
		if (systemMtTask01_12Vo.getSendstatus() != null ) {
			conditionSql.append(" and mttaskxx.").append(
					TableMtTask01_12.SEND_STATUS).append("=").append(
					systemMtTask01_12Vo.getSendstatus());
		}
        // 发送错误编码
		if (systemMtTask01_12Vo.getErrorcode() != null
				&& !"".equals(systemMtTask01_12Vo.getErrorcode())) {
			if ("success".equals(systemMtTask01_12Vo.getErrorcode().trim())) {
				conditionSql.append(" and mttaskxx.").append(
						TableMtTask01_12.ERRO_RCODE).append(
						" in ('DELIVRD','0','       ')");
			}
			else if ("fail".equals(systemMtTask01_12Vo.getErrorcode().trim())) {
				conditionSql.append(" and mttaskxx.").append(
						TableMtTask01_12.ERRO_RCODE).append(
						" not in ('DELIVRD','0','       ')");
			} else {
				conditionSql.append(" and mttaskxx.").append(
						TableMtTask01_12.ERRO_RCODE).append(" like '%").append(
						systemMtTask01_12Vo.getErrorcode().toUpperCase())
						.append("%'");
			}
		}
        //手机号
		if (systemMtTask01_12Vo.getPhone() != null
				&& !"".equals(systemMtTask01_12Vo.getPhone())) {
			if(systemMtTask01_12Vo.getPhone().length()==11){
				conditionSql.append(" and mttaskxx.")
				.append(TableMtTask01_12.PHONE).append(" = '").append(
						systemMtTask01_12Vo.getPhone().trim()).append("'");
			}else{
				conditionSql.append(" and mttaskxx.")
					.append(TableMtTask01_12.PHONE).append(" like '%").append(
							systemMtTask01_12Vo.getPhone().trim()).append("%'");
			}
		}
		
		  //任务批次
		if (systemMtTask01_12Vo.getTaskid() != null) {
			Long taskid =0l;
			if(systemMtTask01_12Vo.getTaskid()==0l){
				taskid=-1l;
			}else{
				taskid=systemMtTask01_12Vo.getTaskid();
			}
			conditionSql.append(" and mttaskxx.").append(TableMtTask01_12.TASK_ID)
					.append("=").append(taskid).append(" ");
		}
		
        //指定时间 段，开始时间 
		if (systemMtTask01_12Vo.getStartTime() != null
				&& !"".equals(systemMtTask01_12Vo.getStartTime())) {
			conditionSql.append(" and mttaskxx.").append(
					TableMtTask01_12.SEND_TIME).append(">='").append(systemMtTask01_12Vo.getStartTime()).append("'");
		}
        //指定时间段，结束时间
		if (systemMtTask01_12Vo.getEndTime() != null
				&& !"".equals(systemMtTask01_12Vo.getEndTime())) {
			conditionSql.append(" and mttaskxx.").append(
					TableMtTask01_12.SEND_TIME).append("<='").append(systemMtTask01_12Vo.getEndTime()).append("'");
		}

		if (systemMtTask01_12Vo.getCpno() != null
				&& !"".equals(systemMtTask01_12Vo.getCpno())) {
			conditionSql.append(" and mttaskxx.").append(TableMtTask01_12.CPNO)
					.append(" like '%").append(systemMtTask01_12Vo.getCpno().trim())
					.append("%'");
		}
		//通道名称
		if (systemMtTask01_12Vo.getGateName() != null
				&& !"".equals(systemMtTask01_12Vo.getGateName())) {
			conditionSql.append(" and gate.GateName")
			.append(" like '%").append(systemMtTask01_12Vo.getGateName().trim())
			.append("%'");
		}
		
		if (systemMtTask01_12Vo.getReportStatus() != null
				&& !"".equals(systemMtTask01_12Vo.getReportStatus())&&"success".equals(systemMtTask01_12Vo.getReportStatus())) {
			conditionSql.append(" and( mttaskxx.").append(TableMtTask.ERROR_CODE)
			.append(" ='DELIVRD' or ").append(TableMtTask.ERROR_CODE) .append(" ='0')");
		}else if(systemMtTask01_12Vo.getReportStatus() != null&&"fail".equals(systemMtTask01_12Vo.getReportStatus())){
			conditionSql.append(" and( mttaskxx.").append(TableMtTask.ERROR_CODE)
			.append(" !='DELIVRD' and ").append(TableMtTask.ERROR_CODE) .append("!='0')");
		}
		//号码类型
		
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
}
