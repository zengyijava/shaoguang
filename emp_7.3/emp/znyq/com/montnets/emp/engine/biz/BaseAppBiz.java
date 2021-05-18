package com.montnets.emp.engine.biz;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.engine.vo.dao.GenericLfServiceVoDAO;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.engine.LfService;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.sms.LfTask;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.engine.vo.LfServiceVo;

/**
 * 
 * @author Administrator
 *
 */
public class BaseAppBiz extends SuperBiz
{ 
	//数据库操作dao
	private GenericLfServiceVoDAO lfServiceVoDAO;
	
	public BaseAppBiz(){
		//初始化数据库操作dao
		lfServiceVoDAO=new GenericLfServiceVoDAO();
		//初始化数据库操作dao
	}
/**
 * 
 * @param lfServiceVo
 * @param userId
 * @param pageInfo
 * @return
 * @throws Exception
 */
	public List<LfServiceVo> getServiceVos(LfServiceVo lfServiceVo,Long userId, PageInfo pageInfo) throws Exception {
		//执行查询
		List<LfServiceVo> serviceVosList = lfServiceVoDAO.findLfServiceVo(lfServiceVo,userId, pageInfo);
		//返回结果
		return serviceVosList;
	}
	
	

	
/**
 * 
 * @param busCode
 * @return
 * @throws Exception
 */
	public List<LfService> getServiceByBusCode(String busCode) throws Exception {
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		//业务编码
		conditionMap.put("busCode", busCode);
		//状态
		conditionMap.put("runState", "1");
		//按条件查询
		List<LfService> servicesList = empDao.findListByCondition(LfService.class, conditionMap, null);
		//返回结果
		return servicesList;
	}
	
/**
 * 
 * @param menuCode
 * @return
 * @throws Exception
 */
	public List<LfService> getServiceByMenuCode(String menuCode) throws Exception {
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		//模块编码
		conditionMap.put("menuCode", menuCode);
		//状态
		conditionMap.put("runState", "1");
		//按条件查询
		List<LfService> servicesList = empDao.findListByCondition(LfService.class, conditionMap, null);
		//返回结果
		return servicesList;
	}
	
/**
 * 
 * @param loginUserID
 * @param menuCode
 * @return
 * @throws Exception
 */
	public List<LfService> getServiceByMenuCode(Long loginUserID, String menuCode) throws Exception {
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		//模块编码
		conditionMap.put("menuCode", menuCode);
		//状态
		conditionMap.put("runState", "1");
		//按条件查询
		List<LfService> servicesList = empDao.findListBySymbolsCondition(loginUserID, LfService.class, conditionMap, null);
		//返回结果
		return servicesList;
	}
	
/**
 * 删除业务
 * @param serId
 * @return
 * @throws Exception
 */
	public Integer deleteService(Long serId) throws Exception {
		//步骤处理biz
		ProcessConfigBiz processBiz = new ProcessConfigBiz();
		//日志biz
		AppLogBiz serLog = new AppLogBiz();
		//结果数
		Integer resultCount = 0;
		//获取连接
		Connection conn = empTransDao.getConnection();
		try{
			//开启事务
			empTransDao.beginTransaction(conn);
			//删除步骤
			processBiz.deleProcessBySerId(conn, serId);
			//删除日志
			serLog.delSerLogBySerId(conn, serId);
			//删除服务
			resultCount = empTransDao.delete(conn, LfService.class, serId.toString());
			//提交事务
			empTransDao.commitTransaction(conn);
		}catch(Exception e){
			//异常回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"删除业务异常serId:"+serId.toString());
		}finally{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		//返回结果
		return resultCount;
	}
	
		/**
		 * 删除业务
		 * @param serId 业务id
		 * @param corpCode 企业编码
		 * @return 大于0表示成功。-1：传入参数为空；-2：获取不到上行业务对象；-9999：异常
		 */
		public int deleteService(String serId, String corpCode)
		{
			if(serId == null || serId.trim().length() < 1 || corpCode == null || corpCode.trim().length() < 1)
			{
				EmpExecutionContext.error("删除下行业务，传入参数为空。serId="+serId+",corpCode="+corpCode);
				return -1;
			}
			
			ServiceBiz serBiz = new ServiceBiz();
			LfService service = serBiz.getService(serId, corpCode);
			if(service == null)
			{
				EmpExecutionContext.error("删除下行业务，下行业务对象为空。serId="+serId+",corpCode="+corpCode);
				return -2;
			}
			
			//获取连接
			Connection conn = empTransDao.getConnection();
			try{
				//开启事务
				empTransDao.beginTransaction(conn);
				
				//步骤处理biz
				ProcessConfigBiz processBiz = new ProcessConfigBiz();
				//删除步骤
				processBiz.deleProcessBySerId(conn, service.getSerId());
				
				//日志biz
				AppLogBiz serLog = new AppLogBiz();
				//删除日志
				serLog.delSerLogBySerId(conn, service.getSerId());
				
				//删除服务
				int resultCount = empTransDao.delete(conn, LfService.class, service.getSerId().toString());
				//提交事务
				empTransDao.commitTransaction(conn);
				//返回结果
				return resultCount;
			}
			catch(Exception e)
			{
				//异常回滚
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e, "删除下行业务，异常。serId="+serId+",corpCode="+corpCode);
				return -9999;
			}
			finally
			{
				//关闭连接
				empTransDao.closeConnection(conn);
			}
		}
	
/**
 * 判断业务名称是否重复
 * @param serName
 * @param serType
 * @return
 * @throws Exception
 */
	public boolean isSerNameExists(String serName,String serType,String corpCode) throws Exception {
		//结果
		boolean result = true;
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		
		try {
			//服务名
			conditionMap.put("serName", serName);
			//类型
			conditionMap.put("serType", serType);
			//企业编码
			conditionMap.put("corpCode", corpCode);
			//执行查询
			List<LfService> tempList = empDao.findListByCondition(LfService.class, conditionMap, null);
			//没记录
			if(tempList == null || tempList.size()==0){
				result = false;
			}

		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e," 判断业务名称是否重复异常！");
			throw e;
		}
		//返回结果
		return result;
	}
	
	/**
	 * 修改业务状态
	 * @param serId
	 * @param runState
	 * @return
	 * @throws Exception
	 */
	public boolean changeServiceState(String serId,String runState) throws Exception
	{
		//结果
		boolean result = true;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		//更新对象
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String,String>();
		try {
			//业务id
			conditionMap.put("serId", serId);
			//状态
			objectMap.put("runState", runState);
			//执行更新
			result=empDao.update(LfService.class,objectMap,conditionMap);

		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"修改业务状态异常！");
			throw e;
		}
		//返回结果
		return result;
	}
	
	/**
	 * 
	 * @param service
	 * @return
	 * @throws Exception
	 */
	public Long addSerTask(LfService service) throws Exception {
		//任务对象
		LfTask newTask = new LfTask();
		newTask.setSlId(service.getSerId());
		newTask.setTaskType(0);
		//操作员id不为空
		if (service.getUserId() != null)
		{
			//获取操作员对象
			LfSysuser sysuser = empDao.findObjectByID(LfSysuser.class, service.getUserId());
			//设置机构id
			newTask.setDepId(sysuser.getDepId());
			//设置操作员登录名
			newTask.setUserName(sysuser.getUserName());
			//设置操作员id
			newTask.setUserId(sysuser.getUserId());
			//设置企业编码
			newTask.setCorpCode(sysuser.getCorpCode());
		}
		//设置发送账号
		newTask.setSpUser(service.getSpUser());
		
		//全局变量biz
		GlobalVariableBiz globalBiz = GlobalVariableBiz.getInstance();
		//获取任务id
		Long taskId = globalBiz.getValueByKey("taskId", 1L);
		//设置任务id
		newTask.setTaskId(taskId);
		//保存到数据库
		empDao.saveObjectReturnID(newTask);
		//返回任务id
		return taskId;
	}
	
/**
 * 
 * @param spUserId
 * @return
 * @throws Exception
 */
	public String getSpPwdBySpUserId(String spUserId) throws Exception {
		//发送账号对象
		Userdata spUser = new Userdata();
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		//发送账号
		conditionMap.put("userId", spUserId);
		//按条件查询
		List<Userdata> tempList = empDao.findListByCondition(Userdata.class, conditionMap, null);
		//有记录
		if(tempList != null && tempList.size() == 1){
			//获取第一条
			spUser = tempList.get(0);
		}
		
		//返回发送账号密码
		return spUser.getUserPassword();
	}
	
	
}
