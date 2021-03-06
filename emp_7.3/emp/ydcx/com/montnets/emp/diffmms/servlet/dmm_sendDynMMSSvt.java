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
 * @datetime 2011-3-15 ??????10:33:27
 * @description
 */
// ????????????
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
    // ?????????
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
    // ??????????????????
    public void find(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long lgguid = null;
            String corpCode = "";
            String userName = "";
            LfSysuser sysUser = null;
            try {
                // ????????????????????????GUID
                // lgguid = Long.valueOf(request.getParameter("lgguid"));
                LfSysuser loginSysuser = (LfSysuser) request.getSession(false)
                        .getAttribute("loginSysuser");
                lgguid = loginSysuser.getUserId();

                // ??????????????????
                // sysUser = baseBiz.getByGuId(LfSysuser.class, lgguid);
                sysUser = baseBiz.getById(LfSysuser.class, lgguid);
                // ??????????????????
                corpCode = sysUser.getCorpCode();
                // ??????????????????????????????
                userName = sysUser.getUserName();
            } catch (Exception e) {
                // ??????????????????
                // ??????????????????
                EmpExecutionContext.error(e, "????????????????????????????????????????????????????????????");
            }

            // ????????????ID
            Long taskId = GlobalVariableBiz.getInstance().getValueByKey(
                    "taskId", 1L);
            request.setAttribute("taskId", taskId);

            LfTemplate lfTemplate = new LfTemplate();
            if (sysUser != null) {
                lfTemplate.setUserId(sysUser.getUserId());
            }
            // ???????????????1??????????????????
            lfTemplate.setDsflag(1L);
            // ?????????????????????
            lfTemplate.setTmState(1L);
            // ?????????????????????????????????
            List<LfTemplate> mmsList = mmsBiz
                    .getMMSTemplateByUserId(lfTemplate);
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("userId", userName);
            // ?????????????????????????????????
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
            // ????????????????????????
            // List<LfMmsAccbind> mmsAccbinds = baseBiz.getByCondition(
            // LfMmsAccbind.class, conditionMap, null);
            // ?????????????????????
            new ServerInof().setServerName(getServletContext().getServerInfo());
            LfMmsAccbind lfMmsAccbind = new LfMmsAccbind();
            lfMmsAccbind.setCorpCode(corpCode);
            List<LfMmsAccbind> mmsAccbinds = mmsBiz.getMmsSpUser(lfMmsAccbind);
            request.setAttribute("mmsAccbinds", mmsAccbinds);

            request.setAttribute("mmsTempList", mmsList);

            // ??????????????????????????????
            conditionMap.clear();
            conditionMap.put("userid", sysUser!=null?sysUser.getUserId().toString():"");
            // 10?????????????????????
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
            EmpExecutionContext.error(e, "?????????????????????????????????????????????");
        }
    }

    /**
     * ??????????????????
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void preview(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // ????????????
        String langName = request.getParameter("langName");
        String result = "";
        request.setCharacterEncoding("UTF-8");

        // ????????????
        String lgcorpcode = "";
        String tmMsg = null;
        // ????????????
        int effCount = 0;
        // ???????????????
        Long subCount = 0l;
        Long badCount = 0l;
        Long badModeCount = 0l;
        // ??????????????????
        Long repeatCount = 0l;
        // ???????????????
        Long blackCount = 0l;
        // ?????????????????????
        Long kwCount = 0l;

        // ????????????
        String dtMsg = null;
        String smsContent = null;
        int index;
        // ????????????????????????
        int templateCount = 0;
        int conCount = 0;

        // ???????????????????????????
        TxtFileUtil txtfileutil = new TxtFileUtil();

        // ??????
        Date time = Calendar.getInstance().getTime();

        StringBuffer contentSb = new StringBuffer();
        StringBuffer badContentSb = new StringBuffer();
        // ?????????????????????????????????
        StringBuffer viewContentSb = new StringBuffer();
        boolean isOverSize = false;
        // ????????????????????????
        long maxSize = 100 * 1024L * 1024L;
        long zipSize = 10 * 1024L * 1024L;
        // ??????????????????
        Long userId = 0L;
        File zipUrl = null;
        File Newfile = null;
        String spuser = "";
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            // ????????????????????????
            String[] haoduan = wb.getHaoduan();
            HashSet<Long> repeatList = new HashSet<Long>();

            DiskFileItemFactory factory = new DiskFileItemFactory();
            // ????????????????????????
            factory.setSizeThreshold(1024 * 1024);
            // ??????????????????????????????
            /*
             * String temp = dirUrl + File.separator + "fileUpload" +
             * File.separator + "mms" + File.separator + "mttasks";
             */
            // ?????????????????????id
            //Long lguserid = Long.parseLong(request.getParameter("lguserid"));
            //???????????? session????????????????????????
            Long lguserid = SysuserUtil.longLguserid(request);


            // ??????????????????????????????
            String[] url = new SameMmsBiz().getSaveUrl((int) (lguserid - 0),
                    time);
            // ????????????????????????
            // ??????????????????
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
            // ??????
            // String[] url = null;
            Iterator<FileItem> it = fileList.iterator();
            while (it.hasNext()) {
                FileItem fileItem = (FileItem) it.next();
                // ?????????
                String fileName = fileItem.getFieldName();
                if (fileName.equals("lgcorpcode")) {
                    // ????????????????????????
                    lgcorpcode = fileItem.getString("UTF-8").toString();

                } else if (fileName.equals("lguserid")) {
                    // ????????????userid
                    userId = Long.valueOf(fileItem.getString("UTF-8")
                            .toString());
                    url = getSaveUrl((int) (userId - 0), time);
                } else if (fileName.equals("mmsUser")) {
                    // ????????????userid
                    spuser = fileItem.getString("UTF-8").toString();
                } else if (fileName.equals("tmUrl")) {
                    tmMsg = fileItem.getString("UTF-8").toString();
                    dtMsg = mpb.getTmsText(tmMsg);
                    // tms???????????????
                    if (dtMsg == null) {
                        result = "noTmsMsg";
                        return;
                    }
                    // ????????????????????????????????????
                    templateCount = getTemplateCount(templateCount, dtMsg);
                } else if (!fileItem.isFormField()
                        && fileItem.getName().length() > 0) {

                    // ????????????????????????????????????
                    if (fileItem.getSize() > maxSize) {
                        fileItem.delete();
                        fileItem = null;
                        isOverSize = true;
                        break;
                    }

                    List<BufferedReader> readerList = new ArrayList<BufferedReader>();
                    // ?????????????????????
                    String fileCurName = fileItem.getName();
                    // ????????????
                    String fileType = fileCurName.substring(
                            fileCurName.lastIndexOf(".")).toLowerCase();
                    //??????????????????????????????
                    if (!fileType.equals(".rar") && !fileType.equals(".zip") && !fileType.equals(".xls")
                            && !fileType.equals(".et") && !fileType.equals(".xlsx") && !fileType.equals(".txt")) {
                        // ?????????????????????
                        EmpExecutionContext.error("??????????????????????????????????????????????????????????????????userId???" + lguserid + "???errCode:" + ErrorCodeInfo.V10003);
                        throw new EMPException(ErrorCodeInfo.V10003);
                    }
                    int fileIndex = 0;
                    // ?????????zip??????
                    if (fileType.equals(".zip")) {
                        // ???????????????????????????
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
                                        EmpExecutionContext.error("??????????????????");
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
                                    // ???????????????
                                    for (int sheetNum = 0; sheetNum < workbook
                                            .getNumberOfSheets(); sheetNum++) {
                                        HSSFSheet sheet = workbook
                                                .getSheetAt(sheetNum);
                                        // ???????????????
                                        for (int rowNum = 0; rowNum <= sheet
                                                .getLastRowNum(); rowNum++) {
                                            HSSFRow row = sheet.getRow(rowNum);
                                            if (row == null) {
                                                continue;
                                            }
                                            // ??????????????????????????????
                                            phoneNum = getCellFormatValue(row
                                                    .getCell(0));
                                            Context = "";
                                            // ???????????????????????????,???????????????
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
                                            // ?????????????????????????????????txt????????????
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
                                            .error(e, "????????????????????????????????????");
                                } catch (IOException e) {
                                    EmpExecutionContext.error(e, "??????io??????????????????");
                                } catch (Exception e) {
                                    EmpExecutionContext.error(e,
                                            "?????????????????????????????????????????????");
                                }

                            }
                            // ?????????.xlsx??????
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
                                    // ???????????????
                                    for (int sheetNum = 0; sheetNum < workbook
                                            .getNumberOfSheets(); sheetNum++) {
                                        XSSFSheet sheet = workbook
                                                .getSheetAt(sheetNum);
                                        // ???????????????
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
                                            .error(e, "????????????????????????????????????");
                                } catch (IOException e) {
                                    EmpExecutionContext.error(e, "??????io??????????????????");
                                } catch (Exception e) {
                                    EmpExecutionContext.error(e,
                                            "?????????????????????????????????????????????");
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
                            // ???????????????
                            for (int sheetNum = 0; sheetNum < workbook
                                    .getNumberOfSheets(); sheetNum++) {
                                HSSFSheet sheet = workbook.getSheetAt(sheetNum);
                                // ???????????????
                                for (int rowNum = 0; rowNum <= sheet
                                        .getLastRowNum(); rowNum++) {
                                    HSSFRow row = sheet.getRow(rowNum);
                                    if (row == null) {
                                        continue;
                                    }
                                    // ??????????????????????????????
                                    phoneNum = getCellFormatValue(row
                                            .getCell(0));
                                    Context = "";
                                    // ???????????????????????????,???????????????
                                    for (int k = 1; k < row.getLastCellNum(); k++) {
                                        HSSFCell cell = row.getCell(k);
                                        if (cell != null
                                                && cell.toString().length() > 0) {
                                            Context += ","
                                                    + getCellFormatValue(cell);
                                        }

                                    }
                                    // ?????????????????????????????????txt????????????
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
                            EmpExecutionContext.error(e, "????????????????????????????????????");
                        } catch (IOException e) {
                            EmpExecutionContext.error(e, "??????io??????????????????");
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, "?????????????????????????????????????????????");
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
                        // ??????
                        String Context = "";
                        try {
                            XSSFWorkbook workbook = new XSSFWorkbook(fileItem
                                    .getInputStream());
                            // ???????????????
                            for (int sheetNum = 0; sheetNum < workbook
                                    .getNumberOfSheets(); sheetNum++) {
                                // ???????????????
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
                            EmpExecutionContext.error(e, "????????????????????????????????????");
                        } catch (IOException e) {
                            EmpExecutionContext.error(e, "??????io??????????????????");
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, "?????????????????????????????????????????????");
                        }
                    } else {
                        // ????????????
                        String charset = "utf-8";
                        InputStream instream = fileItem.getInputStream();
                        try {
                            charset = get_charset(instream);
                        } catch (Exception e) {
                            EmpExecutionContext.error(e, "???????????????????????????????????????");
                        }
                        // ?????????????????????????????????
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
                        // ?????????????????????????????????500???
                        for (int r = 0; r < readerList.size(); r++) {
                            // ?????????????????????5000000
                            /*
                             * if (effCount > 5000000) { break;
                             *
                             * }
                             */
                            BufferedReader reader = readerList.get(r);

                            // ????????????
                            while ((tmp = reader.readLine()) != null) {
                                subCount++;
                                mid = 0;
                                tmp = tmp.trim();

                                index = tmp.indexOf(",");
                                if (index != -1) {
                                    phoneNum = StringUtils.parseMobile(tmp
                                            .substring(0, tmp.indexOf(",")));
                                }
                                // ???????????????????????????
                                if (index == -1 || phoneNum.length() != 11) {
                                    mid = 1;
                                } else {
                                    // ??????????????????
                                    // phoneNum = tmp.substring(0, index);
                                    if (wb.checkMobile(phoneNum, haoduan) != 1) {
                                        mid = 1;
                                    } else if (!checkRepeat(repeatList,
                                            phoneNum)) {
                                        // ??????????????????
                                        mid = 3;
                                    } else if (blBiz.checkMmsBlackList(
                                            lgcorpcode, phoneNum)) {
                                        // ???????????????
                                        mid = 2;
                                    } else {
                                        // ??????????????????
                                        smsContent = tmp.substring(index + 1)
                                                .trim();
                                        // ??????????????????
                                        if (smsContent.length() == 0) {
                                            mid = 1;
                                        } else {
                                            conCount = smsContent.split(",").length;
                                            if (conCount < templateCount) {
                                                // ?????????????????????????????????????????????????????????????????????????????????????????????
                                                mid = 1;
                                            }/*
                                             * else { //????????????????????????
                                             * smsContent=smsSend
                                             * .combineContentSg(smsContent,
                                             * dtMsg); }
                                             */
                                        }
                                        // ???????????????????????????????????????????????????????????????
                                        if (mid == 0) {
                                            // ???????????????false??????????????????true??????????????????
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
                                                    // ???????????????????????????
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
                                        // badContentSb.append("???????????????").append(tmp)
                                        // .append(line);

                                        if (StaticValue.ZH_HK.equals(langName)) {
                                            badContentSb.append("Illegal format:")
                                                    .append(tmp).append(line);
                                        } else if (StaticValue.ZH_TW
                                                .equals(langName)) {
                                            badContentSb.append("???????????????")
                                                    .append(tmp).append(line);
                                        } else {
                                            badContentSb.append("???????????????")
                                                    .append(tmp).append(line);
                                        }
                                        badModeCount++;
                                        badCount++;
                                        break;
                                    case 2:
                                        // badContentSb.append("??????????????????").append(tmp)
                                        // .append(line);
                                        if (StaticValue.ZH_HK.equals(langName)) {
                                            badContentSb.append(
                                                    "Black list number:").append(
                                                    tmp).append(line);
                                        } else if (StaticValue.ZH_TW
                                                .equals(langName)) {
                                            badContentSb.append("??????????????????").append(
                                                    tmp).append(line);
                                        } else {
                                            badContentSb.append("??????????????????").append(
                                                    tmp).append(line);
                                        }
                                        blackCount++;
                                        badCount++;
                                        break;
                                    case 3:
                                        // badContentSb.append("???????????????").append(tmp)
                                        // .append(line);
                                        if (StaticValue.ZH_HK.equals(langName)) {
                                            badContentSb.append("Repeat number:")
                                                    .append(tmp).append(line);
                                        } else if (StaticValue.ZH_TW
                                                .equals(langName)) {
                                            badContentSb.append("???????????????")
                                                    .append(tmp).append(line);
                                        } else {
                                            badContentSb.append("???????????????")
                                                    .append(tmp).append(line);
                                        }
                                        repeatCount++;
                                        badCount++;
                                        break;
                                    case 4:
                                        // badContentSb.append("??????????????????").append(tmp)
                                        // .append(line);
                                        if (StaticValue.ZH_HK.equals(langName)) {
                                            badContentSb.append(
                                                    "Contains keywords:").append(
                                                    tmp).append(line);
                                        } else if (StaticValue.ZH_TW
                                                .equals(langName)) {
                                            badContentSb.append("??????????????????").append(
                                                    tmp).append(line);
                                        } else {
                                            badContentSb.append("??????????????????").append(
                                                    tmp).append(line);
                                        }
                                        kwCount++;
                                        badCount++;
                                        break;
                                }
                                // 1000????????????????????????
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

                            // ?????????
                            reader.close();
                        }

                    } catch (Exception e) {
                        // ?????????
                        // EmpExecutionContext.error("EBFV005");
                        // ????????????
                        deleteFile(url[0]);
                        // ????????????
                        EmpExecutionContext.error(e, "?????????????????????????????????????????????");
                        throw e;
                    } finally {
            			try{
            				IOUtils.closeReaders(getClass(), readerList);
            			}catch(IOException e){
            				EmpExecutionContext.error(e, "");
            			}
                        // ?????????
                        readerList.clear();
                        repeatList.clear();
                        readerList = null;
                        repeatList = null;
                        // ????????????????????????
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
                                    EmpExecutionContext.error("??????????????????");
                                }
                            }
                        }
                        if (Newfile != null) {
                            boolean delete = Newfile.delete();
                            if (!delete) {
                                EmpExecutionContext.error("??????????????????");
                            }
                        }
                    }
                }
            }

            // ?????????????????????
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false)
                    .getAttribute("loginSysuser");
            // ???????????????????????????
            if (lfSysuser == null) {
                EmpExecutionContext
                        .error("????????????????????????session????????????????????????????????????lfSysuser???null???errCode???"
                                + IErrorCode.V10001);
                return;
            }
            // ??????????????????????????? ???????????????????????????SP????????????
            boolean checkFlag = new CheckUtil().checkMmsSysuserInCorp(
                    lfSysuser, lgcorpcode, spuser, null);
            if (!checkFlag) {
                EmpExecutionContext.error("??????????????????????????????????????????????????????????????????????????????"
                        + "???corpCode:" + lgcorpcode + "???userid???"
                        + lfSysuser.getUserId() + "???spUser???" + spuser
                        + "???errCode:" + IErrorCode.V10001);
                return;
            }

            // ?????????????????????????????????
            if (isOverSize) {
                result = "overSize";
            }
            // ??????????????????????????????
            else if (effCount > MAX_SIZE) {
                for (int i = 0; i < url.length; i++) {
                    deleteFile(url[i]);
                }
                result = "overstep";
            } else {

                txtfileutil.writeToTxtFile(url[0], contentSb.toString());

                // ????????????????????????????????????,??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????.
                Long maxcount = 0L;
                int yct = effCount;
                boolean isCharg = true; // ???????????????????????????true;???????????????false;
                String spFeeResult = balanceLogBiz.checkGwFee(spuser, effCount,
                        lgcorpcode, false, 2);
                if (balanceLogBiz.IsChargings(userId)
                        && ("koufeiSuccess".equals(spFeeResult) || "notneedtocheck"
                        .equals(spFeeResult))) {
                    String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                            .format(new Date());
                    try {
                        // ???????????????????????????????????????????????????.
                        maxcount = balanceLogBiz
                                .getAllowMmsAmountByUserId(userId);
                        // maxcount=biz.getAllowSmsAmount(lfSysuser);
                        // ???????????????????????????????????????????????????.
                        if (maxcount == null) {
                            maxcount = 0L;
                        }
                        EmpExecutionContext.debug("date:" + dateStr
                                + "     ???????????????:" + effCount);
                    } catch (Exception e) {
                        EmpExecutionContext.error(e, "???????????????????????????");
                        maxcount = 0L;
                        yct = 0;
                    }
                } else {
                    isCharg = false;
                }
                // ????????????????????????
                if (badContentSb.length() > 0) {
                    txtfileutil.writeToTxtFile(url[2], badContentSb.toString());
                }
                if (subCount == 0) {
                    // ????????????????????????
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
            // ????????????
            result = "error";
            // ????????????
            EmpExecutionContext.error(ex, "???????????????????????????????????????");
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
            // ????????????????????????
            request.setAttribute("result", result);
            // ???????????????????????????????????????????????????
            request.getRequestDispatcher(PATH + "/dmm_preview.jsp").forward(
                    request, response);
        }
    }

    /**
     * ??????????????????????????????
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
            // ??????????????????GUID
            userGuid = Long.valueOf(lgguid);
            // ?????????????????????
            sysUser = baseBiz.getByGuId(LfSysuser.class, userGuid);
            if (sysUser != null) {
                // ??????????????????
                corpCode = sysUser.getCorpCode();
                // ????????????ID
                userId = sysUser.getUserId();
                opUser = sysUser == null ? "" : sysUser.getUserName();
            } else {
                EmpExecutionContext.error("????????????????????????????????????????????????????????????");
                request.setAttribute("mmsresult", ErrorCodeInfo.getInstance(
                        langName).getErrorInfo(IErrorCode.V10001));
                find(request, response);
                return;
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "????????????????????????????????????????????????????????????");
            request.setAttribute("mmsresult", ErrorCodeInfo.getInstance(
                    langName).getErrorInfo(IErrorCode.V10001));
            find(request, response);
            return;
        }
        // ????????????ID
        String taskId = request.getParameter("taskId");
        String taskName = request.getParameter("taskName");
        // ??????????????????,????????????(????????????)
        if (taskName != null && "???????????????????????????".equals(taskName.trim())) {
            EmpExecutionContext.error("???????????????????????????????????????" + "taskName:" + taskName
                    + "???taskId???" + taskId);
            request.setAttribute("mmsresult", ErrorCodeInfo.getInstance(
                    langName).getErrorInfo(IErrorCode.V10001));
            find(request, response);
            return;
        }
        // ?????????????????????????????????????????????
        taskName = taskName.replace("???", "???");
        // 1??????
        String mmsUser = request.getParameter("mmsUser");
        String theme = request.getParameter("tmName");
        // ?????????????????????????????????????????????
        theme = theme.replace("???", "???");
        String result = null;
        String timeType = request.getParameter("sendType");
        // String subType = request.getParameter("subType");
        // ????????????GUID

        // ???????????????ID
        // String mmsTemplateId= request.getParameter("tmMsg");
        String mmsTemplateId = request.getParameter("mmstemplateid");

        String phoneFileUrl = request.getParameter("mobileUrl");
        // ????????????
        String subCount = request.getParameter("subCount");
        // ???????????????
        String effCount = request.getParameter("effCount");

        String opType = null;
        String opContent = null;

        try {
            // ?????????????????????
            LfSysuser lfSysuser = (LfSysuser) request.getSession(false)
                    .getAttribute("loginSysuser");
            // ???????????????????????????
            if (lfSysuser == null) {
                EmpExecutionContext
                        .error("????????????????????????session????????????????????????????????????lfSysuser???null???errCode???"
                                + IErrorCode.V10001);
                request.setAttribute("mmsresult", ErrorCodeInfo.getInstance(
                        langName).getErrorInfo(IErrorCode.V10001));
                find(request, response);
                return;
            }
            // ??????????????????????????? ???????????????????????????SP????????????
            boolean checkFlag = new CheckUtil().checkMmsSysuserInCorp(
                    lfSysuser, corpCode, mmsUser, null);
            if (!checkFlag) {
                EmpExecutionContext.error("?????????????????????????????????????????????????????????????????????????????????taskid:"
                        + taskId + "???corpCode:" + corpCode + "???userid???"
                        + userId + "???spUser???" + mmsUser + "???errCode:"
                        + IErrorCode.V10001);
                request.setAttribute("mmsresult", ErrorCodeInfo.getInstance(
                        langName).getErrorInfo(IErrorCode.V10001));
                find(request, response);
                return;
            }

            opType = StaticValue.ADD;
            // "??????"
            opContent = "??????????????????";
            // ????????????
            opContent += "??????????????????" + taskName + "???";
            LfMttask lfMttask = new LfMttask();
            // ???????????? 1-????????? 0-?????????
            lfMttask.setIsRetry(0);
            // ???????????????1-?????? ;2-?????????
            lfMttask.setMsType(2);
            // ????????????
            taskName = taskName.replaceAll("???", "???");
            lfMttask.setTaskName(taskName);
            // ????????????
            theme = theme.replaceAll("???", "???");
            lfMttask.setTitle(theme);
            // ????????????
            lfMttask.setSpUser(mmsUser);
            // ?????????????????? 10????????????,11??????????????????,12??????????????????
            // ??????????????????
            lfMttask.setBmtType(12);
            // ??????????????????ID
            LfTemplate lfTemplate = baseBiz.getById(LfTemplate.class, Long
                    .parseLong(mmsTemplateId));
            lfMttask.setMsg(String.valueOf(lfTemplate.getSptemplid()));
            // ??????????????????
            lfMttask.setTmplPath(lfTemplate.getTmMsg());
            // ????????????(?????????1?????????2?????????3)
            lfMttask.setSubState(2);
            // ????????????(0-?????????????????????????????????1-???????????????;2-??????;3-?????????;4-?????????)
            lfMttask.setSendstate(0);
            // MOBILE_TYPE ???????????????????????????1???????????????0???
            lfMttask.setMobileType(1);
            // ???????????????URL
            lfMttask.setMobileUrl(phoneFileUrl);
            // ????????????
            lfMttask.setSubCount(Long.parseLong(subCount));
            // ???????????????
            lfMttask.setEffCount(Long.parseLong(effCount));
            // ?????????ID
            lfMttask.setUserId(userId);
            // ????????????
            lfMttask.setCorpCode(corpCode);
            // ????????????
            lfMttask.setSubmitTime(Timestamp.valueOf(new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss").format(new Date())));
            // ?????????????????? 1??????0??????
            // ????????????????????????
            if ("1".equals(timeType)) {
                lfMttask.setTimerStatus(1);
                String stime = request.getParameter("sendtime");
                stime = stime + ":00";
                lfMttask
                        .setTimerTime(new Timestamp(sdf.parse(stime).getTime()));
            } else {
                lfMttask.setTimerStatus(0);
            }
            // ??????taskID
            lfMttask.setTaskId(Long.parseLong(taskId));

            String re = "";
            // ????????????????????????
            if (StaticValue.getISCLUSTER() == 1) {
                CommonBiz commBiz = new CommonBiz();
                // ??????????????????????????????
                // if("success".equals(commBiz.uploadFileToFileCenter(phoneFileUrl)))
                // {
                // //??????????????????
                // //commBiz.deleteFile(phoneFileUrl);
                // }else
                // {
                // re = "????????????????????????????????????????????????";
                // }
                // ???????????????????????????
                lfMttask.setFileuri(commBiz
                        .uploadFileToFileServer(phoneFileUrl));
            } else {
                // ?????????????????????
                lfMttask.setFileuri(StaticValue.BASEURL);
            }
            if ("".equals(re)) {
                re = mmsTaskBiz.addMmsLfMttask(lfMttask);
            }
            if ("createSuccess".equals(re)) {
                // result = "????????????????????????????????????????????????";

                if (StaticValue.ZH_HK.equals(langName)) {
                    result = "Create MMS tasks and submitted to the approval flow success!";
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    result = "????????????????????????????????????????????????";
                } else {
                    result = "????????????????????????????????????????????????";
                }
                spLog.logSuccessString(opUser, opModule, opType, opContent,
                        corpCode);
            } else if ("saveSuccess".equals(re)) {
                // result = "??????????????????";

                if (StaticValue.ZH_HK.equals(langName)) {
                    result = "Saved draft success!";
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    result = "??????????????????";
                } else {
                    result = "??????????????????";
                }
                spLog.logSuccessString(opUser, opModule, opType, opContent,
                        corpCode);
            } else if ("000".equals(re)) {
                // result = "?????????????????????????????????????????????";
                // ??????????????????????????????????????????
                result = "000&" + taskId;
                spLog.logSuccessString(opUser, opModule, opType, opContent,
                        corpCode);
            } else if ("timerSuccess".equals(re)) {
                // result = "????????????????????????????????????????????????";

                if (StaticValue.ZH_HK.equals(langName)) {
                    result = "Create MMS tasks and timing tasks to add success!";
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    result = "????????????????????????????????????????????????";
                } else {
                    result = "????????????????????????????????????????????????";
                }
                spLog.logSuccessString(opUser, opModule, opType, opContent,
                        corpCode);
            } else if ("timerFail".equals(re)) {
                // result = "????????????????????????????????????????????????";

                if (StaticValue.ZH_HK.equals(langName)) {
                    result = "Create a scheduled task failed, cancel the task to create!";
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    result = "????????????????????????????????????????????????";
                } else {
                    result = "????????????????????????????????????????????????";
                }
                spLog.logFailureString(opUser, opModule, opType, opContent
                        + opSper, null, corpCode);
            } else if ("timeError".equals(re)) {
                // result = "??????????????????????????????????????????????????????????????????";

                if (StaticValue.ZH_HK.equals(langName)) {
                    result = "Failed to create a scheduled task, timing time shall not be less than the current time!";
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    result = "??????????????????????????????????????????????????????????????????";
                } else {
                    result = "??????????????????????????????????????????????????????????????????";
                }
                spLog.logFailureString(opUser, opModule, opType, opContent
                        + opSper, null, corpCode);
            } else if ("mmsyuebuzu".equals(re)) {
                // result = "?????????????????????";

                if (StaticValue.ZH_HK.equals(langName)) {
                    result = "MMS insufficient balance!";
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    result = "?????????????????????";
                } else {
                    result = "?????????????????????";
                }
                spLog.logFailureString(opUser, opModule, opType, opContent
                        + opSper, null, corpCode);
            } else if ("subMmsErrer".equals(re)) {
                // result = "???????????????????????????";

                if (StaticValue.ZH_HK.equals(langName)) {
                    result = "Failed to submit MMS task!";
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    result = "???????????????????????????";
                } else {
                    result = "???????????????????????????";
                }
                new SuperOpLog().logFailureString(opUser, opModule, opType,
                        opContent + opSper, null, corpCode);
            } else {
                result = re;
                spLog.logFailureString(opUser, opModule, opType, opContent
                        + opSper, null, corpCode);
            }

            // ??????????????????
            Object loginSysuserObj = request.getSession(false).getAttribute(
                    "loginSysuser");
            if (loginSysuserObj != null) {
                LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
                String contnet = opContent + "??????????????????" + result;
                EmpExecutionContext.info("??????????????????", loginSysuser.getCorpCode(),
                        String.valueOf(loginSysuser.getUserId()), loginSysuser
                                .getUserName(), contnet, "OTHER");
            }

        } catch (EMPException ex) {
            ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
            String desc = info.getErrorInfo(ex.getMessage());
            request.setAttribute("mmsresult", desc);
            EmpExecutionContext.error(ex, "???????????????????????????????????????");
        } catch (Exception ex) {
            spLog.logFailureString(opUser, opModule, opType,
                    opContent + opSper, ex, corpCode);
            EmpExecutionContext.error(ex, "???????????????????????????????????????");
        } finally {
            request.setAttribute("mmsresult", result);
            find(request, response);
        }

    }

    // ??????????????????????????????
    public boolean checkRepeat(HashSet<Long> aa, String ee) {
        try {
            Long a = Long.valueOf(ee);
            if (aa.contains(a)) {
                return false;
            } else {
                aa.add(a);
            }
        } catch (Exception e) {
            EmpExecutionContext.error(e, "?????????????????????????????????????????????");
        }
        return true;
    }

    // ??????txt????????????
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
            // ????????????Cell???Type
            switch (cell.getCellType()) {
                // ????????????Cell???Type???NUMERIC
                case XSSFCell.CELL_TYPE_NUMERIC:
                case XSSFCell.CELL_TYPE_FORMULA: {
                    // ???????????????cell?????????Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // ?????????Date?????????????????????Cell???Date???
                        Date date = cell.getDateCellValue();
                        // ???Date?????????????????????????????????
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
                    // ??????????????????
                    else {
                        // ????????????Cell?????????
                        // ???????????????????????????????????????
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
                // ????????????Cell???Type???STRIN
                case XSSFCell.CELL_TYPE_STRING:
                    // ???????????????Cell?????????
                    cellvalue = cell.getStringCellValue();
                    break;
                // ?????????Cell???
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
            // ????????????Cell???Type
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC: // ??????
                    // ???????????????cell?????????Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // ?????????Date?????????????????????Cell???Date???
                        Date date = cell.getDateCellValue();
                        // ???Date?????????????????????????????????
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
                    // ??????????????????
                    else {
                        // ???????????????????????????????????????
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
                    // ?????????
                    cellvalue = cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    // ??????
                    cellvalue = cell.getCellFormula();
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    // ??????
                    cellvalue = " ";
                    break;
                case HSSFCell.CELL_TYPE_ERROR:
                    // ??????
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
     * ???????????????????????????????????????
     *
     * @param id
     * @param time
     * @return
     */
    public String[] getSaveUrl(int id, Date time) {

        // ??????????????????
        Calendar calendar = Calendar.getInstance();
        // ?????????????????????
        String yearstr = String.valueOf(calendar.get(Calendar.YEAR));
        int month = calendar.get(Calendar.MONTH) + 1;
        // ????????????10?????????
        String monthstr = month > 9 ? String.valueOf(month) : "0"
                + String.valueOf(month);
        String savePath = StaticValue.MMS_MTTASKS + yearstr + "/" + monthstr
                + "/";
        String strNYR = null;
        try {
            new File(txtfileutil.getWebRoot() + savePath).mkdirs();
            strNYR = new TxtFileUtil().getCurNYR();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "????????????????????????????????????????????????");
        }

        // ?????????????????????
        String[] url = new String[5];
        String saveName = "4_" + (int) (id - 0) + "_"
                + (new SimpleDateFormat("yyyyMMddHHmmssS")).format(time)
                + ".txt";
        String logicUrl = savePath + saveName;
        String physicsUrl = txtfileutil.getWebRoot() + logicUrl;
        url[0] = physicsUrl;
        url[1] = logicUrl;
        url[2] = url[0].replace(".txt", "_bad.txt");
        // ??????????????????
        new TxtFileUtil().makeDir(txtfileutil.getWebRoot()
                + StaticValue.MMS_PREVIEW + strNYR);
        String viewPath = StaticValue.MMS_PREVIEW + strNYR + saveName;
        String viewPhysicsUrl = txtfileutil.getWebRoot() + viewPath;
        url[3] = viewPhysicsUrl.replace(".txt", "_view.txt");
        url[4] = viewPath.replace(".txt", "_view.txt");
        return url;
    }

    /**
     * delete file ????????????
     *
     * @param fileName
     * @return
     */
    public boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // ????????????????????????????????????
        if (file.isFile() && file.exists()) {
            // ?????????????????????
            return file.delete();
        }
        return false;
    }

    /**
     * ??????????????????????????????????????????????????????
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
            // ????????????
            String langName = (String) request.getSession().getAttribute(
                    StaticValue.LANG_KEY);
            // ??????????????????
            String url = request.getParameter("url");
            String tmUrl = request.getParameter("tmUrl");
            // ????????????
            url = txtfileutil.getWebRoot() + url;
            // ????????????
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(new File(url)), "GBK"));
            // ????????????????????????
            // ????????????
            String tmp = "";
            int x = 0;
            String mobile = "";
            String content = "";
            Map<String, String> btnMap = (Map<String, String>) request
                    .getSession(false).getAttribute("btnMap");// ????????????Map
            CommonVariables commVa = new CommonVariables();
            StringBuffer buffer = new StringBuffer();
            while ((tmp = reader.readLine()) != null) {
                x++;
                tmp = tmp.trim();
                // ????????????
                mobile = tmp.substring(0, tmp.indexOf(","));
                if (btnMap.get(StaticValue.PHONE_LOOK_CODE) == null
                        && !"".equals(mobile)) {
                    // ?????????????????????????????????????????????????????????
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
                 * + tmUrl+ "\","+ 2+ ",\"td"+x+ "\")'>??????</a></td></tr>";
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
                            + "\")'>??????</a></td></tr>";
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
                            + "\")'>??????</a></td></tr>";
                }

                buffer.append(str);
            }
            response.getWriter().print(buffer.toString());
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "????????????????????????????????????????????????????????????");
        }finally{
        	if(reader!=null){
        		reader.close();
        	}
        }
    }

    // ???????????????
    public void checkBadWords(HttpServletRequest request,
                              HttpServletResponse response) throws IOException {

        // ????????????
        String langName = request.getParameter("langName");
        // ????????????
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
                // ???????????????????????? ??????????????????????????????
                if (StaticValue.getISCLUSTER() == 1
                        && !txtfileutil.checkFile(fileUrl)) {
                    CommonBiz commBiz = new CommonBiz();
                    // ???????????????
                    if (!"success".equals(commBiz
                            .downloadFileFromFileCenter(fileUrl))) {
                        isokDownload = "notmsfile";
                    }
                }
            }
            if (!"".equals(isokDownload)) {
                // response.getWriter().print("????????????????????????");
                if (StaticValue.ZH_HK.equals(langName)) {
                    response.getWriter().print("MMS file does not exist!");
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    response.getWriter().print("????????????????????????");
                } else {
                    response.getWriter().print("????????????????????????");
                }

                return;
            }

            badWords = keyWordAtom.checkText(text, corpCode);
            if ("".equals(badWords)) {
                text = mpb.getTmsText(tmMsg);
                if (text == null) {
                    // response.getWriter().print("????????????????????????");

                    if (StaticValue.ZH_HK.equals(langName)) {
                        response.getWriter().print(
                                "MMS template does not exist!");
                    } else if (StaticValue.ZH_TW.equals(langName)) {
                        response.getWriter().print("????????????????????????");
                    } else {
                        response.getWriter().print("????????????????????????");
                    }
                }
                if (text != null && !"".equals(text)) {
                    text = text.toUpperCase();
                    badWords = keyWordAtom.checkText(text, corpCode);
                }
                if (!"".equals(badWords)) {
                    // response.getWriter().print("??????????????????????????????"+badWords);
                    if (StaticValue.ZH_HK.equals(langName)) {
                        response.getWriter().print(
                                "MMS template contains the keywords:"
                                        + badWords);
                    } else if (StaticValue.ZH_TW.equals(langName)) {
                        response.getWriter().print("??????????????????????????????" + badWords);
                    } else {
                        response.getWriter().print("??????????????????????????????" + badWords);
                    }
                } else {
                    response.getWriter().print("");
                }
            } else {
                // response.getWriter().print("??????????????????????????????"+badWords);
                if (StaticValue.ZH_HK.equals(langName)) {
                    response.getWriter().print(
                            "MMS title contains the keywords:" + badWords);
                } else if (StaticValue.ZH_TW.equals(langName)) {
                    response.getWriter().print("??????????????????????????????" + badWords);
                } else {
                    response.getWriter().print("??????????????????????????????" + badWords);
                }
            }
        } catch (Exception e) {
            response.getWriter().print("");
            EmpExecutionContext.error(e, "????????????????????????????????????????????????");
        }
    }

    /**
     * ????????????????????????
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
            // ????????????
            EmpExecutionContext.error(e, "?????????????????????????????????????????????????????????");
        }
    }

    // ?????????????????????
    public void doAdd(HttpServletRequest request, HttpServletResponse response) {
        // ???????????? 0????????? 1?????????
        String templateType = request.getParameter("templateType");
        try {
            // ???????????????????????????
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
            EmpExecutionContext.error(e, "?????????????????????????????????????????????");
        }
    }

    /**
     * ?????????
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

    // ???????????????????????????????????????????????????
    public void checkBadWord(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        String words = "";
        String templateType = request.getParameter("templateType");
        try {
            String[] content = request.getParameterValues("content[]");
            // ???????????????????????????????????????
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
                            words += "[???" + (i + 1) + "??????" + s + "]";
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
            EmpExecutionContext.error(e, "??????????????????????????????????????????");
        } finally {
            response.getWriter().print(words);
        }
    }

    /**
     * ??????????????????
     *
     * @param request
     * @param response
     */
    public void update(HttpServletRequest request, HttpServletResponse response) {
        // ????????????????????????ID
        //String id = request.getParameter("lguserid");
        //???????????? session????????????????????????
        String id = SysuserUtil.strLguserid(request);


        String lgcorpcode = request.getParameter("lgcorpcode");
        Long lguserId = null;
        String opUser = "";
        String corpcode = "";
        try {
            lguserId = Long.valueOf(id);
            LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserId);
            // ??????sysuser???????????????????????????
            if (sysuser == null) {
                response.getWriter().print("false");
                return;
            }
            opUser = sysuser == null ? "" : sysuser.getUserName();
            corpcode = sysuser.getCorpCode();
        } catch (Exception e) {
            EmpExecutionContext.error(e, "????????????????????????????????????????????????????????????");
        }
        // ??????
        String opType = null;
        String opContent = null;
        // String opT = request.getParameter("opType");
        opType = StaticValue.ADD;
        opContent = "??????????????????";
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
        // ????????????
        String templateType = request.getParameter("templateType");
        // ????????????ID
        String mmsTitle = request.getParameter("mt");
        // ?????????????????????????????????????????????
        mmsTitle = mmsTitle.replace("???", "???");
        LfTemplate lmt = null;
        // ????????????
        String state = request.getParameter("s");
        // ??????
        String[] content = request.getParameterValues("cont[]");
        // ??????????????????
        Integer paramCnt = 0;
        // ??????http??????
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
                // ??????????????????????????????
                if ("success".equals(commBiz.uploadFileToFileCenter(fileName))) {
                    isuploadFileServerSuccess = true;
                }
            }

            // ??????????????????????????????????????????????????????????????????????????????????????????
            if (StaticValue.getISCLUSTER() != 1 || isuploadFileServerSuccess == true) {
                lmt = new LfTemplate();
                // ????????????
                lmt.setTmName(mmsTitle);
                // ????????????(????????????-0????????????-1?????????1?????????2)
                lmt.setIsPass(-1);
                // ???????????????0?????????1?????????2?????????
                lmt.setTmState(Long.parseLong(state));
                // ????????????
                lmt.setAddtime(Timestamp.valueOf(new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss").format(new Date())));
                // ????????????
                lmt.setTmMsg(fileName);
                // ?????????ID
                lmt.setUserId(lguserId);
                lmt.setCorpCode(lgcorpcode);
                // ?????????3-????????????;4-???????????????
                lmt.setTmpType(new Integer("4"));
                lmt.setParamcnt(paramCnt);
                lmt.setDsflag(Long.parseLong(templateType));
                // ??????????????????
                lmt.setAuditstatus(-1);
                // ????????????????????????
                lmt.setTmplstatus(0);
                String emp_templid = getEmpTemplateLid();
                lmt.setEmptemplid(emp_templid);
                lmt.setSubmitstatus(0);
                MmsTemplate mmstl = new MmsTemplate();// ??????????????????????????????
                mmstl.setUserId(lguserId.toString()); // ?????????
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
                mmstl.setAuditStatus(0);// ????????????
                mmstl.setTmplStatus(0);// ????????????
                mmstl.setParamCnt(paramCnt);// ????????????
                mmstl.setTmplPath(httpUrl + fileName);// ????????????
                mmstl.setRecvTime(lmt.getAddtime());// ??????????????????
                // ???""???????????????
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
            EmpExecutionContext.error(e, "?????????????????????????????????");
        }
    }

    /*
     * ????????????id?????????
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
            // ????????????
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
            EmpExecutionContext.error(e, "????????????id?????????");
        }
        return code;
    }

    public String buP(Integer s) {
        return s < 10 ? '0' + s.toString() : s.toString();
    }

    /**
     * ??????????????????????????????
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
            // ???????????????????????? ??????????????????????????????
            if (StaticValue.getISCLUSTER() == 1 && !txtfileutil.checkFile(tmUrl)) {
                CommonBiz commBiz = new CommonBiz();
                // ???????????????
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
                        paramContent = paramContent.replaceAll(",", "???");
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
            EmpExecutionContext.error(e, "?????????????????????????????????????????????");
        }
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     */
    public void checkMmsFile(HttpServletRequest request,
                             HttpServletResponse response) {
        try {
            TxtFileUtil tfu = new TxtFileUtil();
            String url = request.getParameter("url");
            // ??????????????????????????????????????????????????????????????????????????????????????????????????????
            if (StaticValue.getISCLUSTER() == 1 && !tfu.checkFile(url)) {
                CommonBiz commBiz = new CommonBiz();
                commBiz.downloadFileFromFileCenter(url);
            }
            response.getWriter().print(tfu.checkFile(url));
        } catch (Exception e) {
            // ????????????
            EmpExecutionContext.error(e, "???????????????????????????????????????");
        }
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void setDefault(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {

        // ????????????
        String result = "fail";
        try {
            //String lguserid = request.getParameter("lguserid");
            //???????????? session????????????????????????
            String lguserid = SysuserUtil.strLguserid(request);


            String lgcorpcode = request.getParameter("lgcorpcode");
            String spUser = request.getParameter("spUser");
            String flag = request.getParameter("flag");
            if (flag == null || "".equals(flag)) {
                EmpExecutionContext.error("?????????????????????????????????????????????????????????flag:" + flag);
                response.getWriter().print(result);
                return;
            }
            if (lguserid == null || "".equals(lguserid)) {
                EmpExecutionContext.error("?????????????????????????????????????????????????????????lguserid???"
                        + lguserid);
                response.getWriter().print(result);
                return;
            }
            if (spUser == null || "".equals(spUser)) {
                EmpExecutionContext
                        .error("?????????????????????????????????????????????????????????spUser???" + spUser);
                response.getWriter().print(result);
                return;
            }

            // ?????????????????????
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("userid", lguserid);
            conditionMap.put("flag", flag);

            // ????????????
            LfDfadvanced lfDfadvanced = new LfDfadvanced();
            lfDfadvanced.setUserid(Long.parseLong(lguserid));
            lfDfadvanced.setSpuserid(spUser);
            lfDfadvanced.setFlag(Integer.parseInt(flag));
            lfDfadvanced
                    .setCreatetime(new Timestamp(System.currentTimeMillis()));

            result = mmsBiz.setDefault(conditionMap, lfDfadvanced);

            // ????????????
            String opResult = "???????????????????????????????????????????????????";
            if (result != null && "seccuss".equals(result)) {
                opResult = "???????????????????????????????????????????????????";
            }
            // ???????????????
            LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
            // ???????????????
            String opUser = sysuser == null ? "" : sysuser.getUserName();
            // ??????????????????
            StringBuffer content = new StringBuffer();
            content.append("[SP??????](").append(spUser).append(")");

            // ????????????
            EmpExecutionContext.info("??????????????????", lgcorpcode, lguserid, opUser,
                    opResult + content.toString(), "OTHER");

            response.getWriter().print(result);

        } catch (Exception e) {
            EmpExecutionContext.error(e, "???????????????????????????????????????????????????");
            response.getWriter().print(result);
        }
    }

}