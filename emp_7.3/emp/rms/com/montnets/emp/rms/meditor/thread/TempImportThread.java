package com.montnets.emp.rms.meditor.thread;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.rms.meditor.entity.LfTempImportBatch;
import com.montnets.emp.rms.meditor.entity.LfTempImportDetails;
import com.montnets.emp.rms.meditor.tools.HmParseRmsTool;
import com.montnets.emp.util.ExcelReadUtil;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TempImportThread implements Runnable {

    private Workbook wb;
    private LfCorp lfCorp;
    private Long batch;
    private String tmName;
    IEmpDAO empDAO = new DataAccessDriver().getEmpDAO();
    PhoneUtil phoneUtil = new PhoneUtil();

    public TempImportThread(Workbook wb, LfCorp lfCorp, String tmName, Long batch) {

        this.wb = wb;
        this.lfCorp = lfCorp;
        this.batch = batch;
        this.tmName = tmName;
    }

    @Override
    public void run() {

        //1. excel读取
        //读取第一个sheet
        Sheet sheet = wb.getSheetAt(0);
        List<LfTempImportDetails> lfTempImportDetailsList = new ArrayList<LfTempImportDetails>();
        //行计数
        int rowCount = 0;
        //遍历行
        for (Row row : sheet) {
            //跳过第一行标题栏
            if (rowCount < 1) {
                rowCount++;
                continue;
            }
            //获取总列数(空格的不计算)
            int totalCellCounts = row.getPhysicalNumberOfCells();

            //创建表格实体对象，接收表格读取的数据
            LfTempImportDetails lfTempImportDetails = new LfTempImportDetails();
            lfTempImportDetails.setBatch(batch);
            lfTempImportDetails.setImportStatus(1);
            lfTempImportDetails.setTmName(tmName);
            lfTempImportDetails.setName("");
            lfTempImportDetails.setScore("");
            lfTempImportDetails.setSendStatus(0);
            lfTempImportDetails.setCause("");
            lfTempImportDetails.setImageSrc("");
            lfTempImportDetails.setPhoneNum("");
            lfTempImportDetails.setVideoSrc("");
            lfTempImportDetails.setCorpCode(lfCorp.getCorpCode());
            //遍历列
            for (int cellNum = 0; cellNum < totalCellCounts; cellNum++) {
                Cell cell = row.getCell(cellNum);
                //第一、三、五列序号跳过
                if (cellNum == 0 || cellNum == 3 || cellNum == 5) {
                    continue;
                }
                String readRes = ExcelReadUtil.getCellValue(cell);
                switch (cellNum) {
                    case 0:
                        break;
                    case 1:
                        try {
                            if (StringUtils.isNotBlank(readRes)) {
                                lfTempImportDetails.setPhoneNum(readRes);
                            }
                            if (0 == phoneUtil.checkMobile(readRes, null)) {
                                lfTempImportDetails.setImportStatus(0);
                                lfTempImportDetails.setCause("手机号不合法!");
                            }
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, "检查手机号码合法性失败!phone:" + readRes);
                        }
                        break;
                    case 2:
                        if (!(readRes.contains(".jpg"))){
                            readRes = readRes + ".jpg";
                        }
                        lfTempImportDetails.setImageSrc(readRes);
                        break;
                    case 3:
                        break;
                    case 4:
                        if (!(readRes.contains(".mp4"))){
                            readRes = readRes + ".mp4";
                        }
                        lfTempImportDetails.setVideoSrc(readRes);
                        break;
                    case 5:
                        break;
                }
            }
            lfTempImportDetailsList.add(lfTempImportDetails);
        }


        //2. 遍历提交审核
        Long successCount = 0L;//成功数
        Long failCount = 0L;//失败数
        for (LfTempImportDetails lfTempImportDetails : lfTempImportDetailsList) {
            try {
                //调用提交审核
                if (0 !=lfTempImportDetails.getImportStatus()){
                    Map<String,String> mapRes =  new HmParseRmsTool().analysisRichMedia(lfTempImportDetails,lfCorp.getCorpCode());
                    if ("200".equals(mapRes.get("status"))){
                        Long sptemplid = Long.valueOf(mapRes.get("sptempId"));
                        lfTempImportDetails.setSptemplid(sptemplid);
                        lfTempImportDetails.setImportStatus(1);//提交审核成功
                    }else {
                        lfTempImportDetails.setImportStatus(0);//提交审核失败
                        lfTempImportDetails.setCause(mapRes.get("msg"));
                    }
                }
                Thread.sleep(500);//一秒钟提交2个
                empDAO.saveObjectReturnID(lfTempImportDetails);
                if (lfTempImportDetails.getImportStatus() == 1){
                    successCount++;//成功数加1
                }else {
                    failCount++;
                }
            } catch (Exception e) {
                EmpExecutionContext.error(e, "保存详情表信息异常:" + lfTempImportDetails.toString());
                failCount++;//失败数加1
            }
            // System.out.println(lfTempImportDetails.toString());
        }

        //3. update批次表,更新成功数、失败数、处理状态
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("batch", String.valueOf(batch));
        conditionMap.put("corpCode", lfCorp.getCorpCode());
        try {
            List<LfTempImportBatch> lfTempImportBatches = empDAO.findListByCondition(LfTempImportBatch.class, conditionMap, null);
            LfTempImportBatch lfTempImportBatch = lfTempImportBatches.get(0);
            lfTempImportBatch.setSuccessAmount(successCount);
            lfTempImportBatch.setFailAmount(failCount);
            lfTempImportBatch.setProcessStatus(1);
            lfTempImportBatch.setAmount(Long.valueOf(lfTempImportDetailsList.size()));
            empDAO.update(lfTempImportBatch);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "更新批次表信息异常");
        }
    }
}
