package com.montnets.emp.rms.templmanage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.rms.vo.LfShortTemplateVo;
import com.montnets.emp.util.PageInfo;

public class RmsShortTemplateDao extends SuperDAO{

	public List<LfShortTemplateVo> getLfShortTemplate(PageInfo pageInfo,LfShortTemplateVo bean) throws Exception {
		List<LfShortTemplateVo> returnList;
		
		String	searchSql = "SELECT ID,TEMPID,TEMPNAME FROM LF_SHORTTEMP WHERE USERID = "+bean.getUserId()+ " AND CORPCODE = '"+bean.getCorpCode()+"'";
		
		if(pageInfo != null)
		{
			//总条数语句
			String countSql = "select count(*) totalcount FROM (" + searchSql + " ) A";
			returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
					LfShortTemplateVo.class, searchSql, countSql, pageInfo,
					StaticValue.EMP_POOLNAME);
		}
		else
		{
			returnList = findVoListBySQL(LfShortTemplateVo.class, searchSql, StaticValue.EMP_POOLNAME);
		}
		 
		return returnList; 
	}
	public List<LfShortTemplateVo> getLfShortTempList(LfShortTemplateVo bean) throws Exception {
		List<LfShortTemplateVo> returnList;
		String	sql = "SELECT ID,TEMPID,TEMPNAME FROM LF_SHORTTEMP WHERE CORPCODE = '"+bean.getCorpCode()+"'";
		 
		returnList = findVoListBySQL(LfShortTemplateVo.class, sql, StaticValue.EMP_POOLNAME);
		return returnList; 
	}
	
	public boolean deleteShortTemp(LfShortTemplateVo bean) {
		boolean flag = true;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String  sql = "DELETE FROM LF_SHORTTEMP WHERE TEMPID="+bean.getTempId()+" AND USERID= "+bean.getUserId()+" AND CORPCODE="+bean.getCorpCode();
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			int length = ps.executeUpdate();
			if(length > 0){
				flag = true;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取LF_SHORTTEMP表数据失败！");
			return false;
		}
		finally 
		{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "获取LF_SHORTTEMP表数据失败！");
			}
		}
		return flag;
	}

	public boolean addShortTemp(LfShortTemplateVo bean) {
		boolean flag = true;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//兼容多数据库插入系统当前时间值
		String  sql = "INSERT INTO  LF_SHORTTEMP(USERID,CORPCODE,TEMPID,TEMPNAME,ADDTIME) VALUES(?,?,?,?,GETDATE())";

		switch (StaticValue.DBTYPE){
			case StaticValue.ORACLE_DBTYPE:
				sql = "INSERT INTO  LF_SHORTTEMP(USERID,CORPCODE,TEMPID,TEMPNAME,ADDTIME) VALUES(?,?,?,?,SYSDATE)";
				break;
			case StaticValue.DB2_DBTYPE:
				sql = "INSERT INTO  LF_SHORTTEMP(USERID,CORPCODE,TEMPID,TEMPNAME,ADDTIME) VALUES(?,?,?,?,CURRENT TIMESTAMP)";
				break;
			case StaticValue.MYSQL_DBTYPE:
				sql = "INSERT INTO  LF_SHORTTEMP(USERID,CORPCODE,TEMPID,TEMPNAME,ADDTIME) VALUES(?,?,?,?,NOW())";
				break;
		}

		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setLong(1, bean.getUserId());
			ps.setString(2, bean.getCorpCode());
			ps.setLong(3, bean.getTempId());
			ps.setString(4, bean.getTempName());
			int length = ps.executeUpdate();
			if(length > 0){
				flag = true;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取LF_SHORTTEMP表数据失败！");
			return false;
		}
		finally 
		{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "获取LF_SHORTTEMP表数据失败！");
			}
		}
		return flag;
	}

	public LfShortTemplateVo getLfShortTemplate(LfShortTemplateVo bean) {
		LfShortTemplateVo returnBean = new LfShortTemplateVo();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String  sql = "SELECT ID FROM LF_SHORTTEMP WHERE TEMPID = "+bean.getTempId() + " AND USERID = "+bean.getUserId()+ " AND CORPCODE = '"+bean.getCorpCode()+"'";
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				returnBean.setId(rs.getInt("ID"));
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "根据模板ID"+bean.getTempId()+"获取LF_SHORTTEMP表的ID失败！");
			return null;
		}
		finally 
		{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "获取LF_SHORTTEMP表数据失败！");
			}
		}
		return returnBean;
	}

	public int getNumber(LfShortTemplateVo bean) {
		int returnNum = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String  sql = "SELECT COUNT(1) NUM FROM LF_SHORTTEMP WHERE USERID = "+bean.getUserId()+ " AND CORPCODE = '"+bean.getCorpCode()+"'";
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				returnNum = rs.getInt("NUM");
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取LF_SHORTTEMP表的条数失败！");
			return 0;
		}
		finally 
		{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "获取LF_SHORTTEMP表数据失败！");
			}
		}
		return returnNum;
	}

	public String getPrivilegeId(String menusite,LfShortTemplateVo bean) {
		String returnStr = null ;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String  sql = "SELECT RESOURCE_ID FROM LF_PRIVILEGE WHERE MENUSITE = '"+menusite+"'";
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				returnStr = rs.getString("RESOURCE_ID");
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取LF_PRIVILEGE表的PRIVILEGE_ID失败！");
			return "";
		}
		finally 
		{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "获取LF_PRIVILEGE表的PRIVILEGE_ID失败！");
			}
		}
		return returnStr;
	}

	public boolean updateLfTemplate(long id,int param) {
		boolean flag =true;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql="UPDATE LF_TEMPLATE SET ISSHORTTEMP="+param+" WHERE TM_ID="+id;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			int length = ps.executeUpdate();
			if(length > 0){
				flag = true;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取LF_SHORTTEMP表数据失败！");
			return false;
		}
		finally 
		{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "获取LF_SHORTTEMP表数据失败！");
			}
		}
		return flag;
	}

}
