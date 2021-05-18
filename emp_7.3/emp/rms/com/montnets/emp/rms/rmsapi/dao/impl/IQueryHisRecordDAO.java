package com.montnets.emp.rms.rmsapi.dao.impl;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.rms.rmsapi.dao.QueryHisRecordDAO;


public class IQueryHisRecordDAO extends BaseBiz implements QueryHisRecordDAO  {
	/**
	 * 通过sp账号获取密码
	 * @author chenlinyan
	 * @param userid sp账号
	 * @return 密码
	 */
	public String findPwdByUserId(String userid) {
		List<Userdata> list = null;
		String pwd = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("userId", userid);
		try {
			list = empDao.findListBySymbolsCondition(Userdata.class,conditionMap, null);
			if (null!=list&&list.size() > 0) {
				pwd = list.get(0).getUserPassword();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, e.getMessage());
		}
		return pwd;
	}
}
