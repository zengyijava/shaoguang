package com.montnets.emp.znly.biz;

import java.io.BufferedReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiCount;
import com.montnets.emp.entity.wxgl.LfWeiKeyword;
import com.montnets.emp.entity.wxgl.LfWeiMenu;
import com.montnets.emp.entity.wxgl.LfWeiMsg;
import com.montnets.emp.entity.wxgl.LfWeiRevent;
import com.montnets.emp.entity.wxgl.LfWeiRimg;
import com.montnets.emp.entity.wxgl.LfWeiRtext;
import com.montnets.emp.entity.wxgl.LfWeiTemplate;
import com.montnets.emp.entity.wxgl.LfWeiUserInfo;
import com.montnets.emp.ottbase.biz.WeixBiz;
import com.montnets.emp.ottbase.service.SinaStockService;
import com.montnets.emp.ottbase.util.GlobalMethods;
import com.montnets.emp.ottbase.util.TxtFileUtil;
import com.montnets.emp.znly.dao.KeywordDao;
import com.montnets.emp.znly.dao.MsgDao;
import com.montnets.emp.znly.dao.RtextDao;
import com.montnets.emp.znly.dao.TempleDao;

public class WxCommBiz extends SuperBiz
{
    MsgDao  msgDao  = new MsgDao();
    WeixBiz weixBiz = new WeixBiz();
    CountBiz countBiz = new CountBiz();
    
    private static String waitInsertUser = ",";
    
    /**
     * 企业微信接入地址验证
     * 
     * @param signature:微信加密签名
     * @param timestamp:时间戳
     * @param nonce:随机数
     * @param token:随机字符串
     * @return
     * @throws Exception
     */
    public boolean verifySignature(String signature, String timestamp, String nonce, String token) throws Exception
    {
        boolean flag = false;
        String[] array = {token,timestamp,nonce};
        try
        {
            Arrays.sort(array);
            String result = "";
            for (String i : array)
            {
                result += i;
            }
            String ret = "";
            ret = SHAsum(result.getBytes());
            if(ret.equalsIgnoreCase(signature))
            {
                flag = true;
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"EMP企业微信对接验证失败 ！");
            //throw new NoSuchAlgorithmException("验证失败 ！");
        }
        return flag;
    }

    /**
     * 读取微信服务器返回的XML数据，并返回String类型数据
     * 
     * @param request
     * @return
     */
    public String readXMLFromRequestBody(HttpServletRequest request)
    {
        StringBuffer xml = new StringBuffer();
        String line = null;
        try
        {
            BufferedReader reader = request.getReader();
            while((line = reader.readLine()) != null)
            {
                xml.append(line);
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"读取微信服务器返回的XML数据异常！");
        }
        return xml.toString();
    }

    /**
     * 将请求参数转化成Map数据格式，方便读取
     * 
     * @param requestXml
     * @return
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, String> getParamsXmlMap(String requestXml)
    {
        HashMap<String, String> paramsXmlMap = new HashMap<String, String>();
        try
        {
            Document document = DocumentHelper.parseText(requestXml);
            Element items = document.getRootElement();

            for (Iterator i = items.elementIterator(); i.hasNext();)
            {
                Element item = (Element) i.next();
                paramsXmlMap.put(item.getName(), item.getText());
                EmpExecutionContext.debug(item.getName() + ":" + item.getText());
            }
        }
        catch (DocumentException e)
        {
            // 解析异常后处理
            EmpExecutionContext.error(e,"requestXml格式解析失败！");
        }

        return paramsXmlMap;
    }

    /**
     * 创建消息历史记录
     * weixBiz.createLMsg(acct,alink,"0",requestXml,paramsXmlMap);
     * 
     * @param paramsXmlMap
     * @return
     */
    public LfWeiMsg createMsg(LfWeiAccount acct, LfWeiUserInfo user,String type, String msgXml, HashMap<String, String> paramsXmlMap)
    {
        // 消息类型
        String msgtype = paramsXmlMap.get("MsgType");
        Integer mTp = getMsgType(msgtype, type);
        LfWeiMsg lmsg = new LfWeiMsg();

        try
        {
            lmsg.setCorpCode(acct.getCorpCode());
            lmsg.setMsgType(mTp);
            lmsg.setWcId(user.getWcId());
            lmsg.setAId(acct.getAId());
            lmsg.setType(Integer.valueOf(type));
            lmsg.setMsgXml(msgXml);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            lmsg.setCreateTime(Timestamp.valueOf(df.format(new Date())));
            // 如果是文本消息，记录文本消息
            if(msgtype != null && "text".equals(msgtype))
            {
                String content = new String(paramsXmlMap.get("Content"));
                lmsg.setMsgText(StringUtils.defaultString(content, ""));
            }
            else
            {
                // TODO-其他类型的消息获取消息的正文用于页面显示
                lmsg.setMsgText("");
            }
            // id程序自增
            long value = empDao.getIdByPro(514, 1);
            lmsg.setMsgId(Long.valueOf(value));

            msgDao.createMsg(lmsg);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"创建上行或者下行记录失败！");
        }
        return lmsg;
    }

    /**
     * 通过openId获取当前公众帐号
     * @param
     * @return
     */
    public LfWeiAccount getAcctByOpenId(String toUserName)
    {
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("openId", toUserName);
        LfWeiAccount acct = new LfWeiAccount();
        List<LfWeiAccount> acctList;
        try
        {
            if(toUserName != null && !"".equals(toUserName))
            {
                acctList = empDao.findListByCondition(LfWeiAccount.class, conditionMap, null);

                if(acctList != null && acctList.size() > 0)
                {
                    acct = acctList.get(0);
                }
            }

        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"通过公众帐号openId获取当前公众帐号失败！");
        }

        return acct;
    }

    /**
     * 通过openid查找微信用户，如果不存在则创建新的微信用户
     * @param
     * @return
     */
    public LfWeiUserInfo buildOtWeiUserInfo(LfWeiAccount acct,HashMap<String, String> paramsXmlMap)
    {
        LfWeiUserInfo userInfo = null;
        String fromUserName = paramsXmlMap.get("FromUserName");
        try
        {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("openId", fromUserName);
            conditionMap.put("AId", String.valueOf(acct.getAId()));
            List<LfWeiUserInfo> userInfoList = empDao.findListByCondition(LfWeiUserInfo.class, conditionMap, null);
            //获取token
            String token = weixBiz.getToken(acct);
            if(userInfoList != null && userInfoList.size() > 0)
            {
                userInfo = userInfoList.get(0);
                //同步微信用户信息
                weixBiz.updateExistWeixUserInfo(token,userInfo);
            }
            else
            {
                //同步微信用户信息         第一次关注或者微信用户不存在的情况下创建微信用户  unsubscribe  Event
                userInfo = weixBiz.getWeixBaseUserInfo(token,fromUserName);
                userInfo.setCorpCode(acct.getCorpCode());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                userInfo.setCreatetime(Timestamp.valueOf(df.format(new Date())));
                userInfo.setOpenId(fromUserName);
                userInfo.setAId(acct.getAId());
                userInfo.setSubscribe("1");
                // 防止同时并发请求的数据库添加机制
                if(waitInsertUser.indexOf(fromUserName)>0)
                {
                	return null;
                }else
                {
                	waitInsertUser += fromUserName +",";
                	 //创建微信用户
                	empDao.saveObjProReturnID(userInfo);
                	waitInsertUser=waitInsertUser.replace(fromUserName+",", "");
                }
            }
            // 更新统计数据LF_WEI_COUNT表
            this.createOrUpdateCount(acct, "subscribe");
        }
        catch (Exception e)
        {
        	waitInsertUser=waitInsertUser.replace(fromUserName+",", "");
            EmpExecutionContext.error(e, "创建微信用户并建立与微信帐号关联联系操作失败！");
        }
        return userInfo;
    }

    /**
     * 更新当前公众帐号关注人数和取消关注人数
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2014-1-20 下午12:54:30
     */
    public void createOrUpdateCount(LfWeiAccount acct,String tp){
        //声明LF_WEI_COUNT对象
        LfWeiCount otWeiCount = new LfWeiCount();
        try{
            if(acct.getSyncTime()!=null){
                otWeiCount = countBiz.getTodayCount(acct.getAId());
                if(otWeiCount!=null){
                    // 存在情況更新
                    if("subscribe".equals(tp)){
                        otWeiCount.setFollowCount(otWeiCount.getFollowCount()+1);
                        otWeiCount.setAmountCount(otWeiCount.getAmountCount()+1);
                    }else if("unsubscribe".equals(tp)){
                        otWeiCount.setUnfollowCount(otWeiCount.getUnfollowCount()+1);
                    }
                    otWeiCount.setIncomeCount(otWeiCount.getFollowCount()- otWeiCount.getUnfollowCount());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    otWeiCount.setModifytime(Timestamp.valueOf(df.format(System.currentTimeMillis())));
                    empDao.update(otWeiCount);
                }else{
                    Integer amount = countBiz.getLastAmount(acct.getAId());
                    if("subscribe".equals(tp)){
                        otWeiCount = countBiz.ceateTodayCount(acct, 1, 0, 1, amount);
                    }else if("unsubscribe".equals(tp)){
                        otWeiCount = countBiz.ceateTodayCount(acct, 0, 1, -1, amount);
                    }
                    
                    empDao.save(otWeiCount);
                }
            }
        }catch(Exception e){
            EmpExecutionContext.error(e,"更新统计数据失败！");
        }
    }
    
    /**
     * 更新微信用户的关注状态
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2014-1-20 下午05:19:49
    */
    public void updateSubscribeStatus(LfWeiAccount acct,String fromUserName){
        LfWeiUserInfo otWeiUserInfo = null;
        try
        {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap< String, String>();
            conditionMap.put("AId", String.valueOf(acct.getAId()));
            conditionMap.put("openId",fromUserName);
            List<LfWeiUserInfo> userList = empDao.findListByCondition(LfWeiUserInfo.class, conditionMap, null);
            if(userList!=null&&userList.size()>0){
                otWeiUserInfo = userList.get(0);
                otWeiUserInfo.setSubscribe("0");
                boolean result = empDao.update(otWeiUserInfo);
                if(result){
                    // 更新统计数据LF_WEI_COUNT表
                    this.createOrUpdateCount(acct, "unsubscribe");
                }
            }
            
        }
        catch (Exception e)
        {
           EmpExecutionContext.error(e,"updateSubscribeStatus中查询微信用户失败！");
        }
    }
    /**
     * 提取关键字，目前只处理了文本消息关键字提取
     * 
     * @param paramsXmlMap
     * @return
     */
    public String extractMsg(HashMap<String, String> paramsXmlMap)
    {
        // 上行消息类型
        String msgtype = paramsXmlMap.get("MsgType");
        String msg = null;
        if(msgtype != null && "text".equals(msgtype))
        {
            try
            {
                msg = paramsXmlMap.get("Content").trim();
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "提取关键字失败！");
            }
        }
        else
        {
            // TODO-其他类型的消息
            msg = "";
        }
        return msg;
    }

    /**
     *关键字匹配搜索
     * 
     * @param acct
     * @param msg
     * @return
     */
    public String getKeywordMsgXml(LfWeiAccount acct, String msg)
    {
        LfWeiKeyword keywordTemp = new LfWeiKeyword();
        String msgXml = "";
        try
        {
            //如果匹配到系统设定的关键字
            msgXml = getSystemKeywordMsgXml(msg);
            if(msgXml!=null&&!"".equals(msgXml)){
                return msgXml;
            }
            // 匹配关键字
            List<LfWeiKeyword> keywordsList = findKeyWordsByAccount(acct);
            if(keywordsList != null && keywordsList.size() > 0)
            {
                for (LfWeiKeyword keyword : keywordsList)
                {
                    if(keyword.getType() == 0 && msg.contains(keyword.getName()))
                    {
                        keywordTemp.setKId(keyword.getKId());
                        break;
                    }
                    else
                        if(keyword.getType() == 1 && msg.equals(keyword.getName()))
                        {
                            keywordTemp.setKId(keyword.getKId());
                            break;
                        }
                }

                // 如果匹配到关键字，则查询这个关键字对于的模板
                if(keywordTemp.getKId() != null)
                {
                    List<DynaBean> beans = new ArrayList<DynaBean>();
                    beans = findTemplateByKeywordId(String.valueOf(keywordTemp.getKId()));
                    if(beans != null && beans.size() > 0)
                    {
                        msgXml = (String) beans.get(0).get("msg_xml");
                    }
                }
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"通过关键字获取msgXml值失败！");
        }
        return msgXml;
    }

    /**
     * 关注事件消息回复msgXMl
     * 
     * @param acct
     * @return
     */
    public String getSubscribeMsgXml(LfWeiAccount acct)
    {
        String msgXml = "";
        try
        {
            List<LfWeiRevent> reventList = findSubscribeByAccount(acct);
            if(reventList != null && reventList.size() > 0)
            {
                LfWeiRevent revent = reventList.get(0);
                if(revent.getTId() != null && !"0".equals(String.valueOf(revent.getTId())))
                {
                    // 从模板里面读XML，实现同步
                    LfWeiTemplate template = empDao.findObjectByID(LfWeiTemplate.class, revent.getTId());
                    if(template instanceof LfWeiTemplate)
                    {
                        msgXml = template.getMsgXml();
                    }
                }
                else
                {
                    msgXml = revent.getMsgXml();
                }
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"获取关注时回复的msgXml值失败！");
        }
        return msgXml;
    }

    /**
     * 查询公众帐号-默认回复msgXml
     * 
     * @param acct
     * @return
     */
    public String getDefaultReplyMsgXml(LfWeiAccount acct)
    {
        String msgXml = "";
        try
        {
            List<LfWeiRtext> rtextList = findDefaultReplyByAccount(acct);
            if(rtextList != null && rtextList.size() > 0)
            {
                Random r = new Random();
                int n = r.nextInt(rtextList.size());
                LfWeiRtext rtext = rtextList.get(n);
                if(rtext.getTId() != null && !"0".equals(String.valueOf(rtext.getTId())))
                {
                    // 从模板里面读XML，实现同步
                    LfWeiTemplate template = empDao.findObjectByID(LfWeiTemplate.class, rtext.getTId());
                    if(template instanceof LfWeiTemplate)
                    {
                        msgXml = template.getMsgXml();
                    }
                }
                else
                {
                    msgXml = rtext.getMsgXML();
                }
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"获取默认回复msgXML值失败！");
        }
        return msgXml;
    }

    /**
     * 查询公众帐号所有关联关键字，时间倒序排列
     * 
     * @param acct
     * @return
     */
    public List<LfWeiKeyword> findKeyWordsByAccount(LfWeiAccount acct)
    {
        List<LfWeiKeyword> keywordsList = null;
        try
        {
            return new KeywordDao().findKeyWordsByAccount(acct);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"查找公众帐号对应的所有关键字失败！");
        }
        return keywordsList;
    }

    /**
     * 查询关键字所对于的模板
     * 
     * @param kId
     * @return
     */
    public List<DynaBean> findTemplateByKeywordId(String kId)
    {
        List<DynaBean> beans = null;
        try
        {
            return new TempleDao().findTemplateByKeywordId(kId);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"通过关键字获取对应模板失败！");
        }
        return beans;
    }

    /**
     * 查询公众帐号-关注事件回复List<LfWeiRevent>对象列表
     * 
     * @param acct
     * @return
     */
    public List<LfWeiRevent> findSubscribeByAccount(LfWeiAccount acct)
    {
        List<LfWeiRevent> reventList = null;
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();

        conditionMap.put("AId", String.valueOf(acct.getAId()));
        conditionMap.put("evtType", "1");
        orderMap.put("createtime", "DESC");
        try
        {
            reventList = empDao.findListByCondition(LfWeiRevent.class, conditionMap, orderMap);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"通过公众帐号查询关注时回复失败！");
        }
        return reventList;
    }

    /**
     * 查询公众帐号-默认回复List<LfWeiRtext>列表
     * 
     * @param acct
     * @return
     */
    public List<LfWeiRtext> findDefaultReplyByAccount(LfWeiAccount acct)
    {
        List<LfWeiRtext> rtextList = null;
        try
        {
            return new RtextDao().findDefaultReplyByAccount(acct);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"通过公众帐号查询默认回复失败！");
        }
        return rtextList;
    }

    /**
     * 创建返回的xml数据，将ToUserName和FromUserName加上
     * 
     * @param toUser
     * @param fromUser
     * @param msgXml
     * @return
     */
    public String createResponseXml(HashMap<String, String> paramsXmlMap, String msgXml)
    {
        String responseXml = msgXml;
        String toUser = paramsXmlMap.get("FromUserName");
        String fromUser = paramsXmlMap.get("ToUserName");
        if(responseXml.contains("ToUserName") && responseXml.contains("FromUserName"))
        {
            Date time = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(time);
            long createTime = cal.getTimeInMillis() / 1000;

            responseXml = responseXml.replaceAll("<ToUserName>(.*?)</ToUserName>", "<ToUserName>" + toUser + "</ToUserName>");
            responseXml = responseXml.replaceAll("<FromUserName>(.*?)</FromUserName>", "<FromUserName>" + fromUser + "</FromUserName>");
            responseXml = responseXml.replaceAll("<CreateTime>(.*?)</CreateTime>", "<CreateTime>" + String.valueOf(createTime) + "</CreateTime>");
        }
        return responseXml;
    }

       /**
     * 创建返回的xml数据，将ToUserName和FromUserName加上
     * 
     * @param toUser
     * @param fromUser
     * @param msgXml
     * @return
     */
    public String getResponseXml(String weixBasePath,HashMap<String, String> paramsXmlMap, String msgXml)
    {
        // 创建返回的responseXml数据
        String responseXml = createResponseXml(paramsXmlMap, msgXml);
        // 检查responseXml是否符合条件
        if(!checkMsgXml(responseXml))
        {
            //responseXml = getSystemDefaultResponseXml(paramsXmlMap);
            responseXml = getDefaultResponseXml(paramsXmlMap, "尊敬的用户，您的回复已收录，请下次询问！");
        }
        
        // 保存下行记录到LF_WC_MSG表中
        //HashMap<String, String> rparamsXmlMap = new HashMap<String, String>();
        //rparamsXmlMap = wxCommBiz.getParamsXmlMap(responseXml);
        //lmsg = wxCommBiz.createMsg(acct, user, "1", responseXml, rparamsXmlMap);
        
        // 将PicUrl,Url,MusicUrl,HQMusicUrl等标签对中的{basePath}替换成当前服务器的路径
        responseXml = handleMsgXmlBasePath(responseXml, weixBasePath);
        return responseXml;
    }
    
    /**
     * 当文本回复的时候，在msgXml的content中添加会员邀请链接
     * 
     * @param msgXml
     * @return
     */
    public String attachInviteMsgXml(LfWeiAccount acct, LfWeiUserInfo user, String basePath, String msgXml)
    {
        if(user != null && msgXml != null && !"".equals(msgXml))
        {
            Long wcId = user.getWcId();
            String msg = getInviteMsg(acct.getCorpCode(), wcId, basePath);
            HashMap<String, String> paramsXmlMap = new HashMap<String, String>();
            paramsXmlMap = getParamsXmlMap(msgXml);
            // 只有文本消息时，才在content上添加"邀请链接"
            if("text".equals(paramsXmlMap.get("MsgType")))
            {
                msgXml = msgXml.replaceAll("<Content>(.*?)</Content>", "<Content><![CDATA[" + paramsXmlMap.get("Content") + " " + msg + "]]></Content>");
            }
        }
        else
        {
            msgXml = "";
        }
        return msgXml;
    }

    /**
     * 系统无法识别时的回复
     * 
     * @param toUser
     * @param fromUser
     * @return
     */
    public String getInviteMemberMsgXml(LfWeiAccount acct, LfWeiUserInfo user, String basePath)
    {
        Long wcId = user.getWcId();
        String msg = getInviteMsg(acct.getCorpCode(), wcId, basePath);
        StringBuffer xml = new StringBuffer();
        xml.append("<xml>");
        xml.append("<ToUserName></ToUserName>");
        xml.append("<FromUserName></FromUserName>");
        xml.append("<CreateTime></CreateTime>");
        xml.append("<MsgType>text</MsgType>");
        xml.append("<Content><![CDATA[" + msg + "]]></Content>");
        xml.append("</xml>");
        return xml.toString();
    }

    
    
    /**
     * 通过EventKey获取msgXML
     * 
     * @param acct
     * @param EventKey
     * @return
     */
    public String getMenuMsgXml(LfWeiAccount acct, String EventKey)
    {
        String msgXml = "";
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("mkey", EventKey);
        conditionMap.put("AId", String.valueOf(acct.getAId()));

        List<LfWeiMenu> menuList = new ArrayList<LfWeiMenu>();

        try
        {
            menuList = empDao.findListByCondition(LfWeiMenu.class, conditionMap, null);
            if(menuList != null && menuList.size() > 0)
            {
                LfWeiMenu menu = menuList.get(0);
                if(menu.getTId() != null && !"0".equals(String.valueOf(menu.getTId())))
                {
                    // 从模板里面读XML，实现同步
                    LfWeiTemplate template = empDao.findObjectByID(LfWeiTemplate.class, menu.getTId());
                    if(template instanceof LfWeiTemplate)
                    {
                        msgXml = template.getMsgXml();  
                    }
                }
                else
                {
                    msgXml = menu.getMsgXml();
                }
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"通过EventKey获取msgXML失败！");
        }

        return msgXml;
    }

    /**
     * 发送会员邀请的文本和链接地址
     * 
     * @param basePath
     * @param corpCode
     * @param wcId
     * @return
     */
    private String getInviteMsg(String corpCode, Long wcId, String basePath)
    {
        String inviteUrl = basePath + "cwc_infoRelated.hts?lgcorpcode=" + corpCode + "&wcId=" + String.valueOf(wcId);
        String msg = "点击下面的链接，成为我们的会员:<a href=\"" + inviteUrl + "\">点击这里,加入会员</a>";
        return msg;
    }

    // 检查msgXMl格式是否正确(文本回复和图文回复检查)
    private boolean checkMsgXml(String responseXml)
    {
        boolean flag = true;
        HashMap<String, String> paramsXmlMap = new HashMap<String, String>();
        if(responseXml == null || "".equals(responseXml))
        {
            flag = false;
        }
        else
        {
            paramsXmlMap = getParamsXmlMap(responseXml);

            // 回复的消息必须有ToUserName和FromUserName
            if(paramsXmlMap.get("ToUserName") == null || "".equals(paramsXmlMap.get("ToUserName")))
            {
                flag = false;
            }
            else if(paramsXmlMap.get("FromUserName") == null || "".equals(paramsXmlMap.get("FromUserName")))
            {
                flag = false;
            }
            else
            {
                // 回复的是文本消息，消息的内容不能为空
                if("text".equals(paramsXmlMap.get("MsgType")))
                {
                    if(paramsXmlMap.get("Content") == null || "".equals(paramsXmlMap.get("Content")))
                    {
                        flag = false;
                    }
                }
                else if("news".equals(paramsXmlMap.get("MsgType")))
                {
                    // 回复 的是图文消息，消息的图文个数不能为空
                    if(paramsXmlMap.get("ArticleCount") == null || "".equals(paramsXmlMap.get("ArticleCount")))
                    {
                        flag = false;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 系统无法识别时的回复
     * 
     * @param toUser
     * @param fromUser
     * @return
     */
    public String getSystemDefaultResponseXml(HashMap<String, String> paramsXmlMap)
    {
        String toUser = paramsXmlMap.get("FromUserName");
        String fromUser = paramsXmlMap.get("ToUserName");
        StringBuffer msgXml = new StringBuffer();
        Date time = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        long createTime = cal.getTimeInMillis() / 1000;
        msgXml.append("<xml>");
        msgXml.append("<ToUserName><![CDATA[" + toUser + "]]></ToUserName>");
        msgXml.append("<FromUserName><![CDATA[" + fromUser + "]]></FromUserName>");
        msgXml.append("<CreateTime>" + String.valueOf(createTime) + "</CreateTime>");
        msgXml.append("<MsgType><![CDATA[text]]></MsgType>");
        msgXml.append("<Content><![CDATA[" + "尊敬的用户，您的回复已收录，请下次询问！" + "]]></Content>");
        msgXml.append("</xml>");
        return msgXml.toString();
    }

    /**
     * 系统关键字处理，如果没有匹配这返回""
     * 
     */
    public String getSystemKeywordMsgXml(String msg)
    {
        SinaStockService sinaStockService = new SinaStockService();
        return sinaStockService.getSingleStockMsgXml(msg);
    }

    /**
     * 对msgXml中basePath的处理，形如： content =
     * "...<PicUrl><![CDATA[{basePath}/login.png]]></PicUrl>...";
     * 
     * @param content
     * @return
     */
    public String handleMsgXmlBasePath(String content, String weixFilePath)
    {
        List<String> items = new ArrayList<String>();
        items.add("<PicUrl>(.*?)</PicUrl>");
        items.add("<Url>(.*?)</Url>");
        items.add("<MusicUrl>(.*?)</MusicUrl>");
        items.add("<HQMusicUrl>(.*?)</HQMusicUrl>");
        for (String item : items)
        {
            Pattern p = Pattern.compile(item);
            Matcher m = p.matcher(content);
            boolean flag = m.find();
            if(flag)
            {
                if(content != null)
                {
                    content = content.replace("{basePath}", weixFilePath);
                    content = content.replace("{weixFilePath}", weixFilePath);
                }
                else
                {
                    content = "";
                }
            }
        }
        return content;
    }

    /**
     * 文本消息msgXml的创建
     * 
     * @param rtextMsg
     * @return
     * @throws Exception
     */
    public static String createInitRtextMessage(String rtextMsg) throws Exception
    {
        StringBuffer msgXml = new StringBuffer();
        
        if(rtextMsg != null && !"".equals(rtextMsg))
        {
            msgXml.append("<xml>");
            msgXml.append("<ToUserName></ToUserName>");
            msgXml.append("<FromUserName></FromUserName>");
            msgXml.append("<CreateTime></CreateTime>");
            msgXml.append("<MsgType>text</MsgType>");
            msgXml.append("<Content><![CDATA[").append(initnullString(rtextMsg)).append("]]></Content>");
            msgXml.append("</xml>");
        }
        else
        {
            throw new Exception("参数rtextMsg不能为空!");
        }

        return msgXml.toString();
    }

    /**
     * 图文消息msgXMl的创建
     * 
     * @param rimgItemList
     * @return
     * @throws Exception
     */
    public static String createInitRimgMessage(List<LfWeiRimg> rimgItemList) throws Exception
    {
        StringBuffer msgXml = new StringBuffer();
        int articleCount = rimgItemList.size();
        String weixBasePath = GlobalMethods.getWeixFilePath();
        if(articleCount > 0)
        {
            msgXml.append("<xml>");
            msgXml.append("<ToUserName></ToUserName>");
            msgXml.append("<FromUserName></FromUserName>");
            msgXml.append("<CreateTime></CreateTime>");
            msgXml.append("<MsgType>news</MsgType>");
            msgXml.append("<ArticleCount>").append(articleCount).append("</ArticleCount>");
            msgXml.append("<Articles>");
            for (LfWeiRimg item : rimgItemList)
            {
                msgXml.append("<item>");
                msgXml.append("<Title><![CDATA[").append(initnullString(item.getTitle())).append("]]></Title>");
                msgXml.append("<PicUrl><![CDATA[").append(initnullString(item.getPicurl())).append("]]></PicUrl>");
                msgXml.append("<Description><![CDATA[").append(initnullString(item.getDescription())).append("]]></Description>");
                if("1".equals(item.getSourceUrl())&&!GlobalMethods.isInvalidString(item.getLink())){
                    msgXml.append("<Url><![CDATA[").append(initnullString(item.getLink())).append("]]></Url>");
                }else{
                    msgXml.append("<Url><![CDATA[").append("{basePath}weix_imgDetail.hts?rimgid=" + String.valueOf(item.getRimgId())).append("]]></Url>");
                }
                msgXml.append("</item>");
            }
            msgXml.append("</Articles>");
            msgXml.append("</xml>");
        }
        else
        {
            throw new Exception("文本消息的长度不能为空!");
        }

        return msgXml.toString();
    }

    /**
     * 空字符的处理
     * @param o
     * @return1
     */
    private static String initnullString(String o)
    {
        boolean b = GlobalMethods.isInvalidString(o);
        return b ? "" : o.toString();
    }

    /**
     * 使用SHA算法加密
     * 
     * @param convertme
     * @return
     * @throws NoSuchAlgorithmException
     */
    private String SHAsum(byte[] convertme) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return byteArray2Hex(md.digest(convertme));
    }

    /**
     * @param hash
     * @return
     */
    private String byteArray2Hex(final byte[] hash)
    {
    	Formatter formatter = null;
    	try {
    		formatter = new Formatter();
    	    for (byte b : hash)
    	    {
    	    	formatter.format("%02x", b);
    	    }
    	    return formatter.toString();
		} catch (Exception e) {
			EmpExecutionContext.error(e,"byteArray2Hex");
			return "";
		}finally{
			if(formatter != null){
				formatter.close();
			}
		}
       
    }

    /**
     * type(0:上行;1:下行)
     * msgType(0：文本消息；
     *     1：图片消息；
     *     2：地理位置消息；
     *     3：链接消息；
     *     4：事件推送；
     *     5：回复文本；
     *     6：回复图文；
     *     7：回复语音消息
     *     8: 上行声音)
     * 
     * @param type
     */
    public Integer getMsgType(String msgType, String type)
    {
        Integer msgTp = null;
        LinkedHashMap<String, Integer> lMsgTypeMap = getLMsgTypeMap();
        LinkedHashMap<String, Integer> rMsgTypeMap = getRMsgTypeMap();

        if("0".equals(type))
        {
            msgTp = lMsgTypeMap.get(msgType);
        }
        else if("1".equals(type))
            {
                msgTp = rMsgTypeMap.get(msgType);
            }
        return msgTp;
    }

    /**
     * 上行消息类型
     * 
     * @return
     */
    private LinkedHashMap<String, Integer> getLMsgTypeMap()
    {
        LinkedHashMap<String, Integer> lMsgTypeMap = new LinkedHashMap<String, Integer>();
        lMsgTypeMap.put("text", 0);
        lMsgTypeMap.put("image", 1);
        lMsgTypeMap.put("location", 2);
        lMsgTypeMap.put("link", 3);
        lMsgTypeMap.put("event", 4);
        lMsgTypeMap.put("voice", 8);
        return lMsgTypeMap;
    }

    /**
     * 下行消息类型
     * 
     * @return
     */
    private LinkedHashMap<String, Integer> getRMsgTypeMap()
    {
        LinkedHashMap<String, Integer> lMsgTypeMap = new LinkedHashMap<String, Integer>();
        lMsgTypeMap.put("text", 5);
        lMsgTypeMap.put("music", 6);
        lMsgTypeMap.put("news", 7);
        return lMsgTypeMap;
    }
    
    /**
     * 
     * @description    解析微信上行XML文件
     * @param request  请求 
     * @return          HashMap<String, String> 或者NULL           
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-20 下午05:20:40
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, String>  analyzeWeixinXML(HttpServletRequest request){
        
        HashMap<String, String> paramsXmlMap = null;
        try
        {
            BufferedReader reader = request.getReader();
            String temp = null;
            StringBuffer requestXml = new StringBuffer();
            while((temp = reader.readLine()) != null)
            {
                requestXml.append(temp);
            }
            //System.out.println(requestXml.toString());
            if(requestXml.toString().length()>0){
                temp = null; 
                paramsXmlMap = new HashMap<String, String>();
                // 转码，解决乱码的问题
                temp = new String(requestXml.toString().getBytes("iso8859-1"), "UTF-8");
                //将请求写文件
                new TxtFileUtil().writeWeixMsgToFile("request", temp);
                // 获取微信服务发送请求，并返回第一层级json格式数据
                Document document = DocumentHelper.parseText(temp);
                Element items = document.getRootElement();
                for (Iterator i = items.elementIterator(); i.hasNext();)
                {
                    Element item = (Element) i.next();
                    paramsXmlMap.put(item.getName(), item.getText());
                   // EmpExecutionContext.error(item.getName() + ":" + item.getText());
                }
            }
        }
        catch (Exception e)
        {
            paramsXmlMap = null;
            EmpExecutionContext.error(e,"WxCommBiz.analyzeWeixinXML is error");
        }
        return paramsXmlMap;
    }
    
    
    /**
     * 
     * @description    关注 处理回复默认信息
     * @param paramsXmlMap  储存MAP信息
     * @param returnmsg     需要下发的信息内容
     * @return                   
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2014-3-3 下午04:59:00
     */
   public String getDefaultResponseXml(HashMap<String, String> paramsXmlMap,String returnmsg)
    {
        String toUser = paramsXmlMap.get("FromUserName");
        String fromUser = paramsXmlMap.get("ToUserName");
        StringBuffer msgXml = new StringBuffer();
        msgXml.append("<xml>");
        msgXml.append("<ToUserName><![CDATA[" + toUser + "]]></ToUserName>");
        msgXml.append("<FromUserName><![CDATA[" + fromUser + "]]></FromUserName>");
        msgXml.append("<CreateTime>" + String.valueOf(System.currentTimeMillis()) + "</CreateTime>");
        msgXml.append("<MsgType><![CDATA[text]]></MsgType>");
        msgXml.append("<Content><![CDATA[" + returnmsg + "]]></Content>");
        msgXml.append("</xml>");
        return msgXml.toString();
    }
}
