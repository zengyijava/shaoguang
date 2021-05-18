/**
 * 
 */
package com.montnets.emp.database;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.employee.LfEmployee;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-16 下午05:32:00
 * @description
 */

public class ProcedureManager extends SuperDAO implements IProcedureManager {

	public void getClientByFunction() throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int num = 0;
		try {

			IConnectionManager cmm = new ConnectionManagerImp();
			con = cmm.getDBConnection(StaticValue.EMP_POOLNAME);
			String sql = "select * from table(fun4(?))";

			EmpExecutionContext.debug(sql);
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, 1);
			rs = pstmt.executeQuery();
			LfClient lfClient = null;

			while (rs.next()) {
				num++;
				lfClient = new LfClient();
				lfClient.setName(rs.getString("name"));
				lfClient.setCcId(rs.getLong("cc_id"));
				lfClient.setDepId(rs.getLong("dep_Id"));
				lfClient.setMobile(rs.getString("mobile"));
				EmpExecutionContext.debug(rs.getString("name") + "\t"
						+ rs.getString("mobile"));

			}
			if (num > 0) {
				EmpExecutionContext.debug("新导入" + num + "条通讯录");
			}

		} catch (Exception e) {
			num = 0;
			EmpExecutionContext.error(e, "获取通讯录异常。");
		} finally {
			if (rs != null) {
				try{
					rs.close();
				}catch(Exception e){
					EmpExecutionContext.error(e, "关闭数据库资源异常。");
				}
			}
			if (con != null) {
				try{
					con.close();
				}catch(Exception e){
					EmpExecutionContext.error(e, "关闭数据库资源异常。");
				}

			}
			if(pstmt != null){
				try{
					pstmt.close();
				}catch(Exception e){
					EmpExecutionContext.error(e, "关闭数据库资源异常。");
				}
			}
		}
	}

	public int addClientByProc(Long depId, String bizType) throws Exception {
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		int num = 0;
		List<LfClient> lfClientList = null;
		try {

			String procedure = "{call PKG_SYNC_INFOS.SYNC_INFOS(?,?,?,?,?)}";

			con = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			con.setAutoCommit(false);
			cstmt = con.prepareCall(procedure);
			cstmt.setLong(1, depId);
			cstmt.setString(2, bizType);
			cstmt.setString(3, null);
			cstmt.setInt(4, 1); 
			cstmt.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			rs = (ResultSet) cstmt.getObject(5);
			LfClient lfClient = null;
			lfClientList = new ArrayList<LfClient>();
			while (rs.next()) {
				lfClient = new LfClient();
				lfClient.setDepId(rs.getLong("DEP_CODE"));
				lfClient.setMobile(rs.getString("MOBILE"));
				lfClient.setName(rs.getString("NAME"));
				lfClient.setCcId(rs.getLong("CC_ID"));
				//lfClient.setBizId(rs.getString("BIZ_ID"));
				lfClientList.add(lfClient);
				save(lfClient);
				num++;
			}

			con.commit();

		} catch (Exception e) {
			EmpExecutionContext.error(e, "执行PKG_SYNC_INFOS.SYNC_INFOS存储过程异常。");
			num = 0;
			con.rollback();
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cstmt != null) {
					cstmt.close();
				}
				if (con != null) {
					con.setAutoCommit(true);
					con.close();
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e, "保存通讯录异常。");
			}
		}

		// num = save(lfClientList);
		if (num > 0) {
			EmpExecutionContext.debug("新导入" + num + "条通讯录");

		}
		return num;
	}

	public int addEmpolyeeByProc(Long depId, String bizType) throws Exception {
		Connection con = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		int num = 0;
		List<LfEmployee> lfEmployeeList = null;
		try {

			String procedure = "{call PKG_SYNC_INFOS.SYNC_INFOS(?,?,?,?,?)}";

			con = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			con.setAutoCommit(false);
			cstmt = con.prepareCall(procedure);
			cstmt.setLong(1, depId);
			cstmt.setString(2, bizType);
			cstmt.setString(3, null);
			cstmt.setInt(4, 0);
			cstmt.registerOutParameter(5, oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();
			rs = (ResultSet) cstmt.getObject(5);
			LfEmployee lfEmployee = null;
			lfEmployeeList = new ArrayList<LfEmployee>();
			while (rs.next()) {
				lfEmployee = new LfEmployee();
				lfEmployee.setDepId(rs.getLong("DEP_CODE"));
				lfEmployee.setMobile(rs.getString("MOBILE"));
				lfEmployee.setName(rs.getString("NAME"));
				// lfClient.setCcId(rs.getLong("cc_id"));
				lfEmployeeList.add(lfEmployee);
				save(lfEmployee);
				num++;
			}

			con.commit();

		} catch (Exception e) {
			EmpExecutionContext.error(e, "执行PKG_SYNC_INFOS.SYNC_INFOS存储过程异常。");
			num = 0;
			con.rollback();
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (cstmt != null) {
					cstmt.close();
				}
				if (con != null) {
					con.setAutoCommit(true);
					con.close();
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e, "关闭数据库资源异常。");
			}
		}

		// num = save(lfClientList);
		if (num > 0) {
			EmpExecutionContext.debug("新导入" + num + "条通讯录");

		}
		return num;
	}

	private boolean save(Object object) throws Exception {
		boolean issuccess = false;// 定义返回的boolean
		Class<?> entityClass = object.getClass();
		Map<String, String> columns = getORMMap(entityClass);
		Field[] fields = entityClass.getDeclaredFields();
		String fieldType = "";

		
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("insert into ")
				.append(columns.get(entityClass.getSimpleName())).append("(");
		StringBuffer wenhao = new StringBuffer();
		wenhao.append(") values(");
		for (int i = 0; i < fields.length; i++) {
			sqlSb.append(columns.get(fields[i].getName()));
			wenhao.append("?");
			if (i < fields.length - 1) {
				sqlSb.append(",");
				wenhao.append(",");
			}
		}
		sqlSb.append(wenhao.append(")"));
		String sql = sqlSb.toString();

		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : "+sql);
			ps = conn.prepareStatement(sql);
			String kk = "";

			String fieldName = null;
			String fieldNameUpper = null;
			Method entityMethod = null;
			Object value = null;
			for (int i = 0; i < fields.length; i++) {
				fieldName = fields[i].getName();
				fieldNameUpper = Character.toUpperCase(fieldName.charAt(0))
						+ fieldName.substring(1);
				entityMethod = entityClass.getMethod("get" + fieldNameUpper);
				value = entityMethod.invoke(object);

				fieldType = fields[i].getGenericType().toString();
				boolean isDateType = fieldType.equals(StaticValue.TIMESTAMP)
						|| fieldType.equals(StaticValue.DATE_SQL)
						|| fieldType.equals(StaticValue.DATE_UTIL);
				if (value != null && isDateType) {
					ps.setTimestamp(i + 1, Timestamp.valueOf(value.toString()));
				} else {
					ps.setObject(i + 1, value);
				}
				if (value != null)
					kk += value.toString() + "\t";
			}

			
			int count = ps.executeUpdate();
			if (count == 1) {
				issuccess = true;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "执行保存操作异常。");
			throw e;
		} finally {
			super.close(null, ps, conn);
		}
		return issuccess;
	}


	public int updateTaotao(Long depid) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		int num = 0;
		try {
			con = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			con.setAutoCommit(false);
			String sql = "update TAOTAO SET ISREAD=0";
			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
			String sql2 = "delete lf_client where dep_id = ?";
			pstmt = con.prepareStatement(sql2);
			pstmt.setLong(1,depid);
			num = pstmt.executeUpdate();

			con.commit();
		} catch (Exception e) {
			num = -1;
			if(con != null){
				con.rollback();
			}
			EmpExecutionContext.error(e, "更新通讯录异常。");
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) 
			{
				con.setAutoCommit(true);
				con.close();
			}
		}
		return num;
	}

}
