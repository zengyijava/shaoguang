/**
 * @description 统计管理 - 用户统计 - 用户属性
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2014-1-10 上午11:48:57
 */
package com.montnets.emp.wxgl.user.biz;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wxgl.LfWeiCount;
import com.montnets.emp.wxgl.user.dao.UserStatisticsDao;
import com.montnets.emp.wxgl.user.util.DateHelper;

/**
 * @description 统计管理 - 用户统计 - 用户属性
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2014-1-10 上午11:48:57
 */

public class UserStatisticsBiz extends SuperBiz
{
    UserStatisticsDao userStatisticsDao = new UserStatisticsDao();

    /**
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-10 上午11:48:57
     */
    public UserStatisticsBiz()
    {
        super();
    }

    /**
     * 查询用户性别统计结果
     * 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-10 上午11:54:45
     */
    public List<DynaBean> getUserSexDistributionList(String aid, String corpCode)
    {
        List<DynaBean> beans = null;
        try
        {
            beans = userStatisticsDao.getUserSexDistributionList(aid, corpCode);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "获取用户性别信息失败！");
        }
        return beans;
    }

    /**
     * 查询用户省份统计结果
     * 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-10 上午11:55:09
     */
    public List<DynaBean> getUserProvinceDistributionList(String aid, String corpCode)
    {
        List<DynaBean> beans = null;
        try
        {
            beans = userStatisticsDao.getUserProvinceDistributionList(aid, corpCode);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "获取用户性别信息失败！");
        }
        return beans;
    }

    /**
     * 查询用户城市统计结果
     * 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-10 上午11:55:14
     */
    public List<DynaBean> getUserCityDistributionList(String aid, String corpCode)
    {
        List<DynaBean> beans = null;
        try
        {
            beans = userStatisticsDao.getUserCityDistributionList(aid, corpCode);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "获取用户性别信息失败！");
        }
        return beans;
    }

    /**
     * 查询用户（本日、本周、本月）关注数
     * 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-14 上午11:23:55
     */
    public int getUserGrowCount(String startTime, String endTime, String aid, String corpCode)
    {
        int result = 0;
        try
        {
            result = userStatisticsDao.getUserGrowCount(startTime, endTime, aid, corpCode);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "查询用户增加情况失败！");
        }
        return result;
    }

    /**
     * 查询用户（本日、本周、本月）取消关注数
     * 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-14 下午12:57:11
     */
    public int getUserUnFollowCount(String startTime, String endTime, String aid, String corpCode)
    {
        int result = 0;
        try
        {
            result = userStatisticsDao.getUserUnFollowCount(startTime, endTime, aid, corpCode);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "查询累积关注用户数失败！");
        }
        return result;
    }

    /**
     * 查询累积关注用户数
     * 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-14 下午12:55:57
     */
    public int getSubscriberCount(String aid, String corpCode)
    {
        int result = 0;
        try
        {
            result = userStatisticsDao.getSubscriberCount(aid, corpCode);
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "查询累积关注用户数失败！");
        }
        return result;
    }

    /**
     * 根据起至日期查询用户增长信息
     */
    public List<LfWeiCount> getUserCountList(String corpCode, String aid, String startTime, String endTime)
    {
        return userStatisticsDao.getUserCountList(corpCode, aid, startTime, endTime);
    }

    /**
     * 获取用户增长统计XML数据
     * 
     * @author fangyt <foyoto@gmail.com>
     * @datetime 2014-1-18 下午06:50:44
     */
    @SuppressWarnings("unchecked")
    public String getUserCountXml(String corpCode, String aid, String tp, String startTime, String endTime)
    {
        JSONArray resultArray = new JSONArray();
        try
        {
            List<LfWeiCount> otWeiCountList = getUserCountList(corpCode, aid, startTime, endTime);
            Map<String, String> timeMap = new HashMap<String, String>();

            // 查询数据库已经存在的值
            if(null != otWeiCountList && otWeiCountList.size() > 0)
            {
                for (LfWeiCount otWeiCount : otWeiCountList)
                {
                    String followCount = String.valueOf(otWeiCount.getFollowCount());
                    String unfollowCount = String.valueOf(otWeiCount.getUnfollowCount());
                    String incomeCount = String.valueOf(otWeiCount.getIncomeCount());
                    String amountCount = String.valueOf(otWeiCount.getAmountCount());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String dayTime = df.format(otWeiCount.getDayTime());
                    if(null != followCount)
                    {
                        if("amount".equals(tp))
                        {
                        	if(null!=timeMap.get(dayTime)){
                        		amountCount = Integer.parseInt(amountCount)+Integer.parseInt(timeMap.get(dayTime))+ ""; 
                        	}
                            timeMap.put(dayTime, amountCount);
                        }
                        else if("unfollow".equals(tp))
                        {
                        	if(null!=timeMap.get(dayTime)){
                        		unfollowCount = Integer.parseInt(unfollowCount)+Integer.parseInt(timeMap.get(dayTime))+ ""; 
                        	}
                            timeMap.put(dayTime, unfollowCount);
                        }
                        else if("income".equals(tp))
                        {
                        	if(null!=timeMap.get(dayTime)){
                        		incomeCount = Integer.parseInt(incomeCount)+Integer.parseInt(timeMap.get(dayTime))+ ""; 
                        	}
                            timeMap.put(dayTime, incomeCount);
                        }
                        else
                        {
                        	if(null!=timeMap.get(dayTime)){
                        		followCount = Integer.parseInt(followCount)+Integer.parseInt(timeMap.get(dayTime))+ ""; 
                        	}
                            timeMap.put(dayTime, followCount);
                        }
                    }
                }
            }

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar nowDate = Calendar.getInstance();
            String nowTime = df.format(nowDate.getTime());
            // 判断用户选择的时间和今天时间比较,如果用户选择时间比今天时间小(结果返回的是负数)，则取用户选择时间，否则取今天时间
            int daysNum = DateHelper.getDaysBetween(nowTime, endTime);
            if(daysNum <= 0)
            {
                Date d = df.parse(endTime);
                nowDate.setTime(d);
                daysNum = DateHelper.getDaysBetween(startTime, endTime);
            }
            else
            {
                Date d = df.parse(nowTime);
                nowDate.setTime(d);
                daysNum = DateHelper.getDaysBetween(startTime, nowTime);
            }

            // 按照时间查询需要包括开始时间和结束时间，但是以上方法只包含结束时间，所以要+1
            daysNum = daysNum + 1;
            nowDate.add(Calendar.DATE, -1 * daysNum);
            // 算出两个日期的时间差
            String dtime = "";
            // 拿用户查询日期和数据库值进行比较，如果没有查询出相应的数据，则显示数据为0
            for (int i = 0; i < daysNum; i++)
            {
                JSONObject resultJson = new JSONObject();
                nowDate.add(Calendar.DATE, 1);
                dtime = df.format(nowDate.getTime());
                resultJson.put("xname", dtime + "   ");
                if(timeMap.get(dtime) == null)
                {
                    resultJson.put("yvalue", 0);
                }
                else
                {
                    resultJson.put("yvalue", timeMap.get(dtime));
                }
                resultArray.add(resultJson);
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "统计管理-用户增长-查询页面加载出错！");
        }
        return resultArray.toString();
    }

}
