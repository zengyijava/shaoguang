package com.montnets.emp.wyquery.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wyquery.vo.SystemMtTaskVo;

/**
 * 实时记录查询
 * 
 * @project emp
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-5 下午03:57:19
 * @description
 */
public class GenericSystemMtTaskVoDAO extends SuperDAO {

	/**
	 * 带分的下行实时记录查询
	 * @param systemMtTaskVo  带条件的vo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findSystemMtTaskVo(
			SystemMtTaskVo systemMtTaskVo, PageInfo pageInfo) throws Exception {
		List<DynaBean> beanList = null;
        //获取查询列名
		String fieldSql = GenericSystemMtTaskVoSQL.getFieldSql();
        //获取查询表名 
		String tableSql = GenericSystemMtTaskVoSQL.getTableSql();
        //组装过滤条件
		String inter="";
		String conditionSql = GenericSystemMtTaskVoSQL
				.getConditionSql(systemMtTaskVo,inter);
		conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
        //时间段
		List<String> timeList = GenericSystemMtTaskVoSQL
				.getTimeCondition(systemMtTaskVo);
        //排序
		String orderbySql = GenericSystemMtTaskVoSQL.getOrderBySql();
        //组装sql语句
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderbySql).toString();
        //组装统计语句
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(conditionSql).toString();

		EmpExecutionContext.sql("execute sql: " + sql);

		if(pageInfo==null)
		{
			beanList = this.getListDynaBeanBySql(sql);
			
		}
		else
		{
			
			beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}

		return beanList;
	}

	public PageInfo getPageInfo(int preIndex, int pageSize,
			SystemMtTaskVo systemMtTaskVo) throws Exception {
		PageInfo pageInfo = new PageInfo();
		int totalCount = 0;

		String fieldSql = "select count(*) totalcount";

		String tableSql = GenericSystemMtTaskVoSQL.getTableSql();

		String conditionSql = GenericSystemMtTaskVoSQL
				.getConditionSql(systemMtTaskVo,"");
		conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		List<String> timeList = GenericSystemMtTaskVoSQL
				.getTimeCondition(systemMtTaskVo);

		String countSql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).toString();

		totalCount = findCountBySQL(countSql, timeList);
		int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize
				: totalCount / pageSize + 1;
		pageInfo.setTotalRec(totalCount);
		pageInfo.setTotalPage(totalPage);
		pageInfo.setPageSize(pageSize);

		pageInfo.setPageIndex(preIndex);
		return pageInfo;
	}
	
	
	public List<LfSysuser> findSysuserByNameorUserName(String code) throws Exception{
		//拼接sql
		String sql = new StringBuffer("select *")
		             .append(" from ").append(TableLfSysuser.TABLE_NAME).append(" sysuser "+StaticValue.getWITHNOLOCK()+" where sysuser.")
		             .append(TableLfSysuser.USER_NAME).append(" like '%").append(code).append("%' or sysuser.")
		             .append(TableLfSysuser.NAME).append(" like '%").append(code).append("%'")
		             .toString();
		//返回查询结果
		return findEntityListBySQL(LfSysuser.class, sql, StaticValue.EMP_POOLNAME);
	}
}
