package com.montnets.emp.auditpro.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;
/**
 *  流程模板管理数据库 层
 * @author Administrator
 *
 */
public class AuditProDAO extends SuperDAO{
	
	/**
	 *   获取审核流程管理 列表信息 
	 * @param conditionMap	查询条件
	 * @param lfsysuser	当前用户
	 * @param pageInfo	分页
	 * @return
	 */
	public List<DynaBean> findFlowsbyMap(LinkedHashMap<String, String> conditionMap,LfSysuser lfsysuser,PageInfo pageInfo) {
		
		List<DynaBean> beanList = null;
		String flowtask = "";
		if(!"".equals(conditionMap.get("flowtask"))  && conditionMap.get("flowtask") != null){
			flowtask = conditionMap.get("flowtask");
		}
		Integer flowtype = null;
		if(!"".equals(conditionMap.get("flowtype")) && conditionMap.get("flowtype") != null){
			flowtype = Integer.valueOf(conditionMap.get("flowtype"));
		}
		String flowname = "";
		if(!"".equals(conditionMap.get("flowname"))  && conditionMap.get("flowname") != null){
			flowname = conditionMap.get("flowname");
		}
		String sql = "select flow.F_ID,flow.flowstate,F_TASK,flow.R_LEVELAMOUNT,flow.COMMENTS,flow.CORP_CODE,flow.CREATE_USER_ID,flow.CREATE_TIME,flow.UPDATE_TIME,sysuser.NAME " ;
		
		String countSql = "select count(*) totalcount ";
		
		String baseSql = " from LF_FLOW flow inner join LF_SYSUSER sysuser on flow.CREATE_USER_ID = sysuser.USER_ID ";
		
		StringBuffer conditionSql = new StringBuffer("");
		conditionSql.append(" where flow.CORP_CODE ='").append(lfsysuser.getCorpCode()).append("'");
		//权限
		conditionSql.append(" and (sysuser.USER_ID= ").append(lfsysuser.getUserId())
		.append(" or sysuser.DEP_ID in (select DEP_ID from LF_DOMINATION where USER_ID=")
		.append(lfsysuser.getUserId()).append(")) ");
		if(flowtask != null && !"".equals(flowtask)){
			conditionSql.append(" and flow.F_TASK like '%").append(flowtask).append("%'");
		}
		if(flowname != null && !"".equals(flowname)){
			conditionSql.append(" and sysuser.NAME like '%").append(flowname).append("%'");
		}
		if(flowtype != null){
			String otherSql = " inner join LF_FLOWBINDTYPE flowtype on flow.F_ID = flowtype.F_ID ";
			conditionSql.append(" and flowtype.INFO_TYPE =").append(flowtype);
			baseSql += otherSql;
		}
		String orderSql = " order by flow.F_ID DESC";
		
		sql += baseSql;
		countSql += baseSql;
		sql += conditionSql + orderSql;
		countSql += conditionSql;
		
		beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		return beanList;
	}
	
	
	

}
