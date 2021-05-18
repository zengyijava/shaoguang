/**
 * 
 */
package com.montnets.emp.employee.biz;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;

/**
 * @author chensj
 * 操作 机构  合并  调整 
 */
public class EmployeeDoDepBiz extends SuperBiz{
	
	/**
	 *   合并机构
	 * @param employeeDepList	被合并的子机构集合
	 * @param updateDep	需要被合并的机构
	 * @param srcDep	合并后的机构
	 * @return
	 */
	public String combineEmployeeDep(List<LfEmployeeDep> employeeDepList,LfEmployeeDep updateDep,LfEmployeeDep srcDep){
		String returnmsg = "";
		Connection conn = empTransDao.getConnection();
		try{
			empTransDao.beginTransaction(conn);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectByMap = new LinkedHashMap<String, String>();
			//修改 员工
			conditionMap.put("depId", String.valueOf(updateDep.getDepId()));
			objectByMap.put("depId", String.valueOf(srcDep.getDepId()));
			empTransDao.update(conn, LfEmployee.class, objectByMap, conditionMap);
			conditionMap.clear();
			objectByMap.clear();
			//修改关系连接
			conditionMap.put("depId", String.valueOf(updateDep.getDepId()));
			objectByMap.put("depId", String.valueOf(srcDep.getDepId()));
			empTransDao.update(conn, LfEmpDepConn.class, objectByMap, conditionMap);
			conditionMap.clear();
			objectByMap.clear();
			Integer srcdepLevel = srcDep.getDepLevel();
			if(employeeDepList != null && employeeDepList.size()>0){
				//取排序后 最后一个子机构，得到其级别
				LfEmployeeDep lastLevelEmpDep = employeeDepList.get(employeeDepList.size()-1);
				//合并 机构 的 最子 的 机构机构   
				Integer lastLevel = lastLevelEmpDep.getDepLevel();
				//当前合并机构 等级
				Integer curLevel = updateDep.getDepLevel();
				//合并机构的 等级 与 最后子级的 级别差
				Integer levelCount = lastLevel - curLevel;
				String srcDepPath = srcDep.getDeppath();
				//存放机构 ID对应其父级机构对象 
				LinkedHashMap<Long,LfEmployeeDep> depMap = new LinkedHashMap<Long, LfEmployeeDep>();
				LinkedHashMap<Long,String> deppathMap = new LinkedHashMap<Long, String>();
				//初始化的时候将 被合并的机构 与 合并的目的机构联系起来 
				//父机构ID 对应的 父机构对象
				depMap.put(updateDep.getDepId(), srcDep);
				//父机构ID对应的DEPPATH
				deppathMap.put(updateDep.getDepId(),srcDepPath);
				
				for(int a=1;a<=levelCount;a++){
					Integer lev = curLevel + a;
					//循环子机构
					for(LfEmployeeDep dep:employeeDepList){
						if(dep.getDepLevel() - lev == 0){
							//设置合并后机构的等级
							Integer level = dep.getDepLevel() - curLevel;
							dep.setDepLevel(level + srcdepLevel);
							//获取该机构ID所对应父机构对象
							LfEmployeeDep parentDep = depMap.get(dep.getParentId());
							if(parentDep != null){
								String tempPath = "";
								if(deppathMap.containsKey(dep.getParentId())){
									tempPath = deppathMap.get(dep.getParentId())+dep.getDepId()+"/";
									dep.setDeppath(tempPath);
									if(!deppathMap.containsKey(dep.getDepId())){
										deppathMap.put(dep.getDepId(), tempPath);
									}
								}
								dep.setParentId(parentDep.getDepId());
								if(!depMap.containsKey(dep.getDepId())){
									depMap.put(dep.getDepId(), dep);
								}
							}
							empTransDao.update(conn, dep);
						}
					}
					
				}
			}
			empTransDao.delete(conn,LfEmployeeDep.class, updateDep.getDepId().toString());
			empTransDao.commitTransaction(conn);
			returnmsg = "success";
		}catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			returnmsg = "fail";
			EmpExecutionContext.error(e,"合并机构出现失败！");
		}finally{
			empTransDao.closeConnection(conn);
		}
		return returnmsg;
	}
	
	public String ImproveEmployeeDep(List<LfEmployeeDep> employeeDepList,LfEmployeeDep updateDep,LfEmployeeDep srcDep){
		String returnmsg = "";
		Connection conn = empTransDao.getConnection();
		try{
			empTransDao.beginTransaction(conn);
			String b = srcDep.getDeppath();
			String a = "";
			String c = "";
			String d = "";
			for(LfEmployeeDep dep:employeeDepList){
				a = dep.getDeppath();
				c = a.split(updateDep.getDepId().toString())[1];
				if(c.equals("/")){
					dep.setParentId(srcDep.getDepId());
				}
				d = b+updateDep.getDepId()+c;
				dep.setDeppath(d);
				dep.setDepLevel(d.split("/").length);
				empTransDao.update(conn, dep);
			}
			empTransDao.commitTransaction(conn);
			returnmsg ="success";
		}catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			returnmsg = "fail";
			EmpExecutionContext.error(e,"调整机构失败！");
		}finally{
			empTransDao.closeConnection(conn);
		}
		return returnmsg;
	}
	
	/**
	 *   对机构排序做更新
	 * @param idsArray	机构ID
	 * @param sortidsArray	排序ID
	 * @param corpCode	企业编码
	 * @return	返回是否排序成功
	 */
	public String sortEmployeeDep(String[] idsArray,String[] sortidsArray,String corpCode){
		String returnmsg = "false";
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		Connection conn = empTransDao.getConnection();
		try{
			empTransDao.beginTransaction(conn);
			if(idsArray != null && idsArray.length>0){
				for(int i=0;i<idsArray.length;i++){
					if(sortidsArray[i] != null && "0".equals(sortidsArray[i])){
						objectMap.put("addType",null);
					}else{
						objectMap.put("addType",sortidsArray[i]);
					}
					conditionMap.put("depId", idsArray[i]);
					conditionMap.put("corpCode",corpCode);
					empTransDao.update(conn, LfEmployeeDep.class,objectMap, conditionMap);
					objectMap.clear();
					conditionMap.clear();
				}
			}
			empTransDao.commitTransaction(conn);
			returnmsg ="true";
		}catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"员工机构排序失败！");
			returnmsg = "false";
		}finally{
			empTransDao.closeConnection(conn);
		}
		return returnmsg;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
