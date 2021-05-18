package com.montnets.emp.common.dao;

public interface IDataAccessDriver
{
	public  IEmpDAO getEmpDAO();
	
	public  IEmpTransactionDAO getEmpTransDAO();
	
	public  IGenericDAO getGenericDAO();
}
