package com.montnets.emp.samesms.biz;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.PreviewParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sms.LfBigFile;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BigFileSplitBiz extends SuperBiz{

    private WgMsgConfigBiz msgConfigBiz = new WgMsgConfigBiz();
    private CommonBiz comBiz = new CommonBiz();
    private PhoneUtil phoneUtil = new PhoneUtil();
    private BlackListAtom blackBiz = new BlackListAtom();
    // 写文件时候要的换行符
    String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);
    CommonVariables cv = new CommonVariables();

    private Object invoke;
    public void splitBigFile(LfBigFile bigFile,
                             List<BufferedReader> readerList, PreviewParams params,String langName) {
        // 运营商有效号码数
        int[] oprValidPhone = params.getOprValidPhone();
        // 运营商标识。0:移动号码 ;1:联通号码;2:电信号码;3:国际号码
        int index = 0;
        // 号码返回状态
        int resultStatus = 0;
        // 解析号码文件
        String mobile;
        // 每批次的有效号码数，在该批号码写入数据库后重置为默认值
        int perEffCount = 0;
        //每个文件有效号码
        int perFileEffCount = 0;
        //有效号码文件数
        int effFileCount = 0;
        // 每批次的无效号码数，在该批号码写入数据库后重置为默认值
        int perBadCount = 0;
        BufferedReader reader = null;

        TxtFileUtil txtFileUtil = new TxtFileUtil();
        // 有效号码文件
        File perEffFile = null;
        // 无效号码文件
        File perBadFile = null;
        // 合法号码文件流
        FileOutputStream perEffOS = null;
        // 无效号码文件流
        FileOutputStream perBadOS = null;
        String corpCode = bigFile.getCorpCode();
        Long lguserid = bigFile.getUserId();
        // 获取运营商号码段
        String[] haoduan = msgConfigBiz.getHaoduan();
        //String language = bigFile.getRemark();
        String language = langName;
        try {
            // 有效号码
            StringBuilder contentSb = new StringBuilder(64);
            // 无效号码
            StringBuilder badContentSb = new StringBuilder(64);
            EmpExecutionContext.info("超大文件拆分，文件ID:"+bigFile.getId()+"，开始处理");
            for (int r = 0; r < readerList.size(); r++) {
                // 如果上传号码大于设置的最大值就不允许发送
                if (params.getSubCount() > 50000000) {
                    EmpExecutionContext.error(MessageUtils.extractMessage("common",
                            "common_sms_code1",
                            language)
                            + StaticValue.MAX_PHONE_NUM
                            + "，corpCode:"
                            + corpCode
                            + "，userid："
                            + lguserid);
                    throw new EMPException(IErrorCode.A40000);
                }
                reader = readerList.get(r);
                while ((mobile = reader.readLine()) != null) {
                    params.setSubCount(params.getSubCount() + 1);
                    mobile = mobile.trim();
                    // 去掉号码中+86前缀
                    mobile = StringUtils.parseMobile(mobile);
                    // 检查号码合法性和号段
                    if ((index = phoneUtil.getPhoneType(mobile, haoduan)) < 0) {
                        badContentSb.append(MessageUtils.extractMessage("common",
                                "common_sms_code2",
                                language))
                                .append(mobile)
                                .append(line);
                        params.setBadModeCount(params.getBadModeCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                    } else if ((resultStatus = phoneUtil.checkRepeat(mobile, params.getValidPhone())) != 0) {
                        // 1为重复号码
                        if (resultStatus == 1) {
                            badContentSb.append(MessageUtils.extractMessage("common",
                                    "common_sms_code3",
                                    language))
                                    .append(mobile)
                                    .append(line);
                            params.setRepeatCount(params.getRepeatCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                        }
                        // -1为非法号码
                        else {
                            badContentSb.append(MessageUtils.extractMessage("common",
                                    "common_sms_code2",
                                    language))
                                    .append(mobile)
                                    .append(line);
                            params.setBadModeCount(params.getBadModeCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                        }
                    } else if (blackBiz.checkBlackList(corpCode, mobile, bigFile.getBusCode())) {
                        // 检查是否是黑名单
                        badContentSb.append(MessageUtils.extractMessage("common",
                                "common_sms_code4",
                                language))
                                .append(mobile)
                                .append(line);
                        params.setBlackCount(params.getBlackCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                    } else {
                        perEffCount++;
                        perFileEffCount++;

                        // 有效号码
                        contentSb.append(mobile).append(line);
                        params.setEffCount(params.getEffCount() + 1);
                        // 预览10个号码
                        if (params.getEffCount() < 11) {
//                            if (mobile != null
//                                && !"".equals(mobile)
//                                && params.getIshidephone() == 0) {
//                                mobile = cv.replacePhoneNumber(mobile);
//                            }
                            params.setPreviewPhone(params.getPreviewPhone() + mobile  + StaticValue.MSG_SPLIT_CHAR+index+";");
                        }
                        // 累加运营商有效号码数(区分运营商)
                        oprValidPhone[index] += 1;
                    }

                    // 一千条存贮一次
                    if (perEffCount >= StaticValue.PER_PHONE_NUM) {
                        if (params.getSubCount() > 50000000) {
                            EmpExecutionContext.error(MessageUtils.extractMessage("common",
                                    "common_sms_code1",
                                    language)
                                    + StaticValue.MAX_PHONE_NUM
                                    + "，corpCode:"
                                    + corpCode
                                    + "，userid："
                                    + lguserid);
                            throw new EMPException(IErrorCode.A40000);
                        }
                        if (perEffFile == null) {
                            effFileCount++;
                            // 有效号码文件
                            perEffFile = new File(params.getPhoneFilePath()[0].replace(".txt", "_"+effFileCount+".txt"));
                            bigFile.setEffNum(bigFile.getEffNum()+1);
                            setFileUrl(bigFile,params.getPhoneFilePath()[1].replace(".txt", "_"+effFileCount+".txt"),effFileCount);
                            // 判断文件是否存在，不存在就新建一个
                            if (!perEffFile.exists()) {
                                perEffFile.createNewFile();
                            }
                            EmpExecutionContext.info("超大文件拆分，文件ID"+bigFile.getId()+"，开始生成第"+effFileCount+"个文件");
                        }
                        if (perEffOS == null) {
                            // 合法号码文件输出流
                            perEffOS = new FileOutputStream(params.getPhoneFilePath()[0].replace(".txt", "_"+effFileCount+".txt"), true);
                        }
                        // 写入有效号码文件
                        // txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0],
                        // contentSb.toString());
                        //如果该有效文件号码数超过1500万，开启下一个文件流
                        if (perFileEffCount> StaticValue.MAX_PHONE_NUM) {

                            effFileCount++;
                            bigFile.setEffNum(bigFile.getEffNum()+1);
                            setFileUrl(bigFile,params.getPhoneFilePath()[1].replace(".txt", "_"+effFileCount+".txt"),effFileCount);
                            //关闭旧的流
                            if (perEffOS != null) {
                                perEffOS.close();
                            }
                            //开启新的流
                            perEffFile = new File(params.getPhoneFilePath()[0].replace(".txt", "_"+effFileCount+".txt"));
                            // 判断文件是否存在，不存在就新建一个
                            if (!perEffFile.exists()) {
                                perEffFile.createNewFile();
                            }
                            perEffOS = new FileOutputStream(params.getPhoneFilePath()[0].replace(".txt", "_"+effFileCount+".txt"), true);
                            perFileEffCount = 0;
                        }
                        EmpExecutionContext.info("超大文件写入到第"+params.getEffCount()+"个号码");
                        txtFileUtil.repeatWriteToTxtFile(perEffOS, contentSb.toString());
                        contentSb.setLength(0);
                        perEffCount = 0;


                    }
//                    if(params.getEffCount()>StaticValue.MAX_PHONE_NUM && params.getEffCount()%StaticValue.MAX_PHONE_NUM == 1){
//                        bigFile.setEffNum(bigFile.getEffNum()+1);
//                        setFileUrl(bigFile,params.getPhoneFilePath()[1].replace(".txt", "_"+effFileCount+".txt"),effFileCount);
//                    }
                    if (perBadCount >= StaticValue.PER_PHONE_NUM) {
                        if (perBadFile == null) {
                            // 非法号码文件
                            perBadFile = new File(params.getPhoneFilePath()[2]);
                            // 判断文件是否存在，不存在就新建一个
                            if (!perBadFile.exists()) {
                                perBadFile.createNewFile();
                            }
                        }
                        if (perBadOS == null) {
                            // 非法号码文件输出流
                            perBadOS = new FileOutputStream(params.getPhoneFilePath()[2], true);
                        }
                        // 写入非法号码写文件
                        // txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[2],
                        // badContentSb.toString());
                        txtFileUtil.repeatWriteToTxtFile(perBadOS, badContentSb.toString());
                        badContentSb.setLength(0);
                        perBadCount = 0;
                    }
                }
                reader.close();
            }
            // 设置各运营商有效号码数
            params.setOprValidPhone(oprValidPhone);

            if (contentSb.length() > 0) {
                if (params.getSubCount() > 50000000) {
                    EmpExecutionContext.error(MessageUtils.extractMessage("common",
                            "common_sms_code1",
                            language)
                            + StaticValue.MAX_PHONE_NUM
                            + "，corpCode:"
                            + corpCode
                            + "，userid："
                            + lguserid);
                    throw new EMPException(IErrorCode.A40000);
                }
                if (perEffFile == null) {
                    effFileCount++;
                    // 有效号码文件
                    perEffFile = new File(params.getPhoneFilePath()[0].replace(".txt", "_"+effFileCount+".txt"));
                    bigFile.setEffNum(bigFile.getEffNum()+1);
                    setFileUrl(bigFile,params.getPhoneFilePath()[1].replace(".txt", "_"+effFileCount+".txt"),effFileCount);
                    // 判断文件是否存在，不存在就新建一个
                    if (!perEffFile.exists()) {
                        perEffFile.createNewFile();
                    }
                    EmpExecutionContext.info("超大文件拆分，文件ID"+bigFile.getId()+"，开始生成第"+effFileCount+"个文件");
                }
                if (perEffOS == null) {
                    // 合法号码文件输出流
                    perEffOS = new FileOutputStream(params.getPhoneFilePath()[0].replace(".txt", "_"+effFileCount+".txt"), true);
                }
                // 写入有效号码文件
                // txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0],
                // contentSb.toString());
                //如果该有效文件号码数超过1500万，开启下一个文件流
                if (perFileEffCount> StaticValue.MAX_PHONE_NUM) {

                    effFileCount++;
                    bigFile.setEffNum(bigFile.getEffNum()+1);
                    setFileUrl(bigFile,params.getPhoneFilePath()[1].replace(".txt", "_"+effFileCount+".txt"),effFileCount);
                    //关闭旧的流
                    if (perEffOS != null) {
                        perEffOS.close();
                    }
                    //开启新的流
                    perEffFile = new File(params.getPhoneFilePath()[0].replace(".txt", "_"+effFileCount+".txt"));
                    // 判断文件是否存在，不存在就新建一个
                    if (!perEffFile.exists()) {
                        perEffFile.createNewFile();
                    }
                    perEffOS = new FileOutputStream(params.getPhoneFilePath()[0].replace(".txt", "_"+effFileCount+".txt"), true);
                    perFileEffCount = 0;
                }
                EmpExecutionContext.info("超大文件写入到第"+params.getEffCount()+"个号码");
                txtFileUtil.repeatWriteToTxtFile(perEffOS, contentSb.toString());
                contentSb.setLength(0);
                perEffCount = 0;


//                if(params.getEffCount()>StaticValue.MAX_PHONE_NUM && params.getEffCount()%StaticValue.MAX_PHONE_NUM == 1){
//                    bigFile.setEffNum(bigFile.getEffNum()+1);
//                    setFileUrl(bigFile,params.getPhoneFilePath()[1].replace(".txt", "_"+effFileCount+".txt"),effFileCount);
//                }
            }

            if (badContentSb.length() > 0) {
                // 剩余的非法号码写文件
                if (perBadFile == null) {
                    // 非法号码文件
                    perBadFile = new File(params.getPhoneFilePath()[2]);
                    // 判断文件是否存在，不存在就新建一个
                    if (!perBadFile.exists()) {
                        perBadFile.createNewFile();
                    }
                }
                if (perBadOS == null) {
                    // 非法号码文件输出流
                    perBadOS = new FileOutputStream(params.getPhoneFilePath()[2], true);
                }
                // 写入非法号码写文件
                // txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[2],
                // badContentSb.toString());
                txtFileUtil.repeatWriteToTxtFile(perBadOS, badContentSb.toString());
                badContentSb.setLength(0);
            }
            if (params.getSubCount() > 50000000) {
                EmpExecutionContext.error(MessageUtils.extractMessage("common",
                        "common_sms_code1",
                        language)
                        + StaticValue.MAX_PHONE_NUM
                        + "，corpCode:"
                        + corpCode
                        + "，userid："
                        + lguserid);
                throw new EMPException(IErrorCode.A40000);
            }
            //取消500万下限
//            if (params.getEffCount() < 5000000) {
//                EmpExecutionContext.error("超大文件拆分，有效号码小于500万"
//
//                                          + "，corpCode:"
//                                          + corpCode
//                                          + "，userid："
//                                          + lguserid);
//                throw new EMPException("有效号码小于500万");
//            }
            //补齐超大文件相关参数
            bigFile.setSubCount(Long.valueOf(params.getSubCount()));
            bigFile.setEffCount(Long.valueOf(params.getEffCount()));
            bigFile.setBlaCount(Long.valueOf(params.getBlackCount()));
            bigFile.setRepCount(Long.valueOf(params.getRepeatCount()));
            bigFile.setErrCount(Long.valueOf(params.getBadModeCount()));
            bigFile.setBadUrl(params.getPhoneFilePath()[5]);
            bigFile.setViewUrl(params.getPreviewPhone());
            String op = "";
            op = Arrays.toString(params.getOprValidPhone()).replace("[", "").replace("]", "").replace(" ","");
            bigFile.setOprNum(op);
            bigFile.setRemark(" ");
            bigFile.setUpdateTime(new Timestamp(new Date().getTime()));
            bigFile.setHandleStatus(2);
            //上传文件
            uploadFile(bigFile);


        }
        catch (EMPException e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            ErrorCodeInfo info = ErrorCodeInfo.getInstance(language);
            // 获取自定义异常编码
            String message = e.getMessage();
            // 获取自定义异常提示信息
            String desc = "";
            // excel文件加密异常
            if (message.indexOf("B20038") == 0) {
                desc = String.format(info.getErrorInfo(IErrorCode.B20038), message.substring(6));
            }
            // zip文件为异常格式文件
            else if (message.indexOf("B20039") == 0) {
                desc = String.format(info.getErrorInfo(IErrorCode.B20039), message.substring(6));
            }
            // 文件不存在
            else if (message.indexOf("B20042") == 0) {
                desc = String.format(info.getErrorInfo(IErrorCode.B20042), message.substring(6));
            } else {
                desc = info.getErrorInfo(message);
            }
            // 拼接前台自定义异常标识
            if (desc == null) {
                desc = message;
            }
            //TODO 处理状态更新为失败
            bigFile.setUpdateTime(new Timestamp(new Date().getTime()));
            bigFile.setHandleStatus(3);
            bigFile.setRemark(desc);
        }
        catch (Exception e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            //TODO 处理状态更新为失败
            bigFile.setUpdateTime(new Timestamp(new Date().getTime()));
            bigFile.setHandleStatus(3);
            bigFile.setRemark("特殊异常，请重新上传...");
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    EmpExecutionContext.error(e, "超大文件拆分，关闭文件流异常");
                }
            }
            // change by denglj 2018.11.29
            if (readerList != null) {
                for (int i = 0; i < readerList.size(); i++) {
                    if (readerList.get(i) != null) {
                        try {
                            readerList.get(i).close();
                        }
                        catch (Exception e) {
                            EmpExecutionContext.error(e, "文件流关闭异常");
                        }
                    }

                }
                readerList.clear();
                readerList = null;
            }
            if (perEffOS != null) {
                try {
                    perEffOS.close();
                }
                catch (Exception e) {
                    EmpExecutionContext.error(e, "超大文件拆分，解析文本文件流关闭有效号码文件输入流异常，lguserid:"
                            + lguserid
                            + "，corpCode:"
                            + corpCode);
                }
            }
            if (perBadOS != null) {
                try {
                    perBadOS.close();
                }
                catch (Exception e) {
                    EmpExecutionContext.error(e, "超大文件拆分，解析文本文件流关闭无效号码文件输入流异常，lguserid:"
                            + lguserid
                            + "，corpCode:"
                            + corpCode);
                }
            }
            // 执行删除临时文件的操作
            cleanTempFile(params);
            try {
                empDao.update(bigFile);
            } catch (Exception e) {
                EmpExecutionContext.error(e, "超大文件拆分处理，更新状态失败");
            }
        }
    }

    private void uploadFile(LfBigFile bigFile) throws Exception{
        int effNum = bigFile.getEffNum();
        Class clazz = bigFile.getClass();
        Method setMethod = null;
        Method getMethod = null;
        String count = "";
        //上传有效号码文件
        for (int i = 0; i < effNum; i++) {
            count = i ==0?"":String.valueOf(i+1);
            setMethod = clazz.getMethod("setFileUrl"+count, String.class);
            getMethod = clazz.getMethod("getFileUrl"+count);
            String path = (String)getMethod.invoke(bigFile);
            setMethod.invoke(bigFile, comBiz.uploadFileToFileServer(path)+path);
        }
        //错误号码数目大于0，则需要上传错误号码文件
        if((bigFile.getBlaCount()+bigFile.getRepCount()+bigFile.getErrCount())>0L) {
            //上传错误号码文件
            comBiz.uploadFileToFileServer(bigFile.getBadUrl());
        }
        bigFile.setBadUrl(bigFile.getBadUrl());

    }



    private void setFileUrl(LfBigFile bigFile, String fileUrl, int effFileCount) throws Exception {
        Class clazz = bigFile.getClass();
        String count = effFileCount==1?"":String.valueOf(effFileCount);
        Method setMethod = clazz.getMethod("setFileUrl"+count, String.class);
        setMethod.invoke(bigFile, fileUrl);

    }

    /**
     * 删除临时文件
     *
     * @param params
     *            传递参数类
     */
    private void cleanTempFile(PreviewParams params) {
        try {
            // 获取待删除的文件url集合
            List<String> delFileList = params.getDelFilePath();
            if (delFileList != null && delFileList.size() > 0) {
                for (String fileUrl : delFileList) {
                    File file = new File(fileUrl);
                    // 判断文件是否存在
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }
        catch (Exception e) {
            EmpExecutionContext.error(e, "删除临时文件异常");
        }
    }
}
