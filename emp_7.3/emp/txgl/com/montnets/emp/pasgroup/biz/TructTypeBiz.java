package com.montnets.emp.pasgroup.biz;

import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.entity.LfTructType;

public class TructTypeBiz extends SuperBiz{

	ErrorLoger errorLoger = new ErrorLoger();
	/**
	 * 获取指令类型集合
	 * @return
	 * @throws Exception
	 */
	public List<LfTructType> findAllTructTypes()throws Exception{
		List<LfTructType> tructList ;
		try {
			tructList  = empDao.findListByCondition(LfTructType.class, null, null);
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"获取指令类型集合biz层异常！"));
			//异常抛出
			throw e;
		}
		return tructList;
	}
	
	
	
}
