package com.montnets.emp.monmanage.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.gateway.AgwParamConf;
import com.montnets.emp.entity.gateway.AgwParamValue;
import com.montnets.emp.entity.monitor.LfMonErr;
import com.montnets.emp.entity.monitor.LfMonShost;
import com.montnets.emp.entity.monitor.LfMonSproce;
import com.montnets.emp.entity.monitor.MMonDproce;
import com.montnets.emp.entity.monitor.MMonSproce;
import com.montnets.emp.monitor.constant.MonDproceParams;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.monmanage.biz.i.IPrcMonCfgBiz;
import com.montnets.emp.monmanage.dao.PrcMonCfgDAO;
import com.montnets.emp.util.PageInfo;

/**
 * 程序监控管理biz
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-26 下午04:57:00
 */
public class PrcMonCfgBiz extends SuperBiz implements IPrcMonCfgBiz
{

	/**
	 * 获取程序信息
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin 
	 * @datetime 2013-11-27 上午09:48:17
	 */
	public List<DynaBean> getSprc(PageInfo pageInfo, LinkedHashMap< String, String> conditionMap)
	{
		
		return new PrcMonCfgDAO().getSprc(pageInfo, conditionMap);
	}
	
	/**
	 * 获取EMP相关信息
	 * @description    
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午08:38:39
	 */
	public List<DynaBean> getgw(){
		return new PrcMonCfgDAO().getgw();
	}
	
	/**
	 * 获取SPGate信息
	 * @description    
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午08:38:39
	 */
	public List<DynaBean> getSPGate()
	{
		return new PrcMonCfgDAO().getSPGate();
	}
	
	
	/**
	 * 获取主机信息
	 * @description    
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 上午09:48:17
	 */
	public List<DynaBean> getHost()
	{
		return new PrcMonCfgDAO().getHost();
	}
	
	/**
	 * 添加程序信息
	 * @description    
	 * @param sprc 程序基本信息
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 下午03:08:36
	 */
	public boolean addPrc(MMonSproce sprc)
	{
		//获取数据库连接
		Connection conn = empTransDao.getConnection();
		try {
			empTransDao.beginTransaction(conn);
			Long proceid = empTransDao.saveObjectReturnID(conn, sprc);
			
			//程序动态信息
			MMonDproce dprc = new MMonDproce();
			dprc.setProceid(Integer.parseInt(proceid.toString()));
			dprc.setCpuusage(0);
			dprc.setMemusage(0);
			dprc.setVmemusage(0);
			dprc.setDiskfree(0);
			dprc.setDownflow(0L);
			dprc.setUpflow(0L);
			dprc.setUpdatetime(new Timestamp(System.currentTimeMillis()));
			empTransDao.save(conn, dprc);
			empTransDao.commitTransaction(conn);
			return true;
		}
		catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "新建程序信息biz层异常！");
			return false;
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 修改程序信息
	 * @description    
	 * @param sprc 程序信息
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 下午03:08:36
	 */
	public boolean editPrc(LfMonSproce sprc,String mqurl)
	{
		//获取数据库连接
		Connection conn = empTransDao.getConnection();
		try {
			empTransDao.beginTransaction(conn);
			empTransDao.update(conn, sprc);
			LinkedHashMap< String, String> conMap = new LinkedHashMap<String, String>();
			conMap.put("paramItem", "MONITORMQURL");
			conMap.put("gwType", "4000,3000");
			LinkedHashMap<String, String> objMap = new LinkedHashMap<String, String>();
			objMap.put("defaultValue", mqurl);
			//修改mq信息
			empTransDao.update(conn, AgwParamConf.class, objMap, conMap);
			objMap.clear();
			objMap.put("paramValue", mqurl);
			//修改mq信息
			empTransDao.update(conn, AgwParamValue.class, objMap, conMap);
			objMap.put("defaultvalue", mqurl);
			empTransDao.commitTransaction(conn);
			return true;
		}
		catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "修改主机信息biz层异常！");
			return false;
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 获取mq的配置信息
	 * @description    
	 * @return       			 
	 * @author zhangmin 
	 * @datetime 2013-11-27 上午09:48:17
	 */
	public List<DynaBean> getMqMsg()
	{
		
		return new PrcMonCfgDAO().getMqMsg();
	}
	
	/**
	 * 逻辑删除程序
	 * @description    
	 * @param hostid
	 * @param procid
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-1-17 下午05:53:24
	 */
	public boolean delete(Long proceid)
	{
		if(proceid==null||proceid == 0){
			return false;
		}
		boolean isDelHost = false;
		long hostid = 0L;
		LinkedHashMap<String, String> cond = new LinkedHashMap<String, String>();
		//是否需要删除主机判断
		try
		{
			LfMonSproce sproce = empDao.findObjectByID(LfMonSproce.class, proceid);
//			if(proceid == null){
//				return false;
//			}
			hostid = sproce.getHostid();
			if(hostid!=-1){
				cond.put("hostid", String.valueOf(hostid));
				cond.put("proceusestatus", "0");
				List<LfMonSproce> sproces = empDao.findListByCondition(LfMonSproce.class,cond , null);
				//当主机Id不为-1且 主机下的程序不超过1时 则需要删除主机
				if(sproces.size()<=1){
					isDelHost = true;
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "是否需要删除主机判断异常！");
			return false;
		}
		//获取数据库连接
		Connection conn = empTransDao.getConnection();
		LinkedHashMap<String, String> object = new LinkedHashMap<String, String>();
		try {
			empTransDao.beginTransaction(conn);
			//逻辑删除程序
			cond.clear();
			cond.put("proceid", String.valueOf(proceid));
			object.put("proceusestatus", "1");
			empTransDao.update(conn, LfMonSproce.class,object,cond);
			empTransDao.delete(conn,LfMonErr.class, cond);
			if(isDelHost){//逻辑删除主机
				cond.clear();
				object.clear();
				cond.put("hostid", String.valueOf(hostid));
				object.put("hostusestatus", "1");
				empTransDao.update(conn, LfMonShost.class,object,cond);
				empTransDao.delete(conn,LfMonErr.class, cond);
			}
			empTransDao.commitTransaction(conn);
			return true;
		}
		catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "删除程序信息biz层异常！");
			return false;
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
	}
	
	public MonDproceParams copyBy(LfMonSproce monSproce){
		Long hostid = monSproce.getHostid();
		String hostName = "-";
		Map<Long, LfMonShost> hostBaseMap = MonitorStaticValue.getHostBaseMap();
		if(hostBaseMap == null || hostBaseMap.size() < 1)
		{
			hostBaseMap = MonitorStaticValue.getHostBaseMapTemp();
		}
		if(hostid!=null&&hostid!=-1&&hostBaseMap.get(hostid)!=null){
			hostName = hostBaseMap.get(hostid).getHostname();
		}
		MonDproceParams monDproceParams = new MonDproceParams();
		monDproceParams.setProceid(monSproce.getProceid());
		monDproceParams.setUpdatetime(new Timestamp(System.currentTimeMillis()));
		monDproceParams.setGatewayId(monSproce.getGatewayid());
		monDproceParams.setHostname(hostName);
		monDproceParams.setProcename(monSproce.getProcename());
		monDproceParams.setHostid(monSproce.getHostid());
		monDproceParams.setEvttype(0);
		monDproceParams.setVersion(monSproce.getVersion());
		monDproceParams.setProcestatus(0);
		monDproceParams.setProcetype(monSproce.getProcetype());
		monDproceParams.setDbconnectstate(0);
		Map<Integer, Long> monThresholdFlag = new HashMap<Integer, Long>();
		monThresholdFlag.put(1, 0L);
		monThresholdFlag.put(2, 0L);
		monThresholdFlag.put(3, 0L);
		monThresholdFlag.put(4, 0L);
		monThresholdFlag.put(5, 0L);
		monThresholdFlag.put(6, 0L);
		monDproceParams.setMonThresholdFlag(monThresholdFlag);
		return monDproceParams;
	}
	
	public void updateBy(MonDproceParams monDproceParams ,LfMonSproce monSproce){
		if(monDproceParams==null||monSproce==null){
			return;
		}
		Long hostid = monSproce.getHostid();
		String hostName = "-";
		Map<Long, LfMonShost> hostBaseMap = MonitorStaticValue.getHostBaseMap();
		if(hostBaseMap == null || hostBaseMap.size() < 1)
		{
			hostBaseMap = MonitorStaticValue.getHostBaseMapTemp();
		}
		if(hostid!=null&&hostid!=-1&&hostBaseMap.get(hostid)!=null){
			hostName = hostBaseMap.get(hostid).getHostname();
		}
		monDproceParams.setHostname(hostName);
		monDproceParams.setProcename(monSproce.getProcename());
		monDproceParams.setHostid(monSproce.getHostid());
		monDproceParams.setVersion(monSproce.getVersion());
		//monDproceParams.setUpdatetime(new Timestamp(System.currentTimeMillis() - (MonitorStaticValue.networkRrror * 1000)));
	}
}
