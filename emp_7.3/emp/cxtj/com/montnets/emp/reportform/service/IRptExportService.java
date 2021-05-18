package com.montnets.emp.reportform.service;

import com.montnets.emp.reportform.bean.ReportVo;

import java.util.List;

/**
 * 查询统计报表导出接口
 * @author chenguang
 * @date 2018-12-21 10:30:00
 */
public interface IRptExportService {
    /**
     * 导出报表主入口
     * @param reportVos list集合
     * @param module 当前模块
     * @return 导出结果 success 导出成功  fail 导出失败 error 导出错误 oversize 数据量过大
     */
    String createRptExcelByModule(List<ReportVo> reportVos, String module);
}
