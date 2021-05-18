package com.montnets.emp.database;

import java.sql.Connection;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:39:11
 * @description 
 */
public interface ConnectionManager {

	public abstract Connection getConnection();

	public abstract Connection getConnection(String propretiesFile);


	/**
	 *  StaticValue.SMSSVR_POOLNAME)
	 * @param dbname
	 * @return
	 */
	public abstract Connection getDBConnection(String dbname);
	
	public abstract void close(Connection connection);
}
