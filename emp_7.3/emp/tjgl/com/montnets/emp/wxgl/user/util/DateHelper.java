/**
 * @description
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2014-1-14 上午10:54:41
 */
package com.montnets.emp.wxgl.user.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @description
 * @project OTT
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author fanglu <fanglu@montnets.com>
 * @datetime 2014-1-14 上午10:54:41
 */

public class DateHelper
{

    public static long FromYear()
    {
        Calendar nowDate = new java.util.GregorianCalendar();
        nowDate.set(Calendar.HOUR_OF_DAY, 0);
        nowDate.set(Calendar.MINUTE, 0);
        nowDate.set(Calendar.SECOND, 0);
        nowDate.set(Calendar.MILLISECOND, 0);
        nowDate.set(Calendar.DAY_OF_MONTH, 1);
        nowDate.set(Calendar.MONTH, 0);
        return nowDate.getTimeInMillis() / 1000;
    }

    public static long ToYear()
    {
        Calendar nowDate = new java.util.GregorianCalendar();
        nowDate.set(Calendar.HOUR_OF_DAY, 0);
        nowDate.set(Calendar.MINUTE, 0);
        nowDate.set(Calendar.SECOND, 0);
        nowDate.set(Calendar.MILLISECOND, 0);
        nowDate.set(Calendar.DAY_OF_MONTH, 1);
        nowDate.set(Calendar.MONTH, 11);
        return nowDate.getTimeInMillis() / 1000;
    }

    public static long FromMonth()
    {
        Calendar nowDate = new java.util.GregorianCalendar();
        nowDate.set(Calendar.HOUR_OF_DAY, 0);
        nowDate.set(Calendar.MINUTE, 0);
        nowDate.set(Calendar.SECOND, 0);
        nowDate.set(Calendar.MILLISECOND, 0);
        nowDate.set(Calendar.DAY_OF_MONTH, 1);
        return nowDate.getTimeInMillis() / 1000;
    }

    public static long ToMonth()
    {
        Calendar nowDate = new java.util.GregorianCalendar();
        nowDate.set(Calendar.HOUR_OF_DAY, 0);
        nowDate.set(Calendar.MINUTE, 0);
        nowDate.set(Calendar.SECOND, 0);
        nowDate.set(Calendar.MILLISECOND, 0);
        nowDate.set(Calendar.DAY_OF_MONTH, 1);
        nowDate.add(Calendar.MONTH, 1);
        return nowDate.getTimeInMillis() / 1000;
    }

    public static long FromWeek()
    {
        Calendar nowDate = new java.util.GregorianCalendar();
        nowDate.set(Calendar.HOUR_OF_DAY, 0);
        nowDate.set(Calendar.MINUTE, 0);
        nowDate.set(Calendar.SECOND, 0);
        nowDate.set(Calendar.MILLISECOND, 0);
        nowDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return nowDate.getTimeInMillis() / 1000;
    }

    public static long ToWeek()
    {
        Calendar nowDate = new java.util.GregorianCalendar();
        nowDate.set(Calendar.HOUR_OF_DAY, 0);
        nowDate.set(Calendar.MINUTE, 0);
        nowDate.set(Calendar.SECOND, 0);
        nowDate.set(Calendar.MILLISECOND, 0);
        nowDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        nowDate.add(Calendar.WEEK_OF_MONTH, 1);
        return nowDate.getTimeInMillis() / 1000;
    }

    public static long FromYesterday()
    {
        Calendar nowDate = new java.util.GregorianCalendar();
        nowDate.set(Calendar.HOUR_OF_DAY, 0);
        nowDate.set(Calendar.MINUTE, 0);
        nowDate.set(Calendar.SECOND, 0);
        nowDate.set(Calendar.MILLISECOND, 0);
        nowDate.add(Calendar.DAY_OF_MONTH, -1);
        return nowDate.getTimeInMillis() / 1000;
    }

    public static long ToYesterday()
    {
        Calendar nowDate = new java.util.GregorianCalendar();
        nowDate.set(Calendar.HOUR_OF_DAY, 0);
        nowDate.set(Calendar.MINUTE, 0);
        nowDate.set(Calendar.SECOND, 0);
        nowDate.set(Calendar.MILLISECOND, 0);
        return nowDate.getTimeInMillis() / 1000;
    }

    public static long FromToday()
    {
        Calendar nowDate = new java.util.GregorianCalendar();
        nowDate.set(Calendar.HOUR_OF_DAY, 0);
        nowDate.set(Calendar.MINUTE, 0);
        nowDate.set(Calendar.SECOND, 0);
        nowDate.set(Calendar.MILLISECOND, 0);
        return nowDate.getTimeInMillis() / 1000;
    }

    public static long ToToday()
    {
        Calendar nowDate = new java.util.GregorianCalendar();
        nowDate.set(Calendar.HOUR_OF_DAY, 0);
        nowDate.set(Calendar.MINUTE, 0);
        nowDate.set(Calendar.SECOND, 0);
        nowDate.set(Calendar.MILLISECOND, 0);
        nowDate.add(Calendar.DAY_OF_MONTH, 1);
        return nowDate.getTimeInMillis() / 1000;
    }

    /**
     * startDate 格式 2009-02-03
     * startTime 格式 12:20
     */
    public static long FromTime(String startDate, String startTime)
    {
        Calendar nowDate = new java.util.GregorianCalendar();
        long fromtime = 0;
        if(!startDate.equals(""))
        {
            String[] s = startDate.split("-");
            nowDate = new java.util.GregorianCalendar();
            nowDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s[2]));
            nowDate.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
            nowDate.set(Calendar.YEAR, Integer.parseInt(s[0]));

            String[] t = startTime.split(":");
            nowDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t[0]));
            nowDate.set(Calendar.MINUTE, Integer.parseInt(t[1]));
            nowDate.set(Calendar.SECOND, 0);
            fromtime = nowDate.getTimeInMillis() / 1000;
        }
        return fromtime;
    }

    /**
     * endDate 格式 2009-02-03
     * endTime 格式 12:20
     */
    public static long ToTime(String endDate, String endTime)
    {
        Calendar nowDate = new java.util.GregorianCalendar();
        long totime = 0;
        if(!endDate.equals(""))
        {
            String[] s = endDate.split("-");
            nowDate = new java.util.GregorianCalendar();
            nowDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(s[2]));
            nowDate.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);
            nowDate.set(Calendar.YEAR, Integer.parseInt(s[0]));

            String[] t = endTime.split(":");
            nowDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t[0]));
            nowDate.set(Calendar.MINUTE, Integer.parseInt(t[1]));
            nowDate.set(Calendar.SECOND, 0);
            totime = nowDate.getTimeInMillis() / 1000;
        }
        return totime;
    }

    /**
     * 格式化时间戳，参数为秒，不需要乘以1000
     * 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-3-6 下午04:28:10
     */
    public static String FormatTimeStamp(String pattern, long date)
    {
        if(pattern.length() == 0)
            pattern = "yyyy-MM-dd HH:mm:ss";
        java.util.Calendar nowDate = new java.util.GregorianCalendar();
        nowDate.setTimeInMillis(date * 1000);
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(nowDate.getTime());
    }

    /**
     * 取到两个日期之间的天数(计算出来的结果只包含结束日期，不包含开始日期)
     * 
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-3-6 下午04:27:38
     */
    public static int getDaysBetween(String beginDate, String endDate) throws ParseException
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date bDate = format.parse(beginDate);
        Date eDate = format.parse(endDate);
        Calendar d1 = new GregorianCalendar();
        d1.setTime(bDate);
        Calendar d2 = new GregorianCalendar();
        d2.setTime(eDate);
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if(d1.get(Calendar.YEAR) != y2)
        {
            d1 = (Calendar) d1.clone();
            do
            {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);// 得到当年的实际天数
                d1.add(Calendar.YEAR, 1);
            } while(d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }
}
