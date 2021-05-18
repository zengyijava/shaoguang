package com.montnets.emp.clientsms.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.common.vo.LfCustFieldValueVo;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.client.LfCustFieldValue;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.table.client.TableLfCliDepConn;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.client.TableLfClientDep;
import com.montnets.emp.table.employee.TableLfEmpDepConn;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.group.TableLfMalist;
import com.montnets.emp.table.group.TableLfUdgroup;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;
import org.apache.commons.beanutils.DynaBean;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 
 * @description
 */
public class LfClientSmsVoDAO extends SuperDAO
{
	public List<LfCustFieldValueVo> findLfCustFieldValueVo(LfCustFieldValueVo lfCustFieldValueVo) throws Exception
	{
		//查询字段拼接
		String fieldSql = LfClientSmsVoSQL.getFieldSql();
		//查询表名拼接
		String tableSql = LfClientSmsVoSQL.getTableSql();
		//查询条件拼接
		String conditionSql = LfClientSmsVoSQL.getConditionSql(lfCustFieldValueVo);
		conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		//排序字段 拼接
		String orderBySql = LfClientSmsVoSQL.getOrderBySql();
		//sql拼接
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderBySql).toString();
		
		List<LfCustFieldValueVo> returnVoList = findVoListBySQL(
						LfCustFieldValueVo.class, sql,StaticValue.EMP_POOLNAME);
		//返回结果
		return returnVoList;
	}
	
	/**
	 * 查询客户
	 */
	public List<LfClient> findClientByCusField(String corpCode,
			List<LfCustFieldValueVo> custFieldValueVo) throws Exception {
		//sql拼接
		StringBuffer sql = new StringBuffer("select lfClient.* from ");
		sql.append(TableLfClient.TABLE_NAME)
				.append(" lfClient where lfClient.").append(
						TableLfClient.CORP_CODE).append("='").append(corpCode)
				.append("'");
		if (custFieldValueVo != null && !custFieldValueVo.isEmpty()) {
			for (LfCustFieldValueVo custfieldvalue : custFieldValueVo) {
				sql.append(" and (lfClient.").append(
						custfieldvalue.getField_Ref()).append(" like '")
						.append(custfieldvalue.getId()).append(";%'");
				sql.append(" or lfClient.").append(
						custfieldvalue.getField_Ref()).append(" like '%;")
						.append(custfieldvalue.getId()).append(";%'");
				sql.append(" or lfClient.").append(
						custfieldvalue.getField_Ref()).append(" like '%;")
						.append(custfieldvalue.getId()).append("'");
				sql.append(" or lfClient.").append(
						custfieldvalue.getField_Ref()).append(" = '").append(
						custfieldvalue.getId()).append("')");
			}
		} else {
			return null;
		}
		//返回结果
		return findEntityListBySQL(LfClient.class, sql.toString(),
				StaticValue.EMP_POOLNAME);
	}
	
	/**
	 *根据客户属性值查询客户
	 */
	public List<LfClient> findClientByCusFieldValue(String corpCode,
			LfCustFieldValueVo custfieldvalue) throws Exception {
		//sql拼接
		StringBuffer sql = new StringBuffer("select lfClient.* from ");
		sql.append(TableLfClient.TABLE_NAME)
				.append(" lfClient where lfClient.").append(
						TableLfClient.CORP_CODE).append("='").append(corpCode)
				.append("' and ");
		if (custfieldvalue != null) {
				sql.append(" (lfClient.").append(
						custfieldvalue.getField_Ref()).append(" like '")
						.append(custfieldvalue.getId()).append(";%'");
				sql.append(" or lfClient.").append(
						custfieldvalue.getField_Ref()).append(" like '%;")
						.append(custfieldvalue.getId()).append(";%'");
				sql.append(" or lfClient.").append(
						custfieldvalue.getField_Ref()).append(" like '%;")
						.append(custfieldvalue.getId()).append("'");
				sql.append(" or lfClient.").append(
						custfieldvalue.getField_Ref()).append(" = '").append(
						custfieldvalue.getId()).append("')");
		} else {
			return null;
		}
		//EmpExecutionContext.info("[客户群组群发]sql："+sql.toString());
		//返回结果
		return findEntityListBySQL(LfClient.class, sql.toString(),
				StaticValue.EMP_POOLNAME);
	}
	
	//写一个不需要分页的获取员工机构下面所有子机构的方法
	public List<LfClientDep> findClientDepsByDeppath(String corpCode,String deppath) throws Exception {
		String sql = new StringBuffer("select lfclientdep.* from ").append(TableLfClientDep.TABLE_NAME)
		.append(" lfclientdep where lfclientdep.").append(TableLfClientDep.DEP_PATH).append(" like '")
		.append(deppath).append("%' and lfclientdep.").append(TableLfClientDep.CORP_CODE)
		.append("='").append(corpCode).append("'").toString();
		List<LfClientDep> returnVoList = findEntityListBySQL(LfClientDep.class, sql, StaticValue.EMP_POOLNAME);
		return returnVoList;
	}

	/**
	 * 通过群组ID及查询条件获取客户信息
	 * 
	 * @param udgId
	 *            群组Id
	 * @param loginId
	 *            操作员登录Id
	 * @param conditionMap
	 *            查询条件
	 * @param orderbyMap
	 *            排序条件
	 * @param pageInfo
	 *            分页信息，无需分析时传入null
	 * @return 员工信息的集合
	 * @throws Exception
	 */
	public List<LfClient> getClientByGuid(String udgId, String loginId,
			LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		List<LfClient> clients = null;
		String sql = "";
		//第一次进入选择员工页面时群组id为空，就根据loginId查询。点击群组之后udgId不会空
		if (udgId != null && !"".equals(udgId))
		{
			sql = "select * from " + TableLfClient.TABLE_NAME + "  where "
					+ TableLfClient.GUID + "  in (select "
					+ TableLfList2gro.GUID + " from "
					+ TableLfList2gro.TABLE_NAME + "  where "
					+ TableLfList2gro.UDG_ID + " in (" + udgId + ") and "
					+ TableLfList2gro.L2G_TYPE + "=1)";
		}else if (loginId != null && !"".equals(loginId))
		{
			sql = "select * from " + TableLfClient.TABLE_NAME + "  where "
					+ TableLfClient.GUID + "  in (select "
					+ TableLfList2gro.GUID + " from "
					+ TableLfList2gro.TABLE_NAME + "  where "
					+ TableLfList2gro.UDG_ID + " in(select  "
					+ TableLfUdgroup.UDG_ID + " from "
					+ TableLfUdgroup.TABLE_NAME + " where "
					+ TableLfUdgroup.USER_ID + "=" + loginId + "))";
		}

		Iterator<Map.Entry<String, String>> iter = null;
		Map<String, String> columns = getORMMap(LfClient.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			Field[] fields = LfSpDepBind.class.getDeclaredFields();
			String fieldType = null;
			while (iter.hasNext())
			{
				e = iter.next();
				for (int index = 0; index < fields.length; index++)
				{
					if (fields[index].getName().equals(e.getKey()))
					{
						fieldType = fields[index].getGenericType().toString();
						break;
					}
				}
				if (!"".equals(e.getValue()))
				{
					String eKey = e.getKey();
					columnName = eKey.indexOf("&") < 0 ? columns.get(eKey)
							: columns.get(eKey
									.subSequence(0, eKey.indexOf("&")));
					if (eKey.contains("&like"))
					{
						sql = sql + " and " + columnName + " like '%"+ e.getValue() + "%' ";
					} else if (eKey.contains("&>"))
					{
						sql = sql + " and " + columnName + ">" + e.getValue()+ " ";
					} else
					{
						boolean isDateType = fieldType
								.equals(StaticValue.TIMESTAMP)
								|| fieldType.equals(StaticValue.DATE_SQL)
								|| fieldType.equals(StaticValue.DATE_UTIL);
						if (fieldType.equals("class java.lang.String")
								|| isDateType)
						{
							sql = sql + " and " + columnName + "='"+ e.getValue() + "' ";
						} else
						{
							sql = sql + " and " + columnName + "="+ e.getValue();
						}
					}

				}
			}
		}

		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			sql += " order by ";
			iter = orderbyMap.entrySet().iterator();
			while (iter.hasNext())
			{
				e = iter.next();
				sql = sql + columns.get(e.getKey()) + " " + e.getValue() + ",";
			}

			sql = sql.substring(0, sql.lastIndexOf(","));
		}

		if (pageInfo == null)
		{
			clients= findEntityListBySQL(LfClient.class, sql,
					StaticValue.EMP_POOLNAME);
			return clients;
		} else
		{
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql)
					.append(")").toString();
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(LfClient.class,
					sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	/**
	 * 根据机构id,企业编码查询客户信息
	 */
	public List<LfClient> findLfClient(String depId, String corpCode)
			throws Exception {
		// sql拼接
		StringBuffer sql = new StringBuffer("select ").append(
				TableLfClient.MOBILE).append(" from ").append(
				TableLfClient.TABLE_NAME).append(" where ").append(
				TableLfClient.CORP_CODE).append("='").append(corpCode.trim())
				.append("' ");
		if (null != depId && !"".equals(depId)) {
			String depIds = this.getClientChildByParentID(Long.valueOf(depId),
					TableLfClient.DEP_ID);
			sql.append(" and(").append(depIds).append(")");
		}
		List<LfClient> returnList = findPartEntityListBySQL(LfClient.class, sql
				.toString(), StaticValue.EMP_POOLNAME);
		// 返回查询结果
		return returnList;
	}
	
	/**
	 * 获取客户子机构id字符串，包含父机构id
	 * @param depId 父机构机构id
	 * @param refName 字段名
	 * @return 非sqlserver数据库格式为：refName in (id,id,id) or refName in (id,id,id)，sqlserver返回refName in (select DepID from GetCliDepChildByPID(1,depId)
	 * @throws Exception
	 */
	public String getClientChildByParentID(Long depId, String refName)
			throws Exception {

		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE) {
			// oracle
			return this.getChildByParentIDOracle(depId, refName);
		} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE) {
			// sqlserver
			String sql = new StringBuffer(refName).append(" in ( ").append(
					"select DepID from GetCliDepChildByPID(1,").append(depId)
					.append(") ) ").toString();// 获取子机构，包含自己
			// 返回sql
			return sql;
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE) {
			// mysql
			return this.getCliChildIdForMysql(depId.toString(), refName);
		} else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE) {
			// db2
			return this.getCliChildIdForMysql(depId.toString(), refName);
		}
		// 返回空
		return null;
	}
	
	/**
	 * 根据depId获取子机构Id，包含自己（Oracle）
	 * @param depId 机构Id
	 * @return 机构Id，格式为id1,id2,id3
	 * @throws Exception
	 */
	public String getChildByParentIDOracle(Long depId, String refName) throws Exception {
		//数据库连接
		Connection conn = null;
		//结果集
		ResultSet rs = null;
		String depIds = "";
		CallableStatement proc = null;
		
		try 
		{
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//调用存储过程
			proc = conn.prepareCall("{ call GETCLIDEPCHILDBYPID(?,?,?,?) }");
			//设置参数
			proc.setInt(1, 1);
			proc.setLong(2, depId);
			proc.setString(3,StringUtils.getRandom());
			//获取返回
			proc.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
			//执行
			proc.execute();
			//返回结果集
			rs = (ResultSet) proc.getObject(4);
			//处理结果集
			depIds = parseRS(rs, refName);
		} 
		catch (Exception e) 
		{
			//异常处理
			EmpExecutionContext.error(e,"客户群组群发获取子机构调用存储过程出错！");
			throw e;
		} 
		finally 
		{
			try 
			{
				close(rs, proc, conn);
			} 
			catch (SQLException e) 
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		//返回结果
		return depIds;
	}
	
	/**
	 * 根据depId获取子机构Id，包含自己（mysql）（暂不用）
	 * @param depId 机构Id
	 * @return 机构Id，格式为id1,id2,id3
	 * @throws Exception
	 */
	public String getChildByParentIDMySql(Long depId, String refName) throws Exception{
		//调用存储过程sql
		String sql  = "call GETCLIDEPCHILDBYPID(?,?,?)";
		//获取连接
		Connection conn = null;
		//结果集
		ResultSet rs = null;
		String depIds = "";
		CallableStatement comm = null;
		
		try
		{
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			comm = ((java.sql.Connection) conn).prepareCall(sql);
			//设置参数
			comm.setInt(1,1);
			comm.setLong(2,depId);
			comm.setString(3,StringUtils.getRandom());
			//执行
			comm.execute();
			//获取结果集
			rs = comm.getResultSet();
			//处理结果集
			depIds = parseRS(rs, refName);
			
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"客户群组群发获取子机构调用存储过程出错！");
			throw e;
		} finally
		{
			try
			{
				close(rs, comm, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		//返回结果
		return depIds;
	}
	
	/**
	 * 获取子机构id（包含自己）（mysql专用）
	 * @param depId 父机构id
	 * @return 返回格式为:id,id,id
	 * @throws Exception
	 */
	public String getCliChildIdForMysql(String depId, String refName) throws Exception
	{
		StringBuffer depIds = new StringBuffer();
		String conditionDepid = depId;
		//查询sql
		String sql = "";
		//数据库连接
		Connection conn = null;
		PreparedStatement ps = null;
		//结果集
		ResultSet rs = null;
		//记录数
		int incount = 0;
		int n = 1;
		
		try
		{
			//获取连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//结果
			boolean hasNext = true;
			//加入父id
			depIds.append(refName).append(" in ( ").append(depId);
			//循环处理
			while(hasNext)
			{
				//查询sql
				sql = "select dep_Id from lf_client_dep where PARENT_ID in ( "+conditionDepid+")";
				//预处理sql
				ps = conn.prepareStatement(sql);
				//执行并返回结果集
				rs = ps.executeQuery();
				conditionDepid = "";
				//循环处理结果集
				while(rs.next())
				{
					//记录数量
					incount ++;

					//if(incount > StaticValue.inConditionMax*n)
					if(incount > StaticValue.getInConditionMax()*n)
					{
						n++;
						depIds.append(") or ").append(refName).append(" in (")
								.append(rs.getString("dep_id"));
						
						conditionDepid = conditionDepid.substring(0,conditionDepid.length()-1);
						conditionDepid += ") or PARENT_ID in (";
					}
					else
					{
						depIds.append(",").append(rs.getString("dep_id"));
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
			depIds.append(" ) ");
			
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"客户群组群发获取子机构ID出错！");
			throw e;
			
		} finally
		{
			try{
				close(rs, ps, conn);
			}catch(Exception e){
				EmpExecutionContext.error(e,"关闭资源异常");
			}

		}
		//返回结果
		return depIds.toString();	
	}
	/**
	 * 解析结果集
	 * @param rs
	 * @param refName
	 * @return
	 * @throws Exception
	 */
	private String parseRS(ResultSet rs, String refName) throws Exception 
	{
		StringBuffer deps = new StringBuffer();
		//记录计数
		int i = 1;
		int cou=1;
		//结果集有值
		if(rs.next())
		{
			deps.append(refName).append(" in ( ").append(rs.getLong("DepID"));
		}
		else
		{
			return "";
		}
		
		while (rs.next()) {
			i++;
			//if(i > cou*StaticValue.inConditionMax)
			if(i > cou*StaticValue.getInConditionMax())
			{
				//记录数操作数量，则拆分为or xx in(id,id,id...)格式
				cou++;
				deps.append(") or ").append(refName).append(" in (").append(rs.getLong("DepID"));
			}
			else
			{
				deps.append(",").append(rs.getLong("DepID"));
			}
		}
		
		deps.append(" ) ");
		//返回结果
		return deps.toString();
	}
	
	/**
	 * 查询客户
	 */
	public Long getClientCountByCusField(String corpCode,
			LfCustFieldValueVo  custFieldValueVo) throws Exception {
		//sql拼接
		StringBuffer sql = new StringBuffer("select count(lfClient."+TableLfClient.CLIENT_ID+") from ");
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
			return 0L;
		}
		Long count = 0L;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql.toString());
			//执行sql
			rs = ps.executeQuery();
			if(rs.next()){
				count = rs.getLong(1);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询客户失败！");
			throw e;
		} finally
		{
			try
			{
				//关闭数据库资源
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源失败！");
			}
		}
		//返回结果
		return count;
	}
	
	
	
	/**
	 * 获取客户机构列表——用于构建机构树
	 * 
	 * @param userId
	 *            当前操作员的ID
	 * @param depId
	 *            传NULL进来则通过userId去查，有传值时只查传入机构的子级
	 * @return 员工机构的集合
	 * @throws Exception
	 */
	public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId,
			String depId) throws Exception
	{
		String sql = "";
		if (depId == null || "".equals(depId))
		{
			sql = new StringBuffer(" select e.* from ").append(
					TableLfClientDep.TABLE_NAME).append(
					" e " + StaticValue.getWITHNOLOCK() + ",").append(
					TableLfCliDepConn.TABLE_NAME).append(
					" c " + StaticValue.getWITHNOLOCK()).append(" where c.").append(
					TableLfCliDepConn.USER_ID).append(" = ").append(userId)
					.append(" and (c.").append(TableLfEmpDepConn.DEP_ID)
					.append(" =e.").append(TableLfClientDep.DEP_ID).append(
							" or ").append(TableLfClientDep.PARENT_ID).append(
							" = c.").append(TableLfCliDepConn.DEP_ID).append(
							")").toString();
		} else
		{
			sql = new StringBuffer(" select * from ").append(
					TableLfClientDep.TABLE_NAME).append(
					" " + StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfClientDep.PARENT_ID).append(" = ").append(depId)
					.toString();
		}
		List<LfClientDep> lfClientDepList = findEntityListBySQL(
				LfClientDep.class, sql, StaticValue.EMP_POOLNAME);
		return lfClientDepList;
	}

	/**
	 * 通过群组ID及查询条件获取客户信息
	 * @param conditionMap
	 *            查询条件
	 * @param orderbyMap
	 *            排序条件
	 * @param pageInfo
	 *            分页信息，无需分析时传入null
	 * @return 员工信息的集合
	 * @throws Exception
	 */
	public List<DynaBean> findAllClientByUserAttrsOrCusField(String corpCode,LinkedHashMap<String, String> conditionMap,
			List<LfCustFieldValueVo> custFieldValueList,LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
        //拼SQL语句
        String fieldSql = "SELECT lfClient.*,lf_dep.DEP_NAME ";
        String tmpSql = "lfClient.FIELD01";
        for(int i = 2;i<51;i++){
            if(i<10){
                tmpSql = tmpSql + "+','+lfClient.FIELD0"+i;
            }else {
                tmpSql = tmpSql + "+','+lfClient.FIELD"+i;
            }
        }
        fieldSql = fieldSql + ","+  "(" + tmpSql + ")" + " vids " ;

        StringBuffer tableSql = new StringBuffer();
        tableSql.append("FROM " + TableLfClient.TABLE_NAME + " lfClient ");
        tableSql.append("LEFT JOIN " + TableLfClientDep.TABLE_NAME + " lf_dep ").append(" ON lfClient.DEP_ID = lf_dep.DEP_ID ");

        StringBuffer conSql = new StringBuffer();
        conSql.append("  where lfClient.").append(TableLfClient.CORP_CODE).append("='").append(corpCode).append("'");

		//客户基本信息查询条件SQL
        if (conditionMap != null && !conditionMap.entrySet().isEmpty())
        {
            Iterator<Map.Entry<String, String>> iter = null;
            iter = conditionMap.entrySet().iterator();
            Map<String, String> columns = getORMMap(LfClient.class); //等到结果形如:{phone=PHONE, clientCode=CLIENT_CODE, ...}
            Field[] fields = LfClient.class.getDeclaredFields();//[private static final long com.montnets.emp.entity.client.LfClient.serialVersionUID, private java.lang.Long com.montnets.emp.entity.client.LfClient.clientId,...]
            Map.Entry<String, String> e = null;
            
            String sqlTemp = "";
            String columnName = null;
            String fieldType = null;

            while (iter.hasNext())
            {
                e = iter.next();
                
                for (int index = 0; index < fields.length; index++)
                {
                    if (fields[index].getName().equals(e.getKey()))
                    {
                        fieldType = fields[index].getGenericType().toString();
                        break;
                    }
                }
                if (!"".equals(e.getValue()))
                {
                    String eKey = e.getKey();
                    columnName = eKey.indexOf("&") < 0 ? columns.get(eKey) : columns.get(eKey.subSequence(0, eKey.indexOf("&")));
                    if (eKey.contains("&like"))
                    {
                        sqlTemp =  " and lfClient." + columnName + " like '%"+ e.getValue() + "%' ";
                        conSql = conSql.append(sqlTemp);
                    } else if (eKey.contains("&>"))
                    {
                        sqlTemp = " and lfClient." + columnName + "> '" + e.getValue()+ "' ";
                        conSql = conSql.append(sqlTemp);
                    }else if(eKey.contains("&<")){
                        sqlTemp = " and lfClient." + columnName + "< '" + e.getValue()+ "' ";
                        conSql = conSql.append(sqlTemp);
                    }else
                    {
                        boolean isDateType = fieldType
                                .equals(StaticValue.TIMESTAMP)
                                || fieldType.equals(StaticValue.DATE_SQL)
                                || fieldType.equals(StaticValue.DATE_UTIL);
                        if (fieldType.equals("class java.lang.String")
                                || isDateType)
                        {
                            sqlTemp =  " and lfClient." + columnName + "='"+ e.getValue() + "' " ;
                            conSql = conSql.append(sqlTemp);
                        } else
                        {
                            sqlTemp =  " and lfClient." + columnName + "="+ e.getValue();
                            conSql = conSql.append(sqlTemp);
                        }
                    }
                }
            }
        }

		//客户属性查询条件SQL
		if (custFieldValueList != null) {
				for(LfCustFieldValueVo custfieldvalue : custFieldValueList){
                    conSql.append(" and (lfClient.").append(
							custfieldvalue.getField_Ref()).append(" like '")
							.append(custfieldvalue.getId()).append(";%'");
                    conSql.append(" or lfClient.").append(
							custfieldvalue.getField_Ref()).append(" like '%;")
							.append(custfieldvalue.getId()).append(";%'");
                    conSql.append(" or lfClient.").append(
							custfieldvalue.getField_Ref()).append(" like '%;")
							.append(custfieldvalue.getId()).append("'");
                    conSql.append(" or lfClient.").append(
							custfieldvalue.getField_Ref()).append(" = '").append(
							custfieldvalue.getId()).append("')");
				}
		} else {
			return null;
		}
        String orderbySql = " order by lfClient.NAME DESC";
		//返回结果
        String sql = fieldSql  + tableSql +  conSql.toString() + orderbySql;
        String countSql = "select count(*) totalcount " + tableSql + conSql.toString();
        return  new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
	}

	public List<LfCustFieldValue> findClientCustFieldValueByIds(String ids) throws Exception{
		String fieldSql = "SELECT LF_CUSTFIELD_VALUE.* ";
        String tableSql = "FROM LF_CUSTFIELD_VALUE ";
        String conSql = "";
        if(ids !=null&&"".equals(ids)){
        	 conSql = " where 1 <> 1";	
        }else {
        	 conSql = " where LF_CUSTFIELD_VALUE.ID in " + "(" + ids + ")";
        }
        
        String sql =  fieldSql +   tableSql +   conSql;
        List<LfCustFieldValue> lfClientCustFieldValueList = findEntityListBySQL(LfCustFieldValue.class, sql, StaticValue.EMP_POOLNAME);
        return lfClientCustFieldValueList;
	}
	
	
	/**
	 *  查询出客户群组的成员    个人 / 共享
	 * @param groupId
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> findGroupClientByIds(Long groupId,String epname,PageInfo pageInfo) throws Exception{
		//初始化LIST
		List<GroupInfoVo> returnVoList = null;
		//拼凑查询字段
		StringBuffer sqlStr = new StringBuffer("select  list2gro.").append(TableLfList2gro.UDG_ID).append(",list2gro.").append(
				TableLfList2gro.L2G_ID).append(",list2gro.").append(TableLfList2gro.L2G_TYPE).append(",list2gro.").append(
				TableLfList2gro.GUID).append(",udgroup.").append(TableLfUdgroup.GP_ATTRIBUTE).append(",udgroup.").append(
				TableLfUdgroup.GROUP_TYPE).append(",udgroup.").append(TableLfUdgroup.USER_ID).append(",case(list2gro.").append(
				TableLfList2gro.L2G_TYPE).append(") when 1 then client.").append(TableLfClient.MOBILE).append("  else malist.")
				.append(TableLfMalist.MOBILE).append(" end as MOBILE,case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(
						") when 1 then client.").append(TableLfClient.NAME).append("  else malist.").append(TableLfMalist.NAME).append(
						" end as NAME,udgroup.").append(TableLfUdgroup.UDG_NAME);
		//拼凑TABLE
		sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro " ).append(StaticValue.getWITHNOLOCK()).append(" left join ")
				.append(TableLfClient.TABLE_NAME).append(" client  ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=client.").append(TableLfClient.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(
						" malist " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=malist.").append(TableLfMalist.GUID).append(" inner join ").append(TableLfUdgroup.TABLE_NAME)
				.append(" udgroup " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID)
				.append("=udgroup.").append(TableLfUdgroup.UDG_ID).append(" where udgroup.")
				.append(TableLfUdgroup.UDG_ID).append("=").append(groupId);
		
		if(epname != null && !"".equals(epname)){
			sqlStr.append(" and (case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(") when 1 then client.")
			.append(TableLfClient.NAME).append("  else malist.").append(TableLfMalist.NAME).append(" end )")
			.append(" like '%").append(epname).append("%'");
		}
		
		//查询
		String countSql = "select count(*) totalcount from ( " + sqlStr.toString() + " ) a";
		sqlStr.append( " order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc ");

		returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(GroupInfoVo.class, sqlStr.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		//返回
		return returnVoList;
	}
	
	/**
	 *  查询出客户群组的成员    个人 / 共享
	 * @param groupId
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> findGroupClientByIds(Long groupId,String epname,PageInfo pageInfo,String corpCode) throws Exception{
		//初始化LIST
		List<GroupInfoVo> returnVoList = null;
		//拼凑查询字段
		StringBuffer sqlStr = new StringBuffer("select  list2gro.").append(TableLfList2gro.UDG_ID).append(",list2gro.").append(
				TableLfList2gro.L2G_ID).append(",list2gro.").append(TableLfList2gro.L2G_TYPE).append(",list2gro.").append(
				TableLfList2gro.GUID).append(",udgroup.").append(TableLfUdgroup.GP_ATTRIBUTE).append(",udgroup.").append(
				TableLfUdgroup.GROUP_TYPE).append(",udgroup.").append(TableLfUdgroup.USER_ID).append(",case(list2gro.").append(
				TableLfList2gro.L2G_TYPE).append(") when 1 then client.").append(TableLfClient.MOBILE).append("  else malist.")
				.append(TableLfMalist.MOBILE).append(" end as MOBILE,case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(
						") when 1 then client.").append(TableLfClient.NAME).append("  else malist.").append(TableLfMalist.NAME).append(
						" end as NAME,udgroup.").append(TableLfUdgroup.UDG_NAME);
		//拼凑TABLE
		sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro " ).append(StaticValue.getWITHNOLOCK()).append(" left join ")
				.append(TableLfClient.TABLE_NAME).append(" client  ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=client.").append(TableLfClient.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(
						" malist " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=malist.").append(TableLfMalist.GUID).append(" inner join ").append(TableLfUdgroup.TABLE_NAME)
				.append(" udgroup " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID)
				.append("=udgroup.").append(TableLfUdgroup.UDG_ID).append(" where udgroup.")
				.append(TableLfUdgroup.UDG_ID).append("=").append(groupId).append(" and ");
		
		//增加企业编码,将and连接符放在上一个语句中，这样万一没拼接上企业编码执行SQL语句就报错，从而不出问题
		sqlStr.append(" (case(list2gro.L2G_TYPE) when 1 then client.corp_code else malist.corp_code end )='"+corpCode+"' ");
		
		if(epname != null && !"".equals(epname)){
			sqlStr.append(" and (case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(") when 1 then client.")
			.append(TableLfClient.NAME).append("  else malist.").append(TableLfMalist.NAME).append(" end )")
			.append(" like '%").append(epname).append("%'");
		}
		
		//查询
		String countSql = "select count(*) totalcount from ( " + sqlStr.toString() + " ) a";
		sqlStr.append( " order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc ");
		//EmpExecutionContext.info("[客户群组群发]sql："+sqlStr.toString());
		returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(GroupInfoVo.class, sqlStr.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		//返回
		return returnVoList;
	}
	
	/**
	 *   查 客户机构下的客户 (包含子机构的情况没有地方调用且统计总数存在问题)
	 * @param clientDep	客户机构对象
	 * @param LfClient	客户
	 * @param containType	是否包含   1包含   2不包含
	 * @param pageInfo	分页
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findClientsByDepId(LfClientDep clientDep,LfClient LfClient,Integer containType,PageInfo pageInfo) throws Exception
	{
		List<DynaBean> beanList = null;
		String sql = "select distinct client.NAME,client.MOBILE,client.CLIENT_ID,depsp.DEP_ID" ;
		String countSql = "select count(*) totalcount ";
		String baseSql = " from LF_CLIENT client inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID ";
		StringBuffer conditionSql = new StringBuffer("");
		conditionSql.append(" where client.CORP_CODE = '").append(LfClient.getCorpCode()).append("'");
		
		if(LfClient.getName() != null && !"".equals(LfClient.getName())){
			conditionSql.append(" and client.NAME like '%").append(LfClient.getName()).append("%'");
		}
		if(clientDep != null){
			if(containType == 1){
				conditionSql.append(" and depsp.DEP_ID in (select DEP_ID from LF_CLIENT_DEP where DEP_PATH like '").append(clientDep.getDeppath()).append("%') ");
			}else if(containType == 2){
				conditionSql.append(" and depsp.DEP_ID = ").append(clientDep.getDepId());
                //如果为不包含子机构 在单个机构下 不需要对记录进行去重
                sql = sql.replaceFirst(" distinct","");
			}
		}
		String orderSql = " order by client.CLIENT_ID DESC";
		sql += baseSql;
		countSql += baseSql;
		sql += conditionSql + orderSql;
		countSql += conditionSql;
		beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		return beanList;
	}
	
	
	
	/**
	 *   获取机构客户 人数
	 * @param clientDep	机构对象
	 * @param containType 1包含  2是不包含
	 * @return
	 * @throws Exception
	 */
	public Integer findClientsCountByDepId(LfClientDep clientDep,Integer containType) throws Exception
	{
		StringBuffer sqlBuffer = new StringBuffer(" select COUNT(c.CLIENT_ID) as totalcount from ") ;
		sqlBuffer.append(" (select  a.CLIENT_ID  from LF_ClIENT_DEP_SP a ");
		if(containType == 1){
			sqlBuffer.append(" where a.DEP_ID in (select b.DEP_ID from LF_CLIENT_DEP b where b.DEP_PATH like '").append(clientDep.getDeppath()).append("%') ");
		}else if(containType == 2){
			sqlBuffer.append(" where a.DEP_ID = ").append(clientDep.getDepId());
		}
		sqlBuffer.append(")c");
		
		Integer count = findCountBySQL(sqlBuffer.toString());
		
		return count;
	}
	
	
	/**
	 *   处理客户机构查询其客户的手机号码的方法
	 * @param depIdStr	不包含的机构串
	 * @param depPathList	包含子机构的机构PATH
	 * @return
	 * @throws Exception
	 */
	public void getClientPhoneByDepIds(List<String> phoneList,String depIdStr,List<String> depPathList) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//示范sql语句
		/*select distinct client.MOBILE  from LF_CLIENT client 
			inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID 
			where client.CORP_CODE = '100001'
			and (depsp.DEP_ID in (1,2,3) or  depsp.DEP_ID in 
			(select DEP_ID from LF_CLIENT_DEP where DEP_PATH like '1/%') );*/
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select client.MOBILE  from LF_CLIENT client ");
		buffer.append(" inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID ");
		buffer.append("");
		boolean flag = false;
		if(!"".equals(depIdStr) && depIdStr.length() > 0){
			depIdStr = depIdStr.substring(0, depIdStr.length()-1);
			buffer.append(" where ( depsp.DEP_ID in (").append(depIdStr).append(")");
			flag = true;
		}
		if(depPathList != null && depPathList.size() > 0){
			if(flag){
				buffer.append(" or ");
			}else{
				buffer.append(" where (");
			}
			for(int i=0;i<depPathList.size();i++){
				String depstr = "dep"+i;
				if(i > 0){
					buffer.append(" or ");
				}
				buffer.append(" depsp.DEP_ID in ( select ").append(depstr)
				.append(".DEP_ID from LF_CLIENT_DEP ").append(depstr)
				.append("  where ").append(depstr).append(".DEP_PATH like ")
				.append("'").append(depPathList.get(i)).append("%')");
			}
			buffer.append(" ) ");
		}else if(flag){
			buffer.append(" ) ");
		}
		
		try {
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + buffer.toString());
			ps = conn.prepareStatement(buffer.toString());
			//执行SQL
			rs = ps.executeQuery();
			while (rs.next()) {
				//获取手机号码
				phoneList.add(rs.getString(1));
			}
		} catch (SQLException e) {
			EmpExecutionContext.error(e,"处理客户机构查询其客户的手机号码失败！");
			throw e;
		}finally {
			//关闭数据库资源
			try {
				close(rs, ps, conn);
			} catch (SQLException e) {
				EmpExecutionContext.error(e,"关闭数据库资源出错！");
			}
		}
		
	}
	
	
	/**
	 *   查询高级搜索客户查询
	 * @param client
	 * @param conditionMap
	 * @param custFieldValueList
	 * @param pageInfo
	 * @return
	 */
	public List<DynaBean> findAdSearchClient(LfClient client,LinkedHashMap<String, String> conditionMap,
			List<LfCustFieldValueVo> custFieldValueList,PageInfo pageInfo){
		try{
		    //拼SQL语句
			StringBuffer sqlbuffer = new StringBuffer(" select client.* ");
			//client.CLIENT_ID,client.NAME,client.SEX,client.BIRTHDAY,client.MOBILE,client.CLIENT_CODE
			String basesql = " from LF_CLIENT client ";
			
			sqlbuffer.append(basesql);
			//所属企业
			sqlbuffer.append(" where client.").append(TableLfClient.CORP_CODE).append("='").append(client.getCorpCode()).append("' ");
			
			String conditionsql = this.getConditionClient(client, conditionMap, custFieldValueList);
			conditionMap.remove("conditionsql");
			conditionMap.put("conditionsql", conditionsql);
			String orderbySql = " order by client.CLIENT_ID DESC";
			//返回结果
	        String sql = sqlbuffer.toString() +  conditionsql + orderbySql;
	        String countSql=new StringBuffer("select count(*) totalcount ").append(basesql).append(" and client.").append(TableLfClient.CORP_CODE).append("='").append(client.getCorpCode()).append("' ").append(conditionsql).toString();
	        return  new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"客户群组群发高级搜索客户失败！");
		}
		return null;
	}
	
	/**
	 * 高级搜索
	 * 根据机构id,获取该机构和该机构的子机构
	 */
	public List<LfClientDep> findDepIdsAndSelf(String depId, String corpCode)
			throws Exception {
		// sql拼接
		StringBuffer sql = new StringBuffer("select ").append(
				TableLfClientDep.DEP_ID).append(" from ").append(
						TableLfClientDep.TABLE_NAME).append(" where ").append(
				TableLfClient.CORP_CODE).append("='").append(corpCode.trim())
				.append("' ");
		if (null != depId && !"".equals(depId)) {
			String depIds = this.getClientChildByParentID(Long.valueOf(depId),
					TableLfClient.DEP_ID);
			sql.append(" and(").append(depIds).append(")");
		}
		
		List<LfClientDep> returnList = findPartEntityListBySQL(LfClientDep.class, sql
				.toString(), StaticValue.EMP_POOLNAME);
		// 返回查询结果
		return returnList;
	}
	
	/**
	 *   获取查询条件
	 * @param lfclient	客户对象
	 * @param conditionMap	放查询条件
	 * @param custFieldValueList 客户属性
	 * @return
	 */
	private String getConditionClient(LfClient lfclient,LinkedHashMap<String,String> conditionMap,List<LfCustFieldValueVo> custFieldValueList){
			StringBuffer buffer = new StringBuffer();
		try{
				
				//机构
			   if(conditionMap.get("depIds")!=null&&!"".equals(conditionMap.get("depIds"))){
				   if(lfclient.getDepId() != null)
		           {
						buffer.append(" and client.CLIENT_ID in (");
						buffer.append(" select sp.CLIENT_ID from LF_ClIENT_DEP_SP sp where sp.DEP_ID in ( ")
						.append(conditionMap.get("depIds")).append(" )").append(" )"); 
		           }
			   }
			   //拼接查询条件
			   if(lfclient.getClientCode() != null && !"".equals(lfclient.getClientCode()))
               {
				   buffer.append(" and client.CLIENT_CODE like '%").append(lfclient.getClientCode()).append("%'");
               }
               if(lfclient.getName() != null && !"".equals(lfclient.getName()))
               {
				   buffer.append(" and client.NAME like '%").append(lfclient.getName()).append("%'");
               }
               if(lfclient.getSex() != null)
               {
				   buffer.append(" and  client.SEX = ").append(lfclient.getSex());
               }
               if(lfclient.getMobile() != null && !"".equals(lfclient.getMobile()))
               {
				   buffer.append(" and client.MOBILE like '%").append(lfclient.getMobile()).append("%'");
               }
               if(lfclient.getArea() != null && !"".equals(lfclient.getArea()))
               {
				   buffer.append(" and client.AREA ='").append(lfclient.getArea()).append("'");
               }
	           	if (custFieldValueList != null && custFieldValueList.size()>0) {
					for(LfCustFieldValueVo custfieldvalue : custFieldValueList){
						buffer.append(" and (client.").append(
								custfieldvalue.getField_Ref()).append(" like '")
								.append(custfieldvalue.getId()).append(";%'");
						buffer.append(" or client.").append(
								custfieldvalue.getField_Ref()).append(" like '%;")
								.append(custfieldvalue.getId()).append(";%'");
						buffer.append(" or client.").append(
								custfieldvalue.getField_Ref()).append(" like '%;")
								.append(custfieldvalue.getId()).append("'");
						buffer.append(" or client.").append(
								custfieldvalue.getField_Ref()).append(" = '").append(
								custfieldvalue.getId()).append("')");
					}
	           	}
	            if(conditionMap.get("birth1") != null)
               {
	            	String beginbirth = conditionMap.get("birth1");
	            	beginbirth = beginbirth + " 00:00:00";
				  	String str = new DataAccessDriver().getGenericDAO().getTimeCondition(beginbirth);
					buffer.append(" and client.BIRTHDAY >= ").append(str);

               }
	            
	            if(conditionMap.get("birth2") != null)
               {
	            	String lastbirth = conditionMap.get("birth2");
	            	lastbirth = lastbirth + " 23:59:59";
	            	String str =  new DataAccessDriver().getGenericDAO().getTimeCondition(lastbirth);
	            	buffer.append(" and client.BIRTHDAY <= ").append(str);
               }
	           	
		}catch (Exception e) {
			EmpExecutionContext.error(e,"客户群组群发获取查询条件失败！");
			buffer = new StringBuffer();
		}
		return buffer.toString();
	}
	
	
	/**
	 *  通过客户ID查询出客户机构名称
	 * @param clinetId	客户
	 * @return
	 */
	public String getDepnameByClineId(Long clinetId){
		String str = "";
		try{
			StringBuilder sb = new StringBuilder();
			String tt = "SELECT dep.dep_name from LF_CLIENT_DEP dep where dep.dep_id in " +
			"(select sp.dep_id from LF_ClIENT_DEP_SP sp where sp.client_id = "+clinetId+") ORDER BY dep.dep_id asc";
			List<DynaBean> deptnams = getListDynaBeanBySql(tt);
			if(deptnams!=null && deptnams.size()>0){
				for(DynaBean aa :deptnams){
					sb.append(aa.get("dep_name")+",");
				}
			}
			str = sb.toString();
			if(str != null && !"".equals(str)){
				str = str.substring(0, str.length()-1);
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"客户群组群发根据客户ID查询机构名称失败！");
			str = "";
		}
		return str;
	}
	
	/**
	 *  获取该企业下的区域
	 * @param corpCode
	 * @return
	 */
	public List<DynaBean> getClientArea(String corpCode){
		List<DynaBean> beanList = null;
		try{
			String tt = "SELECT DISTINCT client.AREA from LF_CLIENT client where  client.CORP_CODE = '"+corpCode+"'";
			beanList = getListDynaBeanBySql(tt);
		}catch (Exception e) {
			beanList = null;
			EmpExecutionContext.error(e,"客户群组群发获取区域失败！");
		}
		return beanList;
	}
	
	/**
	 * 获取客户手机号
	 * @param corpcode 企业编码
	 * @param conditionsql 查询条件
	 * @param unChioceUserIds 未选中的客户ID字符串
	 * @param //pageInfo 分页
	 * @return
	 */
	public List<DynaBean> getClientByConditionSql(String corpcode,String conditionsql,String unChioceUserIds){
		List<DynaBean> clientBeans =null;
		try{
		    //拼SQL语句
			StringBuffer sqlbuffer = new StringBuffer(" select client.MOBILE ");
			//client.CLIENT_ID,client.NAME,client.SEX,client.BIRTHDAY,client.MOBILE,client.CLIENT_CODE
			String basesql = " from LF_CLIENT client ";
			sqlbuffer.append(basesql);
			StringBuffer conditionSql = new StringBuffer();
			//所属企业
			conditionSql.append(" where client.").append(TableLfClient.CORP_CODE).append("='").append(corpcode).append("' ");
			//传过来的查询条件，包含"and ..."
			if(conditionsql !=null && !"".equals(conditionsql)){
				conditionSql.append(conditionsql);
			}
			//未选中的客户ID,形如 "81,83" 
			if(unChioceUserIds !=null && !"".equals(unChioceUserIds)){
				//unChioceUserIds = unChioceUserIds.substring(0, unChioceUserIds.length()-1);
				conditionSql.append(" and client.CLIENT_ID not in (").append(unChioceUserIds).append(")");
			}
			//返回结果
			String sql = sqlbuffer.toString() + conditionSql.toString();
			
			clientBeans = getListDynaBeanBySql(sql);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"客户群组群发获取客户手机号码失败！");
		}
		return clientBeans;
	}

	
	
	/**
	 *   通过企业编码去获取该企业的微信用户
	 * @param corpcode	企业编码
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getWxuser(String corpcode,String ename) throws Exception{
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select wxuser.uname,wxuser.phone from LF_WC_USERINFO wxuser ");
		buffer.append(" where wxuser.CORP_CODE = '").append(corpcode).append("'");
		if(ename != null && !"".equals(ename)){
			buffer.append(" and wxuser.uname like '%").append(ename).append("%'");
		}
		return getListDynaBeanBySql(buffer.toString());
	}

	
	/**
	 * 根据套餐编码统计套餐数量
	 * @param 
	 * @param 
	 * @return 
	 */
	public List<DynaBean> getYdywGroupMemberCount(String tcCodes,String corpCode){
		List<DynaBean> countBeans =null;
		try
		{
			String sql="";
				sql="select count(ID) membercount,taocan_code taocancode from lf_contract_taocan " +
				" where  taocan_code in ("+tcCodes+") and corp_code='"+corpCode+"'" +" and is_valid='0' "+
				" and guid in (select guid from LF_CLIENT where corp_code='"+
				corpCode+"') group by taocan_code";
			
			countBeans=getListDynaBeanBySql(sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "类型查询群组的成员数量异常!");
		}
		return countBeans;
	}
	
	public List<DynaBean> getYdywGroupMember(String tcCodes,String corpCode){
		List<DynaBean> ydywGroupMemberList =null;
		StringBuffer sbSql=new StringBuffer("");
		sbSql.append("select client.mobile mobile from lf_client client inner join lf_contract_taocan ctc on client.guid=ctc.guid where client.corp_code='"+corpCode+"' and ctc.taocan_code in ("+tcCodes+") and ctc.is_valid='0' ");
		try{
			//EmpExecutionContext.info("[客户群组群发]sql："+sbSql.toString());
			ydywGroupMemberList=getListDynaBeanBySql(sbSql.toString());
		}catch(Exception e){
			EmpExecutionContext.error(e, "查询签约套餐异常!");
		}
		return ydywGroupMemberList;
	}
	
	
	/**
	 * 根据签约ID获取签约ID和签约账号的动态Bean
	 * @param contractIDs 签约ID字符串
	 * @return
	 */
	public List<DynaBean> getAccountNoByContractIds(String contractIDs){
		List<DynaBean> contractBeanList =null;
		String accountNoSql="select contract_id,acct_no from lf_contract where contract_id in ("+contractIDs+")";
		try{
			contractBeanList=getListDynaBeanBySql(accountNoSql);
		}catch(Exception e){
			EmpExecutionContext.error(e, "根据签约ID获取签约ID和签约账号的动态Bean失败！");
		}
		return contractBeanList;
	}
	
	/**
	 *   处理客户机构查询其客户的手机号码的方法(重载方法)
	 * @param depIdStr	不包含的机构串
	 * @param depPathList	包含子机构的机构PATH
	 * @return
	 * @throws Exception
	 */
	public void getClientPhoneByDepIds(List<String> phoneList, String depIdStr, List<String> depPathList, String corpCode) throws Exception
	{
		//不允许同时为空
		if(depIdStr==null&&depPathList==null){
			EmpExecutionContext.error("客户群组群发不包含子机构的ID和包含子机构的ID都为空，不允许查询！");
			return;
		}else{
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			//示范sql语句
			/*select distinct client.MOBILE  from LF_CLIENT client 
				inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID 
				where client.CORP_CODE = '100001'
				and (depsp.DEP_ID in (1,2,3) or  depsp.DEP_ID in 
				(select DEP_ID from LF_CLIENT_DEP where DEP_PATH like '1/%') );*/
			StringBuffer conditionSb=new StringBuffer("");
			boolean flag = false;
			if(depIdStr!=null&&!"".equals(depIdStr.trim()) && depIdStr.length() > 0){
				depIdStr = depIdStr.substring(0, depIdStr.length()-1);
				conditionSb.append("  ( depsp.DEP_ID in (").append(depIdStr).append(")");
				flag = true;
			}
			if(depPathList != null && depPathList.size() > 0){
				if(flag){
					conditionSb.append(" or ");
				}else{
					conditionSb.append(" (");
				}
				for(int i=0;i<depPathList.size();i++){
					String depstr = "dep"+i;
					if(i > 0){
						conditionSb.append(" or ");
					}
					conditionSb.append(" depsp.DEP_ID in ( select ").append(depstr)
					.append(".DEP_ID from LF_CLIENT_DEP ").append(depstr)
					.append("  where ").append(depstr).append(".DEP_PATH like ")
					.append("'").append(depPathList.get(i)).append("%')");
				}
				conditionSb.append(" ) ");
			}else if(flag){
				conditionSb.append(" ) ");
			}
			//这一个判断很重要，特别重要。
			if(conditionSb.length()<1){
				EmpExecutionContext.error("客户群组群发机构查询条件长度为0，不查询！机构必须带查询条件！");
				return ;
			}else{
				StringBuffer buffer = new StringBuffer();
				buffer.append(" select client.MOBILE  from LF_CLIENT client ");
				buffer.append(" inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID ");
				buffer.append(" where  client.corp_code='"+corpCode+"' and ");
				//拼装SQL
				String sql=buffer.toString()+conditionSb.toString();
				try {
					//获取数据库连接
					conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
					//EmpExecutionContext.info("[客户群组群发]sql："+sql);
					EmpExecutionContext.sql("execute sql : " + sql);
					ps = conn.prepareStatement(sql);
					//执行SQL
					rs = ps.executeQuery();
					while (rs.next()) {
						//获取手机号码
						phoneList.add(rs.getString(1));
					}
				} catch (SQLException e) {
					EmpExecutionContext.error(e,"处理客户机构查询其客户的手机号码失败！");
					throw e;
				}finally {
					//关闭数据库资源
					try {
						close(rs, ps, conn);
					} catch (SQLException e) {
						EmpExecutionContext.error(e,"关闭数据库资源出错！");
					}
				}
			}
		}
	}
	/**
	 *   查询高级搜索客户查询(新增方法)
	 * @param client
	 * @param conditionMap
	 * @param custFieldValueList
	 * @param pageInfo
	 * @return
	 */
	public List<DynaBean> findAdSearchClientNew(LfClient client,LinkedHashMap<String, String> conditionMap,
			List<LfCustFieldValueVo> custFieldValueList,PageInfo pageInfo){
		List<DynaBean> returnBeans=null;
		try{
			if(client.getCorpCode()==null||"".equals(client.getCorpCode().trim())){
				EmpExecutionContext.error("异常，客户群组群发高级搜索页面传入的企业编码异常，corpCode:"+client.getCorpCode()+"，不能查询数据！");
				return null;
			}
		    //拼SQL语句
			StringBuffer sqlbuffer = new StringBuffer(" select client.*  from LF_CLIENT client where ");
			//所属企业
			sqlbuffer.append(" client.corp_code='").append(client.getCorpCode()).append("' ");
			
			//这个and很重要，不能删除
			sqlbuffer.append(" and ");
			
			//处理机构
			//页面查询传入的机构,这个很重要。
			//1.获取到页面传入的机构，机构ID为-10代表APP机构，机构ID大于0为普通机构
			if(client.getDepId()!=null&&(client.getDepId().longValue()>0L||client.getDepId().longValue()==-10L)){
				String depStr=conditionMap.get("depIds");
				//1.1异常，用页面传入的机构查询此机构及其子机构异常
				if(depStr==null||"".equals(depStr.trim())){
					EmpExecutionContext.error("异常，客户群组群发高级搜索页面选择的机构ID有值，depID:"+client.getDepId()+"，但是查询此机构的机构及其子机构失败，很可能此机构已经删除，不能查询数据！");
					return null;
				//1.2正常，用页面传入的机构查询此机构及其子机构正常
				}else{
					EmpExecutionContext.info("正常，客户群组群发高级搜索页面选择的机构ID有值,depID为:"+client.getDepId()+"。");
				}
			//2.未获取到页面传入的机构
			}else{
				EmpExecutionContext.error("异常，客户群组群发高级搜索页面选择的机构ID为空或者为0，depID:"+client.getDepId()+"，不能查询数据！");
				return null;
			}
			
			//拼接查询条件
			String conditionsql = this.getConditionClientNew(client, conditionMap, custFieldValueList);
			if(conditionsql==null||"".equals(conditionsql.trim())||conditionsql.trim().length()<1){
				EmpExecutionContext.error("客户群组群发高级搜索查询拼装页面选择的客户机构等查询条件失败！不能查询数据！");
				return null;
			}
			conditionMap.remove("conditionsql");
			conditionMap.put("conditionsql", conditionsql);
			String orderbySql = " order by client.CLIENT_ID DESC";
			//返回结果
	        String sql = sqlbuffer.toString() +  conditionsql + orderbySql;
	        //此语句增加了一个and
	        String countSql=new StringBuffer("select count(*) totalcount  from LF_CLIENT client where  client.corp_code='").append(client.getCorpCode()).append("' ").append(" and ").append(conditionsql).toString();
	        returnBeans= new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}catch (Exception e) {
			returnBeans=null;
			EmpExecutionContext.error(e,"客户群组群发高级搜索客户失败！");
		}
		return returnBeans;
	}

	/**
	 * 客户群组群发高级搜索根据查询条件获取发送的客户手机号（新方法）这个方法很重要
	 * @param corpcode 企业编码
	 * @param conditionsql 查询条件
	 * @param unChioceUserIds 未选中的客户ID字符串
	 * @return
	 */
	public List<DynaBean> getClientByConditionSqlNew(String corpcode,String conditionsql,String unChioceUserIds){
		//conditionsql绝对要有值，最少会限制机构，如果没有值，则不查询。
		if(conditionsql==null||"".equals(conditionsql.trim())){
			EmpExecutionContext.error("异常，客户群组群发高级搜索预览查询手机号码，客户机构等查询条件丢失，不能查询！");
			return null;
		}
		//企业编码一定要有值
		if(corpcode==null||"".equals(corpcode.trim())){
			EmpExecutionContext.error("异常，客户群组群发高级搜索预览查询手机号码，企业编码查询条件丢失，不能查询！");
			return null;
		}
		List<DynaBean> clientBeans =null;
		String sql="";
		try{
		    //拼SQL语句
			StringBuffer sqlbuffer = new StringBuffer(" select client.MOBILE from LF_CLIENT client where ");
			//所属企业
			sqlbuffer.append(" client.corp_code='").append(corpcode).append("' ");
			//添加and
			sqlbuffer.append(" and ");
			
			//传过来的查询条件，包含"and ..."
			StringBuffer conditionSql = new StringBuffer(conditionsql.replaceAll("#&M", "%"));
			//由于过滤可能会出现关键字过滤，select可能会过滤，当select过滤时添加进来
			if(conditionSql.indexOf("select") < 0) {
				int index = conditionSql.indexOf("(") + 1;
				conditionSql.insert(index, "select ");
			}
			//未选中的客户ID,形如 "81,83" 
			if(unChioceUserIds !=null && !"".equals(unChioceUserIds.trim())){
				conditionSql.append(" and client.CLIENT_ID not in (").append(unChioceUserIds).append(")");
			}
			//返回结果
			sql = sqlbuffer.toString() + conditionSql.toString();
			//EmpExecutionContext.info("[客户群组群发]sql："+sql);
			clientBeans = getListDynaBeanBySql(sql);
		}catch (Exception e) {
			clientBeans=null;
			EmpExecutionContext.error(e,"客户群组群发高级搜索根据查询条件获取发送的客户手机号失败！sql语句为"+sql);
		}
		return clientBeans;
	}
	/**
	 *   获取查询条件
	 * @param lfclient	客户对象
	 * @param conditionMap	放查询条件
	 * @param custFieldValueList 客户属性
	 * @return
	 */
	private String getConditionClientNew(LfClient lfclient,LinkedHashMap<String,String> conditionMap,List<LfCustFieldValueVo> custFieldValueList){
		String conditionSqlStr=null;
		try{
			
//					//机构
//				   if(conditionMap.get("depIds")!=null&&!"".equals(conditionMap.get("depIds"))){
//					   if(lfclient.getDepId() != null && !"".equals(lfclient.getDepId()))
//			           {
//							buffer.append(" and client.CLIENT_ID in (");
//							buffer.append(" select sp.CLIENT_ID from LF_ClIENT_DEP_SP sp where sp.DEP_ID in ( ")
//							.append(conditionMap.get("depIds")).append(" )").append(" )"); 
//			           }
//				   }
			    StringBuffer buffer = new StringBuffer("");
				//机构
			    //将and去掉，and在外面拼写 
				buffer.append(" client.CLIENT_ID in (");
				buffer.append(" select sp.CLIENT_ID from LF_ClIENT_DEP_SP sp where sp.DEP_ID in ( ")
				.append(conditionMap.get("depIds")).append(" )").append(" )"); 
		         
			  
			   //拼接查询条件
			   if(lfclient.getClientCode() != null && !"".equals(lfclient.getClientCode()))
               {
				   buffer.append(" and client.CLIENT_CODE like '%").append(lfclient.getClientCode()).append("%'");
               }
               if(lfclient.getName() != null && !"".equals(lfclient.getName()))
               {
				   buffer.append(" and client.NAME like '%").append(lfclient.getName()).append("%'");
               }
               if(lfclient.getSex() != null)
               {
				   buffer.append(" and  client.SEX = ").append(lfclient.getSex());
               }
               if(lfclient.getMobile() != null && !"".equals(lfclient.getMobile()))
               {
				   buffer.append(" and client.MOBILE like '%").append(lfclient.getMobile()).append("%'");
               }
               if(lfclient.getArea() != null && !"".equals(lfclient.getArea()))
               {
				   buffer.append(" and client.AREA ='").append(lfclient.getArea()).append("'");
               }
	           	if (custFieldValueList != null && custFieldValueList.size()>0) {
					for(LfCustFieldValueVo custfieldvalue : custFieldValueList){
						buffer.append(" and (client.").append(
								custfieldvalue.getField_Ref()).append(" like '")
								.append(custfieldvalue.getId()).append(";%'");
						buffer.append(" or client.").append(
								custfieldvalue.getField_Ref()).append(" like '%;")
								.append(custfieldvalue.getId()).append(";%'");
						buffer.append(" or client.").append(
								custfieldvalue.getField_Ref()).append(" like '%;")
								.append(custfieldvalue.getId()).append("'");
						buffer.append(" or client.").append(
								custfieldvalue.getField_Ref()).append(" = '").append(
								custfieldvalue.getId()).append("')");
					}
	           	}
	            if(conditionMap.get("birth1") != null)
               {
	            	String beginbirth = conditionMap.get("birth1");
	            	beginbirth = beginbirth + " 00:00:00";
				  	String str = new DataAccessDriver().getGenericDAO().getTimeCondition(beginbirth);
					buffer.append(" and client.BIRTHDAY >= ").append(str);

               }
	            
	            if(conditionMap.get("birth2") != null)
               {
	            	String lastbirth = conditionMap.get("birth2");
	            	lastbirth = lastbirth + " 23:59:59";
	            	String str =  new DataAccessDriver().getGenericDAO().getTimeCondition(lastbirth);
	            	buffer.append(" and client.BIRTHDAY <= ").append(str);
               }
	            conditionSqlStr=buffer.toString();
		}catch (Exception e) {
			EmpExecutionContext.error(e,"客户群组群发获取查询条件失败！");
			conditionSqlStr=null;
		}
		return conditionSqlStr;
	}
	
	
	
}
