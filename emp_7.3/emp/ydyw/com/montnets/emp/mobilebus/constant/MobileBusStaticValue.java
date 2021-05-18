/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-7 上午11:57:23
 */
package com.montnets.emp.mobilebus.constant;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chentingsheng <cts314@163.com>
 * @description
 * @project p_ydyw
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015-1-7 上午11:57:23
 */

public class MobileBusStaticValue {
    //套餐类型下拉选项，由数据库配置，KEY为值，VALUE为名称，KEY为-1，表示控件名称
    public static final Map<String, String> taoCanType = new LinkedHashMap<String, String>();

    //指令下行短信回复内容
    public static final Map<String, String> reSmsMsgList = new LinkedHashMap<String, String>();

    public static Map<String, String> getTaoCanType() {
        return taoCanType;
    }

    public static Map<String, String> getReSmsMsgList() {
        return reSmsMsgList;
    }

    static {
        //套餐订购成功回复短信内容
        reSmsMsgList.put("100", "尊敬的用户，您已成功定制[%s]套餐业务，信息费为%s元/%s。");
        //套餐重复订购回复短信内容
        reSmsMsgList.put("101", "尊敬的用户，您已定制[%s]套餐业务，无需重新订购。");
        //套餐退订成功回复短信内容
        reSmsMsgList.put("200", "尊敬的用户，您已成功退订[%s]套餐业务，谢谢您的支持！");
        //退订套餐未订制回复短信内容
        reSmsMsgList.put("201", "尊敬的用户，您尚未订制[%s]套餐业务，谢谢您的支持！");
        //全局退订成功回复短信内容
        reSmsMsgList.put("300", "尊敬的用户，您已成功退订所有套餐业务，谢谢您的支持！");
        //全局退订套餐未订制回复短信内容
        reSmsMsgList.put("301", "尊敬的用户，您尚未订制套餐业务，谢谢您的支持！");
    }

    //指令分割符，上行指令带有账号，必须以此分割
    public static String orderDelimiter = "#";

    public static String getOrderDelimiter() {
        return orderDelimiter;
    }

    public static void setOrderDelimiter(String orderDelimiter) {
        MobileBusStaticValue.orderDelimiter = orderDelimiter;
    }

    //签约状态
    public static final Map<String, String> contractType = new LinkedHashMap<String, String>();
    public static final Map<String, String> contractType_zh_HK = new LinkedHashMap<String, String>();
    public static final Map<String, String> contractType_zh_TW = new LinkedHashMap<String, String>();
    //证件类型
    public static final Map<String, String> cardType = new LinkedHashMap<String, String>();
    public static final Map<String, String> cardType_zh_HK = new LinkedHashMap<String, String>();
    public static final Map<String, String> cardType_zh_TW = new LinkedHashMap<String, String>();
    //签约来源
    public static final Map<String, String> contractSource = new LinkedHashMap<String, String>();
    public static final Map<String, String> contractSource_zh_HK = new LinkedHashMap<String, String>();
    public static final Map<String, String> contractSource_zh_TW = new LinkedHashMap<String, String>();
    //取消签约方式
    public static final Map<String, String> cancelconType = new LinkedHashMap<String, String>();
    public static final Map<String, String> cancelconType_zh_HK = new LinkedHashMap<String, String>();
    public static final Map<String, String> cancelconType_zh_TW = new LinkedHashMap<String, String>();

    static {
        contractType.put("0", "已签约");
        contractType.put("1", "已取消签约");
        contractType.put("2", "已冻结");
        cardType.put("1", "身份证");
        cardType.put("2", "港澳通行证");
        cardType.put("3", "军人证");
        cardType.put("4", "中国护照");
        cardType.put("5", "武警证");
        cardType.put("6", "外国护照");
        cardType.put("-1", "其它");
        contractSource.put("0", "人工录入");
        contractSource.put("1", "主动上行");
        contractSource.put("2", "系统同步");
        cancelconType.put("-1", "-");
        cancelconType.put("0", "人工取消");
        cancelconType.put("1", "主动上行");
        /**
         * 增加国际化处理
         */
        contractType_zh_HK.put("0", "Signed");
        contractType_zh_HK.put("1", "Canceled");
        contractType_zh_HK.put("2", "Frozen");
        cardType_zh_HK.put("1", "ID Card");
        cardType_zh_HK.put("2", "HK/Macau Pass");
        cardType_zh_HK.put("3", "Military Card");
        cardType_zh_HK.put("4", "China passport");
        cardType_zh_HK.put("5", "Police certificate");
        cardType_zh_HK.put("6", "Foreign passport");
        cardType_zh_HK.put("-1", "Others");
        contractSource_zh_HK.put("0", "Manual entry");
        contractSource_zh_HK.put("1", "Active up");
        contractSource_zh_HK.put("2", "System synchronization");
        cancelconType_zh_HK.put("-1", "-");
        cancelconType_zh_HK.put("0", "Manual cancellation");
        cancelconType_zh_HK.put("1", "Active up");

        contractType_zh_TW.put("0", "已簽約");
        contractType_zh_TW.put("1", "已取消簽約");
        contractType_zh_TW.put("2", "已凍結");
        cardType_zh_TW.put("1", "身份證");
        cardType_zh_TW.put("2", "港澳通行證");
        cardType_zh_TW.put("3", "軍人證");
        cardType_zh_TW.put("4", "中國護照");
        cardType_zh_TW.put("5", "武警證");
        cardType_zh_TW.put("6", "外國護照");
        cardType_zh_TW.put("-1", "其它");
        contractSource_zh_TW.put("0", "人工錄入");
        contractSource_zh_TW.put("1", "主動上行");
        contractSource_zh_TW.put("2", "系統同步");
        cancelconType_zh_TW.put("-1", "-");
        cancelconType_zh_TW.put("0", "人工取消");
        cancelconType_zh_TW.put("1", "主動上行");
    }

    public static final long rptTimeOut = 72L;
}
