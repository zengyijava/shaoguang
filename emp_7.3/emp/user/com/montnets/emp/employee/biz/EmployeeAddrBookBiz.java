package com.montnets.emp.employee.biz;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SendBirthDateBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.employee.dao.GenericLfEmployeeVoDAO;
import com.montnets.emp.employee.vo.LfEmployeeTypeVo;
import com.montnets.emp.employee.vo.LfEmployeeVo;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfList2gro;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @author Administrator
 * 
 */
public class EmployeeAddrBookBiz extends SuperBiz
{

	private GenericLfEmployeeVoDAO lfEmployeeVoDAO;


	public EmployeeAddrBookBiz()
	{
		lfEmployeeVoDAO = new GenericLfEmployeeVoDAO();
	}

	/**
	 * 删除员工
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
			EmpExecutionContext.error(e,"删除员工异常！");
			empTransDao.rollBackTransaction(conn);
		} finally
		{
			empTransDao.closeConnection(conn);
		}
		return result;
	}

	// 删除部门时，删除群组表中关联员工信息
	public Integer delDepEmployeeByGuid(Connection conn, String guIds)
	{
		Integer result = null;
		// Connection conn = empTransDao.getConnection();
		try
		{
			// empTransDao.beginTransaction(conn);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("l2gType", "0");
			conditionMap.put("guId", guIds);
			empTransDao.delete(conn, LfList2gro.class, conditionMap);
			// empTransDao.commitTransaction(conn);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"删除群组表中关联员工信息异常");
		} 
		return result;
	}

	/**
	 * 获得员工相关信息异常
	 * @param loginUserId
	 * @param employeeVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfEmployeeVo> getEmployeeVo(Long loginUserId, String corpCode,
			LfEmployeeVo employeeVo, PageInfo pageInfo) throws Exception
	{
		List<LfEmployeeVo> employeeVosList;
		try
		{
			employeeVosList = lfEmployeeVoDAO.findEmployeeVo(loginUserId,
					corpCode, employeeVo, pageInfo);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获得员工相关信息异常！");
			throw e;
		}
		return employeeVosList;
	}

	/**
	 * 通过机构ids查询通讯录信息
	 * 
	 * @param loginUserId
	 * @param employeeVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
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
				employeeVosList = lfEmployeeVoDAO.findEmployeeVoByDepIds(
						loginUserId, corpCode, employeeVo);
			} else
			{
				employeeVosList = lfEmployeeVoDAO.findEmployeeVoByDepIds(
						loginUserId, corpCode, employeeVo, pageInfo);
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"通过机构ids查询通讯录信息异常");
			throw e;
		}
		return employeeVosList;
	}

	synchronized public Integer addEmployeeByProcedure(String depCode,
			String bizCode, String employeeCode) throws Exception
	{
		Integer result = -1;
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			result = empTransDao.saveEmpolyeeByProc(conn, depCode, bizCode,
					employeeCode);
			empTransDao.commitTransaction(conn);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"保存员工相关信息异常");
			result = -1;
			empTransDao.rollBackTransaction(conn);
			throw e;
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
		Connection conn = empTransDao.getConnection();
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
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
			lfEmployeeVoDAO.delUdgEmployeeByDepId(conn, depId);
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
			EmpExecutionContext.error(e,"删除员工机构出现异常！");
			empTransDao.rollBackTransaction(conn);
		} finally
		{
			empTransDao.closeConnection(conn);
		}
		return intResult;
	}

	/**
	 * 
	 * @param conn
	 * @param depCode
	 * @return
	 */
	private int delEmpDepConnByCode(Connection conn, String depCode)
	{

		int delCount = 0;
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("depCodeThird", depCode);
			delCount = empTransDao.delete(conn, LfEmpDepConn.class,
					conditionMap);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"删除员工与员工机构权限表异常");
		}
		return delCount;
	}

	/**
	 * 
	 * @return
	 */
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
			EmpExecutionContext.error(e,"判断是否新增的员工异常");
		}
		return result;
	}

	/**
	 * 
	 * @param employee
	 * @return
	 */
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
			EmpExecutionContext.error(e,"更新员工异常");
			empTransDao.rollBackTransaction(conn);
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
			LfSysuser sysuser = this.getObjByGuid(LfSysuser.class,
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
			EmpExecutionContext.error(e,"更新用户信息异常");
			throw e;
		}
		return result;
	}

	public List<LfEmployeeTypeVo> getEmployeeTypeVo(long uid, String corpCode,
			PageInfo pageInfo) throws Exception
	{
		List<LfEmployeeTypeVo> employeeVosList;
		try
		{
			employeeVosList = lfEmployeeVoDAO.findEmployeeTypeVo(uid, corpCode,
					pageInfo);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询员工类型异常");
			throw e;
		}

		return employeeVosList;
	}

	public List<LfEmployeeTypeVo> getAllEmployeeTypeVo(long uid, String corpCode)
			throws Exception
	{
		List<LfEmployeeTypeVo> employeeVosList;
		try
		{
			employeeVosList = lfEmployeeVoDAO.findAllEmployeeTypeVo(uid,
					corpCode);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询所有员工类型异常");
			throw e;
		}

		return employeeVosList;
	}

	public boolean addEmployeeTypeVo(long uid, String name, String corp_code)
			throws Exception
	{
		boolean b = lfEmployeeVoDAO.addEmployeeTypeVo(uid, name, corp_code);
		return b;
	}

	public boolean delEmployeeTypeById(String corpCode, int id)
			throws Exception
	{
		boolean b = lfEmployeeVoDAO.delEmployeeTypeVo(corpCode, id);
		return b;
	}

	public boolean updEmployeeTypeById(long uid, String corpCode, int id,
			String typename) throws Exception
	{
		boolean b = lfEmployeeVoDAO.updEmployeeTypeVo(uid, corpCode, id,
				typename);
		return b;
	}

	// 过滤掉企业管理员删除自己的顶级部门权限
	public boolean filtAdmin(long conn_id, String corpCode) throws Exception
	{
		boolean boo;
		try
		{
			boo = lfEmployeeVoDAO.filtAdmin(conn_id, corpCode);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询员工与员工机构权限表异常");
			throw e;
		}
		return boo;
	}

	/**
	 * 获取员工机构的所有子级机构ID的字符串
	 * 
	 * @param depId
	 *            需要查找的员工机构ID
	 * @return 所有子级机构ID的字符串
	 * @throws Exception
	 */
	public String getChildEmpDepByParentID(Long depId) throws Exception
	{
		String depIds = "";
		if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
		{
			depIds = lfEmployeeVoDAO.executeProcessReutrnCursorOfOracle(
					StaticValue.EMP_POOLNAME, "GETEMPDEPCHILDBYPID",
					new Integer[]
					{1,Integer.valueOf(depId.toString())});
		} else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
		{
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			/*
			 * depIds = lfEmployeeVoDAO.executeProcessReutrnCursorOfMySql(
			 * StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID", new
			 * Integer[]{1,Integer.valueOf(depId.toString())});
			 */
			depIds = lfEmployeeVoDAO.getEmpChildIdByDepId(depId.toString());
		} else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{

		}

		return depIds;
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
	 * 根据guid获取对象
	 * @param <T>
	 * @param entityClass
	 * @param guId
	 * @return 返回对象
	 */
	public <T> T getObjByGuid(Class<T> entityClass, Long guId) {
		//不允许为空
		if(entityClass == null || guId == null){
			//返回
			return null;
		}
		
		T obj = null;
		try{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//guid
			conditionMap.put("guId", String.valueOf(guId));
			//查询
			List<T> objsList = empDao.findListByCondition(entityClass, conditionMap, null);
			if(objsList != null && objsList.size() > 0){
				//返回结果
				obj = objsList.get(0);
			}
			
		}catch(Exception e){
			EmpExecutionContext.error(e,"通过guid获取对象出现异常！");
		}
		//返回
		return obj;
	}
	
}
