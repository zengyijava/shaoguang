/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-5 上午10:12:59
 */
package com.montnets.emp.mobilebus.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;

/**
 * @description 
 * @project p_ydyw
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-5 上午10:12:59
 */

public class MobileBusDAO extends SuperDAO
{
	
	/**
	 * 获取套餐类型
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-7 下午01:45:44
	 */
	public List<DynaBean> getTaoCanType()
	{
		try
		{
			List<DynaBean> taoCanTypeList = null;
			String sql = "SELECT SUBFIELDVALUE,FIELDNAME FROM LF_PAGEFIELD WHERE MODLEID='2200-100' AND PAGEID='2200-1000' AND FIELDID='100005'";
			taoCanTypeList = getListDynaBeanBySql(sql);
			return taoCanTypeList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取套餐类型异常！");
			return null;
		}
	}
	
	/**
	 * 获取套餐指令分割符
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-23 下午03:51:04
	 */
	public List<DynaBean> getRrderDelimiter()
	{
		try
		{
			List<DynaBean> monConfigList = null;
			String sql = "SELECT * FROM LF_GLOBAL_VARIABLE WHERE GLOBALID IN(40)";
			monConfigList = getListDynaBeanBySql(sql);
			return monConfigList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取套餐指令分割符异常！");
			return null;
		}
	}
}
