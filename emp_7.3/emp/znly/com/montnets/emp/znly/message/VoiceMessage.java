/**
 * @description 响应消息之语音消息
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 下午02:27:19
 */
package com.montnets.emp.znly.message;

/**
 * @description 语音消息
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 下午02:27:19
 */

public class VoiceMessage extends BaseMessage
{
    // 语音
    private Voice Voice;

    public Voice getVoice()
    {
        return Voice;
    }

    public void setVoice(Voice voice)
    {
        Voice = voice;
    }

}
