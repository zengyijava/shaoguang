package com.montnets.emp.common.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;

public class CommDataDealDAO extends SuperDAO
{
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 
	 * @description 获取实体类数据库Insert sql语句
	 * @param entityObject 实体类对象
	 * @return 返回Insert sql字符串，异常返回null
	 */
	public String getEntityInsertSQL(Object entityObject)
	{
		try
		{
			Class<?> entityClass = entityObject.getClass();
			// 获取实体类字段与数据库字段映射的map集合
			Map<String, String> columns = getORMMap(entityClass);
			//获取实体类的属性
			Field[] fields = entityClass.getDeclaredFields();
			//  拼接插入的SQL语句
			StringBuffer sqlSb = new StringBuffer()
			.append("insert into ").append(columns.get(entityClass.getSimpleName())).append("(");
			
			StringBuffer valuesSb = new StringBuffer(") values(");
			
			//属性名
			String fieldName;
			//属性名大写
			String fieldNameUpper;
			//属性类型
			String fieldType;
			Method entityMethod;
			//属性值
			Object value;
			
			for (int i = 0; i < fields.length; i++)
			{
				fieldName = fields[i].getName();
				//serialVersionUID，不构造
				if(fieldName.equals("serialVersionUID"))
				{
					continue;
				}
				//tableId，自增id，不构造
				if(columns.get(fieldName).equals(columns.get("tableId")))
				{
					continue;
				}
				
				sqlSb.append(columns.get(fieldName)).append(",");
				
				fieldNameUpper = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
				entityMethod = entityClass.getMethod("get" + fieldNameUpper);
				//通过反射获取变量的值
				value = entityMethod.invoke(entityObject);
				if(value == null)
				{
					valuesSb.append("null").append(",");
					continue;
				}
				
				// 获取变量的类型
				fieldType = fields[i].getGenericType().toString();
				
				//日期类型
				if (fieldType.equals(StaticValue.TIMESTAMP) || fieldType.equals(StaticValue.DATE_SQL) || fieldType.equals(StaticValue.DATE_UTIL))
				{
					String timeSql = getTimeSql(sdf.format(value));
					valuesSb.append(timeSql).append(",");
				}
				//其他类型
				else
				{
					valuesSb.append("'").append(value.toString()).append("'").append(",");
				}
			}
			// 去掉最后一个逗号。
			sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
			valuesSb.deleteCharAt(valuesSb.lastIndexOf(",")).append(")");
			sqlSb.append(valuesSb);
			return sqlSb.toString();
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取实体类数据库Insert sql语句，异常。");
			return null;
		} 
	}
	
	public String getTimeSql(String time)
	{
		try
		{
			//获取当前时间sql
			String timeSql;
			if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
			{
				//oracle数据库
				timeSql = "TO_TIMESTAMP('"+time+"', 'YYYY-MM-DD HH24:MI:SS')";
		   	}
			else
			{
				timeSql = "'"+time+"'";
			}
			/*else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				//sqlserver数据库
				timeSql = time;
			}
			else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
			{
				//db2数据库
				timeSql = time;
			}
			else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
			{
				//mysql数据库
				timeSql = time;
			}
			else
			{
				EmpExecutionContext.error("DAO获取当前时间sql，未知的数据库类型。dbType="+StaticValue.DBTYPE);
				return null;
			}*/
			return timeSql;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO获取当前时间sql，异常。");
			return null;
		}
	}
	
}
