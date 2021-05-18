package com.montnets.emp.wxgl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.json.simple.JSONArray;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.table.online.TableLfOnlServer;
import com.montnets.emp.table.wxgl.TableLfWeiUserInfo;

public class QunfaDao extends SuperDAO
{
    // 获取国家和
    public List<String> getAreas(String corpCode, LinkedHashMap<String, String> conditionMap) throws Exception
    {
        // 拼SQL语句
        String fieldSql = "SELECT userInfo.";
        StringBuffer tableSql = new StringBuffer();
        tableSql.append(" FROM ").append(TableLfWeiUserInfo.TABLE_NAME).append(" userInfo ").append(" WHERE  ");
        // 查询条件
        StringBuffer conSql = new StringBuffer();
        conSql.append(" userInfo.").append(TableLfWeiUserInfo.CORP_CODE).append("='").append(corpCode).append("'");
        // Group条件
        String groupSql = "";
        // column name
        String columnName = "";
        if(conditionMap != null && !conditionMap.entrySet().isEmpty())
        {
            if(conditionMap.get("AId") != null && !"".equals(conditionMap.get("AId")))
            {
                conSql.append(" and userInfo." + TableLfWeiUserInfo.A_ID + " = " + conditionMap.get("AId") + " ");
            }

            if(conditionMap.get("tp") != null && "country".equals(conditionMap.get("tp")))
            {
                fieldSql = fieldSql + "COUNTRY";
                groupSql = "GROUP BY userInfo.COUNTRY";
                columnName = TableLfWeiUserInfo.COUNTRY;
            }
            else if("province".equals(conditionMap.get("tp")))
            {
                fieldSql = fieldSql + "PROVINCE";
                groupSql = "GROUP BY userInfo.PROVINCE";
                columnName = TableLfWeiUserInfo.PROVINCE;
                conSql.append(" and userInfo." + TableLfWeiUserInfo.COUNTRY + " = '" + conditionMap.get("tpvalue") + "' ");
            }
            else if("city".equals(conditionMap.get("tp")))
            {
                fieldSql = fieldSql + "CITY";
                groupSql = "GROUP BY userInfo.CITY";
                columnName = TableLfWeiUserInfo.CITY;
                if(conditionMap.get("tpvalue") != null && !"".equals(conditionMap.get("tpvalue")))
                {
                    conSql.append(" and userInfo." + TableLfWeiUserInfo.PROVINCE + " = '" + conditionMap.get("tpvalue") + "' ");
                }
            }
        }
        else
        {
            return null;
        }
        // 返回结果TableLfWeiUserInfo
        String sql = fieldSql + tableSql.toString() + conSql.toString() + groupSql;

        return findListArrayBySQL(sql, columnName);
    }

    // 通过地区获取关注用户列表
    public JSONArray getOpenIdByGroup(String corpCode, String aId ,String gId) throws Exception
    {
        // column name
        String fieldSql = "select userinfo." + TableLfWeiUserInfo.OPEN_ID;
        StringBuffer tableSql = new StringBuffer();
        tableSql.append(" FROM ").append(TableLfWeiUserInfo.TABLE_NAME).append(" userInfo ").append(" WHERE  ");
        StringBuffer conSql = new StringBuffer();
        conSql.append(" userInfo.").append(TableLfWeiUserInfo.CORP_CODE).append("='").append(corpCode).append("'");
        if(aId==null||"".equals(aId)){
            return null;
        }else{
            conSql.append(" and userInfo.").append(TableLfWeiUserInfo.A_ID).append("=").append(aId);
            if(null!=gId&&!"".equals(gId)&&!"000".equals(gId)){
                conSql.append(" and userInfo.").append(TableLfWeiUserInfo.G_ID).append("=").append(gId);
            }
        }
        
        String sql = fieldSql + tableSql.toString() + conSql.toString();
        return findJsonArrayBySQL(sql, TableLfWeiUserInfo.OPEN_ID);
    }

 // 通过地区获取关注用户列表
    public JSONArray getOpenIdByArea(String corpCode, LinkedHashMap<String, String> conditionMap) throws Exception
    {
        // column name
        String fieldSql = "select userinfo." + TableLfWeiUserInfo.OPEN_ID;
        StringBuffer tableSql = new StringBuffer();
        tableSql.append(" FROM ").append(TableLfWeiUserInfo.TABLE_NAME).append(" userInfo ").append(" WHERE  ");
        StringBuffer conSql = new StringBuffer();
        conSql.append("  userInfo.").append(TableLfWeiUserInfo.CORP_CODE).append("='").append(corpCode).append("'");
        if(conditionMap != null && !conditionMap.entrySet().isEmpty())
        {
            if(conditionMap.get("AId") != null && !"".equals(conditionMap.get("AId")))
            {
                conSql.append(" and userInfo." + TableLfWeiUserInfo.A_ID + " = " + conditionMap.get("AId") + "");
            }

            if(null != conditionMap.get("tp") && !"".equals(conditionMap.get("tp")) && null != conditionMap.get("tpvalue") && !"".equals(conditionMap.get("tpvalue")))
            {
                if("country".equals(conditionMap.get("tp")) && !"0".equals(conditionMap.get("tpvalue")))
                {
                    conSql.append(" and userInfo.").append(TableLfWeiUserInfo.COUNTRY + "='" + conditionMap.get("tpvalue") + "' ");
                }
                else if("province".equals(conditionMap.get("tp")))
                {
                    conSql.append(" and userInfo.").append(TableLfWeiUserInfo.PROVINCE + "='" + conditionMap.get("tpvalue") + "' ");
                }
                else if("city".equals(conditionMap.get("tp")))
                {
                    conSql.append(" and userInfo.").append(TableLfWeiUserInfo.CITY + "='" + conditionMap.get("tpvalue") + "' ");
                }
            }
        }

        String sql = fieldSql + tableSql.toString() + conSql.toString();
        return findJsonArrayBySQL(sql, TableLfWeiUserInfo.OPEN_ID);
    }
    
    /**
     * 查询（本日、本周、本月）群发数
     */
    public int getCount(String startTime, String endTime, String aid, String corpCode) throws Exception
    {
        StringBuffer sql = new StringBuffer();
        
        if("1".equals(String.valueOf(StaticValue.DBTYPE))){
            sql.append("SELECT COUNT(*) as totalcount from LF_WEI_SENDLOG where CREATETIME >= " + " to_date('"+startTime+"','yyyy-mm-dd hh24:mi:ss')" + " and CREATETIME <= " + " to_date('"+endTime+"','yyyy-mm-dd hh24:mi:ss')" + " and CORP_CODE = '" + corpCode + "'");
        }else{
            sql.append("SELECT COUNT(*) as totalcount from lF_WEI_SENDLOG where CREATETIME >= '" + startTime + "' and CREATETIME <= '" + endTime + "' and CORP_CODE = '" + corpCode + "'"); 
        }
        
        if(null != aid && !"".equals(aid))
        {
            sql.append(" and A_ID = " + aid);
        }
        return findCountBySQL(sql.toString());
    }

    /**
     * 加载通讯过的客户列表
     * 
     * @description
     * @param userinfos
     *        当前接入的客户
     * @param aId
     *        公众号id
     * @param customeId
     *        客服人员id
     * @param timeCondition
     *        时间条件
     * @return
     * @throws Exception
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-2-19 下午05:26:04
     */
    public List<DynaBean> getChatUserInfos(String aId, String timeCondition) throws Exception
    {
        String groupSql = "select from_user,max(create_time) ctime from lf_onl_server where a_id=" + aId + " " + " group by from_user,a_id";
        String sql = "select  ser.from_user,ser.create_time,ser.ser_num,userinfo.nick_name " + "from lf_onl_server ser inner join " + "(" + groupSql + ") gser " + "on (gser.from_user = ser.from_user and gser.ctime = ser.create_time) " + "inner join lf_wei_userinfo userinfo on ser.from_user = userinfo.open_id " + "where ser.a_id = " + aId + "  and userinfo.a_id= " + aId;
        if(timeCondition != null)
        {
            if("1".equals(String.valueOf(StaticValue.DBTYPE))){
                sql += " and ser.create_time > to_date('"+timeCondition+"','yyyy-mm-dd hh24:mi:ss')"; 
            }else{
                sql += " and ser.create_time > '" + timeCondition + "'"; 
            }
        }
        return getListDynaBeanBySql(sql);
    }
    
    public JSONArray getOpenIdByOnLineGroup(String aId, String timeCondition) throws Exception
    {
    	String groupSql = "select from_user,max(create_time) ctime from lf_onl_server where a_id=" + aId + " " + " group by from_user,a_id";
        String sql = "select  ser.from_user from lf_onl_server ser inner join " + "(" + groupSql + ") gser " + "on (gser.from_user = ser.from_user and gser.ctime = ser.create_time) " + "inner join lf_wei_userinfo userinfo on ser.from_user = userinfo.open_id " + "where ser.a_id = " + aId + "  and userinfo.a_id= " + aId;
        if(timeCondition != null)
        {
            if("1".equals(String.valueOf(StaticValue.DBTYPE))){
                sql += " and ser.create_time > to_date('"+timeCondition+"','yyyy-mm-dd hh24:mi:ss')"; 
            }else{
                sql += " and ser.create_time > '" + timeCondition + "'"; 
            }
        }
        return findJsonArrayBySQL(sql, TableLfOnlServer.FROM_USER);
    }

    /** 通过sql语句查询结果
     * @param sql
     * @return 结果集
     * @throws Exception
     */
    private List<String> findListArrayBySQL(String sql, String columnName) throws Exception
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> resultList = new ArrayList<String>();
        try
        {
            conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
            EmpExecutionContext.sql("execute sql : " + sql);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next())
            {
                resultList.add(rs.getString(columnName));
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.debug("查询总页数出错！");
        }
        finally
        {
            try
            {
                close(rs, ps, conn);
            }
            catch (Exception e)
            {
                EmpExecutionContext.debug("关闭数据库资源出错！");
            }
        }
        return resultList;
    }

    /**通过sql语句查询转换为json结果集
     * @param sql
     * @return JSONArray数组
     * @throws Exception
     */
    private JSONArray findJsonArrayBySQL(String sql, String columnName) throws Exception
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        JSONArray resultList = new JSONArray();
        try
        {
            conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
            EmpExecutionContext.sql("execute sql : " + sql);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while(rs.next())
            {
                resultList.add(rs.getString(columnName));
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.debug("查询总页数出错！");
        }
        finally
        {
            try
            {
                close(rs, ps, conn);
            }
            catch (Exception e)
            {
                EmpExecutionContext.debug("关闭数据库资源出错！");
            }
        }
        return resultList;
    }
}
