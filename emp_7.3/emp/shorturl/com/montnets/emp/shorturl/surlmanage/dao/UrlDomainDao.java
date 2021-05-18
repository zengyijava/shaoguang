package com.montnets.emp.shorturl.surlmanage.dao;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;

import com.montnets.emp.shorturl.surlmanage.entity.LfDomain;
import com.montnets.emp.util.PageInfo;
import org.apache.commons.beanutils.DynaBean;

public class UrlDomainDao extends SuperDAO{

	public List<LfDomain> getDomains(LinkedHashMap<String, String> conditionMap,PageInfo pageInfo)throws Exception{
		String sql = "select ID,DOMAIN,LEN_ALL,LEN_EXTEN,DTYPE,FLAG,VALID_DAYS,CREATE_UID,CREATE_USER,CREATE_TM,UPDATE_UID,UPDATE_TM,REMARK from LF_DOMAIN where 1=1 " ;
		String conditionSql = getConditionSql(conditionMap);

		sql = sql + conditionSql;
		
		String countSql = "select count(*) totalcount FROM LF_DOMAIN ";
		
		List<LfDomain> returnList = new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(
				LfDomain.class, sql.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		
		return returnList;
		
	}
	
	/**
	 * 组装SQL过滤语句
	 * @param conditionMap
	 * @return
	 */
	private String getConditionSql(LinkedHashMap<String, String> conditionMap) throws Exception{
		String sql = "";
		if (conditionMap.size()<1) {
			return sql;
		}
		//短域名
		if (conditionMap.get("domain")!=null&&!"".equals(conditionMap.get("domain").trim())) {
			sql += " and DOMAIN like'%"+conditionMap.get("domain")+"%' ";
		}
		//应用类型
		if (conditionMap.get("dtype")!=null&&!"".equals(conditionMap.get("dtype").trim())) {
			sql += " and DTYPE like'%"+conditionMap.get("dtype").trim()+"%' ";
		}
		//状态
		String flag = conditionMap.get("flag");
		if (flag!=null&&!"".equals(flag)) {
			
			sql += " and FLAG="+flag;
		}
		
		//创建人
		if (conditionMap.get("createUser")!=null&&!"".equals(conditionMap.get("createUser").trim())) {
			sql += " and CREATE_USER like'%"+conditionMap.get("createUser").trim()+"%'";
		}
		
		//开始时间
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		if (conditionMap.get("startTime")!=null&&!"".equals(conditionMap.get("startTime").trim())) {
			
			sql += " and CREATE_TM >= "+genericDao.getTimeCondition(conditionMap.get("startTime").trim());
		}
		//结束时间
		if (conditionMap.get("recvtime")!=null&&!"".equals(conditionMap.get("recvtime").trim())) {
			sql += " and CREATE_TM <= "+genericDao.getTimeCondition(conditionMap.get("recvtime").trim());
		}
		return sql;
	}


    public List<DynaBean> getDomainByIds(String newList) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM LF_DOMAIN WHERE ID IN(").append(newList).append(")");
	    return new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql.toString());
    }

}
