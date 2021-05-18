package com.montnets.emp.common.security.util;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.security.constant.PatternConst;
import com.montnets.emp.common.security.constant.SecurityConst;
import com.montnets.emp.common.security.entity.FilterValue;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Title: PatternUtils
 * @project emp_7.3
 * @author: liangHuaGeng
 * @Date: 2018/11/29 10:35
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @Description: PatternUtils 工具类，利用其预编译功能，提高性能；
 */
@SuppressWarnings("All")
public class PatternUtils {

    private static List<String[]> JAVA_SCRIPT_PATTERN;
    private static final Pattern SQL_PATTERN;
    private static final String[] NO_FILTER;


    static {
        getProperty("JavScriptPattern.properties");
        NO_FILTER = new String[]{PatternConst.AND, PatternConst.INSERT, PatternConst.DELETE,
                PatternConst.UPDATE, PatternConst.INTO, PatternConst.DROP};
        SQL_PATTERN = complexCompile(PatternConst.SQL_REG);
    }

    /**
     * 获取配置文件信息
     *
     * @param fileName 配置文件
     * @return
     */
    public static void getProperty(String fileName) {
        JAVA_SCRIPT_PATTERN = new ArrayList<String[]>();
        // 将属性文件流装载到Properties对象中
        Properties prop = PropertiesUtils.getProperties(fileName);
        //JavaScript脚本正则表达式配置文件
        for (Entry<Object, Object> entry : prop.entrySet()) {
            //SCRIPT_PATTERN_ALERT=complexCompile("alert\\((.*?)\\)")
            String value = (String) entry.getValue();
            String compileType = null == value ? SecurityConst.EMPTY : value.subSequence(0, value.indexOf("(")).toString();
            String patternStr = null == value ? SecurityConst.EMPTY : value.subSequence(value.indexOf("\"") + 1, value.length() - 2).toString();
            String[] str = new String[2];
            str[0] = compileType;
            str[1] = patternStr;
            JAVA_SCRIPT_PATTERN.add(str);
        }
    }

    public static Pattern compile(String regex) {
        return Pattern.compile(regex);
    }

    public static Pattern simpleCompile(String regex) {
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    public static Pattern complexCompile(String regex) {
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    }

    /**
     * js 脚本过滤
     *
     * @param val
     * @return
     */
    public static String scriptFilter(FilterValue val) {
        if (null == val) {
            return null;
        }
        for (String[] s : JAVA_SCRIPT_PATTERN) {
            String compileType = s[0] == null ? SecurityConst.EMPTY : s[0];
            String patternStr = s[1] == null ? SecurityConst.EMPTY : s[1];
            Pattern pattern;
            if (PatternConst.SIMPLE_COMPILE.equals(compileType)) {
                pattern = simpleCompile(patternStr);
            } else if (PatternConst.COMPLEX_COMPILE.equals(compileType)) {
                pattern = complexCompile(patternStr);
            } else {
                pattern = compile(patternStr);
            }
            replaceEmpty(pattern, val);
        }
        return val.getValue();
    }

    /**
     * sql 关键字过滤
     *
     * @param val
     * @return
     */
    public static String sqlFilter(FilterValue val) {
        if (null == val) {
            return null;
        }
        replaceEmpty(SQL_PATTERN, val);
        return val.getValue();
    }

    /**
     * 将匹配的内容替换为""
     *
     * @param pattern
     * @param val
     * @return
     */
    public static void replaceEmpty(Pattern pattern, FilterValue val) {
        if (null != pattern && null != val) {
            Matcher matcher = pattern.matcher(val.getValue());
            if (matcher.find()) {
                boolean isEdit = true;
                for (String tmp : NO_FILTER) {
                    if (tmp.equals(val.getValue().trim().toLowerCase())) {
                        isEdit = false;
                        break;
                    }
                }
                if (isEdit) {
                    EmpExecutionContext.info("EMP安全检测发现有危险内容, Content = " + val.getValue()
                            + ", 已经在 " + Calendar.getInstance(Locale.CHINA).getTime() + " 对其进行处理!");
                    val.setValue(matcher.replaceAll(SecurityConst.EMPTY));
                }
            }
        }
    }

}