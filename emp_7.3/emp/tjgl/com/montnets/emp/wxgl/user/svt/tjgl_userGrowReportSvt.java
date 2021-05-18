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

import org.json.simple.JSONObject;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wxgl.LfWeiAccount;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.ottbase.svt.BaseServlet;
import com.montnets.emp.wxgl.user.biz.UserStatisticsBiz;
import com.montnets.emp.wxgl.user.util.DateHelper;

/**
 * @description 统计管理 - 用户统计
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2014-1-8 下午02:17:40
 */

public class tjgl_userGrowReportSvt extends BaseServlet
{

    /**
     * @description serialVersionUID
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-14 上午09:43:15
     */
    private static final long   serialVersionUID  = -3977099765407235624L;

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
    public tjgl_userGrowReportSvt()
    {
        super();
    }

    /**
     * 进入 统计管理 - 用户统计 - 用户增长 统计查询页面(加载所有公共账号)
     * 
     * @param request
     * @param response
     * @throws Exception
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-14 上午10:24:48
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
            EmpExecutionContext.error(e, "统计管理 - 用户统计 - 用户增长查询页面加载出错！");
        }
        finally
        {
            request.getRequestDispatcher(PATH + "/tjgl_userGrowReport.jsp").forward(request, response);
        }
    }

    /**
     * 统计微信用户（本日、本周、本月）增长情况
     * 
     * @param request
     * @param response
     * @throws IOException
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-14 上午10:24:30
     */
    @SuppressWarnings("unchecked")
    public void findUserGrowthInfo(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String result = "fail@";
        try
        {
            String aid = request.getParameter("aid");
            String corpCode = request.getParameter("lgcorpcode");
            if(null == corpCode || "".equals(corpCode))
            {
                EmpExecutionContext.error("企业编码传入为空！corpcode："+corpCode);
            }
            else if(null == aid || "".equals(aid)||"null".equals(aid))
            {
                EmpExecutionContext.error("未建公众号，或者未选择公众号条件，传入的公众号为空 aid："+aid);
            }
            else
            {
                // 本日开始时间、结束时间
                String todayStartTime = DateHelper.FormatTimeStamp("", DateHelper.FromToday());
                String todayEndTime = DateHelper.FormatTimeStamp("", DateHelper.ToToday());

                // 本周开始时间、结束时间
                String weekStartTime = DateHelper.FormatTimeStamp("", DateHelper.FromWeek());
                String weekEndTime = DateHelper.FormatTimeStamp("", DateHelper.ToWeek());

                // 本月开始时间、结束时间
                String monthStartTime = DateHelper.FormatTimeStamp("", DateHelper.FromMonth());
                String monthEndTime = DateHelper.FormatTimeStamp("", DateHelper.ToMonth());

                // 本日关注人数
                int todaySubCount = userStatisticsBiz.getUserGrowCount(todayStartTime, todayEndTime, aid, corpCode);

                // 本周关注人数
                int weekSubCount = userStatisticsBiz.getUserGrowCount(weekStartTime, weekEndTime, aid, corpCode);

                // 本月关注人数
                int monthSubCount = userStatisticsBiz.getUserGrowCount(monthStartTime, monthEndTime, aid, corpCode);

                // 本日取消关注人数
                int todayCancelCount = userStatisticsBiz.getUserUnFollowCount(todayStartTime, todayEndTime, aid, corpCode);

                // 本周取消关注人数
                int weekCancelCount = userStatisticsBiz.getUserUnFollowCount(weekStartTime, weekEndTime, aid, corpCode);

                // 本月取消关注人数
                int monthCancelCount = userStatisticsBiz.getUserUnFollowCount(monthStartTime, monthEndTime, aid, corpCode);

                // 本日净增关注人数
                int todayNetGrowthCount = todaySubCount - todayCancelCount;

                // 本周净增关注人数
                int weekNetGrowthCount = weekSubCount - weekCancelCount;

                // 本月净增关注人数
                int monthNetGrowthCount = monthSubCount - monthCancelCount;

                // 累积关注人数
                int totalSubscriberCount = userStatisticsBiz.getSubscriberCount(aid, corpCode);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("todaySubCount", todaySubCount);
                jsonObject.put("weekSubCount", weekSubCount);
                jsonObject.put("monthSubCount", monthSubCount);
                jsonObject.put("todayCancelCount", todayCancelCount);
                jsonObject.put("weekCancelCount", weekCancelCount);
                jsonObject.put("monthCancelCount", monthCancelCount);
                jsonObject.put("todayNetGrowthCount", todayNetGrowthCount);
                jsonObject.put("weekNetGrowthCount", weekNetGrowthCount);
                jsonObject.put("monthNetGrowthCount", monthNetGrowthCount);
                jsonObject.put("totalSubscriberCount", totalSubscriberCount);
                String newGrowth = request.getParameter("newGrowth");

                String startTime = "";
                String endTime = "";

                if("1".equals(newGrowth))
                {
                    // 起始日期
                    startTime = request.getParameter("startdate");
                    startTime = startTime + " 00:00:00";
                    // 结束日期
                    endTime = request.getParameter("enddate");
                    endTime = endTime + " 23:59:59";
                }
                else if("3".equals(newGrowth))
                {
                    // 月起始日期
                    startTime = monthStartTime;
                    // 月结束日期
                    endTime = monthEndTime;
                }
                else
                {
                    // 本周开始时间
                    startTime = weekStartTime;
                    // 今天结束时间
                    endTime = todayEndTime;
                }

                // 统计类型
                String tp = request.getParameter("tp");

                // 查询后默认加载本日新增关注人数图表
                // String year = todayStartTime.substring(0,4);
                // String month = todayStartTime.substring(5,7);
                // String startDay = todayStartTime.substring(8,10);
                // String endDay = todayEndTime.substring(8,10);
                // String startTime = weekStartTime;
                // String endTime = todayEndTime;
                // String reportData = getUserReportList(aid,
                // corpCode,startTime, endTime);
                String reportData = userStatisticsBiz.getUserCountXml(corpCode, aid, tp, startTime, endTime);
                result = "success@" + jsonObject.toString() + "$" + reportData;
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
