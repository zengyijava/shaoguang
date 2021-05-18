package com.montnets.emp.mondetails.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.mondetails.dao.i.IHisMonDAO;
import com.montnets.emp.monitor.dao.MonitorDAO;
import com.montnets.emp.util.PageInfo;

/**
 * 告警历史dao
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-30 下午07:26:17
 */
public class HisMonDAO extends SuperDAO implements IHisMonDAO
{

	/**
	 * 获取历史告警监控详情
	 * @description    
	 * @param pageInfo 分页信息
	 * @param conditionMap 条件map
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午08:38:39
	 */
	public List<DynaBean> getErrMon(PageInfo pageInfo,LinkedHashMap<String, String> conditionMap)
	{
	try {
		List<DynaBean> beanList = null;
		String sql = "select err.*,host.HostName,prc.ProceName,info.GateName,spinfo.AccountName, hnet.hostname as hostnetname, db.procename as dbprocename from LF_MON_ERRHIS err left join LF_MON_SHOST host on err.HostId=host.HostID" +
				" left join LF_MON_SPROCE prc on err.ProceID=prc.ProceID left join LF_MON_SGTACINFO info on err.GateAccount=info.GateAccount" +
				" left join LF_MON_SSPACINFO spinfo on spinfo.SpAccountId=err.SpAccountId left join (select distinct procenode,hostname from LF_MON_HOSTNET) hnet on err.procenode=hnet.procenode" + 
				" left join LF_MON_DBSTATE db on err.procenode=db.procenode";;
		String orderSql = " order by err.evttime desc,err.id desc";
		//事件内容
		String msg = conditionMap.get("msg");
		//告警级别
		String evttype = conditionMap.get("evttype");
		IGenericDAO genDao = new DataAccessDriver().getGenericDAO();
		String recvtime =conditionMap.get("recvtime");
		String sendtime = conditionMap.get("sendtime");
		//监控类型
		String apptype = conditionMap.get("apptype");
		//处理状态
		String dealflag = conditionMap.get("dealflag");
		String dealpeople = conditionMap.get("dealpeople");
		String monname = conditionMap.get("monname");
		StringBuffer conSql = new StringBuffer();
		if(msg!=null && !"".equals(msg))
		{
			conSql.append(" and err.msg like '%").append(msg).append("%'");
		}
		if(evttype!=null && !"".equals(evttype))
		{
			conSql.append(" and err.evttype=").append(evttype);
		}
		if(dealflag!=null && !"".equals(dealflag))
		{
			conSql.append(" and err.dealflag=").append(dealflag);
		}
		if(apptype!=null && !"".equals(apptype))
		{
			if("5000".equals(apptype)){
				conSql.append(" and err.apptype in (5000,5700)");
			}else{
				conSql.append(" and err.apptype=").append(apptype);
			}
		}
		if(sendtime!=null && !"".equals(sendtime))
		{
			sendtime = genDao.getTimeCondition(sendtime);
			conSql.append(" and err.evttime >").append(sendtime);
		}
		if(dealpeople!=null && !"".equals(dealpeople))
		{
			conSql.append(" and err.dealpeople ='").append(dealpeople).append("'");
		}
		if(recvtime!=null && !"".equals(recvtime))
		{
			recvtime = genDao.getTimeCondition(recvtime);
			conSql.append(" and err.evttime <").append(recvtime);
		}
		if(monname!=null && !"".equals(monname))
		{
			conSql.append(" and ((err.apptype=3000 and host.HostName like '%").append(monname).append("%')")
			.append(" or prc.ProceName like '%").append(monname).append("%'")
			.append(" or info.GateName like '%").append(monname).append("%'")
			.append(" or spinfo.AccountName like '%").append(monname).append("%'")
						.append(" or db.procename like '%").append(monname).append("%'")
			.append(" or hnet.hostname like '%").append(monname).append("%')");
		}
		//处理并返回SQL条件
		String conditonSql = new MonitorDAO().getConditionSql(conSql.toString());
		if(conditonSql == null)
		{
			return null;
		}
		sql = sql + conditonSql + orderSql;
		if(pageInfo==null)
		{
			beanList = this.getListDynaBeanBySql(sql);
		}
		else
		{
			String countSql = "select count(*) totalcount from LF_MON_ERRHIS err left join LF_MON_SHOST host on err.HostId=host.HostID" +
				" left join LF_MON_SPROCE prc on err.ProceID=prc.ProceID left join LF_MON_SGTACINFO info on err.GateAccount=info.GateAccount left join LF_MON_SSPACINFO spinfo on spinfo.SpAccountId=err.SpAccountId left join (select distinct procenode,hostname from LF_MON_HOSTNET)" +
				" hnet on err.procenode=hnet.procenode left join LF_MON_DBSTATE db on err.procenode=db.procenode "+ conditonSql;
//			beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
			beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
			}
			return beanList;
		} catch (Exception e) {
			EmpExecutionContext.error("获取告警历史监控详情异常！");
			return null;
		}
	}
}
