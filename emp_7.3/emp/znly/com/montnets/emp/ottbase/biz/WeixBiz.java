package com.montnets.emp.ottbase.biz;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.entity.wxgl.*;
import com.montnets.emp.ottbase.constant.WXStaticValue;
import com.montnets.emp.ottbase.constant.WeixMessage;
import com.montnets.emp.ottbase.dao.OttGenericTransactionDAO;
import com.montnets.emp.ottbase.dao.OttSpecialDAO;
import com.montnets.emp.ottbase.param.WeixParams;
import com.montnets.emp.ottbase.service.HttpsRequestService;
import com.montnets.emp.ottbase.service.IWeixinService;
import com.montnets.emp.ottbase.service.WeixService;
import com.montnets.emp.ottbase.util.GetSxCount;
import com.montnets.emp.ottbase.util.GlobalMethods;
import com.montnets.emp.ottbase.util.StringUtils;
import com.montnets.emp.ottbase.util.TxtFileUtil;
import com.montnets.emp.table.wxgl.TableLfWeiGroup;
import com.montnets.emp.znly.biz.CustomChatBiz;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class WeixBiz extends SuperBiz
{
    // token集合
    private final Map<String, String> tokenMap = new HashMap<String, String>();

    // token获取时间的集合
    private final Map<String, Long> tokenGetTimeMap = new HashMap<String, Long>();

    private final long timeInterval = 100 * 60L * 1000L;

    private final Map<String, LfWeiAccount> accountMapWhitAid = new HashMap<String, LfWeiAccount>();

    private final Map<String, LfWeiAccount> accountMapWhitOpenid = new HashMap<String, LfWeiAccount>();

    // 时间对象
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取token
     * 
     * @description
     * @param account
     *        公众号信息
     * @return
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-11-20 下午02:55:38
     */
    public String getToken(LfWeiAccount account)
    {
        String openId = account.getOpenId();
        if(tokenMap.get(openId) == null || System.currentTimeMillis() - tokenGetTimeMap.get(openId) - timeInterval > 0)
        {
            try
            {
                String token = getAccessToken(account.getAppId(), account.getSecret());
                if("error".equals(token))
                {
                    return null;
                }
                else
                {
                    tokenMap.put(openId, token);
                    tokenGetTimeMap.put(openId, System.currentTimeMillis());
                    return token;
                }
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "获取微信token失败！");
                return null;
            }
        }
        else
        {
            return tokenMap.get(openId);
        }
    }

    /**
     * 在公众帐号编辑的时候。更新accountMapWhitAid的account
     * @description    
     * @param account
     * @return       			 
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年7月7日 上午10:21:48
     */
    public Map<String, LfWeiAccount> updateAccountMapWhitAid(LfWeiAccount account){
        accountMapWhitAid.put(String.valueOf(account.getAId()), account);
        return accountMapWhitAid;
    }
    
    /**
     * 重新获取token
     * @param openId
     * @return
     */
    public String reGetToken(String  openId)
    {
    	LfWeiAccount account = getWeixAccountByOpen(openId);
    	String token = getAccessToken(account.getAppId(), account.getSecret());
        if("error".equals(token))
        {
            return null;
        }
        else
        {
            tokenMap.put(openId, token);
            tokenGetTimeMap.put(openId, System.currentTimeMillis());
            return token;
        }
    }
    
    /**
     * 重新获取token
     * @param openId
     * @return
     */
    public String reGetTokenByAid(String  openId)
    {
    	LfWeiAccount account = getWeixAccountByOpen(openId);
    	String token = getAccessToken(account.getAppId(), account.getSecret());
        if("error".equals(token))
        {
            return null;
        }
        else
        {
            tokenMap.put(openId, token);
            tokenGetTimeMap.put(openId, System.currentTimeMillis());
            return token;
        }
    }
    /**
     * 获取access_token
     * 
     * @description
     * @param openId
     *        公众账号Id
     * @return
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-4 下午06:49:00
     */
    public String getToken(String Aid)
    {
        LfWeiAccount account = getWeixAccountByAId(Aid);
        if(account == null)
        {
            return null;
        }
        return getToken(account);
    }

    /**
     * 通过 公众账openid获取公众账号信息
     * 
     * @description
     * @param openId
     * @return
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-4 下午06:52:32
     */
    public LfWeiAccount getWeixAccountByOpen(String openId)
    {
        LfWeiAccount account = accountMapWhitOpenid.get(openId);
        if(account == null)
        {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("openId", openId);
            List<LfWeiAccount> accountList = null;
            try
            {
                accountList = empDao.findListByCondition(LfWeiAccount.class, conditionMap, null);
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "获取token时查询公众账号信息失败！");
                return null;
            }
            if(accountList != null && accountList.size() > 0)
            {
                account = accountList.get(0);
                accountMapWhitOpenid.put(openId, account);
            }
            else
            {
                return null;
            }
        }
        return account;
    }

    /**
     * 通过 公众账id获取公众账号信息
     * 
     * @description
     * @param openId
     * @return
     * @author linzhihan <zhihanking@163.com>
     * @datetime 2013-12-4 下午06:52:32
     */
    public LfWeiAccount getWeixAccountByAId(String Aid)
    {
        LfWeiAccount account = accountMapWhitAid.get(Aid);
        if(account == null)
        {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("AId", Aid);
            List<LfWeiAccount> accountList = null;
            try
            {
                accountList = empDao.findListByCondition(LfWeiAccount.class, conditionMap, null);
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "获取token时查询公众账号信息失败！");
                return null;
            }
            if(accountList != null && accountList.size() > 0)
            {
                account = accountList.get(0);
                accountMapWhitAid.put(Aid, account);
            }
            else
            {
                return null;
            }
        }
        return account;
    }

    /**
     * @description 微信资源文件
     * @param type
     *        image 图片 voice声音 video视频 qrcode二维码 thumb缩略图
     * @param fileType
     *        文件类型
     * @return
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-25 下午03:01:52
     */
    public String[] getWeixResourceUrl(String type)
    {
        String[] url = new String[3];
        try
        {
            String strNYR = "";
            TxtFileUtil txtFileUtil = new TxtFileUtil();
            try
            {
                strNYR = txtFileUtil.getCurNYR();
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "WeixBiz.getWeixResourceUrl.getCurNYR Is Errer");
                return null;
            }
            String fileDirUrl = "";
            String fileType = "";
            if("image".equals(type))
            {
                // 微信图片
                fileDirUrl = WXStaticValue.WEIX_MEDIA_IMG + strNYR;
                fileType = "jpg";
            }
            else if("voice".equals(type))
            {
                // 微信多媒体声音
                fileDirUrl = WXStaticValue.WEIX_MEDIA_SOUND + strNYR;
                fileType = "amr";
            }
            else if("video".equals(type))
            {
                // 微信多媒体视频
                fileDirUrl = WXStaticValue.WEIX_MEDIA_VIDEO + strNYR;
                fileType = "mp4";
            }
            else if("qrcode".equals(type))
            {
                // 微信二维码
                fileDirUrl = WXStaticValue.WEIX_QRCODE + strNYR;
                fileType = "jpg";
            }
            else if("thumb".equals(type))
            {
                // 微信•缩略图
                fileDirUrl = WXStaticValue.WEIX_MEDIA_IMG + strNYR;
                fileType = "jpg";
            }
            else if("jpg".equals(type) || "png".equals(type))
            {
                // 微信•缩略图
                fileDirUrl = WXStaticValue.FILE_LBS_IMG + strNYR;
                fileType = type;
            }
            else
            {
                EmpExecutionContext.error("WeixBiz.getWeixResourceUrl.type Is Errer");
                return null;
            }
            String webRoot = txtFileUtil.getWebRoot();
            try
            {
                File file = new File(webRoot + fileDirUrl);
                if(!file.exists())
                {
                    file.mkdirs();
                }
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "WeixBiz.getWeixResourceUrl.mkdirs() Is Errer");
                return null;
            }
            GetSxCount sx = GetSxCount.getInstance();
            Date time = Calendar.getInstance().getTime();
            String saveName = type + "_" + (new SimpleDateFormat("yyyyMMddHHmmssS")).format(time) + "_" + sx.getCount() + "." + fileType;
            String logicUrl = fileDirUrl + saveName;
            String physicsUrl = webRoot + logicUrl;
            url[0] = physicsUrl;
            url[1] = logicUrl;
            url[2] = webRoot + fileDirUrl;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.getWeixResourceUrl Is Errer");
        }
        return url;
    }

    /**
     * @description 微信资源文件
     * @param type
     *        image 图片 voice声音 video视频 qrcode二维码 thumb缩略图
     * @param fileType
     *        文件类型
     * @return
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-25 下午03:01:52
     */
    public String[] getResourceUrl(String type,String fType,String mType)
    {
        String[] url = new String[3];
        try
        {
            String strNYR = "";
            TxtFileUtil txtFileUtil = new TxtFileUtil();
            try
            {
                strNYR = txtFileUtil.getCurNYR();
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "WeixBiz.getWeixResourceUrl.getCurNYR Is Errer");
                return null;
            }
            String fileDirUrl = "";
            String fileType = "";
            if("image".equals(type))
            {
                // 微信图片
                fileDirUrl = WXStaticValue.WEIX_MEDIA_IMG + strNYR;
                
                if(mType != null && "app".equals(mType)){
                	if(fType != null && !"".equals(fType) && fType.indexOf(".") != -1){
                		fileType = fType.substring(fType.lastIndexOf(".")+1, fType.length());
                	}else{
                		fileType = "jpg";
                	}
                }else{
                	fileType = "jpg";
                }
            }
            else if("voice".equals(type))
            {
                // 微信多媒体声音
                fileDirUrl = WXStaticValue.WEIX_MEDIA_SOUND + strNYR;
                fileType = "amr";
            }
            else if("video".equals(type))
            {
                // 微信多媒体视频
                fileDirUrl = WXStaticValue.WEIX_MEDIA_VIDEO + strNYR;
                fileType = "mp4";
            }
            else if("qrcode".equals(type))
            {
                // 微信二维码
                fileDirUrl = WXStaticValue.WEIX_QRCODE + strNYR;
                fileType = "jpg";
            }
            else if("thumb".equals(type))
            {
                // 微信•缩略图
                fileDirUrl = WXStaticValue.WEIX_MEDIA_IMG + strNYR;
                fileType = "jpg";
            }
            else if("jpg".equals(type) || "png".equals(type))
            {
                // 微信•缩略图
                fileDirUrl = WXStaticValue.FILE_LBS_IMG + strNYR;
                fileType = type;
            }
            else
            {
                EmpExecutionContext.error("WeixBiz.getWeixResourceUrl.type Is Errer");
                return null;
            }
            String webRoot = txtFileUtil.getWebRoot();
            try
            {
                File file = new File(webRoot + fileDirUrl);
                if(!file.exists())
                {
                    file.mkdirs();
                }
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "WeixBiz.getWeixResourceUrl.mkdirs() Is Errer");
                return null;
            }
            GetSxCount sx = GetSxCount.getInstance();
            Date time = Calendar.getInstance().getTime();
            String saveName = type + "_" + (new SimpleDateFormat("yyyyMMddHHmmssS")).format(time) + "_" + sx.getCount() + "." + fileType;
            String logicUrl = fileDirUrl + saveName;
            String physicsUrl = webRoot + logicUrl;
            url[0] = physicsUrl;
            url[1] = logicUrl;
            url[2] = webRoot + fileDirUrl;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.getWeixResourceUrl Is Errer");
        }
        return url;
    }
    
    /**
     * @description 微信 从流中读取信息，写文件
     * @param inputStream
     *        输入流
     * @param filepath
     *        文件路径 ，全路径
     * @return success/errer
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-25 下午03:18:00
     */
    public String writerFileForDownLoad(InputStream inputStream, String filepath)
    {
        File file = new File(filepath);
        FileOutputStream os = null;
        BufferedInputStream bufferIs = null;
        try
        {
            bufferIs = new BufferedInputStream(inputStream);
            os = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            int size = 0;
            while((size = bufferIs.read(buffer)) != -1)
            {
                os.write(buffer, 0, size);
            }
            os.flush();
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.writerFileForDownLoad Is Errer");
            return "errer";
        }
        finally
        {
            try
            {
            	if(os!=null){
                os.close();
            	}
            	if(bufferIs!=null){
                bufferIs.close();
            	}
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "WeixBiz.writerFileForDownLoad.close() Is fail");
            }
        }
        return "success";
    }

    /**
     * @description 微信 判断文件大小是否符合要求
     * @param resourceSize
     *        上传文件大小
     *        图片（image）: 128K，支持JPG格式
     *        • 语音（voice）：256K，播放长度不超过60s，支持AMR格式
     *        • 视频（video）：1MB，支持MP4格式
     *        • 缩略图（thumb）：64KB，支持JPG格式
     * @param resourceType
     *        文件类型
     * @return 超过规定大小返回false， 符合要求返回true
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-25 下午03:18:00
     */
    public boolean compareWeixResource(long resourceSize, String resourceType)
    {
        boolean isFlag = false;
        try
        {
            if("image".equals(resourceType))
            {
                if(resourceSize - 512 * 1024 > 0)
                {
                    EmpExecutionContext.error("WeixBiz.compareWeixResource.image.size > 512 * 1024");
                    return false;
                }
            }
            else if("voice".equals(resourceType))
            {
                if(resourceSize - 256 * 1024 > 0)
                {
                    EmpExecutionContext.error("WeixBiz.compareWeixResource.voice.size > 256 * 1024");
                    return false;
                }
            }
            else if("video".equals(resourceType))
            {
                if(resourceSize - 1024 * 1024 > 0)
                {
                    EmpExecutionContext.error("WeixBiz.compareWeixResource.video.size > 1024 * 1024");
                    return false;
                }
            }
            else if("thumb".equals(resourceType))
            {
                if(resourceSize - 64 * 1024 > 0)
                {
                    EmpExecutionContext.error("WeixBiz.compareWeixResource.thumb.size > 64 * 1024");
                    return false;
                }
            }
            else
            {
                EmpExecutionContext.error("WeixBiz.compareWeixResource.resourceType Is Errer");
                return false;
            }
            isFlag = true;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.compareWeixResource Is Errer");
        }
        return isFlag;
    }

    /**
     * @description 获取 AccessToken
     * @return 成功返回 AccessToken ，失败返回error
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-3 上午08:45:02
     */
    private String getAccessToken(String appid, String appsecret)
    {
        String returnmsg = "error";
        try
        {
            WeixParams weixParams = new WeixParams();
            if(appid == null || "".equals(appid))
            {
                // 不合法的APPID
                EmpExecutionContext.error("WeixBiz.getAccessToken:" + WeixMessage.getValue("40013"));
                return returnmsg;
            }
            // 第三方用户唯一凭证密钥
            if(appsecret == null || "".equals(appsecret))
            {
                EmpExecutionContext.error("WeixBiz.getAccessToken.appsecret Is Null");
                return returnmsg;
            }
            weixParams.setAppid(appid);
            weixParams.setAppsecret(appsecret);
            IWeixinService weixinService = new WeixService();
            weixParams = weixinService.getWeixAccessToken(weixParams);
            if(weixParams.getErrCode() != null && "000".equals(weixParams.getErrCode()))
            {
                JSONObject object = weixParams.getJsonObj();
                if(object != null && object.get("access_token") != null)
                {
                    return object.get("access_token").toString();
                }
            }
            else
            {
                EmpExecutionContext.error("WeixBiz.getAccessToken errcode:" + weixParams.getErrCode() + " errmsg:" + weixParams.getErrMsg());
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.getAccessToken Is Error");
        }
        return returnmsg;
    }
    
    
    /**
     * 
     * @description    
     * @param msgtype        // 其他文件下载 图片 image/语音 voice/ 视频 video/音乐 music/ 图文news/qrcode二维码
     * @param accessToken
     * @param tickerOrmediaid   mediaid 或者 ticket
     * @return       		fail 失败    	  成功  FileUrl
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-17 下午03:19:48
     */
    public String downLoadResource(String msgtype,String accessToken,String ticketOrmediaid,String openId){
        String returnmsg = "fail";
        try{
            if(msgtype == null || "".equals(msgtype) || accessToken == null || "".equals(accessToken) || 
                    !("image".equals(msgtype) || "voice".equals(msgtype) || "video".equals(msgtype) || "qrcode".equals(msgtype))
                    || ticketOrmediaid == null || "".equals(ticketOrmediaid))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.downLoadResource.msgtype:" + msgtype + ",accessToken:" 
                        + accessToken + ",tickerOrmediaid:" + ticketOrmediaid);
                return returnmsg;
            }
            WeixParams weixParams = new WeixParams();
            weixParams.setMsgtype(msgtype);
            weixParams.setAccess_token(accessToken);
            if("qrcode".equals(msgtype))
            {
                weixParams.setTicket(ticketOrmediaid);
            }else{
                weixParams.setMedia_id(ticketOrmediaid);
            }
            IWeixinService service = new WeixService();
            weixParams = service.donwloadWeixResource(weixParams);
            if(weixParams != null && "000".equals(weixParams.getErrCode()))
            {
                returnmsg = weixParams.getFileUrl();
            }else{
            	// 如果验证失效，则重新获取token
            	if(weixParams != null && validateAssessToken(weixParams.getErrCode()))
            	{
                    reGetToken(openId);
            	}
            	if(weixParams != null){
            		EmpExecutionContext.error("WeixBiz.downLoadResource errcode:" + weixParams.getErrCode() + " errmsg:" + weixParams.getErrMsg());
            	}else{
            		EmpExecutionContext.error("WeixBiz.downLoadResource");
            	}
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.downLoadResource Is Error");
        }
        return returnmsg;
    }

    

    /**
     * @description 上传图片到本地服务器
     * @param path
     *        创建文件路径 String[] path = this.getWeixResourceUrl(msgtype);
     *        msgtype 为 :图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * @param msgtype
     *        图片类型
     * @param filename
     *        文件页面name的值
     * @param request
     *        请求
     * @return paraIsNull 参数不合法 oversize 文件超过大小 uoloadfail 上传失败 success 上传成功
     *         error出现异常
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-16 下午06:26:47
     */
    @SuppressWarnings("unchecked")
    public String uploadToServer(String[] path, String msgtype, String filename, HttpServletRequest request)
    {
        String returnmsg = "paraIsNull";
        try
        {
            if(path == null || path.length == 0)
            {
                EmpExecutionContext.error("WeixBiz.uploadToServer.path[] Is Null");
                return returnmsg;
            }
            if(msgtype == null || "".equals(msgtype) || filename == null || "".equals(filename) || 
                    !("image".equals(msgtype) || "voice".equals(msgtype) || "video".equals(msgtype)))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.uploadToServer.msgtype:" + msgtype + ",filename:" + filename);
                return returnmsg;
            }
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(100 * 1024);
            // 去掉文件名称的文件绝对路径
            factory.setRepository(new File(path[2]));
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);
            Iterator<FileItem> iter = items.iterator();
            while(iter.hasNext())
            {
                FileItem fileItem = iter.next();
                if(!compareWeixResource(fileItem.getSize(), msgtype))
                {
                    // 超过文件要求的大小
                    returnmsg = "oversize";
                    EmpExecutionContext.error("WeixBiz.uploadToServer Is oversize");
                    return returnmsg;
                }
                else if(!fileItem.isFormField() && fileItem.getName().length() > 0 && filename.equals(fileItem.getFieldName()))
                {
                    // 将文件写到本地服务器上 文件的绝对路径
                    fileItem.write(new File(path[0]));
                    // 判断是否使用集群 上传文件到文件服务器 文件的相对路径
                    if(WXStaticValue.ISCLUSTER == 1 && !"success".equals(new CommonBiz().uploadFileToFileCenter(path[1])))
                    {
                        // 是集群并且上传到文件服务器失败，则提示上传失败
                        returnmsg = "uoloadfail";
                        EmpExecutionContext.error("WeixBiz.uploadToServer.uploadFile Is Fail");
                        return returnmsg;
                    }
                    returnmsg = "success";
                }
            }

        }
        catch (Exception e)
        {
            returnmsg = "error";
            EmpExecutionContext.error(e, "WeixBiz.uploadToServer Is Error");
        }
        return returnmsg;
    }

    /**
     * @description 上传 微信服务器
     * @param filename
     *        文件页面name 值
     * @param fileurl
     *        相对文件路径
     * @param msgtype
     *        文件类型 msgtype 为 :图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * @param accessToken
     *        token
     * @return 失败fail 成功为 多媒体ID mediaid
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-16 下午06:44:18
     */
    public String uploadToWeix(String filename, String fileurl, String msgtype, String accessToken,String openId)
    {
        String returnmsg = "fail";
        try
        {
            if(accessToken == null || "".equals(accessToken) || msgtype == null 
                    || "".equals(msgtype) || filename == null || "".equals(filename) || 
                    !("image".equals(msgtype) || "voice".equals(msgtype) || "video".equals(msgtype)))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.sendResource.accessToken:" + accessToken  
                        + ",msgtype:" + msgtype + ",filename:" + filename);
                return returnmsg;
            }
            WeixParams weixParams = new WeixParams();
            // 以下是发送图片文件的，需要上传服务器 // 文件名称
            weixParams.setFilename(filename);
            weixParams.setMsgtype(msgtype);
            weixParams.setAccess_token(accessToken);
            IWeixinService service = new WeixService();
            // 设置文件的相对路径
            weixParams.setFileUrl(fileurl);
            // 上传文件至TX服务器
            weixParams = service.uploadWeixResource(weixParams);
            if(weixParams != null && "000".equals(weixParams.getErrCode()))
            {
                // 判断是否获取mediaid
                if(weixParams.getJsonObj().get("media_id") == null || "".equals(weixParams.getJsonObj().get("media_id")))
                {
                    returnmsg = "fail";
                    EmpExecutionContext.error("WeixBiz.uploadToWeix.getJsonObj() Is Null");
                    return returnmsg;
                }
                // 获取TX服务器多媒体IDmediaid
                String mediaid = (String) weixParams.getJsonObj().get("media_id");
                return mediaid;
            }
            else
            {
            	// 如果验证失效，则重新获取token
            	if(weixParams != null && validateAssessToken(weixParams.getErrCode()))
            	{
                    reGetToken(openId);
            	}
                returnmsg = "fail";
                EmpExecutionContext.error("WeixBiz.uploadToWeix.uploadTXFile Is Fail,code:" + (weixParams!=null?weixParams.getErrCode():""));
                return returnmsg;
            }
        }
        catch (Exception e)
        {
            returnmsg = "fail";
            EmpExecutionContext.error(e, "WeixBiz.uploadToWeix is error");
        }
        return returnmsg;
    }

    /**
     * 
     * @description    发送多媒体文件
     * @param mediaid   多媒体文件id
     * @param msgtype   文件类型
     * @param accessToken    token
     * @param touser    普通用户openid
     * @return       	fail 失败     	 success 成功
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-16 下午06:59:52
     */
    public String sendWeixMsg(String mediaid, String msgtype, String accessToken, String touser,String openId)
    {
        String returnmsg = "fail";
        try
        {
            if(accessToken == null || "".equals(accessToken) || touser == null || "".equals(touser) || msgtype == null || "".equals(msgtype) || mediaid == null || "".equals(mediaid) || !("image".equals(msgtype) || "voice".equals(msgtype) || "video".equals(msgtype)))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.sendWeixMsg.accessToken:" + accessToken + ",touser:" 
                        + touser + ",msgtype:" + msgtype + ",mediaid:" + mediaid);
                return returnmsg;
            }
            WeixParams weixParams = new WeixParams();
            weixParams.setAccess_token(accessToken);
            weixParams.setMsgtype(msgtype);
            weixParams.setTouser(touser);
            weixParams.setMedia_id(mediaid);
            IWeixinService service = new WeixService();
            weixParams = service.sendCustomerMsg(weixParams);
            if(weixParams != null && "000".equals(weixParams.getErrCode()))
            {
                returnmsg = "success";
                EmpExecutionContext.error("WeixBiz.sendWeixMsg is success,touser:"+touser+",mediaid:"+mediaid);
            }
            else
            {
            	// 如果验证失效，则重新获取token
            	if(weixParams != null && validateAssessToken(weixParams.getErrCode()))
            	{
                    reGetToken(openId);
            	}
            	if(weixParams != null){
                EmpExecutionContext.error("WeixBiz.sendWeixMsg errcode:" + weixParams.getErrCode() + " errmsg:" 
                        + weixParams.getErrMsg());
            	}else{
            		  EmpExecutionContext.error("WeixBiz.sendWeixMsg");
            	}
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.sendWeixMsg is error");
        }
        return returnmsg;
    }

    /**
     * @description 发送文本或者图片
     * @param msgtype
     *        文件类型 有文本 text 图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * @param accessToken
     *        token
     * @param touser
     *        普通用户ID
     * @param filename
     *        当发送 文件名称 。页面name 属性值
     * @param request
     *        页面请求，当发送文本时，则为null
     * @return 发送成功为true 失败为false
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-3 下午01:59:57
     */
    @SuppressWarnings("unchecked")
    public boolean sendResource(String msgtype, String accessToken, String touser, String filename, HttpServletRequest request)
    {
        boolean returnFlag = false;
        try
        {
            // 需要上传的文件类型 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
            IWeixinService service = null;
            if(msgtype == null || "".equals(msgtype))
            {
                // 上传文件类型
                EmpExecutionContext.error("WeixBiz.sendResource.Msgtyp Is Null");
                return returnFlag;
            }
            else if(!("image".equals(msgtype) || "voice".equals(msgtype) || "video".equals(msgtype) || "text".equals(msgtype)))
            {
                // 上传文件类型
                EmpExecutionContext.error("WeixBiz.sendResource.Msgtyp Is Error");
                return returnFlag;
            }
            if(accessToken == null || "".equals(accessToken))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.sendResource." + WeixMessage.getValue("40014"));
                return returnFlag;
            }
            if(touser == null || "".equals(touser))
            {
                // 不合法的OpenID
                EmpExecutionContext.error("WeixBiz.sendResource." + WeixMessage.getValue("40003"));
                return returnFlag;
            }
            WeixParams weixParams = new WeixParams();
            weixParams.setAccess_token(accessToken);
            weixParams.setTouser(touser);
            // 发送文本
            if("text".equals(msgtype))
            {
                service = new WeixService();
                weixParams = service.sendCustomerMsg(weixParams);
            }
            else
            {
                // 以下是发送图片文件的，需要上传服务器 // 文件名称
                if(filename == null || "".equals(filename))
                {
                    EmpExecutionContext.error("WeixBiz.sendResource.Filename Is Null");
                    return returnFlag;
                }
                else
                {
                    weixParams.setFilename(filename);
                }
                String[] path = getWeixResourceUrl(msgtype);
                if(path == null || path.length == 0)
                {
                    EmpExecutionContext.error("WeixBiz.sendResource.path[] Is Null");
                    return returnFlag;
                }
                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(100 * 1024);
                // 去掉文件名称的文件绝对路径
                factory.setRepository(new File(path[2]));
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = upload.parseRequest(request);
                Iterator<FileItem> iter = items.iterator();
                while(iter.hasNext())
                {
                    FileItem fileItem = iter.next();
                    if(!compareWeixResource(fileItem.getSize(), msgtype))
                    {
                        // 超过文件要求的大小
                        EmpExecutionContext.error("WeixBiz.sendResource Is oversize");
                        return returnFlag;
                    }
                    else if(!fileItem.isFormField() && fileItem.getName().length() > 0 && filename.equals(fileItem.getFieldName()))
                    {
                        // 将文件写到本地服务器上 文件的绝对路径
                        fileItem.write(new File(path[0]));
                        // 判断是否使用集群 上传文件到文件服务器 文件的相对路径
                        if(WXStaticValue.ISCLUSTER == 1 && !"success".equals(new CommonBiz().uploadFileToFileCenter(path[1])))
                        {
                            // 是集群并且上传到文件服务器失败，则提示上传失败
                            EmpExecutionContext.error("WeixBiz.sendResource.uploadFile Is Fail");
                            return returnFlag;
                        }
                        service = new WeixService();
                        // 设置文件的相对路径
                        weixParams.setFileUrl(path[1]);
                        // 上传文件至TX服务器
                        weixParams = service.uploadWeixResource(weixParams);
                        if(weixParams != null && "000".equals(weixParams.getErrCode()))
                        {
                            // 判断是否获取mediaid
                            if(weixParams.getJsonObj().get("media_id") == null || "".equals(weixParams.getJsonObj().get("media_id")))
                            {
                                EmpExecutionContext.error("WeixBiz.sendResource.getJsonObj() Is Null");
                                return returnFlag;
                            }
                            // 获取TX服务器多媒体ID
                            String mediaid = (String) weixParams.getJsonObj().get("media_id");
                            weixParams.setMedia_id(mediaid);
                            weixParams = service.sendCustomerMsg(weixParams);
                        }
                        else
                        {
                            EmpExecutionContext.error("WeixBiz.sendResource.uploadTXFile Is Fail,code:" + (weixParams!=null?weixParams.getErrCode():""));
                            return returnFlag;
                        }
                    }
                }
            }
            if(weixParams != null && "000".equals(weixParams.getErrCode()))
            {
                returnFlag = true;
            }
            else
            {
            	if(weixParams!=null){
                EmpExecutionContext.error("WeixBiz.sendResource errcode:" + weixParams.getErrCode() + " errmsg:" + weixParams.getErrMsg());
            	}else{
            		 EmpExecutionContext.error("WeixBiz.sendResource errcode:null errmsg:null");
            	}
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.sendResource Is Error");
        }
        return returnFlag;
    }

    /**
     * @description 获取微信用户的基本信息
     * @param accessToken
     *        Token
     * @param touser
     *        OPENID
     * @return 成功 LfWeiUserInfo对象 失败 返回null
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-3 下午02:15:08
     */
    public LfWeiUserInfo getWeixBaseUserInfo(String accessToken, String touser)
    {
        LfWeiUserInfo userInfo = new LfWeiUserInfo();
        userInfo.setOpenId(touser);
        try
        {
            if(accessToken == null || "".equals(accessToken))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.getWeixBaseUserInfo." + WeixMessage.getValue("40014"));
                return null;
            }
            if(touser == null || "".equals(touser))
            {
                // 不合法的OpenID
                EmpExecutionContext.error("WeixBiz.getWeixBaseUserInfo." + WeixMessage.getValue("40003"));
                return null;
            }
            WeixParams weixParams = new WeixParams();
            weixParams.setAccess_token(accessToken);
            weixParams.setTouser(touser);
            IWeixinService service = new WeixService();
            weixParams = service.getUserBaseMsg(weixParams);
            if(weixParams != null && "000".equals(weixParams.getErrCode()))
            {
                // {"errcode":40013,"errmsg":"invalid appid"}
                // 获取JSON中的数据 openid 有值说明获取正确
                JSONObject obj = weixParams.getJsonObj();
                if(obj.get("openid") != null && !"".equals(obj.get("openid")))
                {
                    if(obj.get("subscribe") != null)
                    {
                        String subscribe = obj.get("subscribe").toString();
                        // 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息
                        if("1".equals(subscribe))
                        {
                            userInfo.setSubscribe(subscribe);
                            if(obj.get("nickname") != null)
                            {
                                userInfo.setNickName(obj.get("nickname").toString());
                            }
                            if(obj.get("sex") != null)
                            {
                                userInfo.setSex(obj.get("sex").toString());
                            }
                            if(obj.get("language") != null)
                            {
                                userInfo.setLanguage(obj.get("language").toString());
                            }
                            if(obj.get("city") != null)
                            {
                                userInfo.setCity(obj.get("city").toString());
                            }
                            if(obj.get("province") != null)
                            {
                                userInfo.setProvince(obj.get("province").toString());
                            }
                            if(obj.get("country") != null)
                            {
                                userInfo.setCountry(obj.get("country").toString());
                            }
                            if(obj.get("headimgurl") != null)
                            {
                                userInfo.setHeadImgUrl(obj.get("headimgurl").toString());
                            }
                            if(obj.get("subscribe_time") != null)
                            {
                                userInfo.setSubscribeTime(new Timestamp(Long.valueOf(obj.get("subscribe_time").toString())));
                            }
                        }
                        else
                        {
                            userInfo = null;
                            EmpExecutionContext.error("WeixBiz.getWeixBaseUserInfo.LfWeiUserInfo.subscribe==0");
                        }
                    }
                    else
                    {
                        EmpExecutionContext.error("WeixBiz.getWeixBaseUserInfo.subscribe Is Null");
                    }
                }
            }
            else
            {
                EmpExecutionContext.error("WeixBiz.getWeixBaseUserInfo.getUserBaseMsg Is Fail");
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.getWeixBaseUserInfo Is Error");
        }
        return userInfo;
    }

    /**
     * @description 获取微信用户的基本信息
     * @param accessToken
     *        Token
     * @param touser
     *        OPENID
     * @return 成功 LinkedHashMap<String, Object>对象 失败 返回null
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-3 下午02:15:08
     */
    public boolean updateExistWeixUserInfo(String accessToken, LfWeiUserInfo userinfo)
    {
        boolean resuslt = false;
        //LinkedHashMap<String, Object> objectMap = null;
        try
        {
            if(accessToken == null || "".equals(accessToken))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.getWeixBaseUserInfo." + WeixMessage.getValue("40014"));
                return false;
            }
            WeixParams weixParams = new WeixParams();
            weixParams.setAccess_token(accessToken);
            weixParams.setTouser(userinfo.getOpenId());
            IWeixinService service = new WeixService();
            weixParams = service.getUserBaseMsg(weixParams);
            if(weixParams != null && "000".equals(weixParams.getErrCode()))
            {
                // {"errcode":40013,"errmsg":"invalid appid"}
                // 获取JSON中的数据 openid 有值说明获取正确
                JSONObject obj = weixParams.getJsonObj();
                if(obj.get("openid") != null && !"".equals(obj.get("openid")))
                {
                    if(obj.get("subscribe") != null)
                    {
                        //objectMap = new LinkedHashMap<String, Object>();
                        String subscribe = obj.get("subscribe").toString();
                        // 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息
                        if("1".equals(subscribe))
                        {
                            userinfo.setSubscribe(subscribe);
                            userinfo.setNickName(obj.get("nickname").toString());
                            userinfo.setSex(obj.get("sex").toString());
                            userinfo.setLanguage(obj.get("language").toString());
                            userinfo.setCity(obj.get("city").toString());
                            userinfo.setProvince(obj.get("province").toString());
                            userinfo.setCountry(obj.get("country").toString());
                            userinfo.setHeadImgUrl(obj.get("headimgurl").toString());
                            userinfo.setSubscribeTime(new Timestamp(Long.valueOf(obj.get("subscribe_time").toString())));
                            resuslt =  empDao.update(userinfo);
                        }
                        else
                        {
                            EmpExecutionContext.error("WeixBiz.getWeixBaseUserInfo.LfWeiUserInfo.subscribe==0");
                        }
                    }
                }
            }
            else
            {
                EmpExecutionContext.error("WeixBiz.getWeixBaseUserInfo.getUserBaseMsg Is Fail");
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.getWeixBaseUserInfo Is Error");
        }
        return resuslt;
    }
    
    /**
     * @description 获取微信群组列表
     * @param accessToken
     *        token
     * @return 成功返回 List<LfWeiGroup> ， 失败返回NULL
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-3 下午02:48:07
     */
    public List<LfWeiGroup> getWeixGroup(String accessToken,String Aid)
    {
        List<LfWeiGroup> groupList = null;
        try
        {
            if(accessToken == null || "".equals(accessToken))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.getWeixGroup." + WeixMessage.getValue("40014"));
                return null;
            }
            WeixParams weixParams = new WeixParams();
            weixParams.setAccess_token(accessToken);
            weixParams.setHandleGpType("query");
            IWeixinService service = new WeixService();
            weixParams = service.handleWeixGroup(weixParams);
            if(weixParams != null && "000".equals(weixParams.getErrCode()))
            {
                // 获取JSON中的数据 openid 有值说明获取正确
                JSONObject obj = weixParams.getJsonObj();
                // 获取groups列表
                if(obj != null && obj.get("groups") != null)
                {
                    JSONArray array = (JSONArray) obj.get("groups");
                    if(array != null && array.size() > 0)
                    {
                        groupList = new ArrayList<LfWeiGroup>();
                        LfWeiGroup group = null;
                        JSONObject object = null;
                        for (int i = 0; i < array.size(); i++)
                        {
                            group = new LfWeiGroup();
                            object = (JSONObject) array.get(i);
                            group.setWGId(Long.valueOf(object.get("id").toString()));
                            group.setCount(object.get("count").toString());
                            group.setName(object.get("name").toString());
                            groupList.add(group);
                            group = null;
                            object = null;
                        }
                    }
                }
                else if(obj != null && obj.get("errcode") != null && !"0".equals(obj.get("errcode").toString()))
                {
                    EmpExecutionContext.error("WeixBiz.getWeixGroup errcode:" + obj.get("errcode") + " errmsg:" + obj.get("errmsg"));
                }
                else
                {
                    EmpExecutionContext.error("WeixBiz.getWeixGroup.getJsonObj() Is Null");
                }
            }
            else
            {
            	// 如果验证失效，则重新获取token
            	if(weixParams != null && validateAssessToken(weixParams.getErrCode()))
            	{
                    reGetTokenByAid(Aid);
            	}
            	if(weixParams != null){
                    EmpExecutionContext.error("WeixBiz.sendWeixMsg errcode:" + weixParams.getErrCode() + " errmsg:" 
                            + weixParams.getErrMsg());
                }else{
                	EmpExecutionContext.error("WeixBiz.sendWeixMsg");
                }
            }
        }
        catch (Exception e)
        {
            groupList = null;
            EmpExecutionContext.error(e, "WeixBiz.getWeixGroup Is Error");
        }
        return groupList;
    }

    /**
     * @description 新增微信群组
     * @param accessToken
     *        token
     * @param name
     *        群组名称
     * @return 成功返回 LfWeiGroup对象，失败返回null
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-3 下午03:16:33
     */
    public LfWeiGroup createWeixGroup(String accessToken, String name)
    {
        LfWeiGroup weigroup = null;
        try
        {
            if(accessToken == null || "".equals(accessToken))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.createWeixGroup." + WeixMessage.getValue("40014"));
                return null;
            }
            if(name == null || "".equals(name))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.createWeixGroup.name Is Null");
                return null;
            }
            WeixParams weixParams = new WeixParams();
            weixParams.setAccess_token(accessToken);
            weixParams.setGroupName(name);
            weixParams.setHandleGpType("add");
            IWeixinService service = new WeixService();
            weixParams = service.handleWeixGroup(weixParams);
            if(weixParams != null && "000".equals(weixParams.getErrCode()))
            {
                // 获取JSON中的数据 openid 有值说明获取正确
                JSONObject obj = weixParams.getJsonObj();
                // 获取groups列表
                if(obj != null && obj.get("group") != null)
                {
                    JSONObject object = (JSONObject) obj.get("group");
                    weigroup = new LfWeiGroup();
                    // weigroup.setGId(Long.valueOf(object.get("id").toString()));
                    weigroup.setWGId(Long.valueOf(object.get("id").toString()));
                    weigroup.setName(object.get("name").toString());
                }
                else if(obj != null && obj.get("errcode") != null && !"0".equals(obj.get("errcode").toString()))
                {
                    EmpExecutionContext.error("WeixBiz.createWeixGroup errcode:" + obj.get("errcode") + " errmsg:" + obj.get("errmsg"));
                }
                else
                {
                    EmpExecutionContext.error("WeixBiz.createWeixGroup.getJsonObj() Is Null");
                }
            }
            else
            {
            	if(weixParams != null){
                    EmpExecutionContext.error("WeixBiz.sendWeixMsg errcode:" + weixParams.getErrCode() + " errmsg:" 
                            + weixParams.getErrMsg());
                	}else{
                		  EmpExecutionContext.error("WeixBiz.sendWeixMsg");
                	}
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.createWeixGroup Is Error");
        }
        return weigroup;
    }

    /**
     * @description 修改群组名称
     * @param accessToken
     *        token
     * @param WGId
     *        微信群组id
     * @param groupname
     *        群组名称
     * @return true成功 false失败
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-6 上午11:07:07
     */
    public boolean updateWeixGroup(String accessToken, String WGId, String groupname,String openId)
    {
        boolean isFlag = false;
        try
        {
            if(accessToken == null || "".equals(accessToken))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.updateWeixGroup." + WeixMessage.getValue("40014"));
                return isFlag;
            }
            if(groupname == null || "".equals(groupname))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.updateWeixGroup.name Is Null");
                return isFlag;
            }
            WeixParams weixParams = new WeixParams();
            weixParams.setAccess_token(accessToken);
            weixParams.setGroupId(WGId);
            weixParams.setGroupName(groupname);
            weixParams.setHandleGpType("update");
            IWeixinService service = new WeixService();
            weixParams = service.handleWeixGroup(weixParams);
            if(weixParams != null && "000".equals(weixParams.getErrCode()))
            {
                // 获取JSON中的数据 openid 有值说明获取正确
                JSONObject obj = weixParams.getJsonObj();
                // 获取groups列表
                if(obj != null && obj.get("errcode") != null && "0".equals(obj.get("errcode").toString()))
                {
                    isFlag = true;
                }
                else if(obj != null && obj.get("errcode") != null && !"0".equals(obj.get("errcode").toString()))
                {
                    EmpExecutionContext.error("WeixBiz.updateWeixGroup errcode:" + obj.get("errcode") + " errmsg:" + obj.get("errmsg"));
                }
                else
                {
                    EmpExecutionContext.error("WeixBiz.updateWeixGroup.getJsonObj() Is Null");
                }
            }
            else
            {
                // 如果验证失效，则重新获取token
                if(weixParams != null && validateAssessToken(weixParams.getErrCode()))
                {
                    reGetToken(openId);
                }
                if(weixParams != null){
                    EmpExecutionContext.error("WeixBiz.sendWeixMsg errcode:" + weixParams.getErrCode() + " errmsg:" 
                            + weixParams.getErrMsg());
                	}else{
                		  EmpExecutionContext.error("WeixBiz.sendWeixMsg");
                	}
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.updateWeixGroup Is Error");
        }
        return isFlag;
    }

    /**
     * @description 移动用户到群组
     * @param accessToken
     *        Token
     * @param openid
     *        OPENID 需要移动的用户
     * @param to_groupid
     *        指定群组ID
     * @return true成功 false失败
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-3 下午04:08:33
     */
    public boolean moveWeixGroupMenber(String accessToken, String openid, String to_groupid)
    {
        boolean isFlag = false;
        try
        {
            if(accessToken == null || "".equals(accessToken))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.updateWeixGroup." + WeixMessage.getValue("40014"));
                return isFlag;
            }
            if(openid == null || "".equals(openid))
            {
                EmpExecutionContext.error("WeixBiz.moveWeixGroupMenber.openid Is Null");
                return isFlag;
            }
            if(to_groupid == null || "".equals(to_groupid))
            {
                EmpExecutionContext.error("WeixBiz.moveWeixGroupMenber.to_groupid Is Null");
                return isFlag;
            }
            WeixParams weixParams = new WeixParams();
            weixParams.setAccess_token(accessToken);
            weixParams.setGroupId(to_groupid);
            weixParams.setTouser(openid);
            weixParams.setHandleGpType("move");
            IWeixinService service = new WeixService();
            weixParams = service.handleWeixGroup(weixParams);
            if(weixParams != null && "000".equals(weixParams.getErrCode()))
            {
                // 获取JSON中的数据 openid 有值说明获取正确
                JSONObject obj = weixParams.getJsonObj();
                // 获取groups列表
                if(obj != null && obj.get("errcode") != null && "0".equals(obj.get("errcode").toString()))
                {
                    isFlag = true;
                }
                else if(obj != null && obj.get("errcode") != null && !"0".equals(obj.get("errcode").toString()))
                {
                    EmpExecutionContext.error("WeixBiz.moveWeixGroupMenber errcode:" + obj.get("errcode") + " errmsg:" + obj.get("errmsg"));
                }
                else
                {
                    EmpExecutionContext.error("WeixBiz.moveWeixGroupMenber.getJsonObj() Is Null");
                }
            }
            else
            {
            	if(weixParams != null){
                    EmpExecutionContext.error("WeixBiz.sendWeixMsg errcode:" + weixParams.getErrCode() + " errmsg:" 
                            + weixParams.getErrMsg());
                	}else{
                		  EmpExecutionContext.error("WeixBiz.sendWeixMsg");
                	}
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.moveWeixGroupMenber Is Error");
        }
        return isFlag;
    }

    /**
     * @description 获取二维码的ticket
     * @param accessToken
     *        token
     * @param action_name
     *        二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久
     * @param scene_id
     *        场景值ID，临时二维码时为32位整型，永久二维码时最大值为1000
     * @return 成功 返回ticket字符串 失败返回error
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-3 下午04:43:52
     */
    public String getWeixTicket(String accessToken, String action_name, String scene_id)
    {
        String returnmsg = "error";
        try
        {
            if(accessToken == null || "".equals(accessToken))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.getWeixTicket." + WeixMessage.getValue("40014"));
                return returnmsg;
            }
            if(action_name == null || "".equals(action_name))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.getWeixTicket.action_name Is Null");
                return returnmsg;
            }
            if(scene_id == null || "".equals(scene_id))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.getWeixTicket.scene_id Is Null");
                return returnmsg;
            }
            WeixParams weixParams = new WeixParams();
            weixParams.setAccess_token(accessToken);
            weixParams.setScene_id(Long.valueOf(scene_id));
            // 当临时时设置有效时间，
            weixParams.setExpire_seconds(1800);
            weixParams.setAction_name(action_name);
            IWeixinService service = new WeixService();
            weixParams = service.qrcodeTicketWeix(weixParams);
            if(weixParams != null && "000".equals(weixParams.getErrCode()))
            {
                JSONObject obj = weixParams.getJsonObj();
                if(obj != null && obj.get("ticket") != null)
                {
                    returnmsg = obj.get("ticket").toString();
                }
                else if(obj != null && obj.get("errcode") != null)
                {
                    EmpExecutionContext.error("WeixBiz.getWeixTicket errcode:" + obj.get("errcode") + " errmsg:" + obj.get("errmsg"));
                }
                else
                {
                    EmpExecutionContext.error("WeixBiz.getWeixTicket.getJsonObj() Is Null");
                }
            }
            else
            {
            	if(weixParams != null){
                    EmpExecutionContext.error("WeixBiz.sendWeixMsg errcode:" + weixParams.getErrCode() + " errmsg:" 
                            + weixParams.getErrMsg());
                	}else{
                		  EmpExecutionContext.error("WeixBiz.sendWeixMsg");
                	}
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.getWeixTicket Is Error");
        }
        return returnmsg;
    }

    /**
     * @description 获取关注者列表
     * @param accessToken
     *        token
     * @param nextOpenid
     *        查询时下一个OPENID
     * @return 成功 JSONObject 失败 NULL
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-6 下午02:50:37
     */
    public JSONObject getAttentionList(String accessToken, String nextOpenid)
    {
        JSONObject obj = null;
        try
        {
            if(accessToken == null || "".equals(accessToken))
            {
                // 不合法的access_token
                EmpExecutionContext.error("WeixBiz.getAttentionList." + WeixMessage.getValue("40014"));
                return obj;
            }
            WeixParams weixParams = new WeixParams();
            weixParams.setAccess_token(accessToken);
            if(null != nextOpenid && !"".equals(nextOpenid) && !"null".equals(nextOpenid))
            {
                weixParams.setNext_openid(nextOpenid);
            }
            IWeixinService service = new WeixService();
            weixParams = service.getFollowersList(weixParams);
            if(weixParams != null && "000".equals(weixParams.getErrCode()))
            {
                obj = weixParams.getJsonObj();
                if(obj == null || obj.get("total") == null)
                {
                    EmpExecutionContext.error("WeixBiz.getAttentionList errcode:" + (obj!=null?obj.get("errcode").toString():"") + " errmsg:" + (obj!=null?obj.get("errmsg").toString():""));
                    obj = null;
                }
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "WeixBiz.getAttentionList Is Error");
        }
        return obj;
    }

    /**
     * 将微信同步过来的群组数据插入到数据库
     * 
     * @param accessToken
     * @param aId
     *        公共账号ID
     * @param corpCode
     *        企业编号
     * @author fanglu <fanglu@montnets.com>
     * @throws Exception
     * @datetime 2013-12-6 下午02:30:16
     */
    public List<LfWeiGroup> synchGroupInfo(String accessToken, String aId, String corpCode) throws Exception
    {
        List<LfWeiGroup> groupList = new ArrayList<LfWeiGroup>();

        // 查询出数据库里面已经存在的群组，从中提取出群组id（wgid）
        Collection<String> mywgidsStrings = new ArrayList<String>();
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("AId", aId);
        conditionMap.put("corpCode", corpCode);
        List<LfWeiGroup> myGroupList = empDao.findListByCondition(LfWeiGroup.class, conditionMap, null);
        if(null != myGroupList && myGroupList.size() > 0)
        {
            for (int i = 0; i < myGroupList.size(); i++)
            {
                mywgidsStrings.add(String.valueOf(myGroupList.get(i).getWGId()));
            }
        }

        // 调用微信接口并进行封装后返回的群组信息，从中提取出群组id（wgid）
        Collection<String> wxgidsStrings = new ArrayList<String>();
        List<LfWeiGroup> otWeiGrouplList = getWeixGroup(accessToken,aId);
        if(null != otWeiGrouplList && otWeiGrouplList.size() > 0)
        {
            for (int i = 0; i < otWeiGrouplList.size(); i++)
            {
                wxgidsStrings.add(String.valueOf(otWeiGrouplList.get(i).getWGId()));
            }
        }

        // 此处的交集找出的是数据库已经存在的群组id（wgid）
        Collection<String> intersection = CollectionUtils.intersection(mywgidsStrings, wxgidsStrings);
        Object[] intersectionStrings = new Object[0];
        // 如果有共同的数据，则直接进行更新操作(也可不更新，群组修改名称功能在我们这边，暂时不进行更新)
        if(null != intersection && intersection.size() > 0)
        {
            // 将wgid的collection集合转换成数组
            intersectionStrings = intersection.toArray();
        }

        Iterator<LfWeiGroup> sListIterator = null;
        LfWeiGroup otWeiGroup = null;

        // 从总数据集合中删除重复的数据，方便保存操作
        if(null != otWeiGrouplList)
        {
            sListIterator = otWeiGrouplList.iterator();
            while(sListIterator.hasNext())
            {
                LfWeiGroup owg = sListIterator.next();
                for (int i = 0; i < intersectionStrings.length; i++)
                {
                    if((String.valueOf(owg.getWGId())).equals(String.valueOf(intersectionStrings[i])))
                    {
                        sListIterator.remove();
                    }
                }
            }
            Long gid = new SuperDAO().getIdByPro(Integer.parseInt(TableLfWeiGroup.SEQUENCE), otWeiGrouplList.size());
            int size = otWeiGrouplList.size();
            for (int i = 0; i < size; i++)
            {
                otWeiGroup = otWeiGrouplList.get(i);
                otWeiGroup.setGId(Long.valueOf(gid - size + i + 1));
                otWeiGroup.setAId(Long.valueOf(aId));
                otWeiGroup.setCorpCode(corpCode);
                otWeiGroup.setCreatetime(Timestamp.valueOf(df.format(new Date())));
                // 如果是未分组，则count为所有用户数量，其它组的数量均为0
                if(0L == otWeiGroup.getWGId())
                {
                    // 为了获取所有关注者的数量
                    JSONObject userJsonObject = getAttentionList(accessToken, null);
                    String count = "0";
                    if(null != userJsonObject)
                    {
                        count = String.valueOf(userJsonObject.get("total"));
                    }
                    otWeiGroup.setName("未分组");
                    otWeiGroup.setCount(count);
                    groupList.add(0,otWeiGroup);
                    continue;
                }
                else if(1L == otWeiGroup.getWGId() || 2L == otWeiGroup.getWGId())
                {
                    // 不同步黑名单、星标组过来
                    continue;
                }
                else
                {
                    otWeiGroup.setCount("0");
                }
                groupList.add(otWeiGroup);
            }
        }
        return groupList;
    }

    /**
     * 将微信同步过来的用户数据插入到数据库
     * 
     * @param accessToken
     * @param corpCode
     * @author fanglu <fanglu@montnets.com>
     * @throws Exception
     * @datetime 2013-12-6 下午03:24:27
     */
    @SuppressWarnings("unchecked")
    public JSONObject synchUserInfo(String accessToken, String aId, String corpCode,Long gid) throws Exception
    {
        JSONObject opListObj = new JSONObject();
        List<LfWeiUserInfo> otWeiUserInfoList = new ArrayList<LfWeiUserInfo>();
        String next_openid = null;
        JSONObject jsonObject = new JSONObject();

        // 根据aid先查询出所有已经存在的用户的openId
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("AId", aId);
        List<LfWeiUserInfo> userInfos = empDao.findListByCondition(LfWeiUserInfo.class, conditionMap, null);
        Map<String, String> openIdMap = new HashMap<String, String>();
        if(null != userInfos)
        {
            for (int i = 0; i < userInfos.size(); i++)
            {
                openIdMap.put(userInfos.get(i).getOpenId(), String.valueOf(userInfos.get(i).getWcId()));
            }
        }

        conditionMap.clear();
        conditionMap.put("AId", aId);
        do
        {
            jsonObject = getAttentionList(accessToken, next_openid);
            if(null == jsonObject)
            {
                break;
            }
            next_openid = String.valueOf(jsonObject.get("next_openid"));
            JSONObject dataJsonObject = (JSONObject) jsonObject.get("data");

            if(null != dataJsonObject)
            {
                JSONArray openid = (JSONArray) StringUtils.parsJsonArrStr(String.valueOf(dataJsonObject.get("openid")));

                if(null != openid)
                {
                    // 此处的交集找出的是数据库已经存在的用户openid
                    Collection<String> intersection = CollectionUtils.intersection(openIdMap.keySet(), openid);

                    // 如果有共同的数据，则直接进行更新操作，并从原来集合中剔除这些存在的数据
                    if(null != intersection && intersection.size() > 0)
                    {
                        // 将openid的collection集合转换成数组
                        Object[] intersectionStrings = intersection.toArray();
                        // 调用微信接口，得到封装用户数据
                        Map<String, LinkedHashMap<String, Object>> updateMap = new HttpsRequestService().updateWeixUserInfo(intersectionStrings, accessToken, Long.valueOf(aId), corpCode);
                        List<LinkedHashMap<String, Object>> userLinkedHashMapList = new ArrayList<LinkedHashMap<String,Object>>();
                        List<String> userWcIdList = new ArrayList<String>();
                        for (int i = 0; i < intersectionStrings.length; i++)
                        {
                            LinkedHashMap<String, Object> userLinkedHashMap = updateMap.get(intersectionStrings[i]);
                            CustomChatBiz.getNameMap().put(intersectionStrings[i].toString(), null);
                            userLinkedHashMapList.add(userLinkedHashMap);
                            userWcIdList.add(openIdMap.get(intersectionStrings[i]).toString());
                        }
                        opListObj.put("userLinkedHashMapList", userLinkedHashMapList);
                        opListObj.put("userWcIdList", userWcIdList);
                        openid.removeAll(intersection);
                    }

                    if(openid.size() > 0)
                    {
                        // 根据openid调用微信接口找到详细信息，并保存到数据库
                        otWeiUserInfoList = new HttpsRequestService().addWeixUserInfo(openid, accessToken, Long.valueOf(aId), corpCode, gid);
                        opListObj.put("saveList", otWeiUserInfoList);
                    }
                }
            }
        } while(null != next_openid && !"".equals(next_openid) && !"null".equals(next_openid) );
        return opListObj;
    }

    /**
     * 同步群组信息和关注者信息
     * 
     * @description 确保在同一个事务中进行操作
     * @param accessToken
     * @param aId
     * @param corpCode
     * @return true成功 false失败
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-9 下午05:54:32
     */
    @SuppressWarnings("unchecked")
    public boolean syncDataFromWeix(String accessToken, String aId, String corpCode)
    {
        boolean isSynchOk = false;
        OttGenericTransactionDAO eTransDao = new OttGenericTransactionDAO();
        // 获取事务连接
        Connection conn = null;
        LfWeiAccount acct = null;
        try
        {

            // 同步开始时修改LF_WEI_ACCOUNT表的SYNC_STATE字段为0：同步中
            acct = empDao.findObjectByID(LfWeiAccount.class, Long.valueOf(aId));
            acct.setSyncState(0);
            acct.setModifyTime(Timestamp.valueOf(df.format(new Date())));

            empDao.update(acct);
            
            // 同步用户分组
            List<LfWeiGroup> groupList = synchGroupInfo(accessToken, aId, corpCode);
            
            conn = eTransDao.getConnection();    
            eTransDao.beginTransaction(conn);

            //未分组的群组id
            Long groupId = 0L;
            
            //将同步过来的群组信息插入到数据库
            if(null != groupList && groupList.size() > 0)
            {
            	if(groupList.get(0).getWGId() - 0 == 0)
            	{
            		groupId = groupList.get(0).getGId();
            	}else
            	{
            		groupId = getGroupId(aId, corpCode);
            	}
                eTransDao.savePro(conn, groupList, LfWeiGroup.class);
            }else
        	{
        		groupId = getGroupId(aId, corpCode);
        	}

            // 同步关注用户
            JSONObject userJsonObject = synchUserInfo(accessToken, aId, corpCode,groupId);
            
            //将同步过来的用户信息插入或更新到数据库
            Object updateUserListObject = null;
            Object updateWcidListObject = null;
            Object saveUserListObject = null;
            List<LfWeiUserInfo> otWeiUserInfoList = null;
            List<LinkedHashMap<String, Object>> userLinkedHashMapList = null;
            List<String> wcidList = null;
            
            //取出需要更新或保存的用户信息数据集合
            if(null != userJsonObject)
            {
                updateUserListObject = userJsonObject.get("userLinkedHashMapList");
                updateWcidListObject = userJsonObject.get("userWcIdList");
                saveUserListObject = userJsonObject.get("saveList");
            }
            if(null != updateUserListObject && null != updateWcidListObject)
            {
                userLinkedHashMapList = (List<LinkedHashMap<String, Object>>) updateUserListObject;
                wcidList = (List<String>) updateWcidListObject;
            }
            if(null != saveUserListObject)
            {
                
                otWeiUserInfoList = (List<LfWeiUserInfo>) saveUserListObject;
            }  
            
            WeixEmojiBiz emojiBiz = new WeixEmojiBiz();
            String nickName = null;
            //进行用户信息数据更新
            if(null != userLinkedHashMapList && null != wcidList)
            {
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap.put("AId", aId);
                
                for (int i = 0; i < userLinkedHashMapList.size(); i++)
                {
                    String wgid = wcidList.get(i);
                    LinkedHashMap<String, Object> userLinkedHashMap = userLinkedHashMapList.get(i);
                    nickName = userLinkedHashMap.get("nickName")==null?"":userLinkedHashMap.get("nickName").toString();
                    //过滤emoji表情
                    nickName = emojiBiz.filterEmoji(nickName);
                    if(nickName != null && nickName.length() > 0){
                    	userLinkedHashMap.put("nickName", nickName);
                    }
                    
                    eTransDao.update(conn, LfWeiUserInfo.class, userLinkedHashMap, wgid, conditionMap);
                }
            }
            
            
            //进行用户信息数据插入
            if(null != otWeiUserInfoList && otWeiUserInfoList.size() > 0)
            {
                //TODO-批量添加有问题
                //eTransDao.savePro(conn, otWeiUserInfoList, LfWeiUserInfo.class);
                for(LfWeiUserInfo userinfo : otWeiUserInfoList){
                	nickName = userinfo.getNickName();
                	//过滤emoji表情
                	nickName = emojiBiz.filterEmoji(nickName);
                	if(nickName != null && nickName.length() > 0){
                		userinfo.setNickName(nickName);
                    }
                	
                    eTransDao.saveObjProReturnID(conn, userinfo);
                }
            }

            eTransDao.commitTransaction(conn);
            
            //同步时间为空，说明这是第一次同步，需要往LF_WEI_COUNT表插入数据
            if(null == acct.getSyncTime())
            {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                df.setLenient(false);
                List<LfWeiCount> otWeiCountList = new ArrayList<LfWeiCount>();
                LfWeiCount otWeiCount = null;
                List<DynaBean> subscribeCountList = new OttSpecialDAO().getSubscribeCount(aId);
                if(null != subscribeCountList && subscribeCountList.size() > 0)
                {
                    for (int i = 0; i < subscribeCountList.size(); i++)
                    {
                        DynaBean dynaBean = subscribeCountList.get(i);
                        String subTime = String.valueOf(dynaBean.get("subtime"));
                        Integer subCount = Integer.valueOf(String.valueOf(dynaBean.get("subcount")));
                        otWeiCount = new LfWeiCount();
                        otWeiCount.setAId(Long.valueOf(aId));
                        otWeiCount.setCorpCode(corpCode);
                        otWeiCount.setDayTime(new Timestamp(df.parse(subTime).getTime()));
                        otWeiCount.setFollowCount(subCount);
                        otWeiCount.setIncomeCount(subCount);
                        otWeiCount.setUnfollowCount(0);
                        if(i==0)
                        {
                            otWeiCount.setAmountCount(subCount);
                        }
                        else
                        {
                            String countPre = String.valueOf(otWeiCountList.get(i-1).getAmountCount());
                            
                            otWeiCount.setAmountCount(subCount + Integer.parseInt(countPre));
                        }             
                        otWeiCountList.add(otWeiCount);
                    }        
                    eTransDao.save(conn, otWeiCountList, LfWeiCount.class);            
                }
            }
            
            // 同步完成时修改LF_WEI_ACCOUNT表的SYNC_STATE字段为1：同步完成
            acct.setSyncState(1);
            acct.setModifyTime(Timestamp.valueOf(df.format(new Date())));
            acct.setSyncTime(Timestamp.valueOf(df.format(new Date())));
        }
        catch (Exception e)
        {
            if(null != conn)
            {
                eTransDao.rollBackTransaction(conn);
            }
            EmpExecutionContext.error(e, "WeixBiz.synchGroupInfo Is Error");
        }
        finally
        {
            // 提交事务
            try
            {
                acct.setSyncState(1);
                eTransDao.update(conn, acct);
                eTransDao.commitTransaction(conn);
                isSynchOk = true;
            }
            catch (Exception e)
            {
                isSynchOk = false;
            }
           
            if(null != conn)
            {
                eTransDao.closeConnection(conn);
            }
            try
            {
            
            	// 更新群组的个数
            	new OttSpecialDAO().updateGroupCount(aId);
            	
            }catch(Exception e)
            {
            	EmpExecutionContext.error(e,"同步完成之后更新群组成员数量 ！");
            }
        }
        return isSynchOk;
    }
    
    /**
     * 根据error判断access_token是否合法(无效返回true)
     * @description    
     * @param errorCode
     * @return       			 
     * @author foyoto <foyoto@gmail.com>
     * @datetime 2014年7月2日 下午5:57:08
     */
    public boolean validateAssessToken(String errcode){
        boolean result = false;
        if(errcode.matches("4[0-2]001") || "40014".equals(errcode)){
            result = true;
        }
        return result;
    }
    
    private Long getGroupId(String aid,String corpCode)
    {
    	int gid = new OttSpecialDAO().getGroupId0(aid, corpCode);
    	return Long.valueOf(gid);
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
                    msgXml.append("<Url><![CDATA[").append(weixBasePath + "weix_imgDetail.hts?rimgid=" + String.valueOf(item.getRimgId())).append("]]></Url>");
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
}
