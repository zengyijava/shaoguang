package com.montnets.emp.engine.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.engine.LfService;
import com.montnets.emp.entity.system.LfMotask;

/**
 * 上行业务记录dao
 * @project emp_std_192.169.1.81
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-12-13 上午09:01:21
 * @description
 */
public class MoHandleDao extends SuperDAO
{
	/**
	 * 根据指令id和发送账号获取对应的上行业务对象
	 * @param moTask 上行信息对象
	 * @return 返回指令和发送账户对应的上行业务对象
	 */
	public LfService getSerByOrder(LfMotask moTask) {
		try
		{
			//查询sql
			String sql = " select * from LF_SERVICE "
				+ " where SER_TYPE = 1 "
				+ " and IDENTIFY_MODE = 2 "
				+ " and SP_USER = '"+moTask.getSpUser()+"' "
				+ " and STRUCTCODE = (select STRUCTCODE from A_CMD_ROUTE where ID = "+moTask.getMoOrder()+" ) ";
			//执行查询，获取结果对象
			List<LfService> servicesList = findEntityListBySQL(LfService.class, sql,
					StaticValue.EMP_POOLNAME);
			//有值则返回
			if(servicesList != null && servicesList.size() > 0){
				return servicesList.get(0);
			}

			return null;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "智能引擎获取网讯模板异常。");
			return null;
		}
	}
	
	/**
	 * 更具网讯id获取网讯的page
	 * @param netid 网讯id
	 * @return 返回网讯page的动态bean对象
	 */
	public List<DynaBean> getWxPageById(String netid) {
		try
		{
			String sql = "select * from LF_WX_PAGE wxpage where NETID =" + netid + " and PARENTID = 0 ";

			List<DynaBean> list = this.getListDynaBeanBySql(sql);
			return list;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "智能引擎获取网讯模板异常。");
			return null;
		}
	}
	

}
