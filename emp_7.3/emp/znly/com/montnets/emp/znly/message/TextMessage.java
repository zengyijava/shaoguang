/**
 * @description 响应消息之文本消息
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 上午10:55:48
 */
package com.montnets.emp.znly.message;

/**
 * @description 文本消息
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 上午10:55:48
 */

public class TextMessage extends BaseMessage
{
    // 回复的消息内容
    private String Content;

    public String getContent()
    {
        return Content;
    }

    public void setContent(String content)
    {
        Content = content;
    }

}
