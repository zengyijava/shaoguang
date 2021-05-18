package com.montnets.emp.wxmanger.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

public class NetCheckDao extends SuperDAO{	
	
	/***
	 * 查询网讯基本表的相关信息
	 */
	public List<DynaBean> getBaseInfos(LinkedHashMap<String, String> conMap,PageInfo pageInfo,String userId)
	{
		List<DynaBean> beans = null;
		String fieldSql ="select a.*,b.name as username ";
		
		String tableSql = " From LF_WX_BASEINFO as a left join lf_sysuser as b on a.creatid=b.USER_ID ";
		
		String dominationSql = getDominationSql(userId);
		String conditionSql=getConditionSql(conMap);
		String orderbySql = " order by a.NETID DESC";
		String sql=fieldSql+tableSql+dominationSql+conditionSql+orderbySql;
		String countSql = new StringBuffer("select count(*) totalcount ").append(tableSql).append(dominationSql).append(conditionSql).toString(); 
		beans = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		return beans;
	}
	
	//获取权限
	public  String getDominationSql(String userId)
	{
		StringBuffer dominationSql = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(userId);
		String sql = new StringBuffer(" where (b.").append(
				TableLfSysuser.USER_ID).append("=").append(userId).append(
				" or b.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(dominationSql).append(")")
				.append(")").toString();
		return sql;
	}
	
	/***
	 * 查询基本表所需要增加的查询条件 
	 */
	public String getConditionSql(LinkedHashMap<String, String> conMap)
	{
		StringBuffer sb = new StringBuffer();
		String id = conMap.get("id");
		if(id != null && !"".equals(id)){
			sb.append(" and a.id like '%").append(id).append("%'");
		}
		String status = conMap.get("status");
		if(status != null && !"".equals(status)){
			sb.append(" and a.status =").append(status);
		}else{
			sb.append(" and a.status in(1,2,3)");
		}
		String name = conMap.get("name");
		if(name != null && !"".equals(name)){
			sb.append(" and a.name like '%").append(name).append("%'");
		}
		String user = conMap.get("user");
		if(user != null && !"".equals(user)){
			sb.append(" and b.name like '%").append(user).append("%'");
		}
		String chDate = conMap.get("chDate");
		if(chDate != null && !"".equals(chDate)){
			sb.append(" and a.modifydate >='").append(chDate).append("'");
		}
		String chEndDate = conMap.get("chEndDate");
		if(chEndDate != null && !"".equals(chEndDate)){
			sb.append(" and a.modifydate <='").append(chEndDate).append("'");
		}
		String corpCode = conMap.get("corpCode");
		if(corpCode!=null && !"".equals(corpCode)){
			sb.append(" and a.corp_code ='").append(corpCode).append("'");
		}
		return sb.toString();
	}
	
	/**
	 * 网讯审核生成网讯访问页面
	 * 
	 * @param netId
	 * @param iphORhe
	 * @param corpcode
	 * @return Map
	 */
	public List<DynaBean> getNetByJsp(int netId, String corpcode) {
		String sql = "SELECT P.ID as id,P.NETID as NETID,P.PARENTID as PARENTID,"+
            "P.CHILDID as CHILDID, P.CONTENT as CONTENT, P.PAGESIZE as PAGESIZE,"+
            "P.MODIFYID as MODIFYID,P.MODIFYDATE as MODIFYDATE,P.CREATID as CREATID, "+
            "P.CREATDATE as CREATDATE,b.share as share FROM LF_WX_PAGE AS P ,LF_WX_baseinfo b"+
        " WHERE P.NETID ="+netId+" and b.NETID = "+netId +" and b.corp_code="+corpcode;

		return  new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, null, null, StaticValue.EMP_POOLNAME, null);
	}
}
