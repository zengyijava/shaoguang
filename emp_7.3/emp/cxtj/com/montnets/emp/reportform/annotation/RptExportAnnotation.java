package com.montnets.emp.reportform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解 用于查询统计报表导出
 * @author Chenguang
 * @date 2018-12-20 22:36:25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface RptExportAnnotation {
    /**
     * 该字段用于哪些模块
     * @return 模块
     */
    String[] module();
    /**
     * 表头顺序 一定要与module一一对应
     * 0基
     * @return 数字
     */
    int[] index();
    /**
     * 表头名字
     * @return 表头名字
     */
    String thName();
    /**
     * 表头名字（繁体）
     * @return 表头名字
     */
    String thName_TW() default "";
    /**
     * 表头名字（英文）
     * @return 表头名字
     */
    String thName_HK() default "";
}
