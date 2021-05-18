package com.montnets.emp.wymanage.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wy.AGwroute;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wymanage.dao.RouteManageDAO;
/**
 * 网优路由绑定biz
 * @description 
 * @project p_wygl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2014-3-27 上午09:32:09
 */
public class RouteManageBiz extends SuperBiz
{
	
	/**
	 * 网优绑定查询
	 * @description    
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-3-27 上午09:32:03
	 */
	public List<DynaBean> getRoute(PageInfo pageInfo,LinkedHashMap<String, String> conditionMap)
	{
		return new RouteManageDAO().getRoute( pageInfo,conditionMap);
	}
	
	/**
	 * 网优绑定查询（去除绑定的）
	 * @description    
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-3-27 上午09:32:03
	 */
	public List<DynaBean> getRouteNotBind(LinkedHashMap<String, String> conditionMap)
	{
		return new RouteManageDAO().getRouteNotBind(conditionMap);
	}

	/**
	 * 网优路由绑定
	 * @description    
	 * @param phoneList
	 * @return       			 
	 * @author zhangmin
	 * @throws Exception 
	 * @datetime 2014-3-25 下午04:45:19
	 */
	public void bind(String id,String type) throws Exception
	{
		//获取数据库连接
		Connection conn = empTransDao.getConnection();
		try {
			empTransDao.beginTransaction(conn);
			LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
			//conMap.put("gateid&in", id);
			conMap.put("type", type);
			empTransDao.delete(conn, AGwroute.class,conMap);
			//empDao.delete(AGwroute.class, conMap);
			//id为空表示取消所有绑定
			if(!"".equals(id))
			{
				
				String [] ids=id.split(",");
				List<AGwroute> routeList = new ArrayList<AGwroute>();
				for(int i=0;i<ids.length;i++)
				{
					AGwroute route = new AGwroute();
					route.setArea(0);
					route.setCreatetime(new Timestamp(System.currentTimeMillis()));
					route.setGateid(Integer.parseInt(ids[i]));
					route.setGateseq(0);
					route.setP1(" ");
					route.setP2(" ");
					route.setSendtimebegin("00:00:00");
					route.setSendtimeend("23:59:59");
					route.setSimid(0);
					route.setStatus(0);
					route.setType(Integer.parseInt(type));
					route.setUnicom(0);
					routeList.add(route);
				}
				//empDao.save( routeList,AGwroute.class);
				empTransDao.save(conn, routeList,AGwroute.class);
			}
			empTransDao.commitTransaction(conn);
		}
		catch (Exception e) {
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "网优路由绑定biz层异常！");
			throw e;
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
	}
}
