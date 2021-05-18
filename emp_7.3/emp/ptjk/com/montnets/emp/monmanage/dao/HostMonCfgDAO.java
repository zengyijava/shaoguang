package com.montnets.emp.monmanage.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.monmanage.dao.i.IHostMonCfgDAO;
import com.montnets.emp.util.PageInfo;
/**
 * 程序监控管理dao
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-27 上午08:36:21
 */
public class HostMonCfgDAO extends SuperDAO implements IHostMonCfgDAO
{

	/**
	 * 获取主机信息
	 * @description    
	 * @param pageInfo 分页信息
	 * @param conditionMap 条件map
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午08:38:39
	 */
	public List<DynaBean> getHost(PageInfo pageInfo,LinkedHashMap<String, String> conditionMap)
	{
		List<DynaBean> beanList = null;
		String sql = "select shost.*from lf_mon_shost shost";
		String hostname = conditionMap.get("hostname");
		String adapter1 = conditionMap.get("adapter1");
		String hostid = conditionMap.get("hostid");
		StringBuffer conSql = new StringBuffer();
		conSql.append(" where shost.hostusestatus = 0");
		if(hostname!=null && !"".equals(hostname))
		{
			conSql.append(" and shost.hostname like '%").append(hostname).append("%'");
		}
		if(adapter1!=null && !"".equals(adapter1))
		{
			conSql.append(" and shost.adapter1 like '%").append(adapter1).append("%'");
		}
		if(hostid!=null && !"".equals(hostid))
		{
			conSql.append(" and shost.hostid =").append(hostid);
		}
		sql = sql + conSql.toString();
		if(pageInfo==null)
		{
			beanList = this.getListDynaBeanBySql(sql);
		}
		else
		{
			String countSql = "select count(*) totalcount from lf_mon_shost shost"  + conSql.toString();
//			beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
			beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		return beanList;
	}
	
	/**
	 * 获取主机动态信息
	 * @description    
	 * @param conditionMap 条件map
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午08:38:39
	 */
	public List<DynaBean> getDhost(LinkedHashMap<String, String> conditionMap)
	{
		List<DynaBean> beanList = null;
		String sql = "select shost.hostid,shost.hostname,shost.hostcode,dhost.* from m_mon_dhost dhost inner join m_mon_shost shost on shost.hostid=dhost.hostid";
		String hostid = conditionMap.get("hostid");
		StringBuffer conSql = new StringBuffer();
		if(hostid!=null && !"".equals(hostid))
		{
			conSql.append(" where shost.hostid =").append(hostid).append("");
		}
		sql = sql + conSql.toString();;
		beanList = this.getListDynaBeanBySql(sql);
		return beanList;
	}
}
