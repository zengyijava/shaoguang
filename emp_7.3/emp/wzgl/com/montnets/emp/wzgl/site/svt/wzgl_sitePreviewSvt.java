/**
 * @description
 * @author Administrator <foyoto@gmail.com>
 * @datetime 2013-12-23 下午07:28:09
 */
package com.montnets.emp.wzgl.site.svt;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.site.LfSitInfo;
import com.montnets.emp.entity.site.LfSitPage;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.entity.wxgl.LfWeiUserInfo;
import com.montnets.emp.wzgl.site.biz.SiteBiz;

/**
 * @description
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author Administrator <foyoto@gmail.com>
 * @datetime 2013-12-23 下午07:28:09
 */

public class wzgl_sitePreviewSvt extends BaseServlet
{
    private static final long  serialVersionUID = -1079222152659642834L;
    // 资源路径
    public static final String PATH             = "/wzgl/site";
    
    /**
     * 公用siteBiz层
     */
    private final SiteBiz            siteBiz          = new SiteBiz();
    private final BaseBiz            baseBiz          = new BaseBiz();

    /**
     * 通过token值获取当前微站的页面
     * 1.直接传urlToken(微站的Token或者微站页面的Token)
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2013-12-31 上午10:04:44
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String urlToken = request.getParameter("urlToken");
        String from     = request.getParameter("from");
        LfSitPage  otSitPage = null;
        try
        {
            //当前页面类型
            otSitPage = siteBiz.getSitePageByToken(urlToken);
            request.setAttribute("otSitPage", otSitPage);
            request.setAttribute("from", from);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "微站预览失败！");
        }
        finally
        {
            if(null != otSitPage && null!=otSitPage.getPageId())
            {
                if(from!=null&&"pc".equals(from)){
                    request.getRequestDispatcher(PATH + "/wzgl_pagePreview.jsp").forward(request, response);
                }else{
                    request.getRequestDispatcher(PATH + "/wzgl_pagePreviewPhone.jsp").forward(request, response);
                }
            }
            else
            {
                request.getRequestDispatcher("/wxcommon/weixerror.jsp").forward(request, response);
            }
        }
    }
    
    /**
     * 通过网页授权的方式访问微站
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2013-12-31 上午10:04:44
     */
    @SuppressWarnings("unchecked")
    public void toAccessSite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // 开发者传递值(aid10004tid1003)
        String state = request.getParameter("state");
        // 当前微站对象
        LfSitInfo otSitInfo = null;
        // 当前位置页面
        LfSitPage  otSitPage = null;
        try
        {
            // 获取当前表单的id
            String sid = state.substring(state.indexOf("tid") + 3);
            // 当前访问的公众帐号id
            String aid = state.substring(state.indexOf("aid") + 3, state.indexOf("tid"));

            if(sid != null && !"".equals(sid) && aid != null && !"".equals(aid))
            {
                // 查询当前微站
                otSitInfo = baseBiz.getById(LfSitInfo.class, sid);
                if(otSitInfo == null)
                {
                    EmpExecutionContext.error("wzgl_sitePreviewSvt.toAccessSite.otSitInfo is null");
                    return;
                }else{
                    //当前页面类型
                    otSitPage = siteBiz.getSitePageByToken(otSitInfo.getUrl());
                    request.setAttribute("otSitPage", otSitPage);
                }
            }
            else
            {
                EmpExecutionContext.error("wzgl_sitePreviewSvt.toAccessSite siteid or aid is null");
                return;
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "wzgl_sitePreviewSvt.toAccessSite is error");
        }
        finally
        {            
            if(null != otSitPage && null!=otSitPage.getPageId())
            {
                request.getRequestDispatcher(PATH + "/wzgl_pagePreviewPhone.jsp").forward(request, response);
            }
            else
            {
                request.getRequestDispatcher("/wxcommon/weixerror.jsp").forward(request, response);
            }
        }

    }
}
