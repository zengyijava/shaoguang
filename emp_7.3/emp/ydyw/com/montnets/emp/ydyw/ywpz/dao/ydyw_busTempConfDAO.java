package com.montnets.emp.ydyw.ywpz.dao;

import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.ydyw.ywpz.vo.LfBusTailTmpVo;

public class ydyw_busTempConfDAO extends SuperDAO{

	public List<LfBusTailTmpVo> findByParams(String busName, String busCode,
			String depId, String userId, String startTime, String endTime, 
			PageInfo pageInfo, String corpCode) throws Exception {
		//SQL语句
		String sql = ydyw_busTempConfSQL.retfindByParamsSQL(busName, busCode,
				depId, userId, startTime, endTime, corpCode);
				
		//
		String countSql = "select count(*) totalCount from (" + sql + ") count_tb";
		
		//排序
		sql += ydyw_busTempConfSQL.retSqlOrdby();
		
		//分页查询
		List<LfBusTailTmpVo> list = new DataAccessDriver().getGenericDAO()
				.findPageVoListBySQL(LfBusTailTmpVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		
		//返回list集合
		return list;
	}
	
	public List<LfBusTailTmpVo> findByParams2(String busName, String busCode,
			String depId, String userId, String startTime, String endTime, 
			PageInfo pageInfo, String corpCode) throws Exception {
		//SQL语句
		String sql = ydyw_busTempConfSQL.retfindByParamsSQL2(busName, busCode,
				depId, userId, startTime, endTime, corpCode);
				
		//
		String countSql = "select count(*) totalCount from (" + sql + ") count_tb";
		
		//排序
		sql += ydyw_busTempConfSQL.retSqlOrdby2();
		
		//分页查询
		List<LfBusTailTmpVo> list = new DataAccessDriver().getGenericDAO()
				.findPageVoListBySQL(LfBusTailTmpVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		
		//返回list集合
		return list;
	}
}
