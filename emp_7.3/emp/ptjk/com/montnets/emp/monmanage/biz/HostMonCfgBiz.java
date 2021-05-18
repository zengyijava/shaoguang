package com.montnets.emp.monmanage.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.monitor.LfMonErr;
import com.montnets.emp.entity.monitor.LfMonShost;
import com.montnets.emp.entity.monitor.LfMonSproce;
import com.montnets.emp.monitor.constant.MonDhostParams;
import com.montnets.emp.monmanage.biz.i.IHostMonCfgBiz;
import com.montnets.emp.monmanage.dao.HostMonCfgDAO;
import com.montnets.emp.monmanage.dao.PrcMonCfgDAO;
import com.montnets.emp.util.PageInfo;

/**
 * 主机监控管理biz
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-26 下午04:57:00
 */
public class HostMonCfgBiz extends SuperBiz implements IHostMonCfgBiz
{

	/**
	 * 获取主机信息
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin <ljr0300@163.com>
	 * @datetime 2013-11-27 上午09:48:17
	 */
	public List<DynaBean> getHost(PageInfo pageInfo, LinkedHashMap< String, String> conditionMap)
	{
		
		return new HostMonCfgDAO().getHost(pageInfo, conditionMap);
	}
	
	/**
	 * 获取主机动态信息
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin <ljr0300@163.com>
	 * @datetime 2013-11-27 上午09:48:17
	 */
	public List<DynaBean> getDhost(LinkedHashMap< String, String> conditionMap)
	{
		
		return new HostMonCfgDAO().getDhost(conditionMap);
	}
	
	/**
	 * 添加主机信息
	 * @description    
	 * @param shost 主机基本信息
	 * @param dhost 主机动态信息
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 下午03:08:36
	 */
	public boolean addHost(LfMonShost shost,String proceid)
	{
		//获取数据库连接
		Connection conn = empTransDao.getConnection();
		try {
			empTransDao.beginTransaction(conn);
			Long hostid = empTransDao.saveObjectReturnID(conn, shost);
			if(hostid==null)
			{
				empTransDao.rollBackTransaction(conn);
				return false;
			}

			if(proceid!=null&&!"".equals(proceid.trim())){
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> objiectMap = new LinkedHashMap<String, String>();
				conditionMap.put("proceid", proceid);
				objiectMap.put("hostid", hostid.toString());
				empTransDao.update(conn,LfMonSproce.class, objiectMap,conditionMap);
			}
			empTransDao.commitTransaction(conn);
			return true;
		}
		catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "新建主机信息biz层异常！");
			return false;
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 逻辑删除主机
	 * @description    
	 * @param hostid
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2014-1-17 下午05:05:11
	 */
	public boolean delete(Long hostid)
	{
		if(hostid==null||hostid==0){
			return false;
		}
		//获取数据库连接
		Connection conn = empTransDao.getConnection();
		LinkedHashMap<String, String> object = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> cond = new LinkedHashMap<String, String>();
		try {
			empTransDao.beginTransaction(conn);
			cond.put("hostid", String.valueOf(hostid));
			object.put("hostusestatus", "1");
			empTransDao.update(conn, LfMonShost.class,object,cond);
			empTransDao.delete(conn,LfMonErr.class, cond);
			object.clear();
			object.put("proceusestatus", "1");
			empTransDao.update(conn, LfMonSproce.class,object,cond);
			new PrcMonCfgDAO().deleteErr(conn, String.valueOf(hostid));
			empTransDao.commitTransaction(conn);
			return true;
		}
		catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "删除主机信息biz层异常！");
			return false;
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 把主机信息复制到主机动态信息类中
	 */
	public MonDhostParams copyBy(LfMonShost shost){
		MonDhostParams dhost = new MonDhostParams();
		dhost.setAdapter1(shost.getAdapter1());
		dhost.setHostid(shost.getHostid());
		dhost.setHostName(shost.getHostname());
		dhost.setHoststatus(0);
		dhost.setUpdatetime(new Timestamp(System.currentTimeMillis()));
		dhost.setEvtType(0);
		return dhost;
	}
	
	/**
	 * 更新主机名称和内网ip
	 */
	public void updateBy(MonDhostParams dhost,LfMonShost shost){
		if(dhost==null||shost==null){
			return;
		}
		dhost.setAdapter1(shost.getAdapter1());
		dhost.setHostName(shost.getHostname());
		//dhost.setUpdatetime(new Timestamp(System.currentTimeMillis() - (MonitorStaticValue.networkRrror * 1000)));
	}
}
