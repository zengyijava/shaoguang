package com.montnets.shaoguanga.constant;

/**
 * @param
 * @ClassName SgConstant
 * @Author zengyi
 * @Description
 * @Date 2021/4/19 9:16
 **/
public class SgConstant {

    /** 下行接口响应码*/
    /** 数据验证通过。*/
    public static final String SUCCESS = "success";
    /** mac 校验不通过。*/
    public static final String ILLEGAL_MAC = "IllegalMac";
    /** 无效的签名编码。*/
    public static final String ILLEGAL_SIGN_ID = "IllegalSignId";
    /** 非法消息，请求数据解析失败。*/
    public static final String INVALID_MESSAGE = "InvalidMessage";
    /** 非法用户名/密码。*/
    public static final String INVALID_USR_OR_PWD = "InvalidUsrOrPwd";
    /** 手机号数量超限（>5000），应≤5000）。*/
    public static final String TOO_MANY_MOBILES = "TooManyMobiles";
    /** 缺少发送号码*/
    public static final String NO_MOBILES = "NoMobiles";
    /** 缺少发送内容*/
    public static final String NO_CONTENT = "NoContent";
    /** 非法的 IP 地址*/
    public static final String INVALID_IP = "InvalidIP";
    /** 相同的内容与手机号，属于重复发送*/
    public static final String SAME_NUMBER_AND_CONTENT = "SameNumberAndContent";
    /** 未在平台接入系统注册*/
    public static final String NO_REGISTER = "NoRegister";

    /** 定时获取状态报告相关常量*/
    /** 配置文件中分隔符*/
    public static final String REGEX = "\\$#MW#\\$";
}
