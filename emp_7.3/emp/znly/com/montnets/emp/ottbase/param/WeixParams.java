package com.montnets.emp.ottbase.param;

import java.io.Serializable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * 
 * @description     微信内容回复参数
 * @project ott
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-11-20 上午11:05:21
 */
public class WeixParams implements Serializable
{
    /**
     * @description  
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-22 下午04:52:03
     */
    private static final long serialVersionUID = -6939675930087165489L;
    //access_token
    private String access_token;
    //OPENID    普通用户openid 
    private String touser;
    //公众账号
    private String publicAccounts;
    //创建时间
    private long createTime;
    
    //  消息类型     文本   text/    图片   image/语音voice / 视频 video  /音乐  music/  图文news
    //下载文件类型    二维码        qrcode   图片   image/语音 voice / 视频  video/音乐  music/  图文news
    //上传   图片（image）、语音（voice）、视频（video）和缩略图（thumb，主要用于视频与音乐格式的缩略图）
    private String msgtype;
    //文本消息内容 
    private String content;
    
    //发送的图片的媒体ID      对应        图片/语音/视频
    private String media_id;
    //视频缩略图的媒体ID      对应        视频 / 音乐    
    private String thumb_media_id;
    
    //对应音乐      音乐标题 
    private String title;
    //音乐描述 
    private String description;
    //音乐链接 
    private String musicurl;
    //高品质音乐链接，wifi环境优先使用该链接播放音乐
    private String hqmusicurl;
    
    //错误编码  正确 返回 000
    private String errCode;
    
    //错误信息  正确  返回 success
    private String errMsg;
    //返回JSON格式
    private JSONObject jsonObj;
    //返回XML格式
    private String returnXml;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~
    //用于上传文件用到属性     &&  用于下载文件的相对路径
    //上传文件绝对路径   &&  下载文件的相对路径
    private String fileUrl;
    //上传文件页面name属性
    private String filename;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~
    //~~~~~~~~~~用于群组管理
    //操作群组类型     query查询   update更新    add新增  move移动
    private String handleGpType;
    //群组名称
    private String groupName;
    //群组ID    //用于   分组id
    private String groupId;
    
    //~~~~~~~~~~~~~~用于创建二维码TICKET
    //：{"expire_seconds": 1800, "action_name": "QR_SCENE", "action_info": {"scene": {"scene_id": 123}}}
    //该二维码有效时间，以秒为单位。 最大不超过1800
    private int expire_seconds;
    //二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久 
    private String action_name;
    //场景值ID，临时二维码时为32位整型，永久二维码时最大值为1000 
    private long scene_id;
    //获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
    private String ticket;
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~
    //~~~~~~~~~~用于获取用户关注者列表
    //第一个拉取的OPENID，不填默认从头开始拉取 
    private String next_openid;
    //微信用户的openid
    private String openid;
    //~~~~~~~~~~~~~~~~~~~~~~~~
    //第三方用户唯一凭证 
    private String appid ;
    //第三方用户唯一凭证密钥，既appsecret 
    private String appsecret;
    //网页授权获取用户基本信息-code参数
    private String code;
    //POST提交的数据
    private String data;
    //图文格式数据
    private JSONArray articles;
    
    public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public WeixParams(){
        this.errCode = "-9999";
    }
    
    public String getAccess_token()
    {
        return access_token;
    }
    public void setAccess_token(String accessToken)
    {
        access_token = accessToken;
    }
    public String getTouser()
    {
        return touser;
    }
    public void setTouser(String touser)
    {
        this.touser = touser;
    }
    public String getMsgtype()
    {
        return msgtype;
    }
    public void setMsgtype(String msgtype)
    {
        this.msgtype = msgtype;
    }
    public String getContent()
    {
        return content;
    }
    public void setContent(String content)
    {
        this.content = content;
    }
    public String getMedia_id()
    {
        return media_id;
    }
    public void setMedia_id(String mediaId)
    {
        media_id = mediaId;
    }
    public String getThumb_media_id()
    {
        return thumb_media_id;
    }
    public void setThumb_media_id(String thumbMediaId)
    {
        thumb_media_id = thumbMediaId;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public String getMusicurl()
    {
        return musicurl;
    }
    public void setMusicurl(String musicurl)
    {
        this.musicurl = musicurl;
    }
    public String getHqmusicurl()
    {
        return hqmusicurl;
    }
    public void setHqmusicurl(String hqmusicurl)
    {
        this.hqmusicurl = hqmusicurl;
    }
    public String getErrCode()
    {
        return errCode;
    }
    public void setErrCode(String errCode)
    {
        this.errCode = errCode;
    }
    public String getErrMsg()
    {
        return errMsg;
    }
    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }
   
    
    public JSONObject getJsonObj()
    {
        return jsonObj;
    }
    public void setJsonObj(JSONObject jsonObj)
    {
        this.jsonObj = jsonObj;
    }
    
    public String getFileUrl()
    {
        return fileUrl;
    }
    public void setFileUrl(String fileUrl)
    {
        this.fileUrl = fileUrl;
    }
    public String getFilename()
    {
        return filename;
    }
    public void setFilename(String filename)
    {
        this.filename = filename;
    }
    
    public String getGroupName()
    {
        return groupName;
    }
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }
    public String getGroupId()
    {
        return groupId;
    }
    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }
    
    public String getHandleGpType()
    {
        return handleGpType;
    }
    public void setHandleGpType(String handleGpType)
    {
        this.handleGpType = handleGpType;
    }
    public int getExpire_seconds()
    {
        return expire_seconds;
    }
    public void setExpire_seconds(int expireSeconds)
    {
        expire_seconds = expireSeconds;
    }
    public String getAction_name()
    {
        return action_name;
    }
    public void setAction_name(String actionName)
    {
        action_name = actionName;
    }
    public long getScene_id()
    {
        return scene_id;
    }
    public void setScene_id(long sceneId)
    {
        scene_id = sceneId;
    }
    public String getTicket()
    {
        return ticket;
    }
    public void setTicket(String ticket)
    {
        this.ticket = ticket;
    }
    public void setNext_openid(String nextOpenid)
    {
        next_openid = nextOpenid;
    }
    public String getNext_openid()
    {
        return next_openid;
    }
    public String getPublicAccounts()
    {
        return publicAccounts;
    }
    public void setPublicAccounts(String publicAccounts)
    {
        this.publicAccounts = publicAccounts;
    }
    public long getCreateTime()
    {
        return createTime;
    }
    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
    }
    public String getReturnXml()
    {
        return returnXml;
    }
    public void setReturnXml(String returnXml)
    {
        this.returnXml = returnXml;
    }
    
    public String getAppid()
    {
        return appid;
    }

    public void setAppid(String appid)
    {
        this.appid = appid;
    }

    public String getAppsecret()
    {
        return appsecret;
    }

    public void setAppsecret(String appsecret)
    {
        this.appsecret = appsecret;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setOpenid(String openid)
    {
        this.openid = openid;
    }

    public String getOpenid()
    {
        return openid;
    }

    public JSONArray getArticles()
    {
        return articles;
    }

    public void setArticles(JSONArray articles)
    {
        this.articles = articles;
    }
}
