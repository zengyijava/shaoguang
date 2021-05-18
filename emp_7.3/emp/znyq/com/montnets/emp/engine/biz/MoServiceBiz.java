package com.montnets.emp.engine.biz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.engine.dao.MoServiceDao;
import com.montnets.emp.entity.engine.LfMoService;
import com.montnets.emp.entity.engine.LfProCon;
import com.montnets.emp.entity.engine.LfService;
import com.montnets.emp.entity.pasgroup.AcmdPort;
import com.montnets.emp.entity.pasgroup.AcmdRoute;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sysuser.LfSysuser;

/**
 * 
 * @author Administrator
 *
 */
public class MoServiceBiz
{
	//数据库操作dao
	private IEmpDAO empDao;
	//数据库操作dao，用于事务
	private IEmpTransactionDAO empTransDao;
	public MoServiceBiz(){
		//初始化数据库操作dao
		empDao=new DataAccessDriver().getEmpDAO();
		empTransDao = new DataAccessDriver().getEmpTransDAO();
		
	}
	
	
	/**
	 * 删除上行业务
	 * @param serId
	 * @return
	 * @throws Exception
	 */
	public Integer deleteUpService(Long serId) throws Exception {
		
		//结果数
		Integer resultCount = 0;
		//获取连接
		Connection conn = empTransDao.getConnection();
		try{
			LfService service = empDao.findObjectByID(LfService.class, serId);
			
			ProcessConfigBiz processBiz = new ProcessConfigBiz();
			//打开事务
			empTransDao.beginTransaction(conn);
			processBiz.delUpProcessBySerId(conn, serId);
			AppLogBiz serLog = new AppLogBiz();
			serLog.delSerLogBySerId(conn, serId);
			this.delMoServiceBySerId(conn, serId);
			
			//如果使用指令，所以要删除指令
			if(service.getIdentifyMode() == 2){
				
				//删除上行业务对应网关指令路由绑定表
				boolean delCmdRes = this.delMoSerCmd(conn, service);
				if(!delCmdRes){
					empTransDao.rollBackTransaction(conn);
					return 0;
				}
			}
			
			resultCount = empTransDao.delete(conn, LfService.class, serId.toString()); 
			//提交事务
			empTransDao.commitTransaction(conn);
		}catch(Exception e){
			EmpExecutionContext.error(e,"删除上行业务异常");
			//异常回滚
			empTransDao.rollBackTransaction(conn);
		}finally{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		return resultCount;
	}
	
	/**
	 * 删除上行业务
	 * @param serId 业务id
	 * @param corpCode 企业编码
	 * @return 大于0表示成功。-1：传入参数为空；-2：获取不到上行业务对象；-9999：异常
	 */
	public int deleteUpService(String serId, String corpCode)
	{
		if(serId == null || serId.trim().length() < 1 || corpCode == null || corpCode.trim().length() < 1)
		{
			EmpExecutionContext.error("删除上行业务，传入参数为空。serId="+serId+",corpCode="+corpCode);
			return -1;
		}
		
		ServiceBiz serBiz = new ServiceBiz();
		LfService service = serBiz.getService(serId, corpCode);
		if(service == null)
		{
			EmpExecutionContext.error("删除上行业务，上行业务对象为空。serId="+serId+",corpCode="+corpCode);
			return -2;
		}
		
		//获取连接
		Connection conn = empTransDao.getConnection();
		try
		{
			//打开事务
			empTransDao.beginTransaction(conn);

			ProcessConfigBiz processBiz = new ProcessConfigBiz();
			processBiz.delUpProcessBySerId(conn, service.getSerId());
			AppLogBiz serLog = new AppLogBiz();
			serLog.delSerLogBySerId(conn, service.getSerId());
			this.delMoServiceBySerId(conn, service.getSerId());
			
			//如果使用指令，所以要删除指令
			if(service.getIdentifyMode() == 2)
			{
				//删除上行业务对应网关指令路由绑定表
				boolean delCmdRes = this.delMoSerCmd(conn, service);
				if(!delCmdRes)
				{
					empTransDao.rollBackTransaction(conn);
					return 0;
				}
			}
			
			int resultCount = empTransDao.delete(conn, LfService.class, serId.toString()); 
			//提交事务
			empTransDao.commitTransaction(conn);
			return resultCount;
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e,"删除上行业务，异常。serId="+serId+",corpCode="+corpCode);
			//异常回滚
			empTransDao.rollBackTransaction(conn);
			return -9999;
		}
		finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
	}
	
/**
 * 
 * @param msg
 * @return
 * @throws Exception
 */
	public LfService getUpServiceByOrderCode(String msg, String corpCode) {

		try
		{
			if(corpCode == null || corpCode.trim().length() == 0)
			{
				EmpExecutionContext.error("上行业务处理，获取上行业务对象。企业编码为空。"
						+ "msg=" + msg
						+ ",corpCode=" + corpCode		
				);
				return null;
			}
			else if(msg == null || msg.length() == 0)
			{
				EmpExecutionContext.error("上行业务处理，获取上行业务对象。上行信息内容为空。"
						+ "msg=" + msg
						+ ",corpCode=" + corpCode		
				);
				return null;
			}

			//获取所有上行服务
			List<LfService> servicesList = this.getAllUpService(corpCode);
			if(servicesList == null || servicesList.size() == 0)
			{
				EmpExecutionContext.error("上行业务处理，获取上行业务对象，上行业务对象集合为空。"
						+ "msg=" + msg
						+ ",corpCode=" + corpCode		
				);
				return null;
			}
			
			//服务对象
			LfService service = null;
			LfService tempService = null;
			//指令代码
			String msgOrderCode = null;
			//根据存放分隔符在上行信息中的索引
			int orderIndex = -1;
			//循环处理服务集合
			for (int i = 0; i < servicesList.size(); i++)
			{
				//获取集合中服务对象
				tempService = servicesList.get(i);
				if(tempService.getOrderCode() == null)
				{
					continue;
				}
				//上行信息内容即为指令
				if(tempService.getOrderCode().toLowerCase().equals(msg.toLowerCase()))
				{
					service = tempService;
					break;
				}
				
				//到这里，说明是动态的指令，即指令用分隔符带参数
				//根据分隔符截取出该分隔符在上行信息中的索引
				orderIndex = msg.indexOf(tempService.getMsgSeparated());
				//该上行信息不是使用这种分隔符
				if(orderIndex == -1)
				{
					continue;
				}
				//用分隔符截取出上行信息的指令
				msgOrderCode = msg.substring(0, orderIndex);
				//判断上行信息中的指令是否就是对应这个上行业务
				if(msgOrderCode.toLowerCase().equals(tempService.getOrderCode().toLowerCase()))
				{
					service = tempService;
					break;
				}
			}
			//返回服务对象
			return service;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务处理，获取上行业务对象，异常。"
					+ "msg=" + msg
					+ ",corpCode=" + corpCode
			);
			return null;
		}
	}
	
	/**
	 * 获取上行业务信息
	 * @return
	 * @throws Exception
	 */
	private List<LfService> getAllUpService(String corpCode) throws Exception {
		//服务对象集合
		List<LfService> servicesList;
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("serType", "1");
			conditionMap.put("corpCode", corpCode);
			//按条件查询
			servicesList = empDao.findListByCondition(LfService.class, conditionMap, null);
		}catch(Exception e){
			EmpExecutionContext.error(e,"获取上行业务信息异常！");
			//异常抛出
			throw e;
		}
		//返回结果
		return servicesList;
	}
	
	/**
	 * 判断上行指令代码是否存在
	 * @param orderCode
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	private boolean isServiceOrderCodeExists(String orderCode,String corpCode) throws Exception {
		//保存结果
		boolean result = true;
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("orderCode", orderCode);
			conditionMap.put("corpCode", corpCode);
			//按条件查询获取结果
			List<LfService> servicesList = empDao.findListByCondition(LfService.class, conditionMap, null);
			if(servicesList == null || servicesList.size() == 0){
				//没记录则返回空
				result = false;
			}
		}catch(Exception e){
			EmpExecutionContext.error(e,"判断上行指令代码是否存在异常！");
			//异常抛出
			throw e;
		}
		//返回结果
		return result;
	}
	
	/**
	 * 判断网关指令代码是否存在
	 * @param structcode
	 * @return 不存在返回false
	 * @throws Exception
	 */
	private boolean isSerCmdExists(String structcode) throws Exception {
		
		try{
			//查询条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("structcode", structcode);
			//按条件查询获取结果
			List<AcmdRoute> cmdsList = empDao.findListByCondition(AcmdRoute.class, conditionMap, null);
			if(cmdsList == null || cmdsList.size() == 0){
				//没记录则返回空
				return false;
			}
			return true;
		}catch(Exception e){
			EmpExecutionContext.error(e,"判断上行指令代码是否存在异常！");
			//异常抛出
			return true;
		}
	}
	
	/**
	 * 
	 * @param orderCode
	 * @return
	 * @throws Exception
	 */
	public boolean isOrderCodeExists(String orderCode, String lgcorpcode, String orderType, String identifyMode, String msgSeparated, String curIdentifyMode, String curCode) throws Exception {
		
		//执行判断指令代码是否存在并返回结果
		boolean serResult = true;
		
		//指令没改，状态改了，且原来是使用尾号的，则不检查业务表的指令
		if(curCode.equals(orderCode) && !curIdentifyMode.equals(identifyMode) && "1".equals(curIdentifyMode)){
			serResult = false;
		}
		else{
			serResult = this.isServiceOrderCodeExists(orderCode,lgcorpcode);
		}
		
		//判断网关指令是否存在
		String structcode = null;
		//指令类型。1-动态指令（模糊）；0-静态指令（精确）
		if(orderType != null && orderType.length() > 0 && "1".equals(orderType)){
			structcode = orderCode + msgSeparated;
		}else{
			structcode = orderCode;
		}
		boolean serCmdRes = false;
		//识别模式。空和1-使用尾号；2-使用指令，使用网关指令才判断是否存在
		if("2".equals(identifyMode)){
			serCmdRes = this.isSerCmdExists(structcode);
		}
		
		//已存在
		if(!serResult && !serCmdRes){
			//返回false
			return false;
		}
		//返回结果
		return true;
	}
	
	/**
	 * 删除上行业务
	 * @param conn
	 * @param serId
	 * @return
	 * @throws Exception
	 */
	public Integer delMoServiceBySerId(Connection conn, Long serId) throws Exception {
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//结果记录数
		Integer result = 0;
		try{
			conditionMap.put("serId", serId.toString());
			//执行删除并返回结果
			result = empTransDao.delete(conn, LfMoService.class, conditionMap);
		}catch(Exception e){
			EmpExecutionContext.error(e,"删除上行业务异常！");
			//异常抛出
			throw e;
		}
		//返回结果
		return result;
	}
	
	/**
	 * 删除上行业务对应网关指令路由绑定表
	 * @param conn
	 * @param service 上行业务对象
	 * @return 成功返回true
	 */
	public boolean delMoSerCmd(Connection conn, LfService service) {
		
		try{
			AcmdRoute cmd = this.getCmdRoute(service);
			if(cmd == null){
				EmpExecutionContext.error("删除上行业务对应指令失败，指令对象为null。");
				return false;
			}
			
			//删除通道指令
			boolean delRes = delCmdPortByCmdId(conn, cmd.getId());
			if(!delRes){
				EmpExecutionContext.error("删除上行业务对应通道指令失败。");
				return false;
			}
			
			//执行删除并返回结果
			Integer result = empTransDao.delete(conn, AcmdRoute.class, cmd.getId().toString());
			if(result > 0){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			EmpExecutionContext.error(e,"删除上行业务对应网关指令路由绑定表异常。");
			//异常抛出
			return false;
		}
	}
	
	/**
	 * 
	 * @param conn
	 * @param prId
	 * @return
	 * @throws Exception
	 */
//	public Integer delMoServiceByPrId(Connection conn, Long prId) throws Exception {
//		//保存结果
//		Integer result;
//		//查询条件
//		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
//		try{
//			conditionMap.put("prId", prId.toString());
//			//执行删除并返回结果
//			result = empDao.delete(LfMoService.class, conditionMap);
//		}catch(Exception e){
//			EmpExecutionContext.error(e,"删除上行业务异常！");
//			//异常抛出
//			throw e;
//		}
//		//返回结果
//		return result;
//	}
	
	/**
	 * 
	 * @param proConList
	 * @param prId
	 * @return
	 * @throws Exception
	 */
	public boolean updateProCon(List<LfProCon> proConList,Long prId) throws Exception {
		//保存结果
		boolean result = true;
		//获取连接
		Connection conn = empTransDao.getConnection();
		try{
			//开始事务
			empTransDao.beginTransaction(conn);
			//执行删除
			this.delProConByPrId(conn, prId);
			if(proConList != null && proConList.size() > 0){
				empTransDao.save(conn, proConList, LfProCon.class);
			}
			/*for(int i=0;i<proConList.size();i++)
			{
				empTransDao.save(conn, proConList.get(i));
			}*/
			//提交事务
			empTransDao.commitTransaction(conn);
		}catch(Exception e){
			//异常回滚事务并抛出异常
			result=false;
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"修改数据库配置异常！ ");
			throw e;
		}finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		//返回结果
		return result;
	}
	
	/**
	 * 删除步骤
	 * @param conn
	 * @param prId 步骤id
	 * @return
	 * @throws Exception
	 */
	private Integer delProConByPrId(Connection conn, Long prId) throws Exception {
		//保存结果
		Integer result = 0;
		//查询条件
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("prId", prId.toString());
			//执行删除并返回结果
			result = empTransDao.delete(conn, LfProCon.class, conditionMap);
		}catch(Exception e){
			//抛出异常
			result=0;
			EmpExecutionContext.error(e,"删除步骤异常");
 			throw e;
		} 
		//返回结果
		return result;
	}
	
	/**
	 * 保存上行业务
	 * @param service 上行业务对象
	 * @param cmd 网关指令路由对象
	 * @return 成功返回true
	 */
	public Long addMoService(LfService service, LfSysuser sysuser){
		
		Connection conn = empTransDao.getConnection();
		try{
			//开始事务
			empTransDao.beginTransaction(conn);
			
			//保存上行业务对象
			Long serId = empTransDao.saveObjectReturnID(conn, service);
			
			//获取网关指令对象
			AcmdRoute cmd = setCmdRoute(service, sysuser);
			if(cmd == null){
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error("保存上行业务失败，获取网关指令对象为null。");
				return null;
			}
			
			//保存网关指令对象
			Long cmdId = empTransDao.saveObjectReturnID(conn, cmd);
			if(cmdId == null || cmdId == 0){
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error("保存上行业务失败，保存网关指令路由返回Id不正确。cmdId="+cmdId);
				return null;
			}
			
			//保存网关通道指令集合
			List<AcmdPort> portList = setCmdPort(service, cmdId);
			if(portList == null || portList.size() == 0){
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error("保存上行业务失败，获取网关通道指令对象集合为空。");
				return null;
			}
			
			empTransDao.save(conn, portList, AcmdPort.class);
			
			//提交事务
			empTransDao.commitTransaction(conn);
			return serId;
		}catch(Exception e){
			//异常回滚事务并抛出异常
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"保存上行业务异常。 ");
			return null;
		}finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 修改上行业务，删除网关指令路由
	 * @param service 上行业务对象
	 * @param cmd 网关指令路由对象
	 * @return 成功返回true
	 */
	public boolean updateMoSerToDel(LfService service, AcmdRoute cmd){
		
		Connection conn = empTransDao.getConnection();
		try{
			
			//开始事务
			empTransDao.beginTransaction(conn);
			
			empTransDao.update(conn, service);
			
			//删除通道指令
			boolean delRes = delCmdPortByCmdId(conn, cmd.getId());
			if(!delRes){
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error("修改上行业务，删除网关指令路由失败，先删除记录失败。");
				return false;
			}
			
			//删除网关指令
			empTransDao.delete(conn, AcmdRoute.class, cmd.getId().toString());
			
			//提交事务
			empTransDao.commitTransaction(conn);
			return true;
		}catch(Exception e){
			//异常回滚事务并抛出异常
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"修改上行业务，删除网关指令路由异常。 ");
			return false;
		}finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 修改上行业务，删除网关指令路由
	 * @param service 上行业务对象
	 * @param cmd 网关指令路由对象
	 * @return 成功返回true
	 */
	public boolean updateMoSer(LfService service, AcmdRoute cmd){
		
		Connection conn = empTransDao.getConnection();
		try{
			
			//开始事务
			empTransDao.beginTransaction(conn);
			
			empTransDao.update(conn, service);
			
			empTransDao.update(conn, cmd);
			
			boolean updatePortRes = updateCmdPort(conn, service, cmd.getId());
			if(!updatePortRes){
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error("修改上行业务，更新通道指令失败。");
				return false;
			}
			
			//提交事务
			empTransDao.commitTransaction(conn);
			return true;
		}catch(Exception e){
			//异常回滚事务并抛出异常
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"修改上行业务异常。 ");
			return false;
		}finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 更新网关指令id对应的通道指令
	 * @param conn 数据库连接
	 * @param service 上行业务对象
	 * @param cmdId 网关指令id
	 * @return 成功返回ture
	 */
	private boolean updateCmdPort(Connection conn, LfService service, Long cmdId){
		try
		{
			//先删除
			boolean delRes = delCmdPortByCmdId(conn, cmdId);
			if(!delRes){
				EmpExecutionContext.error("更新通道指令失败，先删除记录失败。");
				return false;
			}
			
			List<AcmdPort> portList = setCmdPort(service, cmdId);
			if(portList == null || portList.size() == 0){
				EmpExecutionContext.error("更新通道指令失败，获取网关通道指令对象集合为空。");
				return false;
			}
			
			//保存网关通道指令集合
			empTransDao.save(conn, portList, AcmdPort.class);
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新网关指令id对应的通道指令异常。");
			return false;
		}
	}
	
	/**
	 * 根据cmdId删除通道指令记录
	 * @param conn 数据库连接
	 * @param cmdId 网关指令id
	 * @return 成功返回true
	 */
	private boolean delCmdPortByCmdId(Connection conn, Long cmdId){
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("cmdId", cmdId.toString());
			empTransDao.delete(conn, AcmdPort.class, conditionMap);
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据cmdId删除通道指令记录异常。");
			return false;
		}
	}
	
	/**
	 * 修改上行业务，新增网关指令路由
	 * @param service 上行业务对象
	 * @param cmd 网关指令路由对象
	 * @return 成功返回true
	 */
	public boolean updateMoSerToAdd(LfService service, LfSysuser sysuser){
		
		Connection conn = empTransDao.getConnection();
		try{
			
			//开始事务
			empTransDao.beginTransaction(conn);
			
			empTransDao.update(conn, service);
			
			AcmdRoute cmd = setCmdRoute(service, sysuser);
			if(cmd == null){
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error("修改上行业务，新增网关指令路由失败，获取网关指令对象为null。");
				return false;
			}
			Long cmdId = empTransDao.saveObjectReturnID(conn, cmd);
			
			//保存网关通道指令集合
			List<AcmdPort> portList = setCmdPort(service, cmdId);
			if(portList == null || portList.size() == 0){
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error("修改上行业务，新增网关指令路由失败，获取网关通道指令对象集合为空。");
				return false;
			}
			
			empTransDao.save(conn, portList, AcmdPort.class);
			
			//提交事务
			empTransDao.commitTransaction(conn);
			return true;
		}catch(Exception e){
			//异常回滚事务并抛出异常
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"修改上行业务，新增网关指令路由异常。 ");
			return false;
		}finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 保存指令到网关表
	 * @param uid SP账号(对应userdata表中uid)
	 * @param service 上行业务对象
	 * @param sysuser 当前登录操作员
	 * @return 保存成功返回true
	 */
	public AcmdRoute setCmdRoute(LfService service, LfSysuser sysuser){
		try
		{
			//AcmdRoute cmd
			Long uid = this.getUidFromSpuser(service.getSpUser());
			if(uid == null || service == null || sysuser == null){
				return null;
			}
			AcmdRoute cmd = new AcmdRoute();
			cmd.setName(service.getSerName());
			//指令代码(包含分隔符)
			cmd.setStructcode(service.getStructcode());
			//SP账号(对应userdata表中uid)
			cmd.setSpid(uid);
			cmd.setCreater(sysuser.getUserName());
			//指令类型01:智能引擎 02:接口接入
			cmd.setTructtype("01");
			//指令状态01:启用 02:引擎绑定
			cmd.setStatus("01");
			//指令匹配模式 0 静态指令，完全匹配  1 动态指令，左完全匹配
			cmd.setMatchmode(service.getOrderType());
			//业务系统名称
			cmd.setBussysname("智能引擎");
			//创建时间
			cmd.setCreattime(new Timestamp(System.currentTimeMillis()));
			//指令类型： 0 全局指令 1 通道指令
			cmd.setCmdType(1);
			return cmd;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"智能引擎上行业务设置网关指令异常。");
			return null;
		}
	}
	
	/**
	 * 保存通道指令到网关表
	 * @param uid SP账号(对应userdata表中uid)
	 * @param service 上行业务对象
	 * @param sysuser 当前登录操作员
	 * @return 保存成功返回true
	 */
	public List<AcmdPort> setCmdPort(LfService service, Long cmdId){
		try
		{
			List<DynaBean> portList = new MoServiceDao().getPortFromSpuser(service.getSpUser());
			if(portList == null || portList.size() == 0){
				EmpExecutionContext.error("设置通道指令对象失败，获取sp发送账号绑定路由为空。");
				return null;
			}
			
			List<AcmdPort> cmdPortList = new ArrayList<AcmdPort>();
			AcmdPort cmdPort = null;
			for(DynaBean port : portList){
				cmdPort = new AcmdPort();
				cmdPort.setGateId(Long.parseLong(port.get("gateid").toString()));
				cmdPort.setCmdId(cmdId);
				//绑定状态：0 启动 1 禁用
				cmdPort.setStatus(0);
				if(port.get("cpno") != null){
					cmdPort.setCpno(port.get("cpno").toString());
				}
				//通道指令匹配失败后，对上行的处理：0 进行子号路由;1 按默认指令处理;2 中断，写库
				cmdPort.setFailOpt(0);
				//cmdPort.setDefCmdId(0);
				
				cmdPortList.add(cmdPort);
			}
			
			return cmdPortList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"智能引擎上行业务设置网关指令异常。");
			return null;
		}
	}
	
	/**
	 * 获取上行业务绑定的网关指令路由
	 * @param service 上行业务对象
	 * @return 返回网关指令路由对象
	 */
	public AcmdRoute getCmdRoute(LfService service){
		try
		{
			//AcmdRoute cmd
			Long uid = this.getUidFromSpuser(service.getSpUser());
			if(uid == null || service == null){
				return null;
			}
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("spid", uid.toString());
			conditionMap.put("structcode", service.getStructcode());
			List<AcmdRoute> cmdsList = empDao.findListByCondition(AcmdRoute.class, conditionMap, null);
			if(cmdsList != null && cmdsList.size() > 0){
				return cmdsList.get(0);
			}
			return null;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"智能引擎上行业务设置网关指令异常。");
			return null;
		}
	}
	
	/**
	 * 根据spuser获取其uid
	 * @param spUser
	 * @return 返回uid，没有则返回null
	 */
	private Long getUidFromSpuser(String spUser){
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", spUser);
			//SP账号类型	(1短信SP账号，2彩信SP账
			conditionMap.put("accouttype", "1");
			List<Userdata> userdatasList = empDao.findListByCondition(Userdata.class, conditionMap, null);
			if(userdatasList != null && userdatasList.size() > 0){
				return userdatasList.get(0).getUid();
			}
			return null;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"智能引擎上行业务设置网关指令异常。");
			return null;
		}
	}
	
	/**
	 * 检查账号是否可用
	 * 
	 * @param userId
	 *        账号id
	 * @param moUrlNeed
	 *        1:需要上行url，2：不需要上行url
	 * @param rptUrlNeed
	 *        1:需要状态报告url，2：不需要状态报告url
	 * @return
	 *         checksuccess：检查通过，可用；checkfail:检查不通过，不可用；nouserid：账号为空；nomourl：没配置上行url
	 *         ；norpturl：没配置状态报告url；checkerror：检查异常；moconnfail:上行url检测连接失败;
	 *         rptconnfail:rpturl检测连接失败
	 */
	public String checkSpUser(String userId, Integer moUrlNeed, Integer rptUrlNeed)
	{
		String result = "checkerror";
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", userId);
			conditionMap.put("accouttype", "1");
			List<Userdata> userdataList = empDao.findListByCondition(Userdata.class, conditionMap, null);
			if(userdataList == null || userdataList.size() == 0)
			{
				// 返回没账号错误
				return "nouserid";
			}
			Userdata userdata = userdataList.get(0);
			result = this.checkSpUser(userdata, moUrlNeed, rptUrlNeed);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务检测URL，检查账号是否可用异常。userId="+userId);
			return "checkerror";
		}
		return result;
	}
	
	/**
	 * @param userdata
	 * @param moUrlNeed
	 *        1:需要上行url，2：不需要上行url
	 * @param rptUrlNeed
	 *        1:需要状态报告url，2：不需要状态报告url
	 * @return
	 *         checksuccess：检查通过，可用；checkfail:检查不通过，不可用；nouserid：账号为空；nomourl：没配置上行url
	 *         ；norpturl：没配置状态报告url；checkerror：检查异常；moconnfail:上行url检测连接失败;
	 *         rptconnfail:rpturl检测连接失败
	 */
	public String checkSpUser(Userdata userdata, Integer moUrlNeed, Integer rptUrlNeed)
	{
		try
		{
			if(userdata == null || userdata.getUserId() == null || "".equals(userdata.getUserId().trim()))
			{
				// 账号id为空
				return "nouserid";
			}

			// 验证mourl结果
			boolean moUrlResult = false;
			if(moUrlNeed == 1)
			{
				// 需要上行
				if(userdata.getMoUrl() == null || "".equals(userdata.getMoUrl().trim()))
				{
					// 账号没有上行url
					return "nomourl";
				}
				// 取出配置emp访问地址url
				String checkUrlResult = checkHttpUrl(userdata.getMoUrl(), 1);
				if("recsuccess".equals(checkUrlResult))
				{
					// 连通成功
					moUrlResult = true;
				}
				else
				{
					// 失败
					moUrlResult = false;
					return "moconnfail";
				}
			}
			else
			{
				// 不需要上行
				moUrlResult = true;
			}

			// 验证rpturl结果
			boolean rptUrlResult = false;
			if(rptUrlNeed == 1)
			{
				// 需要状态报告
				if(userdata.getRptUrl() == null || "".equals(userdata.getRptUrl().trim()))
				{
					// 账号没有上行url
					return "norpturl";
				}
				// 取出配置emp访问地址url
				String checkUrlResult = checkHttpUrl(userdata.getRptUrl(), 2);
				if("recsuccess".equals(checkUrlResult))
				{
					// 连通成功
					rptUrlResult = true;
				}
				else
				{
					// 失败
					rptUrlResult = false;
					return "rptconnfail";
				}
			}
			else
			{
				// 不需要状态报告
				rptUrlResult = true;
			}

			if(moUrlResult && rptUrlResult)
			{
				// 验证通过
				return "checksuccess";
			}
			else
			{
				return "checkfail";
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务检测URL，检查sp账号是否配置可用url异常。UserId="+userdata.getUserId()+",moUrl="+userdata.getMoUrl()+",rptUrl="+userdata.getRptUrl());
			return "checkerror";
		}
	}

	/**
	 * http访问检查url是否连通
	 * @param url 检查地址
	 * @param type 1:mo;2:rpt
	 * @return noresult：无响应结果；recsuccess：成功连通，可用；recfail：成功连通，返回错误；recerror：异常失败;confail：连接失败
	 */
	public String checkHttpUrl(String url, Integer type)
	{
		try
		{
			
			String command = "";
			//String stat = "";
			String testcodevalue = "";
			if(type == 1)
			{
				//mo
				command = "MO_TEST";
				testcodevalue = "motest";
			}
			else if(type == 2)
			{
				//rpt
				command = "RT_TEST";
				testcodevalue = "rpttest";
			}
			else
			{
				//类型错误，无该类型
				return "typeerror";
			}
			
			StringBuffer paramEntity =new StringBuffer("command="+command);
			
			String responseStr = requestHttp("POST",url,paramEntity.toString(),5*1000);
			if(responseStr == null)
			{
				return "confail";
			}

			if(responseStr.length()==0)
			{
				//无响应结果
				return "noresult";
			}
			
			//处理响应
			
			String testcode = responseStr.substring(responseStr.lastIndexOf("=")+1);
			
			if(testcode.trim().equals(testcodevalue))
			{
				//成功接收上行请求
				return "recsuccess";
			}
			else {
				//失败
				return "recfail";
			}
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e,"上行业务检测URL，解析http响应异常。url="+url+",type="+type);
			return "recerror";
		}
	}
	
	/**
	 * 请求http
	 * @param method 请求方式，GET/POST
	 * @param destUrl 请求目标url
	 * @param param 请求参数
	 * @param timeout 超时毫秒数，默认1分钟连接不上则超时
	 * @return 返回回应字符串
	 */
	public String requestHttp(String method, String destUrl, String param, Integer timeout) { 
		
		HttpURLConnection huc = null;
		URL url = null;
		String result="";
		InputStream in = null;
		BufferedReader breader =null;
		try{
			//建立链接
			url = new URL(destUrl);
			
			huc = (HttpURLConnection) url.openConnection();
			
			huc.setDoInput(true);  
			huc.setDoOutput(true);
			if(timeout == null)
			{
				//请求超时，默认1分钟
				huc.setConnectTimeout(60*1000);
			}
			else
			{
				huc.setConnectTimeout(timeout);
			}
			
			if("POST".equals(method)){
				huc.setUseCaches(false);
			}
			//设置请求方式
			huc.setRequestMethod(method);
			//建立连接
			huc.connect();
			//写参数
			if(param != null && !"".equals(param)){
				huc.getOutputStream().write(param.getBytes("utf-8"));
				huc.getOutputStream().flush();
				huc.getOutputStream().close();
			}
			
			//int code = huc.getResponseCode();
			// 获取页面内容
			in = huc.getInputStream();
			breader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String str = breader.readLine();
			while (str != null)
			{
				result += str;
				str = breader.readLine();
			}
			//返回结果
			return result;
		}
		catch(SocketTimeoutException e)
		{
			EmpExecutionContext.error(e,"上行业务检测URL，请求超时。destUrl="+destUrl+",param="+param);
			return null;
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e,"上行业务检测URL，请求http连接异常。destUrl="+destUrl+",param="+param);
			return null;
		}
		finally{
			//关闭
			try {
				if(breader!=null){
					breader.close();
				}
				if(in!=null){
					in.close();
				}
				if(huc != null){
					huc.disconnect();
				}
			} catch (IOException e) {
				EmpExecutionContext.error(e,"上行业务检测URL，关闭资源异常。");
			}
		}
	}
	
}
