package com.montnets.emp.rms.meditor.biz;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.rms.meditor.vo.LfTempImportDetailsVo;
import com.montnets.emp.util.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

public interface IImportTempBiz {

    /**
     *
     * @param file 文件
     * @param corpCode 企业编码
     * @param tmName 富信主题
     * @param request
     * @return
     */
    JSONObject importTemp(File file,String corpCode, String tmName,HttpServletRequest request);

    /**
     * 详情excel表格导出
     * @param LfTempImportDetailsVos
     * @return
     */
    JSONObject exportTempExcel(List<LfTempImportDetailsVo> LfTempImportDetailsVos);

    /**
     * 根据查询条件导出excel
     * @param lfTempImportDetailsVo
     * @param pageInfo
     * @return
     */
    JSONObject exportTempExcelByCondition(LfTempImportDetailsVo lfTempImportDetailsVo, PageInfo pageInfo);

    /**
     * 导出样例excel
     * @return
     */
    JSONObject exportDefaultTempExcel();

    void handleLfMttask(LfMttask mttask, LfSysuser sysUser) throws Exception;

    void handleFeeByCorpAndSpuser(LfMttask mttask) throws EMPException;

    String sendMarathonRms(LfMttask mttask) throws Exception;

    int checkAduitTemplate(String batch);
}
