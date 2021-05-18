package com.montnets.emp.degree.dao;

import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.degree.vo.LfDegreeManageVo;
import com.montnets.emp.util.PageInfo;

public class DegreeDAO extends SuperDAO {
	public static List<LfDegreeManageVo> findLfDegreeManageVo(
			LfDegreeManageVo lfDegreeManageVo, PageInfo pageInfo ,String orderBy)
			throws Exception {
		//查询字段拼接
		String fielSql = DegreeSql.getFieldSql();
		
		//查询表拼接
		String tableSql = DegreeSql.getTableSql();
		
		//查询条件拼接
		String conditionSql = DegreeSql.getConditionSql(lfDegreeManageVo, orderBy,pageInfo);
		
		// 排序条件拼接
		String orderBySql = DegreeSql.getOrderBySql(orderBy);
		
		//总sql
		String sql = new StringBuffer(fielSql).append(tableSql).append(conditionSql)
							.append(orderBySql).toString();
		
		// 组装统计SQL语句
		String countSql = new StringBuffer("select count(*) totalcount from (")
				.append(fielSql).append(tableSql).append(conditionSql).append(") A").toString();
		
		// 调用查询语句
		List<LfDegreeManageVo> lfDegreeManageVoList = new DataAccessDriver()
				.getGenericDAO().findPageVoListBySQL(
						LfDegreeManageVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		
		return lfDegreeManageVoList;
	}
	
	public List<LfDegreeManageVo> findLfDegreeManageVo(
			LfDegreeManageVo lfDegreeManageVo, String orderBy)
			throws Exception {
		//查询字段拼接
		String fielSql = DegreeSql.getFieldSql();
		
		//查询表拼接
		String tableSql = DegreeSql.getTableSql();
		
		//查询条件拼接
		String conditionSql = DegreeSql.getConditionSql(lfDegreeManageVo, orderBy);
		
		// 排序条件拼接
		String orderBySql = DegreeSql.getOrderBySql(orderBy);
		
		//总sql
		String sql = new StringBuffer(fielSql).append(tableSql).append(conditionSql)
							.append(orderBySql).toString();
		
		// 调用查询语句
		List<LfDegreeManageVo> lfDegreeManageVoList = findVoListBySQL(LfDegreeManageVo.class, sql, StaticValue.EMP_POOLNAME);
		
		return lfDegreeManageVoList;
	}
	
}
