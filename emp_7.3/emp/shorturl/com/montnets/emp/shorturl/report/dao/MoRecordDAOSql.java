package com.montnets.emp.shorturl.report.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.shorturl.report.vo.ReplyDetailVo;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.engine.TableLfMotask;
import com.montnets.emp.table.query.TableMoTask;
import com.montnets.emp.table.sms.TableMtTask;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import org.apache.commons.beanutils.DynaBean;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MoRecordDAOSql {

	public static String getAllReplyDetailFieldSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("select motask.").append(TableLfMotask.PHONE).
			append(",motask.").append(TableLfMotask.DELIVERTIME).
			append(",motask.").append(TableLfMotask.MSGCONTENT).
			append(",(select TOP 1 ").append(TableLfSysuser.NAME).
			append(" from (select ").append(TableLfSysuser.NAME).
			append(" from ").append(TableLfSysuser.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).
			append(" where ").append(TableLfSysuser.MOBILE).
			append("= ").append("motask.").append(TableLfMotask.PHONE).
			append(" union all ").append("select ").append(TableLfClient.NAME).
			append(" from ").append(TableLfClient.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).
			append(" where ").append(TableLfClient.MOBILE).
			append("= ").append("motask.").append(TableLfMotask.PHONE).
			append(" union all ").append("select ").append(TableLfEmployee.NAME).
			append(" from ").append(TableLfEmployee.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).
			append(" where ").append(TableLfEmployee.MOBILE).append("= ").
			append("motask.").append(TableLfMotask.PHONE).
			append(") name) REPLYNAME");
		return sql.toString();
	}

	public static String getAllReplyDetailConnditionSql(ReplyDetailVo replyDetailVo) {
		if(replyDetailVo == null){
			EmpExecutionContext.error("企业短链报表查询-批次发送统计-回复详情查询异常，传入参数有误，replyDetailVo为null");
			return "";
		}
		StringBuffer sql = new StringBuffer();
		boolean hasWhere = false;
		//手机号
		if(replyDetailVo.getPhone() != null && !"".equals(replyDetailVo.getPhone())){
			hasWhere = addWhereOrAnd(sql,hasWhere);
			sql.append(TableLfMotask.PHONE).append("='").append(replyDetailVo.getPhone()).append("'");
		}
		//回复内容
		if(replyDetailVo.getMsgContent() != null && !"".equals(replyDetailVo.getMsgContent())){
			hasWhere = addWhereOrAnd(sql,hasWhere);
			sql.append(TableLfMotask.MSGCONTENT).append(" like '%").append(replyDetailVo.getMsgContent()).append("%'");
		}
		//回复名字
		if(replyDetailVo.getReplyName() != null && !"".equals(replyDetailVo.getReplyName())){
			hasWhere = addWhereOrAnd(sql,hasWhere);
			sql.append("REPLYNAME").append(" like '%").append(replyDetailVo.getReplyName()).append("%'");
		}
		return sql.toString();
	}

	public static String getAllReplyDetailTableSql(ReplyDetailVo replyDetailVo) {
		if(replyDetailVo == null || replyDetailVo.getTaskId() == null
				|| replyDetailVo.getSendTime() == null || "".equals(replyDetailVo.getTaskId())){
			EmpExecutionContext.error("企业短链报表查询-批次发送统计-发送详情查询异常，传入参数有误，关键参数为null");
			return "";
		}
		StringBuffer sql = new StringBuffer();

		//根据发送时间查询相应的年月表(MOTASKYYYYMM)与实时表(LF_MOTASK)
		//短链的有效期最长为一个月，最多只查询发送时间当月与下月的年月表
		String yyyyMM = new SimpleDateFormat("yyyyMM").format(replyDetailVo.getSendTime().getTime());
		String currentTableName = "MOTASK" + yyyyMM;
		Integer month = Integer.parseInt(yyyyMM.substring(4)) + 1;
		String nextTableName = "MOTASK" + yyyyMM.substring(0,4) + String.format("%02d",month);

		String currentMotaskTable = getUnionPartSql(currentTableName,replyDetailVo,"a");
		String nextMotaskTable = getUnionPartSql(nextTableName,replyDetailVo,"b");
		String MotaskTable = getUnionPartSql(TableMoTask.TABLE_NAME,replyDetailVo,"c");
		//先根据下行批次查询通道号与子号(存在replyDetailVo对象中)，再以这些对应上行查找

		sql.append(" from (").append(currentMotaskTable).append(" union all ").append(nextMotaskTable).append(" union all ").append(MotaskTable).append(")motask");

		return sql.toString();
	}

	/**
	 * 联合查询三个表，先分别得到每一个表，再用union all 拼接
	 * @param tableName
	 * @param replyDetailVo
	 * @param alias
	 * @return
	 */
	private static String getUnionPartSql(String tableName,ReplyDetailVo replyDetailVo,String alias){
		/*
		 SELECT a.PHONE,a.DELIVERTIME,a.MSGCONTENT,(select TOP 1 NAME FROM (
     SELECT NAME FROM LF_SYSUSER WHERE MOBILE = a.PHONE union all
     SELECT NAME FROM LF_CLIENT WHERE PHONE = a.PHONE union all
     SELECT NAME FROM LF_EMPLOYEE WHERE MOBILE = a.PHONE) name) REPLYNAME FROM LF_MOTASK a WHERE TASK_ID = 123456
		 */
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(alias).append(".").append(TableLfMotask.PHONE).
			append(",").append(alias).append(".").append(TableLfMotask.DELIVERTIME).
			append(",").append(alias).append(".").append(TableLfMotask.MSGCONTENT).
			append(" from ").append(tableName).
			append(" ").append(alias).append(StaticValue.getWITHNOLOCK()).append(" where ").
			append(TableMoTask.SPNUMBER).append("='").append(replyDetailVo.getSpNumber()).append("'");
		return sql.toString();
	}

	private static boolean addWhereOrAnd(StringBuffer sql, boolean hasWhere) {
		if(hasWhere) {
			//有where则加and
			sql.append(" AND ");
		} else {
			//没where则加where
			sql.append(" WHERE ");
		}
		return true;
	}

	public static String getSpNumberFieldSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("select TOP 1 mttask.").append(TableMtTask.SPGATE).
			append(",mttask.").append(TableMtTask.CPNO);
		return sql.toString();
	}

	public static String getSpNumberTableSql(ReplyDetailVo replyDetailVo) {
		StringBuffer sql = new StringBuffer();
		StringBuffer field = new StringBuffer().append(" select ").append(TableMtTask.CPNO).
				append(",").append(TableMtTask.SPGATE).append(" from ");
		String yyyyMM = new SimpleDateFormat("yyyyMM").format(replyDetailVo.getSendTime().getTime());
		String mttaskName = "MTTASK" + yyyyMM;
		sql.append(" from (").append(field).
			append(mttaskName).append(" where ").
			append(TableMtTask.TASK_ID).append("=").
			append(replyDetailVo.getTaskId()).append(" union all ").
			append(field).append(" GW_MT_TASK_BAK ").append(" where ").
			append(TableMtTask.TASK_ID).append("=").
			append(replyDetailVo.getTaskId()).append(")mttask");
		return sql.toString();
	}
}
