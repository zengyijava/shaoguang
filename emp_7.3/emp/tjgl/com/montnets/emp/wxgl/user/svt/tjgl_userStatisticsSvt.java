/**
 * @description 统计管理 - 用户统计
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2014-1-8 下午02:17:40
 */
package com.montnets.emp.wxgl.user.svt;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.ottbase.svt.BaseServlet;
import com.montnets.emp.wxgl.user.biz.UserStatisticsBiz;

/**
 * @description 统计管理 - 用户统计
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2014-1-8 下午02:17:40
 */

public class tjgl_userStatisticsSvt extends BaseServlet
{

    /**
     * @description serialVersionUID
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-8 下午02:18:00
     */
    private static final long   serialVersionUID  = -1159115124149554931L;

    /**
     * 资源路径
     */
    private static final String PATH              = "/wxgl/user";

    /**
     * baseBiz公用对象
     */
    final BaseBiz                     baseBiz           = new BaseBiz();

    /**
     * userStatisticsBiz公用对象
     */
    final UserStatisticsBiz           userStatisticsBiz = new UserStatisticsBiz();

    /**
     * @description 默认构造函数
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-8 下午02:17:41
     */
    public tjgl_userStatisticsSvt()
    {
        super();
    }

    /**
     * 进入 统计管理 - 用户统计 - 用户属性 统计查询页面(加载所有公共账号)
     * 
     * @param request
     * @param response
     * @throws Exception
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-8 下午02:26:34
     */
    public void find(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        try
        {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            // 当前登录企业
            String lgcorpcode = request.getParameter("lgcorpcode");
            // 用来存储所有公众账号
            conditionMap.put("corpCode", lgcorpcode);
            List<LfWeiAccount> otWeiAccList = baseBiz.getByCondition(LfWeiAccount.class, conditionMap, null);
            request.setAttribute("otWeiAccList", otWeiAccList);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "统计管理 - 用户统计 - 用户属性查询页面加载出错！");
        }
        finally
        {
            request.getRequestDispatcher(PATH + "/tjgl_userStatistics.jsp").forward(request, response);
        }
    }

    /**
     * 统计微信用户属性（性别、省份、城市）分布
     * 
     * @param request
     * @param response
     * @throws IOException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-8 下午02:30:15
     */
    public void findUserAttrDistribution(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String result = "fail@";
        try
        {
            String aid = request.getParameter("aid");
            String corpCode = request.getParameter("lgcorpcode");
            // 1：性别分布   2：省份分布  3：城市分布
            String typeid = request.getParameter("typeid");
            if(null == corpCode || "".equals(corpCode))
            {
                EmpExecutionContext.error("获取集团编码为空！");
            }
            else if (null == aid || "".equals(aid))
            {
                EmpExecutionContext.error("获取公共账号为空！");
            }
            else if (null == typeid || "".equals(typeid))
            {
                EmpExecutionContext.error("获取统计标识（用户属性）为空！");
            }
            else
            {
                List<DynaBean> otWeiUserInfoBeans = null;
                if("1".equals(typeid))
                {
                    otWeiUserInfoBeans = userStatisticsBiz.getUserSexDistributionList(aid, corpCode);
                    
                    if(null != otWeiUserInfoBeans && otWeiUserInfoBeans.size() > 0)
                    {
                        StringBuffer xmldateString = new StringBuffer();
                        xmldateString.append("<graph caption='性別分布图' xAxisName='性别' yAxisName='人数' showNames='1' decimalPrecision='0' formatNumberScale='0' outCnvBaseFontSize='14' paletteColors='AFD8F8,F6BD0F,8BBA00,FF8E46,008E8E,D64646,8E468E,588526,B3AA00,008ED6,9D080D,A186BE'>");
                        for(DynaBean bean : otWeiUserInfoBeans)
                        {
                            String sex = String.valueOf(bean.get("sex"));
                            String countsex = String.valueOf(bean.get("countsex"));
                            if(null != sex)
                            {
                                if("1".equals(sex))
                                {
                                    sex = "男";
                                }
                                else if("2".equals(sex))
                                {
                                    sex = "女";
                                }
                                else
                                {
                                    sex = "未知";
                                }    

                                xmldateString.append("<set name='");
                                xmldateString.append(sex);
                                xmldateString.append("' value='");
                                xmldateString.append(countsex);
                                xmldateString.append("'/>");
                            }
                        }
                        xmldateString.append("</graph>");
                        Document document = DocumentHelper.parseText(xmldateString.toString());
                        result = "success@" + document.asXML();
                    }
                    else
                    {
                        result = "success@";
                    }
                }
                else if("2".equals(typeid))
                {
                    otWeiUserInfoBeans = userStatisticsBiz.getUserProvinceDistributionList(aid, corpCode);
                    if(null != otWeiUserInfoBeans && otWeiUserInfoBeans.size() > 0)
                    {
                        StringBuffer xmldateString = new StringBuffer();
                        xmldateString.append("<graph caption='省份分布图' xAxisName='省份' yAxisName='人数' showNames='1' rotateNames='1' decimalPrecision='0' formatNumberScale='0' baseFontSize='14' paletteColors='AFD8F8,F6BD0F,8BBA00,FF8E46,008E8E,D64646,8E468E,588526,B3AA00,008ED6,9D080D,A186BE'>");
                        for(DynaBean bean : otWeiUserInfoBeans)
                        {
                            String province = String.valueOf(bean.get("province"));

                            if(null == province || "".equals(province) || "null".equals(province))

                            {
                                province = "未知";
                            }
                            String countprovince = String.valueOf(bean.get("countprovince"));

                            xmldateString.append("<set name='");
                            xmldateString.append(province);
                            xmldateString.append("' value='");
                            xmldateString.append(countprovince);
                            xmldateString.append("'/>");
                        }
                        xmldateString.append("</graph>");
                        Document document = DocumentHelper.parseText(xmldateString.toString());
                        result = "success@" + document.asXML();
                    }
                    else
                    {
                        result = "success@";
                    }
                }
                else if("3".equals(typeid))
                {
                    otWeiUserInfoBeans = userStatisticsBiz.getUserCityDistributionList(aid, corpCode);
                    if(null != otWeiUserInfoBeans && otWeiUserInfoBeans.size() > 0)
                    {
                        StringBuffer xmldateString = new StringBuffer();
                        xmldateString.append("<graph caption='城市分布图' xAxisName='城市' yAxisName='人数' showNames='1' decimalPrecision='0' formatNumberScale='0' outCnvBaseFontSize='14'  paletteColors='AFD8F8,F6BD0F,8BBA00,FF8E46,008E8E,D64646,8E468E,588526,B3AA00,008ED6,9D080D,A186BE'>");
                        for(DynaBean bean : otWeiUserInfoBeans)
                        {
                            String city = String.valueOf(bean.get("city"));
                            if(null == city || "".equals(city))
                            {
                                city = "未知";
                            }
                            String countcity = String.valueOf(bean.get("countcity"));

                            xmldateString.append("<set name='");
                            xmldateString.append(city);
                            xmldateString.append("' value='");
                            xmldateString.append(countcity);
                            xmldateString.append("'/>");
                        }
                        xmldateString.append("</graph>");
                        Document document = DocumentHelper.parseText(xmldateString.toString());
                        result = "success@" + document.asXML();
                    }
                    else
                    {
                        result = "success@";
                    }
                }
            }
        }
        catch (Exception exception)
        {
            EmpExecutionContext.error(exception, "在线客服-客服系统-客户评价记录查询页面加载出错！");
        }
        finally
        {
            response.getWriter().print(result);
        }
    }
}
