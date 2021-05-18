package com.montnets.emp.employee.biz;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SendBirthDateBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.employee.dao.EmployeeDao;
import com.montnets.emp.employee.vo.AddrBookPermissionsVo;
import com.montnets.emp.employee.vo.LfEmployeeTypeVo;
import com.montnets.emp.employee.vo.LfEmployeeVo;
import com.montnets.emp.employee.vo.LfEnterpriseVo;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.employee.LfEmployeeType;
import com.montnets.emp.entity.group.LfList2gro;
import com.montnets.emp.entity.segnumber.PbServicetype;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;
/**
 *   处理员工各种操作
 * @author Administrator
 *
 */
public class EmployeeBookBiz extends SuperBiz{
	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~员工权限绑定操作  begin
	 */
	
	/**
	 * 员工权限 find查询
	 * @param depType
	 * @param loginUserId
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public List<LfEnterpriseVo> getEnterpriseVos(Integer depType,
			Long loginUserId, String corpCode) throws Exception {
		List<LfEnterpriseVo> treeDepList = new ArrayList<LfEnterpriseVo>();
		LfEnterpriseVo treeDep = null;
		LfEmployeeDep lfEmployeeDep = null;
		List<LfEmployeeDep> lfEmployeeDepList = null;
		lfEmployeeDepList = new EmployeeDao().findDomEmployeeDepBySysuserIDOnlyAddr(loginUserId, corpCode);
		if(lfEmployeeDepList != null && lfEmployeeDepList.size()>0){
			for (int ei = 0; ei < lfEmployeeDepList.size(); ei++) {
				treeDep = new LfEnterpriseVo();
				lfEmployeeDep = lfEmployeeDepList.get(ei);
				treeDep.setDepName(lfEmployeeDep.getDepName());
				treeDepList.add(treeDep);
			}
		}
		return treeDepList;
	}
	
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getAllSysusers(Long userId) throws Exception {
		List<LfSysuser> lfSysuserList = new ArrayList<LfSysuser>();
		try {
			//查询
			if (null != userId){
				lfSysuserList = new EmployeeDao().findDomUserBySysuserID(userId.toString());
			}
		} catch (Exception e) {
			//异常
			EmpExecutionContext.error(e,"员工通讯录获取所有操作员出现异常！");
		}
		return lfSysuserList;
	}
	
	
	
	/**
	 *  操作员绑定员工机构权限
	 * @param loginUserId	登录操作员ID
	 * @param depPath	机构路径
	 * @param corpCode	企业编码
	 * @param name	操作员名称
	 * @param pageInfo	分页信息
	 * @return
	 * @throws Exception
	 */
	public List<AddrBookPermissionsVo> getEmpBindPermissionsList(Long loginUserId,String depPath, String corpCode,String name,PageInfo pageInfo) throws Exception {
		return new EmployeeDao().getPermissionsVo(loginUserId,depPath, corpCode,name,pageInfo);
	}
	
	
	/**
	 * 过滤掉企业管理员删除自己的顶级部门权限
	 * @param conn_id
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public boolean filtAdmin(long conn_id, String corpCode) throws Exception
	{
		boolean boo = false;
		try
		{
			boo = new EmployeeDao().filtAdmin(conn_id, corpCode);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"员工通讯录过滤掉企业管理员删除自己的顶级部门权限出现异常！");
		}
		return boo;
	}
	
	
	
	/**
	 * 
	 * @param loginUserId
	 * @param addrBookPermissionsVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getEmpUnBindPermissionsSysuserList(Long loginUserId, String corpCode,AddrBookPermissionsVo addrBookPermissionsVo, PageInfo pageInfo) throws Exception {
		return new EmployeeDao().getEmpUnBindPermissionsSysuserList(loginUserId, corpCode,addrBookPermissionsVo, pageInfo);
	}
	
	
	
	public boolean isEmployeeDepCustomAdd()
	{
		boolean result = false;
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("addType", "0");
			List<LfEmployeeDep> employeesList = empDao.findListByCondition(
					LfEmployeeDep.class, conditionMap, null);
			if (employeesList != null && employeesList.size() > 0)
			{
				result = true;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询机构相关信息异常");
		}
		return result;
	}
	
	
	
	
	
	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~员工权限绑定操作  END
	 */
	
	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~职员操作  begin
	 */
	/**
	 *   查询find
	 */
	public List<LfEmployeeTypeVo> getEmployeeTypeVo(long uid, String corpCode,
			PageInfo pageInfo) throws Exception
	{
		List<LfEmployeeTypeVo> employeeVosList;
		try
		{
			employeeVosList = new EmployeeDao().findEmployeeTypeVo(uid, corpCode,
					pageInfo);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获得分页时的员工通讯录类型异常");
			throw e;
		}
		return employeeVosList;
	}
	
	/**
	 * 获得所有员工通讯录类型
	 * @param uid
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	
	public List<LfEmployeeTypeVo> getAllEmployeeTypeVo(long uid, String corpCode)
	throws Exception
	{
		List<LfEmployeeTypeVo> employeeVosList;
		try
		{
			employeeVosList = new EmployeeDao().findAllEmployeeTypeVo(uid,
					corpCode);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获得所有员工通讯录类型异常");
			throw e;
		}
	
		return employeeVosList;
	}
	
	/**
	 *  删除职位
	 * @param corpCode	企业编码
	 * @param id	职位ID
	 * @return
	 * @throws Exception
	 */
	public boolean delEmployeeTypeById(String corpCode, int id)throws Exception
	{
		boolean b = new EmployeeDao().delEmployeeTypeVo(corpCode, id);
		return b;
	}
	
	
	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~员工机构操作
	 */
	
	public List<LfEmployeeVo> findEmployeeVoByDepIds(Long loginUserId,
			String corpCode, LfEmployeeVo employeeVo, PageInfo pageInfo)
			throws Exception
	{
		List<LfEmployeeVo> employeeVosList;
		try
		{
			if (pageInfo == null)
			{
				/*employeeVosList = lfEmployeeVoDAO.findEmployeeVoByDepIds(
						loginUserId, corpCode, employeeVo);*/
				employeeVosList = new EmployeeDao().findEmployeeVoByDepIds(
						loginUserId, corpCode, employeeVo);
			} else
			{
				/*employeeVosList = lfEmployeeVoDAO.findEmployeeVoByDepIds(
						loginUserId, corpCode, employeeVo, pageInfo);*/
				employeeVosList = new EmployeeDao().findEmployeeVoByDepIds(
						loginUserId, corpCode, employeeVo, pageInfo);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"通过机构IDs获取员工数据异常");
			throw e;
		}
		return employeeVosList;
	}
	
	
	public boolean updateEmployee(LfEmployee employee)
	{
		boolean result = false;
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			empTransDao.update(conn, employee);
			this.updateSysuserInEmployee(conn, employee);
			empTransDao.commitTransaction(conn);
			result = true;
		} catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"更新员工失败！");
		} finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 
	 * @param conn
	 * @param employee
	 * @return
	 * @throws Exception
	 */
	private boolean updateSysuserInEmployee(Connection conn, LfEmployee employee)
			throws Exception
	{
		boolean result = false;
		try
		{
			/*AddrBookBaseBiz addrbookBaseBiz = new AddrBookBaseBiz();
			LfSysuser sysuser = addrbookBaseBiz.getObjByGuid(LfSysuser.class,
					employee.getGuId());*/
			BaseBiz baseBiz = new BaseBiz();
			LfSysuser sysuser = baseBiz.getByGuId(LfSysuser.class,
					employee.getGuId());
			if (sysuser == null)
			{
				return false;
			}
			sysuser.setEMail(employee.getEmail());
			sysuser.setMsn(employee.getMsn());
			sysuser.setQq(employee.getQq());
			sysuser.setName(employee.getName());
			sysuser.setSex(employee.getSex());
			sysuser.setMobile(employee.getMobile());
			sysuser.setBirthday(employee.getBirthday());
			sysuser.setOph(employee.getOph());
			sysuser.setComments(employee.getCommnets());
			result = empTransDao.update(conn, sysuser);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"更新员工通讯录中的操作员对象出现异常！");
		}
		return result;
	}
	
	/**
	 *   删除员工
	 * @param guIds
	 * @return
	 */
	public Integer delEmployeeByGuid(String guIds)
	{
		Integer result = null;
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("guId", guIds);
			result = empTransDao.delete(conn, LfEmployee.class, conditionMap);
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();

			//解决删除员工，禁用操作员问题
			// objectMap.put("userState", "0");

			objectMap.put("userType", "0");
			conditionMap.clear();
			conditionMap.put("guId", guIds);
			empTransDao.update(conn, LfSysuser.class, objectMap, conditionMap);
			conditionMap.clear();
			conditionMap.put("l2gType", "0");
			conditionMap.put("guId", guIds);
			empTransDao.delete(conn, LfList2gro.class, conditionMap);
			empTransDao.commitTransaction(conn);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"删除员工异常");
			empTransDao.rollBackTransaction(conn);
		} finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}

	
	/**
	 * 删除机构，删除员工 ，删除关联表
	 * 
	 * @param depCode
	 *            机构编码
	 * @param corpCode
	 *            企业编码
	 * @param userId
	 *            用户ID
	 * @return
	 */

	public Integer delEmployeeAddrDep(String depId, String corpCode, long userId)
	{
		Integer intResult = -1;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("parentId", depId);
			conditionMap.put("corpCode", corpCode);
			List<LfEmployeeDep> employeesList = empDao.findListByCondition(
					LfEmployeeDep.class, conditionMap, null);

			if (employeesList != null && employeesList.size() > 0)
			{ 
				// 如果该机构下还有机构，不允许删除
				intResult = 0;
				return intResult;
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "查询员工机构出现异常！");
		}
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			conditionMap.clear();
			conditionMap.put("depId", depId);
			// 删除机构
			int delNum = empTransDao.delete(conn, LfEmployeeDep.class, depId);
			// 删除生日成员表里面的员工成员
			new SendBirthDateBiz().deleteAddrBirthMemberByDepId(depId,
					corpCode, 1, conn);
			// 删除机构下员工
			empTransDao.delete(conn, LfEmployee.class, conditionMap);
			// 删除机构绑定关系
			empTransDao.delete(conn, LfEmpDepConn.class, conditionMap);
			// 删除员工下的群组关联
			/*lfEmployeeVoDAO.delUdgEmployeeByDepId(conn, depId);*/
			new EmployeeDao().delUdgEmployeeByDepId(conn, depId);
			if (delNum > 0)
			{
				intResult = 1;
			} else
			{
				intResult = -1;
			}
			empTransDao.commitTransaction(conn);
		} catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"删除员工机构出现异常！");
		} finally
		{
			empTransDao.closeConnection(conn);
		}
		return intResult;
	}
	
	
	
	/**
	 * 
	 * @param guIds
	 * @param depId
	 * @return
	 */
	public boolean changeEmployeeDep(String guIds, String depId)
	{
		boolean result = false;
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("guId", guIds);
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("depId", depId);
			result = empTransDao.update(conn, LfEmployee.class, objectMap,
					conditionMap);
			empTransDao.commitTransaction(conn);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"更新员工信息异常");
			empTransDao.rollBackTransaction(conn);
		} finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	
	/**
	 * 通过员工机构id查找树
	 * @param userId
	 * @param depId 机构Id
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId,String depId) throws Exception {
		List<LfEmployeeDep> deps = null;
		try{
			deps = new EmployeeDao().getEmpSecondDepTreeByUserIdorDepId( userId,depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取员工机构树出现异常！");
		}
		return deps;
	}
	/**
	 * 通过员工机构id查找树
	 * @param sysuser
	 * @param depId 机构Id
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserorDepId(LfSysuser sysuser,String depId) throws Exception {
		List<LfEmployeeDep> deps = null;
		try{
			deps = new EmployeeDao().getEmpSecondDepTreeByUserorDepId(sysuser, depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取员工机构树出现异常！");
		}
		return deps;
	}

	public LfEmployeeDep getEmployeeNewDep(String depPcode, String corpCode) throws Exception{
		LfEmployeeDep parentDep = empDao.findObjectByID(LfEmployeeDep.class, Long.parseLong(depPcode));
		if (parentDep == null) {
			return null;
		}
		LfEmployeeDep newDep = new LfEmployeeDep();
		newDep.setParentId(parentDep.getDepId());
		return newDep;
	}
	
	
	/**获取所有的 号段
	 * 
	 * @return
	 * @throws Exception
	 */
	public String[] getHaoduan() throws Exception
	{
		String[] haoduans = new String[3];
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			List<PbServicetype> haoduanList = empDao.findListByCondition(
					PbServicetype.class, conditionMap, null);
			for (int i = 0; i < haoduanList.size(); i++)
			{
				PbServicetype pbSer = haoduanList.get(i);
				if (pbSer.getSpisuncm() == 0)
				{
					haoduans[0] = pbSer.getServiceno();
				} else if (pbSer.getSpisuncm() == 1)
				{
					haoduans[1] = pbSer.getServiceno();
				} else if (pbSer.getSpisuncm() == 21)
				{
					haoduans[2] = pbSer.getServiceno();
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取所有的号段异常");
			throw e;
		}
		return haoduans;
	}

	/**
	 *  检测号码
	 * @param mobile
	 * @param haoduan
	 * @return
	 * @throws Exception
	 */
	public int checkMobile(String mobile, String[] haoduan) throws Exception
	{
		if (mobile.length() != 11)
			return 0;
		for (int k = mobile.length(); --k >= 0;)
		{
			if (!Character.isDigit(mobile.charAt(k)))
			{
				return 0;
			}
		}

		String number = haoduan[0] + "," + haoduan[1] + "," + haoduan[2];
		if (number.replace(mobile.substring(0, 3), "").length() == number
				.length())
		{
			return 0;
		}
		return 1;
	}

	
	/**
	 * 查找员工机构的总数
	 */
	public long getEmployeeDepCount(String corpCode) throws Exception 
	{
		return new EmployeeDao().getEmployeeDepCount(corpCode);
	}
	
	/**
	 * 通过机构id查询有多少父级机构（级别限制）
	 * @throws Exception 
	 */
	public long getEmployeeDepLevByDepId(long depId) throws Exception
	{
		return new EmployeeDao().getEmployeeDepLevByDepId(depId);
	}
	
	public boolean addEmpDepConnInAddEmpDep(Long userId, LfEmployeeDep empDep) {
		boolean result = false;
		Connection conn = empTransDao.getConnection();
		try {
			empTransDao.beginTransaction(conn);
			//empTransDao.save(conn, empDep);
			Long depId = empTransDao.saveObjectReturnID(conn, empDep);
			empDep.setDepId(depId);
			empDep.setDeppath(empDep.getDeppath()+depId+"/");
			empTransDao.update(conn, empDep);
			/*
			 * LfEmpDepConn empDepConn = new LfEmpDepConn();
			 * empDepConn.setUserId(userId);
			 * empDepConn.setDepCodeThird(empDep.getDepCode());
			 * empTransDao.save(conn, empDepConn);
			 */

			empTransDao.commitTransaction(conn);
			result = true;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"更新机构相关信息异常");
			result = false;
			empTransDao.rollBackTransaction(conn);
		} finally {
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 更新员工职位
	 * @description    
	 * @param employee
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-5-19 下午05:54:08
	 */
	public boolean updateEmployeePosition(LfEmployeeType type,String name)
	{
		boolean result = false;
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			new EmployeeDao().updatePosition(conn, type, name);
			type.setName(name);
			empTransDao.update(conn, type);
			empTransDao.commitTransaction(conn);
			result = true;
		} catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"更新员工职位失败！");
		} finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	public List<LfEmployee> getAllEmployees(String lgcorpcode) throws SQLException{
		return new EmployeeDao().getAllEmployees(lgcorpcode);
	}
	
	public int getEmployeesCount(String depId) throws SQLException{
		return new EmployeeDao().getEmployeesCount(depId);
	}
	
	/**
	 *  相同的机构名称或机构编码记录数
	 * @description    
	 * @param depName  机构名称
	 * @param depCode	机构编码
	 * @return       	记录数		 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-10-29 下午12:06:07
	 */
	public List<DynaBean> sameDepNameOrDepCode(String depName, String depCode, String scode, String corpcode)
	{
		return new EmployeeDao().sameDepNameOrDepCode(depName, depCode, scode, corpcode);
	}
}
