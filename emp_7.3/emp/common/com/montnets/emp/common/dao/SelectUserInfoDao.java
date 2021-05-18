package com.montnets.emp.common.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.common.vo.LfCustFieldValueVo;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.table.client.TableLfCliDepConn;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.client.TableLfClientDep;
import com.montnets.emp.table.client.TableLfCustField;
import com.montnets.emp.table.client.TableLfCustFieldValue;
import com.montnets.emp.table.employee.TableLfEmpDepConn;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.employee.TableLfEmployeeDep;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.group.TableLfMalist;
import com.montnets.emp.table.group.TableLfUdgroup;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;
import org.apache.commons.beanutils.DynaBean;

import java.util.ArrayList;
import java.util.List;

public class SelectUserInfoDao extends SuperDAO {
    IGenericDAO genericDAO = new DataAccessDriver().getGenericDAO();
    public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId, String depId, String corpCode) throws Exception{
        String sql = "";
        List<LfEmployeeDep> lfEmployeeDepList = null;
        if (depId == null || "".equals(depId)) {
            // 这里是为了解决员工机构关联表出现了多条记录的处理方法，产生2条记录的原因可能是机器卡，或者同步
            List<LfEmpDepConn> connList = null;
            String sb = " select * from " + TableLfEmpDepConn.TABLE_NAME + " c " + StaticValue.getWITHNOLOCK() + " where c." +
                    TableLfEmpDepConn.USER_ID + " = " + userId;
            connList = findEntityListBySQL(LfEmpDepConn.class, sb, StaticValue.EMP_POOLNAME);
            if (connList != null && connList.size() > 0) {
                //机构ID字符串
                StringBuilder ids = new StringBuilder();
                for(LfEmpDepConn co:connList){
                    ids.append(co.getDepId()).append(",");
                }
                //去掉最后一个逗号
                ids = new StringBuilder(ids.substring(0, ids.lastIndexOf(",")));
                //生成SQL语句，必须加企业编码
                sql = " select * from " +
                        TableLfEmployeeDep.TABLE_NAME +
                        " " + StaticValue.getWITHNOLOCK() + " where  (" +
                        TableLfEmployeeDep.DEP_ID + " in(" +
                        ids + ") or " +
                        TableLfEmployeeDep.PARENT_ID + " in(" +
                        ids + ")) " + " AND CORP_CODE='" + corpCode + "' " + " order by " +
                        TableLfEmployeeDep.ADD_TYPE +
                        " " + StaticValue.ASC;
                //查询数据
                lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql, StaticValue.EMP_POOLNAME);
            }
        } else {
            //查询的SQL语句中增加企业编码
            sql = " select * from " +
                    TableLfEmployeeDep.TABLE_NAME +
                    " " + StaticValue.getWITHNOLOCK() + " where " +
                    TableLfEmployeeDep.PARENT_ID + " = " + depId + " AND CORP_CODE='" + corpCode + "' " + " order by " +
                    TableLfEmployeeDep.ADD_TYPE + " " + StaticValue.ASC;
            //查询数据
            lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql,
                    StaticValue.EMP_POOLNAME);
        }
        return lfEmployeeDepList;
    }

    public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId, String depId, String corpCode) throws Exception{
        //定义SQL语句字符串
        String sql = "";
        //查询第一级和第二级机构
        //查询必须带企业编码
        if (depId == null || "".equals(depId)) {
            sql = " select e.* from " +
                    TableLfClientDep.TABLE_NAME +
                    " e " + StaticValue.getWITHNOLOCK() + "," +
                    TableLfCliDepConn.TABLE_NAME +
                    " c " + StaticValue.getWITHNOLOCK() + " where c." +
                    TableLfCliDepConn.USER_ID + " = " + userId + " AND e.CORP_CODE='" + corpCode + "' " +
                    " and (c." + TableLfEmpDepConn.DEP_ID +
                    " =e." + TableLfClientDep.DEP_ID +
                    " or " + TableLfClientDep.PARENT_ID +
                    " = c." + TableLfCliDepConn.DEP_ID +
                    ")";
        } else {
            //根据父机构，查询第一级子机构
            //查询必须带企业编码
            sql = " select * from " +
                    TableLfClientDep.TABLE_NAME +
                    " " + StaticValue.getWITHNOLOCK() + " where " +
                    TableLfClientDep.PARENT_ID + " = " + depId + " AND CORP_CODE='" + corpCode + "' ";
        }
        //查询数据
        return findEntityListBySQL(LfClientDep.class, sql, StaticValue.EMP_POOLNAME);
    }

    public List<GroupInfoVo> findGroupUserByIds(Long groupId, PageInfo pageInfo) throws Exception{
        //初始化LIST
        List<GroupInfoVo> returnVoList = null;
        //拼凑查询字段
        StringBuffer sqlStr = new StringBuffer("select  list2gro.").append(TableLfList2gro.UDG_ID).append(",list2gro.").
                append(TableLfList2gro.L2G_ID).append(",list2gro.").append(TableLfList2gro.L2G_TYPE).append(",list2gro.").
                append(TableLfList2gro.GUID).append(",udgroup.").append(TableLfUdgroup.GP_ATTRIBUTE).append(",udgroup.").
                append(TableLfUdgroup.GROUP_TYPE).append(",udgroup.").append(TableLfUdgroup.USER_ID).append(",case(list2gro.").
                append(TableLfList2gro.L2G_TYPE).append(") when 0 then employee.").append(TableLfEmployee.MOBILE).
                append("  else malist.").append(TableLfMalist.MOBILE).append(" end as MOBILE,case(list2gro.").
                append(TableLfList2gro.L2G_TYPE).append(") when 0 then employee.").append(TableLfEmployee.NAME).
                append("  else malist.").append(TableLfMalist.NAME).append(" end as NAME,udgroup.").append(TableLfUdgroup.UDG_NAME);
        //拼凑TABLE
        sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro " ).append(StaticValue.getWITHNOLOCK()).append(" left join ")
                .append(TableLfEmployee.TABLE_NAME).append(" employee  ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
                .append("=employee.").append(TableLfEmployee.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(
                " malist " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
                .append("=malist.").append(TableLfMalist.GUID).append(" inner join ").append(TableLfUdgroup.TABLE_NAME)
                .append(" udgroup " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID)
                .append("=udgroup.").append(TableLfUdgroup.GROUP_ID).append(" where udgroup.")
                .append(TableLfUdgroup.UDG_ID).append("=").append(groupId);
        //查询
        String countSql = "select count(*) totalcount from ("+sqlStr.toString()+") a";
        sqlStr.append( " order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc");
        returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(GroupInfoVo.class, sqlStr.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
        //返回
        return returnVoList;
    }

    public List<GroupInfoVo> findGroupClientByIds(Long groupId, PageInfo pageInfo) throws Exception{
        //初始化LIST
        List<GroupInfoVo> returnVoList = null;
        //拼凑查询字段
        StringBuilder sqlStr = new StringBuilder("select  list2gro.").append(TableLfList2gro.UDG_ID).append(",list2gro.").append(
                TableLfList2gro.L2G_ID).append(",list2gro.").append(TableLfList2gro.L2G_TYPE).append(",list2gro.").append(TableLfList2gro.GUID).append(",udgroup.").append(
                TableLfUdgroup.GP_ATTRIBUTE).append(",udgroup.").append(TableLfUdgroup.GROUP_TYPE).append(",udgroup.").append(
                TableLfUdgroup.USER_ID).append(",case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(") when 1 then client.").
                append(TableLfClient.MOBILE).append("  else malist.").append(TableLfMalist.MOBILE).append(" end as MOBILE,case(list2gro.").
                append(TableLfList2gro.L2G_TYPE).append(") when 1 then client.").append(TableLfClient.NAME).append("  else malist.").
                append(TableLfMalist.NAME).append(" end as NAME,udgroup.").append(TableLfUdgroup.UDG_NAME);
        //拼凑TABLE
        sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro " ).append(StaticValue.getWITHNOLOCK()).append(" left join ")
                .append(TableLfClient.TABLE_NAME).append(" client  ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
                .append("=client.").append(TableLfClient.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(
                " malist " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
                .append("=malist.").append(TableLfMalist.GUID).append(" inner join ").append(TableLfUdgroup.TABLE_NAME)
                .append(" udgroup " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID)
                .append("=udgroup.").append(TableLfUdgroup.UDG_ID).append(" where udgroup.")
                .append(TableLfUdgroup.UDG_ID).append("=").append(groupId);
        //查询
        String countSql = "select count(*) totalcount from ( " + sqlStr.toString() + " ) a";
        sqlStr.append( " order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc ");

        returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(GroupInfoVo.class, sqlStr.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
        //返回
        return returnVoList;
    }
    /**
     *    查询客户机构人员
     * @param clientDep	当前机构对象
     * @param containType	是否包含  1包含   2不包含
     * @param pageInfo	分页
     * @return
     * @throws Exception
     */
    public List<DynaBean> findClientsByDepId(LfClientDep clientDep, Integer containType, PageInfo pageInfo) {
            List<DynaBean> beanList = null;
            String sql = "select distinct client.NAME,client.MOBILE,client.GUID " ;
            String countSql = "select count(*) totalcount ";
            String baseSql = " from LF_CLIENT client inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID ";
            StringBuffer conditionSql = new StringBuffer(" where ");
            conditionSql.append(" client.CORP_CODE = '").append(clientDep.getCorpCode()).append("'");
            if(containType == 1){
                conditionSql.append(" and depsp.DEP_ID in (select DEP_ID from LF_CLIENT_DEP where DEP_PATH like '").append(clientDep.getDeppath()).append("%') ");
            }else if(containType == 2){
                conditionSql.append(" and depsp.DEP_ID = ").append(clientDep.getDepId());
            }
            String orderSql = " order by client.GUID DESC";
            sql += baseSql;
            countSql += baseSql;
            sql += conditionSql + orderSql;
            countSql += conditionSql;
            beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
            return beanList;
        }

    public List<DynaBean> findClientByName(String name, String corpCode) {
            String sql = "SELECT DISTINCT CLIENT.NAME,CLIENT.MOBILE,CLIENT.GUID" +
                    " FROM LF_CLIENT CLIENT INNER JOIN" +
                    " LF_CLIENT_DEP_SP DEPSP ON CLIENT.CLIENT_ID = DEPSP.CLIENT_ID" +
                    " WHERE CLIENT.CORP_CODE = '"+corpCode+"' AND CLIENT.NAME like '%" +
                    name + "%' ORDER BY CLIENT.GUID DESC" ;
            return new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
    }

    public List<GroupInfoVo> findAllGroupByName(String name, Long lguserid, String udgId) throws Exception{
        /*String sql = "SELECT * FROM(SELECT ROW_NUMBER() OVER ( PARTITION BY AB.GUID,AB.NAME,AB.MOBILE ORDER BY AB.GUID DESC) ROWNUM,* FROM (" +
                "SELECT LIST2GRO.UDG_ID,LIST2GRO.L2G_ID,LIST2GRO.L2G_TYPE,LIST2GRO.GUID,UDGROUP.GP_ATTRIBUTE,UDGROUP.GROUP_TYPE," +
                "UDGROUP.USER_ID,CASE(LIST2GRO.L2G_TYPE) WHEN 0 THEN EMPLOYEE.MOBILE WHEN 1 THEN CLIENT.MOBILE ELSE MALIST.MOBILE END" +
                " AS MOBILE,CASE(LIST2GRO.L2G_TYPE) WHEN 0 THEN EMPLOYEE.NAME WHEN 1 THEN CLIENT.NAME ELSE MALIST.NAME END AS NAME,UDGROUP.UDG_NAME" +
                " FROM LF_LIST2GRO LIST2GRO "+ StaticValue.getWITHNOLOCK() +" LEFT JOIN LF_CLIENT CLIENT "+ StaticValue.getWITHNOLOCK() +"  ON LIST2GRO.GUID=CLIENT.GUID" +
                " LEFT JOIN LF_EMPLOYEE EMPLOYEE "+ StaticValue.getWITHNOLOCK() +"  ON LIST2GRO.GUID=EMPLOYEE.GUID" +
                " LEFT JOIN LF_MALIST MALIST  "+ StaticValue.getWITHNOLOCK() +"  ON LIST2GRO.GUID=MALIST.GUID" +
                " INNER JOIN LF_UDGROUP UDGROUP "+ StaticValue.getWITHNOLOCK() +"  ON LIST2GRO.UDG_ID=UDGROUP.GROUP_ID)AB)ABC " +
                "WHERE ABC.ROWNUM = 1 AND ABC.USER_ID = " + lguserid + " AND ABC.NAME like '%" + name + "%'";*/

//        String sql = "SELECT * FROM ( SELECT LIST2GRO.UDG_ID, LIST2GRO.L2G_ID, LIST2GRO.L2G_TYPE, LIST2GRO.GUID, " +
//                "UDGROUP.GP_ATTRIBUTE , UDGROUP.GROUP_TYPE, UDGROUP.USER_ID , CASE LIST2GRO.L2G_TYPE WHEN 0 THEN " +
//                "EMPLOYEE.MOBILE WHEN 1 THEN CLIENT.MOBILE ELSE MALIST.MOBILE END AS MOBILE , CASE LIST2GRO.L2G_TYPE " +
//                "WHEN 0 THEN EMPLOYEE.NAME WHEN 1 THEN CLIENT.NAME ELSE MALIST.NAME END AS NAME, UDGROUP.UDG_NAME FROM " +
//                "LF_LIST2GRO LIST2GRO LEFT JOIN LF_CLIENT CLIENT ON LIST2GRO.GUID = CLIENT.GUID LEFT JOIN LF_EMPLOYEE " +
//                "EMPLOYEE ON LIST2GRO.GUID = EMPLOYEE.GUID LEFT JOIN LF_MALIST MALIST ON LIST2GRO.GUID = MALIST.GUID " +
//                "INNER JOIN LF_UDGROUP UDGROUP ON LIST2GRO.UDG_ID = UDGROUP.GROUP_ID ) res " +
//                "WHERE res.USER_ID = "+lguserid+" AND res.`NAME` LIKE '%"+name+"%'";

        String sql;
        boolean isMysql = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE;
        if(isMysql) {
            sql = "SELECT * FROM (SELECT * FROM (SELECT LIST2GRO.UDG_ID,LIST2GRO.L2G_ID,LIST2GRO.L2G_TYPE,LIST2GRO.GUID,UDGROUP.GP_ATTRIBUTE,UDGROUP.GROUP_TYPE," +
                    "UDGROUP.USER_ID,CASE(LIST2GRO.L2G_TYPE) WHEN 0 THEN EMPLOYEE.MOBILE WHEN 1 THEN CLIENT.MOBILE ELSE MALIST.MOBILE END " +
                    " AS MOBILE,CASE(LIST2GRO.L2G_TYPE) WHEN 0 THEN EMPLOYEE.NAME WHEN 1 THEN CLIENT.NAME ELSE MALIST.NAME END AS NAME,UDGROUP.UDG_NAME " +
                    " FROM LF_LIST2GRO LIST2GRO LEFT JOIN LF_CLIENT CLIENT ON LIST2GRO.GUID=CLIENT.GUID " +
                    " LEFT JOIN LF_EMPLOYEE EMPLOYEE  ON LIST2GRO.GUID=EMPLOYEE.GUID" +
                    " LEFT JOIN LF_MALIST MALIST ON LIST2GRO.GUID=MALIST.GUID" +
                    " INNER JOIN LF_UDGROUP UDGROUP ON LIST2GRO.UDG_ID=UDGROUP.GROUP_ID " +
                    " AND UDGROUP.RECEIVER=" + lguserid + ")temp ORDER BY temp.GUID DESC)ABC " +
                    " WHERE ABC.NAME like '%" + name + "%' GROUP BY ABC.GUID,ABC.NAME,ABC.MOBILE";
        } else {
            sql = "SELECT ABC.NAME,ABC.UDG_ID,ABC.L2G_ID,ABC.L2G_TYPE,ABC.GUID,ABC.GP_ATTRIBUTE,ABC.GROUP_TYPE,ABC.USER_ID,ABC.UDG_NAME,ABC.MOBILE FROM(SELECT ROW_NUMBER() OVER ( PARTITION BY AB.GUID,AB.NAME,AB.MOBILE ORDER BY AB.GUID DESC) rank,AB.NAME,AB.UDG_ID,AB.L2G_ID,AB.L2G_TYPE," +
                    "AB.GUID,AB.GP_ATTRIBUTE,AB.GROUP_TYPE,AB.USER_ID,AB.UDG_NAME,AB.MOBILE FROM (" +
                    "SELECT LIST2GRO.UDG_ID,LIST2GRO.L2G_ID,LIST2GRO.L2G_TYPE,LIST2GRO.GUID,UDGROUP.GP_ATTRIBUTE,UDGROUP.GROUP_TYPE," +
                    "UDGROUP.USER_ID,CASE(LIST2GRO.L2G_TYPE) WHEN 0 THEN EMPLOYEE.MOBILE WHEN 1 THEN CLIENT.MOBILE ELSE MALIST.MOBILE END" +
                    " AS MOBILE,CASE(LIST2GRO.L2G_TYPE) WHEN 0 THEN EMPLOYEE.NAME WHEN 1 THEN CLIENT.NAME ELSE MALIST.NAME END AS NAME,UDGROUP.UDG_NAME" +
                    " FROM LF_LIST2GRO LIST2GRO "+ StaticValue.getWITHNOLOCK() +" LEFT JOIN LF_CLIENT CLIENT "+ StaticValue.getWITHNOLOCK() +"  ON LIST2GRO.GUID=CLIENT.GUID" +
                    " LEFT JOIN LF_EMPLOYEE EMPLOYEE "+ StaticValue.getWITHNOLOCK() +"  ON LIST2GRO.GUID=EMPLOYEE.GUID" +
                    " LEFT JOIN LF_MALIST MALIST  "+ StaticValue.getWITHNOLOCK() +"  ON LIST2GRO.GUID=MALIST.GUID" +
                    " INNER JOIN LF_UDGROUP UDGROUP "+ StaticValue.getWITHNOLOCK() +"  ON LIST2GRO.UDG_ID=UDGROUP.GROUP_ID AND UDGROUP.RECEIVER=" + lguserid + ")AB)ABC " +
                    "WHERE ABC.rank = 1 AND ABC.NAME like '%" + name + "%'";
        }





//        if (StringUtils.isNotBlank(udgId)){
//            sql = sql + "AND res.UDG_ID = "+udgId;
//        }
        return this.findVoListBySQL(GroupInfoVo.class, sql, StaticValue.EMP_POOLNAME);
    }

    public List<GroupInfoVo> findClientGroupByName(String name, Long lguserid, String udgId) throws Exception{
       /*String sql = "SELECT * FROM (SELECT ROW_NUMBER() OVER ( PARTITION BY AB.GUID,AB.NAME,AB.MOBILE ORDER BY AB.GUID DESC)" +
                " ROWNUM,* FROM (SELECT LIST2GRO.UDG_ID,LIST2GRO.L2G_ID,LIST2GRO.L2G_TYPE, LIST2GRO.GUID,UDGROUP.GP_ATTRIBUTE," +
                "UDGROUP.GROUP_TYPE,UDGROUP.USER_ID,CASE(LIST2GRO.L2G_TYPE) WHEN 1 THEN CLIENT.MOBILE ELSE MALIST.MOBILE END AS " +
                "MOBILE,CASE(LIST2GRO.L2G_TYPE) WHEN 1 THEN CLIENT.NAME ELSE MALIST.NAME END AS NAME,UDGROUP.UDG_NAME FROM LF_LIST2GRO " +
                "LIST2GRO "+ StaticValue.getWITHNOLOCK() +" LEFT JOIN LF_CLIENT CLIENT "+ StaticValue.getWITHNOLOCK() +" ON LIST2GRO.GUID=CLIENT.GUID LEFT JOIN LF_MALIST MALIST  " +
                ""+ StaticValue.getWITHNOLOCK() +"  ON LIST2GRO.GUID=MALIST.GUID INNER JOIN LF_UDGROUP UDGROUP "+ StaticValue.getWITHNOLOCK() +"  ON LIST2GRO.UDG_ID = UDGROUP.GROUP_ID " +
                "and UDGROUP.GP_ATTRIBUTE = 1)AB)ABC WHERE ABC.ROWNUM = 1 AND ABC.USER_ID = "+ lguserid +" AND ABC.NAME like '%"+ name +"%'";*/

        String sql;
        boolean isMysql = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE;
        if(isMysql) {
            sql = "SELECT * FROM (SELECT * FROM (SELECT LIST2GRO.UDG_ID,LIST2GRO.L2G_ID,LIST2GRO.L2G_TYPE, LIST2GRO.GUID,UDGROUP.GP_ATTRIBUTE," +
                    "UDGROUP.GROUP_TYPE,UDGROUP.USER_ID,CASE(LIST2GRO.L2G_TYPE) WHEN 1 THEN CLIENT.MOBILE ELSE MALIST.MOBILE END AS " +
                    "MOBILE,CASE(LIST2GRO.L2G_TYPE) WHEN 1 THEN CLIENT.NAME ELSE MALIST.NAME END AS NAME,UDGROUP.UDG_NAME FROM LF_LIST2GRO " +
                    "LIST2GRO LEFT JOIN LF_CLIENT CLIENT ON LIST2GRO.GUID=CLIENT.GUID LEFT JOIN LF_MALIST MALIST  " +
                    " ON LIST2GRO.GUID=MALIST.GUID INNER JOIN LF_UDGROUP UDGROUP  ON LIST2GRO.UDG_ID = UDGROUP.GROUP_ID " +
                    "and UDGROUP.GP_ATTRIBUTE = 1 AND UDGROUP.RECEIVER=" + lguserid + ")temp ORDER BY temp.GUID DESC)ABC WHERE " +
                    "ABC.NAME like '%"+ name +"%' GROUP BY ABC.GUID,ABC.NAME,ABC.MOBILE";
        } else {
            sql = "SELECT ABC.NAME,ABC.UDG_ID,ABC.L2G_ID,ABC.L2G_TYPE,ABC.GUID,ABC.GP_ATTRIBUTE,ABC.GROUP_TYPE,ABC.USER_ID,ABC.UDG_NAME,ABC.MOBILE FROM (SELECT ROW_NUMBER() OVER ( PARTITION BY AB.GUID,AB.NAME,AB.MOBILE ORDER BY AB.GUID DESC)" +
                    " rank,AB.NAME,AB.UDG_ID,AB.L2G_ID,AB.L2G_TYPE,AB.GUID,AB.GP_ATTRIBUTE,AB.GROUP_TYPE,AB.USER_ID,AB.UDG_NAME,AB.MOBILE FROM (SELECT LIST2GRO.UDG_ID,LIST2GRO.L2G_ID,LIST2GRO.L2G_TYPE, LIST2GRO.GUID,UDGROUP.GP_ATTRIBUTE," +
                    "UDGROUP.GROUP_TYPE,UDGROUP.USER_ID,CASE(LIST2GRO.L2G_TYPE) WHEN 1 THEN CLIENT.MOBILE ELSE MALIST.MOBILE END AS " +
                    "MOBILE,CASE(LIST2GRO.L2G_TYPE) WHEN 1 THEN CLIENT.NAME ELSE MALIST.NAME END AS NAME,UDGROUP.UDG_NAME FROM LF_LIST2GRO " +
                    "LIST2GRO "+ StaticValue.getWITHNOLOCK() +" LEFT JOIN LF_CLIENT CLIENT "+ StaticValue.getWITHNOLOCK() +" ON LIST2GRO.GUID=CLIENT.GUID LEFT JOIN LF_MALIST MALIST  " +
                    ""+ StaticValue.getWITHNOLOCK() +"  ON LIST2GRO.GUID=MALIST.GUID INNER JOIN LF_UDGROUP UDGROUP "+ StaticValue.getWITHNOLOCK() +"  ON LIST2GRO.UDG_ID = UDGROUP.GROUP_ID " +
                    "and UDGROUP.GP_ATTRIBUTE = 1 AND UDGROUP.RECEIVER=" + lguserid + ")AB)ABC WHERE ABC.rank = 1 AND ABC.NAME like '%"+ name +"%'";
        }


//        if (StringUtils.isNotBlank(udgId)){
//            sql = sql + "AND res.UDG_ID = "+udgId;
//        }
        return this.findVoListBySQL(GroupInfoVo.class, sql, StaticValue.EMP_POOLNAME);
    }

    public List<GroupInfoVo> findEmployeeGroupByName(String name, Long lguserid, String udgId) throws Exception{
        /*String sql = "SELECT * FROM (SELECT ROW_NUMBER() OVER ( PARTITION BY AB.GUID,AB.NAME,AB.MOBILE ORDER BY AB.GUID DESC)" +
                " ROWNUM,* FROM (SELECT LIST2GRO.UDG_ID,LIST2GRO.L2G_ID,LIST2GRO.L2G_TYPE, LIST2GRO.GUID,UDGROUP.GP_ATTRIBUTE," +
                "UDGROUP.GROUP_TYPE,UDGROUP.USER_ID,CASE(LIST2GRO.L2G_TYPE) WHEN 0 THEN EMPLOYEE.MOBILE ELSE MALIST.MOBILE END AS " +
                "MOBILE,CASE(LIST2GRO.L2G_TYPE) WHEN 0 THEN EMPLOYEE.NAME ELSE MALIST.NAME END AS NAME,UDGROUP.UDG_NAME FROM LF_LIST2GRO " +
                "LIST2GRO "+ StaticValue.getWITHNOLOCK() +" LEFT JOIN LF_EMPLOYEE EMPLOYEE "+ StaticValue.getWITHNOLOCK() +" ON LIST2GRO.GUID=EMPLOYEE.GUID LEFT JOIN LF_MALIST MALIST  " +
                ""+ StaticValue.getWITHNOLOCK() +"  ON LIST2GRO.GUID=MALIST.GUID INNER JOIN LF_UDGROUP UDGROUP "+ StaticValue.getWITHNOLOCK() +"  ON LIST2GRO.UDG_ID = UDGROUP.GROUP_ID " +
                "and UDGROUP.GP_ATTRIBUTE = 0)AB)ABC WHERE ABC.ROWNUM = 1 AND ABC.USER_ID = "+ lguserid +" AND ABC.NAME like '%"+ name +"%'";*/

        String sql;
        boolean isMysql = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE;
        if(isMysql) {
            sql = "SELECT * FROM (SELECT * FROM (SELECT LIST2GRO.UDG_ID,LIST2GRO.L2G_ID,LIST2GRO.L2G_TYPE, LIST2GRO.GUID,UDGROUP.GP_ATTRIBUTE," +
                    "UDGROUP.GROUP_TYPE,UDGROUP.USER_ID,CASE(LIST2GRO.L2G_TYPE) WHEN 0 THEN EMPLOYEE.MOBILE ELSE MALIST.MOBILE END AS " +
                    "MOBILE,CASE(LIST2GRO.L2G_TYPE) WHEN 0 THEN EMPLOYEE.NAME ELSE MALIST.NAME END AS NAME,UDGROUP.UDG_NAME FROM LF_LIST2GRO " +
                    "LIST2GRO LEFT JOIN LF_EMPLOYEE EMPLOYEE ON LIST2GRO.GUID=EMPLOYEE.GUID LEFT JOIN LF_MALIST MALIST " +
                    " ON LIST2GRO.GUID=MALIST.GUID INNER JOIN LF_UDGROUP UDGROUP   ON LIST2GRO.UDG_ID = UDGROUP.GROUP_ID " +
                    "and UDGROUP.GP_ATTRIBUTE = 0 AND UDGROUP.RECEIVER=" + lguserid +")temp ORDER BY temp.GUID DESC)ABC WHERE "+
                    "ABC.NAME like '%"+ name +"%' GROUP BY ABC.GUID,ABC.NAME,ABC.MOBILE";
        } else {
            sql = "SELECT ABC.NAME,ABC.UDG_ID,ABC.L2G_ID,ABC.L2G_TYPE,ABC.GUID,ABC.GP_ATTRIBUTE,ABC.GROUP_TYPE,ABC.USER_ID,ABC.UDG_NAME,ABC.MOBILE FROM (SELECT ROW_NUMBER() OVER ( PARTITION BY AB.GUID,AB.NAME,AB.MOBILE ORDER BY AB.GUID DESC)" +
                    " rank,AB.NAME,AB.UDG_ID,AB.L2G_ID,AB.L2G_TYPE,AB.GUID,AB.GP_ATTRIBUTE,AB.GROUP_TYPE,AB.USER_ID,AB.UDG_NAME,AB.MOBILE FROM (SELECT LIST2GRO.UDG_ID,LIST2GRO.L2G_ID,LIST2GRO.L2G_TYPE, LIST2GRO.GUID,UDGROUP.GP_ATTRIBUTE," +
                    "UDGROUP.GROUP_TYPE,UDGROUP.USER_ID,CASE(LIST2GRO.L2G_TYPE) WHEN 0 THEN EMPLOYEE.MOBILE ELSE MALIST.MOBILE END AS " +
                    "MOBILE,CASE(LIST2GRO.L2G_TYPE) WHEN 0 THEN EMPLOYEE.NAME ELSE MALIST.NAME END AS NAME,UDGROUP.UDG_NAME FROM LF_LIST2GRO " +
                    "LIST2GRO "+ StaticValue.getWITHNOLOCK() +" LEFT JOIN LF_EMPLOYEE EMPLOYEE "+ StaticValue.getWITHNOLOCK() +" ON LIST2GRO.GUID=EMPLOYEE.GUID LEFT JOIN LF_MALIST MALIST  " +
                    ""+ StaticValue.getWITHNOLOCK() +"  ON LIST2GRO.GUID=MALIST.GUID INNER JOIN LF_UDGROUP UDGROUP "+ StaticValue.getWITHNOLOCK() +"  ON LIST2GRO.UDG_ID = UDGROUP.GROUP_ID " +
                    "and UDGROUP.GP_ATTRIBUTE = 0 AND UDGROUP.RECEIVER=" + lguserid + ")AB)ABC WHERE ABC.rank = 1 AND ABC.NAME like '%"+ name +"%'";
        }


//        if (StringUtils.isNotBlank(udgId)){
//            sql = sql + "AND res.UDG_ID = "+udgId;
//        }
        return this.findVoListBySQL(GroupInfoVo.class, sql, StaticValue.EMP_POOLNAME);
    }

    public Integer findClientsCountByDepId(LfClientDep clientDep, Integer containType) throws Exception {
        StringBuilder sqlBuffer = new StringBuilder(" select COUNT(c.CLIENT_ID) as totalcount from ") ;
        sqlBuffer.append(" (select a.CLIENT_ID  from LF_ClIENT_DEP_SP a where ");
        if(containType == 1){
            sqlBuffer.append(" a.DEP_ID in (select b.DEP_ID from LF_CLIENT_DEP b where b.DEP_PATH like '").append(clientDep.getDeppath()).append("%') ");
        }else if(containType == 2){
            sqlBuffer.append(" a.DEP_ID = ").append(clientDep.getDepId());
        }
        sqlBuffer.append(")c");

        return findCountBySQL(sqlBuffer.toString());
    }

    public List<LfClient> findClientByFieldRef(String corpCode, String fieldRef) throws Exception{
        //sql拼接
        StringBuilder sql = new StringBuilder("select  lfClient.* from ");
        sql.append(TableLfClient.TABLE_NAME)
                .append(" lfClient where lfClient.").append(
                TableLfClient.CORP_CODE).append("='").append(corpCode)
                .append("' and ");
        if (fieldRef != null && !"".equals(fieldRef.trim())) {
            if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
                sql.append(" lfClient.").append(
                        fieldRef).append(" is not null ");
            }else{
                sql.append(" lfClient.").append(
                        fieldRef).append(" !='' ");
            }
        } else {
            return null;
        }
        //返回结果
        return findEntityListBySQL(LfClient.class, sql.toString(),
                StaticValue.EMP_POOLNAME);
    }

    public List<LfCustFieldValueVo> findLfCustFieldValueVo(LfCustFieldValueVo lfCustFieldValueVo) throws Exception{
        //查询字段拼接
        String fieldSql = "select custFieldValue.*,custField." + TableLfCustField.FIELD_REF +
                ",custField." + TableLfCustField.FIELD_NAME +
                ",custField." + TableLfCustField.V_TYPE + ",custField." +
                TableLfCustField.CORP_CODE + ",custField." + TableLfCustField.USERID;
        //查询表名拼接
        String tableSql = " from " + TableLfCustField.TABLE_NAME + " custField inner join " +
                TableLfCustFieldValue.TABLE_NAME + " custFieldValue on custFieldValue." +
                TableLfCustFieldValue.FIELD_ID + " = custField." + TableLfCustField.ID;
        //查询条件拼接
        String conditionSql = getConditionSql(lfCustFieldValueVo);
        conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
        //排序字段 拼接
        String orderBySql = " order by custFieldValue.field_ID asc";
        //sql拼接
        String sql = fieldSql + tableSql + conditionSql + orderBySql;
        //返回结果
        return findVoListBySQL(LfCustFieldValueVo.class, sql, StaticValue.EMP_POOLNAME);
    }

        public static String getConditionSql(LfCustFieldValueVo lfCustFieldValueVo) {
            StringBuilder conditionSql = new StringBuilder();
            // 查询条件----企业编码
            if(lfCustFieldValueVo.getCorp_code() != null && !"".equals(lfCustFieldValueVo.getCorp_code())) {
                conditionSql.append(" and custField.").append(TableLfCustField.CORP_CODE).append("='").append(lfCustFieldValueVo.getCorp_code()).append("'");
            }
            if(lfCustFieldValueVo.getField_ID() != null && !"".equals(lfCustFieldValueVo.getField_ID())) {
                conditionSql.append(" and custField.").append(TableLfCustField.ID).append("=").append(lfCustFieldValueVo.getField_ID());
            }
            if(lfCustFieldValueVo.getField_Value() != null && !"".equals(lfCustFieldValueVo.getField_Value())) {
                conditionSql.append(" and custFieldValue.").append(TableLfCustFieldValue.FIELD_VALUE).append(" like '%").append(lfCustFieldValueVo.getField_Value()).append("%'");
            }
            return conditionSql.toString();
    }

    public List<DynaBean> getsignClientMemberCount(String tcCodes, String corpCode) {
        List<DynaBean> countBeans = new ArrayList<DynaBean>();
        try {
            String sql="";
            sql="select count(ID) membercount,taocan_code taocancode from lf_contract_taocan " +
                    " where  taocan_code in ("+tcCodes+") and corp_code='"+corpCode+"'" +" and is_valid='0' "+
                    " and guid in (select guid from LF_CLIENT where corp_code='"+
                    corpCode+"') group by taocan_code";

            countBeans = getListDynaBeanBySql(sql);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "类型查询群组的成员数量异常!");
        }
        return countBeans;
    }

    public List<DynaBean> findSignClientMember(String searchName, String tcCode, PageInfo pageInfo) {
        StringBuilder sbSql=new StringBuilder();
        sbSql.append("select client.guid guid,client.mobile mobile,client.name name,ctc.contract_id " +
                "contract_id from lf_client client inner join lf_contract_taocan ctc " +
                "on client.guid=ctc.guid where ctc.is_valid='0'");
        if(tcCode != null && !"".equals(tcCode)){
            sbSql.append(" and ctc.taocan_code='").append(tcCode).append("'");
        }
        if(searchName != null && !"".equals(searchName)){
            sbSql.append(" and client.name like '%").append(searchName).append("%'");
        }
        String countSql="select count(*) totalcount from ("+sbSql.toString()+") tmp" ;
        sbSql.append(" order by client.name asc");
        return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sbSql.toString(),
                countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
    }

    public List<DynaBean> getAccountNoByContractIds(String contractIDs) {
        List<DynaBean> contractBeanList =null;
        String accountNoSql="select contract_id,acct_no from lf_contract where contract_id in ("+contractIDs+")";
        try{
            contractBeanList = getListDynaBeanBySql(accountNoSql);
        }catch(Exception e){
            EmpExecutionContext.error(e, "根据签约ID获取签约ID和签约账号的动态Bean失败！");
        }
        return contractBeanList;
    }

    public Integer getClientCountByCusField(String corpCode, LfCustFieldValueVo custFieldValueVo) {
        Integer count;
        try {
            //sql拼接
            StringBuilder sql = new StringBuilder("select count(lfClient." + TableLfClient.CLIENT_ID + ") totalcount from ");
            sql.append(TableLfClient.TABLE_NAME)
                    .append(" lfClient where lfClient.").append(
                    TableLfClient.CORP_CODE).append("='").append(corpCode)
                    .append("'");
            if (custFieldValueVo != null) {
                sql.append(" and (lfClient.").append(
                        custFieldValueVo.getField_Ref()).append(" like '")
                        .append(custFieldValueVo.getId()).append(";%'");
                sql.append(" or lfClient.").append(
                        custFieldValueVo.getField_Ref()).append(" like '%;")
                        .append(custFieldValueVo.getId()).append(";%'");
                sql.append(" or lfClient.").append(
                        custFieldValueVo.getField_Ref()).append(" like '%;")
                        .append(custFieldValueVo.getId()).append("'");
                sql.append(" or lfClient.").append(
                        custFieldValueVo.getField_Ref()).append(" = '").append(
                        custFieldValueVo.getId()).append("')");
            } else {
                return 0;
            }
            count = findCountBySQL(sql.toString());
        }catch (Exception e){
            EmpExecutionContext.error(e,"查询指定属性值下面的用户异常");
            count = 0;
        }
        return count;
    }
    public  List<LfEmployee> getClientOrEmployeeByName(String name, String corpCode, String pageIndex){
        List<LfEmployee> lfEmployees = null;
        try {
            StringBuffer conditionSql = new StringBuffer();
            StringBuffer fieldSql = new StringBuffer("select * from ");
            StringBuffer countSql = new StringBuffer("select count(*) totalcount from ");
            conditionSql.append(TableLfEmployee.TABLE_NAME)
                    .append(" where ")
                    .append(TableLfEmployee.NAME).append(" like '%").append(name).append("%'")
                    .append(" and ")
                    .append(TableLfEmployee.CORP_CODE).append(" = '").append(corpCode).append("'")
                    .append(" order by ").append(TableLfEmployee.GUID).append(" ").append(StaticValue.ASC);
            String sql = String.valueOf(fieldSql.append(conditionSql));
            PageInfo pageInfo = new PageInfo();
            if(StringUtils.isNotBlank(pageIndex)){
                pageInfo.setPageIndex(Integer.valueOf(pageIndex));
            }
            if (StringUtils.isNotBlank(name)){//如果根据名字查询，则给出所有数据
                pageInfo.setPageSize(Integer.MAX_VALUE);
            }
            lfEmployees = genericDAO.findPageEntityListBySQL(LfEmployee.class,String.valueOf(sql) , String.valueOf(countSql.append(conditionSql)), pageInfo, StaticValue.EMP_POOLNAME);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"查询人员信息异常");
        }
        return lfEmployees;
    }
}
