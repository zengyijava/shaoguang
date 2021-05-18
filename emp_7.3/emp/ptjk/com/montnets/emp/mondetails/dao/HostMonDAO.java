package com.montnets.emp.mondetails.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.mondetails.dao.i.IHostMonDAO;
import com.montnets.emp.util.PageInfo;
/**
 * 主机监控详情dao
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-30 下午07:26:17
 */
public class HostMonDAO extends SuperDAO implements IHostMonDAO
{

	/**
	 * 获取主机监控详情
	 * @description    
	 * @param pageInfo 分页信息
	 * @param conditionMap 条件map
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午08:38:39
	 */
	public List<DynaBean> getHostMon(PageInfo pageInfo,LinkedHashMap<String, String> conditionMap)
	{
		List<DynaBean> beanList = null;
		String sql = "select dh.*,sh.hostName,sh.adapter1,sh.hostCode,sh.monStatus,ISNULL(err.EVTTYPE,0) AS EVTTYPE " +
				"from M_MON_DHOST dh  LEFT JOIN (select Hostid,min(evttype) AS EVTTYPE from  m_mon_err err group by Hostid) AS err ON(dh.HOSTID=err.HOSTID) INNER JOIN M_MON_SHOST sh ON (dh.HOSTID=sh.HOSTID) WHERE sh.monstatus=1";
		String hostcode = conditionMap.get("hostcode");
		String hostname = conditionMap.get("hostname");
		String adapter1 = conditionMap.get("adapter1");
		String hostid = conditionMap.get("hostid");
		String hoststatus = conditionMap.get("hoststatus");
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
		if(hostid!=null && !"".equals(hostid))
		{
			conSql.append(" and dh.hostid =").append(hostid);
		}
		if(hoststatus!=null && !"".equals(hoststatus))
		{
			conSql.append(" and dh.hoststatus=").append(hoststatus);
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
		sql = sql + conSql.toString();
		if(pageInfo==null)
		{
			beanList = this.getListDynaBeanBySql(sql);
		}
		else
		{
			String countSql = "select count(*) totalcount from M_MON_DHOST dh  LEFT JOIN (select Hostid,min(evttype) AS EVTTYPE from  m_mon_err err  group by Hostid) AS err ON(dh.HOSTID=err.HOSTID) INNER JOIN M_MON_SHOST sh ON (dh.HOSTID=sh.HOSTID) WHERE sh.monstatus=1 " + conSql.toString();
			beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		return beanList;
	}
}
