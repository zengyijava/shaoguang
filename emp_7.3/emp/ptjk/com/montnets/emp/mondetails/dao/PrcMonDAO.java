package com.montnets.emp.mondetails.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.mondetails.dao.i.IPrcMonDAO;
import com.montnets.emp.monitor.dao.MonitorDAO;
import com.montnets.emp.util.PageInfo;
/**
 * 程序监控详情dao
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-30 下午07:26:17
 */
public class PrcMonDAO extends SuperDAO implements IPrcMonDAO
{

	/**
	 * 获取程序监控详情
	 * @description    
	 * @param pageInfo 分页信息
	 * @param conditionMap 条件map
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午08:38:39
	 */
	public List<DynaBean> getPrcMon(PageInfo pageInfo,LinkedHashMap<String, String> conditionMap)
	{
		List<DynaBean> beanList = null;
		String sql = "select dp.*,ISNULL(err.EVTTYPE,0) AS EVTTYPE,sp.proceName,sp.version,sp.monStatus,sh.hostName,sh.hostcode,sh.adapter1 " +
				"from m_mon_dproce dp LEFT JOIN( select proceId,min(evttype) AS EVTTYPE from m_mon_err err where err.monStatus=1 group by proceId) AS err ON(dp.PROCEID=err.PROCEID) " +
				"INNER JOIN M_MON_SPROCE sp ON(dp.PROCEID=sp.PROCEID) LEFT JOIN M_MON_SHOST sh ON(sp.HOSTID=sh.HOSTID)";
		String hostcode = conditionMap.get("hostcode");
		String hostname = conditionMap.get("hostname");
		String adapter1 = conditionMap.get("adapter1");
		String procename = conditionMap.get("procename");
		String evttype = conditionMap.get("evttype");
		StringBuffer conSql = new StringBuffer();
		if(hostcode!=null && !"".equals(hostcode))
		{
			conSql.append(" and sh.hostcode like '%").append(hostcode).append("%'");
		}
		if(hostname!=null && !"".equals(hostname))
		{
			conSql.append(" and sh.hostname like '%").append(hostname).append("%'");
		}
		if(adapter1!=null && !"".equals(adapter1))
		{
			conSql.append(" and sh.adapter1 like '%").append(adapter1).append("%'");
		}
		if(procename!=null && !"".equals(procename))
		{
			conSql.append(" and sp.procename like '").append(procename).append("%'");
		}
		if(evttype!=null && !"".equals(evttype))
		{
			if("0".equals(evttype))
			{
				conSql.append(" and EVTTYPE is null");
			}
			else
			{
				conSql.append(" and EVTTYPE =").append(evttype);
			}
		}
		//处理并返回SQL条件
		String conditonSql = new MonitorDAO().getConditionSql(conSql.toString());
		if(conditonSql == null)
		{
			return null;
		}
		sql = sql + conditonSql;
		if(pageInfo==null)
		{
			beanList = this.getListDynaBeanBySql(sql);
		}
		else
		{
			String countSql = "select count(*) totalcount from m_mon_dproce dp LEFT JOIN( select proceId,min(evttype) AS EVTTYPE from m_mon_err err where err.monStatus=1 group by proceId) AS err ON(dp.PROCEID=err.PROCEID) " +
				"INNER JOIN M_MON_SPROCE sp ON(dp.PROCEID=sp.PROCEID) LEFT JOIN M_MON_SHOST sh ON(sp.HOSTID=sh.HOSTID)"+ conditonSql;
			beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		return beanList;
	}
	
	/**
	 * 获取用户名和机构名称
	 * @return
	 */
	public List<DynaBean> getUserAndDep()
	{
		List<DynaBean> beanList = null;
		String sql = "select lfsysuser.user_name,lfsysuser.name,lfsysuser.user_id,dep.dep_id,dep.dep_name from lf_sysuser lfsysuser left join lf_dep dep on lfsysuser.dep_id=dep.dep_id ";
		beanList = this.getListDynaBeanBySql(sql);
		return beanList;
	}
}
