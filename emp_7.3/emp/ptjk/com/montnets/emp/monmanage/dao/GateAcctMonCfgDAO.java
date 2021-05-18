package com.montnets.emp.monmanage.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.monitor.dao.MonitorDAO;
import com.montnets.emp.monmanage.dao.i.IGateAcctMonCfgDAO;
import com.montnets.emp.util.PageInfo;
/**
 * sp账号监控管理dao
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-03 上午08:36:21
 */
public class GateAcctMonCfgDAO extends SuperDAO implements IGateAcctMonCfgDAO
{

	/**
	 * 获取sp账号监控管理
	 * @description    
	 * @param pageInfo 分页信息
	 * @param conditionMap 条件map
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-12-03 上午08:38:39
	 */
	public List<DynaBean> getGateAcctMonCfg(PageInfo pageInfo,LinkedHashMap<String, String> conditionMap)
	{
		List<DynaBean> beanList = null;
		String sql = "select distinct * from LF_MON_SGTACINFO";
		StringBuffer conSql = new StringBuffer("");
		if(conditionMap!=null){
			String gateaccount = conditionMap.get("gateaccount");
			String gateaccountType = conditionMap.get("gateaccountType");
			String gatename = conditionMap.get("gatename");
			String monstatus = conditionMap.get("monstatus");
			conSql = new StringBuffer();
			if(gateaccount!=null && !"".equals(gateaccount))
			{	if(!"0".equals(gateaccountType)){
					conSql.append(" and gateaccount like '%").append(gateaccount).append("%'");
				}else{
					conSql.append(" and gateaccount ='").append(gateaccount).append("'");	
				}
				
			}
			if(monstatus!=null && !"".equals(monstatus))
			{
				conSql.append(" and monstatus =").append(monstatus);
			}
			if(gatename!=null && !"".equals(gatename))
			{
				conSql.append(" and gatename like '%").append(gatename).append("%'");
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
			String countSql = "select count(*) totalcount from LF_MON_SGTACINFO"  + conditonSql;
//			beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
			beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		return beanList;
	}
	
}
