/**
 * @description 响应消息之图片消息
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 上午11:07:13
 */
package com.montnets.emp.znly.message;

import java.util.List;

/**
 * @description 图文消息
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2013-12-12 上午11:07:13
 */

public class NewsMessage extends BaseMessage
{
    // 图文消息个数，限制为10条以内
    private int           ArticleCount;

    // 多条图文消息信息，默认第一个item为大图
    private List<Article> Articles;

    public int getArticleCount()
    {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount)
    {
        ArticleCount = articleCount;
    }

    public List<Article> getArticles()
    {
        return Articles;
    }

    public void setArticles(List<Article> articles)
    {
        Articles = articles;
    }

}
