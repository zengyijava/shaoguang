/**
 * @description 语音消息中Video类的定义
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 下午02:33:08
 */
package com.montnets.emp.znly.message;

/**
 * @description 视频model
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 下午02:33:08
 */

public class Video
{
    // 通过上传多媒体文件，得到的id
    private String MediaId;

    // 视频消息的标题
    private String Title;

    // 视频消息的描述
    private String Description;

    public String getMediaId()
    {
        return MediaId;
    }

    public void setMediaId(String mediaId)
    {
        MediaId = mediaId;
    }

    public String getTitle()
    {
        return Title;
    }

    public void setTitle(String title)
    {
        Title = title;
    }

    public String getDescription()
    {
        return Description;
    }

    public void setDescription(String description)
    {
        Description = description;
    }

}
