package com.montnets.emp.sysuser.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.table.sysuser.TableLfDep;
/**
 * @project montnets_dao
 * @author liujianjun <646654831@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-8-6 上午11:16:07
 * @description 
 */

public class SysuserDepDAO extends SuperDAO{

	
	
	
	/**
	 * 查询操作员机构数(depId不为空时查询该机构的第一级子机构数，depId为空时查询机构总数)
	 * @param corpCode
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public Integer getDepCount(String corpCode,String depId) throws Exception{
		String sql = "";
		if(depId != null){
			//如果depId不为空查询该机构的第一级子机构
			sql = new StringBuffer("select count(*) from ").append(TableLfDep.TABLE_NAME)
			.append(" where ").append(TableLfDep.CORP_CODE).append(" = '").append(corpCode)
			.append("' and ").append(TableLfDep.SUPERIOR_ID).append(" = ").append(depId)
			.append(" and ").append(TableLfDep.DEP_STATE).append("=1").toString();
		}else{
			//depId为空，查询所有机构数
			sql = new StringBuffer("select count(*) from ").append(TableLfDep.TABLE_NAME)
			.append(" where ").append(TableLfDep.CORP_CODE).append(" = '").append(corpCode)
			.append("' and ").append(TableLfDep.DEP_STATE).append("=1").toString();
		}
		
		return this.getCountBySql(sql, StaticValue.EMP_POOLNAME);
	}
	
	public Integer getCountBySql(String sql, String POOLNAME) throws Exception
	{
		Integer count = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			//获取数据库连接
			conn = connectionManager.getDBConnection(POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "数据库操作异常！");
			throw e;
		} finally {
			try {
				//关闭连接
				close(null, ps, conn);
			} catch (SQLException e) {
				EmpExecutionContext.debug("关闭数据库资源出错！");
			}
		}
		return count;
	}
	

}
