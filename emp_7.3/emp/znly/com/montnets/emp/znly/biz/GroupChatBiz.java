package com.montnets.emp.znly.biz;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.online.LfOnlGpmem;
import com.montnets.emp.entity.online.LfOnlGpmsgid;
import com.montnets.emp.entity.online.LfOnlGroup;
import com.montnets.emp.entity.online.LfOnlMsgHis;
import com.montnets.emp.ottbase.dao.OttGenericTransactionDAO;
import com.montnets.emp.ottbase.util.StringUtils;
import com.montnets.emp.znly.dao.CustomChatDao;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections4.CollectionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;

public class GroupChatBiz extends SuperBiz
{
    // 每个客服读取的最大消息Id集合
    private static final Map<String,Long> userReadedIdMap = new HashMap<String, Long>();
    
    private static final Map<String,JSONArray> groupMsgMap = new HashMap<String, JSONArray>();
    // 最后一条消息的Id
    private static Long maxGroupMsgId = 0l;
    
    private static final Map<String,Boolean> isUpdateDbMap = new HashMap<String, Boolean>();
    // 每个群组存在的最大消息Id集合
    //private static Map<String,Long> groupMaxIdMap =  new HashMap<String, Long>();
    
    static IEmpDAO empDao =new DataAccessDriver().getEmpDAO();
    
    static
    {
        try
        {
            maxGroupMsgId = new CustomChatDao().getMaxMsgId();
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "获取聊天消息最大ID值失败！");
        }
    }
    
    /**
     * 发送群组消息
     * @description 发送群组消息时，设置群组消息读取的map    
     * @param groupKey
     * @param toCustomId
     * @param fromUser
     * @param msgTpe
     * @param msg
     * @param pushType
     * @param name
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-2 下午02:30:15
     */
    @SuppressWarnings("unchecked")
    public static String setGroupMsgJson(String groupKey,String fromUser,String msgTpe,String msg,String name)
    {
        Timestamp sendTime = new Timestamp(System.currentTimeMillis());
        String time = StringUtils.timeFormat(sendTime);
        // 群组消息在发送时入库
        Long newId  = insertChatMsgHis(0l, fromUser, groupKey, "group"+groupKey, msg, sendTime, msgTpe, 4);
        if(newId.intValue() == 0)
        {
            return "error";
        }
        String servernum = "group" + groupKey;
        JSONObject jsonOb = new JSONObject();
        jsonOb.put("fromuser", fromUser); 
        jsonOb.put("message", msg); 
        jsonOb.put("time", time); 
        jsonOb.put("msgtype", msgTpe); 
        jsonOb.put("servernum", servernum);
        //发送者名称，群组发送时为群组名称，客服对客服发送时为客服人员名称，手机对客服时为手机用户昵称
        jsonOb.put("name", name);
        jsonOb.put("pushtype", "4");
        jsonOb.put("msgid", newId);
        jsonOb.put("dotype", 1);
        
        // 更新最后一条消息的Id
        maxGroupMsgId = newId;
       
        
        JSONArray jsonArr = new JSONArray();
        synchronized(groupMsgMap)
        {
            jsonArr = groupMsgMap.get(groupKey);
            if(jsonArr == null)
            {
                jsonArr = new JSONArray();
               
            }
            jsonArr.add(jsonOb);
            groupMsgMap.put(groupKey, jsonArr);
        }
        synchronized(userReadedIdMap)
        {
            // 更新内存中的消息id
            userReadedIdMap.put(fromUser, newId);
        }
        synchronized(isUpdateDbMap)
        {
            // 更新内存中的消息id
            isUpdateDbMap.put(fromUser,updateGroupMsgId(newId, fromUser));
        }
        
        return time;
    }
    
    /**
     * 更新数据库最大的消息Id
     * @description    
     * @param newId
     * @param fromUser       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-17 下午04:04:24
     */
    public static void updateGpMsgId(Long newId,String fromUser)
    {
        Long userMsgId = userReadedIdMap.get(fromUser);
        if(userMsgId==null || newId - userMsgId > 0)
        {
            userMsgId = newId;
            userReadedIdMap.put(fromUser,newId);
        }
        Boolean isUpdateDb = isUpdateDbMap.get(fromUser);
        if(userMsgId <= maxGroupMsgId && (isUpdateDb == null || !isUpdateDb.booleanValue()))
        {
            isUpdateDbMap.put(fromUser,updateGroupMsgId(userMsgId, fromUser));
        }
    }
    /**
     * 获取群组消息记录
     * @description
     * @param returnJsonArr 消息集合，包含已读取的即时消息  
     * @param groupKeys 所属群组id集合
     * @param customId
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-17 下午04:07:39
     */
    public JSONArray  getGroupMsg(String groupKeys,String customId)
    {
        JSONArray returnJsonArr = new JSONArray();
        if(groupKeys == null || "".equals(groupKeys))
        {
            return returnJsonArr;
        }
        
        Long msgid = 0l;
        // 获取已读的最大Id
        msgid = userReadedIdMap.get(customId);
        
        if(msgid==null)
        {
            msgid=0l;
        }
        // 将要读取的最大消息ID
        Long maxid = maxGroupMsgId;

        String[] groupKeyArray = groupKeys.split(",");
        Set<String> gpSet =  new HashSet<String>(Arrays.asList(groupKeyArray)); 
        groupKeyArray = gpSet.toArray(new String[gpSet.size()]);
        
        String groupKey = "";
        JSONArray jsonArr = new JSONArray();
        
        if(msgid < maxid)
        {
            int j;
            for(int i=0;i<groupKeyArray.length;i++)
            {
                groupKey = groupKeyArray[i];
                // 取出当前群组最大的Id
                synchronized(groupMsgMap)
                {
                    jsonArr = groupMsgMap.get(groupKey);
                    if(jsonArr != null )
                    {
                        for(j=0;j<jsonArr.size();j++)
                        {
                            JSONObject json = (JSONObject)jsonArr.get(j);
                            Long groupMsgId = (Long)json.get("msgid");
                            if(groupMsgId - msgid > 0 && maxid - groupMsgId >= 0)
                            {
                                returnJsonArr.add(json);
                            }
                        }
                    }
                }
            }
            /*synchronized(userReadedIdMap){
                userReadedIdMap.put(customId, maxid);
            }*/
            userReadedIdMap.put(customId, maxid);
            isUpdateDbMap.put(customId, false);
        }
        /*if(returnJsonArr.size() > 0)
        {
            updateGroupMsgId(maxid,customId);
        }*/
       
        return returnJsonArr;
    }
    
    /**
     * 更新已读群组消息的最大Id
     * @description 轮询读取群组消息钱，会先更新已读消息的id    
     * @param msgId
     * @param customId       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-27 上午10:46:35
     */
    public static boolean updateGroupMsgId(Long msgId,String customId)
    {
        Timestamp updateTime = new Timestamp(System.currentTimeMillis());
        try
        {
            LinkedHashMap<String, String> conditionMap  = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> objectMap  = new LinkedHashMap<String, String>();
            
            conditionMap.put("gmUser", customId);
            objectMap.put("msgId", msgId.toString());
            objectMap.put("updateTime", StringUtils.timeFormat(updateTime));
            
            empDao.update(LfOnlGpmsgid.class, objectMap, conditionMap);
            return  true;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"更新客服已读最大消息id失败！");
            return false;
        }
    }
    
    /**
     * 插入聊天消息库
     * @description  客服聊天消息插入数据库  
     * @param AId 公众账号id
     * @param fromUser 发送者
     * @param toUser 接收者
     * @param serverNum 服务号
     * @param message 内容
     * @param sendTime 发送时间
     * @param msgType 消息类型text-文本，voice-声音，image-单图
     * @param pushType 推送类型1-手机to客服，2-客服to手机，3-客服to客服，4群组
     * @return true-成功,false-失败                  
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-19 下午02:55:29
     */
    public static Long insertChatMsgHis(Long AId,String fromUser,String toUser,
            String serverNum,String message,Timestamp sendTime,String msgType,int pushType)
    {
        try
        {
            LfOnlMsgHis onlMsgHis = new LfOnlMsgHis(AId, fromUser, toUser, 
                    serverNum, message, sendTime, msgType, pushType);
            return empDao.saveObjectReturnID(onlMsgHis);
        }
        catch (Exception e)
        {
           return 0L;
        }
    }
    
    /**
     * 创建群组和添加创建者到群组成员表
     * @description    
     * @param gpName 群组名
     * @param userId 创建者
     * @param aId 公众号id
     * @return                   
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2013-12-19 下午06:37:09
     */
    public LfOnlGroup createGroup(String gpName,String userId,String aId,String customId){
       //新建群组
       LfOnlGroup group = new LfOnlGroup();
       //获取事务连接
       Connection conn = empTransDao.getConnection();
       try{
           String[] userIds = userId.split(",");
           empTransDao.beginTransaction(conn);
           
           //群组赋值
           group.setGpName(gpName);
           group.setCreateUser(Long.valueOf(userIds[userIds.length-1]));
           group.setCreateTime(new Timestamp(System.currentTimeMillis()));
           group.setMemCount(1);
           long temp = empTransDao.saveObjProReturnID(conn, group);
           group.setGpId(temp);
           
           List<LfOnlGpmem> memberList = new ArrayList<LfOnlGpmem>();
           for(int i=0;i<userIds.length;i++)
           {
             //建议创建者与群组的成员关系
               LfOnlGpmem member = new LfOnlGpmem();
               member.setGpId(temp);
               member.setGmUser(Long.valueOf(userIds[i]));
               //成员关系有效
               member.setGmState(1);
               
               memberList.add(member);
           }
           
           empTransDao.save(conn, memberList,LfOnlGpmem.class);
           
           String msg = "邀请您加入了新的群组【"+gpName+"】";
           JSONObject json = new JSONObject();
           json.put("msg", msg);
           json.put("groupid", temp);
           json.put("count", userIds.length);
           msg = json.toString();
           Long aidL = 0L;
           try
           {
        	   aidL = Long.valueOf(aId);
           }finally{}
           for(int i=0;i<userIds.length-1;i++)
           {
        	   CustomChatBiz.setMsgJsonBefore(userIds[i], customId+"to"+userIds[i], customId, "newqz",msg, "3", gpName, aidL,0);
           }
           //提交事务
           empTransDao.commitTransaction(conn);
         
       }catch(Exception e){
           empTransDao.rollBackTransaction(conn);
           EmpExecutionContext.error(e, "new GroupChatBiz().createGroup Is Error");
           return null;
       }finally{
           empTransDao.closeConnection(conn);
       }
       return group;
    }
    
    /**
     * 删除群组及群组成员
     * @description    
     * @param gpId
     * @return       			 
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2013-12-19 下午06:37:09
     */
    public boolean delGroup(String gpId){
        boolean success = false;
        //获取事务连接
        Connection conn = empTransDao.getConnection();
        try{
            empTransDao.beginTransaction(conn);
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("gpId", gpId);
            //删除群组
            empTransDao.delete(conn, LfOnlGroup.class, conditionMap);
            empTransDao.delete(conn, LfOnlGpmem.class, conditionMap);
            //提交事务
            empTransDao.commitTransaction(conn);
            success = true;
        }catch(Exception e){
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "new GroupChatBiz().createGroup Is Error");
        }finally{
            empTransDao.closeConnection(conn);
        }
        return success;
    }
    
    /**
     * 定时清除消息内存
     * @description           			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-20 下午05:22:32
     */
    public void checkGroupInfo()
    {
        // 当前时间
        long nowTime = System.currentTimeMillis();
        // 复制群组消息的对象
        Map<String,JSONArray> newMap = new HashMap<String,JSONArray>();
        if(null != groupMsgMap)
        {
            newMap.putAll(groupMsgMap);
        }
        Iterator<String> iter = newMap.keySet().iterator();
        String key = null;
        while(iter.hasNext())
        {
            key = iter.next();
            JSONArray groupArray = newMap.get(key);
            if(groupArray == null)
            {
                continue;
            }
            for(int i = 0;i < groupArray.size();i++)
            {
                JSONObject groupMsg = (JSONObject) groupArray.get(i);
                //如果群组的发送消息间隔现在大于两分钟
                if(nowTime - StringUtils.parseTimeToLong((String)groupMsg.get("time")) > 2*60*1000)
                {
                    synchronized(groupMsgMap)
                    {
                        groupMsgMap.get(key).remove(0);
                    }
                }else
                {
                    i = groupArray.size();
                }
            }
        }
    }
    /**
     * 添加客服到群组
     * @description    
     * @param gpId
     * @param gpmids
     * @return       			 
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2013-12-20 上午10:46:50
     */
    public boolean joinGroup(String gpId,String gpmIds)
    {
    	OttGenericTransactionDAO transDao = new OttGenericTransactionDAO();
        boolean success = false;
        //获取事务连接
        Connection conn = transDao.getConnection();
        try
        {
           transDao.beginTransaction(conn);
           if(!StringUtils.isEmpty(gpId) || !StringUtils.isEmpty(gpmIds))
           {
             //查询添加客服是否已经存在于组中
               LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
               conditionMap.put("gpId", gpId);
               conditionMap.put("gmUser&in", gpmIds);
               List<LfOnlGpmem>  memberList =  empDao.findListByCondition(LfOnlGpmem.class, conditionMap, null);
             
               List<String> existGpmIds = new ArrayList<String>();
               if(memberList!=null&&memberList.size()>0)
               {
                   for(LfOnlGpmem m : memberList)
                   {
                       existGpmIds.add(String.valueOf(m.getGmUser()));
                   }
               }
               
               //需要添加的成员
               String[] tempGpmIds = gpmIds.split(",");
               List<String> gpmIdList = new ArrayList<String>() ;
               for (int i = 0; i < tempGpmIds.length; i++)
               {
                   gpmIdList.add(tempGpmIds[i]);
               }
               
               Collection<String> intersection = CollectionUtils.intersection(gpmIdList, existGpmIds);
               gpmIdList.removeAll(intersection);
               
               //将不存在的的成员添加到群组
               memberList.clear();
               for(int i=0;i<gpmIdList.size();i++)
               {
                   LfOnlGpmem member = new LfOnlGpmem();
                   member.setGpId(Long.valueOf(gpId));
                   member.setGmUser(Long.valueOf(gpmIdList.get(i)));
                   member.setGmState(1);
                   memberList.add(member);
               }
               transDao.savePro(conn, memberList, LfOnlGpmem.class);
               success = true;
           }
           else 
           {
               success = false;
           }
        }
        catch(Exception e)
        {
            transDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, "new GroupChatBiz().joinGroup Is Error");
        }
        finally
        {
            transDao.closeConnection(conn);
        }
        return success;
    }
    
    /**
     * 退群
     * @description    
     * @param gpIds
     * @return		 
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2013-12-20 下午02:50:01
     */
    public int leaveGroup(String gpmIds,String customeId,String name){
    	int deleteCount = 0;
    	LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
    	conditionMap.put("gpId", gpmIds);
    	conditionMap.put("gmUser",  customeId);
    	
    	setGroupMsgJson(gpmIds, customeId, "tcqz", name+" 退出了群组", name);
    	//获取事务连接
    	try{
    		deleteCount = empDao.delete(LfOnlGpmem.class, conditionMap);
    	}catch(Exception e){
    		EmpExecutionContext.error(e, "new GroupChatBiz().leaveGroup Is Error"); 
    	}finally{
    	}
    	return deleteCount;
    }
    /**
     * 检验群组名称是否重复
     * @description    
     * @param userId 用户id
     * @param name 群组名称
     * @return		 
     * @author Administrator <foyoto@gmail.com>
     * @datetime 2013-12-20 下午02:50:01
     */
    public int checkGroupName(String userId,String name){
        int gpid = 0;
		try {
			gpid = new CustomChatDao().getGroupIdByName(userId, name);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"检验群组名称是否重复异常！");
			return -1;
		}
        if(gpid > 0)
        {
        	return 0;
        }else
        {
        	return 1;
        }
    }
    
    /**
     * 加载未读群组消息
     * @description    
     * @param id        客服id
     * @param groupKeys 群组id
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-27 上午10:11:30
     */
    @SuppressWarnings("unchecked")
    public JSONArray getGroupMaxInfo(String id,String groupKeys)
    {
        JSONArray jsonArr = new JSONArray();
        CustomChatDao chatDao = new CustomChatDao();
        Long maxId = maxGroupMsgId;
        try
        {
        	// 已读消息id
        	Long readedMsgId = chatDao.getGpMsgId(id); 
        	if(readedMsgId == 0)
        	{
        		readedMsgId = 1l;
        		LfOnlGpmsgid gp = new LfOnlGpmsgid(1l,Long.valueOf(id));
        		empDao.save(gp);
        	}
            // 如果不属于任何群组，则直接返回
            if(groupKeys == null || "".equals(groupKeys))
            {
                return jsonArr;
            }
            List<LfOnlMsgHis> msgHisList = chatDao.getGroupMsg(id,groupKeys,maxId.toString(),readedMsgId);
            if(msgHisList == null || msgHisList.size() == 0)
            {
                return jsonArr;
            }
            for(LfOnlMsgHis msgHis : msgHisList)
            {
                JSONObject jsonOb = new JSONObject();
                jsonOb.put("fromuser", msgHis.getFromUser()); 
                jsonOb.put("message", msgHis.getMessage()); 
                jsonOb.put("time", StringUtils.timeFormat(msgHis.getSendTime())); 
                jsonOb.put("msgtype", msgHis.getMsgType()); 
                jsonOb.put("servernum", msgHis.getServerNum());
                //发送者名称，群组发送时为群组名称，客服对客服发送时为客服人员名称，手机对客服时为手机用户昵称
                jsonOb.put("name", CustomChatBiz.getPushName(msgHis.getPushType().toString(), msgHis.getFromUser(), ""));
                jsonOb.put("dotype", 1);
                jsonOb.put("msgid", msgHis.getMId());
                jsonOb.put("pushtype", msgHis.getPushType().toString());
                
                jsonArr.add(jsonOb);
            }
            return jsonArr;
        }
        catch (Exception e)
        {
            return jsonArr;
        }finally
        {
            isUpdateDbMap.put(id, false);
            //updateGpMsgId(maxId, id);
        }
        
    }
    /**
     * 获取群组人员列表
     * @description    
     * @param groupId 群组id
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-27 上午10:40:31
     */
    public JSONArray getGroupMember(String groupId,String aid,String user_id)
    {
        JSONArray jsonArr = new JSONArray();
        CustomChatDao chatDao = new CustomChatDao();
        
        try
        {
            List<DynaBean> userList = chatDao.getGroupMem(groupId,user_id);
            if(userList == null)
            {
                return jsonArr;
            }
            for(DynaBean user : userList)
            {
                JSONObject json = new JSONObject();
                json.put("customeid", user.get("user_id").toString());
                
                String name = (String) user.get("name");
                if(user.get("mark_name") != null)
                {
                	name = (String) user.get("mark_name");
                }
                json.put("name", name+"("+user.get("user_name")+")");
                
                Integer state = CustomStatusBiz.getCustomStatusMap().get(user.get("user_id").toString());
                state = state==null?4:state;
                json.put("state", state);
                
                if(user.get("a_id") != null && user.get("a_id").toString().equals(aid))
                {
                	json.put("iswei", 1);
                }else
                {
                	json.put("iswei", 0);
                }
                if(state == 1)
                {
                	jsonArr.add(0,json);
                }else
                {
                	jsonArr.add(json);
                }
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"获取客服群组人员列表失败！");
        }
        return jsonArr;
    }
    
    public static Map<String,Long> getUserReadedIdMap()
    {
    	return userReadedIdMap;
    }
    
    public static Map<String,JSONArray> getGroupMspMap()
    {
    	return groupMsgMap;
    }
    
    public static Map<String,Boolean> getIsUpdateDbMap()
    {
    	return isUpdateDbMap;
    }
}
