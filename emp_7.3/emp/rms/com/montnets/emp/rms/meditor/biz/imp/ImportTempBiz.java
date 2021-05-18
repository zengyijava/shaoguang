package com.montnets.emp.rms.meditor.biz.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.rms.dao.UserDataDAO;
import com.montnets.emp.rms.meditor.biz.IImportTempBiz;
import com.montnets.emp.rms.meditor.biz.IImportTempDetailsBiz;
import com.montnets.emp.rms.meditor.dao.ImportTempDao;
import com.montnets.emp.rms.meditor.dao.imp.ImportTempDaoImp;
import com.montnets.emp.rms.meditor.entity.LfTempImportBatch;
import com.montnets.emp.rms.meditor.entity.SendParam;
import com.montnets.emp.rms.meditor.thread.TempImportThread;
import com.montnets.emp.rms.meditor.vo.LfTempImportDetailsVo;
import com.montnets.emp.rms.rmsapi.constant.OTTHttpConstant;
import com.montnets.emp.rms.rmsapi.model.SendTempParams;
import com.montnets.emp.rms.tools.RmsTxtFileUtil;
import com.montnets.emp.rms.vo.UserDataVO;
import com.montnets.emp.util.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class ImportTempBiz extends BaseBiz implements IImportTempBiz {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    private final ImportTempDao importTempDao = new ImportTempDaoImp();
    private final IImportTempDetailsBiz iImportTempDetailsBiz = new ImportTempDetailsBiz();
    private final PhoneUtil phoneUtil = new PhoneUtil();
    private final CommonBiz commBiz = new CommonBiz();
    private final RmsBalanceLogBiz rmsBalanceLogBiz = new RmsBalanceLogBiz();
    private final RmsTxtFileUtil fileUtil = new RmsTxtFileUtil();
    private final OTTTaskBiz ottTaskBiz = new OTTTaskBiz();
    private final BaseBiz baseBiz = new BaseBiz();
    private final OTTApiBiz ottApiBiz = new OTTApiBiz();
    private final UserDataDAO userDataDAO = new UserDataDAO();
    private final SmsBiz smsBiz = new SmsBiz();
    /**
     * 写文件时候要的换行符
     */
    String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);

    @Override
    public JSONObject importTemp(File file, String corpCode, String tmName, HttpServletRequest request) {

        JSONObject jsonObject = new JSONObject();
        if (null == file) {
            jsonObject.put("status", false);
            jsonObject.put("cause", "文件对象为空，请传入文件对象");
            return jsonObject;
        }
        if (!(file.getName().endsWith(EXCEL_XLS) || file.getName().endsWith(EXCEL_XLSX))) {
            jsonObject.put("status", false);
            jsonObject.put("cause", "传入的文件不是excel文件,请传入.xls，.xlsx表格文件");
            return jsonObject;
        }

        //Worbook生成放在批次表保存前，如果生成worbooke发生异常，则不需要回滚批次表
        Workbook wb = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);

            if (file.getName().endsWith(EXCEL_XLS)) {  //Excel 2003
                wb = new HSSFWorkbook(fileInputStream);
            } else if (file.getName().endsWith(EXCEL_XLSX)) {  // Excel 2007/2010
                wb = new XSSFWorkbook(fileInputStream);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "生成表格对面Workbook异常");
            jsonObject.put("status", false);
            jsonObject.put("cause", e.getMessage());
            return jsonObject;
        }finally{
        	if(fileInputStream != null){
        		try {
					fileInputStream.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "IO关闭异常");
				}
        	}
        }
        int totalRowCount = wb==null?0:wb.getSheetAt(0).getLastRowNum();
        if (totalRowCount > 10001) {
            jsonObject.put("status", false);
            jsonObject.put("cause", "未避免系统资源消耗过大，请将表格行数控制在10000行以内!");
            return jsonObject;
        }

        //判断导入excel数据是否为空，如果为空，则不能导入成功
        String flagStr = checkExcelData(wb);
        if (flagStr != null && !"".equals(flagStr)) {
            jsonObject.put("status", false);
            jsonObject.put("cause", flagStr);
            return jsonObject;
        }


        //1查询批次表，若上一批次未完成，则驳回请求
        LfTempImportBatch lfTempImportBatch = importTempDao.findLastBatchByCorpCode(corpCode);
        if (null != lfTempImportBatch && 1 != lfTempImportBatch.getProcessStatus()) {
            jsonObject.put("status", false);
            jsonObject.put("cause", "上一次批次未处理完成，请耐心等待!");
            return jsonObject;
        }


        //如果该企业没有批次正在处理，往批次表中添加一条处理中的数据
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("corpCode", corpCode);
        List<LfCorp> lfCorps = null;
        try {
            //企业信息
            lfCorps = empDao.findListByCondition(LfCorp.class, conditionMap, null);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "查询企业信息异常");
            jsonObject.put("status", false);
            jsonObject.put("cause", "查询企业信息异常,请查看错误日志");
            return jsonObject;
        }


        LfTempImportBatch lfTempImportBatchSave = new LfTempImportBatch();
        lfTempImportBatchSave.setCorpCode(corpCode);
        if (null != lfCorps) {
            lfTempImportBatchSave.setCorpName(lfCorps.get(0).getCorpName());
        }
        lfTempImportBatchSave.setProcessStatus(0);
        Long batch = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);
        ;
        if (null != lfTempImportBatch) {
            batch = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);
        }
        lfTempImportBatchSave.setBatch(batch);
        try {
            lfTempImportBatchSave.setAmount(0L);
            lfTempImportBatchSave.setSuccessAmount(0L);
            lfTempImportBatchSave.setFailAmount(0L);
            lfTempImportBatchSave.setTmName(tmName);
            Long returnId = empDao.saveObjectReturnID(lfTempImportBatchSave);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "保存批次表信息异常");
            jsonObject.put("status", false);
            jsonObject.put("cause", "保存批次表信息异常,请查看错误日志");
            return jsonObject;
        }

        //启动线程，读取excel数据，写入详情表，调用提交审核
        Thread thread = new Thread(new TempImportThread(wb, lfCorps==null?null:lfCorps.get(0), tmName, lfTempImportBatchSave.getBatch()));
        thread.start();
        jsonObject.put("status", true);
        jsonObject.put("cause", "已提交处理，请注意查看数据!");
        return jsonObject;
    }

    @Override
    public JSONObject exportTempExcel(List<LfTempImportDetailsVo> LfTempImportDetailsVoList) {
        //1.创建Workbook对象
        Workbook wb = wb = new XSSFWorkbook();
        //2.创建.xls文件
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date();
        String basePath = new TxtFileUtil().getWebRoot() + "rms/meditor/file/download/importTemp/";
        String excelFileName = String.valueOf(new Date().getTime()) + ".xlsx";
        String zipName = String.valueOf(new Date().getTime()) + ".zip";
        String excelFilePath = basePath + sdf.format(curDate) + File.separator + excelFileName;
        String zipFilePath = basePath + zipName;
        File excelFile = new File(excelFilePath);
        //3.数据写入
        Sheet sheet = wb.createSheet();
        Cell cell = null;
        Row row = null;
        Integer excelSeq = 0;//序号列

        //设置标题栏
        String[] title = {"序号", "电话号码", "证书图片文件命名", "证书图片格式", "视频文件命名", "视频文件格式", "备注"};
        row = sheet.createRow(0);//创建行
        for (int index = 0; index < title.length; index++) {
            cell = row.createCell(index);
            cell.setCellValue(title[index]);
        }

        for (int i = 0; i < LfTempImportDetailsVoList.size(); i++) {
            row = sheet.createRow(i + 1);//创建行
            for (int cellNum = 0; cellNum < 7; cellNum++) {
                cell = row.createCell(cellNum);//创建列
                switch (cellNum) {
                    case 0:
                        cell.setCellValue(excelSeq++);
                        break;
                    case 1:
                        cell.setCellValue(LfTempImportDetailsVoList.get(i).getPhoneNum());
                        break;
                    case 2:
                        cell.setCellValue(LfTempImportDetailsVoList.get(i).getImageSrc());
                        break;
                    case 3:
                        cell.setCellValue("");
                        break;
                    case 4:
                        cell.setCellValue(LfTempImportDetailsVoList.get(i).getVideoSrc());
                        break;
                    case 5:
                        cell.setCellValue("");
                        break;
                    case 6:
                        cell.setCellValue(LfTempImportDetailsVoList.get(i).getCause());
                        break;
                }
            }
        }
        try {
            if (!excelFile.exists()) {
                if (!(new File(basePath + sdf.format(curDate)).exists())) {
                    new File(basePath + sdf.format(curDate)).mkdirs();
                }
                boolean flag = excelFile.createNewFile();
                if (!flag) {
                    EmpExecutionContext.error("创建文件失败！");
                }
            }
            //将excel写入
            FileOutputStream fileOutputStream = new FileOutputStream(excelFile);
            wb.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            EmpExecutionContext.error(e, "创建excel文件异常");
        }

        try {
            ZipUtil.compress(basePath + sdf.format(curDate), zipFilePath);
            boolean flag = FileUtils.deleteDir(new File(basePath + sdf.format(curDate)));
            if (!flag) {
                EmpExecutionContext.error("刪除文件失敗！");
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "生成压缩文件失败!");
        }
        //4.路径返回
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", true);

        JSONObject jsonObjectData = new JSONObject();
        jsonObjectData.put("src", zipFilePath);

        jsonObject.put("data", jsonObjectData);
        return jsonObject;
    }

    @Override
    public JSONObject exportTempExcelByCondition(LfTempImportDetailsVo lfTempImportDetailsVo, PageInfo pageInfo) {
        List<LfTempImportDetailsVo> lfTempImportDetailsVos = iImportTempDetailsBiz.findLfTempImportDetails(lfTempImportDetailsVo, pageInfo);
        JSONObject jsonObject = exportTempExcel(lfTempImportDetailsVos);
        return jsonObject;
    }

    @Override
    public JSONObject exportDefaultTempExcel() {
        List<LfTempImportDetailsVo> lfTempImportDetailsVos = new ArrayList<LfTempImportDetailsVo>();
        LfTempImportDetailsVo lfTempImportDetailsVo = new LfTempImportDetailsVo();
        lfTempImportDetailsVo.setPhoneNum("13666668888");
        lfTempImportDetailsVo.setCause("");
        lfTempImportDetailsVo.setImageSrc("p13666668888.jpg");
        lfTempImportDetailsVo.setVideoSrc("v13666668888.mp4");
        lfTempImportDetailsVos.add(lfTempImportDetailsVo);
        JSONObject jsonObject = exportTempExcel(lfTempImportDetailsVos);
        return jsonObject;
    }

    /**
     * 判断导入excel数据是否为空，如果为空，则不能导入成功
     *
     * @param wb
     * @return
     */
    public String checkExcelData(Workbook wb) {
        Sheet sheet = wb.getSheetAt(0);
        String cause = "";
        try {
            //行计数
            int rowCount = 0;
            for (Row row : sheet) {
                //跳过第一行标题栏
                if (rowCount < 1) {
                    rowCount++;
                    continue;
                }
                rowCount++;
                if (rowCount > 10001) {
                    cause = "一次导入最多上传的模板数超过了10000 个";
                }

                //获取总列数(空格的不计算)
                int totalCellCounts = row.getPhysicalNumberOfCells();
                for (int cellNum = 0; cellNum < totalCellCounts; cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    String readRes = ExcelReadUtil.getCellValue(cell);
                    if ((readRes == null || "".equals(readRes)) && "".equals(cause)) {
                        switch (cellNum) {
                            case 0:
                                break;
                            case 1:
                                cause = "第" + rowCount + "行电话号码内容为空！";
                                break;
                            case 2:
                                cause = "第" + rowCount + "行证书图片文件命名为空！";
                                break;
                            case 3:
                                break;
                            case 4:
                                cause = "第" + rowCount + "行视频文件命名为空！";
                                break;
                            case 5:
                                break;
                        }
                    } else {
                        if (cellNum == 1 && "".equals(cause)) {
                            if (0 == phoneUtil.checkMobile(readRes, null)) {
                                cause = "第" + rowCount + "行手机号不合法！";
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            cause = "";
            EmpExecutionContext.error(e, "读取导入excel文件异常");
        }
        return cause;
    }

    @Override
    public void handleLfMttask(LfMttask mttask, LfSysuser sysUser) throws Exception {
        //根据taskId查找LfTempImportBatch
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("batch", mttask.getTaskId().toString());
        List<LfTempImportBatch> listByCondition = empDao.findListByCondition(LfTempImportBatch.class, conditionMap, null);
        if (listByCondition.size() == 0) {
            throw new EMPException(IErrorCode.V10001);
        }
        LfTempImportBatch importBatch = listByCondition.get(0);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 任务主题
        mttask.setTitle("");
        //相同内容是1，不同内容是2，动态模板是3 富信暂时不用
        mttask.setTempType(1);
        //提交时间
        mttask.setSubmitTime(Timestamp.valueOf(format.format(Calendar.getInstance().getTime())));
        // 提交状态(创建中1，提交2，取消3)
        mttask.setSubState(2);
        // 发送状态(0是未发送，1是已发送到网关,2发送失败,3网关处理完成,  4发送中,5超时未发送)
        mttask.setSendstate(0);
        //业务类型为默认业务
        mttask.setBusCode("M00000");
        // 提交总数
        mttask.setSubCount(importBatch.getAmount());
        //号码类型（文件上传1或手工输入0）
        mttask.setMobileType(1);
        // 预发送条数预发送条数即有效号码总数
        mttask.setIcount(mttask.getEffCount().toString());
        // 任务主题
        mttask.setTitle(importBatch.getTmName());
        // 信息类型：固定值，富信为21
        mttask.setMsType(21);
        //此处暂时写死 为了判断是否为马拉松项目
        mttask.setMsgType(99);
        mttask.setTimerTime(mttask.getSubmitTime());
        // 是否定时
        mttask.setTimerStatus(0);
        //taskType发送类型   1-EMP界面发送  2-接口发送
        mttask.setTaskType(1);
        //内容编码类型
        mttask.setMsgedcodetype(15);
        //内容时效性 默认48
        mttask.setValidtm(48);
        //是否重发 1-已重发 0-未重发 默认为0
        mttask.setIsRetry(0);
        String spUser = "";
        String spUserPass = "";
        if (StaticValue.getCORPTYPE() == 1) {
            //托管版
            List<UserDataVO> spUserList = userDataDAO.getSPUserList(mttask.getCorpCode());
            if (spUserList != null && spUserList.size() > 0) {
                spUser = spUserList.get(0).getUserId();
                spUserPass = spUserList.get(0).getPassWord();
            }
        } else {
            //标准版
            List<Userdata> spUserList1 = smsBiz.getSpUserList(sysUser);
            if (spUserList1 != null && spUserList1.size() > 0) {
                spUser = spUserList1.get(0).getUserId();
                spUserPass = spUserList1.get(0).getUserPassword();
            }
        }
        // sp账号  随机取一个
        mttask.setSpUser(spUser);
        // 设置发送账户密码
        mttask.setSpPwd(spUserPass);
        //批量任务ID  taskType为1时，batchID就是taskid;taskType为2时，batchID就是网关batch_mt_req表的batchID。
        mttask.setBatchID(mttask.getTaskId());
        //生成文件上传文件服务器
        //判断是否使用集群 FileUrl 文件服务器地址或者本节点地址
        //mttask.setFileuri(StaticValue.ISCLUSTER == 1 ? StaticValue.FILE_SERVER_URL : StaticValue.BASEURL);
        mttask.setFileuri(StaticValue.getISCLUSTER() == 1 ? StaticValue.getFileServerUrl() : StaticValue.BASEURL);
        //发送文件URL
        mttask.setMobileUrl(saveImportDetails2File(mttask));
    }

    private String saveImportDetails2File(LfMttask mttask) throws Exception {
        LfTempImportDetailsVo detailsDto = new LfTempImportDetailsVo();
        //审核通过的
        detailsDto.setAuditstatus(1);
        detailsDto.setBatch(mttask.getTaskId());
        detailsDto.setCorpCode(mttask.getCorpCode());
        IImportTempDetailsBiz importTempDetailsBiz = new ImportTempDetailsBiz();
        List<LfTempImportDetailsVo> importDetails = importTempDetailsBiz.findLfTempImportDetails(detailsDto, null);
        StringBuilder content = new StringBuilder();
        //设置文件名参数
        String[] fileNameparam = {mttask.getTaskId().toString()};
        //获取富信号码文件路径
        String[] url = fileUtil.getSaveRmsMobileFileUrl(mttask.getUserId(), fileNameparam);
        for (LfTempImportDetailsVo vo : importDetails) {
            content.append(vo.getPhoneNum()).append(",").append(vo.getSptemplid()).append(line);
        }
        fileUtil.writeToTxtFile(url[0], content.toString());
        //上传文件服务器
        if (StaticValue.getISCLUSTER() == 1) {
            commBiz.upFileToFileServer(url[1]);
        }
        return url[1];
    }

    @Override
    public void handleFeeByCorpAndSpuser(LfMttask mttask) throws EMPException {
        String opStr = "批量导入详情发送";
        //SP账号余额校验
        String spBalance = rmsBalanceLogBiz.checkSpBalanceRMS(mttask.getSpUser(), mttask.getEffCount(), mttask.getCorpCode(), true);
        /*
         * lessSpFee-SP账号余额不足
         * feeFail-执行SP账号扣费失败
         * koufeiSuccess-扣费成功
         * notNeedToCheck-后付费账户无需扣费
         */
        //如果检测为后付费账户返回"notNeedToCheck" 此处不处理
        if ("koufeiSuccess".equals(spBalance)) {
            String opLog = "SP账号余额扣费成功，扣费sp账号：" + mttask.getSpUser() + ",TaskId：" + mttask.getTaskId() + ",扣费金额：" + mttask.getEffCount() + ",扣费时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            EmpExecutionContext.info(opStr, mttask.getCorpCode(), mttask.getUserId().toString(), "admin", opLog, StaticValue.OTHER);
        } else if ("feeFail".equals(spBalance)) {
            EmpExecutionContext.error(opStr + "，创建企业富信发送任务失败！。查询SP账号余额信息出现异常。ErrorCode：RM00014");
            throw new EMPException(IErrorCode.RM00018);
        } else if ("lessSpFee".equals(spBalance.substring(0, 9))) {
            EmpExecutionContext.error(opStr + "，创建企业富信发送任务失败！。SP账号余额不足，扣费金额大于余额，剩余余额为" + spBalance.substring(10) + "。ErrorCode：RM00017");
            throw new EMPException(IErrorCode.RM00017);
        }

        //富信余额查询操作 1为短信 2为彩信
        String fxBalance = rmsBalanceLogBiz.checkGwFeeRMS(mttask.getSpUser(), Integer.parseInt(mttask.getEffCount().toString()), mttask.getCorpCode(), true, 1);
        /*
            lessgwfee-运营商余额不足
            nogwfee-运营商扣费查询余额信息失败，前端应用账户无法找到对应的后端账户
            feefail-执行运营商余额扣费失败
            notneedtocheck-后付费账户无需扣费,或没有配置需要检查运营商计费
            koufeiSuccess  预付费账户扣费成功
         */
        //如果检测为后付费账户返回"notneedtocheck" 此处不处理
        if ("koufeiSuccess".equals(fxBalance)) {
            String opLog = "运营商余额扣费成功，扣费sp账号：" + mttask.getSpUser() + ",TaskId：" + mttask.getTaskId() + ",扣费金额：" + mttask.getEffCount() + ",扣费时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            EmpExecutionContext.info(opStr, mttask.getCorpCode(), mttask.getUserId().toString(), "admin", opLog, StaticValue.OTHER);
        }
        if ("feefail".equals(fxBalance)) {
            EmpExecutionContext.error(opStr + "，创建企业富信发送任务失败！。运营商扣费查询余额信息出现异常。ErrorCode：RM00014");
            throw new EMPException(IErrorCode.RM00014);
        }
        if ("nogwfee".equals(fxBalance)) {
            EmpExecutionContext.error(opStr + "，创建企业富信发送任务失败！。运营商扣费查询余额信息失败，前端应用账户无法找到对应的后端账户。ErrorCode：RM00013");
            throw new EMPException(IErrorCode.RM00013);
        }
        if ("lessgwfee".equals(fxBalance.substring(0, 9))) {
            EmpExecutionContext.error(opStr + "，创建企业富信发送任务失败！。运营商余额不足，扣费金额大于余额，剩余余额为" + fxBalance.substring(10) + "。ErrorCode：RM00012");
            throw new EMPException(IErrorCode.RM00012);
        }
    }

    @Override
    public String sendMarathonRms(LfMttask lfMttask) throws Exception {
        String result = "sendFail";
        //处理计费 SP账号计费  运营商余额计费
        handleFeeByCorpAndSpuser(lfMttask);

        //将lfMttask对象持久化到数据库中
        Long mtId = empDao.saveObjectReturnID(lfMttask);
        if (mtId > 0) {
            //入库成功则调用发送接口
            lfMttask.setMtId(mtId);
            result = parseMobileUrl2Send(lfMttask);
        }
        return result;
    }

    private String parseMobileUrl2Send(LfMttask lfMttask) throws Exception {
        String fileUrl = lfMttask.getMobileUrl();
        //记录结果
        String result = "sendFail";
        //读取行数位置记录
        long lineIndex = 1L;
        //是否全部上传成功标记
        boolean isAllSuccess = false;
        //文本流
        BufferedReader br = null;
        try {
            //校验号码文件
            if (checkLocalMobileUrlFile(fileUrl)) {
                //获取号码文件绝对路径
                String physicsUrl = fileUtil.getPhysicsUrl(fileUrl);
                File file = new File(physicsUrl);
                br = new BufferedReader(new FileReader(file));
                String tempString = "";
                //先生成表记录
                ottTaskBiz.updateReadPos(lfMttask.getTaskId(), 0L);
                while ((tempString = br.readLine()) != null) {
                    String tempId = tempString.split(",")[1];
                    String phone = tempString.split(",")[0];
                    lfMttask.setTempid(Long.parseLong(tempId));
                    //循环调用发送接口
                    result = sendSingleRms(lfMttask, phone);
                    if ("sendSuccess".equals(result)) {
                        //发送成功 更新LfRmsTaskCtrl表，记录读取文件位置
                        ottTaskBiz.updateReadPos(lfMttask.getTaskId(), lineIndex++);
                        isAllSuccess = true;
                    } else {
                        isAllSuccess = false;
                        break;
                    }
                }
                //根据isAllSuccess判断是否全部发送成功
                if (isAllSuccess) {
                    lfMttask.setSendstate(1);
                    lfMttask.setTempid(0L);
                    empDao.update(lfMttask);
                    return "sendSuccess";
                } else {
                    //发送失败 表示整个task失败 更新发送状态为失败
                    lfMttask.setSendstate(2);
                    lfMttask.setTempid(0L);
                    empDao.update(lfMttask);
                    return result;
                }
            }
        } catch (EMPException epmex) {
            //表明发送失败 表示整个task失败 更新发送状态为失败
            lfMttask.setSendstate(2);
            lfMttask.setTempid(0L);
            empDao.update(lfMttask);
            throw epmex;
        } catch (Exception e) {
            lfMttask.setSendstate(2);
            lfMttask.setTempid(0L);
            empDao.update(lfMttask);
            EmpExecutionContext.error(e, "企业富信批量导入详情，批次从手机号码文件url中取出号码发送出现异常！");
            return result;
        } finally{
        	if(br != null){
        		br.close();
        	}
        }
        return result;
    }

    private String sendSingleRms(LfMttask lfMttask, String phone) throws Exception {
        try {
            String ediVer = OTTHttpConstant.getRMS_EDITOR_VERSION();
            LfSysuser lfSysuser = baseBiz.getById(LfSysuser.class, lfMttask.getUserId());
            SendTempParams tempParams = new SendTempParams();
            ArrayList<SendParam> sendArray = new ArrayList<SendParam>();
            String content = "";
            if ("V3.0".equals(ediVer)) {
                tempParams.setTitle(lfMttask.getTitle());
                //富媒体
                SendParam param = new SendParam();
                param.setType("1");
                param.setContent("");
                sendArray.add(param);

                //富媒体转卡片
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("subject", lfMttask.getTitle());
                map.put("param", new ArrayList());
                String strMap = JSON.toJSONString(map);
                SendParam ottparam = new SendParam();
                ottparam.setContent(strMap);
                ottparam.setType("3");
                sendArray.add(ottparam);

                content = JSONObject.toJSONString(sendArray);
                tempParams.setContent("P1=" + content);
            }
            tempParams.setTmplid(lfMttask.getTempid().toString());
            tempParams.setValidtm(lfMttask.getValidtm());
            tempParams.setUserid(lfMttask.getSpUser());
            tempParams.setPwd(lfMttask.getSpPwd());
            tempParams.setSvrtype(lfMttask.getBusCode());
            tempParams.setMobile(phone);
            tempParams.setTaskid(lfMttask.getTaskId());
            tempParams.setParam1(lfSysuser.getUserCode());
            Map<String, String> resultMap = ottApiBiz.sendTemplate(tempParams);
            //为null说明连接异常
            if (null == resultMap || resultMap.size() == 0) {
                EmpExecutionContext.error("企业富信批量导入详情发送，调用模板发送接口异常,无法连接。ErrorCode：RM0006");
                throw new EMPException(IErrorCode.RM0006);
            }
            //提交至网关成功
            if (Integer.parseInt(resultMap.get("result")) == 0) {
                return "sendSuccess";
            } else {
                return "sendFail&" + resultMap.get("result");
            }
        } catch (EMPException empex) {
            throw empex;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业富信批量导入详情发送出现异常，调用平台模板发送接口出现异常");
            return "sendFail";
        }
    }

    private boolean checkLocalMobileUrlFile(String mobileUrl) {
        //检测本地是否存在,不存在则在集群上检测
        if (!fileUtil.checkFile(mobileUrl)) {
            String checkFile = commBiz.checkServerFile(mobileUrl);
            if (checkFile == null) {
                EmpExecutionContext.error("企业富信发送，获取手机号码Url地址异常。无法从本地或集群获得手机号码文件。");
                return false;
            } else {
                //从文件服务器上下载
                String fileStr = commBiz.downloadFileFromFileCenter(mobileUrl);
                if ("error".equals(fileStr)) {
                    EmpExecutionContext.error("企业富信发送，获取手机号码文件异常。无法从从文件服务器下载手机号码文件。ErrorCode:RM0002");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int checkAduitTemplate(String batch) {
        return importTempDao.checkAduitTemplate(batch);
    }
}
