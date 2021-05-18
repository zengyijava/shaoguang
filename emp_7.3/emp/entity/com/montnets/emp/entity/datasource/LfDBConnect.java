/**
 *
 */
package com.montnets.emp.entity.datasource;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-21 下午02:40:07
 * @description
 */

public class LfDBConnect implements java.io.Serializable
{

	/**
	 *
	 */
	private static final long serialVersionUID = -6016318798065686453L;
	/**
	 *
	 */
	//private static final long serialVersionUID = -5918956284479517951L;
	//库连接ID
	private Long dbId;
	//数据连接说明
	private String dbconName;
	//服务器IP
	private String dbconIP;
	//数据库类型
	private String dbType;
	//实例名
	private String serviceName;
	//数据库名
	private String dbName;
	//端口号
	private String port;
	//用户
	private String dbUser;
	//密码
	private String dbPwd;
	//连接字符串
	private String conStr;
	//描述
	private String comments;
	//ORACLEL连接名称类型(0 service_name，1 SID)
	private Integer dbconnType;
	//企业编码
	private String corpCode;

	public LfDBConnect(){}



	public String toString() {
		//增加content字段，用于页面处理
		String json="{dbid:%d,dbname:'%s'}";

		return String.format(json,this.getDbId(), this.getDbconName());
	}

	public Long getDbId()
	{
		return dbId;
	}

	public void setDbId(Long dbId)
	{
		this.dbId = dbId;
	}

	public String getDbconName()
	{
		return dbconName;
	}

	public void setDbconName(String dbconName)
	{
		this.dbconName = dbconName;
	}

	public String getDbconIP()
	{
		return dbconIP;
	}

	public void setDbconIP(String dbconIP)
	{
		this.dbconIP = dbconIP;
	}

	public String getDbType()
	{
		return dbType;
	}

	public void setDbType(String dbType)
	{
		this.dbType = dbType;
	}

	public String getServiceName()
	{
		return serviceName;
	}

	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}

	public String getDbUser()
	{
		return dbUser;
	}

	public void setDbUser(String dbUser)
	{
		this.dbUser = dbUser;
	}

	public String getDbPwd()
	{
		return dbPwd;
	}

	public void setDbPwd(String dbPwd)
	{
		this.dbPwd = dbPwd;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public Integer getDbconnType()
	{
		return dbconnType;
	}

	public void setDbconnType(Integer dbconnType)
	{
		this.dbconnType = dbconnType;
	}

	public String getDbName()
	{
		return dbName;
	}

	public void setDbName(String dbName)
	{
		this.dbName = dbName;
	}

	public String getConStr()
	{
		return conStr;
	}

	public void setConStr(String conStr)
	{
		this.conStr = conStr;
	}



	public String getCorpCode()
	{
		return corpCode;
	}



	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}


}
