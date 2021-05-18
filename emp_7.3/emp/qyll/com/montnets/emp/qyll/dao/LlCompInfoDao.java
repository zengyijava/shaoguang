package com.montnets.emp.qyll.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.qyll.dao.sql.DataQuerySQLFactory;
import com.montnets.emp.qyll.dao.sql.ReportQuerySql;
import com.montnets.emp.qyll.vo.LlCompInfoVo;

public class LlCompInfoDao extends SuperDAO {

	public LlCompInfoVo getllCompInfoSql() throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		LlCompInfoVo returnBean = null;
		try 
		{
			String sql = "SELECT PASSWORD,ECID,ECNAME,IP,REMARK,PORT,PUSHADDR FROM LL_COMP_INFO";
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) 
			{
				returnBean = new LlCompInfoVo();
				returnBean.setPassword(rs.getString("PASSWORD"));
				returnBean.setCorpCode(rs.getString("ECID"));
				returnBean.setEcName(rs.getString("ECNAME"));
				returnBean.setIp(rs.getString("IP"));
				returnBean.setReMark(rs.getString("REMARK"));
				returnBean.setPort(rs.getString("PORT"));
				returnBean.setPushAddr(rs.getString("PUSHADDR"));
			}
			return returnBean;
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"数据查询异常");
			return null;
		} 
		finally 
		{
			close(rs, ps, conn);
		}
	}

	public boolean addLlCompInfo(LlCompInfoVo llCompInfoBean) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean returnFlag = false;
		try {
			// 通过SQL工厂生产对应数据库的SQL实例
			ReportQuerySql reportQuerySql = new DataQuerySQLFactory().getSearchSql(StaticValue.DBTYPE);
			String sql = reportQuerySql.getLlCompInfoInsertSql();
			//String sql = "INSERT INTO LL_COMP_INFO (ID,PASSWORD,ECID,ECNAME,IP,PORT,REMARK,PUSHADDR) VALUES(1,?,?,?,?,?,?,?)";
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setString(1,llCompInfoBean.getPassword());
			ps.setString(2,llCompInfoBean.getCorpCode());
			ps.setString(3, llCompInfoBean.getEcName());
			ps.setString(4, llCompInfoBean.getIp());
			ps.setInt(5, Integer.parseInt(llCompInfoBean.getPort()));
			ps.setString(6, llCompInfoBean.getReMark());
			ps.setString(7, llCompInfoBean.getPushAddr());
			int length = ps.executeUpdate();
			if(length > 0){
				returnFlag = true;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"数据插入异常");
			return false;
		} finally{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"连接关闭异常");
			}
		}
		return returnFlag;
	}

	public boolean updateLlCompInfo(LlCompInfoVo llCompInfoBean) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean returnFlag = false;
		try {
			ReportQuerySql reportQuerySql = new DataQuerySQLFactory().getSearchSql(StaticValue.DBTYPE);
			String sql = reportQuerySql.getLlCompInfoUpdateSql();
			//String sql="UPDATE LL_COMP_INFO SET PASSWORD=?,ECID=?,ECNAME=?,IP=?,PORT=?,REMARK=?,PUSHADDR=?,UPDATETM=sysdate";
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setString(1,llCompInfoBean.getPassword());
			ps.setString(2,llCompInfoBean.getCorpCode());
			ps.setString(3, llCompInfoBean.getEcName());
			ps.setString(4, llCompInfoBean.getIp());
			ps.setInt(5, Integer.parseInt(llCompInfoBean.getPort()));
			ps.setString(6, llCompInfoBean.getReMark());
			ps.setString(7, llCompInfoBean.getPushAddr());
			int length = ps.executeUpdate();
			if(length > 0){
				returnFlag = true;
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"数据更新异常");
			returnFlag =  false;
			return returnFlag;
		}finally{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"连接关闭异常");
			}
		}
		return returnFlag;
	}
}
