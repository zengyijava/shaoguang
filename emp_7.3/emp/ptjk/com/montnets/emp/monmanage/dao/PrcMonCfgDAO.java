package com.montnets.emp.monmanage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.monmanage.dao.i.IPrcMonCfgDAO;
import com.montnets.emp.table.monitor.TableLfMonErr;
import com.montnets.emp.table.monitor.TableLfMonSproce;
import com.montnets.emp.util.PageInfo;
/**
 * 主机监控管理dao
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-27 上午08:36:21
 */
public class PrcMonCfgDAO extends SuperDAO implements IPrcMonCfgDAO
{

	/**
	 * 获取程序信息
	 * @description    
	 * @param pageInfo 分页信息
	 * @param conditionMap 条件map
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午08:38:39
	 */
	public List<DynaBean> getSprc(PageInfo pageInfo,LinkedHashMap<String, String> conditionMap)
	{
		List<DynaBean> beanList = null;
		String sql = "select sprc.*,shost.hostname from lf_mon_sproce sprc left join lf_mon_shost shost on sprc.hostid=shost.hostid";
		String proceid = conditionMap.get("proceid");
		String hostname = conditionMap.get("hostname");
		String procename = conditionMap.get("procename");
		String hostid = conditionMap.get("hostid");
		StringBuffer conSql = new StringBuffer();
		// 0 未删除
		conSql.append(" where sprc.proceusestatus = 0");
		//程序id
		if(proceid!=null && !"".equals(proceid))
		{
			conSql.append(" and sprc.proceid =").append(proceid);
		}
		//程序名
		if(procename!=null && !"".equals(procename))
		{
			conSql.append(" and sprc.procename like '%").append(procename).append("%'");
		}
		//主机id
		if(hostid!=null && !"".equals(hostid))
		{
			conSql.append(" and sprc.hostid =").append(hostid);
		}
		//主机名称
		if(hostname!=null && !"".equals(hostname))
		{
			conSql.append(" and shost.hostname like '%").append(hostname).append("%'");
		}
		sql = sql + conSql.toString();;
		if(pageInfo==null)
		{
			beanList = this.getListDynaBeanBySql(sql);
		}
		else
		{
			String countSql = "select count(*) totalcount from lf_mon_sproce sprc left join lf_mon_shost shost on sprc.hostid=shost.hostid"  + conSql.toString();
//			beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
			beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		return beanList;
	}
	
	
	/**
	 * 获取EMP相关信息
	 * @description    
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午08:38:39
	 */
	public List<DynaBean> getgw()
	{
		List<DynaBean> beanList = null;
		String sql = "SELECT * from GW_CLUSTATUS where gwType=4000 order by gwNo";
		beanList = this.getListDynaBeanBySql(sql);
		return beanList;
	}
	
	/**
	 * 获取主机名称及id信息
	 * @description    
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午08:38:39
	 */
	public List<DynaBean> getHost()
	{
		List<DynaBean> beanList = null;
		String sql = "select shost.hostname,shost.hostid,shost.hostcode from m_mon_shost";
		beanList = this.getListDynaBeanBySql(sql);
		return beanList;
	}
	
	/**
	 * 获取SPGate信息
	 * @description    
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午08:38:39
	 */
	public List<DynaBean> getSPGate()
	{
		List<DynaBean> beanList = null;
        String UID = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE?"UID":"\"UID\"";
		String sql = "select bind.gwno  from userdata userdata " +
				"left join gw_cluspbind bind on userdata."+UID+" = bind.ptaccuid " +
						"left join gw_clustatus status on bind.gwno = status.gwno " +
						//"where userdata.usertype=1 and userdata.accounttype=1 order by bind.gwno";//以前的逻辑
						"where userdata.usertype=1 and userdata.accounttype=1 and bind.gwno is not null order by bind.gwno";
		beanList = this.getListDynaBeanBySql(sql);
		return beanList;
	}
	
	
	/**
	 * 获取mq的配置信息
	 * @description    
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-1-17 下午08:21:55
	 */
	public List<DynaBean> getMqMsg()
	{
		List<DynaBean> beanList = null;
		String sql = "select * from A_GWPARAMCONF where gwtype=4000 and( paramitem='MONITORMQURL' or paramitem='MONITORQUENAME') ";
		beanList = this.getListDynaBeanBySql(sql);
		return beanList;
	}
	
	public void deleteErr(Connection conn,String hostid) throws SQLException{
		String sql = "delete from "+TableLfMonErr.TABLE_NAME +" where "+TableLfMonErr.PROCEID+" in ( select "
		+TableLfMonSproce.PROCEID+" from "+TableLfMonSproce.TABLE_NAME+" where "+TableLfMonSproce.HOSTID+"="+hostid+")";
		PreparedStatement ps = null;
		try{
			 ps =conn.prepareStatement(sql);
			 ps.executeUpdate();
		}finally{
			try
			{
				close(null, ps, null);
			} catch (Exception e)
			{
				EmpExecutionContext.debug("关闭数据库资源出错！");
			}
		}
	}
}
