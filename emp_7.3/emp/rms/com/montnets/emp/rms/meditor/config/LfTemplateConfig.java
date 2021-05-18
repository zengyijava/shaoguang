package com.montnets.emp.rms.meditor.config;

public class LfTemplateConfig {

    //模板子表数据权重
    public static final Integer SUB_TEMP_PRIORITY = 0;

    //公共场景
    public static final Integer IS_PUBLIC = 1;
    //我的场景
    public static final Integer IS_NOT_PUBLIC = 0;

    //是快捷场景
    public static final Integer IS_SHORT_TEMP = 1;
    //不是快捷场景
    public static final Integer IS_NOT_SHORT_TEMP = 0;

    // 0只给一个主数据
    public static final Integer PREVIEW_TYPE_ONE = 0;
    // 1给所有数据
    public static final Integer PREVIEW_TYPE_ALL = 1;

    //审核通过
    public static final Integer AUDITSTATUS_APPROVE = 1;

    //未审批
    public static final Integer AUDITSTATUS_NO_APPROVEAL = -1;

    //审批未通过
    public static final Integer AUDITSTATUS_APPROVEAL_FAIL = 2;

    //启用
    public static final Integer START_USEING=1;

    //禁用
    public static final Integer FORBIDDEN=0;
}
