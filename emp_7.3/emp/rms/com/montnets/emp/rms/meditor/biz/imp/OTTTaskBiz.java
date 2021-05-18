package com.montnets.emp.rms.meditor.biz.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.atom.SendSmsAtom;
import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.RmsBalanceLogBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.PreviewParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SmsSpecialDAO;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.rms.LfRmsTaskCtrl;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.rms.dao.SameMmsDao;
import com.montnets.emp.rms.meditor.entity.Excel2JsonParam;
import com.montnets.emp.rms.meditor.entity.LfTempParam;
import com.montnets.emp.rms.meditor.entity.ParamExcel;
import com.montnets.emp.rms.meditor.entity.SendParam;
import com.montnets.emp.rms.meditor.entity.TemplateTotalParam;
import com.montnets.emp.rms.meditor.timer.SendRmsTimerTask;
import com.montnets.emp.rms.meditor.tools.ExcelTool;
import com.montnets.emp.rms.meditor.tools.ParamTool;
import com.montnets.emp.rms.meditor.tools.TemplateUtil;
import com.montnets.emp.rms.rmsapi.constant.OTTHttpConstant;
import com.montnets.emp.rms.rmsapi.model.SendTempParams;
import com.montnets.emp.rms.tools.Excel2JsonDto;
import com.montnets.emp.rms.tools.RmsTxtFileUtil;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.PhoneUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.net.URLEncoder.encode;

/**
 * @author yangdl
 * @date  9:38 2018.8.8
 */
public class OTTTaskBiz extends SuperBiz {


    private OTTApiBiz ottApiBiz = new OTTApiBiz();
    private static final int SEND_RMS_SAME = 11;
    private static final int SEND_RMS_DIFF = 12;
    private RmsBalanceLogBiz rmsBalanceLogBiz = new RmsBalanceLogBiz();
    private BaseBiz baseBiz = new BaseBiz();
    private CommonBiz commBiz = new CommonBiz();
    private RmsTxtFileUtil txtFileUtil = new RmsTxtFileUtil();
    private SendSmsAtom smsAtom = new SendSmsAtom();
    private SameMmsDao mmsDao = new SameMmsDao();
    private SmsSpecialDAO smsSpecialDAO = new SmsSpecialDAO();
    private SmsBiz smsBiz = new SmsBiz();
    /**
     * 写文件时候要的换行符
     */
    private String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);
    private static final Integer MAX_PHONE_NUM = 5000000;
    private PhoneUtil phoneUtil = new PhoneUtil();
    private BlackListAtom blackBiz = new BlackListAtom();
    private static final Integer PER_PHONE_NUM = 1000;
    private ParseExcel2JsonBiz biz = new ParseExcel2JsonBiz();
    private CommonTemplateBiz commonTemplateBiz = new CommonTemplateBiz();


    /**
     * 企业富信不同内容发送到接口方法
     *
     * @param lfMttask 下行任务表实体类对象
     * @return 发送失败 sendFail 发送成功 sendSuccess&TaskId
     * @throws Exception 异常 发送失败 sendFail&ErrorCode
     * @author caihq
     * @date 2018-2-2 上午09:26:46
     */
    private String sendDiffRms(LfMttask lfMttask) throws Exception {
        //记录日志
        LfSysuser lfSysuser = baseBiz.getById(LfSysuser.class, lfMttask.getUserId());
        //操作员名称
        String userName = " ";
        if (lfSysuser != null) {
            userName = lfSysuser.getUserName();
        }
        //记录耗时
        long beforeSend = System.currentTimeMillis();
        String errorInfo = "sendFail";
        String url = lfMttask.getMobileUrl();


        //检测本地是否存在,不存在则在集群上检测
        if (!txtFileUtil.checkFile(url)) {
            String checkFile = commBiz.checkServerFile(url);
            if (checkFile == null) {
                EmpExecutionContext.error("企业富信发送，获取手机号码Url地址异常。无法从本地或集群获得手机号码文件。");
                throw new EMPException(IErrorCode.RM0001);
            } else {
                //从文件服务器上下载
                String fileStr = commBiz.downloadFileFromFileCenter(url);
                if ("error".equals(fileStr)) {
                    EmpExecutionContext.error("企业富信发送，获取手机号码文件异常。无法从从文件服务器下载手机号码文件。ErrorCode:RM0002");
                    throw new EMPException(IErrorCode.RM0002);
                }
            }
        }



        url = txtFileUtil.getPhysicsUrl(url);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(url)), "GBK"));
        SendTempParams tempParams = null;
        Map<String, String> resultMap = null;
        String tmp = "";
        int sendSuccessCount = 0;
        boolean sendFlag = false;
        try{
	        while ((tmp = reader.readLine()) != null) {
	            try {
	                StringBuilder param = new StringBuilder();
	                String[] str;
	                tmp = tmp.trim();
	                tempParams = new SendTempParams();
	                if (tmp.startsWith("V3.0")) {
	                    //3.0模板发送
	                    str = tmp.substring(4).split(",", 2);
	                    tempParams.setContent("P1=" + str[1]);
	                    tempParams.setTitle(lfMttask.getTitle());
	                } else if (tmp.startsWith("V2.0")) {
	                    //2.0模板发送
	                    str = tmp.substring(4).split(",", 2);
	                    tempParams.setContent("P1=" + URLEncoder.encode(str[1], OTTHttpConstant.UTF8_ENCODE));
	                } else {
	                    //1.0模板发送
	                    str = tmp.split(",");
	                    for (int i = 1; i < str.length; i++) {
	                        //param += "P"+(i)+"="+str[i]+"&";
	                        param.append("P").append(i).append("=").append(URLEncoder.encode(str[i], OTTHttpConstant.UTF8_ENCODE)).append("&");
	                    }
	                    tempParams.setContent(param.substring(0, param.length() - 1));
	                }
	                tempParams.setMobile(str[0]);
	                tempParams.setUserid(lfMttask.getSpUser());
	                tempParams.setPwd(lfMttask.getSpPwd());
	                tempParams.setValidtm(lfMttask.getValidtm());
	                tempParams.setTmplid(lfMttask.getTempid().toString());
	                tempParams.setTaskid(lfMttask.getTaskId());
	                tempParams.setParam1(lfSysuser.getUserCode());
	                tempParams.setSvrtype(lfMttask.getBusCode());
	                resultMap = ottApiBiz.sendTemplate(tempParams);
	                //判断发送结果
		            /*if(null != resultMap && resultMap.size() > 0 && "0".equals(resultMap.get("result"))){
		            	sendSuccessCount++;
		            	sendFlag = true;
		            }else{
		            	errorInfo = "sendFail";
		            	sendFlag = false;
		            	break;
		            }*/
	                //没有返回结果说明调用接口异常
	                if (resultMap == null || resultMap.size() == 0) {
	                    EmpExecutionContext.error("企业富信相同内容发送，调用模板发送接口异常,无法连接。ErrorCode：RM0006");
	                    throw new EMPException(IErrorCode.RM0006);
	                }
	                if ("0".equals(resultMap.get("result"))) {
	                    sendSuccessCount++;
	                    sendFlag = true;
	                } else {
	                    errorInfo = "sendFail&" + resultMap.get("result");
	                    sendFlag = false;
	                    break;
	                }
	            } catch (Exception e) {
	                EmpExecutionContext.error(e, "企业富信不同内容发送,调用发送接口异常。ErrorCode：RM0006");
	                errorInfo = "sendFail&RM0006";
	                sendFlag = false;
	                break;
	            }
	            if (sendSuccessCount % 100 == 0) {
	                updateReadPos(lfMttask.getTaskId(), (long) sendSuccessCount);
	            }
	        }
        }finally{
        	if(reader !=null){
        		reader.close();
        	}
        }
        updateReadPos(lfMttask.getTaskId(), (long) sendSuccessCount);

        //lfMttask.setSucCount(String.valueOf(sendSuccessCount));
        //lfMttask.setFaiCount(String.valueOf(list.size()-sendSuccessCount));
        if(resultMap==null){
            resultMap = new HashMap<String,String>();
        }
        
        if (sendFlag) {
            //更新状态为已发送到网关
            lfMttask.setSendstate(1);
            empDao.update(lfMttask);
            //记录日志
            String opContent = "发送成功。" +
                    "提交数：" + lfMttask.getSubCount() +
                    ",有效数：" + lfMttask.getEffCount() +
                    ",提交时间：" + new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(lfMttask.getSubmitTime()) +
                    ",taskId：" + lfMttask.getTaskId() +
                    ",发送主题：" + StringUtils.defaultIfEmpty(lfMttask.getTaskName(), "无发送主题") +
                    ",模板Id：" + lfMttask.getTempid() +
                    ",内容时效：" + lfMttask.getValidtm() + "小时" +
                    ",业务类型：" + lfMttask.getBusCode() +
                    ",发送账号：" + lfMttask.getSpUser() +
                    ",平台返回流水号：" + resultMap.get("msgid") +
                    ",custid：" + StringUtils.defaultIfEmpty(resultMap.get("custid"), "") +
                    ",耗时：" + (System.currentTimeMillis() - beforeSend) + "毫秒";
            EmpExecutionContext.info("企业富信不同内容发送", lfMttask.getCorpCode(), lfMttask.getUserId().toString(), userName, opContent, "OTHER");
            return "sendSuccess&" + lfMttask.getTaskId();
        } else {
            lfMttask.setSendstate(2);
            empDao.update(lfMttask);
            String opContent = "发送失败。" +
                    "提交数：" + lfMttask.getSubCount() +
                    ",有效数：" + lfMttask.getEffCount() +
                    ",提交时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lfMttask.getSubmitTime()) +
                    ",taskId：" + lfMttask.getTaskId() +
                    ",发送主题：" + StringUtils.defaultIfEmpty(lfMttask.getTaskName(), "无发送主题") +
                    ",模板Id：" + lfMttask.getTempid() +
                    ",内容时效：" + lfMttask.getValidtm() + "小时" +
                    "，业务类型：" + lfMttask.getBusCode() +
                    ",发送账号：" + lfMttask.getSpUser() +
                    ",平台返回错误码：" + resultMap.get("result") +
                    ",平台返回流水号：" + resultMap.get("msgid") +
                    ",custid：" + StringUtils.defaultIfEmpty(resultMap.get("custid"), "") +
                    ",耗时：" + (System.currentTimeMillis() - beforeSend) + "毫秒";
            EmpExecutionContext.info("企业富信不同内容发送", lfMttask.getCorpCode(), lfMttask.getUserId().toString(), userName, opContent, "OTHER");
            return errorInfo;
        }
    }

    /**
     * 解析Excel，验证号码合法性、过滤黑名单、过滤重号、过滤关键字、拼接动态内容、生成有效号码文件和无效号码文件
     *
     * @param fileList
     * @param preParams
     * @param haoduan
     * @param lguserid
     * @param lgcorpcode
     * @param busCode
     * @param spUser
     */
    public void parseRmsPhoneAndContent(List<FileItem> fileList, PreviewParams preParams, String[] haoduan, String lguserid, String lgcorpcode, String busCode, String spUser, String tempId, String tmName, String tempType, String temVer,Locale locale) throws Exception {
        //拼接的模板内容（html）
        String conbinedTempContent = "";
        Workbook workBook = null;
        // 解析号码文件
        String tmp;
        // 短信内容
        String rmsContent = "";
        String mobile = "";
        // 文件内参数个数
        int paramCount = 0;
        // 有效号码数(统计1000个有效号码数时会清零)
        int perEffCount = 0;
        // 有效号码数 (返回前端展示的)
        int totalEffCount = 0;
        // 无效号码数(统计1000个有效号码数时会清零)
        int perBadCount = 0;
        // 无效号码数(返回前端展示的)
        int totalBadCount = 0;
        //号码返回状态
        int resultStatus = 0;
        //模板Excel参数信息转为的json
        String jsonStr = "";
        KeyWordAtom keyWordAtom = new KeyWordAtom();
        StringBuilder viewContentSb = new StringBuilder();
        //发送类型
        int sendType = preParams.getSendType();
        //根据rmsContent拼接Excel2JsonDto对象
        List<Excel2JsonDto> excel2JsonDtos;
        //国外通道信息
        DynaBean spGate = smsBiz.getInterSpGateInfo(spUser);
        // 获取1到n-1条英文短信内容的长度
        int longSmsFirstLen = 0;
        // 单条短信字数
        int singlelen = 0;
        //英文短信签名长度
        int signLen = 0;
        //是否支持英文短信，1：支持；0：不支持
        int gateprivilege = 0;
        //富信编辑器版本
        String ediVer = OTTHttpConstant.getRMS_EDITOR_VERSION();
        //场景卡片模板内容12
        String ottFrontJson = "";
        //富媒体11
        String rmsFrontJson = "";
        //富文本13
        String richfrontJson = "";
        //短信14
        String smsFrontJson = "";

        if (spGate != null) {
            // 获取1到n-1条英文短信内容的长度
            longSmsFirstLen = Integer.parseInt(spGate.get("enmultilen1").toString());
            //英文短信签名长度
            signLen = Integer.parseInt(spGate.get("ensignlen").toString());
            // 单条短信字数
            singlelen = Integer.parseInt(spGate.get("ensinglelen").toString());
            // 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
            if (signLen == 0) {
                //国外通道英文短信签名长度
                signLen = smsBiz.getenSmsSignLen(spGate.get("ensignstr").toString().trim());
            }
            //是否支持英文短信，1：支持；0：不支持
            gateprivilege = Integer.parseInt(spGate.get("gateprivilege").toString());
            if ((gateprivilege & 2) == 2) {
                gateprivilege = 1;
            }

            //签名前置
            if ((gateprivilege & 4) == 4) {
                longSmsFirstLen = longSmsFirstLen - signLen;
            }
        }
        //号码类型
        int phoneType = 0;
        //0:英文短信;1:中文短信
        String SmsCharType = "1";
        //短信长度
        int smsLen = 0;

        //下标0(0:英文短信;1:中文短信);下标1(处理后的短信内容);下标2(英文短信长度)
        String[] smsContentInfo = {"1", "", "0"};
        try {
            // 有效号码
            StringBuffer contentSb = new StringBuffer();
            // 无效号码
            StringBuffer badContentSb = new StringBuffer();

            //将错误文件信息写入
            StringBuffer invalidFile = new StringBuffer();
            for (Map.Entry<String, String> entry : preParams.getInvalidFileName().entrySet()) {
                invalidFile.append(MessageUtils.getWord("common", locale, "common_send_21")+"(").append(entry.getValue()).append("):").append(entry.getKey()).append(line);
            }
            if (invalidFile.length() > 0) {
                writePhoneFile2txt("badFile", preParams, invalidFile);
            }
/*          12 场景卡片模板内容 ottFrontJson
            11 富媒体 rmsFrontJson
            13 富文本 richfrontJson
            14 短信 smsFrontJson  */
            long contentStartTime = System.currentTimeMillis();
            EmpExecutionContext.error("开始获取frontJson:"+contentStartTime);
            if (tempType.equals("11")){
                rmsFrontJson = commonTemplateBiz.getContentbyTempid(tempId, "1", "11");
            }else if (tempType.equals("12")){
                rmsFrontJson = commonTemplateBiz.getContentbyTempid(tempId, "1", "11");
                ottFrontJson = commonTemplateBiz.getContentbyTempid(tempId, "1", "12");
            }else if (tempType.equals("13")){
                richfrontJson = commonTemplateBiz.getContentbyTempid(tempId, "1", "13");
            }
            smsFrontJson = commonTemplateBiz.getContentbyTempid(tempId, "1", "14");
            long contentEndTime = System.currentTimeMillis();
            EmpExecutionContext.error("获取frontJson结束:"+contentStartTime+" 耗时:"+(contentEndTime-contentStartTime));

            List<LfTempParam> paramList = commonTemplateBiz.getParamByTempid(tempId);
            long startTime = System.currentTimeMillis();
            EmpExecutionContext.error("开始处理预览文件:"+startTime);


            for (FileItem fileItem : fileList) {

                long workStartTime = System.currentTimeMillis();

                // 如果上传号码大于500w就不允许发送
                if (preParams.getEffCount() > MAX_PHONE_NUM) {
                    EmpExecutionContext.error("不同内容预览，有效号码数超过" + StaticValue.MAX_PHONE_NUM + "，corpCode:" + lgcorpcode + "，userid：" + lguserid);
                    throw new EMPException(IErrorCode.A40000);
                }
                if ("".equals(fileItem.getName())) {
                    continue;
                }
                String fileType = fileItem.getName().substring(fileItem.getName().lastIndexOf("."));
                if (".xls".equals(fileType)) {
                    workBook = new HSSFWorkbook(fileItem.getInputStream());
                } else if (".xlsx".equals(fileType)) {
                    workBook = new XSSFWorkbook(fileItem.getInputStream());
                }
                Integer sheetInt = workBook.getNumberOfSheets();
                ArrayList<String> list = new ArrayList<String>();

                long workEndTime = System.currentTimeMillis();
                EmpExecutionContext.error("创建表格workBook对象耗时: "+ (workEndTime-workStartTime));


                //遍历所有sheet
                for (int i = 0; i < sheetInt; i++) {
                    //获得工作薄（Workbook）中工作表（Sheet）
                    EmpExecutionContext.error("开始读取第 "+i +"页数据");
                    Sheet sheet = workBook.getSheetAt(i);
                    if (sheet == null) {
                        break;
                    }
                    Row row2 = sheet.getRow(2);
                    if(row2 == null){
                        break;
                    }
                    short lastCellNum = row2.getLastCellNum();
                    for (int j = 1; j < lastCellNum; j++) {
                        String value = row2.getCell(j).getStringCellValue();
                        list.add(value);
                    }
                    //模板参数个数校验
                    int tempParamCount = preParams.getTempParamCount();
                    int tempParamSize = list.size();
                    if (tempParamCount != tempParamSize) {
                        StringBuilder temp = new StringBuilder("empex"+MessageUtils.getWord("common", locale, "common_send_18"));
                        temp.append(MessageUtils.getWord("common", locale, "common_send_19"));
                        EmpExecutionContext.error("企业富信不同内容发送预览，号码模板文件格式有误:"
                                + "，corpCode:" + lgcorpcode
                                + "，userid：" + lguserid
                                + "，spUser：" + spUser
                                + ", content: " + temp.toString()
                        );
                        throw new EMPException(temp.toString());
                    }

                    //循环要用到的参数
                    LinkedHashMap<String, String> map;
                    String[] contentArray;
                    String previewContent = "";
                    //发送内容集合
                    List<SendParam> sendParamsList;
                    ParamExcel paramExcel;
                    LinkedHashMap<String, String> paramMap;
                    SendParam param;
                    SendParam ottParam;
                    HashMap<String, String> smsMap;
                    SendParam sendParam;
                    ArrayList<ParamExcel> paramExcels;


                    long time1 = 0;
                    long time2 = 0;
                    long time3 = 0;
                    long time4 = 0;
                    long time5 = 0;
                    long time6 = 0;
                    long time7 = 0;
                    long time8 = 0;
                    long time9 = 0;
                    long time10 = 0;
                    long time11 = 0;
                    long time12 = 0;
                    long time13 = 0;
                    long time14 = 0;
                    long time15 = 0;



                    long time1to2 = 0;
                    long time2to3 = 0;
                    long time3to4 = 0;
                    long time4to5 = 0;
                    long time5to6 = 0;
                    long time6to7 = 0;
                    long time7to8 = 0;
                    long time8to9 = 0;
                    long time9to10 = 0;
                    long time10to11 = 0;
                    long time11to12 = 0;
                    long time12to13 = 0;
                    long time13to14 = 0;
                    long time14to15 = 0;

                    //循环判断每一个号码
                    out:

                    for (int j = 4; j <= sheet.getLastRowNum(); j++) {

                        time1 = System.currentTimeMillis();
                        //获取指定行的所有内容，[#MW#]分隔
                        map = new LinkedHashMap<String, String>();
                        tmp = biz.getRowValueByRowId(sheet, j, tempParamCount);
                        if (tmp.equals("")||tmp==null){
                            continue ;
                        }
                        time2 = System.currentTimeMillis();
                        time1to2 = time1to2 + (time2 - time1);
                        int index = tmp.indexOf("[#MW#]");

                        if (index != -1) {
                            mobile = tmp.substring(0, index);
                            // 去掉号码中+86前缀
                            mobile = com.montnets.emp.util.StringUtils.parseMobile(mobile);
                        } else {
                            badContentSb.append(MessageUtils.getWord("common", locale, "common_send_20")).append(tmp).append(line);
                            preParams.setBadModeCount(preParams.getBadModeCount() + 1);
                            preParams.setBadCount(preParams.getBadCount() + 1);
                            perBadCount++;
                            totalBadCount++;
                            continue;
                        }
                        time3 = System.currentTimeMillis();
                        time2to3 = time2to3 + (time3-time2);
                        // 检查号码合法性和号段
                        if ((phoneType = phoneUtil.getPhoneType(mobile, haoduan)) < 0) {
                            badContentSb.append(MessageUtils.getWord("common", locale, "common_send_22")).append(mobile).append(line);
                            preParams.setBadModeCount(preParams.getBadModeCount() + 1);
                            preParams.setBadCount(preParams.getBadCount() + 1);
                            perBadCount++;
                            totalBadCount++;
                            continue;
                        }
                        time4 = System.currentTimeMillis();
                        time3to4 = time3to4 + (time4-time3);
                        //检查重复号码
                        if (preParams.isCheckRepeat() && ((resultStatus = phoneUtil.checkRepeat(mobile, preParams.getValidPhone())) != 0)) {
                            if (resultStatus == 1) {
                                //返回1为重复号码
                                // 前端选择过滤重号时，验证号码是否重复
                                badContentSb.append(MessageUtils.getWord("common", locale, "common_send_23")).append(mobile).append(line);
                                preParams.setBadCount(preParams.getBadCount() + 1);
                                preParams.setRepeatCount(preParams.getRepeatCount() + 1);
                                perBadCount++;
                                totalBadCount++;
                                continue;
                            } else {
                                //-1为非法号码
                                badContentSb.append(MessageUtils.getWord("common", locale, "common_send_24")).append(mobile).append(line);
                                preParams.setBadCount(preParams.getBadCount() + 1);
                                preParams.setBadModeCount(preParams.getBadModeCount() + 1);
                                perBadCount++;
                                totalBadCount++;
                                continue;
                            }
                        }
                        time5 = System.currentTimeMillis();
                        time4to5 = time4to5 +(time5-time4);

                        // 检查是否是黑名单
                        if (blackBiz.checkBlackList(lgcorpcode, mobile, busCode)) {
                            badContentSb.append(MessageUtils.getWord("common", locale, "common_send_25")).append(mobile).append(line);
                            preParams.setBlackCount(preParams.getBlackCount() + 1);
                            preParams.setBadCount(preParams.getBadCount() + 1);
                            perBadCount++;
                            totalBadCount++;
                            continue;
                        }
                        time6 = System.currentTimeMillis();
                        time5to6 = time5to6 + (time6-time5);

                        //过滤短信内容部分
                        rmsContent = tmp.substring(index + 6).trim();
                        contentArray = rmsContent.split("\\[#MW#]");
                        for (int k = 0; k < contentArray.length; k++) {
                            map.put(list.get(k), contentArray[k].equals("[#NULL#]") ? "" : contentArray[k]);
                        }
                        time7 = System.currentTimeMillis();
                        time6to7 = time6to7 + (time7-time6);

                        for (String key : map.keySet()) {
                            boolean b = ExcelTool.checkValue(key, map.get(key), paramList);
                            if (!b) {
                                badContentSb.append(MessageUtils.getWord("common", locale, "common_send_26") + key + "\"-->\"" + map.get(key) + MessageUtils.getWord("common", locale, "common_send_27")).append(mobile).append(line);
                                preParams.setBadModeCount(preParams.getBadModeCount() + 1);
                                preParams.setBadCount(preParams.getBadCount() + 1);
                                perBadCount++;
                                totalBadCount++;
                                continue out;
                            }
                        }
                        time8 = System.currentTimeMillis();
                        time7to8 = time7to8 + (time8-time7);
                        //发送内容集合
                        sendParamsList = new ArrayList<SendParam>();
                        //**************富媒体
                        if ("11".equals(tempType)) {
                            paramExcels = new ArrayList<ParamExcel>();
                            LinkedHashMap<String, ArrayList<String>> excelMap = ParamTool.convertParamRms(ottFrontJson,rmsFrontJson,locale);
                            for (String key : excelMap.keySet()) {
                                paramExcel= new ParamExcel();
                                if (key.equals(MessageUtils.getWord("common", locale, "common_send_17"))){
                                    continue ;
                                }
                                String[] keyArr = ParamTool.resolveString(key);
                                paramExcel.setOrder(keyArr[0]);
                                paramExcel.setType(keyArr[1]);
                                paramMap = new LinkedHashMap<String, String>();
                                List<String> arrayList = excelMap.get(key);
                                for (int k = 0; k < arrayList.size(); k++) {
                                    String s = arrayList.get(k);
                                    String v = map.get(s);
                                    paramMap.put(s, v);
                                }
                                paramExcel.setMap(paramMap);
                                paramExcels.add(paramExcel);
                            }
                            previewContent = TemplateUtil.rmsToString(rmsFrontJson, map);
                            Excel2JsonParam  content = TemplateUtil.richMediaToString(rmsFrontJson, paramExcels);

                            if ("V1.0".equals(temVer)) {
                                for (int k = 0; k < paramExcels.size(); k++) {
                                    paramExcel = paramExcels.get(k);
                                    LinkedHashMap<String, String> paramExcelMap = paramExcel.getMap();
                                    for (String s : paramExcelMap.keySet()) {
                                        jsonStr += paramExcelMap.get(s) + ",";
                                    }
                                }
                                contentSb.append(mobile).append(",").append(jsonStr).append(line);
                            }
                            if ("V2.0".equals(temVer)) {
                                jsonStr = JSONObject.toJSONString(content);
                                contentSb.append("V2.0").append(mobile).append(",").append(jsonStr).append(line);
                            }
                            if ("V3.0".equals(temVer) && "V2.0".equals(ediVer)) {
                                jsonStr = JSONObject.toJSONString(content);
                                contentSb.append("V2.0").append(mobile).append(",").append(jsonStr).append(line);
                            }
                            if ("V3.0".equals(temVer) && "V3.0".equals(ediVer)) {
                                //富媒体
                                param = new SendParam();
                                param.setType("1");
                                param.setContent(JSONObject.toJSONString(content));
                                sendParamsList.add(param);
                                //富媒体新增卡片OTT转换
                                TemplateTotalParam templateTotalParam = TemplateUtil.richMediaParamToTerminal(tmName, rmsFrontJson, paramExcels);
                                ottParam = new SendParam();
                                ottParam.setType("3");
                                ottParam.setContent(JSONObject.toJSONString(templateTotalParam));
                                sendParamsList.add(ottParam);
                                //短信
                                JSONObject jsonObject = JSONObject.parseObject(smsFrontJson);
                                String template = jsonObject.getString("template");
                                LinkedHashMap<String, ArrayList<String>> smsListMap = ParamTool.convertParamRichText(template,locale);
                                smsMap = new HashMap<String, String>();
                                if (smsListMap.size() != 0) {
                                    List<String> smsList = smsListMap.get(MessageUtils.getWord("common", locale, "common_send_13"));
                                    for (String key : map.keySet()) {
                                        for (String s : smsList) {
                                            if (key.equals(s)) {
                                                smsMap.put(key, map.get(key));
                                                break ;
                                            }
                                        }
                                    }
                                }
                                HashMap smsParam = TemplateUtil.getSmsParam(smsMap);
                                sendParam = new SendParam();
                                sendParam.setType("4");
                                sendParam.setContent(smsParam.size() == 0 ? "" : smsParam);
                                sendParamsList.add(sendParam);

                                jsonStr = JSONObject.toJSONString(sendParamsList);
                                contentSb.append("V3.0").append(mobile).append(",").append(jsonStr).append(line);
                            }
                        }
                        time9 = System.currentTimeMillis();
                        time8to9 = time8to9 + (time9-time8);

                        //*************卡片
                        if ("12".equals(tempType)) {
                            //####拼接卡片内容
                            if (StringUtils.isNotEmpty(ottFrontJson)) {
                                TemplateTotalParam ottContent = TemplateUtil.dataToParam(tmName, ottFrontJson, map);
                                ottParam = new SendParam();
                                ottParam.setType("3");
                                ottParam.setContent(JSONObject.toJSONString(ottContent));
                                sendParamsList.add(ottParam);
                                if (j < 12) {
                                    SendParam previewParam = new SendParam();
                                    previewParam.setType("3");
                                    previewParam.setContent(JSONObject.toJSONString(ottContent));
                                    Object[] arr = {previewParam};
                                    previewContent = JSONObject.toJSONString(arr);
                                }
                            }

                            //######拼接富媒体补充方式的内容
                            if (StringUtils.isNotEmpty(rmsFrontJson) && !"[]".equals(rmsFrontJson)) {
                                if (ParamTool.checkParam(rmsFrontJson,locale)) {
                                    paramExcels = new ArrayList<ParamExcel>();
                                    LinkedHashMap<String, ArrayList<String>> excelMap = ParamTool.convertParamRms(ottFrontJson,rmsFrontJson,locale);
                                    for (String key : excelMap.keySet()) {
                                        if (key.equals(MessageUtils.getWord("common", locale, "common_send_17"))){
                                            continue ;
                                        }
                                        paramExcel = new ParamExcel();
                                        String order = key.substring(2);
                                        paramExcel.setOrder(order);
                                        String type = key.replace(order, "");
                                        paramExcel.setType(type);
                                        paramMap = new LinkedHashMap<String, String>();
                                        List<String> arrayList = excelMap.get(key);
                                        for (String str : arrayList){
                                            String v = map.get(str);
                                            paramMap.put(str, v);
                                        }
                                        paramExcel.setMap(paramMap);
                                        paramExcels.add(paramExcel);
                                    }
                                    Excel2JsonParam rmsContents = TemplateUtil.richMediaToString(rmsFrontJson, paramExcels);
                                    SendParam rmsParam = new SendParam();
                                    rmsParam.setType("1");
                                    rmsParam.setContent(JSONObject.toJSONString(rmsContents));
                                    sendParamsList.add(rmsParam);
                                } else {
                                    SendParam rmsParam = new SendParam();
                                    rmsParam.setType("1");
                                    rmsParam.setContent("");
                                    sendParamsList.add(rmsParam);
                                }
                            }
                            //短信
                            JSONObject jsonObject = JSONObject.parseObject(smsFrontJson);
                            String template = jsonObject.getString("template");
                            LinkedHashMap<String, ArrayList<String>> smsListMap = ParamTool.convertParamRichText(template,locale);
                            smsMap = new HashMap<String, String>();
                            if (smsListMap.size() != 0) {
                                List<String> smsList = smsListMap.get(MessageUtils.getWord("common", locale, "common_send_13"));
                                for (String key : map.keySet()) {
                                    for (String s : smsList) {
                                        if (key.equals(s)) {
                                            smsMap.put(key, map.get(key));
                                            break ;
                                        }
                                    }
                                }
                            }

                            HashMap smsParam = TemplateUtil.getSmsParam(smsMap);
                            sendParam = new SendParam();
                            sendParam.setType("4");
                            sendParam.setContent(smsParam.size() == 0 ? "" : smsParam);
                            sendParamsList.add(sendParam);

                            jsonStr = JSONObject.toJSONString(sendParamsList);
                            contentSb.append("V3.0").append(mobile).append(",").append(jsonStr).append(line);
                        }
                        time10 = System.currentTimeMillis();
                        time9to10 = time9to10 + (time10-time9);
                        //***********富文本
                        if ("13".equals(tempType)) {
                            previewContent = TemplateUtil.richToString(richfrontJson, map);
                            TemplateTotalParam content = TemplateUtil.richTextToParam(tmName, map);
                            param = new SendParam();
                            param.setType("3");
                            param.setContent(JSONObject.toJSONString(content));
                            sendParamsList.add(param);

                            //短信
                            JSONObject jsonObject = JSONObject.parseObject(smsFrontJson);
                            String template = jsonObject.getString("template");
                            LinkedHashMap<String, ArrayList<String>> smsListMap = ParamTool.convertParamRichText(template,locale);
                            smsMap = new HashMap<String, String>();
                            if (smsListMap.size() != 0) {
                                List<String> smsList = smsListMap.get(MessageUtils.getWord("common", locale, "common_send_13"));
                                for (String key : map.keySet()) {
                                    for (String s : smsList) {
                                        if (key.equals(s)) {
                                            smsMap.put(key, map.get(key));
                                            break ;
                                        }
                                    }
                                }
                            }
                            HashMap smsParam = TemplateUtil.getSmsParam(smsMap);
                            sendParam = new SendParam();
                            sendParam.setType("4");
                            sendParam.setContent(smsParam.size() == 0 ? "" : smsParam);
                            sendParamsList.add(sendParam);

                            jsonStr = JSONObject.toJSONString(sendParamsList);
                            contentSb.append("V3.0").append(mobile).append(",").append(jsonStr).append(line);
                        }
                        time11 = System.currentTimeMillis();
                        time10to11 = time10to11 + (time11-time10);
                        jsonStr = tmp.substring(index+6);
                        int words = keyWordAtom.filterKeyWord(jsonStr.toUpperCase(),lgcorpcode);
                        //String words = keyWordAtom.checkText(jsonStr.toUpperCase(), kwsList);
                        //过滤内容关键字
                        //StringUtils.isNotEmpty(words) && !"error".equals(words)
                        if (words != 0) {
                            badContentSb.append(MessageUtils.getWord("common", locale, "common_send_28")).append(words).append(")：").append(mobile).append(line);
                            preParams.setBadCount(preParams.getBadCount() + 1);
                            preParams.setKwCount(preParams.getKwCount() + 1);
                            perBadCount++;
                            totalBadCount++;
                            continue;
                        } else if ("error".equals(words)) {
                            EmpExecutionContext.error(MessageUtils.getWord("common", locale, "common_send_29")+"corpCode：" + lgcorpcode + "，smsContent：" + rmsContent.toUpperCase());
                            throw new EMPException(ErrorCodeInfo.B20014);
                        }
                        time12 = System.currentTimeMillis();
                        time11to12 = time11to12 + (time12-time11);

                        //下标0(0:英文短信;1:中文短信);下标1(处理后的短信内容);下标2(短信长度)
                        smsContentInfo = smsBiz.getSmsContentInfo(rmsContent, longSmsFirstLen, singlelen, signLen, gateprivilege);
                        //处理后的短信内容
                        rmsContent = smsContentInfo[1];
                        //国际号码并且存在国外通道
                        if (phoneType == 3 && spGate != null) {
                            //0:英文短信;1:中文短信
                            SmsCharType = smsContentInfo[0];
                            //英文短信长度
                            smsLen = Integer.valueOf(smsContentInfo[2]);
                            //英文短信大于700字或中文短信大于350字
                            if (("0".equals(SmsCharType) && smsLen > (720 - 20)) || ("1".equals(SmsCharType) && rmsContent.length() > (360 - 10))) {
                                badContentSb.append(MessageUtils.getWord("common", locale, "common_send_30")).append(rmsContent.length()).append(MessageUtils.getWord("common", locale, "common_send_31")).append(mobile).append(line);
                                preParams.setBadModeCount(preParams.getBadModeCount() + 1);
                                preParams.setBadCount(preParams.getBadCount() + 1);
                                perBadCount++;
                                totalBadCount++;
                                continue;
                            }
                        }
                        time13 = System.currentTimeMillis();
                        time12to13 = time12to13 + (time13-time12);
                        preParams.setEffCount(preParams.getEffCount() + 1);
                        perEffCount++;
                        totalEffCount++;

                        // 预览10个号码
                        if (preParams.getEffCount() < 11) {
                            if (sendType == 2) {
                                // 预览信息
                                //处理rmsContent 去掉空值，重新计算参数个数
                                String rmsContentStr = ridOfNullAndGetNewParamNum(rmsContent);
                                String[] tempStr = rmsContentStr.split("&PARAMCOUNT&");
                                //tempStr[0]为去掉空值后的rmsContent tempStr[0]为去掉空值后的参数个数
                                viewContentSb.append(tempStr[0]).append("MWHS]#").append(tempStr[1]).append("MWHS]#").
                                        append(mobile).append("MWHS]#").append(previewContent).append(line);
                            } else {
                                viewContentSb.append(mobile).append("MWHS]#").append(rmsContent).append(line);
                            }
                        }
                        time14 = System.currentTimeMillis();
                        time13to14 = time13to14 + (time14-time13);
                        // 将有效号码文件写入txt 一千条存贮一次
                        if (perEffCount >= PER_PHONE_NUM) {
                            writePhoneFile2txt("effFile", preParams, contentSb);
                            perEffCount = 0;
                        }
                        if (perBadCount >= PER_PHONE_NUM) {
                            writePhoneFile2txt("badFile", preParams, badContentSb);
                            perBadCount = 0;
                        }
                        time15 = System.currentTimeMillis();
                        time14to15 = time14to15 + (time15-time14);
                    }
                    EmpExecutionContext.error("各处代码耗时   time1to2:"+time1to2+"   time2to3:"+time2to3+"   time3to4:"+time3to4+"   time4to5:"+time4to5+"    time5to6:"
                            +time5to6+"     time6to7:"+time6to7+"    time7to8:"+time7to8
                            +"  time8to9:"+time8to9+"    time9to10:"+time9to10+"    time10to11:"+time10to11+"    time11to12:"+time11to12+"  time12to13:"+
                            time12to13+"     time13to14:"+time13to14+"  time14to15:"+time14to15);
                }

                //获取提交号码数subCount(即A列从第4行开始后的长度)
                preParams.setSubCount(totalEffCount+totalBadCount);
            }
            long endTime = System.currentTimeMillis();
            EmpExecutionContext.error("预览文件处理结束:"+endTime+" 耗时:"+(endTime - startTime));

            long fileStartTime = System.currentTimeMillis();
            EmpExecutionContext.error("剩余 有效号码、无效号码，和预览信息写入文件开始："+fileStartTime);
            //剩余的有效号码写入文件
            if (contentSb.length() > 0) {
                writePhoneFile2txt("effFile", preParams, contentSb);
            }

            //剩余的无效号码写入文件
            if (badContentSb.length() > 0) {
                writePhoneFile2txt("badFile", preParams, badContentSb);
            }

            // 预览信息写入文件
            if (viewContentSb.length() > 0) {
                txtFileUtil.writeToTxtFile(preParams.getPhoneFilePath()[3], viewContentSb.toString());
                viewContentSb.setLength(0);
            }

            long fileEndTime = System.currentTimeMillis();
            EmpExecutionContext.error("剩余 有效号码、无效号码，和预览信息写入文件结束:"+fileEndTime+" 耗时："+(fileEndTime-fileStartTime));
        } catch (EMPException empEx) {
            txtFileUtil.deleteFile(preParams.getPhoneFilePath()[0]);
            EmpExecutionContext.error(empEx, lguserid, lgcorpcode);
            throw empEx;
        } catch (Exception e) {
            txtFileUtil.deleteFile(preParams.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, lgcorpcode);
            throw new EMPException(IErrorCode.B20005, e);
        }
    }


    /**
     * @param mt LfMttask对象
     * @return 返回结果   {sendFail:发送失败，sendSuccess&TaskId：发送成功，timerSuccess：定时成功，timerFail：定时失败}
     * @throws Exception 异常
     */
    public String addRmsLfMttaskSend(LfMttask mt) throws Exception {
        //记录日志
        String opStr = mt.getBmtType() == SEND_RMS_SAME ? "企业富信相同内容发送" : "企业富信不同内容发送";
        LfSysuser lfSysuser = empDao.findObjectByID(LfSysuser.class, mt.getUserId());
        String userName = "";
        if (lfSysuser != null) {
            userName = lfSysuser.getUserName();
        }

        String spBalance = "notNeedToCheck";
        /*
        //SP账号余额校验
        String spBalance = rmsBalanceLogBiz.checkSpBalanceRMS(mt.getSpUser(), mt.getEffCount(), mt.getCorpCode(), false);
        *//*
         * lessSpFee-SP账号余额不足
         * feeFail-执行SP账号扣费失败
         * koufeiSuccess-扣费成功
         * notNeedToCheck-后付费账户无需扣费
         *//*
        //如果检测为后付费账户返回"notNeedToCheck" 此处不处理
        if ("koufeiSuccess".equals(spBalance)) {
            String opLog = "SP账号余额扣费成功，扣费sp账号：" + mt.getSpUser() + ",TaskId：" + mt.getTaskId() + ",扣费金额：" + mt.getEffCount() + ",扣费时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            EmpExecutionContext.info(opStr, mt.getCorpCode(), mt.getUserId().toString(), userName, opLog, StaticValue.OTHER);
        } else if ("feeFail".equals(spBalance)) {
            EmpExecutionContext.error(opStr + "，创建企业富信发送任务失败！。查询SP账号余额信息出现异常。ErrorCode：RM00014");
            throw new EMPException(IErrorCode.RM00018);
        } else if ("lessSpFee".equals(spBalance.substring(0, 9))) {
            EmpExecutionContext.error(opStr + "，创建企业富信发送任务失败！。SP账号余额不足，扣费金额大于余额，剩余余额为" + spBalance.substring(10) + "。ErrorCode：RM00012");
            throw new EMPException(IErrorCode.RM00017);
        }
        */

        //富信余额查询操作 1为短信 2为彩信
        String fxBalance = rmsBalanceLogBiz.checkGwFeeRMS(mt.getSpUser(), Integer.parseInt(mt.getEffCount().toString()), mt.getCorpCode(), true, 1);
        /*
            lessgwfee-运营商余额不足
            nogwfee-运营商扣费查询余额信息失败，前端应用账户无法找到对应的后端账户
            feefail-执行运营商余额扣费失败
            notneedtocheck-后付费账户无需扣费,或没有配置需要检查运营商计费
            koufeiSuccess  预付费账户扣费成功
         */
        //如果检测为后付费账户返回"notneedtocheck" 此处不处理
        if ("koufeiSuccess".equals(fxBalance)) {
            String opLog = "运营商余额扣费成功，扣费sp账号：" + mt.getSpUser() + ",TaskId：" + mt.getTaskId() + ",扣费金额：" + mt.getEffCount() + ",扣费时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            EmpExecutionContext.info(opStr, mt.getCorpCode(), mt.getUserId().toString(), userName, opLog, StaticValue.OTHER);
        } else if ("nogwfee".equals(fxBalance)) {
            EmpExecutionContext.error(opStr + "，创建企业富信发送任务失败！。运营商扣费查询余额信息失败，前端应用账户无法找到对应的后端账户。ErrorCode：RM00013");
            throw new EMPException(IErrorCode.RM00013);
        } else if ("feefail".equals(fxBalance)) {
            EmpExecutionContext.error(opStr + "，创建企业富信发送任务失败！。运营商扣费查询余额信息出现异常。ErrorCode：RM00014");
            throw new EMPException(IErrorCode.RM00014);
        } else if ("lessgwfee".equals(fxBalance.substring(0, 9))) {
            EmpExecutionContext.error(opStr + "，创建企业富信发送任务失败！。运营商余额不足，扣费金额大于余额，剩余余额为" + fxBalance.substring(10) + "。ErrorCode：RM00012");
            throw new EMPException(IErrorCode.RM00012);
        }
        //SP账号是否为后付费
        Boolean isSpFeeAfter = "notNeedToCheck".equals(spBalance);
        //运营商账号是否为后付费
        Boolean isGwFeeAfter = "notneedtocheck".equals(fxBalance);
        // 实时发送
        if (mt.getTimerStatus() == 0) {
            //将lfMttask对象持久化到数据库中
            Long mtId = empDao.saveObjectReturnID(mt);

            if (mtId == null || mtId <= 0) {
                EmpExecutionContext.error(opStr + "，企业富信发送失败！实时发送富信发送任务设置失败。将LfMttask对象持久化到数据库中异常。ErrorCode：RM00015" +
                        "taskId:" + mt.getTaskId()
                        + "spUser:" + mt.getSpUser()
                        + "lguserid:" + mt.getUserId()
                        + "lgcorpcode" + mt.getCorpCode());
                throw new EMPException(IErrorCode.RM00015);
            }

            //更新成功
            if (isSpFeeAfter && isGwFeeAfter) {
                //后付费实时发送
                return sendRms(mt.getTaskId());
            } else {
                //预付费实时发送
                return preSend(mt, isGwFeeAfter, isSpFeeAfter);
            }

        } else {
            //定时任务
            if (isSpFeeAfter && isGwFeeAfter) {
                //后付费
                return timerSend(mt);
            } else {
                return timerPreSend(mt, isGwFeeAfter, isSpFeeAfter);
            }
        }
    }

    /**
     * 预付费定时发送
     *
     * @param mt           LfMttask对象
     * @param isGwFeeAfter 运营商后付费是否开启
     * @param isSpFeeAfter SP账号后付费是否开启
     * @return 错误值
     * @throws EMPException 异常对象
     */
    private String timerPreSend(LfMttask mt, Boolean isGwFeeAfter, Boolean isSpFeeAfter) throws EMPException {
        //记录日志
        String opStr = mt.getBmtType() == SEND_RMS_SAME ? "企业富信相同内容发送" : "企业富信不同内容发送";
        String returnStr;
        Connection conn = null;
        String userName = "";
        boolean flag = false;
        try {
            LfSysuser lfSysuser = baseBiz.getById(LfSysuser.class, mt.getUserId());
            //操作员名称
            if (lfSysuser != null) {
                userName = lfSysuser.getUserName();
            }

            conn = empTransDao.getConnection();
            //开启事务
            empTransDao.beginTransaction(conn);
            //数据持久化
            Long mtId = empTransDao.saveObjectReturnID(conn, mt);
            //保存失败直接返回
            if (mtId == null || mtId <= 0) {
                EmpExecutionContext.error(opStr + "，企业富信发送失败！预付费情况下，富信定时任务设置失败。将LfMttask对象持久化到数据库中异常。ErrorCode：RM00015" +
                        "taskId:" + mt.getTaskId()
                        + "spUser:" + mt.getSpUser()
                        + "lguserid:" + mt.getUserId()
                        + "lgcorpcode" + mt.getCorpCode());
                throw new EMPException(IErrorCode.RM00015);
            }
            //添加进定时任务
            TaskManagerBiz tm = new TaskManagerBiz();
            SendRmsTimerTask sendRmsTimerTask = new SendRmsTimerTask(
                    mt.getTitle(), 1, new Date(mt.getTimerTime()
                    .getTime()), 0, String.valueOf(mt.getTaskId()));
            sendRmsTimerTask.setTaskId(mt.getTaskId());
            //设置定时任务是否成功
            flag = tm.setJob(sendRmsTimerTask);
            if (flag) {
                //提交事务
                empTransDao.commitTransaction(conn);
                returnStr = "timerSuccess";
                //提交定时成功上传号码文件至文件服务器
                if (StaticValue.getISCLUSTER() == 1) {
                    long beforeUpload = System.currentTimeMillis();
                    //如果连接不上，需要重试三次
                    Boolean isUploadSucceed = commBiz.upFileToFileServer(mt.getMobileUrl());

                    if (isUploadSucceed) {
                        String opContent = "富信预付费-定时任务设置,上传本地号码文件至文件服务器成功，耗时：" + (System.currentTimeMillis() - beforeUpload) + "毫秒";
                        EmpExecutionContext.info(opStr, mt.getCorpCode(), mt.getUserId().toString(), userName, opContent, "OTHER");
                    } else {
                        String opContent = "富信预付费-定时任务设置,上传本地号码文件至文件服务器失败";
                        EmpExecutionContext.error(opStr + "," + opContent + ",ErrorCode:B20023");
                    }
                }
            } else {
                //回滚
                empTransDao.rollBackTransaction(conn);
                returnStr = "timerFail";
            }
        } catch (EMPException empex) {
            throw empex;
        } catch (Exception e) {
            flag = false;
            empTransDao.rollBackTransaction(conn);
            returnStr = "sendError";
            EmpExecutionContext.error(e, opStr + "，企业富信发送失败！预付费情况下，富信定时任务设置失败。" +
                    "taskId:" + mt.getTaskId()
                    + "spUser:" + mt.getSpUser()
                    + "lguserid:" + mt.getUserId()
                    + "lgcorpcode" + mt.getCorpCode());
        } finally {
            // 回收处理
            //定时设置失败则该条下行任务记录不会存表，调回收方法会报空指针
            if (!flag) {
                String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                //手动回收
                if (!isGwFeeAfter) {
                    String recyleRes = rmsBalanceLogBiz.huishouFeeRms(Integer.parseInt(mt.getEffCount() + ""), mt.getSpUser(), 1);
                    if ("true".equals(recyleRes)) {
                        String opLog = "运营商余额回收成功，回收sp账号：" + mt.getSpUser() + ",回收金额：" + mt.getEffCount() + ",回收时间：" + now;
                        EmpExecutionContext.info("企业富信回收余额", mt.getCorpCode(), mt.getUserId().toString(), userName, opLog, StaticValue.OTHER);
                    } else if ("false".equals(recyleRes) || "error".equals(recyleRes)) {
                        EmpExecutionContext.error("企业富信回收余额异常，根据sp账号更新Lf_SPFEE表的RMS_BALANCE字段出现错误。");
                    }
                }
                if (!isSpFeeAfter) {
                    String recyleRes = rmsBalanceLogBiz.recycleSpFee(mt.getEffCount(), mt.getSpUser());
                    //false 入库失败 true 入库成功 error 入库出现错误
                    if ("true".equals(recyleRes)) {
                        String opLog = "运营商余额回收成功，回收sp账号：" + mt.getSpUser() + ",回收金额：" + mt.getEffCount() + ",回收时间：" + now;
                        EmpExecutionContext.info("企业富信回收SP账号余额成功", mt.getCorpCode(), mt.getUserId().toString(), userName, opLog, StaticValue.OTHER);
                    } else if ("false".equals(recyleRes) || "error".equals(recyleRes)) {
                        EmpExecutionContext.error("企业富信回收SP账号余额异常，根据sp账号更新USERFEE表的SENDNUM与SENDEDNUM字段出现错误。");
                    }
                }
            }
            empTransDao.closeConnection(conn);
        }
        return returnStr;
    }

    /**
     * 后付费定时发送
     *
     * @param mt
     * @return
     */
    private String timerSend(LfMttask mt) throws EMPException {
        //记录日志
        String opStr = mt.getBmtType() == SEND_RMS_SAME ? "企业富信相同内容发送" : "企业富信不同内容发送";
        String returnStr = "sendError";
        Connection conn = null;
        try {
            LfSysuser lfSysuser = baseBiz.getById(LfSysuser.class, mt.getUserId());
            //操作员名称
            String userName = " ";

            if (lfSysuser != null) {
                userName = lfSysuser.getUserName();
            }

            conn = empTransDao.getConnection();
            //开启事务
            empTransDao.beginTransaction(conn);
            //保存彩信任务
            Long mtId = empTransDao.saveObjectReturnID(conn, mt);
            //保存失败直接返回
            if (mtId == null || mtId <= 0) {
                EmpExecutionContext.error(opStr + "，企业富信发送失败！后付费情况下，富信定时任务设置失败。将LfMttask对象持久化到数据库中异常。ErrorCode：RM00015" +
                        "taskId:" + mt.getTaskId()
                        + "spUser:" + mt.getSpUser()
                        + "lguserid:" + mt.getUserId()
                        + "lgcorpcode" + mt.getCorpCode());
                throw new EMPException(IErrorCode.RM00015);
            }

            //设置定时情况
            TaskManagerBiz tm = new TaskManagerBiz();
            SendRmsTimerTask sendRmsTimerTask = new SendRmsTimerTask(mt.getTitle(), 1, new Date(mt.getTimerTime()
                    .getTime()), 0, String.valueOf(mt.getTaskId()));
            sendRmsTimerTask.setTaskId(mt.getTaskId());
            //设置定时任务是否成功
            boolean flag = tm.setJob(sendRmsTimerTask);
            if (flag) {
                //提交事务
                empTransDao.commitTransaction(conn);
                returnStr = "timerSuccess";
                //提交定时成功上传号码文件至文件服务器
                //如果连接不上，需要重试三次
                if (StaticValue.getISCLUSTER() == 1) {
                    long beforeSend = System.currentTimeMillis();
                    Boolean isUploadSucceed = commBiz.upFileToFileServer(mt.getMobileUrl());

                    if (isUploadSucceed) {
                        String opContent = "富信-后付费定时任务设置成功。上传本地号码文件至文件服务器成功,耗时：" + (System.currentTimeMillis() - beforeSend) + "毫秒";
                        EmpExecutionContext.info(opStr, mt.getCorpCode(), mt.getUserId().toString(), userName, opContent, "OTHER");
                    } else {
                        String opContent = "富信-后付费定时任务设置成功。上传本地号码文件至文件服务器失败";
                        EmpExecutionContext.error(opStr + "," + opContent + ",ErrorCode:B20023");
                    }
                }
            } else {
                //回滚
                empTransDao.rollBackTransaction(conn);
                returnStr = "timerFail";
            }
        } catch (EMPException empex) {
            throw empex;
        } catch (Exception e) {
            empTransDao.rollBackTransaction(conn);
            EmpExecutionContext.error(e, opStr + "，富信发送失败！后付费情况下，富信定时任务设置失败。" +
                    "taskId:" + mt.getTaskId()
                    + "spUser:" + mt.getSpUser()
                    + "lguserid:" + mt.getUserId()
                    + "lgcorpcode" + mt.getCorpCode());
        } finally {
            empTransDao.closeConnection(conn);
        }
        return returnStr;
    }


    /**
     * 富信后付费实时发送
     *
     * @param taskId 任务Id
     * @return 发送结果 {sendFail:发送失败，sendSuccess&TaskId：发送成功}
     */
    public String sendRms(Long taskId) throws Exception {
        String returnStr = "sendError";
        LfMttask lfMttask = (new CommonBiz()).getLfMttaskbyTaskId(taskId);
        if (lfMttask == null) {
            EmpExecutionContext.error("富信发送失败，根据TaskId查询下行任务记录表实体类对象出现异常，找不到对应的lfMttask对象");
            return returnStr;
        }
        //记录日志
        String opStr = lfMttask.getBmtType() == SEND_RMS_SAME ? "企业富信相同内容发送" : "企业富信不同内容发送";
        LfSysuser lfSysuser = baseBiz.getById(LfSysuser.class, lfMttask.getUserId());
        //操作员名称
        String userName = " ";
        if (lfSysuser != null) {
            userName = lfSysuser.getUserName();
        }
        //开启集群环境则上传文件至文件服务器
        //如果连接不上，需要重试三次
        if (StaticValue.getISCLUSTER() == 1) {
            long beforeUpload = System.currentTimeMillis();
            Boolean isUploadSucceed = commBiz.upFileToFileServer(lfMttask.getMobileUrl());

            if (isUploadSucceed) {
                String opContent = "后付费-实时发送，上传本地号码文件至文件服务器成功,文件相对路径：" + lfMttask.getMobileUrl() + ",耗时：" + (System.currentTimeMillis() - beforeUpload) + "毫秒";
                EmpExecutionContext.info(opStr, lfMttask.getCorpCode(), lfMttask.getUserId().toString(), userName, opContent, "OTHER");
            } else {
                String opContent = "后付费-实时发送，上传本地号码文件至文件服务器失败";
                EmpExecutionContext.error(opStr + "," + opContent + ",ErrorCode:B20023");
            }
        }
        //如果为11则为相同内容发送
        if (SEND_RMS_SAME == lfMttask.getBmtType()) {
            return parseMobileUrl2Send(lfMttask);
        }
        //如果为12则为不同内容发送
        if (SEND_RMS_DIFF == lfMttask.getBmtType()) {
            return sendDiffRms(lfMttask);
        }
        return returnStr;
    }

    /**
     * 批次从手机号码文件url中取出号码发送
     *
     * @param lfMttask 下行任务表实体类
     * @return 返回结果 {sendFail:发送失败，sendSuccess&TaskId：发送成功}
     * @throws Exception 异常
     */
    private String parseMobileUrl2Send(LfMttask lfMttask) throws Exception {
        //记录结果
        String result = "sendFail";
        //读取行数位置记录
        long lineIndex = 0L;
        //文本流
        BufferedReader br = null;
        //是否全部上传成功标记
        Boolean isAllSuccess = false;
        //1000条提交一次
        final long MAX_PHONE_COUNT = 1000L;
        //手机号码地址
        String mobileUrl = "";
        try {
            if (lfMttask == null) {
                EmpExecutionContext.error("富信发送失败，获取下行任务记录表实体类对象出现异常，找不到对应的lfMttask对象。ErrorCode：RM00011");
                return result;
            }

            //************************************************创建下行任务控制表
            updateReadPos(lfMttask.getTaskId(), lineIndex);

            mobileUrl = lfMttask.getMobileUrl();

            if ("".equals(mobileUrl) || mobileUrl == null) {
                EmpExecutionContext.error("企业富信发送，获取手机号码Url地址异常。手机号码Url地址为空。");
                return result;
            }
            //检测本地是否存在,不存在则在集群上检测
            if (!txtFileUtil.checkFile(mobileUrl)) {
                String checkFile = commBiz.checkServerFile(mobileUrl);
                if (checkFile == null) {
                    EmpExecutionContext.error("企业富信发送，获取手机号码Url地址异常。无法从本地或集群获得手机号码文件。");
                    throw new EMPException(IErrorCode.RM0001);
                } else {
                    //从文件服务器上下载
                    String fileStr = commBiz.downloadFileFromFileCenter(mobileUrl);
                    if ("error".equals(fileStr)) {
                        EmpExecutionContext.error("企业富信发送，获取手机号码文件异常。无法从从文件服务器下载手机号码文件。ErrorCode:RM0002");
                        throw new EMPException(IErrorCode.RM0002);
                    }
                }
            }

            //获取号码文件绝对路径
            String physicsUrl = txtFileUtil.getPhysicsUrl(mobileUrl);
            File file = new File(physicsUrl);
            br = new BufferedReader(new FileReader(file));
            //拼接手机号码
            StringBuffer phones = new StringBuffer();
            String tempString = "";
            while ((tempString = br.readLine()) != null) {
                //每读取到1000条号码调用发送方法
                if (lineIndex != 0L && lineIndex % MAX_PHONE_COUNT == 0) {
                    //去掉最后一个逗号
                    if (phones.lastIndexOf(",") != -1) {
                        phones.deleteCharAt(phones.lastIndexOf(","));
                    }
                    result = sendSameBatchRms(lfMttask, phones.toString());
                    if (result.indexOf("sendSuccess") == 0) {
                        //*********************************发送成功 更新LfRmsTaskCtrl表，记录读取文件位置
                        updateReadPos(lfMttask.getTaskId(), lineIndex);
                        //更改标记状态
                        isAllSuccess = true;
                        //清空phones变量
                        phones = new StringBuffer();
                    } else {
                        //清空phones变量
                        phones = new StringBuffer();
                        //发送失败 表示整个task失败 更新发送状态为失败
                        lfMttask.setSendstate(2);
                        empDao.update(lfMttask);
                        break;
                    }
                }
                //读取到一条加1
                lineIndex++;
                //拼接
                phones.append(tempString.trim()).append(",");
            }
            //处理剩余不足1000的号码 继续发送一次
            if (!"".contentEquals(phones) && !",".contentEquals(phones)) {
                //去掉最后一个逗号
                if (phones.lastIndexOf(",") != -1) {
                    phones.deleteCharAt(phones.lastIndexOf(","));
                }
                result = sendSameBatchRms(lfMttask, phones.toString());
                if (result.indexOf("sendSuccess") == 0) {
                    //*************************发送成功 更新LfRmsTaskCtrl表，记录读取文件位置
                    updateReadPos(lfMttask.getTaskId(), lineIndex);
                    isAllSuccess = true;
                } else {
                    //更改标记状态
                    isAllSuccess = false;
                    //发送失败 表示整个task失败 更新发送状态为失败
                    lfMttask.setSendstate(2);
                    empDao.update(lfMttask);
                }
            }
            //根据isAllSuccess判断是否全部发送成功
            if (isAllSuccess) {
                lfMttask.setSendstate(1);
                empDao.update(lfMttask);
                return "sendSuccess&" + lfMttask.getTaskId();
            } else {
                return result;
            }
        } catch (EMPException epmex) {
            //表明发送失败 表示整个task失败 更新发送状态为失败
            lfMttask.setSendstate(2);
            empDao.update(lfMttask);
            throw epmex;
        } catch (Exception e) {
            if (lfMttask != null) {
                lfMttask.setSendstate(2);
                empDao.update(lfMttask);
            }
            EmpExecutionContext.error(e, "企业富信相同内容发送，批次从手机号码文件url中取出号码发送出现异常！");
            return result;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                EmpExecutionContext.error(e, "企业富信相同内容发送,关闭文本流BufferReader异常！ ");
            }
        }
    }

    /**
     * 解析LfMttask对象，调用模板发送接口
     *
     * @param lfMttask LfMttask对象
     * @param phones   待上传手机号 最大支持1000条
     * @return 发送结果 sendSuccess&TaskId  sendFail&ErrorCode
     * @throws Exception 异常返回sendFail
     */
    private String sendSameBatchRms(LfMttask lfMttask, String phones) throws Exception {
        try {
            //记录耗时
            long beforeSend = System.currentTimeMillis();
            //记录日志
            LfSysuser lfSysuser = baseBiz.getById(LfSysuser.class, lfMttask.getUserId());
            //操作员名称
            String userName = " ";
            if (lfSysuser != null) {
                userName = lfSysuser.getUserName();
            }
            Integer tempType = lfMttask.getTempType();

            String ediVer = OTTHttpConstant.getRMS_EDITOR_VERSION();

            String content = "";
            ArrayList<SendParam> sendArray = new ArrayList<SendParam>();

            SendTempParams tempParams = new SendTempParams();

            if (12 == tempType) {
                //卡片
                SendParam param = new SendParam();
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("subject", lfMttask.getTitle());
                map.put("param", new ArrayList());
                content = JSON.toJSONString(map);
                param.setContent(content);
                param.setType("3");
                sendArray.add(param);
                //卡片转富媒体
                SendParam rmsparam = new SendParam();
                rmsparam.setType("1");
                rmsparam.setContent("");
                sendArray.add(rmsparam);
                content = JSONObject.toJSONString(sendArray);
                tempParams.setContent("P1=" + content);
            } else if (11 == tempType) {
                //富信
                if ("V3.0".equals(ediVer)) {
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
            } else if (13 == tempType) {
                //富文本

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("subject", lfMttask.getTitle());
                map.put("param", new ArrayList());
                String strMap = JSON.toJSONString(map);
                SendParam param = new SendParam();
                param.setContent(strMap);
                param.setType("3");
                sendArray.add(param);
                content = JSONObject.toJSONString(sendArray);
                tempParams.setContent("P1=" + content);
            }else if (15 == tempType){
                //H5发送
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("subject", lfMttask.getTitle());
                map.put("param", new ArrayList());
                String strMap = JSON.toJSONString(map);
                SendParam param = new SendParam();
                param.setType("3");
                param.setContent(strMap);
                sendArray.add(param);
                content = JSONObject.toJSONString(sendArray);
                tempParams.setContent("P1=" + content);
            }
            if ("V3.0".equals(ediVer)) {
                tempParams.setTitle(lfMttask.getTitle());
            }
            tempParams.setTmplid(lfMttask.getTempid().toString());
            tempParams.setValidtm(lfMttask.getValidtm());
            tempParams.setUserid(lfMttask.getSpUser());
            tempParams.setPwd(lfMttask.getSpPwd());
            tempParams.setSvrtype(lfMttask.getBusCode());
            tempParams.setMobile(phones);
            tempParams.setTaskid(lfMttask.getTaskId());
            tempParams.setParam1(lfSysuser==null?"":lfSysuser.getUserCode());
            Map<String, String> resultMap = ottApiBiz.sendTemplate(tempParams);

            //为null说明连接异常
            if (null == resultMap || resultMap.size() == 0) {
                EmpExecutionContext.error("企业富信相同内容发送，调用模板发送接口异常,无法连接。ErrorCode：RM0006");
                throw new EMPException(IErrorCode.RM0006);
            }
            //提交至网关成功
            if (Integer.parseInt(resultMap.get("result")) == 0) {
                String opContent = "发送成功。" +
                        "提交数：" + lfMttask.getSubCount() +
                        ",有效数：" + lfMttask.getEffCount() +
                        ",提交时间：" + new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(lfMttask.getSubmitTime()) +
                        ",发送主题：" + StringUtils.defaultIfEmpty(lfMttask.getTaskName(), "无发送主题") +
                        ",模板Id：" + lfMttask.getTempid() +
                        ",内容时效：" + lfMttask.getValidtm() + "小时" +
                        ",业务类型：" + lfMttask.getBusCode() +
                        ",发送账号：" + lfMttask.getSpUser() +
                        ",任务批次：" + lfMttask.getTaskId() +
                        ",平台返回流水号：" + resultMap.get("msgid") +
                        ",custid：" + StringUtils.defaultIfEmpty(resultMap.get("custid"), "") +
                        ",耗时：" + (System.currentTimeMillis() - beforeSend) + "毫秒";
                EmpExecutionContext.info("企业富信相同内容发送", lfMttask.getCorpCode(), lfMttask.getUserId().toString(), userName, opContent, "OTHER");
                return "sendSuccess&" + lfMttask.getTaskId();
            } else {
                String opContent = "发送失败。" +
                        "提交数：" + lfMttask.getSubCount() +
                        ",有效数：" + lfMttask.getEffCount() +
                        ",提交时间：" + new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(lfMttask.getSubmitTime()) +
                        ",任务批次：" + lfMttask.getTaskId() +
                        ",发送主题：" + StringUtils.defaultIfEmpty(lfMttask.getTaskName(), "无发送主题") +
                        ",模板Id：" + lfMttask.getTempid() +
                        ",内容时效：" + lfMttask.getValidtm() + "小时" +
                        "，业务类型：" + lfMttask.getBusCode() +
                        ",发送账号：" + lfMttask.getSpUser() +
                        ",平台返回错误码：" + resultMap.get("result") +
                        ",平台返回流水号：" + resultMap.get("msgid") +
                        ",custid：" + StringUtils.defaultIfEmpty(resultMap.get("custid"), "") +
                        ",耗时：" + (System.currentTimeMillis() - beforeSend) + "毫秒";
                EmpExecutionContext.info("企业富信相同内容发送", lfMttask.getCorpCode(), lfMttask.getUserId().toString(), userName, opContent, "OTHER");
                return "sendFail&" + resultMap.get("result");
            }
        } catch (EMPException empex) {
            throw empex;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业富信相同内容发送出现异常，调用平台模板发送接口出现异常");
            return "sendFail";
        }
    }

    /**
     * 预付费实时发送
     *
     * @param mt           LfMttask对象
     * @param isGwFeeAfter 运营商后付费是否开启
     * @param isSpFeeAfter SP账号后付费是否开启
     * @return sendError 发送错误  或者发送结果
     * @throws EMPException 异常对象
     */
    private String preSend(LfMttask mt, Boolean isGwFeeAfter, Boolean isSpFeeAfter) throws EMPException {
        // 记录日志
        // 回收余额在finally根据控制表发送成功数和lttask的有效数来处理回收余额
        String opStr = mt.getBmtType() == SEND_RMS_SAME ? "企业富信相同内容发送" : "企业富信不同内容发送";
        String returnStr = "sendError";
        try {
            returnStr = this.sendRms(mt.getTaskId());
        } catch (EMPException empex) {
            throw empex;
        } catch (Exception e) {
            EmpExecutionContext.error(e, opStr + "，预付费情况下，企业富信内容发送失败，富信发送出现异常。" + "taskId:" + mt.getTaskId()
                    + "spUser:" + mt.getSpUser() + "lguserid:" + mt.getUserId() + "lgcorpcode" + mt.getCorpCode());
        } finally {
            // 回收
            if (!isGwFeeAfter) {
                huishoufee(mt.getTaskId());
            }
            if (!isSpFeeAfter) {
                recycleSpFee(mt.getTaskId());
            }
        }
        return returnStr;
    }

    /**
     * 根据TaskId查询回收余额(有效号码数 - 发送控制表中已发送数)
     *
     * @param taskId
     * @return
     */
    private Long getRecycleCountBytaskId(Long taskId) {
        Long result = 0L;
        try {
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            //获取发送控制表
            conditionMap.put("taskId", taskId + "");
            List<LfRmsTaskCtrl> taskCtrlList = new BaseBiz().findListByCondition(LfRmsTaskCtrl.class, conditionMap, null);
            LfRmsTaskCtrl lfrmstCtrl = null;
            long currentCount = 0L;
            if (taskCtrlList != null && !taskCtrlList.isEmpty()) {
                lfrmstCtrl = taskCtrlList.get(0);
                currentCount = lfrmstCtrl.getCurrentCount();
            }
            CommonBiz common = new CommonBiz();
            LfMttask current = common.getLfMttaskbyTaskId(taskId);
            result = current.getEffCount() - currentCount;
            return result;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "根据TaskId查询回收余额失败！");
        }
        return result;
    }

    /**
     * 回收SP账号余额
     *
     * @param taskId 任务Id
     */
    private void recycleSpFee(Long taskId) {
        try {
            Long recyleCount = getRecycleCountBytaskId(taskId);
            LfMttask current = commBiz.getLfMttaskbyTaskId(taskId);
            if (recyleCount > 0) {
                String recyleRes = rmsBalanceLogBiz.recycleSpFee(recyleCount, current.getSpUser());
                //false 入库失败 true 入库成功 error 入库出现错误
                if ("true".equals(recyleRes)) {
                    LfSysuser lfSysuser = empDao.findObjectByID(LfSysuser.class, current.getUserId());
                    String userName = lfSysuser.getUserName();
                    String opLog = "运营商余额回收成功，回收sp账号：" + current.getSpUser() + ",回收金额：" + recyleCount + ",回收时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                    EmpExecutionContext.info("企业富信回收SP账号余额成功", current.getCorpCode(), current.getUserId().toString(), userName, opLog, StaticValue.OTHER);
                } else if ("false".equals(recyleRes) || "error".equals(recyleRes)) {
                    EmpExecutionContext.error("企业富信回收SP账号余额异常，根据sp账号更新USERFEE表的SENDNUM与SENDEDNUM字段出现错误。");
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "富信回收SP账号余额失败。");
        }
    }

    /**
     * 回收余额
     *
     * @param taskId
     * @anthor
     */
    private void huishoufee(long taskId) {
        try {
            Long huishouFee = getRecycleCountBytaskId(taskId);

            LfMttask current = commBiz.getLfMttaskbyTaskId(taskId);
            if (huishouFee > 0) {
                String recyleRes = rmsBalanceLogBiz.huishouFeeRms(Integer.parseInt(String.valueOf(huishouFee)), current.getSpUser(), 1);
                //false 入库失败 true 入库成功 empnotfee emp配置不扣费 noneedtocheck 后付费 error 入库出现错误
                if ("true".equals(recyleRes)) {
                    LfSysuser lfSysuser = empDao.findObjectByID(LfSysuser.class, current.getUserId());
                    String userName = "";
                    if (lfSysuser != null) {
                        userName = lfSysuser.getUserName();
                    }
                    String opLog = "运营商余额回收成功，回收sp账号：" + current.getSpUser() + ",回收金额：" + huishouFee + ",回收时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                    EmpExecutionContext.info("企业富信回收余额", current.getCorpCode(), current.getUserId().toString(), userName, opLog, StaticValue.OTHER);
                } else if ("false".equals(recyleRes) || "error".equals(recyleRes)) {
                    EmpExecutionContext.error("企业富信回收余额异常，根据sp账号更新Lf_SPFEE表的RMS_BALANCE字段出现错误。");
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "富信回收余额失败。");
        }
    }

    public LfSysuser getCurrSysUserFromSession(HttpServletRequest request) throws EMPException {
        LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
        //登录操作员信息为空
        if (lfSysuser == null) {
            EmpExecutionContext.error("企业富信相同内容发送预览，从session获取登录操作员信息异常。lfSysuser为null，errCode：" + IErrorCode.V10001);
            throw new EMPException(IErrorCode.V10001);
        }
        return lfSysuser;
    }

    /**
     * 获取表单集合对象
     *
     * @param request  request对象
     * @param lguserid 操作员Id
     * @param sendType 发送类型
     * @return 表单集合对象List
     */
    public List<FileItem> getFileList(HttpServletRequest request, String[] url, Long lguserid, String sendType) throws Exception {
        //记录日志
        String opStr = "1".equals(sendType) ? "企业富信不同内容发送预览" : "企业富信相同内容发送预览";
        //表单元素集合
        List<FileItem> fileList = new ArrayList<FileItem>();
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(1024 * 1024);
            //文件路径前缀
            String tempStr = url[0].substring(0, url[0].lastIndexOf("/"));
            factory.setRepository(new File(tempStr));
            ServletFileUpload upload = new ServletFileUpload(factory);
            //判断所有上传文件最大数
            upload.setSizeMax(StaticValue.MAX_SIZE);
            //以文件方式解析表单
            fileList = upload.parseRequest(request);
        } catch (FileUploadBase.SizeLimitExceededException e) {
            //捕获到文件超出最大数限制的异常
            EmpExecutionContext.error(opStr + "，文件上传失败，超出上传文件大小限制。userId：" + lguserid + "，errCode:" + IErrorCode.V10014);
            throw new EMPException(IErrorCode.V10014, e);
        } catch (FileUploadException e) {
            String logInfo = opStr + "，表单流上传失败。userId:" + lguserid +
                    "，errCode:" + IErrorCode.V10003 +
                    "，耗时:" + (System.currentTimeMillis() - lguserid) + "ms";
            EmpExecutionContext.error(e, logInfo);
            throw new EMPException(IErrorCode.V10003, e);
        }
        return fileList;
    }

    /**
     * 解析request中的表单对象(不包含上传文件信息)
     *
     * @param fileList 表单对象集合
     * @return 包含表单信息的Map
     */
    public Map<String, String> parseRequestForm2Map(List<FileItem> fileList) throws Exception {
        //存放表单控件信息，除了文件格式
        Map<String, String> fieldInfo = new HashMap<String, String>();

        Iterator<FileItem> itemIterator = fileList.iterator();

        while (itemIterator.hasNext()) {
            FileItem fileItem = itemIterator.next();
            //上传文件名字，如果是表单信息应该为null
            String fileName = fileItem.getName();
            if (fileItem.isFormField() && (fileName == null || fileName.length() == 0)) {
                //表单控件name
                String fieldName = fileItem.getFieldName();
                //表单控件value
                String fileValue = fileItem.getString("UTF-8");
                //控件名不为空,将表单控件信息放入MAP
                fieldInfo.put(fieldName, fileValue);
                //将当前fileItem对象从List中删除
                itemIterator.remove();
            }
        }
        return fieldInfo;
    }

    /**
     * 解析request对象中的文件信息并转换成bufferReader的集合
     *
     * @param fileList     表单对象集合
     * @param preParams    预览对象实体类
     * @param url          路径地址
     * @param fileMap      存储文件相关信息的Map
     * @param lguserid     操作员Id
     * @param sendType     发送类型
     * @param allFileNames 所有上传文件名字
     * @param allFileNames 所有上传文件名字
     * @return BufferedReader 集合
     * @throws Exception 异常
     */
    public List<BufferedReader> parseRequestFileForm2ReaderList(List<FileItem> fileList, PreviewParams preParams, String[] url, Long lguserid, String sendType, Map<String, String> fileMap, String allFileNames, String tempVer) throws Exception {
        allFileNames = allFileNames.replaceAll("C:\\\\fakepath\\\\", "");
        //上传文件名
        StringBuilder loadFileName = new StringBuilder("'");
        //文件总大小
        Long allFileSize = 0L;
        //上传文件数量
        Integer fileCount = 0;
        //记录日志
        String opStr = "1".equals(sendType) ? "企业富信不同内容发送预览" : "企业富信相同内容发送预览";
        //上传文件对象集合
        List<FileItem> fileItemsList = new ArrayList<FileItem>();
        //是否超出大小
        boolean isOverSize = true;
        //所有上传文件流集合
        List<BufferedReader> readerList = new ArrayList<BufferedReader>();
        //遍历
        for (FileItem fileItem : fileList) {
            String fileName = fileItem.getName();
            if (!fileItem.isFormField() && fileName != null && fileName.length() > 0) {
                //过滤不需要读取的文件
                if (!"".equals(allFileNames) && allFileNames.contains(fileName)) {
                    continue;
                }
                //判断单个zip文件大小
                isOverSize = isZipOverSize(fileItem);
                //文件大小累加获取总大小
                allFileSize += fileItem.getSize();
                if (isOverSize) {
                    // 大小超出限制
                    EmpExecutionContext.error(opStr + "，文件上传失败，单个zip文件超出上传文件大小限制。userId：" + lguserid + "，errCode:" + IErrorCode.V10014);
                    throw new EMPException(IErrorCode.V10014);
                }
                //有效文件对象存放到集合
                fileItemsList.add(fileItem);
            }
        }
        if (preParams.getSendType() == 2 && !"".equals(tempVer) && "V2.0".equals(tempVer)) {
            //不同内容发送
            //判断模板类型 V2.0直接处理Excel,不用获取文本文件流集合
            for (FileItem aFileItemsList : fileItemsList) {
                loadFileName.append(aFileItemsList.getName()).append("、");
            }
            fileCount = fileItemsList.size();
        } else if (preParams.getSendType() == 1 || (!"".equals(tempVer) && "V1.0".equals(tempVer))) {
            //相同内容发送 或者 处理不同内容发送的V1.0模板时
            // 循环解析每个上传文件对象，获取文本文件流集合
            for (FileItem aFileItemsList : fileItemsList) {
                readerList.addAll(smsAtom.parseFile(aFileItemsList, url[0], fileCount, preParams));
                loadFileName.append(aFileItemsList.getName()).append("、");
            }
            fileCount = fileItemsList.size();
        }

        fileMap.put("fileCount", fileCount.toString());
        fileMap.put("loadFileName", loadFileName.toString());
        fileMap.put("allFileSize", allFileSize.toString());

        return readerList;
    }

    /**
     * 将request中需要的对象信息封装到LfMttask对象中
     *
     * @param request 请求对象
     * @return 封装成功返回LfMttask对象，失败返回null
     */
    public LfMttask getLfMttask(HttpServletRequest request) throws Exception {
        //记录提交时间
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //LfMttask实体对象
        LfMttask mttask = null;
        //用户ID
        String lguseridStr = "";
        //企业编码
        String lgcorpcode = "";
        //企业操作员Id
        Long lguserid = null;
        //记录日志
        String oprInfo = "";
        try {
            //相同0，不同1
            String sendType = request.getParameter("sendType");
            //11富信12卡片13富文本
            String tempType = request.getParameter("templateType");
            //用户ID
            //lguseridStr = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            lguseridStr = SysuserUtil.strLguserid(request);

            //企业编码
            lgcorpcode = request.getParameter("lgcorpcode");
            //富信发送SP账号
            String spUser = request.getParameter("spUser");
            //富信发送主题
            String title = request.getParameter("taskName");
            //业务类型编码
            String busCode = request.getParameter("busCode");
            //是否开启定时发送 0为即时发送 1为定时发送
            String timerStatus = request.getParameter("timerStatus");
            //定时发送时间
            String timerTime = request.getParameter("timerTime");
            //短信内容，富信即为rms文件存储地址
            String msg = request.getParameter("tempUrl");
            //提交号码总数
            String subCount = request.getParameter("subCount");
            //有效号码总数
            String effCount = request.getParameter("effCount");
            //存放手机号码的文件url
            String mobileUrl = request.getParameter("phoneFileUrl");
            //预发送条数即时有效号码总数
            String preSendCount = effCount;
            //富信选择模板文件的ID
            String tempId = request.getParameter("tempId");
            //模板参数个数
            String paramCountStr = request.getParameter("paramCount");
            //时效内容
            String validtm = request.getParameter("validtm");
            // 任务id
            String taskId = request.getParameter("taskId");
            if (sendType != null && sendType.trim().length() > 0) {
                if ("1".equals(sendType)) {
                    oprInfo = "企业富信不同内容发送";
                } else {
                    oprInfo = "企业富信相同内容发送";
                }
            } else {
                EmpExecutionContext.error(oprInfo + ",短信任务发送获取参数异常，sendType:" + sendType + "，errCode：" + "V10001");
                throw new EMPException(IErrorCode.V10001);
            }

            // 判断页面参数是否为空
            if (lguseridStr == null || lgcorpcode == null || spUser == null
                    || msg == null || timerStatus == null || subCount == null
                    || effCount == null || mobileUrl == null || validtm == null) {
                EmpExecutionContext.error(oprInfo + "，发送获取参数异常，" + "strlguserid:" + lguseridStr
                        + ";lgcorpcode:" + lgcorpcode
                        + ";spUser:" + spUser
                        + ";msg:" + msg
                        + ";timerStatus:" + timerStatus
                        + ";subCount:" + subCount
                        + ";effCount:" + effCount
                        + ";mobileUrl:" + mobileUrl
                        + ";preSendCount:" + preSendCount
                        + ";validtm:" + validtm
                        + "，errCode：" + IErrorCode.V10001);
                throw new EMPException(IErrorCode.V10001);
            }
            //主题为默认时设为空
            if (title != null && "不作为富信内容发送".equals(title.trim())) {
                EmpExecutionContext.warn("企业富信相同内容发送获取参数,富信发送主题未设置。" + "title:" + title + "，taskId：" + taskId + "，errCode：" + IErrorCode.V10001);
                title = "";
            }
            //登录操作员信息
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
            //登录操作员信息为空
            if (lfSysuser == null) {
                EmpExecutionContext.error(oprInfo + "，从session获取登录操作员信息异常。lfSysuser为null，errCode：" + IErrorCode.V10001);
                throw new EMPException(IErrorCode.V10001);
            }

            //操作员、企业编码、SP账号检查
            boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfSysuser, lgcorpcode, spUser, null);
            if (!checkFlag) {
                EmpExecutionContext.error(oprInfo + "，检查操作员、企业编码、发送账号不通过，，taskid:" + taskId
                        + "，corpCode:" + lgcorpcode
                        + "，userid：" + lguseridStr
                        + "，spUser：" + spUser
                        + "，errCode:" + IErrorCode.B20007);
                throw new EMPException(IErrorCode.B20007);
            }
            if (!BalanceLogBiz.checkNumber(taskId) || "".equals(taskId)) {
                EmpExecutionContext.error(oprInfo + "，发送获取参数异常，taskId格式非法，taskId:" + taskId
                        + "strlguserid:" + lguseridStr
                        + "，lgcorpcode:" + lgcorpcode
                        + "，errCode：" + IErrorCode.V10001);
                throw new EMPException(IErrorCode.V10001);
            } else {
                //查询任务ID在是否在lf_mttask表已使用,false:存在；true:不存在
                if (!smsSpecialDAO.checkTaskIdNotUse(Long.parseLong(taskId.trim()))) {
                    EmpExecutionContext.error(oprInfo + "，发送获取参数异常，taskId已经被使用:" + taskId
                            + "strlguserid:" + lguseridStr
                            + "，lgcorpcode:" + lgcorpcode
                            + "，errCode：" + IErrorCode.RM0009);
                    throw new EMPException(IErrorCode.RM0009);
                }
            }
            //富信模板
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("sptemplid", tempId);
            List<LfTemplate> list = baseBiz.getByCondition(LfTemplate.class, conditionMap, null);
            if (list == null || list.size() == 0) {
                EmpExecutionContext.error(oprInfo + "，发送获取参数异常，获取模板Id失败，taskId:" + taskId
                        + "strlguserid:" + lguseridStr
                        + "，lgcorpcode:" + lgcorpcode
                        + "，errCode：" + IErrorCode.RM0007);
                throw new EMPException(IErrorCode.RM0007);
            }

            LfTemplate lfTemplate = list.get(0);

            lguserid = Long.valueOf(lguseridStr);
            // 初始化任务对象
            mttask = new LfMttask();
            // 操作员id
            mttask.setUserId(lguserid);
            // 任务主题
            mttask.setTitle(title);
            //内容 富信为url
            mttask.setMsg(msg);
            //相同内容是1，不同内容是2，动态模板是3 富信暂时不用
            mttask.setTempType(Integer.valueOf(tempType));
            //提交时间
            mttask.setSubmitTime(Timestamp.valueOf(format.format(date)));
            // 提交状态(创建中1，提交2，取消3)
            mttask.setSubState(2);
            // 发送状态(0是未发送，1是已发送到网关,2发送失败,3网关处理完成,  4发送中,5超时未发送)
            mttask.setSendstate(0);
            // 提交总数
            mttask.setSubCount(Long.valueOf(subCount));
            // 有效总数
            mttask.setEffCount(Long.valueOf(effCount));
            //群发类型 如果是彩信的话，10普通彩信,11静态模板彩信,12动态模板彩信
            mttask.setBmtType("0".equals(sendType) ? 11 : 12);
            // 号码文件路径
            mttask.setMobileUrl(mobileUrl);
            //号码类型（文件上传1或手工输入0）
            mttask.setMobileType(1);
            // sp账号
            mttask.setSpUser(spUser);
            // 预发送条数预发送条数即时有效号码总数
            mttask.setIcount(preSendCount);
            // 设置发送账户密码
            mttask.setSpPwd(mmsDao.getSPPassword(spUser));
            // 信息类型：固定值，富信为21
            mttask.setMsType(21);
            // 定时任务
            if ("1".equals(timerStatus)) {
                timerTime = timerTime + ":00";
                mttask.setTimerTime(new Timestamp(format.parse(timerTime).getTime()));
            } else {
                // 非定时任务
                mttask.setTimerTime(mttask.getSubmitTime());
            }
            // 是否定时
            mttask.setTimerStatus(("".equals(timerStatus)) ? 0 : Integer.valueOf(timerStatus));
            // 业务类型
            mttask.setBusCode(busCode);
            // 企业编码
            mttask.setCorpCode(lgcorpcode);
            //任务Id
            mttask.setTaskId(Long.valueOf(taskId));
            //富信模板ID
            mttask.setTempid(lfTemplate.getSptemplid());
            //模板参数个数
            paramCountStr = "2";
            if ("1".equals(sendType)) {
                if (paramCountStr != null && !"".equals(paramCountStr) && paramCountStr.matches("^\\d+$")) {
                    mttask.setParamcount(Integer.parseInt(paramCountStr));
                } else {
                    EmpExecutionContext.error(oprInfo + "，发送获取参数异常，获取模板参数个数异常，taskId:" + taskId
                            + "strlguserid:" + lguseridStr
                            + "，lgcorpcode:" + lgcorpcode
                            + "，errCode：" + IErrorCode.RM00010);
                    throw new EMPException(IErrorCode.RM00010);
                }
            }
            //设置模板路径
            mttask.setTmplPath(lfTemplate.getTmMsg());
            //批量任务ID  taskType为1时，batchID就是taskid;taskType为2时，batchID就是网关batch_mt_req表的batchID。
            mttask.setBatchID(Long.parseLong(taskId));
            //判断是否使用集群 FileUrl 文件服务器地址或者本节点地址
            //String fileUri = StaticValue.ISCLUSTER == 1 ? StaticValue.FILE_SERVER_URL : StaticValue.BASEURL;
            String fileUri = StaticValue.getISCLUSTER() == 1 ? StaticValue.getFileServerUrl() : StaticValue.BASEURL;
            //taskType发送类型   1-EMP界面发送  2-接口发送
            mttask.setTaskType(1);
            //发送文件URL
            mttask.setFileuri(fileUri);
            //内容编码类型
            mttask.setMsgedcodetype(15);
            //内容时效性
            mttask.setValidtm(Integer.parseInt(validtm));
            //是否重发 1-已重发 0-未重发 默认为0
            mttask.setIsRetry(0);
            return mttask;

        } catch (EMPException empex) {
            EmpExecutionContext.error(empex, lgcorpcode, lguseridStr);
            EmpExecutionContext.logRequestUrl(request, "企业富信封装LFMTTASK对象异常！ 后台请求");
            throw empex;
        }
    }


    /**
     * 根据Excel2JsonDto对象拼接模板参数
     *
     * @param excel2JsonDtos Excel2JsonDto对象
     * @param tmsMsg         html组成的字符串
     * @return 拼接好的字符串
     */
    private String combineContent(List<Excel2JsonDto> excel2JsonDtos, String tmsMsg) throws Exception {
        Excel2JsonDto dto = null;
        StringBuilder resultStr = new StringBuilder();
        String[] strings = tmsMsg.split("<div class=\"editor-keyframe J-keyframe( active)?\"");
        for (int i = 1, length = strings.length; i < length; i++) {
            //判断哪一帧对应
            for (Excel2JsonDto jsonDto : excel2JsonDtos) {
                if ((i - 1 + "").equals(jsonDto.getFmno())) {
                    dto = jsonDto;
                }
            }
            String str = strings[i];
            //处理文本信息
            Pattern pattern = Pattern.compile("<div class=\"editor-text J-edit-text\" contenteditable=\"true\">(.*?)</div>");
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                String group = matcher.group(1);
                //替换文本信息
                HashMap<String, String> txtMap = dto.getTxt();
                if (txtMap != null) {
                    Pattern txtPatt = Pattern.compile("(\\{#参数[1-9]?\\d#})");
                    Matcher txtMatcher = txtPatt.matcher(group);
                    Integer index = 1;
                    while (txtMatcher.find()) {
                        group = group.replace(txtMatcher.group(1), URLDecoder.decode(txtMap.get("p" + index++), "utf-8"));
                    }
                    resultStr.append(group).append(",");
                }
            }
            //处理图参或报表信息
            pattern = Pattern.compile("data-type=\"(.*?)\"");
            matcher = pattern.matcher(str);
            if (matcher.find()) {
                String dataType = matcher.group(1);
                if ("image".equals(dataType)) {
                    //图参
                    pattern = Pattern.compile("(>.*?图参.*)</div>");
                    matcher = pattern.matcher(str);
                    if (matcher.find()) {
                        String group = matcher.group(1);
                        //替换图参信息
                        HashMap<String, Object> imgMap = dto.getImg();
                        if (imgMap != null) {
                            Pattern imgPatt = Pattern.compile("(\\{#图参[1-9]?\\d#})");
                            Matcher imgMatcher = imgPatt.matcher(group);
                            Integer index = 1;
                            while (imgMatcher.find()) {
                                group = group.replace(imgMatcher.group(1), URLDecoder.decode((String) imgMap.get("p" + index++), "utf-8"));
                            }
                            resultStr.append(group).append(",");
                        }
                    }
                } else if ("chart".equals(dataType)) {
                    //表标题
                    HashMap<String, String> title = dto.getTitle();
                    if (title != null) {
                        pattern = Pattern.compile("\"chartTitle\":\"(.*?)\"");
                        matcher = pattern.matcher(str);
                        if (matcher.find()) {
                            String chartTitle = matcher.group(1);
                            chartTitle = chartTitle.replaceAll("\\{#标题#}", URLDecoder.decode(title.get("p1"), "utf-8"));
                            resultStr.append(chartTitle).append(",");
                        }
                    }
                    HashMap<String, Object> imgMap = dto.getImg();
                    if (imgMap != null) {
                        //列标题
                        HashMap<String, String> rsTitle = (HashMap<String, String>) imgMap.get("rs");
                        if (rsTitle != null) {
                            for (String rsVal : rsTitle.values()) {
                                resultStr.append(URLDecoder.decode(rsVal, "utf-8")).append(",");
                            }
                        }
                        //行标题
                        HashMap<String, String> csTitle = (HashMap<String, String>) imgMap.get("cs");
                        if (csTitle != null) {
                            for (String csVal : csTitle.values()) {
                                resultStr.append(URLDecoder.decode(csVal, "utf-8")).append(",");
                            }
                        }
                        //数据
                        HashMap<String, String> datas = (HashMap<String, String>) imgMap.get("datas");
                        if (datas != null) {
                            for (String dataVal : datas.values()) {
                                resultStr.append(dataVal).append(",");
                            }
                        }
                    }
                }
            }
        }
        //删掉最后一个","
        if (resultStr.lastIndexOf(",") > -1) {
            return resultStr.deleteCharAt(resultStr.length() - 1).toString();
        }
        return resultStr.toString();
    }

    /**
     * 企业富信重发方法
     *
     * @param taskId 任务Id
     * @return 结果字符串
     */
    public String rmsReSend(Long taskId) throws Exception {
        LfMttask oldLfMttask = null;
        Long newTaskId = 0L;
        try {
            //---1.判断原始任务状态是否是已重发。
            //获取老的LfMttask对象
            oldLfMttask = (new CommonBiz()).getLfMttaskbyTaskId(taskId);
            if (oldLfMttask == null) {
                EmpExecutionContext.error("富信重发失败，根据TaskId查询下行任务表（LfMttask）实体类对象出现异常，找不到对应的lfMttask对象");
                return "reSendFail";
            }
            //获取旧任务重发状态
            Integer isRetry = oldLfMttask.getIsRetry();
            if (isRetry != null && isRetry == 1) {
                EmpExecutionContext.error("富信重发失败，不允许补发重发状态（isRetry）为1的任务");
                return "hasResend";
            }
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

            //---2.生成新的lfmttask任务记录。
            //获取一个新的LfMttask对象
            LfMttask newLfMttask = (LfMttask) BeanUtils.cloneBean(oldLfMttask);
            newLfMttask.setIsRetry(0);

            newTaskId = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);
            //查询任务ID在是否在lf_mttask表已使用,false:存在；true:不存在
            if (!smsSpecialDAO.checkTaskIdNotUse(newTaskId)) {
                EmpExecutionContext.error("富信重发失败，发送获取参数异常，taskId已经被使用:" + taskId
                        + "strlguserid:" + oldLfMttask.getUserId()
                        + "，lgcorpcode:" + oldLfMttask.getCorpCode()
                        + "，errCode：" + IErrorCode.V10016);
                throw new EMPException(IErrorCode.V10016);
            }
            //更新
            newLfMttask.setTaskId(newTaskId);
            newLfMttask.setSendstate(0);

            conditionMap.clear();
            conditionMap.put("taskId", taskId.toString());
            List<LfRmsTaskCtrl> rmsTaskCtrls = empDao.findListByCondition(LfRmsTaskCtrl.class, conditionMap, null);
            if (rmsTaskCtrls == null || rmsTaskCtrls.size() == 0) {
                EmpExecutionContext.error("富信重发失败，根据TaskId查询下行任务控制表（LfRmsTaskCtrl）实体类对象出现异常，找不到对应的LfRmsTaskCtrl对象");
                return "reSendFail";
            }
            LfRmsTaskCtrl lfRmsTaskCtrl = rmsTaskCtrls.get(0);
            //处理新的号码文件
            String newMobileUrl = regenerateMobileUrl(oldLfMttask.getUserId(), newTaskId.toString(), oldLfMttask.getMobileUrl(), lfRmsTaskCtrl.getCurrentCount());

            if (!"downloadFail".equals(newMobileUrl)) {
                newLfMttask.setMobileUrl(newMobileUrl);
            } else {
                return "reSendFail";
            }

            //重发任务的提交号码数就是老任务的有效号码数减去LfRmsTaskCtrl表中记录的已发送数
            Long newSubCount = oldLfMttask.getEffCount() - lfRmsTaskCtrl.getCurrentCount();

            //修改重发任务的任务主题，与原来的任务区分
            String title = oldLfMttask.getTitle();
  /*          if (title == null || "".equals(title) || !title.matches("^(.*?)\\[补发_(\\d+)]$")) {
                title = oldLfMttask.getTitle() + "[补发_01]";
            } else {
                Pattern p = Pattern.compile("^(?:.*?)\\[补发_(\\d+)]$");
                Matcher m = p.matcher(title);
                if (m.find()) {
                    int i = Integer.parseInt(m.group(1));
                    if (++i < 10) {
                        title = title.replaceAll("\\[补发_(\\d+)]$", "[补发_" + "0" + i + "]");
                    } else {
                        title = title.replaceAll("\\[补发_(\\d+)]$", "[补发_" + i + "]");
                    }
                }
            }*/
            newLfMttask.setTitle(title);
            //重发任务的有效数等于提交数
            newLfMttask.setSubCount(newSubCount);
            newLfMttask.setEffCount(newSubCount);
            //重发任务的提交时间
            newLfMttask.setSubmitTime(new Timestamp(System.currentTimeMillis()));
            newLfMttask.setTimerTime(newLfMttask.getSubmitTime());
            //重发任务的批量任务ID
            newLfMttask.setBatchID(newTaskId);
            //预发送条数
            newLfMttask.setIcount(newSubCount.toString());

            //重置sucCount与faiCount
            newLfMttask.setSucCount("0");
            newLfMttask.setFaiCount("0");

            //补发统一设置为实时
            newLfMttask.setTimerStatus(0);

            //此处增加马拉松项目逻辑判断
            if(oldLfMttask.getMsgType() == 99){
                //调用发送接口
                return new ImportTempBiz().sendMarathonRms(newLfMttask);
            }else {
                //设置是否走定时
                if (SEND_RMS_SAME == newLfMttask.getBmtType()) {
                    if (newLfMttask.getEffCount() > 100000) {
                        //因网关没有批量文件接口，故相同内容超过10万，走定时
                        newLfMttask.setTimerStatus(2);
                        newLfMttask.setTimerTime(new Timestamp(System.currentTimeMillis() + (90 * 1000)));
                    }
                } else if (SEND_RMS_DIFF == newLfMttask.getBmtType()) {
                    if (newLfMttask.getEffCount() > 1000) {
                        //因网关没有批量文件接口，故不同内容超过1千，走定时
                        newLfMttask.setTimerStatus(2);
                        newLfMttask.setTimerTime(new Timestamp(System.currentTimeMillis() + (90 * 1000)));
                    }
                }
                //3.调用发送方法
                return addRmsLfMttaskSend(newLfMttask);
            }
            //发送失败
            /*if(!result.contains("sendSuccess") || !"timerSuccess".equals(result)){
                //查表判断新任务是否已经入库
                LfMttask newMttask = (new CommonBiz()).getLfMttaskbyTaskId(newTaskId);
                if(newMttask == null){
                    return "reSendFail";
                }else{
                    //发送失败更新发送状态为失败
                	newMttask.setSendstate(2);
                	boolean updateResult = empDao.update(newMttask);
                    if(!updateResult){
                        EmpExecutionContext.error("富信重发失败，更新下行任务表（LfMttask）实体类对象出现异常，无法更新Sendstate字段");
                        return "reSendFail";
                    }
                    oldLfMttask.setIsRetry(1);
                    boolean isUpdate = empDao.update(oldLfMttask);
                    if(!isUpdate){
                        EmpExecutionContext.error("富信重发失败，更新下行任务表（LfMttask）实体类对象出现异常，无法更新IsReply字段");
                        return "reSendFail";
                    }
                }
            }else {
                //发送成功
                //更新老任务状态为已重发
                oldLfMttask.setIsRetry(1);
                boolean isUpdate = empDao.update(oldLfMttask);
                if(!isUpdate){
                    EmpExecutionContext.error("富信重发失败，更新下行任务表（LfMttask）实体类对象出现异常，无法更新IsReply字段");
                    return "reSendFail";
                }
            }*/
        } catch (EMPException empex) {
            throw empex;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业富信重发失败，重发出现异常");
            return "reSendFail";
        } finally {
            //查表判断新任务是否已经入库
            LfMttask newMttask = (new CommonBiz()).getLfMttaskbyTaskId(newTaskId);
            if (newMttask != null && oldLfMttask != null) {
                oldLfMttask.setIsRetry(1);
                boolean isUpdate = empDao.update(oldLfMttask);
                if (!isUpdate) {
                    EmpExecutionContext.error("富信重发失败，更新下行任务表（LfMttask）实体类对象出现异常，无法更新IsReply字段");
                }
            }
        }
    }

    /**
     * 撤销定时任务方法
     *
     * @param lfMttask 任务实体对象
     * @return 返回值 已撤销 hasCanceled
     * 撤销成功 cancelSuccess
     * 撤销失败 cancelFail
     */
    public String cancelRmsTimerTask(LfMttask lfMttask) throws Exception {
        String userName = "";
        LfSysuser lfSysuser = baseBiz.getById(LfSysuser.class, lfMttask.getUserId());
        //操作员名称
        if (lfSysuser != null) {
            userName = lfSysuser.getUserName();
        }
        String returnStr = "";
        if (lfMttask.getSubState() == 3) {
            returnStr = "hasCanceled";
            return returnStr;
        } else {
            //更改状态为已撤销
            lfMttask.setSubState(3);

            boolean flag;

            flag = this.empDao.update(lfMttask);
            if (flag) {
                cancelLfTimer(lfMttask.getTaskId());
                returnStr = "cancelSuccess";
            } else {
                returnStr = "cancelFail";
            }
            //撤销成功则需要回收余额(SP账号余额+运营商余额)
            if ("cancelSuccess".equals(returnStr)) {
                String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

                String recyleResult = rmsBalanceLogBiz.huishouFeeRms(Integer.parseInt(lfMttask.getEffCount() + ""), lfMttask.getSpUser(), 1);
                if ("true".equals(recyleResult)) {
                    String opLog = "企业富信定时任务撤销操作,运营商余额回收成功，回收sp账号：" + lfMttask.getSpUser() + ",回收金额：" + lfMttask + ",回收时间：" + now;
                    EmpExecutionContext.info("企业富信回收余额", lfMttask.getCorpCode(), lfMttask.getUserId().toString(), userName, opLog, StaticValue.OTHER);
                } else if ("false".equals(recyleResult) || "error".equals(recyleResult)) {
                    EmpExecutionContext.error("企业富信定时任务撤销操作，企业富信回收余额异常，根据sp账号更新Lf_SPFEE表的RMS_BALANCE字段出现错误。");
                }

                String recyleRes = rmsBalanceLogBiz.recycleSpFee(lfMttask.getEffCount(), lfMttask.getSpUser());
                //false 入库失败 true 入库成功 error 入库出现错误
                if ("true".equals(recyleRes)) {
                    String opLog = "企业富信定时任务撤销操作,SP账号余额回收成功，回收sp账号：" + lfMttask.getSpUser() + ",回收金额：" + lfMttask.getEffCount() + ",回收时间：" + now;
                    EmpExecutionContext.info("企业富信回收SP账号余额成功", lfMttask.getCorpCode(), lfMttask.getUserId().toString(), userName, opLog, StaticValue.OTHER);
                } else if ("false".equals(recyleRes) || "error".equals(recyleRes)) {
                    EmpExecutionContext.error("企业富信定时任务撤销操作，回收SP账号余额异常，根据sp账号更新USERFEE表的SENDNUM与SENDEDNUM字段出现错误。");
                }
            }

            return returnStr;
        }
    }

    /**
     * 判断文件是否超过指定大小
     *
     * @param fileItem fileItem
     * @return 布尔值
     */
    private boolean isZipOverSize(FileItem fileItem) {
        try {
            String fileCurName = fileItem.getName();
            String fileType = fileCurName.substring(fileCurName.lastIndexOf("."));
            boolean flag = fileType.matches("\\.csv|\\.zip|\\.rar|\\.txt|\\.xlxs|\\.xls|\\.et");
            return flag && fileItem.getSize() > (100L * 1024 * 1024);
        } catch (Exception var4) {
            EmpExecutionContext.error(var4, "zip文件超出大小。错误码：V10014");
            return false;
        }
    }

    /**
     * 根据原来的号码文件以及控制表中记录的位置重新生成号码文件
     *
     * @param lguserid     操作员Id
     * @param taskId       任务Id
     * @param mobileUrl    原来的号码文件url
     * @param currentCount 当前读取位置
     * @return 生成的新的文件路径
     */
    private String regenerateMobileUrl(Long lguserid, String taskId, String mobileUrl, Long currentCount) throws Exception {
        if (mobileUrl == null || currentCount == null) {
            EmpExecutionContext.error("富信重发失败，根据原来的号码文件以及控制表中记录的位置重新生成号码文件失败，找不到对应的原来的号码文件url或者当前读取位置");
            return "downloadFail";
        }
        //下载号码文件
        try {
            if (!downloadFromMobileUrl(mobileUrl)) {
                return "downloadFail";
            }
        } catch (Exception e) {
            EmpExecutionContext.error("富信重发失败，根据原来的号码文件url下载号码文件异常");
            throw e;
        }
        //获取旧号码文件绝对路径
        String oldMobileFile = txtFileUtil.getPhysicsUrl(mobileUrl);
        //存储到新文件中
        //设置文件名参数
        String[] fileNameparam = {taskId};
        //获取富信号码文件路径
        String[] url = txtFileUtil.getSaveRmsMobileFileUrl(lguserid, fileNameparam);
        if (url == null || url.length < 5) {
            EmpExecutionContext.error("根据原来的号码文件以及控制表中记录的位置重新生成号码文件，获取发送文件路径失败。userId：" + lguserid + "，errCode:" + IErrorCode.V10013);
            throw new EMPException(IErrorCode.V10013);
        }
        //输入流
        BufferedReader br = null;
        //输出流
        PrintWriter pw = null;
        //新文件的绝对路径
        String newMobileFile = txtFileUtil.getPhysicsUrl(url[1]);
        //记录行数
        Long lineCount = 0L;
        String tempString = "";
        try {
            File temFile = new File(newMobileFile);
            //文件不存在则创建 若已经存在则抛出异常
            if (!temFile.createNewFile()) {
                EmpExecutionContext.error("根据原来的号码文件以及控制表中记录的位置重新生成号码文件，获取号码文件路径失败，文件路径已存在，文件路径：" + newMobileFile + "，userid:" + lguserid + "，errCode：" + IErrorCode.V10013);
                throw new EMPException(IErrorCode.V10013);
            }

            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(temFile, true), "UTF-8"), true);

            br = new BufferedReader(new FileReader(oldMobileFile));
            //循环读取
            while ((tempString = br.readLine()) != null) {
                lineCount++;
                //小于指定行数直接跳过
                if (lineCount <= currentCount) {
                    continue;
                }
                //写到新文件中
                pw.println(tempString);
            }
            //此处传回的是相对路径，而不是绝对路径
            return url[1];
        } catch (Exception e) {
            throw e;
        } finally {
            if (pw != null) {
                pw.close();
            }

            if (br != null) {
                try {
                    br.close();
                } catch (IOException var19) {
                    EmpExecutionContext.error(var19, "关闭流异常！fileName:" + newMobileFile);
//                    throw new EMPException("B20003", var19);
                }
            }
        }
    }

    /**
     * 根据TaskId更改LfTimer表中数据
     *
     * @param taskId 任务Id
     * @return 布尔值 true 撤销成功或不存在 false 撤销失败
     */
    private boolean cancelLfTimer(Long taskId) {
        TaskManagerBiz taskManagerBiz = new TaskManagerBiz();
        List<LfTimer> lfTimerList = taskManagerBiz.getTaskByExpression(String.valueOf(taskId));
        LfTimer lfTimer = null;
        if (lfTimerList != null && lfTimerList.size() > 0) {
            lfTimer = lfTimerList.get(0);
            return taskManagerBiz.stopTask(lfTimer.getTimerTaskId());
        } else {
            return true;
        }
    }

    /**
     * 根据号码文件url下载号码文件至本地
     *
     * @param mobileUrl 号码文件url
     * @return 是否下载到本地 下载成功 true 失败 false
     */
    private boolean downloadFromMobileUrl(String mobileUrl) throws Exception {
        try {
            if ("".equals(mobileUrl) || mobileUrl == null) {
                EmpExecutionContext.error("企业富信发送，获取手机号码Url地址异常。手机号码Url地址为空。");
                return false;
            }
            //检测本地是否存在,不存在则在集群上检测
            if (!txtFileUtil.checkFile(mobileUrl)) {
                String checkFile = commBiz.checkServerFile(mobileUrl);
                if (checkFile == null) {
                    EmpExecutionContext.error("企业富信发送，获取手机号码Url地址异常。手机号码文件url不存在本地或集群。");
                    return false;
                } else {
                    //从文件服务器上下载
                    String fileStr = commBiz.downloadFileFromFileCenter(mobileUrl);
                    if ("error".equals(fileStr)) {
                        EmpExecutionContext.error("企业富信发送，获取手机号码文件异常。无法从从文件服务器下载手机号码文件。");
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error("企业富信发送，下载手机号码文件至本地异常。");
            throw e;
        }
        return true;
    }

    private void writePhoneFile2txt(String fileFlag, PreviewParams preParams, StringBuffer contentSb) throws Exception {
        FileOutputStream fos = null;
        try {
            if ("badFile".equals(fileFlag)) {
                //非法号码文件输出流
                fos = new FileOutputStream(preParams.getPhoneFilePath()[2], true);
            } else if ("effFile".equals(fileFlag)) {
                //有效号码文件输出流
                fos = new FileOutputStream(preParams.getPhoneFilePath()[0], true);
            }
            //写入非法号码写文件
            txtFileUtil.repeatWriteToTxtFile(fos, contentSb.toString());
            contentSb.setLength(0);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "企业富信-不同内容发送预览，将文本文件流写入号码文件异常，写入内容：" + contentSb);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "企业富信-不同内容发送预览，解析文本文件流关闭无效号码文件输入流异常。");
                }
            }
        }
    }

    /**
     * 校验行列标题，行列值
     *
     * @param rmsContent
     * @param list
     * @throws EMPException
     */
    private void checkJsonDtoList(String rmsContent, ArrayList<Excel2JsonDto> list) throws EMPException {
        //短信内容部分（不包括手机号）
        String[] contentArray = rmsContent.split("\\[#MW#]");
        //记录处理到那一列
        Integer index = 0;
        //实际行标题
        Integer realRowTitle = 0;
        //实际列标题
        Integer realColTitle = 0;
        //起始列标题数
        Integer origColTitle = 0;
        //起始行标题数
        Integer origRowTitle = 0;

        for (Excel2JsonDto dto : list) {
            //重置realRowTitle与realColTitle
            realColTitle = 0;
            realRowTitle = 0;
            //报表标题
            Integer reportTitleNum = dto.getHasReportTitle() ? 1 : 0;
            Integer txtParamCount = dto.getTxtParamCount() == null ? 0 : dto.getTxtParamCount();
            Integer pcts = dto.getPcts() == null ? 0 : dto.getPcts();
            index += txtParamCount + pcts + reportTitleNum;
            //按顺序处理
            if (dto.getHasReport()) {
                //数值动态---行列值已经确定，不允许出现空值
                if (dto.getDynamicType() == 2) {
                    for (int i = 1, l = dto.getRscnt(); i <= l; i++) {
                        for (int j = 1, k = dto.getCscnt(); j <= k; j++) {
                            if ("[#NULL#]".equals(contentArray[index++])) {
                                throw new EMPException("格式非法(数值动态报表的行列值不允许为空)：");
                            }
                        }
                    }
                }
                //全值动态
                if (dto.getDynamicType() == 3) {
                    Boolean flag = false;
                    //如果为饼图则没有列标题
                    if (dto.getChartType() != 3) {
                        origColTitle = dto.getCscnt();
                        origRowTitle = dto.getRscnt();
                        //列标题
                        for (int i = 1, p = origColTitle; i <= p; i++) {
                            if ("[#NULL#]".equals(contentArray[index++])) {
                                flag = true;
                                continue;
                            }
                            if (flag) {
                                throw new EMPException("格式非法(错误的列标题)：");
                            }
                            realColTitle++;
                        }
                    } else {
                        realColTitle = 1;
                    }
                    //行标题---不允许出现空值后还出现具体数值
                    Boolean flag1 = false;
                    for (int i = 1, o = dto.getRscnt(); i <= o; i++) {
                        if ("[#NULL#]".equals(contentArray[index++])) {
                            flag1 = true;
                            continue;
                        }
                        if (flag1) {
                            throw new EMPException("格式非法(错误的行标题)：");
                        }
                        realRowTitle++;
                    }
                    //数据---指定的行列必须要有值
                    for (int j = 1, k = realColTitle; j <= k; j++) {
                        for (int i = 1; i <= realRowTitle; i++) {
                            if ("[#NULL#]".equals(contentArray[index++])) {
                                throw new EMPException("格式非法(指定行列值为空)：");
                            }
                        }
                        if (realRowTitle < origRowTitle) {
                            index += (origRowTitle - realRowTitle);
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理rmsContent 去掉空值，重新计算参数个数
     *
     * @param rmsContent
     * @return
     */
    private String ridOfNullAndGetNewParamNum(String rmsContent) {
        StringBuilder resultStr = new StringBuilder();
        Integer paramCount = 0;
        for (String str : rmsContent.split("\\[#MW#]")) {
            if (!"[#NULL#]".equals(str)) {
                resultStr.append(str).append(",");
                paramCount++;
            }
        }
        if (resultStr.length() > 0) {
            resultStr = resultStr.deleteCharAt(resultStr.length() - 1);
        }
        return resultStr.append("&PARAMCOUNT&").append(paramCount).toString();
    }

    public boolean updateReadPos(Long taskId, Long currentCount) throws Exception {
        boolean retFlag = false;
        LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
        condition.put("taskId", String.valueOf(taskId));
        try {
            List<LfRmsTaskCtrl> list = empDao.findListByCondition(LfRmsTaskCtrl.class, condition, null);
            LfRmsTaskCtrl lfrms = null;
            if (null != list && list.size() > 0) {
                lfrms = list.get(0);
                lfrms.setCurrentCount(currentCount);
                lfrms.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                retFlag = empDao.update(lfrms);
            } else {
                lfrms = new LfRmsTaskCtrl();
                lfrms.setTaskId(taskId);
                lfrms.setCurrentCount(currentCount);
                lfrms.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                retFlag = empDao.save(lfrms);
            }
            retFlag = true;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "更新富信任务控制表异常");
            throw e;
        }
        return retFlag;
    }

    /**
     * 读取每一行数据，完成Excel2JsonDto对象的组装
     *
     * @param rmsContent
     * @param list
     * @return
     * @throws Exception
     */
    private List<Excel2JsonDto> getExcel2JsonDtoList(String rmsContent, ArrayList<Excel2JsonDto> list) throws Exception {
        List<Excel2JsonDto> dtoList = new ArrayList<Excel2JsonDto>();
        //短信内容部分（不包括手机号）
        String[] contentArray = rmsContent.split("\\[#MW#]");

        StringBuilder datas = new StringBuilder();

        //记录处理到那一列
        Integer index = 0;
        Integer txtParamCount;
        Integer imgParamCount = 0;
        Integer dataNum = 0;

        for (Excel2JsonDto dto : list) {
            //用于重置Index
            if (dto.getCscnt() != null && dto.getRscnt() != null) {
                dataNum = dto.getCscnt() * dto.getRscnt();
            }

            //复制一个对象，不污染原来的对象
            Excel2JsonDto cloneDto = (Excel2JsonDto) BeanUtils.cloneBean(dto);
            //原始行标题数
            Integer oriRowTitleNum = cloneDto.getRscnt();
            //按顺序处理
            if (cloneDto.getHasTxt()) {
                //文本参数个数
                txtParamCount = cloneDto.getTxtParamCount();
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 1; i <= txtParamCount; i++) {
                    map.put("p" + i, encode(contentArray[index++], "utf-8"));
                }
                cloneDto.setTxt(map);
            }
            if (cloneDto.getHasImg()) {
                //图文参数个数
                imgParamCount = cloneDto.getPcts();
                LinkedHashMap<String, Object> imgMap = new LinkedHashMap<String, Object>();
                imgMap.put("pcts", imgParamCount.toString());
                for (int i = 1; i <= imgParamCount; i++) {
                    imgMap.put("p" + i, encode(contentArray[index++], "utf-8"));
                }
                cloneDto.setImg(imgMap);
            }
            if (cloneDto.getHasReport()) {
                LinkedHashMap<String, Object> imgMap = new LinkedHashMap<String, Object>();
                //标题
                //判断有没有报表标题
                if (cloneDto.getHasReportTitle()) {
                    HashMap<String, String> titleMap = new HashMap<String, String>();
                    titleMap.put("p1", encode(contentArray[index++], "utf-8"));
                    cloneDto.setTitle(titleMap);
                }
                //如果为数值动态则不需要写入行标题，列标题
                if (cloneDto.getDynamicType() == 3) {
                    //全值动态

                    //如果为饼图则没有列标题
                    if (cloneDto.getChartType() != 3) {
                        //列标题
                        HashMap<String, String> colTitleMap = new HashMap<String, String>();
                        //实际列标题
                        Integer realColTitle = 0;
                        for (int i = 1, p = cloneDto.getCscnt(); i <= p; i++) {
                            if ("[#NULL#]".equals(contentArray[index])) {
                                index += (p - realColTitle);
                                break;
                            }
                            realColTitle++;
                            colTitleMap.put("c" + i, encode(contentArray[index++], "utf-8"));
                        }
                        cloneDto.setCscnt(realColTitle);
                        imgMap.put("cs", colTitleMap);
                    }

                    //实际行标题
                    Integer realRowTitle = 0;
                    //行标题
                    HashMap<String, String> rowTitleMap = new HashMap<String, String>();
                    for (int i = 1, o = cloneDto.getRscnt(); i <= o; i++) {
                        if ("[#NULL#]".equals(contentArray[index])) {
                            index += (o - realRowTitle);
                            break;
                        }
                        realRowTitle++;
                        rowTitleMap.put("r" + i, encode(contentArray[index++], "utf-8"));
                    }
                    cloneDto.setRscnt(realRowTitle);
                    imgMap.put("rs", rowTitleMap);
                }
                //如果行列标题都等于null说明为静态图表但是带标题参数
                if (cloneDto.getRscnt() != null && cloneDto.getCscnt() != null) {
                    //数据
                    List<String> listStr = new ArrayList<String>();
                    //记录起始位置
                    final Integer oriPosition = index;
                    HashMap<String, String> datasMap = new HashMap<String, String>();

                    for (int i = 1, m = cloneDto.getRscnt(); i <= m; i++) {
                        Integer newOriPosition = oriPosition + i - 1;
                        for (int j = 1, k = cloneDto.getCscnt(); j <= k; j++) {
                            //获取每行的位置
                            datas.append(contentArray[newOriPosition]).append(",");
                            newOriPosition += oriRowTitleNum;
                        }
                        datas.deleteCharAt(datas.lastIndexOf(","));
                        listStr.add(datas.toString());
                        datas.setLength(0);
                    }
                    index += dataNum;
                    for (int i = 0, j = listStr.size(); i < j; i++) {
                        datasMap.put("r" + (i + 1), listStr.get(i));
                    }

                    //如果是饼图则默认列标题个数为0
                    imgMap.put("rscnt", cloneDto.getRscnt().toString());
                    imgMap.put("cscnt", cloneDto.getChartType() == 3 ? "0" : cloneDto.getCscnt().toString());
                    imgMap.put("datas", datasMap);
                    cloneDto.setImg(imgMap);
                }
            }
            //重置Index
            dtoList.add(cloneDto);
        }
        return dtoList;
    }


    /**
     * 富信定时发送
     *
     * @param taskId 任务Id
     * @return 发送结果 {sendFail:发送失败，sendSuccess&TaskId：发送成功}
     */
    public String sendTimerRms(Long taskId) throws Exception {
        String returnStr = "sendError";
        LfMttask lfMttask = (new CommonBiz()).getLfMttaskbyTaskId(taskId);
        if (lfMttask == null) {
            EmpExecutionContext.error("富信发送失败，根据TaskId查询下行任务记录表实体类对象出现异常，找不到对应的lfMttask对象");
            return returnStr;
        }
        //记录日志
        String opStr = lfMttask.getBmtType() == SEND_RMS_SAME ? "企业富信相同内容发送" : "企业富信不同内容发送";
        LfSysuser lfSysuser = baseBiz.getById(LfSysuser.class, lfMttask.getUserId());
        //操作员名称
        String userName = " ";
        if (lfSysuser != null) {
            userName = lfSysuser.getUserName();
        }
//        //开启集群环境则上传文件至文件服务器
//        //如果连接不上，需要重试三次
//        if (StaticValue.ISCLUSTER == 1) {
//            long beforeUpload = System.currentTimeMillis();
//            boolean isUploadSucceed = commBiz.upFileToFileServer(lfMttask.getMobileUrl());
//
//            if (isUploadSucceed) {
//                String opContent = "后付费-实时发送，上传本地号码文件至文件服务器成功,文件相对路径：" + lfMttask.getMobileUrl() + ",耗时：" + (System.currentTimeMillis() - beforeUpload) + "毫秒";
//                EmpExecutionContext.info(opStr, lfMttask.getCorpCode(), lfMttask.getUserId().toString(), userName, opContent, "OTHER");
//            } else {
//                String opContent = "后付费-实时发送，上传本地号码文件至文件服务器失败";
//                EmpExecutionContext.error(opStr + "," + opContent + ",ErrorCode:B20023");
//            }
//        }
        //如果为11则为相同内容发送
        if (SEND_RMS_SAME == lfMttask.getBmtType()) {
            return parseMobileUrl2Send(lfMttask);
        }
        //如果为12则为不同内容发送
        if (SEND_RMS_DIFF == lfMttask.getBmtType()) {
            return sendDiffRms(lfMttask);
        }
        return returnStr;
    }


}
