package com.montnets.emp.reportform.bean;

import com.montnets.emp.util.PageInfo;

/**
 * 用来装前端传递过来的参数的 Bean
 * @author lianghuageng
 * @date 2018/12/11 08:52
 */
public class Request {

    /**
     * 模块名称
     */
    private String module;

    /**
     * report 对象
     */
    private ReportVo report;

    /**
     * 分页信息
     */
    private PageInfo page;

    /**
     * 是不是详情页面
     */
    private boolean details;


    public boolean isDetails() {
        return details;
    }

    public void setDetails(boolean details) {
        this.details = details;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public ReportVo getReport() {
        return report;
    }

    public void setReport(ReportVo report) {
        this.report = report;
    }

    public PageInfo getPage() {
        return page;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }
}
