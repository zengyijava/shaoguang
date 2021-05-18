package com.montnets.emp.qyll.dao.sql;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * @author Jason Huang
 * @date 2017年12月28日 下午4:07:02
 */

public class DataQuerySQLFactory {
	
	/**
	 * 根据数据库类型参数生成对应的SQL实例
	 * @param type 数据库类型
	 * @return
	 */
	public DataQuerySQL buildSQL(int type) {
		// 1:Oracle,2:SqlServer,3:MySql,4:DB2
		switch (type) {
		case 1:
			return new DataQueryOracleSQL();
		case 2:
			return new DataQuerySqlServerSQL();
		case 3:
			return new DataQueryMySqlSQL();
		case 4:
			return new DataQueryDB2SQL();
		default:
			return null;
		}
	}
	
	/**
	 * 根据数据库类型参数生成对应的SQL实例
	 * @param type 数据库类型
	 * @return
	 */
	public ReportQuerySql getSearchSql(int type) {
		// 1:Oracle,2:SqlServer,3:MySql,4:DB2
		switch (type) {
		case 1:
			return new ReportQueryOracleSql();
		case 2:
			return new ReportQuerySqlserverSql();
		case 3:
			return new ReportQueryMysqlSql();
		case 4:
			return new ReportQueryDB2Sql();
		default:
			EmpExecutionContext.error("获取查询sql，未知数据库类型。DBTYPE="+StaticValue.DBTYPE);
			return null;
		}
	}

}
