package com.montnets.emp.wxgl.dao;

import java.sql.Connection;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.table.wxgl.TableLfWeiMenu;

public class MenuDao extends SuperDAO
{
	/**
	 * 更新菜单顺序
	 * 
	 * @param pid
	 * @param morder
	 * @return -1:执行失败。大于等于0则为执行成功
	 */
	public int updateMenuOrder(Connection conn, Long pid, Integer morder, Long aid)
	{
		String sql = "update "+TableLfWeiMenu.TABLE_NAME+" set m_order=m_order-1 where p_id=" + pid.toString() + " and m_order >" + morder.toString() + " and a_id=" + aid.toString();

		try
		{
			return executeBySQLReturnCount(conn, sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新微信自定义菜单失败！");
			return -1;
		}
	}
}
