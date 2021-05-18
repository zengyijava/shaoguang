package com.montnets.emp.optlog.biz;

import java.util.List;
import java.util.Map;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.system.LfOpratelog;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.optlog.dao.OptLogDAO;
import com.montnets.emp.util.PageInfo;

/**
 * 操作日志biz
 * @author zm
 *
 */
public class OptLogBiz extends SuperBiz{
	
	public List<LfOpratelog> getUserOpratelog(LfSysuser lfSysuser,Map<String,String> conditionMap,PageInfo pageInfo){
		try {
			return new OptLogDAO().findUserOpratelog(lfSysuser,conditionMap,pageInfo);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询操作日志出现异常！");
		}
		return null;
	}

}
