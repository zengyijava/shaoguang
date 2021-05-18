package com.montnets.emp.common.atom;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.pasgroup.Userdata;

/**
 * 发送账户相关原子类
 * @author Administrator
 *
 */
public class UserdataAtom extends SuperBiz{

	//发送账户对应的密码Map：key-sp账户，value-账户密码
	private static Map<String, String> userdataMap = new HashMap<String, String>();
	//发送账户对应的更新时间Map: key-sp账户,value-更新时间点毫秒数
	private static Map<String, Long> updateTimeMap = new HashMap<String, Long>();
	
	/**
	 * 获取sp账户对应的密码
	 * @param spUserid
	 * @return
	 */
	public String getUserPassWord(String spUserid)
	{
		return userdataMap.get(spUserid);
	}
	
	/**
	 * 将发送账户集合放置到内存map中
	 * @param userdataList
	 */
	public void setUserdata(List<Userdata> userdataList)
	{
		if(userdataList != null)
		{
			//遍历集合
			for(Userdata userdata : userdataList)
			{
				//设置账户密码Map
				userdataMap.put(userdata.getUserId(), userdata.getUserPassword());
				//设置账户更新时间Map
				updateTimeMap.put(userdata.getUserId(), System.currentTimeMillis());
			}
		}
	}
	
	
	/**
	 * 设置所有SP账号
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-16 下午03:05:08
	 */
	public void setAllUserdata()
	{
		try
		{
			// 查询所有SP账号
			List<Userdata> spUserList = null;
			//条件map
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
			conditionMap.put("accouttype", "1");
			spUserList = empDao.findListBySymbolsCondition(Userdata.class, conditionMap, null);
			int count = 0;
			if(spUserList != null && spUserList.size() > 0)
			{
				count = spUserList.size();
				setUserdata(spUserList);
			}
			EmpExecutionContext.info("定时加载发送账号成功!共加载:" + count);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置所有SP账号失败!");
		}
	}
	
	/**
	 * 判断内存中的值是否超过时间间隔，是则从内存map中去除
	 */
	public void cleanUserdataMap()
	{
		/*long currentTime = System.currentTimeMillis();
		Iterator<String> iter = userdataMap.keySet().iterator();
		synchronized(userdataMap)
		{
			synchronized(updateTimeMap)
			{
				while(iter.hasNext())
				{
					//获取map中dkey
					String key = iter.next();
					long updateTime = updateTimeMap.get(key);
					if(currentTime - updateTime > StaticValue.SYNC_TIMER_INTERVAL)
					{
						//移除Map中的值
						userdataMap.remove(key);
						updateTimeMap.remove(key);
					}
				}
			}
		}*/
	}
}
