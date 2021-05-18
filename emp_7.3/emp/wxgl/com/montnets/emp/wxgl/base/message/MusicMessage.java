/**
 * @description 响应消息之音乐消息
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 上午10:57:35
 */
package com.montnets.emp.wxgl.base.message;

/**
 * @description 音乐消息
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 上午10:57:35
 */

public class MusicMessage extends BaseMessage
{
    // 音乐
    private Music Music;

    public Music getMusic()
    {
        return Music;
    }

    public void setMusic(Music music)
    {
        Music = music;
    }

}
