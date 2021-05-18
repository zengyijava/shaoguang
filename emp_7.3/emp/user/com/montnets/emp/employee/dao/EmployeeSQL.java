package com.montnets.emp.employee.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.util.StringUtils;

public class EmployeeSQL extends SuperDAO{
	
	public String executeProcessReutrnCursorOfOracle( String POOLNAME,
			String processStr,Integer[] params)
			throws Exception
	{
		Connection conn = null;
		ResultSet rs = null;
		CallableStatement proc = null;
		StringBuffer deps = new StringBuffer();
		String depIds = "";
		try
		{
			
			conn = connectionManager.getDBConnection(POOLNAME);
			proc = conn.prepareCall("{ call "+processStr+"(?,?,?,?) }");
			proc.setInt(1, params[0]);
			proc.setInt(2, params[1]);
			proc.setString(3, StringUtils.getRandom());
			proc.registerOutParameter(4,oracle.jdbc.OracleTypes.CURSOR);
			proc.execute();
			rs = (ResultSet)proc.getObject(4);
			while (rs.next())
			{
				deps.append(rs.getLong("DepID")).append(",");
			}
			if(deps.length() > 0)
				depIds = deps.substring(0, deps.lastIndexOf(","));
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行存储过程失败！");
			throw e;
		} finally
		{
			
			try
			{
				close(rs, proc, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
		return depIds;
	}
	
	
	public StringBuffer getInConditionSql(StringBuffer conditionSql,String depIds)
	{
		/*******机构in查询处理***********/
		//int maxCount = StaticValue.inConditionMax;//in查询最大个数限制
		int maxCount = StaticValue.getInConditionMax();//in查询最大个数限制
		String[] depIdArry = depIds.split(",");
		int size = depIdArry.length;
		if(size>maxCount)
		{
			conditionSql.append(" and (lfemployeedep.").append(
					TableLfEmployee.DEP_ID).append(" in (");
			
			String inString = "";
			for(int i=0;i<size;i++)
			{
				inString +=  depIdArry[i] ;
				if( i > maxCount-2 && (i+1)%maxCount==0  && i<size - 1  )
				{
					conditionSql.append(inString).append(") or ").append(" lfemployeedep.").append(
							TableLfEmployee.DEP_ID).append(" in (");
					inString = "";
				}else if( i<size - 1 )
				{
					inString += ",";
				}
			}
			conditionSql.append(inString).append("))");
		}else
		{
		
			conditionSql.append(" and lfemployeedep.").append(
					TableLfEmployee.DEP_ID).append(" in (").append(depIds).append(")");
		}
		/*******END-机构in查询处理**********/
		
		return conditionSql;
	}
	
	
	public String getEmpChildIdByDepId( String depId) throws Exception
	{
		String depIds = depId ;
		String conditionDepid = depId;
		String sql = "";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int incount = 0;
		//int maxIncount = StaticValue.inConditionMax;
		int maxIncount = StaticValue.getInConditionMax();
		int n = 1;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			boolean hasNext = true;
			while(hasNext)
			{	
				incount = 0;
				sql = "select dep_Id from lf_employee_dep where PARENT_ID in ( "+conditionDepid+")";
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				conditionDepid = "";
				while(rs.next())
				{
					incount ++;
					depIds += "," + rs.getString("dep_id");
					if(incount > maxIncount*n)
					{
						n++;
						conditionDepid = conditionDepid.substring(0,conditionDepid.length()-1);
						conditionDepid += ") or PARENT_ID in (";
					}
					conditionDepid += rs.getString("dep_id") + ",";
				}
				if(conditionDepid.length() == 0)
				{
					hasNext = false;
				}else
				{
					hasNext = true;
					conditionDepid = conditionDepid.substring(0,conditionDepid.length()-1);
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "员工查询失败！");
			
		} finally
		{

			close(rs, ps, conn);
		}
		return depIds;	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


}
