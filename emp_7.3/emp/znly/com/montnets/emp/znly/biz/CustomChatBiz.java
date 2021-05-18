package com.montnets.emp.znly.biz;

import com.montnets.emp.appwg.biz.WgMwFileBiz;
import com.montnets.emp.appwg.initbuss.HandleMsgBiz;
import com.montnets.emp.appwg.initbuss.IInterfaceBuss;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.appmage.LfAppMwClient;
import com.montnets.emp.entity.online.LfOnlGroup;
import com.montnets.emp.entity.online.LfOnlMsg;
import com.montnets.emp.entity.online.LfOnlMsgHis;
import com.montnets.emp.entity.online.LfOnlServer;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiUserInfo;
import com.montnets.emp.ottbase.biz.WeixBiz;
import com.montnets.emp.ottbase.param.WeixParams;
import com.montnets.emp.ottbase.service.IWeixinService;
import com.montnets.emp.ottbase.service.WeixService;
import com.montnets.emp.ottbase.util.GetSxCount;
import com.montnets.emp.ottbase.util.StringUtils;
import com.montnets.emp.ottbase.util.WavFileUtil;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.znly.dao.CustomChatDao;
import org.apache.commons.beanutils.DynaBean;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;

public class CustomChatBiz
{
    // 手机用户对应的服务关系，key-手机用户openId，value-服务号
    private static final Map<String, String>      queueMap         = new HashMap<String, String>();

    // 服务号对应的消息
    private static final Map<String, JSONObject>  serverMap        = new HashMap<String, JSONObject>();

    // 消息集合
    private static final Map<String, JSONArray>   msgMap           = new HashMap<String, JSONArray>();

    // 客服接入的微信用户集合
    private static final Map<String, String>       custWeixObjectMap = new HashMap<String, String>();
    
    // 客服接入的APP用户集合
    private static final Map<String, String>       custAppObecjtMap = new HashMap<String, String>();

    // 公众号信息集合
    private static final Map<String, LfWeiAccount> accountMap       = new HashMap<String, LfWeiAccount>();
    
    // 客服分配队列集合user(id值):{id,count:已接入客户数量,aid:公众号id}
    private static final JSONObject      serCountMap      = new JSONObject();

    public static JSONObject getSerCountMap() {
        return serCountMap;
    }

    // 微信客服分配队列 key=公众号id，value{“客服id”，接入个数}
    private static final LinkedHashMap<String,JSONObject> weixCustMap = new LinkedHashMap<String, JSONObject>();
    
    // 转接关系
    private static final Map<String,String> zjMap = new HashMap<String,String>();
    
    // 数据库访问层
    private static final IEmpDAO                  empDao           = new DataAccessDriver().getEmpDAO();

    // 退出客服指令
    private static final String                   logout           = "tckf";

    // 服务超时提醒时间设置：20分钟
    private final int                             serverOverTime   = 20 * 60 * 1000;

    // 客服评价超时时间设置：3分钟
    private final int                             evaluateOverTime = 3 * 60 * 1000;
    
    // 存放app对应企业编码的内存
    private static final Map<String,String> appEcodeMap = new LinkedHashMap<String, String>();

    /**
     * 手机上行消息推送
     * 
     * @description 微信上行消息接收后调用测方法
     * @param account
     *        微信公众账户对象
     * @param paramsXmlMap
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-11-29 下午03:38:52
     */
    @SuppressWarnings("unchecked")
    public static String doPush(LfWeiAccount account, Map<String, String> paramsXmlMap)
    {

        // 获取发送者
        String fromUser = paramsXmlMap.get("FromUserName");
        // 获取上行内容
        String msg = paramsXmlMap.get("Content");
        // 获取消息类型
        String msgType = paramsXmlMap.get("MsgType");

        if(queueMap.get(fromUser) == null)
        {
            return "noConnection";
        }
        // 获取服务号
        String serverNum = queueMap.get(fromUser);
        // 获取服务号对应的客服人员
        String toCusUser = queueMap.get(serverNum);

        int serState = (Integer) serverMap.get(serverNum).get("state");
        switch (serState)
        {
            case 1:
                if(msg != null && logout.equals(msg.toLowerCase()))
                {
                    pushEvaluate(toCusUser, fromUser, account.getOpenId(), serverNum,1);
                    msg = "客户退出了客服流程，等待客户进行客服评价。";
                    msgType = "tips";
                }
                else
                {
                    // 更新服务号的激活时间，防止超时
                    updateServerTime(serverNum);
                }
                break;
            case 2:
                if("text".equals(msgType) && logout.equals(msg.toLowerCase()))
                {
                    // 推送客服评价
                    pushEvaluate(toCusUser, fromUser, account.getOpenId(), serverNum,1);
                    msg = "客户退出了客服流程，等待客户进行客服评价。";
                    msgType = "tips";
                }
                else
                {
                    // 更新服务号的激活时间，防止超时
                    updateServerTime(serverNum);
                }
                break;
            case 3:
                saveEvaluate(account.getAId().toString(), toCusUser, fromUser, account.getOpenId(), serverNum, msg,1);
                return "pushSucess";
        }

        if(!"text".equals(msgType) && !"tips".equals(msgType))
        {
            if("location".equals(msgType))
            {
                msg = "我的位置："+paramsXmlMap.get("Label");
                msgType = "text";
            }
            else
            {
                // 声音上行与图片上行的时候处理
                WeixBiz weixBiz = new WeixBiz();
                String accessToken = weixBiz.getToken(account);
                // 下载资源文件到本地服务,成功则返回资源文件本地路径
                msg = weixBiz.downLoadResource(msgType, accessToken, paramsXmlMap.get("MediaId"),account.getOpenId());
                if("fail".equals(msg))
                {
                   return "error";
                }
                // 如果是声音文件
                if("voice".equals(msgType))
                {
                    JSONObject voiceJson = new JSONObject();
                    
                    try {
						voiceJson.put("second", WavFileUtil.getAmrDuration(msg));
					} catch (IOException e) {
						voiceJson.put("second",0);
					}
					StringBuffer ssb = new StringBuffer(msg);
			        msg = ssb.replace(msg.lastIndexOf("."),msg.length(),".mp3").toString();
                    voiceJson.put("path", msg);
                    // 音频文字信息
                    voiceJson.put("msg", paramsXmlMap.get("Recognition"));
                    msg = voiceJson.toString();
                }
            }
        }

        setMsgJsonBefore(toCusUser, serverNum, fromUser, msgType, msg, "1", null, account.getAId(),1);
        return "pushSucess";
    }

    /**
     * 呼叫客服
     * 
     * @description
     * @param account
     * @param paramsXmlMap
     * @return
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-18 下午03:21:15
     */
    @SuppressWarnings("unchecked")
    public static String callCustome(LfWeiAccount account, Map<String, String> paramsXmlMap)
    {
    	// 上行请求的数据
        String fromUser = paramsXmlMap.get("FromUserName");
        // 手机上行客服请求后获取
        // 空闲的客服人员列表
        if(queueMap.get(fromUser) != null)
        {
            String msg = "您好，您当前已经处于客服会话状态，无需重新呼叫客服！";
            return doSendWeix(fromUser, msg, "text", account.getOpenId(), queueMap.get(queueMap.get(fromUser)));
        }
        // 当系统自动分配时则返回单一对象
        String cususer = getWeixCustomeUser(account.getAId().toString());
        
        accountMap.put(account.getOpenId(), account);

        if(cususer != null)
        {
            String serverNum = getServerNum(fromUser,"WEI");// 生成服务号
            String customeId = cususer;

            // 插入客服会话表
            LfOnlServer onlServer = new LfOnlServer();
            onlServer.setCustomeId(Long.valueOf(customeId));
            onlServer.setAId(account.getAId());
            onlServer.setFromUser(fromUser);
            onlServer.setSerNum(serverNum);
            onlServer.setSerType(1);

            try
            {
                empDao.save(onlServer);
                
                queueMap.put(serverNum, customeId);
                queueMap.put(fromUser, serverNum);

                JSONObject serverJson = new JSONObject();
                
                serverJson.put("customeid", customeId);
                serverJson.put("begintime", System.currentTimeMillis());
                serverJson.put("time", System.currentTimeMillis());
                serverJson.put("openid", account.getOpenId());
                serverJson.put("state", 1);
                serverJson.put("fromuser", fromUser);
                serverJson.put("aid", account.getAId());
                serverJson.put("sertype", 1);

                serverMap.put(serverNum, serverJson);
                setCustSerObj(customeId, fromUser, "1",true);
                String msg = "";
                
                String name = getPushName("1", fromUser, null);
                msg = "客户[" + name + "]已接入，现在可以进行会话！";
                setMsgJson(customeId, serverNum, fromUser, "tips", msg, "1", null,account.getAId(),1);
                
                msg = "您好，现在可以开始回答您的客服问题！回复[" + logout + "]，则退出客服服务！";
                return doSendWeix(fromUser, msg, "text", account.getOpenId(), customeId);
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "呼叫客服异常！");
            }
        }
        // 返回没有合适的客服人员推送
        String msg = "您好！您的信息我们已收到，稍后为您提供服务，谢谢！";
        return doSendWeix(fromUser, msg, "text", account.getOpenId(), "");

    }

    /**
     * 判断是否客服流程内的请求
     * @description    
     * @param fromUser      客户openid
     * @param requestType   请求类型
     * @return              true - 该客户请求属于客服请求。false - 非客服请求     			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-4 下午02:53:49
     */
    public static boolean checkCustome(String fromUser, Integer requestType)
    {
        return (queueMap.get(fromUser) == null || (requestType > 2 && requestType != 8));
    }

    /**
     * 获取空闲的客服人员
     * 
     * @description 按照接入个数的顺序
     * @param AId
     *        公众号ID
     * @return
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-18 下午02:02:44
     */
    @SuppressWarnings("unchecked")
    public static String getWeixCustomeUser(String AId)
    {
        String key = null;
        Integer count = 0;
        // 最小接入个数的键值
        String minKey = null;
        Integer minCount = null;
        synchronized (weixCustMap)
        {
        	if(weixCustMap.size() == 0)
        	{
        		return null;
        	}
            JSONObject countJson = weixCustMap.get(AId);
            if(countJson == null)
            {
            	return null;
            }
            Iterator<String> it = countJson.keySet().iterator();
            
            if(it.hasNext())
            {
            	minKey = it.next();
            	minCount = (Integer) countJson.get(minKey);
            }
            while(it.hasNext())
        	{
            	key = it.next();
            	count = (Integer) countJson.get(key);
            	if(minCount > count)
            	{
            		minKey = key;
            		minCount = count;
            	}
        	}
            if(minKey == null)
            {
            	return null;
            }else
            {
            	// 先移除选中的客服消息
            	countJson.remove(minKey);
            	// 再追加到末尾
            	countJson.put(minKey, minCount+1);
            	// 重设总接入个数的集合
            	serCountMap.put(minKey,(Integer)serCountMap.get(minKey)+1);
            	weixCustMap.put(AId, countJson);
            	
            	return minKey;
            }
        }
    }

    /**
     * 手机退出客服状态时调用
     * 
     * @description
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-23 上午09:53:26
     */
    private static void outOfCustome(Long AId, String customId, String fromUser, String openId, boolean isScore,int sertype)
    {
    	try
		{
			setCustSerObj(customId, fromUser, String.valueOf(sertype), false);
			
			// 移除客服对应关系
			String serverNum = queueMap.get(fromUser);
			queueMap.remove(fromUser);
			queueMap.remove(serverNum);
			zjMap.remove(serverNum);
			boolean updateRes = updateServerEndTime(serverNum);
			if(!updateRes){
				EmpExecutionContext.error("手机退出客服，处理并更新记录失败。");
				return;
			}
			String msg = "客服会话已结束。欢迎您再次来访！";
			if(isScore)
			{
			    msg = "感谢您对我们的服务做出评价。" + msg;
			}
			if(sertype == 1)
			{
				doSendWeix(fromUser, msg, "tips", openId, customId);
				setMsgJson(customId, serverNum, fromUser, "tips", "客服会话已结束！", String.valueOf(sertype), null,AId,2);
			}/*else if(sertype == 6)
			{
				doSendApp(customId, msg, "tips", fromUser, openId);
			}*/
			// 移除客服已关联的手机用户
			updateServerCount(AId,customId,false);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "手机退出客服状态异常。");
		}
    }

    /**
     * 更新服务接入情况
     * @param AId
     * @param customId
     * @param isAdd
     */
    private static void updateServerCount(Long AId,String customId,boolean isAdd)
    {
    	int count = isAdd?1:-1;
        synchronized (serCountMap)
        {
        	if(serCountMap.get(customId) != null )
        	{
        		Integer curCount = (Integer)serCountMap.get(customId);
        		if(count != -1 && curCount != 0)
        		{
        			serCountMap.put(customId, curCount+count);
        		}
        	}
        }
        if(AId != null && AId.intValue() > 0 )
        {
        	String aid = AId.toString();
        	synchronized (weixCustMap)
            {
        		if(weixCustMap.get(aid) != null)
        		{
        			if(weixCustMap.get(aid).get(customId) != null)
        			{
        				Integer curCount = (Integer)weixCustMap.get(aid).get(customId);
                		if(count != -1 && curCount != 0)
                		{
                			weixCustMap.get(aid).put(customId,curCount + count);
                		}
        			}
        		}
            }
        }
    }
    
    /**
     * 初始化客服人员的服务数量
     * @description 初始化客服人员的服务数量，客服人员登录时调用
     * @param userId
     *        客服Id
     * @param AId
     *        所属公众号Id
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-18 下午02:02:06
     */
    @SuppressWarnings("unchecked")
    public void initServiceCount(String userId, String AId)
    {
        
        synchronized (serCountMap)
        {
        	// 计算接入的微信用户的个数
            String serObs = custWeixObjectMap.get(userId);
            int count = 0;
            if(serObs != null && serObs.endsWith(","))
            {
                count = serObs.split(",").length;
            }
            if(AId != null && !"0".equals(AId))
            {
            	JSONObject userJson = weixCustMap.get(AId);
            	if(userJson == null)
            	{
            		userJson = new JSONObject();
            	}
            	userJson.put(userId, count);
            	// 根据公众号id区分的接入个数集合
            	weixCustMap.put(AId, userJson);
            }
            serObs = custAppObecjtMap.get(userId);
            if(serObs != null && serObs.endsWith(","))
            {
                count += serObs.split(",").length;
            }
            
            // 统计结果为最终个数
            serCountMap.put(userId,count);
        }
    }

    /**
     * 移除客服分配队列
     * @description    
     * @param customId
     * @param aid       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-4 下午02:51:44
     */
    public static void removeServerCount(String customId,String aid)
    {
        //System.out.println("aid="+aid+",customId="+customId);
        synchronized (serCountMap)
        {
        	serCountMap.remove(customId);
        }
        if(aid != null && !"0".equals(aid) )
        {
        	synchronized (weixCustMap)
            {
        		if(weixCustMap.get(aid)!= null)
        		{
        			weixCustMap.get(aid).remove(customId);
        		}
            }
        }
    }
    
    /**
     * 封装客服人员json格式数据
     * 
     * @description
     * @param userList  客服人员集合
     * @param aid   公众号Id
     * @parame stateReq 是否过滤状态，1-过滤，0-不过滤
     * @return  客服人员集合json
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-20 下午02:10:22
     */
    @SuppressWarnings("unchecked")
    public String getUserJson(List<DynaBean> userList,String aid, int stateReq)
    {
        JSONArray resultArray = new JSONArray();
        if(userList == null || userList.size() == 0)
        {
            return null;
        }
        Map<String, Integer> stateMap = CustomStatusBiz.getCustomStatusMap();
        for (DynaBean user : userList)
        {
            JSONObject userJson = new JSONObject();
            Long userid = Long.valueOf( user.get("user_id").toString());
            
            Integer state = stateMap.get(userid.toString());
            // 如果需要过滤状态时，过滤非在线状态的客服
            if(stateReq == 1 && (state == null || state != 1))
            {
                continue;
            }
            String name = (String) user.get("name");
            if(user.get("mark_name") != null)
            {
            	name = (String) user.get("mark_name");
            }
            userJson.put("name", name+"("+user.get("user_name")+")");
            userJson.put("customeId", userid);
           
            if(state == null)
            {
                userJson.put("state", 4);
            }else
            {
                userJson.put("state", state);
            }
            if(user.get("a_id") != null && user.get("a_id").toString().equals(aid))
            {
            	userJson.put("iswei", 1);
            }else
            {
            	userJson.put("iswei", 0);
            }
            

            resultArray.add(userJson);
        }
        return resultArray.toString();
    }

    /**
     * 生成服务号
     * 
     * @description
     * @param fromUser
     * @return
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-16 下午01:58:43
     */
    private static String getServerNum(String fromUser,String serStr)
    {
        String serverNum = serStr+(fromUser.length()>7?fromUser.substring(0, 4).toUpperCase():fromUser)  +
            String.valueOf(System.currentTimeMillis()).substring(8, 12) +
            GetSxCount.getInstance().getCount();
        return serverNum;
    }

    /**
     * 获取消息
     * 
     * @description
     * @param customId
     * @return
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-11-29 下午03:38:45
     */
    public static JSONArray getMsg(String customId)
    {
        JSONArray mess = null;

        synchronized (msgMap)
        {
            mess = msgMap.get(customId);
        }
        if(mess == null)
        {
            mess = new JSONArray();
        }
        return mess;
    }

    /**
     * 移除已读消息
     * 
     * @description 移除已读消息
     * @param customId
     *        客服ID
     * @param len
     *        已读消息条数
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-10 下午07:27:12
     */
    public static void removeMess(String customId, String len, String aid)
    {
        int size = 0;
        if(len != null && !"".equals(len))
        {
            size = Integer.parseInt(len);
        }
        synchronized (msgMap)
        {
            JSONArray jsonArr = msgMap.get(customId);
            if(jsonArr != null)
            {
            	//集合大小,防止下标越界
            	int jsonLen = jsonArr.size();
                for (int i = 0; i < size && i < jsonLen; i++)
                {
                   /* jsonObject = (JSONObject) jsonArr.get(0);
                    Timestamp sendTime = StringUtils.parseTime((String) jsonObject.get("time"));
                    insertChatMsg(AId, (String) jsonObject.get("fromuser"), customId, 2, (String) jsonObject.get("servernum"), 
                        (String) jsonObject.get("message"), sendTime, (String) jsonObject.get("msgtype"),
                         Integer.parseInt((String) jsonObject.get("pushtype")));*/
                    jsonArr.remove(0);
                }
                msgMap.put(customId, jsonArr);
            }
        }
    }

    /**
     * 客服下发消息判断
     * @description  客服向客户下发消息时，判断客户与客服关系是否对应  
     * @param toUser    发送对象
     * @param msg       发送消息
     * @param msgType   消息类型（text-文本，image-图片）
     * @param openId    公众号
     * @param fromUser  发送者
     * @return error-发送失败，成功则返回时间格式的字符串      			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-17 上午11:52:30
     */
    public static String doSendBefore(String toUser, String msg, String msgType, String openId, String fromUser)
    {
       
        // 判断客户是否存在客服队列中
        if(queueMap.get(toUser) == null || !fromUser.equals(queueMap.get(queueMap.get(toUser))))
        {
            WeixBiz weixBiz = new WeixBiz();
            LfWeiAccount account = weixBiz.getWeixAccountByOpen(openId);
            setMsgJson(fromUser, queueMap.get(toUser), toUser, "tips", "客户已退出本次会话，无法进行信息下发！", "1", "", account.getAId());
            return  "error";
        }
        return doSendWeix(toUser, msg, msgType, openId, fromUser);
    }
    
    /**
     * 向微信下发消息
     * 
     * @description 通过微信接口给手机用户下发消息
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-11-29 下午03:38:27
     */
    public static String doSendWeix(String toUser, String msg, String msgType, String openId, String fromUser)
    {
    	Timestamp sendTime = new Timestamp(System.currentTimeMillis());
    	// 如果是调试模式，则不调用接口
    	if(CustomTestMode.getTestMode())
    	{
    		return CustomTestMode.sendmsg(msg, fromUser, StringUtils.timeFormat(sendTime),toUser,"WEI");
    	}
        WeixBiz weixBiz = new WeixBiz();
        LfWeiAccount account = weixBiz.getWeixAccountByOpen(openId);
        // 写入历史消息库
        String access_token = weixBiz.getToken(account);
        if("image".equals(msgType))
        {
            String fileName = "uploadFile";
            // 上传文件到微信服务器
            String mediaid = weixBiz.uploadToWeix(fileName, msg, msgType, access_token,openId);
            if(!"fail".equals(mediaid))
            {
                // 向微信服务器发送图片下发请求
                String result = weixBiz.sendWeixMsg(mediaid, msgType, access_token, toUser,openId);
                if("success".equals(result) && insertChatMsg(account.getAId(), fromUser, toUser, 2, queueMap.get(toUser), msg, sendTime, msgType, 2)- 0 > 0)
                {
                    return StringUtils.timeFormat(sendTime);
                }
            }
            return "error";
        }

        // "XotSG6be3MVkatsQzdgxR8QK6KREPlmclMJYL7fuglSZxeeP0-NSYPyhYxOUjaUROtZzAluOZADT0BjsKJL2aM6aisJNpsT79yBuSUVzbA4nhpjcJoBwJi0_gGqKdoNSrS8D-rvFAIiErFkCYXOdag";//
        WeixParams weixParams = new WeixParams();
        weixParams.setAccess_token(access_token);
        weixParams.setContent(msg);
        weixParams.setMsgtype("text");
        weixParams.setTouser(toUser);

        IWeixinService service = new WeixService();
        try
        {
            WeixParams params = service.sendCustomerMsg(weixParams);
            if(params != null && "000".equals(params.getErrCode()))
            {
                
                if(insertChatMsg(account.getAId(), fromUser, toUser, 2, queueMap.get(toUser), msg, sendTime, msgType, 2)- 0 > 0)
                {
                    return StringUtils.timeFormat(sendTime);
                }
                else
                {
                    return "error";
                }
            }else
            {
            	// 如果验证失效，则重新获取token
            	if(params != null && weixBiz.validateAssessToken(params.getErrCode()))
            	{
            		weixBiz.reGetToken(openId);
            	}
            	if(params!=null){
                return params.getErrCode();
            	}else{
            		return "error";
            	}
            }
                
                
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            EmpExecutionContext.error(e, "");
        }
        return "error";

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
    public static String setMsgJson(String toCusUser, String serverNum, String fromUser, String msgTpe, String msg, String pushType, String name,Long AId)
    {
    	int dotype = 0;
    	return setMsgJson(toCusUser, serverNum, fromUser, msgTpe, msg, pushType, name, AId, dotype);
    }
    
    /**
     * 封装客服推送消息
     * @description     客服接收消息封装  
     * @param toCusUser 接收者
     * @param serverNum 服务号
     * @param fromUser  发送者
     * @param msgType    消息类型：text-文本消息，image-图片消息，voice-声音消息
     * @param msg       消息内容
     * @param pushType  推送类型：1-客户to客服，2-客服to客户，3-客服to客服，4-群组，5-转接客服
     * @param name      发送者名称
     * @param dotype	前台操作标识，0-不操作，1-上移，2-下移
     * @return          返回消息推送时间
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-4 下午04:52:42
     */
    @SuppressWarnings("unchecked")
    public static String setMsgJson(String toCusUser, String serverNum, String fromUser, String msgType, String msg, String pushType, String name,Long AId,int dotype)
    {
        // 当前时间
        Timestamp sendTime = new Timestamp(System.currentTimeMillis());
        String time = StringUtils.timeFormat(sendTime);
        
        JSONArray jsonArr = new JSONArray();
        // 存入历史消息库
        Long newId  = insertChatMsg(AId, fromUser, toCusUser, 2, serverNum, msg, sendTime, msgType, Integer.parseInt(pushType));
        if(newId != null && newId.intValue() == 0)
        {
        	EmpExecutionContext.error("客服消息插入数据库失败！fromUser="+fromUser+",toUser="+toCusUser+",hisOrReal=2"
        			+",servernum="+serverNum+",message="+msg+",sendtime="+sendTime+",msgtype="+msgType+",pushType="+pushType);
            return "error";
        }
        JSONObject jsonOb = new JSONObject();
        
        jsonOb.put("fromuser", fromUser);
        jsonOb.put("message", msg);
        jsonOb.put("time", time);
        jsonOb.put("msgtype", msgType);
        jsonOb.put("servernum", serverNum);
        // 发送者名称，群组发送时为群组名称，客服对客服发送时为客服人员名称，手机对客服时为手机用户昵称
        name = getPushName(pushType, fromUser, name);
        jsonOb.put("name", name);
        jsonOb.put("pushtype", pushType);
        jsonOb.put("msgid", newId);
        jsonOb.put("dotype", dotype);
        synchronized (msgMap)
        {
            jsonArr = msgMap.get(toCusUser);
            if(jsonArr == null)
            {
                jsonArr = new JSONArray();
            }
            jsonArr.add(jsonOb);
            msgMap.put(toCusUser, jsonArr);
        }
        //System.out.println(msg);
        // //System.out.println(jsonArr.toString());
        return time;
    }

    /**
     * 推送消息给客服人员前的处理
     * @description     判断客服人员是否在线，不在线则将消息记录写入实时消息记录表
     * @param toCusUser
     * @param serverNum
     * @param fromUser
     * @param msgType
     * @param msg
     * @param pushType
     * @param name
     * @param aId
     * @return       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-4 下午02:55:25
     */
    public static String setMsgJsonBefore(String toCusUser, String serverNum, String fromUser, String msgType, String msg, String pushType, String name, Long aId,int dotype)
    {
        // 客户发给客服时，客服发给客服时，需判断接收者是否在线，不在线则消息入库
        if("1".equals(pushType) || "3".equals(pushType) || "6".equals(pushType) )
        {
            // 获取发送对象的状态
            Integer state = CustomStatusBiz.getCustomStatusMap().get(toCusUser);
            // 如果判断发送对象未登录，则写入实时消息库
            if(state == null || state == 4)
            {
                // 如果发送对象已离线，则插入临时消息库
                Timestamp time = new Timestamp(System.currentTimeMillis());
                if(insertChatMsg(aId, fromUser, 
                        toCusUser, 3, serverNum, msg, time, 
                        msgType, Integer.parseInt(pushType)) - 0 > 0)
                {
                	EmpExecutionContext.error("离线消息：fromUser="+fromUser+",toUser="+toCusUser+",time="+StringUtils.timeFormat(time)
                			+",msg="+msg);
                    return StringUtils.timeFormat(time);
                }
                else
                {
                    return "error";
                }
            }
        }
        return setMsgJson(toCusUser, serverNum, fromUser, msgType, msg, pushType, name,aId,dotype);
    }

    /**
     * 获取发送者名称
     * 
     * @description
     * @param pushType
     *        消息推送类型
     * @param fromUser
     *        发送者
     * @return
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-10 上午11:44:19
     */
    private final static Map<String, String> nameMap = new HashMap<String, String>();

    public static Map<String, String> getNameMap() {
        return nameMap;
    }

    /**
     * 获取推送对象名称
     * 
     * @description
     * @param pushType
     * @param fromUser
     * @param name
     * @return
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-25 下午02:57:37
     */
    public static String getPushName(String pushType, String fromUser, String name)
    {
        try
		{
			if(name != null && !"".equals(name))
			{
			    return name;
			}
			if(fromUser == null || "".equals(fromUser))
			{
				EmpExecutionContext.error("在线坐席获取名称，fromUser为空。pushType="+pushType);
			    return "";
			}
			if(nameMap.get(fromUser) == null)
			{
				EmpExecutionContext.info("在线坐席获取名称，nameMap取值为空。fromUser="+fromUser+",pushType="+pushType);
				
			    if("3".equals(pushType) || "4".equals(pushType) || "2".equals(pushType) || "7".equals(pushType))
			    {
			        if("0".equals(fromUser))
			        {
			            return "";
			        }
			        LfSysuser user = empDao.findObjectByID(LfSysuser.class, Long.valueOf(fromUser));
			        nameMap.put(fromUser, user.getName());
			        EmpExecutionContext.info("在线坐席获取名称，查询操作员获取操作员名称。fromUser="+fromUser+",pushType="+pushType+",name="+user.getName());
			        return user.getName();
			    }
			    else if("6".equals(pushType))
			    {
			    	LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			    	conditionMap.put("appcode", fromUser);
			        List<LfAppMwClient> user = empDao.findListByCondition(LfAppMwClient.class, conditionMap, null);
			        if(user != null && user.size() > 0)
			        {
			        	nameMap.put(fromUser, user.get(0).getNickname());
			        	EmpExecutionContext.info("在线坐席获取名称，查询App用户获取用户昵称。fromUser="+fromUser+",pushType="+pushType+",nickname="+user.get(0).getNickname());
			            return user.get(0).getNickname();
			        }
			    }
			    else
			    {
			    	//获取微信用户昵称
			        name = new CustomChatDao().getNickNameByOpenId(fromUser);
			        nameMap.put(fromUser, name);
			        EmpExecutionContext.info("在线坐席获取名称，查询微信用户获取用户昵称。fromUser="+fromUser+",pushType="+pushType+",name="+name);
			        return name;
			    }
			}
			else
			{
			    if("3".equals(pushType) || "4".equals(pushType) || "2".equals(pushType) || "7".equals(pushType))
			    {
			    	return nameMap.get(fromUser);
			    }
			    else if("6".equals(pushType))
			    {
			    	return nameMap.get(fromUser);
			    }
			    else
			    {
			    	//获取微信用户昵称
					//return nameMap.get(fromUser);
					//重新获取最新同步的昵称 保证一致性
				    name = new CustomChatDao().getNickNameByOpenId(fromUser);
				    nameMap.put(fromUser, name);
			        EmpExecutionContext.info("在线坐席获取名称，查询微信用户获取用户昵称。fromUser="+fromUser+",pushType="+pushType+",name="+name);
			        return name;
			    }
			}

			return "";
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "在线坐席获取名称异常。fromUser="+fromUser+",pushType="+pushType+",name="+name);
			return "";
		}
    }

    /**
     * 获取客服群组
     * 
     * @description 通过传入的客服人员Id查找该客服所属的客服群组
     * @param userId
     *        客服人员Id
     * @return 客服群组集合json格式
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-11 下午02:59:51
     */
    @SuppressWarnings("unchecked")
    public String getCustomeGroupList(String userId)
    {
        try
        {
            JSONArray resultArray = new JSONArray();
            List<LfOnlGroup> groupList = new CustomChatDao().getCustomeGroupList(userId);
            if(groupList == null || groupList.size() == 0)
            {
                return null;
            }
            for (LfOnlGroup group : groupList)
            {
                JSONObject userJson = new JSONObject();

                userJson.put("name", group.getGpName());
                userJson.put("groupid", group.getGpId());
                userJson.put("count", group.getMemCount());

                resultArray.add(userJson);
            }
            return resultArray.toString();
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "获取客服群组失败！");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    /**
     *
     *  获取客服连接的微信用户列表
     *  @description
     *  @param custtomeId 客服Id
     *  @param customeId
     *  @param Aid
     *  @return 手机用户信息Json格式
     *  @author linzhihan <zhihanking@163.com>
     *  @datetime 2013-12-18 下午02:06:49
     */
    public String getChatUserInfos(String customeId,String Aid)
    {
    	if(Aid == null || "0".equals(Aid))
    	{
    		return "[]";
    	}
        // 从静态变量中读取接入信息
        String userInfos = custWeixObjectMap.get(customeId);
        JSONObject resultObject = new JSONObject();
        JSONArray curUserArray = new JSONArray();
        JSONArray todayArray = new JSONArray();
        JSONArray yesterArray = new JSONArray();
        JSONArray agoArray = new JSONArray();
       
        try
        {
            
            if(userInfos != null &&  userInfos.lastIndexOf(",") > -1 && userInfos.lastIndexOf(",") == userInfos.length() - 1)
            {
                userInfos = userInfos.substring(0, userInfos.length() - 1);
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("openId&in", userInfos);
                conditionMap.put("AId", Aid);
                List<LfWeiUserInfo> userInfoList = empDao.findListBySymbolsCondition(LfWeiUserInfo.class, conditionMap, null);
                if(userInfoList != null && userInfoList.size() > 0)
                {
                    for (LfWeiUserInfo userInfo : userInfoList)
                    {
                    	if(queueMap.get(userInfo.getOpenId()) != null)
                    	{
	                        JSONObject userJson = new JSONObject();
	                        
	                        userJson.put("name", userInfo.getNickName());
	                        userJson.put("openid", userInfo.getOpenId());
	                        userJson.put("servernum", queueMap.get(userInfo.getOpenId()));
	                        
	                        curUserArray.add(userJson);
                    	}else
                    	{
                    		setCustSerObj(customeId, userInfo.getOpenId(), "1", false);
                    	}
                    }
                }
                userInfos = custWeixObjectMap.get(customeId);
                userInfos = userInfos.substring(0, userInfos.length() - 1);
            }else
            {
                userInfos = null;
            }
            
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            
            Calendar yesterday = Calendar.getInstance();
            yesterday.set(Calendar.HOUR_OF_DAY, 0);
            yesterday.set(Calendar.MINUTE, 0);
            yesterday.set(Calendar.SECOND, 0);
            yesterday.add(Calendar.DAY_OF_MONTH, -1);
            
            // 获取接入该用户的客户微信号集合
            List<DynaBean> beans = new CustomChatDao().getChatUserInfos(userInfos,Aid,customeId,null,"1");
            if(beans != null && beans.size() > 0)
            {
                for(DynaBean bean : beans)
                {
                    JSONObject userJson = new JSONObject();
                    userJson.put("name", bean.get("nick_name"));
                    userJson.put("openid", bean.get("from_user"));
                    userJson.put("servernum", bean.get("ser_num"));
                    
                    String time = bean.get("create_time").toString();
                    Calendar ctime =  Calendar.getInstance();
                    ctime.setTime(StringUtils.parsToDate(time));
                    if(ctime.after(today))
                    {
                        todayArray.add(userJson);
                    }
                    else if(ctime.after(yesterday))
                    {
                        yesterArray.add(userJson);
                    }else
                    {
                        agoArray.add(userJson);
                    }
                }
            }
            resultObject.put("curs", curUserArray);
            resultObject.put("today", todayArray);
            resultObject.put("yest", yesterArray);
            resultObject.put("ago", agoArray);
            return resultObject.toString();
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "获取客服连接的手机用户列表失败！");
            return null;
        }
    }
    
    /**
     * 插入聊天消息库
     * 
     * @description 客服聊天消息插入数据库
     * @param AId
     *        公众账号id
     * @param fromUser
     *        发送者
     * @param toUser
     *        接收者
     * @param hisOrReal
     *        1-实时，2-历史,3-实时and历史
     * @param serverNum
     *        服务号
     * @param message
     *        内容
     * @param sendTime
     *        发送时间
     * @param msgType
     *        消息类型text-文本，voice-声音，image-单图
     * @param pushType
     *        推送类型1-手机to客服，2-客服to手机，3-客服to客服，4群组
     * @return true-成功,false-失败
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-19 下午02:55:29
     */
    public static Long insertChatMsg(Long AId, String fromUser, String toUser, int hisOrReal, String serverNum, String message, Timestamp sendTime, String msgType, int pushType)
    {
        try
        {
            switch (hisOrReal)
            {
                case 1:
                    //OtOnlMsg onlMsg = new OtOnlMsg(AId, fromUser, toUser, serverNum, message, sendTime, msgType, pushType);
                    //return empDao.saveObjectReturnID(onlMsg);
                    return 0L;
                case 2:
                    LfOnlMsgHis onlMsgHis = new LfOnlMsgHis(AId, fromUser, toUser, serverNum, message, sendTime, msgType, pushType);
                    return empDao.saveObjectReturnID(onlMsgHis);
                case 3:
                    LfOnlMsgHis onlMsgHis1 = new LfOnlMsgHis(AId, fromUser, toUser, serverNum, message, sendTime, msgType, pushType);
                    Long msgid = empDao.saveObjectReturnID(onlMsgHis1);
                    if(msgid == null || msgid.intValue() == 0)
                    {
                        return 0L;
                    }
                    LfOnlMsg onlMsg1 = new LfOnlMsg(msgid,AId, fromUser, toUser, serverNum, message, sendTime, msgType, pushType);
                    if(empDao.save(onlMsg1))
                    {
                        return msgid;
                    }
                default:
                    return 0l;
            }
        }
        catch (Exception e)
        {
        	EmpExecutionContext.error(e,"客服消息入库异常！");
            return 0l;
        }
    }

    /**
     * 比较器，用于两个json中的count值
     * 
     * @description 排序json数组时调用
     * @project OTT
     * @company ShenZhen Montnets Technology CO.,LTD.
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-18 下午02:05:10
     */
    static class JsonComparator implements Comparator<JSONObject>
    {
        @Override
        public int compare(JSONObject lbs1, JSONObject lbs2)
        {
            if(((Integer) lbs1.get("count")).compareTo((Integer) lbs2.get("count")) > -1)
            {
                return 1;
            }else
            {
                return -1;
            }
        }
    }

    /**
     * 客服查询历史消息记录
     * 
     * @description
     * @param ownerId
     *        客服自己的ID
     * @param customId
     *        和客服聊天人的ID
     * @param conditionMap
     *        查询条件
     * @param pageInfo
     *        分页对象
     * @return
     * @throws Exception
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-20 上午10:40:21
     */
    public List<LfOnlMsgHis> findHistoryMessage(String ownerId, String customId, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception
    {
        //return new CustomerDao().findHistoryMessage(ownerId, customId, conditionMap, pageInfo);
        return null;
    }

    /**
     * 转接客服
     * 
     * @description
     * @param serverNum
     *        服务号
     * @param customeId
     *        转接对象
     * @return
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-23 上午09:46:51
     */
    public String turnCustome(String serverNum, String fromCust, String customeId, String zjmsg,Long aId,String name)
    {
        // 检测服务号是否存在客户接入
        if(queueMap.get(serverNum) == null || !fromCust.equals(queueMap.get(serverNum)))
        {
            return "customout";
        }
        if(zjMap.get(serverNum) != null)
        {
            return "turning";
        }
        zjMap.put(serverNum, customeId);
        return setMsgJson(customeId, fromCust+"to"+customeId, fromCust, "zjkf",zjmsg, "3", name,aId);
    }
    
    /**
     * 处理客服转接消息
     * @description  处理客服转接请求  
     * @param serverNum 服务号
     * @param fromCust  转接发起人
     * @param customeId 转接对象
     * @param aId       所属公众号Id
     * @param openId    客户微信号
     * @param isagree   1-接受，2-拒绝
     * @return       	处理结果		 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-15 上午10:02:08
     */
    @SuppressWarnings("unchecked")
    public String agreeConver(String serverNum, String fromCust, String customeId,Long aId,String openId,String isagree)
    {
    	// 检测服务号是否存在客户接入
        if(queueMap.get(serverNum) == null || !fromCust.equals(queueMap.get(serverNum)))
        {
            return "customout";
        }
        // 服务类型。1-微信，6-app
    	String sertype = ((Integer) serverMap.get(serverNum).get("sertype")).toString();
    	if("6".equals(sertype))
    	{
    		aId = 0l;
    	}
        if(isagree.equals("0"))
        {
            setMsgJson(fromCust, serverNum, openId, "tips", " 转接请求被拒绝！", sertype, null,aId);
            zjMap.remove(serverNum);
            return "cancel";
        }
        if(zjMap.get(serverNum) == null)
        {
            return "noconver";
        }
        zjMap.remove(serverNum);
        queueMap.put(serverNum, customeId);
       
        // 转接成功推送消息
        setMsgJson(customeId, serverNum, openId, "tips", " 客服转接成功！现在可以与该客户进行通讯！", sertype, null,aId,1);
        // 向原客服人员推送提示消息
        setMsgJson(fromCust, serverNum, openId, "tips", " 客服转接成功！请不要再对此下发信息！", sertype, null,aId,2);
        // 清除原客服人员的关联
        updateServerCount(aId,fromCust,false);
        setCustSerObj(fromCust, openId,sertype,false);
        // 修改转接客服人员的接入情况
        updateServerCount(aId,customeId,true);
        setCustSerObj(customeId, openId,sertype,true);
        // 跟新服务对象中的聊天人信息
        serverMap.get(serverNum).put("customeid", customeId);
        JSONArray jsonArr = new JSONArray();
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
        
        orderbyMap.put("MId", "ASC");
        
        conditionMap.put("serverNum", serverNum);
        conditionMap.put("msgType&not in", "zjkf,tips");
        if("1".equals(sertype))
    	{
        	conditionMap.put("pushType&in", "1,2");
    	}else
    	{
    		conditionMap.put("pushType&in", "6,7");
    	}
        
        List<LfOnlMsgHis> msgHisList;
        try
        {
            msgHisList = empDao.findListBySymbolsCondition(LfOnlMsgHis.class, conditionMap, orderbyMap);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"获取历史消息失败！");
            return "";
        }
        if(msgHisList == null || msgHisList.size() == 0)
        {
            return "";
        }
        for (LfOnlMsgHis msgHis : msgHisList)
        {
            JSONObject jsonOb = new JSONObject();
            jsonOb.put("fromuser", msgHis.getFromUser());
            jsonOb.put("message", msgHis.getMessage());
            jsonOb.put("time", StringUtils.timeFormat(msgHis.getSendTime()));
            jsonOb.put("msgid", msgHis.getMId());
            jsonOb.put("msgtype", msgHis.getMsgType());
            jsonOb.put("servernum", msgHis.getServerNum());
            // 发送者名称，群组发送时为群组名称，客服对客服发送时为客服人员名称，手机对客服时为手机用户昵称
            jsonOb.put("name", getPushName(msgHis.getPushType().toString(), msgHis.getFromUser(), ""));
            jsonOb.put("pushtype", msgHis.getPushType().toString());

            jsonArr.add(jsonOb);
        }
        return jsonArr.toString();
    }
    
    /**
     * 取消转接
     * @description    
     * @param serverNum
     * @param fromCust
     * @param customeId
     * @param aId
     * @param name
     * @return 服务器时间       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-15 下午07:18:26
     */
    public String cancelConver(String serverNum, String fromCust, String customeId,Long aId,String name)
    {
        if(zjMap.get(serverNum)==null)
        {
            return "noconver";
        }
        zjMap.remove(serverNum);
        return setMsgJson(customeId, serverNum, fromCust, "tips","该转接请求已被取消！", "3", name,aId);
    }
    
    /**
     * 读取未读信息
     * 
     * @description
     * @param customeId
     * @return
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-24 上午09:11:02
     */
    @SuppressWarnings("unchecked")
    public void getUnReadMsg(String customeId)
    {
        JSONArray jsonArr = new JSONArray();
        try
        {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
            conditionMap.put("toUser", customeId);
            conditionMap.put("pushType&in", "1,3,6");
            orderbyMap.put("MId", "ASC");
            List<LfOnlMsg> msgHisList = empDao.findListBySymbolsCondition(LfOnlMsg.class, conditionMap, orderbyMap);
            if(msgHisList == null || msgHisList.size() == 0)
            {
                return;
            }
            for (LfOnlMsg msgHis : msgHisList)
            {
                JSONObject jsonOb = new JSONObject();
                jsonOb.put("fromuser", msgHis.getFromUser());
                jsonOb.put("message", msgHis.getMessage());
                jsonOb.put("time", StringUtils.timeFormat(msgHis.getSendTime()));
                jsonOb.put("msgtype", msgHis.getMsgType());
                jsonOb.put("servernum", msgHis.getServerNum());
                // 发送者名称，群组发送时为群组名称，客服对客服发送时为客服人员名称，手机对客服时为手机用户昵称
                jsonOb.put("name", getPushName(msgHis.getPushType().toString(), msgHis.getFromUser(), ""));
                jsonOb.put("pushtype", msgHis.getPushType().toString());
                jsonOb.put("msgid", msgHis.getMId());
                jsonArr.add(jsonOb);
            }
            synchronized (msgMap)
            {
            	JSONArray msgJson = msgMap.get(customeId);
            	if(msgJson != null && msgJson.size() > 0)
            	{
            		jsonArr.addAll(msgJson);
            		msgMap.put(customeId, jsonArr);
            	}else
            	{
            		msgMap.put(customeId, jsonArr);
            	}
            }
            empDao.delete(LfOnlMsg.class, conditionMap);
            // return jsonArr;
        }
        catch (Exception e)
        {
            // return jsonArr;
        }
    }

    /**
     * 检查是否存在超时的客服请求
     * 
     * @description
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-24 下午04:11:44
     */
    @SuppressWarnings("unchecked")
    public void checkServerTime()
    {
        try
		{
			Map<String, JSONObject> newServerMap = new HashMap<String, JSONObject>();
			if(null != serverMap)
			{
			    newServerMap.putAll(serverMap);
			}
			Iterator<String> iter = newServerMap.keySet().iterator();
			long nowTimeLong = System.currentTimeMillis();
			while(iter.hasNext())
			{
			    String key = iter.next();
			    JSONObject serverJson = newServerMap.get(key);
			    long time = (Long) serverJson.get("time");
			    int state = (Integer) serverJson.get("state");
			    int sertype = (Integer) serverJson.get("sertype");
			    // 如果处于客服服务状态中
			    if(state == 1 && nowTimeLong - time > serverOverTime)
			    {
			        // 下发是否要退出客服的提醒。
			        if(sertype == 1)
			        {
			        	String msg = "检测到您许久未进行客服提问，是否退出客服？回复[" + logout + "]退出客服，回复其他内容则继续为您提供服务！";
			        	doSendWeix((String) serverJson.get("fromuser"), msg, "text", (String) serverJson.get("openid"), (String) serverJson.get("customeid"));
			        	 msg = "检测到客户许久未进行客服提问，发送是否退出客服的询问操作。";
			             setMsgJsonBefore((String) serverJson.get("customeid"), key, (String) serverJson.get("fromuser"), 
			             		"tips", msg, String.valueOf(sertype), "",serverJson.get("aid")==null?null:(Long)serverJson.get("aid"),0);
			             serverMap.get(key).put("time", nowTimeLong);
			             serverMap.get(key).put("state", 2);
			         continue;
			        }else if(sertype == 6)
			        {
			        	String msg ="检测到客户许久未进行客服提问，已为您断开与该客户的通讯。";
			        	outOfCustome((Long) serverJson.get("aid"), (String) serverJson.get("customeid"), (String) serverJson.get("fromuser"), (String) serverJson.get("openid"), false,sertype);
			        	setMsgJsonBefore((String) serverJson.get("customeid"), key, (String) serverJson.get("fromuser"), 
			             		"tips", msg, String.valueOf(sertype), "",serverJson.get("aid")==null?null:(Long)serverJson.get("aid"),2);
			        }
			    }
			    // TODO 以下疑问确定后再进行逻辑修改，可合并if
			    // 如果处于等候确认是否退出服务状态中
			    if(state == 2 && nowTimeLong - time > evaluateOverTime)
			    {
			        // TODO 是否推送客服评价内容或直接提示退出？目前是直接退出
			        outOfCustome((Long) serverJson.get("aid"), (String) serverJson.get("customeid"), (String) serverJson.get("fromuser"), (String) serverJson.get("openid"), false,sertype);
			        continue;
			    }
			    // 如果处于客服评价等状态中
			    if(state == 3 && nowTimeLong - time > evaluateOverTime)
			    {
			        // TODO 是否推送退出消息，目前是推送
			        outOfCustome((Long) serverJson.get("aid"), (String) serverJson.get("customeid"), (String) serverJson.get("fromuser"), (String) serverJson.get("openid"), false,sertype);
			        continue;
			    }
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "检查是否存在超时的客服请求异常。");
		}
    }

    /**
     * 推送客服评价消息
     * @description    
     * @param customId
     * @param fromUser
     * @param openId
     * @param serverNum       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-4 下午04:52:12
     */
    @SuppressWarnings("unchecked")
    private static void pushEvaluate(String customId, String fromUser, String openId, String serverNum,int sertype)
    {
        // 下发是否要退出客服的提醒。
        String msg = "很高兴为您服务。回复\r\n[1]不满意，\r\n[2]不怎么满意，\r\n[3]还算满意，\r\n[4]满意，\r\n[5]十分满意。\r\n回复其他或3分钟内不回复则退出客服。";
        if(sertype == 1)
        {
        	doSendWeix(fromUser, msg, "tips", openId, customId);
        }else if(sertype == 6)
        {
        	// openId传的值为ecode；
        	doSendApp(fromUser, msg, "test", customId, openId);
        }
        serverMap.get(serverNum).put("time", System.currentTimeMillis());
        serverMap.get(serverNum).put("state", 3);
    }

    /**
     * 保存评价信息
     * @description    
     * @param AId
     * @param customId
     * @param fromUser  
     * @param openId    客户微信号
     * @param serverNum 会话好
     * @param msg       上行内容      			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-3 下午02:28:35
     */
    private static void saveEvaluate(String AId, String customId, String fromUser, String openId, String serverNum, String msg,int sertype)
    {
        String score = "0";
        String evaluate = msg;
        // 客户上行的消息是否分数
        boolean isScore = false;
        // 判断为数字正则表达式
        Pattern pattern = Pattern.compile("[0-9]*");
        if(pattern.matcher(msg).matches())
        {
            int num = Integer.parseInt(msg);
            if(num > 0 && num < 6)
            {
                score = msg;
                isScore = true;
            }
        }
        
        //JSONObject serJosn = serverJsonMap.
        // 更新评价信息
        LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("serNum", serverNum);
        objectMap.put("evaluate", evaluate);
        objectMap.put("score", score);

        try
        {
            empDao.update(LfOnlServer.class, objectMap, conditionMap);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "保存客户评价信息失败！servernum=" + serverNum);
        }
        // 处理退出事项
        outOfCustome(AId==null?null:Long.valueOf(AId), customId, fromUser, openId, isScore,sertype);
    }

    /**
     * 更改客服会话激活时间
     * @description    每次有新的会话记录则激活会话
     * @param serverNum 会话号       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-3 下午02:27:45
     */
    @SuppressWarnings("unchecked")
    private static void updateServerTime(String serverNum)
    {
        serverMap.get(serverNum).put("time", System.currentTimeMillis());
        serverMap.get(serverNum).put("state", 1);
    }
    
    /**
     * 更新服务号信息
     * @description 客户退出客服时，更新服务号信息    
     * @param serverNum 服务号
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-13 下午02:06:42
     */
    private static boolean updateServerEndTime(String serverNum)
    {
    	try
        {
	        if(serverMap.get(serverNum) == null)
	        {
	        	EmpExecutionContext.error("客户退出客服时，更新服务号信息失败，从缓存中获取服务号信息对象为null。");
	            return false;
	        }
	        JSONObject serJson =  serverMap.get(serverNum);
	        
	        long beginTime = (Long)( serJson.get("begintime")!=null?serJson.get("begintime"):System.currentTimeMillis());
	        long durationLong = System.currentTimeMillis() - beginTime;
	        int duration = (int) durationLong / (60*1000);
	        serverMap.remove(serverNum);
	        
	        LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
	        LinkedHashMap<String,String> objectMap = new LinkedHashMap<String, String>();
	        conditionMap.put("serNum", serverNum);
	        objectMap.put("endTime", StringUtils.timeFormat(new Date()));
	        objectMap.put("duration", String.valueOf(duration));
        
        
            empDao.update(LfOnlServer.class, objectMap, conditionMap);
            return true;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error("客户退出客服时，更新服务号信息异常。serverNum="+serverNum);
            return false;
        }
    }
    
    /**
     * 加载历史消息
     * @description    
     * @param count 加载数量
     * @param serverNum 服务号
     * @param anotherServerNum 当查找客服与客服对话时使用
     * @param msgId 消息id
     * @return 历史聊天消息的集合      			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-17 上午11:11:22
     */
    public String loadMsgHis(String count,String serverNum,String anotherServerNum,String msgId,String pushType)
    {
        CustomChatDao chatDao = new CustomChatDao();
        JSONArray jsonArr = new JSONArray();
        try
        {
            List<LfOnlMsgHis> msgHisList = chatDao.loadMsgHis(count,serverNum, anotherServerNum, msgId,pushType);
            
            if(msgHisList == null || msgHisList.size() == 0)
            {
                return "";
            }
            for (LfOnlMsgHis msgHis : msgHisList)
            {
                JSONObject jsonOb = new JSONObject();
                jsonOb.put("fromuser", msgHis.getFromUser());
                jsonOb.put("message", msgHis.getMessage());
                jsonOb.put("time", StringUtils.timeFormat(msgHis.getSendTime()));
                jsonOb.put("msgtype", msgHis.getMsgType());
                jsonOb.put("servernum", msgHis.getServerNum());
                // 发送者名称，群组发送时为群组名称，客服对客服发送时为客服人员名称，手机对客服时为手机用户昵称
                jsonOb.put("name", getPushName(msgHis.getPushType().toString(), msgHis.getFromUser(), ""));
                jsonOb.put("msgid", msgHis.getMId());
                jsonOb.put("pushtype", msgHis.getPushType().toString());

                jsonArr.add(jsonOb);
            }
            return jsonArr.toString();
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"打开聊天窗口时查询历史聊天消息失败！");
            return "";
        }
    }
    
    /**
     * 设置客服人员的服务对象集合
     * @description    
     * @param customeId 客服Id
     * @param fromUser  客户微信号       			 
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-18 上午11:26:53
     */
    private static void  setCustSerObj(String customeId,String fromUser,String sertype,boolean isAdd)
    {
    	if("1".equals(sertype))
    	{
	        String serObj = custWeixObjectMap.get(customeId);
	        if(serObj != null)
	        {
	        	if(isAdd)
	        	{
		            if(serObj.indexOf(fromUser + ",") == -1)
		            {
		                custWeixObjectMap.put(customeId, serObj + fromUser + ",");
		            }
	        	}else
	        	{
	        		custWeixObjectMap.put(customeId,serObj.replace(fromUser+",", ""));
	        	}
	        }
	        else if(isAdd)
	        {
	            custWeixObjectMap.put(customeId, fromUser + ",");
	        }
    	}else if("6".equals(sertype))
    	{
    		String serObj = custAppObecjtMap.get(customeId);
	        if(serObj != null)
	        {
	        	if(isAdd)
	        	{
		            if(serObj.indexOf(fromUser + ",") == -1)
		            {
		            	custAppObecjtMap.put(customeId, serObj + fromUser + ",");
		            }
	            }else
	        	{
	            	custAppObecjtMap.put(customeId,serObj.replace(fromUser+",", ""));
	        	}
	        }
	        else if(isAdd)
	        {
	        	custAppObecjtMap.put(customeId, fromUser + ",");
	        }
    	}
    }
    
    /**
     * 获取客服人员绑定的公众号
     * @param userId
     * @return
     */
    public String getAidByUserid(String userId) 
    {
    	CustomChatDao chatDao = new CustomChatDao();
        try {
			return String.valueOf(chatDao.getAidByUserid(userId));
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取操作员所属公众号失败！");
			return "0";
		}
    }
    
    /**
     * 获取当前的客服人员
     * 
     * @description
     * @parame stateReq 是否过滤状态，1-过滤，0-不过滤
     * @return
     * @throws Exception
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-1-14 下午05:16:23
     */
    public String getAccountUserJson(String userId, String AId,int stateReq) throws Exception
    {

    	CustomChatDao chatDao = new CustomChatDao();
        // 客服列表
        List<DynaBean> userList = chatDao.getCustomeList(userId,AId,stateReq);
        // 转化客服列表为json格式数据
        String userJson = getUserJson(userList, AId, stateReq);
        return userJson;
    }
    
    /**
     * 拼装App发送json
     * @param corpCode
     * @param fromUser
     * @param toUser
     * @param msgType
     * @param msg
     * @param name
     * @return json格式字符串
     */
    private static String getSendAppJson(String corpCode,String fromUser,String toUser,String msgType,String msg,String name)
    {
    	try
		{
			JSONObject appJson = new JSONObject();
			appJson.put("ECODE", corpCode);
			appJson.put("FROM", fromUser);
			appJson.put("TO", toUser);
			
			if(name == null || name.length() == 0){
				appJson.put("FROMNAME", fromUser);
				EmpExecutionContext.info("在线坐席获取App发送json字符串，FROMNAME为空。fromUser="+fromUser);
			}
			else{
				appJson.put("FROMNAME", name);
			}
			
			JSONObject styleJson = new JSONObject();
			if("text".equals(msgType))
			{
				appJson.put("MSGTYPE", "0");
				JSONObject contentJson = new JSONObject();
				contentJson.put("content", msg);
				styleJson.put("PMessageStyle1", contentJson);
			}
			else if("image".equals(msgType))
			{
				appJson.put("MSGTYPE", "1");
				JSONObject picJson = new JSONObject();
				picJson.put("pic", msg);
				styleJson.put("PMessageStyle2", picJson);
			}
			appJson.put("pMessageStyles", styleJson);
			
			return appJson.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "在线坐席获取App发送json字符串异常。");
			return null;
		}
    }
    
    /**
     * 获取推送的app消息
     * @param json
     */
    public static void getAppMsg(JSONObject json)
    {
    	try
		{
			saveAppLog(json.toString());
			String ecode = json.get("ECODE")==null?"":json.get("ECODE").toString();
			String fromUser = json.get("FROM")==null?"":json.get("FROM").toString();
			//昵称
			String fromName = json.get("FROMNAME")==null?"":json.get("FROMNAME").toString();
			String msgType = json.get("MSGTYPE")==null?"":json.get("MSGTYPE").toString();
			// 0在线，1离线消息
			String OUTLINEMSG = json.get("OUTLINEMSG")==null?"":json.get("OUTLINEMSG").toString();
			
			//离线消息
			if(!"0".equals(OUTLINEMSG)){
				
				//拿内存中的值
				String memFromName = nameMap.get(fromUser);
				
				//内存中没值
				if(memFromName == null){
					//从数据库中取值，并填到内存中
					fromName = getPushName("6", fromUser, null);
				}
				//内存有值，则判断内存中的昵称跟信息中的昵称是不是一样，一样则直接用，不一样就删除内存的，然后从数据库中重新获取
				else if(!memFromName.equals(fromName)){
					EmpExecutionContext.info("在线客服获取内存昵称和信息中昵称不一致，重新更新内存昵称。json="+json);
					nameMap.put(fromUser, null);
					//从数据库中取值，并填到内存中
					fromName = getPushName("6", fromUser, null);
				}
			}
			
			//昵称为空，则从内存中取，内存没，则从数据库中取
			if(fromName == null || fromName.length() == 0){
				fromName = getPushName("6", fromUser, null);
			}
			
			if(fromName == null || fromName.length() == 0){
				EmpExecutionContext.error("在线客服获取不到昵称。json="+json);
			}
			
			//加到内存中
			nameMap.put(fromUser, fromName);
			
			/*// 如果是离线消息，则不采用新推送的名称
			if(OUTLINEMSG != null && !"1".equals(OUTLINEMSG))
			{
				if(fromName != null)
				{
					nameMap.put(fromUser, fromName);
				}
			}*/
			JSONObject styleJson = (JSONObject) json.get("pMessageStyles") ;
			if(fromUser == null)
			{
				EmpExecutionContext.error("获取app推送的消息时，app身份标识为空！");
				return;
			}
			
			if(queueMap.get(fromUser) == null)
			{
				if("noCustome".equals(callAppCustome(fromUser,ecode)))
				{
					EmpExecutionContext.error("app客户请求获取不到客服人员!");
					return;
				}
			}
			appEcodeMap.put(fromUser, ecode);
			
			String msg = "";
			if(styleJson.get("PMessageStyle1") != null)
			{
				msg = (String) ((JSONObject)styleJson.get("PMessageStyle1")).get("content");
				msgType = "text";
			}
			else if(styleJson.get("PMessageStyle2") != null)
			{
				msg = (String) ((JSONObject)styleJson.get("PMessageStyle2")).get("pic");
				msg = downLoadFile(msg,"image");
				if(msg == null)
				{
					EmpExecutionContext.error("接收app图片时从文件服务器下载文件失败！json="+json==null?"":json.toString());
					return;
				}
				msgType = "image";
			}else if(styleJson.get("PMessageStyle3") != null)
			{
				msg = (String) ((JSONObject)styleJson.get("PMessageStyle3")).get("url");
				msg = downLoadFile(msg,"voice");
				if(msg == null)
				{
					EmpExecutionContext.error("接收app音频时从文件服务器下载文件失败！json="+json==null?"":json.toString());
					return;
				}
				
				msgType = "voice";
				JSONObject voiceJson = new JSONObject();
				StringBuffer ssb = new StringBuffer(msg);
			    msg = ssb.replace(msg.lastIndexOf("."),msg.length(),".mp3").toString();
			    voiceJson.put("path", msg);
			    // 音频持续时间
			    voiceJson.put("second", ((JSONObject)styleJson.get("PMessageStyle3")).get("time"));
			    // 音频文字信息
			    voiceJson.put("msg", "");
			    msg = voiceJson.toString();
			}
			else
			{
				EmpExecutionContext.error("无法识别的消息类型！"+json.toString());
				return;
			}
			 // 获取服务号
			String serverNum = queueMap.get(fromUser);
			// 获取服务号对应的客服人员
			String toCusUser = queueMap.get(serverNum);
			// 更新服务号的激活时间，防止超时
			updateServerTime(serverNum);
			
			setMsgJsonBefore(toCusUser, serverNum, fromUser, msgType, msg, "6", fromName, 0L,1);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取推送的app消息异常。json="+json);
		}

    }
    
    /**
     * 呼叫app客服
     * @param fromUser
     * @param ecode
     * @return
     */
    @SuppressWarnings("unchecked")
	public static String callAppCustome(String fromUser,String ecode)
    {
    	
        // 当系统自动分配时则返回单一对象
        String userid = getAppCustomeUser(fromUser);
        
        if(userid != null)
        {
            String serverNum = getServerNum(fromUser,"APP");// 生成服务号
            String customeId = userid;

            // 插入客服会话表
            LfOnlServer onlServer = new LfOnlServer();
            onlServer.setCustomeId(Long.valueOf(customeId));
            onlServer.setAId(0L);
            onlServer.setFromUser(fromUser);
            onlServer.setSerNum(serverNum);
            onlServer.setSerType(6);

            try
            {
                empDao.save(onlServer);
                
                queueMap.put(serverNum, customeId);
                queueMap.put(fromUser, serverNum);

                JSONObject serverJson = new JSONObject();
                
                serverJson.put("customeid", customeId);
                serverJson.put("begintime", System.currentTimeMillis());
                serverJson.put("time", System.currentTimeMillis());
                serverJson.put("state", 1);
                serverJson.put("fromuser", fromUser);
                serverJson.put("aid", 0L);
                serverJson.put("sertype", 6);

                serverMap.put(serverNum, serverJson);
                setCustSerObj(customeId, fromUser, "6", true);
                String msg = "";
                
                String name = getPushName("6", fromUser, null);
                msg = "APP用户[" + name + "]已接入，现在可以进行会话！";
                EmpExecutionContext.debug("APP用户【"+fromUser+"】接入信息："+serverJson.toString());
                return setMsgJson(customeId, serverNum, fromUser, "tips", msg, "6", name,0L,1);
                
                //msg = "您好，请问有什么问题可以为您解答？";
                //return doSendApp(fromUser, msg, "text", userid, corp_code);
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "APP接入呼叫客服异常！");
            }
        }
        // 返回没有合适的客服人员推送
        String msg = "您好！您的信息我们已收到，稍后为您提供服务，谢谢！";
        doSendApp(fromUser, msg, "text", "0", ecode);
        return "noCustome";
    }
    
    /**
     * 向APP下发消息
     * @description 通过微信接口给手机APP用户下发消息
     * @param toUser 发给谁，app身份标识
     * @param msg 内容，图片消息时为本地的文件路径
     * @param msgType 消息类型。text,img
     * @param fromUser 发送者
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-06-19 下午01:38:27
     */
    public static String doSendApp(String toUser, String msg, String msgType, String fromUser,String ecode)
    {
    	if(!"0".equals(fromUser) && (queueMap.get(toUser) == null || !fromUser.equals(queueMap.get(queueMap.get(toUser)))))
        {
    		setMsgJson(fromUser, "", toUser, "tips", "客户已退出本次会话，无法进行信息下发！", "6", "",0L);
    		return  "error";
        }
    	if(ecode == null)
    	{
    		ecode = getEcode(toUser);
    	}
    	Timestamp sendTime = new Timestamp(System.currentTimeMillis());
    	
    	// 如果是调试模式，则不调用接口
    	if(CustomTestMode.getTestMode())
    	{
    		if(insertChatMsg(0L, fromUser, toUser, 2, queueMap.get(toUser), msg, sendTime, msgType, 7)- 0 > 0)
            {
    			return CustomTestMode.sendmsg(msg, fromUser, StringUtils.timeFormat(sendTime),toUser,"APP");
            }
    	}
    	// 用于保存的消息内容
    	String insertMsg = msg;
        // 写入历史消息库
        if("image".equals(msgType))
        {
        	WgMwFileBiz fileBiz = new WgMwFileBiz();
        	String uploadResult = fileBiz.uploadToMwFileSer(new TxtFileUtil().getWebRoot()+msg, "30" , ecode);
        	//根据返回的URL地址长度判断上传是否成功(错误返回码最长两位)
        	if(!(uploadResult != null && uploadResult.length() > 10))
        	{
        		EmpExecutionContext.error("向APP下发消息时上传图片到文件服务器失败！resultcode="+(uploadResult==null?"null":uploadResult));
        		return "error";
        	}
        	msg = uploadResult;
        }
        IInterfaceBuss commuteBiz = new HandleMsgBiz();
        String name = getPushName("7", fromUser, null);
        String sendParams = getSendAppJson(ecode, fromUser, toUser, msgType, msg,name);
        if(insertChatMsg(0L, fromUser, toUser, 2, queueMap.get(toUser), insertMsg, sendTime, msgType, 7)- 0 > 0)
        {
        	int sendResult = commuteBiz.SendAppPersionMsg(sendParams);
        	if(sendResult != 1)
        	{
        		EmpExecutionContext.error("向APP下发消息时下发失败！resultcode="+sendResult+"sendParams="+sendParams);
        		return "error";
        	}
        }else
        {
        	EmpExecutionContext.error("向APP下发消息时入库失败！sendParams="+sendParams);
        	return "error";
        }
        return StringUtils.timeFormat(sendTime);
    }
    
    /**
     * 调用文件服务器下载文件
     * @param fileUrl
     * @param fileType
     * @return
     */
    private static String downLoadFile(String fileUrl,String fileType)
    {
    	WgMwFileBiz mwFileBiz = new WgMwFileBiz();
    	String[] path = new WeixBiz().getWeixResourceUrl(fileType);
    	if( mwFileBiz.downloadFromMwFileSer(path[0], fileUrl))
    	{
    		return path[1];
    	}else
    	{
    		return null;
    	}
    }
    
    /**
     * 获取app客户的企业编码
     * @param fromUser
     * @return
     */
    private static String getEcode(String fromUser) {
		if (appEcodeMap.get(fromUser) == null) 
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        	conditionMap.put("appcode", fromUser);
            List<LfAppMwClient> user;
			try {
				user = empDao.findListByCondition(LfAppMwClient.class, conditionMap, null);
			} catch (Exception e) {
				return null;
			}
            if(user != null && user.size() > 0)
            {
            	appEcodeMap.put(fromUser, user.get(0).getEcode());
            }
		}
		return appEcodeMap.get(fromUser);
	}
    
    /**
     * 获取内存消息
     * @return
     */
    public static Map<String, JSONArray> getMsgMap()
    {
    	return msgMap;
    }
    
    /**
     * 去除微信队列
     * @param aid
     * @param userid
     */
    public static void removeWeiCountMap(String aid,String userid)
    {
    	if(CustomChatBiz.weixCustMap.get(aid) != null)
    	{
    		CustomChatBiz.weixCustMap.get(aid).remove(userid);
    	}
    }
    
    /**
     * 获取服务关系内存数据
     * @return
     */
    public static Map<String, String> getQueueMap()
    {
    	return queueMap;
    }
    
    /**
     * 获取接入服务消息
     * @return
     */
    public static Map<String, JSONObject> getServerMap()
    {
    	return serverMap;
    }
    
    /**
     * 获取转接关系内存消息数据
     * @return
     */
    public static Map<String, String> getZjMap()
    {
    	return zjMap;
    }
    
    /**
     * 获取客服连接的APP用户列表
     * @description
     * @param customeId
     *        客服Id
     * @return 手机用户信息Json格式
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-18 下午02:06:49
     */
    @SuppressWarnings("unchecked")
    public String getAppUserInfos(String customeId)
    {
    	// 从静态变量中读取接入信息
        String userInfos = custAppObecjtMap.get(customeId);
        String userInfosOther = userInfos;
        JSONObject resultObject = new JSONObject();
        JSONArray curUserArray = new JSONArray();
        JSONArray todayArray = new JSONArray();
        JSONArray yesterArray = new JSONArray();
        JSONArray agoArray = new JSONArray();
        JSONArray neverArray = new JSONArray();
        // 当天时间点
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        // 昨天时间点
        Calendar yesterday = Calendar.getInstance();
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
    	try {
			List<LfAppMwClient> clientList = empDao.findListByCondition(LfAppMwClient.class, null,null);
			if(clientList == null)
			{
				return resultObject.toString();
			}
			List<DynaBean> beans = new CustomChatDao().getAppSerTime(customeId);
			Map<String,String> timeMap = new HashMap<String, String>();
			
			if(beans != null && beans.size() > 0)
            {
                for(DynaBean bean : beans)
                {
                	if(bean.get("from_user") != null) {
                        timeMap.put(bean.get("from_user").toString(), bean.get("create_time").toString());
                    }
                }
            }
			for(LfAppMwClient client : clientList)
			{
				String appcode = client.getAppcode();
				JSONObject userJson = new JSONObject();
                // 昵称
                userJson.put("name", client.getNickname());
                // appid
                userJson.put("appcode", appcode);
                // 企业号
                userJson.put("ecode", client.getEcode());
                // 如果属于当前接入
                if(userInfos != null && userInfos.indexOf(appcode) > -1)
        		{
                	userInfosOther = userInfosOther.replace(appcode+",", ",");
                	// 在当前接入关系内存中判断是否有效接入
                	if(queueMap.get(client.getAppcode()) != null)
                	{
	                	userJson.put("servernum", queueMap.get(appcode));
	                	curUserArray.add(userJson);
	                	continue;
                	}else
                	{
                		// 非有效接入则修改接入对象
                		setCustSerObj(customeId, client.getAppcode(), "6", false);
                	}
        		}
               
                userJson.put("servernum", "");
                String time = timeMap.get(appcode);
                if(time == null)
                {
                	neverArray.add(userJson);
                	continue;
                }
                Calendar ctime =  Calendar.getInstance();
                ctime.setTime(StringUtils.parsToDate(time));
                if(ctime.after(today))
                {
                    todayArray.add(userJson);
                }
                else if(ctime.after(yesterday))
                {
                    yesterArray.add(userJson);
                }else
                {
                    agoArray.add(userJson);
                }
			}
			
			if(userInfosOther != null && userInfosOther.length()>1)
			{
				String[] userArray = userInfosOther.split(",");
				for(int i=0;i<userArray.length;i++)
				{
					String appcode = userArray[i];
					if(!"".equals(appcode) )
					{
						if(queueMap.get(appcode) != null)
						{
							JSONObject userJson = new JSONObject();
			                // 昵称
			                userJson.put("name", getPushName("6", appcode, null));
			                // appid
			                userJson.put("appcode", appcode);
			                // 企业号
			                userJson.put("ecode", getEcode(appcode));
			                
			                userJson.put("servernum", queueMap.get(appcode));
		                	curUserArray.add(userJson);
						}else
						{
							setCustSerObj(customeId, appcode, "6", false);
						}
					}
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取客服连接的APP用户列表失败！");
		}
		resultObject.put("curs", curUserArray);
        resultObject.put("today", todayArray);
        resultObject.put("yest", yesterArray);
        resultObject.put("ago", agoArray);
        resultObject.put("never", neverArray);
        return resultObject.toString();

    }

    /**
     * APP分配客服人员
     * 
     * @description 如之前接入的app客服人员在线，则优先分配，否则按照接入个数的顺序
     * @param appcode app身份标识
     * @return 分配的客服人员id
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2014-06-18 下午02:02:44
     */
    @SuppressWarnings("unchecked")
    public static String getAppCustomeUser(String appcode)
    {
    	String bindUser = null;
    	try {
    		// 查找app上次服务的客服id
			bindUser = new CustomChatDao().getCustomByAppcode(appcode);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取app用户先前接入的客服id失败！");
		}
		if(bindUser != null)
		{
			// 判断客服id是否在线，不在线则走自动分配流程
			Integer state =  CustomStatusBiz.getCustomStatusMap().get(bindUser);
			if(state != null && state == 1)
			{
				Integer count = (Integer) serCountMap.get(bindUser);
				if(count == null)
				{
					count = 0;
				}
				count ++;
				serCountMap.put(bindUser, count);
				return bindUser;
			}
		}
        String key = null;
        Integer count = 0;
        // 最小接入个数的键值
        String minKey = null;
        Integer minCount = null;
        synchronized (serCountMap)
        {
            JSONObject countJson = serCountMap;
            Iterator<String> it = countJson.keySet().iterator();
            
            if(it.hasNext())
            {
            	minKey = it.next();
            	minCount = (Integer) countJson.get(minKey);
            }
            while(it.hasNext())
        	{
            	key = it.next();
            	count = (Integer) countJson.get(key);
            	if(minCount > count)
            	{
            		minKey = key;
            		minCount = count;
            	}
        	}
            if(minKey == null)
            {
            	return null;
            }else
            {
            	// 先移除选中的客服消息
            	serCountMap.remove(minKey);
            	// 再追加到末尾
            	serCountMap.put(minKey, minCount+1);
            	return minKey;
            }
        }
    }
    
    /**
     * 保存app消息日志
     * @param json
     */
    private static void saveAppLog(String json)
    {
    	EmpExecutionContext.debug("接收上行消息，json="+json);
    }
    
    /**
     * 检查客服人员是否有微信接入
     * @param uid 客服id
     * @return true-存在客服接入
     */
    public static boolean checkWeixServer(String uid)
    {
    	if(custWeixObjectMap.get(uid)==null || custWeixObjectMap.get(uid).length() < 5)
    	{
    		return true;
    	}
    	return false;
    }
}
