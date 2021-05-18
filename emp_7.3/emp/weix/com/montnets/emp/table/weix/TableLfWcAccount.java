package com.montnets.emp.table.weix;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信公众帐号（LF_WC_ACCOUNT）
 *
 * @author fangyt <foyoto@gmail.com>
 * @project p_weix
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWcAccount {
    /**
     * 公众帐号程序自增编号
     */
    public static final String A_ID = "A_ID";
    /**
     * 腾讯给出的微信公众号的唯一标识
     */
    public static final String FAKE_ID = "FAKE_ID";
    /**
     * 公众帐号的名称
     */
    public static final String NAME = "NAME";
    /**
     * 公众帐号
     */
    public static final String CODE = "CODE";
    /**
     * 微信公众帐号openid
     */
    public static final String OPEN_ID = "OPEN_ID";
    /**
     * 微信图像的url地址
     */
    public static final String IMG = "IMG";
    /**
     * 公众帐号类型
     */
    public static final String TYPE = "TYPE";
    /**
     * 是否认证
     */
    public static final String IS_APPROVE = "IS_APPROVE";
    /**
     * 二维码
     */
    public static final String QRCODE = "QRCODE";
    /**
     * 功能介绍
     */
    public static final String INFO = "INFO";
    /**
     * API接入地址
     */
    public static final String URL = "URL";
    /**
     * 接入TOKEN
     */
    public static final String TOKEN = "TOKEN";
    /**
     * 接入时间
     */
    public static final String BINDTIME = "BINDTIME";
    /**
     * 企业编号
     */
    public static final String CORP_CODE = "CORP_CODE";
    /**
     * 创建时间
     */
    public static final String CREATETIME = "CREATETIME";
    /**
     * 修改时间
     */
    public static final String MODIFYTIME = "MODIFYTIME";
    public static final String GRANTTYPE = "GRANT_TYPE";
    /**
     * 第三方用户唯一凭证
     */
    public static final String APPID = "APPID";
    /**
     * 第三方用户唯一凭证密钥，既appsecret
     */
    public static final String SECRET = "SECRET";
    /**
     * 口令凭证
     */
    public static final String ACCESSTOKEN = "ACCESS_TOKEN";
    /**
     * 获取token的时间
     */
    public static final String ACCESSTIME = "ACCESS_TIME";
    /**
     * 菜单发布时间
     */
    public static final String RELEASETIME = "RELEASE_TIME";
    /**
     * 序列
     */
    public static final String SEQUENCE = "509";
    /**
     * 表名：微信公众帐号
     */
    public static final String TABLE_NAME = "LF_WC_ACCOUNT";
    /**
     * 映射集合
     */
    protected static final Map<String, String> columns = new HashMap<String, String>();

    static {
        columns.put("LfWcAccount", TABLE_NAME);
        columns.put("tableId", A_ID);
        columns.put("sequence", SEQUENCE);
        columns.put("AId", A_ID);
        columns.put("fakeId", FAKE_ID);
        columns.put("name", NAME);
        columns.put("code", CODE);
        columns.put("openId", OPEN_ID);
        columns.put("img", IMG);
        columns.put("type", TYPE);
        columns.put("isApprove", IS_APPROVE);
        columns.put("qrcode", QRCODE);
        columns.put("info", INFO);
        columns.put("url", URL);
        columns.put("token", TOKEN);
        columns.put("bindTime", BINDTIME);
        columns.put("corpCode", CORP_CODE);
        columns.put("createTime", CREATETIME);
        columns.put("modifyTime", MODIFYTIME);
        columns.put("grantType", GRANTTYPE);
        columns.put("appId", APPID);
        columns.put("secret", SECRET);
        columns.put("accessToken", ACCESSTOKEN);
        columns.put("accessTime", ACCESSTIME);
        columns.put("releaseTime", RELEASETIME);
    }

    ;

    /**
     * 返回实体类字段与数据库字段实体类映射的map集合
     *
     * @return
     */
    public static Map<String, String> getORM() {
        return columns;
    }
}
