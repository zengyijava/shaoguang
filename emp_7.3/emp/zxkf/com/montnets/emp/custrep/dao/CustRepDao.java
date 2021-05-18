package com.montnets.emp.custrep.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.util.PageInfo;

public class CustRepDao extends SuperDAO
{

    /**
     * 获取服务评价信息
     * @description    
     * @param conditionMap
     * @param pageInfo
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-2-18 上午11:40:27
     */
    public List<DynaBean> getEvaluates(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
    {
        List<DynaBean> beanList = null;
        try
        {
            // 查询字段
            //String sql = "select case  when ser.score is null then 0 else ser.score end as score," +
            String sql = "select ser.score , acc.name as accname,uin.nick_name,sue.name,ser.create_time" ;
            String fromSql =" from lf_onl_server ser" + 
                " left join lf_wei_account acc on acc.a_id = ser.a_id" + 
                " left join lf_wei_userinfo uin on uin.open_id = ser.from_user" + 
                " inner join lf_sysuser sue on sue.user_id = ser.custome_id where ser.score is not null and ser.score > 0 ";
            String conditionSql = "";
            String AId = conditionMap.get("AId");
            if(AId != null)
            {
                conditionSql += " and ser.a_id = "+ AId;
            }
            String customeId = conditionMap.get("customeId");
            if(customeId != null)
            {
                conditionSql += " and ser.custome_id = "+ customeId;
            }
            List<String> timeList = new ArrayList<String>();
            String startdate = conditionMap.get("startdate");
            if(startdate != null)
            {
                conditionSql += " and ser.create_time >= ? ";
                timeList.add(startdate);
            }
            String enddate = conditionMap.get("enddate");
            if(startdate != null)
            {
                conditionSql += " and ser.create_time <= ? ";
                timeList.add(enddate);
            }
            /*if(conditionSql.length() > 0)
            {
                conditionSql = " where " + conditionSql.substring(conditionSql.indexOf("and") + 4);
            }*/
            // 总数
            String countSql = " SELECT count(*) totalcount " + fromSql + conditionSql;
            sql += fromSql + conditionSql + " order by ser.create_time desc";
            beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
        }
        catch (Exception e)
        {
            beanList = null;
            EmpExecutionContext.error(e, "CustRepDao.getEvaluates is error");
        }
        return beanList;
    }
    
    /**
     * 获取服务时长信息
     * @description    
     * @param conditionMap
     * @param pageInfo
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-2-18 上午11:40:56
     */
    public List<DynaBean> getServerTime(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
    {
        List<DynaBean> beanList = null;
        try
        {
            // 查询字段
            //String sql = "select case  when ser.score is null then 0 else ser.score end as score," +
            String sql = "select sue.name,ser.create_time,ser.duration,ser.end_time,acc.name as accname,uin.nick_name " ;
            String fromSql =" from lf_onl_server ser" + 
                    " left join lf_wei_account acc on acc.a_id = ser.a_id" + 
                    " left join lf_wei_userinfo uin on uin.open_id = ser.from_user" + 
                    " inner join lf_sysuser sue on sue.user_id = ser.custome_id";
            String conditionSql = " where ser.duration  is not null and ser.end_time is not null";
            String AId = conditionMap.get("AId");
            if(AId != null)
            {
                conditionSql += " and ser.a_id = "+ AId;
            }
            String customeId = conditionMap.get("customeId");
            if(customeId != null)
            {
                conditionSql += " and ser.custome_id = "+ customeId;
            }
            List<String> timeList = new ArrayList<String>();
            String startdate = conditionMap.get("startdate");
            if(startdate != null)
            {
                conditionSql += " and ser.create_time >= ? ";
                timeList.add(startdate);
            }
            String enddate = conditionMap.get("enddate");
            if(startdate != null)
            {
                conditionSql += " and ser.create_time <= ? ";
                timeList.add(enddate);
            }
            /*if(conditionSql.length() > 0)
            {
                conditionSql = " where " + conditionSql.substring(conditionSql.indexOf("and") + 4);
            }*/
            // 总数
            String countSql = " SELECT count(*) totalcount " + fromSql + conditionSql;
            sql += fromSql + conditionSql + " order by ser.create_time desc";
            beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
        }
        catch (Exception e)
        {
            beanList = null;
            EmpExecutionContext.error(e, "CustRepDao.getEvaluates is error");
        }
        return beanList;
    }
    
    /**
     * 获取存在聊天记录的聊天对象
     * @description    
     * @param fromUser
     * @return
     * @throws Exception       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-2-18 上午11:46:00
     */
    public List<DynaBean> getTalkUser(String fromUser) throws Exception
    {
        String sql = "select user_id,name from lf_sysuser where user_id in" +
        		"(select DISTINCT to_user from lf_onl_msg_his where from_user ='" + fromUser +
        		"' and push_type = 3 and to_user is not null);";
        return this.getListDynaBeanBySql(sql);
    }
}
