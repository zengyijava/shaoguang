package com.montnets.emp.netnews.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.netnews.entity.LfWXData;
import com.montnets.emp.netnews.entity.LfWXTrustCols;
import com.montnets.emp.netnews.table.TableLfWXData;
import com.montnets.emp.netnews.table.TableLfWXDataBind;
import com.montnets.emp.netnews.table.TableLfWXDataType;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

public class TrustDataDao extends SuperDAO {

    /***
     * 获得后台查询数据
     */
    
    public List<DynaBean> getData(LinkedHashMap<String, String> conditionMap,
                                  PageInfo pageInfo) throws Exception {
        // 拼接sql
        String sql = "select trustdata.*,sysuser.name username,dep.dep_Name depname,datatype.name typename,bind.did  binddid";
        String baseSql = new StringBuffer(" from ").append(
                TableLfWXData.TABLE_NAME).append(" trustdata left join ")
                .append(TableLfWXDataType.TABLE_NAME).append(" datatype on trustdata.datatypeid = datatype.id")
                .append(" left join ").append(TableLfSysuser.TABLE_NAME).append(
                        " sysuser on trustdata.userid=sysuser.user_id left join ")
                .append(TableLfDep.TABLE_NAME).append(" dep on dep.dep_id = sysuser.dep_id left join ")
                .append("(SELECT DID FROM ").append(TableLfWXDataBind.TABLE_NAME).append(" GROUP BY DID) bind on trustdata.did=bind.did ")// 判断是否被引用
                .toString();
        String conditionSql = getConditionSql(conditionMap);
        //存在查询条件
        if (conditionSql != null && conditionSql.length() > 0) {
            //将条件字符串首个and替换为where
            conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
        }

        String orderbySql = " order by trustdata.did DESC";
        sql = sql + baseSql + conditionSql + orderbySql;

        String countSql = "select count(*) totalcount " + baseSql;
        countSql += conditionSql;
        // 执行查询
        List<DynaBean> returnList = new DataAccessDriver()
                .getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
        //.findPageVoListBySQL(LfWXTrustDataVo.class,
        //sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);

        // 返回结果
        return returnList;
    }

    /**
     * 拼装条件
     *
     * @param conditionMap
     * @return
     */
    private String getConditionSql(LinkedHashMap<String, String> conditionMap) {
        StringBuffer sql = new StringBuffer();
        String corpCode = conditionMap.get("corpCode");
        if (corpCode != null
                && !"".equals(corpCode)) {
            sql.append(" and trustdata.corp_code = '").append(
                    corpCode).append("'");
        }
        String code = conditionMap.get("code");
        if (code != null && !"".equals(code)) {
            sql.append(" and trustdata.code like '%").append(
                    code).append("%'");
        }

        String name = conditionMap.get("name");
        if (name != null && !"".equals(name)) {
            sql.append(" and trustdata.name like '%").append(
                    name).append("%'");
        }
        String dataType = conditionMap.get("dataType");
        if (dataType != null && !"".equals(dataType)) {
            sql.append(" and trustdata.dataTypeId =").append(dataType);
        }
        String chUser = conditionMap.get("chUser");
        if (chUser != null && !"".equals(chUser)) {
            sql.append(" and sysuser.name like '%").append(
                    chUser).append("%'");
        }

        IGenericDAO genericDao = new DataAccessDriver().getGenericDAO();

        String chDate = conditionMap.get("chDate");
        if (chDate != null && !"".equals(chDate)) {
            sql.append(" and trustdata.creatDate >=").append(
                    genericDao.getTimeCondition(chDate)).append("");
        }
        String chEndDate = conditionMap.get("chEndDate");
        if (chEndDate != null && !"".equals(chEndDate)) {
            sql.append(" and trustdata.creatDate <=").append(
                    genericDao.getTimeCondition(chEndDate)).append("");
        }
        return sql.toString();
    }

    /**
     * 动态生成业务数据表
     *
     * @param tableName 数据表名
     * @param colList   业务数据字段List
     * @return boolean
     */
    
    public boolean trustDataTable(String tableName,
                                  List<LfWXTrustCols> colList, String trustType) throws SQLException {
        Connection conn = connectionManager
                .getDBConnection(StaticValue.EMP_POOLNAME);
        Statement stmt = conn.createStatement();
        boolean optFlag = false;

        try {
            String tableSql = getSql(tableName, colList);

            stmt.execute(tableSql);

            optFlag = true;
        } catch (SQLException e) {
            EmpExecutionContext.error(e, "查询互动项失败");
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                EmpExecutionContext.error(e, "数据库关闭失败");
            }
        }
        return optFlag;
    }

    private String getSql(String tableName, List<LfWXTrustCols> colList) {
        if (colList == null || colList.size() == 0) {
            EmpExecutionContext.error("列字段为空");
            return null;
        }

        if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE) {
            //Oracle数据库
            return getOracleSql(tableName, colList);
        } else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE) {
            //sqlserver数据库
            return getSqlserverSql(tableName, colList);
        } else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) {
            //db2数据库
            return getDB2Sql(tableName, colList);
        } else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE) {
            //mysql数据库
            return getMySqlSql(tableName, colList);
        }
        return null;
    }

    /**
     * 生成sql
     *
     * @param tableName
     * @param colList
     * @return
     */
    private String getSqlserverSql(String tableName, List<LfWXTrustCols> colList) {
        try {
            StringBuffer sql = new StringBuffer("CREATE TABLE ").append(tableName).append(" ( ");

            LfWXTrustCols trustDataCol = null;
            for (int i = 0; i < colList.size(); i++) {
                String typeSize = "";
                trustDataCol = colList.get(i);

                int colSize = trustDataCol.getColSize();
                if (trustDataCol.getColType() == 0) {
                    //字符串类型
                    colSize = trustDataCol.getColSize() * 2;
                    typeSize = "VARCHAR(" + colSize + ")";
                } else if (trustDataCol.getColType() == 1) {
                    //数字类型
                    typeSize = "INTEGER ";
                } else if (trustDataCol.getColType() == 2) {
                    //日期类型
                    typeSize = "DATETIME ";
                }

                sql.append(trustDataCol.getColName()).append(" ").append(
                        typeSize).append(",");
            }

            String tableSql = sql.toString();
            tableSql = tableSql.substring(0, tableSql.length() - 1) + ");";
            return tableSql;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "组装sql语句失败");
            return null;
        }
    }

    /**
     * 生成sql
     *
     * @param tableName
     * @param colList
     * @return
     */
    private String getOracleSql(String tableName, List<LfWXTrustCols> colList) {
        try {
            StringBuffer sql = new StringBuffer("CREATE TABLE ").append(tableName).append(" ( ");

            LfWXTrustCols trustDataCol = null;
            for (int i = 0; i < colList.size(); i++) {
                String typeSize = "";
                trustDataCol = colList.get(i);

                int colSize = trustDataCol.getColSize();
                if (trustDataCol.getColType() == 0) {
                    //字符串类型
                    colSize = trustDataCol.getColSize() * 2;
                    typeSize = "VARCHAR2(" + colSize + ") ";
                } else if (trustDataCol.getColType() == 1) {
                    //数字类型
                    colSize = trustDataCol.getColSize();
                    typeSize = "NUMBER (" + colSize + ") ";
                } else if (trustDataCol.getColType() == 2) {
                    //日期类型
                    colSize = trustDataCol.getColSize();
                    typeSize = "TIMESTAMP (" + colSize + ") ";
                }

                sql.append(trustDataCol.getColName()).append(" ").append(
                        typeSize).append(",");
            }

            String tableSql = sql.toString();
            tableSql = tableSql.substring(0, tableSql.length() - 1) + ")";
            return tableSql;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "组装sql语句失败");
            return null;
        }
    }

    /**
     * 生成sql
     *
     * @param tableName
     * @param colList
     * @return
     */
    private String getDB2Sql(String tableName, List<LfWXTrustCols> colList) {
        try {
            StringBuffer sql = new StringBuffer("CREATE TABLE ").append(tableName).append(" ( ");

            LfWXTrustCols trustDataCol = null;
            for (int i = 0; i < colList.size(); i++) {
                String typeSize = "";
                trustDataCol = colList.get(i);

                int colSize = trustDataCol.getColSize();
                if (trustDataCol.getColType() == 0) {
                    //字符串类型
                    colSize = trustDataCol.getColSize() * 2;
                    typeSize = "VARCHAR(" + colSize + ") ";
                } else if (trustDataCol.getColType() == 1) {
                    //数字类型
                    colSize = trustDataCol.getColSize();
                    typeSize = "INTEGER ";
                } else if (trustDataCol.getColType() == 2) {
                    //日期类型
                    colSize = trustDataCol.getColSize();
                    typeSize = "TIMESTAMP ";
                }

                sql.append(trustDataCol.getColName()).append(" ").append(
                        typeSize).append(",");
            }

            String tableSql = sql.toString();
            tableSql = tableSql.substring(0, tableSql.length() - 1) + ") IN EMP_TABLESPACE";
            return tableSql;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "组装sql语句失败");
            return null;
        }
    }

    /**
     * 生成sql
     *
     * @param tableName
     * @param colList
     * @return
     */
    private String getMySqlSql(String tableName, List<LfWXTrustCols> colList) {
        try {
            StringBuffer sql = new StringBuffer("CREATE TABLE `").append(tableName).append("` ( ");

            LfWXTrustCols trustDataCol = null;
            for (int i = 0; i < colList.size(); i++) {
                String typeSize = "";
                trustDataCol = colList.get(i);

                int colSize = trustDataCol.getColSize();
                if (trustDataCol.getColType() == 0) {
                    //字符串类型
                    colSize = trustDataCol.getColSize() * 2;
                    typeSize = "VARCHAR(" + colSize + ") ";
                } else if (trustDataCol.getColType() == 1) {
                    //数字类型
                    typeSize = "BIGINT ";
                } else if (trustDataCol.getColType() == 2) {
                    //日期类型

                    typeSize = "DATETIME ";
                }

                sql.append(" `").append(trustDataCol.getColName()).append("` ").append(
                        typeSize).append(",");
            }

            String tableSql = sql.toString();
            tableSql = tableSql.substring(0, tableSql.length() - 1) + ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            return tableSql;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "组装sql语句失败");
            return null;
        }
    }

    /**
     * 动态创建表信息
     *
     * @param tableName 数据表名
     * @param colList   插入信息
     * @param trustType 插入类型
     */
    
    public boolean createDataTable(String tableName, List<LfWXData> colList, String trustType) throws SQLException {

        Connection conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
        Statement stmt = conn.createStatement();
        boolean optFlag = false;
        String tableSql = "";

        try {
            if (colList != null && colList.size() > 0) {
                StringBuffer sql = new StringBuffer("CREATE TABLE ");
                sql.append(tableName).append("(ID INT IDENTITY(1,1),");
                LfWXData trustDataCol = null;
                for (int i = 0; i < colList.size(); i++) {
                    trustDataCol = colList.get(i);
                    String primary = "";
                    int colSize = trustDataCol.getColSize();
                    if (trustDataCol.getColType() == 0) {
                        colSize = trustDataCol.getColSize() * 2;
                    }
                    if (i == 0) {
                        if (trustType.equals("0")) {
                            primary = " PRIMARY KEY";
                        }
                        colSize = trustDataCol.getColSize();
                    } else {
                        if (colSize > 8000) {
                            colSize = trustDataCol.getColSize();
                        }
                    }
                    String typeSize = "VARCHAR(" + colSize + ")" + primary;
                    sql.append(trustDataCol.getColName()).append(" ").append(
                            typeSize).append(",");
                }
                if (trustType.equals("1")) {
                    sql.append("PAGEID VARCHAR(32),DATE_TIME VARCHAR(32),");
                }
                tableSql = sql.toString();
                tableSql = tableSql.substring(0, tableSql.length() - 1) + ");";
                // 创建索引
                tableSql += "CREATE INDEX " + "Index_"
                        + tableName.substring(11) + " ON " + tableName
                        + "(C0_SHOUJIHAOMA);";
                stmt.execute(tableSql);
            }
            optFlag = true;
        } catch (SQLException e) {
            EmpExecutionContext.error(e, "创建索引失败");
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                EmpExecutionContext.error(e, "关闭连接失败");
            }
        }
        return optFlag;
    }

    /**
     * 删除表
     *
     * @param tableName 数据表名
     */
    
    public boolean delTrustTable(String tableName) throws Exception {
        String sql = "DROP TABLE " + tableName;
        Connection conn = connectionManager
                .getDBConnection(StaticValue.EMP_POOLNAME);
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.execute();
            return true;
        } catch (SQLException e) {
            EmpExecutionContext.error(e, "删除表失败");
            return false;
        } finally {
            super.close(null, ps, conn);
        }

    }

    /**
     * 获取所有手机号码HashSet
     *
     * @param tableName 业务数据表名
     * @return HashSet
     * @throws Exception
     */
    
    public HashSet<String> getTrustDataMobile(String tableName)
            throws Exception {

        String sql = "select  t.C0_SHOUJIHAOMA from " + tableName + " t";
        Connection conn = connectionManager
                .getDBConnection(StaticValue.EMP_POOLNAME);
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashSet<String> dataSet = new HashSet<String>();
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                dataSet.add(rs.getString("C0_SHOUJIHAOMA"));
            }
        } catch (SQLException e) {
            EmpExecutionContext.error(e, "获取所有手机号码HashSet");
        } finally {
            super.close(rs, ps, conn);
        }

        return dataSet;
    }

    /**
     * 导入业务数据
     *
     * @param tableName 业务数据表名
     * @return boolean
     */
    
    public boolean trustDataImport(String tableName,
                                   List<List<String>> trustList) throws SQLException {
        Connection conn = connectionManager
                .getDBConnection(StaticValue.EMP_POOLNAME);
        Statement stmt = null;
        boolean optFlag = false;
        String tableSql = "";
        try {
            stmt = conn.createStatement();
            for (int k = 0; k < trustList.size(); k++) {
                List<String> eachList = trustList.get(k);
                String value = "";
                for (int i = 0; i < eachList.size(); i++) {
                    String colValue = eachList.get(i).toString();
                    value += colValue + "','";
                }
                value = value.substring(0, value.length() - 3);
                tableSql = "INSERT INTO " + tableName + " VALUES('" + value
                        + "')";
                stmt.addBatch(tableSql);
                if (k % 10000 == 0) {
                    stmt.executeBatch();
                }
            }
            stmt.executeBatch();
            optFlag = true;
        } catch (SQLException e) {
            EmpExecutionContext.error(e, "导入业务数据异常");
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                EmpExecutionContext.error(e, "关闭数据库连接异常");
            }
        }
        return optFlag;
    }

    /**
     * 获取所有手机号码HashSet
     *
     * @param tableName 业务数据表名
     * @return HashSet
     * @throws Exception
     */
    
    public List<List<String>> getTrustDataView(String tableName, int colNum,
                                               PageInfo pageInfo) throws Exception {

        String sql = "select  t.* from " + tableName + " t order by t.id";
        String countSql = "select count(id) totalcount from " + tableName
                + " t";
        Connection conn = connectionManager
                .getDBConnection(StaticValue.EMP_POOLNAME);
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<List<String>> rsList = new ArrayList<List<String>>();
        List<String> list = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            EmpExecutionContext.sql("execute sql : " + countSql);
            ps = conn.prepareStatement(countSql);
            // 执行SQL查询语句，返回ResultSet
            rs = ps.executeQuery();
            if (rs.next()) {
                // 当前页数
                int pageSize = pageInfo.getPageSize();
                // 总记录数
                int totalCount = rs.getInt("totalcount");
                // 总页数
                int totalPage = totalCount % pageSize == 0 ? totalCount
                        / pageSize : totalCount / pageSize + 1;
                pageInfo.setTotalRec(totalCount);
                pageInfo.setTotalPage(totalPage);
                // 当前页大于总页数则跳转到第一页
                if (pageInfo.getPageIndex() > totalPage) {
                    pageInfo.setPageIndex(1);
                }
            }
            // 开始行数
            int beginCount = pageInfo.getPageSize() * pageInfo.getPageIndex()
                    - (pageInfo.getPageSize() - 1);// 开始行数
            // 结束行数
            int endCount = pageInfo.getPageSize() * pageInfo.getPageIndex();// 结束行数
            StringBuffer sqlSb = new StringBuffer();
            sql = sql.substring(sql.indexOf("select") + 7, sql.length());
            sql = new StringBuffer("select top ").append(endCount).append(
                    " 0 as tempColumn,").append(sql).toString();
            sqlSb
                    .append(
                            "select * from (select row_number() over(order by tempColumn) tempRowNumber,* from (")
                    .append(sql).append(
                    ") t) tt where tempRowNumber>=" + beginCount);
            sql = sqlSb.toString();
            EmpExecutionContext.sql("execute sql : " + sql);
            ps = conn.prepareStatement(sql);
            // 执行SQL
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
//            colNum = rsmd.getColumnCount();
            Integer count = rsmd.getColumnCount();
            while (rs.next()) {
                list = new ArrayList<String>();
                for (int i = 3; i <= count; i++) {
                    list.add(rs.getString(i));
                }
                rsList.add(list);
            }

        } catch (SQLException e) {
            EmpExecutionContext.error(e, "获取所有手机号码HashSet");
        } finally {
            super.close(rs, ps, conn);
        }

        return rsList;
    }

    /**
     * 删除业务数据记录
     *
     * @param tableName 业务数据表名
     * @param ID        自动编号
     * @return int
     * @throws Exception
     */
    
    public int trustDataDelView(String tableName, String ID) throws Exception {
        String sql = "delete from " + tableName + " where id=" + ID;
        Connection conn = connectionManager
                .getDBConnection(StaticValue.EMP_POOLNAME);
        PreparedStatement ps = null;
        int optFlag = 0;
        try {
            ps = conn.prepareStatement(sql);
            optFlag = ps.executeUpdate();
        } catch (SQLException e) {
            EmpExecutionContext.error(e, "删除业务数据记录");
        } finally {
            super.close(null, ps, conn);
        }
        return optFlag;
    }

    /**
     * 编辑业务数据
     *
     * @param tableName 业务数据表名
     * @param ID        自动编号
     * @return List
     * @throws Exception
     */
    
    public List<String> trustDataViewEdit(String tableName, String id)
            throws Exception {
        String sql = "select * from " + tableName + " where id=" + id;
        Connection conn = connectionManager
                .getDBConnection(StaticValue.EMP_POOLNAME);
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> rsList = new ArrayList<String>();
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int colNum = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 2; i <= colNum; i++) {
                    rsList.add(rs.getString(i));
                }
            }
        } catch (SQLException e) {
            EmpExecutionContext.error(e, "编辑业务数据异常");
        } finally {
            super.close(rs, ps, conn);
        }
        return rsList;
    }

    /**
     * 判断手机号码是否存在
     *
     * @return boolean true：存在；false：不存在
     */
    
    public boolean checkTrustDataPhone(String tableName, String phone)
            throws SQLException {

        String sql = "select * from " + tableName + " where C0_SHOUJIHAOMA='"
                + phone + "'";
        Connection conn = connectionManager
                .getDBConnection(StaticValue.EMP_POOLNAME);
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean optFlag = false;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                optFlag = true;
            }
        } catch (SQLException e) {
            EmpExecutionContext.error(e, "查询数据异常");
        } finally {
            try {
                super.close(rs, ps, conn);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "关闭连接失败");
            }
        }

        return optFlag;
    }

    /**
     * 更新业务数据
     *
     * @param tableName 业务数据表名
     * @param ID        自动编号
     * @param colList   字段List
     * @param dataList  数据List
     * @return boolean
     * @throws Exception
     */
    
    public boolean trustDataUpdate(String tableName, String ID, List<LfWXTrustCols> colList,
                                   List<String> dataList) throws Exception {

        Connection conn = connectionManager
                .getDBConnection(StaticValue.EMP_POOLNAME);
        PreparedStatement ps = null;
        boolean optFlag = false;
        String tableSql = "";
        try {
            String colValue = "";
            LfWXTrustCols trustCols = null;
            for (int i = 0; i < colList.size(); i++) {
                trustCols = colList.get(i);
                String value = dataList.get(i);
                value = value.replaceAll("'", "''");
                colValue += trustCols.getColName() + "='" + value + "',";
            }
            colValue = colValue.substring(0, colValue.length() - 1);
            tableSql = "UPDATE " + tableName + " SET " + colValue
                    + " WHERE ID = " + ID;
            ps = conn.prepareStatement(tableSql);
            ps.executeUpdate();
            optFlag = true;

        } catch (SQLException e) {
            EmpExecutionContext.error(e, "更新表执行异常");
        } finally {
            super.close(null, ps, conn);
        }
        return optFlag;
    }

    
    public boolean dropTableByTableName(Connection conn, String tableName)
            throws Exception {
        StringBuffer sqldeleteTable = new StringBuffer("DROP TABLE ")
                .append(tableName.toUpperCase());
        boolean result = false;
        Statement ps = null;
        try {
            ps = conn.createStatement();
            ps.executeUpdate(sqldeleteTable.toString());
            result = true;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "删除表执行异常");
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return result;
    }
}
