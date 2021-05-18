package com.montnets.emp.ottbase.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.ottbase.constant.WXStaticValue;

public class DbUtils {
	/**
	 * 关闭连接
	 * @param rs
	 * @param st
	 * @param conn
	 */
	public static void close(ResultSet rs, Statement st, Connection conn) {
		close(rs);
		close(st);
		close(conn);
	}
    /**
     * 关闭连接
     * @param rs
     */
	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				EmpExecutionContext.error(e,"关闭连接异常！");
			}
		}
	}
    /**
     * 关闭连接
     * @param st
     */
	public static void close(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				EmpExecutionContext.error(e,"关闭连接异常！");
			}
		}
	}
    /**
     * 关闭连接
     * @param conn
     */
	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
	}
    /**
     * 判断是否是时间格式
     * @param fieldType
     * @return
     */
	public static boolean isDateType(String fieldType) {
		if (fieldType.equals(WXStaticValue.TIMESTAMP)
				|| fieldType.equals(WXStaticValue.DATE_SQL)
				|| fieldType.equals(WXStaticValue.DATE_UTIL)) {
			return true;
		}
		return false;
	}
}
