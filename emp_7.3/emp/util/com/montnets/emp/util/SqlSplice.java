package com.montnets.emp.util;

public class SqlSplice
{
	/**
	 * 统计总条数
	 * @param sql
	 * @return
	 */
	public static String getCountSql(String sql){
		 String countSql = new StringBuffer("select count(*) totalcount from (")
			.append(sql).append(")").toString();
		 return countSql;
	}
}
