package com.montnets.emp.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.LfBirthdaySetupVo;
import com.montnets.emp.table.birthwish.TableLfBirthdayMember;
import com.montnets.emp.table.birthwish.TableLfBirthdaySetup;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

public class GenericLfBirthdaySetupVoDAO
{
	/**
	 * 查询所有的生日设置
	 * @param loginUserId
	 * @param vo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	
	public List<LfBirthdaySetupVo> findBirthdaySetupVoList(Long loginUserId, LfBirthdaySetupVo vo,PageInfo pageInfo) throws Exception
	{
		//查询字段拼接
		StringBuffer fieldSql = new StringBuffer("select birthdaySetup.*,sysuser.")
					.append(TableLfSysuser.DEP_ID).append(",sysuser.").append(TableLfSysuser.NAME).append(",dep.").append(TableLfDep.DEP_NAME);
		//查询字段拼接
		StringBuffer tableSql = new StringBuffer(" from ")
				.append(TableLfBirthdaySetup.TABLE_NAME).append(" birthdaySetup ").append(StaticValue.getWITHNOLOCK())
				.append(" left join ").append(TableLfSysuser.TABLE_NAME).append(" sysuser ").append(StaticValue.getWITHNOLOCK())
				.append(" on birthdaySetup.").append(TableLfBirthdaySetup.USER_ID).append(" = sysuser.").append(TableLfSysuser.USER_ID)
				.append(" left join ").append(TableLfDep.TABLE_NAME).append(" dep ").append(StaticValue.getWITHNOLOCK())
				.append(" on sysuser.").append(TableLfSysuser.DEP_ID).append(" = dep.").append(TableLfDep.DEP_ID);
		//管辖范围
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(loginUserId);
		//管辖范围拼接
		String dominationSql = new StringBuffer(" where (sysuser.").append(
				TableLfSysuser.USER_ID).append("=").append(loginUserId).append(
				" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(domination).append("))").toString();
		//查询条件拼接
		StringBuffer conditionSql = new StringBuffer();
		//企业编码
		if(vo.getCorpCode() != null && !"".equals(vo.getCorpCode().trim()))
		{
			conditionSql.append(" and birthdaySetup.").append(TableLfBirthdaySetup.CORPCODE).append(" = '").append(vo.getCorpCode()).append("' ");
		}
		//用户名
		if(vo.getUsername()!=null)
		{
			conditionSql.append(" and sysuser.").append(TableLfSysuser.NAME).append(" like '%").append(vo.getUsername()).append("%' ");
		}
		if(vo.getIsUse() != null)
		{
			conditionSql.append(" and birthdaySetup.").append(TableLfBirthdaySetup.IS_USE).append(" = ").append(vo.getIsUse());
		}
		//sp账号
		if(vo.getSpUser()!=null)
		{
			conditionSql.append(" and birthdaySetup.").append(TableLfBirthdaySetup.SP_USER).append(" = '").append(vo.getSpUser()).append("' ");
		}
		if(vo.getType()!=null)
		{
			conditionSql.append(" and birthdaySetup.").append(TableLfBirthdaySetup.TYPE).append(" = ").append(vo.getType());
		}
		//主题
		if(vo.getTitle()!=null)
		{
			conditionSql.append(" and birthdaySetup.").append(TableLfBirthdaySetup.TITLE).append(" like '%").append(vo.getTitle()).append("%' ");
		}
		
		//查询sql
		String sql = new StringBuffer().append(fieldSql).append(tableSql).append(dominationSql).append(conditionSql).append(" ORDER BY birthdaySetup.ID DESC").toString();
		
		//总数查询sql
		String countSql = new StringBuffer("select count(*) totalcount ")
		.append(tableSql).append(dominationSql).append(conditionSql)
		.toString();
		//调用查询方法
		List<LfBirthdaySetupVo> birthdaySetupVos =  new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(LfBirthdaySetupVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		//结果返回
		return birthdaySetupVos;
	}


	/**
	 * 根据机构id删除生日祝福成员
	 * @param depId
	 * @param corpCode
	 * @param type
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public int deleteAddrBirthMemberByDepId(String depId,String corpCode,Integer type,Connection conn) throws Exception
	{
		Integer deleteCount=0;
		StringBuffer sql=new StringBuffer("delete from ").append(TableLfBirthdayMember.TABLE_NAME);
		StringBuffer sql2=new StringBuffer("delete from ").append(TableLfBirthdayMember.TABLE_NAME);
		String tableName = null;
		if(type==1)
		{
			//表名
			tableName = TableLfEmployee.TABLE_NAME;
			/*sql.append(" where ").append(TableLfBirthdayMember.MEMBER_ID)
			.append(" in ( select ").append(TableLfEmployee.GUID).append(" from ").append(TableLfEmployee.TABLE_NAME).append(StaticValue.getWITHNOLOCK()).append(" where DEP_ID=").append(depId).
			append(" and CORP_CODE='").append(corpCode).append("') and TYPE=1 and CORPCODE='").append(corpCode).append("'");*/
		}
		else if(type == 2)
		{
			tableName = TableLfClient.TABLE_NAME;
			/*sql.append("where MEMBER_ID in ( select guId from LF_CLIENT ").append(StaticValue.getWITHNOLOCK()).append(" where DEP_ID=").append(depId).
			append(" and CORP_CODE='").append(corpCode).append("') and TYPE=2 and CORPCODE='").append(corpCode).append("'");*/
		}
		else
		{
			EmpExecutionContext.error("删除生日祝福成员失败，未定义类型");
			return 0;
		}
		
		//sql拼接
		sql.append(" where ").append(TableLfBirthdayMember.MEMBER_ID)
			.append(" in ( select ").append(" GUID ").append(" from ").append(tableName).append(StaticValue.getWITHNOLOCK())
			.append(" where DEP_ID=").append(depId)
			.append(" and CORP_CODE = '").append(corpCode).append("' ) and ")
			.append(TableLfBirthdayMember.TYPE).append(" = ").append(type)
			.append(" and ").append(TableLfBirthdayMember.CORPCODE).append(" = '").append(corpCode).append("' ");
		sql2.append(" where ").append(TableLfBirthdayMember.MEMBER_ID)
			.append(" =").append(depId).append(" and type=").append(type).append(" and ").append(TableLfBirthdayMember.CORPCODE)
			.append("='").append(corpCode).append("'");
		//执行sql语句
		deleteCount = executeBySQLReturnCount(conn, sql.toString());
		executeBySQLReturnCount(conn, sql2.toString());		
		//返回成功数
		return deleteCount;
	}

	public void close(ResultSet rs, PreparedStatement ps) throws Exception
	{
		if (rs != null)
		{
			rs.close();
		}
		if (ps != null)
		{
			ps.close();
		}
	}
	
	
	
	
	/**
	 * sql查询
	 * @param conn
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public boolean executeBySQL(Connection conn, String sql) throws Exception
	{
		boolean b = false;
		PreparedStatement ps = null;
		try
		{
			ps = conn.prepareStatement(sql);
			//执行sql
			int count = ps.executeUpdate();
			if (count > 0)
			{
				b = true;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "执行SQL异常，sql:"+sql);
			throw e;
		} finally
		{
			try
			{
				//关闭数据库资源
				close(null, ps);
			} catch (SQLException e)
			{
				EmpExecutionContext.error("关闭数据库资源异常。");
			}
		}
		return b;
	}
	
	public void close(ResultSet rs, PreparedStatement ps, Connection conn)
	throws Exception
	{
		if (rs != null)
		{
			rs.close();
		}
		if (ps != null)
		{
			ps.close();
		}
		if (conn != null)
		{
			conn.close();
		}
	}
	public Integer executeBySQLReturnCount(Connection conn, String sql) throws Exception
	{
		Integer count = 0;
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			//执行sql
			count = ps.executeUpdate();
		} catch (Exception e) {
			EmpExecutionContext.error(e, "执行SQL异常，sql:"+sql);
			throw e;
		} finally {
			try {
				// 关闭数据库资源
				close(null, ps);
			} catch (SQLException e) {
				EmpExecutionContext.error("关闭数据库资源异常。");
			}
		}
		return count;
	}

}
