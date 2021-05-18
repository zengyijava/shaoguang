package com.montnets.emp.client.biz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.client.vo.LfEnterpriseVo;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.AddrBookSpecialDAO;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.sysuser.LfSysuser;


public class EnterpriseBiz extends SuperBiz{

	private AddrBookSpecialDAO empSpecialDAO;

	public EnterpriseBiz() {
		empSpecialDAO = new AddrBookSpecialDAO();
	}

	/**
	 * 返回部门级别
	 * @param depCode
	 * @return
	 * @throws Exception
	 */
	public Integer returnDepsLevel(String depCode) throws Exception {
		Integer depLevel = 1;
		Integer maxLevel = depCode.length();
		try {
			int level = 0;
			int temp = 0;
			for (int i = 0; i < maxLevel; i += 2) {
				temp = i + 2;
				if (temp > maxLevel) {
					temp -= 1;
				}
				level = Integer.parseInt(depCode.substring(i, temp));
				if (level > 0 && i >= 2) {
					depLevel++;
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"返回部门级别失败！");
			throw e;
		}
		return depLevel;
	}

	/**
	 * 
	 * @param depType
	 * @param loginUserId
	 * @return
	 * @throws Exception
	 */
	public List<LfEnterpriseVo> getEnterpriseVos(Integer depType,
			Long loginUserId, String corpCode) throws Exception {

		List<LfEnterpriseVo> treeDepList = new ArrayList<LfEnterpriseVo>();

		LfEnterpriseVo treeDep = null;
		switch (depType) {
		case 0:

			LfClientDep lfClientDep = null;
			List<LfClientDep> lfClientDepList = null;

			lfClientDepList = this.getClientDeps(loginUserId, corpCode);

			for (int ci = 0; ci < lfClientDepList.size(); ci++) {
				treeDep = new LfEnterpriseVo();
				lfClientDep = lfClientDepList.get(ci);
				treeDep.setDepName(lfClientDep.getDepName());
				treeDepList.add(treeDep);
			}
			break;
		case 1:

			LfEmployeeDep lfEmployeeDep = null;
			List<LfEmployeeDep> lfEmployeeDepList = null;

			lfEmployeeDepList = empSpecialDAO.findDomEmployeeDepBySysuserIDOnlyAddr(
					loginUserId, corpCode);

			for (int ei = 0; ei < lfEmployeeDepList.size(); ei++) {
				treeDep = new LfEnterpriseVo();
				lfEmployeeDep = lfEmployeeDepList.get(ei);
				treeDep.setDepName(lfEmployeeDep.getDepName());
				treeDepList.add(treeDep);
			}

			break;
		default:
			break;
		}
		return treeDepList;
	}

	/**
	 * 查询客户机构
	 * @param loginUserId
	 * @return
	 * @throws Exception
	 */
	public List<LfClientDep> getClientDeps(Long loginUserId, String corpCode)
			throws Exception {
		List<LfClientDep> lfClientDepList = new ArrayList<LfClientDep>();
		try {
			lfClientDepList = empSpecialDAO.findDomClientDepBySysuserIDOnlyClientAddr(
					loginUserId, corpCode);
		} catch (Exception e) {

			EmpExecutionContext.error(e, "查询客户机构失败");
		}
		return lfClientDepList;
	}
	

	/**
	 * 用户ID查询员工部门
	 * @param loginUserId
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmployeeDeps(Long loginUserId, String corpcode)
			throws Exception {
		List<LfEmployeeDep> lfEmployeeDepList = new ArrayList<LfEmployeeDep>();

		try {
			lfEmployeeDepList = empSpecialDAO.findDomEmployeeDepBySysuserID(
					loginUserId, corpcode);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"通过用户ID查询客户机构失败！");
			throw e;
		}

		return lfEmployeeDepList;
	}

	public LfEmployeeDep getEmployeeNewDep(String depPcode, String corpCode) throws Exception{
		LfEmployeeDep parentDep = empDao.findObjectByID(LfEmployeeDep.class, Long.valueOf(depPcode));
		if (parentDep == null) {
			return null;
		}
		LfEmployeeDep newDep = new LfEmployeeDep();
		newDep.setParentId(parentDep.getDepId());
		return newDep;
	}
	public boolean addEmpDepConnInAddEmpDep(Long userId, LfEmployeeDep empDep) {
		boolean result = false;
		Connection conn = empTransDao.getConnection();
		try {
			empTransDao.beginTransaction(conn);
			Long depId = empTransDao.saveObjectReturnID(conn, empDep);
			empDep.setDepId(depId);
			empDep.setDeppath(empDep.getDeppath()+depId+"/");
			empTransDao.update(conn, empDep);

			empTransDao.commitTransaction(conn);
			result = true;
		} catch (Exception e) {
			EmpExecutionContext.error(e,"保存/修改客户机构失败！");
			result = false;
			empTransDao.rollBackTransaction(conn);
		} finally {
			empTransDao.closeConnection(conn);
		}
		return result;
	}

	/**
	 * 更新客户机构
	 * @param userId
	 * @param cliDep
	 * @return
	 */
	public boolean addCliDepConnInAddCliDep(Long userId, LfClientDep cliDep) {
		boolean result = false;
		Connection conn = empTransDao.getConnection();
		try {
			empTransDao.beginTransaction(conn);
			Long depId = empTransDao.saveObjectReturnID(conn, cliDep);
			cliDep.setDepId(depId);
			cliDep.setDeppath(cliDep.getDeppath()+depId+"/");
			empTransDao.update(conn, cliDep);
			empTransDao.commitTransaction(conn);
			result = true;
		} catch (Exception e) {
			result = false;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"更新客户机构出现异常！");
		} finally {
			empTransDao.closeConnection(conn);
		}
		return result;
	}
	
	
	/**
	 *   这里是做 子级查询的页面
	 * @param lfSysuser
	 * @param depCode
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTree(LfSysuser lfSysuser,String depCode) throws Exception {
		List<LfEmployeeDep> deps = null;
		try{
			deps = empSpecialDAO.getEmpSecondDepTree(lfSysuser, depCode);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取员工机构树信息出现异常！");
		}
		return deps;
	}


	/**
	 * 通过员工机构id查找树
	 * @param lfSysuser
	 * @param depIds 机构Id集合字符串
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId,String depId) throws Exception {
		List<LfEmployeeDep> deps = null;
		try{
			deps = empSpecialDAO.getEmpSecondDepTreeByUserIdorDepId( userId,depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取员工机构树信息出现异常！");
		}
		return deps;
	}
	
	/**
	 * 通过员工机构id查找树
	 * @param lfSysuser
	 * @param depIds 机构Id集合字符串
	 * @param corpCode 公司编码
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(String userId,String depId,String corpCode) throws Exception {
		List<LfEmployeeDep> deps = null;
		try{
			deps = empSpecialDAO.getEmpSecondDepTreeByUserIdorDepId( userId,depId,corpCode);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取员工机构树信息出现异常！");
		}
		return deps;
	}
	
	/**
	 * 通过客户机构id查找树
	 * @param lfSysuser
	 * @param depIds 机构Id集合字符串
	 * @return
	 * @throws Exception
	 */
	public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId,String depId) throws Exception {
		List<LfClientDep> deps = null;
		try{
			deps = empSpecialDAO.getCliSecondDepTreeByUserIdorDepId(userId,depId);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取客户机构树信息出现异常！");
		}
		return deps;
	}
	
	/**
	 * 通过客户机构id查找树
	 * @param lfSysuser
	 * @param depIds 机构Id集合字符串
	 * @return
	 * @throws Exception
	 */
	public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId,String depId,String corpCode) throws Exception {
		List<LfClientDep> deps = null;
		try{
			deps = empSpecialDAO.getCliSecondDepTreeByUserIdorDepId(userId,depId,corpCode);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取客户机构树信息出现异常！");
		}
		return deps;
	}
	
	/**
	 * 通过机构id查询有多少父级机构（级别限制）
	 * @throws Exception 
	 */
	public long getEmployeeDepLevByDepId(long depId) throws Exception
	{
		return empSpecialDAO.getEmployeeDepLevByDepId(depId);
	}
	
	/**
	 * 查找员工机构的总数
	 */
	public long getEmployeeDepCount(String corpCode) throws Exception 
	{
		return empSpecialDAO.getEmployeeDepCount(corpCode);
	}
	
	/**
	 * 通过机构id查询有多少父级机构（级别限制）
	 * @throws Exception 
	 */
	public long getClientDepLevByDepId(long depId) throws Exception
	{
		return empSpecialDAO.getClientDepLevByDepId(depId);
	}
	
	/**
	 * 查找客户机构的总数
	 */
	public long getClientDepCount(String corpCode) throws Exception 
	{
		return empSpecialDAO.getClientDepCount(corpCode);
	}
	/**
	 * 根据机构id获取所有子员工机构
	 */
//	public String getClientChildDepByParentID(Long depId) throws Exception{
//		return new GenericLfEmployeeVoDAO().getEmployeeChildDepByParentID(depId);
//	}
	
	
	
}
