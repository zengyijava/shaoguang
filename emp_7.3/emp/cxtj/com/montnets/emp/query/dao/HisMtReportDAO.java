package com.montnets.emp.query.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

/**
 * 
 * @project emp_std_192.169.1.81_new
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-12-2 下午05:35:15
 * @description
 */
public class HisMtReportDAO extends SuperDAO
{
	
	/**
	 * 查询日期格式化：yyyy-MM-dd HH:mm:ss
	 */
	private SimpleDateFormat sdfSeachTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 查询日期格式化：yyyy
	 */
	private SimpleDateFormat sdf_yyyy = new SimpleDateFormat("yyyy");
	
	/**
	 * 查询日期格式化：MM
	 */
	private SimpleDateFormat sdf_MM = new SimpleDateFormat("MM");

	
	SimpleDateFormat sdf_yyyyMM = new SimpleDateFormat("yyyyMM");
	
	/**
	 * 下行历史记录表名map，验证过是否存在的表名放这里。key为表名；value为是否存在，true表示存在，false表示不存在，null表示未验证
	 */
	private static ConcurrentHashMap<String,Boolean> mtHisTableNameMap = new ConcurrentHashMap<String,Boolean>();


	/**
	 * 统计记录数
	 * @param conditionMap
	 * @return
	 */
	public int countMtTasks(LinkedHashMap<String, String> conditionMap){
		String tableNameA = getTableA(conditionMap);
		String tableNameB = getTableB(conditionMap);
		String baseSql = null;
		String countSql = null;
		int tatolA = 0;
		int tatolB = 0;
		//组装过滤条件语句
		String conditionSql = SysMtRecordDAOSql.getConditionSql(conditionMap, "mttaskxx");
		if (StringUtils.isNotBlank(tableNameA)) {
			baseSql = "SELECT count(ID) totalcount from "+ tableNameA +" mttaskxx ";
			countSql = baseSql + conditionSql;
			tatolA = super.findCountBySQL(countSql);
		}
		if(tableNameB == null){
			tableNameB = "";
		}
		if (StringUtils.isNotBlank(tableNameB) && !tableNameB.equals(tableNameA)) {
			baseSql = "SELECT count(ID) totalcount from "+ tableNameB +" mttaskxx ";
			countSql = baseSql + conditionSql;
			tatolB = super.findCountBySQL(countSql);
		}
		return tatolA + tatolB;
	}
	
	/**
	 * 下行历史记录查询
	 * @param conditionMap 查询条件集合
	 * @param pageInfo 分页信息对象
	 * @return 返回下行历史记录动态bean集合
	 */
	public List<DynaBean> findMtTasksHis(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		try
		{
			//获取查询表名的sql语句
			String tableName = getMttaskHisTableSql(conditionMap);
			if(tableName == null || tableName.length() == 0)
			{
				return null;
			}
			
			String withnolock;
			if(tableName.indexOf("union") == -1)
			{
				//withnolock = StaticValue.WITHNOLOCK;
				withnolock = StaticValue.getWITHNOLOCK();
			}
			else
			{
				withnolock = "";
			}
			
			//获取查询表名的sql语句
			String sql = new StringBuffer("select * from ").append(tableName).append(" t ").append(withnolock).toString();

			List<DynaBean> returnList;
			//读取配置文件里是否启用备用服务器
			String use_backup_server = SystemGlobals.getValue("montnets.emp.use_backup_server");
			//启用备用服务器
			if("1".equals(use_backup_server))
			{
				try 
				{
                    returnList =  new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, null, pageInfo, StaticValue.EMP_BACKUP, null);
                    return returnList;
				}
				catch (Exception e)
				{
					//如果备用服务器连接出错，则主服务器查询获取数据
					EmpExecutionContext.error(e, "DAO查询，下行历史记录，备用服务器连接异常。");
				}
			}
			EmpExecutionContext.info("系统下行历史记录，查询sql："+sql);
            returnList =  new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, null, pageInfo, StaticValue.EMP_POOLNAME, null);

            return returnList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO查询，下行历史记录，异常。");
			return null;
		}
	}
	

	private String getTableA(LinkedHashMap<String, String> conditionMap){
		try {
			//开始时间
			Calendar caStartTime = Calendar.getInstance();
			caStartTime.setTime(sdfSeachTime.parse(conditionMap.get("sendtime")));
			//获取查询表名
			String tableName = getMttaskHisTableName("MTTASK", sdf_yyyy.format(caStartTime.getTime()), sdf_MM.format(caStartTime.getTime()));
			//表不存在
			if(tableName == null) {
				EmpExecutionContext.info("DAO查询下行历史记录，表不存在" + "tableName=" + "MTTASK" + sdf_yyyy.format(caStartTime.getTime()) + sdf_MM.format(caStartTime.getTime()) );
			}
			return tableName;
		} catch (ParseException e) {
			return null;
		}
	}
	private String getTableB(LinkedHashMap<String, String> conditionMap){
		try {
			//结束时间
			Calendar caEndTime = Calendar.getInstance();
			caEndTime.setTime(sdfSeachTime.parse(conditionMap.get("recvtime")));
			//获取查询表名
			String tableName = getMttaskHisTableName("MTTASK", sdf_yyyy.format(caEndTime.getTime()), sdf_MM.format(caEndTime.getTime()));
			//表不存在
			if(tableName == null) {
				EmpExecutionContext.info("DAO查询下行历史记录，表不存在" + "tableName=" + "MTTASK" + sdf_yyyy.format(caEndTime.getTime()) + sdf_MM.format(caEndTime.getTime()) );
			}
			return tableName;
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * 获取表名sql
	 * @param conditionMap 查询条件
	 * @return 表名sql
	 */
	private String getMttaskHisTableSql(LinkedHashMap<String, String> conditionMap) {
		try {
			//开始时间
			Calendar caStartTime = Calendar.getInstance();
			caStartTime.setTime(sdfSeachTime.parse(conditionMap.get("sendtime")));
			
			//结束时间
			Calendar caEndTime = Calendar.getInstance();
			caEndTime.setTime(sdfSeachTime.parse(conditionMap.get("recvtime")));
			
			//获取查询表名
			String tableName = getMttaskHisTableName("MTTASK", sdf_yyyy.format(caStartTime.getTime()), sdf_MM.format(caStartTime.getTime()));

			String fieldSql = "select aa.ID,aa.userid,aa.spgate,aa.cpno,aa.phone,aa.sendstatus,aa.taskid,aa.errorcode,aa.sendtime," +
					"aa.message,aa.unicom,aa.pknumber,aa.pktotal,aa.svrtype,aa.recvtime,aa.p1,aa.p2,aa.usermsgid,aa.custid ";
			//表不存在
			if(tableName == null) {
				EmpExecutionContext.info("DAO查询下行历史记录，获取表名sql，表不存在。" + "tableName=" + "MTTASK" + sdf_yyyy.format(caStartTime.getTime()) + sdf_MM.format(caStartTime.getTime()) );
			}
			
			//如果是同一个月，则查单表
			if(caStartTime.get(Calendar.MONTH) == caEndTime.get(Calendar.MONTH)) {
				//组装过滤条件语句
				String conditionSql = SysMtRecordDAOSql.getConditionSql(conditionMap, "aa");
				return "(" +fieldSql + " from " + tableName + " aa " + conditionSql + StaticValue.getWITHNOLOCK() + ")";
			}
			
			//跨月查询，则需要查询两张表的记录，只允许查两个月以内的数据
			//获取第二张历史表表名
			String secondTableName = getMttaskHisTableName("MTTASK", sdf_yyyy.format(caEndTime.getTime()), sdf_MM.format(caEndTime.getTime()));

			//两个表都不存在
			if(tableName == null && secondTableName == null) {
				EmpExecutionContext.info("DAO查询下行历史记录，获取表名sql，两个表都不存在。" 
						+ "tableName=" + "MTTASK" + sdf_yyyy.format(caStartTime.getTime()) + sdf_MM.format(caStartTime.getTime())
						+ ",secondTableName=" + "MTTASK" + sdf_yyyy.format(caEndTime.getTime()) + sdf_MM.format(caEndTime.getTime())
						);
				return null;
			} else if(tableName == null) {
				//第二个表存在
				EmpExecutionContext.info("DAO查询下行历史记录，获取表名sql，表不存在。" 
						+ "tableName=" + "MTTASK" + sdf_yyyy.format(caStartTime.getTime()) + sdf_MM.format(caStartTime.getTime())
						);
				//组装过滤条件语句
				String conditionSql = SysMtRecordDAOSql.getConditionSql(conditionMap, "aa");
				return "(" +fieldSql + " from " + secondTableName +  " aa " + conditionSql + StaticValue.getWITHNOLOCK() + ")";
			} else if(secondTableName == null) {
				//第一个表存在（界面控制好的话，这种情况应该不会发生）
				EmpExecutionContext.info("DAO查询下行历史记录，获取表名sql，表不存在。" 
						+ ",secondTableName=" + "MTTASK" + sdf_yyyy.format(caEndTime.getTime()) + sdf_MM.format(caEndTime.getTime())
						);
				//组装过滤条件语句
				String conditionSql = SysMtRecordDAOSql.getConditionSql(conditionMap, "aa");
				return "(" +fieldSql + " from " + tableName + " aa " + conditionSql + StaticValue.getWITHNOLOCK() + ")";
			}
			
			//两个表都存在，则用联合查询
			//组装过滤条件语句
			String conditionSql = SysMtRecordDAOSql.getConditionSql(conditionMap, "aa");

			return "(" +
					fieldSql + " from " + tableName + " aa " + conditionSql + StaticValue.getWITHNOLOCK() +
					" union all " +
					fieldSql + " from " + secondTableName + " aa " + conditionSql + StaticValue.getWITHNOLOCK() + ")";
		} catch (Exception e) {
			EmpExecutionContext.error(e, "DAO查询下行历史记录，获取表名sql，异常。");
			return null;
		}
	}
	
	/**
	 * 验证是否升级了历史表。先按新的历史表名去找，若新的历史表不存在，则找历史表名。
	 * 
	 * @param tableName 表名
	 * @param year 年份，4位
	 * @param month 月份，2位
	 * @return 返回下行历史记录表名;表不存在这返回null，异常则返回新表名
	 */
	private String getMttaskHisTableName(String tableName, String year, String month)
	{
		//新的表名，如：MTTASK201512
		String newTableName = tableName + year + month;
		try
		{
			//老的表名，如：MTTASK12
			String oldTableName = tableName + month;

			//0：不存在；1：存在；小于0：异常情况。
			int newTableExists = getMtHisTableExists(newTableName);
			//表存在
			if(newTableExists > 0)
			{
				//表名存在，则放进内存集合中，下次直接用
				mtHisTableNameMap.put(newTableName, true);
				return newTableName;
			}
			//表不存在
			else if(newTableExists == 0)
			{
				//表名不存在，则放进内存集合中，下次直接用
				mtHisTableNameMap.put(newTableName, false);
			}
			//异常情况
			else
			{
				return newTableName;
			}
			
			//0：不存在；1：存在；小于0：异常情况。
			int oldTableExists = getMtHisTableExists(oldTableName);
			if(oldTableExists > 0)
			{
				//表存在，则放进内存集合中，下次直接用
				mtHisTableNameMap.put(oldTableName, true);
				return oldTableName;
			}
			else if(oldTableExists == 0)
			{
				//表不存在，则放进内存集合中，下次直接用
				mtHisTableNameMap.put(oldTableName, false);
				//来到这里，则新旧表都不存在
				return null;
			}
			//异常情况
			else
			{
				//异常情况
				return null;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "验证并获取下行历史表名，异常。");
			return newTableName;
		}
	}
	
	/**
	 * 获取下行历史记录表名是否存在。
	 * @param tableName 下行历史记录表名
	 * @return 0：不存在；1：存在；小于0：异常情况。
	 */
	private int getMtHisTableExists(String tableName)
	{
		try
		{
			//查询sql
			String sql;
			
			switch (StaticValue.DBTYPE)
			{
				case 1:
					// oracle
					sql = "select count(*) ICOUNT from all_tables where table_name=upper('" + tableName + "') and OWNER=upper('" + SystemGlobals.getValue("montnets.emp.user") + "')";
					break;
				case 2:
					// sqlserver2005
					sql = "select count(*) ICOUNT from sysobjects where id = object_id('" + tableName + "') and type = 'u'";
					break;
				case 3:
					// MYSQL
					sql = "select count(*) ICOUNT from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='" + SystemGlobals.getValue("montnets.emp.databaseName") + "' and TABLE_NAME=upper('" + tableName + "')";
					break;
				case 4:
					// DB2
					sql = "select count(*) ICOUNT from syscat.tables where tabname=upper('" + tableName + "')";
					break;
				default:
					EmpExecutionContext.error("获取下行历史记录表名是否存在，不支持的数据库类型。dbType=" + StaticValue.DBTYPE);
					return -1;
			}
			
			//查询新表是否存在
			List<DynaBean> resultList = getListDynaBeanBySql(sql);
			//出异常查不到记录
			if(resultList == null || resultList.size() == 0)
			{
				EmpExecutionContext.error("获取下行历史记录表名是否存在，查询新表是否存在失败。sql="+sql);
				return -2;
			}
			//出异常取不到结果（这种情况不太可能出现）
			if(resultList.get(0).get("icount") == null)
			{
				EmpExecutionContext.error("获取下行历史记录表名是否存在，查询新表是否存在，获取结果为空。sql="+sql);
				return -3;
			}
			
			String icount = resultList.get(0).get("icount").toString();
			return Integer.parseInt(icount);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("获取下行历史记录表名是否存在，异常。");
			return -9999;
		}
	}
	
}
