package com.montnets.emp.gateway.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.servmodule.txgl.dao.SuperTxglDAO;
import com.montnets.emp.servmodule.txgl.entity.GwCluStatus;
import com.montnets.emp.servmodule.txgl.entity.Userdata;
import com.montnets.emp.util.PageInfo;


/**
 * 通道账户管理
 * @description 
 * @project p_txgl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-10-21 上午11:17:44
 */
public class GatewayDAO extends SuperDAO{
	
	/**
	 * 查询通道账户
	 * @description    
	 * @param conditionMap
	 * @param pageinfo
	 * @return
	 * @throws Exception       			 
	 * @author zhangmin
	 * @datetime 2014-4-24 下午02:04:35
	 */
	public List<Userdata> getUserList(LinkedHashMap<String, String> conditionMap,PageInfo pageinfo) throws Exception
	{
		String sql = "select * from userdata where usertype=1";
		String UID = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE?"UID":"\"UID\"";
		String orderbySql=" order by userId asc";
		//通道账号
		String gt_userid=conditionMap.get("userId");
		//账户类型
		String accouttype = conditionMap.get("accouttype");
		//账户状态
		String status = conditionMap.get("status");
		//通道
		String gt_spgate = conditionMap.get("gt_spgate");
		//通道id
		String gate_id = conditionMap.get("gateid");
		//通道账户名称
		String staffName = conditionMap.get("staffName");
		//运营商账户id
		String spaccid = conditionMap.get("spaccid");
		if(gt_userid!=null&&!"".equals(gt_userid))
		{
			sql=sql+" and userId='"+gt_userid+"'";
		}
		if(accouttype!=null&&!"".equals(accouttype))
		{
			sql=sql+" and accounttype="+accouttype;
		}
		if(status!=null&&!"".equals(status))
		{
			sql=sql+" and status="+status;
		}
		if(staffName!=null&&!"".equals(staffName))
		{
			sql=sql+" and staffName like '%"+staffName+"%'";
		}
		if(spaccid!=null&&!"".equals(spaccid))
		{
			sql=sql+" and "+UID +" in( select ptaccuid from A_GWACCOUNT where spaccid='"+spaccid+"')";
		}
		if(gate_id!=null&&!"".equals(gate_id))
		{
			sql=sql+" and "+UID +" in( select ptaccuid from A_GWSPBIND where gateid="+gate_id+")";
		}
		if (pageinfo == null) {
			return findEntityListBySQL(Userdata.class, sql,
					StaticValue.EMP_POOLNAME);
		} else {
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql)
					.append(")derivedtbl_1").toString();
			sql=sql+orderbySql;
//			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(Userdata.class,
//							sql, countSql, pageinfo, StaticValue.EMP_POOLNAME);
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(Userdata.class,
					sql, countSql, pageinfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	public List<DynaBean> getUseridByGwAccount(String ptaccuid,String accountType) throws Exception{
		String sql="select distinct gpu.userid from  gt_port_used gpu inner join xt_gate_queue xt on gpu.spgate=xt.spgate and gpu.spisuncm=xt.spisuncm " +
				" inner join a_gwspbind agwb on agwb.gateid=xt.id inner join userdata ud on ud.userid=gpu.userid where agwb.ptaccuid="+ptaccuid+
				" and ud.accounttype="+accountType;
		return new SuperTxglDAO().getListDynaBeanBySql(sql);
	}

    /**
     * 后端账号查询列表
     * @param conditionMap
     * @param pageinfo
     * @return
     * @throws Exception
     */
    public List<DynaBean> getAccountList(LinkedHashMap<String, String> conditionMap,PageInfo pageinfo) throws Exception
    {
        String UID = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE?"UID":"\"UID\"";
        String sql = "select bind.gwno gw_no,bind.gweight,status.runstatus,userdata.* from userdata userdata " +
                "left join gw_cluspbind bind on userdata."+UID+" = bind.ptaccuid " +
                "left join gw_clustatus status on bind.gwno = status.gwno where userdata.usertype=1";
        String orderbySql=" order by userdata.userId asc,bind.gwno asc";
        //通道账号
        String gt_userid=conditionMap.get("userId");
        //账户类型
        String accouttype = conditionMap.get("accouttype");
        //账户状态
        String status = conditionMap.get("status");
        //通道
        String gt_spgate = conditionMap.get("gt_spgate");
        //通道id
        String gate_id = conditionMap.get("gateid");
        //通道账户名称
        String staffName = conditionMap.get("staffName");
        //运营商账户id
        String spaccid = conditionMap.get("spaccid");
        //网关编号
        String gwno = conditionMap.get("gwno");
        //网关状态
        String gwstate = conditionMap.get("gwstate");
        //主备网关
        String gwbak = conditionMap.get("gwbak");

        //通道账户类型
        String accability = conditionMap.get("accability");

        if(accability!=null&&!"".equals(accability))
        {
            sql=sql+" and userdata.accability & "+accability+"";
        }

        if(gt_userid!=null&&!"".equals(gt_userid))
        {
            sql=sql+" and userdata.userId='"+gt_userid+"'";
        }
        if(accouttype!=null&&!"".equals(accouttype))
        {
            sql=sql+" and userdata.accounttype="+accouttype;
        }
        if(status!=null&&!"".equals(status))
        {
            sql=sql+" and userdata.status="+status;
        }
        if(staffName!=null&&!"".equals(staffName))
        {
            sql=sql+" and userdata.staffName like '%"+staffName+"%'";
        }
        if(spaccid!=null&&!"".equals(spaccid))
        {
            sql=sql+" and userdata."+UID +" in( select ptaccuid from A_GWACCOUNT where spaccid='"+spaccid+"')";
        }
        if(gate_id!=null&&!"".equals(gate_id))
        {
            sql=sql+" and userdata."+UID +" in( select ptaccuid from A_GWSPBIND where gateid="+gate_id+")";
        }
        if(gwno!=null&&!"".equals(gwno))
        {
            sql += " and bind.gwno = "+gwno;
        }
        if(gwstate != null && !"".equals(gwstate))
        {
            sql += " and status.runstatus = "+gwstate;
        }
        if(gwbak != null && !"".equals(gwbak))
        {
            if("1".equals(gwbak))
            {
                sql += " and bind.gweight = 0";
            }
            else if("0".equals(gwbak))
            {
                sql += " and bind.gweight <> 0";
            }
        }
        String countSql = new StringBuffer(
                "select count(*) totalcount from (").append(sql)
                .append(")derivedtbl_1").toString();
        sql=sql+orderbySql;

        return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageinfo, StaticValue.EMP_POOLNAME,null);
    }

    /**
     * 添加备用网关
     * @param uid 通道账号id
     * @return
     */
    public boolean addBak(String uid)
    {
        int count = 0;
        IGenericDAO genericDAO = new DataAccessDriver().getGenericDAO();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "SELECT GWNO FROM (select max(GWNO)+1 gwno from GW_CLUSPBIND" +
                " UNION ALL SELECT GWNO FROM GW_CLUSPBIND WHERE PTACCUID = "+uid+" AND GWEIGHT = 0) tmp ORDER BY gwno DESC ";

        Connection connection = null;
        try {
            connection = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
            List<DynaBean> lists = genericDAO.findDynaBeanBySql(sql);
            if(lists != null && lists.size() >= 2)
            {
                //备用网关编号
                String gwno = lists.get(0).get("gwno").toString();
                //主用网关编号
                String priGwNo = lists.get(1).get("gwno").toString();
                sql = "INSERT INTO GW_CLUSPBIND (PTACCUID,GWNO,GWEIGHT,UPDTIME) VALUES("+uid+","+
                        gwno+",99,"+genericDAO.getTimeCondition(sdf.format(new Date(System.currentTimeMillis())))+")";
                executeBySQLReturnCount(connection,sql);

                //网关状态表 插入数据
                sql = "INSERT INTO GW_CLUSTATUS (GWTYPE, GWNO, PRIGWNO, GWEIGHT, RUNWEIGHT) VALUES (3000,"+gwno+","+priGwNo+",99,99)";
                count = executeBySQLReturnCount(connection,sql);
            }
            connection.commit();
        } catch (Exception e) {
            try {
            	if(connection!=null){
                    connection.rollback();
            	}
            } catch (SQLException e1) {
                EmpExecutionContext.error(e1,"添加备用网关回滚事务异常！");
            }
            EmpExecutionContext.error(e,"设置备份网关异常！");
        }
        finally
        {
         if(connection != null)
         {
             try {
                 connection.close();
             } catch (SQLException e) {
                 EmpExecutionContext.error(e,"添加备用网关连接关闭异常！");
             }
         }
        }
        return count > 0;
    }


    /**
     * 根据网关运行状态表 初始化网关决策表
     * @param connection
     * @return
     */
    public void initGwCludecision(Connection connection,GwCluStatus gwCluStatus) throws Exception {
    	PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO GW_CLUDECISION(GWTYPE,GWNO) VALUES (?,?)";
            ps = connection.prepareStatement(sql);
            ps.setInt(1,gwCluStatus.getGwType());
            ps.setInt(2,gwCluStatus.getGwNo());
            ps.executeUpdate();
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"初始化网关决策表异常！");
            throw e;
        }finally{
        	if(ps != null){
        		ps.close();
        	}
        }
    }


    /**
     * 获取集群网关运行状态信息
     * @return
     */
    public List<DynaBean> getGWCluStatus()
    {
        String sql = "select * from gw_clustatus";
        return getListDynaBeanBySql(sql);
    }

}
