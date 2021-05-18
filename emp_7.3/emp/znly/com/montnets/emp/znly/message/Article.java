/**
 * @description 图文消息中Article类的定义
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 上午11:08:42
 */
package com.montnets.emp.znly.message;

/**
 * @description 图文model
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 上午11:08:42
 */

public class Article
{
    // 图文消息名称
    private String Title;

    // 图文消息描述
    private String Description;

    // 图片链接，支持JPG、PNG 格式，较好的效果为大图640*320，小图80*80，限 制图片链接的域名需要与开发者填写的基本资料中的Url一致
    private String PicUrl;

    // 点击图文消息跳转链接
    private String Url;

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
        return null == Description ? "" : Description;
    }

    public void setDescription(String description)
    {
        Description = description;
    }

    public String getPicUrl()
    {
        return null == PicUrl ? "" : PicUrl;
    }

    public void setPicUrl(String picUrl)
    {
        PicUrl = picUrl;
    }

    public String getUrl()
    {
        return null == Url ? "" : Url;
    }

    public void setUrl(String url)
    {
        Url = url;
    }

}
