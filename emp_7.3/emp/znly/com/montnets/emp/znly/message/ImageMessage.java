/**
 * @description 响应消息之图片消息
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 下午02:12:36
 */
package com.montnets.emp.znly.message;

/**
 * @description 图片消息
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 下午02:12:36
 */

public class ImageMessage extends BaseMessage
{
    // 图片
    private Image Image;

    public Image getImage()
    {
        return Image;
    }

    public void setImage(Image image)
    {
        Image = image;
    }

}
