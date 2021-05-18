/**
 * 
 */
package com.montnets.emp.employee.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.dao.SuperDAO;

/**
 * @author chensj
 *
 */
public class EmpDepDao extends SuperDAO{
	
	public  boolean judgeiSson(String srcpath,String tagid){
		String sql = "SELECT dep_id from LF_EMPLOYEE_DEP where DEP_PATH like '"+srcpath+"%'";
		String s = "select dep_name from LF_EMPLOYEE_DEP where dep_id in ("+sql+") and dep_id="+tagid;
		List<DynaBean> list = getListDynaBeanBySql(s);
		if(list!=null && list.size()>0){
			return true;
		}
		return false;
	}
	
	public  boolean judgeiSson(String srcpath,String tagid, String corpCode){
		String sql = "SELECT dep_id from LF_EMPLOYEE_DEP where CORP_CODE = '"+ corpCode +"' and DEP_PATH like '"+srcpath+"%'";
		String s = "select dep_name from LF_EMPLOYEE_DEP where CORP_CODE = '"+ corpCode +"' and dep_id in ("+sql+") and dep_id="+tagid;
		List<DynaBean> list = getListDynaBeanBySql(s);
		if(list!=null && list.size()>0){
			return true;
		}
		return false;
	}
}
