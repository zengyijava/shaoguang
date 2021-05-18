package com.montnets.emp.gateway.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.servmodule.txgl.dao.SuperTxglDAO;
import com.montnets.emp.servmodule.txgl.entity.GwCluStatus;
import com.montnets.emp.util.PageInfo;

/**
 * 通道账户管理
 * 
 * @description
 * @project p_txgl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-10-21 上午11:17:44
 */
public class MwGatewayDAO extends SuperDAO
{

	public List<DynaBean> getUseridByGwAccount(String ptaccuid, String accountType) throws Exception
	{
		String sql = "select distinct gpu.userid from  gt_port_used gpu inner join xt_gate_queue xt on gpu.spgate=xt.spgate and gpu.spisuncm=xt.spisuncm " + " inner join a_gwspbind agwb on agwb.gateid=xt.id inner join userdata ud on ud.userid=gpu.userid where agwb.ptaccuid=" + ptaccuid + " and ud.accounttype=" + accountType;
		return new SuperTxglDAO().getListDynaBeanBySql(sql);
	}

	/**
	 * 后端账号查询列表
	 * 
	 * @param conditionMap
	 * @param pageinfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getAccountList(LinkedHashMap<String, String> conditionMap, PageInfo pageinfo) throws Exception
	{
		String UID = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE ? "UID" : "\"UID\"";
		String sql = "select bind.GWNO gw_no,userdata." + UID + ",userdata.USERID,userdata.STAFFNAME,userdata.ACCOUNTTYPE,GATECONN.KEEPCONN,GATECONN.LINKCNT," 
			+ "GATECONN.CREATETM,GATECONN.UPDATETM FROM USERDATA userdata " + "left join gw_cluspbind bind on userdata." + UID + " = bind.ptaccuid" 
			+ " LEFT JOIN (SELECT PTACCID,MAX(KEEPCONN) KEEPCONN,MAX(LINKCNT) LINKCNT,MAX(CREATETM) CREATETM,MAX(UPDATETM) UPDATETM " 
			+ "FROM GW_GATECONNINFO GROUP BY PTACCID) GATECONN ON GATECONN.PTACCID=userdata.USERID  where userdata.usertype=1";
		String orderbySql = " order by userdata.userId asc,bind.gwno asc";
		// 通道账号
		String gt_userid = conditionMap.get("userId");
		// 通道账户名称
		String staffName = conditionMap.get("staffName");
		// 运营商账户id
		String spaccid = conditionMap.get("spaccid");
		// 合作商
		String sptype = conditionMap.get("sptype");
		// 连接方式
		String keepconn = conditionMap.get("keepconn");
		// 账户类型
		String accouttype = conditionMap.get("accouttype");
		if(gt_userid != null && !"".equals(gt_userid))
		{
			sql = sql + " and userdata.userId='" + gt_userid + "'";
		}
		if(staffName != null && !"".equals(staffName))
		{
			sql = sql + " and userdata.staffName like '%" + staffName + "%'";
		}
		if(spaccid != null && !"".equals(spaccid))
		{
			sql = sql + " and userdata." + UID + " in( select ptaccuid from A_GWACCOUNT where spaccid='" + spaccid + "')";
		}
		if(sptype != null && !"".equals(sptype))
		{
			sql = sql + " and userdata." + UID + " in( select ptaccuid from A_GWACCOUNT WHERE SPTYPE=" + sptype + ")";
		}
		if(keepconn != null && !"".equals(keepconn))
		{
			sql = sql + " and GATECONN.KEEPCONN=" + keepconn;
		}
		if(accouttype != null && !"".equals(accouttype))
		{
			sql = sql + " and userdata.accounttype=" + accouttype;
		}
		String countSql = new StringBuffer("select count(*) totalcount from (").append(sql).append(")derivedtbl_1").toString();
		sql = sql + orderbySql;

		return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageinfo, StaticValue.EMP_POOLNAME, null);
	}

	/**
	 * 根据网关运行状态表 初始化网关决策表
	 * 
	 * @param connection
	 * @return
	 */
	public void initGwCludecision(Connection connection, GwCluStatus gwCluStatus) throws Exception
	{
		PreparedStatement ps = null;
		try
		{
			String sql = "INSERT INTO GW_CLUDECISION(GWTYPE,GWNO) VALUES (?,?)";
			ps = connection.prepareStatement(sql);
			ps.setInt(1, gwCluStatus.getGwType());
			ps.setInt(2, gwCluStatus.getGwNo());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "初始化网关决策表异常！");
			throw e;
        }finally{
        	if(ps != null){
        		ps.close();
        	}
        }
	}

	/**
	 * 获取集群网关运行状态信息
	 * 
	 * @return
	 */
	public List<DynaBean> getGWCluStatus()
	{
		String sql = "select * from gw_clustatus";
		return getListDynaBeanBySql(sql);
	}

}
