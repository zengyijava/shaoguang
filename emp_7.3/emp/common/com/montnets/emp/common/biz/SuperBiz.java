package com.montnets.emp.common.biz;

import com.montnets.emp.common.dao.IDataAccessDriver;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;


/**
 * 
 * @author Administrator
 *
 */
public class SuperBiz {
	protected IEmpDAO empDao ;
	protected IEmpTransactionDAO empTransDao ;
	
	public SuperBiz(){
		IDataAccessDriver dataAccessDriver=new DataAccessDriver();
		empDao =dataAccessDriver.getEmpDAO();
		empTransDao =dataAccessDriver.getEmpTransDAO();
	}
}
