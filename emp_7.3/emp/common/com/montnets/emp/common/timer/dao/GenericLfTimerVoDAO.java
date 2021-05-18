package com.montnets.emp.common.timer.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.table.system.TableLfTimer;
import com.montnets.emp.util.PageInfo;

/**
 * @project sinolife
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-31 上午11:42:18
 * @description
 */

public class GenericLfTimerVoDAO  extends SuperDAO
{

	private IGenericDAO empDao =new DataAccessDriver().getGenericDAO();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public List<LfTimer> findLfTimerVo(LfTimer lfTimer,
			Long userId, PageInfo pageInfo) throws Exception
	{
		//查询字段
		String fieldSql = GenericLfTimerVoSQL.getFieldSql();
		//查询表
		String tableSql = GenericLfTimerVoSQL.getTableSql();
		//权限
		String dominationSql = GenericLfTimerVoSQL.getDominationSql(String
				.valueOf(userId));
		//条件
		String conditionSql = GenericLfTimerVoSQL.getConditionSql(lfTimer);
		//排序
		String orderBySql = GenericLfTimerVoSQL.getOrderBySql();
		//组装sql
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				dominationSql).append(conditionSql).append(orderBySql)
				.toString();
		//分页
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(dominationSql).append(conditionSql)
				.toString();
		//执行查询
		List<LfTimer> returnVoList =new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(LfTimer.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		//返回结果
		return returnVoList;
	}
	
	/**
	 * 获取运行中且到期的定时任务
	 * @return 返回定时任务对象集合
	 */
	public List<LfTimer> findTimerTask() {
		
		try
		{
			//拼接sql
			String sql = new StringBuffer("select * from ")
			.append(TableLfTimer.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ")
			.append(TableLfTimer.RUNSTATE).append(" = 1 ")
			.append(" and ").append(TableLfTimer.NEXT_TIME).append(" <= ").append(empDao.getTimeCondition(sdf.format(new Date())))
			.toString();
			
			List<LfTimer> returnList = findEntityListBySQL(LfTimer.class,
					sql, StaticValue.EMP_POOLNAME);
			//返回结果
			return returnList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取运行中且到期的定时任务异常。");
			return null;
		}
		
	}
	
}
