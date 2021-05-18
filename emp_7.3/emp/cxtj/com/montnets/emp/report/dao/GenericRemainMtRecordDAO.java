/**
 * 
 */
package com.montnets.emp.report.dao;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sms.MtTaskC;
import com.montnets.emp.inbox.dao.ReciveBoxDao;
import com.montnets.emp.table.sms.TableMtTaskC;

/**
 * 机构统计报表
 * @project p_cxtj
 * @author liaojirong <ljr0300@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 下午02:42:38
 * @description
 */
public class GenericRemainMtRecordDAO extends SuperDAO{

	

	/*public <T> int update(Class<T> entityClass,LinkedHashMap<String, String> objectMap,LinkedHashMap<String, String> conditionMap) throws Exception
	{
		//boolean isSuccess = false;
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = new GenericEmpDAO().getORMMap(entityClass);
	//  拼接修改的SQL语句
		StringBuffer sqlSb = new StringBuffer("update ").append(
				columns.get(entityClass.getSimpleName())).append(" set ");
		Iterator<Map.Entry<String, String>> objIter = objectMap.entrySet()
				.iterator();
		Map.Entry<String, String> e = null;
		//获取实体类的属性
		Field[] fields = entityClass.getDeclaredFields();
		String fieldType = null;
		while (objIter.hasNext())
		{
			e = objIter.next();
			
			for (int index = 0; index < fields.length; index++)
			{
				if (fields[index].getName().equals(e.getKey()))
				{
					fieldType = fields[index].getGenericType()
							.toString();
					break;
				}
			}
			
			if (e.getValue() != null && fieldType.equals(StaticValue.TIMESTAMP)
					|| fieldType.equals(StaticValue.DATE_UTIL))
			{
				if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
					sqlSb.append(columns.get(e.getKey())).append("=to_date('").append(
							e.getValue()).append("','yyyy-MM-dd HH24:mi:ss'),");
				}else{
					sqlSb.append(columns.get(e.getKey())).append("='").append(
							e.getValue()).append("',");
				}
			} else if (e.getValue() != null&&(fieldType.equals("class java.lang.String") || fieldType.equals(StaticValue.TIMESTAMP)
					|| fieldType.equals(StaticValue.DATE_UTIL)))
			{
				sqlSb.append(columns.get(e.getKey())).append("='").append(
						e.getValue()).append("',");
			} else
			{
				sqlSb.append(columns.get(e.getKey())).append("=").append(
						e.getValue()).append(",");
			}
		}
		sqlSb.deleteCharAt(sqlSb.lastIndexOf(",")).append(" ");
		//查询条件
		StringBuffer conditionSql = new StringBuffer();
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			//实体类字段名
			String field = null;
			//实体类字段值
			String fieldValue = null;
			//操作符
			String symbols = null;
			Iterator<Map.Entry<String, String>> iter = conditionMap
					.entrySet().iterator();
			while (iter.hasNext())
			{
				e = iter.next();
				fieldValue = e.getValue();
				if (!"".equals(fieldValue))
				{
					if (e.getKey().contains("&"))
					{
						field = e.getKey().split("&")[0];
						symbols = e.getKey().split("&")[1];
					} else
					{
						field = e.getKey();
						symbols = "=";
					}
					for (int index = 0; index < fields.length; index++)
					{
						if (fields[index].getName().equals(field))
						{
							fieldType = fields[index].getGenericType()
									.toString();
							break;
						}
					}
					//拼接查询条件
					conditionSql.append(" and ").append(columns.get(field));
					if (symbols.equalsIgnoreCase(StaticValue.LIKE1)
							|| symbols.equalsIgnoreCase(StaticValue.LIKE2))
					{
						conditionSql.append(" like ");
					} else
					{
						conditionSql.append(" ").append(symbols)
								.append(" ");
					}
					if (symbols.equalsIgnoreCase(StaticValue.IN)
							|| symbols.equalsIgnoreCase(StaticValue.NOT_IN))
					{
						String[] valueArray = fieldValue.split(",");
						StringBuffer valueSb = new StringBuffer("(");
						//不同数据库引号问题的兼容
						if (fieldType.equals("class java.lang.String")) {
							for (int i = 0; i < valueArray.length; i++) {
								valueSb.append("'").append(valueArray[i])
										.append("',");
							}
						} else {
							for (int i = 0; i < valueArray.length; i++) {
								valueSb.append(valueArray[i]).append(",");
							}
						}
						valueSb.deleteCharAt(valueSb.lastIndexOf(","))
								.append(")");
						conditionSql.append(valueSb);
					} else if (symbols.equalsIgnoreCase(StaticValue.LIKE))
					{
						conditionSql.append("'%").append(fieldValue)
								.append("%'");
					} else if (symbols.equalsIgnoreCase(StaticValue.LIKE1))
					{
						conditionSql.append("'%").append(fieldValue)
								.append("'");
					} else if (symbols.equalsIgnoreCase(StaticValue.LIKE2))
					{
						conditionSql.append("'").append(fieldValue).append(
								"%'");
					} else if (symbols.equals("=") || symbols.equals(">")
							|| symbols.equals("<") || symbols.equals("<>")
							|| symbols.equals(">=") || symbols.equals("<="))
					{
						if (fieldType.equals("class java.sql.Timestamp")
								|| fieldType.equals("class java.util.Date"))
						{
							conditionSql.append(new DataAccessDriver().getGenericDAO().getTimeCondition(
											fieldValue));
						}else if(fieldType.equals("class java.lang.String")){
							conditionSql.append("'").append(fieldValue)
							.append("'");
						} else
						{
							conditionSql.append(fieldValue);
						}
					} else if (symbols
							.equalsIgnoreCase(StaticValue.BETWEEN))
					{
						String[] valueArray = fieldValue.split(",");
						if (fieldType.equals("class java.sql.Timestamp")
								|| fieldType.equals("class java.util.Date"))
						{
							conditionSql.append(new DataAccessDriver().getGenericDAO().getTimeCondition(
											valueArray[0], valueArray[1]));
						} else
						{
							conditionSql.append(valueArray[0]).append(
									" and ").append(valueArray[1]);
						}
					}
				}
			}
		}
		String sql = sqlSb.append(conditionSql).toString();
		System.out.println(sql);
		Connection conn = null;
		PreparedStatement ps = null;
		int count = 0;
		try
		{
			IConnectionManager connectionManager = new ConnectionManagerImp();
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//执行sql
			count = ps.executeUpdate();
			
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex," 查询合计处理异常");
			throw ex;
		} finally
		{
			// 关闭数据库资源
			super.close(null, ps, conn);
		}
		return count;
	}*/

	/**
	 * 获取滞留记录发送账号对应的数量
	 * @return 返回账号数量集合
	 * @throws Exception
	 */
	public List<DynaBean> getMtTaskCount(LinkedHashMap<String, String> conditionMap) {
		
		try
		{
			//拼接sql
			StringBuffer sql = new StringBuffer("SELECT UserId, count(id) as count from ")
					.append(TableMtTaskC.TABLE_NAME)
					.append(" ");
			
			String conditionSql = this.getConditionMapSql(MtTaskC.class, conditionMap);
			//将第一位and替换为where
			conditionSql =new ReciveBoxDao().getConditionSql(conditionSql);
			sql.append(conditionSql).append(" GROUP BY UserId ");
			
			List<DynaBean> returnList = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql.toString());
			//返回结果
			return returnList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取滞留记录发送账号对应的数量异常。");
			return null;
		}
	}

	public String getConditionMapSql(Class entityClass,
			LinkedHashMap<String, String> conditionMap) throws Exception
	{
		//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
	//  拼接查询的SQL语句
		StringBuffer sqlSb = new StringBuffer();
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			Field[] fields = entityClass.getDeclaredFields();
			Map.Entry<String, String> e = null;
			String field = null;
			String fieldValue = null;
			String symbols = null;
			String fieldType = null;
			StringBuffer conditionSql = new StringBuffer();
			Iterator<Map.Entry<String, String>> iter = conditionMap.entrySet()
					.iterator();
			while (iter.hasNext())
			{
				e = iter.next();
				fieldValue = e.getValue();
				if (!"".equals(fieldValue))
				{
					if (e.getKey().contains("&"))
					{
						field = e.getKey().split("&")[0];
						symbols = e.getKey().split("&")[1];
					} else
					{
						field = e.getKey();
						symbols = "=";
					}
					for (int index = 0; index < fields.length; index++)
					{
						if (fields[index].getName().equals(field))
						{
							fieldType = fields[index].getGenericType()
									.toString();
							break;
						}
					}
					conditionSql.append(" and ").append(columns.get(field));
					if (symbols.equalsIgnoreCase(StaticValue.LIKE1)
							|| symbols.equalsIgnoreCase(StaticValue.LIKE2))
					{
						conditionSql.append(" like ");
					} else
					{
						conditionSql.append(" ").append(symbols).append(" ");
					}
					if (symbols.equalsIgnoreCase(StaticValue.IN)
							|| symbols.equalsIgnoreCase(StaticValue.NOT_IN))
					{
						String[] valueArray = fieldValue.split(",");
						StringBuffer valueSb = new StringBuffer("(");
						if(fieldType.equals("class java.lang.String")){
							for (int i = 0; i < valueArray.length; i++)
							{
								valueSb.append("'").append(valueArray[i]).append(
										"',");
							}
						}else{
							for (int i = 0; i < valueArray.length; i++)
							{
								valueSb.append(valueArray[i]).append(",");
							}
						}
						valueSb.deleteCharAt(valueSb.lastIndexOf(",")).append(
								")");
						conditionSql.append(valueSb);
					} else if (symbols.equalsIgnoreCase(StaticValue.LIKE))
					{
						conditionSql.append("'%").append(fieldValue).append(
								"%'");
					} else if (symbols.equalsIgnoreCase(StaticValue.LIKE1))
					{
						conditionSql.append("'%").append(fieldValue)
								.append("'");
					} else if (symbols.equalsIgnoreCase(StaticValue.LIKE2))
					{
						conditionSql.append("'").append(fieldValue)
								.append("%'");
					} else if (symbols.equals("=") || symbols.equals(">")
							|| symbols.equals("<") || symbols.equals("<>")
							|| symbols.equals(">=") || symbols.equals("<="))
					{
						if (fieldType.equals("class java.sql.Timestamp")
								|| fieldType.equals("class java.util.Date"))
						{
							conditionSql.append(new DataAccessDriver().getGenericDAO().getTimeCondition(
											fieldValue));
						}else if(fieldType.equals("class java.lang.String")){
							conditionSql.append("'").append(fieldValue).append(
							"'");
						} else
						{
							conditionSql.append(fieldValue);
						}
					} else if (symbols.equalsIgnoreCase(StaticValue.BETWEEN))
					{
						String[] valueArray = fieldValue.split(",");
						if (fieldType.equals("class java.sql.Timestamp")
								|| fieldType.equals("class java.util.Date"))
						{
							conditionSql.append(new DataAccessDriver().getGenericDAO().getTimeCondition(
											valueArray[0], valueArray[1]));
						} else
						{
							conditionSql.append(valueArray[0]).append(" and ")
									.append(valueArray[1]);
						}
					}
				}
			}
			sqlSb.append(conditionSql);
		}
		
		
		return sqlSb.toString();
	}

	
}
