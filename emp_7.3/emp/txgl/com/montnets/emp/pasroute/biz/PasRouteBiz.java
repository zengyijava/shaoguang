package com.montnets.emp.pasroute.biz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.pasroute.LfMmsAccbind;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.pasroute.dao.PasRouteDao;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.servmodule.txgl.entity.AcmdPort;
import com.montnets.emp.servmodule.txgl.entity.AcmdRoute;
import com.montnets.emp.servmodule.txgl.entity.Userdata;
import com.montnets.emp.servmodule.txgl.entity.XtGateQueue;

public class PasRouteBiz extends SuperBiz{
	ErrorLoger errorLoger = new ErrorLoger();
	public List<Userdata> getAllUserdata(int type) throws Exception
	{
		List<Userdata> userDatasList = null;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("uid&>", "100001");
			if (type == 1)
			{
				conditionMap.put("userType", "0");
			} 
			else if (type == 2)
			{
				conditionMap.put("userType", "1");
			}
			orderMap.put("userId", StaticValue.ASC);
			userDatasList = empDao.findListBySymbolsCondition(Userdata.class,
					conditionMap, orderMap);
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"获取所有账户异常！"));
			throw e;
		}
		return userDatasList;
	}
	
	
	public XtGateQueue getGateByGateIsum(String spgate, Integer spisuncm)
	throws Exception
	{
			if (spgate == null || "".equals(spgate.trim()) || spisuncm == null)
			{
				return null;
			}
			
			XtGateQueue gate = null;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			
			try
			{
				conditionMap.put("spgate", spgate);
				conditionMap.put("spisuncm", spisuncm.toString());
				List<XtGateQueue> gatesList = empDao.findListByCondition(
						XtGateQueue.class, conditionMap, null);
				if (gatesList == null || gatesList.size() == 0)
				{
					return null;
				}
				gate = gatesList.get(0);
			
			} catch (Exception e)
			{
				EmpExecutionContext.error(errorLoger.getErrorLog(e,"获取通道号异常！"));
				throw e;
			}
			return gate;
	}
	
	/**
	 * 通过通道号、营运商、账号类型查询通道信息
	 * @description    
	 * @param spgate 通道号
	 * @param spisuncm 通道号
	 * @param gateType 账号类型
	 * @return
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-9-8 下午08:01:09
	 */
	public XtGateQueue getGateByGateIsumAndTyep(String spgate, Integer spisuncm ,Integer gateType)
	throws Exception
	{
			//通过通道号或营运商或账号类型为空
			if (spgate == null || "".equals(spgate.trim()) || spisuncm == null || gateType == null)
			{
				EmpExecutionContext.error("通过通道号、营运商、账号类型查询通道信息参数为空,spgate:"+spgate
						+",spisuncm:"+spisuncm+",gateType:"+gateType);
				return null;
			}
			
			XtGateQueue gate = null;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			
			try
			{
				//设置查询条件
				conditionMap.put("spgate", spgate);
				conditionMap.put("spisuncm", spisuncm.toString());
				conditionMap.put("gateType", gateType.toString());
				List<XtGateQueue> gatesList = empDao.findListByCondition(
						XtGateQueue.class, conditionMap, null);
				//无记录,返回null
				if (gatesList == null || gatesList.size() == 0)
				{
					return null;
				}
				gate = gatesList.get(0);
			
			} catch (Exception e)
			{
				EmpExecutionContext.error(errorLoger.getErrorLog(e,"通过通道号、营运商、账号类型查询通道信息异常！spgate:"+spgate
						+",spisuncm:"+spisuncm+",gateType:"+gateType));
				throw e;
			}
			return gate;
	}
	
	
	public boolean checkRouteBysmsSpUser(String spuser, String plateFormType)
	throws Exception
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("spUser", spuser);
		conditionMap.put("isValidate", "1");
		conditionMap.put("platFormType", plateFormType);
		
		List<LfSpDepBind> gtList = empDao.findListByCondition(
				LfSpDepBind.class, conditionMap, null);
		
		return !(gtList != null && gtList.size() > 0);
	}
	
	
	public boolean checkRouteByMmsSpUser(String mmsuser)
	throws Exception
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("mmsUser", mmsuser);
		conditionMap.put("isValidate", "1");
		
		List<LfMmsAccbind> gtList = empDao.findListByCondition(
				LfMmsAccbind.class, conditionMap, null);
		
		return !(gtList != null && gtList.size() > 0);
	}
	
	
	public Integer deleteRoute(String ids,String gatetype) throws Exception
	{
		Integer deletecount = 0;
		if (ids != null && !"".equals(ids))
		{
			String[] id = ids.split(",");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			try
			{
				for (int i = 0; i < id.length; i++)
				{
					Long d = Long.valueOf(id[i]);
					GtPortUsed gtportuserd = empDao.findObjectByID(
							GtPortUsed.class, d);
					int deletec = empDao.delete(GtPortUsed.class, d.toString());
					deletecount += deletec;
					conditionMap.put("userId", gtportuserd.getUserId());
					List<GtPortUsed> gtportuserds = empDao.findListByCondition(
							GtPortUsed.class, conditionMap, null);
					conditionMap.clear();
					if (gtportuserds.size() == 0)
					{
						if("1".equals(gatetype)){
							conditionMap.put("spUser", gtportuserd.getUserId());
							objectMap.put("isValidate", "0");
							empDao.update(LfSpDepBind.class, objectMap,	conditionMap);
							conditionMap.clear();
							objectMap.clear();
						}else if("2".equals(gatetype)){
							conditionMap.put("mmsUser", gtportuserd.getUserId());
							objectMap.put("isValidate", "0");
							empDao.update(LfMmsAccbind.class, objectMap,	conditionMap);
							conditionMap.clear();
							objectMap.clear();
						}
					}
				}
			} catch (Exception e)
			{
				EmpExecutionContext.error(errorLoger.getErrorLog(e,"删除出现异常！"));
				throw new Exception("删除出现异常");

			} finally
			{
			}
		} else
		{
			throw new Exception("传入的路由id不能为空");
		}
		return deletecount;

	}

	
	public boolean isRoutExists(String spUserId, String spgate,
			String spisuncm, String routeflag,String gateType) throws Exception
	{
		boolean result = true;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("userId", spUserId);
			conditionMap.put("spgate", spgate);
			conditionMap.put("spisuncm", spisuncm);
			conditionMap.put("routeFlag", routeflag);
			conditionMap.put("gateType", gateType);
			
			List<GtPortUsed> protsList = empDao.findListByCondition(
					GtPortUsed.class, conditionMap, null);

			if (protsList == null || protsList.size() == 0)
			{
				result = false;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"判断是否通道绑定是否存在异常！"));
			throw e;
		}
		return result;
	}
	
	public boolean isDownRoutExists(String spUserId, String spisuncm,
			String routeflag,String gateType) throws Exception
	{
		boolean result = true;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{

			conditionMap.put("userId", spUserId);
			conditionMap.put("spisuncm", spisuncm);
			conditionMap.put("routeFlag", routeflag);
			conditionMap.put("gateType", gateType);

			List<GtPortUsed> protsList = empDao.findListByCondition(
					GtPortUsed.class, conditionMap, null);

			if (protsList == null || protsList.size() == 0)
			{
				result = false;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"查找重复异常！"));
			throw e;
		}
		return result;
	}
	
	
	
	public boolean isUpRoutExists(String spUserId, String spgate,
			String spisuncm, String routeflag,String gatetype) throws Exception
	{
		boolean result = true;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("userId", spUserId);
			conditionMap.put("spgate", spgate);
			conditionMap.put("spisuncm", spisuncm);
			// conditionMap.put("cpno", cpno);
			conditionMap.put("routeFlag", routeflag);
			conditionMap.put("gateType", gatetype);

			List<GtPortUsed> protsList = empDao.findListByCondition(
					GtPortUsed.class, conditionMap, null);

			if (protsList == null || protsList.size() == 0)
			{
				result = false;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"查找重复异常！"));
			throw e;
		}
		return result;
	}
	
	
	public boolean isGateUpRoutExists(String spgate,
			String spisuncm, String cpno, String routeflag,String gatetype) throws Exception
	{
		boolean result = true;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("spgate", spgate);
			conditionMap.put("spisuncm", spisuncm);
			conditionMap.put("cpno", cpno);
			conditionMap.put("routeFlag", routeflag);
			conditionMap.put("gateType", gatetype);

			List<GtPortUsed> protsList = empDao.findListByCondition(
					GtPortUsed.class, conditionMap, null);

			if (protsList == null || protsList.size() == 0)
			{
				result = false;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"查找重复异常！"));
			throw e;
		}
		return result;
	}
	
	public boolean isCpnoContains(String spgate, Integer spisuncm,
			String newCpno,String gatetype) throws Exception
	{
		if (newCpno == null || newCpno.trim().length() == 0)
		{
			return false;
		}
		List<GtPortUsed> portsList = this.getUpPortsBySpgate(spgate, spisuncm,gatetype);
		List<LfSubnoAllotDetail> subdetailList = empDao.findListBySymbolsCondition(LfSubnoAllotDetail.class, null, null);
		if ((portsList == null || portsList.size() == 0)&&(subdetailList == null || subdetailList.size() == 0))
		{
			return false;
		}
		GtPortUsed port = null;
		if(portsList!=null){
			for (int i = 0; i < portsList.size(); i++)
			{
				port = portsList.get(i);
				if (port.getCpno() == null)
				{
					throw new NullPointerException("扩展子号为空");
				}
				if (port.getCpno().startsWith(newCpno)
						|| newCpno.startsWith(port.getCpno()))
				{
					return true;
				}
			}
		}
	/*	LfSubnoAllotDetail subdetail = null;
		if(subdetailList!=null){
			for (int i = 0; i < subdetailList.size(); i++)
			{
				subdetail = subdetailList.get(i);
				if (subdetail.getUsedExtendSubno() == null)
				{
					throw new NullPointerException("扩展子号为空");
				}
				if (subdetail.getUsedExtendSubno().startsWith(newCpno)
						|| newCpno.startsWith(subdetail.getUsedExtendSubno()))
				{
					return true;
				}
			}
		}*/
		
		
		
		
		return false;
	}
	
	
	private List<GtPortUsed> getUpPortsBySpgate(String spgate, Integer spisuncm,String gatetype)
	throws Exception
	{
		List<GtPortUsed> portsList;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("spgate", spgate);
			conditionMap.put("spisuncm", spisuncm.toString());
			conditionMap.put("routeFlag&<>", "1");
			conditionMap.put("gateType", gatetype);
			portsList = empDao.findListBySymbolsCondition(GtPortUsed.class,
					conditionMap, null);
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"查找重复异常！"));
			throw e;
		}
		return portsList;
	}
	
	
	
	public String isSpgateBandExists(String spgate, Integer spisuncm,String gatetype)
	throws Exception
	{
	
		return new PasRouteDao().getUserIdBySpgate(spgate, spisuncm,gatetype);
	}
	
	
	
	public boolean isUpdateUpRoutExists(String spUserId, String spgate,
			String spisuncm, String cpno, String routeflag,String gatetype) throws Exception
	{
		boolean result = true;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{

			conditionMap.put("userId&<>", spUserId);
			conditionMap.put("spgate", spgate);
			conditionMap.put("spisuncm", spisuncm);
			conditionMap.put("cpno", cpno);
			conditionMap.put("routeFlag", routeflag);
			conditionMap.put("gateType", gatetype);

			List<GtPortUsed> protsList = empDao.findListBySymbolsCondition(
					GtPortUsed.class, conditionMap, null);

			if (protsList == null || protsList.size() == 0)
			{
				result = false;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"查找重复异常！"));
			throw e;
		}
		return result;
	}

	
	public boolean isCpnoContains(String spgate, Integer spisuncm,
			String newCpno,String userid,String gatetype) throws Exception
	{

		if (newCpno == null || newCpno.trim().length() == 0)
		{
			return false;
		}

		List<GtPortUsed> portsList = this.getUpPortsBySpgate(spgate, spisuncm,userid,gatetype);
		if (portsList == null || portsList.size() == 0)
		{
			return false;
		}
		GtPortUsed port = null;

		for (int i = 0; i < portsList.size(); i++)
		{
			port = portsList.get(i);
			if (port.getCpno() == null)
			{
				throw new NullPointerException("扩展子号为空");
			}
			if (port.getCpno().startsWith(newCpno)
					|| newCpno.startsWith(port.getCpno()))
			{
				return true;
			}
		}
		return false;
	}
	
	private List<GtPortUsed> getUpPortsBySpgate(String spgate, Integer spisuncm,String userid,String gatetype)
	throws Exception
	{
		List<GtPortUsed> portsList;
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			conditionMap.put("spgate", spgate);
			conditionMap.put("spisuncm", spisuncm.toString());
			conditionMap.put("routeFlag&<>", "1");
			conditionMap.put("userId&<>", userid);
			conditionMap.put("gateType", gatetype);
			portsList = empDao.findListBySymbolsCondition(GtPortUsed.class,
					conditionMap, null);
		} catch (Exception e)
		{
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"查询路由绑定异常！"));
			throw e;
		}
		return portsList;
	}
	
	/**
	 * 更新通道指令
	 * @param spUser 发送账号
	 * @param spgate 通道号
	 * @param cpno 子号
	 * @param spisuncm 运营商
	 * @return 成功返回true
	 */
	public boolean updateCmd(String spUser, String spgate, String cpno, Integer spisuncm){
		
		if(spUser == null || spgate == null || cpno == null || spisuncm == null ){
			EmpExecutionContext.error("账户通道配置，更新指令失败，参数不正确。" +
					"spUser="+spUser + "，spgate="+spgate + "，cpno="+cpno+"，spisuncm="+spisuncm);
			return false;
		}
		
		PasRouteDao pasDao = new PasRouteDao();
		String strCmdId = "";
		List<AcmdPort> portList = new ArrayList<AcmdPort>();
		
		try
		{
			//获取账号所绑定的指令
			List<AcmdRoute> cmdList = pasDao.getSpUserCmd(spUser);
			if(cmdList == null){
				EmpExecutionContext.error("账户通道配置，更新指令，获取指令集合失败。");
				return false;
			}
			//无绑定指令，则不用更新
			if(cmdList.size() == 0){
				return true;
			}
			XtGateQueue gate = pasDao.getGateId(spgate, spisuncm);
			if(gate == null){
				EmpExecutionContext.error("账户通道配置，获取通道id失败。");
				return false;
			}
			
			AcmdPort cmdPort = null;
			
			for(int i =0; i<cmdList.size();i++){
				//把cmdid拿出来，用于后面删除
				strCmdId += cmdList.get(i).getId();
				if(i < cmdList.size() - 1){
					strCmdId += ",";
				}
				
				cmdPort = new AcmdPort();
				cmdPort.setGateId(gate.getId());
				cmdPort.setCmdId(cmdList.get(i).getId());
				//绑定状态：0 启动 1 禁用
				cmdPort.setStatus(0);
				if(cpno != null){
					cmdPort.setCpno(cpno);
				}
				//通道指令匹配失败后，对上行的处理：0 进行子号路由;1 按默认指令处理;2 中断，写库
				cmdPort.setFailOpt(0);
				portList.add(cmdPort);	
			}
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "账户通道配置，处理通道指令异常。");
			return false;
		}
		
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			
			Boolean delRes = pasDao.delPortCmd(conn, strCmdId);
			if(delRes == null){
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error("账户通道配置，删除通道指令失败。");
				return false;
			}
			empTransDao.save(conn, portList, AcmdPort.class);
			empTransDao.commitTransaction(conn);
			return true;
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "账户通道配置，删除并插入通道指令异常。");
			return false;
		}finally{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 更新通道指令
	 * @param spUser 发送账号
	 * @return 成功返回true
	 */
	public boolean updateCmd(String spUser){
		
		if(spUser == null){
			EmpExecutionContext.error("账户通道配置，更新指令失败，参数不正确。spUser="+spUser);
			return false;
		}
		
		PasRouteDao pasDao = new PasRouteDao();
		String strCmdId = "";
		List<AcmdPort> portList = new ArrayList<AcmdPort>();
		
		try
		{
			//获取账号所绑定的指令
			List<AcmdRoute> cmdList = pasDao.getSpUserCmd(spUser);
			if(cmdList == null){
				EmpExecutionContext.error("账户通道配置，更新指令，获取指令集合失败。");
				return false;
			}
			//无绑定指令，则不用更新
			if(cmdList.size() == 0){
				return true;
			}
			
			List<AcmdPort> newPortList = pasDao.getPortFromSpuser(spUser);
			
			List<AcmdPort> portListTemp = null;
			for(int i =0; i<cmdList.size();i++){
				//把cmdid拿出来，用于后面删除
				strCmdId += cmdList.get(i).getId();
				if(i < cmdList.size() - 1){
					strCmdId += ",";
				}
				
				portListTemp = setPortCmd(newPortList, cmdList.get(i).getId());
				if(portListTemp == null){
					EmpExecutionContext.error("账户通道配置，处理指令，获取通道指令集合失败。");
					return false;
				}
				
				portList.addAll(portListTemp);	
			}
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "账户通道配置，处理通道指令异常。");
			return false;
		}
		
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			
			Boolean delRes = pasDao.delPortCmd(conn, strCmdId);
			if(delRes == null){
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error("账户通道配置，删除通道指令失败。");
				return false;
			}
			empTransDao.save(conn, portList, AcmdPort.class);
			empTransDao.commitTransaction(conn);
			return true;
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "账户通道配置，删除并插入通道指令异常。");
			return false;
		}finally{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 批量更新指令
	 * @param gtPortList
	 * @return 成功返回true
	 */
	public boolean updateCmd(List<GtPortUsed> gtPortList){
		if(gtPortList == null || gtPortList.size() == 0){
			EmpExecutionContext.error("账户通道配置，更新指令，账户通道集合为空。");
			return false;
		}
		try
		{
			Map<String,String> spUserMap = new HashMap<String,String>();
			for(GtPortUsed gtport : gtPortList){
				if(gtport.getGateType() != 1){
					continue;
				}
				spUserMap.put(gtport.getUserId(), gtport.getUserId());
			}
			for(String userid : spUserMap.keySet()){
				updateCmd(userid);
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "账户通道配置，批量更新指令异常。");
			return false;
		}
	}
	
	/**
	 * 设置通道指令
	 * @param portList
	 * @param cmdId
	 * @return
	 */
	private List<AcmdPort> setPortCmd(List<AcmdPort> portList, Long cmdId){
		try
		{
			List<AcmdPort> newPortList = new ArrayList<AcmdPort>();
			AcmdPort newPortCmd = null;
			for(AcmdPort portCmd : portList){
				newPortCmd = new AcmdPort();
				newPortCmd.setGateId(portCmd.getGateId());
				newPortCmd.setCmdId(cmdId);
				//绑定状态：0 启动 1 禁用
				newPortCmd.setStatus(0);
				newPortCmd.setCpno(portCmd.getCpno());
				
				//通道指令匹配失败后，对上行的处理：0 进行子号路由;1 按默认指令处理;2 中断，写库
				newPortCmd.setFailOpt(0);
				//cmdPort.setDefCmdId(0);
				
				newPortList.add(newPortCmd);
			}
			
			return newPortList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置通道指令异常。");
			return null;
		}
	}

}
