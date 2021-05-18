package com.montnets.emp.wxgl.svt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiGroup;
import com.montnets.emp.entity.wxgl.LfWeiRimg;
import com.montnets.emp.entity.wxgl.LfWeiSendlog;
import com.montnets.emp.entity.wxgl.LfWeiTemplate;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.ottbase.biz.WeixBiz;
import com.montnets.emp.ottbase.param.WeixParams;
import com.montnets.emp.ottbase.service.WeixService;
import com.montnets.emp.ottbase.svt.BaseServlet;
import com.montnets.emp.ottbase.util.StringUtils;
import com.montnets.emp.wxgl.biz.QunfaBiz;
import com.montnets.emp.wxgl.user.util.DateHelper;

public class weix_qunfaMgrSvt extends BaseServlet
{
    // 基础逻辑层
	private final BaseBiz                     baseBiz      = new BaseBiz();

    private static final String PATH         = "/wxgl/qunfa";

    private final   QunfaBiz                    qunfa        = new QunfaBiz();

    // 时间间隔两天半
    private final long                timeInterval = (long) 5 * 24 * 60 * 30 * 1000;

    private final  WeixService                 weixService  = new WeixService();

    // 分组群发
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            String corpCode = request.getParameter("lgcorpcode");
            // 查询所有公众帐号
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", corpCode);
            List<LfWeiAccount> otWeiAcctList = baseBiz.findListByCondition(LfWeiAccount.class, conditionMap, null);

            // 本日开始时间、结束时间
            String todayStartTime = DateHelper.FormatTimeStamp("", DateHelper.FromToday());
            String todayEndTime = DateHelper.FormatTimeStamp("", DateHelper.ToToday());

            // 本月开始时间、结束时间
            String monthStartTime = DateHelper.FormatTimeStamp("", DateHelper.FromMonth());
            String monthEndTime = DateHelper.FormatTimeStamp("", DateHelper.ToMonth());
            int tcount = qunfa.getCount(todayStartTime, todayEndTime, null, corpCode);
            int mcount = qunfa.getCount(monthStartTime, monthEndTime, null, corpCode);
            request.setAttribute("tcount", tcount);
            request.setAttribute("mcount", mcount);
            request.setAttribute("otWeiAcctList", otWeiAcctList);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "用户群发页面加载失败！");
        }
        finally
        {
            request.getRequestDispatcher(PATH + "/yxgl_qunfa.jsp").forward(request, response);
        }
    }

    // 通过公众帐号获取对应的用户组
    public void getGroups(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String result = "";
        response.setContentType("text/html");
        PrintWriter out = null;
        // openId列表
        List<DynaBean> openIds = new ArrayList<DynaBean>();
        try
        {
            String aid = request.getParameter("aid");
            if("".equals(aid) || null == aid)
            {
                result = "error";
            }
            else
            {
                // 存放查询条件
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("AId", aid);
                ArrayList<LfWeiGroup> otweiGroups = (ArrayList<LfWeiGroup>) baseBiz.getByCondition(LfWeiGroup.class, conditionMap, null);
                JSONObject resultJson = new JSONObject();
                resultJson.put("000", "全部");
                for (LfWeiGroup group : otweiGroups)
                {
                    resultJson.put(group.getGId(), group.getName() + "(" + (group.getCount() == null ? "0" : group.getCount()) + ")");
                }

                // 3天前的时间+15分钟
                Calendar timebeforeThereDays = Calendar.getInstance();
                timebeforeThereDays.add(Calendar.DAY_OF_MONTH, -3);
                timebeforeThereDays.add(Calendar.MINUTE, 15);
                String timeConsition = StringUtils.timeFormat(timebeforeThereDays.getTime());

                openIds = qunfa.getOpenIdByThereDaysBefore(aid, timeConsition);

                resultJson.put("0", "在线客服用户组(" + openIds.size() + ")");
                result = resultJson.toString();
            }

        }
        catch (Exception ex)
        {
            result = "error";
        }
        finally
        {
            out = response.getWriter();
            out.print(result);
            out.close();
        }
    }

    // 通过公众帐号获取对应的用户组
    public void getAreas(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String result = "";
        response.setContentType("text/html");
        request.setCharacterEncoding("utf-8");
        PrintWriter out = null;
        try
        {
            String corpCode = request.getParameter("corpCode");
            String aid = request.getParameter("aid");
            // 请求类型(country:国家，province：省，city：城市)
            String tp = request.getParameter("tp");
            // 类型的值，比如：tp='city',tpvalue='广东',查询广东的所有城市
            String tpvalue = request.getParameter("tpvalue");
            if("".equals(corpCode) || null == corpCode || "".equals(aid) || null == aid)
            {
                result = "error";
            }
            else
            {
                // 存放查询条件
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("AId", aid);
                conditionMap.put("tp", tp);
                conditionMap.put("tpvalue", tpvalue);

                JSONObject resultJson = new QunfaBiz().getAreas(corpCode, conditionMap);
                result = resultJson.toString();
            }
        }
        catch (Exception ex)
        {
            result = "error";
            EmpExecutionContext.error(ex.getMessage());
        }
        finally
        {
            out = response.getWriter();
            out.print(result);
            out.close();
        }
    }

    // 通过公众帐号获取对应的用户组
    public void getCount(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String result = "";
        response.setContentType("text/html");
        request.setCharacterEncoding("utf-8");
        PrintWriter out = null;
        try
        {
            String corpCode = request.getParameter("corpCode");
            String aid = request.getParameter("aid");
            // 本日开始时间、结束时间
            String todayStartTime = DateHelper.FormatTimeStamp("", DateHelper.FromToday());
            String todayEndTime = DateHelper.FormatTimeStamp("", DateHelper.ToToday());

            // 本月开始时间、结束时间
            String monthStartTime = DateHelper.FormatTimeStamp("", DateHelper.FromMonth());
            String monthEndTime = DateHelper.FormatTimeStamp("", DateHelper.ToMonth());

            if("".equals(corpCode) || null == corpCode)
            {
                result = "error";
            }
            else
            {
                JSONObject resultJson = new JSONObject();
                int tcount = qunfa.getCount(todayStartTime, todayEndTime, aid, corpCode);
                int mcount = qunfa.getCount(monthStartTime, monthEndTime, aid, corpCode);
                resultJson.put("tcount", tcount);
                resultJson.put("mcount", mcount);
                result = resultJson.toString();
            }
        }
        catch (Exception ex)
        {
            result = "error";
            EmpExecutionContext.error(ex.getMessage());
        }
        finally
        {
            out = response.getWriter();
            out.print(result);
            out.close();
        }
    }

    // 群组群发
    public void sendAll(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // 群发历史消息记录
        LfWeiSendlog sendlog = new LfWeiSendlog();
        // 返回的结果
        String result = "";
        response.setContentType("text/html");
        PrintWriter out = null;
        // 群发历史记录
        try
        {
            // 企业编号
            String corpCode = request.getParameter("corpCode");
            // 公众帐号ID
            String aid = request.getParameter("aid");
            // 群组ID(默认为"000")
            String gid = request.getParameter("gid");
            // 消息类型(文本，图文)
            String msgtype = request.getParameter("msgtype");
            // 发送对象类型(0：分组；1：地区)
            String tp = request.getParameter("tp");
            // 查询条件初始化
            LinkedHashMap conditionMap = new LinkedHashMap();
            // openId列表
            JSONArray openIds = new JSONArray();

            // 参数为空判断
            if(StringUtils.isBlank(corpCode) || StringUtils.isBlank(aid) || StringUtils.isBlank(gid) || StringUtils.isBlank(msgtype) || StringUtils.isBlank(tp))
            {
                result = "parameterError";
                throw new Exception("参数为空异常！-Yxgl_qunfaMgrSvt#sendAll");
            }

            if("0".equals(tp))
            {
                // 如果是分组，获取分组对应的所有用户的openId列表
                openIds = qunfa.getOpenIdByGroup(corpCode, aid, gid);
            }
            else if("1".equals(tp))
            {
                // 如果是地区，获取地区对应的所有用户的openId列表
                // 地区发送
                String areaid = request.getParameter("country");
                String province = request.getParameter("province");
                String city = request.getParameter("city");
                areaid = (areaid == null || "0".equals(areaid)) ? "" : areaid;
                province = province == null ? "" : province;
                city = city == null ? "" : city;
                sendlog.setAreaValue(areaid + "," + province + "," + city);
                openIds = qunfa.getOpenIdByArea(corpCode, aid, areaid, province, city);
                if(null == openIds || openIds.isEmpty())
                {
                    throw new Exception("通过地区获取关注者openId列表失败！-Yxgl_qunfaMgrSvt#sendAll");
                }
            }

            if(openIds == null||openIds.size() == 0){
                result = "nomember";
                /*修改 pengj   这里可以不用new一个异常，采取下面的将异常改为INFO级别
                throw new Exception("发送人员为空！-Yxgl_qunfaMgrSvt#sendAll");
                */
                EmpExecutionContext.info("发送人员为空！-Yxgl_qunfaMgrSvt#sendAll");
            }
            
            WeixParams weixParams = new WeixParams();
            weixParams.setMsgtype(msgtype);
            weixParams.setGroupId(gid);

            // 第一步： 获取accessToken
            String accessToken = (new WeixBiz()).getToken(aid);
            weixParams.setAccess_token(accessToken);

            // ==获取POST提交的json格式数据开始==
            if("text".equals(msgtype))
            {
                // 文本消息
                String content = request.getParameter("content");
                weixParams.setContent(content);
                sendlog.setSendContent(content);

                // 根据OpenID列表群发
                weixParams.setData(qunfa.getOpenIdSendData(openIds, weixParams));
            }
            else if("mpnews".equals(msgtype))
            {
                // 图文消息
                String tid = request.getParameter("tid");// 回复图文ID
                // 获取模板
                LfWeiTemplate otWeiTemplate = baseBiz.getById(LfWeiTemplate.class, tid);
                sendlog.setTId(Long.valueOf(tid));
                sendlog.setSendContent(otWeiTemplate.getName());
                // 获取图文明细
                conditionMap.clear();
                conditionMap.put("rimgId&in", otWeiTemplate.getRimgids());
                ArrayList<LfWeiRimg> otWeiRimgs = (ArrayList<LfWeiRimg>) baseBiz.getByCondition(LfWeiRimg.class, conditionMap, null);

                if(null != otWeiRimgs && otWeiRimgs.size() > 0)
                {
                    LinkedHashMap<String, LfWeiRimg> mediaRimgMap = qunfa.uploadImagesToWeixinServer(aid, otWeiRimgs, accessToken);
                    String articlesJson = qunfa.convertToArticlesJson(mediaRimgMap);
                    String media_id = qunfa.uploadArticleToWeixinServer(aid, articlesJson, accessToken);
                    if(null == media_id || "".equals(media_id))
                    {
                        throw new Exception("上传群发图文素材失败！");
                    }
                    else
                    {
                        weixParams.setMedia_id(media_id);
                        // 根据OpenID列表群发
                        weixParams.setData(qunfa.getOpenIdSendData(openIds, weixParams));
                    }
                }
            }
            else
            {
                result = "msgTypeError";
                throw new Exception("消息类型不正确！");
            }
            
            // ==获取POST提交的json格式数据结束==
            // 发送类型(0：分组群发，1：按地区发送)
            sendlog.setTp(tp);
            // 消息的类型(文本：text,图文：mpnews)
            sendlog.setMsgType(msgtype);
            // 提交的数据
            sendlog.setPostMsg(weixParams.getData() == null ? "" : weixParams.getData());
            sendlog.setAId(Long.valueOf(aid));
            sendlog.setCorpCode(corpCode);
            // 设置日期格式(new Date()为获取当前系统时间)
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sendlog.setCreatetime(Timestamp.valueOf(df.format(new Date())));

            // 群发提交数据
            weixParams = weixService.sendAll(weixParams);

            if(weixParams != null && "000".equals(weixParams.getErrCode()))
            {
                // 获取JSON中的数据
                JSONObject obj = weixParams.getJsonObj();
                sendlog.setStatus(1);
                sendlog.setMsgId(String.valueOf(obj.get("msg_id")));
                sendlog.setResponseMsg(obj.toString());
                result = "success";
                baseBiz.addObj(sendlog);
            }
            else
            {
                result = "error";
                sendlog.setStatus(0);
                baseBiz.addObj(sendlog);
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, e.getMessage());
        }
        finally
        {
            out = response.getWriter();
            out.print(result);
            out.close();
        }
    }

    // 重新选择发送范围
    public void reSendAll(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        // 获取需要重发的历史消息
        String sendlogId = request.getParameter("sendlogId");
        String corpCode = request.getParameter("lgcorpcode");
        try
        {
            LfWeiSendlog sendlog = baseBiz.getById(LfWeiSendlog.class, sendlogId);
            // 查询所有公众帐号
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("corpCode", corpCode);
            List<LfWeiAccount> otWeiAcctList = baseBiz.findListByCondition(LfWeiAccount.class, conditionMap, null);

            // 本日开始时间、结束时间
            String todayStartTime = DateHelper.FormatTimeStamp("", DateHelper.FromToday());
            String todayEndTime = DateHelper.FormatTimeStamp("", DateHelper.ToToday());

            // 本月开始时间、结束时间
            String monthStartTime = DateHelper.FormatTimeStamp("", DateHelper.FromMonth());
            String monthEndTime = DateHelper.FormatTimeStamp("", DateHelper.ToMonth());
            int tcount = qunfa.getCount(todayStartTime, todayEndTime, null, corpCode);
            int mcount = qunfa.getCount(monthStartTime, monthEndTime, null, corpCode);

            request.setAttribute("tcount", tcount);
            request.setAttribute("mcount", mcount);
            request.setAttribute("sendlog", sendlog);
            request.setAttribute("otWeiAcctList", otWeiAcctList);

        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "重新选择发送范围失败。-Yxgl_qunfaMgrSvt#reChooseTp");
        }
        finally
        {
            request.getRequestDispatcher(PATH + "/yxgl_reSendAll.jsp").forward(request, response);
        }
    }

    // 重新发送
    public void doRepeatSendAll(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
     // 群发历史消息记录
        LfWeiSendlog sendlog = new LfWeiSendlog();
        // 返回的结果
        String result = "";
        response.setContentType("text/html");
        PrintWriter out = null;
        // 群发历史记录
        try
        {
            // 当前历史群发历史消息
            String sendlogId = request.getParameter("sendlogId");
            // 群组ID(默认为"000")
            String gid = request.getParameter("gid");
            // 发送对象类型(0：分组；1：地区)
            String tp = request.getParameter("tp");
            // 公众帐号id
            String aid = request.getParameter("aid");
            // 查询条件初始化
            LinkedHashMap conditionMap = new LinkedHashMap();
            // openId列表
            JSONArray openIds = new JSONArray();

            // 参数为空判断
            if(StringUtils.isBlank(aid) || StringUtils.isBlank(sendlogId) || StringUtils.isBlank(gid) || StringUtils.isBlank(tp))
            {
                result = "parameterError";
                throw new Exception("参数为空异常！-Yxgl_qunfaMgrSvt#doRepeatSendAll");
            }

            // 获取当前的群发历史消息
            sendlog = baseBiz.getById(LfWeiSendlog.class, sendlogId);
            String corpCode = sendlog.getCorpCode();
            

            // 消息类型(文本，图文)
            String msgtype = sendlog.getMsgType();
            
            if("0".equals(tp))
            {
                // 如果是分组，获取分组对应的所有用户的openId列表
                openIds = qunfa.getOpenIdByGroup(corpCode, aid, gid);
            }
            else if("1".equals(tp))
            {
                // 如果是地区，获取地区对应的所有用户的openId列表
                // 地区发送
                String areaid = request.getParameter("country");
                String province = request.getParameter("province");
                String city = request.getParameter("city");
                areaid = (areaid == null || "0".equals(areaid)) ? "" : areaid;
                province = province == null ? "" : province;
                city = city == null ? "" : city;
                sendlog.setAreaValue(areaid + "," + province + "," + city);
                openIds = qunfa.getOpenIdByArea(corpCode, aid, areaid, province, city);
                if(null == openIds || openIds.isEmpty())
                {
                    throw new Exception("通过地区获取关注者openId列表失败！-Yxgl_qunfaMgrSvt#sendAll");
                }
            }

            if(openIds == null||openIds.size() == 0){
                result = "nomember";
                throw new Exception("发送人员为空！-Yxgl_qunfaMgrSvt#sendAll");
            }
            
            WeixParams weixParams = new WeixParams();
            weixParams.setMsgtype(msgtype);
            weixParams.setGroupId(gid);

            // 第一步： 获取accessToken
            String accessToken = (new WeixBiz()).getToken(aid);
            weixParams.setAccess_token(accessToken);

            // ==获取POST提交的json格式数据开始==
            if("text".equals(msgtype))
            {
                // 文本消息
                String content = sendlog.getSendContent() == null ? "" : sendlog.getSendContent();
                weixParams.setContent(content);

                // 根据OpenID列表群发
                weixParams.setData(qunfa.getOpenIdSendData(openIds, weixParams));
            }
            else if("mpnews".equals(msgtype))
            {
             // 当前时间
                long currentTime = System.currentTimeMillis();
                Timestamp createTime = sendlog.getCreatetime();
                // 两天半之后
                if(currentTime - createTime.getTime() - timeInterval > 0)
                {
                    // 图文消息
                    String tid = String.valueOf(sendlog.getTId());// 回复图文ID
                    // 获取模板
                    LfWeiTemplate otWeiTemplate = baseBiz.getById(LfWeiTemplate.class, tid);
                    // 获取图文明细
                    conditionMap.clear();
                    conditionMap.put("rimgId&in", otWeiTemplate.getRimgids());
                    ArrayList<LfWeiRimg> otWeiRimgs = (ArrayList<LfWeiRimg>) baseBiz.getByCondition(LfWeiRimg.class, conditionMap, null);

                    if(null != otWeiRimgs && otWeiRimgs.size() > 0)
                    {
                        LinkedHashMap<String, LfWeiRimg> mediaRimgMap = qunfa.uploadImagesToWeixinServer(aid, otWeiRimgs, accessToken);
                        String articlesJson = qunfa.convertToArticlesJson(mediaRimgMap);
                        String media_id = qunfa.uploadArticleToWeixinServer(aid, articlesJson, accessToken);

                        if(null == media_id || "".equals(media_id))
                        {
                            throw new Exception("上传群发图文素材失败！");
                        }
                        else
                        {
                            weixParams.setMedia_id(media_id);
                            weixParams.setData(qunfa.getOpenIdSendData(openIds, weixParams));
                        }
                    }
                }
                else
                {
                    weixParams.setData(sendlog.getPostMsg());
                }
            }
            else
            {
                result = "msgTypeError";
                throw new Exception("消息类型不正确！");
            }
            
            // ==获取POST提交的json格式数据结束==
            // 发送类型(0：分组群发，1：按地区发送)
            sendlog.setTp(tp);
            // 消息的类型(文本：text,图文：mpnews)
            sendlog.setMsgType(msgtype);
            // 提交的数据
            sendlog.setPostMsg(weixParams.getData() == null ? "" : weixParams.getData());
            sendlog.setAId(Long.valueOf(aid));
            sendlog.setCorpCode(corpCode);
            // 设置日期格式(new Date()为获取当前系统时间)
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sendlog.setCreatetime(Timestamp.valueOf(df.format(new Date())));

            // 群发提交数据
            weixParams = weixService.sendAll(weixParams);

            if(weixParams != null && "000".equals(weixParams.getErrCode()))
            {
                // 获取JSON中的数据
                JSONObject obj = weixParams.getJsonObj();
                sendlog.setStatus(1);
                sendlog.setMsgId(String.valueOf(obj.get("msg_id")));
                sendlog.setResponseMsg(obj.toString());
                result = "success";
                baseBiz.addObj(sendlog);
            }
            else
            {
                result = "error";
                sendlog.setStatus(0);
                baseBiz.addObj(sendlog);
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, e.getMessage());
        }
        finally
        {
            out = response.getWriter();
            out.print(result);
            out.close();
        }
    }

    // 在线客服群发
    public void customerSendAll(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        // 群发历史消息记录
        LfWeiSendlog sendlog = new LfWeiSendlog();
        // 返回的结果
        String result = "";
        // 群发返回的统计信息
        JSONObject responseMsg = new JSONObject();
        // 群发提交信息
        JSONObject postMsg = new JSONObject();
        response.setContentType("text/html");
        PrintWriter out = null;
        // 群发历史记录
        try
        {
            String corpCode = request.getParameter("corpCode");
            // 公众帐号ID
            String aid = request.getParameter("aid");
            // 消息类型(文本，图文)
            String msgtype = request.getParameter("msgtype");
            // 查询条件初始化
            LinkedHashMap conditionMap = new LinkedHashMap();
            // openId列表
            List<DynaBean> openIds = new ArrayList<DynaBean>();

            // 参数为空判断
            if(StringUtils.isBlank(aid) || StringUtils.isBlank(msgtype))
            {
                result = "parameterError";
                throw new Exception("参数为空异常！-Yxgl_qunfaMgrSvt#chatSendAll");
            }

            WeixParams weixParams = new WeixParams();
            weixParams.setMsgtype(msgtype);

            // 第一步： 获取accessToken
            String accessToken = (new WeixBiz()).getToken(aid);
            weixParams.setAccess_token(accessToken);

            // 3天前的时间+15分钟
            Calendar timebeforeThereDays = Calendar.getInstance();
            timebeforeThereDays.add(Calendar.DAY_OF_MONTH, -3);
            timebeforeThereDays.add(Calendar.MINUTE, 15);
            String timeConsition = StringUtils.timeFormat(timebeforeThereDays.getTime());

            openIds = qunfa.getOpenIdByThereDaysBefore(aid, timeConsition);

            // ==获取POST提交的json格式数据开始==
            if("text".equals(msgtype))
            {
                // 文本消息
                String content = request.getParameter("content");
                weixParams.setContent(content);
                sendlog.setSendContent(content);
                responseMsg = weixService.batchSendCustomerMsg(openIds, weixParams);
                postMsg.put("content", content);
            }
            else if("mpnews".equals(msgtype))
            {
                // 图文消息
                String tid = request.getParameter("tid");// 回复图文ID
                // 获取模板
                LfWeiTemplate otWeiTemplate = baseBiz.getById(LfWeiTemplate.class, tid);
                sendlog.setTId(Long.valueOf(tid));
                sendlog.setSendContent(otWeiTemplate.getName());
                // 获取图文明细
                conditionMap.clear();
                conditionMap.put("rimgId&in", otWeiTemplate.getRimgids());
                ArrayList<LfWeiRimg> otWeiRimgs = (ArrayList<LfWeiRimg>) baseBiz.getByCondition(LfWeiRimg.class, conditionMap, null);
                JSONArray articles = qunfa.convertToCustomerArticles(otWeiRimgs);
                weixParams.setMsgtype("news");
                weixParams.setArticles(articles);
                responseMsg = weixService.batchSendCustomerMsg(openIds, weixParams);
                postMsg.put("news", articles);
            }
            else
            {
                result = "msgTypeError";
                throw new Exception("消息类型不正确！");
            }

            // ==获取POST提交的json格式数据结束==
            // 发送类型(0：分组群发，1：按地区发送)
            sendlog.setTp("0");
            // 消息的类型(文本：text,图文：mpnews)
            sendlog.setMsgType(msgtype);
            // 提交的数据
            postMsg.put("groupid", "0");
            postMsg.put("touser", responseMsg.get("openids"));
            postMsg.put("msgtype", msgtype);
            sendlog.setPostMsg(postMsg.toString());
            sendlog.setAId(Long.valueOf(aid));
            sendlog.setCorpCode(corpCode);
            sendlog.setResponseMsg(responseMsg.toString());
            // 设置日期格式(new Date()为获取当前系统时间)
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sendlog.setCreatetime(Timestamp.valueOf(df.format(new Date())));

            if(openIds != null && openIds.size() > 0 && responseMsg.get("totalCount") != "" && !"0".equals(String.valueOf(responseMsg.get("totalCount"))))
            {
                sendlog.setStatus(2);
                responseMsg.put("errcode", "000");
                result = responseMsg.toString();
            }
            else
            {
                result = "error";
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, e.getMessage());
        }
        finally
        {
            try
            {
                if("error".equals(result))
                {
                    sendlog.setStatus(0);
                }
                baseBiz.addObj(sendlog);
            }
            catch (Exception ex)
            {
                EmpExecutionContext.error(ex, "保存sendlog对象失败！-Yxgl_qunfaMgrSvt#sendAll");
            }
            out = response.getWriter();
            out.print(result);
            out.close();
        }
    }

}
