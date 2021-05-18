package com.montnets.emp.perfect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.system.LfMotask;
import com.montnets.emp.perfect.vo.PerfectNoticeVo;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.employee.TableLfEmpDepConn;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.employee.TableLfEmployeeDep;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.group.TableLfMalist;
import com.montnets.emp.table.group.TableLfUdgroup;
import com.montnets.emp.table.perfect.TableLfPerfectNotic;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

public class PerfectDao extends SuperDAO
{

	
    public List<GroupInfoVo> findGroupInfoVo(Long loginUserID, GroupInfoVo groupInfoVo, PageInfo pageInfo) throws Exception
	{
		// 查询字段拼接
		String fieldSql = GroupInfoVoSQL.getFieldSql();
		// 查询表名拼接
		String tableSql = GroupInfoVoSQL.getTableSql();
		// 查询条件拼接
		String conditionSql = GroupInfoVoSQL.getConditionSql(groupInfoVo);
		// 排序条件拼接
		String orderBySql = GroupInfoVoSQL.getOrderBySql();

		// sql拼接
		String sql = new StringBuffer(fieldSql).append(tableSql).append(conditionSql).append(orderBySql).toString();

		// 记录数sql
		String countSql = new StringBuffer("select count(*) totalcount").append(tableSql).append(conditionSql).toString();
		List<GroupInfoVo> returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(GroupInfoVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		// 返回结果
		return returnVoList;
	}

	/**
	 * 完美通知 获取该群组下的成员
	 * 
	 * @param loginUserID
	 * @param groupInfoVo
	 * @return
	 * @throws Exception
	 */
	
    public List<GroupInfoVo> findGroupUserByIds(Long groupId) throws Exception
	{
		// 初始化LIST
		List<GroupInfoVo> returnVoList = null;
		// 拼凑查询字段
		StringBuffer sqlStr = new StringBuffer("select  list2gro.").append(TableLfList2gro.UDG_ID).append(",list2gro.").append(TableLfList2gro.L2G_ID).append(",list2gro.").append(TableLfList2gro.L2G_TYPE).append(",list2gro.").append(TableLfList2gro.GUID).append(",udgroup.").append(TableLfUdgroup.GP_ATTRIBUTE).append(",udgroup.").append(TableLfUdgroup.GROUP_TYPE).append(",udgroup.").append(TableLfUdgroup.USER_ID).append(",case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(") when 0 then employee.").append(TableLfEmployee.MOBILE).append("  else malist.").append(TableLfMalist.MOBILE).append(" end as MOBILE,case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(") when 0 then employee.").append(TableLfEmployee.NAME).append("  else malist.").append(TableLfMalist.NAME).append(" end as NAME,udgroup.").append(TableLfUdgroup.UDG_NAME);
		// 拼凑TABLE
		sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro ").append(StaticValue.getWITHNOLOCK()).append(" left join ").append(TableLfEmployee.TABLE_NAME).append(" employee  ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID).append("=employee.").append(TableLfEmployee.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(" malist ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID).append("=malist.").append(TableLfMalist.GUID).append(" inner join ").append(TableLfUdgroup.TABLE_NAME).append(" udgroup ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID).append("=udgroup.").append(TableLfUdgroup.GROUP_ID).append(" where udgroup.").append(TableLfUdgroup.UDG_ID).append("=").append(groupId).append(" order by NAME asc");
		// 查询
		returnVoList = findVoListBySQL(GroupInfoVo.class, sqlStr.toString(), StaticValue.EMP_POOLNAME);
		// 返回
		return returnVoList;
	}

	/**
	 * 增加分页 查询员工群组中的内容
	 * 
	 * @param groupId
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	
    public List<GroupInfoVo> findGroupUserByIds(Long groupId, String searchname, PageInfo pageInfo) throws Exception
	{
		// 初始化LIST
		List<GroupInfoVo> returnVoList = null;
		// 拼凑查询字段
		StringBuffer sqlStr = new StringBuffer("select  list2gro.").append(TableLfList2gro.UDG_ID).append(",list2gro.").append(TableLfList2gro.L2G_ID).append(",list2gro.").append(TableLfList2gro.L2G_TYPE).append(",list2gro.").append(TableLfList2gro.GUID).append(",udgroup.").append(TableLfUdgroup.GP_ATTRIBUTE).append(",udgroup.").append(TableLfUdgroup.GROUP_TYPE).append(",udgroup.").append(TableLfUdgroup.USER_ID).append(",case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(") when 0 then employee.").append(TableLfEmployee.MOBILE).append("  else malist.").append(TableLfMalist.MOBILE).append(" end as MOBILE,case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(") when 0 then employee.").append(TableLfEmployee.NAME).append("  else malist.").append(TableLfMalist.NAME).append(" end as NAME,udgroup.").append(TableLfUdgroup.UDG_NAME);
		// 拼凑TABLE
		sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro ").append(StaticValue.getWITHNOLOCK()).append(" left join ").append(TableLfEmployee.TABLE_NAME).append(" employee  ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID).append("=employee.").append(TableLfEmployee.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(" malist ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID).append("=malist.").append(TableLfMalist.GUID).append(" inner join ").append(TableLfUdgroup.TABLE_NAME).append(" udgroup ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID).append("=udgroup.").append(TableLfUdgroup.GROUP_ID).append(" where udgroup.").append(TableLfUdgroup.UDG_ID).append("=").append(groupId);

		if(searchname != null && !"".equals(searchname))
		{
			sqlStr.append(" and (case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(") when 0 then employee.").append(TableLfEmployee.NAME).append("  else malist.").append(TableLfMalist.NAME).append(" end )").append(" like '%").append(searchname).append("%'");
		}

		// 查询
		String countSql = "select count(*) totalcount from (" + sqlStr.toString() + ") a";
		sqlStr.append(" order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc");
		returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(GroupInfoVo.class, sqlStr.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		// 返回
		return returnVoList;
	}

	
    public List<GroupInfoVo> findGroupClientByIds(Long groupId, PageInfo pageInfo) throws Exception
	{
		// 初始化LIST
		List<GroupInfoVo> returnVoList = null;
		// 拼凑查询字段
		StringBuffer sqlStr = new StringBuffer("select  list2gro.").append(TableLfList2gro.UDG_ID).append(",list2gro.").append(TableLfList2gro.L2G_ID).append(",list2gro.").append(TableLfList2gro.L2G_TYPE).append(",list2gro.").append(TableLfList2gro.GUID).append(",udgroup.").append(TableLfUdgroup.GP_ATTRIBUTE).append(",udgroup.").append(TableLfUdgroup.GROUP_TYPE).append(",udgroup.").append(TableLfUdgroup.USER_ID).append(",case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(") when 1 then client.").append(TableLfClient.MOBILE).append("  else malist.").append(TableLfMalist.MOBILE).append(" end as MOBILE,case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(") when 1 then client.").append(TableLfClient.NAME).append("  else malist.").append(TableLfMalist.NAME).append(" end as NAME,udgroup.").append(TableLfUdgroup.UDG_NAME);
		// 拼凑TABLE
		sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro ").append(StaticValue.getWITHNOLOCK()).append(" left join ").append(TableLfClient.TABLE_NAME).append(" client  ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID).append("=client.").append(TableLfClient.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(" malist ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID).append("=malist.").append(TableLfMalist.GUID).append(" inner join ").append(TableLfUdgroup.TABLE_NAME).append(" udgroup ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID).append("=udgroup.").append(TableLfUdgroup.UDG_ID).append(" where udgroup.").append(TableLfUdgroup.UDG_ID).append("=").append(groupId);
		// 查询
		String countSql = "select count(*) totalcount from ( " + sqlStr.toString() + " ) a";
		sqlStr.append(" order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc ");

		returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(GroupInfoVo.class, sqlStr.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		// 返回
		return returnVoList;
	}

	/**
	 * 根据部门的DEPPATH 查询出该机构下所有的员工，包括子机构的
	 * 
	 * @param depPath
	 * @return
	 * @throws Exception
	 */
	
    public List<LfEmployee> findEmployeeByDepPath(String depPath) throws Exception
	{
		// select * from lf_employee e where e.dep_id in (select led.dep_id from
		// lf_employee_dep led where led.dep_path like '2861/%');
		// 拼接sql
		String sql = "select * from " + TableLfEmployee.TABLE_NAME + " e  where e." + TableLfEmployee.DEP_ID + " in ( select led." + TableLfEmployeeDep.DEP_ID + " from " + TableLfEmployeeDep.TABLE_NAME + " led where " + " led." + TableLfEmployeeDep.DEP_PATH + " like '" + depPath + "%')";
		// 返回结果
		return findEntityListBySQL(LfEmployee.class, sql, StaticValue.EMP_POOLNAME);
	}

	// 查询完美通知详情记录
	
    public List<PerfectNoticeVo> findlfImPerfectNoticeVos(String corpCode, String senderName, String sendType, String content, String beginTime, String endTime, Long userId, PageInfo pageInfo) throws Exception
	{
		List<PerfectNoticeVo> noticeVos = null;
		// 做数据库兼容
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
		{
			// ORACLE
			noticeVos = findlfImPerfectNoticeVosOracle(corpCode, senderName, sendType, content, beginTime, endTime, userId, pageInfo);
		}
		else
			if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				// SQLSERVER
				noticeVos = findlfImPerfectNoticeVosSqlServer(corpCode, senderName, sendType, content, beginTime, endTime, userId, pageInfo);
			}
			else
				if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
				{
					// DB2
					noticeVos = findlfImPerfectNoticeVosDB2(corpCode, senderName, sendType, content, beginTime, endTime, userId, pageInfo);
				}
				else
					if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
					{
						// MYSQL
						noticeVos = findlfImPerfectNoticeVosMySql(corpCode, senderName, sendType, content, beginTime, endTime, userId, pageInfo);
					}
		return noticeVos;
	}

	/**
	 * 完美通知ORALCE详情
	 * 
	 * @param corpCode
	 *        企业编码
	 * @param senderName
	 *        发送者名称
	 * @param sendType
	 *        发送类型
	 * @param content
	 *        详情
	 * @param beginTime
	 *        时间段
	 * @param endTime
	 * @param userId
	 *        发送者用户ID
	 * @param pageInfo
	 *        分页信息
	 * @return
	 * @throws Exception
	 */
	private List<PerfectNoticeVo> findlfImPerfectNoticeVosOracle(String corpCode, String senderName, String sendType, String content, String beginTime, String endTime, Long userId, PageInfo pageInfo) throws Exception
	{
		// 编写SQL
		// 查询SQL
		String sql = new StringBuffer("select * from ( select pn.*,sysuser.").append(TableLfSysuser.NAME)
		.append(" from ").append(TableLfPerfectNotic.TABLE_NAME).append(" pn ").append(" left join ")
		.append(TableLfSysuser.TABLE_NAME).append(" sysuser on pn.")
		.append(TableLfPerfectNotic.SENDER_GUID).append(" = sysuser.").append(TableLfSysuser.GUID)
		.append(" where (sysuser.").append(TableLfSysuser.USER_ID).append(" = ")
		.append(userId).append(" or sysuser.").append(TableLfSysuser.DEP_ID)
		.append(" in (select dom.").append(TableLfDomination.DEP_ID).append(" from ")
		.append(TableLfDomination.TABLE_NAME).append(" dom where dom.").append(TableLfDomination.USER_ID)
		.append("=").append(userId).append(")) and pn.").append(TableLfPerfectNotic.CORP_CODE)
		.append(" = '").append(corpCode).append("'").toString();
		// 统计SQL
		String countSql = "select count(*) totalcount from (";
		// 拼接
		countSql += sql;
		// 初始化
		StringBuffer conditionSql = new StringBuffer();
		// 判断发送类型
		if(sendType != null && !"".equals(sendType))
		{
			// 转化
			Integer type = Integer.valueOf(sendType);
			// 拼接
			conditionSql.append(" and pn.").append(TableLfPerfectNotic.RECEIVER_TYPE);
			// 对比
			if(type < 2)
			{
				// 条件
				conditionSql.append(" < 2");
			}
			else
			{
				// 条件
				conditionSql.append(" = ").append(type);
			}
		}
		// 内容
		// 条件
		if(content != null && !"".equals(content))
		{
			conditionSql.append(" and pn.").append(TableLfPerfectNotic.CONTENT).append(" like '%").append(content).append("%'");
		}
		// 发送者名称
		if(senderName != null && !"".equals(senderName))
		{
			// 条件
			conditionSql.append(" and sysuser.").append(TableLfSysuser.NAME).append(" like '%").append(senderName).append("%'");
		}
		// 开始时间
		if(beginTime == null || "".equals(beginTime))
		{
			beginTime = "1970-1-1 0:0:0";
		}
		// 结束时间
		if(endTime == null || "".equals(endTime))
		{
			endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		}
		// 开始时间 与结束时间
		// 拼接
		if((beginTime != null && !"".equals(beginTime)) || (endTime != null && !"".equals(endTime)))
		{
			conditionSql.append(" and ").append(TableLfPerfectNotic.SUBMITTIME).append(" BETWEEN to_date('").append(beginTime).append("','yyyy-MM-dd HH24:mi:ss') AND to_date('").append(endTime).append("','yyyy-MM-dd HH24:mi:ss')");

		}
		// 条件
		conditionSql.append(")  order by ").append(TableLfPerfectNotic.SUBMITTIME).append(" DESC ");
		// 连接
		sql += conditionSql;
		// 条件
		countSql += conditionSql + ")";
		// 查询
		List<PerfectNoticeVo> returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(PerfectNoticeVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		// 返回
		return returnVoList;
	}

	/**
	 * SQLSERVER后加USERID
	 * 
	 * @param corpCode
	 *        企业编码
	 * @param senderName
	 *        操作员名称
	 * @param sendType
	 *        发送类型
	 * @param content
	 *        发送内容
	 * @param beginTime
	 *        时间段
	 * @param endTime
	 * @param userId
	 *        操作员用户ID
	 * @param pageInfo
	 *        分页信息
	 * @return
	 * @throws Exception
	 */
	private List<PerfectNoticeVo> findlfImPerfectNoticeVosSqlServer(String corpCode, String senderName, String sendType, String content, String beginTime, String endTime, Long userId, PageInfo pageInfo) throws Exception
	{
		// 拼接
		String sql = new StringBuffer("select TOP (100) PERCENT derivedtbl_2.* from ( select pn.*,")
		.append(" sysuser.").append(TableLfSysuser.NAME).append(" from ").append(TableLfPerfectNotic.TABLE_NAME)
		.append(" pn ").append(" left join ").append(TableLfSysuser.TABLE_NAME).append(" sysuser on pn.").append(TableLfPerfectNotic.SENDER_GUID)
		.append(" = sysuser.").append(TableLfSysuser.GUID).append(" where (sysuser.").append(TableLfSysuser.USER_ID)
		.append(" = ").append(userId).append(" or sysuser.").append(TableLfSysuser.DEP_ID)
		.append(" in (select dom.").append(TableLfDomination.DEP_ID).append(" from ")
		.append(TableLfDomination.TABLE_NAME).append(" dom where dom.").append(TableLfDomination.USER_ID)
		.append("=").append(userId).append(")) and pn.").append(TableLfPerfectNotic.CORP_CODE).append(" = '")
		.append(corpCode).append("'").toString();
		// 统计
		String countSql = "select count(*) totalcount from (";
		// 拼接
		countSql += sql;
		// 初始化
		StringBuffer conditionSql = new StringBuffer();
		// 发送类型条件判断
		if(sendType != null && !"".equals(sendType))
		{
			Integer type = Integer.valueOf(sendType);
			conditionSql.append(" and pn.").append(TableLfPerfectNotic.RECEIVER_TYPE);
			// 条件判断 ，
			if(type < 2)
			{
				conditionSql.append(" < 2");
			}
			else
			{
				conditionSql.append(" = ").append(type);
			}
		}
		// 条件 内容 拼接
		if(content != null && !"".equals(content))
		{
			// 模糊查询
			conditionSql.append(" and pn.").append(TableLfPerfectNotic.CONTENT).append(" like '%").append(content).append("%'");
		}
		// 发送名称 条件
		if(senderName != null && !"".equals(senderName))
		{
			// 模糊查询
			conditionSql.append(" and sysuser.").append(TableLfSysuser.NAME).append(" like '%").append(senderName).append("%'");
		}
		// 起始时间拼接
		if(beginTime == null || "".equals(beginTime))
		{
			beginTime = "1970-1-1 0:0:0";
		}
		// 结束时间
		if(endTime == null || "".equals(endTime))
		{
			endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		}
		// 开始时间 结束时间拼接条件
		if((beginTime != null && !"".equals(beginTime)) || (endTime != null && !"".equals(endTime)))
		{
			conditionSql.append(" and ").append(TableLfPerfectNotic.SUBMITTIME).append(" BETWEEN '").append(beginTime).append("' AND '").append(endTime).append("'");

		}
		// 条件
		conditionSql.append(")derivedtbl_2  order by ").append(TableLfPerfectNotic.P_NOTIC_ID).append(" DESC ");
		// 拼接SQL
		sql += conditionSql;
		countSql += conditionSql + ")derivedtbl_1";
		// 查询
		/*
		 * List<PerfectNoticeVo> returnVoList = new
		 * DataAccessDriver().getGenericDAO()
		 * .findPageVoListBySQL(PerfectNoticeVo.class, sql, countSql,
		 * pageInfo, StaticValue.EMP_POOLNAME);
		 */
		List<PerfectNoticeVo> returnVoList = findPerPageVoListBySQL(PerfectNoticeVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null, null);
		// 返回
		return returnVoList;
	}

	/**
	 * 完美通知DB2详情
	 * 
	 * @param corpCode
	 *        企业编码
	 * @param senderName
	 *        发送者名称
	 * @param sendType
	 *        发送类型
	 * @param content
	 *        详情
	 * @param beginTime
	 *        时间段
	 * @param endTime
	 * @param userId
	 *        发送者用户ID
	 * @param pageInfo
	 *        分页信息
	 * @return
	 * @throws Exception
	 */
	private List<PerfectNoticeVo> findlfImPerfectNoticeVosDB2(String corpCode, String senderName, String sendType, String content, String beginTime, String endTime, Long userId, PageInfo pageInfo) throws Exception
	{
		// 拼接SQL
		String sql = new StringBuffer("select * from ( select pn.*,sysuser.")
		.append(TableLfSysuser.NAME).append(" from ").append(TableLfPerfectNotic.TABLE_NAME).append(" pn ")
		.append(" left join ").append(TableLfSysuser.TABLE_NAME).append(" sysuser on pn.")
		.append(TableLfPerfectNotic.SENDER_GUID).append(" = sysuser.").append(TableLfSysuser.GUID)
		.append(" where (sysuser.").append(TableLfSysuser.USER_ID).append(" = ").append(userId)
		.append(" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (select dom.")
		.append(TableLfDomination.DEP_ID).append(" from ").append(TableLfDomination.TABLE_NAME)
		.append(" dom where dom.").append(TableLfDomination.USER_ID).append("=").append(userId)
		.append(")) and pn.").append(TableLfPerfectNotic.CORP_CODE).append(" = '").append(corpCode).append("'").toString();
		// 统计SQL
		String countSql = "select count(*) totalcount from (";
		// 拼接
		countSql += sql;
		// 初始化
		StringBuffer conditionSql = new StringBuffer();
		// 发送类型
		if(sendType != null && !"".equals(sendType))
		{
			Integer type = Integer.valueOf(sendType);
			conditionSql.append(" and pn.").append(TableLfPerfectNotic.RECEIVER_TYPE);
			// 判断类型
			if(type < 2)
			{
				conditionSql.append(" < 2");
			}
			else
			{
				conditionSql.append(" = ").append(type);
			}
		}
		// 内容条件判断
		if(content != null && !"".equals(content))
		{
			// 模糊查询
			conditionSql.append(" and pn.").append(TableLfPerfectNotic.CONTENT).append(" like '%").append(content).append("%'");
		}
		// 发送者名称判断
		if(senderName != null && !"".equals(senderName))
		{
			// 模糊查询
			conditionSql.append(" and sysuser.").append(TableLfSysuser.NAME).append(" like '%").append(senderName).append("%'");
		}
		// 开始时间
		if(beginTime == null || "".equals(beginTime))
		{
			beginTime = "1970-1-1 00:00:00";
		}
		// 结束时间
		if(endTime == null || "".equals(endTime))
		{
			endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		}
		// 时间段拼接
		if((beginTime != null && !"".equals(beginTime)) || (endTime != null && !"".equals(endTime)))
		{
			conditionSql.append(" and ").append(TableLfPerfectNotic.SUBMITTIME).append(" BETWEEN '").append(beginTime).append("' AND '").append(endTime).append("'");

		}
		// 条件拼接
		conditionSql.append(") temp_t order by ").append(TableLfPerfectNotic.P_NOTIC_ID).append(" DESC ");
		// 拼接
		sql += conditionSql;
		// 拼接
		countSql += conditionSql + ")derivedtbl_1";
		// 查询
		List<PerfectNoticeVo> returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(PerfectNoticeVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		// 返回
		return returnVoList;
	}

	/**
	 * 完美通知MYSQL详情
	 * 
	 * @param corpCode
	 *        企业编码
	 * @param senderName
	 *        发送者名称
	 * @param sendType
	 *        发送类型
	 * @param content
	 *        详情
	 * @param beginTime
	 *        时间段
	 * @param endTime
	 * @param userId
	 *        发送者用户ID
	 * @param pageInfo
	 *        分页信息
	 * @return
	 * @throws Exception
	 */
	private List<PerfectNoticeVo> findlfImPerfectNoticeVosMySql(String corpCode, String senderName, String sendType, String content, String beginTime, String endTime, Long userId, PageInfo pageInfo) throws Exception
	{
		// 拼接SQL
		String sql = new StringBuffer("select derivedtbl_2.* from ( select pn.*,sysuser.").append(TableLfSysuser.NAME)
		.append(" from ").append(TableLfPerfectNotic.TABLE_NAME).append(" pn ").append(" left join ")
		.append(TableLfSysuser.TABLE_NAME).append(" sysuser on pn.")
		.append(TableLfPerfectNotic.SENDER_GUID).append(" = sysuser.").append(TableLfSysuser.GUID)
		.append(" where (sysuser.").append(TableLfSysuser.USER_ID).append(" = ").append(userId)
		.append(" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (select dom.").append(TableLfDomination.DEP_ID)
		.append(" from ").append(TableLfDomination.TABLE_NAME).append(" dom where dom.").append(TableLfDomination.USER_ID)
		.append("=").append(userId).append(")) and pn.").append(TableLfPerfectNotic.CORP_CODE).append(" = '")
		.append(corpCode).append("'").toString();
		// 统计SQL
		String countSql = "select count(*) totalcount from (";
		// 拼接
		countSql += sql;
		// 初始化
		StringBuffer conditionSql = new StringBuffer();
		// 发送类型判断
		if(sendType != null && !"".equals(sendType))
		{
			// 转化
			Integer type = Integer.valueOf(sendType);
			conditionSql.append(" and pn.").append(TableLfPerfectNotic.RECEIVER_TYPE);
			// 拼接
			if(type < 2)
			{
				conditionSql.append(" < 2");
			}
			else
			{
				conditionSql.append(" = ").append(type);
			}
		}
		// 发送内容 条件
		if(content != null && !"".equals(content))
		{
			// 模糊查询
			conditionSql.append(" and pn.").append(TableLfPerfectNotic.CONTENT).append(" like '%").append(content).append("%'");
		}
		// 发送名称 条件
		if(senderName != null && !"".equals(senderName))
		{
			// 模糊查询
			conditionSql.append(" and sysuser.").append(TableLfSysuser.NAME).append(" like '%").append(senderName).append("%'");
		}
		// 开始时间
		if(beginTime == null || "".equals(beginTime))
		{
			beginTime = "1970-1-1 0:0:0";
		}
		// 结束时间
		if(endTime == null || "".equals(endTime))
		{
			// 结束时间
			endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		}
		// 时间段
		if((beginTime != null && !"".equals(beginTime)) || (endTime != null && !"".equals(endTime)))
		{
			conditionSql.append(" and ").append(TableLfPerfectNotic.SUBMITTIME).append(" BETWEEN '").append(beginTime).append("' AND '").append(endTime).append("'");

		}
		// 条件拼接
		conditionSql.append(")derivedtbl_2 order by ").append(TableLfPerfectNotic.SUBMITTIME).append(" DESC ");
		// 拼接
		sql += conditionSql;
		// 拼接
		countSql += conditionSql + ")derivedtbl_1";
		// 查询
		List<PerfectNoticeVo> returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(PerfectNoticeVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		// 返回
		return returnVoList;
	}

	/**
	 * 获取员工机构列表——用于构建机构树
	 * 
	 * @param userId
	 *        当前操作员的ID
	 * @param depId
	 *        传NULL进来则通过userId去查，有传值时只查传入机构的子级
	 * @return 员工机构的集合
	 * @throws Exception
	 */
	
    public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId, String depId) throws Exception
	{
		String sql = "";
		List<LfEmployeeDep> lfEmployeeDepList = null;
		if(depId == null || "".equals(depId))
		{
			/*
			 * sql = new StringBuffer(" select e.* from ").append(
			 * TableLfEmployeeDep
			 * .TABLE_NAME).append(" e "+StaticValue.WITHNOLOCK+",").append(
			 * TableLfEmpDepConn
			 * .TABLE_NAME).append(" c "+StaticValue.WITHNOLOCK)
			 * .append(" where c.")
			 * .append(TableLfEmpDepConn.USER_ID).append(" = "
			 * ).append(userId).append(" and (c.")
			 * .append(TableLfEmpDepConn.DEP_ID
			 * ).append(" =e.").append(TableLfEmployeeDep.DEP_ID)
			 * .append(" or ")
			 * .append(TableLfEmployeeDep.PARENT_ID).append(" = c."
			 * ).append(TableLfEmpDepConn.DEP_ID) .append(")").toString();
			 */
			// 这里是为了解决员工机构关联表出现了多条记录的处理方法，产生2条记录的原因可能是机器卡，或者同步
			List<LfEmpDepConn> connList = null;
			StringBuffer sb = new StringBuffer();
			sb.append(" select * from ").append(TableLfEmpDepConn.TABLE_NAME).append(" c " + StaticValue.getWITHNOLOCK()).append(" where c.").append(TableLfEmpDepConn.USER_ID).append(" = ").append(userId);
			connList = findEntityListBySQL(LfEmpDepConn.class, sb.toString(), StaticValue.EMP_POOLNAME);
			// LfEmpDepConn conn = null;
			if(connList != null && connList.size() > 0)
			{
				// conn = connList.get(0);
				// Long id = conn.getDepId();
				String ids = "";
				for (LfEmpDepConn co : connList)
				{
					ids += co.getDepId() + ",";
				}
				ids = ids.substring(0, ids.lastIndexOf(","));
				sql = new StringBuffer(" select e.* from ").append(TableLfEmployeeDep.TABLE_NAME).append(" e " + StaticValue.getWITHNOLOCK()).append(" where  e.").append(TableLfEmployeeDep.DEP_ID).append(" in( ").append(ids).append(") or ").append(TableLfEmployeeDep.PARENT_ID).append(" in(").append(ids).append(") order by ").append(TableLfEmployeeDep.ADD_TYPE).append(" " + StaticValue.ASC).toString();
				lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql, StaticValue.EMP_POOLNAME);
			}
		}
		else
		{
			sql = new StringBuffer(" select * from ").append(TableLfEmployeeDep.TABLE_NAME).append(" " + StaticValue.getWITHNOLOCK()).append(" where ").append(TableLfEmployeeDep.PARENT_ID).append(" = ").append(depId).append(" order by ").append(TableLfEmployeeDep.ADD_TYPE).append(" " + StaticValue.ASC).toString();
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql, StaticValue.EMP_POOLNAME);
		}

		return lfEmployeeDepList;
	}
	
	
	/**
	 * 获取员工机构列表——用于构建机构树(重载方法)
	 * 
	 * @param userId
	 *        当前操作员的ID
	 * @param depId
	 *        传NULL进来则通过userId去查，有传值时只查传入机构的子级
	 * @param corpCode 企业编码       
	 * @return 员工机构的集合
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId, String depId,String corpCode) throws Exception
	{
		String sql = "";
		List<LfEmployeeDep> lfEmployeeDepList = null;
		if(depId == null || "".equals(depId))
		{
			// 这里是为了解决员工机构关联表出现了多条记录的处理方法，产生2条记录的原因可能是机器卡，或者同步
			List<LfEmpDepConn> connList = null;
			StringBuffer sb = new StringBuffer();
			sb.append(" select * from ").append(TableLfEmpDepConn.TABLE_NAME).append(" c " + StaticValue.getWITHNOLOCK()).append(" where c.").append(TableLfEmpDepConn.USER_ID).append(" = ").append(userId);
			connList = findEntityListBySQL(LfEmpDepConn.class, sb.toString(), StaticValue.EMP_POOLNAME);
			//权限必须大于0，才会去查询机构
			if(connList != null && connList.size() > 0)
			{
			    //机构ID字符串
				String ids = "";
				for (LfEmpDepConn co : connList)
				{
					ids += co.getDepId() + ",";
				}
				//去掉最后一个逗号
				ids = ids.substring(0, ids.lastIndexOf(","));
				//生成SQL语句，必须加企业编码
				sql = new StringBuffer(" select * from ").append(TableLfEmployeeDep.TABLE_NAME).append(" " + StaticValue.getWITHNOLOCK()).append(" where  (").append(TableLfEmployeeDep.DEP_ID).append(" in( ").append(ids).append(") or ").append(TableLfEmployeeDep.PARENT_ID).append(" in(").append(ids).append("))").append(" AND CORP_CODE='"+corpCode+"' ").append(" order by ").append(TableLfEmployeeDep.ADD_TYPE).append(" " + StaticValue.ASC).toString();
				//查询数据
				lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql, StaticValue.EMP_POOLNAME);
			}
		}
		else
		{
			//查询的SQL语句中增加企业编码
			sql = new StringBuffer(" select * from ").append(TableLfEmployeeDep.TABLE_NAME).append(" " + StaticValue.getWITHNOLOCK()).append(" where ").append(TableLfEmployeeDep.PARENT_ID).append(" = ").append(depId).append(" AND CORP_CODE='"+corpCode+"' ").append(" order by ").append(TableLfEmployeeDep.ADD_TYPE).append(" " + StaticValue.ASC).toString();
			//查询数据
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql, StaticValue.EMP_POOLNAME);
		}

		return lfEmployeeDepList;
	}

	private <T> List<T> findPerPageVoListBySQL(Class<T> voClass, String sql, String countSql, PageInfo pageInfo, String POOLNAME, List<String> timeList, Map<String, String> columns) throws Exception
	{
		// 返回LIST
		List<T> returnList = null;
		if(columns == null)
		{
			// 对象所对应的映射
			columns = getVoORMMap(voClass);
		}
		// 连接
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			// 获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			if(countSql != null)
			{
				EmpExecutionContext.sql("execute sql : " + countSql);
				ps = conn.prepareStatement(countSql);
				// 查询条件为时间
				if(timeList != null && timeList.size() > 0)
				{
					int psIndex = 0;
					for (int i = 0; i < timeList.size(); i++)
					{
						psIndex++;
						String time = timeList.get(i);
						ps.setTimestamp(psIndex, Timestamp.valueOf(time));
					}
				}
				// 执行SQL查询语句，返回ResultSet
				rs = ps.executeQuery();
				if(rs.next())
				{
					int pageSize = pageInfo.getPageSize();
					int totalCount = rs.getInt("totalcount");
					int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
					pageInfo.setTotalRec(totalCount);
					pageInfo.setTotalPage(totalPage);
					// 当前页大于总页数则跳转到第一页
					if(pageInfo.getPageIndex() > totalPage)
					{
						pageInfo.setPageIndex(1);
					}
				}
			}
			int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex() - (pageInfo.getPageSize() - 1);
			// 开始行数
			int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();
			// 结束行数
			StringBuffer sqlSb = new StringBuffer();
			sql = sql.substring(sql.indexOf("select") + 25, sql.length());
			sql = new StringBuffer("select top ").append(endCount).append(" 0 as tempColumn,").append(sql).toString();
			sqlSb.append("select * from (select row_number() over(order by tempColumn) tempRowNumber,* from (").append(sql).append(") t) tt where tempRowNumber>=" + beginCount);
			sql = sqlSb.toString();
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			// 查询条件为时间
			if(timeList != null && timeList.size() > 0)
			{
				int psIndex = 0;
				for (int i = 0; i < timeList.size(); i++)
				{
					psIndex++;
					String time = timeList.get(i);
					ps.setTimestamp(psIndex, Timestamp.valueOf(time));
				}
			}
			rs = ps.executeQuery();
			// 将ResultSet转换成List
			returnList = rsToList(rs, voClass, columns);
		}
		catch (Exception e)
		{
			// throw e;
			EmpExecutionContext.error(e, "完美通知查询失败！");
		}
		finally
		{
			// 关闭数据库资源。
			try{
				close(rs, ps, conn);
			}catch (Exception e){
                EmpExecutionContext.error(e, "发现异常");
			}
		}
		return returnList;
	}

	/**
	 * @description 查询 机构 （包含子机构） 下的员工信息
	 * @param employeeDeps
	 *        所需要查询的机构列表
	 * @return 机构下的员工
	 * @throws Exception
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-24 上午09:25:00
	 */
	
    public List<DynaBean> findEmployeeByDep(List<LfEmployeeDep> employeeDeps)
	{
		StringBuffer conditionBuffer = new StringBuffer("");
		for (int i = 0; i < employeeDeps.size(); i++)
		{
			conditionBuffer.append(" led.").append(TableLfEmployeeDep.DEP_PATH).append(" like '")
			.append(employeeDeps.get(i).getDeppath()).append("%'");
			if(i != employeeDeps.size() - 1)
			{
				conditionBuffer.append(" or ");
			}
		}
		String sql = "select e.mobile,e.guid,e.name,1 as usertype from " + TableLfEmployee.TABLE_NAME + " e  where e." 
		+ TableLfEmployee.DEP_ID + " in ( select led." + TableLfEmployeeDep.DEP_ID + " from " 
		+ TableLfEmployeeDep.TABLE_NAME + " led where " + conditionBuffer.toString() + " ) ";
		EmpExecutionContext.info("[完美通知]包含子机构查询sql："+sql);
		return getListDynaBeanBySql(sql);
	}
	/**
	 * 根据员工的机构查找员工
	 * @param depIds
	 * @param corpCode
	 * @return
	 */
	
    public List<DynaBean> findEmployeeByDepIds(String depIds, String corpCode){
		String sql=new StringBuffer("select mobile,guid,name,1 as usertype from lf_employee where dep_id in (")
		.append(depIds).append(")").append(" and corp_code ='").append(corpCode).append("'").toString();
		EmpExecutionContext.info("[完美通知]不包含子机构查询sql："+sql);
		return getListDynaBeanBySql(sql);
	}
	/**
	 * 根据员工的GUID查找员工
	 * @param guids
	 * @param corpCode
	 * @return
	 */
	
    public List<DynaBean> findEmployeeByGuid(String guids, String corpCode){
		String sql=new StringBuffer("select mobile,guid,name,1 as usertype from lf_employee where guid in (")
		.append(guids).append(")").append(" and corp_code ='").append(corpCode).append("'").toString();
		return getListDynaBeanBySql(sql);
	}
	
	/**
	 * 根据自定义的GUID查找自定义人员
	 * @param guids
	 * @param corpCode
	 * @return
	 */
	
    public List<DynaBean> findMaListByGuid(String guids, String corpCode){
		String sql=new StringBuffer("select mobile,guid,name,2 as usertype from lf_malist where guid in (")
		.append(guids).append(")").append(" and corp_code ='").append(corpCode).append("'").toString();
		return getListDynaBeanBySql(sql);
	}
	
	/**
	 * 根据群组ID查找员工通讯录中的人员
	 * @param guids
	 * @param corpCode
	 * @return
	 */
	
    public List<DynaBean> findEmployeeByGroupGuid(String groupIds){
		String sql=new StringBuffer("select mobile,guid,name,1 as usertype from lf_employee where guid in ").append("(")
		.append("select guid from lf_list2gro where udg_id in (").append(groupIds).append(") and l2g_type=0 ").append(")").toString();
		//EmpExecutionContext.info("[完美通知]查询员工群组人员sql："+sql);
		return getListDynaBeanBySql(sql);
	}
	
	/**
	 * 根据群组ID查找自定义通讯录中的人员
	 * @param guids
	 * @param corpCode
	 * @return
	 */
	
    public List<DynaBean> findMaListByGroupGuid(String groupIds){
		String sql=new StringBuffer("select mobile,guid,name,2 as usertype from lf_malist where guid in ").append("(")
		.append("select guid from lf_list2gro where udg_id in (").append(groupIds).append(") and l2g_type=2 ").append(")").toString();
		return getListDynaBeanBySql(sql);
	}
	
	/**
	 *  设置完美通知上行信息
	 * @description    
	 * @param moTask
	 * @return  数据更新成功返回true;更新失败返回false     			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2013-10-29 下午03:12:53
	 */
	
    public boolean setPrefectNoticeUpMsg(LfMotask moTask)
	{
		//返回信息
		boolean isSuccess = false;
		try
		{
			if(moTask == null)
			{
				EmpExecutionContext.error("设置完美通知上行信息参数异常，LfMotask为null");
				return false;
			}
			//回复内容
			String content = moTask.getMsgContent();
			//回复内容中的单引号替换为双引号
			content = content.replaceAll("\'","\\\"");
			
			//追加回复内容
			//oracle
			if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
			{
				content = "CONTENT ||" + "'  " + content + "'";
			}
			//sqlserver数据库
			else if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				content = "ISNULL(CONTENT,'')+'  " + content + "'";
			}
			//mysql数据库
			else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
			{
				content = "CONCAT(IFNULL(CONTENT,''),  '  " + content + "')";
			}
			//DB2数据库
			else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
			{
				content = "COALESCE(CONTENT, '') ||" + "'  " + content + "'";
			}
			else
			{
				EmpExecutionContext.error("设置完美通知上行信息失败，数据库类型错误，DBTYPE：" + StaticValue.DBTYPE);
				return false;
			}
			
			String date = "";
			try
			{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				date = new DataAccessDriver().getGenericDAO().getTimeCondition(format.format(System.currentTimeMillis()));
			}
			catch (Exception e1)
			{
				EmpExecutionContext.error(e1, "设置完美通知上行信息回复时间异常!");
				return false;
			}
			
			//拼接执行SQL
			StringBuffer sqlBuffer=new StringBuffer("UPDATE LF_P_N_UP SET ISREPLY=0,DIALOGIN='2',CONTENT =")
			.append(content).append(",SEND_TIME=").append(date).append(" WHERE SPNUMBER='").append(moTask.getSpnumber()).append("' AND SPUSER='")
			.append(moTask.getSpUser()).append("'");
			//以+号开头的国际号码,网关侧自动转为00开头，对00开头的号码,条件需要加上+开头的号码
			String phone = moTask.getPhone();
			if(phone != null && phone.length()>2 && "00".equals(phone.substring(0, 2)))
			{
				sqlBuffer.append(" AND (MOBILE='").append(phone).append("' OR MOBILE='").append("+" + phone.substring(2)).append("')");
			}
			else
			{
				sqlBuffer.append(" AND MOBILE='").append(phone).append("'");
			}
			String sql = sqlBuffer.toString();
			//执行SQL，设置完美通知上行信息
			isSuccess = executeBySQL(sql, StaticValue.EMP_POOLNAME);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置完美通知上行信息失败!");
			isSuccess = false;
		}finally{
			if(moTask != null)
			{
				EmpExecutionContext.info("完美通知上行信息，Spnumber:" + moTask.getSpnumber()+"，SpUser:" +moTask.getSpUser()+ "，mobile:" + moTask.getPhone() + "，isSuccess：" + isSuccess);
			}
		}
		return isSuccess;
		
	}
	
	/**
	 * 根据状态报告设置完美通知状态
	 * @param objectMap
	 * @param conditionMap
	 * @return
	 */
	public boolean setPrefectNoticeState(LinkedHashMap<String, String> objectMap, LinkedHashMap<String, String> conditionMap)
	{
		//返回信息
		boolean isSuccess = false;
		try {
			String sql = "";
			//拼接执行SQL
			StringBuffer sqlBuffer=new StringBuffer("UPDATE LF_P_N_UP SET ISRECEIVER=").append(objectMap.get("isReceive"))
			.append(", ISATRRED='").append(objectMap.get("isAtrred")).append("'")
			.append(" WHERE TASKID=").append(conditionMap.get("taskId")).append(" AND SPUSER='").append(conditionMap.get("spUser"))
			.append("'").append(" AND ISATRRED='").append(conditionMap.get("isAtrred")).append("'");
			//以+号开头的国际号码,网关侧自动转为00开头，对00开头的号码,条件需要加上+开头的号码
			String phone = conditionMap.get("mobile");
			if(phone != null && phone.length()>2 && "00".equals(phone.substring(0, 2)))
			{
				sqlBuffer.append(" AND (MOBILE='").append(phone).append("' OR MOBILE='").append("+" + phone.substring(2)).append("')");
			}
			else
			{
				sqlBuffer.append(" AND MOBILE='").append(phone).append("'");
			}
			
			sql = sqlBuffer.toString();
			//执行更新SQL
			isSuccess = executeBySQL(sql, StaticValue.EMP_POOLNAME);
			return isSuccess;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "完美通知状态报告更新失败！");
			return false;
		}
	}
}
