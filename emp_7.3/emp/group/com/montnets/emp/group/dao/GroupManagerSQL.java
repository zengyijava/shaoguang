package com.montnets.emp.group.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.database.IConnectionManager;
import com.montnets.emp.util.StringUtils;

public class GroupManagerSQL {

    protected IConnectionManager connectionManager;

    public GroupManagerSQL() {
        connectionManager = new ConnectionManagerImp();
    }

    /**
     * @description 关闭Result返回的结果集，PreparedStatements批量执行语句 connection数据库链接
     * @param rs
     * @param ps
     * @param conn
     * @throws Exception
     */
    public void close(ResultSet rs, PreparedStatement ps, Connection conn) throws Exception {
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * @param rs
     * @param ps
     * @throws Exception
     */
    public void close(ResultSet rs, PreparedStatement ps) throws Exception {
        if (rs != null) {
            rs.close();
        }
        if (ps != null) {
            ps.close();
        }
    }

    public String executeProcessReutrnCursorOfOracle(String POOLNAME,
                                                     String processStr,
                                                     Integer[] params) throws Exception {
        Connection conn = null;
        ResultSet rs = null;
        CallableStatement proc = null;
        StringBuffer deps = new StringBuffer();
        String depIds = "";
        try {

            conn = connectionManager.getDBConnection(POOLNAME);
            proc = conn.prepareCall("{ call " + processStr + "(?,?,?,?) }");
            proc.setInt(1, params[0]);
            proc.setInt(2, params[1]);
            proc.setString(3, StringUtils.getRandom());
            proc.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
            proc.execute();
            rs = (ResultSet) proc.getObject(4);
            while (rs.next()) {
                deps.append(rs.getLong("DepID")).append(",");
            }
            if (deps.length() > 0)
                depIds = deps.substring(0, deps.lastIndexOf(","));
        }
        catch (Exception e) {
            EmpExecutionContext.error(e, "群组操作数据库异常！");
            throw e;
        }
        finally {

            try {
                close(rs, proc, conn);
            }
            catch (SQLException e) {
                EmpExecutionContext.error(e, "群组关闭数据库资源出错！");
            }
        }
        return depIds;
    }

    public String getClientChildIdByDepId(String depId) throws Exception {
        String depIds = depId;
        String conditionDepid = depId;
        String sql = "";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int incount = 0;
        // int maxIncount = StaticValue.inConditionMax;
        int maxIncount = StaticValue.getInConditionMax();
        int n = 1;
        try {
            conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
            boolean hasNext = true;
            while (hasNext) {
                incount = 0;
                sql = "select dep_Id from lf_client_dep where PARENT_ID in ( "
                      + conditionDepid
                      + ")";
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                conditionDepid = "";
                while (rs.next()) {
                    incount++;
                    depIds += "," + rs.getString("dep_id");
                    if (incount > maxIncount * n) {
                        n++;
                        conditionDepid = conditionDepid.substring(0, conditionDepid.length() - 1);
                        conditionDepid += ") or PARENT_ID in (";
                    }
                    conditionDepid += rs.getString("dep_id") + ",";
                }
                if (conditionDepid.length() == 0) {
                    hasNext = false;
                } else {
                    hasNext = true;
                    conditionDepid = conditionDepid.substring(0, conditionDepid.length() - 1);
                }
            }
        }
        catch (Exception e) {
            EmpExecutionContext.error(e, "客户群组查询失败！");

        }
        finally {

            try {
                close(rs, ps, conn);
            }
            catch (SQLException e) {
                EmpExecutionContext.debug("关闭数据库资源出错！");
                EmpExecutionContext.error(e, "关闭数据库资源出错！");
            }
        }
        return depIds;
    }

    public String executeProcessReutrnCursorOfMySql(String POOLNAME,
                                                    String processStr,
                                                    Integer[] params) throws Exception {
        String sql = "call " + processStr + "(?,?,?)";
        Connection conn = null;
        ResultSet rs = null;
        StringBuffer deps = new StringBuffer();
        String depIds = "";
        CallableStatement comm = null;
        try {
            conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
            EmpExecutionContext.sql("execute sql : " + sql);
            comm = ((Connection) conn).prepareCall(sql);
            comm.setInt(1, params[0]);
            comm.setLong(2, params[1]);
            comm.setString(3, StringUtils.getRandom());
            comm.execute();
            rs = comm.getResultSet();
            while (rs.next()) {
                deps.append(rs.getLong("DepID")).append(",");
            }
            depIds = deps.substring(0, deps.lastIndexOf(",")).toString();
        }
        catch (Exception e) {
            EmpExecutionContext.error(e, "群组调用存储过程操作异常！");
            throw e;
        }
        finally {
            try {
                close(rs, comm, conn);
            }
            catch (SQLException e) {
                EmpExecutionContext.debug("关闭数据库资源出错！");
                EmpExecutionContext.error(e, "关闭数据库资源出错！");
            }
        }
        return depIds;
    }

}
