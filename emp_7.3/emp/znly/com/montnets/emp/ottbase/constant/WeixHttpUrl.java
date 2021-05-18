package com.montnets.emp.ottbase.constant;

/**
 * @description 微信接口的请求地址(注意调用所有微信接口时均需使用https协议,改文档说明仅供参考，详细参考微信公众帐号官方文档。)
 * @project ott
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejm <282905282@qq.com>
 * @datetime 2013-11-13 下午02:25:12
 */
public class WeixHttpUrl
{
    /**
     * 名称：获取access token
     * http请求方式: GET
     * 请求形式：https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential
     * &appid=APPID&secret=APPSECRET
     * 返回结果：返回格式 成功 {"access_token":"ACCESS_TOKEN","expires_in":7200}
     * 失败 {"errcode":40013,"errmsg":"invalid appid"}
     */
    public static final String WX_ACCESSTOKEN_URL  = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    /**
     * 名称：上传下载多媒体文件(调用该接口需http协议)
     * http请求方式: POST/FORM
     * 请求形式: http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=
     * ACCESS_TOKEN&type=TYPE
     * type媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * 调用示例（使用curl命令，用FORM表单方式上传一个多媒体文件）：
     * •图片（image）: 128K，支持JPG格式
     * •语音（voice）：256K，播放长度不超过60s，支持AMR与MP3格式
     * •视频（video）：1MB，支持MP4格式
     * •缩略图（thumb）：64KB，支持JPG格式
     * 媒体文件在后台保存时间为3天，即3天后media_id失效。
     * 请求示例: curl -F media=@test.jpg"http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE"
     * 返回结果： 正确返回格式 {"type":"TYPE","media_id":"MEDIA_ID","created_at":123456789}
     */
    public static final String WX_UPLOAD_URL       = "http://file.api.weixin.qq.com/cgi-bin/media/upload?";

    /**
     * 名称: 获取多媒体文件
     * http请求方式: GET
     * 请求形式： http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=
     * ACCESS_TOKEN&media_id=MEDIA_ID
     * 请求示例（示例为通过curl命令获取多媒体文件）
     * curl -I -G"http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID"
     */
    public static final String WX_DOWNLOAD_URL     = "http://file.api.weixin.qq.com/cgi-bin/media/get?";

    /**
     * 名称: 发送客服消息
     * http请求方式: POST
     * 请求形式： https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=
     * ACCESS_TOKEN
     * 请求示例（示例为通过curl命令获取多媒体文件）
     * curl -I -G"http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID"
     */
    public static final String WX_SENDCUSTOM_URL   = "https://api.weixin.qq.com/cgi-bin/message/custom/send?";

    /**
     * 名称: 查询分组
     * http请求方式: GET（请使用https协议）
     * 请求形式：https://api.weixin.qq.com/cgi-bin/groups/get?access_token=
     * ACCESS_TOKEN
     */
    public static final String WX_QUERYGROUP_URL   = "https://api.weixin.qq.com/cgi-bin/groups/get?";

    /**
     * 名称: 创建分组(一个公众账号，最多支持创建500个分组,分组名字，UTF8编码 分组名字(30个字符以内))
     * http请求方式: POST（请使用https协议）
     * 请求形式： https://api.weixin.qq.com/cgi-bin/groups/create?access_token=
     * ACCESS_TOKEN
     * POST数据格式：json
     * 返回结果：{"group":{"name":"test"}}
     */
    public static final String WX_CREATEGROUP_URL  = "https://api.weixin.qq.com/cgi-bin/groups/create?";

    /**
     * 名称: 修改分组名
     * http请求方式: POST（请使用https协议）
     * 请求形式： https://api.weixin.qq.com/cgi-bin/groups/update?access_token=
     * ACCESS_TOKEN
     * POST数据格式：json
     * 返回结果：{"group":{"id":108,"name":"test2_modify2"}} 或者 {"errcode": 0,
     * "errmsg": "ok"}
     */
    public static final String WX_UPDATEGROUP_URL  = "https://api.weixin.qq.com/cgi-bin/groups/update?";

    /**
     * 名称: 移动用户分组
     * http请求方式: POST（请使用https协议）
     * 请求形式：
     * https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token
     * =ACCESS_TOKEN
     * POST数据格式：json
     * 返回结果：{"openid":"oDF3iYx0ro3_7jD4HFRDfrjdCM58","to_groupid":108}
     */
    public static final String WX_MOVEMEMBERS_URL  = "https://api.weixin.qq.com/cgi-bin/groups/members/update?";

    /**
     * 名称: 获取用户基本信息
     * http请求方式: GET
     * 请求形式：
     * https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN
     * &openid=OPENID
     * 数据格式: TODO
     * 返回结果: TODO
     */
    public static final String WX_GETUSERINFO_URL  = "https://api.weixin.qq.com/cgi-bin/user/info?";

    /**
     * 名称: 获取关注者列表
     * 说明： 公众号可通过本接口来获取帐号的关注者列表，关注者列表由一串OpenID（加密后的微信号，
     * 每个用户对每个公众号的OpenID是唯一的）组成。一次拉取调用最多拉取10000个关注者的OpenID，
     * 可以通过多次拉取的方式来满足需求
     * http请求方式: GET（请使用https协议）
     * 请求形式：
     * https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN
     * &next_openid=NEXT_OPENID
     * (next_openid 是 第一个拉取的OPENID，不填默认从头开始拉取)
     * 数据格式: TODO
     * 返回结果: TODO
     */
    public static final String WX_GETCAREUSER_URL  = "https://api.weixin.qq.com/cgi-bin/user/get?";

    /**
     * 名称: 自定义菜单
     * 说明： 用户点击click类型按钮后，微信服务器会通过消息接口推送消息类型为event
     * 的结构给开发者（参考消息接口指南），并且带上按钮中开发者填写的key值，
     * 开发者可以通过自定义的key值与用户进行交互。用户点击view类型按钮后，微信客户端将会打开开发者在按钮中填写的url值 （即网页链接），
     * 达到打开网页的目的，建议与网页授权获取用户基本信息接口结合，获得用户的登入个人信息。
     * http请求方式：POST(请使用https协议)
     * 请求形式：
     * https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN
     * (next_openid 是 第一个拉取的OPENID，不填默认从头开始拉取)
     * 数据格式: TODO
     * 返回结果: TODO
     */
    public static final String WX_CREATEMENU_URL   = "https://api.weixin.qq.com/cgi-bin/menu/create?";

    /**
     * 名称: 查询接口
     * 说明： 使用接口创建自定义菜单后，开发者还可使用接口查询自定义菜单的结构。
     * http请求方式: GET
     * 请求形式：
     * https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN
     * 数据格式: TODO
     * 返回结果: TODO
     */
    public static final String WX_QUERYMENU_URL    = "https://api.weixin.qq.com/cgi-bin/menu/get?";

    /**
     * 名称: 删除接口
     * 说明： 返回 出自微信公众平台开发者文档,使用接口创建自定义菜单后，开发者还可使用接口删除当前使用的自定义菜单。
     * http请求方式: GET
     * 请求形式：https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=
     * ACCESS_TOKEN
     * 数据格式: TODO
     * 返回结果: TODO
     */
    public static final String WX_DELETEMENU_URL   = "https://api.weixin.qq.com/cgi-bin/menu/delete?";

    /**
     * 为了满足用户渠道推广分析的需要，公众平台提供了生成带参数二维码的接口。
     * 使用该接口可以获得多个带不同场景值的二维码，用户扫描后，公众号可以接收到事件推送。
     * 创建二维码ticket
     * 每次创建二维码ticket需要提供一个开发者自行设定的参数（scene_id），分别介绍临时二维码和永久二维码的创建二维码ticket过程。
     * 临时二维码请求说明
     * http请求方式: POST
     * URL: https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN
     * POST数据格式：json
     * POST数据例子：{"expire_seconds": 1800, "action_name": "QR_SCENE",
     * "action_info": {"scene": {"scene_id": 123}}}
     * 永久二维码请求说明
     * http请求方式: POST
     * URL: https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN
     * POST数据格式：json
     * POST数据例子：{"action_name": "QR_LIMIT_SCENE", "action_info": {"scene":
     * {"scene_id": 123}}}
     * QR_SCENE为临时,QR_LIMIT_SCENE为永久
     * scene_id场景值ID，临时二维码时为32位整型，永久二维码时最大值为1000
     * 该二维码有效时间，以秒为单位。 最大不超过1800。
     */
    public static final String WX_CREATEGRCODE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?";

    /**
     * 名称: 通过ticket换取二维码
     * 说明： 获取二维码ticket后，开发者可用ticket换取二维码图片。请注意，本接口无须登录态即可调用。
     * http请求方式: GET请求（请使用https协议）
     * 请求形式：https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET
     * 数据格式: TODO
     * 返回结果: TODO
     */
    public static final String WX_SHOWGRCODE_URL   = "https://mp.weixin.qq.com/cgi-bin/showqrcode?";
    
    /**
     * 网页授权获取用户基本信息-详细参考微站公众平台文档
     */
    public static final String WX_AUTHORIZE_URL   = "https://open.weixin.qq.com/connect/oauth2/authorize?";
    
    /**
     * 网页授权获取用户基本信息-第二步：通过code换取网页授权access_token
     */
    public static final String WX_AUTHORIZE_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";
    
    /**
     * 分组群发功能提交地址
     */
    public static final String WX_SEND_ALL_GROUP_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?";
    
    /**
     * 根据OpenID列表群发功能提交地址
     */
    public static final String WX_SEND_ALL_OPENID_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/send?";
    
    /**
     * 上传图文消息素材
     */
    public static final String WX_UPLOADNEW = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?";
}
