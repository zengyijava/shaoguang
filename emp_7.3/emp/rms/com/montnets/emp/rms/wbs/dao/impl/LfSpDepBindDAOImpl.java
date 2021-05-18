package com.montnets.emp.rms.wbs.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.database.IConnectionManager;
import com.montnets.emp.rms.wbs.dao.ILfSpDepBindDAO;

public class LfSpDepBindDAOImpl implements ILfSpDepBindDAO {
	private IConnectionManager connManager;

	public LfSpDepBindDAOImpl() {
		connManager = new ConnectionManagerImp();// 获取实例对象，为了得到Connection
	}

	/**
	 * 实现sp账号和企业编码的关系
	 * <li>key=spuser;value=corpCode
	 */
	public Map<String, String> findSpAndCorpCode() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = connManager.getDBConnection("EMP");// 获取数据库连接对象那个
			// 定义sql语句
			String sql = "SELECT SPUSER,CORP_CODE FROM LF_SP_DEP_BIND;";
			conn = connManager.getDBConnection("EMP");// 获取数据库连接对象那个
			pstmt = conn.prepareStatement(sql);// 获取PreparedStatement对象
			// 执行查询
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String spuser = rs.getString(1);// 获取sp账号
				String corpCode = rs.getString(2);// 获取企业编号
				map.put(spuser, corpCode);// 保存两则的关系
			}
		} catch (Exception e) {
		} finally {
			// 关闭数据库连接
			if (conn != null) {
				conn.close();
			}
			if(rs != null){
				rs.close();
			}
			if(pstmt != null){
				pstmt.close();
			}
		}
		return map;
	}

}
