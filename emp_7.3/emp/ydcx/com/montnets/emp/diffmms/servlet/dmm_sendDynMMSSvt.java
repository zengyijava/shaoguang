package com.montnets.emp.diffmms.servlet;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.constant.*;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.diffmms.biz.DynamicMmsBiz;
import com.montnets.emp.entity.pasroute.LfMmsAccbind;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.entity.template.MmsTemplate;
import com.montnets.emp.entity.ydcx.LfDfadvanced;
import com.montnets.emp.mmsmate.biz.MaterialBiz;
import com.montnets.emp.mmstask.biz.MmsTaskBiz;
import com.montnets.emp.samemms.biz.CreateTmsFile;
import com.montnets.emp.samemms.biz.FrameItem;
import com.montnets.emp.samemms.biz.SameMmsBiz;
import com.montnets.emp.samemms.biz.TmsFile;
import com.montnets.emp.security.blacklist.BlackListAtom;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.servmodule.ydcx.constant.ServerInof;
import com.montnets.emp.util.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-15 上午10:33:27
 * @description
 */
// 彩信发送
@SuppressWarnings("serial")
public class dmm_sendDynMMSSvt extends BaseServlet {
    private final String opModule = StaticValue.MMS_BOX;
    // String opType = null;
    // String opContent = null;
    private final String opSper = StaticValue.OPSPER;
    // TemplateBiz mtb = new TemplateBiz();
    private final WgMsgConfigBiz wb = new WgMsgConfigBiz();
    private final TxtFileUtil txtfileutil = new TxtFileUtil();
    private final BlackListAtom blBiz = new BlackListAtom();
    // String[] haoduan;
    private final String dirUrl = txtfileutil.getWebRoot();

    private final KeyWordAtom keyWordAtom = new KeyWordAtom();
    // 换行符
    private final String line = StaticValue.systemProperty
            .getProperty(StaticValue.LINE_SEPARATOR);
    private final BalanceLogBiz balanceLogBiz = new BalanceLogBiz();
    private final CreateTmsFile mpb = new CreateTmsFile();
    private final SameMmsBiz mmsBiz = new SameMmsBiz();
    private final MmsTaskBiz mmsTaskBiz = new MmsTaskBiz();
    private static final String PATH = "/ydcx/diffmms";

    private final BaseBiz baseBiz = new BaseBiz();

    private final int MAX_SIZE = 5000000;

    /**
     * @param request
     * @param response
     */
    // 进入发送页面
    public void find(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long lgguid = null;
            String corpCode = "";
            String userName = "";
            LfSysuser sysUser = null;
            try {
                // 获取当前操作员的GUID
                // lgguid = Long.valueOf(request.getParameter("lgguid"));
                LfSysuser loginSysuser = (LfSysuser) request.getSession(false)
                        .getAttribute("loginSysuser");
                lgguid = loginSysuser.getUserId();

                // 获取当前对象
                // sysUser = baseBiz.getByGuId(LfSysuser.class, lgguid);
                sysUser = baseBiz.getById(LfSysuser.class, lgguid);
                // 获取企业编码
                corpCode = sysUser.getCorpCode();
                // 获取当前操作员用户名
                userName = sysUser.getUserName();
            } catch (Exception e) {
                // 获取企业编码
                // 获取用户名称
                EmpExecutionContext.error(e, "动态彩信发送获取当前操作员对象出现异常！");
            }

            // 获取任务ID
            Long taskId = GlobalVariableBiz.getInstance().getValueByKey(
                    "taskId", 1L);
            request.setAttribute("taskId", taskId);

            LfTemplate lfTemplate = new LfTemplate();
            if (sysUser != null) {
                lfTemplate.setUserId(sysUser.getUserId());
            }
            // 模板类型（1是动态模板）
            lfTemplate.setDsflag(1L);
            // 查询启用的模板
            lfTemplate.setTmState(1L);
            // 查询出操作员的彩信模板
            List<LfTemplate> mmsList = mmsBiz
                    .getMMSTemplateByUserId(lfTemplate);
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("userId", userName);
            // 获取该操作员的审批信息
            /*
             * List<LfFlow> flowList =
             * baseBiz.getByCondition(LfFlow.class,conditionMap, null);
             *
             * if (flowList != null && flowList.size() > 0) {
             * request.setAttribute("isFlow", "true"); } else {
             * request.setAttribute("isFlow", "false"); }
             */
            // conditionMap.clear();
            // conditionMap.put("isValidate", "1");
            // conditionMap.put("corpCode", corpCode);
            // 获取彩信发送账号
            // List<LfMmsAccbind> mmsAccbinds = baseBiz.getByCondition(
            // LfMmsAccbind.class, conditionMap, null);
            // 设置服务器名称
            new ServerInof().setServerName(getServletContext().getServerInfo());
            LfMmsAccbind lfMmsAccbind = new LfMmsAccbind();
            lfMmsAccbind.setCorpCode(corpCode);
            List<LfMmsAccbind> mmsAccbinds = mmsBiz.getMmsSpUser(lfMmsAccbind);
            request.setAttribute("mmsAccbinds", mmsAccbinds);

            request.setAttribute("mmsTempList", mmsList);

            // 获取高级设置默认信息
            conditionMap.clear();
            conditionMap.put("userid", sysUser!=null?sysUser.getUserId().toString():"");
            // 10：动态彩讯发送
            conditionMap.put("flag", "10");
            LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
            orderMap.put("id", StaticValue.DESC);
            List<LfDfadvanced> lfDfadvancedList = baseBiz.getByCondition(
                    LfDfadvanced.class, conditionMap, orderMap);
            LfDfadvanced lfDfadvanced = null;
            if (lfDfadvancedList != null && lfDfadvancedList.size() > 0) {
                lfDfadvanced = lfDfadvancedList.get(0);
            }
            request.setAttribute("lfDfadvanced", lfDfadvanced);

            request
                    .getRequestDispatcher(
                            "/ydcx/diffmms/dmm_sendDynamicMMS.jsp").forward(
                    request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "动态彩信发送跳转页面出现异常！");
        }
    }

    /**
     * 预览提交方法
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void preview(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 设置语言
        String langName = request.getParameter("langName");
        String result = "";
        request.setCharacterEncoding("UTF-8");

        // 企业编码
        String lgcorpcode = "";
        String tmMsg = null;
        // 有效总数
        int effCount = 0;
        // 提交号码数
        Long subCount = 0l;
        Long badCount = 0l;
        Long badModeCount = 0l;
        // 重复号码条数
        Long repeatCount = 0l;
        // 黑名单条数
        Long blackCount = 0l;
        // 包含关键字条数
        Long kwCount = 0l;

        // 动态内容
        String dtMsg = null;
        String smsContent = null;
        int index;
        // 短信模板参数个数
        int templateCount = 0;
        int conCount = 0;

        // 常用文件读取工具类
        TxtFileUtil txtfileutil = new TxtFileUtil();

        // 时间
        Date time = Calendar.getInstance().getTime();

        StringBuffer contentSb = new StringBuffer();
        StringBuffer badContentSb = new StringBuffer();
        // 存放用来预览显示的信息
        StringBuffer viewContentSb = new StringBuffer();
        boolean isOverSize = false;
        // 上传文件最大大小
        long maxSize = 100 * 1024L * 1024L;
        long zipSize = 10 * 1024L * 1024L;
        // 当前登录用户
        Long userId = 0L;
        File zipUrl = null;
        File Newfile = null;
        String spuser = "";
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            // 获取运营商号码段
            String[] haoduan = wb.getHaoduan();
            HashSet<Long> repeatList = new HashSet<Long>();

            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 设置上传文件大小
            factory.setSizeThreshold(1024 * 1024);
            // 上传临时文件存放地址
            /*
             * String temp = dirUrl + File.separator + "fileUpload" +
             * File.separator + "mms" + File.separator + "mttasks";
             */
            // 当前登录操作员id
            //Long lguserid = Long.parseLong(request.getParameter("lguserid"));
            //漏洞修复 session里获取操作员信息
            Long lguserid = SysuserUtil.longLguserid(request);


            // 获取彩信号码文件路径
            String[] url = new SameMmsBiz().getSaveUrl((int) (lguserid - 0),
                    time);
            // 上传临时文件存放
            // 文件路径前缀
            String temp = url[0].substring(0, url[0].lastIndexOf("/"));

            factory.setRepository(new File(temp));
            ServletFileUpload upload = new ServletFileUpload(factory);

            List<FileItem> fileList = null;
            try {
                fileList = upload.parseRequest(request);
            } catch (FileUploadException e) {
                EmpExecutionContext.error("EBFV003");
                throw e;
            }
            // 路径
            // String[] url = null;
            Iterator<FileItem> it = fileList.iterator();
            while (it.hasNext()) {
                FileItem fileItem = (FileItem) it.next();
                // 文件名
                String fileName = fileItem.getFieldName();
                if (fileName.equals("lgcorpcode")) {
                    // 当前用户企业编码
                    lgcorpcode = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("lguserid")) {
                    // 当前用户userid
                    userId = Long.valueOf(fileItem.getString("UTF-8")
                            .toString());
                    url = getSaveUrl((int) (userId - 0), time);
                } else if (fileName.equals("mmsUser")) {
                    // 当前用户userid
                    spuser = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("tmUrl")) {
                    tmMsg = fileItem.getString("UTF-8").toString();
                    dtMsg = mpb.getTmsText(tmMsg);
                    // tms文件不存在
                    if (dtMsg == null) {
                        result = "noTmsMsg";
                        return;
                    }
                    // 查找彩信内容中的参数个数
                    templateCount = getTemplateCount(templateCount, dtMsg);
                } else if (!fileItem.isFormField()
                        && fileItem.getName().length() > 0) {

                    // 判断文件大小是否超过限制
                    if (fileItem.getSize() > maxSize) {
                        fileItem.delete();
                        fileItem = null;
                        isOverSize = true;
                        break;
                    }

                    List<BufferedReader> readerList = new ArrayList<BufferedReader>();
                    // 查找关键字信息
                    String fileCurName = fileItem.getName();
                    // 文件类型
                    String fileType = fileCurName.substring(
                            fileCurName.lastIndexOf(".")).toLowerCase();
                    //检验文件类型的合法性
                    if (!fileType.equals(".rar") && !fileType.equals(".zip") && !fileType.equals(".xls")
                            && !fileType.equals(".et") && !fileType.equals(".xlsx") && !fileType.equals(".txt")) {
                        // 文件类型不合法
                        EmpExecutionContext.error("相同内容预览，文件上传失败，文件类型不合法。userId：" + lguserid + "，errCode:" + ErrorCodeInfo.V10003);
                        throw new EMPException(ErrorCodeInfo.V10003);
                    }
                    int fileIndex = 0;
                    // 如果是zip文件
                    if (fileType.equals(".zip")) {
                        // 判断压缩文件的大小
                        if (fileItem.getSize() > zipSize) {
                            fileItem.delete();
                            fileItem = null;
                            isOverSize = true;
                            break;
                        }
                        String zipFileStr = url[0].replace(".txt", ".zip");
                        zipUrl = new File(zipFileStr);
                        fileItem.write(zipUrl);
                        ZipFile zipFile = new ZipFile(zipUrl);
                        Enumeration zipEnum = zipFile.getEntries();
                        ZipEntry entry = null;
                        while (zipEnum.hasMoreElements()) {
                            fileIndex++;
                            entry = (ZipEntry) zipEnum.nextElement();
                            if (!entry.isDirectory()
                                    && entry.getName().toLowerCase().endsWith(
                                    ".txt")) {
                                BufferedReader reader = new BufferedReader(
                                        new InputStreamReader(zipFile
                                                .getInputStream(entry), "GBK"));
                                readerList.add(reader);
                            } else if (!entry.isDirectory()
                                    && (entry.getName().toLowerCase().endsWith(
                                    ".xls") || entry.getName()
                                    .toLowerCase().endsWith(".et"))) {
                                String FileStr = url[0];
                                String FileStrTemp = FileStr.substring(0,
                                        FileStr.indexOf(".txt"))
                                        + "_temp" + fileIndex + ".txt";
                                Newfile = new File(FileStrTemp);
                                if (Newfile.exists()) {
                                    boolean delete = Newfile.delete();
                                    if (!delete) {
                                        EmpExecutionContext.error("删除文件失败");
                                    }
                                }
                                fos = new FileOutputStream(
                                        Newfile);
                                osw = new OutputStreamWriter(
                                        fos, "GBK");
                                bw = new BufferedWriter(osw);
                                String phoneNum = "";
                                String Context = "";
                                try {
                                    HSSFWorkbook workbook = new HSSFWorkbook(
                                            zipFile.getInputStream(entry));
                                    // 循环每张表
                                    for (int sheetNum = 0; sheetNum < workbook
                                            .getNumberOfSheets(); sheetNum++) {
                                        HSSFSheet sheet = workbook
                                                .getSheetAt(sheetNum);
                                        // 循环每一行
                                        for (int rowNum = 0; rowNum <= sheet
                                                .getLastRowNum(); rowNum++) {
                                            HSSFRow row = sheet.getRow(rowNum);
                                            if (row == null) {
                                                continue;
                                            }
                                            // 得到第一列的电话号码
                                            phoneNum = getCellFormatValue(row
                                                    .getCell(0));
                                            Context = "";
                                            // 循环每一列（内容以,分隔开来）
                                            for (int k = 1; k < row
                                                    .getLastCellNum(); k++) {
                                                HSSFCell cell = row.getCell(k);
                                                if (cell != null
                                                        && cell.toString()
                                                        .length() > 0) {
                                                    Context += ","
                                                            + getCellFormatValue(cell);
                                                }

                                            }
                                            // 一行一行的将内容写入到txt文件中。
                                            bw.write(phoneNum + Context + line);
                                        }
                                    }
                                    bw.close();
                                    osw.close();
                                    fos.close();

                                    FileInputStream fis = new FileInputStream(
                                            Newfile);
                                    BufferedReader br = new BufferedReader(
                                            new InputStreamReader(fis, "GBK"));

                                    readerList.add(br);
                                } catch (FileNotFoundException e) {
                                    EmpExecutionContext
                                            .error(e, "彩信文件找不到出现异常！");
                                } catch (IOException e) {
                                    EmpExecutionContext.error(e, "彩信io流出现异常！");
                                } catch (Exception e) {
                                    EmpExecutionContext.error(e,
                                            "彩信文件读取号码文件出现异常！");
                                }

                            }
                            // 如果是.xlsx文件
                            else if (!entry.isDirectory()
                                    && entry.getName().toLowerCase().endsWith(
                                    ".xlsx")) {

                                String FileStr = url[0];
                                String FileStrTemp = FileStr.substring(0,
                                        FileStr.indexOf(".txt"))
                                        + "_temp" + fileIndex + ".txt";
                                Newfile = new File(FileStrTemp);
                                fos = new FileOutputStream(
                                        Newfile);
                                osw = new OutputStreamWriter(
                                        fos, "GBK");
                                bw = new BufferedWriter(osw);
                                String phoneNum = "";
                                String Context = "";
                                try {
                                    XSSFWorkbook workbook = new XSSFWorkbook(
                                            zipFile.getInputStream(entry));
                                    // 循环每张表
                                    for (int sheetNum = 0; sheetNum < workbook
                                            .getNumberOfSheets(); sheetNum++) {
                                        XSSFSheet sheet = workbook
                                                .getSheetAt(sheetNum);
                                        // 循环每一行
                                        for (int rowNum = 0; rowNum <= sheet
                                                .getLastRowNum(); rowNum++) {
                                            XSSFRow row = sheet.getRow(rowNum);
                                            if (row == null) {
                                                continue;
                                            }
                                            phoneNum = getCellFormatValue(row
                                                    .getCell(0));
                                            Context = "";
                                            for (int k = 1; k < row
                                                    .getLastCellNum(); k++) {
                                                XSSFCell cell = row.getCell(k);
                                                if (cell != null
                                                        && cell.toString()
                                                        .length() > 0) {
                                                    Context += ","
                                                            + getCellFormatValue(cell);
                                                }
                                            }
                                            bw.write(phoneNum + Context + line);
                                        }
                                    }
                                    bw.close();
                                    osw.close();
                                    fos.close();

                                    FileInputStream fis = new FileInputStream(
                                            Newfile);
                                    BufferedReader br = new BufferedReader(
                                            new InputStreamReader(fis, "GBK"));

                                    readerList.add(br);
                                } catch (FileNotFoundException e) {
                                    EmpExecutionContext
                                            .error(e, "彩信文件找不到出现异常！");
                                } catch (IOException e) {
                                    EmpExecutionContext.error(e, "彩信io流出现异常！");
                                } catch (Exception e) {
                                    EmpExecutionContext.error(e,
                                            "彩信文件读取号码文件出现异常！");
                                }

                            }

                        }
                    } else if (fileType.equals(".xls")
                            || fileType.equals(".et")) {
                        String FileStr = url[0];
                        String FileStrTemp = FileStr.substring(0, FileStr
                                .indexOf(".txt"))
                                + "_temp.txt";
                        Newfile = new File(FileStrTemp);
                        fos = new FileOutputStream(Newfile);
                        osw = new OutputStreamWriter(fos,
                                "GBK");
                        bw = new BufferedWriter(osw);
                        String phoneNum = "";
                        String Context = "";
                        try {
                            HSSFWorkbook workbook = new HSSFWorkbook(fileItem
                                    .getInputStream());
                            // 循环每张表
                            for (int sheetNum = 0; sheetNum < workbook
                                    .getNumberOfSheets(); sheetNum++) {
                                HSSFSheet sheet = workbook.getSheetAt(sheetNum);
                                // 循环每一行
                                for (int rowNum = 0; rowNum <= sheet
                                        .getLastRowNum(); rowNum++) {
                                    HSSFRow row = sheet.getRow(rowNum);
                                    if (row == null) {
                                        continue;
                                    }
                                    // 得到第一列的电话号码
                                    phoneNum = getCellFormatValue(row
                                            .getCell(0));
                                    Context = "";
                                    // 循环每一列（内容以,分隔开来）
                                    for (int k = 1; k < row.getLastCellNum(); k++) {
                                        HSSFCell cell = row.getCell(k);
                                        if (cell != null
                                                && cell.toString().length() > 0) {
                                            Context += ","
                                                    + getCellFormatValue(cell);
                                        }

                                    }
                                    // 一行一行的将内容写入到txt文件中。
                                    bw.write(phoneNum + Context + line);
                                }
                            }
                            bw.close();
                            osw.close();
                            fos.close();

                            FileInputStream fis = new FileInputStream(Newfile);
                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(fis, "GBK"));
                            readerList.add(br);
                        } catch (FileNotFoundException e) {
                            EmpExecutionContext.error(e, "彩信文件找不到出现异常！");
                        } catch (IOException e) {
                            EmpExecutionContext.error(e, "彩信io流出现异常！");
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, "彩信文件读取号码文件出现异常！");
                        }
                    } else if (fileType.equals(".xlsx")) {
                        String FileStr = url[0];
                        String FileStrTemp = FileStr.substring(0, FileStr
                                .indexOf(".txt"))
                                + "_temp.txt";
                        Newfile = new File(FileStrTemp);
                        fos = new FileOutputStream(Newfile);
                        osw = new OutputStreamWriter(fos,
                                "GBK");
                        bw = new BufferedWriter(osw);
                        String phoneNum = "";
                        // 内容
                        String Context = "";
                        try {
                            XSSFWorkbook workbook = new XSSFWorkbook(fileItem
                                    .getInputStream());
                            // 循环每张表
                            for (int sheetNum = 0; sheetNum < workbook
                                    .getNumberOfSheets(); sheetNum++) {
                                // 循环每一行
                                XSSFSheet sheet = workbook.getSheetAt(sheetNum);
                                for (int rowNum = 0; rowNum <= sheet
                                        .getLastRowNum(); rowNum++) {
                                    XSSFRow row = sheet.getRow(rowNum);
                                    if (row == null) {
                                        continue;
                                    }
                                    phoneNum = getCellFormatValue(row
                                            .getCell(0));
                                    Context = "";
                                    for (int k = 1; k < row.getLastCellNum(); k++) {
                                        XSSFCell cell = row.getCell(k);
                                        if (cell != null
                                                && cell.toString().length() > 0) {
                                            Context += ","
                                                    + getCellFormatValue(cell);
                                        }
                                    }
                                    bw.write(phoneNum + Context + line);
                                }
                            }
                            bw.close();
                            osw.close();
                            fos.close();

                            FileInputStream fis = new FileInputStream(Newfile);
                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(fis, "GBK"));

                            readerList.add(br);
                        } catch (FileNotFoundException e) {
                            EmpExecutionContext.error(e, "彩信文件找不到出现异常！");
                        } catch (IOException e) {
                            EmpExecutionContext.error(e, "彩信io流出现异常！");
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, "彩信文件读取号码文件出现异常！");
                        }
                    } else {
                        // 文件编码
                        String charset = "utf-8";
                        InputStream instream = fileItem.getInputStream();
                        try {
                            charset = get_charset(instream);
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, "动态彩信文件编码出现异常！");
                        }
                        // 江流转换成对应编码格式
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(
                                        fileItem.getInputStream(), charset));
                        if (charset.startsWith("UTF-")) {
                            reader.read(new char[1]);
                        }
                        readerList.add(reader);
                    }
                    String tmp;
                    String phoneNum = "";
                    int mid = 0;
                    try {
                        // 发送彩信号码个数不超过500万
                        for (int r = 0; r < readerList.size(); r++) {
                            // 文件的号码最多5000000
                            /*
                             * if (effCount > 5000000) { break;
                             *
                             * }
                             */
                            BufferedReader reader = readerList.get(r);

                            // 逐行读取
                            while ((tmp = reader.readLine()) != null) {
                                subCount++;
                                mid = 0;
                                tmp = tmp.trim();

                                index = tmp.indexOf(",");
                                if (index != -1) {
                                    phoneNum = StringUtils.parseMobile(tmp
                                            .substring(0, tmp.indexOf(",")));
                                }
                                // 手机号码长度不合格
                                if (index == -1 || phoneNum.length() != 11) {
                                    mid = 1;
                                } else {
                                    // 过滤号码部分
                                    // phoneNum = tmp.substring(0, index);
                                    if (wb.checkMobile(phoneNum, haoduan) != 1) {
                                        mid = 1;
                                    } else if (!checkRepeat(repeatList,
                                            phoneNum)) {
                                        // 过滤重复号码
                                        mid = 3;
                                    } else if (blBiz.checkMmsBlackList(
                                            lgcorpcode, phoneNum)) {
                                        // 过滤黑名单
                                        mid = 2;
                                    } else {
                                        // 过滤内容部分
                                        smsContent = tmp.substring(index + 1)
                                                .trim();
                                        // 内容长度为零
                                        if (smsContent.length() == 0) {
                                            mid = 1;
                                        } else {
                                            conCount = smsContent.split(",").length;
                                            if (conCount < templateCount) {
                                                // 动态模板提交时，如果文件内的参数少于模板内参数则视为格式不正确
                                                mid = 1;
                                            }/*
                                             * else { //动态内容拼接处理
                                             * smsContent=smsSend
                                             * .combineContentSg(smsContent,
                                             * dtMsg); }
                                             */
                                        }
                                        // 如果没在判断参数个数时不合格，就继续往下走
                                        if (mid == 0) {
                                            // 检查关键字false包含关键字，true不包含关键字
                                            // if (kwsList != null &&
                                            // !"".equals(smsBiz.checkMMSText(smsContent.toUpperCase(),kwsList)))
                                            // {
                                            int filterKeyRes = keyWordAtom
                                                    .filterKeyWord(smsContent
                                                                    .toUpperCase(),
                                                            lgcorpcode);
                                            if (filterKeyRes == 1) {
                                                mid = 4;
                                            } else {
                                                repeatList.add(Long
                                                        .parseLong(phoneNum));
                                                contentSb
                                                        .append(
                                                                phoneNum
                                                                        + ","
                                                                        + EmpUtils
                                                                        .StringToBase64(smsContent))
                                                        .append(line);
                                                effCount++;
                                                if (effCount <= 10) {
                                                    viewContentSb
                                                            .append(
                                                                    phoneNum
                                                                            + ","
                                                                            + smsContent)
                                                            .append(line);
                                                    // 将预览信息写入文件
                                                    txtfileutil
                                                            .writeToTxtFile(
                                                                    url[3],
                                                                    viewContentSb
                                                                            .toString());
                                                    viewContentSb.setLength(0);
                                                }
                                            }
                                        }
                                    }
                                }

                                switch (mid) {
                                    case 1:
                                        // badContentSb.append("格式非法：").append(tmp)
                                        // .append(line);

                                        if (StaticValue.ZH_HK.equals(langName)) {
                                            badContentSb.append("Illegal format:")
                                                    .append(tmp).append(line);
                                        } else if (StaticValue.ZH_TW
                                                .equals(langName)) {
                                            badContentSb.append("格式非法：")
                                                    .append(tmp).append(line);
                                        } else {
                                            badContentSb.append("格式非法：")
                                                    .append(tmp).append(line);
                                        }
                                        badModeCount++;
                                        badCount++;
                                        break;
                                    case 2:
                                        // badContentSb.append("黑名单号码：").append(tmp)
                                        // .append(line);
                                        if (StaticValue.ZH_HK.equals(langName)) {
                                            badContentSb.append(
                                                    "Black list number:").append(
                                                    tmp).append(line);
                                        } else if (StaticValue.ZH_TW
                                                .equals(langName)) {
                                            badContentSb.append("黑名單號碼：").append(
                                                    tmp).append(line);
                                        } else {
                                            badContentSb.append("黑名单号码：").append(
                                                    tmp).append(line);
                                        }
                                        blackCount++;
                                        badCount++;
                                        break;
                                    case 3:
                                        // badContentSb.append("重复号码：").append(tmp)
                                        // .append(line);
                                        if (StaticValue.ZH_HK.equals(langName)) {
                                            badContentSb.append("Repeat number:")
                                                    .append(tmp).append(line);
                                        } else if (StaticValue.ZH_TW
                                                .equals(langName)) {
                                            badContentSb.append("重複號碼：")
                                                    .append(tmp).append(line);
                                        } else {
                                            badContentSb.append("重复号码：")
                                                    .append(tmp).append(line);
                                        }
                                        repeatCount++;
                                        badCount++;
                                        break;
                                    case 4:
                                        // badContentSb.append("包含关键字：").append(tmp)
                                        // .append(line);
                                        if (StaticValue.ZH_HK.equals(langName)) {
                                            badContentSb.append(
                                                    "Contains keywords:").append(
                                                    tmp).append(line);
                                        } else if (StaticValue.ZH_TW
                                                .equals(langName)) {
                                            badContentSb.append("包含關鍵字：").append(
                                                    tmp).append(line);
                                        } else {
                                            badContentSb.append("包含关键字：").append(
                                                    tmp).append(line);
                                        }
                                        kwCount++;
                                        badCount++;
                                        break;
                                }
                                // 1000个号码写一次文件
                                if (effCount % 10000 == 0 && effCount >= 10000) {
                                    txtfileutil.writeToTxtFile(url[0],
                                            contentSb.toString());
                                    contentSb = new StringBuffer();
                                }
                                if (badCount % 1000 == 0 && badCount >= 1000) {
                                    txtfileutil.writeToTxtFile(url[2],
                                            badContentSb.toString());
                                    badContentSb = new StringBuffer();
                                }
                            }

                            // 关闭流
                            reader.close();
                        }

                    } catch (Exception e) {
                        // 错误码
                        // EmpExecutionContext.error("EBFV005");
                        // 删除文件
                        deleteFile(url[0]);
                        // 异常处理
                        EmpExecutionContext.error(e, "动态彩信读取号码文件出现异常！");
                        throw e;
                    } finally {
            			try{
            				IOUtils.closeReaders(getClass(), readerList);
            			}catch(IOException e){
            				EmpExecutionContext.error(e, "");
            			}
                        // 清内存
                        readerList.clear();
                        repeatList.clear();
                        readerList = null;
                        repeatList = null;
                        // 删除临时上传文件
                        fileItem.delete();
                        if (zipUrl != null) {
                            if (!zipUrl.delete()) {
                                //
                            }
                        }
                        for (int i = 0; i < fileIndex; i++) {
                            int a = i + 1;
                            String FileStr = url[0];
                            String FileStrTemp = FileStr.substring(0, FileStr
                                    .indexOf(".txt"))
                                    + "_temp" + a + ".txt";
                            File file = new File(FileStrTemp);
                            if (file.exists()) {
                                boolean delete = file.delete();
                                if (!delete) {
                                    EmpExecutionContext.error("删除文件失败");
                                }
                            }
                        }
                        if (Newfile != null) {
                            boolean delete = Newfile.delete();
                            if (!delete) {
                                EmpExecutionContext.error("删除文件失败");
                            }
                        }
                    }
                }
            }

            // 登录操作员信息
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false)
                    .getAttribute("loginSysuser");
            // 登录操作员信息为空
            if (lfSysuser == null) {
                EmpExecutionContext
                        .error("动态彩信预览，从session获取登录操作员信息异常。lfSysuser为null，errCode："
                                + IErrorCode.V10001);
                return;
            }
            // 调用彩信的相关方法 操作员、企业编码、SP账号检查
            boolean checkFlag = new CheckUtil().checkMmsSysuserInCorp(
                    lfSysuser, lgcorpcode, spuser, null);
            if (!checkFlag) {
                EmpExecutionContext.error("动态彩信预览，检查操作员、企业编码、发送账号不通过，"
                        + "，corpCode:" + lgcorpcode + "，userid："
                        + lfSysuser.getUserId() + "，spUser：" + spuser
                        + "，errCode:" + IErrorCode.V10001);
                return;
            }

            // 超出导入文件的最大限制
            if (isOverSize) {
                result = "overSize";
            }
            // 发送号码总数超出限制
            else if (effCount > MAX_SIZE) {
                for (int i = 0; i < url.length; i++) {
                    deleteFile(url[i]);
                }
                result = "overstep";
            } else {

                txtfileutil.writeToTxtFile(url[0], contentSb.toString());

                // 还需判断计费机制是否开启,只有当开启的情况下才进行下面的判断，这里需要添加一个判断发送总条数是否大于当前机构的可发送的最大条数.
                Long maxcount = 0L;
                int yct = effCount;
                boolean isCharg = true; // 如果启用了计费则为true;未启用则为false;
                String spFeeResult = balanceLogBiz.checkGwFee(spuser, effCount,
                        lgcorpcode, false, 2);
                if (balanceLogBiz.IsChargings(userId)
                        && ("koufeiSuccess".equals(spFeeResult) || "notneedtocheck"
                        .equals(spFeeResult))) {
                    String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                            .format(new Date());
                    try {
                        // 提供一个可获取最大可发送条数的方法.
                        maxcount = balanceLogBiz
                                .getAllowMmsAmountByUserId(userId);
                        // maxcount=biz.getAllowSmsAmount(lfSysuser);
                        // 提供一个可获取最大可发送条数的方法.
                        if (maxcount == null) {
                            maxcount = 0L;
                        }
                        EmpExecutionContext.debug("date:" + dateStr
                                + "     预发送条数:" + effCount);
                    } catch (Exception e) {
                        EmpExecutionContext.error(e, "获取机构余额失败！");
                        maxcount = 0L;
                        yct = 0;
                    }
                } else {
                    isCharg = false;
                }
                // 过滤号码写入文件
                if (badContentSb.length() > 0) {
                    txtfileutil.writeToTxtFile(url[2], badContentSb.toString());
                }
                if (subCount == 0) {
                    // 没有可发送的号码
                    result = "noPhone";
                } else {
                    result = url[4] + "&" + url[1] + "&"
                            + String.valueOf(subCount) + "&"
                            + String.valueOf(effCount) + "&"
                            + String.valueOf(blackCount) + "&"
                            + String.valueOf(badModeCount) + "&"
                            + String.valueOf(repeatCount) + "&"
                            + String.valueOf(kwCount) + "&"
                            + String.valueOf(maxcount) + "&"
                            + String.valueOf(yct) + "&"
                            + String.valueOf(isCharg) + "&" + spFeeResult;
                }
            }
        } catch (Exception ex) {
            // 异常错误
            result = "error";
            // 打印异常
            EmpExecutionContext.error(ex, "动态彩信发送预览出现异常！");
        } finally {
            if (bw != null) {
                bw.close();
            }
            if (osw != null) {
                osw.close();
            }
            if (fos != null) {
                fos.close();
            }
            // 将结果返回给页面
            request.setAttribute("result", result);
            // 跳到中间页，（防止并发引起的异常）
            request.getRequestDispatcher(PATH + "/dmm_preview.jsp").forward(
                    request, response);
        }
    }

    /**
     * 彩信内容中的参数个数
     */
    private int getTemplateCount(int templateCount, String dtMsg) {
        String eg = "#[pP]_[1-9][0-9]*#";
        Matcher m = Pattern.compile(eg,
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(dtMsg);
        String paramStr = "";
        String pc = "";
        int paramCount = 0;
        while (m.find()) {
            paramStr = m.group();
            paramStr = paramStr.toUpperCase();
            pc = paramStr.substring(paramStr.indexOf("#P_") + 3, paramStr
                    .lastIndexOf("#"));
            paramCount = Integer.parseInt(pc);
            if (paramCount > templateCount) {
                templateCount = paramCount;
            }
        }
        return templateCount;
    }

    /**
     * @param request
     * @param response
     */
    public void send(HttpServletRequest request, HttpServletResponse response) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String langName = (String) request.getSession().getAttribute(
                StaticValue.LANG_KEY);
        SuperOpLog spLog = new SuperOpLog();
        String corpCode = "";
        LfSysuser sysUser = null;
        Long userId = null;
        Long userGuid = null;
        String lgguid = request.getParameter("lgguid");
        String opUser = "";
        try {
            // 获取操作员的GUID
            userGuid = Long.valueOf(lgguid);
            // 获取操作员对象
            sysUser = baseBiz.getByGuId(LfSysuser.class, userGuid);
            if (sysUser != null) {
                // 获取企业编码
                corpCode = sysUser.getCorpCode();
                // 获取用户ID
                userId = sysUser.getUserId();
                opUser = sysUser == null ? "" : sysUser.getUserName();
            } else {
                EmpExecutionContext.error("动态彩信发送获取当前操作员对象出现异常！");
                request.setAttribute("mmsresult", ErrorCodeInfo.getInstance(
                        langName).getErrorInfo(IErrorCode.V10001));
                find(request, response);
                return;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "动态彩信发送获取当前操作员对象出现异常！");
            request.setAttribute("mmsresult", ErrorCodeInfo.getInstance(
                    langName).getErrorInfo(IErrorCode.V10001));
            find(request, response);
            return;
        }
        // 获取任务ID
        String taskId = request.getParameter("taskId");
        String taskName = request.getParameter("taskName");
        // 主题为默认时,直接返回(防止重发)
        if (taskName != null && "不作为短信内容发送".equals(taskName.trim())) {
            EmpExecutionContext.error("静态彩讯发送获取参数异常，" + "taskName:" + taskName
                    + "，taskId：" + taskId);
            request.setAttribute("mmsresult", ErrorCodeInfo.getInstance(
                    langName).getErrorInfo(IErrorCode.V10001));
            find(request, response);
            return;
        }
        // 特殊符号处理，•存入数据库乱码
        taskName = taskName.replace("•", "．");
        // 1主题
        String mmsUser = request.getParameter("mmsUser");
        String theme = request.getParameter("tmName");
        // 特殊符号处理，•存入数据库乱码
        theme = theme.replace("•", "．");
        String result = null;
        String timeType = request.getParameter("sendType");
        // String subType = request.getParameter("subType");
        // 操作员的GUID

        // 模板标识列ID
        // String mmsTemplateId= request.getParameter("tmMsg");
        String mmsTemplateId = request.getParameter("mmstemplateid");

        String phoneFileUrl = request.getParameter("mobileUrl");
        // 提交总数
        String subCount = request.getParameter("subCount");
        // 有效号码数
        String effCount = request.getParameter("effCount");

        String opType = null;
        String opContent = null;

        try {
            // 登录操作员信息
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false)
                    .getAttribute("loginSysuser");
            // 登录操作员信息为空
            if (lfSysuser == null) {
                EmpExecutionContext
                        .error("动态彩信发送，从session获取登录操作员信息异常。lfSysuser为null，errCode："
                                + IErrorCode.V10001);
                request.setAttribute("mmsresult", ErrorCodeInfo.getInstance(
                        langName).getErrorInfo(IErrorCode.V10001));
                find(request, response);
                return;
            }
            // 调用彩信的相关方法 操作员、企业编码、SP账号检查
            boolean checkFlag = new CheckUtil().checkMmsSysuserInCorp(
                    lfSysuser, corpCode, mmsUser, null);
            if (!checkFlag) {
                EmpExecutionContext.error("动态彩信发送，检查操作员、企业编码、发送账号不通过，，taskid:"
                        + taskId + "，corpCode:" + corpCode + "，userid："
                        + userId + "，spUser：" + mmsUser + "，errCode:"
                        + IErrorCode.V10001);
                request.setAttribute("mmsresult", ErrorCodeInfo.getInstance(
                        langName).getErrorInfo(IErrorCode.V10001));
                find(request, response);
                return;
            }

            opType = StaticValue.ADD;
            // "新建"
            opContent = "创建彩信任务";
            // 彩信账户
            opContent += "（任务名称：" + taskName + "）";
            LfMttask lfMttask = new LfMttask();
            // 是否重发 1-已重发 0-未重发
            lfMttask.setIsRetry(0);
            // 信息类型（1-短信 ;2-彩信）
            lfMttask.setMsType(2);
            // 任务说明
            taskName = taskName.replaceAll("•", "．");
            lfMttask.setTaskName(taskName);
            // 彩信标题
            theme = theme.replaceAll("•", "．");
            lfMttask.setTitle(theme);
            // 彩信账户
            lfMttask.setSpUser(mmsUser);
            // 填写彩信类型 10普通彩信,11静态模板彩信,12动态模板彩信
            // 动态彩信模板
            lfMttask.setBmtType(12);
            // 彩信平台模板ID
            LfTemplate lfTemplate = baseBiz.getById(LfTemplate.class, Long
                    .parseLong(mmsTemplateId));
            lfMttask.setMsg(String.valueOf(lfTemplate.getSptemplid()));
            // 设置模板路径
            lfMttask.setTmplPath(lfTemplate.getTmMsg());
            // 提交状态(创建中1，提交2，取消3)
            lfMttask.setSubState(2);
            // 下行状态(0-代表新消息（未发送）；1-已发送成功;2-失败;3-未使用;4-发送中)
            lfMttask.setSendstate(0);
            // MOBILE_TYPE 号码类型（文件上传1或手工输入0）
            lfMttask.setMobileType(1);
            // 号码文件的URL
            lfMttask.setMobileUrl(phoneFileUrl);
            // 发送总数
            lfMttask.setSubCount(Long.parseLong(subCount));
            // 有效号码数
            lfMttask.setEffCount(Long.parseLong(effCount));
            // 操作员ID
            lfMttask.setUserId(userId);
            // 企业编码
            lfMttask.setCorpCode(corpCode);
            // 提交时间
            lfMttask.setSubmitTime(Timestamp.valueOf(new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss").format(new Date())));
            // 是否定时发送 1定时0即时
            // 短信定时发送时间
            if ("1".equals(timeType)) {
                lfMttask.setTimerStatus(1);
                String stime = request.getParameter("sendtime");
                stime = stime + ":00";
                lfMttask
                        .setTimerTime(new Timestamp(sdf.parse(stime).getTime()));
            } else {
                lfMttask.setTimerStatus(0);
            }
            // 设置taskID
            lfMttask.setTaskId(Long.parseLong(taskId));

            String re = "";
            // 判断是否使用集群
            if (StaticValue.getISCLUSTER() == 1) {
                CommonBiz commBiz = new CommonBiz();
                // 上传文件到文件服务器
                // if("success".equals(commBiz.uploadFileToFileCenter(phoneFileUrl)))
                // {
                // //删除本地文件
                // //commBiz.deleteFile(phoneFileUrl);
                // }else
                // {
                // re = "上传号码文件失败，取消任务创建！";
                // }
                // 使用文件服务器地址
                lfMttask.setFileuri(commBiz
                        .uploadFileToFileServer(phoneFileUrl));
            } else {
                // 使用本节点地址
                lfMttask.setFileuri(StaticValue.BASEURL);
            }
            if ("".equals(re)) {
                re = mmsTaskBiz.addMmsLfMttask(lfMttask);
            }
            if ("createSuccess".equals(re)) {
                // result = "创建彩信任务及提交到审批流成功！";

                if (StaticValue.ZH_HK.equals(langName)) {
                    result = "Create MMS tasks and submitted to the approval flow success!";
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    result = "創建彩信任務及提交到審批流成功！";
                } else {
                    result = "创建彩信任务及提交到审批流成功！";
                }
                spLog.logSuccessString(opUser, opModule, opType, opContent,
                        corpCode);
            } else if ("saveSuccess".equals(re)) {
                // result = "存草稿成功！";

                if (StaticValue.ZH_HK.equals(langName)) {
                    result = "Saved draft success!";
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    result = "存草稿成功！";
                } else {
                    result = "存草稿成功！";
                }
                spLog.logSuccessString(opUser, opModule, opType, opContent,
                        corpCode);
            } else if ("000".equals(re)) {
                // result = "创建彩信任务及发送到网关成功！";
                // 创建彩信任务及发送到网关成功
                result = "000&" + taskId;
                spLog.logSuccessString(opUser, opModule, opType, opContent,
                        corpCode);
            } else if ("timerSuccess".equals(re)) {
                // result = "创建彩信任务及定时任务添加成功！";

                if (StaticValue.ZH_HK.equals(langName)) {
                    result = "Create MMS tasks and timing tasks to add success!";
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    result = "創建彩信任務及定時任務添加成功！";
                } else {
                    result = "创建彩信任务及定时任务添加成功！";
                }
                spLog.logSuccessString(opUser, opModule, opType, opContent,
                        corpCode);
            } else if ("timerFail".equals(re)) {
                // result = "创建定时任务失败，取消任务创建！";

                if (StaticValue.ZH_HK.equals(langName)) {
                    result = "Create a scheduled task failed, cancel the task to create!";
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    result = "創建定時任務失敗，取消任務創建！";
                } else {
                    result = "创建定时任务失败，取消任务创建！";
                }
                spLog.logFailureString(opUser, opModule, opType, opContent
                        + opSper, null, corpCode);
            } else if ("timeError".equals(re)) {
                // result = "创建定时任务失败，定时时间不得小于当前时间！";

                if (StaticValue.ZH_HK.equals(langName)) {
                    result = "Failed to create a scheduled task, timing time shall not be less than the current time!";
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    result = "創建定時任務失敗，定時時間不得小於當前時間！";
                } else {
                    result = "创建定时任务失败，定时时间不得小于当前时间！";
                }
                spLog.logFailureString(opUser, opModule, opType, opContent
                        + opSper, null, corpCode);
            } else if ("mmsyuebuzu".equals(re)) {
                // result = "彩信余额不足！";

                if (StaticValue.ZH_HK.equals(langName)) {
                    result = "MMS insufficient balance!";
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    result = "彩信餘額不足！";
                } else {
                    result = "彩信余额不足！";
                }
                spLog.logFailureString(opUser, opModule, opType, opContent
                        + opSper, null, corpCode);
            } else if ("subMmsErrer".equals(re)) {
                // result = "提交彩信任务失败！";

                if (StaticValue.ZH_HK.equals(langName)) {
                    result = "Failed to submit MMS task!";
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    result = "提交彩信任務失敗！";
                } else {
                    result = "提交彩信任务失败！";
                }
                new SuperOpLog().logFailureString(opUser, opModule, opType,
                        opContent + opSper, null, corpCode);
            } else {
                result = re;
                spLog.logFailureString(opUser, opModule, opType, opContent
                        + opSper, null, corpCode);
            }

            // 增加操作日志
            Object loginSysuserObj = request.getSession(false).getAttribute(
                    "loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                String contnet = opContent + "，创建结果：" + result;
                EmpExecutionContext.info("动态彩信发送", loginSysuser.getCorpCode(),
                        String.valueOf(loginSysuser.getUserId()), loginSysuser
                                .getUserName(), contnet, "OTHER");
            }

        } catch (EMPException ex) {
            ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
            String desc = info.getErrorInfo(ex.getMessage());
            request.setAttribute("mmsresult", desc);
            EmpExecutionContext.error(ex, "动态彩信发送提交出现异常！");
        } catch (Exception ex) {
            spLog.logFailureString(opUser, opModule, opType,
                    opContent + opSper, ex, corpCode);
            EmpExecutionContext.error(ex, "动态彩信发送提交出现异常！");
        } finally {
            request.setAttribute("mmsresult", result);
            find(request, response);
        }

    }

    // 验证号码文件是否重复
    public boolean checkRepeat(HashSet<Long> aa, String ee) {
        try {
            Long a = Long.valueOf(ee);
            if (aa.contains(a)) {
                return false;
            } else {
                aa.add(a);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "验证号码文件是否重复出现异常！");
        }
        return true;
    }

    // 判断txt文本编码
    public String get_charset(InputStream inp) throws Exception {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        BufferedInputStream bis = new BufferedInputStream(inp);
        bis.mark(0);
        int read = bis.read(first3Bytes, 0, 3);
        if (read == -1) {
            return charset;
        }
        if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
            charset = "Unicode";
        } else if (first3Bytes[0] == (byte) 0xFE
                && first3Bytes[1] == (byte) 0xFF) {
            charset = "UTF-16BE";
            // checked = true;
        } else if (first3Bytes[0] == (byte) 0xEF
                && first3Bytes[1] == (byte) 0xBB
                && first3Bytes[2] == (byte) 0xBF) {
            charset = "UTF-8";
            // checked = true;
        }
        bis.reset();
        return charset;
    }

    private static String getCellFormatValue(XSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case XSSFCell.CELL_TYPE_NUMERIC:
                case XSSFCell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，取得该Cell的Date值
                        Date date = cell.getDateCellValue();
                        // 把Date转换成本地格式的字符串
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        if (c.get(Calendar.HOUR) == 0
                                && c.get(Calendar.MINUTE) == 0
                                && c.get(Calendar.SECOND) == 0) {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd")
                                    .format(date);
                        } else {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                    .format(date);
                        }
                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        // 是否有小数部分（分开处理）
                        if (Math.floor(cell.getNumericCellValue()) == cell
                                .getNumericCellValue()) {
                            cellvalue = String.valueOf((long) cell
                                    .getNumericCellValue());
                        } else {
                            cellvalue = cell.getRawValue();
                        }

                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case XSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getStringCellValue();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    private static String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                    // 判断当前的cell是否为Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，取得该Cell的Date值
                        Date date = cell.getDateCellValue();
                        // 把Date转换成本地格式的字符串
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        if (c.get(Calendar.HOUR) == 0
                                && c.get(Calendar.MINUTE) == 0
                                && c.get(Calendar.SECOND) == 0) {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd")
                                    .format(date);
                        } else {
                            cellvalue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                    .format(date);
                        }
                    }
                    // 如果是纯数字
                    else {
                        // 是否有小数部分（分开处理）
                        if (Math.floor(cell.getNumericCellValue()) == cell
                                .getNumericCellValue()) {
                            cellvalue = String.valueOf((long) cell
                                    .getNumericCellValue());
                        } else {
                            cellvalue = String.valueOf(cell.getNumericCellValue());
                        }
                        // System.out.println(cellvalue);
                    }
                    break;
                case HSSFCell.CELL_TYPE_STRING:
                    // 字符串
                    cellvalue = cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    // 公式
                    cellvalue = cell.getCellFormula();
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    // 空值
                    cellvalue = " ";
                    break;
                case HSSFCell.CELL_TYPE_ERROR:
                    // 故障
                    cellvalue = " ";
                    break;
                default:
                    cellvalue = " ";
                    break;
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    /**
     * 上传号码文件的存放路径处理
     *
     * @param id
     * @param time
     * @return
     */
    public String[] getSaveUrl(int id, Date time) {

        // 获取系统时间
        Calendar calendar = Calendar.getInstance();
        // 获取当前年，月
        String yearstr = String.valueOf(calendar.get(Calendar.YEAR));
        int month = calendar.get(Calendar.MONTH) + 1;
        // 处理小于10的月份
        String monthstr = month > 9 ? String.valueOf(month) : "0"
                + String.valueOf(month);
        String savePath = StaticValue.MMS_MTTASKS + yearstr + "/" + monthstr
                + "/";
        String strNYR = null;
        try {
            new File(txtfileutil.getWebRoot() + savePath).mkdirs();
            strNYR = new TxtFileUtil().getCurNYR();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "动态彩信发送获取号码路径出现异常");
        }

        // 存放路径的数组
        String[] url = new String[5];
        String saveName = "4_" + (int) (id - 0) + "_"
                + (new SimpleDateFormat("yyyyMMddHHmmssS")).format(time)
                + ".txt";
        String logicUrl = savePath + saveName;
        String physicsUrl = txtfileutil.getWebRoot() + logicUrl;
        url[0] = physicsUrl;
        url[1] = logicUrl;
        url[2] = url[0].replace(".txt", "_bad.txt");
        // 预览文件路径
        new TxtFileUtil().makeDir(txtfileutil.getWebRoot()
                + StaticValue.MMS_PREVIEW + strNYR);
        String viewPath = StaticValue.MMS_PREVIEW + strNYR + saveName;
        String viewPhysicsUrl = txtfileutil.getWebRoot() + viewPath;
        url[3] = viewPhysicsUrl.replace(".txt", "_view.txt");
        url[4] = viewPath.replace(".txt", "_view.txt");
        return url;
    }

    /**
     * delete file 删除文件
     *
     * @param fileName
     * @return
     */
    public boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件存在并类型是文件
        if (file.isFile() && file.exists()) {
            // 删除并返回结果
            return file.delete();
        }
        return false;
    }

    /**
     * 读取用来存储预览十条信息的文件并显示
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void readMmsContent(HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
    	 BufferedReader reader = null;
        try {
            // 设置语言
            String langName = (String) request.getSession().getAttribute(
                    StaticValue.LANG_KEY);
            // 预览文件地址
            String url = request.getParameter("url");
            String tmUrl = request.getParameter("tmUrl");
            // 地址处理
            url = txtfileutil.getWebRoot() + url;
            // 发送类型
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(new File(url)), "GBK"));
            // 动态模板短信发送
            // 逐行读取
            String tmp = "";
            int x = 0;
            String mobile = "";
            String content = "";
            Map<String, String> btnMap = (Map<String, String>) request
                    .getSession(false).getAttribute("btnMap");// 按钮权限Map
            CommonVariables commVa = new CommonVariables();
            StringBuffer buffer = new StringBuffer();
            while ((tmp = reader.readLine()) != null) {
                x++;
                tmp = tmp.trim();
                // 内容截取
                mobile = tmp.substring(0, tmp.indexOf(","));
                if (btnMap.get(StaticValue.PHONE_LOOK_CODE) == null
                        && !"".equals(mobile)) {
                    // 无号码的查看权限，需替换手机号码的星号
                    mobile = commVa.replacePhoneNumber(mobile);
                }
                content = tmp.substring(tmp.indexOf(",") + 1);
                // content = content.replace(">", "&gt;");
                content = content.replaceAll("<", "&lt;").replaceAll("<",
                        "&lt;");
                /*
                 * String str = "<tr align ='center'><td>" + x+ "</td><td>" +
                 * mobile+ "</td>" +
                 * "<td id='td"+x+"' style='display:none'>"+content+
                 * "</td><td><a style='cursor:pointer;color: #2970c0;' onclick='doPreview(\""
                 * + tmUrl+ "\","+ 2+ ",\"td"+x+ "\")'>预览</a></td></tr>";
                 */

                String str = null;
                if (StaticValue.ZH_HK.equals(langName)) {
                    str = "<tr align ='center'><td>"
                            + x
                            + "</td><td>"
                            + mobile
                            + "</td>"
                            + "<td id='td"
                            + x
                            + "' style='display:none'>"
                            + content
                            + "</td><td><a style='cursor:pointer;color: #2970c0;' onclick='doPreview(\""
                            + tmUrl + "\"," + 2 + ",\"td" + x
                            + "\")'>Preview</a></td></tr>";
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    str = "<tr align ='center'><td>"
                            + x
                            + "</td><td>"
                            + mobile
                            + "</td>"
                            + "<td id='td"
                            + x
                            + "' style='display:none'>"
                            + content
                            + "</td><td><a style='cursor:pointer;color: #2970c0;' onclick='doPreview(\""
                            + tmUrl + "\"," + 2 + ",\"td" + x
                            + "\")'>預覽</a></td></tr>";
                } else {
                    str = "<tr align ='center'><td>"
                            + x
                            + "</td><td>"
                            + mobile
                            + "</td>"
                            + "<td id='td"
                            + x
                            + "' style='display:none'>"
                            + content
                            + "</td><td><a style='cursor:pointer;color: #2970c0;' onclick='doPreview(\""
                            + tmUrl + "\"," + 2 + ",\"td" + x
                            + "\")'>预览</a></td></tr>";
                }

                buffer.append(str);
            }
            response.getWriter().print(buffer.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "读取用来存储预览十条信息的文件出现异常！");
        }finally{
        	if(reader!=null){
        		reader.close();
        	}
        }
    }

    // 验证关键字
    public void checkBadWords(HttpServletRequest request,
                              HttpServletResponse response) throws IOException {

        // 设置语言
        String langName = request.getParameter("langName");
        // 发送内容
        String text = request.getParameter("text");
        String tmMsg = request.getParameter("tmMsg");
        String corpCode = request.getParameter("lgcorpcode");
        String templateId = request.getParameter("templateId");
        if (text != null && !"".equals(text)) {
            text = text.toUpperCase();
        }
        String badWords = "";
        try {
            LfTemplate template = baseBiz.getById(LfTemplate.class, Long
                    .valueOf(templateId));
            String isokDownload = "";
            if (template != null) {
                String fileUrl = template.getTmMsg();
                // 判断是否使用集群 以及如果不存在该文件
                if (StaticValue.getISCLUSTER() == 1
                        && !txtfileutil.checkFile(fileUrl)) {
                    CommonBiz commBiz = new CommonBiz();
                    // 下载到本地
                    if (!"success".equals(commBiz
                            .downloadFileFromFileCenter(fileUrl))) {
                        isokDownload = "notmsfile";
                    }
                }
            }
            if (!"".equals(isokDownload)) {
                // response.getWriter().print("彩信文件不存在！");
                if (StaticValue.ZH_HK.equals(langName)) {
                    response.getWriter().print("MMS file does not exist!");
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    response.getWriter().print("彩信文件不存在！");
                } else {
                    response.getWriter().print("彩信文件不存在！");
                }

                return;
            }

            badWords = keyWordAtom.checkText(text, corpCode);
            if ("".equals(badWords)) {
                text = mpb.getTmsText(tmMsg);
                if (text == null) {
                    // response.getWriter().print("彩信模板不存在！");

                    if (StaticValue.ZH_HK.equals(langName)) {
                        response.getWriter().print(
                                "MMS template does not exist!");
                    } else if (StaticValue.ZH_TW.equals(langName)) {
                        response.getWriter().print("彩信模板不存在！");
                    } else {
                        response.getWriter().print("彩信模板不存在！");
                    }
                }
                if (text != null && !"".equals(text)) {
                    text = text.toUpperCase();
                    badWords = keyWordAtom.checkText(text, corpCode);
                }
                if (!"".equals(badWords)) {
                    // response.getWriter().print("彩信模板包含关键字："+badWords);
                    if (StaticValue.ZH_HK.equals(langName)) {
                        response.getWriter().print(
                                "MMS template contains the keywords:"
                                        + badWords);
                    } else if (StaticValue.ZH_TW.equals(langName)) {
                        response.getWriter().print("彩信模板包含關鍵字：" + badWords);
                    } else {
                        response.getWriter().print("彩信模板包含关键字：" + badWords);
                    }
                } else {
                    response.getWriter().print("");
                }
            } else {
                // response.getWriter().print("彩信标题包含关键字："+badWords);
                if (StaticValue.ZH_HK.equals(langName)) {
                    response.getWriter().print(
                            "MMS title contains the keywords:" + badWords);
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    response.getWriter().print("彩信標題包含關鍵字：" + badWords);
                } else {
                    response.getWriter().print("彩信标题包含关键字：" + badWords);
                }
            }
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "动态彩信发送验证关键字出现异常！");
        }
    }

    /**
     * 检测文件是否存在
     *
     * @param request
     * @param response
     */
    public void goToFile(HttpServletRequest request,
                         HttpServletResponse response) {
        String url = request.getParameter("url");
        TxtFileUtil tfu = new TxtFileUtil();
        try {
            response.getWriter().print(tfu.checkFile(url));
        } catch (Exception e) {
            // 异常处理
            EmpExecutionContext.error(e, "动态彩信发送检测文件是否存在出现异常！");
        }
    }

    // 跳转到新增页面
    public void doAdd(HttpServletRequest request, HttpServletResponse response) {
        // 模板类型 0是静态 1是动态
        String templateType = request.getParameter("templateType");
        try {
            // 查询有没有彩信账号
            if (StaticValue.getCORPTYPE() == 1) {
                LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                conditionMap
                        .put("corpCode", request.getParameter("lgcorpcode"));
                List<LfMmsAccbind> mmsaccs = baseBiz.getByCondition(
                        LfMmsAccbind.class, conditionMap, null);
                if (mmsaccs != null && mmsaccs.size() > 0) {
                    request.setAttribute("mmsacc", "true");
                } else {
                    request.setAttribute("mmsacc", "false");
                }
            } else {
                request.setAttribute("mmsacc", "true");
            }
            request.setAttribute("templateType", templateType);
            request.getRequestDispatcher(PATH + "/mms_addMmsTemplate.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "跳转新增彩信模板页面出现异常！");
        }
    }

    /**
     * 生成树
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public void getMatelTree(HttpServletRequest request,
                             HttpServletResponse response) throws Exception {
        String busProTree = (String) request.getAttribute("busProTree");
        String corpCode = request.getParameter("lgcorpcode");
        busProTree = new MaterialBiz().getMaterialJosnData2(corpCode);
        request.setAttribute("busProTree", busProTree);
        response.getWriter().print(busProTree);
    }

    // 验证关键字及参数格式是否正确的方法
    public void checkBadWord(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        String words = "";
        String templateType = request.getParameter("templateType");
        try {
            String[] content = request.getParameterValues("content[]");
            // 获取当前登录用户的企业编码
            String corpCode = request.getParameter("lgcorpcode");
            String eg = "#[pP]_[1-9][0-9]*#";
            Map<Integer, Integer> values = new HashMap<Integer, Integer>();
            int paramCnt = 0;
            if (content.length > 0) {
                String s = "";
                for (int i = 0; i < content.length; i++) {
                    content[i] = content[i].replace("\r\n", "");
                    content[i] = content[i].replace("\n", "");
                    JSONObject jo = (JSONObject) JSONValue.parse(content[i]);
                    if (jo.get("textContent") != null
                            && !"".equals(jo.get("textContent").toString())) {
                        // s =
                        // badFilter.checkText(jo.get("textContent").toString());
                        s = keyWordAtom.checkText(jo.get("textContent")
                                .toString(), corpCode);
                        Matcher m = Pattern
                                .compile(
                                        eg,
                                        Pattern.CASE_INSENSITIVE
                                                | Pattern.UNICODE_CASE)
                                .matcher(jo.get("textContent").toString());
                        while (m.find()) {
                            String rstr = m.group();
                            rstr = rstr.toUpperCase();
                            String pc = rstr.substring(rstr.indexOf("#P_") + 3,
                                    rstr.lastIndexOf("#"));
                            int pci = Integer.parseInt(pc);
                            if (pci > paramCnt) {
                                paramCnt = pci;
                            }
                            if (!values.containsKey(pci)) {
                                values.put(pci, pci);
                            }
                        }
                        if (!"".equals(s)) {
                            words += "[第" + (i + 1) + "帧：" + s + "]";
                        }
                    }
                }
            }
            if (words.length() > 0) {
                if (",".equals(words.substring(words.length() - 1))) {
                    words = words.substring(0, words.length() - 1);
                }
            } else {
                if ("1".equals(templateType) && paramCnt > 0) {
                    for (int j = 1; j < paramCnt; j++) {
                        if (!values.containsKey(j)) {
                            words = "Pfalse";
                            return;
                        }
                    }
                    if (paramCnt > 20) {
                        words = "moreParam";
                        return;
                    }
                } else if ("1".equals(templateType)) {
                    words = "noParam";
                }
            }

        } catch (Exception e) {
            EmpExecutionContext.error(e, "验证关键字及参数格式是否正确");
        } finally {
            response.getWriter().print(words);
        }
    }

    /**
     * 新建彩信模板
     *
     * @param request
     * @param response
     */
    public void update(HttpServletRequest request, HttpServletResponse response) {
        // 获取操作员的用户ID
        //String id = request.getParameter("lguserid");
        //漏洞修复 session里获取操作员信息
        String id = SysuserUtil.strLguserid(request);


        String lgcorpcode = request.getParameter("lgcorpcode");
        Long lguserId = null;
        String opUser = "";
        String corpcode = "";
        try {
            lguserId = Long.valueOf(id);
            LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserId);
            // 如果sysuser为空，则返回错误！
            if (sysuser == null) {
                response.getWriter().print("false");
                return;
            }
            opUser = sysuser == null ? "" : sysuser.getUserName();
            corpcode = sysuser.getCorpCode();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "彩信动态发送获取当前操作员对象出现异常！");
        }
        // 操作
        String opType = null;
        String opContent = null;
        // String opT = request.getParameter("opType");
        opType = StaticValue.ADD;
        opContent = "新建彩信模板";
        TmsFile tmsfile = new TmsFile();
        String corpcodedir = lgcorpcode + "/";
        new TxtFileUtil().makeDir(dirUrl + StaticValue.MMS_TEMPLATES
                + corpcodedir);
        String fileName = StaticValue.MMS_TEMPLATES
                + corpcodedir
                + "3_"
                + (int) (lguserId - 0)
                + "_"
                + (new SimpleDateFormat("yyyyMMddHHmmss")).format(Calendar
                .getInstance().getTime()) + ".tms";
        // 模板类型
        String templateType = request.getParameter("templateType");
        // 彩信模板ID
        String mmsTitle = request.getParameter("mt");
        // 特殊符号处理，•存入数据库乱码
        mmsTitle = mmsTitle.replace("•", "．");
        LfTemplate lmt = null;
        // 模板状态
        String state = request.getParameter("s");
        // 内容
        String[] content = request.getParameterValues("cont[]");
        // 模板参数个数
        Integer paramCnt = 0;
        // 获取http路径
        Map<String, String[]> prop = WebgatePropInfo.getProp();
        String httpUrl = prop.get("webgateProp")[0];
        try {

            if (content.length > 0) {
                for (int i = 0; i < content.length; i++) {
                    content[i] = content[i].replace("\r\n", "<brrn/>");
                    content[i] = content[i].replace("\n", "<brn/>");
                    JSONObject jo = (JSONObject) JSONValue.parse(content[i]);
                    FrameItem item = new FrameItem();
                    if (jo.get("ImgUrl") != null
                            && !"".equals(jo.get("ImgUrl").toString())) {
                        item.setImageSrc(dirUrl + jo.get("ImgUrl").toString());
                    }
                    if (jo.get("MusUrl") != null
                            && !"".equals(jo.get("MusUrl").toString())) {
                        item.setAudioSrc(dirUrl + jo.get("MusUrl").toString());
                    }
                    if (jo.get("lasttime") != null
                            && !"".equals(jo.get("lasttime").toString())) {
                        item.setDelayTime(Integer.parseInt(jo.get("lasttime")
                                .toString()));
                    }
                    if (jo.get("textContent") != null
                            && !"".equals(jo.get("textContent").toString())) {
                        String nr = jo.get("textContent").toString();
                        nr = nr.replace("<brrn/>", "\r\n");
                        nr = nr.replace("<brn/>", "\n");
                        item.setTextSrc(nr);

                        String eg = "#[pP]_[1-9][0-9]*#";
                        Matcher m = Pattern
                                .compile(
                                        eg,
                                        Pattern.CASE_INSENSITIVE
                                                | Pattern.UNICODE_CASE)
                                .matcher(nr);
                        while (m.find()) {
                            String rstr = m.group();
                            rstr = rstr.toUpperCase();
                            String pc = rstr.substring(rstr.indexOf("#P_") + 3,
                                    rstr.lastIndexOf("#"));
                            int pci = Integer.parseInt(pc);
                            if (pci > paramCnt) {
                                paramCnt = pci;
                            }
                        }
                    }
                    tmsfile.addFrame(item);
                }
            }
            byte bytes[] = tmsfile.getTmsFileBytes();
            if (bytes.length >= 80 * 1024) {
                response.getWriter().print("overSize");
                return;
            }
            File file = new File(dirUrl + fileName);
            OutputStream out = null;
            out = new FileOutputStream(file);
            out.write(bytes);
            out.close();

            boolean isuploadFileServerSuccess = false;
            if (StaticValue.getISCLUSTER() == 1) {
                CommonBiz commBiz = new CommonBiz();
                // 上传文件到文件服务器
                if ("success".equals(commBiz.uploadFileToFileCenter(fileName))) {
                    isuploadFileServerSuccess = true;
                }
            }

            // 不集群或者集群上传文件成功，集群上传文件失败则会提示操作失败
            if (StaticValue.getISCLUSTER() != 1 || isuploadFileServerSuccess == true) {
                lmt = new LfTemplate();
                // 模板名称
                lmt.setTmName(mmsTitle);
                // 是否审核(无需审核-0，未审核-1，同意1，拒绝2)
                lmt.setIsPass(-1);
                // 模板状态（0无效，1有效，2草稿）
                lmt.setTmState(Long.parseLong(state));
                // 添加时间
                lmt.setAddtime(Timestamp.valueOf(new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss").format(new Date())));
                // 模板内容
                lmt.setTmMsg(fileName);
                // 操作员ID
                lmt.setUserId(lguserId);
                lmt.setCorpCode(lgcorpcode);
                // 模板（3-短信模板;4-彩信模板）
                lmt.setTmpType(new Integer("4"));
                lmt.setParamcnt(paramCnt);
                lmt.setDsflag(Long.parseLong(templateType));
                // 网关审核状态
                lmt.setAuditstatus(-1);
                // 网关彩信模板状态
                lmt.setTmplstatus(0);
                String emp_templid = getEmpTemplateLid();
                lmt.setEmptemplid(emp_templid);
                lmt.setSubmitstatus(0);
                MmsTemplate mmstl = new MmsTemplate();// 网关平台彩信模板对象
                mmstl.setUserId(lguserId.toString()); // 创建人
                if (StaticValue.getCORPTYPE() == 1) {
                    LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
                    conditionMap.put("corpCode", lgcorpcode);
                    List<LfMmsAccbind> mmsaccs = baseBiz.getByCondition(
                            LfMmsAccbind.class, conditionMap, null);
                    if (mmsaccs != null && mmsaccs.size() > 0) {
                        mmstl.setUserId(mmsaccs.get(0).getMmsUser());
                    } else {
                        lmt.setTmState(2L);
                    }
                }
                mmstl.setAuditStatus(0);// 审核状态
                mmstl.setTmplStatus(0);// 模板状态
                mmstl.setParamCnt(paramCnt);// 参数个数
                mmstl.setTmplPath(httpUrl + fileName);// 文件路径
                mmstl.setRecvTime(lmt.getAddtime());// 模板接收时间
                // 由""改为空格了
                mmstl.setAuditor(" ");
                mmstl.setAuditTime(Timestamp.valueOf(new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss").format(new Date())));
                mmstl.setRemarks(" ");
                mmstl.setEmptemplid(emp_templid);
                mmstl.setTmplId(0L);
                mmstl.setSubmitstatus(0);
                mmstl.setReServe1("0");
                mmstl.setReServe2("0");
                mmstl.setReServe3("0");
                mmstl.setReServe4("0");
                mmstl.setReServe5("0");

                long result = new DynamicMmsBiz().addTemplate(mmstl, lmt);
                if (result > 0) {
                    new SuperOpLog().logSuccessString(opUser, opModule, opType,
                            opContent, corpcode);
                    if (lmt.getTmState() == 2L) {
                        response.getWriter().print("caogaotrue");
                    } else {
                        response.getWriter().print("true");
                    }
                } else {
                    new SuperOpLog().logFailureString(opUser, opModule, opType,
                            opContent + opSper, null, corpcode);
                    response.getWriter().print("false");
                }
            }
        } catch (Exception e) {
            new SuperOpLog().logFailureString(opUser, opModule, opType,
                    opContent + opSper, e, corpcode);
            EmpExecutionContext.error(e, "新建彩信模板出现异常！");
        }
    }

    /*
     * 获取模板id的方法
     */
    public String getEmpTemplateLid() {
        String code = "";
        try {
            Calendar nowCal = Calendar.getInstance();
            Integer year = nowCal.get(Calendar.YEAR);
            Integer month = nowCal.get(Calendar.MONTH) + 1;
            Integer day = nowCal.get(Calendar.DATE);
            Integer hour = nowCal.get(Calendar.HOUR_OF_DAY);
            Integer minute = nowCal.get(Calendar.MINUTE);
            Integer ss = nowCal.get(Calendar.SECOND);

            String time = year + buP(month) + buP(day) + buP(hour)
                    + buP(minute) + buP(ss);

            String count = GetSxCount.getInstance().getCount().toString();
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            // 彩信模板
            conditionMap.put("tmpType", "4");
            while (true) {
                conditionMap.put("emptemplid", time + count);
                List<LfTemplate> lfTemplates = baseBiz.getByCondition(
                        LfTemplate.class, conditionMap, null);
                if (lfTemplates != null && lfTemplates.size() > 0) {
                    count = GetSxCount.getInstance().getCount().toString();
                } else {
                    break;
                }
            }
            code = time + count;

        } catch (Exception e) {
            EmpExecutionContext.error(e, "获取模板id失败！");
        }
        return code;
    }

    public String buP(Integer s) {
        return s < 10 ? '0' + s.toString() : s.toString();
    }

    /**
     * 动态彩信预览彩信文件
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void getTmMsg(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        try {
            String tmUrl = request.getParameter("tmUrl");
            String paramContent = request.getParameter("paramContent");
            String isokDownload = "";
            // 判断是否使用集群 以及如果不存在该文件
            if (StaticValue.getISCLUSTER() == 1 && !txtfileutil.checkFile(tmUrl)) {
                CommonBiz commBiz = new CommonBiz();
                // 下载到本地
                if (!"success"
                        .equals(commBiz.downloadFileFromFileCenter(tmUrl))) {
                    isokDownload = "notmsfile";
                }
            }
            if ("".equals(isokDownload)) {
                if (tmUrl == null || "".equals(tmUrl)) {
                    response.getWriter().print("");
                } else {
                    String mms = "";
                    if (paramContent != null && !"".equals(paramContent)
                            && !"null".equals(paramContent)) {
                        paramContent = paramContent.replaceAll(",", "，");
                        mms = mpb.getDynTmsFileInfo(tmUrl, paramContent);
                    } else {
                        mms = mpb.getTmsFileInfo(tmUrl);
                    }
                    if (mms != null) {
                        mms = mms.replace("\r\n", "&lt;BR/&gt;");
                        mms = mms.replace("\n", "&lt;BR/&gt;");
                    }
                    response.getWriter().print(mms);
                }
            } else {
                response.getWriter().print("");
            }
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "动态彩信预览彩信文件出现异常！");
        }
    }

    /**
     * 检测文件是否存在
     *
     * @param request
     * @param response
     */
    public void checkMmsFile(HttpServletRequest request,
                             HttpServletResponse response) {
        try {
            TxtFileUtil tfu = new TxtFileUtil();
            String url = request.getParameter("url");
            // 如果是集群，并且文件路径下没有彩信模板，则从文件服务器下载彩信模板。
            if (StaticValue.getISCLUSTER() == 1 && !tfu.checkFile(url)) {
                CommonBiz commBiz = new CommonBiz();
                commBiz.downloadFileFromFileCenter(url);
            }
            response.getWriter().print(tfu.checkFile(url));
        } catch (Exception e) {
            // 异常处理
            EmpExecutionContext.error(e, "检测文件是否存在出现异常！");
        }
    }

    /**
     * 高级设置存为默认
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void setDefault(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {

        // 返回信息
        String result = "fail";
        try {
            //String lguserid = request.getParameter("lguserid");
            //漏洞修复 session里获取操作员信息
            String lguserid = SysuserUtil.strLguserid(request);


            String lgcorpcode = request.getParameter("lgcorpcode");
            String spUser = request.getParameter("spUser");
            String flag = request.getParameter("flag");
            if (flag == null || "".equals(flag)) {
                EmpExecutionContext.error("静态彩讯发送高级设置存为默认参数异常！flag:" + flag);
                response.getWriter().print(result);
                return;
            }
            if (lguserid == null || "".equals(lguserid)) {
                EmpExecutionContext.error("静态彩讯发送高级设置存为默认参数异常！lguserid："
                        + lguserid);
                response.getWriter().print(result);
                return;
            }
            if (spUser == null || "".equals(spUser)) {
                EmpExecutionContext
                        .error("静态彩讯发送高级设置存为默认参数异常！spUser：" + spUser);
                response.getWriter().print(result);
                return;
            }

            // 原记录删除条件
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("userid", lguserid);
            conditionMap.put("flag", flag);

            // 更新对象
            LfDfadvanced lfDfadvanced = new LfDfadvanced();
            lfDfadvanced.setUserid(Long.parseLong(lguserid));
            lfDfadvanced.setSpuserid(spUser);
            lfDfadvanced.setFlag(Integer.parseInt(flag));
            lfDfadvanced
                    .setCreatetime(new Timestamp(System.currentTimeMillis()));

            result = mmsBiz.setDefault(conditionMap, lfDfadvanced);

            // 操作结果
            String opResult = "静态彩讯发送高级设置存为默认失败。";
            if (result != null && "seccuss".equals(result)) {
                opResult = "静态彩讯发送高级设置存为默认成功。";
            }
            // 操作员信息
            LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
            // 操作员姓名
            String opUser = sysuser == null ? "" : sysuser.getUserName();
            // 操作日志信息
            StringBuffer content = new StringBuffer();
            content.append("[SP账号](").append(spUser).append(")");

            // 操作日志
            EmpExecutionContext.info("静态彩讯发送", lgcorpcode, lguserid, opUser,
                    opResult + content.toString(), "OTHER");

            response.getWriter().print(result);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "静态彩讯发送高级设置存为默认异常！");
            response.getWriter().print(result);
        }
    }

}