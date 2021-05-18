package com.montnets.emp.wyquery.biz;

import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.ZipUtil;
import com.montnets.emp.wyquery.vo.LfMttaskVo;
import com.montnets.emp.wyquery.vo.SendedMttaskVo;
import org.apache.poi.ss.usermodel.FontUnderline;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HistoryRecordExcelTool {

    // 报表模板目录
    private final String BASEDIR;

    // 操作员报表文件路径
    private String voucherTemplatePath = "";

    // 产生下行报表路径
    private final String voucherPath = "download";

    private final String TEMP = "temp";

    private final CommonVariables cv;

    public HistoryRecordExcelTool(String context) {
        BASEDIR = context;
        cv = new CommonVariables();
    }

    /**
     * @param fileName 文件名
     * @param mtdList  报表数据
     * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
     * @description：生成发送详情Excel
     * @author
     * @see zhangmin 2012-1-9 创建
     */

    
    public Map<String, String> createSmsMtReportExcel(
            String langName, List<SendedMttaskVo> mtdList, Map<String, String> spgatesMap, String IsexportAll,
            Integer isHidePhone) throws Exception {

        //String execlName = "群发历史详情";
        String execlName = "wyq_historyRecordDetail";

        if ("true".equals(IsexportAll)) {
            voucherTemplatePath = BASEDIR + File.separator
                    + TEMP + File.separator
                    + "wyq_historyRecordDetail.xlsx";

            if (StaticValue.ZH_HK.equals(langName)) {
                voucherTemplatePath = BASEDIR + File.separator
                        + TEMP + File.separator
                        + "wyq_historyRecordDetail-zh_HK.xlsx";
            } else if (StaticValue.ZH_TW.equals(langName)) {
                voucherTemplatePath = BASEDIR + File.separator
                        + TEMP + File.separator
                        + "wyq_historyRecordDetail-zh_TW.xlsx";
            }
        } else {
            voucherTemplatePath = BASEDIR + File.separator
                    + TEMP + File.separator
                    + "wyq_historyRecordOnlyPhone.xlsx";

            if (StaticValue.ZH_HK.equals(langName)) {
                voucherTemplatePath = BASEDIR + File.separator
                        + TEMP + File.separator
                        + "wyq_historyRecordOnlyPhone-zh_HK.xlsx";
            } else if (StaticValue.ZH_TW.equals(langName)) {
                voucherTemplatePath = BASEDIR + File.separator
                        + TEMP + File.separator
                        + "wyq_historyRecordOnlyPhone-zh_TW.xlsx";
            }
        }

        Map<String, String> resultMap = new HashMap<String, String>();

        // 当前每页分页条数
        int intRowsOfPage = 500000;

        // 当前每页显示条数
        int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
                .size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

        int size = intPagesCount; // 生成的工作薄个数

        EmpExecutionContext.info("网优下行记录工作表个数：" + size);
        if (size == 0) {
            EmpExecutionContext.info("网优下行记录无统计详情数据！");
            throw new Exception("网优下行记录无统计详情数据！");
        }

        // 产生报表文件的存储路径
        Date curDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
        String voucherFilePath = BASEDIR + File.separator + voucherPath + File.separator + "groupHisDetail" + File.separator + sdf.format(curDate);
        File fileTemp = new File(voucherFilePath);


        // 报表文件名
        String fileName = null;
        String filePath = null;
        XSSFWorkbook workbook = null;
        FileInputStream in = null;
        FileOutputStream os = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (!fileTemp.exists()) {
                fileTemp.mkdirs();
            }
            // 创建只读的Excel工作薄的对象, 此为模板文件
            File file = new File(voucherTemplatePath);
            SendedMttaskVo mt = null;
            String phone = null;
            String msg = null;
            String errorcode = null;
            String spgate = null;
            String spgateName = null;
            String sendtime = null;

            for (int j = 1; j <= size; j++) {
                fileName = execlName + "_" + sdf.format(curDate) + "_[" + j + "]" + "_" + StaticValue.getServerNumber() + ".xlsx";
                in = new FileInputStream(file);
                workbook = new XSSFWorkbook(in);

                XSSFCellStyle cellStyle = setCellStyle(workbook);
                XSSFSheet sheet = workbook.cloneSheet(0);
                workbook.removeSheetAt(0);
                workbook.setSheetName(0, execlName);
                // 读取模板工作表
                XSSFRow row = null;
                EmpExecutionContext.info("网优下行记录工作薄创建与模板报表移除成功!");

                // 总体流程
                // 根据记录，计算总的页数
                // 每页的处理 一页一个sheet
                // 写入页标题
                // 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
                // 写页尾

                int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
                        .size() : j * intRowsOfPage;

                XSSFCell[] cell = null;


                if ("true".equals(IsexportAll)) {
                    for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

                        mt = (SendedMttaskVo) mtdList.get(k);
                        //运营商
						/*Long unicom = mt.getUnicom();
						String unicomString = null;
						if(unicom-0==0){
							unicomString = "移动";
						}else if(unicom-1==0){
						    unicomString ="联通";
						}else if(unicom-21==0){
							 unicomString ="电信";
						}*/
                        // 手机号
                        phone = mt.getPhone() != null ? mt.getPhone() : "";
                        if (phone != null && !"".equals(phone)
                                && isHidePhone == 0) {
                            phone = cv.replacePhoneNumber(phone);
                        }
                        // 短信文件
                        msg = mt.getMessage() != null ? mt.getMessage() : "";
                        // 分条
						/*String icount = mt.getPknumber() + "/"
								+ mt.getPktotal();*/
                        // 状态
                        // String errorcode =
                        // (mt.getErrorcode().equals("DELIVRD")||mt.getErrorcode().equals("0"))?"发送成功":mt.getErrorcode();
                        errorcode = mt.getErrorcode();
                        if (errorcode != null) {
                            if (errorcode.equals("DELIVRD")
                                    || errorcode.trim().equals("0")) {
                                //errorcode = "发送成功";
                                if (StaticValue.ZH_HK.equals(langName)) {
                                    errorcode = "Sent successfully";
                                } else if (StaticValue.ZH_TW.equals(langName)) {
                                    errorcode = "發送成功";
                                } else {
                                    errorcode = "发送成功";
                                }
                            } else if (errorcode.trim().length() > 0) {
                                //errorcode = "失败[" + errorcode + "]";
                                if (StaticValue.ZH_HK.equals(langName)) {
                                    errorcode = "failure[" + errorcode + "]";
                                } else if (StaticValue.ZH_TW.equals(langName)) {
                                    errorcode = "失敗[" + errorcode + "]";
                                } else {
                                    errorcode = "失败[" + errorcode + "]";
                                }
                            } else {
                                errorcode = "-";
                            }
                        } else {
                            errorcode = "-";
                        }

                        spgate = mt.getSpgate() == null ? "-" : mt.getSpgate();

                        spgateName = spgatesMap.get(mt.getSpgate()) == null ? "-" : spgatesMap.get(mt.getSpgate());

                        sendtime = "-";
                        if (mt.getSendtime() != null) {
                            sendtime = df.format(mt.getSendtime());
                        }
                        String recvtime = "-";
                        if (mt.getRecvtime() != null) {
                            recvtime = df.format(mt.getRecvtime());
                        }

                        cell = new XSSFCell[8];
                        row = sheet.createRow(k + 1);
                        cell[0] = row.createCell(0);
                        cell[1] = row.createCell(1);
                        cell[2] = row.createCell(2);
                        cell[3] = row.createCell(3);
                        cell[4] = row.createCell(4);
                        cell[5] = row.createCell(5);
                        cell[6] = row.createCell(6);
                        cell[7] = row.createCell(7);

                        cell[0].setCellStyle(cellStyle);
                        cell[1].setCellStyle(cellStyle);
                        cell[2].setCellStyle(cellStyle);
                        cell[3].setCellStyle(cellStyle);
                        cell[4].setCellStyle(cellStyle);
                        cell[5].setCellStyle(cellStyle);
                        cell[6].setCellStyle(cellStyle);
                        cell[7].setCellStyle(cellStyle);
//						String[] value = {String.valueOf(k+1),phone,msg,icount,errorcode};

                        cell[0].setCellValue(k + 1D);
                        //cell[1].setCellValue(unicomString);
                        cell[1].setCellValue(phone);
                        cell[2].setCellValue(spgate);
                        cell[3].setCellValue(spgateName);
                        cell[4].setCellValue(sendtime);
                        cell[5].setCellValue(recvtime);
                        cell[6].setCellValue(msg);
                        //cell[4].setCellValue(icount);
                        cell[7].setCellValue(errorcode);
                    }
                } else {
                    for (int k = (j - 1) * intRowsOfPage; k < intCnt; k++) {

                        mt = (SendedMttaskVo) mtdList.get(k);

                        // 手机号
                        phone = mt.getPhone() != null ? mt.getPhone() : "";
                        if (phone != null && !"".equals(phone) && isHidePhone == 0) {
                            phone = cv.replacePhoneNumber(phone);
                        }

                        cell = new XSSFCell[1];
                        row = sheet.createRow(k + 1);

                        cell[0] = row.createCell(0);

                        cell[0].setCellStyle(cellStyle);

                        cell[0].setCellValue(phone);
                    }
                }

                os = new FileOutputStream(voucherFilePath + File.separator + fileName);
                // 写入Excel对象
                workbook.write(os);
            }
            //fileName = "网优历史记录详情" + sdf.format(curDate)+"_"+StaticValue.SERVER_NUMBER + ".zip";
            if (StaticValue.ZH_HK.equals(langName)) {
                fileName = "InternetGiftedHistoryDetails" + sdf.format(curDate) + "_" + StaticValue.getServerNumber() + ".zip";
            } else if (StaticValue.ZH_TW.equals(langName)) {
                fileName = "網優歷史記錄詳情" + sdf.format(curDate) + "_" + StaticValue.getServerNumber() + ".zip";
            } else {
                fileName = "网优历史记录详情" + sdf.format(curDate) + "_" + StaticValue.getServerNumber() + ".zip";
            }

            filePath = BASEDIR + File.separator + voucherPath + File.separator
                    + "groupHisDetail" + File.separator + fileName;

            ZipUtil.compress(voucherFilePath, filePath);
            boolean flag = deleteDir(fileTemp);
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "生成网优历史记录详情Excel异常！");
        } finally {
            // 清除对象
            try {
                if(os != null){
                    os.close();
                }
                if(in != null){
                    in.close();
                }
            } catch (Exception e) {
                EmpExecutionContext.error("流关闭异常！");
            }

            workbook = null;
        }
        resultMap.put("FILE_NAME", fileName);
        resultMap.put("FILE_PATH", filePath);
        return resultMap;

    }

    /**
     * @param fileName 文件名
     * @param mtdList  报表数据
     * @return Map 生成文件成后，返回文件名和文件路径 throws 生成文件失败
     * @description：生成群发历史的excel
     * @author songdejun
     * @see songdejun 2012-1-9 创建
     */

    
    public Map<String, String> createMtReportExcel(String langName, List<LfMttaskVo> mtdList)
            throws Exception {

        //String execlName = "群发历史";
        String execlName = "HistoryRecord";

        voucherTemplatePath = BASEDIR + File.separator + TEMP
                + File.separator + "wyq_historyRecord.xlsx";
        if (StaticValue.ZH_HK.equals(langName)) {
            voucherTemplatePath = BASEDIR + File.separator + TEMP
                    + File.separator + "wyq_historyRecord-zh_HK.xlsx";

        } else if (StaticValue.ZH_TW.equals(langName)) {
            voucherTemplatePath = BASEDIR + File.separator + TEMP
                    + File.separator + "wyq_historyRecord-zh_TW.xlsx";
        }

        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        Map<String, String> resultMap = new HashMap<String, String>();

        // 当前每页分页条数
        int intRowsOfPage = 500000;

        // 当前每页显示条数
        int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
                .size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

        int size = intPagesCount; // 生成的工作薄个数

        EmpExecutionContext.info("网优群发历史工作表个数：" + size);
        if (size == 0) {
            EmpExecutionContext.info("网优群发历史无统计详情数据！");
            throw new Exception("网优群发历史无统计详情数据！");
        }

        // 产生报表文件的存储路径
        Date curDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
        String voucherFilePath = BASEDIR + File.separator + voucherPath + File.separator + "groupHistory" + File.separator + sdf.format(curDate);
        File fileTemp = new File(voucherFilePath);
        if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }

        // 报表文件名
        String fileName = null;
        String filePath = null;
        XSSFWorkbook workbook = null;
        FileInputStream in = null;
        FileOutputStream os = null;
        try {
            String[] wysendinfoArray = null;
            // 创建只读的Excel工作薄的对象, 此为模板文件
            File file = new File(voucherTemplatePath);
            for (int j = 1; j <= size; j++) {

                fileName = execlName + "_" + sdf.format(curDate) + "_[" + j + "]" + "_" + StaticValue.getServerNumber() + ".xlsx";

                in = new FileInputStream(file);
                workbook = new XSSFWorkbook(in);

                XSSFCellStyle cellStyle = setCellStyle(workbook);

                XSSFSheet sheet = workbook.cloneSheet(0);

                workbook.removeSheetAt(0);
                workbook.setSheetName(0, execlName);
                // 读取模板工作表
                XSSFRow row = null;


                EmpExecutionContext.info("网优群发历史工作薄创建与模板报表移除成功!");
                // 总体流程
                // 根据记录，计算总的页数
                // 每页的处理 一页一个sheet
                // 写入页标题
                // 循环写入该页的行数，一次写一行，每页定义了多少行，就写多少数据。直到数据没有。
                // 写页尾

                int intCnt = mtdList.size() < j * intRowsOfPage ? mtdList
                        .size() : j * intRowsOfPage;

                XSSFCell[] cell = new XSSFCell[13];

                for (int k = 0; k < intCnt; k++) {

                    LfMttaskVo mt = (LfMttaskVo) mtdList.get(k);

                    // 操作员
                    String username = mt.getName();
                    if (mt.getUserState() != null && mt.getUserState() == 2) {
                        //username += "(已注销)";
                        if (StaticValue.ZH_HK.equals(langName)) {
                            username += "(Has been canceled)";
                        } else if (StaticValue.ZH_TW.equals(langName)) {
                            username += "(已註銷)";
                        } else {
                            username += "(已注销)";
                        }
                    }
                    // 所属机构
                    String depname = mt.getDepName();
                    // 发送账号
                    String spuser = mt.getSpUser();
                    //发送类型
                    String taskType = "-";
                    if (mt.getTaskType() != null) {
                        //taskType=mt.getTaskType()==1?"EMP发送":"HTTP接入";
                        if (StaticValue.ZH_HK.equals(langName)) {
                            taskType = mt.getTaskType() == 1 ? "EMP sent" : "HTTP access";
                        } else if (StaticValue.ZH_TW.equals(langName)) {
                            taskType = mt.getTaskType() == 1 ? "EMP發送" : "HTTP接入";
                        } else {
                            taskType = mt.getTaskType() == 1 ? "EMP发送" : "HTTP接入";
                        }
                    }

                    //任务批次
                    String taskid = mt.getTaskId() == 0 ? "-" : String.valueOf(mt.getTaskId());

                    // 任务标题
                    String title = mt.getTitle() == null ? "-" : mt.getTitle();
                    // 发送时间
                    String submittime = mt.getTimerTime() == null ? "" : df
                            .format(mt.getTimerTime());
                    // 发送状态
                    String sendState = mt.getSendstate().toString();
                    String error = "";

					/*if (sendState.equals("0")) {
						sendState = "未发送";
					} else if (sendState.equals("1")) {
						sendState = "网关接收成功";
					} else if (sendState.equals("2")) {
						sendState = "失败";
						if (mt.getErrorCodes() != null && !"".equals(mt.getErrorCodes())) {
							error = "[" + mt.getErrorCodes() + "]";
						}
					} else if (sendState.equals("4")) {
						sendState = "EMP发送中";
					} else if(sendState.equals("6")){
						sendState = "终止发送";
					} else if(sendState.equals("3"))
					{
						sendState = "网关处理完成";
					} else {
						sendState = "未使用";
					}*/

                    if (StaticValue.ZH_HK.equals(langName)) {
                        if (sendState.equals("0")) {
                            sendState = "Unsent";
                        } else if (sendState.equals("1")) {
                            sendState = "Gateway received successfully";
                        } else if (sendState.equals("2")) {
                            sendState = "failure";
                            if (mt.getErrorCodes() != null && !"".equals(mt.getErrorCodes())) {
                                error = "[" + mt.getErrorCodes() + "]";
                            }
                        } else if (sendState.equals("4")) {
                            sendState = "EMP Sending";
                        } else if (sendState.equals("6")) {
                            sendState = "Terminate sending";
                        } else if (sendState.equals("3")) {
                            sendState = "Gateway processing is completed";
                        } else {
                            sendState = "Unused";
                        }
                    } else if (StaticValue.ZH_TW.equals(langName)) {
                        if (sendState.equals("0")) {
                            sendState = "未發送";
                        } else if (sendState.equals("1")) {
                            sendState = "網關接收成功";
                        } else if (sendState.equals("2")) {
                            sendState = "失敗";
                            if (mt.getErrorCodes() != null && !"".equals(mt.getErrorCodes())) {
                                error = "[" + mt.getErrorCodes() + "]";
                            }
                        } else if (sendState.equals("4")) {
                            sendState = "EMP發送中";
                        } else if (sendState.equals("6")) {
                            sendState = "終止發送";
                        } else if (sendState.equals("3")) {
                            sendState = "網關處理完成";
                        } else {
                            sendState = "未使用";
                        }
                    } else {
                        if (sendState.equals("0")) {
                            sendState = "未发送";
                        } else if (sendState.equals("1")) {
                            sendState = "网关接收成功";
                        } else if (sendState.equals("2")) {
                            sendState = "失败";
                            if (mt.getErrorCodes() != null && !"".equals(mt.getErrorCodes())) {
                                error = "[" + mt.getErrorCodes() + "]";
                            }
                        } else if (sendState.equals("4")) {
                            sendState = "EMP发送中";
                        } else if (sendState.equals("6")) {
                            sendState = "终止发送";
                        } else if (sendState.equals("3")) {
                            sendState = "网关处理完成";
                        } else {
                            sendState = "未使用";
                        }
                    }

                    String sendStateAnderror = sendState + error;

                    wysendinfoArray = mt.getWySendInfo().split("/");
                    //长度少于4，则数据有问题
                    if (wysendinfoArray.length < 4) {
                        //号码个数
                        mt.setEffCount("0");
                        //提交信息数，预览填
                        mt.setIcount("0");
                        //提交信息数，网关填
                        mt.setIcount2("0");
                        //发送成功数
                        mt.setSucCount("0");
                        //提交失败数
                        mt.setFaiCount("0");
                        //接收失败数
                        mt.setRFail2(0L);
                    } else {
                        //号码个数
                        mt.setEffCount("0");
                        //提交信息数，预览填
                        mt.setIcount(wysendinfoArray[0].trim());
                        //提交信息数，网关填
                        mt.setIcount2(wysendinfoArray[0].trim());

                        //提交信息数
                        long iCount = Long.parseLong(wysendinfoArray[0].trim());
                        //提交失败数
                        long faiCount = Long.parseLong(wysendinfoArray[2].trim());
                        //发送成功数
                        long sucCount = iCount - faiCount;
                        //发送成功数
                        mt.setSucCount(String.valueOf(sucCount));
                        //mt.setSucCount(wysendinfoArray[1].trim());

                        //提交失败数
                        mt.setFaiCount(wysendinfoArray[2].trim());
                        //接收失败数
                        mt.setRFail2(Long.parseLong(wysendinfoArray[3].trim()));
                    }

                    // 有效号码个数
					/*String effcount ="-";
					if(mt.getTaskType()==1){
						effcount= mt.getEffCount();
					}*/

                    // 提交信息总数
                    String icounts = mt.getIcount2() == null ? "-" : mt.getIcount2();
                    // 发送成功总数
                    String succount = mt.getSucCount() == null ? "-" : mt.getSucCount();
                    // 发送失败总数
                    String failcount = mt.getFaiCount() == null ? "-" : mt.getFaiCount();
                    // 接收失败总数
                    String recivefailcount = mt.getRFail2() == null ? "-" : mt.getRFail2().toString();


                    // 短信文件
                    String msgContent;
                    if (mt.getMsg() == null || "".equals(mt.getMsg())) {
                        if (mt.getTaskType() == 1) {
                            //msgContent = "详见文件";
                            if (StaticValue.ZH_HK.equals(langName)) {
                                msgContent = "See the documentation";
                            } else if (StaticValue.ZH_TW.equals(langName)) {
                                msgContent = "詳見文件";
                            } else {
                                msgContent = "详见文件";
                            }
                        } else {
                            msgContent = "-";
                        }
                    } else {
                        msgContent = mt.getMsg();
                    }
                    row = sheet.createRow(k + 1);
                    for (int i = 0; i < cell.length; i++) {
                        cell[i] = row.createCell(i);
                        cell[i].setCellStyle(cellStyle);
                    }
                    if ("-".equals(icounts)) {
                        succount = "-";
                        failcount = "-";
                        recivefailcount = "-";
                    }

                    cell[0].setCellValue(taskid);
                    cell[1].setCellValue(username);
                    cell[2].setCellValue(depname);
                    cell[3].setCellValue(spuser);
                    cell[4].setCellValue(taskType);
                    cell[5].setCellValue(title);
                    cell[6].setCellValue(msgContent);
                    cell[7].setCellValue(submittime);
                    cell[8].setCellValue(sendStateAnderror);
                    //cell[9].setCellValue(effcount);
                    cell[9].setCellValue(icounts);
                    cell[10].setCellValue(succount);
                    cell[11].setCellValue(failcount);
                    cell[12].setCellValue(recivefailcount);

                }
                os = new FileOutputStream(voucherFilePath + File.separator + fileName);
                // 写入Excel对象
                workbook.write(os);
            }
            //fileName = "网优历史记录" + sdf.format(curDate)+"_"+StaticValue.SERVER_NUMBER + ".zip";

            String name = "网优历史记录";
            if (StaticValue.ZH_HK.equals(langName)) {
                name = "NetworkExcellentHistory";
            } else if (StaticValue.ZH_TW.equals(langName)) {
                name = "網優歷史記錄";
            }
            fileName = name + sdf.format(curDate) + "_" + StaticValue.getServerNumber() + ".zip";


            filePath = BASEDIR + File.separator + voucherPath + File.separator
                    + "groupHistory" + File.separator + fileName;

            ZipUtil.compress(voucherFilePath, filePath);
            boolean flag = deleteDir(fileTemp);
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "生成网优历史记录的excel异常！");
        } finally {
            // 清除对象
            try {
                if(os != null){
                    os.close();
                }
                if(in != null){
                    in.close();
                }
            } catch (Exception e) {
                EmpExecutionContext.error("流关闭异常！");
            }
            workbook = null;
        }
        resultMap.put("FILE_NAME", fileName);
        resultMap.put("FILE_PATH", filePath);
        return resultMap;
    }

    /**
     * 群发任务查看导出功能实现
     *
     * @param mtdList
     * @return
     * @throws Exception
     */
    
    public Map<String, String> createSmsSendedBoxExcel(List<LfMttaskVo> mtdList)
            throws Exception {

        //String execlName = "群发任务";
        String execlName = "SmsSendedBox";

        voucherTemplatePath = BASEDIR + File.separator + TEMP
                + File.separator + "smt_AllSmsSendedBox.xlsx";
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        Map<String, String> resultMap = new HashMap<String, String>();
        // 当前每页分页条数
        int intRowsOfPage = 500000;

        // 当前每页显示条数
        int intPagesCount = (mtdList.size() % intRowsOfPage == 0) ? (mtdList
                .size() / intRowsOfPage) : (mtdList.size() / intRowsOfPage + 1);

        int size = intPagesCount; // 生成的工作薄个数

        EmpExecutionContext.info("工作表个数：" + size);
        if (size == 0) {
            EmpExecutionContext.info("无统计详情数据！");
            throw new Exception("无统计详情数据！");
        }

        // 产生报表文件的存储路径
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
        Date curDate = new Date();
        String voucherFilePath = BASEDIR + File.separator + voucherPath + File.separator + "groupSms" + File.separator + sdf.format(curDate);

        File fileTemp = new File(voucherFilePath);

        if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }


        // 报表文件名
        String fileName = null;
        String filePath = null;
        XSSFWorkbook workbook = null;

        InputStream in = null;
        OutputStream os = null;


        try {
            // 创建只读的Excel工作薄的对象, 此为模板文件
            File file = new File(voucherTemplatePath);

            for (int f = 0; f < size; f++) {
                fileName = execlName + "_" + sdf.format(curDate) + "_[" + (f + 1) + "]" + "_" + StaticValue.getServerNumber() + ".xlsx";
                //读取模板
                in = new FileInputStream(file);

                workbook = new XSSFWorkbook(in);

                XSSFCellStyle cellStyle = setCellStyle(workbook);

                XSSFSheet sheet = workbook.cloneSheet(0);
                workbook.removeSheetAt(0);
                workbook.setSheetName(0, execlName);
                // 读取模板工作表
                XSSFRow row = null;
                EmpExecutionContext.debug("工作薄创建与模板移除成功!");


                XSSFCell[] cell = new XSSFCell[11];

                for (int k = 0; k < mtdList.size(); k++) {
                    LfMttaskVo mt = (LfMttaskVo) mtdList.get(k);
                    // 操作员
                    String username = mt.getName();
                    if (mt.getUserState() != null && mt.getUserState() == 2) {
                        username = username + "(已注销)";
                    }
                    // 所属机构
                    String depname = mt.getDepName();
                    String taskType = "-";
                    if (mt.getTaskType() != null) {
                        taskType = mt.getTaskType() == 1 ? "EMP发送" : "HTTP接入";
                    }

                    //任务批次
                    String taskid = mt.getTaskId() == 0 ? "-" : String.valueOf(mt.getTaskId());

                    // 发送账号
                    String spuser = mt.getSpUser();
                    // 任务标题
                    String title = mt.getTitle() == null ? "" : mt.getTitle();
                    // 创建时间
                    String createtime = mt.getSubmitTime() == null ? "-" : df
                            .format(mt.getSubmitTime());

                    // 发送时间
                    String sendedTime = "-";
                    if (mt.getReState() == 2) {// 审批不通过 （发送时间为空）
                        sendedTime = "-";
                    } else if (mt.getSubState() == 3) {// 撤销任务（空）
                        sendedTime = "-";
                    } else if (mt.getSendstate() == 5) {// 超时未发送（空）
                        sendedTime = "-";
                    } else if (mt.getTimerStatus() == 0
                            && mt.getReState() == -1) {// 未定时未审批（待审批）（空）
                        sendedTime = "-";
                    } else if (mt.getTimerStatus() == 1) {// 定时了
                        sendedTime = df.format(mt.getTimerTime());
                        if (mt.getSendstate() == 0) {
                            sendedTime += "(定时中)";
                        }
                    } else if (mt.getSendstate() == 1 || mt.getSendstate() == 2) {// 发送成功或者发送失败
                        sendedTime = df.format(mt.getTimerTime());
                    } else {
                        sendedTime = mt.getTimerTime() == null ? "-" : df
                                .format(mt.getTimerTime());// 这里面的情况就是sendstate=4(发送中)
                    }

                    // 有效号码个数
                    // 有效号码个数
                    String effcount = "-";
                    if (mt.getTaskType() == 1) {
                        effcount = mt.getEffCount();
                    }

                    // 短信文件
                    String msgContent;
                    if (mt.getMsg() == null || "".equals(mt.getMsg())) {
                        if (mt.getTaskType() == 1) {
                            msgContent = "详见文件";
                        } else {
                            msgContent = "-";
                        }
                    } else {
                        msgContent = mt.getMsg();
                    }

                    // 任务状态
                    Integer sendState = mt.getSendstate();
                    Integer subState = mt.getSubState();
                    String state = new String();
                    if (subState == 2 && mt.getReState() == -1) {
                        state = "待审批";
                    } else if (subState == 2 && mt.getReState() == 2) {
                        state = "审批不通过";
                    } else if (subState == 4) {
                        state = "已冻结";
                    } else if (subState == 3) {
                        state = "已撤销";
                    } else if (sendState == 5) {
                        state = "超时未发送";
                    } else if (sendState != 0) {
                        state = "已发送";
                    } else if (subState == 2) {
                        if (mt.getReState() == -1) {
                            state = "待审批";
                        } else if (mt.getReState() == 2) {
                            state = "审批不通过";
                        } else {
                            state = "待发送";
                        }
                    }
                    row = sheet.createRow(k + 1);

                    cell[0] = row.createCell(0);
                    cell[1] = row.createCell(1);
                    cell[2] = row.createCell(2);
                    cell[3] = row.createCell(3);
                    cell[4] = row.createCell(4);
                    cell[5] = row.createCell(5);
                    cell[6] = row.createCell(6);
                    cell[7] = row.createCell(7);
                    cell[8] = row.createCell(8);
                    cell[9] = row.createCell(9);
                    cell[10] = row.createCell(10);


                    cell[0].setCellStyle(cellStyle);
                    cell[1].setCellStyle(cellStyle);
                    cell[2].setCellStyle(cellStyle);
                    cell[3].setCellStyle(cellStyle);
                    cell[4].setCellStyle(cellStyle);
                    cell[5].setCellStyle(cellStyle);
                    cell[6].setCellStyle(cellStyle);
                    cell[7].setCellStyle(cellStyle);
                    cell[8].setCellStyle(cellStyle);
                    cell[9].setCellStyle(cellStyle);
                    cell[10].setCellStyle(cellStyle);

                    cell[0].setCellValue(username);
                    cell[1].setCellValue(depname);
                    cell[2].setCellValue(spuser);
                    cell[3].setCellValue(taskType);
                    cell[4].setCellValue(title);
                    cell[5].setCellValue(taskid);
                    cell[6].setCellValue(createtime);
                    cell[7].setCellValue(sendedTime);
                    cell[8].setCellValue(effcount);
                    cell[9].setCellValue(msgContent);
                    cell[10].setCellValue(state);
                }
                os = new FileOutputStream(voucherFilePath + File.separator + fileName);
                // 写入Excel对象
                workbook.write(os);
            }
            fileName = "群发任务" + sdf.format(curDate) + "_" + StaticValue.getServerNumber() + ".zip";
            filePath = BASEDIR + File.separator + voucherPath + File.separator
                    + "groupSms" + File.separator + fileName;

            ZipUtil.compress(voucherFilePath, filePath);
            boolean flag = deleteDir(fileTemp);
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "群发任务查看导出异常！");
        } finally {
            // 清除对象
            try {
                if(os != null){
                    os.close();
                }
                if(in != null){
                    in.close();
                }
            } catch (Exception e) {
                EmpExecutionContext.error("流关闭异常！");
            }
            workbook = null;
        }
        resultMap.put("FILE_NAME", fileName);
        resultMap.put("FILE_PATH", filePath);
        return resultMap;
    }

    // 将设置单元格属性提取出来成一个方法，方便其他模块调用
    
    public XSSFCellStyle setCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();
        // 字体名称
        font.setFontName("TAHOMA");
        // 粗体
        font.setBold(false);
        // 下环线
        font.setUnderline(FontUnderline.NONE);
        // 字体大小
        font.setFontHeight(11);
        cellStyle.setFont(font);
        // 水平对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        // 竖直对齐
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);

        return cellStyle;
    }

    private static boolean deleteDir(File dir) {

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
