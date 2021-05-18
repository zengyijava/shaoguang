package com.montnets.emp.custrep.svt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.custrep.dao.CustRepDao;
import com.montnets.emp.entity.online.LfOnlMsgHis;
import com.montnets.emp.entity.online.LfOnlServer;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiUserInfo;
import com.montnets.emp.entity.wxsysuser.LfSysUser;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.ottbase.svt.BaseServlet;
import com.montnets.emp.ottbase.util.StringUtils;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.znly.biz.CustomChatBiz;
import com.montnets.emp.zxkf.biz.CustomerBiz;

/**
 * @description 在线客服 - 客服系统 - 历史消息
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-20 下午05:46:26
 */

public class curp_serverMsgSvt extends BaseServlet
{

    /**
     * @description  
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-20 下午05:46:31
     */
    private static final long serialVersionUID = 232349649002264379L;
    
    // 资源路径
    private static final String     PATH            = "/zxkf/custrep";

    private final BaseBiz baseBiz = new BaseBiz();
    
    private final CustomChatBiz customChatBiz  = new CustomChatBiz();
    
    private final CustomerBiz customerBiz  = new CustomerBiz();
    

    /**
     * 进入 在线客服 - 客服系统 - 历史消息 查询页面
     * @description    
     * @param request
     * @param response
     * @throws Exception       			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-23 上午10:00:42
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        List<LfWeiAccount> accountList = baseBiz.getEntityList(LfWeiAccount.class);
        request.setAttribute("accountList", accountList);
        request.getRequestDispatcher(PATH + "/curp_serverMsg.jsp").forward(request, response);
    }
    
    
    /***
     * 查询用户信息
    * @Description: TODO
    * @param @param request
    * @param @param response
    * @param @throws Exception
    * @return void
     */
    public void getCustomes(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
        String aid = request.getParameter("aid");
        orderbyMap.put("name", "ASC");
        conditionMap.put("AId", aid);
        
        List<LfSysUser> userList = baseBiz.getByCondition(LfSysUser.class, conditionMap, orderbyMap);
        
        JSONArray resultArray = new JSONArray();
        if(userList == null || userList.size() == 0)
        {
            response.getWriter().print("user:");
            return ;
        }
        for (LfSysUser user : userList)
        {
            JSONObject userJson = new JSONObject();
            Long userid = user.getUserId();
            userJson.put("name", user.getName());
            userJson.put("uid", userid);

            resultArray.add(userJson);
        }
        response.getWriter().print("user:"+resultArray.toString());
    }
    
    /****
     * 获得在线服务相关信息
    * @Description: TODO
    * @param @param request
    * @param @param response
    * @param @throws Exception
    * @return void
     */
    @SuppressWarnings("unchecked")
    public void getCustomeServer(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
        String uid = request.getParameter("uid");
        orderbyMap.put("createTime", "DESC");
        conditionMap.put("customeId", uid);
        
        List<LfOnlServer> serverList = baseBiz.getByCondition(LfOnlServer.class, conditionMap, orderbyMap);
        
        JSONObject resltJson = new JSONObject();
        JSONArray serverArray = new JSONArray();
        if(serverList != null && serverList.size() > 0)
        {
            for (LfOnlServer server : serverList)
            {
                JSONObject userJson = new JSONObject();
                userJson.put("time", StringUtils.timeFormat(server.getCreateTime()));
                userJson.put("sernum", server.getSerNum());
                userJson.put("duration", server.getDuration());

                serverArray.add(userJson);
            }
        }
        resltJson.put("server", serverArray);
       
        CustRepDao repDao = new CustRepDao();
        List<DynaBean> beanList = repDao.getTalkUser(uid);
        JSONArray talkerArray = new JSONArray();
        if(beanList != null && beanList.size() > 0)
        {
            for (DynaBean bean : beanList)
            {
                JSONObject userJson = new JSONObject();
                userJson.put("uid", bean.get("user_id"));
                userJson.put("name", bean.get("name"));

                talkerArray.add(userJson);
            }
        }
        resltJson.put("talker", talkerArray);
        response.getWriter().print("ser:"+resltJson.toString());
    }
    
    /***
     * 在线的相关信息
    * @Description: TODO
    * @param @param request
    * @param @param response
    * @param @throws Exception
    * @return void
     */
    public void getMsgHis(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
        String sernum = request.getParameter("sernum");
        String msgtype = request.getParameter("msgtype");
        orderbyMap.put("sendTime", "ASC");
        
        if("1".equals(msgtype))
        {
            conditionMap.put("serverNum", sernum);
            conditionMap.put("pushType&in", "1,2");
        }else
        {
            String[] nums = sernum.split("to");
            String otherSernum = nums[1]+"to"+nums[0];
            conditionMap.put("serverNum&in", sernum+","+otherSernum);
            conditionMap.put("pushType&in", "3");
        }
        
        conditionMap.put("msgType&not in", "zjkf,tips");
        
        List<LfOnlMsgHis> msgList = baseBiz.getByCondition(LfOnlMsgHis.class, conditionMap, orderbyMap);
        
        JSONArray resultArray = new JSONArray();
        if(msgList == null || msgList.size() == 0)
        {
            response.getWriter().print("msg:");
            return ;
        }
        for (LfOnlMsgHis msgHis : msgList)
        {
            JSONObject jsonOb = new JSONObject();
            jsonOb.put("fromuser", msgHis.getFromUser());
            jsonOb.put("message", msgHis.getMessage());
            jsonOb.put("time", StringUtils.timeFormat(msgHis.getSendTime()));
            jsonOb.put("msgtype", msgHis.getMsgType());
            jsonOb.put("servernum", msgHis.getServerNum());
            // 发送者名称，群组发送时为群组名称，客服对客服发送时为客服人员名称，手机对客服时为手机用户昵称
            jsonOb.put("name", CustomChatBiz.getPushName(msgHis.getPushType().toString(), msgHis.getFromUser(), ""));
            jsonOb.put("pushtype", msgHis.getPushType().toString());

            resultArray.add(jsonOb);
        }
        response.getWriter().print("msg:"+resultArray.toString());
    }
    /**
     * 客服查询聊天记录
     * 
     * @description
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-20 上午11:11:19
     */
    public void findHistoryMessage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // 分页信息
        PageInfo pageInfo = new PageInfo();
        boolean isFirstEnter = pageSet(pageInfo, request);
        String result = "";
        try
        {
            // 客服自己ID
            String ownerId = request.getParameter("ownerId");
            // 被查询历史记录人员ID
            String customId = request.getParameter("customId");
            // 查询开始时间
            String beginTime = request.getParameter("beginTime");
            // 查询结束时间
            String endTime = request.getParameter("endTime");
            // 查询内容
            String content = request.getParameter("content");

            // 存放查询条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

            // 获取查询条件
            if(!isFirstEnter)
            {
                if(null != beginTime && !"".equals(beginTime))
                {
                    conditionMap.put("beginTime", beginTime);
                }
                if(null != endTime && !"".equals(endTime))
                {
                    conditionMap.put("endTime", endTime);
                }
                if(null != content && !"".equals(content))
                {
                    conditionMap.put("content", content);
                }
            }

            // 用来存储根据条件查询到的历史记录信息
            List<LfOnlMsgHis> otOnlMsgHisList = customChatBiz.findHistoryMessage(ownerId, customId, conditionMap, pageInfo);

            if(null != otOnlMsgHisList)
            {
                StringBuffer sb = new StringBuffer();
                sb.append("[");

                // 得到集合的长度
                int size = otOnlMsgHisList.size();
                int i = 0;

                for (LfOnlMsgHis otOnlMsgHis : otOnlMsgHisList)
                {
                    i++;

                    //自增ID
                    String mid = String.valueOf(otOnlMsgHis.getMId());
                    //发送者
                    String fromUserID = otOnlMsgHis.getFromUser();
                    //接收者
                    String toUserID = otOnlMsgHis.getToUser();
                    //发送时间
                    String sendTime = otOnlMsgHis.getSendTime().toString();
                    //服务号
                    String serverNum = otOnlMsgHis.getServerNum();
                    //消息类型
                    String msgType = String.valueOf(otOnlMsgHis.getMsgType());
                    //内容
                    String msgContent = otOnlMsgHis.getMessage();
                    //推送类型
                    String pushType = String.valueOf(otOnlMsgHis.getPushType());
                    //关联的公众账号id
                    String aid = String.valueOf(otOnlMsgHis.getAId());
                    
                    String fromUserName = "";
                    String toUserName = "";
                    //1-手机to客服，2-客服to手机，3-客服to客服，4群组
                    if(null == pushType)
                    {
                        pushType = "";
                    }
                    if(pushType.equals("1"))
                    {
                        conditionMap.clear();
                        conditionMap.put("openId", fromUserID);
                        List<LfWeiUserInfo> otWeiUserInfoList = baseBiz.getByCondition(LfWeiUserInfo.class, conditionMap, null);
                        if(null != otWeiUserInfoList && otWeiUserInfoList.size()>0)
                        {
                            fromUserName = otWeiUserInfoList.get(0).getNickName();
                        }
                        
                        LfSysUser otSysUser = baseBiz.getById(LfSysUser.class, toUserID);
                        if(null != otSysUser)
                        {
                            toUserName = otSysUser.getName();
                        }
                    }
                    else if(pushType.equals("2"))
                    {
                        LfSysUser otSysUser = baseBiz.getById(LfSysUser.class, fromUserID);
                        if(null != otSysUser)
                        {
                            fromUserName = otSysUser.getName();
                        }
                        
                        conditionMap.clear();
                        conditionMap.put("openId", toUserID);
                        List<LfWeiUserInfo> otWeiUserInfoList = baseBiz.getByCondition(LfWeiUserInfo.class, conditionMap, null);
                        if(null != otWeiUserInfoList && otWeiUserInfoList.size()>0)
                        {
                            toUserName = otWeiUserInfoList.get(0).getNickName();
                        }                        
                    }
                    else if(pushType.equals("3"))
                    {
                        LfSysUser otSysUser = baseBiz.getById(LfSysUser.class, fromUserID);
                        if(null != otSysUser)
                        {
                            fromUserName = otSysUser.getName();
                        }
                        
                        otSysUser = baseBiz.getById(LfSysUser.class, toUserID);
                        if(null != otSysUser)
                        {
                            toUserName = otSysUser.getName();
                        }                       
                    }
                    else if(pushType.equals("4"))
                    {
                                             
                    }
                    
                    sb.append("{\"mid\":\"");
                    sb.append(mid);
                    sb.append("\",\"fromUserID\":\"");
                    sb.append(fromUserID);
                    sb.append("\",\"fromUserName\":\"");
                    sb.append(fromUserName);
                    sb.append("\",\"toUserID\":\"");
                    sb.append(toUserID);
                    sb.append("\",\"toUserName\":\"");
                    sb.append(toUserName);
                    sb.append("\",\"sendTime\":\"");
                    sb.append(sendTime);
                    sb.append("\",\"serverNum\":\"");
                    sb.append(serverNum);
                    sb.append("\",\"msgType\":\"");
                    sb.append(msgType);
                    sb.append("\",\"msgContent\":\"");
                    sb.append(msgContent);
                    sb.append("\",\"pushType\":\"");
                    sb.append(pushType);
                    sb.append("\",\"aid\":\"");
                    sb.append(aid);
                    sb.append("\"}");

                    // 如果i小于size字符串sb中加","
                    if(i < size)
                    {
                        sb.append(",");
                    }
                }

                sb.append("]");

                // 拼好的字符串赋值给变量
                result = "success@" + sb.toString();
            }
            else
            {
                result = "success@[]";
            }   
        }
        catch (Exception e)
        {
            result = "fail@";
            EmpExecutionContext.error(e, "在线客服-历史聊天记录查询页面加载出错！");
        }
        finally
        {
            request.setAttribute("pageInfo", pageInfo);
            response.getWriter().print(result);
        }
    }

    /**
     * 加载所有有聊天历史记录的客服人员信息
     * @description    
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException                   
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-20 下午04:35:14
     */
    public void findAllClient(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String corpcode = request.getParameter("lgcorpcode");
        LinkedHashMap<String, String> conditionMap = null;
        String aid = request.getParameter("aid");
        if(null != aid && !"".equals(aid))
        {
            conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("AId", aid);
        }
        List<DynaBean> clientListBean = customerBiz.findCustomerList(corpcode, conditionMap, null);
        request.setAttribute("clientListBean", clientListBean);

        request.getRequestDispatcher(PATH + "/zxkf_historyMessage.jsp").forward(request, response);
    }
    
    /**
     * 加载所有客服人员所服务的人员信息
     * @description    
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException       			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-23 上午09:04:07
     */
    @SuppressWarnings("unchecked")
    public void findCustomerToClient(HttpServletRequest request, HttpServletResponse response)
    {
        String result = "";
        try
        {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            Map<String, String> userMap = new HashMap<String, String>();
            String clientID = request.getParameter("clientID");
            JSONObject jsonObject = customerBiz.findCustomerToClient(clientID);            
            if(null != jsonObject)
            {
                List<LfSysUser> otSysUserList = null;
                List<LfWeiUserInfo> otWeiUserInfoList = null;
                List<LfWeiAccount> otWeiAccList = null;
                String acctName = "";
                Object otSysUserObject = jsonObject.get("otSysUserObjList");
                Object otWeiUserInfoObject = jsonObject.get("otWeiUserInfoObjList");
                if(null != otSysUserObject)
                {
                    otSysUserList = (List<LfSysUser>) otSysUserObject;
                    for (Iterator iterator = otSysUserList.iterator(); iterator.hasNext();)
                    {
                        LfSysUser otSysUser = (LfSysUser) iterator.next();
                        String aid = String.valueOf(otSysUser.getAId());
                        
                        // 清空conditionMap，下面公用
                        conditionMap.clear();
                        conditionMap.put("AId", aid);
                        // 用来存储所有公众账号
                        otWeiAccList = baseBiz.getByCondition(LfWeiAccount.class, conditionMap, null);
                        if(null != otWeiAccList && otWeiAccList.size() > 0)
                        {
                            acctName = otWeiAccList.get(0).getName();
                        }
                        
                        userMap.put(String.valueOf(otSysUser.getUserId()), otSysUser.getName()+"（"+acctName+"）");
                    }
                }
                if(null != otWeiUserInfoObject)
                {
                    otWeiUserInfoList = (List<LfWeiUserInfo>) otWeiUserInfoObject;
                    for (Iterator iterator = otWeiUserInfoList.iterator(); iterator.hasNext();)
                    {
                        LfWeiUserInfo otWeiUserInfo = (LfWeiUserInfo) iterator.next();
                        String aid = String.valueOf(otWeiUserInfo.getAId());
                        // 清空conditionMap，下面公用
                        conditionMap.clear();
                        conditionMap.put("AId", aid);
                        // 用来存储所有公众账号
                        otWeiAccList = baseBiz.getByCondition(LfWeiAccount.class, conditionMap, null);
                        if(null != otWeiAccList && otWeiAccList.size() > 0)
                        {
                            acctName = otWeiAccList.get(0).getName();
                        }
                        userMap.put(otWeiUserInfo.getOpenId(), otWeiUserInfo.getNickName()+"（"+acctName+"）");
                    }
                }
                jsonObject.clear();
                jsonObject.putAll(userMap);
                result = "success@" + jsonObject.toString();
            }

            response.getWriter().print(result);
        }
        catch (Exception e)
        {
            result = "fail@";
            EmpExecutionContext.error(e, "在线客服-客服系统-历史聊天记录查询页面加载出错！");
        }
    }
    
}
