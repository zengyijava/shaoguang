package com.montnets.emp.servmodule.txgl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.RowSetDynaClass;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.security.context.ErrorLoger;

public class ViewParamsDAO extends SuperDAO
{
	ErrorLoger errorLoger = new ErrorLoger();
	/**
	 * 获取当前位置
	 * @return 当前位置集合 
	 */
	public List<DynaBean> getPosition()
	{
		String sql = "select pr.menucode,pr.menuName,pr.modname,tm.TITLE,pr.RESOURCE_ID from LF_PRIVILEGE pr "+
			"left join LF_THIR_MENUCONTROL tm on tm.PRI_MENU= pr.RESOURCE_ID where pr.OPERATE_ID = 1";
		
		return this.getListDynaBeanBySql(sql);
	}
	

	public List<DynaBean> getListDynaBeanBySql(String sql)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DynaBean> dynaBeanList = null;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			RowSetDynaClass rsdc = new RowSetDynaClass(rs);
			dynaBeanList = rsdc.getRows();
			if(dynaBeanList==null){
				dynaBeanList=new ArrayList<DynaBean>(0);
			}

		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e, "根据sql语句查询并获取动态bean异常。"));
			return null;
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (Exception e)
			{
				EmpExecutionContext.error(errorLoger.getErrorLog(e, "关闭数据库资源失败。"));
			}
		}
		return dynaBeanList;
	}
}
