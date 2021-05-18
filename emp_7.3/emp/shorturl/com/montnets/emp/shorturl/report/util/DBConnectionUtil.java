package com.montnets.emp.shorturl.report.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.shorturl.report.constant.ShortUrlConstant;

public class DBConnectionUtil {
	
	public static Statement getQueryObj() {
		Statement stmt = null;
		Connection connection = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String ip = SystemGlobals.getValue(ShortUrlConstant.DB_IP);
			String port = SystemGlobals.getValue(ShortUrlConstant.DB_PORT);
			String name = SystemGlobals.getValue(ShortUrlConstant.DB_NAME);
			String user = SystemGlobals.getValue(ShortUrlConstant.DB_USER);
			String password = SystemGlobals.getValue(ShortUrlConstant.DB_PASS);
			connection = DriverManager.getConnection("jdbc:sqlserver://"+ip+":"+port+";DatabaseName="+name, user, password);
			stmt = connection.createStatement();
		} catch (ClassNotFoundException e) {
            EmpExecutionContext.error(e, "连接平台历史库查询访问历史数据异常！");
		} catch (SQLException e) {
            EmpExecutionContext.error(e, "连接平台历史库查询访问历史数据异常！");
		}finally{
			if(null != connection){
				try {
					connection.close();
				} catch (SQLException e) {
				}
			}
		}
		return stmt;
	}
	
	public static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String ip = SystemGlobals.getValue(ShortUrlConstant.DB_IP);
			String port = SystemGlobals.getValue(ShortUrlConstant.DB_PORT);
			String name = SystemGlobals.getValue(ShortUrlConstant.DB_NAME);
			String user = SystemGlobals.getValue(ShortUrlConstant.DB_USER);
			String password = SystemGlobals.getValue(ShortUrlConstant.DB_PASS);
			connection = DriverManager.getConnection("jdbc:sqlserver://"+ip+":"+port+";DatabaseName="+name, user, password);
		} catch (ClassNotFoundException e) {
            EmpExecutionContext.error(e, "连接平台历史库查询访问历史数据异常！");
		} catch (SQLException e) {
            EmpExecutionContext.error(e, "连接平台历史库查询访问历史数据异常！");
		}
		return connection;
	}
	
	/**
	 * 关闭数据库连接
	 * @param rs
	 * @param pst
	 * @param conn
	 */
	public static void close(ResultSet rs, PreparedStatement pst, Connection conn){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				EmpExecutionContext.error(e,"关闭ResultSet对象异常");
			}
			rs = null;
		}
		if(pst!=null){
			try {
				pst.close();
			} catch (SQLException e) {
				EmpExecutionContext.error(e,"关闭PreparedStatement对象异常");
			}
			pst = null;
		}
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				EmpExecutionContext.error(e,"关闭Connection对象异常");
			}
			conn = null;
		}
	}
}
