/**
 * @description 在线客服状态Biz
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-16 上午11:09:27
 */
package com.montnets.emp.znly.biz;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;
import org.json.simple.JSONObject;

import java.util.*;
//import com.montnets.emp.zxkf.dao.CustomerDao;

/**
 * @description 在线客服状态修改
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-16 上午11:09:27
 */

public class CustomStatusBiz extends SuperBiz
{

    // 所有客服人员状态map
    private static final Map<String,Integer> customStatusMap = new HashMap<String, Integer>();

    // 需要更改状态的客服map指令
    private static final JSONObject changeStatusMap = new JSONObject();

    // 在线客服人员更新在线时间集合，用户监控轮询
    private static final JSONObject  onlineUserTime  = new JSONObject();

    public static Map<String, Integer> getCustomStatusMap() {
        return customStatusMap;
    }

    public static JSONObject getOnlineUserTime() {
        return onlineUserTime;
    }

    private final int loginTimeoutTime = 150*1000;
    private final int timeoutTime = 20*1000;
    /**
     * 加载所有客服人员状态
     * 
     * @description 所有客服人员状态查询
     * @param corpcode
     *        集团编码
     * @param aid
     *        公众账号ID
     * @param pageInfo
     *        分页对象
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-16 下午05:16:04
     */
    public void loadAllcustomStatus()
    {
        // 查询所有客服人员
        List<LfSysuser> userList = null;
		try {
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("isCustome", "1");
			userList = empDao.findListByCondition(LfSysuser.class, conditionMap, null);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "加载所有客服人员状态异常。");
		}
        if(null == userList || userList.size() == 0)
        {
            return;
        }
        for (int i = 0; i < userList.size(); i++)
        {
        	LfSysuser user = userList.get(i);
            String userId = String.valueOf(user.getUserId());

            customStatusMap.put(userId, 4);
        }
    }

    /**
     * 监控客服人员状态（后台定时器调用方法）
     * 
     * @description 实时监控状态的客服人员，判断客服人员时候在线
     * @param corpcode
     *        集团编码
     * @param aid
     *        公众账号ID
     * @param pageInfo
     *        分页对象
     * @return JSONObject 有改变过状态的客服json结果集
     * @author fanglu <fanglu@montnets.com>
     * @throws Exception 
     * @datetime 2013-12-17 下午05:49:18
     */
    @SuppressWarnings("unchecked")
    public void monitorCustomStatusForBackstage() throws Exception
    {
        long nowTime = System.currentTimeMillis();
        // 判断在规定时间范围类，客服时候有响应操作，超时则清除内存数据，并设置客服为离线状态
        if(null != onlineUserTime && onlineUserTime.size() >  0)
        {
            JSONObject newOnlineUserTime = new JSONObject();
            newOnlineUserTime.putAll(onlineUserTime);
            Iterator<String> useridIterator = newOnlineUserTime.keySet().iterator();
            
            while(useridIterator.hasNext())
            {
                String userid = (String) useridIterator.next();
                JSONObject timeJson = (JSONObject) newOnlineUserTime.get(userid);
                long time = (Long) timeJson.get("time");
                String aid = (String) timeJson.get("aid");
                if(nowTime - time - loginTimeoutTime > 0)
                {
                    //超时则移除在线客服人员更新在线时间集合中数据
                    synchronized (onlineUserTime)
                    {
                        onlineUserTime.remove(userid);
                    }
                    
                    changeCustomStatus(userid,4);
                    CustomChatBiz.removeServerCount(userid, aid);
                }                
            } 
        }
        
        // 判断是否为空，为空则跳出 
//        if(changeStatusMap == null)
//        {
//            return;
//        }
        JSONObject newChangeMap = new JSONObject();
        newChangeMap.putAll(changeStatusMap);
        Iterator<String> newIter = newChangeMap.keySet().iterator();
        while(newIter.hasNext())
        {
            String userid = newIter.next();

            JSONObject stateJson = (JSONObject)newChangeMap.get(userid);
            long time = (Long) stateJson.get("time");
            if(nowTime - time - timeoutTime > 0)
            {
                synchronized (customStatusMap)
                {
                    changeStatusMap.remove(userid);
                }
            }
        
        }
    }
    
    /**
     * 查询某个客服人员状态
     * 
     * @description 客服人员状态展示
     * @param customId
     *        客服唯一标识ID
     * @return 客服状态（1：在线，2：忙碌，3：离开，4：离线）
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-16 下午04:22:16
     */
    public int findCustomStatusById(String customId)
    {
        int status = Integer.parseInt(customStatusMap.get(customId).toString()); 
        return status;
    }

    /**
     * 修改客服状态
     * 
     * @description
     * @param customId
     *        客服唯一标识ID
     * @param status
     *        客服状态（1：在线，2：忙碌，3：离开，4：离线）
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-16 上午11:15:51
     */
    @SuppressWarnings("unchecked")
    public static void changeCustomStatus(String customId, int status)
    {

    	customStatusMap.put(customId, status);
        if(status == 4)
        {
            synchronized (customStatusMap)
            {
                onlineUserTime.remove(customId);
            }
        }
        synchronized (changeStatusMap)
        {
            JSONObject changeStatuJsonObject = (JSONObject) changeStatusMap.get(customId);
            if(null == changeStatuJsonObject)
            {
                changeStatuJsonObject = new JSONObject();
            }

            changeStatuJsonObject.put("status", status);
            changeStatuJsonObject.put("time", System.currentTimeMillis());

            changeStatusMap.put(customId, changeStatuJsonObject);
        }

    }
    
    /**
     * 更新在线状态
     * @description 客服在线时则不断向后台发送请求保持激活在线    
     * @param customeId 客服id
     * @param aId 客服所属公众号ID      			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-27 上午09:46:30
     */
    public static void setUserOnlineTiem(String customeId,String aId)
    {
        JSONObject timeJson = new JSONObject();
        timeJson.put("aid",aId);
        timeJson.put("time",System.currentTimeMillis());
        
        synchronized (onlineUserTime)
        {
            onlineUserTime.put(customeId, timeJson);
        }
        // 如果在线状态为离线，则激活为在线
        if(customStatusMap.get(customeId) == 4)
        {
            changeCustomStatus(customeId, 1);
            // 加载未读消息
            new CustomChatBiz().getUnReadMsg(customeId);
        }
    }
    
    public static JSONObject getChangeStatusMap()
    {
    	return changeStatusMap;
    }
}
