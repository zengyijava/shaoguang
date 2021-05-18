package com.montnets.emp.ottbase.service;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.ottbase.biz.WeixBiz;
import com.montnets.emp.ottbase.constant.WXStaticValue;
import com.montnets.emp.ottbase.constant.WeixHttpUrl;
import com.montnets.emp.ottbase.constant.WeixMessage;
import com.montnets.emp.ottbase.param.HttpReturnParams;
import com.montnets.emp.ottbase.param.WeixParams;
import com.montnets.emp.ottbase.util.StringUtils;
import com.montnets.emp.ottbase.util.TxtFileUtil;
import com.montnets.emp.wxgl.biz.QunfaBiz;

/**
 * @description 微信接口调用
 * @project ott
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-11-20 上午11:19:54
 */
public class WeixService implements IWeixinService
{ 
    //群发逻辑层
    QunfaBiz qunfa = new QunfaBiz();
    /**
     * @description 参数传递转换
     * @param weixParams
     * @return
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-20 上午11:24:24
     */
    private void changeParams(HttpReturnParams params, WeixParams weixParams) throws Exception
    {
        try
        {
            weixParams.setErrCode(params.getErrCode());
            weixParams.setErrMsg(params.getErrMsg());
            if(weixParams.getErrCode() != null && "000".equals(weixParams.getErrCode()))
            {
                // 返回JSON格式
                if(params.getJsonObject() != null)
                {
                    weixParams.setJsonObj(params.getJsonObject());
                }
                // 返回XML格式
                if(params.getReturnXml() != null && !"".equals(params.getReturnXml()))
                {
                    weixParams.setReturnXml(params.getReturnXml());
                }
                // 下载文件名称的相对路径
                if(params.getFileUrl() != null && !"".equals(params.getFileUrl()))
                {
                    weixParams.setFileUrl(params.getFileUrl());
                }
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixinService.changeParams Is Error");
        }
    }

    /**
     * @description 发送客服接口
     * @param weixParams
     *        微信内容参数类
     * @return 成功 ErrCode 000 ErrMsg success 失败 -9999 ErrMsg其他
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-20 上午11:24:24
     */
    @SuppressWarnings("unchecked")
    public WeixParams sendCustomerMsg(WeixParams weixParams) throws Exception
    {
        try
        {
            // 判断消息类型
            if(weixParams.getMsgtype() == null || "".equals(weixParams.getMsgtype()))
            {
                weixParams.setErrMsg("WeixinService.sendCustomerMsg.Msgtype Is Null");
                return weixParams;
            }
            // 判断普通用户的openid
            if(weixParams.getTouser() == null || "".equals(weixParams.getTouser()))
            {
                weixParams.setErrMsg("WeixinService.sendCustomerMsg.Touser Is Null");
                return weixParams;
            }
            // 判断Access_token
            if(weixParams.getAccess_token() == null || "".equals(weixParams.getAccess_token()))
            {
                weixParams.setErrMsg("WeixinService.sendCustomerMsg.Access_token Is Null");
                return weixParams;
            }
            JSONObject secondaryObj = new JSONObject();
            // 判断消息类型 文本 text/ 图片 image/语音 & 视频 voice/音乐 music/ 图文news
            if("text".equals(weixParams.getMsgtype()))
            {
                // 文本内容
                if(weixParams.getContent() == null || "".equals(weixParams.getContent()))
                {
                    weixParams.setErrMsg("WeixinService.sendCustomerMsg.Content Is Null");
                    return weixParams;
                }
                secondaryObj.put("content", weixParams.getContent());
            }
            else if("image".equals(weixParams.getMsgtype()))
            {
                // 图片Media_id
                if(weixParams.getMedia_id() == null || "".equals(weixParams.getMedia_id()))
                {
                    weixParams.setErrMsg("WeixinService.sendCustomerMsg.Media_id Is Null");
                    return weixParams;
                }
                secondaryObj.put("media_id", weixParams.getMedia_id());
            }
            else if("voice".equals(weixParams.getMsgtype()))
            {
                // 音频Media_id
                if(weixParams.getMedia_id() == null || "".equals(weixParams.getMedia_id()))
                {
                    weixParams.setErrMsg("WeixinService.sendCustomerMsg.Media_id Is Null");
                    return weixParams;
                }
                secondaryObj.put("media_id", weixParams.getMedia_id());
            }
            else if("video".equals(weixParams.getMsgtype()))
            {
                // 判断Thumb_media_id
                if(weixParams.getThumb_media_id() == null || "".equals(weixParams.getThumb_media_id()))
                {
                    weixParams.setErrMsg("WeixinService.sendCustomerMsg.Thumb_media_id Is Null");
                    return weixParams;
                }
                // 音频Media_id
                if(weixParams.getMedia_id() == null || "".equals(weixParams.getMedia_id()))
                {
                    weixParams.setErrMsg("WeixinService.sendCustomerMsg.Media_id Is Null");
                    return weixParams;
                }
                secondaryObj.put("thumb_media_id", weixParams.getThumb_media_id());
                secondaryObj.put("media_id", weixParams.getMedia_id());
            }
            else if("music".equals(weixParams.getMsgtype()))
            {
                secondaryObj.put("title", weixParams.getTitle());
                secondaryObj.put("description", weixParams.getDescription());
                // 音乐链接
                if(weixParams.getMusicurl() == null || "".equals(weixParams.getMusicurl()))
                {
                    weixParams.setErrMsg("WeixinService.sendCustomerMsg.Musicurl Is Null");
                    return weixParams;
                }
                // 高品质音乐链接
                if(weixParams.getHqmusicurl() == null || "".equals(weixParams.getHqmusicurl()))
                {
                    weixParams.setErrMsg("WeixinService.sendCustomerMsg.Hqmusicurl Is Null");
                    return weixParams;
                }
                // 缩略图的媒体ID
                if(weixParams.getThumb_media_id() == null || "".equals(weixParams.getThumb_media_id()))
                {
                    weixParams.setErrMsg("WeixinService.sendCustomerMsg.Thumb_media_id Is Null");
                    return weixParams;
                }
                secondaryObj.put("musicurl", weixParams.getMusicurl());
                secondaryObj.put("hqmusicurl", weixParams.getHqmusicurl());
                secondaryObj.put("thumb_media_id", weixParams.getThumb_media_id());
            }
            else if("news".equals(weixParams.getMsgtype()))
            {
                // 图文
                secondaryObj.put("articles", weixParams.getArticles());
            }
            else
            {
                weixParams.setErrMsg("WeixinService.sendCustomerMsg.Msgtype Is Errer");
                return weixParams;
            }

            JSONObject seniorObj = new JSONObject();
            // 消息类型 二级OBJ
            seniorObj.put(weixParams.getMsgtype(), secondaryObj);
            seniorObj.put("touser", weixParams.getTouser());
            seniorObj.put("msgtype", weixParams.getMsgtype());
            // 发送客服消息URL
            String url = WeixHttpUrl.WX_SENDCUSTOM_URL + "access_token=" + weixParams.getAccess_token();
            // 请求http返回参数
            HttpReturnParams httpparams = new HttpReturnParams();
            httpparams.setUrl(url);
            // POST请求
            httpparams.setRequestType("POST");
            // 输入流
            httpparams.setDoInput(true);
            // 输出流
            httpparams.setDoOutput(true);
            
            String jsonmsg = seniorObj.toString();
            
            if(jsonmsg.indexOf("\\u201C") > -1){
            	jsonmsg = jsonmsg.replace("\\u201C", "“");
            }
            if(jsonmsg.indexOf("\\u201D") > -1){
            	jsonmsg = jsonmsg.replace("\\u201D", "”");
            }

            // 将JSON格式转化STRING
            httpparams.setPostMsg(jsonmsg);
            // 设置返回格式
            httpparams.setReturnType(WXStaticValue.RETURNHTTP_JSON);
            // 发送请求
            httpparams = new HttpsRequestService().HandleHttpsRequest(httpparams);
            this.changeParams(httpparams, weixParams);
        }
        catch (Exception e)
        {
            weixParams.setErrMsg("error");
            weixParams.setErrCode("-9999");
            EmpExecutionContext.error(e, "WeixinService.sendCustomerMsg Is Error");
        }
        return weixParams;
    }

    /**
     * @description
     * @param openIds
     * @param weixParams
     * @return
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年6月6日 上午11:40:25
     */
    public JSONObject batchSendCustomerMsg(List<DynaBean> openIds, WeixParams weixParams)
    {
        JSONObject result = new JSONObject();
        JSONArray openIdArray = new JSONArray();
        result.put("totalCount", 0);
        result.put("totalCount", 0);
        result.put("successCount", 0);
        result.put("failCount", 0);
        result.put("openids", "");
        
        if(openIds != null && openIds.size() > 0)
        {
            result.put("totalCount", openIds.size());
            for (DynaBean item : openIds)
            {
                try
                {
                    String from_user = (String) item.get("from_user");
                    if(weixParams != null){
                    	weixParams.setTouser(from_user);
                        weixParams = sendCustomerMsg(weixParams);
                    }
                    openIdArray.add(from_user);
                    if(weixParams != null && "000".equals(weixParams.getErrCode()))
                    {
                        result.put("successCount", (Integer) result.get("successCount") + 1);
                    }
                    else
                    {
                        result.put("failCount", (Integer) result.get("failCount") + 1);
                    }
                }
                catch (Exception e)
                {
                    EmpExecutionContext.error(e, "微信群发-遍历openIds出错。");
                }
            }
            result.put("openids", openIdArray);
        }
        return result;
    }

    /**
     * @description 获取用户信息
     * @return HttpReturnParams类型 (成功 ErrCode:000,ErrMsg:success,失败:
     *         -9999,ErrMsg其他)
     * @throws Exception
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-21 下午02:11:49
     */
    public WeixParams getUserBaseMsg(WeixParams weixParams) throws Exception
    {
        try
        {
            // access_token参数
            String accesstoken = weixParams.getAccess_token();
            // openid参数
            String openid = weixParams.getTouser();
            // 判断Access_token
            if(null == accesstoken || "".equals(accesstoken))
            {
                // 不合法的access_token
                weixParams.setErrMsg(WeixMessage.getValue("40014"));
                return weixParams;
            }
            else
            {
                accesstoken = accesstoken.trim();
            }
            // 判断普通用户的openid
            if(null == openid || "".equals(openid))
            {
                // 不合法的OpenID
                weixParams.setErrMsg(WeixMessage.getValue("40003"));
                return weixParams;
            }
            else
            {
                openid = openid.trim();
            }
            // 获取用户基本信息消息URL
            String url = WeixHttpUrl.WX_GETUSERINFO_URL + "access_token=" + accesstoken + "&openid=" + openid;
            // 请求http返回参数
            HttpReturnParams params = new HttpReturnParams();
            params.setUrl(url);
            // 设置请求方式为GET请求
            params.setRequestType("GET");
            // 输入流
            params.setDoInput(true);
            params.setReturnType(WXStaticValue.RETURNHTTP_JSON);
            // 发送请求
            params = new HttpsRequestService().HandleHttpsRequest(params);
            this.changeParams(params, weixParams);
        }
        catch (Exception e)
        {
            weixParams.setErrMsg("error");
            weixParams.setErrCode("-9999");
            EmpExecutionContext.error(e, "WeixinService.getUserBaseMsg Is Error");
        }
        return weixParams;
    }

    /**
     * @description 下载微信资源文件 二维码 qrcode 图片 image/语音 & 视频 voice/音乐 music/ 图文news
     * @return
     * @throws DocumentException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-21 下午02:11:49
     */
    @SuppressWarnings("deprecation")
    public WeixParams donwloadWeixResource(WeixParams weixParams) throws Exception
    {
        try
        {
            String httpurl = "";
            String[] path = null;
            if(weixParams.getMsgtype() == null || "".equals(weixParams.getMsgtype()))
            {
                // 不合法的消息类型
                weixParams.setErrMsg(WeixMessage.getValue("40008"));
                return weixParams;
            }
            else
            {
                path = new WeixBiz().getWeixResourceUrl(weixParams.getMsgtype());
                if(path == null || path.length == 0)
                {
                    weixParams.setErrMsg("WeixinService.donwloadWeixResource.path Is Null");
                    return weixParams;
                }
                // 二维码文件下载
                if("qrcode".equals(weixParams.getMsgtype()))
                {
                    if(null == weixParams.getTicket() || "".equals(weixParams.getTicket()))
                    {
                        // 不合法的access_token
                        weixParams.setErrMsg(WeixMessage.getValue("40014"));
                        return weixParams;
                    }
                    httpurl = WeixHttpUrl.WX_SHOWGRCODE_URL + "ticket=" + URLEncoder.encode(weixParams.getTicket());
                }
                else
                {
                    // 其他文件下载 图片 image/语音 voice/ 视频 video/音乐 music/ 图文news
                    if(weixParams.getAccess_token() == null || "".equals(weixParams.getAccess_token()))
                    {
                        // 不合法的token
                        weixParams.setErrMsg(WeixMessage.getValue("40002"));
                        return weixParams;
                    }
                    if(weixParams.getMedia_id() == null || "".equals(weixParams.getMedia_id()))
                    {
                        weixParams.setErrMsg(WeixMessage.getValue("40007"));
                        return weixParams;
                    }
                    httpurl = WeixHttpUrl.WX_DOWNLOAD_URL + "access_token=" + weixParams.getAccess_token() + "&media_id=" + weixParams.getMedia_id();
                }
            }

            HttpReturnParams httpparams = new HttpReturnParams();
            httpparams.setUrl(httpurl);
            httpparams.setReturnType(WXStaticValue.WX_HTTPPOSTTYPE_FILE);
            httpparams.setRequestType("GET");
            httpparams.setMenuCode("");
            // 全路径
            httpparams.setUpdownFileUrl(path[0]);
            // 相对路径
            httpparams.setFileUrl(path[1]);
            if("qrcode".equals(weixParams.getMsgtype()))
            {
                httpparams = new HttpsRequestService().HandleHttpsRequest(httpparams);
            }
            else
            {
                httpparams = new HttpRequestService().requestOttHttp(httpparams);
            }
            this.changeParams(httpparams, weixParams);
        }
        catch (Exception e)
        {
            weixParams.setErrMsg("error");
            weixParams.setErrCode("-9999");
            EmpExecutionContext.error(e, "WeixinService.donwloadWeixResource Is Error");
        }
        return weixParams;
    }

    /**
     * @description 上传微信资源文件
     * @return 凭证 Access_token 文件类型 Msgtype
     *         图片（image）、语音（voice）、视频（video）和缩略图（thumb，主要用于视频与音乐格式的缩略图） 文件绝对路径
     *         FileUrl 文件NAME属性对应值 Filename
     * @throws DocumentException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-21 下午02:11:49
     */
    public WeixParams uploadWeixResource(WeixParams weixParams) throws Exception
    {
        try
        {
            if(weixParams.getAccess_token() == null || "".equals(weixParams.getAccess_token()))
            {
                weixParams.setErrMsg(WeixMessage.getValue("40002"));
                return weixParams;
            }
            if(weixParams.getMsgtype() == null || "".equals(weixParams.getMsgtype()))
            {
                weixParams.setErrMsg(WeixMessage.getValue("40004"));
                return weixParams;
            }
            if(weixParams.getFileUrl() == null || "".equals(weixParams.getFileUrl()))
            {
                weixParams.setErrMsg(WeixMessage.getValue("44001"));
                return weixParams;
            }
            if(weixParams.getFilename() == null || "".equals(weixParams.getFilename()))
            {
                weixParams.setErrMsg("WeixinService.uploadWeixResource.Filename Is Null");
                return weixParams;
            }
            String httpurl = WeixHttpUrl.WX_UPLOAD_URL + "access_token=" + weixParams.getAccess_token() + "&type=" + weixParams.getMsgtype();
            HttpReturnParams params = new HttpReturnParams();
            params.setUrl(httpurl);
            params.setReturnType(WXStaticValue.RETURNHTTP_JSON);
            params.setRequestType("POST");
            params.setHttpPostType(WXStaticValue.WX_HTTPPOSTTYPE_FILE);
            params.setMenuCode("");
            String webRoot = new TxtFileUtil().getWebRoot();
            params.setUpdownFileUrl(webRoot + weixParams.getFileUrl());
            params.setPostMsg(weixParams.getFilename());
            params = new HttpRequestService().requestOttHttp(params);
            this.changeParams(params, weixParams);
        }
        catch (Exception e)
        {
            weixParams.setErrMsg("error");
            weixParams.setErrCode("-9999");
            EmpExecutionContext.error(e, "WeixinService.uploadWeixResource.Filename Is Error");
        }
        return weixParams;
    }

    /**
     * @description 操作群组
     * @return HttpReturnParams类型 ( 成功
     *         ErrCode:000,ErrMsg:success,失败:-9999,ErrMsg其他) 处理微信 群组的操作
     *         handleGpType : query查询 update更新 add新增 move移动
     * @throws Exception
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-21 下午02:11:49
     */
    @SuppressWarnings("unchecked")
    public WeixParams handleWeixGroup(WeixParams weixParams) throws Exception
    {
        try
        {
            String handleGpType = weixParams.getHandleGpType();
            if(handleGpType == null || "".equals(handleGpType))
            {
                weixParams.setErrMsg("WeixinService.handleWeixGroup.HandleGpType Is Null");
                return weixParams;
            }
            // access_token参数
            String accesstoken = weixParams.getAccess_token();
            // 判断Access_token
            if(accesstoken == null || "".equals(accesstoken))
            {
                // 不合法的access_token
                weixParams.setErrMsg(WeixMessage.getValue("40014"));
                return weixParams;
            }
            String httpUrl = "";
            // 请求http返回参数
            HttpReturnParams params = new HttpReturnParams();
            // 默认设置请求方式
            params.setRequestType("POST");
            // 默认 输出流
            params.setDoOutput(true);
            // 输入流
            params.setDoInput(true);
            if("query".equals(handleGpType))
            {
                httpUrl = WeixHttpUrl.WX_QUERYGROUP_URL + "access_token=" + accesstoken;
                // 设置请求方式为GET请求
                params.setRequestType("GET");
                params.setDoOutput(false);
            }
            else
            {
                JSONObject secondaryObj = new JSONObject();
                // 分组ID
                String groupId = weixParams.getGroupId();
                if(!"add".equals(handleGpType) && (groupId == null || "".equals(groupId)))
                {
                    // 40050 不合法的分组ID
                    weixParams.setErrMsg(WeixMessage.getValue("40050"));
                    return weixParams;
                }
                else
                {
                    if("move".equals(handleGpType))
                    {
                        secondaryObj.put("to_groupid", Integer.valueOf(groupId));
                    }
                    else if("update".equals(handleGpType))
                    {
                        secondaryObj.put("id", Integer.valueOf(groupId));
                    }
                }
                if("move".equals(handleGpType))
                {
                    // 用户唯一标识符
                    String openid = weixParams.getTouser();
                    // 判断openid
                    if(null == openid || "".equals(openid))
                    {
                        // 40003 不合法的openid
                        weixParams.setErrMsg(WeixMessage.getValue("40003"));
                        return weixParams;
                    }
                    else
                    {
                        secondaryObj.put("openid", openid);
                    }
                    httpUrl = WeixHttpUrl.WX_MOVEMEMBERS_URL + "access_token=" + accesstoken;
                    params.setPostMsg(secondaryObj.toString());
                }
                else if("add".equals(handleGpType) || "update".equals(handleGpType))
                {
                    // 判断分组名称
                    String groupName = weixParams.getGroupName();
                    if(null == groupName || "".equals(groupName))
                    {
                        // 40051 不合法的分组名字
                        weixParams.setErrMsg(WeixMessage.getValue("40051"));
                        return weixParams;
                    }
                    else
                    {
                        secondaryObj.put("name", groupName);
                    }
                    if("update".equals(handleGpType))
                    {
                        httpUrl = WeixHttpUrl.WX_UPDATEGROUP_URL + "access_token=" + accesstoken;
                    }
                    else if("add".equals(handleGpType))
                    {
                        // 修改分组URL
                        httpUrl = WeixHttpUrl.WX_CREATEGROUP_URL + "access_token=" + accesstoken;
                    }
                    JSONObject seniorObj = new JSONObject();
                    seniorObj.put("group", secondaryObj);
                    params.setPostMsg(seniorObj.toString());
                }
            }
            params.setUrl(httpUrl);
            params.setReturnType(WXStaticValue.RETURNHTTP_JSON);
            // 发送请求
            params = new HttpsRequestService().HandleHttpsRequest(params);
            this.changeParams(params, weixParams);
        }
        catch (Exception e)
        {
            weixParams.setErrMsg("error");
            weixParams.setErrCode("-9999");
            EmpExecutionContext.error(e, "WeixinService.handleWeixGroup.HandleGpType Is Error");
        }
        return weixParams;
    }

    /**
     * @description 创建二维码ticket
     * @param weixParams
     *        微信内容参数类
     * @return 成功 ErrCode 000 ErrMsg success 失败 -9999 ErrMsg其他
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-20 上午11:24:24
     */
    @SuppressWarnings("unchecked")
    public WeixParams qrcodeTicketWeix(WeixParams weixParams) throws Exception
    {
        HttpReturnParams params = new HttpReturnParams();
        try
        {
            // access_token参数
            String accesstoken = weixParams.getAccess_token();
            if(accesstoken == null || "".equals(accesstoken))
            {
                // 不合法的access_token
                weixParams.setErrMsg(WeixMessage.getValue("40014"));
                return weixParams;
            }
            JSONObject seniorObj = new JSONObject();
            // 判断二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久
            if(null == weixParams.getAction_name() || "".equals(weixParams.getAction_name()))
            {
                weixParams.setErrMsg("WeixinService.qrcodeTicketWeix.Action_name Is Null");
                return weixParams;
            }
            else if(WXStaticValue.WX_SCENE_ACTION_NAME.equalsIgnoreCase(weixParams.getAction_name()))
            {
                if(weixParams.getExpire_seconds() < 1)
                {
                    weixParams.setErrMsg("WeixinService.qrcodeTicketWeix.Expire_seconds Is Null");
                    return weixParams;
                }
                else
                {
                    seniorObj.put("expire_seconds", weixParams.getExpire_seconds());
                    seniorObj.put("action_name", weixParams.getAction_name());
                }
            }
            else if(WXStaticValue.WX_LIMIT_SCENE_ACTION_NAME.equalsIgnoreCase(weixParams.getAction_name()))
            {
                seniorObj.put("action_name", weixParams.getAction_name());
            }
            else
            {
                weixParams.setErrMsg("WeixinService.qrcodeTicketWeix.Action_name Is Null");
                return weixParams;
            }
            // 场景值ID，
            if(weixParams.getScene_id() < 1)
            {
                weixParams.setErrMsg("WeixinService.qrcodeTicketWeix.Scene_id Is Null");
                return weixParams;
            }
            else
            {
                // 三级OBJECT
                JSONObject threeLevelObj = new JSONObject();
                threeLevelObj.put("scene_id", weixParams.getScene_id());
                // 二级OBJECT
                JSONObject secondaryObj = new JSONObject();
                secondaryObj.put("scene", threeLevelObj);
                // 一级OBEJCT数据
                seniorObj.put("action_info", secondaryObj);
            }
            // 发送客服消息URL
            String httpUrl = WeixHttpUrl.WX_CREATEGRCODE_URL + "access_token=" + accesstoken;
            params.setUrl(httpUrl);
            // POST请求
            params.setRequestType("POST");
            // 输入流
            params.setDoInput(true);
            // 输出流
            params.setDoOutput(true);
            // 将JSON格式转化STRING
            params.setPostMsg(seniorObj.toString());
            params.setReturnType(WXStaticValue.RETURNHTTP_JSON);
            // 发送请求
            params = new HttpsRequestService().HandleHttpsRequest(params);
            this.changeParams(params, weixParams);
        }
        catch (Exception e)
        {
            weixParams.setErrMsg("error");
            weixParams.setErrCode("-9999");
            EmpExecutionContext.error(e, "WeixinService.qrcodeTicketWeix Is Error");
        }
        return weixParams;
    }

    /**
     * @description 获取关注者列表
     * @param weixParams
     *        微信传递参数类
     * @return 微信参数类
     * @throws Exception
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-25 下午02:09:49
     */
    public WeixParams getFollowersList(WeixParams weixParams) throws Exception
    {
        try
        {
            // access_token参数
            String accesstoken = weixParams.getAccess_token();
            // next_openid参数
            String next_openid = weixParams.getNext_openid();
            // 判断Access_token
            if(null == accesstoken || "".equals(accesstoken))
            {
                // 不合法的access_token
                weixParams.setErrMsg(WeixMessage.getValue("40014"));
                return weixParams;
            }
            else
            {
                accesstoken = accesstoken.trim();
            }

            // 获取关注者列表URL
            String url = WeixHttpUrl.WX_GETCAREUSER_URL + "access_token=" + accesstoken;

            // 判断next_openid 第一个拉取的OPENID，不填默认从头开始拉取
            if(null == next_openid || "".equals(next_openid))
            {
                weixParams.setNext_openid("");
            }
            else
            {
                next_openid = next_openid.trim();
                url = url + "&next_openid=" + next_openid;
            }
            // 请求http返回参数
            HttpReturnParams params = new HttpReturnParams();
            params.setUrl(url);
            // 设置请求方式为GET请求
            params.setRequestType("GET");
            // 输入流
            params.setDoInput(true);
            params.setReturnType(WXStaticValue.RETURNHTTP_JSON);
            // 发送请求
            params = new HttpsRequestService().HandleHttpsRequest(params);
            this.changeParams(params, weixParams);
        }
        catch (Exception e)
        {
            weixParams.setErrMsg("error");
            weixParams.setErrCode("-9999");
            EmpExecutionContext.error(e, "WeixinService.getFollowersList Is Error");
        }
        return weixParams;
    }

    /**
     * @description 发送被动响应消息 封装XML格式
     * @param weixParams
     *        微信传递参数类
     * @return 微信参数类
     * @throws Exception
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-25 下午02:23:36
     */
    public WeixParams sendPassiveMsg(WeixParams weixParams) throws Exception
    {
        try
        {
            // 接收方帐号（收到的OpenID）
            String toUserName = weixParams.getTouser();
            // 开发者微信号
            String fromUserName = weixParams.getPublicAccounts();
            // 创建时间
            long createTime = weixParams.getCreateTime();
            // 消息类型
            String msgType = weixParams.getMsgtype();
            if(null == toUserName || "".equals(toUserName))
            {
                // 缺少openid
                weixParams.setErrMsg(WeixMessage.getValue("41009"));
                return weixParams;
            }
            if(null == fromUserName || "".equals(fromUserName))
            {
                // 缺少openid
                weixParams.setErrMsg(WeixMessage.getValue("41009"));
                return weixParams;
            }
            if(0L == createTime)
            {
                // 不合法的参数
                weixParams.setErrMsg(WeixMessage.getValue("40035"));
                return weixParams;
            }
            if(null == msgType || "".equals(msgType))
            {
                // 不合法的消息类型
                weixParams.setErrMsg(WeixMessage.getValue("40008"));
                return weixParams;
            }
            else
            {
                msgType = msgType.trim().toLowerCase();
            }
            StringBuffer msgXml = new StringBuffer();
            msgXml.append("<xml>");
            msgXml.append("<ToUserName><![CDATA[").append(toUserName).append("]]></ToUserName>");
            msgXml.append("<FromUserName><![CDATA[").append(fromUserName).append("]]></FromUserName>");
            msgXml.append("<CreateTime>").append(createTime).append("]]></CreateTime>");
            msgXml.append("<MsgType><![CDATA[").append(msgType).append("]]></MsgType>");
            if(msgType.equals("text"))
            {
                msgXml.append("<Content><![CDATA[").append(weixParams.getContent()).append("]]></Content>");
            }
            else if(msgType.equals("image"))
            {
                msgXml.append("<Image>");
                msgXml.append("<MediaId><![CDATA[").append(weixParams.getMedia_id()).append("]]></MediaId>");
                msgXml.append("</Image>");
            }
            else if(msgType.equals("voice"))
            {
                msgXml.append("<Voice>");
                msgXml.append("<MediaId><![CDATA[").append(weixParams.getMedia_id()).append("]]></MediaId>");
                msgXml.append("</Voice>");
            }
            else if(msgType.equals("video"))
            {
                msgXml.append("<Video>");
                msgXml.append("<MediaId><![CDATA[").append(weixParams.getMedia_id()).append("]]></MediaId>");
                msgXml.append("<ThumbMediaId><![CDATA[").append(weixParams.getThumb_media_id()).append("]]></ThumbMediaId>");
                msgXml.append("</Video>");
            }
            else if(msgType.equals("music"))
            {
                msgXml.append("<Music>");
                msgXml.append("<Title><![CDATA[").append(weixParams.getTitle()).append("]]></Title>");
                msgXml.append("<Description><![CDATA[").append(weixParams.getDescription()).append("]]></Description>");
                msgXml.append("<MusicUrl><![CDATA[").append(weixParams.getMusicurl()).append("]]></MusicUrl>");
                msgXml.append("<HQMusicUrl><![CDATA[").append(weixParams.getHqmusicurl()).append("]]></HQMusicUrl>");
                msgXml.append("<ThumbMediaId><![CDATA[").append(weixParams.getThumb_media_id()).append("]]></ThumbMediaId>");
                msgXml.append("</Music>");
            }
            else if(msgType.equals("news"))
            {
                // 图文
            }
            else
            {
                weixParams.setErrMsg("msgTypeIsNull");
                return weixParams;
            }
            msgXml.append("</xml>");
            // 将拼装xml格式字符串转化成标准xml
            Document document = DocumentHelper.parseText(msgXml.toString());
            // 标准xml得到的字符串
            String xmlString = document.asXML();
            weixParams.setErrCode("000");
            weixParams.setErrMsg("success");
            weixParams.setReturnXml(xmlString);
        }
        catch (Exception e)
        {
            weixParams.setErrMsg("error");
            weixParams.setErrCode("-9999");
            EmpExecutionContext.error(e, "WeixinService.SendPassiveMsg Is Error");
        }
        return weixParams;
    }

    /**
     * @description 获取微信凭证
     * @param weixParams
     *        微信传递参数类
     * @return
     * @throws Exception
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-27 上午10:04:50
     */
    public WeixParams getWeixAccessToken(WeixParams weixParams) throws Exception
    {
        try
        {
            // 第三方用户唯一凭证
            String appid = weixParams.getAppid();
            if(appid == null || "".equals(appid))
            {
                // 不合法的APPID
                weixParams.setErrMsg(WeixMessage.getValue("40013"));
                return weixParams;
            }
            // 第三方用户唯一凭证密钥
            String appsecret = weixParams.getAppsecret();
            if(appsecret == null || "".equals(appsecret))
            {
                weixParams.setErrMsg("WeixinService.getWeixAccessToken.Appsecret Is Null");
                return weixParams;
            }
            String httpurl = WeixHttpUrl.WX_ACCESSTOKEN_URL + "&appid=" + appid + "&secret=" + appsecret;
            HttpReturnParams params = new HttpReturnParams();
            params.setUrl(httpurl);
            params.setDoInput(true);
            params.setDoOutput(false);
            params.setRequestType("GET");
            params.setReturnType(WXStaticValue.RETURNHTTP_JSON);
            params = new HttpsRequestService().HandleHttpsRequest(params);
            this.changeParams(params, weixParams);
        }
        catch (Exception e)
        {
            weixParams.setErrMsg("error");
            weixParams.setErrCode("-9999");
            EmpExecutionContext.error(e, "WeixinService.getWeixAccessToken Is Error");
        }
        return weixParams;
    }

    /**
     * 获取网页授权的access_token
     */
    public WeixParams getOauth2AccessToken(WeixParams weixParams) throws Exception
    {
        try
        {
            // 第三方用户唯一凭证
            String appid = weixParams.getAppid();
            // 第三方用户唯一凭证密钥
            String appsecret = weixParams.getAppsecret();
            // 网页授权code
            String code = weixParams.getCode();

            if(appid == null || "".equals(appid))
            {
                // 不合法的APPID
                weixParams.setErrMsg(WeixMessage.getValue("41002"));
                return weixParams;
            }
            if(appsecret == null || "".equals(appsecret))
            {
                weixParams.setErrMsg(WeixMessage.getValue("41001"));
                return weixParams;
            }
            if(code == null || "".equals(code))
            {
                weixParams.setErrMsg("WeixinService.getOauth2AccessToken.code");
                return weixParams;
            }
            String httpurl = WeixHttpUrl.WX_AUTHORIZE_ACCESS_TOKEN_URL + "appid=" + appid + "&secret=" + appsecret + "&code=" + code + "&grant_type=authorization_code";
            HttpReturnParams params = new HttpReturnParams();
            params.setUrl(httpurl);
            params.setDoInput(true);
            params.setDoOutput(false);
            params.setRequestType("GET");
            params.setReturnType(WXStaticValue.RETURNHTTP_JSON);
            params = new HttpsRequestService().HandleHttpsRequest(params);
            this.changeParams(params, weixParams);
        }
        catch (Exception e)
        {
            weixParams.setErrMsg("error");
            weixParams.setErrCode("-9999");
            EmpExecutionContext.error(e, "WeixinService.getOauth2AccessToken Is Error");
        }
        return weixParams;
    }

    /**
     * tp=0(分组群发) || tp=1(根据OpenID列表群发)
     * 
     * @param weixParams
     * @return
     */
    public WeixParams sendAll(WeixParams weixParams)
    {
        try
        {
            // 调用群发接口的访问凭证
            String accessToken = weixParams.getAccess_token();
            if(accessToken == null || "".equals(accessToken))
            {
                weixParams.setErrMsg(WeixMessage.getValue("41001"));
                return weixParams;
            }
            String httpurl = WeixHttpUrl.WX_SEND_ALL_OPENID_URL + "access_token=" + accessToken;
            HttpReturnParams params = new HttpReturnParams();
            params.setUrl(httpurl);
            params.setDoInput(true);
            params.setDoOutput(true);
            params.setRequestType("POST");
            params.setPostMsg(weixParams.getData());
            params.setReturnType(WXStaticValue.RETURNHTTP_JSON);
            params = new HttpsRequestService().HandleHttpsRequest(params);
            this.changeParams(params, weixParams);
        }
        catch (Exception e)
        {
            weixParams.setErrMsg("error");
            weixParams.setErrCode("-9999");
            EmpExecutionContext.error(e, "WeixinService.sendGroupAll Is Error");
        }
        return weixParams;
    }

   
    /**
     * 群发消息接口(支持文本和图片发送)
     * @description    
     * @param data
     * 1.文本消息格式
     * {
     *  "lgcorpcode"： "10001",
     *  "aid" : "11111"，
     *  "touser": [
     *  "oR5Gjjl_eiZoUpGozMo7dbBJ362A", "oR5Gjjo5rXlMUocSEXKT7Q5RQ63Q" ], "msgtype": "text", "content": "hello from boxer."}
     *  }
     * 2.图片消息
     * {
     * "lgcorpcode"： "10001",
     * "aid" : "11111"，
     * "touser":[
     * "OPENID1",
     * "OPENID2"
     *],
     *"msgtype":"image",
     *   "imageUrl",“file/weix/rimg/2014/7/10/6021ac2c821001.jpg”
     *    }
     * @return       			 
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年7月9日 下午2:56:59
     */
    @SuppressWarnings("unchecked")
    public String sendAllMessage(String dataStr){
        //返回的结果
        JSONObject result = new JSONObject();
        //用户的openId集合
        JSONArray openIds = null;
        try{
            JSONObject data = StringUtils.parsJsonObj(dataStr);
            openIds = (JSONArray) data.get("touser");
            
            //验证消息
            if(data.get("lgcorpcode")==null||data.get("aid")==null){
                result.put("errcode", "-9999");
                result.put("errmsg", "企业编号或者公众帐号编号为空！");
                return result.toString();
            }
            
            if(openIds==null){
                result.put("errcode", "-9999");
                result.put("errmsg", "发送的用户人数不能为空！");
                return result.toString();
            }
           
            //消息的类型
            String msgtype = (String)data.get("msgtype");
            String aid = (String)data.get("aid");
            
            //发送参数封装在WeixParams中，方便调用老的方法
            WeixParams weixParams = new WeixParams();
            // 第一步： 获取accessToken
            String accessToken = (new WeixBiz()).getToken(aid);
            weixParams.setAccess_token(accessToken);
            
            if("text".equals(msgtype)){
                String content = (String)data.get("content");
                weixParams.setMsgtype("text");
                if(content==null){
                    result.put("errcode", "-9999");
                    result.put("errmsg", "文本消息的内容不能为空！");
                    return result.toString();
                }
                weixParams.setData(qunfa.getOpenIdTextMsg(openIds, content));
            }else if("image".equals(msgtype)){
                //图片的名称
                String title = (String)data.get("title");
                String imageUrl = (String)data.get("imageUrl");
                String media_id = null;
                
                if(imageUrl == null){
                    result.put("errcode", "-9999");
                    result.put("errmsg", "图片不能为空！");
                    return result.toString();
                }
                weixParams.setFilename(title == null ? "未知" : title);
                weixParams.setFileUrl(imageUrl);
                weixParams.setMsgtype("image");
                
                weixParams = this.uploadWeixResource(weixParams);
                // 如果上传图片媒体成功，执行下面的操作
                if(weixParams != null && "000".equals(weixParams.getErrCode()))
                {
                    // 获取JSON中的数据
                    JSONObject objResult = weixParams.getJsonObj();
                    media_id = (String) objResult.get("media_id");
                    weixParams.setData(qunfa.getOpenIdImageMsg(openIds, media_id));
                }
                else
                {
                    //上传图片失败
                    EmpExecutionContext.error("群发接口上传图片失败！" + (weixParams!=null?weixParams.getErrMsg():""));
                    result.put("errcode", weixParams!=null?weixParams.getErrCode():"");
                    result.put("errmsg",WeixMessage.getValue(weixParams!=null?weixParams.getErrCode():""));
                    return result.toString();
                }
            }else{
                result.put("errcode", "-9999");
                result.put("errmsg", "消息的类型不正确!");
            }
            
            //日志写入文件
            qunfa.writeToTextFile(weixParams.getData());
            // 群发提交数据
            weixParams = this.sendAll(weixParams);
            if(weixParams != null && "000".equals(weixParams.getErrCode()))
            {
                result.put("errcode", weixParams.getErrCode());
                result.put("errmsg",WeixMessage.getValue(weixParams.getErrCode()));
            }
            else
            {
            	 if(weixParams != null){
	                result.put("errcode", weixParams.getErrCode());
	                result.put("errmsg",WeixMessage.getValue(weixParams.getErrCode()));
            	 }
                return result.toString();
            }
        }catch(Exception e){
            EmpExecutionContext.error("WeixService.sendAllMessage Is Error " + e.getMessage());
            result.put("errcode", "-9999");
            result.put("errmsg", "发送消息失败！");
        }
        
        return result.toString();
    }
    
    // 高级群发接口-提交图文
    public WeixParams uploadArticleToWeixinServer(WeixParams weixParams)
    {
        try
        {
            // 调用群发接口的访问凭证
            String accessToken = weixParams.getAccess_token();
            if(accessToken == null || "".equals(accessToken))
            {
                weixParams.setErrMsg(WeixMessage.getValue("41001"));
                return weixParams;
            }

            String httpurl = WeixHttpUrl.WX_UPLOADNEW + "access_token=" + accessToken;
            HttpReturnParams params = new HttpReturnParams();
            params.setUrl(httpurl);
            params.setDoInput(true);
            params.setDoOutput(true);
            params.setRequestType("POST");
            params.setPostMsg(weixParams.getData());
            params.setReturnType(WXStaticValue.RETURNHTTP_JSON);
            params = new HttpsRequestService().HandleHttpsRequest(params);
            this.changeParams(params, weixParams);
        }
        catch (Exception e)
        {
            weixParams.setErrMsg("error");
            weixParams.setErrCode("-9999");
            EmpExecutionContext.error(e, "WeixinService.sendGroupAll Is Error");
        }
        return weixParams;
    }
}
