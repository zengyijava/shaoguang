package com.montnets.emp.shorturl.surlmanage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.shorturl.surlmanage.vo.LfOperNeturlVo;
import com.montnets.emp.util.PageInfo;

public class UrlOperUrlAuditDao extends SuperDAO{

	
	/**
	 * 获取所有的审核人姓名
	 * @return
	 */
	public Map<String, String> getAllAuditer(){
		Map<String, String> auditers = new HashMap<String, String>(); 
		String sql = "select USER_ID,USER_NAME from LF_SYSUSER where CORP_CODE=100000";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			auditers.clear();
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs!=null&& rs.next()) {
				auditers.put(rs.getString(1), rs.getString(2));
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取审核人姓名map异常！");
		}finally{
			try {
				close(rs, ps,conn);
			} catch (Exception e2) {
				EmpExecutionContext.error(e2,"获取审核人");
			}
		}
		return auditers;
	}
	
	
	/**
	 * 获取连接地址名称list 下拉框
	 * @return
	 */
	public List<String> getAllUrlName(){
		String sql = "select url_name from LF_NETURL  group by url_name";
		List<String> nameList = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			nameList.clear();
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs!=null&&rs.next()) {
				nameList.add(rs.getString(1));
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取连接地址名称list异常！");
		}finally{
			try {
				close(rs, ps, conn);
			} catch (Exception e2) {
				EmpExecutionContext.error(e2,"获取连接地址名称list-关闭数据库连接异常");
			}
		}
		return nameList;
	}
	
	/**
	 * 运营商url详情
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 */
	public List<LfOperNeturlVo> findurlLists(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo)throws Exception{
		String sql = "select net.ID,net.URLSTATE,net.CORP_CODE,crop.CORP_NAME,net.URL_NAME,net.SRC_URL,net.URL_MSG," +
				"net.CREATE_UID,uses.NAME,net.CREATE_TM,net.ISPASS,net.AUDIT_UID,net.REMARKS,net.REMARKS1 " +
				" from LF_NETURL net " +
				" left join LF_CORP crop on net.CORP_CODE=crop.CORP_CODE " +
				" left join LF_SYSUSER uses on net.CREATE_UID = uses.USER_ID  " +
				" where 1=1";
		String conditionSql = getConditionSql(conditionMap);
		
		sql = sql + conditionSql;
		
		String countSql = "select count(*) totalcount FROM (";
        countSql += sql;
        countSql += " ) A";
        sql += " order by net.create_tm desc";
        List<LfOperNeturlVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
        		LfOperNeturlVo.class, sql.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
        
		return returnList;
	}
	
	
	/**
	 * 组装SQL语句
	 * @param conditionMap
	 * @return
	 */
	private String getConditionSql(LinkedHashMap<String, String> conditionMap) {
		String sql = "";
		if (conditionMap.size()<1) {
			return sql;
		}
		//连接地址名称 下拉框
		if (conditionMap.get("urlname")!=null&&!"".equals(conditionMap.get("urlname").trim())) {
			sql += " and net.URL_NAME ='"+conditionMap.get("urlname").trim()+"' ";
		}
		//连接地址
		if (conditionMap.get("srcurl")!=null&&!"".equals(conditionMap.get("srcurl").trim())) {
			sql += " and net.SRC_URL like '%"+conditionMap.get("srcurl").trim()+"%'";
		}
		//审核状态
		if (conditionMap.get("ispass")!=null&&!"".equals(conditionMap.get("ispass").trim())) {
			String ispass = conditionMap.get("ispass").trim();
			if ("-2".equals(ispass)) {
				sql += " and net.ISPASS <= "+ispass;
			}else {
				sql += " and net.ISPASS = "+ispass;
			}
		}
		//企业编号
		if (conditionMap.get("corpnum")!=null&&!"".equals(conditionMap.get("corpnum").trim())) {
			sql += " and net.CORP_CODE="+conditionMap.get("corpnum").trim();
		}
		//企业名称
		if (conditionMap.get("cropname")!=null&&!"".equals(conditionMap.get("cropname").trim())) {
			sql += " and crop.CORP_NAME like '%"+conditionMap.get("cropname").trim()+"%'";
		}
		//开始时间
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		if (conditionMap.get("startTime")!=null&&!"".equals(conditionMap.get("startTime").trim())) {
			sql += "  and net.CREATE_TM >= "+genericDao.getTimeCondition(conditionMap.get("startTime").trim());
		}
		//结束时间
		if (conditionMap.get("recvtime")!=null&&!"".equals(conditionMap.get("recvtime").trim())) {
			sql += "  and net.CREATE_TM <= "+genericDao.getTimeCondition(conditionMap.get("recvtime").trim());
		}
		
		return sql;
	}
	
	
	
	/**
	 * 提交运营商审核
	 * @param id
	 * @param remarks
	 * @param ispass
	 * @return
	 */
	public boolean update(String id, String remarks, String ispass,String userid) {
	    //兼容多种数据库处理
	    String multiSql = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE ? "NOW()" :
                StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE ? "GETDATE()" :
                    StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE ? "SYSDATE" : "CURRENT DATE";

		String sql = "update LF_NETURL SET ISPASS = ?,REMARKS=?,AUDIT_UID=?,AUDIT_TM=" + multiSql + " WHERE ID =? ";
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = -1;
		boolean result = false;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.valueOf(ispass));
			ps.setString(2, remarks.trim());
			ps.setInt(3, Integer.valueOf(userid));
			ps.setInt(4, Integer.valueOf(id));
			rs = ps.executeUpdate();
			if (rs>0) {
				result = true;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "提交运营商审核失败！");
		}finally{
			try {
				close(null,ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "提交运营商审核操作-关闭数据库连接异常");
			}
		}
		return result;
	}
	
	/**
	 * 提交运营商禁用
	 * @param id
	 * @param remarks1
	 * @param ispass
	 * @return
	 */
	public boolean stop(String id, String remarks1, String ispass,String userid) {
        //兼容多种数据库处理
        String multiSql = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE ? "NOW()" :
                StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE ? "GETDATE()" :
                        StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE ? "SYSDATE" : "CURRENT DATE";

		String sql = "update LF_NETURL SET ISPASS = ?,REMARKS1=?,STOP_UID=?,STOP_TM=" + multiSql + " WHERE ID =? ";
		Connection conn = null;
		PreparedStatement ps = null;
		int rs = -1;
		boolean result = false;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.valueOf(ispass));
			ps.setString(2, remarks1.trim());
			ps.setInt(3, Integer.valueOf(userid));
			ps.setInt(4, Integer.valueOf(id));
			rs = ps.executeUpdate();
			if (rs>0) {
				result = true;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "提交运营商禁用失败！");
		}finally{
			try {
				close(null,ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "提交运营商禁用操作-关闭数据库连接异常");
			}
		}
		return result;
	}
	
	
	
}
