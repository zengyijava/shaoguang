/**
 * 
 */
package com.montnets.emp.database;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-16 下午05:32:13
 * @description
 */

public interface IProcedureManager
{

	
	/**
	 * @param depId
	 * @param bizType
	 * @return int
	 * @throws Exception
	 */
	public int addClientByProc(Long depId,String bizType) throws Exception;

	public int updateTaotao(Long depid) throws Exception;


	/**
	 * @param depId
	 * @param bizType
	 * @return int
	 * @throws Exception
	 */
	public int addEmpolyeeByProc(Long depId,String bizType) throws Exception;

}
