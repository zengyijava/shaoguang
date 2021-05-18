package com.montnets.emp.table.weix;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信用户基本信息表（LF_WC_USERINFO）
 *
 * @author fangyt <foyoto@gmail.com>
 * @project p_weix
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfWcUserInfo {
    /**
     * 微信ID
     */
    public static final String WC_ID = "WC_ID";
    /**
     * 微信号的唯一标识
     */
    public static final String FAKE_ID = "FAKE_ID";
    /**
     * 微信名称
     */
    public static final String NAME = "NAME";
    /**
     * 微信名
     */
    public static final String CODE = "CODE";
    /**
     * 微信图像的url地址
     */
    public static final String IMG = "IMG";
    /**
     * 类型
     */
    public static final String TYPE = "TYPE";
    /**
     * 个人签名
     */
    public static final String SIGNATURE = "SIGNATURE";
    /**
     * 企业编号
     */
    public static final String CORP_CODE = "CORP_CODE";
    /**
     * 创建时间
     */
    public static final String CREATETIME = "CREATETIME";
    /**
     * 最后修改时间
     */
    public static final String MODIFYTIME = "MODIFYTIME";
    /**
     * 手机号码
     */
    public static final String PHONE = "PHONE";
    /**
     * emp的姓名（用户，员工，客户其之一的姓名）
     */
    public static final String UNAME = "UNAME";
    /**
     * 其他
     */
    public static final String DESCR = "DESCR";
    /**
     * 序列
     */
    public static final String SEQUENCE = "513";
    /**
     * 提交基本信息时间
     */
    public static final String VERIFYTIME = "VERIFYTIME";
    /**
     * EMP用户ID
     */
    public static final String U_ID = "U_ID";
    /**
     * 表名：用户微信基本信息表
     */
    public static final String TABLE_NAME = "LF_WC_USERINFO";
    /**
     * 映射集合
     */
    protected static final Map<String , String> columns = new HashMap<String, String>();

    static {
        columns.put("LfWcUserInfo", TABLE_NAME);
        columns.put("tableId", WC_ID);
        columns.put("sequence", SEQUENCE);
        columns.put("wcId", WC_ID);
        columns.put("fakeId", FAKE_ID);
        columns.put("name", NAME);
        columns.put("code", CODE);
        columns.put("img", IMG);
        columns.put("type", TYPE);
        columns.put("signature", SIGNATURE);
        columns.put("corpCode", CORP_CODE);
        columns.put("createTime", CREATETIME);
        columns.put("modifyTime", MODIFYTIME);
        columns.put("phone", PHONE);
        columns.put("uname", UNAME);
        columns.put("descr", DESCR);
        columns.put("verifyTime", VERIFYTIME);
        columns.put("UId", U_ID);

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
