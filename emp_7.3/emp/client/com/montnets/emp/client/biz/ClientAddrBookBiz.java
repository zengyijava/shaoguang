package com.montnets.emp.client.biz;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.PropertyUtils;

import com.montnets.emp.client.dao.GenericLfClientVoDAO;
import com.montnets.emp.client.vo.LfClientVo;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.birthwish.LfBirthdayMember;
import com.montnets.emp.entity.client.LfCliDepConn;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClient5Pro;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.client.LfClientDepSp;
import com.montnets.emp.entity.client.LfClientMultiPro;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfList2gro;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @author Administrator
 * 
 */
public class ClientAddrBookBiz extends SuperBiz
{
	// 数据库操作dao
	private GenericLfClientVoDAO lfClientVoDAO;

	public ClientAddrBookBiz()
	{
		//初始化数据库操作dao
		lfClientVoDAO = new GenericLfClientVoDAO();
	}

	public Integer getStrLength(String name){
		if(name == null || name == ""){
			return 0;
		}
		int length = name.length();
		int tempLength = 0;
		String  regex = "[\\u4e00-\\u9fa5]";
		try {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(name);
			while (matcher.find()) {
				tempLength += 1;
			}
			//获得非汉字个数
			int nonChineseNum = length - tempLength;
			length = nonChineseNum+tempLength*2;
		}catch(Exception e){
			EmpExecutionContext.error(e,"正则表达式匹配异常");
		}
		return length;
	}

	/**
	 * 根据guid删除客户机构信息
	 * 
	 * @param guIds
	 * @return
	 */
	public Integer delClientByGuid(String guIds)
	{
		//结果
		Integer result = null;
		// 获取一个连接
		Connection conn = empTransDao.getConnection();
		try
		{
			// 开启一个事务
			empTransDao.beginTransaction(conn);
			// 根据过滤条件删除客户机构信息
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("guId", guIds);
			//执行删除客户并返回结果
			result = empTransDao.delete(conn, LfClient.class, conditionMap);
			conditionMap.clear();
			// 根据过滤条件删除绑定信息
			conditionMap.put("l2gType", "1");
			conditionMap.put("guId", guIds);
			//删除群组记录并返回结果
			empTransDao.delete(conn, LfList2gro.class, conditionMap);
			// 提交事务
			empTransDao.commitTransaction(conn);
		} catch (Exception e)
		{
			//异常回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"删除客户机构信息失败！");
		} finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		//返回结果
		return result;
	}

	// 删除部门时，删除群组表中关联客户信息
	public Integer delDepClientByGuid(Connection conn, String guIds)
	{
		//结果
		Integer result = null;
		try
		{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("l2gType", "1");
			conditionMap.put("guId", guIds);
			//执行删除
			empTransDao.delete(conn, LfList2gro.class, conditionMap);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"删除客户群组中的关联信息出现异常！");
		}
		//返回结果
		return result;
	}

	/**
	 * 得到客户机构vo
	 * 
	 * @param loginUserId
	 * @param clientVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfClientVo> getClientVo(Long loginUserId, LfClientVo clientVo,
			String corpcode, PageInfo pageInfo) throws Exception
	{
		//客户对象集合
		List<LfClientVo> clientList;
		try
		{
			//获取客户记录
			clientList = lfClientVoDAO.findClientVo(loginUserId, corpcode,
					clientVo, pageInfo);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取客户记录出现异常！");
			throw e;
		}
		//返回结果
		return clientList;
	}
	
	public List<DynaBean> getClientVocd(Long loginUserId, LfClientVo clientVo,
			String corpcode, PageInfo pageInfo) throws Exception
	{
		//客户对象集合
		List<DynaBean> clientList;
		try
		{
			//获取客户记录
			clientList = lfClientVoDAO.findClientVocd(loginUserId, corpcode,
					clientVo, pageInfo);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取客户记录出现异常！");
			throw e;
		}
		//返回结果
		return clientList;
	}
	
	public List<DynaBean> getClientVocd(Long loginUserId, LfClientVo clientVo,
			String corpcode, PageInfo pageInfo,LinkedHashMap<String,String> conditionMap) throws Exception
	{
		//客户对象集合
		List<DynaBean> clientList;
		try
		{
			//获取客户记录
			clientList = lfClientVoDAO.findClientVocd(loginUserId, corpcode,
					clientVo, pageInfo,conditionMap);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取客户记录出现异常！");
			throw e;
		}
		//返回结果
		return clientList;
	}
	public void saveClentiddep(Long clientid,String depid){
		lfClientVoDAO.saveDepClient(clientid, depid);
	}
	public String[] getDepName(String id,Long lguserid){
		return lfClientVoDAO.getDepNameByClient(id,lguserid);
	}
	public void cleanClentiddep(String clientid){
		lfClientVoDAO.cleanClentiddep(clientid);
	}
	public Map<String,Set<String>> findClientPhoneNameSet(String depIds){
		return lfClientVoDAO.findClientPhoneNameSetcd(depIds); 
	}
	
	/**
	 * 根据id获取客户机构信息
	 * 
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public List<LfClient> getClientByDepId(Long depId) throws Exception
	{
		//机构id不能为空
		if (depId == null)
		{
			return null;
		}
		//可会对象集合
		List<LfClient> clientsList;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("depId", depId.toString());
			//获取客户记录
			clientsList = empDao.findListByCondition(LfClient.class,
					conditionMap, null);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取客户记录出现异常！");
			throw e;
		}
		//返回结果
		return clientsList;
	}

	/**
	 * 添加客户机构
	 * 
	 * @param depCode
	 * @param bizCode
	 * @param clientCode
	 * @return
	 * @throws Exception
	 */
	synchronized public Integer addClientByProcedure(String depCode,
			String bizCode, String clientCode) throws Exception
	{
		//结果
		Integer result = -1;
		//获取数据库连接
		Connection conn = empTransDao.getConnection();
		try
		{
			//开启事务
			empTransDao.beginTransaction(conn);
			result = empTransDao.saveClientByProc(conn, depCode, bizCode,
					clientCode);
			//提交事务
			empTransDao.commitTransaction(conn);
		} catch (Exception e)
		{
			//异常处理
			result = -1;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"添加客户机构出现异常！");
			throw e;
		} finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		//返回结果
		return result;
	}

	/**
	 * 删除客户机构及其成员
	 * 
	 * @param depCode
	 * @return
	 */
	public Integer delClientDep(String depId, String corpCode, Long userId)
	{
		Integer intResult = -1;
		Connection conn = empTransDao.getConnection();
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			empTransDao.beginTransaction(conn);
			conditionMap.put("depId", depId);
			//删除操作员客户机构权限表
			empTransDao.delete(conn, LfCliDepConn.class,conditionMap);

			GenericLfClientVoDAO dao=new GenericLfClientVoDAO();
			dao.deleteLfList2gro("1",depId,conn);
			
			//原来的删除生日祝福
			this.delClientBirth(depId,null, conn, corpCode);
			dao.deleteLfBirthdayMember(depId,conn);
			
			
			//删除只存在该机构下的客户
//			empTransDao.delete(conn, LfClient.class, conditionMap);
			dao.deleteLfClient(depId,conn);
			
			
			conditionMap.clear();
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("depId", depId);
			//删除机构
			Integer delNum = empTransDao.delete(conn, LfClientDep.class,conditionMap);
			if (delNum > 0){
				intResult = 1;
			}else{
				intResult = -1;
			}
			conditionMap.clear();
			conditionMap.put("depId", depId);
			//删除绑定了该机构的绑定表
			empTransDao.delete(conn, LfClientDepSp.class,conditionMap);
			empTransDao.commitTransaction(conn);
		} catch (Exception e){
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"删除客户机构及其成员出现异常！");
		} finally{
			empTransDao.closeConnection(conn);
		}
		return intResult;
	}
	
	

	/**
	 * 根据code删除客户机构
	 * 
	 * @param conn
	 * @param depCode
	 * @return
	 */
	private int delCliDepConnByCode(Connection conn, String depId)
	{
		//结果数
		int delCount = 0;
		try
		{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("depId", depId);
			//执行删除
			delCount = empTransDao.delete(conn, LfCliDepConn.class,
					conditionMap);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"删除客户机构出现异常！");
		}
		//返回结果
		return delCount;
	}

	/**
	 * 添加
	 * 
	 * @return
	 */
	public boolean isClientDepCustomAdd()
	{
		//结果
		boolean result = false;
		try
		{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("addType", "0");
			//获取客户记录
			List<LfClientDep> clientsList = empDao.findListByCondition(
					LfClientDep.class, conditionMap, null);
			//有记录
			if (clientsList != null && clientsList.size() > 0)
			{
				//返回
				result = true;
			}
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"查询客户记录出现异常！");
		}
		//返回结果
		return result;
	}

	// 过滤掉企业管理员删除自己的顶级部门权限
	public boolean filtAdmin(long conn_id, String corpCode) throws Exception
	{
		//结果
		boolean boo;
		try
		{
			//获取记录
			boo = lfClientVoDAO.filtClientAdmin(conn_id, corpCode);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"过滤掉企业管理员删除自己的顶级部门权限出现异常！");
			throw e;
		}
		//返回结果
		return boo;
	}

	/**
	 * 绑定操作员客户机构权限 2012-9-27
	 * 
	 * @param conns
	 *            绑定权限集合
	 * @return Integer 成功返回true
	 */
	public boolean addPermissionList(List<LfCliDepConn> conns)
	{
		//结果
		boolean intResult = false;
		//获取连接
		Connection conn = empTransDao.getConnection();
		try
		{
			//开启事务
			empTransDao.beginTransaction(conn);
			//循环处理
			for (int i = 0; i < conns.size(); i++)
			{
				//保存到数据库
				empTransDao.save(conn, conns.get(i));
			}
			//提交事务
			empTransDao.commitTransaction(conn);
			intResult = true;

		} catch (Exception e)
		{
			//异常处理
			intResult = false;
			EmpExecutionContext.error(e,"绑定操作员客户机构权限出现异常！");
			empTransDao.rollBackTransaction(conn);
		} finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		//返回结果
		return intResult;
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
			EmpExecutionContext.error(e,"增加员工失败！");
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
			EmpExecutionContext.error(e,"查询部门员工信息失败！");
		}
		return result;
	}
	
	/**
	 * 新增 客户以及客户机构关联表 
	 * @param isFlag	是修改还是新增
	 * @param addDep	需要填加关联的机构 
	 * @param lfClient	客户对象
	 * @return	成功 /失败   success   /fail
	 */
	public String isAddClient(boolean isFlag,List<String> addDep,LfClientMultiPro lfClient){
		String returnmsg = "";
		Connection conn = empTransDao.getConnection();
		try{
			empTransDao.beginTransaction(conn);
			Long clientId = lfClient.getClientId();
			if(isFlag){
				//此处是处理保存实体7-24
				LfClient client=new LfClient();
				PropertyUtils.copyProperties(client, lfClient);
				empTransDao.update(conn, client);
			}else{
				//clientId = empTransDao.saveObjectReturnID(conn, lfClient);
				empTransDao.save(conn, lfClient);
			}
			LfClientDepSp clientDepsp = null;
			List<LfClientDepSp> depspList = new ArrayList<LfClientDepSp>();
			for(String temp:addDep){
				if(temp != null && !"".equals(temp)){
					clientDepsp = new LfClientDepSp();
					clientDepsp.setClientId(clientId);
					clientDepsp.setDepId(Long.valueOf(temp));
					depspList.add(clientDepsp);
				}
			}
			if(depspList.size()>0){
				empTransDao.save(conn, depspList, LfClientDepSp.class);
			}
			empTransDao.commitTransaction(conn);
			if(isFlag){
				returnmsg = "samePerson";
			}else{
				returnmsg = "success";
			}
		}catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"新增 客户以及客户机构关联表出现异常！");
			returnmsg = "fail";
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return returnmsg;
	}
	/**
	 *  是修改还是覆盖客户
	 * @param clientid	 老客户ID
	 * @param isFlag	是否覆盖 true  修改false
	 * @param addDep	需要处理的机构ID
	 * @param lfClient	需要修改的客户
	 * @return success /fail
	 */
	public String isEditClient(LfClient updateClient,boolean isFlag,List<String> addDep,LfClient lfClient,Long lguserid){
		String returnmsg = "";
		Connection conn = empTransDao.getConnection();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			empTransDao.beginTransaction(conn);
			
			//覆盖的话，则删除修改客户的绑定关系表        修改的话  则删除该客户绑定的关系表
			conditionMap.put("clientId", String.valueOf(updateClient.getClientId()));
			empTransDao.delete(conn, LfClientDepSp.class, conditionMap);
			if(isFlag){
				LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
				conditionMap.clear();
				conditionMap.put("guId", String.valueOf(updateClient.getGuId()));
				conditionMap.put("l2gType", "1");
				objectMap.put("guId", String.valueOf(lfClient.getGuId()));
				empTransDao.update(conn, LfList2gro.class, objectMap, conditionMap);
				this.delClientBirth(null, String.valueOf(updateClient.getGuId()), conn, lfClient.getCorpCode());
				//覆盖的话，则删除 修改的客户
				conditionMap.clear();
				conditionMap.put("clientId", String.valueOf(updateClient.getClientId()));
				conditionMap.put("corpCode",updateClient.getCorpCode());
				empTransDao.delete(conn, LfClient.class, conditionMap);
			}
			//覆盖的话，则更新修改客户        修改的话  则更新该客户
			empTransDao.update(conn, lfClient);
			LfClientDepSp clientDepsp = null;
			List<LfClientDepSp> depspList = new ArrayList<LfClientDepSp>();
			//处理该操作员需要绑定的机构关联表
			if(addDep != null && addDep.size()>0){
				for(String temp:addDep){
					clientDepsp = new LfClientDepSp();
					clientDepsp.setClientId(lfClient.getClientId());
					clientDepsp.setDepId(Long.valueOf(temp));
					depspList.add(clientDepsp);
				}
				if(depspList.size()>0){
					empTransDao.save(conn, depspList, LfClientDepSp.class);
				}
			}
			empTransDao.commitTransaction(conn);
			returnmsg = "success";
		}catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"修改或覆盖客户出现异常！");
			returnmsg = "fail";
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return returnmsg;
	}
	
	/**
	 *  删除客户	并且处理是否真需要删除客户的还是只需要删除其关联表的
	 * @param clientid		需要删除的客户ID
	 * @param guidStr		需要删除的客户GUID
	 * @param clientidstr	需要删除的客户客户ID
	 * @param userclientconn	当前操作员的客户管辖范围机构串
	 * @return
	 */
	public String isDeleteClient(String clientid,String guidStr,String clientidstr,String userclientconn,String lgcorpcode){
		String returnmsg = "";
		Connection conn = empTransDao.getConnection();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			empTransDao.beginTransaction(conn);
			if(guidStr != null && guidStr.length()>0){
				guidStr = guidStr.substring(0, guidStr.length()-1);
				conditionMap.put("guId", guidStr);
				conditionMap.put("l2gType", "1");
				empTransDao.delete(conn, LfList2gro.class, conditionMap);
				//这里是删除这一批客户的生日祝福的
				this.delClientBirth(null, guidStr, conn, lgcorpcode);
			}
			conditionMap.clear();
			conditionMap.put("clientId", clientid);
			conditionMap.put("depId", userclientconn);
			empTransDao.delete(conn, LfClientDepSp.class, conditionMap);
			conditionMap.clear();
			if(clientidstr != null && clientidstr.length()>0){
				clientidstr = clientidstr.substring(0, clientidstr.length()-1);
				conditionMap.put("clientId", clientidstr);
				empTransDao.delete(conn, LfClient.class, conditionMap);
			}
			empTransDao.commitTransaction(conn);
			returnmsg = "success";
		}catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"删除客户,处理是否真需要删除客户的还是只需要删除其关联表出现异常！");
			returnmsg = "fail";
		}finally
		{
			empTransDao.closeConnection(conn);
		}
		return returnmsg;
	}
	
	
	
	
	/**
	 *   获取该企业下面客户的信息
	 * @param corpCode	企业编码
	 * @param namephoneSet	名字+手机号码的string
	 * @return  key name+phone value clientid
	 * @throws Exception
	 */
	public LinkedHashMap<String,String> getClientMsg(String corpCode,Set<String> namephoneSet)throws Exception{
		return lfClientVoDAO.findClientPhoneNameSet(corpCode, namephoneSet);
	}
	
	
	/**
	 *   这里处理 客户上传的操作
	 * @param containList	包含的客户 name+phone
	 * @param keyMap	数据库包含的name+phone 对应的客户ID
	 * @param existClientid	上传的客户中name+phone 对应的客户ID的字符串
	 * @param clientList	在数据库中不存在name+phone的客户的集合
	 * @param deps	在批量上传客户的机构所选数组
	 * @return 成功/失败
	 */
	public String uploadClientDoXls(List<String> containList,LinkedHashMap<String,String> keyMap,
			String existClientid,LinkedHashMap<String, LfClient> containClientMap,List<LfClient> clientList,String[] deps){
		String returnmsg = "fail";
		//这里处理的是数据库存在的客户以及该客户所对应的机构
		LinkedHashMap<Long,HashSet<Long>> depspMap = new LinkedHashMap<Long, HashSet<Long>>();
		//这里处理的是数据库存在的客户
		LinkedHashMap<Long,LfClient> clientMap = new LinkedHashMap<Long, LfClient>();
		try{
		
			if(containList != null && containList.size()>0){
				depspMap = this.getExistClientMsg(existClientid);
				clientMap = this.getExistClients(existClientid);
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取客户记录出现异常！");
		}
		Connection conn = empTransDao.getConnection();
		try{
			empTransDao.beginTransaction(conn);
			//这里处理的是NAME+PHONE在数据库重复的记录
			LfClientDepSp depsp = null;
			List<LfClientDepSp> depspList = new ArrayList<LfClientDepSp>();
			if(containList.size()>0){
				for(int i=0;i<containList.size();i++){
					//名字+手机号码
					String namephone = containList.get(i);
					//数据库中保存的客户的客户ID
					Long dbclientid = Long.valueOf(keyMap.get(namephone));
					//数据库中保存的客户
					LfClient dbclient = clientMap.get(dbclientid);
					//数据库中保存的客户所属于的机构ID
					HashSet<Long> dbdepidSet = depspMap.get(dbclientid);
					//在上传XLS中的客户记录
					LfClient updateClient = containClientMap.get(namephone);
					if(updateClient == null){
						continue;
					}
					dbclient = setClientObj(dbclient, updateClient);
					if(deps != null && deps.length > 0){
						for(String temp:deps){
							if(temp != null && !"".equals(temp)){
								depsp = new LfClientDepSp();
								depsp.setDepId(Long.valueOf(temp));
								depsp.setClientId(dbclient.getClientId());
								if(dbdepidSet != null && !dbdepidSet.contains(Long.valueOf(temp))){
									depspList.add(depsp);
								}else if(dbdepidSet == null){
									depspList.add(depsp);
								}
							}
						}
					}
					empTransDao.update(conn, dbclient);
				}
			}
			if(clientList != null && clientList.size()>0){
				for(int j=0;j<clientList.size();j++){
					LfClient client = clientList.get(j);
					Long clientId = empTransDao.saveObjectReturnID(conn, client);
					for(String temp:deps){
						if(temp != null && !"".equals(temp)){
							depsp = new LfClientDepSp();
							depsp.setClientId(clientId);
							depsp.setDepId(Long.valueOf(temp));
							depspList.add(depsp);
						}
					}
				}
			}
			if(depspList != null && depspList.size()>0){
				empTransDao.save(conn, depspList, LfClientDepSp.class);
			}
			empTransDao.commitTransaction(conn);
			returnmsg = "success";
		}catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"处理 客户上传出现异常！");
		}finally{
			empTransDao.closeConnection(conn);
		}
		return returnmsg;
	}
	
	/**
	 *  获取数据库中存在的客户所对应的客户关联表信息
	 * @param existClientid
	 * @return
	 */
	public LinkedHashMap<Long,HashSet<Long>> getExistClientMsg(String existClientid){
		LinkedHashMap<Long,HashSet<Long>> depspMap = new LinkedHashMap<Long, HashSet<Long>>();
		try{
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			existClientid = existClientid.substring(0, existClientid.length()-1);
			conditionMap.put("clientId&in", existClientid);
			List<LfClientDepSp> depspList = empDao.findListBySymbolsCondition(LfClientDepSp.class, conditionMap,null);
			if(depspList != null && depspList.size()>0){
				for(LfClientDepSp depsp:depspList){
					Long clientid = depsp.getClientId();
					Long depid = depsp.getDepId();
					if(depspMap.containsKey(clientid)){
						HashSet<Long> depidtemp = depspMap.get(clientid);
						depidtemp.add(depid);
						depspMap.remove(clientid);
						depspMap.put(clientid, depidtemp);
					}else{
						HashSet<Long> depidSet = new HashSet<Long>();
						depidSet.add(depid);
						depspMap.put(clientid, depidSet);
					}
				}
			}
		}catch (Exception e) {
			depspMap.clear();
			EmpExecutionContext.error(e,"获取数据库中存在的客户所对应的客户关联表信息出现异常！");
		}
		return depspMap;
	}
	
	
	/**
	 *   获取存在的客户ID所对应的客户关联表中的机构ID
	 * @param existClientid
	 * @return
	 */
	public LinkedHashMap<Long,List<Long>> getExistClientDeps(String existClientid){
		LinkedHashMap<Long,List<Long>> depspMap = new LinkedHashMap<Long, List<Long>>();
		try{
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("clientId&in", existClientid);
			List<LfClientDepSp> depspList = empDao.findListBySymbolsCondition(LfClientDepSp.class, conditionMap, null);
			if(depspList != null && depspList.size()>0){
				for(LfClientDepSp depsp:depspList){
					Long clientid = depsp.getClientId();
					Long depid = depsp.getDepId();
					if(depspMap.containsKey(clientid)){
						List<Long> depidtemp = depspMap.get(clientid);
						depidtemp.add(depid);
						depspMap.remove(clientid);
						depspMap.put(clientid, depidtemp);
					}else{
						List<Long> depidSet = new ArrayList<Long>();
						depidSet.add(depid);
						depspMap.put(clientid, depidSet);
					}
				}
			}
		}catch (Exception e) {
			depspMap.clear();
			EmpExecutionContext.error(e,"获取存在的客户ID所对应的客户关联表中的机构ID出现异常！");
		}
		return depspMap;
	}
	
	
	/**
	 *  获取数据库中存在的客户
	 * @param existClientid
	 * @return
	 */
	public LinkedHashMap<Long,LfClient> getExistClients(String existClientid){
		LinkedHashMap<Long,LfClient> clientMap = new LinkedHashMap<Long, LfClient>();
		try{
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			existClientid = existClientid.substring(0, existClientid.length()-1);
			conditionMap.put("clientId&in", existClientid);
			List<LfClient> clientList = new BaseBiz().getByCondition(LfClient.class, conditionMap, null);
			if(clientList != null && clientList.size()>0){
				for(LfClient client:clientList){
					Long clientid = client.getClientId();
					clientMap.put(clientid, client);
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"获取数据库中存在的客户出现异常！");
		}
		return clientMap;
	}
	
	/**
	 *  判断是否为空 
	 * @param temp	值
	 * @return
	 */
	public boolean isEffectValue(String temp){
		if(temp == null || "".equals(temp)){
			return false;
		}
		return true;
	}
	
	@ SuppressWarnings("unchecked")
	public LfClient setClientObj(LfClient dbClient,LfClient updateClient){
		try{
			// 性别
			dbClient.setSex(updateClient.getSex());
			//客户号
			if(isEffectValue(updateClient.getClientCode())){
				dbClient.setClientCode(updateClient.getClientCode().toUpperCase());
			}
			// 职务
			if(isEffectValue(updateClient.getJob())){
				dbClient.setJob(updateClient.getJob());
			}
			//行业
			if(isEffectValue(updateClient.getProfession())){
				dbClient.setProfession(updateClient.getProfession());
			}
			//经理
			if(isEffectValue(updateClient.getEname())){
				dbClient.setEname(updateClient.getEname());
			}
			// qq
			if(isEffectValue(updateClient.getQq())){
				dbClient.setQq(updateClient.getQq());
			}
			// 地区
			if(isEffectValue(updateClient.getArea())){
				dbClient.setArea(updateClient.getArea());
			}
			// msn
			if(isEffectValue(updateClient.getMsn())){
				dbClient.setMsn(updateClient.getMsn());
			}
			// 描述
			if(isEffectValue(updateClient.getComments())){
				dbClient.setComments(updateClient.getComments());
			}
			if(isEffectValue(updateClient.getEMail())){
				dbClient.setEMail(updateClient.getEMail());
			}
			if(isEffectValue(updateClient.getOph())){
				dbClient.setOph(updateClient.getOph());
			}
			if(updateClient.getBirthday() != null){
				try{
					dbClient.setBirthday(updateClient.getBirthday());
				}catch (Exception e) {
					EmpExecutionContext.error(e,"生日转化失败！");
				}
			}
			dbClient.setUserId(updateClient.getUserId());
			String s = "";
			Class clazz = Class.forName("com.montnets.emp.entity.client.LfClient");
			Method mf = null;
			for(int m=1;m<51;m++){
               if(m<10){
                   s = "Field0" + m;
               }else {
              	   s = "Field" + m;
               }
               mf = clazz.getDeclaredMethod("get" + s);
               Object object = mf.invoke(updateClient);
               if(object != null && !"".equals(object)){
            	   mf = null;
            	   mf = clazz.getDeclaredMethod("set" + s,String.class);
            	   mf.invoke(dbClient, String.valueOf(object));
               }
            }
		}catch (Exception e) {
			EmpExecutionContext.error(e,"更新客户字段值出现异常！");
		}
		return dbClient;
	}
	
	
	/**
	 *  获取该操作员管辖的客户通讯录权限
	 * @param sysuser	当前操作员
	 * @return
	 */
	public HashSet<Long> getLoginUserClientConn(LfSysuser sysuser){
		HashSet<Long> connSet = new HashSet<Long>();
		try{
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId",String.valueOf(sysuser.getUserId()));
			List<LfCliDepConn> depConnList = empDao.findListByCondition(LfCliDepConn.class, conditionMap, null);
			if(depConnList != null && depConnList.size()>0){
				for(LfCliDepConn conn:depConnList){
					conditionMap.clear();
					LfClientDep clientDep = empDao.findObjectByID(LfClientDep.class, conn.getDepId());
					conditionMap.put("deppath&like2",clientDep.getDeppath());
					conditionMap.put("corpCode", sysuser.getCorpCode());
					List<LfClientDep> clientDepList = empDao.findListBySymbolsCondition(LfClientDep.class, conditionMap, null);
					if(clientDepList != null && clientDepList.size()>0){
						for(LfClientDep dep:clientDepList){
							connSet.add(dep.getDepId());
						}
					}
				}
			}
		}catch (Exception e) {
			connSet.clear();
			EmpExecutionContext.error(e,"获取该操作员管辖的客户通讯录权限出现异常！");
		}
		return connSet;
	}
	
	
	/**
	 *  返回操作员的机构串
	 * @param sysuser	操作员对象
	 * @return
	 */
	public String getLoginUserClientConnBuffer(LfSysuser sysuser){
		StringBuffer buffer = new StringBuffer();
		try{
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId",String.valueOf(sysuser.getUserId()));
			List<LfCliDepConn> depConnList = empDao.findListByCondition(LfCliDepConn.class, conditionMap, null);
			if(depConnList != null && depConnList.size()>0){
				for(LfCliDepConn conn:depConnList){
					conditionMap.clear();
					LfClientDep clientDep = empDao.findObjectByID(LfClientDep.class, conn.getDepId());
					conditionMap.put("deppath&like2",clientDep.getDeppath());
					conditionMap.put("corpCode", sysuser.getCorpCode());
					List<LfClientDep> clientDepList = empDao.findListBySymbolsCondition(LfClientDep.class, conditionMap, null);
					if(clientDepList != null && clientDepList.size()>0){
						for(LfClientDep dep:clientDepList){
							buffer.append(dep.getDepId()).append(",");
						}
					}
				}
			}
		}catch (Exception e) {
			buffer = new StringBuffer();
			EmpExecutionContext.error(e,"返回操作员的机构串出现异常！");
		}
		return buffer.toString();
	}
	
	
	
	/**
	 *   获取当前操作员的客户管辖权限下的所有机构字符串
	 * @param lguserid 当前操作员用户ID
	 * @return
	 */
	public String getUserClientConnDepId(Long lguserid){
		String returnmsg = "";
		try{
			returnmsg = lfClientVoDAO.getLoginClientConnStrByUserId(lguserid);
		}catch (Exception e) {
			returnmsg = "";
			EmpExecutionContext.error(e,"获取当前操作员的客户管辖权限下的所有机构字符串出现异常！");
		}
		return returnmsg;
	}
	
	
	
	/**
	 *   处理所批量删除的客户中   哪一部分是需要删除 客户对象以及客户关联表 ，哪一部分是只需要删除客户关联表的
	 * @param clientList	所有需要删除的客户
	 * @param clientmsg		所需要删除的客户的CLIENTID  以及对应的  该客户所拥有的客户关联机构的ID
	 * @param clientConnSet	当前所能删除的机构ID
	 * @return	返回所能删除的客户ID 以及 客户guid 用于删除群组 
	 */
	 public List<StringBuffer> filterDelClient(List<LfClient> clientList,LinkedHashMap<Long,List<Long>> clientmsg,HashSet<Long> clientConnSet){
		 List<StringBuffer> bufferList = new ArrayList<StringBuffer>();
		 StringBuffer guidbuffer = new StringBuffer();
		 StringBuffer clientidbuffer = new StringBuffer();
		 try{
			for(LfClient client:clientList){
				List<Long> clientDepSpList = clientmsg.get(client.getClientId());
				if(clientDepSpList != null && clientDepSpList.size()>0){
					boolean isFlag = false;
					for(Long temp:clientDepSpList){
						if(!clientConnSet.contains(temp) && !isFlag){
							isFlag =true;
							break;
						}
					}
					//如果全部在操作员权限范围内的客户，则删除该客户，删除关联
					if(!isFlag){
						guidbuffer.append(client.getGuId()).append(",");
						clientidbuffer.append(client.getClientId()).append(",");
					}
				}
			}
			bufferList.add(clientidbuffer);
			bufferList.add(guidbuffer);
		}catch (Exception e) {
			bufferList.clear();
			bufferList.add(new StringBuffer());
			bufferList.add(new StringBuffer());
			EmpExecutionContext.error(e,"处理批量删除客户出现异常！");
		}
		return bufferList;
	}
	 
	 /**
	  * 处理删除客户的生日祝福
	  * @param depid	删除单个机构
	  * @param clientguid	删除N个客户的guid 
	  * @param conn	连接
	  * @param corpCode	企业编码
	  */
	 public void delClientBirth(String depid,String clientguid,Connection conn,String corpCode){
		 try{
			 LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			 if(depid != null && !"".equals(depid)){
				 conditionMap.put("type", "2");
				 conditionMap.put("corpCode",corpCode);
				 conditionMap.put("memberId", depid);
				 conditionMap.put("membertype", "2");
				 empTransDao.delete(conn, LfBirthdayMember.class, conditionMap);
			 }
			 if(clientguid != null && !"".equals(clientguid)){
				 conditionMap.clear();
				 conditionMap.put("type", "2");
				 conditionMap.put("corpCode",corpCode);
				 conditionMap.put("memberId", clientguid);
				 conditionMap.put("membertype", "1");
				 empTransDao.delete(conn, LfBirthdayMember.class, conditionMap);
			 }
		 }catch (Exception e) {
			EmpExecutionContext.error(e,"处理删除客户的生日祝福出现异常！");
		}
	 }

	 

	
	 /**
	  * 批量上传客户信息更新数据库(0-5个客户属性)
	  * @description    
	  * @param addList 新增的客户信息
	  * @param updateList 修改的客户信息
	  * @param deps机构
	  * @return       			 
	  * @author chentingsheng <cts314@163.com>
	  * @datetime 2014-6-4 下午04:45:42
	  */
	 public String updateClientList(List<LfClientMultiPro> addList,List<LfClientMultiPro> updateList,String[] deps){
			String returnmsg = "fail";
			//机构
			StringBuffer depstrs = new StringBuffer();
			if(deps != null && deps.length > 0){
				for(String temp:deps){
					depstrs.append(temp).append(",");
				}
			}
			if(depstrs.length()>0){
				depstrs.deleteCharAt(depstrs.length()-1);
			}
			if(depstrs.length()<1){
				return returnmsg;
			}
			
			//对应关系信息集合
			List<LfClientDepSp> depspList = new ArrayList<LfClientDepSp>();
			Connection conn = empTransDao.getConnection();
			try{
				empTransDao.beginTransaction(conn);
				GenericLfClientVoDAO dao=new GenericLfClientVoDAO();
				//修改记录
				if(updateList.size()>0){
					LfClientDepSp depsp = null;
					LfClientMultiPro updateClient = null;
					Set<String> dbList = dao.getDepID();
					for(int i=0;i<updateList.size();i++)
					{
						updateClient = updateList.get(i);
						if(updateClient == null)
						{
							continue;
						}
						if(deps != null && deps.length > 0)
						{
							for(String temp:deps)
							{
								if(temp != null && !"".equals(temp))
								{
									if(!dbList.contains(temp + "@" + updateClient.getClientId()))
									{
										depsp = new LfClientDepSp();
										depsp.setDepId(Long.valueOf(temp));
										depsp.setClientId(updateClient.getClientId());
										depspList.add(depsp);
									}
								}
							}
						}
					}
					dao.updateClientMultiPro(conn,updateList);
				}
				if(addList != null && addList.size()>0)
				{
					//新增记录
					empTransDao.save(conn, addList, LfClientMultiPro.class);
					LfClientDepSp depsp = null;
					for(int j=0;j<addList.size();j++){
						LfClientMultiPro client = addList.get(j);
						Long clientId = client.getClientId();
						for(String temp:deps){
							if(temp != null && !"".equals(temp)){
								depsp = new LfClientDepSp();
								depsp.setClientId(clientId);
								depsp.setDepId(Long.valueOf(temp));
								depspList.add(depsp);
							}
						}
					}
				}
				if(depspList != null && depspList.size()>0){
					empTransDao.save(conn, depspList, LfClientDepSp.class);
				}
				empTransDao.commitTransaction(conn);
				returnmsg = "success";
			}catch (Exception e) {
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e,"处理 客户上传出现异常！");
			}finally{
				empTransDao.closeConnection(conn);
			}
			return returnmsg;
		}
	
	 /**
	  * 批量上传客户信息更新数据库(0-5个客户属性)
	  * @description    
	  * @param addList 新增的客户信息
	  * @param updateList 修改的客户信息
	  * @param deps机构
	  * @return       			 
	  * @author chentingsheng <cts314@163.com>
	  * @datetime 2014-6-4 下午04:45:42
	  */
	 public String updateClient5ProList(List<LfClient5Pro> addList,List<LfClient5Pro> updateList,String[] deps){
			String returnmsg = "fail";
			StringBuffer depstrs = new StringBuffer();
			if(deps != null && deps.length > 0){
				for(String temp:deps){
					depstrs.append(temp).append(",");
				}
			}
			if(depstrs.length()>0){
				depstrs.deleteCharAt(depstrs.length()-1);
			}
			if(depstrs.length()<1){
				return returnmsg;
			}
			List<LfClientDepSp> depspList = new ArrayList<LfClientDepSp>();
			Connection conn = empTransDao.getConnection();
			try{
				empTransDao.beginTransaction(conn);
				GenericLfClientVoDAO dao=new GenericLfClientVoDAO();
				if(updateList.size()>0){
					LfClientDepSp depsp = null;
					LfClient5Pro updateClient = null;
					Set<String> dbList = dao.getDepID();
					for(int i=0;i<updateList.size();i++){
						updateClient = updateList.get(i);
						if(updateClient == null){
							continue;
						}
						if(deps != null && deps.length > 0){
							for(String temp:deps){
								if(temp != null && !"".equals(temp)){
									if(!dbList.contains(temp + "@" + updateClient.getClientId()))
									{
										depsp = new LfClientDepSp();
										depsp.setDepId(Long.valueOf(temp));
										depsp.setClientId(updateClient.getClientId());
										depspList.add(depsp);
									}
								}
							}
						}
					}
					dao.updateCleint5Pro(conn,updateList);
					
				}
				if(addList != null && addList.size()>0){
					//新增记录
					empTransDao.save(conn, addList, LfClient5Pro.class);
					LfClientDepSp depsp = null;
					for(int j=0;j<addList.size();j++){
						LfClient5Pro client = addList.get(j);
						Long clientId = client.getClientId();
						for(String temp:deps){
							if(temp != null && !"".equals(temp)){
								depsp = new LfClientDepSp();
								depsp.setClientId(clientId);
								depsp.setDepId(Long.valueOf(temp));
								depspList.add(depsp);
							}
						}
					}
				}
				if(depspList != null && depspList.size()>0){
					empTransDao.save(conn, depspList, LfClientDepSp.class);
				}
				empTransDao.commitTransaction(conn);
				returnmsg = "success";
			}catch (Exception e) {
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e,"处理 客户上传出现异常！");
			}finally{
				empTransDao.closeConnection(conn);
			}
			return returnmsg;
		}
	
	/**
	 * 获取客户记录
	 * @param code
	 * @return
	 */
	public List<DynaBean> getMWClient(String code)throws Exception{
		//客户对象集合
		List<DynaBean> clientList;
		try
		{
			//获取客户记录
			clientList = lfClientVoDAO.getMWClient(code);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"获取客户记录出现异常！");
			throw e;
		}
		//返回结果
		return clientList;
	}
	/**
	 * 根据手机号，姓名条件查询该机构下是否存在同一个人
	 * @param condition
	 * @return
	 */
	public int hasClient(LinkedHashMap<String, String> condition){
		
		return lfClientVoDAO.hasClient(condition);
	}

	/**
	 * 获取APP客户编码
	 * @description    
	 * @param corpCode 企业编码
	 * @param modile   客户手机号码
	 * @return   true:存在;false:不存在    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-8-21 下午03:01:57
	 */
	public String getUnknowDepAppClientId(String corpCode, String modile)
	{
		String clientId = "-1";
		List<DynaBean> appCodeList = lfClientVoDAO.getUnknowDepAppClientId(corpCode, modile);
		if(appCodeList != null && appCodeList.size() > 0)
		{
			clientId = appCodeList.get(0).get("clientid").toString();
		}
		return clientId;
	}
	
	public LinkedHashMap<Long,Long> getUndepClientMsg(String corpCode)throws Exception{
		return lfClientVoDAO.getUndepClientMsg(corpCode);
	}
	
	 public String updateClientList(List<LfClientMultiPro> addList,List<LfClientMultiPro> updateList,String[] deps,List<Long> unlist){
			String returnmsg = "fail";
			//机构
			StringBuffer depstrs = new StringBuffer();
			if(deps != null && deps.length > 0){
				for(String temp:deps){
					depstrs.append(temp).append(",");
				}
			}
			if(depstrs.length()>0){
				depstrs.deleteCharAt(depstrs.length()-1);
			}
			if(depstrs.length()<1){
				return returnmsg;
			}
			
			//对应关系信息集合
			List<LfClientDepSp> depspList = new ArrayList<LfClientDepSp>();
			Connection conn = empTransDao.getConnection();
			try{
				empTransDao.beginTransaction(conn);
				GenericLfClientVoDAO dao=new GenericLfClientVoDAO();
				//修改记录
				if(updateList.size()>0){
					LfClientDepSp depsp = null;
					LfClientMultiPro updateClient = null;
					Set<String> dbList = dao.getDepID();
					for(int i=0;i<updateList.size();i++)
					{
						updateClient = updateList.get(i);
						if(updateClient == null)
						{
							continue;
						}
						if(deps != null && deps.length > 0)
						{
							for(String temp:deps)
							{
								if(temp != null && !"".equals(temp))
								{
									if(!dbList.contains(temp + "@" + updateClient.getClientId()))
									{
										depsp = new LfClientDepSp();
										depsp.setDepId(Long.valueOf(temp));
										depsp.setClientId(updateClient.getClientId());
										depspList.add(depsp);
									}
								}
							}
						}
					}
					dao.updateClientMultiPro(conn,updateList);
				}
				if(addList != null && addList.size()>0)
				{
					//新增记录
					empTransDao.save(conn, addList, LfClientMultiPro.class);
					LfClientDepSp depsp = null;
					for(int j=0;j<addList.size();j++){
						LfClientMultiPro client = addList.get(j);
						Long clientId = client.getClientId();
						for(String temp:deps){
							if(temp != null && !"".equals(temp)){
								depsp = new LfClientDepSp();
								depsp.setClientId(clientId);
								depsp.setDepId(Long.valueOf(temp));
								depspList.add(depsp);
							}
						}
					}
				}
				LinkedHashMap<String, String> condMap = new LinkedHashMap<String, String>();
				//删除未知机构关联表信息
				for(long unid : unlist){
					condMap.put("clientId", unid+"");
					empTransDao.delete(conn, LfClientDepSp.class, condMap);
				}
				if(depspList != null && depspList.size()>0){
					empTransDao.save(conn, depspList, LfClientDepSp.class);
				}
				empTransDao.commitTransaction(conn);
				returnmsg = "success";
			}catch (Exception e) {
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e,"处理 客户上传出现异常！");
			}finally{
				empTransDao.closeConnection(conn);
			}
			return returnmsg;
		}
	
	 public String updateClient5ProList(List<LfClient5Pro> addList,List<LfClient5Pro> updateList,String[] deps,List<Long> unlist){
			String returnmsg = "fail";
			StringBuffer depstrs = new StringBuffer();
			if(deps != null && deps.length > 0){
				for(String temp:deps){
					depstrs.append(temp).append(",");
				}
			}
			if(depstrs.length()>0){
				depstrs.deleteCharAt(depstrs.length()-1);
			}
			if(depstrs.length()<1){
				return returnmsg;
			}
			List<LfClientDepSp> depspList = new ArrayList<LfClientDepSp>();
			Connection conn = empTransDao.getConnection();
			try{
				empTransDao.beginTransaction(conn);
				GenericLfClientVoDAO dao=new GenericLfClientVoDAO();
				if(updateList.size()>0){
					LfClientDepSp depsp = null;
					LfClient5Pro updateClient = null;
					Set<String> dbList = dao.getDepID();
					for(int i=0;i<updateList.size();i++){
						updateClient = updateList.get(i);
						if(updateClient == null){
							continue;
						}
						if(deps != null && deps.length > 0){
							for(String temp:deps){
								if(temp != null && !"".equals(temp)){
									if(!dbList.contains(temp + "@" + updateClient.getClientId()))
									{
										depsp = new LfClientDepSp();
										depsp.setDepId(Long.valueOf(temp));
										depsp.setClientId(updateClient.getClientId());
										depspList.add(depsp);
									}
								}
							}
						}
					}
					dao.updateCleint5Pro(conn,updateList);
					
				}
				if(addList != null && addList.size()>0){
					//新增记录
					empTransDao.save(conn, addList, LfClient5Pro.class);
					LfClientDepSp depsp = null;
					for(int j=0;j<addList.size();j++){
						LfClient5Pro client = addList.get(j);
						Long clientId = client.getClientId();
						for(String temp:deps){
							if(temp != null && !"".equals(temp)){
								depsp = new LfClientDepSp();
								depsp.setClientId(clientId);
								depsp.setDepId(Long.valueOf(temp));
								depspList.add(depsp);
							}
						}
					}
				}
				LinkedHashMap<String, String> condMap = new LinkedHashMap<String, String>();
				//删除未知机构关联表信息
				for(long unid : unlist){
					condMap.put("clientId", unid+"");
					empTransDao.delete(conn, LfClientDepSp.class, condMap);
				}
				if(depspList != null && depspList.size()>0){
					empTransDao.save(conn, depspList, LfClientDepSp.class);
				}
				empTransDao.commitTransaction(conn);
				returnmsg = "success";
			}catch (Exception e) {
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e,"处理 客户上传出现异常！");
			}finally{
				empTransDao.closeConnection(conn);
			}
			return returnmsg;
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
			return lfClientVoDAO.sameDepNameOrDepCode(depName, depCode, scode, corpcode);
		}
}
