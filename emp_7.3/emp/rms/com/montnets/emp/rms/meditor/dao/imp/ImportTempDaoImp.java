package com.montnets.emp.rms.meditor.dao.imp;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.rms.meditor.dao.ImportTempDao;
import com.montnets.emp.rms.meditor.entity.LfTempImportBatch;
import com.montnets.emp.util.PageInfo;

import java.util.List;

public class ImportTempDaoImp extends SuperDAO implements ImportTempDao {
    IGenericDAO genericDAO = new DataAccessDriver().getGenericDAO();
    IEmpDAO empDAO = new DataAccessDriver().getEmpDAO();

    public LfTempImportBatch findLastBatchByCorpCode(String corpCode) {
        String sql = "SELECT * from lf_temp_import_batch where ID = (SELECT MAX(id) FROM lf_temp_import_batch WHERE CORP_CODE = '" + corpCode + "')";

        List<LfTempImportBatch> lfTempImportBatches = null;
        try {
            lfTempImportBatches = genericDAO.findPageEntityListBySQLNoCount(LfTempImportBatch.class, sql, null, new PageInfo(), StaticValue.EMP_POOLNAME);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "批次表查询数据异常");
        }
        if (null == lfTempImportBatches || lfTempImportBatches.size()<1){
            EmpExecutionContext.info("未查询到批次数据");
            return null;
        }else {
            if (lfTempImportBatches.size() > 1) {
                EmpExecutionContext.error("批次表查询数据异常，按企业编码查询最后批次出现一条以上数据");
            }
        }
        return lfTempImportBatches.get(0);
    }

    public int checkAduitTemplate(String batch) {
        //直接拼接sql
        String sql = "SELECT COUNT(*) totalcount FROM LF_TEMPLATE WHERE SP_TEMPLID IN (SELECT SPTEMPLID FROM LF_TEMP_IMPORT_DETAILS WHERE BATCH = '"+ batch +"' and SPTEMPLID <> ' ') AND AUDITSTATUS = 1";
        return findCountBySQL(sql);
    }
}
