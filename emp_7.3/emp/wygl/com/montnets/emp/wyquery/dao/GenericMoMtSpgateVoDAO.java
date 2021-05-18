package com.montnets.emp.wyquery.dao;

import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.wyquery.vo.SysMoMtSpgateVo;

public class GenericMoMtSpgateVoDAO extends SuperDAO{

	/**
	 * 系统上行
	 * @param sysMoMtSpVo
	 * @return
	 * @throws Exception
	 */
	public List<SysMoMtSpgateVo> findSysMoMtSpVoVo(SysMoMtSpgateVo sysMoMtSpVo)throws Exception {
		//获取查询的字段
		String fieldSql = GenericMoMtSpgateVoSQL.getFieldSql(sysMoMtSpVo);
		
		String sql = new StringBuffer(fieldSql).toString();
		EmpExecutionContext.sql("execute sql: "+sql);		
		//返回集合
		List<SysMoMtSpgateVo> returnVoList = findVoListBySQL(SysMoMtSpgateVo.class, sql,
				StaticValue.EMP_POOLNAME, null);
		return returnVoList;
	}


}
