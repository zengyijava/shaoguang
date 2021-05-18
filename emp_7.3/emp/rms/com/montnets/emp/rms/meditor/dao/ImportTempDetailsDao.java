package com.montnets.emp.rms.meditor.dao;

import com.montnets.emp.rms.meditor.dto.LfTempImportBatchDto;
import com.montnets.emp.rms.meditor.vo.LfTempImportDetailsVo;
import com.montnets.emp.rms.vo.LfTempImportBatchVo;
import com.montnets.emp.util.PageInfo;

import java.util.List;

public interface ImportTempDetailsDao {

    /**
     * 查询导入批次
     * @param lfTempImportBatchDto
     * @return
     */
    List<LfTempImportBatchVo> findLfTempImportBatch(LfTempImportBatchDto lfTempImportBatchDto, PageInfo pageInfo);

    /**
     * 查询批次详情
     * @param lfTempImportDetailsVo
     * @param pageInfo
     * @return
     */
    List<LfTempImportDetailsVo> findLfTempImportDetails(LfTempImportDetailsVo lfTempImportDetailsVo, PageInfo pageInfo);

}
