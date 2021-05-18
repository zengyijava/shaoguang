package com.montnets.emp.weix.dao.i;

import java.sql.Connection;
public interface IMenuDao 
{
	/**
	 * 更新菜单顺序
	 * 
	 * @param pid
	 * @param morder
	 * @return -1:执行失败。大于等于0则为执行成功
	 */
	public int updateMenuOrder(Connection conn, Long pid, Integer morder, Long aid);
}
