package com.montnets.emp.rms.meditor.dao;

import com.montnets.emp.rms.meditor.entity.LfTempImportBatch;

public interface ImportTempDao {


    /**
     * 根据企业编码查询最后的导入批次
     * @param corpCode
     * @return
     */
    LfTempImportBatch findLastBatchByCorpCode(String corpCode);

    int checkAduitTemplate(String batch);
}
