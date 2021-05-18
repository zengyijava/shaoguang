/**
 * @description 语音消息中Voice类的定义
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 下午02:23:45
 */
package com.montnets.emp.znly.message;

/**
 * @description 语音model
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 下午02:23:45
 */

public class Voice
{
    // 通过上传多媒体文件，得到的id
    private String MediaId;

    public String getMediaId()
    {
        return MediaId;
    }

    public void setMediaId(String mediaId)
    {
        MediaId = mediaId;
    }
}
