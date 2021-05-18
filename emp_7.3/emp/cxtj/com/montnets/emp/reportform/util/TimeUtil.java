package com.montnets.emp.reportform.util;

import com.montnets.emp.common.context.EmpExecutionContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Title: TimeUtil
 * @project emp_7.3
 * @author: lianghuageng
 * @Date: 2018/12/10 15:11
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @Description:
 */
public final class TimeUtil {

    private TimeUtil() {
    }

    /**
     * 解析一个日期之间的所有月份
     *
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    public static ArrayList<String> getMonthList(String beginDateStr,
                                                 String endDateStr) {
        // 指定要解析的时间格式
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
        // 返回的月份列表
        String sRet = "";

        // 定义一些变量
        Date beginDate = null;
        Date endDate = null;

        GregorianCalendar beginGC = null;
        GregorianCalendar endGC = null;
        ArrayList<String> list = new ArrayList<String>();

        try {
            // 将字符串parse成日期
            beginDate = f.parse(beginDateStr);
            endDate = f.parse(endDateStr);

            // 设置日历
            beginGC = new GregorianCalendar();
            beginGC.setTime(beginDate);

            endGC = new GregorianCalendar();
            endGC.setTime(endDate);

            // 直到两个时间相同
            while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {
                sRet = beginGC.get(Calendar.YEAR) + "-"
                        + (beginGC.get(Calendar.MONTH) + 1);
                list.add(sRet);
                // 以月为单位，增加时间
                beginGC.add(Calendar.MONTH, 1);
            }
            return list;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> getMonthList() 解析一个日期之间的所有月份异常!");
            return null;
        }
    }

    /**
     * 解析一个日期段之间的所有日期
     *
     * @param beginDateStr 开始日期
     * @param endDateStr   结束日期
     * @return
     */
    public static ArrayList<String> getDayList(String beginDateStr,
                                               String endDateStr) {
        // 指定要解析的时间格式
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        // 定义一些变量
        Date beginDate = null;
        Date endDate = null;

        Calendar beginGC = null;
        Calendar endGC = null;
        ArrayList<String> list = new ArrayList<String>();

        try {
            // 将字符串parse成日期
            beginDate = f.parse(beginDateStr);
            endDate = f.parse(endDateStr);

            // 设置日历
            beginGC = Calendar.getInstance();
            beginGC.setTime(beginDate);

            endGC = Calendar.getInstance();
            endGC.setTime(endDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // 直到两个时间相同
            while (beginGC.getTime().compareTo(endGC.getTime()) <= 0) {

                list.add(sdf.format(beginGC.getTime()));
                // 以日为单位，增加时间
                beginGC.add(Calendar.DAY_OF_MONTH, 1);
            }
            return list;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> getDayList() 解析一个日期段之间的所有日期异常!");
            return null;
        }
    }

    public static ArrayList<Integer> getYearList() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        Calendar c = null;
        c = Calendar.getInstance();
        c.setTime(new Date());
        int currYear = Calendar.getInstance().get(Calendar.YEAR);

        int startYear = currYear - 5;
        int endYear = currYear + 10;
        for (int i = startYear; i < endYear; i++) {
            list.add(new Integer(i));
        }
        return list;
    }

    /**
     * 获取当前年
     *
     * @return
     */
    public static int getCurrYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 得到某一年周的总数
     *
     * @param year
     * @return
     */
    public static LinkedHashMap<Integer, String> getWeekList(int year) {
        LinkedHashMap<Integer, String> map = new LinkedHashMap<Integer, String>();
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        int count = getWeekOfYear(c.getTime());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dayOfWeekStart = "";
        String dayOfWeekEnd = "";
        for (int i = 1; i <= count; i++) {
            dayOfWeekStart = sdf.format(getFirstDayOfWeek(year, i));
            dayOfWeekEnd = sdf.format(getLastDayOfWeek(year, i));
            map.put(new Integer(i), "第" + i + "周(从" + dayOfWeekStart + "至"
                    + dayOfWeekEnd + ")");
        }
        return map;

    }

    /**
     * 得到一年的总周数
     *
     * @param year
     * @return
     */
    public static int getWeekCountInYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        int count = getWeekOfYear(c.getTime());
        return count;
    }

    /**
     * 取得当前日期是多少周
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);

        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 得到某年某周的第一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONDAY, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getFirstDayOfWeek(cal.getTime());
    }

    /**
     * 得到某年某周的最后一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONDAY, 0);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return getLastDayOfWeek(cal.getTime());
    }

    /**
     * 得到某年某月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getFirestDayOfMonth(int year, int month) {
        month = month - 1;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        @SuppressWarnings("static-access")
        int day = c.getActualMinimum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTime();

    }

    /**
     * 提到某年某月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(int year, int month) {
        month = month - 1;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        @SuppressWarnings("static-access")
        int day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        c.setTime(date);
        // Monday
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        return c.getTime();
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        c.setTime(date);
        // Sunday
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
        return c.getTime();
    }

    /**
     * 得到某年某季度第一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static Date getFirstDayOfQuarter(int year, int quarter) {
        int month = 0;
        if (quarter > 4) {
            return null;
        } else {
            month = (quarter - 1) * 3 + 1;
        }
        return getFirstDayOfMonth(year, month);
    }

    /**
     * 得到某年某季度最后一天
     *
     * @param year
     * @param quarter
     * @return
     */
    public static Date getLastDayOfQuarter(int year, int quarter) {
        int month = 0;
        if (quarter > 4) {
            return null;
        } else {
            month = quarter * 3;
        }
        return getLastDayOfMonth(year, month);
    }

    /**
     * 得到某年第一天
     *
     * @param year
     * @return
     */
    public static Date getFirstDayOfYear(int year) {
        return getFirstDayOfQuarter(year, 1);
    }

    /**
     * 得到某年最后一天
     *
     * @param year
     * @return
     */
    public static Date getLastDayOfYear(int year) {
        return getLastDayOfQuarter(year, 4);
    }

    /**
     * 得到某年某月的第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static Date getFirstDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));

        return c.getTime();
    }

    /**
     * 获取某年某月某一周的第一天和最后一天（flag=true表示取第一天）
     *
     * @param year
     * @param month
     * @param week
     * @param flag
     * @return
     */
    public static String getDayByWeek(int year, int month, int week,
                                      boolean flag) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.WEEK_OF_MONTH, week);
        int dw = cal.get(Calendar.DAY_OF_WEEK);
        if (!flag) {
            cal.setTimeInMillis(cal.getTimeInMillis() + (8 - dw) * 24 * 60 * 60
                    * 1000);
        } else {
            cal.setTimeInMillis(cal.getTimeInMillis() - (dw - 2) * 24 * 60 * 60
                    * 1000);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String showTime = formatter.format(cal.getTime());
        return showTime.toString();
    }

    public static String getStringToDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 转化字符串时间格式(yyyy年MM月dd日)
     *
     * @param strTime
     * @return
     * @throws ParseException
     */
    public static String formatTimeString(String strTime) throws ParseException {
        SimpleDateFormat getsdf = new SimpleDateFormat(
        );
        SimpleDateFormat resdf = new SimpleDateFormat("yyyy年MM月dd日");
        return resdf.format(getsdf.parse(strTime));
    }

    /**
     * 转化时间格式(yyyy-MM-dd HH:mm:ss.sss)
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static String formatTimeString(Date time) {
        if (null == time) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
            return sdf.format(time);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "--> formatTimeString(Date time) 转化时间格式异常!");
        }
        return time.toString();
    }

    /**
     * 转化字符串时间格式
     *
     * @param strTime 字符串日期
     * @param format  格式
     * @return
     */
    public static String formatTimeString(String strTime, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(strTime);
        } catch (Exception e) {
            EmpExecutionContext.error(e, " -->formatTimeString() 转化字符串时间格式异常!");
        }
        return strTime;
    }

    /**
     * 转化字符串时间格式
     *
     * @param time   Date 日期
     * @param format 格式
     * @return 如果出现异常， 返回默认的 yyyy-MM-dd HH:mm:ss
     */
    public static String formatTimeString(Date time, String format) {
        if (null == time) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(time);
        } catch (Exception e) {
            EmpExecutionContext.error(e, " -->formatTimeString() 转化字符串时间格式异常!");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }

    /**
     * 获取某一个月的周数
     *
     * @param date
     * @throws ParseException
     * @throws Exception
     */
    public static int getMonthWeek(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date s = sdf.parse(date);
        Calendar ca = Calendar.getInstance();
        ca.setTime(s);
        ca.setFirstDayOfWeek(Calendar.MONDAY);
        int weeks = ca.getActualMaximum(Calendar.WEEK_OF_MONTH);
        return weeks;
    }

}

