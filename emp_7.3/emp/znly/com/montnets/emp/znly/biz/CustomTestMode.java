package com.montnets.emp.znly.biz;

import com.montnets.emp.ottbase.util.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedHashSet;


public class CustomTestMode
{

    // 是否调试模式
    private static boolean isTestMode = false;
    
    private static final LinkedHashSet<String> messMap = new LinkedHashSet<String>();

    private static Integer index = 0;
    
    public static void setTestMode(boolean mode)
    {
    	isTestMode = mode;
    }
    
    public static boolean getTestMode()
    {
    	return isTestMode;
    }
    
    /**
     * 封装客服推送消息
     * @description     客服接收消息封装  
     * @param toCusUser 接收者
     * @param serverNum 服务号
     * @param fromUser  发送者
     * @param msgTpe    消息类型：text-文本消息，image-图片消息，voice-声音消息
     * @param msg       消息内容
     * @param pushType  推送类型：1-客户to客服，2-客服to客户，3-客服to客服，4-群组，5-转接客服
     * @param name      发送者名称
     * @return          返回消息推送时间
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-4 下午04:52:42
     */
    @SuppressWarnings("unchecked")
    public static String setMsgJson(String toCusUser, String serverNum, String fromUser, String msgTpe, String msg, String pushType, String name,Long AId)
    {
        // 当前时间
        Timestamp sendTime = new Timestamp(System.currentTimeMillis());
        String time = StringUtils.timeFormat(sendTime);
        
        JSONArray jsonArr = new JSONArray();
        // 存入历史消息库
        Long newId  = CustomChatBiz.insertChatMsg(AId, fromUser, toCusUser, 2, serverNum, msg, sendTime, msgTpe, Integer.parseInt(pushType));
        if(newId != null && newId.intValue() == 0)
        {
            return "error";
        }
        JSONObject jsonOb = new JSONObject();
        
        jsonOb.put("fromuser", fromUser);
        jsonOb.put("message", msg);
        jsonOb.put("time", time);
        jsonOb.put("msgtype", msgTpe);
        jsonOb.put("servernum", serverNum);
        // 发送者名称，群组发送时为群组名称，客服对客服发送时为客服人员名称，手机对客服时为手机用户昵称
        name = CustomChatBiz.getPushName(pushType, fromUser, name);
        jsonOb.put("name", name);
        jsonOb.put("pushtype", pushType);
        jsonOb.put("msgid", newId);
        jsonArr = CustomChatBiz.getMsgMap().get(toCusUser);
        if(jsonArr == null)
        {
            jsonArr = new JSONArray();
        }
        jsonArr.add(jsonOb);
        CustomChatBiz.getMsgMap().put(toCusUser, jsonArr);
        //System.out.println(msg);
        // //System.out.println(jsonArr.toString());
        index ++;
        return time;
    }
    
    public static String sendmsg(String msg,String userid,String time,String touser,String stype)
    {
    	index = index +1;;
    	String mess = index.toString()+"."+stype+"&lt;"+userid+"&gt;-&lt;"+touser+"&gt;"+time+"<br/>"+msg;
    	messMap.add(mess);
    	return time;
    }
    
    public static String getmsg()
    {
    	JSONArray json = new JSONArray();
    	synchronized(messMap)
        {
	    	Iterator<String> it = messMap.iterator();
	    	
	    	while(it.hasNext())
	    	{
	    		json.add(it.next());
	    	}
        }
    	return json.toString();
    }
    
    public static void remove(int i)
    {
    	Iterator<String> it = messMap.iterator();
    	
    	synchronized(messMap)
        {
	    	while(it.hasNext())
	    	{
	    		if(i>0)
	    		{
	    			messMap.remove(it.next());
	    		}
	    		i--;
	    	}
        }
    }
}
