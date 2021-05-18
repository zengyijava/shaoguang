package com.montnets.emp.rms.meditor.dao.imp;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.rms.meditor.base.BaseConfig;
import com.montnets.emp.rms.meditor.dao.ImportTempDetailsDao;
import com.montnets.emp.rms.meditor.dto.LfTempImportBatchDto;
import com.montnets.emp.rms.meditor.vo.LfTempImportDetailsVo;
import com.montnets.emp.rms.vo.LfTempImportBatchVo;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class ImportTempDetailsDaoImp extends SuperDAO implements ImportTempDetailsDao {
    IGenericDAO genericDAO = new DataAccessDriver().getGenericDAO();
    IEmpDAO empDAO = new DataAccessDriver().getEmpDAO();

    public List<LfTempImportBatchVo> findLfTempImportBatch(LfTempImportBatchDto lfTempImportBatchDto, PageInfo pageInfo) {
        StringBuffer sql = new StringBuffer();
        StringBuffer conditionSql = new StringBuffer();
        String countSql = "SELECT count(*) totalcount  FROM lf_temp_import_batch lftemp left join lf_mttask lfmttask on lftemp.batch = lfmttask.taskid";
        sql.append("SELECT lftemp.*,lfmttask.SENDSTATE FROM lf_temp_import_batch lftemp left join lf_mttask lfmttask on lftemp.batch = lfmttask.taskid");
        if (null != lfTempImportBatchDto){
            if (null != lfTempImportBatchDto.getBatch()){
                conditionSql.append(" AND lftemp.BATCH=").append(lfTempImportBatchDto.getBatch());
            }
            if (StringUtils.isNotBlank(lfTempImportBatchDto.getCorpCode())){
                conditionSql.append(" AND lftemp.CORP_CODE='").append(lfTempImportBatchDto.getCorpCode()).append("'");
            }
            if (StringUtils.isNotBlank(lfTempImportBatchDto.getCorpName())){
                conditionSql.append(" AND lftemp.CORP_NAME like '%").append(lfTempImportBatchDto.getCorpName()).append("%'");
            }
            if (null != lfTempImportBatchDto.getProcessStatus()){
                conditionSql.append(" AND lftemp.PROCESS_STATUS=").append(lfTempImportBatchDto.getProcessStatus());
            }

            //创建时间区间
            if (StringUtils.isNotEmpty(lfTempImportBatchDto.getAddtimeStart()) || StringUtils.isNotEmpty(lfTempImportBatchDto.getAddtimeEnd())) {
                //若起始时间为空终止时间不为空，则起始时间取最早时间
                if (StringUtils.isEmpty(lfTempImportBatchDto.getAddtimeStart())) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseConfig.DATEFORMAT);
                    String str = simpleDateFormat.format(new Date(0));
                    lfTempImportBatchDto.setAddtimeStart(genericDAO.getTimeCondition(str));
                } else {
                    lfTempImportBatchDto.setAddtimeStart(genericDAO.getTimeCondition(lfTempImportBatchDto.getAddtimeStart()));
                }
                //若起始时间不为空终止时间为空，则终止时间取当前时间
                if (StringUtils.isEmpty(lfTempImportBatchDto.getAddtimeEnd())) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(BaseConfig.DATEFORMAT);
                    String str = simpleDateFormat.format(new Date());
                    lfTempImportBatchDto.setAddtimeEnd(genericDAO.getTimeCondition(str));
                } else {
                    lfTempImportBatchDto.setAddtimeEnd(genericDAO.getTimeCondition(lfTempImportBatchDto.getAddtimeEnd()));
                }
                conditionSql.append(" AND ADDTIME ").append(" between ").append(lfTempImportBatchDto.getAddtimeStart()).append(" AND ").append(lfTempImportBatchDto.getAddtimeEnd());
            }
        }
        conditionSql.append(" ORDER BY id DESC");

        List<LfTempImportBatchVo> lfTempImportBatches = null;
        if (null == pageInfo){
            pageInfo = new PageInfo();
            pageInfo.setPageSize(Integer.MAX_VALUE);
        }
        String endSql= String.valueOf(sql.append(getConditionSql(String.valueOf(conditionSql))));
        countSql = countSql + getConditionSql(String.valueOf(conditionSql));
        try {
         lfTempImportBatches =  genericDAO.findPageVoListBySQL(LfTempImportBatchVo.class,endSql , countSql, pageInfo, StaticValue.EMP_POOLNAME);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询模板导入批次表异常");
        }
        return lfTempImportBatches;
    }

    @Override
    public List<LfTempImportDetailsVo> findLfTempImportDetails(LfTempImportDetailsVo lfTempImportDetailsVo, PageInfo pageInfo) {

        StringBuffer tableSql = new StringBuffer();
        String countSql ="SELECT count(*) totalcount  FROM lf_temp_import_details de LEFT JOIN lf_template temp ON de.SPTEMPLID = temp.SP_TEMPLID ";
        StringBuffer conditionSql = new StringBuffer();

        tableSql.append("SELECT ")
                .append("de.ID,")
                .append("de.BATCH,")
                .append("de.TM_NAME,")
                .append("de.`NAME`,")
                .append("de.PHONE_NUM,")
                .append("de.SCORE,")
                .append("de.SPTEMPLID,")
                .append("de.SEND_STATUS,")
                .append("de.IMPORT_STATUS,")
                .append("de.CAUSE,")
                .append("de.IMAGE_SRC,")
                .append("de.VIDEO_SRC,")
                .append("de.ADDTIME,")
                .append("temp.AUDITSTATUS, ")
                .append("de.CORP_CODE")
                .append(" FROM ")
                .append("lf_temp_import_details de ")
                .append(" LEFT JOIN lf_template temp ON de.SPTEMPLID = temp.SP_TEMPLID ");


        if (null !=lfTempImportDetailsVo){
            if (null != lfTempImportDetailsVo.getBatch()){
                conditionSql.append(" AND de.BATCH=").append(lfTempImportDetailsVo.getBatch());
            }
            if (StringUtils.isNotBlank(lfTempImportDetailsVo.getTmName())){
                conditionSql.append(" AND de.TM_NAME='").append(lfTempImportDetailsVo.getTmName()).append("'");
            }
            if (StringUtils.isNotBlank(lfTempImportDetailsVo.getName())){
                conditionSql.append(" AND de.NAME like '%").append(lfTempImportDetailsVo.getName()).append("%'");
            }
            if (StringUtils.isNotBlank(lfTempImportDetailsVo.getPhoneNum())){
                conditionSql.append(" AND de.PHONE_NUM like '%").append(lfTempImportDetailsVo.getPhoneNum()).append("%'");
            }
            if (StringUtils.isNotBlank(lfTempImportDetailsVo.getScore())){
                conditionSql.append(" AND de.SCORE='").append(lfTempImportDetailsVo.getScore()).append("'");
            }
            if (null != lfTempImportDetailsVo.getSptemplid()){
                conditionSql.append(" AND de.SPTEMPLID='").append(lfTempImportDetailsVo.getSptemplid()).append("'");
            }

            if (null != lfTempImportDetailsVo.getImportStatus()){
                conditionSql.append(" AND de.IMPORT_STATUS=").append(lfTempImportDetailsVo.getImportStatus());
            }

            if (null != lfTempImportDetailsVo.getSendStatus()){
                conditionSql.append(" AND de.SEND_STATUS=").append(lfTempImportDetailsVo.getSendStatus());
            }
            if (null != lfTempImportDetailsVo.getAuditstatus()){
                conditionSql.append(" AND temp.AUDITSTATUS=").append(lfTempImportDetailsVo.getAuditstatus());
            }
            if (null != lfTempImportDetailsVo.getCorpCode()){
                conditionSql.append(" AND de.CORP_CODE=").append(lfTempImportDetailsVo.getCorpCode());
            }
        }

        List<LfTempImportDetailsVo> lfTempImportDetailsVos = null;
        if (null == pageInfo){
            pageInfo = new PageInfo();
            pageInfo.setPageSize(Integer.MAX_VALUE);
        }
        String sql = String.valueOf(tableSql.append(getConditionSql(String.valueOf(conditionSql))));
        countSql = countSql + getConditionSql(String.valueOf(conditionSql));
        try {
            lfTempImportDetailsVos =  genericDAO.findPageVoListBySQL(LfTempImportDetailsVo.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询导入详情表异常");
        }
        return lfTempImportDetailsVos;
    }
    /**
     * 处理SQL条件,不允许使用1 =1方式
     * @param conSql 条件
     * @return 处理后的条件
     */
    public static String getConditionSql(String conSql) {
        String conditionSql = "";
        try {
            //存在查询条件
            if(conSql != null && conSql.length() > 0)
            {
                //将条件字符串首个and替换为where,不允许1 =1方式
                conditionSql = conSql.replaceFirst("^(\\s*)(?i)and", "$1where");
            }
            return conditionSql;
        } catch (Exception e) {
            EmpExecutionContext.error("处理SQL条件异常，conSql:" + conSql);
            return null;
        }
    }
}
