package com.montnets.emp.rms.meditor.biz.imp;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.meditor.biz.IImportTempDetailsBiz;
import com.montnets.emp.rms.meditor.dao.ImportTempDetailsDao;
import com.montnets.emp.rms.meditor.dao.imp.ImportTempDetailsDaoImp;
import com.montnets.emp.rms.meditor.dto.LfTempImportBatchDto;
import com.montnets.emp.rms.meditor.vo.LfTempImportDetailsVo;
import com.montnets.emp.rms.vo.LfTempImportBatchVo;
import com.montnets.emp.util.PageInfo;

import java.util.List;

public class ImportTempDetailsBiz extends BaseBiz implements IImportTempDetailsBiz {
    ImportTempDetailsDao importTempDetailsDao = new ImportTempDetailsDaoImp();

    public List<LfTempImportBatchVo> findLfTempImportBatch(LfTempImportBatchDto lfTempImportBatchDto, PageInfo pageInfo) {
        List<LfTempImportBatchVo> lfTempImportBatches = null;
        try{
            lfTempImportBatches = importTempDetailsDao.findLfTempImportBatch(lfTempImportBatchDto,pageInfo);
        }catch (Exception e){
            EmpExecutionContext.error(e, "企业富信-数据查询-批量导入详情-批次查询异常");
        }
        return lfTempImportBatches;
    }

    public List<LfTempImportDetailsVo> findLfTempImportDetails(LfTempImportDetailsVo lfTempImportDetailsVo, PageInfo pageInfo) {
        List<LfTempImportDetailsVo> lfTempImportDetailsVos = null;
        try {
            lfTempImportDetailsVos = importTempDetailsDao.findLfTempImportDetails(lfTempImportDetailsVo, pageInfo);
        }catch (Exception e){
            EmpExecutionContext.error(e, "企业富信-数据查询-批量导入详情-导入详情查看查询异常");
        }
        return lfTempImportDetailsVos;
    }
}
