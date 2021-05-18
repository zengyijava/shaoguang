package com.montnets.emp.query.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @project emp_std_192.169.1.81_new
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-12-1 下午04:06:10
 * @description
 */
public class RealMtReportDAO extends SuperDAO {

	/**
	 * 统计记录数
	 * 
	 * @param conditionMap
	 * @return
	 */
	public int countMtTasks (LinkedHashMap<String, String> conditionMap) {
		String baseSql = "SELECT count(ID) totalcount from gw_mt_task_bak mtt ";
		// 组装过滤条件
		String conditionSql = SysMtRecordDAOSql.getConditionSql(conditionMap, "mtt");
		String hasWhere = conditionSql.contains("where") ? "and" : "where";
		String mysqlTableStr = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE ? hasWhere
				+ "( PTMSGID < 0 OR  PTMSGID > 30000000000000 )" : "";
		String countSql = baseSql + conditionSql + StaticValue.getWITHNOLOCK()
				+ mysqlTableStr;
		return super.findCountBySQL(countSql);
	}
	/**
	 * 下行实时记录查询
	 * 
	 * @param conditionMap
	 *            查询条件集合
	 * @param pageInfo
	 *            分页信息对象
	 * @return 返回下行历史记录动态bean集合
	 */
	public List<DynaBean> findMtTasksReal (LinkedHashMap<String, String> conditionMap,
			PageInfo pageInfo) {
		try {
			// 实时记录查询，修改成备份表和实时表联合查询
			String fieldSql = "select ID,USERID,SPGATE,CPNO,PHONE,TASKID,SENDSTATUS,ERRORCODE,SENDTIME,MESSAGE,UNICOM,RECVTIME,SVRTYPE,PKNUMBER,PKTOTAL,MSGFMT,P1,P2,usermsgid ";
			// 组装过滤条件
			String conditionSql = SysMtRecordDAOSql.getConditionSql(conditionMap, "mtt");
			// mysql实时表mt_task有两条多余的下行记录，使用 PTMSGID < 0 OR PTMSGID >
			// 30000000000000条件排除
			// 加一个字段custid的值
			// 加一个字段custid的值,mt_task表里没有这个字段，赋值为NULL
			// 判断conditionSql是否包含where关键字
			String hasWhere = conditionSql.contains("where") ? "and" : "where";
			String mysqlTableStr = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE ? hasWhere
					+ "( PTMSGID < 0 OR  PTMSGID > 30000000000000 ) "
					: "";
			String tableName = "(" + fieldSql + ",custid " + " from "
					+ "gw_mt_task_bak mtt " + conditionSql + StaticValue.getWITHNOLOCK()
					+ mysqlTableStr + ")";

			// 查询表名
			String tableSql = " from " + tableName + " mttask ";
			// 获得语句
			String sql = getSql(tableSql);
			// 返回list
			List<DynaBean> returnList;
			// 组装统计语句
			String countSql = "select count(ID) totalcount from " + tableName + " mttask";

			// 读取配置文件里是否启用备用服务器
			String backupServer = SystemGlobals
					.getValue("montnets.emp.use_backup_server");
			// 启用备用服务器
			if ("1".equals(backupServer)) {
				try {
					// 用备用服务器查询获取数据
					returnList = new DataAccessDriver().getGenericDAO()
							.findPageDynaBeanBySQL(sql, countSql, pageInfo,
									StaticValue.EMP_BACKUP, null);
					return returnList;
				} catch (Exception e) {
					// 如果备用服务器连接出错，则从主服务器查询获取数据
					EmpExecutionContext.error(e, "DAO查询，下行实时记录，备用服务器连接异常。");
				}
			}
			// 记录查询语句
			EmpExecutionContext.info("系统下行实时记录，查询sql：" + sql);

			// 不启用备用服务器的情况
			returnList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(
					sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
			// 返回LIST
			return returnList;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "DAO查询，下行实时记录，异常。");
			return null;
		}
	}

	/**
	 * 获取查询sql
	 * 
	 * @param tableSql
	 *            表名
	 * @return 查询sql
	 */
	private String getSql (String tableSql) {
		// 获取查询列名
		String fieldSql = "select mttask.ID,mttask.USERID,mttask.SPGATE,mttask.CPNO,mttask.PHONE,"
				+ "mttask.TASKID,mttask.SENDSTATUS,mttask.ERRORCODE,mttask.SENDTIME,mttask.MESSAGE,"
				+ "mttask.UNICOM,mttask.RECVTIME,mttask.SVRTYPE,mttask.PKNUMBER,mttask.PKTOTAL,"
				+ "mttask.MSGFMT,mttask.P1,mttask.P2,mttask.usermsgid,mttask.custid ";
		// 排序
		String orderbySql = " order by mttask.ID desc";
		// 组装sql语句
		return fieldSql + tableSql + orderbySql;
	}

	/**
	 * 
	 * @description 系统下行记录获取错误代码说明
	 * @param corpCode
	 *            企业编码
	 * @return 错误代码说明动态bean集合
	 * @datetime 2016-1-19 上午09:02:50
	 */
	public List<DynaBean> getErrCodeDis (String corpCode) {
		String sql = "select state_code, state_des from lf_statecode where CORP_CODE='"
				+ corpCode + "'";
		return getListDynaBeanBySql(sql);
	}

}
