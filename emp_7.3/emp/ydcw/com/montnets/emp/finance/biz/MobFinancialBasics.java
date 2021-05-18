package com.montnets.emp.finance.biz;

import com.etool.ETool;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.finance.util.BaseRegVerification;
import com.montnets.emp.finance.util.BaseRegVerificationImpl;
import com.montnets.emp.finance.util.FinanceBasicData;
import com.montnets.emp.finance.util.YdcwErrorStatus;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.util.ChangeCharset;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.StringUtils;
import com.montnets.emp.util.TxtFileUtil;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.fileupload.FileItem;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 文档业务处理,包括各类验证,消息加密等
 *
 * @author Jinny.Ding (djhsun@sina.com)
 * @version 1.0
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @date Mar 29, 2012
 */
public class MobFinancialBasics {

    private static final String UTF_8_NO_BOM = "UTF-8_NO_BOM";
    private final ChangeCharset charsetUtil = new ChangeCharset();

    protected YdcwBiz ydcwBiz = new YdcwBiz();
    protected HttpSmsSend hss = new HttpSmsSend();
    protected SendFinancialSMS ssms = new SendFinancialSMS();
    protected BaseRegVerification ibrv = new BaseRegVerificationImpl();
    protected ElecPayrollCommon epc = new ElecPayrollCommon();
    // 每次写入文档的短信条数
    private static final int SEND_MSG_MAX = 5000;
    // 短信预览条数
    private static final int PREVIEW_MSG_MAX = 10;
    private final BlackListAtom blackListBiz = new BlackListAtom();
    private final WgMsgConfigBiz haoduanBiz = new WgMsgConfigBiz();
    private final KeyWordAtom keyWordAtom = new KeyWordAtom();
    private final String line = StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);
    private final PhoneUtil phoneUtil = new PhoneUtil();

    /**
     * @param fData
     * @return int
     * @throws Exception
     * @author Jinny.Ding
     * @date May 14, 2012
     */
    @SuppressWarnings("unchecked")
    public int[] resolveDocument(FinanceBasicData fData, String empLangName) throws Exception {
        PrintWriter pw = null;
        try {

            if (fData == null) {
                return null;
            }
            // 1.却的各变量值
            HttpSession session = fData.getSession();
            String busCode = fData.getBusCode();
            ElecPayrollCommon epcObject = fData.getEpcObject();
            FileItem fileItem = fData.getFileItem();
            String spAccount = fData.getSpAccount();
            String templateIds = fData.getTemplateIds();
            String corpCode = fData.getCorpCode();
            String template = fData.getTemplate();
            String verifycode = fData.getVerifycode();
            List<String> paraList = null;
            if (fData.getList() != null) {
                paraList = fData.getList();
            }
            String illegalFormat = "zh_HK".equals(empLangName) ? "Illegal format：" : "格式非法：";
            String blacklistNumber = "zh_HK".equals(empLangName) ? "Blacklist number：" : "zh_TW".equals(empLangName) ? "黑名單號碼：" : "黑名单号码：";
            String includeKeywords = "zh_HK".equals(empLangName) ? "Include keywords：" : "zh_TW".equals(empLangName) ? "包含關鍵字：" : "包含关键字：";
            String repeatedNumber = "zh_HK".equals(empLangName) ? "Repeated number：" : "zh_TW".equals(empLangName) ? "重複號碼：" : "重复号码：";
            // 2. 判断并存储是哪种操作
            session.setAttribute("txtOrExcel", fData.getTextOrExcel());

            List<String> list = new ArrayList<String>();
            // 存储手机号码+短信内容
            String[] smsArr = null;
            // 记录异常短信
            Map<String, String> erMap = new HashMap<String, String>();
            // 记录预览数据
            List<String[]> previewList = new ArrayList<String[]>();
            // 记录统计数据
            Map<Integer, String> countMap = new HashMap<Integer, String>();
            BufferedReader reader = null;
            String tmp = "";
            //总数
            int total = 0;
            //有效数
            int effs = 0;
            //重复数
            int sames = 0;
            //黑名单
            int bls = 0;
            //关键字
            int keywords = 0;
            //格式异常(参数不对)
            int formats = 0;
            //写入文件数
            int wfilerows = 0;
            //网关实发数
            int routes = 0;
            //模板参数个数
            int countPara = 0;

            // 3.取得临时文件存储路径
            String path = fData.getUrl() + File.separator + fData.getTempFileName() + ".txt";
            String badFilePath = fData.getUrl() + File.separator + fData.getTempFileName() + "_bad.txt";
            session.setAttribute("badFilePath", fData.getFilePath());
            // 7.如果不存在同名文件则创建，如果存在同名文件，则删除后再创建。
            File file = new File(path);
            if (!file.exists()) {
                boolean state = file.createNewFile();
                if (!state) {
                    EmpExecutionContext.error("创建文件失败");
                }
            } else {
                if (file.isFile()) {
                    boolean flag = file.delete();
                    if (!flag) {
                        EmpExecutionContext.error("删除文件失败！");
                    }
                }
                boolean state = file.createNewFile();
                if (!state) {
                    EmpExecutionContext.error("创建文件失败");
                }
            }

            File badFile = new File(badFilePath);
            if (!badFile.exists()) {
                boolean state = badFile.createNewFile();
                if (!state) {
                    EmpExecutionContext.error("创建文件失败");
                }
            } else {
                if (badFile.isFile()) {
                    boolean state = badFile.delete();
                    if (!state) {
                        EmpExecutionContext.error("创建文件失败");
                    }
                }
                boolean state = badFile.createNewFile();
                if (!state) {
                    EmpExecutionContext.error("创建文件失败");
                }
            }

            // 4.取得开启对象
            FileWriter fw = new FileWriter(path, true);
            pw = new PrintWriter(fw);

            //PrintWriter badPw = new PrintWriter(new FileWriter(badFilePath, true));

            StringBuffer badContentSb = new StringBuffer();
            // 5.参数下标,用于匹配参数
            String[] tempArr = null;
            if (templateIds != null) {
                tempArr = templateIds.split("\\?");
            }

            int[] countArr = new int[3];
            try {
                // 6.获取关键字列表

                // 7.获取号段列表
                String[] haoduan = haoduanBiz.getHaoduan();
                if (haoduan.length == 0) {
                    session.setAttribute("ErrorReport", "ECWD100");
                    return null;
                }
                String yd = haoduan[0];
                String lt = haoduan[1];
                String dx = haoduan[2];


                // 6.获得模板参数个数
                countPara = epcObject.countParameter(template);
                // 8.遍历参数行,将有参数据行存入集合
                Set<String> setTel = new HashSet<String>();
                Set<String> setTel2 = new HashSet<String>();
                // 9.检查内存数据是否存在
                if (fileItem == null && fData.getTextOrExcel() != 3) {
                    session.setAttribute("ErrorReport", "ECWV101");
                    return null;
                }

                //fileItem不能为空
                if (fileItem == null && fData.getTextOrExcel() != 3) {
                    session.setAttribute("ErrorReport", "ECWV101");
                    return null;
                }

                // 10.模板参数下标数字
                if ("".equals(templateIds) ) {
                    session.setAttribute("ErrorReport", "ECWV101");
                    return null;
                }
                // 11.获取路由信息
                Map<String, Map> map = epc.getRouteBySpUserid(spAccount);
                ETool etool = new ETool();
                String mobile = "";
                switch (fData.getTextOrExcel()) {
                    case 1:
                        // document type is txt
                        // 字符编码
					/*String charset = FileUtils.get_charset(fileItem
							.getInputStream());*/
					    //获取文件的编码格式
                        String charset = charsetUtil.get_charset(fileItem
                                .getInputStream());
                        //是否为UTF-8_NO_BOM编码
                        boolean isUtf8NoBom = UTF_8_NO_BOM.equals(charset);
                        //是UTF-8_NO_BOM编码,则编码设置为UTF-8
                        if(isUtf8NoBom){
                            charset = ChangeCharset.UTF_8;
                        }
                        reader = new BufferedReader(new InputStreamReader(fileItem
                                .getInputStream(), charset));
                        if (charset.startsWith("UTF-")&& !isUtf8NoBom) {
                            reader.read(new char[1]);
                        }
                        while ((tmp = reader.readLine()) != null) {
                            try {
                                if (tmp == null || "".equals(tmp.trim())) {
                                    continue;
                                }
                            } catch (Exception e) {
                                EmpExecutionContext.error(e, "处理移动财务上传时为空行");
                            }
                            total++;
//						wfilerows++;
                            // a.验证异常数据
                            int firstLabel = tmp.indexOf(",");
                            if (firstLabel == -1) {
//							wfilerows--;
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tmp);
                                badContentSb.append(illegalFormat).append(tmp).append(line);
                                continue;
                            }
                            String tel = tmp.substring(0, firstLabel);
                            mobile = StringUtils.parseMobile(tel);
                            String parameter = tmp.substring(firstLabel + 1);

                            int docParas = 0;
                            if (!"".equals(parameter)) {
                                docParas = (parameter.split(",").length);
                            }
                            //==========================TXT==========================
                            // b.判断手机号码是否合法
                            //haoduan
                            if (phoneUtil.getPhoneType(mobile, haoduan) == -1) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }
                            // c.验证文档参数和模板参数是否一致
                            if (docParas < countPara) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }
                            // d.检查模板参数是否存在于文档参数数据行中
                            if (tempArr != null) {
                                if (!"undefined".equals(tempArr[tempArr.length - 1])
                                        && ((parameter.split(",").length) < (Integer
                                        .parseInt(tempArr[tempArr.length - 1])))) {
                                    formats++;
                                    //badPw.flush();
                                    //badPw.println("格式非法："+tel);
                                    badContentSb.append(illegalFormat).append(tel).append(line);
                                    continue;
                                }
                            }

                            // d.检查号段
                            boolean isHas = true;
                            if (yd.indexOf(mobile.substring(0, 3)) != -1) {
                                isHas = false;
                            }
                            if (isHas == true && (lt.indexOf(mobile.substring(0, 3)) != -1)) {
                                isHas = false;
                            }
                            if (isHas == true && (dx.indexOf(mobile.substring(0, 3)) != -1)) {
                                isHas = false;
                            }
                            if (isHas) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }
                            // e.过滤黑名单
                            if (haoduan.length > 0) {
                                if (blackListBiz.checkBlackList(corpCode, mobile, busCode)) {
                                    bls++;
                                    //badPw.flush();
                                    //badPw.println("黑名单号码："+tel);
                                    badContentSb.append(blacklistNumber).append(tel).append(line);
                                    continue;
                                }
                            }
                            // f.拼接短信内容字符串
                            String payMessage = hss.combineContentSg(parameter, template);

                            //过滤空格
                            if (payMessage == null || (payMessage != null && payMessage.trim().length() <= 0)) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }

                            int filterKeyRes = keyWordAtom.filterKeyWord(payMessage.toUpperCase(), fData.getCorpCode());
                            // g.过滤关键字(目下的是只要有关键字该条短信就不发送)
                            if (filterKeyRes == 1) {
                                keywords++;
                                //badPw.flush();
                                //badPw.println("包含关键字："+tel);
                                badContentSb.append(includeKeywords).append(tel).append(line);
                                continue;
                            }
                            // h.短信内容不得超过640个字,超过则取消发送
                            if (!ibrv.isByteLengthWithFinancialMsg(payMessage)) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }
                            //模拟网关统计实发短信数量
                            if (setTel2.add(mobile)) {
                                int routeRow = epc.countOfSms(spAccount, mobile + "," + payMessage, haoduan, map);
                                routes = routes + routeRow;
                            }
                            //对财务短信内容进行加密
                            String[] result = null;
                            try {
                                //result = new ETool().EncryptMsg(spAccount, verifycode, new String(payMessage.getBytes(), "GBK"), 0);
                                result = etool.EncryptMsg(spAccount, verifycode, new String(payMessage.getBytes("GBK"), "GBK"), 0);
                            } catch (Exception e) {
                                EmpExecutionContext.error(e, "财务短信加密异常!");
                                break;
                            }
                            //int countRows = 0;
                            if (Integer.parseInt(result[0]) > 0) {
                                String fileContent = mobile + "," + result[1];
                                //去掉重复的手机号码
                                if (setTel.contains(mobile)) {
                                    sames++;
                                    //badPw.flush();
                                    //badPw.println("重复号码："+tel);
                                    badContentSb.append(repeatedNumber).append(tel).append(line);
                                }

                                if (setTel.add(mobile)) {
                                    wfilerows++;
                                    effs++;
                                    list.add(fileContent);
                                    //存储预览数据
                                    if (effs <= PREVIEW_MSG_MAX) {
                                        smsArr = new String[2];
                                        smsArr[0] = mobile;
                                        smsArr[1] = payMessage;
                                        previewList.add(smsArr);
                                    }
                                }
                                //写入临时文档
                                if (wfilerows > 0 && wfilerows % SEND_MSG_MAX == 0) {
                                    try {
                                        pw.flush();
                                        for (int i = 0; i < list.size(); i++) {
                                            pw.println(list.get(i));
                                        }
//									wfilerows = 0;
                                        list = new ArrayList<String>();
                                    } catch (Exception e) {
                                        session.setAttribute("ErrorReport", "ECWB102");
                                        EmpExecutionContext.error(e, "写入临时文档异常!");// 删除已写的短信
                                        break;
                                    }
                                }
                            } else {
                                session.setAttribute("ErrorReport", "ECWB111");
                                break;
                            }
                            //====================================================

                        }// end while()
                        break;
                    case 2: // document type is xls
                        Workbook wb = Workbook.getWorkbook(fileItem.getInputStream());
                        Sheet sh = wb.getSheet(0);

                        for (int k = 0; k < sh.getRows(); k++) {
                            total++;
//						wfilerows++;
                            Cell[] cells = sh.getRow(k);
                            // a.异常数据
                            if (cells.length == 0) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法：");
                                badContentSb.append(illegalFormat).append(line);
                                continue;
                            }
                            String tel = cells[0].getContents();
                            mobile = StringUtils.parseMobile(tel);
                            String[] paramArr = epc.getMessageParameter(cells);
                            int docParas = 0;
                            String paramStr = "";
                            for (int i = 0; i < paramArr.length; i++) {
                                paramStr += paramArr[i];
                            }
                            if (!"".equals(paramStr)) {
                                docParas = cells.length - 1;
                            }
                            //==========================EXCEL==========================
                            // b.判断手机号码是否合法
                            if (phoneUtil.getPhoneType(mobile, haoduan) == -1) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }

                            // c.验证文档参数和模板参数是否一致
                            if (docParas < countPara) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }

                            // d.检查模板参数是否存在于文档参数数据行中
                            if (tempArr != null) {
                                if (!"undefined".equals(tempArr[tempArr.length - 1])
                                        && ((paramArr.length) < (Integer
                                        .parseInt(tempArr[tempArr.length - 1])))) {
                                    formats++;
                                    //badPw.flush();
                                    //badPw.println("格式非法："+tel);
                                    badContentSb.append(illegalFormat).append(tel).append(line);
                                    continue;
                                }
                            }

                            // d.检查号段
                            boolean isHas = true;
                            if (yd.indexOf(mobile.substring(0, 3)) != -1) {
                                isHas = false;
                            }
                            if (isHas == true && (lt.indexOf(mobile.substring(0, 3)) != -1)) {
                                isHas = false;
                            }
                            if (isHas == true && (dx.indexOf(mobile.substring(0, 3)) != -1)) {
                                isHas = false;
                            }
                            if (isHas) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }
                            // e.过滤黑名单
                            if (haoduan.length > 0) {
                                if (blackListBiz.checkBlackList(corpCode, mobile, busCode)) {
                                    bls++;
                                    //badPw.flush();
                                    //badPw.println("黑名单号码："+tel);
                                    badContentSb.append(blacklistNumber).append(tel).append(line);
                                    continue;
                                }
                            }
                            // f.拼接短信内容字符串
                            String payMessage = hss.combineContentSg2(paramArr, template);

                            //过滤空格
                            if (payMessage == null || (payMessage != null && payMessage.trim().length() <= 0)) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }

                            int filterKeyRes = keyWordAtom.filterKeyWord(payMessage.toUpperCase(), fData.getCorpCode());
                            // g.过滤关键字(目下的是只要有关键字该条短信就不发送)
                            if (filterKeyRes == 1) {
                                keywords++;
                                //badPw.flush();
                                //badPw.println("包含关键字："+tel);
                                badContentSb.append(includeKeywords).append(tel).append(line);
                                continue;
                            }
                            // h.短信内容不得超过640个字节,超过则取消发送
                            if (!ibrv.isByteLengthWithFinancialMsg(payMessage)) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }
                            //模拟网关统计实发短信数量
                            if (setTel2.add(mobile)) {
                                int routeRow = epc.countOfSms(spAccount, mobile + "," + payMessage, haoduan, map);
                                routes = routes + routeRow;
                            }
                            //对财务短信内容进行加密
                            String[] result = null;
                            try {
                                //result = new ETool().EncryptMsg(spAccount, verifycode, new String(payMessage.getBytes(), "GBK"), 0);
                                result = etool.EncryptMsg(spAccount, verifycode, new String(payMessage.getBytes("GBK"), "GBK"), 0);
                            } catch (Exception e) {
                                EmpExecutionContext.error(e, "财务短信内容进行加密异常!");
                                break;
                            }
                            if (Integer.parseInt(result[0]) > 0) {
                                String fileContent = mobile + "," + result[1];
                                //去掉重复的手机号码
                                if (setTel.contains(mobile)) {
                                    sames++;
                                    //badPw.flush();
                                    //badPw.println("重复号码："+tel);
                                    badContentSb.append(repeatedNumber).append(tel).append(line);
                                }
                                if (setTel.add(mobile)) {
                                    wfilerows++;
                                    effs++;
                                    list.add(fileContent);
                                    // 存储预览数据
                                    if (effs <= PREVIEW_MSG_MAX) {
                                        smsArr = new String[2];
                                        smsArr[0] = mobile;
                                        smsArr[1] = payMessage;
                                        previewList.add(smsArr);
                                    }
                                }
                                //写入临时文档
                                if (wfilerows > 0 && wfilerows % SEND_MSG_MAX == 0) {
                                    try {
                                        pw.flush();
                                        for (int i = 0; i < list.size(); i++) {
                                            pw.println(list.get(i));
                                        }
//									wfilerows = 0;
                                        list = new ArrayList<String>();
                                    } catch (Exception e) {
                                        session.setAttribute("ErrorReport", "ECWB102");
                                        EmpExecutionContext.error(e, "写入临时文档异常!");// 删除已写的短信
                                        break;
                                    }
                                }
                            } else {
                                session.setAttribute("ErrorReport", "ECWB111");
                                break;
                            }
                            //====================================================
                        }// end for()

                        break;

                    case 3:
                        // 手工录入操作
                        for (int i = 0; i < paraList.size(); i++) {
                            total++;
//						wfilerows++;
                            String paraValue = (String) paraList.get(i);
                            String[] paraArr = (String[]) paraValue.split("&");
                            if (paraArr.length <= 0) {
                                formats++;
//							wfilerows--;
                                //badPw.flush();
                                //badPw.println("格式非法：");
                                badContentSb.append(illegalFormat).append(line);
                                continue;
                            }
                            // a.判断手机号码是否合法
                            String tel = paraArr[0];
                            String parameter = epcObject.spliceMessageParameter(paraArr);
                            //==========================手工录入==========================
                            // b.判断手机号码是否合法
                            if (phoneUtil.getPhoneType(tel, haoduan) == -1) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }
                            // d.检查模板参数是否存在于文档参数数据行中
                            if (tempArr != null) {
                                if (!"undefined".equals(tempArr[tempArr.length - 1])
                                        && ((parameter.split("&").length) < (Integer
                                        .parseInt(tempArr[tempArr.length - 1])))) {
                                    formats++;
                                    //badPw.flush();
                                    //badPw.println("格式非法："+tel);
                                    badContentSb.append(illegalFormat).append(tel).append(line);
                                    continue;
                                }
                            }

                            // d.检查号段
                            boolean isHas = true;
                            if (yd.indexOf(tel.substring(0, 3)) != -1) {
                                isHas = false;
                            }
                            if (isHas == true && (lt.indexOf(tel.substring(0, 3)) != -1)) {
                                isHas = false;
                            }
                            if (isHas == true && (dx.indexOf(tel.substring(0, 3)) != -1)) {
                                isHas = false;
                            }
                            if (isHas) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }

                            // e.过滤黑名单
                            if (haoduan.length > 0) {
                                if (blackListBiz.checkBlackList(corpCode, tel, busCode)) {
                                    bls++;
                                    //badPw.flush();
                                    //badPw.println("黑名单号码："+tel);
                                    badContentSb.append(blacklistNumber).append(tel).append(line);
                                    continue;
                                }
                            }
                            // f.拼接短信内容字符串
                            String payMessage = combineContentSGBaseManual(parameter, template);
                            int filterKeyRes = keyWordAtom.filterKeyWord(payMessage.toUpperCase(), fData.getCorpCode());
                            // g.过滤关键字(目下的是只要有关键字该条短信就不发送)
                            if (filterKeyRes == 1) {
                                keywords++;
                                //badPw.flush();
                                //badPw.println("包含关键字："+tel);
                                badContentSb.append(includeKeywords).append(tel).append(line);
                                continue;
                            }
                            // h.短信内容不得超过640个字节,超过则取消发送
                            if (!ibrv.isByteLengthWithFinancialMsg(payMessage)) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }
                            //模拟网关统计实发短信数量
                            if (setTel2.add(tel)) {
                                int routeRow = epc.countOfSms(spAccount, tel + "," + payMessage, haoduan, map);
                                routes = routes + routeRow;
                            }
                            //对财务短信内容进行加密
                            String[] result = null;
                            try {
                                //result = new ETool().EncryptMsg(spAccount, verifycode, new String(payMessage.getBytes(), "GBK"), 0);
                                result = etool.EncryptMsg(spAccount, verifycode, new String(payMessage.getBytes("GBK"), "GBK"), 0);
                            } catch (Exception e) {
                                EmpExecutionContext.error(e, "财务短信内容进行加密异常!");
                                break;
                            }
                            if (Integer.parseInt(result[0]) > 0) {
                                String fileContent = tel + "," + result[1];
                                //去掉重复的手机号码
                                if (setTel.contains(tel)) {
                                    sames++;
                                    //badPw.flush();
                                    //badPw.println("重复号码："+tel);
                                    badContentSb.append(repeatedNumber).append(tel).append(line);
                                }
                                if (setTel.add(tel)) {
                                    wfilerows++;
                                    effs++;
                                    list.add(fileContent);
                                    // 存储预览数据
                                    if (effs <= PREVIEW_MSG_MAX) {
                                        smsArr = new String[2];
                                        smsArr[0] = tel;
                                        smsArr[1] = payMessage;
                                        previewList.add(smsArr);
                                    }
                                }
                                // 写入临时文档
                                if (wfilerows > 0 && wfilerows % SEND_MSG_MAX == 0) {
                                    try {
                                        pw.flush();
                                        for (int j = 0; j < list.size(); j++) {
                                            pw.println(list.get(j));
                                        }
//									wfilerows = 0;
                                        list = new ArrayList<String>();
                                    } catch (Exception e) {
                                        session.setAttribute("ErrorReport", "ECWB102");
                                        EmpExecutionContext.error(e, "写入临时文档异常!");// 删除已写的短信
                                        break;
                                    }
                                }
                            } else {
                                session.setAttribute("ErrorReport", "ECWB111");
                                //System.out.println("Mobile financial(Manual input) gateway returns the value anomaly.  result[0]:"+result[0]);
                                break;
                            }
                            //====================================================
                        }
                        break;
                    case 4: // document type is xls
                        DecimalFormat df = new DecimalFormat("########");
                        XSSFWorkbook xwb = new XSSFWorkbook(fileItem.getInputStream());
                        XSSFSheet sheet = xwb.getSheetAt(0);

                        for (int k = 0; k < sheet.getPhysicalNumberOfRows(); k++) {
                            total++;
//						wfilerows++;
                            XSSFRow row = sheet.getRow(k);
                            int cellNum = row.getPhysicalNumberOfCells();
                            //Cell[] cells = row.
                            // a.异常数据
                            if (cellNum == 0) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法：");
                                badContentSb.append(illegalFormat).append(line);
                                continue;
                            }
                            String tel = df.format(row.getCell(0).getNumericCellValue());
                            mobile = StringUtils.parseMobile(tel);
                            //	String[] paramArr = epc.getMessageParameter(cells);
                            String[] paramArr = getCell(row);
                            int docParas = 0;
                            String paramStr = "";
                            for (int i = 0; i < paramArr.length; i++) {
                                paramStr += paramArr[i];
                            }
                            if (!"".equals(paramStr)) {
                                docParas = cellNum - 1;
                            }
                            //==========================EXCEL==========================
                            // b.判断手机号码是否合法
                            if (phoneUtil.getPhoneType(mobile, haoduan) == -1) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }

                            // c.验证文档参数和模板参数是否一致
                            if (docParas < countPara) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }

                            // d.检查模板参数是否存在于文档参数数据行中
                            if (tempArr != null) {
                                if (!"undefined".equals(tempArr[tempArr.length - 1])
                                        && ((paramArr.length) < (Integer
                                        .parseInt(tempArr[tempArr.length - 1])))) {
                                    formats++;
                                    //badPw.flush();
                                    //badPw.println("格式非法："+tel);
                                    badContentSb.append(illegalFormat).append(tel).append(line);
                                    continue;
                                }
                            }

                            // d.检查号段
                            boolean isHas = true;
                            if (yd.indexOf(mobile.substring(0, 3)) != -1) {
                                isHas = false;
                            }
                            if (isHas == true && (lt.indexOf(mobile.substring(0, 3)) != -1)) {
                                isHas = false;
                            }
                            if (isHas == true && (dx.indexOf(mobile.substring(0, 3)) != -1)) {
                                isHas = false;
                            }
                            if (isHas) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }
                            // e.过滤黑名单
                            if (haoduan.length > 0) {
                                if (blackListBiz.checkBlackList(corpCode, mobile, busCode)) {
                                    bls++;
                                    //badPw.flush();
                                    //badPw.println("黑名单号码："+tel);
                                    badContentSb.append(blacklistNumber).append(tel).append(line);
                                    continue;
                                }
                            }
                            // f.拼接短信内容字符串
                            String payMessage = hss.combineContentSg2(paramArr, template);

                            //过滤空格
                            if (payMessage == null || (payMessage != null && payMessage.trim().length() <= 0)) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }

                            int filterKeyRes = keyWordAtom.filterKeyWord(payMessage.toUpperCase(), fData.getCorpCode());
                            // g.过滤关键字(目下的是只要有关键字该条短信就不发送)
                            if (filterKeyRes == 1) {
                                keywords++;
                                //badPw.flush();
                                //badPw.println("包含关键字："+tel);
                                badContentSb.append(includeKeywords).append(tel).append(line);
                                continue;
                            }
                            // h.短信内容不得超过640个字节,超过则取消发送
                            if (!ibrv.isByteLengthWithFinancialMsg(payMessage)) {
                                formats++;
                                //badPw.flush();
                                //badPw.println("格式非法："+tel);
                                badContentSb.append(illegalFormat).append(tel).append(line);
                                continue;
                            }
                            //模拟网关统计实发短信数量
                            if (setTel2.add(mobile)) {
                                int routeRow = epc.countOfSms(spAccount, mobile + "," + payMessage, haoduan, map);
                                routes = routes + routeRow;
                            }
                            //对财务短信内容进行加密
                            String[] result = null;
                            try {
                                //result = new ETool().EncryptMsg(spAccount, verifycode, new String(payMessage.getBytes(), "GBK"), 0);
                                result = etool.EncryptMsg(spAccount, verifycode, new String(payMessage.getBytes("GBK"), "GBK"), 0);
                            } catch (Exception e) {
                                EmpExecutionContext.error(e, "财务短信内容进行加密异常!");
                                break;
                            }
                            if (Integer.parseInt(result[0]) > 0) {
                                String fileContent = mobile + "," + result[1];
                                //去掉重复的手机号码
                                if (setTel.contains(mobile)) {
                                    sames++;
                                    //badPw.flush();
                                    //badPw.println("重复号码："+tel);
                                    badContentSb.append(repeatedNumber).append(tel).append(line);
                                }
                                if (setTel.add(mobile)) {
                                    wfilerows++;
                                    effs++;
                                    list.add(fileContent);
                                    // 存储预览数据
                                    if (effs <= PREVIEW_MSG_MAX) {
                                        smsArr = new String[2];
                                        smsArr[0] = mobile;
                                        smsArr[1] = payMessage;
                                        previewList.add(smsArr);
                                    }
                                }
                                //写入临时文档
                                if (wfilerows > 0 && wfilerows % SEND_MSG_MAX == 0) {
                                    try {
                                        pw.flush();
                                        for (int i = 0; i < list.size(); i++) {
                                            pw.println(list.get(i));
                                        }
//									wfilerows = 0;
                                        list = new ArrayList<String>();
                                    } catch (Exception e) {
                                        session.setAttribute("ErrorReport", "ECWB102");
                                        EmpExecutionContext.error(e, "写入临时文档异常!");// 删除已写的短信
                                        break;
                                    }
                                }
                            } else {
                                session.setAttribute("ErrorReport", "ECWB111");
                                break;
                            }
                            //====================================================
                        }// end for()

                        break;
                    default:
                        break;
                }
                // 如果FOR循环结束后,数据行少于MAX的批量存储
                try {
                    if (wfilerows != 0) {
                        pw.flush();
                        if (list.size() != 0) {
                            for (int i = 0; i < list.size(); i++) {
                                pw.println(list.get(i));
                            }
                        }
                    }
                } catch (Exception e) {
                    session.setAttribute("ErrorReport", "ECWB102");
                    EmpExecutionContext.error(e, "模板参数或文档参数不对");
                    throw e;
                }

                if (wfilerows == 0) {
                    session.setAttribute("ErrorReport", "ECWB102");
                }

                if (null == paraList || formats == paraList.size() || bls == paraList.size() || keywords == paraList.size()) {
                    session.setAttribute("ErrorAll", "ECWB102");
                }

                //总数
                countMap.put(1, String.valueOf(total));
                //有效数
                countMap.put(2, String.valueOf(effs));
                //模拟网关实发数量
                countMap.put(3, String.valueOf(routes));
                //存在于黑名单数
                countMap.put(4, String.valueOf(bls));
                //sames = sames-effs;
                //重复号码
                countMap.put(5, String.valueOf(sames));
                //含关键字数
                countMap.put(6, String.valueOf(keywords));
                //非法格式数
                countMap.put(7, String.valueOf(formats));

                if (formats == total) {
                    session.setAttribute("ErrorAll", "RESULT_FORMAT");
                } else if (bls == total) {
                    session.setAttribute("ErrorAll", "RESULT_BLACKLIST");
                } else if (keywords == total) {
                    session.setAttribute("ErrorAll", "RESULT_KEYWORDS");
                } else if (effs == 0) {
                    session.setAttribute("ErrorAll", "RESULT_EFFS");
                }
                if (effs != 0) {
                    countArr[0] = total;
                    countArr[1] = effs;
                    countArr[2] = routes;
                }
                session.setAttribute("previewList", previewList);
                session.setAttribute("countMap", countMap);

            } catch (Exception e) {
                routes = 0;
                // 要删除已写的短信
                session.setAttribute("ErrorReport", "ECWB102");
                EmpExecutionContext.error(e, YdcwErrorStatus.ECWB102);
                throw e;
            } finally {
                // 关闭BufferedReader
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        EmpExecutionContext.error(e, "关闭BufferedReader异常");
                    }
                }
                if (fData != null) {
                    fData = null;
                }
                if (pw != null) {
                    try {
                        pw.close();
                    } catch (Exception e2) {
                        EmpExecutionContext.error(e2, "关闭PrintWriter异常");
                    }
                }
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (Exception e2) {
                        EmpExecutionContext.error(e2, "关闭FileWriter异常");
                    }
                }
                if (badContentSb.length() > 0) {
                    // 剩余的有效号码写文件
                    try {
                        new TxtFileUtil().writeToTxtFile(badFilePath, badContentSb.toString());
                        badContentSb.setLength(0);
                    } catch (Exception e) {
                        EmpExecutionContext.error(e, "移动财务写错误号码文件出现异常！");
                    }
                }
			/*	if (badPw != null) {
					try {
						badPw.close();						
					} catch (Exception e2) {
					}
				}*/
            }
            session.setAttribute("errorDescMap", erMap);
            return countArr;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "处理临时文件异常");
            throw e;
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }


    /**
     * 处理带模板的短信发送
     *
     * @param info
     * @param
     * @return
     */
    private String combineContentSGBaseManual(String info, String inputContent) {
        int bit = 50;
        String content = inputContent.replace("\r\n", " ");
        String params[] = info.split("&");
        StringBuffer mid = null;
        int k = 0;
        for (int i = 1; i < bit; i++) {
            mid = new StringBuffer();
            mid.append("#P_").append(i).append("#");
            if (content.indexOf(mid.toString()) != -1) {
                //替换大写的参数内容
                content = content.replace(mid.toString(), params[k]);
                //替换小写的参数内容
                content = content.replace(mid.toString().replace("#P_", "#p_"), params[k]);
                k++;
            }
        }
        return content;
    }

    private String[] getCell(XSSFRow row) {
        DecimalFormat df = new DecimalFormat("########");
        int cellNum = row.getPhysicalNumberOfCells();
        String[] s = new String[cellNum - 1];
        for (int i = 1; i < cellNum; i++) {

            if (row.getCell(i).getCellType() == org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC) {
                s[i - 1] = df.format(row.getCell(i).getNumericCellValue());
            } else {
                s[i - 1] = row.getCell(i).getStringCellValue();
            }

        }
        return s;
    }


}
