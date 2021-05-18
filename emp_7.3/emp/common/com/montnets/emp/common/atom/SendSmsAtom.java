package com.montnets.emp.common.atom;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.SameMmsBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.PreviewParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfDrafts;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.util.ChangeCharset;
import com.montnets.emp.util.ExcelTool;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendSmsAtom{
    private final SameMmsBiz sameMmsBiz = new SameMmsBiz();
    private final BaseBiz baseBiz = new BaseBiz();
    private final SmsBiz smsBiz = new SmsBiz();
    private final ChangeCharset charsetUtil = new ChangeCharset();
    private final ExcelTool excelTool = new ExcelTool();

    private final PhoneUtil phoneUtil = new PhoneUtil();

    private final DepBiz depBiz = new DepBiz();

    private final BlackListAtom blackBiz = new BlackListAtom();

    private final TxtFileUtil txtFileUtil = new TxtFileUtil();

    private final CommonVariables cv = new CommonVariables();

    // 写文件时候要的换行符
    String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);

    /**
     * 解析上传文件，（txt,et,xls,zip），返回文件流
     *
     * @param fileItem  文件对象
     * @param url       文件地址
     * @param fileCount 文件序号
     * @return 返回文件流
     * @throws Exception
     */
    
    public List<BufferedReader> parseFile(FileItem fileItem, String url, int fileCount, PreviewParams preParams) throws Exception {
        // 文件名
        String fileCurName = "";
        // change by denglj 20181129 再文件解析异常的情况下，直接抛异常，会导致当前的流未关闭，所以需要在catch里面关闭
        BufferedReader bufferedReader = null;
        List<BufferedReader> readerList = new ArrayList<BufferedReader>();

        try {

            // 获取文件名
            fileCurName = fileItem.getName();
            // 通过文件名截取到文件类型
            String fileType = fileCurName.substring(fileCurName.lastIndexOf(".")).toLowerCase();
            //设置文件名
            preParams.setFileName(fileCurName);
            // 读取rar压缩文件流
            if (fileType.equals(".rar")) {
                readerList.addAll(parseRar(fileItem, url, fileCount, preParams));
            }
            // 读取zip压缩文件流
            else if (fileType.equals(".zip")) {
                readerList.addAll(parseZip(fileItem, url, fileCount, preParams));
            }
            // 解析2003及WPS格式的的excel文件
            else if (fileType.equals(".xls") || fileType.equals(".et")) {
                //readerList.add(excelTool.jxExcel(url, fileItem.getInputStream(), "_" + String.valueOf(fileCount), 1, preParams));
                //调用2003文件解析方法
                bufferedReader = excelTool.jxExcel2003_V1(url, fileItem.getInputStream(), "_" + String.valueOf(fileCount), preParams);
                //解析成功
                if (bufferedReader != null) {
                    readerList.add(bufferedReader);
                }
                //解析失败
                else {
                    //是否Excel2007格式文件
                    if (excelTool.isExcel2007(fileItem.getInputStream(), fileCurName)) {
                        EmpExecutionContext.error("解析EXCEL2003或et文件失败，检查文件为xlsx类型，使用jxExcel2007解析方法重新进行解析！文件名:" + preParams.getFileName());
                        //2007格式文件，调用2007解析方法
                        readerList.add(excelTool.jxExcel2007(url, fileItem.getInputStream(), "_" + String.valueOf(fileCount), preParams, -1));
                    } else {
                        EmpExecutionContext.error("解析上传文件异常。文件名：" + fileCurName);
                        throw new EMPException(ErrorCodeInfo.B20004);
                    }
                }
            }
            // 解析excel2007文件
            else if (fileType.equals(".xlsx")) {
                //readerList.add(excelTool.jxExcel(url, fileItem.getInputStream(), "_" + String.valueOf(fileCount), 2, preParams));
                //调用2007文件解析方法
                bufferedReader = excelTool.jxExcel2007_V1(url, fileItem.getInputStream(), "_" + String.valueOf(fileCount), preParams, -1);
                //解析成功
                if (bufferedReader != null) {

                    readerList.add(bufferedReader);
                }
                //解析失败
                else {
                    //是否Excel2003格式文件
                    if (excelTool.isExcel2003(fileItem.getInputStream(), fileCurName)) {
                        EmpExecutionContext.error("解析EXCEL2007文件失败，检查文件为xls类型，使用jxExcel2003解析方法重新进行解析！文件名:" + fileCurName);
                        //2003格式文件，调用2003解析方法
                        readerList.add(excelTool.jxExcel2003(url, fileItem.getInputStream(), "_" + String.valueOf(fileCount), preParams));
                    } else {
                        EmpExecutionContext.error("解析上传文件异常。文件名：" + fileCurName);
                        throw new EMPException(ErrorCodeInfo.B20043);
                    }
                }
            }
            // 解析TXT文本
            else {
                /*InputStream instream = fileItem.getInputStream();
                String charset = charsetUtil.get_charset(instream);
                bufferedReader = new BufferedReader(new InputStreamReader(fileItem.getInputStream(), charset));
                if (charset.startsWith("UTF-")) {
                    bufferedReader.read(new char[1]);
                }*/
                bufferedReader = charsetUtil.getReader(fileItem.getInputStream(), fileItem.getInputStream());
                readerList.add(bufferedReader);

            }
            return readerList;
        } catch (EMPException e) {

            // add by denglj 2018.11.29 ,注意这里不能放finally里面进行关闭，因为正常情况下，外部要调用
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e1) {
                EmpExecutionContext.error(e1, "解析上传文件关闭流异常！");
            }
            closereaderList(readerList);
            EmpExecutionContext.error(e, "解析上传文件异常。文件名：" + fileCurName);
            throw e;
        } catch (Exception e) {
            //start add by denglj 2018.11.29 ,注意这里不能放finally里面进行关闭，因为正常情况下，外部要调用
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e1) {
                    EmpExecutionContext.error(e1, "解析上传文件关闭流异常！");
                }
            }
            closereaderList(readerList);
            //end

            EmpExecutionContext.error(e, "解析上传文件异常。文件名：" + fileCurName);
            throw new EMPException(ErrorCodeInfo.B20005, e);
        }
    }

    /**
     * 解析zip文件，返回文件流
     *
     * @param fileItem
     * @param url
     * @param fileCount
     * @return 返回文件流
     * @throws Exception
     */
    private List<BufferedReader> parseZip(FileItem fileItem, String url, int fileCount, PreviewParams params) throws Exception {
        File zipUrl = null;
        // 返回结果
        List<BufferedReader> returnList = new ArrayList<BufferedReader>();
        try {
            // 创建临时文件，获取zip地址
            String zipFileStr = url.replace(".txt", "_" + fileCount + ".zip");
            // TODO:不需要写文件，直接解析
            zipUrl = new File(zipFileStr);
            // 将上传的文件写入临时文件
            fileItem.write(zipUrl);
            ZipFile zipFile = new ZipFile(zipUrl);
            Enumeration zipEnum = zipFile.getEntries();
            ZipEntry entry = null;
            int fileIndex = 0;
            while (zipEnum.hasMoreElements()) {
                // 这个变量的作用是，如果压缩包里面有多个文件，就需要多个临时文件，另外删除临时文件也可以用到
                fileIndex++;

                // 解压缩后的文件名的标签序号，用于删除文件时
                String fileIndexStr = String.valueOf(fileIndex) + "_" + String.valueOf(fileCount);
                entry = (ZipEntry) zipEnum.nextElement();
                // 处理txt文件
                if (!entry.isDirectory() && (entry.getName().toLowerCase().endsWith(".txt") || entry.getName().toLowerCase().endsWith(".csv"))) {
                   /* InputStream instream = zipFile.getInputStream(entry);
                    String charset = charsetUtil.get_charset(instream);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry), charset));
                    if (charset.startsWith("UTF-")) {
                        reader.read(new char[1]);
                    }*/
                    BufferedReader reader = charsetUtil.getReader(zipFile.getInputStream(entry), zipFile.getInputStream(entry));
                    returnList.add(reader);
                } else if (!entry.isDirectory() && (entry.getName().toLowerCase().endsWith(".xls") || entry.getName().toLowerCase().endsWith(".et"))) {
                    //returnList.add(excelTool.jxExcel(url, zipFile.getInputStream(entry), fileIndexStr, 1, params));
                    returnList.add(excelTool.jxExcel2003(url, zipFile.getInputStream(entry), fileIndexStr, params));

                }
                // 如果是.xlsx文件
                else if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".xlsx")) {
                    //returnList.add(excelTool.jxExcel(url, zipFile.getInputStream(entry), fileIndexStr, 2, params));
                    returnList.add(excelTool.jxExcel2007(url, zipFile.getInputStream(entry), fileIndexStr, params, -1));
                }
            }
            return returnList;
        } catch (EMPException e) {
            closereaderList(returnList);
            EmpExecutionContext.error(e, "解析zip文件异常。");
            throw e;
        } catch (Exception e) {
            closereaderList(returnList);
            if (e.getMessage().equals("Negative seek offset")) {
                EmpExecutionContext.error(e, "'" + zipUrl + "'文件不是正确的zip格式。");
                throw new EMPException(ErrorCodeInfo.B20039 + params.getFileName(), e);
            } else {
                EmpExecutionContext.error(e, "解析zip文件异常。zipUrl:" + zipUrl);
                throw new EMPException(ErrorCodeInfo.B20018, e);
            }
        } finally {
            // 删除临时zip
            if (zipUrl != null) {
                try {
                    boolean state = zipUrl.delete();
                    if (!state) {
                        EmpExecutionContext.error("删除失败");
                    }
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "删除zip文件异常!zipUrl:" + zipUrl);
                }
            }
        }

    }

    /**
     * 解析rar文件，返回文件流
     *
     * @param fileItem
     * @param url
     * @param fileCount
     * @param params
     * @return
     * @throws Exception
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-4-8 下午05:43:09
     */
    public List<BufferedReader> parseRar(FileItem fileItem, String url, int fileCount, PreviewParams params) throws Exception {
        File rarUrl = null;
        // 返回结果
        List<BufferedReader> returnList = new ArrayList<BufferedReader>();
        Archive archive = null;
        try {
            // 创建临时文件，获取zip地址
            String rarFileStr = url.replace(".txt", "_" + fileCount + ".rar");
            rarUrl = new File(rarFileStr);
            // 将上传的文件写入临时文件
            fileItem.write(rarUrl);
            archive = new Archive(rarUrl);
            //文件个数
            int fileIndex = 0;
            //压缩包内的文件名
            String fileName = "";
            //文件头
            FileHeader fileHeader = null;
            //文件流
            InputStream inputStream = null;
            while ((fileHeader = archive.nextFileHeader()) != null) {
                // 这个变量的作用是，如果压缩包里面有多个文件，就需要多个临时文件，另外删除临时文件也可以用到
                fileIndex++;
                // 解压缩后的文件名的标签序号，用于删除文件时
                String fileIndexStr = String.valueOf(fileIndex) + "_" + String.valueOf(fileCount);
                if (!fileHeader.isDirectory()) {
                    //获取文件流
                    inputStream = archive.getInputStream(fileHeader);
                    //获取文件名
                    fileName = fileHeader.getFileNameString().toLowerCase();
                    //fileName = fileHeader.getFileName().toLowerCase();
                    // 处理txt文件
                    if (fileName.endsWith(".txt") || fileName.endsWith(".csv")) {
                        /*String charset = charsetUtil.get_charset(inputStream);
                        inputStream = archive.getInputStream(fileHeader);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
                        if (charset.startsWith("UTF-")) {
                            reader.read(new char[1]);
                        }*/
                        //注释老方法
                        //BufferedReader reader = charsetUtil.getReader(inputStream, fileItem.getInputStream());
                        BufferedReader reader = charsetUtil.getReader(inputStream);
                        returnList.add(reader);
                    } else if (fileName.endsWith(".xls") || fileName.endsWith(".et")) {
                        returnList.add(excelTool.jxExcel2003(url, inputStream, fileIndexStr, params));

                    }
                    // 如果是.xlsx文件
                    else if (fileName.endsWith(".xlsx")) {
                        returnList.add(excelTool.jxExcel2007(url, inputStream, fileIndexStr, params, -1));
                    }
                }
            }
            return returnList;
        } catch (EMPException e) {
            closereaderList(returnList);
            EmpExecutionContext.error(e, "解析rar文件异常。rarUrl:" + rarUrl);
            throw e;
        } catch (Exception e) {
            closereaderList(returnList);
            if (e.getMessage().equals("Negative seek offset")) {
                EmpExecutionContext.error(e, "'" + rarUrl + "'文件不是正确的rar格式。");
                throw new EMPException(ErrorCodeInfo.B20039 + params.getFileName(), e);
            } else {
                EmpExecutionContext.error(e, "解析rar文件异常。rarUrl:" + rarUrl);
                throw new EMPException(ErrorCodeInfo.B20018, e);
            }
        } finally {
            // 删除临时rar文件
            if (rarUrl != null) {
                try {
                    boolean state = rarUrl.delete();
                    if (!state) {
                        EmpExecutionContext.error("删除失败");
                    }
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "删除rar文件异常!rarUrl:" + rarUrl);
                }
            }
//            if (archive != null) {
//                try {
//                    archive.close();
//                } catch (Exception e) {
//                    EmpExecutionContext.error(e, "关闭资源异常" + archive);
//                }
//            }
        }
    }

    private void closereaderList(List<BufferedReader> readerList) {
        if (readerList != null) {
            for (int i = 0; i < readerList.size(); i++) {
                if (readerList.get(i) != null) {
                    try {
                        BufferedReader br = readerList.get(i);
                        br.close();
                    } catch (IOException e1) {
                        EmpExecutionContext.error(e1, "解析上传文件关闭流异常！");
                    }
                }
            }
        }
    }

    /**
     * 获取文件大小
     *
     * @param fileItemsList
     * @return
     */
    
    public long getFilesSize(List<FileItem> fileItemsList) {
        try {
            // 处理文件
            long curSize = 0l;
            for (int i = 0; i < fileItemsList.size(); i++) {
                curSize += fileItemsList.get(i).getSize();
            }
            return curSize;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取上传文件大小失败。错误码：" + IErrorCode.V10014);
            return StaticValue.EXP_RETURN;
        }

    }

    /**
     * 检查zip文件大小
     *
     * @param fileItem
     * @return
     */
    
    public boolean isZipOverSize(FileItem fileItem) {
        try {
            String fileCurName = fileItem.getName();
            String fileType = fileCurName.substring(fileCurName.lastIndexOf("."));

            if (".zip".equals(fileType) && fileItem.getSize() > StaticValue.ZIP_SIZE) {
                return true;
            }
            return false;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "zip文件超出大小。错误码：" + IErrorCode.V10014);
            return false;
        }

    }

    /**
     * 解析号码，检验号码合法，过滤黑名单、过滤重号
     *
     * @param readerList
     * @param params
     * @param haoduan
     * @param lguserid
     * @param corpCode
     * @throws Exception
     */
    
    public void parsePhone(List<BufferedReader> readerList, PreviewParams params, String[] haoduan, String lguserid, String corpCode, String busCode, HttpServletRequest request) throws Exception {
        // 运营商有效号码数
        int[] oprValidPhone = params.getOprValidPhone();
        // 运营商标识。0:移动号码 ;1:联通号码;2:电信号码;3:国际号码
        int index = 0;
        //号码返回状态
        int resultStatus = 0;
        // 解析号码文件
        String mobile;
        // 每批次的有效号码数，在该批号码写入数据库后重置为默认值
        int perEffCount = 0;
        // 每批次的无效号码数，在该批号码写入数据库后重置为默认值
        int perBadCount = 0;
        BufferedReader reader = null;

        TxtFileUtil txtFileUtil = new TxtFileUtil();
        //有效号码文件
        File perEffFile = null;
        //无效号码文件
        File perBadFile = null;
        //合法号码文件流
        FileOutputStream perEffOS = null;
        //无效号码文件流
        FileOutputStream perBadOS = null;
        //提交号码总数
        Integer subCount = 0;
        try {
            // 有效号码
            StringBuilder contentSb = new StringBuilder(64);
            // 无效号码
            StringBuilder badContentSb = new StringBuilder(64);
            for (int r = 0; r < readerList.size(); r++) {
                // 如果上传号码大于设置的最大值就不允许发送
                if (params.getEffCount() > StaticValue.MAX_PHONE_NUM) {
                    EmpExecutionContext.error(MessageUtils.extractMessage("common", "common_sms_code1", request) + StaticValue.MAX_PHONE_NUM + "，corpCode:" + corpCode + "，userid：" + lguserid);
                    throw new EMPException(IErrorCode.A40000);
                }
                reader = readerList.get(r);
                while ((mobile = reader.readLine()) != null) {
                    subCount++;
                    mobile = mobile.trim();
                    // 去掉号码中+86前缀
                    mobile = StringUtils.parseMobile(mobile);
                    // 检查号码合法性和号段
                    if ((index = phoneUtil.getPhoneType(mobile, haoduan)) < 0) {
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(mobile).append(line);
                        params.setBadModeCount(params.getBadModeCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                    } else if ((resultStatus = phoneUtil.checkRepeat(mobile, params.getValidPhone())) != 0) {
                        // 1为重复号码
                        if (resultStatus == 1) {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code3", request)).append(mobile).append(line);
                            params.setRepeatCount(params.getRepeatCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                        }
                        //-1为非法号码
                        else {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(mobile).append(line);
                            params.setBadModeCount(params.getBadModeCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                        }
                    } else if (blackBiz.checkBlackList(corpCode, mobile, busCode)) {
                        // 检查是否是黑名单
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code4", request)).append(mobile).append(line);
                        params.setBlackCount(params.getBlackCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                    } else {
                        perEffCount++;
                        // 有效号码
                        contentSb.append(mobile).append(line);
                        params.setEffCount(params.getEffCount() + 1);
                        // 预览10个号码
                        if (params.getEffCount() < 11) {
                            if (mobile != null && !"".equals(mobile) && params.getIshidephone() == 0) {
                                mobile = cv.replacePhoneNumber(mobile);
                            }
                            params.setPreviewPhone(params.getPreviewPhone() + mobile + StaticValue.MSG_SPLIT_CHAR + index + ";");
                        }
                        // 累加运营商有效号码数(区分运营商)
                        oprValidPhone[index] += 1;
                    }

                    // 一千条存贮一次
                    if (perEffCount >= StaticValue.PER_PHONE_NUM) {
                        if (perEffFile == null) {
                            //有效号码文件
                            perEffFile = new File(params.getPhoneFilePath()[0]);
                            //判断文件是否存在，不存在就新建一个
                            if (!perEffFile.exists()) {

                                boolean state = perEffFile.createNewFile();
                                if (!state) {
                                    EmpExecutionContext.error("创建失败");
                                }
                            }
                        }
                        if (perEffOS == null) {
                            //合法号码文件输出流
                            perEffOS = new FileOutputStream(params.getPhoneFilePath()[0], true);
                        }
                        //写入有效号码文件
                        txtFileUtil.repeatWriteToTxtFile(perEffOS, contentSb.toString());
                        contentSb.setLength(0);
                        perEffCount = 0;
                    }
                    if (perBadCount >= StaticValue.PER_PHONE_NUM) {
                        if (perBadFile == null) {
                            //非法号码文件
                            perBadFile = new File(params.getPhoneFilePath()[2]);
                            //判断文件是否存在，不存在就新建一个
                            if (!perBadFile.exists()) {
                                boolean state = perBadFile.createNewFile();
                                if (!state) {
                                    EmpExecutionContext.error("创建失败");
                                }
                            }
                        }
                        if (perBadOS == null) {
                            //非法号码文件输出流
                            perBadOS = new FileOutputStream(params.getPhoneFilePath()[2], true);
                        }
                        //写入非法号码写文件
                        txtFileUtil.repeatWriteToTxtFile(perBadOS, badContentSb.toString());
                        badContentSb.setLength(0);
                        perBadCount = 0;
                    }
                }
                reader.close();
            }
            //设置提交总数
            params.setSubCount(subCount);
            // 设置各运营商有效号码数
            params.setOprValidPhone(oprValidPhone);

            if (contentSb.length() > 0) {
                // 剩余的有效号码写文件
                if (perEffFile == null) {
                    //有效号码文件
                    perEffFile = new File(params.getPhoneFilePath()[0]);
                    //判断文件是否存在，不存在就新建一个
                    if (!perEffFile.exists()) {
                        boolean state = perEffFile.createNewFile();
                        if (!state) {
                            EmpExecutionContext.error("创建失败");
                        }
                    }
                }
                if (perEffOS == null) {
                    //合法号码文件输出流
                    perEffOS = new FileOutputStream(params.getPhoneFilePath()[0], true);
                }
                //写入有效号码文件
                txtFileUtil.repeatWriteToTxtFile(perEffOS, contentSb.toString());
                contentSb.setLength(0);
            }

            if (badContentSb.length() > 0) {
                // 剩余的非法号码写文件
                if (perBadFile == null) {
                    //非法号码文件
                    perBadFile = new File(params.getPhoneFilePath()[2]);
                    //判断文件是否存在，不存在就新建一个
                    if (!perBadFile.exists()) {
                        boolean state = perBadFile.createNewFile();
                        if (!state) {
                            EmpExecutionContext.error("创建失败");
                        }
                    }
                }
                if (perBadOS == null) {
                    //非法号码文件输出流
                    perBadOS = new FileOutputStream(params.getPhoneFilePath()[2], true);
                }
                //写入非法号码写文件
                txtFileUtil.repeatWriteToTxtFile(perBadOS, badContentSb.toString());
                badContentSb.setLength(0);
            }

        } catch (EMPException e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            throw e;
        } catch (Exception e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            throw new EMPException(IErrorCode.B20005, e);
        } finally {
            if (reader != null) {
                reader.close();
            }
            // change by denglj 2018.11.29
            if (readerList != null) {
                for (int i = 0; i < readerList.size(); i++) {
                    if (readerList.get(i) != null) {
                        try {
                            readerList.get(i).close();
                        } catch (Exception e) {
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
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "相同内容发送，解析文本文件流关闭有效号码文件输入流异常，lguserid:" + lguserid + "，corpCode:" + corpCode);
                }
            }
            if (perBadOS != null) {
                try {
                    perBadOS.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "相同内容发送，解析文本文件流关闭无效号码文件输入流异常，lguserid:" + lguserid + "，corpCode:" + corpCode);
                }
            }
            // 执行删除临时文件的操作
            cleanTempFile(params);
        }
    }

    /**
     * 不同内容群发时，解析号码，检验号码合法，过滤黑名单、过滤重号、过滤关键字
     * Deprecated by denglj at 2018.11.29
     *
     * @param readerList
     * @param params
     * @param haoduan
     * @param lguserid
     * @param corpCode
     * @param busCode
     * @param dtMsg
     * @throws Exception
     */
    
    @Deprecated
    public void parsePhoneAndContent(List<BufferedReader> readerList, PreviewParams params, String[] haoduan, String lguserid, String corpCode, String busCode, String dtMsg, HttpServletRequest request) throws Exception {
        // 解析号码文件
        String tmp;
        String smsContent = "";
        //文件参数内容
        String fileContent = "";
        String mobile = "";
        BufferedReader reader;
        // 文件内参数格式，用于动态模板发送时计算文件内参数个数与模板参数个数比较
        int paramCount = 0;

        // 每批次的有效号码数，在该批号码写入数据库后重置为默认值
        int perEffCount = 0;
        // 每批次的无效号码数，在该批号码写入数据库后重置为默认值
        int perBadCount = 0;
        //号码返回状态
        int resultStatus = 0;
        TxtFileUtil txtFileUtil = new TxtFileUtil();
        KeyWordAtom keyWordAtom = new KeyWordAtom();
        StringBuffer viewContentSb = new StringBuffer();
        int sendType = params.getSendType();
        try {
            // 有效号码
            StringBuffer contentSb = new StringBuffer();
            // 无效号码
            StringBuffer badContentSb = new StringBuffer();
            for (int r = 0; r < readerList.size(); r++) {
                // 如果上传号码大于500w就不允许发送
                if (params.getEffCount() > StaticValue.MAX_PHONE_NUM) {
                    throw new EMPException(IErrorCode.A40000);
                }
                reader = readerList.get(r);
                while ((tmp = reader.readLine()) != null) {
                    params.setSubCount(params.getSubCount() + 1);
                    tmp = tmp.trim();
                    int index = tmp.indexOf(",");
                    if (index != -1) {
                        mobile = tmp.substring(0, tmp.indexOf(",")).trim();
                        // 去掉号码中+86前缀
                        mobile = StringUtils.parseMobile(mobile);
                    } else {
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code5", request)).append(tmp).append(line);
                        params.setBadModeCount(params.getBadModeCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                        continue;
                    }
                    // 检查号码合法性和号段
                    if (phoneUtil.getPhoneType(mobile, haoduan) < 0) {
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code6", request)).append(tmp).append(line);
                        params.setBadModeCount(params.getBadModeCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                    } else if (params.isCheckRepeat() && ((resultStatus = phoneUtil.checkRepeat(mobile, params.getValidPhone())) != 0)) {
                        //返回1为重复号码
                        if (resultStatus == 1) {
                            // 前端选择过滤重号时，验证号码是否重复
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code3", request)).append(tmp).append(line);
                            params.setRepeatCount(params.getRepeatCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                        }
                        //-1为非法号码
                        else {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code7", request)).append(tmp).append(line);
                            params.setBadModeCount(params.getBadModeCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                        }

                    } else if (blackBiz.checkBlackList(corpCode, mobile, busCode)) {
                        // 检查是否是黑名单
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code4", request)).append(tmp).append(line);
                        params.setBlackCount(params.getBlackCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                    } else {
                        // 过滤内容部分
                        smsContent = tmp.substring(index + 1).trim();
                        // 内容长度为零时
                        if (smsContent.length() == 0) {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code8", request)).append(tmp).append(line);
                            params.setBadModeCount(params.getBadModeCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                            // 跳转下一循环
                            continue;
                        }
                        // 动态模板发送时，需验证文件参数个数是否匹配模板参数个数
                        else if (sendType == 2) {
                            // 文件参数个数
                            paramCount = smsContent.split(StaticValue.MSG_SPLIT_CHAR).length;
                            fileContent = smsContent;
                            // params.getTempParamCount()是短信动态模板内容中的参数个数
                            if (paramCount < params.getTempParamCount()) {
                                // 动态模板提交时，如果文件内的参数少于模板内参数则视为格式不正确
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code9", request)).append(tmp).append(line);
                                params.setBadModeCount(params.getBadModeCount() + 1);
                                params.setBadCount(params.getBadCount() + 1);
                                perBadCount++;
                                continue;
                            } else {
                                // 动态内容拼接处理
                                smsContent = combineContentSg(smsContent, dtMsg);
                            }
                        }
                        if (smsContent.length() > StaticValue.MAX_MSG_LEN || smsContent.length() == 0) {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code10", request) + smsContent.length() + "）(" + MessageUtils.extractMessage("common", "common_sms_code11", request) + "1-" + StaticValue.MAX_MSG_LEN + MessageUtils.extractMessage("common", "common_sms_code12", request) + ")：").append(tmp).append(line);
                            params.setBadModeCount(params.getBadModeCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                            // 跳转下一循环
                            continue;
                        }
                        int filterKeyRes = keyWordAtom.filterKeyWord(smsContent.toUpperCase(), corpCode);
                        // 过滤内容关键字
                        if (filterKeyRes == 1) {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code13", request)).append(tmp).append(line);
                            params.setKwCount(params.getKwCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                            // 跳转下一循环
                            continue;
                        } else if (filterKeyRes == -1) {
                            throw new EMPException(ErrorCodeInfo.B20014);
                        }

//						// 仅当是有效号码的时候才把号码添加到过滤重号的集合
//						if(params.isCheckRepeat())
//						{
//							params.getValidPhone().add(Long.valueOf(mobile));
//						}

                        // 有效号码
                        contentSb.append(mobile + "," + smsContent).append(line);
                        params.setEffCount(params.getEffCount() + 1);
                        perEffCount++;
                        // 预览10个号码
                        if (params.getEffCount() < 11) {
                            if (sendType == 2) {
                                // 预览信息
                                viewContentSb.append(String.valueOf(fileContent) + "MWHS]#" + String.valueOf(paramCount) + "MWHS]#");
                            }
                            viewContentSb.append(mobile + "MWHS]#" + smsContent).append(line);
                        }
                    }

                    // 一千条存贮一次
                    if (perEffCount >= StaticValue.PER_PHONE_NUM) {
                        txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0], contentSb.toString());
                        contentSb.setLength(0);
                        perEffCount = 0;
                    }
                    if (perBadCount >= StaticValue.PER_PHONE_NUM) {

                        txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[2], badContentSb.toString());
                        badContentSb.setLength(0);
                        perBadCount = 0;
                    }
                }
                reader.close();
            }

            if (contentSb.length() > 0) {
                // 剩余的有效号码写文件
                txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0], contentSb.toString());
                contentSb.setLength(0);
            }

            if (badContentSb.length() > 0) {
                // 剩余的有效号码写文件
                txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[2], badContentSb.toString());
                badContentSb.setLength(0);
            }

            if (viewContentSb.length() > 0) {
                // 将预览信息写入文件
                txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[3], viewContentSb.toString());
                viewContentSb.setLength(0);
            }
        } catch (EMPException e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            throw e;
        } catch (Exception e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            throw new EMPException(IErrorCode.B20005, e);
        } finally {
            readerList.clear();
            readerList = null;
            // 执行删除临时文件的操作
            cleanTempFile(params);
        }
    }

    /**
     * 不同内容群发时，解析号码，检验号码合法，过滤黑名单、过滤重号、过滤关键字(支持国外通道英文短信)
     *
     * @param readerList
     * @param params
     * @param haoduan
     * @param lguserid
     * @param corpCode
     * @param busCode
     * @param dtMsg
     * @throws Exception
     */
    public void parsePhoneAndContent(List<BufferedReader> readerList, PreviewParams params, String[] haoduan, String lguserid, String corpCode, String busCode, String dtMsg, String spUser, HttpServletRequest request) throws Exception {
        // 解析号码文件
        String tmp;
        // 短信内容
        String smsContent = "";
        // Base64编码后的短信内容
        String smsContentBase64 = "";
        //文件参数内容
        String fileContent = "";
        String mobile = "";
        BufferedReader reader = null;
        // 文件内参数格式，用于动态模板发送时计算文件内参数个数与模板参数个数比较
        int paramCount = 0;

        // 每批次的有效号码数，在该批号码写入数据库后重置为默认值
        int perEffCount = 0;
        // 每批次的无效号码数，在该批号码写入数据库后重置为默认值
        int perBadCount = 0;
        //号码返回状态
        int resultStatus = 0;
        KeyWordAtom keyWordAtom = new KeyWordAtom();
        StringBuilder viewContentSb = new StringBuilder(64);
        int sendType = params.getSendType();
        //有效号码文件
        File perEffFile = null;
        //无效号码文件
        File perBadFile = null;
        //合法号码文件流
        FileOutputStream perEffOS = null;
        //无效号码文件流
        FileOutputStream perBadOS = null;
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
        if (spGate != null) {
            // 获取1到n-1条英文短信内容的长度
            longSmsFirstLen = Integer.parseInt(spGate.get("enmultilen1").toString());
            // 单条短信字数
            singlelen = Integer.parseInt(spGate.get("ensinglelen").toString());
            //英文短信签名长度
            signLen = Integer.parseInt(spGate.get("ensignlen").toString());
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
            StringBuilder contentSb = new StringBuilder(128);
            // 无效号码
            StringBuilder badContentSb = new StringBuilder(128);
            for (int r = 0; r < readerList.size(); r++) {
                // 如果上传号码大于500w就不允许发送
                if (params.getEffCount() > StaticValue.MAX_PHONE_NUM) {
                    EmpExecutionContext.error("不同内容预览，有效号码数超过" + StaticValue.MAX_PHONE_NUM + "，corpCode:" + corpCode + "，userid：" + lguserid);
                    throw new EMPException(IErrorCode.A40000);
                }
                reader = readerList.get(r);
                while ((tmp = reader.readLine()) != null) {
                    params.setSubCount(params.getSubCount() + 1);
                    tmp = tmp.trim();
                    int index = tmp.indexOf(",");
                    if (index != -1) {
                        mobile = tmp.substring(0, tmp.indexOf(",")).trim();
                        // 去掉号码中+86前缀
                        mobile = StringUtils.parseMobile(mobile);
                    } else {
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code5", request)).append(tmp).append(line);
                        params.setBadModeCount(params.getBadModeCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                        continue;
                    }
                    // 检查号码合法性和号段
                    if ((phoneType = phoneUtil.getPhoneType(mobile, haoduan)) < 0) {
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code6", request)).append(tmp).append(line);
                        params.setBadModeCount(params.getBadModeCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                    } else if (params.isCheckRepeat() && ((resultStatus = phoneUtil.checkRepeat(mobile, params.getValidPhone())) != 0)) {
                        //返回1为重复号码
                        if (resultStatus == 1) {
                            // 前端选择过滤重号时，验证号码是否重复
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code3", request)).append(tmp).append(line);
                            params.setRepeatCount(params.getRepeatCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                        }
                        //-1为非法号码
                        else {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code7", request)).append(tmp).append(line);
                            params.setBadModeCount(params.getBadModeCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                        }

                    } else if (blackBiz.checkBlackList(corpCode, mobile, busCode)) {
                        // 检查是否是黑名单
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code4", request)).append(tmp).append(line);
                        params.setBlackCount(params.getBlackCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                    } else {
                        // 过滤内容部分
                        smsContent = tmp.substring(index + 1).trim();
                        // 内容长度为零时或为","
                        if (smsContent.length() == 0 || ",".equals(smsContent)) {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code8", request)).append(tmp).append(line);
                            params.setBadModeCount(params.getBadModeCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                            // 跳转下一循环
                            continue;
                        }
                        // 动态模板发送时，需验证文件参数个数是否匹配模板参数个数
                        else if (sendType == 2) {
                            // 文件参数个数
                            paramCount = smsContent.split(StaticValue.MSG_SPLIT_CHAR).length;
                            fileContent = smsContent.replaceAll(StaticValue.EXECL_SPLID, ",");
                            // params.getTempParamCount()是短信动态模板内容中的参数个数
                            if (paramCount < params.getTempParamCount()) {
                                // 动态模板提交时，如果文件内的参数少于模板内参数则视为格式不正确
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code9", request)).append(tmp).append(line);
                                params.setBadModeCount(params.getBadModeCount() + 1);
                                params.setBadCount(params.getBadCount() + 1);
                                perBadCount++;
                                continue;
                            } else {
                                // 动态内容拼接处理
//								smsContent = combineContentSg(smsContent, dtMsg);
                                // 动态内容拼接处理，不去换行
                                smsContent = combineContent(smsContent, dtMsg);
                                // 动态内容拼接处理异常
                                if (smsContent == null) {
                                    // 动态模板提交时，如果动态内容拼接处理异常
                                    badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code14", request)).append(tmp).append(line);
                                    params.setBadModeCount(params.getBadModeCount() + 1);
                                    params.setBadCount(params.getBadCount() + 1);
                                    perBadCount++;
                                    continue;
                                }
                            }
                        }
                        //文件内容发送
                        else if (sendType == 3) {
                            smsContent = smsContent.replaceAll(StaticValue.EXECL_SPLID, ",");
                            //贴尾内容
                            if (dtMsg != null && dtMsg.trim().length() > 0) {
                                smsContent += dtMsg;
                            }
                        }
                        //下标0(0:英文短信;1:中文短信);下标1(处理后的短信内容);下标2(短信长度)
                        smsContentInfo = smsBiz.getSmsContentInfo(smsContent, longSmsFirstLen, singlelen, signLen, gateprivilege);
                        //处理后的短信内容
                        smsContent = smsContentInfo[1];
                        if (smsContent.length() > StaticValue.MAX_MSG_LEN || smsContent.length() == 0) {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code10", request) + smsContent.length() + "）(" + MessageUtils.extractMessage("common", "common_sms_code11", request) + "1-" + StaticValue.MAX_MSG_LEN + MessageUtils.extractMessage("common", "common_sms_code12", request) + ")：").append(tmp).append(line);
                            params.setBadModeCount(params.getBadModeCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                            // 跳转下一循环
                            continue;
                        }

                        //国际号码并且存在国外通道
                        if (phoneType == 3 && spGate != null) {
                            //0:英文短信;1:中文短信
                            SmsCharType = smsContentInfo[0];
                            //英文短信长度
                            smsLen = Integer.valueOf(smsContentInfo[2]);
                            //英文短信大于700字或中文短信大于350字
                            if (("0".equals(SmsCharType) && smsLen > (720 - 20)) || ("1".equals(SmsCharType) && smsContent.length() > (360 - 10))) {
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code10", request) + smsContent.length() + "，" + MessageUtils.extractMessage("common", "common_sms_code15", request) + ")：").append(tmp).append(line);
                                params.setBadModeCount(params.getBadModeCount() + 1);
                                params.setBadCount(params.getBadCount() + 1);
                                perBadCount++;
                                // 跳转下一循环
                                continue;
                            }
                        }

                        int filterKeyRes = keyWordAtom.filterKeyWord(smsContent.toUpperCase(), corpCode);
                        // 过滤内容关键字
                        if (filterKeyRes == 1) {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code13", request)).append(tmp).append(line);
                            params.setKwCount(params.getKwCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                            // 跳转下一循环
                            continue;
                        } else if (filterKeyRes == -1) {
                            EmpExecutionContext.error("不同内容预览，过滤关键字异常，corpCode：" + corpCode + "，smsContent：" + smsContent.toUpperCase());
                            throw new EMPException(ErrorCodeInfo.B20014);
                        }

//						// 仅当是有效号码的时候才把号码添加到过滤重号的集合
//						if(params.isCheckRepeat())
//						{
//							params.getValidPhone().add(Long.valueOf(mobile));
//						}

                        // 动态模板发送时，对短信内容进行BASE64编码
                        if (sendType == 2) {
                            // 短信内容BASE64编码
                            smsContentBase64 = contentBase64Encoder(smsContent);
                            // 短信内容BASE64编码异常
                            if (smsContentBase64 == null) {
                                // 动态模板提交时，如果动态内容拼接处理异常
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code16", request)).append(tmp).append(line);
                                params.setBadModeCount(params.getBadModeCount() + 1);
                                params.setBadCount(params.getBadCount() + 1);
                                perBadCount++;
                                continue;
                            }
                            // 有效号码
                            contentSb.append(mobile + "," + smsContentBase64).append(line);
                        } else {
                            // 有效号码
                            contentSb.append(mobile + "," + smsContent).append(line);
                        }
                        params.setEffCount(params.getEffCount() + 1);
                        perEffCount++;
                        // 预览10个号码
                        if (params.getEffCount() < 11) {
                            if (sendType == 2) {
                                // 预览信息
                                viewContentSb.append(String.valueOf(fileContent) + "MWHS]#" + String.valueOf(paramCount) + "MWHS]#");
                                viewContentSb.append(mobile + "MWHS]#" + smsContent.replace("\r\n", " ")).append(line);
                            } else {
                                viewContentSb.append(mobile + "MWHS]#" + smsContent).append(line);
                            }
                        }
                    }

                    // 一千条存贮一次
                    if (perEffCount >= StaticValue.PER_PHONE_NUM) {
                        if (perEffFile == null) {
                            //有效号码文件
                            perEffFile = new File(params.getPhoneFilePath()[0]);
                            //判断文件是否存在，不存在就新建一个
                            if (!perEffFile.exists()) {
                                boolean state = perEffFile.createNewFile();
                                if (!state) {
                                    EmpExecutionContext.error("创建失败");
                                }
                            }
                        }
                        if (perEffOS == null) {
                            //合法号码文件输出流
                            perEffOS = new FileOutputStream(params.getPhoneFilePath()[0], true);
                        }
                        //写入有效号码文件
                        txtFileUtil.repeatWriteToTxtFile(perEffOS, contentSb.toString());
                        //txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0], contentSb.toString());
                        contentSb.setLength(0);
                        perEffCount = 0;
                    }
                    if (perBadCount >= StaticValue.PER_PHONE_NUM) {
                        if (perBadFile == null) {
                            //非法号码文件
                            perBadFile = new File(params.getPhoneFilePath()[2]);
                            //判断文件是否存在，不存在就新建一个
                            if (!perBadFile.exists()) {
                                boolean state = perBadFile.createNewFile();
                                if (!state) {
                                    EmpExecutionContext.error("创建失败");
                                }
                            }
                        }
                        if (perBadOS == null) {
                            //非法号码文件输出流
                            perBadOS = new FileOutputStream(params.getPhoneFilePath()[2], true);
                        }
                        //写入非法号码写文件
                        txtFileUtil.repeatWriteToTxtFile(perBadOS, badContentSb.toString());
                        //txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[2], badContentSb.toString());
                        badContentSb.setLength(0);
                        perBadCount = 0;
                    }
                }
                reader.close();
            }

            if (contentSb.length() > 0) {
                if (perEffFile == null) {
                    //有效号码文件
                    perEffFile = new File(params.getPhoneFilePath()[0]);
                    //判断文件是否存在，不存在就新建一个
                    if (!perEffFile.exists()) {
                        boolean state = perEffFile.createNewFile();
                        if (!state) {
                            EmpExecutionContext.error("创建失败");
                        }

                    }
                }
                if (perEffOS == null) {
                    //合法号码文件输出流
                    perEffOS = new FileOutputStream(params.getPhoneFilePath()[0], true);
                }
                // 剩余的有效号码写文件
                txtFileUtil.repeatWriteToTxtFile(perEffOS, contentSb.toString());
                //txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0], contentSb.toString());
                contentSb.setLength(0);
            }

            if (badContentSb.length() > 0) {
                if (perBadFile == null) {
                    //非法号码文件
                    perBadFile = new File(params.getPhoneFilePath()[2]);
                    //判断文件是否存在，不存在就新建一个
                    if (!perBadFile.exists()) {
                        boolean state = perBadFile.createNewFile();
                        if (!state) {
                            EmpExecutionContext.error("创建失败");
                        }

                    }
                }
                if (perBadOS == null) {
                    //非法号码文件输出流
                    perBadOS = new FileOutputStream(params.getPhoneFilePath()[2], true);
                }
                // 剩余的有效号码写文件
                txtFileUtil.repeatWriteToTxtFile(perBadOS, badContentSb.toString());
                //txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[2], badContentSb.toString());
                badContentSb.setLength(0);
            }

            if (viewContentSb.length() > 0) {
                // 将预览信息写入文件
                txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[3], viewContentSb.toString());
                viewContentSb.setLength(0);
            }
        } catch (EMPException e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            throw e;
        } catch (Exception e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            throw new EMPException(IErrorCode.B20005, e);
        } finally {
            // add by denglj 2018.11.29 异常情况下，保证关闭流
            if (reader != null) {
                reader.close();
            }

            if (readerList != null) {

                readerList.clear();
                readerList = null;
            }
            if (perEffOS != null) {
                try {
                    perEffOS.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "不同内容发送，解析文本文件流关闭无效号码文件输入流异常，lguserid:" + lguserid + "，corpCode:" + corpCode);
                }
            }
            if (perBadOS != null) {
                try {
                    perBadOS.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "不同内容发送，解析文本文件流关闭无效号码文件输入流异常，lguserid:" + lguserid + "，corpCode:" + corpCode);
                }
            }
            // 执行删除临时文件的操作
            cleanTempFile(params);
        }
    }

    /**
     * 不同内容群发时，解析号码，检验号码合法，过滤黑名单、过滤重号、过滤关键字(支持国外通道英文短信)
     * 2018-1-24,caihq,不同内容发送，彩信，
     *
     * @param readerList
     * @param params
     * @param haoduan
     * @param lguserid
     * @param corpCode
     * @param busCode
     * @param dtMsg
     * @throws Exception
     */
    public void parseRmsPhoneAndContent(List<BufferedReader> readerList, PreviewParams params, String[] haoduan, String lguserid, String corpCode, String busCode, String dtMsg, String spUser, HttpServletRequest request) throws Exception {
        // 解析号码文件
        String tmp;
        // 短信内容
        String smsContent = "";
        // Base64编码后的短信内容
        String smsContentBase64 = "";
        //文件参数内容
        String fileContent = "";
        String mobile = "";
        BufferedReader reader;
        // 文件内参数格式，用于动态模板发送时计算文件内参数个数与模板参数个数比较
        int paramCount = 0;

        // 每批次的有效号码数，在该批号码写入数据库后重置为默认值
        int perEffCount = 0;
        // 每批次的无效号码数，在该批号码写入数据库后重置为默认值
        int perBadCount = 0;
        //号码返回状态
        int resultStatus = 0;
        KeyWordAtom keyWordAtom = new KeyWordAtom();
        StringBuffer viewContentSb = new StringBuffer();
        int sendType = params.getSendType();
        //有效号码文件
        File perEffFile = null;
        //无效号码文件
        File perBadFile = null;
        //合法号码文件流
        FileOutputStream perEffOS = null;
        //无效号码文件流
        FileOutputStream perBadOS = null;
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
        if (spGate != null) {
            // 获取1到n-1条英文短信内容的长度
            longSmsFirstLen = Integer.parseInt(spGate.get("enmultilen1").toString());
            // 单条短信字数
            singlelen = Integer.parseInt(spGate.get("ensinglelen").toString());
            //英文短信签名长度
            signLen = Integer.parseInt(spGate.get("ensignlen").toString());
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
            for (int r = 0; r < readerList.size(); r++) {
                // 如果上传号码大于500w就不允许发送
                if (params.getEffCount() > StaticValue.MAX_PHONE_NUM) {
                    EmpExecutionContext.error("不同内容预览，有效号码数超过" + StaticValue.MAX_PHONE_NUM + "，corpCode:" + corpCode + "，userid：" + lguserid);
                    throw new EMPException(IErrorCode.A40000);
                }
                reader = readerList.get(r);
                while ((tmp = reader.readLine()) != null) {
                    params.setSubCount(params.getSubCount() + 1);
                    tmp = tmp.trim();
                    int index = tmp.indexOf(",");
                    if (index != -1) {
                        mobile = tmp.substring(0, tmp.indexOf(",")).trim();
                        // 去掉号码中+86前缀
                        mobile = StringUtils.parseMobile(mobile);
                    } else {
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code5", request)).append(tmp).append(line);
                        params.setBadModeCount(params.getBadModeCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                        continue;
                    }
                    // 检查号码合法性和号段
                    if ((phoneType = phoneUtil.getPhoneType(mobile, haoduan)) < 0) {
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code6", request)).append(tmp).append(line);
                        params.setBadModeCount(params.getBadModeCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                    } else if (params.isCheckRepeat() && ((resultStatus = phoneUtil.checkRepeat(mobile, params.getValidPhone())) != 0)) {
                        //返回1为重复号码
                        if (resultStatus == 1) {
                            // 前端选择过滤重号时，验证号码是否重复
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code3", request)).append(tmp).append(line);
                            params.setRepeatCount(params.getRepeatCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                        }
                        //-1为非法号码
                        else {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code7", request)).append(tmp).append(line);
                            params.setBadModeCount(params.getBadModeCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                        }

                    } else if (blackBiz.checkBlackList(corpCode, mobile, busCode)) {
                        // 检查是否是黑名单
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code4", request)).append(tmp).append(line);
                        params.setBlackCount(params.getBlackCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        perBadCount++;
                    } else {
                        // 过滤内容部分
                        smsContent = tmp.substring(index + 1).trim();
                        // 内容长度为零时或为","
                        if (smsContent.length() == 0 || ",".equals(smsContent)) {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code8", request)).append(tmp).append(line);
                            params.setBadModeCount(params.getBadModeCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                            // 跳转下一循环
                            continue;
                        }
                        // 动态模板发送时，需验证文件参数个数是否匹配模板参数个数
                        else if (sendType == 2) {
                            // 文件参数个数
                            paramCount = smsContent.split(StaticValue.MSG_SPLIT_CHAR).length;
                            fileContent = smsContent.replaceAll(StaticValue.EXECL_SPLID, ",");
                            // params.getTempParamCount()是短信动态模板内容中的参数个数
                            if (paramCount < params.getTempParamCount()) {
                                // 动态模板提交时，如果文件内的参数少于模板内参数则视为格式不正确
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code9", request)).append(tmp).append(line);
                                params.setBadModeCount(params.getBadModeCount() + 1);
                                params.setBadCount(params.getBadCount() + 1);
                                perBadCount++;
                                continue;
                            } else {
                                //重新组合参数
                                paramCount = params.getTempParamCount();
                                fileContent = getCombina(smsContent, paramCount);
                                // 动态内容拼接处理
//								smsContent = combineContentSg(smsContent, dtMsg);
                                // 动态内容拼接处理，不去换行
                                smsContent = combineContent(smsContent, dtMsg);
                                // 动态内容拼接处理异常
                                if (smsContent == null) {
                                    // 动态模板提交时，如果动态内容拼接处理异常
                                    badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code14", request)).append(tmp).append(line);
                                    params.setBadModeCount(params.getBadModeCount() + 1);
                                    params.setBadCount(params.getBadCount() + 1);
                                    perBadCount++;
                                    continue;
                                }
                            }
                        }
                        //文件内容发送
                        else if (sendType == 3) {
                            smsContent = smsContent.replaceAll(StaticValue.EXECL_SPLID, ",");
                            //贴尾内容
                            if (dtMsg != null && dtMsg.trim().length() > 0) {
                                smsContent += dtMsg;
                            }
                        }
                        //下标0(0:英文短信;1:中文短信);下标1(处理后的短信内容);下标2(短信长度)
                        smsContentInfo = smsBiz.getSmsContentInfo(smsContent, longSmsFirstLen, singlelen, signLen, gateprivilege);
                        //处理后的短信内容
                        smsContent = smsContentInfo[1];
//						if(smsContent.length() > StaticValue.MAX_MSG_LEN || smsContent.length() == 0)
//						{
//							badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code10", request) + smsContent.length() + "）("+MessageUtils.extractMessage("common", "common_sms_code11", request)+"1-" + StaticValue.MAX_MSG_LEN + MessageUtils.extractMessage("common", "common_sms_code12", request)+")：").append(tmp).append(line);
//							params.setBadModeCount(params.getBadModeCount() + 1);
//							params.setBadCount(params.getBadCount() + 1);
//							perBadCount++;
//							// 跳转下一循环
//							continue;
//						}

                        //国际号码并且存在国外通道
                        if (phoneType == 3 && spGate != null) {
                            //0:英文短信;1:中文短信
                            SmsCharType = smsContentInfo[0];
                            //英文短信长度
                            smsLen = Integer.valueOf(smsContentInfo[2]);
                            //英文短信大于700字或中文短信大于350字
                            if (("0".equals(SmsCharType) && smsLen > (720 - 20)) || ("1".equals(SmsCharType) && smsContent.length() > (360 - 10))) {
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code10", request) + smsContent.length() + "，" + MessageUtils.extractMessage("common", "common_sms_code15", request) + ")：").append(tmp).append(line);
                                params.setBadModeCount(params.getBadModeCount() + 1);
                                params.setBadCount(params.getBadCount() + 1);
                                perBadCount++;
                                // 跳转下一循环
                                continue;
                            }
                        }

                        int filterKeyRes = keyWordAtom.filterKeyWord(smsContent.toUpperCase(), corpCode);
                        // 过滤内容关键字
                        if (filterKeyRes == 1) {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code13", request)).append(tmp).append(line);
                            params.setKwCount(params.getKwCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            perBadCount++;
                            // 跳转下一循环
                            continue;
                        } else if (filterKeyRes == -1) {
                            EmpExecutionContext.error("不同内容预览，过滤关键字异常，corpCode：" + corpCode + "，smsContent：" + smsContent.toUpperCase());
                            throw new EMPException(ErrorCodeInfo.B20014);
                        }

//						// 仅当是有效号码的时候才把号码添加到过滤重号的集合
//						if(params.isCheckRepeat())
//						{
//							params.getValidPhone().add(Long.valueOf(mobile));
//						}

                        // 动态模板发送时，对短信内容进行BASE64编码
                        if (sendType == 2) {
                            // 短信内容BASE64编码
                            smsContentBase64 = contentBase64Encoder(smsContent);
                            // 短信内容BASE64编码异常
                            if (smsContentBase64 == null) {
                                // 动态模板提交时，如果动态内容拼接处理异常
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code16", request)).append(tmp).append(line);
                                params.setBadModeCount(params.getBadModeCount() + 1);
                                params.setBadCount(params.getBadCount() + 1);
                                perBadCount++;
                                continue;
                            }
                            // 有效号码
                            //contentSb.append(mobile + "," + smsContentBase64).append(line);
                            contentSb.append(getCombina(tmp, params.getTempParamCount() + 1)).append(line);
                        } else {
                            // 有效号码
                            contentSb.append(mobile + "," + smsContent).append(line);
                        }
                        params.setEffCount(params.getEffCount() + 1);
                        perEffCount++;
                        // 预览10个号码
                        if (params.getEffCount() < 11) {
                            if (sendType == 2) {
                                // 预览信息
                                viewContentSb.append(String.valueOf(fileContent) + "MWHS]#" + String.valueOf(paramCount) + "MWHS]#");
                                viewContentSb.append(mobile + "MWHS]#" + smsContent.replace("\r\n", " ")).append(line);
                            } else {
                                viewContentSb.append(mobile + "MWHS]#" + smsContent).append(line);
                            }
                        }
                    }

                    // 一千条存贮一次
                    if (perEffCount >= StaticValue.PER_PHONE_NUM) {
                        if (perEffFile == null) {
                            //有效号码文件
                            perEffFile = new File(params.getPhoneFilePath()[0]);
                            //判断文件是否存在，不存在就新建一个
                            if (!perEffFile.exists()) {

                                boolean state = perEffFile.createNewFile();
                                if (!state) {
                                    EmpExecutionContext.error("创建失败");
                                }

                            }
                        }
                        if (perEffOS == null) {
                            //合法号码文件输出流
                            perEffOS = new FileOutputStream(params.getPhoneFilePath()[0], true);
                        }
                        //写入有效号码文件
                        txtFileUtil.repeatWriteToTxtFile(perEffOS, contentSb.toString());
                        //txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0], contentSb.toString());
                        contentSb.setLength(0);
                        perEffCount = 0;
                    }
                    if (perBadCount >= StaticValue.PER_PHONE_NUM) {
                        if (perBadFile == null) {
                            //非法号码文件
                            perBadFile = new File(params.getPhoneFilePath()[2]);
                            //判断文件是否存在，不存在就新建一个
                            if (!perBadFile.exists()) {

                                boolean state = perBadFile.createNewFile();
                                if (!state) {
                                    EmpExecutionContext.error("创建失败");
                                }

                            }
                        }
                        if (perBadOS == null) {
                            //非法号码文件输出流
                            perBadOS = new FileOutputStream(params.getPhoneFilePath()[2], true);
                        }
                        //写入非法号码写文件
                        txtFileUtil.repeatWriteToTxtFile(perBadOS, badContentSb.toString());
                        //txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[2], badContentSb.toString());
                        badContentSb.setLength(0);
                        perBadCount = 0;
                    }
                }
                reader.close();
            }

            if (contentSb.length() > 0) {
                if (perEffFile == null) {
                    //有效号码文件
                    perEffFile = new File(params.getPhoneFilePath()[0]);
                    //判断文件是否存在，不存在就新建一个
                    if (!perEffFile.exists()) {
                        boolean state = perEffFile.createNewFile();
                        if (!state) {
                            EmpExecutionContext.error("创建失败");
                        }
                    }
                }
                if (perEffOS == null) {
                    //合法号码文件输出流
                    perEffOS = new FileOutputStream(params.getPhoneFilePath()[0], true);
                }
                // 剩余的有效号码写文件
                txtFileUtil.repeatWriteToTxtFile(perEffOS, contentSb.toString());
                //txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0], contentSb.toString());
                contentSb.setLength(0);
            }

            if (badContentSb.length() > 0) {
                if (perBadFile == null) {
                    //非法号码文件
                    perBadFile = new File(params.getPhoneFilePath()[2]);
                    //判断文件是否存在，不存在就新建一个
                    if (!perBadFile.exists()) {
                        boolean state = perBadFile.createNewFile();
                        if (!state) {
                            EmpExecutionContext.error("创建失败");
                        }

                    }
                }
                if (perBadOS == null) {
                    //非法号码文件输出流
                    perBadOS = new FileOutputStream(params.getPhoneFilePath()[2], true);
                }
                // 剩余的有效号码写文件
                txtFileUtil.repeatWriteToTxtFile(perBadOS, badContentSb.toString());
                //txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[2], badContentSb.toString());
                badContentSb.setLength(0);
            }

            if (viewContentSb.length() > 0) {
                // 将预览信息写入文件
                txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[3], viewContentSb.toString());
                viewContentSb.setLength(0);
            }
        } catch (EMPException e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            throw e;
        } catch (Exception e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            throw new EMPException(IErrorCode.B20005, e);
        } finally {
            if (readerList != null) {
                readerList.clear();
                readerList = null;
            }
            if (perEffOS != null) {
                try {
                    perEffOS.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "不同内容发送，解析文本文件流关闭无效号码文件输入流异常，lguserid:" + lguserid + "，corpCode:" + corpCode);
                }
            }
            if (perBadOS != null) {
                try {
                    perBadOS.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "不同内容发送，解析文本文件流关闭无效号码文件输入流异常，lguserid:" + lguserid + "，corpCode:" + corpCode);
                }
            }
            // 执行删除临时文件的操作
            cleanTempFile(params);
        }
    }

    /**
     * @param source
     * @param paramCount
     * @return
     * @description 组合电话号码和参数，把多余的参数丢掉
     * @author caihq
     * @date 2018-2-1 上午09:25:24
     */
    private String getCombina(String source, int paramCount) {
        String[] arrStr = source.split(",");
        String resStr = "";
        for (int i = 0; i < paramCount; i++) {
            resStr += arrStr[i] + ",";
        }
        return resStr.substring(0, resStr.length() - 1);
    }

    /**
     * 删除临时文件
     *
     * @param params 传递参数类
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
                        boolean state = file.delete();
                        if (!state) {
                            EmpExecutionContext.error("删除失败");
                        }
                    }
                }
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "删除临时文件异常");
        }
    }

    /**
     * 解析手工输入和通讯录的号码
     *
     * @param params     传递参数类
     * @param depIdStr   员工机构ID
     * @param phoneStr   员工号码
     * @param groupStr   员工群组id
     * @param lgcorpcode 企业编码
     * @param haoduan    号段
     * @throws Exception
     */
    
    public void parseAddrAndInputPhone(PreviewParams params, String depIdStr, String phoneStr, String groupStr, String lgcorpcode, String[] haoduan, String busCode, HttpServletRequest request) throws Exception {
        try {
            // 运营商有效号码数
            int[] oprValidPhone = params.getOprValidPhone();
            // 运营商标识。0:移动号码 ;1:联通号码;2:电信号码;3:国际号码
            int index = 0;
            //号码返回状态
            int resultStatus = 0;
            StringBuffer contentSb = new StringBuffer();
            StringBuffer badContentSb = new StringBuffer();
            //最后一位不为","时,增加","
            if (phoneStr != null && phoneStr.length() > 0 && !",".equals(phoneStr.substring(phoneStr.length() - 1, phoneStr.length()))) {
                phoneStr += ",";
            }
            // 解析号码字符串
            if (depIdStr != null && depIdStr.length() > 0 && !",".equals(depIdStr)) {
                // 通过机构id查找电话
                phoneStr = phoneStr + depBiz.getEmpByDepId(depIdStr, lgcorpcode);

            }
            if (groupStr != null && groupStr.length() > 0 && !",".equals(groupStr)) {
                // 通过群组查找电话
                phoneStr = sameMmsBiz.getGroupPhoneStrById(phoneStr, groupStr);

            }
            if (phoneStr.length() <= 0) {
                return;
            }

            String[] phones = phoneStr.split(",");
            String phoneString = params.getPreviewPhone();
            for (String num : phones) {
                params.setSubCount(params.getSubCount() + 1);
                if (num != null) {
                    if (num.length() >= 7 && num.length() <= 21) {
                        // 去掉号码中+86前缀
                        num = num.trim();
                        num = StringUtils.parseMobile(num);

                        if ((index = phoneUtil.getPhoneType(num, haoduan)) < 0) {
                            // 过滤号段
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(num).append(line);
                            params.setBadModeCount(params.getBadModeCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            continue;
                        } else if ((resultStatus = phoneUtil.checkRepeat(num, params.getValidPhone())) != 0) {
                            //1为重复号码
                            if (resultStatus == 1) {
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code3", request)).append(num).append(line);
                                params.setRepeatCount(params.getRepeatCount() + 1);
                                params.setBadCount(params.getBadCount() + 1);
                                continue;
                            }
                            //-1为非法号码
                            else {
                                // 过滤号段
                                badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(num).append(line);
                                params.setBadModeCount(params.getBadModeCount() + 1);
                                params.setBadCount(params.getBadCount() + 1);
                                continue;
                            }

                        } else if (blackBiz.checkBlackList(lgcorpcode, num, busCode)) {
                            badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code4", request)).append(num).append(line);
                            params.setBlackCount(params.getBlackCount() + 1);
                            params.setBadCount(params.getBadCount() + 1);
                            continue;
                        }

                        contentSb.append(num).append(line);
                        params.setEffCount(params.getEffCount() + 1);

                        // 累加运营商有效号码数(区分运营商)
                        oprValidPhone[index] += 1;

                        if (params.getEffCount() < 11) {
                            if (num != null && !"".equals(num) && params.getIshidephone() == 0) {
                                num = cv.replacePhoneNumber(num);
                            }
                            phoneString = phoneString + num + StaticValue.MSG_SPLIT_CHAR + index + ";";
                        }
                    } else {
                        badContentSb.append(MessageUtils.extractMessage("common", "common_sms_code2", request)).append(num).append(line);
                        params.setBadModeCount(params.getBadModeCount() + 1);
                        params.setBadCount(params.getBadCount() + 1);
                        continue;
                    }
                }
            }
            //预览号码
            params.setPreviewPhone(phoneString);

            // 设置各运营商有效号码数
            params.setOprValidPhone(oprValidPhone);

            if (contentSb.length() > 0) {
                txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0], contentSb.toString());
                contentSb.setLength(0);
            }

            if (badContentSb.length() > 0) {
                txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[2], badContentSb.toString());
                badContentSb.setLength(0);
            }

        } catch (EMPException e) {
            EmpExecutionContext.error(e, "相同内容预览，解析手工输入和通讯录的号码异常。");
            throw e;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "相同内容预览，解析手工输入和通讯录的号码异常。");
            throw new EMPException(IErrorCode.B20005, e);
        } finally {
            if (phoneStr != null) {
                phoneStr = null;
            }
        }
    }

    /**
     * 计算动态模板内容的参数个数
     *
     * @param dtMsg
     * @return
     */
    
    public int getTempParamCount(String dtMsg) {
        int templateCount = 0;
        // 查找短信内容中的参数个数（如果是动态模板短信）
        String eg = "#[pP]_[1-9][0-9]*#";
        Matcher m = Pattern.compile(eg, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(dtMsg);
        while (m.find()) {
            String rstr = m.group();
            rstr = rstr.toUpperCase();
            String pc = rstr.substring(rstr.indexOf("#P_") + 3, rstr.lastIndexOf("#"));
            int pci = Integer.parseInt(pc);
            if (pci > templateCount) {
                templateCount = pci;
            }
        }
        return templateCount;
    }

    /**
     * 设置发送账户跟密码对应的MAP
     *
     * @param userdataList
     * @param request
     */
    
    public void setUserdataMap(List<Userdata> userdataList, HttpServletRequest request) {
        try {
            if (userdataList != null && userdataList.size() > 0) {
                Map<String, String> userPwdMap = new HashMap<String, String>();
                for (Userdata userdata : userdataList) {
                    // key-发送账户,value-发送账户的密码
                    userPwdMap.put(userdata.getUserId(), userdata.getUserPassword());
                }
                request.getSession(false).setAttribute("userPwdMap", userPwdMap);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "设置发送账户信息失败！");
        }
    }

    /**
     * 处理带模板的短信发送
     *
     * @param info 参数内容
     * @param msg  动态模板内容
     * @return
     */
    private String combineContentSg(String info, String msg) {
        // 将回车换行换成一个空格
        String content = msg.replace("\r\n", " ");
        String params[] = info.split(",");
        int size = info.length() - info.replace(",", "").length() + 1;
        StringBuffer mid = null;
        for (int i = 1; i < size + 1; i++) {
            mid = new StringBuffer();
            mid.append("#P_").append(i).append("#");
            if (i < params.length + 1) {
                content = content.replace(mid.toString(), params[i - 1]);
                content = content.replace(mid.toString().replace("#P_", "#p_"), params[i - 1]);
            }
        }
        return content.replaceAll(StaticValue.EXECL_SPLID, ",");
    }

    /**
     * 处理带模板的短信发送(不进行换行处理)
     *
     * @param info 参数内容
     * @param msg  动态模板内容
     * @return
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-1-18 下午05:41:44
     */
    private String combineContent(String info, String msg) {
        String content = msg;
        try {
            String params[] = info.split(",");
            int size = info.length() - info.replace(",", "").length() + 1;
            StringBuffer mid = null;
            for (int i = 1; i < size + 1; i++) {
                mid = new StringBuffer();
                mid.append("#P_").append(i).append("#");
                if (i < params.length + 1) {
                    content = content.replace(mid.toString(), params[i - 1]);
                    content = content.replace(mid.toString().replace("#P_", "#p_"), params[i - 1]);
                }
            }
            return content.replaceAll(StaticValue.EXECL_SPLID, ",");
        } catch (Exception e) {
            EmpExecutionContext.error(e, "不同内容动态模板发送，处理短信内容异常！info:" + info + "，msg:" + msg);
            return null;
        }
    }

    /**
     * 短信内容BASE64编码
     *
     * @param msg 短信内容
     * @return
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-1-18 下午07:30:15
     */
    private String contentBase64Encoder(String msg) {
        try {
            // 短信内容BASE64编码
            String content = new BASE64Encoder().encode(msg.getBytes("GBK"));
            //windows操作系统
            if (StaticValue.getServerSystemType() == 0) {
                // 去回车换行
                content = content.replace("\r\n", "");
            }
            //其他系统
            else {
                // 去回车换行
                content = content.replace("\n", "");
            }
            return content;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "不同内容动态模板发送，短信内容BASE64编码异常！msg:" + msg);
            return null;
        }
    }

    /**
     * 短信内容BASE64解码
     *
     * @param msg 短信内容
     * @return
     * @description
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-1-19 上午11:52:06
     */
    public String contentBase64Decoder(String msg) {
        String content = "";
        try {
            if (msg != null && msg.trim().length() > 0) {
                content = new String(new BASE64Decoder().decodeBuffer(msg));
            }
            return content;
        } catch (IOException e) {
            EmpExecutionContext.error(e, "短信内容BASE64解码异常！msg:" + msg);
            return null;
        }
    }

    /**
     * @param readerList 文件流对象集合
     * @param params     传递参数对象
     * @param lguserid   操作员id
     * @param corpCode   企业编码
     * @throws Exception
     * @description 解析文件流中的号码
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-20 下午03:56:22
     */
    public void parseUploadFilePhone(List<BufferedReader> readerList, PreviewParams params, String lguserid, String corpCode) throws Exception {
        if (readerList == null || readerList.size() < 1) {
            return;
        }
        // 解析号码文件
        String mobile;
        // 每批次的有效号码数，在该批号码写入数据库后重置为默认值
        int perEffCount = 0;
        BufferedReader reader;

        //合法号码文件
        FileOutputStream contentOS = null;
        try {
            // 有效号码
            StringBuffer contentSb = new StringBuffer();
            for (int r = 0; r < readerList.size(); r++) {
                // 如果上传号码大于500w就不允许发送
                if (params.getEffCount() > StaticValue.MAX_PHONE_NUM) {
                    EmpExecutionContext.error("相同内容暂存草稿，有效号码数超过" + StaticValue.MAX_PHONE_NUM + "，corpCode:" + corpCode + "，userid：" + lguserid);
                    throw new EMPException(IErrorCode.A40000);
                }
                reader = readerList.get(r);
                while ((mobile = reader.readLine()) != null) {
                    params.setSubCount(params.getSubCount() + 1);

                    perEffCount++;
                    // 有效号码
                    contentSb.append(mobile).append(line);
                    params.setEffCount(params.getEffCount() + 1);

                    // 一千条存贮一次
                    if (perEffCount >= StaticValue.PER_PHONE_NUM) {
                        if (contentOS == null) {
                            //合法号码文件输出流
                            contentOS = new FileOutputStream(params.getPhoneFilePath()[0], true);
                        }
                        //写入有效号码文件
                        txtFileUtil.repeatWriteToTxtFile(contentOS,  params.getPhoneFilePath()[0], contentSb.toString());
                        contentSb.setLength(0);
                        perEffCount = 0;
                    }
                }
                reader.close();
            }

            if (contentSb.length() > 0) {
                if (contentOS == null) {
                    //合法号码文件输出流
                    contentOS = new FileOutputStream(params.getPhoneFilePath()[0], true);
                }
                // 剩余的有效号码写文件
                txtFileUtil.repeatWriteToTxtFile(contentOS,  params.getPhoneFilePath()[0], contentSb.toString());
                contentSb.setLength(0);
            }

        } catch (EMPException e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            throw e;
        } catch (Exception e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            throw new EMPException(IErrorCode.B20005, e);
        } finally {
            if (contentOS != null) {
                try {
                    contentOS.close();
                } catch (Exception e) {
                    EmpExecutionContext.error(e, "相同内容暂存草稿，解析上传文件，关闭有效号码输出流异常！");
                }
            }
            if (readerList != null) {
                readerList.clear();
                readerList = null;
            }
        }
    }

    /**
     * @param params     传递参数对象
     * @param depIdStr   员工机构ID
     * @param phoneStr   员工号码
     * @param groupStr   员工群组id
     * @param lgcorpcode 企业编码
     * @throws Exception
     * @description 解析手工输入和通讯录的号码
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-20 下午03:54:42
     */
    public void parseAddrAndInputPhone(PreviewParams params, String depIdStr, String phoneStr, String groupStr, String lgcorpcode) throws Exception {
        try {
            StringBuffer contentSb = new StringBuffer();
            //最后一位不为","时,增加","
            if (phoneStr != null && phoneStr.length() > 0 && !",".equals(phoneStr.substring(phoneStr.length() - 1, phoneStr.length()))) {
                phoneStr += ",";
            }
            // 解析号码字符串
            if (depIdStr != null && depIdStr.length() > 0 && !",".equals(depIdStr)) {
                // 通过机构id查找电话
                phoneStr = phoneStr + depBiz.getEmpByDepId(depIdStr, lgcorpcode);

            }
            if (groupStr != null && groupStr.length() > 0 && !",".equals(groupStr)) {
                // 通过群组查找电话
                phoneStr = phoneStr + depBiz.getEmpByGroupStr(groupStr, lgcorpcode);

            }
            if (phoneStr.length() <= 0) {
                return;
            }

            String[] phones = phoneStr.split(",");
            for (String num : phones) {
                params.setSubCount(params.getSubCount() + 1);
                if (num == null) {
                    continue;
                }
                contentSb.append(num).append(line);
                params.setEffCount(params.getEffCount() + 1);
            }

            if (contentSb.length() > 0) {
                txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0], contentSb.toString());
                contentSb.setLength(0);
            }
        } catch (EMPException e) {
            EmpExecutionContext.error(e, "相同内容暂存草稿，解析手工输入和通讯录的号码异常。");
            throw e;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "相同内容暂存草稿，解析手工输入和通讯录的号码异常。");
            throw new EMPException(IErrorCode.B20005, e);
        } finally {
            if (phoneStr != null) {
                phoneStr = null;
            }
        }
    }

    /**
     * @param drafts        草稿箱对象
     * @param params        传递参数对象
     * @param containDraft  是否包含草稿箱文件
     * @param draftFilePath 草稿箱文件相对路径
     * @param draftFileTemp 草稿箱临时文件相对路径
     * @param lgcorpcode    企业编码
     * @throws Exception
     * @description 设置草稿箱号码文件路径
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-20 下午03:49:40
     */
    public void setDraftMobileUrl(LfDrafts drafts, PreviewParams params, String containDraft, String draftFilePath, String draftFileTemp, String lgcorpcode) throws Exception {
        try {
            //不使用草稿箱文件，没新提交号码
            if ((containDraft == null || containDraft.trim().length() < 1) && params.getSubCount() < 1) {
                //号码为空的草稿箱
                drafts.setMobileurl("");
                return;
            }
            //不使用草稿箱文件，有新提交号码
            else if ((containDraft == null || containDraft.trim().length() < 1) && params.getSubCount() > 0) {
                //直接使用生成的新号码文件
                drafts.setMobileurl(params.getPhoneFilePath()[1]);
            }
            //使用草稿箱文件，没新提交号码
            else if (containDraft != null && containDraft.trim().length() > 0 && params.getSubCount() < 1) {
                //使用原来的，不用修改
                drafts.setMobileurl(draftFileTemp);
                //用原来的草稿箱文件，可能不在本地，这里也先下载下来
                downloadFile(drafts.getMobileurl());
                return;
            }
            //使用草稿箱文件，有新提交号码
            else if (containDraft != null && containDraft.trim().length() > 0 && params.getSubCount() > 0) {
                //草稿箱文件内容写入新提交号码文件中
                parseDraftFilePhone(params.getPhoneFilePath()[0], draftFilePath, draftFileTemp, lgcorpcode);
                //使用新号码文件
                drafts.setMobileurl(params.getPhoneFilePath()[1]);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "暂存草稿，读写草稿箱文件，异常"
                    + "。containDraft=" + containDraft
                    + ",draftFilePath=" + draftFilePath
                    + ",lgcorpcode=" + lgcorpcode);
        }
    }

    /**
     * @param phoneFilePath 新号码文件
     * @param draftFilePath 草稿箱文件相对路径
     * @param lgcorpcode    企业编码
     * @description 解析草稿箱文件内容并写入号码文件中
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-20 下午03:51:25
     */
    private void parseDraftFilePhone(String phoneFilePath, String draftFilePath, String draftFileTemp, String lgcorpcode) {
        try {
            if (draftFilePath == null || draftFilePath.trim().length() < 1) {
                EmpExecutionContext.error("暂存草稿，读写草稿箱文件，草稿箱文件路径为空。draftFilePath=" + draftFilePath + ",phoneFilePath=" + phoneFilePath);
                return;
            }
            //获取文件
            File draftFile;
            //获取临时文件
            File draftTempFile = getDraftTempFile(draftFileTemp);
            //源文件
            //String sourceFilePaht;
            //没临时文件，则使用草稿箱文件路径
            if (draftTempFile == null) {
                //sourceFilePaht = draftFilePath;
                //获取文件
                draftFile = getDraftFile(draftFilePath);
            }
            //临时文件路径有值，且跟草稿箱文件路径一样，即表示现在是第一次操作保存，还没产生临时文件，所以使用草稿箱文件路径
            else if (draftFilePath.equals(draftFileTemp)) {
                //sourceFilePaht = draftFilePath;
                //获取文件
                draftFile = getDraftFile(draftFilePath);
            }
            //临时文件路径有值，且跟草稿箱文件路径不同，即表示不是第一次操作保存，之前已产生临时文件，所以使用临时文件路径
            //else if(!draftFilePath.equals(draftFileTemp))
            else {
                //sourceFilePaht = draftFileTemp;
                //获取文件
                draftFile = draftTempFile;
            }
//			//其他情况使用草稿箱文件路径
//			else
//			{
//				//sourceFilePaht = draftFilePath;
//				//获取文件
//				draftFile = getDraftFile(draftFilePath);
//			}

            //获取文件
            //draftFile = getDraftFile(sourceFilePaht);
            if (draftFile == null) {
                EmpExecutionContext.error("暂存草稿，读写草稿箱文件，未找到草稿箱发送文件。draftFilePath=" + draftFilePath + ",draftFileTemp=" + draftFileTemp);
                return;
            }

            //草稿箱文件写到号码文件中
            txtFileUtil.FileWriteToFile(draftFile.getAbsolutePath(), phoneFilePath);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "暂存草稿，读写草稿箱文件，异常"
                    + "。phoneFilePath=" + phoneFilePath
                    + ",draftFilePath=" + draftFilePath
                    + ",draftFileTemp=" + draftFileTemp
                    + ",lgcorpcode=" + lgcorpcode);
        }
    }

    /**
     * @param draftFilePath 草稿箱文件路径
     * @return 返回草稿箱文件对象。没有或者没找到，则返回null
     * @description 获取草稿箱文件对象
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-21 下午06:35:36
     */
    private File getDraftFile(String draftFilePath) {
        try {
            if (draftFilePath == null || draftFilePath.trim().length() < 1) {
                EmpExecutionContext.error("获取草稿箱文件，草稿箱文件路径为空。draftFilePath=" + draftFilePath);
                return null;
            }
            String webRoot = txtFileUtil.getWebRoot();
            File draftFile = new File(webRoot, draftFilePath);
            if (!draftFile.exists() && StaticValue.getISCLUSTER() == 1) {
                CommonBiz comBiz = new CommonBiz();
                String downloadRes = "error";
                //最大尝试次数
                int retryTime = 3;
                while (!"success".equals(downloadRes) && retryTime-- > 0) {
                    downloadRes = comBiz.downloadFileFromFileCenter(draftFilePath);
                }
                if (!"success".equals(downloadRes)) {
                    EmpExecutionContext.error("获取草稿箱文件，草稿箱文件从文件服务器下载失败。draftFilePath=" + draftFilePath);
                }
            }
            if (!draftFile.exists()) {
                EmpExecutionContext.error("获取草稿箱文件，未找到草稿箱发送文件。draftFilePath=" + draftFilePath);
                return null;
            }
            return draftFile;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取草稿箱文件，异常"
                    + "。draftFilePath=" + draftFilePath
            );
            return null;
        }
    }

    /**
     * @param filePath 文件相对路径
     * @description 从文件服务器下载文件
     */
    private void downloadFile(String filePath) {
        try {
            if (filePath == null || filePath.trim().length() < 1) {
                EmpExecutionContext.error("下载文件，传入的文件路径为空。");
                return;
            }
            //非集群，没文件服务器，不用下载
            if (StaticValue.getISCLUSTER() != 1) {
                return;
            }

            String webRoot = txtFileUtil.getWebRoot();
            File file = new File(webRoot, filePath);
            //文件存在，不需要下载
            if (file.exists()) {
                return;
            }

            CommonBiz comBiz = new CommonBiz();
            String downloadRes = "error";
            //最大尝试次数
            int retryTime = 3;
            while (!"success".equals(downloadRes) && retryTime-- > 0) {
                downloadRes = comBiz.downloadFileFromFileCenter(filePath);
            }
            if (!"success".equals(downloadRes)) {
                EmpExecutionContext.error("下载文件，从文件服务器下载失败。FilePath=" + filePath);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "下载文件，异常。FilePath=" + filePath);
        }
    }

    /**
     * @param draftFileTemp 临时文件路径
     * @return
     * @description 获取草稿箱临时文件对象
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-21 下午06:37:08
     */
    private File getDraftTempFile(String draftFileTemp) {
        try {
            if (draftFileTemp == null || draftFileTemp.trim().length() < 1) {
                EmpExecutionContext.error("获取草稿箱临时文件，文件路径为空。draftFileTemp=" + draftFileTemp);
                return null;
            }
            String webRoot = txtFileUtil.getWebRoot();
            File draftFile = new File(webRoot, draftFileTemp);

            if (!draftFile.exists()) {
                EmpExecutionContext.error("获取草稿箱临时文件，未找到文件。draftFilePath=" + draftFileTemp);
                return null;
            }
            return draftFile;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取草稿箱临时文件，异常"
                    + "。draftFileTemp=" + draftFileTemp
            );
            return null;
        }
    }

    /**
     * @param drafts 草稿箱对象
     * @param params 预览参数对象
     * @return 成功返回true
     * @description 保存草稿箱对象
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-20 下午03:53:56
     */
    public boolean saveDrafts(LfDrafts drafts, PreviewParams params) {
        try {
            //使用集群，将文件上传到文件服务器
            if (StaticValue.getISCLUSTER() == 1 && drafts.getMobileurl() != null && drafts.getMobileurl().trim().length() > 0) {
                // 本地文件地址
                String fileUrl = txtFileUtil.getWebRoot() + drafts.getMobileurl();
                File file = new File(fileUrl);
                //文件存在，则上传
                if (file.exists()) {
                    //上传文件到文件服务器
                    CommonBiz comBiz = new CommonBiz();
                    boolean upFileRes = false;
                    //最大尝试次数
                    int retryTime = 3;
                    while (!upFileRes && retryTime-- > 0) {
                        upFileRes = comBiz.upFileToFileServer(drafts.getMobileurl());
                    }
                    if (!upFileRes) {
                        EmpExecutionContext.error("暂存草稿箱，草稿箱文件上传文件到服务器失败。错误码：" + IErrorCode.B20023);
                        return false;
                    }
                }
            }

            //新建
            if (drafts.getId() == null) {
                Long id = baseBiz.addObjReturnId(drafts);
                if (id != null && id > 0) {
                    drafts.setId(id);
                    return true;
                }
                return false;
            }
            //修改
            else {
                boolean result = baseBiz.updateObj(drafts);
                return result;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "暂存草稿箱，保存草稿箱，异常。");
            return false;
        }
    }

    /**
     * @param readerList 上传文件流对象集合
     * @param params     传递参数对象
     * @param lguserid   当前登录操作员id
     * @param corpCode   企业编码
     * @throws Exception
     * @description 不同内容群发，保存草稿
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-22 下午02:32:31
     */
    public void parseDsmUploadFile(List<BufferedReader> readerList, PreviewParams params, String lguserid, String corpCode) throws Exception {
        if (readerList == null || readerList.size() < 1) {
            return;
        }
        try {
            BufferedReader reader;
            // 解析号码文件
            String tmp;
            // 每批次的有效号码数，在该批号码写入数据库后重置为默认值
            int perEffCount = 0;
            // 有效号码
            StringBuffer contentSb = new StringBuffer();
            for (int r = 0; r < readerList.size(); r++) {
                // 如果上传号码大于500w就不允许发送
                if (params.getEffCount() > StaticValue.MAX_PHONE_NUM) {
                    EmpExecutionContext.error("不同内容暂存草稿，有效号码数超过" + StaticValue.MAX_PHONE_NUM + "，corpCode:" + corpCode + "，userid：" + lguserid);
                    throw new EMPException(IErrorCode.A40000);
                }
                reader = readerList.get(r);
                while ((tmp = reader.readLine()) != null) {
                    params.setSubCount(params.getSubCount() + 1);
                    // 有效号码
                    params.setEffCount(params.getEffCount() + 1);
                    contentSb.append(tmp).append(line);
                    perEffCount++;

                    // 一千条存贮一次
                    if (perEffCount >= StaticValue.PER_PHONE_NUM) {
                        //写入有效号码文件
                        txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0], contentSb.toString());
                        contentSb.setLength(0);
                        perEffCount = 0;
                    }
                }
                reader.close();
            }

            if (contentSb.length() > 0) {
                // 剩余的有效号码写文件
                txtFileUtil.writeToTxtFile(params.getPhoneFilePath()[0], contentSb.toString());
                contentSb.setLength(0);
            }
        } catch (EMPException e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            throw e;
        } catch (Exception e) {
            txtFileUtil.deleteFile(params.getPhoneFilePath()[0]);
            EmpExecutionContext.error(e, lguserid, corpCode);
            throw new EMPException(IErrorCode.B20005, e);
        } finally {
            if (readerList != null) {
                readerList.clear();
                readerList = null;
            }
        }
    }

    /**
     * @param draftId       草稿箱id
     * @param draftFileTemp 草稿箱临时文件相对路径
     * @param lguserid      登录操作员id
     * @param corpCode      企业编码
     * @return 1：成功； -1：参数为空； -2：草稿箱对象不存在；-3：草稿箱文件url为空；-4：草稿箱文件对象为空；-9999：异常
     * @description 设置草稿箱文件流到readerList
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-26 下午03:26:44
     */
    public int setDraftReader(List<BufferedReader> readerList, String draftId, String draftFileTemp, String lguserid, String corpCode) {
        //草稿箱id为空
        if (draftId == null || draftId.trim().length() < 1) {
            EmpExecutionContext.error("设置草稿箱文件流，draftId为空。"
                    + "。draftId=" + draftId
                    + ",draftFileTemp=" + draftFileTemp
                    + ",lguserid=" + lguserid
                    + ",corpCode=" + corpCode
            );
            return -1;
        }

        //加载草稿箱对象
        LfDrafts drafts = getDrafts(draftId);
        if (drafts == null) {
            EmpExecutionContext.error("设置草稿箱文件流，获取不到草稿箱对象。"
                    + "。draftId=" + draftId
                    + ",draftFileTemp=" + draftFileTemp
                    + ",lguserid=" + lguserid
                    + ",corpCode=" + corpCode
            );
            return -2;
        }

        //获取文件对象
        File draftFile;
        //临时文件对象
        File draftTempFile = getDraftTempFile(draftFileTemp);
        //没找到临时文件，则拿草稿箱的文件
        if (draftTempFile == null) {
            if (drafts.getMobileurl() == null || drafts.getMobileurl().trim().length() < 1) {
                EmpExecutionContext.error("设置草稿箱文件流，草稿箱文件路径为空"
                        + "。draftId=" + drafts.getId()
                        + ",draftFileTemp=" + draftFileTemp
                        + ",title=" + drafts.getTitle()
                        + ",lguserid=" + lguserid
                        + ",corpCode=" + corpCode
                );
                return -3;
            }
            //获取文件对象
            draftFile = getDraftFile(drafts.getMobileurl());
        } else {
            //获取文件对象
            draftFile = draftTempFile;
        }

        if (draftFile == null) {
            EmpExecutionContext.error("设置草稿箱文件流，草稿箱文件为空"
                    + "。draftId=" + draftId
                    + ",draftFileTemp=" + draftFileTemp
                    + ",lguserid=" + lguserid
                    + ",corpCode=" + corpCode
            );
            return -4;
        }

        try {
            //草稿箱文件的文件流对象
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(draftFile), "GBK"));
            readerList.add(reader);
            return 1;
        } catch (Exception e) {
            EmpExecutionContext.error("设置草稿箱文件流，异常"
                    + "。draftId=" + draftId
                    + ",draftFileTemp=" + draftFileTemp
                    + ",lguserid=" + lguserid
                    + ",corpCode=" + corpCode
            );
            EmpExecutionContext.error(e, lguserid, corpCode);
            return -9999;
        }
    }

    /**
     * @param draftId 草稿箱id
     * @return 草稿箱对象
     * @description 获取草稿箱对象
     * @author huangzb <huangzb@126.com>
     * @datetime 2016-1-26 下午02:41:49
     */
    public LfDrafts getDrafts(String draftId) {
        try {
            if (draftId == null || draftId.trim().length() < 1) {
                return null;
            }

            BaseBiz baseBiz = new BaseBiz();
            //加载存在的草稿箱对象
            LfDrafts draft = baseBiz.getById(LfDrafts.class, draftId);
            return draft;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取草稿箱对象，异常。draftId=" + draftId);
            return null;
        }
    }

}
