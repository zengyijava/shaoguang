/**
 * @description 消息基类（公众帐号普通用户）
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 上午10:48:49
 */
package com.montnets.emp.wxgl.base.message;

/**
 * @description 消息基类
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 上午10:48:49
 */

public class BaseMessage
{
    // 接收方帐号（收到的 OpenID ）
    private String ToUserName;

    // 开发者微信号
    private String FromUserName;

    // 消息创建时间 （整型）
    private long   CreateTime;

    // 消息类型（text/music/news）
    private String MsgType;

    /**
     * @description 默认构造函数
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2013-12-12 上午10:48:49
     */
    public BaseMessage()
    {
        super();
    }

    public String getToUserName()
    {
        return ToUserName;
    }

    public void setToUserName(String toUserName)
    {
        ToUserName = toUserName;
    }

    public String getFromUserName()
    {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName)
    {
        FromUserName = fromUserName;
    }

    public long getCreateTime()
    {
        return CreateTime;
    }

    public void setCreateTime(long createTime)
    {
        CreateTime = createTime;
    }

    public String getMsgType()
    {
        return MsgType;
    }

    public void setMsgType(String msgType)
    {
        MsgType = msgType;
    }

}
