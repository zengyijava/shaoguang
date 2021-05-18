/**
 * @description 响应消息之视频消息
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 下午02:35:49
 */
package com.montnets.emp.wxgl.base.message;

/**
 * @description 视频消息
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 下午02:35:49
 */

public class VideoMessage extends BaseMessage
{
    // 视频
    private Video Video;

    public Video getVideo()
    {
        return Video;
    }

    public void setVideo(Video video)
    {
        Video = video;
    }

}
