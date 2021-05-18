package com.montnets.emp.common.dao.impl;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IDataAccessDriver;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.IGenericDAO;

public class DataAccessDriver implements IDataAccessDriver
{

	public IEmpDAO getEmpDAO()
	{
		IEmpDAO empDao=null;
		try
		{
			String path="com.montnets.emp.common.dao."+StaticValue.getDaoPackage()+"."+StaticValue.getDaoClass()+"EmpDAO";
			empDao=(IEmpDAO)Class.forName(path).newInstance();
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
		}
		return empDao;
	}

	public IEmpTransactionDAO getEmpTransDAO()
	{
		IEmpTransactionDAO empTransDao=null;
		try
		{
			String path="com.montnets.emp.common.dao."+StaticValue.getDaoPackage()+"."+StaticValue.getDaoClass()+"EmpTransactionDAO";
			empTransDao=(IEmpTransactionDAO)Class.forName(path).newInstance();
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
		}
		return empTransDao;
	}

	public IGenericDAO getGenericDAO()
	{
		IGenericDAO genericDao=null;
		try
		{
			String path="com.montnets.emp.common.dao."+StaticValue.getDaoPackage()+"."+StaticValue.getDaoClass()+"GenericDAO";
			genericDao=(IGenericDAO)Class.forName(path).newInstance();
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"DAO方法"+Thread.currentThread().getStackTrace()[1].getMethodName()+"执行失败！");
		}
		return genericDao;
	}
	
}
