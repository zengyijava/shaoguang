package com.montnets.emp.wxgl.svt;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiSendlog;
import com.montnets.emp.ottbase.util.GlobalMethods;
import com.montnets.emp.ottbase.util.TxtFileUtil;
import com.montnets.emp.znly.biz.CustomChatBiz;
import com.montnets.emp.znly.biz.CustomThread;
import com.montnets.emp.znly.biz.LbsPiosBiz;
import com.montnets.emp.znly.biz.WxCommBiz;

@SuppressWarnings("serial")
public class weix_wxCommonSvt extends HttpServlet
{
    // 基础逻辑层
    private BaseBiz   baseBiz   = new BaseBiz();

    // 微信逻辑层
    private WxCommBiz wxCommBiz = new WxCommBiz();

    /**
     * EMP企业微信公众平台验证接入
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        
        if(signature== null || timestamp == null || nonce == null)
        {
        	EmpExecutionContext.error("EMP企业微信对接验证时发现空值！signature:"+signature+";timestamp:"+timestamp+";nonce:"+nonce+";echostr:"+echostr);
        	response.getWriter().print(echostr);
        	return;
        }
        
        // 接入地址
        String url = request.getRequestURI();
        // 字段URL，提取唯一身份标识
        String identity = url.substring(url.lastIndexOf("/") + 1, url.length());
        // 查询条件
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        // TOKEN标志
        String token = "";
        PrintWriter out = response.getWriter();

        try
        {
            conditionMap.put("url", identity);
            List<LfWeiAccount> acctList = baseBiz.getByCondition(LfWeiAccount.class, conditionMap, null);
            if(acctList != null && acctList.size() > 0)
            {
                LfWeiAccount acct = acctList.get(0);
                token = acct.getToken();
            }
            // 验证是否通过
            boolean vpass = wxCommBiz.verifySignature(signature, timestamp, nonce, token);

            if(vpass)
            {
                out.print(echostr);
            }
        }
        catch (NoSuchAlgorithmException ex)
        {
            out.print("Exception:" + ex.getMessage());
        }
        catch (Exception ex)
        {
            out.print("Exception:" + ex.getMessage());
        }
        finally
        {
            out.close();
        }
    }

    /**
     * EMP企业微信响应腾讯微信服务器POST请求接入方法
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // 服务器地址
        String weixBasePath = GlobalMethods.getWeixBasePath();
        // 待返回msgXml格式消息
        String returnMsgXml = "";
        // 请求参数的Map化，方便后面取值
        HashMap<String, String> paramsXmlMap = null;
        // 人工服务和自动回复模式
        boolean isRobot = true;
        try
        {
            // 解析微信上行XML文件
            paramsXmlMap = wxCommBiz.analyzeWeixinXML(request);
            if(paramsXmlMap == null || paramsXmlMap.size() == 0)
            {
                EmpExecutionContext.error("weix_wxCommSvt.paramsXmlMap is null");
                return;
            }
            // 当前公众帐号
            LfWeiAccount acct = wxCommBiz.getAcctByOpenId(paramsXmlMap.get("ToUserName"));
            // 流程控制标识-是否空响应
            boolean noReply = false;
            if(acct.getAId() != null)
            {
                // 消息类型判断(0:text;1:image;2:localtion;link:3;event:4)
                Integer requestType = wxCommBiz.getMsgType(paramsXmlMap.get("MsgType"), "0");
                // 判读当前是微信用户的回复模式("机器人处理""在线客服处理")判断是否客服请求状态
                isRobot = CustomChatBiz.checkCustome(paramsXmlMap.get("FromUserName"),requestType);

                if(!isRobot)
                {
                	CustomThread customThread= new  CustomThread();
                	customThread.setAcct(acct);
                	customThread.setXmlMap(paramsXmlMap);
                	Thread thread = new Thread(customThread); 
                	thread.start();
                }
                else
                {
                    switch (requestType)
                    {
                        case 0:
                            // 文本消息处理
                            String msg = wxCommBiz.extractMsg(paramsXmlMap);

                            if(msg != null && !"".equals(msg))
                            {
                                // 判断是否上行客服请求指令
                                if("qdkf".equals(msg.toLowerCase()))
                                {
                                    // 启动客服
                                    CustomChatBiz.callCustome(acct, paramsXmlMap);
                                    noReply = true;
                                }
                                else
                                {
                                    // 关键字匹配搜索逻辑,返回msgXml**
                                    // TODO-关键字搜索规则在getKeywordMsgXml方法实现
                                    returnMsgXml = wxCommBiz.getKeywordMsgXml(acct, msg);
                                }
                            }
                            break;
                        case 1:
                            break;
                        case 2:
                            returnMsgXml = new LbsPiosBiz().getLocationMsgXml(paramsXmlMap.get("Location_Y"), paramsXmlMap.get("Location_X"), paramsXmlMap.get("FromUserName"), "location", acct);
                            break;
                        case 4:
                            // 处理点击事件 订阅/菜单
                            returnMsgXml = this.operateClickEvent(paramsXmlMap, acct, weixBasePath, isRobot);
                            if(returnMsgXml == null)
                            {
                                // 如果返回的是NULL，则不需要下发信息
                                noReply = true;
                            }
                            break;
                        default:
                            returnMsgXml = "";
                    }
                }
                if(noReply == true)
                {
                    // 如果系统不需要返回xml格式数据，那么直接返回空字符
                    returnMsgXml = "";
                }
                else
                {
                    // msgXml为空执行默认回复处理
                    if(returnMsgXml == null || "".equals(returnMsgXml))
                    {
                        returnMsgXml = wxCommBiz.getDefaultReplyMsgXml(acct);
                    }
                    // 返回相应消息给微信用户
                    returnMsgXml = wxCommBiz.getResponseXml(weixBasePath, paramsXmlMap, returnMsgXml);
                }
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "EMP企业微信响应客户端请求失败！");
        }
        finally
        {
            if(isRobot && returnMsgXml != null && !"".equals(returnMsgXml))
            {
                /** 服务器响应的XML数据返回给微信服务器 **/
                response.setContentType("text/xml;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.print(returnMsgXml);
                out.close();
                new TxtFileUtil().writeWeixMsgToFile("response", returnMsgXml);
            }
            // ----企业微信公众平台上行/下行消息结束----
        }
    }

    /** 初始化 **/
    public void init() throws ServletException
    {
    }

    public void destroy()
    {
        super.destroy();
    }

    /**
     * @description 操作CLICK 事件
     * @param paramsXmlMap
     *        微信上行
     * @param acct
     *        公众账号
     * @param user
     *        普通用户对象
     * @param weixBasePath
     *        外网地址
     * @param isRobot
     *        机器 true 人工false
     * @return 成功返回xml格式 失败 返回“”则需要下发，返回null则不需要下发
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-20 下午03:51:14
     */
    private String operateClickEvent(HashMap<String, String> paramsXmlMap, LfWeiAccount acct, String weixBasePath, boolean isRobot)
    {
        String msgXml = "";
        try
        {
            String event = paramsXmlMap.get("Event");
            // 事件类消息处理(subscribe,unsubscribe,CLICK)消息
            if("subscribe".equals(event))
            {
                // LfWeiUserInfo user = wxCommBiz.buildOtWeiUserInfo(acct, paramsXmlMap);
                if(wxCommBiz.buildOtWeiUserInfo(acct, paramsXmlMap)==null)
                {
                	return "";
                }
                // 关注时回复已经取消选择模板功能，回复内容为简单的文本
                msgXml = wxCommBiz.getSubscribeMsgXml(acct);
                // 如果没有获取到该公众帐号关注回复的模板,则自动下发默认信息
                if(msgXml == null || "".equals(msgXml))
                {
                    // msgXml = wxCommBiz.attachInviteMsgXml(acct, user, weixBasePath, msgXml);
                    msgXml = wxCommBiz.getDefaultResponseXml(paramsXmlMap, "谢谢您的关注！");
                }
            }
            else if("unsubscribe".equals(event))
            {
                // 取消关注事件
                wxCommBiz.updateSubscribeStatus(acct, paramsXmlMap.get("FromUserName"));
            }
            else if("CLICK".equals(event))
            {
                // 菜单事件处理(CLICK) 获取EventKey的标识
                String eventKey = paramsXmlMap.get("EventKey");

                if(eventKey.contains("_module_zxkf"))
                {
                    // 激活当前客服在线客服功能
                    CustomChatBiz.callCustome(acct, paramsXmlMap);
                    msgXml = null;
                }
                else if(eventKey.contains("_module_lbs"))
                {
                    // 点LBS事件
                    msgXml = new LbsPiosBiz().getLocationMsgXmlByMenu(paramsXmlMap, acct);
                }
                else if(eventKey.contains("_module_choujiang"))
                {
                    // 抽奖
                    // msgXml = new YxglLotteryBiz().getLotteryMsg(paramsXmlMap, acct,eventKey);
                }
                else
                {
                    msgXml = wxCommBiz.getMenuMsgXml(acct, eventKey);
                    // 如果菜单没有关联图文获取msgXML为空，则将noReply设置为true
                    if((msgXml == null || "".equals(msgXml)))
                    {
                        msgXml = null;
                    }
                }

            }
            else if("LOCATION".equals(event))
            {
                // 获取用户地理位置
                // 纬度
                String lat = paramsXmlMap.get("Latitude");
                // 经度
                String lng = paramsXmlMap.get("Longitude");
                // 普通用户openid
                String fromUserName = paramsXmlMap.get("FromUserName");
                if(lat == null || "".equals(lat) || lng == null || "".equals(lng) || fromUserName == null || "".equals(fromUserName))
                {
                    EmpExecutionContext.error("weix_wxCommSvt.operateClickEvent.lat:" + lat + ",lng:" + lng + ",fromUserName:" + fromUserName);
                    return "";
                }
                new LbsPiosBiz().handleLbsUserPios(lat, lng, fromUserName, System.currentTimeMillis(), "push", acct);
                msgXml = null;
                // 不需要下发信息
            }
            else if("MASSSENDJOBFINISH".equals(event))
            {
                String msgId = paramsXmlMap.get("MsgID");
                String msgType = paramsXmlMap.get("MsgType");
                if(null == msgId || "".equals(msgId) || msgType == null || "".equals(msgType))
                {
                    EmpExecutionContext.error("weix_wxCommSvt.operateClickEvent.msg_id:");
                    return "";
                }
                else
                {
                    LfWeiSendlog sendlog = null;
                    LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                    conditionMap.put("msgId", msgId);
                    conditionMap.put("msgType", msgType);
                    List<LfWeiSendlog> sendlogs = baseBiz.getByCondition(LfWeiSendlog.class, conditionMap, null);
                    if(null != sendlogs && sendlogs.size() > 0)
                    {
                        sendlog = sendlogs.get(0);
                        sendlog.setStatus(2);
                        sendlog.setEventData(paramsXmlMap.toString());
                        baseBiz.addObj(sendlog);
                    }
                }
            }
        }
        catch (Exception e)
        {
            msgXml = "";
            EmpExecutionContext.error(e, "weix_wxCommSvt.operateClickEvent is error");
        }
        return msgXml;
    }

}
