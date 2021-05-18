package com.montnets.emp.ottbase.service;

import com.montnets.emp.ottbase.param.HttpReturnParams;
import com.montnets.emp.ottbase.param.WeixParams;
/**
 * 
 * @description     微信接口
 * @project ott
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-11-21 下午01:42:31
 */
public interface IWeixinService
{
    /**
     * 
     * @description    发送客服接口
     * @param weixParams    微信内容参数类
     * @return     成功 ErrCode 000   ErrMsg success    失败  -9999         ErrMsg其他 
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-20 上午11:24:24
     */
    public WeixParams sendCustomerMsg(WeixParams weixParams) throws Exception;
    
    
    /**
     * 
     * @description    获取用户基本信息
     * @param weixParams      微信内容参数类
     * @return
     * @throws Exception       			 
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-21 下午01:42:56
     */
    public WeixParams getUserBaseMsg(WeixParams weixParams) throws Exception;
    
    
    
    /**
     * 
     * @description           下载微信资源
     * @param weixParams      微信内容参数类       二维码        qrcode   图片   image/语音 & 视频  voice/音乐  music/  图文news
     * @return                
     * @throws Exception       			 
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-21 下午02:17:49
     */
    public WeixParams donwloadWeixResource(WeixParams weixParams) throws Exception;
    
    
    /**
     * 
     * @description           上传微信资源
     * @param weixParams      微信内容参数类
     * @return                
     * @throws Exception                 
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-21 下午02:17:49
     */
    public WeixParams uploadWeixResource(WeixParams weixParams) throws Exception;
    
    
    
    /**
     * 
     * @description    处理微信 群组的操作           handleGpType : query查询   update更新    add新增  move移动
     * @param weixParams    微信传递参数类
     * @return
     * @throws Exception       			 
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-22 上午10:08:12
     */
    public WeixParams handleWeixGroup(WeixParams weixParams) throws Exception;
    
    
    /**
     * 
     * @description    创建二维码ticket
     * @param weixParams     微信传递参数类
     * @return
     * @throws Exception       			 
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-22 下午02:36:10
     */
    public WeixParams qrcodeTicketWeix(WeixParams weixParams) throws Exception;
    
    
    /**
     * @description    获取关注者列表
     * @param weixParams 微信传递参数类
     * @return 微信参数类
     * @throws Exception       			 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-11-25 下午02:09:49
     */
    public WeixParams getFollowersList(WeixParams weixParams) throws Exception;
    
    /**
     * 
     * @description    发送被动响应消息       封装XML格式
     * @param weixParams 微信传递参数类
     * @return
     * @throws Exception       			 
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-25 下午02:23:36
     */
    public WeixParams sendPassiveMsg(WeixParams weixParams) throws Exception;

    
    

    
    /**
     * 
     * @description     获取微信凭证
     * @param weixParams    微信传递参数类
     * @return
     * @throws Exception       			 
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-11-27 上午10:04:50
     */
    public WeixParams getWeixAccessToken(WeixParams weixParams) throws Exception;

    
    
    /**
     * @description   通过code换取网页授权access_token 
     * @param weixParams
     * @return
     * @throws Exception       			 
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2014-2-26 下午04:55:16
     */
    public WeixParams getOauth2AccessToken(WeixParams weixParams) throws Exception;
}
