package com.montnets.emp.monmanage.biz;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.monitor.LfMonSspacinfo;
import com.montnets.emp.entity.monitor.LfSpofflineprd;
import com.montnets.emp.monmanage.biz.i.ISpAcctMonCfgBiz;
import com.montnets.emp.monmanage.dao.SpAcctMonCfgDAO;
import com.montnets.emp.util.PageInfo;

/**
 * sp账号监控管理biz
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-03 下午04:57:00
 */
public class SpAcctMonCfgBiz extends SuperBiz implements ISpAcctMonCfgBiz
{

	/**
	 * 获取sp账号监控管理
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin <ljr0300@163.com>
	 * @datetime 2013-12-03 下午04:58:00
	 */
	public List<DynaBean> getspAcctMonCfg(PageInfo pageInfo, LinkedHashMap< String, String> conditionMap)
	{
		
		return new SpAcctMonCfgDAO().getspAcctMonCfg(pageInfo, conditionMap);
	}
	
	/**
	 * 设置sp账号阀值信息
	 * @description    
	 * @param objectMap 修改map
	 * @param spaccountid sp账号
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 下午03:08:36
	 */
	public boolean editSpAcct(LinkedHashMap<String, String> objectMap,String spaccountid)
	{
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("spaccountid", spaccountid);
		try {
			empDao.update(LfMonSspacinfo.class, objectMap, map);
			return true;
		}
		catch (Exception e) {
			EmpExecutionContext.error(e, "设置sp账号阀值信息biz层异常！");
			return false;
		}
	}
	
	/**
	 * 设置sp账号阀值信息，新方法
	 * @description    
	 * @param objectMap 修改map
	 * @param spaccountid sp账号
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 下午03:08:36
	 */
	public boolean editSpAcctNew(LinkedHashMap<String, String> objectMap,String spaccountid,List<LfSpofflineprd> lfSpofflineprds,String corpCode)
	{
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("spaccountid", spaccountid);
		Connection conn =null;
		try {
				//获取连接
				conn = empTransDao.getConnection();
				//开始事务
				empTransDao.beginTransaction(conn);
				
				empTransDao.update(conn,LfMonSspacinfo.class, objectMap, map);
				//长时间阀值未设置
				if(lfSpofflineprds==null||lfSpofflineprds.size()==0)
				{
					//先删除SP离线时间段告警阀值
					LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
					condition.put("corpcode",corpCode);
					condition.put("spaccountid", spaccountid);
					empTransDao.delete(conn, LfSpofflineprd.class, condition);
				}else{
					//长时间阀值已设置
					//先删除SP离线时间段告警阀值
					LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
					condition.put("corpcode",corpCode);
					condition.put("spaccountid", spaccountid);
					empTransDao.delete(conn, LfSpofflineprd.class, condition);
					//再添加SP离线时间段告警阀值
					empTransDao.save(conn, lfSpofflineprds, LfSpofflineprd.class);
				}
				//提交事务
				empTransDao.commitTransaction(conn);
				return true;
			}catch (Exception e) {
				//回滚事务
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error(e, "设置sp账号阀值信息biz层异常！");
				return false;
			}finally
			{
				//关闭连接
				empTransDao.closeConnection(conn);
			}
	}
}
