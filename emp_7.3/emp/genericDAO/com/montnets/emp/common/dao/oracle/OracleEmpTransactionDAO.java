package com.montnets.emp.common.dao.oracle;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.GenericEmpTransactionDAO;

/**
 * 
 * @project emp
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-16 上午11:40:33
 * @description
 */
public class OracleEmpTransactionDAO extends GenericEmpTransactionDAO
{
	@Override
	public Long saveObjectReturnID(Connection conn, Object object)
			throws Exception
	{
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			//oracle数据库
			return this.saveObjectReturnIDOracle(conn, object);
	   	} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
	   	//sqlserver数据库
	   		return this.saveObjectReturnIDSqlServer(conn, object);
		}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
			//db2数据库
			return this.saveObjectReturnIDDB2(conn, object);
		}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
			//mysql数据库
			return this.saveObjectReturnIDMySql(conn, object);
		}
		return 0L;
	}
	
	private Long saveObjectReturnIDOracle(Connection conn, Object object)
			throws Exception {
		//插入数据后返回的主键id
		Long returnID = 0L;
		Class<?> entityClass = object.getClass();
	//  获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		//表名
		String tableName = columns.get(entityClass.getSimpleName());
	//  拼接插入的SQL语句
		StringBuffer sqlSb = new StringBuffer("insert into ").append(tableName)
				.append("(");
		StringBuffer wenhao = new StringBuffer(") values(");
		//获取实体类属性
		Field[] fields = entityClass.getDeclaredFields();
		String entityId = null;
		for (int i = 0; i < fields.length; i++) {
			if (!fields[i].getName().equals("serialVersionUID")) {
				sqlSb.append(columns.get(fields[i].getName())).append(",");
				wenhao.append("?").append(",");
				if (columns.get(fields[i].getName()).equals(
						columns.get("tableId"))) {
					entityId = fields[i].getName();
				}
			}
		}
		//去掉最后一个逗号
		sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		wenhao.deleteCharAt(wenhao.lastIndexOf(",")).append(")");
		sqlSb.append(wenhao);
		String sql = sqlSb.toString();
		PreparedStatement ps = null;
		try {
			//获取序列值
			Long sequenceValue = getSequenceNextValue(conn, columns
					.get("sequence"));

			if (sequenceValue != null) {
				EmpExecutionContext.sql("execute sql : " + sql);
				ps = conn.prepareStatement(sql);
				//属性类型
				String fieldType = null;
				//属性名称大写
				String fieldNameUpper = null;
				Method entityMethod = null;
				//属性值
				Object value = null;
				int psIndex = 0;
				for (int i = 0; i < fields.length; i++) {
					if (!fields[i].getName().equals("serialVersionUID")) {
						psIndex++;
						String fieldName = fields[i].getName();
						if (!fieldName.equals(entityId)) {
							fieldNameUpper = Character.toUpperCase(fieldName
									.charAt(0))
									+ fieldName.substring(1);
							entityMethod = entityClass.getMethod("get"
									+ fieldNameUpper);
							//通过反射获取变量的值
							value = entityMethod.invoke(object);
							// 获取变量的类型
							fieldType = fields[i].getGenericType().toString();
							// 是否是日期类型
							boolean isDateType = fieldType
									.equals(StaticValue.TIMESTAMP)
									|| fieldType.equals(StaticValue.DATE_SQL)
									|| fieldType.equals(StaticValue.DATE_UTIL);
							//填充value
							if (value != null && isDateType) {
								ps.setTimestamp(psIndex, Timestamp
										.valueOf(value.toString()));
							} else {
								ps.setObject(psIndex, value);
							}
						} else {
							ps.setObject(psIndex, sequenceValue);
						}
					}
				}
				//执行操作
				int count = ps.executeUpdate();
				if (count == 1) {
					returnID = sequenceValue;
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally {
			// 关闭数据库资源
			super.close(null, ps);
		}
		return returnID;
	}

	/**
	 * @param conn
	 * @param object
	 * @return returnID
	 * @throws Exception
	 */
	private Long saveObjectReturnIDSqlServer(Connection conn, Object object)
			throws Exception {
		//插入数据后返回主键id
		Long returnID = 0L;
		Class<?> entityClass = object.getClass();
		// 获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		//表名
		String tableName = columns.get(entityClass.getSimpleName());
		// 拼接插入的SQL语句
		StringBuffer sqlSb = new StringBuffer("insert into ").append(tableName)
				.append("(");
		StringBuffer wenhao = new StringBuffer(") values(");
		//获取实体类的属性
		Field[] fields = entityClass.getDeclaredFields();
		String entityId = null;
		for (int i = 0; i < fields.length; i++) {
			if (!fields[i].getName().equals("serialVersionUID")) {
				if (columns.get(fields[i].getName()).equals(
						columns.get("tableId"))) {
					entityId = fields[i].getName();
				} else {
					sqlSb.append(columns.get(fields[i].getName())).append(",");
					wenhao.append("?").append(",");
				}
			}
		}
		// 去掉最后一个逗号。
		sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		wenhao.deleteCharAt(wenhao.lastIndexOf(",")).append(")");
		sqlSb.append(wenhao);
		String sql = sqlSb.toString();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//属性类型
			String fieldType = null;
			//属性名大写
			String fieldNameUpper = null;
			//属性值
			Method entityMethod = null;
			Object value = null;
			int psIndex = 0;
			for (int i = 0; i < fields.length; i++) {
				if (!fields[i].getName().equals("serialVersionUID")) {
					String fieldName = fields[i].getName();
					if (!fieldName.equals(entityId)) {
						psIndex++;
						fieldNameUpper = Character.toUpperCase(fieldName
								.charAt(0))
								+ fieldName.substring(1);
						entityMethod = entityClass.getMethod("get"
								+ fieldNameUpper);
						//获取属性值
						value = entityMethod.invoke(object);
						// 获取变量的类型
						fieldType = fields[i].getGenericType().toString();
						//是否是日期类型
						boolean isDateType = fieldType
								.equals(StaticValue.TIMESTAMP)
								|| fieldType.equals(StaticValue.DATE_SQL)
								|| fieldType.equals(StaticValue.DATE_UTIL);
						//填充value
						if (value != null && isDateType) {
							ps.setTimestamp(psIndex, Timestamp.valueOf(value
									.toString()));
						} else {
							ps.setObject(psIndex, value);
						}
					}
				}
			}
			// 执行SQL语句。
			int count = ps.executeUpdate();
			if (count == 1) {
				ps = conn.prepareStatement("select IDENT_CURRENT('" + tableName
						+ "')");
				//执行sql
				rs = ps.executeQuery();
				if (rs.next()) {
					returnID = rs.getLong(1);
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally {
			//关闭数据库资源
			super.close(rs, ps, null);
		}
		return returnID;
	}
	
	/**
	 * @param conn
	 * @param object
	 * @return returnID
	 * @throws Exception
	 */
	private Long saveObjectReturnIDDB2(Connection conn, Object object)
			throws Exception {
		Long returnID = 0L;
		Class<?> entityClass = object.getClass();
		// 获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		//表名
		String tableName = columns.get(entityClass.getSimpleName());
		// 拼接插入的SQL语句
		StringBuffer sqlSb = new StringBuffer("insert into ").append(tableName)
				.append("(");
		StringBuffer wenhao = new StringBuffer(") values(");
		//获取实体类的属性
		Field[] fields = entityClass.getDeclaredFields();
		String entityId = null;
		for (int i = 0; i < fields.length; i++) {
			if (!fields[i].getName().equals("serialVersionUID")) {
				if (columns.get(fields[i].getName()).equals(
						columns.get("tableId"))) {
					entityId = fields[i].getName();
				} else {
					sqlSb.append(columns.get(fields[i].getName())).append(",");
					wenhao.append("?").append(",");
				}
			}
		}
		// 去掉最后一个逗号。
		sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		wenhao.deleteCharAt(wenhao.lastIndexOf(",")).append(")");
		sqlSb.append(wenhao);
		String sql = sqlSb.toString();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//属性类型
			String fieldType = null;
			//属性名大写
			String fieldNameUpper = null;
			Method entityMethod = null;
			//属性值
			Object value = null;
			int psIndex = 0;
			for (int i = 0; i < fields.length; i++) {
				if (!fields[i].getName().equals("serialVersionUID")) {
					String fieldName = fields[i].getName();
					if (!fieldName.equals(entityId)) {
						psIndex++;
						fieldNameUpper = Character.toUpperCase(fieldName
								.charAt(0))
								+ fieldName.substring(1);
						entityMethod = entityClass.getMethod("get"
								+ fieldNameUpper);
						//获取变量的值
						value = entityMethod.invoke(object);
						// 获取变量的类型
						fieldType = fields[i].getGenericType().toString();
						//是否是日期类型
						boolean isDateType = fieldType
								.equals(StaticValue.TIMESTAMP)
								|| fieldType.equals(StaticValue.DATE_SQL)
								|| fieldType.equals(StaticValue.DATE_UTIL);
						//填充value
						if (value != null && isDateType) {
							ps.setTimestamp(psIndex, Timestamp.valueOf(value
									.toString()));
						}else if(value == null){
							ps.setNull(psIndex, Types.VARCHAR);
						} else {
							ps.setObject(psIndex, value);
						}
					}
				}
			}
			// 执行SQL语句。
			int count = ps.executeUpdate();
			if (count == 1) {
				ps = conn.prepareStatement("values IDENTITY_VAL_LOCAL()");
				//执行sql
				rs = ps.executeQuery();
				if (rs.next()) {
					returnID = rs.getLong(1);
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally {
			//关闭数据库资源
			super.close(rs, ps, null);
		}
		return returnID;
	}
	
	private Long saveObjectReturnIDMySql(Connection conn, Object object)
			throws Exception {
		//插入后返回的主键id
		Long returnID = 0L;
		Class<?> entityClass = object.getClass();
		// 获取实体类字段与数据库字段映射的map集合
		Map<String, String> columns = getORMMap(entityClass);
		//表名
		String tableName = columns.get(entityClass.getSimpleName());
		// 拼接插入的SQL语句
		StringBuffer sqlSb = new StringBuffer("insert into ").append(tableName)
				.append("(");
		StringBuffer wenhao = new StringBuffer(") values(");
		//获取实体类的属性
		Field[] fields = entityClass.getDeclaredFields();
		String entityId = null;
		for (int i = 0; i < fields.length; i++) {
			if (!fields[i].getName().equals("serialVersionUID")) {
				if (columns.get(fields[i].getName()).equals(columns.get("tableId"))) {
					entityId = fields[i].getName();
				} else {
					sqlSb.append(columns.get(fields[i].getName())).append(",");
					wenhao.append("?").append(",");
				}
			}
		}
		// 去掉最后一个逗号。
		sqlSb.deleteCharAt(sqlSb.lastIndexOf(","));
		wenhao.deleteCharAt(wenhao.lastIndexOf(",")).append(")");
		sqlSb.append(wenhao);
		String sql = sqlSb.toString();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			//属性类型
			String fieldType = null;
			//属性名大写
			String fieldNameUpper = null;
			Method entityMethod = null;
			//属性值
			Object value = null;
			int psIndex = 0;
			for (int i = 0; i < fields.length; i++) {
				if (!fields[i].getName().equals("serialVersionUID")) {
					String fieldName = fields[i].getName();
					if (!fieldName.equals(entityId)) {
						psIndex++;
						fieldNameUpper = Character.toUpperCase(fieldName
								.charAt(0))
								+ fieldName.substring(1);
						entityMethod = entityClass.getMethod("get"
								+ fieldNameUpper);
						//获取属性值
						value = entityMethod.invoke(object);
						// 获取变量的类型
						fieldType = fields[i].getGenericType().toString();
						//是否是日期类型
						boolean isDateType = fieldType
								.equals(StaticValue.TIMESTAMP)
								|| fieldType.equals(StaticValue.DATE_SQL)
								|| fieldType.equals(StaticValue.DATE_UTIL);
						//填充value
						if (value != null && isDateType) {
							ps.setTimestamp(psIndex, Timestamp.valueOf(value
									.toString()));
						} else {
							ps.setObject(psIndex, value);
						}
					}
				}
			}
			// 执行SQL语句。
			int count = ps.executeUpdate();
			if (count == 1) {
				ps = conn.prepareStatement("select last_insert_id()");
				// 执行SQL语句。
				rs = ps.executeQuery();
				if (rs.next()) {
					returnID = rs.getLong(1);
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		} finally {
			//关闭数据库资源
			super.close(rs, ps, null);
		}
		return returnID;
	}
	
	public boolean CreateTableByField(Connection conn,
			List<String> fieldStrings, String tableName) throws Exception {
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
		    return this.CreateTableByFieldOracle(conn, fieldStrings, tableName);
	   	} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
	   		return this.CreateTableByFieldSqlServer(conn, fieldStrings, tableName);
		}
		return false;
	}
	
	private boolean CreateTableByFieldOracle(Connection conn,
			List<String> fieldStrings, String tableName) throws Exception {
		StringBuffer sqlcreateTable=new StringBuffer("create table ");
		sqlcreateTable.append(tableName).append("(").append("FDMID NUMBER(16),AIID NUMBER(16),");
		for(String fieldString:fieldStrings){
			String[] fieldproperty=fieldString.split(",");
			sqlcreateTable.append(fieldproperty[0]).append(" ");
			Integer type=Integer.parseInt(fieldproperty[1]);

			switch (type) {
			case 0:
				sqlcreateTable.append("VARCHAR2(");
				break;
			case 1:
				sqlcreateTable.append("NUMBER(");
				break;
			case 2:
				sqlcreateTable.append("varchar2(");
				break;
			case 3:
				sqlcreateTable.append("varchar2(");
				break;
			case 4:
				sqlcreateTable.append("varchar2(");
				break;
			case 5:
				sqlcreateTable.append("varchar2(");
				break;
			case 6:
				sqlcreateTable.append("NUMBER(");
				break;
			case 7:
				sqlcreateTable.append("NUMBER(");
				break;
			case 8:
				sqlcreateTable.append("varchar2(");
				break;
			default:
				sqlcreateTable.append("varchar2(");
				break;
			}
		  sqlcreateTable.append(fieldproperty[2]).append(")").append(" ");
		  Integer isnull=Integer.parseInt(fieldproperty[3]);
		  if(isnull==1){
			  sqlcreateTable.append(" not null,");
		  }else{
			  sqlcreateTable.append(",");
		  }
		}
		sqlcreateTable.deleteCharAt(sqlcreateTable.lastIndexOf(","));
		sqlcreateTable.append(") tablespace EMP_TABLESPACE pctfree 10 initrans 1 maxtrans 255 storage ( initial 64K minextents 1 maxextents unlimited ) ");
		StringBuffer sqlcreateTable1=new StringBuffer(" alter table ");
		sqlcreateTable1.append(tableName).append(" add constraint PK_").append(tableName).append(" primary key (FDMID) using index tablespace EMP_TABLESPACE pctfree 10 initrans 2 maxtrans 255 storage ( initial 64K minextents 1 maxextents unlimited ) ");
		StringBuffer sqlcreateTable2=new StringBuffer(" create sequence S_");
		sqlcreateTable2.append(tableName).append(" minvalue 1 maxvalue 999999999999999999999999999 start with 1 increment by 1 cache 20 ");
		StringBuffer sqlcreateTable3=new StringBuffer(" create or replace trigger TIG_");
		sqlcreateTable3.append(tableName).append("_QUEUE before insert on ").append(tableName).append(" for each row begin if(:new.FDMID is null) then select S_")
		.append(tableName).append(".NEXTVAL into :new.FDMID from dual;  end if;  end; ");
		boolean result=false;
		Statement ps=null;
		try
		{
			ps = conn.createStatement();
			ps.executeUpdate(sqlcreateTable.toString());
			ps.executeUpdate(sqlcreateTable1.toString());
			ps.executeUpdate(sqlcreateTable2.toString());
			ps.executeUpdate(sqlcreateTable3.toString());
			result=true;
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		}finally{
			if (ps != null)
			{
				ps.close();
			}
		}  
		return result;
	}
	
	private boolean CreateTableByFieldSqlServer(Connection conn,
			List<String> fieldStrings, String tableName) throws Exception {
		return false;
	}
	
	
	public boolean DeleteTableByTableName(Connection conn,String tableName) throws Exception {
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
		    return this.DeleteTableByTableNameOracle(conn, tableName);
	   	} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
	   		return this.DeleteTableByTableNameSqlServer(conn, tableName);
		}
		return false;
	}
	
	private boolean DeleteTableByTableNameOracle(Connection conn,String tableName) throws Exception {
		StringBuffer sqldeleteSeq=new StringBuffer("DROP SEQUENCE S_").append(tableName.toUpperCase());
		StringBuffer sqldeleteTig=new StringBuffer("DROP TRIGGER TIG_").append(tableName.toUpperCase()).append("_QUEUE");
		StringBuffer sqldeleteTable=new StringBuffer("DROP TABLE ").append(tableName.toUpperCase());
		StringBuffer sqltab = new StringBuffer("select count(*) as count from user_tables where table_name=upper('").append(tableName).append("')");
		StringBuffer sqlseq = new StringBuffer("select count(*) as count from user_objects where object_type='SEQUENCE' and object_name=upper('S_").append(tableName).append("')");
		StringBuffer sqltig = new StringBuffer("select count(*) as count from user_objects where object_type='TRIGGER' and object_name=upper('TIG_").append(tableName).append("_QUEUE')");
		Integer tseq = getInt("count", sqlseq.toString(), StaticValue.EMP_POOLNAME);
		Integer ttig = getInt("count", sqltig.toString(), StaticValue.EMP_POOLNAME);
		Integer ttab = getInt("count", sqltab.toString(), StaticValue.EMP_POOLNAME);
		boolean result=false;
		Statement ps=null;
		try
		{
			ps = conn.createStatement();
			if(tseq>0){
				ps.executeUpdate(sqldeleteSeq.toString());
			}
			if(ttig>0){
				ps.executeUpdate(sqldeleteTig.toString());
			}
			if(ttab>0){
				ps.executeUpdate(sqldeleteTable.toString());
			}
			result=true;
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
			throw e;
		}finally{
			if (ps != null)
			{
				ps.close();
			}
		} 
		return result;
	}
	private boolean DeleteTableByTableNameSqlServer(Connection conn,String tableName) throws Exception {
		return false;
	}
}
