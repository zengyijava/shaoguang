package com.montnets.emp.qyll.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.qyll.vo.SystemMtTaskVo;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * 实时记录查询
 * 
 * @project emp
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-5 下午03:57:19
 * @description
 */
public class GenericSystemMtTaskVoDAO extends SuperDAO
{

	/**
	 * 不带分条件系统下行实时记录查询
	 * @param systemMtTaskVo
	 * @return
	 * @throws Exception
	 */
	public List<SystemMtTaskVo> findSystemMtTaskVo(SystemMtTaskVo systemMtTaskVo)
			throws Exception {
		//获取查询列名
		String fieldSql = GenericSystemMtTaskVoSQL.getFieldSql();
		 //获取查询表名 
		String tableSql = GenericSystemMtTaskVoSQL.getTableSql();
		 //组装过滤条件
		String conditionSql = GenericSystemMtTaskVoSQL
				.getConditionSql(systemMtTaskVo);
		 //时间段
		List<String> timeList = GenericSystemMtTaskVoSQL
				.getTimeCondition(systemMtTaskVo);
		//排序
		String orderbySql = GenericSystemMtTaskVoSQL.getOrderBySql();
		//组装sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderbySql).toString();

		EmpExecutionContext.sql("execute sql: " + sql);
		/**
		 * 读取配置文件里是否启用备用服务器
		 */
		ResourceBundle rb = ResourceBundle.getBundle("SystemGlobals");
		int backup = Integer.parseInt(rb
				.getString("montnets.emp.use_backup_server"));
		List<SystemMtTaskVo> returnList = new ArrayList<SystemMtTaskVo>();
		// 启用备用服务器
		if (1 == backup) {
			try {
				// 用备用服务器查询获取数据
				returnList = findVoListBySQL(SystemMtTaskVo.class, sql,
						StaticValue.EMP_BACKUP, timeList);
			} catch (Exception e) {
				//如果备用服务器连接出错，则从主服务器查询获取数据
				returnList = findVoListBySQL(SystemMtTaskVo.class, sql,
						StaticValue.EMP_POOLNAME, timeList);
				EmpExecutionContext.error(e,"查询备用服务器连接异常");
			}
		} else {
			//不启用备用服务器的情况
			returnList = findVoListBySQL(SystemMtTaskVo.class, sql,
					StaticValue.EMP_POOLNAME, timeList);
		}
		return returnList;
	}

	/**
	 * 带分的下行实时记录查询
	 * @param systemMtTaskVo  带条件的vo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<SystemMtTaskVo> findSystemMtTaskVo(SystemMtTaskVo systemMtTaskVo, PageInfo pageInfo) throws Exception 
	{
		//查询表名 
		String tableSql = new StringBuffer(" from gw_mt_task_bak mttask ").append(StaticValue.getWITHNOLOCK()).append(" ").toString();
        //组装过滤条件
		String conditionSql = GenericSystemMtTaskVoSQL.getConditionSql(systemMtTaskVo);
		
		String sql;
		switch (StaticValue.DBTYPE) 
		{
			case 1:
				// oracle
				sql = getOraclePageSql(conditionSql,tableSql);
				break;
			case 2:
				// sqlserver2005
				sql = getSql(conditionSql,tableSql);
				break;
			case 3:
				// MYSQL
				sql = getSql(conditionSql,tableSql);
				break;
			case 4:
				// DB2
				sql = getSql(conditionSql,tableSql);
				break;
			default:
				sql = getSql(conditionSql,tableSql);
				break;
		}

		//时间段
		List<String> timeList = GenericSystemMtTaskVoSQL.getTimeCondition(systemMtTaskVo);
		EmpExecutionContext.sql("execute sql: " + sql);

		/**
		 * 读取配置文件里是否启用备用服务器
		 */
		String use_backup_server = SystemGlobals.getValue("montnets.emp.use_backup_server");
		List<SystemMtTaskVo> returnList;
		//有分页
		if(pageInfo != null)
		{
			MtTaskGenericDAO mtTaskDao = new MtTaskGenericDAO();
			//组装统计语句
			//String countSql = new StringBuffer("select count(ID) totalcount").append(tableSql).append(conditionSql).toString();
			// 启用备用服务器
			if ("1".equals(use_backup_server)) 
			{
				try 
				{
					// 用备用服务器查询获取数据
					returnList = mtTaskDao.findPageVoListBySQL(
							SystemMtTaskVo.class, sql, null, pageInfo, StaticValue.EMP_BACKUP, timeList);
					return returnList;
				}
				catch (Exception e) 
				{
					//如果备用服务器连接出错，则从主服务器查询获取数据
					EmpExecutionContext.error(e,"系统下行记录实时查询，备用服务器连接异常。");
				}
			} 
			
			//不启用备用服务器的情况
			returnList = mtTaskDao.findPageVoListBySQL(
					SystemMtTaskVo.class, sql, null, pageInfo,
					StaticValue.EMP_POOLNAME, timeList);
			
			return returnList;
		}

		//没分页
		// 启用备用服务器
		if ("1".equals(use_backup_server)) 
		{
			try 
			{
				// 用备用服务器查询获取数据
				returnList = findVoListBySQL(SystemMtTaskVo.class, sql, StaticValue.EMP_BACKUP, timeList);
				return returnList;
			}
			catch (Exception e) 
			{
				//如果备用服务器连接出错，则从主服务器查询获取数据
				EmpExecutionContext.error(e,"系统下行记录实时查询，备用服务器连接异常。");
			}
		}
		//不启用备用服务器的情况
		returnList = findVoListBySQL(SystemMtTaskVo.class, sql, StaticValue.EMP_POOLNAME, timeList);
		return returnList;
	}
	
	/**
	 * 查询下行实时记录分页信息
	 * @param systemMtTaskVo  带条件的vo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public boolean findSystemMtTaskPageInfo(SystemMtTaskVo systemMtTaskVo, PageInfo pageInfo) 
	{
		try
		{
			//查询表名 
			String tableSql = new StringBuffer(" from gw_mt_task_bak mttask ").append(StaticValue.getWITHNOLOCK()).append(" ").toString();
			//组装过滤条件
			String conditionSql = GenericSystemMtTaskVoSQL.getConditionSql(systemMtTaskVo);
			
			String sql;
			switch (StaticValue.DBTYPE) 
			{
				case 1:
					// oracle
					sql = getOraclePageSql(conditionSql, tableSql);
					break;
				case 2:
					// sqlserver2005
					sql = getSql(conditionSql,tableSql);
					break;
				case 3:
					// MYSQL
					sql = getSql(conditionSql,tableSql);
					break;
				case 4:
					// DB2
					sql = getSql(conditionSql,tableSql);
					break;
				default:
					sql = getSql(conditionSql, tableSql);
					break;
			}

			//时间段
			List<String> timeList = GenericSystemMtTaskVoSQL.getTimeCondition(systemMtTaskVo);
			EmpExecutionContext.sql("execute sql: " + sql);

			//读取配置文件里是否启用备用服务器
			String use_backup_server = SystemGlobals.getValue("montnets.emp.use_backup_server");
			
			MtTaskGenericDAO mtTaskDao = new MtTaskGenericDAO();
			//组装统计语句
			String countSql = new StringBuffer("select count(ID) totalcount")
					.append(tableSql).append(conditionSql).toString();
			// 启用备用服务器
			if ("1".equals(use_backup_server)) 
			{
				try 
				{
					// 用备用服务器查询获取数据
					return mtTaskDao.findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_BACKUP, timeList);
				}
				catch (Exception e) 
				{
					//如果备用服务器连接出错，则从主服务器查询获取数据
					EmpExecutionContext.error(e,"系统下行记录实时查询，备用服务器连接异常。");
				}
			} 
			
			//不启用备用服务器的情况
			return mtTaskDao.findPageInfoBySQL(countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询下行实时记录分页信息，异常。");
			return false;
		}
	}
	
	private String getSql(String conditionSql, String tableSql)
	{
		//获取查询列名
		String fieldSql = "select mttask.ID,mttask.USERID,mttask.SPGATE,mttask.CPNO,mttask.PHONE,mttask.TASKID,mttask.SENDSTATUS,mttask.ERRORCODE,mttask.SENDTIME,mttask.MESSAGE,mttask.UNICOM,mttask.RECVTIME,mttask.SVRTYPE,mttask.PKNUMBER,mttask.PKTOTAL,mttask.MSGFMT ";
		
        //排序
		String orderbySql = " order by mttask.ID desc ";
        //组装sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderbySql).toString();
		return sql;
	}
	
	/**
	 * 获取Mysql查询sql
	 * @param conditionSql
	 * @param tableSql
	 * @return
	 */
/*	private String getMysqlSql(String conditionSql, String tableSql)
	{
		//获取查询列名
		String fieldSql = "select mttask.ID,mttask.USERID,mttask.SPGATE,mttask.CPNO,mttask.PHONE,mttask.TASKID,mttask.SENDSTATUS,mttask.ERRORCODE,mttask.SENDTIME,mttask.MESSAGE,mttask.UNICOM,mttask.RECVTIME,mttask.SVRTYPE,mttask.PKNUMBER,mttask.PKTOTAL,mttask.MSGFMT ";
		
        //排序
		String orderbySql = " order by mttask.ID desc ";
        //组装sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderbySql).toString();
		return sql;
	}*/
	
	/**
	 * 获取oracle分页sql
	 * @param systemMtTaskVo
	 * @return
	 */
	private String getOraclePageSql(String conditionSql, String tableSql)
	{
		 //获取查询列名
		StringBuffer fieldSql = new StringBuffer("select mttask.ID,mttask.USERID,mttask.SPGATE,mttask.CPNO,mttask.PHONE,mttask.TASKID,mttask.SENDSTATUS,mttask.ERRORCODE,mttask.SENDTIME,mttask.MESSAGE,mttask.UNICOM,mttask.RECVTIME,mttask.SVRTYPE,mttask.PKNUMBER,mttask.PKTOTAL,mttask.MSGFMT ");
		
		//排序
		String orderbySql = " order by mttask.ID desc ";
		
		StringBuffer pageSql = new StringBuffer("WHERE rowid IN ( SELECT rid FROM ( SELECT rownum rn, rid FROM ( SELECT rowid rid ")
				.append(tableSql).append(conditionSql).append(orderbySql).append(") WHERE rownum <= ? ) WHERE rn >= ? )");
        
        //组装sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				pageSql).append(orderbySql).toString();
		return sql;
	}

	public PageInfo getPageInfo(int preIndex, int pageSize,
			SystemMtTaskVo systemMtTaskVo) throws Exception {
		PageInfo pageInfo = new PageInfo();
		int totalCount = 0;

		String fieldSql = "select count(ID) totalcount";

		String tableSql = GenericSystemMtTaskVoSQL.getTableSql();

		String conditionSql = GenericSystemMtTaskVoSQL
				.getConditionSql(systemMtTaskVo);
		List<String> timeList = GenericSystemMtTaskVoSQL
				.getTimeCondition(systemMtTaskVo);

		String countSql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).toString();

		totalCount = findCountBySQL(countSql, timeList);
		int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize
				: totalCount / pageSize + 1;
		pageInfo.setTotalRec(totalCount);
		pageInfo.setTotalPage(totalPage);
		pageInfo.setPageSize(pageSize);

		pageInfo.setPageIndex(preIndex);
		return pageInfo;
	}
	
	public List<LfSysuser> findSysuserByNameorUserName(String code) throws Exception{
		//拼接sql
		String sql = new StringBuffer("select *")
		             .append(" from ").append(TableLfSysuser.TABLE_NAME).append(" sysuser "+StaticValue.getWITHNOLOCK()+" where sysuser.")
		             .append(TableLfSysuser.USER_NAME).append(" like '%").append(code).append("%' or sysuser.")
		             .append(TableLfSysuser.NAME).append(" like '%").append(code).append("%'")
		             .toString();
		//返回查询结果
		return findEntityListBySQL(LfSysuser.class, sql, StaticValue.EMP_POOLNAME);
	}
}
