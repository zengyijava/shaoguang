package com.montnets.emp.engine.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.engine.vo.LfServicelogVo;
import com.montnets.emp.engine.vo.dao.GenericLfServicelogVoDAO;
import com.montnets.emp.entity.engine.LfServicelog;
import com.montnets.emp.util.PageInfo;


/**
 * 
 * @author Administrator
 *
 */
public class AppLogBiz extends SuperBiz{

	//数据库操作dao
	private GenericLfServicelogVoDAO lfServicelogVoDAO;
	
	public AppLogBiz(){
		//初始化数据库操作dao
		lfServicelogVoDAO=new GenericLfServicelogVoDAO();
		//初始化数据库操作dao
		
	}
	
	/**
	 * 业务日志保存
	 * @param state
	 * @param serId
	 * @param url
	 * @return
	 */
	public boolean addNewServiceLog(Integer state,Long serId,String url) {
		try
		{
			//服务日志biz
			LfServicelog serviceLog = new LfServicelog();
			serviceLog.setSlState(state);
			serviceLog.setSerId(serId);
			serviceLog.setUrl(url);
			serviceLog.setRunTime(Timestamp
					.valueOf((new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss"))
							.format(Calendar.getInstance()
									.getTime())));
			//执行保存并返回结果
			boolean result = empDao.save(serviceLog);
			//返回结果
			return result;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "业务日志保存异常。");
			return false;
		}
	}
	
	/**
	 * 业务日志保存并返回id
	 * @param state
	 * @param serId
	 * @param url
	 * @return
	 */
	public Long addServiceLogReturnId(Integer state,Long serId,String url) {
		try
		{
			//服务日志biz
			LfServicelog serviceLog = new LfServicelog();
			serviceLog.setSlState(state);
			serviceLog.setSerId(serId);
			serviceLog.setUrl(url);
			serviceLog.setRunTime(Timestamp
					.valueOf((new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss"))
							.format(Calendar.getInstance()
									.getTime())));
			
			//执行保存并返回id
			Long slId = empDao.saveObjectReturnID(serviceLog);
			//返回id
			return slId;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "业务日志保存并返回id异常。");
			return null;
		}
		
	}
	
	/**
	 * 更新日志
	 * @param slId 日志id
	 * @param state 状态
	 * @param url 发送文件url
	 * @return 成功返回true
	 */
	public boolean updateServiceLog(Long slId, Integer state, String url)
	{
		try
		{
			//结果
			boolean result = false;
			//服务日志biz
			LfServicelog serviceLog = new LfServicelog();
			serviceLog.setSlId(slId);
			serviceLog.setSlState(state);
			serviceLog.setUrl(url);
			//执行更新
			result = empDao.update(serviceLog);
			//返回结果
			return result;
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e,"智能引擎更新运行日志异常");
			return false;
		}

	}
	
/**
 * 获取服务日志信息集合
 * @param curLoginedUserId
 * @param lfServicelogVo
 * @param pageInfo
 * @return
 * @throws Exception
 */
	public List<LfServicelogVo> getSerLogVos(Long curLoginedUserId, LfServicelogVo lfServicelogVo, PageInfo pageInfo) throws Exception {
		//服务日志对象集合
		List<LfServicelogVo> serLogVosList;
		try{
			//分页为空
			if(pageInfo==null)
			{
				//不分页查询
				serLogVosList = lfServicelogVoDAO.findLfServicelogVo(curLoginedUserId, lfServicelogVo);
			}else
			{
				//分页查询
				serLogVosList = lfServicelogVoDAO.findLfServicelogVo(curLoginedUserId, lfServicelogVo, pageInfo);
			}
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e,"获取服务日志信息集合异常");
			throw e;
		}
		//返回结果
		return serLogVosList;
	}
	
/**
 * 获取服务日志对象
 * @param serId
 * @return
 * @throws Exception
 */
	public LfServicelog getServiceLogById(Long serId) throws Exception {
		//服务日志对象
		LfServicelog serLog = null;
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		try{
			conditionMap.put("serId", serId.toString());
			//按条件查询
			List<LfServicelog> tempList = empDao.findListByCondition(LfServicelog.class, conditionMap, null);
			//有记录
			if(tempList != null && tempList.size()==1){
				//获取第一条
				serLog = tempList.get(0);
			}
		}catch(Exception e){
			//处理异常
			EmpExecutionContext.error(e,"获取服务日志对象异常");
			throw e;
		}
		//返回结果
		return serLog;
	}

/**
 * 删除对应业务的服务日志
 * @param conn 数据库连接
 * @param serId 业务ID
 * @return
 * @throws Exception
 */
	public Integer delSerLogBySerId(Connection conn, Long serId) throws Exception {
		//服务id不能为空
		if(serId == null){
			return null;
		}
		//结果
		Integer result = 0;
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("serId", serId.toString());
			//执行删除
			result = empTransDao.delete(conn, LfServicelog.class, conditionMap);
		}catch(Exception e){
			//处理异常
			EmpExecutionContext.error(e,"删除对应业务(serid:"+serId.toString()+")的服务日志异常");
			throw e;
		}
		//返回结果
		return result;
	}


}
