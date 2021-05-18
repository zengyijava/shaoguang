package com.montnets.emp.rms.rmstask.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.rms.meditor.table.TableLfTempImportBatch;
import com.montnets.emp.rms.rmstask.vo.LfMttaskVo;
import com.montnets.emp.table.biztype.TableLfBusManager;
import com.montnets.emp.table.rms.TableLfRmsTaskCtrl;
import com.montnets.emp.table.sms.TableLfMttask;
import com.montnets.emp.table.sms.TableMtTask01_12;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.template.TableLfTemplate;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedHashMap;

public class RmsTaskSql {

	/**
	 * LfMttaskVoDAO 组装查询语句
	 *
	 * @return
	 */
	public static String getTableSql() {
		return " FROM " +
				TableLfMttask.TABLE_NAME + " mttask " + StaticValue.getWITHNOLOCK() + "LEFT JOIN " +
				TableLfBusManager.TABLE_NAME + " busmanager" + StaticValue.getWITHNOLOCK() + " ON mttask." +
				TableLfMttask.BUS_CODE + " = busmanager." + TableLfBusManager.BUS_CODE + " LEFT JOIN " +
				TableLfSysuser.TABLE_NAME + " sysuser" + StaticValue.getWITHNOLOCK() + " ON mttask." +
				TableLfMttask.USER_ID + " = sysuser." + TableLfSysuser.USER_ID + " LEFT JOIN " +
				TableLfDep.TABLE_NAME + " dep" + StaticValue.getWITHNOLOCK() + " ON sysuser." +
				TableLfSysuser.DEP_ID + " = dep." + TableLfDep.DEP_ID + " LEFT JOIN " +
				TableLfTemplate.TABLE_NAME + " template" + StaticValue.getWITHNOLOCK() + " ON mttask." +
				TableLfMttask.TEMP_ID + " = template." + TableLfTemplate.SP_TEMPLID + " AND template."+
				TableLfTemplate.SP_TEMPLID +" <> 0" + " LEFT JOIN " +
				TableLfRmsTaskCtrl.TABLE_NAME + " rmsctrl" + StaticValue.getWITHNOLOCK() + " ON mttask." +
				TableLfMttask.TASKID + " = rmsctrl." + TableLfRmsTaskCtrl.TASKID + " LEFT JOIN " +
				TableLfTempImportBatch.TABLE_NAME + " lfbatch ON lfbatch." + TableLfTempImportBatch.BATCH +
				" = mttask." + TableLfMttask.TASKID;
	}

	/**
	 * LfMttaskVoDAO 组装查询语句
	 * 
	 * @return
	 */
	public static String getFieldSql() {
		return "SELECT mttask." + TableLfMttask.MT_ID +
				",mttask." + TableLfMttask.SP_USER +
				",mttask." + TableLfMttask.MOBILE_URL +
				",mttask." + TableLfMttask.USER_ID +
				",mttask." + TableLfMttask.TITLE +
				",mttask." + TableLfMttask.BUS_CODE +
				",mttask." + TableLfMttask.TEMP_ID +
				",mttask." + TableLfMttask.EFF_COUNT +
				",mttask." + TableLfMttask.ICOUNT2 +
				",mttask." + TableLfMttask.ICOUNT +
				",mttask." + TableLfMttask.RFAIL2 +
				",mttask." + TableLfMttask.FAI_COUNT +
				",mttask." + TableLfMttask.MSG_TYPE +
				",mttask." + TableLfMttask.SUBMIT_TIME +
				",mttask." + TableLfMttask.TIMER_TIME +
				",mttask." + TableLfMttask.TASKTYPE +
				",mttask." + TableLfMttask.TIMER_STATUS +
				",mttask." + TableLfMttask.TASKID +
				",mttask." + TableLfMttask.SUB_STATE +
				",mttask." + TableLfMttask.SEND_STATE +
				",mttask." + TableLfMttask.ISRETRY +
				",mttask." + TableLfMttask.TMPL_PATH +
				",sysuser." + TableLfSysuser.NAME +
				",sysuser." + TableLfSysuser.USER_STATE +
				",busmanager." + TableLfBusManager.BUS_NAME +
				",dep." + TableLfDep.DEP_NAME +
				",CASE(mttask."+ TableLfMttask.MSG_TYPE +") WHEN 99 THEN lfbatch." + TableLfTempImportBatch.TM_NAME +
				" ELSE template."+ TableLfTemplate.TM_NAME +" END AS TM_NAME,template." + TableLfTemplate.TM_ID +
				",template." + TableLfTemplate.DEGREE +
				",rmsctrl." + TableLfRmsTaskCtrl.CURRENT_COUNT;
	}

	public static String getDominationSql(Long userId) {
		return " AND mttask."+ TableLfMttask.USER_ID +" IN "
				+ "(SELECT USER_ID FROM LF_SYSUSER WHERE DEP_ID IN (SELECT DEP_ID FROM LF_DOMINATION WHERE USER_ID ="
				+ userId + "))";
	}

	public static String getDominationSql2(Long userId) {
		return " AND mttask." + TableLfMttask.USER_ID + "=" + userId;
	}

    public static String getConditionSql(LfMttaskVo mtVo) {
		StringBuilder sql = new StringBuilder();
		//信息类型 21表示富信
		sql.append(" WHERE mttask.").append(TableLfMttask.MS_TYPE).append("= 21");
		//企业编码
		if(StringUtils.isNotEmpty(mtVo.getCorpCode())){
			sql.append(" AND mttask.").append(TableLfMttask.CORP_CODE)
					.append("='").append(mtVo.getCorpCode()).append("'");
		}
		// sp账号
		if (StringUtils.isNotEmpty(mtVo.getSpUser())) {
			// 如果是db2 oracle 则大写小写都加一份 兼容小写
			Boolean isDB2OrOracle = (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE || StaticValue.DBTYPE == StaticValue.DB2_DBTYPE);
			if ("1".equals(SystemGlobals.getSysParam("SPUSER_ISLWR")) && isDB2OrOracle) {
				String spUser = mtVo.getSpUser();
				spUser = spUser.toUpperCase() + "','" + spUser.toLowerCase();
				sql.append(" AND mttask.").append(TableLfMttask.SP_USER).append(" in ('").append(spUser).append("') ");
			} else {
				sql.append(" AND mttask.").append(TableLfMttask.SP_USER).append(" = '").append(mtVo.getSpUser()).append("'");
			}
		}
		// 发送主题
		if (StringUtils.isNotEmpty(mtVo.getTitle())) {
			sql.append(" AND mttask.").append(TableLfMttask.TITLE).append(" like '%").append(mtVo.getTitle()).append("%'");
		}
		// 富信主题
		if (StringUtils.isNotEmpty(mtVo.getTmName())) {
			sql.append(" AND template.").append(TableLfTemplate.TM_NAME).append(" like '%").append(mtVo.getTmName()).append("%'");
		}
		//模板Id
		if (mtVo.getTempId() != null) {
			sql.append(" AND mttask.").append(TableLfMttask.TEMP_ID).append(" = ").append(mtVo.getTempId());
		}
		//任务批次
		if (mtVo.getTaskId() != null) {
			sql.append(" AND mttask.").append(TableLfMttask.TASKID).append(" = ").append(mtVo.getTaskId());
		}
		//档位
		if (mtVo.getDegree() != null) {
			sql.append(" AND template.").append(TableLfTemplate.DEGREE).append(" = ").append(mtVo.getDegree());
		}
		IGenericDAO genericDao = new DataAccessDriver().getGenericDAO();
		// 指定时间段，开始时间 --群发任务
		if (StringUtils.isNotEmpty(mtVo.getStartSendTime())) {
			sql.append(" AND mttask.").append(TableLfMttask.TIMER_TIME).append(">=").append(genericDao.getTimeCondition(mtVo.getStartSendTime()));
		}
		// 指定时间段，结束时间 --群发任务
		if (StringUtils.isNotEmpty(mtVo.getEndSendTime())) {
			sql.append(" AND mttask.").append(TableLfMttask.TIMER_TIME).append("<=").append(genericDao.getTimeCondition(mtVo.getEndSendTime()));
		}
		// 指定时间段，开始时间 --群发历史
		if (StringUtils.isNotEmpty(mtVo.getStartSubTime())) {
			sql.append(" AND mttask.").append(TableLfMttask.SUBMIT_TIME).append(">=").append(genericDao.getTimeCondition(mtVo.getStartSubTime()));
		}
		// 指定时间段，结束时间 --群发历史
		if (StringUtils.isNotEmpty(mtVo.getEndSubTime())) {
			sql.append(" AND mttask.").append(TableLfMttask.SUBMIT_TIME).append("<=").append(genericDao.getTimeCondition(mtVo.getEndSubTime()));
		}
		//任务状态
		if (mtVo.getTaskState() != null) {
			switch (mtVo.getTaskState()) {
				case 1:
					//提交成功
					sql.append(" AND mttask.").append(TableLfMttask.SEND_STATE).append(" = 1 ");
					break;
				case 2:
					//全部提交失败
					sql.append(" AND mttask.").append(TableLfMttask.SEND_STATE).append(" = 2 and rmsctrl.").append(TableLfRmsTaskCtrl.CURRENT_COUNT).append(" = 0");
					break;
				case 3:
					//定时中
					sql.append(" AND mttask.").append(TableLfMttask.TIMER_STATUS).append(" = 1 and mttask.").append(TableLfMttask.SEND_STATE).append(" = 0 and mttask.").append(TableLfMttask.SUB_STATE).append(" <> 3 ");
					break;
				case 4:
					//已撤销
					sql.append(" AND mttask.").append(TableLfMttask.TIMER_STATUS).append(" = 1 and mttask.").append(TableLfMttask.SUB_STATE).append(" = 3 ");
					break;
				case 5:
					//超时未提交
					sql.append(" AND mttask.").append(TableLfMttask.SEND_STATE).append(" = 5 ");
					break;
				case 6:
					//已冻结
					sql.append(" AND mttask.").append(TableLfMttask.SUB_STATE).append(" = 4 ");
					break;
				case 7:
					sql.append(" AND mttask.").append(TableLfMttask.SEND_STATE).append(" = 2 AND rmsctrl.").append(TableLfRmsTaskCtrl.CURRENT_COUNT).append(" > 0");
					break;
				default:
					//do Nothing
			}
		}
		// 操作员
		if (StringUtils.isNotEmpty(mtVo.getUserIds())) {
			sql.append(" AND mttask.").append(TableLfMttask.USER_ID).append(" in (").append(mtVo.getUserIds()).append(")");
		}
		//机构
		if (StringUtils.isNotEmpty(mtVo.getDepIds())) {
			sql.append(" AND dep.").append(mtVo.getDepIds());
		}
		return sql.toString();
    }

	public static String getOrderBySql() {
		return " ORDER BY mttask." + TableLfMttask.MT_ID + " DESC";
	}

	public static String getSendDetailFieldSql() {
		return "SELECT mt." + TableMtTask01_12.UNICOM +
				",mt." + TableMtTask01_12.PHONE + ",mt." +
				TableMtTask01_12.ERROR_CODE2 + ",mt." +
				TableMtTask01_12.ERRO_RCODE;
	}

	public static String getSendDetailTableSql(String tableName) {
		return " FROM " + tableName + " mt " + StaticValue.getWITHNOLOCK();
	}

	public static String getSendDetailConditionSql(LinkedHashMap<String,String> conditionMap) {
		StringBuilder conditionSql = new StringBuilder();
		//任务批次
		if (conditionMap.containsKey("taskId")) {
			conditionSql.append(" WHERE mt.").append(TableMtTask01_12.TASK_ID).append("=").append(conditionMap.get("taskId"));
		} else if (conditionMap.containsKey("batchId")) {
			conditionSql.append(" WHERE mt.").append(TableMtTask01_12.BATCHID).append("=").append(conditionMap.get("batchId"));
		}
		//手机
		String phone = conditionMap.get("phone");
		if (StringUtils.isNotEmpty(phone)) {
			conditionSql.append(" and mt.").append(TableMtTask01_12.PHONE).append(" like '%").append(phone).append("%'");
		}
		//运营商
		String unicom = conditionMap.get("unicom");
		if (StringUtils.isNotEmpty(unicom)) {
			conditionSql.append(" and mt.").append(TableMtTask01_12.UNICOM).append("=").append(unicom);
		}
		//下载状态
		String downStatus = conditionMap.get("errorcode2");
		if (StringUtils.isNotEmpty(downStatus)) {
			if("1".equals(downStatus)){
				//成功
				conditionSql.append(" AND mt.").append(TableMtTask01_12.ERROR_CODE2).append("='DELIVRD'");
			}else if("2".equals(downStatus)){
				//失败
				conditionSql.append(" AND mt.").append(TableMtTask01_12.ERROR_CODE2).append("!='DELIVRD'");
				conditionSql.append(" AND mt.").append(TableMtTask01_12.ERROR_CODE2).append("!=' '");
				conditionSql.append(" AND mt.").append(TableMtTask01_12.ERROR_CODE2).append(" is not null");
			}else if("3".equals(downStatus)){
				//未返
				conditionSql.append(" AND mt.").append(TableMtTask01_12.ERROR_CODE2).append("=' '");
			}
		}
		//接收状态
		String recvStatus = conditionMap.get("errorcode");
		if (StringUtils.isNotEmpty(recvStatus)) {
			if("-1".equals(recvStatus)){
				//查询未返加成功
				conditionSql.append(" AND (mt.").append(TableMtTask01_12.ERRO_RCODE).append("=' ' or mt.").
						append(TableMtTask01_12.ERRO_RCODE).append("='DELIVRD')");
			}else if("1".equals(recvStatus)){
				//成功
				conditionSql.append(" AND mt.").append(TableMtTask01_12.ERRO_RCODE).append("='DELIVRD'");
			}else if("2".equals(recvStatus)){
				//失败
				conditionSql.append(" AND mt.").append(TableMtTask01_12.ERRO_RCODE).append("!='DELIVRD'");
				conditionSql.append(" AND mt.").append(TableMtTask01_12.ERRO_RCODE).append("!=' '");
				conditionSql.append(" AND mt.").append(TableMtTask01_12.ERRO_RCODE).append(" is not null");
			}else if("3".equals(recvStatus)){
				//未返
				conditionSql.append(" AND mt.").append(TableMtTask01_12.ERRO_RCODE).append("=' '");
			}else if("4".equals(recvStatus)){
				//接收失败
				conditionSql.append(" AND mt.").append(TableMtTask01_12.ERRO_RCODE).append(" NOT LIKE 'E1:%' AND mt.")
						.append(TableMtTask01_12.ERRO_RCODE).append(" NOT LIKE 'E2:%' AND mt.")
						.append(TableMtTask01_12.ERRO_RCODE).append(" NOT IN ('DELIVRD' ,' ')");
			}else if("5".equals(recvStatus)){
				//提交失败
				conditionSql.append(" AND (mt.").append(TableMtTask01_12.ERRO_RCODE).append(" LIKE 'E1:%' OR mt." )
						.append(TableMtTask01_12.ERRO_RCODE).append(" LIKE 'E2:%' )");
			}
		}
		return conditionSql.toString();
	}
}
