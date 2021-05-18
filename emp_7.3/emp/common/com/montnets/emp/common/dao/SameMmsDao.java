package com.montnets.emp.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.group.TableLfMalist;
import com.montnets.emp.table.group.TableLfUdgroup;

public class SameMmsDao extends SuperDAO{
	 /**
     * 处理群组获取手机号码的方法
     *
     * @param groupId
     * @return
     * @throws Exception
     */
    public String getClientGroupUserById(String groupId) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        //List<String> returnList=new ArrayList<String>();
        StringBuilder buffer = new StringBuilder();


        StringBuffer sqlStr = new StringBuffer("select  list2gro.").append(TableLfList2gro.L2G_TYPE).append(",case(list2gro.").append(
                TableLfList2gro.L2G_TYPE).append(") when 0 then employee.").append(TableLfEmployee.MOBILE).append(" when 1 then client.")
                .append(TableLfClient.MOBILE).append("  else malist.")
                .append(TableLfMalist.MOBILE).append(" end as MOBILE ");
        //拼凑TABLE
        sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro " ).append(StaticValue.getWITHNOLOCK()).append(" left join ")
                .append(TableLfClient.TABLE_NAME).append(" client  ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
                .append("=client.").append(TableLfClient.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(
                " malist " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
                .append("=malist.").append(TableLfMalist.GUID).append(" left join ").append(TableLfEmployee.TABLE_NAME).append(
                " employee " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
                .append("=employee.").append(TableLfEmployee.GUID)
                .append(" inner join ").append(TableLfUdgroup.TABLE_NAME)
                .append(" udgroup " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID)
                .append("=udgroup.").append(TableLfUdgroup.UDG_ID).append(" where udgroup.")
                .append(TableLfUdgroup.UDG_ID).append("=").append(groupId).append( " order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc");

        try {
            //获取数据库连接
            conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
            EmpExecutionContext.sql("execute sql : " + sqlStr.toString());
            ps = conn.prepareStatement(sqlStr.toString());
            //执行SQL
            rs = ps.executeQuery();
            while (rs.next()) {
                //获取手机号码
                //returnList.add(rs.getString(2));
                buffer.append(rs.getString(2)).append(",");
            }
        } catch (SQLException e) {
            EmpExecutionContext.error(e,"获取群组手机号码失败！");
            throw e;
        }finally {
            //关闭数据库资源
            try {
                close(rs, ps, conn);
            } catch (SQLException e) {
                EmpExecutionContext.error(e,"关闭数据库资源出错！");
            }
        }
        return buffer.toString();
    }
}
