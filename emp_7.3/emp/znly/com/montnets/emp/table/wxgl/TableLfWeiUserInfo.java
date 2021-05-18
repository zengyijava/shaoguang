package com.montnets.emp.table.wxgl;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableOtWeiUserinfo 用户微信资料表
 * 
 * @project p_wei
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWeiUserInfo
{
    // 表名
    public static final String              TABLE_NAME     = "LF_WEI_USERINFO";

    // 微信ID
    public static final String        WC_ID          = "WC_ID";

    // 微信号的唯一标识
    public static final String        FAKE_ID        = "FAKE_ID";

    // 用户组ID
    public static final String        G_ID           = "G_ID";

    // 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息
    public static final String        SUBSCRIBE      = "SUBSCRIBE";

    // 用户的昵称
    public static final String        NICK_NAME      = "NICK_NAME";

    // 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    public static final String        SEX            = "SEX";

    // 用户所在城市
    public static final String        CITY           = "CITY";

    // 用户所在省份
    public static final String        PROVINCE       = "PROVINCE";

    // 用户所在国家
    public static final String        COUNTRY        = "COUNTRY";

    // 用户的语言，简体中文为zh_CN
    public static final String        LANGUAGE       = "LANGUAGE";

    // 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
    public static final String        HEAD_IMG_URL   = "HEAD_IMG_URL";

    // 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
    public static final String        SUBSCRIBE_TIME = "SUBSCRIBE_TIME";

    // 姓名（用户，员工，客户其之一的姓名）
    public static final String        UNAME          = "UNAME";

    // 手机号码
    public static final String        PHONE          = "PHONE";

    // 其他
    public static final String        DESCR          = "DESCR";

    // 提交基本信息时间
    public static final String        VERIFYTIME     = "VERIFYTIME";

    // 用户ID
    public static final String        U_ID           = "U_ID";

    // 企业编号
    public static final String        CORP_CODE      = "CORP_CODE";

    // 创建时间
    public static final String        CREATETIME     = "CREATETIME";

    // 最后修改时间
    public static final String        MODIFYTIME     = "MODIFYTIME";

    // 序列
    public static final String        SEQUENCE       = "513";

    // 微信用户openid
    public static final String        OPEN_ID        = "OPEN_ID";

    // 微信公众帐号ID
    public static final String        A_ID           = "A_ID";

    // 用户头像图片本地存放地址
    public static final String        LOCAL_IMG_URL  = "LOCAL_IMG_URL";

    // 映射集合
    protected static final Map<String, String> columns        = new HashMap<String, String>();

    static
    {
        columns.put("LfWeiUserInfo", TABLE_NAME);
        columns.put("tableId", WC_ID);
        columns.put("sequence", SEQUENCE);
        columns.put("wcId", WC_ID);
        columns.put("fakeId", FAKE_ID);
        columns.put("GId", G_ID);
        columns.put("subscribe", SUBSCRIBE);
        columns.put("nickName", NICK_NAME);
        columns.put("sex", SEX);
        columns.put("city", CITY);
        columns.put("province", PROVINCE);
        columns.put("country", COUNTRY);
        columns.put("language", LANGUAGE);
        columns.put("headImgUrl", HEAD_IMG_URL);
        columns.put("subscribeTime", SUBSCRIBE_TIME);
        columns.put("uname", UNAME);
        columns.put("phone", PHONE);
        columns.put("descr", DESCR);
        columns.put("verifytime", VERIFYTIME);
        columns.put("UId", U_ID);
        columns.put("corpCode", CORP_CODE);
        columns.put("createtime", CREATETIME);
        columns.put("modifytime", MODIFYTIME);
        columns.put("openId", OPEN_ID);
        columns.put("AId", A_ID);
        columns.put("localImgUrl", LOCAL_IMG_URL);
    };

    /**
     * 返回实体类字段与数据库字段实体类映射的map集合
     * 
     * @return
     */
    public static Map<String, String> getORM()
    {
        return columns;
    }
}
