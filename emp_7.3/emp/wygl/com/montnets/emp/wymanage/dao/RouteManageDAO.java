package com.montnets.emp.wymanage.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.util.PageInfo;

/**
 * 网优路由绑定dao
 * @description 
 * @project p_wygl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2014-3-27 上午09:30:30
 */
public class RouteManageDAO extends SuperDAO {
	
	/**
	 * 网优路由绑定查询
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-3-24 下午04:36:08
	 */
	public List<DynaBean> getRoute(PageInfo pageInfo,LinkedHashMap<String, String> conditionMap)
	{
		List<DynaBean> beanList = null;
		String sql = "select rt.*,ip.id ip_id,ip.gatename,ip.gateid ip_gateid,ip.corpsign,xt.spgate from a_gwroute rt left join a_ipcominfo ip on rt.gateid=ip.gateid left join XT_GATE_QUEUE xt on rt.gateid=xt.id ";
		String conSql="";
		String type = conditionMap.get("type");
		if(type!=null&&!"".equals(type))
		{
			conSql = " where rt.type =" + type ;
		}
		String orderSql=" order by rt.gateid asc";
		sql = sql + conSql.toString() + orderSql;
		if(pageInfo==null)
		{
			beanList = this.getListDynaBeanBySql(sql);
		}
		return beanList;
	}
	
	/**
	 * 网优路由绑定查询
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-3-24 下午04:36:08
	 */
	public List<DynaBean> getRouteNotBind(LinkedHashMap<String, String> conditionMap)
	{
		List<DynaBean> beanList = null;
		String type=conditionMap.get("type");
		String sql = "select ip.id ip_id,ip.gatename,ip.gateid ip_gateid,ip.corpsign,xt.spgate from  a_ipcominfo ip left join XT_GATE_QUEUE xt on ip.gateid=xt.id where ip.gateid not in(select gateid from a_gwroute where type="+type+")  order by gateid asc";
		beanList = this.getListDynaBeanBySql(sql);
		return beanList;
	}

}
